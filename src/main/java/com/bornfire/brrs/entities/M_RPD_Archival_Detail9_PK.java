package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class M_RPD_Archival_Detail9_PK  implements Serializable {

    private Date reportDate;
    private String reportVersion;

    // default constructor
    public M_RPD_Archival_Detail9_PK() {
    }

    // parameterized constructor
    public M_RPD_Archival_Detail9_PK(Date reportDate, String reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof M_RPD_Archival_Detail9_PK))
            return false;
        M_RPD_Archival_Detail9_PK that = (M_RPD_Archival_Detail9_PK) o;
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

    public String getReportVersion() {
        return reportVersion;
    }

    public void setReportVersion(String reportVersion) {
        this.reportVersion = reportVersion;
    }
}
