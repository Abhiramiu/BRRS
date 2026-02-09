package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Q_RLFA1_Archival_Detail_PK implements Serializable {
	
    private Date report_date;
    private BigDecimal report_version;

    // ✅ Default constructor is REQUIRED
    public Q_RLFA1_Archival_Detail_PK() {
    }

    public Q_RLFA1_Archival_Detail_PK(Date report_date, BigDecimal report_version) {
        this.report_date = report_date;
        this.report_version = report_version;
    }

    // ✅ MUST override equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Q_RLFA1_Archival_Detail_PK)) return false;
        Q_RLFA1_Archival_Detail_PK that = (Q_RLFA1_Archival_Detail_PK) o;
        return Objects.equals(report_date, that.report_date) &&
               Objects.equals(report_version, that.report_version);
    }

    // ✅ MUST override hashCode
    @Override
    public int hashCode() {
        return Objects.hash(report_date, report_version);
    }

}
