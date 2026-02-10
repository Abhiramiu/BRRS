package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_M_AIDP_RESUBTABLE_DETAIL1")
@IdClass(M_AIDP_PK.class)
public class M_AIDP_Resub_Detail_Entity1 implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Date report_date;

	private BigDecimal report_version;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	private String R11_NAME_OF_BANK;
	private String R11_TYPE_OF_ACC;
	private String R11_PURPOSE;
	private String R11_CURRENCY;
	private String R11_BANK_RATE;
	private BigDecimal R11_AMT_LESS_184_DAYS;
	private BigDecimal R11_AMT_MORE_184_DAYS;
	private String R12_NAME_OF_BANK;
	private String R12_TYPE_OF_ACC;
	private String R12_PURPOSE;
	private String R12_CURRENCY;
	private String R12_BANK_RATE;
	private BigDecimal R12_AMT_LESS_184_DAYS;
	private BigDecimal R12_AMT_MORE_184_DAYS;
	private String R13_NAME_OF_BANK;
	private String R13_TYPE_OF_ACC;
	private String R13_PURPOSE;
	private String R13_CURRENCY;
	private String R13_BANK_RATE;
	private BigDecimal R13_AMT_LESS_184_DAYS;
	private BigDecimal R13_AMT_MORE_184_DAYS;
	private String R14_NAME_OF_BANK;
	private String R14_TYPE_OF_ACC;
	private String R14_PURPOSE;
	private String R14_CURRENCY;
	private String R14_BANK_RATE;
	private BigDecimal R14_AMT_LESS_184_DAYS;
	private BigDecimal R14_AMT_MORE_184_DAYS;
	private String R15_NAME_OF_BANK;
	private String R15_TYPE_OF_ACC;
	private String R15_PURPOSE;
	private String R15_CURRENCY;
	private String R15_BANK_RATE;
	private BigDecimal R15_AMT_LESS_184_DAYS;
	private BigDecimal R15_AMT_MORE_184_DAYS;
	private String R16_NAME_OF_BANK;
	private String R16_TYPE_OF_ACC;
	private String R16_PURPOSE;
	private String R16_CURRENCY;
	private String R16_BANK_RATE;
	private BigDecimal R16_AMT_LESS_184_DAYS;
	private BigDecimal R16_AMT_MORE_184_DAYS;
	private String R17_NAME_OF_BANK;
	private String R17_TYPE_OF_ACC;
	private String R17_PURPOSE;
	private String R17_CURRENCY;
	private String R17_BANK_RATE;
	private BigDecimal R17_AMT_LESS_184_DAYS;
	private BigDecimal R17_AMT_MORE_184_DAYS;
	private String R18_NAME_OF_BANK;
	private String R18_TYPE_OF_ACC;
	private String R18_PURPOSE;
	private String R18_CURRENCY;
	private String R18_BANK_RATE;
	private BigDecimal R18_AMT_LESS_184_DAYS;
	private BigDecimal R18_AMT_MORE_184_DAYS;
	private String R19_NAME_OF_BANK;
	private String R19_TYPE_OF_ACC;
	private String R19_PURPOSE;
	private String R19_CURRENCY;
	private String R19_BANK_RATE;
	private BigDecimal R19_AMT_LESS_184_DAYS;
	private BigDecimal R19_AMT_MORE_184_DAYS;
	private String R20_NAME_OF_BANK;
	private String R20_TYPE_OF_ACC;
	private String R20_PURPOSE;
	private String R20_CURRENCY;
	private String R20_BANK_RATE;
	private BigDecimal R20_AMT_LESS_184_DAYS;
	private BigDecimal R20_AMT_MORE_184_DAYS;
	private String R21_NAME_OF_BANK;
	private String R21_TYPE_OF_ACC;
	private String R21_PURPOSE;
	private String R21_CURRENCY;
	private String R21_BANK_RATE;
	private BigDecimal R21_AMT_LESS_184_DAYS;
	private BigDecimal R21_AMT_MORE_184_DAYS;
	private String R22_NAME_OF_BANK;
	private String R22_TYPE_OF_ACC;
	private String R22_PURPOSE;
	private String R22_CURRENCY;
	private String R22_BANK_RATE;
	private BigDecimal R22_AMT_LESS_184_DAYS;
	private BigDecimal R22_AMT_MORE_184_DAYS;
	private String R23_NAME_OF_BANK;
	private String R23_TYPE_OF_ACC;
	private String R23_PURPOSE;
	private String R23_CURRENCY;
	private String R23_BANK_RATE;
	private BigDecimal R23_AMT_LESS_184_DAYS;
	private BigDecimal R23_AMT_MORE_184_DAYS;
	private String R24_NAME_OF_BANK;
	private String R24_TYPE_OF_ACC;
	private String R24_PURPOSE;
	private String R24_CURRENCY;
	private String R24_BANK_RATE;
	private BigDecimal R24_AMT_LESS_184_DAYS;
	private BigDecimal R24_AMT_MORE_184_DAYS;
	private String R25_NAME_OF_BANK;
	private String R25_TYPE_OF_ACC;
	private String R25_PURPOSE;
	private String R25_CURRENCY;
	private String R25_BANK_RATE;
	private BigDecimal R25_AMT_LESS_184_DAYS;
	private BigDecimal R25_AMT_MORE_184_DAYS;
	private String R26_NAME_OF_BANK;
	private String R26_TYPE_OF_ACC;
	private String R26_PURPOSE;
	private String R26_CURRENCY;
	private String R26_BANK_RATE;
	private BigDecimal R26_AMT_LESS_184_DAYS;
	private BigDecimal R26_AMT_MORE_184_DAYS;
	private String R27_NAME_OF_BANK;
	private String R27_TYPE_OF_ACC;
	private String R27_PURPOSE;
	private String R27_CURRENCY;
	private String R27_BANK_RATE;
	private BigDecimal R27_AMT_LESS_184_DAYS;
	private BigDecimal R27_AMT_MORE_184_DAYS;
	private String R28_NAME_OF_BANK;
	private String R28_TYPE_OF_ACC;
	private String R28_PURPOSE;
	private String R28_CURRENCY;
	private String R28_BANK_RATE;
	private BigDecimal R28_AMT_LESS_184_DAYS;
	private BigDecimal R28_AMT_MORE_184_DAYS;
	private String R29_NAME_OF_BANK;
	private String R29_TYPE_OF_ACC;
	private String R29_PURPOSE;
	private String R29_CURRENCY;
	private String R29_BANK_RATE;
	private BigDecimal R29_AMT_LESS_184_DAYS;
	private BigDecimal R29_AMT_MORE_184_DAYS;
	private String R30_NAME_OF_BANK;
	private String R30_TYPE_OF_ACC;
	private String R30_PURPOSE;
	private String R30_CURRENCY;
	private String R30_BANK_RATE;
	private BigDecimal R30_AMT_LESS_184_DAYS;
	private BigDecimal R30_AMT_MORE_184_DAYS;
	private String R31_NAME_OF_BANK;
	private String R31_TYPE_OF_ACC;
	private String R31_PURPOSE;
	private String R31_CURRENCY;
	private String R31_BANK_RATE;
	private BigDecimal R31_AMT_LESS_184_DAYS;
	private BigDecimal R31_AMT_MORE_184_DAYS;
	private String R32_NAME_OF_BANK;
	private String R32_TYPE_OF_ACC;
	private String R32_PURPOSE;
	private String R32_CURRENCY;
	private String R32_BANK_RATE;
	private BigDecimal R32_AMT_LESS_184_DAYS;
	private BigDecimal R32_AMT_MORE_184_DAYS;
	private String R33_NAME_OF_BANK;
	private String R33_TYPE_OF_ACC;
	private String R33_PURPOSE;
	private String R33_CURRENCY;
	private String R33_BANK_RATE;
	private BigDecimal R33_AMT_LESS_184_DAYS;
	private BigDecimal R33_AMT_MORE_184_DAYS;
	private String R34_NAME_OF_BANK;
	private String R34_TYPE_OF_ACC;
	private String R34_PURPOSE;
	private String R34_CURRENCY;
	private String R34_BANK_RATE;
	private BigDecimal R34_AMT_LESS_184_DAYS;
	private BigDecimal R34_AMT_MORE_184_DAYS;
	private String R35_NAME_OF_BANK;
	private String R35_TYPE_OF_ACC;
	private String R35_PURPOSE;
	private String R35_CURRENCY;
	private String R35_BANK_RATE;
	private BigDecimal R35_AMT_LESS_184_DAYS;
	private BigDecimal R35_AMT_MORE_184_DAYS;
	private String R36_NAME_OF_BANK;
	private String R36_TYPE_OF_ACC;
	private String R36_PURPOSE;
	private String R36_CURRENCY;
	private String R36_BANK_RATE;
	private BigDecimal R36_AMT_LESS_184_DAYS;
	private BigDecimal R36_AMT_MORE_184_DAYS;
	private String R37_NAME_OF_BANK;
	private String R37_TYPE_OF_ACC;
	private String R37_PURPOSE;
	private String R37_CURRENCY;
	private String R37_BANK_RATE;
	private BigDecimal R37_AMT_LESS_184_DAYS;
	private BigDecimal R37_AMT_MORE_184_DAYS;
	private String R38_NAME_OF_BANK;
	private String R38_TYPE_OF_ACC;
	private String R38_PURPOSE;
	private String R38_CURRENCY;
	private String R38_BANK_RATE;
	private BigDecimal R38_AMT_LESS_184_DAYS;
	private BigDecimal R38_AMT_MORE_184_DAYS;
	private String R39_NAME_OF_BANK;
	private String R39_TYPE_OF_ACC;
	private String R39_PURPOSE;
	private String R39_CURRENCY;
	private String R39_BANK_RATE;
	private BigDecimal R39_AMT_LESS_184_DAYS;
	private BigDecimal R39_AMT_MORE_184_DAYS;
	private String R40_NAME_OF_BANK;
	private String R40_TYPE_OF_ACC;
	private String R40_PURPOSE;
	private String R40_CURRENCY;
	private String R40_BANK_RATE;
	private BigDecimal R40_AMT_LESS_184_DAYS;
	private BigDecimal R40_AMT_MORE_184_DAYS;
	private String R41_NAME_OF_BANK;
	private String R41_TYPE_OF_ACC;
	private String R41_PURPOSE;
	private String R41_CURRENCY;
	private String R41_BANK_RATE;
	private BigDecimal R41_AMT_LESS_184_DAYS;
	private BigDecimal R41_AMT_MORE_184_DAYS;
	private String R42_NAME_OF_BANK;
	private String R42_TYPE_OF_ACC;
	private String R42_PURPOSE;
	private String R42_CURRENCY;
	private String R42_BANK_RATE;
	private BigDecimal R42_AMT_LESS_184_DAYS;
	private BigDecimal R42_AMT_MORE_184_DAYS;
	private String R43_NAME_OF_BANK;
	private String R43_TYPE_OF_ACC;
	private String R43_PURPOSE;
	private String R43_CURRENCY;
	private String R43_BANK_RATE;
	private BigDecimal R43_AMT_LESS_184_DAYS;
	private BigDecimal R43_AMT_MORE_184_DAYS;
	private String R44_NAME_OF_BANK;
	private String R44_TYPE_OF_ACC;
	private String R44_PURPOSE;
	private String R44_CURRENCY;
	private String R44_BANK_RATE;
	private BigDecimal R44_AMT_LESS_184_DAYS;
	private BigDecimal R44_AMT_MORE_184_DAYS;
	private String R45_NAME_OF_BANK;
	private String R45_TYPE_OF_ACC;
	private String R45_PURPOSE;
	private String R45_CURRENCY;
	private String R45_BANK_RATE;
	private BigDecimal R45_AMT_LESS_184_DAYS;
	private BigDecimal R45_AMT_MORE_184_DAYS;
	private String R46_NAME_OF_BANK;
	private String R46_TYPE_OF_ACC;
	private String R46_PURPOSE;
	private String R46_CURRENCY;
	private String R46_BANK_RATE;
	private BigDecimal R46_AMT_LESS_184_DAYS;
	private BigDecimal R46_AMT_MORE_184_DAYS;
	private String R47_NAME_OF_BANK;
	private String R47_TYPE_OF_ACC;
	private String R47_PURPOSE;
	private String R47_CURRENCY;
	private String R47_BANK_RATE;
	private BigDecimal R47_AMT_LESS_184_DAYS;
	private BigDecimal R47_AMT_MORE_184_DAYS;
	private String R48_NAME_OF_BANK;
	private String R48_TYPE_OF_ACC;
	private String R48_PURPOSE;
	private String R48_CURRENCY;
	private String R48_BANK_RATE;
	private BigDecimal R48_AMT_LESS_184_DAYS;
	private BigDecimal R48_AMT_MORE_184_DAYS;
	private String R49_NAME_OF_BANK;
	private String R49_TYPE_OF_ACC;
	private String R49_PURPOSE;
	private String R49_CURRENCY;
	private String R49_BANK_RATE;
	private BigDecimal R49_AMT_LESS_184_DAYS;
	private BigDecimal R49_AMT_MORE_184_DAYS;
	private String R50_NAME_OF_BANK;
	private String R50_TYPE_OF_ACC;
	private String R50_PURPOSE;
	private String R50_CURRENCY;
	private String R50_BANK_RATE;
	private BigDecimal R50_AMT_LESS_184_DAYS;
	private BigDecimal R50_AMT_MORE_184_DAYS;

	private BigDecimal R51_TOT_AMT_LESS_184_DAYS;
	private BigDecimal R51_TOT_AMT_MORE_184_DAYS;

	public Date getReport_date() {
		return report_date;
	}

	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}

	public BigDecimal getReport_version() {
		return report_version;
	}

	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getR11_NAME_OF_BANK() {
		return R11_NAME_OF_BANK;
	}

	public void setR11_NAME_OF_BANK(String r11_NAME_OF_BANK) {
		R11_NAME_OF_BANK = r11_NAME_OF_BANK;
	}

	public String getR11_TYPE_OF_ACC() {
		return R11_TYPE_OF_ACC;
	}

	public void setR11_TYPE_OF_ACC(String r11_TYPE_OF_ACC) {
		R11_TYPE_OF_ACC = r11_TYPE_OF_ACC;
	}

	public String getR11_PURPOSE() {
		return R11_PURPOSE;
	}

	public void setR11_PURPOSE(String r11_PURPOSE) {
		R11_PURPOSE = r11_PURPOSE;
	}

	public String getR11_CURRENCY() {
		return R11_CURRENCY;
	}

	public void setR11_CURRENCY(String r11_CURRENCY) {
		R11_CURRENCY = r11_CURRENCY;
	}

	public String getR11_BANK_RATE() {
		return R11_BANK_RATE;
	}

	public void setR11_BANK_RATE(String r11_BANK_RATE) {
		R11_BANK_RATE = r11_BANK_RATE;
	}

	public BigDecimal getR11_AMT_LESS_184_DAYS() {
		return R11_AMT_LESS_184_DAYS;
	}

	public void setR11_AMT_LESS_184_DAYS(BigDecimal r11_AMT_LESS_184_DAYS) {
		R11_AMT_LESS_184_DAYS = r11_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR11_AMT_MORE_184_DAYS() {
		return R11_AMT_MORE_184_DAYS;
	}

	public void setR11_AMT_MORE_184_DAYS(BigDecimal r11_AMT_MORE_184_DAYS) {
		R11_AMT_MORE_184_DAYS = r11_AMT_MORE_184_DAYS;
	}

	public String getR12_NAME_OF_BANK() {
		return R12_NAME_OF_BANK;
	}

	public void setR12_NAME_OF_BANK(String r12_NAME_OF_BANK) {
		R12_NAME_OF_BANK = r12_NAME_OF_BANK;
	}

	public String getR12_TYPE_OF_ACC() {
		return R12_TYPE_OF_ACC;
	}

	public void setR12_TYPE_OF_ACC(String r12_TYPE_OF_ACC) {
		R12_TYPE_OF_ACC = r12_TYPE_OF_ACC;
	}

	public String getR12_PURPOSE() {
		return R12_PURPOSE;
	}

	public void setR12_PURPOSE(String r12_PURPOSE) {
		R12_PURPOSE = r12_PURPOSE;
	}

	public String getR12_CURRENCY() {
		return R12_CURRENCY;
	}

	public void setR12_CURRENCY(String r12_CURRENCY) {
		R12_CURRENCY = r12_CURRENCY;
	}

	public String getR12_BANK_RATE() {
		return R12_BANK_RATE;
	}

	public void setR12_BANK_RATE(String r12_BANK_RATE) {
		R12_BANK_RATE = r12_BANK_RATE;
	}

	public BigDecimal getR12_AMT_LESS_184_DAYS() {
		return R12_AMT_LESS_184_DAYS;
	}

	public void setR12_AMT_LESS_184_DAYS(BigDecimal r12_AMT_LESS_184_DAYS) {
		R12_AMT_LESS_184_DAYS = r12_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR12_AMT_MORE_184_DAYS() {
		return R12_AMT_MORE_184_DAYS;
	}

	public void setR12_AMT_MORE_184_DAYS(BigDecimal r12_AMT_MORE_184_DAYS) {
		R12_AMT_MORE_184_DAYS = r12_AMT_MORE_184_DAYS;
	}

	public String getR13_NAME_OF_BANK() {
		return R13_NAME_OF_BANK;
	}

	public void setR13_NAME_OF_BANK(String r13_NAME_OF_BANK) {
		R13_NAME_OF_BANK = r13_NAME_OF_BANK;
	}

	public String getR13_TYPE_OF_ACC() {
		return R13_TYPE_OF_ACC;
	}

	public void setR13_TYPE_OF_ACC(String r13_TYPE_OF_ACC) {
		R13_TYPE_OF_ACC = r13_TYPE_OF_ACC;
	}

	public String getR13_PURPOSE() {
		return R13_PURPOSE;
	}

	public void setR13_PURPOSE(String r13_PURPOSE) {
		R13_PURPOSE = r13_PURPOSE;
	}

	public String getR13_CURRENCY() {
		return R13_CURRENCY;
	}

	public void setR13_CURRENCY(String r13_CURRENCY) {
		R13_CURRENCY = r13_CURRENCY;
	}

	public String getR13_BANK_RATE() {
		return R13_BANK_RATE;
	}

	public void setR13_BANK_RATE(String r13_BANK_RATE) {
		R13_BANK_RATE = r13_BANK_RATE;
	}

	public BigDecimal getR13_AMT_LESS_184_DAYS() {
		return R13_AMT_LESS_184_DAYS;
	}

	public void setR13_AMT_LESS_184_DAYS(BigDecimal r13_AMT_LESS_184_DAYS) {
		R13_AMT_LESS_184_DAYS = r13_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR13_AMT_MORE_184_DAYS() {
		return R13_AMT_MORE_184_DAYS;
	}

	public void setR13_AMT_MORE_184_DAYS(BigDecimal r13_AMT_MORE_184_DAYS) {
		R13_AMT_MORE_184_DAYS = r13_AMT_MORE_184_DAYS;
	}

	public String getR14_NAME_OF_BANK() {
		return R14_NAME_OF_BANK;
	}

	public void setR14_NAME_OF_BANK(String r14_NAME_OF_BANK) {
		R14_NAME_OF_BANK = r14_NAME_OF_BANK;
	}

	public String getR14_TYPE_OF_ACC() {
		return R14_TYPE_OF_ACC;
	}

	public void setR14_TYPE_OF_ACC(String r14_TYPE_OF_ACC) {
		R14_TYPE_OF_ACC = r14_TYPE_OF_ACC;
	}

	public String getR14_PURPOSE() {
		return R14_PURPOSE;
	}

	public void setR14_PURPOSE(String r14_PURPOSE) {
		R14_PURPOSE = r14_PURPOSE;
	}

	public String getR14_CURRENCY() {
		return R14_CURRENCY;
	}

	public void setR14_CURRENCY(String r14_CURRENCY) {
		R14_CURRENCY = r14_CURRENCY;
	}

	public String getR14_BANK_RATE() {
		return R14_BANK_RATE;
	}

	public void setR14_BANK_RATE(String r14_BANK_RATE) {
		R14_BANK_RATE = r14_BANK_RATE;
	}

	public BigDecimal getR14_AMT_LESS_184_DAYS() {
		return R14_AMT_LESS_184_DAYS;
	}

	public void setR14_AMT_LESS_184_DAYS(BigDecimal r14_AMT_LESS_184_DAYS) {
		R14_AMT_LESS_184_DAYS = r14_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR14_AMT_MORE_184_DAYS() {
		return R14_AMT_MORE_184_DAYS;
	}

	public void setR14_AMT_MORE_184_DAYS(BigDecimal r14_AMT_MORE_184_DAYS) {
		R14_AMT_MORE_184_DAYS = r14_AMT_MORE_184_DAYS;
	}

	public String getR15_NAME_OF_BANK() {
		return R15_NAME_OF_BANK;
	}

	public void setR15_NAME_OF_BANK(String r15_NAME_OF_BANK) {
		R15_NAME_OF_BANK = r15_NAME_OF_BANK;
	}

	public String getR15_TYPE_OF_ACC() {
		return R15_TYPE_OF_ACC;
	}

	public void setR15_TYPE_OF_ACC(String r15_TYPE_OF_ACC) {
		R15_TYPE_OF_ACC = r15_TYPE_OF_ACC;
	}

	public String getR15_PURPOSE() {
		return R15_PURPOSE;
	}

	public void setR15_PURPOSE(String r15_PURPOSE) {
		R15_PURPOSE = r15_PURPOSE;
	}

	public String getR15_CURRENCY() {
		return R15_CURRENCY;
	}

	public void setR15_CURRENCY(String r15_CURRENCY) {
		R15_CURRENCY = r15_CURRENCY;
	}

	public String getR15_BANK_RATE() {
		return R15_BANK_RATE;
	}

	public void setR15_BANK_RATE(String r15_BANK_RATE) {
		R15_BANK_RATE = r15_BANK_RATE;
	}

	public BigDecimal getR15_AMT_LESS_184_DAYS() {
		return R15_AMT_LESS_184_DAYS;
	}

	public void setR15_AMT_LESS_184_DAYS(BigDecimal r15_AMT_LESS_184_DAYS) {
		R15_AMT_LESS_184_DAYS = r15_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR15_AMT_MORE_184_DAYS() {
		return R15_AMT_MORE_184_DAYS;
	}

	public void setR15_AMT_MORE_184_DAYS(BigDecimal r15_AMT_MORE_184_DAYS) {
		R15_AMT_MORE_184_DAYS = r15_AMT_MORE_184_DAYS;
	}

	public String getR16_NAME_OF_BANK() {
		return R16_NAME_OF_BANK;
	}

	public void setR16_NAME_OF_BANK(String r16_NAME_OF_BANK) {
		R16_NAME_OF_BANK = r16_NAME_OF_BANK;
	}

	public String getR16_TYPE_OF_ACC() {
		return R16_TYPE_OF_ACC;
	}

	public void setR16_TYPE_OF_ACC(String r16_TYPE_OF_ACC) {
		R16_TYPE_OF_ACC = r16_TYPE_OF_ACC;
	}

	public String getR16_PURPOSE() {
		return R16_PURPOSE;
	}

	public void setR16_PURPOSE(String r16_PURPOSE) {
		R16_PURPOSE = r16_PURPOSE;
	}

	public String getR16_CURRENCY() {
		return R16_CURRENCY;
	}

	public void setR16_CURRENCY(String r16_CURRENCY) {
		R16_CURRENCY = r16_CURRENCY;
	}

	public String getR16_BANK_RATE() {
		return R16_BANK_RATE;
	}

	public void setR16_BANK_RATE(String r16_BANK_RATE) {
		R16_BANK_RATE = r16_BANK_RATE;
	}

	public BigDecimal getR16_AMT_LESS_184_DAYS() {
		return R16_AMT_LESS_184_DAYS;
	}

	public void setR16_AMT_LESS_184_DAYS(BigDecimal r16_AMT_LESS_184_DAYS) {
		R16_AMT_LESS_184_DAYS = r16_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR16_AMT_MORE_184_DAYS() {
		return R16_AMT_MORE_184_DAYS;
	}

	public void setR16_AMT_MORE_184_DAYS(BigDecimal r16_AMT_MORE_184_DAYS) {
		R16_AMT_MORE_184_DAYS = r16_AMT_MORE_184_DAYS;
	}

	public String getR17_NAME_OF_BANK() {
		return R17_NAME_OF_BANK;
	}

	public void setR17_NAME_OF_BANK(String r17_NAME_OF_BANK) {
		R17_NAME_OF_BANK = r17_NAME_OF_BANK;
	}

	public String getR17_TYPE_OF_ACC() {
		return R17_TYPE_OF_ACC;
	}

	public void setR17_TYPE_OF_ACC(String r17_TYPE_OF_ACC) {
		R17_TYPE_OF_ACC = r17_TYPE_OF_ACC;
	}

	public String getR17_PURPOSE() {
		return R17_PURPOSE;
	}

	public void setR17_PURPOSE(String r17_PURPOSE) {
		R17_PURPOSE = r17_PURPOSE;
	}

	public String getR17_CURRENCY() {
		return R17_CURRENCY;
	}

	public void setR17_CURRENCY(String r17_CURRENCY) {
		R17_CURRENCY = r17_CURRENCY;
	}

	public String getR17_BANK_RATE() {
		return R17_BANK_RATE;
	}

	public void setR17_BANK_RATE(String r17_BANK_RATE) {
		R17_BANK_RATE = r17_BANK_RATE;
	}

	public BigDecimal getR17_AMT_LESS_184_DAYS() {
		return R17_AMT_LESS_184_DAYS;
	}

	public void setR17_AMT_LESS_184_DAYS(BigDecimal r17_AMT_LESS_184_DAYS) {
		R17_AMT_LESS_184_DAYS = r17_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR17_AMT_MORE_184_DAYS() {
		return R17_AMT_MORE_184_DAYS;
	}

	public void setR17_AMT_MORE_184_DAYS(BigDecimal r17_AMT_MORE_184_DAYS) {
		R17_AMT_MORE_184_DAYS = r17_AMT_MORE_184_DAYS;
	}

	public String getR18_NAME_OF_BANK() {
		return R18_NAME_OF_BANK;
	}

	public void setR18_NAME_OF_BANK(String r18_NAME_OF_BANK) {
		R18_NAME_OF_BANK = r18_NAME_OF_BANK;
	}

	public String getR18_TYPE_OF_ACC() {
		return R18_TYPE_OF_ACC;
	}

	public void setR18_TYPE_OF_ACC(String r18_TYPE_OF_ACC) {
		R18_TYPE_OF_ACC = r18_TYPE_OF_ACC;
	}

	public String getR18_PURPOSE() {
		return R18_PURPOSE;
	}

	public void setR18_PURPOSE(String r18_PURPOSE) {
		R18_PURPOSE = r18_PURPOSE;
	}

	public String getR18_CURRENCY() {
		return R18_CURRENCY;
	}

	public void setR18_CURRENCY(String r18_CURRENCY) {
		R18_CURRENCY = r18_CURRENCY;
	}

	public String getR18_BANK_RATE() {
		return R18_BANK_RATE;
	}

	public void setR18_BANK_RATE(String r18_BANK_RATE) {
		R18_BANK_RATE = r18_BANK_RATE;
	}

	public BigDecimal getR18_AMT_LESS_184_DAYS() {
		return R18_AMT_LESS_184_DAYS;
	}

	public void setR18_AMT_LESS_184_DAYS(BigDecimal r18_AMT_LESS_184_DAYS) {
		R18_AMT_LESS_184_DAYS = r18_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR18_AMT_MORE_184_DAYS() {
		return R18_AMT_MORE_184_DAYS;
	}

	public void setR18_AMT_MORE_184_DAYS(BigDecimal r18_AMT_MORE_184_DAYS) {
		R18_AMT_MORE_184_DAYS = r18_AMT_MORE_184_DAYS;
	}

	public String getR19_NAME_OF_BANK() {
		return R19_NAME_OF_BANK;
	}

	public void setR19_NAME_OF_BANK(String r19_NAME_OF_BANK) {
		R19_NAME_OF_BANK = r19_NAME_OF_BANK;
	}

	public String getR19_TYPE_OF_ACC() {
		return R19_TYPE_OF_ACC;
	}

	public void setR19_TYPE_OF_ACC(String r19_TYPE_OF_ACC) {
		R19_TYPE_OF_ACC = r19_TYPE_OF_ACC;
	}

	public String getR19_PURPOSE() {
		return R19_PURPOSE;
	}

	public void setR19_PURPOSE(String r19_PURPOSE) {
		R19_PURPOSE = r19_PURPOSE;
	}

	public String getR19_CURRENCY() {
		return R19_CURRENCY;
	}

	public void setR19_CURRENCY(String r19_CURRENCY) {
		R19_CURRENCY = r19_CURRENCY;
	}

	public String getR19_BANK_RATE() {
		return R19_BANK_RATE;
	}

	public void setR19_BANK_RATE(String r19_BANK_RATE) {
		R19_BANK_RATE = r19_BANK_RATE;
	}

	public BigDecimal getR19_AMT_LESS_184_DAYS() {
		return R19_AMT_LESS_184_DAYS;
	}

	public void setR19_AMT_LESS_184_DAYS(BigDecimal r19_AMT_LESS_184_DAYS) {
		R19_AMT_LESS_184_DAYS = r19_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR19_AMT_MORE_184_DAYS() {
		return R19_AMT_MORE_184_DAYS;
	}

	public void setR19_AMT_MORE_184_DAYS(BigDecimal r19_AMT_MORE_184_DAYS) {
		R19_AMT_MORE_184_DAYS = r19_AMT_MORE_184_DAYS;
	}

	public String getR20_NAME_OF_BANK() {
		return R20_NAME_OF_BANK;
	}

	public void setR20_NAME_OF_BANK(String r20_NAME_OF_BANK) {
		R20_NAME_OF_BANK = r20_NAME_OF_BANK;
	}

	public String getR20_TYPE_OF_ACC() {
		return R20_TYPE_OF_ACC;
	}

	public void setR20_TYPE_OF_ACC(String r20_TYPE_OF_ACC) {
		R20_TYPE_OF_ACC = r20_TYPE_OF_ACC;
	}

	public String getR20_PURPOSE() {
		return R20_PURPOSE;
	}

	public void setR20_PURPOSE(String r20_PURPOSE) {
		R20_PURPOSE = r20_PURPOSE;
	}

	public String getR20_CURRENCY() {
		return R20_CURRENCY;
	}

	public void setR20_CURRENCY(String r20_CURRENCY) {
		R20_CURRENCY = r20_CURRENCY;
	}

	public String getR20_BANK_RATE() {
		return R20_BANK_RATE;
	}

	public void setR20_BANK_RATE(String r20_BANK_RATE) {
		R20_BANK_RATE = r20_BANK_RATE;
	}

	public BigDecimal getR20_AMT_LESS_184_DAYS() {
		return R20_AMT_LESS_184_DAYS;
	}

	public void setR20_AMT_LESS_184_DAYS(BigDecimal r20_AMT_LESS_184_DAYS) {
		R20_AMT_LESS_184_DAYS = r20_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR20_AMT_MORE_184_DAYS() {
		return R20_AMT_MORE_184_DAYS;
	}

	public void setR20_AMT_MORE_184_DAYS(BigDecimal r20_AMT_MORE_184_DAYS) {
		R20_AMT_MORE_184_DAYS = r20_AMT_MORE_184_DAYS;
	}

	public String getR21_NAME_OF_BANK() {
		return R21_NAME_OF_BANK;
	}

	public void setR21_NAME_OF_BANK(String r21_NAME_OF_BANK) {
		R21_NAME_OF_BANK = r21_NAME_OF_BANK;
	}

	public String getR21_TYPE_OF_ACC() {
		return R21_TYPE_OF_ACC;
	}

	public void setR21_TYPE_OF_ACC(String r21_TYPE_OF_ACC) {
		R21_TYPE_OF_ACC = r21_TYPE_OF_ACC;
	}

	public String getR21_PURPOSE() {
		return R21_PURPOSE;
	}

	public void setR21_PURPOSE(String r21_PURPOSE) {
		R21_PURPOSE = r21_PURPOSE;
	}

	public String getR21_CURRENCY() {
		return R21_CURRENCY;
	}

	public void setR21_CURRENCY(String r21_CURRENCY) {
		R21_CURRENCY = r21_CURRENCY;
	}

	public String getR21_BANK_RATE() {
		return R21_BANK_RATE;
	}

	public void setR21_BANK_RATE(String r21_BANK_RATE) {
		R21_BANK_RATE = r21_BANK_RATE;
	}

	public BigDecimal getR21_AMT_LESS_184_DAYS() {
		return R21_AMT_LESS_184_DAYS;
	}

	public void setR21_AMT_LESS_184_DAYS(BigDecimal r21_AMT_LESS_184_DAYS) {
		R21_AMT_LESS_184_DAYS = r21_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR21_AMT_MORE_184_DAYS() {
		return R21_AMT_MORE_184_DAYS;
	}

	public void setR21_AMT_MORE_184_DAYS(BigDecimal r21_AMT_MORE_184_DAYS) {
		R21_AMT_MORE_184_DAYS = r21_AMT_MORE_184_DAYS;
	}

	public String getR22_NAME_OF_BANK() {
		return R22_NAME_OF_BANK;
	}

	public void setR22_NAME_OF_BANK(String r22_NAME_OF_BANK) {
		R22_NAME_OF_BANK = r22_NAME_OF_BANK;
	}

	public String getR22_TYPE_OF_ACC() {
		return R22_TYPE_OF_ACC;
	}

	public void setR22_TYPE_OF_ACC(String r22_TYPE_OF_ACC) {
		R22_TYPE_OF_ACC = r22_TYPE_OF_ACC;
	}

	public String getR22_PURPOSE() {
		return R22_PURPOSE;
	}

	public void setR22_PURPOSE(String r22_PURPOSE) {
		R22_PURPOSE = r22_PURPOSE;
	}

	public String getR22_CURRENCY() {
		return R22_CURRENCY;
	}

	public void setR22_CURRENCY(String r22_CURRENCY) {
		R22_CURRENCY = r22_CURRENCY;
	}

	public String getR22_BANK_RATE() {
		return R22_BANK_RATE;
	}

	public void setR22_BANK_RATE(String r22_BANK_RATE) {
		R22_BANK_RATE = r22_BANK_RATE;
	}

	public BigDecimal getR22_AMT_LESS_184_DAYS() {
		return R22_AMT_LESS_184_DAYS;
	}

	public void setR22_AMT_LESS_184_DAYS(BigDecimal r22_AMT_LESS_184_DAYS) {
		R22_AMT_LESS_184_DAYS = r22_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR22_AMT_MORE_184_DAYS() {
		return R22_AMT_MORE_184_DAYS;
	}

	public void setR22_AMT_MORE_184_DAYS(BigDecimal r22_AMT_MORE_184_DAYS) {
		R22_AMT_MORE_184_DAYS = r22_AMT_MORE_184_DAYS;
	}

	public String getR23_NAME_OF_BANK() {
		return R23_NAME_OF_BANK;
	}

	public void setR23_NAME_OF_BANK(String r23_NAME_OF_BANK) {
		R23_NAME_OF_BANK = r23_NAME_OF_BANK;
	}

	public String getR23_TYPE_OF_ACC() {
		return R23_TYPE_OF_ACC;
	}

	public void setR23_TYPE_OF_ACC(String r23_TYPE_OF_ACC) {
		R23_TYPE_OF_ACC = r23_TYPE_OF_ACC;
	}

	public String getR23_PURPOSE() {
		return R23_PURPOSE;
	}

	public void setR23_PURPOSE(String r23_PURPOSE) {
		R23_PURPOSE = r23_PURPOSE;
	}

	public String getR23_CURRENCY() {
		return R23_CURRENCY;
	}

	public void setR23_CURRENCY(String r23_CURRENCY) {
		R23_CURRENCY = r23_CURRENCY;
	}

	public String getR23_BANK_RATE() {
		return R23_BANK_RATE;
	}

	public void setR23_BANK_RATE(String r23_BANK_RATE) {
		R23_BANK_RATE = r23_BANK_RATE;
	}

	public BigDecimal getR23_AMT_LESS_184_DAYS() {
		return R23_AMT_LESS_184_DAYS;
	}

	public void setR23_AMT_LESS_184_DAYS(BigDecimal r23_AMT_LESS_184_DAYS) {
		R23_AMT_LESS_184_DAYS = r23_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR23_AMT_MORE_184_DAYS() {
		return R23_AMT_MORE_184_DAYS;
	}

	public void setR23_AMT_MORE_184_DAYS(BigDecimal r23_AMT_MORE_184_DAYS) {
		R23_AMT_MORE_184_DAYS = r23_AMT_MORE_184_DAYS;
	}

	public String getR24_NAME_OF_BANK() {
		return R24_NAME_OF_BANK;
	}

	public void setR24_NAME_OF_BANK(String r24_NAME_OF_BANK) {
		R24_NAME_OF_BANK = r24_NAME_OF_BANK;
	}

	public String getR24_TYPE_OF_ACC() {
		return R24_TYPE_OF_ACC;
	}

	public void setR24_TYPE_OF_ACC(String r24_TYPE_OF_ACC) {
		R24_TYPE_OF_ACC = r24_TYPE_OF_ACC;
	}

	public String getR24_PURPOSE() {
		return R24_PURPOSE;
	}

	public void setR24_PURPOSE(String r24_PURPOSE) {
		R24_PURPOSE = r24_PURPOSE;
	}

	public String getR24_CURRENCY() {
		return R24_CURRENCY;
	}

	public void setR24_CURRENCY(String r24_CURRENCY) {
		R24_CURRENCY = r24_CURRENCY;
	}

	public String getR24_BANK_RATE() {
		return R24_BANK_RATE;
	}

	public void setR24_BANK_RATE(String r24_BANK_RATE) {
		R24_BANK_RATE = r24_BANK_RATE;
	}

	public BigDecimal getR24_AMT_LESS_184_DAYS() {
		return R24_AMT_LESS_184_DAYS;
	}

	public void setR24_AMT_LESS_184_DAYS(BigDecimal r24_AMT_LESS_184_DAYS) {
		R24_AMT_LESS_184_DAYS = r24_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR24_AMT_MORE_184_DAYS() {
		return R24_AMT_MORE_184_DAYS;
	}

	public void setR24_AMT_MORE_184_DAYS(BigDecimal r24_AMT_MORE_184_DAYS) {
		R24_AMT_MORE_184_DAYS = r24_AMT_MORE_184_DAYS;
	}

	public String getR25_NAME_OF_BANK() {
		return R25_NAME_OF_BANK;
	}

	public void setR25_NAME_OF_BANK(String r25_NAME_OF_BANK) {
		R25_NAME_OF_BANK = r25_NAME_OF_BANK;
	}

	public String getR25_TYPE_OF_ACC() {
		return R25_TYPE_OF_ACC;
	}

	public void setR25_TYPE_OF_ACC(String r25_TYPE_OF_ACC) {
		R25_TYPE_OF_ACC = r25_TYPE_OF_ACC;
	}

	public String getR25_PURPOSE() {
		return R25_PURPOSE;
	}

	public void setR25_PURPOSE(String r25_PURPOSE) {
		R25_PURPOSE = r25_PURPOSE;
	}

	public String getR25_CURRENCY() {
		return R25_CURRENCY;
	}

	public void setR25_CURRENCY(String r25_CURRENCY) {
		R25_CURRENCY = r25_CURRENCY;
	}

	public String getR25_BANK_RATE() {
		return R25_BANK_RATE;
	}

	public void setR25_BANK_RATE(String r25_BANK_RATE) {
		R25_BANK_RATE = r25_BANK_RATE;
	}

	public BigDecimal getR25_AMT_LESS_184_DAYS() {
		return R25_AMT_LESS_184_DAYS;
	}

	public void setR25_AMT_LESS_184_DAYS(BigDecimal r25_AMT_LESS_184_DAYS) {
		R25_AMT_LESS_184_DAYS = r25_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR25_AMT_MORE_184_DAYS() {
		return R25_AMT_MORE_184_DAYS;
	}

	public void setR25_AMT_MORE_184_DAYS(BigDecimal r25_AMT_MORE_184_DAYS) {
		R25_AMT_MORE_184_DAYS = r25_AMT_MORE_184_DAYS;
	}

	public String getR26_NAME_OF_BANK() {
		return R26_NAME_OF_BANK;
	}

	public void setR26_NAME_OF_BANK(String r26_NAME_OF_BANK) {
		R26_NAME_OF_BANK = r26_NAME_OF_BANK;
	}

	public String getR26_TYPE_OF_ACC() {
		return R26_TYPE_OF_ACC;
	}

	public void setR26_TYPE_OF_ACC(String r26_TYPE_OF_ACC) {
		R26_TYPE_OF_ACC = r26_TYPE_OF_ACC;
	}

	public String getR26_PURPOSE() {
		return R26_PURPOSE;
	}

	public void setR26_PURPOSE(String r26_PURPOSE) {
		R26_PURPOSE = r26_PURPOSE;
	}

	public String getR26_CURRENCY() {
		return R26_CURRENCY;
	}

	public void setR26_CURRENCY(String r26_CURRENCY) {
		R26_CURRENCY = r26_CURRENCY;
	}

	public String getR26_BANK_RATE() {
		return R26_BANK_RATE;
	}

	public void setR26_BANK_RATE(String r26_BANK_RATE) {
		R26_BANK_RATE = r26_BANK_RATE;
	}

	public BigDecimal getR26_AMT_LESS_184_DAYS() {
		return R26_AMT_LESS_184_DAYS;
	}

	public void setR26_AMT_LESS_184_DAYS(BigDecimal r26_AMT_LESS_184_DAYS) {
		R26_AMT_LESS_184_DAYS = r26_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR26_AMT_MORE_184_DAYS() {
		return R26_AMT_MORE_184_DAYS;
	}

	public void setR26_AMT_MORE_184_DAYS(BigDecimal r26_AMT_MORE_184_DAYS) {
		R26_AMT_MORE_184_DAYS = r26_AMT_MORE_184_DAYS;
	}

	public String getR27_NAME_OF_BANK() {
		return R27_NAME_OF_BANK;
	}

	public void setR27_NAME_OF_BANK(String r27_NAME_OF_BANK) {
		R27_NAME_OF_BANK = r27_NAME_OF_BANK;
	}

	public String getR27_TYPE_OF_ACC() {
		return R27_TYPE_OF_ACC;
	}

	public void setR27_TYPE_OF_ACC(String r27_TYPE_OF_ACC) {
		R27_TYPE_OF_ACC = r27_TYPE_OF_ACC;
	}

	public String getR27_PURPOSE() {
		return R27_PURPOSE;
	}

	public void setR27_PURPOSE(String r27_PURPOSE) {
		R27_PURPOSE = r27_PURPOSE;
	}

	public String getR27_CURRENCY() {
		return R27_CURRENCY;
	}

	public void setR27_CURRENCY(String r27_CURRENCY) {
		R27_CURRENCY = r27_CURRENCY;
	}

	public String getR27_BANK_RATE() {
		return R27_BANK_RATE;
	}

	public void setR27_BANK_RATE(String r27_BANK_RATE) {
		R27_BANK_RATE = r27_BANK_RATE;
	}

	public BigDecimal getR27_AMT_LESS_184_DAYS() {
		return R27_AMT_LESS_184_DAYS;
	}

	public void setR27_AMT_LESS_184_DAYS(BigDecimal r27_AMT_LESS_184_DAYS) {
		R27_AMT_LESS_184_DAYS = r27_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR27_AMT_MORE_184_DAYS() {
		return R27_AMT_MORE_184_DAYS;
	}

	public void setR27_AMT_MORE_184_DAYS(BigDecimal r27_AMT_MORE_184_DAYS) {
		R27_AMT_MORE_184_DAYS = r27_AMT_MORE_184_DAYS;
	}

	public String getR28_NAME_OF_BANK() {
		return R28_NAME_OF_BANK;
	}

	public void setR28_NAME_OF_BANK(String r28_NAME_OF_BANK) {
		R28_NAME_OF_BANK = r28_NAME_OF_BANK;
	}

	public String getR28_TYPE_OF_ACC() {
		return R28_TYPE_OF_ACC;
	}

	public void setR28_TYPE_OF_ACC(String r28_TYPE_OF_ACC) {
		R28_TYPE_OF_ACC = r28_TYPE_OF_ACC;
	}

	public String getR28_PURPOSE() {
		return R28_PURPOSE;
	}

	public void setR28_PURPOSE(String r28_PURPOSE) {
		R28_PURPOSE = r28_PURPOSE;
	}

	public String getR28_CURRENCY() {
		return R28_CURRENCY;
	}

	public void setR28_CURRENCY(String r28_CURRENCY) {
		R28_CURRENCY = r28_CURRENCY;
	}

	public String getR28_BANK_RATE() {
		return R28_BANK_RATE;
	}

	public void setR28_BANK_RATE(String r28_BANK_RATE) {
		R28_BANK_RATE = r28_BANK_RATE;
	}

	public BigDecimal getR28_AMT_LESS_184_DAYS() {
		return R28_AMT_LESS_184_DAYS;
	}

	public void setR28_AMT_LESS_184_DAYS(BigDecimal r28_AMT_LESS_184_DAYS) {
		R28_AMT_LESS_184_DAYS = r28_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR28_AMT_MORE_184_DAYS() {
		return R28_AMT_MORE_184_DAYS;
	}

	public void setR28_AMT_MORE_184_DAYS(BigDecimal r28_AMT_MORE_184_DAYS) {
		R28_AMT_MORE_184_DAYS = r28_AMT_MORE_184_DAYS;
	}

	public String getR29_NAME_OF_BANK() {
		return R29_NAME_OF_BANK;
	}

	public void setR29_NAME_OF_BANK(String r29_NAME_OF_BANK) {
		R29_NAME_OF_BANK = r29_NAME_OF_BANK;
	}

	public String getR29_TYPE_OF_ACC() {
		return R29_TYPE_OF_ACC;
	}

	public void setR29_TYPE_OF_ACC(String r29_TYPE_OF_ACC) {
		R29_TYPE_OF_ACC = r29_TYPE_OF_ACC;
	}

	public String getR29_PURPOSE() {
		return R29_PURPOSE;
	}

	public void setR29_PURPOSE(String r29_PURPOSE) {
		R29_PURPOSE = r29_PURPOSE;
	}

	public String getR29_CURRENCY() {
		return R29_CURRENCY;
	}

	public void setR29_CURRENCY(String r29_CURRENCY) {
		R29_CURRENCY = r29_CURRENCY;
	}

	public String getR29_BANK_RATE() {
		return R29_BANK_RATE;
	}

	public void setR29_BANK_RATE(String r29_BANK_RATE) {
		R29_BANK_RATE = r29_BANK_RATE;
	}

	public BigDecimal getR29_AMT_LESS_184_DAYS() {
		return R29_AMT_LESS_184_DAYS;
	}

	public void setR29_AMT_LESS_184_DAYS(BigDecimal r29_AMT_LESS_184_DAYS) {
		R29_AMT_LESS_184_DAYS = r29_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR29_AMT_MORE_184_DAYS() {
		return R29_AMT_MORE_184_DAYS;
	}

	public void setR29_AMT_MORE_184_DAYS(BigDecimal r29_AMT_MORE_184_DAYS) {
		R29_AMT_MORE_184_DAYS = r29_AMT_MORE_184_DAYS;
	}

	public String getR30_NAME_OF_BANK() {
		return R30_NAME_OF_BANK;
	}

	public void setR30_NAME_OF_BANK(String r30_NAME_OF_BANK) {
		R30_NAME_OF_BANK = r30_NAME_OF_BANK;
	}

	public String getR30_TYPE_OF_ACC() {
		return R30_TYPE_OF_ACC;
	}

	public void setR30_TYPE_OF_ACC(String r30_TYPE_OF_ACC) {
		R30_TYPE_OF_ACC = r30_TYPE_OF_ACC;
	}

	public String getR30_PURPOSE() {
		return R30_PURPOSE;
	}

	public void setR30_PURPOSE(String r30_PURPOSE) {
		R30_PURPOSE = r30_PURPOSE;
	}

	public String getR30_CURRENCY() {
		return R30_CURRENCY;
	}

	public void setR30_CURRENCY(String r30_CURRENCY) {
		R30_CURRENCY = r30_CURRENCY;
	}

	public String getR30_BANK_RATE() {
		return R30_BANK_RATE;
	}

	public void setR30_BANK_RATE(String r30_BANK_RATE) {
		R30_BANK_RATE = r30_BANK_RATE;
	}

	public BigDecimal getR30_AMT_LESS_184_DAYS() {
		return R30_AMT_LESS_184_DAYS;
	}

	public void setR30_AMT_LESS_184_DAYS(BigDecimal r30_AMT_LESS_184_DAYS) {
		R30_AMT_LESS_184_DAYS = r30_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR30_AMT_MORE_184_DAYS() {
		return R30_AMT_MORE_184_DAYS;
	}

	public void setR30_AMT_MORE_184_DAYS(BigDecimal r30_AMT_MORE_184_DAYS) {
		R30_AMT_MORE_184_DAYS = r30_AMT_MORE_184_DAYS;
	}

	public String getR31_NAME_OF_BANK() {
		return R31_NAME_OF_BANK;
	}

	public void setR31_NAME_OF_BANK(String r31_NAME_OF_BANK) {
		R31_NAME_OF_BANK = r31_NAME_OF_BANK;
	}

	public String getR31_TYPE_OF_ACC() {
		return R31_TYPE_OF_ACC;
	}

	public void setR31_TYPE_OF_ACC(String r31_TYPE_OF_ACC) {
		R31_TYPE_OF_ACC = r31_TYPE_OF_ACC;
	}

	public String getR31_PURPOSE() {
		return R31_PURPOSE;
	}

	public void setR31_PURPOSE(String r31_PURPOSE) {
		R31_PURPOSE = r31_PURPOSE;
	}

	public String getR31_CURRENCY() {
		return R31_CURRENCY;
	}

	public void setR31_CURRENCY(String r31_CURRENCY) {
		R31_CURRENCY = r31_CURRENCY;
	}

	public String getR31_BANK_RATE() {
		return R31_BANK_RATE;
	}

	public void setR31_BANK_RATE(String r31_BANK_RATE) {
		R31_BANK_RATE = r31_BANK_RATE;
	}

	public BigDecimal getR31_AMT_LESS_184_DAYS() {
		return R31_AMT_LESS_184_DAYS;
	}

	public void setR31_AMT_LESS_184_DAYS(BigDecimal r31_AMT_LESS_184_DAYS) {
		R31_AMT_LESS_184_DAYS = r31_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR31_AMT_MORE_184_DAYS() {
		return R31_AMT_MORE_184_DAYS;
	}

	public void setR31_AMT_MORE_184_DAYS(BigDecimal r31_AMT_MORE_184_DAYS) {
		R31_AMT_MORE_184_DAYS = r31_AMT_MORE_184_DAYS;
	}

	public String getR32_NAME_OF_BANK() {
		return R32_NAME_OF_BANK;
	}

	public void setR32_NAME_OF_BANK(String r32_NAME_OF_BANK) {
		R32_NAME_OF_BANK = r32_NAME_OF_BANK;
	}

	public String getR32_TYPE_OF_ACC() {
		return R32_TYPE_OF_ACC;
	}

	public void setR32_TYPE_OF_ACC(String r32_TYPE_OF_ACC) {
		R32_TYPE_OF_ACC = r32_TYPE_OF_ACC;
	}

	public String getR32_PURPOSE() {
		return R32_PURPOSE;
	}

	public void setR32_PURPOSE(String r32_PURPOSE) {
		R32_PURPOSE = r32_PURPOSE;
	}

	public String getR32_CURRENCY() {
		return R32_CURRENCY;
	}

	public void setR32_CURRENCY(String r32_CURRENCY) {
		R32_CURRENCY = r32_CURRENCY;
	}

	public String getR32_BANK_RATE() {
		return R32_BANK_RATE;
	}

	public void setR32_BANK_RATE(String r32_BANK_RATE) {
		R32_BANK_RATE = r32_BANK_RATE;
	}

	public BigDecimal getR32_AMT_LESS_184_DAYS() {
		return R32_AMT_LESS_184_DAYS;
	}

	public void setR32_AMT_LESS_184_DAYS(BigDecimal r32_AMT_LESS_184_DAYS) {
		R32_AMT_LESS_184_DAYS = r32_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR32_AMT_MORE_184_DAYS() {
		return R32_AMT_MORE_184_DAYS;
	}

	public void setR32_AMT_MORE_184_DAYS(BigDecimal r32_AMT_MORE_184_DAYS) {
		R32_AMT_MORE_184_DAYS = r32_AMT_MORE_184_DAYS;
	}

	public String getR33_NAME_OF_BANK() {
		return R33_NAME_OF_BANK;
	}

	public void setR33_NAME_OF_BANK(String r33_NAME_OF_BANK) {
		R33_NAME_OF_BANK = r33_NAME_OF_BANK;
	}

	public String getR33_TYPE_OF_ACC() {
		return R33_TYPE_OF_ACC;
	}

	public void setR33_TYPE_OF_ACC(String r33_TYPE_OF_ACC) {
		R33_TYPE_OF_ACC = r33_TYPE_OF_ACC;
	}

	public String getR33_PURPOSE() {
		return R33_PURPOSE;
	}

	public void setR33_PURPOSE(String r33_PURPOSE) {
		R33_PURPOSE = r33_PURPOSE;
	}

	public String getR33_CURRENCY() {
		return R33_CURRENCY;
	}

	public void setR33_CURRENCY(String r33_CURRENCY) {
		R33_CURRENCY = r33_CURRENCY;
	}

	public String getR33_BANK_RATE() {
		return R33_BANK_RATE;
	}

	public void setR33_BANK_RATE(String r33_BANK_RATE) {
		R33_BANK_RATE = r33_BANK_RATE;
	}

	public BigDecimal getR33_AMT_LESS_184_DAYS() {
		return R33_AMT_LESS_184_DAYS;
	}

	public void setR33_AMT_LESS_184_DAYS(BigDecimal r33_AMT_LESS_184_DAYS) {
		R33_AMT_LESS_184_DAYS = r33_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR33_AMT_MORE_184_DAYS() {
		return R33_AMT_MORE_184_DAYS;
	}

	public void setR33_AMT_MORE_184_DAYS(BigDecimal r33_AMT_MORE_184_DAYS) {
		R33_AMT_MORE_184_DAYS = r33_AMT_MORE_184_DAYS;
	}

	public String getR34_NAME_OF_BANK() {
		return R34_NAME_OF_BANK;
	}

	public void setR34_NAME_OF_BANK(String r34_NAME_OF_BANK) {
		R34_NAME_OF_BANK = r34_NAME_OF_BANK;
	}

	public String getR34_TYPE_OF_ACC() {
		return R34_TYPE_OF_ACC;
	}

	public void setR34_TYPE_OF_ACC(String r34_TYPE_OF_ACC) {
		R34_TYPE_OF_ACC = r34_TYPE_OF_ACC;
	}

	public String getR34_PURPOSE() {
		return R34_PURPOSE;
	}

	public void setR34_PURPOSE(String r34_PURPOSE) {
		R34_PURPOSE = r34_PURPOSE;
	}

	public String getR34_CURRENCY() {
		return R34_CURRENCY;
	}

	public void setR34_CURRENCY(String r34_CURRENCY) {
		R34_CURRENCY = r34_CURRENCY;
	}

	public String getR34_BANK_RATE() {
		return R34_BANK_RATE;
	}

	public void setR34_BANK_RATE(String r34_BANK_RATE) {
		R34_BANK_RATE = r34_BANK_RATE;
	}

	public BigDecimal getR34_AMT_LESS_184_DAYS() {
		return R34_AMT_LESS_184_DAYS;
	}

	public void setR34_AMT_LESS_184_DAYS(BigDecimal r34_AMT_LESS_184_DAYS) {
		R34_AMT_LESS_184_DAYS = r34_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR34_AMT_MORE_184_DAYS() {
		return R34_AMT_MORE_184_DAYS;
	}

	public void setR34_AMT_MORE_184_DAYS(BigDecimal r34_AMT_MORE_184_DAYS) {
		R34_AMT_MORE_184_DAYS = r34_AMT_MORE_184_DAYS;
	}

	public String getR35_NAME_OF_BANK() {
		return R35_NAME_OF_BANK;
	}

	public void setR35_NAME_OF_BANK(String r35_NAME_OF_BANK) {
		R35_NAME_OF_BANK = r35_NAME_OF_BANK;
	}

	public String getR35_TYPE_OF_ACC() {
		return R35_TYPE_OF_ACC;
	}

	public void setR35_TYPE_OF_ACC(String r35_TYPE_OF_ACC) {
		R35_TYPE_OF_ACC = r35_TYPE_OF_ACC;
	}

	public String getR35_PURPOSE() {
		return R35_PURPOSE;
	}

	public void setR35_PURPOSE(String r35_PURPOSE) {
		R35_PURPOSE = r35_PURPOSE;
	}

	public String getR35_CURRENCY() {
		return R35_CURRENCY;
	}

	public void setR35_CURRENCY(String r35_CURRENCY) {
		R35_CURRENCY = r35_CURRENCY;
	}

	public String getR35_BANK_RATE() {
		return R35_BANK_RATE;
	}

	public void setR35_BANK_RATE(String r35_BANK_RATE) {
		R35_BANK_RATE = r35_BANK_RATE;
	}

	public BigDecimal getR35_AMT_LESS_184_DAYS() {
		return R35_AMT_LESS_184_DAYS;
	}

	public void setR35_AMT_LESS_184_DAYS(BigDecimal r35_AMT_LESS_184_DAYS) {
		R35_AMT_LESS_184_DAYS = r35_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR35_AMT_MORE_184_DAYS() {
		return R35_AMT_MORE_184_DAYS;
	}

	public void setR35_AMT_MORE_184_DAYS(BigDecimal r35_AMT_MORE_184_DAYS) {
		R35_AMT_MORE_184_DAYS = r35_AMT_MORE_184_DAYS;
	}

	public String getR36_NAME_OF_BANK() {
		return R36_NAME_OF_BANK;
	}

	public void setR36_NAME_OF_BANK(String r36_NAME_OF_BANK) {
		R36_NAME_OF_BANK = r36_NAME_OF_BANK;
	}

	public String getR36_TYPE_OF_ACC() {
		return R36_TYPE_OF_ACC;
	}

	public void setR36_TYPE_OF_ACC(String r36_TYPE_OF_ACC) {
		R36_TYPE_OF_ACC = r36_TYPE_OF_ACC;
	}

	public String getR36_PURPOSE() {
		return R36_PURPOSE;
	}

	public void setR36_PURPOSE(String r36_PURPOSE) {
		R36_PURPOSE = r36_PURPOSE;
	}

	public String getR36_CURRENCY() {
		return R36_CURRENCY;
	}

	public void setR36_CURRENCY(String r36_CURRENCY) {
		R36_CURRENCY = r36_CURRENCY;
	}

	public String getR36_BANK_RATE() {
		return R36_BANK_RATE;
	}

	public void setR36_BANK_RATE(String r36_BANK_RATE) {
		R36_BANK_RATE = r36_BANK_RATE;
	}

	public BigDecimal getR36_AMT_LESS_184_DAYS() {
		return R36_AMT_LESS_184_DAYS;
	}

	public void setR36_AMT_LESS_184_DAYS(BigDecimal r36_AMT_LESS_184_DAYS) {
		R36_AMT_LESS_184_DAYS = r36_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR36_AMT_MORE_184_DAYS() {
		return R36_AMT_MORE_184_DAYS;
	}

	public void setR36_AMT_MORE_184_DAYS(BigDecimal r36_AMT_MORE_184_DAYS) {
		R36_AMT_MORE_184_DAYS = r36_AMT_MORE_184_DAYS;
	}

	public String getR37_NAME_OF_BANK() {
		return R37_NAME_OF_BANK;
	}

	public void setR37_NAME_OF_BANK(String r37_NAME_OF_BANK) {
		R37_NAME_OF_BANK = r37_NAME_OF_BANK;
	}

	public String getR37_TYPE_OF_ACC() {
		return R37_TYPE_OF_ACC;
	}

	public void setR37_TYPE_OF_ACC(String r37_TYPE_OF_ACC) {
		R37_TYPE_OF_ACC = r37_TYPE_OF_ACC;
	}

	public String getR37_PURPOSE() {
		return R37_PURPOSE;
	}

	public void setR37_PURPOSE(String r37_PURPOSE) {
		R37_PURPOSE = r37_PURPOSE;
	}

	public String getR37_CURRENCY() {
		return R37_CURRENCY;
	}

	public void setR37_CURRENCY(String r37_CURRENCY) {
		R37_CURRENCY = r37_CURRENCY;
	}

	public String getR37_BANK_RATE() {
		return R37_BANK_RATE;
	}

	public void setR37_BANK_RATE(String r37_BANK_RATE) {
		R37_BANK_RATE = r37_BANK_RATE;
	}

	public BigDecimal getR37_AMT_LESS_184_DAYS() {
		return R37_AMT_LESS_184_DAYS;
	}

	public void setR37_AMT_LESS_184_DAYS(BigDecimal r37_AMT_LESS_184_DAYS) {
		R37_AMT_LESS_184_DAYS = r37_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR37_AMT_MORE_184_DAYS() {
		return R37_AMT_MORE_184_DAYS;
	}

	public void setR37_AMT_MORE_184_DAYS(BigDecimal r37_AMT_MORE_184_DAYS) {
		R37_AMT_MORE_184_DAYS = r37_AMT_MORE_184_DAYS;
	}

	public String getR38_NAME_OF_BANK() {
		return R38_NAME_OF_BANK;
	}

	public void setR38_NAME_OF_BANK(String r38_NAME_OF_BANK) {
		R38_NAME_OF_BANK = r38_NAME_OF_BANK;
	}

	public String getR38_TYPE_OF_ACC() {
		return R38_TYPE_OF_ACC;
	}

	public void setR38_TYPE_OF_ACC(String r38_TYPE_OF_ACC) {
		R38_TYPE_OF_ACC = r38_TYPE_OF_ACC;
	}

	public String getR38_PURPOSE() {
		return R38_PURPOSE;
	}

	public void setR38_PURPOSE(String r38_PURPOSE) {
		R38_PURPOSE = r38_PURPOSE;
	}

	public String getR38_CURRENCY() {
		return R38_CURRENCY;
	}

	public void setR38_CURRENCY(String r38_CURRENCY) {
		R38_CURRENCY = r38_CURRENCY;
	}

	public String getR38_BANK_RATE() {
		return R38_BANK_RATE;
	}

	public void setR38_BANK_RATE(String r38_BANK_RATE) {
		R38_BANK_RATE = r38_BANK_RATE;
	}

	public BigDecimal getR38_AMT_LESS_184_DAYS() {
		return R38_AMT_LESS_184_DAYS;
	}

	public void setR38_AMT_LESS_184_DAYS(BigDecimal r38_AMT_LESS_184_DAYS) {
		R38_AMT_LESS_184_DAYS = r38_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR38_AMT_MORE_184_DAYS() {
		return R38_AMT_MORE_184_DAYS;
	}

	public void setR38_AMT_MORE_184_DAYS(BigDecimal r38_AMT_MORE_184_DAYS) {
		R38_AMT_MORE_184_DAYS = r38_AMT_MORE_184_DAYS;
	}

	public String getR39_NAME_OF_BANK() {
		return R39_NAME_OF_BANK;
	}

	public void setR39_NAME_OF_BANK(String r39_NAME_OF_BANK) {
		R39_NAME_OF_BANK = r39_NAME_OF_BANK;
	}

	public String getR39_TYPE_OF_ACC() {
		return R39_TYPE_OF_ACC;
	}

	public void setR39_TYPE_OF_ACC(String r39_TYPE_OF_ACC) {
		R39_TYPE_OF_ACC = r39_TYPE_OF_ACC;
	}

	public String getR39_PURPOSE() {
		return R39_PURPOSE;
	}

	public void setR39_PURPOSE(String r39_PURPOSE) {
		R39_PURPOSE = r39_PURPOSE;
	}

	public String getR39_CURRENCY() {
		return R39_CURRENCY;
	}

	public void setR39_CURRENCY(String r39_CURRENCY) {
		R39_CURRENCY = r39_CURRENCY;
	}

	public String getR39_BANK_RATE() {
		return R39_BANK_RATE;
	}

	public void setR39_BANK_RATE(String r39_BANK_RATE) {
		R39_BANK_RATE = r39_BANK_RATE;
	}

	public BigDecimal getR39_AMT_LESS_184_DAYS() {
		return R39_AMT_LESS_184_DAYS;
	}

	public void setR39_AMT_LESS_184_DAYS(BigDecimal r39_AMT_LESS_184_DAYS) {
		R39_AMT_LESS_184_DAYS = r39_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR39_AMT_MORE_184_DAYS() {
		return R39_AMT_MORE_184_DAYS;
	}

	public void setR39_AMT_MORE_184_DAYS(BigDecimal r39_AMT_MORE_184_DAYS) {
		R39_AMT_MORE_184_DAYS = r39_AMT_MORE_184_DAYS;
	}

	public String getR40_NAME_OF_BANK() {
		return R40_NAME_OF_BANK;
	}

	public void setR40_NAME_OF_BANK(String r40_NAME_OF_BANK) {
		R40_NAME_OF_BANK = r40_NAME_OF_BANK;
	}

	public String getR40_TYPE_OF_ACC() {
		return R40_TYPE_OF_ACC;
	}

	public void setR40_TYPE_OF_ACC(String r40_TYPE_OF_ACC) {
		R40_TYPE_OF_ACC = r40_TYPE_OF_ACC;
	}

	public String getR40_PURPOSE() {
		return R40_PURPOSE;
	}

	public void setR40_PURPOSE(String r40_PURPOSE) {
		R40_PURPOSE = r40_PURPOSE;
	}

	public String getR40_CURRENCY() {
		return R40_CURRENCY;
	}

	public void setR40_CURRENCY(String r40_CURRENCY) {
		R40_CURRENCY = r40_CURRENCY;
	}

	public String getR40_BANK_RATE() {
		return R40_BANK_RATE;
	}

	public void setR40_BANK_RATE(String r40_BANK_RATE) {
		R40_BANK_RATE = r40_BANK_RATE;
	}

	public BigDecimal getR40_AMT_LESS_184_DAYS() {
		return R40_AMT_LESS_184_DAYS;
	}

	public void setR40_AMT_LESS_184_DAYS(BigDecimal r40_AMT_LESS_184_DAYS) {
		R40_AMT_LESS_184_DAYS = r40_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR40_AMT_MORE_184_DAYS() {
		return R40_AMT_MORE_184_DAYS;
	}

	public void setR40_AMT_MORE_184_DAYS(BigDecimal r40_AMT_MORE_184_DAYS) {
		R40_AMT_MORE_184_DAYS = r40_AMT_MORE_184_DAYS;
	}

	public String getR41_NAME_OF_BANK() {
		return R41_NAME_OF_BANK;
	}

	public void setR41_NAME_OF_BANK(String r41_NAME_OF_BANK) {
		R41_NAME_OF_BANK = r41_NAME_OF_BANK;
	}

	public String getR41_TYPE_OF_ACC() {
		return R41_TYPE_OF_ACC;
	}

	public void setR41_TYPE_OF_ACC(String r41_TYPE_OF_ACC) {
		R41_TYPE_OF_ACC = r41_TYPE_OF_ACC;
	}

	public String getR41_PURPOSE() {
		return R41_PURPOSE;
	}

	public void setR41_PURPOSE(String r41_PURPOSE) {
		R41_PURPOSE = r41_PURPOSE;
	}

	public String getR41_CURRENCY() {
		return R41_CURRENCY;
	}

	public void setR41_CURRENCY(String r41_CURRENCY) {
		R41_CURRENCY = r41_CURRENCY;
	}

	public String getR41_BANK_RATE() {
		return R41_BANK_RATE;
	}

	public void setR41_BANK_RATE(String r41_BANK_RATE) {
		R41_BANK_RATE = r41_BANK_RATE;
	}

	public BigDecimal getR41_AMT_LESS_184_DAYS() {
		return R41_AMT_LESS_184_DAYS;
	}

	public void setR41_AMT_LESS_184_DAYS(BigDecimal r41_AMT_LESS_184_DAYS) {
		R41_AMT_LESS_184_DAYS = r41_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR41_AMT_MORE_184_DAYS() {
		return R41_AMT_MORE_184_DAYS;
	}

	public void setR41_AMT_MORE_184_DAYS(BigDecimal r41_AMT_MORE_184_DAYS) {
		R41_AMT_MORE_184_DAYS = r41_AMT_MORE_184_DAYS;
	}

	public String getR42_NAME_OF_BANK() {
		return R42_NAME_OF_BANK;
	}

	public void setR42_NAME_OF_BANK(String r42_NAME_OF_BANK) {
		R42_NAME_OF_BANK = r42_NAME_OF_BANK;
	}

	public String getR42_TYPE_OF_ACC() {
		return R42_TYPE_OF_ACC;
	}

	public void setR42_TYPE_OF_ACC(String r42_TYPE_OF_ACC) {
		R42_TYPE_OF_ACC = r42_TYPE_OF_ACC;
	}

	public String getR42_PURPOSE() {
		return R42_PURPOSE;
	}

	public void setR42_PURPOSE(String r42_PURPOSE) {
		R42_PURPOSE = r42_PURPOSE;
	}

	public String getR42_CURRENCY() {
		return R42_CURRENCY;
	}

	public void setR42_CURRENCY(String r42_CURRENCY) {
		R42_CURRENCY = r42_CURRENCY;
	}

	public String getR42_BANK_RATE() {
		return R42_BANK_RATE;
	}

	public void setR42_BANK_RATE(String r42_BANK_RATE) {
		R42_BANK_RATE = r42_BANK_RATE;
	}

	public BigDecimal getR42_AMT_LESS_184_DAYS() {
		return R42_AMT_LESS_184_DAYS;
	}

	public void setR42_AMT_LESS_184_DAYS(BigDecimal r42_AMT_LESS_184_DAYS) {
		R42_AMT_LESS_184_DAYS = r42_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR42_AMT_MORE_184_DAYS() {
		return R42_AMT_MORE_184_DAYS;
	}

	public void setR42_AMT_MORE_184_DAYS(BigDecimal r42_AMT_MORE_184_DAYS) {
		R42_AMT_MORE_184_DAYS = r42_AMT_MORE_184_DAYS;
	}

	public String getR43_NAME_OF_BANK() {
		return R43_NAME_OF_BANK;
	}

	public void setR43_NAME_OF_BANK(String r43_NAME_OF_BANK) {
		R43_NAME_OF_BANK = r43_NAME_OF_BANK;
	}

	public String getR43_TYPE_OF_ACC() {
		return R43_TYPE_OF_ACC;
	}

	public void setR43_TYPE_OF_ACC(String r43_TYPE_OF_ACC) {
		R43_TYPE_OF_ACC = r43_TYPE_OF_ACC;
	}

	public String getR43_PURPOSE() {
		return R43_PURPOSE;
	}

	public void setR43_PURPOSE(String r43_PURPOSE) {
		R43_PURPOSE = r43_PURPOSE;
	}

	public String getR43_CURRENCY() {
		return R43_CURRENCY;
	}

	public void setR43_CURRENCY(String r43_CURRENCY) {
		R43_CURRENCY = r43_CURRENCY;
	}

	public String getR43_BANK_RATE() {
		return R43_BANK_RATE;
	}

	public void setR43_BANK_RATE(String r43_BANK_RATE) {
		R43_BANK_RATE = r43_BANK_RATE;
	}

	public BigDecimal getR43_AMT_LESS_184_DAYS() {
		return R43_AMT_LESS_184_DAYS;
	}

	public void setR43_AMT_LESS_184_DAYS(BigDecimal r43_AMT_LESS_184_DAYS) {
		R43_AMT_LESS_184_DAYS = r43_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR43_AMT_MORE_184_DAYS() {
		return R43_AMT_MORE_184_DAYS;
	}

	public void setR43_AMT_MORE_184_DAYS(BigDecimal r43_AMT_MORE_184_DAYS) {
		R43_AMT_MORE_184_DAYS = r43_AMT_MORE_184_DAYS;
	}

	public String getR44_NAME_OF_BANK() {
		return R44_NAME_OF_BANK;
	}

	public void setR44_NAME_OF_BANK(String r44_NAME_OF_BANK) {
		R44_NAME_OF_BANK = r44_NAME_OF_BANK;
	}

	public String getR44_TYPE_OF_ACC() {
		return R44_TYPE_OF_ACC;
	}

	public void setR44_TYPE_OF_ACC(String r44_TYPE_OF_ACC) {
		R44_TYPE_OF_ACC = r44_TYPE_OF_ACC;
	}

	public String getR44_PURPOSE() {
		return R44_PURPOSE;
	}

	public void setR44_PURPOSE(String r44_PURPOSE) {
		R44_PURPOSE = r44_PURPOSE;
	}

	public String getR44_CURRENCY() {
		return R44_CURRENCY;
	}

	public void setR44_CURRENCY(String r44_CURRENCY) {
		R44_CURRENCY = r44_CURRENCY;
	}

	public String getR44_BANK_RATE() {
		return R44_BANK_RATE;
	}

	public void setR44_BANK_RATE(String r44_BANK_RATE) {
		R44_BANK_RATE = r44_BANK_RATE;
	}

	public BigDecimal getR44_AMT_LESS_184_DAYS() {
		return R44_AMT_LESS_184_DAYS;
	}

	public void setR44_AMT_LESS_184_DAYS(BigDecimal r44_AMT_LESS_184_DAYS) {
		R44_AMT_LESS_184_DAYS = r44_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR44_AMT_MORE_184_DAYS() {
		return R44_AMT_MORE_184_DAYS;
	}

	public void setR44_AMT_MORE_184_DAYS(BigDecimal r44_AMT_MORE_184_DAYS) {
		R44_AMT_MORE_184_DAYS = r44_AMT_MORE_184_DAYS;
	}

	public String getR45_NAME_OF_BANK() {
		return R45_NAME_OF_BANK;
	}

	public void setR45_NAME_OF_BANK(String r45_NAME_OF_BANK) {
		R45_NAME_OF_BANK = r45_NAME_OF_BANK;
	}

	public String getR45_TYPE_OF_ACC() {
		return R45_TYPE_OF_ACC;
	}

	public void setR45_TYPE_OF_ACC(String r45_TYPE_OF_ACC) {
		R45_TYPE_OF_ACC = r45_TYPE_OF_ACC;
	}

	public String getR45_PURPOSE() {
		return R45_PURPOSE;
	}

	public void setR45_PURPOSE(String r45_PURPOSE) {
		R45_PURPOSE = r45_PURPOSE;
	}

	public String getR45_CURRENCY() {
		return R45_CURRENCY;
	}

	public void setR45_CURRENCY(String r45_CURRENCY) {
		R45_CURRENCY = r45_CURRENCY;
	}

	public String getR45_BANK_RATE() {
		return R45_BANK_RATE;
	}

	public void setR45_BANK_RATE(String r45_BANK_RATE) {
		R45_BANK_RATE = r45_BANK_RATE;
	}

	public BigDecimal getR45_AMT_LESS_184_DAYS() {
		return R45_AMT_LESS_184_DAYS;
	}

	public void setR45_AMT_LESS_184_DAYS(BigDecimal r45_AMT_LESS_184_DAYS) {
		R45_AMT_LESS_184_DAYS = r45_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR45_AMT_MORE_184_DAYS() {
		return R45_AMT_MORE_184_DAYS;
	}

	public void setR45_AMT_MORE_184_DAYS(BigDecimal r45_AMT_MORE_184_DAYS) {
		R45_AMT_MORE_184_DAYS = r45_AMT_MORE_184_DAYS;
	}

	public String getR46_NAME_OF_BANK() {
		return R46_NAME_OF_BANK;
	}

	public void setR46_NAME_OF_BANK(String r46_NAME_OF_BANK) {
		R46_NAME_OF_BANK = r46_NAME_OF_BANK;
	}

	public String getR46_TYPE_OF_ACC() {
		return R46_TYPE_OF_ACC;
	}

	public void setR46_TYPE_OF_ACC(String r46_TYPE_OF_ACC) {
		R46_TYPE_OF_ACC = r46_TYPE_OF_ACC;
	}

	public String getR46_PURPOSE() {
		return R46_PURPOSE;
	}

	public void setR46_PURPOSE(String r46_PURPOSE) {
		R46_PURPOSE = r46_PURPOSE;
	}

	public String getR46_CURRENCY() {
		return R46_CURRENCY;
	}

	public void setR46_CURRENCY(String r46_CURRENCY) {
		R46_CURRENCY = r46_CURRENCY;
	}

	public String getR46_BANK_RATE() {
		return R46_BANK_RATE;
	}

	public void setR46_BANK_RATE(String r46_BANK_RATE) {
		R46_BANK_RATE = r46_BANK_RATE;
	}

	public BigDecimal getR46_AMT_LESS_184_DAYS() {
		return R46_AMT_LESS_184_DAYS;
	}

	public void setR46_AMT_LESS_184_DAYS(BigDecimal r46_AMT_LESS_184_DAYS) {
		R46_AMT_LESS_184_DAYS = r46_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR46_AMT_MORE_184_DAYS() {
		return R46_AMT_MORE_184_DAYS;
	}

	public void setR46_AMT_MORE_184_DAYS(BigDecimal r46_AMT_MORE_184_DAYS) {
		R46_AMT_MORE_184_DAYS = r46_AMT_MORE_184_DAYS;
	}

	public String getR47_NAME_OF_BANK() {
		return R47_NAME_OF_BANK;
	}

	public void setR47_NAME_OF_BANK(String r47_NAME_OF_BANK) {
		R47_NAME_OF_BANK = r47_NAME_OF_BANK;
	}

	public String getR47_TYPE_OF_ACC() {
		return R47_TYPE_OF_ACC;
	}

	public void setR47_TYPE_OF_ACC(String r47_TYPE_OF_ACC) {
		R47_TYPE_OF_ACC = r47_TYPE_OF_ACC;
	}

	public String getR47_PURPOSE() {
		return R47_PURPOSE;
	}

	public void setR47_PURPOSE(String r47_PURPOSE) {
		R47_PURPOSE = r47_PURPOSE;
	}

	public String getR47_CURRENCY() {
		return R47_CURRENCY;
	}

	public void setR47_CURRENCY(String r47_CURRENCY) {
		R47_CURRENCY = r47_CURRENCY;
	}

	public String getR47_BANK_RATE() {
		return R47_BANK_RATE;
	}

	public void setR47_BANK_RATE(String r47_BANK_RATE) {
		R47_BANK_RATE = r47_BANK_RATE;
	}

	public BigDecimal getR47_AMT_LESS_184_DAYS() {
		return R47_AMT_LESS_184_DAYS;
	}

	public void setR47_AMT_LESS_184_DAYS(BigDecimal r47_AMT_LESS_184_DAYS) {
		R47_AMT_LESS_184_DAYS = r47_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR47_AMT_MORE_184_DAYS() {
		return R47_AMT_MORE_184_DAYS;
	}

	public void setR47_AMT_MORE_184_DAYS(BigDecimal r47_AMT_MORE_184_DAYS) {
		R47_AMT_MORE_184_DAYS = r47_AMT_MORE_184_DAYS;
	}

	public String getR48_NAME_OF_BANK() {
		return R48_NAME_OF_BANK;
	}

	public void setR48_NAME_OF_BANK(String r48_NAME_OF_BANK) {
		R48_NAME_OF_BANK = r48_NAME_OF_BANK;
	}

	public String getR48_TYPE_OF_ACC() {
		return R48_TYPE_OF_ACC;
	}

	public void setR48_TYPE_OF_ACC(String r48_TYPE_OF_ACC) {
		R48_TYPE_OF_ACC = r48_TYPE_OF_ACC;
	}

	public String getR48_PURPOSE() {
		return R48_PURPOSE;
	}

	public void setR48_PURPOSE(String r48_PURPOSE) {
		R48_PURPOSE = r48_PURPOSE;
	}

	public String getR48_CURRENCY() {
		return R48_CURRENCY;
	}

	public void setR48_CURRENCY(String r48_CURRENCY) {
		R48_CURRENCY = r48_CURRENCY;
	}

	public String getR48_BANK_RATE() {
		return R48_BANK_RATE;
	}

	public void setR48_BANK_RATE(String r48_BANK_RATE) {
		R48_BANK_RATE = r48_BANK_RATE;
	}

	public BigDecimal getR48_AMT_LESS_184_DAYS() {
		return R48_AMT_LESS_184_DAYS;
	}

	public void setR48_AMT_LESS_184_DAYS(BigDecimal r48_AMT_LESS_184_DAYS) {
		R48_AMT_LESS_184_DAYS = r48_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR48_AMT_MORE_184_DAYS() {
		return R48_AMT_MORE_184_DAYS;
	}

	public void setR48_AMT_MORE_184_DAYS(BigDecimal r48_AMT_MORE_184_DAYS) {
		R48_AMT_MORE_184_DAYS = r48_AMT_MORE_184_DAYS;
	}

	public String getR49_NAME_OF_BANK() {
		return R49_NAME_OF_BANK;
	}

	public void setR49_NAME_OF_BANK(String r49_NAME_OF_BANK) {
		R49_NAME_OF_BANK = r49_NAME_OF_BANK;
	}

	public String getR49_TYPE_OF_ACC() {
		return R49_TYPE_OF_ACC;
	}

	public void setR49_TYPE_OF_ACC(String r49_TYPE_OF_ACC) {
		R49_TYPE_OF_ACC = r49_TYPE_OF_ACC;
	}

	public String getR49_PURPOSE() {
		return R49_PURPOSE;
	}

	public void setR49_PURPOSE(String r49_PURPOSE) {
		R49_PURPOSE = r49_PURPOSE;
	}

	public String getR49_CURRENCY() {
		return R49_CURRENCY;
	}

	public void setR49_CURRENCY(String r49_CURRENCY) {
		R49_CURRENCY = r49_CURRENCY;
	}

	public String getR49_BANK_RATE() {
		return R49_BANK_RATE;
	}

	public void setR49_BANK_RATE(String r49_BANK_RATE) {
		R49_BANK_RATE = r49_BANK_RATE;
	}

	public BigDecimal getR49_AMT_LESS_184_DAYS() {
		return R49_AMT_LESS_184_DAYS;
	}

	public void setR49_AMT_LESS_184_DAYS(BigDecimal r49_AMT_LESS_184_DAYS) {
		R49_AMT_LESS_184_DAYS = r49_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR49_AMT_MORE_184_DAYS() {
		return R49_AMT_MORE_184_DAYS;
	}

	public void setR49_AMT_MORE_184_DAYS(BigDecimal r49_AMT_MORE_184_DAYS) {
		R49_AMT_MORE_184_DAYS = r49_AMT_MORE_184_DAYS;
	}

	public String getR50_NAME_OF_BANK() {
		return R50_NAME_OF_BANK;
	}

	public void setR50_NAME_OF_BANK(String r50_NAME_OF_BANK) {
		R50_NAME_OF_BANK = r50_NAME_OF_BANK;
	}

	public String getR50_TYPE_OF_ACC() {
		return R50_TYPE_OF_ACC;
	}

	public void setR50_TYPE_OF_ACC(String r50_TYPE_OF_ACC) {
		R50_TYPE_OF_ACC = r50_TYPE_OF_ACC;
	}

	public String getR50_PURPOSE() {
		return R50_PURPOSE;
	}

	public void setR50_PURPOSE(String r50_PURPOSE) {
		R50_PURPOSE = r50_PURPOSE;
	}

	public String getR50_CURRENCY() {
		return R50_CURRENCY;
	}

	public void setR50_CURRENCY(String r50_CURRENCY) {
		R50_CURRENCY = r50_CURRENCY;
	}

	public String getR50_BANK_RATE() {
		return R50_BANK_RATE;
	}

	public void setR50_BANK_RATE(String r50_BANK_RATE) {
		R50_BANK_RATE = r50_BANK_RATE;
	}

	public BigDecimal getR50_AMT_LESS_184_DAYS() {
		return R50_AMT_LESS_184_DAYS;
	}

	public void setR50_AMT_LESS_184_DAYS(BigDecimal r50_AMT_LESS_184_DAYS) {
		R50_AMT_LESS_184_DAYS = r50_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR50_AMT_MORE_184_DAYS() {
		return R50_AMT_MORE_184_DAYS;
	}

	public void setR50_AMT_MORE_184_DAYS(BigDecimal r50_AMT_MORE_184_DAYS) {
		R50_AMT_MORE_184_DAYS = r50_AMT_MORE_184_DAYS;
	}

	public BigDecimal getR51_TOT_AMT_LESS_184_DAYS() {
		return R51_TOT_AMT_LESS_184_DAYS;
	}

	public void setR51_TOT_AMT_LESS_184_DAYS(BigDecimal r51_TOT_AMT_LESS_184_DAYS) {
		R51_TOT_AMT_LESS_184_DAYS = r51_TOT_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR51_TOT_AMT_MORE_184_DAYS() {
		return R51_TOT_AMT_MORE_184_DAYS;
	}

	public void setR51_TOT_AMT_MORE_184_DAYS(BigDecimal r51_TOT_AMT_MORE_184_DAYS) {
		R51_TOT_AMT_MORE_184_DAYS = r51_TOT_AMT_MORE_184_DAYS;
	}

	public M_AIDP_Resub_Detail_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(M_AIDP_PK pk) {
		// TODO Auto-generated method stub

	}
}
