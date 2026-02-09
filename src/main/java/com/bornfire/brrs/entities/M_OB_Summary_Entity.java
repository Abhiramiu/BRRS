
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
@Table(name = "BRRS_M_OB_SUMMARYTABLE")
public class M_OB_Summary_Entity {



	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;

    
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;


    




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







	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}







	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}







	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}







	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}







	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}







	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}







	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}







	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}







	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}







	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}







	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}







	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}







	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}







	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}







	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}







	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}







	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}







	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}







	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}







	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}







	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}







	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}







	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}







	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}







	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}







	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}







	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}







	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}







	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}







	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}







	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}







	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}







	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}







	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}







	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}







	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}







	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}







	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}







	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}







	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}







	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}







	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}







	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}







	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}







	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}







	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}







	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}







	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}







	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}







	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}







	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}







	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}







	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}







	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}







	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}







	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}







	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}







	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}







	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}







	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}







	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}







	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}







	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}







	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}







	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}







	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}







	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}







	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}







	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}







	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}







	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}







	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}







	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}







	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}







	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}







	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}







	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}







	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}







	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}







	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}







	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}







	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}







	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}







	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}







	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}







	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}







	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}







	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}







	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}







	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}







	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}







	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}







	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}







	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}







	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}







	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}







	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}







	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}







	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}







	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}







	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}







	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}







	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}







	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}







	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}







	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}







	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}







	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}







	public M_OB_Summary_Entity() {
        super();
    }

}