package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_RWA_SUMMARYTABLE")
public class RWA_Summary_Entity {


@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id

private Date report_date;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;
	
	private BigDecimal R8_BOOK_VALUE;
	private BigDecimal R8_MARGINS;
	private BigDecimal R8_BOOK_VALUE_NET;
	private BigDecimal R8_RW;
	private BigDecimal R8_RISK_VALUE;

	private BigDecimal R9_BOOK_VALUE;
	private BigDecimal R9_MARGINS;
	private BigDecimal R9_BOOK_VALUE_NET;
	private BigDecimal R9_RW;
	private BigDecimal R9_RISK_VALUE;

	private BigDecimal R10_BOOK_VALUE;
	private BigDecimal R10_MARGINS;
	private BigDecimal R10_BOOK_VALUE_NET;
	private BigDecimal R10_RW;
	private BigDecimal R10_RISK_VALUE;

	private BigDecimal R11_BOOK_VALUE;
	private BigDecimal R11_MARGINS;
	private BigDecimal R11_BOOK_VALUE_NET;
	private BigDecimal R11_RW;
	private BigDecimal R11_RISK_VALUE;

	private BigDecimal R12_BOOK_VALUE;
	private BigDecimal R12_MARGINS;
	private BigDecimal R12_BOOK_VALUE_NET;
	private BigDecimal R12_RW;
	private BigDecimal R12_RISK_VALUE;

	private BigDecimal R13_BOOK_VALUE;
	private BigDecimal R13_MARGINS;
	private BigDecimal R13_BOOK_VALUE_NET;
	private BigDecimal R13_RW;
	private BigDecimal R13_RISK_VALUE;

	private BigDecimal R14_BOOK_VALUE;
	private BigDecimal R14_MARGINS;
	private BigDecimal R14_BOOK_VALUE_NET;
	private BigDecimal R14_RW;
	private BigDecimal R14_RISK_VALUE;

	private BigDecimal R15_BOOK_VALUE;
	private BigDecimal R15_MARGINS;
	private BigDecimal R15_BOOK_VALUE_NET;
	private BigDecimal R15_RW;
	private BigDecimal R15_RISK_VALUE;

	private BigDecimal R16_BOOK_VALUE;
	private BigDecimal R16_MARGINS;
	private BigDecimal R16_BOOK_VALUE_NET;
	private BigDecimal R16_RW;
	private BigDecimal R16_RISK_VALUE;

	private BigDecimal R17_BOOK_VALUE;
	private BigDecimal R17_MARGINS;
	private BigDecimal R17_BOOK_VALUE_NET;
	private BigDecimal R17_RW;
	private BigDecimal R17_RISK_VALUE;

	private BigDecimal R18_BOOK_VALUE;
	private BigDecimal R18_MARGINS;
	private BigDecimal R18_BOOK_VALUE_NET;
	private BigDecimal R18_RW;
	private BigDecimal R18_RISK_VALUE;

	private BigDecimal R19_BOOK_VALUE;
	private BigDecimal R19_MARGINS;
	private BigDecimal R19_BOOK_VALUE_NET;
	private BigDecimal R19_RW;
	private BigDecimal R19_RISK_VALUE;

	private BigDecimal R20_BOOK_VALUE;
	private BigDecimal R20_MARGINS;
	private BigDecimal R20_BOOK_VALUE_NET;
	private BigDecimal R20_RW;
	private BigDecimal R20_RISK_VALUE;

	private BigDecimal R21_BOOK_VALUE;
	private BigDecimal R21_MARGINS;
	private BigDecimal R21_BOOK_VALUE_NET;
	private BigDecimal R21_RW;
	private BigDecimal R21_RISK_VALUE;

	private BigDecimal R22_BOOK_VALUE;
	private BigDecimal R22_MARGINS;
	private BigDecimal R22_BOOK_VALUE_NET;
	private BigDecimal R22_RW;
	private BigDecimal R22_RISK_VALUE;

	private BigDecimal R23_BOOK_VALUE;
	private BigDecimal R23_MARGINS;
	private BigDecimal R23_BOOK_VALUE_NET;
	private BigDecimal R23_RW;
	private BigDecimal R23_RISK_VALUE;

	private BigDecimal R24_BOOK_VALUE;
	private BigDecimal R24_MARGINS;
	private BigDecimal R24_BOOK_VALUE_NET;
	private BigDecimal R24_RW;
	private BigDecimal R24_RISK_VALUE;

	private BigDecimal R25_BOOK_VALUE;
	private BigDecimal R25_MARGINS;
	private BigDecimal R25_BOOK_VALUE_NET;
	private BigDecimal R25_RW;
	private BigDecimal R25_RISK_VALUE;

	private BigDecimal R26_BOOK_VALUE;
	private BigDecimal R26_MARGINS;
	private BigDecimal R26_BOOK_VALUE_NET;
	private BigDecimal R26_RW;
	private BigDecimal R26_RISK_VALUE;

	private BigDecimal R27_BOOK_VALUE;
	private BigDecimal R27_MARGINS;
	private BigDecimal R27_BOOK_VALUE_NET;
	private BigDecimal R27_RW;
	private BigDecimal R27_RISK_VALUE;

	private BigDecimal R28_BOOK_VALUE;
	private BigDecimal R28_MARGINS;
	private BigDecimal R28_BOOK_VALUE_NET;
	private BigDecimal R28_RW;
	private BigDecimal R28_RISK_VALUE;

	private BigDecimal R29_BOOK_VALUE;
	private BigDecimal R29_MARGINS;
	private BigDecimal R29_BOOK_VALUE_NET;
	private BigDecimal R29_RW;
	private BigDecimal R29_RISK_VALUE;

	private BigDecimal R30_BOOK_VALUE;
	private BigDecimal R30_MARGINS;
	private BigDecimal R30_BOOK_VALUE_NET;
	private BigDecimal R30_RW;
	private BigDecimal R30_RISK_VALUE;

	private BigDecimal R31_BOOK_VALUE;
	private BigDecimal R31_MARGINS;
	private BigDecimal R31_BOOK_VALUE_NET;
	private BigDecimal R31_RW;
	private BigDecimal R31_RISK_VALUE;

	private BigDecimal R32_BOOK_VALUE;
	private BigDecimal R32_MARGINS;
	private BigDecimal R32_BOOK_VALUE_NET;
	private BigDecimal R32_RW;
	private BigDecimal R32_RISK_VALUE;

	private BigDecimal R33_BOOK_VALUE;
	private BigDecimal R33_MARGINS;
	private BigDecimal R33_BOOK_VALUE_NET;
	private BigDecimal R33_RW;
	private BigDecimal R33_RISK_VALUE;

	private BigDecimal R34_BOOK_VALUE;
	private BigDecimal R34_MARGINS;
	private BigDecimal R34_BOOK_VALUE_NET;
	private BigDecimal R34_RW;
	private BigDecimal R34_RISK_VALUE;

	private BigDecimal R35_BOOK_VALUE;
	private BigDecimal R35_MARGINS;
	private BigDecimal R35_BOOK_VALUE_NET;
	private BigDecimal R35_RW;
	private BigDecimal R35_RISK_VALUE;

	private BigDecimal R36_BOOK_VALUE;
	private BigDecimal R36_MARGINS;
	private BigDecimal R36_BOOK_VALUE_NET;
	private BigDecimal R36_RW;
	private BigDecimal R36_RISK_VALUE;

	private BigDecimal R37_BOOK_VALUE;
	private BigDecimal R37_MARGINS;
	private BigDecimal R37_BOOK_VALUE_NET;
	private BigDecimal R37_RW;
	private BigDecimal R37_RISK_VALUE;

	private BigDecimal R38_BOOK_VALUE;
	private BigDecimal R38_MARGINS;
	private BigDecimal R38_BOOK_VALUE_NET;
	private BigDecimal R38_RW;
	private BigDecimal R38_RISK_VALUE;

	private BigDecimal R39_BOOK_VALUE;
	private BigDecimal R39_MARGINS;
	private BigDecimal R39_BOOK_VALUE_NET;
	private BigDecimal R39_RW;
	private BigDecimal R39_RISK_VALUE;

	private BigDecimal R40_BOOK_VALUE;
	private BigDecimal R40_MARGINS;
	private BigDecimal R40_BOOK_VALUE_NET;
	private BigDecimal R40_RW;
	private BigDecimal R40_RISK_VALUE;

	private BigDecimal R41_BOOK_VALUE;
	private BigDecimal R41_MARGINS;
	private BigDecimal R41_BOOK_VALUE_NET;
	private BigDecimal R41_RW;
	private BigDecimal R41_RISK_VALUE;

	private BigDecimal R42_BOOK_VALUE;
	private BigDecimal R42_MARGINS;
	private BigDecimal R42_BOOK_VALUE_NET;
	private BigDecimal R42_RW;
	private BigDecimal R42_RISK_VALUE;

	private BigDecimal R43_BOOK_VALUE;
	private BigDecimal R43_MARGINS;
	private BigDecimal R43_BOOK_VALUE_NET;
	private BigDecimal R43_RW;
	private BigDecimal R43_RISK_VALUE;

	private BigDecimal R44_BOOK_VALUE;
	private BigDecimal R44_MARGINS;
	private BigDecimal R44_BOOK_VALUE_NET;
	private BigDecimal R44_RW;
	private BigDecimal R44_RISK_VALUE;

	private BigDecimal R45_BOOK_VALUE;
	private BigDecimal R45_MARGINS;
	private BigDecimal R45_BOOK_VALUE_NET;
	private BigDecimal R45_RW;
	private BigDecimal R45_RISK_VALUE;

	private BigDecimal R46_BOOK_VALUE;
	private BigDecimal R46_MARGINS;
	private BigDecimal R46_BOOK_VALUE_NET;
	private BigDecimal R46_RW;
	private BigDecimal R46_RISK_VALUE;

	private BigDecimal R48_BOOK_VALUE;
	private BigDecimal R48_MARGINS;
	private BigDecimal R48_BOOK_VALUE_NET;
	private BigDecimal R48_RW;
	private BigDecimal R48_RISK_VALUE;

	private BigDecimal R61_BOOK_VALUE;
	private BigDecimal R61_MARGINS;
	private BigDecimal R61_BOOK_VALUE_NET;
	private BigDecimal R61_RW;
	private BigDecimal R61_RISK_VALUE;

	private BigDecimal R63_BOOK_VALUE;
	private BigDecimal R63_MARGINS;
	private BigDecimal R63_BOOK_VALUE_NET;
	private BigDecimal R63_RW;
	private BigDecimal R63_RISK_VALUE;

	private BigDecimal R64_BOOK_VALUE;
	private BigDecimal R64_MARGINS;
	private BigDecimal R64_BOOK_VALUE_NET;
	private BigDecimal R64_RW;
	private BigDecimal R64_RISK_VALUE;

	private BigDecimal R65_BOOK_VALUE;
	private BigDecimal R65_MARGINS;
	private BigDecimal R65_BOOK_VALUE_NET;
	private BigDecimal R65_RW;
	private BigDecimal R65_RISK_VALUE;

	private BigDecimal R66_BOOK_VALUE;
	private BigDecimal R66_MARGINS;
	private BigDecimal R66_BOOK_VALUE_NET;
	private BigDecimal R66_RW;
	private BigDecimal R66_RISK_VALUE;

	private BigDecimal R67_BOOK_VALUE;
	private BigDecimal R67_MARGINS;
	private BigDecimal R67_BOOK_VALUE_NET;
	private BigDecimal R67_RW;
	private BigDecimal R67_RISK_VALUE;

	private BigDecimal R68_BOOK_VALUE;
	private BigDecimal R68_MARGINS;
	private BigDecimal R68_BOOK_VALUE_NET;
	private BigDecimal R68_RW;
	private BigDecimal R68_RISK_VALUE;

	private BigDecimal R69_BOOK_VALUE;
	private BigDecimal R69_MARGINS;
	private BigDecimal R69_BOOK_VALUE_NET;
	private BigDecimal R69_RW;
	private BigDecimal R69_RISK_VALUE;

	private BigDecimal R70_BOOK_VALUE;
	private BigDecimal R70_MARGINS;
	private BigDecimal R70_BOOK_VALUE_NET;
	private BigDecimal R70_RW;
	private BigDecimal R70_RISK_VALUE;

	private BigDecimal R71_BOOK_VALUE;
	private BigDecimal R71_MARGINS;
	private BigDecimal R71_BOOK_VALUE_NET;
	private BigDecimal R71_RW;
	private BigDecimal R71_RISK_VALUE;

	private BigDecimal R72_BOOK_VALUE;
	private BigDecimal R72_MARGINS;
	private BigDecimal R72_BOOK_VALUE_NET;
	private BigDecimal R72_RW;
	private BigDecimal R72_RISK_VALUE;

	private BigDecimal R73_BOOK_VALUE;
	private BigDecimal R73_MARGINS;
	private BigDecimal R73_BOOK_VALUE_NET;
	private BigDecimal R73_RW;
	private BigDecimal R73_RISK_VALUE;

	private BigDecimal R74_BOOK_VALUE;
	private BigDecimal R74_MARGINS;
	private BigDecimal R74_BOOK_VALUE_NET;
	private BigDecimal R74_RW;
	private BigDecimal R74_RISK_VALUE;

	private BigDecimal R75_BOOK_VALUE;
	private BigDecimal R75_MARGINS;
	private BigDecimal R75_BOOK_VALUE_NET;
	private BigDecimal R75_RW;
	private BigDecimal R75_RISK_VALUE;

	private BigDecimal R76_BOOK_VALUE;
	private BigDecimal R76_MARGINS;
	private BigDecimal R76_BOOK_VALUE_NET;
	private BigDecimal R76_RW;
	private BigDecimal R76_RISK_VALUE;

	private BigDecimal R77_BOOK_VALUE;
	private BigDecimal R77_MARGINS;
	private BigDecimal R77_BOOK_VALUE_NET;
	private BigDecimal R77_RW;
	private BigDecimal R77_RISK_VALUE;

	private BigDecimal R78_BOOK_VALUE;
	private BigDecimal R78_MARGINS;
	private BigDecimal R78_BOOK_VALUE_NET;
	private BigDecimal R78_RW;
	private BigDecimal R78_RISK_VALUE;

	private BigDecimal R79_BOOK_VALUE;
	private BigDecimal R79_MARGINS;
	private BigDecimal R79_BOOK_VALUE_NET;
	private BigDecimal R79_RW;
	private BigDecimal R79_RISK_VALUE;

	private BigDecimal R80_BOOK_VALUE;
	private BigDecimal R80_MARGINS;
	private BigDecimal R80_BOOK_VALUE_NET;
	private BigDecimal R80_RW;
	private BigDecimal R80_RISK_VALUE;

	private BigDecimal R81_BOOK_VALUE;
	private BigDecimal R81_MARGINS;
	private BigDecimal R81_BOOK_VALUE_NET;
	private BigDecimal R81_RW;
	private BigDecimal R81_RISK_VALUE;

	private BigDecimal R82_BOOK_VALUE;
	private BigDecimal R82_MARGINS;
	private BigDecimal R82_BOOK_VALUE_NET;
	private BigDecimal R82_RW;
	private BigDecimal R82_RISK_VALUE;

	private BigDecimal R97_BOOK_VALUE;
	private BigDecimal R97_MARGINS;
	private BigDecimal R97_BOOK_VALUE_NET;
	private BigDecimal R97_RW;
	private BigDecimal R97_RISK_VALUE;

	private BigDecimal R98_BOOK_VALUE;
	private BigDecimal R98_MARGINS;
	private BigDecimal R98_BOOK_VALUE_NET;
	private BigDecimal R98_RW;
	private BigDecimal R98_RISK_VALUE;

	private BigDecimal R99_BOOK_VALUE;
	private BigDecimal R99_MARGINS;
	private BigDecimal R99_BOOK_VALUE_NET;
	private BigDecimal R99_RW;
	private BigDecimal R99_RISK_VALUE;

	private BigDecimal R100_BOOK_VALUE;
	private BigDecimal R100_MARGINS;
	private BigDecimal R100_BOOK_VALUE_NET;
	private BigDecimal R100_RW;
	private BigDecimal R100_RISK_VALUE;

	private BigDecimal R101_BOOK_VALUE;
	private BigDecimal R101_MARGINS;
	private BigDecimal R101_BOOK_VALUE_NET;
	private BigDecimal R101_RW;
	private BigDecimal R101_RISK_VALUE;

	private BigDecimal R102_BOOK_VALUE;
	private BigDecimal R102_MARGINS;
	private BigDecimal R102_BOOK_VALUE_NET;
	private BigDecimal R102_RW;
	private BigDecimal R102_RISK_VALUE;

	private BigDecimal R103_BOOK_VALUE;
	private BigDecimal R103_MARGINS;
	private BigDecimal R103_BOOK_VALUE_NET;
	private BigDecimal R103_RW;
	private BigDecimal R103_RISK_VALUE;

	private BigDecimal R104_BOOK_VALUE;
	private BigDecimal R104_MARGINS;
	private BigDecimal R104_BOOK_VALUE_NET;
	private BigDecimal R104_RW;
	private BigDecimal R104_RISK_VALUE;

	private BigDecimal R105_BOOK_VALUE;
	private BigDecimal R105_MARGINS;
	private BigDecimal R105_BOOK_VALUE_NET;
	private BigDecimal R105_RW;
	private BigDecimal R105_RISK_VALUE;

	private BigDecimal R106_BOOK_VALUE;
	private BigDecimal R106_MARGINS;
	private BigDecimal R106_BOOK_VALUE_NET;
	private BigDecimal R106_RW;
	private BigDecimal R106_RISK_VALUE;

	private BigDecimal R107_BOOK_VALUE;
	private BigDecimal R107_MARGINS;
	private BigDecimal R107_BOOK_VALUE_NET;
	private BigDecimal R107_RW;
	private BigDecimal R107_RISK_VALUE;

	private BigDecimal R108_BOOK_VALUE;
	private BigDecimal R108_MARGINS;
	private BigDecimal R108_BOOK_VALUE_NET;
	private BigDecimal R108_RW;
	private BigDecimal R108_RISK_VALUE;

	private BigDecimal R109_BOOK_VALUE;
	private BigDecimal R109_MARGINS;
	private BigDecimal R109_BOOK_VALUE_NET;
	private BigDecimal R109_RW;
	private BigDecimal R109_RISK_VALUE;

	private BigDecimal R110_BOOK_VALUE;
	private BigDecimal R110_MARGINS;
	private BigDecimal R110_BOOK_VALUE_NET;
	private BigDecimal R110_RW;
	private BigDecimal R110_RISK_VALUE;

	private BigDecimal R111_BOOK_VALUE;
	private BigDecimal R111_MARGINS;
	private BigDecimal R111_BOOK_VALUE_NET;
	private BigDecimal R111_RW;
	private BigDecimal R111_RISK_VALUE;

	private BigDecimal R112_BOOK_VALUE;
	private BigDecimal R112_MARGINS;
	private BigDecimal R112_BOOK_VALUE_NET;
	private BigDecimal R112_RW;
	private BigDecimal R112_RISK_VALUE;

	private BigDecimal R113_BOOK_VALUE;
	private BigDecimal R113_MARGINS;
	private BigDecimal R113_BOOK_VALUE_NET;
	private BigDecimal R113_RW;
	private BigDecimal R113_RISK_VALUE;

	private BigDecimal R114_BOOK_VALUE;
	private BigDecimal R114_MARGINS;
	private BigDecimal R114_BOOK_VALUE_NET;
	private BigDecimal R114_RW;
	private BigDecimal R114_RISK_VALUE;

	private BigDecimal R115_BOOK_VALUE;
	private BigDecimal R115_MARGINS;
	private BigDecimal R115_BOOK_VALUE_NET;
	private BigDecimal R115_RW;
	private BigDecimal R115_RISK_VALUE;

	private BigDecimal R116_BOOK_VALUE;
	private BigDecimal R116_MARGINS;
	private BigDecimal R116_BOOK_VALUE_NET;
	private BigDecimal R116_RW;
	private BigDecimal R116_RISK_VALUE;

	private BigDecimal R117_BOOK_VALUE;
	private BigDecimal R117_MARGINS;
	private BigDecimal R117_BOOK_VALUE_NET;
	private BigDecimal R117_RW;
	private BigDecimal R117_RISK_VALUE;

	private BigDecimal R118_BOOK_VALUE;
	private BigDecimal R118_MARGINS;
	private BigDecimal R118_BOOK_VALUE_NET;
	private BigDecimal R118_RW;
	private BigDecimal R118_RISK_VALUE;

	private BigDecimal R119_BOOK_VALUE;
	private BigDecimal R119_MARGINS;
	private BigDecimal R119_BOOK_VALUE_NET;
	private BigDecimal R119_RW;
	private BigDecimal R119_RISK_VALUE;

	private BigDecimal R120_BOOK_VALUE;
	private BigDecimal R120_MARGINS;
	private BigDecimal R120_BOOK_VALUE_NET;
	private BigDecimal R120_RW;
	private BigDecimal R120_RISK_VALUE;

	private BigDecimal R121_BOOK_VALUE;
	private BigDecimal R121_MARGINS;
	private BigDecimal R121_BOOK_VALUE_NET;
	private BigDecimal R121_RW;
	private BigDecimal R121_RISK_VALUE;

	private BigDecimal R122_BOOK_VALUE;
	private BigDecimal R122_MARGINS;
	private BigDecimal R122_BOOK_VALUE_NET;
	private BigDecimal R122_RW;
	private BigDecimal R122_RISK_VALUE;

	private BigDecimal R123_BOOK_VALUE;
	private BigDecimal R123_MARGINS;
	private BigDecimal R123_BOOK_VALUE_NET;
	private BigDecimal R123_RW;
	private BigDecimal R123_RISK_VALUE;

	private BigDecimal R124_BOOK_VALUE;
	private BigDecimal R124_MARGINS;
	private BigDecimal R124_BOOK_VALUE_NET;
	private BigDecimal R124_RW;
	private BigDecimal R124_RISK_VALUE;

	private BigDecimal R125_BOOK_VALUE;
	private BigDecimal R125_MARGINS;
	private BigDecimal R125_BOOK_VALUE_NET;
	private BigDecimal R125_RW;
	private BigDecimal R125_RISK_VALUE;

	private BigDecimal R126_BOOK_VALUE;
	private BigDecimal R126_MARGINS;
	private BigDecimal R126_BOOK_VALUE_NET;
	private BigDecimal R126_RW;
	private BigDecimal R126_RISK_VALUE;

	private BigDecimal R127_BOOK_VALUE;
	private BigDecimal R127_MARGINS;
	private BigDecimal R127_BOOK_VALUE_NET;
	private BigDecimal R127_RW;
	private BigDecimal R127_RISK_VALUE;

	private BigDecimal R128_BOOK_VALUE;
	private BigDecimal R128_MARGINS;
	private BigDecimal R128_BOOK_VALUE_NET;
	private BigDecimal R128_RW;
	private BigDecimal R128_RISK_VALUE;

	private BigDecimal R129_BOOK_VALUE;
	private BigDecimal R129_MARGINS;
	private BigDecimal R129_BOOK_VALUE_NET;
	private BigDecimal R129_RW;
	private BigDecimal R129_RISK_VALUE;

	private BigDecimal R130_BOOK_VALUE;
	private BigDecimal R130_MARGINS;
	private BigDecimal R130_BOOK_VALUE_NET;
	private BigDecimal R130_RW;
	private BigDecimal R130_RISK_VALUE;
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getReport_version() {
		return report_version;
	}
	public void setReport_version(String report_version) {
		this.report_version = report_version;
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
	public BigDecimal getR8_BOOK_VALUE() {
		return R8_BOOK_VALUE;
	}
	public void setR8_BOOK_VALUE(BigDecimal r8_BOOK_VALUE) {
		R8_BOOK_VALUE = r8_BOOK_VALUE;
	}
	public BigDecimal getR8_MARGINS() {
		return R8_MARGINS;
	}
	public void setR8_MARGINS(BigDecimal r8_MARGINS) {
		R8_MARGINS = r8_MARGINS;
	}
	public BigDecimal getR8_BOOK_VALUE_NET() {
		return R8_BOOK_VALUE_NET;
	}
	public void setR8_BOOK_VALUE_NET(BigDecimal r8_BOOK_VALUE_NET) {
		R8_BOOK_VALUE_NET = r8_BOOK_VALUE_NET;
	}
	public BigDecimal getR8_RW() {
		return R8_RW;
	}
	public void setR8_RW(BigDecimal r8_RW) {
		R8_RW = r8_RW;
	}
	public BigDecimal getR8_RISK_VALUE() {
		return R8_RISK_VALUE;
	}
	public void setR8_RISK_VALUE(BigDecimal r8_RISK_VALUE) {
		R8_RISK_VALUE = r8_RISK_VALUE;
	}
	public BigDecimal getR9_BOOK_VALUE() {
		return R9_BOOK_VALUE;
	}
	public void setR9_BOOK_VALUE(BigDecimal r9_BOOK_VALUE) {
		R9_BOOK_VALUE = r9_BOOK_VALUE;
	}
	public BigDecimal getR9_MARGINS() {
		return R9_MARGINS;
	}
	public void setR9_MARGINS(BigDecimal r9_MARGINS) {
		R9_MARGINS = r9_MARGINS;
	}
	public BigDecimal getR9_BOOK_VALUE_NET() {
		return R9_BOOK_VALUE_NET;
	}
	public void setR9_BOOK_VALUE_NET(BigDecimal r9_BOOK_VALUE_NET) {
		R9_BOOK_VALUE_NET = r9_BOOK_VALUE_NET;
	}
	public BigDecimal getR9_RW() {
		return R9_RW;
	}
	public void setR9_RW(BigDecimal r9_RW) {
		R9_RW = r9_RW;
	}
	public BigDecimal getR9_RISK_VALUE() {
		return R9_RISK_VALUE;
	}
	public void setR9_RISK_VALUE(BigDecimal r9_RISK_VALUE) {
		R9_RISK_VALUE = r9_RISK_VALUE;
	}
	public BigDecimal getR10_BOOK_VALUE() {
		return R10_BOOK_VALUE;
	}
	public void setR10_BOOK_VALUE(BigDecimal r10_BOOK_VALUE) {
		R10_BOOK_VALUE = r10_BOOK_VALUE;
	}
	public BigDecimal getR10_MARGINS() {
		return R10_MARGINS;
	}
	public void setR10_MARGINS(BigDecimal r10_MARGINS) {
		R10_MARGINS = r10_MARGINS;
	}
	public BigDecimal getR10_BOOK_VALUE_NET() {
		return R10_BOOK_VALUE_NET;
	}
	public void setR10_BOOK_VALUE_NET(BigDecimal r10_BOOK_VALUE_NET) {
		R10_BOOK_VALUE_NET = r10_BOOK_VALUE_NET;
	}
	public BigDecimal getR10_RW() {
		return R10_RW;
	}
	public void setR10_RW(BigDecimal r10_RW) {
		R10_RW = r10_RW;
	}
	public BigDecimal getR10_RISK_VALUE() {
		return R10_RISK_VALUE;
	}
	public void setR10_RISK_VALUE(BigDecimal r10_RISK_VALUE) {
		R10_RISK_VALUE = r10_RISK_VALUE;
	}
	public BigDecimal getR11_BOOK_VALUE() {
		return R11_BOOK_VALUE;
	}
	public void setR11_BOOK_VALUE(BigDecimal r11_BOOK_VALUE) {
		R11_BOOK_VALUE = r11_BOOK_VALUE;
	}
	public BigDecimal getR11_MARGINS() {
		return R11_MARGINS;
	}
	public void setR11_MARGINS(BigDecimal r11_MARGINS) {
		R11_MARGINS = r11_MARGINS;
	}
	public BigDecimal getR11_BOOK_VALUE_NET() {
		return R11_BOOK_VALUE_NET;
	}
	public void setR11_BOOK_VALUE_NET(BigDecimal r11_BOOK_VALUE_NET) {
		R11_BOOK_VALUE_NET = r11_BOOK_VALUE_NET;
	}
	public BigDecimal getR11_RW() {
		return R11_RW;
	}
	public void setR11_RW(BigDecimal r11_RW) {
		R11_RW = r11_RW;
	}
	public BigDecimal getR11_RISK_VALUE() {
		return R11_RISK_VALUE;
	}
	public void setR11_RISK_VALUE(BigDecimal r11_RISK_VALUE) {
		R11_RISK_VALUE = r11_RISK_VALUE;
	}
	public BigDecimal getR12_BOOK_VALUE() {
		return R12_BOOK_VALUE;
	}
	public void setR12_BOOK_VALUE(BigDecimal r12_BOOK_VALUE) {
		R12_BOOK_VALUE = r12_BOOK_VALUE;
	}
	public BigDecimal getR12_MARGINS() {
		return R12_MARGINS;
	}
	public void setR12_MARGINS(BigDecimal r12_MARGINS) {
		R12_MARGINS = r12_MARGINS;
	}
	public BigDecimal getR12_BOOK_VALUE_NET() {
		return R12_BOOK_VALUE_NET;
	}
	public void setR12_BOOK_VALUE_NET(BigDecimal r12_BOOK_VALUE_NET) {
		R12_BOOK_VALUE_NET = r12_BOOK_VALUE_NET;
	}
	public BigDecimal getR12_RW() {
		return R12_RW;
	}
	public void setR12_RW(BigDecimal r12_RW) {
		R12_RW = r12_RW;
	}
	public BigDecimal getR12_RISK_VALUE() {
		return R12_RISK_VALUE;
	}
	public void setR12_RISK_VALUE(BigDecimal r12_RISK_VALUE) {
		R12_RISK_VALUE = r12_RISK_VALUE;
	}
	public BigDecimal getR13_BOOK_VALUE() {
		return R13_BOOK_VALUE;
	}
	public void setR13_BOOK_VALUE(BigDecimal r13_BOOK_VALUE) {
		R13_BOOK_VALUE = r13_BOOK_VALUE;
	}
	public BigDecimal getR13_MARGINS() {
		return R13_MARGINS;
	}
	public void setR13_MARGINS(BigDecimal r13_MARGINS) {
		R13_MARGINS = r13_MARGINS;
	}
	public BigDecimal getR13_BOOK_VALUE_NET() {
		return R13_BOOK_VALUE_NET;
	}
	public void setR13_BOOK_VALUE_NET(BigDecimal r13_BOOK_VALUE_NET) {
		R13_BOOK_VALUE_NET = r13_BOOK_VALUE_NET;
	}
	public BigDecimal getR13_RW() {
		return R13_RW;
	}
	public void setR13_RW(BigDecimal r13_RW) {
		R13_RW = r13_RW;
	}
	public BigDecimal getR13_RISK_VALUE() {
		return R13_RISK_VALUE;
	}
	public void setR13_RISK_VALUE(BigDecimal r13_RISK_VALUE) {
		R13_RISK_VALUE = r13_RISK_VALUE;
	}
	public BigDecimal getR14_BOOK_VALUE() {
		return R14_BOOK_VALUE;
	}
	public void setR14_BOOK_VALUE(BigDecimal r14_BOOK_VALUE) {
		R14_BOOK_VALUE = r14_BOOK_VALUE;
	}
	public BigDecimal getR14_MARGINS() {
		return R14_MARGINS;
	}
	public void setR14_MARGINS(BigDecimal r14_MARGINS) {
		R14_MARGINS = r14_MARGINS;
	}
	public BigDecimal getR14_BOOK_VALUE_NET() {
		return R14_BOOK_VALUE_NET;
	}
	public void setR14_BOOK_VALUE_NET(BigDecimal r14_BOOK_VALUE_NET) {
		R14_BOOK_VALUE_NET = r14_BOOK_VALUE_NET;
	}
	public BigDecimal getR14_RW() {
		return R14_RW;
	}
	public void setR14_RW(BigDecimal r14_RW) {
		R14_RW = r14_RW;
	}
	public BigDecimal getR14_RISK_VALUE() {
		return R14_RISK_VALUE;
	}
	public void setR14_RISK_VALUE(BigDecimal r14_RISK_VALUE) {
		R14_RISK_VALUE = r14_RISK_VALUE;
	}
	public BigDecimal getR15_BOOK_VALUE() {
		return R15_BOOK_VALUE;
	}
	public void setR15_BOOK_VALUE(BigDecimal r15_BOOK_VALUE) {
		R15_BOOK_VALUE = r15_BOOK_VALUE;
	}
	public BigDecimal getR15_MARGINS() {
		return R15_MARGINS;
	}
	public void setR15_MARGINS(BigDecimal r15_MARGINS) {
		R15_MARGINS = r15_MARGINS;
	}
	public BigDecimal getR15_BOOK_VALUE_NET() {
		return R15_BOOK_VALUE_NET;
	}
	public void setR15_BOOK_VALUE_NET(BigDecimal r15_BOOK_VALUE_NET) {
		R15_BOOK_VALUE_NET = r15_BOOK_VALUE_NET;
	}
	public BigDecimal getR15_RW() {
		return R15_RW;
	}
	public void setR15_RW(BigDecimal r15_RW) {
		R15_RW = r15_RW;
	}
	public BigDecimal getR15_RISK_VALUE() {
		return R15_RISK_VALUE;
	}
	public void setR15_RISK_VALUE(BigDecimal r15_RISK_VALUE) {
		R15_RISK_VALUE = r15_RISK_VALUE;
	}
	public BigDecimal getR16_BOOK_VALUE() {
		return R16_BOOK_VALUE;
	}
	public void setR16_BOOK_VALUE(BigDecimal r16_BOOK_VALUE) {
		R16_BOOK_VALUE = r16_BOOK_VALUE;
	}
	public BigDecimal getR16_MARGINS() {
		return R16_MARGINS;
	}
	public void setR16_MARGINS(BigDecimal r16_MARGINS) {
		R16_MARGINS = r16_MARGINS;
	}
	public BigDecimal getR16_BOOK_VALUE_NET() {
		return R16_BOOK_VALUE_NET;
	}
	public void setR16_BOOK_VALUE_NET(BigDecimal r16_BOOK_VALUE_NET) {
		R16_BOOK_VALUE_NET = r16_BOOK_VALUE_NET;
	}
	public BigDecimal getR16_RW() {
		return R16_RW;
	}
	public void setR16_RW(BigDecimal r16_RW) {
		R16_RW = r16_RW;
	}
	public BigDecimal getR16_RISK_VALUE() {
		return R16_RISK_VALUE;
	}
	public void setR16_RISK_VALUE(BigDecimal r16_RISK_VALUE) {
		R16_RISK_VALUE = r16_RISK_VALUE;
	}
	public BigDecimal getR17_BOOK_VALUE() {
		return R17_BOOK_VALUE;
	}
	public void setR17_BOOK_VALUE(BigDecimal r17_BOOK_VALUE) {
		R17_BOOK_VALUE = r17_BOOK_VALUE;
	}
	public BigDecimal getR17_MARGINS() {
		return R17_MARGINS;
	}
	public void setR17_MARGINS(BigDecimal r17_MARGINS) {
		R17_MARGINS = r17_MARGINS;
	}
	public BigDecimal getR17_BOOK_VALUE_NET() {
		return R17_BOOK_VALUE_NET;
	}
	public void setR17_BOOK_VALUE_NET(BigDecimal r17_BOOK_VALUE_NET) {
		R17_BOOK_VALUE_NET = r17_BOOK_VALUE_NET;
	}
	public BigDecimal getR17_RW() {
		return R17_RW;
	}
	public void setR17_RW(BigDecimal r17_RW) {
		R17_RW = r17_RW;
	}
	public BigDecimal getR17_RISK_VALUE() {
		return R17_RISK_VALUE;
	}
	public void setR17_RISK_VALUE(BigDecimal r17_RISK_VALUE) {
		R17_RISK_VALUE = r17_RISK_VALUE;
	}
	public BigDecimal getR18_BOOK_VALUE() {
		return R18_BOOK_VALUE;
	}
	public void setR18_BOOK_VALUE(BigDecimal r18_BOOK_VALUE) {
		R18_BOOK_VALUE = r18_BOOK_VALUE;
	}
	public BigDecimal getR18_MARGINS() {
		return R18_MARGINS;
	}
	public void setR18_MARGINS(BigDecimal r18_MARGINS) {
		R18_MARGINS = r18_MARGINS;
	}
	public BigDecimal getR18_BOOK_VALUE_NET() {
		return R18_BOOK_VALUE_NET;
	}
	public void setR18_BOOK_VALUE_NET(BigDecimal r18_BOOK_VALUE_NET) {
		R18_BOOK_VALUE_NET = r18_BOOK_VALUE_NET;
	}
	public BigDecimal getR18_RW() {
		return R18_RW;
	}
	public void setR18_RW(BigDecimal r18_RW) {
		R18_RW = r18_RW;
	}
	public BigDecimal getR18_RISK_VALUE() {
		return R18_RISK_VALUE;
	}
	public void setR18_RISK_VALUE(BigDecimal r18_RISK_VALUE) {
		R18_RISK_VALUE = r18_RISK_VALUE;
	}
	public BigDecimal getR19_BOOK_VALUE() {
		return R19_BOOK_VALUE;
	}
	public void setR19_BOOK_VALUE(BigDecimal r19_BOOK_VALUE) {
		R19_BOOK_VALUE = r19_BOOK_VALUE;
	}
	public BigDecimal getR19_MARGINS() {
		return R19_MARGINS;
	}
	public void setR19_MARGINS(BigDecimal r19_MARGINS) {
		R19_MARGINS = r19_MARGINS;
	}
	public BigDecimal getR19_BOOK_VALUE_NET() {
		return R19_BOOK_VALUE_NET;
	}
	public void setR19_BOOK_VALUE_NET(BigDecimal r19_BOOK_VALUE_NET) {
		R19_BOOK_VALUE_NET = r19_BOOK_VALUE_NET;
	}
	public BigDecimal getR19_RW() {
		return R19_RW;
	}
	public void setR19_RW(BigDecimal r19_RW) {
		R19_RW = r19_RW;
	}
	public BigDecimal getR19_RISK_VALUE() {
		return R19_RISK_VALUE;
	}
	public void setR19_RISK_VALUE(BigDecimal r19_RISK_VALUE) {
		R19_RISK_VALUE = r19_RISK_VALUE;
	}
	public BigDecimal getR20_BOOK_VALUE() {
		return R20_BOOK_VALUE;
	}
	public void setR20_BOOK_VALUE(BigDecimal r20_BOOK_VALUE) {
		R20_BOOK_VALUE = r20_BOOK_VALUE;
	}
	public BigDecimal getR20_MARGINS() {
		return R20_MARGINS;
	}
	public void setR20_MARGINS(BigDecimal r20_MARGINS) {
		R20_MARGINS = r20_MARGINS;
	}
	public BigDecimal getR20_BOOK_VALUE_NET() {
		return R20_BOOK_VALUE_NET;
	}
	public void setR20_BOOK_VALUE_NET(BigDecimal r20_BOOK_VALUE_NET) {
		R20_BOOK_VALUE_NET = r20_BOOK_VALUE_NET;
	}
	public BigDecimal getR20_RW() {
		return R20_RW;
	}
	public void setR20_RW(BigDecimal r20_RW) {
		R20_RW = r20_RW;
	}
	public BigDecimal getR20_RISK_VALUE() {
		return R20_RISK_VALUE;
	}
	public void setR20_RISK_VALUE(BigDecimal r20_RISK_VALUE) {
		R20_RISK_VALUE = r20_RISK_VALUE;
	}
	public BigDecimal getR21_BOOK_VALUE() {
		return R21_BOOK_VALUE;
	}
	public void setR21_BOOK_VALUE(BigDecimal r21_BOOK_VALUE) {
		R21_BOOK_VALUE = r21_BOOK_VALUE;
	}
	public BigDecimal getR21_MARGINS() {
		return R21_MARGINS;
	}
	public void setR21_MARGINS(BigDecimal r21_MARGINS) {
		R21_MARGINS = r21_MARGINS;
	}
	public BigDecimal getR21_BOOK_VALUE_NET() {
		return R21_BOOK_VALUE_NET;
	}
	public void setR21_BOOK_VALUE_NET(BigDecimal r21_BOOK_VALUE_NET) {
		R21_BOOK_VALUE_NET = r21_BOOK_VALUE_NET;
	}
	public BigDecimal getR21_RW() {
		return R21_RW;
	}
	public void setR21_RW(BigDecimal r21_RW) {
		R21_RW = r21_RW;
	}
	public BigDecimal getR21_RISK_VALUE() {
		return R21_RISK_VALUE;
	}
	public void setR21_RISK_VALUE(BigDecimal r21_RISK_VALUE) {
		R21_RISK_VALUE = r21_RISK_VALUE;
	}
	public BigDecimal getR22_BOOK_VALUE() {
		return R22_BOOK_VALUE;
	}
	public void setR22_BOOK_VALUE(BigDecimal r22_BOOK_VALUE) {
		R22_BOOK_VALUE = r22_BOOK_VALUE;
	}
	public BigDecimal getR22_MARGINS() {
		return R22_MARGINS;
	}
	public void setR22_MARGINS(BigDecimal r22_MARGINS) {
		R22_MARGINS = r22_MARGINS;
	}
	public BigDecimal getR22_BOOK_VALUE_NET() {
		return R22_BOOK_VALUE_NET;
	}
	public void setR22_BOOK_VALUE_NET(BigDecimal r22_BOOK_VALUE_NET) {
		R22_BOOK_VALUE_NET = r22_BOOK_VALUE_NET;
	}
	public BigDecimal getR22_RW() {
		return R22_RW;
	}
	public void setR22_RW(BigDecimal r22_RW) {
		R22_RW = r22_RW;
	}
	public BigDecimal getR22_RISK_VALUE() {
		return R22_RISK_VALUE;
	}
	public void setR22_RISK_VALUE(BigDecimal r22_RISK_VALUE) {
		R22_RISK_VALUE = r22_RISK_VALUE;
	}
	public BigDecimal getR23_BOOK_VALUE() {
		return R23_BOOK_VALUE;
	}
	public void setR23_BOOK_VALUE(BigDecimal r23_BOOK_VALUE) {
		R23_BOOK_VALUE = r23_BOOK_VALUE;
	}
	public BigDecimal getR23_MARGINS() {
		return R23_MARGINS;
	}
	public void setR23_MARGINS(BigDecimal r23_MARGINS) {
		R23_MARGINS = r23_MARGINS;
	}
	public BigDecimal getR23_BOOK_VALUE_NET() {
		return R23_BOOK_VALUE_NET;
	}
	public void setR23_BOOK_VALUE_NET(BigDecimal r23_BOOK_VALUE_NET) {
		R23_BOOK_VALUE_NET = r23_BOOK_VALUE_NET;
	}
	public BigDecimal getR23_RW() {
		return R23_RW;
	}
	public void setR23_RW(BigDecimal r23_RW) {
		R23_RW = r23_RW;
	}
	public BigDecimal getR23_RISK_VALUE() {
		return R23_RISK_VALUE;
	}
	public void setR23_RISK_VALUE(BigDecimal r23_RISK_VALUE) {
		R23_RISK_VALUE = r23_RISK_VALUE;
	}
	public BigDecimal getR24_BOOK_VALUE() {
		return R24_BOOK_VALUE;
	}
	public void setR24_BOOK_VALUE(BigDecimal r24_BOOK_VALUE) {
		R24_BOOK_VALUE = r24_BOOK_VALUE;
	}
	public BigDecimal getR24_MARGINS() {
		return R24_MARGINS;
	}
	public void setR24_MARGINS(BigDecimal r24_MARGINS) {
		R24_MARGINS = r24_MARGINS;
	}
	public BigDecimal getR24_BOOK_VALUE_NET() {
		return R24_BOOK_VALUE_NET;
	}
	public void setR24_BOOK_VALUE_NET(BigDecimal r24_BOOK_VALUE_NET) {
		R24_BOOK_VALUE_NET = r24_BOOK_VALUE_NET;
	}
	public BigDecimal getR24_RW() {
		return R24_RW;
	}
	public void setR24_RW(BigDecimal r24_RW) {
		R24_RW = r24_RW;
	}
	public BigDecimal getR24_RISK_VALUE() {
		return R24_RISK_VALUE;
	}
	public void setR24_RISK_VALUE(BigDecimal r24_RISK_VALUE) {
		R24_RISK_VALUE = r24_RISK_VALUE;
	}
	public BigDecimal getR25_BOOK_VALUE() {
		return R25_BOOK_VALUE;
	}
	public void setR25_BOOK_VALUE(BigDecimal r25_BOOK_VALUE) {
		R25_BOOK_VALUE = r25_BOOK_VALUE;
	}
	public BigDecimal getR25_MARGINS() {
		return R25_MARGINS;
	}
	public void setR25_MARGINS(BigDecimal r25_MARGINS) {
		R25_MARGINS = r25_MARGINS;
	}
	public BigDecimal getR25_BOOK_VALUE_NET() {
		return R25_BOOK_VALUE_NET;
	}
	public void setR25_BOOK_VALUE_NET(BigDecimal r25_BOOK_VALUE_NET) {
		R25_BOOK_VALUE_NET = r25_BOOK_VALUE_NET;
	}
	public BigDecimal getR25_RW() {
		return R25_RW;
	}
	public void setR25_RW(BigDecimal r25_RW) {
		R25_RW = r25_RW;
	}
	public BigDecimal getR25_RISK_VALUE() {
		return R25_RISK_VALUE;
	}
	public void setR25_RISK_VALUE(BigDecimal r25_RISK_VALUE) {
		R25_RISK_VALUE = r25_RISK_VALUE;
	}
	public BigDecimal getR26_BOOK_VALUE() {
		return R26_BOOK_VALUE;
	}
	public void setR26_BOOK_VALUE(BigDecimal r26_BOOK_VALUE) {
		R26_BOOK_VALUE = r26_BOOK_VALUE;
	}
	public BigDecimal getR26_MARGINS() {
		return R26_MARGINS;
	}
	public void setR26_MARGINS(BigDecimal r26_MARGINS) {
		R26_MARGINS = r26_MARGINS;
	}
	public BigDecimal getR26_BOOK_VALUE_NET() {
		return R26_BOOK_VALUE_NET;
	}
	public void setR26_BOOK_VALUE_NET(BigDecimal r26_BOOK_VALUE_NET) {
		R26_BOOK_VALUE_NET = r26_BOOK_VALUE_NET;
	}
	public BigDecimal getR26_RW() {
		return R26_RW;
	}
	public void setR26_RW(BigDecimal r26_RW) {
		R26_RW = r26_RW;
	}
	public BigDecimal getR26_RISK_VALUE() {
		return R26_RISK_VALUE;
	}
	public void setR26_RISK_VALUE(BigDecimal r26_RISK_VALUE) {
		R26_RISK_VALUE = r26_RISK_VALUE;
	}
	public BigDecimal getR27_BOOK_VALUE() {
		return R27_BOOK_VALUE;
	}
	public void setR27_BOOK_VALUE(BigDecimal r27_BOOK_VALUE) {
		R27_BOOK_VALUE = r27_BOOK_VALUE;
	}
	public BigDecimal getR27_MARGINS() {
		return R27_MARGINS;
	}
	public void setR27_MARGINS(BigDecimal r27_MARGINS) {
		R27_MARGINS = r27_MARGINS;
	}
	public BigDecimal getR27_BOOK_VALUE_NET() {
		return R27_BOOK_VALUE_NET;
	}
	public void setR27_BOOK_VALUE_NET(BigDecimal r27_BOOK_VALUE_NET) {
		R27_BOOK_VALUE_NET = r27_BOOK_VALUE_NET;
	}
	public BigDecimal getR27_RW() {
		return R27_RW;
	}
	public void setR27_RW(BigDecimal r27_RW) {
		R27_RW = r27_RW;
	}
	public BigDecimal getR27_RISK_VALUE() {
		return R27_RISK_VALUE;
	}
	public void setR27_RISK_VALUE(BigDecimal r27_RISK_VALUE) {
		R27_RISK_VALUE = r27_RISK_VALUE;
	}
	public BigDecimal getR28_BOOK_VALUE() {
		return R28_BOOK_VALUE;
	}
	public void setR28_BOOK_VALUE(BigDecimal r28_BOOK_VALUE) {
		R28_BOOK_VALUE = r28_BOOK_VALUE;
	}
	public BigDecimal getR28_MARGINS() {
		return R28_MARGINS;
	}
	public void setR28_MARGINS(BigDecimal r28_MARGINS) {
		R28_MARGINS = r28_MARGINS;
	}
	public BigDecimal getR28_BOOK_VALUE_NET() {
		return R28_BOOK_VALUE_NET;
	}
	public void setR28_BOOK_VALUE_NET(BigDecimal r28_BOOK_VALUE_NET) {
		R28_BOOK_VALUE_NET = r28_BOOK_VALUE_NET;
	}
	public BigDecimal getR28_RW() {
		return R28_RW;
	}
	public void setR28_RW(BigDecimal r28_RW) {
		R28_RW = r28_RW;
	}
	public BigDecimal getR28_RISK_VALUE() {
		return R28_RISK_VALUE;
	}
	public void setR28_RISK_VALUE(BigDecimal r28_RISK_VALUE) {
		R28_RISK_VALUE = r28_RISK_VALUE;
	}
	public BigDecimal getR29_BOOK_VALUE() {
		return R29_BOOK_VALUE;
	}
	public void setR29_BOOK_VALUE(BigDecimal r29_BOOK_VALUE) {
		R29_BOOK_VALUE = r29_BOOK_VALUE;
	}
	public BigDecimal getR29_MARGINS() {
		return R29_MARGINS;
	}
	public void setR29_MARGINS(BigDecimal r29_MARGINS) {
		R29_MARGINS = r29_MARGINS;
	}
	public BigDecimal getR29_BOOK_VALUE_NET() {
		return R29_BOOK_VALUE_NET;
	}
	public void setR29_BOOK_VALUE_NET(BigDecimal r29_BOOK_VALUE_NET) {
		R29_BOOK_VALUE_NET = r29_BOOK_VALUE_NET;
	}
	public BigDecimal getR29_RW() {
		return R29_RW;
	}
	public void setR29_RW(BigDecimal r29_RW) {
		R29_RW = r29_RW;
	}
	public BigDecimal getR29_RISK_VALUE() {
		return R29_RISK_VALUE;
	}
	public void setR29_RISK_VALUE(BigDecimal r29_RISK_VALUE) {
		R29_RISK_VALUE = r29_RISK_VALUE;
	}
	public BigDecimal getR30_BOOK_VALUE() {
		return R30_BOOK_VALUE;
	}
	public void setR30_BOOK_VALUE(BigDecimal r30_BOOK_VALUE) {
		R30_BOOK_VALUE = r30_BOOK_VALUE;
	}
	public BigDecimal getR30_MARGINS() {
		return R30_MARGINS;
	}
	public void setR30_MARGINS(BigDecimal r30_MARGINS) {
		R30_MARGINS = r30_MARGINS;
	}
	public BigDecimal getR30_BOOK_VALUE_NET() {
		return R30_BOOK_VALUE_NET;
	}
	public void setR30_BOOK_VALUE_NET(BigDecimal r30_BOOK_VALUE_NET) {
		R30_BOOK_VALUE_NET = r30_BOOK_VALUE_NET;
	}
	public BigDecimal getR30_RW() {
		return R30_RW;
	}
	public void setR30_RW(BigDecimal r30_RW) {
		R30_RW = r30_RW;
	}
	public BigDecimal getR30_RISK_VALUE() {
		return R30_RISK_VALUE;
	}
	public void setR30_RISK_VALUE(BigDecimal r30_RISK_VALUE) {
		R30_RISK_VALUE = r30_RISK_VALUE;
	}
	public BigDecimal getR31_BOOK_VALUE() {
		return R31_BOOK_VALUE;
	}
	public void setR31_BOOK_VALUE(BigDecimal r31_BOOK_VALUE) {
		R31_BOOK_VALUE = r31_BOOK_VALUE;
	}
	public BigDecimal getR31_MARGINS() {
		return R31_MARGINS;
	}
	public void setR31_MARGINS(BigDecimal r31_MARGINS) {
		R31_MARGINS = r31_MARGINS;
	}
	public BigDecimal getR31_BOOK_VALUE_NET() {
		return R31_BOOK_VALUE_NET;
	}
	public void setR31_BOOK_VALUE_NET(BigDecimal r31_BOOK_VALUE_NET) {
		R31_BOOK_VALUE_NET = r31_BOOK_VALUE_NET;
	}
	public BigDecimal getR31_RW() {
		return R31_RW;
	}
	public void setR31_RW(BigDecimal r31_RW) {
		R31_RW = r31_RW;
	}
	public BigDecimal getR31_RISK_VALUE() {
		return R31_RISK_VALUE;
	}
	public void setR31_RISK_VALUE(BigDecimal r31_RISK_VALUE) {
		R31_RISK_VALUE = r31_RISK_VALUE;
	}
	public BigDecimal getR32_BOOK_VALUE() {
		return R32_BOOK_VALUE;
	}
	public void setR32_BOOK_VALUE(BigDecimal r32_BOOK_VALUE) {
		R32_BOOK_VALUE = r32_BOOK_VALUE;
	}
	public BigDecimal getR32_MARGINS() {
		return R32_MARGINS;
	}
	public void setR32_MARGINS(BigDecimal r32_MARGINS) {
		R32_MARGINS = r32_MARGINS;
	}
	public BigDecimal getR32_BOOK_VALUE_NET() {
		return R32_BOOK_VALUE_NET;
	}
	public void setR32_BOOK_VALUE_NET(BigDecimal r32_BOOK_VALUE_NET) {
		R32_BOOK_VALUE_NET = r32_BOOK_VALUE_NET;
	}
	public BigDecimal getR32_RW() {
		return R32_RW;
	}
	public void setR32_RW(BigDecimal r32_RW) {
		R32_RW = r32_RW;
	}
	public BigDecimal getR32_RISK_VALUE() {
		return R32_RISK_VALUE;
	}
	public void setR32_RISK_VALUE(BigDecimal r32_RISK_VALUE) {
		R32_RISK_VALUE = r32_RISK_VALUE;
	}
	public BigDecimal getR33_BOOK_VALUE() {
		return R33_BOOK_VALUE;
	}
	public void setR33_BOOK_VALUE(BigDecimal r33_BOOK_VALUE) {
		R33_BOOK_VALUE = r33_BOOK_VALUE;
	}
	public BigDecimal getR33_MARGINS() {
		return R33_MARGINS;
	}
	public void setR33_MARGINS(BigDecimal r33_MARGINS) {
		R33_MARGINS = r33_MARGINS;
	}
	public BigDecimal getR33_BOOK_VALUE_NET() {
		return R33_BOOK_VALUE_NET;
	}
	public void setR33_BOOK_VALUE_NET(BigDecimal r33_BOOK_VALUE_NET) {
		R33_BOOK_VALUE_NET = r33_BOOK_VALUE_NET;
	}
	public BigDecimal getR33_RW() {
		return R33_RW;
	}
	public void setR33_RW(BigDecimal r33_RW) {
		R33_RW = r33_RW;
	}
	public BigDecimal getR33_RISK_VALUE() {
		return R33_RISK_VALUE;
	}
	public void setR33_RISK_VALUE(BigDecimal r33_RISK_VALUE) {
		R33_RISK_VALUE = r33_RISK_VALUE;
	}
	public BigDecimal getR34_BOOK_VALUE() {
		return R34_BOOK_VALUE;
	}
	public void setR34_BOOK_VALUE(BigDecimal r34_BOOK_VALUE) {
		R34_BOOK_VALUE = r34_BOOK_VALUE;
	}
	public BigDecimal getR34_MARGINS() {
		return R34_MARGINS;
	}
	public void setR34_MARGINS(BigDecimal r34_MARGINS) {
		R34_MARGINS = r34_MARGINS;
	}
	public BigDecimal getR34_BOOK_VALUE_NET() {
		return R34_BOOK_VALUE_NET;
	}
	public void setR34_BOOK_VALUE_NET(BigDecimal r34_BOOK_VALUE_NET) {
		R34_BOOK_VALUE_NET = r34_BOOK_VALUE_NET;
	}
	public BigDecimal getR34_RW() {
		return R34_RW;
	}
	public void setR34_RW(BigDecimal r34_RW) {
		R34_RW = r34_RW;
	}
	public BigDecimal getR34_RISK_VALUE() {
		return R34_RISK_VALUE;
	}
	public void setR34_RISK_VALUE(BigDecimal r34_RISK_VALUE) {
		R34_RISK_VALUE = r34_RISK_VALUE;
	}
	public BigDecimal getR35_BOOK_VALUE() {
		return R35_BOOK_VALUE;
	}
	public void setR35_BOOK_VALUE(BigDecimal r35_BOOK_VALUE) {
		R35_BOOK_VALUE = r35_BOOK_VALUE;
	}
	public BigDecimal getR35_MARGINS() {
		return R35_MARGINS;
	}
	public void setR35_MARGINS(BigDecimal r35_MARGINS) {
		R35_MARGINS = r35_MARGINS;
	}
	public BigDecimal getR35_BOOK_VALUE_NET() {
		return R35_BOOK_VALUE_NET;
	}
	public void setR35_BOOK_VALUE_NET(BigDecimal r35_BOOK_VALUE_NET) {
		R35_BOOK_VALUE_NET = r35_BOOK_VALUE_NET;
	}
	public BigDecimal getR35_RW() {
		return R35_RW;
	}
	public void setR35_RW(BigDecimal r35_RW) {
		R35_RW = r35_RW;
	}
	public BigDecimal getR35_RISK_VALUE() {
		return R35_RISK_VALUE;
	}
	public void setR35_RISK_VALUE(BigDecimal r35_RISK_VALUE) {
		R35_RISK_VALUE = r35_RISK_VALUE;
	}
	public BigDecimal getR36_BOOK_VALUE() {
		return R36_BOOK_VALUE;
	}
	public void setR36_BOOK_VALUE(BigDecimal r36_BOOK_VALUE) {
		R36_BOOK_VALUE = r36_BOOK_VALUE;
	}
	public BigDecimal getR36_MARGINS() {
		return R36_MARGINS;
	}
	public void setR36_MARGINS(BigDecimal r36_MARGINS) {
		R36_MARGINS = r36_MARGINS;
	}
	public BigDecimal getR36_BOOK_VALUE_NET() {
		return R36_BOOK_VALUE_NET;
	}
	public void setR36_BOOK_VALUE_NET(BigDecimal r36_BOOK_VALUE_NET) {
		R36_BOOK_VALUE_NET = r36_BOOK_VALUE_NET;
	}
	public BigDecimal getR36_RW() {
		return R36_RW;
	}
	public void setR36_RW(BigDecimal r36_RW) {
		R36_RW = r36_RW;
	}
	public BigDecimal getR36_RISK_VALUE() {
		return R36_RISK_VALUE;
	}
	public void setR36_RISK_VALUE(BigDecimal r36_RISK_VALUE) {
		R36_RISK_VALUE = r36_RISK_VALUE;
	}
	public BigDecimal getR37_BOOK_VALUE() {
		return R37_BOOK_VALUE;
	}
	public void setR37_BOOK_VALUE(BigDecimal r37_BOOK_VALUE) {
		R37_BOOK_VALUE = r37_BOOK_VALUE;
	}
	public BigDecimal getR37_MARGINS() {
		return R37_MARGINS;
	}
	public void setR37_MARGINS(BigDecimal r37_MARGINS) {
		R37_MARGINS = r37_MARGINS;
	}
	public BigDecimal getR37_BOOK_VALUE_NET() {
		return R37_BOOK_VALUE_NET;
	}
	public void setR37_BOOK_VALUE_NET(BigDecimal r37_BOOK_VALUE_NET) {
		R37_BOOK_VALUE_NET = r37_BOOK_VALUE_NET;
	}
	public BigDecimal getR37_RW() {
		return R37_RW;
	}
	public void setR37_RW(BigDecimal r37_RW) {
		R37_RW = r37_RW;
	}
	public BigDecimal getR37_RISK_VALUE() {
		return R37_RISK_VALUE;
	}
	public void setR37_RISK_VALUE(BigDecimal r37_RISK_VALUE) {
		R37_RISK_VALUE = r37_RISK_VALUE;
	}
	public BigDecimal getR38_BOOK_VALUE() {
		return R38_BOOK_VALUE;
	}
	public void setR38_BOOK_VALUE(BigDecimal r38_BOOK_VALUE) {
		R38_BOOK_VALUE = r38_BOOK_VALUE;
	}
	public BigDecimal getR38_MARGINS() {
		return R38_MARGINS;
	}
	public void setR38_MARGINS(BigDecimal r38_MARGINS) {
		R38_MARGINS = r38_MARGINS;
	}
	public BigDecimal getR38_BOOK_VALUE_NET() {
		return R38_BOOK_VALUE_NET;
	}
	public void setR38_BOOK_VALUE_NET(BigDecimal r38_BOOK_VALUE_NET) {
		R38_BOOK_VALUE_NET = r38_BOOK_VALUE_NET;
	}
	public BigDecimal getR38_RW() {
		return R38_RW;
	}
	public void setR38_RW(BigDecimal r38_RW) {
		R38_RW = r38_RW;
	}
	public BigDecimal getR38_RISK_VALUE() {
		return R38_RISK_VALUE;
	}
	public void setR38_RISK_VALUE(BigDecimal r38_RISK_VALUE) {
		R38_RISK_VALUE = r38_RISK_VALUE;
	}
	public BigDecimal getR39_BOOK_VALUE() {
		return R39_BOOK_VALUE;
	}
	public void setR39_BOOK_VALUE(BigDecimal r39_BOOK_VALUE) {
		R39_BOOK_VALUE = r39_BOOK_VALUE;
	}
	public BigDecimal getR39_MARGINS() {
		return R39_MARGINS;
	}
	public void setR39_MARGINS(BigDecimal r39_MARGINS) {
		R39_MARGINS = r39_MARGINS;
	}
	public BigDecimal getR39_BOOK_VALUE_NET() {
		return R39_BOOK_VALUE_NET;
	}
	public void setR39_BOOK_VALUE_NET(BigDecimal r39_BOOK_VALUE_NET) {
		R39_BOOK_VALUE_NET = r39_BOOK_VALUE_NET;
	}
	public BigDecimal getR39_RW() {
		return R39_RW;
	}
	public void setR39_RW(BigDecimal r39_RW) {
		R39_RW = r39_RW;
	}
	public BigDecimal getR39_RISK_VALUE() {
		return R39_RISK_VALUE;
	}
	public void setR39_RISK_VALUE(BigDecimal r39_RISK_VALUE) {
		R39_RISK_VALUE = r39_RISK_VALUE;
	}
	public BigDecimal getR40_BOOK_VALUE() {
		return R40_BOOK_VALUE;
	}
	public void setR40_BOOK_VALUE(BigDecimal r40_BOOK_VALUE) {
		R40_BOOK_VALUE = r40_BOOK_VALUE;
	}
	public BigDecimal getR40_MARGINS() {
		return R40_MARGINS;
	}
	public void setR40_MARGINS(BigDecimal r40_MARGINS) {
		R40_MARGINS = r40_MARGINS;
	}
	public BigDecimal getR40_BOOK_VALUE_NET() {
		return R40_BOOK_VALUE_NET;
	}
	public void setR40_BOOK_VALUE_NET(BigDecimal r40_BOOK_VALUE_NET) {
		R40_BOOK_VALUE_NET = r40_BOOK_VALUE_NET;
	}
	public BigDecimal getR40_RW() {
		return R40_RW;
	}
	public void setR40_RW(BigDecimal r40_RW) {
		R40_RW = r40_RW;
	}
	public BigDecimal getR40_RISK_VALUE() {
		return R40_RISK_VALUE;
	}
	public void setR40_RISK_VALUE(BigDecimal r40_RISK_VALUE) {
		R40_RISK_VALUE = r40_RISK_VALUE;
	}
	public BigDecimal getR41_BOOK_VALUE() {
		return R41_BOOK_VALUE;
	}
	public void setR41_BOOK_VALUE(BigDecimal r41_BOOK_VALUE) {
		R41_BOOK_VALUE = r41_BOOK_VALUE;
	}
	public BigDecimal getR41_MARGINS() {
		return R41_MARGINS;
	}
	public void setR41_MARGINS(BigDecimal r41_MARGINS) {
		R41_MARGINS = r41_MARGINS;
	}
	public BigDecimal getR41_BOOK_VALUE_NET() {
		return R41_BOOK_VALUE_NET;
	}
	public void setR41_BOOK_VALUE_NET(BigDecimal r41_BOOK_VALUE_NET) {
		R41_BOOK_VALUE_NET = r41_BOOK_VALUE_NET;
	}
	public BigDecimal getR41_RW() {
		return R41_RW;
	}
	public void setR41_RW(BigDecimal r41_RW) {
		R41_RW = r41_RW;
	}
	public BigDecimal getR41_RISK_VALUE() {
		return R41_RISK_VALUE;
	}
	public void setR41_RISK_VALUE(BigDecimal r41_RISK_VALUE) {
		R41_RISK_VALUE = r41_RISK_VALUE;
	}
	public BigDecimal getR42_BOOK_VALUE() {
		return R42_BOOK_VALUE;
	}
	public void setR42_BOOK_VALUE(BigDecimal r42_BOOK_VALUE) {
		R42_BOOK_VALUE = r42_BOOK_VALUE;
	}
	public BigDecimal getR42_MARGINS() {
		return R42_MARGINS;
	}
	public void setR42_MARGINS(BigDecimal r42_MARGINS) {
		R42_MARGINS = r42_MARGINS;
	}
	public BigDecimal getR42_BOOK_VALUE_NET() {
		return R42_BOOK_VALUE_NET;
	}
	public void setR42_BOOK_VALUE_NET(BigDecimal r42_BOOK_VALUE_NET) {
		R42_BOOK_VALUE_NET = r42_BOOK_VALUE_NET;
	}
	public BigDecimal getR42_RW() {
		return R42_RW;
	}
	public void setR42_RW(BigDecimal r42_RW) {
		R42_RW = r42_RW;
	}
	public BigDecimal getR42_RISK_VALUE() {
		return R42_RISK_VALUE;
	}
	public void setR42_RISK_VALUE(BigDecimal r42_RISK_VALUE) {
		R42_RISK_VALUE = r42_RISK_VALUE;
	}
	public BigDecimal getR43_BOOK_VALUE() {
		return R43_BOOK_VALUE;
	}
	public void setR43_BOOK_VALUE(BigDecimal r43_BOOK_VALUE) {
		R43_BOOK_VALUE = r43_BOOK_VALUE;
	}
	public BigDecimal getR43_MARGINS() {
		return R43_MARGINS;
	}
	public void setR43_MARGINS(BigDecimal r43_MARGINS) {
		R43_MARGINS = r43_MARGINS;
	}
	public BigDecimal getR43_BOOK_VALUE_NET() {
		return R43_BOOK_VALUE_NET;
	}
	public void setR43_BOOK_VALUE_NET(BigDecimal r43_BOOK_VALUE_NET) {
		R43_BOOK_VALUE_NET = r43_BOOK_VALUE_NET;
	}
	public BigDecimal getR43_RW() {
		return R43_RW;
	}
	public void setR43_RW(BigDecimal r43_RW) {
		R43_RW = r43_RW;
	}
	public BigDecimal getR43_RISK_VALUE() {
		return R43_RISK_VALUE;
	}
	public void setR43_RISK_VALUE(BigDecimal r43_RISK_VALUE) {
		R43_RISK_VALUE = r43_RISK_VALUE;
	}
	public BigDecimal getR44_BOOK_VALUE() {
		return R44_BOOK_VALUE;
	}
	public void setR44_BOOK_VALUE(BigDecimal r44_BOOK_VALUE) {
		R44_BOOK_VALUE = r44_BOOK_VALUE;
	}
	public BigDecimal getR44_MARGINS() {
		return R44_MARGINS;
	}
	public void setR44_MARGINS(BigDecimal r44_MARGINS) {
		R44_MARGINS = r44_MARGINS;
	}
	public BigDecimal getR44_BOOK_VALUE_NET() {
		return R44_BOOK_VALUE_NET;
	}
	public void setR44_BOOK_VALUE_NET(BigDecimal r44_BOOK_VALUE_NET) {
		R44_BOOK_VALUE_NET = r44_BOOK_VALUE_NET;
	}
	public BigDecimal getR44_RW() {
		return R44_RW;
	}
	public void setR44_RW(BigDecimal r44_RW) {
		R44_RW = r44_RW;
	}
	public BigDecimal getR44_RISK_VALUE() {
		return R44_RISK_VALUE;
	}
	public void setR44_RISK_VALUE(BigDecimal r44_RISK_VALUE) {
		R44_RISK_VALUE = r44_RISK_VALUE;
	}
	public BigDecimal getR45_BOOK_VALUE() {
		return R45_BOOK_VALUE;
	}
	public void setR45_BOOK_VALUE(BigDecimal r45_BOOK_VALUE) {
		R45_BOOK_VALUE = r45_BOOK_VALUE;
	}
	public BigDecimal getR45_MARGINS() {
		return R45_MARGINS;
	}
	public void setR45_MARGINS(BigDecimal r45_MARGINS) {
		R45_MARGINS = r45_MARGINS;
	}
	public BigDecimal getR45_BOOK_VALUE_NET() {
		return R45_BOOK_VALUE_NET;
	}
	public void setR45_BOOK_VALUE_NET(BigDecimal r45_BOOK_VALUE_NET) {
		R45_BOOK_VALUE_NET = r45_BOOK_VALUE_NET;
	}
	public BigDecimal getR45_RW() {
		return R45_RW;
	}
	public void setR45_RW(BigDecimal r45_RW) {
		R45_RW = r45_RW;
	}
	public BigDecimal getR45_RISK_VALUE() {
		return R45_RISK_VALUE;
	}
	public void setR45_RISK_VALUE(BigDecimal r45_RISK_VALUE) {
		R45_RISK_VALUE = r45_RISK_VALUE;
	}
	public BigDecimal getR46_BOOK_VALUE() {
		return R46_BOOK_VALUE;
	}
	public void setR46_BOOK_VALUE(BigDecimal r46_BOOK_VALUE) {
		R46_BOOK_VALUE = r46_BOOK_VALUE;
	}
	public BigDecimal getR46_MARGINS() {
		return R46_MARGINS;
	}
	public void setR46_MARGINS(BigDecimal r46_MARGINS) {
		R46_MARGINS = r46_MARGINS;
	}
	public BigDecimal getR46_BOOK_VALUE_NET() {
		return R46_BOOK_VALUE_NET;
	}
	public void setR46_BOOK_VALUE_NET(BigDecimal r46_BOOK_VALUE_NET) {
		R46_BOOK_VALUE_NET = r46_BOOK_VALUE_NET;
	}
	public BigDecimal getR46_RW() {
		return R46_RW;
	}
	public void setR46_RW(BigDecimal r46_RW) {
		R46_RW = r46_RW;
	}
	public BigDecimal getR46_RISK_VALUE() {
		return R46_RISK_VALUE;
	}
	public void setR46_RISK_VALUE(BigDecimal r46_RISK_VALUE) {
		R46_RISK_VALUE = r46_RISK_VALUE;
	}
	public BigDecimal getR48_BOOK_VALUE() {
		return R48_BOOK_VALUE;
	}
	public void setR48_BOOK_VALUE(BigDecimal r48_BOOK_VALUE) {
		R48_BOOK_VALUE = r48_BOOK_VALUE;
	}
	public BigDecimal getR48_MARGINS() {
		return R48_MARGINS;
	}
	public void setR48_MARGINS(BigDecimal r48_MARGINS) {
		R48_MARGINS = r48_MARGINS;
	}
	public BigDecimal getR48_BOOK_VALUE_NET() {
		return R48_BOOK_VALUE_NET;
	}
	public void setR48_BOOK_VALUE_NET(BigDecimal r48_BOOK_VALUE_NET) {
		R48_BOOK_VALUE_NET = r48_BOOK_VALUE_NET;
	}
	public BigDecimal getR48_RW() {
		return R48_RW;
	}
	public void setR48_RW(BigDecimal r48_RW) {
		R48_RW = r48_RW;
	}
	public BigDecimal getR48_RISK_VALUE() {
		return R48_RISK_VALUE;
	}
	public void setR48_RISK_VALUE(BigDecimal r48_RISK_VALUE) {
		R48_RISK_VALUE = r48_RISK_VALUE;
	}
	public BigDecimal getR61_BOOK_VALUE() {
		return R61_BOOK_VALUE;
	}
	public void setR61_BOOK_VALUE(BigDecimal r61_BOOK_VALUE) {
		R61_BOOK_VALUE = r61_BOOK_VALUE;
	}
	public BigDecimal getR61_MARGINS() {
		return R61_MARGINS;
	}
	public void setR61_MARGINS(BigDecimal r61_MARGINS) {
		R61_MARGINS = r61_MARGINS;
	}
	public BigDecimal getR61_BOOK_VALUE_NET() {
		return R61_BOOK_VALUE_NET;
	}
	public void setR61_BOOK_VALUE_NET(BigDecimal r61_BOOK_VALUE_NET) {
		R61_BOOK_VALUE_NET = r61_BOOK_VALUE_NET;
	}
	public BigDecimal getR61_RW() {
		return R61_RW;
	}
	public void setR61_RW(BigDecimal r61_RW) {
		R61_RW = r61_RW;
	}
	public BigDecimal getR61_RISK_VALUE() {
		return R61_RISK_VALUE;
	}
	public void setR61_RISK_VALUE(BigDecimal r61_RISK_VALUE) {
		R61_RISK_VALUE = r61_RISK_VALUE;
	}
	public BigDecimal getR63_BOOK_VALUE() {
		return R63_BOOK_VALUE;
	}
	public void setR63_BOOK_VALUE(BigDecimal r63_BOOK_VALUE) {
		R63_BOOK_VALUE = r63_BOOK_VALUE;
	}
	public BigDecimal getR63_MARGINS() {
		return R63_MARGINS;
	}
	public void setR63_MARGINS(BigDecimal r63_MARGINS) {
		R63_MARGINS = r63_MARGINS;
	}
	public BigDecimal getR63_BOOK_VALUE_NET() {
		return R63_BOOK_VALUE_NET;
	}
	public void setR63_BOOK_VALUE_NET(BigDecimal r63_BOOK_VALUE_NET) {
		R63_BOOK_VALUE_NET = r63_BOOK_VALUE_NET;
	}
	public BigDecimal getR63_RW() {
		return R63_RW;
	}
	public void setR63_RW(BigDecimal r63_RW) {
		R63_RW = r63_RW;
	}
	public BigDecimal getR63_RISK_VALUE() {
		return R63_RISK_VALUE;
	}
	public void setR63_RISK_VALUE(BigDecimal r63_RISK_VALUE) {
		R63_RISK_VALUE = r63_RISK_VALUE;
	}
	public BigDecimal getR64_BOOK_VALUE() {
		return R64_BOOK_VALUE;
	}
	public void setR64_BOOK_VALUE(BigDecimal r64_BOOK_VALUE) {
		R64_BOOK_VALUE = r64_BOOK_VALUE;
	}
	public BigDecimal getR64_MARGINS() {
		return R64_MARGINS;
	}
	public void setR64_MARGINS(BigDecimal r64_MARGINS) {
		R64_MARGINS = r64_MARGINS;
	}
	public BigDecimal getR64_BOOK_VALUE_NET() {
		return R64_BOOK_VALUE_NET;
	}
	public void setR64_BOOK_VALUE_NET(BigDecimal r64_BOOK_VALUE_NET) {
		R64_BOOK_VALUE_NET = r64_BOOK_VALUE_NET;
	}
	public BigDecimal getR64_RW() {
		return R64_RW;
	}
	public void setR64_RW(BigDecimal r64_RW) {
		R64_RW = r64_RW;
	}
	public BigDecimal getR64_RISK_VALUE() {
		return R64_RISK_VALUE;
	}
	public void setR64_RISK_VALUE(BigDecimal r64_RISK_VALUE) {
		R64_RISK_VALUE = r64_RISK_VALUE;
	}
	public BigDecimal getR65_BOOK_VALUE() {
		return R65_BOOK_VALUE;
	}
	public void setR65_BOOK_VALUE(BigDecimal r65_BOOK_VALUE) {
		R65_BOOK_VALUE = r65_BOOK_VALUE;
	}
	public BigDecimal getR65_MARGINS() {
		return R65_MARGINS;
	}
	public void setR65_MARGINS(BigDecimal r65_MARGINS) {
		R65_MARGINS = r65_MARGINS;
	}
	public BigDecimal getR65_BOOK_VALUE_NET() {
		return R65_BOOK_VALUE_NET;
	}
	public void setR65_BOOK_VALUE_NET(BigDecimal r65_BOOK_VALUE_NET) {
		R65_BOOK_VALUE_NET = r65_BOOK_VALUE_NET;
	}
	public BigDecimal getR65_RW() {
		return R65_RW;
	}
	public void setR65_RW(BigDecimal r65_RW) {
		R65_RW = r65_RW;
	}
	public BigDecimal getR65_RISK_VALUE() {
		return R65_RISK_VALUE;
	}
	public void setR65_RISK_VALUE(BigDecimal r65_RISK_VALUE) {
		R65_RISK_VALUE = r65_RISK_VALUE;
	}
	public BigDecimal getR66_BOOK_VALUE() {
		return R66_BOOK_VALUE;
	}
	public void setR66_BOOK_VALUE(BigDecimal r66_BOOK_VALUE) {
		R66_BOOK_VALUE = r66_BOOK_VALUE;
	}
	public BigDecimal getR66_MARGINS() {
		return R66_MARGINS;
	}
	public void setR66_MARGINS(BigDecimal r66_MARGINS) {
		R66_MARGINS = r66_MARGINS;
	}
	public BigDecimal getR66_BOOK_VALUE_NET() {
		return R66_BOOK_VALUE_NET;
	}
	public void setR66_BOOK_VALUE_NET(BigDecimal r66_BOOK_VALUE_NET) {
		R66_BOOK_VALUE_NET = r66_BOOK_VALUE_NET;
	}
	public BigDecimal getR66_RW() {
		return R66_RW;
	}
	public void setR66_RW(BigDecimal r66_RW) {
		R66_RW = r66_RW;
	}
	public BigDecimal getR66_RISK_VALUE() {
		return R66_RISK_VALUE;
	}
	public void setR66_RISK_VALUE(BigDecimal r66_RISK_VALUE) {
		R66_RISK_VALUE = r66_RISK_VALUE;
	}
	public BigDecimal getR67_BOOK_VALUE() {
		return R67_BOOK_VALUE;
	}
	public void setR67_BOOK_VALUE(BigDecimal r67_BOOK_VALUE) {
		R67_BOOK_VALUE = r67_BOOK_VALUE;
	}
	public BigDecimal getR67_MARGINS() {
		return R67_MARGINS;
	}
	public void setR67_MARGINS(BigDecimal r67_MARGINS) {
		R67_MARGINS = r67_MARGINS;
	}
	public BigDecimal getR67_BOOK_VALUE_NET() {
		return R67_BOOK_VALUE_NET;
	}
	public void setR67_BOOK_VALUE_NET(BigDecimal r67_BOOK_VALUE_NET) {
		R67_BOOK_VALUE_NET = r67_BOOK_VALUE_NET;
	}
	public BigDecimal getR67_RW() {
		return R67_RW;
	}
	public void setR67_RW(BigDecimal r67_RW) {
		R67_RW = r67_RW;
	}
	public BigDecimal getR67_RISK_VALUE() {
		return R67_RISK_VALUE;
	}
	public void setR67_RISK_VALUE(BigDecimal r67_RISK_VALUE) {
		R67_RISK_VALUE = r67_RISK_VALUE;
	}
	public BigDecimal getR68_BOOK_VALUE() {
		return R68_BOOK_VALUE;
	}
	public void setR68_BOOK_VALUE(BigDecimal r68_BOOK_VALUE) {
		R68_BOOK_VALUE = r68_BOOK_VALUE;
	}
	public BigDecimal getR68_MARGINS() {
		return R68_MARGINS;
	}
	public void setR68_MARGINS(BigDecimal r68_MARGINS) {
		R68_MARGINS = r68_MARGINS;
	}
	public BigDecimal getR68_BOOK_VALUE_NET() {
		return R68_BOOK_VALUE_NET;
	}
	public void setR68_BOOK_VALUE_NET(BigDecimal r68_BOOK_VALUE_NET) {
		R68_BOOK_VALUE_NET = r68_BOOK_VALUE_NET;
	}
	public BigDecimal getR68_RW() {
		return R68_RW;
	}
	public void setR68_RW(BigDecimal r68_RW) {
		R68_RW = r68_RW;
	}
	public BigDecimal getR68_RISK_VALUE() {
		return R68_RISK_VALUE;
	}
	public void setR68_RISK_VALUE(BigDecimal r68_RISK_VALUE) {
		R68_RISK_VALUE = r68_RISK_VALUE;
	}
	public BigDecimal getR69_BOOK_VALUE() {
		return R69_BOOK_VALUE;
	}
	public void setR69_BOOK_VALUE(BigDecimal r69_BOOK_VALUE) {
		R69_BOOK_VALUE = r69_BOOK_VALUE;
	}
	public BigDecimal getR69_MARGINS() {
		return R69_MARGINS;
	}
	public void setR69_MARGINS(BigDecimal r69_MARGINS) {
		R69_MARGINS = r69_MARGINS;
	}
	public BigDecimal getR69_BOOK_VALUE_NET() {
		return R69_BOOK_VALUE_NET;
	}
	public void setR69_BOOK_VALUE_NET(BigDecimal r69_BOOK_VALUE_NET) {
		R69_BOOK_VALUE_NET = r69_BOOK_VALUE_NET;
	}
	public BigDecimal getR69_RW() {
		return R69_RW;
	}
	public void setR69_RW(BigDecimal r69_RW) {
		R69_RW = r69_RW;
	}
	public BigDecimal getR69_RISK_VALUE() {
		return R69_RISK_VALUE;
	}
	public void setR69_RISK_VALUE(BigDecimal r69_RISK_VALUE) {
		R69_RISK_VALUE = r69_RISK_VALUE;
	}
	public BigDecimal getR70_BOOK_VALUE() {
		return R70_BOOK_VALUE;
	}
	public void setR70_BOOK_VALUE(BigDecimal r70_BOOK_VALUE) {
		R70_BOOK_VALUE = r70_BOOK_VALUE;
	}
	public BigDecimal getR70_MARGINS() {
		return R70_MARGINS;
	}
	public void setR70_MARGINS(BigDecimal r70_MARGINS) {
		R70_MARGINS = r70_MARGINS;
	}
	public BigDecimal getR70_BOOK_VALUE_NET() {
		return R70_BOOK_VALUE_NET;
	}
	public void setR70_BOOK_VALUE_NET(BigDecimal r70_BOOK_VALUE_NET) {
		R70_BOOK_VALUE_NET = r70_BOOK_VALUE_NET;
	}
	public BigDecimal getR70_RW() {
		return R70_RW;
	}
	public void setR70_RW(BigDecimal r70_RW) {
		R70_RW = r70_RW;
	}
	public BigDecimal getR70_RISK_VALUE() {
		return R70_RISK_VALUE;
	}
	public void setR70_RISK_VALUE(BigDecimal r70_RISK_VALUE) {
		R70_RISK_VALUE = r70_RISK_VALUE;
	}
	public BigDecimal getR71_BOOK_VALUE() {
		return R71_BOOK_VALUE;
	}
	public void setR71_BOOK_VALUE(BigDecimal r71_BOOK_VALUE) {
		R71_BOOK_VALUE = r71_BOOK_VALUE;
	}
	public BigDecimal getR71_MARGINS() {
		return R71_MARGINS;
	}
	public void setR71_MARGINS(BigDecimal r71_MARGINS) {
		R71_MARGINS = r71_MARGINS;
	}
	public BigDecimal getR71_BOOK_VALUE_NET() {
		return R71_BOOK_VALUE_NET;
	}
	public void setR71_BOOK_VALUE_NET(BigDecimal r71_BOOK_VALUE_NET) {
		R71_BOOK_VALUE_NET = r71_BOOK_VALUE_NET;
	}
	public BigDecimal getR71_RW() {
		return R71_RW;
	}
	public void setR71_RW(BigDecimal r71_RW) {
		R71_RW = r71_RW;
	}
	public BigDecimal getR71_RISK_VALUE() {
		return R71_RISK_VALUE;
	}
	public void setR71_RISK_VALUE(BigDecimal r71_RISK_VALUE) {
		R71_RISK_VALUE = r71_RISK_VALUE;
	}
	public BigDecimal getR72_BOOK_VALUE() {
		return R72_BOOK_VALUE;
	}
	public void setR72_BOOK_VALUE(BigDecimal r72_BOOK_VALUE) {
		R72_BOOK_VALUE = r72_BOOK_VALUE;
	}
	public BigDecimal getR72_MARGINS() {
		return R72_MARGINS;
	}
	public void setR72_MARGINS(BigDecimal r72_MARGINS) {
		R72_MARGINS = r72_MARGINS;
	}
	public BigDecimal getR72_BOOK_VALUE_NET() {
		return R72_BOOK_VALUE_NET;
	}
	public void setR72_BOOK_VALUE_NET(BigDecimal r72_BOOK_VALUE_NET) {
		R72_BOOK_VALUE_NET = r72_BOOK_VALUE_NET;
	}
	public BigDecimal getR72_RW() {
		return R72_RW;
	}
	public void setR72_RW(BigDecimal r72_RW) {
		R72_RW = r72_RW;
	}
	public BigDecimal getR72_RISK_VALUE() {
		return R72_RISK_VALUE;
	}
	public void setR72_RISK_VALUE(BigDecimal r72_RISK_VALUE) {
		R72_RISK_VALUE = r72_RISK_VALUE;
	}
	public BigDecimal getR73_BOOK_VALUE() {
		return R73_BOOK_VALUE;
	}
	public void setR73_BOOK_VALUE(BigDecimal r73_BOOK_VALUE) {
		R73_BOOK_VALUE = r73_BOOK_VALUE;
	}
	public BigDecimal getR73_MARGINS() {
		return R73_MARGINS;
	}
	public void setR73_MARGINS(BigDecimal r73_MARGINS) {
		R73_MARGINS = r73_MARGINS;
	}
	public BigDecimal getR73_BOOK_VALUE_NET() {
		return R73_BOOK_VALUE_NET;
	}
	public void setR73_BOOK_VALUE_NET(BigDecimal r73_BOOK_VALUE_NET) {
		R73_BOOK_VALUE_NET = r73_BOOK_VALUE_NET;
	}
	public BigDecimal getR73_RW() {
		return R73_RW;
	}
	public void setR73_RW(BigDecimal r73_RW) {
		R73_RW = r73_RW;
	}
	public BigDecimal getR73_RISK_VALUE() {
		return R73_RISK_VALUE;
	}
	public void setR73_RISK_VALUE(BigDecimal r73_RISK_VALUE) {
		R73_RISK_VALUE = r73_RISK_VALUE;
	}
	public BigDecimal getR74_BOOK_VALUE() {
		return R74_BOOK_VALUE;
	}
	public void setR74_BOOK_VALUE(BigDecimal r74_BOOK_VALUE) {
		R74_BOOK_VALUE = r74_BOOK_VALUE;
	}
	public BigDecimal getR74_MARGINS() {
		return R74_MARGINS;
	}
	public void setR74_MARGINS(BigDecimal r74_MARGINS) {
		R74_MARGINS = r74_MARGINS;
	}
	public BigDecimal getR74_BOOK_VALUE_NET() {
		return R74_BOOK_VALUE_NET;
	}
	public void setR74_BOOK_VALUE_NET(BigDecimal r74_BOOK_VALUE_NET) {
		R74_BOOK_VALUE_NET = r74_BOOK_VALUE_NET;
	}
	public BigDecimal getR74_RW() {
		return R74_RW;
	}
	public void setR74_RW(BigDecimal r74_RW) {
		R74_RW = r74_RW;
	}
	public BigDecimal getR74_RISK_VALUE() {
		return R74_RISK_VALUE;
	}
	public void setR74_RISK_VALUE(BigDecimal r74_RISK_VALUE) {
		R74_RISK_VALUE = r74_RISK_VALUE;
	}
	public BigDecimal getR75_BOOK_VALUE() {
		return R75_BOOK_VALUE;
	}
	public void setR75_BOOK_VALUE(BigDecimal r75_BOOK_VALUE) {
		R75_BOOK_VALUE = r75_BOOK_VALUE;
	}
	public BigDecimal getR75_MARGINS() {
		return R75_MARGINS;
	}
	public void setR75_MARGINS(BigDecimal r75_MARGINS) {
		R75_MARGINS = r75_MARGINS;
	}
	public BigDecimal getR75_BOOK_VALUE_NET() {
		return R75_BOOK_VALUE_NET;
	}
	public void setR75_BOOK_VALUE_NET(BigDecimal r75_BOOK_VALUE_NET) {
		R75_BOOK_VALUE_NET = r75_BOOK_VALUE_NET;
	}
	public BigDecimal getR75_RW() {
		return R75_RW;
	}
	public void setR75_RW(BigDecimal r75_RW) {
		R75_RW = r75_RW;
	}
	public BigDecimal getR75_RISK_VALUE() {
		return R75_RISK_VALUE;
	}
	public void setR75_RISK_VALUE(BigDecimal r75_RISK_VALUE) {
		R75_RISK_VALUE = r75_RISK_VALUE;
	}
	public BigDecimal getR76_BOOK_VALUE() {
		return R76_BOOK_VALUE;
	}
	public void setR76_BOOK_VALUE(BigDecimal r76_BOOK_VALUE) {
		R76_BOOK_VALUE = r76_BOOK_VALUE;
	}
	public BigDecimal getR76_MARGINS() {
		return R76_MARGINS;
	}
	public void setR76_MARGINS(BigDecimal r76_MARGINS) {
		R76_MARGINS = r76_MARGINS;
	}
	public BigDecimal getR76_BOOK_VALUE_NET() {
		return R76_BOOK_VALUE_NET;
	}
	public void setR76_BOOK_VALUE_NET(BigDecimal r76_BOOK_VALUE_NET) {
		R76_BOOK_VALUE_NET = r76_BOOK_VALUE_NET;
	}
	public BigDecimal getR76_RW() {
		return R76_RW;
	}
	public void setR76_RW(BigDecimal r76_RW) {
		R76_RW = r76_RW;
	}
	public BigDecimal getR76_RISK_VALUE() {
		return R76_RISK_VALUE;
	}
	public void setR76_RISK_VALUE(BigDecimal r76_RISK_VALUE) {
		R76_RISK_VALUE = r76_RISK_VALUE;
	}
	public BigDecimal getR77_BOOK_VALUE() {
		return R77_BOOK_VALUE;
	}
	public void setR77_BOOK_VALUE(BigDecimal r77_BOOK_VALUE) {
		R77_BOOK_VALUE = r77_BOOK_VALUE;
	}
	public BigDecimal getR77_MARGINS() {
		return R77_MARGINS;
	}
	public void setR77_MARGINS(BigDecimal r77_MARGINS) {
		R77_MARGINS = r77_MARGINS;
	}
	public BigDecimal getR77_BOOK_VALUE_NET() {
		return R77_BOOK_VALUE_NET;
	}
	public void setR77_BOOK_VALUE_NET(BigDecimal r77_BOOK_VALUE_NET) {
		R77_BOOK_VALUE_NET = r77_BOOK_VALUE_NET;
	}
	public BigDecimal getR77_RW() {
		return R77_RW;
	}
	public void setR77_RW(BigDecimal r77_RW) {
		R77_RW = r77_RW;
	}
	public BigDecimal getR77_RISK_VALUE() {
		return R77_RISK_VALUE;
	}
	public void setR77_RISK_VALUE(BigDecimal r77_RISK_VALUE) {
		R77_RISK_VALUE = r77_RISK_VALUE;
	}
	public BigDecimal getR78_BOOK_VALUE() {
		return R78_BOOK_VALUE;
	}
	public void setR78_BOOK_VALUE(BigDecimal r78_BOOK_VALUE) {
		R78_BOOK_VALUE = r78_BOOK_VALUE;
	}
	public BigDecimal getR78_MARGINS() {
		return R78_MARGINS;
	}
	public void setR78_MARGINS(BigDecimal r78_MARGINS) {
		R78_MARGINS = r78_MARGINS;
	}
	public BigDecimal getR78_BOOK_VALUE_NET() {
		return R78_BOOK_VALUE_NET;
	}
	public void setR78_BOOK_VALUE_NET(BigDecimal r78_BOOK_VALUE_NET) {
		R78_BOOK_VALUE_NET = r78_BOOK_VALUE_NET;
	}
	public BigDecimal getR78_RW() {
		return R78_RW;
	}
	public void setR78_RW(BigDecimal r78_RW) {
		R78_RW = r78_RW;
	}
	public BigDecimal getR78_RISK_VALUE() {
		return R78_RISK_VALUE;
	}
	public void setR78_RISK_VALUE(BigDecimal r78_RISK_VALUE) {
		R78_RISK_VALUE = r78_RISK_VALUE;
	}
	public BigDecimal getR79_BOOK_VALUE() {
		return R79_BOOK_VALUE;
	}
	public void setR79_BOOK_VALUE(BigDecimal r79_BOOK_VALUE) {
		R79_BOOK_VALUE = r79_BOOK_VALUE;
	}
	public BigDecimal getR79_MARGINS() {
		return R79_MARGINS;
	}
	public void setR79_MARGINS(BigDecimal r79_MARGINS) {
		R79_MARGINS = r79_MARGINS;
	}
	public BigDecimal getR79_BOOK_VALUE_NET() {
		return R79_BOOK_VALUE_NET;
	}
	public void setR79_BOOK_VALUE_NET(BigDecimal r79_BOOK_VALUE_NET) {
		R79_BOOK_VALUE_NET = r79_BOOK_VALUE_NET;
	}
	public BigDecimal getR79_RW() {
		return R79_RW;
	}
	public void setR79_RW(BigDecimal r79_RW) {
		R79_RW = r79_RW;
	}
	public BigDecimal getR79_RISK_VALUE() {
		return R79_RISK_VALUE;
	}
	public void setR79_RISK_VALUE(BigDecimal r79_RISK_VALUE) {
		R79_RISK_VALUE = r79_RISK_VALUE;
	}
	public BigDecimal getR80_BOOK_VALUE() {
		return R80_BOOK_VALUE;
	}
	public void setR80_BOOK_VALUE(BigDecimal r80_BOOK_VALUE) {
		R80_BOOK_VALUE = r80_BOOK_VALUE;
	}
	public BigDecimal getR80_MARGINS() {
		return R80_MARGINS;
	}
	public void setR80_MARGINS(BigDecimal r80_MARGINS) {
		R80_MARGINS = r80_MARGINS;
	}
	public BigDecimal getR80_BOOK_VALUE_NET() {
		return R80_BOOK_VALUE_NET;
	}
	public void setR80_BOOK_VALUE_NET(BigDecimal r80_BOOK_VALUE_NET) {
		R80_BOOK_VALUE_NET = r80_BOOK_VALUE_NET;
	}
	public BigDecimal getR80_RW() {
		return R80_RW;
	}
	public void setR80_RW(BigDecimal r80_RW) {
		R80_RW = r80_RW;
	}
	public BigDecimal getR80_RISK_VALUE() {
		return R80_RISK_VALUE;
	}
	public void setR80_RISK_VALUE(BigDecimal r80_RISK_VALUE) {
		R80_RISK_VALUE = r80_RISK_VALUE;
	}
	public BigDecimal getR81_BOOK_VALUE() {
		return R81_BOOK_VALUE;
	}
	public void setR81_BOOK_VALUE(BigDecimal r81_BOOK_VALUE) {
		R81_BOOK_VALUE = r81_BOOK_VALUE;
	}
	public BigDecimal getR81_MARGINS() {
		return R81_MARGINS;
	}
	public void setR81_MARGINS(BigDecimal r81_MARGINS) {
		R81_MARGINS = r81_MARGINS;
	}
	public BigDecimal getR81_BOOK_VALUE_NET() {
		return R81_BOOK_VALUE_NET;
	}
	public void setR81_BOOK_VALUE_NET(BigDecimal r81_BOOK_VALUE_NET) {
		R81_BOOK_VALUE_NET = r81_BOOK_VALUE_NET;
	}
	public BigDecimal getR81_RW() {
		return R81_RW;
	}
	public void setR81_RW(BigDecimal r81_RW) {
		R81_RW = r81_RW;
	}
	public BigDecimal getR81_RISK_VALUE() {
		return R81_RISK_VALUE;
	}
	public void setR81_RISK_VALUE(BigDecimal r81_RISK_VALUE) {
		R81_RISK_VALUE = r81_RISK_VALUE;
	}
	public BigDecimal getR82_BOOK_VALUE() {
		return R82_BOOK_VALUE;
	}
	public void setR82_BOOK_VALUE(BigDecimal r82_BOOK_VALUE) {
		R82_BOOK_VALUE = r82_BOOK_VALUE;
	}
	public BigDecimal getR82_MARGINS() {
		return R82_MARGINS;
	}
	public void setR82_MARGINS(BigDecimal r82_MARGINS) {
		R82_MARGINS = r82_MARGINS;
	}
	public BigDecimal getR82_BOOK_VALUE_NET() {
		return R82_BOOK_VALUE_NET;
	}
	public void setR82_BOOK_VALUE_NET(BigDecimal r82_BOOK_VALUE_NET) {
		R82_BOOK_VALUE_NET = r82_BOOK_VALUE_NET;
	}
	public BigDecimal getR82_RW() {
		return R82_RW;
	}
	public void setR82_RW(BigDecimal r82_RW) {
		R82_RW = r82_RW;
	}
	public BigDecimal getR82_RISK_VALUE() {
		return R82_RISK_VALUE;
	}
	public void setR82_RISK_VALUE(BigDecimal r82_RISK_VALUE) {
		R82_RISK_VALUE = r82_RISK_VALUE;
	}
	public BigDecimal getR97_BOOK_VALUE() {
		return R97_BOOK_VALUE;
	}
	public void setR97_BOOK_VALUE(BigDecimal r97_BOOK_VALUE) {
		R97_BOOK_VALUE = r97_BOOK_VALUE;
	}
	public BigDecimal getR97_MARGINS() {
		return R97_MARGINS;
	}
	public void setR97_MARGINS(BigDecimal r97_MARGINS) {
		R97_MARGINS = r97_MARGINS;
	}
	public BigDecimal getR97_BOOK_VALUE_NET() {
		return R97_BOOK_VALUE_NET;
	}
	public void setR97_BOOK_VALUE_NET(BigDecimal r97_BOOK_VALUE_NET) {
		R97_BOOK_VALUE_NET = r97_BOOK_VALUE_NET;
	}
	public BigDecimal getR97_RW() {
		return R97_RW;
	}
	public void setR97_RW(BigDecimal r97_RW) {
		R97_RW = r97_RW;
	}
	public BigDecimal getR97_RISK_VALUE() {
		return R97_RISK_VALUE;
	}
	public void setR97_RISK_VALUE(BigDecimal r97_RISK_VALUE) {
		R97_RISK_VALUE = r97_RISK_VALUE;
	}
	public BigDecimal getR98_BOOK_VALUE() {
		return R98_BOOK_VALUE;
	}
	public void setR98_BOOK_VALUE(BigDecimal r98_BOOK_VALUE) {
		R98_BOOK_VALUE = r98_BOOK_VALUE;
	}
	public BigDecimal getR98_MARGINS() {
		return R98_MARGINS;
	}
	public void setR98_MARGINS(BigDecimal r98_MARGINS) {
		R98_MARGINS = r98_MARGINS;
	}
	public BigDecimal getR98_BOOK_VALUE_NET() {
		return R98_BOOK_VALUE_NET;
	}
	public void setR98_BOOK_VALUE_NET(BigDecimal r98_BOOK_VALUE_NET) {
		R98_BOOK_VALUE_NET = r98_BOOK_VALUE_NET;
	}
	public BigDecimal getR98_RW() {
		return R98_RW;
	}
	public void setR98_RW(BigDecimal r98_RW) {
		R98_RW = r98_RW;
	}
	public BigDecimal getR98_RISK_VALUE() {
		return R98_RISK_VALUE;
	}
	public void setR98_RISK_VALUE(BigDecimal r98_RISK_VALUE) {
		R98_RISK_VALUE = r98_RISK_VALUE;
	}
	public BigDecimal getR99_BOOK_VALUE() {
		return R99_BOOK_VALUE;
	}
	public void setR99_BOOK_VALUE(BigDecimal r99_BOOK_VALUE) {
		R99_BOOK_VALUE = r99_BOOK_VALUE;
	}
	public BigDecimal getR99_MARGINS() {
		return R99_MARGINS;
	}
	public void setR99_MARGINS(BigDecimal r99_MARGINS) {
		R99_MARGINS = r99_MARGINS;
	}
	public BigDecimal getR99_BOOK_VALUE_NET() {
		return R99_BOOK_VALUE_NET;
	}
	public void setR99_BOOK_VALUE_NET(BigDecimal r99_BOOK_VALUE_NET) {
		R99_BOOK_VALUE_NET = r99_BOOK_VALUE_NET;
	}
	public BigDecimal getR99_RW() {
		return R99_RW;
	}
	public void setR99_RW(BigDecimal r99_RW) {
		R99_RW = r99_RW;
	}
	public BigDecimal getR99_RISK_VALUE() {
		return R99_RISK_VALUE;
	}
	public void setR99_RISK_VALUE(BigDecimal r99_RISK_VALUE) {
		R99_RISK_VALUE = r99_RISK_VALUE;
	}
	public BigDecimal getR100_BOOK_VALUE() {
		return R100_BOOK_VALUE;
	}
	public void setR100_BOOK_VALUE(BigDecimal r100_BOOK_VALUE) {
		R100_BOOK_VALUE = r100_BOOK_VALUE;
	}
	public BigDecimal getR100_MARGINS() {
		return R100_MARGINS;
	}
	public void setR100_MARGINS(BigDecimal r100_MARGINS) {
		R100_MARGINS = r100_MARGINS;
	}
	public BigDecimal getR100_BOOK_VALUE_NET() {
		return R100_BOOK_VALUE_NET;
	}
	public void setR100_BOOK_VALUE_NET(BigDecimal r100_BOOK_VALUE_NET) {
		R100_BOOK_VALUE_NET = r100_BOOK_VALUE_NET;
	}
	public BigDecimal getR100_RW() {
		return R100_RW;
	}
	public void setR100_RW(BigDecimal r100_RW) {
		R100_RW = r100_RW;
	}
	public BigDecimal getR100_RISK_VALUE() {
		return R100_RISK_VALUE;
	}
	public void setR100_RISK_VALUE(BigDecimal r100_RISK_VALUE) {
		R100_RISK_VALUE = r100_RISK_VALUE;
	}
	public BigDecimal getR101_BOOK_VALUE() {
		return R101_BOOK_VALUE;
	}
	public void setR101_BOOK_VALUE(BigDecimal r101_BOOK_VALUE) {
		R101_BOOK_VALUE = r101_BOOK_VALUE;
	}
	public BigDecimal getR101_MARGINS() {
		return R101_MARGINS;
	}
	public void setR101_MARGINS(BigDecimal r101_MARGINS) {
		R101_MARGINS = r101_MARGINS;
	}
	public BigDecimal getR101_BOOK_VALUE_NET() {
		return R101_BOOK_VALUE_NET;
	}
	public void setR101_BOOK_VALUE_NET(BigDecimal r101_BOOK_VALUE_NET) {
		R101_BOOK_VALUE_NET = r101_BOOK_VALUE_NET;
	}
	public BigDecimal getR101_RW() {
		return R101_RW;
	}
	public void setR101_RW(BigDecimal r101_RW) {
		R101_RW = r101_RW;
	}
	public BigDecimal getR101_RISK_VALUE() {
		return R101_RISK_VALUE;
	}
	public void setR101_RISK_VALUE(BigDecimal r101_RISK_VALUE) {
		R101_RISK_VALUE = r101_RISK_VALUE;
	}
	public BigDecimal getR102_BOOK_VALUE() {
		return R102_BOOK_VALUE;
	}
	public void setR102_BOOK_VALUE(BigDecimal r102_BOOK_VALUE) {
		R102_BOOK_VALUE = r102_BOOK_VALUE;
	}
	public BigDecimal getR102_MARGINS() {
		return R102_MARGINS;
	}
	public void setR102_MARGINS(BigDecimal r102_MARGINS) {
		R102_MARGINS = r102_MARGINS;
	}
	public BigDecimal getR102_BOOK_VALUE_NET() {
		return R102_BOOK_VALUE_NET;
	}
	public void setR102_BOOK_VALUE_NET(BigDecimal r102_BOOK_VALUE_NET) {
		R102_BOOK_VALUE_NET = r102_BOOK_VALUE_NET;
	}
	public BigDecimal getR102_RW() {
		return R102_RW;
	}
	public void setR102_RW(BigDecimal r102_RW) {
		R102_RW = r102_RW;
	}
	public BigDecimal getR102_RISK_VALUE() {
		return R102_RISK_VALUE;
	}
	public void setR102_RISK_VALUE(BigDecimal r102_RISK_VALUE) {
		R102_RISK_VALUE = r102_RISK_VALUE;
	}
	public BigDecimal getR103_BOOK_VALUE() {
		return R103_BOOK_VALUE;
	}
	public void setR103_BOOK_VALUE(BigDecimal r103_BOOK_VALUE) {
		R103_BOOK_VALUE = r103_BOOK_VALUE;
	}
	public BigDecimal getR103_MARGINS() {
		return R103_MARGINS;
	}
	public void setR103_MARGINS(BigDecimal r103_MARGINS) {
		R103_MARGINS = r103_MARGINS;
	}
	public BigDecimal getR103_BOOK_VALUE_NET() {
		return R103_BOOK_VALUE_NET;
	}
	public void setR103_BOOK_VALUE_NET(BigDecimal r103_BOOK_VALUE_NET) {
		R103_BOOK_VALUE_NET = r103_BOOK_VALUE_NET;
	}
	public BigDecimal getR103_RW() {
		return R103_RW;
	}
	public void setR103_RW(BigDecimal r103_RW) {
		R103_RW = r103_RW;
	}
	public BigDecimal getR103_RISK_VALUE() {
		return R103_RISK_VALUE;
	}
	public void setR103_RISK_VALUE(BigDecimal r103_RISK_VALUE) {
		R103_RISK_VALUE = r103_RISK_VALUE;
	}
	public BigDecimal getR104_BOOK_VALUE() {
		return R104_BOOK_VALUE;
	}
	public void setR104_BOOK_VALUE(BigDecimal r104_BOOK_VALUE) {
		R104_BOOK_VALUE = r104_BOOK_VALUE;
	}
	public BigDecimal getR104_MARGINS() {
		return R104_MARGINS;
	}
	public void setR104_MARGINS(BigDecimal r104_MARGINS) {
		R104_MARGINS = r104_MARGINS;
	}
	public BigDecimal getR104_BOOK_VALUE_NET() {
		return R104_BOOK_VALUE_NET;
	}
	public void setR104_BOOK_VALUE_NET(BigDecimal r104_BOOK_VALUE_NET) {
		R104_BOOK_VALUE_NET = r104_BOOK_VALUE_NET;
	}
	public BigDecimal getR104_RW() {
		return R104_RW;
	}
	public void setR104_RW(BigDecimal r104_RW) {
		R104_RW = r104_RW;
	}
	public BigDecimal getR104_RISK_VALUE() {
		return R104_RISK_VALUE;
	}
	public void setR104_RISK_VALUE(BigDecimal r104_RISK_VALUE) {
		R104_RISK_VALUE = r104_RISK_VALUE;
	}
	public BigDecimal getR105_BOOK_VALUE() {
		return R105_BOOK_VALUE;
	}
	public void setR105_BOOK_VALUE(BigDecimal r105_BOOK_VALUE) {
		R105_BOOK_VALUE = r105_BOOK_VALUE;
	}
	public BigDecimal getR105_MARGINS() {
		return R105_MARGINS;
	}
	public void setR105_MARGINS(BigDecimal r105_MARGINS) {
		R105_MARGINS = r105_MARGINS;
	}
	public BigDecimal getR105_BOOK_VALUE_NET() {
		return R105_BOOK_VALUE_NET;
	}
	public void setR105_BOOK_VALUE_NET(BigDecimal r105_BOOK_VALUE_NET) {
		R105_BOOK_VALUE_NET = r105_BOOK_VALUE_NET;
	}
	public BigDecimal getR105_RW() {
		return R105_RW;
	}
	public void setR105_RW(BigDecimal r105_RW) {
		R105_RW = r105_RW;
	}
	public BigDecimal getR105_RISK_VALUE() {
		return R105_RISK_VALUE;
	}
	public void setR105_RISK_VALUE(BigDecimal r105_RISK_VALUE) {
		R105_RISK_VALUE = r105_RISK_VALUE;
	}
	public BigDecimal getR106_BOOK_VALUE() {
		return R106_BOOK_VALUE;
	}
	public void setR106_BOOK_VALUE(BigDecimal r106_BOOK_VALUE) {
		R106_BOOK_VALUE = r106_BOOK_VALUE;
	}
	public BigDecimal getR106_MARGINS() {
		return R106_MARGINS;
	}
	public void setR106_MARGINS(BigDecimal r106_MARGINS) {
		R106_MARGINS = r106_MARGINS;
	}
	public BigDecimal getR106_BOOK_VALUE_NET() {
		return R106_BOOK_VALUE_NET;
	}
	public void setR106_BOOK_VALUE_NET(BigDecimal r106_BOOK_VALUE_NET) {
		R106_BOOK_VALUE_NET = r106_BOOK_VALUE_NET;
	}
	public BigDecimal getR106_RW() {
		return R106_RW;
	}
	public void setR106_RW(BigDecimal r106_RW) {
		R106_RW = r106_RW;
	}
	public BigDecimal getR106_RISK_VALUE() {
		return R106_RISK_VALUE;
	}
	public void setR106_RISK_VALUE(BigDecimal r106_RISK_VALUE) {
		R106_RISK_VALUE = r106_RISK_VALUE;
	}
	public BigDecimal getR107_BOOK_VALUE() {
		return R107_BOOK_VALUE;
	}
	public void setR107_BOOK_VALUE(BigDecimal r107_BOOK_VALUE) {
		R107_BOOK_VALUE = r107_BOOK_VALUE;
	}
	public BigDecimal getR107_MARGINS() {
		return R107_MARGINS;
	}
	public void setR107_MARGINS(BigDecimal r107_MARGINS) {
		R107_MARGINS = r107_MARGINS;
	}
	public BigDecimal getR107_BOOK_VALUE_NET() {
		return R107_BOOK_VALUE_NET;
	}
	public void setR107_BOOK_VALUE_NET(BigDecimal r107_BOOK_VALUE_NET) {
		R107_BOOK_VALUE_NET = r107_BOOK_VALUE_NET;
	}
	public BigDecimal getR107_RW() {
		return R107_RW;
	}
	public void setR107_RW(BigDecimal r107_RW) {
		R107_RW = r107_RW;
	}
	public BigDecimal getR107_RISK_VALUE() {
		return R107_RISK_VALUE;
	}
	public void setR107_RISK_VALUE(BigDecimal r107_RISK_VALUE) {
		R107_RISK_VALUE = r107_RISK_VALUE;
	}
	public BigDecimal getR108_BOOK_VALUE() {
		return R108_BOOK_VALUE;
	}
	public void setR108_BOOK_VALUE(BigDecimal r108_BOOK_VALUE) {
		R108_BOOK_VALUE = r108_BOOK_VALUE;
	}
	public BigDecimal getR108_MARGINS() {
		return R108_MARGINS;
	}
	public void setR108_MARGINS(BigDecimal r108_MARGINS) {
		R108_MARGINS = r108_MARGINS;
	}
	public BigDecimal getR108_BOOK_VALUE_NET() {
		return R108_BOOK_VALUE_NET;
	}
	public void setR108_BOOK_VALUE_NET(BigDecimal r108_BOOK_VALUE_NET) {
		R108_BOOK_VALUE_NET = r108_BOOK_VALUE_NET;
	}
	public BigDecimal getR108_RW() {
		return R108_RW;
	}
	public void setR108_RW(BigDecimal r108_RW) {
		R108_RW = r108_RW;
	}
	public BigDecimal getR108_RISK_VALUE() {
		return R108_RISK_VALUE;
	}
	public void setR108_RISK_VALUE(BigDecimal r108_RISK_VALUE) {
		R108_RISK_VALUE = r108_RISK_VALUE;
	}
	public BigDecimal getR109_BOOK_VALUE() {
		return R109_BOOK_VALUE;
	}
	public void setR109_BOOK_VALUE(BigDecimal r109_BOOK_VALUE) {
		R109_BOOK_VALUE = r109_BOOK_VALUE;
	}
	public BigDecimal getR109_MARGINS() {
		return R109_MARGINS;
	}
	public void setR109_MARGINS(BigDecimal r109_MARGINS) {
		R109_MARGINS = r109_MARGINS;
	}
	public BigDecimal getR109_BOOK_VALUE_NET() {
		return R109_BOOK_VALUE_NET;
	}
	public void setR109_BOOK_VALUE_NET(BigDecimal r109_BOOK_VALUE_NET) {
		R109_BOOK_VALUE_NET = r109_BOOK_VALUE_NET;
	}
	public BigDecimal getR109_RW() {
		return R109_RW;
	}
	public void setR109_RW(BigDecimal r109_RW) {
		R109_RW = r109_RW;
	}
	public BigDecimal getR109_RISK_VALUE() {
		return R109_RISK_VALUE;
	}
	public void setR109_RISK_VALUE(BigDecimal r109_RISK_VALUE) {
		R109_RISK_VALUE = r109_RISK_VALUE;
	}
	public BigDecimal getR110_BOOK_VALUE() {
		return R110_BOOK_VALUE;
	}
	public void setR110_BOOK_VALUE(BigDecimal r110_BOOK_VALUE) {
		R110_BOOK_VALUE = r110_BOOK_VALUE;
	}
	public BigDecimal getR110_MARGINS() {
		return R110_MARGINS;
	}
	public void setR110_MARGINS(BigDecimal r110_MARGINS) {
		R110_MARGINS = r110_MARGINS;
	}
	public BigDecimal getR110_BOOK_VALUE_NET() {
		return R110_BOOK_VALUE_NET;
	}
	public void setR110_BOOK_VALUE_NET(BigDecimal r110_BOOK_VALUE_NET) {
		R110_BOOK_VALUE_NET = r110_BOOK_VALUE_NET;
	}
	public BigDecimal getR110_RW() {
		return R110_RW;
	}
	public void setR110_RW(BigDecimal r110_RW) {
		R110_RW = r110_RW;
	}
	public BigDecimal getR110_RISK_VALUE() {
		return R110_RISK_VALUE;
	}
	public void setR110_RISK_VALUE(BigDecimal r110_RISK_VALUE) {
		R110_RISK_VALUE = r110_RISK_VALUE;
	}
	public BigDecimal getR111_BOOK_VALUE() {
		return R111_BOOK_VALUE;
	}
	public void setR111_BOOK_VALUE(BigDecimal r111_BOOK_VALUE) {
		R111_BOOK_VALUE = r111_BOOK_VALUE;
	}
	public BigDecimal getR111_MARGINS() {
		return R111_MARGINS;
	}
	public void setR111_MARGINS(BigDecimal r111_MARGINS) {
		R111_MARGINS = r111_MARGINS;
	}
	public BigDecimal getR111_BOOK_VALUE_NET() {
		return R111_BOOK_VALUE_NET;
	}
	public void setR111_BOOK_VALUE_NET(BigDecimal r111_BOOK_VALUE_NET) {
		R111_BOOK_VALUE_NET = r111_BOOK_VALUE_NET;
	}
	public BigDecimal getR111_RW() {
		return R111_RW;
	}
	public void setR111_RW(BigDecimal r111_RW) {
		R111_RW = r111_RW;
	}
	public BigDecimal getR111_RISK_VALUE() {
		return R111_RISK_VALUE;
	}
	public void setR111_RISK_VALUE(BigDecimal r111_RISK_VALUE) {
		R111_RISK_VALUE = r111_RISK_VALUE;
	}
	public BigDecimal getR112_BOOK_VALUE() {
		return R112_BOOK_VALUE;
	}
	public void setR112_BOOK_VALUE(BigDecimal r112_BOOK_VALUE) {
		R112_BOOK_VALUE = r112_BOOK_VALUE;
	}
	public BigDecimal getR112_MARGINS() {
		return R112_MARGINS;
	}
	public void setR112_MARGINS(BigDecimal r112_MARGINS) {
		R112_MARGINS = r112_MARGINS;
	}
	public BigDecimal getR112_BOOK_VALUE_NET() {
		return R112_BOOK_VALUE_NET;
	}
	public void setR112_BOOK_VALUE_NET(BigDecimal r112_BOOK_VALUE_NET) {
		R112_BOOK_VALUE_NET = r112_BOOK_VALUE_NET;
	}
	public BigDecimal getR112_RW() {
		return R112_RW;
	}
	public void setR112_RW(BigDecimal r112_RW) {
		R112_RW = r112_RW;
	}
	public BigDecimal getR112_RISK_VALUE() {
		return R112_RISK_VALUE;
	}
	public void setR112_RISK_VALUE(BigDecimal r112_RISK_VALUE) {
		R112_RISK_VALUE = r112_RISK_VALUE;
	}
	public BigDecimal getR113_BOOK_VALUE() {
		return R113_BOOK_VALUE;
	}
	public void setR113_BOOK_VALUE(BigDecimal r113_BOOK_VALUE) {
		R113_BOOK_VALUE = r113_BOOK_VALUE;
	}
	public BigDecimal getR113_MARGINS() {
		return R113_MARGINS;
	}
	public void setR113_MARGINS(BigDecimal r113_MARGINS) {
		R113_MARGINS = r113_MARGINS;
	}
	public BigDecimal getR113_BOOK_VALUE_NET() {
		return R113_BOOK_VALUE_NET;
	}
	public void setR113_BOOK_VALUE_NET(BigDecimal r113_BOOK_VALUE_NET) {
		R113_BOOK_VALUE_NET = r113_BOOK_VALUE_NET;
	}
	public BigDecimal getR113_RW() {
		return R113_RW;
	}
	public void setR113_RW(BigDecimal r113_RW) {
		R113_RW = r113_RW;
	}
	public BigDecimal getR113_RISK_VALUE() {
		return R113_RISK_VALUE;
	}
	public void setR113_RISK_VALUE(BigDecimal r113_RISK_VALUE) {
		R113_RISK_VALUE = r113_RISK_VALUE;
	}
	public BigDecimal getR114_BOOK_VALUE() {
		return R114_BOOK_VALUE;
	}
	public void setR114_BOOK_VALUE(BigDecimal r114_BOOK_VALUE) {
		R114_BOOK_VALUE = r114_BOOK_VALUE;
	}
	public BigDecimal getR114_MARGINS() {
		return R114_MARGINS;
	}
	public void setR114_MARGINS(BigDecimal r114_MARGINS) {
		R114_MARGINS = r114_MARGINS;
	}
	public BigDecimal getR114_BOOK_VALUE_NET() {
		return R114_BOOK_VALUE_NET;
	}
	public void setR114_BOOK_VALUE_NET(BigDecimal r114_BOOK_VALUE_NET) {
		R114_BOOK_VALUE_NET = r114_BOOK_VALUE_NET;
	}
	public BigDecimal getR114_RW() {
		return R114_RW;
	}
	public void setR114_RW(BigDecimal r114_RW) {
		R114_RW = r114_RW;
	}
	public BigDecimal getR114_RISK_VALUE() {
		return R114_RISK_VALUE;
	}
	public void setR114_RISK_VALUE(BigDecimal r114_RISK_VALUE) {
		R114_RISK_VALUE = r114_RISK_VALUE;
	}
	public BigDecimal getR115_BOOK_VALUE() {
		return R115_BOOK_VALUE;
	}
	public void setR115_BOOK_VALUE(BigDecimal r115_BOOK_VALUE) {
		R115_BOOK_VALUE = r115_BOOK_VALUE;
	}
	public BigDecimal getR115_MARGINS() {
		return R115_MARGINS;
	}
	public void setR115_MARGINS(BigDecimal r115_MARGINS) {
		R115_MARGINS = r115_MARGINS;
	}
	public BigDecimal getR115_BOOK_VALUE_NET() {
		return R115_BOOK_VALUE_NET;
	}
	public void setR115_BOOK_VALUE_NET(BigDecimal r115_BOOK_VALUE_NET) {
		R115_BOOK_VALUE_NET = r115_BOOK_VALUE_NET;
	}
	public BigDecimal getR115_RW() {
		return R115_RW;
	}
	public void setR115_RW(BigDecimal r115_RW) {
		R115_RW = r115_RW;
	}
	public BigDecimal getR115_RISK_VALUE() {
		return R115_RISK_VALUE;
	}
	public void setR115_RISK_VALUE(BigDecimal r115_RISK_VALUE) {
		R115_RISK_VALUE = r115_RISK_VALUE;
	}
	public BigDecimal getR116_BOOK_VALUE() {
		return R116_BOOK_VALUE;
	}
	public void setR116_BOOK_VALUE(BigDecimal r116_BOOK_VALUE) {
		R116_BOOK_VALUE = r116_BOOK_VALUE;
	}
	public BigDecimal getR116_MARGINS() {
		return R116_MARGINS;
	}
	public void setR116_MARGINS(BigDecimal r116_MARGINS) {
		R116_MARGINS = r116_MARGINS;
	}
	public BigDecimal getR116_BOOK_VALUE_NET() {
		return R116_BOOK_VALUE_NET;
	}
	public void setR116_BOOK_VALUE_NET(BigDecimal r116_BOOK_VALUE_NET) {
		R116_BOOK_VALUE_NET = r116_BOOK_VALUE_NET;
	}
	public BigDecimal getR116_RW() {
		return R116_RW;
	}
	public void setR116_RW(BigDecimal r116_RW) {
		R116_RW = r116_RW;
	}
	public BigDecimal getR116_RISK_VALUE() {
		return R116_RISK_VALUE;
	}
	public void setR116_RISK_VALUE(BigDecimal r116_RISK_VALUE) {
		R116_RISK_VALUE = r116_RISK_VALUE;
	}
	public BigDecimal getR117_BOOK_VALUE() {
		return R117_BOOK_VALUE;
	}
	public void setR117_BOOK_VALUE(BigDecimal r117_BOOK_VALUE) {
		R117_BOOK_VALUE = r117_BOOK_VALUE;
	}
	public BigDecimal getR117_MARGINS() {
		return R117_MARGINS;
	}
	public void setR117_MARGINS(BigDecimal r117_MARGINS) {
		R117_MARGINS = r117_MARGINS;
	}
	public BigDecimal getR117_BOOK_VALUE_NET() {
		return R117_BOOK_VALUE_NET;
	}
	public void setR117_BOOK_VALUE_NET(BigDecimal r117_BOOK_VALUE_NET) {
		R117_BOOK_VALUE_NET = r117_BOOK_VALUE_NET;
	}
	public BigDecimal getR117_RW() {
		return R117_RW;
	}
	public void setR117_RW(BigDecimal r117_RW) {
		R117_RW = r117_RW;
	}
	public BigDecimal getR117_RISK_VALUE() {
		return R117_RISK_VALUE;
	}
	public void setR117_RISK_VALUE(BigDecimal r117_RISK_VALUE) {
		R117_RISK_VALUE = r117_RISK_VALUE;
	}
	public BigDecimal getR118_BOOK_VALUE() {
		return R118_BOOK_VALUE;
	}
	public void setR118_BOOK_VALUE(BigDecimal r118_BOOK_VALUE) {
		R118_BOOK_VALUE = r118_BOOK_VALUE;
	}
	public BigDecimal getR118_MARGINS() {
		return R118_MARGINS;
	}
	public void setR118_MARGINS(BigDecimal r118_MARGINS) {
		R118_MARGINS = r118_MARGINS;
	}
	public BigDecimal getR118_BOOK_VALUE_NET() {
		return R118_BOOK_VALUE_NET;
	}
	public void setR118_BOOK_VALUE_NET(BigDecimal r118_BOOK_VALUE_NET) {
		R118_BOOK_VALUE_NET = r118_BOOK_VALUE_NET;
	}
	public BigDecimal getR118_RW() {
		return R118_RW;
	}
	public void setR118_RW(BigDecimal r118_RW) {
		R118_RW = r118_RW;
	}
	public BigDecimal getR118_RISK_VALUE() {
		return R118_RISK_VALUE;
	}
	public void setR118_RISK_VALUE(BigDecimal r118_RISK_VALUE) {
		R118_RISK_VALUE = r118_RISK_VALUE;
	}
	public BigDecimal getR119_BOOK_VALUE() {
		return R119_BOOK_VALUE;
	}
	public void setR119_BOOK_VALUE(BigDecimal r119_BOOK_VALUE) {
		R119_BOOK_VALUE = r119_BOOK_VALUE;
	}
	public BigDecimal getR119_MARGINS() {
		return R119_MARGINS;
	}
	public void setR119_MARGINS(BigDecimal r119_MARGINS) {
		R119_MARGINS = r119_MARGINS;
	}
	public BigDecimal getR119_BOOK_VALUE_NET() {
		return R119_BOOK_VALUE_NET;
	}
	public void setR119_BOOK_VALUE_NET(BigDecimal r119_BOOK_VALUE_NET) {
		R119_BOOK_VALUE_NET = r119_BOOK_VALUE_NET;
	}
	public BigDecimal getR119_RW() {
		return R119_RW;
	}
	public void setR119_RW(BigDecimal r119_RW) {
		R119_RW = r119_RW;
	}
	public BigDecimal getR119_RISK_VALUE() {
		return R119_RISK_VALUE;
	}
	public void setR119_RISK_VALUE(BigDecimal r119_RISK_VALUE) {
		R119_RISK_VALUE = r119_RISK_VALUE;
	}
	public BigDecimal getR120_BOOK_VALUE() {
		return R120_BOOK_VALUE;
	}
	public void setR120_BOOK_VALUE(BigDecimal r120_BOOK_VALUE) {
		R120_BOOK_VALUE = r120_BOOK_VALUE;
	}
	public BigDecimal getR120_MARGINS() {
		return R120_MARGINS;
	}
	public void setR120_MARGINS(BigDecimal r120_MARGINS) {
		R120_MARGINS = r120_MARGINS;
	}
	public BigDecimal getR120_BOOK_VALUE_NET() {
		return R120_BOOK_VALUE_NET;
	}
	public void setR120_BOOK_VALUE_NET(BigDecimal r120_BOOK_VALUE_NET) {
		R120_BOOK_VALUE_NET = r120_BOOK_VALUE_NET;
	}
	public BigDecimal getR120_RW() {
		return R120_RW;
	}
	public void setR120_RW(BigDecimal r120_RW) {
		R120_RW = r120_RW;
	}
	public BigDecimal getR120_RISK_VALUE() {
		return R120_RISK_VALUE;
	}
	public void setR120_RISK_VALUE(BigDecimal r120_RISK_VALUE) {
		R120_RISK_VALUE = r120_RISK_VALUE;
	}
	public BigDecimal getR121_BOOK_VALUE() {
		return R121_BOOK_VALUE;
	}
	public void setR121_BOOK_VALUE(BigDecimal r121_BOOK_VALUE) {
		R121_BOOK_VALUE = r121_BOOK_VALUE;
	}
	public BigDecimal getR121_MARGINS() {
		return R121_MARGINS;
	}
	public void setR121_MARGINS(BigDecimal r121_MARGINS) {
		R121_MARGINS = r121_MARGINS;
	}
	public BigDecimal getR121_BOOK_VALUE_NET() {
		return R121_BOOK_VALUE_NET;
	}
	public void setR121_BOOK_VALUE_NET(BigDecimal r121_BOOK_VALUE_NET) {
		R121_BOOK_VALUE_NET = r121_BOOK_VALUE_NET;
	}
	public BigDecimal getR121_RW() {
		return R121_RW;
	}
	public void setR121_RW(BigDecimal r121_RW) {
		R121_RW = r121_RW;
	}
	public BigDecimal getR121_RISK_VALUE() {
		return R121_RISK_VALUE;
	}
	public void setR121_RISK_VALUE(BigDecimal r121_RISK_VALUE) {
		R121_RISK_VALUE = r121_RISK_VALUE;
	}
	public BigDecimal getR122_BOOK_VALUE() {
		return R122_BOOK_VALUE;
	}
	public void setR122_BOOK_VALUE(BigDecimal r122_BOOK_VALUE) {
		R122_BOOK_VALUE = r122_BOOK_VALUE;
	}
	public BigDecimal getR122_MARGINS() {
		return R122_MARGINS;
	}
	public void setR122_MARGINS(BigDecimal r122_MARGINS) {
		R122_MARGINS = r122_MARGINS;
	}
	public BigDecimal getR122_BOOK_VALUE_NET() {
		return R122_BOOK_VALUE_NET;
	}
	public void setR122_BOOK_VALUE_NET(BigDecimal r122_BOOK_VALUE_NET) {
		R122_BOOK_VALUE_NET = r122_BOOK_VALUE_NET;
	}
	public BigDecimal getR122_RW() {
		return R122_RW;
	}
	public void setR122_RW(BigDecimal r122_RW) {
		R122_RW = r122_RW;
	}
	public BigDecimal getR122_RISK_VALUE() {
		return R122_RISK_VALUE;
	}
	public void setR122_RISK_VALUE(BigDecimal r122_RISK_VALUE) {
		R122_RISK_VALUE = r122_RISK_VALUE;
	}
	public BigDecimal getR123_BOOK_VALUE() {
		return R123_BOOK_VALUE;
	}
	public void setR123_BOOK_VALUE(BigDecimal r123_BOOK_VALUE) {
		R123_BOOK_VALUE = r123_BOOK_VALUE;
	}
	public BigDecimal getR123_MARGINS() {
		return R123_MARGINS;
	}
	public void setR123_MARGINS(BigDecimal r123_MARGINS) {
		R123_MARGINS = r123_MARGINS;
	}
	public BigDecimal getR123_BOOK_VALUE_NET() {
		return R123_BOOK_VALUE_NET;
	}
	public void setR123_BOOK_VALUE_NET(BigDecimal r123_BOOK_VALUE_NET) {
		R123_BOOK_VALUE_NET = r123_BOOK_VALUE_NET;
	}
	public BigDecimal getR123_RW() {
		return R123_RW;
	}
	public void setR123_RW(BigDecimal r123_RW) {
		R123_RW = r123_RW;
	}
	public BigDecimal getR123_RISK_VALUE() {
		return R123_RISK_VALUE;
	}
	public void setR123_RISK_VALUE(BigDecimal r123_RISK_VALUE) {
		R123_RISK_VALUE = r123_RISK_VALUE;
	}
	public BigDecimal getR124_BOOK_VALUE() {
		return R124_BOOK_VALUE;
	}
	public void setR124_BOOK_VALUE(BigDecimal r124_BOOK_VALUE) {
		R124_BOOK_VALUE = r124_BOOK_VALUE;
	}
	public BigDecimal getR124_MARGINS() {
		return R124_MARGINS;
	}
	public void setR124_MARGINS(BigDecimal r124_MARGINS) {
		R124_MARGINS = r124_MARGINS;
	}
	public BigDecimal getR124_BOOK_VALUE_NET() {
		return R124_BOOK_VALUE_NET;
	}
	public void setR124_BOOK_VALUE_NET(BigDecimal r124_BOOK_VALUE_NET) {
		R124_BOOK_VALUE_NET = r124_BOOK_VALUE_NET;
	}
	public BigDecimal getR124_RW() {
		return R124_RW;
	}
	public void setR124_RW(BigDecimal r124_RW) {
		R124_RW = r124_RW;
	}
	public BigDecimal getR124_RISK_VALUE() {
		return R124_RISK_VALUE;
	}
	public void setR124_RISK_VALUE(BigDecimal r124_RISK_VALUE) {
		R124_RISK_VALUE = r124_RISK_VALUE;
	}
	public BigDecimal getR125_BOOK_VALUE() {
		return R125_BOOK_VALUE;
	}
	public void setR125_BOOK_VALUE(BigDecimal r125_BOOK_VALUE) {
		R125_BOOK_VALUE = r125_BOOK_VALUE;
	}
	public BigDecimal getR125_MARGINS() {
		return R125_MARGINS;
	}
	public void setR125_MARGINS(BigDecimal r125_MARGINS) {
		R125_MARGINS = r125_MARGINS;
	}
	public BigDecimal getR125_BOOK_VALUE_NET() {
		return R125_BOOK_VALUE_NET;
	}
	public void setR125_BOOK_VALUE_NET(BigDecimal r125_BOOK_VALUE_NET) {
		R125_BOOK_VALUE_NET = r125_BOOK_VALUE_NET;
	}
	public BigDecimal getR125_RW() {
		return R125_RW;
	}
	public void setR125_RW(BigDecimal r125_RW) {
		R125_RW = r125_RW;
	}
	public BigDecimal getR125_RISK_VALUE() {
		return R125_RISK_VALUE;
	}
	public void setR125_RISK_VALUE(BigDecimal r125_RISK_VALUE) {
		R125_RISK_VALUE = r125_RISK_VALUE;
	}
	public BigDecimal getR126_BOOK_VALUE() {
		return R126_BOOK_VALUE;
	}
	public void setR126_BOOK_VALUE(BigDecimal r126_BOOK_VALUE) {
		R126_BOOK_VALUE = r126_BOOK_VALUE;
	}
	public BigDecimal getR126_MARGINS() {
		return R126_MARGINS;
	}
	public void setR126_MARGINS(BigDecimal r126_MARGINS) {
		R126_MARGINS = r126_MARGINS;
	}
	public BigDecimal getR126_BOOK_VALUE_NET() {
		return R126_BOOK_VALUE_NET;
	}
	public void setR126_BOOK_VALUE_NET(BigDecimal r126_BOOK_VALUE_NET) {
		R126_BOOK_VALUE_NET = r126_BOOK_VALUE_NET;
	}
	public BigDecimal getR126_RW() {
		return R126_RW;
	}
	public void setR126_RW(BigDecimal r126_RW) {
		R126_RW = r126_RW;
	}
	public BigDecimal getR126_RISK_VALUE() {
		return R126_RISK_VALUE;
	}
	public void setR126_RISK_VALUE(BigDecimal r126_RISK_VALUE) {
		R126_RISK_VALUE = r126_RISK_VALUE;
	}
	public BigDecimal getR127_BOOK_VALUE() {
		return R127_BOOK_VALUE;
	}
	public void setR127_BOOK_VALUE(BigDecimal r127_BOOK_VALUE) {
		R127_BOOK_VALUE = r127_BOOK_VALUE;
	}
	public BigDecimal getR127_MARGINS() {
		return R127_MARGINS;
	}
	public void setR127_MARGINS(BigDecimal r127_MARGINS) {
		R127_MARGINS = r127_MARGINS;
	}
	public BigDecimal getR127_BOOK_VALUE_NET() {
		return R127_BOOK_VALUE_NET;
	}
	public void setR127_BOOK_VALUE_NET(BigDecimal r127_BOOK_VALUE_NET) {
		R127_BOOK_VALUE_NET = r127_BOOK_VALUE_NET;
	}
	public BigDecimal getR127_RW() {
		return R127_RW;
	}
	public void setR127_RW(BigDecimal r127_RW) {
		R127_RW = r127_RW;
	}
	public BigDecimal getR127_RISK_VALUE() {
		return R127_RISK_VALUE;
	}
	public void setR127_RISK_VALUE(BigDecimal r127_RISK_VALUE) {
		R127_RISK_VALUE = r127_RISK_VALUE;
	}
	public BigDecimal getR128_BOOK_VALUE() {
		return R128_BOOK_VALUE;
	}
	public void setR128_BOOK_VALUE(BigDecimal r128_BOOK_VALUE) {
		R128_BOOK_VALUE = r128_BOOK_VALUE;
	}
	public BigDecimal getR128_MARGINS() {
		return R128_MARGINS;
	}
	public void setR128_MARGINS(BigDecimal r128_MARGINS) {
		R128_MARGINS = r128_MARGINS;
	}
	public BigDecimal getR128_BOOK_VALUE_NET() {
		return R128_BOOK_VALUE_NET;
	}
	public void setR128_BOOK_VALUE_NET(BigDecimal r128_BOOK_VALUE_NET) {
		R128_BOOK_VALUE_NET = r128_BOOK_VALUE_NET;
	}
	public BigDecimal getR128_RW() {
		return R128_RW;
	}
	public void setR128_RW(BigDecimal r128_RW) {
		R128_RW = r128_RW;
	}
	public BigDecimal getR128_RISK_VALUE() {
		return R128_RISK_VALUE;
	}
	public void setR128_RISK_VALUE(BigDecimal r128_RISK_VALUE) {
		R128_RISK_VALUE = r128_RISK_VALUE;
	}
	public BigDecimal getR129_BOOK_VALUE() {
		return R129_BOOK_VALUE;
	}
	public void setR129_BOOK_VALUE(BigDecimal r129_BOOK_VALUE) {
		R129_BOOK_VALUE = r129_BOOK_VALUE;
	}
	public BigDecimal getR129_MARGINS() {
		return R129_MARGINS;
	}
	public void setR129_MARGINS(BigDecimal r129_MARGINS) {
		R129_MARGINS = r129_MARGINS;
	}
	public BigDecimal getR129_BOOK_VALUE_NET() {
		return R129_BOOK_VALUE_NET;
	}
	public void setR129_BOOK_VALUE_NET(BigDecimal r129_BOOK_VALUE_NET) {
		R129_BOOK_VALUE_NET = r129_BOOK_VALUE_NET;
	}
	public BigDecimal getR129_RW() {
		return R129_RW;
	}
	public void setR129_RW(BigDecimal r129_RW) {
		R129_RW = r129_RW;
	}
	public BigDecimal getR129_RISK_VALUE() {
		return R129_RISK_VALUE;
	}
	public void setR129_RISK_VALUE(BigDecimal r129_RISK_VALUE) {
		R129_RISK_VALUE = r129_RISK_VALUE;
	}
	public BigDecimal getR130_BOOK_VALUE() {
		return R130_BOOK_VALUE;
	}
	public void setR130_BOOK_VALUE(BigDecimal r130_BOOK_VALUE) {
		R130_BOOK_VALUE = r130_BOOK_VALUE;
	}
	public BigDecimal getR130_MARGINS() {
		return R130_MARGINS;
	}
	public void setR130_MARGINS(BigDecimal r130_MARGINS) {
		R130_MARGINS = r130_MARGINS;
	}
	public BigDecimal getR130_BOOK_VALUE_NET() {
		return R130_BOOK_VALUE_NET;
	}
	public void setR130_BOOK_VALUE_NET(BigDecimal r130_BOOK_VALUE_NET) {
		R130_BOOK_VALUE_NET = r130_BOOK_VALUE_NET;
	}
	public BigDecimal getR130_RW() {
		return R130_RW;
	}
	public void setR130_RW(BigDecimal r130_RW) {
		R130_RW = r130_RW;
	}
	public BigDecimal getR130_RISK_VALUE() {
		return R130_RISK_VALUE;
	}
	public void setR130_RISK_VALUE(BigDecimal r130_RISK_VALUE) {
		R130_RISK_VALUE = r130_RISK_VALUE;
	}
	public RWA_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
