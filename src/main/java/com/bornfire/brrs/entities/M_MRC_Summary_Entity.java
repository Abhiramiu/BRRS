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
@Table(name = "BRRS_M_MRC_SUMMARYTABLE")

public class M_MRC_Summary_Entity {
	
	private BigDecimal R9_LINE;
    private String R9_PRODUCT;
    private BigDecimal R9_TOTAL;

    private BigDecimal R10_LINE;
    private String R10_PRODUCT;
    private BigDecimal R10_TOTAL;

    private BigDecimal R11_LINE;
    private String R11_PRODUCT;
    private BigDecimal R11_TOTAL;

    private BigDecimal R12_LINE;
    private String R12_PRODUCT;
    private BigDecimal R12_TOTAL;

    private BigDecimal R13_LINE;
    private String R13_PRODUCT;
    private BigDecimal R13_TOTAL;

    private BigDecimal R14_LINE;
    private String R14_PRODUCT;
    private BigDecimal R14_TOTAL;

    private BigDecimal R15_LINE;
    private String R15_PRODUCT;
    private BigDecimal R15_TOTAL;

    private BigDecimal R16_LINE;
    private String R16_PRODUCT;
    private BigDecimal R16_TOTAL;

    private BigDecimal R17_LINE;
    private String R17_PRODUCT;
    private BigDecimal R17_TOTAL;

    private BigDecimal R18_LINE;
    private String R18_PRODUCT;
    private BigDecimal R18_TOTAL;

    private BigDecimal R19_LINE;
    private String R19_PRODUCT;
    private BigDecimal R19_TOTAL;

    private BigDecimal R20_LINE;
    private String R20_PRODUCT;
    private BigDecimal R20_TOTAL;

    private BigDecimal R21_LINE;
    private String R21_PRODUCT;
    private BigDecimal R21_TOTAL;

    private BigDecimal R22_LINE;
    private String R22_PRODUCT;
    private BigDecimal R22_TOTAL;

    private BigDecimal R23_LINE;
    private String R23_PRODUCT;
    private BigDecimal R23_TOTAL;

    private BigDecimal R24_LINE;
    private String R24_PRODUCT;
    private BigDecimal R24_TOTAL;

    private BigDecimal R25_LINE;
    private String R25_PRODUCT;
    private BigDecimal R25_TOTAL;

    private BigDecimal R26_LINE;
    private String R26_PRODUCT;
    private BigDecimal R26_TOTAL;

    private BigDecimal R27_LINE;
    private String R27_PRODUCT;
    private BigDecimal R27_TOTAL;

    private BigDecimal R28_LINE;
    private String R28_PRODUCT;
    private BigDecimal R28_TOTAL;

    private BigDecimal R29_LINE;
    private String R29_PRODUCT;
    private BigDecimal R29_TOTAL;

    private BigDecimal R30_LINE;
    private String R30_PRODUCT;
    private BigDecimal R30_TOTAL;

    private BigDecimal R31_LINE;
    private String R31_PRODUCT;
    private BigDecimal R31_TOTAL;

    private BigDecimal R32_LINE;
    private String R32_PRODUCT;
    private BigDecimal R32_TOTAL;

    private BigDecimal R33_LINE;
    private String R33_PRODUCT;
    private BigDecimal R33_TOTAL;

    private BigDecimal R34_LINE;
    private String R34_PRODUCT;
    private BigDecimal R34_TOTAL;

    private BigDecimal R35_LINE;
    private String R35_PRODUCT;
    private BigDecimal R35_TOTAL;

    private BigDecimal R36_LINE;
    private String R36_PRODUCT;
    private BigDecimal R36_TOTAL;
    
    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	public Date REPORT_DATE;
    public String REPORT_VERSION;
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;
	public BigDecimal getR9_LINE() {
		return R9_LINE;
	}
	public void setR9_LINE(BigDecimal r9_LINE) {
		R9_LINE = r9_LINE;
	}
	public String getR9_PRODUCT() {
		return R9_PRODUCT;
	}
	public void setR9_PRODUCT(String r9_PRODUCT) {
		R9_PRODUCT = r9_PRODUCT;
	}
	public BigDecimal getR9_TOTAL() {
		return R9_TOTAL;
	}
	public void setR9_TOTAL(BigDecimal r9_TOTAL) {
		R9_TOTAL = r9_TOTAL;
	}
	public BigDecimal getR10_LINE() {
		return R10_LINE;
	}
	public void setR10_LINE(BigDecimal r10_LINE) {
		R10_LINE = r10_LINE;
	}
	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}
	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}
	public BigDecimal getR10_TOTAL() {
		return R10_TOTAL;
	}
	public void setR10_TOTAL(BigDecimal r10_TOTAL) {
		R10_TOTAL = r10_TOTAL;
	}
	public BigDecimal getR11_LINE() {
		return R11_LINE;
	}
	public void setR11_LINE(BigDecimal r11_LINE) {
		R11_LINE = r11_LINE;
	}
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public BigDecimal getR12_LINE() {
		return R12_LINE;
	}
	public void setR12_LINE(BigDecimal r12_LINE) {
		R12_LINE = r12_LINE;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public BigDecimal getR13_LINE() {
		return R13_LINE;
	}
	public void setR13_LINE(BigDecimal r13_LINE) {
		R13_LINE = r13_LINE;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public BigDecimal getR14_LINE() {
		return R14_LINE;
	}
	public void setR14_LINE(BigDecimal r14_LINE) {
		R14_LINE = r14_LINE;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public BigDecimal getR15_LINE() {
		return R15_LINE;
	}
	public void setR15_LINE(BigDecimal r15_LINE) {
		R15_LINE = r15_LINE;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public BigDecimal getR16_LINE() {
		return R16_LINE;
	}
	public void setR16_LINE(BigDecimal r16_LINE) {
		R16_LINE = r16_LINE;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public BigDecimal getR17_LINE() {
		return R17_LINE;
	}
	public void setR17_LINE(BigDecimal r17_LINE) {
		R17_LINE = r17_LINE;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public BigDecimal getR18_LINE() {
		return R18_LINE;
	}
	public void setR18_LINE(BigDecimal r18_LINE) {
		R18_LINE = r18_LINE;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public BigDecimal getR19_LINE() {
		return R19_LINE;
	}
	public void setR19_LINE(BigDecimal r19_LINE) {
		R19_LINE = r19_LINE;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public BigDecimal getR20_LINE() {
		return R20_LINE;
	}
	public void setR20_LINE(BigDecimal r20_LINE) {
		R20_LINE = r20_LINE;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public BigDecimal getR21_LINE() {
		return R21_LINE;
	}
	public void setR21_LINE(BigDecimal r21_LINE) {
		R21_LINE = r21_LINE;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public BigDecimal getR22_LINE() {
		return R22_LINE;
	}
	public void setR22_LINE(BigDecimal r22_LINE) {
		R22_LINE = r22_LINE;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public BigDecimal getR23_LINE() {
		return R23_LINE;
	}
	public void setR23_LINE(BigDecimal r23_LINE) {
		R23_LINE = r23_LINE;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public BigDecimal getR24_LINE() {
		return R24_LINE;
	}
	public void setR24_LINE(BigDecimal r24_LINE) {
		R24_LINE = r24_LINE;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public BigDecimal getR25_LINE() {
		return R25_LINE;
	}
	public void setR25_LINE(BigDecimal r25_LINE) {
		R25_LINE = r25_LINE;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public BigDecimal getR26_LINE() {
		return R26_LINE;
	}
	public void setR26_LINE(BigDecimal r26_LINE) {
		R26_LINE = r26_LINE;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public BigDecimal getR27_LINE() {
		return R27_LINE;
	}
	public void setR27_LINE(BigDecimal r27_LINE) {
		R27_LINE = r27_LINE;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_TOTAL() {
		return R27_TOTAL;
	}
	public void setR27_TOTAL(BigDecimal r27_TOTAL) {
		R27_TOTAL = r27_TOTAL;
	}
	public BigDecimal getR28_LINE() {
		return R28_LINE;
	}
	public void setR28_LINE(BigDecimal r28_LINE) {
		R28_LINE = r28_LINE;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_TOTAL() {
		return R28_TOTAL;
	}
	public void setR28_TOTAL(BigDecimal r28_TOTAL) {
		R28_TOTAL = r28_TOTAL;
	}
	public BigDecimal getR29_LINE() {
		return R29_LINE;
	}
	public void setR29_LINE(BigDecimal r29_LINE) {
		R29_LINE = r29_LINE;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_TOTAL() {
		return R29_TOTAL;
	}
	public void setR29_TOTAL(BigDecimal r29_TOTAL) {
		R29_TOTAL = r29_TOTAL;
	}
	public BigDecimal getR30_LINE() {
		return R30_LINE;
	}
	public void setR30_LINE(BigDecimal r30_LINE) {
		R30_LINE = r30_LINE;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_TOTAL() {
		return R30_TOTAL;
	}
	public void setR30_TOTAL(BigDecimal r30_TOTAL) {
		R30_TOTAL = r30_TOTAL;
	}
	public BigDecimal getR31_LINE() {
		return R31_LINE;
	}
	public void setR31_LINE(BigDecimal r31_LINE) {
		R31_LINE = r31_LINE;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_TOTAL() {
		return R31_TOTAL;
	}
	public void setR31_TOTAL(BigDecimal r31_TOTAL) {
		R31_TOTAL = r31_TOTAL;
	}
	public BigDecimal getR32_LINE() {
		return R32_LINE;
	}
	public void setR32_LINE(BigDecimal r32_LINE) {
		R32_LINE = r32_LINE;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public BigDecimal getR33_LINE() {
		return R33_LINE;
	}
	public void setR33_LINE(BigDecimal r33_LINE) {
		R33_LINE = r33_LINE;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public BigDecimal getR34_LINE() {
		return R34_LINE;
	}
	public void setR34_LINE(BigDecimal r34_LINE) {
		R34_LINE = r34_LINE;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public BigDecimal getR35_LINE() {
		return R35_LINE;
	}
	public void setR35_LINE(BigDecimal r35_LINE) {
		R35_LINE = r35_LINE;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public BigDecimal getR36_LINE() {
		return R36_LINE;
	}
	public void setR36_LINE(BigDecimal r36_LINE) {
		R36_LINE = r36_LINE;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
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
	public M_MRC_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
	
    
    
    
    
}
    
    
    
    
