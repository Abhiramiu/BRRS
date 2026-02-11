package com.bornfire.brrs.entities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
public class M_SRWA_12D_PK implements Serializable{
	 // private static final long serialVersionUID = 1L;

	    private Date report_date;
	    private BigDecimal report_version;

	    // mandatory no-arg constructor
	    public M_SRWA_12D_PK() {
	    }

	    // optional parameterized constructor
	    public M_SRWA_12D_PK(Date report_date, BigDecimal report_version) {
	        this.report_date = report_date;
	        this.report_version = report_version;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof M_SRWA_12D_PK)) return false;
	        M_SRWA_12D_PK that = (M_SRWA_12D_PK) o;
	        return Objects.equals(report_date, that.report_date) &&
	               Objects.equals(report_version, that.report_version);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(report_date, report_version);
	    }

	    public Date getReport_date() {
	        return report_date;
	    }

	    public void setReport_date(Date report_date) {
	        this.report_date = report_date;
	    }

	    public BigDecimal getReport_version() {
	        return report_version;
	    }

	    public void setReport_version(BigDecimal report_version) {
	        this.report_version = report_version;
	    }
}
