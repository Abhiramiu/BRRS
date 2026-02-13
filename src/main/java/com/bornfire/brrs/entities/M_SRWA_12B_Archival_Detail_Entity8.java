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
@Table(name = "BRRS_M_SRWA_12B_ARCHIVALTABLE_DETAIL8")
@IdClass(M_SRWA_12B_Archival_Summary1_PK.class)
public class M_SRWA_12B_Archival_Detail_Entity8{
    
private String r110_new_colum_product;
private BigDecimal r110_new_colum_exp_bef_crm;
private BigDecimal r110_new_colum_spec_prov_past_due_exp;
private BigDecimal r110_new_colum_amt_eli_bal_sheet_net;
private BigDecimal r110_new_colum_tot_exp_aft_net;
private BigDecimal r110_new_colum_exp_eli_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_guan_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_cre_deri_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
private BigDecimal r110_new_colum_value;
private BigDecimal r110_new_colum_expeli_colexp_crm_comapr;
private BigDecimal r110_new_colum_exp_aft_voladj_colexp_crm_comapr;
private BigDecimal r110_new_colum_cash_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_equities_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_exp_efc_haircuts_colexp_crm_comapr;
private BigDecimal r110_new_colum_rwa_exp_notcov_crm_rwa;
private BigDecimal r110_new_colum_unsecu_exp_credit_risk_rwa;
private BigDecimal r110_new_colum_rwa_unsecu_exp_rwa;
private BigDecimal r110_new_colum_tot_rwa;

private String r111_new_colum_product;
private BigDecimal r111_new_colum_exp_bef_crm;
private BigDecimal r111_new_colum_spec_prov_past_due_exp;
private BigDecimal r111_new_colum_amt_eli_bal_sheet_net;
private BigDecimal r111_new_colum_tot_exp_aft_net;
private BigDecimal r111_new_colum_exp_eli_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_guan_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_cre_deri_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
private BigDecimal r111_new_colum_value;
private BigDecimal r111_new_colum_expeli_colexp_crm_comapr;
private BigDecimal r111_new_colum_exp_aft_voladj_colexp_crm_comapr;
private BigDecimal r111_new_colum_cash_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_equities_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_exp_efc_haircuts_colexp_crm_comapr;
private BigDecimal r111_new_colum_rwa_exp_notcov_crm_rwa;
private BigDecimal r111_new_colum_unsecu_exp_credit_risk_rwa;
private BigDecimal r111_new_colum_rwa_unsecu_exp_rwa;
private BigDecimal r111_new_colum_tot_rwa;

private String r112_new_colum_product;
private BigDecimal r112_new_colum_exp_bef_crm;
private BigDecimal r112_new_colum_spec_prov_past_due_exp;
private BigDecimal r112_new_colum_amt_eli_bal_sheet_net;
private BigDecimal r112_new_colum_tot_exp_aft_net;
private BigDecimal r112_new_colum_exp_eli_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_guan_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_cre_deri_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
private BigDecimal r112_new_colum_value;
private BigDecimal r112_new_colum_expeli_colexp_crm_comapr;
private BigDecimal r112_new_colum_exp_aft_voladj_colexp_crm_comapr;
private BigDecimal r112_new_colum_cash_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_equities_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_exp_efc_haircuts_colexp_crm_comapr;
private BigDecimal r112_new_colum_rwa_exp_notcov_crm_rwa;
private BigDecimal r112_new_colum_unsecu_exp_credit_risk_rwa;
private BigDecimal r112_new_colum_rwa_unsecu_exp_rwa;
private BigDecimal r112_new_colum_tot_rwa;

@Temporal(TemporalType.DATE)
@DateTimeFormat(pattern = "dd/MM/yyyy")
@Id
@Column(name = "REPORT_DATE")
private Date reportDate;
@Column(name = "REPORT_VERSION")
private String reportVersion;
	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	public String getR110_new_colum_product() {
		return r110_new_colum_product;
	}
	public BigDecimal getR110_new_colum_exp_bef_crm() {
		return r110_new_colum_exp_bef_crm;
	}
	public BigDecimal getR110_new_colum_spec_prov_past_due_exp() {
		return r110_new_colum_spec_prov_past_due_exp;
	}
	public BigDecimal getR110_new_colum_amt_eli_bal_sheet_net() {
		return r110_new_colum_amt_eli_bal_sheet_net;
	}
	public BigDecimal getR110_new_colum_tot_exp_aft_net() {
		return r110_new_colum_tot_exp_aft_net;
	}
	public BigDecimal getR110_new_colum_exp_eli_noncol_exp_crm_subapr() {
		return r110_new_colum_exp_eli_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_guan_noncol_exp_crm_subapr() {
		return r110_new_colum_guan_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_cre_deri_noncol_exp_crm_subapr() {
		return r110_new_colum_cre_deri_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_exp_cov_crm_noncol_exp_crm_subapr() {
		return r110_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr() {
		return r110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_riskweights_rwa_noncol_exp_crm_subapr() {
		return r110_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr() {
		return r110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr() {
		return r110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr() {
		return r110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR110_new_colum_value() {
		return r110_new_colum_value;
	}
	public BigDecimal getR110_new_colum_expeli_colexp_crm_comapr() {
		return r110_new_colum_expeli_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_exp_aft_voladj_colexp_crm_comapr() {
		return r110_new_colum_exp_aft_voladj_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_cash_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_cash_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_equities_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_equities_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_exp_efc_haircuts_colexp_crm_comapr() {
		return r110_new_colum_exp_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR110_new_colum_rwa_exp_notcov_crm_rwa() {
		return r110_new_colum_rwa_exp_notcov_crm_rwa;
	}
	public BigDecimal getR110_new_colum_unsecu_exp_credit_risk_rwa() {
		return r110_new_colum_unsecu_exp_credit_risk_rwa;
	}
	public BigDecimal getR110_new_colum_rwa_unsecu_exp_rwa() {
		return r110_new_colum_rwa_unsecu_exp_rwa;
	}
	public BigDecimal getR110_new_colum_tot_rwa() {
		return r110_new_colum_tot_rwa;
	}
	public String getR111_new_colum_product() {
		return r111_new_colum_product;
	}
	public BigDecimal getR111_new_colum_exp_bef_crm() {
		return r111_new_colum_exp_bef_crm;
	}
	public BigDecimal getR111_new_colum_spec_prov_past_due_exp() {
		return r111_new_colum_spec_prov_past_due_exp;
	}
	public BigDecimal getR111_new_colum_amt_eli_bal_sheet_net() {
		return r111_new_colum_amt_eli_bal_sheet_net;
	}
	public BigDecimal getR111_new_colum_tot_exp_aft_net() {
		return r111_new_colum_tot_exp_aft_net;
	}
	public BigDecimal getR111_new_colum_exp_eli_noncol_exp_crm_subapr() {
		return r111_new_colum_exp_eli_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_guan_noncol_exp_crm_subapr() {
		return r111_new_colum_guan_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_cre_deri_noncol_exp_crm_subapr() {
		return r111_new_colum_cre_deri_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_exp_cov_crm_noncol_exp_crm_subapr() {
		return r111_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr() {
		return r111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_riskweights_rwa_noncol_exp_crm_subapr() {
		return r111_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr() {
		return r111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr() {
		return r111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr() {
		return r111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR111_new_colum_value() {
		return r111_new_colum_value;
	}
	public BigDecimal getR111_new_colum_expeli_colexp_crm_comapr() {
		return r111_new_colum_expeli_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_exp_aft_voladj_colexp_crm_comapr() {
		return r111_new_colum_exp_aft_voladj_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_cash_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_cash_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_equities_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_equities_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_exp_efc_haircuts_colexp_crm_comapr() {
		return r111_new_colum_exp_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR111_new_colum_rwa_exp_notcov_crm_rwa() {
		return r111_new_colum_rwa_exp_notcov_crm_rwa;
	}
	public BigDecimal getR111_new_colum_unsecu_exp_credit_risk_rwa() {
		return r111_new_colum_unsecu_exp_credit_risk_rwa;
	}
	public BigDecimal getR111_new_colum_rwa_unsecu_exp_rwa() {
		return r111_new_colum_rwa_unsecu_exp_rwa;
	}
	public BigDecimal getR111_new_colum_tot_rwa() {
		return r111_new_colum_tot_rwa;
	}
	public String getR112_new_colum_product() {
		return r112_new_colum_product;
	}
	public BigDecimal getR112_new_colum_exp_bef_crm() {
		return r112_new_colum_exp_bef_crm;
	}
	public BigDecimal getR112_new_colum_spec_prov_past_due_exp() {
		return r112_new_colum_spec_prov_past_due_exp;
	}
	public BigDecimal getR112_new_colum_amt_eli_bal_sheet_net() {
		return r112_new_colum_amt_eli_bal_sheet_net;
	}
	public BigDecimal getR112_new_colum_tot_exp_aft_net() {
		return r112_new_colum_tot_exp_aft_net;
	}
	public BigDecimal getR112_new_colum_exp_eli_noncol_exp_crm_subapr() {
		return r112_new_colum_exp_eli_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_guan_noncol_exp_crm_subapr() {
		return r112_new_colum_guan_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_cre_deri_noncol_exp_crm_subapr() {
		return r112_new_colum_cre_deri_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_exp_cov_crm_noncol_exp_crm_subapr() {
		return r112_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr() {
		return r112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_riskweights_rwa_noncol_exp_crm_subapr() {
		return r112_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr() {
		return r112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr() {
		return r112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr() {
		return r112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
	}
	public BigDecimal getR112_new_colum_value() {
		return r112_new_colum_value;
	}
	public BigDecimal getR112_new_colum_expeli_colexp_crm_comapr() {
		return r112_new_colum_expeli_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_exp_aft_voladj_colexp_crm_comapr() {
		return r112_new_colum_exp_aft_voladj_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_cash_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_cash_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_equities_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_equities_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_exp_efc_haircuts_colexp_crm_comapr() {
		return r112_new_colum_exp_efc_haircuts_colexp_crm_comapr;
	}
	public BigDecimal getR112_new_colum_rwa_exp_notcov_crm_rwa() {
		return r112_new_colum_rwa_exp_notcov_crm_rwa;
	}
	public BigDecimal getR112_new_colum_unsecu_exp_credit_risk_rwa() {
		return r112_new_colum_unsecu_exp_credit_risk_rwa;
	}
	public BigDecimal getR112_new_colum_rwa_unsecu_exp_rwa() {
		return r112_new_colum_rwa_unsecu_exp_rwa;
	}
	public BigDecimal getR112_new_colum_tot_rwa() {
		return r112_new_colum_tot_rwa;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public String getReportVersion() {
		return reportVersion;
	}
	public String getReport_frequency() {
		return report_frequency;
	}
	public String getReport_code() {
		return report_code;
	}
	public String getReport_desc() {
		return report_desc;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setR110_new_colum_product(String r110_new_colum_product) {
		this.r110_new_colum_product = r110_new_colum_product;
	}
	public void setR110_new_colum_exp_bef_crm(BigDecimal r110_new_colum_exp_bef_crm) {
		this.r110_new_colum_exp_bef_crm = r110_new_colum_exp_bef_crm;
	}
	public void setR110_new_colum_spec_prov_past_due_exp(BigDecimal r110_new_colum_spec_prov_past_due_exp) {
		this.r110_new_colum_spec_prov_past_due_exp = r110_new_colum_spec_prov_past_due_exp;
	}
	public void setR110_new_colum_amt_eli_bal_sheet_net(BigDecimal r110_new_colum_amt_eli_bal_sheet_net) {
		this.r110_new_colum_amt_eli_bal_sheet_net = r110_new_colum_amt_eli_bal_sheet_net;
	}
	public void setR110_new_colum_tot_exp_aft_net(BigDecimal r110_new_colum_tot_exp_aft_net) {
		this.r110_new_colum_tot_exp_aft_net = r110_new_colum_tot_exp_aft_net;
	}
	public void setR110_new_colum_exp_eli_noncol_exp_crm_subapr(BigDecimal r110_new_colum_exp_eli_noncol_exp_crm_subapr) {
		this.r110_new_colum_exp_eli_noncol_exp_crm_subapr = r110_new_colum_exp_eli_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_guan_noncol_exp_crm_subapr(BigDecimal r110_new_colum_guan_noncol_exp_crm_subapr) {
		this.r110_new_colum_guan_noncol_exp_crm_subapr = r110_new_colum_guan_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_cre_deri_noncol_exp_crm_subapr(BigDecimal r110_new_colum_cre_deri_noncol_exp_crm_subapr) {
		this.r110_new_colum_cre_deri_noncol_exp_crm_subapr = r110_new_colum_cre_deri_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_exp_cov_crm_noncol_exp_crm_subapr(
			BigDecimal r110_new_colum_exp_cov_crm_noncol_exp_crm_subapr) {
		this.r110_new_colum_exp_cov_crm_noncol_exp_crm_subapr = r110_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr(
			BigDecimal r110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr) {
		this.r110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr = r110_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_riskweights_rwa_noncol_exp_crm_subapr(
			BigDecimal r110_new_colum_riskweights_rwa_noncol_exp_crm_subapr) {
		this.r110_new_colum_riskweights_rwa_noncol_exp_crm_subapr = r110_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr(
			BigDecimal r110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr) {
		this.r110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr = r110_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr(
			BigDecimal r110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr) {
		this.r110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr = r110_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr(
			BigDecimal r110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr) {
		this.r110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr = r110_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
	}
	public void setR110_new_colum_value(BigDecimal r110_new_colum_value) {
		this.r110_new_colum_value = r110_new_colum_value;
	}
	public void setR110_new_colum_expeli_colexp_crm_comapr(BigDecimal r110_new_colum_expeli_colexp_crm_comapr) {
		this.r110_new_colum_expeli_colexp_crm_comapr = r110_new_colum_expeli_colexp_crm_comapr;
	}
	public void setR110_new_colum_exp_aft_voladj_colexp_crm_comapr(
			BigDecimal r110_new_colum_exp_aft_voladj_colexp_crm_comapr) {
		this.r110_new_colum_exp_aft_voladj_colexp_crm_comapr = r110_new_colum_exp_aft_voladj_colexp_crm_comapr;
	}
	public void setR110_new_colum_cash_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_cash_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_cash_efc_haircuts_colexp_crm_comapr = r110_new_colum_cash_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr = r110_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr = r110_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_equities_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_equities_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_equities_efc_haircuts_colexp_crm_comapr = r110_new_colum_equities_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr = r110_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr = r110_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_exp_efc_haircuts_colexp_crm_comapr(
			BigDecimal r110_new_colum_exp_efc_haircuts_colexp_crm_comapr) {
		this.r110_new_colum_exp_efc_haircuts_colexp_crm_comapr = r110_new_colum_exp_efc_haircuts_colexp_crm_comapr;
	}
	public void setR110_new_colum_rwa_exp_notcov_crm_rwa(BigDecimal r110_new_colum_rwa_exp_notcov_crm_rwa) {
		this.r110_new_colum_rwa_exp_notcov_crm_rwa = r110_new_colum_rwa_exp_notcov_crm_rwa;
	}
	public void setR110_new_colum_unsecu_exp_credit_risk_rwa(BigDecimal r110_new_colum_unsecu_exp_credit_risk_rwa) {
		this.r110_new_colum_unsecu_exp_credit_risk_rwa = r110_new_colum_unsecu_exp_credit_risk_rwa;
	}
	public void setR110_new_colum_rwa_unsecu_exp_rwa(BigDecimal r110_new_colum_rwa_unsecu_exp_rwa) {
		this.r110_new_colum_rwa_unsecu_exp_rwa = r110_new_colum_rwa_unsecu_exp_rwa;
	}
	public void setR110_new_colum_tot_rwa(BigDecimal r110_new_colum_tot_rwa) {
		this.r110_new_colum_tot_rwa = r110_new_colum_tot_rwa;
	}
	public void setR111_new_colum_product(String r111_new_colum_product) {
		this.r111_new_colum_product = r111_new_colum_product;
	}
	public void setR111_new_colum_exp_bef_crm(BigDecimal r111_new_colum_exp_bef_crm) {
		this.r111_new_colum_exp_bef_crm = r111_new_colum_exp_bef_crm;
	}
	public void setR111_new_colum_spec_prov_past_due_exp(BigDecimal r111_new_colum_spec_prov_past_due_exp) {
		this.r111_new_colum_spec_prov_past_due_exp = r111_new_colum_spec_prov_past_due_exp;
	}
	public void setR111_new_colum_amt_eli_bal_sheet_net(BigDecimal r111_new_colum_amt_eli_bal_sheet_net) {
		this.r111_new_colum_amt_eli_bal_sheet_net = r111_new_colum_amt_eli_bal_sheet_net;
	}
	public void setR111_new_colum_tot_exp_aft_net(BigDecimal r111_new_colum_tot_exp_aft_net) {
		this.r111_new_colum_tot_exp_aft_net = r111_new_colum_tot_exp_aft_net;
	}
	public void setR111_new_colum_exp_eli_noncol_exp_crm_subapr(BigDecimal r111_new_colum_exp_eli_noncol_exp_crm_subapr) {
		this.r111_new_colum_exp_eli_noncol_exp_crm_subapr = r111_new_colum_exp_eli_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_guan_noncol_exp_crm_subapr(BigDecimal r111_new_colum_guan_noncol_exp_crm_subapr) {
		this.r111_new_colum_guan_noncol_exp_crm_subapr = r111_new_colum_guan_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_cre_deri_noncol_exp_crm_subapr(BigDecimal r111_new_colum_cre_deri_noncol_exp_crm_subapr) {
		this.r111_new_colum_cre_deri_noncol_exp_crm_subapr = r111_new_colum_cre_deri_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_exp_cov_crm_noncol_exp_crm_subapr(
			BigDecimal r111_new_colum_exp_cov_crm_noncol_exp_crm_subapr) {
		this.r111_new_colum_exp_cov_crm_noncol_exp_crm_subapr = r111_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr(
			BigDecimal r111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr) {
		this.r111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr = r111_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_riskweights_rwa_noncol_exp_crm_subapr(
			BigDecimal r111_new_colum_riskweights_rwa_noncol_exp_crm_subapr) {
		this.r111_new_colum_riskweights_rwa_noncol_exp_crm_subapr = r111_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr(
			BigDecimal r111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr) {
		this.r111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr = r111_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr(
			BigDecimal r111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr) {
		this.r111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr = r111_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr(
			BigDecimal r111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr) {
		this.r111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr = r111_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
	}
	public void setR111_new_colum_value(BigDecimal r111_new_colum_value) {
		this.r111_new_colum_value = r111_new_colum_value;
	}
	public void setR111_new_colum_expeli_colexp_crm_comapr(BigDecimal r111_new_colum_expeli_colexp_crm_comapr) {
		this.r111_new_colum_expeli_colexp_crm_comapr = r111_new_colum_expeli_colexp_crm_comapr;
	}
	public void setR111_new_colum_exp_aft_voladj_colexp_crm_comapr(
			BigDecimal r111_new_colum_exp_aft_voladj_colexp_crm_comapr) {
		this.r111_new_colum_exp_aft_voladj_colexp_crm_comapr = r111_new_colum_exp_aft_voladj_colexp_crm_comapr;
	}
	public void setR111_new_colum_cash_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_cash_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_cash_efc_haircuts_colexp_crm_comapr = r111_new_colum_cash_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr = r111_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr = r111_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_equities_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_equities_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_equities_efc_haircuts_colexp_crm_comapr = r111_new_colum_equities_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr = r111_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr = r111_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_exp_efc_haircuts_colexp_crm_comapr(
			BigDecimal r111_new_colum_exp_efc_haircuts_colexp_crm_comapr) {
		this.r111_new_colum_exp_efc_haircuts_colexp_crm_comapr = r111_new_colum_exp_efc_haircuts_colexp_crm_comapr;
	}
	public void setR111_new_colum_rwa_exp_notcov_crm_rwa(BigDecimal r111_new_colum_rwa_exp_notcov_crm_rwa) {
		this.r111_new_colum_rwa_exp_notcov_crm_rwa = r111_new_colum_rwa_exp_notcov_crm_rwa;
	}
	public void setR111_new_colum_unsecu_exp_credit_risk_rwa(BigDecimal r111_new_colum_unsecu_exp_credit_risk_rwa) {
		this.r111_new_colum_unsecu_exp_credit_risk_rwa = r111_new_colum_unsecu_exp_credit_risk_rwa;
	}
	public void setR111_new_colum_rwa_unsecu_exp_rwa(BigDecimal r111_new_colum_rwa_unsecu_exp_rwa) {
		this.r111_new_colum_rwa_unsecu_exp_rwa = r111_new_colum_rwa_unsecu_exp_rwa;
	}
	public void setR111_new_colum_tot_rwa(BigDecimal r111_new_colum_tot_rwa) {
		this.r111_new_colum_tot_rwa = r111_new_colum_tot_rwa;
	}
	public void setR112_new_colum_product(String r112_new_colum_product) {
		this.r112_new_colum_product = r112_new_colum_product;
	}
	public void setR112_new_colum_exp_bef_crm(BigDecimal r112_new_colum_exp_bef_crm) {
		this.r112_new_colum_exp_bef_crm = r112_new_colum_exp_bef_crm;
	}
	public void setR112_new_colum_spec_prov_past_due_exp(BigDecimal r112_new_colum_spec_prov_past_due_exp) {
		this.r112_new_colum_spec_prov_past_due_exp = r112_new_colum_spec_prov_past_due_exp;
	}
	public void setR112_new_colum_amt_eli_bal_sheet_net(BigDecimal r112_new_colum_amt_eli_bal_sheet_net) {
		this.r112_new_colum_amt_eli_bal_sheet_net = r112_new_colum_amt_eli_bal_sheet_net;
	}
	public void setR112_new_colum_tot_exp_aft_net(BigDecimal r112_new_colum_tot_exp_aft_net) {
		this.r112_new_colum_tot_exp_aft_net = r112_new_colum_tot_exp_aft_net;
	}
	public void setR112_new_colum_exp_eli_noncol_exp_crm_subapr(BigDecimal r112_new_colum_exp_eli_noncol_exp_crm_subapr) {
		this.r112_new_colum_exp_eli_noncol_exp_crm_subapr = r112_new_colum_exp_eli_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_guan_noncol_exp_crm_subapr(BigDecimal r112_new_colum_guan_noncol_exp_crm_subapr) {
		this.r112_new_colum_guan_noncol_exp_crm_subapr = r112_new_colum_guan_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_cre_deri_noncol_exp_crm_subapr(BigDecimal r112_new_colum_cre_deri_noncol_exp_crm_subapr) {
		this.r112_new_colum_cre_deri_noncol_exp_crm_subapr = r112_new_colum_cre_deri_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_exp_cov_crm_noncol_exp_crm_subapr(
			BigDecimal r112_new_colum_exp_cov_crm_noncol_exp_crm_subapr) {
		this.r112_new_colum_exp_cov_crm_noncol_exp_crm_subapr = r112_new_colum_exp_cov_crm_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr(
			BigDecimal r112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr) {
		this.r112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr = r112_new_colum_eliexp_notcov_crm_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_riskweights_rwa_noncol_exp_crm_subapr(
			BigDecimal r112_new_colum_riskweights_rwa_noncol_exp_crm_subapr) {
		this.r112_new_colum_riskweights_rwa_noncol_exp_crm_subapr = r112_new_colum_riskweights_rwa_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr(
			BigDecimal r112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr) {
		this.r112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr = r112_new_colum_rwa_cov_crm_rwa_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr(
			BigDecimal r112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr) {
		this.r112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr = r112_new_colum_rw_oricount_rwa_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr(
			BigDecimal r112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr) {
		this.r112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr = r112_new_colum_rwa_notcov_crm_rwa_noncol_exp_crm_subapr;
	}
	public void setR112_new_colum_value(BigDecimal r112_new_colum_value) {
		this.r112_new_colum_value = r112_new_colum_value;
	}
	public void setR112_new_colum_expeli_colexp_crm_comapr(BigDecimal r112_new_colum_expeli_colexp_crm_comapr) {
		this.r112_new_colum_expeli_colexp_crm_comapr = r112_new_colum_expeli_colexp_crm_comapr;
	}
	public void setR112_new_colum_exp_aft_voladj_colexp_crm_comapr(
			BigDecimal r112_new_colum_exp_aft_voladj_colexp_crm_comapr) {
		this.r112_new_colum_exp_aft_voladj_colexp_crm_comapr = r112_new_colum_exp_aft_voladj_colexp_crm_comapr;
	}
	public void setR112_new_colum_cash_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_cash_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_cash_efc_haircuts_colexp_crm_comapr = r112_new_colum_cash_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr = r112_new_colum_tresbills_bot_bob_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr = r112_new_colum_debt_secu_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_equities_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_equities_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_equities_efc_haircuts_colexp_crm_comapr = r112_new_colum_equities_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr = r112_new_colum_u_trust_m_funds_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr = r112_new_colum_totcol_aft_hc_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_exp_efc_haircuts_colexp_crm_comapr(
			BigDecimal r112_new_colum_exp_efc_haircuts_colexp_crm_comapr) {
		this.r112_new_colum_exp_efc_haircuts_colexp_crm_comapr = r112_new_colum_exp_efc_haircuts_colexp_crm_comapr;
	}
	public void setR112_new_colum_rwa_exp_notcov_crm_rwa(BigDecimal r112_new_colum_rwa_exp_notcov_crm_rwa) {
		this.r112_new_colum_rwa_exp_notcov_crm_rwa = r112_new_colum_rwa_exp_notcov_crm_rwa;
	}
	public void setR112_new_colum_unsecu_exp_credit_risk_rwa(BigDecimal r112_new_colum_unsecu_exp_credit_risk_rwa) {
		this.r112_new_colum_unsecu_exp_credit_risk_rwa = r112_new_colum_unsecu_exp_credit_risk_rwa;
	}
	public void setR112_new_colum_rwa_unsecu_exp_rwa(BigDecimal r112_new_colum_rwa_unsecu_exp_rwa) {
		this.r112_new_colum_rwa_unsecu_exp_rwa = r112_new_colum_rwa_unsecu_exp_rwa;
	}
	public void setR112_new_colum_tot_rwa(BigDecimal r112_new_colum_tot_rwa) {
		this.r112_new_colum_tot_rwa = r112_new_colum_tot_rwa;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}
	public void setReport_frequency(String report_frequency) {
		this.report_frequency = report_frequency;
	}
	public void setReport_code(String report_code) {
		this.report_code = report_code;
	}
	public void setReport_desc(String report_desc) {
		this.report_desc = report_desc;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	
	

}