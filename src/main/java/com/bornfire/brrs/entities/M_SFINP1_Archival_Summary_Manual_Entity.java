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
@Table(name = "BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL")
public class M_SFINP1_Archival_Summary_Manual_Entity {
	
	private BigDecimal R14_MONTH_END;
	private BigDecimal R34_MONTH_END;
   private BigDecimal R37_MONTH_END;
   private BigDecimal R39_MONTH_END;
   private BigDecimal R43_MONTH_END;
   private BigDecimal R50_MONTH_END;
   private BigDecimal R51_MONTH_END;
   private BigDecimal R52_MONTH_END;
   private BigDecimal R57_MONTH_END;
   private BigDecimal R59_MONTH_END;
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
   public BigDecimal getR14_MONTH_END() {
	return R14_MONTH_END;
   }
   public void setR14_MONTH_END(BigDecimal r14_MONTH_END) {
	R14_MONTH_END = r14_MONTH_END;
   }
   public BigDecimal getR34_MONTH_END() {
	return R34_MONTH_END;
   }
   public void setR34_MONTH_END(BigDecimal r34_MONTH_END) {
	R34_MONTH_END = r34_MONTH_END;
   }
   public BigDecimal getR37_MONTH_END() {
	return R37_MONTH_END;
   }
   public void setR37_MONTH_END(BigDecimal r37_MONTH_END) {
	R37_MONTH_END = r37_MONTH_END;
   }
   public BigDecimal getR39_MONTH_END() {
	return R39_MONTH_END;
   }
   public void setR39_MONTH_END(BigDecimal r39_MONTH_END) {
	R39_MONTH_END = r39_MONTH_END;
   }
   public BigDecimal getR43_MONTH_END() {
	return R43_MONTH_END;
   }
   public void setR43_MONTH_END(BigDecimal r43_MONTH_END) {
	R43_MONTH_END = r43_MONTH_END;
   }
   public BigDecimal getR50_MONTH_END() {
	return R50_MONTH_END;
   }
   public void setR50_MONTH_END(BigDecimal r50_MONTH_END) {
	R50_MONTH_END = r50_MONTH_END;
   }
   public BigDecimal getR51_MONTH_END() {
	return R51_MONTH_END;
   }
   public void setR51_MONTH_END(BigDecimal r51_MONTH_END) {
	R51_MONTH_END = r51_MONTH_END;
   }
   public BigDecimal getR52_MONTH_END() {
	return R52_MONTH_END;
   }
   public void setR52_MONTH_END(BigDecimal r52_MONTH_END) {
	R52_MONTH_END = r52_MONTH_END;
   }
   public BigDecimal getR57_MONTH_END() {
	return R57_MONTH_END;
   }
   public void setR57_MONTH_END(BigDecimal r57_MONTH_END) {
	R57_MONTH_END = r57_MONTH_END;
   }
   public BigDecimal getR59_MONTH_END() {
	return R59_MONTH_END;
   }
   public void setR59_MONTH_END(BigDecimal r59_MONTH_END) {
	R59_MONTH_END = r59_MONTH_END;
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
   public M_SFINP1_Archival_Summary_Manual_Entity() {
	super();
	// TODO Auto-generated constructor stub
   }
   
   
}
