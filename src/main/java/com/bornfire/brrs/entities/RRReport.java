package com.bornfire.brrs.entities;

import javax.persistence.*;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_RR_RPT_MAST")
public class RRReport {

    @Id
    @Column(name = "SRL_NO")
    private int srlNo;

    @Column(name = "RPT_START_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rptStartDate;

    @Column(name = "RPT_CODE")
    private String rptCode;

    @Column(name = "RPT_DESCRIPTION")
    private String rptDescription;

    @Column(name = "RPT_TYPE")
    private String rptType;

    @Column(name = "DET_FLG")
    private String detFlg;

    @Column(name = "RPT_STATUS")
    private String rptStatus;

    @Column(name = "START_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "REMARKS_1")
    private String remarks1;

    @Column(name = "REMARKS_2")
    private String remarks2;

    @Column(name = "REMARKS_3")
    private String remarks3;

    @Column(name = "REMARKS_4")
    private String remarks4;

    @Column(name = "REMARKS_5")
    private String remarks5;

    @Column(name = "DEL_FLG")
    private String delFlg;

    @Column(name = "ENTITY_FLG")
    private String entityFlg;

    @Column(name = "MODIFY_FLG")
    private String modifyFlg;

    @Column(name = "ENTRY_USER")
    private String entryUser;

    @Column(name = "MODIFY_USER")
    private String modifyUser;

    @Column(name = "VERIFY_USER")
    private String verifyUser;

    @Column(name = "ENTRY_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date entryDate;

    @Column(name = "MODIFY_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyDate;

    @Column(name = "VERIFY_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date verifyDate;

    @Column(name = "TAXONOMY_VERSION")
    private String taxonomyVersion;

    @Column(name = "TAXONOMY_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date taxonomyDate;

    @Column(name = "DOMAIN")
    private String domain;

    @Column(name = "DOMAIN_ID")
    private String domainId;

	public int getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(int srlNo) {
		this.srlNo = srlNo;
	}

	public Date getRptStartDate() {
		return rptStartDate;
	}

	public void setRptStartDate(Date rptStartDate) {
		this.rptStartDate = rptStartDate;
	}

	public String getRptCode() {
		return rptCode;
	}

	public void setRptCode(String rptCode) {
		this.rptCode = rptCode;
	}

	public String getRptDescription() {
		return rptDescription;
	}

	public void setRptDescription(String rptDescription) {
		this.rptDescription = rptDescription;
	}

	public String getRptType() {
		return rptType;
	}

	public void setRptType(String rptType) {
		this.rptType = rptType;
	}

	public String getDetFlg() {
		return detFlg;
	}

	public void setDetFlg(String detFlg) {
		this.detFlg = detFlg;
	}

	public String getRptStatus() {
		return rptStatus;
	}

	public void setRptStatus(String rptStatus) {
		this.rptStatus = rptStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRemarks1() {
		return remarks1;
	}

	public void setRemarks1(String remarks1) {
		this.remarks1 = remarks1;
	}

	public String getRemarks2() {
		return remarks2;
	}

	public void setRemarks2(String remarks2) {
		this.remarks2 = remarks2;
	}

	public String getRemarks3() {
		return remarks3;
	}

	public void setRemarks3(String remarks3) {
		this.remarks3 = remarks3;
	}

	public String getRemarks4() {
		return remarks4;
	}

	public void setRemarks4(String remarks4) {
		this.remarks4 = remarks4;
	}

	public String getRemarks5() {
		return remarks5;
	}

	public void setRemarks5(String remarks5) {
		this.remarks5 = remarks5;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}

	public String getModifyFlg() {
		return modifyFlg;
	}

	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}

	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getTaxonomyVersion() {
		return taxonomyVersion;
	}

	public void setTaxonomyVersion(String taxonomyVersion) {
		this.taxonomyVersion = taxonomyVersion;
	}

	public Date getTaxonomyDate() {
		return taxonomyDate;
	}

	public void setTaxonomyDate(Date taxonomyDate) {
		this.taxonomyDate = taxonomyDate;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public RRReport(int srlNo, Date rptStartDate, String rptCode, String rptDescription, String rptType, String detFlg,
			String rptStatus, Date startDate, Date endDate, String remarks1, String remarks2, String remarks3,
			String remarks4, String remarks5, String delFlg, String entityFlg, String modifyFlg, String entryUser,
			String modifyUser, String verifyUser, Date entryDate, Date modifyDate, Date verifyDate,
			String taxonomyVersion, Date taxonomyDate, String domain, String domainId) {
		super();
		this.srlNo = srlNo;
		this.rptStartDate = rptStartDate;
		this.rptCode = rptCode;
		this.rptDescription = rptDescription;
		this.rptType = rptType;
		this.detFlg = detFlg;
		this.rptStatus = rptStatus;
		this.startDate = startDate;
		this.endDate = endDate;
		this.remarks1 = remarks1;
		this.remarks2 = remarks2;
		this.remarks3 = remarks3;
		this.remarks4 = remarks4;
		this.remarks5 = remarks5;
		this.delFlg = delFlg;
		this.entityFlg = entityFlg;
		this.modifyFlg = modifyFlg;
		this.entryUser = entryUser;
		this.modifyUser = modifyUser;
		this.verifyUser = verifyUser;
		this.entryDate = entryDate;
		this.modifyDate = modifyDate;
		this.verifyDate = verifyDate;
		this.taxonomyVersion = taxonomyVersion;
		this.taxonomyDate = taxonomyDate;
		this.domain = domain;
		this.domainId = domainId;
	}

	public RRReport() {
		super();
		// TODO Auto-generated constructor stub
	}

    
}
