package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class FORMAT_III_PK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date reportDate;
    private BigDecimal reportVersion;

    // Default constructor
    public FORMAT_III_PK() {}

    // Parameterized constructor
    public FORMAT_III_PK(Date reportDate, BigDecimal reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // Getters & Setters
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

    // equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FORMAT_III_PK that = (FORMAT_III_PK) o;
        return Objects.equals(reportDate, that.reportDate) &&
               Objects.equals(reportVersion, that.reportVersion);
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(reportDate, reportVersion);
    }
}