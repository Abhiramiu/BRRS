package com.bornfire.brrs.dto;

public class ReportLineItemDTO {

    private int srlNo;
    private String fieldDescription;
    private String reportLabel;
    private String header;  // "Y" or "N"
    private String remarks;

    // Getters and Setters
    public int getSrlNo() { return srlNo; }
    public void setSrlNo(int srlNo) { this.srlNo = srlNo; }
    public String getFieldDescription() { return fieldDescription; }
    public void setFieldDescription(String fieldDescription) { this.fieldDescription = fieldDescription; }
    public String getReportLabel() { return reportLabel; }
    public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }
    public String getHeader() { return header; }
    public void setHeader(String header) { this.header = header; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}