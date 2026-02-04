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
@Table(name = "BRRS_M_CA7_ARCHIVALTABLE_SUMMARY")

@IdClass(M_CA7_Archival_Summary_PK.class)
public class M_CA7_Archival_Summary_Entity {
	
	    private BigDecimal r12_pre_ifrs_pro;   

	    private BigDecimal r12_post_ifrs9_pro;
	    private BigDecimal r12_trans_amt;

	    private String r19_product;
	    private BigDecimal r19_cap_year1;
	    private BigDecimal r19_amt_add_year1;

	    private String r20_product;
	    private BigDecimal r20_cap_year2;
	    private BigDecimal r20_amt_add_year2;

	    private String r21_product;
	    private BigDecimal r21_cap_year3;
	    private BigDecimal r21_amt_add_year3;

	    private String r22_product;
	    private BigDecimal r22_cap_year4;
	    private BigDecimal r22_amt_add_year4;

		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Column(name = "REPORT_DATE")
		private Date reportDate;
		
		@Id
		@Column(name = "REPORT_VERSION")
		private BigDecimal reportVersion;
		
	    @Column(name = "REPORT_RESUBDATE")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date reportResubDate;	
		
	    private String report_frequency;
	    private String report_code;
	    private String report_desc;

	    private String entity_flg;
	    private String modify_flg;
	    private String del_flg;
		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}
		public void setR12_pre_ifrs_pro(BigDecimal r12_pre_ifrs_pro) {
			this.r12_pre_ifrs_pro = r12_pre_ifrs_pro;
		}
		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}
		public void setR12_post_ifrs9_pro(BigDecimal r12_post_ifrs9_pro) {
			this.r12_post_ifrs9_pro = r12_post_ifrs9_pro;
		}
		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}
		public void setR12_trans_amt(BigDecimal r12_trans_amt) {
			this.r12_trans_amt = r12_trans_amt;
		}
		public String getR19_product() {
			return r19_product;
		}
		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}
		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}
		public void setR19_cap_year1(BigDecimal r19_cap_year1) {
			this.r19_cap_year1 = r19_cap_year1;
		}
		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}
		public void setR19_amt_add_year1(BigDecimal r19_amt_add_year1) {
			this.r19_amt_add_year1 = r19_amt_add_year1;
		}
		public String getR20_product() {
			return r20_product;
		}
		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}
		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}
		public void setR20_cap_year2(BigDecimal r20_cap_year2) {
			this.r20_cap_year2 = r20_cap_year2;
		}
		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}
		public void setR20_amt_add_year2(BigDecimal r20_amt_add_year2) {
			this.r20_amt_add_year2 = r20_amt_add_year2;
		}
		public String getR21_product() {
			return r21_product;
		}
		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}
		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}
		public void setR21_cap_year3(BigDecimal r21_cap_year3) {
			this.r21_cap_year3 = r21_cap_year3;
		}
		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}
		public void setR21_amt_add_year3(BigDecimal r21_amt_add_year3) {
			this.r21_amt_add_year3 = r21_amt_add_year3;
		}
		public String getR22_product() {
			return r22_product;
		}
		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}
		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}
		public void setR22_cap_year4(BigDecimal r22_cap_year4) {
			this.r22_cap_year4 = r22_cap_year4;
		}
		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}
		public void setR22_amt_add_year4(BigDecimal r22_amt_add_year4) {
			this.r22_amt_add_year4 = r22_amt_add_year4;
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
		public M_CA7_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

}
