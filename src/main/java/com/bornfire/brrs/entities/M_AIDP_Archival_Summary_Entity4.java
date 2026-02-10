package com.bornfire.brrs.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_M_AIDP_ARCHIVALTABLE_SUMMARY4")
@IdClass(M_AIDP_PK.class)
public class M_AIDP_Archival_Summary_Entity4 implements Serializable {

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

	private String R147_NAME_OF_BANK;
	private String R147_COUNTRY;
	private String R147_TYPE_OF_ACC;
	private String R147_PURPOSE;
	private String R147_CURRENCY;
	private String R147_BANK_RATE;
	private BigDecimal R147_AMT_DEMAND;
	private BigDecimal R147_AMT_TIME;
	private String R148_NAME_OF_BANK;
	private String R148_COUNTRY;
	private String R148_TYPE_OF_ACC;
	private String R148_PURPOSE;
	private String R148_CURRENCY;
	private String R148_BANK_RATE;
	private BigDecimal R148_AMT_DEMAND;
	private BigDecimal R148_AMT_TIME;
	private String R149_NAME_OF_BANK;
	private String R149_COUNTRY;
	private String R149_TYPE_OF_ACC;
	private String R149_PURPOSE;
	private String R149_CURRENCY;
	private String R149_BANK_RATE;
	private BigDecimal R149_AMT_DEMAND;
	private BigDecimal R149_AMT_TIME;
	private String R150_NAME_OF_BANK;
	private String R150_COUNTRY;
	private String R150_TYPE_OF_ACC;
	private String R150_PURPOSE;
	private String R150_CURRENCY;
	private String R150_BANK_RATE;
	private BigDecimal R150_AMT_DEMAND;
	private BigDecimal R150_AMT_TIME;
	private String R151_NAME_OF_BANK;
	private String R151_COUNTRY;
	private String R151_TYPE_OF_ACC;
	private String R151_PURPOSE;
	private String R151_CURRENCY;
	private String R151_BANK_RATE;
	private BigDecimal R151_AMT_DEMAND;
	private BigDecimal R151_AMT_TIME;
	private String R152_NAME_OF_BANK;
	private String R152_COUNTRY;
	private String R152_TYPE_OF_ACC;
	private String R152_PURPOSE;
	private String R152_CURRENCY;
	private String R152_BANK_RATE;
	private BigDecimal R152_AMT_DEMAND;
	private BigDecimal R152_AMT_TIME;
	private String R153_NAME_OF_BANK;
	private String R153_COUNTRY;
	private String R153_TYPE_OF_ACC;
	private String R153_PURPOSE;
	private String R153_CURRENCY;
	private String R153_BANK_RATE;
	private BigDecimal R153_AMT_DEMAND;
	private BigDecimal R153_AMT_TIME;
	private String R154_NAME_OF_BANK;
	private String R154_COUNTRY;
	private String R154_TYPE_OF_ACC;
	private String R154_PURPOSE;
	private String R154_CURRENCY;
	private String R154_BANK_RATE;
	private BigDecimal R154_AMT_DEMAND;
	private BigDecimal R154_AMT_TIME;
	private String R155_NAME_OF_BANK;
	private String R155_COUNTRY;
	private String R155_TYPE_OF_ACC;
	private String R155_PURPOSE;
	private String R155_CURRENCY;
	private String R155_BANK_RATE;
	private BigDecimal R155_AMT_DEMAND;
	private BigDecimal R155_AMT_TIME;
	private String R156_NAME_OF_BANK;
	private String R156_COUNTRY;
	private String R156_TYPE_OF_ACC;
	private String R156_PURPOSE;
	private String R156_CURRENCY;
	private String R156_BANK_RATE;
	private BigDecimal R156_AMT_DEMAND;
	private BigDecimal R156_AMT_TIME;
	private String R157_NAME_OF_BANK;
	private String R157_COUNTRY;
	private String R157_TYPE_OF_ACC;
	private String R157_PURPOSE;
	private String R157_CURRENCY;
	private String R157_BANK_RATE;
	private BigDecimal R157_AMT_DEMAND;
	private BigDecimal R157_AMT_TIME;
	private String R158_NAME_OF_BANK;
	private String R158_COUNTRY;
	private String R158_TYPE_OF_ACC;
	private String R158_PURPOSE;
	private String R158_CURRENCY;
	private String R158_BANK_RATE;
	private BigDecimal R158_AMT_DEMAND;
	private BigDecimal R158_AMT_TIME;
	private String R159_NAME_OF_BANK;
	private String R159_COUNTRY;
	private String R159_TYPE_OF_ACC;
	private String R159_PURPOSE;
	private String R159_CURRENCY;
	private String R159_BANK_RATE;
	private BigDecimal R159_AMT_DEMAND;
	private BigDecimal R159_AMT_TIME;
	private String R160_NAME_OF_BANK;
	private String R160_COUNTRY;
	private String R160_TYPE_OF_ACC;
	private String R160_PURPOSE;
	private String R160_CURRENCY;
	private String R160_BANK_RATE;
	private BigDecimal R160_AMT_DEMAND;
	private BigDecimal R160_AMT_TIME;
	private String R161_NAME_OF_BANK;
	private String R161_COUNTRY;
	private String R161_TYPE_OF_ACC;
	private String R161_PURPOSE;
	private String R161_CURRENCY;
	private String R161_BANK_RATE;
	private BigDecimal R161_AMT_DEMAND;
	private BigDecimal R161_AMT_TIME;
	private String R162_NAME_OF_BANK;
	private String R162_COUNTRY;
	private String R162_TYPE_OF_ACC;
	private String R162_PURPOSE;
	private String R162_CURRENCY;
	private String R162_BANK_RATE;
	private BigDecimal R162_AMT_DEMAND;
	private BigDecimal R162_AMT_TIME;
	private String R163_NAME_OF_BANK;
	private String R163_COUNTRY;
	private String R163_TYPE_OF_ACC;
	private String R163_PURPOSE;
	private String R163_CURRENCY;
	private String R163_BANK_RATE;
	private BigDecimal R163_AMT_DEMAND;
	private BigDecimal R163_AMT_TIME;
	private String R164_NAME_OF_BANK;
	private String R164_COUNTRY;
	private String R164_TYPE_OF_ACC;
	private String R164_PURPOSE;
	private String R164_CURRENCY;
	private String R164_BANK_RATE;
	private BigDecimal R164_AMT_DEMAND;
	private BigDecimal R164_AMT_TIME;
	private String R165_NAME_OF_BANK;
	private String R165_COUNTRY;
	private String R165_TYPE_OF_ACC;
	private String R165_PURPOSE;
	private String R165_CURRENCY;
	private String R165_BANK_RATE;
	private BigDecimal R165_AMT_DEMAND;
	private BigDecimal R165_AMT_TIME;
	private String R166_NAME_OF_BANK;
	private String R166_COUNTRY;
	private String R166_TYPE_OF_ACC;
	private String R166_PURPOSE;
	private String R166_CURRENCY;
	private String R166_BANK_RATE;
	private BigDecimal R166_AMT_DEMAND;
	private BigDecimal R166_AMT_TIME;
	private String R167_NAME_OF_BANK;
	private String R167_COUNTRY;
	private String R167_TYPE_OF_ACC;
	private String R167_PURPOSE;
	private String R167_CURRENCY;
	private String R167_BANK_RATE;
	private BigDecimal R167_AMT_DEMAND;
	private BigDecimal R167_AMT_TIME;
	private String R168_NAME_OF_BANK;
	private String R168_COUNTRY;
	private String R168_TYPE_OF_ACC;
	private String R168_PURPOSE;
	private String R168_CURRENCY;
	private String R168_BANK_RATE;
	private BigDecimal R168_AMT_DEMAND;
	private BigDecimal R168_AMT_TIME;
	private String R169_NAME_OF_BANK;
	private String R169_COUNTRY;
	private String R169_TYPE_OF_ACC;
	private String R169_PURPOSE;
	private String R169_CURRENCY;
	private String R169_BANK_RATE;
	private BigDecimal R169_AMT_DEMAND;
	private BigDecimal R169_AMT_TIME;
	private String R170_NAME_OF_BANK;
	private String R170_COUNTRY;
	private String R170_TYPE_OF_ACC;
	private String R170_PURPOSE;
	private String R170_CURRENCY;
	private String R170_BANK_RATE;
	private BigDecimal R170_AMT_DEMAND;
	private BigDecimal R170_AMT_TIME;
	private String R171_NAME_OF_BANK;
	private String R171_COUNTRY;
	private String R171_TYPE_OF_ACC;
	private String R171_PURPOSE;
	private String R171_CURRENCY;
	private String R171_BANK_RATE;
	private BigDecimal R171_AMT_DEMAND;
	private BigDecimal R171_AMT_TIME;
	private String R172_NAME_OF_BANK;
	private String R172_COUNTRY;
	private String R172_TYPE_OF_ACC;
	private String R172_PURPOSE;
	private String R172_CURRENCY;
	private String R172_BANK_RATE;
	private BigDecimal R172_AMT_DEMAND;
	private BigDecimal R172_AMT_TIME;
	private String R173_NAME_OF_BANK;
	private String R173_COUNTRY;
	private String R173_TYPE_OF_ACC;
	private String R173_PURPOSE;
	private String R173_CURRENCY;
	private String R173_BANK_RATE;
	private BigDecimal R173_AMT_DEMAND;
	private BigDecimal R173_AMT_TIME;
	private String R174_NAME_OF_BANK;
	private String R174_COUNTRY;
	private String R174_TYPE_OF_ACC;
	private String R174_PURPOSE;
	private String R174_CURRENCY;
	private String R174_BANK_RATE;
	private BigDecimal R174_AMT_DEMAND;
	private BigDecimal R174_AMT_TIME;
	private String R175_NAME_OF_BANK;
	private String R175_COUNTRY;
	private String R175_TYPE_OF_ACC;
	private String R175_PURPOSE;
	private String R175_CURRENCY;
	private String R175_BANK_RATE;
	private BigDecimal R175_AMT_DEMAND;
	private BigDecimal R175_AMT_TIME;
	private String R176_NAME_OF_BANK;
	private String R176_COUNTRY;
	private String R176_TYPE_OF_ACC;
	private String R176_PURPOSE;
	private String R176_CURRENCY;
	private String R176_BANK_RATE;
	private BigDecimal R176_AMT_DEMAND;
	private BigDecimal R176_AMT_TIME;
	private String R177_NAME_OF_BANK;
	private String R177_COUNTRY;
	private String R177_TYPE_OF_ACC;
	private String R177_PURPOSE;
	private String R177_CURRENCY;
	private String R177_BANK_RATE;
	private BigDecimal R177_AMT_DEMAND;
	private BigDecimal R177_AMT_TIME;
	private String R178_NAME_OF_BANK;
	private String R178_COUNTRY;
	private String R178_TYPE_OF_ACC;
	private String R178_PURPOSE;
	private String R178_CURRENCY;
	private String R178_BANK_RATE;
	private BigDecimal R178_AMT_DEMAND;
	private BigDecimal R178_AMT_TIME;
	private String R179_NAME_OF_BANK;
	private String R179_COUNTRY;
	private String R179_TYPE_OF_ACC;
	private String R179_PURPOSE;
	private String R179_CURRENCY;
	private String R179_BANK_RATE;
	private BigDecimal R179_AMT_DEMAND;
	private BigDecimal R179_AMT_TIME;
	private String R180_NAME_OF_BANK;
	private String R180_COUNTRY;
	private String R180_TYPE_OF_ACC;
	private String R180_PURPOSE;
	private String R180_CURRENCY;
	private String R180_BANK_RATE;
	private BigDecimal R180_AMT_DEMAND;
	private BigDecimal R180_AMT_TIME;
	private String R181_NAME_OF_BANK;
	private String R181_COUNTRY;
	private String R181_TYPE_OF_ACC;
	private String R181_PURPOSE;
	private String R181_CURRENCY;
	private String R181_BANK_RATE;
	private BigDecimal R181_AMT_DEMAND;
	private BigDecimal R181_AMT_TIME;
	private String R182_NAME_OF_BANK;
	private String R182_COUNTRY;
	private String R182_TYPE_OF_ACC;
	private String R182_PURPOSE;
	private String R182_CURRENCY;
	private String R182_BANK_RATE;
	private BigDecimal R182_AMT_DEMAND;
	private BigDecimal R182_AMT_TIME;
	private String R183_NAME_OF_BANK;
	private String R183_COUNTRY;
	private String R183_TYPE_OF_ACC;
	private String R183_PURPOSE;
	private String R183_CURRENCY;
	private String R183_BANK_RATE;
	private BigDecimal R183_AMT_DEMAND;
	private BigDecimal R183_AMT_TIME;
	private String R184_NAME_OF_BANK;
	private String R184_COUNTRY;
	private String R184_TYPE_OF_ACC;
	private String R184_PURPOSE;
	private String R184_CURRENCY;
	private String R184_BANK_RATE;
	private BigDecimal R184_AMT_DEMAND;
	private BigDecimal R184_AMT_TIME;
	private String R185_NAME_OF_BANK;
	private String R185_COUNTRY;
	private String R185_TYPE_OF_ACC;
	private String R185_PURPOSE;
	private String R185_CURRENCY;
	private String R185_BANK_RATE;
	private BigDecimal R185_AMT_DEMAND;
	private BigDecimal R185_AMT_TIME;
	private String R186_NAME_OF_BANK;
	private String R186_COUNTRY;
	private String R186_TYPE_OF_ACC;
	private String R186_PURPOSE;
	private String R186_CURRENCY;
	private String R186_BANK_RATE;
	private BigDecimal R186_AMT_DEMAND;
	private BigDecimal R186_AMT_TIME;
	private String R187_NAME_OF_BANK;
	private String R187_COUNTRY;
	private String R187_TYPE_OF_ACC;
	private String R187_PURPOSE;
	private String R187_CURRENCY;
	private String R187_BANK_RATE;
	private BigDecimal R187_AMT_DEMAND;
	private BigDecimal R187_AMT_TIME;
	private String R188_NAME_OF_BANK;
	private String R188_COUNTRY;
	private String R188_TYPE_OF_ACC;
	private String R188_PURPOSE;
	private String R188_CURRENCY;
	private String R188_BANK_RATE;
	private BigDecimal R188_AMT_DEMAND;
	private BigDecimal R188_AMT_TIME;
	private String R189_NAME_OF_BANK;
	private String R189_COUNTRY;
	private String R189_TYPE_OF_ACC;
	private String R189_PURPOSE;
	private String R189_CURRENCY;
	private String R189_BANK_RATE;
	private BigDecimal R189_AMT_DEMAND;
	private BigDecimal R189_AMT_TIME;
	private String R190_NAME_OF_BANK;
	private String R190_COUNTRY;
	private String R190_TYPE_OF_ACC;
	private String R190_PURPOSE;
	private String R190_CURRENCY;
	private String R190_BANK_RATE;
	private BigDecimal R190_AMT_DEMAND;
	private BigDecimal R190_AMT_TIME;
	private String R191_NAME_OF_BANK;
	private String R191_COUNTRY;
	private String R191_TYPE_OF_ACC;
	private String R191_PURPOSE;
	private String R191_CURRENCY;
	private String R191_BANK_RATE;
	private BigDecimal R191_AMT_DEMAND;
	private BigDecimal R191_AMT_TIME;
	private String R192_NAME_OF_BANK;
	private String R192_COUNTRY;
	private String R192_TYPE_OF_ACC;
	private String R192_PURPOSE;
	private String R192_CURRENCY;
	private String R192_BANK_RATE;
	private BigDecimal R192_AMT_DEMAND;
	private BigDecimal R192_AMT_TIME;
	private String R193_NAME_OF_BANK;
	private String R193_COUNTRY;
	private String R193_TYPE_OF_ACC;
	private String R193_PURPOSE;
	private String R193_CURRENCY;
	private String R193_BANK_RATE;
	private BigDecimal R193_AMT_DEMAND;
	private BigDecimal R193_AMT_TIME;
	private BigDecimal R194_TOT_AMT_DEMAND;
	private BigDecimal R194_TOT_AMT_TIME;

	public Date getReport_date() {
		return report_date;
	}

	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getReport_version() {
		return report_version;
	}

	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
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

	public String getR147_NAME_OF_BANK() {
		return R147_NAME_OF_BANK;
	}

	public void setR147_NAME_OF_BANK(String r147_NAME_OF_BANK) {
		R147_NAME_OF_BANK = r147_NAME_OF_BANK;
	}

	public String getR147_COUNTRY() {
		return R147_COUNTRY;
	}

	public void setR147_COUNTRY(String r147_COUNTRY) {
		R147_COUNTRY = r147_COUNTRY;
	}

	public String getR147_TYPE_OF_ACC() {
		return R147_TYPE_OF_ACC;
	}

	public void setR147_TYPE_OF_ACC(String r147_TYPE_OF_ACC) {
		R147_TYPE_OF_ACC = r147_TYPE_OF_ACC;
	}

	public String getR147_PURPOSE() {
		return R147_PURPOSE;
	}

	public void setR147_PURPOSE(String r147_PURPOSE) {
		R147_PURPOSE = r147_PURPOSE;
	}

	public String getR147_CURRENCY() {
		return R147_CURRENCY;
	}

	public void setR147_CURRENCY(String r147_CURRENCY) {
		R147_CURRENCY = r147_CURRENCY;
	}

	public String getR147_BANK_RATE() {
		return R147_BANK_RATE;
	}

	public void setR147_BANK_RATE(String r147_BANK_RATE) {
		R147_BANK_RATE = r147_BANK_RATE;
	}

	public BigDecimal getR147_AMT_DEMAND() {
		return R147_AMT_DEMAND;
	}

	public void setR147_AMT_DEMAND(BigDecimal r147_AMT_DEMAND) {
		R147_AMT_DEMAND = r147_AMT_DEMAND;
	}

	public BigDecimal getR147_AMT_TIME() {
		return R147_AMT_TIME;
	}

	public void setR147_AMT_TIME(BigDecimal r147_AMT_TIME) {
		R147_AMT_TIME = r147_AMT_TIME;
	}

	public String getR148_NAME_OF_BANK() {
		return R148_NAME_OF_BANK;
	}

	public void setR148_NAME_OF_BANK(String r148_NAME_OF_BANK) {
		R148_NAME_OF_BANK = r148_NAME_OF_BANK;
	}

	public String getR148_COUNTRY() {
		return R148_COUNTRY;
	}

	public void setR148_COUNTRY(String r148_COUNTRY) {
		R148_COUNTRY = r148_COUNTRY;
	}

	public String getR148_TYPE_OF_ACC() {
		return R148_TYPE_OF_ACC;
	}

	public void setR148_TYPE_OF_ACC(String r148_TYPE_OF_ACC) {
		R148_TYPE_OF_ACC = r148_TYPE_OF_ACC;
	}

	public String getR148_PURPOSE() {
		return R148_PURPOSE;
	}

	public void setR148_PURPOSE(String r148_PURPOSE) {
		R148_PURPOSE = r148_PURPOSE;
	}

	public String getR148_CURRENCY() {
		return R148_CURRENCY;
	}

	public void setR148_CURRENCY(String r148_CURRENCY) {
		R148_CURRENCY = r148_CURRENCY;
	}

	public String getR148_BANK_RATE() {
		return R148_BANK_RATE;
	}

	public void setR148_BANK_RATE(String r148_BANK_RATE) {
		R148_BANK_RATE = r148_BANK_RATE;
	}

	public BigDecimal getR148_AMT_DEMAND() {
		return R148_AMT_DEMAND;
	}

	public void setR148_AMT_DEMAND(BigDecimal r148_AMT_DEMAND) {
		R148_AMT_DEMAND = r148_AMT_DEMAND;
	}

	public BigDecimal getR148_AMT_TIME() {
		return R148_AMT_TIME;
	}

	public void setR148_AMT_TIME(BigDecimal r148_AMT_TIME) {
		R148_AMT_TIME = r148_AMT_TIME;
	}

	public String getR149_NAME_OF_BANK() {
		return R149_NAME_OF_BANK;
	}

	public void setR149_NAME_OF_BANK(String r149_NAME_OF_BANK) {
		R149_NAME_OF_BANK = r149_NAME_OF_BANK;
	}

	public String getR149_COUNTRY() {
		return R149_COUNTRY;
	}

	public void setR149_COUNTRY(String r149_COUNTRY) {
		R149_COUNTRY = r149_COUNTRY;
	}

	public String getR149_TYPE_OF_ACC() {
		return R149_TYPE_OF_ACC;
	}

	public void setR149_TYPE_OF_ACC(String r149_TYPE_OF_ACC) {
		R149_TYPE_OF_ACC = r149_TYPE_OF_ACC;
	}

	public String getR149_PURPOSE() {
		return R149_PURPOSE;
	}

	public void setR149_PURPOSE(String r149_PURPOSE) {
		R149_PURPOSE = r149_PURPOSE;
	}

	public String getR149_CURRENCY() {
		return R149_CURRENCY;
	}

	public void setR149_CURRENCY(String r149_CURRENCY) {
		R149_CURRENCY = r149_CURRENCY;
	}

	public String getR149_BANK_RATE() {
		return R149_BANK_RATE;
	}

	public void setR149_BANK_RATE(String r149_BANK_RATE) {
		R149_BANK_RATE = r149_BANK_RATE;
	}

	public BigDecimal getR149_AMT_DEMAND() {
		return R149_AMT_DEMAND;
	}

	public void setR149_AMT_DEMAND(BigDecimal r149_AMT_DEMAND) {
		R149_AMT_DEMAND = r149_AMT_DEMAND;
	}

	public BigDecimal getR149_AMT_TIME() {
		return R149_AMT_TIME;
	}

	public void setR149_AMT_TIME(BigDecimal r149_AMT_TIME) {
		R149_AMT_TIME = r149_AMT_TIME;
	}

	public String getR150_NAME_OF_BANK() {
		return R150_NAME_OF_BANK;
	}

	public void setR150_NAME_OF_BANK(String r150_NAME_OF_BANK) {
		R150_NAME_OF_BANK = r150_NAME_OF_BANK;
	}

	public String getR150_COUNTRY() {
		return R150_COUNTRY;
	}

	public void setR150_COUNTRY(String r150_COUNTRY) {
		R150_COUNTRY = r150_COUNTRY;
	}

	public String getR150_TYPE_OF_ACC() {
		return R150_TYPE_OF_ACC;
	}

	public void setR150_TYPE_OF_ACC(String r150_TYPE_OF_ACC) {
		R150_TYPE_OF_ACC = r150_TYPE_OF_ACC;
	}

	public String getR150_PURPOSE() {
		return R150_PURPOSE;
	}

	public void setR150_PURPOSE(String r150_PURPOSE) {
		R150_PURPOSE = r150_PURPOSE;
	}

	public String getR150_CURRENCY() {
		return R150_CURRENCY;
	}

	public void setR150_CURRENCY(String r150_CURRENCY) {
		R150_CURRENCY = r150_CURRENCY;
	}

	public String getR150_BANK_RATE() {
		return R150_BANK_RATE;
	}

	public void setR150_BANK_RATE(String r150_BANK_RATE) {
		R150_BANK_RATE = r150_BANK_RATE;
	}

	public BigDecimal getR150_AMT_DEMAND() {
		return R150_AMT_DEMAND;
	}

	public void setR150_AMT_DEMAND(BigDecimal r150_AMT_DEMAND) {
		R150_AMT_DEMAND = r150_AMT_DEMAND;
	}

	public BigDecimal getR150_AMT_TIME() {
		return R150_AMT_TIME;
	}

	public void setR150_AMT_TIME(BigDecimal r150_AMT_TIME) {
		R150_AMT_TIME = r150_AMT_TIME;
	}

	public String getR151_NAME_OF_BANK() {
		return R151_NAME_OF_BANK;
	}

	public void setR151_NAME_OF_BANK(String r151_NAME_OF_BANK) {
		R151_NAME_OF_BANK = r151_NAME_OF_BANK;
	}

	public String getR151_COUNTRY() {
		return R151_COUNTRY;
	}

	public void setR151_COUNTRY(String r151_COUNTRY) {
		R151_COUNTRY = r151_COUNTRY;
	}

	public String getR151_TYPE_OF_ACC() {
		return R151_TYPE_OF_ACC;
	}

	public void setR151_TYPE_OF_ACC(String r151_TYPE_OF_ACC) {
		R151_TYPE_OF_ACC = r151_TYPE_OF_ACC;
	}

	public String getR151_PURPOSE() {
		return R151_PURPOSE;
	}

	public void setR151_PURPOSE(String r151_PURPOSE) {
		R151_PURPOSE = r151_PURPOSE;
	}

	public String getR151_CURRENCY() {
		return R151_CURRENCY;
	}

	public void setR151_CURRENCY(String r151_CURRENCY) {
		R151_CURRENCY = r151_CURRENCY;
	}

	public String getR151_BANK_RATE() {
		return R151_BANK_RATE;
	}

	public void setR151_BANK_RATE(String r151_BANK_RATE) {
		R151_BANK_RATE = r151_BANK_RATE;
	}

	public BigDecimal getR151_AMT_DEMAND() {
		return R151_AMT_DEMAND;
	}

	public void setR151_AMT_DEMAND(BigDecimal r151_AMT_DEMAND) {
		R151_AMT_DEMAND = r151_AMT_DEMAND;
	}

	public BigDecimal getR151_AMT_TIME() {
		return R151_AMT_TIME;
	}

	public void setR151_AMT_TIME(BigDecimal r151_AMT_TIME) {
		R151_AMT_TIME = r151_AMT_TIME;
	}

	public String getR152_NAME_OF_BANK() {
		return R152_NAME_OF_BANK;
	}

	public void setR152_NAME_OF_BANK(String r152_NAME_OF_BANK) {
		R152_NAME_OF_BANK = r152_NAME_OF_BANK;
	}

	public String getR152_COUNTRY() {
		return R152_COUNTRY;
	}

	public void setR152_COUNTRY(String r152_COUNTRY) {
		R152_COUNTRY = r152_COUNTRY;
	}

	public String getR152_TYPE_OF_ACC() {
		return R152_TYPE_OF_ACC;
	}

	public void setR152_TYPE_OF_ACC(String r152_TYPE_OF_ACC) {
		R152_TYPE_OF_ACC = r152_TYPE_OF_ACC;
	}

	public String getR152_PURPOSE() {
		return R152_PURPOSE;
	}

	public void setR152_PURPOSE(String r152_PURPOSE) {
		R152_PURPOSE = r152_PURPOSE;
	}

	public String getR152_CURRENCY() {
		return R152_CURRENCY;
	}

	public void setR152_CURRENCY(String r152_CURRENCY) {
		R152_CURRENCY = r152_CURRENCY;
	}

	public String getR152_BANK_RATE() {
		return R152_BANK_RATE;
	}

	public void setR152_BANK_RATE(String r152_BANK_RATE) {
		R152_BANK_RATE = r152_BANK_RATE;
	}

	public BigDecimal getR152_AMT_DEMAND() {
		return R152_AMT_DEMAND;
	}

	public void setR152_AMT_DEMAND(BigDecimal r152_AMT_DEMAND) {
		R152_AMT_DEMAND = r152_AMT_DEMAND;
	}

	public BigDecimal getR152_AMT_TIME() {
		return R152_AMT_TIME;
	}

	public void setR152_AMT_TIME(BigDecimal r152_AMT_TIME) {
		R152_AMT_TIME = r152_AMT_TIME;
	}

	public String getR153_NAME_OF_BANK() {
		return R153_NAME_OF_BANK;
	}

	public void setR153_NAME_OF_BANK(String r153_NAME_OF_BANK) {
		R153_NAME_OF_BANK = r153_NAME_OF_BANK;
	}

	public String getR153_COUNTRY() {
		return R153_COUNTRY;
	}

	public void setR153_COUNTRY(String r153_COUNTRY) {
		R153_COUNTRY = r153_COUNTRY;
	}

	public String getR153_TYPE_OF_ACC() {
		return R153_TYPE_OF_ACC;
	}

	public void setR153_TYPE_OF_ACC(String r153_TYPE_OF_ACC) {
		R153_TYPE_OF_ACC = r153_TYPE_OF_ACC;
	}

	public String getR153_PURPOSE() {
		return R153_PURPOSE;
	}

	public void setR153_PURPOSE(String r153_PURPOSE) {
		R153_PURPOSE = r153_PURPOSE;
	}

	public String getR153_CURRENCY() {
		return R153_CURRENCY;
	}

	public void setR153_CURRENCY(String r153_CURRENCY) {
		R153_CURRENCY = r153_CURRENCY;
	}

	public String getR153_BANK_RATE() {
		return R153_BANK_RATE;
	}

	public void setR153_BANK_RATE(String r153_BANK_RATE) {
		R153_BANK_RATE = r153_BANK_RATE;
	}

	public BigDecimal getR153_AMT_DEMAND() {
		return R153_AMT_DEMAND;
	}

	public void setR153_AMT_DEMAND(BigDecimal r153_AMT_DEMAND) {
		R153_AMT_DEMAND = r153_AMT_DEMAND;
	}

	public BigDecimal getR153_AMT_TIME() {
		return R153_AMT_TIME;
	}

	public void setR153_AMT_TIME(BigDecimal r153_AMT_TIME) {
		R153_AMT_TIME = r153_AMT_TIME;
	}

	public String getR154_NAME_OF_BANK() {
		return R154_NAME_OF_BANK;
	}

	public void setR154_NAME_OF_BANK(String r154_NAME_OF_BANK) {
		R154_NAME_OF_BANK = r154_NAME_OF_BANK;
	}

	public String getR154_COUNTRY() {
		return R154_COUNTRY;
	}

	public void setR154_COUNTRY(String r154_COUNTRY) {
		R154_COUNTRY = r154_COUNTRY;
	}

	public String getR154_TYPE_OF_ACC() {
		return R154_TYPE_OF_ACC;
	}

	public void setR154_TYPE_OF_ACC(String r154_TYPE_OF_ACC) {
		R154_TYPE_OF_ACC = r154_TYPE_OF_ACC;
	}

	public String getR154_PURPOSE() {
		return R154_PURPOSE;
	}

	public void setR154_PURPOSE(String r154_PURPOSE) {
		R154_PURPOSE = r154_PURPOSE;
	}

	public String getR154_CURRENCY() {
		return R154_CURRENCY;
	}

	public void setR154_CURRENCY(String r154_CURRENCY) {
		R154_CURRENCY = r154_CURRENCY;
	}

	public String getR154_BANK_RATE() {
		return R154_BANK_RATE;
	}

	public void setR154_BANK_RATE(String r154_BANK_RATE) {
		R154_BANK_RATE = r154_BANK_RATE;
	}

	public BigDecimal getR154_AMT_DEMAND() {
		return R154_AMT_DEMAND;
	}

	public void setR154_AMT_DEMAND(BigDecimal r154_AMT_DEMAND) {
		R154_AMT_DEMAND = r154_AMT_DEMAND;
	}

	public BigDecimal getR154_AMT_TIME() {
		return R154_AMT_TIME;
	}

	public void setR154_AMT_TIME(BigDecimal r154_AMT_TIME) {
		R154_AMT_TIME = r154_AMT_TIME;
	}

	public String getR155_NAME_OF_BANK() {
		return R155_NAME_OF_BANK;
	}

	public void setR155_NAME_OF_BANK(String r155_NAME_OF_BANK) {
		R155_NAME_OF_BANK = r155_NAME_OF_BANK;
	}

	public String getR155_COUNTRY() {
		return R155_COUNTRY;
	}

	public void setR155_COUNTRY(String r155_COUNTRY) {
		R155_COUNTRY = r155_COUNTRY;
	}

	public String getR155_TYPE_OF_ACC() {
		return R155_TYPE_OF_ACC;
	}

	public void setR155_TYPE_OF_ACC(String r155_TYPE_OF_ACC) {
		R155_TYPE_OF_ACC = r155_TYPE_OF_ACC;
	}

	public String getR155_PURPOSE() {
		return R155_PURPOSE;
	}

	public void setR155_PURPOSE(String r155_PURPOSE) {
		R155_PURPOSE = r155_PURPOSE;
	}

	public String getR155_CURRENCY() {
		return R155_CURRENCY;
	}

	public void setR155_CURRENCY(String r155_CURRENCY) {
		R155_CURRENCY = r155_CURRENCY;
	}

	public String getR155_BANK_RATE() {
		return R155_BANK_RATE;
	}

	public void setR155_BANK_RATE(String r155_BANK_RATE) {
		R155_BANK_RATE = r155_BANK_RATE;
	}

	public BigDecimal getR155_AMT_DEMAND() {
		return R155_AMT_DEMAND;
	}

	public void setR155_AMT_DEMAND(BigDecimal r155_AMT_DEMAND) {
		R155_AMT_DEMAND = r155_AMT_DEMAND;
	}

	public BigDecimal getR155_AMT_TIME() {
		return R155_AMT_TIME;
	}

	public void setR155_AMT_TIME(BigDecimal r155_AMT_TIME) {
		R155_AMT_TIME = r155_AMT_TIME;
	}

	public String getR156_NAME_OF_BANK() {
		return R156_NAME_OF_BANK;
	}

	public void setR156_NAME_OF_BANK(String r156_NAME_OF_BANK) {
		R156_NAME_OF_BANK = r156_NAME_OF_BANK;
	}

	public String getR156_COUNTRY() {
		return R156_COUNTRY;
	}

	public void setR156_COUNTRY(String r156_COUNTRY) {
		R156_COUNTRY = r156_COUNTRY;
	}

	public String getR156_TYPE_OF_ACC() {
		return R156_TYPE_OF_ACC;
	}

	public void setR156_TYPE_OF_ACC(String r156_TYPE_OF_ACC) {
		R156_TYPE_OF_ACC = r156_TYPE_OF_ACC;
	}

	public String getR156_PURPOSE() {
		return R156_PURPOSE;
	}

	public void setR156_PURPOSE(String r156_PURPOSE) {
		R156_PURPOSE = r156_PURPOSE;
	}

	public String getR156_CURRENCY() {
		return R156_CURRENCY;
	}

	public void setR156_CURRENCY(String r156_CURRENCY) {
		R156_CURRENCY = r156_CURRENCY;
	}

	public String getR156_BANK_RATE() {
		return R156_BANK_RATE;
	}

	public void setR156_BANK_RATE(String r156_BANK_RATE) {
		R156_BANK_RATE = r156_BANK_RATE;
	}

	public BigDecimal getR156_AMT_DEMAND() {
		return R156_AMT_DEMAND;
	}

	public void setR156_AMT_DEMAND(BigDecimal r156_AMT_DEMAND) {
		R156_AMT_DEMAND = r156_AMT_DEMAND;
	}

	public BigDecimal getR156_AMT_TIME() {
		return R156_AMT_TIME;
	}

	public void setR156_AMT_TIME(BigDecimal r156_AMT_TIME) {
		R156_AMT_TIME = r156_AMT_TIME;
	}

	public String getR157_NAME_OF_BANK() {
		return R157_NAME_OF_BANK;
	}

	public void setR157_NAME_OF_BANK(String r157_NAME_OF_BANK) {
		R157_NAME_OF_BANK = r157_NAME_OF_BANK;
	}

	public String getR157_COUNTRY() {
		return R157_COUNTRY;
	}

	public void setR157_COUNTRY(String r157_COUNTRY) {
		R157_COUNTRY = r157_COUNTRY;
	}

	public String getR157_TYPE_OF_ACC() {
		return R157_TYPE_OF_ACC;
	}

	public void setR157_TYPE_OF_ACC(String r157_TYPE_OF_ACC) {
		R157_TYPE_OF_ACC = r157_TYPE_OF_ACC;
	}

	public String getR157_PURPOSE() {
		return R157_PURPOSE;
	}

	public void setR157_PURPOSE(String r157_PURPOSE) {
		R157_PURPOSE = r157_PURPOSE;
	}

	public String getR157_CURRENCY() {
		return R157_CURRENCY;
	}

	public void setR157_CURRENCY(String r157_CURRENCY) {
		R157_CURRENCY = r157_CURRENCY;
	}

	public String getR157_BANK_RATE() {
		return R157_BANK_RATE;
	}

	public void setR157_BANK_RATE(String r157_BANK_RATE) {
		R157_BANK_RATE = r157_BANK_RATE;
	}

	public BigDecimal getR157_AMT_DEMAND() {
		return R157_AMT_DEMAND;
	}

	public void setR157_AMT_DEMAND(BigDecimal r157_AMT_DEMAND) {
		R157_AMT_DEMAND = r157_AMT_DEMAND;
	}

	public BigDecimal getR157_AMT_TIME() {
		return R157_AMT_TIME;
	}

	public void setR157_AMT_TIME(BigDecimal r157_AMT_TIME) {
		R157_AMT_TIME = r157_AMT_TIME;
	}

	public String getR158_NAME_OF_BANK() {
		return R158_NAME_OF_BANK;
	}

	public void setR158_NAME_OF_BANK(String r158_NAME_OF_BANK) {
		R158_NAME_OF_BANK = r158_NAME_OF_BANK;
	}

	public String getR158_COUNTRY() {
		return R158_COUNTRY;
	}

	public void setR158_COUNTRY(String r158_COUNTRY) {
		R158_COUNTRY = r158_COUNTRY;
	}

	public String getR158_TYPE_OF_ACC() {
		return R158_TYPE_OF_ACC;
	}

	public void setR158_TYPE_OF_ACC(String r158_TYPE_OF_ACC) {
		R158_TYPE_OF_ACC = r158_TYPE_OF_ACC;
	}

	public String getR158_PURPOSE() {
		return R158_PURPOSE;
	}

	public void setR158_PURPOSE(String r158_PURPOSE) {
		R158_PURPOSE = r158_PURPOSE;
	}

	public String getR158_CURRENCY() {
		return R158_CURRENCY;
	}

	public void setR158_CURRENCY(String r158_CURRENCY) {
		R158_CURRENCY = r158_CURRENCY;
	}

	public String getR158_BANK_RATE() {
		return R158_BANK_RATE;
	}

	public void setR158_BANK_RATE(String r158_BANK_RATE) {
		R158_BANK_RATE = r158_BANK_RATE;
	}

	public BigDecimal getR158_AMT_DEMAND() {
		return R158_AMT_DEMAND;
	}

	public void setR158_AMT_DEMAND(BigDecimal r158_AMT_DEMAND) {
		R158_AMT_DEMAND = r158_AMT_DEMAND;
	}

	public BigDecimal getR158_AMT_TIME() {
		return R158_AMT_TIME;
	}

	public void setR158_AMT_TIME(BigDecimal r158_AMT_TIME) {
		R158_AMT_TIME = r158_AMT_TIME;
	}

	public String getR159_NAME_OF_BANK() {
		return R159_NAME_OF_BANK;
	}

	public void setR159_NAME_OF_BANK(String r159_NAME_OF_BANK) {
		R159_NAME_OF_BANK = r159_NAME_OF_BANK;
	}

	public String getR159_COUNTRY() {
		return R159_COUNTRY;
	}

	public void setR159_COUNTRY(String r159_COUNTRY) {
		R159_COUNTRY = r159_COUNTRY;
	}

	public String getR159_TYPE_OF_ACC() {
		return R159_TYPE_OF_ACC;
	}

	public void setR159_TYPE_OF_ACC(String r159_TYPE_OF_ACC) {
		R159_TYPE_OF_ACC = r159_TYPE_OF_ACC;
	}

	public String getR159_PURPOSE() {
		return R159_PURPOSE;
	}

	public void setR159_PURPOSE(String r159_PURPOSE) {
		R159_PURPOSE = r159_PURPOSE;
	}

	public String getR159_CURRENCY() {
		return R159_CURRENCY;
	}

	public void setR159_CURRENCY(String r159_CURRENCY) {
		R159_CURRENCY = r159_CURRENCY;
	}

	public String getR159_BANK_RATE() {
		return R159_BANK_RATE;
	}

	public void setR159_BANK_RATE(String r159_BANK_RATE) {
		R159_BANK_RATE = r159_BANK_RATE;
	}

	public BigDecimal getR159_AMT_DEMAND() {
		return R159_AMT_DEMAND;
	}

	public void setR159_AMT_DEMAND(BigDecimal r159_AMT_DEMAND) {
		R159_AMT_DEMAND = r159_AMT_DEMAND;
	}

	public BigDecimal getR159_AMT_TIME() {
		return R159_AMT_TIME;
	}

	public void setR159_AMT_TIME(BigDecimal r159_AMT_TIME) {
		R159_AMT_TIME = r159_AMT_TIME;
	}

	public String getR160_NAME_OF_BANK() {
		return R160_NAME_OF_BANK;
	}

	public void setR160_NAME_OF_BANK(String r160_NAME_OF_BANK) {
		R160_NAME_OF_BANK = r160_NAME_OF_BANK;
	}

	public String getR160_COUNTRY() {
		return R160_COUNTRY;
	}

	public void setR160_COUNTRY(String r160_COUNTRY) {
		R160_COUNTRY = r160_COUNTRY;
	}

	public String getR160_TYPE_OF_ACC() {
		return R160_TYPE_OF_ACC;
	}

	public void setR160_TYPE_OF_ACC(String r160_TYPE_OF_ACC) {
		R160_TYPE_OF_ACC = r160_TYPE_OF_ACC;
	}

	public String getR160_PURPOSE() {
		return R160_PURPOSE;
	}

	public void setR160_PURPOSE(String r160_PURPOSE) {
		R160_PURPOSE = r160_PURPOSE;
	}

	public String getR160_CURRENCY() {
		return R160_CURRENCY;
	}

	public void setR160_CURRENCY(String r160_CURRENCY) {
		R160_CURRENCY = r160_CURRENCY;
	}

	public String getR160_BANK_RATE() {
		return R160_BANK_RATE;
	}

	public void setR160_BANK_RATE(String r160_BANK_RATE) {
		R160_BANK_RATE = r160_BANK_RATE;
	}

	public BigDecimal getR160_AMT_DEMAND() {
		return R160_AMT_DEMAND;
	}

	public void setR160_AMT_DEMAND(BigDecimal r160_AMT_DEMAND) {
		R160_AMT_DEMAND = r160_AMT_DEMAND;
	}

	public BigDecimal getR160_AMT_TIME() {
		return R160_AMT_TIME;
	}

	public void setR160_AMT_TIME(BigDecimal r160_AMT_TIME) {
		R160_AMT_TIME = r160_AMT_TIME;
	}

	public String getR161_NAME_OF_BANK() {
		return R161_NAME_OF_BANK;
	}

	public void setR161_NAME_OF_BANK(String r161_NAME_OF_BANK) {
		R161_NAME_OF_BANK = r161_NAME_OF_BANK;
	}

	public String getR161_COUNTRY() {
		return R161_COUNTRY;
	}

	public void setR161_COUNTRY(String r161_COUNTRY) {
		R161_COUNTRY = r161_COUNTRY;
	}

	public String getR161_TYPE_OF_ACC() {
		return R161_TYPE_OF_ACC;
	}

	public void setR161_TYPE_OF_ACC(String r161_TYPE_OF_ACC) {
		R161_TYPE_OF_ACC = r161_TYPE_OF_ACC;
	}

	public String getR161_PURPOSE() {
		return R161_PURPOSE;
	}

	public void setR161_PURPOSE(String r161_PURPOSE) {
		R161_PURPOSE = r161_PURPOSE;
	}

	public String getR161_CURRENCY() {
		return R161_CURRENCY;
	}

	public void setR161_CURRENCY(String r161_CURRENCY) {
		R161_CURRENCY = r161_CURRENCY;
	}

	public String getR161_BANK_RATE() {
		return R161_BANK_RATE;
	}

	public void setR161_BANK_RATE(String r161_BANK_RATE) {
		R161_BANK_RATE = r161_BANK_RATE;
	}

	public BigDecimal getR161_AMT_DEMAND() {
		return R161_AMT_DEMAND;
	}

	public void setR161_AMT_DEMAND(BigDecimal r161_AMT_DEMAND) {
		R161_AMT_DEMAND = r161_AMT_DEMAND;
	}

	public BigDecimal getR161_AMT_TIME() {
		return R161_AMT_TIME;
	}

	public void setR161_AMT_TIME(BigDecimal r161_AMT_TIME) {
		R161_AMT_TIME = r161_AMT_TIME;
	}

	public String getR162_NAME_OF_BANK() {
		return R162_NAME_OF_BANK;
	}

	public void setR162_NAME_OF_BANK(String r162_NAME_OF_BANK) {
		R162_NAME_OF_BANK = r162_NAME_OF_BANK;
	}

	public String getR162_COUNTRY() {
		return R162_COUNTRY;
	}

	public void setR162_COUNTRY(String r162_COUNTRY) {
		R162_COUNTRY = r162_COUNTRY;
	}

	public String getR162_TYPE_OF_ACC() {
		return R162_TYPE_OF_ACC;
	}

	public void setR162_TYPE_OF_ACC(String r162_TYPE_OF_ACC) {
		R162_TYPE_OF_ACC = r162_TYPE_OF_ACC;
	}

	public String getR162_PURPOSE() {
		return R162_PURPOSE;
	}

	public void setR162_PURPOSE(String r162_PURPOSE) {
		R162_PURPOSE = r162_PURPOSE;
	}

	public String getR162_CURRENCY() {
		return R162_CURRENCY;
	}

	public void setR162_CURRENCY(String r162_CURRENCY) {
		R162_CURRENCY = r162_CURRENCY;
	}

	public String getR162_BANK_RATE() {
		return R162_BANK_RATE;
	}

	public void setR162_BANK_RATE(String r162_BANK_RATE) {
		R162_BANK_RATE = r162_BANK_RATE;
	}

	public BigDecimal getR162_AMT_DEMAND() {
		return R162_AMT_DEMAND;
	}

	public void setR162_AMT_DEMAND(BigDecimal r162_AMT_DEMAND) {
		R162_AMT_DEMAND = r162_AMT_DEMAND;
	}

	public BigDecimal getR162_AMT_TIME() {
		return R162_AMT_TIME;
	}

	public void setR162_AMT_TIME(BigDecimal r162_AMT_TIME) {
		R162_AMT_TIME = r162_AMT_TIME;
	}

	public String getR163_NAME_OF_BANK() {
		return R163_NAME_OF_BANK;
	}

	public void setR163_NAME_OF_BANK(String r163_NAME_OF_BANK) {
		R163_NAME_OF_BANK = r163_NAME_OF_BANK;
	}

	public String getR163_COUNTRY() {
		return R163_COUNTRY;
	}

	public void setR163_COUNTRY(String r163_COUNTRY) {
		R163_COUNTRY = r163_COUNTRY;
	}

	public String getR163_TYPE_OF_ACC() {
		return R163_TYPE_OF_ACC;
	}

	public void setR163_TYPE_OF_ACC(String r163_TYPE_OF_ACC) {
		R163_TYPE_OF_ACC = r163_TYPE_OF_ACC;
	}

	public String getR163_PURPOSE() {
		return R163_PURPOSE;
	}

	public void setR163_PURPOSE(String r163_PURPOSE) {
		R163_PURPOSE = r163_PURPOSE;
	}

	public String getR163_CURRENCY() {
		return R163_CURRENCY;
	}

	public void setR163_CURRENCY(String r163_CURRENCY) {
		R163_CURRENCY = r163_CURRENCY;
	}

	public String getR163_BANK_RATE() {
		return R163_BANK_RATE;
	}

	public void setR163_BANK_RATE(String r163_BANK_RATE) {
		R163_BANK_RATE = r163_BANK_RATE;
	}

	public BigDecimal getR163_AMT_DEMAND() {
		return R163_AMT_DEMAND;
	}

	public void setR163_AMT_DEMAND(BigDecimal r163_AMT_DEMAND) {
		R163_AMT_DEMAND = r163_AMT_DEMAND;
	}

	public BigDecimal getR163_AMT_TIME() {
		return R163_AMT_TIME;
	}

	public void setR163_AMT_TIME(BigDecimal r163_AMT_TIME) {
		R163_AMT_TIME = r163_AMT_TIME;
	}

	public String getR164_NAME_OF_BANK() {
		return R164_NAME_OF_BANK;
	}

	public void setR164_NAME_OF_BANK(String r164_NAME_OF_BANK) {
		R164_NAME_OF_BANK = r164_NAME_OF_BANK;
	}

	public String getR164_COUNTRY() {
		return R164_COUNTRY;
	}

	public void setR164_COUNTRY(String r164_COUNTRY) {
		R164_COUNTRY = r164_COUNTRY;
	}

	public String getR164_TYPE_OF_ACC() {
		return R164_TYPE_OF_ACC;
	}

	public void setR164_TYPE_OF_ACC(String r164_TYPE_OF_ACC) {
		R164_TYPE_OF_ACC = r164_TYPE_OF_ACC;
	}

	public String getR164_PURPOSE() {
		return R164_PURPOSE;
	}

	public void setR164_PURPOSE(String r164_PURPOSE) {
		R164_PURPOSE = r164_PURPOSE;
	}

	public String getR164_CURRENCY() {
		return R164_CURRENCY;
	}

	public void setR164_CURRENCY(String r164_CURRENCY) {
		R164_CURRENCY = r164_CURRENCY;
	}

	public String getR164_BANK_RATE() {
		return R164_BANK_RATE;
	}

	public void setR164_BANK_RATE(String r164_BANK_RATE) {
		R164_BANK_RATE = r164_BANK_RATE;
	}

	public BigDecimal getR164_AMT_DEMAND() {
		return R164_AMT_DEMAND;
	}

	public void setR164_AMT_DEMAND(BigDecimal r164_AMT_DEMAND) {
		R164_AMT_DEMAND = r164_AMT_DEMAND;
	}

	public BigDecimal getR164_AMT_TIME() {
		return R164_AMT_TIME;
	}

	public void setR164_AMT_TIME(BigDecimal r164_AMT_TIME) {
		R164_AMT_TIME = r164_AMT_TIME;
	}

	public String getR165_NAME_OF_BANK() {
		return R165_NAME_OF_BANK;
	}

	public void setR165_NAME_OF_BANK(String r165_NAME_OF_BANK) {
		R165_NAME_OF_BANK = r165_NAME_OF_BANK;
	}

	public String getR165_COUNTRY() {
		return R165_COUNTRY;
	}

	public void setR165_COUNTRY(String r165_COUNTRY) {
		R165_COUNTRY = r165_COUNTRY;
	}

	public String getR165_TYPE_OF_ACC() {
		return R165_TYPE_OF_ACC;
	}

	public void setR165_TYPE_OF_ACC(String r165_TYPE_OF_ACC) {
		R165_TYPE_OF_ACC = r165_TYPE_OF_ACC;
	}

	public String getR165_PURPOSE() {
		return R165_PURPOSE;
	}

	public void setR165_PURPOSE(String r165_PURPOSE) {
		R165_PURPOSE = r165_PURPOSE;
	}

	public String getR165_CURRENCY() {
		return R165_CURRENCY;
	}

	public void setR165_CURRENCY(String r165_CURRENCY) {
		R165_CURRENCY = r165_CURRENCY;
	}

	public String getR165_BANK_RATE() {
		return R165_BANK_RATE;
	}

	public void setR165_BANK_RATE(String r165_BANK_RATE) {
		R165_BANK_RATE = r165_BANK_RATE;
	}

	public BigDecimal getR165_AMT_DEMAND() {
		return R165_AMT_DEMAND;
	}

	public void setR165_AMT_DEMAND(BigDecimal r165_AMT_DEMAND) {
		R165_AMT_DEMAND = r165_AMT_DEMAND;
	}

	public BigDecimal getR165_AMT_TIME() {
		return R165_AMT_TIME;
	}

	public void setR165_AMT_TIME(BigDecimal r165_AMT_TIME) {
		R165_AMT_TIME = r165_AMT_TIME;
	}

	public String getR166_NAME_OF_BANK() {
		return R166_NAME_OF_BANK;
	}

	public void setR166_NAME_OF_BANK(String r166_NAME_OF_BANK) {
		R166_NAME_OF_BANK = r166_NAME_OF_BANK;
	}

	public String getR166_COUNTRY() {
		return R166_COUNTRY;
	}

	public void setR166_COUNTRY(String r166_COUNTRY) {
		R166_COUNTRY = r166_COUNTRY;
	}

	public String getR166_TYPE_OF_ACC() {
		return R166_TYPE_OF_ACC;
	}

	public void setR166_TYPE_OF_ACC(String r166_TYPE_OF_ACC) {
		R166_TYPE_OF_ACC = r166_TYPE_OF_ACC;
	}

	public String getR166_PURPOSE() {
		return R166_PURPOSE;
	}

	public void setR166_PURPOSE(String r166_PURPOSE) {
		R166_PURPOSE = r166_PURPOSE;
	}

	public String getR166_CURRENCY() {
		return R166_CURRENCY;
	}

	public void setR166_CURRENCY(String r166_CURRENCY) {
		R166_CURRENCY = r166_CURRENCY;
	}

	public String getR166_BANK_RATE() {
		return R166_BANK_RATE;
	}

	public void setR166_BANK_RATE(String r166_BANK_RATE) {
		R166_BANK_RATE = r166_BANK_RATE;
	}

	public BigDecimal getR166_AMT_DEMAND() {
		return R166_AMT_DEMAND;
	}

	public void setR166_AMT_DEMAND(BigDecimal r166_AMT_DEMAND) {
		R166_AMT_DEMAND = r166_AMT_DEMAND;
	}

	public BigDecimal getR166_AMT_TIME() {
		return R166_AMT_TIME;
	}

	public void setR166_AMT_TIME(BigDecimal r166_AMT_TIME) {
		R166_AMT_TIME = r166_AMT_TIME;
	}

	public String getR167_NAME_OF_BANK() {
		return R167_NAME_OF_BANK;
	}

	public void setR167_NAME_OF_BANK(String r167_NAME_OF_BANK) {
		R167_NAME_OF_BANK = r167_NAME_OF_BANK;
	}

	public String getR167_COUNTRY() {
		return R167_COUNTRY;
	}

	public void setR167_COUNTRY(String r167_COUNTRY) {
		R167_COUNTRY = r167_COUNTRY;
	}

	public String getR167_TYPE_OF_ACC() {
		return R167_TYPE_OF_ACC;
	}

	public void setR167_TYPE_OF_ACC(String r167_TYPE_OF_ACC) {
		R167_TYPE_OF_ACC = r167_TYPE_OF_ACC;
	}

	public String getR167_PURPOSE() {
		return R167_PURPOSE;
	}

	public void setR167_PURPOSE(String r167_PURPOSE) {
		R167_PURPOSE = r167_PURPOSE;
	}

	public String getR167_CURRENCY() {
		return R167_CURRENCY;
	}

	public void setR167_CURRENCY(String r167_CURRENCY) {
		R167_CURRENCY = r167_CURRENCY;
	}

	public String getR167_BANK_RATE() {
		return R167_BANK_RATE;
	}

	public void setR167_BANK_RATE(String r167_BANK_RATE) {
		R167_BANK_RATE = r167_BANK_RATE;
	}

	public BigDecimal getR167_AMT_DEMAND() {
		return R167_AMT_DEMAND;
	}

	public void setR167_AMT_DEMAND(BigDecimal r167_AMT_DEMAND) {
		R167_AMT_DEMAND = r167_AMT_DEMAND;
	}

	public BigDecimal getR167_AMT_TIME() {
		return R167_AMT_TIME;
	}

	public void setR167_AMT_TIME(BigDecimal r167_AMT_TIME) {
		R167_AMT_TIME = r167_AMT_TIME;
	}

	public String getR168_NAME_OF_BANK() {
		return R168_NAME_OF_BANK;
	}

	public void setR168_NAME_OF_BANK(String r168_NAME_OF_BANK) {
		R168_NAME_OF_BANK = r168_NAME_OF_BANK;
	}

	public String getR168_COUNTRY() {
		return R168_COUNTRY;
	}

	public void setR168_COUNTRY(String r168_COUNTRY) {
		R168_COUNTRY = r168_COUNTRY;
	}

	public String getR168_TYPE_OF_ACC() {
		return R168_TYPE_OF_ACC;
	}

	public void setR168_TYPE_OF_ACC(String r168_TYPE_OF_ACC) {
		R168_TYPE_OF_ACC = r168_TYPE_OF_ACC;
	}

	public String getR168_PURPOSE() {
		return R168_PURPOSE;
	}

	public void setR168_PURPOSE(String r168_PURPOSE) {
		R168_PURPOSE = r168_PURPOSE;
	}

	public String getR168_CURRENCY() {
		return R168_CURRENCY;
	}

	public void setR168_CURRENCY(String r168_CURRENCY) {
		R168_CURRENCY = r168_CURRENCY;
	}

	public String getR168_BANK_RATE() {
		return R168_BANK_RATE;
	}

	public void setR168_BANK_RATE(String r168_BANK_RATE) {
		R168_BANK_RATE = r168_BANK_RATE;
	}

	public BigDecimal getR168_AMT_DEMAND() {
		return R168_AMT_DEMAND;
	}

	public void setR168_AMT_DEMAND(BigDecimal r168_AMT_DEMAND) {
		R168_AMT_DEMAND = r168_AMT_DEMAND;
	}

	public BigDecimal getR168_AMT_TIME() {
		return R168_AMT_TIME;
	}

	public void setR168_AMT_TIME(BigDecimal r168_AMT_TIME) {
		R168_AMT_TIME = r168_AMT_TIME;
	}

	public String getR169_NAME_OF_BANK() {
		return R169_NAME_OF_BANK;
	}

	public void setR169_NAME_OF_BANK(String r169_NAME_OF_BANK) {
		R169_NAME_OF_BANK = r169_NAME_OF_BANK;
	}

	public String getR169_COUNTRY() {
		return R169_COUNTRY;
	}

	public void setR169_COUNTRY(String r169_COUNTRY) {
		R169_COUNTRY = r169_COUNTRY;
	}

	public String getR169_TYPE_OF_ACC() {
		return R169_TYPE_OF_ACC;
	}

	public void setR169_TYPE_OF_ACC(String r169_TYPE_OF_ACC) {
		R169_TYPE_OF_ACC = r169_TYPE_OF_ACC;
	}

	public String getR169_PURPOSE() {
		return R169_PURPOSE;
	}

	public void setR169_PURPOSE(String r169_PURPOSE) {
		R169_PURPOSE = r169_PURPOSE;
	}

	public String getR169_CURRENCY() {
		return R169_CURRENCY;
	}

	public void setR169_CURRENCY(String r169_CURRENCY) {
		R169_CURRENCY = r169_CURRENCY;
	}

	public String getR169_BANK_RATE() {
		return R169_BANK_RATE;
	}

	public void setR169_BANK_RATE(String r169_BANK_RATE) {
		R169_BANK_RATE = r169_BANK_RATE;
	}

	public BigDecimal getR169_AMT_DEMAND() {
		return R169_AMT_DEMAND;
	}

	public void setR169_AMT_DEMAND(BigDecimal r169_AMT_DEMAND) {
		R169_AMT_DEMAND = r169_AMT_DEMAND;
	}

	public BigDecimal getR169_AMT_TIME() {
		return R169_AMT_TIME;
	}

	public void setR169_AMT_TIME(BigDecimal r169_AMT_TIME) {
		R169_AMT_TIME = r169_AMT_TIME;
	}

	public String getR170_NAME_OF_BANK() {
		return R170_NAME_OF_BANK;
	}

	public void setR170_NAME_OF_BANK(String r170_NAME_OF_BANK) {
		R170_NAME_OF_BANK = r170_NAME_OF_BANK;
	}

	public String getR170_COUNTRY() {
		return R170_COUNTRY;
	}

	public void setR170_COUNTRY(String r170_COUNTRY) {
		R170_COUNTRY = r170_COUNTRY;
	}

	public String getR170_TYPE_OF_ACC() {
		return R170_TYPE_OF_ACC;
	}

	public void setR170_TYPE_OF_ACC(String r170_TYPE_OF_ACC) {
		R170_TYPE_OF_ACC = r170_TYPE_OF_ACC;
	}

	public String getR170_PURPOSE() {
		return R170_PURPOSE;
	}

	public void setR170_PURPOSE(String r170_PURPOSE) {
		R170_PURPOSE = r170_PURPOSE;
	}

	public String getR170_CURRENCY() {
		return R170_CURRENCY;
	}

	public void setR170_CURRENCY(String r170_CURRENCY) {
		R170_CURRENCY = r170_CURRENCY;
	}

	public String getR170_BANK_RATE() {
		return R170_BANK_RATE;
	}

	public void setR170_BANK_RATE(String r170_BANK_RATE) {
		R170_BANK_RATE = r170_BANK_RATE;
	}

	public BigDecimal getR170_AMT_DEMAND() {
		return R170_AMT_DEMAND;
	}

	public void setR170_AMT_DEMAND(BigDecimal r170_AMT_DEMAND) {
		R170_AMT_DEMAND = r170_AMT_DEMAND;
	}

	public BigDecimal getR170_AMT_TIME() {
		return R170_AMT_TIME;
	}

	public void setR170_AMT_TIME(BigDecimal r170_AMT_TIME) {
		R170_AMT_TIME = r170_AMT_TIME;
	}

	public String getR171_NAME_OF_BANK() {
		return R171_NAME_OF_BANK;
	}

	public void setR171_NAME_OF_BANK(String r171_NAME_OF_BANK) {
		R171_NAME_OF_BANK = r171_NAME_OF_BANK;
	}

	public String getR171_COUNTRY() {
		return R171_COUNTRY;
	}

	public void setR171_COUNTRY(String r171_COUNTRY) {
		R171_COUNTRY = r171_COUNTRY;
	}

	public String getR171_TYPE_OF_ACC() {
		return R171_TYPE_OF_ACC;
	}

	public void setR171_TYPE_OF_ACC(String r171_TYPE_OF_ACC) {
		R171_TYPE_OF_ACC = r171_TYPE_OF_ACC;
	}

	public String getR171_PURPOSE() {
		return R171_PURPOSE;
	}

	public void setR171_PURPOSE(String r171_PURPOSE) {
		R171_PURPOSE = r171_PURPOSE;
	}

	public String getR171_CURRENCY() {
		return R171_CURRENCY;
	}

	public void setR171_CURRENCY(String r171_CURRENCY) {
		R171_CURRENCY = r171_CURRENCY;
	}

	public String getR171_BANK_RATE() {
		return R171_BANK_RATE;
	}

	public void setR171_BANK_RATE(String r171_BANK_RATE) {
		R171_BANK_RATE = r171_BANK_RATE;
	}

	public BigDecimal getR171_AMT_DEMAND() {
		return R171_AMT_DEMAND;
	}

	public void setR171_AMT_DEMAND(BigDecimal r171_AMT_DEMAND) {
		R171_AMT_DEMAND = r171_AMT_DEMAND;
	}

	public BigDecimal getR171_AMT_TIME() {
		return R171_AMT_TIME;
	}

	public void setR171_AMT_TIME(BigDecimal r171_AMT_TIME) {
		R171_AMT_TIME = r171_AMT_TIME;
	}

	public String getR172_NAME_OF_BANK() {
		return R172_NAME_OF_BANK;
	}

	public void setR172_NAME_OF_BANK(String r172_NAME_OF_BANK) {
		R172_NAME_OF_BANK = r172_NAME_OF_BANK;
	}

	public String getR172_COUNTRY() {
		return R172_COUNTRY;
	}

	public void setR172_COUNTRY(String r172_COUNTRY) {
		R172_COUNTRY = r172_COUNTRY;
	}

	public String getR172_TYPE_OF_ACC() {
		return R172_TYPE_OF_ACC;
	}

	public void setR172_TYPE_OF_ACC(String r172_TYPE_OF_ACC) {
		R172_TYPE_OF_ACC = r172_TYPE_OF_ACC;
	}

	public String getR172_PURPOSE() {
		return R172_PURPOSE;
	}

	public void setR172_PURPOSE(String r172_PURPOSE) {
		R172_PURPOSE = r172_PURPOSE;
	}

	public String getR172_CURRENCY() {
		return R172_CURRENCY;
	}

	public void setR172_CURRENCY(String r172_CURRENCY) {
		R172_CURRENCY = r172_CURRENCY;
	}

	public String getR172_BANK_RATE() {
		return R172_BANK_RATE;
	}

	public void setR172_BANK_RATE(String r172_BANK_RATE) {
		R172_BANK_RATE = r172_BANK_RATE;
	}

	public BigDecimal getR172_AMT_DEMAND() {
		return R172_AMT_DEMAND;
	}

	public void setR172_AMT_DEMAND(BigDecimal r172_AMT_DEMAND) {
		R172_AMT_DEMAND = r172_AMT_DEMAND;
	}

	public BigDecimal getR172_AMT_TIME() {
		return R172_AMT_TIME;
	}

	public void setR172_AMT_TIME(BigDecimal r172_AMT_TIME) {
		R172_AMT_TIME = r172_AMT_TIME;
	}

	public String getR173_NAME_OF_BANK() {
		return R173_NAME_OF_BANK;
	}

	public void setR173_NAME_OF_BANK(String r173_NAME_OF_BANK) {
		R173_NAME_OF_BANK = r173_NAME_OF_BANK;
	}

	public String getR173_COUNTRY() {
		return R173_COUNTRY;
	}

	public void setR173_COUNTRY(String r173_COUNTRY) {
		R173_COUNTRY = r173_COUNTRY;
	}

	public String getR173_TYPE_OF_ACC() {
		return R173_TYPE_OF_ACC;
	}

	public void setR173_TYPE_OF_ACC(String r173_TYPE_OF_ACC) {
		R173_TYPE_OF_ACC = r173_TYPE_OF_ACC;
	}

	public String getR173_PURPOSE() {
		return R173_PURPOSE;
	}

	public void setR173_PURPOSE(String r173_PURPOSE) {
		R173_PURPOSE = r173_PURPOSE;
	}

	public String getR173_CURRENCY() {
		return R173_CURRENCY;
	}

	public void setR173_CURRENCY(String r173_CURRENCY) {
		R173_CURRENCY = r173_CURRENCY;
	}

	public String getR173_BANK_RATE() {
		return R173_BANK_RATE;
	}

	public void setR173_BANK_RATE(String r173_BANK_RATE) {
		R173_BANK_RATE = r173_BANK_RATE;
	}

	public BigDecimal getR173_AMT_DEMAND() {
		return R173_AMT_DEMAND;
	}

	public void setR173_AMT_DEMAND(BigDecimal r173_AMT_DEMAND) {
		R173_AMT_DEMAND = r173_AMT_DEMAND;
	}

	public BigDecimal getR173_AMT_TIME() {
		return R173_AMT_TIME;
	}

	public void setR173_AMT_TIME(BigDecimal r173_AMT_TIME) {
		R173_AMT_TIME = r173_AMT_TIME;
	}

	public String getR174_NAME_OF_BANK() {
		return R174_NAME_OF_BANK;
	}

	public void setR174_NAME_OF_BANK(String r174_NAME_OF_BANK) {
		R174_NAME_OF_BANK = r174_NAME_OF_BANK;
	}

	public String getR174_COUNTRY() {
		return R174_COUNTRY;
	}

	public void setR174_COUNTRY(String r174_COUNTRY) {
		R174_COUNTRY = r174_COUNTRY;
	}

	public String getR174_TYPE_OF_ACC() {
		return R174_TYPE_OF_ACC;
	}

	public void setR174_TYPE_OF_ACC(String r174_TYPE_OF_ACC) {
		R174_TYPE_OF_ACC = r174_TYPE_OF_ACC;
	}

	public String getR174_PURPOSE() {
		return R174_PURPOSE;
	}

	public void setR174_PURPOSE(String r174_PURPOSE) {
		R174_PURPOSE = r174_PURPOSE;
	}

	public String getR174_CURRENCY() {
		return R174_CURRENCY;
	}

	public void setR174_CURRENCY(String r174_CURRENCY) {
		R174_CURRENCY = r174_CURRENCY;
	}

	public String getR174_BANK_RATE() {
		return R174_BANK_RATE;
	}

	public void setR174_BANK_RATE(String r174_BANK_RATE) {
		R174_BANK_RATE = r174_BANK_RATE;
	}

	public BigDecimal getR174_AMT_DEMAND() {
		return R174_AMT_DEMAND;
	}

	public void setR174_AMT_DEMAND(BigDecimal r174_AMT_DEMAND) {
		R174_AMT_DEMAND = r174_AMT_DEMAND;
	}

	public BigDecimal getR174_AMT_TIME() {
		return R174_AMT_TIME;
	}

	public void setR174_AMT_TIME(BigDecimal r174_AMT_TIME) {
		R174_AMT_TIME = r174_AMT_TIME;
	}

	public String getR175_NAME_OF_BANK() {
		return R175_NAME_OF_BANK;
	}

	public void setR175_NAME_OF_BANK(String r175_NAME_OF_BANK) {
		R175_NAME_OF_BANK = r175_NAME_OF_BANK;
	}

	public String getR175_COUNTRY() {
		return R175_COUNTRY;
	}

	public void setR175_COUNTRY(String r175_COUNTRY) {
		R175_COUNTRY = r175_COUNTRY;
	}

	public String getR175_TYPE_OF_ACC() {
		return R175_TYPE_OF_ACC;
	}

	public void setR175_TYPE_OF_ACC(String r175_TYPE_OF_ACC) {
		R175_TYPE_OF_ACC = r175_TYPE_OF_ACC;
	}

	public String getR175_PURPOSE() {
		return R175_PURPOSE;
	}

	public void setR175_PURPOSE(String r175_PURPOSE) {
		R175_PURPOSE = r175_PURPOSE;
	}

	public String getR175_CURRENCY() {
		return R175_CURRENCY;
	}

	public void setR175_CURRENCY(String r175_CURRENCY) {
		R175_CURRENCY = r175_CURRENCY;
	}

	public String getR175_BANK_RATE() {
		return R175_BANK_RATE;
	}

	public void setR175_BANK_RATE(String r175_BANK_RATE) {
		R175_BANK_RATE = r175_BANK_RATE;
	}

	public BigDecimal getR175_AMT_DEMAND() {
		return R175_AMT_DEMAND;
	}

	public void setR175_AMT_DEMAND(BigDecimal r175_AMT_DEMAND) {
		R175_AMT_DEMAND = r175_AMT_DEMAND;
	}

	public BigDecimal getR175_AMT_TIME() {
		return R175_AMT_TIME;
	}

	public void setR175_AMT_TIME(BigDecimal r175_AMT_TIME) {
		R175_AMT_TIME = r175_AMT_TIME;
	}

	public String getR176_NAME_OF_BANK() {
		return R176_NAME_OF_BANK;
	}

	public void setR176_NAME_OF_BANK(String r176_NAME_OF_BANK) {
		R176_NAME_OF_BANK = r176_NAME_OF_BANK;
	}

	public String getR176_COUNTRY() {
		return R176_COUNTRY;
	}

	public void setR176_COUNTRY(String r176_COUNTRY) {
		R176_COUNTRY = r176_COUNTRY;
	}

	public String getR176_TYPE_OF_ACC() {
		return R176_TYPE_OF_ACC;
	}

	public void setR176_TYPE_OF_ACC(String r176_TYPE_OF_ACC) {
		R176_TYPE_OF_ACC = r176_TYPE_OF_ACC;
	}

	public String getR176_PURPOSE() {
		return R176_PURPOSE;
	}

	public void setR176_PURPOSE(String r176_PURPOSE) {
		R176_PURPOSE = r176_PURPOSE;
	}

	public String getR176_CURRENCY() {
		return R176_CURRENCY;
	}

	public void setR176_CURRENCY(String r176_CURRENCY) {
		R176_CURRENCY = r176_CURRENCY;
	}

	public String getR176_BANK_RATE() {
		return R176_BANK_RATE;
	}

	public void setR176_BANK_RATE(String r176_BANK_RATE) {
		R176_BANK_RATE = r176_BANK_RATE;
	}

	public BigDecimal getR176_AMT_DEMAND() {
		return R176_AMT_DEMAND;
	}

	public void setR176_AMT_DEMAND(BigDecimal r176_AMT_DEMAND) {
		R176_AMT_DEMAND = r176_AMT_DEMAND;
	}

	public BigDecimal getR176_AMT_TIME() {
		return R176_AMT_TIME;
	}

	public void setR176_AMT_TIME(BigDecimal r176_AMT_TIME) {
		R176_AMT_TIME = r176_AMT_TIME;
	}

	public String getR177_NAME_OF_BANK() {
		return R177_NAME_OF_BANK;
	}

	public void setR177_NAME_OF_BANK(String r177_NAME_OF_BANK) {
		R177_NAME_OF_BANK = r177_NAME_OF_BANK;
	}

	public String getR177_COUNTRY() {
		return R177_COUNTRY;
	}

	public void setR177_COUNTRY(String r177_COUNTRY) {
		R177_COUNTRY = r177_COUNTRY;
	}

	public String getR177_TYPE_OF_ACC() {
		return R177_TYPE_OF_ACC;
	}

	public void setR177_TYPE_OF_ACC(String r177_TYPE_OF_ACC) {
		R177_TYPE_OF_ACC = r177_TYPE_OF_ACC;
	}

	public String getR177_PURPOSE() {
		return R177_PURPOSE;
	}

	public void setR177_PURPOSE(String r177_PURPOSE) {
		R177_PURPOSE = r177_PURPOSE;
	}

	public String getR177_CURRENCY() {
		return R177_CURRENCY;
	}

	public void setR177_CURRENCY(String r177_CURRENCY) {
		R177_CURRENCY = r177_CURRENCY;
	}

	public String getR177_BANK_RATE() {
		return R177_BANK_RATE;
	}

	public void setR177_BANK_RATE(String r177_BANK_RATE) {
		R177_BANK_RATE = r177_BANK_RATE;
	}

	public BigDecimal getR177_AMT_DEMAND() {
		return R177_AMT_DEMAND;
	}

	public void setR177_AMT_DEMAND(BigDecimal r177_AMT_DEMAND) {
		R177_AMT_DEMAND = r177_AMT_DEMAND;
	}

	public BigDecimal getR177_AMT_TIME() {
		return R177_AMT_TIME;
	}

	public void setR177_AMT_TIME(BigDecimal r177_AMT_TIME) {
		R177_AMT_TIME = r177_AMT_TIME;
	}

	public String getR178_NAME_OF_BANK() {
		return R178_NAME_OF_BANK;
	}

	public void setR178_NAME_OF_BANK(String r178_NAME_OF_BANK) {
		R178_NAME_OF_BANK = r178_NAME_OF_BANK;
	}

	public String getR178_COUNTRY() {
		return R178_COUNTRY;
	}

	public void setR178_COUNTRY(String r178_COUNTRY) {
		R178_COUNTRY = r178_COUNTRY;
	}

	public String getR178_TYPE_OF_ACC() {
		return R178_TYPE_OF_ACC;
	}

	public void setR178_TYPE_OF_ACC(String r178_TYPE_OF_ACC) {
		R178_TYPE_OF_ACC = r178_TYPE_OF_ACC;
	}

	public String getR178_PURPOSE() {
		return R178_PURPOSE;
	}

	public void setR178_PURPOSE(String r178_PURPOSE) {
		R178_PURPOSE = r178_PURPOSE;
	}

	public String getR178_CURRENCY() {
		return R178_CURRENCY;
	}

	public void setR178_CURRENCY(String r178_CURRENCY) {
		R178_CURRENCY = r178_CURRENCY;
	}

	public String getR178_BANK_RATE() {
		return R178_BANK_RATE;
	}

	public void setR178_BANK_RATE(String r178_BANK_RATE) {
		R178_BANK_RATE = r178_BANK_RATE;
	}

	public BigDecimal getR178_AMT_DEMAND() {
		return R178_AMT_DEMAND;
	}

	public void setR178_AMT_DEMAND(BigDecimal r178_AMT_DEMAND) {
		R178_AMT_DEMAND = r178_AMT_DEMAND;
	}

	public BigDecimal getR178_AMT_TIME() {
		return R178_AMT_TIME;
	}

	public void setR178_AMT_TIME(BigDecimal r178_AMT_TIME) {
		R178_AMT_TIME = r178_AMT_TIME;
	}

	public String getR179_NAME_OF_BANK() {
		return R179_NAME_OF_BANK;
	}

	public void setR179_NAME_OF_BANK(String r179_NAME_OF_BANK) {
		R179_NAME_OF_BANK = r179_NAME_OF_BANK;
	}

	public String getR179_COUNTRY() {
		return R179_COUNTRY;
	}

	public void setR179_COUNTRY(String r179_COUNTRY) {
		R179_COUNTRY = r179_COUNTRY;
	}

	public String getR179_TYPE_OF_ACC() {
		return R179_TYPE_OF_ACC;
	}

	public void setR179_TYPE_OF_ACC(String r179_TYPE_OF_ACC) {
		R179_TYPE_OF_ACC = r179_TYPE_OF_ACC;
	}

	public String getR179_PURPOSE() {
		return R179_PURPOSE;
	}

	public void setR179_PURPOSE(String r179_PURPOSE) {
		R179_PURPOSE = r179_PURPOSE;
	}

	public String getR179_CURRENCY() {
		return R179_CURRENCY;
	}

	public void setR179_CURRENCY(String r179_CURRENCY) {
		R179_CURRENCY = r179_CURRENCY;
	}

	public String getR179_BANK_RATE() {
		return R179_BANK_RATE;
	}

	public void setR179_BANK_RATE(String r179_BANK_RATE) {
		R179_BANK_RATE = r179_BANK_RATE;
	}

	public BigDecimal getR179_AMT_DEMAND() {
		return R179_AMT_DEMAND;
	}

	public void setR179_AMT_DEMAND(BigDecimal r179_AMT_DEMAND) {
		R179_AMT_DEMAND = r179_AMT_DEMAND;
	}

	public BigDecimal getR179_AMT_TIME() {
		return R179_AMT_TIME;
	}

	public void setR179_AMT_TIME(BigDecimal r179_AMT_TIME) {
		R179_AMT_TIME = r179_AMT_TIME;
	}

	public String getR180_NAME_OF_BANK() {
		return R180_NAME_OF_BANK;
	}

	public void setR180_NAME_OF_BANK(String r180_NAME_OF_BANK) {
		R180_NAME_OF_BANK = r180_NAME_OF_BANK;
	}

	public String getR180_COUNTRY() {
		return R180_COUNTRY;
	}

	public void setR180_COUNTRY(String r180_COUNTRY) {
		R180_COUNTRY = r180_COUNTRY;
	}

	public String getR180_TYPE_OF_ACC() {
		return R180_TYPE_OF_ACC;
	}

	public void setR180_TYPE_OF_ACC(String r180_TYPE_OF_ACC) {
		R180_TYPE_OF_ACC = r180_TYPE_OF_ACC;
	}

	public String getR180_PURPOSE() {
		return R180_PURPOSE;
	}

	public void setR180_PURPOSE(String r180_PURPOSE) {
		R180_PURPOSE = r180_PURPOSE;
	}

	public String getR180_CURRENCY() {
		return R180_CURRENCY;
	}

	public void setR180_CURRENCY(String r180_CURRENCY) {
		R180_CURRENCY = r180_CURRENCY;
	}

	public String getR180_BANK_RATE() {
		return R180_BANK_RATE;
	}

	public void setR180_BANK_RATE(String r180_BANK_RATE) {
		R180_BANK_RATE = r180_BANK_RATE;
	}

	public BigDecimal getR180_AMT_DEMAND() {
		return R180_AMT_DEMAND;
	}

	public void setR180_AMT_DEMAND(BigDecimal r180_AMT_DEMAND) {
		R180_AMT_DEMAND = r180_AMT_DEMAND;
	}

	public BigDecimal getR180_AMT_TIME() {
		return R180_AMT_TIME;
	}

	public void setR180_AMT_TIME(BigDecimal r180_AMT_TIME) {
		R180_AMT_TIME = r180_AMT_TIME;
	}

	public String getR181_NAME_OF_BANK() {
		return R181_NAME_OF_BANK;
	}

	public void setR181_NAME_OF_BANK(String r181_NAME_OF_BANK) {
		R181_NAME_OF_BANK = r181_NAME_OF_BANK;
	}

	public String getR181_COUNTRY() {
		return R181_COUNTRY;
	}

	public void setR181_COUNTRY(String r181_COUNTRY) {
		R181_COUNTRY = r181_COUNTRY;
	}

	public String getR181_TYPE_OF_ACC() {
		return R181_TYPE_OF_ACC;
	}

	public void setR181_TYPE_OF_ACC(String r181_TYPE_OF_ACC) {
		R181_TYPE_OF_ACC = r181_TYPE_OF_ACC;
	}

	public String getR181_PURPOSE() {
		return R181_PURPOSE;
	}

	public void setR181_PURPOSE(String r181_PURPOSE) {
		R181_PURPOSE = r181_PURPOSE;
	}

	public String getR181_CURRENCY() {
		return R181_CURRENCY;
	}

	public void setR181_CURRENCY(String r181_CURRENCY) {
		R181_CURRENCY = r181_CURRENCY;
	}

	public String getR181_BANK_RATE() {
		return R181_BANK_RATE;
	}

	public void setR181_BANK_RATE(String r181_BANK_RATE) {
		R181_BANK_RATE = r181_BANK_RATE;
	}

	public BigDecimal getR181_AMT_DEMAND() {
		return R181_AMT_DEMAND;
	}

	public void setR181_AMT_DEMAND(BigDecimal r181_AMT_DEMAND) {
		R181_AMT_DEMAND = r181_AMT_DEMAND;
	}

	public BigDecimal getR181_AMT_TIME() {
		return R181_AMT_TIME;
	}

	public void setR181_AMT_TIME(BigDecimal r181_AMT_TIME) {
		R181_AMT_TIME = r181_AMT_TIME;
	}

	public String getR182_NAME_OF_BANK() {
		return R182_NAME_OF_BANK;
	}

	public void setR182_NAME_OF_BANK(String r182_NAME_OF_BANK) {
		R182_NAME_OF_BANK = r182_NAME_OF_BANK;
	}

	public String getR182_COUNTRY() {
		return R182_COUNTRY;
	}

	public void setR182_COUNTRY(String r182_COUNTRY) {
		R182_COUNTRY = r182_COUNTRY;
	}

	public String getR182_TYPE_OF_ACC() {
		return R182_TYPE_OF_ACC;
	}

	public void setR182_TYPE_OF_ACC(String r182_TYPE_OF_ACC) {
		R182_TYPE_OF_ACC = r182_TYPE_OF_ACC;
	}

	public String getR182_PURPOSE() {
		return R182_PURPOSE;
	}

	public void setR182_PURPOSE(String r182_PURPOSE) {
		R182_PURPOSE = r182_PURPOSE;
	}

	public String getR182_CURRENCY() {
		return R182_CURRENCY;
	}

	public void setR182_CURRENCY(String r182_CURRENCY) {
		R182_CURRENCY = r182_CURRENCY;
	}

	public String getR182_BANK_RATE() {
		return R182_BANK_RATE;
	}

	public void setR182_BANK_RATE(String r182_BANK_RATE) {
		R182_BANK_RATE = r182_BANK_RATE;
	}

	public BigDecimal getR182_AMT_DEMAND() {
		return R182_AMT_DEMAND;
	}

	public void setR182_AMT_DEMAND(BigDecimal r182_AMT_DEMAND) {
		R182_AMT_DEMAND = r182_AMT_DEMAND;
	}

	public BigDecimal getR182_AMT_TIME() {
		return R182_AMT_TIME;
	}

	public void setR182_AMT_TIME(BigDecimal r182_AMT_TIME) {
		R182_AMT_TIME = r182_AMT_TIME;
	}

	public String getR183_NAME_OF_BANK() {
		return R183_NAME_OF_BANK;
	}

	public void setR183_NAME_OF_BANK(String r183_NAME_OF_BANK) {
		R183_NAME_OF_BANK = r183_NAME_OF_BANK;
	}

	public String getR183_COUNTRY() {
		return R183_COUNTRY;
	}

	public void setR183_COUNTRY(String r183_COUNTRY) {
		R183_COUNTRY = r183_COUNTRY;
	}

	public String getR183_TYPE_OF_ACC() {
		return R183_TYPE_OF_ACC;
	}

	public void setR183_TYPE_OF_ACC(String r183_TYPE_OF_ACC) {
		R183_TYPE_OF_ACC = r183_TYPE_OF_ACC;
	}

	public String getR183_PURPOSE() {
		return R183_PURPOSE;
	}

	public void setR183_PURPOSE(String r183_PURPOSE) {
		R183_PURPOSE = r183_PURPOSE;
	}

	public String getR183_CURRENCY() {
		return R183_CURRENCY;
	}

	public void setR183_CURRENCY(String r183_CURRENCY) {
		R183_CURRENCY = r183_CURRENCY;
	}

	public String getR183_BANK_RATE() {
		return R183_BANK_RATE;
	}

	public void setR183_BANK_RATE(String r183_BANK_RATE) {
		R183_BANK_RATE = r183_BANK_RATE;
	}

	public BigDecimal getR183_AMT_DEMAND() {
		return R183_AMT_DEMAND;
	}

	public void setR183_AMT_DEMAND(BigDecimal r183_AMT_DEMAND) {
		R183_AMT_DEMAND = r183_AMT_DEMAND;
	}

	public BigDecimal getR183_AMT_TIME() {
		return R183_AMT_TIME;
	}

	public void setR183_AMT_TIME(BigDecimal r183_AMT_TIME) {
		R183_AMT_TIME = r183_AMT_TIME;
	}

	public String getR184_NAME_OF_BANK() {
		return R184_NAME_OF_BANK;
	}

	public void setR184_NAME_OF_BANK(String r184_NAME_OF_BANK) {
		R184_NAME_OF_BANK = r184_NAME_OF_BANK;
	}

	public String getR184_COUNTRY() {
		return R184_COUNTRY;
	}

	public void setR184_COUNTRY(String r184_COUNTRY) {
		R184_COUNTRY = r184_COUNTRY;
	}

	public String getR184_TYPE_OF_ACC() {
		return R184_TYPE_OF_ACC;
	}

	public void setR184_TYPE_OF_ACC(String r184_TYPE_OF_ACC) {
		R184_TYPE_OF_ACC = r184_TYPE_OF_ACC;
	}

	public String getR184_PURPOSE() {
		return R184_PURPOSE;
	}

	public void setR184_PURPOSE(String r184_PURPOSE) {
		R184_PURPOSE = r184_PURPOSE;
	}

	public String getR184_CURRENCY() {
		return R184_CURRENCY;
	}

	public void setR184_CURRENCY(String r184_CURRENCY) {
		R184_CURRENCY = r184_CURRENCY;
	}

	public String getR184_BANK_RATE() {
		return R184_BANK_RATE;
	}

	public void setR184_BANK_RATE(String r184_BANK_RATE) {
		R184_BANK_RATE = r184_BANK_RATE;
	}

	public BigDecimal getR184_AMT_DEMAND() {
		return R184_AMT_DEMAND;
	}

	public void setR184_AMT_DEMAND(BigDecimal r184_AMT_DEMAND) {
		R184_AMT_DEMAND = r184_AMT_DEMAND;
	}

	public BigDecimal getR184_AMT_TIME() {
		return R184_AMT_TIME;
	}

	public void setR184_AMT_TIME(BigDecimal r184_AMT_TIME) {
		R184_AMT_TIME = r184_AMT_TIME;
	}

	public String getR185_NAME_OF_BANK() {
		return R185_NAME_OF_BANK;
	}

	public void setR185_NAME_OF_BANK(String r185_NAME_OF_BANK) {
		R185_NAME_OF_BANK = r185_NAME_OF_BANK;
	}

	public String getR185_COUNTRY() {
		return R185_COUNTRY;
	}

	public void setR185_COUNTRY(String r185_COUNTRY) {
		R185_COUNTRY = r185_COUNTRY;
	}

	public String getR185_TYPE_OF_ACC() {
		return R185_TYPE_OF_ACC;
	}

	public void setR185_TYPE_OF_ACC(String r185_TYPE_OF_ACC) {
		R185_TYPE_OF_ACC = r185_TYPE_OF_ACC;
	}

	public String getR185_PURPOSE() {
		return R185_PURPOSE;
	}

	public void setR185_PURPOSE(String r185_PURPOSE) {
		R185_PURPOSE = r185_PURPOSE;
	}

	public String getR185_CURRENCY() {
		return R185_CURRENCY;
	}

	public void setR185_CURRENCY(String r185_CURRENCY) {
		R185_CURRENCY = r185_CURRENCY;
	}

	public String getR185_BANK_RATE() {
		return R185_BANK_RATE;
	}

	public void setR185_BANK_RATE(String r185_BANK_RATE) {
		R185_BANK_RATE = r185_BANK_RATE;
	}

	public BigDecimal getR185_AMT_DEMAND() {
		return R185_AMT_DEMAND;
	}

	public void setR185_AMT_DEMAND(BigDecimal r185_AMT_DEMAND) {
		R185_AMT_DEMAND = r185_AMT_DEMAND;
	}

	public BigDecimal getR185_AMT_TIME() {
		return R185_AMT_TIME;
	}

	public void setR185_AMT_TIME(BigDecimal r185_AMT_TIME) {
		R185_AMT_TIME = r185_AMT_TIME;
	}

	public String getR186_NAME_OF_BANK() {
		return R186_NAME_OF_BANK;
	}

	public void setR186_NAME_OF_BANK(String r186_NAME_OF_BANK) {
		R186_NAME_OF_BANK = r186_NAME_OF_BANK;
	}

	public String getR186_COUNTRY() {
		return R186_COUNTRY;
	}

	public void setR186_COUNTRY(String r186_COUNTRY) {
		R186_COUNTRY = r186_COUNTRY;
	}

	public String getR186_TYPE_OF_ACC() {
		return R186_TYPE_OF_ACC;
	}

	public void setR186_TYPE_OF_ACC(String r186_TYPE_OF_ACC) {
		R186_TYPE_OF_ACC = r186_TYPE_OF_ACC;
	}

	public String getR186_PURPOSE() {
		return R186_PURPOSE;
	}

	public void setR186_PURPOSE(String r186_PURPOSE) {
		R186_PURPOSE = r186_PURPOSE;
	}

	public String getR186_CURRENCY() {
		return R186_CURRENCY;
	}

	public void setR186_CURRENCY(String r186_CURRENCY) {
		R186_CURRENCY = r186_CURRENCY;
	}

	public String getR186_BANK_RATE() {
		return R186_BANK_RATE;
	}

	public void setR186_BANK_RATE(String r186_BANK_RATE) {
		R186_BANK_RATE = r186_BANK_RATE;
	}

	public BigDecimal getR186_AMT_DEMAND() {
		return R186_AMT_DEMAND;
	}

	public void setR186_AMT_DEMAND(BigDecimal r186_AMT_DEMAND) {
		R186_AMT_DEMAND = r186_AMT_DEMAND;
	}

	public BigDecimal getR186_AMT_TIME() {
		return R186_AMT_TIME;
	}

	public void setR186_AMT_TIME(BigDecimal r186_AMT_TIME) {
		R186_AMT_TIME = r186_AMT_TIME;
	}

	public String getR187_NAME_OF_BANK() {
		return R187_NAME_OF_BANK;
	}

	public void setR187_NAME_OF_BANK(String r187_NAME_OF_BANK) {
		R187_NAME_OF_BANK = r187_NAME_OF_BANK;
	}

	public String getR187_COUNTRY() {
		return R187_COUNTRY;
	}

	public void setR187_COUNTRY(String r187_COUNTRY) {
		R187_COUNTRY = r187_COUNTRY;
	}

	public String getR187_TYPE_OF_ACC() {
		return R187_TYPE_OF_ACC;
	}

	public void setR187_TYPE_OF_ACC(String r187_TYPE_OF_ACC) {
		R187_TYPE_OF_ACC = r187_TYPE_OF_ACC;
	}

	public String getR187_PURPOSE() {
		return R187_PURPOSE;
	}

	public void setR187_PURPOSE(String r187_PURPOSE) {
		R187_PURPOSE = r187_PURPOSE;
	}

	public String getR187_CURRENCY() {
		return R187_CURRENCY;
	}

	public void setR187_CURRENCY(String r187_CURRENCY) {
		R187_CURRENCY = r187_CURRENCY;
	}

	public String getR187_BANK_RATE() {
		return R187_BANK_RATE;
	}

	public void setR187_BANK_RATE(String r187_BANK_RATE) {
		R187_BANK_RATE = r187_BANK_RATE;
	}

	public BigDecimal getR187_AMT_DEMAND() {
		return R187_AMT_DEMAND;
	}

	public void setR187_AMT_DEMAND(BigDecimal r187_AMT_DEMAND) {
		R187_AMT_DEMAND = r187_AMT_DEMAND;
	}

	public BigDecimal getR187_AMT_TIME() {
		return R187_AMT_TIME;
	}

	public void setR187_AMT_TIME(BigDecimal r187_AMT_TIME) {
		R187_AMT_TIME = r187_AMT_TIME;
	}

	public String getR188_NAME_OF_BANK() {
		return R188_NAME_OF_BANK;
	}

	public void setR188_NAME_OF_BANK(String r188_NAME_OF_BANK) {
		R188_NAME_OF_BANK = r188_NAME_OF_BANK;
	}

	public String getR188_COUNTRY() {
		return R188_COUNTRY;
	}

	public void setR188_COUNTRY(String r188_COUNTRY) {
		R188_COUNTRY = r188_COUNTRY;
	}

	public String getR188_TYPE_OF_ACC() {
		return R188_TYPE_OF_ACC;
	}

	public void setR188_TYPE_OF_ACC(String r188_TYPE_OF_ACC) {
		R188_TYPE_OF_ACC = r188_TYPE_OF_ACC;
	}

	public String getR188_PURPOSE() {
		return R188_PURPOSE;
	}

	public void setR188_PURPOSE(String r188_PURPOSE) {
		R188_PURPOSE = r188_PURPOSE;
	}

	public String getR188_CURRENCY() {
		return R188_CURRENCY;
	}

	public void setR188_CURRENCY(String r188_CURRENCY) {
		R188_CURRENCY = r188_CURRENCY;
	}

	public String getR188_BANK_RATE() {
		return R188_BANK_RATE;
	}

	public void setR188_BANK_RATE(String r188_BANK_RATE) {
		R188_BANK_RATE = r188_BANK_RATE;
	}

	public BigDecimal getR188_AMT_DEMAND() {
		return R188_AMT_DEMAND;
	}

	public void setR188_AMT_DEMAND(BigDecimal r188_AMT_DEMAND) {
		R188_AMT_DEMAND = r188_AMT_DEMAND;
	}

	public BigDecimal getR188_AMT_TIME() {
		return R188_AMT_TIME;
	}

	public void setR188_AMT_TIME(BigDecimal r188_AMT_TIME) {
		R188_AMT_TIME = r188_AMT_TIME;
	}

	public String getR189_NAME_OF_BANK() {
		return R189_NAME_OF_BANK;
	}

	public void setR189_NAME_OF_BANK(String r189_NAME_OF_BANK) {
		R189_NAME_OF_BANK = r189_NAME_OF_BANK;
	}

	public String getR189_COUNTRY() {
		return R189_COUNTRY;
	}

	public void setR189_COUNTRY(String r189_COUNTRY) {
		R189_COUNTRY = r189_COUNTRY;
	}

	public String getR189_TYPE_OF_ACC() {
		return R189_TYPE_OF_ACC;
	}

	public void setR189_TYPE_OF_ACC(String r189_TYPE_OF_ACC) {
		R189_TYPE_OF_ACC = r189_TYPE_OF_ACC;
	}

	public String getR189_PURPOSE() {
		return R189_PURPOSE;
	}

	public void setR189_PURPOSE(String r189_PURPOSE) {
		R189_PURPOSE = r189_PURPOSE;
	}

	public String getR189_CURRENCY() {
		return R189_CURRENCY;
	}

	public void setR189_CURRENCY(String r189_CURRENCY) {
		R189_CURRENCY = r189_CURRENCY;
	}

	public String getR189_BANK_RATE() {
		return R189_BANK_RATE;
	}

	public void setR189_BANK_RATE(String r189_BANK_RATE) {
		R189_BANK_RATE = r189_BANK_RATE;
	}

	public BigDecimal getR189_AMT_DEMAND() {
		return R189_AMT_DEMAND;
	}

	public void setR189_AMT_DEMAND(BigDecimal r189_AMT_DEMAND) {
		R189_AMT_DEMAND = r189_AMT_DEMAND;
	}

	public BigDecimal getR189_AMT_TIME() {
		return R189_AMT_TIME;
	}

	public void setR189_AMT_TIME(BigDecimal r189_AMT_TIME) {
		R189_AMT_TIME = r189_AMT_TIME;
	}

	public String getR190_NAME_OF_BANK() {
		return R190_NAME_OF_BANK;
	}

	public void setR190_NAME_OF_BANK(String r190_NAME_OF_BANK) {
		R190_NAME_OF_BANK = r190_NAME_OF_BANK;
	}

	public String getR190_COUNTRY() {
		return R190_COUNTRY;
	}

	public void setR190_COUNTRY(String r190_COUNTRY) {
		R190_COUNTRY = r190_COUNTRY;
	}

	public String getR190_TYPE_OF_ACC() {
		return R190_TYPE_OF_ACC;
	}

	public void setR190_TYPE_OF_ACC(String r190_TYPE_OF_ACC) {
		R190_TYPE_OF_ACC = r190_TYPE_OF_ACC;
	}

	public String getR190_PURPOSE() {
		return R190_PURPOSE;
	}

	public void setR190_PURPOSE(String r190_PURPOSE) {
		R190_PURPOSE = r190_PURPOSE;
	}

	public String getR190_CURRENCY() {
		return R190_CURRENCY;
	}

	public void setR190_CURRENCY(String r190_CURRENCY) {
		R190_CURRENCY = r190_CURRENCY;
	}

	public String getR190_BANK_RATE() {
		return R190_BANK_RATE;
	}

	public void setR190_BANK_RATE(String r190_BANK_RATE) {
		R190_BANK_RATE = r190_BANK_RATE;
	}

	public BigDecimal getR190_AMT_DEMAND() {
		return R190_AMT_DEMAND;
	}

	public void setR190_AMT_DEMAND(BigDecimal r190_AMT_DEMAND) {
		R190_AMT_DEMAND = r190_AMT_DEMAND;
	}

	public BigDecimal getR190_AMT_TIME() {
		return R190_AMT_TIME;
	}

	public void setR190_AMT_TIME(BigDecimal r190_AMT_TIME) {
		R190_AMT_TIME = r190_AMT_TIME;
	}

	public String getR191_NAME_OF_BANK() {
		return R191_NAME_OF_BANK;
	}

	public void setR191_NAME_OF_BANK(String r191_NAME_OF_BANK) {
		R191_NAME_OF_BANK = r191_NAME_OF_BANK;
	}

	public String getR191_COUNTRY() {
		return R191_COUNTRY;
	}

	public void setR191_COUNTRY(String r191_COUNTRY) {
		R191_COUNTRY = r191_COUNTRY;
	}

	public String getR191_TYPE_OF_ACC() {
		return R191_TYPE_OF_ACC;
	}

	public void setR191_TYPE_OF_ACC(String r191_TYPE_OF_ACC) {
		R191_TYPE_OF_ACC = r191_TYPE_OF_ACC;
	}

	public String getR191_PURPOSE() {
		return R191_PURPOSE;
	}

	public void setR191_PURPOSE(String r191_PURPOSE) {
		R191_PURPOSE = r191_PURPOSE;
	}

	public String getR191_CURRENCY() {
		return R191_CURRENCY;
	}

	public void setR191_CURRENCY(String r191_CURRENCY) {
		R191_CURRENCY = r191_CURRENCY;
	}

	public String getR191_BANK_RATE() {
		return R191_BANK_RATE;
	}

	public void setR191_BANK_RATE(String r191_BANK_RATE) {
		R191_BANK_RATE = r191_BANK_RATE;
	}

	public BigDecimal getR191_AMT_DEMAND() {
		return R191_AMT_DEMAND;
	}

	public void setR191_AMT_DEMAND(BigDecimal r191_AMT_DEMAND) {
		R191_AMT_DEMAND = r191_AMT_DEMAND;
	}

	public BigDecimal getR191_AMT_TIME() {
		return R191_AMT_TIME;
	}

	public void setR191_AMT_TIME(BigDecimal r191_AMT_TIME) {
		R191_AMT_TIME = r191_AMT_TIME;
	}

	public String getR192_NAME_OF_BANK() {
		return R192_NAME_OF_BANK;
	}

	public void setR192_NAME_OF_BANK(String r192_NAME_OF_BANK) {
		R192_NAME_OF_BANK = r192_NAME_OF_BANK;
	}

	public String getR192_COUNTRY() {
		return R192_COUNTRY;
	}

	public void setR192_COUNTRY(String r192_COUNTRY) {
		R192_COUNTRY = r192_COUNTRY;
	}

	public String getR192_TYPE_OF_ACC() {
		return R192_TYPE_OF_ACC;
	}

	public void setR192_TYPE_OF_ACC(String r192_TYPE_OF_ACC) {
		R192_TYPE_OF_ACC = r192_TYPE_OF_ACC;
	}

	public String getR192_PURPOSE() {
		return R192_PURPOSE;
	}

	public void setR192_PURPOSE(String r192_PURPOSE) {
		R192_PURPOSE = r192_PURPOSE;
	}

	public String getR192_CURRENCY() {
		return R192_CURRENCY;
	}

	public void setR192_CURRENCY(String r192_CURRENCY) {
		R192_CURRENCY = r192_CURRENCY;
	}

	public String getR192_BANK_RATE() {
		return R192_BANK_RATE;
	}

	public void setR192_BANK_RATE(String r192_BANK_RATE) {
		R192_BANK_RATE = r192_BANK_RATE;
	}

	public BigDecimal getR192_AMT_DEMAND() {
		return R192_AMT_DEMAND;
	}

	public void setR192_AMT_DEMAND(BigDecimal r192_AMT_DEMAND) {
		R192_AMT_DEMAND = r192_AMT_DEMAND;
	}

	public BigDecimal getR192_AMT_TIME() {
		return R192_AMT_TIME;
	}

	public void setR192_AMT_TIME(BigDecimal r192_AMT_TIME) {
		R192_AMT_TIME = r192_AMT_TIME;
	}

	public String getR193_NAME_OF_BANK() {
		return R193_NAME_OF_BANK;
	}

	public void setR193_NAME_OF_BANK(String r193_NAME_OF_BANK) {
		R193_NAME_OF_BANK = r193_NAME_OF_BANK;
	}

	public String getR193_COUNTRY() {
		return R193_COUNTRY;
	}

	public void setR193_COUNTRY(String r193_COUNTRY) {
		R193_COUNTRY = r193_COUNTRY;
	}

	public String getR193_TYPE_OF_ACC() {
		return R193_TYPE_OF_ACC;
	}

	public void setR193_TYPE_OF_ACC(String r193_TYPE_OF_ACC) {
		R193_TYPE_OF_ACC = r193_TYPE_OF_ACC;
	}

	public String getR193_PURPOSE() {
		return R193_PURPOSE;
	}

	public void setR193_PURPOSE(String r193_PURPOSE) {
		R193_PURPOSE = r193_PURPOSE;
	}

	public String getR193_CURRENCY() {
		return R193_CURRENCY;
	}

	public void setR193_CURRENCY(String r193_CURRENCY) {
		R193_CURRENCY = r193_CURRENCY;
	}

	public String getR193_BANK_RATE() {
		return R193_BANK_RATE;
	}

	public void setR193_BANK_RATE(String r193_BANK_RATE) {
		R193_BANK_RATE = r193_BANK_RATE;
	}

	public BigDecimal getR193_AMT_DEMAND() {
		return R193_AMT_DEMAND;
	}

	public void setR193_AMT_DEMAND(BigDecimal r193_AMT_DEMAND) {
		R193_AMT_DEMAND = r193_AMT_DEMAND;
	}

	public BigDecimal getR193_AMT_TIME() {
		return R193_AMT_TIME;
	}

	public void setR193_AMT_TIME(BigDecimal r193_AMT_TIME) {
		R193_AMT_TIME = r193_AMT_TIME;
	}

	public BigDecimal getR194_TOT_AMT_DEMAND() {
		return R194_TOT_AMT_DEMAND;
	}

	public void setR194_TOT_AMT_DEMAND(BigDecimal r194_TOT_AMT_DEMAND) {
		R194_TOT_AMT_DEMAND = r194_TOT_AMT_DEMAND;
	}

	public BigDecimal getR194_TOT_AMT_TIME() {
		return R194_TOT_AMT_TIME;
	}

	public void setR194_TOT_AMT_TIME(BigDecimal r194_TOT_AMT_TIME) {
		R194_TOT_AMT_TIME = r194_TOT_AMT_TIME;
	}

	public M_AIDP_Archival_Summary_Entity4() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(M_AIDP_PK pk) {
		// TODO Auto-generated method stub

	}

}