package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class M_SECL_Resub_Summary_PK implements Serializable {

    private Date report_date;
    private BigDecimal report_version;

    // default constructor
    public M_SECL_Resub_Summary_PK() {}

    // parameterized constructor
    public M_SECL_Resub_Summary_PK(Date report_date, BigDecimal report_version) {
        this.report_date = report_date;
        this.report_version = report_version;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_SECL_Resub_Summary_PK)) return false;
        M_SECL_Resub_Summary_PK that = (M_SECL_Resub_Summary_PK) o;
        return Objects.equals(report_date, that.report_date) &&
               Objects.equals(report_version, that.report_version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(report_date, report_version);
    }

    // getters & setters
    public Date getReportDate() { return report_date; }
    public void setReportDate(Date report_date) { this.report_date = report_date; }

    public BigDecimal getReportVersion() { return report_version; }
    public void setReportVersion(BigDecimal report_version) { this.report_version = report_version; }
}

