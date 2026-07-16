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
@Table(name = "BRRS_M_PD_SUMMARYTABLE")
public class M_PD_Summary_Entity {
	


    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Id
    private Date report_date;
    private String REPORT_VERSION;
    private String REPORT_CODE;
    private String REPORT_DESC;
    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DEL_FLG;

	
	
	
	private String R8_PRODUCT;
	private String R9_PRODUCT;
	private BigDecimal R9_180D_ABOVE_PASTDUE;
	private BigDecimal R9_NON_ACCRUALS3;
	private BigDecimal R9_SPECIFIC_PROV3;
	private BigDecimal R9_NO_OF_ACC3;
	private String R10_PRODUCT;
	private BigDecimal R10_180D_ABOVE_PASTDUE;
	private BigDecimal R10_NON_ACCRUALS3;
	private BigDecimal R10_SPECIFIC_PROV3;
	private BigDecimal R10_NO_OF_ACC3;
	private String R11_PRODUCT;
	private BigDecimal R11_180D_ABOVE_PASTDUE;
	private BigDecimal R11_NON_ACCRUALS3;
	private BigDecimal R11_SPECIFIC_PROV3;
	private BigDecimal R11_NO_OF_ACC3;
	private String R12_PRODUCT;
	private String R13_PRODUCT;
	private BigDecimal R13_180D_ABOVE_PASTDUE;
	private BigDecimal R13_NON_ACCRUALS3;
	private BigDecimal R13_SPECIFIC_PROV3;
	private BigDecimal R13_NO_OF_ACC3;
	private String R14_PRODUCT;
	private BigDecimal R14_180D_ABOVE_PASTDUE;
	private BigDecimal R14_NON_ACCRUALS3;
	private BigDecimal R14_SPECIFIC_PROV3;
	private BigDecimal R14_NO_OF_ACC3;
	private String R15_PRODUCT;
	private BigDecimal R15_180D_ABOVE_PASTDUE;
	private BigDecimal R15_NON_ACCRUALS3;
	private BigDecimal R15_SPECIFIC_PROV3;
	private BigDecimal R15_NO_OF_ACC3;
	private String R16_PRODUCT;
	private BigDecimal R16_180D_ABOVE_PASTDUE;
	private BigDecimal R16_NON_ACCRUALS3;
	private BigDecimal R16_SPECIFIC_PROV3;
	private BigDecimal R16_NO_OF_ACC3;
	private String R17_PRODUCT;
	private BigDecimal R17_180D_ABOVE_PASTDUE;
	private BigDecimal R17_NON_ACCRUALS3;
	private BigDecimal R17_SPECIFIC_PROV3;
	private BigDecimal R17_NO_OF_ACC3;
	private String R18_PRODUCT;
	private BigDecimal R18_180D_ABOVE_PASTDUE;
	private BigDecimal R18_NON_ACCRUALS3;
	private BigDecimal R18_SPECIFIC_PROV3;
	private BigDecimal R18_NO_OF_ACC3;
	private String R19_PRODUCT;
	private BigDecimal R19_180D_ABOVE_PASTDUE;
	private BigDecimal R19_NON_ACCRUALS3;
	private BigDecimal R19_SPECIFIC_PROV3;
	private BigDecimal R19_NO_OF_ACC3;
	private String R20_PRODUCT;
	private BigDecimal R20_180D_ABOVE_PASTDUE;
	private BigDecimal R20_NON_ACCRUALS3;
	private BigDecimal R20_SPECIFIC_PROV3;
	private BigDecimal R20_NO_OF_ACC3;
	private String R21_PRODUCT;
	private BigDecimal R21_180D_ABOVE_PASTDUE;
	private BigDecimal R21_NON_ACCRUALS3;
	private BigDecimal R21_SPECIFIC_PROV3;
	private BigDecimal R21_NO_OF_ACC3;
	private String R22_PRODUCT;
	private BigDecimal R22_180D_ABOVE_PASTDUE;
	private BigDecimal R22_NON_ACCRUALS3;
	private BigDecimal R22_SPECIFIC_PROV3;
	private BigDecimal R22_NO_OF_ACC3;
	private String R23_PRODUCT;
	private BigDecimal R23_180D_ABOVE_PASTDUE;
	private BigDecimal R23_NON_ACCRUALS3;
	private BigDecimal R23_SPECIFIC_PROV3;
	private BigDecimal R23_NO_OF_ACC3;
	private String R24_PRODUCT;
	private BigDecimal R24_180D_ABOVE_PASTDUE;
	private BigDecimal R24_NON_ACCRUALS3;
	private BigDecimal R24_SPECIFIC_PROV3;
	private BigDecimal R24_NO_OF_ACC3;
	private String R25_PRODUCT;
	private BigDecimal R25_180D_ABOVE_PASTDUE;
	private BigDecimal R25_NON_ACCRUALS3;
	private BigDecimal R25_SPECIFIC_PROV3;
	private BigDecimal R25_NO_OF_ACC3;
	private String R26_PRODUCT;
	private String R27_PRODUCT;
	private BigDecimal R27_180D_ABOVE_PASTDUE;
	private BigDecimal R27_NON_ACCRUALS3;
	private BigDecimal R27_SPECIFIC_PROV3;
	private BigDecimal R27_NO_OF_ACC3;
	private String R28_PRODUCT;
	private BigDecimal R28_180D_ABOVE_PASTDUE;
	private BigDecimal R28_NON_ACCRUALS3;
	private BigDecimal R28_SPECIFIC_PROV3;
	private BigDecimal R28_NO_OF_ACC3;
	private String R29_PRODUCT;
	private BigDecimal R29_180D_ABOVE_PASTDUE;
	private BigDecimal R29_NON_ACCRUALS3;
	private BigDecimal R29_SPECIFIC_PROV3;
	private BigDecimal R29_NO_OF_ACC3;
	private String R30_PRODUCT;
	private BigDecimal R30_180D_ABOVE_PASTDUE;
	private BigDecimal R30_NON_ACCRUALS3;
	private BigDecimal R30_SPECIFIC_PROV3;
	private BigDecimal R30_NO_OF_ACC3;
	private String R31_PRODUCT;
	private BigDecimal R31_180D_ABOVE_PASTDUE;
	private BigDecimal R31_NON_ACCRUALS3;
	private BigDecimal R31_SPECIFIC_PROV3;
	private BigDecimal R31_NO_OF_ACC3;
	private String R32_PRODUCT;
	private BigDecimal R32_180D_ABOVE_PASTDUE;
	private BigDecimal R32_NON_ACCRUALS3;
	private BigDecimal R32_SPECIFIC_PROV3;
	private BigDecimal R32_NO_OF_ACC3;
	private String R33_PRODUCT;
	private BigDecimal R33_180D_ABOVE_PASTDUE;
	private BigDecimal R33_NON_ACCRUALS3;
	private BigDecimal R33_SPECIFIC_PROV3;
	private BigDecimal R33_NO_OF_ACC3;
	private String R34_PRODUCT;
	private BigDecimal R34_180D_ABOVE_PASTDUE;
	private BigDecimal R34_NON_ACCRUALS3;
	private BigDecimal R34_SPECIFIC_PROV3;
	private BigDecimal R34_NO_OF_ACC3;
	private String R35_PRODUCT;
	private String R36_PRODUCT;
	private BigDecimal R36_180D_ABOVE_PASTDUE;
	private BigDecimal R36_NON_ACCRUALS3;
	private BigDecimal R36_SPECIFIC_PROV3;
	private BigDecimal R36_NO_OF_ACC3;
	private String R37_PRODUCT;
	private BigDecimal R37_180D_ABOVE_PASTDUE;
	private BigDecimal R37_NON_ACCRUALS3;
	private BigDecimal R37_SPECIFIC_PROV3;
	private BigDecimal R37_NO_OF_ACC3;
	private String R38_PRODUCT;
	private BigDecimal R39_180D_ABOVE_PASTDUE;
	private BigDecimal R39_NON_ACCRUALS3;
	private BigDecimal R39_SPECIFIC_PROV3;
	private BigDecimal R39_NO_OF_ACC3;
	private String R39_PRODUCT;
	private String R40_PRODUCT;
	private BigDecimal R40_180D_ABOVE_PASTDUE;
	private BigDecimal R40_NON_ACCRUALS3;
	private BigDecimal R40_SPECIFIC_PROV3;
	private BigDecimal R40_NO_OF_ACC3;
	private String R41_PRODUCT;
	private String R42_PRODUCT;
	private BigDecimal R42_180D_ABOVE_PASTDUE;
	private BigDecimal R42_NON_ACCRUALS3;
	private BigDecimal R42_SPECIFIC_PROV3;
	private BigDecimal R42_NO_OF_ACC3;
	private String R43_PRODUCT;
	private BigDecimal R43_180D_ABOVE_PASTDUE;
	private BigDecimal R43_NON_ACCRUALS3;
	private BigDecimal R43_SPECIFIC_PROV3;
	private BigDecimal R43_NO_OF_ACC3;
	private String R44_PRODUCT;
	private BigDecimal R44_180D_ABOVE_PASTDUE;
	private BigDecimal R44_NON_ACCRUALS3;
	private BigDecimal R44_SPECIFIC_PROV3;
	private BigDecimal R44_NO_OF_ACC3;
	private String R45_PRODUCT;
	private BigDecimal R45_180D_ABOVE_PASTDUE;
	private BigDecimal R45_NON_ACCRUALS3;
	private BigDecimal R45_SPECIFIC_PROV3;
	private BigDecimal R45_NO_OF_ACC3;
	private String R46_PRODUCT;
	private String R47_PRODUCT;
	private BigDecimal R47_180D_ABOVE_PASTDUE;
	private BigDecimal R47_NON_ACCRUALS3;
	private BigDecimal R47_SPECIFIC_PROV3;
	private BigDecimal R47_NO_OF_ACC3;
	private String R48_PRODUCT;
	private BigDecimal R48_180D_ABOVE_PASTDUE;
	private BigDecimal R48_NON_ACCRUALS3;
	private BigDecimal R48_SPECIFIC_PROV3;
	private BigDecimal R48_NO_OF_ACC3;
	private String R49_PRODUCT;
	private BigDecimal R49_180D_ABOVE_PASTDUE;
	private BigDecimal R49_NON_ACCRUALS3;
	private BigDecimal R49_SPECIFIC_PROV3;
	private BigDecimal R49_NO_OF_ACC3;
	private String R50_PRODUCT;
	private String R51_PRODUCT;
	private BigDecimal R51_180D_ABOVE_PASTDUE;
	private BigDecimal R51_NON_ACCRUALS3;
	private BigDecimal R51_SPECIFIC_PROV3;
	private BigDecimal R51_NO_OF_ACC3;
	private String R52_PRODUCT;
	private BigDecimal R52_180D_ABOVE_PASTDUE;
	private BigDecimal R52_NON_ACCRUALS3;
	private BigDecimal R52_SPECIFIC_PROV3;
	private BigDecimal R52_NO_OF_ACC3;
	private String R53_PRODUCT;
	private BigDecimal R53_180D_ABOVE_PASTDUE;
	private BigDecimal R53_NON_ACCRUALS3;
	private BigDecimal R53_SPECIFIC_PROV3;
	private BigDecimal R53_NO_OF_ACC3;
	private String R54_PRODUCT;
	private String R55_PRODUCT;
	private BigDecimal R55_180D_ABOVE_PASTDUE;
	private BigDecimal R55_NON_ACCRUALS3;
	private BigDecimal R55_SPECIFIC_PROV3;
	private BigDecimal R55_NO_OF_ACC3;
	private String R56_PRODUCT;
	private BigDecimal R56_180D_ABOVE_PASTDUE;
	private BigDecimal R56_NON_ACCRUALS3;
	private BigDecimal R56_SPECIFIC_PROV3;
	private BigDecimal R56_NO_OF_ACC3;
	private String R57_PRODUCT;
	private BigDecimal R57_180D_ABOVE_PASTDUE;
	private BigDecimal R57_NON_ACCRUALS3;
	private BigDecimal R57_SPECIFIC_PROV3;
	private BigDecimal R57_NO_OF_ACC3;
	private String R58_PRODUCT;
	private BigDecimal R58_180D_ABOVE_PASTDUE;
	private BigDecimal R58_NON_ACCRUALS3;
	private BigDecimal R58_SPECIFIC_PROV3;
	private BigDecimal R58_NO_OF_ACC3;
	private String R59_PRODUCT;
	private BigDecimal R59_180D_ABOVE_PASTDUE;
	private BigDecimal R59_NON_ACCRUALS3;
	private BigDecimal R59_SPECIFIC_PROV3;
	private BigDecimal R59_NO_OF_ACC3;
	private String R60_PRODUCT;
	private BigDecimal R60_180D_ABOVE_PASTDUE;
	private BigDecimal R60_NON_ACCRUALS3;
	private BigDecimal R60_SPECIFIC_PROV3;
	private BigDecimal R60_NO_OF_ACC3;
	private String R61_PRODUCT;


	
	
	
	public Date getReport_date() {
		return report_date;
	}





	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}





	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}





	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
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





	public String getR8_PRODUCT() {
		return R8_PRODUCT;
	}





	public void setR8_PRODUCT(String r8_PRODUCT) {
		R8_PRODUCT = r8_PRODUCT;
	}





	public String getR9_PRODUCT() {
		return R9_PRODUCT;
	}





	public void setR9_PRODUCT(String r9_PRODUCT) {
		R9_PRODUCT = r9_PRODUCT;
	}





	public BigDecimal getR9_180D_ABOVE_PASTDUE() {
		return R9_180D_ABOVE_PASTDUE;
	}





	public void setR9_180D_ABOVE_PASTDUE(BigDecimal r9_180d_ABOVE_PASTDUE) {
		R9_180D_ABOVE_PASTDUE = r9_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR9_NON_ACCRUALS3() {
		return R9_NON_ACCRUALS3;
	}





	public void setR9_NON_ACCRUALS3(BigDecimal r9_NON_ACCRUALS3) {
		R9_NON_ACCRUALS3 = r9_NON_ACCRUALS3;
	}





	public BigDecimal getR9_SPECIFIC_PROV3() {
		return R9_SPECIFIC_PROV3;
	}





	public void setR9_SPECIFIC_PROV3(BigDecimal r9_SPECIFIC_PROV3) {
		R9_SPECIFIC_PROV3 = r9_SPECIFIC_PROV3;
	}





	public BigDecimal getR9_NO_OF_ACC3() {
		return R9_NO_OF_ACC3;
	}





	public void setR9_NO_OF_ACC3(BigDecimal r9_NO_OF_ACC3) {
		R9_NO_OF_ACC3 = r9_NO_OF_ACC3;
	}





	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}





	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}





	public BigDecimal getR10_180D_ABOVE_PASTDUE() {
		return R10_180D_ABOVE_PASTDUE;
	}





	public void setR10_180D_ABOVE_PASTDUE(BigDecimal r10_180d_ABOVE_PASTDUE) {
		R10_180D_ABOVE_PASTDUE = r10_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR10_NON_ACCRUALS3() {
		return R10_NON_ACCRUALS3;
	}





	public void setR10_NON_ACCRUALS3(BigDecimal r10_NON_ACCRUALS3) {
		R10_NON_ACCRUALS3 = r10_NON_ACCRUALS3;
	}





	public BigDecimal getR10_SPECIFIC_PROV3() {
		return R10_SPECIFIC_PROV3;
	}





	public void setR10_SPECIFIC_PROV3(BigDecimal r10_SPECIFIC_PROV3) {
		R10_SPECIFIC_PROV3 = r10_SPECIFIC_PROV3;
	}





	public BigDecimal getR10_NO_OF_ACC3() {
		return R10_NO_OF_ACC3;
	}





	public void setR10_NO_OF_ACC3(BigDecimal r10_NO_OF_ACC3) {
		R10_NO_OF_ACC3 = r10_NO_OF_ACC3;
	}





	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}





	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}





	public BigDecimal getR11_180D_ABOVE_PASTDUE() {
		return R11_180D_ABOVE_PASTDUE;
	}





	public void setR11_180D_ABOVE_PASTDUE(BigDecimal r11_180d_ABOVE_PASTDUE) {
		R11_180D_ABOVE_PASTDUE = r11_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR11_NON_ACCRUALS3() {
		return R11_NON_ACCRUALS3;
	}





	public void setR11_NON_ACCRUALS3(BigDecimal r11_NON_ACCRUALS3) {
		R11_NON_ACCRUALS3 = r11_NON_ACCRUALS3;
	}





	public BigDecimal getR11_SPECIFIC_PROV3() {
		return R11_SPECIFIC_PROV3;
	}





	public void setR11_SPECIFIC_PROV3(BigDecimal r11_SPECIFIC_PROV3) {
		R11_SPECIFIC_PROV3 = r11_SPECIFIC_PROV3;
	}





	public BigDecimal getR11_NO_OF_ACC3() {
		return R11_NO_OF_ACC3;
	}





	public void setR11_NO_OF_ACC3(BigDecimal r11_NO_OF_ACC3) {
		R11_NO_OF_ACC3 = r11_NO_OF_ACC3;
	}





	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}





	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}





	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}





	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}





	public BigDecimal getR13_180D_ABOVE_PASTDUE() {
		return R13_180D_ABOVE_PASTDUE;
	}





	public void setR13_180D_ABOVE_PASTDUE(BigDecimal r13_180d_ABOVE_PASTDUE) {
		R13_180D_ABOVE_PASTDUE = r13_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR13_NON_ACCRUALS3() {
		return R13_NON_ACCRUALS3;
	}





	public void setR13_NON_ACCRUALS3(BigDecimal r13_NON_ACCRUALS3) {
		R13_NON_ACCRUALS3 = r13_NON_ACCRUALS3;
	}





	public BigDecimal getR13_SPECIFIC_PROV3() {
		return R13_SPECIFIC_PROV3;
	}





	public void setR13_SPECIFIC_PROV3(BigDecimal r13_SPECIFIC_PROV3) {
		R13_SPECIFIC_PROV3 = r13_SPECIFIC_PROV3;
	}





	public BigDecimal getR13_NO_OF_ACC3() {
		return R13_NO_OF_ACC3;
	}





	public void setR13_NO_OF_ACC3(BigDecimal r13_NO_OF_ACC3) {
		R13_NO_OF_ACC3 = r13_NO_OF_ACC3;
	}





	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}





	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}





	public BigDecimal getR14_180D_ABOVE_PASTDUE() {
		return R14_180D_ABOVE_PASTDUE;
	}





	public void setR14_180D_ABOVE_PASTDUE(BigDecimal r14_180d_ABOVE_PASTDUE) {
		R14_180D_ABOVE_PASTDUE = r14_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR14_NON_ACCRUALS3() {
		return R14_NON_ACCRUALS3;
	}





	public void setR14_NON_ACCRUALS3(BigDecimal r14_NON_ACCRUALS3) {
		R14_NON_ACCRUALS3 = r14_NON_ACCRUALS3;
	}





	public BigDecimal getR14_SPECIFIC_PROV3() {
		return R14_SPECIFIC_PROV3;
	}





	public void setR14_SPECIFIC_PROV3(BigDecimal r14_SPECIFIC_PROV3) {
		R14_SPECIFIC_PROV3 = r14_SPECIFIC_PROV3;
	}





	public BigDecimal getR14_NO_OF_ACC3() {
		return R14_NO_OF_ACC3;
	}





	public void setR14_NO_OF_ACC3(BigDecimal r14_NO_OF_ACC3) {
		R14_NO_OF_ACC3 = r14_NO_OF_ACC3;
	}





	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}





	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}





	public BigDecimal getR15_180D_ABOVE_PASTDUE() {
		return R15_180D_ABOVE_PASTDUE;
	}





	public void setR15_180D_ABOVE_PASTDUE(BigDecimal r15_180d_ABOVE_PASTDUE) {
		R15_180D_ABOVE_PASTDUE = r15_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR15_NON_ACCRUALS3() {
		return R15_NON_ACCRUALS3;
	}





	public void setR15_NON_ACCRUALS3(BigDecimal r15_NON_ACCRUALS3) {
		R15_NON_ACCRUALS3 = r15_NON_ACCRUALS3;
	}





	public BigDecimal getR15_SPECIFIC_PROV3() {
		return R15_SPECIFIC_PROV3;
	}





	public void setR15_SPECIFIC_PROV3(BigDecimal r15_SPECIFIC_PROV3) {
		R15_SPECIFIC_PROV3 = r15_SPECIFIC_PROV3;
	}





	public BigDecimal getR15_NO_OF_ACC3() {
		return R15_NO_OF_ACC3;
	}





	public void setR15_NO_OF_ACC3(BigDecimal r15_NO_OF_ACC3) {
		R15_NO_OF_ACC3 = r15_NO_OF_ACC3;
	}





	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}





	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}





	public BigDecimal getR16_180D_ABOVE_PASTDUE() {
		return R16_180D_ABOVE_PASTDUE;
	}





	public void setR16_180D_ABOVE_PASTDUE(BigDecimal r16_180d_ABOVE_PASTDUE) {
		R16_180D_ABOVE_PASTDUE = r16_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR16_NON_ACCRUALS3() {
		return R16_NON_ACCRUALS3;
	}





	public void setR16_NON_ACCRUALS3(BigDecimal r16_NON_ACCRUALS3) {
		R16_NON_ACCRUALS3 = r16_NON_ACCRUALS3;
	}





	public BigDecimal getR16_SPECIFIC_PROV3() {
		return R16_SPECIFIC_PROV3;
	}





	public void setR16_SPECIFIC_PROV3(BigDecimal r16_SPECIFIC_PROV3) {
		R16_SPECIFIC_PROV3 = r16_SPECIFIC_PROV3;
	}





	public BigDecimal getR16_NO_OF_ACC3() {
		return R16_NO_OF_ACC3;
	}





	public void setR16_NO_OF_ACC3(BigDecimal r16_NO_OF_ACC3) {
		R16_NO_OF_ACC3 = r16_NO_OF_ACC3;
	}





	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}





	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}





	public BigDecimal getR17_180D_ABOVE_PASTDUE() {
		return R17_180D_ABOVE_PASTDUE;
	}





	public void setR17_180D_ABOVE_PASTDUE(BigDecimal r17_180d_ABOVE_PASTDUE) {
		R17_180D_ABOVE_PASTDUE = r17_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR17_NON_ACCRUALS3() {
		return R17_NON_ACCRUALS3;
	}





	public void setR17_NON_ACCRUALS3(BigDecimal r17_NON_ACCRUALS3) {
		R17_NON_ACCRUALS3 = r17_NON_ACCRUALS3;
	}





	public BigDecimal getR17_SPECIFIC_PROV3() {
		return R17_SPECIFIC_PROV3;
	}





	public void setR17_SPECIFIC_PROV3(BigDecimal r17_SPECIFIC_PROV3) {
		R17_SPECIFIC_PROV3 = r17_SPECIFIC_PROV3;
	}





	public BigDecimal getR17_NO_OF_ACC3() {
		return R17_NO_OF_ACC3;
	}





	public void setR17_NO_OF_ACC3(BigDecimal r17_NO_OF_ACC3) {
		R17_NO_OF_ACC3 = r17_NO_OF_ACC3;
	}





	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}





	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}





	public BigDecimal getR18_180D_ABOVE_PASTDUE() {
		return R18_180D_ABOVE_PASTDUE;
	}





	public void setR18_180D_ABOVE_PASTDUE(BigDecimal r18_180d_ABOVE_PASTDUE) {
		R18_180D_ABOVE_PASTDUE = r18_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR18_NON_ACCRUALS3() {
		return R18_NON_ACCRUALS3;
	}





	public void setR18_NON_ACCRUALS3(BigDecimal r18_NON_ACCRUALS3) {
		R18_NON_ACCRUALS3 = r18_NON_ACCRUALS3;
	}





	public BigDecimal getR18_SPECIFIC_PROV3() {
		return R18_SPECIFIC_PROV3;
	}





	public void setR18_SPECIFIC_PROV3(BigDecimal r18_SPECIFIC_PROV3) {
		R18_SPECIFIC_PROV3 = r18_SPECIFIC_PROV3;
	}





	public BigDecimal getR18_NO_OF_ACC3() {
		return R18_NO_OF_ACC3;
	}





	public void setR18_NO_OF_ACC3(BigDecimal r18_NO_OF_ACC3) {
		R18_NO_OF_ACC3 = r18_NO_OF_ACC3;
	}





	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}





	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}





	public BigDecimal getR19_180D_ABOVE_PASTDUE() {
		return R19_180D_ABOVE_PASTDUE;
	}





	public void setR19_180D_ABOVE_PASTDUE(BigDecimal r19_180d_ABOVE_PASTDUE) {
		R19_180D_ABOVE_PASTDUE = r19_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR19_NON_ACCRUALS3() {
		return R19_NON_ACCRUALS3;
	}





	public void setR19_NON_ACCRUALS3(BigDecimal r19_NON_ACCRUALS3) {
		R19_NON_ACCRUALS3 = r19_NON_ACCRUALS3;
	}





	public BigDecimal getR19_SPECIFIC_PROV3() {
		return R19_SPECIFIC_PROV3;
	}





	public void setR19_SPECIFIC_PROV3(BigDecimal r19_SPECIFIC_PROV3) {
		R19_SPECIFIC_PROV3 = r19_SPECIFIC_PROV3;
	}





	public BigDecimal getR19_NO_OF_ACC3() {
		return R19_NO_OF_ACC3;
	}





	public void setR19_NO_OF_ACC3(BigDecimal r19_NO_OF_ACC3) {
		R19_NO_OF_ACC3 = r19_NO_OF_ACC3;
	}





	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}





	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}





	public BigDecimal getR20_180D_ABOVE_PASTDUE() {
		return R20_180D_ABOVE_PASTDUE;
	}





	public void setR20_180D_ABOVE_PASTDUE(BigDecimal r20_180d_ABOVE_PASTDUE) {
		R20_180D_ABOVE_PASTDUE = r20_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR20_NON_ACCRUALS3() {
		return R20_NON_ACCRUALS3;
	}





	public void setR20_NON_ACCRUALS3(BigDecimal r20_NON_ACCRUALS3) {
		R20_NON_ACCRUALS3 = r20_NON_ACCRUALS3;
	}





	public BigDecimal getR20_SPECIFIC_PROV3() {
		return R20_SPECIFIC_PROV3;
	}





	public void setR20_SPECIFIC_PROV3(BigDecimal r20_SPECIFIC_PROV3) {
		R20_SPECIFIC_PROV3 = r20_SPECIFIC_PROV3;
	}





	public BigDecimal getR20_NO_OF_ACC3() {
		return R20_NO_OF_ACC3;
	}





	public void setR20_NO_OF_ACC3(BigDecimal r20_NO_OF_ACC3) {
		R20_NO_OF_ACC3 = r20_NO_OF_ACC3;
	}





	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}





	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}





	public BigDecimal getR21_180D_ABOVE_PASTDUE() {
		return R21_180D_ABOVE_PASTDUE;
	}





	public void setR21_180D_ABOVE_PASTDUE(BigDecimal r21_180d_ABOVE_PASTDUE) {
		R21_180D_ABOVE_PASTDUE = r21_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR21_NON_ACCRUALS3() {
		return R21_NON_ACCRUALS3;
	}





	public void setR21_NON_ACCRUALS3(BigDecimal r21_NON_ACCRUALS3) {
		R21_NON_ACCRUALS3 = r21_NON_ACCRUALS3;
	}





	public BigDecimal getR21_SPECIFIC_PROV3() {
		return R21_SPECIFIC_PROV3;
	}





	public void setR21_SPECIFIC_PROV3(BigDecimal r21_SPECIFIC_PROV3) {
		R21_SPECIFIC_PROV3 = r21_SPECIFIC_PROV3;
	}





	public BigDecimal getR21_NO_OF_ACC3() {
		return R21_NO_OF_ACC3;
	}





	public void setR21_NO_OF_ACC3(BigDecimal r21_NO_OF_ACC3) {
		R21_NO_OF_ACC3 = r21_NO_OF_ACC3;
	}





	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}





	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}





	public BigDecimal getR22_180D_ABOVE_PASTDUE() {
		return R22_180D_ABOVE_PASTDUE;
	}





	public void setR22_180D_ABOVE_PASTDUE(BigDecimal r22_180d_ABOVE_PASTDUE) {
		R22_180D_ABOVE_PASTDUE = r22_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR22_NON_ACCRUALS3() {
		return R22_NON_ACCRUALS3;
	}





	public void setR22_NON_ACCRUALS3(BigDecimal r22_NON_ACCRUALS3) {
		R22_NON_ACCRUALS3 = r22_NON_ACCRUALS3;
	}





	public BigDecimal getR22_SPECIFIC_PROV3() {
		return R22_SPECIFIC_PROV3;
	}





	public void setR22_SPECIFIC_PROV3(BigDecimal r22_SPECIFIC_PROV3) {
		R22_SPECIFIC_PROV3 = r22_SPECIFIC_PROV3;
	}





	public BigDecimal getR22_NO_OF_ACC3() {
		return R22_NO_OF_ACC3;
	}





	public void setR22_NO_OF_ACC3(BigDecimal r22_NO_OF_ACC3) {
		R22_NO_OF_ACC3 = r22_NO_OF_ACC3;
	}





	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}





	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}





	public BigDecimal getR23_180D_ABOVE_PASTDUE() {
		return R23_180D_ABOVE_PASTDUE;
	}





	public void setR23_180D_ABOVE_PASTDUE(BigDecimal r23_180d_ABOVE_PASTDUE) {
		R23_180D_ABOVE_PASTDUE = r23_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR23_NON_ACCRUALS3() {
		return R23_NON_ACCRUALS3;
	}





	public void setR23_NON_ACCRUALS3(BigDecimal r23_NON_ACCRUALS3) {
		R23_NON_ACCRUALS3 = r23_NON_ACCRUALS3;
	}





	public BigDecimal getR23_SPECIFIC_PROV3() {
		return R23_SPECIFIC_PROV3;
	}





	public void setR23_SPECIFIC_PROV3(BigDecimal r23_SPECIFIC_PROV3) {
		R23_SPECIFIC_PROV3 = r23_SPECIFIC_PROV3;
	}





	public BigDecimal getR23_NO_OF_ACC3() {
		return R23_NO_OF_ACC3;
	}





	public void setR23_NO_OF_ACC3(BigDecimal r23_NO_OF_ACC3) {
		R23_NO_OF_ACC3 = r23_NO_OF_ACC3;
	}





	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}





	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}





	public BigDecimal getR24_180D_ABOVE_PASTDUE() {
		return R24_180D_ABOVE_PASTDUE;
	}





	public void setR24_180D_ABOVE_PASTDUE(BigDecimal r24_180d_ABOVE_PASTDUE) {
		R24_180D_ABOVE_PASTDUE = r24_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR24_NON_ACCRUALS3() {
		return R24_NON_ACCRUALS3;
	}





	public void setR24_NON_ACCRUALS3(BigDecimal r24_NON_ACCRUALS3) {
		R24_NON_ACCRUALS3 = r24_NON_ACCRUALS3;
	}





	public BigDecimal getR24_SPECIFIC_PROV3() {
		return R24_SPECIFIC_PROV3;
	}





	public void setR24_SPECIFIC_PROV3(BigDecimal r24_SPECIFIC_PROV3) {
		R24_SPECIFIC_PROV3 = r24_SPECIFIC_PROV3;
	}





	public BigDecimal getR24_NO_OF_ACC3() {
		return R24_NO_OF_ACC3;
	}





	public void setR24_NO_OF_ACC3(BigDecimal r24_NO_OF_ACC3) {
		R24_NO_OF_ACC3 = r24_NO_OF_ACC3;
	}





	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}





	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}





	public BigDecimal getR25_180D_ABOVE_PASTDUE() {
		return R25_180D_ABOVE_PASTDUE;
	}





	public void setR25_180D_ABOVE_PASTDUE(BigDecimal r25_180d_ABOVE_PASTDUE) {
		R25_180D_ABOVE_PASTDUE = r25_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR25_NON_ACCRUALS3() {
		return R25_NON_ACCRUALS3;
	}





	public void setR25_NON_ACCRUALS3(BigDecimal r25_NON_ACCRUALS3) {
		R25_NON_ACCRUALS3 = r25_NON_ACCRUALS3;
	}





	public BigDecimal getR25_SPECIFIC_PROV3() {
		return R25_SPECIFIC_PROV3;
	}





	public void setR25_SPECIFIC_PROV3(BigDecimal r25_SPECIFIC_PROV3) {
		R25_SPECIFIC_PROV3 = r25_SPECIFIC_PROV3;
	}





	public BigDecimal getR25_NO_OF_ACC3() {
		return R25_NO_OF_ACC3;
	}





	public void setR25_NO_OF_ACC3(BigDecimal r25_NO_OF_ACC3) {
		R25_NO_OF_ACC3 = r25_NO_OF_ACC3;
	}





	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}





	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}





	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}





	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}





	public BigDecimal getR27_180D_ABOVE_PASTDUE() {
		return R27_180D_ABOVE_PASTDUE;
	}





	public void setR27_180D_ABOVE_PASTDUE(BigDecimal r27_180d_ABOVE_PASTDUE) {
		R27_180D_ABOVE_PASTDUE = r27_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR27_NON_ACCRUALS3() {
		return R27_NON_ACCRUALS3;
	}





	public void setR27_NON_ACCRUALS3(BigDecimal r27_NON_ACCRUALS3) {
		R27_NON_ACCRUALS3 = r27_NON_ACCRUALS3;
	}





	public BigDecimal getR27_SPECIFIC_PROV3() {
		return R27_SPECIFIC_PROV3;
	}





	public void setR27_SPECIFIC_PROV3(BigDecimal r27_SPECIFIC_PROV3) {
		R27_SPECIFIC_PROV3 = r27_SPECIFIC_PROV3;
	}





	public BigDecimal getR27_NO_OF_ACC3() {
		return R27_NO_OF_ACC3;
	}





	public void setR27_NO_OF_ACC3(BigDecimal r27_NO_OF_ACC3) {
		R27_NO_OF_ACC3 = r27_NO_OF_ACC3;
	}





	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}





	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}





	public BigDecimal getR28_180D_ABOVE_PASTDUE() {
		return R28_180D_ABOVE_PASTDUE;
	}





	public void setR28_180D_ABOVE_PASTDUE(BigDecimal r28_180d_ABOVE_PASTDUE) {
		R28_180D_ABOVE_PASTDUE = r28_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR28_NON_ACCRUALS3() {
		return R28_NON_ACCRUALS3;
	}





	public void setR28_NON_ACCRUALS3(BigDecimal r28_NON_ACCRUALS3) {
		R28_NON_ACCRUALS3 = r28_NON_ACCRUALS3;
	}





	public BigDecimal getR28_SPECIFIC_PROV3() {
		return R28_SPECIFIC_PROV3;
	}





	public void setR28_SPECIFIC_PROV3(BigDecimal r28_SPECIFIC_PROV3) {
		R28_SPECIFIC_PROV3 = r28_SPECIFIC_PROV3;
	}





	public BigDecimal getR28_NO_OF_ACC3() {
		return R28_NO_OF_ACC3;
	}





	public void setR28_NO_OF_ACC3(BigDecimal r28_NO_OF_ACC3) {
		R28_NO_OF_ACC3 = r28_NO_OF_ACC3;
	}





	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}





	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}





	public BigDecimal getR29_180D_ABOVE_PASTDUE() {
		return R29_180D_ABOVE_PASTDUE;
	}





	public void setR29_180D_ABOVE_PASTDUE(BigDecimal r29_180d_ABOVE_PASTDUE) {
		R29_180D_ABOVE_PASTDUE = r29_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR29_NON_ACCRUALS3() {
		return R29_NON_ACCRUALS3;
	}





	public void setR29_NON_ACCRUALS3(BigDecimal r29_NON_ACCRUALS3) {
		R29_NON_ACCRUALS3 = r29_NON_ACCRUALS3;
	}





	public BigDecimal getR29_SPECIFIC_PROV3() {
		return R29_SPECIFIC_PROV3;
	}





	public void setR29_SPECIFIC_PROV3(BigDecimal r29_SPECIFIC_PROV3) {
		R29_SPECIFIC_PROV3 = r29_SPECIFIC_PROV3;
	}





	public BigDecimal getR29_NO_OF_ACC3() {
		return R29_NO_OF_ACC3;
	}





	public void setR29_NO_OF_ACC3(BigDecimal r29_NO_OF_ACC3) {
		R29_NO_OF_ACC3 = r29_NO_OF_ACC3;
	}





	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}





	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}





	public BigDecimal getR30_180D_ABOVE_PASTDUE() {
		return R30_180D_ABOVE_PASTDUE;
	}





	public void setR30_180D_ABOVE_PASTDUE(BigDecimal r30_180d_ABOVE_PASTDUE) {
		R30_180D_ABOVE_PASTDUE = r30_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR30_NON_ACCRUALS3() {
		return R30_NON_ACCRUALS3;
	}





	public void setR30_NON_ACCRUALS3(BigDecimal r30_NON_ACCRUALS3) {
		R30_NON_ACCRUALS3 = r30_NON_ACCRUALS3;
	}





	public BigDecimal getR30_SPECIFIC_PROV3() {
		return R30_SPECIFIC_PROV3;
	}





	public void setR30_SPECIFIC_PROV3(BigDecimal r30_SPECIFIC_PROV3) {
		R30_SPECIFIC_PROV3 = r30_SPECIFIC_PROV3;
	}





	public BigDecimal getR30_NO_OF_ACC3() {
		return R30_NO_OF_ACC3;
	}





	public void setR30_NO_OF_ACC3(BigDecimal r30_NO_OF_ACC3) {
		R30_NO_OF_ACC3 = r30_NO_OF_ACC3;
	}





	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}





	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}





	public BigDecimal getR31_180D_ABOVE_PASTDUE() {
		return R31_180D_ABOVE_PASTDUE;
	}





	public void setR31_180D_ABOVE_PASTDUE(BigDecimal r31_180d_ABOVE_PASTDUE) {
		R31_180D_ABOVE_PASTDUE = r31_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR31_NON_ACCRUALS3() {
		return R31_NON_ACCRUALS3;
	}





	public void setR31_NON_ACCRUALS3(BigDecimal r31_NON_ACCRUALS3) {
		R31_NON_ACCRUALS3 = r31_NON_ACCRUALS3;
	}





	public BigDecimal getR31_SPECIFIC_PROV3() {
		return R31_SPECIFIC_PROV3;
	}





	public void setR31_SPECIFIC_PROV3(BigDecimal r31_SPECIFIC_PROV3) {
		R31_SPECIFIC_PROV3 = r31_SPECIFIC_PROV3;
	}





	public BigDecimal getR31_NO_OF_ACC3() {
		return R31_NO_OF_ACC3;
	}





	public void setR31_NO_OF_ACC3(BigDecimal r31_NO_OF_ACC3) {
		R31_NO_OF_ACC3 = r31_NO_OF_ACC3;
	}





	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}





	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}





	public BigDecimal getR32_180D_ABOVE_PASTDUE() {
		return R32_180D_ABOVE_PASTDUE;
	}





	public void setR32_180D_ABOVE_PASTDUE(BigDecimal r32_180d_ABOVE_PASTDUE) {
		R32_180D_ABOVE_PASTDUE = r32_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR32_NON_ACCRUALS3() {
		return R32_NON_ACCRUALS3;
	}





	public void setR32_NON_ACCRUALS3(BigDecimal r32_NON_ACCRUALS3) {
		R32_NON_ACCRUALS3 = r32_NON_ACCRUALS3;
	}





	public BigDecimal getR32_SPECIFIC_PROV3() {
		return R32_SPECIFIC_PROV3;
	}





	public void setR32_SPECIFIC_PROV3(BigDecimal r32_SPECIFIC_PROV3) {
		R32_SPECIFIC_PROV3 = r32_SPECIFIC_PROV3;
	}





	public BigDecimal getR32_NO_OF_ACC3() {
		return R32_NO_OF_ACC3;
	}





	public void setR32_NO_OF_ACC3(BigDecimal r32_NO_OF_ACC3) {
		R32_NO_OF_ACC3 = r32_NO_OF_ACC3;
	}





	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}





	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}





	public BigDecimal getR33_180D_ABOVE_PASTDUE() {
		return R33_180D_ABOVE_PASTDUE;
	}





	public void setR33_180D_ABOVE_PASTDUE(BigDecimal r33_180d_ABOVE_PASTDUE) {
		R33_180D_ABOVE_PASTDUE = r33_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR33_NON_ACCRUALS3() {
		return R33_NON_ACCRUALS3;
	}





	public void setR33_NON_ACCRUALS3(BigDecimal r33_NON_ACCRUALS3) {
		R33_NON_ACCRUALS3 = r33_NON_ACCRUALS3;
	}





	public BigDecimal getR33_SPECIFIC_PROV3() {
		return R33_SPECIFIC_PROV3;
	}





	public void setR33_SPECIFIC_PROV3(BigDecimal r33_SPECIFIC_PROV3) {
		R33_SPECIFIC_PROV3 = r33_SPECIFIC_PROV3;
	}





	public BigDecimal getR33_NO_OF_ACC3() {
		return R33_NO_OF_ACC3;
	}





	public void setR33_NO_OF_ACC3(BigDecimal r33_NO_OF_ACC3) {
		R33_NO_OF_ACC3 = r33_NO_OF_ACC3;
	}





	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}





	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}





	public BigDecimal getR34_180D_ABOVE_PASTDUE() {
		return R34_180D_ABOVE_PASTDUE;
	}





	public void setR34_180D_ABOVE_PASTDUE(BigDecimal r34_180d_ABOVE_PASTDUE) {
		R34_180D_ABOVE_PASTDUE = r34_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR34_NON_ACCRUALS3() {
		return R34_NON_ACCRUALS3;
	}





	public void setR34_NON_ACCRUALS3(BigDecimal r34_NON_ACCRUALS3) {
		R34_NON_ACCRUALS3 = r34_NON_ACCRUALS3;
	}





	public BigDecimal getR34_SPECIFIC_PROV3() {
		return R34_SPECIFIC_PROV3;
	}





	public void setR34_SPECIFIC_PROV3(BigDecimal r34_SPECIFIC_PROV3) {
		R34_SPECIFIC_PROV3 = r34_SPECIFIC_PROV3;
	}





	public BigDecimal getR34_NO_OF_ACC3() {
		return R34_NO_OF_ACC3;
	}





	public void setR34_NO_OF_ACC3(BigDecimal r34_NO_OF_ACC3) {
		R34_NO_OF_ACC3 = r34_NO_OF_ACC3;
	}





	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}





	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}





	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}





	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}





	public BigDecimal getR36_180D_ABOVE_PASTDUE() {
		return R36_180D_ABOVE_PASTDUE;
	}





	public void setR36_180D_ABOVE_PASTDUE(BigDecimal r36_180d_ABOVE_PASTDUE) {
		R36_180D_ABOVE_PASTDUE = r36_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR36_NON_ACCRUALS3() {
		return R36_NON_ACCRUALS3;
	}





	public void setR36_NON_ACCRUALS3(BigDecimal r36_NON_ACCRUALS3) {
		R36_NON_ACCRUALS3 = r36_NON_ACCRUALS3;
	}





	public BigDecimal getR36_SPECIFIC_PROV3() {
		return R36_SPECIFIC_PROV3;
	}





	public void setR36_SPECIFIC_PROV3(BigDecimal r36_SPECIFIC_PROV3) {
		R36_SPECIFIC_PROV3 = r36_SPECIFIC_PROV3;
	}





	public BigDecimal getR36_NO_OF_ACC3() {
		return R36_NO_OF_ACC3;
	}





	public void setR36_NO_OF_ACC3(BigDecimal r36_NO_OF_ACC3) {
		R36_NO_OF_ACC3 = r36_NO_OF_ACC3;
	}





	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}





	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}





	public BigDecimal getR37_180D_ABOVE_PASTDUE() {
		return R37_180D_ABOVE_PASTDUE;
	}





	public void setR37_180D_ABOVE_PASTDUE(BigDecimal r37_180d_ABOVE_PASTDUE) {
		R37_180D_ABOVE_PASTDUE = r37_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR37_NON_ACCRUALS3() {
		return R37_NON_ACCRUALS3;
	}





	public void setR37_NON_ACCRUALS3(BigDecimal r37_NON_ACCRUALS3) {
		R37_NON_ACCRUALS3 = r37_NON_ACCRUALS3;
	}





	public BigDecimal getR37_SPECIFIC_PROV3() {
		return R37_SPECIFIC_PROV3;
	}





	public void setR37_SPECIFIC_PROV3(BigDecimal r37_SPECIFIC_PROV3) {
		R37_SPECIFIC_PROV3 = r37_SPECIFIC_PROV3;
	}





	public BigDecimal getR37_NO_OF_ACC3() {
		return R37_NO_OF_ACC3;
	}





	public void setR37_NO_OF_ACC3(BigDecimal r37_NO_OF_ACC3) {
		R37_NO_OF_ACC3 = r37_NO_OF_ACC3;
	}





	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}





	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}





	public BigDecimal getR39_180D_ABOVE_PASTDUE() {
		return R39_180D_ABOVE_PASTDUE;
	}





	public void setR39_180D_ABOVE_PASTDUE(BigDecimal r39_180d_ABOVE_PASTDUE) {
		R39_180D_ABOVE_PASTDUE = r39_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR39_NON_ACCRUALS3() {
		return R39_NON_ACCRUALS3;
	}





	public void setR39_NON_ACCRUALS3(BigDecimal r39_NON_ACCRUALS3) {
		R39_NON_ACCRUALS3 = r39_NON_ACCRUALS3;
	}





	public BigDecimal getR39_SPECIFIC_PROV3() {
		return R39_SPECIFIC_PROV3;
	}





	public void setR39_SPECIFIC_PROV3(BigDecimal r39_SPECIFIC_PROV3) {
		R39_SPECIFIC_PROV3 = r39_SPECIFIC_PROV3;
	}





	public BigDecimal getR39_NO_OF_ACC3() {
		return R39_NO_OF_ACC3;
	}





	public void setR39_NO_OF_ACC3(BigDecimal r39_NO_OF_ACC3) {
		R39_NO_OF_ACC3 = r39_NO_OF_ACC3;
	}





	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}





	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}





	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}





	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}





	public BigDecimal getR40_180D_ABOVE_PASTDUE() {
		return R40_180D_ABOVE_PASTDUE;
	}





	public void setR40_180D_ABOVE_PASTDUE(BigDecimal r40_180d_ABOVE_PASTDUE) {
		R40_180D_ABOVE_PASTDUE = r40_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR40_NON_ACCRUALS3() {
		return R40_NON_ACCRUALS3;
	}





	public void setR40_NON_ACCRUALS3(BigDecimal r40_NON_ACCRUALS3) {
		R40_NON_ACCRUALS3 = r40_NON_ACCRUALS3;
	}





	public BigDecimal getR40_SPECIFIC_PROV3() {
		return R40_SPECIFIC_PROV3;
	}





	public void setR40_SPECIFIC_PROV3(BigDecimal r40_SPECIFIC_PROV3) {
		R40_SPECIFIC_PROV3 = r40_SPECIFIC_PROV3;
	}





	public BigDecimal getR40_NO_OF_ACC3() {
		return R40_NO_OF_ACC3;
	}





	public void setR40_NO_OF_ACC3(BigDecimal r40_NO_OF_ACC3) {
		R40_NO_OF_ACC3 = r40_NO_OF_ACC3;
	}





	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}





	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}





	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}





	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}





	public BigDecimal getR42_180D_ABOVE_PASTDUE() {
		return R42_180D_ABOVE_PASTDUE;
	}





	public void setR42_180D_ABOVE_PASTDUE(BigDecimal r42_180d_ABOVE_PASTDUE) {
		R42_180D_ABOVE_PASTDUE = r42_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR42_NON_ACCRUALS3() {
		return R42_NON_ACCRUALS3;
	}





	public void setR42_NON_ACCRUALS3(BigDecimal r42_NON_ACCRUALS3) {
		R42_NON_ACCRUALS3 = r42_NON_ACCRUALS3;
	}





	public BigDecimal getR42_SPECIFIC_PROV3() {
		return R42_SPECIFIC_PROV3;
	}





	public void setR42_SPECIFIC_PROV3(BigDecimal r42_SPECIFIC_PROV3) {
		R42_SPECIFIC_PROV3 = r42_SPECIFIC_PROV3;
	}





	public BigDecimal getR42_NO_OF_ACC3() {
		return R42_NO_OF_ACC3;
	}





	public void setR42_NO_OF_ACC3(BigDecimal r42_NO_OF_ACC3) {
		R42_NO_OF_ACC3 = r42_NO_OF_ACC3;
	}





	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}





	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}





	public BigDecimal getR43_180D_ABOVE_PASTDUE() {
		return R43_180D_ABOVE_PASTDUE;
	}





	public void setR43_180D_ABOVE_PASTDUE(BigDecimal r43_180d_ABOVE_PASTDUE) {
		R43_180D_ABOVE_PASTDUE = r43_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR43_NON_ACCRUALS3() {
		return R43_NON_ACCRUALS3;
	}





	public void setR43_NON_ACCRUALS3(BigDecimal r43_NON_ACCRUALS3) {
		R43_NON_ACCRUALS3 = r43_NON_ACCRUALS3;
	}





	public BigDecimal getR43_SPECIFIC_PROV3() {
		return R43_SPECIFIC_PROV3;
	}





	public void setR43_SPECIFIC_PROV3(BigDecimal r43_SPECIFIC_PROV3) {
		R43_SPECIFIC_PROV3 = r43_SPECIFIC_PROV3;
	}





	public BigDecimal getR43_NO_OF_ACC3() {
		return R43_NO_OF_ACC3;
	}





	public void setR43_NO_OF_ACC3(BigDecimal r43_NO_OF_ACC3) {
		R43_NO_OF_ACC3 = r43_NO_OF_ACC3;
	}





	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}





	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}





	public BigDecimal getR44_180D_ABOVE_PASTDUE() {
		return R44_180D_ABOVE_PASTDUE;
	}





	public void setR44_180D_ABOVE_PASTDUE(BigDecimal r44_180d_ABOVE_PASTDUE) {
		R44_180D_ABOVE_PASTDUE = r44_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR44_NON_ACCRUALS3() {
		return R44_NON_ACCRUALS3;
	}





	public void setR44_NON_ACCRUALS3(BigDecimal r44_NON_ACCRUALS3) {
		R44_NON_ACCRUALS3 = r44_NON_ACCRUALS3;
	}





	public BigDecimal getR44_SPECIFIC_PROV3() {
		return R44_SPECIFIC_PROV3;
	}





	public void setR44_SPECIFIC_PROV3(BigDecimal r44_SPECIFIC_PROV3) {
		R44_SPECIFIC_PROV3 = r44_SPECIFIC_PROV3;
	}





	public BigDecimal getR44_NO_OF_ACC3() {
		return R44_NO_OF_ACC3;
	}





	public void setR44_NO_OF_ACC3(BigDecimal r44_NO_OF_ACC3) {
		R44_NO_OF_ACC3 = r44_NO_OF_ACC3;
	}





	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}





	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}





	public BigDecimal getR45_180D_ABOVE_PASTDUE() {
		return R45_180D_ABOVE_PASTDUE;
	}





	public void setR45_180D_ABOVE_PASTDUE(BigDecimal r45_180d_ABOVE_PASTDUE) {
		R45_180D_ABOVE_PASTDUE = r45_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR45_NON_ACCRUALS3() {
		return R45_NON_ACCRUALS3;
	}





	public void setR45_NON_ACCRUALS3(BigDecimal r45_NON_ACCRUALS3) {
		R45_NON_ACCRUALS3 = r45_NON_ACCRUALS3;
	}





	public BigDecimal getR45_SPECIFIC_PROV3() {
		return R45_SPECIFIC_PROV3;
	}





	public void setR45_SPECIFIC_PROV3(BigDecimal r45_SPECIFIC_PROV3) {
		R45_SPECIFIC_PROV3 = r45_SPECIFIC_PROV3;
	}





	public BigDecimal getR45_NO_OF_ACC3() {
		return R45_NO_OF_ACC3;
	}





	public void setR45_NO_OF_ACC3(BigDecimal r45_NO_OF_ACC3) {
		R45_NO_OF_ACC3 = r45_NO_OF_ACC3;
	}





	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}





	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}





	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}





	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}





	public BigDecimal getR47_180D_ABOVE_PASTDUE() {
		return R47_180D_ABOVE_PASTDUE;
	}





	public void setR47_180D_ABOVE_PASTDUE(BigDecimal r47_180d_ABOVE_PASTDUE) {
		R47_180D_ABOVE_PASTDUE = r47_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR47_NON_ACCRUALS3() {
		return R47_NON_ACCRUALS3;
	}





	public void setR47_NON_ACCRUALS3(BigDecimal r47_NON_ACCRUALS3) {
		R47_NON_ACCRUALS3 = r47_NON_ACCRUALS3;
	}





	public BigDecimal getR47_SPECIFIC_PROV3() {
		return R47_SPECIFIC_PROV3;
	}





	public void setR47_SPECIFIC_PROV3(BigDecimal r47_SPECIFIC_PROV3) {
		R47_SPECIFIC_PROV3 = r47_SPECIFIC_PROV3;
	}





	public BigDecimal getR47_NO_OF_ACC3() {
		return R47_NO_OF_ACC3;
	}





	public void setR47_NO_OF_ACC3(BigDecimal r47_NO_OF_ACC3) {
		R47_NO_OF_ACC3 = r47_NO_OF_ACC3;
	}





	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}





	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}





	public BigDecimal getR48_180D_ABOVE_PASTDUE() {
		return R48_180D_ABOVE_PASTDUE;
	}





	public void setR48_180D_ABOVE_PASTDUE(BigDecimal r48_180d_ABOVE_PASTDUE) {
		R48_180D_ABOVE_PASTDUE = r48_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR48_NON_ACCRUALS3() {
		return R48_NON_ACCRUALS3;
	}





	public void setR48_NON_ACCRUALS3(BigDecimal r48_NON_ACCRUALS3) {
		R48_NON_ACCRUALS3 = r48_NON_ACCRUALS3;
	}





	public BigDecimal getR48_SPECIFIC_PROV3() {
		return R48_SPECIFIC_PROV3;
	}





	public void setR48_SPECIFIC_PROV3(BigDecimal r48_SPECIFIC_PROV3) {
		R48_SPECIFIC_PROV3 = r48_SPECIFIC_PROV3;
	}





	public BigDecimal getR48_NO_OF_ACC3() {
		return R48_NO_OF_ACC3;
	}





	public void setR48_NO_OF_ACC3(BigDecimal r48_NO_OF_ACC3) {
		R48_NO_OF_ACC3 = r48_NO_OF_ACC3;
	}





	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}





	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}





	public BigDecimal getR49_180D_ABOVE_PASTDUE() {
		return R49_180D_ABOVE_PASTDUE;
	}





	public void setR49_180D_ABOVE_PASTDUE(BigDecimal r49_180d_ABOVE_PASTDUE) {
		R49_180D_ABOVE_PASTDUE = r49_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR49_NON_ACCRUALS3() {
		return R49_NON_ACCRUALS3;
	}





	public void setR49_NON_ACCRUALS3(BigDecimal r49_NON_ACCRUALS3) {
		R49_NON_ACCRUALS3 = r49_NON_ACCRUALS3;
	}





	public BigDecimal getR49_SPECIFIC_PROV3() {
		return R49_SPECIFIC_PROV3;
	}





	public void setR49_SPECIFIC_PROV3(BigDecimal r49_SPECIFIC_PROV3) {
		R49_SPECIFIC_PROV3 = r49_SPECIFIC_PROV3;
	}





	public BigDecimal getR49_NO_OF_ACC3() {
		return R49_NO_OF_ACC3;
	}





	public void setR49_NO_OF_ACC3(BigDecimal r49_NO_OF_ACC3) {
		R49_NO_OF_ACC3 = r49_NO_OF_ACC3;
	}





	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}





	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}





	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}





	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}





	public BigDecimal getR51_180D_ABOVE_PASTDUE() {
		return R51_180D_ABOVE_PASTDUE;
	}





	public void setR51_180D_ABOVE_PASTDUE(BigDecimal r51_180d_ABOVE_PASTDUE) {
		R51_180D_ABOVE_PASTDUE = r51_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR51_NON_ACCRUALS3() {
		return R51_NON_ACCRUALS3;
	}





	public void setR51_NON_ACCRUALS3(BigDecimal r51_NON_ACCRUALS3) {
		R51_NON_ACCRUALS3 = r51_NON_ACCRUALS3;
	}





	public BigDecimal getR51_SPECIFIC_PROV3() {
		return R51_SPECIFIC_PROV3;
	}





	public void setR51_SPECIFIC_PROV3(BigDecimal r51_SPECIFIC_PROV3) {
		R51_SPECIFIC_PROV3 = r51_SPECIFIC_PROV3;
	}





	public BigDecimal getR51_NO_OF_ACC3() {
		return R51_NO_OF_ACC3;
	}





	public void setR51_NO_OF_ACC3(BigDecimal r51_NO_OF_ACC3) {
		R51_NO_OF_ACC3 = r51_NO_OF_ACC3;
	}





	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}





	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}





	public BigDecimal getR52_180D_ABOVE_PASTDUE() {
		return R52_180D_ABOVE_PASTDUE;
	}





	public void setR52_180D_ABOVE_PASTDUE(BigDecimal r52_180d_ABOVE_PASTDUE) {
		R52_180D_ABOVE_PASTDUE = r52_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR52_NON_ACCRUALS3() {
		return R52_NON_ACCRUALS3;
	}





	public void setR52_NON_ACCRUALS3(BigDecimal r52_NON_ACCRUALS3) {
		R52_NON_ACCRUALS3 = r52_NON_ACCRUALS3;
	}





	public BigDecimal getR52_SPECIFIC_PROV3() {
		return R52_SPECIFIC_PROV3;
	}





	public void setR52_SPECIFIC_PROV3(BigDecimal r52_SPECIFIC_PROV3) {
		R52_SPECIFIC_PROV3 = r52_SPECIFIC_PROV3;
	}





	public BigDecimal getR52_NO_OF_ACC3() {
		return R52_NO_OF_ACC3;
	}





	public void setR52_NO_OF_ACC3(BigDecimal r52_NO_OF_ACC3) {
		R52_NO_OF_ACC3 = r52_NO_OF_ACC3;
	}





	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}





	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}





	public BigDecimal getR53_180D_ABOVE_PASTDUE() {
		return R53_180D_ABOVE_PASTDUE;
	}





	public void setR53_180D_ABOVE_PASTDUE(BigDecimal r53_180d_ABOVE_PASTDUE) {
		R53_180D_ABOVE_PASTDUE = r53_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR53_NON_ACCRUALS3() {
		return R53_NON_ACCRUALS3;
	}





	public void setR53_NON_ACCRUALS3(BigDecimal r53_NON_ACCRUALS3) {
		R53_NON_ACCRUALS3 = r53_NON_ACCRUALS3;
	}





	public BigDecimal getR53_SPECIFIC_PROV3() {
		return R53_SPECIFIC_PROV3;
	}





	public void setR53_SPECIFIC_PROV3(BigDecimal r53_SPECIFIC_PROV3) {
		R53_SPECIFIC_PROV3 = r53_SPECIFIC_PROV3;
	}





	public BigDecimal getR53_NO_OF_ACC3() {
		return R53_NO_OF_ACC3;
	}





	public void setR53_NO_OF_ACC3(BigDecimal r53_NO_OF_ACC3) {
		R53_NO_OF_ACC3 = r53_NO_OF_ACC3;
	}





	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}





	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}





	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}





	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}





	public BigDecimal getR55_180D_ABOVE_PASTDUE() {
		return R55_180D_ABOVE_PASTDUE;
	}





	public void setR55_180D_ABOVE_PASTDUE(BigDecimal r55_180d_ABOVE_PASTDUE) {
		R55_180D_ABOVE_PASTDUE = r55_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR55_NON_ACCRUALS3() {
		return R55_NON_ACCRUALS3;
	}





	public void setR55_NON_ACCRUALS3(BigDecimal r55_NON_ACCRUALS3) {
		R55_NON_ACCRUALS3 = r55_NON_ACCRUALS3;
	}





	public BigDecimal getR55_SPECIFIC_PROV3() {
		return R55_SPECIFIC_PROV3;
	}





	public void setR55_SPECIFIC_PROV3(BigDecimal r55_SPECIFIC_PROV3) {
		R55_SPECIFIC_PROV3 = r55_SPECIFIC_PROV3;
	}





	public BigDecimal getR55_NO_OF_ACC3() {
		return R55_NO_OF_ACC3;
	}





	public void setR55_NO_OF_ACC3(BigDecimal r55_NO_OF_ACC3) {
		R55_NO_OF_ACC3 = r55_NO_OF_ACC3;
	}





	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}





	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}





	public BigDecimal getR56_180D_ABOVE_PASTDUE() {
		return R56_180D_ABOVE_PASTDUE;
	}





	public void setR56_180D_ABOVE_PASTDUE(BigDecimal r56_180d_ABOVE_PASTDUE) {
		R56_180D_ABOVE_PASTDUE = r56_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR56_NON_ACCRUALS3() {
		return R56_NON_ACCRUALS3;
	}





	public void setR56_NON_ACCRUALS3(BigDecimal r56_NON_ACCRUALS3) {
		R56_NON_ACCRUALS3 = r56_NON_ACCRUALS3;
	}





	public BigDecimal getR56_SPECIFIC_PROV3() {
		return R56_SPECIFIC_PROV3;
	}





	public void setR56_SPECIFIC_PROV3(BigDecimal r56_SPECIFIC_PROV3) {
		R56_SPECIFIC_PROV3 = r56_SPECIFIC_PROV3;
	}





	public BigDecimal getR56_NO_OF_ACC3() {
		return R56_NO_OF_ACC3;
	}





	public void setR56_NO_OF_ACC3(BigDecimal r56_NO_OF_ACC3) {
		R56_NO_OF_ACC3 = r56_NO_OF_ACC3;
	}





	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}





	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}





	public BigDecimal getR57_180D_ABOVE_PASTDUE() {
		return R57_180D_ABOVE_PASTDUE;
	}





	public void setR57_180D_ABOVE_PASTDUE(BigDecimal r57_180d_ABOVE_PASTDUE) {
		R57_180D_ABOVE_PASTDUE = r57_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR57_NON_ACCRUALS3() {
		return R57_NON_ACCRUALS3;
	}





	public void setR57_NON_ACCRUALS3(BigDecimal r57_NON_ACCRUALS3) {
		R57_NON_ACCRUALS3 = r57_NON_ACCRUALS3;
	}





	public BigDecimal getR57_SPECIFIC_PROV3() {
		return R57_SPECIFIC_PROV3;
	}





	public void setR57_SPECIFIC_PROV3(BigDecimal r57_SPECIFIC_PROV3) {
		R57_SPECIFIC_PROV3 = r57_SPECIFIC_PROV3;
	}





	public BigDecimal getR57_NO_OF_ACC3() {
		return R57_NO_OF_ACC3;
	}





	public void setR57_NO_OF_ACC3(BigDecimal r57_NO_OF_ACC3) {
		R57_NO_OF_ACC3 = r57_NO_OF_ACC3;
	}





	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}





	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}





	public BigDecimal getR58_180D_ABOVE_PASTDUE() {
		return R58_180D_ABOVE_PASTDUE;
	}





	public void setR58_180D_ABOVE_PASTDUE(BigDecimal r58_180d_ABOVE_PASTDUE) {
		R58_180D_ABOVE_PASTDUE = r58_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR58_NON_ACCRUALS3() {
		return R58_NON_ACCRUALS3;
	}





	public void setR58_NON_ACCRUALS3(BigDecimal r58_NON_ACCRUALS3) {
		R58_NON_ACCRUALS3 = r58_NON_ACCRUALS3;
	}





	public BigDecimal getR58_SPECIFIC_PROV3() {
		return R58_SPECIFIC_PROV3;
	}





	public void setR58_SPECIFIC_PROV3(BigDecimal r58_SPECIFIC_PROV3) {
		R58_SPECIFIC_PROV3 = r58_SPECIFIC_PROV3;
	}





	public BigDecimal getR58_NO_OF_ACC3() {
		return R58_NO_OF_ACC3;
	}





	public void setR58_NO_OF_ACC3(BigDecimal r58_NO_OF_ACC3) {
		R58_NO_OF_ACC3 = r58_NO_OF_ACC3;
	}





	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}





	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}





	public BigDecimal getR59_180D_ABOVE_PASTDUE() {
		return R59_180D_ABOVE_PASTDUE;
	}





	public void setR59_180D_ABOVE_PASTDUE(BigDecimal r59_180d_ABOVE_PASTDUE) {
		R59_180D_ABOVE_PASTDUE = r59_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR59_NON_ACCRUALS3() {
		return R59_NON_ACCRUALS3;
	}





	public void setR59_NON_ACCRUALS3(BigDecimal r59_NON_ACCRUALS3) {
		R59_NON_ACCRUALS3 = r59_NON_ACCRUALS3;
	}





	public BigDecimal getR59_SPECIFIC_PROV3() {
		return R59_SPECIFIC_PROV3;
	}





	public void setR59_SPECIFIC_PROV3(BigDecimal r59_SPECIFIC_PROV3) {
		R59_SPECIFIC_PROV3 = r59_SPECIFIC_PROV3;
	}





	public BigDecimal getR59_NO_OF_ACC3() {
		return R59_NO_OF_ACC3;
	}





	public void setR59_NO_OF_ACC3(BigDecimal r59_NO_OF_ACC3) {
		R59_NO_OF_ACC3 = r59_NO_OF_ACC3;
	}





	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}





	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}





	public BigDecimal getR60_180D_ABOVE_PASTDUE() {
		return R60_180D_ABOVE_PASTDUE;
	}





	public void setR60_180D_ABOVE_PASTDUE(BigDecimal r60_180d_ABOVE_PASTDUE) {
		R60_180D_ABOVE_PASTDUE = r60_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR60_NON_ACCRUALS3() {
		return R60_NON_ACCRUALS3;
	}





	public void setR60_NON_ACCRUALS3(BigDecimal r60_NON_ACCRUALS3) {
		R60_NON_ACCRUALS3 = r60_NON_ACCRUALS3;
	}





	public BigDecimal getR60_SPECIFIC_PROV3() {
		return R60_SPECIFIC_PROV3;
	}





	public void setR60_SPECIFIC_PROV3(BigDecimal r60_SPECIFIC_PROV3) {
		R60_SPECIFIC_PROV3 = r60_SPECIFIC_PROV3;
	}





	public BigDecimal getR60_NO_OF_ACC3() {
		return R60_NO_OF_ACC3;
	}





	public void setR60_NO_OF_ACC3(BigDecimal r60_NO_OF_ACC3) {
		R60_NO_OF_ACC3 = r60_NO_OF_ACC3;
	}





	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}





	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}





	public M_PD_Summary_Entity() {
        super();
    }

}