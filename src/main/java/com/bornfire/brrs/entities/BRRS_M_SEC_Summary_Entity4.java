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
@Table(name = "BRRS_M_SEC_SUMMARYTABLE4") 

public class BRRS_M_SEC_Summary_Entity4 {
	// === R36 ===
	private String R36_PRODUCT;
	private BigDecimal R36_0_1Y_FT;
	private BigDecimal R36_0_1Y_HTM;
	private BigDecimal R36_0_1Y_TOTAL;
	private BigDecimal R36_1_5Y_FT;
	private BigDecimal R36_1_5Y_HTM;
	private BigDecimal R36_1_5Y_TOTAL;
	private BigDecimal R36_O5Y_FT;
	private BigDecimal R36_O5Y_HTM;
	private BigDecimal R36_O5Y_TOTAL;
	private BigDecimal R36_T_FT;
	private BigDecimal R36_T_HTM;
	private BigDecimal R36_T_TOTAL;

	// === R37 ===
	private String R37_PRODUCT;
	private BigDecimal R37_0_1Y_FT;
	private BigDecimal R37_0_1Y_HTM;
	private BigDecimal R37_0_1Y_TOTAL;
	private BigDecimal R37_1_5Y_FT;
	private BigDecimal R37_1_5Y_HTM;
	private BigDecimal R37_1_5Y_TOTAL;
	private BigDecimal R37_O5Y_FT;
	private BigDecimal R37_O5Y_HTM;
	private BigDecimal R37_O5Y_TOTAL;
	private BigDecimal R37_T_FT;
	private BigDecimal R37_T_HTM;
	private BigDecimal R37_T_TOTAL;

	// === R38 ===
	private String R38_PRODUCT;
	private BigDecimal R38_0_1Y_FT;
	private BigDecimal R38_0_1Y_HTM;
	private BigDecimal R38_0_1Y_TOTAL;
	private BigDecimal R38_1_5Y_FT;
	private BigDecimal R38_1_5Y_HTM;
	private BigDecimal R38_1_5Y_TOTAL;
	private BigDecimal R38_O5Y_FT;
	private BigDecimal R38_O5Y_HTM;
	private BigDecimal R38_O5Y_TOTAL;
	private BigDecimal R38_T_FT;
	private BigDecimal R38_T_HTM;
	private BigDecimal R38_T_TOTAL;

	// === R39 ===
	private String R39_PRODUCT;
	private BigDecimal R39_0_1Y_FT;
	private BigDecimal R39_0_1Y_HTM;
	private BigDecimal R39_0_1Y_TOTAL;
	private BigDecimal R39_1_5Y_FT;
	private BigDecimal R39_1_5Y_HTM;
	private BigDecimal R39_1_5Y_TOTAL;
	private BigDecimal R39_O5Y_FT;
	private BigDecimal R39_O5Y_HTM;
	private BigDecimal R39_O5Y_TOTAL;
	private BigDecimal R39_T_FT;
	private BigDecimal R39_T_HTM;
	private BigDecimal R39_T_TOTAL;

	// === R40 ===
	private String R40_PRODUCT;
	private BigDecimal R40_0_1Y_FT;
	private BigDecimal R40_0_1Y_HTM;
	private BigDecimal R40_0_1Y_TOTAL;
	private BigDecimal R40_1_5Y_FT;
	private BigDecimal R40_1_5Y_HTM;
	private BigDecimal R40_1_5Y_TOTAL;
	private BigDecimal R40_O5Y_FT;
	private BigDecimal R40_O5Y_HTM;
	private BigDecimal R40_O5Y_TOTAL;
	private BigDecimal R40_T_FT;
	private BigDecimal R40_T_HTM;
	private BigDecimal R40_T_TOTAL;

	// === R41 ===
	private String R41_PRODUCT;
	private BigDecimal R41_0_1Y_FT;
	private BigDecimal R41_0_1Y_HTM;
	private BigDecimal R41_0_1Y_TOTAL;
	private BigDecimal R41_1_5Y_FT;
	private BigDecimal R41_1_5Y_HTM;
	private BigDecimal R41_1_5Y_TOTAL;
	private BigDecimal R41_O5Y_FT;
	private BigDecimal R41_O5Y_HTM;
	private BigDecimal R41_O5Y_TOTAL;
	private BigDecimal R41_T_FT;
	private BigDecimal R41_T_HTM;
	private BigDecimal R41_T_TOTAL;

	// === R42 ===
	private String R42_PRODUCT;
	private BigDecimal R42_0_1Y_FT;
	private BigDecimal R42_0_1Y_HTM;
	private BigDecimal R42_0_1Y_TOTAL;
	private BigDecimal R42_1_5Y_FT;
	private BigDecimal R42_1_5Y_HTM;
	private BigDecimal R42_1_5Y_TOTAL;
	private BigDecimal R42_O5Y_FT;
	private BigDecimal R42_O5Y_HTM;
	private BigDecimal R42_O5Y_TOTAL;
	private BigDecimal R42_T_FT;
	private BigDecimal R42_T_HTM;
	private BigDecimal R42_T_TOTAL;

	// === R43 ===
	private String R43_PRODUCT;
	private BigDecimal R43_0_1Y_FT;
	private BigDecimal R43_0_1Y_HTM;
	private BigDecimal R43_0_1Y_TOTAL;
	private BigDecimal R43_1_5Y_FT;
	private BigDecimal R43_1_5Y_HTM;
	private BigDecimal R43_1_5Y_TOTAL;
	private BigDecimal R43_O5Y_FT;
	private BigDecimal R43_O5Y_HTM;
	private BigDecimal R43_O5Y_TOTAL;
	private BigDecimal R43_T_FT;
	private BigDecimal R43_T_HTM;
	private BigDecimal R43_T_TOTAL;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_0_1Y_FT() {
		return R36_0_1Y_FT;
	}
	public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
		R36_0_1Y_FT = r36_0_1y_FT;
	}
	public BigDecimal getR36_0_1Y_HTM() {
		return R36_0_1Y_HTM;
	}
	public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
		R36_0_1Y_HTM = r36_0_1y_HTM;
	}
	public BigDecimal getR36_0_1Y_TOTAL() {
		return R36_0_1Y_TOTAL;
	}
	public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
		R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
	}
	public BigDecimal getR36_1_5Y_FT() {
		return R36_1_5Y_FT;
	}
	public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
		R36_1_5Y_FT = r36_1_5y_FT;
	}
	public BigDecimal getR36_1_5Y_HTM() {
		return R36_1_5Y_HTM;
	}
	public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
		R36_1_5Y_HTM = r36_1_5y_HTM;
	}
	public BigDecimal getR36_1_5Y_TOTAL() {
		return R36_1_5Y_TOTAL;
	}
	public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
		R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
	}
	public BigDecimal getR36_O5Y_FT() {
		return R36_O5Y_FT;
	}
	public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
		R36_O5Y_FT = r36_O5Y_FT;
	}
	public BigDecimal getR36_O5Y_HTM() {
		return R36_O5Y_HTM;
	}
	public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
		R36_O5Y_HTM = r36_O5Y_HTM;
	}
	public BigDecimal getR36_O5Y_TOTAL() {
		return R36_O5Y_TOTAL;
	}
	public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
		R36_O5Y_TOTAL = r36_O5Y_TOTAL;
	}
	public BigDecimal getR36_T_FT() {
		return R36_T_FT;
	}
	public void setR36_T_FT(BigDecimal r36_T_FT) {
		R36_T_FT = r36_T_FT;
	}
	public BigDecimal getR36_T_HTM() {
		return R36_T_HTM;
	}
	public void setR36_T_HTM(BigDecimal r36_T_HTM) {
		R36_T_HTM = r36_T_HTM;
	}
	public BigDecimal getR36_T_TOTAL() {
		return R36_T_TOTAL;
	}
	public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
		R36_T_TOTAL = r36_T_TOTAL;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_0_1Y_FT() {
		return R37_0_1Y_FT;
	}
	public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
		R37_0_1Y_FT = r37_0_1y_FT;
	}
	public BigDecimal getR37_0_1Y_HTM() {
		return R37_0_1Y_HTM;
	}
	public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
		R37_0_1Y_HTM = r37_0_1y_HTM;
	}
	public BigDecimal getR37_0_1Y_TOTAL() {
		return R37_0_1Y_TOTAL;
	}
	public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
		R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
	}
	public BigDecimal getR37_1_5Y_FT() {
		return R37_1_5Y_FT;
	}
	public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
		R37_1_5Y_FT = r37_1_5y_FT;
	}
	public BigDecimal getR37_1_5Y_HTM() {
		return R37_1_5Y_HTM;
	}
	public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
		R37_1_5Y_HTM = r37_1_5y_HTM;
	}
	public BigDecimal getR37_1_5Y_TOTAL() {
		return R37_1_5Y_TOTAL;
	}
	public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
		R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
	}
	public BigDecimal getR37_O5Y_FT() {
		return R37_O5Y_FT;
	}
	public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
		R37_O5Y_FT = r37_O5Y_FT;
	}
	public BigDecimal getR37_O5Y_HTM() {
		return R37_O5Y_HTM;
	}
	public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
		R37_O5Y_HTM = r37_O5Y_HTM;
	}
	public BigDecimal getR37_O5Y_TOTAL() {
		return R37_O5Y_TOTAL;
	}
	public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
		R37_O5Y_TOTAL = r37_O5Y_TOTAL;
	}
	public BigDecimal getR37_T_FT() {
		return R37_T_FT;
	}
	public void setR37_T_FT(BigDecimal r37_T_FT) {
		R37_T_FT = r37_T_FT;
	}
	public BigDecimal getR37_T_HTM() {
		return R37_T_HTM;
	}
	public void setR37_T_HTM(BigDecimal r37_T_HTM) {
		R37_T_HTM = r37_T_HTM;
	}
	public BigDecimal getR37_T_TOTAL() {
		return R37_T_TOTAL;
	}
	public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
		R37_T_TOTAL = r37_T_TOTAL;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_0_1Y_FT() {
		return R38_0_1Y_FT;
	}
	public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
		R38_0_1Y_FT = r38_0_1y_FT;
	}
	public BigDecimal getR38_0_1Y_HTM() {
		return R38_0_1Y_HTM;
	}
	public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
		R38_0_1Y_HTM = r38_0_1y_HTM;
	}
	public BigDecimal getR38_0_1Y_TOTAL() {
		return R38_0_1Y_TOTAL;
	}
	public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
		R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
	}
	public BigDecimal getR38_1_5Y_FT() {
		return R38_1_5Y_FT;
	}
	public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
		R38_1_5Y_FT = r38_1_5y_FT;
	}
	public BigDecimal getR38_1_5Y_HTM() {
		return R38_1_5Y_HTM;
	}
	public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
		R38_1_5Y_HTM = r38_1_5y_HTM;
	}
	public BigDecimal getR38_1_5Y_TOTAL() {
		return R38_1_5Y_TOTAL;
	}
	public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
		R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
	}
	public BigDecimal getR38_O5Y_FT() {
		return R38_O5Y_FT;
	}
	public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
		R38_O5Y_FT = r38_O5Y_FT;
	}
	public BigDecimal getR38_O5Y_HTM() {
		return R38_O5Y_HTM;
	}
	public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
		R38_O5Y_HTM = r38_O5Y_HTM;
	}
	public BigDecimal getR38_O5Y_TOTAL() {
		return R38_O5Y_TOTAL;
	}
	public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
		R38_O5Y_TOTAL = r38_O5Y_TOTAL;
	}
	public BigDecimal getR38_T_FT() {
		return R38_T_FT;
	}
	public void setR38_T_FT(BigDecimal r38_T_FT) {
		R38_T_FT = r38_T_FT;
	}
	public BigDecimal getR38_T_HTM() {
		return R38_T_HTM;
	}
	public void setR38_T_HTM(BigDecimal r38_T_HTM) {
		R38_T_HTM = r38_T_HTM;
	}
	public BigDecimal getR38_T_TOTAL() {
		return R38_T_TOTAL;
	}
	public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
		R38_T_TOTAL = r38_T_TOTAL;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_0_1Y_FT() {
		return R39_0_1Y_FT;
	}
	public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
		R39_0_1Y_FT = r39_0_1y_FT;
	}
	public BigDecimal getR39_0_1Y_HTM() {
		return R39_0_1Y_HTM;
	}
	public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
		R39_0_1Y_HTM = r39_0_1y_HTM;
	}
	public BigDecimal getR39_0_1Y_TOTAL() {
		return R39_0_1Y_TOTAL;
	}
	public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
		R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
	}
	public BigDecimal getR39_1_5Y_FT() {
		return R39_1_5Y_FT;
	}
	public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
		R39_1_5Y_FT = r39_1_5y_FT;
	}
	public BigDecimal getR39_1_5Y_HTM() {
		return R39_1_5Y_HTM;
	}
	public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
		R39_1_5Y_HTM = r39_1_5y_HTM;
	}
	public BigDecimal getR39_1_5Y_TOTAL() {
		return R39_1_5Y_TOTAL;
	}
	public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
		R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
	}
	public BigDecimal getR39_O5Y_FT() {
		return R39_O5Y_FT;
	}
	public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
		R39_O5Y_FT = r39_O5Y_FT;
	}
	public BigDecimal getR39_O5Y_HTM() {
		return R39_O5Y_HTM;
	}
	public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
		R39_O5Y_HTM = r39_O5Y_HTM;
	}
	public BigDecimal getR39_O5Y_TOTAL() {
		return R39_O5Y_TOTAL;
	}
	public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
		R39_O5Y_TOTAL = r39_O5Y_TOTAL;
	}
	public BigDecimal getR39_T_FT() {
		return R39_T_FT;
	}
	public void setR39_T_FT(BigDecimal r39_T_FT) {
		R39_T_FT = r39_T_FT;
	}
	public BigDecimal getR39_T_HTM() {
		return R39_T_HTM;
	}
	public void setR39_T_HTM(BigDecimal r39_T_HTM) {
		R39_T_HTM = r39_T_HTM;
	}
	public BigDecimal getR39_T_TOTAL() {
		return R39_T_TOTAL;
	}
	public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
		R39_T_TOTAL = r39_T_TOTAL;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_0_1Y_FT() {
		return R40_0_1Y_FT;
	}
	public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
		R40_0_1Y_FT = r40_0_1y_FT;
	}
	public BigDecimal getR40_0_1Y_HTM() {
		return R40_0_1Y_HTM;
	}
	public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
		R40_0_1Y_HTM = r40_0_1y_HTM;
	}
	public BigDecimal getR40_0_1Y_TOTAL() {
		return R40_0_1Y_TOTAL;
	}
	public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
		R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
	}
	public BigDecimal getR40_1_5Y_FT() {
		return R40_1_5Y_FT;
	}
	public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
		R40_1_5Y_FT = r40_1_5y_FT;
	}
	public BigDecimal getR40_1_5Y_HTM() {
		return R40_1_5Y_HTM;
	}
	public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
		R40_1_5Y_HTM = r40_1_5y_HTM;
	}
	public BigDecimal getR40_1_5Y_TOTAL() {
		return R40_1_5Y_TOTAL;
	}
	public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
		R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
	}
	public BigDecimal getR40_O5Y_FT() {
		return R40_O5Y_FT;
	}
	public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
		R40_O5Y_FT = r40_O5Y_FT;
	}
	public BigDecimal getR40_O5Y_HTM() {
		return R40_O5Y_HTM;
	}
	public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
		R40_O5Y_HTM = r40_O5Y_HTM;
	}
	public BigDecimal getR40_O5Y_TOTAL() {
		return R40_O5Y_TOTAL;
	}
	public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
		R40_O5Y_TOTAL = r40_O5Y_TOTAL;
	}
	public BigDecimal getR40_T_FT() {
		return R40_T_FT;
	}
	public void setR40_T_FT(BigDecimal r40_T_FT) {
		R40_T_FT = r40_T_FT;
	}
	public BigDecimal getR40_T_HTM() {
		return R40_T_HTM;
	}
	public void setR40_T_HTM(BigDecimal r40_T_HTM) {
		R40_T_HTM = r40_T_HTM;
	}
	public BigDecimal getR40_T_TOTAL() {
		return R40_T_TOTAL;
	}
	public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
		R40_T_TOTAL = r40_T_TOTAL;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_0_1Y_FT() {
		return R41_0_1Y_FT;
	}
	public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
		R41_0_1Y_FT = r41_0_1y_FT;
	}
	public BigDecimal getR41_0_1Y_HTM() {
		return R41_0_1Y_HTM;
	}
	public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
		R41_0_1Y_HTM = r41_0_1y_HTM;
	}
	public BigDecimal getR41_0_1Y_TOTAL() {
		return R41_0_1Y_TOTAL;
	}
	public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
		R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
	}
	public BigDecimal getR41_1_5Y_FT() {
		return R41_1_5Y_FT;
	}
	public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
		R41_1_5Y_FT = r41_1_5y_FT;
	}
	public BigDecimal getR41_1_5Y_HTM() {
		return R41_1_5Y_HTM;
	}
	public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
		R41_1_5Y_HTM = r41_1_5y_HTM;
	}
	public BigDecimal getR41_1_5Y_TOTAL() {
		return R41_1_5Y_TOTAL;
	}
	public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
		R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
	}
	public BigDecimal getR41_O5Y_FT() {
		return R41_O5Y_FT;
	}
	public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
		R41_O5Y_FT = r41_O5Y_FT;
	}
	public BigDecimal getR41_O5Y_HTM() {
		return R41_O5Y_HTM;
	}
	public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
		R41_O5Y_HTM = r41_O5Y_HTM;
	}
	public BigDecimal getR41_O5Y_TOTAL() {
		return R41_O5Y_TOTAL;
	}
	public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
		R41_O5Y_TOTAL = r41_O5Y_TOTAL;
	}
	public BigDecimal getR41_T_FT() {
		return R41_T_FT;
	}
	public void setR41_T_FT(BigDecimal r41_T_FT) {
		R41_T_FT = r41_T_FT;
	}
	public BigDecimal getR41_T_HTM() {
		return R41_T_HTM;
	}
	public void setR41_T_HTM(BigDecimal r41_T_HTM) {
		R41_T_HTM = r41_T_HTM;
	}
	public BigDecimal getR41_T_TOTAL() {
		return R41_T_TOTAL;
	}
	public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
		R41_T_TOTAL = r41_T_TOTAL;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_0_1Y_FT() {
		return R42_0_1Y_FT;
	}
	public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
		R42_0_1Y_FT = r42_0_1y_FT;
	}
	public BigDecimal getR42_0_1Y_HTM() {
		return R42_0_1Y_HTM;
	}
	public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
		R42_0_1Y_HTM = r42_0_1y_HTM;
	}
	public BigDecimal getR42_0_1Y_TOTAL() {
		return R42_0_1Y_TOTAL;
	}
	public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
		R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
	}
	public BigDecimal getR42_1_5Y_FT() {
		return R42_1_5Y_FT;
	}
	public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
		R42_1_5Y_FT = r42_1_5y_FT;
	}
	public BigDecimal getR42_1_5Y_HTM() {
		return R42_1_5Y_HTM;
	}
	public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
		R42_1_5Y_HTM = r42_1_5y_HTM;
	}
	public BigDecimal getR42_1_5Y_TOTAL() {
		return R42_1_5Y_TOTAL;
	}
	public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
		R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
	}
	public BigDecimal getR42_O5Y_FT() {
		return R42_O5Y_FT;
	}
	public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
		R42_O5Y_FT = r42_O5Y_FT;
	}
	public BigDecimal getR42_O5Y_HTM() {
		return R42_O5Y_HTM;
	}
	public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
		R42_O5Y_HTM = r42_O5Y_HTM;
	}
	public BigDecimal getR42_O5Y_TOTAL() {
		return R42_O5Y_TOTAL;
	}
	public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
		R42_O5Y_TOTAL = r42_O5Y_TOTAL;
	}
	public BigDecimal getR42_T_FT() {
		return R42_T_FT;
	}
	public void setR42_T_FT(BigDecimal r42_T_FT) {
		R42_T_FT = r42_T_FT;
	}
	public BigDecimal getR42_T_HTM() {
		return R42_T_HTM;
	}
	public void setR42_T_HTM(BigDecimal r42_T_HTM) {
		R42_T_HTM = r42_T_HTM;
	}
	public BigDecimal getR42_T_TOTAL() {
		return R42_T_TOTAL;
	}
	public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
		R42_T_TOTAL = r42_T_TOTAL;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_0_1Y_FT() {
		return R43_0_1Y_FT;
	}
	public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
		R43_0_1Y_FT = r43_0_1y_FT;
	}
	public BigDecimal getR43_0_1Y_HTM() {
		return R43_0_1Y_HTM;
	}
	public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
		R43_0_1Y_HTM = r43_0_1y_HTM;
	}
	public BigDecimal getR43_0_1Y_TOTAL() {
		return R43_0_1Y_TOTAL;
	}
	public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
		R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
	}
	public BigDecimal getR43_1_5Y_FT() {
		return R43_1_5Y_FT;
	}
	public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
		R43_1_5Y_FT = r43_1_5y_FT;
	}
	public BigDecimal getR43_1_5Y_HTM() {
		return R43_1_5Y_HTM;
	}
	public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
		R43_1_5Y_HTM = r43_1_5y_HTM;
	}
	public BigDecimal getR43_1_5Y_TOTAL() {
		return R43_1_5Y_TOTAL;
	}
	public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
		R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
	}
	public BigDecimal getR43_O5Y_FT() {
		return R43_O5Y_FT;
	}
	public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
		R43_O5Y_FT = r43_O5Y_FT;
	}
	public BigDecimal getR43_O5Y_HTM() {
		return R43_O5Y_HTM;
	}
	public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
		R43_O5Y_HTM = r43_O5Y_HTM;
	}
	public BigDecimal getR43_O5Y_TOTAL() {
		return R43_O5Y_TOTAL;
	}
	public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
		R43_O5Y_TOTAL = r43_O5Y_TOTAL;
	}
	public BigDecimal getR43_T_FT() {
		return R43_T_FT;
	}
	public void setR43_T_FT(BigDecimal r43_T_FT) {
		R43_T_FT = r43_T_FT;
	}
	public BigDecimal getR43_T_HTM() {
		return R43_T_HTM;
	}
	public void setR43_T_HTM(BigDecimal r43_T_HTM) {
		R43_T_HTM = r43_T_HTM;
	}
	public BigDecimal getR43_T_TOTAL() {
		return R43_T_TOTAL;
	}
	public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
		R43_T_TOTAL = r43_T_TOTAL;
	}
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
	
	
	

	
	

}