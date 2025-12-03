

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
@Table(name = "BRRS_M_PI_ARCHIVAL_MANUAL_SUMMARYTABLE")
public class M_PI_Manual_Archival_Summary_Entity {


    @Id
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date REPORT_DATE;

    private String REPORT_VERSION;
    private String REPORT_FREQUENCY;
    private String REPORT_CODE;
    private String REPORT_DESC;

    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DEL_FLG;

    private BigDecimal R14_VALUE;
    private BigDecimal R18_VALUE;
    private BigDecimal R19_VALUE;
    private BigDecimal R25_VALUE;
	
	
	
	
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




	public BigDecimal getR14_VALUE() {
		return R14_VALUE;
	}




	public void setR14_VALUE(BigDecimal r14_VALUE) {
		R14_VALUE = r14_VALUE;
	}




	public BigDecimal getR18_VALUE() {
		return R18_VALUE;
	}




	public void setR18_VALUE(BigDecimal r18_VALUE) {
		R18_VALUE = r18_VALUE;
	}




	public BigDecimal getR19_VALUE() {
		return R19_VALUE;
	}




	public void setR19_VALUE(BigDecimal r19_VALUE) {
		R19_VALUE = r19_VALUE;
	}




	public BigDecimal getR25_VALUE() {
		return R25_VALUE;
	}




	public void setR25_VALUE(BigDecimal r25_VALUE) {
		R25_VALUE = r25_VALUE;
	}



public M_PI_Manual_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}





	
	
	
	
}
