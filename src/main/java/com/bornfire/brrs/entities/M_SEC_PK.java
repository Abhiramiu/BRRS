
package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class M_SEC_PK implements Serializable {

    private Date report_date;
    private BigDecimal report_version;

    // default constructor
    public M_SEC_PK() {
    }

    // parameterized constructor
    public M_SEC_PK(Date report_date, BigDecimal report_version) {
        this.report_date = report_date;
        this.report_version = report_version;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof M_SEC_PK))
            return false;
        M_SEC_PK that = (M_SEC_PK) o;
        return Objects.equals(report_date, that.report_date) &&
                Objects.equals(report_version, that.report_version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(report_date, report_version);
    }

    // getters & setters
    public Date getreport_date() {
        return report_date;
    }

    public void setreport_date(Date report_date) {
        this.report_date = report_date;
    }

    public BigDecimal getreport_version() {
        return report_version;
    }

    public void setreport_version(BigDecimal report_version) {
        this.report_version = report_version;
    }
}
