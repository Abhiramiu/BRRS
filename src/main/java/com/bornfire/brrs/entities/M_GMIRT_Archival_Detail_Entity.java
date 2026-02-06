package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_GMIRT_ARCHIVALTABLE_DETAIL")

public class M_GMIRT_Archival_Detail_Entity {

	private String r9_currency;
	private BigDecimal r9_pula;
	private BigDecimal r9_usd;
	private BigDecimal r9_zar;
	private BigDecimal r9_gbp;
	private BigDecimal r9_euro;
	private BigDecimal r9_jpy;
	private BigDecimal r9_rupee;
	private BigDecimal r9_renminbi;
	private BigDecimal r9_other;
	private BigDecimal r9_tot_cap_req;

	private String r10_currency;
	private BigDecimal r10_pula;
	private BigDecimal r10_usd;
	private BigDecimal r10_zar;
	private BigDecimal r10_gbp;
	private BigDecimal r10_euro;
	private BigDecimal r10_jpy;
	private BigDecimal r10_rupee;
	private BigDecimal r10_renminbi;
	private BigDecimal r10_other;
	private BigDecimal r10_tot_cap_req;

	private String r11_currency;
	private BigDecimal r11_pula;
	private BigDecimal r11_usd;
	private BigDecimal r11_zar;
	private BigDecimal r11_gbp;
	private BigDecimal r11_euro;
	private BigDecimal r11_jpy;
	private BigDecimal r11_rupee;
	private BigDecimal r11_renminbi;
	private BigDecimal r11_other;
	private BigDecimal r11_tot_cap_req;

	private String r12_currency;
	private BigDecimal r12_pula;
	private BigDecimal r12_usd;
	private BigDecimal r12_zar;
	private BigDecimal r12_gbp;
	private BigDecimal r12_euro;
	private BigDecimal r12_jpy;
	private BigDecimal r12_rupee;
	private BigDecimal r12_renminbi;
	private BigDecimal r12_other;
	private BigDecimal r12_tot_cap_req;

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

	public String getR9_currency() {
		return r9_currency;
	}

	public void setR9_currency(String r9_currency) {
		this.r9_currency = r9_currency;
	}

	public BigDecimal getR9_pula() {
		return r9_pula;
	}

	public void setR9_pula(BigDecimal r9_pula) {
		this.r9_pula = r9_pula;
	}

	public BigDecimal getR9_usd() {
		return r9_usd;
	}

	public void setR9_usd(BigDecimal r9_usd) {
		this.r9_usd = r9_usd;
	}

	public BigDecimal getR9_zar() {
		return r9_zar;
	}

	public void setR9_zar(BigDecimal r9_zar) {
		this.r9_zar = r9_zar;
	}

	public BigDecimal getR9_gbp() {
		return r9_gbp;
	}

	public void setR9_gbp(BigDecimal r9_gbp) {
		this.r9_gbp = r9_gbp;
	}

	public BigDecimal getR9_euro() {
		return r9_euro;
	}

	public void setR9_euro(BigDecimal r9_euro) {
		this.r9_euro = r9_euro;
	}

	public BigDecimal getR9_jpy() {
		return r9_jpy;
	}

	public void setR9_jpy(BigDecimal r9_jpy) {
		this.r9_jpy = r9_jpy;
	}

	public BigDecimal getR9_rupee() {
		return r9_rupee;
	}

	public void setR9_rupee(BigDecimal r9_rupee) {
		this.r9_rupee = r9_rupee;
	}

	public BigDecimal getR9_renminbi() {
		return r9_renminbi;
	}

	public void setR9_renminbi(BigDecimal r9_renminbi) {
		this.r9_renminbi = r9_renminbi;
	}

	public BigDecimal getR9_other() {
		return r9_other;
	}

	public void setR9_other(BigDecimal r9_other) {
		this.r9_other = r9_other;
	}

	public BigDecimal getR9_tot_cap_req() {
		return r9_tot_cap_req;
	}

	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) {
		this.r9_tot_cap_req = r9_tot_cap_req;
	}

	public String getR10_currency() {
		return r10_currency;
	}

	public void setR10_currency(String r10_currency) {
		this.r10_currency = r10_currency;
	}

	public BigDecimal getR10_pula() {
		return r10_pula;
	}

	public void setR10_pula(BigDecimal r10_pula) {
		this.r10_pula = r10_pula;
	}

	public BigDecimal getR10_usd() {
		return r10_usd;
	}

	public void setR10_usd(BigDecimal r10_usd) {
		this.r10_usd = r10_usd;
	}

	public BigDecimal getR10_zar() {
		return r10_zar;
	}

	public void setR10_zar(BigDecimal r10_zar) {
		this.r10_zar = r10_zar;
	}

	public BigDecimal getR10_gbp() {
		return r10_gbp;
	}

	public void setR10_gbp(BigDecimal r10_gbp) {
		this.r10_gbp = r10_gbp;
	}

	public BigDecimal getR10_euro() {
		return r10_euro;
	}

	public void setR10_euro(BigDecimal r10_euro) {
		this.r10_euro = r10_euro;
	}

	public BigDecimal getR10_jpy() {
		return r10_jpy;
	}

	public void setR10_jpy(BigDecimal r10_jpy) {
		this.r10_jpy = r10_jpy;
	}

	public BigDecimal getR10_rupee() {
		return r10_rupee;
	}

	public void setR10_rupee(BigDecimal r10_rupee) {
		this.r10_rupee = r10_rupee;
	}

	public BigDecimal getR10_renminbi() {
		return r10_renminbi;
	}

	public void setR10_renminbi(BigDecimal r10_renminbi) {
		this.r10_renminbi = r10_renminbi;
	}

	public BigDecimal getR10_other() {
		return r10_other;
	}

	public void setR10_other(BigDecimal r10_other) {
		this.r10_other = r10_other;
	}

	public BigDecimal getR10_tot_cap_req() {
		return r10_tot_cap_req;
	}

	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) {
		this.r10_tot_cap_req = r10_tot_cap_req;
	}

	public String getR11_currency() {
		return r11_currency;
	}

	public void setR11_currency(String r11_currency) {
		this.r11_currency = r11_currency;
	}

	public BigDecimal getR11_pula() {
		return r11_pula;
	}

	public void setR11_pula(BigDecimal r11_pula) {
		this.r11_pula = r11_pula;
	}

	public BigDecimal getR11_usd() {
		return r11_usd;
	}

	public void setR11_usd(BigDecimal r11_usd) {
		this.r11_usd = r11_usd;
	}

	public BigDecimal getR11_zar() {
		return r11_zar;
	}

	public void setR11_zar(BigDecimal r11_zar) {
		this.r11_zar = r11_zar;
	}

	public BigDecimal getR11_gbp() {
		return r11_gbp;
	}

	public void setR11_gbp(BigDecimal r11_gbp) {
		this.r11_gbp = r11_gbp;
	}

	public BigDecimal getR11_euro() {
		return r11_euro;
	}

	public void setR11_euro(BigDecimal r11_euro) {
		this.r11_euro = r11_euro;
	}

	public BigDecimal getR11_jpy() {
		return r11_jpy;
	}

	public void setR11_jpy(BigDecimal r11_jpy) {
		this.r11_jpy = r11_jpy;
	}

	public BigDecimal getR11_rupee() {
		return r11_rupee;
	}

	public void setR11_rupee(BigDecimal r11_rupee) {
		this.r11_rupee = r11_rupee;
	}

	public BigDecimal getR11_renminbi() {
		return r11_renminbi;
	}

	public void setR11_renminbi(BigDecimal r11_renminbi) {
		this.r11_renminbi = r11_renminbi;
	}

	public BigDecimal getR11_other() {
		return r11_other;
	}

	public void setR11_other(BigDecimal r11_other) {
		this.r11_other = r11_other;
	}

	public BigDecimal getR11_tot_cap_req() {
		return r11_tot_cap_req;
	}

	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) {
		this.r11_tot_cap_req = r11_tot_cap_req;
	}

	public String getR12_currency() {
		return r12_currency;
	}

	public void setR12_currency(String r12_currency) {
		this.r12_currency = r12_currency;
	}

	public BigDecimal getR12_pula() {
		return r12_pula;
	}

	public void setR12_pula(BigDecimal r12_pula) {
		this.r12_pula = r12_pula;
	}

	public BigDecimal getR12_usd() {
		return r12_usd;
	}

	public void setR12_usd(BigDecimal r12_usd) {
		this.r12_usd = r12_usd;
	}

	public BigDecimal getR12_zar() {
		return r12_zar;
	}

	public void setR12_zar(BigDecimal r12_zar) {
		this.r12_zar = r12_zar;
	}

	public BigDecimal getR12_gbp() {
		return r12_gbp;
	}

	public void setR12_gbp(BigDecimal r12_gbp) {
		this.r12_gbp = r12_gbp;
	}

	public BigDecimal getR12_euro() {
		return r12_euro;
	}

	public void setR12_euro(BigDecimal r12_euro) {
		this.r12_euro = r12_euro;
	}

	public BigDecimal getR12_jpy() {
		return r12_jpy;
	}

	public void setR12_jpy(BigDecimal r12_jpy) {
		this.r12_jpy = r12_jpy;
	}

	public BigDecimal getR12_rupee() {
		return r12_rupee;
	}

	public void setR12_rupee(BigDecimal r12_rupee) {
		this.r12_rupee = r12_rupee;
	}

	public BigDecimal getR12_renminbi() {
		return r12_renminbi;
	}

	public void setR12_renminbi(BigDecimal r12_renminbi) {
		this.r12_renminbi = r12_renminbi;
	}

	public BigDecimal getR12_other() {
		return r12_other;
	}

	public void setR12_other(BigDecimal r12_other) {
		this.r12_other = r12_other;
	}

	public BigDecimal getR12_tot_cap_req() {
		return r12_tot_cap_req;
	}

	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) {
		this.r12_tot_cap_req = r12_tot_cap_req;
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

	public M_GMIRT_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}