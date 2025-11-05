package com.bornfire.brrs.entities;


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
@Table(name = "BRRS_M_SRWA_12A_SUMMARYTABLE2")


public class M_SRWA_12A_Summary_Entity2 {
	
	
	private String	r60_exposure_class;
//	private BigDecimal	r60_expo_crm;
	private BigDecimal	r60_spe_pro_expo;
	private BigDecimal	r60_amt_elig_sht_net;
	private BigDecimal	r60_tot_expo_net_spe;
	private BigDecimal	r60_crm_sub_elig_sub_app;
	private BigDecimal	r60_crm_sub_non_col_guar;
	private BigDecimal	r60_crm_sub_non_col_cre_der;
	private BigDecimal	r60_crm_sub_col_elig_cash;
	private BigDecimal	r60_crm_sub_col_elig_trea_bills;
	private BigDecimal	r60_crm_sub_col_elig_deb_sec;
	private BigDecimal	r60_crm_sub_col_elig_equi;
	private BigDecimal	r60_crm_sub_col_elig_unit_tru;
	private BigDecimal	r60_crm_sub_col_exp_cov;
	private BigDecimal	r60_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r60_crm_sub_rwa_ris_crm;
	private BigDecimal	r60_crm_sub_rwa_cov_crm;
	private BigDecimal	r60_crm_sub_rwa_org_cou;
	private BigDecimal	r60_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r60_crm_comp_col_expo_elig;
	private BigDecimal	r60_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r60_crm_comp_col_elig_fin_hai;
	private BigDecimal	r60_crm_comp_col_expo_val;
	private BigDecimal	r60_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r60_rwa_unsec_expo_cre_ris;
	private BigDecimal	r60_rwa_unsec_expo;
	private BigDecimal	r60_rwa_tot_ris_wei_ass;

	private String	r61_exposure_class;
	private BigDecimal	r61_expo_crm;
	private BigDecimal	r61_spe_pro_expo;
	private BigDecimal	r61_amt_elig_sht_net;
	private BigDecimal	r61_tot_expo_net_spe;
	private BigDecimal	r61_crm_sub_elig_sub_app;
	private BigDecimal	r61_crm_sub_non_col_guar;
	private BigDecimal	r61_crm_sub_non_col_cre_der;
	private BigDecimal	r61_crm_sub_col_elig_cash;
	private BigDecimal	r61_crm_sub_col_elig_trea_bills;
	private BigDecimal	r61_crm_sub_col_elig_deb_sec;
	private BigDecimal	r61_crm_sub_col_elig_equi;
	private BigDecimal	r61_crm_sub_col_elig_unit_tru;
	private BigDecimal	r61_crm_sub_col_exp_cov;
	private BigDecimal	r61_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r61_crm_sub_rwa_ris_crm;
	private BigDecimal	r61_crm_sub_rwa_cov_crm;
	private BigDecimal	r61_crm_sub_rwa_org_cou;
	private BigDecimal	r61_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r61_crm_comp_col_expo_elig;
	private BigDecimal	r61_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r61_crm_comp_col_elig_fin_hai;
	private BigDecimal	r61_crm_comp_col_expo_val;
	private BigDecimal	r61_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r61_rwa_unsec_expo_cre_ris;
	private BigDecimal	r61_rwa_unsec_expo;
	private BigDecimal	r61_rwa_tot_ris_wei_ass;
	
	private String	r62_exposure_class;
	private BigDecimal	r62_expo_crm;
	private BigDecimal	r62_spe_pro_expo;
	private BigDecimal	r62_amt_elig_sht_net;
	private BigDecimal	r62_tot_expo_net_spe;
	private BigDecimal	r62_crm_sub_elig_sub_app;
	private BigDecimal	r62_crm_sub_non_col_guar;
	private BigDecimal	r62_crm_sub_non_col_cre_der;
	private BigDecimal	r62_crm_sub_col_elig_cash;
	private BigDecimal	r62_crm_sub_col_elig_trea_bills;
	private BigDecimal	r62_crm_sub_col_elig_deb_sec;
	private BigDecimal	r62_crm_sub_col_elig_equi;
	private BigDecimal	r62_crm_sub_col_elig_unit_tru;
	private BigDecimal	r62_crm_sub_col_exp_cov;
	private BigDecimal	r62_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r62_crm_sub_rwa_ris_crm;
	private BigDecimal	r62_crm_sub_rwa_cov_crm;
	private BigDecimal	r62_crm_sub_rwa_org_cou;
	private BigDecimal	r62_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r62_crm_comp_col_expo_elig;
	private BigDecimal	r62_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r62_crm_comp_col_elig_fin_hai;
	private BigDecimal	r62_crm_comp_col_expo_val;
	private BigDecimal	r62_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r62_rwa_unsec_expo_cre_ris;
	private BigDecimal	r62_rwa_unsec_expo;
	private BigDecimal	r62_rwa_tot_ris_wei_ass;
	
	private String	r63_exposure_class;
//	private BigDecimal	r63_expo_crm;
	private BigDecimal	r63_spe_pro_expo;
	private BigDecimal	r63_amt_elig_sht_net;
	private BigDecimal	r63_tot_expo_net_spe;
	private BigDecimal	r63_crm_sub_elig_sub_app;
	private BigDecimal	r63_crm_sub_non_col_guar;
	private BigDecimal	r63_crm_sub_non_col_cre_der;
	private BigDecimal	r63_crm_sub_col_elig_cash;
	private BigDecimal	r63_crm_sub_col_elig_trea_bills;
	private BigDecimal	r63_crm_sub_col_elig_deb_sec;
	private BigDecimal	r63_crm_sub_col_elig_equi;
	private BigDecimal	r63_crm_sub_col_elig_unit_tru;
	private BigDecimal	r63_crm_sub_col_exp_cov;
	private BigDecimal	r63_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r63_crm_sub_rwa_ris_crm;
	private BigDecimal	r63_crm_sub_rwa_cov_crm;
	private BigDecimal	r63_crm_sub_rwa_org_cou;
	private BigDecimal	r63_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r63_crm_comp_col_expo_elig;
	private BigDecimal	r63_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r63_crm_comp_col_elig_fin_hai;
	private BigDecimal	r63_crm_comp_col_expo_val;
	private BigDecimal	r63_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r63_rwa_unsec_expo_cre_ris;
	private BigDecimal	r63_rwa_unsec_expo;
	private BigDecimal	r63_rwa_tot_ris_wei_ass;
	
	private String	r64_exposure_class;
	private BigDecimal	r64_expo_crm;
	private BigDecimal	r64_spe_pro_expo;
	private BigDecimal	r64_amt_elig_sht_net;
	private BigDecimal	r64_tot_expo_net_spe;
	private BigDecimal	r64_crm_sub_elig_sub_app;
	private BigDecimal	r64_crm_sub_non_col_guar;
	private BigDecimal	r64_crm_sub_non_col_cre_der;
	private BigDecimal	r64_crm_sub_col_elig_cash;
	private BigDecimal	r64_crm_sub_col_elig_trea_bills;
	private BigDecimal	r64_crm_sub_col_elig_deb_sec;
	private BigDecimal	r64_crm_sub_col_elig_equi;
	private BigDecimal	r64_crm_sub_col_elig_unit_tru;
	private BigDecimal	r64_crm_sub_col_exp_cov;
	private BigDecimal	r64_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r64_crm_sub_rwa_ris_crm;
	private BigDecimal	r64_crm_sub_rwa_cov_crm;
	private BigDecimal	r64_crm_sub_rwa_org_cou;
	private BigDecimal	r64_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r64_crm_comp_col_expo_elig;
	private BigDecimal	r64_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r64_crm_comp_col_elig_fin_hai;
	private BigDecimal	r64_crm_comp_col_expo_val;
	private BigDecimal	r64_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r64_rwa_unsec_expo_cre_ris;
	private BigDecimal	r64_rwa_unsec_expo;
	private BigDecimal	r64_rwa_tot_ris_wei_ass;
	
	private String	r65_exposure_class;
	private BigDecimal	r65_expo_crm;
	private BigDecimal	r65_spe_pro_expo;
	private BigDecimal	r65_amt_elig_sht_net;
	private BigDecimal	r65_tot_expo_net_spe;
	private BigDecimal	r65_crm_sub_elig_sub_app;
	private BigDecimal	r65_crm_sub_non_col_guar;
	private BigDecimal	r65_crm_sub_non_col_cre_der;
	private BigDecimal	r65_crm_sub_col_elig_cash;
	private BigDecimal	r65_crm_sub_col_elig_trea_bills;
	private BigDecimal	r65_crm_sub_col_elig_deb_sec;
	private BigDecimal	r65_crm_sub_col_elig_equi;
	private BigDecimal	r65_crm_sub_col_elig_unit_tru;
	private BigDecimal	r65_crm_sub_col_exp_cov;
	private BigDecimal	r65_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r65_crm_sub_rwa_ris_crm;
	private BigDecimal	r65_crm_sub_rwa_cov_crm;
	private BigDecimal	r65_crm_sub_rwa_org_cou;
	private BigDecimal	r65_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r65_crm_comp_col_expo_elig;
	private BigDecimal	r65_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r65_crm_comp_col_elig_fin_hai;
	private BigDecimal	r65_crm_comp_col_expo_val;
	private BigDecimal	r65_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r65_rwa_unsec_expo_cre_ris;
	private BigDecimal	r65_rwa_unsec_expo;
	private BigDecimal	r65_rwa_tot_ris_wei_ass;
	
	private String	r66_exposure_class;
	private BigDecimal	r66_expo_crm;
	private BigDecimal	r66_spe_pro_expo;
	private BigDecimal	r66_amt_elig_sht_net;
	private BigDecimal	r66_tot_expo_net_spe;
	private BigDecimal	r66_crm_sub_elig_sub_app;
	private BigDecimal	r66_crm_sub_non_col_guar;
	private BigDecimal	r66_crm_sub_non_col_cre_der;
	private BigDecimal	r66_crm_sub_col_elig_cash;
	private BigDecimal	r66_crm_sub_col_elig_trea_bills;
	private BigDecimal	r66_crm_sub_col_elig_deb_sec;
	private BigDecimal	r66_crm_sub_col_elig_equi;
	private BigDecimal	r66_crm_sub_col_elig_unit_tru;
	private BigDecimal	r66_crm_sub_col_exp_cov;
	private BigDecimal	r66_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r66_crm_sub_rwa_ris_crm;
	private BigDecimal	r66_crm_sub_rwa_cov_crm;
	private BigDecimal	r66_crm_sub_rwa_org_cou;
	private BigDecimal	r66_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r66_crm_comp_col_expo_elig;
	private BigDecimal	r66_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r66_crm_comp_col_elig_fin_hai;
	private BigDecimal	r66_crm_comp_col_expo_val;
	private BigDecimal	r66_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r66_rwa_unsec_expo_cre_ris;
	private BigDecimal	r66_rwa_unsec_expo;
	private BigDecimal	r66_rwa_tot_ris_wei_ass;

	private String	r67_exposure_class;
	private BigDecimal	r67_expo_crm;
	private BigDecimal	r67_spe_pro_expo;
	private BigDecimal	r67_amt_elig_sht_net;
	private BigDecimal	r67_tot_expo_net_spe;
	private BigDecimal	r67_crm_sub_elig_sub_app;
	private BigDecimal	r67_crm_sub_non_col_guar;
	private BigDecimal	r67_crm_sub_non_col_cre_der;
	private BigDecimal	r67_crm_sub_col_elig_cash;
	private BigDecimal	r67_crm_sub_col_elig_trea_bills;
	private BigDecimal	r67_crm_sub_col_elig_deb_sec;
	private BigDecimal	r67_crm_sub_col_elig_equi;
	private BigDecimal	r67_crm_sub_col_elig_unit_tru;
	private BigDecimal	r67_crm_sub_col_exp_cov;
	private BigDecimal	r67_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r67_crm_sub_rwa_ris_crm;
	private BigDecimal	r67_crm_sub_rwa_cov_crm;
	private BigDecimal	r67_crm_sub_rwa_org_cou;
	private BigDecimal	r67_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r67_crm_comp_col_expo_elig;
	private BigDecimal	r67_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r67_crm_comp_col_elig_fin_hai;
	private BigDecimal	r67_crm_comp_col_expo_val;
	private BigDecimal	r67_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r67_rwa_unsec_expo_cre_ris;
	private BigDecimal	r67_rwa_unsec_expo;
	private BigDecimal	r67_rwa_tot_ris_wei_ass;
	
	private String	r68_exposure_class;
	private BigDecimal	r68_expo_crm;
	private BigDecimal	r68_spe_pro_expo;
	private BigDecimal	r68_amt_elig_sht_net;
	private BigDecimal	r68_tot_expo_net_spe;
	private BigDecimal	r68_crm_sub_elig_sub_app;
	private BigDecimal	r68_crm_sub_non_col_guar;
	private BigDecimal	r68_crm_sub_non_col_cre_der;
	private BigDecimal	r68_crm_sub_col_elig_cash;
	private BigDecimal	r68_crm_sub_col_elig_trea_bills;
	private BigDecimal	r68_crm_sub_col_elig_deb_sec;
	private BigDecimal	r68_crm_sub_col_elig_equi;
	private BigDecimal	r68_crm_sub_col_elig_unit_tru;
	private BigDecimal	r68_crm_sub_col_exp_cov;
	private BigDecimal	r68_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r68_crm_sub_rwa_ris_crm;
	private BigDecimal	r68_crm_sub_rwa_cov_crm;
	private BigDecimal	r68_crm_sub_rwa_org_cou;
	private BigDecimal	r68_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r68_crm_comp_col_expo_elig;
	private BigDecimal	r68_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r68_crm_comp_col_elig_fin_hai;
	private BigDecimal	r68_crm_comp_col_expo_val;
	private BigDecimal	r68_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r68_rwa_unsec_expo_cre_ris;
	private BigDecimal	r68_rwa_unsec_expo;
	private BigDecimal	r68_rwa_tot_ris_wei_ass;
	
	private String	r69_exposure_class;
	private BigDecimal	r69_expo_crm;
	private BigDecimal	r69_spe_pro_expo;
	private BigDecimal	r69_amt_elig_sht_net;
	private BigDecimal	r69_tot_expo_net_spe;
	private BigDecimal	r69_crm_sub_elig_sub_app;
	private BigDecimal	r69_crm_sub_non_col_guar;
	private BigDecimal	r69_crm_sub_non_col_cre_der;
	private BigDecimal	r69_crm_sub_col_elig_cash;
	private BigDecimal	r69_crm_sub_col_elig_trea_bills;
	private BigDecimal	r69_crm_sub_col_elig_deb_sec;
	private BigDecimal	r69_crm_sub_col_elig_equi;
	private BigDecimal	r69_crm_sub_col_elig_unit_tru;
	private BigDecimal	r69_crm_sub_col_exp_cov;
	private BigDecimal	r69_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r69_crm_sub_rwa_ris_crm;
	private BigDecimal	r69_crm_sub_rwa_cov_crm;
	private BigDecimal	r69_crm_sub_rwa_org_cou;
	private BigDecimal	r69_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r69_crm_comp_col_expo_elig;
	private BigDecimal	r69_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r69_crm_comp_col_elig_fin_hai;
	private BigDecimal	r69_crm_comp_col_expo_val;
	private BigDecimal	r69_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r69_rwa_unsec_expo_cre_ris;
	private BigDecimal	r69_rwa_unsec_expo;
	private BigDecimal	r69_rwa_tot_ris_wei_ass;
	
	private String	r70_exposure_class;
	private BigDecimal	r70_expo_crm;
	private BigDecimal	r70_spe_pro_expo;
	private BigDecimal	r70_amt_elig_sht_net;
	private BigDecimal	r70_tot_expo_net_spe;
	private BigDecimal	r70_crm_sub_elig_sub_app;
	private BigDecimal	r70_crm_sub_non_col_guar;
	private BigDecimal	r70_crm_sub_non_col_cre_der;
	private BigDecimal	r70_crm_sub_col_elig_cash;
	private BigDecimal	r70_crm_sub_col_elig_trea_bills;
	private BigDecimal	r70_crm_sub_col_elig_deb_sec;
	private BigDecimal	r70_crm_sub_col_elig_equi;
	private BigDecimal	r70_crm_sub_col_elig_unit_tru;
	private BigDecimal	r70_crm_sub_col_exp_cov;
	private BigDecimal	r70_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r70_crm_sub_rwa_ris_crm;
	private BigDecimal	r70_crm_sub_rwa_cov_crm;
	private BigDecimal	r70_crm_sub_rwa_org_cou;
	private BigDecimal	r70_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r70_crm_comp_col_expo_elig;
	private BigDecimal	r70_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r70_crm_comp_col_elig_fin_hai;
	private BigDecimal	r70_crm_comp_col_expo_val;
	private BigDecimal	r70_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r70_rwa_unsec_expo_cre_ris;
	private BigDecimal	r70_rwa_unsec_expo;
	private BigDecimal	r70_rwa_tot_ris_wei_ass;
	
	private String	r73_exposure_class;
	private BigDecimal	r73_expo_crm;
	private BigDecimal	r73_spe_pro_expo;
	private BigDecimal	r73_amt_elig_sht_net;
	private BigDecimal	r73_tot_expo_net_spe;
	private BigDecimal	r73_crm_sub_elig_sub_app;
	private BigDecimal	r73_crm_sub_non_col_guar;
	private BigDecimal	r73_crm_sub_non_col_cre_der;
	private BigDecimal	r73_crm_sub_col_elig_cash;
	private BigDecimal	r73_crm_sub_col_elig_trea_bills;
	private BigDecimal	r73_crm_sub_col_elig_deb_sec;
	private BigDecimal	r73_crm_sub_col_elig_equi;
	private BigDecimal	r73_crm_sub_col_elig_unit_tru;
	private BigDecimal	r73_crm_sub_col_exp_cov;
	private BigDecimal	r73_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r73_crm_sub_rwa_ris_crm;
	private BigDecimal	r73_crm_sub_rwa_cov_crm;
	private BigDecimal	r73_crm_sub_rwa_org_cou;
	private BigDecimal	r73_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r73_crm_comp_col_expo_elig;
	private BigDecimal	r73_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r73_crm_comp_col_elig_fin_hai;
	private BigDecimal	r73_crm_comp_col_expo_val;
	private BigDecimal	r73_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r73_rwa_unsec_expo_cre_ris;
	private BigDecimal	r73_rwa_unsec_expo;
	private BigDecimal	r73_rwa_tot_ris_wei_ass;
	
	private String	r74_exposure_class;
	private BigDecimal	r74_expo_crm;
	private BigDecimal	r74_spe_pro_expo;
	private BigDecimal	r74_amt_elig_sht_net;
	private BigDecimal	r74_tot_expo_net_spe;
	private BigDecimal	r74_crm_sub_elig_sub_app;
	private BigDecimal	r74_crm_sub_non_col_guar;
	private BigDecimal	r74_crm_sub_non_col_cre_der;
	private BigDecimal	r74_crm_sub_col_elig_cash;
	private BigDecimal	r74_crm_sub_col_elig_trea_bills;
	private BigDecimal	r74_crm_sub_col_elig_deb_sec;
	private BigDecimal	r74_crm_sub_col_elig_equi;
	private BigDecimal	r74_crm_sub_col_elig_unit_tru;
	private BigDecimal	r74_crm_sub_col_exp_cov;
	private BigDecimal	r74_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r74_crm_sub_rwa_ris_crm;
	private BigDecimal	r74_crm_sub_rwa_cov_crm;
	private BigDecimal	r74_crm_sub_rwa_org_cou;
	private BigDecimal	r74_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r74_crm_comp_col_expo_elig;
	private BigDecimal	r74_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r74_crm_comp_col_elig_fin_hai;
	private BigDecimal	r74_crm_comp_col_expo_val;
	private BigDecimal	r74_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r74_rwa_unsec_expo_cre_ris;
	private BigDecimal	r74_rwa_unsec_expo;
	private BigDecimal	r74_rwa_tot_ris_wei_ass;
	
	private String	r75_exposure_class;
	private BigDecimal	r75_expo_crm;
	private BigDecimal	r75_spe_pro_expo;
	private BigDecimal	r75_amt_elig_sht_net;
	private BigDecimal	r75_tot_expo_net_spe;
	private BigDecimal	r75_crm_sub_elig_sub_app;
	private BigDecimal	r75_crm_sub_non_col_guar;
	private BigDecimal	r75_crm_sub_non_col_cre_der;
	private BigDecimal	r75_crm_sub_col_elig_cash;
	private BigDecimal	r75_crm_sub_col_elig_trea_bills;
	private BigDecimal	r75_crm_sub_col_elig_deb_sec;
	private BigDecimal	r75_crm_sub_col_elig_equi;
	private BigDecimal	r75_crm_sub_col_elig_unit_tru;
	private BigDecimal	r75_crm_sub_col_exp_cov;
	private BigDecimal	r75_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r75_crm_sub_rwa_ris_crm;
	private BigDecimal	r75_crm_sub_rwa_cov_crm;
	private BigDecimal	r75_crm_sub_rwa_org_cou;
	private BigDecimal	r75_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r75_crm_comp_col_expo_elig;
	private BigDecimal	r75_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r75_crm_comp_col_elig_fin_hai;
	private BigDecimal	r75_crm_comp_col_expo_val;
	private BigDecimal	r75_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r75_rwa_unsec_expo_cre_ris;
	private BigDecimal	r75_rwa_unsec_expo;
	private BigDecimal	r75_rwa_tot_ris_wei_ass;
	
	private String	r76_exposure_class;
	private BigDecimal	r76_expo_crm;
	private BigDecimal	r76_spe_pro_expo;
	private BigDecimal	r76_amt_elig_sht_net;
	private BigDecimal	r76_tot_expo_net_spe;
	private BigDecimal	r76_crm_sub_elig_sub_app;
	private BigDecimal	r76_crm_sub_non_col_guar;
	private BigDecimal	r76_crm_sub_non_col_cre_der;
	private BigDecimal	r76_crm_sub_col_elig_cash;
	private BigDecimal	r76_crm_sub_col_elig_trea_bills;
	private BigDecimal	r76_crm_sub_col_elig_deb_sec;
	private BigDecimal	r76_crm_sub_col_elig_equi;
	private BigDecimal	r76_crm_sub_col_elig_unit_tru;
	private BigDecimal	r76_crm_sub_col_exp_cov;
	private BigDecimal	r76_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r76_crm_sub_rwa_ris_crm;
	private BigDecimal	r76_crm_sub_rwa_cov_crm;
	private BigDecimal	r76_crm_sub_rwa_org_cou;
	private BigDecimal	r76_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r76_crm_comp_col_expo_elig;
	private BigDecimal	r76_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r76_crm_comp_col_elig_fin_hai;
	private BigDecimal	r76_crm_comp_col_expo_val;
	private BigDecimal	r76_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r76_rwa_unsec_expo_cre_ris;
	private BigDecimal	r76_rwa_unsec_expo;
	private BigDecimal	r76_rwa_tot_ris_wei_ass;
	
	private String	r77_exposure_class;
	private BigDecimal	r77_expo_crm;
	private BigDecimal	r77_spe_pro_expo;
	private BigDecimal	r77_amt_elig_sht_net;
	private BigDecimal	r77_tot_expo_net_spe;
	private BigDecimal	r77_crm_sub_elig_sub_app;
	private BigDecimal	r77_crm_sub_non_col_guar;
	private BigDecimal	r77_crm_sub_non_col_cre_der;
	private BigDecimal	r77_crm_sub_col_elig_cash;
	private BigDecimal	r77_crm_sub_col_elig_trea_bills;
	private BigDecimal	r77_crm_sub_col_elig_deb_sec;
	private BigDecimal	r77_crm_sub_col_elig_equi;
	private BigDecimal	r77_crm_sub_col_elig_unit_tru;
	private BigDecimal	r77_crm_sub_col_exp_cov;
	private BigDecimal	r77_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r77_crm_sub_rwa_ris_crm;
	private BigDecimal	r77_crm_sub_rwa_cov_crm;
	private BigDecimal	r77_crm_sub_rwa_org_cou;
	private BigDecimal	r77_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r77_crm_comp_col_expo_elig;
	private BigDecimal	r77_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r77_crm_comp_col_elig_fin_hai;
	private BigDecimal	r77_crm_comp_col_expo_val;
	private BigDecimal	r77_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r77_rwa_unsec_expo_cre_ris;
	private BigDecimal	r77_rwa_unsec_expo;
	private BigDecimal	r77_rwa_tot_ris_wei_ass;
	
	private String	r78_exposure_class;
	private BigDecimal	r78_expo_crm;
	private BigDecimal	r78_spe_pro_expo;
	private BigDecimal	r78_amt_elig_sht_net;
	private BigDecimal	r78_tot_expo_net_spe;
	private BigDecimal	r78_crm_sub_elig_sub_app;
	private BigDecimal	r78_crm_sub_non_col_guar;
	private BigDecimal	r78_crm_sub_non_col_cre_der;
	private BigDecimal	r78_crm_sub_col_elig_cash;
	private BigDecimal	r78_crm_sub_col_elig_trea_bills;
	private BigDecimal	r78_crm_sub_col_elig_deb_sec;
	private BigDecimal	r78_crm_sub_col_elig_equi;
	private BigDecimal	r78_crm_sub_col_elig_unit_tru;
	private BigDecimal	r78_crm_sub_col_exp_cov;
	private BigDecimal	r78_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r78_crm_sub_rwa_ris_crm;
	private BigDecimal	r78_crm_sub_rwa_cov_crm;
	private BigDecimal	r78_crm_sub_rwa_org_cou;
	private BigDecimal	r78_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r78_crm_comp_col_expo_elig;
	private BigDecimal	r78_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r78_crm_comp_col_elig_fin_hai;
	private BigDecimal	r78_crm_comp_col_expo_val;
	private BigDecimal	r78_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r78_rwa_unsec_expo_cre_ris;
	private BigDecimal	r78_rwa_unsec_expo;
	private BigDecimal	r78_rwa_tot_ris_wei_ass;
	
	private String	r79_exposure_class;
	private BigDecimal	r79_expo_crm;
	private BigDecimal	r79_spe_pro_expo;
	private BigDecimal	r79_amt_elig_sht_net;
	private BigDecimal	r79_tot_expo_net_spe;
	private BigDecimal	r79_crm_sub_elig_sub_app;
	private BigDecimal	r79_crm_sub_non_col_guar;
	private BigDecimal	r79_crm_sub_non_col_cre_der;
	private BigDecimal	r79_crm_sub_col_elig_cash;
	private BigDecimal	r79_crm_sub_col_elig_trea_bills;
	private BigDecimal	r79_crm_sub_col_elig_deb_sec;
	private BigDecimal	r79_crm_sub_col_elig_equi;
	private BigDecimal	r79_crm_sub_col_elig_unit_tru;
	private BigDecimal	r79_crm_sub_col_exp_cov;
	private BigDecimal	r79_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r79_crm_sub_rwa_ris_crm;
	private BigDecimal	r79_crm_sub_rwa_cov_crm;
	private BigDecimal	r79_crm_sub_rwa_org_cou;
	private BigDecimal	r79_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r79_crm_comp_col_expo_elig;
	private BigDecimal	r79_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r79_crm_comp_col_elig_fin_hai;
	private BigDecimal	r79_crm_comp_col_expo_val;
	private BigDecimal	r79_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r79_rwa_unsec_expo_cre_ris;
	private BigDecimal	r79_rwa_unsec_expo;
	private BigDecimal	r79_rwa_tot_ris_wei_ass;
	
	private String	r80_exposure_class;
	private BigDecimal	r80_expo_crm;
	private BigDecimal	r80_spe_pro_expo;
	private BigDecimal	r80_amt_elig_sht_net;
	private BigDecimal	r80_tot_expo_net_spe;
	private BigDecimal	r80_crm_sub_elig_sub_app;
	private BigDecimal	r80_crm_sub_non_col_guar;
	private BigDecimal	r80_crm_sub_non_col_cre_der;
	private BigDecimal	r80_crm_sub_col_elig_cash;
	private BigDecimal	r80_crm_sub_col_elig_trea_bills;
	private BigDecimal	r80_crm_sub_col_elig_deb_sec;
	private BigDecimal	r80_crm_sub_col_elig_equi;
	private BigDecimal	r80_crm_sub_col_elig_unit_tru;
	private BigDecimal	r80_crm_sub_col_exp_cov;
	private BigDecimal	r80_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r80_crm_sub_rwa_ris_crm;
	private BigDecimal	r80_crm_sub_rwa_cov_crm;
	private BigDecimal	r80_crm_sub_rwa_org_cou;
	private BigDecimal	r80_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r80_crm_comp_col_expo_elig;
	private BigDecimal	r80_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r80_crm_comp_col_elig_fin_hai;
	private BigDecimal	r80_crm_comp_col_expo_val;
	private BigDecimal	r80_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r80_rwa_unsec_expo_cre_ris;
	private BigDecimal	r80_rwa_unsec_expo;
	private BigDecimal	r80_rwa_tot_ris_wei_ass;
	
	private String	r81_exposure_class;
	private BigDecimal	r81_expo_crm;
	private BigDecimal	r81_spe_pro_expo;
	private BigDecimal	r81_amt_elig_sht_net;
	private BigDecimal	r81_tot_expo_net_spe;
	private BigDecimal	r81_crm_sub_elig_sub_app;
	private BigDecimal	r81_crm_sub_non_col_guar;
	private BigDecimal	r81_crm_sub_non_col_cre_der;
	private BigDecimal	r81_crm_sub_col_elig_cash;
	private BigDecimal	r81_crm_sub_col_elig_trea_bills;
	private BigDecimal	r81_crm_sub_col_elig_deb_sec;
	private BigDecimal	r81_crm_sub_col_elig_equi;
	private BigDecimal	r81_crm_sub_col_elig_unit_tru;
	private BigDecimal	r81_crm_sub_col_exp_cov;
	private BigDecimal	r81_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r81_crm_sub_rwa_ris_crm;
	private BigDecimal	r81_crm_sub_rwa_cov_crm;
	private BigDecimal	r81_crm_sub_rwa_org_cou;
	private BigDecimal	r81_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r81_crm_comp_col_expo_elig;
	private BigDecimal	r81_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r81_crm_comp_col_elig_fin_hai;
	private BigDecimal	r81_crm_comp_col_expo_val;
	private BigDecimal	r81_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r81_rwa_unsec_expo_cre_ris;
	private BigDecimal	r81_rwa_unsec_expo;
	private BigDecimal	r81_rwa_tot_ris_wei_ass;

	private String	r82_exposure_class;
	private BigDecimal	r82_expo_crm;
	private BigDecimal	r82_spe_pro_expo;
	private BigDecimal	r82_amt_elig_sht_net;
	private BigDecimal	r82_tot_expo_net_spe;
	private BigDecimal	r82_crm_sub_elig_sub_app;
	private BigDecimal	r82_crm_sub_non_col_guar;
	private BigDecimal	r82_crm_sub_non_col_cre_der;
	private BigDecimal	r82_crm_sub_col_elig_cash;
	private BigDecimal	r82_crm_sub_col_elig_trea_bills;
	private BigDecimal	r82_crm_sub_col_elig_deb_sec;
	private BigDecimal	r82_crm_sub_col_elig_equi;
	private BigDecimal	r82_crm_sub_col_elig_unit_tru;
	private BigDecimal	r82_crm_sub_col_exp_cov;
	private BigDecimal	r82_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r82_crm_sub_rwa_ris_crm;
	private BigDecimal	r82_crm_sub_rwa_cov_crm;
	private BigDecimal	r82_crm_sub_rwa_org_cou;
	private BigDecimal	r82_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r82_crm_comp_col_expo_elig;
	private BigDecimal	r82_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r82_crm_comp_col_elig_fin_hai;
	private BigDecimal	r82_crm_comp_col_expo_val;
	private BigDecimal	r82_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r82_rwa_unsec_expo_cre_ris;
	private BigDecimal	r82_rwa_unsec_expo;
	private BigDecimal	r82_rwa_tot_ris_wei_ass;
	
	private String	r85_exposure_class;
	private BigDecimal	r85_expo_crm;
	private BigDecimal	r85_spe_pro_expo;
	private BigDecimal	r85_amt_elig_sht_net;
	private BigDecimal	r85_tot_expo_net_spe;
	private BigDecimal	r85_crm_sub_elig_sub_app;
	private BigDecimal	r85_crm_sub_non_col_guar;
	private BigDecimal	r85_crm_sub_non_col_cre_der;
	private BigDecimal	r85_crm_sub_col_elig_cash;
	private BigDecimal	r85_crm_sub_col_elig_trea_bills;
	private BigDecimal	r85_crm_sub_col_elig_deb_sec;
	private BigDecimal	r85_crm_sub_col_elig_equi;
	private BigDecimal	r85_crm_sub_col_elig_unit_tru;
	private BigDecimal	r85_crm_sub_col_exp_cov;
	private BigDecimal	r85_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r85_crm_sub_rwa_ris_crm;
	private BigDecimal	r85_crm_sub_rwa_cov_crm;
	private BigDecimal	r85_crm_sub_rwa_org_cou;
	private BigDecimal	r85_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r85_crm_comp_col_expo_elig;
	private BigDecimal	r85_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r85_crm_comp_col_elig_fin_hai;
	private BigDecimal	r85_crm_comp_col_expo_val;
	private BigDecimal	r85_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r85_rwa_unsec_expo_cre_ris;
	private BigDecimal	r85_rwa_unsec_expo;
	private BigDecimal	r85_rwa_tot_ris_wei_ass;
	
	private String	r86_exposure_class;
	private BigDecimal	r86_expo_crm;
	private BigDecimal	r86_spe_pro_expo;
	private BigDecimal	r86_amt_elig_sht_net;
	private BigDecimal	r86_tot_expo_net_spe;
	private BigDecimal	r86_crm_sub_elig_sub_app;
	private BigDecimal	r86_crm_sub_non_col_guar;
	private BigDecimal	r86_crm_sub_non_col_cre_der;
	private BigDecimal	r86_crm_sub_col_elig_cash;
	private BigDecimal	r86_crm_sub_col_elig_trea_bills;
	private BigDecimal	r86_crm_sub_col_elig_deb_sec;
	private BigDecimal	r86_crm_sub_col_elig_equi;
	private BigDecimal	r86_crm_sub_col_elig_unit_tru;
	private BigDecimal	r86_crm_sub_col_exp_cov;
	private BigDecimal	r86_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r86_crm_sub_rwa_ris_crm;
	private BigDecimal	r86_crm_sub_rwa_cov_crm;
	private BigDecimal	r86_crm_sub_rwa_org_cou;
	private BigDecimal	r86_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r86_crm_comp_col_expo_elig;
	private BigDecimal	r86_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r86_crm_comp_col_elig_fin_hai;
	private BigDecimal	r86_crm_comp_col_expo_val;
	private BigDecimal	r86_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r86_rwa_unsec_expo_cre_ris;
	private BigDecimal	r86_rwa_unsec_expo;
	private BigDecimal	r86_rwa_tot_ris_wei_ass;
	
	private String	r87_exposure_class;
//	private BigDecimal	r87_expo_crm;
	private BigDecimal	r87_spe_pro_expo;
	private BigDecimal	r87_amt_elig_sht_net;
	private BigDecimal	r87_tot_expo_net_spe;
	private BigDecimal	r87_crm_sub_elig_sub_app;
	private BigDecimal	r87_crm_sub_non_col_guar;
	private BigDecimal	r87_crm_sub_non_col_cre_der;
	private BigDecimal	r87_crm_sub_col_elig_cash;
	private BigDecimal	r87_crm_sub_col_elig_trea_bills;
	private BigDecimal	r87_crm_sub_col_elig_deb_sec;
	private BigDecimal	r87_crm_sub_col_elig_equi;
	private BigDecimal	r87_crm_sub_col_elig_unit_tru;
	private BigDecimal	r87_crm_sub_col_exp_cov;
	private BigDecimal	r87_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r87_crm_sub_rwa_ris_crm;
	private BigDecimal	r87_crm_sub_rwa_cov_crm;
	private BigDecimal	r87_crm_sub_rwa_org_cou;
	private BigDecimal	r87_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r87_crm_comp_col_expo_elig;
	private BigDecimal	r87_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r87_crm_comp_col_elig_fin_hai;
	private BigDecimal	r87_crm_comp_col_expo_val;
	private BigDecimal	r87_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r87_rwa_unsec_expo_cre_ris;
	private BigDecimal	r87_rwa_unsec_expo;
	private BigDecimal	r87_rwa_tot_ris_wei_ass;
	
	private String	r88_exposure_class;
	private BigDecimal	r88_expo_crm;
	private BigDecimal	r88_spe_pro_expo;
	private BigDecimal	r88_amt_elig_sht_net;
	private BigDecimal	r88_tot_expo_net_spe;
	private BigDecimal	r88_crm_sub_elig_sub_app;
	private BigDecimal	r88_crm_sub_non_col_guar;
	private BigDecimal	r88_crm_sub_non_col_cre_der;
	private BigDecimal	r88_crm_sub_col_elig_cash;
	private BigDecimal	r88_crm_sub_col_elig_trea_bills;
	private BigDecimal	r88_crm_sub_col_elig_deb_sec;
	private BigDecimal	r88_crm_sub_col_elig_equi;
	private BigDecimal	r88_crm_sub_col_elig_unit_tru;
	private BigDecimal	r88_crm_sub_col_exp_cov;
	private BigDecimal	r88_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r88_crm_sub_rwa_ris_crm;
	private BigDecimal	r88_crm_sub_rwa_cov_crm;
	private BigDecimal	r88_crm_sub_rwa_org_cou;
	private BigDecimal	r88_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r88_crm_comp_col_expo_elig;
	private BigDecimal	r88_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r88_crm_comp_col_elig_fin_hai;
	private BigDecimal	r88_crm_comp_col_expo_val;
	private BigDecimal	r88_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r88_rwa_unsec_expo_cre_ris;
	private BigDecimal	r88_rwa_unsec_expo;
	private BigDecimal	r88_rwa_tot_ris_wei_ass;

	private String	r89_exposure_class;
	private BigDecimal	r89_expo_crm;
	private BigDecimal	r89_spe_pro_expo;
	private BigDecimal	r89_amt_elig_sht_net;
	private BigDecimal	r89_tot_expo_net_spe;
	private BigDecimal	r89_crm_sub_elig_sub_app;
	private BigDecimal	r89_crm_sub_non_col_guar;
	private BigDecimal	r89_crm_sub_non_col_cre_der;
	private BigDecimal	r89_crm_sub_col_elig_cash;
	private BigDecimal	r89_crm_sub_col_elig_trea_bills;
	private BigDecimal	r89_crm_sub_col_elig_deb_sec;
	private BigDecimal	r89_crm_sub_col_elig_equi;
	private BigDecimal	r89_crm_sub_col_elig_unit_tru;
	private BigDecimal	r89_crm_sub_col_exp_cov;
	private BigDecimal	r89_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r89_crm_sub_rwa_ris_crm;
	private BigDecimal	r89_crm_sub_rwa_cov_crm;
	private BigDecimal	r89_crm_sub_rwa_org_cou;
	private BigDecimal	r89_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r89_crm_comp_col_expo_elig;
	private BigDecimal	r89_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r89_crm_comp_col_elig_fin_hai;
	private BigDecimal	r89_crm_comp_col_expo_val;
	private BigDecimal	r89_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r89_rwa_unsec_expo_cre_ris;
	private BigDecimal	r89_rwa_unsec_expo;
	private BigDecimal	r89_rwa_tot_ris_wei_ass;
	
	private String	r90_exposure_class;
	private BigDecimal	r90_expo_crm;
	private BigDecimal	r90_spe_pro_expo;
	private BigDecimal	r90_amt_elig_sht_net;
	private BigDecimal	r90_tot_expo_net_spe;
	private BigDecimal	r90_crm_sub_elig_sub_app;
	private BigDecimal	r90_crm_sub_non_col_guar;
	private BigDecimal	r90_crm_sub_non_col_cre_der;
	private BigDecimal	r90_crm_sub_col_elig_cash;
	private BigDecimal	r90_crm_sub_col_elig_trea_bills;
	private BigDecimal	r90_crm_sub_col_elig_deb_sec;
	private BigDecimal	r90_crm_sub_col_elig_equi;
	private BigDecimal	r90_crm_sub_col_elig_unit_tru;
	private BigDecimal	r90_crm_sub_col_exp_cov;
	private BigDecimal	r90_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r90_crm_sub_rwa_ris_crm;
	private BigDecimal	r90_crm_sub_rwa_cov_crm;
	private BigDecimal	r90_crm_sub_rwa_org_cou;
	private BigDecimal	r90_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r90_crm_comp_col_expo_elig;
	private BigDecimal	r90_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r90_crm_comp_col_elig_fin_hai;
	private BigDecimal	r90_crm_comp_col_expo_val;
	private BigDecimal	r90_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r90_rwa_unsec_expo_cre_ris;
	private BigDecimal	r90_rwa_unsec_expo;
	private BigDecimal	r90_rwa_tot_ris_wei_ass;

	private String	r91_exposure_class;
	private BigDecimal	r91_expo_crm;
	private BigDecimal	r91_spe_pro_expo;
	private BigDecimal	r91_amt_elig_sht_net;
	private BigDecimal	r91_tot_expo_net_spe;
	private BigDecimal	r91_crm_sub_elig_sub_app;
	private BigDecimal	r91_crm_sub_non_col_guar;
	private BigDecimal	r91_crm_sub_non_col_cre_der;
	private BigDecimal	r91_crm_sub_col_elig_cash;
	private BigDecimal	r91_crm_sub_col_elig_trea_bills;
	private BigDecimal	r91_crm_sub_col_elig_deb_sec;
	private BigDecimal	r91_crm_sub_col_elig_equi;
	private BigDecimal	r91_crm_sub_col_elig_unit_tru;
	private BigDecimal	r91_crm_sub_col_exp_cov;
	private BigDecimal	r91_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r91_crm_sub_rwa_ris_crm;
	private BigDecimal	r91_crm_sub_rwa_cov_crm;
	private BigDecimal	r91_crm_sub_rwa_org_cou;
	private BigDecimal	r91_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r91_crm_comp_col_expo_elig;
	private BigDecimal	r91_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r91_crm_comp_col_elig_fin_hai;
	private BigDecimal	r91_crm_comp_col_expo_val;
	private BigDecimal	r91_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r91_rwa_unsec_expo_cre_ris;
	private BigDecimal	r91_rwa_unsec_expo;
	private BigDecimal	r91_rwa_tot_ris_wei_ass;
	
	private String	r92_exposure_class;
//	private BigDecimal	r92_expo_crm;
	private BigDecimal	r92_spe_pro_expo;
	private BigDecimal	r92_amt_elig_sht_net;
	private BigDecimal	r92_tot_expo_net_spe;
	private BigDecimal	r92_crm_sub_elig_sub_app;
	private BigDecimal	r92_crm_sub_non_col_guar;
	private BigDecimal	r92_crm_sub_non_col_cre_der;
	private BigDecimal	r92_crm_sub_col_elig_cash;
	private BigDecimal	r92_crm_sub_col_elig_trea_bills;
	private BigDecimal	r92_crm_sub_col_elig_deb_sec;
	private BigDecimal	r92_crm_sub_col_elig_equi;
	private BigDecimal	r92_crm_sub_col_elig_unit_tru;
	private BigDecimal	r92_crm_sub_col_exp_cov;
	private BigDecimal	r92_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r92_crm_sub_rwa_ris_crm;
	private BigDecimal	r92_crm_sub_rwa_cov_crm;
	private BigDecimal	r92_crm_sub_rwa_org_cou;
	private BigDecimal	r92_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r92_crm_comp_col_expo_elig;
	private BigDecimal	r92_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r92_crm_comp_col_elig_fin_hai;
	private BigDecimal	r92_crm_comp_col_expo_val;
	private BigDecimal	r92_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r92_rwa_unsec_expo_cre_ris;
	private BigDecimal	r92_rwa_unsec_expo;
	private BigDecimal	r92_rwa_tot_ris_wei_ass;
	
	private String	r93_exposure_class;
	private BigDecimal	r93_expo_crm;
	private BigDecimal	r93_spe_pro_expo;
	private BigDecimal	r93_amt_elig_sht_net;
	private BigDecimal	r93_tot_expo_net_spe;
	private BigDecimal	r93_crm_sub_elig_sub_app;
	private BigDecimal	r93_crm_sub_non_col_guar;
	private BigDecimal	r93_crm_sub_non_col_cre_der;
	private BigDecimal	r93_crm_sub_col_elig_cash;
	private BigDecimal	r93_crm_sub_col_elig_trea_bills;
	private BigDecimal	r93_crm_sub_col_elig_deb_sec;
	private BigDecimal	r93_crm_sub_col_elig_equi;
	private BigDecimal	r93_crm_sub_col_elig_unit_tru;
	private BigDecimal	r93_crm_sub_col_exp_cov;
	private BigDecimal	r93_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r93_crm_sub_rwa_ris_crm;
	private BigDecimal	r93_crm_sub_rwa_cov_crm;
	private BigDecimal	r93_crm_sub_rwa_org_cou;
	private BigDecimal	r93_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r93_crm_comp_col_expo_elig;
	private BigDecimal	r93_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r93_crm_comp_col_elig_fin_hai;
	private BigDecimal	r93_crm_comp_col_expo_val;
	private BigDecimal	r93_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r93_rwa_unsec_expo_cre_ris;
	private BigDecimal	r93_rwa_unsec_expo;
	private BigDecimal	r93_rwa_tot_ris_wei_ass;
	
	private String	r96_exposure_class;
//	private BigDecimal	r96_expo_crm;
	private BigDecimal	r96_spe_pro_expo;
	private BigDecimal	r96_amt_elig_sht_net;
	private BigDecimal	r96_tot_expo_net_spe;
	private BigDecimal	r96_crm_sub_elig_sub_app;
	private BigDecimal	r96_crm_sub_non_col_guar;
	private BigDecimal	r96_crm_sub_non_col_cre_der;
	private BigDecimal	r96_crm_sub_col_elig_cash;
	private BigDecimal	r96_crm_sub_col_elig_trea_bills;
	private BigDecimal	r96_crm_sub_col_elig_deb_sec;
	private BigDecimal	r96_crm_sub_col_elig_equi;
	private BigDecimal	r96_crm_sub_col_elig_unit_tru;
	private BigDecimal	r96_crm_sub_col_exp_cov;
	private BigDecimal	r96_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r96_crm_sub_rwa_ris_crm;
	private BigDecimal	r96_crm_sub_rwa_cov_crm;
	private BigDecimal	r96_crm_sub_rwa_org_cou;
	private BigDecimal	r96_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r96_crm_comp_col_expo_elig;
	private BigDecimal	r96_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r96_crm_comp_col_elig_fin_hai;
	private BigDecimal	r96_crm_comp_col_expo_val;
	private BigDecimal	r96_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r96_rwa_unsec_expo_cre_ris;
	private BigDecimal	r96_rwa_unsec_expo;
	private BigDecimal	r96_rwa_tot_ris_wei_ass;
	
	private String	r97_exposure_class;
	private BigDecimal	r97_expo_crm;
	private BigDecimal	r97_spe_pro_expo;
	private BigDecimal	r97_amt_elig_sht_net;
	private BigDecimal	r97_tot_expo_net_spe;
	private BigDecimal	r97_crm_sub_elig_sub_app;
	private BigDecimal	r97_crm_sub_non_col_guar;
	private BigDecimal	r97_crm_sub_non_col_cre_der;
	private BigDecimal	r97_crm_sub_col_elig_cash;
	private BigDecimal	r97_crm_sub_col_elig_trea_bills;
	private BigDecimal	r97_crm_sub_col_elig_deb_sec;
	private BigDecimal	r97_crm_sub_col_elig_equi;
	private BigDecimal	r97_crm_sub_col_elig_unit_tru;
	private BigDecimal	r97_crm_sub_col_exp_cov;
	private BigDecimal	r97_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r97_crm_sub_rwa_ris_crm;
	private BigDecimal	r97_crm_sub_rwa_cov_crm;
	private BigDecimal	r97_crm_sub_rwa_org_cou;
	private BigDecimal	r97_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r97_crm_comp_col_expo_elig;
	private BigDecimal	r97_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r97_crm_comp_col_elig_fin_hai;
	private BigDecimal	r97_crm_comp_col_expo_val;
	private BigDecimal	r97_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r97_rwa_unsec_expo_cre_ris;
	private BigDecimal	r97_rwa_unsec_expo;
	private BigDecimal	r97_rwa_tot_ris_wei_ass;

	private String	r98_exposure_class;
	private BigDecimal	r98_expo_crm;
	private BigDecimal	r98_spe_pro_expo;
	private BigDecimal	r98_amt_elig_sht_net;
	private BigDecimal	r98_tot_expo_net_spe;
	private BigDecimal	r98_crm_sub_elig_sub_app;
	private BigDecimal	r98_crm_sub_non_col_guar;
	private BigDecimal	r98_crm_sub_non_col_cre_der;
	private BigDecimal	r98_crm_sub_col_elig_cash;
	private BigDecimal	r98_crm_sub_col_elig_trea_bills;
	private BigDecimal	r98_crm_sub_col_elig_deb_sec;
	private BigDecimal	r98_crm_sub_col_elig_equi;
	private BigDecimal	r98_crm_sub_col_elig_unit_tru;
	private BigDecimal	r98_crm_sub_col_exp_cov;
	private BigDecimal	r98_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r98_crm_sub_rwa_ris_crm;
	private BigDecimal	r98_crm_sub_rwa_cov_crm;
	private BigDecimal	r98_crm_sub_rwa_org_cou;
	private BigDecimal	r98_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r98_crm_comp_col_expo_elig;
	private BigDecimal	r98_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r98_crm_comp_col_elig_fin_hai;
	private BigDecimal	r98_crm_comp_col_expo_val;
	private BigDecimal	r98_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r98_rwa_unsec_expo_cre_ris;
	private BigDecimal	r98_rwa_unsec_expo;
	private BigDecimal	r98_rwa_tot_ris_wei_ass;
	
	private String	r99_exposure_class;
	private BigDecimal	r99_expo_crm;
	private BigDecimal	r99_spe_pro_expo;
	private BigDecimal	r99_amt_elig_sht_net;
	private BigDecimal	r99_tot_expo_net_spe;
	private BigDecimal	r99_crm_sub_elig_sub_app;
	private BigDecimal	r99_crm_sub_non_col_guar;
	private BigDecimal	r99_crm_sub_non_col_cre_der;
	private BigDecimal	r99_crm_sub_col_elig_cash;
	private BigDecimal	r99_crm_sub_col_elig_trea_bills;
	private BigDecimal	r99_crm_sub_col_elig_deb_sec;
	private BigDecimal	r99_crm_sub_col_elig_equi;
	private BigDecimal	r99_crm_sub_col_elig_unit_tru;
	private BigDecimal	r99_crm_sub_col_exp_cov;
	private BigDecimal	r99_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r99_crm_sub_rwa_ris_crm;
	private BigDecimal	r99_crm_sub_rwa_cov_crm;
	private BigDecimal	r99_crm_sub_rwa_org_cou;
	private BigDecimal	r99_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r99_crm_comp_col_expo_elig;
	private BigDecimal	r99_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r99_crm_comp_col_elig_fin_hai;
	private BigDecimal	r99_crm_comp_col_expo_val;
	private BigDecimal	r99_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r99_rwa_unsec_expo_cre_ris;
	private BigDecimal	r99_rwa_unsec_expo;
	private BigDecimal	r99_rwa_tot_ris_wei_ass;

   
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
	public String getR60_exposure_class() {
		return r60_exposure_class;
	}
	public void setR60_exposure_class(String r60_exposure_class) {
		this.r60_exposure_class = r60_exposure_class;
	}
	
	public BigDecimal getR60_spe_pro_expo() {
		return r60_spe_pro_expo;
	}
	public void setR60_spe_pro_expo(BigDecimal r60_spe_pro_expo) {
		this.r60_spe_pro_expo = r60_spe_pro_expo;
	}
	public BigDecimal getR60_amt_elig_sht_net() {
		return r60_amt_elig_sht_net;
	}
	public void setR60_amt_elig_sht_net(BigDecimal r60_amt_elig_sht_net) {
		this.r60_amt_elig_sht_net = r60_amt_elig_sht_net;
	}
	public BigDecimal getR60_tot_expo_net_spe() {
		return r60_tot_expo_net_spe;
	}
	public void setR60_tot_expo_net_spe(BigDecimal r60_tot_expo_net_spe) {
		this.r60_tot_expo_net_spe = r60_tot_expo_net_spe;
	}
	public BigDecimal getR60_crm_sub_elig_sub_app() {
		return r60_crm_sub_elig_sub_app;
	}
	public void setR60_crm_sub_elig_sub_app(BigDecimal r60_crm_sub_elig_sub_app) {
		this.r60_crm_sub_elig_sub_app = r60_crm_sub_elig_sub_app;
	}
	public BigDecimal getR60_crm_sub_non_col_guar() {
		return r60_crm_sub_non_col_guar;
	}
	public void setR60_crm_sub_non_col_guar(BigDecimal r60_crm_sub_non_col_guar) {
		this.r60_crm_sub_non_col_guar = r60_crm_sub_non_col_guar;
	}
	public BigDecimal getR60_crm_sub_non_col_cre_der() {
		return r60_crm_sub_non_col_cre_der;
	}
	public void setR60_crm_sub_non_col_cre_der(BigDecimal r60_crm_sub_non_col_cre_der) {
		this.r60_crm_sub_non_col_cre_der = r60_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR60_crm_sub_col_elig_cash() {
		return r60_crm_sub_col_elig_cash;
	}
	public void setR60_crm_sub_col_elig_cash(BigDecimal r60_crm_sub_col_elig_cash) {
		this.r60_crm_sub_col_elig_cash = r60_crm_sub_col_elig_cash;
	}
	public BigDecimal getR60_crm_sub_col_elig_trea_bills() {
		return r60_crm_sub_col_elig_trea_bills;
	}
	public void setR60_crm_sub_col_elig_trea_bills(BigDecimal r60_crm_sub_col_elig_trea_bills) {
		this.r60_crm_sub_col_elig_trea_bills = r60_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR60_crm_sub_col_elig_deb_sec() {
		return r60_crm_sub_col_elig_deb_sec;
	}
	public void setR60_crm_sub_col_elig_deb_sec(BigDecimal r60_crm_sub_col_elig_deb_sec) {
		this.r60_crm_sub_col_elig_deb_sec = r60_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR60_crm_sub_col_elig_equi() {
		return r60_crm_sub_col_elig_equi;
	}
	public void setR60_crm_sub_col_elig_equi(BigDecimal r60_crm_sub_col_elig_equi) {
		this.r60_crm_sub_col_elig_equi = r60_crm_sub_col_elig_equi;
	}
	public BigDecimal getR60_crm_sub_col_elig_unit_tru() {
		return r60_crm_sub_col_elig_unit_tru;
	}
	public void setR60_crm_sub_col_elig_unit_tru(BigDecimal r60_crm_sub_col_elig_unit_tru) {
		this.r60_crm_sub_col_elig_unit_tru = r60_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR60_crm_sub_col_exp_cov() {
		return r60_crm_sub_col_exp_cov;
	}
	public void setR60_crm_sub_col_exp_cov(BigDecimal r60_crm_sub_col_exp_cov) {
		this.r60_crm_sub_col_exp_cov = r60_crm_sub_col_exp_cov;
	}
	public BigDecimal getR60_crm_sub_col_elig_exp_not_cov() {
		return r60_crm_sub_col_elig_exp_not_cov;
	}
	public void setR60_crm_sub_col_elig_exp_not_cov(BigDecimal r60_crm_sub_col_elig_exp_not_cov) {
		this.r60_crm_sub_col_elig_exp_not_cov = r60_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR60_crm_sub_rwa_ris_crm() {
		return r60_crm_sub_rwa_ris_crm;
	}
	public void setR60_crm_sub_rwa_ris_crm(BigDecimal r60_crm_sub_rwa_ris_crm) {
		this.r60_crm_sub_rwa_ris_crm = r60_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR60_crm_sub_rwa_cov_crm() {
		return r60_crm_sub_rwa_cov_crm;
	}
	public void setR60_crm_sub_rwa_cov_crm(BigDecimal r60_crm_sub_rwa_cov_crm) {
		this.r60_crm_sub_rwa_cov_crm = r60_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR60_crm_sub_rwa_org_cou() {
		return r60_crm_sub_rwa_org_cou;
	}
	public void setR60_crm_sub_rwa_org_cou(BigDecimal r60_crm_sub_rwa_org_cou) {
		this.r60_crm_sub_rwa_org_cou = r60_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR60_crm_sub_rwa_not_cov_crm() {
		return r60_crm_sub_rwa_not_cov_crm;
	}
	public void setR60_crm_sub_rwa_not_cov_crm(BigDecimal r60_crm_sub_rwa_not_cov_crm) {
		this.r60_crm_sub_rwa_not_cov_crm = r60_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR60_crm_comp_col_expo_elig() {
		return r60_crm_comp_col_expo_elig;
	}
	public void setR60_crm_comp_col_expo_elig(BigDecimal r60_crm_comp_col_expo_elig) {
		this.r60_crm_comp_col_expo_elig = r60_crm_comp_col_expo_elig;
	}
	public BigDecimal getR60_crm_comp_col_elig_expo_vol_adj() {
		return r60_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR60_crm_comp_col_elig_expo_vol_adj(BigDecimal r60_crm_comp_col_elig_expo_vol_adj) {
		this.r60_crm_comp_col_elig_expo_vol_adj = r60_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR60_crm_comp_col_elig_fin_hai() {
		return r60_crm_comp_col_elig_fin_hai;
	}
	public void setR60_crm_comp_col_elig_fin_hai(BigDecimal r60_crm_comp_col_elig_fin_hai) {
		this.r60_crm_comp_col_elig_fin_hai = r60_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR60_crm_comp_col_expo_val() {
		return r60_crm_comp_col_expo_val;
	}
	public void setR60_crm_comp_col_expo_val(BigDecimal r60_crm_comp_col_expo_val) {
		this.r60_crm_comp_col_expo_val = r60_crm_comp_col_expo_val;
	}
	public BigDecimal getR60_rwa_elig_expo_not_cov_crm() {
		return r60_rwa_elig_expo_not_cov_crm;
	}
	public void setR60_rwa_elig_expo_not_cov_crm(BigDecimal r60_rwa_elig_expo_not_cov_crm) {
		this.r60_rwa_elig_expo_not_cov_crm = r60_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR60_rwa_unsec_expo_cre_ris() {
		return r60_rwa_unsec_expo_cre_ris;
	}
	public void setR60_rwa_unsec_expo_cre_ris(BigDecimal r60_rwa_unsec_expo_cre_ris) {
		this.r60_rwa_unsec_expo_cre_ris = r60_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR60_rwa_unsec_expo() {
		return r60_rwa_unsec_expo;
	}
	public void setR60_rwa_unsec_expo(BigDecimal r60_rwa_unsec_expo) {
		this.r60_rwa_unsec_expo = r60_rwa_unsec_expo;
	}
	public BigDecimal getR60_rwa_tot_ris_wei_ass() {
		return r60_rwa_tot_ris_wei_ass;
	}
	public void setR60_rwa_tot_ris_wei_ass(BigDecimal r60_rwa_tot_ris_wei_ass) {
		this.r60_rwa_tot_ris_wei_ass = r60_rwa_tot_ris_wei_ass;
	}
	public String getR61_exposure_class() {
		return r61_exposure_class;
	}
	public void setR61_exposure_class(String r61_exposure_class) {
		this.r61_exposure_class = r61_exposure_class;
	}
	public BigDecimal getR61_expo_crm() {
		return r61_expo_crm;
	}
	public void setR61_expo_crm(BigDecimal r61_expo_crm) {
		this.r61_expo_crm = r61_expo_crm;
	}
	public BigDecimal getR61_spe_pro_expo() {
		return r61_spe_pro_expo;
	}
	public void setR61_spe_pro_expo(BigDecimal r61_spe_pro_expo) {
		this.r61_spe_pro_expo = r61_spe_pro_expo;
	}
	public BigDecimal getR61_amt_elig_sht_net() {
		return r61_amt_elig_sht_net;
	}
	public void setR61_amt_elig_sht_net(BigDecimal r61_amt_elig_sht_net) {
		this.r61_amt_elig_sht_net = r61_amt_elig_sht_net;
	}
	public BigDecimal getR61_tot_expo_net_spe() {
		return r61_tot_expo_net_spe;
	}
	public void setR61_tot_expo_net_spe(BigDecimal r61_tot_expo_net_spe) {
		this.r61_tot_expo_net_spe = r61_tot_expo_net_spe;
	}
	public BigDecimal getR61_crm_sub_elig_sub_app() {
		return r61_crm_sub_elig_sub_app;
	}
	public void setR61_crm_sub_elig_sub_app(BigDecimal r61_crm_sub_elig_sub_app) {
		this.r61_crm_sub_elig_sub_app = r61_crm_sub_elig_sub_app;
	}
	public BigDecimal getR61_crm_sub_non_col_guar() {
		return r61_crm_sub_non_col_guar;
	}
	public void setR61_crm_sub_non_col_guar(BigDecimal r61_crm_sub_non_col_guar) {
		this.r61_crm_sub_non_col_guar = r61_crm_sub_non_col_guar;
	}
	public BigDecimal getR61_crm_sub_non_col_cre_der() {
		return r61_crm_sub_non_col_cre_der;
	}
	public void setR61_crm_sub_non_col_cre_der(BigDecimal r61_crm_sub_non_col_cre_der) {
		this.r61_crm_sub_non_col_cre_der = r61_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR61_crm_sub_col_elig_cash() {
		return r61_crm_sub_col_elig_cash;
	}
	public void setR61_crm_sub_col_elig_cash(BigDecimal r61_crm_sub_col_elig_cash) {
		this.r61_crm_sub_col_elig_cash = r61_crm_sub_col_elig_cash;
	}
	public BigDecimal getR61_crm_sub_col_elig_trea_bills() {
		return r61_crm_sub_col_elig_trea_bills;
	}
	public void setR61_crm_sub_col_elig_trea_bills(BigDecimal r61_crm_sub_col_elig_trea_bills) {
		this.r61_crm_sub_col_elig_trea_bills = r61_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR61_crm_sub_col_elig_deb_sec() {
		return r61_crm_sub_col_elig_deb_sec;
	}
	public void setR61_crm_sub_col_elig_deb_sec(BigDecimal r61_crm_sub_col_elig_deb_sec) {
		this.r61_crm_sub_col_elig_deb_sec = r61_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR61_crm_sub_col_elig_equi() {
		return r61_crm_sub_col_elig_equi;
	}
	public void setR61_crm_sub_col_elig_equi(BigDecimal r61_crm_sub_col_elig_equi) {
		this.r61_crm_sub_col_elig_equi = r61_crm_sub_col_elig_equi;
	}
	public BigDecimal getR61_crm_sub_col_elig_unit_tru() {
		return r61_crm_sub_col_elig_unit_tru;
	}
	public void setR61_crm_sub_col_elig_unit_tru(BigDecimal r61_crm_sub_col_elig_unit_tru) {
		this.r61_crm_sub_col_elig_unit_tru = r61_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR61_crm_sub_col_exp_cov() {
		return r61_crm_sub_col_exp_cov;
	}
	public void setR61_crm_sub_col_exp_cov(BigDecimal r61_crm_sub_col_exp_cov) {
		this.r61_crm_sub_col_exp_cov = r61_crm_sub_col_exp_cov;
	}
	public BigDecimal getR61_crm_sub_col_elig_exp_not_cov() {
		return r61_crm_sub_col_elig_exp_not_cov;
	}
	public void setR61_crm_sub_col_elig_exp_not_cov(BigDecimal r61_crm_sub_col_elig_exp_not_cov) {
		this.r61_crm_sub_col_elig_exp_not_cov = r61_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR61_crm_sub_rwa_ris_crm() {
		return r61_crm_sub_rwa_ris_crm;
	}
	public void setR61_crm_sub_rwa_ris_crm(BigDecimal r61_crm_sub_rwa_ris_crm) {
		this.r61_crm_sub_rwa_ris_crm = r61_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR61_crm_sub_rwa_cov_crm() {
		return r61_crm_sub_rwa_cov_crm;
	}
	public void setR61_crm_sub_rwa_cov_crm(BigDecimal r61_crm_sub_rwa_cov_crm) {
		this.r61_crm_sub_rwa_cov_crm = r61_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR61_crm_sub_rwa_org_cou() {
		return r61_crm_sub_rwa_org_cou;
	}
	public void setR61_crm_sub_rwa_org_cou(BigDecimal r61_crm_sub_rwa_org_cou) {
		this.r61_crm_sub_rwa_org_cou = r61_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR61_crm_sub_rwa_not_cov_crm() {
		return r61_crm_sub_rwa_not_cov_crm;
	}
	public void setR61_crm_sub_rwa_not_cov_crm(BigDecimal r61_crm_sub_rwa_not_cov_crm) {
		this.r61_crm_sub_rwa_not_cov_crm = r61_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR61_crm_comp_col_expo_elig() {
		return r61_crm_comp_col_expo_elig;
	}
	public void setR61_crm_comp_col_expo_elig(BigDecimal r61_crm_comp_col_expo_elig) {
		this.r61_crm_comp_col_expo_elig = r61_crm_comp_col_expo_elig;
	}
	public BigDecimal getR61_crm_comp_col_elig_expo_vol_adj() {
		return r61_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR61_crm_comp_col_elig_expo_vol_adj(BigDecimal r61_crm_comp_col_elig_expo_vol_adj) {
		this.r61_crm_comp_col_elig_expo_vol_adj = r61_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR61_crm_comp_col_elig_fin_hai() {
		return r61_crm_comp_col_elig_fin_hai;
	}
	public void setR61_crm_comp_col_elig_fin_hai(BigDecimal r61_crm_comp_col_elig_fin_hai) {
		this.r61_crm_comp_col_elig_fin_hai = r61_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR61_crm_comp_col_expo_val() {
		return r61_crm_comp_col_expo_val;
	}
	public void setR61_crm_comp_col_expo_val(BigDecimal r61_crm_comp_col_expo_val) {
		this.r61_crm_comp_col_expo_val = r61_crm_comp_col_expo_val;
	}
	public BigDecimal getR61_rwa_elig_expo_not_cov_crm() {
		return r61_rwa_elig_expo_not_cov_crm;
	}
	public void setR61_rwa_elig_expo_not_cov_crm(BigDecimal r61_rwa_elig_expo_not_cov_crm) {
		this.r61_rwa_elig_expo_not_cov_crm = r61_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR61_rwa_unsec_expo_cre_ris() {
		return r61_rwa_unsec_expo_cre_ris;
	}
	public void setR61_rwa_unsec_expo_cre_ris(BigDecimal r61_rwa_unsec_expo_cre_ris) {
		this.r61_rwa_unsec_expo_cre_ris = r61_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR61_rwa_unsec_expo() {
		return r61_rwa_unsec_expo;
	}
	public void setR61_rwa_unsec_expo(BigDecimal r61_rwa_unsec_expo) {
		this.r61_rwa_unsec_expo = r61_rwa_unsec_expo;
	}
	public BigDecimal getR61_rwa_tot_ris_wei_ass() {
		return r61_rwa_tot_ris_wei_ass;
	}
	public void setR61_rwa_tot_ris_wei_ass(BigDecimal r61_rwa_tot_ris_wei_ass) {
		this.r61_rwa_tot_ris_wei_ass = r61_rwa_tot_ris_wei_ass;
	}
	public String getR62_exposure_class() {
		return r62_exposure_class;
	}
	public void setR62_exposure_class(String r62_exposure_class) {
		this.r62_exposure_class = r62_exposure_class;
	}
	public BigDecimal getR62_expo_crm() {
		return r62_expo_crm;
	}
	public void setR62_expo_crm(BigDecimal r62_expo_crm) {
		this.r62_expo_crm = r62_expo_crm;
	}
	public BigDecimal getR62_spe_pro_expo() {
		return r62_spe_pro_expo;
	}
	public void setR62_spe_pro_expo(BigDecimal r62_spe_pro_expo) {
		this.r62_spe_pro_expo = r62_spe_pro_expo;
	}
	public BigDecimal getR62_amt_elig_sht_net() {
		return r62_amt_elig_sht_net;
	}
	public void setR62_amt_elig_sht_net(BigDecimal r62_amt_elig_sht_net) {
		this.r62_amt_elig_sht_net = r62_amt_elig_sht_net;
	}
	public BigDecimal getR62_tot_expo_net_spe() {
		return r62_tot_expo_net_spe;
	}
	public void setR62_tot_expo_net_spe(BigDecimal r62_tot_expo_net_spe) {
		this.r62_tot_expo_net_spe = r62_tot_expo_net_spe;
	}
	public BigDecimal getR62_crm_sub_elig_sub_app() {
		return r62_crm_sub_elig_sub_app;
	}
	public void setR62_crm_sub_elig_sub_app(BigDecimal r62_crm_sub_elig_sub_app) {
		this.r62_crm_sub_elig_sub_app = r62_crm_sub_elig_sub_app;
	}
	public BigDecimal getR62_crm_sub_non_col_guar() {
		return r62_crm_sub_non_col_guar;
	}
	public void setR62_crm_sub_non_col_guar(BigDecimal r62_crm_sub_non_col_guar) {
		this.r62_crm_sub_non_col_guar = r62_crm_sub_non_col_guar;
	}
	public BigDecimal getR62_crm_sub_non_col_cre_der() {
		return r62_crm_sub_non_col_cre_der;
	}
	public void setR62_crm_sub_non_col_cre_der(BigDecimal r62_crm_sub_non_col_cre_der) {
		this.r62_crm_sub_non_col_cre_der = r62_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR62_crm_sub_col_elig_cash() {
		return r62_crm_sub_col_elig_cash;
	}
	public void setR62_crm_sub_col_elig_cash(BigDecimal r62_crm_sub_col_elig_cash) {
		this.r62_crm_sub_col_elig_cash = r62_crm_sub_col_elig_cash;
	}
	public BigDecimal getR62_crm_sub_col_elig_trea_bills() {
		return r62_crm_sub_col_elig_trea_bills;
	}
	public void setR62_crm_sub_col_elig_trea_bills(BigDecimal r62_crm_sub_col_elig_trea_bills) {
		this.r62_crm_sub_col_elig_trea_bills = r62_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR62_crm_sub_col_elig_deb_sec() {
		return r62_crm_sub_col_elig_deb_sec;
	}
	public void setR62_crm_sub_col_elig_deb_sec(BigDecimal r62_crm_sub_col_elig_deb_sec) {
		this.r62_crm_sub_col_elig_deb_sec = r62_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR62_crm_sub_col_elig_equi() {
		return r62_crm_sub_col_elig_equi;
	}
	public void setR62_crm_sub_col_elig_equi(BigDecimal r62_crm_sub_col_elig_equi) {
		this.r62_crm_sub_col_elig_equi = r62_crm_sub_col_elig_equi;
	}
	public BigDecimal getR62_crm_sub_col_elig_unit_tru() {
		return r62_crm_sub_col_elig_unit_tru;
	}
	public void setR62_crm_sub_col_elig_unit_tru(BigDecimal r62_crm_sub_col_elig_unit_tru) {
		this.r62_crm_sub_col_elig_unit_tru = r62_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR62_crm_sub_col_exp_cov() {
		return r62_crm_sub_col_exp_cov;
	}
	public void setR62_crm_sub_col_exp_cov(BigDecimal r62_crm_sub_col_exp_cov) {
		this.r62_crm_sub_col_exp_cov = r62_crm_sub_col_exp_cov;
	}
	public BigDecimal getR62_crm_sub_col_elig_exp_not_cov() {
		return r62_crm_sub_col_elig_exp_not_cov;
	}
	public void setR62_crm_sub_col_elig_exp_not_cov(BigDecimal r62_crm_sub_col_elig_exp_not_cov) {
		this.r62_crm_sub_col_elig_exp_not_cov = r62_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR62_crm_sub_rwa_ris_crm() {
		return r62_crm_sub_rwa_ris_crm;
	}
	public void setR62_crm_sub_rwa_ris_crm(BigDecimal r62_crm_sub_rwa_ris_crm) {
		this.r62_crm_sub_rwa_ris_crm = r62_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR62_crm_sub_rwa_cov_crm() {
		return r62_crm_sub_rwa_cov_crm;
	}
	public void setR62_crm_sub_rwa_cov_crm(BigDecimal r62_crm_sub_rwa_cov_crm) {
		this.r62_crm_sub_rwa_cov_crm = r62_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR62_crm_sub_rwa_org_cou() {
		return r62_crm_sub_rwa_org_cou;
	}
	public void setR62_crm_sub_rwa_org_cou(BigDecimal r62_crm_sub_rwa_org_cou) {
		this.r62_crm_sub_rwa_org_cou = r62_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR62_crm_sub_rwa_not_cov_crm() {
		return r62_crm_sub_rwa_not_cov_crm;
	}
	public void setR62_crm_sub_rwa_not_cov_crm(BigDecimal r62_crm_sub_rwa_not_cov_crm) {
		this.r62_crm_sub_rwa_not_cov_crm = r62_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR62_crm_comp_col_expo_elig() {
		return r62_crm_comp_col_expo_elig;
	}
	public void setR62_crm_comp_col_expo_elig(BigDecimal r62_crm_comp_col_expo_elig) {
		this.r62_crm_comp_col_expo_elig = r62_crm_comp_col_expo_elig;
	}
	public BigDecimal getR62_crm_comp_col_elig_expo_vol_adj() {
		return r62_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR62_crm_comp_col_elig_expo_vol_adj(BigDecimal r62_crm_comp_col_elig_expo_vol_adj) {
		this.r62_crm_comp_col_elig_expo_vol_adj = r62_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR62_crm_comp_col_elig_fin_hai() {
		return r62_crm_comp_col_elig_fin_hai;
	}
	public void setR62_crm_comp_col_elig_fin_hai(BigDecimal r62_crm_comp_col_elig_fin_hai) {
		this.r62_crm_comp_col_elig_fin_hai = r62_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR62_crm_comp_col_expo_val() {
		return r62_crm_comp_col_expo_val;
	}
	public void setR62_crm_comp_col_expo_val(BigDecimal r62_crm_comp_col_expo_val) {
		this.r62_crm_comp_col_expo_val = r62_crm_comp_col_expo_val;
	}
	public BigDecimal getR62_rwa_elig_expo_not_cov_crm() {
		return r62_rwa_elig_expo_not_cov_crm;
	}
	public void setR62_rwa_elig_expo_not_cov_crm(BigDecimal r62_rwa_elig_expo_not_cov_crm) {
		this.r62_rwa_elig_expo_not_cov_crm = r62_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR62_rwa_unsec_expo_cre_ris() {
		return r62_rwa_unsec_expo_cre_ris;
	}
	public void setR62_rwa_unsec_expo_cre_ris(BigDecimal r62_rwa_unsec_expo_cre_ris) {
		this.r62_rwa_unsec_expo_cre_ris = r62_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR62_rwa_unsec_expo() {
		return r62_rwa_unsec_expo;
	}
	public void setR62_rwa_unsec_expo(BigDecimal r62_rwa_unsec_expo) {
		this.r62_rwa_unsec_expo = r62_rwa_unsec_expo;
	}
	public BigDecimal getR62_rwa_tot_ris_wei_ass() {
		return r62_rwa_tot_ris_wei_ass;
	}
	public void setR62_rwa_tot_ris_wei_ass(BigDecimal r62_rwa_tot_ris_wei_ass) {
		this.r62_rwa_tot_ris_wei_ass = r62_rwa_tot_ris_wei_ass;
	}
	public String getR63_exposure_class() {
		return r63_exposure_class;
	}
	public void setR63_exposure_class(String r63_exposure_class) {
		this.r63_exposure_class = r63_exposure_class;
	}
	
	public BigDecimal getR63_spe_pro_expo() {
		return r63_spe_pro_expo;
	}
	public void setR63_spe_pro_expo(BigDecimal r63_spe_pro_expo) {
		this.r63_spe_pro_expo = r63_spe_pro_expo;
	}
	public BigDecimal getR63_amt_elig_sht_net() {
		return r63_amt_elig_sht_net;
	}
	public void setR63_amt_elig_sht_net(BigDecimal r63_amt_elig_sht_net) {
		this.r63_amt_elig_sht_net = r63_amt_elig_sht_net;
	}
	public BigDecimal getR63_tot_expo_net_spe() {
		return r63_tot_expo_net_spe;
	}
	public void setR63_tot_expo_net_spe(BigDecimal r63_tot_expo_net_spe) {
		this.r63_tot_expo_net_spe = r63_tot_expo_net_spe;
	}
	public BigDecimal getR63_crm_sub_elig_sub_app() {
		return r63_crm_sub_elig_sub_app;
	}
	public void setR63_crm_sub_elig_sub_app(BigDecimal r63_crm_sub_elig_sub_app) {
		this.r63_crm_sub_elig_sub_app = r63_crm_sub_elig_sub_app;
	}
	public BigDecimal getR63_crm_sub_non_col_guar() {
		return r63_crm_sub_non_col_guar;
	}
	public void setR63_crm_sub_non_col_guar(BigDecimal r63_crm_sub_non_col_guar) {
		this.r63_crm_sub_non_col_guar = r63_crm_sub_non_col_guar;
	}
	public BigDecimal getR63_crm_sub_non_col_cre_der() {
		return r63_crm_sub_non_col_cre_der;
	}
	public void setR63_crm_sub_non_col_cre_der(BigDecimal r63_crm_sub_non_col_cre_der) {
		this.r63_crm_sub_non_col_cre_der = r63_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR63_crm_sub_col_elig_cash() {
		return r63_crm_sub_col_elig_cash;
	}
	public void setR63_crm_sub_col_elig_cash(BigDecimal r63_crm_sub_col_elig_cash) {
		this.r63_crm_sub_col_elig_cash = r63_crm_sub_col_elig_cash;
	}
	public BigDecimal getR63_crm_sub_col_elig_trea_bills() {
		return r63_crm_sub_col_elig_trea_bills;
	}
	public void setR63_crm_sub_col_elig_trea_bills(BigDecimal r63_crm_sub_col_elig_trea_bills) {
		this.r63_crm_sub_col_elig_trea_bills = r63_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR63_crm_sub_col_elig_deb_sec() {
		return r63_crm_sub_col_elig_deb_sec;
	}
	public void setR63_crm_sub_col_elig_deb_sec(BigDecimal r63_crm_sub_col_elig_deb_sec) {
		this.r63_crm_sub_col_elig_deb_sec = r63_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR63_crm_sub_col_elig_equi() {
		return r63_crm_sub_col_elig_equi;
	}
	public void setR63_crm_sub_col_elig_equi(BigDecimal r63_crm_sub_col_elig_equi) {
		this.r63_crm_sub_col_elig_equi = r63_crm_sub_col_elig_equi;
	}
	public BigDecimal getR63_crm_sub_col_elig_unit_tru() {
		return r63_crm_sub_col_elig_unit_tru;
	}
	public void setR63_crm_sub_col_elig_unit_tru(BigDecimal r63_crm_sub_col_elig_unit_tru) {
		this.r63_crm_sub_col_elig_unit_tru = r63_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR63_crm_sub_col_exp_cov() {
		return r63_crm_sub_col_exp_cov;
	}
	public void setR63_crm_sub_col_exp_cov(BigDecimal r63_crm_sub_col_exp_cov) {
		this.r63_crm_sub_col_exp_cov = r63_crm_sub_col_exp_cov;
	}
	public BigDecimal getR63_crm_sub_col_elig_exp_not_cov() {
		return r63_crm_sub_col_elig_exp_not_cov;
	}
	public void setR63_crm_sub_col_elig_exp_not_cov(BigDecimal r63_crm_sub_col_elig_exp_not_cov) {
		this.r63_crm_sub_col_elig_exp_not_cov = r63_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR63_crm_sub_rwa_ris_crm() {
		return r63_crm_sub_rwa_ris_crm;
	}
	public void setR63_crm_sub_rwa_ris_crm(BigDecimal r63_crm_sub_rwa_ris_crm) {
		this.r63_crm_sub_rwa_ris_crm = r63_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR63_crm_sub_rwa_cov_crm() {
		return r63_crm_sub_rwa_cov_crm;
	}
	public void setR63_crm_sub_rwa_cov_crm(BigDecimal r63_crm_sub_rwa_cov_crm) {
		this.r63_crm_sub_rwa_cov_crm = r63_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR63_crm_sub_rwa_org_cou() {
		return r63_crm_sub_rwa_org_cou;
	}
	public void setR63_crm_sub_rwa_org_cou(BigDecimal r63_crm_sub_rwa_org_cou) {
		this.r63_crm_sub_rwa_org_cou = r63_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR63_crm_sub_rwa_not_cov_crm() {
		return r63_crm_sub_rwa_not_cov_crm;
	}
	public void setR63_crm_sub_rwa_not_cov_crm(BigDecimal r63_crm_sub_rwa_not_cov_crm) {
		this.r63_crm_sub_rwa_not_cov_crm = r63_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR63_crm_comp_col_expo_elig() {
		return r63_crm_comp_col_expo_elig;
	}
	public void setR63_crm_comp_col_expo_elig(BigDecimal r63_crm_comp_col_expo_elig) {
		this.r63_crm_comp_col_expo_elig = r63_crm_comp_col_expo_elig;
	}
	public BigDecimal getR63_crm_comp_col_elig_expo_vol_adj() {
		return r63_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR63_crm_comp_col_elig_expo_vol_adj(BigDecimal r63_crm_comp_col_elig_expo_vol_adj) {
		this.r63_crm_comp_col_elig_expo_vol_adj = r63_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR63_crm_comp_col_elig_fin_hai() {
		return r63_crm_comp_col_elig_fin_hai;
	}
	public void setR63_crm_comp_col_elig_fin_hai(BigDecimal r63_crm_comp_col_elig_fin_hai) {
		this.r63_crm_comp_col_elig_fin_hai = r63_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR63_crm_comp_col_expo_val() {
		return r63_crm_comp_col_expo_val;
	}
	public void setR63_crm_comp_col_expo_val(BigDecimal r63_crm_comp_col_expo_val) {
		this.r63_crm_comp_col_expo_val = r63_crm_comp_col_expo_val;
	}
	public BigDecimal getR63_rwa_elig_expo_not_cov_crm() {
		return r63_rwa_elig_expo_not_cov_crm;
	}
	public void setR63_rwa_elig_expo_not_cov_crm(BigDecimal r63_rwa_elig_expo_not_cov_crm) {
		this.r63_rwa_elig_expo_not_cov_crm = r63_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR63_rwa_unsec_expo_cre_ris() {
		return r63_rwa_unsec_expo_cre_ris;
	}
	public void setR63_rwa_unsec_expo_cre_ris(BigDecimal r63_rwa_unsec_expo_cre_ris) {
		this.r63_rwa_unsec_expo_cre_ris = r63_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR63_rwa_unsec_expo() {
		return r63_rwa_unsec_expo;
	}
	public void setR63_rwa_unsec_expo(BigDecimal r63_rwa_unsec_expo) {
		this.r63_rwa_unsec_expo = r63_rwa_unsec_expo;
	}
	public BigDecimal getR63_rwa_tot_ris_wei_ass() {
		return r63_rwa_tot_ris_wei_ass;
	}
	public void setR63_rwa_tot_ris_wei_ass(BigDecimal r63_rwa_tot_ris_wei_ass) {
		this.r63_rwa_tot_ris_wei_ass = r63_rwa_tot_ris_wei_ass;
	}
	public String getR64_exposure_class() {
		return r64_exposure_class;
	}
	public void setR64_exposure_class(String r64_exposure_class) {
		this.r64_exposure_class = r64_exposure_class;
	}
	public BigDecimal getR64_expo_crm() {
		return r64_expo_crm;
	}
	public void setR64_expo_crm(BigDecimal r64_expo_crm) {
		this.r64_expo_crm = r64_expo_crm;
	}
	public BigDecimal getR64_spe_pro_expo() {
		return r64_spe_pro_expo;
	}
	public void setR64_spe_pro_expo(BigDecimal r64_spe_pro_expo) {
		this.r64_spe_pro_expo = r64_spe_pro_expo;
	}
	public BigDecimal getR64_amt_elig_sht_net() {
		return r64_amt_elig_sht_net;
	}
	public void setR64_amt_elig_sht_net(BigDecimal r64_amt_elig_sht_net) {
		this.r64_amt_elig_sht_net = r64_amt_elig_sht_net;
	}
	public BigDecimal getR64_tot_expo_net_spe() {
		return r64_tot_expo_net_spe;
	}
	public void setR64_tot_expo_net_spe(BigDecimal r64_tot_expo_net_spe) {
		this.r64_tot_expo_net_spe = r64_tot_expo_net_spe;
	}
	public BigDecimal getR64_crm_sub_elig_sub_app() {
		return r64_crm_sub_elig_sub_app;
	}
	public void setR64_crm_sub_elig_sub_app(BigDecimal r64_crm_sub_elig_sub_app) {
		this.r64_crm_sub_elig_sub_app = r64_crm_sub_elig_sub_app;
	}
	public BigDecimal getR64_crm_sub_non_col_guar() {
		return r64_crm_sub_non_col_guar;
	}
	public void setR64_crm_sub_non_col_guar(BigDecimal r64_crm_sub_non_col_guar) {
		this.r64_crm_sub_non_col_guar = r64_crm_sub_non_col_guar;
	}
	public BigDecimal getR64_crm_sub_non_col_cre_der() {
		return r64_crm_sub_non_col_cre_der;
	}
	public void setR64_crm_sub_non_col_cre_der(BigDecimal r64_crm_sub_non_col_cre_der) {
		this.r64_crm_sub_non_col_cre_der = r64_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR64_crm_sub_col_elig_cash() {
		return r64_crm_sub_col_elig_cash;
	}
	public void setR64_crm_sub_col_elig_cash(BigDecimal r64_crm_sub_col_elig_cash) {
		this.r64_crm_sub_col_elig_cash = r64_crm_sub_col_elig_cash;
	}
	public BigDecimal getR64_crm_sub_col_elig_trea_bills() {
		return r64_crm_sub_col_elig_trea_bills;
	}
	public void setR64_crm_sub_col_elig_trea_bills(BigDecimal r64_crm_sub_col_elig_trea_bills) {
		this.r64_crm_sub_col_elig_trea_bills = r64_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR64_crm_sub_col_elig_deb_sec() {
		return r64_crm_sub_col_elig_deb_sec;
	}
	public void setR64_crm_sub_col_elig_deb_sec(BigDecimal r64_crm_sub_col_elig_deb_sec) {
		this.r64_crm_sub_col_elig_deb_sec = r64_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR64_crm_sub_col_elig_equi() {
		return r64_crm_sub_col_elig_equi;
	}
	public void setR64_crm_sub_col_elig_equi(BigDecimal r64_crm_sub_col_elig_equi) {
		this.r64_crm_sub_col_elig_equi = r64_crm_sub_col_elig_equi;
	}
	public BigDecimal getR64_crm_sub_col_elig_unit_tru() {
		return r64_crm_sub_col_elig_unit_tru;
	}
	public void setR64_crm_sub_col_elig_unit_tru(BigDecimal r64_crm_sub_col_elig_unit_tru) {
		this.r64_crm_sub_col_elig_unit_tru = r64_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR64_crm_sub_col_exp_cov() {
		return r64_crm_sub_col_exp_cov;
	}
	public void setR64_crm_sub_col_exp_cov(BigDecimal r64_crm_sub_col_exp_cov) {
		this.r64_crm_sub_col_exp_cov = r64_crm_sub_col_exp_cov;
	}
	public BigDecimal getR64_crm_sub_col_elig_exp_not_cov() {
		return r64_crm_sub_col_elig_exp_not_cov;
	}
	public void setR64_crm_sub_col_elig_exp_not_cov(BigDecimal r64_crm_sub_col_elig_exp_not_cov) {
		this.r64_crm_sub_col_elig_exp_not_cov = r64_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR64_crm_sub_rwa_ris_crm() {
		return r64_crm_sub_rwa_ris_crm;
	}
	public void setR64_crm_sub_rwa_ris_crm(BigDecimal r64_crm_sub_rwa_ris_crm) {
		this.r64_crm_sub_rwa_ris_crm = r64_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR64_crm_sub_rwa_cov_crm() {
		return r64_crm_sub_rwa_cov_crm;
	}
	public void setR64_crm_sub_rwa_cov_crm(BigDecimal r64_crm_sub_rwa_cov_crm) {
		this.r64_crm_sub_rwa_cov_crm = r64_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR64_crm_sub_rwa_org_cou() {
		return r64_crm_sub_rwa_org_cou;
	}
	public void setR64_crm_sub_rwa_org_cou(BigDecimal r64_crm_sub_rwa_org_cou) {
		this.r64_crm_sub_rwa_org_cou = r64_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR64_crm_sub_rwa_not_cov_crm() {
		return r64_crm_sub_rwa_not_cov_crm;
	}
	public void setR64_crm_sub_rwa_not_cov_crm(BigDecimal r64_crm_sub_rwa_not_cov_crm) {
		this.r64_crm_sub_rwa_not_cov_crm = r64_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR64_crm_comp_col_expo_elig() {
		return r64_crm_comp_col_expo_elig;
	}
	public void setR64_crm_comp_col_expo_elig(BigDecimal r64_crm_comp_col_expo_elig) {
		this.r64_crm_comp_col_expo_elig = r64_crm_comp_col_expo_elig;
	}
	public BigDecimal getR64_crm_comp_col_elig_expo_vol_adj() {
		return r64_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR64_crm_comp_col_elig_expo_vol_adj(BigDecimal r64_crm_comp_col_elig_expo_vol_adj) {
		this.r64_crm_comp_col_elig_expo_vol_adj = r64_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR64_crm_comp_col_elig_fin_hai() {
		return r64_crm_comp_col_elig_fin_hai;
	}
	public void setR64_crm_comp_col_elig_fin_hai(BigDecimal r64_crm_comp_col_elig_fin_hai) {
		this.r64_crm_comp_col_elig_fin_hai = r64_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR64_crm_comp_col_expo_val() {
		return r64_crm_comp_col_expo_val;
	}
	public void setR64_crm_comp_col_expo_val(BigDecimal r64_crm_comp_col_expo_val) {
		this.r64_crm_comp_col_expo_val = r64_crm_comp_col_expo_val;
	}
	public BigDecimal getR64_rwa_elig_expo_not_cov_crm() {
		return r64_rwa_elig_expo_not_cov_crm;
	}
	public void setR64_rwa_elig_expo_not_cov_crm(BigDecimal r64_rwa_elig_expo_not_cov_crm) {
		this.r64_rwa_elig_expo_not_cov_crm = r64_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR64_rwa_unsec_expo_cre_ris() {
		return r64_rwa_unsec_expo_cre_ris;
	}
	public void setR64_rwa_unsec_expo_cre_ris(BigDecimal r64_rwa_unsec_expo_cre_ris) {
		this.r64_rwa_unsec_expo_cre_ris = r64_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR64_rwa_unsec_expo() {
		return r64_rwa_unsec_expo;
	}
	public void setR64_rwa_unsec_expo(BigDecimal r64_rwa_unsec_expo) {
		this.r64_rwa_unsec_expo = r64_rwa_unsec_expo;
	}
	public BigDecimal getR64_rwa_tot_ris_wei_ass() {
		return r64_rwa_tot_ris_wei_ass;
	}
	public void setR64_rwa_tot_ris_wei_ass(BigDecimal r64_rwa_tot_ris_wei_ass) {
		this.r64_rwa_tot_ris_wei_ass = r64_rwa_tot_ris_wei_ass;
	}
	public String getR65_exposure_class() {
		return r65_exposure_class;
	}
	public void setR65_exposure_class(String r65_exposure_class) {
		this.r65_exposure_class = r65_exposure_class;
	}
	public BigDecimal getR65_expo_crm() {
		return r65_expo_crm;
	}
	public void setR65_expo_crm(BigDecimal r65_expo_crm) {
		this.r65_expo_crm = r65_expo_crm;
	}
	public BigDecimal getR65_spe_pro_expo() {
		return r65_spe_pro_expo;
	}
	public void setR65_spe_pro_expo(BigDecimal r65_spe_pro_expo) {
		this.r65_spe_pro_expo = r65_spe_pro_expo;
	}
	public BigDecimal getR65_amt_elig_sht_net() {
		return r65_amt_elig_sht_net;
	}
	public void setR65_amt_elig_sht_net(BigDecimal r65_amt_elig_sht_net) {
		this.r65_amt_elig_sht_net = r65_amt_elig_sht_net;
	}
	public BigDecimal getR65_tot_expo_net_spe() {
		return r65_tot_expo_net_spe;
	}
	public void setR65_tot_expo_net_spe(BigDecimal r65_tot_expo_net_spe) {
		this.r65_tot_expo_net_spe = r65_tot_expo_net_spe;
	}
	public BigDecimal getR65_crm_sub_elig_sub_app() {
		return r65_crm_sub_elig_sub_app;
	}
	public void setR65_crm_sub_elig_sub_app(BigDecimal r65_crm_sub_elig_sub_app) {
		this.r65_crm_sub_elig_sub_app = r65_crm_sub_elig_sub_app;
	}
	public BigDecimal getR65_crm_sub_non_col_guar() {
		return r65_crm_sub_non_col_guar;
	}
	public void setR65_crm_sub_non_col_guar(BigDecimal r65_crm_sub_non_col_guar) {
		this.r65_crm_sub_non_col_guar = r65_crm_sub_non_col_guar;
	}
	public BigDecimal getR65_crm_sub_non_col_cre_der() {
		return r65_crm_sub_non_col_cre_der;
	}
	public void setR65_crm_sub_non_col_cre_der(BigDecimal r65_crm_sub_non_col_cre_der) {
		this.r65_crm_sub_non_col_cre_der = r65_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR65_crm_sub_col_elig_cash() {
		return r65_crm_sub_col_elig_cash;
	}
	public void setR65_crm_sub_col_elig_cash(BigDecimal r65_crm_sub_col_elig_cash) {
		this.r65_crm_sub_col_elig_cash = r65_crm_sub_col_elig_cash;
	}
	public BigDecimal getR65_crm_sub_col_elig_trea_bills() {
		return r65_crm_sub_col_elig_trea_bills;
	}
	public void setR65_crm_sub_col_elig_trea_bills(BigDecimal r65_crm_sub_col_elig_trea_bills) {
		this.r65_crm_sub_col_elig_trea_bills = r65_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR65_crm_sub_col_elig_deb_sec() {
		return r65_crm_sub_col_elig_deb_sec;
	}
	public void setR65_crm_sub_col_elig_deb_sec(BigDecimal r65_crm_sub_col_elig_deb_sec) {
		this.r65_crm_sub_col_elig_deb_sec = r65_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR65_crm_sub_col_elig_equi() {
		return r65_crm_sub_col_elig_equi;
	}
	public void setR65_crm_sub_col_elig_equi(BigDecimal r65_crm_sub_col_elig_equi) {
		this.r65_crm_sub_col_elig_equi = r65_crm_sub_col_elig_equi;
	}
	public BigDecimal getR65_crm_sub_col_elig_unit_tru() {
		return r65_crm_sub_col_elig_unit_tru;
	}
	public void setR65_crm_sub_col_elig_unit_tru(BigDecimal r65_crm_sub_col_elig_unit_tru) {
		this.r65_crm_sub_col_elig_unit_tru = r65_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR65_crm_sub_col_exp_cov() {
		return r65_crm_sub_col_exp_cov;
	}
	public void setR65_crm_sub_col_exp_cov(BigDecimal r65_crm_sub_col_exp_cov) {
		this.r65_crm_sub_col_exp_cov = r65_crm_sub_col_exp_cov;
	}
	public BigDecimal getR65_crm_sub_col_elig_exp_not_cov() {
		return r65_crm_sub_col_elig_exp_not_cov;
	}
	public void setR65_crm_sub_col_elig_exp_not_cov(BigDecimal r65_crm_sub_col_elig_exp_not_cov) {
		this.r65_crm_sub_col_elig_exp_not_cov = r65_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR65_crm_sub_rwa_ris_crm() {
		return r65_crm_sub_rwa_ris_crm;
	}
	public void setR65_crm_sub_rwa_ris_crm(BigDecimal r65_crm_sub_rwa_ris_crm) {
		this.r65_crm_sub_rwa_ris_crm = r65_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR65_crm_sub_rwa_cov_crm() {
		return r65_crm_sub_rwa_cov_crm;
	}
	public void setR65_crm_sub_rwa_cov_crm(BigDecimal r65_crm_sub_rwa_cov_crm) {
		this.r65_crm_sub_rwa_cov_crm = r65_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR65_crm_sub_rwa_org_cou() {
		return r65_crm_sub_rwa_org_cou;
	}
	public void setR65_crm_sub_rwa_org_cou(BigDecimal r65_crm_sub_rwa_org_cou) {
		this.r65_crm_sub_rwa_org_cou = r65_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR65_crm_sub_rwa_not_cov_crm() {
		return r65_crm_sub_rwa_not_cov_crm;
	}
	public void setR65_crm_sub_rwa_not_cov_crm(BigDecimal r65_crm_sub_rwa_not_cov_crm) {
		this.r65_crm_sub_rwa_not_cov_crm = r65_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR65_crm_comp_col_expo_elig() {
		return r65_crm_comp_col_expo_elig;
	}
	public void setR65_crm_comp_col_expo_elig(BigDecimal r65_crm_comp_col_expo_elig) {
		this.r65_crm_comp_col_expo_elig = r65_crm_comp_col_expo_elig;
	}
	public BigDecimal getR65_crm_comp_col_elig_expo_vol_adj() {
		return r65_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR65_crm_comp_col_elig_expo_vol_adj(BigDecimal r65_crm_comp_col_elig_expo_vol_adj) {
		this.r65_crm_comp_col_elig_expo_vol_adj = r65_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR65_crm_comp_col_elig_fin_hai() {
		return r65_crm_comp_col_elig_fin_hai;
	}
	public void setR65_crm_comp_col_elig_fin_hai(BigDecimal r65_crm_comp_col_elig_fin_hai) {
		this.r65_crm_comp_col_elig_fin_hai = r65_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR65_crm_comp_col_expo_val() {
		return r65_crm_comp_col_expo_val;
	}
	public void setR65_crm_comp_col_expo_val(BigDecimal r65_crm_comp_col_expo_val) {
		this.r65_crm_comp_col_expo_val = r65_crm_comp_col_expo_val;
	}
	public BigDecimal getR65_rwa_elig_expo_not_cov_crm() {
		return r65_rwa_elig_expo_not_cov_crm;
	}
	public void setR65_rwa_elig_expo_not_cov_crm(BigDecimal r65_rwa_elig_expo_not_cov_crm) {
		this.r65_rwa_elig_expo_not_cov_crm = r65_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR65_rwa_unsec_expo_cre_ris() {
		return r65_rwa_unsec_expo_cre_ris;
	}
	public void setR65_rwa_unsec_expo_cre_ris(BigDecimal r65_rwa_unsec_expo_cre_ris) {
		this.r65_rwa_unsec_expo_cre_ris = r65_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR65_rwa_unsec_expo() {
		return r65_rwa_unsec_expo;
	}
	public void setR65_rwa_unsec_expo(BigDecimal r65_rwa_unsec_expo) {
		this.r65_rwa_unsec_expo = r65_rwa_unsec_expo;
	}
	public BigDecimal getR65_rwa_tot_ris_wei_ass() {
		return r65_rwa_tot_ris_wei_ass;
	}
	public void setR65_rwa_tot_ris_wei_ass(BigDecimal r65_rwa_tot_ris_wei_ass) {
		this.r65_rwa_tot_ris_wei_ass = r65_rwa_tot_ris_wei_ass;
	}
	public String getR66_exposure_class() {
		return r66_exposure_class;
	}
	public void setR66_exposure_class(String r66_exposure_class) {
		this.r66_exposure_class = r66_exposure_class;
	}
	public BigDecimal getR66_expo_crm() {
		return r66_expo_crm;
	}
	public void setR66_expo_crm(BigDecimal r66_expo_crm) {
		this.r66_expo_crm = r66_expo_crm;
	}
	public BigDecimal getR66_spe_pro_expo() {
		return r66_spe_pro_expo;
	}
	public void setR66_spe_pro_expo(BigDecimal r66_spe_pro_expo) {
		this.r66_spe_pro_expo = r66_spe_pro_expo;
	}
	public BigDecimal getR66_amt_elig_sht_net() {
		return r66_amt_elig_sht_net;
	}
	public void setR66_amt_elig_sht_net(BigDecimal r66_amt_elig_sht_net) {
		this.r66_amt_elig_sht_net = r66_amt_elig_sht_net;
	}
	public BigDecimal getR66_tot_expo_net_spe() {
		return r66_tot_expo_net_spe;
	}
	public void setR66_tot_expo_net_spe(BigDecimal r66_tot_expo_net_spe) {
		this.r66_tot_expo_net_spe = r66_tot_expo_net_spe;
	}
	public BigDecimal getR66_crm_sub_elig_sub_app() {
		return r66_crm_sub_elig_sub_app;
	}
	public void setR66_crm_sub_elig_sub_app(BigDecimal r66_crm_sub_elig_sub_app) {
		this.r66_crm_sub_elig_sub_app = r66_crm_sub_elig_sub_app;
	}
	public BigDecimal getR66_crm_sub_non_col_guar() {
		return r66_crm_sub_non_col_guar;
	}
	public void setR66_crm_sub_non_col_guar(BigDecimal r66_crm_sub_non_col_guar) {
		this.r66_crm_sub_non_col_guar = r66_crm_sub_non_col_guar;
	}
	public BigDecimal getR66_crm_sub_non_col_cre_der() {
		return r66_crm_sub_non_col_cre_der;
	}
	public void setR66_crm_sub_non_col_cre_der(BigDecimal r66_crm_sub_non_col_cre_der) {
		this.r66_crm_sub_non_col_cre_der = r66_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR66_crm_sub_col_elig_cash() {
		return r66_crm_sub_col_elig_cash;
	}
	public void setR66_crm_sub_col_elig_cash(BigDecimal r66_crm_sub_col_elig_cash) {
		this.r66_crm_sub_col_elig_cash = r66_crm_sub_col_elig_cash;
	}
	public BigDecimal getR66_crm_sub_col_elig_trea_bills() {
		return r66_crm_sub_col_elig_trea_bills;
	}
	public void setR66_crm_sub_col_elig_trea_bills(BigDecimal r66_crm_sub_col_elig_trea_bills) {
		this.r66_crm_sub_col_elig_trea_bills = r66_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR66_crm_sub_col_elig_deb_sec() {
		return r66_crm_sub_col_elig_deb_sec;
	}
	public void setR66_crm_sub_col_elig_deb_sec(BigDecimal r66_crm_sub_col_elig_deb_sec) {
		this.r66_crm_sub_col_elig_deb_sec = r66_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR66_crm_sub_col_elig_equi() {
		return r66_crm_sub_col_elig_equi;
	}
	public void setR66_crm_sub_col_elig_equi(BigDecimal r66_crm_sub_col_elig_equi) {
		this.r66_crm_sub_col_elig_equi = r66_crm_sub_col_elig_equi;
	}
	public BigDecimal getR66_crm_sub_col_elig_unit_tru() {
		return r66_crm_sub_col_elig_unit_tru;
	}
	public void setR66_crm_sub_col_elig_unit_tru(BigDecimal r66_crm_sub_col_elig_unit_tru) {
		this.r66_crm_sub_col_elig_unit_tru = r66_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR66_crm_sub_col_exp_cov() {
		return r66_crm_sub_col_exp_cov;
	}
	public void setR66_crm_sub_col_exp_cov(BigDecimal r66_crm_sub_col_exp_cov) {
		this.r66_crm_sub_col_exp_cov = r66_crm_sub_col_exp_cov;
	}
	public BigDecimal getR66_crm_sub_col_elig_exp_not_cov() {
		return r66_crm_sub_col_elig_exp_not_cov;
	}
	public void setR66_crm_sub_col_elig_exp_not_cov(BigDecimal r66_crm_sub_col_elig_exp_not_cov) {
		this.r66_crm_sub_col_elig_exp_not_cov = r66_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR66_crm_sub_rwa_ris_crm() {
		return r66_crm_sub_rwa_ris_crm;
	}
	public void setR66_crm_sub_rwa_ris_crm(BigDecimal r66_crm_sub_rwa_ris_crm) {
		this.r66_crm_sub_rwa_ris_crm = r66_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR66_crm_sub_rwa_cov_crm() {
		return r66_crm_sub_rwa_cov_crm;
	}
	public void setR66_crm_sub_rwa_cov_crm(BigDecimal r66_crm_sub_rwa_cov_crm) {
		this.r66_crm_sub_rwa_cov_crm = r66_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR66_crm_sub_rwa_org_cou() {
		return r66_crm_sub_rwa_org_cou;
	}
	public void setR66_crm_sub_rwa_org_cou(BigDecimal r66_crm_sub_rwa_org_cou) {
		this.r66_crm_sub_rwa_org_cou = r66_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR66_crm_sub_rwa_not_cov_crm() {
		return r66_crm_sub_rwa_not_cov_crm;
	}
	public void setR66_crm_sub_rwa_not_cov_crm(BigDecimal r66_crm_sub_rwa_not_cov_crm) {
		this.r66_crm_sub_rwa_not_cov_crm = r66_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR66_crm_comp_col_expo_elig() {
		return r66_crm_comp_col_expo_elig;
	}
	public void setR66_crm_comp_col_expo_elig(BigDecimal r66_crm_comp_col_expo_elig) {
		this.r66_crm_comp_col_expo_elig = r66_crm_comp_col_expo_elig;
	}
	public BigDecimal getR66_crm_comp_col_elig_expo_vol_adj() {
		return r66_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR66_crm_comp_col_elig_expo_vol_adj(BigDecimal r66_crm_comp_col_elig_expo_vol_adj) {
		this.r66_crm_comp_col_elig_expo_vol_adj = r66_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR66_crm_comp_col_elig_fin_hai() {
		return r66_crm_comp_col_elig_fin_hai;
	}
	public void setR66_crm_comp_col_elig_fin_hai(BigDecimal r66_crm_comp_col_elig_fin_hai) {
		this.r66_crm_comp_col_elig_fin_hai = r66_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR66_crm_comp_col_expo_val() {
		return r66_crm_comp_col_expo_val;
	}
	public void setR66_crm_comp_col_expo_val(BigDecimal r66_crm_comp_col_expo_val) {
		this.r66_crm_comp_col_expo_val = r66_crm_comp_col_expo_val;
	}
	public BigDecimal getR66_rwa_elig_expo_not_cov_crm() {
		return r66_rwa_elig_expo_not_cov_crm;
	}
	public void setR66_rwa_elig_expo_not_cov_crm(BigDecimal r66_rwa_elig_expo_not_cov_crm) {
		this.r66_rwa_elig_expo_not_cov_crm = r66_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR66_rwa_unsec_expo_cre_ris() {
		return r66_rwa_unsec_expo_cre_ris;
	}
	public void setR66_rwa_unsec_expo_cre_ris(BigDecimal r66_rwa_unsec_expo_cre_ris) {
		this.r66_rwa_unsec_expo_cre_ris = r66_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR66_rwa_unsec_expo() {
		return r66_rwa_unsec_expo;
	}
	public void setR66_rwa_unsec_expo(BigDecimal r66_rwa_unsec_expo) {
		this.r66_rwa_unsec_expo = r66_rwa_unsec_expo;
	}
	public BigDecimal getR66_rwa_tot_ris_wei_ass() {
		return r66_rwa_tot_ris_wei_ass;
	}
	public void setR66_rwa_tot_ris_wei_ass(BigDecimal r66_rwa_tot_ris_wei_ass) {
		this.r66_rwa_tot_ris_wei_ass = r66_rwa_tot_ris_wei_ass;
	}
	public String getR67_exposure_class() {
		return r67_exposure_class;
	}
	public void setR67_exposure_class(String r67_exposure_class) {
		this.r67_exposure_class = r67_exposure_class;
	}
	public BigDecimal getR67_expo_crm() {
		return r67_expo_crm;
	}
	public void setR67_expo_crm(BigDecimal r67_expo_crm) {
		this.r67_expo_crm = r67_expo_crm;
	}
	public BigDecimal getR67_spe_pro_expo() {
		return r67_spe_pro_expo;
	}
	public void setR67_spe_pro_expo(BigDecimal r67_spe_pro_expo) {
		this.r67_spe_pro_expo = r67_spe_pro_expo;
	}
	public BigDecimal getR67_amt_elig_sht_net() {
		return r67_amt_elig_sht_net;
	}
	public void setR67_amt_elig_sht_net(BigDecimal r67_amt_elig_sht_net) {
		this.r67_amt_elig_sht_net = r67_amt_elig_sht_net;
	}
	public BigDecimal getR67_tot_expo_net_spe() {
		return r67_tot_expo_net_spe;
	}
	public void setR67_tot_expo_net_spe(BigDecimal r67_tot_expo_net_spe) {
		this.r67_tot_expo_net_spe = r67_tot_expo_net_spe;
	}
	public BigDecimal getR67_crm_sub_elig_sub_app() {
		return r67_crm_sub_elig_sub_app;
	}
	public void setR67_crm_sub_elig_sub_app(BigDecimal r67_crm_sub_elig_sub_app) {
		this.r67_crm_sub_elig_sub_app = r67_crm_sub_elig_sub_app;
	}
	public BigDecimal getR67_crm_sub_non_col_guar() {
		return r67_crm_sub_non_col_guar;
	}
	public void setR67_crm_sub_non_col_guar(BigDecimal r67_crm_sub_non_col_guar) {
		this.r67_crm_sub_non_col_guar = r67_crm_sub_non_col_guar;
	}
	public BigDecimal getR67_crm_sub_non_col_cre_der() {
		return r67_crm_sub_non_col_cre_der;
	}
	public void setR67_crm_sub_non_col_cre_der(BigDecimal r67_crm_sub_non_col_cre_der) {
		this.r67_crm_sub_non_col_cre_der = r67_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR67_crm_sub_col_elig_cash() {
		return r67_crm_sub_col_elig_cash;
	}
	public void setR67_crm_sub_col_elig_cash(BigDecimal r67_crm_sub_col_elig_cash) {
		this.r67_crm_sub_col_elig_cash = r67_crm_sub_col_elig_cash;
	}
	public BigDecimal getR67_crm_sub_col_elig_trea_bills() {
		return r67_crm_sub_col_elig_trea_bills;
	}
	public void setR67_crm_sub_col_elig_trea_bills(BigDecimal r67_crm_sub_col_elig_trea_bills) {
		this.r67_crm_sub_col_elig_trea_bills = r67_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR67_crm_sub_col_elig_deb_sec() {
		return r67_crm_sub_col_elig_deb_sec;
	}
	public void setR67_crm_sub_col_elig_deb_sec(BigDecimal r67_crm_sub_col_elig_deb_sec) {
		this.r67_crm_sub_col_elig_deb_sec = r67_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR67_crm_sub_col_elig_equi() {
		return r67_crm_sub_col_elig_equi;
	}
	public void setR67_crm_sub_col_elig_equi(BigDecimal r67_crm_sub_col_elig_equi) {
		this.r67_crm_sub_col_elig_equi = r67_crm_sub_col_elig_equi;
	}
	public BigDecimal getR67_crm_sub_col_elig_unit_tru() {
		return r67_crm_sub_col_elig_unit_tru;
	}
	public void setR67_crm_sub_col_elig_unit_tru(BigDecimal r67_crm_sub_col_elig_unit_tru) {
		this.r67_crm_sub_col_elig_unit_tru = r67_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR67_crm_sub_col_exp_cov() {
		return r67_crm_sub_col_exp_cov;
	}
	public void setR67_crm_sub_col_exp_cov(BigDecimal r67_crm_sub_col_exp_cov) {
		this.r67_crm_sub_col_exp_cov = r67_crm_sub_col_exp_cov;
	}
	public BigDecimal getR67_crm_sub_col_elig_exp_not_cov() {
		return r67_crm_sub_col_elig_exp_not_cov;
	}
	public void setR67_crm_sub_col_elig_exp_not_cov(BigDecimal r67_crm_sub_col_elig_exp_not_cov) {
		this.r67_crm_sub_col_elig_exp_not_cov = r67_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR67_crm_sub_rwa_ris_crm() {
		return r67_crm_sub_rwa_ris_crm;
	}
	public void setR67_crm_sub_rwa_ris_crm(BigDecimal r67_crm_sub_rwa_ris_crm) {
		this.r67_crm_sub_rwa_ris_crm = r67_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR67_crm_sub_rwa_cov_crm() {
		return r67_crm_sub_rwa_cov_crm;
	}
	public void setR67_crm_sub_rwa_cov_crm(BigDecimal r67_crm_sub_rwa_cov_crm) {
		this.r67_crm_sub_rwa_cov_crm = r67_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR67_crm_sub_rwa_org_cou() {
		return r67_crm_sub_rwa_org_cou;
	}
	public void setR67_crm_sub_rwa_org_cou(BigDecimal r67_crm_sub_rwa_org_cou) {
		this.r67_crm_sub_rwa_org_cou = r67_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR67_crm_sub_rwa_not_cov_crm() {
		return r67_crm_sub_rwa_not_cov_crm;
	}
	public void setR67_crm_sub_rwa_not_cov_crm(BigDecimal r67_crm_sub_rwa_not_cov_crm) {
		this.r67_crm_sub_rwa_not_cov_crm = r67_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR67_crm_comp_col_expo_elig() {
		return r67_crm_comp_col_expo_elig;
	}
	public void setR67_crm_comp_col_expo_elig(BigDecimal r67_crm_comp_col_expo_elig) {
		this.r67_crm_comp_col_expo_elig = r67_crm_comp_col_expo_elig;
	}
	public BigDecimal getR67_crm_comp_col_elig_expo_vol_adj() {
		return r67_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR67_crm_comp_col_elig_expo_vol_adj(BigDecimal r67_crm_comp_col_elig_expo_vol_adj) {
		this.r67_crm_comp_col_elig_expo_vol_adj = r67_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR67_crm_comp_col_elig_fin_hai() {
		return r67_crm_comp_col_elig_fin_hai;
	}
	public void setR67_crm_comp_col_elig_fin_hai(BigDecimal r67_crm_comp_col_elig_fin_hai) {
		this.r67_crm_comp_col_elig_fin_hai = r67_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR67_crm_comp_col_expo_val() {
		return r67_crm_comp_col_expo_val;
	}
	public void setR67_crm_comp_col_expo_val(BigDecimal r67_crm_comp_col_expo_val) {
		this.r67_crm_comp_col_expo_val = r67_crm_comp_col_expo_val;
	}
	public BigDecimal getR67_rwa_elig_expo_not_cov_crm() {
		return r67_rwa_elig_expo_not_cov_crm;
	}
	public void setR67_rwa_elig_expo_not_cov_crm(BigDecimal r67_rwa_elig_expo_not_cov_crm) {
		this.r67_rwa_elig_expo_not_cov_crm = r67_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR67_rwa_unsec_expo_cre_ris() {
		return r67_rwa_unsec_expo_cre_ris;
	}
	public void setR67_rwa_unsec_expo_cre_ris(BigDecimal r67_rwa_unsec_expo_cre_ris) {
		this.r67_rwa_unsec_expo_cre_ris = r67_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR67_rwa_unsec_expo() {
		return r67_rwa_unsec_expo;
	}
	public void setR67_rwa_unsec_expo(BigDecimal r67_rwa_unsec_expo) {
		this.r67_rwa_unsec_expo = r67_rwa_unsec_expo;
	}
	public BigDecimal getR67_rwa_tot_ris_wei_ass() {
		return r67_rwa_tot_ris_wei_ass;
	}
	public void setR67_rwa_tot_ris_wei_ass(BigDecimal r67_rwa_tot_ris_wei_ass) {
		this.r67_rwa_tot_ris_wei_ass = r67_rwa_tot_ris_wei_ass;
	}
	public String getR68_exposure_class() {
		return r68_exposure_class;
	}
	public void setR68_exposure_class(String r68_exposure_class) {
		this.r68_exposure_class = r68_exposure_class;
	}
	public BigDecimal getR68_expo_crm() {
		return r68_expo_crm;
	}
	public void setR68_expo_crm(BigDecimal r68_expo_crm) {
		this.r68_expo_crm = r68_expo_crm;
	}
	public BigDecimal getR68_spe_pro_expo() {
		return r68_spe_pro_expo;
	}
	public void setR68_spe_pro_expo(BigDecimal r68_spe_pro_expo) {
		this.r68_spe_pro_expo = r68_spe_pro_expo;
	}
	public BigDecimal getR68_amt_elig_sht_net() {
		return r68_amt_elig_sht_net;
	}
	public void setR68_amt_elig_sht_net(BigDecimal r68_amt_elig_sht_net) {
		this.r68_amt_elig_sht_net = r68_amt_elig_sht_net;
	}
	public BigDecimal getR68_tot_expo_net_spe() {
		return r68_tot_expo_net_spe;
	}
	public void setR68_tot_expo_net_spe(BigDecimal r68_tot_expo_net_spe) {
		this.r68_tot_expo_net_spe = r68_tot_expo_net_spe;
	}
	public BigDecimal getR68_crm_sub_elig_sub_app() {
		return r68_crm_sub_elig_sub_app;
	}
	public void setR68_crm_sub_elig_sub_app(BigDecimal r68_crm_sub_elig_sub_app) {
		this.r68_crm_sub_elig_sub_app = r68_crm_sub_elig_sub_app;
	}
	public BigDecimal getR68_crm_sub_non_col_guar() {
		return r68_crm_sub_non_col_guar;
	}
	public void setR68_crm_sub_non_col_guar(BigDecimal r68_crm_sub_non_col_guar) {
		this.r68_crm_sub_non_col_guar = r68_crm_sub_non_col_guar;
	}
	public BigDecimal getR68_crm_sub_non_col_cre_der() {
		return r68_crm_sub_non_col_cre_der;
	}
	public void setR68_crm_sub_non_col_cre_der(BigDecimal r68_crm_sub_non_col_cre_der) {
		this.r68_crm_sub_non_col_cre_der = r68_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR68_crm_sub_col_elig_cash() {
		return r68_crm_sub_col_elig_cash;
	}
	public void setR68_crm_sub_col_elig_cash(BigDecimal r68_crm_sub_col_elig_cash) {
		this.r68_crm_sub_col_elig_cash = r68_crm_sub_col_elig_cash;
	}
	public BigDecimal getR68_crm_sub_col_elig_trea_bills() {
		return r68_crm_sub_col_elig_trea_bills;
	}
	public void setR68_crm_sub_col_elig_trea_bills(BigDecimal r68_crm_sub_col_elig_trea_bills) {
		this.r68_crm_sub_col_elig_trea_bills = r68_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR68_crm_sub_col_elig_deb_sec() {
		return r68_crm_sub_col_elig_deb_sec;
	}
	public void setR68_crm_sub_col_elig_deb_sec(BigDecimal r68_crm_sub_col_elig_deb_sec) {
		this.r68_crm_sub_col_elig_deb_sec = r68_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR68_crm_sub_col_elig_equi() {
		return r68_crm_sub_col_elig_equi;
	}
	public void setR68_crm_sub_col_elig_equi(BigDecimal r68_crm_sub_col_elig_equi) {
		this.r68_crm_sub_col_elig_equi = r68_crm_sub_col_elig_equi;
	}
	public BigDecimal getR68_crm_sub_col_elig_unit_tru() {
		return r68_crm_sub_col_elig_unit_tru;
	}
	public void setR68_crm_sub_col_elig_unit_tru(BigDecimal r68_crm_sub_col_elig_unit_tru) {
		this.r68_crm_sub_col_elig_unit_tru = r68_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR68_crm_sub_col_exp_cov() {
		return r68_crm_sub_col_exp_cov;
	}
	public void setR68_crm_sub_col_exp_cov(BigDecimal r68_crm_sub_col_exp_cov) {
		this.r68_crm_sub_col_exp_cov = r68_crm_sub_col_exp_cov;
	}
	public BigDecimal getR68_crm_sub_col_elig_exp_not_cov() {
		return r68_crm_sub_col_elig_exp_not_cov;
	}
	public void setR68_crm_sub_col_elig_exp_not_cov(BigDecimal r68_crm_sub_col_elig_exp_not_cov) {
		this.r68_crm_sub_col_elig_exp_not_cov = r68_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR68_crm_sub_rwa_ris_crm() {
		return r68_crm_sub_rwa_ris_crm;
	}
	public void setR68_crm_sub_rwa_ris_crm(BigDecimal r68_crm_sub_rwa_ris_crm) {
		this.r68_crm_sub_rwa_ris_crm = r68_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR68_crm_sub_rwa_cov_crm() {
		return r68_crm_sub_rwa_cov_crm;
	}
	public void setR68_crm_sub_rwa_cov_crm(BigDecimal r68_crm_sub_rwa_cov_crm) {
		this.r68_crm_sub_rwa_cov_crm = r68_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR68_crm_sub_rwa_org_cou() {
		return r68_crm_sub_rwa_org_cou;
	}
	public void setR68_crm_sub_rwa_org_cou(BigDecimal r68_crm_sub_rwa_org_cou) {
		this.r68_crm_sub_rwa_org_cou = r68_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR68_crm_sub_rwa_not_cov_crm() {
		return r68_crm_sub_rwa_not_cov_crm;
	}
	public void setR68_crm_sub_rwa_not_cov_crm(BigDecimal r68_crm_sub_rwa_not_cov_crm) {
		this.r68_crm_sub_rwa_not_cov_crm = r68_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR68_crm_comp_col_expo_elig() {
		return r68_crm_comp_col_expo_elig;
	}
	public void setR68_crm_comp_col_expo_elig(BigDecimal r68_crm_comp_col_expo_elig) {
		this.r68_crm_comp_col_expo_elig = r68_crm_comp_col_expo_elig;
	}
	public BigDecimal getR68_crm_comp_col_elig_expo_vol_adj() {
		return r68_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR68_crm_comp_col_elig_expo_vol_adj(BigDecimal r68_crm_comp_col_elig_expo_vol_adj) {
		this.r68_crm_comp_col_elig_expo_vol_adj = r68_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR68_crm_comp_col_elig_fin_hai() {
		return r68_crm_comp_col_elig_fin_hai;
	}
	public void setR68_crm_comp_col_elig_fin_hai(BigDecimal r68_crm_comp_col_elig_fin_hai) {
		this.r68_crm_comp_col_elig_fin_hai = r68_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR68_crm_comp_col_expo_val() {
		return r68_crm_comp_col_expo_val;
	}
	public void setR68_crm_comp_col_expo_val(BigDecimal r68_crm_comp_col_expo_val) {
		this.r68_crm_comp_col_expo_val = r68_crm_comp_col_expo_val;
	}
	public BigDecimal getR68_rwa_elig_expo_not_cov_crm() {
		return r68_rwa_elig_expo_not_cov_crm;
	}
	public void setR68_rwa_elig_expo_not_cov_crm(BigDecimal r68_rwa_elig_expo_not_cov_crm) {
		this.r68_rwa_elig_expo_not_cov_crm = r68_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR68_rwa_unsec_expo_cre_ris() {
		return r68_rwa_unsec_expo_cre_ris;
	}
	public void setR68_rwa_unsec_expo_cre_ris(BigDecimal r68_rwa_unsec_expo_cre_ris) {
		this.r68_rwa_unsec_expo_cre_ris = r68_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR68_rwa_unsec_expo() {
		return r68_rwa_unsec_expo;
	}
	public void setR68_rwa_unsec_expo(BigDecimal r68_rwa_unsec_expo) {
		this.r68_rwa_unsec_expo = r68_rwa_unsec_expo;
	}
	public BigDecimal getR68_rwa_tot_ris_wei_ass() {
		return r68_rwa_tot_ris_wei_ass;
	}
	public void setR68_rwa_tot_ris_wei_ass(BigDecimal r68_rwa_tot_ris_wei_ass) {
		this.r68_rwa_tot_ris_wei_ass = r68_rwa_tot_ris_wei_ass;
	}
	public String getR69_exposure_class() {
		return r69_exposure_class;
	}
	public void setR69_exposure_class(String r69_exposure_class) {
		this.r69_exposure_class = r69_exposure_class;
	}
	public BigDecimal getR69_expo_crm() {
		return r69_expo_crm;
	}
	public void setR69_expo_crm(BigDecimal r69_expo_crm) {
		this.r69_expo_crm = r69_expo_crm;
	}
	public BigDecimal getR69_spe_pro_expo() {
		return r69_spe_pro_expo;
	}
	public void setR69_spe_pro_expo(BigDecimal r69_spe_pro_expo) {
		this.r69_spe_pro_expo = r69_spe_pro_expo;
	}
	public BigDecimal getR69_amt_elig_sht_net() {
		return r69_amt_elig_sht_net;
	}
	public void setR69_amt_elig_sht_net(BigDecimal r69_amt_elig_sht_net) {
		this.r69_amt_elig_sht_net = r69_amt_elig_sht_net;
	}
	public BigDecimal getR69_tot_expo_net_spe() {
		return r69_tot_expo_net_spe;
	}
	public void setR69_tot_expo_net_spe(BigDecimal r69_tot_expo_net_spe) {
		this.r69_tot_expo_net_spe = r69_tot_expo_net_spe;
	}
	public BigDecimal getR69_crm_sub_elig_sub_app() {
		return r69_crm_sub_elig_sub_app;
	}
	public void setR69_crm_sub_elig_sub_app(BigDecimal r69_crm_sub_elig_sub_app) {
		this.r69_crm_sub_elig_sub_app = r69_crm_sub_elig_sub_app;
	}
	public BigDecimal getR69_crm_sub_non_col_guar() {
		return r69_crm_sub_non_col_guar;
	}
	public void setR69_crm_sub_non_col_guar(BigDecimal r69_crm_sub_non_col_guar) {
		this.r69_crm_sub_non_col_guar = r69_crm_sub_non_col_guar;
	}
	public BigDecimal getR69_crm_sub_non_col_cre_der() {
		return r69_crm_sub_non_col_cre_der;
	}
	public void setR69_crm_sub_non_col_cre_der(BigDecimal r69_crm_sub_non_col_cre_der) {
		this.r69_crm_sub_non_col_cre_der = r69_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR69_crm_sub_col_elig_cash() {
		return r69_crm_sub_col_elig_cash;
	}
	public void setR69_crm_sub_col_elig_cash(BigDecimal r69_crm_sub_col_elig_cash) {
		this.r69_crm_sub_col_elig_cash = r69_crm_sub_col_elig_cash;
	}
	public BigDecimal getR69_crm_sub_col_elig_trea_bills() {
		return r69_crm_sub_col_elig_trea_bills;
	}
	public void setR69_crm_sub_col_elig_trea_bills(BigDecimal r69_crm_sub_col_elig_trea_bills) {
		this.r69_crm_sub_col_elig_trea_bills = r69_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR69_crm_sub_col_elig_deb_sec() {
		return r69_crm_sub_col_elig_deb_sec;
	}
	public void setR69_crm_sub_col_elig_deb_sec(BigDecimal r69_crm_sub_col_elig_deb_sec) {
		this.r69_crm_sub_col_elig_deb_sec = r69_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR69_crm_sub_col_elig_equi() {
		return r69_crm_sub_col_elig_equi;
	}
	public void setR69_crm_sub_col_elig_equi(BigDecimal r69_crm_sub_col_elig_equi) {
		this.r69_crm_sub_col_elig_equi = r69_crm_sub_col_elig_equi;
	}
	public BigDecimal getR69_crm_sub_col_elig_unit_tru() {
		return r69_crm_sub_col_elig_unit_tru;
	}
	public void setR69_crm_sub_col_elig_unit_tru(BigDecimal r69_crm_sub_col_elig_unit_tru) {
		this.r69_crm_sub_col_elig_unit_tru = r69_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR69_crm_sub_col_exp_cov() {
		return r69_crm_sub_col_exp_cov;
	}
	public void setR69_crm_sub_col_exp_cov(BigDecimal r69_crm_sub_col_exp_cov) {
		this.r69_crm_sub_col_exp_cov = r69_crm_sub_col_exp_cov;
	}
	public BigDecimal getR69_crm_sub_col_elig_exp_not_cov() {
		return r69_crm_sub_col_elig_exp_not_cov;
	}
	public void setR69_crm_sub_col_elig_exp_not_cov(BigDecimal r69_crm_sub_col_elig_exp_not_cov) {
		this.r69_crm_sub_col_elig_exp_not_cov = r69_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR69_crm_sub_rwa_ris_crm() {
		return r69_crm_sub_rwa_ris_crm;
	}
	public void setR69_crm_sub_rwa_ris_crm(BigDecimal r69_crm_sub_rwa_ris_crm) {
		this.r69_crm_sub_rwa_ris_crm = r69_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR69_crm_sub_rwa_cov_crm() {
		return r69_crm_sub_rwa_cov_crm;
	}
	public void setR69_crm_sub_rwa_cov_crm(BigDecimal r69_crm_sub_rwa_cov_crm) {
		this.r69_crm_sub_rwa_cov_crm = r69_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR69_crm_sub_rwa_org_cou() {
		return r69_crm_sub_rwa_org_cou;
	}
	public void setR69_crm_sub_rwa_org_cou(BigDecimal r69_crm_sub_rwa_org_cou) {
		this.r69_crm_sub_rwa_org_cou = r69_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR69_crm_sub_rwa_not_cov_crm() {
		return r69_crm_sub_rwa_not_cov_crm;
	}
	public void setR69_crm_sub_rwa_not_cov_crm(BigDecimal r69_crm_sub_rwa_not_cov_crm) {
		this.r69_crm_sub_rwa_not_cov_crm = r69_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR69_crm_comp_col_expo_elig() {
		return r69_crm_comp_col_expo_elig;
	}
	public void setR69_crm_comp_col_expo_elig(BigDecimal r69_crm_comp_col_expo_elig) {
		this.r69_crm_comp_col_expo_elig = r69_crm_comp_col_expo_elig;
	}
	public BigDecimal getR69_crm_comp_col_elig_expo_vol_adj() {
		return r69_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR69_crm_comp_col_elig_expo_vol_adj(BigDecimal r69_crm_comp_col_elig_expo_vol_adj) {
		this.r69_crm_comp_col_elig_expo_vol_adj = r69_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR69_crm_comp_col_elig_fin_hai() {
		return r69_crm_comp_col_elig_fin_hai;
	}
	public void setR69_crm_comp_col_elig_fin_hai(BigDecimal r69_crm_comp_col_elig_fin_hai) {
		this.r69_crm_comp_col_elig_fin_hai = r69_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR69_crm_comp_col_expo_val() {
		return r69_crm_comp_col_expo_val;
	}
	public void setR69_crm_comp_col_expo_val(BigDecimal r69_crm_comp_col_expo_val) {
		this.r69_crm_comp_col_expo_val = r69_crm_comp_col_expo_val;
	}
	public BigDecimal getR69_rwa_elig_expo_not_cov_crm() {
		return r69_rwa_elig_expo_not_cov_crm;
	}
	public void setR69_rwa_elig_expo_not_cov_crm(BigDecimal r69_rwa_elig_expo_not_cov_crm) {
		this.r69_rwa_elig_expo_not_cov_crm = r69_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR69_rwa_unsec_expo_cre_ris() {
		return r69_rwa_unsec_expo_cre_ris;
	}
	public void setR69_rwa_unsec_expo_cre_ris(BigDecimal r69_rwa_unsec_expo_cre_ris) {
		this.r69_rwa_unsec_expo_cre_ris = r69_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR69_rwa_unsec_expo() {
		return r69_rwa_unsec_expo;
	}
	public void setR69_rwa_unsec_expo(BigDecimal r69_rwa_unsec_expo) {
		this.r69_rwa_unsec_expo = r69_rwa_unsec_expo;
	}
	public BigDecimal getR69_rwa_tot_ris_wei_ass() {
		return r69_rwa_tot_ris_wei_ass;
	}
	public void setR69_rwa_tot_ris_wei_ass(BigDecimal r69_rwa_tot_ris_wei_ass) {
		this.r69_rwa_tot_ris_wei_ass = r69_rwa_tot_ris_wei_ass;
	}
	public String getR70_exposure_class() {
		return r70_exposure_class;
	}
	public void setR70_exposure_class(String r70_exposure_class) {
		this.r70_exposure_class = r70_exposure_class;
	}
	public BigDecimal getR70_expo_crm() {
		return r70_expo_crm;
	}
	public void setR70_expo_crm(BigDecimal r70_expo_crm) {
		this.r70_expo_crm = r70_expo_crm;
	}
	public BigDecimal getR70_spe_pro_expo() {
		return r70_spe_pro_expo;
	}
	public void setR70_spe_pro_expo(BigDecimal r70_spe_pro_expo) {
		this.r70_spe_pro_expo = r70_spe_pro_expo;
	}
	public BigDecimal getR70_amt_elig_sht_net() {
		return r70_amt_elig_sht_net;
	}
	public void setR70_amt_elig_sht_net(BigDecimal r70_amt_elig_sht_net) {
		this.r70_amt_elig_sht_net = r70_amt_elig_sht_net;
	}
	public BigDecimal getR70_tot_expo_net_spe() {
		return r70_tot_expo_net_spe;
	}
	public void setR70_tot_expo_net_spe(BigDecimal r70_tot_expo_net_spe) {
		this.r70_tot_expo_net_spe = r70_tot_expo_net_spe;
	}
	public BigDecimal getR70_crm_sub_elig_sub_app() {
		return r70_crm_sub_elig_sub_app;
	}
	public void setR70_crm_sub_elig_sub_app(BigDecimal r70_crm_sub_elig_sub_app) {
		this.r70_crm_sub_elig_sub_app = r70_crm_sub_elig_sub_app;
	}
	public BigDecimal getR70_crm_sub_non_col_guar() {
		return r70_crm_sub_non_col_guar;
	}
	public void setR70_crm_sub_non_col_guar(BigDecimal r70_crm_sub_non_col_guar) {
		this.r70_crm_sub_non_col_guar = r70_crm_sub_non_col_guar;
	}
	public BigDecimal getR70_crm_sub_non_col_cre_der() {
		return r70_crm_sub_non_col_cre_der;
	}
	public void setR70_crm_sub_non_col_cre_der(BigDecimal r70_crm_sub_non_col_cre_der) {
		this.r70_crm_sub_non_col_cre_der = r70_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR70_crm_sub_col_elig_cash() {
		return r70_crm_sub_col_elig_cash;
	}
	public void setR70_crm_sub_col_elig_cash(BigDecimal r70_crm_sub_col_elig_cash) {
		this.r70_crm_sub_col_elig_cash = r70_crm_sub_col_elig_cash;
	}
	public BigDecimal getR70_crm_sub_col_elig_trea_bills() {
		return r70_crm_sub_col_elig_trea_bills;
	}
	public void setR70_crm_sub_col_elig_trea_bills(BigDecimal r70_crm_sub_col_elig_trea_bills) {
		this.r70_crm_sub_col_elig_trea_bills = r70_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR70_crm_sub_col_elig_deb_sec() {
		return r70_crm_sub_col_elig_deb_sec;
	}
	public void setR70_crm_sub_col_elig_deb_sec(BigDecimal r70_crm_sub_col_elig_deb_sec) {
		this.r70_crm_sub_col_elig_deb_sec = r70_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR70_crm_sub_col_elig_equi() {
		return r70_crm_sub_col_elig_equi;
	}
	public void setR70_crm_sub_col_elig_equi(BigDecimal r70_crm_sub_col_elig_equi) {
		this.r70_crm_sub_col_elig_equi = r70_crm_sub_col_elig_equi;
	}
	public BigDecimal getR70_crm_sub_col_elig_unit_tru() {
		return r70_crm_sub_col_elig_unit_tru;
	}
	public void setR70_crm_sub_col_elig_unit_tru(BigDecimal r70_crm_sub_col_elig_unit_tru) {
		this.r70_crm_sub_col_elig_unit_tru = r70_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR70_crm_sub_col_exp_cov() {
		return r70_crm_sub_col_exp_cov;
	}
	public void setR70_crm_sub_col_exp_cov(BigDecimal r70_crm_sub_col_exp_cov) {
		this.r70_crm_sub_col_exp_cov = r70_crm_sub_col_exp_cov;
	}
	public BigDecimal getR70_crm_sub_col_elig_exp_not_cov() {
		return r70_crm_sub_col_elig_exp_not_cov;
	}
	public void setR70_crm_sub_col_elig_exp_not_cov(BigDecimal r70_crm_sub_col_elig_exp_not_cov) {
		this.r70_crm_sub_col_elig_exp_not_cov = r70_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR70_crm_sub_rwa_ris_crm() {
		return r70_crm_sub_rwa_ris_crm;
	}
	public void setR70_crm_sub_rwa_ris_crm(BigDecimal r70_crm_sub_rwa_ris_crm) {
		this.r70_crm_sub_rwa_ris_crm = r70_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR70_crm_sub_rwa_cov_crm() {
		return r70_crm_sub_rwa_cov_crm;
	}
	public void setR70_crm_sub_rwa_cov_crm(BigDecimal r70_crm_sub_rwa_cov_crm) {
		this.r70_crm_sub_rwa_cov_crm = r70_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR70_crm_sub_rwa_org_cou() {
		return r70_crm_sub_rwa_org_cou;
	}
	public void setR70_crm_sub_rwa_org_cou(BigDecimal r70_crm_sub_rwa_org_cou) {
		this.r70_crm_sub_rwa_org_cou = r70_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR70_crm_sub_rwa_not_cov_crm() {
		return r70_crm_sub_rwa_not_cov_crm;
	}
	public void setR70_crm_sub_rwa_not_cov_crm(BigDecimal r70_crm_sub_rwa_not_cov_crm) {
		this.r70_crm_sub_rwa_not_cov_crm = r70_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR70_crm_comp_col_expo_elig() {
		return r70_crm_comp_col_expo_elig;
	}
	public void setR70_crm_comp_col_expo_elig(BigDecimal r70_crm_comp_col_expo_elig) {
		this.r70_crm_comp_col_expo_elig = r70_crm_comp_col_expo_elig;
	}
	public BigDecimal getR70_crm_comp_col_elig_expo_vol_adj() {
		return r70_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR70_crm_comp_col_elig_expo_vol_adj(BigDecimal r70_crm_comp_col_elig_expo_vol_adj) {
		this.r70_crm_comp_col_elig_expo_vol_adj = r70_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR70_crm_comp_col_elig_fin_hai() {
		return r70_crm_comp_col_elig_fin_hai;
	}
	public void setR70_crm_comp_col_elig_fin_hai(BigDecimal r70_crm_comp_col_elig_fin_hai) {
		this.r70_crm_comp_col_elig_fin_hai = r70_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR70_crm_comp_col_expo_val() {
		return r70_crm_comp_col_expo_val;
	}
	public void setR70_crm_comp_col_expo_val(BigDecimal r70_crm_comp_col_expo_val) {
		this.r70_crm_comp_col_expo_val = r70_crm_comp_col_expo_val;
	}
	public BigDecimal getR70_rwa_elig_expo_not_cov_crm() {
		return r70_rwa_elig_expo_not_cov_crm;
	}
	public void setR70_rwa_elig_expo_not_cov_crm(BigDecimal r70_rwa_elig_expo_not_cov_crm) {
		this.r70_rwa_elig_expo_not_cov_crm = r70_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR70_rwa_unsec_expo_cre_ris() {
		return r70_rwa_unsec_expo_cre_ris;
	}
	public void setR70_rwa_unsec_expo_cre_ris(BigDecimal r70_rwa_unsec_expo_cre_ris) {
		this.r70_rwa_unsec_expo_cre_ris = r70_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR70_rwa_unsec_expo() {
		return r70_rwa_unsec_expo;
	}
	public void setR70_rwa_unsec_expo(BigDecimal r70_rwa_unsec_expo) {
		this.r70_rwa_unsec_expo = r70_rwa_unsec_expo;
	}
	public BigDecimal getR70_rwa_tot_ris_wei_ass() {
		return r70_rwa_tot_ris_wei_ass;
	}
	public void setR70_rwa_tot_ris_wei_ass(BigDecimal r70_rwa_tot_ris_wei_ass) {
		this.r70_rwa_tot_ris_wei_ass = r70_rwa_tot_ris_wei_ass;
	}
	public String getR73_exposure_class() {
		return r73_exposure_class;
	}
	public void setR73_exposure_class(String r73_exposure_class) {
		this.r73_exposure_class = r73_exposure_class;
	}
	public BigDecimal getR73_expo_crm() {
		return r73_expo_crm;
	}
	public void setR73_expo_crm(BigDecimal r73_expo_crm) {
		this.r73_expo_crm = r73_expo_crm;
	}
	public BigDecimal getR73_spe_pro_expo() {
		return r73_spe_pro_expo;
	}
	public void setR73_spe_pro_expo(BigDecimal r73_spe_pro_expo) {
		this.r73_spe_pro_expo = r73_spe_pro_expo;
	}
	public BigDecimal getR73_amt_elig_sht_net() {
		return r73_amt_elig_sht_net;
	}
	public void setR73_amt_elig_sht_net(BigDecimal r73_amt_elig_sht_net) {
		this.r73_amt_elig_sht_net = r73_amt_elig_sht_net;
	}
	public BigDecimal getR73_tot_expo_net_spe() {
		return r73_tot_expo_net_spe;
	}
	public void setR73_tot_expo_net_spe(BigDecimal r73_tot_expo_net_spe) {
		this.r73_tot_expo_net_spe = r73_tot_expo_net_spe;
	}
	public BigDecimal getR73_crm_sub_elig_sub_app() {
		return r73_crm_sub_elig_sub_app;
	}
	public void setR73_crm_sub_elig_sub_app(BigDecimal r73_crm_sub_elig_sub_app) {
		this.r73_crm_sub_elig_sub_app = r73_crm_sub_elig_sub_app;
	}
	public BigDecimal getR73_crm_sub_non_col_guar() {
		return r73_crm_sub_non_col_guar;
	}
	public void setR73_crm_sub_non_col_guar(BigDecimal r73_crm_sub_non_col_guar) {
		this.r73_crm_sub_non_col_guar = r73_crm_sub_non_col_guar;
	}
	public BigDecimal getR73_crm_sub_non_col_cre_der() {
		return r73_crm_sub_non_col_cre_der;
	}
	public void setR73_crm_sub_non_col_cre_der(BigDecimal r73_crm_sub_non_col_cre_der) {
		this.r73_crm_sub_non_col_cre_der = r73_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR73_crm_sub_col_elig_cash() {
		return r73_crm_sub_col_elig_cash;
	}
	public void setR73_crm_sub_col_elig_cash(BigDecimal r73_crm_sub_col_elig_cash) {
		this.r73_crm_sub_col_elig_cash = r73_crm_sub_col_elig_cash;
	}
	public BigDecimal getR73_crm_sub_col_elig_trea_bills() {
		return r73_crm_sub_col_elig_trea_bills;
	}
	public void setR73_crm_sub_col_elig_trea_bills(BigDecimal r73_crm_sub_col_elig_trea_bills) {
		this.r73_crm_sub_col_elig_trea_bills = r73_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR73_crm_sub_col_elig_deb_sec() {
		return r73_crm_sub_col_elig_deb_sec;
	}
	public void setR73_crm_sub_col_elig_deb_sec(BigDecimal r73_crm_sub_col_elig_deb_sec) {
		this.r73_crm_sub_col_elig_deb_sec = r73_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR73_crm_sub_col_elig_equi() {
		return r73_crm_sub_col_elig_equi;
	}
	public void setR73_crm_sub_col_elig_equi(BigDecimal r73_crm_sub_col_elig_equi) {
		this.r73_crm_sub_col_elig_equi = r73_crm_sub_col_elig_equi;
	}
	public BigDecimal getR73_crm_sub_col_elig_unit_tru() {
		return r73_crm_sub_col_elig_unit_tru;
	}
	public void setR73_crm_sub_col_elig_unit_tru(BigDecimal r73_crm_sub_col_elig_unit_tru) {
		this.r73_crm_sub_col_elig_unit_tru = r73_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR73_crm_sub_col_exp_cov() {
		return r73_crm_sub_col_exp_cov;
	}
	public void setR73_crm_sub_col_exp_cov(BigDecimal r73_crm_sub_col_exp_cov) {
		this.r73_crm_sub_col_exp_cov = r73_crm_sub_col_exp_cov;
	}
	public BigDecimal getR73_crm_sub_col_elig_exp_not_cov() {
		return r73_crm_sub_col_elig_exp_not_cov;
	}
	public void setR73_crm_sub_col_elig_exp_not_cov(BigDecimal r73_crm_sub_col_elig_exp_not_cov) {
		this.r73_crm_sub_col_elig_exp_not_cov = r73_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR73_crm_sub_rwa_ris_crm() {
		return r73_crm_sub_rwa_ris_crm;
	}
	public void setR73_crm_sub_rwa_ris_crm(BigDecimal r73_crm_sub_rwa_ris_crm) {
		this.r73_crm_sub_rwa_ris_crm = r73_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR73_crm_sub_rwa_cov_crm() {
		return r73_crm_sub_rwa_cov_crm;
	}
	public void setR73_crm_sub_rwa_cov_crm(BigDecimal r73_crm_sub_rwa_cov_crm) {
		this.r73_crm_sub_rwa_cov_crm = r73_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR73_crm_sub_rwa_org_cou() {
		return r73_crm_sub_rwa_org_cou;
	}
	public void setR73_crm_sub_rwa_org_cou(BigDecimal r73_crm_sub_rwa_org_cou) {
		this.r73_crm_sub_rwa_org_cou = r73_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR73_crm_sub_rwa_not_cov_crm() {
		return r73_crm_sub_rwa_not_cov_crm;
	}
	public void setR73_crm_sub_rwa_not_cov_crm(BigDecimal r73_crm_sub_rwa_not_cov_crm) {
		this.r73_crm_sub_rwa_not_cov_crm = r73_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR73_crm_comp_col_expo_elig() {
		return r73_crm_comp_col_expo_elig;
	}
	public void setR73_crm_comp_col_expo_elig(BigDecimal r73_crm_comp_col_expo_elig) {
		this.r73_crm_comp_col_expo_elig = r73_crm_comp_col_expo_elig;
	}
	public BigDecimal getR73_crm_comp_col_elig_expo_vol_adj() {
		return r73_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR73_crm_comp_col_elig_expo_vol_adj(BigDecimal r73_crm_comp_col_elig_expo_vol_adj) {
		this.r73_crm_comp_col_elig_expo_vol_adj = r73_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR73_crm_comp_col_elig_fin_hai() {
		return r73_crm_comp_col_elig_fin_hai;
	}
	public void setR73_crm_comp_col_elig_fin_hai(BigDecimal r73_crm_comp_col_elig_fin_hai) {
		this.r73_crm_comp_col_elig_fin_hai = r73_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR73_crm_comp_col_expo_val() {
		return r73_crm_comp_col_expo_val;
	}
	public void setR73_crm_comp_col_expo_val(BigDecimal r73_crm_comp_col_expo_val) {
		this.r73_crm_comp_col_expo_val = r73_crm_comp_col_expo_val;
	}
	public BigDecimal getR73_rwa_elig_expo_not_cov_crm() {
		return r73_rwa_elig_expo_not_cov_crm;
	}
	public void setR73_rwa_elig_expo_not_cov_crm(BigDecimal r73_rwa_elig_expo_not_cov_crm) {
		this.r73_rwa_elig_expo_not_cov_crm = r73_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR73_rwa_unsec_expo_cre_ris() {
		return r73_rwa_unsec_expo_cre_ris;
	}
	public void setR73_rwa_unsec_expo_cre_ris(BigDecimal r73_rwa_unsec_expo_cre_ris) {
		this.r73_rwa_unsec_expo_cre_ris = r73_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR73_rwa_unsec_expo() {
		return r73_rwa_unsec_expo;
	}
	public void setR73_rwa_unsec_expo(BigDecimal r73_rwa_unsec_expo) {
		this.r73_rwa_unsec_expo = r73_rwa_unsec_expo;
	}
	public BigDecimal getR73_rwa_tot_ris_wei_ass() {
		return r73_rwa_tot_ris_wei_ass;
	}
	public void setR73_rwa_tot_ris_wei_ass(BigDecimal r73_rwa_tot_ris_wei_ass) {
		this.r73_rwa_tot_ris_wei_ass = r73_rwa_tot_ris_wei_ass;
	}
	public String getR74_exposure_class() {
		return r74_exposure_class;
	}
	public void setR74_exposure_class(String r74_exposure_class) {
		this.r74_exposure_class = r74_exposure_class;
	}
	public BigDecimal getR74_expo_crm() {
		return r74_expo_crm;
	}
	public void setR74_expo_crm(BigDecimal r74_expo_crm) {
		this.r74_expo_crm = r74_expo_crm;
	}
	public BigDecimal getR74_spe_pro_expo() {
		return r74_spe_pro_expo;
	}
	public void setR74_spe_pro_expo(BigDecimal r74_spe_pro_expo) {
		this.r74_spe_pro_expo = r74_spe_pro_expo;
	}
	public BigDecimal getR74_amt_elig_sht_net() {
		return r74_amt_elig_sht_net;
	}
	public void setR74_amt_elig_sht_net(BigDecimal r74_amt_elig_sht_net) {
		this.r74_amt_elig_sht_net = r74_amt_elig_sht_net;
	}
	public BigDecimal getR74_tot_expo_net_spe() {
		return r74_tot_expo_net_spe;
	}
	public void setR74_tot_expo_net_spe(BigDecimal r74_tot_expo_net_spe) {
		this.r74_tot_expo_net_spe = r74_tot_expo_net_spe;
	}
	public BigDecimal getR74_crm_sub_elig_sub_app() {
		return r74_crm_sub_elig_sub_app;
	}
	public void setR74_crm_sub_elig_sub_app(BigDecimal r74_crm_sub_elig_sub_app) {
		this.r74_crm_sub_elig_sub_app = r74_crm_sub_elig_sub_app;
	}
	public BigDecimal getR74_crm_sub_non_col_guar() {
		return r74_crm_sub_non_col_guar;
	}
	public void setR74_crm_sub_non_col_guar(BigDecimal r74_crm_sub_non_col_guar) {
		this.r74_crm_sub_non_col_guar = r74_crm_sub_non_col_guar;
	}
	public BigDecimal getR74_crm_sub_non_col_cre_der() {
		return r74_crm_sub_non_col_cre_der;
	}
	public void setR74_crm_sub_non_col_cre_der(BigDecimal r74_crm_sub_non_col_cre_der) {
		this.r74_crm_sub_non_col_cre_der = r74_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR74_crm_sub_col_elig_cash() {
		return r74_crm_sub_col_elig_cash;
	}
	public void setR74_crm_sub_col_elig_cash(BigDecimal r74_crm_sub_col_elig_cash) {
		this.r74_crm_sub_col_elig_cash = r74_crm_sub_col_elig_cash;
	}
	public BigDecimal getR74_crm_sub_col_elig_trea_bills() {
		return r74_crm_sub_col_elig_trea_bills;
	}
	public void setR74_crm_sub_col_elig_trea_bills(BigDecimal r74_crm_sub_col_elig_trea_bills) {
		this.r74_crm_sub_col_elig_trea_bills = r74_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR74_crm_sub_col_elig_deb_sec() {
		return r74_crm_sub_col_elig_deb_sec;
	}
	public void setR74_crm_sub_col_elig_deb_sec(BigDecimal r74_crm_sub_col_elig_deb_sec) {
		this.r74_crm_sub_col_elig_deb_sec = r74_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR74_crm_sub_col_elig_equi() {
		return r74_crm_sub_col_elig_equi;
	}
	public void setR74_crm_sub_col_elig_equi(BigDecimal r74_crm_sub_col_elig_equi) {
		this.r74_crm_sub_col_elig_equi = r74_crm_sub_col_elig_equi;
	}
	public BigDecimal getR74_crm_sub_col_elig_unit_tru() {
		return r74_crm_sub_col_elig_unit_tru;
	}
	public void setR74_crm_sub_col_elig_unit_tru(BigDecimal r74_crm_sub_col_elig_unit_tru) {
		this.r74_crm_sub_col_elig_unit_tru = r74_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR74_crm_sub_col_exp_cov() {
		return r74_crm_sub_col_exp_cov;
	}
	public void setR74_crm_sub_col_exp_cov(BigDecimal r74_crm_sub_col_exp_cov) {
		this.r74_crm_sub_col_exp_cov = r74_crm_sub_col_exp_cov;
	}
	public BigDecimal getR74_crm_sub_col_elig_exp_not_cov() {
		return r74_crm_sub_col_elig_exp_not_cov;
	}
	public void setR74_crm_sub_col_elig_exp_not_cov(BigDecimal r74_crm_sub_col_elig_exp_not_cov) {
		this.r74_crm_sub_col_elig_exp_not_cov = r74_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR74_crm_sub_rwa_ris_crm() {
		return r74_crm_sub_rwa_ris_crm;
	}
	public void setR74_crm_sub_rwa_ris_crm(BigDecimal r74_crm_sub_rwa_ris_crm) {
		this.r74_crm_sub_rwa_ris_crm = r74_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR74_crm_sub_rwa_cov_crm() {
		return r74_crm_sub_rwa_cov_crm;
	}
	public void setR74_crm_sub_rwa_cov_crm(BigDecimal r74_crm_sub_rwa_cov_crm) {
		this.r74_crm_sub_rwa_cov_crm = r74_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR74_crm_sub_rwa_org_cou() {
		return r74_crm_sub_rwa_org_cou;
	}
	public void setR74_crm_sub_rwa_org_cou(BigDecimal r74_crm_sub_rwa_org_cou) {
		this.r74_crm_sub_rwa_org_cou = r74_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR74_crm_sub_rwa_not_cov_crm() {
		return r74_crm_sub_rwa_not_cov_crm;
	}
	public void setR74_crm_sub_rwa_not_cov_crm(BigDecimal r74_crm_sub_rwa_not_cov_crm) {
		this.r74_crm_sub_rwa_not_cov_crm = r74_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR74_crm_comp_col_expo_elig() {
		return r74_crm_comp_col_expo_elig;
	}
	public void setR74_crm_comp_col_expo_elig(BigDecimal r74_crm_comp_col_expo_elig) {
		this.r74_crm_comp_col_expo_elig = r74_crm_comp_col_expo_elig;
	}
	public BigDecimal getR74_crm_comp_col_elig_expo_vol_adj() {
		return r74_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR74_crm_comp_col_elig_expo_vol_adj(BigDecimal r74_crm_comp_col_elig_expo_vol_adj) {
		this.r74_crm_comp_col_elig_expo_vol_adj = r74_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR74_crm_comp_col_elig_fin_hai() {
		return r74_crm_comp_col_elig_fin_hai;
	}
	public void setR74_crm_comp_col_elig_fin_hai(BigDecimal r74_crm_comp_col_elig_fin_hai) {
		this.r74_crm_comp_col_elig_fin_hai = r74_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR74_crm_comp_col_expo_val() {
		return r74_crm_comp_col_expo_val;
	}
	public void setR74_crm_comp_col_expo_val(BigDecimal r74_crm_comp_col_expo_val) {
		this.r74_crm_comp_col_expo_val = r74_crm_comp_col_expo_val;
	}
	public BigDecimal getR74_rwa_elig_expo_not_cov_crm() {
		return r74_rwa_elig_expo_not_cov_crm;
	}
	public void setR74_rwa_elig_expo_not_cov_crm(BigDecimal r74_rwa_elig_expo_not_cov_crm) {
		this.r74_rwa_elig_expo_not_cov_crm = r74_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR74_rwa_unsec_expo_cre_ris() {
		return r74_rwa_unsec_expo_cre_ris;
	}
	public void setR74_rwa_unsec_expo_cre_ris(BigDecimal r74_rwa_unsec_expo_cre_ris) {
		this.r74_rwa_unsec_expo_cre_ris = r74_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR74_rwa_unsec_expo() {
		return r74_rwa_unsec_expo;
	}
	public void setR74_rwa_unsec_expo(BigDecimal r74_rwa_unsec_expo) {
		this.r74_rwa_unsec_expo = r74_rwa_unsec_expo;
	}
	public BigDecimal getR74_rwa_tot_ris_wei_ass() {
		return r74_rwa_tot_ris_wei_ass;
	}
	public void setR74_rwa_tot_ris_wei_ass(BigDecimal r74_rwa_tot_ris_wei_ass) {
		this.r74_rwa_tot_ris_wei_ass = r74_rwa_tot_ris_wei_ass;
	}
	public String getR75_exposure_class() {
		return r75_exposure_class;
	}
	public void setR75_exposure_class(String r75_exposure_class) {
		this.r75_exposure_class = r75_exposure_class;
	}
	public BigDecimal getR75_expo_crm() {
		return r75_expo_crm;
	}
	public void setR75_expo_crm(BigDecimal r75_expo_crm) {
		this.r75_expo_crm = r75_expo_crm;
	}
	public BigDecimal getR75_spe_pro_expo() {
		return r75_spe_pro_expo;
	}
	public void setR75_spe_pro_expo(BigDecimal r75_spe_pro_expo) {
		this.r75_spe_pro_expo = r75_spe_pro_expo;
	}
	public BigDecimal getR75_amt_elig_sht_net() {
		return r75_amt_elig_sht_net;
	}
	public void setR75_amt_elig_sht_net(BigDecimal r75_amt_elig_sht_net) {
		this.r75_amt_elig_sht_net = r75_amt_elig_sht_net;
	}
	public BigDecimal getR75_tot_expo_net_spe() {
		return r75_tot_expo_net_spe;
	}
	public void setR75_tot_expo_net_spe(BigDecimal r75_tot_expo_net_spe) {
		this.r75_tot_expo_net_spe = r75_tot_expo_net_spe;
	}
	public BigDecimal getR75_crm_sub_elig_sub_app() {
		return r75_crm_sub_elig_sub_app;
	}
	public void setR75_crm_sub_elig_sub_app(BigDecimal r75_crm_sub_elig_sub_app) {
		this.r75_crm_sub_elig_sub_app = r75_crm_sub_elig_sub_app;
	}
	public BigDecimal getR75_crm_sub_non_col_guar() {
		return r75_crm_sub_non_col_guar;
	}
	public void setR75_crm_sub_non_col_guar(BigDecimal r75_crm_sub_non_col_guar) {
		this.r75_crm_sub_non_col_guar = r75_crm_sub_non_col_guar;
	}
	public BigDecimal getR75_crm_sub_non_col_cre_der() {
		return r75_crm_sub_non_col_cre_der;
	}
	public void setR75_crm_sub_non_col_cre_der(BigDecimal r75_crm_sub_non_col_cre_der) {
		this.r75_crm_sub_non_col_cre_der = r75_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR75_crm_sub_col_elig_cash() {
		return r75_crm_sub_col_elig_cash;
	}
	public void setR75_crm_sub_col_elig_cash(BigDecimal r75_crm_sub_col_elig_cash) {
		this.r75_crm_sub_col_elig_cash = r75_crm_sub_col_elig_cash;
	}
	public BigDecimal getR75_crm_sub_col_elig_trea_bills() {
		return r75_crm_sub_col_elig_trea_bills;
	}
	public void setR75_crm_sub_col_elig_trea_bills(BigDecimal r75_crm_sub_col_elig_trea_bills) {
		this.r75_crm_sub_col_elig_trea_bills = r75_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR75_crm_sub_col_elig_deb_sec() {
		return r75_crm_sub_col_elig_deb_sec;
	}
	public void setR75_crm_sub_col_elig_deb_sec(BigDecimal r75_crm_sub_col_elig_deb_sec) {
		this.r75_crm_sub_col_elig_deb_sec = r75_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR75_crm_sub_col_elig_equi() {
		return r75_crm_sub_col_elig_equi;
	}
	public void setR75_crm_sub_col_elig_equi(BigDecimal r75_crm_sub_col_elig_equi) {
		this.r75_crm_sub_col_elig_equi = r75_crm_sub_col_elig_equi;
	}
	public BigDecimal getR75_crm_sub_col_elig_unit_tru() {
		return r75_crm_sub_col_elig_unit_tru;
	}
	public void setR75_crm_sub_col_elig_unit_tru(BigDecimal r75_crm_sub_col_elig_unit_tru) {
		this.r75_crm_sub_col_elig_unit_tru = r75_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR75_crm_sub_col_exp_cov() {
		return r75_crm_sub_col_exp_cov;
	}
	public void setR75_crm_sub_col_exp_cov(BigDecimal r75_crm_sub_col_exp_cov) {
		this.r75_crm_sub_col_exp_cov = r75_crm_sub_col_exp_cov;
	}
	public BigDecimal getR75_crm_sub_col_elig_exp_not_cov() {
		return r75_crm_sub_col_elig_exp_not_cov;
	}
	public void setR75_crm_sub_col_elig_exp_not_cov(BigDecimal r75_crm_sub_col_elig_exp_not_cov) {
		this.r75_crm_sub_col_elig_exp_not_cov = r75_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR75_crm_sub_rwa_ris_crm() {
		return r75_crm_sub_rwa_ris_crm;
	}
	public void setR75_crm_sub_rwa_ris_crm(BigDecimal r75_crm_sub_rwa_ris_crm) {
		this.r75_crm_sub_rwa_ris_crm = r75_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR75_crm_sub_rwa_cov_crm() {
		return r75_crm_sub_rwa_cov_crm;
	}
	public void setR75_crm_sub_rwa_cov_crm(BigDecimal r75_crm_sub_rwa_cov_crm) {
		this.r75_crm_sub_rwa_cov_crm = r75_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR75_crm_sub_rwa_org_cou() {
		return r75_crm_sub_rwa_org_cou;
	}
	public void setR75_crm_sub_rwa_org_cou(BigDecimal r75_crm_sub_rwa_org_cou) {
		this.r75_crm_sub_rwa_org_cou = r75_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR75_crm_sub_rwa_not_cov_crm() {
		return r75_crm_sub_rwa_not_cov_crm;
	}
	public void setR75_crm_sub_rwa_not_cov_crm(BigDecimal r75_crm_sub_rwa_not_cov_crm) {
		this.r75_crm_sub_rwa_not_cov_crm = r75_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR75_crm_comp_col_expo_elig() {
		return r75_crm_comp_col_expo_elig;
	}
	public void setR75_crm_comp_col_expo_elig(BigDecimal r75_crm_comp_col_expo_elig) {
		this.r75_crm_comp_col_expo_elig = r75_crm_comp_col_expo_elig;
	}
	public BigDecimal getR75_crm_comp_col_elig_expo_vol_adj() {
		return r75_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR75_crm_comp_col_elig_expo_vol_adj(BigDecimal r75_crm_comp_col_elig_expo_vol_adj) {
		this.r75_crm_comp_col_elig_expo_vol_adj = r75_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR75_crm_comp_col_elig_fin_hai() {
		return r75_crm_comp_col_elig_fin_hai;
	}
	public void setR75_crm_comp_col_elig_fin_hai(BigDecimal r75_crm_comp_col_elig_fin_hai) {
		this.r75_crm_comp_col_elig_fin_hai = r75_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR75_crm_comp_col_expo_val() {
		return r75_crm_comp_col_expo_val;
	}
	public void setR75_crm_comp_col_expo_val(BigDecimal r75_crm_comp_col_expo_val) {
		this.r75_crm_comp_col_expo_val = r75_crm_comp_col_expo_val;
	}
	public BigDecimal getR75_rwa_elig_expo_not_cov_crm() {
		return r75_rwa_elig_expo_not_cov_crm;
	}
	public void setR75_rwa_elig_expo_not_cov_crm(BigDecimal r75_rwa_elig_expo_not_cov_crm) {
		this.r75_rwa_elig_expo_not_cov_crm = r75_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR75_rwa_unsec_expo_cre_ris() {
		return r75_rwa_unsec_expo_cre_ris;
	}
	public void setR75_rwa_unsec_expo_cre_ris(BigDecimal r75_rwa_unsec_expo_cre_ris) {
		this.r75_rwa_unsec_expo_cre_ris = r75_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR75_rwa_unsec_expo() {
		return r75_rwa_unsec_expo;
	}
	public void setR75_rwa_unsec_expo(BigDecimal r75_rwa_unsec_expo) {
		this.r75_rwa_unsec_expo = r75_rwa_unsec_expo;
	}
	public BigDecimal getR75_rwa_tot_ris_wei_ass() {
		return r75_rwa_tot_ris_wei_ass;
	}
	public void setR75_rwa_tot_ris_wei_ass(BigDecimal r75_rwa_tot_ris_wei_ass) {
		this.r75_rwa_tot_ris_wei_ass = r75_rwa_tot_ris_wei_ass;
	}
	public String getR76_exposure_class() {
		return r76_exposure_class;
	}
	public void setR76_exposure_class(String r76_exposure_class) {
		this.r76_exposure_class = r76_exposure_class;
	}
	public BigDecimal getR76_expo_crm() {
		return r76_expo_crm;
	}
	public void setR76_expo_crm(BigDecimal r76_expo_crm) {
		this.r76_expo_crm = r76_expo_crm;
	}
	public BigDecimal getR76_spe_pro_expo() {
		return r76_spe_pro_expo;
	}
	public void setR76_spe_pro_expo(BigDecimal r76_spe_pro_expo) {
		this.r76_spe_pro_expo = r76_spe_pro_expo;
	}
	public BigDecimal getR76_amt_elig_sht_net() {
		return r76_amt_elig_sht_net;
	}
	public void setR76_amt_elig_sht_net(BigDecimal r76_amt_elig_sht_net) {
		this.r76_amt_elig_sht_net = r76_amt_elig_sht_net;
	}
	public BigDecimal getR76_tot_expo_net_spe() {
		return r76_tot_expo_net_spe;
	}
	public void setR76_tot_expo_net_spe(BigDecimal r76_tot_expo_net_spe) {
		this.r76_tot_expo_net_spe = r76_tot_expo_net_spe;
	}
	public BigDecimal getR76_crm_sub_elig_sub_app() {
		return r76_crm_sub_elig_sub_app;
	}
	public void setR76_crm_sub_elig_sub_app(BigDecimal r76_crm_sub_elig_sub_app) {
		this.r76_crm_sub_elig_sub_app = r76_crm_sub_elig_sub_app;
	}
	public BigDecimal getR76_crm_sub_non_col_guar() {
		return r76_crm_sub_non_col_guar;
	}
	public void setR76_crm_sub_non_col_guar(BigDecimal r76_crm_sub_non_col_guar) {
		this.r76_crm_sub_non_col_guar = r76_crm_sub_non_col_guar;
	}
	public BigDecimal getR76_crm_sub_non_col_cre_der() {
		return r76_crm_sub_non_col_cre_der;
	}
	public void setR76_crm_sub_non_col_cre_der(BigDecimal r76_crm_sub_non_col_cre_der) {
		this.r76_crm_sub_non_col_cre_der = r76_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR76_crm_sub_col_elig_cash() {
		return r76_crm_sub_col_elig_cash;
	}
	public void setR76_crm_sub_col_elig_cash(BigDecimal r76_crm_sub_col_elig_cash) {
		this.r76_crm_sub_col_elig_cash = r76_crm_sub_col_elig_cash;
	}
	public BigDecimal getR76_crm_sub_col_elig_trea_bills() {
		return r76_crm_sub_col_elig_trea_bills;
	}
	public void setR76_crm_sub_col_elig_trea_bills(BigDecimal r76_crm_sub_col_elig_trea_bills) {
		this.r76_crm_sub_col_elig_trea_bills = r76_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR76_crm_sub_col_elig_deb_sec() {
		return r76_crm_sub_col_elig_deb_sec;
	}
	public void setR76_crm_sub_col_elig_deb_sec(BigDecimal r76_crm_sub_col_elig_deb_sec) {
		this.r76_crm_sub_col_elig_deb_sec = r76_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR76_crm_sub_col_elig_equi() {
		return r76_crm_sub_col_elig_equi;
	}
	public void setR76_crm_sub_col_elig_equi(BigDecimal r76_crm_sub_col_elig_equi) {
		this.r76_crm_sub_col_elig_equi = r76_crm_sub_col_elig_equi;
	}
	public BigDecimal getR76_crm_sub_col_elig_unit_tru() {
		return r76_crm_sub_col_elig_unit_tru;
	}
	public void setR76_crm_sub_col_elig_unit_tru(BigDecimal r76_crm_sub_col_elig_unit_tru) {
		this.r76_crm_sub_col_elig_unit_tru = r76_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR76_crm_sub_col_exp_cov() {
		return r76_crm_sub_col_exp_cov;
	}
	public void setR76_crm_sub_col_exp_cov(BigDecimal r76_crm_sub_col_exp_cov) {
		this.r76_crm_sub_col_exp_cov = r76_crm_sub_col_exp_cov;
	}
	public BigDecimal getR76_crm_sub_col_elig_exp_not_cov() {
		return r76_crm_sub_col_elig_exp_not_cov;
	}
	public void setR76_crm_sub_col_elig_exp_not_cov(BigDecimal r76_crm_sub_col_elig_exp_not_cov) {
		this.r76_crm_sub_col_elig_exp_not_cov = r76_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR76_crm_sub_rwa_ris_crm() {
		return r76_crm_sub_rwa_ris_crm;
	}
	public void setR76_crm_sub_rwa_ris_crm(BigDecimal r76_crm_sub_rwa_ris_crm) {
		this.r76_crm_sub_rwa_ris_crm = r76_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR76_crm_sub_rwa_cov_crm() {
		return r76_crm_sub_rwa_cov_crm;
	}
	public void setR76_crm_sub_rwa_cov_crm(BigDecimal r76_crm_sub_rwa_cov_crm) {
		this.r76_crm_sub_rwa_cov_crm = r76_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR76_crm_sub_rwa_org_cou() {
		return r76_crm_sub_rwa_org_cou;
	}
	public void setR76_crm_sub_rwa_org_cou(BigDecimal r76_crm_sub_rwa_org_cou) {
		this.r76_crm_sub_rwa_org_cou = r76_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR76_crm_sub_rwa_not_cov_crm() {
		return r76_crm_sub_rwa_not_cov_crm;
	}
	public void setR76_crm_sub_rwa_not_cov_crm(BigDecimal r76_crm_sub_rwa_not_cov_crm) {
		this.r76_crm_sub_rwa_not_cov_crm = r76_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR76_crm_comp_col_expo_elig() {
		return r76_crm_comp_col_expo_elig;
	}
	public void setR76_crm_comp_col_expo_elig(BigDecimal r76_crm_comp_col_expo_elig) {
		this.r76_crm_comp_col_expo_elig = r76_crm_comp_col_expo_elig;
	}
	public BigDecimal getR76_crm_comp_col_elig_expo_vol_adj() {
		return r76_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR76_crm_comp_col_elig_expo_vol_adj(BigDecimal r76_crm_comp_col_elig_expo_vol_adj) {
		this.r76_crm_comp_col_elig_expo_vol_adj = r76_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR76_crm_comp_col_elig_fin_hai() {
		return r76_crm_comp_col_elig_fin_hai;
	}
	public void setR76_crm_comp_col_elig_fin_hai(BigDecimal r76_crm_comp_col_elig_fin_hai) {
		this.r76_crm_comp_col_elig_fin_hai = r76_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR76_crm_comp_col_expo_val() {
		return r76_crm_comp_col_expo_val;
	}
	public void setR76_crm_comp_col_expo_val(BigDecimal r76_crm_comp_col_expo_val) {
		this.r76_crm_comp_col_expo_val = r76_crm_comp_col_expo_val;
	}
	public BigDecimal getR76_rwa_elig_expo_not_cov_crm() {
		return r76_rwa_elig_expo_not_cov_crm;
	}
	public void setR76_rwa_elig_expo_not_cov_crm(BigDecimal r76_rwa_elig_expo_not_cov_crm) {
		this.r76_rwa_elig_expo_not_cov_crm = r76_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR76_rwa_unsec_expo_cre_ris() {
		return r76_rwa_unsec_expo_cre_ris;
	}
	public void setR76_rwa_unsec_expo_cre_ris(BigDecimal r76_rwa_unsec_expo_cre_ris) {
		this.r76_rwa_unsec_expo_cre_ris = r76_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR76_rwa_unsec_expo() {
		return r76_rwa_unsec_expo;
	}
	public void setR76_rwa_unsec_expo(BigDecimal r76_rwa_unsec_expo) {
		this.r76_rwa_unsec_expo = r76_rwa_unsec_expo;
	}
	public BigDecimal getR76_rwa_tot_ris_wei_ass() {
		return r76_rwa_tot_ris_wei_ass;
	}
	public void setR76_rwa_tot_ris_wei_ass(BigDecimal r76_rwa_tot_ris_wei_ass) {
		this.r76_rwa_tot_ris_wei_ass = r76_rwa_tot_ris_wei_ass;
	}
	public String getR77_exposure_class() {
		return r77_exposure_class;
	}
	public void setR77_exposure_class(String r77_exposure_class) {
		this.r77_exposure_class = r77_exposure_class;
	}
	public BigDecimal getR77_expo_crm() {
		return r77_expo_crm;
	}
	public void setR77_expo_crm(BigDecimal r77_expo_crm) {
		this.r77_expo_crm = r77_expo_crm;
	}
	public BigDecimal getR77_spe_pro_expo() {
		return r77_spe_pro_expo;
	}
	public void setR77_spe_pro_expo(BigDecimal r77_spe_pro_expo) {
		this.r77_spe_pro_expo = r77_spe_pro_expo;
	}
	public BigDecimal getR77_amt_elig_sht_net() {
		return r77_amt_elig_sht_net;
	}
	public void setR77_amt_elig_sht_net(BigDecimal r77_amt_elig_sht_net) {
		this.r77_amt_elig_sht_net = r77_amt_elig_sht_net;
	}
	public BigDecimal getR77_tot_expo_net_spe() {
		return r77_tot_expo_net_spe;
	}
	public void setR77_tot_expo_net_spe(BigDecimal r77_tot_expo_net_spe) {
		this.r77_tot_expo_net_spe = r77_tot_expo_net_spe;
	}
	public BigDecimal getR77_crm_sub_elig_sub_app() {
		return r77_crm_sub_elig_sub_app;
	}
	public void setR77_crm_sub_elig_sub_app(BigDecimal r77_crm_sub_elig_sub_app) {
		this.r77_crm_sub_elig_sub_app = r77_crm_sub_elig_sub_app;
	}
	public BigDecimal getR77_crm_sub_non_col_guar() {
		return r77_crm_sub_non_col_guar;
	}
	public void setR77_crm_sub_non_col_guar(BigDecimal r77_crm_sub_non_col_guar) {
		this.r77_crm_sub_non_col_guar = r77_crm_sub_non_col_guar;
	}
	public BigDecimal getR77_crm_sub_non_col_cre_der() {
		return r77_crm_sub_non_col_cre_der;
	}
	public void setR77_crm_sub_non_col_cre_der(BigDecimal r77_crm_sub_non_col_cre_der) {
		this.r77_crm_sub_non_col_cre_der = r77_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR77_crm_sub_col_elig_cash() {
		return r77_crm_sub_col_elig_cash;
	}
	public void setR77_crm_sub_col_elig_cash(BigDecimal r77_crm_sub_col_elig_cash) {
		this.r77_crm_sub_col_elig_cash = r77_crm_sub_col_elig_cash;
	}
	public BigDecimal getR77_crm_sub_col_elig_trea_bills() {
		return r77_crm_sub_col_elig_trea_bills;
	}
	public void setR77_crm_sub_col_elig_trea_bills(BigDecimal r77_crm_sub_col_elig_trea_bills) {
		this.r77_crm_sub_col_elig_trea_bills = r77_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR77_crm_sub_col_elig_deb_sec() {
		return r77_crm_sub_col_elig_deb_sec;
	}
	public void setR77_crm_sub_col_elig_deb_sec(BigDecimal r77_crm_sub_col_elig_deb_sec) {
		this.r77_crm_sub_col_elig_deb_sec = r77_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR77_crm_sub_col_elig_equi() {
		return r77_crm_sub_col_elig_equi;
	}
	public void setR77_crm_sub_col_elig_equi(BigDecimal r77_crm_sub_col_elig_equi) {
		this.r77_crm_sub_col_elig_equi = r77_crm_sub_col_elig_equi;
	}
	public BigDecimal getR77_crm_sub_col_elig_unit_tru() {
		return r77_crm_sub_col_elig_unit_tru;
	}
	public void setR77_crm_sub_col_elig_unit_tru(BigDecimal r77_crm_sub_col_elig_unit_tru) {
		this.r77_crm_sub_col_elig_unit_tru = r77_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR77_crm_sub_col_exp_cov() {
		return r77_crm_sub_col_exp_cov;
	}
	public void setR77_crm_sub_col_exp_cov(BigDecimal r77_crm_sub_col_exp_cov) {
		this.r77_crm_sub_col_exp_cov = r77_crm_sub_col_exp_cov;
	}
	public BigDecimal getR77_crm_sub_col_elig_exp_not_cov() {
		return r77_crm_sub_col_elig_exp_not_cov;
	}
	public void setR77_crm_sub_col_elig_exp_not_cov(BigDecimal r77_crm_sub_col_elig_exp_not_cov) {
		this.r77_crm_sub_col_elig_exp_not_cov = r77_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR77_crm_sub_rwa_ris_crm() {
		return r77_crm_sub_rwa_ris_crm;
	}
	public void setR77_crm_sub_rwa_ris_crm(BigDecimal r77_crm_sub_rwa_ris_crm) {
		this.r77_crm_sub_rwa_ris_crm = r77_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR77_crm_sub_rwa_cov_crm() {
		return r77_crm_sub_rwa_cov_crm;
	}
	public void setR77_crm_sub_rwa_cov_crm(BigDecimal r77_crm_sub_rwa_cov_crm) {
		this.r77_crm_sub_rwa_cov_crm = r77_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR77_crm_sub_rwa_org_cou() {
		return r77_crm_sub_rwa_org_cou;
	}
	public void setR77_crm_sub_rwa_org_cou(BigDecimal r77_crm_sub_rwa_org_cou) {
		this.r77_crm_sub_rwa_org_cou = r77_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR77_crm_sub_rwa_not_cov_crm() {
		return r77_crm_sub_rwa_not_cov_crm;
	}
	public void setR77_crm_sub_rwa_not_cov_crm(BigDecimal r77_crm_sub_rwa_not_cov_crm) {
		this.r77_crm_sub_rwa_not_cov_crm = r77_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR77_crm_comp_col_expo_elig() {
		return r77_crm_comp_col_expo_elig;
	}
	public void setR77_crm_comp_col_expo_elig(BigDecimal r77_crm_comp_col_expo_elig) {
		this.r77_crm_comp_col_expo_elig = r77_crm_comp_col_expo_elig;
	}
	public BigDecimal getR77_crm_comp_col_elig_expo_vol_adj() {
		return r77_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR77_crm_comp_col_elig_expo_vol_adj(BigDecimal r77_crm_comp_col_elig_expo_vol_adj) {
		this.r77_crm_comp_col_elig_expo_vol_adj = r77_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR77_crm_comp_col_elig_fin_hai() {
		return r77_crm_comp_col_elig_fin_hai;
	}
	public void setR77_crm_comp_col_elig_fin_hai(BigDecimal r77_crm_comp_col_elig_fin_hai) {
		this.r77_crm_comp_col_elig_fin_hai = r77_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR77_crm_comp_col_expo_val() {
		return r77_crm_comp_col_expo_val;
	}
	public void setR77_crm_comp_col_expo_val(BigDecimal r77_crm_comp_col_expo_val) {
		this.r77_crm_comp_col_expo_val = r77_crm_comp_col_expo_val;
	}
	public BigDecimal getR77_rwa_elig_expo_not_cov_crm() {
		return r77_rwa_elig_expo_not_cov_crm;
	}
	public void setR77_rwa_elig_expo_not_cov_crm(BigDecimal r77_rwa_elig_expo_not_cov_crm) {
		this.r77_rwa_elig_expo_not_cov_crm = r77_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR77_rwa_unsec_expo_cre_ris() {
		return r77_rwa_unsec_expo_cre_ris;
	}
	public void setR77_rwa_unsec_expo_cre_ris(BigDecimal r77_rwa_unsec_expo_cre_ris) {
		this.r77_rwa_unsec_expo_cre_ris = r77_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR77_rwa_unsec_expo() {
		return r77_rwa_unsec_expo;
	}
	public void setR77_rwa_unsec_expo(BigDecimal r77_rwa_unsec_expo) {
		this.r77_rwa_unsec_expo = r77_rwa_unsec_expo;
	}
	public BigDecimal getR77_rwa_tot_ris_wei_ass() {
		return r77_rwa_tot_ris_wei_ass;
	}
	public void setR77_rwa_tot_ris_wei_ass(BigDecimal r77_rwa_tot_ris_wei_ass) {
		this.r77_rwa_tot_ris_wei_ass = r77_rwa_tot_ris_wei_ass;
	}
	public String getR78_exposure_class() {
		return r78_exposure_class;
	}
	public void setR78_exposure_class(String r78_exposure_class) {
		this.r78_exposure_class = r78_exposure_class;
	}
	public BigDecimal getR78_expo_crm() {
		return r78_expo_crm;
	}
	public void setR78_expo_crm(BigDecimal r78_expo_crm) {
		this.r78_expo_crm = r78_expo_crm;
	}
	public BigDecimal getR78_spe_pro_expo() {
		return r78_spe_pro_expo;
	}
	public void setR78_spe_pro_expo(BigDecimal r78_spe_pro_expo) {
		this.r78_spe_pro_expo = r78_spe_pro_expo;
	}
	public BigDecimal getR78_amt_elig_sht_net() {
		return r78_amt_elig_sht_net;
	}
	public void setR78_amt_elig_sht_net(BigDecimal r78_amt_elig_sht_net) {
		this.r78_amt_elig_sht_net = r78_amt_elig_sht_net;
	}
	public BigDecimal getR78_tot_expo_net_spe() {
		return r78_tot_expo_net_spe;
	}
	public void setR78_tot_expo_net_spe(BigDecimal r78_tot_expo_net_spe) {
		this.r78_tot_expo_net_spe = r78_tot_expo_net_spe;
	}
	public BigDecimal getR78_crm_sub_elig_sub_app() {
		return r78_crm_sub_elig_sub_app;
	}
	public void setR78_crm_sub_elig_sub_app(BigDecimal r78_crm_sub_elig_sub_app) {
		this.r78_crm_sub_elig_sub_app = r78_crm_sub_elig_sub_app;
	}
	public BigDecimal getR78_crm_sub_non_col_guar() {
		return r78_crm_sub_non_col_guar;
	}
	public void setR78_crm_sub_non_col_guar(BigDecimal r78_crm_sub_non_col_guar) {
		this.r78_crm_sub_non_col_guar = r78_crm_sub_non_col_guar;
	}
	public BigDecimal getR78_crm_sub_non_col_cre_der() {
		return r78_crm_sub_non_col_cre_der;
	}
	public void setR78_crm_sub_non_col_cre_der(BigDecimal r78_crm_sub_non_col_cre_der) {
		this.r78_crm_sub_non_col_cre_der = r78_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR78_crm_sub_col_elig_cash() {
		return r78_crm_sub_col_elig_cash;
	}
	public void setR78_crm_sub_col_elig_cash(BigDecimal r78_crm_sub_col_elig_cash) {
		this.r78_crm_sub_col_elig_cash = r78_crm_sub_col_elig_cash;
	}
	public BigDecimal getR78_crm_sub_col_elig_trea_bills() {
		return r78_crm_sub_col_elig_trea_bills;
	}
	public void setR78_crm_sub_col_elig_trea_bills(BigDecimal r78_crm_sub_col_elig_trea_bills) {
		this.r78_crm_sub_col_elig_trea_bills = r78_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR78_crm_sub_col_elig_deb_sec() {
		return r78_crm_sub_col_elig_deb_sec;
	}
	public void setR78_crm_sub_col_elig_deb_sec(BigDecimal r78_crm_sub_col_elig_deb_sec) {
		this.r78_crm_sub_col_elig_deb_sec = r78_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR78_crm_sub_col_elig_equi() {
		return r78_crm_sub_col_elig_equi;
	}
	public void setR78_crm_sub_col_elig_equi(BigDecimal r78_crm_sub_col_elig_equi) {
		this.r78_crm_sub_col_elig_equi = r78_crm_sub_col_elig_equi;
	}
	public BigDecimal getR78_crm_sub_col_elig_unit_tru() {
		return r78_crm_sub_col_elig_unit_tru;
	}
	public void setR78_crm_sub_col_elig_unit_tru(BigDecimal r78_crm_sub_col_elig_unit_tru) {
		this.r78_crm_sub_col_elig_unit_tru = r78_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR78_crm_sub_col_exp_cov() {
		return r78_crm_sub_col_exp_cov;
	}
	public void setR78_crm_sub_col_exp_cov(BigDecimal r78_crm_sub_col_exp_cov) {
		this.r78_crm_sub_col_exp_cov = r78_crm_sub_col_exp_cov;
	}
	public BigDecimal getR78_crm_sub_col_elig_exp_not_cov() {
		return r78_crm_sub_col_elig_exp_not_cov;
	}
	public void setR78_crm_sub_col_elig_exp_not_cov(BigDecimal r78_crm_sub_col_elig_exp_not_cov) {
		this.r78_crm_sub_col_elig_exp_not_cov = r78_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR78_crm_sub_rwa_ris_crm() {
		return r78_crm_sub_rwa_ris_crm;
	}
	public void setR78_crm_sub_rwa_ris_crm(BigDecimal r78_crm_sub_rwa_ris_crm) {
		this.r78_crm_sub_rwa_ris_crm = r78_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR78_crm_sub_rwa_cov_crm() {
		return r78_crm_sub_rwa_cov_crm;
	}
	public void setR78_crm_sub_rwa_cov_crm(BigDecimal r78_crm_sub_rwa_cov_crm) {
		this.r78_crm_sub_rwa_cov_crm = r78_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR78_crm_sub_rwa_org_cou() {
		return r78_crm_sub_rwa_org_cou;
	}
	public void setR78_crm_sub_rwa_org_cou(BigDecimal r78_crm_sub_rwa_org_cou) {
		this.r78_crm_sub_rwa_org_cou = r78_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR78_crm_sub_rwa_not_cov_crm() {
		return r78_crm_sub_rwa_not_cov_crm;
	}
	public void setR78_crm_sub_rwa_not_cov_crm(BigDecimal r78_crm_sub_rwa_not_cov_crm) {
		this.r78_crm_sub_rwa_not_cov_crm = r78_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR78_crm_comp_col_expo_elig() {
		return r78_crm_comp_col_expo_elig;
	}
	public void setR78_crm_comp_col_expo_elig(BigDecimal r78_crm_comp_col_expo_elig) {
		this.r78_crm_comp_col_expo_elig = r78_crm_comp_col_expo_elig;
	}
	public BigDecimal getR78_crm_comp_col_elig_expo_vol_adj() {
		return r78_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR78_crm_comp_col_elig_expo_vol_adj(BigDecimal r78_crm_comp_col_elig_expo_vol_adj) {
		this.r78_crm_comp_col_elig_expo_vol_adj = r78_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR78_crm_comp_col_elig_fin_hai() {
		return r78_crm_comp_col_elig_fin_hai;
	}
	public void setR78_crm_comp_col_elig_fin_hai(BigDecimal r78_crm_comp_col_elig_fin_hai) {
		this.r78_crm_comp_col_elig_fin_hai = r78_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR78_crm_comp_col_expo_val() {
		return r78_crm_comp_col_expo_val;
	}
	public void setR78_crm_comp_col_expo_val(BigDecimal r78_crm_comp_col_expo_val) {
		this.r78_crm_comp_col_expo_val = r78_crm_comp_col_expo_val;
	}
	public BigDecimal getR78_rwa_elig_expo_not_cov_crm() {
		return r78_rwa_elig_expo_not_cov_crm;
	}
	public void setR78_rwa_elig_expo_not_cov_crm(BigDecimal r78_rwa_elig_expo_not_cov_crm) {
		this.r78_rwa_elig_expo_not_cov_crm = r78_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR78_rwa_unsec_expo_cre_ris() {
		return r78_rwa_unsec_expo_cre_ris;
	}
	public void setR78_rwa_unsec_expo_cre_ris(BigDecimal r78_rwa_unsec_expo_cre_ris) {
		this.r78_rwa_unsec_expo_cre_ris = r78_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR78_rwa_unsec_expo() {
		return r78_rwa_unsec_expo;
	}
	public void setR78_rwa_unsec_expo(BigDecimal r78_rwa_unsec_expo) {
		this.r78_rwa_unsec_expo = r78_rwa_unsec_expo;
	}
	public BigDecimal getR78_rwa_tot_ris_wei_ass() {
		return r78_rwa_tot_ris_wei_ass;
	}
	public void setR78_rwa_tot_ris_wei_ass(BigDecimal r78_rwa_tot_ris_wei_ass) {
		this.r78_rwa_tot_ris_wei_ass = r78_rwa_tot_ris_wei_ass;
	}
	public String getR79_exposure_class() {
		return r79_exposure_class;
	}
	public void setR79_exposure_class(String r79_exposure_class) {
		this.r79_exposure_class = r79_exposure_class;
	}
	public BigDecimal getR79_expo_crm() {
		return r79_expo_crm;
	}
	public void setR79_expo_crm(BigDecimal r79_expo_crm) {
		this.r79_expo_crm = r79_expo_crm;
	}
	public BigDecimal getR79_spe_pro_expo() {
		return r79_spe_pro_expo;
	}
	public void setR79_spe_pro_expo(BigDecimal r79_spe_pro_expo) {
		this.r79_spe_pro_expo = r79_spe_pro_expo;
	}
	public BigDecimal getR79_amt_elig_sht_net() {
		return r79_amt_elig_sht_net;
	}
	public void setR79_amt_elig_sht_net(BigDecimal r79_amt_elig_sht_net) {
		this.r79_amt_elig_sht_net = r79_amt_elig_sht_net;
	}
	public BigDecimal getR79_tot_expo_net_spe() {
		return r79_tot_expo_net_spe;
	}
	public void setR79_tot_expo_net_spe(BigDecimal r79_tot_expo_net_spe) {
		this.r79_tot_expo_net_spe = r79_tot_expo_net_spe;
	}
	public BigDecimal getR79_crm_sub_elig_sub_app() {
		return r79_crm_sub_elig_sub_app;
	}
	public void setR79_crm_sub_elig_sub_app(BigDecimal r79_crm_sub_elig_sub_app) {
		this.r79_crm_sub_elig_sub_app = r79_crm_sub_elig_sub_app;
	}
	public BigDecimal getR79_crm_sub_non_col_guar() {
		return r79_crm_sub_non_col_guar;
	}
	public void setR79_crm_sub_non_col_guar(BigDecimal r79_crm_sub_non_col_guar) {
		this.r79_crm_sub_non_col_guar = r79_crm_sub_non_col_guar;
	}
	public BigDecimal getR79_crm_sub_non_col_cre_der() {
		return r79_crm_sub_non_col_cre_der;
	}
	public void setR79_crm_sub_non_col_cre_der(BigDecimal r79_crm_sub_non_col_cre_der) {
		this.r79_crm_sub_non_col_cre_der = r79_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR79_crm_sub_col_elig_cash() {
		return r79_crm_sub_col_elig_cash;
	}
	public void setR79_crm_sub_col_elig_cash(BigDecimal r79_crm_sub_col_elig_cash) {
		this.r79_crm_sub_col_elig_cash = r79_crm_sub_col_elig_cash;
	}
	public BigDecimal getR79_crm_sub_col_elig_trea_bills() {
		return r79_crm_sub_col_elig_trea_bills;
	}
	public void setR79_crm_sub_col_elig_trea_bills(BigDecimal r79_crm_sub_col_elig_trea_bills) {
		this.r79_crm_sub_col_elig_trea_bills = r79_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR79_crm_sub_col_elig_deb_sec() {
		return r79_crm_sub_col_elig_deb_sec;
	}
	public void setR79_crm_sub_col_elig_deb_sec(BigDecimal r79_crm_sub_col_elig_deb_sec) {
		this.r79_crm_sub_col_elig_deb_sec = r79_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR79_crm_sub_col_elig_equi() {
		return r79_crm_sub_col_elig_equi;
	}
	public void setR79_crm_sub_col_elig_equi(BigDecimal r79_crm_sub_col_elig_equi) {
		this.r79_crm_sub_col_elig_equi = r79_crm_sub_col_elig_equi;
	}
	public BigDecimal getR79_crm_sub_col_elig_unit_tru() {
		return r79_crm_sub_col_elig_unit_tru;
	}
	public void setR79_crm_sub_col_elig_unit_tru(BigDecimal r79_crm_sub_col_elig_unit_tru) {
		this.r79_crm_sub_col_elig_unit_tru = r79_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR79_crm_sub_col_exp_cov() {
		return r79_crm_sub_col_exp_cov;
	}
	public void setR79_crm_sub_col_exp_cov(BigDecimal r79_crm_sub_col_exp_cov) {
		this.r79_crm_sub_col_exp_cov = r79_crm_sub_col_exp_cov;
	}
	public BigDecimal getR79_crm_sub_col_elig_exp_not_cov() {
		return r79_crm_sub_col_elig_exp_not_cov;
	}
	public void setR79_crm_sub_col_elig_exp_not_cov(BigDecimal r79_crm_sub_col_elig_exp_not_cov) {
		this.r79_crm_sub_col_elig_exp_not_cov = r79_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR79_crm_sub_rwa_ris_crm() {
		return r79_crm_sub_rwa_ris_crm;
	}
	public void setR79_crm_sub_rwa_ris_crm(BigDecimal r79_crm_sub_rwa_ris_crm) {
		this.r79_crm_sub_rwa_ris_crm = r79_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR79_crm_sub_rwa_cov_crm() {
		return r79_crm_sub_rwa_cov_crm;
	}
	public void setR79_crm_sub_rwa_cov_crm(BigDecimal r79_crm_sub_rwa_cov_crm) {
		this.r79_crm_sub_rwa_cov_crm = r79_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR79_crm_sub_rwa_org_cou() {
		return r79_crm_sub_rwa_org_cou;
	}
	public void setR79_crm_sub_rwa_org_cou(BigDecimal r79_crm_sub_rwa_org_cou) {
		this.r79_crm_sub_rwa_org_cou = r79_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR79_crm_sub_rwa_not_cov_crm() {
		return r79_crm_sub_rwa_not_cov_crm;
	}
	public void setR79_crm_sub_rwa_not_cov_crm(BigDecimal r79_crm_sub_rwa_not_cov_crm) {
		this.r79_crm_sub_rwa_not_cov_crm = r79_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR79_crm_comp_col_expo_elig() {
		return r79_crm_comp_col_expo_elig;
	}
	public void setR79_crm_comp_col_expo_elig(BigDecimal r79_crm_comp_col_expo_elig) {
		this.r79_crm_comp_col_expo_elig = r79_crm_comp_col_expo_elig;
	}
	public BigDecimal getR79_crm_comp_col_elig_expo_vol_adj() {
		return r79_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR79_crm_comp_col_elig_expo_vol_adj(BigDecimal r79_crm_comp_col_elig_expo_vol_adj) {
		this.r79_crm_comp_col_elig_expo_vol_adj = r79_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR79_crm_comp_col_elig_fin_hai() {
		return r79_crm_comp_col_elig_fin_hai;
	}
	public void setR79_crm_comp_col_elig_fin_hai(BigDecimal r79_crm_comp_col_elig_fin_hai) {
		this.r79_crm_comp_col_elig_fin_hai = r79_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR79_crm_comp_col_expo_val() {
		return r79_crm_comp_col_expo_val;
	}
	public void setR79_crm_comp_col_expo_val(BigDecimal r79_crm_comp_col_expo_val) {
		this.r79_crm_comp_col_expo_val = r79_crm_comp_col_expo_val;
	}
	public BigDecimal getR79_rwa_elig_expo_not_cov_crm() {
		return r79_rwa_elig_expo_not_cov_crm;
	}
	public void setR79_rwa_elig_expo_not_cov_crm(BigDecimal r79_rwa_elig_expo_not_cov_crm) {
		this.r79_rwa_elig_expo_not_cov_crm = r79_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR79_rwa_unsec_expo_cre_ris() {
		return r79_rwa_unsec_expo_cre_ris;
	}
	public void setR79_rwa_unsec_expo_cre_ris(BigDecimal r79_rwa_unsec_expo_cre_ris) {
		this.r79_rwa_unsec_expo_cre_ris = r79_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR79_rwa_unsec_expo() {
		return r79_rwa_unsec_expo;
	}
	public void setR79_rwa_unsec_expo(BigDecimal r79_rwa_unsec_expo) {
		this.r79_rwa_unsec_expo = r79_rwa_unsec_expo;
	}
	public BigDecimal getR79_rwa_tot_ris_wei_ass() {
		return r79_rwa_tot_ris_wei_ass;
	}
	public void setR79_rwa_tot_ris_wei_ass(BigDecimal r79_rwa_tot_ris_wei_ass) {
		this.r79_rwa_tot_ris_wei_ass = r79_rwa_tot_ris_wei_ass;
	}
	public String getR80_exposure_class() {
		return r80_exposure_class;
	}
	public void setR80_exposure_class(String r80_exposure_class) {
		this.r80_exposure_class = r80_exposure_class;
	}
	public BigDecimal getR80_expo_crm() {
		return r80_expo_crm;
	}
	public void setR80_expo_crm(BigDecimal r80_expo_crm) {
		this.r80_expo_crm = r80_expo_crm;
	}
	public BigDecimal getR80_spe_pro_expo() {
		return r80_spe_pro_expo;
	}
	public void setR80_spe_pro_expo(BigDecimal r80_spe_pro_expo) {
		this.r80_spe_pro_expo = r80_spe_pro_expo;
	}
	public BigDecimal getR80_amt_elig_sht_net() {
		return r80_amt_elig_sht_net;
	}
	public void setR80_amt_elig_sht_net(BigDecimal r80_amt_elig_sht_net) {
		this.r80_amt_elig_sht_net = r80_amt_elig_sht_net;
	}
	public BigDecimal getR80_tot_expo_net_spe() {
		return r80_tot_expo_net_spe;
	}
	public void setR80_tot_expo_net_spe(BigDecimal r80_tot_expo_net_spe) {
		this.r80_tot_expo_net_spe = r80_tot_expo_net_spe;
	}
	public BigDecimal getR80_crm_sub_elig_sub_app() {
		return r80_crm_sub_elig_sub_app;
	}
	public void setR80_crm_sub_elig_sub_app(BigDecimal r80_crm_sub_elig_sub_app) {
		this.r80_crm_sub_elig_sub_app = r80_crm_sub_elig_sub_app;
	}
	public BigDecimal getR80_crm_sub_non_col_guar() {
		return r80_crm_sub_non_col_guar;
	}
	public void setR80_crm_sub_non_col_guar(BigDecimal r80_crm_sub_non_col_guar) {
		this.r80_crm_sub_non_col_guar = r80_crm_sub_non_col_guar;
	}
	public BigDecimal getR80_crm_sub_non_col_cre_der() {
		return r80_crm_sub_non_col_cre_der;
	}
	public void setR80_crm_sub_non_col_cre_der(BigDecimal r80_crm_sub_non_col_cre_der) {
		this.r80_crm_sub_non_col_cre_der = r80_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR80_crm_sub_col_elig_cash() {
		return r80_crm_sub_col_elig_cash;
	}
	public void setR80_crm_sub_col_elig_cash(BigDecimal r80_crm_sub_col_elig_cash) {
		this.r80_crm_sub_col_elig_cash = r80_crm_sub_col_elig_cash;
	}
	public BigDecimal getR80_crm_sub_col_elig_trea_bills() {
		return r80_crm_sub_col_elig_trea_bills;
	}
	public void setR80_crm_sub_col_elig_trea_bills(BigDecimal r80_crm_sub_col_elig_trea_bills) {
		this.r80_crm_sub_col_elig_trea_bills = r80_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR80_crm_sub_col_elig_deb_sec() {
		return r80_crm_sub_col_elig_deb_sec;
	}
	public void setR80_crm_sub_col_elig_deb_sec(BigDecimal r80_crm_sub_col_elig_deb_sec) {
		this.r80_crm_sub_col_elig_deb_sec = r80_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR80_crm_sub_col_elig_equi() {
		return r80_crm_sub_col_elig_equi;
	}
	public void setR80_crm_sub_col_elig_equi(BigDecimal r80_crm_sub_col_elig_equi) {
		this.r80_crm_sub_col_elig_equi = r80_crm_sub_col_elig_equi;
	}
	public BigDecimal getR80_crm_sub_col_elig_unit_tru() {
		return r80_crm_sub_col_elig_unit_tru;
	}
	public void setR80_crm_sub_col_elig_unit_tru(BigDecimal r80_crm_sub_col_elig_unit_tru) {
		this.r80_crm_sub_col_elig_unit_tru = r80_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR80_crm_sub_col_exp_cov() {
		return r80_crm_sub_col_exp_cov;
	}
	public void setR80_crm_sub_col_exp_cov(BigDecimal r80_crm_sub_col_exp_cov) {
		this.r80_crm_sub_col_exp_cov = r80_crm_sub_col_exp_cov;
	}
	public BigDecimal getR80_crm_sub_col_elig_exp_not_cov() {
		return r80_crm_sub_col_elig_exp_not_cov;
	}
	public void setR80_crm_sub_col_elig_exp_not_cov(BigDecimal r80_crm_sub_col_elig_exp_not_cov) {
		this.r80_crm_sub_col_elig_exp_not_cov = r80_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR80_crm_sub_rwa_ris_crm() {
		return r80_crm_sub_rwa_ris_crm;
	}
	public void setR80_crm_sub_rwa_ris_crm(BigDecimal r80_crm_sub_rwa_ris_crm) {
		this.r80_crm_sub_rwa_ris_crm = r80_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR80_crm_sub_rwa_cov_crm() {
		return r80_crm_sub_rwa_cov_crm;
	}
	public void setR80_crm_sub_rwa_cov_crm(BigDecimal r80_crm_sub_rwa_cov_crm) {
		this.r80_crm_sub_rwa_cov_crm = r80_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR80_crm_sub_rwa_org_cou() {
		return r80_crm_sub_rwa_org_cou;
	}
	public void setR80_crm_sub_rwa_org_cou(BigDecimal r80_crm_sub_rwa_org_cou) {
		this.r80_crm_sub_rwa_org_cou = r80_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR80_crm_sub_rwa_not_cov_crm() {
		return r80_crm_sub_rwa_not_cov_crm;
	}
	public void setR80_crm_sub_rwa_not_cov_crm(BigDecimal r80_crm_sub_rwa_not_cov_crm) {
		this.r80_crm_sub_rwa_not_cov_crm = r80_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR80_crm_comp_col_expo_elig() {
		return r80_crm_comp_col_expo_elig;
	}
	public void setR80_crm_comp_col_expo_elig(BigDecimal r80_crm_comp_col_expo_elig) {
		this.r80_crm_comp_col_expo_elig = r80_crm_comp_col_expo_elig;
	}
	public BigDecimal getR80_crm_comp_col_elig_expo_vol_adj() {
		return r80_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR80_crm_comp_col_elig_expo_vol_adj(BigDecimal r80_crm_comp_col_elig_expo_vol_adj) {
		this.r80_crm_comp_col_elig_expo_vol_adj = r80_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR80_crm_comp_col_elig_fin_hai() {
		return r80_crm_comp_col_elig_fin_hai;
	}
	public void setR80_crm_comp_col_elig_fin_hai(BigDecimal r80_crm_comp_col_elig_fin_hai) {
		this.r80_crm_comp_col_elig_fin_hai = r80_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR80_crm_comp_col_expo_val() {
		return r80_crm_comp_col_expo_val;
	}
	public void setR80_crm_comp_col_expo_val(BigDecimal r80_crm_comp_col_expo_val) {
		this.r80_crm_comp_col_expo_val = r80_crm_comp_col_expo_val;
	}
	public BigDecimal getR80_rwa_elig_expo_not_cov_crm() {
		return r80_rwa_elig_expo_not_cov_crm;
	}
	public void setR80_rwa_elig_expo_not_cov_crm(BigDecimal r80_rwa_elig_expo_not_cov_crm) {
		this.r80_rwa_elig_expo_not_cov_crm = r80_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR80_rwa_unsec_expo_cre_ris() {
		return r80_rwa_unsec_expo_cre_ris;
	}
	public void setR80_rwa_unsec_expo_cre_ris(BigDecimal r80_rwa_unsec_expo_cre_ris) {
		this.r80_rwa_unsec_expo_cre_ris = r80_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR80_rwa_unsec_expo() {
		return r80_rwa_unsec_expo;
	}
	public void setR80_rwa_unsec_expo(BigDecimal r80_rwa_unsec_expo) {
		this.r80_rwa_unsec_expo = r80_rwa_unsec_expo;
	}
	public BigDecimal getR80_rwa_tot_ris_wei_ass() {
		return r80_rwa_tot_ris_wei_ass;
	}
	public void setR80_rwa_tot_ris_wei_ass(BigDecimal r80_rwa_tot_ris_wei_ass) {
		this.r80_rwa_tot_ris_wei_ass = r80_rwa_tot_ris_wei_ass;
	}
	public String getR81_exposure_class() {
		return r81_exposure_class;
	}
	public void setR81_exposure_class(String r81_exposure_class) {
		this.r81_exposure_class = r81_exposure_class;
	}
	public BigDecimal getR81_expo_crm() {
		return r81_expo_crm;
	}
	public void setR81_expo_crm(BigDecimal r81_expo_crm) {
		this.r81_expo_crm = r81_expo_crm;
	}
	public BigDecimal getR81_spe_pro_expo() {
		return r81_spe_pro_expo;
	}
	public void setR81_spe_pro_expo(BigDecimal r81_spe_pro_expo) {
		this.r81_spe_pro_expo = r81_spe_pro_expo;
	}
	public BigDecimal getR81_amt_elig_sht_net() {
		return r81_amt_elig_sht_net;
	}
	public void setR81_amt_elig_sht_net(BigDecimal r81_amt_elig_sht_net) {
		this.r81_amt_elig_sht_net = r81_amt_elig_sht_net;
	}
	public BigDecimal getR81_tot_expo_net_spe() {
		return r81_tot_expo_net_spe;
	}
	public void setR81_tot_expo_net_spe(BigDecimal r81_tot_expo_net_spe) {
		this.r81_tot_expo_net_spe = r81_tot_expo_net_spe;
	}
	public BigDecimal getR81_crm_sub_elig_sub_app() {
		return r81_crm_sub_elig_sub_app;
	}
	public void setR81_crm_sub_elig_sub_app(BigDecimal r81_crm_sub_elig_sub_app) {
		this.r81_crm_sub_elig_sub_app = r81_crm_sub_elig_sub_app;
	}
	public BigDecimal getR81_crm_sub_non_col_guar() {
		return r81_crm_sub_non_col_guar;
	}
	public void setR81_crm_sub_non_col_guar(BigDecimal r81_crm_sub_non_col_guar) {
		this.r81_crm_sub_non_col_guar = r81_crm_sub_non_col_guar;
	}
	public BigDecimal getR81_crm_sub_non_col_cre_der() {
		return r81_crm_sub_non_col_cre_der;
	}
	public void setR81_crm_sub_non_col_cre_der(BigDecimal r81_crm_sub_non_col_cre_der) {
		this.r81_crm_sub_non_col_cre_der = r81_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR81_crm_sub_col_elig_cash() {
		return r81_crm_sub_col_elig_cash;
	}
	public void setR81_crm_sub_col_elig_cash(BigDecimal r81_crm_sub_col_elig_cash) {
		this.r81_crm_sub_col_elig_cash = r81_crm_sub_col_elig_cash;
	}
	public BigDecimal getR81_crm_sub_col_elig_trea_bills() {
		return r81_crm_sub_col_elig_trea_bills;
	}
	public void setR81_crm_sub_col_elig_trea_bills(BigDecimal r81_crm_sub_col_elig_trea_bills) {
		this.r81_crm_sub_col_elig_trea_bills = r81_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR81_crm_sub_col_elig_deb_sec() {
		return r81_crm_sub_col_elig_deb_sec;
	}
	public void setR81_crm_sub_col_elig_deb_sec(BigDecimal r81_crm_sub_col_elig_deb_sec) {
		this.r81_crm_sub_col_elig_deb_sec = r81_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR81_crm_sub_col_elig_equi() {
		return r81_crm_sub_col_elig_equi;
	}
	public void setR81_crm_sub_col_elig_equi(BigDecimal r81_crm_sub_col_elig_equi) {
		this.r81_crm_sub_col_elig_equi = r81_crm_sub_col_elig_equi;
	}
	public BigDecimal getR81_crm_sub_col_elig_unit_tru() {
		return r81_crm_sub_col_elig_unit_tru;
	}
	public void setR81_crm_sub_col_elig_unit_tru(BigDecimal r81_crm_sub_col_elig_unit_tru) {
		this.r81_crm_sub_col_elig_unit_tru = r81_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR81_crm_sub_col_exp_cov() {
		return r81_crm_sub_col_exp_cov;
	}
	public void setR81_crm_sub_col_exp_cov(BigDecimal r81_crm_sub_col_exp_cov) {
		this.r81_crm_sub_col_exp_cov = r81_crm_sub_col_exp_cov;
	}
	public BigDecimal getR81_crm_sub_col_elig_exp_not_cov() {
		return r81_crm_sub_col_elig_exp_not_cov;
	}
	public void setR81_crm_sub_col_elig_exp_not_cov(BigDecimal r81_crm_sub_col_elig_exp_not_cov) {
		this.r81_crm_sub_col_elig_exp_not_cov = r81_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR81_crm_sub_rwa_ris_crm() {
		return r81_crm_sub_rwa_ris_crm;
	}
	public void setR81_crm_sub_rwa_ris_crm(BigDecimal r81_crm_sub_rwa_ris_crm) {
		this.r81_crm_sub_rwa_ris_crm = r81_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR81_crm_sub_rwa_cov_crm() {
		return r81_crm_sub_rwa_cov_crm;
	}
	public void setR81_crm_sub_rwa_cov_crm(BigDecimal r81_crm_sub_rwa_cov_crm) {
		this.r81_crm_sub_rwa_cov_crm = r81_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR81_crm_sub_rwa_org_cou() {
		return r81_crm_sub_rwa_org_cou;
	}
	public void setR81_crm_sub_rwa_org_cou(BigDecimal r81_crm_sub_rwa_org_cou) {
		this.r81_crm_sub_rwa_org_cou = r81_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR81_crm_sub_rwa_not_cov_crm() {
		return r81_crm_sub_rwa_not_cov_crm;
	}
	public void setR81_crm_sub_rwa_not_cov_crm(BigDecimal r81_crm_sub_rwa_not_cov_crm) {
		this.r81_crm_sub_rwa_not_cov_crm = r81_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR81_crm_comp_col_expo_elig() {
		return r81_crm_comp_col_expo_elig;
	}
	public void setR81_crm_comp_col_expo_elig(BigDecimal r81_crm_comp_col_expo_elig) {
		this.r81_crm_comp_col_expo_elig = r81_crm_comp_col_expo_elig;
	}
	public BigDecimal getR81_crm_comp_col_elig_expo_vol_adj() {
		return r81_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR81_crm_comp_col_elig_expo_vol_adj(BigDecimal r81_crm_comp_col_elig_expo_vol_adj) {
		this.r81_crm_comp_col_elig_expo_vol_adj = r81_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR81_crm_comp_col_elig_fin_hai() {
		return r81_crm_comp_col_elig_fin_hai;
	}
	public void setR81_crm_comp_col_elig_fin_hai(BigDecimal r81_crm_comp_col_elig_fin_hai) {
		this.r81_crm_comp_col_elig_fin_hai = r81_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR81_crm_comp_col_expo_val() {
		return r81_crm_comp_col_expo_val;
	}
	public void setR81_crm_comp_col_expo_val(BigDecimal r81_crm_comp_col_expo_val) {
		this.r81_crm_comp_col_expo_val = r81_crm_comp_col_expo_val;
	}
	public BigDecimal getR81_rwa_elig_expo_not_cov_crm() {
		return r81_rwa_elig_expo_not_cov_crm;
	}
	public void setR81_rwa_elig_expo_not_cov_crm(BigDecimal r81_rwa_elig_expo_not_cov_crm) {
		this.r81_rwa_elig_expo_not_cov_crm = r81_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR81_rwa_unsec_expo_cre_ris() {
		return r81_rwa_unsec_expo_cre_ris;
	}
	public void setR81_rwa_unsec_expo_cre_ris(BigDecimal r81_rwa_unsec_expo_cre_ris) {
		this.r81_rwa_unsec_expo_cre_ris = r81_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR81_rwa_unsec_expo() {
		return r81_rwa_unsec_expo;
	}
	public void setR81_rwa_unsec_expo(BigDecimal r81_rwa_unsec_expo) {
		this.r81_rwa_unsec_expo = r81_rwa_unsec_expo;
	}
	public BigDecimal getR81_rwa_tot_ris_wei_ass() {
		return r81_rwa_tot_ris_wei_ass;
	}
	public void setR81_rwa_tot_ris_wei_ass(BigDecimal r81_rwa_tot_ris_wei_ass) {
		this.r81_rwa_tot_ris_wei_ass = r81_rwa_tot_ris_wei_ass;
	}
	public String getR82_exposure_class() {
		return r82_exposure_class;
	}
	public void setR82_exposure_class(String r82_exposure_class) {
		this.r82_exposure_class = r82_exposure_class;
	}
	public BigDecimal getR82_expo_crm() {
		return r82_expo_crm;
	}
	public void setR82_expo_crm(BigDecimal r82_expo_crm) {
		this.r82_expo_crm = r82_expo_crm;
	}
	public BigDecimal getR82_spe_pro_expo() {
		return r82_spe_pro_expo;
	}
	public void setR82_spe_pro_expo(BigDecimal r82_spe_pro_expo) {
		this.r82_spe_pro_expo = r82_spe_pro_expo;
	}
	public BigDecimal getR82_amt_elig_sht_net() {
		return r82_amt_elig_sht_net;
	}
	public void setR82_amt_elig_sht_net(BigDecimal r82_amt_elig_sht_net) {
		this.r82_amt_elig_sht_net = r82_amt_elig_sht_net;
	}
	public BigDecimal getR82_tot_expo_net_spe() {
		return r82_tot_expo_net_spe;
	}
	public void setR82_tot_expo_net_spe(BigDecimal r82_tot_expo_net_spe) {
		this.r82_tot_expo_net_spe = r82_tot_expo_net_spe;
	}
	public BigDecimal getR82_crm_sub_elig_sub_app() {
		return r82_crm_sub_elig_sub_app;
	}
	public void setR82_crm_sub_elig_sub_app(BigDecimal r82_crm_sub_elig_sub_app) {
		this.r82_crm_sub_elig_sub_app = r82_crm_sub_elig_sub_app;
	}
	public BigDecimal getR82_crm_sub_non_col_guar() {
		return r82_crm_sub_non_col_guar;
	}
	public void setR82_crm_sub_non_col_guar(BigDecimal r82_crm_sub_non_col_guar) {
		this.r82_crm_sub_non_col_guar = r82_crm_sub_non_col_guar;
	}
	public BigDecimal getR82_crm_sub_non_col_cre_der() {
		return r82_crm_sub_non_col_cre_der;
	}
	public void setR82_crm_sub_non_col_cre_der(BigDecimal r82_crm_sub_non_col_cre_der) {
		this.r82_crm_sub_non_col_cre_der = r82_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR82_crm_sub_col_elig_cash() {
		return r82_crm_sub_col_elig_cash;
	}
	public void setR82_crm_sub_col_elig_cash(BigDecimal r82_crm_sub_col_elig_cash) {
		this.r82_crm_sub_col_elig_cash = r82_crm_sub_col_elig_cash;
	}
	public BigDecimal getR82_crm_sub_col_elig_trea_bills() {
		return r82_crm_sub_col_elig_trea_bills;
	}
	public void setR82_crm_sub_col_elig_trea_bills(BigDecimal r82_crm_sub_col_elig_trea_bills) {
		this.r82_crm_sub_col_elig_trea_bills = r82_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR82_crm_sub_col_elig_deb_sec() {
		return r82_crm_sub_col_elig_deb_sec;
	}
	public void setR82_crm_sub_col_elig_deb_sec(BigDecimal r82_crm_sub_col_elig_deb_sec) {
		this.r82_crm_sub_col_elig_deb_sec = r82_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR82_crm_sub_col_elig_equi() {
		return r82_crm_sub_col_elig_equi;
	}
	public void setR82_crm_sub_col_elig_equi(BigDecimal r82_crm_sub_col_elig_equi) {
		this.r82_crm_sub_col_elig_equi = r82_crm_sub_col_elig_equi;
	}
	public BigDecimal getR82_crm_sub_col_elig_unit_tru() {
		return r82_crm_sub_col_elig_unit_tru;
	}
	public void setR82_crm_sub_col_elig_unit_tru(BigDecimal r82_crm_sub_col_elig_unit_tru) {
		this.r82_crm_sub_col_elig_unit_tru = r82_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR82_crm_sub_col_exp_cov() {
		return r82_crm_sub_col_exp_cov;
	}
	public void setR82_crm_sub_col_exp_cov(BigDecimal r82_crm_sub_col_exp_cov) {
		this.r82_crm_sub_col_exp_cov = r82_crm_sub_col_exp_cov;
	}
	public BigDecimal getR82_crm_sub_col_elig_exp_not_cov() {
		return r82_crm_sub_col_elig_exp_not_cov;
	}
	public void setR82_crm_sub_col_elig_exp_not_cov(BigDecimal r82_crm_sub_col_elig_exp_not_cov) {
		this.r82_crm_sub_col_elig_exp_not_cov = r82_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR82_crm_sub_rwa_ris_crm() {
		return r82_crm_sub_rwa_ris_crm;
	}
	public void setR82_crm_sub_rwa_ris_crm(BigDecimal r82_crm_sub_rwa_ris_crm) {
		this.r82_crm_sub_rwa_ris_crm = r82_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR82_crm_sub_rwa_cov_crm() {
		return r82_crm_sub_rwa_cov_crm;
	}
	public void setR82_crm_sub_rwa_cov_crm(BigDecimal r82_crm_sub_rwa_cov_crm) {
		this.r82_crm_sub_rwa_cov_crm = r82_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR82_crm_sub_rwa_org_cou() {
		return r82_crm_sub_rwa_org_cou;
	}
	public void setR82_crm_sub_rwa_org_cou(BigDecimal r82_crm_sub_rwa_org_cou) {
		this.r82_crm_sub_rwa_org_cou = r82_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR82_crm_sub_rwa_not_cov_crm() {
		return r82_crm_sub_rwa_not_cov_crm;
	}
	public void setR82_crm_sub_rwa_not_cov_crm(BigDecimal r82_crm_sub_rwa_not_cov_crm) {
		this.r82_crm_sub_rwa_not_cov_crm = r82_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR82_crm_comp_col_expo_elig() {
		return r82_crm_comp_col_expo_elig;
	}
	public void setR82_crm_comp_col_expo_elig(BigDecimal r82_crm_comp_col_expo_elig) {
		this.r82_crm_comp_col_expo_elig = r82_crm_comp_col_expo_elig;
	}
	public BigDecimal getR82_crm_comp_col_elig_expo_vol_adj() {
		return r82_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR82_crm_comp_col_elig_expo_vol_adj(BigDecimal r82_crm_comp_col_elig_expo_vol_adj) {
		this.r82_crm_comp_col_elig_expo_vol_adj = r82_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR82_crm_comp_col_elig_fin_hai() {
		return r82_crm_comp_col_elig_fin_hai;
	}
	public void setR82_crm_comp_col_elig_fin_hai(BigDecimal r82_crm_comp_col_elig_fin_hai) {
		this.r82_crm_comp_col_elig_fin_hai = r82_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR82_crm_comp_col_expo_val() {
		return r82_crm_comp_col_expo_val;
	}
	public void setR82_crm_comp_col_expo_val(BigDecimal r82_crm_comp_col_expo_val) {
		this.r82_crm_comp_col_expo_val = r82_crm_comp_col_expo_val;
	}
	public BigDecimal getR82_rwa_elig_expo_not_cov_crm() {
		return r82_rwa_elig_expo_not_cov_crm;
	}
	public void setR82_rwa_elig_expo_not_cov_crm(BigDecimal r82_rwa_elig_expo_not_cov_crm) {
		this.r82_rwa_elig_expo_not_cov_crm = r82_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR82_rwa_unsec_expo_cre_ris() {
		return r82_rwa_unsec_expo_cre_ris;
	}
	public void setR82_rwa_unsec_expo_cre_ris(BigDecimal r82_rwa_unsec_expo_cre_ris) {
		this.r82_rwa_unsec_expo_cre_ris = r82_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR82_rwa_unsec_expo() {
		return r82_rwa_unsec_expo;
	}
	public void setR82_rwa_unsec_expo(BigDecimal r82_rwa_unsec_expo) {
		this.r82_rwa_unsec_expo = r82_rwa_unsec_expo;
	}
	public BigDecimal getR82_rwa_tot_ris_wei_ass() {
		return r82_rwa_tot_ris_wei_ass;
	}
	public void setR82_rwa_tot_ris_wei_ass(BigDecimal r82_rwa_tot_ris_wei_ass) {
		this.r82_rwa_tot_ris_wei_ass = r82_rwa_tot_ris_wei_ass;
	}
	public String getR85_exposure_class() {
		return r85_exposure_class;
	}
	public void setR85_exposure_class(String r85_exposure_class) {
		this.r85_exposure_class = r85_exposure_class;
	}
	public BigDecimal getR85_expo_crm() {
		return r85_expo_crm;
	}
	public void setR85_expo_crm(BigDecimal r85_expo_crm) {
		this.r85_expo_crm = r85_expo_crm;
	}
	public BigDecimal getR85_spe_pro_expo() {
		return r85_spe_pro_expo;
	}
	public void setR85_spe_pro_expo(BigDecimal r85_spe_pro_expo) {
		this.r85_spe_pro_expo = r85_spe_pro_expo;
	}
	public BigDecimal getR85_amt_elig_sht_net() {
		return r85_amt_elig_sht_net;
	}
	public void setR85_amt_elig_sht_net(BigDecimal r85_amt_elig_sht_net) {
		this.r85_amt_elig_sht_net = r85_amt_elig_sht_net;
	}
	public BigDecimal getR85_tot_expo_net_spe() {
		return r85_tot_expo_net_spe;
	}
	public void setR85_tot_expo_net_spe(BigDecimal r85_tot_expo_net_spe) {
		this.r85_tot_expo_net_spe = r85_tot_expo_net_spe;
	}
	public BigDecimal getR85_crm_sub_elig_sub_app() {
		return r85_crm_sub_elig_sub_app;
	}
	public void setR85_crm_sub_elig_sub_app(BigDecimal r85_crm_sub_elig_sub_app) {
		this.r85_crm_sub_elig_sub_app = r85_crm_sub_elig_sub_app;
	}
	public BigDecimal getR85_crm_sub_non_col_guar() {
		return r85_crm_sub_non_col_guar;
	}
	public void setR85_crm_sub_non_col_guar(BigDecimal r85_crm_sub_non_col_guar) {
		this.r85_crm_sub_non_col_guar = r85_crm_sub_non_col_guar;
	}
	public BigDecimal getR85_crm_sub_non_col_cre_der() {
		return r85_crm_sub_non_col_cre_der;
	}
	public void setR85_crm_sub_non_col_cre_der(BigDecimal r85_crm_sub_non_col_cre_der) {
		this.r85_crm_sub_non_col_cre_der = r85_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR85_crm_sub_col_elig_cash() {
		return r85_crm_sub_col_elig_cash;
	}
	public void setR85_crm_sub_col_elig_cash(BigDecimal r85_crm_sub_col_elig_cash) {
		this.r85_crm_sub_col_elig_cash = r85_crm_sub_col_elig_cash;
	}
	public BigDecimal getR85_crm_sub_col_elig_trea_bills() {
		return r85_crm_sub_col_elig_trea_bills;
	}
	public void setR85_crm_sub_col_elig_trea_bills(BigDecimal r85_crm_sub_col_elig_trea_bills) {
		this.r85_crm_sub_col_elig_trea_bills = r85_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR85_crm_sub_col_elig_deb_sec() {
		return r85_crm_sub_col_elig_deb_sec;
	}
	public void setR85_crm_sub_col_elig_deb_sec(BigDecimal r85_crm_sub_col_elig_deb_sec) {
		this.r85_crm_sub_col_elig_deb_sec = r85_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR85_crm_sub_col_elig_equi() {
		return r85_crm_sub_col_elig_equi;
	}
	public void setR85_crm_sub_col_elig_equi(BigDecimal r85_crm_sub_col_elig_equi) {
		this.r85_crm_sub_col_elig_equi = r85_crm_sub_col_elig_equi;
	}
	public BigDecimal getR85_crm_sub_col_elig_unit_tru() {
		return r85_crm_sub_col_elig_unit_tru;
	}
	public void setR85_crm_sub_col_elig_unit_tru(BigDecimal r85_crm_sub_col_elig_unit_tru) {
		this.r85_crm_sub_col_elig_unit_tru = r85_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR85_crm_sub_col_exp_cov() {
		return r85_crm_sub_col_exp_cov;
	}
	public void setR85_crm_sub_col_exp_cov(BigDecimal r85_crm_sub_col_exp_cov) {
		this.r85_crm_sub_col_exp_cov = r85_crm_sub_col_exp_cov;
	}
	public BigDecimal getR85_crm_sub_col_elig_exp_not_cov() {
		return r85_crm_sub_col_elig_exp_not_cov;
	}
	public void setR85_crm_sub_col_elig_exp_not_cov(BigDecimal r85_crm_sub_col_elig_exp_not_cov) {
		this.r85_crm_sub_col_elig_exp_not_cov = r85_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR85_crm_sub_rwa_ris_crm() {
		return r85_crm_sub_rwa_ris_crm;
	}
	public void setR85_crm_sub_rwa_ris_crm(BigDecimal r85_crm_sub_rwa_ris_crm) {
		this.r85_crm_sub_rwa_ris_crm = r85_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR85_crm_sub_rwa_cov_crm() {
		return r85_crm_sub_rwa_cov_crm;
	}
	public void setR85_crm_sub_rwa_cov_crm(BigDecimal r85_crm_sub_rwa_cov_crm) {
		this.r85_crm_sub_rwa_cov_crm = r85_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR85_crm_sub_rwa_org_cou() {
		return r85_crm_sub_rwa_org_cou;
	}
	public void setR85_crm_sub_rwa_org_cou(BigDecimal r85_crm_sub_rwa_org_cou) {
		this.r85_crm_sub_rwa_org_cou = r85_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR85_crm_sub_rwa_not_cov_crm() {
		return r85_crm_sub_rwa_not_cov_crm;
	}
	public void setR85_crm_sub_rwa_not_cov_crm(BigDecimal r85_crm_sub_rwa_not_cov_crm) {
		this.r85_crm_sub_rwa_not_cov_crm = r85_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR85_crm_comp_col_expo_elig() {
		return r85_crm_comp_col_expo_elig;
	}
	public void setR85_crm_comp_col_expo_elig(BigDecimal r85_crm_comp_col_expo_elig) {
		this.r85_crm_comp_col_expo_elig = r85_crm_comp_col_expo_elig;
	}
	public BigDecimal getR85_crm_comp_col_elig_expo_vol_adj() {
		return r85_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR85_crm_comp_col_elig_expo_vol_adj(BigDecimal r85_crm_comp_col_elig_expo_vol_adj) {
		this.r85_crm_comp_col_elig_expo_vol_adj = r85_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR85_crm_comp_col_elig_fin_hai() {
		return r85_crm_comp_col_elig_fin_hai;
	}
	public void setR85_crm_comp_col_elig_fin_hai(BigDecimal r85_crm_comp_col_elig_fin_hai) {
		this.r85_crm_comp_col_elig_fin_hai = r85_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR85_crm_comp_col_expo_val() {
		return r85_crm_comp_col_expo_val;
	}
	public void setR85_crm_comp_col_expo_val(BigDecimal r85_crm_comp_col_expo_val) {
		this.r85_crm_comp_col_expo_val = r85_crm_comp_col_expo_val;
	}
	public BigDecimal getR85_rwa_elig_expo_not_cov_crm() {
		return r85_rwa_elig_expo_not_cov_crm;
	}
	public void setR85_rwa_elig_expo_not_cov_crm(BigDecimal r85_rwa_elig_expo_not_cov_crm) {
		this.r85_rwa_elig_expo_not_cov_crm = r85_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR85_rwa_unsec_expo_cre_ris() {
		return r85_rwa_unsec_expo_cre_ris;
	}
	public void setR85_rwa_unsec_expo_cre_ris(BigDecimal r85_rwa_unsec_expo_cre_ris) {
		this.r85_rwa_unsec_expo_cre_ris = r85_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR85_rwa_unsec_expo() {
		return r85_rwa_unsec_expo;
	}
	public void setR85_rwa_unsec_expo(BigDecimal r85_rwa_unsec_expo) {
		this.r85_rwa_unsec_expo = r85_rwa_unsec_expo;
	}
	public BigDecimal getR85_rwa_tot_ris_wei_ass() {
		return r85_rwa_tot_ris_wei_ass;
	}
	public void setR85_rwa_tot_ris_wei_ass(BigDecimal r85_rwa_tot_ris_wei_ass) {
		this.r85_rwa_tot_ris_wei_ass = r85_rwa_tot_ris_wei_ass;
	}
	public String getR86_exposure_class() {
		return r86_exposure_class;
	}
	public void setR86_exposure_class(String r86_exposure_class) {
		this.r86_exposure_class = r86_exposure_class;
	}
	public BigDecimal getR86_expo_crm() {
		return r86_expo_crm;
	}
	public void setR86_expo_crm(BigDecimal r86_expo_crm) {
		this.r86_expo_crm = r86_expo_crm;
	}
	public BigDecimal getR86_spe_pro_expo() {
		return r86_spe_pro_expo;
	}
	public void setR86_spe_pro_expo(BigDecimal r86_spe_pro_expo) {
		this.r86_spe_pro_expo = r86_spe_pro_expo;
	}
	public BigDecimal getR86_amt_elig_sht_net() {
		return r86_amt_elig_sht_net;
	}
	public void setR86_amt_elig_sht_net(BigDecimal r86_amt_elig_sht_net) {
		this.r86_amt_elig_sht_net = r86_amt_elig_sht_net;
	}
	public BigDecimal getR86_tot_expo_net_spe() {
		return r86_tot_expo_net_spe;
	}
	public void setR86_tot_expo_net_spe(BigDecimal r86_tot_expo_net_spe) {
		this.r86_tot_expo_net_spe = r86_tot_expo_net_spe;
	}
	public BigDecimal getR86_crm_sub_elig_sub_app() {
		return r86_crm_sub_elig_sub_app;
	}
	public void setR86_crm_sub_elig_sub_app(BigDecimal r86_crm_sub_elig_sub_app) {
		this.r86_crm_sub_elig_sub_app = r86_crm_sub_elig_sub_app;
	}
	public BigDecimal getR86_crm_sub_non_col_guar() {
		return r86_crm_sub_non_col_guar;
	}
	public void setR86_crm_sub_non_col_guar(BigDecimal r86_crm_sub_non_col_guar) {
		this.r86_crm_sub_non_col_guar = r86_crm_sub_non_col_guar;
	}
	public BigDecimal getR86_crm_sub_non_col_cre_der() {
		return r86_crm_sub_non_col_cre_der;
	}
	public void setR86_crm_sub_non_col_cre_der(BigDecimal r86_crm_sub_non_col_cre_der) {
		this.r86_crm_sub_non_col_cre_der = r86_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR86_crm_sub_col_elig_cash() {
		return r86_crm_sub_col_elig_cash;
	}
	public void setR86_crm_sub_col_elig_cash(BigDecimal r86_crm_sub_col_elig_cash) {
		this.r86_crm_sub_col_elig_cash = r86_crm_sub_col_elig_cash;
	}
	public BigDecimal getR86_crm_sub_col_elig_trea_bills() {
		return r86_crm_sub_col_elig_trea_bills;
	}
	public void setR86_crm_sub_col_elig_trea_bills(BigDecimal r86_crm_sub_col_elig_trea_bills) {
		this.r86_crm_sub_col_elig_trea_bills = r86_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR86_crm_sub_col_elig_deb_sec() {
		return r86_crm_sub_col_elig_deb_sec;
	}
	public void setR86_crm_sub_col_elig_deb_sec(BigDecimal r86_crm_sub_col_elig_deb_sec) {
		this.r86_crm_sub_col_elig_deb_sec = r86_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR86_crm_sub_col_elig_equi() {
		return r86_crm_sub_col_elig_equi;
	}
	public void setR86_crm_sub_col_elig_equi(BigDecimal r86_crm_sub_col_elig_equi) {
		this.r86_crm_sub_col_elig_equi = r86_crm_sub_col_elig_equi;
	}
	public BigDecimal getR86_crm_sub_col_elig_unit_tru() {
		return r86_crm_sub_col_elig_unit_tru;
	}
	public void setR86_crm_sub_col_elig_unit_tru(BigDecimal r86_crm_sub_col_elig_unit_tru) {
		this.r86_crm_sub_col_elig_unit_tru = r86_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR86_crm_sub_col_exp_cov() {
		return r86_crm_sub_col_exp_cov;
	}
	public void setR86_crm_sub_col_exp_cov(BigDecimal r86_crm_sub_col_exp_cov) {
		this.r86_crm_sub_col_exp_cov = r86_crm_sub_col_exp_cov;
	}
	public BigDecimal getR86_crm_sub_col_elig_exp_not_cov() {
		return r86_crm_sub_col_elig_exp_not_cov;
	}
	public void setR86_crm_sub_col_elig_exp_not_cov(BigDecimal r86_crm_sub_col_elig_exp_not_cov) {
		this.r86_crm_sub_col_elig_exp_not_cov = r86_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR86_crm_sub_rwa_ris_crm() {
		return r86_crm_sub_rwa_ris_crm;
	}
	public void setR86_crm_sub_rwa_ris_crm(BigDecimal r86_crm_sub_rwa_ris_crm) {
		this.r86_crm_sub_rwa_ris_crm = r86_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR86_crm_sub_rwa_cov_crm() {
		return r86_crm_sub_rwa_cov_crm;
	}
	public void setR86_crm_sub_rwa_cov_crm(BigDecimal r86_crm_sub_rwa_cov_crm) {
		this.r86_crm_sub_rwa_cov_crm = r86_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR86_crm_sub_rwa_org_cou() {
		return r86_crm_sub_rwa_org_cou;
	}
	public void setR86_crm_sub_rwa_org_cou(BigDecimal r86_crm_sub_rwa_org_cou) {
		this.r86_crm_sub_rwa_org_cou = r86_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR86_crm_sub_rwa_not_cov_crm() {
		return r86_crm_sub_rwa_not_cov_crm;
	}
	public void setR86_crm_sub_rwa_not_cov_crm(BigDecimal r86_crm_sub_rwa_not_cov_crm) {
		this.r86_crm_sub_rwa_not_cov_crm = r86_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR86_crm_comp_col_expo_elig() {
		return r86_crm_comp_col_expo_elig;
	}
	public void setR86_crm_comp_col_expo_elig(BigDecimal r86_crm_comp_col_expo_elig) {
		this.r86_crm_comp_col_expo_elig = r86_crm_comp_col_expo_elig;
	}
	public BigDecimal getR86_crm_comp_col_elig_expo_vol_adj() {
		return r86_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR86_crm_comp_col_elig_expo_vol_adj(BigDecimal r86_crm_comp_col_elig_expo_vol_adj) {
		this.r86_crm_comp_col_elig_expo_vol_adj = r86_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR86_crm_comp_col_elig_fin_hai() {
		return r86_crm_comp_col_elig_fin_hai;
	}
	public void setR86_crm_comp_col_elig_fin_hai(BigDecimal r86_crm_comp_col_elig_fin_hai) {
		this.r86_crm_comp_col_elig_fin_hai = r86_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR86_crm_comp_col_expo_val() {
		return r86_crm_comp_col_expo_val;
	}
	public void setR86_crm_comp_col_expo_val(BigDecimal r86_crm_comp_col_expo_val) {
		this.r86_crm_comp_col_expo_val = r86_crm_comp_col_expo_val;
	}
	public BigDecimal getR86_rwa_elig_expo_not_cov_crm() {
		return r86_rwa_elig_expo_not_cov_crm;
	}
	public void setR86_rwa_elig_expo_not_cov_crm(BigDecimal r86_rwa_elig_expo_not_cov_crm) {
		this.r86_rwa_elig_expo_not_cov_crm = r86_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR86_rwa_unsec_expo_cre_ris() {
		return r86_rwa_unsec_expo_cre_ris;
	}
	public void setR86_rwa_unsec_expo_cre_ris(BigDecimal r86_rwa_unsec_expo_cre_ris) {
		this.r86_rwa_unsec_expo_cre_ris = r86_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR86_rwa_unsec_expo() {
		return r86_rwa_unsec_expo;
	}
	public void setR86_rwa_unsec_expo(BigDecimal r86_rwa_unsec_expo) {
		this.r86_rwa_unsec_expo = r86_rwa_unsec_expo;
	}
	public BigDecimal getR86_rwa_tot_ris_wei_ass() {
		return r86_rwa_tot_ris_wei_ass;
	}
	public void setR86_rwa_tot_ris_wei_ass(BigDecimal r86_rwa_tot_ris_wei_ass) {
		this.r86_rwa_tot_ris_wei_ass = r86_rwa_tot_ris_wei_ass;
	}
	public String getR87_exposure_class() {
		return r87_exposure_class;
	}
	public void setR87_exposure_class(String r87_exposure_class) {
		this.r87_exposure_class = r87_exposure_class;
	}
	
	public BigDecimal getR87_spe_pro_expo() {
		return r87_spe_pro_expo;
	}
	public void setR87_spe_pro_expo(BigDecimal r87_spe_pro_expo) {
		this.r87_spe_pro_expo = r87_spe_pro_expo;
	}
	public BigDecimal getR87_amt_elig_sht_net() {
		return r87_amt_elig_sht_net;
	}
	public void setR87_amt_elig_sht_net(BigDecimal r87_amt_elig_sht_net) {
		this.r87_amt_elig_sht_net = r87_amt_elig_sht_net;
	}
	public BigDecimal getR87_tot_expo_net_spe() {
		return r87_tot_expo_net_spe;
	}
	public void setR87_tot_expo_net_spe(BigDecimal r87_tot_expo_net_spe) {
		this.r87_tot_expo_net_spe = r87_tot_expo_net_spe;
	}
	public BigDecimal getR87_crm_sub_elig_sub_app() {
		return r87_crm_sub_elig_sub_app;
	}
	public void setR87_crm_sub_elig_sub_app(BigDecimal r87_crm_sub_elig_sub_app) {
		this.r87_crm_sub_elig_sub_app = r87_crm_sub_elig_sub_app;
	}
	public BigDecimal getR87_crm_sub_non_col_guar() {
		return r87_crm_sub_non_col_guar;
	}
	public void setR87_crm_sub_non_col_guar(BigDecimal r87_crm_sub_non_col_guar) {
		this.r87_crm_sub_non_col_guar = r87_crm_sub_non_col_guar;
	}
	public BigDecimal getR87_crm_sub_non_col_cre_der() {
		return r87_crm_sub_non_col_cre_der;
	}
	public void setR87_crm_sub_non_col_cre_der(BigDecimal r87_crm_sub_non_col_cre_der) {
		this.r87_crm_sub_non_col_cre_der = r87_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR87_crm_sub_col_elig_cash() {
		return r87_crm_sub_col_elig_cash;
	}
	public void setR87_crm_sub_col_elig_cash(BigDecimal r87_crm_sub_col_elig_cash) {
		this.r87_crm_sub_col_elig_cash = r87_crm_sub_col_elig_cash;
	}
	public BigDecimal getR87_crm_sub_col_elig_trea_bills() {
		return r87_crm_sub_col_elig_trea_bills;
	}
	public void setR87_crm_sub_col_elig_trea_bills(BigDecimal r87_crm_sub_col_elig_trea_bills) {
		this.r87_crm_sub_col_elig_trea_bills = r87_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR87_crm_sub_col_elig_deb_sec() {
		return r87_crm_sub_col_elig_deb_sec;
	}
	public void setR87_crm_sub_col_elig_deb_sec(BigDecimal r87_crm_sub_col_elig_deb_sec) {
		this.r87_crm_sub_col_elig_deb_sec = r87_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR87_crm_sub_col_elig_equi() {
		return r87_crm_sub_col_elig_equi;
	}
	public void setR87_crm_sub_col_elig_equi(BigDecimal r87_crm_sub_col_elig_equi) {
		this.r87_crm_sub_col_elig_equi = r87_crm_sub_col_elig_equi;
	}
	public BigDecimal getR87_crm_sub_col_elig_unit_tru() {
		return r87_crm_sub_col_elig_unit_tru;
	}
	public void setR87_crm_sub_col_elig_unit_tru(BigDecimal r87_crm_sub_col_elig_unit_tru) {
		this.r87_crm_sub_col_elig_unit_tru = r87_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR87_crm_sub_col_exp_cov() {
		return r87_crm_sub_col_exp_cov;
	}
	public void setR87_crm_sub_col_exp_cov(BigDecimal r87_crm_sub_col_exp_cov) {
		this.r87_crm_sub_col_exp_cov = r87_crm_sub_col_exp_cov;
	}
	public BigDecimal getR87_crm_sub_col_elig_exp_not_cov() {
		return r87_crm_sub_col_elig_exp_not_cov;
	}
	public void setR87_crm_sub_col_elig_exp_not_cov(BigDecimal r87_crm_sub_col_elig_exp_not_cov) {
		this.r87_crm_sub_col_elig_exp_not_cov = r87_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR87_crm_sub_rwa_ris_crm() {
		return r87_crm_sub_rwa_ris_crm;
	}
	public void setR87_crm_sub_rwa_ris_crm(BigDecimal r87_crm_sub_rwa_ris_crm) {
		this.r87_crm_sub_rwa_ris_crm = r87_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR87_crm_sub_rwa_cov_crm() {
		return r87_crm_sub_rwa_cov_crm;
	}
	public void setR87_crm_sub_rwa_cov_crm(BigDecimal r87_crm_sub_rwa_cov_crm) {
		this.r87_crm_sub_rwa_cov_crm = r87_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR87_crm_sub_rwa_org_cou() {
		return r87_crm_sub_rwa_org_cou;
	}
	public void setR87_crm_sub_rwa_org_cou(BigDecimal r87_crm_sub_rwa_org_cou) {
		this.r87_crm_sub_rwa_org_cou = r87_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR87_crm_sub_rwa_not_cov_crm() {
		return r87_crm_sub_rwa_not_cov_crm;
	}
	public void setR87_crm_sub_rwa_not_cov_crm(BigDecimal r87_crm_sub_rwa_not_cov_crm) {
		this.r87_crm_sub_rwa_not_cov_crm = r87_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR87_crm_comp_col_expo_elig() {
		return r87_crm_comp_col_expo_elig;
	}
	public void setR87_crm_comp_col_expo_elig(BigDecimal r87_crm_comp_col_expo_elig) {
		this.r87_crm_comp_col_expo_elig = r87_crm_comp_col_expo_elig;
	}
	public BigDecimal getR87_crm_comp_col_elig_expo_vol_adj() {
		return r87_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR87_crm_comp_col_elig_expo_vol_adj(BigDecimal r87_crm_comp_col_elig_expo_vol_adj) {
		this.r87_crm_comp_col_elig_expo_vol_adj = r87_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR87_crm_comp_col_elig_fin_hai() {
		return r87_crm_comp_col_elig_fin_hai;
	}
	public void setR87_crm_comp_col_elig_fin_hai(BigDecimal r87_crm_comp_col_elig_fin_hai) {
		this.r87_crm_comp_col_elig_fin_hai = r87_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR87_crm_comp_col_expo_val() {
		return r87_crm_comp_col_expo_val;
	}
	public void setR87_crm_comp_col_expo_val(BigDecimal r87_crm_comp_col_expo_val) {
		this.r87_crm_comp_col_expo_val = r87_crm_comp_col_expo_val;
	}
	public BigDecimal getR87_rwa_elig_expo_not_cov_crm() {
		return r87_rwa_elig_expo_not_cov_crm;
	}
	public void setR87_rwa_elig_expo_not_cov_crm(BigDecimal r87_rwa_elig_expo_not_cov_crm) {
		this.r87_rwa_elig_expo_not_cov_crm = r87_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR87_rwa_unsec_expo_cre_ris() {
		return r87_rwa_unsec_expo_cre_ris;
	}
	public void setR87_rwa_unsec_expo_cre_ris(BigDecimal r87_rwa_unsec_expo_cre_ris) {
		this.r87_rwa_unsec_expo_cre_ris = r87_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR87_rwa_unsec_expo() {
		return r87_rwa_unsec_expo;
	}
	public void setR87_rwa_unsec_expo(BigDecimal r87_rwa_unsec_expo) {
		this.r87_rwa_unsec_expo = r87_rwa_unsec_expo;
	}
	public BigDecimal getR87_rwa_tot_ris_wei_ass() {
		return r87_rwa_tot_ris_wei_ass;
	}
	public void setR87_rwa_tot_ris_wei_ass(BigDecimal r87_rwa_tot_ris_wei_ass) {
		this.r87_rwa_tot_ris_wei_ass = r87_rwa_tot_ris_wei_ass;
	}
	public String getR88_exposure_class() {
		return r88_exposure_class;
	}
	public void setR88_exposure_class(String r88_exposure_class) {
		this.r88_exposure_class = r88_exposure_class;
	}
	public BigDecimal getR88_expo_crm() {
		return r88_expo_crm;
	}
	public void setR88_expo_crm(BigDecimal r88_expo_crm) {
		this.r88_expo_crm = r88_expo_crm;
	}
	public BigDecimal getR88_spe_pro_expo() {
		return r88_spe_pro_expo;
	}
	public void setR88_spe_pro_expo(BigDecimal r88_spe_pro_expo) {
		this.r88_spe_pro_expo = r88_spe_pro_expo;
	}
	public BigDecimal getR88_amt_elig_sht_net() {
		return r88_amt_elig_sht_net;
	}
	public void setR88_amt_elig_sht_net(BigDecimal r88_amt_elig_sht_net) {
		this.r88_amt_elig_sht_net = r88_amt_elig_sht_net;
	}
	public BigDecimal getR88_tot_expo_net_spe() {
		return r88_tot_expo_net_spe;
	}
	public void setR88_tot_expo_net_spe(BigDecimal r88_tot_expo_net_spe) {
		this.r88_tot_expo_net_spe = r88_tot_expo_net_spe;
	}
	public BigDecimal getR88_crm_sub_elig_sub_app() {
		return r88_crm_sub_elig_sub_app;
	}
	public void setR88_crm_sub_elig_sub_app(BigDecimal r88_crm_sub_elig_sub_app) {
		this.r88_crm_sub_elig_sub_app = r88_crm_sub_elig_sub_app;
	}
	public BigDecimal getR88_crm_sub_non_col_guar() {
		return r88_crm_sub_non_col_guar;
	}
	public void setR88_crm_sub_non_col_guar(BigDecimal r88_crm_sub_non_col_guar) {
		this.r88_crm_sub_non_col_guar = r88_crm_sub_non_col_guar;
	}
	public BigDecimal getR88_crm_sub_non_col_cre_der() {
		return r88_crm_sub_non_col_cre_der;
	}
	public void setR88_crm_sub_non_col_cre_der(BigDecimal r88_crm_sub_non_col_cre_der) {
		this.r88_crm_sub_non_col_cre_der = r88_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR88_crm_sub_col_elig_cash() {
		return r88_crm_sub_col_elig_cash;
	}
	public void setR88_crm_sub_col_elig_cash(BigDecimal r88_crm_sub_col_elig_cash) {
		this.r88_crm_sub_col_elig_cash = r88_crm_sub_col_elig_cash;
	}
	public BigDecimal getR88_crm_sub_col_elig_trea_bills() {
		return r88_crm_sub_col_elig_trea_bills;
	}
	public void setR88_crm_sub_col_elig_trea_bills(BigDecimal r88_crm_sub_col_elig_trea_bills) {
		this.r88_crm_sub_col_elig_trea_bills = r88_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR88_crm_sub_col_elig_deb_sec() {
		return r88_crm_sub_col_elig_deb_sec;
	}
	public void setR88_crm_sub_col_elig_deb_sec(BigDecimal r88_crm_sub_col_elig_deb_sec) {
		this.r88_crm_sub_col_elig_deb_sec = r88_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR88_crm_sub_col_elig_equi() {
		return r88_crm_sub_col_elig_equi;
	}
	public void setR88_crm_sub_col_elig_equi(BigDecimal r88_crm_sub_col_elig_equi) {
		this.r88_crm_sub_col_elig_equi = r88_crm_sub_col_elig_equi;
	}
	public BigDecimal getR88_crm_sub_col_elig_unit_tru() {
		return r88_crm_sub_col_elig_unit_tru;
	}
	public void setR88_crm_sub_col_elig_unit_tru(BigDecimal r88_crm_sub_col_elig_unit_tru) {
		this.r88_crm_sub_col_elig_unit_tru = r88_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR88_crm_sub_col_exp_cov() {
		return r88_crm_sub_col_exp_cov;
	}
	public void setR88_crm_sub_col_exp_cov(BigDecimal r88_crm_sub_col_exp_cov) {
		this.r88_crm_sub_col_exp_cov = r88_crm_sub_col_exp_cov;
	}
	public BigDecimal getR88_crm_sub_col_elig_exp_not_cov() {
		return r88_crm_sub_col_elig_exp_not_cov;
	}
	public void setR88_crm_sub_col_elig_exp_not_cov(BigDecimal r88_crm_sub_col_elig_exp_not_cov) {
		this.r88_crm_sub_col_elig_exp_not_cov = r88_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR88_crm_sub_rwa_ris_crm() {
		return r88_crm_sub_rwa_ris_crm;
	}
	public void setR88_crm_sub_rwa_ris_crm(BigDecimal r88_crm_sub_rwa_ris_crm) {
		this.r88_crm_sub_rwa_ris_crm = r88_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR88_crm_sub_rwa_cov_crm() {
		return r88_crm_sub_rwa_cov_crm;
	}
	public void setR88_crm_sub_rwa_cov_crm(BigDecimal r88_crm_sub_rwa_cov_crm) {
		this.r88_crm_sub_rwa_cov_crm = r88_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR88_crm_sub_rwa_org_cou() {
		return r88_crm_sub_rwa_org_cou;
	}
	public void setR88_crm_sub_rwa_org_cou(BigDecimal r88_crm_sub_rwa_org_cou) {
		this.r88_crm_sub_rwa_org_cou = r88_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR88_crm_sub_rwa_not_cov_crm() {
		return r88_crm_sub_rwa_not_cov_crm;
	}
	public void setR88_crm_sub_rwa_not_cov_crm(BigDecimal r88_crm_sub_rwa_not_cov_crm) {
		this.r88_crm_sub_rwa_not_cov_crm = r88_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR88_crm_comp_col_expo_elig() {
		return r88_crm_comp_col_expo_elig;
	}
	public void setR88_crm_comp_col_expo_elig(BigDecimal r88_crm_comp_col_expo_elig) {
		this.r88_crm_comp_col_expo_elig = r88_crm_comp_col_expo_elig;
	}
	public BigDecimal getR88_crm_comp_col_elig_expo_vol_adj() {
		return r88_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR88_crm_comp_col_elig_expo_vol_adj(BigDecimal r88_crm_comp_col_elig_expo_vol_adj) {
		this.r88_crm_comp_col_elig_expo_vol_adj = r88_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR88_crm_comp_col_elig_fin_hai() {
		return r88_crm_comp_col_elig_fin_hai;
	}
	public void setR88_crm_comp_col_elig_fin_hai(BigDecimal r88_crm_comp_col_elig_fin_hai) {
		this.r88_crm_comp_col_elig_fin_hai = r88_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR88_crm_comp_col_expo_val() {
		return r88_crm_comp_col_expo_val;
	}
	public void setR88_crm_comp_col_expo_val(BigDecimal r88_crm_comp_col_expo_val) {
		this.r88_crm_comp_col_expo_val = r88_crm_comp_col_expo_val;
	}
	public BigDecimal getR88_rwa_elig_expo_not_cov_crm() {
		return r88_rwa_elig_expo_not_cov_crm;
	}
	public void setR88_rwa_elig_expo_not_cov_crm(BigDecimal r88_rwa_elig_expo_not_cov_crm) {
		this.r88_rwa_elig_expo_not_cov_crm = r88_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR88_rwa_unsec_expo_cre_ris() {
		return r88_rwa_unsec_expo_cre_ris;
	}
	public void setR88_rwa_unsec_expo_cre_ris(BigDecimal r88_rwa_unsec_expo_cre_ris) {
		this.r88_rwa_unsec_expo_cre_ris = r88_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR88_rwa_unsec_expo() {
		return r88_rwa_unsec_expo;
	}
	public void setR88_rwa_unsec_expo(BigDecimal r88_rwa_unsec_expo) {
		this.r88_rwa_unsec_expo = r88_rwa_unsec_expo;
	}
	public BigDecimal getR88_rwa_tot_ris_wei_ass() {
		return r88_rwa_tot_ris_wei_ass;
	}
	public void setR88_rwa_tot_ris_wei_ass(BigDecimal r88_rwa_tot_ris_wei_ass) {
		this.r88_rwa_tot_ris_wei_ass = r88_rwa_tot_ris_wei_ass;
	}
	public String getR89_exposure_class() {
		return r89_exposure_class;
	}
	public void setR89_exposure_class(String r89_exposure_class) {
		this.r89_exposure_class = r89_exposure_class;
	}
	public BigDecimal getR89_expo_crm() {
		return r89_expo_crm;
	}
	public void setR89_expo_crm(BigDecimal r89_expo_crm) {
		this.r89_expo_crm = r89_expo_crm;
	}
	public BigDecimal getR89_spe_pro_expo() {
		return r89_spe_pro_expo;
	}
	public void setR89_spe_pro_expo(BigDecimal r89_spe_pro_expo) {
		this.r89_spe_pro_expo = r89_spe_pro_expo;
	}
	public BigDecimal getR89_amt_elig_sht_net() {
		return r89_amt_elig_sht_net;
	}
	public void setR89_amt_elig_sht_net(BigDecimal r89_amt_elig_sht_net) {
		this.r89_amt_elig_sht_net = r89_amt_elig_sht_net;
	}
	public BigDecimal getR89_tot_expo_net_spe() {
		return r89_tot_expo_net_spe;
	}
	public void setR89_tot_expo_net_spe(BigDecimal r89_tot_expo_net_spe) {
		this.r89_tot_expo_net_spe = r89_tot_expo_net_spe;
	}
	public BigDecimal getR89_crm_sub_elig_sub_app() {
		return r89_crm_sub_elig_sub_app;
	}
	public void setR89_crm_sub_elig_sub_app(BigDecimal r89_crm_sub_elig_sub_app) {
		this.r89_crm_sub_elig_sub_app = r89_crm_sub_elig_sub_app;
	}
	public BigDecimal getR89_crm_sub_non_col_guar() {
		return r89_crm_sub_non_col_guar;
	}
	public void setR89_crm_sub_non_col_guar(BigDecimal r89_crm_sub_non_col_guar) {
		this.r89_crm_sub_non_col_guar = r89_crm_sub_non_col_guar;
	}
	public BigDecimal getR89_crm_sub_non_col_cre_der() {
		return r89_crm_sub_non_col_cre_der;
	}
	public void setR89_crm_sub_non_col_cre_der(BigDecimal r89_crm_sub_non_col_cre_der) {
		this.r89_crm_sub_non_col_cre_der = r89_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR89_crm_sub_col_elig_cash() {
		return r89_crm_sub_col_elig_cash;
	}
	public void setR89_crm_sub_col_elig_cash(BigDecimal r89_crm_sub_col_elig_cash) {
		this.r89_crm_sub_col_elig_cash = r89_crm_sub_col_elig_cash;
	}
	public BigDecimal getR89_crm_sub_col_elig_trea_bills() {
		return r89_crm_sub_col_elig_trea_bills;
	}
	public void setR89_crm_sub_col_elig_trea_bills(BigDecimal r89_crm_sub_col_elig_trea_bills) {
		this.r89_crm_sub_col_elig_trea_bills = r89_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR89_crm_sub_col_elig_deb_sec() {
		return r89_crm_sub_col_elig_deb_sec;
	}
	public void setR89_crm_sub_col_elig_deb_sec(BigDecimal r89_crm_sub_col_elig_deb_sec) {
		this.r89_crm_sub_col_elig_deb_sec = r89_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR89_crm_sub_col_elig_equi() {
		return r89_crm_sub_col_elig_equi;
	}
	public void setR89_crm_sub_col_elig_equi(BigDecimal r89_crm_sub_col_elig_equi) {
		this.r89_crm_sub_col_elig_equi = r89_crm_sub_col_elig_equi;
	}
	public BigDecimal getR89_crm_sub_col_elig_unit_tru() {
		return r89_crm_sub_col_elig_unit_tru;
	}
	public void setR89_crm_sub_col_elig_unit_tru(BigDecimal r89_crm_sub_col_elig_unit_tru) {
		this.r89_crm_sub_col_elig_unit_tru = r89_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR89_crm_sub_col_exp_cov() {
		return r89_crm_sub_col_exp_cov;
	}
	public void setR89_crm_sub_col_exp_cov(BigDecimal r89_crm_sub_col_exp_cov) {
		this.r89_crm_sub_col_exp_cov = r89_crm_sub_col_exp_cov;
	}
	public BigDecimal getR89_crm_sub_col_elig_exp_not_cov() {
		return r89_crm_sub_col_elig_exp_not_cov;
	}
	public void setR89_crm_sub_col_elig_exp_not_cov(BigDecimal r89_crm_sub_col_elig_exp_not_cov) {
		this.r89_crm_sub_col_elig_exp_not_cov = r89_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR89_crm_sub_rwa_ris_crm() {
		return r89_crm_sub_rwa_ris_crm;
	}
	public void setR89_crm_sub_rwa_ris_crm(BigDecimal r89_crm_sub_rwa_ris_crm) {
		this.r89_crm_sub_rwa_ris_crm = r89_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR89_crm_sub_rwa_cov_crm() {
		return r89_crm_sub_rwa_cov_crm;
	}
	public void setR89_crm_sub_rwa_cov_crm(BigDecimal r89_crm_sub_rwa_cov_crm) {
		this.r89_crm_sub_rwa_cov_crm = r89_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR89_crm_sub_rwa_org_cou() {
		return r89_crm_sub_rwa_org_cou;
	}
	public void setR89_crm_sub_rwa_org_cou(BigDecimal r89_crm_sub_rwa_org_cou) {
		this.r89_crm_sub_rwa_org_cou = r89_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR89_crm_sub_rwa_not_cov_crm() {
		return r89_crm_sub_rwa_not_cov_crm;
	}
	public void setR89_crm_sub_rwa_not_cov_crm(BigDecimal r89_crm_sub_rwa_not_cov_crm) {
		this.r89_crm_sub_rwa_not_cov_crm = r89_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR89_crm_comp_col_expo_elig() {
		return r89_crm_comp_col_expo_elig;
	}
	public void setR89_crm_comp_col_expo_elig(BigDecimal r89_crm_comp_col_expo_elig) {
		this.r89_crm_comp_col_expo_elig = r89_crm_comp_col_expo_elig;
	}
	public BigDecimal getR89_crm_comp_col_elig_expo_vol_adj() {
		return r89_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR89_crm_comp_col_elig_expo_vol_adj(BigDecimal r89_crm_comp_col_elig_expo_vol_adj) {
		this.r89_crm_comp_col_elig_expo_vol_adj = r89_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR89_crm_comp_col_elig_fin_hai() {
		return r89_crm_comp_col_elig_fin_hai;
	}
	public void setR89_crm_comp_col_elig_fin_hai(BigDecimal r89_crm_comp_col_elig_fin_hai) {
		this.r89_crm_comp_col_elig_fin_hai = r89_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR89_crm_comp_col_expo_val() {
		return r89_crm_comp_col_expo_val;
	}
	public void setR89_crm_comp_col_expo_val(BigDecimal r89_crm_comp_col_expo_val) {
		this.r89_crm_comp_col_expo_val = r89_crm_comp_col_expo_val;
	}
	public BigDecimal getR89_rwa_elig_expo_not_cov_crm() {
		return r89_rwa_elig_expo_not_cov_crm;
	}
	public void setR89_rwa_elig_expo_not_cov_crm(BigDecimal r89_rwa_elig_expo_not_cov_crm) {
		this.r89_rwa_elig_expo_not_cov_crm = r89_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR89_rwa_unsec_expo_cre_ris() {
		return r89_rwa_unsec_expo_cre_ris;
	}
	public void setR89_rwa_unsec_expo_cre_ris(BigDecimal r89_rwa_unsec_expo_cre_ris) {
		this.r89_rwa_unsec_expo_cre_ris = r89_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR89_rwa_unsec_expo() {
		return r89_rwa_unsec_expo;
	}
	public void setR89_rwa_unsec_expo(BigDecimal r89_rwa_unsec_expo) {
		this.r89_rwa_unsec_expo = r89_rwa_unsec_expo;
	}
	public BigDecimal getR89_rwa_tot_ris_wei_ass() {
		return r89_rwa_tot_ris_wei_ass;
	}
	public void setR89_rwa_tot_ris_wei_ass(BigDecimal r89_rwa_tot_ris_wei_ass) {
		this.r89_rwa_tot_ris_wei_ass = r89_rwa_tot_ris_wei_ass;
	}
	public String getR90_exposure_class() {
		return r90_exposure_class;
	}
	public void setR90_exposure_class(String r90_exposure_class) {
		this.r90_exposure_class = r90_exposure_class;
	}
	public BigDecimal getR90_expo_crm() {
		return r90_expo_crm;
	}
	public void setR90_expo_crm(BigDecimal r90_expo_crm) {
		this.r90_expo_crm = r90_expo_crm;
	}
	public BigDecimal getR90_spe_pro_expo() {
		return r90_spe_pro_expo;
	}
	public void setR90_spe_pro_expo(BigDecimal r90_spe_pro_expo) {
		this.r90_spe_pro_expo = r90_spe_pro_expo;
	}
	public BigDecimal getR90_amt_elig_sht_net() {
		return r90_amt_elig_sht_net;
	}
	public void setR90_amt_elig_sht_net(BigDecimal r90_amt_elig_sht_net) {
		this.r90_amt_elig_sht_net = r90_amt_elig_sht_net;
	}
	public BigDecimal getR90_tot_expo_net_spe() {
		return r90_tot_expo_net_spe;
	}
	public void setR90_tot_expo_net_spe(BigDecimal r90_tot_expo_net_spe) {
		this.r90_tot_expo_net_spe = r90_tot_expo_net_spe;
	}
	public BigDecimal getR90_crm_sub_elig_sub_app() {
		return r90_crm_sub_elig_sub_app;
	}
	public void setR90_crm_sub_elig_sub_app(BigDecimal r90_crm_sub_elig_sub_app) {
		this.r90_crm_sub_elig_sub_app = r90_crm_sub_elig_sub_app;
	}
	public BigDecimal getR90_crm_sub_non_col_guar() {
		return r90_crm_sub_non_col_guar;
	}
	public void setR90_crm_sub_non_col_guar(BigDecimal r90_crm_sub_non_col_guar) {
		this.r90_crm_sub_non_col_guar = r90_crm_sub_non_col_guar;
	}
	public BigDecimal getR90_crm_sub_non_col_cre_der() {
		return r90_crm_sub_non_col_cre_der;
	}
	public void setR90_crm_sub_non_col_cre_der(BigDecimal r90_crm_sub_non_col_cre_der) {
		this.r90_crm_sub_non_col_cre_der = r90_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR90_crm_sub_col_elig_cash() {
		return r90_crm_sub_col_elig_cash;
	}
	public void setR90_crm_sub_col_elig_cash(BigDecimal r90_crm_sub_col_elig_cash) {
		this.r90_crm_sub_col_elig_cash = r90_crm_sub_col_elig_cash;
	}
	public BigDecimal getR90_crm_sub_col_elig_trea_bills() {
		return r90_crm_sub_col_elig_trea_bills;
	}
	public void setR90_crm_sub_col_elig_trea_bills(BigDecimal r90_crm_sub_col_elig_trea_bills) {
		this.r90_crm_sub_col_elig_trea_bills = r90_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR90_crm_sub_col_elig_deb_sec() {
		return r90_crm_sub_col_elig_deb_sec;
	}
	public void setR90_crm_sub_col_elig_deb_sec(BigDecimal r90_crm_sub_col_elig_deb_sec) {
		this.r90_crm_sub_col_elig_deb_sec = r90_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR90_crm_sub_col_elig_equi() {
		return r90_crm_sub_col_elig_equi;
	}
	public void setR90_crm_sub_col_elig_equi(BigDecimal r90_crm_sub_col_elig_equi) {
		this.r90_crm_sub_col_elig_equi = r90_crm_sub_col_elig_equi;
	}
	public BigDecimal getR90_crm_sub_col_elig_unit_tru() {
		return r90_crm_sub_col_elig_unit_tru;
	}
	public void setR90_crm_sub_col_elig_unit_tru(BigDecimal r90_crm_sub_col_elig_unit_tru) {
		this.r90_crm_sub_col_elig_unit_tru = r90_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR90_crm_sub_col_exp_cov() {
		return r90_crm_sub_col_exp_cov;
	}
	public void setR90_crm_sub_col_exp_cov(BigDecimal r90_crm_sub_col_exp_cov) {
		this.r90_crm_sub_col_exp_cov = r90_crm_sub_col_exp_cov;
	}
	public BigDecimal getR90_crm_sub_col_elig_exp_not_cov() {
		return r90_crm_sub_col_elig_exp_not_cov;
	}
	public void setR90_crm_sub_col_elig_exp_not_cov(BigDecimal r90_crm_sub_col_elig_exp_not_cov) {
		this.r90_crm_sub_col_elig_exp_not_cov = r90_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR90_crm_sub_rwa_ris_crm() {
		return r90_crm_sub_rwa_ris_crm;
	}
	public void setR90_crm_sub_rwa_ris_crm(BigDecimal r90_crm_sub_rwa_ris_crm) {
		this.r90_crm_sub_rwa_ris_crm = r90_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR90_crm_sub_rwa_cov_crm() {
		return r90_crm_sub_rwa_cov_crm;
	}
	public void setR90_crm_sub_rwa_cov_crm(BigDecimal r90_crm_sub_rwa_cov_crm) {
		this.r90_crm_sub_rwa_cov_crm = r90_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR90_crm_sub_rwa_org_cou() {
		return r90_crm_sub_rwa_org_cou;
	}
	public void setR90_crm_sub_rwa_org_cou(BigDecimal r90_crm_sub_rwa_org_cou) {
		this.r90_crm_sub_rwa_org_cou = r90_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR90_crm_sub_rwa_not_cov_crm() {
		return r90_crm_sub_rwa_not_cov_crm;
	}
	public void setR90_crm_sub_rwa_not_cov_crm(BigDecimal r90_crm_sub_rwa_not_cov_crm) {
		this.r90_crm_sub_rwa_not_cov_crm = r90_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR90_crm_comp_col_expo_elig() {
		return r90_crm_comp_col_expo_elig;
	}
	public void setR90_crm_comp_col_expo_elig(BigDecimal r90_crm_comp_col_expo_elig) {
		this.r90_crm_comp_col_expo_elig = r90_crm_comp_col_expo_elig;
	}
	public BigDecimal getR90_crm_comp_col_elig_expo_vol_adj() {
		return r90_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR90_crm_comp_col_elig_expo_vol_adj(BigDecimal r90_crm_comp_col_elig_expo_vol_adj) {
		this.r90_crm_comp_col_elig_expo_vol_adj = r90_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR90_crm_comp_col_elig_fin_hai() {
		return r90_crm_comp_col_elig_fin_hai;
	}
	public void setR90_crm_comp_col_elig_fin_hai(BigDecimal r90_crm_comp_col_elig_fin_hai) {
		this.r90_crm_comp_col_elig_fin_hai = r90_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR90_crm_comp_col_expo_val() {
		return r90_crm_comp_col_expo_val;
	}
	public void setR90_crm_comp_col_expo_val(BigDecimal r90_crm_comp_col_expo_val) {
		this.r90_crm_comp_col_expo_val = r90_crm_comp_col_expo_val;
	}
	public BigDecimal getR90_rwa_elig_expo_not_cov_crm() {
		return r90_rwa_elig_expo_not_cov_crm;
	}
	public void setR90_rwa_elig_expo_not_cov_crm(BigDecimal r90_rwa_elig_expo_not_cov_crm) {
		this.r90_rwa_elig_expo_not_cov_crm = r90_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR90_rwa_unsec_expo_cre_ris() {
		return r90_rwa_unsec_expo_cre_ris;
	}
	public void setR90_rwa_unsec_expo_cre_ris(BigDecimal r90_rwa_unsec_expo_cre_ris) {
		this.r90_rwa_unsec_expo_cre_ris = r90_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR90_rwa_unsec_expo() {
		return r90_rwa_unsec_expo;
	}
	public void setR90_rwa_unsec_expo(BigDecimal r90_rwa_unsec_expo) {
		this.r90_rwa_unsec_expo = r90_rwa_unsec_expo;
	}
	public BigDecimal getR90_rwa_tot_ris_wei_ass() {
		return r90_rwa_tot_ris_wei_ass;
	}
	public void setR90_rwa_tot_ris_wei_ass(BigDecimal r90_rwa_tot_ris_wei_ass) {
		this.r90_rwa_tot_ris_wei_ass = r90_rwa_tot_ris_wei_ass;
	}
	public String getR91_exposure_class() {
		return r91_exposure_class;
	}
	public void setR91_exposure_class(String r91_exposure_class) {
		this.r91_exposure_class = r91_exposure_class;
	}
	public BigDecimal getR91_expo_crm() {
		return r91_expo_crm;
	}
	public void setR91_expo_crm(BigDecimal r91_expo_crm) {
		this.r91_expo_crm = r91_expo_crm;
	}
	public BigDecimal getR91_spe_pro_expo() {
		return r91_spe_pro_expo;
	}
	public void setR91_spe_pro_expo(BigDecimal r91_spe_pro_expo) {
		this.r91_spe_pro_expo = r91_spe_pro_expo;
	}
	public BigDecimal getR91_amt_elig_sht_net() {
		return r91_amt_elig_sht_net;
	}
	public void setR91_amt_elig_sht_net(BigDecimal r91_amt_elig_sht_net) {
		this.r91_amt_elig_sht_net = r91_amt_elig_sht_net;
	}
	public BigDecimal getR91_tot_expo_net_spe() {
		return r91_tot_expo_net_spe;
	}
	public void setR91_tot_expo_net_spe(BigDecimal r91_tot_expo_net_spe) {
		this.r91_tot_expo_net_spe = r91_tot_expo_net_spe;
	}
	public BigDecimal getR91_crm_sub_elig_sub_app() {
		return r91_crm_sub_elig_sub_app;
	}
	public void setR91_crm_sub_elig_sub_app(BigDecimal r91_crm_sub_elig_sub_app) {
		this.r91_crm_sub_elig_sub_app = r91_crm_sub_elig_sub_app;
	}
	public BigDecimal getR91_crm_sub_non_col_guar() {
		return r91_crm_sub_non_col_guar;
	}
	public void setR91_crm_sub_non_col_guar(BigDecimal r91_crm_sub_non_col_guar) {
		this.r91_crm_sub_non_col_guar = r91_crm_sub_non_col_guar;
	}
	public BigDecimal getR91_crm_sub_non_col_cre_der() {
		return r91_crm_sub_non_col_cre_der;
	}
	public void setR91_crm_sub_non_col_cre_der(BigDecimal r91_crm_sub_non_col_cre_der) {
		this.r91_crm_sub_non_col_cre_der = r91_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR91_crm_sub_col_elig_cash() {
		return r91_crm_sub_col_elig_cash;
	}
	public void setR91_crm_sub_col_elig_cash(BigDecimal r91_crm_sub_col_elig_cash) {
		this.r91_crm_sub_col_elig_cash = r91_crm_sub_col_elig_cash;
	}
	public BigDecimal getR91_crm_sub_col_elig_trea_bills() {
		return r91_crm_sub_col_elig_trea_bills;
	}
	public void setR91_crm_sub_col_elig_trea_bills(BigDecimal r91_crm_sub_col_elig_trea_bills) {
		this.r91_crm_sub_col_elig_trea_bills = r91_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR91_crm_sub_col_elig_deb_sec() {
		return r91_crm_sub_col_elig_deb_sec;
	}
	public void setR91_crm_sub_col_elig_deb_sec(BigDecimal r91_crm_sub_col_elig_deb_sec) {
		this.r91_crm_sub_col_elig_deb_sec = r91_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR91_crm_sub_col_elig_equi() {
		return r91_crm_sub_col_elig_equi;
	}
	public void setR91_crm_sub_col_elig_equi(BigDecimal r91_crm_sub_col_elig_equi) {
		this.r91_crm_sub_col_elig_equi = r91_crm_sub_col_elig_equi;
	}
	public BigDecimal getR91_crm_sub_col_elig_unit_tru() {
		return r91_crm_sub_col_elig_unit_tru;
	}
	public void setR91_crm_sub_col_elig_unit_tru(BigDecimal r91_crm_sub_col_elig_unit_tru) {
		this.r91_crm_sub_col_elig_unit_tru = r91_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR91_crm_sub_col_exp_cov() {
		return r91_crm_sub_col_exp_cov;
	}
	public void setR91_crm_sub_col_exp_cov(BigDecimal r91_crm_sub_col_exp_cov) {
		this.r91_crm_sub_col_exp_cov = r91_crm_sub_col_exp_cov;
	}
	public BigDecimal getR91_crm_sub_col_elig_exp_not_cov() {
		return r91_crm_sub_col_elig_exp_not_cov;
	}
	public void setR91_crm_sub_col_elig_exp_not_cov(BigDecimal r91_crm_sub_col_elig_exp_not_cov) {
		this.r91_crm_sub_col_elig_exp_not_cov = r91_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR91_crm_sub_rwa_ris_crm() {
		return r91_crm_sub_rwa_ris_crm;
	}
	public void setR91_crm_sub_rwa_ris_crm(BigDecimal r91_crm_sub_rwa_ris_crm) {
		this.r91_crm_sub_rwa_ris_crm = r91_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR91_crm_sub_rwa_cov_crm() {
		return r91_crm_sub_rwa_cov_crm;
	}
	public void setR91_crm_sub_rwa_cov_crm(BigDecimal r91_crm_sub_rwa_cov_crm) {
		this.r91_crm_sub_rwa_cov_crm = r91_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR91_crm_sub_rwa_org_cou() {
		return r91_crm_sub_rwa_org_cou;
	}
	public void setR91_crm_sub_rwa_org_cou(BigDecimal r91_crm_sub_rwa_org_cou) {
		this.r91_crm_sub_rwa_org_cou = r91_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR91_crm_sub_rwa_not_cov_crm() {
		return r91_crm_sub_rwa_not_cov_crm;
	}
	public void setR91_crm_sub_rwa_not_cov_crm(BigDecimal r91_crm_sub_rwa_not_cov_crm) {
		this.r91_crm_sub_rwa_not_cov_crm = r91_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR91_crm_comp_col_expo_elig() {
		return r91_crm_comp_col_expo_elig;
	}
	public void setR91_crm_comp_col_expo_elig(BigDecimal r91_crm_comp_col_expo_elig) {
		this.r91_crm_comp_col_expo_elig = r91_crm_comp_col_expo_elig;
	}
	public BigDecimal getR91_crm_comp_col_elig_expo_vol_adj() {
		return r91_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR91_crm_comp_col_elig_expo_vol_adj(BigDecimal r91_crm_comp_col_elig_expo_vol_adj) {
		this.r91_crm_comp_col_elig_expo_vol_adj = r91_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR91_crm_comp_col_elig_fin_hai() {
		return r91_crm_comp_col_elig_fin_hai;
	}
	public void setR91_crm_comp_col_elig_fin_hai(BigDecimal r91_crm_comp_col_elig_fin_hai) {
		this.r91_crm_comp_col_elig_fin_hai = r91_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR91_crm_comp_col_expo_val() {
		return r91_crm_comp_col_expo_val;
	}
	public void setR91_crm_comp_col_expo_val(BigDecimal r91_crm_comp_col_expo_val) {
		this.r91_crm_comp_col_expo_val = r91_crm_comp_col_expo_val;
	}
	public BigDecimal getR91_rwa_elig_expo_not_cov_crm() {
		return r91_rwa_elig_expo_not_cov_crm;
	}
	public void setR91_rwa_elig_expo_not_cov_crm(BigDecimal r91_rwa_elig_expo_not_cov_crm) {
		this.r91_rwa_elig_expo_not_cov_crm = r91_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR91_rwa_unsec_expo_cre_ris() {
		return r91_rwa_unsec_expo_cre_ris;
	}
	public void setR91_rwa_unsec_expo_cre_ris(BigDecimal r91_rwa_unsec_expo_cre_ris) {
		this.r91_rwa_unsec_expo_cre_ris = r91_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR91_rwa_unsec_expo() {
		return r91_rwa_unsec_expo;
	}
	public void setR91_rwa_unsec_expo(BigDecimal r91_rwa_unsec_expo) {
		this.r91_rwa_unsec_expo = r91_rwa_unsec_expo;
	}
	public BigDecimal getR91_rwa_tot_ris_wei_ass() {
		return r91_rwa_tot_ris_wei_ass;
	}
	public void setR91_rwa_tot_ris_wei_ass(BigDecimal r91_rwa_tot_ris_wei_ass) {
		this.r91_rwa_tot_ris_wei_ass = r91_rwa_tot_ris_wei_ass;
	}
	public String getR92_exposure_class() {
		return r92_exposure_class;
	}
	public void setR92_exposure_class(String r92_exposure_class) {
		this.r92_exposure_class = r92_exposure_class;
	}
	
	public BigDecimal getR92_spe_pro_expo() {
		return r92_spe_pro_expo;
	}
	public void setR92_spe_pro_expo(BigDecimal r92_spe_pro_expo) {
		this.r92_spe_pro_expo = r92_spe_pro_expo;
	}
	public BigDecimal getR92_amt_elig_sht_net() {
		return r92_amt_elig_sht_net;
	}
	public void setR92_amt_elig_sht_net(BigDecimal r92_amt_elig_sht_net) {
		this.r92_amt_elig_sht_net = r92_amt_elig_sht_net;
	}
	public BigDecimal getR92_tot_expo_net_spe() {
		return r92_tot_expo_net_spe;
	}
	public void setR92_tot_expo_net_spe(BigDecimal r92_tot_expo_net_spe) {
		this.r92_tot_expo_net_spe = r92_tot_expo_net_spe;
	}
	public BigDecimal getR92_crm_sub_elig_sub_app() {
		return r92_crm_sub_elig_sub_app;
	}
	public void setR92_crm_sub_elig_sub_app(BigDecimal r92_crm_sub_elig_sub_app) {
		this.r92_crm_sub_elig_sub_app = r92_crm_sub_elig_sub_app;
	}
	public BigDecimal getR92_crm_sub_non_col_guar() {
		return r92_crm_sub_non_col_guar;
	}
	public void setR92_crm_sub_non_col_guar(BigDecimal r92_crm_sub_non_col_guar) {
		this.r92_crm_sub_non_col_guar = r92_crm_sub_non_col_guar;
	}
	public BigDecimal getR92_crm_sub_non_col_cre_der() {
		return r92_crm_sub_non_col_cre_der;
	}
	public void setR92_crm_sub_non_col_cre_der(BigDecimal r92_crm_sub_non_col_cre_der) {
		this.r92_crm_sub_non_col_cre_der = r92_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR92_crm_sub_col_elig_cash() {
		return r92_crm_sub_col_elig_cash;
	}
	public void setR92_crm_sub_col_elig_cash(BigDecimal r92_crm_sub_col_elig_cash) {
		this.r92_crm_sub_col_elig_cash = r92_crm_sub_col_elig_cash;
	}
	public BigDecimal getR92_crm_sub_col_elig_trea_bills() {
		return r92_crm_sub_col_elig_trea_bills;
	}
	public void setR92_crm_sub_col_elig_trea_bills(BigDecimal r92_crm_sub_col_elig_trea_bills) {
		this.r92_crm_sub_col_elig_trea_bills = r92_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR92_crm_sub_col_elig_deb_sec() {
		return r92_crm_sub_col_elig_deb_sec;
	}
	public void setR92_crm_sub_col_elig_deb_sec(BigDecimal r92_crm_sub_col_elig_deb_sec) {
		this.r92_crm_sub_col_elig_deb_sec = r92_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR92_crm_sub_col_elig_equi() {
		return r92_crm_sub_col_elig_equi;
	}
	public void setR92_crm_sub_col_elig_equi(BigDecimal r92_crm_sub_col_elig_equi) {
		this.r92_crm_sub_col_elig_equi = r92_crm_sub_col_elig_equi;
	}
	public BigDecimal getR92_crm_sub_col_elig_unit_tru() {
		return r92_crm_sub_col_elig_unit_tru;
	}
	public void setR92_crm_sub_col_elig_unit_tru(BigDecimal r92_crm_sub_col_elig_unit_tru) {
		this.r92_crm_sub_col_elig_unit_tru = r92_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR92_crm_sub_col_exp_cov() {
		return r92_crm_sub_col_exp_cov;
	}
	public void setR92_crm_sub_col_exp_cov(BigDecimal r92_crm_sub_col_exp_cov) {
		this.r92_crm_sub_col_exp_cov = r92_crm_sub_col_exp_cov;
	}
	public BigDecimal getR92_crm_sub_col_elig_exp_not_cov() {
		return r92_crm_sub_col_elig_exp_not_cov;
	}
	public void setR92_crm_sub_col_elig_exp_not_cov(BigDecimal r92_crm_sub_col_elig_exp_not_cov) {
		this.r92_crm_sub_col_elig_exp_not_cov = r92_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR92_crm_sub_rwa_ris_crm() {
		return r92_crm_sub_rwa_ris_crm;
	}
	public void setR92_crm_sub_rwa_ris_crm(BigDecimal r92_crm_sub_rwa_ris_crm) {
		this.r92_crm_sub_rwa_ris_crm = r92_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR92_crm_sub_rwa_cov_crm() {
		return r92_crm_sub_rwa_cov_crm;
	}
	public void setR92_crm_sub_rwa_cov_crm(BigDecimal r92_crm_sub_rwa_cov_crm) {
		this.r92_crm_sub_rwa_cov_crm = r92_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR92_crm_sub_rwa_org_cou() {
		return r92_crm_sub_rwa_org_cou;
	}
	public void setR92_crm_sub_rwa_org_cou(BigDecimal r92_crm_sub_rwa_org_cou) {
		this.r92_crm_sub_rwa_org_cou = r92_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR92_crm_sub_rwa_not_cov_crm() {
		return r92_crm_sub_rwa_not_cov_crm;
	}
	public void setR92_crm_sub_rwa_not_cov_crm(BigDecimal r92_crm_sub_rwa_not_cov_crm) {
		this.r92_crm_sub_rwa_not_cov_crm = r92_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR92_crm_comp_col_expo_elig() {
		return r92_crm_comp_col_expo_elig;
	}
	public void setR92_crm_comp_col_expo_elig(BigDecimal r92_crm_comp_col_expo_elig) {
		this.r92_crm_comp_col_expo_elig = r92_crm_comp_col_expo_elig;
	}
	public BigDecimal getR92_crm_comp_col_elig_expo_vol_adj() {
		return r92_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR92_crm_comp_col_elig_expo_vol_adj(BigDecimal r92_crm_comp_col_elig_expo_vol_adj) {
		this.r92_crm_comp_col_elig_expo_vol_adj = r92_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR92_crm_comp_col_elig_fin_hai() {
		return r92_crm_comp_col_elig_fin_hai;
	}
	public void setR92_crm_comp_col_elig_fin_hai(BigDecimal r92_crm_comp_col_elig_fin_hai) {
		this.r92_crm_comp_col_elig_fin_hai = r92_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR92_crm_comp_col_expo_val() {
		return r92_crm_comp_col_expo_val;
	}
	public void setR92_crm_comp_col_expo_val(BigDecimal r92_crm_comp_col_expo_val) {
		this.r92_crm_comp_col_expo_val = r92_crm_comp_col_expo_val;
	}
	public BigDecimal getR92_rwa_elig_expo_not_cov_crm() {
		return r92_rwa_elig_expo_not_cov_crm;
	}
	public void setR92_rwa_elig_expo_not_cov_crm(BigDecimal r92_rwa_elig_expo_not_cov_crm) {
		this.r92_rwa_elig_expo_not_cov_crm = r92_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR92_rwa_unsec_expo_cre_ris() {
		return r92_rwa_unsec_expo_cre_ris;
	}
	public void setR92_rwa_unsec_expo_cre_ris(BigDecimal r92_rwa_unsec_expo_cre_ris) {
		this.r92_rwa_unsec_expo_cre_ris = r92_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR92_rwa_unsec_expo() {
		return r92_rwa_unsec_expo;
	}
	public void setR92_rwa_unsec_expo(BigDecimal r92_rwa_unsec_expo) {
		this.r92_rwa_unsec_expo = r92_rwa_unsec_expo;
	}
	public BigDecimal getR92_rwa_tot_ris_wei_ass() {
		return r92_rwa_tot_ris_wei_ass;
	}
	public void setR92_rwa_tot_ris_wei_ass(BigDecimal r92_rwa_tot_ris_wei_ass) {
		this.r92_rwa_tot_ris_wei_ass = r92_rwa_tot_ris_wei_ass;
	}
	public String getR93_exposure_class() {
		return r93_exposure_class;
	}
	public void setR93_exposure_class(String r93_exposure_class) {
		this.r93_exposure_class = r93_exposure_class;
	}
	public BigDecimal getR93_expo_crm() {
		return r93_expo_crm;
	}
	public void setR93_expo_crm(BigDecimal r93_expo_crm) {
		this.r93_expo_crm = r93_expo_crm;
	}
	public BigDecimal getR93_spe_pro_expo() {
		return r93_spe_pro_expo;
	}
	public void setR93_spe_pro_expo(BigDecimal r93_spe_pro_expo) {
		this.r93_spe_pro_expo = r93_spe_pro_expo;
	}
	public BigDecimal getR93_amt_elig_sht_net() {
		return r93_amt_elig_sht_net;
	}
	public void setR93_amt_elig_sht_net(BigDecimal r93_amt_elig_sht_net) {
		this.r93_amt_elig_sht_net = r93_amt_elig_sht_net;
	}
	public BigDecimal getR93_tot_expo_net_spe() {
		return r93_tot_expo_net_spe;
	}
	public void setR93_tot_expo_net_spe(BigDecimal r93_tot_expo_net_spe) {
		this.r93_tot_expo_net_spe = r93_tot_expo_net_spe;
	}
	public BigDecimal getR93_crm_sub_elig_sub_app() {
		return r93_crm_sub_elig_sub_app;
	}
	public void setR93_crm_sub_elig_sub_app(BigDecimal r93_crm_sub_elig_sub_app) {
		this.r93_crm_sub_elig_sub_app = r93_crm_sub_elig_sub_app;
	}
	public BigDecimal getR93_crm_sub_non_col_guar() {
		return r93_crm_sub_non_col_guar;
	}
	public void setR93_crm_sub_non_col_guar(BigDecimal r93_crm_sub_non_col_guar) {
		this.r93_crm_sub_non_col_guar = r93_crm_sub_non_col_guar;
	}
	public BigDecimal getR93_crm_sub_non_col_cre_der() {
		return r93_crm_sub_non_col_cre_der;
	}
	public void setR93_crm_sub_non_col_cre_der(BigDecimal r93_crm_sub_non_col_cre_der) {
		this.r93_crm_sub_non_col_cre_der = r93_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR93_crm_sub_col_elig_cash() {
		return r93_crm_sub_col_elig_cash;
	}
	public void setR93_crm_sub_col_elig_cash(BigDecimal r93_crm_sub_col_elig_cash) {
		this.r93_crm_sub_col_elig_cash = r93_crm_sub_col_elig_cash;
	}
	public BigDecimal getR93_crm_sub_col_elig_trea_bills() {
		return r93_crm_sub_col_elig_trea_bills;
	}
	public void setR93_crm_sub_col_elig_trea_bills(BigDecimal r93_crm_sub_col_elig_trea_bills) {
		this.r93_crm_sub_col_elig_trea_bills = r93_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR93_crm_sub_col_elig_deb_sec() {
		return r93_crm_sub_col_elig_deb_sec;
	}
	public void setR93_crm_sub_col_elig_deb_sec(BigDecimal r93_crm_sub_col_elig_deb_sec) {
		this.r93_crm_sub_col_elig_deb_sec = r93_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR93_crm_sub_col_elig_equi() {
		return r93_crm_sub_col_elig_equi;
	}
	public void setR93_crm_sub_col_elig_equi(BigDecimal r93_crm_sub_col_elig_equi) {
		this.r93_crm_sub_col_elig_equi = r93_crm_sub_col_elig_equi;
	}
	public BigDecimal getR93_crm_sub_col_elig_unit_tru() {
		return r93_crm_sub_col_elig_unit_tru;
	}
	public void setR93_crm_sub_col_elig_unit_tru(BigDecimal r93_crm_sub_col_elig_unit_tru) {
		this.r93_crm_sub_col_elig_unit_tru = r93_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR93_crm_sub_col_exp_cov() {
		return r93_crm_sub_col_exp_cov;
	}
	public void setR93_crm_sub_col_exp_cov(BigDecimal r93_crm_sub_col_exp_cov) {
		this.r93_crm_sub_col_exp_cov = r93_crm_sub_col_exp_cov;
	}
	public BigDecimal getR93_crm_sub_col_elig_exp_not_cov() {
		return r93_crm_sub_col_elig_exp_not_cov;
	}
	public void setR93_crm_sub_col_elig_exp_not_cov(BigDecimal r93_crm_sub_col_elig_exp_not_cov) {
		this.r93_crm_sub_col_elig_exp_not_cov = r93_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR93_crm_sub_rwa_ris_crm() {
		return r93_crm_sub_rwa_ris_crm;
	}
	public void setR93_crm_sub_rwa_ris_crm(BigDecimal r93_crm_sub_rwa_ris_crm) {
		this.r93_crm_sub_rwa_ris_crm = r93_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR93_crm_sub_rwa_cov_crm() {
		return r93_crm_sub_rwa_cov_crm;
	}
	public void setR93_crm_sub_rwa_cov_crm(BigDecimal r93_crm_sub_rwa_cov_crm) {
		this.r93_crm_sub_rwa_cov_crm = r93_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR93_crm_sub_rwa_org_cou() {
		return r93_crm_sub_rwa_org_cou;
	}
	public void setR93_crm_sub_rwa_org_cou(BigDecimal r93_crm_sub_rwa_org_cou) {
		this.r93_crm_sub_rwa_org_cou = r93_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR93_crm_sub_rwa_not_cov_crm() {
		return r93_crm_sub_rwa_not_cov_crm;
	}
	public void setR93_crm_sub_rwa_not_cov_crm(BigDecimal r93_crm_sub_rwa_not_cov_crm) {
		this.r93_crm_sub_rwa_not_cov_crm = r93_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR93_crm_comp_col_expo_elig() {
		return r93_crm_comp_col_expo_elig;
	}
	public void setR93_crm_comp_col_expo_elig(BigDecimal r93_crm_comp_col_expo_elig) {
		this.r93_crm_comp_col_expo_elig = r93_crm_comp_col_expo_elig;
	}
	public BigDecimal getR93_crm_comp_col_elig_expo_vol_adj() {
		return r93_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR93_crm_comp_col_elig_expo_vol_adj(BigDecimal r93_crm_comp_col_elig_expo_vol_adj) {
		this.r93_crm_comp_col_elig_expo_vol_adj = r93_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR93_crm_comp_col_elig_fin_hai() {
		return r93_crm_comp_col_elig_fin_hai;
	}
	public void setR93_crm_comp_col_elig_fin_hai(BigDecimal r93_crm_comp_col_elig_fin_hai) {
		this.r93_crm_comp_col_elig_fin_hai = r93_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR93_crm_comp_col_expo_val() {
		return r93_crm_comp_col_expo_val;
	}
	public void setR93_crm_comp_col_expo_val(BigDecimal r93_crm_comp_col_expo_val) {
		this.r93_crm_comp_col_expo_val = r93_crm_comp_col_expo_val;
	}
	public BigDecimal getR93_rwa_elig_expo_not_cov_crm() {
		return r93_rwa_elig_expo_not_cov_crm;
	}
	public void setR93_rwa_elig_expo_not_cov_crm(BigDecimal r93_rwa_elig_expo_not_cov_crm) {
		this.r93_rwa_elig_expo_not_cov_crm = r93_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR93_rwa_unsec_expo_cre_ris() {
		return r93_rwa_unsec_expo_cre_ris;
	}
	public void setR93_rwa_unsec_expo_cre_ris(BigDecimal r93_rwa_unsec_expo_cre_ris) {
		this.r93_rwa_unsec_expo_cre_ris = r93_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR93_rwa_unsec_expo() {
		return r93_rwa_unsec_expo;
	}
	public void setR93_rwa_unsec_expo(BigDecimal r93_rwa_unsec_expo) {
		this.r93_rwa_unsec_expo = r93_rwa_unsec_expo;
	}
	public BigDecimal getR93_rwa_tot_ris_wei_ass() {
		return r93_rwa_tot_ris_wei_ass;
	}
	public void setR93_rwa_tot_ris_wei_ass(BigDecimal r93_rwa_tot_ris_wei_ass) {
		this.r93_rwa_tot_ris_wei_ass = r93_rwa_tot_ris_wei_ass;
	}
	public String getR96_exposure_class() {
		return r96_exposure_class;
	}
	public void setR96_exposure_class(String r96_exposure_class) {
		this.r96_exposure_class = r96_exposure_class;
	}
	
	public BigDecimal getR96_spe_pro_expo() {
		return r96_spe_pro_expo;
	}
	public void setR96_spe_pro_expo(BigDecimal r96_spe_pro_expo) {
		this.r96_spe_pro_expo = r96_spe_pro_expo;
	}
	public BigDecimal getR96_amt_elig_sht_net() {
		return r96_amt_elig_sht_net;
	}
	public void setR96_amt_elig_sht_net(BigDecimal r96_amt_elig_sht_net) {
		this.r96_amt_elig_sht_net = r96_amt_elig_sht_net;
	}
	public BigDecimal getR96_tot_expo_net_spe() {
		return r96_tot_expo_net_spe;
	}
	public void setR96_tot_expo_net_spe(BigDecimal r96_tot_expo_net_spe) {
		this.r96_tot_expo_net_spe = r96_tot_expo_net_spe;
	}
	public BigDecimal getR96_crm_sub_elig_sub_app() {
		return r96_crm_sub_elig_sub_app;
	}
	public void setR96_crm_sub_elig_sub_app(BigDecimal r96_crm_sub_elig_sub_app) {
		this.r96_crm_sub_elig_sub_app = r96_crm_sub_elig_sub_app;
	}
	public BigDecimal getR96_crm_sub_non_col_guar() {
		return r96_crm_sub_non_col_guar;
	}
	public void setR96_crm_sub_non_col_guar(BigDecimal r96_crm_sub_non_col_guar) {
		this.r96_crm_sub_non_col_guar = r96_crm_sub_non_col_guar;
	}
	public BigDecimal getR96_crm_sub_non_col_cre_der() {
		return r96_crm_sub_non_col_cre_der;
	}
	public void setR96_crm_sub_non_col_cre_der(BigDecimal r96_crm_sub_non_col_cre_der) {
		this.r96_crm_sub_non_col_cre_der = r96_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR96_crm_sub_col_elig_cash() {
		return r96_crm_sub_col_elig_cash;
	}
	public void setR96_crm_sub_col_elig_cash(BigDecimal r96_crm_sub_col_elig_cash) {
		this.r96_crm_sub_col_elig_cash = r96_crm_sub_col_elig_cash;
	}
	public BigDecimal getR96_crm_sub_col_elig_trea_bills() {
		return r96_crm_sub_col_elig_trea_bills;
	}
	public void setR96_crm_sub_col_elig_trea_bills(BigDecimal r96_crm_sub_col_elig_trea_bills) {
		this.r96_crm_sub_col_elig_trea_bills = r96_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR96_crm_sub_col_elig_deb_sec() {
		return r96_crm_sub_col_elig_deb_sec;
	}
	public void setR96_crm_sub_col_elig_deb_sec(BigDecimal r96_crm_sub_col_elig_deb_sec) {
		this.r96_crm_sub_col_elig_deb_sec = r96_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR96_crm_sub_col_elig_equi() {
		return r96_crm_sub_col_elig_equi;
	}
	public void setR96_crm_sub_col_elig_equi(BigDecimal r96_crm_sub_col_elig_equi) {
		this.r96_crm_sub_col_elig_equi = r96_crm_sub_col_elig_equi;
	}
	public BigDecimal getR96_crm_sub_col_elig_unit_tru() {
		return r96_crm_sub_col_elig_unit_tru;
	}
	public void setR96_crm_sub_col_elig_unit_tru(BigDecimal r96_crm_sub_col_elig_unit_tru) {
		this.r96_crm_sub_col_elig_unit_tru = r96_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR96_crm_sub_col_exp_cov() {
		return r96_crm_sub_col_exp_cov;
	}
	public void setR96_crm_sub_col_exp_cov(BigDecimal r96_crm_sub_col_exp_cov) {
		this.r96_crm_sub_col_exp_cov = r96_crm_sub_col_exp_cov;
	}
	public BigDecimal getR96_crm_sub_col_elig_exp_not_cov() {
		return r96_crm_sub_col_elig_exp_not_cov;
	}
	public void setR96_crm_sub_col_elig_exp_not_cov(BigDecimal r96_crm_sub_col_elig_exp_not_cov) {
		this.r96_crm_sub_col_elig_exp_not_cov = r96_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR96_crm_sub_rwa_ris_crm() {
		return r96_crm_sub_rwa_ris_crm;
	}
	public void setR96_crm_sub_rwa_ris_crm(BigDecimal r96_crm_sub_rwa_ris_crm) {
		this.r96_crm_sub_rwa_ris_crm = r96_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR96_crm_sub_rwa_cov_crm() {
		return r96_crm_sub_rwa_cov_crm;
	}
	public void setR96_crm_sub_rwa_cov_crm(BigDecimal r96_crm_sub_rwa_cov_crm) {
		this.r96_crm_sub_rwa_cov_crm = r96_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR96_crm_sub_rwa_org_cou() {
		return r96_crm_sub_rwa_org_cou;
	}
	public void setR96_crm_sub_rwa_org_cou(BigDecimal r96_crm_sub_rwa_org_cou) {
		this.r96_crm_sub_rwa_org_cou = r96_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR96_crm_sub_rwa_not_cov_crm() {
		return r96_crm_sub_rwa_not_cov_crm;
	}
	public void setR96_crm_sub_rwa_not_cov_crm(BigDecimal r96_crm_sub_rwa_not_cov_crm) {
		this.r96_crm_sub_rwa_not_cov_crm = r96_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR96_crm_comp_col_expo_elig() {
		return r96_crm_comp_col_expo_elig;
	}
	public void setR96_crm_comp_col_expo_elig(BigDecimal r96_crm_comp_col_expo_elig) {
		this.r96_crm_comp_col_expo_elig = r96_crm_comp_col_expo_elig;
	}
	public BigDecimal getR96_crm_comp_col_elig_expo_vol_adj() {
		return r96_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR96_crm_comp_col_elig_expo_vol_adj(BigDecimal r96_crm_comp_col_elig_expo_vol_adj) {
		this.r96_crm_comp_col_elig_expo_vol_adj = r96_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR96_crm_comp_col_elig_fin_hai() {
		return r96_crm_comp_col_elig_fin_hai;
	}
	public void setR96_crm_comp_col_elig_fin_hai(BigDecimal r96_crm_comp_col_elig_fin_hai) {
		this.r96_crm_comp_col_elig_fin_hai = r96_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR96_crm_comp_col_expo_val() {
		return r96_crm_comp_col_expo_val;
	}
	public void setR96_crm_comp_col_expo_val(BigDecimal r96_crm_comp_col_expo_val) {
		this.r96_crm_comp_col_expo_val = r96_crm_comp_col_expo_val;
	}
	public BigDecimal getR96_rwa_elig_expo_not_cov_crm() {
		return r96_rwa_elig_expo_not_cov_crm;
	}
	public void setR96_rwa_elig_expo_not_cov_crm(BigDecimal r96_rwa_elig_expo_not_cov_crm) {
		this.r96_rwa_elig_expo_not_cov_crm = r96_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR96_rwa_unsec_expo_cre_ris() {
		return r96_rwa_unsec_expo_cre_ris;
	}
	public void setR96_rwa_unsec_expo_cre_ris(BigDecimal r96_rwa_unsec_expo_cre_ris) {
		this.r96_rwa_unsec_expo_cre_ris = r96_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR96_rwa_unsec_expo() {
		return r96_rwa_unsec_expo;
	}
	public void setR96_rwa_unsec_expo(BigDecimal r96_rwa_unsec_expo) {
		this.r96_rwa_unsec_expo = r96_rwa_unsec_expo;
	}
	public BigDecimal getR96_rwa_tot_ris_wei_ass() {
		return r96_rwa_tot_ris_wei_ass;
	}
	public void setR96_rwa_tot_ris_wei_ass(BigDecimal r96_rwa_tot_ris_wei_ass) {
		this.r96_rwa_tot_ris_wei_ass = r96_rwa_tot_ris_wei_ass;
	}
	public String getR97_exposure_class() {
		return r97_exposure_class;
	}
	public void setR97_exposure_class(String r97_exposure_class) {
		this.r97_exposure_class = r97_exposure_class;
	}
	public BigDecimal getR97_expo_crm() {
		return r97_expo_crm;
	}
	public void setR97_expo_crm(BigDecimal r97_expo_crm) {
		this.r97_expo_crm = r97_expo_crm;
	}
	public BigDecimal getR97_spe_pro_expo() {
		return r97_spe_pro_expo;
	}
	public void setR97_spe_pro_expo(BigDecimal r97_spe_pro_expo) {
		this.r97_spe_pro_expo = r97_spe_pro_expo;
	}
	public BigDecimal getR97_amt_elig_sht_net() {
		return r97_amt_elig_sht_net;
	}
	public void setR97_amt_elig_sht_net(BigDecimal r97_amt_elig_sht_net) {
		this.r97_amt_elig_sht_net = r97_amt_elig_sht_net;
	}
	public BigDecimal getR97_tot_expo_net_spe() {
		return r97_tot_expo_net_spe;
	}
	public void setR97_tot_expo_net_spe(BigDecimal r97_tot_expo_net_spe) {
		this.r97_tot_expo_net_spe = r97_tot_expo_net_spe;
	}
	public BigDecimal getR97_crm_sub_elig_sub_app() {
		return r97_crm_sub_elig_sub_app;
	}
	public void setR97_crm_sub_elig_sub_app(BigDecimal r97_crm_sub_elig_sub_app) {
		this.r97_crm_sub_elig_sub_app = r97_crm_sub_elig_sub_app;
	}
	public BigDecimal getR97_crm_sub_non_col_guar() {
		return r97_crm_sub_non_col_guar;
	}
	public void setR97_crm_sub_non_col_guar(BigDecimal r97_crm_sub_non_col_guar) {
		this.r97_crm_sub_non_col_guar = r97_crm_sub_non_col_guar;
	}
	public BigDecimal getR97_crm_sub_non_col_cre_der() {
		return r97_crm_sub_non_col_cre_der;
	}
	public void setR97_crm_sub_non_col_cre_der(BigDecimal r97_crm_sub_non_col_cre_der) {
		this.r97_crm_sub_non_col_cre_der = r97_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR97_crm_sub_col_elig_cash() {
		return r97_crm_sub_col_elig_cash;
	}
	public void setR97_crm_sub_col_elig_cash(BigDecimal r97_crm_sub_col_elig_cash) {
		this.r97_crm_sub_col_elig_cash = r97_crm_sub_col_elig_cash;
	}
	public BigDecimal getR97_crm_sub_col_elig_trea_bills() {
		return r97_crm_sub_col_elig_trea_bills;
	}
	public void setR97_crm_sub_col_elig_trea_bills(BigDecimal r97_crm_sub_col_elig_trea_bills) {
		this.r97_crm_sub_col_elig_trea_bills = r97_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR97_crm_sub_col_elig_deb_sec() {
		return r97_crm_sub_col_elig_deb_sec;
	}
	public void setR97_crm_sub_col_elig_deb_sec(BigDecimal r97_crm_sub_col_elig_deb_sec) {
		this.r97_crm_sub_col_elig_deb_sec = r97_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR97_crm_sub_col_elig_equi() {
		return r97_crm_sub_col_elig_equi;
	}
	public void setR97_crm_sub_col_elig_equi(BigDecimal r97_crm_sub_col_elig_equi) {
		this.r97_crm_sub_col_elig_equi = r97_crm_sub_col_elig_equi;
	}
	public BigDecimal getR97_crm_sub_col_elig_unit_tru() {
		return r97_crm_sub_col_elig_unit_tru;
	}
	public void setR97_crm_sub_col_elig_unit_tru(BigDecimal r97_crm_sub_col_elig_unit_tru) {
		this.r97_crm_sub_col_elig_unit_tru = r97_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR97_crm_sub_col_exp_cov() {
		return r97_crm_sub_col_exp_cov;
	}
	public void setR97_crm_sub_col_exp_cov(BigDecimal r97_crm_sub_col_exp_cov) {
		this.r97_crm_sub_col_exp_cov = r97_crm_sub_col_exp_cov;
	}
	public BigDecimal getR97_crm_sub_col_elig_exp_not_cov() {
		return r97_crm_sub_col_elig_exp_not_cov;
	}
	public void setR97_crm_sub_col_elig_exp_not_cov(BigDecimal r97_crm_sub_col_elig_exp_not_cov) {
		this.r97_crm_sub_col_elig_exp_not_cov = r97_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR97_crm_sub_rwa_ris_crm() {
		return r97_crm_sub_rwa_ris_crm;
	}
	public void setR97_crm_sub_rwa_ris_crm(BigDecimal r97_crm_sub_rwa_ris_crm) {
		this.r97_crm_sub_rwa_ris_crm = r97_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR97_crm_sub_rwa_cov_crm() {
		return r97_crm_sub_rwa_cov_crm;
	}
	public void setR97_crm_sub_rwa_cov_crm(BigDecimal r97_crm_sub_rwa_cov_crm) {
		this.r97_crm_sub_rwa_cov_crm = r97_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR97_crm_sub_rwa_org_cou() {
		return r97_crm_sub_rwa_org_cou;
	}
	public void setR97_crm_sub_rwa_org_cou(BigDecimal r97_crm_sub_rwa_org_cou) {
		this.r97_crm_sub_rwa_org_cou = r97_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR97_crm_sub_rwa_not_cov_crm() {
		return r97_crm_sub_rwa_not_cov_crm;
	}
	public void setR97_crm_sub_rwa_not_cov_crm(BigDecimal r97_crm_sub_rwa_not_cov_crm) {
		this.r97_crm_sub_rwa_not_cov_crm = r97_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR97_crm_comp_col_expo_elig() {
		return r97_crm_comp_col_expo_elig;
	}
	public void setR97_crm_comp_col_expo_elig(BigDecimal r97_crm_comp_col_expo_elig) {
		this.r97_crm_comp_col_expo_elig = r97_crm_comp_col_expo_elig;
	}
	public BigDecimal getR97_crm_comp_col_elig_expo_vol_adj() {
		return r97_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR97_crm_comp_col_elig_expo_vol_adj(BigDecimal r97_crm_comp_col_elig_expo_vol_adj) {
		this.r97_crm_comp_col_elig_expo_vol_adj = r97_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR97_crm_comp_col_elig_fin_hai() {
		return r97_crm_comp_col_elig_fin_hai;
	}
	public void setR97_crm_comp_col_elig_fin_hai(BigDecimal r97_crm_comp_col_elig_fin_hai) {
		this.r97_crm_comp_col_elig_fin_hai = r97_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR97_crm_comp_col_expo_val() {
		return r97_crm_comp_col_expo_val;
	}
	public void setR97_crm_comp_col_expo_val(BigDecimal r97_crm_comp_col_expo_val) {
		this.r97_crm_comp_col_expo_val = r97_crm_comp_col_expo_val;
	}
	public BigDecimal getR97_rwa_elig_expo_not_cov_crm() {
		return r97_rwa_elig_expo_not_cov_crm;
	}
	public void setR97_rwa_elig_expo_not_cov_crm(BigDecimal r97_rwa_elig_expo_not_cov_crm) {
		this.r97_rwa_elig_expo_not_cov_crm = r97_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR97_rwa_unsec_expo_cre_ris() {
		return r97_rwa_unsec_expo_cre_ris;
	}
	public void setR97_rwa_unsec_expo_cre_ris(BigDecimal r97_rwa_unsec_expo_cre_ris) {
		this.r97_rwa_unsec_expo_cre_ris = r97_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR97_rwa_unsec_expo() {
		return r97_rwa_unsec_expo;
	}
	public void setR97_rwa_unsec_expo(BigDecimal r97_rwa_unsec_expo) {
		this.r97_rwa_unsec_expo = r97_rwa_unsec_expo;
	}
	public BigDecimal getR97_rwa_tot_ris_wei_ass() {
		return r97_rwa_tot_ris_wei_ass;
	}
	public void setR97_rwa_tot_ris_wei_ass(BigDecimal r97_rwa_tot_ris_wei_ass) {
		this.r97_rwa_tot_ris_wei_ass = r97_rwa_tot_ris_wei_ass;
	}
	public String getR98_exposure_class() {
		return r98_exposure_class;
	}
	public void setR98_exposure_class(String r98_exposure_class) {
		this.r98_exposure_class = r98_exposure_class;
	}
	public BigDecimal getR98_expo_crm() {
		return r98_expo_crm;
	}
	public void setR98_expo_crm(BigDecimal r98_expo_crm) {
		this.r98_expo_crm = r98_expo_crm;
	}
	public BigDecimal getR98_spe_pro_expo() {
		return r98_spe_pro_expo;
	}
	public void setR98_spe_pro_expo(BigDecimal r98_spe_pro_expo) {
		this.r98_spe_pro_expo = r98_spe_pro_expo;
	}
	public BigDecimal getR98_amt_elig_sht_net() {
		return r98_amt_elig_sht_net;
	}
	public void setR98_amt_elig_sht_net(BigDecimal r98_amt_elig_sht_net) {
		this.r98_amt_elig_sht_net = r98_amt_elig_sht_net;
	}
	public BigDecimal getR98_tot_expo_net_spe() {
		return r98_tot_expo_net_spe;
	}
	public void setR98_tot_expo_net_spe(BigDecimal r98_tot_expo_net_spe) {
		this.r98_tot_expo_net_spe = r98_tot_expo_net_spe;
	}
	public BigDecimal getR98_crm_sub_elig_sub_app() {
		return r98_crm_sub_elig_sub_app;
	}
	public void setR98_crm_sub_elig_sub_app(BigDecimal r98_crm_sub_elig_sub_app) {
		this.r98_crm_sub_elig_sub_app = r98_crm_sub_elig_sub_app;
	}
	public BigDecimal getR98_crm_sub_non_col_guar() {
		return r98_crm_sub_non_col_guar;
	}
	public void setR98_crm_sub_non_col_guar(BigDecimal r98_crm_sub_non_col_guar) {
		this.r98_crm_sub_non_col_guar = r98_crm_sub_non_col_guar;
	}
	public BigDecimal getR98_crm_sub_non_col_cre_der() {
		return r98_crm_sub_non_col_cre_der;
	}
	public void setR98_crm_sub_non_col_cre_der(BigDecimal r98_crm_sub_non_col_cre_der) {
		this.r98_crm_sub_non_col_cre_der = r98_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR98_crm_sub_col_elig_cash() {
		return r98_crm_sub_col_elig_cash;
	}
	public void setR98_crm_sub_col_elig_cash(BigDecimal r98_crm_sub_col_elig_cash) {
		this.r98_crm_sub_col_elig_cash = r98_crm_sub_col_elig_cash;
	}
	public BigDecimal getR98_crm_sub_col_elig_trea_bills() {
		return r98_crm_sub_col_elig_trea_bills;
	}
	public void setR98_crm_sub_col_elig_trea_bills(BigDecimal r98_crm_sub_col_elig_trea_bills) {
		this.r98_crm_sub_col_elig_trea_bills = r98_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR98_crm_sub_col_elig_deb_sec() {
		return r98_crm_sub_col_elig_deb_sec;
	}
	public void setR98_crm_sub_col_elig_deb_sec(BigDecimal r98_crm_sub_col_elig_deb_sec) {
		this.r98_crm_sub_col_elig_deb_sec = r98_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR98_crm_sub_col_elig_equi() {
		return r98_crm_sub_col_elig_equi;
	}
	public void setR98_crm_sub_col_elig_equi(BigDecimal r98_crm_sub_col_elig_equi) {
		this.r98_crm_sub_col_elig_equi = r98_crm_sub_col_elig_equi;
	}
	public BigDecimal getR98_crm_sub_col_elig_unit_tru() {
		return r98_crm_sub_col_elig_unit_tru;
	}
	public void setR98_crm_sub_col_elig_unit_tru(BigDecimal r98_crm_sub_col_elig_unit_tru) {
		this.r98_crm_sub_col_elig_unit_tru = r98_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR98_crm_sub_col_exp_cov() {
		return r98_crm_sub_col_exp_cov;
	}
	public void setR98_crm_sub_col_exp_cov(BigDecimal r98_crm_sub_col_exp_cov) {
		this.r98_crm_sub_col_exp_cov = r98_crm_sub_col_exp_cov;
	}
	public BigDecimal getR98_crm_sub_col_elig_exp_not_cov() {
		return r98_crm_sub_col_elig_exp_not_cov;
	}
	public void setR98_crm_sub_col_elig_exp_not_cov(BigDecimal r98_crm_sub_col_elig_exp_not_cov) {
		this.r98_crm_sub_col_elig_exp_not_cov = r98_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR98_crm_sub_rwa_ris_crm() {
		return r98_crm_sub_rwa_ris_crm;
	}
	public void setR98_crm_sub_rwa_ris_crm(BigDecimal r98_crm_sub_rwa_ris_crm) {
		this.r98_crm_sub_rwa_ris_crm = r98_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR98_crm_sub_rwa_cov_crm() {
		return r98_crm_sub_rwa_cov_crm;
	}
	public void setR98_crm_sub_rwa_cov_crm(BigDecimal r98_crm_sub_rwa_cov_crm) {
		this.r98_crm_sub_rwa_cov_crm = r98_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR98_crm_sub_rwa_org_cou() {
		return r98_crm_sub_rwa_org_cou;
	}
	public void setR98_crm_sub_rwa_org_cou(BigDecimal r98_crm_sub_rwa_org_cou) {
		this.r98_crm_sub_rwa_org_cou = r98_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR98_crm_sub_rwa_not_cov_crm() {
		return r98_crm_sub_rwa_not_cov_crm;
	}
	public void setR98_crm_sub_rwa_not_cov_crm(BigDecimal r98_crm_sub_rwa_not_cov_crm) {
		this.r98_crm_sub_rwa_not_cov_crm = r98_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR98_crm_comp_col_expo_elig() {
		return r98_crm_comp_col_expo_elig;
	}
	public void setR98_crm_comp_col_expo_elig(BigDecimal r98_crm_comp_col_expo_elig) {
		this.r98_crm_comp_col_expo_elig = r98_crm_comp_col_expo_elig;
	}
	public BigDecimal getR98_crm_comp_col_elig_expo_vol_adj() {
		return r98_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR98_crm_comp_col_elig_expo_vol_adj(BigDecimal r98_crm_comp_col_elig_expo_vol_adj) {
		this.r98_crm_comp_col_elig_expo_vol_adj = r98_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR98_crm_comp_col_elig_fin_hai() {
		return r98_crm_comp_col_elig_fin_hai;
	}
	public void setR98_crm_comp_col_elig_fin_hai(BigDecimal r98_crm_comp_col_elig_fin_hai) {
		this.r98_crm_comp_col_elig_fin_hai = r98_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR98_crm_comp_col_expo_val() {
		return r98_crm_comp_col_expo_val;
	}
	public void setR98_crm_comp_col_expo_val(BigDecimal r98_crm_comp_col_expo_val) {
		this.r98_crm_comp_col_expo_val = r98_crm_comp_col_expo_val;
	}
	public BigDecimal getR98_rwa_elig_expo_not_cov_crm() {
		return r98_rwa_elig_expo_not_cov_crm;
	}
	public void setR98_rwa_elig_expo_not_cov_crm(BigDecimal r98_rwa_elig_expo_not_cov_crm) {
		this.r98_rwa_elig_expo_not_cov_crm = r98_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR98_rwa_unsec_expo_cre_ris() {
		return r98_rwa_unsec_expo_cre_ris;
	}
	public void setR98_rwa_unsec_expo_cre_ris(BigDecimal r98_rwa_unsec_expo_cre_ris) {
		this.r98_rwa_unsec_expo_cre_ris = r98_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR98_rwa_unsec_expo() {
		return r98_rwa_unsec_expo;
	}
	public void setR98_rwa_unsec_expo(BigDecimal r98_rwa_unsec_expo) {
		this.r98_rwa_unsec_expo = r98_rwa_unsec_expo;
	}
	public BigDecimal getR98_rwa_tot_ris_wei_ass() {
		return r98_rwa_tot_ris_wei_ass;
	}
	public void setR98_rwa_tot_ris_wei_ass(BigDecimal r98_rwa_tot_ris_wei_ass) {
		this.r98_rwa_tot_ris_wei_ass = r98_rwa_tot_ris_wei_ass;
	}
	public String getR99_exposure_class() {
		return r99_exposure_class;
	}
	public void setR99_exposure_class(String r99_exposure_class) {
		this.r99_exposure_class = r99_exposure_class;
	}
	public BigDecimal getR99_expo_crm() {
		return r99_expo_crm;
	}
	public void setR99_expo_crm(BigDecimal r99_expo_crm) {
		this.r99_expo_crm = r99_expo_crm;
	}
	public BigDecimal getR99_spe_pro_expo() {
		return r99_spe_pro_expo;
	}
	public void setR99_spe_pro_expo(BigDecimal r99_spe_pro_expo) {
		this.r99_spe_pro_expo = r99_spe_pro_expo;
	}
	public BigDecimal getR99_amt_elig_sht_net() {
		return r99_amt_elig_sht_net;
	}
	public void setR99_amt_elig_sht_net(BigDecimal r99_amt_elig_sht_net) {
		this.r99_amt_elig_sht_net = r99_amt_elig_sht_net;
	}
	public BigDecimal getR99_tot_expo_net_spe() {
		return r99_tot_expo_net_spe;
	}
	public void setR99_tot_expo_net_spe(BigDecimal r99_tot_expo_net_spe) {
		this.r99_tot_expo_net_spe = r99_tot_expo_net_spe;
	}
	public BigDecimal getR99_crm_sub_elig_sub_app() {
		return r99_crm_sub_elig_sub_app;
	}
	public void setR99_crm_sub_elig_sub_app(BigDecimal r99_crm_sub_elig_sub_app) {
		this.r99_crm_sub_elig_sub_app = r99_crm_sub_elig_sub_app;
	}
	public BigDecimal getR99_crm_sub_non_col_guar() {
		return r99_crm_sub_non_col_guar;
	}
	public void setR99_crm_sub_non_col_guar(BigDecimal r99_crm_sub_non_col_guar) {
		this.r99_crm_sub_non_col_guar = r99_crm_sub_non_col_guar;
	}
	public BigDecimal getR99_crm_sub_non_col_cre_der() {
		return r99_crm_sub_non_col_cre_der;
	}
	public void setR99_crm_sub_non_col_cre_der(BigDecimal r99_crm_sub_non_col_cre_der) {
		this.r99_crm_sub_non_col_cre_der = r99_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR99_crm_sub_col_elig_cash() {
		return r99_crm_sub_col_elig_cash;
	}
	public void setR99_crm_sub_col_elig_cash(BigDecimal r99_crm_sub_col_elig_cash) {
		this.r99_crm_sub_col_elig_cash = r99_crm_sub_col_elig_cash;
	}
	public BigDecimal getR99_crm_sub_col_elig_trea_bills() {
		return r99_crm_sub_col_elig_trea_bills;
	}
	public void setR99_crm_sub_col_elig_trea_bills(BigDecimal r99_crm_sub_col_elig_trea_bills) {
		this.r99_crm_sub_col_elig_trea_bills = r99_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR99_crm_sub_col_elig_deb_sec() {
		return r99_crm_sub_col_elig_deb_sec;
	}
	public void setR99_crm_sub_col_elig_deb_sec(BigDecimal r99_crm_sub_col_elig_deb_sec) {
		this.r99_crm_sub_col_elig_deb_sec = r99_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR99_crm_sub_col_elig_equi() {
		return r99_crm_sub_col_elig_equi;
	}
	public void setR99_crm_sub_col_elig_equi(BigDecimal r99_crm_sub_col_elig_equi) {
		this.r99_crm_sub_col_elig_equi = r99_crm_sub_col_elig_equi;
	}
	public BigDecimal getR99_crm_sub_col_elig_unit_tru() {
		return r99_crm_sub_col_elig_unit_tru;
	}
	public void setR99_crm_sub_col_elig_unit_tru(BigDecimal r99_crm_sub_col_elig_unit_tru) {
		this.r99_crm_sub_col_elig_unit_tru = r99_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR99_crm_sub_col_exp_cov() {
		return r99_crm_sub_col_exp_cov;
	}
	public void setR99_crm_sub_col_exp_cov(BigDecimal r99_crm_sub_col_exp_cov) {
		this.r99_crm_sub_col_exp_cov = r99_crm_sub_col_exp_cov;
	}
	public BigDecimal getR99_crm_sub_col_elig_exp_not_cov() {
		return r99_crm_sub_col_elig_exp_not_cov;
	}
	public void setR99_crm_sub_col_elig_exp_not_cov(BigDecimal r99_crm_sub_col_elig_exp_not_cov) {
		this.r99_crm_sub_col_elig_exp_not_cov = r99_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR99_crm_sub_rwa_ris_crm() {
		return r99_crm_sub_rwa_ris_crm;
	}
	public void setR99_crm_sub_rwa_ris_crm(BigDecimal r99_crm_sub_rwa_ris_crm) {
		this.r99_crm_sub_rwa_ris_crm = r99_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR99_crm_sub_rwa_cov_crm() {
		return r99_crm_sub_rwa_cov_crm;
	}
	public void setR99_crm_sub_rwa_cov_crm(BigDecimal r99_crm_sub_rwa_cov_crm) {
		this.r99_crm_sub_rwa_cov_crm = r99_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR99_crm_sub_rwa_org_cou() {
		return r99_crm_sub_rwa_org_cou;
	}
	public void setR99_crm_sub_rwa_org_cou(BigDecimal r99_crm_sub_rwa_org_cou) {
		this.r99_crm_sub_rwa_org_cou = r99_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR99_crm_sub_rwa_not_cov_crm() {
		return r99_crm_sub_rwa_not_cov_crm;
	}
	public void setR99_crm_sub_rwa_not_cov_crm(BigDecimal r99_crm_sub_rwa_not_cov_crm) {
		this.r99_crm_sub_rwa_not_cov_crm = r99_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR99_crm_comp_col_expo_elig() {
		return r99_crm_comp_col_expo_elig;
	}
	public void setR99_crm_comp_col_expo_elig(BigDecimal r99_crm_comp_col_expo_elig) {
		this.r99_crm_comp_col_expo_elig = r99_crm_comp_col_expo_elig;
	}
	public BigDecimal getR99_crm_comp_col_elig_expo_vol_adj() {
		return r99_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR99_crm_comp_col_elig_expo_vol_adj(BigDecimal r99_crm_comp_col_elig_expo_vol_adj) {
		this.r99_crm_comp_col_elig_expo_vol_adj = r99_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR99_crm_comp_col_elig_fin_hai() {
		return r99_crm_comp_col_elig_fin_hai;
	}
	public void setR99_crm_comp_col_elig_fin_hai(BigDecimal r99_crm_comp_col_elig_fin_hai) {
		this.r99_crm_comp_col_elig_fin_hai = r99_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR99_crm_comp_col_expo_val() {
		return r99_crm_comp_col_expo_val;
	}
	public void setR99_crm_comp_col_expo_val(BigDecimal r99_crm_comp_col_expo_val) {
		this.r99_crm_comp_col_expo_val = r99_crm_comp_col_expo_val;
	}
	public BigDecimal getR99_rwa_elig_expo_not_cov_crm() {
		return r99_rwa_elig_expo_not_cov_crm;
	}
	public void setR99_rwa_elig_expo_not_cov_crm(BigDecimal r99_rwa_elig_expo_not_cov_crm) {
		this.r99_rwa_elig_expo_not_cov_crm = r99_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR99_rwa_unsec_expo_cre_ris() {
		return r99_rwa_unsec_expo_cre_ris;
	}
	public void setR99_rwa_unsec_expo_cre_ris(BigDecimal r99_rwa_unsec_expo_cre_ris) {
		this.r99_rwa_unsec_expo_cre_ris = r99_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR99_rwa_unsec_expo() {
		return r99_rwa_unsec_expo;
	}
	public void setR99_rwa_unsec_expo(BigDecimal r99_rwa_unsec_expo) {
		this.r99_rwa_unsec_expo = r99_rwa_unsec_expo;
	}
	public BigDecimal getR99_rwa_tot_ris_wei_ass() {
		return r99_rwa_tot_ris_wei_ass;
	}
	public void setR99_rwa_tot_ris_wei_ass(BigDecimal r99_rwa_tot_ris_wei_ass) {
		this.r99_rwa_tot_ris_wei_ass = r99_rwa_tot_ris_wei_ass;
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
	public M_SRWA_12A_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	

}