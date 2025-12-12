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
@Table(name = "BRRS_FSI_SUMMARYTABLE")
public class FSI_Summary_Entity {


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


private BigDecimal R11_AMOUNT;
private BigDecimal R12_AMOUNT;
private BigDecimal R13_AMOUNT;
private BigDecimal R14_AMOUNT;
private BigDecimal R15_AMOUNT;
private BigDecimal R16_AMOUNT;
private BigDecimal R17_AMOUNT;
private BigDecimal R18_AMOUNT;
private BigDecimal R19_AMOUNT;
private BigDecimal R20_AMOUNT;
private BigDecimal R21_AMOUNT;
private BigDecimal R22_AMOUNT;
private BigDecimal R23_AMOUNT;
private BigDecimal R24_AMOUNT;
private BigDecimal R25_AMOUNT;
private BigDecimal R26_AMOUNT;
private BigDecimal R27_AMOUNT;
private BigDecimal R28_AMOUNT;
private BigDecimal R29_AMOUNT;
private BigDecimal R30_AMOUNT;
private BigDecimal R31_AMOUNT;
private BigDecimal R32_AMOUNT;
private BigDecimal R33_AMOUNT;

private BigDecimal R39_AMOUNT;
private BigDecimal R40_AMOUNT;
private BigDecimal R41_AMOUNT;
private BigDecimal R42_AMOUNT;
private BigDecimal R43_AMOUNT;
private BigDecimal R44_AMOUNT;
private BigDecimal R45_AMOUNT;
private BigDecimal R46_AMOUNT;
private BigDecimal R47_AMOUNT;
private BigDecimal R48_AMOUNT;
private BigDecimal R49_AMOUNT;
private BigDecimal R50_AMOUNT;
private BigDecimal R51_AMOUNT;
private BigDecimal R52_AMOUNT;
private BigDecimal R53_AMOUNT;
private BigDecimal R54_AMOUNT;
private BigDecimal R55_AMOUNT;
private BigDecimal R56_AMOUNT;
private BigDecimal R57_AMOUNT;
private BigDecimal R58_AMOUNT;
private BigDecimal R59_AMOUNT;
private BigDecimal R60_AMOUNT;
private BigDecimal R61_AMOUNT;
private BigDecimal R62_AMOUNT;
private BigDecimal R63_AMOUNT;
private BigDecimal R64_AMOUNT;
private BigDecimal R65_AMOUNT;
private BigDecimal R66_AMOUNT;
private BigDecimal R67_AMOUNT;
private BigDecimal R68_AMOUNT;
private BigDecimal R69_AMOUNT;
private BigDecimal R70_AMOUNT;
private BigDecimal R71_AMOUNT;
private BigDecimal R72_AMOUNT;
private BigDecimal R73_AMOUNT;
private BigDecimal R74_AMOUNT;
private BigDecimal R75_AMOUNT;
private BigDecimal R76_AMOUNT;
private BigDecimal R77_AMOUNT;
private BigDecimal R86_AMOUNT;
private BigDecimal R87_AMOUNT;
private BigDecimal R88_AMOUNT;
private BigDecimal R89_AMOUNT;
private BigDecimal R90_AMOUNT;
private BigDecimal R91_AMOUNT;
private BigDecimal R92_AMOUNT;
private BigDecimal R93_AMOUNT;
private BigDecimal R94_AMOUNT;
private BigDecimal R95_AMOUNT;
private BigDecimal R96_AMOUNT;
private BigDecimal R97_AMOUNT;
private BigDecimal R98_AMOUNT;
private BigDecimal R99_AMOUNT;
private BigDecimal R100_AMOUNT;
private BigDecimal R101_AMOUNT;
private BigDecimal R102_AMOUNT;
private BigDecimal R103_AMOUNT;
private BigDecimal R104_AMOUNT;
private BigDecimal R105_AMOUNT;
private BigDecimal R106_AMOUNT;
private BigDecimal R107_AMOUNT;
private BigDecimal R108_AMOUNT;
private BigDecimal R109_AMOUNT;
private BigDecimal R110_AMOUNT;
private BigDecimal R111_AMOUNT;
private BigDecimal R112_AMOUNT;
private BigDecimal R113_AMOUNT;
private BigDecimal R114_AMOUNT;
private BigDecimal R115_AMOUNT;
private BigDecimal R116_AMOUNT;
private BigDecimal R117_AMOUNT;
private BigDecimal R118_AMOUNT;
private BigDecimal R119_AMOUNT;
private BigDecimal R120_AMOUNT;
private BigDecimal R121_AMOUNT;
private BigDecimal R122_AMOUNT;
private BigDecimal R123_AMOUNT;
private BigDecimal R124_AMOUNT;
private BigDecimal R125_AMOUNT;
private BigDecimal R126_AMOUNT;
private BigDecimal R127_AMOUNT;
private BigDecimal R128_AMOUNT;
private BigDecimal R129_AMOUNT;
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
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public BigDecimal getR27_AMOUNT() {
	return R27_AMOUNT;
}
public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
	R27_AMOUNT = r27_AMOUNT;
}
public BigDecimal getR28_AMOUNT() {
	return R28_AMOUNT;
}
public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
	R28_AMOUNT = r28_AMOUNT;
}
public BigDecimal getR29_AMOUNT() {
	return R29_AMOUNT;
}
public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
	R29_AMOUNT = r29_AMOUNT;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
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
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
}
public BigDecimal getR51_AMOUNT() {
	return R51_AMOUNT;
}
public void setR51_AMOUNT(BigDecimal r51_AMOUNT) {
	R51_AMOUNT = r51_AMOUNT;
}
public BigDecimal getR52_AMOUNT() {
	return R52_AMOUNT;
}
public void setR52_AMOUNT(BigDecimal r52_AMOUNT) {
	R52_AMOUNT = r52_AMOUNT;
}
public BigDecimal getR53_AMOUNT() {
	return R53_AMOUNT;
}
public void setR53_AMOUNT(BigDecimal r53_AMOUNT) {
	R53_AMOUNT = r53_AMOUNT;
}
public BigDecimal getR54_AMOUNT() {
	return R54_AMOUNT;
}
public void setR54_AMOUNT(BigDecimal r54_AMOUNT) {
	R54_AMOUNT = r54_AMOUNT;
}
public BigDecimal getR55_AMOUNT() {
	return R55_AMOUNT;
}
public void setR55_AMOUNT(BigDecimal r55_AMOUNT) {
	R55_AMOUNT = r55_AMOUNT;
}
public BigDecimal getR56_AMOUNT() {
	return R56_AMOUNT;
}
public void setR56_AMOUNT(BigDecimal r56_AMOUNT) {
	R56_AMOUNT = r56_AMOUNT;
}
public BigDecimal getR57_AMOUNT() {
	return R57_AMOUNT;
}
public void setR57_AMOUNT(BigDecimal r57_AMOUNT) {
	R57_AMOUNT = r57_AMOUNT;
}
public BigDecimal getR58_AMOUNT() {
	return R58_AMOUNT;
}
public void setR58_AMOUNT(BigDecimal r58_AMOUNT) {
	R58_AMOUNT = r58_AMOUNT;
}
public BigDecimal getR59_AMOUNT() {
	return R59_AMOUNT;
}
public void setR59_AMOUNT(BigDecimal r59_AMOUNT) {
	R59_AMOUNT = r59_AMOUNT;
}
public BigDecimal getR60_AMOUNT() {
	return R60_AMOUNT;
}
public void setR60_AMOUNT(BigDecimal r60_AMOUNT) {
	R60_AMOUNT = r60_AMOUNT;
}
public BigDecimal getR61_AMOUNT() {
	return R61_AMOUNT;
}
public void setR61_AMOUNT(BigDecimal r61_AMOUNT) {
	R61_AMOUNT = r61_AMOUNT;
}
public BigDecimal getR62_AMOUNT() {
	return R62_AMOUNT;
}
public void setR62_AMOUNT(BigDecimal r62_AMOUNT) {
	R62_AMOUNT = r62_AMOUNT;
}
public BigDecimal getR63_AMOUNT() {
	return R63_AMOUNT;
}
public void setR63_AMOUNT(BigDecimal r63_AMOUNT) {
	R63_AMOUNT = r63_AMOUNT;
}
public BigDecimal getR64_AMOUNT() {
	return R64_AMOUNT;
}
public void setR64_AMOUNT(BigDecimal r64_AMOUNT) {
	R64_AMOUNT = r64_AMOUNT;
}
public BigDecimal getR65_AMOUNT() {
	return R65_AMOUNT;
}
public void setR65_AMOUNT(BigDecimal r65_AMOUNT) {
	R65_AMOUNT = r65_AMOUNT;
}
public BigDecimal getR66_AMOUNT() {
	return R66_AMOUNT;
}
public void setR66_AMOUNT(BigDecimal r66_AMOUNT) {
	R66_AMOUNT = r66_AMOUNT;
}
public BigDecimal getR67_AMOUNT() {
	return R67_AMOUNT;
}
public void setR67_AMOUNT(BigDecimal r67_AMOUNT) {
	R67_AMOUNT = r67_AMOUNT;
}
public BigDecimal getR68_AMOUNT() {
	return R68_AMOUNT;
}
public void setR68_AMOUNT(BigDecimal r68_AMOUNT) {
	R68_AMOUNT = r68_AMOUNT;
}
public BigDecimal getR69_AMOUNT() {
	return R69_AMOUNT;
}
public void setR69_AMOUNT(BigDecimal r69_AMOUNT) {
	R69_AMOUNT = r69_AMOUNT;
}
public BigDecimal getR70_AMOUNT() {
	return R70_AMOUNT;
}
public void setR70_AMOUNT(BigDecimal r70_AMOUNT) {
	R70_AMOUNT = r70_AMOUNT;
}
public BigDecimal getR71_AMOUNT() {
	return R71_AMOUNT;
}
public void setR71_AMOUNT(BigDecimal r71_AMOUNT) {
	R71_AMOUNT = r71_AMOUNT;
}
public BigDecimal getR72_AMOUNT() {
	return R72_AMOUNT;
}
public void setR72_AMOUNT(BigDecimal r72_AMOUNT) {
	R72_AMOUNT = r72_AMOUNT;
}
public BigDecimal getR73_AMOUNT() {
	return R73_AMOUNT;
}
public void setR73_AMOUNT(BigDecimal r73_AMOUNT) {
	R73_AMOUNT = r73_AMOUNT;
}
public BigDecimal getR74_AMOUNT() {
	return R74_AMOUNT;
}
public void setR74_AMOUNT(BigDecimal r74_AMOUNT) {
	R74_AMOUNT = r74_AMOUNT;
}
public BigDecimal getR75_AMOUNT() {
	return R75_AMOUNT;
}
public void setR75_AMOUNT(BigDecimal r75_AMOUNT) {
	R75_AMOUNT = r75_AMOUNT;
}
public BigDecimal getR76_AMOUNT() {
	return R76_AMOUNT;
}
public void setR76_AMOUNT(BigDecimal r76_AMOUNT) {
	R76_AMOUNT = r76_AMOUNT;
}
public BigDecimal getR77_AMOUNT() {
	return R77_AMOUNT;
}
public void setR77_AMOUNT(BigDecimal r77_AMOUNT) {
	R77_AMOUNT = r77_AMOUNT;
}
public BigDecimal getR86_AMOUNT() {
	return R86_AMOUNT;
}
public void setR86_AMOUNT(BigDecimal r86_AMOUNT) {
	R86_AMOUNT = r86_AMOUNT;
}
public BigDecimal getR87_AMOUNT() {
	return R87_AMOUNT;
}
public void setR87_AMOUNT(BigDecimal r87_AMOUNT) {
	R87_AMOUNT = r87_AMOUNT;
}
public BigDecimal getR88_AMOUNT() {
	return R88_AMOUNT;
}
public void setR88_AMOUNT(BigDecimal r88_AMOUNT) {
	R88_AMOUNT = r88_AMOUNT;
}
public BigDecimal getR89_AMOUNT() {
	return R89_AMOUNT;
}
public void setR89_AMOUNT(BigDecimal r89_AMOUNT) {
	R89_AMOUNT = r89_AMOUNT;
}
public BigDecimal getR90_AMOUNT() {
	return R90_AMOUNT;
}
public void setR90_AMOUNT(BigDecimal r90_AMOUNT) {
	R90_AMOUNT = r90_AMOUNT;
}
public BigDecimal getR91_AMOUNT() {
	return R91_AMOUNT;
}
public void setR91_AMOUNT(BigDecimal r91_AMOUNT) {
	R91_AMOUNT = r91_AMOUNT;
}
public BigDecimal getR92_AMOUNT() {
	return R92_AMOUNT;
}
public void setR92_AMOUNT(BigDecimal r92_AMOUNT) {
	R92_AMOUNT = r92_AMOUNT;
}
public BigDecimal getR93_AMOUNT() {
	return R93_AMOUNT;
}
public void setR93_AMOUNT(BigDecimal r93_AMOUNT) {
	R93_AMOUNT = r93_AMOUNT;
}
public BigDecimal getR94_AMOUNT() {
	return R94_AMOUNT;
}
public void setR94_AMOUNT(BigDecimal r94_AMOUNT) {
	R94_AMOUNT = r94_AMOUNT;
}
public BigDecimal getR95_AMOUNT() {
	return R95_AMOUNT;
}
public void setR95_AMOUNT(BigDecimal r95_AMOUNT) {
	R95_AMOUNT = r95_AMOUNT;
}
public BigDecimal getR96_AMOUNT() {
	return R96_AMOUNT;
}
public void setR96_AMOUNT(BigDecimal r96_AMOUNT) {
	R96_AMOUNT = r96_AMOUNT;
}
public BigDecimal getR97_AMOUNT() {
	return R97_AMOUNT;
}
public void setR97_AMOUNT(BigDecimal r97_AMOUNT) {
	R97_AMOUNT = r97_AMOUNT;
}
public BigDecimal getR98_AMOUNT() {
	return R98_AMOUNT;
}
public void setR98_AMOUNT(BigDecimal r98_AMOUNT) {
	R98_AMOUNT = r98_AMOUNT;
}
public BigDecimal getR99_AMOUNT() {
	return R99_AMOUNT;
}
public void setR99_AMOUNT(BigDecimal r99_AMOUNT) {
	R99_AMOUNT = r99_AMOUNT;
}
public BigDecimal getR100_AMOUNT() {
	return R100_AMOUNT;
}
public void setR100_AMOUNT(BigDecimal r100_AMOUNT) {
	R100_AMOUNT = r100_AMOUNT;
}
public BigDecimal getR101_AMOUNT() {
	return R101_AMOUNT;
}
public void setR101_AMOUNT(BigDecimal r101_AMOUNT) {
	R101_AMOUNT = r101_AMOUNT;
}
public BigDecimal getR102_AMOUNT() {
	return R102_AMOUNT;
}
public void setR102_AMOUNT(BigDecimal r102_AMOUNT) {
	R102_AMOUNT = r102_AMOUNT;
}
public BigDecimal getR103_AMOUNT() {
	return R103_AMOUNT;
}
public void setR103_AMOUNT(BigDecimal r103_AMOUNT) {
	R103_AMOUNT = r103_AMOUNT;
}
public BigDecimal getR104_AMOUNT() {
	return R104_AMOUNT;
}
public void setR104_AMOUNT(BigDecimal r104_AMOUNT) {
	R104_AMOUNT = r104_AMOUNT;
}
public BigDecimal getR105_AMOUNT() {
	return R105_AMOUNT;
}
public void setR105_AMOUNT(BigDecimal r105_AMOUNT) {
	R105_AMOUNT = r105_AMOUNT;
}
public BigDecimal getR106_AMOUNT() {
	return R106_AMOUNT;
}
public void setR106_AMOUNT(BigDecimal r106_AMOUNT) {
	R106_AMOUNT = r106_AMOUNT;
}
public BigDecimal getR107_AMOUNT() {
	return R107_AMOUNT;
}
public void setR107_AMOUNT(BigDecimal r107_AMOUNT) {
	R107_AMOUNT = r107_AMOUNT;
}
public BigDecimal getR108_AMOUNT() {
	return R108_AMOUNT;
}
public void setR108_AMOUNT(BigDecimal r108_AMOUNT) {
	R108_AMOUNT = r108_AMOUNT;
}
public BigDecimal getR109_AMOUNT() {
	return R109_AMOUNT;
}
public void setR109_AMOUNT(BigDecimal r109_AMOUNT) {
	R109_AMOUNT = r109_AMOUNT;
}
public BigDecimal getR110_AMOUNT() {
	return R110_AMOUNT;
}
public void setR110_AMOUNT(BigDecimal r110_AMOUNT) {
	R110_AMOUNT = r110_AMOUNT;
}
public BigDecimal getR111_AMOUNT() {
	return R111_AMOUNT;
}
public void setR111_AMOUNT(BigDecimal r111_AMOUNT) {
	R111_AMOUNT = r111_AMOUNT;
}
public BigDecimal getR112_AMOUNT() {
	return R112_AMOUNT;
}
public void setR112_AMOUNT(BigDecimal r112_AMOUNT) {
	R112_AMOUNT = r112_AMOUNT;
}
public BigDecimal getR113_AMOUNT() {
	return R113_AMOUNT;
}
public void setR113_AMOUNT(BigDecimal r113_AMOUNT) {
	R113_AMOUNT = r113_AMOUNT;
}
public BigDecimal getR114_AMOUNT() {
	return R114_AMOUNT;
}
public void setR114_AMOUNT(BigDecimal r114_AMOUNT) {
	R114_AMOUNT = r114_AMOUNT;
}
public BigDecimal getR115_AMOUNT() {
	return R115_AMOUNT;
}
public void setR115_AMOUNT(BigDecimal r115_AMOUNT) {
	R115_AMOUNT = r115_AMOUNT;
}
public BigDecimal getR116_AMOUNT() {
	return R116_AMOUNT;
}
public void setR116_AMOUNT(BigDecimal r116_AMOUNT) {
	R116_AMOUNT = r116_AMOUNT;
}
public BigDecimal getR117_AMOUNT() {
	return R117_AMOUNT;
}
public void setR117_AMOUNT(BigDecimal r117_AMOUNT) {
	R117_AMOUNT = r117_AMOUNT;
}
public BigDecimal getR118_AMOUNT() {
	return R118_AMOUNT;
}
public void setR118_AMOUNT(BigDecimal r118_AMOUNT) {
	R118_AMOUNT = r118_AMOUNT;
}
public BigDecimal getR119_AMOUNT() {
	return R119_AMOUNT;
}
public void setR119_AMOUNT(BigDecimal r119_AMOUNT) {
	R119_AMOUNT = r119_AMOUNT;
}
public BigDecimal getR120_AMOUNT() {
	return R120_AMOUNT;
}
public void setR120_AMOUNT(BigDecimal r120_AMOUNT) {
	R120_AMOUNT = r120_AMOUNT;
}
public BigDecimal getR121_AMOUNT() {
	return R121_AMOUNT;
}
public void setR121_AMOUNT(BigDecimal r121_AMOUNT) {
	R121_AMOUNT = r121_AMOUNT;
}
public BigDecimal getR122_AMOUNT() {
	return R122_AMOUNT;
}
public void setR122_AMOUNT(BigDecimal r122_AMOUNT) {
	R122_AMOUNT = r122_AMOUNT;
}
public BigDecimal getR123_AMOUNT() {
	return R123_AMOUNT;
}
public void setR123_AMOUNT(BigDecimal r123_AMOUNT) {
	R123_AMOUNT = r123_AMOUNT;
}
public BigDecimal getR124_AMOUNT() {
	return R124_AMOUNT;
}
public void setR124_AMOUNT(BigDecimal r124_AMOUNT) {
	R124_AMOUNT = r124_AMOUNT;
}
public BigDecimal getR125_AMOUNT() {
	return R125_AMOUNT;
}
public void setR125_AMOUNT(BigDecimal r125_AMOUNT) {
	R125_AMOUNT = r125_AMOUNT;
}
public BigDecimal getR126_AMOUNT() {
	return R126_AMOUNT;
}
public void setR126_AMOUNT(BigDecimal r126_AMOUNT) {
	R126_AMOUNT = r126_AMOUNT;
}
public BigDecimal getR127_AMOUNT() {
	return R127_AMOUNT;
}
public void setR127_AMOUNT(BigDecimal r127_AMOUNT) {
	R127_AMOUNT = r127_AMOUNT;
}
public BigDecimal getR128_AMOUNT() {
	return R128_AMOUNT;
}
public void setR128_AMOUNT(BigDecimal r128_AMOUNT) {
	R128_AMOUNT = r128_AMOUNT;
}
public BigDecimal getR129_AMOUNT() {
	return R129_AMOUNT;
}
public void setR129_AMOUNT(BigDecimal r129_AMOUNT) {
	R129_AMOUNT = r129_AMOUNT;
}
public FSI_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}








}
