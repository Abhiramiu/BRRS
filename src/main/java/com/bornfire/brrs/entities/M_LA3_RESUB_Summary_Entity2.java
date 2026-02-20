package com.bornfire.brrs.entities;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
@Entity
@Table(name = "BRRS_M_LA3_RESUB_SUMMARYTABLE2")
@IdClass(M_LA3_PK.class)

public class M_LA3_RESUB_Summary_Entity2 {
	
	
	// ====== R36 ======
		private String R36_PRODUCT;
		private BigDecimal R36_NO_OF_AC;
		private BigDecimal R36_CREDIT_LIMIT;
		private BigDecimal R36_AMOUNT_OUTSTANDING;

		// ====== R37 ======
		private String R37_PRODUCT;
		private BigDecimal R37_NO_OF_AC;
		private BigDecimal R37_CREDIT_LIMIT;
		private BigDecimal R37_AMOUNT_OUTSTANDING;

		// ====== R38 ======
		private String R38_PRODUCT;
		private BigDecimal R38_NO_OF_AC;
		private BigDecimal R38_CREDIT_LIMIT;
		private BigDecimal R38_AMOUNT_OUTSTANDING;

		// ====== R39 ======
		private String R39_PRODUCT;
		private BigDecimal R39_NO_OF_AC;
		private BigDecimal R39_CREDIT_LIMIT;
		private BigDecimal R39_AMOUNT_OUTSTANDING;

		// ====== R40 ======
		private String R40_PRODUCT;
		private BigDecimal R40_NO_OF_AC;
		private BigDecimal R40_CREDIT_LIMIT;
		private BigDecimal R40_AMOUNT_OUTSTANDING;

		// ====== R41 ======
		private String R41_PRODUCT;
		private BigDecimal R41_NO_OF_AC;
		private BigDecimal R41_CREDIT_LIMIT;
		private BigDecimal R41_AMOUNT_OUTSTANDING;

		// ====== R42 ======
		private String R42_PRODUCT;
		private BigDecimal R42_NO_OF_AC;
		private BigDecimal R42_CREDIT_LIMIT;
		private BigDecimal R42_AMOUNT_OUTSTANDING;

		 @Id
		    @Temporal(TemporalType.DATE)
		    @DateTimeFormat(pattern = "dd/MM/yyyy")
			@Column(name = "REPORT_DATE")
		    private Date reportDate;
			
			@Id
			@Column(name = "REPORT_VERSION")
			private BigDecimal reportVersion;
			
		    @Column(name = "REPORT_RESUBDATE")
		    private Date reportResubDate;
			private String REPORT_FREQUENCY;
			private String REPORT_CODE;
			private String REPORT_DESC;
			private String ENTITY_FLG;
			private String MODIFY_FLG;
			private String DEL_FLG;
			
			
			
			public String getR36_PRODUCT() {
				return R36_PRODUCT;
			}



			public void setR36_PRODUCT(String r36_PRODUCT) {
				R36_PRODUCT = r36_PRODUCT;
			}



			public BigDecimal getR36_NO_OF_AC() {
				return R36_NO_OF_AC;
			}



			public void setR36_NO_OF_AC(BigDecimal r36_NO_OF_AC) {
				R36_NO_OF_AC = r36_NO_OF_AC;
			}



			public BigDecimal getR36_CREDIT_LIMIT() {
				return R36_CREDIT_LIMIT;
			}



			public void setR36_CREDIT_LIMIT(BigDecimal r36_CREDIT_LIMIT) {
				R36_CREDIT_LIMIT = r36_CREDIT_LIMIT;
			}



			public BigDecimal getR36_AMOUNT_OUTSTANDING() {
				return R36_AMOUNT_OUTSTANDING;
			}



			public void setR36_AMOUNT_OUTSTANDING(BigDecimal r36_AMOUNT_OUTSTANDING) {
				R36_AMOUNT_OUTSTANDING = r36_AMOUNT_OUTSTANDING;
			}



			public String getR37_PRODUCT() {
				return R37_PRODUCT;
			}



			public void setR37_PRODUCT(String r37_PRODUCT) {
				R37_PRODUCT = r37_PRODUCT;
			}



			public BigDecimal getR37_NO_OF_AC() {
				return R37_NO_OF_AC;
			}



			public void setR37_NO_OF_AC(BigDecimal r37_NO_OF_AC) {
				R37_NO_OF_AC = r37_NO_OF_AC;
			}



			public BigDecimal getR37_CREDIT_LIMIT() {
				return R37_CREDIT_LIMIT;
			}



			public void setR37_CREDIT_LIMIT(BigDecimal r37_CREDIT_LIMIT) {
				R37_CREDIT_LIMIT = r37_CREDIT_LIMIT;
			}



			public BigDecimal getR37_AMOUNT_OUTSTANDING() {
				return R37_AMOUNT_OUTSTANDING;
			}



			public void setR37_AMOUNT_OUTSTANDING(BigDecimal r37_AMOUNT_OUTSTANDING) {
				R37_AMOUNT_OUTSTANDING = r37_AMOUNT_OUTSTANDING;
			}



			public String getR38_PRODUCT() {
				return R38_PRODUCT;
			}



			public void setR38_PRODUCT(String r38_PRODUCT) {
				R38_PRODUCT = r38_PRODUCT;
			}



			public BigDecimal getR38_NO_OF_AC() {
				return R38_NO_OF_AC;
			}



			public void setR38_NO_OF_AC(BigDecimal r38_NO_OF_AC) {
				R38_NO_OF_AC = r38_NO_OF_AC;
			}



			public BigDecimal getR38_CREDIT_LIMIT() {
				return R38_CREDIT_LIMIT;
			}



			public void setR38_CREDIT_LIMIT(BigDecimal r38_CREDIT_LIMIT) {
				R38_CREDIT_LIMIT = r38_CREDIT_LIMIT;
			}



			public BigDecimal getR38_AMOUNT_OUTSTANDING() {
				return R38_AMOUNT_OUTSTANDING;
			}



			public void setR38_AMOUNT_OUTSTANDING(BigDecimal r38_AMOUNT_OUTSTANDING) {
				R38_AMOUNT_OUTSTANDING = r38_AMOUNT_OUTSTANDING;
			}



			public String getR39_PRODUCT() {
				return R39_PRODUCT;
			}



			public void setR39_PRODUCT(String r39_PRODUCT) {
				R39_PRODUCT = r39_PRODUCT;
			}



			public BigDecimal getR39_NO_OF_AC() {
				return R39_NO_OF_AC;
			}



			public void setR39_NO_OF_AC(BigDecimal r39_NO_OF_AC) {
				R39_NO_OF_AC = r39_NO_OF_AC;
			}



			public BigDecimal getR39_CREDIT_LIMIT() {
				return R39_CREDIT_LIMIT;
			}



			public void setR39_CREDIT_LIMIT(BigDecimal r39_CREDIT_LIMIT) {
				R39_CREDIT_LIMIT = r39_CREDIT_LIMIT;
			}



			public BigDecimal getR39_AMOUNT_OUTSTANDING() {
				return R39_AMOUNT_OUTSTANDING;
			}



			public void setR39_AMOUNT_OUTSTANDING(BigDecimal r39_AMOUNT_OUTSTANDING) {
				R39_AMOUNT_OUTSTANDING = r39_AMOUNT_OUTSTANDING;
			}



			public String getR40_PRODUCT() {
				return R40_PRODUCT;
			}



			public void setR40_PRODUCT(String r40_PRODUCT) {
				R40_PRODUCT = r40_PRODUCT;
			}



			public BigDecimal getR40_NO_OF_AC() {
				return R40_NO_OF_AC;
			}



			public void setR40_NO_OF_AC(BigDecimal r40_NO_OF_AC) {
				R40_NO_OF_AC = r40_NO_OF_AC;
			}



			public BigDecimal getR40_CREDIT_LIMIT() {
				return R40_CREDIT_LIMIT;
			}



			public void setR40_CREDIT_LIMIT(BigDecimal r40_CREDIT_LIMIT) {
				R40_CREDIT_LIMIT = r40_CREDIT_LIMIT;
			}



			public BigDecimal getR40_AMOUNT_OUTSTANDING() {
				return R40_AMOUNT_OUTSTANDING;
			}



			public void setR40_AMOUNT_OUTSTANDING(BigDecimal r40_AMOUNT_OUTSTANDING) {
				R40_AMOUNT_OUTSTANDING = r40_AMOUNT_OUTSTANDING;
			}



			public String getR41_PRODUCT() {
				return R41_PRODUCT;
			}



			public void setR41_PRODUCT(String r41_PRODUCT) {
				R41_PRODUCT = r41_PRODUCT;
			}



			public BigDecimal getR41_NO_OF_AC() {
				return R41_NO_OF_AC;
			}



			public void setR41_NO_OF_AC(BigDecimal r41_NO_OF_AC) {
				R41_NO_OF_AC = r41_NO_OF_AC;
			}



			public BigDecimal getR41_CREDIT_LIMIT() {
				return R41_CREDIT_LIMIT;
			}



			public void setR41_CREDIT_LIMIT(BigDecimal r41_CREDIT_LIMIT) {
				R41_CREDIT_LIMIT = r41_CREDIT_LIMIT;
			}



			public BigDecimal getR41_AMOUNT_OUTSTANDING() {
				return R41_AMOUNT_OUTSTANDING;
			}



			public void setR41_AMOUNT_OUTSTANDING(BigDecimal r41_AMOUNT_OUTSTANDING) {
				R41_AMOUNT_OUTSTANDING = r41_AMOUNT_OUTSTANDING;
			}



			public String getR42_PRODUCT() {
				return R42_PRODUCT;
			}



			public void setR42_PRODUCT(String r42_PRODUCT) {
				R42_PRODUCT = r42_PRODUCT;
			}



			public BigDecimal getR42_NO_OF_AC() {
				return R42_NO_OF_AC;
			}



			public void setR42_NO_OF_AC(BigDecimal r42_NO_OF_AC) {
				R42_NO_OF_AC = r42_NO_OF_AC;
			}



			public BigDecimal getR42_CREDIT_LIMIT() {
				return R42_CREDIT_LIMIT;
			}



			public void setR42_CREDIT_LIMIT(BigDecimal r42_CREDIT_LIMIT) {
				R42_CREDIT_LIMIT = r42_CREDIT_LIMIT;
			}



			public BigDecimal getR42_AMOUNT_OUTSTANDING() {
				return R42_AMOUNT_OUTSTANDING;
			}



			public void setR42_AMOUNT_OUTSTANDING(BigDecimal r42_AMOUNT_OUTSTANDING) {
				R42_AMOUNT_OUTSTANDING = r42_AMOUNT_OUTSTANDING;
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



			public Date getReportResubDate() {
				return reportResubDate;
			}



			public void setReportResubDate(Date reportResubDate) {
				this.reportResubDate = reportResubDate;
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



			public M_LA3_RESUB_Summary_Entity2() {
				super();
				// TODO Auto-generated constructor stub
			}
			

	}



