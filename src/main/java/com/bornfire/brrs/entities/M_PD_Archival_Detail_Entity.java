package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_PD_ARCHIVAL_TABLE_DETAIL")
public class M_PD_Archival_Detail_Entity {
	
	
	
    @Id
    @Column(name = "CUST_ID", length = 100)
    private String CUST_ID;

    @Column(name = "ACCT_NUMBER", length = 100)
    private String ACCT_NUMBER;

    @Column(name = "ACCT_NAME", length = 100)
    private String ACCT_NAME;

    @Column(name = "DATA_TYPE", length = 100)
    private String DATA_TYPE;

    @Column(name = "ROW_ID", length = 100)
    private String ROW_ID;

    @Column(name = "COLUMN_ID", length = 100)
    private String COLUMN_ID;

    @Column(name = "REPORT_REMARKS", length = 100)
    private String REPORT_REMARKS;

    @Column(name = "MODIFICATION_REMARKS", length = 100)
    private String MODIFICATION_REMARKS;

    @Column(name = "DATA_ENTRY_VERSION", length = 100)
    private String DATA_ENTRY_VERSION;

    @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
    private BigDecimal ACCT_BALANCE_IN_PULA;

    @Column(name = "REPORT_DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date REPORT_DATE;

    @Column(name = "REPORT_NAME", length = 100)
    private String REPORT_NAME;

    @Column(name = "CREATE_USER", length = 100)
    private String CREATE_USER;

    @Column(name = "CREATE_TIME")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date CREATE_TIME;

    @Column(name = "MODIFY_USER", length = 50)
    private String MODIFY_USER;

    @Column(name = "MODIFY_TIME")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date MODIFY_TIME;

    @Column(name = "VERIFY_USER", length = 50)
    private String VERIFY_USER;

    @Column(name = "VERIFY_TIME")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date VERIFY_TIME;

    @Column(name = "ENTITY_FLG", length = 1)
    private String ENTITY_FLG;

    @Column(name = "MODIFY_FLG", length = 1)
    private String MODIFY_FLG;

    @Column(name = "DEL_FLG", length = 1)
    private String DEL_FLG;
    
    
    
    
        public String getCUST_ID() {
		return CUST_ID;
	}



	public void setCUST_ID(String cUST_ID) {
		CUST_ID = cUST_ID;
	}



	public String getACCT_NUMBER() {
		return ACCT_NUMBER;
	}



	public void setACCT_NUMBER(String aCCT_NUMBER) {
		ACCT_NUMBER = aCCT_NUMBER;
	}



	public String getACCT_NAME() {
		return ACCT_NAME;
	}



	public void setACCT_NAME(String aCCT_NAME) {
		ACCT_NAME = aCCT_NAME;
	}



	public String getDATA_TYPE() {
		return DATA_TYPE;
	}



	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}



	public String getROW_ID() {
		return ROW_ID;
	}



	public void setROW_ID(String rOW_ID) {
		ROW_ID = rOW_ID;
	}



	public String getCOLUMN_ID() {
		return COLUMN_ID;
	}



	public void setCOLUMN_ID(String cOLUMN_ID) {
		COLUMN_ID = cOLUMN_ID;
	}



	public String getREPORT_REMARKS() {
		return REPORT_REMARKS;
	}



	public void setREPORT_REMARKS(String rEPORT_REMARKS) {
		REPORT_REMARKS = rEPORT_REMARKS;
	}



	public String getMODIFICATION_REMARKS() {
		return MODIFICATION_REMARKS;
	}



	public void setMODIFICATION_REMARKS(String mODIFICATION_REMARKS) {
		MODIFICATION_REMARKS = mODIFICATION_REMARKS;
	}



	public String getDATA_ENTRY_VERSION() {
		return DATA_ENTRY_VERSION;
	}



	public void setDATA_ENTRY_VERSION(String dATA_ENTRY_VERSION) {
		DATA_ENTRY_VERSION = dATA_ENTRY_VERSION;
	}



	public BigDecimal getACCT_BALANCE_IN_PULA() {
		return ACCT_BALANCE_IN_PULA;
	}



	public void setACCT_BALANCE_IN_PULA(BigDecimal aCCT_BALANCE_IN_PULA) {
		ACCT_BALANCE_IN_PULA = aCCT_BALANCE_IN_PULA;
	}



	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}



	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}



	public String getREPORT_NAME() {
		return REPORT_NAME;
	}



	public void setREPORT_NAME(String rEPORT_NAME) {
		REPORT_NAME = rEPORT_NAME;
	}



	public String getCREATE_USER() {
		return CREATE_USER;
	}



	public void setCREATE_USER(String cREATE_USER) {
		CREATE_USER = cREATE_USER;
	}



	public Date getCREATE_TIME() {
		return CREATE_TIME;
	}



	public void setCREATE_TIME(Date cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}



	public String getMODIFY_USER() {
		return MODIFY_USER;
	}



	public void setMODIFY_USER(String mODIFY_USER) {
		MODIFY_USER = mODIFY_USER;
	}



	public Date getMODIFY_TIME() {
		return MODIFY_TIME;
	}



	public void setMODIFY_TIME(Date mODIFY_TIME) {
		MODIFY_TIME = mODIFY_TIME;
	}



	public String getVERIFY_USER() {
		return VERIFY_USER;
	}



	public void setVERIFY_USER(String vERIFY_USER) {
		VERIFY_USER = vERIFY_USER;
	}



	public Date getVERIFY_TIME() {
		return VERIFY_TIME;
	}



	public void setVERIFY_TIME(Date vERIFY_TIME) {
		VERIFY_TIME = vERIFY_TIME;
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



	public String getDEL_FLG() {
		return DEL_FLG;
	}



	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}



    public M_PD_Archival_Detail_Entity() {
        super();
    }

}



