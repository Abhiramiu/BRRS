package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Common_Disclosure_PK implements Serializable {

    private Date REPORT_DATE;
    private BigDecimal REPORT_VERSION;

    public Common_Disclosure_PK() {}

    public Common_Disclosure_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
        this.REPORT_DATE = REPORT_DATE;
        this.REPORT_VERSION = REPORT_VERSION;
    }

    // equals and hashCode (MANDATORY)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Common_Disclosure_PK)) return false;
        Common_Disclosure_PK that = (Common_Disclosure_PK) o;
        return Objects.equals(REPORT_DATE, that.REPORT_DATE) &&
               Objects.equals(REPORT_VERSION, that.REPORT_VERSION);
    }

    @Override
    public int hashCode() {
        return Objects.hash(REPORT_DATE, REPORT_VERSION);
    }
}