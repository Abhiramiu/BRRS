package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_MDISB5_ARCHIVALTABLE_SUMMARY2")
@IdClass(MDISB5_Archival_Summary2_PK.class)

public class MDISB5_Archival_Summary_Entity2 {
	
	private String R20_NAME_OF_BOARD_MEMBERS;
    private String R20_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R20_NUMBER_OF_ACCOUNTS;
    private BigDecimal R20_AMOUNT;

    private String R21_NAME_OF_BOARD_MEMBERS;
    private String R21_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R21_NUMBER_OF_ACCOUNTS;
    private BigDecimal R21_AMOUNT;

    private String R22_NAME_OF_BOARD_MEMBERS;
    private String R22_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R22_NUMBER_OF_ACCOUNTS;
    private BigDecimal R22_AMOUNT;

    private String R23_NAME_OF_BOARD_MEMBERS;
    private String R23_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R23_NUMBER_OF_ACCOUNTS;
    private BigDecimal R23_AMOUNT;

    private String R24_NAME_OF_BOARD_MEMBERS;
    private String R24_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R24_NUMBER_OF_ACCOUNTS;
    private BigDecimal R24_AMOUNT;

    private String R25_NAME_OF_BOARD_MEMBERS;
    private String R25_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R25_NUMBER_OF_ACCOUNTS;
    private BigDecimal R25_AMOUNT;

    private String R26_NAME_OF_BOARD_MEMBERS;
    private String R26_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R26_NUMBER_OF_ACCOUNTS;
    private BigDecimal R26_AMOUNT;

    private String R27_NAME_OF_BOARD_MEMBERS;
    private String R27_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R27_NUMBER_OF_ACCOUNTS;
    private BigDecimal R27_AMOUNT;

    private String R28_NAME_OF_BOARD_MEMBERS;
    private String R28_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R28_NUMBER_OF_ACCOUNTS;
    private BigDecimal R28_AMOUNT;

    private String R29_NAME_OF_BOARD_MEMBERS;
    private String R29_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R29_NUMBER_OF_ACCOUNTS;
    private BigDecimal R29_AMOUNT;

    private String R30_NAME_OF_BOARD_MEMBERS;
    private String R30_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R30_NUMBER_OF_ACCOUNTS;
    private BigDecimal R30_AMOUNT;

    private String R31_NAME_OF_BOARD_MEMBERS;
    private String R31_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R31_NUMBER_OF_ACCOUNTS;
    private BigDecimal R31_AMOUNT;

    private String R32_NAME_OF_BOARD_MEMBERS;
    private String R32_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R32_NUMBER_OF_ACCOUNTS;
    private BigDecimal R32_AMOUNT;

    private String R33_NAME_OF_BOARD_MEMBERS;
    private String R33_EXECUTIVE_OR_NONEXECUTIVE;
    private BigDecimal R33_NUMBER_OF_ACCOUNTS;
    private BigDecimal R33_AMOUNT;
    
    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Id
    @Column(name = "REPORT_DATE")
	private Date reportDate;
    
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
   
    @Temporal(TemporalType.TIMESTAMP)
    private Date REPORT_RESUBDATE;
   
    private String REPORT_FREQUENCY;
    private String REPORT_CODE;
    private String REPORT_DESC;
    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DELETE_FLG;
	
    
    
	public String getR20_NAME_OF_BOARD_MEMBERS() {
		return R20_NAME_OF_BOARD_MEMBERS;
	}

	public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
		R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
	}

	public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
		return R20_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
		R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
		return R20_NUMBER_OF_ACCOUNTS;
	}

	public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
		R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR20_AMOUNT() {
		return R20_AMOUNT;
	}

	public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
		R20_AMOUNT = r20_AMOUNT;
	}

	public String getR21_NAME_OF_BOARD_MEMBERS() {
		return R21_NAME_OF_BOARD_MEMBERS;
	}

	public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
		R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
	}

	public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
		return R21_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
		R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
		return R21_NUMBER_OF_ACCOUNTS;
	}

	public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
		R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR21_AMOUNT() {
		return R21_AMOUNT;
	}

	public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
		R21_AMOUNT = r21_AMOUNT;
	}

	public String getR22_NAME_OF_BOARD_MEMBERS() {
		return R22_NAME_OF_BOARD_MEMBERS;
	}

	public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
		R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
	}

	public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
		return R22_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
		R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
		return R22_NUMBER_OF_ACCOUNTS;
	}

	public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
		R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR22_AMOUNT() {
		return R22_AMOUNT;
	}

	public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
		R22_AMOUNT = r22_AMOUNT;
	}

	public String getR23_NAME_OF_BOARD_MEMBERS() {
		return R23_NAME_OF_BOARD_MEMBERS;
	}

	public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
		R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
	}

	public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
		return R23_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
		R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
		return R23_NUMBER_OF_ACCOUNTS;
	}

	public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
		R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR23_AMOUNT() {
		return R23_AMOUNT;
	}

	public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
		R23_AMOUNT = r23_AMOUNT;
	}

	public String getR24_NAME_OF_BOARD_MEMBERS() {
		return R24_NAME_OF_BOARD_MEMBERS;
	}

	public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
		R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
	}

	public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
		return R24_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
		R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
		return R24_NUMBER_OF_ACCOUNTS;
	}

	public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
		R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR24_AMOUNT() {
		return R24_AMOUNT;
	}

	public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
		R24_AMOUNT = r24_AMOUNT;
	}

	public String getR25_NAME_OF_BOARD_MEMBERS() {
		return R25_NAME_OF_BOARD_MEMBERS;
	}

	public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
		R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
	}

	public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
		return R25_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
		R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
		return R25_NUMBER_OF_ACCOUNTS;
	}

	public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
		R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR25_AMOUNT() {
		return R25_AMOUNT;
	}

	public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
		R25_AMOUNT = r25_AMOUNT;
	}

	public String getR26_NAME_OF_BOARD_MEMBERS() {
		return R26_NAME_OF_BOARD_MEMBERS;
	}

	public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
		R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
	}

	public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
		return R26_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
		R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
		return R26_NUMBER_OF_ACCOUNTS;
	}

	public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
		R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR26_AMOUNT() {
		return R26_AMOUNT;
	}

	public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
		R26_AMOUNT = r26_AMOUNT;
	}

	public String getR27_NAME_OF_BOARD_MEMBERS() {
		return R27_NAME_OF_BOARD_MEMBERS;
	}

	public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
		R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
	}

	public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
		return R27_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
		R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
		return R27_NUMBER_OF_ACCOUNTS;
	}

	public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
		R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR27_AMOUNT() {
		return R27_AMOUNT;
	}

	public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
		R27_AMOUNT = r27_AMOUNT;
	}

	public String getR28_NAME_OF_BOARD_MEMBERS() {
		return R28_NAME_OF_BOARD_MEMBERS;
	}

	public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
		R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
	}

	public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
		return R28_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
		R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
		return R28_NUMBER_OF_ACCOUNTS;
	}

	public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
		R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR28_AMOUNT() {
		return R28_AMOUNT;
	}

	public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
		R28_AMOUNT = r28_AMOUNT;
	}

	public String getR29_NAME_OF_BOARD_MEMBERS() {
		return R29_NAME_OF_BOARD_MEMBERS;
	}

	public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
		R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
	}

	public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
		return R29_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
		R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
		return R29_NUMBER_OF_ACCOUNTS;
	}

	public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
		R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR29_AMOUNT() {
		return R29_AMOUNT;
	}

	public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
		R29_AMOUNT = r29_AMOUNT;
	}

	public String getR30_NAME_OF_BOARD_MEMBERS() {
		return R30_NAME_OF_BOARD_MEMBERS;
	}

	public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
		R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
	}

	public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
		return R30_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
		R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
		return R30_NUMBER_OF_ACCOUNTS;
	}

	public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
		R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR30_AMOUNT() {
		return R30_AMOUNT;
	}

	public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
		R30_AMOUNT = r30_AMOUNT;
	}

	public String getR31_NAME_OF_BOARD_MEMBERS() {
		return R31_NAME_OF_BOARD_MEMBERS;
	}

	public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
		R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
	}

	public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
		return R31_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
		R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
		return R31_NUMBER_OF_ACCOUNTS;
	}

	public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
		R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR31_AMOUNT() {
		return R31_AMOUNT;
	}

	public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
		R31_AMOUNT = r31_AMOUNT;
	}

	public String getR32_NAME_OF_BOARD_MEMBERS() {
		return R32_NAME_OF_BOARD_MEMBERS;
	}

	public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
		R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
	}

	public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
		return R32_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
		R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
		return R32_NUMBER_OF_ACCOUNTS;
	}

	public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
		R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR32_AMOUNT() {
		return R32_AMOUNT;
	}

	public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
		R32_AMOUNT = r32_AMOUNT;
	}

	public String getR33_NAME_OF_BOARD_MEMBERS() {
		return R33_NAME_OF_BOARD_MEMBERS;
	}

	public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
		R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
	}

	public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
		return R33_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
		R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
	}

	public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
		return R33_NUMBER_OF_ACCOUNTS;
	}

	public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
		R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR33_AMOUNT() {
		return R33_AMOUNT;
	}

	public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
		R33_AMOUNT = r33_AMOUNT;
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

	public Date getREPORT_RESUBDATE() {
		return REPORT_RESUBDATE;
	}

	public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
		REPORT_RESUBDATE = rEPORT_RESUBDATE;
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

	
	
	
	public MDISB5_Archival_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	

}
