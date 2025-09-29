package com.bornfire.brrs.entities;

public class ReportLineItemDTO {
	 private String reportCode;
	    private String fieldDescription;
	    private String reportLabel;
	    private String header;
	    private String remarks;
	    private String rowNumber; // R1, R2, ...

	    // Constructors
	    public ReportLineItemDTO() {}

	    public ReportLineItemDTO(String reportCode, String fieldDescription, String reportLabel, String header, String remarks, String rowNumber) {
	        this.reportCode = reportCode;
	        this.fieldDescription = fieldDescription;
	        this.reportLabel = reportLabel;
	        this.header = header;
	        this.remarks = remarks;
	        this.rowNumber = rowNumber;
	    }

	    // Getters and setters
	    public String getReportCode() { return reportCode; }
	    public void setReportCode(String reportCode) { this.reportCode = reportCode; }

	    public String getFieldDescription() { return fieldDescription; }
	    public void setFieldDescription(String fieldDescription) { this.fieldDescription = fieldDescription; }

	    public String getReportLabel() { return reportLabel; }
	    public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }

	    public String getHeader() { return header; }
	    public void setHeader(String header) { this.header = header; }

	    public String getRemarks() { return remarks; }
	    public void setRemarks(String remarks) { this.remarks = remarks; }

	    public String getRowNumber() { return rowNumber; }
	    public void setRowNumber(String rowNumber) { this.rowNumber = rowNumber; }

	    @Override
	    public String toString() {
	        return "ReportLineItemDTO{" +
	                "reportCode='" + reportCode + '\'' +
	                ", fieldDescription='" + fieldDescription + '\'' +
	                ", reportLabel='" + reportLabel + '\'' +
	                ", header='" + header + '\'' +
	                ", remarks='" + remarks + '\'' +
	                ", rowNumber='" + rowNumber + '\'' +
	                '}';
	    }
}
