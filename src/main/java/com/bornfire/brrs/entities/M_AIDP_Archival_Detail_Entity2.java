package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_M_AIDP_ARCHIVALTABLE_DETAIL2")
@IdClass(M_AIDP_PK.class)
public class M_AIDP_Archival_Detail_Entity2 implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Date report_date;

	private BigDecimal report_version;
	private Date report_resubdate;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	private String R56_NAME_OF_BANK;
	private String R56_TYPE_OF_ACC;
	private String R56_PURPOSE;
	private String R56_CURRENCY;
	private String R56_BANK_RATE;
	private BigDecimal R56_AMT_LESS_184_DAYS;
	private BigDecimal R56_AMT_MORE_184_DAYS;
	private String R57_NAME_OF_BANK;
	private String R57_TYPE_OF_ACC;
	private String R57_PURPOSE;
	private String R57_CURRENCY;
	private String R57_BANK_RATE;
	private BigDecimal R57_AMT_LESS_184_DAYS;
	private BigDecimal R57_AMT_MORE_184_DAYS;
	private String R58_NAME_OF_BANK;
	private String R58_TYPE_OF_ACC;
	private String R58_PURPOSE;
	private String R58_CURRENCY;
	private String R58_BANK_RATE;
	private BigDecimal R58_AMT_LESS_184_DAYS;
	private BigDecimal R58_AMT_MORE_184_DAYS;
	private String R59_NAME_OF_BANK;
	private String R59_TYPE_OF_ACC;
	private String R59_PURPOSE;
	private String R59_CURRENCY;
	private String R59_BANK_RATE;
	private BigDecimal R59_AMT_LESS_184_DAYS;
	private BigDecimal R59_AMT_MORE_184_DAYS;
	private String R60_NAME_OF_BANK;
	private String R60_TYPE_OF_ACC;
	private String R60_PURPOSE;
	private String R60_CURRENCY;
	private String R60_BANK_RATE;
	private BigDecimal R60_AMT_LESS_184_DAYS;
	private BigDecimal R60_AMT_MORE_184_DAYS;
	private String R61_NAME_OF_BANK;
	private String R61_TYPE_OF_ACC;
	private String R61_PURPOSE;
	private String R61_CURRENCY;
	private String R61_BANK_RATE;
	private BigDecimal R61_AMT_LESS_184_DAYS;
	private BigDecimal R61_AMT_MORE_184_DAYS;
	private String R62_NAME_OF_BANK;
	private String R62_TYPE_OF_ACC;
	private String R62_PURPOSE;
	private String R62_CURRENCY;
	private String R62_BANK_RATE;
	private BigDecimal R62_AMT_LESS_184_DAYS;
	private BigDecimal R62_AMT_MORE_184_DAYS;
	private String R63_NAME_OF_BANK;
	private String R63_TYPE_OF_ACC;
	private String R63_PURPOSE;
	private String R63_CURRENCY;
	private String R63_BANK_RATE;
	private BigDecimal R63_AMT_LESS_184_DAYS;
	private BigDecimal R63_AMT_MORE_184_DAYS;
	private String R64_NAME_OF_BANK;
	private String R64_TYPE_OF_ACC;
	private String R64_PURPOSE;
	private String R64_CURRENCY;
	private String R64_BANK_RATE;
	private BigDecimal R64_AMT_LESS_184_DAYS;
	private BigDecimal R64_AMT_MORE_184_DAYS;
	private String R65_NAME_OF_BANK;
	private String R65_TYPE_OF_ACC;
	private String R65_PURPOSE;
	private String R65_CURRENCY;
	private String R65_BANK_RATE;
	private BigDecimal R65_AMT_LESS_184_DAYS;
	private BigDecimal R65_AMT_MORE_184_DAYS;
	private String R66_NAME_OF_BANK;
	private String R66_TYPE_OF_ACC;
	private String R66_PURPOSE;
	private String R66_CURRENCY;
	private String R66_BANK_RATE;
	private BigDecimal R66_AMT_LESS_184_DAYS;
	private BigDecimal R66_AMT_MORE_184_DAYS;
	private String R67_NAME_OF_BANK;
	private String R67_TYPE_OF_ACC;
	private String R67_PURPOSE;
	private String R67_CURRENCY;
	private String R67_BANK_RATE;
	private BigDecimal R67_AMT_LESS_184_DAYS;
	private BigDecimal R67_AMT_MORE_184_DAYS;
	private String R68_NAME_OF_BANK;
	private String R68_TYPE_OF_ACC;
	private String R68_PURPOSE;
	private String R68_CURRENCY;
	private String R68_BANK_RATE;
	private BigDecimal R68_AMT_LESS_184_DAYS;
	private BigDecimal R68_AMT_MORE_184_DAYS;
	private String R69_NAME_OF_BANK;
	private String R69_TYPE_OF_ACC;
	private String R69_PURPOSE;
	private String R69_CURRENCY;
	private String R69_BANK_RATE;
	private BigDecimal R69_AMT_LESS_184_DAYS;
	private BigDecimal R69_AMT_MORE_184_DAYS;
	private String R70_NAME_OF_BANK;
	private String R70_TYPE_OF_ACC;
	private String R70_PURPOSE;
	private String R70_CURRENCY;
	private String R70_BANK_RATE;
	private BigDecimal R70_AMT_LESS_184_DAYS;
	private BigDecimal R70_AMT_MORE_184_DAYS;
	private String R71_NAME_OF_BANK;
	private String R71_TYPE_OF_ACC;
	private String R71_PURPOSE;
	private String R71_CURRENCY;
	private String R71_BANK_RATE;
	private BigDecimal R71_AMT_LESS_184_DAYS;
	private BigDecimal R71_AMT_MORE_184_DAYS;
	private String R72_NAME_OF_BANK;
	private String R72_TYPE_OF_ACC;
	private String R72_PURPOSE;
	private String R72_CURRENCY;
	private String R72_BANK_RATE;
	private BigDecimal R72_AMT_LESS_184_DAYS;
	private BigDecimal R72_AMT_MORE_184_DAYS;
	private String R73_NAME_OF_BANK;
	private String R73_TYPE_OF_ACC;
	private String R73_PURPOSE;
	private String R73_CURRENCY;
	private String R73_BANK_RATE;
	private BigDecimal R73_AMT_LESS_184_DAYS;
	private BigDecimal R73_AMT_MORE_184_DAYS;
	private String R74_NAME_OF_BANK;
	private String R74_TYPE_OF_ACC;
	private String R74_PURPOSE;
	private String R74_CURRENCY;
	private String R74_BANK_RATE;
	private BigDecimal R74_AMT_LESS_184_DAYS;
	private BigDecimal R74_AMT_MORE_184_DAYS;
	private String R75_NAME_OF_BANK;
	private String R75_TYPE_OF_ACC;
	private String R75_PURPOSE;
	private String R75_CURRENCY;
	private String R75_BANK_RATE;
	private BigDecimal R75_AMT_LESS_184_DAYS;
	private BigDecimal R75_AMT_MORE_184_DAYS;
	private String R76_NAME_OF_BANK;
	private String R76_TYPE_OF_ACC;
	private String R76_PURPOSE;
	private String R76_CURRENCY;
	private String R76_BANK_RATE;
	private BigDecimal R76_AMT_LESS_184_DAYS;
	private BigDecimal R76_AMT_MORE_184_DAYS;
	private String R77_NAME_OF_BANK;
	private String R77_TYPE_OF_ACC;
	private String R77_PURPOSE;
	private String R77_CURRENCY;
	private String R77_BANK_RATE;
	private BigDecimal R77_AMT_LESS_184_DAYS;
	private BigDecimal R77_AMT_MORE_184_DAYS;
	private String R78_NAME_OF_BANK;
	private String R78_TYPE_OF_ACC;
	private String R78_PURPOSE;
	private String R78_CURRENCY;
	private String R78_BANK_RATE;
	private BigDecimal R78_AMT_LESS_184_DAYS;
	private BigDecimal R78_AMT_MORE_184_DAYS;
	private String R79_NAME_OF_BANK;
	private String R79_TYPE_OF_ACC;
	private String R79_PURPOSE;
	private String R79_CURRENCY;
	private String R79_BANK_RATE;
	private BigDecimal R79_AMT_LESS_184_DAYS;
	private BigDecimal R79_AMT_MORE_184_DAYS;
	private String R80_NAME_OF_BANK;
	private String R80_TYPE_OF_ACC;
	private String R80_PURPOSE;
	private String R80_CURRENCY;
	private String R80_BANK_RATE;
	private BigDecimal R80_AMT_LESS_184_DAYS;
	private BigDecimal R80_AMT_MORE_184_DAYS;
	private String R81_NAME_OF_BANK;
	private String R81_TYPE_OF_ACC;
	private String R81_PURPOSE;
	private String R81_CURRENCY;
	private String R81_BANK_RATE;
	private BigDecimal R81_AMT_LESS_184_DAYS;
	private BigDecimal R81_AMT_MORE_184_DAYS;
	private String R82_NAME_OF_BANK;
	private String R82_TYPE_OF_ACC;
	private String R82_PURPOSE;
	private String R82_CURRENCY;
	private String R82_BANK_RATE;
	private BigDecimal R82_AMT_LESS_184_DAYS;
	private BigDecimal R82_AMT_MORE_184_DAYS;
	private String R83_NAME_OF_BANK;
	private String R83_TYPE_OF_ACC;
	private String R83_PURPOSE;
	private String R83_CURRENCY;
	private String R83_BANK_RATE;
	private BigDecimal R83_AMT_LESS_184_DAYS;
	private BigDecimal R83_AMT_MORE_184_DAYS;
	private String R84_NAME_OF_BANK;
	private String R84_TYPE_OF_ACC;
	private String R84_PURPOSE;
	private String R84_CURRENCY;
	private String R84_BANK_RATE;
	private BigDecimal R84_AMT_LESS_184_DAYS;
	private BigDecimal R84_AMT_MORE_184_DAYS;
	private String R85_NAME_OF_BANK;
	private String R85_TYPE_OF_ACC;
	private String R85_PURPOSE;
	private String R85_CURRENCY;
	private String R85_BANK_RATE;
	private BigDecimal R85_AMT_LESS_184_DAYS;
	private BigDecimal R85_AMT_MORE_184_DAYS;
	private String R86_NAME_OF_BANK;
	private String R86_TYPE_OF_ACC;
	private String R86_PURPOSE;
	private String R86_CURRENCY;
	private String R86_BANK_RATE;
	private BigDecimal R86_AMT_LESS_184_DAYS;
	private BigDecimal R86_AMT_MORE_184_DAYS;
	private String R87_NAME_OF_BANK;
	private String R87_TYPE_OF_ACC;
	private String R87_PURPOSE;
	private String R87_CURRENCY;
	private String R87_BANK_RATE;
	private BigDecimal R87_AMT_LESS_184_DAYS;
	private BigDecimal R87_AMT_MORE_184_DAYS;
	private String R88_NAME_OF_BANK;
	private String R88_TYPE_OF_ACC;
	private String R88_PURPOSE;
	private String R88_CURRENCY;
	private String R88_BANK_RATE;
	private BigDecimal R88_AMT_LESS_184_DAYS;
	private BigDecimal R88_AMT_MORE_184_DAYS;
	private String R89_NAME_OF_BANK;
	private String R89_TYPE_OF_ACC;
	private String R89_PURPOSE;
	private String R89_CURRENCY;
	private String R89_BANK_RATE;
	private BigDecimal R89_AMT_LESS_184_DAYS;
	private BigDecimal R89_AMT_MORE_184_DAYS;
	private String R90_NAME_OF_BANK;
	private String R90_TYPE_OF_ACC;
	private String R90_PURPOSE;
	private String R90_CURRENCY;
	private String R90_BANK_RATE;
	private BigDecimal R90_AMT_LESS_184_DAYS;
	private BigDecimal R90_AMT_MORE_184_DAYS;
	private String R91_NAME_OF_BANK;
	private String R91_TYPE_OF_ACC;
	private String R91_PURPOSE;
	private String R91_CURRENCY;
	private String R91_BANK_RATE;
	private BigDecimal R91_AMT_LESS_184_DAYS;
	private BigDecimal R91_AMT_MORE_184_DAYS;
	private String R92_NAME_OF_BANK;
	private String R92_TYPE_OF_ACC;
	private String R92_PURPOSE;
	private String R92_CURRENCY;
	private String R92_BANK_RATE;
	private BigDecimal R92_AMT_LESS_184_DAYS;
	private BigDecimal R92_AMT_MORE_184_DAYS;
	private String R93_NAME_OF_BANK;
	private String R93_TYPE_OF_ACC;
	private String R93_PURPOSE;
	private String R93_CURRENCY;
	private String R93_BANK_RATE;
	private BigDecimal R93_AMT_LESS_184_DAYS;
	private BigDecimal R93_AMT_MORE_184_DAYS;
	private String R94_NAME_OF_BANK;
	private String R94_TYPE_OF_ACC;
	private String R94_PURPOSE;
	private String R94_CURRENCY;
	private String R94_BANK_RATE;
	private BigDecimal R94_AMT_LESS_184_DAYS;
	private BigDecimal R94_AMT_MORE_184_DAYS;
	private String R95_NAME_OF_BANK;
	private String R95_TYPE_OF_ACC;
	private String R95_PURPOSE;
	private String R95_CURRENCY;
	private String R95_BANK_RATE;
	private BigDecimal R95_AMT_LESS_184_DAYS;
	private BigDecimal R95_AMT_MORE_184_DAYS;
	private BigDecimal R96_TOT_AMT_LESS_184_DAYS;
	private BigDecimal R96_TOT_AMT_MORE_184_DAYS;

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

	public String getR56_NAME_OF_BANK() {
		return R56_NAME_OF_BANK;
	}

	public void setR56_NAME_OF_BANK(String r56_NAME_OF_BANK) {
		R56_NAME_OF_BANK = r56_NAME_OF_BANK;
	}

	public String getR56_TYPE_OF_ACC() {
		return R56_TYPE_OF_ACC;
	}

	public void setR56_TYPE_OF_ACC(String r56_TYPE_OF_ACC) {
		R56_TYPE_OF_ACC = r56_TYPE_OF_ACC;
	}

	public String getR56_PURPOSE() {
		return R56_PURPOSE;
	}

	public void setR56_PURPOSE(String r56_PURPOSE) {
		R56_PURPOSE = r56_PURPOSE;
	}

	public String getR56_CURRENCY() {
		return R56_CURRENCY;
	}

	public void setR56_CURRENCY(String r56_CURRENCY) {
		R56_CURRENCY = r56_CURRENCY;
	}

	public String getR56_BANK_RATE() {
		return R56_BANK_RATE;
	}

	public void setR56_BANK_RATE(String r56_BANK_RATE) {
		R56_BANK_RATE = r56_BANK_RATE;
	}

	public BigDecimal getR56_AMT_LESS_184_DAYS() {
		return R56_AMT_LESS_184_DAYS;
	}

	public void setR56_AMT_LESS_184_DAYS(BigDecimal r56_AMT_LESS_184_DAYS) {
		R56_AMT_LESS_184_DAYS = r56_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR56_AMT_MORE_184_DAYS() {
		return R56_AMT_MORE_184_DAYS;
	}

	public void setR56_AMT_MORE_184_DAYS(BigDecimal r56_AMT_MORE_184_DAYS) {
		R56_AMT_MORE_184_DAYS = r56_AMT_MORE_184_DAYS;
	}

	public String getR57_NAME_OF_BANK() {
		return R57_NAME_OF_BANK;
	}

	public void setR57_NAME_OF_BANK(String r57_NAME_OF_BANK) {
		R57_NAME_OF_BANK = r57_NAME_OF_BANK;
	}

	public String getR57_TYPE_OF_ACC() {
		return R57_TYPE_OF_ACC;
	}

	public void setR57_TYPE_OF_ACC(String r57_TYPE_OF_ACC) {
		R57_TYPE_OF_ACC = r57_TYPE_OF_ACC;
	}

	public String getR57_PURPOSE() {
		return R57_PURPOSE;
	}

	public void setR57_PURPOSE(String r57_PURPOSE) {
		R57_PURPOSE = r57_PURPOSE;
	}

	public String getR57_CURRENCY() {
		return R57_CURRENCY;
	}

	public void setR57_CURRENCY(String r57_CURRENCY) {
		R57_CURRENCY = r57_CURRENCY;
	}

	public String getR57_BANK_RATE() {
		return R57_BANK_RATE;
	}

	public void setR57_BANK_RATE(String r57_BANK_RATE) {
		R57_BANK_RATE = r57_BANK_RATE;
	}

	public BigDecimal getR57_AMT_LESS_184_DAYS() {
		return R57_AMT_LESS_184_DAYS;
	}

	public void setR57_AMT_LESS_184_DAYS(BigDecimal r57_AMT_LESS_184_DAYS) {
		R57_AMT_LESS_184_DAYS = r57_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR57_AMT_MORE_184_DAYS() {
		return R57_AMT_MORE_184_DAYS;
	}

	public void setR57_AMT_MORE_184_DAYS(BigDecimal r57_AMT_MORE_184_DAYS) {
		R57_AMT_MORE_184_DAYS = r57_AMT_MORE_184_DAYS;
	}

	public String getR58_NAME_OF_BANK() {
		return R58_NAME_OF_BANK;
	}

	public void setR58_NAME_OF_BANK(String r58_NAME_OF_BANK) {
		R58_NAME_OF_BANK = r58_NAME_OF_BANK;
	}

	public String getR58_TYPE_OF_ACC() {
		return R58_TYPE_OF_ACC;
	}

	public void setR58_TYPE_OF_ACC(String r58_TYPE_OF_ACC) {
		R58_TYPE_OF_ACC = r58_TYPE_OF_ACC;
	}

	public String getR58_PURPOSE() {
		return R58_PURPOSE;
	}

	public void setR58_PURPOSE(String r58_PURPOSE) {
		R58_PURPOSE = r58_PURPOSE;
	}

	public String getR58_CURRENCY() {
		return R58_CURRENCY;
	}

	public void setR58_CURRENCY(String r58_CURRENCY) {
		R58_CURRENCY = r58_CURRENCY;
	}

	public String getR58_BANK_RATE() {
		return R58_BANK_RATE;
	}

	public void setR58_BANK_RATE(String r58_BANK_RATE) {
		R58_BANK_RATE = r58_BANK_RATE;
	}

	public BigDecimal getR58_AMT_LESS_184_DAYS() {
		return R58_AMT_LESS_184_DAYS;
	}

	public void setR58_AMT_LESS_184_DAYS(BigDecimal r58_AMT_LESS_184_DAYS) {
		R58_AMT_LESS_184_DAYS = r58_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR58_AMT_MORE_184_DAYS() {
		return R58_AMT_MORE_184_DAYS;
	}

	public void setR58_AMT_MORE_184_DAYS(BigDecimal r58_AMT_MORE_184_DAYS) {
		R58_AMT_MORE_184_DAYS = r58_AMT_MORE_184_DAYS;
	}

	public String getR59_NAME_OF_BANK() {
		return R59_NAME_OF_BANK;
	}

	public void setR59_NAME_OF_BANK(String r59_NAME_OF_BANK) {
		R59_NAME_OF_BANK = r59_NAME_OF_BANK;
	}

	public String getR59_TYPE_OF_ACC() {
		return R59_TYPE_OF_ACC;
	}

	public void setR59_TYPE_OF_ACC(String r59_TYPE_OF_ACC) {
		R59_TYPE_OF_ACC = r59_TYPE_OF_ACC;
	}

	public String getR59_PURPOSE() {
		return R59_PURPOSE;
	}

	public void setR59_PURPOSE(String r59_PURPOSE) {
		R59_PURPOSE = r59_PURPOSE;
	}

	public String getR59_CURRENCY() {
		return R59_CURRENCY;
	}

	public void setR59_CURRENCY(String r59_CURRENCY) {
		R59_CURRENCY = r59_CURRENCY;
	}

	public String getR59_BANK_RATE() {
		return R59_BANK_RATE;
	}

	public void setR59_BANK_RATE(String r59_BANK_RATE) {
		R59_BANK_RATE = r59_BANK_RATE;
	}

	public BigDecimal getR59_AMT_LESS_184_DAYS() {
		return R59_AMT_LESS_184_DAYS;
	}

	public void setR59_AMT_LESS_184_DAYS(BigDecimal r59_AMT_LESS_184_DAYS) {
		R59_AMT_LESS_184_DAYS = r59_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR59_AMT_MORE_184_DAYS() {
		return R59_AMT_MORE_184_DAYS;
	}

	public void setR59_AMT_MORE_184_DAYS(BigDecimal r59_AMT_MORE_184_DAYS) {
		R59_AMT_MORE_184_DAYS = r59_AMT_MORE_184_DAYS;
	}

	public String getR60_NAME_OF_BANK() {
		return R60_NAME_OF_BANK;
	}

	public void setR60_NAME_OF_BANK(String r60_NAME_OF_BANK) {
		R60_NAME_OF_BANK = r60_NAME_OF_BANK;
	}

	public String getR60_TYPE_OF_ACC() {
		return R60_TYPE_OF_ACC;
	}

	public void setR60_TYPE_OF_ACC(String r60_TYPE_OF_ACC) {
		R60_TYPE_OF_ACC = r60_TYPE_OF_ACC;
	}

	public String getR60_PURPOSE() {
		return R60_PURPOSE;
	}

	public void setR60_PURPOSE(String r60_PURPOSE) {
		R60_PURPOSE = r60_PURPOSE;
	}

	public String getR60_CURRENCY() {
		return R60_CURRENCY;
	}

	public void setR60_CURRENCY(String r60_CURRENCY) {
		R60_CURRENCY = r60_CURRENCY;
	}

	public String getR60_BANK_RATE() {
		return R60_BANK_RATE;
	}

	public void setR60_BANK_RATE(String r60_BANK_RATE) {
		R60_BANK_RATE = r60_BANK_RATE;
	}

	public BigDecimal getR60_AMT_LESS_184_DAYS() {
		return R60_AMT_LESS_184_DAYS;
	}

	public void setR60_AMT_LESS_184_DAYS(BigDecimal r60_AMT_LESS_184_DAYS) {
		R60_AMT_LESS_184_DAYS = r60_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR60_AMT_MORE_184_DAYS() {
		return R60_AMT_MORE_184_DAYS;
	}

	public void setR60_AMT_MORE_184_DAYS(BigDecimal r60_AMT_MORE_184_DAYS) {
		R60_AMT_MORE_184_DAYS = r60_AMT_MORE_184_DAYS;
	}

	public String getR61_NAME_OF_BANK() {
		return R61_NAME_OF_BANK;
	}

	public void setR61_NAME_OF_BANK(String r61_NAME_OF_BANK) {
		R61_NAME_OF_BANK = r61_NAME_OF_BANK;
	}

	public String getR61_TYPE_OF_ACC() {
		return R61_TYPE_OF_ACC;
	}

	public void setR61_TYPE_OF_ACC(String r61_TYPE_OF_ACC) {
		R61_TYPE_OF_ACC = r61_TYPE_OF_ACC;
	}

	public String getR61_PURPOSE() {
		return R61_PURPOSE;
	}

	public void setR61_PURPOSE(String r61_PURPOSE) {
		R61_PURPOSE = r61_PURPOSE;
	}

	public String getR61_CURRENCY() {
		return R61_CURRENCY;
	}

	public void setR61_CURRENCY(String r61_CURRENCY) {
		R61_CURRENCY = r61_CURRENCY;
	}

	public String getR61_BANK_RATE() {
		return R61_BANK_RATE;
	}

	public void setR61_BANK_RATE(String r61_BANK_RATE) {
		R61_BANK_RATE = r61_BANK_RATE;
	}

	public BigDecimal getR61_AMT_LESS_184_DAYS() {
		return R61_AMT_LESS_184_DAYS;
	}

	public void setR61_AMT_LESS_184_DAYS(BigDecimal r61_AMT_LESS_184_DAYS) {
		R61_AMT_LESS_184_DAYS = r61_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR61_AMT_MORE_184_DAYS() {
		return R61_AMT_MORE_184_DAYS;
	}

	public void setR61_AMT_MORE_184_DAYS(BigDecimal r61_AMT_MORE_184_DAYS) {
		R61_AMT_MORE_184_DAYS = r61_AMT_MORE_184_DAYS;
	}

	public String getR62_NAME_OF_BANK() {
		return R62_NAME_OF_BANK;
	}

	public void setR62_NAME_OF_BANK(String r62_NAME_OF_BANK) {
		R62_NAME_OF_BANK = r62_NAME_OF_BANK;
	}

	public String getR62_TYPE_OF_ACC() {
		return R62_TYPE_OF_ACC;
	}

	public void setR62_TYPE_OF_ACC(String r62_TYPE_OF_ACC) {
		R62_TYPE_OF_ACC = r62_TYPE_OF_ACC;
	}

	public String getR62_PURPOSE() {
		return R62_PURPOSE;
	}

	public void setR62_PURPOSE(String r62_PURPOSE) {
		R62_PURPOSE = r62_PURPOSE;
	}

	public String getR62_CURRENCY() {
		return R62_CURRENCY;
	}

	public void setR62_CURRENCY(String r62_CURRENCY) {
		R62_CURRENCY = r62_CURRENCY;
	}

	public String getR62_BANK_RATE() {
		return R62_BANK_RATE;
	}

	public void setR62_BANK_RATE(String r62_BANK_RATE) {
		R62_BANK_RATE = r62_BANK_RATE;
	}

	public BigDecimal getR62_AMT_LESS_184_DAYS() {
		return R62_AMT_LESS_184_DAYS;
	}

	public void setR62_AMT_LESS_184_DAYS(BigDecimal r62_AMT_LESS_184_DAYS) {
		R62_AMT_LESS_184_DAYS = r62_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR62_AMT_MORE_184_DAYS() {
		return R62_AMT_MORE_184_DAYS;
	}

	public void setR62_AMT_MORE_184_DAYS(BigDecimal r62_AMT_MORE_184_DAYS) {
		R62_AMT_MORE_184_DAYS = r62_AMT_MORE_184_DAYS;
	}

	public String getR63_NAME_OF_BANK() {
		return R63_NAME_OF_BANK;
	}

	public void setR63_NAME_OF_BANK(String r63_NAME_OF_BANK) {
		R63_NAME_OF_BANK = r63_NAME_OF_BANK;
	}

	public String getR63_TYPE_OF_ACC() {
		return R63_TYPE_OF_ACC;
	}

	public void setR63_TYPE_OF_ACC(String r63_TYPE_OF_ACC) {
		R63_TYPE_OF_ACC = r63_TYPE_OF_ACC;
	}

	public String getR63_PURPOSE() {
		return R63_PURPOSE;
	}

	public void setR63_PURPOSE(String r63_PURPOSE) {
		R63_PURPOSE = r63_PURPOSE;
	}

	public String getR63_CURRENCY() {
		return R63_CURRENCY;
	}

	public void setR63_CURRENCY(String r63_CURRENCY) {
		R63_CURRENCY = r63_CURRENCY;
	}

	public String getR63_BANK_RATE() {
		return R63_BANK_RATE;
	}

	public void setR63_BANK_RATE(String r63_BANK_RATE) {
		R63_BANK_RATE = r63_BANK_RATE;
	}

	public BigDecimal getR63_AMT_LESS_184_DAYS() {
		return R63_AMT_LESS_184_DAYS;
	}

	public void setR63_AMT_LESS_184_DAYS(BigDecimal r63_AMT_LESS_184_DAYS) {
		R63_AMT_LESS_184_DAYS = r63_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR63_AMT_MORE_184_DAYS() {
		return R63_AMT_MORE_184_DAYS;
	}

	public void setR63_AMT_MORE_184_DAYS(BigDecimal r63_AMT_MORE_184_DAYS) {
		R63_AMT_MORE_184_DAYS = r63_AMT_MORE_184_DAYS;
	}

	public String getR64_NAME_OF_BANK() {
		return R64_NAME_OF_BANK;
	}

	public void setR64_NAME_OF_BANK(String r64_NAME_OF_BANK) {
		R64_NAME_OF_BANK = r64_NAME_OF_BANK;
	}

	public String getR64_TYPE_OF_ACC() {
		return R64_TYPE_OF_ACC;
	}

	public void setR64_TYPE_OF_ACC(String r64_TYPE_OF_ACC) {
		R64_TYPE_OF_ACC = r64_TYPE_OF_ACC;
	}

	public String getR64_PURPOSE() {
		return R64_PURPOSE;
	}

	public void setR64_PURPOSE(String r64_PURPOSE) {
		R64_PURPOSE = r64_PURPOSE;
	}

	public String getR64_CURRENCY() {
		return R64_CURRENCY;
	}

	public void setR64_CURRENCY(String r64_CURRENCY) {
		R64_CURRENCY = r64_CURRENCY;
	}

	public String getR64_BANK_RATE() {
		return R64_BANK_RATE;
	}

	public void setR64_BANK_RATE(String r64_BANK_RATE) {
		R64_BANK_RATE = r64_BANK_RATE;
	}

	public BigDecimal getR64_AMT_LESS_184_DAYS() {
		return R64_AMT_LESS_184_DAYS;
	}

	public void setR64_AMT_LESS_184_DAYS(BigDecimal r64_AMT_LESS_184_DAYS) {
		R64_AMT_LESS_184_DAYS = r64_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR64_AMT_MORE_184_DAYS() {
		return R64_AMT_MORE_184_DAYS;
	}

	public void setR64_AMT_MORE_184_DAYS(BigDecimal r64_AMT_MORE_184_DAYS) {
		R64_AMT_MORE_184_DAYS = r64_AMT_MORE_184_DAYS;
	}

	public String getR65_NAME_OF_BANK() {
		return R65_NAME_OF_BANK;
	}

	public void setR65_NAME_OF_BANK(String r65_NAME_OF_BANK) {
		R65_NAME_OF_BANK = r65_NAME_OF_BANK;
	}

	public String getR65_TYPE_OF_ACC() {
		return R65_TYPE_OF_ACC;
	}

	public void setR65_TYPE_OF_ACC(String r65_TYPE_OF_ACC) {
		R65_TYPE_OF_ACC = r65_TYPE_OF_ACC;
	}

	public String getR65_PURPOSE() {
		return R65_PURPOSE;
	}

	public void setR65_PURPOSE(String r65_PURPOSE) {
		R65_PURPOSE = r65_PURPOSE;
	}

	public String getR65_CURRENCY() {
		return R65_CURRENCY;
	}

	public void setR65_CURRENCY(String r65_CURRENCY) {
		R65_CURRENCY = r65_CURRENCY;
	}

	public String getR65_BANK_RATE() {
		return R65_BANK_RATE;
	}

	public void setR65_BANK_RATE(String r65_BANK_RATE) {
		R65_BANK_RATE = r65_BANK_RATE;
	}

	public BigDecimal getR65_AMT_LESS_184_DAYS() {
		return R65_AMT_LESS_184_DAYS;
	}

	public void setR65_AMT_LESS_184_DAYS(BigDecimal r65_AMT_LESS_184_DAYS) {
		R65_AMT_LESS_184_DAYS = r65_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR65_AMT_MORE_184_DAYS() {
		return R65_AMT_MORE_184_DAYS;
	}

	public void setR65_AMT_MORE_184_DAYS(BigDecimal r65_AMT_MORE_184_DAYS) {
		R65_AMT_MORE_184_DAYS = r65_AMT_MORE_184_DAYS;
	}

	public String getR66_NAME_OF_BANK() {
		return R66_NAME_OF_BANK;
	}

	public void setR66_NAME_OF_BANK(String r66_NAME_OF_BANK) {
		R66_NAME_OF_BANK = r66_NAME_OF_BANK;
	}

	public String getR66_TYPE_OF_ACC() {
		return R66_TYPE_OF_ACC;
	}

	public void setR66_TYPE_OF_ACC(String r66_TYPE_OF_ACC) {
		R66_TYPE_OF_ACC = r66_TYPE_OF_ACC;
	}

	public String getR66_PURPOSE() {
		return R66_PURPOSE;
	}

	public void setR66_PURPOSE(String r66_PURPOSE) {
		R66_PURPOSE = r66_PURPOSE;
	}

	public String getR66_CURRENCY() {
		return R66_CURRENCY;
	}

	public void setR66_CURRENCY(String r66_CURRENCY) {
		R66_CURRENCY = r66_CURRENCY;
	}

	public String getR66_BANK_RATE() {
		return R66_BANK_RATE;
	}

	public void setR66_BANK_RATE(String r66_BANK_RATE) {
		R66_BANK_RATE = r66_BANK_RATE;
	}

	public BigDecimal getR66_AMT_LESS_184_DAYS() {
		return R66_AMT_LESS_184_DAYS;
	}

	public void setR66_AMT_LESS_184_DAYS(BigDecimal r66_AMT_LESS_184_DAYS) {
		R66_AMT_LESS_184_DAYS = r66_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR66_AMT_MORE_184_DAYS() {
		return R66_AMT_MORE_184_DAYS;
	}

	public void setR66_AMT_MORE_184_DAYS(BigDecimal r66_AMT_MORE_184_DAYS) {
		R66_AMT_MORE_184_DAYS = r66_AMT_MORE_184_DAYS;
	}

	public String getR67_NAME_OF_BANK() {
		return R67_NAME_OF_BANK;
	}

	public void setR67_NAME_OF_BANK(String r67_NAME_OF_BANK) {
		R67_NAME_OF_BANK = r67_NAME_OF_BANK;
	}

	public String getR67_TYPE_OF_ACC() {
		return R67_TYPE_OF_ACC;
	}

	public void setR67_TYPE_OF_ACC(String r67_TYPE_OF_ACC) {
		R67_TYPE_OF_ACC = r67_TYPE_OF_ACC;
	}

	public String getR67_PURPOSE() {
		return R67_PURPOSE;
	}

	public void setR67_PURPOSE(String r67_PURPOSE) {
		R67_PURPOSE = r67_PURPOSE;
	}

	public String getR67_CURRENCY() {
		return R67_CURRENCY;
	}

	public void setR67_CURRENCY(String r67_CURRENCY) {
		R67_CURRENCY = r67_CURRENCY;
	}

	public String getR67_BANK_RATE() {
		return R67_BANK_RATE;
	}

	public void setR67_BANK_RATE(String r67_BANK_RATE) {
		R67_BANK_RATE = r67_BANK_RATE;
	}

	public BigDecimal getR67_AMT_LESS_184_DAYS() {
		return R67_AMT_LESS_184_DAYS;
	}

	public void setR67_AMT_LESS_184_DAYS(BigDecimal r67_AMT_LESS_184_DAYS) {
		R67_AMT_LESS_184_DAYS = r67_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR67_AMT_MORE_184_DAYS() {
		return R67_AMT_MORE_184_DAYS;
	}

	public void setR67_AMT_MORE_184_DAYS(BigDecimal r67_AMT_MORE_184_DAYS) {
		R67_AMT_MORE_184_DAYS = r67_AMT_MORE_184_DAYS;
	}

	public String getR68_NAME_OF_BANK() {
		return R68_NAME_OF_BANK;
	}

	public void setR68_NAME_OF_BANK(String r68_NAME_OF_BANK) {
		R68_NAME_OF_BANK = r68_NAME_OF_BANK;
	}

	public String getR68_TYPE_OF_ACC() {
		return R68_TYPE_OF_ACC;
	}

	public void setR68_TYPE_OF_ACC(String r68_TYPE_OF_ACC) {
		R68_TYPE_OF_ACC = r68_TYPE_OF_ACC;
	}

	public String getR68_PURPOSE() {
		return R68_PURPOSE;
	}

	public void setR68_PURPOSE(String r68_PURPOSE) {
		R68_PURPOSE = r68_PURPOSE;
	}

	public String getR68_CURRENCY() {
		return R68_CURRENCY;
	}

	public void setR68_CURRENCY(String r68_CURRENCY) {
		R68_CURRENCY = r68_CURRENCY;
	}

	public String getR68_BANK_RATE() {
		return R68_BANK_RATE;
	}

	public void setR68_BANK_RATE(String r68_BANK_RATE) {
		R68_BANK_RATE = r68_BANK_RATE;
	}

	public BigDecimal getR68_AMT_LESS_184_DAYS() {
		return R68_AMT_LESS_184_DAYS;
	}

	public void setR68_AMT_LESS_184_DAYS(BigDecimal r68_AMT_LESS_184_DAYS) {
		R68_AMT_LESS_184_DAYS = r68_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR68_AMT_MORE_184_DAYS() {
		return R68_AMT_MORE_184_DAYS;
	}

	public void setR68_AMT_MORE_184_DAYS(BigDecimal r68_AMT_MORE_184_DAYS) {
		R68_AMT_MORE_184_DAYS = r68_AMT_MORE_184_DAYS;
	}

	public String getR69_NAME_OF_BANK() {
		return R69_NAME_OF_BANK;
	}

	public void setR69_NAME_OF_BANK(String r69_NAME_OF_BANK) {
		R69_NAME_OF_BANK = r69_NAME_OF_BANK;
	}

	public String getR69_TYPE_OF_ACC() {
		return R69_TYPE_OF_ACC;
	}

	public void setR69_TYPE_OF_ACC(String r69_TYPE_OF_ACC) {
		R69_TYPE_OF_ACC = r69_TYPE_OF_ACC;
	}

	public String getR69_PURPOSE() {
		return R69_PURPOSE;
	}

	public void setR69_PURPOSE(String r69_PURPOSE) {
		R69_PURPOSE = r69_PURPOSE;
	}

	public String getR69_CURRENCY() {
		return R69_CURRENCY;
	}

	public void setR69_CURRENCY(String r69_CURRENCY) {
		R69_CURRENCY = r69_CURRENCY;
	}

	public String getR69_BANK_RATE() {
		return R69_BANK_RATE;
	}

	public void setR69_BANK_RATE(String r69_BANK_RATE) {
		R69_BANK_RATE = r69_BANK_RATE;
	}

	public BigDecimal getR69_AMT_LESS_184_DAYS() {
		return R69_AMT_LESS_184_DAYS;
	}

	public void setR69_AMT_LESS_184_DAYS(BigDecimal r69_AMT_LESS_184_DAYS) {
		R69_AMT_LESS_184_DAYS = r69_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR69_AMT_MORE_184_DAYS() {
		return R69_AMT_MORE_184_DAYS;
	}

	public void setR69_AMT_MORE_184_DAYS(BigDecimal r69_AMT_MORE_184_DAYS) {
		R69_AMT_MORE_184_DAYS = r69_AMT_MORE_184_DAYS;
	}

	public String getR70_NAME_OF_BANK() {
		return R70_NAME_OF_BANK;
	}

	public void setR70_NAME_OF_BANK(String r70_NAME_OF_BANK) {
		R70_NAME_OF_BANK = r70_NAME_OF_BANK;
	}

	public String getR70_TYPE_OF_ACC() {
		return R70_TYPE_OF_ACC;
	}

	public void setR70_TYPE_OF_ACC(String r70_TYPE_OF_ACC) {
		R70_TYPE_OF_ACC = r70_TYPE_OF_ACC;
	}

	public String getR70_PURPOSE() {
		return R70_PURPOSE;
	}

	public void setR70_PURPOSE(String r70_PURPOSE) {
		R70_PURPOSE = r70_PURPOSE;
	}

	public String getR70_CURRENCY() {
		return R70_CURRENCY;
	}

	public void setR70_CURRENCY(String r70_CURRENCY) {
		R70_CURRENCY = r70_CURRENCY;
	}

	public String getR70_BANK_RATE() {
		return R70_BANK_RATE;
	}

	public void setR70_BANK_RATE(String r70_BANK_RATE) {
		R70_BANK_RATE = r70_BANK_RATE;
	}

	public BigDecimal getR70_AMT_LESS_184_DAYS() {
		return R70_AMT_LESS_184_DAYS;
	}

	public void setR70_AMT_LESS_184_DAYS(BigDecimal r70_AMT_LESS_184_DAYS) {
		R70_AMT_LESS_184_DAYS = r70_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR70_AMT_MORE_184_DAYS() {
		return R70_AMT_MORE_184_DAYS;
	}

	public void setR70_AMT_MORE_184_DAYS(BigDecimal r70_AMT_MORE_184_DAYS) {
		R70_AMT_MORE_184_DAYS = r70_AMT_MORE_184_DAYS;
	}

	public String getR71_NAME_OF_BANK() {
		return R71_NAME_OF_BANK;
	}

	public void setR71_NAME_OF_BANK(String r71_NAME_OF_BANK) {
		R71_NAME_OF_BANK = r71_NAME_OF_BANK;
	}

	public String getR71_TYPE_OF_ACC() {
		return R71_TYPE_OF_ACC;
	}

	public void setR71_TYPE_OF_ACC(String r71_TYPE_OF_ACC) {
		R71_TYPE_OF_ACC = r71_TYPE_OF_ACC;
	}

	public String getR71_PURPOSE() {
		return R71_PURPOSE;
	}

	public void setR71_PURPOSE(String r71_PURPOSE) {
		R71_PURPOSE = r71_PURPOSE;
	}

	public String getR71_CURRENCY() {
		return R71_CURRENCY;
	}

	public void setR71_CURRENCY(String r71_CURRENCY) {
		R71_CURRENCY = r71_CURRENCY;
	}

	public String getR71_BANK_RATE() {
		return R71_BANK_RATE;
	}

	public void setR71_BANK_RATE(String r71_BANK_RATE) {
		R71_BANK_RATE = r71_BANK_RATE;
	}

	public BigDecimal getR71_AMT_LESS_184_DAYS() {
		return R71_AMT_LESS_184_DAYS;
	}

	public void setR71_AMT_LESS_184_DAYS(BigDecimal r71_AMT_LESS_184_DAYS) {
		R71_AMT_LESS_184_DAYS = r71_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR71_AMT_MORE_184_DAYS() {
		return R71_AMT_MORE_184_DAYS;
	}

	public void setR71_AMT_MORE_184_DAYS(BigDecimal r71_AMT_MORE_184_DAYS) {
		R71_AMT_MORE_184_DAYS = r71_AMT_MORE_184_DAYS;
	}

	public String getR72_NAME_OF_BANK() {
		return R72_NAME_OF_BANK;
	}

	public void setR72_NAME_OF_BANK(String r72_NAME_OF_BANK) {
		R72_NAME_OF_BANK = r72_NAME_OF_BANK;
	}

	public String getR72_TYPE_OF_ACC() {
		return R72_TYPE_OF_ACC;
	}

	public void setR72_TYPE_OF_ACC(String r72_TYPE_OF_ACC) {
		R72_TYPE_OF_ACC = r72_TYPE_OF_ACC;
	}

	public String getR72_PURPOSE() {
		return R72_PURPOSE;
	}

	public void setR72_PURPOSE(String r72_PURPOSE) {
		R72_PURPOSE = r72_PURPOSE;
	}

	public String getR72_CURRENCY() {
		return R72_CURRENCY;
	}

	public void setR72_CURRENCY(String r72_CURRENCY) {
		R72_CURRENCY = r72_CURRENCY;
	}

	public String getR72_BANK_RATE() {
		return R72_BANK_RATE;
	}

	public void setR72_BANK_RATE(String r72_BANK_RATE) {
		R72_BANK_RATE = r72_BANK_RATE;
	}

	public BigDecimal getR72_AMT_LESS_184_DAYS() {
		return R72_AMT_LESS_184_DAYS;
	}

	public void setR72_AMT_LESS_184_DAYS(BigDecimal r72_AMT_LESS_184_DAYS) {
		R72_AMT_LESS_184_DAYS = r72_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR72_AMT_MORE_184_DAYS() {
		return R72_AMT_MORE_184_DAYS;
	}

	public void setR72_AMT_MORE_184_DAYS(BigDecimal r72_AMT_MORE_184_DAYS) {
		R72_AMT_MORE_184_DAYS = r72_AMT_MORE_184_DAYS;
	}

	public String getR73_NAME_OF_BANK() {
		return R73_NAME_OF_BANK;
	}

	public void setR73_NAME_OF_BANK(String r73_NAME_OF_BANK) {
		R73_NAME_OF_BANK = r73_NAME_OF_BANK;
	}

	public String getR73_TYPE_OF_ACC() {
		return R73_TYPE_OF_ACC;
	}

	public void setR73_TYPE_OF_ACC(String r73_TYPE_OF_ACC) {
		R73_TYPE_OF_ACC = r73_TYPE_OF_ACC;
	}

	public String getR73_PURPOSE() {
		return R73_PURPOSE;
	}

	public void setR73_PURPOSE(String r73_PURPOSE) {
		R73_PURPOSE = r73_PURPOSE;
	}

	public String getR73_CURRENCY() {
		return R73_CURRENCY;
	}

	public void setR73_CURRENCY(String r73_CURRENCY) {
		R73_CURRENCY = r73_CURRENCY;
	}

	public String getR73_BANK_RATE() {
		return R73_BANK_RATE;
	}

	public void setR73_BANK_RATE(String r73_BANK_RATE) {
		R73_BANK_RATE = r73_BANK_RATE;
	}

	public BigDecimal getR73_AMT_LESS_184_DAYS() {
		return R73_AMT_LESS_184_DAYS;
	}

	public void setR73_AMT_LESS_184_DAYS(BigDecimal r73_AMT_LESS_184_DAYS) {
		R73_AMT_LESS_184_DAYS = r73_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR73_AMT_MORE_184_DAYS() {
		return R73_AMT_MORE_184_DAYS;
	}

	public void setR73_AMT_MORE_184_DAYS(BigDecimal r73_AMT_MORE_184_DAYS) {
		R73_AMT_MORE_184_DAYS = r73_AMT_MORE_184_DAYS;
	}

	public String getR74_NAME_OF_BANK() {
		return R74_NAME_OF_BANK;
	}

	public void setR74_NAME_OF_BANK(String r74_NAME_OF_BANK) {
		R74_NAME_OF_BANK = r74_NAME_OF_BANK;
	}

	public String getR74_TYPE_OF_ACC() {
		return R74_TYPE_OF_ACC;
	}

	public void setR74_TYPE_OF_ACC(String r74_TYPE_OF_ACC) {
		R74_TYPE_OF_ACC = r74_TYPE_OF_ACC;
	}

	public String getR74_PURPOSE() {
		return R74_PURPOSE;
	}

	public void setR74_PURPOSE(String r74_PURPOSE) {
		R74_PURPOSE = r74_PURPOSE;
	}

	public String getR74_CURRENCY() {
		return R74_CURRENCY;
	}

	public void setR74_CURRENCY(String r74_CURRENCY) {
		R74_CURRENCY = r74_CURRENCY;
	}

	public String getR74_BANK_RATE() {
		return R74_BANK_RATE;
	}

	public void setR74_BANK_RATE(String r74_BANK_RATE) {
		R74_BANK_RATE = r74_BANK_RATE;
	}

	public BigDecimal getR74_AMT_LESS_184_DAYS() {
		return R74_AMT_LESS_184_DAYS;
	}

	public void setR74_AMT_LESS_184_DAYS(BigDecimal r74_AMT_LESS_184_DAYS) {
		R74_AMT_LESS_184_DAYS = r74_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR74_AMT_MORE_184_DAYS() {
		return R74_AMT_MORE_184_DAYS;
	}

	public void setR74_AMT_MORE_184_DAYS(BigDecimal r74_AMT_MORE_184_DAYS) {
		R74_AMT_MORE_184_DAYS = r74_AMT_MORE_184_DAYS;
	}

	public String getR75_NAME_OF_BANK() {
		return R75_NAME_OF_BANK;
	}

	public void setR75_NAME_OF_BANK(String r75_NAME_OF_BANK) {
		R75_NAME_OF_BANK = r75_NAME_OF_BANK;
	}

	public String getR75_TYPE_OF_ACC() {
		return R75_TYPE_OF_ACC;
	}

	public void setR75_TYPE_OF_ACC(String r75_TYPE_OF_ACC) {
		R75_TYPE_OF_ACC = r75_TYPE_OF_ACC;
	}

	public String getR75_PURPOSE() {
		return R75_PURPOSE;
	}

	public void setR75_PURPOSE(String r75_PURPOSE) {
		R75_PURPOSE = r75_PURPOSE;
	}

	public String getR75_CURRENCY() {
		return R75_CURRENCY;
	}

	public void setR75_CURRENCY(String r75_CURRENCY) {
		R75_CURRENCY = r75_CURRENCY;
	}

	public String getR75_BANK_RATE() {
		return R75_BANK_RATE;
	}

	public void setR75_BANK_RATE(String r75_BANK_RATE) {
		R75_BANK_RATE = r75_BANK_RATE;
	}

	public BigDecimal getR75_AMT_LESS_184_DAYS() {
		return R75_AMT_LESS_184_DAYS;
	}

	public void setR75_AMT_LESS_184_DAYS(BigDecimal r75_AMT_LESS_184_DAYS) {
		R75_AMT_LESS_184_DAYS = r75_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR75_AMT_MORE_184_DAYS() {
		return R75_AMT_MORE_184_DAYS;
	}

	public void setR75_AMT_MORE_184_DAYS(BigDecimal r75_AMT_MORE_184_DAYS) {
		R75_AMT_MORE_184_DAYS = r75_AMT_MORE_184_DAYS;
	}

	public String getR76_NAME_OF_BANK() {
		return R76_NAME_OF_BANK;
	}

	public void setR76_NAME_OF_BANK(String r76_NAME_OF_BANK) {
		R76_NAME_OF_BANK = r76_NAME_OF_BANK;
	}

	public String getR76_TYPE_OF_ACC() {
		return R76_TYPE_OF_ACC;
	}

	public void setR76_TYPE_OF_ACC(String r76_TYPE_OF_ACC) {
		R76_TYPE_OF_ACC = r76_TYPE_OF_ACC;
	}

	public String getR76_PURPOSE() {
		return R76_PURPOSE;
	}

	public void setR76_PURPOSE(String r76_PURPOSE) {
		R76_PURPOSE = r76_PURPOSE;
	}

	public String getR76_CURRENCY() {
		return R76_CURRENCY;
	}

	public void setR76_CURRENCY(String r76_CURRENCY) {
		R76_CURRENCY = r76_CURRENCY;
	}

	public String getR76_BANK_RATE() {
		return R76_BANK_RATE;
	}

	public void setR76_BANK_RATE(String r76_BANK_RATE) {
		R76_BANK_RATE = r76_BANK_RATE;
	}

	public BigDecimal getR76_AMT_LESS_184_DAYS() {
		return R76_AMT_LESS_184_DAYS;
	}

	public void setR76_AMT_LESS_184_DAYS(BigDecimal r76_AMT_LESS_184_DAYS) {
		R76_AMT_LESS_184_DAYS = r76_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR76_AMT_MORE_184_DAYS() {
		return R76_AMT_MORE_184_DAYS;
	}

	public void setR76_AMT_MORE_184_DAYS(BigDecimal r76_AMT_MORE_184_DAYS) {
		R76_AMT_MORE_184_DAYS = r76_AMT_MORE_184_DAYS;
	}

	public String getR77_NAME_OF_BANK() {
		return R77_NAME_OF_BANK;
	}

	public void setR77_NAME_OF_BANK(String r77_NAME_OF_BANK) {
		R77_NAME_OF_BANK = r77_NAME_OF_BANK;
	}

	public String getR77_TYPE_OF_ACC() {
		return R77_TYPE_OF_ACC;
	}

	public void setR77_TYPE_OF_ACC(String r77_TYPE_OF_ACC) {
		R77_TYPE_OF_ACC = r77_TYPE_OF_ACC;
	}

	public String getR77_PURPOSE() {
		return R77_PURPOSE;
	}

	public void setR77_PURPOSE(String r77_PURPOSE) {
		R77_PURPOSE = r77_PURPOSE;
	}

	public String getR77_CURRENCY() {
		return R77_CURRENCY;
	}

	public void setR77_CURRENCY(String r77_CURRENCY) {
		R77_CURRENCY = r77_CURRENCY;
	}

	public String getR77_BANK_RATE() {
		return R77_BANK_RATE;
	}

	public void setR77_BANK_RATE(String r77_BANK_RATE) {
		R77_BANK_RATE = r77_BANK_RATE;
	}

	public BigDecimal getR77_AMT_LESS_184_DAYS() {
		return R77_AMT_LESS_184_DAYS;
	}

	public void setR77_AMT_LESS_184_DAYS(BigDecimal r77_AMT_LESS_184_DAYS) {
		R77_AMT_LESS_184_DAYS = r77_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR77_AMT_MORE_184_DAYS() {
		return R77_AMT_MORE_184_DAYS;
	}

	public void setR77_AMT_MORE_184_DAYS(BigDecimal r77_AMT_MORE_184_DAYS) {
		R77_AMT_MORE_184_DAYS = r77_AMT_MORE_184_DAYS;
	}

	public String getR78_NAME_OF_BANK() {
		return R78_NAME_OF_BANK;
	}

	public void setR78_NAME_OF_BANK(String r78_NAME_OF_BANK) {
		R78_NAME_OF_BANK = r78_NAME_OF_BANK;
	}

	public String getR78_TYPE_OF_ACC() {
		return R78_TYPE_OF_ACC;
	}

	public void setR78_TYPE_OF_ACC(String r78_TYPE_OF_ACC) {
		R78_TYPE_OF_ACC = r78_TYPE_OF_ACC;
	}

	public String getR78_PURPOSE() {
		return R78_PURPOSE;
	}

	public void setR78_PURPOSE(String r78_PURPOSE) {
		R78_PURPOSE = r78_PURPOSE;
	}

	public String getR78_CURRENCY() {
		return R78_CURRENCY;
	}

	public void setR78_CURRENCY(String r78_CURRENCY) {
		R78_CURRENCY = r78_CURRENCY;
	}

	public String getR78_BANK_RATE() {
		return R78_BANK_RATE;
	}

	public void setR78_BANK_RATE(String r78_BANK_RATE) {
		R78_BANK_RATE = r78_BANK_RATE;
	}

	public BigDecimal getR78_AMT_LESS_184_DAYS() {
		return R78_AMT_LESS_184_DAYS;
	}

	public void setR78_AMT_LESS_184_DAYS(BigDecimal r78_AMT_LESS_184_DAYS) {
		R78_AMT_LESS_184_DAYS = r78_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR78_AMT_MORE_184_DAYS() {
		return R78_AMT_MORE_184_DAYS;
	}

	public void setR78_AMT_MORE_184_DAYS(BigDecimal r78_AMT_MORE_184_DAYS) {
		R78_AMT_MORE_184_DAYS = r78_AMT_MORE_184_DAYS;
	}

	public String getR79_NAME_OF_BANK() {
		return R79_NAME_OF_BANK;
	}

	public void setR79_NAME_OF_BANK(String r79_NAME_OF_BANK) {
		R79_NAME_OF_BANK = r79_NAME_OF_BANK;
	}

	public String getR79_TYPE_OF_ACC() {
		return R79_TYPE_OF_ACC;
	}

	public void setR79_TYPE_OF_ACC(String r79_TYPE_OF_ACC) {
		R79_TYPE_OF_ACC = r79_TYPE_OF_ACC;
	}

	public String getR79_PURPOSE() {
		return R79_PURPOSE;
	}

	public void setR79_PURPOSE(String r79_PURPOSE) {
		R79_PURPOSE = r79_PURPOSE;
	}

	public String getR79_CURRENCY() {
		return R79_CURRENCY;
	}

	public void setR79_CURRENCY(String r79_CURRENCY) {
		R79_CURRENCY = r79_CURRENCY;
	}

	public String getR79_BANK_RATE() {
		return R79_BANK_RATE;
	}

	public void setR79_BANK_RATE(String r79_BANK_RATE) {
		R79_BANK_RATE = r79_BANK_RATE;
	}

	public BigDecimal getR79_AMT_LESS_184_DAYS() {
		return R79_AMT_LESS_184_DAYS;
	}

	public void setR79_AMT_LESS_184_DAYS(BigDecimal r79_AMT_LESS_184_DAYS) {
		R79_AMT_LESS_184_DAYS = r79_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR79_AMT_MORE_184_DAYS() {
		return R79_AMT_MORE_184_DAYS;
	}

	public void setR79_AMT_MORE_184_DAYS(BigDecimal r79_AMT_MORE_184_DAYS) {
		R79_AMT_MORE_184_DAYS = r79_AMT_MORE_184_DAYS;
	}

	public String getR80_NAME_OF_BANK() {
		return R80_NAME_OF_BANK;
	}

	public void setR80_NAME_OF_BANK(String r80_NAME_OF_BANK) {
		R80_NAME_OF_BANK = r80_NAME_OF_BANK;
	}

	public String getR80_TYPE_OF_ACC() {
		return R80_TYPE_OF_ACC;
	}

	public void setR80_TYPE_OF_ACC(String r80_TYPE_OF_ACC) {
		R80_TYPE_OF_ACC = r80_TYPE_OF_ACC;
	}

	public String getR80_PURPOSE() {
		return R80_PURPOSE;
	}

	public void setR80_PURPOSE(String r80_PURPOSE) {
		R80_PURPOSE = r80_PURPOSE;
	}

	public String getR80_CURRENCY() {
		return R80_CURRENCY;
	}

	public void setR80_CURRENCY(String r80_CURRENCY) {
		R80_CURRENCY = r80_CURRENCY;
	}

	public String getR80_BANK_RATE() {
		return R80_BANK_RATE;
	}

	public void setR80_BANK_RATE(String r80_BANK_RATE) {
		R80_BANK_RATE = r80_BANK_RATE;
	}

	public BigDecimal getR80_AMT_LESS_184_DAYS() {
		return R80_AMT_LESS_184_DAYS;
	}

	public void setR80_AMT_LESS_184_DAYS(BigDecimal r80_AMT_LESS_184_DAYS) {
		R80_AMT_LESS_184_DAYS = r80_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR80_AMT_MORE_184_DAYS() {
		return R80_AMT_MORE_184_DAYS;
	}

	public void setR80_AMT_MORE_184_DAYS(BigDecimal r80_AMT_MORE_184_DAYS) {
		R80_AMT_MORE_184_DAYS = r80_AMT_MORE_184_DAYS;
	}

	public String getR81_NAME_OF_BANK() {
		return R81_NAME_OF_BANK;
	}

	public void setR81_NAME_OF_BANK(String r81_NAME_OF_BANK) {
		R81_NAME_OF_BANK = r81_NAME_OF_BANK;
	}

	public String getR81_TYPE_OF_ACC() {
		return R81_TYPE_OF_ACC;
	}

	public void setR81_TYPE_OF_ACC(String r81_TYPE_OF_ACC) {
		R81_TYPE_OF_ACC = r81_TYPE_OF_ACC;
	}

	public String getR81_PURPOSE() {
		return R81_PURPOSE;
	}

	public void setR81_PURPOSE(String r81_PURPOSE) {
		R81_PURPOSE = r81_PURPOSE;
	}

	public String getR81_CURRENCY() {
		return R81_CURRENCY;
	}

	public void setR81_CURRENCY(String r81_CURRENCY) {
		R81_CURRENCY = r81_CURRENCY;
	}

	public String getR81_BANK_RATE() {
		return R81_BANK_RATE;
	}

	public void setR81_BANK_RATE(String r81_BANK_RATE) {
		R81_BANK_RATE = r81_BANK_RATE;
	}

	public BigDecimal getR81_AMT_LESS_184_DAYS() {
		return R81_AMT_LESS_184_DAYS;
	}

	public void setR81_AMT_LESS_184_DAYS(BigDecimal r81_AMT_LESS_184_DAYS) {
		R81_AMT_LESS_184_DAYS = r81_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR81_AMT_MORE_184_DAYS() {
		return R81_AMT_MORE_184_DAYS;
	}

	public void setR81_AMT_MORE_184_DAYS(BigDecimal r81_AMT_MORE_184_DAYS) {
		R81_AMT_MORE_184_DAYS = r81_AMT_MORE_184_DAYS;
	}

	public String getR82_NAME_OF_BANK() {
		return R82_NAME_OF_BANK;
	}

	public void setR82_NAME_OF_BANK(String r82_NAME_OF_BANK) {
		R82_NAME_OF_BANK = r82_NAME_OF_BANK;
	}

	public String getR82_TYPE_OF_ACC() {
		return R82_TYPE_OF_ACC;
	}

	public void setR82_TYPE_OF_ACC(String r82_TYPE_OF_ACC) {
		R82_TYPE_OF_ACC = r82_TYPE_OF_ACC;
	}

	public String getR82_PURPOSE() {
		return R82_PURPOSE;
	}

	public void setR82_PURPOSE(String r82_PURPOSE) {
		R82_PURPOSE = r82_PURPOSE;
	}

	public String getR82_CURRENCY() {
		return R82_CURRENCY;
	}

	public void setR82_CURRENCY(String r82_CURRENCY) {
		R82_CURRENCY = r82_CURRENCY;
	}

	public String getR82_BANK_RATE() {
		return R82_BANK_RATE;
	}

	public void setR82_BANK_RATE(String r82_BANK_RATE) {
		R82_BANK_RATE = r82_BANK_RATE;
	}

	public BigDecimal getR82_AMT_LESS_184_DAYS() {
		return R82_AMT_LESS_184_DAYS;
	}

	public void setR82_AMT_LESS_184_DAYS(BigDecimal r82_AMT_LESS_184_DAYS) {
		R82_AMT_LESS_184_DAYS = r82_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR82_AMT_MORE_184_DAYS() {
		return R82_AMT_MORE_184_DAYS;
	}

	public void setR82_AMT_MORE_184_DAYS(BigDecimal r82_AMT_MORE_184_DAYS) {
		R82_AMT_MORE_184_DAYS = r82_AMT_MORE_184_DAYS;
	}

	public String getR83_NAME_OF_BANK() {
		return R83_NAME_OF_BANK;
	}

	public void setR83_NAME_OF_BANK(String r83_NAME_OF_BANK) {
		R83_NAME_OF_BANK = r83_NAME_OF_BANK;
	}

	public String getR83_TYPE_OF_ACC() {
		return R83_TYPE_OF_ACC;
	}

	public void setR83_TYPE_OF_ACC(String r83_TYPE_OF_ACC) {
		R83_TYPE_OF_ACC = r83_TYPE_OF_ACC;
	}

	public String getR83_PURPOSE() {
		return R83_PURPOSE;
	}

	public void setR83_PURPOSE(String r83_PURPOSE) {
		R83_PURPOSE = r83_PURPOSE;
	}

	public String getR83_CURRENCY() {
		return R83_CURRENCY;
	}

	public void setR83_CURRENCY(String r83_CURRENCY) {
		R83_CURRENCY = r83_CURRENCY;
	}

	public String getR83_BANK_RATE() {
		return R83_BANK_RATE;
	}

	public void setR83_BANK_RATE(String r83_BANK_RATE) {
		R83_BANK_RATE = r83_BANK_RATE;
	}

	public BigDecimal getR83_AMT_LESS_184_DAYS() {
		return R83_AMT_LESS_184_DAYS;
	}

	public void setR83_AMT_LESS_184_DAYS(BigDecimal r83_AMT_LESS_184_DAYS) {
		R83_AMT_LESS_184_DAYS = r83_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR83_AMT_MORE_184_DAYS() {
		return R83_AMT_MORE_184_DAYS;
	}

	public void setR83_AMT_MORE_184_DAYS(BigDecimal r83_AMT_MORE_184_DAYS) {
		R83_AMT_MORE_184_DAYS = r83_AMT_MORE_184_DAYS;
	}

	public String getR84_NAME_OF_BANK() {
		return R84_NAME_OF_BANK;
	}

	public void setR84_NAME_OF_BANK(String r84_NAME_OF_BANK) {
		R84_NAME_OF_BANK = r84_NAME_OF_BANK;
	}

	public String getR84_TYPE_OF_ACC() {
		return R84_TYPE_OF_ACC;
	}

	public void setR84_TYPE_OF_ACC(String r84_TYPE_OF_ACC) {
		R84_TYPE_OF_ACC = r84_TYPE_OF_ACC;
	}

	public String getR84_PURPOSE() {
		return R84_PURPOSE;
	}

	public void setR84_PURPOSE(String r84_PURPOSE) {
		R84_PURPOSE = r84_PURPOSE;
	}

	public String getR84_CURRENCY() {
		return R84_CURRENCY;
	}

	public void setR84_CURRENCY(String r84_CURRENCY) {
		R84_CURRENCY = r84_CURRENCY;
	}

	public String getR84_BANK_RATE() {
		return R84_BANK_RATE;
	}

	public void setR84_BANK_RATE(String r84_BANK_RATE) {
		R84_BANK_RATE = r84_BANK_RATE;
	}

	public BigDecimal getR84_AMT_LESS_184_DAYS() {
		return R84_AMT_LESS_184_DAYS;
	}

	public void setR84_AMT_LESS_184_DAYS(BigDecimal r84_AMT_LESS_184_DAYS) {
		R84_AMT_LESS_184_DAYS = r84_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR84_AMT_MORE_184_DAYS() {
		return R84_AMT_MORE_184_DAYS;
	}

	public void setR84_AMT_MORE_184_DAYS(BigDecimal r84_AMT_MORE_184_DAYS) {
		R84_AMT_MORE_184_DAYS = r84_AMT_MORE_184_DAYS;
	}

	public String getR85_NAME_OF_BANK() {
		return R85_NAME_OF_BANK;
	}

	public void setR85_NAME_OF_BANK(String r85_NAME_OF_BANK) {
		R85_NAME_OF_BANK = r85_NAME_OF_BANK;
	}

	public String getR85_TYPE_OF_ACC() {
		return R85_TYPE_OF_ACC;
	}

	public void setR85_TYPE_OF_ACC(String r85_TYPE_OF_ACC) {
		R85_TYPE_OF_ACC = r85_TYPE_OF_ACC;
	}

	public String getR85_PURPOSE() {
		return R85_PURPOSE;
	}

	public void setR85_PURPOSE(String r85_PURPOSE) {
		R85_PURPOSE = r85_PURPOSE;
	}

	public String getR85_CURRENCY() {
		return R85_CURRENCY;
	}

	public void setR85_CURRENCY(String r85_CURRENCY) {
		R85_CURRENCY = r85_CURRENCY;
	}

	public String getR85_BANK_RATE() {
		return R85_BANK_RATE;
	}

	public void setR85_BANK_RATE(String r85_BANK_RATE) {
		R85_BANK_RATE = r85_BANK_RATE;
	}

	public BigDecimal getR85_AMT_LESS_184_DAYS() {
		return R85_AMT_LESS_184_DAYS;
	}

	public void setR85_AMT_LESS_184_DAYS(BigDecimal r85_AMT_LESS_184_DAYS) {
		R85_AMT_LESS_184_DAYS = r85_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR85_AMT_MORE_184_DAYS() {
		return R85_AMT_MORE_184_DAYS;
	}

	public void setR85_AMT_MORE_184_DAYS(BigDecimal r85_AMT_MORE_184_DAYS) {
		R85_AMT_MORE_184_DAYS = r85_AMT_MORE_184_DAYS;
	}

	public String getR86_NAME_OF_BANK() {
		return R86_NAME_OF_BANK;
	}

	public void setR86_NAME_OF_BANK(String r86_NAME_OF_BANK) {
		R86_NAME_OF_BANK = r86_NAME_OF_BANK;
	}

	public String getR86_TYPE_OF_ACC() {
		return R86_TYPE_OF_ACC;
	}

	public void setR86_TYPE_OF_ACC(String r86_TYPE_OF_ACC) {
		R86_TYPE_OF_ACC = r86_TYPE_OF_ACC;
	}

	public String getR86_PURPOSE() {
		return R86_PURPOSE;
	}

	public void setR86_PURPOSE(String r86_PURPOSE) {
		R86_PURPOSE = r86_PURPOSE;
	}

	public String getR86_CURRENCY() {
		return R86_CURRENCY;
	}

	public void setR86_CURRENCY(String r86_CURRENCY) {
		R86_CURRENCY = r86_CURRENCY;
	}

	public String getR86_BANK_RATE() {
		return R86_BANK_RATE;
	}

	public void setR86_BANK_RATE(String r86_BANK_RATE) {
		R86_BANK_RATE = r86_BANK_RATE;
	}

	public BigDecimal getR86_AMT_LESS_184_DAYS() {
		return R86_AMT_LESS_184_DAYS;
	}

	public void setR86_AMT_LESS_184_DAYS(BigDecimal r86_AMT_LESS_184_DAYS) {
		R86_AMT_LESS_184_DAYS = r86_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR86_AMT_MORE_184_DAYS() {
		return R86_AMT_MORE_184_DAYS;
	}

	public void setR86_AMT_MORE_184_DAYS(BigDecimal r86_AMT_MORE_184_DAYS) {
		R86_AMT_MORE_184_DAYS = r86_AMT_MORE_184_DAYS;
	}

	public String getR87_NAME_OF_BANK() {
		return R87_NAME_OF_BANK;
	}

	public void setR87_NAME_OF_BANK(String r87_NAME_OF_BANK) {
		R87_NAME_OF_BANK = r87_NAME_OF_BANK;
	}

	public String getR87_TYPE_OF_ACC() {
		return R87_TYPE_OF_ACC;
	}

	public void setR87_TYPE_OF_ACC(String r87_TYPE_OF_ACC) {
		R87_TYPE_OF_ACC = r87_TYPE_OF_ACC;
	}

	public String getR87_PURPOSE() {
		return R87_PURPOSE;
	}

	public void setR87_PURPOSE(String r87_PURPOSE) {
		R87_PURPOSE = r87_PURPOSE;
	}

	public String getR87_CURRENCY() {
		return R87_CURRENCY;
	}

	public void setR87_CURRENCY(String r87_CURRENCY) {
		R87_CURRENCY = r87_CURRENCY;
	}

	public String getR87_BANK_RATE() {
		return R87_BANK_RATE;
	}

	public void setR87_BANK_RATE(String r87_BANK_RATE) {
		R87_BANK_RATE = r87_BANK_RATE;
	}

	public BigDecimal getR87_AMT_LESS_184_DAYS() {
		return R87_AMT_LESS_184_DAYS;
	}

	public void setR87_AMT_LESS_184_DAYS(BigDecimal r87_AMT_LESS_184_DAYS) {
		R87_AMT_LESS_184_DAYS = r87_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR87_AMT_MORE_184_DAYS() {
		return R87_AMT_MORE_184_DAYS;
	}

	public void setR87_AMT_MORE_184_DAYS(BigDecimal r87_AMT_MORE_184_DAYS) {
		R87_AMT_MORE_184_DAYS = r87_AMT_MORE_184_DAYS;
	}

	public String getR88_NAME_OF_BANK() {
		return R88_NAME_OF_BANK;
	}

	public void setR88_NAME_OF_BANK(String r88_NAME_OF_BANK) {
		R88_NAME_OF_BANK = r88_NAME_OF_BANK;
	}

	public String getR88_TYPE_OF_ACC() {
		return R88_TYPE_OF_ACC;
	}

	public void setR88_TYPE_OF_ACC(String r88_TYPE_OF_ACC) {
		R88_TYPE_OF_ACC = r88_TYPE_OF_ACC;
	}

	public String getR88_PURPOSE() {
		return R88_PURPOSE;
	}

	public void setR88_PURPOSE(String r88_PURPOSE) {
		R88_PURPOSE = r88_PURPOSE;
	}

	public String getR88_CURRENCY() {
		return R88_CURRENCY;
	}

	public void setR88_CURRENCY(String r88_CURRENCY) {
		R88_CURRENCY = r88_CURRENCY;
	}

	public String getR88_BANK_RATE() {
		return R88_BANK_RATE;
	}

	public void setR88_BANK_RATE(String r88_BANK_RATE) {
		R88_BANK_RATE = r88_BANK_RATE;
	}

	public BigDecimal getR88_AMT_LESS_184_DAYS() {
		return R88_AMT_LESS_184_DAYS;
	}

	public void setR88_AMT_LESS_184_DAYS(BigDecimal r88_AMT_LESS_184_DAYS) {
		R88_AMT_LESS_184_DAYS = r88_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR88_AMT_MORE_184_DAYS() {
		return R88_AMT_MORE_184_DAYS;
	}

	public void setR88_AMT_MORE_184_DAYS(BigDecimal r88_AMT_MORE_184_DAYS) {
		R88_AMT_MORE_184_DAYS = r88_AMT_MORE_184_DAYS;
	}

	public String getR89_NAME_OF_BANK() {
		return R89_NAME_OF_BANK;
	}

	public void setR89_NAME_OF_BANK(String r89_NAME_OF_BANK) {
		R89_NAME_OF_BANK = r89_NAME_OF_BANK;
	}

	public String getR89_TYPE_OF_ACC() {
		return R89_TYPE_OF_ACC;
	}

	public void setR89_TYPE_OF_ACC(String r89_TYPE_OF_ACC) {
		R89_TYPE_OF_ACC = r89_TYPE_OF_ACC;
	}

	public String getR89_PURPOSE() {
		return R89_PURPOSE;
	}

	public void setR89_PURPOSE(String r89_PURPOSE) {
		R89_PURPOSE = r89_PURPOSE;
	}

	public String getR89_CURRENCY() {
		return R89_CURRENCY;
	}

	public void setR89_CURRENCY(String r89_CURRENCY) {
		R89_CURRENCY = r89_CURRENCY;
	}

	public String getR89_BANK_RATE() {
		return R89_BANK_RATE;
	}

	public void setR89_BANK_RATE(String r89_BANK_RATE) {
		R89_BANK_RATE = r89_BANK_RATE;
	}

	public BigDecimal getR89_AMT_LESS_184_DAYS() {
		return R89_AMT_LESS_184_DAYS;
	}

	public void setR89_AMT_LESS_184_DAYS(BigDecimal r89_AMT_LESS_184_DAYS) {
		R89_AMT_LESS_184_DAYS = r89_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR89_AMT_MORE_184_DAYS() {
		return R89_AMT_MORE_184_DAYS;
	}

	public void setR89_AMT_MORE_184_DAYS(BigDecimal r89_AMT_MORE_184_DAYS) {
		R89_AMT_MORE_184_DAYS = r89_AMT_MORE_184_DAYS;
	}

	public String getR90_NAME_OF_BANK() {
		return R90_NAME_OF_BANK;
	}

	public void setR90_NAME_OF_BANK(String r90_NAME_OF_BANK) {
		R90_NAME_OF_BANK = r90_NAME_OF_BANK;
	}

	public String getR90_TYPE_OF_ACC() {
		return R90_TYPE_OF_ACC;
	}

	public void setR90_TYPE_OF_ACC(String r90_TYPE_OF_ACC) {
		R90_TYPE_OF_ACC = r90_TYPE_OF_ACC;
	}

	public String getR90_PURPOSE() {
		return R90_PURPOSE;
	}

	public void setR90_PURPOSE(String r90_PURPOSE) {
		R90_PURPOSE = r90_PURPOSE;
	}

	public String getR90_CURRENCY() {
		return R90_CURRENCY;
	}

	public void setR90_CURRENCY(String r90_CURRENCY) {
		R90_CURRENCY = r90_CURRENCY;
	}

	public String getR90_BANK_RATE() {
		return R90_BANK_RATE;
	}

	public void setR90_BANK_RATE(String r90_BANK_RATE) {
		R90_BANK_RATE = r90_BANK_RATE;
	}

	public BigDecimal getR90_AMT_LESS_184_DAYS() {
		return R90_AMT_LESS_184_DAYS;
	}

	public void setR90_AMT_LESS_184_DAYS(BigDecimal r90_AMT_LESS_184_DAYS) {
		R90_AMT_LESS_184_DAYS = r90_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR90_AMT_MORE_184_DAYS() {
		return R90_AMT_MORE_184_DAYS;
	}

	public void setR90_AMT_MORE_184_DAYS(BigDecimal r90_AMT_MORE_184_DAYS) {
		R90_AMT_MORE_184_DAYS = r90_AMT_MORE_184_DAYS;
	}

	public String getR91_NAME_OF_BANK() {
		return R91_NAME_OF_BANK;
	}

	public void setR91_NAME_OF_BANK(String r91_NAME_OF_BANK) {
		R91_NAME_OF_BANK = r91_NAME_OF_BANK;
	}

	public String getR91_TYPE_OF_ACC() {
		return R91_TYPE_OF_ACC;
	}

	public void setR91_TYPE_OF_ACC(String r91_TYPE_OF_ACC) {
		R91_TYPE_OF_ACC = r91_TYPE_OF_ACC;
	}

	public String getR91_PURPOSE() {
		return R91_PURPOSE;
	}

	public void setR91_PURPOSE(String r91_PURPOSE) {
		R91_PURPOSE = r91_PURPOSE;
	}

	public String getR91_CURRENCY() {
		return R91_CURRENCY;
	}

	public void setR91_CURRENCY(String r91_CURRENCY) {
		R91_CURRENCY = r91_CURRENCY;
	}

	public String getR91_BANK_RATE() {
		return R91_BANK_RATE;
	}

	public void setR91_BANK_RATE(String r91_BANK_RATE) {
		R91_BANK_RATE = r91_BANK_RATE;
	}

	public BigDecimal getR91_AMT_LESS_184_DAYS() {
		return R91_AMT_LESS_184_DAYS;
	}

	public void setR91_AMT_LESS_184_DAYS(BigDecimal r91_AMT_LESS_184_DAYS) {
		R91_AMT_LESS_184_DAYS = r91_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR91_AMT_MORE_184_DAYS() {
		return R91_AMT_MORE_184_DAYS;
	}

	public void setR91_AMT_MORE_184_DAYS(BigDecimal r91_AMT_MORE_184_DAYS) {
		R91_AMT_MORE_184_DAYS = r91_AMT_MORE_184_DAYS;
	}

	public String getR92_NAME_OF_BANK() {
		return R92_NAME_OF_BANK;
	}

	public void setR92_NAME_OF_BANK(String r92_NAME_OF_BANK) {
		R92_NAME_OF_BANK = r92_NAME_OF_BANK;
	}

	public String getR92_TYPE_OF_ACC() {
		return R92_TYPE_OF_ACC;
	}

	public void setR92_TYPE_OF_ACC(String r92_TYPE_OF_ACC) {
		R92_TYPE_OF_ACC = r92_TYPE_OF_ACC;
	}

	public String getR92_PURPOSE() {
		return R92_PURPOSE;
	}

	public void setR92_PURPOSE(String r92_PURPOSE) {
		R92_PURPOSE = r92_PURPOSE;
	}

	public String getR92_CURRENCY() {
		return R92_CURRENCY;
	}

	public void setR92_CURRENCY(String r92_CURRENCY) {
		R92_CURRENCY = r92_CURRENCY;
	}

	public String getR92_BANK_RATE() {
		return R92_BANK_RATE;
	}

	public void setR92_BANK_RATE(String r92_BANK_RATE) {
		R92_BANK_RATE = r92_BANK_RATE;
	}

	public BigDecimal getR92_AMT_LESS_184_DAYS() {
		return R92_AMT_LESS_184_DAYS;
	}

	public void setR92_AMT_LESS_184_DAYS(BigDecimal r92_AMT_LESS_184_DAYS) {
		R92_AMT_LESS_184_DAYS = r92_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR92_AMT_MORE_184_DAYS() {
		return R92_AMT_MORE_184_DAYS;
	}

	public void setR92_AMT_MORE_184_DAYS(BigDecimal r92_AMT_MORE_184_DAYS) {
		R92_AMT_MORE_184_DAYS = r92_AMT_MORE_184_DAYS;
	}

	public String getR93_NAME_OF_BANK() {
		return R93_NAME_OF_BANK;
	}

	public void setR93_NAME_OF_BANK(String r93_NAME_OF_BANK) {
		R93_NAME_OF_BANK = r93_NAME_OF_BANK;
	}

	public String getR93_TYPE_OF_ACC() {
		return R93_TYPE_OF_ACC;
	}

	public void setR93_TYPE_OF_ACC(String r93_TYPE_OF_ACC) {
		R93_TYPE_OF_ACC = r93_TYPE_OF_ACC;
	}

	public String getR93_PURPOSE() {
		return R93_PURPOSE;
	}

	public void setR93_PURPOSE(String r93_PURPOSE) {
		R93_PURPOSE = r93_PURPOSE;
	}

	public String getR93_CURRENCY() {
		return R93_CURRENCY;
	}

	public void setR93_CURRENCY(String r93_CURRENCY) {
		R93_CURRENCY = r93_CURRENCY;
	}

	public String getR93_BANK_RATE() {
		return R93_BANK_RATE;
	}

	public void setR93_BANK_RATE(String r93_BANK_RATE) {
		R93_BANK_RATE = r93_BANK_RATE;
	}

	public BigDecimal getR93_AMT_LESS_184_DAYS() {
		return R93_AMT_LESS_184_DAYS;
	}

	public void setR93_AMT_LESS_184_DAYS(BigDecimal r93_AMT_LESS_184_DAYS) {
		R93_AMT_LESS_184_DAYS = r93_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR93_AMT_MORE_184_DAYS() {
		return R93_AMT_MORE_184_DAYS;
	}

	public void setR93_AMT_MORE_184_DAYS(BigDecimal r93_AMT_MORE_184_DAYS) {
		R93_AMT_MORE_184_DAYS = r93_AMT_MORE_184_DAYS;
	}

	public String getR94_NAME_OF_BANK() {
		return R94_NAME_OF_BANK;
	}

	public void setR94_NAME_OF_BANK(String r94_NAME_OF_BANK) {
		R94_NAME_OF_BANK = r94_NAME_OF_BANK;
	}

	public String getR94_TYPE_OF_ACC() {
		return R94_TYPE_OF_ACC;
	}

	public void setR94_TYPE_OF_ACC(String r94_TYPE_OF_ACC) {
		R94_TYPE_OF_ACC = r94_TYPE_OF_ACC;
	}

	public String getR94_PURPOSE() {
		return R94_PURPOSE;
	}

	public void setR94_PURPOSE(String r94_PURPOSE) {
		R94_PURPOSE = r94_PURPOSE;
	}

	public String getR94_CURRENCY() {
		return R94_CURRENCY;
	}

	public void setR94_CURRENCY(String r94_CURRENCY) {
		R94_CURRENCY = r94_CURRENCY;
	}

	public String getR94_BANK_RATE() {
		return R94_BANK_RATE;
	}

	public void setR94_BANK_RATE(String r94_BANK_RATE) {
		R94_BANK_RATE = r94_BANK_RATE;
	}

	public BigDecimal getR94_AMT_LESS_184_DAYS() {
		return R94_AMT_LESS_184_DAYS;
	}

	public void setR94_AMT_LESS_184_DAYS(BigDecimal r94_AMT_LESS_184_DAYS) {
		R94_AMT_LESS_184_DAYS = r94_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR94_AMT_MORE_184_DAYS() {
		return R94_AMT_MORE_184_DAYS;
	}

	public void setR94_AMT_MORE_184_DAYS(BigDecimal r94_AMT_MORE_184_DAYS) {
		R94_AMT_MORE_184_DAYS = r94_AMT_MORE_184_DAYS;
	}

	public String getR95_NAME_OF_BANK() {
		return R95_NAME_OF_BANK;
	}

	public void setR95_NAME_OF_BANK(String r95_NAME_OF_BANK) {
		R95_NAME_OF_BANK = r95_NAME_OF_BANK;
	}

	public String getR95_TYPE_OF_ACC() {
		return R95_TYPE_OF_ACC;
	}

	public void setR95_TYPE_OF_ACC(String r95_TYPE_OF_ACC) {
		R95_TYPE_OF_ACC = r95_TYPE_OF_ACC;
	}

	public String getR95_PURPOSE() {
		return R95_PURPOSE;
	}

	public void setR95_PURPOSE(String r95_PURPOSE) {
		R95_PURPOSE = r95_PURPOSE;
	}

	public String getR95_CURRENCY() {
		return R95_CURRENCY;
	}

	public void setR95_CURRENCY(String r95_CURRENCY) {
		R95_CURRENCY = r95_CURRENCY;
	}

	public String getR95_BANK_RATE() {
		return R95_BANK_RATE;
	}

	public void setR95_BANK_RATE(String r95_BANK_RATE) {
		R95_BANK_RATE = r95_BANK_RATE;
	}

	public BigDecimal getR95_AMT_LESS_184_DAYS() {
		return R95_AMT_LESS_184_DAYS;
	}

	public void setR95_AMT_LESS_184_DAYS(BigDecimal r95_AMT_LESS_184_DAYS) {
		R95_AMT_LESS_184_DAYS = r95_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR95_AMT_MORE_184_DAYS() {
		return R95_AMT_MORE_184_DAYS;
	}

	public void setR95_AMT_MORE_184_DAYS(BigDecimal r95_AMT_MORE_184_DAYS) {
		R95_AMT_MORE_184_DAYS = r95_AMT_MORE_184_DAYS;
	}

	public BigDecimal getR96_TOT_AMT_LESS_184_DAYS() {
		return R96_TOT_AMT_LESS_184_DAYS;
	}

	public void setR96_TOT_AMT_LESS_184_DAYS(BigDecimal r96_TOT_AMT_LESS_184_DAYS) {
		R96_TOT_AMT_LESS_184_DAYS = r96_TOT_AMT_LESS_184_DAYS;
	}

	public BigDecimal getR96_TOT_AMT_MORE_184_DAYS() {
		return R96_TOT_AMT_MORE_184_DAYS;
	}

	public void setR96_TOT_AMT_MORE_184_DAYS(BigDecimal r96_TOT_AMT_MORE_184_DAYS) {
		R96_TOT_AMT_MORE_184_DAYS = r96_TOT_AMT_MORE_184_DAYS;
	}
	public Date getReport_resubdate() {
		return report_resubdate;
	}

	public void setReport_resubdate(Date report_resubdate) {
		this.report_resubdate = report_resubdate;
	}

	public M_AIDP_Archival_Detail_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(M_AIDP_PK pk) {
		// TODO Auto-generated method stub

	}
}
