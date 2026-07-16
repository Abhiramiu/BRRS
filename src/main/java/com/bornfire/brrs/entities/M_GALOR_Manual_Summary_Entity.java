package com.bornfire.brrs.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Entity
@Table(name = "BRRS_M_GALOR_MANUAL_SUMMARYTABLE")
public class M_GALOR_Manual_Summary_Entity {

	
	private String		r22_product;
	private BigDecimal	r22_botswana;
	private BigDecimal	r22_south_africa;
	private BigDecimal	r22_sadc;
	private BigDecimal	r22_usa;
	private BigDecimal	r22_uk;
	private BigDecimal	r22_europe;
	private BigDecimal	r22_india;
	private BigDecimal	r22_sydney;
	private BigDecimal	r22_uganda;
	private BigDecimal	r22_c10;
	private BigDecimal	r22_c11;
	private BigDecimal	r22_c12;
	private BigDecimal	r22_c13;
	private BigDecimal	r22_c14;
	private BigDecimal	r22_c15;
	private BigDecimal	r22_c16;
	private BigDecimal	r22_total;
	private String		r23_product;
	private BigDecimal	r23_botswana;
	private BigDecimal	r23_south_africa;
	private BigDecimal	r23_sadc;
	private BigDecimal	r23_usa;
	private BigDecimal	r23_uk;
	private BigDecimal	r23_europe;
	private BigDecimal	r23_india;
	private BigDecimal	r23_sydney;
	private BigDecimal	r23_uganda;
	private BigDecimal	r23_c10;
	private BigDecimal	r23_c11;
	private BigDecimal	r23_c12;
	private BigDecimal	r23_c13;
	private BigDecimal	r23_c14;
	private BigDecimal	r23_c15;
	private BigDecimal	r23_c16;
	private BigDecimal	r23_total;
	
	private String		r57_product;
	private BigDecimal	r57_botswana;
	private BigDecimal	r57_south_africa;
	private BigDecimal	r57_sadc;
	private BigDecimal	r57_usa;
	private BigDecimal	r57_uk;
	private BigDecimal	r57_europe;
	private BigDecimal	r57_india;
	private BigDecimal	r57_sydney;
	private BigDecimal	r57_uganda;
	private BigDecimal	r57_c10;
	private BigDecimal	r57_c11;
	private BigDecimal	r57_c12;
	private BigDecimal	r57_c13;
	private BigDecimal	r57_c14;
	private BigDecimal	r57_c15;
	private BigDecimal	r57_c16;
	private BigDecimal	r57_total;
	private String		r58_product;
	private BigDecimal	r58_botswana;
	private BigDecimal	r58_south_africa;
	private BigDecimal	r58_sadc;
	private BigDecimal	r58_usa;
	private BigDecimal	r58_uk;
	private BigDecimal	r58_europe;
	private BigDecimal	r58_india;
	private BigDecimal	r58_sydney;
	private BigDecimal	r58_uganda;
	private BigDecimal	r58_c10;
	private BigDecimal	r58_c11;
	private BigDecimal	r58_c12;
	private BigDecimal	r58_c13;
	private BigDecimal	r58_c14;
	private BigDecimal	r58_c15;
	private BigDecimal	r58_c16;
	private BigDecimal	r58_total;
	
	private String		r60_product;
	private BigDecimal	r60_botswana;
	private BigDecimal	r60_south_africa;
	private BigDecimal	r60_sadc;
	private BigDecimal	r60_usa;
	private BigDecimal	r60_uk;
	private BigDecimal	r60_europe;
	private BigDecimal	r60_india;
	private BigDecimal	r60_sydney;
	private BigDecimal	r60_uganda;
	private BigDecimal	r60_c10;
	private BigDecimal	r60_c11;
	private BigDecimal	r60_c12;
	private BigDecimal	r60_c13;
	private BigDecimal	r60_c14;
	private BigDecimal	r60_c15;
	private BigDecimal	r60_c16;
	private BigDecimal	r60_total;
	private String		r61_product;
	private BigDecimal	r61_botswana;
	private BigDecimal	r61_south_africa;
	private BigDecimal	r61_sadc;
	private BigDecimal	r61_usa;
	private BigDecimal	r61_uk;
	private BigDecimal	r61_europe;
	private BigDecimal	r61_india;
	private BigDecimal	r61_sydney;
	private BigDecimal	r61_uganda;
	private BigDecimal	r61_c10;
	private BigDecimal	r61_c11;
	private BigDecimal	r61_c12;
	private BigDecimal	r61_c13;
	private BigDecimal	r61_c14;
	private BigDecimal	r61_c15;
	private BigDecimal	r61_c16;
	private BigDecimal	r61_total;

	private String		r64_product;
	private BigDecimal	r64_botswana;
	private BigDecimal	r64_south_africa;
	private BigDecimal	r64_sadc;
	private BigDecimal	r64_usa;
	private BigDecimal	r64_uk;
	private BigDecimal	r64_europe;
	private BigDecimal	r64_india;
	private BigDecimal	r64_sydney;
	private BigDecimal	r64_uganda;
	private BigDecimal	r64_c10;
	private BigDecimal	r64_c11;
	private BigDecimal	r64_c12;
	private BigDecimal	r64_c13;
	private BigDecimal	r64_c14;
	private BigDecimal	r64_c15;
	private BigDecimal	r64_c16;
	private BigDecimal	r64_total;
	private String		r65_product;
	private BigDecimal	r65_botswana;
	private BigDecimal	r65_south_africa;
	private BigDecimal	r65_sadc;
	private BigDecimal	r65_usa;
	private BigDecimal	r65_uk;
	private BigDecimal	r65_europe;
	private BigDecimal	r65_india;
	private BigDecimal	r65_sydney;
	private BigDecimal	r65_uganda;
	private BigDecimal	r65_c10;
	private BigDecimal	r65_c11;
	private BigDecimal	r65_c12;
	private BigDecimal	r65_c13;
	private BigDecimal	r65_c14;
	private BigDecimal	r65_c15;
	private BigDecimal	r65_c16;
	private BigDecimal	r65_total;
	
	private String		r67_product;
	private BigDecimal	r67_botswana;
	private BigDecimal	r67_south_africa;
	private BigDecimal	r67_sadc;
	private BigDecimal	r67_usa;
	private BigDecimal	r67_uk;
	private BigDecimal	r67_europe;
	private BigDecimal	r67_india;
	private BigDecimal	r67_sydney;
	private BigDecimal	r67_uganda;
	private BigDecimal	r67_c10;
	private BigDecimal	r67_c11;
	private BigDecimal	r67_c12;
	private BigDecimal	r67_c13;
	private BigDecimal	r67_c14;
	private BigDecimal	r67_c15;
	private BigDecimal	r67_c16;
	private BigDecimal	r67_total;
	private String		r68_product;
	private BigDecimal	r68_botswana;
	private BigDecimal	r68_south_africa;
	private BigDecimal	r68_sadc;
	private BigDecimal	r68_usa;
	private BigDecimal	r68_uk;
	private BigDecimal	r68_europe;
	private BigDecimal	r68_india;
	private BigDecimal	r68_sydney;
	private BigDecimal	r68_uganda;
	private BigDecimal	r68_c10;
	private BigDecimal	r68_c11;
	private BigDecimal	r68_c12;
	private BigDecimal	r68_c13;
	private BigDecimal	r68_c14;
	private BigDecimal	r68_c15;
	private BigDecimal	r68_c16;
	private BigDecimal	r68_total;
	private BigDecimal	r111_botswana;
	private BigDecimal	r112_botswana;
	private BigDecimal	r113_botswana;
	private BigDecimal	r114_botswana;
	

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id

	private Date	report_date;
	private String	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_botswana() {
		return r22_botswana;
	}
	public void setR22_botswana(BigDecimal r22_botswana) {
		this.r22_botswana = r22_botswana;
	}
	public BigDecimal getR22_south_africa() {
		return r22_south_africa;
	}
	public void setR22_south_africa(BigDecimal r22_south_africa) {
		this.r22_south_africa = r22_south_africa;
	}
	public BigDecimal getR22_sadc() {
		return r22_sadc;
	}
	public void setR22_sadc(BigDecimal r22_sadc) {
		this.r22_sadc = r22_sadc;
	}
	public BigDecimal getR22_usa() {
		return r22_usa;
	}
	public void setR22_usa(BigDecimal r22_usa) {
		this.r22_usa = r22_usa;
	}
	public BigDecimal getR22_uk() {
		return r22_uk;
	}
	public void setR22_uk(BigDecimal r22_uk) {
		this.r22_uk = r22_uk;
	}
	public BigDecimal getR22_europe() {
		return r22_europe;
	}
	public void setR22_europe(BigDecimal r22_europe) {
		this.r22_europe = r22_europe;
	}
	public BigDecimal getR22_india() {
		return r22_india;
	}
	public void setR22_india(BigDecimal r22_india) {
		this.r22_india = r22_india;
	}
	public BigDecimal getR22_sydney() {
		return r22_sydney;
	}
	public void setR22_sydney(BigDecimal r22_sydney) {
		this.r22_sydney = r22_sydney;
	}
	public BigDecimal getR22_uganda() {
		return r22_uganda;
	}
	public void setR22_uganda(BigDecimal r22_uganda) {
		this.r22_uganda = r22_uganda;
	}
	public BigDecimal getR22_c10() {
		return r22_c10;
	}
	public void setR22_c10(BigDecimal r22_c10) {
		this.r22_c10 = r22_c10;
	}
	public BigDecimal getR22_c11() {
		return r22_c11;
	}
	public void setR22_c11(BigDecimal r22_c11) {
		this.r22_c11 = r22_c11;
	}
	public BigDecimal getR22_c12() {
		return r22_c12;
	}
	public void setR22_c12(BigDecimal r22_c12) {
		this.r22_c12 = r22_c12;
	}
	public BigDecimal getR22_c13() {
		return r22_c13;
	}
	public void setR22_c13(BigDecimal r22_c13) {
		this.r22_c13 = r22_c13;
	}
	public BigDecimal getR22_c14() {
		return r22_c14;
	}
	public void setR22_c14(BigDecimal r22_c14) {
		this.r22_c14 = r22_c14;
	}
	public BigDecimal getR22_c15() {
		return r22_c15;
	}
	public void setR22_c15(BigDecimal r22_c15) {
		this.r22_c15 = r22_c15;
	}
	public BigDecimal getR22_c16() {
		return r22_c16;
	}
	public void setR22_c16(BigDecimal r22_c16) {
		this.r22_c16 = r22_c16;
	}
	public BigDecimal getR22_total() {
		return r22_total;
	}
	public void setR22_total(BigDecimal r22_total) {
		this.r22_total = r22_total;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_botswana() {
		return r23_botswana;
	}
	public void setR23_botswana(BigDecimal r23_botswana) {
		this.r23_botswana = r23_botswana;
	}
	public BigDecimal getR23_south_africa() {
		return r23_south_africa;
	}
	public void setR23_south_africa(BigDecimal r23_south_africa) {
		this.r23_south_africa = r23_south_africa;
	}
	public BigDecimal getR23_sadc() {
		return r23_sadc;
	}
	public void setR23_sadc(BigDecimal r23_sadc) {
		this.r23_sadc = r23_sadc;
	}
	public BigDecimal getR23_usa() {
		return r23_usa;
	}
	public void setR23_usa(BigDecimal r23_usa) {
		this.r23_usa = r23_usa;
	}
	public BigDecimal getR23_uk() {
		return r23_uk;
	}
	public void setR23_uk(BigDecimal r23_uk) {
		this.r23_uk = r23_uk;
	}
	public BigDecimal getR23_europe() {
		return r23_europe;
	}
	public void setR23_europe(BigDecimal r23_europe) {
		this.r23_europe = r23_europe;
	}
	public BigDecimal getR23_india() {
		return r23_india;
	}
	public void setR23_india(BigDecimal r23_india) {
		this.r23_india = r23_india;
	}
	public BigDecimal getR23_sydney() {
		return r23_sydney;
	}
	public void setR23_sydney(BigDecimal r23_sydney) {
		this.r23_sydney = r23_sydney;
	}
	public BigDecimal getR23_uganda() {
		return r23_uganda;
	}
	public void setR23_uganda(BigDecimal r23_uganda) {
		this.r23_uganda = r23_uganda;
	}
	public BigDecimal getR23_c10() {
		return r23_c10;
	}
	public void setR23_c10(BigDecimal r23_c10) {
		this.r23_c10 = r23_c10;
	}
	public BigDecimal getR23_c11() {
		return r23_c11;
	}
	public void setR23_c11(BigDecimal r23_c11) {
		this.r23_c11 = r23_c11;
	}
	public BigDecimal getR23_c12() {
		return r23_c12;
	}
	public void setR23_c12(BigDecimal r23_c12) {
		this.r23_c12 = r23_c12;
	}
	public BigDecimal getR23_c13() {
		return r23_c13;
	}
	public void setR23_c13(BigDecimal r23_c13) {
		this.r23_c13 = r23_c13;
	}
	public BigDecimal getR23_c14() {
		return r23_c14;
	}
	public void setR23_c14(BigDecimal r23_c14) {
		this.r23_c14 = r23_c14;
	}
	public BigDecimal getR23_c15() {
		return r23_c15;
	}
	public void setR23_c15(BigDecimal r23_c15) {
		this.r23_c15 = r23_c15;
	}
	public BigDecimal getR23_c16() {
		return r23_c16;
	}
	public void setR23_c16(BigDecimal r23_c16) {
		this.r23_c16 = r23_c16;
	}
	public BigDecimal getR23_total() {
		return r23_total;
	}
	public void setR23_total(BigDecimal r23_total) {
		this.r23_total = r23_total;
	}
	public String getR57_product() {
		return r57_product;
	}
	public void setR57_product(String r57_product) {
		this.r57_product = r57_product;
	}
	public BigDecimal getR57_botswana() {
		return r57_botswana;
	}
	public void setR57_botswana(BigDecimal r57_botswana) {
		this.r57_botswana = r57_botswana;
	}
	public BigDecimal getR57_south_africa() {
		return r57_south_africa;
	}
	public void setR57_south_africa(BigDecimal r57_south_africa) {
		this.r57_south_africa = r57_south_africa;
	}
	public BigDecimal getR57_sadc() {
		return r57_sadc;
	}
	public void setR57_sadc(BigDecimal r57_sadc) {
		this.r57_sadc = r57_sadc;
	}
	public BigDecimal getR57_usa() {
		return r57_usa;
	}
	public void setR57_usa(BigDecimal r57_usa) {
		this.r57_usa = r57_usa;
	}
	public BigDecimal getR57_uk() {
		return r57_uk;
	}
	public void setR57_uk(BigDecimal r57_uk) {
		this.r57_uk = r57_uk;
	}
	public BigDecimal getR57_europe() {
		return r57_europe;
	}
	public void setR57_europe(BigDecimal r57_europe) {
		this.r57_europe = r57_europe;
	}
	public BigDecimal getR57_india() {
		return r57_india;
	}
	public void setR57_india(BigDecimal r57_india) {
		this.r57_india = r57_india;
	}
	public BigDecimal getR57_sydney() {
		return r57_sydney;
	}
	public void setR57_sydney(BigDecimal r57_sydney) {
		this.r57_sydney = r57_sydney;
	}
	public BigDecimal getR57_uganda() {
		return r57_uganda;
	}
	public void setR57_uganda(BigDecimal r57_uganda) {
		this.r57_uganda = r57_uganda;
	}
	public BigDecimal getR57_c10() {
		return r57_c10;
	}
	public void setR57_c10(BigDecimal r57_c10) {
		this.r57_c10 = r57_c10;
	}
	public BigDecimal getR57_c11() {
		return r57_c11;
	}
	public void setR57_c11(BigDecimal r57_c11) {
		this.r57_c11 = r57_c11;
	}
	public BigDecimal getR57_c12() {
		return r57_c12;
	}
	public void setR57_c12(BigDecimal r57_c12) {
		this.r57_c12 = r57_c12;
	}
	public BigDecimal getR57_c13() {
		return r57_c13;
	}
	public void setR57_c13(BigDecimal r57_c13) {
		this.r57_c13 = r57_c13;
	}
	public BigDecimal getR57_c14() {
		return r57_c14;
	}
	public void setR57_c14(BigDecimal r57_c14) {
		this.r57_c14 = r57_c14;
	}
	public BigDecimal getR57_c15() {
		return r57_c15;
	}
	public void setR57_c15(BigDecimal r57_c15) {
		this.r57_c15 = r57_c15;
	}
	public BigDecimal getR57_c16() {
		return r57_c16;
	}
	public void setR57_c16(BigDecimal r57_c16) {
		this.r57_c16 = r57_c16;
	}
	public BigDecimal getR57_total() {
		return r57_total;
	}
	public void setR57_total(BigDecimal r57_total) {
		this.r57_total = r57_total;
	}
	public String getR58_product() {
		return r58_product;
	}
	public void setR58_product(String r58_product) {
		this.r58_product = r58_product;
	}
	public BigDecimal getR58_botswana() {
		return r58_botswana;
	}
	public void setR58_botswana(BigDecimal r58_botswana) {
		this.r58_botswana = r58_botswana;
	}
	public BigDecimal getR58_south_africa() {
		return r58_south_africa;
	}
	public void setR58_south_africa(BigDecimal r58_south_africa) {
		this.r58_south_africa = r58_south_africa;
	}
	public BigDecimal getR58_sadc() {
		return r58_sadc;
	}
	public void setR58_sadc(BigDecimal r58_sadc) {
		this.r58_sadc = r58_sadc;
	}
	public BigDecimal getR58_usa() {
		return r58_usa;
	}
	public void setR58_usa(BigDecimal r58_usa) {
		this.r58_usa = r58_usa;
	}
	public BigDecimal getR58_uk() {
		return r58_uk;
	}
	public void setR58_uk(BigDecimal r58_uk) {
		this.r58_uk = r58_uk;
	}
	public BigDecimal getR58_europe() {
		return r58_europe;
	}
	public void setR58_europe(BigDecimal r58_europe) {
		this.r58_europe = r58_europe;
	}
	public BigDecimal getR58_india() {
		return r58_india;
	}
	public void setR58_india(BigDecimal r58_india) {
		this.r58_india = r58_india;
	}
	public BigDecimal getR58_sydney() {
		return r58_sydney;
	}
	public void setR58_sydney(BigDecimal r58_sydney) {
		this.r58_sydney = r58_sydney;
	}
	public BigDecimal getR58_uganda() {
		return r58_uganda;
	}
	public void setR58_uganda(BigDecimal r58_uganda) {
		this.r58_uganda = r58_uganda;
	}
	public BigDecimal getR58_c10() {
		return r58_c10;
	}
	public void setR58_c10(BigDecimal r58_c10) {
		this.r58_c10 = r58_c10;
	}
	public BigDecimal getR58_c11() {
		return r58_c11;
	}
	public void setR58_c11(BigDecimal r58_c11) {
		this.r58_c11 = r58_c11;
	}
	public BigDecimal getR58_c12() {
		return r58_c12;
	}
	public void setR58_c12(BigDecimal r58_c12) {
		this.r58_c12 = r58_c12;
	}
	public BigDecimal getR58_c13() {
		return r58_c13;
	}
	public void setR58_c13(BigDecimal r58_c13) {
		this.r58_c13 = r58_c13;
	}
	public BigDecimal getR58_c14() {
		return r58_c14;
	}
	public void setR58_c14(BigDecimal r58_c14) {
		this.r58_c14 = r58_c14;
	}
	public BigDecimal getR58_c15() {
		return r58_c15;
	}
	public void setR58_c15(BigDecimal r58_c15) {
		this.r58_c15 = r58_c15;
	}
	public BigDecimal getR58_c16() {
		return r58_c16;
	}
	public void setR58_c16(BigDecimal r58_c16) {
		this.r58_c16 = r58_c16;
	}
	public BigDecimal getR58_total() {
		return r58_total;
	}
	public void setR58_total(BigDecimal r58_total) {
		this.r58_total = r58_total;
	}
	public String getR60_product() {
		return r60_product;
	}
	public void setR60_product(String r60_product) {
		this.r60_product = r60_product;
	}
	public BigDecimal getR60_botswana() {
		return r60_botswana;
	}
	public void setR60_botswana(BigDecimal r60_botswana) {
		this.r60_botswana = r60_botswana;
	}
	public BigDecimal getR60_south_africa() {
		return r60_south_africa;
	}
	public void setR60_south_africa(BigDecimal r60_south_africa) {
		this.r60_south_africa = r60_south_africa;
	}
	public BigDecimal getR60_sadc() {
		return r60_sadc;
	}
	public void setR60_sadc(BigDecimal r60_sadc) {
		this.r60_sadc = r60_sadc;
	}
	public BigDecimal getR60_usa() {
		return r60_usa;
	}
	public void setR60_usa(BigDecimal r60_usa) {
		this.r60_usa = r60_usa;
	}
	public BigDecimal getR60_uk() {
		return r60_uk;
	}
	public void setR60_uk(BigDecimal r60_uk) {
		this.r60_uk = r60_uk;
	}
	public BigDecimal getR60_europe() {
		return r60_europe;
	}
	public void setR60_europe(BigDecimal r60_europe) {
		this.r60_europe = r60_europe;
	}
	public BigDecimal getR60_india() {
		return r60_india;
	}
	public void setR60_india(BigDecimal r60_india) {
		this.r60_india = r60_india;
	}
	public BigDecimal getR60_sydney() {
		return r60_sydney;
	}
	public void setR60_sydney(BigDecimal r60_sydney) {
		this.r60_sydney = r60_sydney;
	}
	public BigDecimal getR60_uganda() {
		return r60_uganda;
	}
	public void setR60_uganda(BigDecimal r60_uganda) {
		this.r60_uganda = r60_uganda;
	}
	public BigDecimal getR60_c10() {
		return r60_c10;
	}
	public void setR60_c10(BigDecimal r60_c10) {
		this.r60_c10 = r60_c10;
	}
	public BigDecimal getR60_c11() {
		return r60_c11;
	}
	public void setR60_c11(BigDecimal r60_c11) {
		this.r60_c11 = r60_c11;
	}
	public BigDecimal getR60_c12() {
		return r60_c12;
	}
	public void setR60_c12(BigDecimal r60_c12) {
		this.r60_c12 = r60_c12;
	}
	public BigDecimal getR60_c13() {
		return r60_c13;
	}
	public void setR60_c13(BigDecimal r60_c13) {
		this.r60_c13 = r60_c13;
	}
	public BigDecimal getR60_c14() {
		return r60_c14;
	}
	public void setR60_c14(BigDecimal r60_c14) {
		this.r60_c14 = r60_c14;
	}
	public BigDecimal getR60_c15() {
		return r60_c15;
	}
	public void setR60_c15(BigDecimal r60_c15) {
		this.r60_c15 = r60_c15;
	}
	public BigDecimal getR60_c16() {
		return r60_c16;
	}
	public void setR60_c16(BigDecimal r60_c16) {
		this.r60_c16 = r60_c16;
	}
	public BigDecimal getR60_total() {
		return r60_total;
	}
	public void setR60_total(BigDecimal r60_total) {
		this.r60_total = r60_total;
	}
	public String getR61_product() {
		return r61_product;
	}
	public void setR61_product(String r61_product) {
		this.r61_product = r61_product;
	}
	public BigDecimal getR61_botswana() {
		return r61_botswana;
	}
	public void setR61_botswana(BigDecimal r61_botswana) {
		this.r61_botswana = r61_botswana;
	}
	public BigDecimal getR61_south_africa() {
		return r61_south_africa;
	}
	public void setR61_south_africa(BigDecimal r61_south_africa) {
		this.r61_south_africa = r61_south_africa;
	}
	public BigDecimal getR61_sadc() {
		return r61_sadc;
	}
	public void setR61_sadc(BigDecimal r61_sadc) {
		this.r61_sadc = r61_sadc;
	}
	public BigDecimal getR61_usa() {
		return r61_usa;
	}
	public void setR61_usa(BigDecimal r61_usa) {
		this.r61_usa = r61_usa;
	}
	public BigDecimal getR61_uk() {
		return r61_uk;
	}
	public void setR61_uk(BigDecimal r61_uk) {
		this.r61_uk = r61_uk;
	}
	public BigDecimal getR61_europe() {
		return r61_europe;
	}
	public void setR61_europe(BigDecimal r61_europe) {
		this.r61_europe = r61_europe;
	}
	public BigDecimal getR61_india() {
		return r61_india;
	}
	public void setR61_india(BigDecimal r61_india) {
		this.r61_india = r61_india;
	}
	public BigDecimal getR61_sydney() {
		return r61_sydney;
	}
	public void setR61_sydney(BigDecimal r61_sydney) {
		this.r61_sydney = r61_sydney;
	}
	public BigDecimal getR61_uganda() {
		return r61_uganda;
	}
	public void setR61_uganda(BigDecimal r61_uganda) {
		this.r61_uganda = r61_uganda;
	}
	public BigDecimal getR61_c10() {
		return r61_c10;
	}
	public void setR61_c10(BigDecimal r61_c10) {
		this.r61_c10 = r61_c10;
	}
	public BigDecimal getR61_c11() {
		return r61_c11;
	}
	public void setR61_c11(BigDecimal r61_c11) {
		this.r61_c11 = r61_c11;
	}
	public BigDecimal getR61_c12() {
		return r61_c12;
	}
	public void setR61_c12(BigDecimal r61_c12) {
		this.r61_c12 = r61_c12;
	}
	public BigDecimal getR61_c13() {
		return r61_c13;
	}
	public void setR61_c13(BigDecimal r61_c13) {
		this.r61_c13 = r61_c13;
	}
	public BigDecimal getR61_c14() {
		return r61_c14;
	}
	public void setR61_c14(BigDecimal r61_c14) {
		this.r61_c14 = r61_c14;
	}
	public BigDecimal getR61_c15() {
		return r61_c15;
	}
	public void setR61_c15(BigDecimal r61_c15) {
		this.r61_c15 = r61_c15;
	}
	public BigDecimal getR61_c16() {
		return r61_c16;
	}
	public void setR61_c16(BigDecimal r61_c16) {
		this.r61_c16 = r61_c16;
	}
	public BigDecimal getR61_total() {
		return r61_total;
	}
	public void setR61_total(BigDecimal r61_total) {
		this.r61_total = r61_total;
	}
	public String getR64_product() {
		return r64_product;
	}
	public void setR64_product(String r64_product) {
		this.r64_product = r64_product;
	}
	public BigDecimal getR64_botswana() {
		return r64_botswana;
	}
	public void setR64_botswana(BigDecimal r64_botswana) {
		this.r64_botswana = r64_botswana;
	}
	public BigDecimal getR64_south_africa() {
		return r64_south_africa;
	}
	public void setR64_south_africa(BigDecimal r64_south_africa) {
		this.r64_south_africa = r64_south_africa;
	}
	public BigDecimal getR64_sadc() {
		return r64_sadc;
	}
	public void setR64_sadc(BigDecimal r64_sadc) {
		this.r64_sadc = r64_sadc;
	}
	public BigDecimal getR64_usa() {
		return r64_usa;
	}
	public void setR64_usa(BigDecimal r64_usa) {
		this.r64_usa = r64_usa;
	}
	public BigDecimal getR64_uk() {
		return r64_uk;
	}
	public void setR64_uk(BigDecimal r64_uk) {
		this.r64_uk = r64_uk;
	}
	public BigDecimal getR64_europe() {
		return r64_europe;
	}
	public void setR64_europe(BigDecimal r64_europe) {
		this.r64_europe = r64_europe;
	}
	public BigDecimal getR64_india() {
		return r64_india;
	}
	public void setR64_india(BigDecimal r64_india) {
		this.r64_india = r64_india;
	}
	public BigDecimal getR64_sydney() {
		return r64_sydney;
	}
	public void setR64_sydney(BigDecimal r64_sydney) {
		this.r64_sydney = r64_sydney;
	}
	public BigDecimal getR64_uganda() {
		return r64_uganda;
	}
	public void setR64_uganda(BigDecimal r64_uganda) {
		this.r64_uganda = r64_uganda;
	}
	public BigDecimal getR64_c10() {
		return r64_c10;
	}
	public void setR64_c10(BigDecimal r64_c10) {
		this.r64_c10 = r64_c10;
	}
	public BigDecimal getR64_c11() {
		return r64_c11;
	}
	public void setR64_c11(BigDecimal r64_c11) {
		this.r64_c11 = r64_c11;
	}
	public BigDecimal getR64_c12() {
		return r64_c12;
	}
	public void setR64_c12(BigDecimal r64_c12) {
		this.r64_c12 = r64_c12;
	}
	public BigDecimal getR64_c13() {
		return r64_c13;
	}
	public void setR64_c13(BigDecimal r64_c13) {
		this.r64_c13 = r64_c13;
	}
	public BigDecimal getR64_c14() {
		return r64_c14;
	}
	public void setR64_c14(BigDecimal r64_c14) {
		this.r64_c14 = r64_c14;
	}
	public BigDecimal getR64_c15() {
		return r64_c15;
	}
	public void setR64_c15(BigDecimal r64_c15) {
		this.r64_c15 = r64_c15;
	}
	public BigDecimal getR64_c16() {
		return r64_c16;
	}
	public void setR64_c16(BigDecimal r64_c16) {
		this.r64_c16 = r64_c16;
	}
	public BigDecimal getR64_total() {
		return r64_total;
	}
	public void setR64_total(BigDecimal r64_total) {
		this.r64_total = r64_total;
	}
	public String getR65_product() {
		return r65_product;
	}
	public void setR65_product(String r65_product) {
		this.r65_product = r65_product;
	}
	public BigDecimal getR65_botswana() {
		return r65_botswana;
	}
	public void setR65_botswana(BigDecimal r65_botswana) {
		this.r65_botswana = r65_botswana;
	}
	public BigDecimal getR65_south_africa() {
		return r65_south_africa;
	}
	public void setR65_south_africa(BigDecimal r65_south_africa) {
		this.r65_south_africa = r65_south_africa;
	}
	public BigDecimal getR65_sadc() {
		return r65_sadc;
	}
	public void setR65_sadc(BigDecimal r65_sadc) {
		this.r65_sadc = r65_sadc;
	}
	public BigDecimal getR65_usa() {
		return r65_usa;
	}
	public void setR65_usa(BigDecimal r65_usa) {
		this.r65_usa = r65_usa;
	}
	public BigDecimal getR65_uk() {
		return r65_uk;
	}
	public void setR65_uk(BigDecimal r65_uk) {
		this.r65_uk = r65_uk;
	}
	public BigDecimal getR65_europe() {
		return r65_europe;
	}
	public void setR65_europe(BigDecimal r65_europe) {
		this.r65_europe = r65_europe;
	}
	public BigDecimal getR65_india() {
		return r65_india;
	}
	public void setR65_india(BigDecimal r65_india) {
		this.r65_india = r65_india;
	}
	public BigDecimal getR65_sydney() {
		return r65_sydney;
	}
	public void setR65_sydney(BigDecimal r65_sydney) {
		this.r65_sydney = r65_sydney;
	}
	public BigDecimal getR65_uganda() {
		return r65_uganda;
	}
	public void setR65_uganda(BigDecimal r65_uganda) {
		this.r65_uganda = r65_uganda;
	}
	public BigDecimal getR65_c10() {
		return r65_c10;
	}
	public void setR65_c10(BigDecimal r65_c10) {
		this.r65_c10 = r65_c10;
	}
	public BigDecimal getR65_c11() {
		return r65_c11;
	}
	public void setR65_c11(BigDecimal r65_c11) {
		this.r65_c11 = r65_c11;
	}
	public BigDecimal getR65_c12() {
		return r65_c12;
	}
	public void setR65_c12(BigDecimal r65_c12) {
		this.r65_c12 = r65_c12;
	}
	public BigDecimal getR65_c13() {
		return r65_c13;
	}
	public void setR65_c13(BigDecimal r65_c13) {
		this.r65_c13 = r65_c13;
	}
	public BigDecimal getR65_c14() {
		return r65_c14;
	}
	public void setR65_c14(BigDecimal r65_c14) {
		this.r65_c14 = r65_c14;
	}
	public BigDecimal getR65_c15() {
		return r65_c15;
	}
	public void setR65_c15(BigDecimal r65_c15) {
		this.r65_c15 = r65_c15;
	}
	public BigDecimal getR65_c16() {
		return r65_c16;
	}
	public void setR65_c16(BigDecimal r65_c16) {
		this.r65_c16 = r65_c16;
	}
	public BigDecimal getR65_total() {
		return r65_total;
	}
	public void setR65_total(BigDecimal r65_total) {
		this.r65_total = r65_total;
	}
	public String getR67_product() {
		return r67_product;
	}
	public void setR67_product(String r67_product) {
		this.r67_product = r67_product;
	}
	public BigDecimal getR67_botswana() {
		return r67_botswana;
	}
	public void setR67_botswana(BigDecimal r67_botswana) {
		this.r67_botswana = r67_botswana;
	}
	public BigDecimal getR67_south_africa() {
		return r67_south_africa;
	}
	public void setR67_south_africa(BigDecimal r67_south_africa) {
		this.r67_south_africa = r67_south_africa;
	}
	public BigDecimal getR67_sadc() {
		return r67_sadc;
	}
	public void setR67_sadc(BigDecimal r67_sadc) {
		this.r67_sadc = r67_sadc;
	}
	public BigDecimal getR67_usa() {
		return r67_usa;
	}
	public void setR67_usa(BigDecimal r67_usa) {
		this.r67_usa = r67_usa;
	}
	public BigDecimal getR67_uk() {
		return r67_uk;
	}
	public void setR67_uk(BigDecimal r67_uk) {
		this.r67_uk = r67_uk;
	}
	public BigDecimal getR67_europe() {
		return r67_europe;
	}
	public void setR67_europe(BigDecimal r67_europe) {
		this.r67_europe = r67_europe;
	}
	public BigDecimal getR67_india() {
		return r67_india;
	}
	public void setR67_india(BigDecimal r67_india) {
		this.r67_india = r67_india;
	}
	public BigDecimal getR67_sydney() {
		return r67_sydney;
	}
	public void setR67_sydney(BigDecimal r67_sydney) {
		this.r67_sydney = r67_sydney;
	}
	public BigDecimal getR67_uganda() {
		return r67_uganda;
	}
	public void setR67_uganda(BigDecimal r67_uganda) {
		this.r67_uganda = r67_uganda;
	}
	public BigDecimal getR67_c10() {
		return r67_c10;
	}
	public void setR67_c10(BigDecimal r67_c10) {
		this.r67_c10 = r67_c10;
	}
	public BigDecimal getR67_c11() {
		return r67_c11;
	}
	public void setR67_c11(BigDecimal r67_c11) {
		this.r67_c11 = r67_c11;
	}
	public BigDecimal getR67_c12() {
		return r67_c12;
	}
	public void setR67_c12(BigDecimal r67_c12) {
		this.r67_c12 = r67_c12;
	}
	public BigDecimal getR67_c13() {
		return r67_c13;
	}
	public void setR67_c13(BigDecimal r67_c13) {
		this.r67_c13 = r67_c13;
	}
	public BigDecimal getR67_c14() {
		return r67_c14;
	}
	public void setR67_c14(BigDecimal r67_c14) {
		this.r67_c14 = r67_c14;
	}
	public BigDecimal getR67_c15() {
		return r67_c15;
	}
	public void setR67_c15(BigDecimal r67_c15) {
		this.r67_c15 = r67_c15;
	}
	public BigDecimal getR67_c16() {
		return r67_c16;
	}
	public void setR67_c16(BigDecimal r67_c16) {
		this.r67_c16 = r67_c16;
	}
	public BigDecimal getR67_total() {
		return r67_total;
	}
	public void setR67_total(BigDecimal r67_total) {
		this.r67_total = r67_total;
	}
	public String getR68_product() {
		return r68_product;
	}
	public void setR68_product(String r68_product) {
		this.r68_product = r68_product;
	}
	public BigDecimal getR68_botswana() {
		return r68_botswana;
	}
	public void setR68_botswana(BigDecimal r68_botswana) {
		this.r68_botswana = r68_botswana;
	}
	public BigDecimal getR68_south_africa() {
		return r68_south_africa;
	}
	public void setR68_south_africa(BigDecimal r68_south_africa) {
		this.r68_south_africa = r68_south_africa;
	}
	public BigDecimal getR68_sadc() {
		return r68_sadc;
	}
	public void setR68_sadc(BigDecimal r68_sadc) {
		this.r68_sadc = r68_sadc;
	}
	public BigDecimal getR68_usa() {
		return r68_usa;
	}
	public void setR68_usa(BigDecimal r68_usa) {
		this.r68_usa = r68_usa;
	}
	public BigDecimal getR68_uk() {
		return r68_uk;
	}
	public void setR68_uk(BigDecimal r68_uk) {
		this.r68_uk = r68_uk;
	}
	public BigDecimal getR68_europe() {
		return r68_europe;
	}
	public void setR68_europe(BigDecimal r68_europe) {
		this.r68_europe = r68_europe;
	}
	public BigDecimal getR68_india() {
		return r68_india;
	}
	public void setR68_india(BigDecimal r68_india) {
		this.r68_india = r68_india;
	}
	public BigDecimal getR68_sydney() {
		return r68_sydney;
	}
	public void setR68_sydney(BigDecimal r68_sydney) {
		this.r68_sydney = r68_sydney;
	}
	public BigDecimal getR68_uganda() {
		return r68_uganda;
	}
	public void setR68_uganda(BigDecimal r68_uganda) {
		this.r68_uganda = r68_uganda;
	}
	public BigDecimal getR68_c10() {
		return r68_c10;
	}
	public void setR68_c10(BigDecimal r68_c10) {
		this.r68_c10 = r68_c10;
	}
	public BigDecimal getR68_c11() {
		return r68_c11;
	}
	public void setR68_c11(BigDecimal r68_c11) {
		this.r68_c11 = r68_c11;
	}
	public BigDecimal getR68_c12() {
		return r68_c12;
	}
	public void setR68_c12(BigDecimal r68_c12) {
		this.r68_c12 = r68_c12;
	}
	public BigDecimal getR68_c13() {
		return r68_c13;
	}
	public void setR68_c13(BigDecimal r68_c13) {
		this.r68_c13 = r68_c13;
	}
	public BigDecimal getR68_c14() {
		return r68_c14;
	}
	public void setR68_c14(BigDecimal r68_c14) {
		this.r68_c14 = r68_c14;
	}
	public BigDecimal getR68_c15() {
		return r68_c15;
	}
	public void setR68_c15(BigDecimal r68_c15) {
		this.r68_c15 = r68_c15;
	}
	public BigDecimal getR68_c16() {
		return r68_c16;
	}
	public void setR68_c16(BigDecimal r68_c16) {
		this.r68_c16 = r68_c16;
	}
	public BigDecimal getR68_total() {
		return r68_total;
	}
	public void setR68_total(BigDecimal r68_total) {
		this.r68_total = r68_total;
	}
	public BigDecimal getR111_botswana() {
		return r111_botswana;
	}
	public void setR111_botswana(BigDecimal r111_botswana) {
		this.r111_botswana = r111_botswana;
	}
	public BigDecimal getR112_botswana() {
		return r112_botswana;
	}
	public void setR112_botswana(BigDecimal r112_botswana) {
		this.r112_botswana = r112_botswana;
	}
	public BigDecimal getR113_botswana() {
		return r113_botswana;
	}
	public void setR113_botswana(BigDecimal r113_botswana) {
		this.r113_botswana = r113_botswana;
	}
	public BigDecimal getR114_botswana() {
		return r114_botswana;
	}
	public void setR114_botswana(BigDecimal r114_botswana) {
		this.r114_botswana = r114_botswana;
	}
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
	public M_GALOR_Manual_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	


}
