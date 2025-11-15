
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY")
@IdClass(M_INT_RATES_Archival_Summary_PK.class)

public class M_INT_RATES_Archival_Summary_Entity{	



	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Id
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
	
    @Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;	
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	
	private String R11_LENDING;
	private BigDecimal R11_NOMINAL_INTEREST_RATE;
	private BigDecimal R11_AVG_EFFECTIVE_RATE;
	private BigDecimal R11_VOLUME;

	private String R12_LENDING;
	private BigDecimal R12_NOMINAL_INTEREST_RATE;
	private BigDecimal R12_AVG_EFFECTIVE_RATE;
	private BigDecimal R12_VOLUME;

	private String R13_LENDING;
	private BigDecimal R13_NOMINAL_INTEREST_RATE;
	private BigDecimal R13_AVG_EFFECTIVE_RATE;
	private BigDecimal R13_VOLUME;

	private String R14_LENDING;
	private BigDecimal R14_NOMINAL_INTEREST_RATE;
	private BigDecimal R14_AVG_EFFECTIVE_RATE;
	private BigDecimal R14_VOLUME;

	private String R15_LENDING;
	private BigDecimal R15_NOMINAL_INTEREST_RATE;
	private BigDecimal R15_AVG_EFFECTIVE_RATE;
	private BigDecimal R15_VOLUME;

	private String R16_LENDING;
	private BigDecimal R16_NOMINAL_INTEREST_RATE;
	private BigDecimal R16_AVG_EFFECTIVE_RATE;
	private BigDecimal R16_VOLUME;

	private String R17_LENDING;
	private BigDecimal R17_NOMINAL_INTEREST_RATE;
	private BigDecimal R17_AVG_EFFECTIVE_RATE;
	private BigDecimal R17_VOLUME;

	private String R18_LENDING;
	private BigDecimal R18_NOMINAL_INTEREST_RATE;
	private BigDecimal R18_AVG_EFFECTIVE_RATE;
	private BigDecimal R18_VOLUME;

	private String R19_LENDING;
	private BigDecimal R19_NOMINAL_INTEREST_RATE;
	private BigDecimal R19_AVG_EFFECTIVE_RATE;
	private BigDecimal R19_VOLUME;

	private String R20_LENDING;
	private BigDecimal R20_NOMINAL_INTEREST_RATE;
	private BigDecimal R20_AVG_EFFECTIVE_RATE;
	private BigDecimal R20_VOLUME;

	private String R21_LENDING;
	private BigDecimal R21_NOMINAL_INTEREST_RATE;
	private BigDecimal R21_AVG_EFFECTIVE_RATE;
	private BigDecimal R21_VOLUME;

	private String R22_LENDING;
	private BigDecimal R22_NOMINAL_INTEREST_RATE;
	private BigDecimal R22_AVG_EFFECTIVE_RATE;
	private BigDecimal R22_VOLUME;

	private String R23_LENDING;
	private BigDecimal R23_NOMINAL_INTEREST_RATE;
	private BigDecimal R23_AVG_EFFECTIVE_RATE;
	private BigDecimal R23_VOLUME;
	
	private String R24_LENDING;

	// These must be String because DB column type is VARCHAR2(24)
	private String R24_NOMINAL_INTEREST_RATE;  
	private String R24_AVG_EFFECTIVE_RATE;

	private BigDecimal R24_VOLUME;
	
	private String R25_LENDING;
	private BigDecimal R25_NOMINAL_INTEREST_RATE;
	private BigDecimal R25_AVG_EFFECTIVE_RATE;
	private BigDecimal R25_VOLUME;

	private String R26_LENDING;
	private BigDecimal R26_NOMINAL_INTEREST_RATE;
	private BigDecimal R26_AVG_EFFECTIVE_RATE;
	private BigDecimal R26_VOLUME;

	private String R27_LENDING;
	private BigDecimal R27_NOMINAL_INTEREST_RATE;
	private BigDecimal R27_AVG_EFFECTIVE_RATE;
	private BigDecimal R27_VOLUME;

	private String R28_LENDING;
	private BigDecimal R28_NOMINAL_INTEREST_RATE;
	private BigDecimal R28_AVG_EFFECTIVE_RATE;
	private BigDecimal R28_VOLUME;

	private String R29_LENDING;
	private BigDecimal R29_NOMINAL_INTEREST_RATE;
	private BigDecimal R29_AVG_EFFECTIVE_RATE;
	private BigDecimal R29_VOLUME;

	private String R30_LENDING;
	private BigDecimal R30_NOMINAL_INTEREST_RATE;
	private BigDecimal R30_AVG_EFFECTIVE_RATE;
	private BigDecimal R30_VOLUME;

	private String R31_LENDING;
	private BigDecimal R31_NOMINAL_INTEREST_RATE;
	private BigDecimal R31_AVG_EFFECTIVE_RATE;
	private BigDecimal R31_VOLUME;

	private String R32_LENDING;
	private BigDecimal R32_NOMINAL_INTEREST_RATE;
	private BigDecimal R32_AVG_EFFECTIVE_RATE;
	private BigDecimal R32_VOLUME;

	private String R33_LENDING;
	private BigDecimal R33_NOMINAL_INTEREST_RATE;
	private BigDecimal R33_AVG_EFFECTIVE_RATE;
	private BigDecimal R33_VOLUME;

	private String R34_LENDING;
	private BigDecimal R34_NOMINAL_INTEREST_RATE;
	private BigDecimal R34_AVG_EFFECTIVE_RATE;
	private BigDecimal R34_VOLUME;

	private String R35_LENDING;
	private BigDecimal R35_NOMINAL_INTEREST_RATE;
	private BigDecimal R35_AVG_EFFECTIVE_RATE;
	private BigDecimal R35_VOLUME;

	private String R36_LENDING;
	private BigDecimal R36_NOMINAL_INTEREST_RATE;
	private BigDecimal R36_AVG_EFFECTIVE_RATE;
	private BigDecimal R36_VOLUME;

	private String R37_LENDING;
	private BigDecimal R37_NOMINAL_INTEREST_RATE;
	private BigDecimal R37_AVG_EFFECTIVE_RATE;
	private BigDecimal R37_VOLUME;

	private String R38_LENDING;
	private BigDecimal R38_NOMINAL_INTEREST_RATE;
	private BigDecimal R38_AVG_EFFECTIVE_RATE;
	private BigDecimal R38_VOLUME;

	private String R39_LENDING;
	private BigDecimal R39_NOMINAL_INTEREST_RATE;
	private BigDecimal R39_AVG_EFFECTIVE_RATE;
	private BigDecimal R39_VOLUME;

	private String R40_LENDING;
	private BigDecimal R40_NOMINAL_INTEREST_RATE;
	private BigDecimal R40_AVG_EFFECTIVE_RATE;
	private BigDecimal R40_VOLUME;

	private String R41_LENDING;
	private BigDecimal R41_NOMINAL_INTEREST_RATE;
	private BigDecimal R41_AVG_EFFECTIVE_RATE;
	private BigDecimal R41_VOLUME;

	private String R42_LENDING;
	private BigDecimal R42_NOMINAL_INTEREST_RATE;
	private BigDecimal R42_AVG_EFFECTIVE_RATE;
	private BigDecimal R42_VOLUME;
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportVersion() {
		return reportVersion;
	}
	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}
	public String getReport_frequency() {
		return report_frequency;
	}
	public void setReport_frequency(String report_frequency) {
		this.report_frequency = report_frequency;
	}
	public String getReport_code() {
		return report_code;
	}
	public void setReport_code(String report_code) {
		this.report_code = report_code;
	}
	public String getReport_desc() {
		return report_desc;
	}
	public void setReport_desc(String report_desc) {
		this.report_desc = report_desc;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public String getR11_LENDING() {
		return R11_LENDING;
	}
	public void setR11_LENDING(String r11_LENDING) {
		R11_LENDING = r11_LENDING;
	}
	public BigDecimal getR11_NOMINAL_INTEREST_RATE() {
		return R11_NOMINAL_INTEREST_RATE;
	}
	public void setR11_NOMINAL_INTEREST_RATE(BigDecimal r11_NOMINAL_INTEREST_RATE) {
		R11_NOMINAL_INTEREST_RATE = r11_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR11_AVG_EFFECTIVE_RATE() {
		return R11_AVG_EFFECTIVE_RATE;
	}
	public void setR11_AVG_EFFECTIVE_RATE(BigDecimal r11_AVG_EFFECTIVE_RATE) {
		R11_AVG_EFFECTIVE_RATE = r11_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR11_VOLUME() {
		return R11_VOLUME;
	}
	public void setR11_VOLUME(BigDecimal r11_VOLUME) {
		R11_VOLUME = r11_VOLUME;
	}
	public String getR12_LENDING() {
		return R12_LENDING;
	}
	public void setR12_LENDING(String r12_LENDING) {
		R12_LENDING = r12_LENDING;
	}
	public BigDecimal getR12_NOMINAL_INTEREST_RATE() {
		return R12_NOMINAL_INTEREST_RATE;
	}
	public void setR12_NOMINAL_INTEREST_RATE(BigDecimal r12_NOMINAL_INTEREST_RATE) {
		R12_NOMINAL_INTEREST_RATE = r12_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR12_AVG_EFFECTIVE_RATE() {
		return R12_AVG_EFFECTIVE_RATE;
	}
	public void setR12_AVG_EFFECTIVE_RATE(BigDecimal r12_AVG_EFFECTIVE_RATE) {
		R12_AVG_EFFECTIVE_RATE = r12_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR12_VOLUME() {
		return R12_VOLUME;
	}
	public void setR12_VOLUME(BigDecimal r12_VOLUME) {
		R12_VOLUME = r12_VOLUME;
	}
	public String getR13_LENDING() {
		return R13_LENDING;
	}
	public void setR13_LENDING(String r13_LENDING) {
		R13_LENDING = r13_LENDING;
	}
	public BigDecimal getR13_NOMINAL_INTEREST_RATE() {
		return R13_NOMINAL_INTEREST_RATE;
	}
	public void setR13_NOMINAL_INTEREST_RATE(BigDecimal r13_NOMINAL_INTEREST_RATE) {
		R13_NOMINAL_INTEREST_RATE = r13_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR13_AVG_EFFECTIVE_RATE() {
		return R13_AVG_EFFECTIVE_RATE;
	}
	public void setR13_AVG_EFFECTIVE_RATE(BigDecimal r13_AVG_EFFECTIVE_RATE) {
		R13_AVG_EFFECTIVE_RATE = r13_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR13_VOLUME() {
		return R13_VOLUME;
	}
	public void setR13_VOLUME(BigDecimal r13_VOLUME) {
		R13_VOLUME = r13_VOLUME;
	}
	public String getR14_LENDING() {
		return R14_LENDING;
	}
	public void setR14_LENDING(String r14_LENDING) {
		R14_LENDING = r14_LENDING;
	}
	public BigDecimal getR14_NOMINAL_INTEREST_RATE() {
		return R14_NOMINAL_INTEREST_RATE;
	}
	public void setR14_NOMINAL_INTEREST_RATE(BigDecimal r14_NOMINAL_INTEREST_RATE) {
		R14_NOMINAL_INTEREST_RATE = r14_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR14_AVG_EFFECTIVE_RATE() {
		return R14_AVG_EFFECTIVE_RATE;
	}
	public void setR14_AVG_EFFECTIVE_RATE(BigDecimal r14_AVG_EFFECTIVE_RATE) {
		R14_AVG_EFFECTIVE_RATE = r14_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR14_VOLUME() {
		return R14_VOLUME;
	}
	public void setR14_VOLUME(BigDecimal r14_VOLUME) {
		R14_VOLUME = r14_VOLUME;
	}
	public String getR15_LENDING() {
		return R15_LENDING;
	}
	public void setR15_LENDING(String r15_LENDING) {
		R15_LENDING = r15_LENDING;
	}
	public BigDecimal getR15_NOMINAL_INTEREST_RATE() {
		return R15_NOMINAL_INTEREST_RATE;
	}
	public void setR15_NOMINAL_INTEREST_RATE(BigDecimal r15_NOMINAL_INTEREST_RATE) {
		R15_NOMINAL_INTEREST_RATE = r15_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR15_AVG_EFFECTIVE_RATE() {
		return R15_AVG_EFFECTIVE_RATE;
	}
	public void setR15_AVG_EFFECTIVE_RATE(BigDecimal r15_AVG_EFFECTIVE_RATE) {
		R15_AVG_EFFECTIVE_RATE = r15_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR15_VOLUME() {
		return R15_VOLUME;
	}
	public void setR15_VOLUME(BigDecimal r15_VOLUME) {
		R15_VOLUME = r15_VOLUME;
	}
	public String getR16_LENDING() {
		return R16_LENDING;
	}
	public void setR16_LENDING(String r16_LENDING) {
		R16_LENDING = r16_LENDING;
	}
	public BigDecimal getR16_NOMINAL_INTEREST_RATE() {
		return R16_NOMINAL_INTEREST_RATE;
	}
	public void setR16_NOMINAL_INTEREST_RATE(BigDecimal r16_NOMINAL_INTEREST_RATE) {
		R16_NOMINAL_INTEREST_RATE = r16_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR16_AVG_EFFECTIVE_RATE() {
		return R16_AVG_EFFECTIVE_RATE;
	}
	public void setR16_AVG_EFFECTIVE_RATE(BigDecimal r16_AVG_EFFECTIVE_RATE) {
		R16_AVG_EFFECTIVE_RATE = r16_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR16_VOLUME() {
		return R16_VOLUME;
	}
	public void setR16_VOLUME(BigDecimal r16_VOLUME) {
		R16_VOLUME = r16_VOLUME;
	}
	public String getR17_LENDING() {
		return R17_LENDING;
	}
	public void setR17_LENDING(String r17_LENDING) {
		R17_LENDING = r17_LENDING;
	}
	public BigDecimal getR17_NOMINAL_INTEREST_RATE() {
		return R17_NOMINAL_INTEREST_RATE;
	}
	public void setR17_NOMINAL_INTEREST_RATE(BigDecimal r17_NOMINAL_INTEREST_RATE) {
		R17_NOMINAL_INTEREST_RATE = r17_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR17_AVG_EFFECTIVE_RATE() {
		return R17_AVG_EFFECTIVE_RATE;
	}
	public void setR17_AVG_EFFECTIVE_RATE(BigDecimal r17_AVG_EFFECTIVE_RATE) {
		R17_AVG_EFFECTIVE_RATE = r17_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR17_VOLUME() {
		return R17_VOLUME;
	}
	public void setR17_VOLUME(BigDecimal r17_VOLUME) {
		R17_VOLUME = r17_VOLUME;
	}
	public String getR18_LENDING() {
		return R18_LENDING;
	}
	public void setR18_LENDING(String r18_LENDING) {
		R18_LENDING = r18_LENDING;
	}
	public BigDecimal getR18_NOMINAL_INTEREST_RATE() {
		return R18_NOMINAL_INTEREST_RATE;
	}
	public void setR18_NOMINAL_INTEREST_RATE(BigDecimal r18_NOMINAL_INTEREST_RATE) {
		R18_NOMINAL_INTEREST_RATE = r18_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR18_AVG_EFFECTIVE_RATE() {
		return R18_AVG_EFFECTIVE_RATE;
	}
	public void setR18_AVG_EFFECTIVE_RATE(BigDecimal r18_AVG_EFFECTIVE_RATE) {
		R18_AVG_EFFECTIVE_RATE = r18_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR18_VOLUME() {
		return R18_VOLUME;
	}
	public void setR18_VOLUME(BigDecimal r18_VOLUME) {
		R18_VOLUME = r18_VOLUME;
	}
	public String getR19_LENDING() {
		return R19_LENDING;
	}
	public void setR19_LENDING(String r19_LENDING) {
		R19_LENDING = r19_LENDING;
	}
	public BigDecimal getR19_NOMINAL_INTEREST_RATE() {
		return R19_NOMINAL_INTEREST_RATE;
	}
	public void setR19_NOMINAL_INTEREST_RATE(BigDecimal r19_NOMINAL_INTEREST_RATE) {
		R19_NOMINAL_INTEREST_RATE = r19_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR19_AVG_EFFECTIVE_RATE() {
		return R19_AVG_EFFECTIVE_RATE;
	}
	public void setR19_AVG_EFFECTIVE_RATE(BigDecimal r19_AVG_EFFECTIVE_RATE) {
		R19_AVG_EFFECTIVE_RATE = r19_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR19_VOLUME() {
		return R19_VOLUME;
	}
	public void setR19_VOLUME(BigDecimal r19_VOLUME) {
		R19_VOLUME = r19_VOLUME;
	}
	public String getR20_LENDING() {
		return R20_LENDING;
	}
	public void setR20_LENDING(String r20_LENDING) {
		R20_LENDING = r20_LENDING;
	}
	public BigDecimal getR20_NOMINAL_INTEREST_RATE() {
		return R20_NOMINAL_INTEREST_RATE;
	}
	public void setR20_NOMINAL_INTEREST_RATE(BigDecimal r20_NOMINAL_INTEREST_RATE) {
		R20_NOMINAL_INTEREST_RATE = r20_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR20_AVG_EFFECTIVE_RATE() {
		return R20_AVG_EFFECTIVE_RATE;
	}
	public void setR20_AVG_EFFECTIVE_RATE(BigDecimal r20_AVG_EFFECTIVE_RATE) {
		R20_AVG_EFFECTIVE_RATE = r20_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR20_VOLUME() {
		return R20_VOLUME;
	}
	public void setR20_VOLUME(BigDecimal r20_VOLUME) {
		R20_VOLUME = r20_VOLUME;
	}
	public String getR21_LENDING() {
		return R21_LENDING;
	}
	public void setR21_LENDING(String r21_LENDING) {
		R21_LENDING = r21_LENDING;
	}
	public BigDecimal getR21_NOMINAL_INTEREST_RATE() {
		return R21_NOMINAL_INTEREST_RATE;
	}
	public void setR21_NOMINAL_INTEREST_RATE(BigDecimal r21_NOMINAL_INTEREST_RATE) {
		R21_NOMINAL_INTEREST_RATE = r21_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR21_AVG_EFFECTIVE_RATE() {
		return R21_AVG_EFFECTIVE_RATE;
	}
	public void setR21_AVG_EFFECTIVE_RATE(BigDecimal r21_AVG_EFFECTIVE_RATE) {
		R21_AVG_EFFECTIVE_RATE = r21_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR21_VOLUME() {
		return R21_VOLUME;
	}
	public void setR21_VOLUME(BigDecimal r21_VOLUME) {
		R21_VOLUME = r21_VOLUME;
	}
	public String getR22_LENDING() {
		return R22_LENDING;
	}
	public void setR22_LENDING(String r22_LENDING) {
		R22_LENDING = r22_LENDING;
	}
	public BigDecimal getR22_NOMINAL_INTEREST_RATE() {
		return R22_NOMINAL_INTEREST_RATE;
	}
	public void setR22_NOMINAL_INTEREST_RATE(BigDecimal r22_NOMINAL_INTEREST_RATE) {
		R22_NOMINAL_INTEREST_RATE = r22_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR22_AVG_EFFECTIVE_RATE() {
		return R22_AVG_EFFECTIVE_RATE;
	}
	public void setR22_AVG_EFFECTIVE_RATE(BigDecimal r22_AVG_EFFECTIVE_RATE) {
		R22_AVG_EFFECTIVE_RATE = r22_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR22_VOLUME() {
		return R22_VOLUME;
	}
	public void setR22_VOLUME(BigDecimal r22_VOLUME) {
		R22_VOLUME = r22_VOLUME;
	}
	public String getR23_LENDING() {
		return R23_LENDING;
	}
	public void setR23_LENDING(String r23_LENDING) {
		R23_LENDING = r23_LENDING;
	}
	public BigDecimal getR23_NOMINAL_INTEREST_RATE() {
		return R23_NOMINAL_INTEREST_RATE;
	}
	public void setR23_NOMINAL_INTEREST_RATE(BigDecimal r23_NOMINAL_INTEREST_RATE) {
		R23_NOMINAL_INTEREST_RATE = r23_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR23_AVG_EFFECTIVE_RATE() {
		return R23_AVG_EFFECTIVE_RATE;
	}
	public void setR23_AVG_EFFECTIVE_RATE(BigDecimal r23_AVG_EFFECTIVE_RATE) {
		R23_AVG_EFFECTIVE_RATE = r23_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR23_VOLUME() {
		return R23_VOLUME;
	}
	public void setR23_VOLUME(BigDecimal r23_VOLUME) {
		R23_VOLUME = r23_VOLUME;
	}
	public String getR24_LENDING() {
		return R24_LENDING;
	}
	public void setR24_LENDING(String r24_LENDING) {
		R24_LENDING = r24_LENDING;
	}
	public String getR24_NOMINAL_INTEREST_RATE() {
		return R24_NOMINAL_INTEREST_RATE;
	}
	public void setR24_NOMINAL_INTEREST_RATE(String r24_NOMINAL_INTEREST_RATE) {
		R24_NOMINAL_INTEREST_RATE = r24_NOMINAL_INTEREST_RATE;
	}
	public String getR24_AVG_EFFECTIVE_RATE() {
		return R24_AVG_EFFECTIVE_RATE;
	}
	public void setR24_AVG_EFFECTIVE_RATE(String r24_AVG_EFFECTIVE_RATE) {
		R24_AVG_EFFECTIVE_RATE = r24_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR24_VOLUME() {
		return R24_VOLUME;
	}
	public void setR24_VOLUME(BigDecimal r24_VOLUME) {
		R24_VOLUME = r24_VOLUME;
	}
	public String getR25_LENDING() {
		return R25_LENDING;
	}
	public void setR25_LENDING(String r25_LENDING) {
		R25_LENDING = r25_LENDING;
	}
	public BigDecimal getR25_NOMINAL_INTEREST_RATE() {
		return R25_NOMINAL_INTEREST_RATE;
	}
	public void setR25_NOMINAL_INTEREST_RATE(BigDecimal r25_NOMINAL_INTEREST_RATE) {
		R25_NOMINAL_INTEREST_RATE = r25_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR25_AVG_EFFECTIVE_RATE() {
		return R25_AVG_EFFECTIVE_RATE;
	}
	public void setR25_AVG_EFFECTIVE_RATE(BigDecimal r25_AVG_EFFECTIVE_RATE) {
		R25_AVG_EFFECTIVE_RATE = r25_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR25_VOLUME() {
		return R25_VOLUME;
	}
	public void setR25_VOLUME(BigDecimal r25_VOLUME) {
		R25_VOLUME = r25_VOLUME;
	}
	public String getR26_LENDING() {
		return R26_LENDING;
	}
	public void setR26_LENDING(String r26_LENDING) {
		R26_LENDING = r26_LENDING;
	}
	public BigDecimal getR26_NOMINAL_INTEREST_RATE() {
		return R26_NOMINAL_INTEREST_RATE;
	}
	public void setR26_NOMINAL_INTEREST_RATE(BigDecimal r26_NOMINAL_INTEREST_RATE) {
		R26_NOMINAL_INTEREST_RATE = r26_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR26_AVG_EFFECTIVE_RATE() {
		return R26_AVG_EFFECTIVE_RATE;
	}
	public void setR26_AVG_EFFECTIVE_RATE(BigDecimal r26_AVG_EFFECTIVE_RATE) {
		R26_AVG_EFFECTIVE_RATE = r26_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR26_VOLUME() {
		return R26_VOLUME;
	}
	public void setR26_VOLUME(BigDecimal r26_VOLUME) {
		R26_VOLUME = r26_VOLUME;
	}
	public String getR27_LENDING() {
		return R27_LENDING;
	}
	public void setR27_LENDING(String r27_LENDING) {
		R27_LENDING = r27_LENDING;
	}
	public BigDecimal getR27_NOMINAL_INTEREST_RATE() {
		return R27_NOMINAL_INTEREST_RATE;
	}
	public void setR27_NOMINAL_INTEREST_RATE(BigDecimal r27_NOMINAL_INTEREST_RATE) {
		R27_NOMINAL_INTEREST_RATE = r27_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR27_AVG_EFFECTIVE_RATE() {
		return R27_AVG_EFFECTIVE_RATE;
	}
	public void setR27_AVG_EFFECTIVE_RATE(BigDecimal r27_AVG_EFFECTIVE_RATE) {
		R27_AVG_EFFECTIVE_RATE = r27_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR27_VOLUME() {
		return R27_VOLUME;
	}
	public void setR27_VOLUME(BigDecimal r27_VOLUME) {
		R27_VOLUME = r27_VOLUME;
	}
	public String getR28_LENDING() {
		return R28_LENDING;
	}
	public void setR28_LENDING(String r28_LENDING) {
		R28_LENDING = r28_LENDING;
	}
	public BigDecimal getR28_NOMINAL_INTEREST_RATE() {
		return R28_NOMINAL_INTEREST_RATE;
	}
	public void setR28_NOMINAL_INTEREST_RATE(BigDecimal r28_NOMINAL_INTEREST_RATE) {
		R28_NOMINAL_INTEREST_RATE = r28_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR28_AVG_EFFECTIVE_RATE() {
		return R28_AVG_EFFECTIVE_RATE;
	}
	public void setR28_AVG_EFFECTIVE_RATE(BigDecimal r28_AVG_EFFECTIVE_RATE) {
		R28_AVG_EFFECTIVE_RATE = r28_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR28_VOLUME() {
		return R28_VOLUME;
	}
	public void setR28_VOLUME(BigDecimal r28_VOLUME) {
		R28_VOLUME = r28_VOLUME;
	}
	public String getR29_LENDING() {
		return R29_LENDING;
	}
	public void setR29_LENDING(String r29_LENDING) {
		R29_LENDING = r29_LENDING;
	}
	public BigDecimal getR29_NOMINAL_INTEREST_RATE() {
		return R29_NOMINAL_INTEREST_RATE;
	}
	public void setR29_NOMINAL_INTEREST_RATE(BigDecimal r29_NOMINAL_INTEREST_RATE) {
		R29_NOMINAL_INTEREST_RATE = r29_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR29_AVG_EFFECTIVE_RATE() {
		return R29_AVG_EFFECTIVE_RATE;
	}
	public void setR29_AVG_EFFECTIVE_RATE(BigDecimal r29_AVG_EFFECTIVE_RATE) {
		R29_AVG_EFFECTIVE_RATE = r29_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR29_VOLUME() {
		return R29_VOLUME;
	}
	public void setR29_VOLUME(BigDecimal r29_VOLUME) {
		R29_VOLUME = r29_VOLUME;
	}
	public String getR30_LENDING() {
		return R30_LENDING;
	}
	public void setR30_LENDING(String r30_LENDING) {
		R30_LENDING = r30_LENDING;
	}
	public BigDecimal getR30_NOMINAL_INTEREST_RATE() {
		return R30_NOMINAL_INTEREST_RATE;
	}
	public void setR30_NOMINAL_INTEREST_RATE(BigDecimal r30_NOMINAL_INTEREST_RATE) {
		R30_NOMINAL_INTEREST_RATE = r30_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR30_AVG_EFFECTIVE_RATE() {
		return R30_AVG_EFFECTIVE_RATE;
	}
	public void setR30_AVG_EFFECTIVE_RATE(BigDecimal r30_AVG_EFFECTIVE_RATE) {
		R30_AVG_EFFECTIVE_RATE = r30_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR30_VOLUME() {
		return R30_VOLUME;
	}
	public void setR30_VOLUME(BigDecimal r30_VOLUME) {
		R30_VOLUME = r30_VOLUME;
	}
	public String getR31_LENDING() {
		return R31_LENDING;
	}
	public void setR31_LENDING(String r31_LENDING) {
		R31_LENDING = r31_LENDING;
	}
	public BigDecimal getR31_NOMINAL_INTEREST_RATE() {
		return R31_NOMINAL_INTEREST_RATE;
	}
	public void setR31_NOMINAL_INTEREST_RATE(BigDecimal r31_NOMINAL_INTEREST_RATE) {
		R31_NOMINAL_INTEREST_RATE = r31_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR31_AVG_EFFECTIVE_RATE() {
		return R31_AVG_EFFECTIVE_RATE;
	}
	public void setR31_AVG_EFFECTIVE_RATE(BigDecimal r31_AVG_EFFECTIVE_RATE) {
		R31_AVG_EFFECTIVE_RATE = r31_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR31_VOLUME() {
		return R31_VOLUME;
	}
	public void setR31_VOLUME(BigDecimal r31_VOLUME) {
		R31_VOLUME = r31_VOLUME;
	}
	public String getR32_LENDING() {
		return R32_LENDING;
	}
	public void setR32_LENDING(String r32_LENDING) {
		R32_LENDING = r32_LENDING;
	}
	public BigDecimal getR32_NOMINAL_INTEREST_RATE() {
		return R32_NOMINAL_INTEREST_RATE;
	}
	public void setR32_NOMINAL_INTEREST_RATE(BigDecimal r32_NOMINAL_INTEREST_RATE) {
		R32_NOMINAL_INTEREST_RATE = r32_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR32_AVG_EFFECTIVE_RATE() {
		return R32_AVG_EFFECTIVE_RATE;
	}
	public void setR32_AVG_EFFECTIVE_RATE(BigDecimal r32_AVG_EFFECTIVE_RATE) {
		R32_AVG_EFFECTIVE_RATE = r32_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR32_VOLUME() {
		return R32_VOLUME;
	}
	public void setR32_VOLUME(BigDecimal r32_VOLUME) {
		R32_VOLUME = r32_VOLUME;
	}
	public String getR33_LENDING() {
		return R33_LENDING;
	}
	public void setR33_LENDING(String r33_LENDING) {
		R33_LENDING = r33_LENDING;
	}
	public BigDecimal getR33_NOMINAL_INTEREST_RATE() {
		return R33_NOMINAL_INTEREST_RATE;
	}
	public void setR33_NOMINAL_INTEREST_RATE(BigDecimal r33_NOMINAL_INTEREST_RATE) {
		R33_NOMINAL_INTEREST_RATE = r33_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR33_AVG_EFFECTIVE_RATE() {
		return R33_AVG_EFFECTIVE_RATE;
	}
	public void setR33_AVG_EFFECTIVE_RATE(BigDecimal r33_AVG_EFFECTIVE_RATE) {
		R33_AVG_EFFECTIVE_RATE = r33_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR33_VOLUME() {
		return R33_VOLUME;
	}
	public void setR33_VOLUME(BigDecimal r33_VOLUME) {
		R33_VOLUME = r33_VOLUME;
	}
	public String getR34_LENDING() {
		return R34_LENDING;
	}
	public void setR34_LENDING(String r34_LENDING) {
		R34_LENDING = r34_LENDING;
	}
	public BigDecimal getR34_NOMINAL_INTEREST_RATE() {
		return R34_NOMINAL_INTEREST_RATE;
	}
	public void setR34_NOMINAL_INTEREST_RATE(BigDecimal r34_NOMINAL_INTEREST_RATE) {
		R34_NOMINAL_INTEREST_RATE = r34_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR34_AVG_EFFECTIVE_RATE() {
		return R34_AVG_EFFECTIVE_RATE;
	}
	public void setR34_AVG_EFFECTIVE_RATE(BigDecimal r34_AVG_EFFECTIVE_RATE) {
		R34_AVG_EFFECTIVE_RATE = r34_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR34_VOLUME() {
		return R34_VOLUME;
	}
	public void setR34_VOLUME(BigDecimal r34_VOLUME) {
		R34_VOLUME = r34_VOLUME;
	}
	public String getR35_LENDING() {
		return R35_LENDING;
	}
	public void setR35_LENDING(String r35_LENDING) {
		R35_LENDING = r35_LENDING;
	}
	public BigDecimal getR35_NOMINAL_INTEREST_RATE() {
		return R35_NOMINAL_INTEREST_RATE;
	}
	public void setR35_NOMINAL_INTEREST_RATE(BigDecimal r35_NOMINAL_INTEREST_RATE) {
		R35_NOMINAL_INTEREST_RATE = r35_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR35_AVG_EFFECTIVE_RATE() {
		return R35_AVG_EFFECTIVE_RATE;
	}
	public void setR35_AVG_EFFECTIVE_RATE(BigDecimal r35_AVG_EFFECTIVE_RATE) {
		R35_AVG_EFFECTIVE_RATE = r35_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR35_VOLUME() {
		return R35_VOLUME;
	}
	public void setR35_VOLUME(BigDecimal r35_VOLUME) {
		R35_VOLUME = r35_VOLUME;
	}
	public String getR36_LENDING() {
		return R36_LENDING;
	}
	public void setR36_LENDING(String r36_LENDING) {
		R36_LENDING = r36_LENDING;
	}
	public BigDecimal getR36_NOMINAL_INTEREST_RATE() {
		return R36_NOMINAL_INTEREST_RATE;
	}
	public void setR36_NOMINAL_INTEREST_RATE(BigDecimal r36_NOMINAL_INTEREST_RATE) {
		R36_NOMINAL_INTEREST_RATE = r36_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR36_AVG_EFFECTIVE_RATE() {
		return R36_AVG_EFFECTIVE_RATE;
	}
	public void setR36_AVG_EFFECTIVE_RATE(BigDecimal r36_AVG_EFFECTIVE_RATE) {
		R36_AVG_EFFECTIVE_RATE = r36_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR36_VOLUME() {
		return R36_VOLUME;
	}
	public void setR36_VOLUME(BigDecimal r36_VOLUME) {
		R36_VOLUME = r36_VOLUME;
	}
	public String getR37_LENDING() {
		return R37_LENDING;
	}
	public void setR37_LENDING(String r37_LENDING) {
		R37_LENDING = r37_LENDING;
	}
	public BigDecimal getR37_NOMINAL_INTEREST_RATE() {
		return R37_NOMINAL_INTEREST_RATE;
	}
	public void setR37_NOMINAL_INTEREST_RATE(BigDecimal r37_NOMINAL_INTEREST_RATE) {
		R37_NOMINAL_INTEREST_RATE = r37_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR37_AVG_EFFECTIVE_RATE() {
		return R37_AVG_EFFECTIVE_RATE;
	}
	public void setR37_AVG_EFFECTIVE_RATE(BigDecimal r37_AVG_EFFECTIVE_RATE) {
		R37_AVG_EFFECTIVE_RATE = r37_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR37_VOLUME() {
		return R37_VOLUME;
	}
	public void setR37_VOLUME(BigDecimal r37_VOLUME) {
		R37_VOLUME = r37_VOLUME;
	}
	public String getR38_LENDING() {
		return R38_LENDING;
	}
	public void setR38_LENDING(String r38_LENDING) {
		R38_LENDING = r38_LENDING;
	}
	public BigDecimal getR38_NOMINAL_INTEREST_RATE() {
		return R38_NOMINAL_INTEREST_RATE;
	}
	public void setR38_NOMINAL_INTEREST_RATE(BigDecimal r38_NOMINAL_INTEREST_RATE) {
		R38_NOMINAL_INTEREST_RATE = r38_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR38_AVG_EFFECTIVE_RATE() {
		return R38_AVG_EFFECTIVE_RATE;
	}
	public void setR38_AVG_EFFECTIVE_RATE(BigDecimal r38_AVG_EFFECTIVE_RATE) {
		R38_AVG_EFFECTIVE_RATE = r38_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR38_VOLUME() {
		return R38_VOLUME;
	}
	public void setR38_VOLUME(BigDecimal r38_VOLUME) {
		R38_VOLUME = r38_VOLUME;
	}
	public String getR39_LENDING() {
		return R39_LENDING;
	}
	public void setR39_LENDING(String r39_LENDING) {
		R39_LENDING = r39_LENDING;
	}
	public BigDecimal getR39_NOMINAL_INTEREST_RATE() {
		return R39_NOMINAL_INTEREST_RATE;
	}
	public void setR39_NOMINAL_INTEREST_RATE(BigDecimal r39_NOMINAL_INTEREST_RATE) {
		R39_NOMINAL_INTEREST_RATE = r39_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR39_AVG_EFFECTIVE_RATE() {
		return R39_AVG_EFFECTIVE_RATE;
	}
	public void setR39_AVG_EFFECTIVE_RATE(BigDecimal r39_AVG_EFFECTIVE_RATE) {
		R39_AVG_EFFECTIVE_RATE = r39_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR39_VOLUME() {
		return R39_VOLUME;
	}
	public void setR39_VOLUME(BigDecimal r39_VOLUME) {
		R39_VOLUME = r39_VOLUME;
	}
	public String getR40_LENDING() {
		return R40_LENDING;
	}
	public void setR40_LENDING(String r40_LENDING) {
		R40_LENDING = r40_LENDING;
	}
	public BigDecimal getR40_NOMINAL_INTEREST_RATE() {
		return R40_NOMINAL_INTEREST_RATE;
	}
	public void setR40_NOMINAL_INTEREST_RATE(BigDecimal r40_NOMINAL_INTEREST_RATE) {
		R40_NOMINAL_INTEREST_RATE = r40_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR40_AVG_EFFECTIVE_RATE() {
		return R40_AVG_EFFECTIVE_RATE;
	}
	public void setR40_AVG_EFFECTIVE_RATE(BigDecimal r40_AVG_EFFECTIVE_RATE) {
		R40_AVG_EFFECTIVE_RATE = r40_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR40_VOLUME() {
		return R40_VOLUME;
	}
	public void setR40_VOLUME(BigDecimal r40_VOLUME) {
		R40_VOLUME = r40_VOLUME;
	}
	public String getR41_LENDING() {
		return R41_LENDING;
	}
	public void setR41_LENDING(String r41_LENDING) {
		R41_LENDING = r41_LENDING;
	}
	public BigDecimal getR41_NOMINAL_INTEREST_RATE() {
		return R41_NOMINAL_INTEREST_RATE;
	}
	public void setR41_NOMINAL_INTEREST_RATE(BigDecimal r41_NOMINAL_INTEREST_RATE) {
		R41_NOMINAL_INTEREST_RATE = r41_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR41_AVG_EFFECTIVE_RATE() {
		return R41_AVG_EFFECTIVE_RATE;
	}
	public void setR41_AVG_EFFECTIVE_RATE(BigDecimal r41_AVG_EFFECTIVE_RATE) {
		R41_AVG_EFFECTIVE_RATE = r41_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR41_VOLUME() {
		return R41_VOLUME;
	}
	public void setR41_VOLUME(BigDecimal r41_VOLUME) {
		R41_VOLUME = r41_VOLUME;
	}
	public String getR42_LENDING() {
		return R42_LENDING;
	}
	public void setR42_LENDING(String r42_LENDING) {
		R42_LENDING = r42_LENDING;
	}
	public BigDecimal getR42_NOMINAL_INTEREST_RATE() {
		return R42_NOMINAL_INTEREST_RATE;
	}
	public void setR42_NOMINAL_INTEREST_RATE(BigDecimal r42_NOMINAL_INTEREST_RATE) {
		R42_NOMINAL_INTEREST_RATE = r42_NOMINAL_INTEREST_RATE;
	}
	public BigDecimal getR42_AVG_EFFECTIVE_RATE() {
		return R42_AVG_EFFECTIVE_RATE;
	}
	public void setR42_AVG_EFFECTIVE_RATE(BigDecimal r42_AVG_EFFECTIVE_RATE) {
		R42_AVG_EFFECTIVE_RATE = r42_AVG_EFFECTIVE_RATE;
	}
	public BigDecimal getR42_VOLUME() {
		return R42_VOLUME;
	}
	public void setR42_VOLUME(BigDecimal r42_VOLUME) {
		R42_VOLUME = r42_VOLUME;
	}
	public M_INT_RATES_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	



	
	
	
}
	
	
	
	
	
	
