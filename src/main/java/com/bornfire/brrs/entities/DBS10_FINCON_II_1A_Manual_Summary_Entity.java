package com.bornfire.brrs.entities;

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
@Table(name = "BRRS_DBS10_FINCON_II_1A_MANUAL_SUMMARYTABLE")

public class DBS10_FINCON_II_1A_Manual_Summary_Entity {

	private BigDecimal R8_AMOUNT_X010;
	private BigDecimal R12_AMOUNT_X010;
	private BigDecimal R14_AMOUNT_X010;
	private BigDecimal R22_AMOUNT_X010;
	private BigDecimal R23_AMOUNT_X010;
	private BigDecimal R25_AMOUNT_X010;
	private BigDecimal R26_AMOUNT_X010;
	private BigDecimal R27_AMOUNT_X010;
	private BigDecimal R28_AMOUNT_X010;
	private BigDecimal R31_AMOUNT_X010;
	private BigDecimal R32_AMOUNT_X010;
	private BigDecimal R35_AMOUNT_X010;
	private BigDecimal R36_AMOUNT_X010;
	private BigDecimal R37_AMOUNT_X010;
	private BigDecimal R41_AMOUNT_X010;
	private BigDecimal R42_AMOUNT_X010;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DELETE_FLG;

	public BigDecimal getR8_AMOUNT_X010() {
		return R8_AMOUNT_X010;
	}

	public void setR8_AMOUNT_X010(BigDecimal r8_AMOUNT_X010) {
		R8_AMOUNT_X010 = r8_AMOUNT_X010;
	}

	public BigDecimal getR12_AMOUNT_X010() {
		return R12_AMOUNT_X010;
	}

	public void setR12_AMOUNT_X010(BigDecimal r12_AMOUNT_X010) {
		R12_AMOUNT_X010 = r12_AMOUNT_X010;
	}

	public BigDecimal getR14_AMOUNT_X010() {
		return R14_AMOUNT_X010;
	}

	public void setR14_AMOUNT_X010(BigDecimal r14_AMOUNT_X010) {
		R14_AMOUNT_X010 = r14_AMOUNT_X010;
	}

	public BigDecimal getR22_AMOUNT_X010() {
		return R22_AMOUNT_X010;
	}

	public void setR22_AMOUNT_X010(BigDecimal r22_AMOUNT_X010) {
		R22_AMOUNT_X010 = r22_AMOUNT_X010;
	}

	public BigDecimal getR23_AMOUNT_X010() {
		return R23_AMOUNT_X010;
	}

	public void setR23_AMOUNT_X010(BigDecimal r23_AMOUNT_X010) {
		R23_AMOUNT_X010 = r23_AMOUNT_X010;
	}

	public BigDecimal getR25_AMOUNT_X010() {
		return R25_AMOUNT_X010;
	}

	public void setR25_AMOUNT_X010(BigDecimal r25_AMOUNT_X010) {
		R25_AMOUNT_X010 = r25_AMOUNT_X010;
	}

	public BigDecimal getR26_AMOUNT_X010() {
		return R26_AMOUNT_X010;
	}

	public void setR26_AMOUNT_X010(BigDecimal r26_AMOUNT_X010) {
		R26_AMOUNT_X010 = r26_AMOUNT_X010;
	}

	public BigDecimal getR27_AMOUNT_X010() {
		return R27_AMOUNT_X010;
	}

	public void setR27_AMOUNT_X010(BigDecimal r27_AMOUNT_X010) {
		R27_AMOUNT_X010 = r27_AMOUNT_X010;
	}

	public BigDecimal getR28_AMOUNT_X010() {
		return R28_AMOUNT_X010;
	}

	public void setR28_AMOUNT_X010(BigDecimal r28_AMOUNT_X010) {
		R28_AMOUNT_X010 = r28_AMOUNT_X010;
	}

	public BigDecimal getR31_AMOUNT_X010() {
		return R31_AMOUNT_X010;
	}

	public void setR31_AMOUNT_X010(BigDecimal r31_AMOUNT_X010) {
		R31_AMOUNT_X010 = r31_AMOUNT_X010;
	}

	public BigDecimal getR32_AMOUNT_X010() {
		return R32_AMOUNT_X010;
	}

	public void setR32_AMOUNT_X010(BigDecimal r32_AMOUNT_X010) {
		R32_AMOUNT_X010 = r32_AMOUNT_X010;
	}

	public BigDecimal getR35_AMOUNT_X010() {
		return R35_AMOUNT_X010;
	}

	public void setR35_AMOUNT_X010(BigDecimal r35_AMOUNT_X010) {
		R35_AMOUNT_X010 = r35_AMOUNT_X010;
	}

	public BigDecimal getR36_AMOUNT_X010() {
		return R36_AMOUNT_X010;
	}

	public void setR36_AMOUNT_X010(BigDecimal r36_AMOUNT_X010) {
		R36_AMOUNT_X010 = r36_AMOUNT_X010;
	}

	public BigDecimal getR37_AMOUNT_X010() {
		return R37_AMOUNT_X010;
	}

	public void setR37_AMOUNT_X010(BigDecimal r37_AMOUNT_X010) {
		R37_AMOUNT_X010 = r37_AMOUNT_X010;
	}

	public BigDecimal getR41_AMOUNT_X010() {
		return R41_AMOUNT_X010;
	}

	public void setR41_AMOUNT_X010(BigDecimal r41_AMOUNT_X010) {
		R41_AMOUNT_X010 = r41_AMOUNT_X010;
	}

	public BigDecimal getR42_AMOUNT_X010() {
		return R42_AMOUNT_X010;
	}

	public void setR42_AMOUNT_X010(BigDecimal r42_AMOUNT_X010) {
		R42_AMOUNT_X010 = r42_AMOUNT_X010;
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

	public DBS10_FINCON_II_1A_Manual_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
