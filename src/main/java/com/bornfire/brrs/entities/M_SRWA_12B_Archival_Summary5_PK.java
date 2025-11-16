package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class M_SRWA_12B_Archival_Summary5_PK implements Serializable {

    private Date reportDate;
    private String reportVersion;

    public M_SRWA_12B_Archival_Summary5_PK() {
    }

    public M_SRWA_12B_Archival_Summary5_PK(Date reportDate, String reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_SRWA_12B_Archival_Summary5_PK)) return false;
        M_SRWA_12B_Archival_Summary5_PK that = (M_SRWA_12B_Archival_Summary5_PK) o;
        return Objects.equals(reportDate, that.reportDate) &&
               Objects.equals(reportVersion, that.reportVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportDate, reportVersion);
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
}
