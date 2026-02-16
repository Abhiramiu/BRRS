
package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import java.math.BigDecimal;

public class M_CA5_Archival_Summary1_PK implements Serializable {

    private Date reportDate;
    private BigDecimal reportVersion;

    // default constructor
    public M_CA5_Archival_Summary1_PK() {
    }

    // parameterized constructor
    public M_CA5_Archival_Summary1_PK(Date reportDate, BigDecimal reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof M_CA5_Archival_Summary1_PK))
            return false;
        M_CA5_Archival_Summary1_PK that = (M_CA5_Archival_Summary1_PK) o;
        return Objects.equals(reportDate, that.reportDate) &&
                Objects.equals(reportVersion, that.reportVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportDate, reportVersion);
    }

    // getters & setters
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
}
