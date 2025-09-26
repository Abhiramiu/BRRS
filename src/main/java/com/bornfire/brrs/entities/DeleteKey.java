package com.bornfire.brrs.entities;


import java.util.Date;

public class DeleteKey {
    private String glCode;
    private String glSubCode;
    private String headAccNo;
    private String currency;
    private Date reportDate;

    public DeleteKey(String glCode, String glSubCode, String headAccNo, String currency, Date reportDate) {
        this.glCode = glCode;
        this.glSubCode = glSubCode;
        this.headAccNo = headAccNo;
        this.currency = currency;
        this.reportDate = reportDate;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getGlSubCode() {
        return glSubCode;
    }

    public void setGlSubCode(String glSubCode) {
        this.glSubCode = glSubCode;
    }

    public String getHeadAccNo() {
        return headAccNo;
    }

    public void setHeadAccNo(String headAccNo) {
        this.headAccNo = headAccNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
}
