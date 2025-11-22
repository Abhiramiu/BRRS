
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
@Table(name = "BRRS_M_IS_MAPPING_ARCHIVALSUMMARYTABLE")
public class M_IS_Mapping_ArchivalSummaryEntity {
    
    public BigDecimal R28_HELD_FOR_TRADING;
    
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

   public BigDecimal getR28_HELD_FOR_TRADING() {
    return R28_HELD_FOR_TRADING;
   }
   public void setR28_HELD_FOR_TRADING(BigDecimal r28_HELD_FOR_TRADING) {
    R28_HELD_FOR_TRADING = r28_HELD_FOR_TRADING;
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

   
    
}
