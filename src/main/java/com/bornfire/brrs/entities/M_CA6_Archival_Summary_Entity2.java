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
@Table(name = "BRRS_M_CA6_ARCHIVALTABLE_SUMMARY2")

@IdClass(M_CA6_Archival_Summary2_PK.class)
public class M_CA6_Archival_Summary_Entity2 {
	

	private String R28_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R28_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R28_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R28_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R28_AMOUNT;

    private String R29_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R29_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R29_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R29_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R29_AMOUNT;

    private String R30_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R30_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R30_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R30_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R30_AMOUNT;

    private String R31_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R31_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R31_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R31_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R31_AMOUNT;

    private String R32_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R32_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R32_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R32_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R32_AMOUNT;

    private String R33_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R33_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R33_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R33_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R33_AMOUNT;

    private String R34_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R34_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R34_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R34_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R34_AMOUNT;

    private BigDecimal R35_AMOUNT;

    private String R40_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R40_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R40_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R40_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R40_AMOUNT;

    private String R41_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R41_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R41_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R41_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R41_AMOUNT;

    private String R42_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R42_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R42_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R42_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R42_AMOUNT;

    private String R43_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R43_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R43_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R43_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R43_AMOUNT;

    private String R44_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R44_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R44_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R44_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R44_AMOUNT;

    private String R45_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R45_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R45_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R45_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R45_AMOUNT;

    private String R46_PRODUCT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R46_ISSUANCE_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R46_CONTRACTUAL_MATURITY_DATE;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R46_EFFECTIVE_MATURITY_DATE;
    private BigDecimal R46_AMOUNT;

    private BigDecimal R47_AMOUNT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	  @Column(name = "REPORT_RESUBDATE")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date reportResubDate;	
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public Date getR28_ISSUANCE_DATE() {
		return R28_ISSUANCE_DATE;
	}
	public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
		R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
	}
	public Date getR28_CONTRACTUAL_MATURITY_DATE() {
		return R28_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
		R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR28_EFFECTIVE_MATURITY_DATE() {
		return R28_EFFECTIVE_MATURITY_DATE;
	}
	public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
		R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR28_AMOUNT() {
		return R28_AMOUNT;
	}
	public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
		R28_AMOUNT = r28_AMOUNT;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public Date getR29_ISSUANCE_DATE() {
		return R29_ISSUANCE_DATE;
	}
	public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
		R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
	}
	public Date getR29_CONTRACTUAL_MATURITY_DATE() {
		return R29_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
		R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR29_EFFECTIVE_MATURITY_DATE() {
		return R29_EFFECTIVE_MATURITY_DATE;
	}
	public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
		R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR29_AMOUNT() {
		return R29_AMOUNT;
	}
	public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
		R29_AMOUNT = r29_AMOUNT;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public Date getR30_ISSUANCE_DATE() {
		return R30_ISSUANCE_DATE;
	}
	public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
		R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
	}
	public Date getR30_CONTRACTUAL_MATURITY_DATE() {
		return R30_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
		R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR30_EFFECTIVE_MATURITY_DATE() {
		return R30_EFFECTIVE_MATURITY_DATE;
	}
	public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
		R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR30_AMOUNT() {
		return R30_AMOUNT;
	}
	public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
		R30_AMOUNT = r30_AMOUNT;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public Date getR31_ISSUANCE_DATE() {
		return R31_ISSUANCE_DATE;
	}
	public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
		R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
	}
	public Date getR31_CONTRACTUAL_MATURITY_DATE() {
		return R31_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
		R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR31_EFFECTIVE_MATURITY_DATE() {
		return R31_EFFECTIVE_MATURITY_DATE;
	}
	public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
		R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR31_AMOUNT() {
		return R31_AMOUNT;
	}
	public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
		R31_AMOUNT = r31_AMOUNT;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public Date getR32_ISSUANCE_DATE() {
		return R32_ISSUANCE_DATE;
	}
	public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
		R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
	}
	public Date getR32_CONTRACTUAL_MATURITY_DATE() {
		return R32_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
		R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR32_EFFECTIVE_MATURITY_DATE() {
		return R32_EFFECTIVE_MATURITY_DATE;
	}
	public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
		R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR32_AMOUNT() {
		return R32_AMOUNT;
	}
	public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
		R32_AMOUNT = r32_AMOUNT;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public Date getR33_ISSUANCE_DATE() {
		return R33_ISSUANCE_DATE;
	}
	public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
		R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
	}
	public Date getR33_CONTRACTUAL_MATURITY_DATE() {
		return R33_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
		R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR33_EFFECTIVE_MATURITY_DATE() {
		return R33_EFFECTIVE_MATURITY_DATE;
	}
	public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
		R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR33_AMOUNT() {
		return R33_AMOUNT;
	}
	public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
		R33_AMOUNT = r33_AMOUNT;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public Date getR34_ISSUANCE_DATE() {
		return R34_ISSUANCE_DATE;
	}
	public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
		R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
	}
	public Date getR34_CONTRACTUAL_MATURITY_DATE() {
		return R34_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
		R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR34_EFFECTIVE_MATURITY_DATE() {
		return R34_EFFECTIVE_MATURITY_DATE;
	}
	public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
		R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR34_AMOUNT() {
		return R34_AMOUNT;
	}
	public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
		R34_AMOUNT = r34_AMOUNT;
	}
	public BigDecimal getR35_AMOUNT() {
		return R35_AMOUNT;
	}
	public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
		R35_AMOUNT = r35_AMOUNT;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public Date getR40_ISSUANCE_DATE() {
		return R40_ISSUANCE_DATE;
	}
	public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
		R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
	}
	public Date getR40_CONTRACTUAL_MATURITY_DATE() {
		return R40_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
		R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR40_EFFECTIVE_MATURITY_DATE() {
		return R40_EFFECTIVE_MATURITY_DATE;
	}
	public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
		R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR40_AMOUNT() {
		return R40_AMOUNT;
	}
	public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
		R40_AMOUNT = r40_AMOUNT;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public Date getR41_ISSUANCE_DATE() {
		return R41_ISSUANCE_DATE;
	}
	public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
		R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
	}
	public Date getR41_CONTRACTUAL_MATURITY_DATE() {
		return R41_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
		R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR41_EFFECTIVE_MATURITY_DATE() {
		return R41_EFFECTIVE_MATURITY_DATE;
	}
	public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
		R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR41_AMOUNT() {
		return R41_AMOUNT;
	}
	public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
		R41_AMOUNT = r41_AMOUNT;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public Date getR42_ISSUANCE_DATE() {
		return R42_ISSUANCE_DATE;
	}
	public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
		R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
	}
	public Date getR42_CONTRACTUAL_MATURITY_DATE() {
		return R42_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
		R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR42_EFFECTIVE_MATURITY_DATE() {
		return R42_EFFECTIVE_MATURITY_DATE;
	}
	public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
		R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR42_AMOUNT() {
		return R42_AMOUNT;
	}
	public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
		R42_AMOUNT = r42_AMOUNT;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public Date getR43_ISSUANCE_DATE() {
		return R43_ISSUANCE_DATE;
	}
	public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
		R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
	}
	public Date getR43_CONTRACTUAL_MATURITY_DATE() {
		return R43_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
		R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR43_EFFECTIVE_MATURITY_DATE() {
		return R43_EFFECTIVE_MATURITY_DATE;
	}
	public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
		R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR43_AMOUNT() {
		return R43_AMOUNT;
	}
	public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
		R43_AMOUNT = r43_AMOUNT;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public Date getR44_ISSUANCE_DATE() {
		return R44_ISSUANCE_DATE;
	}
	public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
		R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
	}
	public Date getR44_CONTRACTUAL_MATURITY_DATE() {
		return R44_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
		R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR44_EFFECTIVE_MATURITY_DATE() {
		return R44_EFFECTIVE_MATURITY_DATE;
	}
	public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
		R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR44_AMOUNT() {
		return R44_AMOUNT;
	}
	public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
		R44_AMOUNT = r44_AMOUNT;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public Date getR45_ISSUANCE_DATE() {
		return R45_ISSUANCE_DATE;
	}
	public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
		R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
	}
	public Date getR45_CONTRACTUAL_MATURITY_DATE() {
		return R45_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
		R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR45_EFFECTIVE_MATURITY_DATE() {
		return R45_EFFECTIVE_MATURITY_DATE;
	}
	public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
		R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR45_AMOUNT() {
		return R45_AMOUNT;
	}
	public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
		R45_AMOUNT = r45_AMOUNT;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public Date getR46_ISSUANCE_DATE() {
		return R46_ISSUANCE_DATE;
	}
	public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
		R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
	}
	public Date getR46_CONTRACTUAL_MATURITY_DATE() {
		return R46_CONTRACTUAL_MATURITY_DATE;
	}
	public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
		R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
	}
	public Date getR46_EFFECTIVE_MATURITY_DATE() {
		return R46_EFFECTIVE_MATURITY_DATE;
	}
	public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
		R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
	}
	public BigDecimal getR46_AMOUNT() {
		return R46_AMOUNT;
	}
	public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
		R46_AMOUNT = r46_AMOUNT;
	}
	public BigDecimal getR47_AMOUNT() {
		return R47_AMOUNT;
	}
	public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
		R47_AMOUNT = r47_AMOUNT;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public BigDecimal getReportVersion() {
		return reportVersion;
	}
	public void setReportVersion(BigDecimal reportVersion) {
		this.reportVersion = reportVersion;
	}
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}
	public M_CA6_Archival_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
}