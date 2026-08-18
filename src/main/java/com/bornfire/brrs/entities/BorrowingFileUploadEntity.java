package com.bornfire.brrs.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BRRS_BORROWING_FILEUPLOAD")
public class BorrowingFileUploadEntity {

	@Id
	@Column(name = "DEAL_NO", nullable = false, length = 100)
	private String dealNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "DEAL_DATE")
	private Date dealDate;

	@Column(name = "BANK", length = 150)
	private String bank;

	@Column(name = "CURRENCY_PURCHASED", length = 50)
	private String currencyPurchased;

	@Column(name = "AMOUNT_PURCHASED", precision = 18, scale = 4)
	private BigDecimal amountPurchased;

	@Column(name = "AMOUNT_IN_BWP", precision = 18, scale = 4)
	private BigDecimal amountInBwp;

	@Column(name = "RATE", precision = 12, scale = 6)
	private BigDecimal rate;

	@Column(name = "DAYS")
	private Integer days;

	@Column(name = "CURRENCY_SOLD", length = 50)
	private String currencySold;

	@Column(name = "INT_AMT", precision = 18, scale = 4)
	private BigDecimal intAmt;

	@Column(name = "PAYABLE_AMT", precision = 18, scale = 4)
	private BigDecimal payableAmt;

	@Temporal(TemporalType.DATE)
	@Column(name = "MAT_DT")
	private Date matDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "REF_DATE")
	private Date refDate;

	@Column(name = "RESIDUAL_PERIOD")
	private Integer residualPeriod;

	@Column(name = "CATEGORY", length = 20)
	private String category;

	@Temporal(TemporalType.DATE)
	@Column(name = "AS_ON_DATE")
	private Date asOnDate;

	// --- Constructors ---
	public BorrowingFileUploadEntity() {
	}

	// --- Getters & Setters ---
	public String getDealNo() {
		return dealNo;
	}

	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}

	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCurrencyPurchased() {
		return currencyPurchased;
	}

	public void setCurrencyPurchased(String currencyPurchased) {
		this.currencyPurchased = currencyPurchased;
	}

	public BigDecimal getAmountPurchased() {
		return amountPurchased;
	}

	public void setAmountPurchased(BigDecimal amountPurchased) {
		this.amountPurchased = amountPurchased;
	}

	public BigDecimal getAmountInBwp() {
		return amountInBwp;
	}

	public void setAmountInBwp(BigDecimal amountInBwp) {
		this.amountInBwp = amountInBwp;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getCurrencySold() {
		return currencySold;
	}

	public void setCurrencySold(String currencySold) {
		this.currencySold = currencySold;
	}

	public BigDecimal getIntAmt() {
		return intAmt;
	}

	public void setIntAmt(BigDecimal intAmt) {
		this.intAmt = intAmt;
	}

	public BigDecimal getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(BigDecimal payableAmt) {
		this.payableAmt = payableAmt;
	}

	public Date getMatDt() {
		return matDt;
	}

	public void setMatDt(Date matDt) {
		this.matDt = matDt;
	}

	public Date getRefDate() {
		return refDate;
	}

	public void setRefDate(Date refDate) {
		this.refDate = refDate;
	}

	public Integer getResidualPeriod() {
		return residualPeriod;
	}

	public void setResidualPeriod(Integer residualPeriod) {
		this.residualPeriod = residualPeriod;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}
}