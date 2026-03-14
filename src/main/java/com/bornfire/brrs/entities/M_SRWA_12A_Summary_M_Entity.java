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
@Table(name = "BRRS_M_SRWA_12A_SUMMARYTABLE_M")


public class M_SRWA_12A_Summary_M_Entity {
	
	
	private BigDecimal	r19_expo_crm;
	private BigDecimal	r60_expo_crm;
	private BigDecimal	r63_expo_crm;
	private BigDecimal	r87_expo_crm;
	private BigDecimal	r92_expo_crm;
	private BigDecimal	r117_expo_crm;
	private BigDecimal	r124_expo_crm;
	private BigDecimal	r129_expo_crm;
	private BigDecimal	r131_expo_crm;
	private BigDecimal	r158_nom_pri_amt;
	private BigDecimal	r96_expo_crm;
	
	//----------
	
	private String r17_exposure_class;
	private BigDecimal r17_expo_crm;
	private BigDecimal r17_spe_pro_expo;
	private BigDecimal r17_amt_elig_sht_net;
	private BigDecimal r17_tot_expo_net_spe;
	private BigDecimal r17_crm_sub_elig_sub_app;
	private BigDecimal r17_crm_sub_non_col_guar;
	private BigDecimal r17_crm_sub_non_col_cre_der;
	private BigDecimal r17_crm_sub_col_elig_cash;
	private BigDecimal r17_crm_sub_col_elig_trea_bills;
	private BigDecimal r17_crm_sub_col_elig_deb_sec;
	private BigDecimal r17_crm_sub_col_elig_equi;
	private BigDecimal r17_crm_sub_col_elig_unit_tru;
	private BigDecimal r17_crm_sub_col_exp_cov;
	private BigDecimal r17_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r17_crm_sub_rwa_ris_crm;
	private BigDecimal r17_crm_sub_rwa_cov_crm;
	private BigDecimal r17_crm_sub_rwa_org_cou;
	private BigDecimal r17_crm_sub_rwa_not_cov_crm;
	private BigDecimal r17_crm_comp_col_expo_elig;
	private BigDecimal r17_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r17_crm_comp_col_elig_fin_hai;
	private BigDecimal r17_crm_comp_col_expo_val;
	private BigDecimal r17_rwa_elig_expo_not_cov_crm;
	private BigDecimal r17_rwa_unsec_expo_cre_ris;
	private BigDecimal r17_rwa_unsec_expo;
	private BigDecimal r17_rwa_tot_ris_wei_ass;


	private String r18_exposure_class;
	private BigDecimal r18_expo_crm;
	private BigDecimal r18_spe_pro_expo;
	private BigDecimal r18_amt_elig_sht_net;
	private BigDecimal r18_tot_expo_net_spe;
	private BigDecimal r18_crm_sub_elig_sub_app;
	private BigDecimal r18_crm_sub_non_col_guar;
	private BigDecimal r18_crm_sub_non_col_cre_der;
	private BigDecimal r18_crm_sub_col_elig_cash;
	private BigDecimal r18_crm_sub_col_elig_trea_bills;
	private BigDecimal r18_crm_sub_col_elig_deb_sec;
	private BigDecimal r18_crm_sub_col_elig_equi;
	private BigDecimal r18_crm_sub_col_elig_unit_tru;
	private BigDecimal r18_crm_sub_col_exp_cov;
	private BigDecimal r18_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r18_crm_sub_rwa_ris_crm;
	private BigDecimal r18_crm_sub_rwa_cov_crm;
	private BigDecimal r18_crm_sub_rwa_org_cou;
	private BigDecimal r18_crm_sub_rwa_not_cov_crm;
	private BigDecimal r18_crm_comp_col_expo_elig;
	private BigDecimal r18_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r18_crm_comp_col_elig_fin_hai;
	private BigDecimal r18_crm_comp_col_expo_val;
	private BigDecimal r18_rwa_elig_expo_not_cov_crm;
	private BigDecimal r18_rwa_unsec_expo_cre_ris;
	private BigDecimal r18_rwa_unsec_expo;
	private BigDecimal r18_rwa_tot_ris_wei_ass;

	private String r30_exposure_class;
	private BigDecimal r30_expo_crm;
	private BigDecimal r30_spe_pro_expo;
	private BigDecimal r30_amt_elig_sht_net;
	private BigDecimal r30_tot_expo_net_spe;
	private BigDecimal r30_crm_sub_elig_sub_app;
	private BigDecimal r30_crm_sub_non_col_guar;
	private BigDecimal r30_crm_sub_non_col_cre_der;
	private BigDecimal r30_crm_sub_col_elig_cash;
	private BigDecimal r30_crm_sub_col_elig_trea_bills;
	private BigDecimal r30_crm_sub_col_elig_deb_sec;
	private BigDecimal r30_crm_sub_col_elig_equi;
	private BigDecimal r30_crm_sub_col_elig_unit_tru;
	private BigDecimal r30_crm_sub_col_exp_cov;
	private BigDecimal r30_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r30_crm_sub_rwa_ris_crm;
	private BigDecimal r30_crm_sub_rwa_cov_crm;
	private BigDecimal r30_crm_sub_rwa_org_cou;
	private BigDecimal r30_crm_sub_rwa_not_cov_crm;
	private BigDecimal r30_crm_comp_col_expo_elig;
	private BigDecimal r30_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r30_crm_comp_col_elig_fin_hai;
	private BigDecimal r30_crm_comp_col_expo_val;
	private BigDecimal r30_rwa_elig_expo_not_cov_crm;
	private BigDecimal r30_rwa_unsec_expo_cre_ris;
	private BigDecimal r30_rwa_unsec_expo;
	private BigDecimal r30_rwa_tot_ris_wei_ass;

	private String r31_exposure_class;
	private BigDecimal r31_expo_crm;
	private BigDecimal r31_spe_pro_expo;
	private BigDecimal r31_amt_elig_sht_net;
	private BigDecimal r31_tot_expo_net_spe;
	private BigDecimal r31_crm_sub_elig_sub_app;
	private BigDecimal r31_crm_sub_non_col_guar;
	private BigDecimal r31_crm_sub_non_col_cre_der;
	private BigDecimal r31_crm_sub_col_elig_cash;
	private BigDecimal r31_crm_sub_col_elig_trea_bills;
	private BigDecimal r31_crm_sub_col_elig_deb_sec;
	private BigDecimal r31_crm_sub_col_elig_equi;
	private BigDecimal r31_crm_sub_col_elig_unit_tru;
	private BigDecimal r31_crm_sub_col_exp_cov;
	private BigDecimal r31_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r31_crm_sub_rwa_ris_crm;
	private BigDecimal r31_crm_sub_rwa_cov_crm;
	private BigDecimal r31_crm_sub_rwa_org_cou;
	private BigDecimal r31_crm_sub_rwa_not_cov_crm;
	private BigDecimal r31_crm_comp_col_expo_elig;
	private BigDecimal r31_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r31_crm_comp_col_elig_fin_hai;
	private BigDecimal r31_crm_comp_col_expo_val;
	private BigDecimal r31_rwa_elig_expo_not_cov_crm;
	private BigDecimal r31_rwa_unsec_expo_cre_ris;
	private BigDecimal r31_rwa_unsec_expo;
	private BigDecimal r31_rwa_tot_ris_wei_ass;

	private String r45_exposure_class;
	private BigDecimal r45_expo_crm;
	private BigDecimal r45_spe_pro_expo;
	private BigDecimal r45_amt_elig_sht_net;
	private BigDecimal r45_tot_expo_net_spe;
	private BigDecimal r45_crm_sub_elig_sub_app;
	private BigDecimal r45_crm_sub_non_col_guar;
	private BigDecimal r45_crm_sub_non_col_cre_der;
	private BigDecimal r45_crm_sub_col_elig_cash;
	private BigDecimal r45_crm_sub_col_elig_trea_bills;
	private BigDecimal r45_crm_sub_col_elig_deb_sec;
	private BigDecimal r45_crm_sub_col_elig_equi;
	private BigDecimal r45_crm_sub_col_elig_unit_tru;
	private BigDecimal r45_crm_sub_col_exp_cov;
	private BigDecimal r45_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r45_crm_sub_rwa_ris_crm;
	private BigDecimal r45_crm_sub_rwa_cov_crm;
	private BigDecimal r45_crm_sub_rwa_org_cou;
	private BigDecimal r45_crm_sub_rwa_not_cov_crm;
	private BigDecimal r45_crm_comp_col_expo_elig;
	private BigDecimal r45_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r45_crm_comp_col_elig_fin_hai;
	private BigDecimal r45_crm_comp_col_expo_val;
	private BigDecimal r45_rwa_elig_expo_not_cov_crm;
	private BigDecimal r45_rwa_unsec_expo_cre_ris;
	private BigDecimal r45_rwa_unsec_expo;
	private BigDecimal r45_rwa_tot_ris_wei_ass;


	private String r46_exposure_class;
	private BigDecimal r46_expo_crm;
	private BigDecimal r46_spe_pro_expo;
	private BigDecimal r46_amt_elig_sht_net;
	private BigDecimal r46_tot_expo_net_spe;
	private BigDecimal r46_crm_sub_elig_sub_app;
	private BigDecimal r46_crm_sub_non_col_guar;
	private BigDecimal r46_crm_sub_non_col_cre_der;
	private BigDecimal r46_crm_sub_col_elig_cash;
	private BigDecimal r46_crm_sub_col_elig_trea_bills;
	private BigDecimal r46_crm_sub_col_elig_deb_sec;
	private BigDecimal r46_crm_sub_col_elig_equi;
	private BigDecimal r46_crm_sub_col_elig_unit_tru;
	private BigDecimal r46_crm_sub_col_exp_cov;
	private BigDecimal r46_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r46_crm_sub_rwa_ris_crm;
	private BigDecimal r46_crm_sub_rwa_cov_crm;
	private BigDecimal r46_crm_sub_rwa_org_cou;
	private BigDecimal r46_crm_sub_rwa_not_cov_crm;
	private BigDecimal r46_crm_comp_col_expo_elig;
	private BigDecimal r46_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r46_crm_comp_col_elig_fin_hai;
	private BigDecimal r46_crm_comp_col_expo_val;
	private BigDecimal r46_rwa_elig_expo_not_cov_crm;
	private BigDecimal r46_rwa_unsec_expo_cre_ris;
	private BigDecimal r46_rwa_unsec_expo;
	private BigDecimal r46_rwa_tot_ris_wei_ass;


	private String r58_exposure_class;
	private BigDecimal r58_expo_crm;
	private BigDecimal r58_spe_pro_expo;
	private BigDecimal r58_amt_elig_sht_net;
	private BigDecimal r58_tot_expo_net_spe;
	private BigDecimal r58_crm_sub_elig_sub_app;
	private BigDecimal r58_crm_sub_non_col_guar;
	private BigDecimal r58_crm_sub_non_col_cre_der;
	private BigDecimal r58_crm_sub_col_elig_cash;
	private BigDecimal r58_crm_sub_col_elig_trea_bills;
	private BigDecimal r58_crm_sub_col_elig_deb_sec;
	private BigDecimal r58_crm_sub_col_elig_equi;
	private BigDecimal r58_crm_sub_col_elig_unit_tru;
	private BigDecimal r58_crm_sub_col_exp_cov;
	private BigDecimal r58_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r58_crm_sub_rwa_ris_crm;
	private BigDecimal r58_crm_sub_rwa_cov_crm;
	private BigDecimal r58_crm_sub_rwa_org_cou;
	private BigDecimal r58_crm_sub_rwa_not_cov_crm;
	private BigDecimal r58_crm_comp_col_expo_elig;
	private BigDecimal r58_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r58_crm_comp_col_elig_fin_hai;
	private BigDecimal r58_crm_comp_col_expo_val;
	private BigDecimal r58_rwa_elig_expo_not_cov_crm;
	private BigDecimal r58_rwa_unsec_expo_cre_ris;
	private BigDecimal r58_rwa_unsec_expo;
	private BigDecimal r58_rwa_tot_ris_wei_ass;


	private String r59_exposure_class;
	private BigDecimal r59_expo_crm;
	private BigDecimal r59_spe_pro_expo;
	private BigDecimal r59_amt_elig_sht_net;
	private BigDecimal r59_tot_expo_net_spe;
	private BigDecimal r59_crm_sub_elig_sub_app;
	private BigDecimal r59_crm_sub_non_col_guar;
	private BigDecimal r59_crm_sub_non_col_cre_der;
	private BigDecimal r59_crm_sub_col_elig_cash;
	private BigDecimal r59_crm_sub_col_elig_trea_bills;
	private BigDecimal r59_crm_sub_col_elig_deb_sec;
	private BigDecimal r59_crm_sub_col_elig_equi;
	private BigDecimal r59_crm_sub_col_elig_unit_tru;
	private BigDecimal r59_crm_sub_col_exp_cov;
	private BigDecimal r59_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r59_crm_sub_rwa_ris_crm;
	private BigDecimal r59_crm_sub_rwa_cov_crm;
	private BigDecimal r59_crm_sub_rwa_org_cou;
	private BigDecimal r59_crm_sub_rwa_not_cov_crm;
	private BigDecimal r59_crm_comp_col_expo_elig;
	private BigDecimal r59_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r59_crm_comp_col_elig_fin_hai;
	private BigDecimal r59_crm_comp_col_expo_val;
	private BigDecimal r59_rwa_elig_expo_not_cov_crm;
	private BigDecimal r59_rwa_unsec_expo_cre_ris;
	private BigDecimal r59_rwa_unsec_expo;
	private BigDecimal r59_rwa_tot_ris_wei_ass;

	private String r71_exposure_class;
	private BigDecimal r71_expo_crm;
	private BigDecimal r71_spe_pro_expo;
	private BigDecimal r71_amt_elig_sht_net;
	private BigDecimal r71_tot_expo_net_spe;
	private BigDecimal r71_crm_sub_elig_sub_app;
	private BigDecimal r71_crm_sub_non_col_guar;
	private BigDecimal r71_crm_sub_non_col_cre_der;
	private BigDecimal r71_crm_sub_col_elig_cash;
	private BigDecimal r71_crm_sub_col_elig_trea_bills;
	private BigDecimal r71_crm_sub_col_elig_deb_sec;
	private BigDecimal r71_crm_sub_col_elig_equi;
	private BigDecimal r71_crm_sub_col_elig_unit_tru;
	private BigDecimal r71_crm_sub_col_exp_cov;
	private BigDecimal r71_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r71_crm_sub_rwa_ris_crm;
	private BigDecimal r71_crm_sub_rwa_cov_crm;
	private BigDecimal r71_crm_sub_rwa_org_cou;
	private BigDecimal r71_crm_sub_rwa_not_cov_crm;
	private BigDecimal r71_crm_comp_col_expo_elig;
	private BigDecimal r71_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r71_crm_comp_col_elig_fin_hai;
	private BigDecimal r71_crm_comp_col_expo_val;
	private BigDecimal r71_rwa_elig_expo_not_cov_crm;
	private BigDecimal r71_rwa_unsec_expo_cre_ris;
	private BigDecimal r71_rwa_unsec_expo;
	private BigDecimal r71_rwa_tot_ris_wei_ass;


	private String r72_exposure_class;
	private BigDecimal r72_expo_crm;
	private BigDecimal r72_spe_pro_expo;
	private BigDecimal r72_amt_elig_sht_net;
	private BigDecimal r72_tot_expo_net_spe;
	private BigDecimal r72_crm_sub_elig_sub_app;
	private BigDecimal r72_crm_sub_non_col_guar;
	private BigDecimal r72_crm_sub_non_col_cre_der;
	private BigDecimal r72_crm_sub_col_elig_cash;
	private BigDecimal r72_crm_sub_col_elig_trea_bills;
	private BigDecimal r72_crm_sub_col_elig_deb_sec;
	private BigDecimal r72_crm_sub_col_elig_equi;
	private BigDecimal r72_crm_sub_col_elig_unit_tru;
	private BigDecimal r72_crm_sub_col_exp_cov;
	private BigDecimal r72_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r72_crm_sub_rwa_ris_crm;
	private BigDecimal r72_crm_sub_rwa_cov_crm;
	private BigDecimal r72_crm_sub_rwa_org_cou;
	private BigDecimal r72_crm_sub_rwa_not_cov_crm;
	private BigDecimal r72_crm_comp_col_expo_elig;
	private BigDecimal r72_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r72_crm_comp_col_elig_fin_hai;
	private BigDecimal r72_crm_comp_col_expo_val;
	private BigDecimal r72_rwa_elig_expo_not_cov_crm;
	private BigDecimal r72_rwa_unsec_expo_cre_ris;
	private BigDecimal r72_rwa_unsec_expo;
	private BigDecimal r72_rwa_tot_ris_wei_ass;


	private String r83_exposure_class;
	private BigDecimal r83_expo_crm;
	private BigDecimal r83_spe_pro_expo;
	private BigDecimal r83_amt_elig_sht_net;
	private BigDecimal r83_tot_expo_net_spe;
	private BigDecimal r83_crm_sub_elig_sub_app;
	private BigDecimal r83_crm_sub_non_col_guar;
	private BigDecimal r83_crm_sub_non_col_cre_der;
	private BigDecimal r83_crm_sub_col_elig_cash;
	private BigDecimal r83_crm_sub_col_elig_trea_bills;
	private BigDecimal r83_crm_sub_col_elig_deb_sec;
	private BigDecimal r83_crm_sub_col_elig_equi;
	private BigDecimal r83_crm_sub_col_elig_unit_tru;
	private BigDecimal r83_crm_sub_col_exp_cov;
	private BigDecimal r83_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r83_crm_sub_rwa_ris_crm;
	private BigDecimal r83_crm_sub_rwa_cov_crm;
	private BigDecimal r83_crm_sub_rwa_org_cou;
	private BigDecimal r83_crm_sub_rwa_not_cov_crm;
	private BigDecimal r83_crm_comp_col_expo_elig;
	private BigDecimal r83_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r83_crm_comp_col_elig_fin_hai;
	private BigDecimal r83_crm_comp_col_expo_val;
	private BigDecimal r83_rwa_elig_expo_not_cov_crm;
	private BigDecimal r83_rwa_unsec_expo_cre_ris;
	private BigDecimal r83_rwa_unsec_expo;
	private BigDecimal r83_rwa_tot_ris_wei_ass;


	private String r84_exposure_class;
	private BigDecimal r84_expo_crm;
	private BigDecimal r84_spe_pro_expo;
	private BigDecimal r84_amt_elig_sht_net;
	private BigDecimal r84_tot_expo_net_spe;
	private BigDecimal r84_crm_sub_elig_sub_app;
	private BigDecimal r84_crm_sub_non_col_guar;
	private BigDecimal r84_crm_sub_non_col_cre_der;
	private BigDecimal r84_crm_sub_col_elig_cash;
	private BigDecimal r84_crm_sub_col_elig_trea_bills;
	private BigDecimal r84_crm_sub_col_elig_deb_sec;
	private BigDecimal r84_crm_sub_col_elig_equi;
	private BigDecimal r84_crm_sub_col_elig_unit_tru;
	private BigDecimal r84_crm_sub_col_exp_cov;
	private BigDecimal r84_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r84_crm_sub_rwa_ris_crm;
	private BigDecimal r84_crm_sub_rwa_cov_crm;
	private BigDecimal r84_crm_sub_rwa_org_cou;
	private BigDecimal r84_crm_sub_rwa_not_cov_crm;
	private BigDecimal r84_crm_comp_col_expo_elig;
	private BigDecimal r84_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r84_crm_comp_col_elig_fin_hai;
	private BigDecimal r84_crm_comp_col_expo_val;
	private BigDecimal r84_rwa_elig_expo_not_cov_crm;
	private BigDecimal r84_rwa_unsec_expo_cre_ris;
	private BigDecimal r84_rwa_unsec_expo;
	private BigDecimal r84_rwa_tot_ris_wei_ass;



	private String r94_exposure_class;
	private BigDecimal r94_expo_crm;
	private BigDecimal r94_spe_pro_expo;
	private BigDecimal r94_amt_elig_sht_net;
	private BigDecimal r94_tot_expo_net_spe;
	private BigDecimal r94_crm_sub_elig_sub_app;
	private BigDecimal r94_crm_sub_non_col_guar;
	private BigDecimal r94_crm_sub_non_col_cre_der;
	private BigDecimal r94_crm_sub_col_elig_cash;
	private BigDecimal r94_crm_sub_col_elig_trea_bills;
	private BigDecimal r94_crm_sub_col_elig_deb_sec;
	private BigDecimal r94_crm_sub_col_elig_equi;
	private BigDecimal r94_crm_sub_col_elig_unit_tru;
	private BigDecimal r94_crm_sub_col_exp_cov;
	private BigDecimal r94_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r94_crm_sub_rwa_ris_crm;
	private BigDecimal r94_crm_sub_rwa_cov_crm;
	private BigDecimal r94_crm_sub_rwa_org_cou;
	private BigDecimal r94_crm_sub_rwa_not_cov_crm;
	private BigDecimal r94_crm_comp_col_expo_elig;
	private BigDecimal r94_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r94_crm_comp_col_elig_fin_hai;
	private BigDecimal r94_crm_comp_col_expo_val;
	private BigDecimal r94_rwa_elig_expo_not_cov_crm;
	private BigDecimal r94_rwa_unsec_expo_cre_ris;
	private BigDecimal r94_rwa_unsec_expo;
	private BigDecimal r94_rwa_tot_ris_wei_ass;


	private String r95_exposure_class;
	private BigDecimal r95_expo_crm;
	private BigDecimal r95_spe_pro_expo;
	private BigDecimal r95_amt_elig_sht_net;
	private BigDecimal r95_tot_expo_net_spe;
	private BigDecimal r95_crm_sub_elig_sub_app;
	private BigDecimal r95_crm_sub_non_col_guar;
	private BigDecimal r95_crm_sub_non_col_cre_der;
	private BigDecimal r95_crm_sub_col_elig_cash;
	private BigDecimal r95_crm_sub_col_elig_trea_bills;
	private BigDecimal r95_crm_sub_col_elig_deb_sec;
	private BigDecimal r95_crm_sub_col_elig_equi;
	private BigDecimal r95_crm_sub_col_elig_unit_tru;
	private BigDecimal r95_crm_sub_col_exp_cov;
	private BigDecimal r95_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r95_crm_sub_rwa_ris_crm;
	private BigDecimal r95_crm_sub_rwa_cov_crm;
	private BigDecimal r95_crm_sub_rwa_org_cou;
	private BigDecimal r95_crm_sub_rwa_not_cov_crm;
	private BigDecimal r95_crm_comp_col_expo_elig;
	private BigDecimal r95_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r95_crm_comp_col_elig_fin_hai;
	private BigDecimal r95_crm_comp_col_expo_val;
	private BigDecimal r95_rwa_elig_expo_not_cov_crm;
	private BigDecimal r95_rwa_unsec_expo_cre_ris;
	private BigDecimal r95_rwa_unsec_expo;
	private BigDecimal r95_rwa_tot_ris_wei_ass;


	private String r103_exposure_class;
	private BigDecimal r103_expo_crm;
	private BigDecimal r103_spe_pro_expo;
	private BigDecimal r103_amt_elig_sht_net;
	private BigDecimal r103_tot_expo_net_spe;
	private BigDecimal r103_crm_sub_elig_sub_app;
	private BigDecimal r103_crm_sub_non_col_guar;
	private BigDecimal r103_crm_sub_non_col_cre_der;
	private BigDecimal r103_crm_sub_col_elig_cash;
	private BigDecimal r103_crm_sub_col_elig_trea_bills;
	private BigDecimal r103_crm_sub_col_elig_deb_sec;
	private BigDecimal r103_crm_sub_col_elig_equi;
	private BigDecimal r103_crm_sub_col_elig_unit_tru;
	private BigDecimal r103_crm_sub_col_exp_cov;
	private BigDecimal r103_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r103_crm_sub_rwa_ris_crm;
	private BigDecimal r103_crm_sub_rwa_cov_crm;
	private BigDecimal r103_crm_sub_rwa_org_cou;
	private BigDecimal r103_crm_sub_rwa_not_cov_crm;
	private BigDecimal r103_crm_comp_col_expo_elig;
	private BigDecimal r103_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r103_crm_comp_col_elig_fin_hai;
	private BigDecimal r103_crm_comp_col_expo_val;
	private BigDecimal r103_rwa_elig_expo_not_cov_crm;
	private BigDecimal r103_rwa_unsec_expo_cre_ris;
	private BigDecimal r103_rwa_unsec_expo;
	private BigDecimal r103_rwa_tot_ris_wei_ass;


	private String r104_exposure_class;
	private BigDecimal r104_expo_crm;
	private BigDecimal r104_spe_pro_expo;
	private BigDecimal r104_amt_elig_sht_net;
	private BigDecimal r104_tot_expo_net_spe;
	private BigDecimal r104_crm_sub_elig_sub_app;
	private BigDecimal r104_crm_sub_non_col_guar;
	private BigDecimal r104_crm_sub_non_col_cre_der;
	private BigDecimal r104_crm_sub_col_elig_cash;
	private BigDecimal r104_crm_sub_col_elig_trea_bills;
	private BigDecimal r104_crm_sub_col_elig_deb_sec;
	private BigDecimal r104_crm_sub_col_elig_equi;
	private BigDecimal r104_crm_sub_col_elig_unit_tru;
	private BigDecimal r104_crm_sub_col_exp_cov;
	private BigDecimal r104_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r104_crm_sub_rwa_ris_crm;
	private BigDecimal r104_crm_sub_rwa_cov_crm;
	private BigDecimal r104_crm_sub_rwa_org_cou;
	private BigDecimal r104_crm_sub_rwa_not_cov_crm;
	private BigDecimal r104_crm_comp_col_expo_elig;
	private BigDecimal r104_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r104_crm_comp_col_elig_fin_hai;
	private BigDecimal r104_crm_comp_col_expo_val;
	private BigDecimal r104_rwa_elig_expo_not_cov_crm;
	private BigDecimal r104_rwa_unsec_expo_cre_ris;
	private BigDecimal r104_rwa_unsec_expo;
	private BigDecimal r104_rwa_tot_ris_wei_ass;

	private String r115_exposure_class;
	private BigDecimal r115_expo_crm;
	private BigDecimal r115_spe_pro_expo;
	private BigDecimal r115_amt_elig_sht_net;
	private BigDecimal r115_tot_expo_net_spe;
	private BigDecimal r115_crm_sub_elig_sub_app;
	private BigDecimal r115_crm_sub_non_col_guar;
	private BigDecimal r115_crm_sub_non_col_cre_der;
	private BigDecimal r115_crm_sub_col_elig_cash;
	private BigDecimal r115_crm_sub_col_elig_trea_bills;
	private BigDecimal r115_crm_sub_col_elig_deb_sec;
	private BigDecimal r115_crm_sub_col_elig_equi;
	private BigDecimal r115_crm_sub_col_elig_unit_tru;
	private BigDecimal r115_crm_sub_col_exp_cov;
	private BigDecimal r115_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r115_crm_sub_rwa_ris_crm;
	private BigDecimal r115_crm_sub_rwa_cov_crm;
	private BigDecimal r115_crm_sub_rwa_org_cou;
	private BigDecimal r115_crm_sub_rwa_not_cov_crm;
	private BigDecimal r115_crm_comp_col_expo_elig;
	private BigDecimal r115_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r115_crm_comp_col_elig_fin_hai;
	private BigDecimal r115_crm_comp_col_expo_val;
	private BigDecimal r115_rwa_elig_expo_not_cov_crm;
	private BigDecimal r115_rwa_unsec_expo_cre_ris;
	private BigDecimal r115_rwa_unsec_expo;
	private BigDecimal r115_rwa_tot_ris_wei_ass;


	private String r116_exposure_class;
	private BigDecimal r116_expo_crm;
	private BigDecimal r116_spe_pro_expo;
	private BigDecimal r116_amt_elig_sht_net;
	private BigDecimal r116_tot_expo_net_spe;
	private BigDecimal r116_crm_sub_elig_sub_app;
	private BigDecimal r116_crm_sub_non_col_guar;
	private BigDecimal r116_crm_sub_non_col_cre_der;
	private BigDecimal r116_crm_sub_col_elig_cash;
	private BigDecimal r116_crm_sub_col_elig_trea_bills;
	private BigDecimal r116_crm_sub_col_elig_deb_sec;
	private BigDecimal r116_crm_sub_col_elig_equi;
	private BigDecimal r116_crm_sub_col_elig_unit_tru;
	private BigDecimal r116_crm_sub_col_exp_cov;
	private BigDecimal r116_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r116_crm_sub_rwa_ris_crm;
	private BigDecimal r116_crm_sub_rwa_cov_crm;
	private BigDecimal r116_crm_sub_rwa_org_cou;
	private BigDecimal r116_crm_sub_rwa_not_cov_crm;
	private BigDecimal r116_crm_comp_col_expo_elig;
	private BigDecimal r116_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r116_crm_comp_col_elig_fin_hai;
	private BigDecimal r116_crm_comp_col_expo_val;
	private BigDecimal r116_rwa_elig_expo_not_cov_crm;
	private BigDecimal r116_rwa_unsec_expo_cre_ris;
	private BigDecimal r116_rwa_unsec_expo;
	private BigDecimal r116_rwa_tot_ris_wei_ass;


	private String r123_exposure_class;
	private BigDecimal r123_expo_crm;
	private BigDecimal r123_spe_pro_expo;
	private BigDecimal r123_amt_elig_sht_net;
	private BigDecimal r123_tot_expo_net_spe;
	private BigDecimal r123_crm_sub_elig_sub_app;
	private BigDecimal r123_crm_sub_non_col_guar;
	private BigDecimal r123_crm_sub_non_col_cre_der;
	private BigDecimal r123_crm_sub_col_elig_cash;
	private BigDecimal r123_crm_sub_col_elig_trea_bills;
	private BigDecimal r123_crm_sub_col_elig_deb_sec;
	private BigDecimal r123_crm_sub_col_elig_equi;
	private BigDecimal r123_crm_sub_col_elig_unit_tru;
	private BigDecimal r123_crm_sub_col_exp_cov;
	private BigDecimal r123_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r123_crm_sub_rwa_ris_crm;
	private BigDecimal r123_crm_sub_rwa_cov_crm;
	private BigDecimal r123_crm_sub_rwa_org_cou;
	private BigDecimal r123_crm_sub_rwa_not_cov_crm;
	private BigDecimal r123_crm_comp_col_expo_elig;
	private BigDecimal r123_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r123_crm_comp_col_elig_fin_hai;
	private BigDecimal r123_crm_comp_col_expo_val;
	private BigDecimal r123_rwa_elig_expo_not_cov_crm;
	private BigDecimal r123_rwa_unsec_expo_cre_ris;
	private BigDecimal r123_rwa_unsec_expo;
	private BigDecimal r123_rwa_tot_ris_wei_ass;

	private String r134_exposure_class;
	private BigDecimal r134_expo_crm;
	private BigDecimal r134_spe_pro_expo;
	private BigDecimal r134_amt_elig_sht_net;
	private BigDecimal r134_tot_expo_net_spe;
	private BigDecimal r134_crm_sub_elig_sub_app;
	private BigDecimal r134_crm_sub_non_col_guar;
	private BigDecimal r134_crm_sub_non_col_cre_der;
	private BigDecimal r134_crm_sub_col_elig_cash;
	private BigDecimal r134_crm_sub_col_elig_trea_bills;
	private BigDecimal r134_crm_sub_col_elig_deb_sec;
	private BigDecimal r134_crm_sub_col_elig_equi;
	private BigDecimal r134_crm_sub_col_elig_unit_tru;
	private BigDecimal r134_crm_sub_col_exp_cov;
	private BigDecimal r134_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r134_crm_sub_rwa_ris_crm;
	private BigDecimal r134_crm_sub_rwa_cov_crm;
	private BigDecimal r134_crm_sub_rwa_org_cou;
	private BigDecimal r134_crm_sub_rwa_not_cov_crm;
	private BigDecimal r134_crm_comp_col_expo_elig;
	private BigDecimal r134_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r134_crm_comp_col_elig_fin_hai;
	private BigDecimal r134_crm_comp_col_expo_val;
	private BigDecimal r134_rwa_elig_expo_not_cov_crm;
	private BigDecimal r134_rwa_unsec_expo_cre_ris;
	private BigDecimal r134_rwa_unsec_expo;
	private BigDecimal r134_rwa_tot_ris_wei_ass;


	private String r135_exposure_class;
	private BigDecimal r135_expo_crm;
	private BigDecimal r135_spe_pro_expo;
	private BigDecimal r135_amt_elig_sht_net;
	private BigDecimal r135_tot_expo_net_spe;
	private BigDecimal r135_crm_sub_elig_sub_app;
	private BigDecimal r135_crm_sub_non_col_guar;
	private BigDecimal r135_crm_sub_non_col_cre_der;
	private BigDecimal r135_crm_sub_col_elig_cash;
	private BigDecimal r135_crm_sub_col_elig_trea_bills;
	private BigDecimal r135_crm_sub_col_elig_deb_sec;
	private BigDecimal r135_crm_sub_col_elig_equi;
	private BigDecimal r135_crm_sub_col_elig_unit_tru;
	private BigDecimal r135_crm_sub_col_exp_cov;
	private BigDecimal r135_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r135_crm_sub_rwa_ris_crm;
	private BigDecimal r135_crm_sub_rwa_cov_crm;
	private BigDecimal r135_crm_sub_rwa_org_cou;
	private BigDecimal r135_crm_sub_rwa_not_cov_crm;
	private BigDecimal r135_crm_comp_col_expo_elig;
	private BigDecimal r135_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r135_crm_comp_col_elig_fin_hai;
	private BigDecimal r135_crm_comp_col_expo_val;
	private BigDecimal r135_rwa_elig_expo_not_cov_crm;
	private BigDecimal r135_rwa_unsec_expo_cre_ris;
	private BigDecimal r135_rwa_unsec_expo;
	private BigDecimal r135_rwa_tot_ris_wei_ass;

	private String r136_exposure_class;
	private BigDecimal r136_expo_crm;
	private BigDecimal r136_spe_pro_expo;
	private BigDecimal r136_amt_elig_sht_net;
	private BigDecimal r136_tot_expo_net_spe;
	private BigDecimal r136_crm_sub_elig_sub_app;
	private BigDecimal r136_crm_sub_non_col_guar;
	private BigDecimal r136_crm_sub_non_col_cre_der;
	private BigDecimal r136_crm_sub_col_elig_cash;
	private BigDecimal r136_crm_sub_col_elig_trea_bills;
	private BigDecimal r136_crm_sub_col_elig_deb_sec;
	private BigDecimal r136_crm_sub_col_elig_equi;
	private BigDecimal r136_crm_sub_col_elig_unit_tru;
	private BigDecimal r136_crm_sub_col_exp_cov;
	private BigDecimal r136_crm_sub_col_elig_exp_not_cov;
	private BigDecimal r136_crm_sub_rwa_ris_crm;
	private BigDecimal r136_crm_sub_rwa_cov_crm;
	private BigDecimal r136_crm_sub_rwa_org_cou;
	private BigDecimal r136_crm_sub_rwa_not_cov_crm;
	private BigDecimal r136_crm_comp_col_expo_elig;
	private BigDecimal r136_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal r136_crm_comp_col_elig_fin_hai;
	private BigDecimal r136_crm_comp_col_expo_val;
	private BigDecimal r136_rwa_elig_expo_not_cov_crm;
	private BigDecimal r136_rwa_unsec_expo_cre_ris;
	private BigDecimal r136_rwa_unsec_expo;
	private BigDecimal r136_rwa_tot_ris_wei_ass;


	private String r144_exposure_class_off_bal;
	private BigDecimal r144_nom_pri_amt;
	private BigDecimal r144_ccf;
	private BigDecimal r144_cea;
	private BigDecimal r144_cea_elig_coun_bilt_net;
	private BigDecimal r144_cea_aft_net;
	private BigDecimal r144_crm_sub_app_cea_elig;
	private BigDecimal r144_crm_sub_app_non_col_guar_elig;
	private BigDecimal r144_crm_sub_app_non_col_cre_der;
	private BigDecimal r144_crm_sub_app_col_elig_cash;
	private BigDecimal r144_crm_sub_app_col_elig_tre_bills;
	private BigDecimal r144_crm_sub_app_col_elig_deb_sec;
	private BigDecimal r144_crm_sub_app_col_elig_euiq;
	private BigDecimal r144_crm_sub_app_col_elig_uni_tru;
	private BigDecimal r144_crm_sub_app_col_cea_cov;
	private BigDecimal r144_crm_sub_app_col_cea_not_cov;
	private BigDecimal r144_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal r144_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal r144_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal r144_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal r144_crm_com_app_col_cea_elig_crm;
	private BigDecimal r144_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal r144_crm_com_app_col_elig_fin_hai;
	private BigDecimal r144_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal r144_rwa_elig_cea_not_cov;
	private BigDecimal r144_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal r144_rwa_unsec_cea;
	private BigDecimal r144_rwa_tot_ris_wei_ass;



	private BigDecimal r251_nom_pri_amt;
	private BigDecimal r251_ccf;
	private BigDecimal r251_cea;
	private BigDecimal r251_cea_elig_coun_bilt_net;
	private BigDecimal r251_cea_aft_net;
	private BigDecimal r251_crm_sub_app_cea_elig;
	private BigDecimal r251_crm_sub_app_non_col_guar_elig;
	private BigDecimal r251_crm_sub_app_non_col_cre_der;
	private BigDecimal r251_crm_sub_app_col_elig_cash;
	private BigDecimal r251_crm_sub_app_col_elig_tre_bills;
	private BigDecimal r251_crm_sub_app_col_elig_deb_sec;
	private BigDecimal r251_crm_sub_app_col_elig_euiq;
	private BigDecimal r251_crm_sub_app_col_elig_uni_tru;
	private BigDecimal r251_crm_sub_app_col_cea_cov;
	private BigDecimal r251_crm_sub_app_col_cea_not_cov;
	private BigDecimal r251_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal r251_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal r251_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal r251_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal r251_crm_com_app_col_cea_elig_crm;
	private BigDecimal r251_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal r251_crm_com_app_col_elig_fin_hai;
	private BigDecimal r251_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal r251_rwa_elig_cea_not_cov;
	private BigDecimal r251_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal r251_rwa_unsec_cea;



	private BigDecimal r252_nom_pri_amt;
	private BigDecimal r252_ccf;
	private BigDecimal r252_cea;
	private BigDecimal r252_cea_elig_coun_bilt_net;
	private BigDecimal r252_cea_aft_net;
	private BigDecimal r252_crm_sub_app_cea_elig;
	private BigDecimal r252_crm_sub_app_non_col_guar_elig;
	private BigDecimal r252_crm_sub_app_non_col_cre_der;
	private BigDecimal r252_crm_sub_app_col_elig_cash;
	private BigDecimal r252_crm_sub_app_col_elig_tre_bills;
	private BigDecimal r252_crm_sub_app_col_elig_deb_sec;
	private BigDecimal r252_crm_sub_app_col_elig_euiq;
	private BigDecimal r252_crm_sub_app_col_elig_uni_tru;
	private BigDecimal r252_crm_sub_app_col_cea_cov;
	private BigDecimal r252_crm_sub_app_col_cea_not_cov;
	private BigDecimal r252_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal r252_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal r252_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal r252_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal r252_crm_com_app_col_cea_elig_crm;
	private BigDecimal r252_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal r252_crm_com_app_col_elig_fin_hai;
	private BigDecimal r252_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal r252_rwa_elig_cea_not_cov;
	private BigDecimal r252_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal r252_rwa_unsec_cea;
	
	
	private BigDecimal r254_nom_pri_amt;
	private BigDecimal r254_ccf;
	private BigDecimal r254_cea;
	private BigDecimal r254_cea_elig_coun_bilt_net;
	private BigDecimal r254_cea_aft_net;
	private BigDecimal r254_crm_sub_app_cea_elig;
	private BigDecimal r254_crm_sub_app_non_col_guar_elig;
	private BigDecimal r254_crm_sub_app_non_col_cre_der;
	private BigDecimal r254_crm_sub_app_col_elig_cash;
	private BigDecimal r254_crm_sub_app_col_elig_tre_bills;
	private BigDecimal r254_crm_sub_app_col_elig_deb_sec;
	private BigDecimal r254_crm_sub_app_col_elig_euiq;
	private BigDecimal r254_crm_sub_app_col_elig_uni_tru;
	private BigDecimal r254_crm_sub_app_col_cea_cov;
	private BigDecimal r254_crm_sub_app_col_cea_not_cov;
	private BigDecimal r254_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal r254_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal r254_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal r254_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal r254_crm_com_app_col_cea_elig_crm;
	private BigDecimal r254_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal r254_crm_com_app_col_elig_fin_hai;
	private BigDecimal r254_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal r254_rwa_elig_cea_not_cov;
	private BigDecimal r254_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal r254_rwa_unsec_cea;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public BigDecimal getR19_expo_crm() {
		return r19_expo_crm;
	}
	public void setR19_expo_crm(BigDecimal r19_expo_crm) {
		this.r19_expo_crm = r19_expo_crm;
	}
	public BigDecimal getR60_expo_crm() {
		return r60_expo_crm;
	}
	public void setR60_expo_crm(BigDecimal r60_expo_crm) {
		this.r60_expo_crm = r60_expo_crm;
	}
	public BigDecimal getR63_expo_crm() {
		return r63_expo_crm;
	}
	public void setR63_expo_crm(BigDecimal r63_expo_crm) {
		this.r63_expo_crm = r63_expo_crm;
	}
	public BigDecimal getR87_expo_crm() {
		return r87_expo_crm;
	}
	public void setR87_expo_crm(BigDecimal r87_expo_crm) {
		this.r87_expo_crm = r87_expo_crm;
	}
	public BigDecimal getR92_expo_crm() {
		return r92_expo_crm;
	}
	public void setR92_expo_crm(BigDecimal r92_expo_crm) {
		this.r92_expo_crm = r92_expo_crm;
	}
	public BigDecimal getR117_expo_crm() {
		return r117_expo_crm;
	}
	public void setR117_expo_crm(BigDecimal r117_expo_crm) {
		this.r117_expo_crm = r117_expo_crm;
	}
	public BigDecimal getR124_expo_crm() {
		return r124_expo_crm;
	}
	public void setR124_expo_crm(BigDecimal r124_expo_crm) {
		this.r124_expo_crm = r124_expo_crm;
	}
	public BigDecimal getR129_expo_crm() {
		return r129_expo_crm;
	}
	public void setR129_expo_crm(BigDecimal r129_expo_crm) {
		this.r129_expo_crm = r129_expo_crm;
	}
	public BigDecimal getR131_expo_crm() {
		return r131_expo_crm;
	}
	public void setR131_expo_crm(BigDecimal r131_expo_crm) {
		this.r131_expo_crm = r131_expo_crm;
	}
	public BigDecimal getR158_nom_pri_amt() {
		return r158_nom_pri_amt;
	}
	public void setR158_nom_pri_amt(BigDecimal r158_nom_pri_amt) {
		this.r158_nom_pri_amt = r158_nom_pri_amt;
	}
	public BigDecimal getR96_expo_crm() {
		return r96_expo_crm;
	}
	public void setR96_expo_crm(BigDecimal r96_expo_crm) {
		this.r96_expo_crm = r96_expo_crm;
	}
	public String getR17_exposure_class() {
		return r17_exposure_class;
	}
	public void setR17_exposure_class(String r17_exposure_class) {
		this.r17_exposure_class = r17_exposure_class;
	}
	public BigDecimal getR17_expo_crm() {
		return r17_expo_crm;
	}
	public void setR17_expo_crm(BigDecimal r17_expo_crm) {
		this.r17_expo_crm = r17_expo_crm;
	}
	public BigDecimal getR17_spe_pro_expo() {
		return r17_spe_pro_expo;
	}
	public void setR17_spe_pro_expo(BigDecimal r17_spe_pro_expo) {
		this.r17_spe_pro_expo = r17_spe_pro_expo;
	}
	public BigDecimal getR17_amt_elig_sht_net() {
		return r17_amt_elig_sht_net;
	}
	public void setR17_amt_elig_sht_net(BigDecimal r17_amt_elig_sht_net) {
		this.r17_amt_elig_sht_net = r17_amt_elig_sht_net;
	}
	public BigDecimal getR17_tot_expo_net_spe() {
		return r17_tot_expo_net_spe;
	}
	public void setR17_tot_expo_net_spe(BigDecimal r17_tot_expo_net_spe) {
		this.r17_tot_expo_net_spe = r17_tot_expo_net_spe;
	}
	public BigDecimal getR17_crm_sub_elig_sub_app() {
		return r17_crm_sub_elig_sub_app;
	}
	public void setR17_crm_sub_elig_sub_app(BigDecimal r17_crm_sub_elig_sub_app) {
		this.r17_crm_sub_elig_sub_app = r17_crm_sub_elig_sub_app;
	}
	public BigDecimal getR17_crm_sub_non_col_guar() {
		return r17_crm_sub_non_col_guar;
	}
	public void setR17_crm_sub_non_col_guar(BigDecimal r17_crm_sub_non_col_guar) {
		this.r17_crm_sub_non_col_guar = r17_crm_sub_non_col_guar;
	}
	public BigDecimal getR17_crm_sub_non_col_cre_der() {
		return r17_crm_sub_non_col_cre_der;
	}
	public void setR17_crm_sub_non_col_cre_der(BigDecimal r17_crm_sub_non_col_cre_der) {
		this.r17_crm_sub_non_col_cre_der = r17_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR17_crm_sub_col_elig_cash() {
		return r17_crm_sub_col_elig_cash;
	}
	public void setR17_crm_sub_col_elig_cash(BigDecimal r17_crm_sub_col_elig_cash) {
		this.r17_crm_sub_col_elig_cash = r17_crm_sub_col_elig_cash;
	}
	public BigDecimal getR17_crm_sub_col_elig_trea_bills() {
		return r17_crm_sub_col_elig_trea_bills;
	}
	public void setR17_crm_sub_col_elig_trea_bills(BigDecimal r17_crm_sub_col_elig_trea_bills) {
		this.r17_crm_sub_col_elig_trea_bills = r17_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR17_crm_sub_col_elig_deb_sec() {
		return r17_crm_sub_col_elig_deb_sec;
	}
	public void setR17_crm_sub_col_elig_deb_sec(BigDecimal r17_crm_sub_col_elig_deb_sec) {
		this.r17_crm_sub_col_elig_deb_sec = r17_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR17_crm_sub_col_elig_equi() {
		return r17_crm_sub_col_elig_equi;
	}
	public void setR17_crm_sub_col_elig_equi(BigDecimal r17_crm_sub_col_elig_equi) {
		this.r17_crm_sub_col_elig_equi = r17_crm_sub_col_elig_equi;
	}
	public BigDecimal getR17_crm_sub_col_elig_unit_tru() {
		return r17_crm_sub_col_elig_unit_tru;
	}
	public void setR17_crm_sub_col_elig_unit_tru(BigDecimal r17_crm_sub_col_elig_unit_tru) {
		this.r17_crm_sub_col_elig_unit_tru = r17_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR17_crm_sub_col_exp_cov() {
		return r17_crm_sub_col_exp_cov;
	}
	public void setR17_crm_sub_col_exp_cov(BigDecimal r17_crm_sub_col_exp_cov) {
		this.r17_crm_sub_col_exp_cov = r17_crm_sub_col_exp_cov;
	}
	public BigDecimal getR17_crm_sub_col_elig_exp_not_cov() {
		return r17_crm_sub_col_elig_exp_not_cov;
	}
	public void setR17_crm_sub_col_elig_exp_not_cov(BigDecimal r17_crm_sub_col_elig_exp_not_cov) {
		this.r17_crm_sub_col_elig_exp_not_cov = r17_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR17_crm_sub_rwa_ris_crm() {
		return r17_crm_sub_rwa_ris_crm;
	}
	public void setR17_crm_sub_rwa_ris_crm(BigDecimal r17_crm_sub_rwa_ris_crm) {
		this.r17_crm_sub_rwa_ris_crm = r17_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR17_crm_sub_rwa_cov_crm() {
		return r17_crm_sub_rwa_cov_crm;
	}
	public void setR17_crm_sub_rwa_cov_crm(BigDecimal r17_crm_sub_rwa_cov_crm) {
		this.r17_crm_sub_rwa_cov_crm = r17_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR17_crm_sub_rwa_org_cou() {
		return r17_crm_sub_rwa_org_cou;
	}
	public void setR17_crm_sub_rwa_org_cou(BigDecimal r17_crm_sub_rwa_org_cou) {
		this.r17_crm_sub_rwa_org_cou = r17_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR17_crm_sub_rwa_not_cov_crm() {
		return r17_crm_sub_rwa_not_cov_crm;
	}
	public void setR17_crm_sub_rwa_not_cov_crm(BigDecimal r17_crm_sub_rwa_not_cov_crm) {
		this.r17_crm_sub_rwa_not_cov_crm = r17_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR17_crm_comp_col_expo_elig() {
		return r17_crm_comp_col_expo_elig;
	}
	public void setR17_crm_comp_col_expo_elig(BigDecimal r17_crm_comp_col_expo_elig) {
		this.r17_crm_comp_col_expo_elig = r17_crm_comp_col_expo_elig;
	}
	public BigDecimal getR17_crm_comp_col_elig_expo_vol_adj() {
		return r17_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR17_crm_comp_col_elig_expo_vol_adj(BigDecimal r17_crm_comp_col_elig_expo_vol_adj) {
		this.r17_crm_comp_col_elig_expo_vol_adj = r17_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR17_crm_comp_col_elig_fin_hai() {
		return r17_crm_comp_col_elig_fin_hai;
	}
	public void setR17_crm_comp_col_elig_fin_hai(BigDecimal r17_crm_comp_col_elig_fin_hai) {
		this.r17_crm_comp_col_elig_fin_hai = r17_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR17_crm_comp_col_expo_val() {
		return r17_crm_comp_col_expo_val;
	}
	public void setR17_crm_comp_col_expo_val(BigDecimal r17_crm_comp_col_expo_val) {
		this.r17_crm_comp_col_expo_val = r17_crm_comp_col_expo_val;
	}
	public BigDecimal getR17_rwa_elig_expo_not_cov_crm() {
		return r17_rwa_elig_expo_not_cov_crm;
	}
	public void setR17_rwa_elig_expo_not_cov_crm(BigDecimal r17_rwa_elig_expo_not_cov_crm) {
		this.r17_rwa_elig_expo_not_cov_crm = r17_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR17_rwa_unsec_expo_cre_ris() {
		return r17_rwa_unsec_expo_cre_ris;
	}
	public void setR17_rwa_unsec_expo_cre_ris(BigDecimal r17_rwa_unsec_expo_cre_ris) {
		this.r17_rwa_unsec_expo_cre_ris = r17_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR17_rwa_unsec_expo() {
		return r17_rwa_unsec_expo;
	}
	public void setR17_rwa_unsec_expo(BigDecimal r17_rwa_unsec_expo) {
		this.r17_rwa_unsec_expo = r17_rwa_unsec_expo;
	}
	public BigDecimal getR17_rwa_tot_ris_wei_ass() {
		return r17_rwa_tot_ris_wei_ass;
	}
	public void setR17_rwa_tot_ris_wei_ass(BigDecimal r17_rwa_tot_ris_wei_ass) {
		this.r17_rwa_tot_ris_wei_ass = r17_rwa_tot_ris_wei_ass;
	}
	public String getR18_exposure_class() {
		return r18_exposure_class;
	}
	public void setR18_exposure_class(String r18_exposure_class) {
		this.r18_exposure_class = r18_exposure_class;
	}
	public BigDecimal getR18_expo_crm() {
		return r18_expo_crm;
	}
	public void setR18_expo_crm(BigDecimal r18_expo_crm) {
		this.r18_expo_crm = r18_expo_crm;
	}
	public BigDecimal getR18_spe_pro_expo() {
		return r18_spe_pro_expo;
	}
	public void setR18_spe_pro_expo(BigDecimal r18_spe_pro_expo) {
		this.r18_spe_pro_expo = r18_spe_pro_expo;
	}
	public BigDecimal getR18_amt_elig_sht_net() {
		return r18_amt_elig_sht_net;
	}
	public void setR18_amt_elig_sht_net(BigDecimal r18_amt_elig_sht_net) {
		this.r18_amt_elig_sht_net = r18_amt_elig_sht_net;
	}
	public BigDecimal getR18_tot_expo_net_spe() {
		return r18_tot_expo_net_spe;
	}
	public void setR18_tot_expo_net_spe(BigDecimal r18_tot_expo_net_spe) {
		this.r18_tot_expo_net_spe = r18_tot_expo_net_spe;
	}
	public BigDecimal getR18_crm_sub_elig_sub_app() {
		return r18_crm_sub_elig_sub_app;
	}
	public void setR18_crm_sub_elig_sub_app(BigDecimal r18_crm_sub_elig_sub_app) {
		this.r18_crm_sub_elig_sub_app = r18_crm_sub_elig_sub_app;
	}
	public BigDecimal getR18_crm_sub_non_col_guar() {
		return r18_crm_sub_non_col_guar;
	}
	public void setR18_crm_sub_non_col_guar(BigDecimal r18_crm_sub_non_col_guar) {
		this.r18_crm_sub_non_col_guar = r18_crm_sub_non_col_guar;
	}
	public BigDecimal getR18_crm_sub_non_col_cre_der() {
		return r18_crm_sub_non_col_cre_der;
	}
	public void setR18_crm_sub_non_col_cre_der(BigDecimal r18_crm_sub_non_col_cre_der) {
		this.r18_crm_sub_non_col_cre_der = r18_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR18_crm_sub_col_elig_cash() {
		return r18_crm_sub_col_elig_cash;
	}
	public void setR18_crm_sub_col_elig_cash(BigDecimal r18_crm_sub_col_elig_cash) {
		this.r18_crm_sub_col_elig_cash = r18_crm_sub_col_elig_cash;
	}
	public BigDecimal getR18_crm_sub_col_elig_trea_bills() {
		return r18_crm_sub_col_elig_trea_bills;
	}
	public void setR18_crm_sub_col_elig_trea_bills(BigDecimal r18_crm_sub_col_elig_trea_bills) {
		this.r18_crm_sub_col_elig_trea_bills = r18_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR18_crm_sub_col_elig_deb_sec() {
		return r18_crm_sub_col_elig_deb_sec;
	}
	public void setR18_crm_sub_col_elig_deb_sec(BigDecimal r18_crm_sub_col_elig_deb_sec) {
		this.r18_crm_sub_col_elig_deb_sec = r18_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR18_crm_sub_col_elig_equi() {
		return r18_crm_sub_col_elig_equi;
	}
	public void setR18_crm_sub_col_elig_equi(BigDecimal r18_crm_sub_col_elig_equi) {
		this.r18_crm_sub_col_elig_equi = r18_crm_sub_col_elig_equi;
	}
	public BigDecimal getR18_crm_sub_col_elig_unit_tru() {
		return r18_crm_sub_col_elig_unit_tru;
	}
	public void setR18_crm_sub_col_elig_unit_tru(BigDecimal r18_crm_sub_col_elig_unit_tru) {
		this.r18_crm_sub_col_elig_unit_tru = r18_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR18_crm_sub_col_exp_cov() {
		return r18_crm_sub_col_exp_cov;
	}
	public void setR18_crm_sub_col_exp_cov(BigDecimal r18_crm_sub_col_exp_cov) {
		this.r18_crm_sub_col_exp_cov = r18_crm_sub_col_exp_cov;
	}
	public BigDecimal getR18_crm_sub_col_elig_exp_not_cov() {
		return r18_crm_sub_col_elig_exp_not_cov;
	}
	public void setR18_crm_sub_col_elig_exp_not_cov(BigDecimal r18_crm_sub_col_elig_exp_not_cov) {
		this.r18_crm_sub_col_elig_exp_not_cov = r18_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR18_crm_sub_rwa_ris_crm() {
		return r18_crm_sub_rwa_ris_crm;
	}
	public void setR18_crm_sub_rwa_ris_crm(BigDecimal r18_crm_sub_rwa_ris_crm) {
		this.r18_crm_sub_rwa_ris_crm = r18_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR18_crm_sub_rwa_cov_crm() {
		return r18_crm_sub_rwa_cov_crm;
	}
	public void setR18_crm_sub_rwa_cov_crm(BigDecimal r18_crm_sub_rwa_cov_crm) {
		this.r18_crm_sub_rwa_cov_crm = r18_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR18_crm_sub_rwa_org_cou() {
		return r18_crm_sub_rwa_org_cou;
	}
	public void setR18_crm_sub_rwa_org_cou(BigDecimal r18_crm_sub_rwa_org_cou) {
		this.r18_crm_sub_rwa_org_cou = r18_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR18_crm_sub_rwa_not_cov_crm() {
		return r18_crm_sub_rwa_not_cov_crm;
	}
	public void setR18_crm_sub_rwa_not_cov_crm(BigDecimal r18_crm_sub_rwa_not_cov_crm) {
		this.r18_crm_sub_rwa_not_cov_crm = r18_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR18_crm_comp_col_expo_elig() {
		return r18_crm_comp_col_expo_elig;
	}
	public void setR18_crm_comp_col_expo_elig(BigDecimal r18_crm_comp_col_expo_elig) {
		this.r18_crm_comp_col_expo_elig = r18_crm_comp_col_expo_elig;
	}
	public BigDecimal getR18_crm_comp_col_elig_expo_vol_adj() {
		return r18_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR18_crm_comp_col_elig_expo_vol_adj(BigDecimal r18_crm_comp_col_elig_expo_vol_adj) {
		this.r18_crm_comp_col_elig_expo_vol_adj = r18_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR18_crm_comp_col_elig_fin_hai() {
		return r18_crm_comp_col_elig_fin_hai;
	}
	public void setR18_crm_comp_col_elig_fin_hai(BigDecimal r18_crm_comp_col_elig_fin_hai) {
		this.r18_crm_comp_col_elig_fin_hai = r18_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR18_crm_comp_col_expo_val() {
		return r18_crm_comp_col_expo_val;
	}
	public void setR18_crm_comp_col_expo_val(BigDecimal r18_crm_comp_col_expo_val) {
		this.r18_crm_comp_col_expo_val = r18_crm_comp_col_expo_val;
	}
	public BigDecimal getR18_rwa_elig_expo_not_cov_crm() {
		return r18_rwa_elig_expo_not_cov_crm;
	}
	public void setR18_rwa_elig_expo_not_cov_crm(BigDecimal r18_rwa_elig_expo_not_cov_crm) {
		this.r18_rwa_elig_expo_not_cov_crm = r18_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR18_rwa_unsec_expo_cre_ris() {
		return r18_rwa_unsec_expo_cre_ris;
	}
	public void setR18_rwa_unsec_expo_cre_ris(BigDecimal r18_rwa_unsec_expo_cre_ris) {
		this.r18_rwa_unsec_expo_cre_ris = r18_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR18_rwa_unsec_expo() {
		return r18_rwa_unsec_expo;
	}
	public void setR18_rwa_unsec_expo(BigDecimal r18_rwa_unsec_expo) {
		this.r18_rwa_unsec_expo = r18_rwa_unsec_expo;
	}
	public BigDecimal getR18_rwa_tot_ris_wei_ass() {
		return r18_rwa_tot_ris_wei_ass;
	}
	public void setR18_rwa_tot_ris_wei_ass(BigDecimal r18_rwa_tot_ris_wei_ass) {
		this.r18_rwa_tot_ris_wei_ass = r18_rwa_tot_ris_wei_ass;
	}
	public String getR30_exposure_class() {
		return r30_exposure_class;
	}
	public void setR30_exposure_class(String r30_exposure_class) {
		this.r30_exposure_class = r30_exposure_class;
	}
	public BigDecimal getR30_expo_crm() {
		return r30_expo_crm;
	}
	public void setR30_expo_crm(BigDecimal r30_expo_crm) {
		this.r30_expo_crm = r30_expo_crm;
	}
	public BigDecimal getR30_spe_pro_expo() {
		return r30_spe_pro_expo;
	}
	public void setR30_spe_pro_expo(BigDecimal r30_spe_pro_expo) {
		this.r30_spe_pro_expo = r30_spe_pro_expo;
	}
	public BigDecimal getR30_amt_elig_sht_net() {
		return r30_amt_elig_sht_net;
	}
	public void setR30_amt_elig_sht_net(BigDecimal r30_amt_elig_sht_net) {
		this.r30_amt_elig_sht_net = r30_amt_elig_sht_net;
	}
	public BigDecimal getR30_tot_expo_net_spe() {
		return r30_tot_expo_net_spe;
	}
	public void setR30_tot_expo_net_spe(BigDecimal r30_tot_expo_net_spe) {
		this.r30_tot_expo_net_spe = r30_tot_expo_net_spe;
	}
	public BigDecimal getR30_crm_sub_elig_sub_app() {
		return r30_crm_sub_elig_sub_app;
	}
	public void setR30_crm_sub_elig_sub_app(BigDecimal r30_crm_sub_elig_sub_app) {
		this.r30_crm_sub_elig_sub_app = r30_crm_sub_elig_sub_app;
	}
	public BigDecimal getR30_crm_sub_non_col_guar() {
		return r30_crm_sub_non_col_guar;
	}
	public void setR30_crm_sub_non_col_guar(BigDecimal r30_crm_sub_non_col_guar) {
		this.r30_crm_sub_non_col_guar = r30_crm_sub_non_col_guar;
	}
	public BigDecimal getR30_crm_sub_non_col_cre_der() {
		return r30_crm_sub_non_col_cre_der;
	}
	public void setR30_crm_sub_non_col_cre_der(BigDecimal r30_crm_sub_non_col_cre_der) {
		this.r30_crm_sub_non_col_cre_der = r30_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR30_crm_sub_col_elig_cash() {
		return r30_crm_sub_col_elig_cash;
	}
	public void setR30_crm_sub_col_elig_cash(BigDecimal r30_crm_sub_col_elig_cash) {
		this.r30_crm_sub_col_elig_cash = r30_crm_sub_col_elig_cash;
	}
	public BigDecimal getR30_crm_sub_col_elig_trea_bills() {
		return r30_crm_sub_col_elig_trea_bills;
	}
	public void setR30_crm_sub_col_elig_trea_bills(BigDecimal r30_crm_sub_col_elig_trea_bills) {
		this.r30_crm_sub_col_elig_trea_bills = r30_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR30_crm_sub_col_elig_deb_sec() {
		return r30_crm_sub_col_elig_deb_sec;
	}
	public void setR30_crm_sub_col_elig_deb_sec(BigDecimal r30_crm_sub_col_elig_deb_sec) {
		this.r30_crm_sub_col_elig_deb_sec = r30_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR30_crm_sub_col_elig_equi() {
		return r30_crm_sub_col_elig_equi;
	}
	public void setR30_crm_sub_col_elig_equi(BigDecimal r30_crm_sub_col_elig_equi) {
		this.r30_crm_sub_col_elig_equi = r30_crm_sub_col_elig_equi;
	}
	public BigDecimal getR30_crm_sub_col_elig_unit_tru() {
		return r30_crm_sub_col_elig_unit_tru;
	}
	public void setR30_crm_sub_col_elig_unit_tru(BigDecimal r30_crm_sub_col_elig_unit_tru) {
		this.r30_crm_sub_col_elig_unit_tru = r30_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR30_crm_sub_col_exp_cov() {
		return r30_crm_sub_col_exp_cov;
	}
	public void setR30_crm_sub_col_exp_cov(BigDecimal r30_crm_sub_col_exp_cov) {
		this.r30_crm_sub_col_exp_cov = r30_crm_sub_col_exp_cov;
	}
	public BigDecimal getR30_crm_sub_col_elig_exp_not_cov() {
		return r30_crm_sub_col_elig_exp_not_cov;
	}
	public void setR30_crm_sub_col_elig_exp_not_cov(BigDecimal r30_crm_sub_col_elig_exp_not_cov) {
		this.r30_crm_sub_col_elig_exp_not_cov = r30_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR30_crm_sub_rwa_ris_crm() {
		return r30_crm_sub_rwa_ris_crm;
	}
	public void setR30_crm_sub_rwa_ris_crm(BigDecimal r30_crm_sub_rwa_ris_crm) {
		this.r30_crm_sub_rwa_ris_crm = r30_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR30_crm_sub_rwa_cov_crm() {
		return r30_crm_sub_rwa_cov_crm;
	}
	public void setR30_crm_sub_rwa_cov_crm(BigDecimal r30_crm_sub_rwa_cov_crm) {
		this.r30_crm_sub_rwa_cov_crm = r30_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR30_crm_sub_rwa_org_cou() {
		return r30_crm_sub_rwa_org_cou;
	}
	public void setR30_crm_sub_rwa_org_cou(BigDecimal r30_crm_sub_rwa_org_cou) {
		this.r30_crm_sub_rwa_org_cou = r30_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR30_crm_sub_rwa_not_cov_crm() {
		return r30_crm_sub_rwa_not_cov_crm;
	}
	public void setR30_crm_sub_rwa_not_cov_crm(BigDecimal r30_crm_sub_rwa_not_cov_crm) {
		this.r30_crm_sub_rwa_not_cov_crm = r30_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR30_crm_comp_col_expo_elig() {
		return r30_crm_comp_col_expo_elig;
	}
	public void setR30_crm_comp_col_expo_elig(BigDecimal r30_crm_comp_col_expo_elig) {
		this.r30_crm_comp_col_expo_elig = r30_crm_comp_col_expo_elig;
	}
	public BigDecimal getR30_crm_comp_col_elig_expo_vol_adj() {
		return r30_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR30_crm_comp_col_elig_expo_vol_adj(BigDecimal r30_crm_comp_col_elig_expo_vol_adj) {
		this.r30_crm_comp_col_elig_expo_vol_adj = r30_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR30_crm_comp_col_elig_fin_hai() {
		return r30_crm_comp_col_elig_fin_hai;
	}
	public void setR30_crm_comp_col_elig_fin_hai(BigDecimal r30_crm_comp_col_elig_fin_hai) {
		this.r30_crm_comp_col_elig_fin_hai = r30_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR30_crm_comp_col_expo_val() {
		return r30_crm_comp_col_expo_val;
	}
	public void setR30_crm_comp_col_expo_val(BigDecimal r30_crm_comp_col_expo_val) {
		this.r30_crm_comp_col_expo_val = r30_crm_comp_col_expo_val;
	}
	public BigDecimal getR30_rwa_elig_expo_not_cov_crm() {
		return r30_rwa_elig_expo_not_cov_crm;
	}
	public void setR30_rwa_elig_expo_not_cov_crm(BigDecimal r30_rwa_elig_expo_not_cov_crm) {
		this.r30_rwa_elig_expo_not_cov_crm = r30_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR30_rwa_unsec_expo_cre_ris() {
		return r30_rwa_unsec_expo_cre_ris;
	}
	public void setR30_rwa_unsec_expo_cre_ris(BigDecimal r30_rwa_unsec_expo_cre_ris) {
		this.r30_rwa_unsec_expo_cre_ris = r30_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR30_rwa_unsec_expo() {
		return r30_rwa_unsec_expo;
	}
	public void setR30_rwa_unsec_expo(BigDecimal r30_rwa_unsec_expo) {
		this.r30_rwa_unsec_expo = r30_rwa_unsec_expo;
	}
	public BigDecimal getR30_rwa_tot_ris_wei_ass() {
		return r30_rwa_tot_ris_wei_ass;
	}
	public void setR30_rwa_tot_ris_wei_ass(BigDecimal r30_rwa_tot_ris_wei_ass) {
		this.r30_rwa_tot_ris_wei_ass = r30_rwa_tot_ris_wei_ass;
	}
	public String getR31_exposure_class() {
		return r31_exposure_class;
	}
	public void setR31_exposure_class(String r31_exposure_class) {
		this.r31_exposure_class = r31_exposure_class;
	}
	public BigDecimal getR31_expo_crm() {
		return r31_expo_crm;
	}
	public void setR31_expo_crm(BigDecimal r31_expo_crm) {
		this.r31_expo_crm = r31_expo_crm;
	}
	public BigDecimal getR31_spe_pro_expo() {
		return r31_spe_pro_expo;
	}
	public void setR31_spe_pro_expo(BigDecimal r31_spe_pro_expo) {
		this.r31_spe_pro_expo = r31_spe_pro_expo;
	}
	public BigDecimal getR31_amt_elig_sht_net() {
		return r31_amt_elig_sht_net;
	}
	public void setR31_amt_elig_sht_net(BigDecimal r31_amt_elig_sht_net) {
		this.r31_amt_elig_sht_net = r31_amt_elig_sht_net;
	}
	public BigDecimal getR31_tot_expo_net_spe() {
		return r31_tot_expo_net_spe;
	}
	public void setR31_tot_expo_net_spe(BigDecimal r31_tot_expo_net_spe) {
		this.r31_tot_expo_net_spe = r31_tot_expo_net_spe;
	}
	public BigDecimal getR31_crm_sub_elig_sub_app() {
		return r31_crm_sub_elig_sub_app;
	}
	public void setR31_crm_sub_elig_sub_app(BigDecimal r31_crm_sub_elig_sub_app) {
		this.r31_crm_sub_elig_sub_app = r31_crm_sub_elig_sub_app;
	}
	public BigDecimal getR31_crm_sub_non_col_guar() {
		return r31_crm_sub_non_col_guar;
	}
	public void setR31_crm_sub_non_col_guar(BigDecimal r31_crm_sub_non_col_guar) {
		this.r31_crm_sub_non_col_guar = r31_crm_sub_non_col_guar;
	}
	public BigDecimal getR31_crm_sub_non_col_cre_der() {
		return r31_crm_sub_non_col_cre_der;
	}
	public void setR31_crm_sub_non_col_cre_der(BigDecimal r31_crm_sub_non_col_cre_der) {
		this.r31_crm_sub_non_col_cre_der = r31_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR31_crm_sub_col_elig_cash() {
		return r31_crm_sub_col_elig_cash;
	}
	public void setR31_crm_sub_col_elig_cash(BigDecimal r31_crm_sub_col_elig_cash) {
		this.r31_crm_sub_col_elig_cash = r31_crm_sub_col_elig_cash;
	}
	public BigDecimal getR31_crm_sub_col_elig_trea_bills() {
		return r31_crm_sub_col_elig_trea_bills;
	}
	public void setR31_crm_sub_col_elig_trea_bills(BigDecimal r31_crm_sub_col_elig_trea_bills) {
		this.r31_crm_sub_col_elig_trea_bills = r31_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR31_crm_sub_col_elig_deb_sec() {
		return r31_crm_sub_col_elig_deb_sec;
	}
	public void setR31_crm_sub_col_elig_deb_sec(BigDecimal r31_crm_sub_col_elig_deb_sec) {
		this.r31_crm_sub_col_elig_deb_sec = r31_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR31_crm_sub_col_elig_equi() {
		return r31_crm_sub_col_elig_equi;
	}
	public void setR31_crm_sub_col_elig_equi(BigDecimal r31_crm_sub_col_elig_equi) {
		this.r31_crm_sub_col_elig_equi = r31_crm_sub_col_elig_equi;
	}
	public BigDecimal getR31_crm_sub_col_elig_unit_tru() {
		return r31_crm_sub_col_elig_unit_tru;
	}
	public void setR31_crm_sub_col_elig_unit_tru(BigDecimal r31_crm_sub_col_elig_unit_tru) {
		this.r31_crm_sub_col_elig_unit_tru = r31_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR31_crm_sub_col_exp_cov() {
		return r31_crm_sub_col_exp_cov;
	}
	public void setR31_crm_sub_col_exp_cov(BigDecimal r31_crm_sub_col_exp_cov) {
		this.r31_crm_sub_col_exp_cov = r31_crm_sub_col_exp_cov;
	}
	public BigDecimal getR31_crm_sub_col_elig_exp_not_cov() {
		return r31_crm_sub_col_elig_exp_not_cov;
	}
	public void setR31_crm_sub_col_elig_exp_not_cov(BigDecimal r31_crm_sub_col_elig_exp_not_cov) {
		this.r31_crm_sub_col_elig_exp_not_cov = r31_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR31_crm_sub_rwa_ris_crm() {
		return r31_crm_sub_rwa_ris_crm;
	}
	public void setR31_crm_sub_rwa_ris_crm(BigDecimal r31_crm_sub_rwa_ris_crm) {
		this.r31_crm_sub_rwa_ris_crm = r31_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR31_crm_sub_rwa_cov_crm() {
		return r31_crm_sub_rwa_cov_crm;
	}
	public void setR31_crm_sub_rwa_cov_crm(BigDecimal r31_crm_sub_rwa_cov_crm) {
		this.r31_crm_sub_rwa_cov_crm = r31_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR31_crm_sub_rwa_org_cou() {
		return r31_crm_sub_rwa_org_cou;
	}
	public void setR31_crm_sub_rwa_org_cou(BigDecimal r31_crm_sub_rwa_org_cou) {
		this.r31_crm_sub_rwa_org_cou = r31_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR31_crm_sub_rwa_not_cov_crm() {
		return r31_crm_sub_rwa_not_cov_crm;
	}
	public void setR31_crm_sub_rwa_not_cov_crm(BigDecimal r31_crm_sub_rwa_not_cov_crm) {
		this.r31_crm_sub_rwa_not_cov_crm = r31_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR31_crm_comp_col_expo_elig() {
		return r31_crm_comp_col_expo_elig;
	}
	public void setR31_crm_comp_col_expo_elig(BigDecimal r31_crm_comp_col_expo_elig) {
		this.r31_crm_comp_col_expo_elig = r31_crm_comp_col_expo_elig;
	}
	public BigDecimal getR31_crm_comp_col_elig_expo_vol_adj() {
		return r31_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR31_crm_comp_col_elig_expo_vol_adj(BigDecimal r31_crm_comp_col_elig_expo_vol_adj) {
		this.r31_crm_comp_col_elig_expo_vol_adj = r31_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR31_crm_comp_col_elig_fin_hai() {
		return r31_crm_comp_col_elig_fin_hai;
	}
	public void setR31_crm_comp_col_elig_fin_hai(BigDecimal r31_crm_comp_col_elig_fin_hai) {
		this.r31_crm_comp_col_elig_fin_hai = r31_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR31_crm_comp_col_expo_val() {
		return r31_crm_comp_col_expo_val;
	}
	public void setR31_crm_comp_col_expo_val(BigDecimal r31_crm_comp_col_expo_val) {
		this.r31_crm_comp_col_expo_val = r31_crm_comp_col_expo_val;
	}
	public BigDecimal getR31_rwa_elig_expo_not_cov_crm() {
		return r31_rwa_elig_expo_not_cov_crm;
	}
	public void setR31_rwa_elig_expo_not_cov_crm(BigDecimal r31_rwa_elig_expo_not_cov_crm) {
		this.r31_rwa_elig_expo_not_cov_crm = r31_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR31_rwa_unsec_expo_cre_ris() {
		return r31_rwa_unsec_expo_cre_ris;
	}
	public void setR31_rwa_unsec_expo_cre_ris(BigDecimal r31_rwa_unsec_expo_cre_ris) {
		this.r31_rwa_unsec_expo_cre_ris = r31_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR31_rwa_unsec_expo() {
		return r31_rwa_unsec_expo;
	}
	public void setR31_rwa_unsec_expo(BigDecimal r31_rwa_unsec_expo) {
		this.r31_rwa_unsec_expo = r31_rwa_unsec_expo;
	}
	public BigDecimal getR31_rwa_tot_ris_wei_ass() {
		return r31_rwa_tot_ris_wei_ass;
	}
	public void setR31_rwa_tot_ris_wei_ass(BigDecimal r31_rwa_tot_ris_wei_ass) {
		this.r31_rwa_tot_ris_wei_ass = r31_rwa_tot_ris_wei_ass;
	}
	public String getR45_exposure_class() {
		return r45_exposure_class;
	}
	public void setR45_exposure_class(String r45_exposure_class) {
		this.r45_exposure_class = r45_exposure_class;
	}
	public BigDecimal getR45_expo_crm() {
		return r45_expo_crm;
	}
	public void setR45_expo_crm(BigDecimal r45_expo_crm) {
		this.r45_expo_crm = r45_expo_crm;
	}
	public BigDecimal getR45_spe_pro_expo() {
		return r45_spe_pro_expo;
	}
	public void setR45_spe_pro_expo(BigDecimal r45_spe_pro_expo) {
		this.r45_spe_pro_expo = r45_spe_pro_expo;
	}
	public BigDecimal getR45_amt_elig_sht_net() {
		return r45_amt_elig_sht_net;
	}
	public void setR45_amt_elig_sht_net(BigDecimal r45_amt_elig_sht_net) {
		this.r45_amt_elig_sht_net = r45_amt_elig_sht_net;
	}
	public BigDecimal getR45_tot_expo_net_spe() {
		return r45_tot_expo_net_spe;
	}
	public void setR45_tot_expo_net_spe(BigDecimal r45_tot_expo_net_spe) {
		this.r45_tot_expo_net_spe = r45_tot_expo_net_spe;
	}
	public BigDecimal getR45_crm_sub_elig_sub_app() {
		return r45_crm_sub_elig_sub_app;
	}
	public void setR45_crm_sub_elig_sub_app(BigDecimal r45_crm_sub_elig_sub_app) {
		this.r45_crm_sub_elig_sub_app = r45_crm_sub_elig_sub_app;
	}
	public BigDecimal getR45_crm_sub_non_col_guar() {
		return r45_crm_sub_non_col_guar;
	}
	public void setR45_crm_sub_non_col_guar(BigDecimal r45_crm_sub_non_col_guar) {
		this.r45_crm_sub_non_col_guar = r45_crm_sub_non_col_guar;
	}
	public BigDecimal getR45_crm_sub_non_col_cre_der() {
		return r45_crm_sub_non_col_cre_der;
	}
	public void setR45_crm_sub_non_col_cre_der(BigDecimal r45_crm_sub_non_col_cre_der) {
		this.r45_crm_sub_non_col_cre_der = r45_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR45_crm_sub_col_elig_cash() {
		return r45_crm_sub_col_elig_cash;
	}
	public void setR45_crm_sub_col_elig_cash(BigDecimal r45_crm_sub_col_elig_cash) {
		this.r45_crm_sub_col_elig_cash = r45_crm_sub_col_elig_cash;
	}
	public BigDecimal getR45_crm_sub_col_elig_trea_bills() {
		return r45_crm_sub_col_elig_trea_bills;
	}
	public void setR45_crm_sub_col_elig_trea_bills(BigDecimal r45_crm_sub_col_elig_trea_bills) {
		this.r45_crm_sub_col_elig_trea_bills = r45_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR45_crm_sub_col_elig_deb_sec() {
		return r45_crm_sub_col_elig_deb_sec;
	}
	public void setR45_crm_sub_col_elig_deb_sec(BigDecimal r45_crm_sub_col_elig_deb_sec) {
		this.r45_crm_sub_col_elig_deb_sec = r45_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR45_crm_sub_col_elig_equi() {
		return r45_crm_sub_col_elig_equi;
	}
	public void setR45_crm_sub_col_elig_equi(BigDecimal r45_crm_sub_col_elig_equi) {
		this.r45_crm_sub_col_elig_equi = r45_crm_sub_col_elig_equi;
	}
	public BigDecimal getR45_crm_sub_col_elig_unit_tru() {
		return r45_crm_sub_col_elig_unit_tru;
	}
	public void setR45_crm_sub_col_elig_unit_tru(BigDecimal r45_crm_sub_col_elig_unit_tru) {
		this.r45_crm_sub_col_elig_unit_tru = r45_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR45_crm_sub_col_exp_cov() {
		return r45_crm_sub_col_exp_cov;
	}
	public void setR45_crm_sub_col_exp_cov(BigDecimal r45_crm_sub_col_exp_cov) {
		this.r45_crm_sub_col_exp_cov = r45_crm_sub_col_exp_cov;
	}
	public BigDecimal getR45_crm_sub_col_elig_exp_not_cov() {
		return r45_crm_sub_col_elig_exp_not_cov;
	}
	public void setR45_crm_sub_col_elig_exp_not_cov(BigDecimal r45_crm_sub_col_elig_exp_not_cov) {
		this.r45_crm_sub_col_elig_exp_not_cov = r45_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR45_crm_sub_rwa_ris_crm() {
		return r45_crm_sub_rwa_ris_crm;
	}
	public void setR45_crm_sub_rwa_ris_crm(BigDecimal r45_crm_sub_rwa_ris_crm) {
		this.r45_crm_sub_rwa_ris_crm = r45_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR45_crm_sub_rwa_cov_crm() {
		return r45_crm_sub_rwa_cov_crm;
	}
	public void setR45_crm_sub_rwa_cov_crm(BigDecimal r45_crm_sub_rwa_cov_crm) {
		this.r45_crm_sub_rwa_cov_crm = r45_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR45_crm_sub_rwa_org_cou() {
		return r45_crm_sub_rwa_org_cou;
	}
	public void setR45_crm_sub_rwa_org_cou(BigDecimal r45_crm_sub_rwa_org_cou) {
		this.r45_crm_sub_rwa_org_cou = r45_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR45_crm_sub_rwa_not_cov_crm() {
		return r45_crm_sub_rwa_not_cov_crm;
	}
	public void setR45_crm_sub_rwa_not_cov_crm(BigDecimal r45_crm_sub_rwa_not_cov_crm) {
		this.r45_crm_sub_rwa_not_cov_crm = r45_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR45_crm_comp_col_expo_elig() {
		return r45_crm_comp_col_expo_elig;
	}
	public void setR45_crm_comp_col_expo_elig(BigDecimal r45_crm_comp_col_expo_elig) {
		this.r45_crm_comp_col_expo_elig = r45_crm_comp_col_expo_elig;
	}
	public BigDecimal getR45_crm_comp_col_elig_expo_vol_adj() {
		return r45_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR45_crm_comp_col_elig_expo_vol_adj(BigDecimal r45_crm_comp_col_elig_expo_vol_adj) {
		this.r45_crm_comp_col_elig_expo_vol_adj = r45_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR45_crm_comp_col_elig_fin_hai() {
		return r45_crm_comp_col_elig_fin_hai;
	}
	public void setR45_crm_comp_col_elig_fin_hai(BigDecimal r45_crm_comp_col_elig_fin_hai) {
		this.r45_crm_comp_col_elig_fin_hai = r45_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR45_crm_comp_col_expo_val() {
		return r45_crm_comp_col_expo_val;
	}
	public void setR45_crm_comp_col_expo_val(BigDecimal r45_crm_comp_col_expo_val) {
		this.r45_crm_comp_col_expo_val = r45_crm_comp_col_expo_val;
	}
	public BigDecimal getR45_rwa_elig_expo_not_cov_crm() {
		return r45_rwa_elig_expo_not_cov_crm;
	}
	public void setR45_rwa_elig_expo_not_cov_crm(BigDecimal r45_rwa_elig_expo_not_cov_crm) {
		this.r45_rwa_elig_expo_not_cov_crm = r45_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR45_rwa_unsec_expo_cre_ris() {
		return r45_rwa_unsec_expo_cre_ris;
	}
	public void setR45_rwa_unsec_expo_cre_ris(BigDecimal r45_rwa_unsec_expo_cre_ris) {
		this.r45_rwa_unsec_expo_cre_ris = r45_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR45_rwa_unsec_expo() {
		return r45_rwa_unsec_expo;
	}
	public void setR45_rwa_unsec_expo(BigDecimal r45_rwa_unsec_expo) {
		this.r45_rwa_unsec_expo = r45_rwa_unsec_expo;
	}
	public BigDecimal getR45_rwa_tot_ris_wei_ass() {
		return r45_rwa_tot_ris_wei_ass;
	}
	public void setR45_rwa_tot_ris_wei_ass(BigDecimal r45_rwa_tot_ris_wei_ass) {
		this.r45_rwa_tot_ris_wei_ass = r45_rwa_tot_ris_wei_ass;
	}
	public String getR46_exposure_class() {
		return r46_exposure_class;
	}
	public void setR46_exposure_class(String r46_exposure_class) {
		this.r46_exposure_class = r46_exposure_class;
	}
	public BigDecimal getR46_expo_crm() {
		return r46_expo_crm;
	}
	public void setR46_expo_crm(BigDecimal r46_expo_crm) {
		this.r46_expo_crm = r46_expo_crm;
	}
	public BigDecimal getR46_spe_pro_expo() {
		return r46_spe_pro_expo;
	}
	public void setR46_spe_pro_expo(BigDecimal r46_spe_pro_expo) {
		this.r46_spe_pro_expo = r46_spe_pro_expo;
	}
	public BigDecimal getR46_amt_elig_sht_net() {
		return r46_amt_elig_sht_net;
	}
	public void setR46_amt_elig_sht_net(BigDecimal r46_amt_elig_sht_net) {
		this.r46_amt_elig_sht_net = r46_amt_elig_sht_net;
	}
	public BigDecimal getR46_tot_expo_net_spe() {
		return r46_tot_expo_net_spe;
	}
	public void setR46_tot_expo_net_spe(BigDecimal r46_tot_expo_net_spe) {
		this.r46_tot_expo_net_spe = r46_tot_expo_net_spe;
	}
	public BigDecimal getR46_crm_sub_elig_sub_app() {
		return r46_crm_sub_elig_sub_app;
	}
	public void setR46_crm_sub_elig_sub_app(BigDecimal r46_crm_sub_elig_sub_app) {
		this.r46_crm_sub_elig_sub_app = r46_crm_sub_elig_sub_app;
	}
	public BigDecimal getR46_crm_sub_non_col_guar() {
		return r46_crm_sub_non_col_guar;
	}
	public void setR46_crm_sub_non_col_guar(BigDecimal r46_crm_sub_non_col_guar) {
		this.r46_crm_sub_non_col_guar = r46_crm_sub_non_col_guar;
	}
	public BigDecimal getR46_crm_sub_non_col_cre_der() {
		return r46_crm_sub_non_col_cre_der;
	}
	public void setR46_crm_sub_non_col_cre_der(BigDecimal r46_crm_sub_non_col_cre_der) {
		this.r46_crm_sub_non_col_cre_der = r46_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR46_crm_sub_col_elig_cash() {
		return r46_crm_sub_col_elig_cash;
	}
	public void setR46_crm_sub_col_elig_cash(BigDecimal r46_crm_sub_col_elig_cash) {
		this.r46_crm_sub_col_elig_cash = r46_crm_sub_col_elig_cash;
	}
	public BigDecimal getR46_crm_sub_col_elig_trea_bills() {
		return r46_crm_sub_col_elig_trea_bills;
	}
	public void setR46_crm_sub_col_elig_trea_bills(BigDecimal r46_crm_sub_col_elig_trea_bills) {
		this.r46_crm_sub_col_elig_trea_bills = r46_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR46_crm_sub_col_elig_deb_sec() {
		return r46_crm_sub_col_elig_deb_sec;
	}
	public void setR46_crm_sub_col_elig_deb_sec(BigDecimal r46_crm_sub_col_elig_deb_sec) {
		this.r46_crm_sub_col_elig_deb_sec = r46_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR46_crm_sub_col_elig_equi() {
		return r46_crm_sub_col_elig_equi;
	}
	public void setR46_crm_sub_col_elig_equi(BigDecimal r46_crm_sub_col_elig_equi) {
		this.r46_crm_sub_col_elig_equi = r46_crm_sub_col_elig_equi;
	}
	public BigDecimal getR46_crm_sub_col_elig_unit_tru() {
		return r46_crm_sub_col_elig_unit_tru;
	}
	public void setR46_crm_sub_col_elig_unit_tru(BigDecimal r46_crm_sub_col_elig_unit_tru) {
		this.r46_crm_sub_col_elig_unit_tru = r46_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR46_crm_sub_col_exp_cov() {
		return r46_crm_sub_col_exp_cov;
	}
	public void setR46_crm_sub_col_exp_cov(BigDecimal r46_crm_sub_col_exp_cov) {
		this.r46_crm_sub_col_exp_cov = r46_crm_sub_col_exp_cov;
	}
	public BigDecimal getR46_crm_sub_col_elig_exp_not_cov() {
		return r46_crm_sub_col_elig_exp_not_cov;
	}
	public void setR46_crm_sub_col_elig_exp_not_cov(BigDecimal r46_crm_sub_col_elig_exp_not_cov) {
		this.r46_crm_sub_col_elig_exp_not_cov = r46_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR46_crm_sub_rwa_ris_crm() {
		return r46_crm_sub_rwa_ris_crm;
	}
	public void setR46_crm_sub_rwa_ris_crm(BigDecimal r46_crm_sub_rwa_ris_crm) {
		this.r46_crm_sub_rwa_ris_crm = r46_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR46_crm_sub_rwa_cov_crm() {
		return r46_crm_sub_rwa_cov_crm;
	}
	public void setR46_crm_sub_rwa_cov_crm(BigDecimal r46_crm_sub_rwa_cov_crm) {
		this.r46_crm_sub_rwa_cov_crm = r46_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR46_crm_sub_rwa_org_cou() {
		return r46_crm_sub_rwa_org_cou;
	}
	public void setR46_crm_sub_rwa_org_cou(BigDecimal r46_crm_sub_rwa_org_cou) {
		this.r46_crm_sub_rwa_org_cou = r46_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR46_crm_sub_rwa_not_cov_crm() {
		return r46_crm_sub_rwa_not_cov_crm;
	}
	public void setR46_crm_sub_rwa_not_cov_crm(BigDecimal r46_crm_sub_rwa_not_cov_crm) {
		this.r46_crm_sub_rwa_not_cov_crm = r46_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR46_crm_comp_col_expo_elig() {
		return r46_crm_comp_col_expo_elig;
	}
	public void setR46_crm_comp_col_expo_elig(BigDecimal r46_crm_comp_col_expo_elig) {
		this.r46_crm_comp_col_expo_elig = r46_crm_comp_col_expo_elig;
	}
	public BigDecimal getR46_crm_comp_col_elig_expo_vol_adj() {
		return r46_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR46_crm_comp_col_elig_expo_vol_adj(BigDecimal r46_crm_comp_col_elig_expo_vol_adj) {
		this.r46_crm_comp_col_elig_expo_vol_adj = r46_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR46_crm_comp_col_elig_fin_hai() {
		return r46_crm_comp_col_elig_fin_hai;
	}
	public void setR46_crm_comp_col_elig_fin_hai(BigDecimal r46_crm_comp_col_elig_fin_hai) {
		this.r46_crm_comp_col_elig_fin_hai = r46_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR46_crm_comp_col_expo_val() {
		return r46_crm_comp_col_expo_val;
	}
	public void setR46_crm_comp_col_expo_val(BigDecimal r46_crm_comp_col_expo_val) {
		this.r46_crm_comp_col_expo_val = r46_crm_comp_col_expo_val;
	}
	public BigDecimal getR46_rwa_elig_expo_not_cov_crm() {
		return r46_rwa_elig_expo_not_cov_crm;
	}
	public void setR46_rwa_elig_expo_not_cov_crm(BigDecimal r46_rwa_elig_expo_not_cov_crm) {
		this.r46_rwa_elig_expo_not_cov_crm = r46_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR46_rwa_unsec_expo_cre_ris() {
		return r46_rwa_unsec_expo_cre_ris;
	}
	public void setR46_rwa_unsec_expo_cre_ris(BigDecimal r46_rwa_unsec_expo_cre_ris) {
		this.r46_rwa_unsec_expo_cre_ris = r46_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR46_rwa_unsec_expo() {
		return r46_rwa_unsec_expo;
	}
	public void setR46_rwa_unsec_expo(BigDecimal r46_rwa_unsec_expo) {
		this.r46_rwa_unsec_expo = r46_rwa_unsec_expo;
	}
	public BigDecimal getR46_rwa_tot_ris_wei_ass() {
		return r46_rwa_tot_ris_wei_ass;
	}
	public void setR46_rwa_tot_ris_wei_ass(BigDecimal r46_rwa_tot_ris_wei_ass) {
		this.r46_rwa_tot_ris_wei_ass = r46_rwa_tot_ris_wei_ass;
	}
	public String getR58_exposure_class() {
		return r58_exposure_class;
	}
	public void setR58_exposure_class(String r58_exposure_class) {
		this.r58_exposure_class = r58_exposure_class;
	}
	public BigDecimal getR58_expo_crm() {
		return r58_expo_crm;
	}
	public void setR58_expo_crm(BigDecimal r58_expo_crm) {
		this.r58_expo_crm = r58_expo_crm;
	}
	public BigDecimal getR58_spe_pro_expo() {
		return r58_spe_pro_expo;
	}
	public void setR58_spe_pro_expo(BigDecimal r58_spe_pro_expo) {
		this.r58_spe_pro_expo = r58_spe_pro_expo;
	}
	public BigDecimal getR58_amt_elig_sht_net() {
		return r58_amt_elig_sht_net;
	}
	public void setR58_amt_elig_sht_net(BigDecimal r58_amt_elig_sht_net) {
		this.r58_amt_elig_sht_net = r58_amt_elig_sht_net;
	}
	public BigDecimal getR58_tot_expo_net_spe() {
		return r58_tot_expo_net_spe;
	}
	public void setR58_tot_expo_net_spe(BigDecimal r58_tot_expo_net_spe) {
		this.r58_tot_expo_net_spe = r58_tot_expo_net_spe;
	}
	public BigDecimal getR58_crm_sub_elig_sub_app() {
		return r58_crm_sub_elig_sub_app;
	}
	public void setR58_crm_sub_elig_sub_app(BigDecimal r58_crm_sub_elig_sub_app) {
		this.r58_crm_sub_elig_sub_app = r58_crm_sub_elig_sub_app;
	}
	public BigDecimal getR58_crm_sub_non_col_guar() {
		return r58_crm_sub_non_col_guar;
	}
	public void setR58_crm_sub_non_col_guar(BigDecimal r58_crm_sub_non_col_guar) {
		this.r58_crm_sub_non_col_guar = r58_crm_sub_non_col_guar;
	}
	public BigDecimal getR58_crm_sub_non_col_cre_der() {
		return r58_crm_sub_non_col_cre_der;
	}
	public void setR58_crm_sub_non_col_cre_der(BigDecimal r58_crm_sub_non_col_cre_der) {
		this.r58_crm_sub_non_col_cre_der = r58_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR58_crm_sub_col_elig_cash() {
		return r58_crm_sub_col_elig_cash;
	}
	public void setR58_crm_sub_col_elig_cash(BigDecimal r58_crm_sub_col_elig_cash) {
		this.r58_crm_sub_col_elig_cash = r58_crm_sub_col_elig_cash;
	}
	public BigDecimal getR58_crm_sub_col_elig_trea_bills() {
		return r58_crm_sub_col_elig_trea_bills;
	}
	public void setR58_crm_sub_col_elig_trea_bills(BigDecimal r58_crm_sub_col_elig_trea_bills) {
		this.r58_crm_sub_col_elig_trea_bills = r58_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR58_crm_sub_col_elig_deb_sec() {
		return r58_crm_sub_col_elig_deb_sec;
	}
	public void setR58_crm_sub_col_elig_deb_sec(BigDecimal r58_crm_sub_col_elig_deb_sec) {
		this.r58_crm_sub_col_elig_deb_sec = r58_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR58_crm_sub_col_elig_equi() {
		return r58_crm_sub_col_elig_equi;
	}
	public void setR58_crm_sub_col_elig_equi(BigDecimal r58_crm_sub_col_elig_equi) {
		this.r58_crm_sub_col_elig_equi = r58_crm_sub_col_elig_equi;
	}
	public BigDecimal getR58_crm_sub_col_elig_unit_tru() {
		return r58_crm_sub_col_elig_unit_tru;
	}
	public void setR58_crm_sub_col_elig_unit_tru(BigDecimal r58_crm_sub_col_elig_unit_tru) {
		this.r58_crm_sub_col_elig_unit_tru = r58_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR58_crm_sub_col_exp_cov() {
		return r58_crm_sub_col_exp_cov;
	}
	public void setR58_crm_sub_col_exp_cov(BigDecimal r58_crm_sub_col_exp_cov) {
		this.r58_crm_sub_col_exp_cov = r58_crm_sub_col_exp_cov;
	}
	public BigDecimal getR58_crm_sub_col_elig_exp_not_cov() {
		return r58_crm_sub_col_elig_exp_not_cov;
	}
	public void setR58_crm_sub_col_elig_exp_not_cov(BigDecimal r58_crm_sub_col_elig_exp_not_cov) {
		this.r58_crm_sub_col_elig_exp_not_cov = r58_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR58_crm_sub_rwa_ris_crm() {
		return r58_crm_sub_rwa_ris_crm;
	}
	public void setR58_crm_sub_rwa_ris_crm(BigDecimal r58_crm_sub_rwa_ris_crm) {
		this.r58_crm_sub_rwa_ris_crm = r58_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR58_crm_sub_rwa_cov_crm() {
		return r58_crm_sub_rwa_cov_crm;
	}
	public void setR58_crm_sub_rwa_cov_crm(BigDecimal r58_crm_sub_rwa_cov_crm) {
		this.r58_crm_sub_rwa_cov_crm = r58_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR58_crm_sub_rwa_org_cou() {
		return r58_crm_sub_rwa_org_cou;
	}
	public void setR58_crm_sub_rwa_org_cou(BigDecimal r58_crm_sub_rwa_org_cou) {
		this.r58_crm_sub_rwa_org_cou = r58_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR58_crm_sub_rwa_not_cov_crm() {
		return r58_crm_sub_rwa_not_cov_crm;
	}
	public void setR58_crm_sub_rwa_not_cov_crm(BigDecimal r58_crm_sub_rwa_not_cov_crm) {
		this.r58_crm_sub_rwa_not_cov_crm = r58_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR58_crm_comp_col_expo_elig() {
		return r58_crm_comp_col_expo_elig;
	}
	public void setR58_crm_comp_col_expo_elig(BigDecimal r58_crm_comp_col_expo_elig) {
		this.r58_crm_comp_col_expo_elig = r58_crm_comp_col_expo_elig;
	}
	public BigDecimal getR58_crm_comp_col_elig_expo_vol_adj() {
		return r58_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR58_crm_comp_col_elig_expo_vol_adj(BigDecimal r58_crm_comp_col_elig_expo_vol_adj) {
		this.r58_crm_comp_col_elig_expo_vol_adj = r58_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR58_crm_comp_col_elig_fin_hai() {
		return r58_crm_comp_col_elig_fin_hai;
	}
	public void setR58_crm_comp_col_elig_fin_hai(BigDecimal r58_crm_comp_col_elig_fin_hai) {
		this.r58_crm_comp_col_elig_fin_hai = r58_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR58_crm_comp_col_expo_val() {
		return r58_crm_comp_col_expo_val;
	}
	public void setR58_crm_comp_col_expo_val(BigDecimal r58_crm_comp_col_expo_val) {
		this.r58_crm_comp_col_expo_val = r58_crm_comp_col_expo_val;
	}
	public BigDecimal getR58_rwa_elig_expo_not_cov_crm() {
		return r58_rwa_elig_expo_not_cov_crm;
	}
	public void setR58_rwa_elig_expo_not_cov_crm(BigDecimal r58_rwa_elig_expo_not_cov_crm) {
		this.r58_rwa_elig_expo_not_cov_crm = r58_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR58_rwa_unsec_expo_cre_ris() {
		return r58_rwa_unsec_expo_cre_ris;
	}
	public void setR58_rwa_unsec_expo_cre_ris(BigDecimal r58_rwa_unsec_expo_cre_ris) {
		this.r58_rwa_unsec_expo_cre_ris = r58_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR58_rwa_unsec_expo() {
		return r58_rwa_unsec_expo;
	}
	public void setR58_rwa_unsec_expo(BigDecimal r58_rwa_unsec_expo) {
		this.r58_rwa_unsec_expo = r58_rwa_unsec_expo;
	}
	public BigDecimal getR58_rwa_tot_ris_wei_ass() {
		return r58_rwa_tot_ris_wei_ass;
	}
	public void setR58_rwa_tot_ris_wei_ass(BigDecimal r58_rwa_tot_ris_wei_ass) {
		this.r58_rwa_tot_ris_wei_ass = r58_rwa_tot_ris_wei_ass;
	}
	public String getR59_exposure_class() {
		return r59_exposure_class;
	}
	public void setR59_exposure_class(String r59_exposure_class) {
		this.r59_exposure_class = r59_exposure_class;
	}
	public BigDecimal getR59_expo_crm() {
		return r59_expo_crm;
	}
	public void setR59_expo_crm(BigDecimal r59_expo_crm) {
		this.r59_expo_crm = r59_expo_crm;
	}
	public BigDecimal getR59_spe_pro_expo() {
		return r59_spe_pro_expo;
	}
	public void setR59_spe_pro_expo(BigDecimal r59_spe_pro_expo) {
		this.r59_spe_pro_expo = r59_spe_pro_expo;
	}
	public BigDecimal getR59_amt_elig_sht_net() {
		return r59_amt_elig_sht_net;
	}
	public void setR59_amt_elig_sht_net(BigDecimal r59_amt_elig_sht_net) {
		this.r59_amt_elig_sht_net = r59_amt_elig_sht_net;
	}
	public BigDecimal getR59_tot_expo_net_spe() {
		return r59_tot_expo_net_spe;
	}
	public void setR59_tot_expo_net_spe(BigDecimal r59_tot_expo_net_spe) {
		this.r59_tot_expo_net_spe = r59_tot_expo_net_spe;
	}
	public BigDecimal getR59_crm_sub_elig_sub_app() {
		return r59_crm_sub_elig_sub_app;
	}
	public void setR59_crm_sub_elig_sub_app(BigDecimal r59_crm_sub_elig_sub_app) {
		this.r59_crm_sub_elig_sub_app = r59_crm_sub_elig_sub_app;
	}
	public BigDecimal getR59_crm_sub_non_col_guar() {
		return r59_crm_sub_non_col_guar;
	}
	public void setR59_crm_sub_non_col_guar(BigDecimal r59_crm_sub_non_col_guar) {
		this.r59_crm_sub_non_col_guar = r59_crm_sub_non_col_guar;
	}
	public BigDecimal getR59_crm_sub_non_col_cre_der() {
		return r59_crm_sub_non_col_cre_der;
	}
	public void setR59_crm_sub_non_col_cre_der(BigDecimal r59_crm_sub_non_col_cre_der) {
		this.r59_crm_sub_non_col_cre_der = r59_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR59_crm_sub_col_elig_cash() {
		return r59_crm_sub_col_elig_cash;
	}
	public void setR59_crm_sub_col_elig_cash(BigDecimal r59_crm_sub_col_elig_cash) {
		this.r59_crm_sub_col_elig_cash = r59_crm_sub_col_elig_cash;
	}
	public BigDecimal getR59_crm_sub_col_elig_trea_bills() {
		return r59_crm_sub_col_elig_trea_bills;
	}
	public void setR59_crm_sub_col_elig_trea_bills(BigDecimal r59_crm_sub_col_elig_trea_bills) {
		this.r59_crm_sub_col_elig_trea_bills = r59_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR59_crm_sub_col_elig_deb_sec() {
		return r59_crm_sub_col_elig_deb_sec;
	}
	public void setR59_crm_sub_col_elig_deb_sec(BigDecimal r59_crm_sub_col_elig_deb_sec) {
		this.r59_crm_sub_col_elig_deb_sec = r59_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR59_crm_sub_col_elig_equi() {
		return r59_crm_sub_col_elig_equi;
	}
	public void setR59_crm_sub_col_elig_equi(BigDecimal r59_crm_sub_col_elig_equi) {
		this.r59_crm_sub_col_elig_equi = r59_crm_sub_col_elig_equi;
	}
	public BigDecimal getR59_crm_sub_col_elig_unit_tru() {
		return r59_crm_sub_col_elig_unit_tru;
	}
	public void setR59_crm_sub_col_elig_unit_tru(BigDecimal r59_crm_sub_col_elig_unit_tru) {
		this.r59_crm_sub_col_elig_unit_tru = r59_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR59_crm_sub_col_exp_cov() {
		return r59_crm_sub_col_exp_cov;
	}
	public void setR59_crm_sub_col_exp_cov(BigDecimal r59_crm_sub_col_exp_cov) {
		this.r59_crm_sub_col_exp_cov = r59_crm_sub_col_exp_cov;
	}
	public BigDecimal getR59_crm_sub_col_elig_exp_not_cov() {
		return r59_crm_sub_col_elig_exp_not_cov;
	}
	public void setR59_crm_sub_col_elig_exp_not_cov(BigDecimal r59_crm_sub_col_elig_exp_not_cov) {
		this.r59_crm_sub_col_elig_exp_not_cov = r59_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR59_crm_sub_rwa_ris_crm() {
		return r59_crm_sub_rwa_ris_crm;
	}
	public void setR59_crm_sub_rwa_ris_crm(BigDecimal r59_crm_sub_rwa_ris_crm) {
		this.r59_crm_sub_rwa_ris_crm = r59_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR59_crm_sub_rwa_cov_crm() {
		return r59_crm_sub_rwa_cov_crm;
	}
	public void setR59_crm_sub_rwa_cov_crm(BigDecimal r59_crm_sub_rwa_cov_crm) {
		this.r59_crm_sub_rwa_cov_crm = r59_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR59_crm_sub_rwa_org_cou() {
		return r59_crm_sub_rwa_org_cou;
	}
	public void setR59_crm_sub_rwa_org_cou(BigDecimal r59_crm_sub_rwa_org_cou) {
		this.r59_crm_sub_rwa_org_cou = r59_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR59_crm_sub_rwa_not_cov_crm() {
		return r59_crm_sub_rwa_not_cov_crm;
	}
	public void setR59_crm_sub_rwa_not_cov_crm(BigDecimal r59_crm_sub_rwa_not_cov_crm) {
		this.r59_crm_sub_rwa_not_cov_crm = r59_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR59_crm_comp_col_expo_elig() {
		return r59_crm_comp_col_expo_elig;
	}
	public void setR59_crm_comp_col_expo_elig(BigDecimal r59_crm_comp_col_expo_elig) {
		this.r59_crm_comp_col_expo_elig = r59_crm_comp_col_expo_elig;
	}
	public BigDecimal getR59_crm_comp_col_elig_expo_vol_adj() {
		return r59_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR59_crm_comp_col_elig_expo_vol_adj(BigDecimal r59_crm_comp_col_elig_expo_vol_adj) {
		this.r59_crm_comp_col_elig_expo_vol_adj = r59_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR59_crm_comp_col_elig_fin_hai() {
		return r59_crm_comp_col_elig_fin_hai;
	}
	public void setR59_crm_comp_col_elig_fin_hai(BigDecimal r59_crm_comp_col_elig_fin_hai) {
		this.r59_crm_comp_col_elig_fin_hai = r59_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR59_crm_comp_col_expo_val() {
		return r59_crm_comp_col_expo_val;
	}
	public void setR59_crm_comp_col_expo_val(BigDecimal r59_crm_comp_col_expo_val) {
		this.r59_crm_comp_col_expo_val = r59_crm_comp_col_expo_val;
	}
	public BigDecimal getR59_rwa_elig_expo_not_cov_crm() {
		return r59_rwa_elig_expo_not_cov_crm;
	}
	public void setR59_rwa_elig_expo_not_cov_crm(BigDecimal r59_rwa_elig_expo_not_cov_crm) {
		this.r59_rwa_elig_expo_not_cov_crm = r59_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR59_rwa_unsec_expo_cre_ris() {
		return r59_rwa_unsec_expo_cre_ris;
	}
	public void setR59_rwa_unsec_expo_cre_ris(BigDecimal r59_rwa_unsec_expo_cre_ris) {
		this.r59_rwa_unsec_expo_cre_ris = r59_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR59_rwa_unsec_expo() {
		return r59_rwa_unsec_expo;
	}
	public void setR59_rwa_unsec_expo(BigDecimal r59_rwa_unsec_expo) {
		this.r59_rwa_unsec_expo = r59_rwa_unsec_expo;
	}
	public BigDecimal getR59_rwa_tot_ris_wei_ass() {
		return r59_rwa_tot_ris_wei_ass;
	}
	public void setR59_rwa_tot_ris_wei_ass(BigDecimal r59_rwa_tot_ris_wei_ass) {
		this.r59_rwa_tot_ris_wei_ass = r59_rwa_tot_ris_wei_ass;
	}
	public String getR71_exposure_class() {
		return r71_exposure_class;
	}
	public void setR71_exposure_class(String r71_exposure_class) {
		this.r71_exposure_class = r71_exposure_class;
	}
	public BigDecimal getR71_expo_crm() {
		return r71_expo_crm;
	}
	public void setR71_expo_crm(BigDecimal r71_expo_crm) {
		this.r71_expo_crm = r71_expo_crm;
	}
	public BigDecimal getR71_spe_pro_expo() {
		return r71_spe_pro_expo;
	}
	public void setR71_spe_pro_expo(BigDecimal r71_spe_pro_expo) {
		this.r71_spe_pro_expo = r71_spe_pro_expo;
	}
	public BigDecimal getR71_amt_elig_sht_net() {
		return r71_amt_elig_sht_net;
	}
	public void setR71_amt_elig_sht_net(BigDecimal r71_amt_elig_sht_net) {
		this.r71_amt_elig_sht_net = r71_amt_elig_sht_net;
	}
	public BigDecimal getR71_tot_expo_net_spe() {
		return r71_tot_expo_net_spe;
	}
	public void setR71_tot_expo_net_spe(BigDecimal r71_tot_expo_net_spe) {
		this.r71_tot_expo_net_spe = r71_tot_expo_net_spe;
	}
	public BigDecimal getR71_crm_sub_elig_sub_app() {
		return r71_crm_sub_elig_sub_app;
	}
	public void setR71_crm_sub_elig_sub_app(BigDecimal r71_crm_sub_elig_sub_app) {
		this.r71_crm_sub_elig_sub_app = r71_crm_sub_elig_sub_app;
	}
	public BigDecimal getR71_crm_sub_non_col_guar() {
		return r71_crm_sub_non_col_guar;
	}
	public void setR71_crm_sub_non_col_guar(BigDecimal r71_crm_sub_non_col_guar) {
		this.r71_crm_sub_non_col_guar = r71_crm_sub_non_col_guar;
	}
	public BigDecimal getR71_crm_sub_non_col_cre_der() {
		return r71_crm_sub_non_col_cre_der;
	}
	public void setR71_crm_sub_non_col_cre_der(BigDecimal r71_crm_sub_non_col_cre_der) {
		this.r71_crm_sub_non_col_cre_der = r71_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR71_crm_sub_col_elig_cash() {
		return r71_crm_sub_col_elig_cash;
	}
	public void setR71_crm_sub_col_elig_cash(BigDecimal r71_crm_sub_col_elig_cash) {
		this.r71_crm_sub_col_elig_cash = r71_crm_sub_col_elig_cash;
	}
	public BigDecimal getR71_crm_sub_col_elig_trea_bills() {
		return r71_crm_sub_col_elig_trea_bills;
	}
	public void setR71_crm_sub_col_elig_trea_bills(BigDecimal r71_crm_sub_col_elig_trea_bills) {
		this.r71_crm_sub_col_elig_trea_bills = r71_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR71_crm_sub_col_elig_deb_sec() {
		return r71_crm_sub_col_elig_deb_sec;
	}
	public void setR71_crm_sub_col_elig_deb_sec(BigDecimal r71_crm_sub_col_elig_deb_sec) {
		this.r71_crm_sub_col_elig_deb_sec = r71_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR71_crm_sub_col_elig_equi() {
		return r71_crm_sub_col_elig_equi;
	}
	public void setR71_crm_sub_col_elig_equi(BigDecimal r71_crm_sub_col_elig_equi) {
		this.r71_crm_sub_col_elig_equi = r71_crm_sub_col_elig_equi;
	}
	public BigDecimal getR71_crm_sub_col_elig_unit_tru() {
		return r71_crm_sub_col_elig_unit_tru;
	}
	public void setR71_crm_sub_col_elig_unit_tru(BigDecimal r71_crm_sub_col_elig_unit_tru) {
		this.r71_crm_sub_col_elig_unit_tru = r71_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR71_crm_sub_col_exp_cov() {
		return r71_crm_sub_col_exp_cov;
	}
	public void setR71_crm_sub_col_exp_cov(BigDecimal r71_crm_sub_col_exp_cov) {
		this.r71_crm_sub_col_exp_cov = r71_crm_sub_col_exp_cov;
	}
	public BigDecimal getR71_crm_sub_col_elig_exp_not_cov() {
		return r71_crm_sub_col_elig_exp_not_cov;
	}
	public void setR71_crm_sub_col_elig_exp_not_cov(BigDecimal r71_crm_sub_col_elig_exp_not_cov) {
		this.r71_crm_sub_col_elig_exp_not_cov = r71_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR71_crm_sub_rwa_ris_crm() {
		return r71_crm_sub_rwa_ris_crm;
	}
	public void setR71_crm_sub_rwa_ris_crm(BigDecimal r71_crm_sub_rwa_ris_crm) {
		this.r71_crm_sub_rwa_ris_crm = r71_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR71_crm_sub_rwa_cov_crm() {
		return r71_crm_sub_rwa_cov_crm;
	}
	public void setR71_crm_sub_rwa_cov_crm(BigDecimal r71_crm_sub_rwa_cov_crm) {
		this.r71_crm_sub_rwa_cov_crm = r71_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR71_crm_sub_rwa_org_cou() {
		return r71_crm_sub_rwa_org_cou;
	}
	public void setR71_crm_sub_rwa_org_cou(BigDecimal r71_crm_sub_rwa_org_cou) {
		this.r71_crm_sub_rwa_org_cou = r71_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR71_crm_sub_rwa_not_cov_crm() {
		return r71_crm_sub_rwa_not_cov_crm;
	}
	public void setR71_crm_sub_rwa_not_cov_crm(BigDecimal r71_crm_sub_rwa_not_cov_crm) {
		this.r71_crm_sub_rwa_not_cov_crm = r71_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR71_crm_comp_col_expo_elig() {
		return r71_crm_comp_col_expo_elig;
	}
	public void setR71_crm_comp_col_expo_elig(BigDecimal r71_crm_comp_col_expo_elig) {
		this.r71_crm_comp_col_expo_elig = r71_crm_comp_col_expo_elig;
	}
	public BigDecimal getR71_crm_comp_col_elig_expo_vol_adj() {
		return r71_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR71_crm_comp_col_elig_expo_vol_adj(BigDecimal r71_crm_comp_col_elig_expo_vol_adj) {
		this.r71_crm_comp_col_elig_expo_vol_adj = r71_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR71_crm_comp_col_elig_fin_hai() {
		return r71_crm_comp_col_elig_fin_hai;
	}
	public void setR71_crm_comp_col_elig_fin_hai(BigDecimal r71_crm_comp_col_elig_fin_hai) {
		this.r71_crm_comp_col_elig_fin_hai = r71_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR71_crm_comp_col_expo_val() {
		return r71_crm_comp_col_expo_val;
	}
	public void setR71_crm_comp_col_expo_val(BigDecimal r71_crm_comp_col_expo_val) {
		this.r71_crm_comp_col_expo_val = r71_crm_comp_col_expo_val;
	}
	public BigDecimal getR71_rwa_elig_expo_not_cov_crm() {
		return r71_rwa_elig_expo_not_cov_crm;
	}
	public void setR71_rwa_elig_expo_not_cov_crm(BigDecimal r71_rwa_elig_expo_not_cov_crm) {
		this.r71_rwa_elig_expo_not_cov_crm = r71_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR71_rwa_unsec_expo_cre_ris() {
		return r71_rwa_unsec_expo_cre_ris;
	}
	public void setR71_rwa_unsec_expo_cre_ris(BigDecimal r71_rwa_unsec_expo_cre_ris) {
		this.r71_rwa_unsec_expo_cre_ris = r71_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR71_rwa_unsec_expo() {
		return r71_rwa_unsec_expo;
	}
	public void setR71_rwa_unsec_expo(BigDecimal r71_rwa_unsec_expo) {
		this.r71_rwa_unsec_expo = r71_rwa_unsec_expo;
	}
	public BigDecimal getR71_rwa_tot_ris_wei_ass() {
		return r71_rwa_tot_ris_wei_ass;
	}
	public void setR71_rwa_tot_ris_wei_ass(BigDecimal r71_rwa_tot_ris_wei_ass) {
		this.r71_rwa_tot_ris_wei_ass = r71_rwa_tot_ris_wei_ass;
	}
	public String getR72_exposure_class() {
		return r72_exposure_class;
	}
	public void setR72_exposure_class(String r72_exposure_class) {
		this.r72_exposure_class = r72_exposure_class;
	}
	public BigDecimal getR72_expo_crm() {
		return r72_expo_crm;
	}
	public void setR72_expo_crm(BigDecimal r72_expo_crm) {
		this.r72_expo_crm = r72_expo_crm;
	}
	public BigDecimal getR72_spe_pro_expo() {
		return r72_spe_pro_expo;
	}
	public void setR72_spe_pro_expo(BigDecimal r72_spe_pro_expo) {
		this.r72_spe_pro_expo = r72_spe_pro_expo;
	}
	public BigDecimal getR72_amt_elig_sht_net() {
		return r72_amt_elig_sht_net;
	}
	public void setR72_amt_elig_sht_net(BigDecimal r72_amt_elig_sht_net) {
		this.r72_amt_elig_sht_net = r72_amt_elig_sht_net;
	}
	public BigDecimal getR72_tot_expo_net_spe() {
		return r72_tot_expo_net_spe;
	}
	public void setR72_tot_expo_net_spe(BigDecimal r72_tot_expo_net_spe) {
		this.r72_tot_expo_net_spe = r72_tot_expo_net_spe;
	}
	public BigDecimal getR72_crm_sub_elig_sub_app() {
		return r72_crm_sub_elig_sub_app;
	}
	public void setR72_crm_sub_elig_sub_app(BigDecimal r72_crm_sub_elig_sub_app) {
		this.r72_crm_sub_elig_sub_app = r72_crm_sub_elig_sub_app;
	}
	public BigDecimal getR72_crm_sub_non_col_guar() {
		return r72_crm_sub_non_col_guar;
	}
	public void setR72_crm_sub_non_col_guar(BigDecimal r72_crm_sub_non_col_guar) {
		this.r72_crm_sub_non_col_guar = r72_crm_sub_non_col_guar;
	}
	public BigDecimal getR72_crm_sub_non_col_cre_der() {
		return r72_crm_sub_non_col_cre_der;
	}
	public void setR72_crm_sub_non_col_cre_der(BigDecimal r72_crm_sub_non_col_cre_der) {
		this.r72_crm_sub_non_col_cre_der = r72_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR72_crm_sub_col_elig_cash() {
		return r72_crm_sub_col_elig_cash;
	}
	public void setR72_crm_sub_col_elig_cash(BigDecimal r72_crm_sub_col_elig_cash) {
		this.r72_crm_sub_col_elig_cash = r72_crm_sub_col_elig_cash;
	}
	public BigDecimal getR72_crm_sub_col_elig_trea_bills() {
		return r72_crm_sub_col_elig_trea_bills;
	}
	public void setR72_crm_sub_col_elig_trea_bills(BigDecimal r72_crm_sub_col_elig_trea_bills) {
		this.r72_crm_sub_col_elig_trea_bills = r72_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR72_crm_sub_col_elig_deb_sec() {
		return r72_crm_sub_col_elig_deb_sec;
	}
	public void setR72_crm_sub_col_elig_deb_sec(BigDecimal r72_crm_sub_col_elig_deb_sec) {
		this.r72_crm_sub_col_elig_deb_sec = r72_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR72_crm_sub_col_elig_equi() {
		return r72_crm_sub_col_elig_equi;
	}
	public void setR72_crm_sub_col_elig_equi(BigDecimal r72_crm_sub_col_elig_equi) {
		this.r72_crm_sub_col_elig_equi = r72_crm_sub_col_elig_equi;
	}
	public BigDecimal getR72_crm_sub_col_elig_unit_tru() {
		return r72_crm_sub_col_elig_unit_tru;
	}
	public void setR72_crm_sub_col_elig_unit_tru(BigDecimal r72_crm_sub_col_elig_unit_tru) {
		this.r72_crm_sub_col_elig_unit_tru = r72_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR72_crm_sub_col_exp_cov() {
		return r72_crm_sub_col_exp_cov;
	}
	public void setR72_crm_sub_col_exp_cov(BigDecimal r72_crm_sub_col_exp_cov) {
		this.r72_crm_sub_col_exp_cov = r72_crm_sub_col_exp_cov;
	}
	public BigDecimal getR72_crm_sub_col_elig_exp_not_cov() {
		return r72_crm_sub_col_elig_exp_not_cov;
	}
	public void setR72_crm_sub_col_elig_exp_not_cov(BigDecimal r72_crm_sub_col_elig_exp_not_cov) {
		this.r72_crm_sub_col_elig_exp_not_cov = r72_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR72_crm_sub_rwa_ris_crm() {
		return r72_crm_sub_rwa_ris_crm;
	}
	public void setR72_crm_sub_rwa_ris_crm(BigDecimal r72_crm_sub_rwa_ris_crm) {
		this.r72_crm_sub_rwa_ris_crm = r72_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR72_crm_sub_rwa_cov_crm() {
		return r72_crm_sub_rwa_cov_crm;
	}
	public void setR72_crm_sub_rwa_cov_crm(BigDecimal r72_crm_sub_rwa_cov_crm) {
		this.r72_crm_sub_rwa_cov_crm = r72_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR72_crm_sub_rwa_org_cou() {
		return r72_crm_sub_rwa_org_cou;
	}
	public void setR72_crm_sub_rwa_org_cou(BigDecimal r72_crm_sub_rwa_org_cou) {
		this.r72_crm_sub_rwa_org_cou = r72_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR72_crm_sub_rwa_not_cov_crm() {
		return r72_crm_sub_rwa_not_cov_crm;
	}
	public void setR72_crm_sub_rwa_not_cov_crm(BigDecimal r72_crm_sub_rwa_not_cov_crm) {
		this.r72_crm_sub_rwa_not_cov_crm = r72_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR72_crm_comp_col_expo_elig() {
		return r72_crm_comp_col_expo_elig;
	}
	public void setR72_crm_comp_col_expo_elig(BigDecimal r72_crm_comp_col_expo_elig) {
		this.r72_crm_comp_col_expo_elig = r72_crm_comp_col_expo_elig;
	}
	public BigDecimal getR72_crm_comp_col_elig_expo_vol_adj() {
		return r72_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR72_crm_comp_col_elig_expo_vol_adj(BigDecimal r72_crm_comp_col_elig_expo_vol_adj) {
		this.r72_crm_comp_col_elig_expo_vol_adj = r72_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR72_crm_comp_col_elig_fin_hai() {
		return r72_crm_comp_col_elig_fin_hai;
	}
	public void setR72_crm_comp_col_elig_fin_hai(BigDecimal r72_crm_comp_col_elig_fin_hai) {
		this.r72_crm_comp_col_elig_fin_hai = r72_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR72_crm_comp_col_expo_val() {
		return r72_crm_comp_col_expo_val;
	}
	public void setR72_crm_comp_col_expo_val(BigDecimal r72_crm_comp_col_expo_val) {
		this.r72_crm_comp_col_expo_val = r72_crm_comp_col_expo_val;
	}
	public BigDecimal getR72_rwa_elig_expo_not_cov_crm() {
		return r72_rwa_elig_expo_not_cov_crm;
	}
	public void setR72_rwa_elig_expo_not_cov_crm(BigDecimal r72_rwa_elig_expo_not_cov_crm) {
		this.r72_rwa_elig_expo_not_cov_crm = r72_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR72_rwa_unsec_expo_cre_ris() {
		return r72_rwa_unsec_expo_cre_ris;
	}
	public void setR72_rwa_unsec_expo_cre_ris(BigDecimal r72_rwa_unsec_expo_cre_ris) {
		this.r72_rwa_unsec_expo_cre_ris = r72_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR72_rwa_unsec_expo() {
		return r72_rwa_unsec_expo;
	}
	public void setR72_rwa_unsec_expo(BigDecimal r72_rwa_unsec_expo) {
		this.r72_rwa_unsec_expo = r72_rwa_unsec_expo;
	}
	public BigDecimal getR72_rwa_tot_ris_wei_ass() {
		return r72_rwa_tot_ris_wei_ass;
	}
	public void setR72_rwa_tot_ris_wei_ass(BigDecimal r72_rwa_tot_ris_wei_ass) {
		this.r72_rwa_tot_ris_wei_ass = r72_rwa_tot_ris_wei_ass;
	}
	public String getR83_exposure_class() {
		return r83_exposure_class;
	}
	public void setR83_exposure_class(String r83_exposure_class) {
		this.r83_exposure_class = r83_exposure_class;
	}
	public BigDecimal getR83_expo_crm() {
		return r83_expo_crm;
	}
	public void setR83_expo_crm(BigDecimal r83_expo_crm) {
		this.r83_expo_crm = r83_expo_crm;
	}
	public BigDecimal getR83_spe_pro_expo() {
		return r83_spe_pro_expo;
	}
	public void setR83_spe_pro_expo(BigDecimal r83_spe_pro_expo) {
		this.r83_spe_pro_expo = r83_spe_pro_expo;
	}
	public BigDecimal getR83_amt_elig_sht_net() {
		return r83_amt_elig_sht_net;
	}
	public void setR83_amt_elig_sht_net(BigDecimal r83_amt_elig_sht_net) {
		this.r83_amt_elig_sht_net = r83_amt_elig_sht_net;
	}
	public BigDecimal getR83_tot_expo_net_spe() {
		return r83_tot_expo_net_spe;
	}
	public void setR83_tot_expo_net_spe(BigDecimal r83_tot_expo_net_spe) {
		this.r83_tot_expo_net_spe = r83_tot_expo_net_spe;
	}
	public BigDecimal getR83_crm_sub_elig_sub_app() {
		return r83_crm_sub_elig_sub_app;
	}
	public void setR83_crm_sub_elig_sub_app(BigDecimal r83_crm_sub_elig_sub_app) {
		this.r83_crm_sub_elig_sub_app = r83_crm_sub_elig_sub_app;
	}
	public BigDecimal getR83_crm_sub_non_col_guar() {
		return r83_crm_sub_non_col_guar;
	}
	public void setR83_crm_sub_non_col_guar(BigDecimal r83_crm_sub_non_col_guar) {
		this.r83_crm_sub_non_col_guar = r83_crm_sub_non_col_guar;
	}
	public BigDecimal getR83_crm_sub_non_col_cre_der() {
		return r83_crm_sub_non_col_cre_der;
	}
	public void setR83_crm_sub_non_col_cre_der(BigDecimal r83_crm_sub_non_col_cre_der) {
		this.r83_crm_sub_non_col_cre_der = r83_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR83_crm_sub_col_elig_cash() {
		return r83_crm_sub_col_elig_cash;
	}
	public void setR83_crm_sub_col_elig_cash(BigDecimal r83_crm_sub_col_elig_cash) {
		this.r83_crm_sub_col_elig_cash = r83_crm_sub_col_elig_cash;
	}
	public BigDecimal getR83_crm_sub_col_elig_trea_bills() {
		return r83_crm_sub_col_elig_trea_bills;
	}
	public void setR83_crm_sub_col_elig_trea_bills(BigDecimal r83_crm_sub_col_elig_trea_bills) {
		this.r83_crm_sub_col_elig_trea_bills = r83_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR83_crm_sub_col_elig_deb_sec() {
		return r83_crm_sub_col_elig_deb_sec;
	}
	public void setR83_crm_sub_col_elig_deb_sec(BigDecimal r83_crm_sub_col_elig_deb_sec) {
		this.r83_crm_sub_col_elig_deb_sec = r83_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR83_crm_sub_col_elig_equi() {
		return r83_crm_sub_col_elig_equi;
	}
	public void setR83_crm_sub_col_elig_equi(BigDecimal r83_crm_sub_col_elig_equi) {
		this.r83_crm_sub_col_elig_equi = r83_crm_sub_col_elig_equi;
	}
	public BigDecimal getR83_crm_sub_col_elig_unit_tru() {
		return r83_crm_sub_col_elig_unit_tru;
	}
	public void setR83_crm_sub_col_elig_unit_tru(BigDecimal r83_crm_sub_col_elig_unit_tru) {
		this.r83_crm_sub_col_elig_unit_tru = r83_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR83_crm_sub_col_exp_cov() {
		return r83_crm_sub_col_exp_cov;
	}
	public void setR83_crm_sub_col_exp_cov(BigDecimal r83_crm_sub_col_exp_cov) {
		this.r83_crm_sub_col_exp_cov = r83_crm_sub_col_exp_cov;
	}
	public BigDecimal getR83_crm_sub_col_elig_exp_not_cov() {
		return r83_crm_sub_col_elig_exp_not_cov;
	}
	public void setR83_crm_sub_col_elig_exp_not_cov(BigDecimal r83_crm_sub_col_elig_exp_not_cov) {
		this.r83_crm_sub_col_elig_exp_not_cov = r83_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR83_crm_sub_rwa_ris_crm() {
		return r83_crm_sub_rwa_ris_crm;
	}
	public void setR83_crm_sub_rwa_ris_crm(BigDecimal r83_crm_sub_rwa_ris_crm) {
		this.r83_crm_sub_rwa_ris_crm = r83_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR83_crm_sub_rwa_cov_crm() {
		return r83_crm_sub_rwa_cov_crm;
	}
	public void setR83_crm_sub_rwa_cov_crm(BigDecimal r83_crm_sub_rwa_cov_crm) {
		this.r83_crm_sub_rwa_cov_crm = r83_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR83_crm_sub_rwa_org_cou() {
		return r83_crm_sub_rwa_org_cou;
	}
	public void setR83_crm_sub_rwa_org_cou(BigDecimal r83_crm_sub_rwa_org_cou) {
		this.r83_crm_sub_rwa_org_cou = r83_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR83_crm_sub_rwa_not_cov_crm() {
		return r83_crm_sub_rwa_not_cov_crm;
	}
	public void setR83_crm_sub_rwa_not_cov_crm(BigDecimal r83_crm_sub_rwa_not_cov_crm) {
		this.r83_crm_sub_rwa_not_cov_crm = r83_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR83_crm_comp_col_expo_elig() {
		return r83_crm_comp_col_expo_elig;
	}
	public void setR83_crm_comp_col_expo_elig(BigDecimal r83_crm_comp_col_expo_elig) {
		this.r83_crm_comp_col_expo_elig = r83_crm_comp_col_expo_elig;
	}
	public BigDecimal getR83_crm_comp_col_elig_expo_vol_adj() {
		return r83_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR83_crm_comp_col_elig_expo_vol_adj(BigDecimal r83_crm_comp_col_elig_expo_vol_adj) {
		this.r83_crm_comp_col_elig_expo_vol_adj = r83_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR83_crm_comp_col_elig_fin_hai() {
		return r83_crm_comp_col_elig_fin_hai;
	}
	public void setR83_crm_comp_col_elig_fin_hai(BigDecimal r83_crm_comp_col_elig_fin_hai) {
		this.r83_crm_comp_col_elig_fin_hai = r83_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR83_crm_comp_col_expo_val() {
		return r83_crm_comp_col_expo_val;
	}
	public void setR83_crm_comp_col_expo_val(BigDecimal r83_crm_comp_col_expo_val) {
		this.r83_crm_comp_col_expo_val = r83_crm_comp_col_expo_val;
	}
	public BigDecimal getR83_rwa_elig_expo_not_cov_crm() {
		return r83_rwa_elig_expo_not_cov_crm;
	}
	public void setR83_rwa_elig_expo_not_cov_crm(BigDecimal r83_rwa_elig_expo_not_cov_crm) {
		this.r83_rwa_elig_expo_not_cov_crm = r83_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR83_rwa_unsec_expo_cre_ris() {
		return r83_rwa_unsec_expo_cre_ris;
	}
	public void setR83_rwa_unsec_expo_cre_ris(BigDecimal r83_rwa_unsec_expo_cre_ris) {
		this.r83_rwa_unsec_expo_cre_ris = r83_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR83_rwa_unsec_expo() {
		return r83_rwa_unsec_expo;
	}
	public void setR83_rwa_unsec_expo(BigDecimal r83_rwa_unsec_expo) {
		this.r83_rwa_unsec_expo = r83_rwa_unsec_expo;
	}
	public BigDecimal getR83_rwa_tot_ris_wei_ass() {
		return r83_rwa_tot_ris_wei_ass;
	}
	public void setR83_rwa_tot_ris_wei_ass(BigDecimal r83_rwa_tot_ris_wei_ass) {
		this.r83_rwa_tot_ris_wei_ass = r83_rwa_tot_ris_wei_ass;
	}
	public String getR84_exposure_class() {
		return r84_exposure_class;
	}
	public void setR84_exposure_class(String r84_exposure_class) {
		this.r84_exposure_class = r84_exposure_class;
	}
	public BigDecimal getR84_expo_crm() {
		return r84_expo_crm;
	}
	public void setR84_expo_crm(BigDecimal r84_expo_crm) {
		this.r84_expo_crm = r84_expo_crm;
	}
	public BigDecimal getR84_spe_pro_expo() {
		return r84_spe_pro_expo;
	}
	public void setR84_spe_pro_expo(BigDecimal r84_spe_pro_expo) {
		this.r84_spe_pro_expo = r84_spe_pro_expo;
	}
	public BigDecimal getR84_amt_elig_sht_net() {
		return r84_amt_elig_sht_net;
	}
	public void setR84_amt_elig_sht_net(BigDecimal r84_amt_elig_sht_net) {
		this.r84_amt_elig_sht_net = r84_amt_elig_sht_net;
	}
	public BigDecimal getR84_tot_expo_net_spe() {
		return r84_tot_expo_net_spe;
	}
	public void setR84_tot_expo_net_spe(BigDecimal r84_tot_expo_net_spe) {
		this.r84_tot_expo_net_spe = r84_tot_expo_net_spe;
	}
	public BigDecimal getR84_crm_sub_elig_sub_app() {
		return r84_crm_sub_elig_sub_app;
	}
	public void setR84_crm_sub_elig_sub_app(BigDecimal r84_crm_sub_elig_sub_app) {
		this.r84_crm_sub_elig_sub_app = r84_crm_sub_elig_sub_app;
	}
	public BigDecimal getR84_crm_sub_non_col_guar() {
		return r84_crm_sub_non_col_guar;
	}
	public void setR84_crm_sub_non_col_guar(BigDecimal r84_crm_sub_non_col_guar) {
		this.r84_crm_sub_non_col_guar = r84_crm_sub_non_col_guar;
	}
	public BigDecimal getR84_crm_sub_non_col_cre_der() {
		return r84_crm_sub_non_col_cre_der;
	}
	public void setR84_crm_sub_non_col_cre_der(BigDecimal r84_crm_sub_non_col_cre_der) {
		this.r84_crm_sub_non_col_cre_der = r84_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR84_crm_sub_col_elig_cash() {
		return r84_crm_sub_col_elig_cash;
	}
	public void setR84_crm_sub_col_elig_cash(BigDecimal r84_crm_sub_col_elig_cash) {
		this.r84_crm_sub_col_elig_cash = r84_crm_sub_col_elig_cash;
	}
	public BigDecimal getR84_crm_sub_col_elig_trea_bills() {
		return r84_crm_sub_col_elig_trea_bills;
	}
	public void setR84_crm_sub_col_elig_trea_bills(BigDecimal r84_crm_sub_col_elig_trea_bills) {
		this.r84_crm_sub_col_elig_trea_bills = r84_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR84_crm_sub_col_elig_deb_sec() {
		return r84_crm_sub_col_elig_deb_sec;
	}
	public void setR84_crm_sub_col_elig_deb_sec(BigDecimal r84_crm_sub_col_elig_deb_sec) {
		this.r84_crm_sub_col_elig_deb_sec = r84_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR84_crm_sub_col_elig_equi() {
		return r84_crm_sub_col_elig_equi;
	}
	public void setR84_crm_sub_col_elig_equi(BigDecimal r84_crm_sub_col_elig_equi) {
		this.r84_crm_sub_col_elig_equi = r84_crm_sub_col_elig_equi;
	}
	public BigDecimal getR84_crm_sub_col_elig_unit_tru() {
		return r84_crm_sub_col_elig_unit_tru;
	}
	public void setR84_crm_sub_col_elig_unit_tru(BigDecimal r84_crm_sub_col_elig_unit_tru) {
		this.r84_crm_sub_col_elig_unit_tru = r84_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR84_crm_sub_col_exp_cov() {
		return r84_crm_sub_col_exp_cov;
	}
	public void setR84_crm_sub_col_exp_cov(BigDecimal r84_crm_sub_col_exp_cov) {
		this.r84_crm_sub_col_exp_cov = r84_crm_sub_col_exp_cov;
	}
	public BigDecimal getR84_crm_sub_col_elig_exp_not_cov() {
		return r84_crm_sub_col_elig_exp_not_cov;
	}
	public void setR84_crm_sub_col_elig_exp_not_cov(BigDecimal r84_crm_sub_col_elig_exp_not_cov) {
		this.r84_crm_sub_col_elig_exp_not_cov = r84_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR84_crm_sub_rwa_ris_crm() {
		return r84_crm_sub_rwa_ris_crm;
	}
	public void setR84_crm_sub_rwa_ris_crm(BigDecimal r84_crm_sub_rwa_ris_crm) {
		this.r84_crm_sub_rwa_ris_crm = r84_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR84_crm_sub_rwa_cov_crm() {
		return r84_crm_sub_rwa_cov_crm;
	}
	public void setR84_crm_sub_rwa_cov_crm(BigDecimal r84_crm_sub_rwa_cov_crm) {
		this.r84_crm_sub_rwa_cov_crm = r84_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR84_crm_sub_rwa_org_cou() {
		return r84_crm_sub_rwa_org_cou;
	}
	public void setR84_crm_sub_rwa_org_cou(BigDecimal r84_crm_sub_rwa_org_cou) {
		this.r84_crm_sub_rwa_org_cou = r84_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR84_crm_sub_rwa_not_cov_crm() {
		return r84_crm_sub_rwa_not_cov_crm;
	}
	public void setR84_crm_sub_rwa_not_cov_crm(BigDecimal r84_crm_sub_rwa_not_cov_crm) {
		this.r84_crm_sub_rwa_not_cov_crm = r84_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR84_crm_comp_col_expo_elig() {
		return r84_crm_comp_col_expo_elig;
	}
	public void setR84_crm_comp_col_expo_elig(BigDecimal r84_crm_comp_col_expo_elig) {
		this.r84_crm_comp_col_expo_elig = r84_crm_comp_col_expo_elig;
	}
	public BigDecimal getR84_crm_comp_col_elig_expo_vol_adj() {
		return r84_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR84_crm_comp_col_elig_expo_vol_adj(BigDecimal r84_crm_comp_col_elig_expo_vol_adj) {
		this.r84_crm_comp_col_elig_expo_vol_adj = r84_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR84_crm_comp_col_elig_fin_hai() {
		return r84_crm_comp_col_elig_fin_hai;
	}
	public void setR84_crm_comp_col_elig_fin_hai(BigDecimal r84_crm_comp_col_elig_fin_hai) {
		this.r84_crm_comp_col_elig_fin_hai = r84_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR84_crm_comp_col_expo_val() {
		return r84_crm_comp_col_expo_val;
	}
	public void setR84_crm_comp_col_expo_val(BigDecimal r84_crm_comp_col_expo_val) {
		this.r84_crm_comp_col_expo_val = r84_crm_comp_col_expo_val;
	}
	public BigDecimal getR84_rwa_elig_expo_not_cov_crm() {
		return r84_rwa_elig_expo_not_cov_crm;
	}
	public void setR84_rwa_elig_expo_not_cov_crm(BigDecimal r84_rwa_elig_expo_not_cov_crm) {
		this.r84_rwa_elig_expo_not_cov_crm = r84_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR84_rwa_unsec_expo_cre_ris() {
		return r84_rwa_unsec_expo_cre_ris;
	}
	public void setR84_rwa_unsec_expo_cre_ris(BigDecimal r84_rwa_unsec_expo_cre_ris) {
		this.r84_rwa_unsec_expo_cre_ris = r84_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR84_rwa_unsec_expo() {
		return r84_rwa_unsec_expo;
	}
	public void setR84_rwa_unsec_expo(BigDecimal r84_rwa_unsec_expo) {
		this.r84_rwa_unsec_expo = r84_rwa_unsec_expo;
	}
	public BigDecimal getR84_rwa_tot_ris_wei_ass() {
		return r84_rwa_tot_ris_wei_ass;
	}
	public void setR84_rwa_tot_ris_wei_ass(BigDecimal r84_rwa_tot_ris_wei_ass) {
		this.r84_rwa_tot_ris_wei_ass = r84_rwa_tot_ris_wei_ass;
	}
	public String getR94_exposure_class() {
		return r94_exposure_class;
	}
	public void setR94_exposure_class(String r94_exposure_class) {
		this.r94_exposure_class = r94_exposure_class;
	}
	public BigDecimal getR94_expo_crm() {
		return r94_expo_crm;
	}
	public void setR94_expo_crm(BigDecimal r94_expo_crm) {
		this.r94_expo_crm = r94_expo_crm;
	}
	public BigDecimal getR94_spe_pro_expo() {
		return r94_spe_pro_expo;
	}
	public void setR94_spe_pro_expo(BigDecimal r94_spe_pro_expo) {
		this.r94_spe_pro_expo = r94_spe_pro_expo;
	}
	public BigDecimal getR94_amt_elig_sht_net() {
		return r94_amt_elig_sht_net;
	}
	public void setR94_amt_elig_sht_net(BigDecimal r94_amt_elig_sht_net) {
		this.r94_amt_elig_sht_net = r94_amt_elig_sht_net;
	}
	public BigDecimal getR94_tot_expo_net_spe() {
		return r94_tot_expo_net_spe;
	}
	public void setR94_tot_expo_net_spe(BigDecimal r94_tot_expo_net_spe) {
		this.r94_tot_expo_net_spe = r94_tot_expo_net_spe;
	}
	public BigDecimal getR94_crm_sub_elig_sub_app() {
		return r94_crm_sub_elig_sub_app;
	}
	public void setR94_crm_sub_elig_sub_app(BigDecimal r94_crm_sub_elig_sub_app) {
		this.r94_crm_sub_elig_sub_app = r94_crm_sub_elig_sub_app;
	}
	public BigDecimal getR94_crm_sub_non_col_guar() {
		return r94_crm_sub_non_col_guar;
	}
	public void setR94_crm_sub_non_col_guar(BigDecimal r94_crm_sub_non_col_guar) {
		this.r94_crm_sub_non_col_guar = r94_crm_sub_non_col_guar;
	}
	public BigDecimal getR94_crm_sub_non_col_cre_der() {
		return r94_crm_sub_non_col_cre_der;
	}
	public void setR94_crm_sub_non_col_cre_der(BigDecimal r94_crm_sub_non_col_cre_der) {
		this.r94_crm_sub_non_col_cre_der = r94_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR94_crm_sub_col_elig_cash() {
		return r94_crm_sub_col_elig_cash;
	}
	public void setR94_crm_sub_col_elig_cash(BigDecimal r94_crm_sub_col_elig_cash) {
		this.r94_crm_sub_col_elig_cash = r94_crm_sub_col_elig_cash;
	}
	public BigDecimal getR94_crm_sub_col_elig_trea_bills() {
		return r94_crm_sub_col_elig_trea_bills;
	}
	public void setR94_crm_sub_col_elig_trea_bills(BigDecimal r94_crm_sub_col_elig_trea_bills) {
		this.r94_crm_sub_col_elig_trea_bills = r94_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR94_crm_sub_col_elig_deb_sec() {
		return r94_crm_sub_col_elig_deb_sec;
	}
	public void setR94_crm_sub_col_elig_deb_sec(BigDecimal r94_crm_sub_col_elig_deb_sec) {
		this.r94_crm_sub_col_elig_deb_sec = r94_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR94_crm_sub_col_elig_equi() {
		return r94_crm_sub_col_elig_equi;
	}
	public void setR94_crm_sub_col_elig_equi(BigDecimal r94_crm_sub_col_elig_equi) {
		this.r94_crm_sub_col_elig_equi = r94_crm_sub_col_elig_equi;
	}
	public BigDecimal getR94_crm_sub_col_elig_unit_tru() {
		return r94_crm_sub_col_elig_unit_tru;
	}
	public void setR94_crm_sub_col_elig_unit_tru(BigDecimal r94_crm_sub_col_elig_unit_tru) {
		this.r94_crm_sub_col_elig_unit_tru = r94_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR94_crm_sub_col_exp_cov() {
		return r94_crm_sub_col_exp_cov;
	}
	public void setR94_crm_sub_col_exp_cov(BigDecimal r94_crm_sub_col_exp_cov) {
		this.r94_crm_sub_col_exp_cov = r94_crm_sub_col_exp_cov;
	}
	public BigDecimal getR94_crm_sub_col_elig_exp_not_cov() {
		return r94_crm_sub_col_elig_exp_not_cov;
	}
	public void setR94_crm_sub_col_elig_exp_not_cov(BigDecimal r94_crm_sub_col_elig_exp_not_cov) {
		this.r94_crm_sub_col_elig_exp_not_cov = r94_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR94_crm_sub_rwa_ris_crm() {
		return r94_crm_sub_rwa_ris_crm;
	}
	public void setR94_crm_sub_rwa_ris_crm(BigDecimal r94_crm_sub_rwa_ris_crm) {
		this.r94_crm_sub_rwa_ris_crm = r94_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR94_crm_sub_rwa_cov_crm() {
		return r94_crm_sub_rwa_cov_crm;
	}
	public void setR94_crm_sub_rwa_cov_crm(BigDecimal r94_crm_sub_rwa_cov_crm) {
		this.r94_crm_sub_rwa_cov_crm = r94_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR94_crm_sub_rwa_org_cou() {
		return r94_crm_sub_rwa_org_cou;
	}
	public void setR94_crm_sub_rwa_org_cou(BigDecimal r94_crm_sub_rwa_org_cou) {
		this.r94_crm_sub_rwa_org_cou = r94_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR94_crm_sub_rwa_not_cov_crm() {
		return r94_crm_sub_rwa_not_cov_crm;
	}
	public void setR94_crm_sub_rwa_not_cov_crm(BigDecimal r94_crm_sub_rwa_not_cov_crm) {
		this.r94_crm_sub_rwa_not_cov_crm = r94_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR94_crm_comp_col_expo_elig() {
		return r94_crm_comp_col_expo_elig;
	}
	public void setR94_crm_comp_col_expo_elig(BigDecimal r94_crm_comp_col_expo_elig) {
		this.r94_crm_comp_col_expo_elig = r94_crm_comp_col_expo_elig;
	}
	public BigDecimal getR94_crm_comp_col_elig_expo_vol_adj() {
		return r94_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR94_crm_comp_col_elig_expo_vol_adj(BigDecimal r94_crm_comp_col_elig_expo_vol_adj) {
		this.r94_crm_comp_col_elig_expo_vol_adj = r94_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR94_crm_comp_col_elig_fin_hai() {
		return r94_crm_comp_col_elig_fin_hai;
	}
	public void setR94_crm_comp_col_elig_fin_hai(BigDecimal r94_crm_comp_col_elig_fin_hai) {
		this.r94_crm_comp_col_elig_fin_hai = r94_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR94_crm_comp_col_expo_val() {
		return r94_crm_comp_col_expo_val;
	}
	public void setR94_crm_comp_col_expo_val(BigDecimal r94_crm_comp_col_expo_val) {
		this.r94_crm_comp_col_expo_val = r94_crm_comp_col_expo_val;
	}
	public BigDecimal getR94_rwa_elig_expo_not_cov_crm() {
		return r94_rwa_elig_expo_not_cov_crm;
	}
	public void setR94_rwa_elig_expo_not_cov_crm(BigDecimal r94_rwa_elig_expo_not_cov_crm) {
		this.r94_rwa_elig_expo_not_cov_crm = r94_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR94_rwa_unsec_expo_cre_ris() {
		return r94_rwa_unsec_expo_cre_ris;
	}
	public void setR94_rwa_unsec_expo_cre_ris(BigDecimal r94_rwa_unsec_expo_cre_ris) {
		this.r94_rwa_unsec_expo_cre_ris = r94_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR94_rwa_unsec_expo() {
		return r94_rwa_unsec_expo;
	}
	public void setR94_rwa_unsec_expo(BigDecimal r94_rwa_unsec_expo) {
		this.r94_rwa_unsec_expo = r94_rwa_unsec_expo;
	}
	public BigDecimal getR94_rwa_tot_ris_wei_ass() {
		return r94_rwa_tot_ris_wei_ass;
	}
	public void setR94_rwa_tot_ris_wei_ass(BigDecimal r94_rwa_tot_ris_wei_ass) {
		this.r94_rwa_tot_ris_wei_ass = r94_rwa_tot_ris_wei_ass;
	}
	public String getR95_exposure_class() {
		return r95_exposure_class;
	}
	public void setR95_exposure_class(String r95_exposure_class) {
		this.r95_exposure_class = r95_exposure_class;
	}
	public BigDecimal getR95_expo_crm() {
		return r95_expo_crm;
	}
	public void setR95_expo_crm(BigDecimal r95_expo_crm) {
		this.r95_expo_crm = r95_expo_crm;
	}
	public BigDecimal getR95_spe_pro_expo() {
		return r95_spe_pro_expo;
	}
	public void setR95_spe_pro_expo(BigDecimal r95_spe_pro_expo) {
		this.r95_spe_pro_expo = r95_spe_pro_expo;
	}
	public BigDecimal getR95_amt_elig_sht_net() {
		return r95_amt_elig_sht_net;
	}
	public void setR95_amt_elig_sht_net(BigDecimal r95_amt_elig_sht_net) {
		this.r95_amt_elig_sht_net = r95_amt_elig_sht_net;
	}
	public BigDecimal getR95_tot_expo_net_spe() {
		return r95_tot_expo_net_spe;
	}
	public void setR95_tot_expo_net_spe(BigDecimal r95_tot_expo_net_spe) {
		this.r95_tot_expo_net_spe = r95_tot_expo_net_spe;
	}
	public BigDecimal getR95_crm_sub_elig_sub_app() {
		return r95_crm_sub_elig_sub_app;
	}
	public void setR95_crm_sub_elig_sub_app(BigDecimal r95_crm_sub_elig_sub_app) {
		this.r95_crm_sub_elig_sub_app = r95_crm_sub_elig_sub_app;
	}
	public BigDecimal getR95_crm_sub_non_col_guar() {
		return r95_crm_sub_non_col_guar;
	}
	public void setR95_crm_sub_non_col_guar(BigDecimal r95_crm_sub_non_col_guar) {
		this.r95_crm_sub_non_col_guar = r95_crm_sub_non_col_guar;
	}
	public BigDecimal getR95_crm_sub_non_col_cre_der() {
		return r95_crm_sub_non_col_cre_der;
	}
	public void setR95_crm_sub_non_col_cre_der(BigDecimal r95_crm_sub_non_col_cre_der) {
		this.r95_crm_sub_non_col_cre_der = r95_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR95_crm_sub_col_elig_cash() {
		return r95_crm_sub_col_elig_cash;
	}
	public void setR95_crm_sub_col_elig_cash(BigDecimal r95_crm_sub_col_elig_cash) {
		this.r95_crm_sub_col_elig_cash = r95_crm_sub_col_elig_cash;
	}
	public BigDecimal getR95_crm_sub_col_elig_trea_bills() {
		return r95_crm_sub_col_elig_trea_bills;
	}
	public void setR95_crm_sub_col_elig_trea_bills(BigDecimal r95_crm_sub_col_elig_trea_bills) {
		this.r95_crm_sub_col_elig_trea_bills = r95_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR95_crm_sub_col_elig_deb_sec() {
		return r95_crm_sub_col_elig_deb_sec;
	}
	public void setR95_crm_sub_col_elig_deb_sec(BigDecimal r95_crm_sub_col_elig_deb_sec) {
		this.r95_crm_sub_col_elig_deb_sec = r95_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR95_crm_sub_col_elig_equi() {
		return r95_crm_sub_col_elig_equi;
	}
	public void setR95_crm_sub_col_elig_equi(BigDecimal r95_crm_sub_col_elig_equi) {
		this.r95_crm_sub_col_elig_equi = r95_crm_sub_col_elig_equi;
	}
	public BigDecimal getR95_crm_sub_col_elig_unit_tru() {
		return r95_crm_sub_col_elig_unit_tru;
	}
	public void setR95_crm_sub_col_elig_unit_tru(BigDecimal r95_crm_sub_col_elig_unit_tru) {
		this.r95_crm_sub_col_elig_unit_tru = r95_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR95_crm_sub_col_exp_cov() {
		return r95_crm_sub_col_exp_cov;
	}
	public void setR95_crm_sub_col_exp_cov(BigDecimal r95_crm_sub_col_exp_cov) {
		this.r95_crm_sub_col_exp_cov = r95_crm_sub_col_exp_cov;
	}
	public BigDecimal getR95_crm_sub_col_elig_exp_not_cov() {
		return r95_crm_sub_col_elig_exp_not_cov;
	}
	public void setR95_crm_sub_col_elig_exp_not_cov(BigDecimal r95_crm_sub_col_elig_exp_not_cov) {
		this.r95_crm_sub_col_elig_exp_not_cov = r95_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR95_crm_sub_rwa_ris_crm() {
		return r95_crm_sub_rwa_ris_crm;
	}
	public void setR95_crm_sub_rwa_ris_crm(BigDecimal r95_crm_sub_rwa_ris_crm) {
		this.r95_crm_sub_rwa_ris_crm = r95_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR95_crm_sub_rwa_cov_crm() {
		return r95_crm_sub_rwa_cov_crm;
	}
	public void setR95_crm_sub_rwa_cov_crm(BigDecimal r95_crm_sub_rwa_cov_crm) {
		this.r95_crm_sub_rwa_cov_crm = r95_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR95_crm_sub_rwa_org_cou() {
		return r95_crm_sub_rwa_org_cou;
	}
	public void setR95_crm_sub_rwa_org_cou(BigDecimal r95_crm_sub_rwa_org_cou) {
		this.r95_crm_sub_rwa_org_cou = r95_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR95_crm_sub_rwa_not_cov_crm() {
		return r95_crm_sub_rwa_not_cov_crm;
	}
	public void setR95_crm_sub_rwa_not_cov_crm(BigDecimal r95_crm_sub_rwa_not_cov_crm) {
		this.r95_crm_sub_rwa_not_cov_crm = r95_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR95_crm_comp_col_expo_elig() {
		return r95_crm_comp_col_expo_elig;
	}
	public void setR95_crm_comp_col_expo_elig(BigDecimal r95_crm_comp_col_expo_elig) {
		this.r95_crm_comp_col_expo_elig = r95_crm_comp_col_expo_elig;
	}
	public BigDecimal getR95_crm_comp_col_elig_expo_vol_adj() {
		return r95_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR95_crm_comp_col_elig_expo_vol_adj(BigDecimal r95_crm_comp_col_elig_expo_vol_adj) {
		this.r95_crm_comp_col_elig_expo_vol_adj = r95_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR95_crm_comp_col_elig_fin_hai() {
		return r95_crm_comp_col_elig_fin_hai;
	}
	public void setR95_crm_comp_col_elig_fin_hai(BigDecimal r95_crm_comp_col_elig_fin_hai) {
		this.r95_crm_comp_col_elig_fin_hai = r95_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR95_crm_comp_col_expo_val() {
		return r95_crm_comp_col_expo_val;
	}
	public void setR95_crm_comp_col_expo_val(BigDecimal r95_crm_comp_col_expo_val) {
		this.r95_crm_comp_col_expo_val = r95_crm_comp_col_expo_val;
	}
	public BigDecimal getR95_rwa_elig_expo_not_cov_crm() {
		return r95_rwa_elig_expo_not_cov_crm;
	}
	public void setR95_rwa_elig_expo_not_cov_crm(BigDecimal r95_rwa_elig_expo_not_cov_crm) {
		this.r95_rwa_elig_expo_not_cov_crm = r95_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR95_rwa_unsec_expo_cre_ris() {
		return r95_rwa_unsec_expo_cre_ris;
	}
	public void setR95_rwa_unsec_expo_cre_ris(BigDecimal r95_rwa_unsec_expo_cre_ris) {
		this.r95_rwa_unsec_expo_cre_ris = r95_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR95_rwa_unsec_expo() {
		return r95_rwa_unsec_expo;
	}
	public void setR95_rwa_unsec_expo(BigDecimal r95_rwa_unsec_expo) {
		this.r95_rwa_unsec_expo = r95_rwa_unsec_expo;
	}
	public BigDecimal getR95_rwa_tot_ris_wei_ass() {
		return r95_rwa_tot_ris_wei_ass;
	}
	public void setR95_rwa_tot_ris_wei_ass(BigDecimal r95_rwa_tot_ris_wei_ass) {
		this.r95_rwa_tot_ris_wei_ass = r95_rwa_tot_ris_wei_ass;
	}
	public String getR103_exposure_class() {
		return r103_exposure_class;
	}
	public void setR103_exposure_class(String r103_exposure_class) {
		this.r103_exposure_class = r103_exposure_class;
	}
	public BigDecimal getR103_expo_crm() {
		return r103_expo_crm;
	}
	public void setR103_expo_crm(BigDecimal r103_expo_crm) {
		this.r103_expo_crm = r103_expo_crm;
	}
	public BigDecimal getR103_spe_pro_expo() {
		return r103_spe_pro_expo;
	}
	public void setR103_spe_pro_expo(BigDecimal r103_spe_pro_expo) {
		this.r103_spe_pro_expo = r103_spe_pro_expo;
	}
	public BigDecimal getR103_amt_elig_sht_net() {
		return r103_amt_elig_sht_net;
	}
	public void setR103_amt_elig_sht_net(BigDecimal r103_amt_elig_sht_net) {
		this.r103_amt_elig_sht_net = r103_amt_elig_sht_net;
	}
	public BigDecimal getR103_tot_expo_net_spe() {
		return r103_tot_expo_net_spe;
	}
	public void setR103_tot_expo_net_spe(BigDecimal r103_tot_expo_net_spe) {
		this.r103_tot_expo_net_spe = r103_tot_expo_net_spe;
	}
	public BigDecimal getR103_crm_sub_elig_sub_app() {
		return r103_crm_sub_elig_sub_app;
	}
	public void setR103_crm_sub_elig_sub_app(BigDecimal r103_crm_sub_elig_sub_app) {
		this.r103_crm_sub_elig_sub_app = r103_crm_sub_elig_sub_app;
	}
	public BigDecimal getR103_crm_sub_non_col_guar() {
		return r103_crm_sub_non_col_guar;
	}
	public void setR103_crm_sub_non_col_guar(BigDecimal r103_crm_sub_non_col_guar) {
		this.r103_crm_sub_non_col_guar = r103_crm_sub_non_col_guar;
	}
	public BigDecimal getR103_crm_sub_non_col_cre_der() {
		return r103_crm_sub_non_col_cre_der;
	}
	public void setR103_crm_sub_non_col_cre_der(BigDecimal r103_crm_sub_non_col_cre_der) {
		this.r103_crm_sub_non_col_cre_der = r103_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR103_crm_sub_col_elig_cash() {
		return r103_crm_sub_col_elig_cash;
	}
	public void setR103_crm_sub_col_elig_cash(BigDecimal r103_crm_sub_col_elig_cash) {
		this.r103_crm_sub_col_elig_cash = r103_crm_sub_col_elig_cash;
	}
	public BigDecimal getR103_crm_sub_col_elig_trea_bills() {
		return r103_crm_sub_col_elig_trea_bills;
	}
	public void setR103_crm_sub_col_elig_trea_bills(BigDecimal r103_crm_sub_col_elig_trea_bills) {
		this.r103_crm_sub_col_elig_trea_bills = r103_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR103_crm_sub_col_elig_deb_sec() {
		return r103_crm_sub_col_elig_deb_sec;
	}
	public void setR103_crm_sub_col_elig_deb_sec(BigDecimal r103_crm_sub_col_elig_deb_sec) {
		this.r103_crm_sub_col_elig_deb_sec = r103_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR103_crm_sub_col_elig_equi() {
		return r103_crm_sub_col_elig_equi;
	}
	public void setR103_crm_sub_col_elig_equi(BigDecimal r103_crm_sub_col_elig_equi) {
		this.r103_crm_sub_col_elig_equi = r103_crm_sub_col_elig_equi;
	}
	public BigDecimal getR103_crm_sub_col_elig_unit_tru() {
		return r103_crm_sub_col_elig_unit_tru;
	}
	public void setR103_crm_sub_col_elig_unit_tru(BigDecimal r103_crm_sub_col_elig_unit_tru) {
		this.r103_crm_sub_col_elig_unit_tru = r103_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR103_crm_sub_col_exp_cov() {
		return r103_crm_sub_col_exp_cov;
	}
	public void setR103_crm_sub_col_exp_cov(BigDecimal r103_crm_sub_col_exp_cov) {
		this.r103_crm_sub_col_exp_cov = r103_crm_sub_col_exp_cov;
	}
	public BigDecimal getR103_crm_sub_col_elig_exp_not_cov() {
		return r103_crm_sub_col_elig_exp_not_cov;
	}
	public void setR103_crm_sub_col_elig_exp_not_cov(BigDecimal r103_crm_sub_col_elig_exp_not_cov) {
		this.r103_crm_sub_col_elig_exp_not_cov = r103_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR103_crm_sub_rwa_ris_crm() {
		return r103_crm_sub_rwa_ris_crm;
	}
	public void setR103_crm_sub_rwa_ris_crm(BigDecimal r103_crm_sub_rwa_ris_crm) {
		this.r103_crm_sub_rwa_ris_crm = r103_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR103_crm_sub_rwa_cov_crm() {
		return r103_crm_sub_rwa_cov_crm;
	}
	public void setR103_crm_sub_rwa_cov_crm(BigDecimal r103_crm_sub_rwa_cov_crm) {
		this.r103_crm_sub_rwa_cov_crm = r103_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR103_crm_sub_rwa_org_cou() {
		return r103_crm_sub_rwa_org_cou;
	}
	public void setR103_crm_sub_rwa_org_cou(BigDecimal r103_crm_sub_rwa_org_cou) {
		this.r103_crm_sub_rwa_org_cou = r103_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR103_crm_sub_rwa_not_cov_crm() {
		return r103_crm_sub_rwa_not_cov_crm;
	}
	public void setR103_crm_sub_rwa_not_cov_crm(BigDecimal r103_crm_sub_rwa_not_cov_crm) {
		this.r103_crm_sub_rwa_not_cov_crm = r103_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR103_crm_comp_col_expo_elig() {
		return r103_crm_comp_col_expo_elig;
	}
	public void setR103_crm_comp_col_expo_elig(BigDecimal r103_crm_comp_col_expo_elig) {
		this.r103_crm_comp_col_expo_elig = r103_crm_comp_col_expo_elig;
	}
	public BigDecimal getR103_crm_comp_col_elig_expo_vol_adj() {
		return r103_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR103_crm_comp_col_elig_expo_vol_adj(BigDecimal r103_crm_comp_col_elig_expo_vol_adj) {
		this.r103_crm_comp_col_elig_expo_vol_adj = r103_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR103_crm_comp_col_elig_fin_hai() {
		return r103_crm_comp_col_elig_fin_hai;
	}
	public void setR103_crm_comp_col_elig_fin_hai(BigDecimal r103_crm_comp_col_elig_fin_hai) {
		this.r103_crm_comp_col_elig_fin_hai = r103_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR103_crm_comp_col_expo_val() {
		return r103_crm_comp_col_expo_val;
	}
	public void setR103_crm_comp_col_expo_val(BigDecimal r103_crm_comp_col_expo_val) {
		this.r103_crm_comp_col_expo_val = r103_crm_comp_col_expo_val;
	}
	public BigDecimal getR103_rwa_elig_expo_not_cov_crm() {
		return r103_rwa_elig_expo_not_cov_crm;
	}
	public void setR103_rwa_elig_expo_not_cov_crm(BigDecimal r103_rwa_elig_expo_not_cov_crm) {
		this.r103_rwa_elig_expo_not_cov_crm = r103_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR103_rwa_unsec_expo_cre_ris() {
		return r103_rwa_unsec_expo_cre_ris;
	}
	public void setR103_rwa_unsec_expo_cre_ris(BigDecimal r103_rwa_unsec_expo_cre_ris) {
		this.r103_rwa_unsec_expo_cre_ris = r103_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR103_rwa_unsec_expo() {
		return r103_rwa_unsec_expo;
	}
	public void setR103_rwa_unsec_expo(BigDecimal r103_rwa_unsec_expo) {
		this.r103_rwa_unsec_expo = r103_rwa_unsec_expo;
	}
	public BigDecimal getR103_rwa_tot_ris_wei_ass() {
		return r103_rwa_tot_ris_wei_ass;
	}
	public void setR103_rwa_tot_ris_wei_ass(BigDecimal r103_rwa_tot_ris_wei_ass) {
		this.r103_rwa_tot_ris_wei_ass = r103_rwa_tot_ris_wei_ass;
	}
	public String getR104_exposure_class() {
		return r104_exposure_class;
	}
	public void setR104_exposure_class(String r104_exposure_class) {
		this.r104_exposure_class = r104_exposure_class;
	}
	public BigDecimal getR104_expo_crm() {
		return r104_expo_crm;
	}
	public void setR104_expo_crm(BigDecimal r104_expo_crm) {
		this.r104_expo_crm = r104_expo_crm;
	}
	public BigDecimal getR104_spe_pro_expo() {
		return r104_spe_pro_expo;
	}
	public void setR104_spe_pro_expo(BigDecimal r104_spe_pro_expo) {
		this.r104_spe_pro_expo = r104_spe_pro_expo;
	}
	public BigDecimal getR104_amt_elig_sht_net() {
		return r104_amt_elig_sht_net;
	}
	public void setR104_amt_elig_sht_net(BigDecimal r104_amt_elig_sht_net) {
		this.r104_amt_elig_sht_net = r104_amt_elig_sht_net;
	}
	public BigDecimal getR104_tot_expo_net_spe() {
		return r104_tot_expo_net_spe;
	}
	public void setR104_tot_expo_net_spe(BigDecimal r104_tot_expo_net_spe) {
		this.r104_tot_expo_net_spe = r104_tot_expo_net_spe;
	}
	public BigDecimal getR104_crm_sub_elig_sub_app() {
		return r104_crm_sub_elig_sub_app;
	}
	public void setR104_crm_sub_elig_sub_app(BigDecimal r104_crm_sub_elig_sub_app) {
		this.r104_crm_sub_elig_sub_app = r104_crm_sub_elig_sub_app;
	}
	public BigDecimal getR104_crm_sub_non_col_guar() {
		return r104_crm_sub_non_col_guar;
	}
	public void setR104_crm_sub_non_col_guar(BigDecimal r104_crm_sub_non_col_guar) {
		this.r104_crm_sub_non_col_guar = r104_crm_sub_non_col_guar;
	}
	public BigDecimal getR104_crm_sub_non_col_cre_der() {
		return r104_crm_sub_non_col_cre_der;
	}
	public void setR104_crm_sub_non_col_cre_der(BigDecimal r104_crm_sub_non_col_cre_der) {
		this.r104_crm_sub_non_col_cre_der = r104_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR104_crm_sub_col_elig_cash() {
		return r104_crm_sub_col_elig_cash;
	}
	public void setR104_crm_sub_col_elig_cash(BigDecimal r104_crm_sub_col_elig_cash) {
		this.r104_crm_sub_col_elig_cash = r104_crm_sub_col_elig_cash;
	}
	public BigDecimal getR104_crm_sub_col_elig_trea_bills() {
		return r104_crm_sub_col_elig_trea_bills;
	}
	public void setR104_crm_sub_col_elig_trea_bills(BigDecimal r104_crm_sub_col_elig_trea_bills) {
		this.r104_crm_sub_col_elig_trea_bills = r104_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR104_crm_sub_col_elig_deb_sec() {
		return r104_crm_sub_col_elig_deb_sec;
	}
	public void setR104_crm_sub_col_elig_deb_sec(BigDecimal r104_crm_sub_col_elig_deb_sec) {
		this.r104_crm_sub_col_elig_deb_sec = r104_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR104_crm_sub_col_elig_equi() {
		return r104_crm_sub_col_elig_equi;
	}
	public void setR104_crm_sub_col_elig_equi(BigDecimal r104_crm_sub_col_elig_equi) {
		this.r104_crm_sub_col_elig_equi = r104_crm_sub_col_elig_equi;
	}
	public BigDecimal getR104_crm_sub_col_elig_unit_tru() {
		return r104_crm_sub_col_elig_unit_tru;
	}
	public void setR104_crm_sub_col_elig_unit_tru(BigDecimal r104_crm_sub_col_elig_unit_tru) {
		this.r104_crm_sub_col_elig_unit_tru = r104_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR104_crm_sub_col_exp_cov() {
		return r104_crm_sub_col_exp_cov;
	}
	public void setR104_crm_sub_col_exp_cov(BigDecimal r104_crm_sub_col_exp_cov) {
		this.r104_crm_sub_col_exp_cov = r104_crm_sub_col_exp_cov;
	}
	public BigDecimal getR104_crm_sub_col_elig_exp_not_cov() {
		return r104_crm_sub_col_elig_exp_not_cov;
	}
	public void setR104_crm_sub_col_elig_exp_not_cov(BigDecimal r104_crm_sub_col_elig_exp_not_cov) {
		this.r104_crm_sub_col_elig_exp_not_cov = r104_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR104_crm_sub_rwa_ris_crm() {
		return r104_crm_sub_rwa_ris_crm;
	}
	public void setR104_crm_sub_rwa_ris_crm(BigDecimal r104_crm_sub_rwa_ris_crm) {
		this.r104_crm_sub_rwa_ris_crm = r104_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR104_crm_sub_rwa_cov_crm() {
		return r104_crm_sub_rwa_cov_crm;
	}
	public void setR104_crm_sub_rwa_cov_crm(BigDecimal r104_crm_sub_rwa_cov_crm) {
		this.r104_crm_sub_rwa_cov_crm = r104_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR104_crm_sub_rwa_org_cou() {
		return r104_crm_sub_rwa_org_cou;
	}
	public void setR104_crm_sub_rwa_org_cou(BigDecimal r104_crm_sub_rwa_org_cou) {
		this.r104_crm_sub_rwa_org_cou = r104_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR104_crm_sub_rwa_not_cov_crm() {
		return r104_crm_sub_rwa_not_cov_crm;
	}
	public void setR104_crm_sub_rwa_not_cov_crm(BigDecimal r104_crm_sub_rwa_not_cov_crm) {
		this.r104_crm_sub_rwa_not_cov_crm = r104_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR104_crm_comp_col_expo_elig() {
		return r104_crm_comp_col_expo_elig;
	}
	public void setR104_crm_comp_col_expo_elig(BigDecimal r104_crm_comp_col_expo_elig) {
		this.r104_crm_comp_col_expo_elig = r104_crm_comp_col_expo_elig;
	}
	public BigDecimal getR104_crm_comp_col_elig_expo_vol_adj() {
		return r104_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR104_crm_comp_col_elig_expo_vol_adj(BigDecimal r104_crm_comp_col_elig_expo_vol_adj) {
		this.r104_crm_comp_col_elig_expo_vol_adj = r104_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR104_crm_comp_col_elig_fin_hai() {
		return r104_crm_comp_col_elig_fin_hai;
	}
	public void setR104_crm_comp_col_elig_fin_hai(BigDecimal r104_crm_comp_col_elig_fin_hai) {
		this.r104_crm_comp_col_elig_fin_hai = r104_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR104_crm_comp_col_expo_val() {
		return r104_crm_comp_col_expo_val;
	}
	public void setR104_crm_comp_col_expo_val(BigDecimal r104_crm_comp_col_expo_val) {
		this.r104_crm_comp_col_expo_val = r104_crm_comp_col_expo_val;
	}
	public BigDecimal getR104_rwa_elig_expo_not_cov_crm() {
		return r104_rwa_elig_expo_not_cov_crm;
	}
	public void setR104_rwa_elig_expo_not_cov_crm(BigDecimal r104_rwa_elig_expo_not_cov_crm) {
		this.r104_rwa_elig_expo_not_cov_crm = r104_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR104_rwa_unsec_expo_cre_ris() {
		return r104_rwa_unsec_expo_cre_ris;
	}
	public void setR104_rwa_unsec_expo_cre_ris(BigDecimal r104_rwa_unsec_expo_cre_ris) {
		this.r104_rwa_unsec_expo_cre_ris = r104_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR104_rwa_unsec_expo() {
		return r104_rwa_unsec_expo;
	}
	public void setR104_rwa_unsec_expo(BigDecimal r104_rwa_unsec_expo) {
		this.r104_rwa_unsec_expo = r104_rwa_unsec_expo;
	}
	public BigDecimal getR104_rwa_tot_ris_wei_ass() {
		return r104_rwa_tot_ris_wei_ass;
	}
	public void setR104_rwa_tot_ris_wei_ass(BigDecimal r104_rwa_tot_ris_wei_ass) {
		this.r104_rwa_tot_ris_wei_ass = r104_rwa_tot_ris_wei_ass;
	}
	public String getR115_exposure_class() {
		return r115_exposure_class;
	}
	public void setR115_exposure_class(String r115_exposure_class) {
		this.r115_exposure_class = r115_exposure_class;
	}
	public BigDecimal getR115_expo_crm() {
		return r115_expo_crm;
	}
	public void setR115_expo_crm(BigDecimal r115_expo_crm) {
		this.r115_expo_crm = r115_expo_crm;
	}
	public BigDecimal getR115_spe_pro_expo() {
		return r115_spe_pro_expo;
	}
	public void setR115_spe_pro_expo(BigDecimal r115_spe_pro_expo) {
		this.r115_spe_pro_expo = r115_spe_pro_expo;
	}
	public BigDecimal getR115_amt_elig_sht_net() {
		return r115_amt_elig_sht_net;
	}
	public void setR115_amt_elig_sht_net(BigDecimal r115_amt_elig_sht_net) {
		this.r115_amt_elig_sht_net = r115_amt_elig_sht_net;
	}
	public BigDecimal getR115_tot_expo_net_spe() {
		return r115_tot_expo_net_spe;
	}
	public void setR115_tot_expo_net_spe(BigDecimal r115_tot_expo_net_spe) {
		this.r115_tot_expo_net_spe = r115_tot_expo_net_spe;
	}
	public BigDecimal getR115_crm_sub_elig_sub_app() {
		return r115_crm_sub_elig_sub_app;
	}
	public void setR115_crm_sub_elig_sub_app(BigDecimal r115_crm_sub_elig_sub_app) {
		this.r115_crm_sub_elig_sub_app = r115_crm_sub_elig_sub_app;
	}
	public BigDecimal getR115_crm_sub_non_col_guar() {
		return r115_crm_sub_non_col_guar;
	}
	public void setR115_crm_sub_non_col_guar(BigDecimal r115_crm_sub_non_col_guar) {
		this.r115_crm_sub_non_col_guar = r115_crm_sub_non_col_guar;
	}
	public BigDecimal getR115_crm_sub_non_col_cre_der() {
		return r115_crm_sub_non_col_cre_der;
	}
	public void setR115_crm_sub_non_col_cre_der(BigDecimal r115_crm_sub_non_col_cre_der) {
		this.r115_crm_sub_non_col_cre_der = r115_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR115_crm_sub_col_elig_cash() {
		return r115_crm_sub_col_elig_cash;
	}
	public void setR115_crm_sub_col_elig_cash(BigDecimal r115_crm_sub_col_elig_cash) {
		this.r115_crm_sub_col_elig_cash = r115_crm_sub_col_elig_cash;
	}
	public BigDecimal getR115_crm_sub_col_elig_trea_bills() {
		return r115_crm_sub_col_elig_trea_bills;
	}
	public void setR115_crm_sub_col_elig_trea_bills(BigDecimal r115_crm_sub_col_elig_trea_bills) {
		this.r115_crm_sub_col_elig_trea_bills = r115_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR115_crm_sub_col_elig_deb_sec() {
		return r115_crm_sub_col_elig_deb_sec;
	}
	public void setR115_crm_sub_col_elig_deb_sec(BigDecimal r115_crm_sub_col_elig_deb_sec) {
		this.r115_crm_sub_col_elig_deb_sec = r115_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR115_crm_sub_col_elig_equi() {
		return r115_crm_sub_col_elig_equi;
	}
	public void setR115_crm_sub_col_elig_equi(BigDecimal r115_crm_sub_col_elig_equi) {
		this.r115_crm_sub_col_elig_equi = r115_crm_sub_col_elig_equi;
	}
	public BigDecimal getR115_crm_sub_col_elig_unit_tru() {
		return r115_crm_sub_col_elig_unit_tru;
	}
	public void setR115_crm_sub_col_elig_unit_tru(BigDecimal r115_crm_sub_col_elig_unit_tru) {
		this.r115_crm_sub_col_elig_unit_tru = r115_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR115_crm_sub_col_exp_cov() {
		return r115_crm_sub_col_exp_cov;
	}
	public void setR115_crm_sub_col_exp_cov(BigDecimal r115_crm_sub_col_exp_cov) {
		this.r115_crm_sub_col_exp_cov = r115_crm_sub_col_exp_cov;
	}
	public BigDecimal getR115_crm_sub_col_elig_exp_not_cov() {
		return r115_crm_sub_col_elig_exp_not_cov;
	}
	public void setR115_crm_sub_col_elig_exp_not_cov(BigDecimal r115_crm_sub_col_elig_exp_not_cov) {
		this.r115_crm_sub_col_elig_exp_not_cov = r115_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR115_crm_sub_rwa_ris_crm() {
		return r115_crm_sub_rwa_ris_crm;
	}
	public void setR115_crm_sub_rwa_ris_crm(BigDecimal r115_crm_sub_rwa_ris_crm) {
		this.r115_crm_sub_rwa_ris_crm = r115_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR115_crm_sub_rwa_cov_crm() {
		return r115_crm_sub_rwa_cov_crm;
	}
	public void setR115_crm_sub_rwa_cov_crm(BigDecimal r115_crm_sub_rwa_cov_crm) {
		this.r115_crm_sub_rwa_cov_crm = r115_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR115_crm_sub_rwa_org_cou() {
		return r115_crm_sub_rwa_org_cou;
	}
	public void setR115_crm_sub_rwa_org_cou(BigDecimal r115_crm_sub_rwa_org_cou) {
		this.r115_crm_sub_rwa_org_cou = r115_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR115_crm_sub_rwa_not_cov_crm() {
		return r115_crm_sub_rwa_not_cov_crm;
	}
	public void setR115_crm_sub_rwa_not_cov_crm(BigDecimal r115_crm_sub_rwa_not_cov_crm) {
		this.r115_crm_sub_rwa_not_cov_crm = r115_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR115_crm_comp_col_expo_elig() {
		return r115_crm_comp_col_expo_elig;
	}
	public void setR115_crm_comp_col_expo_elig(BigDecimal r115_crm_comp_col_expo_elig) {
		this.r115_crm_comp_col_expo_elig = r115_crm_comp_col_expo_elig;
	}
	public BigDecimal getR115_crm_comp_col_elig_expo_vol_adj() {
		return r115_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR115_crm_comp_col_elig_expo_vol_adj(BigDecimal r115_crm_comp_col_elig_expo_vol_adj) {
		this.r115_crm_comp_col_elig_expo_vol_adj = r115_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR115_crm_comp_col_elig_fin_hai() {
		return r115_crm_comp_col_elig_fin_hai;
	}
	public void setR115_crm_comp_col_elig_fin_hai(BigDecimal r115_crm_comp_col_elig_fin_hai) {
		this.r115_crm_comp_col_elig_fin_hai = r115_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR115_crm_comp_col_expo_val() {
		return r115_crm_comp_col_expo_val;
	}
	public void setR115_crm_comp_col_expo_val(BigDecimal r115_crm_comp_col_expo_val) {
		this.r115_crm_comp_col_expo_val = r115_crm_comp_col_expo_val;
	}
	public BigDecimal getR115_rwa_elig_expo_not_cov_crm() {
		return r115_rwa_elig_expo_not_cov_crm;
	}
	public void setR115_rwa_elig_expo_not_cov_crm(BigDecimal r115_rwa_elig_expo_not_cov_crm) {
		this.r115_rwa_elig_expo_not_cov_crm = r115_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR115_rwa_unsec_expo_cre_ris() {
		return r115_rwa_unsec_expo_cre_ris;
	}
	public void setR115_rwa_unsec_expo_cre_ris(BigDecimal r115_rwa_unsec_expo_cre_ris) {
		this.r115_rwa_unsec_expo_cre_ris = r115_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR115_rwa_unsec_expo() {
		return r115_rwa_unsec_expo;
	}
	public void setR115_rwa_unsec_expo(BigDecimal r115_rwa_unsec_expo) {
		this.r115_rwa_unsec_expo = r115_rwa_unsec_expo;
	}
	public BigDecimal getR115_rwa_tot_ris_wei_ass() {
		return r115_rwa_tot_ris_wei_ass;
	}
	public void setR115_rwa_tot_ris_wei_ass(BigDecimal r115_rwa_tot_ris_wei_ass) {
		this.r115_rwa_tot_ris_wei_ass = r115_rwa_tot_ris_wei_ass;
	}
	public String getR116_exposure_class() {
		return r116_exposure_class;
	}
	public void setR116_exposure_class(String r116_exposure_class) {
		this.r116_exposure_class = r116_exposure_class;
	}
	public BigDecimal getR116_expo_crm() {
		return r116_expo_crm;
	}
	public void setR116_expo_crm(BigDecimal r116_expo_crm) {
		this.r116_expo_crm = r116_expo_crm;
	}
	public BigDecimal getR116_spe_pro_expo() {
		return r116_spe_pro_expo;
	}
	public void setR116_spe_pro_expo(BigDecimal r116_spe_pro_expo) {
		this.r116_spe_pro_expo = r116_spe_pro_expo;
	}
	public BigDecimal getR116_amt_elig_sht_net() {
		return r116_amt_elig_sht_net;
	}
	public void setR116_amt_elig_sht_net(BigDecimal r116_amt_elig_sht_net) {
		this.r116_amt_elig_sht_net = r116_amt_elig_sht_net;
	}
	public BigDecimal getR116_tot_expo_net_spe() {
		return r116_tot_expo_net_spe;
	}
	public void setR116_tot_expo_net_spe(BigDecimal r116_tot_expo_net_spe) {
		this.r116_tot_expo_net_spe = r116_tot_expo_net_spe;
	}
	public BigDecimal getR116_crm_sub_elig_sub_app() {
		return r116_crm_sub_elig_sub_app;
	}
	public void setR116_crm_sub_elig_sub_app(BigDecimal r116_crm_sub_elig_sub_app) {
		this.r116_crm_sub_elig_sub_app = r116_crm_sub_elig_sub_app;
	}
	public BigDecimal getR116_crm_sub_non_col_guar() {
		return r116_crm_sub_non_col_guar;
	}
	public void setR116_crm_sub_non_col_guar(BigDecimal r116_crm_sub_non_col_guar) {
		this.r116_crm_sub_non_col_guar = r116_crm_sub_non_col_guar;
	}
	public BigDecimal getR116_crm_sub_non_col_cre_der() {
		return r116_crm_sub_non_col_cre_der;
	}
	public void setR116_crm_sub_non_col_cre_der(BigDecimal r116_crm_sub_non_col_cre_der) {
		this.r116_crm_sub_non_col_cre_der = r116_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR116_crm_sub_col_elig_cash() {
		return r116_crm_sub_col_elig_cash;
	}
	public void setR116_crm_sub_col_elig_cash(BigDecimal r116_crm_sub_col_elig_cash) {
		this.r116_crm_sub_col_elig_cash = r116_crm_sub_col_elig_cash;
	}
	public BigDecimal getR116_crm_sub_col_elig_trea_bills() {
		return r116_crm_sub_col_elig_trea_bills;
	}
	public void setR116_crm_sub_col_elig_trea_bills(BigDecimal r116_crm_sub_col_elig_trea_bills) {
		this.r116_crm_sub_col_elig_trea_bills = r116_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR116_crm_sub_col_elig_deb_sec() {
		return r116_crm_sub_col_elig_deb_sec;
	}
	public void setR116_crm_sub_col_elig_deb_sec(BigDecimal r116_crm_sub_col_elig_deb_sec) {
		this.r116_crm_sub_col_elig_deb_sec = r116_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR116_crm_sub_col_elig_equi() {
		return r116_crm_sub_col_elig_equi;
	}
	public void setR116_crm_sub_col_elig_equi(BigDecimal r116_crm_sub_col_elig_equi) {
		this.r116_crm_sub_col_elig_equi = r116_crm_sub_col_elig_equi;
	}
	public BigDecimal getR116_crm_sub_col_elig_unit_tru() {
		return r116_crm_sub_col_elig_unit_tru;
	}
	public void setR116_crm_sub_col_elig_unit_tru(BigDecimal r116_crm_sub_col_elig_unit_tru) {
		this.r116_crm_sub_col_elig_unit_tru = r116_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR116_crm_sub_col_exp_cov() {
		return r116_crm_sub_col_exp_cov;
	}
	public void setR116_crm_sub_col_exp_cov(BigDecimal r116_crm_sub_col_exp_cov) {
		this.r116_crm_sub_col_exp_cov = r116_crm_sub_col_exp_cov;
	}
	public BigDecimal getR116_crm_sub_col_elig_exp_not_cov() {
		return r116_crm_sub_col_elig_exp_not_cov;
	}
	public void setR116_crm_sub_col_elig_exp_not_cov(BigDecimal r116_crm_sub_col_elig_exp_not_cov) {
		this.r116_crm_sub_col_elig_exp_not_cov = r116_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR116_crm_sub_rwa_ris_crm() {
		return r116_crm_sub_rwa_ris_crm;
	}
	public void setR116_crm_sub_rwa_ris_crm(BigDecimal r116_crm_sub_rwa_ris_crm) {
		this.r116_crm_sub_rwa_ris_crm = r116_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR116_crm_sub_rwa_cov_crm() {
		return r116_crm_sub_rwa_cov_crm;
	}
	public void setR116_crm_sub_rwa_cov_crm(BigDecimal r116_crm_sub_rwa_cov_crm) {
		this.r116_crm_sub_rwa_cov_crm = r116_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR116_crm_sub_rwa_org_cou() {
		return r116_crm_sub_rwa_org_cou;
	}
	public void setR116_crm_sub_rwa_org_cou(BigDecimal r116_crm_sub_rwa_org_cou) {
		this.r116_crm_sub_rwa_org_cou = r116_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR116_crm_sub_rwa_not_cov_crm() {
		return r116_crm_sub_rwa_not_cov_crm;
	}
	public void setR116_crm_sub_rwa_not_cov_crm(BigDecimal r116_crm_sub_rwa_not_cov_crm) {
		this.r116_crm_sub_rwa_not_cov_crm = r116_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR116_crm_comp_col_expo_elig() {
		return r116_crm_comp_col_expo_elig;
	}
	public void setR116_crm_comp_col_expo_elig(BigDecimal r116_crm_comp_col_expo_elig) {
		this.r116_crm_comp_col_expo_elig = r116_crm_comp_col_expo_elig;
	}
	public BigDecimal getR116_crm_comp_col_elig_expo_vol_adj() {
		return r116_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR116_crm_comp_col_elig_expo_vol_adj(BigDecimal r116_crm_comp_col_elig_expo_vol_adj) {
		this.r116_crm_comp_col_elig_expo_vol_adj = r116_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR116_crm_comp_col_elig_fin_hai() {
		return r116_crm_comp_col_elig_fin_hai;
	}
	public void setR116_crm_comp_col_elig_fin_hai(BigDecimal r116_crm_comp_col_elig_fin_hai) {
		this.r116_crm_comp_col_elig_fin_hai = r116_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR116_crm_comp_col_expo_val() {
		return r116_crm_comp_col_expo_val;
	}
	public void setR116_crm_comp_col_expo_val(BigDecimal r116_crm_comp_col_expo_val) {
		this.r116_crm_comp_col_expo_val = r116_crm_comp_col_expo_val;
	}
	public BigDecimal getR116_rwa_elig_expo_not_cov_crm() {
		return r116_rwa_elig_expo_not_cov_crm;
	}
	public void setR116_rwa_elig_expo_not_cov_crm(BigDecimal r116_rwa_elig_expo_not_cov_crm) {
		this.r116_rwa_elig_expo_not_cov_crm = r116_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR116_rwa_unsec_expo_cre_ris() {
		return r116_rwa_unsec_expo_cre_ris;
	}
	public void setR116_rwa_unsec_expo_cre_ris(BigDecimal r116_rwa_unsec_expo_cre_ris) {
		this.r116_rwa_unsec_expo_cre_ris = r116_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR116_rwa_unsec_expo() {
		return r116_rwa_unsec_expo;
	}
	public void setR116_rwa_unsec_expo(BigDecimal r116_rwa_unsec_expo) {
		this.r116_rwa_unsec_expo = r116_rwa_unsec_expo;
	}
	public BigDecimal getR116_rwa_tot_ris_wei_ass() {
		return r116_rwa_tot_ris_wei_ass;
	}
	public void setR116_rwa_tot_ris_wei_ass(BigDecimal r116_rwa_tot_ris_wei_ass) {
		this.r116_rwa_tot_ris_wei_ass = r116_rwa_tot_ris_wei_ass;
	}
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
	public String getR135_exposure_class() {
		return r135_exposure_class;
	}
	public void setR135_exposure_class(String r135_exposure_class) {
		this.r135_exposure_class = r135_exposure_class;
	}
	public BigDecimal getR135_expo_crm() {
		return r135_expo_crm;
	}
	public void setR135_expo_crm(BigDecimal r135_expo_crm) {
		this.r135_expo_crm = r135_expo_crm;
	}
	public BigDecimal getR135_spe_pro_expo() {
		return r135_spe_pro_expo;
	}
	public void setR135_spe_pro_expo(BigDecimal r135_spe_pro_expo) {
		this.r135_spe_pro_expo = r135_spe_pro_expo;
	}
	public BigDecimal getR135_amt_elig_sht_net() {
		return r135_amt_elig_sht_net;
	}
	public void setR135_amt_elig_sht_net(BigDecimal r135_amt_elig_sht_net) {
		this.r135_amt_elig_sht_net = r135_amt_elig_sht_net;
	}
	public BigDecimal getR135_tot_expo_net_spe() {
		return r135_tot_expo_net_spe;
	}
	public void setR135_tot_expo_net_spe(BigDecimal r135_tot_expo_net_spe) {
		this.r135_tot_expo_net_spe = r135_tot_expo_net_spe;
	}
	public BigDecimal getR135_crm_sub_elig_sub_app() {
		return r135_crm_sub_elig_sub_app;
	}
	public void setR135_crm_sub_elig_sub_app(BigDecimal r135_crm_sub_elig_sub_app) {
		this.r135_crm_sub_elig_sub_app = r135_crm_sub_elig_sub_app;
	}
	public BigDecimal getR135_crm_sub_non_col_guar() {
		return r135_crm_sub_non_col_guar;
	}
	public void setR135_crm_sub_non_col_guar(BigDecimal r135_crm_sub_non_col_guar) {
		this.r135_crm_sub_non_col_guar = r135_crm_sub_non_col_guar;
	}
	public BigDecimal getR135_crm_sub_non_col_cre_der() {
		return r135_crm_sub_non_col_cre_der;
	}
	public void setR135_crm_sub_non_col_cre_der(BigDecimal r135_crm_sub_non_col_cre_der) {
		this.r135_crm_sub_non_col_cre_der = r135_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR135_crm_sub_col_elig_cash() {
		return r135_crm_sub_col_elig_cash;
	}
	public void setR135_crm_sub_col_elig_cash(BigDecimal r135_crm_sub_col_elig_cash) {
		this.r135_crm_sub_col_elig_cash = r135_crm_sub_col_elig_cash;
	}
	public BigDecimal getR135_crm_sub_col_elig_trea_bills() {
		return r135_crm_sub_col_elig_trea_bills;
	}
	public void setR135_crm_sub_col_elig_trea_bills(BigDecimal r135_crm_sub_col_elig_trea_bills) {
		this.r135_crm_sub_col_elig_trea_bills = r135_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR135_crm_sub_col_elig_deb_sec() {
		return r135_crm_sub_col_elig_deb_sec;
	}
	public void setR135_crm_sub_col_elig_deb_sec(BigDecimal r135_crm_sub_col_elig_deb_sec) {
		this.r135_crm_sub_col_elig_deb_sec = r135_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR135_crm_sub_col_elig_equi() {
		return r135_crm_sub_col_elig_equi;
	}
	public void setR135_crm_sub_col_elig_equi(BigDecimal r135_crm_sub_col_elig_equi) {
		this.r135_crm_sub_col_elig_equi = r135_crm_sub_col_elig_equi;
	}
	public BigDecimal getR135_crm_sub_col_elig_unit_tru() {
		return r135_crm_sub_col_elig_unit_tru;
	}
	public void setR135_crm_sub_col_elig_unit_tru(BigDecimal r135_crm_sub_col_elig_unit_tru) {
		this.r135_crm_sub_col_elig_unit_tru = r135_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR135_crm_sub_col_exp_cov() {
		return r135_crm_sub_col_exp_cov;
	}
	public void setR135_crm_sub_col_exp_cov(BigDecimal r135_crm_sub_col_exp_cov) {
		this.r135_crm_sub_col_exp_cov = r135_crm_sub_col_exp_cov;
	}
	public BigDecimal getR135_crm_sub_col_elig_exp_not_cov() {
		return r135_crm_sub_col_elig_exp_not_cov;
	}
	public void setR135_crm_sub_col_elig_exp_not_cov(BigDecimal r135_crm_sub_col_elig_exp_not_cov) {
		this.r135_crm_sub_col_elig_exp_not_cov = r135_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR135_crm_sub_rwa_ris_crm() {
		return r135_crm_sub_rwa_ris_crm;
	}
	public void setR135_crm_sub_rwa_ris_crm(BigDecimal r135_crm_sub_rwa_ris_crm) {
		this.r135_crm_sub_rwa_ris_crm = r135_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR135_crm_sub_rwa_cov_crm() {
		return r135_crm_sub_rwa_cov_crm;
	}
	public void setR135_crm_sub_rwa_cov_crm(BigDecimal r135_crm_sub_rwa_cov_crm) {
		this.r135_crm_sub_rwa_cov_crm = r135_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR135_crm_sub_rwa_org_cou() {
		return r135_crm_sub_rwa_org_cou;
	}
	public void setR135_crm_sub_rwa_org_cou(BigDecimal r135_crm_sub_rwa_org_cou) {
		this.r135_crm_sub_rwa_org_cou = r135_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR135_crm_sub_rwa_not_cov_crm() {
		return r135_crm_sub_rwa_not_cov_crm;
	}
	public void setR135_crm_sub_rwa_not_cov_crm(BigDecimal r135_crm_sub_rwa_not_cov_crm) {
		this.r135_crm_sub_rwa_not_cov_crm = r135_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR135_crm_comp_col_expo_elig() {
		return r135_crm_comp_col_expo_elig;
	}
	public void setR135_crm_comp_col_expo_elig(BigDecimal r135_crm_comp_col_expo_elig) {
		this.r135_crm_comp_col_expo_elig = r135_crm_comp_col_expo_elig;
	}
	public BigDecimal getR135_crm_comp_col_elig_expo_vol_adj() {
		return r135_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR135_crm_comp_col_elig_expo_vol_adj(BigDecimal r135_crm_comp_col_elig_expo_vol_adj) {
		this.r135_crm_comp_col_elig_expo_vol_adj = r135_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR135_crm_comp_col_elig_fin_hai() {
		return r135_crm_comp_col_elig_fin_hai;
	}
	public void setR135_crm_comp_col_elig_fin_hai(BigDecimal r135_crm_comp_col_elig_fin_hai) {
		this.r135_crm_comp_col_elig_fin_hai = r135_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR135_crm_comp_col_expo_val() {
		return r135_crm_comp_col_expo_val;
	}
	public void setR135_crm_comp_col_expo_val(BigDecimal r135_crm_comp_col_expo_val) {
		this.r135_crm_comp_col_expo_val = r135_crm_comp_col_expo_val;
	}
	public BigDecimal getR135_rwa_elig_expo_not_cov_crm() {
		return r135_rwa_elig_expo_not_cov_crm;
	}
	public void setR135_rwa_elig_expo_not_cov_crm(BigDecimal r135_rwa_elig_expo_not_cov_crm) {
		this.r135_rwa_elig_expo_not_cov_crm = r135_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR135_rwa_unsec_expo_cre_ris() {
		return r135_rwa_unsec_expo_cre_ris;
	}
	public void setR135_rwa_unsec_expo_cre_ris(BigDecimal r135_rwa_unsec_expo_cre_ris) {
		this.r135_rwa_unsec_expo_cre_ris = r135_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR135_rwa_unsec_expo() {
		return r135_rwa_unsec_expo;
	}
	public void setR135_rwa_unsec_expo(BigDecimal r135_rwa_unsec_expo) {
		this.r135_rwa_unsec_expo = r135_rwa_unsec_expo;
	}
	public BigDecimal getR135_rwa_tot_ris_wei_ass() {
		return r135_rwa_tot_ris_wei_ass;
	}
	public void setR135_rwa_tot_ris_wei_ass(BigDecimal r135_rwa_tot_ris_wei_ass) {
		this.r135_rwa_tot_ris_wei_ass = r135_rwa_tot_ris_wei_ass;
	}
	public String getR136_exposure_class() {
		return r136_exposure_class;
	}
	public void setR136_exposure_class(String r136_exposure_class) {
		this.r136_exposure_class = r136_exposure_class;
	}
	public BigDecimal getR136_expo_crm() {
		return r136_expo_crm;
	}
	public void setR136_expo_crm(BigDecimal r136_expo_crm) {
		this.r136_expo_crm = r136_expo_crm;
	}
	public BigDecimal getR136_spe_pro_expo() {
		return r136_spe_pro_expo;
	}
	public void setR136_spe_pro_expo(BigDecimal r136_spe_pro_expo) {
		this.r136_spe_pro_expo = r136_spe_pro_expo;
	}
	public BigDecimal getR136_amt_elig_sht_net() {
		return r136_amt_elig_sht_net;
	}
	public void setR136_amt_elig_sht_net(BigDecimal r136_amt_elig_sht_net) {
		this.r136_amt_elig_sht_net = r136_amt_elig_sht_net;
	}
	public BigDecimal getR136_tot_expo_net_spe() {
		return r136_tot_expo_net_spe;
	}
	public void setR136_tot_expo_net_spe(BigDecimal r136_tot_expo_net_spe) {
		this.r136_tot_expo_net_spe = r136_tot_expo_net_spe;
	}
	public BigDecimal getR136_crm_sub_elig_sub_app() {
		return r136_crm_sub_elig_sub_app;
	}
	public void setR136_crm_sub_elig_sub_app(BigDecimal r136_crm_sub_elig_sub_app) {
		this.r136_crm_sub_elig_sub_app = r136_crm_sub_elig_sub_app;
	}
	public BigDecimal getR136_crm_sub_non_col_guar() {
		return r136_crm_sub_non_col_guar;
	}
	public void setR136_crm_sub_non_col_guar(BigDecimal r136_crm_sub_non_col_guar) {
		this.r136_crm_sub_non_col_guar = r136_crm_sub_non_col_guar;
	}
	public BigDecimal getR136_crm_sub_non_col_cre_der() {
		return r136_crm_sub_non_col_cre_der;
	}
	public void setR136_crm_sub_non_col_cre_der(BigDecimal r136_crm_sub_non_col_cre_der) {
		this.r136_crm_sub_non_col_cre_der = r136_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR136_crm_sub_col_elig_cash() {
		return r136_crm_sub_col_elig_cash;
	}
	public void setR136_crm_sub_col_elig_cash(BigDecimal r136_crm_sub_col_elig_cash) {
		this.r136_crm_sub_col_elig_cash = r136_crm_sub_col_elig_cash;
	}
	public BigDecimal getR136_crm_sub_col_elig_trea_bills() {
		return r136_crm_sub_col_elig_trea_bills;
	}
	public void setR136_crm_sub_col_elig_trea_bills(BigDecimal r136_crm_sub_col_elig_trea_bills) {
		this.r136_crm_sub_col_elig_trea_bills = r136_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR136_crm_sub_col_elig_deb_sec() {
		return r136_crm_sub_col_elig_deb_sec;
	}
	public void setR136_crm_sub_col_elig_deb_sec(BigDecimal r136_crm_sub_col_elig_deb_sec) {
		this.r136_crm_sub_col_elig_deb_sec = r136_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR136_crm_sub_col_elig_equi() {
		return r136_crm_sub_col_elig_equi;
	}
	public void setR136_crm_sub_col_elig_equi(BigDecimal r136_crm_sub_col_elig_equi) {
		this.r136_crm_sub_col_elig_equi = r136_crm_sub_col_elig_equi;
	}
	public BigDecimal getR136_crm_sub_col_elig_unit_tru() {
		return r136_crm_sub_col_elig_unit_tru;
	}
	public void setR136_crm_sub_col_elig_unit_tru(BigDecimal r136_crm_sub_col_elig_unit_tru) {
		this.r136_crm_sub_col_elig_unit_tru = r136_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR136_crm_sub_col_exp_cov() {
		return r136_crm_sub_col_exp_cov;
	}
	public void setR136_crm_sub_col_exp_cov(BigDecimal r136_crm_sub_col_exp_cov) {
		this.r136_crm_sub_col_exp_cov = r136_crm_sub_col_exp_cov;
	}
	public BigDecimal getR136_crm_sub_col_elig_exp_not_cov() {
		return r136_crm_sub_col_elig_exp_not_cov;
	}
	public void setR136_crm_sub_col_elig_exp_not_cov(BigDecimal r136_crm_sub_col_elig_exp_not_cov) {
		this.r136_crm_sub_col_elig_exp_not_cov = r136_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR136_crm_sub_rwa_ris_crm() {
		return r136_crm_sub_rwa_ris_crm;
	}
	public void setR136_crm_sub_rwa_ris_crm(BigDecimal r136_crm_sub_rwa_ris_crm) {
		this.r136_crm_sub_rwa_ris_crm = r136_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR136_crm_sub_rwa_cov_crm() {
		return r136_crm_sub_rwa_cov_crm;
	}
	public void setR136_crm_sub_rwa_cov_crm(BigDecimal r136_crm_sub_rwa_cov_crm) {
		this.r136_crm_sub_rwa_cov_crm = r136_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR136_crm_sub_rwa_org_cou() {
		return r136_crm_sub_rwa_org_cou;
	}
	public void setR136_crm_sub_rwa_org_cou(BigDecimal r136_crm_sub_rwa_org_cou) {
		this.r136_crm_sub_rwa_org_cou = r136_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR136_crm_sub_rwa_not_cov_crm() {
		return r136_crm_sub_rwa_not_cov_crm;
	}
	public void setR136_crm_sub_rwa_not_cov_crm(BigDecimal r136_crm_sub_rwa_not_cov_crm) {
		this.r136_crm_sub_rwa_not_cov_crm = r136_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR136_crm_comp_col_expo_elig() {
		return r136_crm_comp_col_expo_elig;
	}
	public void setR136_crm_comp_col_expo_elig(BigDecimal r136_crm_comp_col_expo_elig) {
		this.r136_crm_comp_col_expo_elig = r136_crm_comp_col_expo_elig;
	}
	public BigDecimal getR136_crm_comp_col_elig_expo_vol_adj() {
		return r136_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR136_crm_comp_col_elig_expo_vol_adj(BigDecimal r136_crm_comp_col_elig_expo_vol_adj) {
		this.r136_crm_comp_col_elig_expo_vol_adj = r136_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR136_crm_comp_col_elig_fin_hai() {
		return r136_crm_comp_col_elig_fin_hai;
	}
	public void setR136_crm_comp_col_elig_fin_hai(BigDecimal r136_crm_comp_col_elig_fin_hai) {
		this.r136_crm_comp_col_elig_fin_hai = r136_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR136_crm_comp_col_expo_val() {
		return r136_crm_comp_col_expo_val;
	}
	public void setR136_crm_comp_col_expo_val(BigDecimal r136_crm_comp_col_expo_val) {
		this.r136_crm_comp_col_expo_val = r136_crm_comp_col_expo_val;
	}
	public BigDecimal getR136_rwa_elig_expo_not_cov_crm() {
		return r136_rwa_elig_expo_not_cov_crm;
	}
	public void setR136_rwa_elig_expo_not_cov_crm(BigDecimal r136_rwa_elig_expo_not_cov_crm) {
		this.r136_rwa_elig_expo_not_cov_crm = r136_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR136_rwa_unsec_expo_cre_ris() {
		return r136_rwa_unsec_expo_cre_ris;
	}
	public void setR136_rwa_unsec_expo_cre_ris(BigDecimal r136_rwa_unsec_expo_cre_ris) {
		this.r136_rwa_unsec_expo_cre_ris = r136_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR136_rwa_unsec_expo() {
		return r136_rwa_unsec_expo;
	}
	public void setR136_rwa_unsec_expo(BigDecimal r136_rwa_unsec_expo) {
		this.r136_rwa_unsec_expo = r136_rwa_unsec_expo;
	}
	public BigDecimal getR136_rwa_tot_ris_wei_ass() {
		return r136_rwa_tot_ris_wei_ass;
	}
	public void setR136_rwa_tot_ris_wei_ass(BigDecimal r136_rwa_tot_ris_wei_ass) {
		this.r136_rwa_tot_ris_wei_ass = r136_rwa_tot_ris_wei_ass;
	}
	public String getR144_exposure_class_off_bal() {
		return r144_exposure_class_off_bal;
	}
	public void setR144_exposure_class_off_bal(String r144_exposure_class_off_bal) {
		this.r144_exposure_class_off_bal = r144_exposure_class_off_bal;
	}
	public BigDecimal getR144_nom_pri_amt() {
		return r144_nom_pri_amt;
	}
	public void setR144_nom_pri_amt(BigDecimal r144_nom_pri_amt) {
		this.r144_nom_pri_amt = r144_nom_pri_amt;
	}
	public BigDecimal getR144_ccf() {
		return r144_ccf;
	}
	public void setR144_ccf(BigDecimal r144_ccf) {
		this.r144_ccf = r144_ccf;
	}
	public BigDecimal getR144_cea() {
		return r144_cea;
	}
	public void setR144_cea(BigDecimal r144_cea) {
		this.r144_cea = r144_cea;
	}
	public BigDecimal getR144_cea_elig_coun_bilt_net() {
		return r144_cea_elig_coun_bilt_net;
	}
	public void setR144_cea_elig_coun_bilt_net(BigDecimal r144_cea_elig_coun_bilt_net) {
		this.r144_cea_elig_coun_bilt_net = r144_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR144_cea_aft_net() {
		return r144_cea_aft_net;
	}
	public void setR144_cea_aft_net(BigDecimal r144_cea_aft_net) {
		this.r144_cea_aft_net = r144_cea_aft_net;
	}
	public BigDecimal getR144_crm_sub_app_cea_elig() {
		return r144_crm_sub_app_cea_elig;
	}
	public void setR144_crm_sub_app_cea_elig(BigDecimal r144_crm_sub_app_cea_elig) {
		this.r144_crm_sub_app_cea_elig = r144_crm_sub_app_cea_elig;
	}
	public BigDecimal getR144_crm_sub_app_non_col_guar_elig() {
		return r144_crm_sub_app_non_col_guar_elig;
	}
	public void setR144_crm_sub_app_non_col_guar_elig(BigDecimal r144_crm_sub_app_non_col_guar_elig) {
		this.r144_crm_sub_app_non_col_guar_elig = r144_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR144_crm_sub_app_non_col_cre_der() {
		return r144_crm_sub_app_non_col_cre_der;
	}
	public void setR144_crm_sub_app_non_col_cre_der(BigDecimal r144_crm_sub_app_non_col_cre_der) {
		this.r144_crm_sub_app_non_col_cre_der = r144_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR144_crm_sub_app_col_elig_cash() {
		return r144_crm_sub_app_col_elig_cash;
	}
	public void setR144_crm_sub_app_col_elig_cash(BigDecimal r144_crm_sub_app_col_elig_cash) {
		this.r144_crm_sub_app_col_elig_cash = r144_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR144_crm_sub_app_col_elig_tre_bills() {
		return r144_crm_sub_app_col_elig_tre_bills;
	}
	public void setR144_crm_sub_app_col_elig_tre_bills(BigDecimal r144_crm_sub_app_col_elig_tre_bills) {
		this.r144_crm_sub_app_col_elig_tre_bills = r144_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR144_crm_sub_app_col_elig_deb_sec() {
		return r144_crm_sub_app_col_elig_deb_sec;
	}
	public void setR144_crm_sub_app_col_elig_deb_sec(BigDecimal r144_crm_sub_app_col_elig_deb_sec) {
		this.r144_crm_sub_app_col_elig_deb_sec = r144_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR144_crm_sub_app_col_elig_euiq() {
		return r144_crm_sub_app_col_elig_euiq;
	}
	public void setR144_crm_sub_app_col_elig_euiq(BigDecimal r144_crm_sub_app_col_elig_euiq) {
		this.r144_crm_sub_app_col_elig_euiq = r144_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR144_crm_sub_app_col_elig_uni_tru() {
		return r144_crm_sub_app_col_elig_uni_tru;
	}
	public void setR144_crm_sub_app_col_elig_uni_tru(BigDecimal r144_crm_sub_app_col_elig_uni_tru) {
		this.r144_crm_sub_app_col_elig_uni_tru = r144_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR144_crm_sub_app_col_cea_cov() {
		return r144_crm_sub_app_col_cea_cov;
	}
	public void setR144_crm_sub_app_col_cea_cov(BigDecimal r144_crm_sub_app_col_cea_cov) {
		this.r144_crm_sub_app_col_cea_cov = r144_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR144_crm_sub_app_col_cea_not_cov() {
		return r144_crm_sub_app_col_cea_not_cov;
	}
	public void setR144_crm_sub_app_col_cea_not_cov(BigDecimal r144_crm_sub_app_col_cea_not_cov) {
		this.r144_crm_sub_app_col_cea_not_cov = r144_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR144_crm_sub_app_rwa_ris_wei_crm() {
		return r144_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR144_crm_sub_app_rwa_ris_wei_crm(BigDecimal r144_crm_sub_app_rwa_ris_wei_crm) {
		this.r144_crm_sub_app_rwa_ris_wei_crm = r144_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR144_crm_sub_app_rwa_ris_cea_cov() {
		return r144_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR144_crm_sub_app_rwa_ris_cea_cov(BigDecimal r144_crm_sub_app_rwa_ris_cea_cov) {
		this.r144_crm_sub_app_rwa_ris_cea_cov = r144_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR144_crm_sub_app_rwa_appl_org_coun() {
		return r144_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR144_crm_sub_app_rwa_appl_org_coun(BigDecimal r144_crm_sub_app_rwa_appl_org_coun) {
		this.r144_crm_sub_app_rwa_appl_org_coun = r144_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR144_crm_sub_app_rwa_ris_cea_not_cov() {
		return r144_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR144_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r144_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r144_crm_sub_app_rwa_ris_cea_not_cov = r144_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR144_crm_com_app_col_cea_elig_crm() {
		return r144_crm_com_app_col_cea_elig_crm;
	}
	public void setR144_crm_com_app_col_cea_elig_crm(BigDecimal r144_crm_com_app_col_cea_elig_crm) {
		this.r144_crm_com_app_col_cea_elig_crm = r144_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR144_crm_com_app_col_elig_cea_vol_adj() {
		return r144_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR144_crm_com_app_col_elig_cea_vol_adj(BigDecimal r144_crm_com_app_col_elig_cea_vol_adj) {
		this.r144_crm_com_app_col_elig_cea_vol_adj = r144_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR144_crm_com_app_col_elig_fin_hai() {
		return r144_crm_com_app_col_elig_fin_hai;
	}
	public void setR144_crm_com_app_col_elig_fin_hai(BigDecimal r144_crm_com_app_col_elig_fin_hai) {
		this.r144_crm_com_app_col_elig_fin_hai = r144_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR144_crm_com_app_col_cea_val_aft_crm() {
		return r144_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR144_crm_com_app_col_cea_val_aft_crm(BigDecimal r144_crm_com_app_col_cea_val_aft_crm) {
		this.r144_crm_com_app_col_cea_val_aft_crm = r144_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR144_rwa_elig_cea_not_cov() {
		return r144_rwa_elig_cea_not_cov;
	}
	public void setR144_rwa_elig_cea_not_cov(BigDecimal r144_rwa_elig_cea_not_cov) {
		this.r144_rwa_elig_cea_not_cov = r144_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR144_rwa_unsec_cea_sub_cre_ris() {
		return r144_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR144_rwa_unsec_cea_sub_cre_ris(BigDecimal r144_rwa_unsec_cea_sub_cre_ris) {
		this.r144_rwa_unsec_cea_sub_cre_ris = r144_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR144_rwa_unsec_cea() {
		return r144_rwa_unsec_cea;
	}
	public void setR144_rwa_unsec_cea(BigDecimal r144_rwa_unsec_cea) {
		this.r144_rwa_unsec_cea = r144_rwa_unsec_cea;
	}
	public BigDecimal getR144_rwa_tot_ris_wei_ass() {
		return r144_rwa_tot_ris_wei_ass;
	}
	public void setR144_rwa_tot_ris_wei_ass(BigDecimal r144_rwa_tot_ris_wei_ass) {
		this.r144_rwa_tot_ris_wei_ass = r144_rwa_tot_ris_wei_ass;
	}
	public BigDecimal getR251_nom_pri_amt() {
		return r251_nom_pri_amt;
	}
	public void setR251_nom_pri_amt(BigDecimal r251_nom_pri_amt) {
		this.r251_nom_pri_amt = r251_nom_pri_amt;
	}
	public BigDecimal getR251_ccf() {
		return r251_ccf;
	}
	public void setR251_ccf(BigDecimal r251_ccf) {
		this.r251_ccf = r251_ccf;
	}
	public BigDecimal getR251_cea() {
		return r251_cea;
	}
	public void setR251_cea(BigDecimal r251_cea) {
		this.r251_cea = r251_cea;
	}
	public BigDecimal getR251_cea_elig_coun_bilt_net() {
		return r251_cea_elig_coun_bilt_net;
	}
	public void setR251_cea_elig_coun_bilt_net(BigDecimal r251_cea_elig_coun_bilt_net) {
		this.r251_cea_elig_coun_bilt_net = r251_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR251_cea_aft_net() {
		return r251_cea_aft_net;
	}
	public void setR251_cea_aft_net(BigDecimal r251_cea_aft_net) {
		this.r251_cea_aft_net = r251_cea_aft_net;
	}
	public BigDecimal getR251_crm_sub_app_cea_elig() {
		return r251_crm_sub_app_cea_elig;
	}
	public void setR251_crm_sub_app_cea_elig(BigDecimal r251_crm_sub_app_cea_elig) {
		this.r251_crm_sub_app_cea_elig = r251_crm_sub_app_cea_elig;
	}
	public BigDecimal getR251_crm_sub_app_non_col_guar_elig() {
		return r251_crm_sub_app_non_col_guar_elig;
	}
	public void setR251_crm_sub_app_non_col_guar_elig(BigDecimal r251_crm_sub_app_non_col_guar_elig) {
		this.r251_crm_sub_app_non_col_guar_elig = r251_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR251_crm_sub_app_non_col_cre_der() {
		return r251_crm_sub_app_non_col_cre_der;
	}
	public void setR251_crm_sub_app_non_col_cre_der(BigDecimal r251_crm_sub_app_non_col_cre_der) {
		this.r251_crm_sub_app_non_col_cre_der = r251_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR251_crm_sub_app_col_elig_cash() {
		return r251_crm_sub_app_col_elig_cash;
	}
	public void setR251_crm_sub_app_col_elig_cash(BigDecimal r251_crm_sub_app_col_elig_cash) {
		this.r251_crm_sub_app_col_elig_cash = r251_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR251_crm_sub_app_col_elig_tre_bills() {
		return r251_crm_sub_app_col_elig_tre_bills;
	}
	public void setR251_crm_sub_app_col_elig_tre_bills(BigDecimal r251_crm_sub_app_col_elig_tre_bills) {
		this.r251_crm_sub_app_col_elig_tre_bills = r251_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR251_crm_sub_app_col_elig_deb_sec() {
		return r251_crm_sub_app_col_elig_deb_sec;
	}
	public void setR251_crm_sub_app_col_elig_deb_sec(BigDecimal r251_crm_sub_app_col_elig_deb_sec) {
		this.r251_crm_sub_app_col_elig_deb_sec = r251_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR251_crm_sub_app_col_elig_euiq() {
		return r251_crm_sub_app_col_elig_euiq;
	}
	public void setR251_crm_sub_app_col_elig_euiq(BigDecimal r251_crm_sub_app_col_elig_euiq) {
		this.r251_crm_sub_app_col_elig_euiq = r251_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR251_crm_sub_app_col_elig_uni_tru() {
		return r251_crm_sub_app_col_elig_uni_tru;
	}
	public void setR251_crm_sub_app_col_elig_uni_tru(BigDecimal r251_crm_sub_app_col_elig_uni_tru) {
		this.r251_crm_sub_app_col_elig_uni_tru = r251_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR251_crm_sub_app_col_cea_cov() {
		return r251_crm_sub_app_col_cea_cov;
	}
	public void setR251_crm_sub_app_col_cea_cov(BigDecimal r251_crm_sub_app_col_cea_cov) {
		this.r251_crm_sub_app_col_cea_cov = r251_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR251_crm_sub_app_col_cea_not_cov() {
		return r251_crm_sub_app_col_cea_not_cov;
	}
	public void setR251_crm_sub_app_col_cea_not_cov(BigDecimal r251_crm_sub_app_col_cea_not_cov) {
		this.r251_crm_sub_app_col_cea_not_cov = r251_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR251_crm_sub_app_rwa_ris_wei_crm() {
		return r251_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR251_crm_sub_app_rwa_ris_wei_crm(BigDecimal r251_crm_sub_app_rwa_ris_wei_crm) {
		this.r251_crm_sub_app_rwa_ris_wei_crm = r251_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR251_crm_sub_app_rwa_ris_cea_cov() {
		return r251_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR251_crm_sub_app_rwa_ris_cea_cov(BigDecimal r251_crm_sub_app_rwa_ris_cea_cov) {
		this.r251_crm_sub_app_rwa_ris_cea_cov = r251_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR251_crm_sub_app_rwa_appl_org_coun() {
		return r251_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR251_crm_sub_app_rwa_appl_org_coun(BigDecimal r251_crm_sub_app_rwa_appl_org_coun) {
		this.r251_crm_sub_app_rwa_appl_org_coun = r251_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR251_crm_sub_app_rwa_ris_cea_not_cov() {
		return r251_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR251_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r251_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r251_crm_sub_app_rwa_ris_cea_not_cov = r251_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR251_crm_com_app_col_cea_elig_crm() {
		return r251_crm_com_app_col_cea_elig_crm;
	}
	public void setR251_crm_com_app_col_cea_elig_crm(BigDecimal r251_crm_com_app_col_cea_elig_crm) {
		this.r251_crm_com_app_col_cea_elig_crm = r251_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR251_crm_com_app_col_elig_cea_vol_adj() {
		return r251_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR251_crm_com_app_col_elig_cea_vol_adj(BigDecimal r251_crm_com_app_col_elig_cea_vol_adj) {
		this.r251_crm_com_app_col_elig_cea_vol_adj = r251_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR251_crm_com_app_col_elig_fin_hai() {
		return r251_crm_com_app_col_elig_fin_hai;
	}
	public void setR251_crm_com_app_col_elig_fin_hai(BigDecimal r251_crm_com_app_col_elig_fin_hai) {
		this.r251_crm_com_app_col_elig_fin_hai = r251_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR251_crm_com_app_col_cea_val_aft_crm() {
		return r251_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR251_crm_com_app_col_cea_val_aft_crm(BigDecimal r251_crm_com_app_col_cea_val_aft_crm) {
		this.r251_crm_com_app_col_cea_val_aft_crm = r251_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR251_rwa_elig_cea_not_cov() {
		return r251_rwa_elig_cea_not_cov;
	}
	public void setR251_rwa_elig_cea_not_cov(BigDecimal r251_rwa_elig_cea_not_cov) {
		this.r251_rwa_elig_cea_not_cov = r251_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR251_rwa_unsec_cea_sub_cre_ris() {
		return r251_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR251_rwa_unsec_cea_sub_cre_ris(BigDecimal r251_rwa_unsec_cea_sub_cre_ris) {
		this.r251_rwa_unsec_cea_sub_cre_ris = r251_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR251_rwa_unsec_cea() {
		return r251_rwa_unsec_cea;
	}
	public void setR251_rwa_unsec_cea(BigDecimal r251_rwa_unsec_cea) {
		this.r251_rwa_unsec_cea = r251_rwa_unsec_cea;
	}
	public BigDecimal getR252_nom_pri_amt() {
		return r252_nom_pri_amt;
	}
	public void setR252_nom_pri_amt(BigDecimal r252_nom_pri_amt) {
		this.r252_nom_pri_amt = r252_nom_pri_amt;
	}
	public BigDecimal getR252_ccf() {
		return r252_ccf;
	}
	public void setR252_ccf(BigDecimal r252_ccf) {
		this.r252_ccf = r252_ccf;
	}
	public BigDecimal getR252_cea() {
		return r252_cea;
	}
	public void setR252_cea(BigDecimal r252_cea) {
		this.r252_cea = r252_cea;
	}
	public BigDecimal getR252_cea_elig_coun_bilt_net() {
		return r252_cea_elig_coun_bilt_net;
	}
	public void setR252_cea_elig_coun_bilt_net(BigDecimal r252_cea_elig_coun_bilt_net) {
		this.r252_cea_elig_coun_bilt_net = r252_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR252_cea_aft_net() {
		return r252_cea_aft_net;
	}
	public void setR252_cea_aft_net(BigDecimal r252_cea_aft_net) {
		this.r252_cea_aft_net = r252_cea_aft_net;
	}
	public BigDecimal getR252_crm_sub_app_cea_elig() {
		return r252_crm_sub_app_cea_elig;
	}
	public void setR252_crm_sub_app_cea_elig(BigDecimal r252_crm_sub_app_cea_elig) {
		this.r252_crm_sub_app_cea_elig = r252_crm_sub_app_cea_elig;
	}
	public BigDecimal getR252_crm_sub_app_non_col_guar_elig() {
		return r252_crm_sub_app_non_col_guar_elig;
	}
	public void setR252_crm_sub_app_non_col_guar_elig(BigDecimal r252_crm_sub_app_non_col_guar_elig) {
		this.r252_crm_sub_app_non_col_guar_elig = r252_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR252_crm_sub_app_non_col_cre_der() {
		return r252_crm_sub_app_non_col_cre_der;
	}
	public void setR252_crm_sub_app_non_col_cre_der(BigDecimal r252_crm_sub_app_non_col_cre_der) {
		this.r252_crm_sub_app_non_col_cre_der = r252_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR252_crm_sub_app_col_elig_cash() {
		return r252_crm_sub_app_col_elig_cash;
	}
	public void setR252_crm_sub_app_col_elig_cash(BigDecimal r252_crm_sub_app_col_elig_cash) {
		this.r252_crm_sub_app_col_elig_cash = r252_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR252_crm_sub_app_col_elig_tre_bills() {
		return r252_crm_sub_app_col_elig_tre_bills;
	}
	public void setR252_crm_sub_app_col_elig_tre_bills(BigDecimal r252_crm_sub_app_col_elig_tre_bills) {
		this.r252_crm_sub_app_col_elig_tre_bills = r252_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR252_crm_sub_app_col_elig_deb_sec() {
		return r252_crm_sub_app_col_elig_deb_sec;
	}
	public void setR252_crm_sub_app_col_elig_deb_sec(BigDecimal r252_crm_sub_app_col_elig_deb_sec) {
		this.r252_crm_sub_app_col_elig_deb_sec = r252_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR252_crm_sub_app_col_elig_euiq() {
		return r252_crm_sub_app_col_elig_euiq;
	}
	public void setR252_crm_sub_app_col_elig_euiq(BigDecimal r252_crm_sub_app_col_elig_euiq) {
		this.r252_crm_sub_app_col_elig_euiq = r252_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR252_crm_sub_app_col_elig_uni_tru() {
		return r252_crm_sub_app_col_elig_uni_tru;
	}
	public void setR252_crm_sub_app_col_elig_uni_tru(BigDecimal r252_crm_sub_app_col_elig_uni_tru) {
		this.r252_crm_sub_app_col_elig_uni_tru = r252_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR252_crm_sub_app_col_cea_cov() {
		return r252_crm_sub_app_col_cea_cov;
	}
	public void setR252_crm_sub_app_col_cea_cov(BigDecimal r252_crm_sub_app_col_cea_cov) {
		this.r252_crm_sub_app_col_cea_cov = r252_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR252_crm_sub_app_col_cea_not_cov() {
		return r252_crm_sub_app_col_cea_not_cov;
	}
	public void setR252_crm_sub_app_col_cea_not_cov(BigDecimal r252_crm_sub_app_col_cea_not_cov) {
		this.r252_crm_sub_app_col_cea_not_cov = r252_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR252_crm_sub_app_rwa_ris_wei_crm() {
		return r252_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR252_crm_sub_app_rwa_ris_wei_crm(BigDecimal r252_crm_sub_app_rwa_ris_wei_crm) {
		this.r252_crm_sub_app_rwa_ris_wei_crm = r252_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR252_crm_sub_app_rwa_ris_cea_cov() {
		return r252_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR252_crm_sub_app_rwa_ris_cea_cov(BigDecimal r252_crm_sub_app_rwa_ris_cea_cov) {
		this.r252_crm_sub_app_rwa_ris_cea_cov = r252_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR252_crm_sub_app_rwa_appl_org_coun() {
		return r252_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR252_crm_sub_app_rwa_appl_org_coun(BigDecimal r252_crm_sub_app_rwa_appl_org_coun) {
		this.r252_crm_sub_app_rwa_appl_org_coun = r252_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR252_crm_sub_app_rwa_ris_cea_not_cov() {
		return r252_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR252_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r252_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r252_crm_sub_app_rwa_ris_cea_not_cov = r252_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR252_crm_com_app_col_cea_elig_crm() {
		return r252_crm_com_app_col_cea_elig_crm;
	}
	public void setR252_crm_com_app_col_cea_elig_crm(BigDecimal r252_crm_com_app_col_cea_elig_crm) {
		this.r252_crm_com_app_col_cea_elig_crm = r252_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR252_crm_com_app_col_elig_cea_vol_adj() {
		return r252_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR252_crm_com_app_col_elig_cea_vol_adj(BigDecimal r252_crm_com_app_col_elig_cea_vol_adj) {
		this.r252_crm_com_app_col_elig_cea_vol_adj = r252_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR252_crm_com_app_col_elig_fin_hai() {
		return r252_crm_com_app_col_elig_fin_hai;
	}
	public void setR252_crm_com_app_col_elig_fin_hai(BigDecimal r252_crm_com_app_col_elig_fin_hai) {
		this.r252_crm_com_app_col_elig_fin_hai = r252_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR252_crm_com_app_col_cea_val_aft_crm() {
		return r252_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR252_crm_com_app_col_cea_val_aft_crm(BigDecimal r252_crm_com_app_col_cea_val_aft_crm) {
		this.r252_crm_com_app_col_cea_val_aft_crm = r252_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR252_rwa_elig_cea_not_cov() {
		return r252_rwa_elig_cea_not_cov;
	}
	public void setR252_rwa_elig_cea_not_cov(BigDecimal r252_rwa_elig_cea_not_cov) {
		this.r252_rwa_elig_cea_not_cov = r252_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR252_rwa_unsec_cea_sub_cre_ris() {
		return r252_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR252_rwa_unsec_cea_sub_cre_ris(BigDecimal r252_rwa_unsec_cea_sub_cre_ris) {
		this.r252_rwa_unsec_cea_sub_cre_ris = r252_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR252_rwa_unsec_cea() {
		return r252_rwa_unsec_cea;
	}
	public void setR252_rwa_unsec_cea(BigDecimal r252_rwa_unsec_cea) {
		this.r252_rwa_unsec_cea = r252_rwa_unsec_cea;
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
	public M_SRWA_12A_Summary_M_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BigDecimal getR254_nom_pri_amt() {
		return r254_nom_pri_amt;
	}
	public void setR254_nom_pri_amt(BigDecimal r254_nom_pri_amt) {
		this.r254_nom_pri_amt = r254_nom_pri_amt;
	}
	public BigDecimal getR254_ccf() {
		return r254_ccf;
	}
	public void setR254_ccf(BigDecimal r254_ccf) {
		this.r254_ccf = r254_ccf;
	}
	public BigDecimal getR254_cea() {
		return r254_cea;
	}
	public void setR254_cea(BigDecimal r254_cea) {
		this.r254_cea = r254_cea;
	}
	public BigDecimal getR254_cea_elig_coun_bilt_net() {
		return r254_cea_elig_coun_bilt_net;
	}
	public void setR254_cea_elig_coun_bilt_net(BigDecimal r254_cea_elig_coun_bilt_net) {
		this.r254_cea_elig_coun_bilt_net = r254_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR254_cea_aft_net() {
		return r254_cea_aft_net;
	}
	public void setR254_cea_aft_net(BigDecimal r254_cea_aft_net) {
		this.r254_cea_aft_net = r254_cea_aft_net;
	}
	public BigDecimal getR254_crm_sub_app_cea_elig() {
		return r254_crm_sub_app_cea_elig;
	}
	public void setR254_crm_sub_app_cea_elig(BigDecimal r254_crm_sub_app_cea_elig) {
		this.r254_crm_sub_app_cea_elig = r254_crm_sub_app_cea_elig;
	}
	public BigDecimal getR254_crm_sub_app_non_col_guar_elig() {
		return r254_crm_sub_app_non_col_guar_elig;
	}
	public void setR254_crm_sub_app_non_col_guar_elig(BigDecimal r254_crm_sub_app_non_col_guar_elig) {
		this.r254_crm_sub_app_non_col_guar_elig = r254_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR254_crm_sub_app_non_col_cre_der() {
		return r254_crm_sub_app_non_col_cre_der;
	}
	public void setR254_crm_sub_app_non_col_cre_der(BigDecimal r254_crm_sub_app_non_col_cre_der) {
		this.r254_crm_sub_app_non_col_cre_der = r254_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR254_crm_sub_app_col_elig_cash() {
		return r254_crm_sub_app_col_elig_cash;
	}
	public void setR254_crm_sub_app_col_elig_cash(BigDecimal r254_crm_sub_app_col_elig_cash) {
		this.r254_crm_sub_app_col_elig_cash = r254_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR254_crm_sub_app_col_elig_tre_bills() {
		return r254_crm_sub_app_col_elig_tre_bills;
	}
	public void setR254_crm_sub_app_col_elig_tre_bills(BigDecimal r254_crm_sub_app_col_elig_tre_bills) {
		this.r254_crm_sub_app_col_elig_tre_bills = r254_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR254_crm_sub_app_col_elig_deb_sec() {
		return r254_crm_sub_app_col_elig_deb_sec;
	}
	public void setR254_crm_sub_app_col_elig_deb_sec(BigDecimal r254_crm_sub_app_col_elig_deb_sec) {
		this.r254_crm_sub_app_col_elig_deb_sec = r254_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR254_crm_sub_app_col_elig_euiq() {
		return r254_crm_sub_app_col_elig_euiq;
	}
	public void setR254_crm_sub_app_col_elig_euiq(BigDecimal r254_crm_sub_app_col_elig_euiq) {
		this.r254_crm_sub_app_col_elig_euiq = r254_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR254_crm_sub_app_col_elig_uni_tru() {
		return r254_crm_sub_app_col_elig_uni_tru;
	}
	public void setR254_crm_sub_app_col_elig_uni_tru(BigDecimal r254_crm_sub_app_col_elig_uni_tru) {
		this.r254_crm_sub_app_col_elig_uni_tru = r254_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR254_crm_sub_app_col_cea_cov() {
		return r254_crm_sub_app_col_cea_cov;
	}
	public void setR254_crm_sub_app_col_cea_cov(BigDecimal r254_crm_sub_app_col_cea_cov) {
		this.r254_crm_sub_app_col_cea_cov = r254_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR254_crm_sub_app_col_cea_not_cov() {
		return r254_crm_sub_app_col_cea_not_cov;
	}
	public void setR254_crm_sub_app_col_cea_not_cov(BigDecimal r254_crm_sub_app_col_cea_not_cov) {
		this.r254_crm_sub_app_col_cea_not_cov = r254_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR254_crm_sub_app_rwa_ris_wei_crm() {
		return r254_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR254_crm_sub_app_rwa_ris_wei_crm(BigDecimal r254_crm_sub_app_rwa_ris_wei_crm) {
		this.r254_crm_sub_app_rwa_ris_wei_crm = r254_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR254_crm_sub_app_rwa_ris_cea_cov() {
		return r254_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR254_crm_sub_app_rwa_ris_cea_cov(BigDecimal r254_crm_sub_app_rwa_ris_cea_cov) {
		this.r254_crm_sub_app_rwa_ris_cea_cov = r254_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR254_crm_sub_app_rwa_appl_org_coun() {
		return r254_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR254_crm_sub_app_rwa_appl_org_coun(BigDecimal r254_crm_sub_app_rwa_appl_org_coun) {
		this.r254_crm_sub_app_rwa_appl_org_coun = r254_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR254_crm_sub_app_rwa_ris_cea_not_cov() {
		return r254_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR254_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r254_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r254_crm_sub_app_rwa_ris_cea_not_cov = r254_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR254_crm_com_app_col_cea_elig_crm() {
		return r254_crm_com_app_col_cea_elig_crm;
	}
	public void setR254_crm_com_app_col_cea_elig_crm(BigDecimal r254_crm_com_app_col_cea_elig_crm) {
		this.r254_crm_com_app_col_cea_elig_crm = r254_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR254_crm_com_app_col_elig_cea_vol_adj() {
		return r254_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR254_crm_com_app_col_elig_cea_vol_adj(BigDecimal r254_crm_com_app_col_elig_cea_vol_adj) {
		this.r254_crm_com_app_col_elig_cea_vol_adj = r254_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR254_crm_com_app_col_elig_fin_hai() {
		return r254_crm_com_app_col_elig_fin_hai;
	}
	public void setR254_crm_com_app_col_elig_fin_hai(BigDecimal r254_crm_com_app_col_elig_fin_hai) {
		this.r254_crm_com_app_col_elig_fin_hai = r254_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR254_crm_com_app_col_cea_val_aft_crm() {
		return r254_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR254_crm_com_app_col_cea_val_aft_crm(BigDecimal r254_crm_com_app_col_cea_val_aft_crm) {
		this.r254_crm_com_app_col_cea_val_aft_crm = r254_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR254_rwa_elig_cea_not_cov() {
		return r254_rwa_elig_cea_not_cov;
	}
	public void setR254_rwa_elig_cea_not_cov(BigDecimal r254_rwa_elig_cea_not_cov) {
		this.r254_rwa_elig_cea_not_cov = r254_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR254_rwa_unsec_cea_sub_cre_ris() {
		return r254_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR254_rwa_unsec_cea_sub_cre_ris(BigDecimal r254_rwa_unsec_cea_sub_cre_ris) {
		this.r254_rwa_unsec_cea_sub_cre_ris = r254_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR254_rwa_unsec_cea() {
		return r254_rwa_unsec_cea;
	}
	public void setR254_rwa_unsec_cea(BigDecimal r254_rwa_unsec_cea) {
		this.r254_rwa_unsec_cea = r254_rwa_unsec_cea;
	}
	
	
	

}
