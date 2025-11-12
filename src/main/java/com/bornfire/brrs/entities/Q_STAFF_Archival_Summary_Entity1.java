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
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY1")
@IdClass(Q_STAFF_Archival_Summary1_PK.class)
public class Q_STAFF_Archival_Summary_Entity1 {

private String R9_STAFF_COMPLEMENT;
private BigDecimal R9_LOCAL;
private BigDecimal R9_EXPARIATES;
private BigDecimal R9_TOTAL;
private String R10_STAFF_COMPLEMENT;
private BigDecimal R10_LOCAL;
private BigDecimal R10_EXPARIATES;
private BigDecimal R10_TOTAL;
private String  R11_STAFF_COMPLEMENT;
private BigDecimal R11_LOCAL;
private BigDecimal R11_EXPARIATES;
private BigDecimal R11_TOTAL;
private String R12_STAFF_COMPLEMENT;
private BigDecimal R12_LOCAL;
private BigDecimal R12_EXPARIATES;
private BigDecimal R12_TOTAL;
private String  R13_STAFF_COMPLEMENT;
private BigDecimal R13_LOCAL;
private BigDecimal R13_EXPARIATES;
private BigDecimal R13_TOTAL;
private String  R14_STAFF_COMPLEMENT;
private BigDecimal R14_LOCAL;
private BigDecimal R14_EXPARIATES;
private BigDecimal R14_TOTAL;
private String R15_STAFF_COMPLEMENT;
private BigDecimal R15_LOCAL;
private BigDecimal R15_EXPARIATES;
private BigDecimal R15_TOTAL;


	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private String reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reportResubDate;

   private String REPORT_FREQUENCY;
   private String REPORT_CODE;
   private String REPORT_DESC;
   private String ENTITY_FLG;
   private String MODIFY_FLG;
   private String DEL_FLG;

   public String getR9_STAFF_COMPLEMENT() {
    return R9_STAFF_COMPLEMENT;
   }
   public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
    R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
   }
   public BigDecimal getR9_LOCAL() {
    return R9_LOCAL;
   }
   public void setR9_LOCAL(BigDecimal r9_LOCAL) {
    R9_LOCAL = r9_LOCAL;
   }
   public BigDecimal getR9_EXPARIATES() {
    return R9_EXPARIATES;
   }
   public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
    R9_EXPARIATES = r9_EXPARIATES;
   }
   public BigDecimal getR9_TOTAL() {
    return R9_TOTAL;
   }
   public void setR9_TOTAL(BigDecimal r9_TOTAL) {
    R9_TOTAL = r9_TOTAL;
   }
   public String getR10_STAFF_COMPLEMENT() {
    return R10_STAFF_COMPLEMENT;
   }
   public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
    R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
   }
   public BigDecimal getR10_LOCAL() {
    return R10_LOCAL;
   }
   public void setR10_LOCAL(BigDecimal r10_LOCAL) {
    R10_LOCAL = r10_LOCAL;
   }
   public BigDecimal getR10_EXPARIATES() {
    return R10_EXPARIATES;
   }
   public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
    R10_EXPARIATES = r10_EXPARIATES;
   }
   public BigDecimal getR10_TOTAL() {
    return R10_TOTAL;
   }
   public void setR10_TOTAL(BigDecimal r10_TOTAL) {
    R10_TOTAL = r10_TOTAL;
   }
   public String getR11_STAFF_COMPLEMENT() {
    return R11_STAFF_COMPLEMENT;
   }
   public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
    R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
   }
   public BigDecimal getR11_LOCAL() {
    return R11_LOCAL;
   }
   public void setR11_LOCAL(BigDecimal r11_LOCAL) {
    R11_LOCAL = r11_LOCAL;
   }
   public BigDecimal getR11_EXPARIATES() {
    return R11_EXPARIATES;
   }
   public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
    R11_EXPARIATES = r11_EXPARIATES;
   }
   public BigDecimal getR11_TOTAL() {
    return R11_TOTAL;
   }
   public void setR11_TOTAL(BigDecimal r11_TOTAL) {
    R11_TOTAL = r11_TOTAL;
   }
   public String getR12_STAFF_COMPLEMENT() {
    return R12_STAFF_COMPLEMENT;
   }
   public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
    R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
   }
   public BigDecimal getR12_LOCAL() {
    return R12_LOCAL;
   }
   public void setR12_LOCAL(BigDecimal r12_LOCAL) {
    R12_LOCAL = r12_LOCAL;
   }
   public BigDecimal getR12_EXPARIATES() {
    return R12_EXPARIATES;
   }
   public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
    R12_EXPARIATES = r12_EXPARIATES;
   }
   public BigDecimal getR12_TOTAL() {
    return R12_TOTAL;
   }
   public void setR12_TOTAL(BigDecimal r12_TOTAL) {
    R12_TOTAL = r12_TOTAL;
   }
   public String getR13_STAFF_COMPLEMENT() {
    return R13_STAFF_COMPLEMENT;
   }
   public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
    R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
   }
   public BigDecimal getR13_LOCAL() {
    return R13_LOCAL;
   }
   public void setR13_LOCAL(BigDecimal r13_LOCAL) {
    R13_LOCAL = r13_LOCAL;
   }
   public BigDecimal getR13_EXPARIATES() {
    return R13_EXPARIATES;
   }
   public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
    R13_EXPARIATES = r13_EXPARIATES;
   }
   public BigDecimal getR13_TOTAL() {
    return R13_TOTAL;
   }
   public void setR13_TOTAL(BigDecimal r13_TOTAL) {
    R13_TOTAL = r13_TOTAL;
   }
   public String getR14_STAFF_COMPLEMENT() {
    return R14_STAFF_COMPLEMENT;
   }
   public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
    R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
   }
   public BigDecimal getR14_LOCAL() {
    return R14_LOCAL;
   }
   public void setR14_LOCAL(BigDecimal r14_LOCAL) {
    R14_LOCAL = r14_LOCAL;
   }
   public BigDecimal getR14_EXPARIATES() {
    return R14_EXPARIATES;
   }
   public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
    R14_EXPARIATES = r14_EXPARIATES;
   }
   public BigDecimal getR14_TOTAL() {
    return R14_TOTAL;
   }
   public void setR14_TOTAL(BigDecimal r14_TOTAL) {
    R14_TOTAL = r14_TOTAL;
   }
   public String getR15_STAFF_COMPLEMENT() {
    return R15_STAFF_COMPLEMENT;
   }
   public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
    R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
   }
   public BigDecimal getR15_LOCAL() {
    return R15_LOCAL;
   }
   public void setR15_LOCAL(BigDecimal r15_LOCAL) {
    R15_LOCAL = r15_LOCAL;
   }
   public BigDecimal getR15_EXPARIATES() {
    return R15_EXPARIATES;
   }
   public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
    R15_EXPARIATES = r15_EXPARIATES;
   }
   public BigDecimal getR15_TOTAL() {
    return R15_TOTAL;
   }
   public void setR15_TOTAL(BigDecimal r15_TOTAL) {
    R15_TOTAL = r15_TOTAL;
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
   public Date getReportResubDate() {
     return reportResubDate;
   }
   public void setReportResubDate(Date reportResubDate) {
     this.reportResubDate = reportResubDate;
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

   public Q_STAFF_Archival_Summary_Entity1() {
    super();
   }

   
   
}
