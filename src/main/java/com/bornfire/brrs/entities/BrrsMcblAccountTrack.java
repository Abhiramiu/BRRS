package com.bornfire.brrs.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_MCBL_ACCOUNT_TRACK")
public class BrrsMcblAccountTrack {

    @Id
    @Column(name = "ID", length = 50)
    private String id;

    @Column(name = "REPORT_DATE", nullable = false)
    private LocalDate reportDate;

    @Column(name = "ACCOUNT_NO", nullable = false, length = 50)
    private String accountNo;

    @Column(name = "CHANGE_TYPE", length = 10)
    private String changeType; // 'ADDED' or 'DELETED'

    @Column(name = "ENTRY_USER", length = 50)
    private String entryUser;

    @Column(name = "ENTRY_TIME")
    private LocalDateTime entryTime;

    @Column(name = "REMARKS", length = 200)
    private String remarks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getReportDate() {
		return reportDate;
	}

	public void setReportDate(LocalDate reportDate) {
		this.reportDate = reportDate;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	public LocalDateTime getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
    
}
