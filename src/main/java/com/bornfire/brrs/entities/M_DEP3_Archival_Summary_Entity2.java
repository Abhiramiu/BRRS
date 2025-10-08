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
@Table(name = "BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY2")
public class M_DEP3_Archival_Summary_Entity2 {

private String	r28_pur_of_frn_cuu_acc;
private BigDecimal	r28_import;
private BigDecimal	r28_investment;
private BigDecimal	r28_other;

private String	r29_pur_of_frn_cuu_acc;
private BigDecimal	r29_import;
private BigDecimal	r29_investment;
private BigDecimal	r29_other;

private String	r30_pur_of_frn_cuu_acc;
private BigDecimal	r30_import;
private BigDecimal	r30_investment;
private BigDecimal	r30_other;

private String	r31_pur_of_frn_cuu_acc;
private BigDecimal	r31_import;
private BigDecimal	r31_investment;
private BigDecimal	r31_other;

private String	r32_pur_of_frn_cuu_acc;
private BigDecimal	r32_import;
private BigDecimal	r32_investment;
private BigDecimal	r32_other;

private String	r33_pur_of_frn_cuu_acc;
private BigDecimal	r33_import;
private BigDecimal	r33_investment;
private BigDecimal	r33_other;

private String	r34_pur_of_frn_cuu_acc;
private BigDecimal	r34_import;
private BigDecimal	r34_investment;
private BigDecimal	r34_other;

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

public String getR28_pur_of_frn_cuu_acc() {
    return r28_pur_of_frn_cuu_acc;
}
public void setR28_pur_of_frn_cuu_acc(String r28_pur_of_frn_cuu_acc) {
    this.r28_pur_of_frn_cuu_acc = r28_pur_of_frn_cuu_acc;
}
public BigDecimal getR28_import() {
    return r28_import;
}
public void setR28_import(BigDecimal r28_import) {
    this.r28_import = r28_import;
}
public BigDecimal getR28_investment() {
    return r28_investment;
}
public void setR28_investment(BigDecimal r28_investment) {
    this.r28_investment = r28_investment;
}
public BigDecimal getR28_other() {
    return r28_other;
}
public void setR28_other(BigDecimal r28_other) {
    this.r28_other = r28_other;
}
public String getR29_pur_of_frn_cuu_acc() {
    return r29_pur_of_frn_cuu_acc;
}
public void setR29_pur_of_frn_cuu_acc(String r29_pur_of_frn_cuu_acc) {
    this.r29_pur_of_frn_cuu_acc = r29_pur_of_frn_cuu_acc;
}
public BigDecimal getR29_import() {
    return r29_import;
}
public void setR29_import(BigDecimal r29_import) {
    this.r29_import = r29_import;
}
public BigDecimal getR29_investment() {
    return r29_investment;
}
public void setR29_investment(BigDecimal r29_investment) {
    this.r29_investment = r29_investment;
}
public BigDecimal getR29_other() {
    return r29_other;
}
public void setR29_other(BigDecimal r29_other) {
    this.r29_other = r29_other;
}
public String getR30_pur_of_frn_cuu_acc() {
    return r30_pur_of_frn_cuu_acc;
}
public void setR30_pur_of_frn_cuu_acc(String r30_pur_of_frn_cuu_acc) {
    this.r30_pur_of_frn_cuu_acc = r30_pur_of_frn_cuu_acc;
}
public BigDecimal getR30_import() {
    return r30_import;
}
public void setR30_import(BigDecimal r30_import) {
    this.r30_import = r30_import;
}
public BigDecimal getR30_investment() {
    return r30_investment;
}
public void setR30_investment(BigDecimal r30_investment) {
    this.r30_investment = r30_investment;
}
public BigDecimal getR30_other() {
    return r30_other;
}
public void setR30_other(BigDecimal r30_other) {
    this.r30_other = r30_other;
}
public String getR31_pur_of_frn_cuu_acc() {
    return r31_pur_of_frn_cuu_acc;
}
public void setR31_pur_of_frn_cuu_acc(String r31_pur_of_frn_cuu_acc) {
    this.r31_pur_of_frn_cuu_acc = r31_pur_of_frn_cuu_acc;
}
public BigDecimal getR31_import() {
    return r31_import;
}
public void setR31_import(BigDecimal r31_import) {
    this.r31_import = r31_import;
}
public BigDecimal getR31_investment() {
    return r31_investment;
}
public void setR31_investment(BigDecimal r31_investment) {
    this.r31_investment = r31_investment;
}
public BigDecimal getR31_other() {
    return r31_other;
}
public void setR31_other(BigDecimal r31_other) {
    this.r31_other = r31_other;
}
public String getR32_pur_of_frn_cuu_acc() {
    return r32_pur_of_frn_cuu_acc;
}
public void setR32_pur_of_frn_cuu_acc(String r32_pur_of_frn_cuu_acc) {
    this.r32_pur_of_frn_cuu_acc = r32_pur_of_frn_cuu_acc;
}
public BigDecimal getR32_import() {
    return r32_import;
}
public void setR32_import(BigDecimal r32_import) {
    this.r32_import = r32_import;
}
public BigDecimal getR32_investment() {
    return r32_investment;
}
public void setR32_investment(BigDecimal r32_investment) {
    this.r32_investment = r32_investment;
}
public BigDecimal getR32_other() {
    return r32_other;
}
public void setR32_other(BigDecimal r32_other) {
    this.r32_other = r32_other;
}
public String getR33_pur_of_frn_cuu_acc() {
    return r33_pur_of_frn_cuu_acc;
}
public void setR33_pur_of_frn_cuu_acc(String r33_pur_of_frn_cuu_acc) {
    this.r33_pur_of_frn_cuu_acc = r33_pur_of_frn_cuu_acc;
}
public BigDecimal getR33_import() {
    return r33_import;
}
public void setR33_import(BigDecimal r33_import) {
    this.r33_import = r33_import;
}
public BigDecimal getR33_investment() {
    return r33_investment;
}
public void setR33_investment(BigDecimal r33_investment) {
    this.r33_investment = r33_investment;
}
public BigDecimal getR33_other() {
    return r33_other;
}
public void setR33_other(BigDecimal r33_other) {
    this.r33_other = r33_other;
}
public String getR34_pur_of_frn_cuu_acc() {
    return r34_pur_of_frn_cuu_acc;
}
public void setR34_pur_of_frn_cuu_acc(String r34_pur_of_frn_cuu_acc) {
    this.r34_pur_of_frn_cuu_acc = r34_pur_of_frn_cuu_acc;
}
public BigDecimal getR34_import() {
    return r34_import;
}
public void setR34_import(BigDecimal r34_import) {
    this.r34_import = r34_import;
}
public BigDecimal getR34_investment() {
    return r34_investment;
}
public void setR34_investment(BigDecimal r34_investment) {
    this.r34_investment = r34_investment;
}
public BigDecimal getR34_other() {
    return r34_other;
}
public void setR34_other(BigDecimal r34_other) {
    this.r34_other = r34_other;
}
public Date getReport_date() {
    return REPORT_DATE;
}
public void setReport_date(Date REPORT_DATE) {
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

public M_DEP3_Archival_Summary_Entity2() {
    super();
}




}
