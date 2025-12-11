package com.bornfire.brrs.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_MDISB5_SUMMARYTABLE3")

public class MDISB5_Summary_Entity3 {
	
	private String R37_NAME;
    private String R37_DESIGNATION_OR_POSITION;
    private Double R37_NUMBER_OF_ACCOUNTS;
    private Double R37_AMOUNT;

    private String R38_NAME;
    private String R38_DESIGNATION_OR_POSITION;
    private Double R38_NUMBER_OF_ACCOUNTS;
    private Double R38_AMOUNT;

    private String R39_NAME;
    private String R39_DESIGNATION_OR_POSITION;
    private Double R39_NUMBER_OF_ACCOUNTS;
    private Double R39_AMOUNT;

    private String R40_NAME;
    private String R40_DESIGNATION_OR_POSITION;
    private Double R40_NUMBER_OF_ACCOUNTS;
    private Double R40_AMOUNT;

    private String R41_NAME;
    private String R41_DESIGNATION_OR_POSITION;
    private Double R41_NUMBER_OF_ACCOUNTS;
    private Double R41_AMOUNT;

    private String R42_NAME;
    private String R42_DESIGNATION_OR_POSITION;
    private Double R42_NUMBER_OF_ACCOUNTS;
    private Double R42_AMOUNT;

    private String R43_NAME;
    private String R43_DESIGNATION_OR_POSITION;
    private Double R43_NUMBER_OF_ACCOUNTS;
    private Double R43_AMOUNT;

    private String R44_NAME;
    private String R44_DESIGNATION_OR_POSITION;
    private Double R44_NUMBER_OF_ACCOUNTS;
    private Double R44_AMOUNT;

    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Id
    @Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
    private String REPORT_FREQUENCY;
    private String REPORT_CODE;
    private String REPORT_DESC;
    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DELETE_FLG;
	public String getR37_NAME() {
		return R37_NAME;
	}
	public void setR37_NAME(String r37_NAME) {
		R37_NAME = r37_NAME;
	}
	public String getR37_DESIGNATION_OR_POSITION() {
		return R37_DESIGNATION_OR_POSITION;
	}
	public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
		R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
	}
	public Double getR37_NUMBER_OF_ACCOUNTS() {
		return R37_NUMBER_OF_ACCOUNTS;
	}
	public void setR37_NUMBER_OF_ACCOUNTS(Double r37_NUMBER_OF_ACCOUNTS) {
		R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
	}
	public Double getR37_AMOUNT() {
		return R37_AMOUNT;
	}
	public void setR37_AMOUNT(Double r37_AMOUNT) {
		R37_AMOUNT = r37_AMOUNT;
	}
	public String getR38_NAME() {
		return R38_NAME;
	}
	public void setR38_NAME(String r38_NAME) {
		R38_NAME = r38_NAME;
	}
	public String getR38_DESIGNATION_OR_POSITION() {
		return R38_DESIGNATION_OR_POSITION;
	}
	public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
		R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
	}
	public Double getR38_NUMBER_OF_ACCOUNTS() {
		return R38_NUMBER_OF_ACCOUNTS;
	}
	public void setR38_NUMBER_OF_ACCOUNTS(Double r38_NUMBER_OF_ACCOUNTS) {
		R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
	}
	public Double getR38_AMOUNT() {
		return R38_AMOUNT;
	}
	public void setR38_AMOUNT(Double r38_AMOUNT) {
		R38_AMOUNT = r38_AMOUNT;
	}
	public String getR39_NAME() {
		return R39_NAME;
	}
	public void setR39_NAME(String r39_NAME) {
		R39_NAME = r39_NAME;
	}
	public String getR39_DESIGNATION_OR_POSITION() {
		return R39_DESIGNATION_OR_POSITION;
	}
	public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
		R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
	}
	public Double getR39_NUMBER_OF_ACCOUNTS() {
		return R39_NUMBER_OF_ACCOUNTS;
	}
	public void setR39_NUMBER_OF_ACCOUNTS(Double r39_NUMBER_OF_ACCOUNTS) {
		R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
	}
	public Double getR39_AMOUNT() {
		return R39_AMOUNT;
	}
	public void setR39_AMOUNT(Double r39_AMOUNT) {
		R39_AMOUNT = r39_AMOUNT;
	}
	public String getR40_NAME() {
		return R40_NAME;
	}
	public void setR40_NAME(String r40_NAME) {
		R40_NAME = r40_NAME;
	}
	public String getR40_DESIGNATION_OR_POSITION() {
		return R40_DESIGNATION_OR_POSITION;
	}
	public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
		R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
	}
	public Double getR40_NUMBER_OF_ACCOUNTS() {
		return R40_NUMBER_OF_ACCOUNTS;
	}
	public void setR40_NUMBER_OF_ACCOUNTS(Double r40_NUMBER_OF_ACCOUNTS) {
		R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
	}
	public Double getR40_AMOUNT() {
		return R40_AMOUNT;
	}
	public void setR40_AMOUNT(Double r40_AMOUNT) {
		R40_AMOUNT = r40_AMOUNT;
	}
	public String getR41_NAME() {
		return R41_NAME;
	}
	public void setR41_NAME(String r41_NAME) {
		R41_NAME = r41_NAME;
	}
	public String getR41_DESIGNATION_OR_POSITION() {
		return R41_DESIGNATION_OR_POSITION;
	}
	public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
		R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
	}
	public Double getR41_NUMBER_OF_ACCOUNTS() {
		return R41_NUMBER_OF_ACCOUNTS;
	}
	public void setR41_NUMBER_OF_ACCOUNTS(Double r41_NUMBER_OF_ACCOUNTS) {
		R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
	}
	public Double getR41_AMOUNT() {
		return R41_AMOUNT;
	}
	public void setR41_AMOUNT(Double r41_AMOUNT) {
		R41_AMOUNT = r41_AMOUNT;
	}
	public String getR42_NAME() {
		return R42_NAME;
	}
	public void setR42_NAME(String r42_NAME) {
		R42_NAME = r42_NAME;
	}
	public String getR42_DESIGNATION_OR_POSITION() {
		return R42_DESIGNATION_OR_POSITION;
	}
	public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
		R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
	}
	public Double getR42_NUMBER_OF_ACCOUNTS() {
		return R42_NUMBER_OF_ACCOUNTS;
	}
	public void setR42_NUMBER_OF_ACCOUNTS(Double r42_NUMBER_OF_ACCOUNTS) {
		R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
	}
	public Double getR42_AMOUNT() {
		return R42_AMOUNT;
	}
	public void setR42_AMOUNT(Double r42_AMOUNT) {
		R42_AMOUNT = r42_AMOUNT;
	}
	public String getR43_NAME() {
		return R43_NAME;
	}
	public void setR43_NAME(String r43_NAME) {
		R43_NAME = r43_NAME;
	}
	public String getR43_DESIGNATION_OR_POSITION() {
		return R43_DESIGNATION_OR_POSITION;
	}
	public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
		R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
	}
	public Double getR43_NUMBER_OF_ACCOUNTS() {
		return R43_NUMBER_OF_ACCOUNTS;
	}
	public void setR43_NUMBER_OF_ACCOUNTS(Double r43_NUMBER_OF_ACCOUNTS) {
		R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
	}
	public Double getR43_AMOUNT() {
		return R43_AMOUNT;
	}
	public void setR43_AMOUNT(Double r43_AMOUNT) {
		R43_AMOUNT = r43_AMOUNT;
	}
	public String getR44_NAME() {
		return R44_NAME;
	}
	public void setR44_NAME(String r44_NAME) {
		R44_NAME = r44_NAME;
	}
	public String getR44_DESIGNATION_OR_POSITION() {
		return R44_DESIGNATION_OR_POSITION;
	}
	public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
		R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
	}
	public Double getR44_NUMBER_OF_ACCOUNTS() {
		return R44_NUMBER_OF_ACCOUNTS;
	}
	public void setR44_NUMBER_OF_ACCOUNTS(Double r44_NUMBER_OF_ACCOUNTS) {
		R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
	}
	public Double getR44_AMOUNT() {
		return R44_AMOUNT;
	}
	public void setR44_AMOUNT(Double r44_AMOUNT) {
		R44_AMOUNT = r44_AMOUNT;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportVersion() {
		return reportVersion;
	}
	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}
	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}
	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}
	public String getREPORT_CODE() {
		return REPORT_CODE;
	}
	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}
	public String getREPORT_DESC() {
		return REPORT_DESC;
	}
	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}
	public String getDELETE_FLG() {
		return DELETE_FLG;
	}
	public void setDELETE_FLG(String dELETE_FLG) {
		DELETE_FLG = dELETE_FLG;
	}
	public MDISB5_Summary_Entity3() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
	

}
