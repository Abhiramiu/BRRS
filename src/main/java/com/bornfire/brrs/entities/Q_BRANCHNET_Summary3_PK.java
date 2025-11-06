package com.bornfire.brrs.entities;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Q_BRANCHNET_Summary3_PK implements Serializable {

    private Date reportDate;
    private String reportVersion;

    public Q_BRANCHNET_Summary3_PK() {}

    public Q_BRANCHNET_Summary3_PK(Date reportDate, String reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Q_BRANCHNET_Summary3_PK)) return false;
        Q_BRANCHNET_Summary3_PK that = (Q_BRANCHNET_Summary3_PK) o;
        return Objects.equals(reportDate, that.reportDate) &&
               Objects.equals(reportVersion, that.reportVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportDate, reportVersion);
    }

    // getters/setters if needed
}
