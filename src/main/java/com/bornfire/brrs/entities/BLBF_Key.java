package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BLBF_Key implements Serializable {
    private String account_no;
    private Date report_date;

    public BLBF_Key() {}

    public BLBF_Key(String account_no, Date report_date) {
        this.account_no = account_no;
        this.report_date = report_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BLBF_Key)) return false;
        BLBF_Key key = (BLBF_Key) o;
        return Objects.equals(account_no, key.account_no) &&
               Objects.equals(report_date, key.report_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account_no, report_date);
    }
}
