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
@Table(name = "BRRS_Q_BRANCHNET_SUMMARYTABLE4")
public class Q_BRANCHNET_Summary_Entity4 {

    private String r55_credit_district;
    private BigDecimal r55_opening_no_of_cards;
    private BigDecimal r55_no_of_cards_issued;
    private BigDecimal r55_no_cards_of_closed;
    private BigDecimal r55_closing_bal_of_active_cards;

    private String r56_credit_district;
    private BigDecimal r56_opening_no_of_cards;
    private BigDecimal r56_no_of_cards_issued;
    private BigDecimal r56_no_cards_of_closed;
    private BigDecimal r56_closing_bal_of_active_cards;

    private String r57_credit_district;
    private BigDecimal r57_opening_no_of_cards;
    private BigDecimal r57_no_of_cards_issued;
    private BigDecimal r57_no_cards_of_closed;
    private BigDecimal r57_closing_bal_of_active_cards;

    private String r58_credit_district;
    private BigDecimal r58_opening_no_of_cards;
    private BigDecimal r58_no_of_cards_issued;
    private BigDecimal r58_no_cards_of_closed;
    private BigDecimal r58_closing_bal_of_active_cards;

    private String r59_credit_district;
    private BigDecimal r59_opening_no_of_cards;
    private BigDecimal r59_no_of_cards_issued;
    private BigDecimal r59_no_cards_of_closed;
    private BigDecimal r59_closing_bal_of_active_cards;

    private String r60_credit_district;
    private BigDecimal r60_opening_no_of_cards;
    private BigDecimal r60_no_of_cards_issued;
    private BigDecimal r60_no_cards_of_closed;
    private BigDecimal r60_closing_bal_of_active_cards;

    private String r61_credit_district;
    private BigDecimal r61_opening_no_of_cards;
    private BigDecimal r61_no_of_cards_issued;
    private BigDecimal r61_no_cards_of_closed;
    private BigDecimal r61_closing_bal_of_active_cards;

    private String r62_credit_district;
    private BigDecimal r62_opening_no_of_cards;
    private BigDecimal r62_no_of_cards_issued;
    private BigDecimal r62_no_cards_of_closed;
    private BigDecimal r62_closing_bal_of_active_cards;

    private String r63_credit_district;
    private BigDecimal r63_opening_no_of_cards;
    private BigDecimal r63_no_of_cards_issued;
    private BigDecimal r63_no_cards_of_closed;
    private BigDecimal r63_closing_bal_of_active_cards;

    private String r64_credit_district;
    private BigDecimal r64_opening_no_of_cards;
    private BigDecimal r64_no_of_cards_issued;
    private BigDecimal r64_no_cards_of_closed;
    private BigDecimal r64_closing_bal_of_active_cards;

    private String r65_credit_district;
    private BigDecimal r65_opening_no_of_cards;
    private BigDecimal r65_no_of_cards_issued;
    private BigDecimal r65_no_cards_of_closed;
    private BigDecimal r65_closing_bal_of_active_cards;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
  
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;

    public String getR55_credit_district() {
        return r55_credit_district;
    }

    public void setR55_credit_district(String r55_credit_district) {
        this.r55_credit_district = r55_credit_district;
    }

    public BigDecimal getR55_opening_no_of_cards() {
        return r55_opening_no_of_cards;
    }

    public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
        this.r55_opening_no_of_cards = r55_opening_no_of_cards;
    }

    public BigDecimal getR55_no_of_cards_issued() {
        return r55_no_of_cards_issued;
    }

    public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
        this.r55_no_of_cards_issued = r55_no_of_cards_issued;
    }

    public BigDecimal getR55_no_cards_of_closed() {
        return r55_no_cards_of_closed;
    }

    public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
        this.r55_no_cards_of_closed = r55_no_cards_of_closed;
    }

    public BigDecimal getR55_closing_bal_of_active_cards() {
        return r55_closing_bal_of_active_cards;
    }

    public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
        this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
    }

    public String getR56_credit_district() {
        return r56_credit_district;
    }

    public void setR56_credit_district(String r56_credit_district) {
        this.r56_credit_district = r56_credit_district;
    }

    public BigDecimal getR56_opening_no_of_cards() {
        return r56_opening_no_of_cards;
    }

    public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
        this.r56_opening_no_of_cards = r56_opening_no_of_cards;
    }

    public BigDecimal getR56_no_of_cards_issued() {
        return r56_no_of_cards_issued;
    }

    public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
        this.r56_no_of_cards_issued = r56_no_of_cards_issued;
    }

    public BigDecimal getR56_no_cards_of_closed() {
        return r56_no_cards_of_closed;
    }

    public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
        this.r56_no_cards_of_closed = r56_no_cards_of_closed;
    }

    public BigDecimal getR56_closing_bal_of_active_cards() {
        return r56_closing_bal_of_active_cards;
    }

    public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
        this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
    }

    public String getR57_credit_district() {
        return r57_credit_district;
    }

    public void setR57_credit_district(String r57_credit_district) {
        this.r57_credit_district = r57_credit_district;
    }

    public BigDecimal getR57_opening_no_of_cards() {
        return r57_opening_no_of_cards;
    }

    public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
        this.r57_opening_no_of_cards = r57_opening_no_of_cards;
    }

    public BigDecimal getR57_no_of_cards_issued() {
        return r57_no_of_cards_issued;
    }

    public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
        this.r57_no_of_cards_issued = r57_no_of_cards_issued;
    }

    public BigDecimal getR57_no_cards_of_closed() {
        return r57_no_cards_of_closed;
    }

    public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
        this.r57_no_cards_of_closed = r57_no_cards_of_closed;
    }

    public BigDecimal getR57_closing_bal_of_active_cards() {
        return r57_closing_bal_of_active_cards;
    }

    public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
        this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
    }

    public String getR58_credit_district() {
        return r58_credit_district;
    }

    public void setR58_credit_district(String r58_credit_district) {
        this.r58_credit_district = r58_credit_district;
    }

    public BigDecimal getR58_opening_no_of_cards() {
        return r58_opening_no_of_cards;
    }

    public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
        this.r58_opening_no_of_cards = r58_opening_no_of_cards;
    }

    public BigDecimal getR58_no_of_cards_issued() {
        return r58_no_of_cards_issued;
    }

    public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
        this.r58_no_of_cards_issued = r58_no_of_cards_issued;
    }

    public BigDecimal getR58_no_cards_of_closed() {
        return r58_no_cards_of_closed;
    }

    public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
        this.r58_no_cards_of_closed = r58_no_cards_of_closed;
    }

    public BigDecimal getR58_closing_bal_of_active_cards() {
        return r58_closing_bal_of_active_cards;
    }

    public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
        this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
    }

    public String getR59_credit_district() {
        return r59_credit_district;
    }

    public void setR59_credit_district(String r59_credit_district) {
        this.r59_credit_district = r59_credit_district;
    }

    public BigDecimal getR59_opening_no_of_cards() {
        return r59_opening_no_of_cards;
    }

    public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
        this.r59_opening_no_of_cards = r59_opening_no_of_cards;
    }

    public BigDecimal getR59_no_of_cards_issued() {
        return r59_no_of_cards_issued;
    }

    public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
        this.r59_no_of_cards_issued = r59_no_of_cards_issued;
    }

    public BigDecimal getR59_no_cards_of_closed() {
        return r59_no_cards_of_closed;
    }

    public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
        this.r59_no_cards_of_closed = r59_no_cards_of_closed;
    }

    public BigDecimal getR59_closing_bal_of_active_cards() {
        return r59_closing_bal_of_active_cards;
    }

    public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
        this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
    }

    public String getR60_credit_district() {
        return r60_credit_district;
    }

    public void setR60_credit_district(String r60_credit_district) {
        this.r60_credit_district = r60_credit_district;
    }

    public BigDecimal getR60_opening_no_of_cards() {
        return r60_opening_no_of_cards;
    }

    public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
        this.r60_opening_no_of_cards = r60_opening_no_of_cards;
    }

    public BigDecimal getR60_no_of_cards_issued() {
        return r60_no_of_cards_issued;
    }

    public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
        this.r60_no_of_cards_issued = r60_no_of_cards_issued;
    }

    public BigDecimal getR60_no_cards_of_closed() {
        return r60_no_cards_of_closed;
    }

    public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
        this.r60_no_cards_of_closed = r60_no_cards_of_closed;
    }

    public BigDecimal getR60_closing_bal_of_active_cards() {
        return r60_closing_bal_of_active_cards;
    }

    public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
        this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
    }

    public String getR61_credit_district() {
        return r61_credit_district;
    }

    public void setR61_credit_district(String r61_credit_district) {
        this.r61_credit_district = r61_credit_district;
    }

    public BigDecimal getR61_opening_no_of_cards() {
        return r61_opening_no_of_cards;
    }

    public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
        this.r61_opening_no_of_cards = r61_opening_no_of_cards;
    }

    public BigDecimal getR61_no_of_cards_issued() {
        return r61_no_of_cards_issued;
    }

    public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
        this.r61_no_of_cards_issued = r61_no_of_cards_issued;
    }

    public BigDecimal getR61_no_cards_of_closed() {
        return r61_no_cards_of_closed;
    }

    public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
        this.r61_no_cards_of_closed = r61_no_cards_of_closed;
    }

    public BigDecimal getR61_closing_bal_of_active_cards() {
        return r61_closing_bal_of_active_cards;
    }

    public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
        this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
    }

    public String getR62_credit_district() {
        return r62_credit_district;
    }

    public void setR62_credit_district(String r62_credit_district) {
        this.r62_credit_district = r62_credit_district;
    }

    public BigDecimal getR62_opening_no_of_cards() {
        return r62_opening_no_of_cards;
    }

    public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
        this.r62_opening_no_of_cards = r62_opening_no_of_cards;
    }

    public BigDecimal getR62_no_of_cards_issued() {
        return r62_no_of_cards_issued;
    }

    public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
        this.r62_no_of_cards_issued = r62_no_of_cards_issued;
    }

    public BigDecimal getR62_no_cards_of_closed() {
        return r62_no_cards_of_closed;
    }

    public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
        this.r62_no_cards_of_closed = r62_no_cards_of_closed;
    }

    public BigDecimal getR62_closing_bal_of_active_cards() {
        return r62_closing_bal_of_active_cards;
    }

    public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
        this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
    }

    public String getR63_credit_district() {
        return r63_credit_district;
    }

    public void setR63_credit_district(String r63_credit_district) {
        this.r63_credit_district = r63_credit_district;
    }

    public BigDecimal getR63_opening_no_of_cards() {
        return r63_opening_no_of_cards;
    }

    public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
        this.r63_opening_no_of_cards = r63_opening_no_of_cards;
    }

    public BigDecimal getR63_no_of_cards_issued() {
        return r63_no_of_cards_issued;
    }

    public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
        this.r63_no_of_cards_issued = r63_no_of_cards_issued;
    }

    public BigDecimal getR63_no_cards_of_closed() {
        return r63_no_cards_of_closed;
    }

    public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
        this.r63_no_cards_of_closed = r63_no_cards_of_closed;
    }

    public BigDecimal getR63_closing_bal_of_active_cards() {
        return r63_closing_bal_of_active_cards;
    }

    public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
        this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
    }

    public String getR64_credit_district() {
        return r64_credit_district;
    }

    public void setR64_credit_district(String r64_credit_district) {
        this.r64_credit_district = r64_credit_district;
    }

    public BigDecimal getR64_opening_no_of_cards() {
        return r64_opening_no_of_cards;
    }

    public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
        this.r64_opening_no_of_cards = r64_opening_no_of_cards;
    }

    public BigDecimal getR64_no_of_cards_issued() {
        return r64_no_of_cards_issued;
    }

    public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
        this.r64_no_of_cards_issued = r64_no_of_cards_issued;
    }

    public BigDecimal getR64_no_cards_of_closed() {
        return r64_no_cards_of_closed;
    }

    public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
        this.r64_no_cards_of_closed = r64_no_cards_of_closed;
    }

    public BigDecimal getR64_closing_bal_of_active_cards() {
        return r64_closing_bal_of_active_cards;
    }

    public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
        this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
    }

    public String getR65_credit_district() {
        return r65_credit_district;
    }

    public void setR65_credit_district(String r65_credit_district) {
        this.r65_credit_district = r65_credit_district;
    }

    public BigDecimal getR65_opening_no_of_cards() {
        return r65_opening_no_of_cards;
    }

    public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
        this.r65_opening_no_of_cards = r65_opening_no_of_cards;
    }

    public BigDecimal getR65_no_of_cards_issued() {
        return r65_no_of_cards_issued;
    }

    public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
        this.r65_no_of_cards_issued = r65_no_of_cards_issued;
    }

    public BigDecimal getR65_no_cards_of_closed() {
        return r65_no_cards_of_closed;
    }

    public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
        this.r65_no_cards_of_closed = r65_no_cards_of_closed;
    }

    public BigDecimal getR65_closing_bal_of_active_cards() {
        return r65_closing_bal_of_active_cards;
    }

    public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
        this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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

    public Q_BRANCHNET_Summary_Entity4() {
        super();
    }

}
