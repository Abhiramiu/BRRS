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
@Table(name = "BRRS_M_SEC_ARCHIVALTABLE_DETAIL3") 

public class M_SEC_Archival_Detail3_Entity {
        private String R26_PRODUCT;
	    private BigDecimal R26_0_1Y_FT;
	    private BigDecimal R26_0_1Y_HTM;
	    private BigDecimal R26_0_1Y_TOTAL;
	    private BigDecimal R26_1_5Y_FT;
	    private BigDecimal R26_1_5Y_HTM;
	    private BigDecimal R26_1_5Y_TOTAL;
	    private BigDecimal R26_O5Y_FT;
	    private BigDecimal R26_O5Y_HTM;
	    private BigDecimal R26_O5Y_TOTAL;
	    private BigDecimal R26_T_FT;
	    private BigDecimal R26_T_HTM;
	    private BigDecimal R26_T_TOTAL;

	    // === R27 ===
	    private String R27_PRODUCT;
	    private BigDecimal R27_0_1Y_FT;
	    private BigDecimal R27_0_1Y_HTM;
	    private BigDecimal R27_0_1Y_TOTAL;
	    private BigDecimal R27_1_5Y_FT;
	    private BigDecimal R27_1_5Y_HTM;
	    private BigDecimal R27_1_5Y_TOTAL;
	    private BigDecimal R27_O5Y_FT;
	    private BigDecimal R27_O5Y_HTM;
	    private BigDecimal R27_O5Y_TOTAL;
	    private BigDecimal R27_T_FT;
	    private BigDecimal R27_T_HTM;
	    private BigDecimal R27_T_TOTAL;

	    // === R28 ===
	    private String R28_PRODUCT;
	    private BigDecimal R28_0_1Y_FT;
	    private BigDecimal R28_0_1Y_HTM;
	    private BigDecimal R28_0_1Y_TOTAL;
	    private BigDecimal R28_1_5Y_FT;
	    private BigDecimal R28_1_5Y_HTM;
	    private BigDecimal R28_1_5Y_TOTAL;
	    private BigDecimal R28_O5Y_FT;
	    private BigDecimal R28_O5Y_HTM;
	    private BigDecimal R28_O5Y_TOTAL;
	    private BigDecimal R28_T_FT;
	    private BigDecimal R28_T_HTM;
	    private BigDecimal R28_T_TOTAL;

	    
	    // === R29 ===
	    private String R29_PRODUCT;
	    private BigDecimal R29_0_1Y_FT;
	    private BigDecimal R29_0_1Y_HTM;
	    private BigDecimal R29_0_1Y_TOTAL;
	    
	    private BigDecimal R29_1_5Y_FT;
	    private BigDecimal R29_1_5Y_HTM;
	    private BigDecimal R29_1_5Y_TOTAL;
	    private BigDecimal R29_O5Y_FT;
	    private BigDecimal R29_O5Y_HTM;
	    private BigDecimal R29_O5Y_TOTAL;
	    private BigDecimal R29_T_FT;
	    private BigDecimal R29_T_HTM;
	    private BigDecimal R29_T_TOTAL;

	    // === R30 ===
	    private String R30_PRODUCT;
	    private BigDecimal R30_0_1Y_FT;
	    private BigDecimal R30_0_1Y_HTM;
	    private BigDecimal R30_0_1Y_TOTAL;
	    private BigDecimal R30_1_5Y_FT;
	    private BigDecimal R30_1_5Y_HTM;
	    private BigDecimal R30_1_5Y_TOTAL;
	    private BigDecimal R30_O5Y_FT;
	    private BigDecimal R30_O5Y_HTM;
	    private BigDecimal R30_O5Y_TOTAL;
	    private BigDecimal R30_T_FT;
	    private BigDecimal R30_T_HTM;
	    private BigDecimal R30_T_TOTAL;

	    // === R31 ===
	    private String R31_PRODUCT;
	    private BigDecimal R31_0_1Y_FT;
	    private BigDecimal R31_0_1Y_HTM;
	    private BigDecimal R31_0_1Y_TOTAL;
	    private BigDecimal R31_1_5Y_FT;
	    private BigDecimal R31_1_5Y_HTM;
	    private BigDecimal R31_1_5Y_TOTAL;
	    private BigDecimal R31_O5Y_FT;
	    private BigDecimal R31_O5Y_HTM;
	    private BigDecimal R31_O5Y_TOTAL;
	    private BigDecimal R31_T_FT;
	    private BigDecimal R31_T_HTM;
	    private BigDecimal R31_T_TOTAL;
	    
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
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}
		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}
		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}
		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}
		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}
		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}
		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}
		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}
		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}
		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}
		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}
		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}
		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}
		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}
		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}
		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}
		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}
		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}
		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}
		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}
		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}
		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}
		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}
		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}
		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}
		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}
		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}
		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}
		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}
		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}
		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}
		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}
		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}
		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}
		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}
		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}
		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}
		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}
		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}
		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}
		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}
		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}
		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}
		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}
		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}
		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}
		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}
		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}
		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}
		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}
		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}
		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}
		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}
		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}
		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}
		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}
		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}
		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}
		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}
		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}
		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}
		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}
		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}
		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}
		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}
		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}
		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}
		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}
		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}
		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}
		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}
		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}
		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}
		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}
		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}
		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}
		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}
		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}
		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}
		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}
		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}
		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}
		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}
		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}
		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}
		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}
		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}
		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}
		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}
		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}
		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}
		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}
		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}
		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}
		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}
		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}
		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}
		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}
		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}
		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}
		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}
		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}
		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}
		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}
		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}
		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}
		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}
		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}
		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}
		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}
		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}
		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}
		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}
		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}
		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}
		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}
		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}
		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}
		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}
		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}
		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}
		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}
		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}
		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}
		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}
		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}
		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}
		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}
		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}
		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}
		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}
		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}
		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}
		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}
		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}
		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}
		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}
		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}
		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}
		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}
		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}
		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}
		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}
		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
		}
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
		public M_SEC_Archival_Detail3_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		

	   
}
