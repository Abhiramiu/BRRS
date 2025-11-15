package com.bornfire.brrs.dto;

public class ReportLineItemDTO {
	private int srlNo;
	private String fieldDescription;
	private String reportLabel;
	private String header;
	private String remarks;

	public ReportLineItemDTO() {
	}

	public ReportLineItemDTO(int srlNo, String fieldDescription, String reportLabel, String header, String remarks) {
		this.srlNo = srlNo;
		this.fieldDescription = fieldDescription;
		this.reportLabel = reportLabel;
		this.header = header;
		this.remarks = remarks;
	}

	public int getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(int srlNo) {
		this.srlNo = srlNo;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public String getReportLabel() {
		return reportLabel;
	}

	public void setReportLabel(String reportLabel) {
		this.reportLabel = reportLabel;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "ReportLineItemDTO{" + "srlNo=" + srlNo + ", fieldDescription='" + fieldDescription + '\''
				+ ", reportLabel='" + reportLabel + '\'' + ", header='" + header + '\'' + ", remarks='" + remarks + '\''
				+ '}';
	}
}