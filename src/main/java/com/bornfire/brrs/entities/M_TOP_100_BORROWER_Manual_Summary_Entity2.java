package com.bornfire.brrs.entities;

import java.lang.reflect.Method;
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
@Table(name = "BRRS_M_TOP_100_BORROWER_MANUAL_SUMMARYTABLE2")

public class M_TOP_100_BORROWER_Manual_Summary_Entity2 {

	
	private String R71_GROUP_CODE;
	private String R71_GROUP_NAME;
	private BigDecimal R71_CRM;
	private BigDecimal R71_NFBLT;
	private BigDecimal R71_NFBOS;
	private BigDecimal R71_CRM_2;
	private BigDecimal R71_NFB;
	private String R71_BOND;
	private String R71_CP;
	private String R71_EQULITY;
	private String R71_FOREX;
	private String R71_OTHERS;
	private String R71_INT_BANK;
	private String R71_DERIVATIVE;

	private String R72_GROUP_CODE;
	private String R72_GROUP_NAME;
	private BigDecimal R72_CRM;
	private BigDecimal R72_NFBLT;
	private BigDecimal R72_NFBOS;
	private BigDecimal R72_CRM_2;
	private BigDecimal R72_NFB;
	private String R72_BOND;
	private String R72_CP;
	private String R72_EQULITY;
	private String R72_FOREX;
	private String R72_OTHERS;
	private String R72_INT_BANK;
	private String R72_DERIVATIVE;

	private String R73_GROUP_CODE;
	private String R73_GROUP_NAME;
	private BigDecimal R73_CRM;
	private BigDecimal R73_NFBLT;
	private BigDecimal R73_NFBOS;
	private BigDecimal R73_CRM_2;
	private BigDecimal R73_NFB;
	private String R73_BOND;
	private String R73_CP;
	private String R73_EQULITY;
	private String R73_FOREX;
	private String R73_OTHERS;
	private String R73_INT_BANK;
	private String R73_DERIVATIVE;

	private String R74_GROUP_CODE;
	private String R74_GROUP_NAME;
	private BigDecimal R74_CRM;
	private BigDecimal R74_NFBLT;
	private BigDecimal R74_NFBOS;
	private BigDecimal R74_CRM_2;
	private BigDecimal R74_NFB;
	private String R74_BOND;
	private String R74_CP;
	private String R74_EQULITY;
	private String R74_FOREX;
	private String R74_OTHERS;
	private String R74_INT_BANK;
	private String R74_DERIVATIVE;

	private String R75_GROUP_CODE;
	private String R75_GROUP_NAME;
	private BigDecimal R75_CRM;
	private BigDecimal R75_NFBLT;
	private BigDecimal R75_NFBOS;
	private BigDecimal R75_CRM_2;
	private BigDecimal R75_NFB;
	private String R75_BOND;
	private String R75_CP;
	private String R75_EQULITY;
	private String R75_FOREX;
	private String R75_OTHERS;
	private String R75_INT_BANK;
	private String R75_DERIVATIVE;

	private String R76_GROUP_CODE;
	private String R76_GROUP_NAME;
	private BigDecimal R76_CRM;
	private BigDecimal R76_NFBLT;
	private BigDecimal R76_NFBOS;
	private BigDecimal R76_CRM_2;
	private BigDecimal R76_NFB;
	private String R76_BOND;
	private String R76_CP;
	private String R76_EQULITY;
	private String R76_FOREX;
	private String R76_OTHERS;
	private String R76_INT_BANK;
	private String R76_DERIVATIVE;

	private String R77_GROUP_CODE;
	private String R77_GROUP_NAME;
	private BigDecimal R77_CRM;
	private BigDecimal R77_NFBLT;
	private BigDecimal R77_NFBOS;
	private BigDecimal R77_CRM_2;
	private BigDecimal R77_NFB;
	private String R77_BOND;
	private String R77_CP;
	private String R77_EQULITY;
	private String R77_FOREX;
	private String R77_OTHERS;
	private String R77_INT_BANK;
	private String R77_DERIVATIVE;

	private String R78_GROUP_CODE;
	private String R78_GROUP_NAME;
	private BigDecimal R78_CRM;
	private BigDecimal R78_NFBLT;
	private BigDecimal R78_NFBOS;
	private BigDecimal R78_CRM_2;
	private BigDecimal R78_NFB;
	private String R78_BOND;
	private String R78_CP;
	private String R78_EQULITY;
	private String R78_FOREX;
	private String R78_OTHERS;
	private String R78_INT_BANK;
	private String R78_DERIVATIVE;

	private String R79_GROUP_CODE;
	private String R79_GROUP_NAME;
	private BigDecimal R79_CRM;
	private BigDecimal R79_NFBLT;
	private BigDecimal R79_NFBOS;
	private BigDecimal R79_CRM_2;
	private BigDecimal R79_NFB;
	private String R79_BOND;
	private String R79_CP;
	private String R79_EQULITY;
	private String R79_FOREX;
	private String R79_OTHERS;
	private String R79_INT_BANK;
	private String R79_DERIVATIVE;

	private String R80_GROUP_CODE;
	private String R80_GROUP_NAME;
	private BigDecimal R80_CRM;
	private BigDecimal R80_NFBLT;
	private BigDecimal R80_NFBOS;
	private BigDecimal R80_CRM_2;
	private BigDecimal R80_NFB;
	private String R80_BOND;
	private String R80_CP;
	private String R80_EQULITY;
	private String R80_FOREX;
	private String R80_OTHERS;
	private String R80_INT_BANK;
	private String R80_DERIVATIVE;

	private String R81_GROUP_CODE;
	private String R81_GROUP_NAME;
	private BigDecimal R81_CRM;
	private BigDecimal R81_NFBLT;
	private BigDecimal R81_NFBOS;
	private BigDecimal R81_CRM_2;
	private BigDecimal R81_NFB;
	private String R81_BOND;
	private String R81_CP;
	private String R81_EQULITY;
	private String R81_FOREX;
	private String R81_OTHERS;
	private String R81_INT_BANK;
	private String R81_DERIVATIVE;

	private String R82_GROUP_CODE;
	private String R82_GROUP_NAME;
	private BigDecimal R82_CRM;
	private BigDecimal R82_NFBLT;
	private BigDecimal R82_NFBOS;
	private BigDecimal R82_CRM_2;
	private BigDecimal R82_NFB;
	private String R82_BOND;
	private String R82_CP;
	private String R82_EQULITY;
	private String R82_FOREX;
	private String R82_OTHERS;
	private String R82_INT_BANK;
	private String R82_DERIVATIVE;

	private String R83_GROUP_CODE;
	private String R83_GROUP_NAME;
	private BigDecimal R83_CRM;
	private BigDecimal R83_NFBLT;
	private BigDecimal R83_NFBOS;
	private BigDecimal R83_CRM_2;
	private BigDecimal R83_NFB;
	private String R83_BOND;
	private String R83_CP;
	private String R83_EQULITY;
	private String R83_FOREX;
	private String R83_OTHERS;
	private String R83_INT_BANK;
	private String R83_DERIVATIVE;

	private String R84_GROUP_CODE;
	private String R84_GROUP_NAME;
	private BigDecimal R84_CRM;
	private BigDecimal R84_NFBLT;
	private BigDecimal R84_NFBOS;
	private BigDecimal R84_CRM_2;
	private BigDecimal R84_NFB;
	private String R84_BOND;
	private String R84_CP;
	private String R84_EQULITY;
	private String R84_FOREX;
	private String R84_OTHERS;
	private String R84_INT_BANK;
	private String R84_DERIVATIVE;

	private String R85_GROUP_CODE;
	private String R85_GROUP_NAME;
	private BigDecimal R85_CRM;
	private BigDecimal R85_NFBLT;
	private BigDecimal R85_NFBOS;
	private BigDecimal R85_CRM_2;
	private BigDecimal R85_NFB;
	private String R85_BOND;
	private String R85_CP;
	private String R85_EQULITY;
	private String R85_FOREX;
	private String R85_OTHERS;
	private String R85_INT_BANK;
	private String R85_DERIVATIVE;

	private String R86_GROUP_CODE;
	private String R86_GROUP_NAME;
	private BigDecimal R86_CRM;
	private BigDecimal R86_NFBLT;
	private BigDecimal R86_NFBOS;
	private BigDecimal R86_CRM_2;
	private BigDecimal R86_NFB;
	private String R86_BOND;
	private String R86_CP;
	private String R86_EQULITY;
	private String R86_FOREX;
	private String R86_OTHERS;
	private String R86_INT_BANK;
	private String R86_DERIVATIVE;

	private String R87_GROUP_CODE;
	private String R87_GROUP_NAME;
	private BigDecimal R87_CRM;
	private BigDecimal R87_NFBLT;
	private BigDecimal R87_NFBOS;
	private BigDecimal R87_CRM_2;
	private BigDecimal R87_NFB;
	private String R87_BOND;
	private String R87_CP;
	private String R87_EQULITY;
	private String R87_FOREX;
	private String R87_OTHERS;
	private String R87_INT_BANK;
	private String R87_DERIVATIVE;

	private String R88_GROUP_CODE;
	private String R88_GROUP_NAME;
	private BigDecimal R88_CRM;
	private BigDecimal R88_NFBLT;
	private BigDecimal R88_NFBOS;
	private BigDecimal R88_CRM_2;
	private BigDecimal R88_NFB;
	private String R88_BOND;
	private String R88_CP;
	private String R88_EQULITY;
	private String R88_FOREX;
	private String R88_OTHERS;
	private String R88_INT_BANK;
	private String R88_DERIVATIVE;

	private String R89_GROUP_CODE;
	private String R89_GROUP_NAME;
	private BigDecimal R89_CRM;
	private BigDecimal R89_NFBLT;
	private BigDecimal R89_NFBOS;
	private BigDecimal R89_CRM_2;
	private BigDecimal R89_NFB;
	private String R89_BOND;
	private String R89_CP;
	private String R89_EQULITY;
	private String R89_FOREX;
	private String R89_OTHERS;
	private String R89_INT_BANK;
	private String R89_DERIVATIVE;

	private String R90_GROUP_CODE;
	private String R90_GROUP_NAME;
	private BigDecimal R90_CRM;
	private BigDecimal R90_NFBLT;
	private BigDecimal R90_NFBOS;
	private BigDecimal R90_CRM_2;
	private BigDecimal R90_NFB;
	private String R90_BOND;
	private String R90_CP;
	private String R90_EQULITY;
	private String R90_FOREX;
	private String R90_OTHERS;
	private String R90_INT_BANK;
	private String R90_DERIVATIVE;

	private String R91_GROUP_CODE;
	private String R91_GROUP_NAME;
	private BigDecimal R91_CRM;
	private BigDecimal R91_NFBLT;
	private BigDecimal R91_NFBOS;
	private BigDecimal R91_CRM_2;
	private BigDecimal R91_NFB;
	private String R91_BOND;
	private String R91_CP;
	private String R91_EQULITY;
	private String R91_FOREX;
	private String R91_OTHERS;
	private String R91_INT_BANK;
	private String R91_DERIVATIVE;

	private String R92_GROUP_CODE;
	private String R92_GROUP_NAME;
	private BigDecimal R92_CRM;
	private BigDecimal R92_NFBLT;
	private BigDecimal R92_NFBOS;
	private BigDecimal R92_CRM_2;
	private BigDecimal R92_NFB;
	private String R92_BOND;
	private String R92_CP;
	private String R92_EQULITY;
	private String R92_FOREX;
	private String R92_OTHERS;
	private String R92_INT_BANK;
	private String R92_DERIVATIVE;

	private String R93_GROUP_CODE;
	private String R93_GROUP_NAME;
	private BigDecimal R93_CRM;
	private BigDecimal R93_NFBLT;
	private BigDecimal R93_NFBOS;
	private BigDecimal R93_CRM_2;
	private BigDecimal R93_NFB;
	private String R93_BOND;
	private String R93_CP;
	private String R93_EQULITY;
	private String R93_FOREX;
	private String R93_OTHERS;
	private String R93_INT_BANK;
	private String R93_DERIVATIVE;

	private String R94_GROUP_CODE;
	private String R94_GROUP_NAME;
	private BigDecimal R94_CRM;
	private BigDecimal R94_NFBLT;
	private BigDecimal R94_NFBOS;
	private BigDecimal R94_CRM_2;
	private BigDecimal R94_NFB;
	private String R94_BOND;
	private String R94_CP;
	private String R94_EQULITY;
	private String R94_FOREX;
	private String R94_OTHERS;
	private String R94_INT_BANK;
	private String R94_DERIVATIVE;

	private String R95_GROUP_CODE;
	private String R95_GROUP_NAME;
	private BigDecimal R95_CRM;
	private BigDecimal R95_NFBLT;
	private BigDecimal R95_NFBOS;
	private BigDecimal R95_CRM_2;
	private BigDecimal R95_NFB;
	private String R95_BOND;
	private String R95_CP;
	private String R95_EQULITY;
	private String R95_FOREX;
	private String R95_OTHERS;
	private String R95_INT_BANK;
	private String R95_DERIVATIVE;

	private String R96_GROUP_CODE;
	private String R96_GROUP_NAME;
	private BigDecimal R96_CRM;
	private BigDecimal R96_NFBLT;
	private BigDecimal R96_NFBOS;
	private BigDecimal R96_CRM_2;
	private BigDecimal R96_NFB;
	private String R96_BOND;
	private String R96_CP;
	private String R96_EQULITY;
	private String R96_FOREX;
	private String R96_OTHERS;
	private String R96_INT_BANK;
	private String R96_DERIVATIVE;

	private String R97_GROUP_CODE;
	private String R97_GROUP_NAME;
	private BigDecimal R97_CRM;
	private BigDecimal R97_NFBLT;
	private BigDecimal R97_NFBOS;
	private BigDecimal R97_CRM_2;
	private BigDecimal R97_NFB;
	private String R97_BOND;
	private String R97_CP;
	private String R97_EQULITY;
	private String R97_FOREX;
	private String R97_OTHERS;
	private String R97_INT_BANK;
	private String R97_DERIVATIVE;

	private String R98_GROUP_CODE;
	private String R98_GROUP_NAME;
	private BigDecimal R98_CRM;
	private BigDecimal R98_NFBLT;
	private BigDecimal R98_NFBOS;
	private BigDecimal R98_CRM_2;
	private BigDecimal R98_NFB;
	private String R98_BOND;
	private String R98_CP;
	private String R98_EQULITY;
	private String R98_FOREX;
	private String R98_OTHERS;
	private String R98_INT_BANK;
	private String R98_DERIVATIVE;

	private String R99_GROUP_CODE;
	private String R99_GROUP_NAME;
	private BigDecimal R99_CRM;
	private BigDecimal R99_NFBLT;
	private BigDecimal R99_NFBOS;
	private BigDecimal R99_CRM_2;
	private BigDecimal R99_NFB;
	private String R99_BOND;
	private String R99_CP;
	private String R99_EQULITY;
	private String R99_FOREX;
	private String R99_OTHERS;
	private String R99_INT_BANK;
	private String R99_DERIVATIVE;

	private String R100_GROUP_CODE;
	private String R100_GROUP_NAME;
	private BigDecimal R100_CRM;
	private BigDecimal R100_NFBLT;
	private BigDecimal R100_NFBOS;
	private BigDecimal R100_CRM_2;
	private BigDecimal R100_NFB;
	private String R100_BOND;
	private String R100_CP;
	private String R100_EQULITY;
	private String R100_FOREX;
	private String R100_OTHERS;
	private String R100_INT_BANK;
	private String R100_DERIVATIVE;

	private String R101_GROUP_CODE;
	private String R101_GROUP_NAME;
	private BigDecimal R101_CRM;
	private BigDecimal R101_NFBLT;
	private BigDecimal R101_NFBOS;
	private BigDecimal R101_CRM_2;
	private BigDecimal R101_NFB;
	private String R101_BOND;
	private String R101_CP;
	private String R101_EQULITY;
	private String R101_FOREX;
	private String R101_OTHERS;
	private String R101_INT_BANK;
	private String R101_DERIVATIVE;

	private String R102_GROUP_CODE;
	private String R102_GROUP_NAME;
	private BigDecimal R102_CRM;
	private BigDecimal R102_NFBLT;
	private BigDecimal R102_NFBOS;
	private BigDecimal R102_CRM_2;
	private BigDecimal R102_NFB;
	private String R102_BOND;
	private String R102_CP;
	private String R102_EQULITY;
	private String R102_FOREX;
	private String R102_OTHERS;
	private String R102_INT_BANK;
	private String R102_DERIVATIVE;
    
    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
    private Date report_date;
    private BigDecimal report_version;
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;
    
    
	public String getR71_GROUP_CODE() {
		return R71_GROUP_CODE;
	}
	public void setR71_GROUP_CODE(String r71_GROUP_CODE) {
		R71_GROUP_CODE = r71_GROUP_CODE;
	}
	public String getR71_GROUP_NAME() {
		return R71_GROUP_NAME;
	}
	public void setR71_GROUP_NAME(String r71_GROUP_NAME) {
		R71_GROUP_NAME = r71_GROUP_NAME;
	}
	public BigDecimal getR71_CRM() {
		return R71_CRM;
	}
	public void setR71_CRM(BigDecimal r71_CRM) {
		R71_CRM = r71_CRM;
	}
	public BigDecimal getR71_NFBLT() {
		return R71_NFBLT;
	}
	public void setR71_NFBLT(BigDecimal r71_NFBLT) {
		R71_NFBLT = r71_NFBLT;
	}
	public BigDecimal getR71_NFBOS() {
		return R71_NFBOS;
	}
	public void setR71_NFBOS(BigDecimal r71_NFBOS) {
		R71_NFBOS = r71_NFBOS;
	}
	public BigDecimal getR71_CRM_2() {
		return R71_CRM_2;
	}
	public void setR71_CRM_2(BigDecimal r71_CRM_2) {
		R71_CRM_2 = r71_CRM_2;
	}
	public BigDecimal getR71_NFB() {
		return R71_NFB;
	}
	public void setR71_NFB(BigDecimal r71_NFB) {
		R71_NFB = r71_NFB;
	}
	public String getR71_BOND() {
		return R71_BOND;
	}
	public void setR71_BOND(String r71_BOND) {
		R71_BOND = r71_BOND;
	}
	public String getR71_CP() {
		return R71_CP;
	}
	public void setR71_CP(String r71_CP) {
		R71_CP = r71_CP;
	}
	public String getR71_EQULITY() {
		return R71_EQULITY;
	}
	public void setR71_EQULITY(String r71_EQULITY) {
		R71_EQULITY = r71_EQULITY;
	}
	public String getR71_FOREX() {
		return R71_FOREX;
	}
	public void setR71_FOREX(String r71_FOREX) {
		R71_FOREX = r71_FOREX;
	}
	public String getR71_OTHERS() {
		return R71_OTHERS;
	}
	public void setR71_OTHERS(String r71_OTHERS) {
		R71_OTHERS = r71_OTHERS;
	}
	public String getR71_INT_BANK() {
		return R71_INT_BANK;
	}
	public void setR71_INT_BANK(String r71_INT_BANK) {
		R71_INT_BANK = r71_INT_BANK;
	}
	public String getR71_DERIVATIVE() {
		return R71_DERIVATIVE;
	}
	public void setR71_DERIVATIVE(String r71_DERIVATIVE) {
		R71_DERIVATIVE = r71_DERIVATIVE;
	}
	public String getR72_GROUP_CODE() {
		return R72_GROUP_CODE;
	}
	public void setR72_GROUP_CODE(String r72_GROUP_CODE) {
		R72_GROUP_CODE = r72_GROUP_CODE;
	}
	public String getR72_GROUP_NAME() {
		return R72_GROUP_NAME;
	}
	public void setR72_GROUP_NAME(String r72_GROUP_NAME) {
		R72_GROUP_NAME = r72_GROUP_NAME;
	}
	public BigDecimal getR72_CRM() {
		return R72_CRM;
	}
	public void setR72_CRM(BigDecimal r72_CRM) {
		R72_CRM = r72_CRM;
	}
	public BigDecimal getR72_NFBLT() {
		return R72_NFBLT;
	}
	public void setR72_NFBLT(BigDecimal r72_NFBLT) {
		R72_NFBLT = r72_NFBLT;
	}
	public BigDecimal getR72_NFBOS() {
		return R72_NFBOS;
	}
	public void setR72_NFBOS(BigDecimal r72_NFBOS) {
		R72_NFBOS = r72_NFBOS;
	}
	public BigDecimal getR72_CRM_2() {
		return R72_CRM_2;
	}
	public void setR72_CRM_2(BigDecimal r72_CRM_2) {
		R72_CRM_2 = r72_CRM_2;
	}
	public BigDecimal getR72_NFB() {
		return R72_NFB;
	}
	public void setR72_NFB(BigDecimal r72_NFB) {
		R72_NFB = r72_NFB;
	}
	public String getR72_BOND() {
		return R72_BOND;
	}
	public void setR72_BOND(String r72_BOND) {
		R72_BOND = r72_BOND;
	}
	public String getR72_CP() {
		return R72_CP;
	}
	public void setR72_CP(String r72_CP) {
		R72_CP = r72_CP;
	}
	public String getR72_EQULITY() {
		return R72_EQULITY;
	}
	public void setR72_EQULITY(String r72_EQULITY) {
		R72_EQULITY = r72_EQULITY;
	}
	public String getR72_FOREX() {
		return R72_FOREX;
	}
	public void setR72_FOREX(String r72_FOREX) {
		R72_FOREX = r72_FOREX;
	}
	public String getR72_OTHERS() {
		return R72_OTHERS;
	}
	public void setR72_OTHERS(String r72_OTHERS) {
		R72_OTHERS = r72_OTHERS;
	}
	public String getR72_INT_BANK() {
		return R72_INT_BANK;
	}
	public void setR72_INT_BANK(String r72_INT_BANK) {
		R72_INT_BANK = r72_INT_BANK;
	}
	public String getR72_DERIVATIVE() {
		return R72_DERIVATIVE;
	}
	public void setR72_DERIVATIVE(String r72_DERIVATIVE) {
		R72_DERIVATIVE = r72_DERIVATIVE;
	}
	public String getR73_GROUP_CODE() {
		return R73_GROUP_CODE;
	}
	public void setR73_GROUP_CODE(String r73_GROUP_CODE) {
		R73_GROUP_CODE = r73_GROUP_CODE;
	}
	public String getR73_GROUP_NAME() {
		return R73_GROUP_NAME;
	}
	public void setR73_GROUP_NAME(String r73_GROUP_NAME) {
		R73_GROUP_NAME = r73_GROUP_NAME;
	}
	public BigDecimal getR73_CRM() {
		return R73_CRM;
	}
	public void setR73_CRM(BigDecimal r73_CRM) {
		R73_CRM = r73_CRM;
	}
	public BigDecimal getR73_NFBLT() {
		return R73_NFBLT;
	}
	public void setR73_NFBLT(BigDecimal r73_NFBLT) {
		R73_NFBLT = r73_NFBLT;
	}
	public BigDecimal getR73_NFBOS() {
		return R73_NFBOS;
	}
	public void setR73_NFBOS(BigDecimal r73_NFBOS) {
		R73_NFBOS = r73_NFBOS;
	}
	public BigDecimal getR73_CRM_2() {
		return R73_CRM_2;
	}
	public void setR73_CRM_2(BigDecimal r73_CRM_2) {
		R73_CRM_2 = r73_CRM_2;
	}
	public BigDecimal getR73_NFB() {
		return R73_NFB;
	}
	public void setR73_NFB(BigDecimal r73_NFB) {
		R73_NFB = r73_NFB;
	}
	public String getR73_BOND() {
		return R73_BOND;
	}
	public void setR73_BOND(String r73_BOND) {
		R73_BOND = r73_BOND;
	}
	public String getR73_CP() {
		return R73_CP;
	}
	public void setR73_CP(String r73_CP) {
		R73_CP = r73_CP;
	}
	public String getR73_EQULITY() {
		return R73_EQULITY;
	}
	public void setR73_EQULITY(String r73_EQULITY) {
		R73_EQULITY = r73_EQULITY;
	}
	public String getR73_FOREX() {
		return R73_FOREX;
	}
	public void setR73_FOREX(String r73_FOREX) {
		R73_FOREX = r73_FOREX;
	}
	public String getR73_OTHERS() {
		return R73_OTHERS;
	}
	public void setR73_OTHERS(String r73_OTHERS) {
		R73_OTHERS = r73_OTHERS;
	}
	public String getR73_INT_BANK() {
		return R73_INT_BANK;
	}
	public void setR73_INT_BANK(String r73_INT_BANK) {
		R73_INT_BANK = r73_INT_BANK;
	}
	public String getR73_DERIVATIVE() {
		return R73_DERIVATIVE;
	}
	public void setR73_DERIVATIVE(String r73_DERIVATIVE) {
		R73_DERIVATIVE = r73_DERIVATIVE;
	}
	public String getR74_GROUP_CODE() {
		return R74_GROUP_CODE;
	}
	public void setR74_GROUP_CODE(String r74_GROUP_CODE) {
		R74_GROUP_CODE = r74_GROUP_CODE;
	}
	public String getR74_GROUP_NAME() {
		return R74_GROUP_NAME;
	}
	public void setR74_GROUP_NAME(String r74_GROUP_NAME) {
		R74_GROUP_NAME = r74_GROUP_NAME;
	}
	public BigDecimal getR74_CRM() {
		return R74_CRM;
	}
	public void setR74_CRM(BigDecimal r74_CRM) {
		R74_CRM = r74_CRM;
	}
	public BigDecimal getR74_NFBLT() {
		return R74_NFBLT;
	}
	public void setR74_NFBLT(BigDecimal r74_NFBLT) {
		R74_NFBLT = r74_NFBLT;
	}
	public BigDecimal getR74_NFBOS() {
		return R74_NFBOS;
	}
	public void setR74_NFBOS(BigDecimal r74_NFBOS) {
		R74_NFBOS = r74_NFBOS;
	}
	public BigDecimal getR74_CRM_2() {
		return R74_CRM_2;
	}
	public void setR74_CRM_2(BigDecimal r74_CRM_2) {
		R74_CRM_2 = r74_CRM_2;
	}
	public BigDecimal getR74_NFB() {
		return R74_NFB;
	}
	public void setR74_NFB(BigDecimal r74_NFB) {
		R74_NFB = r74_NFB;
	}
	public String getR74_BOND() {
		return R74_BOND;
	}
	public void setR74_BOND(String r74_BOND) {
		R74_BOND = r74_BOND;
	}
	public String getR74_CP() {
		return R74_CP;
	}
	public void setR74_CP(String r74_CP) {
		R74_CP = r74_CP;
	}
	public String getR74_EQULITY() {
		return R74_EQULITY;
	}
	public void setR74_EQULITY(String r74_EQULITY) {
		R74_EQULITY = r74_EQULITY;
	}
	public String getR74_FOREX() {
		return R74_FOREX;
	}
	public void setR74_FOREX(String r74_FOREX) {
		R74_FOREX = r74_FOREX;
	}
	public String getR74_OTHERS() {
		return R74_OTHERS;
	}
	public void setR74_OTHERS(String r74_OTHERS) {
		R74_OTHERS = r74_OTHERS;
	}
	public String getR74_INT_BANK() {
		return R74_INT_BANK;
	}
	public void setR74_INT_BANK(String r74_INT_BANK) {
		R74_INT_BANK = r74_INT_BANK;
	}
	public String getR74_DERIVATIVE() {
		return R74_DERIVATIVE;
	}
	public void setR74_DERIVATIVE(String r74_DERIVATIVE) {
		R74_DERIVATIVE = r74_DERIVATIVE;
	}
	public String getR75_GROUP_CODE() {
		return R75_GROUP_CODE;
	}
	public void setR75_GROUP_CODE(String r75_GROUP_CODE) {
		R75_GROUP_CODE = r75_GROUP_CODE;
	}
	public String getR75_GROUP_NAME() {
		return R75_GROUP_NAME;
	}
	public void setR75_GROUP_NAME(String r75_GROUP_NAME) {
		R75_GROUP_NAME = r75_GROUP_NAME;
	}
	public BigDecimal getR75_CRM() {
		return R75_CRM;
	}
	public void setR75_CRM(BigDecimal r75_CRM) {
		R75_CRM = r75_CRM;
	}
	public BigDecimal getR75_NFBLT() {
		return R75_NFBLT;
	}
	public void setR75_NFBLT(BigDecimal r75_NFBLT) {
		R75_NFBLT = r75_NFBLT;
	}
	public BigDecimal getR75_NFBOS() {
		return R75_NFBOS;
	}
	public void setR75_NFBOS(BigDecimal r75_NFBOS) {
		R75_NFBOS = r75_NFBOS;
	}
	public BigDecimal getR75_CRM_2() {
		return R75_CRM_2;
	}
	public void setR75_CRM_2(BigDecimal r75_CRM_2) {
		R75_CRM_2 = r75_CRM_2;
	}
	public BigDecimal getR75_NFB() {
		return R75_NFB;
	}
	public void setR75_NFB(BigDecimal r75_NFB) {
		R75_NFB = r75_NFB;
	}
	public String getR75_BOND() {
		return R75_BOND;
	}
	public void setR75_BOND(String r75_BOND) {
		R75_BOND = r75_BOND;
	}
	public String getR75_CP() {
		return R75_CP;
	}
	public void setR75_CP(String r75_CP) {
		R75_CP = r75_CP;
	}
	public String getR75_EQULITY() {
		return R75_EQULITY;
	}
	public void setR75_EQULITY(String r75_EQULITY) {
		R75_EQULITY = r75_EQULITY;
	}
	public String getR75_FOREX() {
		return R75_FOREX;
	}
	public void setR75_FOREX(String r75_FOREX) {
		R75_FOREX = r75_FOREX;
	}
	public String getR75_OTHERS() {
		return R75_OTHERS;
	}
	public void setR75_OTHERS(String r75_OTHERS) {
		R75_OTHERS = r75_OTHERS;
	}
	public String getR75_INT_BANK() {
		return R75_INT_BANK;
	}
	public void setR75_INT_BANK(String r75_INT_BANK) {
		R75_INT_BANK = r75_INT_BANK;
	}
	public String getR75_DERIVATIVE() {
		return R75_DERIVATIVE;
	}
	public void setR75_DERIVATIVE(String r75_DERIVATIVE) {
		R75_DERIVATIVE = r75_DERIVATIVE;
	}
	public String getR76_GROUP_CODE() {
		return R76_GROUP_CODE;
	}
	public void setR76_GROUP_CODE(String r76_GROUP_CODE) {
		R76_GROUP_CODE = r76_GROUP_CODE;
	}
	public String getR76_GROUP_NAME() {
		return R76_GROUP_NAME;
	}
	public void setR76_GROUP_NAME(String r76_GROUP_NAME) {
		R76_GROUP_NAME = r76_GROUP_NAME;
	}
	public BigDecimal getR76_CRM() {
		return R76_CRM;
	}
	public void setR76_CRM(BigDecimal r76_CRM) {
		R76_CRM = r76_CRM;
	}
	public BigDecimal getR76_NFBLT() {
		return R76_NFBLT;
	}
	public void setR76_NFBLT(BigDecimal r76_NFBLT) {
		R76_NFBLT = r76_NFBLT;
	}
	public BigDecimal getR76_NFBOS() {
		return R76_NFBOS;
	}
	public void setR76_NFBOS(BigDecimal r76_NFBOS) {
		R76_NFBOS = r76_NFBOS;
	}
	public BigDecimal getR76_CRM_2() {
		return R76_CRM_2;
	}
	public void setR76_CRM_2(BigDecimal r76_CRM_2) {
		R76_CRM_2 = r76_CRM_2;
	}
	public BigDecimal getR76_NFB() {
		return R76_NFB;
	}
	public void setR76_NFB(BigDecimal r76_NFB) {
		R76_NFB = r76_NFB;
	}
	public String getR76_BOND() {
		return R76_BOND;
	}
	public void setR76_BOND(String r76_BOND) {
		R76_BOND = r76_BOND;
	}
	public String getR76_CP() {
		return R76_CP;
	}
	public void setR76_CP(String r76_CP) {
		R76_CP = r76_CP;
	}
	public String getR76_EQULITY() {
		return R76_EQULITY;
	}
	public void setR76_EQULITY(String r76_EQULITY) {
		R76_EQULITY = r76_EQULITY;
	}
	public String getR76_FOREX() {
		return R76_FOREX;
	}
	public void setR76_FOREX(String r76_FOREX) {
		R76_FOREX = r76_FOREX;
	}
	public String getR76_OTHERS() {
		return R76_OTHERS;
	}
	public void setR76_OTHERS(String r76_OTHERS) {
		R76_OTHERS = r76_OTHERS;
	}
	public String getR76_INT_BANK() {
		return R76_INT_BANK;
	}
	public void setR76_INT_BANK(String r76_INT_BANK) {
		R76_INT_BANK = r76_INT_BANK;
	}
	public String getR76_DERIVATIVE() {
		return R76_DERIVATIVE;
	}
	public void setR76_DERIVATIVE(String r76_DERIVATIVE) {
		R76_DERIVATIVE = r76_DERIVATIVE;
	}
	public String getR77_GROUP_CODE() {
		return R77_GROUP_CODE;
	}
	public void setR77_GROUP_CODE(String r77_GROUP_CODE) {
		R77_GROUP_CODE = r77_GROUP_CODE;
	}
	public String getR77_GROUP_NAME() {
		return R77_GROUP_NAME;
	}
	public void setR77_GROUP_NAME(String r77_GROUP_NAME) {
		R77_GROUP_NAME = r77_GROUP_NAME;
	}
	public BigDecimal getR77_CRM() {
		return R77_CRM;
	}
	public void setR77_CRM(BigDecimal r77_CRM) {
		R77_CRM = r77_CRM;
	}
	public BigDecimal getR77_NFBLT() {
		return R77_NFBLT;
	}
	public void setR77_NFBLT(BigDecimal r77_NFBLT) {
		R77_NFBLT = r77_NFBLT;
	}
	public BigDecimal getR77_NFBOS() {
		return R77_NFBOS;
	}
	public void setR77_NFBOS(BigDecimal r77_NFBOS) {
		R77_NFBOS = r77_NFBOS;
	}
	public BigDecimal getR77_CRM_2() {
		return R77_CRM_2;
	}
	public void setR77_CRM_2(BigDecimal r77_CRM_2) {
		R77_CRM_2 = r77_CRM_2;
	}
	public BigDecimal getR77_NFB() {
		return R77_NFB;
	}
	public void setR77_NFB(BigDecimal r77_NFB) {
		R77_NFB = r77_NFB;
	}
	public String getR77_BOND() {
		return R77_BOND;
	}
	public void setR77_BOND(String r77_BOND) {
		R77_BOND = r77_BOND;
	}
	public String getR77_CP() {
		return R77_CP;
	}
	public void setR77_CP(String r77_CP) {
		R77_CP = r77_CP;
	}
	public String getR77_EQULITY() {
		return R77_EQULITY;
	}
	public void setR77_EQULITY(String r77_EQULITY) {
		R77_EQULITY = r77_EQULITY;
	}
	public String getR77_FOREX() {
		return R77_FOREX;
	}
	public void setR77_FOREX(String r77_FOREX) {
		R77_FOREX = r77_FOREX;
	}
	public String getR77_OTHERS() {
		return R77_OTHERS;
	}
	public void setR77_OTHERS(String r77_OTHERS) {
		R77_OTHERS = r77_OTHERS;
	}
	public String getR77_INT_BANK() {
		return R77_INT_BANK;
	}
	public void setR77_INT_BANK(String r77_INT_BANK) {
		R77_INT_BANK = r77_INT_BANK;
	}
	public String getR77_DERIVATIVE() {
		return R77_DERIVATIVE;
	}
	public void setR77_DERIVATIVE(String r77_DERIVATIVE) {
		R77_DERIVATIVE = r77_DERIVATIVE;
	}
	public String getR78_GROUP_CODE() {
		return R78_GROUP_CODE;
	}
	public void setR78_GROUP_CODE(String r78_GROUP_CODE) {
		R78_GROUP_CODE = r78_GROUP_CODE;
	}
	public String getR78_GROUP_NAME() {
		return R78_GROUP_NAME;
	}
	public void setR78_GROUP_NAME(String r78_GROUP_NAME) {
		R78_GROUP_NAME = r78_GROUP_NAME;
	}
	public BigDecimal getR78_CRM() {
		return R78_CRM;
	}
	public void setR78_CRM(BigDecimal r78_CRM) {
		R78_CRM = r78_CRM;
	}
	public BigDecimal getR78_NFBLT() {
		return R78_NFBLT;
	}
	public void setR78_NFBLT(BigDecimal r78_NFBLT) {
		R78_NFBLT = r78_NFBLT;
	}
	public BigDecimal getR78_NFBOS() {
		return R78_NFBOS;
	}
	public void setR78_NFBOS(BigDecimal r78_NFBOS) {
		R78_NFBOS = r78_NFBOS;
	}
	public BigDecimal getR78_CRM_2() {
		return R78_CRM_2;
	}
	public void setR78_CRM_2(BigDecimal r78_CRM_2) {
		R78_CRM_2 = r78_CRM_2;
	}
	public BigDecimal getR78_NFB() {
		return R78_NFB;
	}
	public void setR78_NFB(BigDecimal r78_NFB) {
		R78_NFB = r78_NFB;
	}
	public String getR78_BOND() {
		return R78_BOND;
	}
	public void setR78_BOND(String r78_BOND) {
		R78_BOND = r78_BOND;
	}
	public String getR78_CP() {
		return R78_CP;
	}
	public void setR78_CP(String r78_CP) {
		R78_CP = r78_CP;
	}
	public String getR78_EQULITY() {
		return R78_EQULITY;
	}
	public void setR78_EQULITY(String r78_EQULITY) {
		R78_EQULITY = r78_EQULITY;
	}
	public String getR78_FOREX() {
		return R78_FOREX;
	}
	public void setR78_FOREX(String r78_FOREX) {
		R78_FOREX = r78_FOREX;
	}
	public String getR78_OTHERS() {
		return R78_OTHERS;
	}
	public void setR78_OTHERS(String r78_OTHERS) {
		R78_OTHERS = r78_OTHERS;
	}
	public String getR78_INT_BANK() {
		return R78_INT_BANK;
	}
	public void setR78_INT_BANK(String r78_INT_BANK) {
		R78_INT_BANK = r78_INT_BANK;
	}
	public String getR78_DERIVATIVE() {
		return R78_DERIVATIVE;
	}
	public void setR78_DERIVATIVE(String r78_DERIVATIVE) {
		R78_DERIVATIVE = r78_DERIVATIVE;
	}
	public String getR79_GROUP_CODE() {
		return R79_GROUP_CODE;
	}
	public void setR79_GROUP_CODE(String r79_GROUP_CODE) {
		R79_GROUP_CODE = r79_GROUP_CODE;
	}
	public String getR79_GROUP_NAME() {
		return R79_GROUP_NAME;
	}
	public void setR79_GROUP_NAME(String r79_GROUP_NAME) {
		R79_GROUP_NAME = r79_GROUP_NAME;
	}
	public BigDecimal getR79_CRM() {
		return R79_CRM;
	}
	public void setR79_CRM(BigDecimal r79_CRM) {
		R79_CRM = r79_CRM;
	}
	public BigDecimal getR79_NFBLT() {
		return R79_NFBLT;
	}
	public void setR79_NFBLT(BigDecimal r79_NFBLT) {
		R79_NFBLT = r79_NFBLT;
	}
	public BigDecimal getR79_NFBOS() {
		return R79_NFBOS;
	}
	public void setR79_NFBOS(BigDecimal r79_NFBOS) {
		R79_NFBOS = r79_NFBOS;
	}
	public BigDecimal getR79_CRM_2() {
		return R79_CRM_2;
	}
	public void setR79_CRM_2(BigDecimal r79_CRM_2) {
		R79_CRM_2 = r79_CRM_2;
	}
	public BigDecimal getR79_NFB() {
		return R79_NFB;
	}
	public void setR79_NFB(BigDecimal r79_NFB) {
		R79_NFB = r79_NFB;
	}
	public String getR79_BOND() {
		return R79_BOND;
	}
	public void setR79_BOND(String r79_BOND) {
		R79_BOND = r79_BOND;
	}
	public String getR79_CP() {
		return R79_CP;
	}
	public void setR79_CP(String r79_CP) {
		R79_CP = r79_CP;
	}
	public String getR79_EQULITY() {
		return R79_EQULITY;
	}
	public void setR79_EQULITY(String r79_EQULITY) {
		R79_EQULITY = r79_EQULITY;
	}
	public String getR79_FOREX() {
		return R79_FOREX;
	}
	public void setR79_FOREX(String r79_FOREX) {
		R79_FOREX = r79_FOREX;
	}
	public String getR79_OTHERS() {
		return R79_OTHERS;
	}
	public void setR79_OTHERS(String r79_OTHERS) {
		R79_OTHERS = r79_OTHERS;
	}
	public String getR79_INT_BANK() {
		return R79_INT_BANK;
	}
	public void setR79_INT_BANK(String r79_INT_BANK) {
		R79_INT_BANK = r79_INT_BANK;
	}
	public String getR79_DERIVATIVE() {
		return R79_DERIVATIVE;
	}
	public void setR79_DERIVATIVE(String r79_DERIVATIVE) {
		R79_DERIVATIVE = r79_DERIVATIVE;
	}
	public String getR80_GROUP_CODE() {
		return R80_GROUP_CODE;
	}
	public void setR80_GROUP_CODE(String r80_GROUP_CODE) {
		R80_GROUP_CODE = r80_GROUP_CODE;
	}
	public String getR80_GROUP_NAME() {
		return R80_GROUP_NAME;
	}
	public void setR80_GROUP_NAME(String r80_GROUP_NAME) {
		R80_GROUP_NAME = r80_GROUP_NAME;
	}
	public BigDecimal getR80_CRM() {
		return R80_CRM;
	}
	public void setR80_CRM(BigDecimal r80_CRM) {
		R80_CRM = r80_CRM;
	}
	public BigDecimal getR80_NFBLT() {
		return R80_NFBLT;
	}
	public void setR80_NFBLT(BigDecimal r80_NFBLT) {
		R80_NFBLT = r80_NFBLT;
	}
	public BigDecimal getR80_NFBOS() {
		return R80_NFBOS;
	}
	public void setR80_NFBOS(BigDecimal r80_NFBOS) {
		R80_NFBOS = r80_NFBOS;
	}
	public BigDecimal getR80_CRM_2() {
		return R80_CRM_2;
	}
	public void setR80_CRM_2(BigDecimal r80_CRM_2) {
		R80_CRM_2 = r80_CRM_2;
	}
	public BigDecimal getR80_NFB() {
		return R80_NFB;
	}
	public void setR80_NFB(BigDecimal r80_NFB) {
		R80_NFB = r80_NFB;
	}
	public String getR80_BOND() {
		return R80_BOND;
	}
	public void setR80_BOND(String r80_BOND) {
		R80_BOND = r80_BOND;
	}
	public String getR80_CP() {
		return R80_CP;
	}
	public void setR80_CP(String r80_CP) {
		R80_CP = r80_CP;
	}
	public String getR80_EQULITY() {
		return R80_EQULITY;
	}
	public void setR80_EQULITY(String r80_EQULITY) {
		R80_EQULITY = r80_EQULITY;
	}
	public String getR80_FOREX() {
		return R80_FOREX;
	}
	public void setR80_FOREX(String r80_FOREX) {
		R80_FOREX = r80_FOREX;
	}
	public String getR80_OTHERS() {
		return R80_OTHERS;
	}
	public void setR80_OTHERS(String r80_OTHERS) {
		R80_OTHERS = r80_OTHERS;
	}
	public String getR80_INT_BANK() {
		return R80_INT_BANK;
	}
	public void setR80_INT_BANK(String r80_INT_BANK) {
		R80_INT_BANK = r80_INT_BANK;
	}
	public String getR80_DERIVATIVE() {
		return R80_DERIVATIVE;
	}
	public void setR80_DERIVATIVE(String r80_DERIVATIVE) {
		R80_DERIVATIVE = r80_DERIVATIVE;
	}
	public String getR81_GROUP_CODE() {
		return R81_GROUP_CODE;
	}
	public void setR81_GROUP_CODE(String r81_GROUP_CODE) {
		R81_GROUP_CODE = r81_GROUP_CODE;
	}
	public String getR81_GROUP_NAME() {
		return R81_GROUP_NAME;
	}
	public void setR81_GROUP_NAME(String r81_GROUP_NAME) {
		R81_GROUP_NAME = r81_GROUP_NAME;
	}
	public BigDecimal getR81_CRM() {
		return R81_CRM;
	}
	public void setR81_CRM(BigDecimal r81_CRM) {
		R81_CRM = r81_CRM;
	}
	public BigDecimal getR81_NFBLT() {
		return R81_NFBLT;
	}
	public void setR81_NFBLT(BigDecimal r81_NFBLT) {
		R81_NFBLT = r81_NFBLT;
	}
	public BigDecimal getR81_NFBOS() {
		return R81_NFBOS;
	}
	public void setR81_NFBOS(BigDecimal r81_NFBOS) {
		R81_NFBOS = r81_NFBOS;
	}
	public BigDecimal getR81_CRM_2() {
		return R81_CRM_2;
	}
	public void setR81_CRM_2(BigDecimal r81_CRM_2) {
		R81_CRM_2 = r81_CRM_2;
	}
	public BigDecimal getR81_NFB() {
		return R81_NFB;
	}
	public void setR81_NFB(BigDecimal r81_NFB) {
		R81_NFB = r81_NFB;
	}
	public String getR81_BOND() {
		return R81_BOND;
	}
	public void setR81_BOND(String r81_BOND) {
		R81_BOND = r81_BOND;
	}
	public String getR81_CP() {
		return R81_CP;
	}
	public void setR81_CP(String r81_CP) {
		R81_CP = r81_CP;
	}
	public String getR81_EQULITY() {
		return R81_EQULITY;
	}
	public void setR81_EQULITY(String r81_EQULITY) {
		R81_EQULITY = r81_EQULITY;
	}
	public String getR81_FOREX() {
		return R81_FOREX;
	}
	public void setR81_FOREX(String r81_FOREX) {
		R81_FOREX = r81_FOREX;
	}
	public String getR81_OTHERS() {
		return R81_OTHERS;
	}
	public void setR81_OTHERS(String r81_OTHERS) {
		R81_OTHERS = r81_OTHERS;
	}
	public String getR81_INT_BANK() {
		return R81_INT_BANK;
	}
	public void setR81_INT_BANK(String r81_INT_BANK) {
		R81_INT_BANK = r81_INT_BANK;
	}
	public String getR81_DERIVATIVE() {
		return R81_DERIVATIVE;
	}
	public void setR81_DERIVATIVE(String r81_DERIVATIVE) {
		R81_DERIVATIVE = r81_DERIVATIVE;
	}
	public String getR82_GROUP_CODE() {
		return R82_GROUP_CODE;
	}
	public void setR82_GROUP_CODE(String r82_GROUP_CODE) {
		R82_GROUP_CODE = r82_GROUP_CODE;
	}
	public String getR82_GROUP_NAME() {
		return R82_GROUP_NAME;
	}
	public void setR82_GROUP_NAME(String r82_GROUP_NAME) {
		R82_GROUP_NAME = r82_GROUP_NAME;
	}
	public BigDecimal getR82_CRM() {
		return R82_CRM;
	}
	public void setR82_CRM(BigDecimal r82_CRM) {
		R82_CRM = r82_CRM;
	}
	public BigDecimal getR82_NFBLT() {
		return R82_NFBLT;
	}
	public void setR82_NFBLT(BigDecimal r82_NFBLT) {
		R82_NFBLT = r82_NFBLT;
	}
	public BigDecimal getR82_NFBOS() {
		return R82_NFBOS;
	}
	public void setR82_NFBOS(BigDecimal r82_NFBOS) {
		R82_NFBOS = r82_NFBOS;
	}
	public BigDecimal getR82_CRM_2() {
		return R82_CRM_2;
	}
	public void setR82_CRM_2(BigDecimal r82_CRM_2) {
		R82_CRM_2 = r82_CRM_2;
	}
	public BigDecimal getR82_NFB() {
		return R82_NFB;
	}
	public void setR82_NFB(BigDecimal r82_NFB) {
		R82_NFB = r82_NFB;
	}
	public String getR82_BOND() {
		return R82_BOND;
	}
	public void setR82_BOND(String r82_BOND) {
		R82_BOND = r82_BOND;
	}
	public String getR82_CP() {
		return R82_CP;
	}
	public void setR82_CP(String r82_CP) {
		R82_CP = r82_CP;
	}
	public String getR82_EQULITY() {
		return R82_EQULITY;
	}
	public void setR82_EQULITY(String r82_EQULITY) {
		R82_EQULITY = r82_EQULITY;
	}
	public String getR82_FOREX() {
		return R82_FOREX;
	}
	public void setR82_FOREX(String r82_FOREX) {
		R82_FOREX = r82_FOREX;
	}
	public String getR82_OTHERS() {
		return R82_OTHERS;
	}
	public void setR82_OTHERS(String r82_OTHERS) {
		R82_OTHERS = r82_OTHERS;
	}
	public String getR82_INT_BANK() {
		return R82_INT_BANK;
	}
	public void setR82_INT_BANK(String r82_INT_BANK) {
		R82_INT_BANK = r82_INT_BANK;
	}
	public String getR82_DERIVATIVE() {
		return R82_DERIVATIVE;
	}
	public void setR82_DERIVATIVE(String r82_DERIVATIVE) {
		R82_DERIVATIVE = r82_DERIVATIVE;
	}
	public String getR83_GROUP_CODE() {
		return R83_GROUP_CODE;
	}
	public void setR83_GROUP_CODE(String r83_GROUP_CODE) {
		R83_GROUP_CODE = r83_GROUP_CODE;
	}
	public String getR83_GROUP_NAME() {
		return R83_GROUP_NAME;
	}
	public void setR83_GROUP_NAME(String r83_GROUP_NAME) {
		R83_GROUP_NAME = r83_GROUP_NAME;
	}
	public BigDecimal getR83_CRM() {
		return R83_CRM;
	}
	public void setR83_CRM(BigDecimal r83_CRM) {
		R83_CRM = r83_CRM;
	}
	public BigDecimal getR83_NFBLT() {
		return R83_NFBLT;
	}
	public void setR83_NFBLT(BigDecimal r83_NFBLT) {
		R83_NFBLT = r83_NFBLT;
	}
	public BigDecimal getR83_NFBOS() {
		return R83_NFBOS;
	}
	public void setR83_NFBOS(BigDecimal r83_NFBOS) {
		R83_NFBOS = r83_NFBOS;
	}
	public BigDecimal getR83_CRM_2() {
		return R83_CRM_2;
	}
	public void setR83_CRM_2(BigDecimal r83_CRM_2) {
		R83_CRM_2 = r83_CRM_2;
	}
	public BigDecimal getR83_NFB() {
		return R83_NFB;
	}
	public void setR83_NFB(BigDecimal r83_NFB) {
		R83_NFB = r83_NFB;
	}
	public String getR83_BOND() {
		return R83_BOND;
	}
	public void setR83_BOND(String r83_BOND) {
		R83_BOND = r83_BOND;
	}
	public String getR83_CP() {
		return R83_CP;
	}
	public void setR83_CP(String r83_CP) {
		R83_CP = r83_CP;
	}
	public String getR83_EQULITY() {
		return R83_EQULITY;
	}
	public void setR83_EQULITY(String r83_EQULITY) {
		R83_EQULITY = r83_EQULITY;
	}
	public String getR83_FOREX() {
		return R83_FOREX;
	}
	public void setR83_FOREX(String r83_FOREX) {
		R83_FOREX = r83_FOREX;
	}
	public String getR83_OTHERS() {
		return R83_OTHERS;
	}
	public void setR83_OTHERS(String r83_OTHERS) {
		R83_OTHERS = r83_OTHERS;
	}
	public String getR83_INT_BANK() {
		return R83_INT_BANK;
	}
	public void setR83_INT_BANK(String r83_INT_BANK) {
		R83_INT_BANK = r83_INT_BANK;
	}
	public String getR83_DERIVATIVE() {
		return R83_DERIVATIVE;
	}
	public void setR83_DERIVATIVE(String r83_DERIVATIVE) {
		R83_DERIVATIVE = r83_DERIVATIVE;
	}
	public String getR84_GROUP_CODE() {
		return R84_GROUP_CODE;
	}
	public void setR84_GROUP_CODE(String r84_GROUP_CODE) {
		R84_GROUP_CODE = r84_GROUP_CODE;
	}
	public String getR84_GROUP_NAME() {
		return R84_GROUP_NAME;
	}
	public void setR84_GROUP_NAME(String r84_GROUP_NAME) {
		R84_GROUP_NAME = r84_GROUP_NAME;
	}
	public BigDecimal getR84_CRM() {
		return R84_CRM;
	}
	public void setR84_CRM(BigDecimal r84_CRM) {
		R84_CRM = r84_CRM;
	}
	public BigDecimal getR84_NFBLT() {
		return R84_NFBLT;
	}
	public void setR84_NFBLT(BigDecimal r84_NFBLT) {
		R84_NFBLT = r84_NFBLT;
	}
	public BigDecimal getR84_NFBOS() {
		return R84_NFBOS;
	}
	public void setR84_NFBOS(BigDecimal r84_NFBOS) {
		R84_NFBOS = r84_NFBOS;
	}
	public BigDecimal getR84_CRM_2() {
		return R84_CRM_2;
	}
	public void setR84_CRM_2(BigDecimal r84_CRM_2) {
		R84_CRM_2 = r84_CRM_2;
	}
	public BigDecimal getR84_NFB() {
		return R84_NFB;
	}
	public void setR84_NFB(BigDecimal r84_NFB) {
		R84_NFB = r84_NFB;
	}
	public String getR84_BOND() {
		return R84_BOND;
	}
	public void setR84_BOND(String r84_BOND) {
		R84_BOND = r84_BOND;
	}
	public String getR84_CP() {
		return R84_CP;
	}
	public void setR84_CP(String r84_CP) {
		R84_CP = r84_CP;
	}
	public String getR84_EQULITY() {
		return R84_EQULITY;
	}
	public void setR84_EQULITY(String r84_EQULITY) {
		R84_EQULITY = r84_EQULITY;
	}
	public String getR84_FOREX() {
		return R84_FOREX;
	}
	public void setR84_FOREX(String r84_FOREX) {
		R84_FOREX = r84_FOREX;
	}
	public String getR84_OTHERS() {
		return R84_OTHERS;
	}
	public void setR84_OTHERS(String r84_OTHERS) {
		R84_OTHERS = r84_OTHERS;
	}
	public String getR84_INT_BANK() {
		return R84_INT_BANK;
	}
	public void setR84_INT_BANK(String r84_INT_BANK) {
		R84_INT_BANK = r84_INT_BANK;
	}
	public String getR84_DERIVATIVE() {
		return R84_DERIVATIVE;
	}
	public void setR84_DERIVATIVE(String r84_DERIVATIVE) {
		R84_DERIVATIVE = r84_DERIVATIVE;
	}
	public String getR85_GROUP_CODE() {
		return R85_GROUP_CODE;
	}
	public void setR85_GROUP_CODE(String r85_GROUP_CODE) {
		R85_GROUP_CODE = r85_GROUP_CODE;
	}
	public String getR85_GROUP_NAME() {
		return R85_GROUP_NAME;
	}
	public void setR85_GROUP_NAME(String r85_GROUP_NAME) {
		R85_GROUP_NAME = r85_GROUP_NAME;
	}
	public BigDecimal getR85_CRM() {
		return R85_CRM;
	}
	public void setR85_CRM(BigDecimal r85_CRM) {
		R85_CRM = r85_CRM;
	}
	public BigDecimal getR85_NFBLT() {
		return R85_NFBLT;
	}
	public void setR85_NFBLT(BigDecimal r85_NFBLT) {
		R85_NFBLT = r85_NFBLT;
	}
	public BigDecimal getR85_NFBOS() {
		return R85_NFBOS;
	}
	public void setR85_NFBOS(BigDecimal r85_NFBOS) {
		R85_NFBOS = r85_NFBOS;
	}
	public BigDecimal getR85_CRM_2() {
		return R85_CRM_2;
	}
	public void setR85_CRM_2(BigDecimal r85_CRM_2) {
		R85_CRM_2 = r85_CRM_2;
	}
	public BigDecimal getR85_NFB() {
		return R85_NFB;
	}
	public void setR85_NFB(BigDecimal r85_NFB) {
		R85_NFB = r85_NFB;
	}
	public String getR85_BOND() {
		return R85_BOND;
	}
	public void setR85_BOND(String r85_BOND) {
		R85_BOND = r85_BOND;
	}
	public String getR85_CP() {
		return R85_CP;
	}
	public void setR85_CP(String r85_CP) {
		R85_CP = r85_CP;
	}
	public String getR85_EQULITY() {
		return R85_EQULITY;
	}
	public void setR85_EQULITY(String r85_EQULITY) {
		R85_EQULITY = r85_EQULITY;
	}
	public String getR85_FOREX() {
		return R85_FOREX;
	}
	public void setR85_FOREX(String r85_FOREX) {
		R85_FOREX = r85_FOREX;
	}
	public String getR85_OTHERS() {
		return R85_OTHERS;
	}
	public void setR85_OTHERS(String r85_OTHERS) {
		R85_OTHERS = r85_OTHERS;
	}
	public String getR85_INT_BANK() {
		return R85_INT_BANK;
	}
	public void setR85_INT_BANK(String r85_INT_BANK) {
		R85_INT_BANK = r85_INT_BANK;
	}
	public String getR85_DERIVATIVE() {
		return R85_DERIVATIVE;
	}
	public void setR85_DERIVATIVE(String r85_DERIVATIVE) {
		R85_DERIVATIVE = r85_DERIVATIVE;
	}
	public String getR86_GROUP_CODE() {
		return R86_GROUP_CODE;
	}
	public void setR86_GROUP_CODE(String r86_GROUP_CODE) {
		R86_GROUP_CODE = r86_GROUP_CODE;
	}
	public String getR86_GROUP_NAME() {
		return R86_GROUP_NAME;
	}
	public void setR86_GROUP_NAME(String r86_GROUP_NAME) {
		R86_GROUP_NAME = r86_GROUP_NAME;
	}
	public BigDecimal getR86_CRM() {
		return R86_CRM;
	}
	public void setR86_CRM(BigDecimal r86_CRM) {
		R86_CRM = r86_CRM;
	}
	public BigDecimal getR86_NFBLT() {
		return R86_NFBLT;
	}
	public void setR86_NFBLT(BigDecimal r86_NFBLT) {
		R86_NFBLT = r86_NFBLT;
	}
	public BigDecimal getR86_NFBOS() {
		return R86_NFBOS;
	}
	public void setR86_NFBOS(BigDecimal r86_NFBOS) {
		R86_NFBOS = r86_NFBOS;
	}
	public BigDecimal getR86_CRM_2() {
		return R86_CRM_2;
	}
	public void setR86_CRM_2(BigDecimal r86_CRM_2) {
		R86_CRM_2 = r86_CRM_2;
	}
	public BigDecimal getR86_NFB() {
		return R86_NFB;
	}
	public void setR86_NFB(BigDecimal r86_NFB) {
		R86_NFB = r86_NFB;
	}
	public String getR86_BOND() {
		return R86_BOND;
	}
	public void setR86_BOND(String r86_BOND) {
		R86_BOND = r86_BOND;
	}
	public String getR86_CP() {
		return R86_CP;
	}
	public void setR86_CP(String r86_CP) {
		R86_CP = r86_CP;
	}
	public String getR86_EQULITY() {
		return R86_EQULITY;
	}
	public void setR86_EQULITY(String r86_EQULITY) {
		R86_EQULITY = r86_EQULITY;
	}
	public String getR86_FOREX() {
		return R86_FOREX;
	}
	public void setR86_FOREX(String r86_FOREX) {
		R86_FOREX = r86_FOREX;
	}
	public String getR86_OTHERS() {
		return R86_OTHERS;
	}
	public void setR86_OTHERS(String r86_OTHERS) {
		R86_OTHERS = r86_OTHERS;
	}
	public String getR86_INT_BANK() {
		return R86_INT_BANK;
	}
	public void setR86_INT_BANK(String r86_INT_BANK) {
		R86_INT_BANK = r86_INT_BANK;
	}
	public String getR86_DERIVATIVE() {
		return R86_DERIVATIVE;
	}
	public void setR86_DERIVATIVE(String r86_DERIVATIVE) {
		R86_DERIVATIVE = r86_DERIVATIVE;
	}
	public String getR87_GROUP_CODE() {
		return R87_GROUP_CODE;
	}
	public void setR87_GROUP_CODE(String r87_GROUP_CODE) {
		R87_GROUP_CODE = r87_GROUP_CODE;
	}
	public String getR87_GROUP_NAME() {
		return R87_GROUP_NAME;
	}
	public void setR87_GROUP_NAME(String r87_GROUP_NAME) {
		R87_GROUP_NAME = r87_GROUP_NAME;
	}
	public BigDecimal getR87_CRM() {
		return R87_CRM;
	}
	public void setR87_CRM(BigDecimal r87_CRM) {
		R87_CRM = r87_CRM;
	}
	public BigDecimal getR87_NFBLT() {
		return R87_NFBLT;
	}
	public void setR87_NFBLT(BigDecimal r87_NFBLT) {
		R87_NFBLT = r87_NFBLT;
	}
	public BigDecimal getR87_NFBOS() {
		return R87_NFBOS;
	}
	public void setR87_NFBOS(BigDecimal r87_NFBOS) {
		R87_NFBOS = r87_NFBOS;
	}
	public BigDecimal getR87_CRM_2() {
		return R87_CRM_2;
	}
	public void setR87_CRM_2(BigDecimal r87_CRM_2) {
		R87_CRM_2 = r87_CRM_2;
	}
	public BigDecimal getR87_NFB() {
		return R87_NFB;
	}
	public void setR87_NFB(BigDecimal r87_NFB) {
		R87_NFB = r87_NFB;
	}
	public String getR87_BOND() {
		return R87_BOND;
	}
	public void setR87_BOND(String r87_BOND) {
		R87_BOND = r87_BOND;
	}
	public String getR87_CP() {
		return R87_CP;
	}
	public void setR87_CP(String r87_CP) {
		R87_CP = r87_CP;
	}
	public String getR87_EQULITY() {
		return R87_EQULITY;
	}
	public void setR87_EQULITY(String r87_EQULITY) {
		R87_EQULITY = r87_EQULITY;
	}
	public String getR87_FOREX() {
		return R87_FOREX;
	}
	public void setR87_FOREX(String r87_FOREX) {
		R87_FOREX = r87_FOREX;
	}
	public String getR87_OTHERS() {
		return R87_OTHERS;
	}
	public void setR87_OTHERS(String r87_OTHERS) {
		R87_OTHERS = r87_OTHERS;
	}
	public String getR87_INT_BANK() {
		return R87_INT_BANK;
	}
	public void setR87_INT_BANK(String r87_INT_BANK) {
		R87_INT_BANK = r87_INT_BANK;
	}
	public String getR87_DERIVATIVE() {
		return R87_DERIVATIVE;
	}
	public void setR87_DERIVATIVE(String r87_DERIVATIVE) {
		R87_DERIVATIVE = r87_DERIVATIVE;
	}
	public String getR88_GROUP_CODE() {
		return R88_GROUP_CODE;
	}
	public void setR88_GROUP_CODE(String r88_GROUP_CODE) {
		R88_GROUP_CODE = r88_GROUP_CODE;
	}
	public String getR88_GROUP_NAME() {
		return R88_GROUP_NAME;
	}
	public void setR88_GROUP_NAME(String r88_GROUP_NAME) {
		R88_GROUP_NAME = r88_GROUP_NAME;
	}
	public BigDecimal getR88_CRM() {
		return R88_CRM;
	}
	public void setR88_CRM(BigDecimal r88_CRM) {
		R88_CRM = r88_CRM;
	}
	public BigDecimal getR88_NFBLT() {
		return R88_NFBLT;
	}
	public void setR88_NFBLT(BigDecimal r88_NFBLT) {
		R88_NFBLT = r88_NFBLT;
	}
	public BigDecimal getR88_NFBOS() {
		return R88_NFBOS;
	}
	public void setR88_NFBOS(BigDecimal r88_NFBOS) {
		R88_NFBOS = r88_NFBOS;
	}
	public BigDecimal getR88_CRM_2() {
		return R88_CRM_2;
	}
	public void setR88_CRM_2(BigDecimal r88_CRM_2) {
		R88_CRM_2 = r88_CRM_2;
	}
	public BigDecimal getR88_NFB() {
		return R88_NFB;
	}
	public void setR88_NFB(BigDecimal r88_NFB) {
		R88_NFB = r88_NFB;
	}
	public String getR88_BOND() {
		return R88_BOND;
	}
	public void setR88_BOND(String r88_BOND) {
		R88_BOND = r88_BOND;
	}
	public String getR88_CP() {
		return R88_CP;
	}
	public void setR88_CP(String r88_CP) {
		R88_CP = r88_CP;
	}
	public String getR88_EQULITY() {
		return R88_EQULITY;
	}
	public void setR88_EQULITY(String r88_EQULITY) {
		R88_EQULITY = r88_EQULITY;
	}
	public String getR88_FOREX() {
		return R88_FOREX;
	}
	public void setR88_FOREX(String r88_FOREX) {
		R88_FOREX = r88_FOREX;
	}
	public String getR88_OTHERS() {
		return R88_OTHERS;
	}
	public void setR88_OTHERS(String r88_OTHERS) {
		R88_OTHERS = r88_OTHERS;
	}
	public String getR88_INT_BANK() {
		return R88_INT_BANK;
	}
	public void setR88_INT_BANK(String r88_INT_BANK) {
		R88_INT_BANK = r88_INT_BANK;
	}
	public String getR88_DERIVATIVE() {
		return R88_DERIVATIVE;
	}
	public void setR88_DERIVATIVE(String r88_DERIVATIVE) {
		R88_DERIVATIVE = r88_DERIVATIVE;
	}
	public String getR89_GROUP_CODE() {
		return R89_GROUP_CODE;
	}
	public void setR89_GROUP_CODE(String r89_GROUP_CODE) {
		R89_GROUP_CODE = r89_GROUP_CODE;
	}
	public String getR89_GROUP_NAME() {
		return R89_GROUP_NAME;
	}
	public void setR89_GROUP_NAME(String r89_GROUP_NAME) {
		R89_GROUP_NAME = r89_GROUP_NAME;
	}
	public BigDecimal getR89_CRM() {
		return R89_CRM;
	}
	public void setR89_CRM(BigDecimal r89_CRM) {
		R89_CRM = r89_CRM;
	}
	public BigDecimal getR89_NFBLT() {
		return R89_NFBLT;
	}
	public void setR89_NFBLT(BigDecimal r89_NFBLT) {
		R89_NFBLT = r89_NFBLT;
	}
	public BigDecimal getR89_NFBOS() {
		return R89_NFBOS;
	}
	public void setR89_NFBOS(BigDecimal r89_NFBOS) {
		R89_NFBOS = r89_NFBOS;
	}
	public BigDecimal getR89_CRM_2() {
		return R89_CRM_2;
	}
	public void setR89_CRM_2(BigDecimal r89_CRM_2) {
		R89_CRM_2 = r89_CRM_2;
	}
	public BigDecimal getR89_NFB() {
		return R89_NFB;
	}
	public void setR89_NFB(BigDecimal r89_NFB) {
		R89_NFB = r89_NFB;
	}
	public String getR89_BOND() {
		return R89_BOND;
	}
	public void setR89_BOND(String r89_BOND) {
		R89_BOND = r89_BOND;
	}
	public String getR89_CP() {
		return R89_CP;
	}
	public void setR89_CP(String r89_CP) {
		R89_CP = r89_CP;
	}
	public String getR89_EQULITY() {
		return R89_EQULITY;
	}
	public void setR89_EQULITY(String r89_EQULITY) {
		R89_EQULITY = r89_EQULITY;
	}
	public String getR89_FOREX() {
		return R89_FOREX;
	}
	public void setR89_FOREX(String r89_FOREX) {
		R89_FOREX = r89_FOREX;
	}
	public String getR89_OTHERS() {
		return R89_OTHERS;
	}
	public void setR89_OTHERS(String r89_OTHERS) {
		R89_OTHERS = r89_OTHERS;
	}
	public String getR89_INT_BANK() {
		return R89_INT_BANK;
	}
	public void setR89_INT_BANK(String r89_INT_BANK) {
		R89_INT_BANK = r89_INT_BANK;
	}
	public String getR89_DERIVATIVE() {
		return R89_DERIVATIVE;
	}
	public void setR89_DERIVATIVE(String r89_DERIVATIVE) {
		R89_DERIVATIVE = r89_DERIVATIVE;
	}
	public String getR90_GROUP_CODE() {
		return R90_GROUP_CODE;
	}
	public void setR90_GROUP_CODE(String r90_GROUP_CODE) {
		R90_GROUP_CODE = r90_GROUP_CODE;
	}
	public String getR90_GROUP_NAME() {
		return R90_GROUP_NAME;
	}
	public void setR90_GROUP_NAME(String r90_GROUP_NAME) {
		R90_GROUP_NAME = r90_GROUP_NAME;
	}
	public BigDecimal getR90_CRM() {
		return R90_CRM;
	}
	public void setR90_CRM(BigDecimal r90_CRM) {
		R90_CRM = r90_CRM;
	}
	public BigDecimal getR90_NFBLT() {
		return R90_NFBLT;
	}
	public void setR90_NFBLT(BigDecimal r90_NFBLT) {
		R90_NFBLT = r90_NFBLT;
	}
	public BigDecimal getR90_NFBOS() {
		return R90_NFBOS;
	}
	public void setR90_NFBOS(BigDecimal r90_NFBOS) {
		R90_NFBOS = r90_NFBOS;
	}
	public BigDecimal getR90_CRM_2() {
		return R90_CRM_2;
	}
	public void setR90_CRM_2(BigDecimal r90_CRM_2) {
		R90_CRM_2 = r90_CRM_2;
	}
	public BigDecimal getR90_NFB() {
		return R90_NFB;
	}
	public void setR90_NFB(BigDecimal r90_NFB) {
		R90_NFB = r90_NFB;
	}
	public String getR90_BOND() {
		return R90_BOND;
	}
	public void setR90_BOND(String r90_BOND) {
		R90_BOND = r90_BOND;
	}
	public String getR90_CP() {
		return R90_CP;
	}
	public void setR90_CP(String r90_CP) {
		R90_CP = r90_CP;
	}
	public String getR90_EQULITY() {
		return R90_EQULITY;
	}
	public void setR90_EQULITY(String r90_EQULITY) {
		R90_EQULITY = r90_EQULITY;
	}
	public String getR90_FOREX() {
		return R90_FOREX;
	}
	public void setR90_FOREX(String r90_FOREX) {
		R90_FOREX = r90_FOREX;
	}
	public String getR90_OTHERS() {
		return R90_OTHERS;
	}
	public void setR90_OTHERS(String r90_OTHERS) {
		R90_OTHERS = r90_OTHERS;
	}
	public String getR90_INT_BANK() {
		return R90_INT_BANK;
	}
	public void setR90_INT_BANK(String r90_INT_BANK) {
		R90_INT_BANK = r90_INT_BANK;
	}
	public String getR90_DERIVATIVE() {
		return R90_DERIVATIVE;
	}
	public void setR90_DERIVATIVE(String r90_DERIVATIVE) {
		R90_DERIVATIVE = r90_DERIVATIVE;
	}
	public String getR91_GROUP_CODE() {
		return R91_GROUP_CODE;
	}
	public void setR91_GROUP_CODE(String r91_GROUP_CODE) {
		R91_GROUP_CODE = r91_GROUP_CODE;
	}
	public String getR91_GROUP_NAME() {
		return R91_GROUP_NAME;
	}
	public void setR91_GROUP_NAME(String r91_GROUP_NAME) {
		R91_GROUP_NAME = r91_GROUP_NAME;
	}
	public BigDecimal getR91_CRM() {
		return R91_CRM;
	}
	public void setR91_CRM(BigDecimal r91_CRM) {
		R91_CRM = r91_CRM;
	}
	public BigDecimal getR91_NFBLT() {
		return R91_NFBLT;
	}
	public void setR91_NFBLT(BigDecimal r91_NFBLT) {
		R91_NFBLT = r91_NFBLT;
	}
	public BigDecimal getR91_NFBOS() {
		return R91_NFBOS;
	}
	public void setR91_NFBOS(BigDecimal r91_NFBOS) {
		R91_NFBOS = r91_NFBOS;
	}
	public BigDecimal getR91_CRM_2() {
		return R91_CRM_2;
	}
	public void setR91_CRM_2(BigDecimal r91_CRM_2) {
		R91_CRM_2 = r91_CRM_2;
	}
	public BigDecimal getR91_NFB() {
		return R91_NFB;
	}
	public void setR91_NFB(BigDecimal r91_NFB) {
		R91_NFB = r91_NFB;
	}
	public String getR91_BOND() {
		return R91_BOND;
	}
	public void setR91_BOND(String r91_BOND) {
		R91_BOND = r91_BOND;
	}
	public String getR91_CP() {
		return R91_CP;
	}
	public void setR91_CP(String r91_CP) {
		R91_CP = r91_CP;
	}
	public String getR91_EQULITY() {
		return R91_EQULITY;
	}
	public void setR91_EQULITY(String r91_EQULITY) {
		R91_EQULITY = r91_EQULITY;
	}
	public String getR91_FOREX() {
		return R91_FOREX;
	}
	public void setR91_FOREX(String r91_FOREX) {
		R91_FOREX = r91_FOREX;
	}
	public String getR91_OTHERS() {
		return R91_OTHERS;
	}
	public void setR91_OTHERS(String r91_OTHERS) {
		R91_OTHERS = r91_OTHERS;
	}
	public String getR91_INT_BANK() {
		return R91_INT_BANK;
	}
	public void setR91_INT_BANK(String r91_INT_BANK) {
		R91_INT_BANK = r91_INT_BANK;
	}
	public String getR91_DERIVATIVE() {
		return R91_DERIVATIVE;
	}
	public void setR91_DERIVATIVE(String r91_DERIVATIVE) {
		R91_DERIVATIVE = r91_DERIVATIVE;
	}
	public String getR92_GROUP_CODE() {
		return R92_GROUP_CODE;
	}
	public void setR92_GROUP_CODE(String r92_GROUP_CODE) {
		R92_GROUP_CODE = r92_GROUP_CODE;
	}
	public String getR92_GROUP_NAME() {
		return R92_GROUP_NAME;
	}
	public void setR92_GROUP_NAME(String r92_GROUP_NAME) {
		R92_GROUP_NAME = r92_GROUP_NAME;
	}
	public BigDecimal getR92_CRM() {
		return R92_CRM;
	}
	public void setR92_CRM(BigDecimal r92_CRM) {
		R92_CRM = r92_CRM;
	}
	public BigDecimal getR92_NFBLT() {
		return R92_NFBLT;
	}
	public void setR92_NFBLT(BigDecimal r92_NFBLT) {
		R92_NFBLT = r92_NFBLT;
	}
	public BigDecimal getR92_NFBOS() {
		return R92_NFBOS;
	}
	public void setR92_NFBOS(BigDecimal r92_NFBOS) {
		R92_NFBOS = r92_NFBOS;
	}
	public BigDecimal getR92_CRM_2() {
		return R92_CRM_2;
	}
	public void setR92_CRM_2(BigDecimal r92_CRM_2) {
		R92_CRM_2 = r92_CRM_2;
	}
	public BigDecimal getR92_NFB() {
		return R92_NFB;
	}
	public void setR92_NFB(BigDecimal r92_NFB) {
		R92_NFB = r92_NFB;
	}
	public String getR92_BOND() {
		return R92_BOND;
	}
	public void setR92_BOND(String r92_BOND) {
		R92_BOND = r92_BOND;
	}
	public String getR92_CP() {
		return R92_CP;
	}
	public void setR92_CP(String r92_CP) {
		R92_CP = r92_CP;
	}
	public String getR92_EQULITY() {
		return R92_EQULITY;
	}
	public void setR92_EQULITY(String r92_EQULITY) {
		R92_EQULITY = r92_EQULITY;
	}
	public String getR92_FOREX() {
		return R92_FOREX;
	}
	public void setR92_FOREX(String r92_FOREX) {
		R92_FOREX = r92_FOREX;
	}
	public String getR92_OTHERS() {
		return R92_OTHERS;
	}
	public void setR92_OTHERS(String r92_OTHERS) {
		R92_OTHERS = r92_OTHERS;
	}
	public String getR92_INT_BANK() {
		return R92_INT_BANK;
	}
	public void setR92_INT_BANK(String r92_INT_BANK) {
		R92_INT_BANK = r92_INT_BANK;
	}
	public String getR92_DERIVATIVE() {
		return R92_DERIVATIVE;
	}
	public void setR92_DERIVATIVE(String r92_DERIVATIVE) {
		R92_DERIVATIVE = r92_DERIVATIVE;
	}
	public String getR93_GROUP_CODE() {
		return R93_GROUP_CODE;
	}
	public void setR93_GROUP_CODE(String r93_GROUP_CODE) {
		R93_GROUP_CODE = r93_GROUP_CODE;
	}
	public String getR93_GROUP_NAME() {
		return R93_GROUP_NAME;
	}
	public void setR93_GROUP_NAME(String r93_GROUP_NAME) {
		R93_GROUP_NAME = r93_GROUP_NAME;
	}
	public BigDecimal getR93_CRM() {
		return R93_CRM;
	}
	public void setR93_CRM(BigDecimal r93_CRM) {
		R93_CRM = r93_CRM;
	}
	public BigDecimal getR93_NFBLT() {
		return R93_NFBLT;
	}
	public void setR93_NFBLT(BigDecimal r93_NFBLT) {
		R93_NFBLT = r93_NFBLT;
	}
	public BigDecimal getR93_NFBOS() {
		return R93_NFBOS;
	}
	public void setR93_NFBOS(BigDecimal r93_NFBOS) {
		R93_NFBOS = r93_NFBOS;
	}
	public BigDecimal getR93_CRM_2() {
		return R93_CRM_2;
	}
	public void setR93_CRM_2(BigDecimal r93_CRM_2) {
		R93_CRM_2 = r93_CRM_2;
	}
	public BigDecimal getR93_NFB() {
		return R93_NFB;
	}
	public void setR93_NFB(BigDecimal r93_NFB) {
		R93_NFB = r93_NFB;
	}
	public String getR93_BOND() {
		return R93_BOND;
	}
	public void setR93_BOND(String r93_BOND) {
		R93_BOND = r93_BOND;
	}
	public String getR93_CP() {
		return R93_CP;
	}
	public void setR93_CP(String r93_CP) {
		R93_CP = r93_CP;
	}
	public String getR93_EQULITY() {
		return R93_EQULITY;
	}
	public void setR93_EQULITY(String r93_EQULITY) {
		R93_EQULITY = r93_EQULITY;
	}
	public String getR93_FOREX() {
		return R93_FOREX;
	}
	public void setR93_FOREX(String r93_FOREX) {
		R93_FOREX = r93_FOREX;
	}
	public String getR93_OTHERS() {
		return R93_OTHERS;
	}
	public void setR93_OTHERS(String r93_OTHERS) {
		R93_OTHERS = r93_OTHERS;
	}
	public String getR93_INT_BANK() {
		return R93_INT_BANK;
	}
	public void setR93_INT_BANK(String r93_INT_BANK) {
		R93_INT_BANK = r93_INT_BANK;
	}
	public String getR93_DERIVATIVE() {
		return R93_DERIVATIVE;
	}
	public void setR93_DERIVATIVE(String r93_DERIVATIVE) {
		R93_DERIVATIVE = r93_DERIVATIVE;
	}
	public String getR94_GROUP_CODE() {
		return R94_GROUP_CODE;
	}
	public void setR94_GROUP_CODE(String r94_GROUP_CODE) {
		R94_GROUP_CODE = r94_GROUP_CODE;
	}
	public String getR94_GROUP_NAME() {
		return R94_GROUP_NAME;
	}
	public void setR94_GROUP_NAME(String r94_GROUP_NAME) {
		R94_GROUP_NAME = r94_GROUP_NAME;
	}
	public BigDecimal getR94_CRM() {
		return R94_CRM;
	}
	public void setR94_CRM(BigDecimal r94_CRM) {
		R94_CRM = r94_CRM;
	}
	public BigDecimal getR94_NFBLT() {
		return R94_NFBLT;
	}
	public void setR94_NFBLT(BigDecimal r94_NFBLT) {
		R94_NFBLT = r94_NFBLT;
	}
	public BigDecimal getR94_NFBOS() {
		return R94_NFBOS;
	}
	public void setR94_NFBOS(BigDecimal r94_NFBOS) {
		R94_NFBOS = r94_NFBOS;
	}
	public BigDecimal getR94_CRM_2() {
		return R94_CRM_2;
	}
	public void setR94_CRM_2(BigDecimal r94_CRM_2) {
		R94_CRM_2 = r94_CRM_2;
	}
	public BigDecimal getR94_NFB() {
		return R94_NFB;
	}
	public void setR94_NFB(BigDecimal r94_NFB) {
		R94_NFB = r94_NFB;
	}
	public String getR94_BOND() {
		return R94_BOND;
	}
	public void setR94_BOND(String r94_BOND) {
		R94_BOND = r94_BOND;
	}
	public String getR94_CP() {
		return R94_CP;
	}
	public void setR94_CP(String r94_CP) {
		R94_CP = r94_CP;
	}
	public String getR94_EQULITY() {
		return R94_EQULITY;
	}
	public void setR94_EQULITY(String r94_EQULITY) {
		R94_EQULITY = r94_EQULITY;
	}
	public String getR94_FOREX() {
		return R94_FOREX;
	}
	public void setR94_FOREX(String r94_FOREX) {
		R94_FOREX = r94_FOREX;
	}
	public String getR94_OTHERS() {
		return R94_OTHERS;
	}
	public void setR94_OTHERS(String r94_OTHERS) {
		R94_OTHERS = r94_OTHERS;
	}
	public String getR94_INT_BANK() {
		return R94_INT_BANK;
	}
	public void setR94_INT_BANK(String r94_INT_BANK) {
		R94_INT_BANK = r94_INT_BANK;
	}
	public String getR94_DERIVATIVE() {
		return R94_DERIVATIVE;
	}
	public void setR94_DERIVATIVE(String r94_DERIVATIVE) {
		R94_DERIVATIVE = r94_DERIVATIVE;
	}
	public String getR95_GROUP_CODE() {
		return R95_GROUP_CODE;
	}
	public void setR95_GROUP_CODE(String r95_GROUP_CODE) {
		R95_GROUP_CODE = r95_GROUP_CODE;
	}
	public String getR95_GROUP_NAME() {
		return R95_GROUP_NAME;
	}
	public void setR95_GROUP_NAME(String r95_GROUP_NAME) {
		R95_GROUP_NAME = r95_GROUP_NAME;
	}
	public BigDecimal getR95_CRM() {
		return R95_CRM;
	}
	public void setR95_CRM(BigDecimal r95_CRM) {
		R95_CRM = r95_CRM;
	}
	public BigDecimal getR95_NFBLT() {
		return R95_NFBLT;
	}
	public void setR95_NFBLT(BigDecimal r95_NFBLT) {
		R95_NFBLT = r95_NFBLT;
	}
	public BigDecimal getR95_NFBOS() {
		return R95_NFBOS;
	}
	public void setR95_NFBOS(BigDecimal r95_NFBOS) {
		R95_NFBOS = r95_NFBOS;
	}
	public BigDecimal getR95_CRM_2() {
		return R95_CRM_2;
	}
	public void setR95_CRM_2(BigDecimal r95_CRM_2) {
		R95_CRM_2 = r95_CRM_2;
	}
	public BigDecimal getR95_NFB() {
		return R95_NFB;
	}
	public void setR95_NFB(BigDecimal r95_NFB) {
		R95_NFB = r95_NFB;
	}
	public String getR95_BOND() {
		return R95_BOND;
	}
	public void setR95_BOND(String r95_BOND) {
		R95_BOND = r95_BOND;
	}
	public String getR95_CP() {
		return R95_CP;
	}
	public void setR95_CP(String r95_CP) {
		R95_CP = r95_CP;
	}
	public String getR95_EQULITY() {
		return R95_EQULITY;
	}
	public void setR95_EQULITY(String r95_EQULITY) {
		R95_EQULITY = r95_EQULITY;
	}
	public String getR95_FOREX() {
		return R95_FOREX;
	}
	public void setR95_FOREX(String r95_FOREX) {
		R95_FOREX = r95_FOREX;
	}
	public String getR95_OTHERS() {
		return R95_OTHERS;
	}
	public void setR95_OTHERS(String r95_OTHERS) {
		R95_OTHERS = r95_OTHERS;
	}
	public String getR95_INT_BANK() {
		return R95_INT_BANK;
	}
	public void setR95_INT_BANK(String r95_INT_BANK) {
		R95_INT_BANK = r95_INT_BANK;
	}
	public String getR95_DERIVATIVE() {
		return R95_DERIVATIVE;
	}
	public void setR95_DERIVATIVE(String r95_DERIVATIVE) {
		R95_DERIVATIVE = r95_DERIVATIVE;
	}
	public String getR96_GROUP_CODE() {
		return R96_GROUP_CODE;
	}
	public void setR96_GROUP_CODE(String r96_GROUP_CODE) {
		R96_GROUP_CODE = r96_GROUP_CODE;
	}
	public String getR96_GROUP_NAME() {
		return R96_GROUP_NAME;
	}
	public void setR96_GROUP_NAME(String r96_GROUP_NAME) {
		R96_GROUP_NAME = r96_GROUP_NAME;
	}
	public BigDecimal getR96_CRM() {
		return R96_CRM;
	}
	public void setR96_CRM(BigDecimal r96_CRM) {
		R96_CRM = r96_CRM;
	}
	public BigDecimal getR96_NFBLT() {
		return R96_NFBLT;
	}
	public void setR96_NFBLT(BigDecimal r96_NFBLT) {
		R96_NFBLT = r96_NFBLT;
	}
	public BigDecimal getR96_NFBOS() {
		return R96_NFBOS;
	}
	public void setR96_NFBOS(BigDecimal r96_NFBOS) {
		R96_NFBOS = r96_NFBOS;
	}
	public BigDecimal getR96_CRM_2() {
		return R96_CRM_2;
	}
	public void setR96_CRM_2(BigDecimal r96_CRM_2) {
		R96_CRM_2 = r96_CRM_2;
	}
	public BigDecimal getR96_NFB() {
		return R96_NFB;
	}
	public void setR96_NFB(BigDecimal r96_NFB) {
		R96_NFB = r96_NFB;
	}
	public String getR96_BOND() {
		return R96_BOND;
	}
	public void setR96_BOND(String r96_BOND) {
		R96_BOND = r96_BOND;
	}
	public String getR96_CP() {
		return R96_CP;
	}
	public void setR96_CP(String r96_CP) {
		R96_CP = r96_CP;
	}
	public String getR96_EQULITY() {
		return R96_EQULITY;
	}
	public void setR96_EQULITY(String r96_EQULITY) {
		R96_EQULITY = r96_EQULITY;
	}
	public String getR96_FOREX() {
		return R96_FOREX;
	}
	public void setR96_FOREX(String r96_FOREX) {
		R96_FOREX = r96_FOREX;
	}
	public String getR96_OTHERS() {
		return R96_OTHERS;
	}
	public void setR96_OTHERS(String r96_OTHERS) {
		R96_OTHERS = r96_OTHERS;
	}
	public String getR96_INT_BANK() {
		return R96_INT_BANK;
	}
	public void setR96_INT_BANK(String r96_INT_BANK) {
		R96_INT_BANK = r96_INT_BANK;
	}
	public String getR96_DERIVATIVE() {
		return R96_DERIVATIVE;
	}
	public void setR96_DERIVATIVE(String r96_DERIVATIVE) {
		R96_DERIVATIVE = r96_DERIVATIVE;
	}
	public String getR97_GROUP_CODE() {
		return R97_GROUP_CODE;
	}
	public void setR97_GROUP_CODE(String r97_GROUP_CODE) {
		R97_GROUP_CODE = r97_GROUP_CODE;
	}
	public String getR97_GROUP_NAME() {
		return R97_GROUP_NAME;
	}
	public void setR97_GROUP_NAME(String r97_GROUP_NAME) {
		R97_GROUP_NAME = r97_GROUP_NAME;
	}
	public BigDecimal getR97_CRM() {
		return R97_CRM;
	}
	public void setR97_CRM(BigDecimal r97_CRM) {
		R97_CRM = r97_CRM;
	}
	public BigDecimal getR97_NFBLT() {
		return R97_NFBLT;
	}
	public void setR97_NFBLT(BigDecimal r97_NFBLT) {
		R97_NFBLT = r97_NFBLT;
	}
	public BigDecimal getR97_NFBOS() {
		return R97_NFBOS;
	}
	public void setR97_NFBOS(BigDecimal r97_NFBOS) {
		R97_NFBOS = r97_NFBOS;
	}
	public BigDecimal getR97_CRM_2() {
		return R97_CRM_2;
	}
	public void setR97_CRM_2(BigDecimal r97_CRM_2) {
		R97_CRM_2 = r97_CRM_2;
	}
	public BigDecimal getR97_NFB() {
		return R97_NFB;
	}
	public void setR97_NFB(BigDecimal r97_NFB) {
		R97_NFB = r97_NFB;
	}
	public String getR97_BOND() {
		return R97_BOND;
	}
	public void setR97_BOND(String r97_BOND) {
		R97_BOND = r97_BOND;
	}
	public String getR97_CP() {
		return R97_CP;
	}
	public void setR97_CP(String r97_CP) {
		R97_CP = r97_CP;
	}
	public String getR97_EQULITY() {
		return R97_EQULITY;
	}
	public void setR97_EQULITY(String r97_EQULITY) {
		R97_EQULITY = r97_EQULITY;
	}
	public String getR97_FOREX() {
		return R97_FOREX;
	}
	public void setR97_FOREX(String r97_FOREX) {
		R97_FOREX = r97_FOREX;
	}
	public String getR97_OTHERS() {
		return R97_OTHERS;
	}
	public void setR97_OTHERS(String r97_OTHERS) {
		R97_OTHERS = r97_OTHERS;
	}
	public String getR97_INT_BANK() {
		return R97_INT_BANK;
	}
	public void setR97_INT_BANK(String r97_INT_BANK) {
		R97_INT_BANK = r97_INT_BANK;
	}
	public String getR97_DERIVATIVE() {
		return R97_DERIVATIVE;
	}
	public void setR97_DERIVATIVE(String r97_DERIVATIVE) {
		R97_DERIVATIVE = r97_DERIVATIVE;
	}
	public String getR98_GROUP_CODE() {
		return R98_GROUP_CODE;
	}
	public void setR98_GROUP_CODE(String r98_GROUP_CODE) {
		R98_GROUP_CODE = r98_GROUP_CODE;
	}
	public String getR98_GROUP_NAME() {
		return R98_GROUP_NAME;
	}
	public void setR98_GROUP_NAME(String r98_GROUP_NAME) {
		R98_GROUP_NAME = r98_GROUP_NAME;
	}
	public BigDecimal getR98_CRM() {
		return R98_CRM;
	}
	public void setR98_CRM(BigDecimal r98_CRM) {
		R98_CRM = r98_CRM;
	}
	public BigDecimal getR98_NFBLT() {
		return R98_NFBLT;
	}
	public void setR98_NFBLT(BigDecimal r98_NFBLT) {
		R98_NFBLT = r98_NFBLT;
	}
	public BigDecimal getR98_NFBOS() {
		return R98_NFBOS;
	}
	public void setR98_NFBOS(BigDecimal r98_NFBOS) {
		R98_NFBOS = r98_NFBOS;
	}
	public BigDecimal getR98_CRM_2() {
		return R98_CRM_2;
	}
	public void setR98_CRM_2(BigDecimal r98_CRM_2) {
		R98_CRM_2 = r98_CRM_2;
	}
	public BigDecimal getR98_NFB() {
		return R98_NFB;
	}
	public void setR98_NFB(BigDecimal r98_NFB) {
		R98_NFB = r98_NFB;
	}
	public String getR98_BOND() {
		return R98_BOND;
	}
	public void setR98_BOND(String r98_BOND) {
		R98_BOND = r98_BOND;
	}
	public String getR98_CP() {
		return R98_CP;
	}
	public void setR98_CP(String r98_CP) {
		R98_CP = r98_CP;
	}
	public String getR98_EQULITY() {
		return R98_EQULITY;
	}
	public void setR98_EQULITY(String r98_EQULITY) {
		R98_EQULITY = r98_EQULITY;
	}
	public String getR98_FOREX() {
		return R98_FOREX;
	}
	public void setR98_FOREX(String r98_FOREX) {
		R98_FOREX = r98_FOREX;
	}
	public String getR98_OTHERS() {
		return R98_OTHERS;
	}
	public void setR98_OTHERS(String r98_OTHERS) {
		R98_OTHERS = r98_OTHERS;
	}
	public String getR98_INT_BANK() {
		return R98_INT_BANK;
	}
	public void setR98_INT_BANK(String r98_INT_BANK) {
		R98_INT_BANK = r98_INT_BANK;
	}
	public String getR98_DERIVATIVE() {
		return R98_DERIVATIVE;
	}
	public void setR98_DERIVATIVE(String r98_DERIVATIVE) {
		R98_DERIVATIVE = r98_DERIVATIVE;
	}
	public String getR99_GROUP_CODE() {
		return R99_GROUP_CODE;
	}
	public void setR99_GROUP_CODE(String r99_GROUP_CODE) {
		R99_GROUP_CODE = r99_GROUP_CODE;
	}
	public String getR99_GROUP_NAME() {
		return R99_GROUP_NAME;
	}
	public void setR99_GROUP_NAME(String r99_GROUP_NAME) {
		R99_GROUP_NAME = r99_GROUP_NAME;
	}
	public BigDecimal getR99_CRM() {
		return R99_CRM;
	}
	public void setR99_CRM(BigDecimal r99_CRM) {
		R99_CRM = r99_CRM;
	}
	public BigDecimal getR99_NFBLT() {
		return R99_NFBLT;
	}
	public void setR99_NFBLT(BigDecimal r99_NFBLT) {
		R99_NFBLT = r99_NFBLT;
	}
	public BigDecimal getR99_NFBOS() {
		return R99_NFBOS;
	}
	public void setR99_NFBOS(BigDecimal r99_NFBOS) {
		R99_NFBOS = r99_NFBOS;
	}
	public BigDecimal getR99_CRM_2() {
		return R99_CRM_2;
	}
	public void setR99_CRM_2(BigDecimal r99_CRM_2) {
		R99_CRM_2 = r99_CRM_2;
	}
	public BigDecimal getR99_NFB() {
		return R99_NFB;
	}
	public void setR99_NFB(BigDecimal r99_NFB) {
		R99_NFB = r99_NFB;
	}
	public String getR99_BOND() {
		return R99_BOND;
	}
	public void setR99_BOND(String r99_BOND) {
		R99_BOND = r99_BOND;
	}
	public String getR99_CP() {
		return R99_CP;
	}
	public void setR99_CP(String r99_CP) {
		R99_CP = r99_CP;
	}
	public String getR99_EQULITY() {
		return R99_EQULITY;
	}
	public void setR99_EQULITY(String r99_EQULITY) {
		R99_EQULITY = r99_EQULITY;
	}
	public String getR99_FOREX() {
		return R99_FOREX;
	}
	public void setR99_FOREX(String r99_FOREX) {
		R99_FOREX = r99_FOREX;
	}
	public String getR99_OTHERS() {
		return R99_OTHERS;
	}
	public void setR99_OTHERS(String r99_OTHERS) {
		R99_OTHERS = r99_OTHERS;
	}
	public String getR99_INT_BANK() {
		return R99_INT_BANK;
	}
	public void setR99_INT_BANK(String r99_INT_BANK) {
		R99_INT_BANK = r99_INT_BANK;
	}
	public String getR99_DERIVATIVE() {
		return R99_DERIVATIVE;
	}
	public void setR99_DERIVATIVE(String r99_DERIVATIVE) {
		R99_DERIVATIVE = r99_DERIVATIVE;
	}
	public String getR100_GROUP_CODE() {
		return R100_GROUP_CODE;
	}
	public void setR100_GROUP_CODE(String r100_GROUP_CODE) {
		R100_GROUP_CODE = r100_GROUP_CODE;
	}
	public String getR100_GROUP_NAME() {
		return R100_GROUP_NAME;
	}
	public void setR100_GROUP_NAME(String r100_GROUP_NAME) {
		R100_GROUP_NAME = r100_GROUP_NAME;
	}
	public BigDecimal getR100_CRM() {
		return R100_CRM;
	}
	public void setR100_CRM(BigDecimal r100_CRM) {
		R100_CRM = r100_CRM;
	}
	public BigDecimal getR100_NFBLT() {
		return R100_NFBLT;
	}
	public void setR100_NFBLT(BigDecimal r100_NFBLT) {
		R100_NFBLT = r100_NFBLT;
	}
	public BigDecimal getR100_NFBOS() {
		return R100_NFBOS;
	}
	public void setR100_NFBOS(BigDecimal r100_NFBOS) {
		R100_NFBOS = r100_NFBOS;
	}
	public BigDecimal getR100_CRM_2() {
		return R100_CRM_2;
	}
	public void setR100_CRM_2(BigDecimal r100_CRM_2) {
		R100_CRM_2 = r100_CRM_2;
	}
	public BigDecimal getR100_NFB() {
		return R100_NFB;
	}
	public void setR100_NFB(BigDecimal r100_NFB) {
		R100_NFB = r100_NFB;
	}
	public String getR100_BOND() {
		return R100_BOND;
	}
	public void setR100_BOND(String r100_BOND) {
		R100_BOND = r100_BOND;
	}
	public String getR100_CP() {
		return R100_CP;
	}
	public void setR100_CP(String r100_CP) {
		R100_CP = r100_CP;
	}
	public String getR100_EQULITY() {
		return R100_EQULITY;
	}
	public void setR100_EQULITY(String r100_EQULITY) {
		R100_EQULITY = r100_EQULITY;
	}
	public String getR100_FOREX() {
		return R100_FOREX;
	}
	public void setR100_FOREX(String r100_FOREX) {
		R100_FOREX = r100_FOREX;
	}
	public String getR100_OTHERS() {
		return R100_OTHERS;
	}
	public void setR100_OTHERS(String r100_OTHERS) {
		R100_OTHERS = r100_OTHERS;
	}
	public String getR100_INT_BANK() {
		return R100_INT_BANK;
	}
	public void setR100_INT_BANK(String r100_INT_BANK) {
		R100_INT_BANK = r100_INT_BANK;
	}
	public String getR100_DERIVATIVE() {
		return R100_DERIVATIVE;
	}
	public void setR100_DERIVATIVE(String r100_DERIVATIVE) {
		R100_DERIVATIVE = r100_DERIVATIVE;
	}
	public String getR101_GROUP_CODE() {
		return R101_GROUP_CODE;
	}
	public void setR101_GROUP_CODE(String r101_GROUP_CODE) {
		R101_GROUP_CODE = r101_GROUP_CODE;
	}
	public String getR101_GROUP_NAME() {
		return R101_GROUP_NAME;
	}
	public void setR101_GROUP_NAME(String r101_GROUP_NAME) {
		R101_GROUP_NAME = r101_GROUP_NAME;
	}
	public BigDecimal getR101_CRM() {
		return R101_CRM;
	}
	public void setR101_CRM(BigDecimal r101_CRM) {
		R101_CRM = r101_CRM;
	}
	public BigDecimal getR101_NFBLT() {
		return R101_NFBLT;
	}
	public void setR101_NFBLT(BigDecimal r101_NFBLT) {
		R101_NFBLT = r101_NFBLT;
	}
	public BigDecimal getR101_NFBOS() {
		return R101_NFBOS;
	}
	public void setR101_NFBOS(BigDecimal r101_NFBOS) {
		R101_NFBOS = r101_NFBOS;
	}
	public BigDecimal getR101_CRM_2() {
		return R101_CRM_2;
	}
	public void setR101_CRM_2(BigDecimal r101_CRM_2) {
		R101_CRM_2 = r101_CRM_2;
	}
	public BigDecimal getR101_NFB() {
		return R101_NFB;
	}
	public void setR101_NFB(BigDecimal r101_NFB) {
		R101_NFB = r101_NFB;
	}
	public String getR101_BOND() {
		return R101_BOND;
	}
	public void setR101_BOND(String r101_BOND) {
		R101_BOND = r101_BOND;
	}
	public String getR101_CP() {
		return R101_CP;
	}
	public void setR101_CP(String r101_CP) {
		R101_CP = r101_CP;
	}
	public String getR101_EQULITY() {
		return R101_EQULITY;
	}
	public void setR101_EQULITY(String r101_EQULITY) {
		R101_EQULITY = r101_EQULITY;
	}
	public String getR101_FOREX() {
		return R101_FOREX;
	}
	public void setR101_FOREX(String r101_FOREX) {
		R101_FOREX = r101_FOREX;
	}
	public String getR101_OTHERS() {
		return R101_OTHERS;
	}
	public void setR101_OTHERS(String r101_OTHERS) {
		R101_OTHERS = r101_OTHERS;
	}
	public String getR101_INT_BANK() {
		return R101_INT_BANK;
	}
	public void setR101_INT_BANK(String r101_INT_BANK) {
		R101_INT_BANK = r101_INT_BANK;
	}
	public String getR101_DERIVATIVE() {
		return R101_DERIVATIVE;
	}
	public void setR101_DERIVATIVE(String r101_DERIVATIVE) {
		R101_DERIVATIVE = r101_DERIVATIVE;
	}
	public String getR102_GROUP_CODE() {
		return R102_GROUP_CODE;
	}
	public void setR102_GROUP_CODE(String r102_GROUP_CODE) {
		R102_GROUP_CODE = r102_GROUP_CODE;
	}
	public String getR102_GROUP_NAME() {
		return R102_GROUP_NAME;
	}
	public void setR102_GROUP_NAME(String r102_GROUP_NAME) {
		R102_GROUP_NAME = r102_GROUP_NAME;
	}
	public BigDecimal getR102_CRM() {
		return R102_CRM;
	}
	public void setR102_CRM(BigDecimal r102_CRM) {
		R102_CRM = r102_CRM;
	}
	public BigDecimal getR102_NFBLT() {
		return R102_NFBLT;
	}
	public void setR102_NFBLT(BigDecimal r102_NFBLT) {
		R102_NFBLT = r102_NFBLT;
	}
	public BigDecimal getR102_NFBOS() {
		return R102_NFBOS;
	}
	public void setR102_NFBOS(BigDecimal r102_NFBOS) {
		R102_NFBOS = r102_NFBOS;
	}
	public BigDecimal getR102_CRM_2() {
		return R102_CRM_2;
	}
	public void setR102_CRM_2(BigDecimal r102_CRM_2) {
		R102_CRM_2 = r102_CRM_2;
	}
	public BigDecimal getR102_NFB() {
		return R102_NFB;
	}
	public void setR102_NFB(BigDecimal r102_NFB) {
		R102_NFB = r102_NFB;
	}
	public String getR102_BOND() {
		return R102_BOND;
	}
	public void setR102_BOND(String r102_BOND) {
		R102_BOND = r102_BOND;
	}
	public String getR102_CP() {
		return R102_CP;
	}
	public void setR102_CP(String r102_CP) {
		R102_CP = r102_CP;
	}
	public String getR102_EQULITY() {
		return R102_EQULITY;
	}
	public void setR102_EQULITY(String r102_EQULITY) {
		R102_EQULITY = r102_EQULITY;
	}
	public String getR102_FOREX() {
		return R102_FOREX;
	}
	public void setR102_FOREX(String r102_FOREX) {
		R102_FOREX = r102_FOREX;
	}
	public String getR102_OTHERS() {
		return R102_OTHERS;
	}
	public void setR102_OTHERS(String r102_OTHERS) {
		R102_OTHERS = r102_OTHERS;
	}
	public String getR102_INT_BANK() {
		return R102_INT_BANK;
	}
	public void setR102_INT_BANK(String r102_INT_BANK) {
		R102_INT_BANK = r102_INT_BANK;
	}
	public String getR102_DERIVATIVE() {
		return R102_DERIVATIVE;
	}
	public void setR102_DERIVATIVE(String r102_DERIVATIVE) {
		R102_DERIVATIVE = r102_DERIVATIVE;
	}
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
	public M_TOP_100_BORROWER_Manual_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    
    
    
}
