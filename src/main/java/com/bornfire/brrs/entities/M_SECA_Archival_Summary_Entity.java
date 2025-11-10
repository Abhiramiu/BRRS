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
@Table(name = "BRRS_M_SECA_ARCHIVAL_TABLE_SUMMARY")
public class M_SECA_Archival_Summary_Entity {

	private String R13_PRODUCT;
	private BigDecimal R13_EQUITY;
	private BigDecimal R13_BONDS;
	private BigDecimal R13_BOBC;
	private BigDecimal R13_TRES_BILLS;
	private BigDecimal R13_REPURCHASE_AGREE;
	private BigDecimal R13_COM_PAPER;
	private BigDecimal R13_CERT_OF_DEP;
	private BigDecimal R13_PLEDGED_ASSET;
	private BigDecimal R13_OTHER;
	private BigDecimal R13_TOTAL;
	private String R14_PRODUCT;
	private BigDecimal R14_EQUITY;
	private BigDecimal R14_BONDS;
	private BigDecimal R14_BOBC;
	private BigDecimal R14_TRES_BILLS;
	private BigDecimal R14_REPURCHASE_AGREE;
	private BigDecimal R14_COM_PAPER;
	private BigDecimal R14_CERT_OF_DEP;
	private BigDecimal R14_PLEDGED_ASSET;
	private BigDecimal R14_OTHER;
	private BigDecimal R14_TOTAL;
	private String R15_PRODUCT;
	private BigDecimal R15_EQUITY;
	private BigDecimal R15_BONDS;
	private BigDecimal R15_BOBC;
	private BigDecimal R15_TRES_BILLS;
	private BigDecimal R15_REPURCHASE_AGREE;
	private BigDecimal R15_COM_PAPER;
	private BigDecimal R15_CERT_OF_DEP;
	private BigDecimal R15_PLEDGED_ASSET;
	private BigDecimal R15_OTHER;
	private BigDecimal R15_TOTAL;
	private String R16_PRODUCT;
	private BigDecimal R16_EQUITY;
	private BigDecimal R16_BONDS;
	private BigDecimal R16_BOBC;
	private BigDecimal R16_TRES_BILLS;
	private BigDecimal R16_REPURCHASE_AGREE;
	private BigDecimal R16_COM_PAPER;
	private BigDecimal R16_CERT_OF_DEP;
	private BigDecimal R16_PLEDGED_ASSET;
	private BigDecimal R16_OTHER;
	private BigDecimal R16_TOTAL;
	private String R17_PRODUCT;
	private BigDecimal R17_EQUITY;
	private BigDecimal R17_BONDS;
	private BigDecimal R17_BOBC;
	private BigDecimal R17_TRES_BILLS;
	private BigDecimal R17_REPURCHASE_AGREE;
	private BigDecimal R17_COM_PAPER;
	private BigDecimal R17_CERT_OF_DEP;
	private BigDecimal R17_PLEDGED_ASSET;
	private BigDecimal R17_OTHER;
	private BigDecimal R17_TOTAL;
	private String R18_PRODUCT;
	private BigDecimal R18_EQUITY;
	private BigDecimal R18_BONDS;
	private BigDecimal R18_BOBC;
	private BigDecimal R18_TRES_BILLS;
	private BigDecimal R18_REPURCHASE_AGREE;
	private BigDecimal R18_COM_PAPER;
	private BigDecimal R18_CERT_OF_DEP;
	private BigDecimal R18_PLEDGED_ASSET;
	private BigDecimal R18_OTHER;
	private BigDecimal R18_TOTAL;
	private String R19_PRODUCT;
	private BigDecimal R19_EQUITY;
	private BigDecimal R19_BONDS;
	private BigDecimal R19_BOBC;
	private BigDecimal R19_TRES_BILLS;
	private BigDecimal R19_REPURCHASE_AGREE;
	private BigDecimal R19_COM_PAPER;
	private BigDecimal R19_CERT_OF_DEP;
	private BigDecimal R19_PLEDGED_ASSET;
	private BigDecimal R19_OTHER;
	private BigDecimal R19_TOTAL;
	private String R20_PRODUCT;
	private BigDecimal R20_EQUITY;
	private BigDecimal R20_BONDS;
	private BigDecimal R20_BOBC;
	private BigDecimal R20_TRES_BILLS;
	private BigDecimal R20_REPURCHASE_AGREE;
	private BigDecimal R20_COM_PAPER;
	private BigDecimal R20_CERT_OF_DEP;
	private BigDecimal R20_PLEDGED_ASSET;
	private BigDecimal R20_OTHER;
	private BigDecimal R20_TOTAL;
	private String R21_PRODUCT;
	private BigDecimal R21_EQUITY;
	private BigDecimal R21_BONDS;
	private BigDecimal R21_BOBC;
	private BigDecimal R21_TRES_BILLS;
	private BigDecimal R21_REPURCHASE_AGREE;
	private BigDecimal R21_COM_PAPER;
	private BigDecimal R21_CERT_OF_DEP;
	private BigDecimal R21_PLEDGED_ASSET;
	private BigDecimal R21_OTHER;
	private BigDecimal R21_TOTAL;
	private String R22_PRODUCT;
	private BigDecimal R22_EQUITY;
	private BigDecimal R22_BONDS;
	private BigDecimal R22_BOBC;
	private BigDecimal R22_TRES_BILLS;
	private BigDecimal R22_REPURCHASE_AGREE;
	private BigDecimal R22_COM_PAPER;
	private BigDecimal R22_CERT_OF_DEP;
	private BigDecimal R22_PLEDGED_ASSET;
	private BigDecimal R22_OTHER;
	private BigDecimal R22_TOTAL;
	private String R23_PRODUCT;
	private BigDecimal R23_EQUITY;
	private BigDecimal R23_BONDS;
	private BigDecimal R23_BOBC;
	private BigDecimal R23_TRES_BILLS;
	private BigDecimal R23_REPURCHASE_AGREE;
	private BigDecimal R23_COM_PAPER;
	private BigDecimal R23_CERT_OF_DEP;
	private BigDecimal R23_PLEDGED_ASSET;
	private BigDecimal R23_OTHER;
	private BigDecimal R23_TOTAL;
	private String R24_PRODUCT;
	private BigDecimal R24_EQUITY;
	private BigDecimal R24_BONDS;
	private BigDecimal R24_BOBC;
	private BigDecimal R24_TRES_BILLS;
	private BigDecimal R24_REPURCHASE_AGREE;
	private BigDecimal R24_COM_PAPER;
	private BigDecimal R24_CERT_OF_DEP;
	private BigDecimal R24_PLEDGED_ASSET;
	private BigDecimal R24_OTHER;
	private BigDecimal R24_TOTAL;
	private String R25_PRODUCT;
	private BigDecimal R25_EQUITY;
	private BigDecimal R25_BONDS;
	private BigDecimal R25_BOBC;
	private BigDecimal R25_TRES_BILLS;
	private BigDecimal R25_REPURCHASE_AGREE;
	private BigDecimal R25_COM_PAPER;
	private BigDecimal R25_CERT_OF_DEP;
	private BigDecimal R25_PLEDGED_ASSET;
	private BigDecimal R25_OTHER;
	private BigDecimal R25_TOTAL;
	private String R26_PRODUCT;
	private BigDecimal R26_EQUITY;
	private BigDecimal R26_BONDS;
	private BigDecimal R26_BOBC;
	private BigDecimal R26_TRES_BILLS;
	private BigDecimal R26_REPURCHASE_AGREE;
	private BigDecimal R26_COM_PAPER;
	private BigDecimal R26_CERT_OF_DEP;
	private BigDecimal R26_PLEDGED_ASSET;
	private BigDecimal R26_OTHER;
	private BigDecimal R26_TOTAL;
	private String R27_PRODUCT;
	private BigDecimal R27_EQUITY;
	private BigDecimal R27_BONDS;
	private BigDecimal R27_BOBC;
	private BigDecimal R27_TRES_BILLS;
	private BigDecimal R27_REPURCHASE_AGREE;
	private BigDecimal R27_COM_PAPER;
	private BigDecimal R27_CERT_OF_DEP;
	private BigDecimal R27_PLEDGED_ASSET;
	private BigDecimal R27_OTHER;
	private BigDecimal R27_TOTAL;
	private String R28_PRODUCT;
	private BigDecimal R28_EQUITY;
	private BigDecimal R28_BONDS;
	private BigDecimal R28_BOBC;
	private BigDecimal R28_TRES_BILLS;
	private BigDecimal R28_REPURCHASE_AGREE;
	private BigDecimal R28_COM_PAPER;
	private BigDecimal R28_CERT_OF_DEP;
	private BigDecimal R28_PLEDGED_ASSET;
	private BigDecimal R28_OTHER;
	private BigDecimal R28_TOTAL;
	private String R29_PRODUCT;
	private BigDecimal R29_EQUITY;
	private BigDecimal R29_BONDS;
	private BigDecimal R29_BOBC;
	private BigDecimal R29_TRES_BILLS;
	private BigDecimal R29_REPURCHASE_AGREE;
	private BigDecimal R29_COM_PAPER;
	private BigDecimal R29_CERT_OF_DEP;
	private BigDecimal R29_PLEDGED_ASSET;
	private BigDecimal R29_OTHER;
	private BigDecimal R29_TOTAL;
	private String R30_PRODUCT;
	private BigDecimal R30_EQUITY;
	private BigDecimal R30_BONDS;
	private BigDecimal R30_BOBC;
	private BigDecimal R30_TRES_BILLS;
	private BigDecimal R30_REPURCHASE_AGREE;
	private BigDecimal R30_COM_PAPER;
	private BigDecimal R30_CERT_OF_DEP;
	private BigDecimal R30_PLEDGED_ASSET;
	private BigDecimal R30_OTHER;
	private BigDecimal R30_TOTAL;
	private String R31_PRODUCT;
	private BigDecimal R31_EQUITY;
	private BigDecimal R31_BONDS;
	private BigDecimal R31_BOBC;
	private BigDecimal R31_TRES_BILLS;
	private BigDecimal R31_REPURCHASE_AGREE;
	private BigDecimal R31_COM_PAPER;
	private BigDecimal R31_CERT_OF_DEP;
	private BigDecimal R31_PLEDGED_ASSET;
	private BigDecimal R31_OTHER;
	private BigDecimal R31_TOTAL;
	private String R32_PRODUCT;
	private BigDecimal R32_EQUITY;
	private BigDecimal R32_BONDS;
	private BigDecimal R32_BOBC;
	private BigDecimal R32_TRES_BILLS;
	private BigDecimal R32_REPURCHASE_AGREE;
	private BigDecimal R32_COM_PAPER;
	private BigDecimal R32_CERT_OF_DEP;
	private BigDecimal R32_PLEDGED_ASSET;
	private BigDecimal R32_OTHER;
	private BigDecimal R32_TOTAL;
	private String R33_PRODUCT;
	private BigDecimal R33_EQUITY;
	private BigDecimal R33_BONDS;
	private BigDecimal R33_BOBC;
	private BigDecimal R33_TRES_BILLS;
	private BigDecimal R33_REPURCHASE_AGREE;
	private BigDecimal R33_COM_PAPER;
	private BigDecimal R33_CERT_OF_DEP;
	private BigDecimal R33_PLEDGED_ASSET;
	private BigDecimal R33_OTHER;
	private BigDecimal R33_TOTAL;
	private String R34_PRODUCT;
	private BigDecimal R34_EQUITY;
	private BigDecimal R34_BONDS;
	private BigDecimal R34_BOBC;
	private BigDecimal R34_TRES_BILLS;
	private BigDecimal R34_REPURCHASE_AGREE;
	private BigDecimal R34_COM_PAPER;
	private BigDecimal R34_CERT_OF_DEP;
	private BigDecimal R34_PLEDGED_ASSET;
	private BigDecimal R34_OTHER;
	private BigDecimal R34_TOTAL;
	private String R35_PRODUCT;
	private BigDecimal R35_EQUITY;
	private BigDecimal R35_BONDS;
	private BigDecimal R35_BOBC;
	private BigDecimal R35_TRES_BILLS;
	private BigDecimal R35_REPURCHASE_AGREE;
	private BigDecimal R35_COM_PAPER;
	private BigDecimal R35_CERT_OF_DEP;
	private BigDecimal R35_PLEDGED_ASSET;
	private BigDecimal R35_OTHER;
	private BigDecimal R35_TOTAL;
	private String R36_PRODUCT;
	private BigDecimal R36_EQUITY;
	private BigDecimal R36_BONDS;
	private BigDecimal R36_BOBC;
	private BigDecimal R36_TRES_BILLS;
	private BigDecimal R36_REPURCHASE_AGREE;
	private BigDecimal R36_COM_PAPER;
	private BigDecimal R36_CERT_OF_DEP;
	private BigDecimal R36_PLEDGED_ASSET;
	private BigDecimal R36_OTHER;
	private BigDecimal R36_TOTAL;
	private String R37_PRODUCT;
	private BigDecimal R37_EQUITY;
	private BigDecimal R37_BONDS;
	private BigDecimal R37_BOBC;
	private BigDecimal R37_TRES_BILLS;
	private BigDecimal R37_REPURCHASE_AGREE;
	private BigDecimal R37_COM_PAPER;
	private BigDecimal R37_CERT_OF_DEP;
	private BigDecimal R37_PLEDGED_ASSET;
	private BigDecimal R37_OTHER;
	private BigDecimal R37_TOTAL;
	private String R38_PRODUCT;
	private BigDecimal R38_EQUITY;
	private BigDecimal R38_BONDS;
	private BigDecimal R38_BOBC;
	private BigDecimal R38_TRES_BILLS;
	private BigDecimal R38_REPURCHASE_AGREE;
	private BigDecimal R38_COM_PAPER;
	private BigDecimal R38_CERT_OF_DEP;
	private BigDecimal R38_PLEDGED_ASSET;
	private BigDecimal R38_OTHER;
	private BigDecimal R38_TOTAL;
	private String R39_PRODUCT;
	private BigDecimal R39_EQUITY;
	private BigDecimal R39_BONDS;
	private BigDecimal R39_BOBC;
	private BigDecimal R39_TRES_BILLS;
	private BigDecimal R39_REPURCHASE_AGREE;
	private BigDecimal R39_COM_PAPER;
	private BigDecimal R39_CERT_OF_DEP;
	private BigDecimal R39_PLEDGED_ASSET;
	private BigDecimal R39_OTHER;
	private BigDecimal R39_TOTAL;
	private String R40_PRODUCT;
	private BigDecimal R40_EQUITY;
	private BigDecimal R40_BONDS;
	private BigDecimal R40_BOBC;
	private BigDecimal R40_TRES_BILLS;
	private BigDecimal R40_REPURCHASE_AGREE;
	private BigDecimal R40_COM_PAPER;
	private BigDecimal R40_CERT_OF_DEP;
	private BigDecimal R40_PLEDGED_ASSET;
	private BigDecimal R40_OTHER;
	private BigDecimal R40_TOTAL;
	private String R41_PRODUCT;
	private BigDecimal R41_EQUITY;
	private BigDecimal R41_BONDS;
	private BigDecimal R41_BOBC;
	private BigDecimal R41_TRES_BILLS;
	private BigDecimal R41_REPURCHASE_AGREE;
	private BigDecimal R41_COM_PAPER;
	private BigDecimal R41_CERT_OF_DEP;
	private BigDecimal R41_PLEDGED_ASSET;
	private BigDecimal R41_OTHER;
	private BigDecimal R41_TOTAL;
	private String R42_PRODUCT;
	private BigDecimal R42_EQUITY;
	private BigDecimal R42_BONDS;
	private BigDecimal R42_BOBC;
	private BigDecimal R42_TRES_BILLS;
	private BigDecimal R42_REPURCHASE_AGREE;
	private BigDecimal R42_COM_PAPER;
	private BigDecimal R42_CERT_OF_DEP;
	private BigDecimal R42_PLEDGED_ASSET;
	private BigDecimal R42_OTHER;
	private BigDecimal R42_TOTAL;
	private String R43_PRODUCT;
	private BigDecimal R43_EQUITY;
	private BigDecimal R43_BONDS;
	private BigDecimal R43_BOBC;
	private BigDecimal R43_TRES_BILLS;
	private BigDecimal R43_REPURCHASE_AGREE;
	private BigDecimal R43_COM_PAPER;
	private BigDecimal R43_CERT_OF_DEP;
	private BigDecimal R43_PLEDGED_ASSET;
	private BigDecimal R43_OTHER;
	private BigDecimal R43_TOTAL;
	private String R44_PRODUCT;
	private BigDecimal R44_EQUITY;
	private BigDecimal R44_BONDS;
	private BigDecimal R44_BOBC;
	private BigDecimal R44_TRES_BILLS;
	private BigDecimal R44_REPURCHASE_AGREE;
	private BigDecimal R44_COM_PAPER;
	private BigDecimal R44_CERT_OF_DEP;
	private BigDecimal R44_PLEDGED_ASSET;
	private BigDecimal R44_OTHER;
	private BigDecimal R44_TOTAL;
	private String R45_PRODUCT;
	private BigDecimal R45_EQUITY;
	private BigDecimal R45_BONDS;
	private BigDecimal R45_BOBC;
	private BigDecimal R45_TRES_BILLS;
	private BigDecimal R45_REPURCHASE_AGREE;
	private BigDecimal R45_COM_PAPER;
	private BigDecimal R45_CERT_OF_DEP;
	private BigDecimal R45_PLEDGED_ASSET;
	private BigDecimal R45_OTHER;
	private BigDecimal R45_TOTAL;
	private String R46_PRODUCT;
	private BigDecimal R46_EQUITY;
	private BigDecimal R46_BONDS;
	private BigDecimal R46_BOBC;
	private BigDecimal R46_TRES_BILLS;
	private BigDecimal R46_REPURCHASE_AGREE;
	private BigDecimal R46_COM_PAPER;
	private BigDecimal R46_CERT_OF_DEP;
	private BigDecimal R46_PLEDGED_ASSET;
	private BigDecimal R46_OTHER;
	private BigDecimal R46_TOTAL;
	private String R47_PRODUCT;
	private BigDecimal R47_EQUITY;
	private BigDecimal R47_BONDS;
	private BigDecimal R47_BOBC;
	private BigDecimal R47_TRES_BILLS;
	private BigDecimal R47_REPURCHASE_AGREE;
	private BigDecimal R47_COM_PAPER;
	private BigDecimal R47_CERT_OF_DEP;
	private BigDecimal R47_PLEDGED_ASSET;
	private BigDecimal R47_OTHER;
	private BigDecimal R47_TOTAL;
	private String R48_PRODUCT;
	private BigDecimal R48_EQUITY;
	private BigDecimal R48_BONDS;
	private BigDecimal R48_BOBC;
	private BigDecimal R48_TRES_BILLS;
	private BigDecimal R48_REPURCHASE_AGREE;
	private BigDecimal R48_COM_PAPER;
	private BigDecimal R48_CERT_OF_DEP;
	private BigDecimal R48_PLEDGED_ASSET;
	private BigDecimal R48_OTHER;
	private BigDecimal R48_TOTAL;
	private String R49_PRODUCT;
	private BigDecimal R49_EQUITY;
	private BigDecimal R49_BONDS;
	private BigDecimal R49_BOBC;
	private BigDecimal R49_TRES_BILLS;
	private BigDecimal R49_REPURCHASE_AGREE;
	private BigDecimal R49_COM_PAPER;
	private BigDecimal R49_CERT_OF_DEP;
	private BigDecimal R49_PLEDGED_ASSET;
	private BigDecimal R49_OTHER;
	private BigDecimal R49_TOTAL;
	private String R50_PRODUCT;
	private BigDecimal R50_EQUITY;
	private BigDecimal R50_BONDS;
	private BigDecimal R50_BOBC;
	private BigDecimal R50_TRES_BILLS;
	private BigDecimal R50_REPURCHASE_AGREE;
	private BigDecimal R50_COM_PAPER;
	private BigDecimal R50_CERT_OF_DEP;
	private BigDecimal R50_PLEDGED_ASSET;
	private BigDecimal R50_OTHER;
	private BigDecimal R50_TOTAL;
	private String R51_PRODUCT;
	private BigDecimal R51_EQUITY;
	private BigDecimal R51_BONDS;
	private BigDecimal R51_BOBC;
	private BigDecimal R51_TRES_BILLS;
	private BigDecimal R51_REPURCHASE_AGREE;
	private BigDecimal R51_COM_PAPER;
	private BigDecimal R51_CERT_OF_DEP;
	private BigDecimal R51_PLEDGED_ASSET;
	private BigDecimal R51_OTHER;
	private BigDecimal R51_TOTAL;
	private String R52_PRODUCT;
	private BigDecimal R52_EQUITY;
	private BigDecimal R52_BONDS;
	private BigDecimal R52_BOBC;
	private BigDecimal R52_TRES_BILLS;
	private BigDecimal R52_REPURCHASE_AGREE;
	private BigDecimal R52_COM_PAPER;
	private BigDecimal R52_CERT_OF_DEP;
	private BigDecimal R52_PLEDGED_ASSET;
	private BigDecimal R52_OTHER;
	private BigDecimal R52_TOTAL;
	private String R53_PRODUCT;
	private BigDecimal R53_EQUITY;
	private BigDecimal R53_BONDS;
	private BigDecimal R53_BOBC;
	private BigDecimal R53_TRES_BILLS;
	private BigDecimal R53_REPURCHASE_AGREE;
	private BigDecimal R53_COM_PAPER;
	private BigDecimal R53_CERT_OF_DEP;
	private BigDecimal R53_PLEDGED_ASSET;
	private BigDecimal R53_OTHER;
	private BigDecimal R53_TOTAL;
	private String R54_PRODUCT;
	private BigDecimal R54_EQUITY;
	private BigDecimal R54_BONDS;
	private BigDecimal R54_BOBC;
	private BigDecimal R54_TRES_BILLS;
	private BigDecimal R54_REPURCHASE_AGREE;
	private BigDecimal R54_COM_PAPER;
	private BigDecimal R54_CERT_OF_DEP;
	private BigDecimal R54_PLEDGED_ASSET;
	private BigDecimal R54_OTHER;
	private BigDecimal R54_TOTAL;
	private String R55_PRODUCT;
	private BigDecimal R55_EQUITY;
	private BigDecimal R55_BONDS;
	private BigDecimal R55_BOBC;
	private BigDecimal R55_TRES_BILLS;
	private BigDecimal R55_REPURCHASE_AGREE;
	private BigDecimal R55_COM_PAPER;
	private BigDecimal R55_CERT_OF_DEP;
	private BigDecimal R55_PLEDGED_ASSET;
	private BigDecimal R55_OTHER;
	private BigDecimal R55_TOTAL;
	private String R56_PRODUCT;
	private BigDecimal R56_EQUITY;
	private BigDecimal R56_BONDS;
	private BigDecimal R56_BOBC;
	private BigDecimal R56_TRES_BILLS;
	private BigDecimal R56_REPURCHASE_AGREE;
	private BigDecimal R56_COM_PAPER;
	private BigDecimal R56_CERT_OF_DEP;
	private BigDecimal R56_PLEDGED_ASSET;
	private BigDecimal R56_OTHER;
	private BigDecimal R56_TOTAL;
	private String R57_PRODUCT;
	private BigDecimal R57_EQUITY;
	private BigDecimal R57_BONDS;
	private BigDecimal R57_BOBC;
	private BigDecimal R57_TRES_BILLS;
	private BigDecimal R57_REPURCHASE_AGREE;
	private BigDecimal R57_COM_PAPER;
	private BigDecimal R57_CERT_OF_DEP;
	private BigDecimal R57_PLEDGED_ASSET;
	private BigDecimal R57_OTHER;
	private BigDecimal R57_TOTAL;
	private String R58_PRODUCT;
	private BigDecimal R58_EQUITY;
	private BigDecimal R58_BONDS;
	private BigDecimal R58_BOBC;
	private BigDecimal R58_TRES_BILLS;
	private BigDecimal R58_REPURCHASE_AGREE;
	private BigDecimal R58_COM_PAPER;
	private BigDecimal R58_CERT_OF_DEP;
	private BigDecimal R58_PLEDGED_ASSET;
	private BigDecimal R58_OTHER;
	private BigDecimal R58_TOTAL;
	private String R59_PRODUCT;
	private BigDecimal R59_EQUITY;
	private BigDecimal R59_BONDS;
	private BigDecimal R59_BOBC;
	private BigDecimal R59_TRES_BILLS;
	private BigDecimal R59_REPURCHASE_AGREE;
	private BigDecimal R59_COM_PAPER;
	private BigDecimal R59_CERT_OF_DEP;
	private BigDecimal R59_PLEDGED_ASSET;
	private BigDecimal R59_OTHER;
	private BigDecimal R59_TOTAL;


    

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





	public BigDecimal getR13_BOBC() {
		return R13_BOBC;
	}





	public void setR13_BOBC(BigDecimal r13_BOBC) {
		R13_BOBC = r13_BOBC;
	}





	public BigDecimal getR13_TRES_BILLS() {
		return R13_TRES_BILLS;
	}





	public void setR13_TRES_BILLS(BigDecimal r13_TRES_BILLS) {
		R13_TRES_BILLS = r13_TRES_BILLS;
	}





	public BigDecimal getR13_REPURCHASE_AGREE() {
		return R13_REPURCHASE_AGREE;
	}





	public void setR13_REPURCHASE_AGREE(BigDecimal r13_REPURCHASE_AGREE) {
		R13_REPURCHASE_AGREE = r13_REPURCHASE_AGREE;
	}





	public BigDecimal getR13_COM_PAPER() {
		return R13_COM_PAPER;
	}





	public void setR13_COM_PAPER(BigDecimal r13_COM_PAPER) {
		R13_COM_PAPER = r13_COM_PAPER;
	}





	public BigDecimal getR13_CERT_OF_DEP() {
		return R13_CERT_OF_DEP;
	}





	public void setR13_CERT_OF_DEP(BigDecimal r13_CERT_OF_DEP) {
		R13_CERT_OF_DEP = r13_CERT_OF_DEP;
	}





	public BigDecimal getR13_PLEDGED_ASSET() {
		return R13_PLEDGED_ASSET;
	}





	public void setR13_PLEDGED_ASSET(BigDecimal r13_PLEDGED_ASSET) {
		R13_PLEDGED_ASSET = r13_PLEDGED_ASSET;
	}





	public BigDecimal getR13_OTHER() {
		return R13_OTHER;
	}





	public void setR13_OTHER(BigDecimal r13_OTHER) {
		R13_OTHER = r13_OTHER;
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





	public BigDecimal getR14_BOBC() {
		return R14_BOBC;
	}





	public void setR14_BOBC(BigDecimal r14_BOBC) {
		R14_BOBC = r14_BOBC;
	}





	public BigDecimal getR14_TRES_BILLS() {
		return R14_TRES_BILLS;
	}





	public void setR14_TRES_BILLS(BigDecimal r14_TRES_BILLS) {
		R14_TRES_BILLS = r14_TRES_BILLS;
	}





	public BigDecimal getR14_REPURCHASE_AGREE() {
		return R14_REPURCHASE_AGREE;
	}





	public void setR14_REPURCHASE_AGREE(BigDecimal r14_REPURCHASE_AGREE) {
		R14_REPURCHASE_AGREE = r14_REPURCHASE_AGREE;
	}





	public BigDecimal getR14_COM_PAPER() {
		return R14_COM_PAPER;
	}





	public void setR14_COM_PAPER(BigDecimal r14_COM_PAPER) {
		R14_COM_PAPER = r14_COM_PAPER;
	}





	public BigDecimal getR14_CERT_OF_DEP() {
		return R14_CERT_OF_DEP;
	}





	public void setR14_CERT_OF_DEP(BigDecimal r14_CERT_OF_DEP) {
		R14_CERT_OF_DEP = r14_CERT_OF_DEP;
	}





	public BigDecimal getR14_PLEDGED_ASSET() {
		return R14_PLEDGED_ASSET;
	}





	public void setR14_PLEDGED_ASSET(BigDecimal r14_PLEDGED_ASSET) {
		R14_PLEDGED_ASSET = r14_PLEDGED_ASSET;
	}





	public BigDecimal getR14_OTHER() {
		return R14_OTHER;
	}





	public void setR14_OTHER(BigDecimal r14_OTHER) {
		R14_OTHER = r14_OTHER;
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





	public BigDecimal getR15_BOBC() {
		return R15_BOBC;
	}





	public void setR15_BOBC(BigDecimal r15_BOBC) {
		R15_BOBC = r15_BOBC;
	}





	public BigDecimal getR15_TRES_BILLS() {
		return R15_TRES_BILLS;
	}





	public void setR15_TRES_BILLS(BigDecimal r15_TRES_BILLS) {
		R15_TRES_BILLS = r15_TRES_BILLS;
	}





	public BigDecimal getR15_REPURCHASE_AGREE() {
		return R15_REPURCHASE_AGREE;
	}





	public void setR15_REPURCHASE_AGREE(BigDecimal r15_REPURCHASE_AGREE) {
		R15_REPURCHASE_AGREE = r15_REPURCHASE_AGREE;
	}





	public BigDecimal getR15_COM_PAPER() {
		return R15_COM_PAPER;
	}





	public void setR15_COM_PAPER(BigDecimal r15_COM_PAPER) {
		R15_COM_PAPER = r15_COM_PAPER;
	}





	public BigDecimal getR15_CERT_OF_DEP() {
		return R15_CERT_OF_DEP;
	}





	public void setR15_CERT_OF_DEP(BigDecimal r15_CERT_OF_DEP) {
		R15_CERT_OF_DEP = r15_CERT_OF_DEP;
	}





	public BigDecimal getR15_PLEDGED_ASSET() {
		return R15_PLEDGED_ASSET;
	}





	public void setR15_PLEDGED_ASSET(BigDecimal r15_PLEDGED_ASSET) {
		R15_PLEDGED_ASSET = r15_PLEDGED_ASSET;
	}





	public BigDecimal getR15_OTHER() {
		return R15_OTHER;
	}





	public void setR15_OTHER(BigDecimal r15_OTHER) {
		R15_OTHER = r15_OTHER;
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





	public BigDecimal getR16_BOBC() {
		return R16_BOBC;
	}





	public void setR16_BOBC(BigDecimal r16_BOBC) {
		R16_BOBC = r16_BOBC;
	}





	public BigDecimal getR16_TRES_BILLS() {
		return R16_TRES_BILLS;
	}





	public void setR16_TRES_BILLS(BigDecimal r16_TRES_BILLS) {
		R16_TRES_BILLS = r16_TRES_BILLS;
	}





	public BigDecimal getR16_REPURCHASE_AGREE() {
		return R16_REPURCHASE_AGREE;
	}





	public void setR16_REPURCHASE_AGREE(BigDecimal r16_REPURCHASE_AGREE) {
		R16_REPURCHASE_AGREE = r16_REPURCHASE_AGREE;
	}





	public BigDecimal getR16_COM_PAPER() {
		return R16_COM_PAPER;
	}





	public void setR16_COM_PAPER(BigDecimal r16_COM_PAPER) {
		R16_COM_PAPER = r16_COM_PAPER;
	}





	public BigDecimal getR16_CERT_OF_DEP() {
		return R16_CERT_OF_DEP;
	}





	public void setR16_CERT_OF_DEP(BigDecimal r16_CERT_OF_DEP) {
		R16_CERT_OF_DEP = r16_CERT_OF_DEP;
	}





	public BigDecimal getR16_PLEDGED_ASSET() {
		return R16_PLEDGED_ASSET;
	}





	public void setR16_PLEDGED_ASSET(BigDecimal r16_PLEDGED_ASSET) {
		R16_PLEDGED_ASSET = r16_PLEDGED_ASSET;
	}





	public BigDecimal getR16_OTHER() {
		return R16_OTHER;
	}





	public void setR16_OTHER(BigDecimal r16_OTHER) {
		R16_OTHER = r16_OTHER;
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





	public BigDecimal getR17_BOBC() {
		return R17_BOBC;
	}





	public void setR17_BOBC(BigDecimal r17_BOBC) {
		R17_BOBC = r17_BOBC;
	}





	public BigDecimal getR17_TRES_BILLS() {
		return R17_TRES_BILLS;
	}





	public void setR17_TRES_BILLS(BigDecimal r17_TRES_BILLS) {
		R17_TRES_BILLS = r17_TRES_BILLS;
	}





	public BigDecimal getR17_REPURCHASE_AGREE() {
		return R17_REPURCHASE_AGREE;
	}





	public void setR17_REPURCHASE_AGREE(BigDecimal r17_REPURCHASE_AGREE) {
		R17_REPURCHASE_AGREE = r17_REPURCHASE_AGREE;
	}





	public BigDecimal getR17_COM_PAPER() {
		return R17_COM_PAPER;
	}





	public void setR17_COM_PAPER(BigDecimal r17_COM_PAPER) {
		R17_COM_PAPER = r17_COM_PAPER;
	}





	public BigDecimal getR17_CERT_OF_DEP() {
		return R17_CERT_OF_DEP;
	}





	public void setR17_CERT_OF_DEP(BigDecimal r17_CERT_OF_DEP) {
		R17_CERT_OF_DEP = r17_CERT_OF_DEP;
	}





	public BigDecimal getR17_PLEDGED_ASSET() {
		return R17_PLEDGED_ASSET;
	}





	public void setR17_PLEDGED_ASSET(BigDecimal r17_PLEDGED_ASSET) {
		R17_PLEDGED_ASSET = r17_PLEDGED_ASSET;
	}





	public BigDecimal getR17_OTHER() {
		return R17_OTHER;
	}





	public void setR17_OTHER(BigDecimal r17_OTHER) {
		R17_OTHER = r17_OTHER;
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





	public BigDecimal getR18_BOBC() {
		return R18_BOBC;
	}





	public void setR18_BOBC(BigDecimal r18_BOBC) {
		R18_BOBC = r18_BOBC;
	}





	public BigDecimal getR18_TRES_BILLS() {
		return R18_TRES_BILLS;
	}





	public void setR18_TRES_BILLS(BigDecimal r18_TRES_BILLS) {
		R18_TRES_BILLS = r18_TRES_BILLS;
	}





	public BigDecimal getR18_REPURCHASE_AGREE() {
		return R18_REPURCHASE_AGREE;
	}





	public void setR18_REPURCHASE_AGREE(BigDecimal r18_REPURCHASE_AGREE) {
		R18_REPURCHASE_AGREE = r18_REPURCHASE_AGREE;
	}





	public BigDecimal getR18_COM_PAPER() {
		return R18_COM_PAPER;
	}





	public void setR18_COM_PAPER(BigDecimal r18_COM_PAPER) {
		R18_COM_PAPER = r18_COM_PAPER;
	}





	public BigDecimal getR18_CERT_OF_DEP() {
		return R18_CERT_OF_DEP;
	}





	public void setR18_CERT_OF_DEP(BigDecimal r18_CERT_OF_DEP) {
		R18_CERT_OF_DEP = r18_CERT_OF_DEP;
	}





	public BigDecimal getR18_PLEDGED_ASSET() {
		return R18_PLEDGED_ASSET;
	}





	public void setR18_PLEDGED_ASSET(BigDecimal r18_PLEDGED_ASSET) {
		R18_PLEDGED_ASSET = r18_PLEDGED_ASSET;
	}





	public BigDecimal getR18_OTHER() {
		return R18_OTHER;
	}





	public void setR18_OTHER(BigDecimal r18_OTHER) {
		R18_OTHER = r18_OTHER;
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





	public BigDecimal getR19_BOBC() {
		return R19_BOBC;
	}





	public void setR19_BOBC(BigDecimal r19_BOBC) {
		R19_BOBC = r19_BOBC;
	}





	public BigDecimal getR19_TRES_BILLS() {
		return R19_TRES_BILLS;
	}





	public void setR19_TRES_BILLS(BigDecimal r19_TRES_BILLS) {
		R19_TRES_BILLS = r19_TRES_BILLS;
	}





	public BigDecimal getR19_REPURCHASE_AGREE() {
		return R19_REPURCHASE_AGREE;
	}





	public void setR19_REPURCHASE_AGREE(BigDecimal r19_REPURCHASE_AGREE) {
		R19_REPURCHASE_AGREE = r19_REPURCHASE_AGREE;
	}





	public BigDecimal getR19_COM_PAPER() {
		return R19_COM_PAPER;
	}





	public void setR19_COM_PAPER(BigDecimal r19_COM_PAPER) {
		R19_COM_PAPER = r19_COM_PAPER;
	}





	public BigDecimal getR19_CERT_OF_DEP() {
		return R19_CERT_OF_DEP;
	}





	public void setR19_CERT_OF_DEP(BigDecimal r19_CERT_OF_DEP) {
		R19_CERT_OF_DEP = r19_CERT_OF_DEP;
	}





	public BigDecimal getR19_PLEDGED_ASSET() {
		return R19_PLEDGED_ASSET;
	}





	public void setR19_PLEDGED_ASSET(BigDecimal r19_PLEDGED_ASSET) {
		R19_PLEDGED_ASSET = r19_PLEDGED_ASSET;
	}





	public BigDecimal getR19_OTHER() {
		return R19_OTHER;
	}





	public void setR19_OTHER(BigDecimal r19_OTHER) {
		R19_OTHER = r19_OTHER;
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





	public BigDecimal getR20_BOBC() {
		return R20_BOBC;
	}





	public void setR20_BOBC(BigDecimal r20_BOBC) {
		R20_BOBC = r20_BOBC;
	}





	public BigDecimal getR20_TRES_BILLS() {
		return R20_TRES_BILLS;
	}





	public void setR20_TRES_BILLS(BigDecimal r20_TRES_BILLS) {
		R20_TRES_BILLS = r20_TRES_BILLS;
	}





	public BigDecimal getR20_REPURCHASE_AGREE() {
		return R20_REPURCHASE_AGREE;
	}





	public void setR20_REPURCHASE_AGREE(BigDecimal r20_REPURCHASE_AGREE) {
		R20_REPURCHASE_AGREE = r20_REPURCHASE_AGREE;
	}





	public BigDecimal getR20_COM_PAPER() {
		return R20_COM_PAPER;
	}





	public void setR20_COM_PAPER(BigDecimal r20_COM_PAPER) {
		R20_COM_PAPER = r20_COM_PAPER;
	}





	public BigDecimal getR20_CERT_OF_DEP() {
		return R20_CERT_OF_DEP;
	}





	public void setR20_CERT_OF_DEP(BigDecimal r20_CERT_OF_DEP) {
		R20_CERT_OF_DEP = r20_CERT_OF_DEP;
	}





	public BigDecimal getR20_PLEDGED_ASSET() {
		return R20_PLEDGED_ASSET;
	}





	public void setR20_PLEDGED_ASSET(BigDecimal r20_PLEDGED_ASSET) {
		R20_PLEDGED_ASSET = r20_PLEDGED_ASSET;
	}





	public BigDecimal getR20_OTHER() {
		return R20_OTHER;
	}





	public void setR20_OTHER(BigDecimal r20_OTHER) {
		R20_OTHER = r20_OTHER;
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





	public BigDecimal getR21_BOBC() {
		return R21_BOBC;
	}





	public void setR21_BOBC(BigDecimal r21_BOBC) {
		R21_BOBC = r21_BOBC;
	}





	public BigDecimal getR21_TRES_BILLS() {
		return R21_TRES_BILLS;
	}





	public void setR21_TRES_BILLS(BigDecimal r21_TRES_BILLS) {
		R21_TRES_BILLS = r21_TRES_BILLS;
	}





	public BigDecimal getR21_REPURCHASE_AGREE() {
		return R21_REPURCHASE_AGREE;
	}





	public void setR21_REPURCHASE_AGREE(BigDecimal r21_REPURCHASE_AGREE) {
		R21_REPURCHASE_AGREE = r21_REPURCHASE_AGREE;
	}





	public BigDecimal getR21_COM_PAPER() {
		return R21_COM_PAPER;
	}





	public void setR21_COM_PAPER(BigDecimal r21_COM_PAPER) {
		R21_COM_PAPER = r21_COM_PAPER;
	}





	public BigDecimal getR21_CERT_OF_DEP() {
		return R21_CERT_OF_DEP;
	}





	public void setR21_CERT_OF_DEP(BigDecimal r21_CERT_OF_DEP) {
		R21_CERT_OF_DEP = r21_CERT_OF_DEP;
	}





	public BigDecimal getR21_PLEDGED_ASSET() {
		return R21_PLEDGED_ASSET;
	}





	public void setR21_PLEDGED_ASSET(BigDecimal r21_PLEDGED_ASSET) {
		R21_PLEDGED_ASSET = r21_PLEDGED_ASSET;
	}





	public BigDecimal getR21_OTHER() {
		return R21_OTHER;
	}





	public void setR21_OTHER(BigDecimal r21_OTHER) {
		R21_OTHER = r21_OTHER;
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





	public BigDecimal getR22_BOBC() {
		return R22_BOBC;
	}





	public void setR22_BOBC(BigDecimal r22_BOBC) {
		R22_BOBC = r22_BOBC;
	}





	public BigDecimal getR22_TRES_BILLS() {
		return R22_TRES_BILLS;
	}





	public void setR22_TRES_BILLS(BigDecimal r22_TRES_BILLS) {
		R22_TRES_BILLS = r22_TRES_BILLS;
	}





	public BigDecimal getR22_REPURCHASE_AGREE() {
		return R22_REPURCHASE_AGREE;
	}





	public void setR22_REPURCHASE_AGREE(BigDecimal r22_REPURCHASE_AGREE) {
		R22_REPURCHASE_AGREE = r22_REPURCHASE_AGREE;
	}





	public BigDecimal getR22_COM_PAPER() {
		return R22_COM_PAPER;
	}





	public void setR22_COM_PAPER(BigDecimal r22_COM_PAPER) {
		R22_COM_PAPER = r22_COM_PAPER;
	}





	public BigDecimal getR22_CERT_OF_DEP() {
		return R22_CERT_OF_DEP;
	}





	public void setR22_CERT_OF_DEP(BigDecimal r22_CERT_OF_DEP) {
		R22_CERT_OF_DEP = r22_CERT_OF_DEP;
	}





	public BigDecimal getR22_PLEDGED_ASSET() {
		return R22_PLEDGED_ASSET;
	}





	public void setR22_PLEDGED_ASSET(BigDecimal r22_PLEDGED_ASSET) {
		R22_PLEDGED_ASSET = r22_PLEDGED_ASSET;
	}





	public BigDecimal getR22_OTHER() {
		return R22_OTHER;
	}





	public void setR22_OTHER(BigDecimal r22_OTHER) {
		R22_OTHER = r22_OTHER;
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





	public BigDecimal getR23_BOBC() {
		return R23_BOBC;
	}





	public void setR23_BOBC(BigDecimal r23_BOBC) {
		R23_BOBC = r23_BOBC;
	}





	public BigDecimal getR23_TRES_BILLS() {
		return R23_TRES_BILLS;
	}





	public void setR23_TRES_BILLS(BigDecimal r23_TRES_BILLS) {
		R23_TRES_BILLS = r23_TRES_BILLS;
	}





	public BigDecimal getR23_REPURCHASE_AGREE() {
		return R23_REPURCHASE_AGREE;
	}





	public void setR23_REPURCHASE_AGREE(BigDecimal r23_REPURCHASE_AGREE) {
		R23_REPURCHASE_AGREE = r23_REPURCHASE_AGREE;
	}





	public BigDecimal getR23_COM_PAPER() {
		return R23_COM_PAPER;
	}





	public void setR23_COM_PAPER(BigDecimal r23_COM_PAPER) {
		R23_COM_PAPER = r23_COM_PAPER;
	}





	public BigDecimal getR23_CERT_OF_DEP() {
		return R23_CERT_OF_DEP;
	}





	public void setR23_CERT_OF_DEP(BigDecimal r23_CERT_OF_DEP) {
		R23_CERT_OF_DEP = r23_CERT_OF_DEP;
	}





	public BigDecimal getR23_PLEDGED_ASSET() {
		return R23_PLEDGED_ASSET;
	}





	public void setR23_PLEDGED_ASSET(BigDecimal r23_PLEDGED_ASSET) {
		R23_PLEDGED_ASSET = r23_PLEDGED_ASSET;
	}





	public BigDecimal getR23_OTHER() {
		return R23_OTHER;
	}





	public void setR23_OTHER(BigDecimal r23_OTHER) {
		R23_OTHER = r23_OTHER;
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





	public BigDecimal getR24_BOBC() {
		return R24_BOBC;
	}





	public void setR24_BOBC(BigDecimal r24_BOBC) {
		R24_BOBC = r24_BOBC;
	}





	public BigDecimal getR24_TRES_BILLS() {
		return R24_TRES_BILLS;
	}





	public void setR24_TRES_BILLS(BigDecimal r24_TRES_BILLS) {
		R24_TRES_BILLS = r24_TRES_BILLS;
	}





	public BigDecimal getR24_REPURCHASE_AGREE() {
		return R24_REPURCHASE_AGREE;
	}





	public void setR24_REPURCHASE_AGREE(BigDecimal r24_REPURCHASE_AGREE) {
		R24_REPURCHASE_AGREE = r24_REPURCHASE_AGREE;
	}





	public BigDecimal getR24_COM_PAPER() {
		return R24_COM_PAPER;
	}





	public void setR24_COM_PAPER(BigDecimal r24_COM_PAPER) {
		R24_COM_PAPER = r24_COM_PAPER;
	}





	public BigDecimal getR24_CERT_OF_DEP() {
		return R24_CERT_OF_DEP;
	}





	public void setR24_CERT_OF_DEP(BigDecimal r24_CERT_OF_DEP) {
		R24_CERT_OF_DEP = r24_CERT_OF_DEP;
	}





	public BigDecimal getR24_PLEDGED_ASSET() {
		return R24_PLEDGED_ASSET;
	}





	public void setR24_PLEDGED_ASSET(BigDecimal r24_PLEDGED_ASSET) {
		R24_PLEDGED_ASSET = r24_PLEDGED_ASSET;
	}





	public BigDecimal getR24_OTHER() {
		return R24_OTHER;
	}





	public void setR24_OTHER(BigDecimal r24_OTHER) {
		R24_OTHER = r24_OTHER;
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





	public BigDecimal getR25_BOBC() {
		return R25_BOBC;
	}





	public void setR25_BOBC(BigDecimal r25_BOBC) {
		R25_BOBC = r25_BOBC;
	}





	public BigDecimal getR25_TRES_BILLS() {
		return R25_TRES_BILLS;
	}





	public void setR25_TRES_BILLS(BigDecimal r25_TRES_BILLS) {
		R25_TRES_BILLS = r25_TRES_BILLS;
	}





	public BigDecimal getR25_REPURCHASE_AGREE() {
		return R25_REPURCHASE_AGREE;
	}





	public void setR25_REPURCHASE_AGREE(BigDecimal r25_REPURCHASE_AGREE) {
		R25_REPURCHASE_AGREE = r25_REPURCHASE_AGREE;
	}





	public BigDecimal getR25_COM_PAPER() {
		return R25_COM_PAPER;
	}





	public void setR25_COM_PAPER(BigDecimal r25_COM_PAPER) {
		R25_COM_PAPER = r25_COM_PAPER;
	}





	public BigDecimal getR25_CERT_OF_DEP() {
		return R25_CERT_OF_DEP;
	}





	public void setR25_CERT_OF_DEP(BigDecimal r25_CERT_OF_DEP) {
		R25_CERT_OF_DEP = r25_CERT_OF_DEP;
	}





	public BigDecimal getR25_PLEDGED_ASSET() {
		return R25_PLEDGED_ASSET;
	}





	public void setR25_PLEDGED_ASSET(BigDecimal r25_PLEDGED_ASSET) {
		R25_PLEDGED_ASSET = r25_PLEDGED_ASSET;
	}





	public BigDecimal getR25_OTHER() {
		return R25_OTHER;
	}





	public void setR25_OTHER(BigDecimal r25_OTHER) {
		R25_OTHER = r25_OTHER;
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





	public BigDecimal getR26_BOBC() {
		return R26_BOBC;
	}





	public void setR26_BOBC(BigDecimal r26_BOBC) {
		R26_BOBC = r26_BOBC;
	}





	public BigDecimal getR26_TRES_BILLS() {
		return R26_TRES_BILLS;
	}





	public void setR26_TRES_BILLS(BigDecimal r26_TRES_BILLS) {
		R26_TRES_BILLS = r26_TRES_BILLS;
	}





	public BigDecimal getR26_REPURCHASE_AGREE() {
		return R26_REPURCHASE_AGREE;
	}





	public void setR26_REPURCHASE_AGREE(BigDecimal r26_REPURCHASE_AGREE) {
		R26_REPURCHASE_AGREE = r26_REPURCHASE_AGREE;
	}





	public BigDecimal getR26_COM_PAPER() {
		return R26_COM_PAPER;
	}





	public void setR26_COM_PAPER(BigDecimal r26_COM_PAPER) {
		R26_COM_PAPER = r26_COM_PAPER;
	}





	public BigDecimal getR26_CERT_OF_DEP() {
		return R26_CERT_OF_DEP;
	}





	public void setR26_CERT_OF_DEP(BigDecimal r26_CERT_OF_DEP) {
		R26_CERT_OF_DEP = r26_CERT_OF_DEP;
	}





	public BigDecimal getR26_PLEDGED_ASSET() {
		return R26_PLEDGED_ASSET;
	}





	public void setR26_PLEDGED_ASSET(BigDecimal r26_PLEDGED_ASSET) {
		R26_PLEDGED_ASSET = r26_PLEDGED_ASSET;
	}





	public BigDecimal getR26_OTHER() {
		return R26_OTHER;
	}





	public void setR26_OTHER(BigDecimal r26_OTHER) {
		R26_OTHER = r26_OTHER;
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





	public BigDecimal getR27_BOBC() {
		return R27_BOBC;
	}





	public void setR27_BOBC(BigDecimal r27_BOBC) {
		R27_BOBC = r27_BOBC;
	}





	public BigDecimal getR27_TRES_BILLS() {
		return R27_TRES_BILLS;
	}





	public void setR27_TRES_BILLS(BigDecimal r27_TRES_BILLS) {
		R27_TRES_BILLS = r27_TRES_BILLS;
	}





	public BigDecimal getR27_REPURCHASE_AGREE() {
		return R27_REPURCHASE_AGREE;
	}





	public void setR27_REPURCHASE_AGREE(BigDecimal r27_REPURCHASE_AGREE) {
		R27_REPURCHASE_AGREE = r27_REPURCHASE_AGREE;
	}





	public BigDecimal getR27_COM_PAPER() {
		return R27_COM_PAPER;
	}





	public void setR27_COM_PAPER(BigDecimal r27_COM_PAPER) {
		R27_COM_PAPER = r27_COM_PAPER;
	}





	public BigDecimal getR27_CERT_OF_DEP() {
		return R27_CERT_OF_DEP;
	}





	public void setR27_CERT_OF_DEP(BigDecimal r27_CERT_OF_DEP) {
		R27_CERT_OF_DEP = r27_CERT_OF_DEP;
	}





	public BigDecimal getR27_PLEDGED_ASSET() {
		return R27_PLEDGED_ASSET;
	}





	public void setR27_PLEDGED_ASSET(BigDecimal r27_PLEDGED_ASSET) {
		R27_PLEDGED_ASSET = r27_PLEDGED_ASSET;
	}





	public BigDecimal getR27_OTHER() {
		return R27_OTHER;
	}





	public void setR27_OTHER(BigDecimal r27_OTHER) {
		R27_OTHER = r27_OTHER;
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





	public BigDecimal getR28_BOBC() {
		return R28_BOBC;
	}





	public void setR28_BOBC(BigDecimal r28_BOBC) {
		R28_BOBC = r28_BOBC;
	}





	public BigDecimal getR28_TRES_BILLS() {
		return R28_TRES_BILLS;
	}





	public void setR28_TRES_BILLS(BigDecimal r28_TRES_BILLS) {
		R28_TRES_BILLS = r28_TRES_BILLS;
	}





	public BigDecimal getR28_REPURCHASE_AGREE() {
		return R28_REPURCHASE_AGREE;
	}





	public void setR28_REPURCHASE_AGREE(BigDecimal r28_REPURCHASE_AGREE) {
		R28_REPURCHASE_AGREE = r28_REPURCHASE_AGREE;
	}





	public BigDecimal getR28_COM_PAPER() {
		return R28_COM_PAPER;
	}





	public void setR28_COM_PAPER(BigDecimal r28_COM_PAPER) {
		R28_COM_PAPER = r28_COM_PAPER;
	}





	public BigDecimal getR28_CERT_OF_DEP() {
		return R28_CERT_OF_DEP;
	}





	public void setR28_CERT_OF_DEP(BigDecimal r28_CERT_OF_DEP) {
		R28_CERT_OF_DEP = r28_CERT_OF_DEP;
	}





	public BigDecimal getR28_PLEDGED_ASSET() {
		return R28_PLEDGED_ASSET;
	}





	public void setR28_PLEDGED_ASSET(BigDecimal r28_PLEDGED_ASSET) {
		R28_PLEDGED_ASSET = r28_PLEDGED_ASSET;
	}





	public BigDecimal getR28_OTHER() {
		return R28_OTHER;
	}





	public void setR28_OTHER(BigDecimal r28_OTHER) {
		R28_OTHER = r28_OTHER;
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





	public BigDecimal getR29_BOBC() {
		return R29_BOBC;
	}





	public void setR29_BOBC(BigDecimal r29_BOBC) {
		R29_BOBC = r29_BOBC;
	}





	public BigDecimal getR29_TRES_BILLS() {
		return R29_TRES_BILLS;
	}





	public void setR29_TRES_BILLS(BigDecimal r29_TRES_BILLS) {
		R29_TRES_BILLS = r29_TRES_BILLS;
	}





	public BigDecimal getR29_REPURCHASE_AGREE() {
		return R29_REPURCHASE_AGREE;
	}





	public void setR29_REPURCHASE_AGREE(BigDecimal r29_REPURCHASE_AGREE) {
		R29_REPURCHASE_AGREE = r29_REPURCHASE_AGREE;
	}





	public BigDecimal getR29_COM_PAPER() {
		return R29_COM_PAPER;
	}





	public void setR29_COM_PAPER(BigDecimal r29_COM_PAPER) {
		R29_COM_PAPER = r29_COM_PAPER;
	}





	public BigDecimal getR29_CERT_OF_DEP() {
		return R29_CERT_OF_DEP;
	}





	public void setR29_CERT_OF_DEP(BigDecimal r29_CERT_OF_DEP) {
		R29_CERT_OF_DEP = r29_CERT_OF_DEP;
	}





	public BigDecimal getR29_PLEDGED_ASSET() {
		return R29_PLEDGED_ASSET;
	}





	public void setR29_PLEDGED_ASSET(BigDecimal r29_PLEDGED_ASSET) {
		R29_PLEDGED_ASSET = r29_PLEDGED_ASSET;
	}





	public BigDecimal getR29_OTHER() {
		return R29_OTHER;
	}





	public void setR29_OTHER(BigDecimal r29_OTHER) {
		R29_OTHER = r29_OTHER;
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





	public BigDecimal getR30_BOBC() {
		return R30_BOBC;
	}





	public void setR30_BOBC(BigDecimal r30_BOBC) {
		R30_BOBC = r30_BOBC;
	}





	public BigDecimal getR30_TRES_BILLS() {
		return R30_TRES_BILLS;
	}





	public void setR30_TRES_BILLS(BigDecimal r30_TRES_BILLS) {
		R30_TRES_BILLS = r30_TRES_BILLS;
	}





	public BigDecimal getR30_REPURCHASE_AGREE() {
		return R30_REPURCHASE_AGREE;
	}





	public void setR30_REPURCHASE_AGREE(BigDecimal r30_REPURCHASE_AGREE) {
		R30_REPURCHASE_AGREE = r30_REPURCHASE_AGREE;
	}





	public BigDecimal getR30_COM_PAPER() {
		return R30_COM_PAPER;
	}





	public void setR30_COM_PAPER(BigDecimal r30_COM_PAPER) {
		R30_COM_PAPER = r30_COM_PAPER;
	}





	public BigDecimal getR30_CERT_OF_DEP() {
		return R30_CERT_OF_DEP;
	}





	public void setR30_CERT_OF_DEP(BigDecimal r30_CERT_OF_DEP) {
		R30_CERT_OF_DEP = r30_CERT_OF_DEP;
	}





	public BigDecimal getR30_PLEDGED_ASSET() {
		return R30_PLEDGED_ASSET;
	}





	public void setR30_PLEDGED_ASSET(BigDecimal r30_PLEDGED_ASSET) {
		R30_PLEDGED_ASSET = r30_PLEDGED_ASSET;
	}





	public BigDecimal getR30_OTHER() {
		return R30_OTHER;
	}





	public void setR30_OTHER(BigDecimal r30_OTHER) {
		R30_OTHER = r30_OTHER;
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





	public BigDecimal getR31_BOBC() {
		return R31_BOBC;
	}





	public void setR31_BOBC(BigDecimal r31_BOBC) {
		R31_BOBC = r31_BOBC;
	}





	public BigDecimal getR31_TRES_BILLS() {
		return R31_TRES_BILLS;
	}





	public void setR31_TRES_BILLS(BigDecimal r31_TRES_BILLS) {
		R31_TRES_BILLS = r31_TRES_BILLS;
	}





	public BigDecimal getR31_REPURCHASE_AGREE() {
		return R31_REPURCHASE_AGREE;
	}





	public void setR31_REPURCHASE_AGREE(BigDecimal r31_REPURCHASE_AGREE) {
		R31_REPURCHASE_AGREE = r31_REPURCHASE_AGREE;
	}





	public BigDecimal getR31_COM_PAPER() {
		return R31_COM_PAPER;
	}





	public void setR31_COM_PAPER(BigDecimal r31_COM_PAPER) {
		R31_COM_PAPER = r31_COM_PAPER;
	}





	public BigDecimal getR31_CERT_OF_DEP() {
		return R31_CERT_OF_DEP;
	}





	public void setR31_CERT_OF_DEP(BigDecimal r31_CERT_OF_DEP) {
		R31_CERT_OF_DEP = r31_CERT_OF_DEP;
	}





	public BigDecimal getR31_PLEDGED_ASSET() {
		return R31_PLEDGED_ASSET;
	}





	public void setR31_PLEDGED_ASSET(BigDecimal r31_PLEDGED_ASSET) {
		R31_PLEDGED_ASSET = r31_PLEDGED_ASSET;
	}





	public BigDecimal getR31_OTHER() {
		return R31_OTHER;
	}





	public void setR31_OTHER(BigDecimal r31_OTHER) {
		R31_OTHER = r31_OTHER;
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





	public BigDecimal getR32_BOBC() {
		return R32_BOBC;
	}





	public void setR32_BOBC(BigDecimal r32_BOBC) {
		R32_BOBC = r32_BOBC;
	}





	public BigDecimal getR32_TRES_BILLS() {
		return R32_TRES_BILLS;
	}





	public void setR32_TRES_BILLS(BigDecimal r32_TRES_BILLS) {
		R32_TRES_BILLS = r32_TRES_BILLS;
	}





	public BigDecimal getR32_REPURCHASE_AGREE() {
		return R32_REPURCHASE_AGREE;
	}





	public void setR32_REPURCHASE_AGREE(BigDecimal r32_REPURCHASE_AGREE) {
		R32_REPURCHASE_AGREE = r32_REPURCHASE_AGREE;
	}





	public BigDecimal getR32_COM_PAPER() {
		return R32_COM_PAPER;
	}





	public void setR32_COM_PAPER(BigDecimal r32_COM_PAPER) {
		R32_COM_PAPER = r32_COM_PAPER;
	}





	public BigDecimal getR32_CERT_OF_DEP() {
		return R32_CERT_OF_DEP;
	}





	public void setR32_CERT_OF_DEP(BigDecimal r32_CERT_OF_DEP) {
		R32_CERT_OF_DEP = r32_CERT_OF_DEP;
	}





	public BigDecimal getR32_PLEDGED_ASSET() {
		return R32_PLEDGED_ASSET;
	}





	public void setR32_PLEDGED_ASSET(BigDecimal r32_PLEDGED_ASSET) {
		R32_PLEDGED_ASSET = r32_PLEDGED_ASSET;
	}





	public BigDecimal getR32_OTHER() {
		return R32_OTHER;
	}





	public void setR32_OTHER(BigDecimal r32_OTHER) {
		R32_OTHER = r32_OTHER;
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





	public BigDecimal getR33_BOBC() {
		return R33_BOBC;
	}





	public void setR33_BOBC(BigDecimal r33_BOBC) {
		R33_BOBC = r33_BOBC;
	}





	public BigDecimal getR33_TRES_BILLS() {
		return R33_TRES_BILLS;
	}





	public void setR33_TRES_BILLS(BigDecimal r33_TRES_BILLS) {
		R33_TRES_BILLS = r33_TRES_BILLS;
	}





	public BigDecimal getR33_REPURCHASE_AGREE() {
		return R33_REPURCHASE_AGREE;
	}





	public void setR33_REPURCHASE_AGREE(BigDecimal r33_REPURCHASE_AGREE) {
		R33_REPURCHASE_AGREE = r33_REPURCHASE_AGREE;
	}





	public BigDecimal getR33_COM_PAPER() {
		return R33_COM_PAPER;
	}





	public void setR33_COM_PAPER(BigDecimal r33_COM_PAPER) {
		R33_COM_PAPER = r33_COM_PAPER;
	}





	public BigDecimal getR33_CERT_OF_DEP() {
		return R33_CERT_OF_DEP;
	}





	public void setR33_CERT_OF_DEP(BigDecimal r33_CERT_OF_DEP) {
		R33_CERT_OF_DEP = r33_CERT_OF_DEP;
	}





	public BigDecimal getR33_PLEDGED_ASSET() {
		return R33_PLEDGED_ASSET;
	}





	public void setR33_PLEDGED_ASSET(BigDecimal r33_PLEDGED_ASSET) {
		R33_PLEDGED_ASSET = r33_PLEDGED_ASSET;
	}





	public BigDecimal getR33_OTHER() {
		return R33_OTHER;
	}





	public void setR33_OTHER(BigDecimal r33_OTHER) {
		R33_OTHER = r33_OTHER;
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





	public BigDecimal getR34_BOBC() {
		return R34_BOBC;
	}





	public void setR34_BOBC(BigDecimal r34_BOBC) {
		R34_BOBC = r34_BOBC;
	}





	public BigDecimal getR34_TRES_BILLS() {
		return R34_TRES_BILLS;
	}





	public void setR34_TRES_BILLS(BigDecimal r34_TRES_BILLS) {
		R34_TRES_BILLS = r34_TRES_BILLS;
	}





	public BigDecimal getR34_REPURCHASE_AGREE() {
		return R34_REPURCHASE_AGREE;
	}





	public void setR34_REPURCHASE_AGREE(BigDecimal r34_REPURCHASE_AGREE) {
		R34_REPURCHASE_AGREE = r34_REPURCHASE_AGREE;
	}





	public BigDecimal getR34_COM_PAPER() {
		return R34_COM_PAPER;
	}





	public void setR34_COM_PAPER(BigDecimal r34_COM_PAPER) {
		R34_COM_PAPER = r34_COM_PAPER;
	}





	public BigDecimal getR34_CERT_OF_DEP() {
		return R34_CERT_OF_DEP;
	}





	public void setR34_CERT_OF_DEP(BigDecimal r34_CERT_OF_DEP) {
		R34_CERT_OF_DEP = r34_CERT_OF_DEP;
	}





	public BigDecimal getR34_PLEDGED_ASSET() {
		return R34_PLEDGED_ASSET;
	}





	public void setR34_PLEDGED_ASSET(BigDecimal r34_PLEDGED_ASSET) {
		R34_PLEDGED_ASSET = r34_PLEDGED_ASSET;
	}





	public BigDecimal getR34_OTHER() {
		return R34_OTHER;
	}





	public void setR34_OTHER(BigDecimal r34_OTHER) {
		R34_OTHER = r34_OTHER;
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





	public BigDecimal getR35_BOBC() {
		return R35_BOBC;
	}





	public void setR35_BOBC(BigDecimal r35_BOBC) {
		R35_BOBC = r35_BOBC;
	}





	public BigDecimal getR35_TRES_BILLS() {
		return R35_TRES_BILLS;
	}





	public void setR35_TRES_BILLS(BigDecimal r35_TRES_BILLS) {
		R35_TRES_BILLS = r35_TRES_BILLS;
	}





	public BigDecimal getR35_REPURCHASE_AGREE() {
		return R35_REPURCHASE_AGREE;
	}





	public void setR35_REPURCHASE_AGREE(BigDecimal r35_REPURCHASE_AGREE) {
		R35_REPURCHASE_AGREE = r35_REPURCHASE_AGREE;
	}





	public BigDecimal getR35_COM_PAPER() {
		return R35_COM_PAPER;
	}





	public void setR35_COM_PAPER(BigDecimal r35_COM_PAPER) {
		R35_COM_PAPER = r35_COM_PAPER;
	}





	public BigDecimal getR35_CERT_OF_DEP() {
		return R35_CERT_OF_DEP;
	}





	public void setR35_CERT_OF_DEP(BigDecimal r35_CERT_OF_DEP) {
		R35_CERT_OF_DEP = r35_CERT_OF_DEP;
	}





	public BigDecimal getR35_PLEDGED_ASSET() {
		return R35_PLEDGED_ASSET;
	}





	public void setR35_PLEDGED_ASSET(BigDecimal r35_PLEDGED_ASSET) {
		R35_PLEDGED_ASSET = r35_PLEDGED_ASSET;
	}





	public BigDecimal getR35_OTHER() {
		return R35_OTHER;
	}





	public void setR35_OTHER(BigDecimal r35_OTHER) {
		R35_OTHER = r35_OTHER;
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





	public BigDecimal getR36_BOBC() {
		return R36_BOBC;
	}





	public void setR36_BOBC(BigDecimal r36_BOBC) {
		R36_BOBC = r36_BOBC;
	}





	public BigDecimal getR36_TRES_BILLS() {
		return R36_TRES_BILLS;
	}





	public void setR36_TRES_BILLS(BigDecimal r36_TRES_BILLS) {
		R36_TRES_BILLS = r36_TRES_BILLS;
	}





	public BigDecimal getR36_REPURCHASE_AGREE() {
		return R36_REPURCHASE_AGREE;
	}





	public void setR36_REPURCHASE_AGREE(BigDecimal r36_REPURCHASE_AGREE) {
		R36_REPURCHASE_AGREE = r36_REPURCHASE_AGREE;
	}





	public BigDecimal getR36_COM_PAPER() {
		return R36_COM_PAPER;
	}





	public void setR36_COM_PAPER(BigDecimal r36_COM_PAPER) {
		R36_COM_PAPER = r36_COM_PAPER;
	}





	public BigDecimal getR36_CERT_OF_DEP() {
		return R36_CERT_OF_DEP;
	}





	public void setR36_CERT_OF_DEP(BigDecimal r36_CERT_OF_DEP) {
		R36_CERT_OF_DEP = r36_CERT_OF_DEP;
	}





	public BigDecimal getR36_PLEDGED_ASSET() {
		return R36_PLEDGED_ASSET;
	}





	public void setR36_PLEDGED_ASSET(BigDecimal r36_PLEDGED_ASSET) {
		R36_PLEDGED_ASSET = r36_PLEDGED_ASSET;
	}





	public BigDecimal getR36_OTHER() {
		return R36_OTHER;
	}





	public void setR36_OTHER(BigDecimal r36_OTHER) {
		R36_OTHER = r36_OTHER;
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





	public BigDecimal getR37_BOBC() {
		return R37_BOBC;
	}





	public void setR37_BOBC(BigDecimal r37_BOBC) {
		R37_BOBC = r37_BOBC;
	}





	public BigDecimal getR37_TRES_BILLS() {
		return R37_TRES_BILLS;
	}





	public void setR37_TRES_BILLS(BigDecimal r37_TRES_BILLS) {
		R37_TRES_BILLS = r37_TRES_BILLS;
	}





	public BigDecimal getR37_REPURCHASE_AGREE() {
		return R37_REPURCHASE_AGREE;
	}





	public void setR37_REPURCHASE_AGREE(BigDecimal r37_REPURCHASE_AGREE) {
		R37_REPURCHASE_AGREE = r37_REPURCHASE_AGREE;
	}





	public BigDecimal getR37_COM_PAPER() {
		return R37_COM_PAPER;
	}





	public void setR37_COM_PAPER(BigDecimal r37_COM_PAPER) {
		R37_COM_PAPER = r37_COM_PAPER;
	}





	public BigDecimal getR37_CERT_OF_DEP() {
		return R37_CERT_OF_DEP;
	}





	public void setR37_CERT_OF_DEP(BigDecimal r37_CERT_OF_DEP) {
		R37_CERT_OF_DEP = r37_CERT_OF_DEP;
	}





	public BigDecimal getR37_PLEDGED_ASSET() {
		return R37_PLEDGED_ASSET;
	}





	public void setR37_PLEDGED_ASSET(BigDecimal r37_PLEDGED_ASSET) {
		R37_PLEDGED_ASSET = r37_PLEDGED_ASSET;
	}





	public BigDecimal getR37_OTHER() {
		return R37_OTHER;
	}





	public void setR37_OTHER(BigDecimal r37_OTHER) {
		R37_OTHER = r37_OTHER;
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





	public BigDecimal getR38_BOBC() {
		return R38_BOBC;
	}





	public void setR38_BOBC(BigDecimal r38_BOBC) {
		R38_BOBC = r38_BOBC;
	}





	public BigDecimal getR38_TRES_BILLS() {
		return R38_TRES_BILLS;
	}





	public void setR38_TRES_BILLS(BigDecimal r38_TRES_BILLS) {
		R38_TRES_BILLS = r38_TRES_BILLS;
	}





	public BigDecimal getR38_REPURCHASE_AGREE() {
		return R38_REPURCHASE_AGREE;
	}





	public void setR38_REPURCHASE_AGREE(BigDecimal r38_REPURCHASE_AGREE) {
		R38_REPURCHASE_AGREE = r38_REPURCHASE_AGREE;
	}





	public BigDecimal getR38_COM_PAPER() {
		return R38_COM_PAPER;
	}





	public void setR38_COM_PAPER(BigDecimal r38_COM_PAPER) {
		R38_COM_PAPER = r38_COM_PAPER;
	}





	public BigDecimal getR38_CERT_OF_DEP() {
		return R38_CERT_OF_DEP;
	}





	public void setR38_CERT_OF_DEP(BigDecimal r38_CERT_OF_DEP) {
		R38_CERT_OF_DEP = r38_CERT_OF_DEP;
	}





	public BigDecimal getR38_PLEDGED_ASSET() {
		return R38_PLEDGED_ASSET;
	}





	public void setR38_PLEDGED_ASSET(BigDecimal r38_PLEDGED_ASSET) {
		R38_PLEDGED_ASSET = r38_PLEDGED_ASSET;
	}





	public BigDecimal getR38_OTHER() {
		return R38_OTHER;
	}





	public void setR38_OTHER(BigDecimal r38_OTHER) {
		R38_OTHER = r38_OTHER;
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





	public BigDecimal getR39_BOBC() {
		return R39_BOBC;
	}





	public void setR39_BOBC(BigDecimal r39_BOBC) {
		R39_BOBC = r39_BOBC;
	}





	public BigDecimal getR39_TRES_BILLS() {
		return R39_TRES_BILLS;
	}





	public void setR39_TRES_BILLS(BigDecimal r39_TRES_BILLS) {
		R39_TRES_BILLS = r39_TRES_BILLS;
	}





	public BigDecimal getR39_REPURCHASE_AGREE() {
		return R39_REPURCHASE_AGREE;
	}





	public void setR39_REPURCHASE_AGREE(BigDecimal r39_REPURCHASE_AGREE) {
		R39_REPURCHASE_AGREE = r39_REPURCHASE_AGREE;
	}





	public BigDecimal getR39_COM_PAPER() {
		return R39_COM_PAPER;
	}





	public void setR39_COM_PAPER(BigDecimal r39_COM_PAPER) {
		R39_COM_PAPER = r39_COM_PAPER;
	}





	public BigDecimal getR39_CERT_OF_DEP() {
		return R39_CERT_OF_DEP;
	}





	public void setR39_CERT_OF_DEP(BigDecimal r39_CERT_OF_DEP) {
		R39_CERT_OF_DEP = r39_CERT_OF_DEP;
	}





	public BigDecimal getR39_PLEDGED_ASSET() {
		return R39_PLEDGED_ASSET;
	}





	public void setR39_PLEDGED_ASSET(BigDecimal r39_PLEDGED_ASSET) {
		R39_PLEDGED_ASSET = r39_PLEDGED_ASSET;
	}





	public BigDecimal getR39_OTHER() {
		return R39_OTHER;
	}





	public void setR39_OTHER(BigDecimal r39_OTHER) {
		R39_OTHER = r39_OTHER;
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





	public BigDecimal getR40_BOBC() {
		return R40_BOBC;
	}





	public void setR40_BOBC(BigDecimal r40_BOBC) {
		R40_BOBC = r40_BOBC;
	}





	public BigDecimal getR40_TRES_BILLS() {
		return R40_TRES_BILLS;
	}





	public void setR40_TRES_BILLS(BigDecimal r40_TRES_BILLS) {
		R40_TRES_BILLS = r40_TRES_BILLS;
	}





	public BigDecimal getR40_REPURCHASE_AGREE() {
		return R40_REPURCHASE_AGREE;
	}





	public void setR40_REPURCHASE_AGREE(BigDecimal r40_REPURCHASE_AGREE) {
		R40_REPURCHASE_AGREE = r40_REPURCHASE_AGREE;
	}





	public BigDecimal getR40_COM_PAPER() {
		return R40_COM_PAPER;
	}





	public void setR40_COM_PAPER(BigDecimal r40_COM_PAPER) {
		R40_COM_PAPER = r40_COM_PAPER;
	}





	public BigDecimal getR40_CERT_OF_DEP() {
		return R40_CERT_OF_DEP;
	}





	public void setR40_CERT_OF_DEP(BigDecimal r40_CERT_OF_DEP) {
		R40_CERT_OF_DEP = r40_CERT_OF_DEP;
	}





	public BigDecimal getR40_PLEDGED_ASSET() {
		return R40_PLEDGED_ASSET;
	}





	public void setR40_PLEDGED_ASSET(BigDecimal r40_PLEDGED_ASSET) {
		R40_PLEDGED_ASSET = r40_PLEDGED_ASSET;
	}





	public BigDecimal getR40_OTHER() {
		return R40_OTHER;
	}





	public void setR40_OTHER(BigDecimal r40_OTHER) {
		R40_OTHER = r40_OTHER;
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





	public BigDecimal getR41_BOBC() {
		return R41_BOBC;
	}





	public void setR41_BOBC(BigDecimal r41_BOBC) {
		R41_BOBC = r41_BOBC;
	}





	public BigDecimal getR41_TRES_BILLS() {
		return R41_TRES_BILLS;
	}





	public void setR41_TRES_BILLS(BigDecimal r41_TRES_BILLS) {
		R41_TRES_BILLS = r41_TRES_BILLS;
	}





	public BigDecimal getR41_REPURCHASE_AGREE() {
		return R41_REPURCHASE_AGREE;
	}





	public void setR41_REPURCHASE_AGREE(BigDecimal r41_REPURCHASE_AGREE) {
		R41_REPURCHASE_AGREE = r41_REPURCHASE_AGREE;
	}





	public BigDecimal getR41_COM_PAPER() {
		return R41_COM_PAPER;
	}





	public void setR41_COM_PAPER(BigDecimal r41_COM_PAPER) {
		R41_COM_PAPER = r41_COM_PAPER;
	}





	public BigDecimal getR41_CERT_OF_DEP() {
		return R41_CERT_OF_DEP;
	}





	public void setR41_CERT_OF_DEP(BigDecimal r41_CERT_OF_DEP) {
		R41_CERT_OF_DEP = r41_CERT_OF_DEP;
	}





	public BigDecimal getR41_PLEDGED_ASSET() {
		return R41_PLEDGED_ASSET;
	}





	public void setR41_PLEDGED_ASSET(BigDecimal r41_PLEDGED_ASSET) {
		R41_PLEDGED_ASSET = r41_PLEDGED_ASSET;
	}





	public BigDecimal getR41_OTHER() {
		return R41_OTHER;
	}





	public void setR41_OTHER(BigDecimal r41_OTHER) {
		R41_OTHER = r41_OTHER;
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





	public BigDecimal getR42_BOBC() {
		return R42_BOBC;
	}





	public void setR42_BOBC(BigDecimal r42_BOBC) {
		R42_BOBC = r42_BOBC;
	}





	public BigDecimal getR42_TRES_BILLS() {
		return R42_TRES_BILLS;
	}





	public void setR42_TRES_BILLS(BigDecimal r42_TRES_BILLS) {
		R42_TRES_BILLS = r42_TRES_BILLS;
	}





	public BigDecimal getR42_REPURCHASE_AGREE() {
		return R42_REPURCHASE_AGREE;
	}





	public void setR42_REPURCHASE_AGREE(BigDecimal r42_REPURCHASE_AGREE) {
		R42_REPURCHASE_AGREE = r42_REPURCHASE_AGREE;
	}





	public BigDecimal getR42_COM_PAPER() {
		return R42_COM_PAPER;
	}





	public void setR42_COM_PAPER(BigDecimal r42_COM_PAPER) {
		R42_COM_PAPER = r42_COM_PAPER;
	}





	public BigDecimal getR42_CERT_OF_DEP() {
		return R42_CERT_OF_DEP;
	}





	public void setR42_CERT_OF_DEP(BigDecimal r42_CERT_OF_DEP) {
		R42_CERT_OF_DEP = r42_CERT_OF_DEP;
	}





	public BigDecimal getR42_PLEDGED_ASSET() {
		return R42_PLEDGED_ASSET;
	}





	public void setR42_PLEDGED_ASSET(BigDecimal r42_PLEDGED_ASSET) {
		R42_PLEDGED_ASSET = r42_PLEDGED_ASSET;
	}





	public BigDecimal getR42_OTHER() {
		return R42_OTHER;
	}





	public void setR42_OTHER(BigDecimal r42_OTHER) {
		R42_OTHER = r42_OTHER;
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





	public BigDecimal getR43_BOBC() {
		return R43_BOBC;
	}





	public void setR43_BOBC(BigDecimal r43_BOBC) {
		R43_BOBC = r43_BOBC;
	}





	public BigDecimal getR43_TRES_BILLS() {
		return R43_TRES_BILLS;
	}





	public void setR43_TRES_BILLS(BigDecimal r43_TRES_BILLS) {
		R43_TRES_BILLS = r43_TRES_BILLS;
	}





	public BigDecimal getR43_REPURCHASE_AGREE() {
		return R43_REPURCHASE_AGREE;
	}





	public void setR43_REPURCHASE_AGREE(BigDecimal r43_REPURCHASE_AGREE) {
		R43_REPURCHASE_AGREE = r43_REPURCHASE_AGREE;
	}





	public BigDecimal getR43_COM_PAPER() {
		return R43_COM_PAPER;
	}





	public void setR43_COM_PAPER(BigDecimal r43_COM_PAPER) {
		R43_COM_PAPER = r43_COM_PAPER;
	}





	public BigDecimal getR43_CERT_OF_DEP() {
		return R43_CERT_OF_DEP;
	}





	public void setR43_CERT_OF_DEP(BigDecimal r43_CERT_OF_DEP) {
		R43_CERT_OF_DEP = r43_CERT_OF_DEP;
	}





	public BigDecimal getR43_PLEDGED_ASSET() {
		return R43_PLEDGED_ASSET;
	}





	public void setR43_PLEDGED_ASSET(BigDecimal r43_PLEDGED_ASSET) {
		R43_PLEDGED_ASSET = r43_PLEDGED_ASSET;
	}





	public BigDecimal getR43_OTHER() {
		return R43_OTHER;
	}





	public void setR43_OTHER(BigDecimal r43_OTHER) {
		R43_OTHER = r43_OTHER;
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





	public BigDecimal getR44_BOBC() {
		return R44_BOBC;
	}





	public void setR44_BOBC(BigDecimal r44_BOBC) {
		R44_BOBC = r44_BOBC;
	}





	public BigDecimal getR44_TRES_BILLS() {
		return R44_TRES_BILLS;
	}





	public void setR44_TRES_BILLS(BigDecimal r44_TRES_BILLS) {
		R44_TRES_BILLS = r44_TRES_BILLS;
	}





	public BigDecimal getR44_REPURCHASE_AGREE() {
		return R44_REPURCHASE_AGREE;
	}





	public void setR44_REPURCHASE_AGREE(BigDecimal r44_REPURCHASE_AGREE) {
		R44_REPURCHASE_AGREE = r44_REPURCHASE_AGREE;
	}





	public BigDecimal getR44_COM_PAPER() {
		return R44_COM_PAPER;
	}





	public void setR44_COM_PAPER(BigDecimal r44_COM_PAPER) {
		R44_COM_PAPER = r44_COM_PAPER;
	}





	public BigDecimal getR44_CERT_OF_DEP() {
		return R44_CERT_OF_DEP;
	}





	public void setR44_CERT_OF_DEP(BigDecimal r44_CERT_OF_DEP) {
		R44_CERT_OF_DEP = r44_CERT_OF_DEP;
	}





	public BigDecimal getR44_PLEDGED_ASSET() {
		return R44_PLEDGED_ASSET;
	}





	public void setR44_PLEDGED_ASSET(BigDecimal r44_PLEDGED_ASSET) {
		R44_PLEDGED_ASSET = r44_PLEDGED_ASSET;
	}





	public BigDecimal getR44_OTHER() {
		return R44_OTHER;
	}





	public void setR44_OTHER(BigDecimal r44_OTHER) {
		R44_OTHER = r44_OTHER;
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





	public BigDecimal getR45_BOBC() {
		return R45_BOBC;
	}





	public void setR45_BOBC(BigDecimal r45_BOBC) {
		R45_BOBC = r45_BOBC;
	}





	public BigDecimal getR45_TRES_BILLS() {
		return R45_TRES_BILLS;
	}





	public void setR45_TRES_BILLS(BigDecimal r45_TRES_BILLS) {
		R45_TRES_BILLS = r45_TRES_BILLS;
	}





	public BigDecimal getR45_REPURCHASE_AGREE() {
		return R45_REPURCHASE_AGREE;
	}





	public void setR45_REPURCHASE_AGREE(BigDecimal r45_REPURCHASE_AGREE) {
		R45_REPURCHASE_AGREE = r45_REPURCHASE_AGREE;
	}





	public BigDecimal getR45_COM_PAPER() {
		return R45_COM_PAPER;
	}





	public void setR45_COM_PAPER(BigDecimal r45_COM_PAPER) {
		R45_COM_PAPER = r45_COM_PAPER;
	}





	public BigDecimal getR45_CERT_OF_DEP() {
		return R45_CERT_OF_DEP;
	}





	public void setR45_CERT_OF_DEP(BigDecimal r45_CERT_OF_DEP) {
		R45_CERT_OF_DEP = r45_CERT_OF_DEP;
	}





	public BigDecimal getR45_PLEDGED_ASSET() {
		return R45_PLEDGED_ASSET;
	}





	public void setR45_PLEDGED_ASSET(BigDecimal r45_PLEDGED_ASSET) {
		R45_PLEDGED_ASSET = r45_PLEDGED_ASSET;
	}





	public BigDecimal getR45_OTHER() {
		return R45_OTHER;
	}





	public void setR45_OTHER(BigDecimal r45_OTHER) {
		R45_OTHER = r45_OTHER;
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





	public BigDecimal getR46_BOBC() {
		return R46_BOBC;
	}





	public void setR46_BOBC(BigDecimal r46_BOBC) {
		R46_BOBC = r46_BOBC;
	}





	public BigDecimal getR46_TRES_BILLS() {
		return R46_TRES_BILLS;
	}





	public void setR46_TRES_BILLS(BigDecimal r46_TRES_BILLS) {
		R46_TRES_BILLS = r46_TRES_BILLS;
	}





	public BigDecimal getR46_REPURCHASE_AGREE() {
		return R46_REPURCHASE_AGREE;
	}





	public void setR46_REPURCHASE_AGREE(BigDecimal r46_REPURCHASE_AGREE) {
		R46_REPURCHASE_AGREE = r46_REPURCHASE_AGREE;
	}





	public BigDecimal getR46_COM_PAPER() {
		return R46_COM_PAPER;
	}





	public void setR46_COM_PAPER(BigDecimal r46_COM_PAPER) {
		R46_COM_PAPER = r46_COM_PAPER;
	}





	public BigDecimal getR46_CERT_OF_DEP() {
		return R46_CERT_OF_DEP;
	}





	public void setR46_CERT_OF_DEP(BigDecimal r46_CERT_OF_DEP) {
		R46_CERT_OF_DEP = r46_CERT_OF_DEP;
	}





	public BigDecimal getR46_PLEDGED_ASSET() {
		return R46_PLEDGED_ASSET;
	}





	public void setR46_PLEDGED_ASSET(BigDecimal r46_PLEDGED_ASSET) {
		R46_PLEDGED_ASSET = r46_PLEDGED_ASSET;
	}





	public BigDecimal getR46_OTHER() {
		return R46_OTHER;
	}





	public void setR46_OTHER(BigDecimal r46_OTHER) {
		R46_OTHER = r46_OTHER;
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





	public BigDecimal getR47_BOBC() {
		return R47_BOBC;
	}





	public void setR47_BOBC(BigDecimal r47_BOBC) {
		R47_BOBC = r47_BOBC;
	}





	public BigDecimal getR47_TRES_BILLS() {
		return R47_TRES_BILLS;
	}





	public void setR47_TRES_BILLS(BigDecimal r47_TRES_BILLS) {
		R47_TRES_BILLS = r47_TRES_BILLS;
	}





	public BigDecimal getR47_REPURCHASE_AGREE() {
		return R47_REPURCHASE_AGREE;
	}





	public void setR47_REPURCHASE_AGREE(BigDecimal r47_REPURCHASE_AGREE) {
		R47_REPURCHASE_AGREE = r47_REPURCHASE_AGREE;
	}





	public BigDecimal getR47_COM_PAPER() {
		return R47_COM_PAPER;
	}





	public void setR47_COM_PAPER(BigDecimal r47_COM_PAPER) {
		R47_COM_PAPER = r47_COM_PAPER;
	}





	public BigDecimal getR47_CERT_OF_DEP() {
		return R47_CERT_OF_DEP;
	}





	public void setR47_CERT_OF_DEP(BigDecimal r47_CERT_OF_DEP) {
		R47_CERT_OF_DEP = r47_CERT_OF_DEP;
	}





	public BigDecimal getR47_PLEDGED_ASSET() {
		return R47_PLEDGED_ASSET;
	}





	public void setR47_PLEDGED_ASSET(BigDecimal r47_PLEDGED_ASSET) {
		R47_PLEDGED_ASSET = r47_PLEDGED_ASSET;
	}





	public BigDecimal getR47_OTHER() {
		return R47_OTHER;
	}





	public void setR47_OTHER(BigDecimal r47_OTHER) {
		R47_OTHER = r47_OTHER;
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





	public BigDecimal getR48_BOBC() {
		return R48_BOBC;
	}





	public void setR48_BOBC(BigDecimal r48_BOBC) {
		R48_BOBC = r48_BOBC;
	}





	public BigDecimal getR48_TRES_BILLS() {
		return R48_TRES_BILLS;
	}





	public void setR48_TRES_BILLS(BigDecimal r48_TRES_BILLS) {
		R48_TRES_BILLS = r48_TRES_BILLS;
	}





	public BigDecimal getR48_REPURCHASE_AGREE() {
		return R48_REPURCHASE_AGREE;
	}





	public void setR48_REPURCHASE_AGREE(BigDecimal r48_REPURCHASE_AGREE) {
		R48_REPURCHASE_AGREE = r48_REPURCHASE_AGREE;
	}





	public BigDecimal getR48_COM_PAPER() {
		return R48_COM_PAPER;
	}





	public void setR48_COM_PAPER(BigDecimal r48_COM_PAPER) {
		R48_COM_PAPER = r48_COM_PAPER;
	}





	public BigDecimal getR48_CERT_OF_DEP() {
		return R48_CERT_OF_DEP;
	}





	public void setR48_CERT_OF_DEP(BigDecimal r48_CERT_OF_DEP) {
		R48_CERT_OF_DEP = r48_CERT_OF_DEP;
	}





	public BigDecimal getR48_PLEDGED_ASSET() {
		return R48_PLEDGED_ASSET;
	}





	public void setR48_PLEDGED_ASSET(BigDecimal r48_PLEDGED_ASSET) {
		R48_PLEDGED_ASSET = r48_PLEDGED_ASSET;
	}





	public BigDecimal getR48_OTHER() {
		return R48_OTHER;
	}





	public void setR48_OTHER(BigDecimal r48_OTHER) {
		R48_OTHER = r48_OTHER;
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





	public BigDecimal getR49_BOBC() {
		return R49_BOBC;
	}





	public void setR49_BOBC(BigDecimal r49_BOBC) {
		R49_BOBC = r49_BOBC;
	}





	public BigDecimal getR49_TRES_BILLS() {
		return R49_TRES_BILLS;
	}





	public void setR49_TRES_BILLS(BigDecimal r49_TRES_BILLS) {
		R49_TRES_BILLS = r49_TRES_BILLS;
	}





	public BigDecimal getR49_REPURCHASE_AGREE() {
		return R49_REPURCHASE_AGREE;
	}





	public void setR49_REPURCHASE_AGREE(BigDecimal r49_REPURCHASE_AGREE) {
		R49_REPURCHASE_AGREE = r49_REPURCHASE_AGREE;
	}





	public BigDecimal getR49_COM_PAPER() {
		return R49_COM_PAPER;
	}





	public void setR49_COM_PAPER(BigDecimal r49_COM_PAPER) {
		R49_COM_PAPER = r49_COM_PAPER;
	}





	public BigDecimal getR49_CERT_OF_DEP() {
		return R49_CERT_OF_DEP;
	}





	public void setR49_CERT_OF_DEP(BigDecimal r49_CERT_OF_DEP) {
		R49_CERT_OF_DEP = r49_CERT_OF_DEP;
	}





	public BigDecimal getR49_PLEDGED_ASSET() {
		return R49_PLEDGED_ASSET;
	}





	public void setR49_PLEDGED_ASSET(BigDecimal r49_PLEDGED_ASSET) {
		R49_PLEDGED_ASSET = r49_PLEDGED_ASSET;
	}





	public BigDecimal getR49_OTHER() {
		return R49_OTHER;
	}





	public void setR49_OTHER(BigDecimal r49_OTHER) {
		R49_OTHER = r49_OTHER;
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





	public BigDecimal getR50_BOBC() {
		return R50_BOBC;
	}





	public void setR50_BOBC(BigDecimal r50_BOBC) {
		R50_BOBC = r50_BOBC;
	}





	public BigDecimal getR50_TRES_BILLS() {
		return R50_TRES_BILLS;
	}





	public void setR50_TRES_BILLS(BigDecimal r50_TRES_BILLS) {
		R50_TRES_BILLS = r50_TRES_BILLS;
	}





	public BigDecimal getR50_REPURCHASE_AGREE() {
		return R50_REPURCHASE_AGREE;
	}





	public void setR50_REPURCHASE_AGREE(BigDecimal r50_REPURCHASE_AGREE) {
		R50_REPURCHASE_AGREE = r50_REPURCHASE_AGREE;
	}





	public BigDecimal getR50_COM_PAPER() {
		return R50_COM_PAPER;
	}





	public void setR50_COM_PAPER(BigDecimal r50_COM_PAPER) {
		R50_COM_PAPER = r50_COM_PAPER;
	}





	public BigDecimal getR50_CERT_OF_DEP() {
		return R50_CERT_OF_DEP;
	}





	public void setR50_CERT_OF_DEP(BigDecimal r50_CERT_OF_DEP) {
		R50_CERT_OF_DEP = r50_CERT_OF_DEP;
	}





	public BigDecimal getR50_PLEDGED_ASSET() {
		return R50_PLEDGED_ASSET;
	}





	public void setR50_PLEDGED_ASSET(BigDecimal r50_PLEDGED_ASSET) {
		R50_PLEDGED_ASSET = r50_PLEDGED_ASSET;
	}





	public BigDecimal getR50_OTHER() {
		return R50_OTHER;
	}





	public void setR50_OTHER(BigDecimal r50_OTHER) {
		R50_OTHER = r50_OTHER;
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





	public BigDecimal getR51_BOBC() {
		return R51_BOBC;
	}





	public void setR51_BOBC(BigDecimal r51_BOBC) {
		R51_BOBC = r51_BOBC;
	}





	public BigDecimal getR51_TRES_BILLS() {
		return R51_TRES_BILLS;
	}





	public void setR51_TRES_BILLS(BigDecimal r51_TRES_BILLS) {
		R51_TRES_BILLS = r51_TRES_BILLS;
	}





	public BigDecimal getR51_REPURCHASE_AGREE() {
		return R51_REPURCHASE_AGREE;
	}





	public void setR51_REPURCHASE_AGREE(BigDecimal r51_REPURCHASE_AGREE) {
		R51_REPURCHASE_AGREE = r51_REPURCHASE_AGREE;
	}





	public BigDecimal getR51_COM_PAPER() {
		return R51_COM_PAPER;
	}





	public void setR51_COM_PAPER(BigDecimal r51_COM_PAPER) {
		R51_COM_PAPER = r51_COM_PAPER;
	}





	public BigDecimal getR51_CERT_OF_DEP() {
		return R51_CERT_OF_DEP;
	}





	public void setR51_CERT_OF_DEP(BigDecimal r51_CERT_OF_DEP) {
		R51_CERT_OF_DEP = r51_CERT_OF_DEP;
	}





	public BigDecimal getR51_PLEDGED_ASSET() {
		return R51_PLEDGED_ASSET;
	}





	public void setR51_PLEDGED_ASSET(BigDecimal r51_PLEDGED_ASSET) {
		R51_PLEDGED_ASSET = r51_PLEDGED_ASSET;
	}





	public BigDecimal getR51_OTHER() {
		return R51_OTHER;
	}





	public void setR51_OTHER(BigDecimal r51_OTHER) {
		R51_OTHER = r51_OTHER;
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





	public BigDecimal getR52_BOBC() {
		return R52_BOBC;
	}





	public void setR52_BOBC(BigDecimal r52_BOBC) {
		R52_BOBC = r52_BOBC;
	}





	public BigDecimal getR52_TRES_BILLS() {
		return R52_TRES_BILLS;
	}





	public void setR52_TRES_BILLS(BigDecimal r52_TRES_BILLS) {
		R52_TRES_BILLS = r52_TRES_BILLS;
	}





	public BigDecimal getR52_REPURCHASE_AGREE() {
		return R52_REPURCHASE_AGREE;
	}





	public void setR52_REPURCHASE_AGREE(BigDecimal r52_REPURCHASE_AGREE) {
		R52_REPURCHASE_AGREE = r52_REPURCHASE_AGREE;
	}





	public BigDecimal getR52_COM_PAPER() {
		return R52_COM_PAPER;
	}





	public void setR52_COM_PAPER(BigDecimal r52_COM_PAPER) {
		R52_COM_PAPER = r52_COM_PAPER;
	}





	public BigDecimal getR52_CERT_OF_DEP() {
		return R52_CERT_OF_DEP;
	}





	public void setR52_CERT_OF_DEP(BigDecimal r52_CERT_OF_DEP) {
		R52_CERT_OF_DEP = r52_CERT_OF_DEP;
	}





	public BigDecimal getR52_PLEDGED_ASSET() {
		return R52_PLEDGED_ASSET;
	}





	public void setR52_PLEDGED_ASSET(BigDecimal r52_PLEDGED_ASSET) {
		R52_PLEDGED_ASSET = r52_PLEDGED_ASSET;
	}





	public BigDecimal getR52_OTHER() {
		return R52_OTHER;
	}





	public void setR52_OTHER(BigDecimal r52_OTHER) {
		R52_OTHER = r52_OTHER;
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





	public BigDecimal getR53_BOBC() {
		return R53_BOBC;
	}





	public void setR53_BOBC(BigDecimal r53_BOBC) {
		R53_BOBC = r53_BOBC;
	}





	public BigDecimal getR53_TRES_BILLS() {
		return R53_TRES_BILLS;
	}





	public void setR53_TRES_BILLS(BigDecimal r53_TRES_BILLS) {
		R53_TRES_BILLS = r53_TRES_BILLS;
	}





	public BigDecimal getR53_REPURCHASE_AGREE() {
		return R53_REPURCHASE_AGREE;
	}





	public void setR53_REPURCHASE_AGREE(BigDecimal r53_REPURCHASE_AGREE) {
		R53_REPURCHASE_AGREE = r53_REPURCHASE_AGREE;
	}





	public BigDecimal getR53_COM_PAPER() {
		return R53_COM_PAPER;
	}





	public void setR53_COM_PAPER(BigDecimal r53_COM_PAPER) {
		R53_COM_PAPER = r53_COM_PAPER;
	}





	public BigDecimal getR53_CERT_OF_DEP() {
		return R53_CERT_OF_DEP;
	}





	public void setR53_CERT_OF_DEP(BigDecimal r53_CERT_OF_DEP) {
		R53_CERT_OF_DEP = r53_CERT_OF_DEP;
	}





	public BigDecimal getR53_PLEDGED_ASSET() {
		return R53_PLEDGED_ASSET;
	}





	public void setR53_PLEDGED_ASSET(BigDecimal r53_PLEDGED_ASSET) {
		R53_PLEDGED_ASSET = r53_PLEDGED_ASSET;
	}





	public BigDecimal getR53_OTHER() {
		return R53_OTHER;
	}





	public void setR53_OTHER(BigDecimal r53_OTHER) {
		R53_OTHER = r53_OTHER;
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





	public BigDecimal getR54_BOBC() {
		return R54_BOBC;
	}





	public void setR54_BOBC(BigDecimal r54_BOBC) {
		R54_BOBC = r54_BOBC;
	}





	public BigDecimal getR54_TRES_BILLS() {
		return R54_TRES_BILLS;
	}





	public void setR54_TRES_BILLS(BigDecimal r54_TRES_BILLS) {
		R54_TRES_BILLS = r54_TRES_BILLS;
	}





	public BigDecimal getR54_REPURCHASE_AGREE() {
		return R54_REPURCHASE_AGREE;
	}





	public void setR54_REPURCHASE_AGREE(BigDecimal r54_REPURCHASE_AGREE) {
		R54_REPURCHASE_AGREE = r54_REPURCHASE_AGREE;
	}





	public BigDecimal getR54_COM_PAPER() {
		return R54_COM_PAPER;
	}





	public void setR54_COM_PAPER(BigDecimal r54_COM_PAPER) {
		R54_COM_PAPER = r54_COM_PAPER;
	}





	public BigDecimal getR54_CERT_OF_DEP() {
		return R54_CERT_OF_DEP;
	}





	public void setR54_CERT_OF_DEP(BigDecimal r54_CERT_OF_DEP) {
		R54_CERT_OF_DEP = r54_CERT_OF_DEP;
	}





	public BigDecimal getR54_PLEDGED_ASSET() {
		return R54_PLEDGED_ASSET;
	}





	public void setR54_PLEDGED_ASSET(BigDecimal r54_PLEDGED_ASSET) {
		R54_PLEDGED_ASSET = r54_PLEDGED_ASSET;
	}





	public BigDecimal getR54_OTHER() {
		return R54_OTHER;
	}





	public void setR54_OTHER(BigDecimal r54_OTHER) {
		R54_OTHER = r54_OTHER;
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





	public BigDecimal getR55_BOBC() {
		return R55_BOBC;
	}





	public void setR55_BOBC(BigDecimal r55_BOBC) {
		R55_BOBC = r55_BOBC;
	}





	public BigDecimal getR55_TRES_BILLS() {
		return R55_TRES_BILLS;
	}





	public void setR55_TRES_BILLS(BigDecimal r55_TRES_BILLS) {
		R55_TRES_BILLS = r55_TRES_BILLS;
	}





	public BigDecimal getR55_REPURCHASE_AGREE() {
		return R55_REPURCHASE_AGREE;
	}





	public void setR55_REPURCHASE_AGREE(BigDecimal r55_REPURCHASE_AGREE) {
		R55_REPURCHASE_AGREE = r55_REPURCHASE_AGREE;
	}





	public BigDecimal getR55_COM_PAPER() {
		return R55_COM_PAPER;
	}





	public void setR55_COM_PAPER(BigDecimal r55_COM_PAPER) {
		R55_COM_PAPER = r55_COM_PAPER;
	}





	public BigDecimal getR55_CERT_OF_DEP() {
		return R55_CERT_OF_DEP;
	}





	public void setR55_CERT_OF_DEP(BigDecimal r55_CERT_OF_DEP) {
		R55_CERT_OF_DEP = r55_CERT_OF_DEP;
	}





	public BigDecimal getR55_PLEDGED_ASSET() {
		return R55_PLEDGED_ASSET;
	}





	public void setR55_PLEDGED_ASSET(BigDecimal r55_PLEDGED_ASSET) {
		R55_PLEDGED_ASSET = r55_PLEDGED_ASSET;
	}





	public BigDecimal getR55_OTHER() {
		return R55_OTHER;
	}





	public void setR55_OTHER(BigDecimal r55_OTHER) {
		R55_OTHER = r55_OTHER;
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





	public BigDecimal getR56_BOBC() {
		return R56_BOBC;
	}





	public void setR56_BOBC(BigDecimal r56_BOBC) {
		R56_BOBC = r56_BOBC;
	}





	public BigDecimal getR56_TRES_BILLS() {
		return R56_TRES_BILLS;
	}





	public void setR56_TRES_BILLS(BigDecimal r56_TRES_BILLS) {
		R56_TRES_BILLS = r56_TRES_BILLS;
	}





	public BigDecimal getR56_REPURCHASE_AGREE() {
		return R56_REPURCHASE_AGREE;
	}





	public void setR56_REPURCHASE_AGREE(BigDecimal r56_REPURCHASE_AGREE) {
		R56_REPURCHASE_AGREE = r56_REPURCHASE_AGREE;
	}





	public BigDecimal getR56_COM_PAPER() {
		return R56_COM_PAPER;
	}





	public void setR56_COM_PAPER(BigDecimal r56_COM_PAPER) {
		R56_COM_PAPER = r56_COM_PAPER;
	}





	public BigDecimal getR56_CERT_OF_DEP() {
		return R56_CERT_OF_DEP;
	}





	public void setR56_CERT_OF_DEP(BigDecimal r56_CERT_OF_DEP) {
		R56_CERT_OF_DEP = r56_CERT_OF_DEP;
	}





	public BigDecimal getR56_PLEDGED_ASSET() {
		return R56_PLEDGED_ASSET;
	}





	public void setR56_PLEDGED_ASSET(BigDecimal r56_PLEDGED_ASSET) {
		R56_PLEDGED_ASSET = r56_PLEDGED_ASSET;
	}





	public BigDecimal getR56_OTHER() {
		return R56_OTHER;
	}





	public void setR56_OTHER(BigDecimal r56_OTHER) {
		R56_OTHER = r56_OTHER;
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





	public BigDecimal getR57_BOBC() {
		return R57_BOBC;
	}





	public void setR57_BOBC(BigDecimal r57_BOBC) {
		R57_BOBC = r57_BOBC;
	}





	public BigDecimal getR57_TRES_BILLS() {
		return R57_TRES_BILLS;
	}





	public void setR57_TRES_BILLS(BigDecimal r57_TRES_BILLS) {
		R57_TRES_BILLS = r57_TRES_BILLS;
	}





	public BigDecimal getR57_REPURCHASE_AGREE() {
		return R57_REPURCHASE_AGREE;
	}





	public void setR57_REPURCHASE_AGREE(BigDecimal r57_REPURCHASE_AGREE) {
		R57_REPURCHASE_AGREE = r57_REPURCHASE_AGREE;
	}





	public BigDecimal getR57_COM_PAPER() {
		return R57_COM_PAPER;
	}





	public void setR57_COM_PAPER(BigDecimal r57_COM_PAPER) {
		R57_COM_PAPER = r57_COM_PAPER;
	}





	public BigDecimal getR57_CERT_OF_DEP() {
		return R57_CERT_OF_DEP;
	}





	public void setR57_CERT_OF_DEP(BigDecimal r57_CERT_OF_DEP) {
		R57_CERT_OF_DEP = r57_CERT_OF_DEP;
	}





	public BigDecimal getR57_PLEDGED_ASSET() {
		return R57_PLEDGED_ASSET;
	}





	public void setR57_PLEDGED_ASSET(BigDecimal r57_PLEDGED_ASSET) {
		R57_PLEDGED_ASSET = r57_PLEDGED_ASSET;
	}





	public BigDecimal getR57_OTHER() {
		return R57_OTHER;
	}





	public void setR57_OTHER(BigDecimal r57_OTHER) {
		R57_OTHER = r57_OTHER;
	}





	public BigDecimal getR57_TOTAL() {
		return R57_TOTAL;
	}





	public void setR57_TOTAL(BigDecimal r57_TOTAL) {
		R57_TOTAL = r57_TOTAL;
	}





	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}





	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}





	public BigDecimal getR58_EQUITY() {
		return R58_EQUITY;
	}





	public void setR58_EQUITY(BigDecimal r58_EQUITY) {
		R58_EQUITY = r58_EQUITY;
	}





	public BigDecimal getR58_BONDS() {
		return R58_BONDS;
	}





	public void setR58_BONDS(BigDecimal r58_BONDS) {
		R58_BONDS = r58_BONDS;
	}





	public BigDecimal getR58_BOBC() {
		return R58_BOBC;
	}





	public void setR58_BOBC(BigDecimal r58_BOBC) {
		R58_BOBC = r58_BOBC;
	}





	public BigDecimal getR58_TRES_BILLS() {
		return R58_TRES_BILLS;
	}





	public void setR58_TRES_BILLS(BigDecimal r58_TRES_BILLS) {
		R58_TRES_BILLS = r58_TRES_BILLS;
	}





	public BigDecimal getR58_REPURCHASE_AGREE() {
		return R58_REPURCHASE_AGREE;
	}





	public void setR58_REPURCHASE_AGREE(BigDecimal r58_REPURCHASE_AGREE) {
		R58_REPURCHASE_AGREE = r58_REPURCHASE_AGREE;
	}





	public BigDecimal getR58_COM_PAPER() {
		return R58_COM_PAPER;
	}





	public void setR58_COM_PAPER(BigDecimal r58_COM_PAPER) {
		R58_COM_PAPER = r58_COM_PAPER;
	}





	public BigDecimal getR58_CERT_OF_DEP() {
		return R58_CERT_OF_DEP;
	}





	public void setR58_CERT_OF_DEP(BigDecimal r58_CERT_OF_DEP) {
		R58_CERT_OF_DEP = r58_CERT_OF_DEP;
	}





	public BigDecimal getR58_PLEDGED_ASSET() {
		return R58_PLEDGED_ASSET;
	}





	public void setR58_PLEDGED_ASSET(BigDecimal r58_PLEDGED_ASSET) {
		R58_PLEDGED_ASSET = r58_PLEDGED_ASSET;
	}





	public BigDecimal getR58_OTHER() {
		return R58_OTHER;
	}





	public void setR58_OTHER(BigDecimal r58_OTHER) {
		R58_OTHER = r58_OTHER;
	}





	public BigDecimal getR58_TOTAL() {
		return R58_TOTAL;
	}





	public void setR58_TOTAL(BigDecimal r58_TOTAL) {
		R58_TOTAL = r58_TOTAL;
	}





	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}





	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}





	public BigDecimal getR59_EQUITY() {
		return R59_EQUITY;
	}





	public void setR59_EQUITY(BigDecimal r59_EQUITY) {
		R59_EQUITY = r59_EQUITY;
	}





	public BigDecimal getR59_BONDS() {
		return R59_BONDS;
	}





	public void setR59_BONDS(BigDecimal r59_BONDS) {
		R59_BONDS = r59_BONDS;
	}





	public BigDecimal getR59_BOBC() {
		return R59_BOBC;
	}





	public void setR59_BOBC(BigDecimal r59_BOBC) {
		R59_BOBC = r59_BOBC;
	}





	public BigDecimal getR59_TRES_BILLS() {
		return R59_TRES_BILLS;
	}





	public void setR59_TRES_BILLS(BigDecimal r59_TRES_BILLS) {
		R59_TRES_BILLS = r59_TRES_BILLS;
	}





	public BigDecimal getR59_REPURCHASE_AGREE() {
		return R59_REPURCHASE_AGREE;
	}





	public void setR59_REPURCHASE_AGREE(BigDecimal r59_REPURCHASE_AGREE) {
		R59_REPURCHASE_AGREE = r59_REPURCHASE_AGREE;
	}





	public BigDecimal getR59_COM_PAPER() {
		return R59_COM_PAPER;
	}





	public void setR59_COM_PAPER(BigDecimal r59_COM_PAPER) {
		R59_COM_PAPER = r59_COM_PAPER;
	}





	public BigDecimal getR59_CERT_OF_DEP() {
		return R59_CERT_OF_DEP;
	}





	public void setR59_CERT_OF_DEP(BigDecimal r59_CERT_OF_DEP) {
		R59_CERT_OF_DEP = r59_CERT_OF_DEP;
	}





	public BigDecimal getR59_PLEDGED_ASSET() {
		return R59_PLEDGED_ASSET;
	}





	public void setR59_PLEDGED_ASSET(BigDecimal r59_PLEDGED_ASSET) {
		R59_PLEDGED_ASSET = r59_PLEDGED_ASSET;
	}





	public BigDecimal getR59_OTHER() {
		return R59_OTHER;
	}





	public void setR59_OTHER(BigDecimal r59_OTHER) {
		R59_OTHER = r59_OTHER;
	}





	public BigDecimal getR59_TOTAL() {
		return R59_TOTAL;
	}





	public void setR59_TOTAL(BigDecimal r59_TOTAL) {
		R59_TOTAL = r59_TOTAL;
	}





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





	public M_SECA_Archival_Summary_Entity() {
        super();
    }

}