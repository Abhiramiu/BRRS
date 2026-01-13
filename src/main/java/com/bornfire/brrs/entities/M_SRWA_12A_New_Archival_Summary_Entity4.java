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
@Table(name = "BRRS_M_SRWA_12A_NEW_ARCHIVALTABLE_SUMMARY4")


public class M_SRWA_12A_New_Archival_Summary_Entity4 {
	
	

		
	
	private String	r123_exposure_class;
	private BigDecimal	r123_expo_crm;
	private BigDecimal	r123_spe_pro_expo;
	private BigDecimal	r123_amt_elig_sht_net;
	private BigDecimal	r123_tot_expo_net_spe;
	private BigDecimal	r123_crm_sub_elig_sub_app;
	private BigDecimal	r123_crm_sub_non_col_guar;
	private BigDecimal	r123_crm_sub_non_col_cre_der;
	private BigDecimal	r123_crm_sub_col_elig_cash;
	private BigDecimal	r123_crm_sub_col_elig_trea_bills;
	private BigDecimal	r123_crm_sub_col_elig_deb_sec;
	private BigDecimal	r123_crm_sub_col_elig_equi;
	private BigDecimal	r123_crm_sub_col_elig_unit_tru;
	private BigDecimal	r123_crm_sub_col_exp_cov;
	private BigDecimal	r123_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r123_crm_sub_rwa_ris_crm;
	private BigDecimal	r123_crm_sub_rwa_cov_crm;
	private BigDecimal	r123_crm_sub_rwa_org_cou;
	private BigDecimal	r123_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r123_crm_comp_col_expo_elig;
	private BigDecimal	r123_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r123_crm_comp_col_elig_fin_hai;
	private BigDecimal	r123_crm_comp_col_expo_val;
	private BigDecimal	r123_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r123_rwa_unsec_expo_cre_ris;
	private BigDecimal	r123_rwa_unsec_expo;
	private BigDecimal	r123_rwa_tot_ris_wei_ass;
	private String	r124_exposure_class;
	private BigDecimal	r124_expo_crm;
	private BigDecimal	r124_spe_pro_expo;
	private BigDecimal	r124_amt_elig_sht_net;
	private BigDecimal	r124_tot_expo_net_spe;
	private BigDecimal	r124_crm_sub_elig_sub_app;
	private BigDecimal	r124_crm_sub_non_col_guar;
	private BigDecimal	r124_crm_sub_non_col_cre_der;
	private BigDecimal	r124_crm_sub_col_elig_cash;
	private BigDecimal	r124_crm_sub_col_elig_trea_bills;
	private BigDecimal	r124_crm_sub_col_elig_deb_sec;
	private BigDecimal	r124_crm_sub_col_elig_equi;
	private BigDecimal	r124_crm_sub_col_elig_unit_tru;
	private BigDecimal	r124_crm_sub_col_exp_cov;
	private BigDecimal	r124_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r124_crm_sub_rwa_ris_crm;
	private BigDecimal	r124_crm_sub_rwa_cov_crm;
	private BigDecimal	r124_crm_sub_rwa_org_cou;
	private BigDecimal	r124_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r124_crm_comp_col_expo_elig;
	private BigDecimal	r124_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r124_crm_comp_col_elig_fin_hai;
	private BigDecimal	r124_crm_comp_col_expo_val;
	private BigDecimal	r124_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r124_rwa_unsec_expo_cre_ris;
	private BigDecimal	r124_rwa_unsec_expo;
	private BigDecimal	r124_rwa_tot_ris_wei_ass;
	private String	r125_exposure_class;
	private BigDecimal	r125_expo_crm;
	private BigDecimal	r125_spe_pro_expo;
	private BigDecimal	r125_amt_elig_sht_net;
	private BigDecimal	r125_tot_expo_net_spe;
	private BigDecimal	r125_crm_sub_elig_sub_app;
	private BigDecimal	r125_crm_sub_non_col_guar;
	private BigDecimal	r125_crm_sub_non_col_cre_der;
	private BigDecimal	r125_crm_sub_col_elig_cash;
	private BigDecimal	r125_crm_sub_col_elig_trea_bills;
	private BigDecimal	r125_crm_sub_col_elig_deb_sec;
	private BigDecimal	r125_crm_sub_col_elig_equi;
	private BigDecimal	r125_crm_sub_col_elig_unit_tru;
	private BigDecimal	r125_crm_sub_col_exp_cov;
	private BigDecimal	r125_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r125_crm_sub_rwa_ris_crm;
	private BigDecimal	r125_crm_sub_rwa_cov_crm;
	private BigDecimal	r125_crm_sub_rwa_org_cou;
	private BigDecimal	r125_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r125_crm_comp_col_expo_elig;
	private BigDecimal	r125_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r125_crm_comp_col_elig_fin_hai;
	private BigDecimal	r125_crm_comp_col_expo_val;
	private BigDecimal	r125_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r125_rwa_unsec_expo_cre_ris;
	private BigDecimal	r125_rwa_unsec_expo;
	private BigDecimal	r125_rwa_tot_ris_wei_ass;
	private String	r126_exposure_class;
	private BigDecimal	r126_expo_crm;
	private BigDecimal	r126_spe_pro_expo;
	private BigDecimal	r126_amt_elig_sht_net;
	private BigDecimal	r126_tot_expo_net_spe;
	private BigDecimal	r126_crm_sub_elig_sub_app;
	private BigDecimal	r126_crm_sub_non_col_guar;
	private BigDecimal	r126_crm_sub_non_col_cre_der;
	private BigDecimal	r126_crm_sub_col_elig_cash;
	private BigDecimal	r126_crm_sub_col_elig_trea_bills;
	private BigDecimal	r126_crm_sub_col_elig_deb_sec;
	private BigDecimal	r126_crm_sub_col_elig_equi;
	private BigDecimal	r126_crm_sub_col_elig_unit_tru;
	private BigDecimal	r126_crm_sub_col_exp_cov;
	private BigDecimal	r126_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r126_crm_sub_rwa_ris_crm;
	private BigDecimal	r126_crm_sub_rwa_cov_crm;
	private BigDecimal	r126_crm_sub_rwa_org_cou;
	private BigDecimal	r126_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r126_crm_comp_col_expo_elig;
	private BigDecimal	r126_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r126_crm_comp_col_elig_fin_hai;
	private BigDecimal	r126_crm_comp_col_expo_val;
	private BigDecimal	r126_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r126_rwa_unsec_expo_cre_ris;
	private BigDecimal	r126_rwa_unsec_expo;
	private BigDecimal	r126_rwa_tot_ris_wei_ass;
	private String	r127_exposure_class;
	private BigDecimal	r127_expo_crm;
	private BigDecimal	r127_spe_pro_expo;
	private BigDecimal	r127_amt_elig_sht_net;
	private BigDecimal	r127_tot_expo_net_spe;
	private BigDecimal	r127_crm_sub_elig_sub_app;
	private BigDecimal	r127_crm_sub_non_col_guar;
	private BigDecimal	r127_crm_sub_non_col_cre_der;
	private BigDecimal	r127_crm_sub_col_elig_cash;
	private BigDecimal	r127_crm_sub_col_elig_trea_bills;
	private BigDecimal	r127_crm_sub_col_elig_deb_sec;
	private BigDecimal	r127_crm_sub_col_elig_equi;
	private BigDecimal	r127_crm_sub_col_elig_unit_tru;
	private BigDecimal	r127_crm_sub_col_exp_cov;
	private BigDecimal	r127_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r127_crm_sub_rwa_ris_crm;
	private BigDecimal	r127_crm_sub_rwa_cov_crm;
	private BigDecimal	r127_crm_sub_rwa_org_cou;
	private BigDecimal	r127_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r127_crm_comp_col_expo_elig;
	private BigDecimal	r127_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r127_crm_comp_col_elig_fin_hai;
	private BigDecimal	r127_crm_comp_col_expo_val;
	private BigDecimal	r127_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r127_rwa_unsec_expo_cre_ris;
	private BigDecimal	r127_rwa_unsec_expo;
	private BigDecimal	r127_rwa_tot_ris_wei_ass;
	private String	r128_exposure_class;
	private BigDecimal	r128_expo_crm;
	private BigDecimal	r128_spe_pro_expo;
	private BigDecimal	r128_amt_elig_sht_net;
	private BigDecimal	r128_tot_expo_net_spe;
	private BigDecimal	r128_crm_sub_elig_sub_app;
	private BigDecimal	r128_crm_sub_non_col_guar;
	private BigDecimal	r128_crm_sub_non_col_cre_der;
	private BigDecimal	r128_crm_sub_col_elig_cash;
	private BigDecimal	r128_crm_sub_col_elig_trea_bills;
	private BigDecimal	r128_crm_sub_col_elig_deb_sec;
	private BigDecimal	r128_crm_sub_col_elig_equi;
	private BigDecimal	r128_crm_sub_col_elig_unit_tru;
	private BigDecimal	r128_crm_sub_col_exp_cov;
	private BigDecimal	r128_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r128_crm_sub_rwa_ris_crm;
	private BigDecimal	r128_crm_sub_rwa_cov_crm;
	private BigDecimal	r128_crm_sub_rwa_org_cou;
	private BigDecimal	r128_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r128_crm_comp_col_expo_elig;
	private BigDecimal	r128_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r128_crm_comp_col_elig_fin_hai;
	private BigDecimal	r128_crm_comp_col_expo_val;
	private BigDecimal	r128_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r128_rwa_unsec_expo_cre_ris;
	private BigDecimal	r128_rwa_unsec_expo;
	private BigDecimal	r128_rwa_tot_ris_wei_ass;
	private String	r129_exposure_class;
	private BigDecimal	r129_expo_crm;
	private BigDecimal	r129_spe_pro_expo;
	private BigDecimal	r129_amt_elig_sht_net;
	private BigDecimal	r129_tot_expo_net_spe;
	private BigDecimal	r129_crm_sub_elig_sub_app;
	private BigDecimal	r129_crm_sub_non_col_guar;
	private BigDecimal	r129_crm_sub_non_col_cre_der;
	private BigDecimal	r129_crm_sub_col_elig_cash;
	private BigDecimal	r129_crm_sub_col_elig_trea_bills;
	private BigDecimal	r129_crm_sub_col_elig_deb_sec;
	private BigDecimal	r129_crm_sub_col_elig_equi;
	private BigDecimal	r129_crm_sub_col_elig_unit_tru;
	private BigDecimal	r129_crm_sub_col_exp_cov;
	private BigDecimal	r129_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r129_crm_sub_rwa_ris_crm;
	private BigDecimal	r129_crm_sub_rwa_cov_crm;
	private BigDecimal	r129_crm_sub_rwa_org_cou;
	private BigDecimal	r129_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r129_crm_comp_col_expo_elig;
	private BigDecimal	r129_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r129_crm_comp_col_elig_fin_hai;
	private BigDecimal	r129_crm_comp_col_expo_val;
	private BigDecimal	r129_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r129_rwa_unsec_expo_cre_ris;
	private BigDecimal	r129_rwa_unsec_expo;
	private BigDecimal	r129_rwa_tot_ris_wei_ass;
	private String	r130_exposure_class;
	private BigDecimal	r130_expo_crm;
	private BigDecimal	r130_spe_pro_expo;
	private BigDecimal	r130_amt_elig_sht_net;
	private BigDecimal	r130_tot_expo_net_spe;
	private BigDecimal	r130_crm_sub_elig_sub_app;
	private BigDecimal	r130_crm_sub_non_col_guar;
	private BigDecimal	r130_crm_sub_non_col_cre_der;
	private BigDecimal	r130_crm_sub_col_elig_cash;
	private BigDecimal	r130_crm_sub_col_elig_trea_bills;
	private BigDecimal	r130_crm_sub_col_elig_deb_sec;
	private BigDecimal	r130_crm_sub_col_elig_equi;
	private BigDecimal	r130_crm_sub_col_elig_unit_tru;
	private BigDecimal	r130_crm_sub_col_exp_cov;
	private BigDecimal	r130_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r130_crm_sub_rwa_ris_crm;
	private BigDecimal	r130_crm_sub_rwa_cov_crm;
	private BigDecimal	r130_crm_sub_rwa_org_cou;
	private BigDecimal	r130_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r130_crm_comp_col_expo_elig;
	private BigDecimal	r130_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r130_crm_comp_col_elig_fin_hai;
	private BigDecimal	r130_crm_comp_col_expo_val;
	private BigDecimal	r130_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r130_rwa_unsec_expo_cre_ris;
	private BigDecimal	r130_rwa_unsec_expo;
	private BigDecimal	r130_rwa_tot_ris_wei_ass;
	private String	r131_exposure_class;
	private BigDecimal	r131_expo_crm;
	private BigDecimal	r131_spe_pro_expo;
	private BigDecimal	r131_amt_elig_sht_net;
	private BigDecimal	r131_tot_expo_net_spe;
	private BigDecimal	r131_crm_sub_elig_sub_app;
	private BigDecimal	r131_crm_sub_non_col_guar;
	private BigDecimal	r131_crm_sub_non_col_cre_der;
	private BigDecimal	r131_crm_sub_col_elig_cash;
	private BigDecimal	r131_crm_sub_col_elig_trea_bills;
	private BigDecimal	r131_crm_sub_col_elig_deb_sec;
	private BigDecimal	r131_crm_sub_col_elig_equi;
	private BigDecimal	r131_crm_sub_col_elig_unit_tru;
	private BigDecimal	r131_crm_sub_col_exp_cov;
	private BigDecimal	r131_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r131_crm_sub_rwa_ris_crm;
	private BigDecimal	r131_crm_sub_rwa_cov_crm;
	private BigDecimal	r131_crm_sub_rwa_org_cou;
	private BigDecimal	r131_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r131_crm_comp_col_expo_elig;
	private BigDecimal	r131_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r131_crm_comp_col_elig_fin_hai;
	private BigDecimal	r131_crm_comp_col_expo_val;
	private BigDecimal	r131_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r131_rwa_unsec_expo_cre_ris;
	private BigDecimal	r131_rwa_unsec_expo;
	private BigDecimal	r131_rwa_tot_ris_wei_ass;
	private String	r132_exposure_class;
	private BigDecimal	r132_expo_crm;
	private BigDecimal	r132_spe_pro_expo;
	private BigDecimal	r132_amt_elig_sht_net;
	private BigDecimal	r132_tot_expo_net_spe;
	private BigDecimal	r132_crm_sub_elig_sub_app;
	private BigDecimal	r132_crm_sub_non_col_guar;
	private BigDecimal	r132_crm_sub_non_col_cre_der;
	private BigDecimal	r132_crm_sub_col_elig_cash;
	private BigDecimal	r132_crm_sub_col_elig_trea_bills;
	private BigDecimal	r132_crm_sub_col_elig_deb_sec;
	private BigDecimal	r132_crm_sub_col_elig_equi;
	private BigDecimal	r132_crm_sub_col_elig_unit_tru;
	private BigDecimal	r132_crm_sub_col_exp_cov;
	private BigDecimal	r132_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r132_crm_sub_rwa_ris_crm;
	private BigDecimal	r132_crm_sub_rwa_cov_crm;
	private BigDecimal	r132_crm_sub_rwa_org_cou;
	private BigDecimal	r132_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r132_crm_comp_col_expo_elig;
	private BigDecimal	r132_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r132_crm_comp_col_elig_fin_hai;
	private BigDecimal	r132_crm_comp_col_expo_val;
	private BigDecimal	r132_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r132_rwa_unsec_expo_cre_ris;
	private BigDecimal	r132_rwa_unsec_expo;
	private BigDecimal	r132_rwa_tot_ris_wei_ass;
	private String	r133_exposure_class;
	private BigDecimal	r133_expo_crm;
	private BigDecimal	r133_spe_pro_expo;
	private BigDecimal	r133_amt_elig_sht_net;
	private BigDecimal	r133_tot_expo_net_spe;
	private BigDecimal	r133_crm_sub_elig_sub_app;
	private BigDecimal	r133_crm_sub_non_col_guar;
	private BigDecimal	r133_crm_sub_non_col_cre_der;
	private BigDecimal	r133_crm_sub_col_elig_cash;
	private BigDecimal	r133_crm_sub_col_elig_trea_bills;
	private BigDecimal	r133_crm_sub_col_elig_deb_sec;
	private BigDecimal	r133_crm_sub_col_elig_equi;
	private BigDecimal	r133_crm_sub_col_elig_unit_tru;
	private BigDecimal	r133_crm_sub_col_exp_cov;
	private BigDecimal	r133_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r133_crm_sub_rwa_ris_crm;
	private BigDecimal	r133_crm_sub_rwa_cov_crm;
	private BigDecimal	r133_crm_sub_rwa_org_cou;
	private BigDecimal	r133_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r133_crm_comp_col_expo_elig;
	private BigDecimal	r133_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r133_crm_comp_col_elig_fin_hai;
	private BigDecimal	r133_crm_comp_col_expo_val;
	private BigDecimal	r133_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r133_rwa_unsec_expo_cre_ris;
	private BigDecimal	r133_rwa_unsec_expo;
	private BigDecimal	r133_rwa_tot_ris_wei_ass;
	private String	r134_exposure_class;
	private BigDecimal	r134_expo_crm;
	private BigDecimal	r134_spe_pro_expo;
	private BigDecimal	r134_amt_elig_sht_net;
	private BigDecimal	r134_tot_expo_net_spe;
	private BigDecimal	r134_crm_sub_elig_sub_app;
	private BigDecimal	r134_crm_sub_non_col_guar;
	private BigDecimal	r134_crm_sub_non_col_cre_der;
	private BigDecimal	r134_crm_sub_col_elig_cash;
	private BigDecimal	r134_crm_sub_col_elig_trea_bills;
	private BigDecimal	r134_crm_sub_col_elig_deb_sec;
	private BigDecimal	r134_crm_sub_col_elig_equi;
	private BigDecimal	r134_crm_sub_col_elig_unit_tru;
	private BigDecimal	r134_crm_sub_col_exp_cov;
	private BigDecimal	r134_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r134_crm_sub_rwa_ris_crm;
	private BigDecimal	r134_crm_sub_rwa_cov_crm;
	private BigDecimal	r134_crm_sub_rwa_org_cou;
	private BigDecimal	r134_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r134_crm_comp_col_expo_elig;
	private BigDecimal	r134_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r134_crm_comp_col_elig_fin_hai;
	private BigDecimal	r134_crm_comp_col_expo_val;
	private BigDecimal	r134_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r134_rwa_unsec_expo_cre_ris;
	private BigDecimal	r134_rwa_unsec_expo;
	private BigDecimal	r134_rwa_tot_ris_wei_ass;


	               
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
	private String	del_flg;
	public String getR123_exposure_class() {
		return r123_exposure_class;
	}
	public void setR123_exposure_class(String r123_exposure_class) {
		this.r123_exposure_class = r123_exposure_class;
	}
	public BigDecimal getR123_expo_crm() {
		return r123_expo_crm;
	}
	public void setR123_expo_crm(BigDecimal r123_expo_crm) {
		this.r123_expo_crm = r123_expo_crm;
	}
	public BigDecimal getR123_spe_pro_expo() {
		return r123_spe_pro_expo;
	}
	public void setR123_spe_pro_expo(BigDecimal r123_spe_pro_expo) {
		this.r123_spe_pro_expo = r123_spe_pro_expo;
	}
	public BigDecimal getR123_amt_elig_sht_net() {
		return r123_amt_elig_sht_net;
	}
	public void setR123_amt_elig_sht_net(BigDecimal r123_amt_elig_sht_net) {
		this.r123_amt_elig_sht_net = r123_amt_elig_sht_net;
	}
	public BigDecimal getR123_tot_expo_net_spe() {
		return r123_tot_expo_net_spe;
	}
	public void setR123_tot_expo_net_spe(BigDecimal r123_tot_expo_net_spe) {
		this.r123_tot_expo_net_spe = r123_tot_expo_net_spe;
	}
	public BigDecimal getR123_crm_sub_elig_sub_app() {
		return r123_crm_sub_elig_sub_app;
	}
	public void setR123_crm_sub_elig_sub_app(BigDecimal r123_crm_sub_elig_sub_app) {
		this.r123_crm_sub_elig_sub_app = r123_crm_sub_elig_sub_app;
	}
	public BigDecimal getR123_crm_sub_non_col_guar() {
		return r123_crm_sub_non_col_guar;
	}
	public void setR123_crm_sub_non_col_guar(BigDecimal r123_crm_sub_non_col_guar) {
		this.r123_crm_sub_non_col_guar = r123_crm_sub_non_col_guar;
	}
	public BigDecimal getR123_crm_sub_non_col_cre_der() {
		return r123_crm_sub_non_col_cre_der;
	}
	public void setR123_crm_sub_non_col_cre_der(BigDecimal r123_crm_sub_non_col_cre_der) {
		this.r123_crm_sub_non_col_cre_der = r123_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR123_crm_sub_col_elig_cash() {
		return r123_crm_sub_col_elig_cash;
	}
	public void setR123_crm_sub_col_elig_cash(BigDecimal r123_crm_sub_col_elig_cash) {
		this.r123_crm_sub_col_elig_cash = r123_crm_sub_col_elig_cash;
	}
	public BigDecimal getR123_crm_sub_col_elig_trea_bills() {
		return r123_crm_sub_col_elig_trea_bills;
	}
	public void setR123_crm_sub_col_elig_trea_bills(BigDecimal r123_crm_sub_col_elig_trea_bills) {
		this.r123_crm_sub_col_elig_trea_bills = r123_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR123_crm_sub_col_elig_deb_sec() {
		return r123_crm_sub_col_elig_deb_sec;
	}
	public void setR123_crm_sub_col_elig_deb_sec(BigDecimal r123_crm_sub_col_elig_deb_sec) {
		this.r123_crm_sub_col_elig_deb_sec = r123_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR123_crm_sub_col_elig_equi() {
		return r123_crm_sub_col_elig_equi;
	}
	public void setR123_crm_sub_col_elig_equi(BigDecimal r123_crm_sub_col_elig_equi) {
		this.r123_crm_sub_col_elig_equi = r123_crm_sub_col_elig_equi;
	}
	public BigDecimal getR123_crm_sub_col_elig_unit_tru() {
		return r123_crm_sub_col_elig_unit_tru;
	}
	public void setR123_crm_sub_col_elig_unit_tru(BigDecimal r123_crm_sub_col_elig_unit_tru) {
		this.r123_crm_sub_col_elig_unit_tru = r123_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR123_crm_sub_col_exp_cov() {
		return r123_crm_sub_col_exp_cov;
	}
	public void setR123_crm_sub_col_exp_cov(BigDecimal r123_crm_sub_col_exp_cov) {
		this.r123_crm_sub_col_exp_cov = r123_crm_sub_col_exp_cov;
	}
	public BigDecimal getR123_crm_sub_col_elig_exp_not_cov() {
		return r123_crm_sub_col_elig_exp_not_cov;
	}
	public void setR123_crm_sub_col_elig_exp_not_cov(BigDecimal r123_crm_sub_col_elig_exp_not_cov) {
		this.r123_crm_sub_col_elig_exp_not_cov = r123_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR123_crm_sub_rwa_ris_crm() {
		return r123_crm_sub_rwa_ris_crm;
	}
	public void setR123_crm_sub_rwa_ris_crm(BigDecimal r123_crm_sub_rwa_ris_crm) {
		this.r123_crm_sub_rwa_ris_crm = r123_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR123_crm_sub_rwa_cov_crm() {
		return r123_crm_sub_rwa_cov_crm;
	}
	public void setR123_crm_sub_rwa_cov_crm(BigDecimal r123_crm_sub_rwa_cov_crm) {
		this.r123_crm_sub_rwa_cov_crm = r123_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR123_crm_sub_rwa_org_cou() {
		return r123_crm_sub_rwa_org_cou;
	}
	public void setR123_crm_sub_rwa_org_cou(BigDecimal r123_crm_sub_rwa_org_cou) {
		this.r123_crm_sub_rwa_org_cou = r123_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR123_crm_sub_rwa_not_cov_crm() {
		return r123_crm_sub_rwa_not_cov_crm;
	}
	public void setR123_crm_sub_rwa_not_cov_crm(BigDecimal r123_crm_sub_rwa_not_cov_crm) {
		this.r123_crm_sub_rwa_not_cov_crm = r123_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR123_crm_comp_col_expo_elig() {
		return r123_crm_comp_col_expo_elig;
	}
	public void setR123_crm_comp_col_expo_elig(BigDecimal r123_crm_comp_col_expo_elig) {
		this.r123_crm_comp_col_expo_elig = r123_crm_comp_col_expo_elig;
	}
	public BigDecimal getR123_crm_comp_col_elig_expo_vol_adj() {
		return r123_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR123_crm_comp_col_elig_expo_vol_adj(BigDecimal r123_crm_comp_col_elig_expo_vol_adj) {
		this.r123_crm_comp_col_elig_expo_vol_adj = r123_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR123_crm_comp_col_elig_fin_hai() {
		return r123_crm_comp_col_elig_fin_hai;
	}
	public void setR123_crm_comp_col_elig_fin_hai(BigDecimal r123_crm_comp_col_elig_fin_hai) {
		this.r123_crm_comp_col_elig_fin_hai = r123_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR123_crm_comp_col_expo_val() {
		return r123_crm_comp_col_expo_val;
	}
	public void setR123_crm_comp_col_expo_val(BigDecimal r123_crm_comp_col_expo_val) {
		this.r123_crm_comp_col_expo_val = r123_crm_comp_col_expo_val;
	}
	public BigDecimal getR123_rwa_elig_expo_not_cov_crm() {
		return r123_rwa_elig_expo_not_cov_crm;
	}
	public void setR123_rwa_elig_expo_not_cov_crm(BigDecimal r123_rwa_elig_expo_not_cov_crm) {
		this.r123_rwa_elig_expo_not_cov_crm = r123_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR123_rwa_unsec_expo_cre_ris() {
		return r123_rwa_unsec_expo_cre_ris;
	}
	public void setR123_rwa_unsec_expo_cre_ris(BigDecimal r123_rwa_unsec_expo_cre_ris) {
		this.r123_rwa_unsec_expo_cre_ris = r123_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR123_rwa_unsec_expo() {
		return r123_rwa_unsec_expo;
	}
	public void setR123_rwa_unsec_expo(BigDecimal r123_rwa_unsec_expo) {
		this.r123_rwa_unsec_expo = r123_rwa_unsec_expo;
	}
	public BigDecimal getR123_rwa_tot_ris_wei_ass() {
		return r123_rwa_tot_ris_wei_ass;
	}
	public void setR123_rwa_tot_ris_wei_ass(BigDecimal r123_rwa_tot_ris_wei_ass) {
		this.r123_rwa_tot_ris_wei_ass = r123_rwa_tot_ris_wei_ass;
	}
	public String getR124_exposure_class() {
		return r124_exposure_class;
	}
	public void setR124_exposure_class(String r124_exposure_class) {
		this.r124_exposure_class = r124_exposure_class;
	}
	public BigDecimal getR124_expo_crm() {
		return r124_expo_crm;
	}
	public void setR124_expo_crm(BigDecimal r124_expo_crm) {
		this.r124_expo_crm = r124_expo_crm;
	}
	public BigDecimal getR124_spe_pro_expo() {
		return r124_spe_pro_expo;
	}
	public void setR124_spe_pro_expo(BigDecimal r124_spe_pro_expo) {
		this.r124_spe_pro_expo = r124_spe_pro_expo;
	}
	public BigDecimal getR124_amt_elig_sht_net() {
		return r124_amt_elig_sht_net;
	}
	public void setR124_amt_elig_sht_net(BigDecimal r124_amt_elig_sht_net) {
		this.r124_amt_elig_sht_net = r124_amt_elig_sht_net;
	}
	public BigDecimal getR124_tot_expo_net_spe() {
		return r124_tot_expo_net_spe;
	}
	public void setR124_tot_expo_net_spe(BigDecimal r124_tot_expo_net_spe) {
		this.r124_tot_expo_net_spe = r124_tot_expo_net_spe;
	}
	public BigDecimal getR124_crm_sub_elig_sub_app() {
		return r124_crm_sub_elig_sub_app;
	}
	public void setR124_crm_sub_elig_sub_app(BigDecimal r124_crm_sub_elig_sub_app) {
		this.r124_crm_sub_elig_sub_app = r124_crm_sub_elig_sub_app;
	}
	public BigDecimal getR124_crm_sub_non_col_guar() {
		return r124_crm_sub_non_col_guar;
	}
	public void setR124_crm_sub_non_col_guar(BigDecimal r124_crm_sub_non_col_guar) {
		this.r124_crm_sub_non_col_guar = r124_crm_sub_non_col_guar;
	}
	public BigDecimal getR124_crm_sub_non_col_cre_der() {
		return r124_crm_sub_non_col_cre_der;
	}
	public void setR124_crm_sub_non_col_cre_der(BigDecimal r124_crm_sub_non_col_cre_der) {
		this.r124_crm_sub_non_col_cre_der = r124_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR124_crm_sub_col_elig_cash() {
		return r124_crm_sub_col_elig_cash;
	}
	public void setR124_crm_sub_col_elig_cash(BigDecimal r124_crm_sub_col_elig_cash) {
		this.r124_crm_sub_col_elig_cash = r124_crm_sub_col_elig_cash;
	}
	public BigDecimal getR124_crm_sub_col_elig_trea_bills() {
		return r124_crm_sub_col_elig_trea_bills;
	}
	public void setR124_crm_sub_col_elig_trea_bills(BigDecimal r124_crm_sub_col_elig_trea_bills) {
		this.r124_crm_sub_col_elig_trea_bills = r124_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR124_crm_sub_col_elig_deb_sec() {
		return r124_crm_sub_col_elig_deb_sec;
	}
	public void setR124_crm_sub_col_elig_deb_sec(BigDecimal r124_crm_sub_col_elig_deb_sec) {
		this.r124_crm_sub_col_elig_deb_sec = r124_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR124_crm_sub_col_elig_equi() {
		return r124_crm_sub_col_elig_equi;
	}
	public void setR124_crm_sub_col_elig_equi(BigDecimal r124_crm_sub_col_elig_equi) {
		this.r124_crm_sub_col_elig_equi = r124_crm_sub_col_elig_equi;
	}
	public BigDecimal getR124_crm_sub_col_elig_unit_tru() {
		return r124_crm_sub_col_elig_unit_tru;
	}
	public void setR124_crm_sub_col_elig_unit_tru(BigDecimal r124_crm_sub_col_elig_unit_tru) {
		this.r124_crm_sub_col_elig_unit_tru = r124_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR124_crm_sub_col_exp_cov() {
		return r124_crm_sub_col_exp_cov;
	}
	public void setR124_crm_sub_col_exp_cov(BigDecimal r124_crm_sub_col_exp_cov) {
		this.r124_crm_sub_col_exp_cov = r124_crm_sub_col_exp_cov;
	}
	public BigDecimal getR124_crm_sub_col_elig_exp_not_cov() {
		return r124_crm_sub_col_elig_exp_not_cov;
	}
	public void setR124_crm_sub_col_elig_exp_not_cov(BigDecimal r124_crm_sub_col_elig_exp_not_cov) {
		this.r124_crm_sub_col_elig_exp_not_cov = r124_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR124_crm_sub_rwa_ris_crm() {
		return r124_crm_sub_rwa_ris_crm;
	}
	public void setR124_crm_sub_rwa_ris_crm(BigDecimal r124_crm_sub_rwa_ris_crm) {
		this.r124_crm_sub_rwa_ris_crm = r124_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR124_crm_sub_rwa_cov_crm() {
		return r124_crm_sub_rwa_cov_crm;
	}
	public void setR124_crm_sub_rwa_cov_crm(BigDecimal r124_crm_sub_rwa_cov_crm) {
		this.r124_crm_sub_rwa_cov_crm = r124_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR124_crm_sub_rwa_org_cou() {
		return r124_crm_sub_rwa_org_cou;
	}
	public void setR124_crm_sub_rwa_org_cou(BigDecimal r124_crm_sub_rwa_org_cou) {
		this.r124_crm_sub_rwa_org_cou = r124_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR124_crm_sub_rwa_not_cov_crm() {
		return r124_crm_sub_rwa_not_cov_crm;
	}
	public void setR124_crm_sub_rwa_not_cov_crm(BigDecimal r124_crm_sub_rwa_not_cov_crm) {
		this.r124_crm_sub_rwa_not_cov_crm = r124_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR124_crm_comp_col_expo_elig() {
		return r124_crm_comp_col_expo_elig;
	}
	public void setR124_crm_comp_col_expo_elig(BigDecimal r124_crm_comp_col_expo_elig) {
		this.r124_crm_comp_col_expo_elig = r124_crm_comp_col_expo_elig;
	}
	public BigDecimal getR124_crm_comp_col_elig_expo_vol_adj() {
		return r124_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR124_crm_comp_col_elig_expo_vol_adj(BigDecimal r124_crm_comp_col_elig_expo_vol_adj) {
		this.r124_crm_comp_col_elig_expo_vol_adj = r124_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR124_crm_comp_col_elig_fin_hai() {
		return r124_crm_comp_col_elig_fin_hai;
	}
	public void setR124_crm_comp_col_elig_fin_hai(BigDecimal r124_crm_comp_col_elig_fin_hai) {
		this.r124_crm_comp_col_elig_fin_hai = r124_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR124_crm_comp_col_expo_val() {
		return r124_crm_comp_col_expo_val;
	}
	public void setR124_crm_comp_col_expo_val(BigDecimal r124_crm_comp_col_expo_val) {
		this.r124_crm_comp_col_expo_val = r124_crm_comp_col_expo_val;
	}
	public BigDecimal getR124_rwa_elig_expo_not_cov_crm() {
		return r124_rwa_elig_expo_not_cov_crm;
	}
	public void setR124_rwa_elig_expo_not_cov_crm(BigDecimal r124_rwa_elig_expo_not_cov_crm) {
		this.r124_rwa_elig_expo_not_cov_crm = r124_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR124_rwa_unsec_expo_cre_ris() {
		return r124_rwa_unsec_expo_cre_ris;
	}
	public void setR124_rwa_unsec_expo_cre_ris(BigDecimal r124_rwa_unsec_expo_cre_ris) {
		this.r124_rwa_unsec_expo_cre_ris = r124_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR124_rwa_unsec_expo() {
		return r124_rwa_unsec_expo;
	}
	public void setR124_rwa_unsec_expo(BigDecimal r124_rwa_unsec_expo) {
		this.r124_rwa_unsec_expo = r124_rwa_unsec_expo;
	}
	public BigDecimal getR124_rwa_tot_ris_wei_ass() {
		return r124_rwa_tot_ris_wei_ass;
	}
	public void setR124_rwa_tot_ris_wei_ass(BigDecimal r124_rwa_tot_ris_wei_ass) {
		this.r124_rwa_tot_ris_wei_ass = r124_rwa_tot_ris_wei_ass;
	}
	public String getR125_exposure_class() {
		return r125_exposure_class;
	}
	public void setR125_exposure_class(String r125_exposure_class) {
		this.r125_exposure_class = r125_exposure_class;
	}
	public BigDecimal getR125_expo_crm() {
		return r125_expo_crm;
	}
	public void setR125_expo_crm(BigDecimal r125_expo_crm) {
		this.r125_expo_crm = r125_expo_crm;
	}
	public BigDecimal getR125_spe_pro_expo() {
		return r125_spe_pro_expo;
	}
	public void setR125_spe_pro_expo(BigDecimal r125_spe_pro_expo) {
		this.r125_spe_pro_expo = r125_spe_pro_expo;
	}
	public BigDecimal getR125_amt_elig_sht_net() {
		return r125_amt_elig_sht_net;
	}
	public void setR125_amt_elig_sht_net(BigDecimal r125_amt_elig_sht_net) {
		this.r125_amt_elig_sht_net = r125_amt_elig_sht_net;
	}
	public BigDecimal getR125_tot_expo_net_spe() {
		return r125_tot_expo_net_spe;
	}
	public void setR125_tot_expo_net_spe(BigDecimal r125_tot_expo_net_spe) {
		this.r125_tot_expo_net_spe = r125_tot_expo_net_spe;
	}
	public BigDecimal getR125_crm_sub_elig_sub_app() {
		return r125_crm_sub_elig_sub_app;
	}
	public void setR125_crm_sub_elig_sub_app(BigDecimal r125_crm_sub_elig_sub_app) {
		this.r125_crm_sub_elig_sub_app = r125_crm_sub_elig_sub_app;
	}
	public BigDecimal getR125_crm_sub_non_col_guar() {
		return r125_crm_sub_non_col_guar;
	}
	public void setR125_crm_sub_non_col_guar(BigDecimal r125_crm_sub_non_col_guar) {
		this.r125_crm_sub_non_col_guar = r125_crm_sub_non_col_guar;
	}
	public BigDecimal getR125_crm_sub_non_col_cre_der() {
		return r125_crm_sub_non_col_cre_der;
	}
	public void setR125_crm_sub_non_col_cre_der(BigDecimal r125_crm_sub_non_col_cre_der) {
		this.r125_crm_sub_non_col_cre_der = r125_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR125_crm_sub_col_elig_cash() {
		return r125_crm_sub_col_elig_cash;
	}
	public void setR125_crm_sub_col_elig_cash(BigDecimal r125_crm_sub_col_elig_cash) {
		this.r125_crm_sub_col_elig_cash = r125_crm_sub_col_elig_cash;
	}
	public BigDecimal getR125_crm_sub_col_elig_trea_bills() {
		return r125_crm_sub_col_elig_trea_bills;
	}
	public void setR125_crm_sub_col_elig_trea_bills(BigDecimal r125_crm_sub_col_elig_trea_bills) {
		this.r125_crm_sub_col_elig_trea_bills = r125_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR125_crm_sub_col_elig_deb_sec() {
		return r125_crm_sub_col_elig_deb_sec;
	}
	public void setR125_crm_sub_col_elig_deb_sec(BigDecimal r125_crm_sub_col_elig_deb_sec) {
		this.r125_crm_sub_col_elig_deb_sec = r125_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR125_crm_sub_col_elig_equi() {
		return r125_crm_sub_col_elig_equi;
	}
	public void setR125_crm_sub_col_elig_equi(BigDecimal r125_crm_sub_col_elig_equi) {
		this.r125_crm_sub_col_elig_equi = r125_crm_sub_col_elig_equi;
	}
	public BigDecimal getR125_crm_sub_col_elig_unit_tru() {
		return r125_crm_sub_col_elig_unit_tru;
	}
	public void setR125_crm_sub_col_elig_unit_tru(BigDecimal r125_crm_sub_col_elig_unit_tru) {
		this.r125_crm_sub_col_elig_unit_tru = r125_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR125_crm_sub_col_exp_cov() {
		return r125_crm_sub_col_exp_cov;
	}
	public void setR125_crm_sub_col_exp_cov(BigDecimal r125_crm_sub_col_exp_cov) {
		this.r125_crm_sub_col_exp_cov = r125_crm_sub_col_exp_cov;
	}
	public BigDecimal getR125_crm_sub_col_elig_exp_not_cov() {
		return r125_crm_sub_col_elig_exp_not_cov;
	}
	public void setR125_crm_sub_col_elig_exp_not_cov(BigDecimal r125_crm_sub_col_elig_exp_not_cov) {
		this.r125_crm_sub_col_elig_exp_not_cov = r125_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR125_crm_sub_rwa_ris_crm() {
		return r125_crm_sub_rwa_ris_crm;
	}
	public void setR125_crm_sub_rwa_ris_crm(BigDecimal r125_crm_sub_rwa_ris_crm) {
		this.r125_crm_sub_rwa_ris_crm = r125_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR125_crm_sub_rwa_cov_crm() {
		return r125_crm_sub_rwa_cov_crm;
	}
	public void setR125_crm_sub_rwa_cov_crm(BigDecimal r125_crm_sub_rwa_cov_crm) {
		this.r125_crm_sub_rwa_cov_crm = r125_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR125_crm_sub_rwa_org_cou() {
		return r125_crm_sub_rwa_org_cou;
	}
	public void setR125_crm_sub_rwa_org_cou(BigDecimal r125_crm_sub_rwa_org_cou) {
		this.r125_crm_sub_rwa_org_cou = r125_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR125_crm_sub_rwa_not_cov_crm() {
		return r125_crm_sub_rwa_not_cov_crm;
	}
	public void setR125_crm_sub_rwa_not_cov_crm(BigDecimal r125_crm_sub_rwa_not_cov_crm) {
		this.r125_crm_sub_rwa_not_cov_crm = r125_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR125_crm_comp_col_expo_elig() {
		return r125_crm_comp_col_expo_elig;
	}
	public void setR125_crm_comp_col_expo_elig(BigDecimal r125_crm_comp_col_expo_elig) {
		this.r125_crm_comp_col_expo_elig = r125_crm_comp_col_expo_elig;
	}
	public BigDecimal getR125_crm_comp_col_elig_expo_vol_adj() {
		return r125_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR125_crm_comp_col_elig_expo_vol_adj(BigDecimal r125_crm_comp_col_elig_expo_vol_adj) {
		this.r125_crm_comp_col_elig_expo_vol_adj = r125_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR125_crm_comp_col_elig_fin_hai() {
		return r125_crm_comp_col_elig_fin_hai;
	}
	public void setR125_crm_comp_col_elig_fin_hai(BigDecimal r125_crm_comp_col_elig_fin_hai) {
		this.r125_crm_comp_col_elig_fin_hai = r125_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR125_crm_comp_col_expo_val() {
		return r125_crm_comp_col_expo_val;
	}
	public void setR125_crm_comp_col_expo_val(BigDecimal r125_crm_comp_col_expo_val) {
		this.r125_crm_comp_col_expo_val = r125_crm_comp_col_expo_val;
	}
	public BigDecimal getR125_rwa_elig_expo_not_cov_crm() {
		return r125_rwa_elig_expo_not_cov_crm;
	}
	public void setR125_rwa_elig_expo_not_cov_crm(BigDecimal r125_rwa_elig_expo_not_cov_crm) {
		this.r125_rwa_elig_expo_not_cov_crm = r125_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR125_rwa_unsec_expo_cre_ris() {
		return r125_rwa_unsec_expo_cre_ris;
	}
	public void setR125_rwa_unsec_expo_cre_ris(BigDecimal r125_rwa_unsec_expo_cre_ris) {
		this.r125_rwa_unsec_expo_cre_ris = r125_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR125_rwa_unsec_expo() {
		return r125_rwa_unsec_expo;
	}
	public void setR125_rwa_unsec_expo(BigDecimal r125_rwa_unsec_expo) {
		this.r125_rwa_unsec_expo = r125_rwa_unsec_expo;
	}
	public BigDecimal getR125_rwa_tot_ris_wei_ass() {
		return r125_rwa_tot_ris_wei_ass;
	}
	public void setR125_rwa_tot_ris_wei_ass(BigDecimal r125_rwa_tot_ris_wei_ass) {
		this.r125_rwa_tot_ris_wei_ass = r125_rwa_tot_ris_wei_ass;
	}
	public String getR126_exposure_class() {
		return r126_exposure_class;
	}
	public void setR126_exposure_class(String r126_exposure_class) {
		this.r126_exposure_class = r126_exposure_class;
	}
	public BigDecimal getR126_expo_crm() {
		return r126_expo_crm;
	}
	public void setR126_expo_crm(BigDecimal r126_expo_crm) {
		this.r126_expo_crm = r126_expo_crm;
	}
	public BigDecimal getR126_spe_pro_expo() {
		return r126_spe_pro_expo;
	}
	public void setR126_spe_pro_expo(BigDecimal r126_spe_pro_expo) {
		this.r126_spe_pro_expo = r126_spe_pro_expo;
	}
	public BigDecimal getR126_amt_elig_sht_net() {
		return r126_amt_elig_sht_net;
	}
	public void setR126_amt_elig_sht_net(BigDecimal r126_amt_elig_sht_net) {
		this.r126_amt_elig_sht_net = r126_amt_elig_sht_net;
	}
	public BigDecimal getR126_tot_expo_net_spe() {
		return r126_tot_expo_net_spe;
	}
	public void setR126_tot_expo_net_spe(BigDecimal r126_tot_expo_net_spe) {
		this.r126_tot_expo_net_spe = r126_tot_expo_net_spe;
	}
	public BigDecimal getR126_crm_sub_elig_sub_app() {
		return r126_crm_sub_elig_sub_app;
	}
	public void setR126_crm_sub_elig_sub_app(BigDecimal r126_crm_sub_elig_sub_app) {
		this.r126_crm_sub_elig_sub_app = r126_crm_sub_elig_sub_app;
	}
	public BigDecimal getR126_crm_sub_non_col_guar() {
		return r126_crm_sub_non_col_guar;
	}
	public void setR126_crm_sub_non_col_guar(BigDecimal r126_crm_sub_non_col_guar) {
		this.r126_crm_sub_non_col_guar = r126_crm_sub_non_col_guar;
	}
	public BigDecimal getR126_crm_sub_non_col_cre_der() {
		return r126_crm_sub_non_col_cre_der;
	}
	public void setR126_crm_sub_non_col_cre_der(BigDecimal r126_crm_sub_non_col_cre_der) {
		this.r126_crm_sub_non_col_cre_der = r126_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR126_crm_sub_col_elig_cash() {
		return r126_crm_sub_col_elig_cash;
	}
	public void setR126_crm_sub_col_elig_cash(BigDecimal r126_crm_sub_col_elig_cash) {
		this.r126_crm_sub_col_elig_cash = r126_crm_sub_col_elig_cash;
	}
	public BigDecimal getR126_crm_sub_col_elig_trea_bills() {
		return r126_crm_sub_col_elig_trea_bills;
	}
	public void setR126_crm_sub_col_elig_trea_bills(BigDecimal r126_crm_sub_col_elig_trea_bills) {
		this.r126_crm_sub_col_elig_trea_bills = r126_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR126_crm_sub_col_elig_deb_sec() {
		return r126_crm_sub_col_elig_deb_sec;
	}
	public void setR126_crm_sub_col_elig_deb_sec(BigDecimal r126_crm_sub_col_elig_deb_sec) {
		this.r126_crm_sub_col_elig_deb_sec = r126_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR126_crm_sub_col_elig_equi() {
		return r126_crm_sub_col_elig_equi;
	}
	public void setR126_crm_sub_col_elig_equi(BigDecimal r126_crm_sub_col_elig_equi) {
		this.r126_crm_sub_col_elig_equi = r126_crm_sub_col_elig_equi;
	}
	public BigDecimal getR126_crm_sub_col_elig_unit_tru() {
		return r126_crm_sub_col_elig_unit_tru;
	}
	public void setR126_crm_sub_col_elig_unit_tru(BigDecimal r126_crm_sub_col_elig_unit_tru) {
		this.r126_crm_sub_col_elig_unit_tru = r126_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR126_crm_sub_col_exp_cov() {
		return r126_crm_sub_col_exp_cov;
	}
	public void setR126_crm_sub_col_exp_cov(BigDecimal r126_crm_sub_col_exp_cov) {
		this.r126_crm_sub_col_exp_cov = r126_crm_sub_col_exp_cov;
	}
	public BigDecimal getR126_crm_sub_col_elig_exp_not_cov() {
		return r126_crm_sub_col_elig_exp_not_cov;
	}
	public void setR126_crm_sub_col_elig_exp_not_cov(BigDecimal r126_crm_sub_col_elig_exp_not_cov) {
		this.r126_crm_sub_col_elig_exp_not_cov = r126_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR126_crm_sub_rwa_ris_crm() {
		return r126_crm_sub_rwa_ris_crm;
	}
	public void setR126_crm_sub_rwa_ris_crm(BigDecimal r126_crm_sub_rwa_ris_crm) {
		this.r126_crm_sub_rwa_ris_crm = r126_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR126_crm_sub_rwa_cov_crm() {
		return r126_crm_sub_rwa_cov_crm;
	}
	public void setR126_crm_sub_rwa_cov_crm(BigDecimal r126_crm_sub_rwa_cov_crm) {
		this.r126_crm_sub_rwa_cov_crm = r126_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR126_crm_sub_rwa_org_cou() {
		return r126_crm_sub_rwa_org_cou;
	}
	public void setR126_crm_sub_rwa_org_cou(BigDecimal r126_crm_sub_rwa_org_cou) {
		this.r126_crm_sub_rwa_org_cou = r126_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR126_crm_sub_rwa_not_cov_crm() {
		return r126_crm_sub_rwa_not_cov_crm;
	}
	public void setR126_crm_sub_rwa_not_cov_crm(BigDecimal r126_crm_sub_rwa_not_cov_crm) {
		this.r126_crm_sub_rwa_not_cov_crm = r126_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR126_crm_comp_col_expo_elig() {
		return r126_crm_comp_col_expo_elig;
	}
	public void setR126_crm_comp_col_expo_elig(BigDecimal r126_crm_comp_col_expo_elig) {
		this.r126_crm_comp_col_expo_elig = r126_crm_comp_col_expo_elig;
	}
	public BigDecimal getR126_crm_comp_col_elig_expo_vol_adj() {
		return r126_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR126_crm_comp_col_elig_expo_vol_adj(BigDecimal r126_crm_comp_col_elig_expo_vol_adj) {
		this.r126_crm_comp_col_elig_expo_vol_adj = r126_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR126_crm_comp_col_elig_fin_hai() {
		return r126_crm_comp_col_elig_fin_hai;
	}
	public void setR126_crm_comp_col_elig_fin_hai(BigDecimal r126_crm_comp_col_elig_fin_hai) {
		this.r126_crm_comp_col_elig_fin_hai = r126_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR126_crm_comp_col_expo_val() {
		return r126_crm_comp_col_expo_val;
	}
	public void setR126_crm_comp_col_expo_val(BigDecimal r126_crm_comp_col_expo_val) {
		this.r126_crm_comp_col_expo_val = r126_crm_comp_col_expo_val;
	}
	public BigDecimal getR126_rwa_elig_expo_not_cov_crm() {
		return r126_rwa_elig_expo_not_cov_crm;
	}
	public void setR126_rwa_elig_expo_not_cov_crm(BigDecimal r126_rwa_elig_expo_not_cov_crm) {
		this.r126_rwa_elig_expo_not_cov_crm = r126_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR126_rwa_unsec_expo_cre_ris() {
		return r126_rwa_unsec_expo_cre_ris;
	}
	public void setR126_rwa_unsec_expo_cre_ris(BigDecimal r126_rwa_unsec_expo_cre_ris) {
		this.r126_rwa_unsec_expo_cre_ris = r126_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR126_rwa_unsec_expo() {
		return r126_rwa_unsec_expo;
	}
	public void setR126_rwa_unsec_expo(BigDecimal r126_rwa_unsec_expo) {
		this.r126_rwa_unsec_expo = r126_rwa_unsec_expo;
	}
	public BigDecimal getR126_rwa_tot_ris_wei_ass() {
		return r126_rwa_tot_ris_wei_ass;
	}
	public void setR126_rwa_tot_ris_wei_ass(BigDecimal r126_rwa_tot_ris_wei_ass) {
		this.r126_rwa_tot_ris_wei_ass = r126_rwa_tot_ris_wei_ass;
	}
	public String getR127_exposure_class() {
		return r127_exposure_class;
	}
	public void setR127_exposure_class(String r127_exposure_class) {
		this.r127_exposure_class = r127_exposure_class;
	}
	public BigDecimal getR127_expo_crm() {
		return r127_expo_crm;
	}
	public void setR127_expo_crm(BigDecimal r127_expo_crm) {
		this.r127_expo_crm = r127_expo_crm;
	}
	public BigDecimal getR127_spe_pro_expo() {
		return r127_spe_pro_expo;
	}
	public void setR127_spe_pro_expo(BigDecimal r127_spe_pro_expo) {
		this.r127_spe_pro_expo = r127_spe_pro_expo;
	}
	public BigDecimal getR127_amt_elig_sht_net() {
		return r127_amt_elig_sht_net;
	}
	public void setR127_amt_elig_sht_net(BigDecimal r127_amt_elig_sht_net) {
		this.r127_amt_elig_sht_net = r127_amt_elig_sht_net;
	}
	public BigDecimal getR127_tot_expo_net_spe() {
		return r127_tot_expo_net_spe;
	}
	public void setR127_tot_expo_net_spe(BigDecimal r127_tot_expo_net_spe) {
		this.r127_tot_expo_net_spe = r127_tot_expo_net_spe;
	}
	public BigDecimal getR127_crm_sub_elig_sub_app() {
		return r127_crm_sub_elig_sub_app;
	}
	public void setR127_crm_sub_elig_sub_app(BigDecimal r127_crm_sub_elig_sub_app) {
		this.r127_crm_sub_elig_sub_app = r127_crm_sub_elig_sub_app;
	}
	public BigDecimal getR127_crm_sub_non_col_guar() {
		return r127_crm_sub_non_col_guar;
	}
	public void setR127_crm_sub_non_col_guar(BigDecimal r127_crm_sub_non_col_guar) {
		this.r127_crm_sub_non_col_guar = r127_crm_sub_non_col_guar;
	}
	public BigDecimal getR127_crm_sub_non_col_cre_der() {
		return r127_crm_sub_non_col_cre_der;
	}
	public void setR127_crm_sub_non_col_cre_der(BigDecimal r127_crm_sub_non_col_cre_der) {
		this.r127_crm_sub_non_col_cre_der = r127_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR127_crm_sub_col_elig_cash() {
		return r127_crm_sub_col_elig_cash;
	}
	public void setR127_crm_sub_col_elig_cash(BigDecimal r127_crm_sub_col_elig_cash) {
		this.r127_crm_sub_col_elig_cash = r127_crm_sub_col_elig_cash;
	}
	public BigDecimal getR127_crm_sub_col_elig_trea_bills() {
		return r127_crm_sub_col_elig_trea_bills;
	}
	public void setR127_crm_sub_col_elig_trea_bills(BigDecimal r127_crm_sub_col_elig_trea_bills) {
		this.r127_crm_sub_col_elig_trea_bills = r127_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR127_crm_sub_col_elig_deb_sec() {
		return r127_crm_sub_col_elig_deb_sec;
	}
	public void setR127_crm_sub_col_elig_deb_sec(BigDecimal r127_crm_sub_col_elig_deb_sec) {
		this.r127_crm_sub_col_elig_deb_sec = r127_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR127_crm_sub_col_elig_equi() {
		return r127_crm_sub_col_elig_equi;
	}
	public void setR127_crm_sub_col_elig_equi(BigDecimal r127_crm_sub_col_elig_equi) {
		this.r127_crm_sub_col_elig_equi = r127_crm_sub_col_elig_equi;
	}
	public BigDecimal getR127_crm_sub_col_elig_unit_tru() {
		return r127_crm_sub_col_elig_unit_tru;
	}
	public void setR127_crm_sub_col_elig_unit_tru(BigDecimal r127_crm_sub_col_elig_unit_tru) {
		this.r127_crm_sub_col_elig_unit_tru = r127_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR127_crm_sub_col_exp_cov() {
		return r127_crm_sub_col_exp_cov;
	}
	public void setR127_crm_sub_col_exp_cov(BigDecimal r127_crm_sub_col_exp_cov) {
		this.r127_crm_sub_col_exp_cov = r127_crm_sub_col_exp_cov;
	}
	public BigDecimal getR127_crm_sub_col_elig_exp_not_cov() {
		return r127_crm_sub_col_elig_exp_not_cov;
	}
	public void setR127_crm_sub_col_elig_exp_not_cov(BigDecimal r127_crm_sub_col_elig_exp_not_cov) {
		this.r127_crm_sub_col_elig_exp_not_cov = r127_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR127_crm_sub_rwa_ris_crm() {
		return r127_crm_sub_rwa_ris_crm;
	}
	public void setR127_crm_sub_rwa_ris_crm(BigDecimal r127_crm_sub_rwa_ris_crm) {
		this.r127_crm_sub_rwa_ris_crm = r127_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR127_crm_sub_rwa_cov_crm() {
		return r127_crm_sub_rwa_cov_crm;
	}
	public void setR127_crm_sub_rwa_cov_crm(BigDecimal r127_crm_sub_rwa_cov_crm) {
		this.r127_crm_sub_rwa_cov_crm = r127_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR127_crm_sub_rwa_org_cou() {
		return r127_crm_sub_rwa_org_cou;
	}
	public void setR127_crm_sub_rwa_org_cou(BigDecimal r127_crm_sub_rwa_org_cou) {
		this.r127_crm_sub_rwa_org_cou = r127_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR127_crm_sub_rwa_not_cov_crm() {
		return r127_crm_sub_rwa_not_cov_crm;
	}
	public void setR127_crm_sub_rwa_not_cov_crm(BigDecimal r127_crm_sub_rwa_not_cov_crm) {
		this.r127_crm_sub_rwa_not_cov_crm = r127_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR127_crm_comp_col_expo_elig() {
		return r127_crm_comp_col_expo_elig;
	}
	public void setR127_crm_comp_col_expo_elig(BigDecimal r127_crm_comp_col_expo_elig) {
		this.r127_crm_comp_col_expo_elig = r127_crm_comp_col_expo_elig;
	}
	public BigDecimal getR127_crm_comp_col_elig_expo_vol_adj() {
		return r127_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR127_crm_comp_col_elig_expo_vol_adj(BigDecimal r127_crm_comp_col_elig_expo_vol_adj) {
		this.r127_crm_comp_col_elig_expo_vol_adj = r127_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR127_crm_comp_col_elig_fin_hai() {
		return r127_crm_comp_col_elig_fin_hai;
	}
	public void setR127_crm_comp_col_elig_fin_hai(BigDecimal r127_crm_comp_col_elig_fin_hai) {
		this.r127_crm_comp_col_elig_fin_hai = r127_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR127_crm_comp_col_expo_val() {
		return r127_crm_comp_col_expo_val;
	}
	public void setR127_crm_comp_col_expo_val(BigDecimal r127_crm_comp_col_expo_val) {
		this.r127_crm_comp_col_expo_val = r127_crm_comp_col_expo_val;
	}
	public BigDecimal getR127_rwa_elig_expo_not_cov_crm() {
		return r127_rwa_elig_expo_not_cov_crm;
	}
	public void setR127_rwa_elig_expo_not_cov_crm(BigDecimal r127_rwa_elig_expo_not_cov_crm) {
		this.r127_rwa_elig_expo_not_cov_crm = r127_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR127_rwa_unsec_expo_cre_ris() {
		return r127_rwa_unsec_expo_cre_ris;
	}
	public void setR127_rwa_unsec_expo_cre_ris(BigDecimal r127_rwa_unsec_expo_cre_ris) {
		this.r127_rwa_unsec_expo_cre_ris = r127_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR127_rwa_unsec_expo() {
		return r127_rwa_unsec_expo;
	}
	public void setR127_rwa_unsec_expo(BigDecimal r127_rwa_unsec_expo) {
		this.r127_rwa_unsec_expo = r127_rwa_unsec_expo;
	}
	public BigDecimal getR127_rwa_tot_ris_wei_ass() {
		return r127_rwa_tot_ris_wei_ass;
	}
	public void setR127_rwa_tot_ris_wei_ass(BigDecimal r127_rwa_tot_ris_wei_ass) {
		this.r127_rwa_tot_ris_wei_ass = r127_rwa_tot_ris_wei_ass;
	}
	public String getR128_exposure_class() {
		return r128_exposure_class;
	}
	public void setR128_exposure_class(String r128_exposure_class) {
		this.r128_exposure_class = r128_exposure_class;
	}
	public BigDecimal getR128_expo_crm() {
		return r128_expo_crm;
	}
	public void setR128_expo_crm(BigDecimal r128_expo_crm) {
		this.r128_expo_crm = r128_expo_crm;
	}
	public BigDecimal getR128_spe_pro_expo() {
		return r128_spe_pro_expo;
	}
	public void setR128_spe_pro_expo(BigDecimal r128_spe_pro_expo) {
		this.r128_spe_pro_expo = r128_spe_pro_expo;
	}
	public BigDecimal getR128_amt_elig_sht_net() {
		return r128_amt_elig_sht_net;
	}
	public void setR128_amt_elig_sht_net(BigDecimal r128_amt_elig_sht_net) {
		this.r128_amt_elig_sht_net = r128_amt_elig_sht_net;
	}
	public BigDecimal getR128_tot_expo_net_spe() {
		return r128_tot_expo_net_spe;
	}
	public void setR128_tot_expo_net_spe(BigDecimal r128_tot_expo_net_spe) {
		this.r128_tot_expo_net_spe = r128_tot_expo_net_spe;
	}
	public BigDecimal getR128_crm_sub_elig_sub_app() {
		return r128_crm_sub_elig_sub_app;
	}
	public void setR128_crm_sub_elig_sub_app(BigDecimal r128_crm_sub_elig_sub_app) {
		this.r128_crm_sub_elig_sub_app = r128_crm_sub_elig_sub_app;
	}
	public BigDecimal getR128_crm_sub_non_col_guar() {
		return r128_crm_sub_non_col_guar;
	}
	public void setR128_crm_sub_non_col_guar(BigDecimal r128_crm_sub_non_col_guar) {
		this.r128_crm_sub_non_col_guar = r128_crm_sub_non_col_guar;
	}
	public BigDecimal getR128_crm_sub_non_col_cre_der() {
		return r128_crm_sub_non_col_cre_der;
	}
	public void setR128_crm_sub_non_col_cre_der(BigDecimal r128_crm_sub_non_col_cre_der) {
		this.r128_crm_sub_non_col_cre_der = r128_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR128_crm_sub_col_elig_cash() {
		return r128_crm_sub_col_elig_cash;
	}
	public void setR128_crm_sub_col_elig_cash(BigDecimal r128_crm_sub_col_elig_cash) {
		this.r128_crm_sub_col_elig_cash = r128_crm_sub_col_elig_cash;
	}
	public BigDecimal getR128_crm_sub_col_elig_trea_bills() {
		return r128_crm_sub_col_elig_trea_bills;
	}
	public void setR128_crm_sub_col_elig_trea_bills(BigDecimal r128_crm_sub_col_elig_trea_bills) {
		this.r128_crm_sub_col_elig_trea_bills = r128_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR128_crm_sub_col_elig_deb_sec() {
		return r128_crm_sub_col_elig_deb_sec;
	}
	public void setR128_crm_sub_col_elig_deb_sec(BigDecimal r128_crm_sub_col_elig_deb_sec) {
		this.r128_crm_sub_col_elig_deb_sec = r128_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR128_crm_sub_col_elig_equi() {
		return r128_crm_sub_col_elig_equi;
	}
	public void setR128_crm_sub_col_elig_equi(BigDecimal r128_crm_sub_col_elig_equi) {
		this.r128_crm_sub_col_elig_equi = r128_crm_sub_col_elig_equi;
	}
	public BigDecimal getR128_crm_sub_col_elig_unit_tru() {
		return r128_crm_sub_col_elig_unit_tru;
	}
	public void setR128_crm_sub_col_elig_unit_tru(BigDecimal r128_crm_sub_col_elig_unit_tru) {
		this.r128_crm_sub_col_elig_unit_tru = r128_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR128_crm_sub_col_exp_cov() {
		return r128_crm_sub_col_exp_cov;
	}
	public void setR128_crm_sub_col_exp_cov(BigDecimal r128_crm_sub_col_exp_cov) {
		this.r128_crm_sub_col_exp_cov = r128_crm_sub_col_exp_cov;
	}
	public BigDecimal getR128_crm_sub_col_elig_exp_not_cov() {
		return r128_crm_sub_col_elig_exp_not_cov;
	}
	public void setR128_crm_sub_col_elig_exp_not_cov(BigDecimal r128_crm_sub_col_elig_exp_not_cov) {
		this.r128_crm_sub_col_elig_exp_not_cov = r128_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR128_crm_sub_rwa_ris_crm() {
		return r128_crm_sub_rwa_ris_crm;
	}
	public void setR128_crm_sub_rwa_ris_crm(BigDecimal r128_crm_sub_rwa_ris_crm) {
		this.r128_crm_sub_rwa_ris_crm = r128_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR128_crm_sub_rwa_cov_crm() {
		return r128_crm_sub_rwa_cov_crm;
	}
	public void setR128_crm_sub_rwa_cov_crm(BigDecimal r128_crm_sub_rwa_cov_crm) {
		this.r128_crm_sub_rwa_cov_crm = r128_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR128_crm_sub_rwa_org_cou() {
		return r128_crm_sub_rwa_org_cou;
	}
	public void setR128_crm_sub_rwa_org_cou(BigDecimal r128_crm_sub_rwa_org_cou) {
		this.r128_crm_sub_rwa_org_cou = r128_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR128_crm_sub_rwa_not_cov_crm() {
		return r128_crm_sub_rwa_not_cov_crm;
	}
	public void setR128_crm_sub_rwa_not_cov_crm(BigDecimal r128_crm_sub_rwa_not_cov_crm) {
		this.r128_crm_sub_rwa_not_cov_crm = r128_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR128_crm_comp_col_expo_elig() {
		return r128_crm_comp_col_expo_elig;
	}
	public void setR128_crm_comp_col_expo_elig(BigDecimal r128_crm_comp_col_expo_elig) {
		this.r128_crm_comp_col_expo_elig = r128_crm_comp_col_expo_elig;
	}
	public BigDecimal getR128_crm_comp_col_elig_expo_vol_adj() {
		return r128_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR128_crm_comp_col_elig_expo_vol_adj(BigDecimal r128_crm_comp_col_elig_expo_vol_adj) {
		this.r128_crm_comp_col_elig_expo_vol_adj = r128_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR128_crm_comp_col_elig_fin_hai() {
		return r128_crm_comp_col_elig_fin_hai;
	}
	public void setR128_crm_comp_col_elig_fin_hai(BigDecimal r128_crm_comp_col_elig_fin_hai) {
		this.r128_crm_comp_col_elig_fin_hai = r128_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR128_crm_comp_col_expo_val() {
		return r128_crm_comp_col_expo_val;
	}
	public void setR128_crm_comp_col_expo_val(BigDecimal r128_crm_comp_col_expo_val) {
		this.r128_crm_comp_col_expo_val = r128_crm_comp_col_expo_val;
	}
	public BigDecimal getR128_rwa_elig_expo_not_cov_crm() {
		return r128_rwa_elig_expo_not_cov_crm;
	}
	public void setR128_rwa_elig_expo_not_cov_crm(BigDecimal r128_rwa_elig_expo_not_cov_crm) {
		this.r128_rwa_elig_expo_not_cov_crm = r128_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR128_rwa_unsec_expo_cre_ris() {
		return r128_rwa_unsec_expo_cre_ris;
	}
	public void setR128_rwa_unsec_expo_cre_ris(BigDecimal r128_rwa_unsec_expo_cre_ris) {
		this.r128_rwa_unsec_expo_cre_ris = r128_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR128_rwa_unsec_expo() {
		return r128_rwa_unsec_expo;
	}
	public void setR128_rwa_unsec_expo(BigDecimal r128_rwa_unsec_expo) {
		this.r128_rwa_unsec_expo = r128_rwa_unsec_expo;
	}
	public BigDecimal getR128_rwa_tot_ris_wei_ass() {
		return r128_rwa_tot_ris_wei_ass;
	}
	public void setR128_rwa_tot_ris_wei_ass(BigDecimal r128_rwa_tot_ris_wei_ass) {
		this.r128_rwa_tot_ris_wei_ass = r128_rwa_tot_ris_wei_ass;
	}
	public String getR129_exposure_class() {
		return r129_exposure_class;
	}
	public void setR129_exposure_class(String r129_exposure_class) {
		this.r129_exposure_class = r129_exposure_class;
	}
	public BigDecimal getR129_expo_crm() {
		return r129_expo_crm;
	}
	public void setR129_expo_crm(BigDecimal r129_expo_crm) {
		this.r129_expo_crm = r129_expo_crm;
	}
	public BigDecimal getR129_spe_pro_expo() {
		return r129_spe_pro_expo;
	}
	public void setR129_spe_pro_expo(BigDecimal r129_spe_pro_expo) {
		this.r129_spe_pro_expo = r129_spe_pro_expo;
	}
	public BigDecimal getR129_amt_elig_sht_net() {
		return r129_amt_elig_sht_net;
	}
	public void setR129_amt_elig_sht_net(BigDecimal r129_amt_elig_sht_net) {
		this.r129_amt_elig_sht_net = r129_amt_elig_sht_net;
	}
	public BigDecimal getR129_tot_expo_net_spe() {
		return r129_tot_expo_net_spe;
	}
	public void setR129_tot_expo_net_spe(BigDecimal r129_tot_expo_net_spe) {
		this.r129_tot_expo_net_spe = r129_tot_expo_net_spe;
	}
	public BigDecimal getR129_crm_sub_elig_sub_app() {
		return r129_crm_sub_elig_sub_app;
	}
	public void setR129_crm_sub_elig_sub_app(BigDecimal r129_crm_sub_elig_sub_app) {
		this.r129_crm_sub_elig_sub_app = r129_crm_sub_elig_sub_app;
	}
	public BigDecimal getR129_crm_sub_non_col_guar() {
		return r129_crm_sub_non_col_guar;
	}
	public void setR129_crm_sub_non_col_guar(BigDecimal r129_crm_sub_non_col_guar) {
		this.r129_crm_sub_non_col_guar = r129_crm_sub_non_col_guar;
	}
	public BigDecimal getR129_crm_sub_non_col_cre_der() {
		return r129_crm_sub_non_col_cre_der;
	}
	public void setR129_crm_sub_non_col_cre_der(BigDecimal r129_crm_sub_non_col_cre_der) {
		this.r129_crm_sub_non_col_cre_der = r129_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR129_crm_sub_col_elig_cash() {
		return r129_crm_sub_col_elig_cash;
	}
	public void setR129_crm_sub_col_elig_cash(BigDecimal r129_crm_sub_col_elig_cash) {
		this.r129_crm_sub_col_elig_cash = r129_crm_sub_col_elig_cash;
	}
	public BigDecimal getR129_crm_sub_col_elig_trea_bills() {
		return r129_crm_sub_col_elig_trea_bills;
	}
	public void setR129_crm_sub_col_elig_trea_bills(BigDecimal r129_crm_sub_col_elig_trea_bills) {
		this.r129_crm_sub_col_elig_trea_bills = r129_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR129_crm_sub_col_elig_deb_sec() {
		return r129_crm_sub_col_elig_deb_sec;
	}
	public void setR129_crm_sub_col_elig_deb_sec(BigDecimal r129_crm_sub_col_elig_deb_sec) {
		this.r129_crm_sub_col_elig_deb_sec = r129_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR129_crm_sub_col_elig_equi() {
		return r129_crm_sub_col_elig_equi;
	}
	public void setR129_crm_sub_col_elig_equi(BigDecimal r129_crm_sub_col_elig_equi) {
		this.r129_crm_sub_col_elig_equi = r129_crm_sub_col_elig_equi;
	}
	public BigDecimal getR129_crm_sub_col_elig_unit_tru() {
		return r129_crm_sub_col_elig_unit_tru;
	}
	public void setR129_crm_sub_col_elig_unit_tru(BigDecimal r129_crm_sub_col_elig_unit_tru) {
		this.r129_crm_sub_col_elig_unit_tru = r129_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR129_crm_sub_col_exp_cov() {
		return r129_crm_sub_col_exp_cov;
	}
	public void setR129_crm_sub_col_exp_cov(BigDecimal r129_crm_sub_col_exp_cov) {
		this.r129_crm_sub_col_exp_cov = r129_crm_sub_col_exp_cov;
	}
	public BigDecimal getR129_crm_sub_col_elig_exp_not_cov() {
		return r129_crm_sub_col_elig_exp_not_cov;
	}
	public void setR129_crm_sub_col_elig_exp_not_cov(BigDecimal r129_crm_sub_col_elig_exp_not_cov) {
		this.r129_crm_sub_col_elig_exp_not_cov = r129_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR129_crm_sub_rwa_ris_crm() {
		return r129_crm_sub_rwa_ris_crm;
	}
	public void setR129_crm_sub_rwa_ris_crm(BigDecimal r129_crm_sub_rwa_ris_crm) {
		this.r129_crm_sub_rwa_ris_crm = r129_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR129_crm_sub_rwa_cov_crm() {
		return r129_crm_sub_rwa_cov_crm;
	}
	public void setR129_crm_sub_rwa_cov_crm(BigDecimal r129_crm_sub_rwa_cov_crm) {
		this.r129_crm_sub_rwa_cov_crm = r129_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR129_crm_sub_rwa_org_cou() {
		return r129_crm_sub_rwa_org_cou;
	}
	public void setR129_crm_sub_rwa_org_cou(BigDecimal r129_crm_sub_rwa_org_cou) {
		this.r129_crm_sub_rwa_org_cou = r129_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR129_crm_sub_rwa_not_cov_crm() {
		return r129_crm_sub_rwa_not_cov_crm;
	}
	public void setR129_crm_sub_rwa_not_cov_crm(BigDecimal r129_crm_sub_rwa_not_cov_crm) {
		this.r129_crm_sub_rwa_not_cov_crm = r129_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR129_crm_comp_col_expo_elig() {
		return r129_crm_comp_col_expo_elig;
	}
	public void setR129_crm_comp_col_expo_elig(BigDecimal r129_crm_comp_col_expo_elig) {
		this.r129_crm_comp_col_expo_elig = r129_crm_comp_col_expo_elig;
	}
	public BigDecimal getR129_crm_comp_col_elig_expo_vol_adj() {
		return r129_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR129_crm_comp_col_elig_expo_vol_adj(BigDecimal r129_crm_comp_col_elig_expo_vol_adj) {
		this.r129_crm_comp_col_elig_expo_vol_adj = r129_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR129_crm_comp_col_elig_fin_hai() {
		return r129_crm_comp_col_elig_fin_hai;
	}
	public void setR129_crm_comp_col_elig_fin_hai(BigDecimal r129_crm_comp_col_elig_fin_hai) {
		this.r129_crm_comp_col_elig_fin_hai = r129_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR129_crm_comp_col_expo_val() {
		return r129_crm_comp_col_expo_val;
	}
	public void setR129_crm_comp_col_expo_val(BigDecimal r129_crm_comp_col_expo_val) {
		this.r129_crm_comp_col_expo_val = r129_crm_comp_col_expo_val;
	}
	public BigDecimal getR129_rwa_elig_expo_not_cov_crm() {
		return r129_rwa_elig_expo_not_cov_crm;
	}
	public void setR129_rwa_elig_expo_not_cov_crm(BigDecimal r129_rwa_elig_expo_not_cov_crm) {
		this.r129_rwa_elig_expo_not_cov_crm = r129_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR129_rwa_unsec_expo_cre_ris() {
		return r129_rwa_unsec_expo_cre_ris;
	}
	public void setR129_rwa_unsec_expo_cre_ris(BigDecimal r129_rwa_unsec_expo_cre_ris) {
		this.r129_rwa_unsec_expo_cre_ris = r129_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR129_rwa_unsec_expo() {
		return r129_rwa_unsec_expo;
	}
	public void setR129_rwa_unsec_expo(BigDecimal r129_rwa_unsec_expo) {
		this.r129_rwa_unsec_expo = r129_rwa_unsec_expo;
	}
	public BigDecimal getR129_rwa_tot_ris_wei_ass() {
		return r129_rwa_tot_ris_wei_ass;
	}
	public void setR129_rwa_tot_ris_wei_ass(BigDecimal r129_rwa_tot_ris_wei_ass) {
		this.r129_rwa_tot_ris_wei_ass = r129_rwa_tot_ris_wei_ass;
	}
	public String getR130_exposure_class() {
		return r130_exposure_class;
	}
	public void setR130_exposure_class(String r130_exposure_class) {
		this.r130_exposure_class = r130_exposure_class;
	}
	public BigDecimal getR130_expo_crm() {
		return r130_expo_crm;
	}
	public void setR130_expo_crm(BigDecimal r130_expo_crm) {
		this.r130_expo_crm = r130_expo_crm;
	}
	public BigDecimal getR130_spe_pro_expo() {
		return r130_spe_pro_expo;
	}
	public void setR130_spe_pro_expo(BigDecimal r130_spe_pro_expo) {
		this.r130_spe_pro_expo = r130_spe_pro_expo;
	}
	public BigDecimal getR130_amt_elig_sht_net() {
		return r130_amt_elig_sht_net;
	}
	public void setR130_amt_elig_sht_net(BigDecimal r130_amt_elig_sht_net) {
		this.r130_amt_elig_sht_net = r130_amt_elig_sht_net;
	}
	public BigDecimal getR130_tot_expo_net_spe() {
		return r130_tot_expo_net_spe;
	}
	public void setR130_tot_expo_net_spe(BigDecimal r130_tot_expo_net_spe) {
		this.r130_tot_expo_net_spe = r130_tot_expo_net_spe;
	}
	public BigDecimal getR130_crm_sub_elig_sub_app() {
		return r130_crm_sub_elig_sub_app;
	}
	public void setR130_crm_sub_elig_sub_app(BigDecimal r130_crm_sub_elig_sub_app) {
		this.r130_crm_sub_elig_sub_app = r130_crm_sub_elig_sub_app;
	}
	public BigDecimal getR130_crm_sub_non_col_guar() {
		return r130_crm_sub_non_col_guar;
	}
	public void setR130_crm_sub_non_col_guar(BigDecimal r130_crm_sub_non_col_guar) {
		this.r130_crm_sub_non_col_guar = r130_crm_sub_non_col_guar;
	}
	public BigDecimal getR130_crm_sub_non_col_cre_der() {
		return r130_crm_sub_non_col_cre_der;
	}
	public void setR130_crm_sub_non_col_cre_der(BigDecimal r130_crm_sub_non_col_cre_der) {
		this.r130_crm_sub_non_col_cre_der = r130_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR130_crm_sub_col_elig_cash() {
		return r130_crm_sub_col_elig_cash;
	}
	public void setR130_crm_sub_col_elig_cash(BigDecimal r130_crm_sub_col_elig_cash) {
		this.r130_crm_sub_col_elig_cash = r130_crm_sub_col_elig_cash;
	}
	public BigDecimal getR130_crm_sub_col_elig_trea_bills() {
		return r130_crm_sub_col_elig_trea_bills;
	}
	public void setR130_crm_sub_col_elig_trea_bills(BigDecimal r130_crm_sub_col_elig_trea_bills) {
		this.r130_crm_sub_col_elig_trea_bills = r130_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR130_crm_sub_col_elig_deb_sec() {
		return r130_crm_sub_col_elig_deb_sec;
	}
	public void setR130_crm_sub_col_elig_deb_sec(BigDecimal r130_crm_sub_col_elig_deb_sec) {
		this.r130_crm_sub_col_elig_deb_sec = r130_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR130_crm_sub_col_elig_equi() {
		return r130_crm_sub_col_elig_equi;
	}
	public void setR130_crm_sub_col_elig_equi(BigDecimal r130_crm_sub_col_elig_equi) {
		this.r130_crm_sub_col_elig_equi = r130_crm_sub_col_elig_equi;
	}
	public BigDecimal getR130_crm_sub_col_elig_unit_tru() {
		return r130_crm_sub_col_elig_unit_tru;
	}
	public void setR130_crm_sub_col_elig_unit_tru(BigDecimal r130_crm_sub_col_elig_unit_tru) {
		this.r130_crm_sub_col_elig_unit_tru = r130_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR130_crm_sub_col_exp_cov() {
		return r130_crm_sub_col_exp_cov;
	}
	public void setR130_crm_sub_col_exp_cov(BigDecimal r130_crm_sub_col_exp_cov) {
		this.r130_crm_sub_col_exp_cov = r130_crm_sub_col_exp_cov;
	}
	public BigDecimal getR130_crm_sub_col_elig_exp_not_cov() {
		return r130_crm_sub_col_elig_exp_not_cov;
	}
	public void setR130_crm_sub_col_elig_exp_not_cov(BigDecimal r130_crm_sub_col_elig_exp_not_cov) {
		this.r130_crm_sub_col_elig_exp_not_cov = r130_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR130_crm_sub_rwa_ris_crm() {
		return r130_crm_sub_rwa_ris_crm;
	}
	public void setR130_crm_sub_rwa_ris_crm(BigDecimal r130_crm_sub_rwa_ris_crm) {
		this.r130_crm_sub_rwa_ris_crm = r130_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR130_crm_sub_rwa_cov_crm() {
		return r130_crm_sub_rwa_cov_crm;
	}
	public void setR130_crm_sub_rwa_cov_crm(BigDecimal r130_crm_sub_rwa_cov_crm) {
		this.r130_crm_sub_rwa_cov_crm = r130_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR130_crm_sub_rwa_org_cou() {
		return r130_crm_sub_rwa_org_cou;
	}
	public void setR130_crm_sub_rwa_org_cou(BigDecimal r130_crm_sub_rwa_org_cou) {
		this.r130_crm_sub_rwa_org_cou = r130_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR130_crm_sub_rwa_not_cov_crm() {
		return r130_crm_sub_rwa_not_cov_crm;
	}
	public void setR130_crm_sub_rwa_not_cov_crm(BigDecimal r130_crm_sub_rwa_not_cov_crm) {
		this.r130_crm_sub_rwa_not_cov_crm = r130_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR130_crm_comp_col_expo_elig() {
		return r130_crm_comp_col_expo_elig;
	}
	public void setR130_crm_comp_col_expo_elig(BigDecimal r130_crm_comp_col_expo_elig) {
		this.r130_crm_comp_col_expo_elig = r130_crm_comp_col_expo_elig;
	}
	public BigDecimal getR130_crm_comp_col_elig_expo_vol_adj() {
		return r130_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR130_crm_comp_col_elig_expo_vol_adj(BigDecimal r130_crm_comp_col_elig_expo_vol_adj) {
		this.r130_crm_comp_col_elig_expo_vol_adj = r130_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR130_crm_comp_col_elig_fin_hai() {
		return r130_crm_comp_col_elig_fin_hai;
	}
	public void setR130_crm_comp_col_elig_fin_hai(BigDecimal r130_crm_comp_col_elig_fin_hai) {
		this.r130_crm_comp_col_elig_fin_hai = r130_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR130_crm_comp_col_expo_val() {
		return r130_crm_comp_col_expo_val;
	}
	public void setR130_crm_comp_col_expo_val(BigDecimal r130_crm_comp_col_expo_val) {
		this.r130_crm_comp_col_expo_val = r130_crm_comp_col_expo_val;
	}
	public BigDecimal getR130_rwa_elig_expo_not_cov_crm() {
		return r130_rwa_elig_expo_not_cov_crm;
	}
	public void setR130_rwa_elig_expo_not_cov_crm(BigDecimal r130_rwa_elig_expo_not_cov_crm) {
		this.r130_rwa_elig_expo_not_cov_crm = r130_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR130_rwa_unsec_expo_cre_ris() {
		return r130_rwa_unsec_expo_cre_ris;
	}
	public void setR130_rwa_unsec_expo_cre_ris(BigDecimal r130_rwa_unsec_expo_cre_ris) {
		this.r130_rwa_unsec_expo_cre_ris = r130_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR130_rwa_unsec_expo() {
		return r130_rwa_unsec_expo;
	}
	public void setR130_rwa_unsec_expo(BigDecimal r130_rwa_unsec_expo) {
		this.r130_rwa_unsec_expo = r130_rwa_unsec_expo;
	}
	public BigDecimal getR130_rwa_tot_ris_wei_ass() {
		return r130_rwa_tot_ris_wei_ass;
	}
	public void setR130_rwa_tot_ris_wei_ass(BigDecimal r130_rwa_tot_ris_wei_ass) {
		this.r130_rwa_tot_ris_wei_ass = r130_rwa_tot_ris_wei_ass;
	}
	public String getR131_exposure_class() {
		return r131_exposure_class;
	}
	public void setR131_exposure_class(String r131_exposure_class) {
		this.r131_exposure_class = r131_exposure_class;
	}
	public BigDecimal getR131_expo_crm() {
		return r131_expo_crm;
	}
	public void setR131_expo_crm(BigDecimal r131_expo_crm) {
		this.r131_expo_crm = r131_expo_crm;
	}
	public BigDecimal getR131_spe_pro_expo() {
		return r131_spe_pro_expo;
	}
	public void setR131_spe_pro_expo(BigDecimal r131_spe_pro_expo) {
		this.r131_spe_pro_expo = r131_spe_pro_expo;
	}
	public BigDecimal getR131_amt_elig_sht_net() {
		return r131_amt_elig_sht_net;
	}
	public void setR131_amt_elig_sht_net(BigDecimal r131_amt_elig_sht_net) {
		this.r131_amt_elig_sht_net = r131_amt_elig_sht_net;
	}
	public BigDecimal getR131_tot_expo_net_spe() {
		return r131_tot_expo_net_spe;
	}
	public void setR131_tot_expo_net_spe(BigDecimal r131_tot_expo_net_spe) {
		this.r131_tot_expo_net_spe = r131_tot_expo_net_spe;
	}
	public BigDecimal getR131_crm_sub_elig_sub_app() {
		return r131_crm_sub_elig_sub_app;
	}
	public void setR131_crm_sub_elig_sub_app(BigDecimal r131_crm_sub_elig_sub_app) {
		this.r131_crm_sub_elig_sub_app = r131_crm_sub_elig_sub_app;
	}
	public BigDecimal getR131_crm_sub_non_col_guar() {
		return r131_crm_sub_non_col_guar;
	}
	public void setR131_crm_sub_non_col_guar(BigDecimal r131_crm_sub_non_col_guar) {
		this.r131_crm_sub_non_col_guar = r131_crm_sub_non_col_guar;
	}
	public BigDecimal getR131_crm_sub_non_col_cre_der() {
		return r131_crm_sub_non_col_cre_der;
	}
	public void setR131_crm_sub_non_col_cre_der(BigDecimal r131_crm_sub_non_col_cre_der) {
		this.r131_crm_sub_non_col_cre_der = r131_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR131_crm_sub_col_elig_cash() {
		return r131_crm_sub_col_elig_cash;
	}
	public void setR131_crm_sub_col_elig_cash(BigDecimal r131_crm_sub_col_elig_cash) {
		this.r131_crm_sub_col_elig_cash = r131_crm_sub_col_elig_cash;
	}
	public BigDecimal getR131_crm_sub_col_elig_trea_bills() {
		return r131_crm_sub_col_elig_trea_bills;
	}
	public void setR131_crm_sub_col_elig_trea_bills(BigDecimal r131_crm_sub_col_elig_trea_bills) {
		this.r131_crm_sub_col_elig_trea_bills = r131_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR131_crm_sub_col_elig_deb_sec() {
		return r131_crm_sub_col_elig_deb_sec;
	}
	public void setR131_crm_sub_col_elig_deb_sec(BigDecimal r131_crm_sub_col_elig_deb_sec) {
		this.r131_crm_sub_col_elig_deb_sec = r131_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR131_crm_sub_col_elig_equi() {
		return r131_crm_sub_col_elig_equi;
	}
	public void setR131_crm_sub_col_elig_equi(BigDecimal r131_crm_sub_col_elig_equi) {
		this.r131_crm_sub_col_elig_equi = r131_crm_sub_col_elig_equi;
	}
	public BigDecimal getR131_crm_sub_col_elig_unit_tru() {
		return r131_crm_sub_col_elig_unit_tru;
	}
	public void setR131_crm_sub_col_elig_unit_tru(BigDecimal r131_crm_sub_col_elig_unit_tru) {
		this.r131_crm_sub_col_elig_unit_tru = r131_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR131_crm_sub_col_exp_cov() {
		return r131_crm_sub_col_exp_cov;
	}
	public void setR131_crm_sub_col_exp_cov(BigDecimal r131_crm_sub_col_exp_cov) {
		this.r131_crm_sub_col_exp_cov = r131_crm_sub_col_exp_cov;
	}
	public BigDecimal getR131_crm_sub_col_elig_exp_not_cov() {
		return r131_crm_sub_col_elig_exp_not_cov;
	}
	public void setR131_crm_sub_col_elig_exp_not_cov(BigDecimal r131_crm_sub_col_elig_exp_not_cov) {
		this.r131_crm_sub_col_elig_exp_not_cov = r131_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR131_crm_sub_rwa_ris_crm() {
		return r131_crm_sub_rwa_ris_crm;
	}
	public void setR131_crm_sub_rwa_ris_crm(BigDecimal r131_crm_sub_rwa_ris_crm) {
		this.r131_crm_sub_rwa_ris_crm = r131_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR131_crm_sub_rwa_cov_crm() {
		return r131_crm_sub_rwa_cov_crm;
	}
	public void setR131_crm_sub_rwa_cov_crm(BigDecimal r131_crm_sub_rwa_cov_crm) {
		this.r131_crm_sub_rwa_cov_crm = r131_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR131_crm_sub_rwa_org_cou() {
		return r131_crm_sub_rwa_org_cou;
	}
	public void setR131_crm_sub_rwa_org_cou(BigDecimal r131_crm_sub_rwa_org_cou) {
		this.r131_crm_sub_rwa_org_cou = r131_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR131_crm_sub_rwa_not_cov_crm() {
		return r131_crm_sub_rwa_not_cov_crm;
	}
	public void setR131_crm_sub_rwa_not_cov_crm(BigDecimal r131_crm_sub_rwa_not_cov_crm) {
		this.r131_crm_sub_rwa_not_cov_crm = r131_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR131_crm_comp_col_expo_elig() {
		return r131_crm_comp_col_expo_elig;
	}
	public void setR131_crm_comp_col_expo_elig(BigDecimal r131_crm_comp_col_expo_elig) {
		this.r131_crm_comp_col_expo_elig = r131_crm_comp_col_expo_elig;
	}
	public BigDecimal getR131_crm_comp_col_elig_expo_vol_adj() {
		return r131_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR131_crm_comp_col_elig_expo_vol_adj(BigDecimal r131_crm_comp_col_elig_expo_vol_adj) {
		this.r131_crm_comp_col_elig_expo_vol_adj = r131_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR131_crm_comp_col_elig_fin_hai() {
		return r131_crm_comp_col_elig_fin_hai;
	}
	public void setR131_crm_comp_col_elig_fin_hai(BigDecimal r131_crm_comp_col_elig_fin_hai) {
		this.r131_crm_comp_col_elig_fin_hai = r131_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR131_crm_comp_col_expo_val() {
		return r131_crm_comp_col_expo_val;
	}
	public void setR131_crm_comp_col_expo_val(BigDecimal r131_crm_comp_col_expo_val) {
		this.r131_crm_comp_col_expo_val = r131_crm_comp_col_expo_val;
	}
	public BigDecimal getR131_rwa_elig_expo_not_cov_crm() {
		return r131_rwa_elig_expo_not_cov_crm;
	}
	public void setR131_rwa_elig_expo_not_cov_crm(BigDecimal r131_rwa_elig_expo_not_cov_crm) {
		this.r131_rwa_elig_expo_not_cov_crm = r131_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR131_rwa_unsec_expo_cre_ris() {
		return r131_rwa_unsec_expo_cre_ris;
	}
	public void setR131_rwa_unsec_expo_cre_ris(BigDecimal r131_rwa_unsec_expo_cre_ris) {
		this.r131_rwa_unsec_expo_cre_ris = r131_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR131_rwa_unsec_expo() {
		return r131_rwa_unsec_expo;
	}
	public void setR131_rwa_unsec_expo(BigDecimal r131_rwa_unsec_expo) {
		this.r131_rwa_unsec_expo = r131_rwa_unsec_expo;
	}
	public BigDecimal getR131_rwa_tot_ris_wei_ass() {
		return r131_rwa_tot_ris_wei_ass;
	}
	public void setR131_rwa_tot_ris_wei_ass(BigDecimal r131_rwa_tot_ris_wei_ass) {
		this.r131_rwa_tot_ris_wei_ass = r131_rwa_tot_ris_wei_ass;
	}
	public String getR132_exposure_class() {
		return r132_exposure_class;
	}
	public void setR132_exposure_class(String r132_exposure_class) {
		this.r132_exposure_class = r132_exposure_class;
	}
	public BigDecimal getR132_expo_crm() {
		return r132_expo_crm;
	}
	public void setR132_expo_crm(BigDecimal r132_expo_crm) {
		this.r132_expo_crm = r132_expo_crm;
	}
	public BigDecimal getR132_spe_pro_expo() {
		return r132_spe_pro_expo;
	}
	public void setR132_spe_pro_expo(BigDecimal r132_spe_pro_expo) {
		this.r132_spe_pro_expo = r132_spe_pro_expo;
	}
	public BigDecimal getR132_amt_elig_sht_net() {
		return r132_amt_elig_sht_net;
	}
	public void setR132_amt_elig_sht_net(BigDecimal r132_amt_elig_sht_net) {
		this.r132_amt_elig_sht_net = r132_amt_elig_sht_net;
	}
	public BigDecimal getR132_tot_expo_net_spe() {
		return r132_tot_expo_net_spe;
	}
	public void setR132_tot_expo_net_spe(BigDecimal r132_tot_expo_net_spe) {
		this.r132_tot_expo_net_spe = r132_tot_expo_net_spe;
	}
	public BigDecimal getR132_crm_sub_elig_sub_app() {
		return r132_crm_sub_elig_sub_app;
	}
	public void setR132_crm_sub_elig_sub_app(BigDecimal r132_crm_sub_elig_sub_app) {
		this.r132_crm_sub_elig_sub_app = r132_crm_sub_elig_sub_app;
	}
	public BigDecimal getR132_crm_sub_non_col_guar() {
		return r132_crm_sub_non_col_guar;
	}
	public void setR132_crm_sub_non_col_guar(BigDecimal r132_crm_sub_non_col_guar) {
		this.r132_crm_sub_non_col_guar = r132_crm_sub_non_col_guar;
	}
	public BigDecimal getR132_crm_sub_non_col_cre_der() {
		return r132_crm_sub_non_col_cre_der;
	}
	public void setR132_crm_sub_non_col_cre_der(BigDecimal r132_crm_sub_non_col_cre_der) {
		this.r132_crm_sub_non_col_cre_der = r132_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR132_crm_sub_col_elig_cash() {
		return r132_crm_sub_col_elig_cash;
	}
	public void setR132_crm_sub_col_elig_cash(BigDecimal r132_crm_sub_col_elig_cash) {
		this.r132_crm_sub_col_elig_cash = r132_crm_sub_col_elig_cash;
	}
	public BigDecimal getR132_crm_sub_col_elig_trea_bills() {
		return r132_crm_sub_col_elig_trea_bills;
	}
	public void setR132_crm_sub_col_elig_trea_bills(BigDecimal r132_crm_sub_col_elig_trea_bills) {
		this.r132_crm_sub_col_elig_trea_bills = r132_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR132_crm_sub_col_elig_deb_sec() {
		return r132_crm_sub_col_elig_deb_sec;
	}
	public void setR132_crm_sub_col_elig_deb_sec(BigDecimal r132_crm_sub_col_elig_deb_sec) {
		this.r132_crm_sub_col_elig_deb_sec = r132_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR132_crm_sub_col_elig_equi() {
		return r132_crm_sub_col_elig_equi;
	}
	public void setR132_crm_sub_col_elig_equi(BigDecimal r132_crm_sub_col_elig_equi) {
		this.r132_crm_sub_col_elig_equi = r132_crm_sub_col_elig_equi;
	}
	public BigDecimal getR132_crm_sub_col_elig_unit_tru() {
		return r132_crm_sub_col_elig_unit_tru;
	}
	public void setR132_crm_sub_col_elig_unit_tru(BigDecimal r132_crm_sub_col_elig_unit_tru) {
		this.r132_crm_sub_col_elig_unit_tru = r132_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR132_crm_sub_col_exp_cov() {
		return r132_crm_sub_col_exp_cov;
	}
	public void setR132_crm_sub_col_exp_cov(BigDecimal r132_crm_sub_col_exp_cov) {
		this.r132_crm_sub_col_exp_cov = r132_crm_sub_col_exp_cov;
	}
	public BigDecimal getR132_crm_sub_col_elig_exp_not_cov() {
		return r132_crm_sub_col_elig_exp_not_cov;
	}
	public void setR132_crm_sub_col_elig_exp_not_cov(BigDecimal r132_crm_sub_col_elig_exp_not_cov) {
		this.r132_crm_sub_col_elig_exp_not_cov = r132_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR132_crm_sub_rwa_ris_crm() {
		return r132_crm_sub_rwa_ris_crm;
	}
	public void setR132_crm_sub_rwa_ris_crm(BigDecimal r132_crm_sub_rwa_ris_crm) {
		this.r132_crm_sub_rwa_ris_crm = r132_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR132_crm_sub_rwa_cov_crm() {
		return r132_crm_sub_rwa_cov_crm;
	}
	public void setR132_crm_sub_rwa_cov_crm(BigDecimal r132_crm_sub_rwa_cov_crm) {
		this.r132_crm_sub_rwa_cov_crm = r132_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR132_crm_sub_rwa_org_cou() {
		return r132_crm_sub_rwa_org_cou;
	}
	public void setR132_crm_sub_rwa_org_cou(BigDecimal r132_crm_sub_rwa_org_cou) {
		this.r132_crm_sub_rwa_org_cou = r132_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR132_crm_sub_rwa_not_cov_crm() {
		return r132_crm_sub_rwa_not_cov_crm;
	}
	public void setR132_crm_sub_rwa_not_cov_crm(BigDecimal r132_crm_sub_rwa_not_cov_crm) {
		this.r132_crm_sub_rwa_not_cov_crm = r132_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR132_crm_comp_col_expo_elig() {
		return r132_crm_comp_col_expo_elig;
	}
	public void setR132_crm_comp_col_expo_elig(BigDecimal r132_crm_comp_col_expo_elig) {
		this.r132_crm_comp_col_expo_elig = r132_crm_comp_col_expo_elig;
	}
	public BigDecimal getR132_crm_comp_col_elig_expo_vol_adj() {
		return r132_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR132_crm_comp_col_elig_expo_vol_adj(BigDecimal r132_crm_comp_col_elig_expo_vol_adj) {
		this.r132_crm_comp_col_elig_expo_vol_adj = r132_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR132_crm_comp_col_elig_fin_hai() {
		return r132_crm_comp_col_elig_fin_hai;
	}
	public void setR132_crm_comp_col_elig_fin_hai(BigDecimal r132_crm_comp_col_elig_fin_hai) {
		this.r132_crm_comp_col_elig_fin_hai = r132_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR132_crm_comp_col_expo_val() {
		return r132_crm_comp_col_expo_val;
	}
	public void setR132_crm_comp_col_expo_val(BigDecimal r132_crm_comp_col_expo_val) {
		this.r132_crm_comp_col_expo_val = r132_crm_comp_col_expo_val;
	}
	public BigDecimal getR132_rwa_elig_expo_not_cov_crm() {
		return r132_rwa_elig_expo_not_cov_crm;
	}
	public void setR132_rwa_elig_expo_not_cov_crm(BigDecimal r132_rwa_elig_expo_not_cov_crm) {
		this.r132_rwa_elig_expo_not_cov_crm = r132_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR132_rwa_unsec_expo_cre_ris() {
		return r132_rwa_unsec_expo_cre_ris;
	}
	public void setR132_rwa_unsec_expo_cre_ris(BigDecimal r132_rwa_unsec_expo_cre_ris) {
		this.r132_rwa_unsec_expo_cre_ris = r132_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR132_rwa_unsec_expo() {
		return r132_rwa_unsec_expo;
	}
	public void setR132_rwa_unsec_expo(BigDecimal r132_rwa_unsec_expo) {
		this.r132_rwa_unsec_expo = r132_rwa_unsec_expo;
	}
	public BigDecimal getR132_rwa_tot_ris_wei_ass() {
		return r132_rwa_tot_ris_wei_ass;
	}
	public void setR132_rwa_tot_ris_wei_ass(BigDecimal r132_rwa_tot_ris_wei_ass) {
		this.r132_rwa_tot_ris_wei_ass = r132_rwa_tot_ris_wei_ass;
	}
	public String getR133_exposure_class() {
		return r133_exposure_class;
	}
	public void setR133_exposure_class(String r133_exposure_class) {
		this.r133_exposure_class = r133_exposure_class;
	}
	public BigDecimal getR133_expo_crm() {
		return r133_expo_crm;
	}
	public void setR133_expo_crm(BigDecimal r133_expo_crm) {
		this.r133_expo_crm = r133_expo_crm;
	}
	public BigDecimal getR133_spe_pro_expo() {
		return r133_spe_pro_expo;
	}
	public void setR133_spe_pro_expo(BigDecimal r133_spe_pro_expo) {
		this.r133_spe_pro_expo = r133_spe_pro_expo;
	}
	public BigDecimal getR133_amt_elig_sht_net() {
		return r133_amt_elig_sht_net;
	}
	public void setR133_amt_elig_sht_net(BigDecimal r133_amt_elig_sht_net) {
		this.r133_amt_elig_sht_net = r133_amt_elig_sht_net;
	}
	public BigDecimal getR133_tot_expo_net_spe() {
		return r133_tot_expo_net_spe;
	}
	public void setR133_tot_expo_net_spe(BigDecimal r133_tot_expo_net_spe) {
		this.r133_tot_expo_net_spe = r133_tot_expo_net_spe;
	}
	public BigDecimal getR133_crm_sub_elig_sub_app() {
		return r133_crm_sub_elig_sub_app;
	}
	public void setR133_crm_sub_elig_sub_app(BigDecimal r133_crm_sub_elig_sub_app) {
		this.r133_crm_sub_elig_sub_app = r133_crm_sub_elig_sub_app;
	}
	public BigDecimal getR133_crm_sub_non_col_guar() {
		return r133_crm_sub_non_col_guar;
	}
	public void setR133_crm_sub_non_col_guar(BigDecimal r133_crm_sub_non_col_guar) {
		this.r133_crm_sub_non_col_guar = r133_crm_sub_non_col_guar;
	}
	public BigDecimal getR133_crm_sub_non_col_cre_der() {
		return r133_crm_sub_non_col_cre_der;
	}
	public void setR133_crm_sub_non_col_cre_der(BigDecimal r133_crm_sub_non_col_cre_der) {
		this.r133_crm_sub_non_col_cre_der = r133_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR133_crm_sub_col_elig_cash() {
		return r133_crm_sub_col_elig_cash;
	}
	public void setR133_crm_sub_col_elig_cash(BigDecimal r133_crm_sub_col_elig_cash) {
		this.r133_crm_sub_col_elig_cash = r133_crm_sub_col_elig_cash;
	}
	public BigDecimal getR133_crm_sub_col_elig_trea_bills() {
		return r133_crm_sub_col_elig_trea_bills;
	}
	public void setR133_crm_sub_col_elig_trea_bills(BigDecimal r133_crm_sub_col_elig_trea_bills) {
		this.r133_crm_sub_col_elig_trea_bills = r133_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR133_crm_sub_col_elig_deb_sec() {
		return r133_crm_sub_col_elig_deb_sec;
	}
	public void setR133_crm_sub_col_elig_deb_sec(BigDecimal r133_crm_sub_col_elig_deb_sec) {
		this.r133_crm_sub_col_elig_deb_sec = r133_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR133_crm_sub_col_elig_equi() {
		return r133_crm_sub_col_elig_equi;
	}
	public void setR133_crm_sub_col_elig_equi(BigDecimal r133_crm_sub_col_elig_equi) {
		this.r133_crm_sub_col_elig_equi = r133_crm_sub_col_elig_equi;
	}
	public BigDecimal getR133_crm_sub_col_elig_unit_tru() {
		return r133_crm_sub_col_elig_unit_tru;
	}
	public void setR133_crm_sub_col_elig_unit_tru(BigDecimal r133_crm_sub_col_elig_unit_tru) {
		this.r133_crm_sub_col_elig_unit_tru = r133_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR133_crm_sub_col_exp_cov() {
		return r133_crm_sub_col_exp_cov;
	}
	public void setR133_crm_sub_col_exp_cov(BigDecimal r133_crm_sub_col_exp_cov) {
		this.r133_crm_sub_col_exp_cov = r133_crm_sub_col_exp_cov;
	}
	public BigDecimal getR133_crm_sub_col_elig_exp_not_cov() {
		return r133_crm_sub_col_elig_exp_not_cov;
	}
	public void setR133_crm_sub_col_elig_exp_not_cov(BigDecimal r133_crm_sub_col_elig_exp_not_cov) {
		this.r133_crm_sub_col_elig_exp_not_cov = r133_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR133_crm_sub_rwa_ris_crm() {
		return r133_crm_sub_rwa_ris_crm;
	}
	public void setR133_crm_sub_rwa_ris_crm(BigDecimal r133_crm_sub_rwa_ris_crm) {
		this.r133_crm_sub_rwa_ris_crm = r133_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR133_crm_sub_rwa_cov_crm() {
		return r133_crm_sub_rwa_cov_crm;
	}
	public void setR133_crm_sub_rwa_cov_crm(BigDecimal r133_crm_sub_rwa_cov_crm) {
		this.r133_crm_sub_rwa_cov_crm = r133_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR133_crm_sub_rwa_org_cou() {
		return r133_crm_sub_rwa_org_cou;
	}
	public void setR133_crm_sub_rwa_org_cou(BigDecimal r133_crm_sub_rwa_org_cou) {
		this.r133_crm_sub_rwa_org_cou = r133_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR133_crm_sub_rwa_not_cov_crm() {
		return r133_crm_sub_rwa_not_cov_crm;
	}
	public void setR133_crm_sub_rwa_not_cov_crm(BigDecimal r133_crm_sub_rwa_not_cov_crm) {
		this.r133_crm_sub_rwa_not_cov_crm = r133_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR133_crm_comp_col_expo_elig() {
		return r133_crm_comp_col_expo_elig;
	}
	public void setR133_crm_comp_col_expo_elig(BigDecimal r133_crm_comp_col_expo_elig) {
		this.r133_crm_comp_col_expo_elig = r133_crm_comp_col_expo_elig;
	}
	public BigDecimal getR133_crm_comp_col_elig_expo_vol_adj() {
		return r133_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR133_crm_comp_col_elig_expo_vol_adj(BigDecimal r133_crm_comp_col_elig_expo_vol_adj) {
		this.r133_crm_comp_col_elig_expo_vol_adj = r133_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR133_crm_comp_col_elig_fin_hai() {
		return r133_crm_comp_col_elig_fin_hai;
	}
	public void setR133_crm_comp_col_elig_fin_hai(BigDecimal r133_crm_comp_col_elig_fin_hai) {
		this.r133_crm_comp_col_elig_fin_hai = r133_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR133_crm_comp_col_expo_val() {
		return r133_crm_comp_col_expo_val;
	}
	public void setR133_crm_comp_col_expo_val(BigDecimal r133_crm_comp_col_expo_val) {
		this.r133_crm_comp_col_expo_val = r133_crm_comp_col_expo_val;
	}
	public BigDecimal getR133_rwa_elig_expo_not_cov_crm() {
		return r133_rwa_elig_expo_not_cov_crm;
	}
	public void setR133_rwa_elig_expo_not_cov_crm(BigDecimal r133_rwa_elig_expo_not_cov_crm) {
		this.r133_rwa_elig_expo_not_cov_crm = r133_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR133_rwa_unsec_expo_cre_ris() {
		return r133_rwa_unsec_expo_cre_ris;
	}
	public void setR133_rwa_unsec_expo_cre_ris(BigDecimal r133_rwa_unsec_expo_cre_ris) {
		this.r133_rwa_unsec_expo_cre_ris = r133_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR133_rwa_unsec_expo() {
		return r133_rwa_unsec_expo;
	}
	public void setR133_rwa_unsec_expo(BigDecimal r133_rwa_unsec_expo) {
		this.r133_rwa_unsec_expo = r133_rwa_unsec_expo;
	}
	public BigDecimal getR133_rwa_tot_ris_wei_ass() {
		return r133_rwa_tot_ris_wei_ass;
	}
	public void setR133_rwa_tot_ris_wei_ass(BigDecimal r133_rwa_tot_ris_wei_ass) {
		this.r133_rwa_tot_ris_wei_ass = r133_rwa_tot_ris_wei_ass;
	}
	public String getR134_exposure_class() {
		return r134_exposure_class;
	}
	public void setR134_exposure_class(String r134_exposure_class) {
		this.r134_exposure_class = r134_exposure_class;
	}
	public BigDecimal getR134_expo_crm() {
		return r134_expo_crm;
	}
	public void setR134_expo_crm(BigDecimal r134_expo_crm) {
		this.r134_expo_crm = r134_expo_crm;
	}
	public BigDecimal getR134_spe_pro_expo() {
		return r134_spe_pro_expo;
	}
	public void setR134_spe_pro_expo(BigDecimal r134_spe_pro_expo) {
		this.r134_spe_pro_expo = r134_spe_pro_expo;
	}
	public BigDecimal getR134_amt_elig_sht_net() {
		return r134_amt_elig_sht_net;
	}
	public void setR134_amt_elig_sht_net(BigDecimal r134_amt_elig_sht_net) {
		this.r134_amt_elig_sht_net = r134_amt_elig_sht_net;
	}
	public BigDecimal getR134_tot_expo_net_spe() {
		return r134_tot_expo_net_spe;
	}
	public void setR134_tot_expo_net_spe(BigDecimal r134_tot_expo_net_spe) {
		this.r134_tot_expo_net_spe = r134_tot_expo_net_spe;
	}
	public BigDecimal getR134_crm_sub_elig_sub_app() {
		return r134_crm_sub_elig_sub_app;
	}
	public void setR134_crm_sub_elig_sub_app(BigDecimal r134_crm_sub_elig_sub_app) {
		this.r134_crm_sub_elig_sub_app = r134_crm_sub_elig_sub_app;
	}
	public BigDecimal getR134_crm_sub_non_col_guar() {
		return r134_crm_sub_non_col_guar;
	}
	public void setR134_crm_sub_non_col_guar(BigDecimal r134_crm_sub_non_col_guar) {
		this.r134_crm_sub_non_col_guar = r134_crm_sub_non_col_guar;
	}
	public BigDecimal getR134_crm_sub_non_col_cre_der() {
		return r134_crm_sub_non_col_cre_der;
	}
	public void setR134_crm_sub_non_col_cre_der(BigDecimal r134_crm_sub_non_col_cre_der) {
		this.r134_crm_sub_non_col_cre_der = r134_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR134_crm_sub_col_elig_cash() {
		return r134_crm_sub_col_elig_cash;
	}
	public void setR134_crm_sub_col_elig_cash(BigDecimal r134_crm_sub_col_elig_cash) {
		this.r134_crm_sub_col_elig_cash = r134_crm_sub_col_elig_cash;
	}
	public BigDecimal getR134_crm_sub_col_elig_trea_bills() {
		return r134_crm_sub_col_elig_trea_bills;
	}
	public void setR134_crm_sub_col_elig_trea_bills(BigDecimal r134_crm_sub_col_elig_trea_bills) {
		this.r134_crm_sub_col_elig_trea_bills = r134_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR134_crm_sub_col_elig_deb_sec() {
		return r134_crm_sub_col_elig_deb_sec;
	}
	public void setR134_crm_sub_col_elig_deb_sec(BigDecimal r134_crm_sub_col_elig_deb_sec) {
		this.r134_crm_sub_col_elig_deb_sec = r134_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR134_crm_sub_col_elig_equi() {
		return r134_crm_sub_col_elig_equi;
	}
	public void setR134_crm_sub_col_elig_equi(BigDecimal r134_crm_sub_col_elig_equi) {
		this.r134_crm_sub_col_elig_equi = r134_crm_sub_col_elig_equi;
	}
	public BigDecimal getR134_crm_sub_col_elig_unit_tru() {
		return r134_crm_sub_col_elig_unit_tru;
	}
	public void setR134_crm_sub_col_elig_unit_tru(BigDecimal r134_crm_sub_col_elig_unit_tru) {
		this.r134_crm_sub_col_elig_unit_tru = r134_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR134_crm_sub_col_exp_cov() {
		return r134_crm_sub_col_exp_cov;
	}
	public void setR134_crm_sub_col_exp_cov(BigDecimal r134_crm_sub_col_exp_cov) {
		this.r134_crm_sub_col_exp_cov = r134_crm_sub_col_exp_cov;
	}
	public BigDecimal getR134_crm_sub_col_elig_exp_not_cov() {
		return r134_crm_sub_col_elig_exp_not_cov;
	}
	public void setR134_crm_sub_col_elig_exp_not_cov(BigDecimal r134_crm_sub_col_elig_exp_not_cov) {
		this.r134_crm_sub_col_elig_exp_not_cov = r134_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR134_crm_sub_rwa_ris_crm() {
		return r134_crm_sub_rwa_ris_crm;
	}
	public void setR134_crm_sub_rwa_ris_crm(BigDecimal r134_crm_sub_rwa_ris_crm) {
		this.r134_crm_sub_rwa_ris_crm = r134_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR134_crm_sub_rwa_cov_crm() {
		return r134_crm_sub_rwa_cov_crm;
	}
	public void setR134_crm_sub_rwa_cov_crm(BigDecimal r134_crm_sub_rwa_cov_crm) {
		this.r134_crm_sub_rwa_cov_crm = r134_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR134_crm_sub_rwa_org_cou() {
		return r134_crm_sub_rwa_org_cou;
	}
	public void setR134_crm_sub_rwa_org_cou(BigDecimal r134_crm_sub_rwa_org_cou) {
		this.r134_crm_sub_rwa_org_cou = r134_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR134_crm_sub_rwa_not_cov_crm() {
		return r134_crm_sub_rwa_not_cov_crm;
	}
	public void setR134_crm_sub_rwa_not_cov_crm(BigDecimal r134_crm_sub_rwa_not_cov_crm) {
		this.r134_crm_sub_rwa_not_cov_crm = r134_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR134_crm_comp_col_expo_elig() {
		return r134_crm_comp_col_expo_elig;
	}
	public void setR134_crm_comp_col_expo_elig(BigDecimal r134_crm_comp_col_expo_elig) {
		this.r134_crm_comp_col_expo_elig = r134_crm_comp_col_expo_elig;
	}
	public BigDecimal getR134_crm_comp_col_elig_expo_vol_adj() {
		return r134_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR134_crm_comp_col_elig_expo_vol_adj(BigDecimal r134_crm_comp_col_elig_expo_vol_adj) {
		this.r134_crm_comp_col_elig_expo_vol_adj = r134_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR134_crm_comp_col_elig_fin_hai() {
		return r134_crm_comp_col_elig_fin_hai;
	}
	public void setR134_crm_comp_col_elig_fin_hai(BigDecimal r134_crm_comp_col_elig_fin_hai) {
		this.r134_crm_comp_col_elig_fin_hai = r134_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR134_crm_comp_col_expo_val() {
		return r134_crm_comp_col_expo_val;
	}
	public void setR134_crm_comp_col_expo_val(BigDecimal r134_crm_comp_col_expo_val) {
		this.r134_crm_comp_col_expo_val = r134_crm_comp_col_expo_val;
	}
	public BigDecimal getR134_rwa_elig_expo_not_cov_crm() {
		return r134_rwa_elig_expo_not_cov_crm;
	}
	public void setR134_rwa_elig_expo_not_cov_crm(BigDecimal r134_rwa_elig_expo_not_cov_crm) {
		this.r134_rwa_elig_expo_not_cov_crm = r134_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR134_rwa_unsec_expo_cre_ris() {
		return r134_rwa_unsec_expo_cre_ris;
	}
	public void setR134_rwa_unsec_expo_cre_ris(BigDecimal r134_rwa_unsec_expo_cre_ris) {
		this.r134_rwa_unsec_expo_cre_ris = r134_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR134_rwa_unsec_expo() {
		return r134_rwa_unsec_expo;
	}
	public void setR134_rwa_unsec_expo(BigDecimal r134_rwa_unsec_expo) {
		this.r134_rwa_unsec_expo = r134_rwa_unsec_expo;
	}
	public BigDecimal getR134_rwa_tot_ris_wei_ass() {
		return r134_rwa_tot_ris_wei_ass;
	}
	public void setR134_rwa_tot_ris_wei_ass(BigDecimal r134_rwa_tot_ris_wei_ass) {
		this.r134_rwa_tot_ris_wei_ass = r134_rwa_tot_ris_wei_ass;
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
	public M_SRWA_12A_New_Archival_Summary_Entity4() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}