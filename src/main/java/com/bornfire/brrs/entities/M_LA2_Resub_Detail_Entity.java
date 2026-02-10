
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
@Table(name = "BRRS_M_LA2_RESUB_DETAILTABLE")
@IdClass(M_LA2_PK.class)
public class M_LA2_Resub_Detail_Entity {
	private String R12_INDUSTRY;
	private BigDecimal R12_TOTAL;
	private String R13_INDUSTRY;
	private BigDecimal R13_TOTAL;
	private String R14_INDUSTRY;
	private BigDecimal R14_TOTAL;
	private String R15_INDUSTRY;
	private BigDecimal R15_TOTAL;
	private String R16_INDUSTRY;
	private BigDecimal R16_TOTAL;
	private String R17_INDUSTRY;
	private BigDecimal R17_TOTAL;
	private String R18_INDUSTRY;
	private BigDecimal R18_TOTAL;
	private String R19_INDUSTRY;
	private BigDecimal R19_TOTAL;
	private String R20_INDUSTRY;
	private BigDecimal R20_TOTAL;
	private String R21_INDUSTRY;
	private BigDecimal R21_TOTAL;
	private String R22_INDUSTRY;
	private BigDecimal R22_TOTAL;
	private String R23_INDUSTRY;
	private BigDecimal R23_TOTAL;
	private String R24_INDUSTRY;
	private BigDecimal R24_TOTAL;
	private String R25_INDUSTRY;
	private BigDecimal R25_TOTAL;
	private String R26_INDUSTRY;
	private BigDecimal R26_TOTAL;
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	
	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	private Date REPORT_RESUBDATE;
	public String getR12_INDUSTRY() {
		return R12_INDUSTRY;
	}
	public void setR12_INDUSTRY(String r12_INDUSTRY) {
		R12_INDUSTRY = r12_INDUSTRY;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public String getR13_INDUSTRY() {
		return R13_INDUSTRY;
	}
	public void setR13_INDUSTRY(String r13_INDUSTRY) {
		R13_INDUSTRY = r13_INDUSTRY;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public String getR14_INDUSTRY() {
		return R14_INDUSTRY;
	}
	public void setR14_INDUSTRY(String r14_INDUSTRY) {
		R14_INDUSTRY = r14_INDUSTRY;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public String getR15_INDUSTRY() {
		return R15_INDUSTRY;
	}
	public void setR15_INDUSTRY(String r15_INDUSTRY) {
		R15_INDUSTRY = r15_INDUSTRY;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public String getR16_INDUSTRY() {
		return R16_INDUSTRY;
	}
	public void setR16_INDUSTRY(String r16_INDUSTRY) {
		R16_INDUSTRY = r16_INDUSTRY;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public String getR17_INDUSTRY() {
		return R17_INDUSTRY;
	}
	public void setR17_INDUSTRY(String r17_INDUSTRY) {
		R17_INDUSTRY = r17_INDUSTRY;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public String getR18_INDUSTRY() {
		return R18_INDUSTRY;
	}
	public void setR18_INDUSTRY(String r18_INDUSTRY) {
		R18_INDUSTRY = r18_INDUSTRY;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public String getR19_INDUSTRY() {
		return R19_INDUSTRY;
	}
	public void setR19_INDUSTRY(String r19_INDUSTRY) {
		R19_INDUSTRY = r19_INDUSTRY;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public String getR20_INDUSTRY() {
		return R20_INDUSTRY;
	}
	public void setR20_INDUSTRY(String r20_INDUSTRY) {
		R20_INDUSTRY = r20_INDUSTRY;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public String getR21_INDUSTRY() {
		return R21_INDUSTRY;
	}
	public void setR21_INDUSTRY(String r21_INDUSTRY) {
		R21_INDUSTRY = r21_INDUSTRY;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public String getR22_INDUSTRY() {
		return R22_INDUSTRY;
	}
	public void setR22_INDUSTRY(String r22_INDUSTRY) {
		R22_INDUSTRY = r22_INDUSTRY;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public String getR23_INDUSTRY() {
		return R23_INDUSTRY;
	}
	public void setR23_INDUSTRY(String r23_INDUSTRY) {
		R23_INDUSTRY = r23_INDUSTRY;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public String getR24_INDUSTRY() {
		return R24_INDUSTRY;
	}
	public void setR24_INDUSTRY(String r24_INDUSTRY) {
		R24_INDUSTRY = r24_INDUSTRY;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public String getR25_INDUSTRY() {
		return R25_INDUSTRY;
	}
	public void setR25_INDUSTRY(String r25_INDUSTRY) {
		R25_INDUSTRY = r25_INDUSTRY;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public String getR26_INDUSTRY() {
		return R26_INDUSTRY;
	}
	public void setR26_INDUSTRY(String r26_INDUSTRY) {
		R26_INDUSTRY = r26_INDUSTRY;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
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
	public Date getREPORT_RESUBDATE() {
		return REPORT_RESUBDATE;
	}
	public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
		REPORT_RESUBDATE = rEPORT_RESUBDATE;
	}
	public M_LA2_Resub_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
	
	
	