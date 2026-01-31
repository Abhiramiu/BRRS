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
@Table(name = "BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY2")
@IdClass(Q_BRANCHNET_Archival_Summary2_PK.class)
public class Q_BRANCHNET_Archival_Summary_Entity2 {
    private String r25_atm_mini_atm_district;
    private BigDecimal r25_no_of_atms;
    private BigDecimal r25_no_of_mini_atms;
    private BigDecimal r25_encashment_points;

    private String r26_atm_mini_atm_district;
    private BigDecimal r26_no_of_atms;
    private BigDecimal r26_no_of_mini_atms;
    private BigDecimal r26_encashment_points;

    private String r27_atm_mini_atm_district;
    private BigDecimal r27_no_of_atms;
    private BigDecimal r27_no_of_mini_atms;
    private BigDecimal r27_encashment_points;

    private String r28_atm_mini_atm_district;
    private BigDecimal r28_no_of_atms;
    private BigDecimal r28_no_of_mini_atms;
    private BigDecimal r28_encashment_points;

    private String r29_atm_mini_atm_district;
    private BigDecimal r29_no_of_atms;
    private BigDecimal r29_no_of_mini_atms;
    private BigDecimal r29_encashment_points;

    private String r30_atm_mini_atm_district;
    private BigDecimal r30_no_of_atms;
    private BigDecimal r30_no_of_mini_atms;
    private BigDecimal r30_encashment_points;

    private String r31_atm_mini_atm_district;
    private BigDecimal r31_no_of_atms;
    private BigDecimal r31_no_of_mini_atms;
    private BigDecimal r31_encashment_points;

    private String r32_atm_mini_atm_district;
    private BigDecimal r32_no_of_atms;
    private BigDecimal r32_no_of_mini_atms;
    private BigDecimal r32_encashment_points;

    private String r33_atm_mini_atm_district;
    private BigDecimal r33_no_of_atms;
    private BigDecimal r33_no_of_mini_atms;
    private BigDecimal r33_encashment_points;

    private String r34_atm_mini_atm_district;
    private BigDecimal r34_no_of_atms;
    private BigDecimal r34_no_of_mini_atms;
    private BigDecimal r34_encashment_points;

    private String r35_atm_mini_atm_district;
    private BigDecimal r35_no_of_atms;
    private BigDecimal r35_no_of_mini_atms;
    private BigDecimal r35_encashment_points;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "REPORT_DATE")
    private Date reportDate;

    @Id
    @Column(name = "REPORT_VERSION")
    private BigDecimal reportVersion;

    @Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;

    public String getR26_atm_mini_atm_district() {
        return r26_atm_mini_atm_district;
    }

    public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
        this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
    }

    public BigDecimal getR26_no_of_atms() {
        return r26_no_of_atms;
    }

    public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
        this.r26_no_of_atms = r26_no_of_atms;
    }

    public BigDecimal getR26_no_of_mini_atms() {
        return r26_no_of_mini_atms;
    }

    public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
        this.r26_no_of_mini_atms = r26_no_of_mini_atms;
    }

    public BigDecimal getR26_encashment_points() {
        return r26_encashment_points;
    }

    public void setR26_encashment_points(BigDecimal r26_encashment_points) {
        this.r26_encashment_points = r26_encashment_points;
    }

    public String getR27_atm_mini_atm_district() {
        return r27_atm_mini_atm_district;
    }

    public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
        this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
    }

    public BigDecimal getR27_no_of_atms() {
        return r27_no_of_atms;
    }

    public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
        this.r27_no_of_atms = r27_no_of_atms;
    }

    public BigDecimal getR27_no_of_mini_atms() {
        return r27_no_of_mini_atms;
    }

    public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
        this.r27_no_of_mini_atms = r27_no_of_mini_atms;
    }

    public BigDecimal getR27_encashment_points() {
        return r27_encashment_points;
    }

    public void setR27_encashment_points(BigDecimal r27_encashment_points) {
        this.r27_encashment_points = r27_encashment_points;
    }

    public String getR28_atm_mini_atm_district() {
        return r28_atm_mini_atm_district;
    }

    public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
        this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
    }

    public BigDecimal getR28_no_of_atms() {
        return r28_no_of_atms;
    }

    public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
        this.r28_no_of_atms = r28_no_of_atms;
    }

    public BigDecimal getR28_no_of_mini_atms() {
        return r28_no_of_mini_atms;
    }

    public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
        this.r28_no_of_mini_atms = r28_no_of_mini_atms;
    }

    public BigDecimal getR28_encashment_points() {
        return r28_encashment_points;
    }

    public void setR28_encashment_points(BigDecimal r28_encashment_points) {
        this.r28_encashment_points = r28_encashment_points;
    }

    public String getR29_atm_mini_atm_district() {
        return r29_atm_mini_atm_district;
    }

    public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
        this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
    }

    public BigDecimal getR29_no_of_atms() {
        return r29_no_of_atms;
    }

    public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
        this.r29_no_of_atms = r29_no_of_atms;
    }

    public BigDecimal getR29_no_of_mini_atms() {
        return r29_no_of_mini_atms;
    }

    public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
        this.r29_no_of_mini_atms = r29_no_of_mini_atms;
    }

    public BigDecimal getR29_encashment_points() {
        return r29_encashment_points;
    }

    public void setR29_encashment_points(BigDecimal r29_encashment_points) {
        this.r29_encashment_points = r29_encashment_points;
    }

    public String getR30_atm_mini_atm_district() {
        return r30_atm_mini_atm_district;
    }

    public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
        this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
    }

    public BigDecimal getR30_no_of_atms() {
        return r30_no_of_atms;
    }

    public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
        this.r30_no_of_atms = r30_no_of_atms;
    }

    public BigDecimal getR30_no_of_mini_atms() {
        return r30_no_of_mini_atms;
    }

    public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
        this.r30_no_of_mini_atms = r30_no_of_mini_atms;
    }

    public BigDecimal getR30_encashment_points() {
        return r30_encashment_points;
    }

    public void setR30_encashment_points(BigDecimal r30_encashment_points) {
        this.r30_encashment_points = r30_encashment_points;
    }

    public String getR31_atm_mini_atm_district() {
        return r31_atm_mini_atm_district;
    }

    public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
        this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
    }

    public BigDecimal getR31_no_of_atms() {
        return r31_no_of_atms;
    }

    public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
        this.r31_no_of_atms = r31_no_of_atms;
    }

    public BigDecimal getR31_no_of_mini_atms() {
        return r31_no_of_mini_atms;
    }

    public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
        this.r31_no_of_mini_atms = r31_no_of_mini_atms;
    }

    public BigDecimal getR31_encashment_points() {
        return r31_encashment_points;
    }

    public void setR31_encashment_points(BigDecimal r31_encashment_points) {
        this.r31_encashment_points = r31_encashment_points;
    }

    public String getR32_atm_mini_atm_district() {
        return r32_atm_mini_atm_district;
    }

    public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
        this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
    }

    public BigDecimal getR32_no_of_atms() {
        return r32_no_of_atms;
    }

    public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
        this.r32_no_of_atms = r32_no_of_atms;
    }

    public BigDecimal getR32_no_of_mini_atms() {
        return r32_no_of_mini_atms;
    }

    public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
        this.r32_no_of_mini_atms = r32_no_of_mini_atms;
    }

    public BigDecimal getR32_encashment_points() {
        return r32_encashment_points;
    }

    public void setR32_encashment_points(BigDecimal r32_encashment_points) {
        this.r32_encashment_points = r32_encashment_points;
    }

    public String getR33_atm_mini_atm_district() {
        return r33_atm_mini_atm_district;
    }

    public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
        this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
    }

    public BigDecimal getR33_no_of_atms() {
        return r33_no_of_atms;
    }

    public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
        this.r33_no_of_atms = r33_no_of_atms;
    }

    public BigDecimal getR33_no_of_mini_atms() {
        return r33_no_of_mini_atms;
    }

    public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
        this.r33_no_of_mini_atms = r33_no_of_mini_atms;
    }

    public BigDecimal getR33_encashment_points() {
        return r33_encashment_points;
    }

    public void setR33_encashment_points(BigDecimal r33_encashment_points) {
        this.r33_encashment_points = r33_encashment_points;
    }

    public String getR34_atm_mini_atm_district() {
        return r34_atm_mini_atm_district;
    }

    public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
        this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
    }

    public BigDecimal getR34_no_of_atms() {
        return r34_no_of_atms;
    }

    public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
        this.r34_no_of_atms = r34_no_of_atms;
    }

    public BigDecimal getR34_no_of_mini_atms() {
        return r34_no_of_mini_atms;
    }

    public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
        this.r34_no_of_mini_atms = r34_no_of_mini_atms;
    }

    public BigDecimal getR34_encashment_points() {
        return r34_encashment_points;
    }

    public void setR34_encashment_points(BigDecimal r34_encashment_points) {
        this.r34_encashment_points = r34_encashment_points;
    }

    public String getR35_atm_mini_atm_district() {
        return r35_atm_mini_atm_district;
    }

    public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
        this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
    }

    public BigDecimal getR35_no_of_atms() {
        return r35_no_of_atms;
    }

    public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
        this.r35_no_of_atms = r35_no_of_atms;
    }

    public BigDecimal getR35_no_of_mini_atms() {
        return r35_no_of_mini_atms;
    }

    public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
        this.r35_no_of_mini_atms = r35_no_of_mini_atms;
    }

    public BigDecimal getR35_encashment_points() {
        return r35_encashment_points;
    }

    public void setR35_encashment_points(BigDecimal r35_encashment_points) {
        this.r35_encashment_points = r35_encashment_points;
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

    public String getR25_atm_mini_atm_district() {
        return r25_atm_mini_atm_district;
    }

    public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
        this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
    }

    public BigDecimal getR25_no_of_atms() {
        return r25_no_of_atms;
    }

    public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
        this.r25_no_of_atms = r25_no_of_atms;
    }

    public BigDecimal getR25_no_of_mini_atms() {
        return r25_no_of_mini_atms;
    }

    public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
        this.r25_no_of_mini_atms = r25_no_of_mini_atms;
    }

    public BigDecimal getR25_encashment_points() {
        return r25_encashment_points;
    }

    public void setR25_encashment_points(BigDecimal r25_encashment_points) {
        this.r25_encashment_points = r25_encashment_points;
    }

    public Q_BRANCHNET_Archival_Summary_Entity2() {
        super();
    }

}