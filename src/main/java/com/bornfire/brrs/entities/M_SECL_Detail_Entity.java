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
@Table(name = "BRRS_M_SECL_DETAILTABLE")
public class M_SECL_Detail_Entity 
{
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
	
	//@Column(name = "REPORT_RESUBDATE")
	//@Temporal(TemporalType.TIMESTAMP)
	//private Date reportResubDate;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	

	private String R12_PRODUCT;
	private BigDecimal R12_EQUITY;
	private BigDecimal R12_BONDS;
	private BigDecimal R12_BOBCS;
	private BigDecimal R12_TREASURY_BILLS;
	private BigDecimal R12_REPURCHASE_AGREEMENTS;
	private BigDecimal R12_COMMERCIAL_PAPER;
	private BigDecimal R12_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R12_PLEDGED_ASSETS;
	private BigDecimal R12_OTHER_SPECIFY;
	private BigDecimal R12_TOTAL;

	private String R13_PRODUCT;
	private BigDecimal R13_EQUITY;
	private BigDecimal R13_BONDS;
	private BigDecimal R13_BOBCS;
	private BigDecimal R13_TREASURY_BILLS;
	private BigDecimal R13_REPURCHASE_AGREEMENTS;
	private BigDecimal R13_COMMERCIAL_PAPER;
	private BigDecimal R13_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R13_PLEDGED_ASSETS;
	private BigDecimal R13_OTHER_SPECIFY;
	private BigDecimal R13_TOTAL;

	private String R14_PRODUCT;
	private BigDecimal R14_EQUITY;
	private BigDecimal R14_BONDS;
	private BigDecimal R14_BOBCS;
	private BigDecimal R14_TREASURY_BILLS;
	private BigDecimal R14_REPURCHASE_AGREEMENTS;
	private BigDecimal R14_COMMERCIAL_PAPER;
	private BigDecimal R14_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R14_PLEDGED_ASSETS;
	private BigDecimal R14_OTHER_SPECIFY;
	private BigDecimal R14_TOTAL;

	private String R15_PRODUCT;
	private BigDecimal R15_EQUITY;
	private BigDecimal R15_BONDS;
	private BigDecimal R15_BOBCS;
	private BigDecimal R15_TREASURY_BILLS;
	private BigDecimal R15_REPURCHASE_AGREEMENTS;
	private BigDecimal R15_COMMERCIAL_PAPER;
	private BigDecimal R15_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R15_PLEDGED_ASSETS;
	private BigDecimal R15_OTHER_SPECIFY;
	private BigDecimal R15_TOTAL;

	private String R16_PRODUCT;
	private BigDecimal R16_EQUITY;
	private BigDecimal R16_BONDS;
	private BigDecimal R16_BOBCS;
	private BigDecimal R16_TREASURY_BILLS;
	private BigDecimal R16_REPURCHASE_AGREEMENTS;
	private BigDecimal R16_COMMERCIAL_PAPER;
	private BigDecimal R16_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R16_PLEDGED_ASSETS;
	private BigDecimal R16_OTHER_SPECIFY;
	private BigDecimal R16_TOTAL;

	private String R17_PRODUCT;
	private BigDecimal R17_EQUITY;
	private BigDecimal R17_BONDS;
	private BigDecimal R17_BOBCS;
	private BigDecimal R17_TREASURY_BILLS;
	private BigDecimal R17_REPURCHASE_AGREEMENTS;
	private BigDecimal R17_COMMERCIAL_PAPER;
	private BigDecimal R17_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R17_PLEDGED_ASSETS;
	private BigDecimal R17_OTHER_SPECIFY;
	private BigDecimal R17_TOTAL;

	private String R18_PRODUCT;
	private BigDecimal R18_EQUITY;
	private BigDecimal R18_BONDS;
	private BigDecimal R18_BOBCS;
	private BigDecimal R18_TREASURY_BILLS;
	private BigDecimal R18_REPURCHASE_AGREEMENTS;
	private BigDecimal R18_COMMERCIAL_PAPER;
	private BigDecimal R18_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R18_PLEDGED_ASSETS;
	private BigDecimal R18_OTHER_SPECIFY;
	private BigDecimal R18_TOTAL;

	private String R19_PRODUCT;
	private BigDecimal R19_EQUITY;
	private BigDecimal R19_BONDS;
	private BigDecimal R19_BOBCS;
	private BigDecimal R19_TREASURY_BILLS;
	private BigDecimal R19_REPURCHASE_AGREEMENTS;
	private BigDecimal R19_COMMERCIAL_PAPER;
	private BigDecimal R19_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R19_PLEDGED_ASSETS;
	private BigDecimal R19_OTHER_SPECIFY;
	private BigDecimal R19_TOTAL;

	private String R20_PRODUCT;
	private BigDecimal R20_EQUITY;
	private BigDecimal R20_BONDS;
	private BigDecimal R20_BOBCS;
	private BigDecimal R20_TREASURY_BILLS;
	private BigDecimal R20_REPURCHASE_AGREEMENTS;
	private BigDecimal R20_COMMERCIAL_PAPER;
	private BigDecimal R20_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R20_PLEDGED_ASSETS;
	private BigDecimal R20_OTHER_SPECIFY;
	private BigDecimal R20_TOTAL;

	private String R21_PRODUCT;
	private BigDecimal R21_EQUITY;
	private BigDecimal R21_BONDS;
	private BigDecimal R21_BOBCS;
	private BigDecimal R21_TREASURY_BILLS;
	private BigDecimal R21_REPURCHASE_AGREEMENTS;
	private BigDecimal R21_COMMERCIAL_PAPER;
	private BigDecimal R21_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R21_PLEDGED_ASSETS;
	private BigDecimal R21_OTHER_SPECIFY;
	private BigDecimal R21_TOTAL;

	private String R22_PRODUCT;
	private BigDecimal R22_EQUITY;
	private BigDecimal R22_BONDS;
	private BigDecimal R22_BOBCS;
	private BigDecimal R22_TREASURY_BILLS;
	private BigDecimal R22_REPURCHASE_AGREEMENTS;
	private BigDecimal R22_COMMERCIAL_PAPER;
	private BigDecimal R22_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R22_PLEDGED_ASSETS;
	private BigDecimal R22_OTHER_SPECIFY;
	private BigDecimal R22_TOTAL;

	private String R23_PRODUCT;
	private BigDecimal R23_EQUITY;
	private BigDecimal R23_BONDS;
	private BigDecimal R23_BOBCS;
	private BigDecimal R23_TREASURY_BILLS;
	private BigDecimal R23_REPURCHASE_AGREEMENTS;
	private BigDecimal R23_COMMERCIAL_PAPER;
	private BigDecimal R23_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R23_PLEDGED_ASSETS;
	private BigDecimal R23_OTHER_SPECIFY;
	private BigDecimal R23_TOTAL;

	private String R24_PRODUCT;
	private BigDecimal R24_EQUITY;
	private BigDecimal R24_BONDS;
	private BigDecimal R24_BOBCS;
	private BigDecimal R24_TREASURY_BILLS;
	private BigDecimal R24_REPURCHASE_AGREEMENTS;
	private BigDecimal R24_COMMERCIAL_PAPER;
	private BigDecimal R24_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R24_PLEDGED_ASSETS;
	private BigDecimal R24_OTHER_SPECIFY;
	private BigDecimal R24_TOTAL;

	private String R25_PRODUCT;
	private BigDecimal R25_EQUITY;
	private BigDecimal R25_BONDS;
	private BigDecimal R25_BOBCS;
	private BigDecimal R25_TREASURY_BILLS;
	private BigDecimal R25_REPURCHASE_AGREEMENTS;
	private BigDecimal R25_COMMERCIAL_PAPER;
	private BigDecimal R25_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R25_PLEDGED_ASSETS;
	private BigDecimal R25_OTHER_SPECIFY;
	private BigDecimal R25_TOTAL;

	private String R26_PRODUCT;
	private BigDecimal R26_EQUITY;
	private BigDecimal R26_BONDS;
	private BigDecimal R26_BOBCS;
	private BigDecimal R26_TREASURY_BILLS;
	private BigDecimal R26_REPURCHASE_AGREEMENTS;
	private BigDecimal R26_COMMERCIAL_PAPER;
	private BigDecimal R26_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R26_PLEDGED_ASSETS;
	private BigDecimal R26_OTHER_SPECIFY;
	private BigDecimal R26_TOTAL;

	private String R27_PRODUCT;
	private BigDecimal R27_EQUITY;
	private BigDecimal R27_BONDS;
	private BigDecimal R27_BOBCS;
	private BigDecimal R27_TREASURY_BILLS;
	private BigDecimal R27_REPURCHASE_AGREEMENTS;
	private BigDecimal R27_COMMERCIAL_PAPER;
	private BigDecimal R27_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R27_PLEDGED_ASSETS;
	private BigDecimal R27_OTHER_SPECIFY;
	private BigDecimal R27_TOTAL;

	private String R28_PRODUCT;
	private BigDecimal R28_EQUITY;
	private BigDecimal R28_BONDS;
	private BigDecimal R28_BOBCS;
	private BigDecimal R28_TREASURY_BILLS;
	private BigDecimal R28_REPURCHASE_AGREEMENTS;
	private BigDecimal R28_COMMERCIAL_PAPER;
	private BigDecimal R28_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R28_PLEDGED_ASSETS;
	private BigDecimal R28_OTHER_SPECIFY;
	private BigDecimal R28_TOTAL;

	private String R29_PRODUCT;
	private BigDecimal R29_EQUITY;
	private BigDecimal R29_BONDS;
	private BigDecimal R29_BOBCS;
	private BigDecimal R29_TREASURY_BILLS;
	private BigDecimal R29_REPURCHASE_AGREEMENTS;
	private BigDecimal R29_COMMERCIAL_PAPER;
	private BigDecimal R29_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R29_PLEDGED_ASSETS;
	private BigDecimal R29_OTHER_SPECIFY;
	private BigDecimal R29_TOTAL;

	private String R30_PRODUCT;
	private BigDecimal R30_EQUITY;
	private BigDecimal R30_BONDS;
	private BigDecimal R30_BOBCS;
	private BigDecimal R30_TREASURY_BILLS;
	private BigDecimal R30_REPURCHASE_AGREEMENTS;
	private BigDecimal R30_COMMERCIAL_PAPER;
	private BigDecimal R30_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R30_PLEDGED_ASSETS;
	private BigDecimal R30_OTHER_SPECIFY;
	private BigDecimal R30_TOTAL;

	private String R31_PRODUCT;
	private BigDecimal R31_EQUITY;
	private BigDecimal R31_BONDS;
	private BigDecimal R31_BOBCS;
	private BigDecimal R31_TREASURY_BILLS;
	private BigDecimal R31_REPURCHASE_AGREEMENTS;
	private BigDecimal R31_COMMERCIAL_PAPER;
	private BigDecimal R31_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R31_PLEDGED_ASSETS;
	private BigDecimal R31_OTHER_SPECIFY;
	private BigDecimal R31_TOTAL;

	private String R32_PRODUCT;
	private BigDecimal R32_EQUITY;
	private BigDecimal R32_BONDS;
	private BigDecimal R32_BOBCS;
	private BigDecimal R32_TREASURY_BILLS;
	private BigDecimal R32_REPURCHASE_AGREEMENTS;
	private BigDecimal R32_COMMERCIAL_PAPER;
	private BigDecimal R32_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R32_PLEDGED_ASSETS;
	private BigDecimal R32_OTHER_SPECIFY;
	private BigDecimal R32_TOTAL;

	private String R33_PRODUCT;
	private BigDecimal R33_EQUITY;
	private BigDecimal R33_BONDS;
	private BigDecimal R33_BOBCS;
	private BigDecimal R33_TREASURY_BILLS;
	private BigDecimal R33_REPURCHASE_AGREEMENTS;
	private BigDecimal R33_COMMERCIAL_PAPER;
	private BigDecimal R33_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R33_PLEDGED_ASSETS;
	private BigDecimal R33_OTHER_SPECIFY;
	private BigDecimal R33_TOTAL;

	private String R34_PRODUCT;
	private BigDecimal R34_EQUITY;
	private BigDecimal R34_BONDS;
	private BigDecimal R34_BOBCS;
	private BigDecimal R34_TREASURY_BILLS;
	private BigDecimal R34_REPURCHASE_AGREEMENTS;
	private BigDecimal R34_COMMERCIAL_PAPER;
	private BigDecimal R34_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R34_PLEDGED_ASSETS;
	private BigDecimal R34_OTHER_SPECIFY;
	private BigDecimal R34_TOTAL;

	private String R35_PRODUCT;
	private BigDecimal R35_EQUITY;
	private BigDecimal R35_BONDS;
	private BigDecimal R35_BOBCS;
	private BigDecimal R35_TREASURY_BILLS;
	private BigDecimal R35_REPURCHASE_AGREEMENTS;
	private BigDecimal R35_COMMERCIAL_PAPER;
	private BigDecimal R35_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R35_PLEDGED_ASSETS;
	private BigDecimal R35_OTHER_SPECIFY;
	private BigDecimal R35_TOTAL;

	private String R36_PRODUCT;
	private BigDecimal R36_EQUITY;
	private BigDecimal R36_BONDS;
	private BigDecimal R36_BOBCS;
	private BigDecimal R36_TREASURY_BILLS;
	private BigDecimal R36_REPURCHASE_AGREEMENTS;
	private BigDecimal R36_COMMERCIAL_PAPER;
	private BigDecimal R36_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R36_PLEDGED_ASSETS;
	private BigDecimal R36_OTHER_SPECIFY;
	private BigDecimal R36_TOTAL;

	private String R37_PRODUCT;
	private BigDecimal R37_EQUITY;
	private BigDecimal R37_BONDS;
	private BigDecimal R37_BOBCS;
	private BigDecimal R37_TREASURY_BILLS;
	private BigDecimal R37_REPURCHASE_AGREEMENTS;
	private BigDecimal R37_COMMERCIAL_PAPER;
	private BigDecimal R37_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R37_PLEDGED_ASSETS;
	private BigDecimal R37_OTHER_SPECIFY;
	private BigDecimal R37_TOTAL;

	private String R38_PRODUCT;
	private BigDecimal R38_EQUITY;
	private BigDecimal R38_BONDS;
	private BigDecimal R38_BOBCS;
	private BigDecimal R38_TREASURY_BILLS;
	private BigDecimal R38_REPURCHASE_AGREEMENTS;
	private BigDecimal R38_COMMERCIAL_PAPER;
	private BigDecimal R38_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R38_PLEDGED_ASSETS;
	private BigDecimal R38_OTHER_SPECIFY;
	private BigDecimal R38_TOTAL;

	private String R39_PRODUCT;
	private BigDecimal R39_EQUITY;
	private BigDecimal R39_BONDS;
	private BigDecimal R39_BOBCS;
	private BigDecimal R39_TREASURY_BILLS;
	private BigDecimal R39_REPURCHASE_AGREEMENTS;
	private BigDecimal R39_COMMERCIAL_PAPER;
	private BigDecimal R39_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R39_PLEDGED_ASSETS;
	private BigDecimal R39_OTHER_SPECIFY;
	private BigDecimal R39_TOTAL;

	private String R40_PRODUCT;
	private BigDecimal R40_EQUITY;
	private BigDecimal R40_BONDS;
	private BigDecimal R40_BOBCS;
	private BigDecimal R40_TREASURY_BILLS;
	private BigDecimal R40_REPURCHASE_AGREEMENTS;
	private BigDecimal R40_COMMERCIAL_PAPER;
	private BigDecimal R40_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R40_PLEDGED_ASSETS;
	private BigDecimal R40_OTHER_SPECIFY;
	private BigDecimal R40_TOTAL;

	private String R41_PRODUCT;
	private BigDecimal R41_EQUITY;
	private BigDecimal R41_BONDS;
	private BigDecimal R41_BOBCS;
	private BigDecimal R41_TREASURY_BILLS;
	private BigDecimal R41_REPURCHASE_AGREEMENTS;
	private BigDecimal R41_COMMERCIAL_PAPER;
	private BigDecimal R41_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R41_PLEDGED_ASSETS;
	private BigDecimal R41_OTHER_SPECIFY;
	private BigDecimal R41_TOTAL;

	private String R42_PRODUCT;
	private BigDecimal R42_EQUITY;
	private BigDecimal R42_BONDS;
	private BigDecimal R42_BOBCS;
	private BigDecimal R42_TREASURY_BILLS;
	private BigDecimal R42_REPURCHASE_AGREEMENTS;
	private BigDecimal R42_COMMERCIAL_PAPER;
	private BigDecimal R42_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R42_PLEDGED_ASSETS;
	private BigDecimal R42_OTHER_SPECIFY;
	private BigDecimal R42_TOTAL;

	private String R43_PRODUCT;
	private BigDecimal R43_EQUITY;
	private BigDecimal R43_BONDS;
	private BigDecimal R43_BOBCS;
	private BigDecimal R43_TREASURY_BILLS;
	private BigDecimal R43_REPURCHASE_AGREEMENTS;
	private BigDecimal R43_COMMERCIAL_PAPER;
	private BigDecimal R43_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R43_PLEDGED_ASSETS;
	private BigDecimal R43_OTHER_SPECIFY;
	private BigDecimal R43_TOTAL;

	private String R44_PRODUCT;
	private BigDecimal R44_EQUITY;
	private BigDecimal R44_BONDS;
	private BigDecimal R44_BOBCS;
	private BigDecimal R44_TREASURY_BILLS;
	private BigDecimal R44_REPURCHASE_AGREEMENTS;
	private BigDecimal R44_COMMERCIAL_PAPER;
	private BigDecimal R44_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R44_PLEDGED_ASSETS;
	private BigDecimal R44_OTHER_SPECIFY;
	private BigDecimal R44_TOTAL;

	private String R45_PRODUCT;
	private BigDecimal R45_EQUITY;
	private BigDecimal R45_BONDS;
	private BigDecimal R45_BOBCS;
	private BigDecimal R45_TREASURY_BILLS;
	private BigDecimal R45_REPURCHASE_AGREEMENTS;
	private BigDecimal R45_COMMERCIAL_PAPER;
	private BigDecimal R45_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R45_PLEDGED_ASSETS;
	private BigDecimal R45_OTHER_SPECIFY;
	private BigDecimal R45_TOTAL;

	private String R46_PRODUCT;
	private BigDecimal R46_EQUITY;
	private BigDecimal R46_BONDS;
	private BigDecimal R46_BOBCS;
	private BigDecimal R46_TREASURY_BILLS;
	private BigDecimal R46_REPURCHASE_AGREEMENTS;
	private BigDecimal R46_COMMERCIAL_PAPER;
	private BigDecimal R46_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R46_PLEDGED_ASSETS;
	private BigDecimal R46_OTHER_SPECIFY;
	private BigDecimal R46_TOTAL;

	private String R47_PRODUCT;
	private BigDecimal R47_EQUITY;
	private BigDecimal R47_BONDS;
	private BigDecimal R47_BOBCS;
	private BigDecimal R47_TREASURY_BILLS;
	private BigDecimal R47_REPURCHASE_AGREEMENTS;
	private BigDecimal R47_COMMERCIAL_PAPER;
	private BigDecimal R47_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R47_PLEDGED_ASSETS;
	private BigDecimal R47_OTHER_SPECIFY;
	private BigDecimal R47_TOTAL;

	private String R48_PRODUCT;
	private BigDecimal R48_EQUITY;
	private BigDecimal R48_BONDS;
	private BigDecimal R48_BOBCS;
	private BigDecimal R48_TREASURY_BILLS;
	private BigDecimal R48_REPURCHASE_AGREEMENTS;
	private BigDecimal R48_COMMERCIAL_PAPER;
	private BigDecimal R48_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R48_PLEDGED_ASSETS;
	private BigDecimal R48_OTHER_SPECIFY;
	private BigDecimal R48_TOTAL;

	private String R49_PRODUCT;
	private BigDecimal R49_EQUITY;
	private BigDecimal R49_BONDS;
	private BigDecimal R49_BOBCS;
	private BigDecimal R49_TREASURY_BILLS;
	private BigDecimal R49_REPURCHASE_AGREEMENTS;
	private BigDecimal R49_COMMERCIAL_PAPER;
	private BigDecimal R49_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R49_PLEDGED_ASSETS;
	private BigDecimal R49_OTHER_SPECIFY;
	private BigDecimal R49_TOTAL;

	private String R50_PRODUCT;
	private BigDecimal R50_EQUITY;
	private BigDecimal R50_BONDS;
	private BigDecimal R50_BOBCS;
	private BigDecimal R50_TREASURY_BILLS;
	private BigDecimal R50_REPURCHASE_AGREEMENTS;
	private BigDecimal R50_COMMERCIAL_PAPER;
	private BigDecimal R50_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R50_PLEDGED_ASSETS;
	private BigDecimal R50_OTHER_SPECIFY;
	private BigDecimal R50_TOTAL;

	private String R51_PRODUCT;
	private BigDecimal R51_EQUITY;
	private BigDecimal R51_BONDS;
	private BigDecimal R51_BOBCS;
	private BigDecimal R51_TREASURY_BILLS;
	private BigDecimal R51_REPURCHASE_AGREEMENTS;
	private BigDecimal R51_COMMERCIAL_PAPER;
	private BigDecimal R51_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R51_PLEDGED_ASSETS;
	private BigDecimal R51_OTHER_SPECIFY;
	private BigDecimal R51_TOTAL;

	private String R52_PRODUCT;
	private BigDecimal R52_EQUITY;
	private BigDecimal R52_BONDS;
	private BigDecimal R52_BOBCS;
	private BigDecimal R52_TREASURY_BILLS;
	private BigDecimal R52_REPURCHASE_AGREEMENTS;
	private BigDecimal R52_COMMERCIAL_PAPER;
	private BigDecimal R52_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R52_PLEDGED_ASSETS;
	private BigDecimal R52_OTHER_SPECIFY;
	private BigDecimal R52_TOTAL;

	private String R53_PRODUCT;
	private BigDecimal R53_EQUITY;
	private BigDecimal R53_BONDS;
	private BigDecimal R53_BOBCS;
	private BigDecimal R53_TREASURY_BILLS;
	private BigDecimal R53_REPURCHASE_AGREEMENTS;
	private BigDecimal R53_COMMERCIAL_PAPER;
	private BigDecimal R53_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R53_PLEDGED_ASSETS;
	private BigDecimal R53_OTHER_SPECIFY;
	private BigDecimal R53_TOTAL;

	private String R54_PRODUCT;
	private BigDecimal R54_EQUITY;
	private BigDecimal R54_BONDS;
	private BigDecimal R54_BOBCS;
	private BigDecimal R54_TREASURY_BILLS;
	private BigDecimal R54_REPURCHASE_AGREEMENTS;
	private BigDecimal R54_COMMERCIAL_PAPER;
	private BigDecimal R54_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R54_PLEDGED_ASSETS;
	private BigDecimal R54_OTHER_SPECIFY;
	private BigDecimal R54_TOTAL;

	private String R55_PRODUCT;
	private BigDecimal R55_EQUITY;
	private BigDecimal R55_BONDS;
	private BigDecimal R55_BOBCS;
	private BigDecimal R55_TREASURY_BILLS;
	private BigDecimal R55_REPURCHASE_AGREEMENTS;
	private BigDecimal R55_COMMERCIAL_PAPER;
	private BigDecimal R55_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R55_PLEDGED_ASSETS;
	private BigDecimal R55_OTHER_SPECIFY;
	private BigDecimal R55_TOTAL;

	private String R56_PRODUCT;
	private BigDecimal R56_EQUITY;
	private BigDecimal R56_BONDS;
	private BigDecimal R56_BOBCS;
	private BigDecimal R56_TREASURY_BILLS;
	private BigDecimal R56_REPURCHASE_AGREEMENTS;
	private BigDecimal R56_COMMERCIAL_PAPER;
	private BigDecimal R56_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R56_PLEDGED_ASSETS;
	private BigDecimal R56_OTHER_SPECIFY;
	private BigDecimal R56_TOTAL;

	private String R57_PRODUCT;
	private BigDecimal R57_EQUITY;
	private BigDecimal R57_BONDS;
	private BigDecimal R57_BOBCS;
	private BigDecimal R57_TREASURY_BILLS;
	private BigDecimal R57_REPURCHASE_AGREEMENTS;
	private BigDecimal R57_COMMERCIAL_PAPER;
	private BigDecimal R57_CERTIFICATES_OF_DEPOSITS;
	private BigDecimal R57_PLEDGED_ASSETS;
	private BigDecimal R57_OTHER_SPECIFY;
	private BigDecimal R57_TOTAL;
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
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_EQUITY() {
		return R12_EQUITY;
	}
	public void setR12_EQUITY(BigDecimal r12_EQUITY) {
		R12_EQUITY = r12_EQUITY;
	}
	public BigDecimal getR12_BONDS() {
		return R12_BONDS;
	}
	public void setR12_BONDS(BigDecimal r12_BONDS) {
		R12_BONDS = r12_BONDS;
	}
	public BigDecimal getR12_BOBCS() {
		return R12_BOBCS;
	}
	public void setR12_BOBCS(BigDecimal r12_BOBCS) {
		R12_BOBCS = r12_BOBCS;
	}
	public BigDecimal getR12_TREASURY_BILLS() {
		return R12_TREASURY_BILLS;
	}
	public void setR12_TREASURY_BILLS(BigDecimal r12_TREASURY_BILLS) {
		R12_TREASURY_BILLS = r12_TREASURY_BILLS;
	}
	public BigDecimal getR12_REPURCHASE_AGREEMENTS() {
		return R12_REPURCHASE_AGREEMENTS;
	}
	public void setR12_REPURCHASE_AGREEMENTS(BigDecimal r12_REPURCHASE_AGREEMENTS) {
		R12_REPURCHASE_AGREEMENTS = r12_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR12_COMMERCIAL_PAPER() {
		return R12_COMMERCIAL_PAPER;
	}
	public void setR12_COMMERCIAL_PAPER(BigDecimal r12_COMMERCIAL_PAPER) {
		R12_COMMERCIAL_PAPER = r12_COMMERCIAL_PAPER;
	}
	public BigDecimal getR12_CERTIFICATES_OF_DEPOSITS() {
		return R12_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR12_CERTIFICATES_OF_DEPOSITS(BigDecimal r12_CERTIFICATES_OF_DEPOSITS) {
		R12_CERTIFICATES_OF_DEPOSITS = r12_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR12_PLEDGED_ASSETS() {
		return R12_PLEDGED_ASSETS;
	}
	public void setR12_PLEDGED_ASSETS(BigDecimal r12_PLEDGED_ASSETS) {
		R12_PLEDGED_ASSETS = r12_PLEDGED_ASSETS;
	}
	public BigDecimal getR12_OTHER_SPECIFY() {
		return R12_OTHER_SPECIFY;
	}
	public void setR12_OTHER_SPECIFY(BigDecimal r12_OTHER_SPECIFY) {
		R12_OTHER_SPECIFY = r12_OTHER_SPECIFY;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_EQUITY() {
		return R13_EQUITY;
	}
	public void setR13_EQUITY(BigDecimal r13_EQUITY) {
		R13_EQUITY = r13_EQUITY;
	}
	public BigDecimal getR13_BONDS() {
		return R13_BONDS;
	}
	public void setR13_BONDS(BigDecimal r13_BONDS) {
		R13_BONDS = r13_BONDS;
	}
	public BigDecimal getR13_BOBCS() {
		return R13_BOBCS;
	}
	public void setR13_BOBCS(BigDecimal r13_BOBCS) {
		R13_BOBCS = r13_BOBCS;
	}
	public BigDecimal getR13_TREASURY_BILLS() {
		return R13_TREASURY_BILLS;
	}
	public void setR13_TREASURY_BILLS(BigDecimal r13_TREASURY_BILLS) {
		R13_TREASURY_BILLS = r13_TREASURY_BILLS;
	}
	public BigDecimal getR13_REPURCHASE_AGREEMENTS() {
		return R13_REPURCHASE_AGREEMENTS;
	}
	public void setR13_REPURCHASE_AGREEMENTS(BigDecimal r13_REPURCHASE_AGREEMENTS) {
		R13_REPURCHASE_AGREEMENTS = r13_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR13_COMMERCIAL_PAPER() {
		return R13_COMMERCIAL_PAPER;
	}
	public void setR13_COMMERCIAL_PAPER(BigDecimal r13_COMMERCIAL_PAPER) {
		R13_COMMERCIAL_PAPER = r13_COMMERCIAL_PAPER;
	}
	public BigDecimal getR13_CERTIFICATES_OF_DEPOSITS() {
		return R13_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR13_CERTIFICATES_OF_DEPOSITS(BigDecimal r13_CERTIFICATES_OF_DEPOSITS) {
		R13_CERTIFICATES_OF_DEPOSITS = r13_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR13_PLEDGED_ASSETS() {
		return R13_PLEDGED_ASSETS;
	}
	public void setR13_PLEDGED_ASSETS(BigDecimal r13_PLEDGED_ASSETS) {
		R13_PLEDGED_ASSETS = r13_PLEDGED_ASSETS;
	}
	public BigDecimal getR13_OTHER_SPECIFY() {
		return R13_OTHER_SPECIFY;
	}
	public void setR13_OTHER_SPECIFY(BigDecimal r13_OTHER_SPECIFY) {
		R13_OTHER_SPECIFY = r13_OTHER_SPECIFY;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_EQUITY() {
		return R14_EQUITY;
	}
	public void setR14_EQUITY(BigDecimal r14_EQUITY) {
		R14_EQUITY = r14_EQUITY;
	}
	public BigDecimal getR14_BONDS() {
		return R14_BONDS;
	}
	public void setR14_BONDS(BigDecimal r14_BONDS) {
		R14_BONDS = r14_BONDS;
	}
	public BigDecimal getR14_BOBCS() {
		return R14_BOBCS;
	}
	public void setR14_BOBCS(BigDecimal r14_BOBCS) {
		R14_BOBCS = r14_BOBCS;
	}
	public BigDecimal getR14_TREASURY_BILLS() {
		return R14_TREASURY_BILLS;
	}
	public void setR14_TREASURY_BILLS(BigDecimal r14_TREASURY_BILLS) {
		R14_TREASURY_BILLS = r14_TREASURY_BILLS;
	}
	public BigDecimal getR14_REPURCHASE_AGREEMENTS() {
		return R14_REPURCHASE_AGREEMENTS;
	}
	public void setR14_REPURCHASE_AGREEMENTS(BigDecimal r14_REPURCHASE_AGREEMENTS) {
		R14_REPURCHASE_AGREEMENTS = r14_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR14_COMMERCIAL_PAPER() {
		return R14_COMMERCIAL_PAPER;
	}
	public void setR14_COMMERCIAL_PAPER(BigDecimal r14_COMMERCIAL_PAPER) {
		R14_COMMERCIAL_PAPER = r14_COMMERCIAL_PAPER;
	}
	public BigDecimal getR14_CERTIFICATES_OF_DEPOSITS() {
		return R14_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR14_CERTIFICATES_OF_DEPOSITS(BigDecimal r14_CERTIFICATES_OF_DEPOSITS) {
		R14_CERTIFICATES_OF_DEPOSITS = r14_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR14_PLEDGED_ASSETS() {
		return R14_PLEDGED_ASSETS;
	}
	public void setR14_PLEDGED_ASSETS(BigDecimal r14_PLEDGED_ASSETS) {
		R14_PLEDGED_ASSETS = r14_PLEDGED_ASSETS;
	}
	public BigDecimal getR14_OTHER_SPECIFY() {
		return R14_OTHER_SPECIFY;
	}
	public void setR14_OTHER_SPECIFY(BigDecimal r14_OTHER_SPECIFY) {
		R14_OTHER_SPECIFY = r14_OTHER_SPECIFY;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_EQUITY() {
		return R15_EQUITY;
	}
	public void setR15_EQUITY(BigDecimal r15_EQUITY) {
		R15_EQUITY = r15_EQUITY;
	}
	public BigDecimal getR15_BONDS() {
		return R15_BONDS;
	}
	public void setR15_BONDS(BigDecimal r15_BONDS) {
		R15_BONDS = r15_BONDS;
	}
	public BigDecimal getR15_BOBCS() {
		return R15_BOBCS;
	}
	public void setR15_BOBCS(BigDecimal r15_BOBCS) {
		R15_BOBCS = r15_BOBCS;
	}
	public BigDecimal getR15_TREASURY_BILLS() {
		return R15_TREASURY_BILLS;
	}
	public void setR15_TREASURY_BILLS(BigDecimal r15_TREASURY_BILLS) {
		R15_TREASURY_BILLS = r15_TREASURY_BILLS;
	}
	public BigDecimal getR15_REPURCHASE_AGREEMENTS() {
		return R15_REPURCHASE_AGREEMENTS;
	}
	public void setR15_REPURCHASE_AGREEMENTS(BigDecimal r15_REPURCHASE_AGREEMENTS) {
		R15_REPURCHASE_AGREEMENTS = r15_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR15_COMMERCIAL_PAPER() {
		return R15_COMMERCIAL_PAPER;
	}
	public void setR15_COMMERCIAL_PAPER(BigDecimal r15_COMMERCIAL_PAPER) {
		R15_COMMERCIAL_PAPER = r15_COMMERCIAL_PAPER;
	}
	public BigDecimal getR15_CERTIFICATES_OF_DEPOSITS() {
		return R15_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR15_CERTIFICATES_OF_DEPOSITS(BigDecimal r15_CERTIFICATES_OF_DEPOSITS) {
		R15_CERTIFICATES_OF_DEPOSITS = r15_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR15_PLEDGED_ASSETS() {
		return R15_PLEDGED_ASSETS;
	}
	public void setR15_PLEDGED_ASSETS(BigDecimal r15_PLEDGED_ASSETS) {
		R15_PLEDGED_ASSETS = r15_PLEDGED_ASSETS;
	}
	public BigDecimal getR15_OTHER_SPECIFY() {
		return R15_OTHER_SPECIFY;
	}
	public void setR15_OTHER_SPECIFY(BigDecimal r15_OTHER_SPECIFY) {
		R15_OTHER_SPECIFY = r15_OTHER_SPECIFY;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_EQUITY() {
		return R16_EQUITY;
	}
	public void setR16_EQUITY(BigDecimal r16_EQUITY) {
		R16_EQUITY = r16_EQUITY;
	}
	public BigDecimal getR16_BONDS() {
		return R16_BONDS;
	}
	public void setR16_BONDS(BigDecimal r16_BONDS) {
		R16_BONDS = r16_BONDS;
	}
	public BigDecimal getR16_BOBCS() {
		return R16_BOBCS;
	}
	public void setR16_BOBCS(BigDecimal r16_BOBCS) {
		R16_BOBCS = r16_BOBCS;
	}
	public BigDecimal getR16_TREASURY_BILLS() {
		return R16_TREASURY_BILLS;
	}
	public void setR16_TREASURY_BILLS(BigDecimal r16_TREASURY_BILLS) {
		R16_TREASURY_BILLS = r16_TREASURY_BILLS;
	}
	public BigDecimal getR16_REPURCHASE_AGREEMENTS() {
		return R16_REPURCHASE_AGREEMENTS;
	}
	public void setR16_REPURCHASE_AGREEMENTS(BigDecimal r16_REPURCHASE_AGREEMENTS) {
		R16_REPURCHASE_AGREEMENTS = r16_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR16_COMMERCIAL_PAPER() {
		return R16_COMMERCIAL_PAPER;
	}
	public void setR16_COMMERCIAL_PAPER(BigDecimal r16_COMMERCIAL_PAPER) {
		R16_COMMERCIAL_PAPER = r16_COMMERCIAL_PAPER;
	}
	public BigDecimal getR16_CERTIFICATES_OF_DEPOSITS() {
		return R16_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR16_CERTIFICATES_OF_DEPOSITS(BigDecimal r16_CERTIFICATES_OF_DEPOSITS) {
		R16_CERTIFICATES_OF_DEPOSITS = r16_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR16_PLEDGED_ASSETS() {
		return R16_PLEDGED_ASSETS;
	}
	public void setR16_PLEDGED_ASSETS(BigDecimal r16_PLEDGED_ASSETS) {
		R16_PLEDGED_ASSETS = r16_PLEDGED_ASSETS;
	}
	public BigDecimal getR16_OTHER_SPECIFY() {
		return R16_OTHER_SPECIFY;
	}
	public void setR16_OTHER_SPECIFY(BigDecimal r16_OTHER_SPECIFY) {
		R16_OTHER_SPECIFY = r16_OTHER_SPECIFY;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_EQUITY() {
		return R17_EQUITY;
	}
	public void setR17_EQUITY(BigDecimal r17_EQUITY) {
		R17_EQUITY = r17_EQUITY;
	}
	public BigDecimal getR17_BONDS() {
		return R17_BONDS;
	}
	public void setR17_BONDS(BigDecimal r17_BONDS) {
		R17_BONDS = r17_BONDS;
	}
	public BigDecimal getR17_BOBCS() {
		return R17_BOBCS;
	}
	public void setR17_BOBCS(BigDecimal r17_BOBCS) {
		R17_BOBCS = r17_BOBCS;
	}
	public BigDecimal getR17_TREASURY_BILLS() {
		return R17_TREASURY_BILLS;
	}
	public void setR17_TREASURY_BILLS(BigDecimal r17_TREASURY_BILLS) {
		R17_TREASURY_BILLS = r17_TREASURY_BILLS;
	}
	public BigDecimal getR17_REPURCHASE_AGREEMENTS() {
		return R17_REPURCHASE_AGREEMENTS;
	}
	public void setR17_REPURCHASE_AGREEMENTS(BigDecimal r17_REPURCHASE_AGREEMENTS) {
		R17_REPURCHASE_AGREEMENTS = r17_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR17_COMMERCIAL_PAPER() {
		return R17_COMMERCIAL_PAPER;
	}
	public void setR17_COMMERCIAL_PAPER(BigDecimal r17_COMMERCIAL_PAPER) {
		R17_COMMERCIAL_PAPER = r17_COMMERCIAL_PAPER;
	}
	public BigDecimal getR17_CERTIFICATES_OF_DEPOSITS() {
		return R17_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR17_CERTIFICATES_OF_DEPOSITS(BigDecimal r17_CERTIFICATES_OF_DEPOSITS) {
		R17_CERTIFICATES_OF_DEPOSITS = r17_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR17_PLEDGED_ASSETS() {
		return R17_PLEDGED_ASSETS;
	}
	public void setR17_PLEDGED_ASSETS(BigDecimal r17_PLEDGED_ASSETS) {
		R17_PLEDGED_ASSETS = r17_PLEDGED_ASSETS;
	}
	public BigDecimal getR17_OTHER_SPECIFY() {
		return R17_OTHER_SPECIFY;
	}
	public void setR17_OTHER_SPECIFY(BigDecimal r17_OTHER_SPECIFY) {
		R17_OTHER_SPECIFY = r17_OTHER_SPECIFY;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_EQUITY() {
		return R18_EQUITY;
	}
	public void setR18_EQUITY(BigDecimal r18_EQUITY) {
		R18_EQUITY = r18_EQUITY;
	}
	public BigDecimal getR18_BONDS() {
		return R18_BONDS;
	}
	public void setR18_BONDS(BigDecimal r18_BONDS) {
		R18_BONDS = r18_BONDS;
	}
	public BigDecimal getR18_BOBCS() {
		return R18_BOBCS;
	}
	public void setR18_BOBCS(BigDecimal r18_BOBCS) {
		R18_BOBCS = r18_BOBCS;
	}
	public BigDecimal getR18_TREASURY_BILLS() {
		return R18_TREASURY_BILLS;
	}
	public void setR18_TREASURY_BILLS(BigDecimal r18_TREASURY_BILLS) {
		R18_TREASURY_BILLS = r18_TREASURY_BILLS;
	}
	public BigDecimal getR18_REPURCHASE_AGREEMENTS() {
		return R18_REPURCHASE_AGREEMENTS;
	}
	public void setR18_REPURCHASE_AGREEMENTS(BigDecimal r18_REPURCHASE_AGREEMENTS) {
		R18_REPURCHASE_AGREEMENTS = r18_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR18_COMMERCIAL_PAPER() {
		return R18_COMMERCIAL_PAPER;
	}
	public void setR18_COMMERCIAL_PAPER(BigDecimal r18_COMMERCIAL_PAPER) {
		R18_COMMERCIAL_PAPER = r18_COMMERCIAL_PAPER;
	}
	public BigDecimal getR18_CERTIFICATES_OF_DEPOSITS() {
		return R18_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR18_CERTIFICATES_OF_DEPOSITS(BigDecimal r18_CERTIFICATES_OF_DEPOSITS) {
		R18_CERTIFICATES_OF_DEPOSITS = r18_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR18_PLEDGED_ASSETS() {
		return R18_PLEDGED_ASSETS;
	}
	public void setR18_PLEDGED_ASSETS(BigDecimal r18_PLEDGED_ASSETS) {
		R18_PLEDGED_ASSETS = r18_PLEDGED_ASSETS;
	}
	public BigDecimal getR18_OTHER_SPECIFY() {
		return R18_OTHER_SPECIFY;
	}
	public void setR18_OTHER_SPECIFY(BigDecimal r18_OTHER_SPECIFY) {
		R18_OTHER_SPECIFY = r18_OTHER_SPECIFY;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_EQUITY() {
		return R19_EQUITY;
	}
	public void setR19_EQUITY(BigDecimal r19_EQUITY) {
		R19_EQUITY = r19_EQUITY;
	}
	public BigDecimal getR19_BONDS() {
		return R19_BONDS;
	}
	public void setR19_BONDS(BigDecimal r19_BONDS) {
		R19_BONDS = r19_BONDS;
	}
	public BigDecimal getR19_BOBCS() {
		return R19_BOBCS;
	}
	public void setR19_BOBCS(BigDecimal r19_BOBCS) {
		R19_BOBCS = r19_BOBCS;
	}
	public BigDecimal getR19_TREASURY_BILLS() {
		return R19_TREASURY_BILLS;
	}
	public void setR19_TREASURY_BILLS(BigDecimal r19_TREASURY_BILLS) {
		R19_TREASURY_BILLS = r19_TREASURY_BILLS;
	}
	public BigDecimal getR19_REPURCHASE_AGREEMENTS() {
		return R19_REPURCHASE_AGREEMENTS;
	}
	public void setR19_REPURCHASE_AGREEMENTS(BigDecimal r19_REPURCHASE_AGREEMENTS) {
		R19_REPURCHASE_AGREEMENTS = r19_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR19_COMMERCIAL_PAPER() {
		return R19_COMMERCIAL_PAPER;
	}
	public void setR19_COMMERCIAL_PAPER(BigDecimal r19_COMMERCIAL_PAPER) {
		R19_COMMERCIAL_PAPER = r19_COMMERCIAL_PAPER;
	}
	public BigDecimal getR19_CERTIFICATES_OF_DEPOSITS() {
		return R19_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR19_CERTIFICATES_OF_DEPOSITS(BigDecimal r19_CERTIFICATES_OF_DEPOSITS) {
		R19_CERTIFICATES_OF_DEPOSITS = r19_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR19_PLEDGED_ASSETS() {
		return R19_PLEDGED_ASSETS;
	}
	public void setR19_PLEDGED_ASSETS(BigDecimal r19_PLEDGED_ASSETS) {
		R19_PLEDGED_ASSETS = r19_PLEDGED_ASSETS;
	}
	public BigDecimal getR19_OTHER_SPECIFY() {
		return R19_OTHER_SPECIFY;
	}
	public void setR19_OTHER_SPECIFY(BigDecimal r19_OTHER_SPECIFY) {
		R19_OTHER_SPECIFY = r19_OTHER_SPECIFY;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_EQUITY() {
		return R20_EQUITY;
	}
	public void setR20_EQUITY(BigDecimal r20_EQUITY) {
		R20_EQUITY = r20_EQUITY;
	}
	public BigDecimal getR20_BONDS() {
		return R20_BONDS;
	}
	public void setR20_BONDS(BigDecimal r20_BONDS) {
		R20_BONDS = r20_BONDS;
	}
	public BigDecimal getR20_BOBCS() {
		return R20_BOBCS;
	}
	public void setR20_BOBCS(BigDecimal r20_BOBCS) {
		R20_BOBCS = r20_BOBCS;
	}
	public BigDecimal getR20_TREASURY_BILLS() {
		return R20_TREASURY_BILLS;
	}
	public void setR20_TREASURY_BILLS(BigDecimal r20_TREASURY_BILLS) {
		R20_TREASURY_BILLS = r20_TREASURY_BILLS;
	}
	public BigDecimal getR20_REPURCHASE_AGREEMENTS() {
		return R20_REPURCHASE_AGREEMENTS;
	}
	public void setR20_REPURCHASE_AGREEMENTS(BigDecimal r20_REPURCHASE_AGREEMENTS) {
		R20_REPURCHASE_AGREEMENTS = r20_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR20_COMMERCIAL_PAPER() {
		return R20_COMMERCIAL_PAPER;
	}
	public void setR20_COMMERCIAL_PAPER(BigDecimal r20_COMMERCIAL_PAPER) {
		R20_COMMERCIAL_PAPER = r20_COMMERCIAL_PAPER;
	}
	public BigDecimal getR20_CERTIFICATES_OF_DEPOSITS() {
		return R20_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR20_CERTIFICATES_OF_DEPOSITS(BigDecimal r20_CERTIFICATES_OF_DEPOSITS) {
		R20_CERTIFICATES_OF_DEPOSITS = r20_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR20_PLEDGED_ASSETS() {
		return R20_PLEDGED_ASSETS;
	}
	public void setR20_PLEDGED_ASSETS(BigDecimal r20_PLEDGED_ASSETS) {
		R20_PLEDGED_ASSETS = r20_PLEDGED_ASSETS;
	}
	public BigDecimal getR20_OTHER_SPECIFY() {
		return R20_OTHER_SPECIFY;
	}
	public void setR20_OTHER_SPECIFY(BigDecimal r20_OTHER_SPECIFY) {
		R20_OTHER_SPECIFY = r20_OTHER_SPECIFY;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_EQUITY() {
		return R21_EQUITY;
	}
	public void setR21_EQUITY(BigDecimal r21_EQUITY) {
		R21_EQUITY = r21_EQUITY;
	}
	public BigDecimal getR21_BONDS() {
		return R21_BONDS;
	}
	public void setR21_BONDS(BigDecimal r21_BONDS) {
		R21_BONDS = r21_BONDS;
	}
	public BigDecimal getR21_BOBCS() {
		return R21_BOBCS;
	}
	public void setR21_BOBCS(BigDecimal r21_BOBCS) {
		R21_BOBCS = r21_BOBCS;
	}
	public BigDecimal getR21_TREASURY_BILLS() {
		return R21_TREASURY_BILLS;
	}
	public void setR21_TREASURY_BILLS(BigDecimal r21_TREASURY_BILLS) {
		R21_TREASURY_BILLS = r21_TREASURY_BILLS;
	}
	public BigDecimal getR21_REPURCHASE_AGREEMENTS() {
		return R21_REPURCHASE_AGREEMENTS;
	}
	public void setR21_REPURCHASE_AGREEMENTS(BigDecimal r21_REPURCHASE_AGREEMENTS) {
		R21_REPURCHASE_AGREEMENTS = r21_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR21_COMMERCIAL_PAPER() {
		return R21_COMMERCIAL_PAPER;
	}
	public void setR21_COMMERCIAL_PAPER(BigDecimal r21_COMMERCIAL_PAPER) {
		R21_COMMERCIAL_PAPER = r21_COMMERCIAL_PAPER;
	}
	public BigDecimal getR21_CERTIFICATES_OF_DEPOSITS() {
		return R21_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR21_CERTIFICATES_OF_DEPOSITS(BigDecimal r21_CERTIFICATES_OF_DEPOSITS) {
		R21_CERTIFICATES_OF_DEPOSITS = r21_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR21_PLEDGED_ASSETS() {
		return R21_PLEDGED_ASSETS;
	}
	public void setR21_PLEDGED_ASSETS(BigDecimal r21_PLEDGED_ASSETS) {
		R21_PLEDGED_ASSETS = r21_PLEDGED_ASSETS;
	}
	public BigDecimal getR21_OTHER_SPECIFY() {
		return R21_OTHER_SPECIFY;
	}
	public void setR21_OTHER_SPECIFY(BigDecimal r21_OTHER_SPECIFY) {
		R21_OTHER_SPECIFY = r21_OTHER_SPECIFY;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_EQUITY() {
		return R22_EQUITY;
	}
	public void setR22_EQUITY(BigDecimal r22_EQUITY) {
		R22_EQUITY = r22_EQUITY;
	}
	public BigDecimal getR22_BONDS() {
		return R22_BONDS;
	}
	public void setR22_BONDS(BigDecimal r22_BONDS) {
		R22_BONDS = r22_BONDS;
	}
	public BigDecimal getR22_BOBCS() {
		return R22_BOBCS;
	}
	public void setR22_BOBCS(BigDecimal r22_BOBCS) {
		R22_BOBCS = r22_BOBCS;
	}
	public BigDecimal getR22_TREASURY_BILLS() {
		return R22_TREASURY_BILLS;
	}
	public void setR22_TREASURY_BILLS(BigDecimal r22_TREASURY_BILLS) {
		R22_TREASURY_BILLS = r22_TREASURY_BILLS;
	}
	public BigDecimal getR22_REPURCHASE_AGREEMENTS() {
		return R22_REPURCHASE_AGREEMENTS;
	}
	public void setR22_REPURCHASE_AGREEMENTS(BigDecimal r22_REPURCHASE_AGREEMENTS) {
		R22_REPURCHASE_AGREEMENTS = r22_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR22_COMMERCIAL_PAPER() {
		return R22_COMMERCIAL_PAPER;
	}
	public void setR22_COMMERCIAL_PAPER(BigDecimal r22_COMMERCIAL_PAPER) {
		R22_COMMERCIAL_PAPER = r22_COMMERCIAL_PAPER;
	}
	public BigDecimal getR22_CERTIFICATES_OF_DEPOSITS() {
		return R22_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR22_CERTIFICATES_OF_DEPOSITS(BigDecimal r22_CERTIFICATES_OF_DEPOSITS) {
		R22_CERTIFICATES_OF_DEPOSITS = r22_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR22_PLEDGED_ASSETS() {
		return R22_PLEDGED_ASSETS;
	}
	public void setR22_PLEDGED_ASSETS(BigDecimal r22_PLEDGED_ASSETS) {
		R22_PLEDGED_ASSETS = r22_PLEDGED_ASSETS;
	}
	public BigDecimal getR22_OTHER_SPECIFY() {
		return R22_OTHER_SPECIFY;
	}
	public void setR22_OTHER_SPECIFY(BigDecimal r22_OTHER_SPECIFY) {
		R22_OTHER_SPECIFY = r22_OTHER_SPECIFY;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_EQUITY() {
		return R23_EQUITY;
	}
	public void setR23_EQUITY(BigDecimal r23_EQUITY) {
		R23_EQUITY = r23_EQUITY;
	}
	public BigDecimal getR23_BONDS() {
		return R23_BONDS;
	}
	public void setR23_BONDS(BigDecimal r23_BONDS) {
		R23_BONDS = r23_BONDS;
	}
	public BigDecimal getR23_BOBCS() {
		return R23_BOBCS;
	}
	public void setR23_BOBCS(BigDecimal r23_BOBCS) {
		R23_BOBCS = r23_BOBCS;
	}
	public BigDecimal getR23_TREASURY_BILLS() {
		return R23_TREASURY_BILLS;
	}
	public void setR23_TREASURY_BILLS(BigDecimal r23_TREASURY_BILLS) {
		R23_TREASURY_BILLS = r23_TREASURY_BILLS;
	}
	public BigDecimal getR23_REPURCHASE_AGREEMENTS() {
		return R23_REPURCHASE_AGREEMENTS;
	}
	public void setR23_REPURCHASE_AGREEMENTS(BigDecimal r23_REPURCHASE_AGREEMENTS) {
		R23_REPURCHASE_AGREEMENTS = r23_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR23_COMMERCIAL_PAPER() {
		return R23_COMMERCIAL_PAPER;
	}
	public void setR23_COMMERCIAL_PAPER(BigDecimal r23_COMMERCIAL_PAPER) {
		R23_COMMERCIAL_PAPER = r23_COMMERCIAL_PAPER;
	}
	public BigDecimal getR23_CERTIFICATES_OF_DEPOSITS() {
		return R23_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR23_CERTIFICATES_OF_DEPOSITS(BigDecimal r23_CERTIFICATES_OF_DEPOSITS) {
		R23_CERTIFICATES_OF_DEPOSITS = r23_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR23_PLEDGED_ASSETS() {
		return R23_PLEDGED_ASSETS;
	}
	public void setR23_PLEDGED_ASSETS(BigDecimal r23_PLEDGED_ASSETS) {
		R23_PLEDGED_ASSETS = r23_PLEDGED_ASSETS;
	}
	public BigDecimal getR23_OTHER_SPECIFY() {
		return R23_OTHER_SPECIFY;
	}
	public void setR23_OTHER_SPECIFY(BigDecimal r23_OTHER_SPECIFY) {
		R23_OTHER_SPECIFY = r23_OTHER_SPECIFY;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_EQUITY() {
		return R24_EQUITY;
	}
	public void setR24_EQUITY(BigDecimal r24_EQUITY) {
		R24_EQUITY = r24_EQUITY;
	}
	public BigDecimal getR24_BONDS() {
		return R24_BONDS;
	}
	public void setR24_BONDS(BigDecimal r24_BONDS) {
		R24_BONDS = r24_BONDS;
	}
	public BigDecimal getR24_BOBCS() {
		return R24_BOBCS;
	}
	public void setR24_BOBCS(BigDecimal r24_BOBCS) {
		R24_BOBCS = r24_BOBCS;
	}
	public BigDecimal getR24_TREASURY_BILLS() {
		return R24_TREASURY_BILLS;
	}
	public void setR24_TREASURY_BILLS(BigDecimal r24_TREASURY_BILLS) {
		R24_TREASURY_BILLS = r24_TREASURY_BILLS;
	}
	public BigDecimal getR24_REPURCHASE_AGREEMENTS() {
		return R24_REPURCHASE_AGREEMENTS;
	}
	public void setR24_REPURCHASE_AGREEMENTS(BigDecimal r24_REPURCHASE_AGREEMENTS) {
		R24_REPURCHASE_AGREEMENTS = r24_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR24_COMMERCIAL_PAPER() {
		return R24_COMMERCIAL_PAPER;
	}
	public void setR24_COMMERCIAL_PAPER(BigDecimal r24_COMMERCIAL_PAPER) {
		R24_COMMERCIAL_PAPER = r24_COMMERCIAL_PAPER;
	}
	public BigDecimal getR24_CERTIFICATES_OF_DEPOSITS() {
		return R24_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR24_CERTIFICATES_OF_DEPOSITS(BigDecimal r24_CERTIFICATES_OF_DEPOSITS) {
		R24_CERTIFICATES_OF_DEPOSITS = r24_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR24_PLEDGED_ASSETS() {
		return R24_PLEDGED_ASSETS;
	}
	public void setR24_PLEDGED_ASSETS(BigDecimal r24_PLEDGED_ASSETS) {
		R24_PLEDGED_ASSETS = r24_PLEDGED_ASSETS;
	}
	public BigDecimal getR24_OTHER_SPECIFY() {
		return R24_OTHER_SPECIFY;
	}
	public void setR24_OTHER_SPECIFY(BigDecimal r24_OTHER_SPECIFY) {
		R24_OTHER_SPECIFY = r24_OTHER_SPECIFY;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_EQUITY() {
		return R25_EQUITY;
	}
	public void setR25_EQUITY(BigDecimal r25_EQUITY) {
		R25_EQUITY = r25_EQUITY;
	}
	public BigDecimal getR25_BONDS() {
		return R25_BONDS;
	}
	public void setR25_BONDS(BigDecimal r25_BONDS) {
		R25_BONDS = r25_BONDS;
	}
	public BigDecimal getR25_BOBCS() {
		return R25_BOBCS;
	}
	public void setR25_BOBCS(BigDecimal r25_BOBCS) {
		R25_BOBCS = r25_BOBCS;
	}
	public BigDecimal getR25_TREASURY_BILLS() {
		return R25_TREASURY_BILLS;
	}
	public void setR25_TREASURY_BILLS(BigDecimal r25_TREASURY_BILLS) {
		R25_TREASURY_BILLS = r25_TREASURY_BILLS;
	}
	public BigDecimal getR25_REPURCHASE_AGREEMENTS() {
		return R25_REPURCHASE_AGREEMENTS;
	}
	public void setR25_REPURCHASE_AGREEMENTS(BigDecimal r25_REPURCHASE_AGREEMENTS) {
		R25_REPURCHASE_AGREEMENTS = r25_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR25_COMMERCIAL_PAPER() {
		return R25_COMMERCIAL_PAPER;
	}
	public void setR25_COMMERCIAL_PAPER(BigDecimal r25_COMMERCIAL_PAPER) {
		R25_COMMERCIAL_PAPER = r25_COMMERCIAL_PAPER;
	}
	public BigDecimal getR25_CERTIFICATES_OF_DEPOSITS() {
		return R25_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR25_CERTIFICATES_OF_DEPOSITS(BigDecimal r25_CERTIFICATES_OF_DEPOSITS) {
		R25_CERTIFICATES_OF_DEPOSITS = r25_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR25_PLEDGED_ASSETS() {
		return R25_PLEDGED_ASSETS;
	}
	public void setR25_PLEDGED_ASSETS(BigDecimal r25_PLEDGED_ASSETS) {
		R25_PLEDGED_ASSETS = r25_PLEDGED_ASSETS;
	}
	public BigDecimal getR25_OTHER_SPECIFY() {
		return R25_OTHER_SPECIFY;
	}
	public void setR25_OTHER_SPECIFY(BigDecimal r25_OTHER_SPECIFY) {
		R25_OTHER_SPECIFY = r25_OTHER_SPECIFY;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_EQUITY() {
		return R26_EQUITY;
	}
	public void setR26_EQUITY(BigDecimal r26_EQUITY) {
		R26_EQUITY = r26_EQUITY;
	}
	public BigDecimal getR26_BONDS() {
		return R26_BONDS;
	}
	public void setR26_BONDS(BigDecimal r26_BONDS) {
		R26_BONDS = r26_BONDS;
	}
	public BigDecimal getR26_BOBCS() {
		return R26_BOBCS;
	}
	public void setR26_BOBCS(BigDecimal r26_BOBCS) {
		R26_BOBCS = r26_BOBCS;
	}
	public BigDecimal getR26_TREASURY_BILLS() {
		return R26_TREASURY_BILLS;
	}
	public void setR26_TREASURY_BILLS(BigDecimal r26_TREASURY_BILLS) {
		R26_TREASURY_BILLS = r26_TREASURY_BILLS;
	}
	public BigDecimal getR26_REPURCHASE_AGREEMENTS() {
		return R26_REPURCHASE_AGREEMENTS;
	}
	public void setR26_REPURCHASE_AGREEMENTS(BigDecimal r26_REPURCHASE_AGREEMENTS) {
		R26_REPURCHASE_AGREEMENTS = r26_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR26_COMMERCIAL_PAPER() {
		return R26_COMMERCIAL_PAPER;
	}
	public void setR26_COMMERCIAL_PAPER(BigDecimal r26_COMMERCIAL_PAPER) {
		R26_COMMERCIAL_PAPER = r26_COMMERCIAL_PAPER;
	}
	public BigDecimal getR26_CERTIFICATES_OF_DEPOSITS() {
		return R26_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR26_CERTIFICATES_OF_DEPOSITS(BigDecimal r26_CERTIFICATES_OF_DEPOSITS) {
		R26_CERTIFICATES_OF_DEPOSITS = r26_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR26_PLEDGED_ASSETS() {
		return R26_PLEDGED_ASSETS;
	}
	public void setR26_PLEDGED_ASSETS(BigDecimal r26_PLEDGED_ASSETS) {
		R26_PLEDGED_ASSETS = r26_PLEDGED_ASSETS;
	}
	public BigDecimal getR26_OTHER_SPECIFY() {
		return R26_OTHER_SPECIFY;
	}
	public void setR26_OTHER_SPECIFY(BigDecimal r26_OTHER_SPECIFY) {
		R26_OTHER_SPECIFY = r26_OTHER_SPECIFY;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_EQUITY() {
		return R27_EQUITY;
	}
	public void setR27_EQUITY(BigDecimal r27_EQUITY) {
		R27_EQUITY = r27_EQUITY;
	}
	public BigDecimal getR27_BONDS() {
		return R27_BONDS;
	}
	public void setR27_BONDS(BigDecimal r27_BONDS) {
		R27_BONDS = r27_BONDS;
	}
	public BigDecimal getR27_BOBCS() {
		return R27_BOBCS;
	}
	public void setR27_BOBCS(BigDecimal r27_BOBCS) {
		R27_BOBCS = r27_BOBCS;
	}
	public BigDecimal getR27_TREASURY_BILLS() {
		return R27_TREASURY_BILLS;
	}
	public void setR27_TREASURY_BILLS(BigDecimal r27_TREASURY_BILLS) {
		R27_TREASURY_BILLS = r27_TREASURY_BILLS;
	}
	public BigDecimal getR27_REPURCHASE_AGREEMENTS() {
		return R27_REPURCHASE_AGREEMENTS;
	}
	public void setR27_REPURCHASE_AGREEMENTS(BigDecimal r27_REPURCHASE_AGREEMENTS) {
		R27_REPURCHASE_AGREEMENTS = r27_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR27_COMMERCIAL_PAPER() {
		return R27_COMMERCIAL_PAPER;
	}
	public void setR27_COMMERCIAL_PAPER(BigDecimal r27_COMMERCIAL_PAPER) {
		R27_COMMERCIAL_PAPER = r27_COMMERCIAL_PAPER;
	}
	public BigDecimal getR27_CERTIFICATES_OF_DEPOSITS() {
		return R27_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR27_CERTIFICATES_OF_DEPOSITS(BigDecimal r27_CERTIFICATES_OF_DEPOSITS) {
		R27_CERTIFICATES_OF_DEPOSITS = r27_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR27_PLEDGED_ASSETS() {
		return R27_PLEDGED_ASSETS;
	}
	public void setR27_PLEDGED_ASSETS(BigDecimal r27_PLEDGED_ASSETS) {
		R27_PLEDGED_ASSETS = r27_PLEDGED_ASSETS;
	}
	public BigDecimal getR27_OTHER_SPECIFY() {
		return R27_OTHER_SPECIFY;
	}
	public void setR27_OTHER_SPECIFY(BigDecimal r27_OTHER_SPECIFY) {
		R27_OTHER_SPECIFY = r27_OTHER_SPECIFY;
	}
	public BigDecimal getR27_TOTAL() {
		return R27_TOTAL;
	}
	public void setR27_TOTAL(BigDecimal r27_TOTAL) {
		R27_TOTAL = r27_TOTAL;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_EQUITY() {
		return R28_EQUITY;
	}
	public void setR28_EQUITY(BigDecimal r28_EQUITY) {
		R28_EQUITY = r28_EQUITY;
	}
	public BigDecimal getR28_BONDS() {
		return R28_BONDS;
	}
	public void setR28_BONDS(BigDecimal r28_BONDS) {
		R28_BONDS = r28_BONDS;
	}
	public BigDecimal getR28_BOBCS() {
		return R28_BOBCS;
	}
	public void setR28_BOBCS(BigDecimal r28_BOBCS) {
		R28_BOBCS = r28_BOBCS;
	}
	public BigDecimal getR28_TREASURY_BILLS() {
		return R28_TREASURY_BILLS;
	}
	public void setR28_TREASURY_BILLS(BigDecimal r28_TREASURY_BILLS) {
		R28_TREASURY_BILLS = r28_TREASURY_BILLS;
	}
	public BigDecimal getR28_REPURCHASE_AGREEMENTS() {
		return R28_REPURCHASE_AGREEMENTS;
	}
	public void setR28_REPURCHASE_AGREEMENTS(BigDecimal r28_REPURCHASE_AGREEMENTS) {
		R28_REPURCHASE_AGREEMENTS = r28_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR28_COMMERCIAL_PAPER() {
		return R28_COMMERCIAL_PAPER;
	}
	public void setR28_COMMERCIAL_PAPER(BigDecimal r28_COMMERCIAL_PAPER) {
		R28_COMMERCIAL_PAPER = r28_COMMERCIAL_PAPER;
	}
	public BigDecimal getR28_CERTIFICATES_OF_DEPOSITS() {
		return R28_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR28_CERTIFICATES_OF_DEPOSITS(BigDecimal r28_CERTIFICATES_OF_DEPOSITS) {
		R28_CERTIFICATES_OF_DEPOSITS = r28_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR28_PLEDGED_ASSETS() {
		return R28_PLEDGED_ASSETS;
	}
	public void setR28_PLEDGED_ASSETS(BigDecimal r28_PLEDGED_ASSETS) {
		R28_PLEDGED_ASSETS = r28_PLEDGED_ASSETS;
	}
	public BigDecimal getR28_OTHER_SPECIFY() {
		return R28_OTHER_SPECIFY;
	}
	public void setR28_OTHER_SPECIFY(BigDecimal r28_OTHER_SPECIFY) {
		R28_OTHER_SPECIFY = r28_OTHER_SPECIFY;
	}
	public BigDecimal getR28_TOTAL() {
		return R28_TOTAL;
	}
	public void setR28_TOTAL(BigDecimal r28_TOTAL) {
		R28_TOTAL = r28_TOTAL;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_EQUITY() {
		return R29_EQUITY;
	}
	public void setR29_EQUITY(BigDecimal r29_EQUITY) {
		R29_EQUITY = r29_EQUITY;
	}
	public BigDecimal getR29_BONDS() {
		return R29_BONDS;
	}
	public void setR29_BONDS(BigDecimal r29_BONDS) {
		R29_BONDS = r29_BONDS;
	}
	public BigDecimal getR29_BOBCS() {
		return R29_BOBCS;
	}
	public void setR29_BOBCS(BigDecimal r29_BOBCS) {
		R29_BOBCS = r29_BOBCS;
	}
	public BigDecimal getR29_TREASURY_BILLS() {
		return R29_TREASURY_BILLS;
	}
	public void setR29_TREASURY_BILLS(BigDecimal r29_TREASURY_BILLS) {
		R29_TREASURY_BILLS = r29_TREASURY_BILLS;
	}
	public BigDecimal getR29_REPURCHASE_AGREEMENTS() {
		return R29_REPURCHASE_AGREEMENTS;
	}
	public void setR29_REPURCHASE_AGREEMENTS(BigDecimal r29_REPURCHASE_AGREEMENTS) {
		R29_REPURCHASE_AGREEMENTS = r29_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR29_COMMERCIAL_PAPER() {
		return R29_COMMERCIAL_PAPER;
	}
	public void setR29_COMMERCIAL_PAPER(BigDecimal r29_COMMERCIAL_PAPER) {
		R29_COMMERCIAL_PAPER = r29_COMMERCIAL_PAPER;
	}
	public BigDecimal getR29_CERTIFICATES_OF_DEPOSITS() {
		return R29_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR29_CERTIFICATES_OF_DEPOSITS(BigDecimal r29_CERTIFICATES_OF_DEPOSITS) {
		R29_CERTIFICATES_OF_DEPOSITS = r29_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR29_PLEDGED_ASSETS() {
		return R29_PLEDGED_ASSETS;
	}
	public void setR29_PLEDGED_ASSETS(BigDecimal r29_PLEDGED_ASSETS) {
		R29_PLEDGED_ASSETS = r29_PLEDGED_ASSETS;
	}
	public BigDecimal getR29_OTHER_SPECIFY() {
		return R29_OTHER_SPECIFY;
	}
	public void setR29_OTHER_SPECIFY(BigDecimal r29_OTHER_SPECIFY) {
		R29_OTHER_SPECIFY = r29_OTHER_SPECIFY;
	}
	public BigDecimal getR29_TOTAL() {
		return R29_TOTAL;
	}
	public void setR29_TOTAL(BigDecimal r29_TOTAL) {
		R29_TOTAL = r29_TOTAL;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_EQUITY() {
		return R30_EQUITY;
	}
	public void setR30_EQUITY(BigDecimal r30_EQUITY) {
		R30_EQUITY = r30_EQUITY;
	}
	public BigDecimal getR30_BONDS() {
		return R30_BONDS;
	}
	public void setR30_BONDS(BigDecimal r30_BONDS) {
		R30_BONDS = r30_BONDS;
	}
	public BigDecimal getR30_BOBCS() {
		return R30_BOBCS;
	}
	public void setR30_BOBCS(BigDecimal r30_BOBCS) {
		R30_BOBCS = r30_BOBCS;
	}
	public BigDecimal getR30_TREASURY_BILLS() {
		return R30_TREASURY_BILLS;
	}
	public void setR30_TREASURY_BILLS(BigDecimal r30_TREASURY_BILLS) {
		R30_TREASURY_BILLS = r30_TREASURY_BILLS;
	}
	public BigDecimal getR30_REPURCHASE_AGREEMENTS() {
		return R30_REPURCHASE_AGREEMENTS;
	}
	public void setR30_REPURCHASE_AGREEMENTS(BigDecimal r30_REPURCHASE_AGREEMENTS) {
		R30_REPURCHASE_AGREEMENTS = r30_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR30_COMMERCIAL_PAPER() {
		return R30_COMMERCIAL_PAPER;
	}
	public void setR30_COMMERCIAL_PAPER(BigDecimal r30_COMMERCIAL_PAPER) {
		R30_COMMERCIAL_PAPER = r30_COMMERCIAL_PAPER;
	}
	public BigDecimal getR30_CERTIFICATES_OF_DEPOSITS() {
		return R30_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR30_CERTIFICATES_OF_DEPOSITS(BigDecimal r30_CERTIFICATES_OF_DEPOSITS) {
		R30_CERTIFICATES_OF_DEPOSITS = r30_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR30_PLEDGED_ASSETS() {
		return R30_PLEDGED_ASSETS;
	}
	public void setR30_PLEDGED_ASSETS(BigDecimal r30_PLEDGED_ASSETS) {
		R30_PLEDGED_ASSETS = r30_PLEDGED_ASSETS;
	}
	public BigDecimal getR30_OTHER_SPECIFY() {
		return R30_OTHER_SPECIFY;
	}
	public void setR30_OTHER_SPECIFY(BigDecimal r30_OTHER_SPECIFY) {
		R30_OTHER_SPECIFY = r30_OTHER_SPECIFY;
	}
	public BigDecimal getR30_TOTAL() {
		return R30_TOTAL;
	}
	public void setR30_TOTAL(BigDecimal r30_TOTAL) {
		R30_TOTAL = r30_TOTAL;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_EQUITY() {
		return R31_EQUITY;
	}
	public void setR31_EQUITY(BigDecimal r31_EQUITY) {
		R31_EQUITY = r31_EQUITY;
	}
	public BigDecimal getR31_BONDS() {
		return R31_BONDS;
	}
	public void setR31_BONDS(BigDecimal r31_BONDS) {
		R31_BONDS = r31_BONDS;
	}
	public BigDecimal getR31_BOBCS() {
		return R31_BOBCS;
	}
	public void setR31_BOBCS(BigDecimal r31_BOBCS) {
		R31_BOBCS = r31_BOBCS;
	}
	public BigDecimal getR31_TREASURY_BILLS() {
		return R31_TREASURY_BILLS;
	}
	public void setR31_TREASURY_BILLS(BigDecimal r31_TREASURY_BILLS) {
		R31_TREASURY_BILLS = r31_TREASURY_BILLS;
	}
	public BigDecimal getR31_REPURCHASE_AGREEMENTS() {
		return R31_REPURCHASE_AGREEMENTS;
	}
	public void setR31_REPURCHASE_AGREEMENTS(BigDecimal r31_REPURCHASE_AGREEMENTS) {
		R31_REPURCHASE_AGREEMENTS = r31_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR31_COMMERCIAL_PAPER() {
		return R31_COMMERCIAL_PAPER;
	}
	public void setR31_COMMERCIAL_PAPER(BigDecimal r31_COMMERCIAL_PAPER) {
		R31_COMMERCIAL_PAPER = r31_COMMERCIAL_PAPER;
	}
	public BigDecimal getR31_CERTIFICATES_OF_DEPOSITS() {
		return R31_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR31_CERTIFICATES_OF_DEPOSITS(BigDecimal r31_CERTIFICATES_OF_DEPOSITS) {
		R31_CERTIFICATES_OF_DEPOSITS = r31_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR31_PLEDGED_ASSETS() {
		return R31_PLEDGED_ASSETS;
	}
	public void setR31_PLEDGED_ASSETS(BigDecimal r31_PLEDGED_ASSETS) {
		R31_PLEDGED_ASSETS = r31_PLEDGED_ASSETS;
	}
	public BigDecimal getR31_OTHER_SPECIFY() {
		return R31_OTHER_SPECIFY;
	}
	public void setR31_OTHER_SPECIFY(BigDecimal r31_OTHER_SPECIFY) {
		R31_OTHER_SPECIFY = r31_OTHER_SPECIFY;
	}
	public BigDecimal getR31_TOTAL() {
		return R31_TOTAL;
	}
	public void setR31_TOTAL(BigDecimal r31_TOTAL) {
		R31_TOTAL = r31_TOTAL;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_EQUITY() {
		return R32_EQUITY;
	}
	public void setR32_EQUITY(BigDecimal r32_EQUITY) {
		R32_EQUITY = r32_EQUITY;
	}
	public BigDecimal getR32_BONDS() {
		return R32_BONDS;
	}
	public void setR32_BONDS(BigDecimal r32_BONDS) {
		R32_BONDS = r32_BONDS;
	}
	public BigDecimal getR32_BOBCS() {
		return R32_BOBCS;
	}
	public void setR32_BOBCS(BigDecimal r32_BOBCS) {
		R32_BOBCS = r32_BOBCS;
	}
	public BigDecimal getR32_TREASURY_BILLS() {
		return R32_TREASURY_BILLS;
	}
	public void setR32_TREASURY_BILLS(BigDecimal r32_TREASURY_BILLS) {
		R32_TREASURY_BILLS = r32_TREASURY_BILLS;
	}
	public BigDecimal getR32_REPURCHASE_AGREEMENTS() {
		return R32_REPURCHASE_AGREEMENTS;
	}
	public void setR32_REPURCHASE_AGREEMENTS(BigDecimal r32_REPURCHASE_AGREEMENTS) {
		R32_REPURCHASE_AGREEMENTS = r32_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR32_COMMERCIAL_PAPER() {
		return R32_COMMERCIAL_PAPER;
	}
	public void setR32_COMMERCIAL_PAPER(BigDecimal r32_COMMERCIAL_PAPER) {
		R32_COMMERCIAL_PAPER = r32_COMMERCIAL_PAPER;
	}
	public BigDecimal getR32_CERTIFICATES_OF_DEPOSITS() {
		return R32_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR32_CERTIFICATES_OF_DEPOSITS(BigDecimal r32_CERTIFICATES_OF_DEPOSITS) {
		R32_CERTIFICATES_OF_DEPOSITS = r32_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR32_PLEDGED_ASSETS() {
		return R32_PLEDGED_ASSETS;
	}
	public void setR32_PLEDGED_ASSETS(BigDecimal r32_PLEDGED_ASSETS) {
		R32_PLEDGED_ASSETS = r32_PLEDGED_ASSETS;
	}
	public BigDecimal getR32_OTHER_SPECIFY() {
		return R32_OTHER_SPECIFY;
	}
	public void setR32_OTHER_SPECIFY(BigDecimal r32_OTHER_SPECIFY) {
		R32_OTHER_SPECIFY = r32_OTHER_SPECIFY;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_EQUITY() {
		return R33_EQUITY;
	}
	public void setR33_EQUITY(BigDecimal r33_EQUITY) {
		R33_EQUITY = r33_EQUITY;
	}
	public BigDecimal getR33_BONDS() {
		return R33_BONDS;
	}
	public void setR33_BONDS(BigDecimal r33_BONDS) {
		R33_BONDS = r33_BONDS;
	}
	public BigDecimal getR33_BOBCS() {
		return R33_BOBCS;
	}
	public void setR33_BOBCS(BigDecimal r33_BOBCS) {
		R33_BOBCS = r33_BOBCS;
	}
	public BigDecimal getR33_TREASURY_BILLS() {
		return R33_TREASURY_BILLS;
	}
	public void setR33_TREASURY_BILLS(BigDecimal r33_TREASURY_BILLS) {
		R33_TREASURY_BILLS = r33_TREASURY_BILLS;
	}
	public BigDecimal getR33_REPURCHASE_AGREEMENTS() {
		return R33_REPURCHASE_AGREEMENTS;
	}
	public void setR33_REPURCHASE_AGREEMENTS(BigDecimal r33_REPURCHASE_AGREEMENTS) {
		R33_REPURCHASE_AGREEMENTS = r33_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR33_COMMERCIAL_PAPER() {
		return R33_COMMERCIAL_PAPER;
	}
	public void setR33_COMMERCIAL_PAPER(BigDecimal r33_COMMERCIAL_PAPER) {
		R33_COMMERCIAL_PAPER = r33_COMMERCIAL_PAPER;
	}
	public BigDecimal getR33_CERTIFICATES_OF_DEPOSITS() {
		return R33_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR33_CERTIFICATES_OF_DEPOSITS(BigDecimal r33_CERTIFICATES_OF_DEPOSITS) {
		R33_CERTIFICATES_OF_DEPOSITS = r33_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR33_PLEDGED_ASSETS() {
		return R33_PLEDGED_ASSETS;
	}
	public void setR33_PLEDGED_ASSETS(BigDecimal r33_PLEDGED_ASSETS) {
		R33_PLEDGED_ASSETS = r33_PLEDGED_ASSETS;
	}
	public BigDecimal getR33_OTHER_SPECIFY() {
		return R33_OTHER_SPECIFY;
	}
	public void setR33_OTHER_SPECIFY(BigDecimal r33_OTHER_SPECIFY) {
		R33_OTHER_SPECIFY = r33_OTHER_SPECIFY;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_EQUITY() {
		return R34_EQUITY;
	}
	public void setR34_EQUITY(BigDecimal r34_EQUITY) {
		R34_EQUITY = r34_EQUITY;
	}
	public BigDecimal getR34_BONDS() {
		return R34_BONDS;
	}
	public void setR34_BONDS(BigDecimal r34_BONDS) {
		R34_BONDS = r34_BONDS;
	}
	public BigDecimal getR34_BOBCS() {
		return R34_BOBCS;
	}
	public void setR34_BOBCS(BigDecimal r34_BOBCS) {
		R34_BOBCS = r34_BOBCS;
	}
	public BigDecimal getR34_TREASURY_BILLS() {
		return R34_TREASURY_BILLS;
	}
	public void setR34_TREASURY_BILLS(BigDecimal r34_TREASURY_BILLS) {
		R34_TREASURY_BILLS = r34_TREASURY_BILLS;
	}
	public BigDecimal getR34_REPURCHASE_AGREEMENTS() {
		return R34_REPURCHASE_AGREEMENTS;
	}
	public void setR34_REPURCHASE_AGREEMENTS(BigDecimal r34_REPURCHASE_AGREEMENTS) {
		R34_REPURCHASE_AGREEMENTS = r34_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR34_COMMERCIAL_PAPER() {
		return R34_COMMERCIAL_PAPER;
	}
	public void setR34_COMMERCIAL_PAPER(BigDecimal r34_COMMERCIAL_PAPER) {
		R34_COMMERCIAL_PAPER = r34_COMMERCIAL_PAPER;
	}
	public BigDecimal getR34_CERTIFICATES_OF_DEPOSITS() {
		return R34_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR34_CERTIFICATES_OF_DEPOSITS(BigDecimal r34_CERTIFICATES_OF_DEPOSITS) {
		R34_CERTIFICATES_OF_DEPOSITS = r34_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR34_PLEDGED_ASSETS() {
		return R34_PLEDGED_ASSETS;
	}
	public void setR34_PLEDGED_ASSETS(BigDecimal r34_PLEDGED_ASSETS) {
		R34_PLEDGED_ASSETS = r34_PLEDGED_ASSETS;
	}
	public BigDecimal getR34_OTHER_SPECIFY() {
		return R34_OTHER_SPECIFY;
	}
	public void setR34_OTHER_SPECIFY(BigDecimal r34_OTHER_SPECIFY) {
		R34_OTHER_SPECIFY = r34_OTHER_SPECIFY;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_EQUITY() {
		return R35_EQUITY;
	}
	public void setR35_EQUITY(BigDecimal r35_EQUITY) {
		R35_EQUITY = r35_EQUITY;
	}
	public BigDecimal getR35_BONDS() {
		return R35_BONDS;
	}
	public void setR35_BONDS(BigDecimal r35_BONDS) {
		R35_BONDS = r35_BONDS;
	}
	public BigDecimal getR35_BOBCS() {
		return R35_BOBCS;
	}
	public void setR35_BOBCS(BigDecimal r35_BOBCS) {
		R35_BOBCS = r35_BOBCS;
	}
	public BigDecimal getR35_TREASURY_BILLS() {
		return R35_TREASURY_BILLS;
	}
	public void setR35_TREASURY_BILLS(BigDecimal r35_TREASURY_BILLS) {
		R35_TREASURY_BILLS = r35_TREASURY_BILLS;
	}
	public BigDecimal getR35_REPURCHASE_AGREEMENTS() {
		return R35_REPURCHASE_AGREEMENTS;
	}
	public void setR35_REPURCHASE_AGREEMENTS(BigDecimal r35_REPURCHASE_AGREEMENTS) {
		R35_REPURCHASE_AGREEMENTS = r35_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR35_COMMERCIAL_PAPER() {
		return R35_COMMERCIAL_PAPER;
	}
	public void setR35_COMMERCIAL_PAPER(BigDecimal r35_COMMERCIAL_PAPER) {
		R35_COMMERCIAL_PAPER = r35_COMMERCIAL_PAPER;
	}
	public BigDecimal getR35_CERTIFICATES_OF_DEPOSITS() {
		return R35_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR35_CERTIFICATES_OF_DEPOSITS(BigDecimal r35_CERTIFICATES_OF_DEPOSITS) {
		R35_CERTIFICATES_OF_DEPOSITS = r35_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR35_PLEDGED_ASSETS() {
		return R35_PLEDGED_ASSETS;
	}
	public void setR35_PLEDGED_ASSETS(BigDecimal r35_PLEDGED_ASSETS) {
		R35_PLEDGED_ASSETS = r35_PLEDGED_ASSETS;
	}
	public BigDecimal getR35_OTHER_SPECIFY() {
		return R35_OTHER_SPECIFY;
	}
	public void setR35_OTHER_SPECIFY(BigDecimal r35_OTHER_SPECIFY) {
		R35_OTHER_SPECIFY = r35_OTHER_SPECIFY;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_EQUITY() {
		return R36_EQUITY;
	}
	public void setR36_EQUITY(BigDecimal r36_EQUITY) {
		R36_EQUITY = r36_EQUITY;
	}
	public BigDecimal getR36_BONDS() {
		return R36_BONDS;
	}
	public void setR36_BONDS(BigDecimal r36_BONDS) {
		R36_BONDS = r36_BONDS;
	}
	public BigDecimal getR36_BOBCS() {
		return R36_BOBCS;
	}
	public void setR36_BOBCS(BigDecimal r36_BOBCS) {
		R36_BOBCS = r36_BOBCS;
	}
	public BigDecimal getR36_TREASURY_BILLS() {
		return R36_TREASURY_BILLS;
	}
	public void setR36_TREASURY_BILLS(BigDecimal r36_TREASURY_BILLS) {
		R36_TREASURY_BILLS = r36_TREASURY_BILLS;
	}
	public BigDecimal getR36_REPURCHASE_AGREEMENTS() {
		return R36_REPURCHASE_AGREEMENTS;
	}
	public void setR36_REPURCHASE_AGREEMENTS(BigDecimal r36_REPURCHASE_AGREEMENTS) {
		R36_REPURCHASE_AGREEMENTS = r36_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR36_COMMERCIAL_PAPER() {
		return R36_COMMERCIAL_PAPER;
	}
	public void setR36_COMMERCIAL_PAPER(BigDecimal r36_COMMERCIAL_PAPER) {
		R36_COMMERCIAL_PAPER = r36_COMMERCIAL_PAPER;
	}
	public BigDecimal getR36_CERTIFICATES_OF_DEPOSITS() {
		return R36_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR36_CERTIFICATES_OF_DEPOSITS(BigDecimal r36_CERTIFICATES_OF_DEPOSITS) {
		R36_CERTIFICATES_OF_DEPOSITS = r36_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR36_PLEDGED_ASSETS() {
		return R36_PLEDGED_ASSETS;
	}
	public void setR36_PLEDGED_ASSETS(BigDecimal r36_PLEDGED_ASSETS) {
		R36_PLEDGED_ASSETS = r36_PLEDGED_ASSETS;
	}
	public BigDecimal getR36_OTHER_SPECIFY() {
		return R36_OTHER_SPECIFY;
	}
	public void setR36_OTHER_SPECIFY(BigDecimal r36_OTHER_SPECIFY) {
		R36_OTHER_SPECIFY = r36_OTHER_SPECIFY;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_EQUITY() {
		return R37_EQUITY;
	}
	public void setR37_EQUITY(BigDecimal r37_EQUITY) {
		R37_EQUITY = r37_EQUITY;
	}
	public BigDecimal getR37_BONDS() {
		return R37_BONDS;
	}
	public void setR37_BONDS(BigDecimal r37_BONDS) {
		R37_BONDS = r37_BONDS;
	}
	public BigDecimal getR37_BOBCS() {
		return R37_BOBCS;
	}
	public void setR37_BOBCS(BigDecimal r37_BOBCS) {
		R37_BOBCS = r37_BOBCS;
	}
	public BigDecimal getR37_TREASURY_BILLS() {
		return R37_TREASURY_BILLS;
	}
	public void setR37_TREASURY_BILLS(BigDecimal r37_TREASURY_BILLS) {
		R37_TREASURY_BILLS = r37_TREASURY_BILLS;
	}
	public BigDecimal getR37_REPURCHASE_AGREEMENTS() {
		return R37_REPURCHASE_AGREEMENTS;
	}
	public void setR37_REPURCHASE_AGREEMENTS(BigDecimal r37_REPURCHASE_AGREEMENTS) {
		R37_REPURCHASE_AGREEMENTS = r37_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR37_COMMERCIAL_PAPER() {
		return R37_COMMERCIAL_PAPER;
	}
	public void setR37_COMMERCIAL_PAPER(BigDecimal r37_COMMERCIAL_PAPER) {
		R37_COMMERCIAL_PAPER = r37_COMMERCIAL_PAPER;
	}
	public BigDecimal getR37_CERTIFICATES_OF_DEPOSITS() {
		return R37_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR37_CERTIFICATES_OF_DEPOSITS(BigDecimal r37_CERTIFICATES_OF_DEPOSITS) {
		R37_CERTIFICATES_OF_DEPOSITS = r37_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR37_PLEDGED_ASSETS() {
		return R37_PLEDGED_ASSETS;
	}
	public void setR37_PLEDGED_ASSETS(BigDecimal r37_PLEDGED_ASSETS) {
		R37_PLEDGED_ASSETS = r37_PLEDGED_ASSETS;
	}
	public BigDecimal getR37_OTHER_SPECIFY() {
		return R37_OTHER_SPECIFY;
	}
	public void setR37_OTHER_SPECIFY(BigDecimal r37_OTHER_SPECIFY) {
		R37_OTHER_SPECIFY = r37_OTHER_SPECIFY;
	}
	public BigDecimal getR37_TOTAL() {
		return R37_TOTAL;
	}
	public void setR37_TOTAL(BigDecimal r37_TOTAL) {
		R37_TOTAL = r37_TOTAL;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_EQUITY() {
		return R38_EQUITY;
	}
	public void setR38_EQUITY(BigDecimal r38_EQUITY) {
		R38_EQUITY = r38_EQUITY;
	}
	public BigDecimal getR38_BONDS() {
		return R38_BONDS;
	}
	public void setR38_BONDS(BigDecimal r38_BONDS) {
		R38_BONDS = r38_BONDS;
	}
	public BigDecimal getR38_BOBCS() {
		return R38_BOBCS;
	}
	public void setR38_BOBCS(BigDecimal r38_BOBCS) {
		R38_BOBCS = r38_BOBCS;
	}
	public BigDecimal getR38_TREASURY_BILLS() {
		return R38_TREASURY_BILLS;
	}
	public void setR38_TREASURY_BILLS(BigDecimal r38_TREASURY_BILLS) {
		R38_TREASURY_BILLS = r38_TREASURY_BILLS;
	}
	public BigDecimal getR38_REPURCHASE_AGREEMENTS() {
		return R38_REPURCHASE_AGREEMENTS;
	}
	public void setR38_REPURCHASE_AGREEMENTS(BigDecimal r38_REPURCHASE_AGREEMENTS) {
		R38_REPURCHASE_AGREEMENTS = r38_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR38_COMMERCIAL_PAPER() {
		return R38_COMMERCIAL_PAPER;
	}
	public void setR38_COMMERCIAL_PAPER(BigDecimal r38_COMMERCIAL_PAPER) {
		R38_COMMERCIAL_PAPER = r38_COMMERCIAL_PAPER;
	}
	public BigDecimal getR38_CERTIFICATES_OF_DEPOSITS() {
		return R38_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR38_CERTIFICATES_OF_DEPOSITS(BigDecimal r38_CERTIFICATES_OF_DEPOSITS) {
		R38_CERTIFICATES_OF_DEPOSITS = r38_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR38_PLEDGED_ASSETS() {
		return R38_PLEDGED_ASSETS;
	}
	public void setR38_PLEDGED_ASSETS(BigDecimal r38_PLEDGED_ASSETS) {
		R38_PLEDGED_ASSETS = r38_PLEDGED_ASSETS;
	}
	public BigDecimal getR38_OTHER_SPECIFY() {
		return R38_OTHER_SPECIFY;
	}
	public void setR38_OTHER_SPECIFY(BigDecimal r38_OTHER_SPECIFY) {
		R38_OTHER_SPECIFY = r38_OTHER_SPECIFY;
	}
	public BigDecimal getR38_TOTAL() {
		return R38_TOTAL;
	}
	public void setR38_TOTAL(BigDecimal r38_TOTAL) {
		R38_TOTAL = r38_TOTAL;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_EQUITY() {
		return R39_EQUITY;
	}
	public void setR39_EQUITY(BigDecimal r39_EQUITY) {
		R39_EQUITY = r39_EQUITY;
	}
	public BigDecimal getR39_BONDS() {
		return R39_BONDS;
	}
	public void setR39_BONDS(BigDecimal r39_BONDS) {
		R39_BONDS = r39_BONDS;
	}
	public BigDecimal getR39_BOBCS() {
		return R39_BOBCS;
	}
	public void setR39_BOBCS(BigDecimal r39_BOBCS) {
		R39_BOBCS = r39_BOBCS;
	}
	public BigDecimal getR39_TREASURY_BILLS() {
		return R39_TREASURY_BILLS;
	}
	public void setR39_TREASURY_BILLS(BigDecimal r39_TREASURY_BILLS) {
		R39_TREASURY_BILLS = r39_TREASURY_BILLS;
	}
	public BigDecimal getR39_REPURCHASE_AGREEMENTS() {
		return R39_REPURCHASE_AGREEMENTS;
	}
	public void setR39_REPURCHASE_AGREEMENTS(BigDecimal r39_REPURCHASE_AGREEMENTS) {
		R39_REPURCHASE_AGREEMENTS = r39_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR39_COMMERCIAL_PAPER() {
		return R39_COMMERCIAL_PAPER;
	}
	public void setR39_COMMERCIAL_PAPER(BigDecimal r39_COMMERCIAL_PAPER) {
		R39_COMMERCIAL_PAPER = r39_COMMERCIAL_PAPER;
	}
	public BigDecimal getR39_CERTIFICATES_OF_DEPOSITS() {
		return R39_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR39_CERTIFICATES_OF_DEPOSITS(BigDecimal r39_CERTIFICATES_OF_DEPOSITS) {
		R39_CERTIFICATES_OF_DEPOSITS = r39_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR39_PLEDGED_ASSETS() {
		return R39_PLEDGED_ASSETS;
	}
	public void setR39_PLEDGED_ASSETS(BigDecimal r39_PLEDGED_ASSETS) {
		R39_PLEDGED_ASSETS = r39_PLEDGED_ASSETS;
	}
	public BigDecimal getR39_OTHER_SPECIFY() {
		return R39_OTHER_SPECIFY;
	}
	public void setR39_OTHER_SPECIFY(BigDecimal r39_OTHER_SPECIFY) {
		R39_OTHER_SPECIFY = r39_OTHER_SPECIFY;
	}
	public BigDecimal getR39_TOTAL() {
		return R39_TOTAL;
	}
	public void setR39_TOTAL(BigDecimal r39_TOTAL) {
		R39_TOTAL = r39_TOTAL;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_EQUITY() {
		return R40_EQUITY;
	}
	public void setR40_EQUITY(BigDecimal r40_EQUITY) {
		R40_EQUITY = r40_EQUITY;
	}
	public BigDecimal getR40_BONDS() {
		return R40_BONDS;
	}
	public void setR40_BONDS(BigDecimal r40_BONDS) {
		R40_BONDS = r40_BONDS;
	}
	public BigDecimal getR40_BOBCS() {
		return R40_BOBCS;
	}
	public void setR40_BOBCS(BigDecimal r40_BOBCS) {
		R40_BOBCS = r40_BOBCS;
	}
	public BigDecimal getR40_TREASURY_BILLS() {
		return R40_TREASURY_BILLS;
	}
	public void setR40_TREASURY_BILLS(BigDecimal r40_TREASURY_BILLS) {
		R40_TREASURY_BILLS = r40_TREASURY_BILLS;
	}
	public BigDecimal getR40_REPURCHASE_AGREEMENTS() {
		return R40_REPURCHASE_AGREEMENTS;
	}
	public void setR40_REPURCHASE_AGREEMENTS(BigDecimal r40_REPURCHASE_AGREEMENTS) {
		R40_REPURCHASE_AGREEMENTS = r40_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR40_COMMERCIAL_PAPER() {
		return R40_COMMERCIAL_PAPER;
	}
	public void setR40_COMMERCIAL_PAPER(BigDecimal r40_COMMERCIAL_PAPER) {
		R40_COMMERCIAL_PAPER = r40_COMMERCIAL_PAPER;
	}
	public BigDecimal getR40_CERTIFICATES_OF_DEPOSITS() {
		return R40_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR40_CERTIFICATES_OF_DEPOSITS(BigDecimal r40_CERTIFICATES_OF_DEPOSITS) {
		R40_CERTIFICATES_OF_DEPOSITS = r40_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR40_PLEDGED_ASSETS() {
		return R40_PLEDGED_ASSETS;
	}
	public void setR40_PLEDGED_ASSETS(BigDecimal r40_PLEDGED_ASSETS) {
		R40_PLEDGED_ASSETS = r40_PLEDGED_ASSETS;
	}
	public BigDecimal getR40_OTHER_SPECIFY() {
		return R40_OTHER_SPECIFY;
	}
	public void setR40_OTHER_SPECIFY(BigDecimal r40_OTHER_SPECIFY) {
		R40_OTHER_SPECIFY = r40_OTHER_SPECIFY;
	}
	public BigDecimal getR40_TOTAL() {
		return R40_TOTAL;
	}
	public void setR40_TOTAL(BigDecimal r40_TOTAL) {
		R40_TOTAL = r40_TOTAL;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_EQUITY() {
		return R41_EQUITY;
	}
	public void setR41_EQUITY(BigDecimal r41_EQUITY) {
		R41_EQUITY = r41_EQUITY;
	}
	public BigDecimal getR41_BONDS() {
		return R41_BONDS;
	}
	public void setR41_BONDS(BigDecimal r41_BONDS) {
		R41_BONDS = r41_BONDS;
	}
	public BigDecimal getR41_BOBCS() {
		return R41_BOBCS;
	}
	public void setR41_BOBCS(BigDecimal r41_BOBCS) {
		R41_BOBCS = r41_BOBCS;
	}
	public BigDecimal getR41_TREASURY_BILLS() {
		return R41_TREASURY_BILLS;
	}
	public void setR41_TREASURY_BILLS(BigDecimal r41_TREASURY_BILLS) {
		R41_TREASURY_BILLS = r41_TREASURY_BILLS;
	}
	public BigDecimal getR41_REPURCHASE_AGREEMENTS() {
		return R41_REPURCHASE_AGREEMENTS;
	}
	public void setR41_REPURCHASE_AGREEMENTS(BigDecimal r41_REPURCHASE_AGREEMENTS) {
		R41_REPURCHASE_AGREEMENTS = r41_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR41_COMMERCIAL_PAPER() {
		return R41_COMMERCIAL_PAPER;
	}
	public void setR41_COMMERCIAL_PAPER(BigDecimal r41_COMMERCIAL_PAPER) {
		R41_COMMERCIAL_PAPER = r41_COMMERCIAL_PAPER;
	}
	public BigDecimal getR41_CERTIFICATES_OF_DEPOSITS() {
		return R41_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR41_CERTIFICATES_OF_DEPOSITS(BigDecimal r41_CERTIFICATES_OF_DEPOSITS) {
		R41_CERTIFICATES_OF_DEPOSITS = r41_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR41_PLEDGED_ASSETS() {
		return R41_PLEDGED_ASSETS;
	}
	public void setR41_PLEDGED_ASSETS(BigDecimal r41_PLEDGED_ASSETS) {
		R41_PLEDGED_ASSETS = r41_PLEDGED_ASSETS;
	}
	public BigDecimal getR41_OTHER_SPECIFY() {
		return R41_OTHER_SPECIFY;
	}
	public void setR41_OTHER_SPECIFY(BigDecimal r41_OTHER_SPECIFY) {
		R41_OTHER_SPECIFY = r41_OTHER_SPECIFY;
	}
	public BigDecimal getR41_TOTAL() {
		return R41_TOTAL;
	}
	public void setR41_TOTAL(BigDecimal r41_TOTAL) {
		R41_TOTAL = r41_TOTAL;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_EQUITY() {
		return R42_EQUITY;
	}
	public void setR42_EQUITY(BigDecimal r42_EQUITY) {
		R42_EQUITY = r42_EQUITY;
	}
	public BigDecimal getR42_BONDS() {
		return R42_BONDS;
	}
	public void setR42_BONDS(BigDecimal r42_BONDS) {
		R42_BONDS = r42_BONDS;
	}
	public BigDecimal getR42_BOBCS() {
		return R42_BOBCS;
	}
	public void setR42_BOBCS(BigDecimal r42_BOBCS) {
		R42_BOBCS = r42_BOBCS;
	}
	public BigDecimal getR42_TREASURY_BILLS() {
		return R42_TREASURY_BILLS;
	}
	public void setR42_TREASURY_BILLS(BigDecimal r42_TREASURY_BILLS) {
		R42_TREASURY_BILLS = r42_TREASURY_BILLS;
	}
	public BigDecimal getR42_REPURCHASE_AGREEMENTS() {
		return R42_REPURCHASE_AGREEMENTS;
	}
	public void setR42_REPURCHASE_AGREEMENTS(BigDecimal r42_REPURCHASE_AGREEMENTS) {
		R42_REPURCHASE_AGREEMENTS = r42_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR42_COMMERCIAL_PAPER() {
		return R42_COMMERCIAL_PAPER;
	}
	public void setR42_COMMERCIAL_PAPER(BigDecimal r42_COMMERCIAL_PAPER) {
		R42_COMMERCIAL_PAPER = r42_COMMERCIAL_PAPER;
	}
	public BigDecimal getR42_CERTIFICATES_OF_DEPOSITS() {
		return R42_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR42_CERTIFICATES_OF_DEPOSITS(BigDecimal r42_CERTIFICATES_OF_DEPOSITS) {
		R42_CERTIFICATES_OF_DEPOSITS = r42_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR42_PLEDGED_ASSETS() {
		return R42_PLEDGED_ASSETS;
	}
	public void setR42_PLEDGED_ASSETS(BigDecimal r42_PLEDGED_ASSETS) {
		R42_PLEDGED_ASSETS = r42_PLEDGED_ASSETS;
	}
	public BigDecimal getR42_OTHER_SPECIFY() {
		return R42_OTHER_SPECIFY;
	}
	public void setR42_OTHER_SPECIFY(BigDecimal r42_OTHER_SPECIFY) {
		R42_OTHER_SPECIFY = r42_OTHER_SPECIFY;
	}
	public BigDecimal getR42_TOTAL() {
		return R42_TOTAL;
	}
	public void setR42_TOTAL(BigDecimal r42_TOTAL) {
		R42_TOTAL = r42_TOTAL;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_EQUITY() {
		return R43_EQUITY;
	}
	public void setR43_EQUITY(BigDecimal r43_EQUITY) {
		R43_EQUITY = r43_EQUITY;
	}
	public BigDecimal getR43_BONDS() {
		return R43_BONDS;
	}
	public void setR43_BONDS(BigDecimal r43_BONDS) {
		R43_BONDS = r43_BONDS;
	}
	public BigDecimal getR43_BOBCS() {
		return R43_BOBCS;
	}
	public void setR43_BOBCS(BigDecimal r43_BOBCS) {
		R43_BOBCS = r43_BOBCS;
	}
	public BigDecimal getR43_TREASURY_BILLS() {
		return R43_TREASURY_BILLS;
	}
	public void setR43_TREASURY_BILLS(BigDecimal r43_TREASURY_BILLS) {
		R43_TREASURY_BILLS = r43_TREASURY_BILLS;
	}
	public BigDecimal getR43_REPURCHASE_AGREEMENTS() {
		return R43_REPURCHASE_AGREEMENTS;
	}
	public void setR43_REPURCHASE_AGREEMENTS(BigDecimal r43_REPURCHASE_AGREEMENTS) {
		R43_REPURCHASE_AGREEMENTS = r43_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR43_COMMERCIAL_PAPER() {
		return R43_COMMERCIAL_PAPER;
	}
	public void setR43_COMMERCIAL_PAPER(BigDecimal r43_COMMERCIAL_PAPER) {
		R43_COMMERCIAL_PAPER = r43_COMMERCIAL_PAPER;
	}
	public BigDecimal getR43_CERTIFICATES_OF_DEPOSITS() {
		return R43_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR43_CERTIFICATES_OF_DEPOSITS(BigDecimal r43_CERTIFICATES_OF_DEPOSITS) {
		R43_CERTIFICATES_OF_DEPOSITS = r43_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR43_PLEDGED_ASSETS() {
		return R43_PLEDGED_ASSETS;
	}
	public void setR43_PLEDGED_ASSETS(BigDecimal r43_PLEDGED_ASSETS) {
		R43_PLEDGED_ASSETS = r43_PLEDGED_ASSETS;
	}
	public BigDecimal getR43_OTHER_SPECIFY() {
		return R43_OTHER_SPECIFY;
	}
	public void setR43_OTHER_SPECIFY(BigDecimal r43_OTHER_SPECIFY) {
		R43_OTHER_SPECIFY = r43_OTHER_SPECIFY;
	}
	public BigDecimal getR43_TOTAL() {
		return R43_TOTAL;
	}
	public void setR43_TOTAL(BigDecimal r43_TOTAL) {
		R43_TOTAL = r43_TOTAL;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_EQUITY() {
		return R44_EQUITY;
	}
	public void setR44_EQUITY(BigDecimal r44_EQUITY) {
		R44_EQUITY = r44_EQUITY;
	}
	public BigDecimal getR44_BONDS() {
		return R44_BONDS;
	}
	public void setR44_BONDS(BigDecimal r44_BONDS) {
		R44_BONDS = r44_BONDS;
	}
	public BigDecimal getR44_BOBCS() {
		return R44_BOBCS;
	}
	public void setR44_BOBCS(BigDecimal r44_BOBCS) {
		R44_BOBCS = r44_BOBCS;
	}
	public BigDecimal getR44_TREASURY_BILLS() {
		return R44_TREASURY_BILLS;
	}
	public void setR44_TREASURY_BILLS(BigDecimal r44_TREASURY_BILLS) {
		R44_TREASURY_BILLS = r44_TREASURY_BILLS;
	}
	public BigDecimal getR44_REPURCHASE_AGREEMENTS() {
		return R44_REPURCHASE_AGREEMENTS;
	}
	public void setR44_REPURCHASE_AGREEMENTS(BigDecimal r44_REPURCHASE_AGREEMENTS) {
		R44_REPURCHASE_AGREEMENTS = r44_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR44_COMMERCIAL_PAPER() {
		return R44_COMMERCIAL_PAPER;
	}
	public void setR44_COMMERCIAL_PAPER(BigDecimal r44_COMMERCIAL_PAPER) {
		R44_COMMERCIAL_PAPER = r44_COMMERCIAL_PAPER;
	}
	public BigDecimal getR44_CERTIFICATES_OF_DEPOSITS() {
		return R44_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR44_CERTIFICATES_OF_DEPOSITS(BigDecimal r44_CERTIFICATES_OF_DEPOSITS) {
		R44_CERTIFICATES_OF_DEPOSITS = r44_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR44_PLEDGED_ASSETS() {
		return R44_PLEDGED_ASSETS;
	}
	public void setR44_PLEDGED_ASSETS(BigDecimal r44_PLEDGED_ASSETS) {
		R44_PLEDGED_ASSETS = r44_PLEDGED_ASSETS;
	}
	public BigDecimal getR44_OTHER_SPECIFY() {
		return R44_OTHER_SPECIFY;
	}
	public void setR44_OTHER_SPECIFY(BigDecimal r44_OTHER_SPECIFY) {
		R44_OTHER_SPECIFY = r44_OTHER_SPECIFY;
	}
	public BigDecimal getR44_TOTAL() {
		return R44_TOTAL;
	}
	public void setR44_TOTAL(BigDecimal r44_TOTAL) {
		R44_TOTAL = r44_TOTAL;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_EQUITY() {
		return R45_EQUITY;
	}
	public void setR45_EQUITY(BigDecimal r45_EQUITY) {
		R45_EQUITY = r45_EQUITY;
	}
	public BigDecimal getR45_BONDS() {
		return R45_BONDS;
	}
	public void setR45_BONDS(BigDecimal r45_BONDS) {
		R45_BONDS = r45_BONDS;
	}
	public BigDecimal getR45_BOBCS() {
		return R45_BOBCS;
	}
	public void setR45_BOBCS(BigDecimal r45_BOBCS) {
		R45_BOBCS = r45_BOBCS;
	}
	public BigDecimal getR45_TREASURY_BILLS() {
		return R45_TREASURY_BILLS;
	}
	public void setR45_TREASURY_BILLS(BigDecimal r45_TREASURY_BILLS) {
		R45_TREASURY_BILLS = r45_TREASURY_BILLS;
	}
	public BigDecimal getR45_REPURCHASE_AGREEMENTS() {
		return R45_REPURCHASE_AGREEMENTS;
	}
	public void setR45_REPURCHASE_AGREEMENTS(BigDecimal r45_REPURCHASE_AGREEMENTS) {
		R45_REPURCHASE_AGREEMENTS = r45_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR45_COMMERCIAL_PAPER() {
		return R45_COMMERCIAL_PAPER;
	}
	public void setR45_COMMERCIAL_PAPER(BigDecimal r45_COMMERCIAL_PAPER) {
		R45_COMMERCIAL_PAPER = r45_COMMERCIAL_PAPER;
	}
	public BigDecimal getR45_CERTIFICATES_OF_DEPOSITS() {
		return R45_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR45_CERTIFICATES_OF_DEPOSITS(BigDecimal r45_CERTIFICATES_OF_DEPOSITS) {
		R45_CERTIFICATES_OF_DEPOSITS = r45_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR45_PLEDGED_ASSETS() {
		return R45_PLEDGED_ASSETS;
	}
	public void setR45_PLEDGED_ASSETS(BigDecimal r45_PLEDGED_ASSETS) {
		R45_PLEDGED_ASSETS = r45_PLEDGED_ASSETS;
	}
	public BigDecimal getR45_OTHER_SPECIFY() {
		return R45_OTHER_SPECIFY;
	}
	public void setR45_OTHER_SPECIFY(BigDecimal r45_OTHER_SPECIFY) {
		R45_OTHER_SPECIFY = r45_OTHER_SPECIFY;
	}
	public BigDecimal getR45_TOTAL() {
		return R45_TOTAL;
	}
	public void setR45_TOTAL(BigDecimal r45_TOTAL) {
		R45_TOTAL = r45_TOTAL;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_EQUITY() {
		return R46_EQUITY;
	}
	public void setR46_EQUITY(BigDecimal r46_EQUITY) {
		R46_EQUITY = r46_EQUITY;
	}
	public BigDecimal getR46_BONDS() {
		return R46_BONDS;
	}
	public void setR46_BONDS(BigDecimal r46_BONDS) {
		R46_BONDS = r46_BONDS;
	}
	public BigDecimal getR46_BOBCS() {
		return R46_BOBCS;
	}
	public void setR46_BOBCS(BigDecimal r46_BOBCS) {
		R46_BOBCS = r46_BOBCS;
	}
	public BigDecimal getR46_TREASURY_BILLS() {
		return R46_TREASURY_BILLS;
	}
	public void setR46_TREASURY_BILLS(BigDecimal r46_TREASURY_BILLS) {
		R46_TREASURY_BILLS = r46_TREASURY_BILLS;
	}
	public BigDecimal getR46_REPURCHASE_AGREEMENTS() {
		return R46_REPURCHASE_AGREEMENTS;
	}
	public void setR46_REPURCHASE_AGREEMENTS(BigDecimal r46_REPURCHASE_AGREEMENTS) {
		R46_REPURCHASE_AGREEMENTS = r46_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR46_COMMERCIAL_PAPER() {
		return R46_COMMERCIAL_PAPER;
	}
	public void setR46_COMMERCIAL_PAPER(BigDecimal r46_COMMERCIAL_PAPER) {
		R46_COMMERCIAL_PAPER = r46_COMMERCIAL_PAPER;
	}
	public BigDecimal getR46_CERTIFICATES_OF_DEPOSITS() {
		return R46_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR46_CERTIFICATES_OF_DEPOSITS(BigDecimal r46_CERTIFICATES_OF_DEPOSITS) {
		R46_CERTIFICATES_OF_DEPOSITS = r46_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR46_PLEDGED_ASSETS() {
		return R46_PLEDGED_ASSETS;
	}
	public void setR46_PLEDGED_ASSETS(BigDecimal r46_PLEDGED_ASSETS) {
		R46_PLEDGED_ASSETS = r46_PLEDGED_ASSETS;
	}
	public BigDecimal getR46_OTHER_SPECIFY() {
		return R46_OTHER_SPECIFY;
	}
	public void setR46_OTHER_SPECIFY(BigDecimal r46_OTHER_SPECIFY) {
		R46_OTHER_SPECIFY = r46_OTHER_SPECIFY;
	}
	public BigDecimal getR46_TOTAL() {
		return R46_TOTAL;
	}
	public void setR46_TOTAL(BigDecimal r46_TOTAL) {
		R46_TOTAL = r46_TOTAL;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_EQUITY() {
		return R47_EQUITY;
	}
	public void setR47_EQUITY(BigDecimal r47_EQUITY) {
		R47_EQUITY = r47_EQUITY;
	}
	public BigDecimal getR47_BONDS() {
		return R47_BONDS;
	}
	public void setR47_BONDS(BigDecimal r47_BONDS) {
		R47_BONDS = r47_BONDS;
	}
	public BigDecimal getR47_BOBCS() {
		return R47_BOBCS;
	}
	public void setR47_BOBCS(BigDecimal r47_BOBCS) {
		R47_BOBCS = r47_BOBCS;
	}
	public BigDecimal getR47_TREASURY_BILLS() {
		return R47_TREASURY_BILLS;
	}
	public void setR47_TREASURY_BILLS(BigDecimal r47_TREASURY_BILLS) {
		R47_TREASURY_BILLS = r47_TREASURY_BILLS;
	}
	public BigDecimal getR47_REPURCHASE_AGREEMENTS() {
		return R47_REPURCHASE_AGREEMENTS;
	}
	public void setR47_REPURCHASE_AGREEMENTS(BigDecimal r47_REPURCHASE_AGREEMENTS) {
		R47_REPURCHASE_AGREEMENTS = r47_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR47_COMMERCIAL_PAPER() {
		return R47_COMMERCIAL_PAPER;
	}
	public void setR47_COMMERCIAL_PAPER(BigDecimal r47_COMMERCIAL_PAPER) {
		R47_COMMERCIAL_PAPER = r47_COMMERCIAL_PAPER;
	}
	public BigDecimal getR47_CERTIFICATES_OF_DEPOSITS() {
		return R47_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR47_CERTIFICATES_OF_DEPOSITS(BigDecimal r47_CERTIFICATES_OF_DEPOSITS) {
		R47_CERTIFICATES_OF_DEPOSITS = r47_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR47_PLEDGED_ASSETS() {
		return R47_PLEDGED_ASSETS;
	}
	public void setR47_PLEDGED_ASSETS(BigDecimal r47_PLEDGED_ASSETS) {
		R47_PLEDGED_ASSETS = r47_PLEDGED_ASSETS;
	}
	public BigDecimal getR47_OTHER_SPECIFY() {
		return R47_OTHER_SPECIFY;
	}
	public void setR47_OTHER_SPECIFY(BigDecimal r47_OTHER_SPECIFY) {
		R47_OTHER_SPECIFY = r47_OTHER_SPECIFY;
	}
	public BigDecimal getR47_TOTAL() {
		return R47_TOTAL;
	}
	public void setR47_TOTAL(BigDecimal r47_TOTAL) {
		R47_TOTAL = r47_TOTAL;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_EQUITY() {
		return R48_EQUITY;
	}
	public void setR48_EQUITY(BigDecimal r48_EQUITY) {
		R48_EQUITY = r48_EQUITY;
	}
	public BigDecimal getR48_BONDS() {
		return R48_BONDS;
	}
	public void setR48_BONDS(BigDecimal r48_BONDS) {
		R48_BONDS = r48_BONDS;
	}
	public BigDecimal getR48_BOBCS() {
		return R48_BOBCS;
	}
	public void setR48_BOBCS(BigDecimal r48_BOBCS) {
		R48_BOBCS = r48_BOBCS;
	}
	public BigDecimal getR48_TREASURY_BILLS() {
		return R48_TREASURY_BILLS;
	}
	public void setR48_TREASURY_BILLS(BigDecimal r48_TREASURY_BILLS) {
		R48_TREASURY_BILLS = r48_TREASURY_BILLS;
	}
	public BigDecimal getR48_REPURCHASE_AGREEMENTS() {
		return R48_REPURCHASE_AGREEMENTS;
	}
	public void setR48_REPURCHASE_AGREEMENTS(BigDecimal r48_REPURCHASE_AGREEMENTS) {
		R48_REPURCHASE_AGREEMENTS = r48_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR48_COMMERCIAL_PAPER() {
		return R48_COMMERCIAL_PAPER;
	}
	public void setR48_COMMERCIAL_PAPER(BigDecimal r48_COMMERCIAL_PAPER) {
		R48_COMMERCIAL_PAPER = r48_COMMERCIAL_PAPER;
	}
	public BigDecimal getR48_CERTIFICATES_OF_DEPOSITS() {
		return R48_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR48_CERTIFICATES_OF_DEPOSITS(BigDecimal r48_CERTIFICATES_OF_DEPOSITS) {
		R48_CERTIFICATES_OF_DEPOSITS = r48_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR48_PLEDGED_ASSETS() {
		return R48_PLEDGED_ASSETS;
	}
	public void setR48_PLEDGED_ASSETS(BigDecimal r48_PLEDGED_ASSETS) {
		R48_PLEDGED_ASSETS = r48_PLEDGED_ASSETS;
	}
	public BigDecimal getR48_OTHER_SPECIFY() {
		return R48_OTHER_SPECIFY;
	}
	public void setR48_OTHER_SPECIFY(BigDecimal r48_OTHER_SPECIFY) {
		R48_OTHER_SPECIFY = r48_OTHER_SPECIFY;
	}
	public BigDecimal getR48_TOTAL() {
		return R48_TOTAL;
	}
	public void setR48_TOTAL(BigDecimal r48_TOTAL) {
		R48_TOTAL = r48_TOTAL;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_EQUITY() {
		return R49_EQUITY;
	}
	public void setR49_EQUITY(BigDecimal r49_EQUITY) {
		R49_EQUITY = r49_EQUITY;
	}
	public BigDecimal getR49_BONDS() {
		return R49_BONDS;
	}
	public void setR49_BONDS(BigDecimal r49_BONDS) {
		R49_BONDS = r49_BONDS;
	}
	public BigDecimal getR49_BOBCS() {
		return R49_BOBCS;
	}
	public void setR49_BOBCS(BigDecimal r49_BOBCS) {
		R49_BOBCS = r49_BOBCS;
	}
	public BigDecimal getR49_TREASURY_BILLS() {
		return R49_TREASURY_BILLS;
	}
	public void setR49_TREASURY_BILLS(BigDecimal r49_TREASURY_BILLS) {
		R49_TREASURY_BILLS = r49_TREASURY_BILLS;
	}
	public BigDecimal getR49_REPURCHASE_AGREEMENTS() {
		return R49_REPURCHASE_AGREEMENTS;
	}
	public void setR49_REPURCHASE_AGREEMENTS(BigDecimal r49_REPURCHASE_AGREEMENTS) {
		R49_REPURCHASE_AGREEMENTS = r49_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR49_COMMERCIAL_PAPER() {
		return R49_COMMERCIAL_PAPER;
	}
	public void setR49_COMMERCIAL_PAPER(BigDecimal r49_COMMERCIAL_PAPER) {
		R49_COMMERCIAL_PAPER = r49_COMMERCIAL_PAPER;
	}
	public BigDecimal getR49_CERTIFICATES_OF_DEPOSITS() {
		return R49_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR49_CERTIFICATES_OF_DEPOSITS(BigDecimal r49_CERTIFICATES_OF_DEPOSITS) {
		R49_CERTIFICATES_OF_DEPOSITS = r49_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR49_PLEDGED_ASSETS() {
		return R49_PLEDGED_ASSETS;
	}
	public void setR49_PLEDGED_ASSETS(BigDecimal r49_PLEDGED_ASSETS) {
		R49_PLEDGED_ASSETS = r49_PLEDGED_ASSETS;
	}
	public BigDecimal getR49_OTHER_SPECIFY() {
		return R49_OTHER_SPECIFY;
	}
	public void setR49_OTHER_SPECIFY(BigDecimal r49_OTHER_SPECIFY) {
		R49_OTHER_SPECIFY = r49_OTHER_SPECIFY;
	}
	public BigDecimal getR49_TOTAL() {
		return R49_TOTAL;
	}
	public void setR49_TOTAL(BigDecimal r49_TOTAL) {
		R49_TOTAL = r49_TOTAL;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_EQUITY() {
		return R50_EQUITY;
	}
	public void setR50_EQUITY(BigDecimal r50_EQUITY) {
		R50_EQUITY = r50_EQUITY;
	}
	public BigDecimal getR50_BONDS() {
		return R50_BONDS;
	}
	public void setR50_BONDS(BigDecimal r50_BONDS) {
		R50_BONDS = r50_BONDS;
	}
	public BigDecimal getR50_BOBCS() {
		return R50_BOBCS;
	}
	public void setR50_BOBCS(BigDecimal r50_BOBCS) {
		R50_BOBCS = r50_BOBCS;
	}
	public BigDecimal getR50_TREASURY_BILLS() {
		return R50_TREASURY_BILLS;
	}
	public void setR50_TREASURY_BILLS(BigDecimal r50_TREASURY_BILLS) {
		R50_TREASURY_BILLS = r50_TREASURY_BILLS;
	}
	public BigDecimal getR50_REPURCHASE_AGREEMENTS() {
		return R50_REPURCHASE_AGREEMENTS;
	}
	public void setR50_REPURCHASE_AGREEMENTS(BigDecimal r50_REPURCHASE_AGREEMENTS) {
		R50_REPURCHASE_AGREEMENTS = r50_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR50_COMMERCIAL_PAPER() {
		return R50_COMMERCIAL_PAPER;
	}
	public void setR50_COMMERCIAL_PAPER(BigDecimal r50_COMMERCIAL_PAPER) {
		R50_COMMERCIAL_PAPER = r50_COMMERCIAL_PAPER;
	}
	public BigDecimal getR50_CERTIFICATES_OF_DEPOSITS() {
		return R50_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR50_CERTIFICATES_OF_DEPOSITS(BigDecimal r50_CERTIFICATES_OF_DEPOSITS) {
		R50_CERTIFICATES_OF_DEPOSITS = r50_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR50_PLEDGED_ASSETS() {
		return R50_PLEDGED_ASSETS;
	}
	public void setR50_PLEDGED_ASSETS(BigDecimal r50_PLEDGED_ASSETS) {
		R50_PLEDGED_ASSETS = r50_PLEDGED_ASSETS;
	}
	public BigDecimal getR50_OTHER_SPECIFY() {
		return R50_OTHER_SPECIFY;
	}
	public void setR50_OTHER_SPECIFY(BigDecimal r50_OTHER_SPECIFY) {
		R50_OTHER_SPECIFY = r50_OTHER_SPECIFY;
	}
	public BigDecimal getR50_TOTAL() {
		return R50_TOTAL;
	}
	public void setR50_TOTAL(BigDecimal r50_TOTAL) {
		R50_TOTAL = r50_TOTAL;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_EQUITY() {
		return R51_EQUITY;
	}
	public void setR51_EQUITY(BigDecimal r51_EQUITY) {
		R51_EQUITY = r51_EQUITY;
	}
	public BigDecimal getR51_BONDS() {
		return R51_BONDS;
	}
	public void setR51_BONDS(BigDecimal r51_BONDS) {
		R51_BONDS = r51_BONDS;
	}
	public BigDecimal getR51_BOBCS() {
		return R51_BOBCS;
	}
	public void setR51_BOBCS(BigDecimal r51_BOBCS) {
		R51_BOBCS = r51_BOBCS;
	}
	public BigDecimal getR51_TREASURY_BILLS() {
		return R51_TREASURY_BILLS;
	}
	public void setR51_TREASURY_BILLS(BigDecimal r51_TREASURY_BILLS) {
		R51_TREASURY_BILLS = r51_TREASURY_BILLS;
	}
	public BigDecimal getR51_REPURCHASE_AGREEMENTS() {
		return R51_REPURCHASE_AGREEMENTS;
	}
	public void setR51_REPURCHASE_AGREEMENTS(BigDecimal r51_REPURCHASE_AGREEMENTS) {
		R51_REPURCHASE_AGREEMENTS = r51_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR51_COMMERCIAL_PAPER() {
		return R51_COMMERCIAL_PAPER;
	}
	public void setR51_COMMERCIAL_PAPER(BigDecimal r51_COMMERCIAL_PAPER) {
		R51_COMMERCIAL_PAPER = r51_COMMERCIAL_PAPER;
	}
	public BigDecimal getR51_CERTIFICATES_OF_DEPOSITS() {
		return R51_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR51_CERTIFICATES_OF_DEPOSITS(BigDecimal r51_CERTIFICATES_OF_DEPOSITS) {
		R51_CERTIFICATES_OF_DEPOSITS = r51_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR51_PLEDGED_ASSETS() {
		return R51_PLEDGED_ASSETS;
	}
	public void setR51_PLEDGED_ASSETS(BigDecimal r51_PLEDGED_ASSETS) {
		R51_PLEDGED_ASSETS = r51_PLEDGED_ASSETS;
	}
	public BigDecimal getR51_OTHER_SPECIFY() {
		return R51_OTHER_SPECIFY;
	}
	public void setR51_OTHER_SPECIFY(BigDecimal r51_OTHER_SPECIFY) {
		R51_OTHER_SPECIFY = r51_OTHER_SPECIFY;
	}
	public BigDecimal getR51_TOTAL() {
		return R51_TOTAL;
	}
	public void setR51_TOTAL(BigDecimal r51_TOTAL) {
		R51_TOTAL = r51_TOTAL;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_EQUITY() {
		return R52_EQUITY;
	}
	public void setR52_EQUITY(BigDecimal r52_EQUITY) {
		R52_EQUITY = r52_EQUITY;
	}
	public BigDecimal getR52_BONDS() {
		return R52_BONDS;
	}
	public void setR52_BONDS(BigDecimal r52_BONDS) {
		R52_BONDS = r52_BONDS;
	}
	public BigDecimal getR52_BOBCS() {
		return R52_BOBCS;
	}
	public void setR52_BOBCS(BigDecimal r52_BOBCS) {
		R52_BOBCS = r52_BOBCS;
	}
	public BigDecimal getR52_TREASURY_BILLS() {
		return R52_TREASURY_BILLS;
	}
	public void setR52_TREASURY_BILLS(BigDecimal r52_TREASURY_BILLS) {
		R52_TREASURY_BILLS = r52_TREASURY_BILLS;
	}
	public BigDecimal getR52_REPURCHASE_AGREEMENTS() {
		return R52_REPURCHASE_AGREEMENTS;
	}
	public void setR52_REPURCHASE_AGREEMENTS(BigDecimal r52_REPURCHASE_AGREEMENTS) {
		R52_REPURCHASE_AGREEMENTS = r52_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR52_COMMERCIAL_PAPER() {
		return R52_COMMERCIAL_PAPER;
	}
	public void setR52_COMMERCIAL_PAPER(BigDecimal r52_COMMERCIAL_PAPER) {
		R52_COMMERCIAL_PAPER = r52_COMMERCIAL_PAPER;
	}
	public BigDecimal getR52_CERTIFICATES_OF_DEPOSITS() {
		return R52_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR52_CERTIFICATES_OF_DEPOSITS(BigDecimal r52_CERTIFICATES_OF_DEPOSITS) {
		R52_CERTIFICATES_OF_DEPOSITS = r52_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR52_PLEDGED_ASSETS() {
		return R52_PLEDGED_ASSETS;
	}
	public void setR52_PLEDGED_ASSETS(BigDecimal r52_PLEDGED_ASSETS) {
		R52_PLEDGED_ASSETS = r52_PLEDGED_ASSETS;
	}
	public BigDecimal getR52_OTHER_SPECIFY() {
		return R52_OTHER_SPECIFY;
	}
	public void setR52_OTHER_SPECIFY(BigDecimal r52_OTHER_SPECIFY) {
		R52_OTHER_SPECIFY = r52_OTHER_SPECIFY;
	}
	public BigDecimal getR52_TOTAL() {
		return R52_TOTAL;
	}
	public void setR52_TOTAL(BigDecimal r52_TOTAL) {
		R52_TOTAL = r52_TOTAL;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_EQUITY() {
		return R53_EQUITY;
	}
	public void setR53_EQUITY(BigDecimal r53_EQUITY) {
		R53_EQUITY = r53_EQUITY;
	}
	public BigDecimal getR53_BONDS() {
		return R53_BONDS;
	}
	public void setR53_BONDS(BigDecimal r53_BONDS) {
		R53_BONDS = r53_BONDS;
	}
	public BigDecimal getR53_BOBCS() {
		return R53_BOBCS;
	}
	public void setR53_BOBCS(BigDecimal r53_BOBCS) {
		R53_BOBCS = r53_BOBCS;
	}
	public BigDecimal getR53_TREASURY_BILLS() {
		return R53_TREASURY_BILLS;
	}
	public void setR53_TREASURY_BILLS(BigDecimal r53_TREASURY_BILLS) {
		R53_TREASURY_BILLS = r53_TREASURY_BILLS;
	}
	public BigDecimal getR53_REPURCHASE_AGREEMENTS() {
		return R53_REPURCHASE_AGREEMENTS;
	}
	public void setR53_REPURCHASE_AGREEMENTS(BigDecimal r53_REPURCHASE_AGREEMENTS) {
		R53_REPURCHASE_AGREEMENTS = r53_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR53_COMMERCIAL_PAPER() {
		return R53_COMMERCIAL_PAPER;
	}
	public void setR53_COMMERCIAL_PAPER(BigDecimal r53_COMMERCIAL_PAPER) {
		R53_COMMERCIAL_PAPER = r53_COMMERCIAL_PAPER;
	}
	public BigDecimal getR53_CERTIFICATES_OF_DEPOSITS() {
		return R53_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR53_CERTIFICATES_OF_DEPOSITS(BigDecimal r53_CERTIFICATES_OF_DEPOSITS) {
		R53_CERTIFICATES_OF_DEPOSITS = r53_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR53_PLEDGED_ASSETS() {
		return R53_PLEDGED_ASSETS;
	}
	public void setR53_PLEDGED_ASSETS(BigDecimal r53_PLEDGED_ASSETS) {
		R53_PLEDGED_ASSETS = r53_PLEDGED_ASSETS;
	}
	public BigDecimal getR53_OTHER_SPECIFY() {
		return R53_OTHER_SPECIFY;
	}
	public void setR53_OTHER_SPECIFY(BigDecimal r53_OTHER_SPECIFY) {
		R53_OTHER_SPECIFY = r53_OTHER_SPECIFY;
	}
	public BigDecimal getR53_TOTAL() {
		return R53_TOTAL;
	}
	public void setR53_TOTAL(BigDecimal r53_TOTAL) {
		R53_TOTAL = r53_TOTAL;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_EQUITY() {
		return R54_EQUITY;
	}
	public void setR54_EQUITY(BigDecimal r54_EQUITY) {
		R54_EQUITY = r54_EQUITY;
	}
	public BigDecimal getR54_BONDS() {
		return R54_BONDS;
	}
	public void setR54_BONDS(BigDecimal r54_BONDS) {
		R54_BONDS = r54_BONDS;
	}
	public BigDecimal getR54_BOBCS() {
		return R54_BOBCS;
	}
	public void setR54_BOBCS(BigDecimal r54_BOBCS) {
		R54_BOBCS = r54_BOBCS;
	}
	public BigDecimal getR54_TREASURY_BILLS() {
		return R54_TREASURY_BILLS;
	}
	public void setR54_TREASURY_BILLS(BigDecimal r54_TREASURY_BILLS) {
		R54_TREASURY_BILLS = r54_TREASURY_BILLS;
	}
	public BigDecimal getR54_REPURCHASE_AGREEMENTS() {
		return R54_REPURCHASE_AGREEMENTS;
	}
	public void setR54_REPURCHASE_AGREEMENTS(BigDecimal r54_REPURCHASE_AGREEMENTS) {
		R54_REPURCHASE_AGREEMENTS = r54_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR54_COMMERCIAL_PAPER() {
		return R54_COMMERCIAL_PAPER;
	}
	public void setR54_COMMERCIAL_PAPER(BigDecimal r54_COMMERCIAL_PAPER) {
		R54_COMMERCIAL_PAPER = r54_COMMERCIAL_PAPER;
	}
	public BigDecimal getR54_CERTIFICATES_OF_DEPOSITS() {
		return R54_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR54_CERTIFICATES_OF_DEPOSITS(BigDecimal r54_CERTIFICATES_OF_DEPOSITS) {
		R54_CERTIFICATES_OF_DEPOSITS = r54_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR54_PLEDGED_ASSETS() {
		return R54_PLEDGED_ASSETS;
	}
	public void setR54_PLEDGED_ASSETS(BigDecimal r54_PLEDGED_ASSETS) {
		R54_PLEDGED_ASSETS = r54_PLEDGED_ASSETS;
	}
	public BigDecimal getR54_OTHER_SPECIFY() {
		return R54_OTHER_SPECIFY;
	}
	public void setR54_OTHER_SPECIFY(BigDecimal r54_OTHER_SPECIFY) {
		R54_OTHER_SPECIFY = r54_OTHER_SPECIFY;
	}
	public BigDecimal getR54_TOTAL() {
		return R54_TOTAL;
	}
	public void setR54_TOTAL(BigDecimal r54_TOTAL) {
		R54_TOTAL = r54_TOTAL;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_EQUITY() {
		return R55_EQUITY;
	}
	public void setR55_EQUITY(BigDecimal r55_EQUITY) {
		R55_EQUITY = r55_EQUITY;
	}
	public BigDecimal getR55_BONDS() {
		return R55_BONDS;
	}
	public void setR55_BONDS(BigDecimal r55_BONDS) {
		R55_BONDS = r55_BONDS;
	}
	public BigDecimal getR55_BOBCS() {
		return R55_BOBCS;
	}
	public void setR55_BOBCS(BigDecimal r55_BOBCS) {
		R55_BOBCS = r55_BOBCS;
	}
	public BigDecimal getR55_TREASURY_BILLS() {
		return R55_TREASURY_BILLS;
	}
	public void setR55_TREASURY_BILLS(BigDecimal r55_TREASURY_BILLS) {
		R55_TREASURY_BILLS = r55_TREASURY_BILLS;
	}
	public BigDecimal getR55_REPURCHASE_AGREEMENTS() {
		return R55_REPURCHASE_AGREEMENTS;
	}
	public void setR55_REPURCHASE_AGREEMENTS(BigDecimal r55_REPURCHASE_AGREEMENTS) {
		R55_REPURCHASE_AGREEMENTS = r55_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR55_COMMERCIAL_PAPER() {
		return R55_COMMERCIAL_PAPER;
	}
	public void setR55_COMMERCIAL_PAPER(BigDecimal r55_COMMERCIAL_PAPER) {
		R55_COMMERCIAL_PAPER = r55_COMMERCIAL_PAPER;
	}
	public BigDecimal getR55_CERTIFICATES_OF_DEPOSITS() {
		return R55_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR55_CERTIFICATES_OF_DEPOSITS(BigDecimal r55_CERTIFICATES_OF_DEPOSITS) {
		R55_CERTIFICATES_OF_DEPOSITS = r55_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR55_PLEDGED_ASSETS() {
		return R55_PLEDGED_ASSETS;
	}
	public void setR55_PLEDGED_ASSETS(BigDecimal r55_PLEDGED_ASSETS) {
		R55_PLEDGED_ASSETS = r55_PLEDGED_ASSETS;
	}
	public BigDecimal getR55_OTHER_SPECIFY() {
		return R55_OTHER_SPECIFY;
	}
	public void setR55_OTHER_SPECIFY(BigDecimal r55_OTHER_SPECIFY) {
		R55_OTHER_SPECIFY = r55_OTHER_SPECIFY;
	}
	public BigDecimal getR55_TOTAL() {
		return R55_TOTAL;
	}
	public void setR55_TOTAL(BigDecimal r55_TOTAL) {
		R55_TOTAL = r55_TOTAL;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_EQUITY() {
		return R56_EQUITY;
	}
	public void setR56_EQUITY(BigDecimal r56_EQUITY) {
		R56_EQUITY = r56_EQUITY;
	}
	public BigDecimal getR56_BONDS() {
		return R56_BONDS;
	}
	public void setR56_BONDS(BigDecimal r56_BONDS) {
		R56_BONDS = r56_BONDS;
	}
	public BigDecimal getR56_BOBCS() {
		return R56_BOBCS;
	}
	public void setR56_BOBCS(BigDecimal r56_BOBCS) {
		R56_BOBCS = r56_BOBCS;
	}
	public BigDecimal getR56_TREASURY_BILLS() {
		return R56_TREASURY_BILLS;
	}
	public void setR56_TREASURY_BILLS(BigDecimal r56_TREASURY_BILLS) {
		R56_TREASURY_BILLS = r56_TREASURY_BILLS;
	}
	public BigDecimal getR56_REPURCHASE_AGREEMENTS() {
		return R56_REPURCHASE_AGREEMENTS;
	}
	public void setR56_REPURCHASE_AGREEMENTS(BigDecimal r56_REPURCHASE_AGREEMENTS) {
		R56_REPURCHASE_AGREEMENTS = r56_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR56_COMMERCIAL_PAPER() {
		return R56_COMMERCIAL_PAPER;
	}
	public void setR56_COMMERCIAL_PAPER(BigDecimal r56_COMMERCIAL_PAPER) {
		R56_COMMERCIAL_PAPER = r56_COMMERCIAL_PAPER;
	}
	public BigDecimal getR56_CERTIFICATES_OF_DEPOSITS() {
		return R56_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR56_CERTIFICATES_OF_DEPOSITS(BigDecimal r56_CERTIFICATES_OF_DEPOSITS) {
		R56_CERTIFICATES_OF_DEPOSITS = r56_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR56_PLEDGED_ASSETS() {
		return R56_PLEDGED_ASSETS;
	}
	public void setR56_PLEDGED_ASSETS(BigDecimal r56_PLEDGED_ASSETS) {
		R56_PLEDGED_ASSETS = r56_PLEDGED_ASSETS;
	}
	public BigDecimal getR56_OTHER_SPECIFY() {
		return R56_OTHER_SPECIFY;
	}
	public void setR56_OTHER_SPECIFY(BigDecimal r56_OTHER_SPECIFY) {
		R56_OTHER_SPECIFY = r56_OTHER_SPECIFY;
	}
	public BigDecimal getR56_TOTAL() {
		return R56_TOTAL;
	}
	public void setR56_TOTAL(BigDecimal r56_TOTAL) {
		R56_TOTAL = r56_TOTAL;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_EQUITY() {
		return R57_EQUITY;
	}
	public void setR57_EQUITY(BigDecimal r57_EQUITY) {
		R57_EQUITY = r57_EQUITY;
	}
	public BigDecimal getR57_BONDS() {
		return R57_BONDS;
	}
	public void setR57_BONDS(BigDecimal r57_BONDS) {
		R57_BONDS = r57_BONDS;
	}
	public BigDecimal getR57_BOBCS() {
		return R57_BOBCS;
	}
	public void setR57_BOBCS(BigDecimal r57_BOBCS) {
		R57_BOBCS = r57_BOBCS;
	}
	public BigDecimal getR57_TREASURY_BILLS() {
		return R57_TREASURY_BILLS;
	}
	public void setR57_TREASURY_BILLS(BigDecimal r57_TREASURY_BILLS) {
		R57_TREASURY_BILLS = r57_TREASURY_BILLS;
	}
	public BigDecimal getR57_REPURCHASE_AGREEMENTS() {
		return R57_REPURCHASE_AGREEMENTS;
	}
	public void setR57_REPURCHASE_AGREEMENTS(BigDecimal r57_REPURCHASE_AGREEMENTS) {
		R57_REPURCHASE_AGREEMENTS = r57_REPURCHASE_AGREEMENTS;
	}
	public BigDecimal getR57_COMMERCIAL_PAPER() {
		return R57_COMMERCIAL_PAPER;
	}
	public void setR57_COMMERCIAL_PAPER(BigDecimal r57_COMMERCIAL_PAPER) {
		R57_COMMERCIAL_PAPER = r57_COMMERCIAL_PAPER;
	}
	public BigDecimal getR57_CERTIFICATES_OF_DEPOSITS() {
		return R57_CERTIFICATES_OF_DEPOSITS;
	}
	public void setR57_CERTIFICATES_OF_DEPOSITS(BigDecimal r57_CERTIFICATES_OF_DEPOSITS) {
		R57_CERTIFICATES_OF_DEPOSITS = r57_CERTIFICATES_OF_DEPOSITS;
	}
	public BigDecimal getR57_PLEDGED_ASSETS() {
		return R57_PLEDGED_ASSETS;
	}
	public void setR57_PLEDGED_ASSETS(BigDecimal r57_PLEDGED_ASSETS) {
		R57_PLEDGED_ASSETS = r57_PLEDGED_ASSETS;
	}
	public BigDecimal getR57_OTHER_SPECIFY() {
		return R57_OTHER_SPECIFY;
	}
	public void setR57_OTHER_SPECIFY(BigDecimal r57_OTHER_SPECIFY) {
		R57_OTHER_SPECIFY = r57_OTHER_SPECIFY;
	}
	public BigDecimal getR57_TOTAL() {
		return R57_TOTAL;
	}
	public void setR57_TOTAL(BigDecimal r57_TOTAL) {
		R57_TOTAL = r57_TOTAL;
	}
	public M_SECL_Detail_Entity() {
		super();
	}
	
	
}
