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
@Table(name = "BRRS_Q_BRANCHNET_ARCHIVAL_SUMMARYTABLE3")
public class Q_BRANCHNET_Archival_Summary_Entity3 {
    
private String	r40_debit_district;
private BigDecimal	r40_opening_no_of_cards;
private BigDecimal	r40_no_of_cards_issued;
private BigDecimal	r40_no_cards_of_closed;
private BigDecimal	r40_closing_bal_of_active_cards;
 
private String	r41_debit_district;
private BigDecimal	r41_opening_no_of_cards;
private BigDecimal	r41_no_of_cards_issued;
private BigDecimal	r41_no_cards_of_closed;
private BigDecimal	r41_closing_bal_of_active_cards;
 
private String	r42_debit_district;
private BigDecimal	r42_opening_no_of_cards;
private BigDecimal	r42_no_of_cards_issued;
private BigDecimal	r42_no_cards_of_closed;
private BigDecimal	r42_closing_bal_of_active_cards;
 
private String	r43_debit_district;
private BigDecimal	r43_opening_no_of_cards;
private BigDecimal	r43_no_of_cards_issued;
private BigDecimal	r43_no_cards_of_closed;
private BigDecimal	r43_closing_bal_of_active_cards;
 
 
private String	r44_debit_district;
private BigDecimal	r44_opening_no_of_cards;
private BigDecimal	r44_no_of_cards_issued;
private BigDecimal	r44_no_cards_of_closed;
private BigDecimal	r44_closing_bal_of_active_cards;
 
 
private String	r45_debit_district;
private BigDecimal	r45_opening_no_of_cards;
private BigDecimal	r45_no_of_cards_issued;
private BigDecimal	r45_no_cards_of_closed;
private BigDecimal	r45_closing_bal_of_active_cards;
 
 
private String	r46_debit_district;
private BigDecimal	r46_opening_no_of_cards;
private BigDecimal	r46_no_of_cards_issued;
private BigDecimal	r46_no_cards_of_closed;
private BigDecimal	r46_closing_bal_of_active_cards;
 
 
private String	r47_debit_district;
private BigDecimal	r47_opening_no_of_cards;
private BigDecimal	r47_no_of_cards_issued;
private BigDecimal	r47_no_cards_of_closed;
private BigDecimal	r47_closing_bal_of_active_cards;
  
private String	r48_debit_district;
private BigDecimal	r48_opening_no_of_cards;
private BigDecimal	r48_no_of_cards_issued;
private BigDecimal	r48_no_cards_of_closed;
private BigDecimal	r48_closing_bal_of_active_cards;
 
 
private String	r49_debit_district;
private BigDecimal	r49_opening_no_of_cards;
private BigDecimal	r49_no_of_cards_issued;
private BigDecimal	r49_no_cards_of_closed;
private BigDecimal	r49_closing_bal_of_active_cards;
 
private String	r50_debit_district;
private BigDecimal	r50_opening_no_of_cards;
private BigDecimal	r50_no_of_cards_issued;
private BigDecimal	r50_no_cards_of_closed;
private BigDecimal	r50_closing_bal_of_active_cards;

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

public String getR40_debit_district() {
    return r40_debit_district;
}
public void setR40_debit_district(String r40_debit_district) {
    this.r40_debit_district = r40_debit_district;
}
public BigDecimal getR40_opening_no_of_cards() {
    return r40_opening_no_of_cards;
}
public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
    this.r40_opening_no_of_cards = r40_opening_no_of_cards;
}
public BigDecimal getR40_no_of_cards_issued() {
    return r40_no_of_cards_issued;
}
public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
    this.r40_no_of_cards_issued = r40_no_of_cards_issued;
}
public BigDecimal getR40_no_cards_of_closed() {
    return r40_no_cards_of_closed;
}
public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
    this.r40_no_cards_of_closed = r40_no_cards_of_closed;
}
public BigDecimal getR40_closing_bal_of_active_cards() {
    return r40_closing_bal_of_active_cards;
}
public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
    this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
}
public String getR41_debit_district() {
    return r41_debit_district;
}
public void setR41_debit_district(String r41_debit_district) {
    this.r41_debit_district = r41_debit_district;
}
public BigDecimal getR41_opening_no_of_cards() {
    return r41_opening_no_of_cards;
}
public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
    this.r41_opening_no_of_cards = r41_opening_no_of_cards;
}
public BigDecimal getR41_no_of_cards_issued() {
    return r41_no_of_cards_issued;
}
public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
    this.r41_no_of_cards_issued = r41_no_of_cards_issued;
}
public BigDecimal getR41_no_cards_of_closed() {
    return r41_no_cards_of_closed;
}
public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
    this.r41_no_cards_of_closed = r41_no_cards_of_closed;
}
public BigDecimal getR41_closing_bal_of_active_cards() {
    return r41_closing_bal_of_active_cards;
}
public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
    this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
}
public String getR42_debit_district() {
    return r42_debit_district;
}
public void setR42_debit_district(String r42_debit_district) {
    this.r42_debit_district = r42_debit_district;
}
public BigDecimal getR42_opening_no_of_cards() {
    return r42_opening_no_of_cards;
}
public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
    this.r42_opening_no_of_cards = r42_opening_no_of_cards;
}
public BigDecimal getR42_no_of_cards_issued() {
    return r42_no_of_cards_issued;
}
public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
    this.r42_no_of_cards_issued = r42_no_of_cards_issued;
}
public BigDecimal getR42_no_cards_of_closed() {
    return r42_no_cards_of_closed;
}
public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
    this.r42_no_cards_of_closed = r42_no_cards_of_closed;
}
public BigDecimal getR42_closing_bal_of_active_cards() {
    return r42_closing_bal_of_active_cards;
}
public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
    this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
}
public String getR43_debit_district() {
    return r43_debit_district;
}
public void setR43_debit_district(String r43_debit_district) {
    this.r43_debit_district = r43_debit_district;
}
public BigDecimal getR43_opening_no_of_cards() {
    return r43_opening_no_of_cards;
}
public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
    this.r43_opening_no_of_cards = r43_opening_no_of_cards;
}
public BigDecimal getR43_no_of_cards_issued() {
    return r43_no_of_cards_issued;
}
public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
    this.r43_no_of_cards_issued = r43_no_of_cards_issued;
}
public BigDecimal getR43_no_cards_of_closed() {
    return r43_no_cards_of_closed;
}
public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
    this.r43_no_cards_of_closed = r43_no_cards_of_closed;
}
public BigDecimal getR43_closing_bal_of_active_cards() {
    return r43_closing_bal_of_active_cards;
}
public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
    this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
}
public String getR44_debit_district() {
    return r44_debit_district;
}
public void setR44_debit_district(String r44_debit_district) {
    this.r44_debit_district = r44_debit_district;
}
public BigDecimal getR44_opening_no_of_cards() {
    return r44_opening_no_of_cards;
}
public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
    this.r44_opening_no_of_cards = r44_opening_no_of_cards;
}
public BigDecimal getR44_no_of_cards_issued() {
    return r44_no_of_cards_issued;
}
public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
    this.r44_no_of_cards_issued = r44_no_of_cards_issued;
}
public BigDecimal getR44_no_cards_of_closed() {
    return r44_no_cards_of_closed;
}
public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
    this.r44_no_cards_of_closed = r44_no_cards_of_closed;
}
public BigDecimal getR44_closing_bal_of_active_cards() {
    return r44_closing_bal_of_active_cards;
}
public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
    this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
}
public String getR45_debit_district() {
    return r45_debit_district;
}
public void setR45_debit_district(String r45_debit_district) {
    this.r45_debit_district = r45_debit_district;
}
public BigDecimal getR45_opening_no_of_cards() {
    return r45_opening_no_of_cards;
}
public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
    this.r45_opening_no_of_cards = r45_opening_no_of_cards;
}
public BigDecimal getR45_no_of_cards_issued() {
    return r45_no_of_cards_issued;
}
public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
    this.r45_no_of_cards_issued = r45_no_of_cards_issued;
}
public BigDecimal getR45_no_cards_of_closed() {
    return r45_no_cards_of_closed;
}
public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
    this.r45_no_cards_of_closed = r45_no_cards_of_closed;
}
public BigDecimal getR45_closing_bal_of_active_cards() {
    return r45_closing_bal_of_active_cards;
}
public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
    this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
}
public String getR46_debit_district() {
    return r46_debit_district;
}
public void setR46_debit_district(String r46_debit_district) {
    this.r46_debit_district = r46_debit_district;
}
public BigDecimal getR46_opening_no_of_cards() {
    return r46_opening_no_of_cards;
}
public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
    this.r46_opening_no_of_cards = r46_opening_no_of_cards;
}
public BigDecimal getR46_no_of_cards_issued() {
    return r46_no_of_cards_issued;
}
public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
    this.r46_no_of_cards_issued = r46_no_of_cards_issued;
}
public BigDecimal getR46_no_cards_of_closed() {
    return r46_no_cards_of_closed;
}
public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
    this.r46_no_cards_of_closed = r46_no_cards_of_closed;
}
public BigDecimal getR46_closing_bal_of_active_cards() {
    return r46_closing_bal_of_active_cards;
}
public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
    this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
}
public String getR47_debit_district() {
    return r47_debit_district;
}
public void setR47_debit_district(String r47_debit_district) {
    this.r47_debit_district = r47_debit_district;
}
public BigDecimal getR47_opening_no_of_cards() {
    return r47_opening_no_of_cards;
}
public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
    this.r47_opening_no_of_cards = r47_opening_no_of_cards;
}
public BigDecimal getR47_no_of_cards_issued() {
    return r47_no_of_cards_issued;
}
public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
    this.r47_no_of_cards_issued = r47_no_of_cards_issued;
}
public BigDecimal getR47_no_cards_of_closed() {
    return r47_no_cards_of_closed;
}
public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
    this.r47_no_cards_of_closed = r47_no_cards_of_closed;
}
public BigDecimal getR47_closing_bal_of_active_cards() {
    return r47_closing_bal_of_active_cards;
}
public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
    this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
}
public String getR48_debit_district() {
    return r48_debit_district;
}
public void setR48_debit_district(String r48_debit_district) {
    this.r48_debit_district = r48_debit_district;
}
public BigDecimal getR48_opening_no_of_cards() {
    return r48_opening_no_of_cards;
}
public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
    this.r48_opening_no_of_cards = r48_opening_no_of_cards;
}
public BigDecimal getR48_no_of_cards_issued() {
    return r48_no_of_cards_issued;
}
public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
    this.r48_no_of_cards_issued = r48_no_of_cards_issued;
}
public BigDecimal getR48_no_cards_of_closed() {
    return r48_no_cards_of_closed;
}
public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
    this.r48_no_cards_of_closed = r48_no_cards_of_closed;
}
public BigDecimal getR48_closing_bal_of_active_cards() {
    return r48_closing_bal_of_active_cards;
}
public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
    this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
}
public String getR49_debit_district() {
    return r49_debit_district;
}
public void setR49_debit_district(String r49_debit_district) {
    this.r49_debit_district = r49_debit_district;
}
public BigDecimal getR49_opening_no_of_cards() {
    return r49_opening_no_of_cards;
}
public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
    this.r49_opening_no_of_cards = r49_opening_no_of_cards;
}
public BigDecimal getR49_no_of_cards_issued() {
    return r49_no_of_cards_issued;
}
public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
    this.r49_no_of_cards_issued = r49_no_of_cards_issued;
}
public BigDecimal getR49_no_cards_of_closed() {
    return r49_no_cards_of_closed;
}
public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
    this.r49_no_cards_of_closed = r49_no_cards_of_closed;
}
public BigDecimal getR49_closing_bal_of_active_cards() {
    return r49_closing_bal_of_active_cards;
}
public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
    this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
}
public String getR50_debit_district() {
    return r50_debit_district;
}
public void setR50_debit_district(String r50_debit_district) {
    this.r50_debit_district = r50_debit_district;
}
public BigDecimal getR50_opening_no_of_cards() {
    return r50_opening_no_of_cards;
}
public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
    this.r50_opening_no_of_cards = r50_opening_no_of_cards;
}
public BigDecimal getR50_no_of_cards_issued() {
    return r50_no_of_cards_issued;
}
public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
    this.r50_no_of_cards_issued = r50_no_of_cards_issued;
}
public BigDecimal getR50_no_cards_of_closed() {
    return r50_no_cards_of_closed;
}
public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
    this.r50_no_cards_of_closed = r50_no_cards_of_closed;
}
public BigDecimal getR50_closing_bal_of_active_cards() {
    return r50_closing_bal_of_active_cards;
}
public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
    this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
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
public Q_BRANCHNET_Archival_Summary_Entity3() {
    super();
}



}
