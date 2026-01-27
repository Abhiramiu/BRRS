
package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.math.BigDecimal;

public class Q_STAFF_New_Archival_Summary3_PK implements Serializable {

    private Date reportDate;
    private BigDecimal reportVersion;

    // default constructor
    public Q_STAFF_New_Archival_Summary3_PK() {
    }

    // parameterized constructor
    public Q_STAFF_New_Archival_Summary3_PK(Date reportDate, BigDecimal reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Q_STAFF_New_Archival_Summary3_PK))
            return false;
        Q_STAFF_New_Archival_Summary3_PK that = (Q_STAFF_New_Archival_Summary3_PK) o;
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
