package com.bornfire.brrs.entities;
import java.io.Serializable;
import java.math.BigDecimal;

import java.util.Date;
import java.util.Objects;

public class M_SFINP2_PK implements Serializable {

    private Date REPORT_DATE;
    private BigDecimal REPORT_VERSION;

    // default constructor
    public M_SFINP2_PK() {}

    // parameterized constructor
    public M_SFINP2_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
        this.REPORT_DATE = REPORT_DATE;
        this.REPORT_VERSION = REPORT_VERSION;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_SFINP2_PK)) return false;
        M_SFINP2_PK that = (M_SFINP2_PK) o;
        return Objects.equals(REPORT_DATE, that.REPORT_DATE) &&
               Objects.equals(REPORT_VERSION, that.REPORT_VERSION);
    }

    @Override
    public int hashCode() {
        return Objects.hash(REPORT_DATE, REPORT_VERSION);
    }

    // getters & setters
    public Date getREPORT_DATE() { return REPORT_DATE; }
    public void setREPORT_DATE(Date REPORT_DATE) { this.REPORT_DATE = REPORT_DATE; }

    public BigDecimal getREPORT_VERSION() { return REPORT_VERSION; }
    public void setREPORT_VERSION(BigDecimal REPORT_VERSION) { this.REPORT_VERSION = REPORT_VERSION; }
}

