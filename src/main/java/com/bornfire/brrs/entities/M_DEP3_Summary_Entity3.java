
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
@Table(name = "BRRS_M_DEP3_SUMMARYTABLE3")
public class M_DEP3_Summary_Entity3 {

private String	r28_holder_of_frn_curr_acc;
private BigDecimal	r28_residents;
private BigDecimal	r28_non_residents;

private String	r29_holder_of_frn_curr_acc;
private BigDecimal	r29_residents;
private BigDecimal	r29_non_residents;

private String	r30_holder_of_frn_curr_acc;
private BigDecimal	r30_residents;
private BigDecimal	r30_non_residents;

private String	r31_holder_of_frn_curr_acc;
private BigDecimal	r31_residents;
private BigDecimal	r31_non_residents;

private String	r32_holder_of_frn_curr_acc;
private BigDecimal	r32_residents;
private BigDecimal	r32_non_residents;

private String	r33_holder_of_frn_curr_acc;
private BigDecimal	r33_residents;
private BigDecimal	r33_non_residents;

private String	r34_holder_of_frn_curr_acc;
private BigDecimal	r34_residents;
private BigDecimal	r34_non_residents;


@Temporal(TemporalType.DATE)
@DateTimeFormat(pattern = "dd/MM/yyyy")
@Id

private Date REPORT_DATE;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;

public String getR28_holder_of_frn_curr_acc() {
    return r28_holder_of_frn_curr_acc;
}

public void setR28_holder_of_frn_curr_acc(String r28_holder_of_frn_curr_acc) {
    this.r28_holder_of_frn_curr_acc = r28_holder_of_frn_curr_acc;
}

public BigDecimal getR28_residents() {
    return r28_residents;
}

public void setR28_residents(BigDecimal r28_residents) {
    this.r28_residents = r28_residents;
}

public BigDecimal getR28_non_residents() {
    return r28_non_residents;
}

public void setR28_non_residents(BigDecimal r28_non_residents) {
    this.r28_non_residents = r28_non_residents;
}

public String getR29_holder_of_frn_curr_acc() {
    return r29_holder_of_frn_curr_acc;
}

public void setR29_holder_of_frn_curr_acc(String r29_holder_of_frn_curr_acc) {
    this.r29_holder_of_frn_curr_acc = r29_holder_of_frn_curr_acc;
}

public BigDecimal getR29_residents() {
    return r29_residents;
}

public void setR29_residents(BigDecimal r29_residents) {
    this.r29_residents = r29_residents;
}

public BigDecimal getR29_non_residents() {
    return r29_non_residents;
}

public void setR29_non_residents(BigDecimal r29_non_residents) {
    this.r29_non_residents = r29_non_residents;
}

public String getR30_holder_of_frn_curr_acc() {
    return r30_holder_of_frn_curr_acc;
}

public void setR30_holder_of_frn_curr_acc(String r30_holder_of_frn_curr_acc) {
    this.r30_holder_of_frn_curr_acc = r30_holder_of_frn_curr_acc;
}

public BigDecimal getR30_residents() {
    return r30_residents;
}

public void setR30_residents(BigDecimal r30_residents) {
    this.r30_residents = r30_residents;
}

public BigDecimal getR30_non_residents() {
    return r30_non_residents;
}

public void setR30_non_residents(BigDecimal r30_non_residents) {
    this.r30_non_residents = r30_non_residents;
}

public String getR31_holder_of_frn_curr_acc() {
    return r31_holder_of_frn_curr_acc;
}

public void setR31_holder_of_frn_curr_acc(String r31_holder_of_frn_curr_acc) {
    this.r31_holder_of_frn_curr_acc = r31_holder_of_frn_curr_acc;
}

public BigDecimal getR31_residents() {
    return r31_residents;
}

public void setR31_residents(BigDecimal r31_residents) {
    this.r31_residents = r31_residents;
}

public BigDecimal getR31_non_residents() {
    return r31_non_residents;
}

public void setR31_non_residents(BigDecimal r31_non_residents) {
    this.r31_non_residents = r31_non_residents;
}

public String getR32_holder_of_frn_curr_acc() {
    return r32_holder_of_frn_curr_acc;
}

public void setR32_holder_of_frn_curr_acc(String r32_holder_of_frn_curr_acc) {
    this.r32_holder_of_frn_curr_acc = r32_holder_of_frn_curr_acc;
}

public BigDecimal getR32_residents() {
    return r32_residents;
}

public void setR32_residents(BigDecimal r32_residents) {
    this.r32_residents = r32_residents;
}

public BigDecimal getR32_non_residents() {
    return r32_non_residents;
}

public void setR32_non_residents(BigDecimal r32_non_residents) {
    this.r32_non_residents = r32_non_residents;
}

public String getR33_holder_of_frn_curr_acc() {
    return r33_holder_of_frn_curr_acc;
}

public void setR33_holder_of_frn_curr_acc(String r33_holder_of_frn_curr_acc) {
    this.r33_holder_of_frn_curr_acc = r33_holder_of_frn_curr_acc;
}

public BigDecimal getR33_residents() {
    return r33_residents;
}

public void setR33_residents(BigDecimal r33_residents) {
    this.r33_residents = r33_residents;
}

public BigDecimal getR33_non_residents() {
    return r33_non_residents;
}

public void setR33_non_residents(BigDecimal r33_non_residents) {
    this.r33_non_residents = r33_non_residents;
}

public String getR34_holder_of_frn_curr_acc() {
    return r34_holder_of_frn_curr_acc;
}

public void setR34_holder_of_frn_curr_acc(String r34_holder_of_frn_curr_acc) {
    this.r34_holder_of_frn_curr_acc = r34_holder_of_frn_curr_acc;
}

public BigDecimal getR34_residents() {
    return r34_residents;
}

public void setR34_residents(BigDecimal r34_residents) {
    this.r34_residents = r34_residents;
}

public BigDecimal getR34_non_residents() {
    return r34_non_residents;
}

public void setR34_non_residents(BigDecimal r34_non_residents) {
    this.r34_non_residents = r34_non_residents;
}

public Date getREPORT_DATE() {
    return REPORT_DATE;
}

public void setREPORT_DATE(Date REPORT_DATE) {
    this.REPORT_DATE = REPORT_DATE;
}

public String getReport_version() {
    return report_version;
}

public void setReport_version(String report_version) {
    this.report_version = report_version;
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

public M_DEP3_Summary_Entity3() {
    super();
}


}
