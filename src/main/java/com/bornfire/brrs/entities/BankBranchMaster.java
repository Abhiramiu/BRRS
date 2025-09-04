package com.bornfire.brrs.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BANK_BRANCH_MASTER_TB")
public class BankBranchMaster {

    @Id
    @Column(name = "SOL_ID", length = 8, nullable = false)
    private String solId;

    @Column(name = "SOL_DESC", length = 132)
    private String solDesc;

    @Column(name = "BANK_CODE", length = 6)
    private String bankCode;

    @Column(name = "ABBR_BANK_NAME", length = 3)
    private String abbrBankName;

    @Column(name = "BR_CODE", length = 6)
    private String brCode;

    @Column(name = "ABBR_BR_NAME", length = 3)
    private String abbrBrName;

    @Column(name = "ADDR_1", length = 45)
    private String addr1;

    @Column(name = "ADDR_2", length = 45)
    private String addr2;

    @Column(name = "CITY_CODE", length = 5)
    private String cityCode;

    @Column(name = "STATE_CODE", length = 5)
    private String stateCode;

    @Column(name = "PIN_CODE", length = 10)
    private String pinCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BR_OPEN_DATE")
    private LocalDate brOpenDate;


    @Column(name = "HOME_CRNCY_CODE", length = 3)
    private String homeCrncyCode;

    @Column(name = "ENTITY_FLG", length = 1)
    private String entityFlg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENTRY_TIME")
    private Date entryTime;

    @Column(name = "ENTRY_USER", length = 20)
    private String entryUser;

    @Column(name = "MODIFY_FLG", length = 1)
    private String modifyFlg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @Column(name = "MODIFY_USER", length = 20)
    private String modifyUser;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "DEL_USER", length = 20)
    private String delUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEL_TIME")
    private Date delTime;

    // Getters and Setters
    public String getSolId() {
        return solId;
    }

    public void setSolId(String solId) {
        this.solId = solId;
    }

    public String getSolDesc() {
        return solDesc;
    }

    public void setSolDesc(String solDesc) {
        this.solDesc = solDesc;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAbbrBankName() {
        return abbrBankName;
    }

    public void setAbbrBankName(String abbrBankName) {
        this.abbrBankName = abbrBankName;
    }

    public String getBrCode() {
        return brCode;
    }

    public void setBrCode(String brCode) {
        this.brCode = brCode;
    }

    public String getAbbrBrName() {
        return abbrBrName;
    }

    public void setAbbrBrName(String abbrBrName) {
        this.abbrBrName = abbrBrName;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public LocalDate getBrOpenDate() {
        return brOpenDate;
    }

    public void setBrOpenDate(LocalDate brOpenDate) {
        this.brOpenDate = brOpenDate;
    }

    public String getHomeCrncyCode() {
        return homeCrncyCode;
    }

    public void setHomeCrncyCode(String homeCrncyCode) {
        this.homeCrncyCode = homeCrncyCode;
    }

    public String getEntityFlg() {
        return entityFlg;
    }

    public void setEntityFlg(String entityFlg) {
        this.entityFlg = entityFlg;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(String entryUser) {
        this.entryUser = entryUser;
    }

    public String getModifyFlg() {
        return modifyFlg;
    }

    public void setModifyFlg(String modifyFlg) {
        this.modifyFlg = modifyFlg;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }

    public String getDelUser() {
        return delUser;
    }

    public void setDelUser(String delUser) {
        this.delUser = delUser;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

	public BankBranchMaster(String solId, String solDesc, String bankCode, String abbrBankName, String brCode,
			String abbrBrName, String addr1, String addr2, String cityCode, String stateCode, String pinCode,
			LocalDate brOpenDate, String homeCrncyCode, String entityFlg, Date entryTime, String entryUser, String modifyFlg,
			Date modifyTime, String modifyUser, String delFlg, String delUser, Date delTime) {
		super();
		this.solId = solId;
		this.solDesc = solDesc;
		this.bankCode = bankCode;
		this.abbrBankName = abbrBankName;
		this.brCode = brCode;
		this.abbrBrName = abbrBrName;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.cityCode = cityCode;
		this.stateCode = stateCode;
		this.pinCode = pinCode;
		this.brOpenDate = brOpenDate;
		this.homeCrncyCode = homeCrncyCode;
		this.entityFlg = entityFlg;
		this.entryTime = entryTime;
		this.entryUser = entryUser;
		this.modifyFlg = modifyFlg;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
		this.delFlg = delFlg;
		this.delUser = delUser;
		this.delTime = delTime;
	}

	public BankBranchMaster() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
