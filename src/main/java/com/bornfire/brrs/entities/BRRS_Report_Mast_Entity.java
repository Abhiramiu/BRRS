package com.bornfire.brrs.entities;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "BRRS_REPORT_MAST")
public class BRRS_Report_Mast_Entity {
	@Id
	private BigDecimal	srl_no;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	rpt_start_date;
	private String	rpt_code;
	private String	rpt_description;
	private String	rpt_type;
	private String	det_flg;
	private String	rpt_status;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	start_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	end_date;
	private String	remarks_1;
	private String	remarks_2;
	private String	remarks_3;
	private String	remarks_4;
	private String	remarks_5;
	private String	del_flg;
	private String	entity_flg;
	private String	modify_flg;
	private String	entry_user;
	private String	modify_user;
	private String	verify_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	entry_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	modify_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	verify_date;
	private String	taxonomy_version;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	taxonomy_date;
	private String	domain;
	private String	domain_id;
	private String	report_validity;
	private String	report_frequency;
	private String	lchg_user_id;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	lchg_time;
	public BigDecimal getSrl_no() {
		return srl_no;
	}
	public void setSrl_no(BigDecimal srl_no) {
		this.srl_no = srl_no;
	}
	public Date getRpt_start_date() {
		return rpt_start_date;
	}
	public void setRpt_start_date(Date rpt_start_date) {
		this.rpt_start_date = rpt_start_date;
	}
	public String getRpt_code() {
		return rpt_code;
	}
	public void setRpt_code(String rpt_code) {
		this.rpt_code = rpt_code;
	}
	public String getRpt_description() {
		return rpt_description;
	}
	public void setRpt_description(String rpt_description) {
		this.rpt_description = rpt_description;
	}
	public String getRpt_type() {
		return rpt_type;
	}
	public void setRpt_type(String rpt_type) {
		this.rpt_type = rpt_type;
	}
	public String getDet_flg() {
		return det_flg;
	}
	public void setDet_flg(String det_flg) {
		this.det_flg = det_flg;
	}
	public String getRpt_status() {
		return rpt_status;
	}
	public void setRpt_status(String rpt_status) {
		this.rpt_status = rpt_status;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getRemarks_1() {
		return remarks_1;
	}
	public void setRemarks_1(String remarks_1) {
		this.remarks_1 = remarks_1;
	}
	public String getRemarks_2() {
		return remarks_2;
	}
	public void setRemarks_2(String remarks_2) {
		this.remarks_2 = remarks_2;
	}
	public String getRemarks_3() {
		return remarks_3;
	}
	public void setRemarks_3(String remarks_3) {
		this.remarks_3 = remarks_3;
	}
	public String getRemarks_4() {
		return remarks_4;
	}
	public void setRemarks_4(String remarks_4) {
		this.remarks_4 = remarks_4;
	}
	public String getRemarks_5() {
		return remarks_5;
	}
	public void setRemarks_5(String remarks_5) {
		this.remarks_5 = remarks_5;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
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
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public Date getEntry_date() {
		return entry_date;
	}
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	public Date getVerify_date() {
		return verify_date;
	}
	public void setVerify_date(Date verify_date) {
		this.verify_date = verify_date;
	}
	public String getTaxonomy_version() {
		return taxonomy_version;
	}
	public void setTaxonomy_version(String taxonomy_version) {
		this.taxonomy_version = taxonomy_version;
	}
	public Date getTaxonomy_date() {
		return taxonomy_date;
	}
	public void setTaxonomy_date(Date taxonomy_date) {
		this.taxonomy_date = taxonomy_date;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDomain_id() {
		return domain_id;
	}
	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}
	public String getReport_validity() {
		return report_validity;
	}
	public void setReport_validity(String report_validity) {
		this.report_validity = report_validity;
	}
	public String getReport_frequency() {
		return report_frequency;
	}
	public void setReport_frequency(String report_frequency) {
		this.report_frequency = report_frequency;
	}
	public String getLchg_user_id() {
		return lchg_user_id;
	}
	public void setLchg_user_id(String lchg_user_id) {
		this.lchg_user_id = lchg_user_id;
	}
	public Date getLchg_time() {
		return lchg_time;
	}
	public void setLchg_time(Date lchg_time) {
		this.lchg_time = lchg_time;
	}

}
