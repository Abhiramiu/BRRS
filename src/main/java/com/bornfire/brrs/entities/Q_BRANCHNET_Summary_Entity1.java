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
@Table(name = "BRRS_Q_BRANCHNET_SUMMARYTABLE1")
public class Q_BRANCHNET_Summary_Entity1 {
    private String r10_bran_sub_bran_district;
    private BigDecimal r10_no1_of_branches;
    private BigDecimal r10_no1_of_sub_branches;
    private BigDecimal r10_no1_of_agencies;

    private String r11_bran_sub_bran_district;
    private BigDecimal r11_no1_of_branches;
    private BigDecimal r11_no1_of_sub_branches;
    private BigDecimal r11_no1_of_agencies;

    private String r12_bran_sub_bran_district;
    private BigDecimal r12_no1_of_branches;
    private BigDecimal r12_no1_of_sub_branches;
    private BigDecimal r12_no1_of_agencies;

    private String r13_bran_sub_bran_district;
    private BigDecimal r13_no1_of_branches;
    private BigDecimal r13_no1_of_sub_branches;
    private BigDecimal r13_no1_of_agencies;

    private String r14_bran_sub_bran_district;
    private BigDecimal r14_no1_of_branches;
    private BigDecimal r14_no1_of_sub_branches;
    private BigDecimal r14_no1_of_agencies;

    private String r15_bran_sub_bran_district;
    private BigDecimal r15_no1_of_branches;
    private BigDecimal r15_no1_of_sub_branches;
    private BigDecimal r15_no1_of_agencies;

    private String r16_bran_sub_bran_district;
    private BigDecimal r16_no1_of_branches;
    private BigDecimal r16_no1_of_sub_branches;
    private BigDecimal r16_no1_of_agencies;

    private String r17_bran_sub_bran_district;
    private BigDecimal r17_no1_of_branches;
    private BigDecimal r17_no1_of_sub_branches;
    private BigDecimal r17_no1_of_agencies;

    private String r18_bran_sub_bran_district;
    private BigDecimal r18_no1_of_branches;
    private BigDecimal r18_no1_of_sub_branches;
    private BigDecimal r18_no1_of_agencies;

    private String r19_bran_sub_bran_district;
    private BigDecimal r19_no1_of_branches;
    private BigDecimal r19_no1_of_sub_branches;
    private BigDecimal r19_no1_of_agencies;

    private String r20_bran_sub_bran_district;
    private BigDecimal r20_no1_of_branches;
    private BigDecimal r20_no1_of_sub_branches;
    private BigDecimal r20_no1_of_agencies;

 
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
  
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;

    public String getR10_bran_sub_bran_district() {
        return r10_bran_sub_bran_district;
    }

    public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
        this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
    }

    public BigDecimal getR10_no1_of_branches() {
        return r10_no1_of_branches;
    }

    public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
        this.r10_no1_of_branches = r10_no1_of_branches;
    }

    public BigDecimal getR10_no1_of_sub_branches() {
        return r10_no1_of_sub_branches;
    }

    public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
        this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
    }

    public BigDecimal getR10_no1_of_agencies() {
        return r10_no1_of_agencies;
    }

    public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
        this.r10_no1_of_agencies = r10_no1_of_agencies;
    }

    public String getR11_bran_sub_bran_district() {
        return r11_bran_sub_bran_district;
    }

    public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
        this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
    }

    public BigDecimal getR11_no1_of_branches() {
        return r11_no1_of_branches;
    }

    public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
        this.r11_no1_of_branches = r11_no1_of_branches;
    }

    public BigDecimal getR11_no1_of_sub_branches() {
        return r11_no1_of_sub_branches;
    }

    public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
        this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
    }

    public BigDecimal getR11_no1_of_agencies() {
        return r11_no1_of_agencies;
    }

    public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
        this.r11_no1_of_agencies = r11_no1_of_agencies;
    }

    public String getR12_bran_sub_bran_district() {
        return r12_bran_sub_bran_district;
    }

    public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
        this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
    }

    public BigDecimal getR12_no1_of_branches() {
        return r12_no1_of_branches;
    }

    public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
        this.r12_no1_of_branches = r12_no1_of_branches;
    }

    public BigDecimal getR12_no1_of_sub_branches() {
        return r12_no1_of_sub_branches;
    }

    public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
        this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
    }

    public BigDecimal getR12_no1_of_agencies() {
        return r12_no1_of_agencies;
    }

    public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
        this.r12_no1_of_agencies = r12_no1_of_agencies;
    }

    public String getR13_bran_sub_bran_district() {
        return r13_bran_sub_bran_district;
    }

    public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
        this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
    }

    public BigDecimal getR13_no1_of_branches() {
        return r13_no1_of_branches;
    }

    public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
        this.r13_no1_of_branches = r13_no1_of_branches;
    }

    public BigDecimal getR13_no1_of_sub_branches() {
        return r13_no1_of_sub_branches;
    }

    public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
        this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
    }

    public BigDecimal getR13_no1_of_agencies() {
        return r13_no1_of_agencies;
    }

    public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
        this.r13_no1_of_agencies = r13_no1_of_agencies;
    }

    public String getR14_bran_sub_bran_district() {
        return r14_bran_sub_bran_district;
    }

    public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
        this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
    }

    public BigDecimal getR14_no1_of_branches() {
        return r14_no1_of_branches;
    }

    public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
        this.r14_no1_of_branches = r14_no1_of_branches;
    }

    public BigDecimal getR14_no1_of_sub_branches() {
        return r14_no1_of_sub_branches;
    }

    public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
        this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
    }

    public BigDecimal getR14_no1_of_agencies() {
        return r14_no1_of_agencies;
    }

    public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
        this.r14_no1_of_agencies = r14_no1_of_agencies;
    }

    public String getR15_bran_sub_bran_district() {
        return r15_bran_sub_bran_district;
    }

    public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
        this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
    }

    public BigDecimal getR15_no1_of_branches() {
        return r15_no1_of_branches;
    }

    public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
        this.r15_no1_of_branches = r15_no1_of_branches;
    }

    public BigDecimal getR15_no1_of_sub_branches() {
        return r15_no1_of_sub_branches;
    }

    public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
        this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
    }

    public BigDecimal getR15_no1_of_agencies() {
        return r15_no1_of_agencies;
    }

    public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
        this.r15_no1_of_agencies = r15_no1_of_agencies;
    }

    public String getR16_bran_sub_bran_district() {
        return r16_bran_sub_bran_district;
    }

    public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
        this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
    }

    public BigDecimal getR16_no1_of_branches() {
        return r16_no1_of_branches;
    }

    public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
        this.r16_no1_of_branches = r16_no1_of_branches;
    }

    public BigDecimal getR16_no1_of_sub_branches() {
        return r16_no1_of_sub_branches;
    }

    public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
        this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
    }

    public BigDecimal getR16_no1_of_agencies() {
        return r16_no1_of_agencies;
    }

    public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
        this.r16_no1_of_agencies = r16_no1_of_agencies;
    }

    public String getR17_bran_sub_bran_district() {
        return r17_bran_sub_bran_district;
    }

    public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
        this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
    }

    public BigDecimal getR17_no1_of_branches() {
        return r17_no1_of_branches;
    }

    public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
        this.r17_no1_of_branches = r17_no1_of_branches;
    }

    public BigDecimal getR17_no1_of_sub_branches() {
        return r17_no1_of_sub_branches;
    }

    public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
        this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
    }

    public BigDecimal getR17_no1_of_agencies() {
        return r17_no1_of_agencies;
    }

    public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
        this.r17_no1_of_agencies = r17_no1_of_agencies;
    }

    public String getR18_bran_sub_bran_district() {
        return r18_bran_sub_bran_district;
    }

    public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
        this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
    }

    public BigDecimal getR18_no1_of_branches() {
        return r18_no1_of_branches;
    }

    public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
        this.r18_no1_of_branches = r18_no1_of_branches;
    }

    public BigDecimal getR18_no1_of_sub_branches() {
        return r18_no1_of_sub_branches;
    }

    public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
        this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
    }

    public BigDecimal getR18_no1_of_agencies() {
        return r18_no1_of_agencies;
    }

    public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
        this.r18_no1_of_agencies = r18_no1_of_agencies;
    }

    public String getR19_bran_sub_bran_district() {
        return r19_bran_sub_bran_district;
    }

    public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
        this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
    }

    public BigDecimal getR19_no1_of_branches() {
        return r19_no1_of_branches;
    }

    public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
        this.r19_no1_of_branches = r19_no1_of_branches;
    }

    public BigDecimal getR19_no1_of_sub_branches() {
        return r19_no1_of_sub_branches;
    }

    public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
        this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
    }

    public BigDecimal getR19_no1_of_agencies() {
        return r19_no1_of_agencies;
    }

    public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
        this.r19_no1_of_agencies = r19_no1_of_agencies;
    }

    public String getR20_bran_sub_bran_district() {
        return r20_bran_sub_bran_district;
    }

    public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
        this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
    }

    public BigDecimal getR20_no1_of_branches() {
        return r20_no1_of_branches;
    }

    public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
        this.r20_no1_of_branches = r20_no1_of_branches;
    }

    public BigDecimal getR20_no1_of_sub_branches() {
        return r20_no1_of_sub_branches;
    }

    public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
        this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
    }

    public BigDecimal getR20_no1_of_agencies() {
        return r20_no1_of_agencies;
    }

    public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
        this.r20_no1_of_agencies = r20_no1_of_agencies;
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

    public String getReport_frequency() {
        return report_frequency;
    }

    public void setReport_frequency(String report_frequency) {
        this.report_frequency = report_frequency;
    }

    public String getReport_code() {
        return report_code;
    }

    public void setReport_code(String report_code) {
        this.report_code = report_code;
    }

    public String getReport_desc() {
        return report_desc;
    }

    public void setReport_desc(String report_desc) {
        this.report_desc = report_desc;
    }

    public String getEntity_flg() {
        return entity_flg;
    }

    public void setEntity_flg(String entity_flg) {
        this.entity_flg = entity_flg;
    }

    public String getModify_flg() {
        return modify_flg;
    }

    public void setModify_flg(String modify_flg) {
        this.modify_flg = modify_flg;
    }

    public String getDel_flg() {
        return del_flg;
    }

    public void setDel_flg(String del_flg) {
        this.del_flg = del_flg;
    }

    public Q_BRANCHNET_Summary_Entity1() {
        super();
    }

}
