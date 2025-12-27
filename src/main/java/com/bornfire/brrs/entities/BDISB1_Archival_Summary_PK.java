package com.bornfire.brrs.entities;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BDISB1_Archival_Summary_PK implements Serializable {

    private Date reportDate;
    private String reportVersion;

    // default constructor
    public BDISB1_Archival_Summary_PK() {}

    // parameterized constructor
    public BDISB1_Archival_Summary_PK(Date reportDate, String reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BDISB1_Archival_Summary_PK)) return false;
        BDISB1_Archival_Summary_PK that = (BDISB1_Archival_Summary_PK) o;
        return Objects.equals(reportDate, that.reportDate) &&
               Objects.equals(reportVersion, that.reportVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportDate, reportVersion);
    }

    // getters & setters
    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public String getReportVersion() { return reportVersion; }
    public void setReportVersion(String reportVersion) { this.reportVersion = reportVersion; }
}

