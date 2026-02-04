package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_M_LA2_ARCHIVALTABLE_DETAIL")
public class M_LA2_Archival_Detail_Entity {

	private String	R12_INDUSTRY;
	private BigDecimal	R12_TOTAL;
	private String	R13_INDUSTRY;
	private BigDecimal	R13_TOTAL;
	private String	R14_INDUSTRY;
	private BigDecimal	R14_TOTAL;
	private String	R15_INDUSTRY;
	private BigDecimal	R15_TOTAL;
	private String	R16_INDUSTRY;
	private BigDecimal	R16_TOTAL;
	private String	R17_INDUSTRY;
	private BigDecimal	R17_TOTAL;
	private String	R18_INDUSTRY;
	private BigDecimal	R18_TOTAL;
	private String	R19_INDUSTRY;
	private BigDecimal	R19_TOTAL;
	private String	R20_INDUSTRY;
	private BigDecimal	R20_TOTAL;
	private String	R21_INDUSTRY;
	private BigDecimal	R21_TOTAL;
	private String	R22_INDUSTRY;
	private BigDecimal	R22_TOTAL;
	private String	R23_INDUSTRY;
	private BigDecimal	R23_TOTAL;
	private String	R24_INDUSTRY;
	private BigDecimal	R24_TOTAL;
	private String	R25_INDUSTRY;
	private BigDecimal	R25_TOTAL;
	private String	R26_INDUSTRY;
	private BigDecimal	R26_TOTAL;
	 @Id
	private Date	REPORT_DATE;
	private BigDecimal	REPORT_VERSION;
	private String	REPORT_FREQUENCY;
	private String	REPORT_CODE;
	private String	REPORT_DESC;
	private String	ENTITY_FLG;
	private String	MODIFY_FLG;
	private String	DEL_FLG;
	public String getR12_INDUSTRY() {
		return R12_INDUSTRY;
	}
	public void setR12_INDUSTRY(String R12_INDUSTRY) {
		this.R12_INDUSTRY = R12_INDUSTRY;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal R12_TOTAL) {
		this.R12_TOTAL = R12_TOTAL;
	}
	public String getR13_INDUSTRY() {
		return R13_INDUSTRY;
	}
	public void setR13_INDUSTRY(String R13_INDUSTRY) {
		this.R13_INDUSTRY = R13_INDUSTRY;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal R13_TOTAL) {
		this.R13_TOTAL = R13_TOTAL;
	}
	public String getR14_INDUSTRY() {
		return R14_INDUSTRY;
	}
	public void setR14_INDUSTRY(String R14_INDUSTRY) {
		this.R14_INDUSTRY = R14_INDUSTRY;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal R14_TOTAL) {
		this.R14_TOTAL = R14_TOTAL;
	}
	public String getR15_INDUSTRY() {
		return R15_INDUSTRY;
	}
	public void setR15_INDUSTRY(String R15_INDUSTRY) {
		this.R15_INDUSTRY = R15_INDUSTRY;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal R15_TOTAL) {
		this.R15_TOTAL = R15_TOTAL;
	}
	public String getR16_INDUSTRY() {
		return R16_INDUSTRY;
	}
	public void setR16_INDUSTRY(String R16_INDUSTRY) {
		this.R16_INDUSTRY = R16_INDUSTRY;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal R16_TOTAL) {
		this.R16_TOTAL = R16_TOTAL;
	}
	public String getR17_INDUSTRY() {
		return R17_INDUSTRY;
	}
	public void setR17_INDUSTRY(String R17_INDUSTRY) {
		this.R17_INDUSTRY = R17_INDUSTRY;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal R17_TOTAL) {
		this.R17_TOTAL = R17_TOTAL;
	}
	public String getR18_INDUSTRY() {
		return R18_INDUSTRY;
	}
	public void setR18_INDUSTRY(String R18_INDUSTRY) {
		this.R18_INDUSTRY = R18_INDUSTRY;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal R18_TOTAL) {
		this.R18_TOTAL = R18_TOTAL;
	}
	public String getR19_INDUSTRY() {
		return R19_INDUSTRY;
	}
	public void setR19_INDUSTRY(String R19_INDUSTRY) {
		this.R19_INDUSTRY = R19_INDUSTRY;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal R19_TOTAL) {
		this.R19_TOTAL = R19_TOTAL;
	}
	public String getR20_INDUSTRY() {
		return R20_INDUSTRY;
	}
	public void setR20_INDUSTRY(String R20_INDUSTRY) {
		this.R20_INDUSTRY = R20_INDUSTRY;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal R20_TOTAL) {
		this.R20_TOTAL = R20_TOTAL;
	}
	public String getR21_INDUSTRY() {
		return R21_INDUSTRY;
	}
	public void setR21_INDUSTRY(String R21_INDUSTRY) {
		this.R21_INDUSTRY = R21_INDUSTRY;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal R21_TOTAL) {
		this.R21_TOTAL = R21_TOTAL;
	}
	public String getR22_INDUSTRY() {
		return R22_INDUSTRY;
	}
	public void setR22_INDUSTRY(String R22_INDUSTRY) {
		this.R22_INDUSTRY = R22_INDUSTRY;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal R22_TOTAL) {
		this.R22_TOTAL = R22_TOTAL;
	}
	public String getR23_INDUSTRY() {
		return R23_INDUSTRY;
	}
	public void setR23_INDUSTRY(String R23_INDUSTRY) {
		this.R23_INDUSTRY = R23_INDUSTRY;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal R23_TOTAL) {
		this.R23_TOTAL = R23_TOTAL;
	}
	public String getR24_INDUSTRY() {
		return R24_INDUSTRY;
	}
	public void setR24_INDUSTRY(String R24_INDUSTRY) {
		this.R24_INDUSTRY = R24_INDUSTRY;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal R24_TOTAL) {
		this.R24_TOTAL = R24_TOTAL;
	}
	public String getR25_INDUSTRY() {
		return R25_INDUSTRY;
	}
	public void setR25_INDUSTRY(String R25_INDUSTRY) {
		this.R25_INDUSTRY = R25_INDUSTRY;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal R25_TOTAL) {
		this.R25_TOTAL = R25_TOTAL;
	}
	public String getR26_INDUSTRY() {
		return R26_INDUSTRY;
	}
	public void setR26_INDUSTRY(String R26_INDUSTRY) {
		this.R26_INDUSTRY = R26_INDUSTRY;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal R26_TOTAL) {
		this.R26_TOTAL = R26_TOTAL;
	}
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}
	public BigDecimal getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
		this.REPORT_VERSION = REPORT_VERSION;
	}
	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}
	public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
		this.REPORT_FREQUENCY = REPORT_FREQUENCY;
	}
	public String getREPORT_CODE() {
		return REPORT_CODE;
	}
	public void setREPORT_CODE(String REPORT_CODE) {
		this.REPORT_CODE = REPORT_CODE;
	}
	public String getREPORT_DESC() {
		return REPORT_DESC;
	}
	public void setREPORT_DESC(String REPORT_DESC) {
		this.REPORT_DESC = REPORT_DESC;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}
	    	public M_LA2_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
    
    
}