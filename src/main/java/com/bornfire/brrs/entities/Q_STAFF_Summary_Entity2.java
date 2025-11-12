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
@Table(name = "BRRS_Q_STAFF_SUMMARYTABLE2")
public class Q_STAFF_Summary_Entity2 {

  private String R21_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R21_LOCAL;
  private BigDecimal R21_EXPARIATES;
  private BigDecimal R21_TOTAL;
  private String R22_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R22_LOCAL;
  private BigDecimal R22_EXPARIATES;
  private BigDecimal R22_TOTAL;
  private String R23_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R23_LOCAL;
  private BigDecimal R23_EXPARIATES;
  private BigDecimal R23_TOTAL;
  private String R24_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R24_LOCAL;
  private BigDecimal R24_EXPARIATES;
  private BigDecimal R24_TOTAL;
  private String R25_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R25_LOCAL;
  private BigDecimal R25_EXPARIATES;
  private BigDecimal R25_TOTAL;
  private String R26_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R26_LOCAL;
  private BigDecimal R26_EXPARIATES;
  private BigDecimal R26_TOTAL;
  private String R27_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R27_LOCAL;
  private BigDecimal R27_EXPARIATES;
  private BigDecimal R27_TOTAL;
  private String R28_SENIOR_MANAGEMENT_COMPENSATION;
  private BigDecimal R28_LOCAL;
  private BigDecimal R28_EXPARIATES;
  private BigDecimal R28_TOTAL;

  @Temporal(TemporalType.DATE)
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  @Id
  @Column(name = "REPORT_DATE")
  private Date reportDate;
  @Column(name = "REPORT_VERSION")
  private String reportVersion;
  
  private String REPORT_FREQUENCY;
  private String REPORT_CODE;
  private String REPORT_DESC;
  private String ENTITY_FLG;
  private String MODIFY_FLG;
  private String DEL_FLG;

  public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
    return R21_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
    R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR21_LOCAL() {
    return R21_LOCAL;
  }

  public void setR21_LOCAL(BigDecimal r21_LOCAL) {
    R21_LOCAL = r21_LOCAL;
  }

  public BigDecimal getR21_EXPARIATES() {
    return R21_EXPARIATES;
  }

  public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
    R21_EXPARIATES = r21_EXPARIATES;
  }

  public BigDecimal getR21_TOTAL() {
    return R21_TOTAL;
  }

  public void setR21_TOTAL(BigDecimal r21_TOTAL) {
    R21_TOTAL = r21_TOTAL;
  }

  public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
    return R22_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
    R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR22_LOCAL() {
    return R22_LOCAL;
  }

  public void setR22_LOCAL(BigDecimal r22_LOCAL) {
    R22_LOCAL = r22_LOCAL;
  }

  public BigDecimal getR22_EXPARIATES() {
    return R22_EXPARIATES;
  }

  public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
    R22_EXPARIATES = r22_EXPARIATES;
  }

  public BigDecimal getR22_TOTAL() {
    return R22_TOTAL;
  }

  public void setR22_TOTAL(BigDecimal r22_TOTAL) {
    R22_TOTAL = r22_TOTAL;
  }

  public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
    return R23_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
    R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR23_LOCAL() {
    return R23_LOCAL;
  }

  public void setR23_LOCAL(BigDecimal r23_LOCAL) {
    R23_LOCAL = r23_LOCAL;
  }

  public BigDecimal getR23_EXPARIATES() {
    return R23_EXPARIATES;
  }

  public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
    R23_EXPARIATES = r23_EXPARIATES;
  }

  public BigDecimal getR23_TOTAL() {
    return R23_TOTAL;
  }

  public void setR23_TOTAL(BigDecimal r23_TOTAL) {
    R23_TOTAL = r23_TOTAL;
  }

  public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
    return R24_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
    R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR24_LOCAL() {
    return R24_LOCAL;
  }

  public void setR24_LOCAL(BigDecimal r24_LOCAL) {
    R24_LOCAL = r24_LOCAL;
  }

  public BigDecimal getR24_EXPARIATES() {
    return R24_EXPARIATES;
  }

  public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
    R24_EXPARIATES = r24_EXPARIATES;
  }

  public BigDecimal getR24_TOTAL() {
    return R24_TOTAL;
  }

  public void setR24_TOTAL(BigDecimal r24_TOTAL) {
    R24_TOTAL = r24_TOTAL;
  }

  public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
    return R25_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
    R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR25_LOCAL() {
    return R25_LOCAL;
  }

  public void setR25_LOCAL(BigDecimal r25_LOCAL) {
    R25_LOCAL = r25_LOCAL;
  }

  public BigDecimal getR25_EXPARIATES() {
    return R25_EXPARIATES;
  }

  public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
    R25_EXPARIATES = r25_EXPARIATES;
  }

  public BigDecimal getR25_TOTAL() {
    return R25_TOTAL;
  }

  public void setR25_TOTAL(BigDecimal r25_TOTAL) {
    R25_TOTAL = r25_TOTAL;
  }

  public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
    return R26_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
    R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR26_LOCAL() {
    return R26_LOCAL;
  }

  public void setR26_LOCAL(BigDecimal r26_LOCAL) {
    R26_LOCAL = r26_LOCAL;
  }

  public BigDecimal getR26_EXPARIATES() {
    return R26_EXPARIATES;
  }

  public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
    R26_EXPARIATES = r26_EXPARIATES;
  }

  public BigDecimal getR26_TOTAL() {
    return R26_TOTAL;
  }

  public void setR26_TOTAL(BigDecimal r26_TOTAL) {
    R26_TOTAL = r26_TOTAL;
  }

  public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
    return R27_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
    R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR27_LOCAL() {
    return R27_LOCAL;
  }

  public void setR27_LOCAL(BigDecimal r27_LOCAL) {
    R27_LOCAL = r27_LOCAL;
  }

  public BigDecimal getR27_EXPARIATES() {
    return R27_EXPARIATES;
  }

  public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
    R27_EXPARIATES = r27_EXPARIATES;
  }

  public BigDecimal getR27_TOTAL() {
    return R27_TOTAL;
  }

  public void setR27_TOTAL(BigDecimal r27_TOTAL) {
    R27_TOTAL = r27_TOTAL;
  }

  public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
    return R28_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
    R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
  }

  public BigDecimal getR28_LOCAL() {
    return R28_LOCAL;
  }

  public void setR28_LOCAL(BigDecimal r28_LOCAL) {
    R28_LOCAL = r28_LOCAL;
  }

  public BigDecimal getR28_EXPARIATES() {
    return R28_EXPARIATES;
  }

  public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
    R28_EXPARIATES = r28_EXPARIATES;
  }

  public BigDecimal getR28_TOTAL() {
    return R28_TOTAL;
  }

  public void setR28_TOTAL(BigDecimal r28_TOTAL) {
    R28_TOTAL = r28_TOTAL;
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

  public Q_STAFF_Summary_Entity2() {
    super();
  }

}
