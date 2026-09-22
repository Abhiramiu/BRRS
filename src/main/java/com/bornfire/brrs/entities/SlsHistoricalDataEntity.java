package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "BRRS_SLS_HISTORICAL_DATA")
public class SlsHistoricalDataEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sls_hist_seq")
	@SequenceGenerator(name = "sls_hist_seq", sequenceName = "BRRS_SLS_HIST_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "MONTH_NO")
	private Integer monthNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH_YEAR")
	private Date monthYear;

	@Column(name = "CA")
	private Long ca;

	@Column(name = "CALL")
	private Long call;

	@Column(name = "SB")
	private Long sb;

	@Column(name = "FDR")
	private Long fdr;

	@Column(name = "TOTAL_DEP")
	private Long totalDep;

	@Column(name = "BC")
	private Long bc;

	@Column(name = "OTHER_LIABILITY")
	private Long otherLiability;

	@Column(name = "OTHER_ASSET")
	private Long otherAsset;

	@Column(name = "OD")
	private Long od;

	@Column(name = "LOAN")
	private Long loan;

	@Column(name = "TOTAL_LOAN")
	private Long totalLoan;

	@Column(name = "CD_RATIO", precision = 5, scale = 2)
	private BigDecimal cdRatio;

	@Column(name = "DEL_FLG", length = 1)
	private String delFlg;

	public SlsHistoricalDataEntity() {
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Integer getMonthNo() { return monthNo; }
	public void setMonthNo(Integer monthNo) { this.monthNo = monthNo; }

	public Date getMonthYear() { return monthYear; }
	public void setMonthYear(Date monthYear) { this.monthYear = monthYear; }

	public Long getCa() { return ca; }
	public void setCa(Long ca) { this.ca = ca; }

	public Long getCall() { return call; }
	public void setCall(Long call) { this.call = call; }

	public Long getSb() { return sb; }
	public void setSb(Long sb) { this.sb = sb; }

	public Long getFdr() { return fdr; }
	public void setFdr(Long fdr) { this.fdr = fdr; }

	public Long getTotalDep() { return totalDep; }
	public void setTotalDep(Long totalDep) { this.totalDep = totalDep; }

	public Long getBc() { return bc; }
	public void setBc(Long bc) { this.bc = bc; }

	public Long getOtherLiability() { return otherLiability; }
	public void setOtherLiability(Long otherLiability) { this.otherLiability = otherLiability; }

	public Long getOtherAsset() { return otherAsset; }
	public void setOtherAsset(Long otherAsset) { this.otherAsset = otherAsset; }

	public Long getOd() { return od; }
	public void setOd(Long od) { this.od = od; }

	public Long getLoan() { return loan; }
	public void setLoan(Long loan) { this.loan = loan; }

	public Long getTotalLoan() { return totalLoan; }
	public void setTotalLoan(Long totalLoan) { this.totalLoan = totalLoan; }

	public BigDecimal getCdRatio() { return cdRatio; }
	public void setCdRatio(BigDecimal cdRatio) { this.cdRatio = cdRatio; }

	public String getDelFlg() { return delFlg; }
	public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
}