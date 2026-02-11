
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

@Entity
@Table(name = "BRRS_M_CR_RESUB_DETAILTABLE")
@IdClass(M_CR_PK.class)
public class M_CR_Resub_Detail_Entity {

	private String R10_PRODUCT;
	private BigDecimal R10_TOTAL_LONG_POS;
	private BigDecimal R10_TOTAL_SHORT_POS;
	private BigDecimal R10_GROSS_OPEN_POS;
	private BigDecimal R10_CHARGE_BASIS_RISK;
	private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R10_NET_OPEN_POS;
	private BigDecimal R10_CHARGE_DIR_RISK;
	private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
	private String R11_PRODUCT;
	private BigDecimal R11_TOTAL_LONG_POS;
	private BigDecimal R11_TOTAL_SHORT_POS;
	private BigDecimal R11_GROSS_OPEN_POS;
	private BigDecimal R11_CHARGE_BASIS_RISK;
	private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R11_NET_OPEN_POS;
	private BigDecimal R11_CHARGE_DIR_RISK;
	private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
	private String R12_PRODUCT;
	private BigDecimal R12_TOTAL_LONG_POS;
	private BigDecimal R12_TOTAL_SHORT_POS;
	private BigDecimal R12_GROSS_OPEN_POS;
	private BigDecimal R12_CHARGE_BASIS_RISK;
	private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R12_NET_OPEN_POS;
	private BigDecimal R12_CHARGE_DIR_RISK;
	private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
	private String R13_PRODUCT;
	private BigDecimal R13_TOTAL_LONG_POS;
	private BigDecimal R13_TOTAL_SHORT_POS;
	private BigDecimal R13_GROSS_OPEN_POS;
	private BigDecimal R13_CHARGE_BASIS_RISK;
	private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R13_NET_OPEN_POS;
	private BigDecimal R13_CHARGE_DIR_RISK;
	private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
	private String R14_PRODUCT;
	private BigDecimal R14_TOTAL_LONG_POS;
	private BigDecimal R14_TOTAL_SHORT_POS;
	private BigDecimal R14_GROSS_OPEN_POS;
	private BigDecimal R14_CHARGE_BASIS_RISK;
	private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R14_NET_OPEN_POS;
	private BigDecimal R14_CHARGE_DIR_RISK;
	private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
	private String R15_PRODUCT;
	private BigDecimal R15_TOTAL_LONG_POS;
	private BigDecimal R15_TOTAL_SHORT_POS;
	private BigDecimal R15_GROSS_OPEN_POS;
	private BigDecimal R15_CHARGE_BASIS_RISK;
	private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R15_NET_OPEN_POS;
	private BigDecimal R15_CHARGE_DIR_RISK;
	private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
	private String R16_PRODUCT;
	private BigDecimal R16_TOTAL_LONG_POS;
	private BigDecimal R16_TOTAL_SHORT_POS;
	private BigDecimal R16_GROSS_OPEN_POS;
	private BigDecimal R16_CHARGE_BASIS_RISK;
	private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R16_NET_OPEN_POS;
	private BigDecimal R16_CHARGE_DIR_RISK;
	private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
	private String R17_PRODUCT;
	private BigDecimal R17_TOTAL_LONG_POS;
	private BigDecimal R17_TOTAL_SHORT_POS;
	private BigDecimal R17_GROSS_OPEN_POS;
	private BigDecimal R17_CHARGE_BASIS_RISK;
	private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
	private BigDecimal R17_NET_OPEN_POS;
	private BigDecimal R17_CHARGE_DIR_RISK;
	private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
	private BigDecimal R17_TOTAL_CAPITAL_CHARGE;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	
	private Date reportResubDate;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}
	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}
	public BigDecimal getR10_TOTAL_LONG_POS() {
		return R10_TOTAL_LONG_POS;
	}
	public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
		R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
	}
	public BigDecimal getR10_TOTAL_SHORT_POS() {
		return R10_TOTAL_SHORT_POS;
	}
	public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
		R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
	}
	public BigDecimal getR10_GROSS_OPEN_POS() {
		return R10_GROSS_OPEN_POS;
	}
	public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
		R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
	}
	public BigDecimal getR10_CHARGE_BASIS_RISK() {
		return R10_CHARGE_BASIS_RISK;
	}
	public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
		R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
		return R10_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
		R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR10_NET_OPEN_POS() {
		return R10_NET_OPEN_POS;
	}
	public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
		R10_NET_OPEN_POS = r10_NET_OPEN_POS;
	}
	public BigDecimal getR10_CHARGE_DIR_RISK() {
		return R10_CHARGE_DIR_RISK;
	}
	public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
		R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
	}
	public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
		return R10_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
		R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
		return R10_TOTAL_CAPITAL_CHARGE;
	}
	public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
		R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
	}
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_TOTAL_LONG_POS() {
		return R11_TOTAL_LONG_POS;
	}
	public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
		R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
	}
	public BigDecimal getR11_TOTAL_SHORT_POS() {
		return R11_TOTAL_SHORT_POS;
	}
	public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
		R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
	}
	public BigDecimal getR11_GROSS_OPEN_POS() {
		return R11_GROSS_OPEN_POS;
	}
	public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
		R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
	}
	public BigDecimal getR11_CHARGE_BASIS_RISK() {
		return R11_CHARGE_BASIS_RISK;
	}
	public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
		R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
		return R11_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
		R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR11_NET_OPEN_POS() {
		return R11_NET_OPEN_POS;
	}
	public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
		R11_NET_OPEN_POS = r11_NET_OPEN_POS;
	}
	public BigDecimal getR11_CHARGE_DIR_RISK() {
		return R11_CHARGE_DIR_RISK;
	}
	public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
		R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
	}
	public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
		return R11_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
		R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
		return R11_TOTAL_CAPITAL_CHARGE;
	}
	public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
		R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_TOTAL_LONG_POS() {
		return R12_TOTAL_LONG_POS;
	}
	public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
		R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
	}
	public BigDecimal getR12_TOTAL_SHORT_POS() {
		return R12_TOTAL_SHORT_POS;
	}
	public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
		R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
	}
	public BigDecimal getR12_GROSS_OPEN_POS() {
		return R12_GROSS_OPEN_POS;
	}
	public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
		R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
	}
	public BigDecimal getR12_CHARGE_BASIS_RISK() {
		return R12_CHARGE_BASIS_RISK;
	}
	public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
		R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
		return R12_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
		R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR12_NET_OPEN_POS() {
		return R12_NET_OPEN_POS;
	}
	public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
		R12_NET_OPEN_POS = r12_NET_OPEN_POS;
	}
	public BigDecimal getR12_CHARGE_DIR_RISK() {
		return R12_CHARGE_DIR_RISK;
	}
	public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
		R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
	}
	public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
		return R12_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
		R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
		return R12_TOTAL_CAPITAL_CHARGE;
	}
	public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
		R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_TOTAL_LONG_POS() {
		return R13_TOTAL_LONG_POS;
	}
	public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
		R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
	}
	public BigDecimal getR13_TOTAL_SHORT_POS() {
		return R13_TOTAL_SHORT_POS;
	}
	public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
		R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
	}
	public BigDecimal getR13_GROSS_OPEN_POS() {
		return R13_GROSS_OPEN_POS;
	}
	public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
		R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
	}
	public BigDecimal getR13_CHARGE_BASIS_RISK() {
		return R13_CHARGE_BASIS_RISK;
	}
	public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
		R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
		return R13_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
		R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR13_NET_OPEN_POS() {
		return R13_NET_OPEN_POS;
	}
	public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
		R13_NET_OPEN_POS = r13_NET_OPEN_POS;
	}
	public BigDecimal getR13_CHARGE_DIR_RISK() {
		return R13_CHARGE_DIR_RISK;
	}
	public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
		R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
	}
	public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
		return R13_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
		R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
		return R13_TOTAL_CAPITAL_CHARGE;
	}
	public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
		R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_TOTAL_LONG_POS() {
		return R14_TOTAL_LONG_POS;
	}
	public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
		R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
	}
	public BigDecimal getR14_TOTAL_SHORT_POS() {
		return R14_TOTAL_SHORT_POS;
	}
	public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
		R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
	}
	public BigDecimal getR14_GROSS_OPEN_POS() {
		return R14_GROSS_OPEN_POS;
	}
	public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
		R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
	}
	public BigDecimal getR14_CHARGE_BASIS_RISK() {
		return R14_CHARGE_BASIS_RISK;
	}
	public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
		R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
		return R14_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
		R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR14_NET_OPEN_POS() {
		return R14_NET_OPEN_POS;
	}
	public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
		R14_NET_OPEN_POS = r14_NET_OPEN_POS;
	}
	public BigDecimal getR14_CHARGE_DIR_RISK() {
		return R14_CHARGE_DIR_RISK;
	}
	public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
		R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
	}
	public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
		return R14_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
		R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
		return R14_TOTAL_CAPITAL_CHARGE;
	}
	public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
		R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_TOTAL_LONG_POS() {
		return R15_TOTAL_LONG_POS;
	}
	public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
		R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
	}
	public BigDecimal getR15_TOTAL_SHORT_POS() {
		return R15_TOTAL_SHORT_POS;
	}
	public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
		R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
	}
	public BigDecimal getR15_GROSS_OPEN_POS() {
		return R15_GROSS_OPEN_POS;
	}
	public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
		R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
	}
	public BigDecimal getR15_CHARGE_BASIS_RISK() {
		return R15_CHARGE_BASIS_RISK;
	}
	public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
		R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
		return R15_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
		R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR15_NET_OPEN_POS() {
		return R15_NET_OPEN_POS;
	}
	public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
		R15_NET_OPEN_POS = r15_NET_OPEN_POS;
	}
	public BigDecimal getR15_CHARGE_DIR_RISK() {
		return R15_CHARGE_DIR_RISK;
	}
	public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
		R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
	}
	public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
		return R15_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
		R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
		return R15_TOTAL_CAPITAL_CHARGE;
	}
	public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
		R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_TOTAL_LONG_POS() {
		return R16_TOTAL_LONG_POS;
	}
	public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
		R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
	}
	public BigDecimal getR16_TOTAL_SHORT_POS() {
		return R16_TOTAL_SHORT_POS;
	}
	public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
		R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
	}
	public BigDecimal getR16_GROSS_OPEN_POS() {
		return R16_GROSS_OPEN_POS;
	}
	public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
		R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
	}
	public BigDecimal getR16_CHARGE_BASIS_RISK() {
		return R16_CHARGE_BASIS_RISK;
	}
	public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
		R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
		return R16_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
		R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR16_NET_OPEN_POS() {
		return R16_NET_OPEN_POS;
	}
	public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
		R16_NET_OPEN_POS = r16_NET_OPEN_POS;
	}
	public BigDecimal getR16_CHARGE_DIR_RISK() {
		return R16_CHARGE_DIR_RISK;
	}
	public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
		R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
	}
	public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
		return R16_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
		R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
		return R16_TOTAL_CAPITAL_CHARGE;
	}
	public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
		R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_TOTAL_LONG_POS() {
		return R17_TOTAL_LONG_POS;
	}
	public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
		R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
	}
	public BigDecimal getR17_TOTAL_SHORT_POS() {
		return R17_TOTAL_SHORT_POS;
	}
	public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
		R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
	}
	public BigDecimal getR17_GROSS_OPEN_POS() {
		return R17_GROSS_OPEN_POS;
	}
	public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
		R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
	}
	public BigDecimal getR17_CHARGE_BASIS_RISK() {
		return R17_CHARGE_BASIS_RISK;
	}
	public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
		R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
		return R17_CAPITAL_CHARGE_BASIS_RISK;
	}
	public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
		R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
	}
	public BigDecimal getR17_NET_OPEN_POS() {
		return R17_NET_OPEN_POS;
	}
	public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
		R17_NET_OPEN_POS = r17_NET_OPEN_POS;
	}
	public BigDecimal getR17_CHARGE_DIR_RISK() {
		return R17_CHARGE_DIR_RISK;
	}
	public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
		R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
	}
	public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
		return R17_CAPITAL_CHARGE_DIR_RISK;
	}
	public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
		R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
	}
	public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
		return R17_TOTAL_CAPITAL_CHARGE;
	}
	public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
		R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
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
	public M_CR_Resub_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}