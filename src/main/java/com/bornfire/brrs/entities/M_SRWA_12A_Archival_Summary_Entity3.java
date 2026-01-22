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
@Table(name = "BRRS_M_SRWA_12A_ARCHIVALTABLE_SUMMARY3")


public class M_SRWA_12A_Archival_Summary_Entity3 {
	
	
	private String	r100_exposure_class;
	private BigDecimal	r100_expo_crm;
	private BigDecimal	r100_spe_pro_expo;
	private BigDecimal	r100_amt_elig_sht_net;
	private BigDecimal	r100_tot_expo_net_spe;
	private BigDecimal	r100_crm_sub_elig_sub_app;
	private BigDecimal	r100_crm_sub_non_col_guar;
	private BigDecimal	r100_crm_sub_non_col_cre_der;
	private BigDecimal	r100_crm_sub_col_elig_cash;
	private BigDecimal	r100_crm_sub_col_elig_trea_bills;
	private BigDecimal	r100_crm_sub_col_elig_deb_sec;
	private BigDecimal	r100_crm_sub_col_elig_equi;
	private BigDecimal	r100_crm_sub_col_elig_unit_tru;
	private BigDecimal	r100_crm_sub_col_exp_cov;
	private BigDecimal	r100_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r100_crm_sub_rwa_ris_crm;
	private BigDecimal	r100_crm_sub_rwa_cov_crm;
	private BigDecimal	r100_crm_sub_rwa_org_cou;
	private BigDecimal	r100_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r100_crm_comp_col_expo_elig;
	private BigDecimal	r100_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r100_crm_comp_col_elig_fin_hai;
	private BigDecimal	r100_crm_comp_col_expo_val;
	private BigDecimal	r100_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r100_rwa_unsec_expo_cre_ris;
	private BigDecimal	r100_rwa_unsec_expo;
	private BigDecimal	r100_rwa_tot_ris_wei_ass;
	
	private String	r101_exposure_class;
	private BigDecimal	r101_expo_crm;
	private BigDecimal	r101_spe_pro_expo;
	private BigDecimal	r101_amt_elig_sht_net;
	private BigDecimal	r101_tot_expo_net_spe;
	private BigDecimal	r101_crm_sub_elig_sub_app;
	private BigDecimal	r101_crm_sub_non_col_guar;
	private BigDecimal	r101_crm_sub_non_col_cre_der;
	private BigDecimal	r101_crm_sub_col_elig_cash;
	private BigDecimal	r101_crm_sub_col_elig_trea_bills;
	private BigDecimal	r101_crm_sub_col_elig_deb_sec;
	private BigDecimal	r101_crm_sub_col_elig_equi;
	private BigDecimal	r101_crm_sub_col_elig_unit_tru;
	private BigDecimal	r101_crm_sub_col_exp_cov;
	private BigDecimal	r101_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r101_crm_sub_rwa_ris_crm;
	private BigDecimal	r101_crm_sub_rwa_cov_crm;
	private BigDecimal	r101_crm_sub_rwa_org_cou;
	private BigDecimal	r101_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r101_crm_comp_col_expo_elig;
	private BigDecimal	r101_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r101_crm_comp_col_elig_fin_hai;
	private BigDecimal	r101_crm_comp_col_expo_val;
	private BigDecimal	r101_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r101_rwa_unsec_expo_cre_ris;
	private BigDecimal	r101_rwa_unsec_expo;
	private BigDecimal	r101_rwa_tot_ris_wei_ass;
	
	private String	r102_exposure_class;
	private BigDecimal	r102_expo_crm;
	private BigDecimal	r102_spe_pro_expo;
	private BigDecimal	r102_amt_elig_sht_net;
	private BigDecimal	r102_tot_expo_net_spe;
	private BigDecimal	r102_crm_sub_elig_sub_app;
	private BigDecimal	r102_crm_sub_non_col_guar;
	private BigDecimal	r102_crm_sub_non_col_cre_der;
	private BigDecimal	r102_crm_sub_col_elig_cash;
	private BigDecimal	r102_crm_sub_col_elig_trea_bills;
	private BigDecimal	r102_crm_sub_col_elig_deb_sec;
	private BigDecimal	r102_crm_sub_col_elig_equi;
	private BigDecimal	r102_crm_sub_col_elig_unit_tru;
	private BigDecimal	r102_crm_sub_col_exp_cov;
	private BigDecimal	r102_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r102_crm_sub_rwa_ris_crm;
	private BigDecimal	r102_crm_sub_rwa_cov_crm;
	private BigDecimal	r102_crm_sub_rwa_org_cou;
	private BigDecimal	r102_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r102_crm_comp_col_expo_elig;
	private BigDecimal	r102_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r102_crm_comp_col_elig_fin_hai;
	private BigDecimal	r102_crm_comp_col_expo_val;
	private BigDecimal	r102_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r102_rwa_unsec_expo_cre_ris;
	private BigDecimal	r102_rwa_unsec_expo;
	private BigDecimal	r102_rwa_tot_ris_wei_ass;
	
	private String	r105_exposure_class;
	private BigDecimal	r105_expo_crm;
	private BigDecimal	r105_spe_pro_expo;
	private BigDecimal	r105_amt_elig_sht_net;
	private BigDecimal	r105_tot_expo_net_spe;
	private BigDecimal	r105_crm_sub_elig_sub_app;
	private BigDecimal	r105_crm_sub_non_col_guar;
	private BigDecimal	r105_crm_sub_non_col_cre_der;
	private BigDecimal	r105_crm_sub_col_elig_cash;
	private BigDecimal	r105_crm_sub_col_elig_trea_bills;
	private BigDecimal	r105_crm_sub_col_elig_deb_sec;
	private BigDecimal	r105_crm_sub_col_elig_equi;
	private BigDecimal	r105_crm_sub_col_elig_unit_tru;
	private BigDecimal	r105_crm_sub_col_exp_cov;
	private BigDecimal	r105_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r105_crm_sub_rwa_ris_crm;
	private BigDecimal	r105_crm_sub_rwa_cov_crm;
	private BigDecimal	r105_crm_sub_rwa_org_cou;
	private BigDecimal	r105_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r105_crm_comp_col_expo_elig;
	private BigDecimal	r105_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r105_crm_comp_col_elig_fin_hai;
	private BigDecimal	r105_crm_comp_col_expo_val;
	private BigDecimal	r105_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r105_rwa_unsec_expo_cre_ris;
	private BigDecimal	r105_rwa_unsec_expo;
	private BigDecimal	r105_rwa_tot_ris_wei_ass;
	
	private String	r106_exposure_class;
	private BigDecimal	r106_expo_crm;
	private BigDecimal	r106_spe_pro_expo;
	private BigDecimal	r106_amt_elig_sht_net;
	private BigDecimal	r106_tot_expo_net_spe;
	private BigDecimal	r106_crm_sub_elig_sub_app;
	private BigDecimal	r106_crm_sub_non_col_guar;
	private BigDecimal	r106_crm_sub_non_col_cre_der;
	private BigDecimal	r106_crm_sub_col_elig_cash;
	private BigDecimal	r106_crm_sub_col_elig_trea_bills;
	private BigDecimal	r106_crm_sub_col_elig_deb_sec;
	private BigDecimal	r106_crm_sub_col_elig_equi;
	private BigDecimal	r106_crm_sub_col_elig_unit_tru;
	private BigDecimal	r106_crm_sub_col_exp_cov;
	private BigDecimal	r106_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r106_crm_sub_rwa_ris_crm;
	private BigDecimal	r106_crm_sub_rwa_cov_crm;
	private BigDecimal	r106_crm_sub_rwa_org_cou;
	private BigDecimal	r106_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r106_crm_comp_col_expo_elig;
	private BigDecimal	r106_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r106_crm_comp_col_elig_fin_hai;
	private BigDecimal	r106_crm_comp_col_expo_val;
	private BigDecimal	r106_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r106_rwa_unsec_expo_cre_ris;
	private BigDecimal	r106_rwa_unsec_expo;
	private BigDecimal	r106_rwa_tot_ris_wei_ass;

	private String	r107_exposure_class;
	private BigDecimal	r107_expo_crm;
	private BigDecimal	r107_spe_pro_expo;
	private BigDecimal	r107_amt_elig_sht_net;
	private BigDecimal	r107_tot_expo_net_spe;
	private BigDecimal	r107_crm_sub_elig_sub_app;
	private BigDecimal	r107_crm_sub_non_col_guar;
	private BigDecimal	r107_crm_sub_non_col_cre_der;
	private BigDecimal	r107_crm_sub_col_elig_cash;
	private BigDecimal	r107_crm_sub_col_elig_trea_bills;
	private BigDecimal	r107_crm_sub_col_elig_deb_sec;
	private BigDecimal	r107_crm_sub_col_elig_equi;
	private BigDecimal	r107_crm_sub_col_elig_unit_tru;
	private BigDecimal	r107_crm_sub_col_exp_cov;
	private BigDecimal	r107_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r107_crm_sub_rwa_ris_crm;
	private BigDecimal	r107_crm_sub_rwa_cov_crm;
	private BigDecimal	r107_crm_sub_rwa_org_cou;
	private BigDecimal	r107_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r107_crm_comp_col_expo_elig;
	private BigDecimal	r107_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r107_crm_comp_col_elig_fin_hai;
	private BigDecimal	r107_crm_comp_col_expo_val;
	private BigDecimal	r107_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r107_rwa_unsec_expo_cre_ris;
	private BigDecimal	r107_rwa_unsec_expo;
	private BigDecimal	r107_rwa_tot_ris_wei_ass;
	
	private String	r108_exposure_class;
	private BigDecimal	r108_expo_crm;
	private BigDecimal	r108_spe_pro_expo;
	private BigDecimal	r108_amt_elig_sht_net;
	private BigDecimal	r108_tot_expo_net_spe;
	private BigDecimal	r108_crm_sub_elig_sub_app;
	private BigDecimal	r108_crm_sub_non_col_guar;
	private BigDecimal	r108_crm_sub_non_col_cre_der;
	private BigDecimal	r108_crm_sub_col_elig_cash;
	private BigDecimal	r108_crm_sub_col_elig_trea_bills;
	private BigDecimal	r108_crm_sub_col_elig_deb_sec;
	private BigDecimal	r108_crm_sub_col_elig_equi;
	private BigDecimal	r108_crm_sub_col_elig_unit_tru;
	private BigDecimal	r108_crm_sub_col_exp_cov;
	private BigDecimal	r108_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r108_crm_sub_rwa_ris_crm;
	private BigDecimal	r108_crm_sub_rwa_cov_crm;
	private BigDecimal	r108_crm_sub_rwa_org_cou;
	private BigDecimal	r108_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r108_crm_comp_col_expo_elig;
	private BigDecimal	r108_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r108_crm_comp_col_elig_fin_hai;
	private BigDecimal	r108_crm_comp_col_expo_val;
	private BigDecimal	r108_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r108_rwa_unsec_expo_cre_ris;
	private BigDecimal	r108_rwa_unsec_expo;
	private BigDecimal	r108_rwa_tot_ris_wei_ass;
	
	private String	r109_exposure_class;
	private BigDecimal	r109_expo_crm;
	private BigDecimal	r109_spe_pro_expo;
	private BigDecimal	r109_amt_elig_sht_net;
	private BigDecimal	r109_tot_expo_net_spe;
	private BigDecimal	r109_crm_sub_elig_sub_app;
	private BigDecimal	r109_crm_sub_non_col_guar;
	private BigDecimal	r109_crm_sub_non_col_cre_der;
	private BigDecimal	r109_crm_sub_col_elig_cash;
	private BigDecimal	r109_crm_sub_col_elig_trea_bills;
	private BigDecimal	r109_crm_sub_col_elig_deb_sec;
	private BigDecimal	r109_crm_sub_col_elig_equi;
	private BigDecimal	r109_crm_sub_col_elig_unit_tru;
	private BigDecimal	r109_crm_sub_col_exp_cov;
	private BigDecimal	r109_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r109_crm_sub_rwa_ris_crm;
	private BigDecimal	r109_crm_sub_rwa_cov_crm;
	private BigDecimal	r109_crm_sub_rwa_org_cou;
	private BigDecimal	r109_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r109_crm_comp_col_expo_elig;
	private BigDecimal	r109_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r109_crm_comp_col_elig_fin_hai;
	private BigDecimal	r109_crm_comp_col_expo_val;
	private BigDecimal	r109_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r109_rwa_unsec_expo_cre_ris;
	private BigDecimal	r109_rwa_unsec_expo;
	private BigDecimal	r109_rwa_tot_ris_wei_ass;
	
	private String	r110_exposure_class;
	private BigDecimal	r110_expo_crm;
	private BigDecimal	r110_spe_pro_expo;
	private BigDecimal	r110_amt_elig_sht_net;
	private BigDecimal	r110_tot_expo_net_spe;
	private BigDecimal	r110_crm_sub_elig_sub_app;
	private BigDecimal	r110_crm_sub_non_col_guar;
	private BigDecimal	r110_crm_sub_non_col_cre_der;
	private BigDecimal	r110_crm_sub_col_elig_cash;
	private BigDecimal	r110_crm_sub_col_elig_trea_bills;
	private BigDecimal	r110_crm_sub_col_elig_deb_sec;
	private BigDecimal	r110_crm_sub_col_elig_equi;
	private BigDecimal	r110_crm_sub_col_elig_unit_tru;
	private BigDecimal	r110_crm_sub_col_exp_cov;
	private BigDecimal	r110_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r110_crm_sub_rwa_ris_crm;
	private BigDecimal	r110_crm_sub_rwa_cov_crm;
	private BigDecimal	r110_crm_sub_rwa_org_cou;
	private BigDecimal	r110_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r110_crm_comp_col_expo_elig;
	private BigDecimal	r110_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r110_crm_comp_col_elig_fin_hai;
	private BigDecimal	r110_crm_comp_col_expo_val;
	private BigDecimal	r110_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r110_rwa_unsec_expo_cre_ris;
	private BigDecimal	r110_rwa_unsec_expo;
	private BigDecimal	r110_rwa_tot_ris_wei_ass;
	
	private String	r111_exposure_class;
	private BigDecimal	r111_expo_crm;
	private BigDecimal	r111_spe_pro_expo;
	private BigDecimal	r111_amt_elig_sht_net;
	private BigDecimal	r111_tot_expo_net_spe;
	private BigDecimal	r111_crm_sub_elig_sub_app;
	private BigDecimal	r111_crm_sub_non_col_guar;
	private BigDecimal	r111_crm_sub_non_col_cre_der;
	private BigDecimal	r111_crm_sub_col_elig_cash;
	private BigDecimal	r111_crm_sub_col_elig_trea_bills;
	private BigDecimal	r111_crm_sub_col_elig_deb_sec;
	private BigDecimal	r111_crm_sub_col_elig_equi;
	private BigDecimal	r111_crm_sub_col_elig_unit_tru;
	private BigDecimal	r111_crm_sub_col_exp_cov;
	private BigDecimal	r111_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r111_crm_sub_rwa_ris_crm;
	private BigDecimal	r111_crm_sub_rwa_cov_crm;
	private BigDecimal	r111_crm_sub_rwa_org_cou;
	private BigDecimal	r111_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r111_crm_comp_col_expo_elig;
	private BigDecimal	r111_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r111_crm_comp_col_elig_fin_hai;
	private BigDecimal	r111_crm_comp_col_expo_val;
	private BigDecimal	r111_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r111_rwa_unsec_expo_cre_ris;
	private BigDecimal	r111_rwa_unsec_expo;
	private BigDecimal	r111_rwa_tot_ris_wei_ass;
	
	private String	r112_exposure_class;
	private BigDecimal	r112_expo_crm;
	private BigDecimal	r112_spe_pro_expo;
	private BigDecimal	r112_amt_elig_sht_net;
	private BigDecimal	r112_tot_expo_net_spe;
	private BigDecimal	r112_crm_sub_elig_sub_app;
	private BigDecimal	r112_crm_sub_non_col_guar;
	private BigDecimal	r112_crm_sub_non_col_cre_der;
	private BigDecimal	r112_crm_sub_col_elig_cash;
	private BigDecimal	r112_crm_sub_col_elig_trea_bills;
	private BigDecimal	r112_crm_sub_col_elig_deb_sec;
	private BigDecimal	r112_crm_sub_col_elig_equi;
	private BigDecimal	r112_crm_sub_col_elig_unit_tru;
	private BigDecimal	r112_crm_sub_col_exp_cov;
	private BigDecimal	r112_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r112_crm_sub_rwa_ris_crm;
	private BigDecimal	r112_crm_sub_rwa_cov_crm;
	private BigDecimal	r112_crm_sub_rwa_org_cou;
	private BigDecimal	r112_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r112_crm_comp_col_expo_elig;
	private BigDecimal	r112_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r112_crm_comp_col_elig_fin_hai;
	private BigDecimal	r112_crm_comp_col_expo_val;
	private BigDecimal	r112_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r112_rwa_unsec_expo_cre_ris;
	private BigDecimal	r112_rwa_unsec_expo;
	private BigDecimal	r112_rwa_tot_ris_wei_ass;
	
	private String	r113_exposure_class;
	private BigDecimal	r113_expo_crm;
	private BigDecimal	r113_spe_pro_expo;
	private BigDecimal	r113_amt_elig_sht_net;
	private BigDecimal	r113_tot_expo_net_spe;
	private BigDecimal	r113_crm_sub_elig_sub_app;
	private BigDecimal	r113_crm_sub_non_col_guar;
	private BigDecimal	r113_crm_sub_non_col_cre_der;
	private BigDecimal	r113_crm_sub_col_elig_cash;
	private BigDecimal	r113_crm_sub_col_elig_trea_bills;
	private BigDecimal	r113_crm_sub_col_elig_deb_sec;
	private BigDecimal	r113_crm_sub_col_elig_equi;
	private BigDecimal	r113_crm_sub_col_elig_unit_tru;
	private BigDecimal	r113_crm_sub_col_exp_cov;
	private BigDecimal	r113_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r113_crm_sub_rwa_ris_crm;
	private BigDecimal	r113_crm_sub_rwa_cov_crm;
	private BigDecimal	r113_crm_sub_rwa_org_cou;
	private BigDecimal	r113_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r113_crm_comp_col_expo_elig;
	private BigDecimal	r113_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r113_crm_comp_col_elig_fin_hai;
	private BigDecimal	r113_crm_comp_col_expo_val;
	private BigDecimal	r113_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r113_rwa_unsec_expo_cre_ris;
	private BigDecimal	r113_rwa_unsec_expo;
	private BigDecimal	r113_rwa_tot_ris_wei_ass;
	
	private String	r114_exposure_class;
	private BigDecimal	r114_expo_crm;
	private BigDecimal	r114_spe_pro_expo;
	private BigDecimal	r114_amt_elig_sht_net;
	private BigDecimal	r114_tot_expo_net_spe;
	private BigDecimal	r114_crm_sub_elig_sub_app;
	private BigDecimal	r114_crm_sub_non_col_guar;
	private BigDecimal	r114_crm_sub_non_col_cre_der;
	private BigDecimal	r114_crm_sub_col_elig_cash;
	private BigDecimal	r114_crm_sub_col_elig_trea_bills;
	private BigDecimal	r114_crm_sub_col_elig_deb_sec;
	private BigDecimal	r114_crm_sub_col_elig_equi;
	private BigDecimal	r114_crm_sub_col_elig_unit_tru;
	private BigDecimal	r114_crm_sub_col_exp_cov;
	private BigDecimal	r114_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r114_crm_sub_rwa_ris_crm;
	private BigDecimal	r114_crm_sub_rwa_cov_crm;
	private BigDecimal	r114_crm_sub_rwa_org_cou;
	private BigDecimal	r114_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r114_crm_comp_col_expo_elig;
	private BigDecimal	r114_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r114_crm_comp_col_elig_fin_hai;
	private BigDecimal	r114_crm_comp_col_expo_val;
	private BigDecimal	r114_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r114_rwa_unsec_expo_cre_ris;
	private BigDecimal	r114_rwa_unsec_expo;
	private BigDecimal	r114_rwa_tot_ris_wei_ass;
	
	private String	r117_exposure_class;
//	private BigDecimal	r117_expo_crm;
	private BigDecimal	r117_spe_pro_expo;
	private BigDecimal	r117_amt_elig_sht_net;
	private BigDecimal	r117_tot_expo_net_spe;
	private BigDecimal	r117_crm_sub_elig_sub_app;
	private BigDecimal	r117_crm_sub_non_col_guar;
	private BigDecimal	r117_crm_sub_non_col_cre_der;
	private BigDecimal	r117_crm_sub_col_elig_cash;
	private BigDecimal	r117_crm_sub_col_elig_trea_bills;
	private BigDecimal	r117_crm_sub_col_elig_deb_sec;
	private BigDecimal	r117_crm_sub_col_elig_equi;
	private BigDecimal	r117_crm_sub_col_elig_unit_tru;
	private BigDecimal	r117_crm_sub_col_exp_cov;
	private BigDecimal	r117_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r117_crm_sub_rwa_ris_crm;
	private BigDecimal	r117_crm_sub_rwa_cov_crm;
	private BigDecimal	r117_crm_sub_rwa_org_cou;
	private BigDecimal	r117_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r117_crm_comp_col_expo_elig;
	private BigDecimal	r117_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r117_crm_comp_col_elig_fin_hai;
	private BigDecimal	r117_crm_comp_col_expo_val;
	private BigDecimal	r117_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r117_rwa_unsec_expo_cre_ris;
	private BigDecimal	r117_rwa_unsec_expo;
	private BigDecimal	r117_rwa_tot_ris_wei_ass;
	
	private String	r118_exposure_class;
	private BigDecimal	r118_expo_crm;
	private BigDecimal	r118_spe_pro_expo;
	private BigDecimal	r118_amt_elig_sht_net;
	private BigDecimal	r118_tot_expo_net_spe;
	private BigDecimal	r118_crm_sub_elig_sub_app;
	private BigDecimal	r118_crm_sub_non_col_guar;
	private BigDecimal	r118_crm_sub_non_col_cre_der;
	private BigDecimal	r118_crm_sub_col_elig_cash;
	private BigDecimal	r118_crm_sub_col_elig_trea_bills;
	private BigDecimal	r118_crm_sub_col_elig_deb_sec;
	private BigDecimal	r118_crm_sub_col_elig_equi;
	private BigDecimal	r118_crm_sub_col_elig_unit_tru;
	private BigDecimal	r118_crm_sub_col_exp_cov;
	private BigDecimal	r118_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r118_crm_sub_rwa_ris_crm;
	private BigDecimal	r118_crm_sub_rwa_cov_crm;
	private BigDecimal	r118_crm_sub_rwa_org_cou;
	private BigDecimal	r118_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r118_crm_comp_col_expo_elig;
	private BigDecimal	r118_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r118_crm_comp_col_elig_fin_hai;
	private BigDecimal	r118_crm_comp_col_expo_val;
	private BigDecimal	r118_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r118_rwa_unsec_expo_cre_ris;
	private BigDecimal	r118_rwa_unsec_expo;
	private BigDecimal	r118_rwa_tot_ris_wei_ass;
	
	private String	r119_exposure_class;
	private BigDecimal	r119_expo_crm;
	private BigDecimal	r119_spe_pro_expo;
	private BigDecimal	r119_amt_elig_sht_net;
	private BigDecimal	r119_tot_expo_net_spe;
	private BigDecimal	r119_crm_sub_elig_sub_app;
	private BigDecimal	r119_crm_sub_non_col_guar;
	private BigDecimal	r119_crm_sub_non_col_cre_der;
	private BigDecimal	r119_crm_sub_col_elig_cash;
	private BigDecimal	r119_crm_sub_col_elig_trea_bills;
	private BigDecimal	r119_crm_sub_col_elig_deb_sec;
	private BigDecimal	r119_crm_sub_col_elig_equi;
	private BigDecimal	r119_crm_sub_col_elig_unit_tru;
	private BigDecimal	r119_crm_sub_col_exp_cov;
	private BigDecimal	r119_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r119_crm_sub_rwa_ris_crm;
	private BigDecimal	r119_crm_sub_rwa_cov_crm;
	private BigDecimal	r119_crm_sub_rwa_org_cou;
	private BigDecimal	r119_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r119_crm_comp_col_expo_elig;
	private BigDecimal	r119_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r119_crm_comp_col_elig_fin_hai;
	private BigDecimal	r119_crm_comp_col_expo_val;
	private BigDecimal	r119_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r119_rwa_unsec_expo_cre_ris;
	private BigDecimal	r119_rwa_unsec_expo;
	private BigDecimal	r119_rwa_tot_ris_wei_ass;
	
	private String	r120_exposure_class;
	private BigDecimal	r120_expo_crm;
	private BigDecimal	r120_spe_pro_expo;
	private BigDecimal	r120_amt_elig_sht_net;
	private BigDecimal	r120_tot_expo_net_spe;
	private BigDecimal	r120_crm_sub_elig_sub_app;
	private BigDecimal	r120_crm_sub_non_col_guar;
	private BigDecimal	r120_crm_sub_non_col_cre_der;
	private BigDecimal	r120_crm_sub_col_elig_cash;
	private BigDecimal	r120_crm_sub_col_elig_trea_bills;
	private BigDecimal	r120_crm_sub_col_elig_deb_sec;
	private BigDecimal	r120_crm_sub_col_elig_equi;
	private BigDecimal	r120_crm_sub_col_elig_unit_tru;
	private BigDecimal	r120_crm_sub_col_exp_cov;
	private BigDecimal	r120_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r120_crm_sub_rwa_ris_crm;
	private BigDecimal	r120_crm_sub_rwa_cov_crm;
	private BigDecimal	r120_crm_sub_rwa_org_cou;
	private BigDecimal	r120_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r120_crm_comp_col_expo_elig;
	private BigDecimal	r120_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r120_crm_comp_col_elig_fin_hai;
	private BigDecimal	r120_crm_comp_col_expo_val;
	private BigDecimal	r120_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r120_rwa_unsec_expo_cre_ris;
	private BigDecimal	r120_rwa_unsec_expo;
	private BigDecimal	r120_rwa_tot_ris_wei_ass;
	
	private String	r121_exposure_class;
	private BigDecimal	r121_expo_crm;
	private BigDecimal	r121_spe_pro_expo;
	private BigDecimal	r121_amt_elig_sht_net;
	private BigDecimal	r121_tot_expo_net_spe;
	private BigDecimal	r121_crm_sub_elig_sub_app;
	private BigDecimal	r121_crm_sub_non_col_guar;
	private BigDecimal	r121_crm_sub_non_col_cre_der;
	private BigDecimal	r121_crm_sub_col_elig_cash;
	private BigDecimal	r121_crm_sub_col_elig_trea_bills;
	private BigDecimal	r121_crm_sub_col_elig_deb_sec;
	private BigDecimal	r121_crm_sub_col_elig_equi;
	private BigDecimal	r121_crm_sub_col_elig_unit_tru;
	private BigDecimal	r121_crm_sub_col_exp_cov;
	private BigDecimal	r121_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r121_crm_sub_rwa_ris_crm;
	private BigDecimal	r121_crm_sub_rwa_cov_crm;
	private BigDecimal	r121_crm_sub_rwa_org_cou;
	private BigDecimal	r121_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r121_crm_comp_col_expo_elig;
	private BigDecimal	r121_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r121_crm_comp_col_elig_fin_hai;
	private BigDecimal	r121_crm_comp_col_expo_val;
	private BigDecimal	r121_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r121_rwa_unsec_expo_cre_ris;
	private BigDecimal	r121_rwa_unsec_expo;
	private BigDecimal	r121_rwa_tot_ris_wei_ass;
	
	
	private String	r122_exposure_class;
	private BigDecimal	r122_expo_crm;
	private BigDecimal	r122_spe_pro_expo;
	private BigDecimal	r122_amt_elig_sht_net;
	private BigDecimal	r122_tot_expo_net_spe;
	private BigDecimal	r122_crm_sub_elig_sub_app;
	private BigDecimal	r122_crm_sub_non_col_guar;
	private BigDecimal	r122_crm_sub_non_col_cre_der;
	private BigDecimal	r122_crm_sub_col_elig_cash;
	private BigDecimal	r122_crm_sub_col_elig_trea_bills;
	private BigDecimal	r122_crm_sub_col_elig_deb_sec;
	private BigDecimal	r122_crm_sub_col_elig_equi;
	private BigDecimal	r122_crm_sub_col_elig_unit_tru;
	private BigDecimal	r122_crm_sub_col_exp_cov;
	private BigDecimal	r122_crm_sub_col_elig_exp_not_cov;
	private BigDecimal	r122_crm_sub_rwa_ris_crm;
	private BigDecimal	r122_crm_sub_rwa_cov_crm;
	private BigDecimal	r122_crm_sub_rwa_org_cou;
	private BigDecimal	r122_crm_sub_rwa_not_cov_crm;
	private BigDecimal	r122_crm_comp_col_expo_elig;
	private BigDecimal	r122_crm_comp_col_elig_expo_vol_adj;
	private BigDecimal	r122_crm_comp_col_elig_fin_hai;
	private BigDecimal	r122_crm_comp_col_expo_val;
	private BigDecimal	r122_rwa_elig_expo_not_cov_crm;
	private BigDecimal	r122_rwa_unsec_expo_cre_ris;
	private BigDecimal	r122_rwa_unsec_expo;
	private BigDecimal	r122_rwa_tot_ris_wei_ass;
	
	private String	r124_exposure_class;
//	private BigDecimal	r124_expo_crm;
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
//	private BigDecimal	r129_expo_crm;
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
//	private BigDecimal	r131_expo_crm;
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
	public String getR100_exposure_class() {
		return r100_exposure_class;
	}
	public void setR100_exposure_class(String r100_exposure_class) {
		this.r100_exposure_class = r100_exposure_class;
	}
	public BigDecimal getR100_expo_crm() {
		return r100_expo_crm;
	}
	public void setR100_expo_crm(BigDecimal r100_expo_crm) {
		this.r100_expo_crm = r100_expo_crm;
	}
	public BigDecimal getR100_spe_pro_expo() {
		return r100_spe_pro_expo;
	}
	public void setR100_spe_pro_expo(BigDecimal r100_spe_pro_expo) {
		this.r100_spe_pro_expo = r100_spe_pro_expo;
	}
	public BigDecimal getR100_amt_elig_sht_net() {
		return r100_amt_elig_sht_net;
	}
	public void setR100_amt_elig_sht_net(BigDecimal r100_amt_elig_sht_net) {
		this.r100_amt_elig_sht_net = r100_amt_elig_sht_net;
	}
	public BigDecimal getR100_tot_expo_net_spe() {
		return r100_tot_expo_net_spe;
	}
	public void setR100_tot_expo_net_spe(BigDecimal r100_tot_expo_net_spe) {
		this.r100_tot_expo_net_spe = r100_tot_expo_net_spe;
	}
	public BigDecimal getR100_crm_sub_elig_sub_app() {
		return r100_crm_sub_elig_sub_app;
	}
	public void setR100_crm_sub_elig_sub_app(BigDecimal r100_crm_sub_elig_sub_app) {
		this.r100_crm_sub_elig_sub_app = r100_crm_sub_elig_sub_app;
	}
	public BigDecimal getR100_crm_sub_non_col_guar() {
		return r100_crm_sub_non_col_guar;
	}
	public void setR100_crm_sub_non_col_guar(BigDecimal r100_crm_sub_non_col_guar) {
		this.r100_crm_sub_non_col_guar = r100_crm_sub_non_col_guar;
	}
	public BigDecimal getR100_crm_sub_non_col_cre_der() {
		return r100_crm_sub_non_col_cre_der;
	}
	public void setR100_crm_sub_non_col_cre_der(BigDecimal r100_crm_sub_non_col_cre_der) {
		this.r100_crm_sub_non_col_cre_der = r100_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR100_crm_sub_col_elig_cash() {
		return r100_crm_sub_col_elig_cash;
	}
	public void setR100_crm_sub_col_elig_cash(BigDecimal r100_crm_sub_col_elig_cash) {
		this.r100_crm_sub_col_elig_cash = r100_crm_sub_col_elig_cash;
	}
	public BigDecimal getR100_crm_sub_col_elig_trea_bills() {
		return r100_crm_sub_col_elig_trea_bills;
	}
	public void setR100_crm_sub_col_elig_trea_bills(BigDecimal r100_crm_sub_col_elig_trea_bills) {
		this.r100_crm_sub_col_elig_trea_bills = r100_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR100_crm_sub_col_elig_deb_sec() {
		return r100_crm_sub_col_elig_deb_sec;
	}
	public void setR100_crm_sub_col_elig_deb_sec(BigDecimal r100_crm_sub_col_elig_deb_sec) {
		this.r100_crm_sub_col_elig_deb_sec = r100_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR100_crm_sub_col_elig_equi() {
		return r100_crm_sub_col_elig_equi;
	}
	public void setR100_crm_sub_col_elig_equi(BigDecimal r100_crm_sub_col_elig_equi) {
		this.r100_crm_sub_col_elig_equi = r100_crm_sub_col_elig_equi;
	}
	public BigDecimal getR100_crm_sub_col_elig_unit_tru() {
		return r100_crm_sub_col_elig_unit_tru;
	}
	public void setR100_crm_sub_col_elig_unit_tru(BigDecimal r100_crm_sub_col_elig_unit_tru) {
		this.r100_crm_sub_col_elig_unit_tru = r100_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR100_crm_sub_col_exp_cov() {
		return r100_crm_sub_col_exp_cov;
	}
	public void setR100_crm_sub_col_exp_cov(BigDecimal r100_crm_sub_col_exp_cov) {
		this.r100_crm_sub_col_exp_cov = r100_crm_sub_col_exp_cov;
	}
	public BigDecimal getR100_crm_sub_col_elig_exp_not_cov() {
		return r100_crm_sub_col_elig_exp_not_cov;
	}
	public void setR100_crm_sub_col_elig_exp_not_cov(BigDecimal r100_crm_sub_col_elig_exp_not_cov) {
		this.r100_crm_sub_col_elig_exp_not_cov = r100_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR100_crm_sub_rwa_ris_crm() {
		return r100_crm_sub_rwa_ris_crm;
	}
	public void setR100_crm_sub_rwa_ris_crm(BigDecimal r100_crm_sub_rwa_ris_crm) {
		this.r100_crm_sub_rwa_ris_crm = r100_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR100_crm_sub_rwa_cov_crm() {
		return r100_crm_sub_rwa_cov_crm;
	}
	public void setR100_crm_sub_rwa_cov_crm(BigDecimal r100_crm_sub_rwa_cov_crm) {
		this.r100_crm_sub_rwa_cov_crm = r100_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR100_crm_sub_rwa_org_cou() {
		return r100_crm_sub_rwa_org_cou;
	}
	public void setR100_crm_sub_rwa_org_cou(BigDecimal r100_crm_sub_rwa_org_cou) {
		this.r100_crm_sub_rwa_org_cou = r100_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR100_crm_sub_rwa_not_cov_crm() {
		return r100_crm_sub_rwa_not_cov_crm;
	}
	public void setR100_crm_sub_rwa_not_cov_crm(BigDecimal r100_crm_sub_rwa_not_cov_crm) {
		this.r100_crm_sub_rwa_not_cov_crm = r100_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR100_crm_comp_col_expo_elig() {
		return r100_crm_comp_col_expo_elig;
	}
	public void setR100_crm_comp_col_expo_elig(BigDecimal r100_crm_comp_col_expo_elig) {
		this.r100_crm_comp_col_expo_elig = r100_crm_comp_col_expo_elig;
	}
	public BigDecimal getR100_crm_comp_col_elig_expo_vol_adj() {
		return r100_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR100_crm_comp_col_elig_expo_vol_adj(BigDecimal r100_crm_comp_col_elig_expo_vol_adj) {
		this.r100_crm_comp_col_elig_expo_vol_adj = r100_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR100_crm_comp_col_elig_fin_hai() {
		return r100_crm_comp_col_elig_fin_hai;
	}
	public void setR100_crm_comp_col_elig_fin_hai(BigDecimal r100_crm_comp_col_elig_fin_hai) {
		this.r100_crm_comp_col_elig_fin_hai = r100_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR100_crm_comp_col_expo_val() {
		return r100_crm_comp_col_expo_val;
	}
	public void setR100_crm_comp_col_expo_val(BigDecimal r100_crm_comp_col_expo_val) {
		this.r100_crm_comp_col_expo_val = r100_crm_comp_col_expo_val;
	}
	public BigDecimal getR100_rwa_elig_expo_not_cov_crm() {
		return r100_rwa_elig_expo_not_cov_crm;
	}
	public void setR100_rwa_elig_expo_not_cov_crm(BigDecimal r100_rwa_elig_expo_not_cov_crm) {
		this.r100_rwa_elig_expo_not_cov_crm = r100_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR100_rwa_unsec_expo_cre_ris() {
		return r100_rwa_unsec_expo_cre_ris;
	}
	public void setR100_rwa_unsec_expo_cre_ris(BigDecimal r100_rwa_unsec_expo_cre_ris) {
		this.r100_rwa_unsec_expo_cre_ris = r100_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR100_rwa_unsec_expo() {
		return r100_rwa_unsec_expo;
	}
	public void setR100_rwa_unsec_expo(BigDecimal r100_rwa_unsec_expo) {
		this.r100_rwa_unsec_expo = r100_rwa_unsec_expo;
	}
	public BigDecimal getR100_rwa_tot_ris_wei_ass() {
		return r100_rwa_tot_ris_wei_ass;
	}
	public void setR100_rwa_tot_ris_wei_ass(BigDecimal r100_rwa_tot_ris_wei_ass) {
		this.r100_rwa_tot_ris_wei_ass = r100_rwa_tot_ris_wei_ass;
	}
	public String getR101_exposure_class() {
		return r101_exposure_class;
	}
	public void setR101_exposure_class(String r101_exposure_class) {
		this.r101_exposure_class = r101_exposure_class;
	}
	public BigDecimal getR101_expo_crm() {
		return r101_expo_crm;
	}
	public void setR101_expo_crm(BigDecimal r101_expo_crm) {
		this.r101_expo_crm = r101_expo_crm;
	}
	public BigDecimal getR101_spe_pro_expo() {
		return r101_spe_pro_expo;
	}
	public void setR101_spe_pro_expo(BigDecimal r101_spe_pro_expo) {
		this.r101_spe_pro_expo = r101_spe_pro_expo;
	}
	public BigDecimal getR101_amt_elig_sht_net() {
		return r101_amt_elig_sht_net;
	}
	public void setR101_amt_elig_sht_net(BigDecimal r101_amt_elig_sht_net) {
		this.r101_amt_elig_sht_net = r101_amt_elig_sht_net;
	}
	public BigDecimal getR101_tot_expo_net_spe() {
		return r101_tot_expo_net_spe;
	}
	public void setR101_tot_expo_net_spe(BigDecimal r101_tot_expo_net_spe) {
		this.r101_tot_expo_net_spe = r101_tot_expo_net_spe;
	}
	public BigDecimal getR101_crm_sub_elig_sub_app() {
		return r101_crm_sub_elig_sub_app;
	}
	public void setR101_crm_sub_elig_sub_app(BigDecimal r101_crm_sub_elig_sub_app) {
		this.r101_crm_sub_elig_sub_app = r101_crm_sub_elig_sub_app;
	}
	public BigDecimal getR101_crm_sub_non_col_guar() {
		return r101_crm_sub_non_col_guar;
	}
	public void setR101_crm_sub_non_col_guar(BigDecimal r101_crm_sub_non_col_guar) {
		this.r101_crm_sub_non_col_guar = r101_crm_sub_non_col_guar;
	}
	public BigDecimal getR101_crm_sub_non_col_cre_der() {
		return r101_crm_sub_non_col_cre_der;
	}
	public void setR101_crm_sub_non_col_cre_der(BigDecimal r101_crm_sub_non_col_cre_der) {
		this.r101_crm_sub_non_col_cre_der = r101_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR101_crm_sub_col_elig_cash() {
		return r101_crm_sub_col_elig_cash;
	}
	public void setR101_crm_sub_col_elig_cash(BigDecimal r101_crm_sub_col_elig_cash) {
		this.r101_crm_sub_col_elig_cash = r101_crm_sub_col_elig_cash;
	}
	public BigDecimal getR101_crm_sub_col_elig_trea_bills() {
		return r101_crm_sub_col_elig_trea_bills;
	}
	public void setR101_crm_sub_col_elig_trea_bills(BigDecimal r101_crm_sub_col_elig_trea_bills) {
		this.r101_crm_sub_col_elig_trea_bills = r101_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR101_crm_sub_col_elig_deb_sec() {
		return r101_crm_sub_col_elig_deb_sec;
	}
	public void setR101_crm_sub_col_elig_deb_sec(BigDecimal r101_crm_sub_col_elig_deb_sec) {
		this.r101_crm_sub_col_elig_deb_sec = r101_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR101_crm_sub_col_elig_equi() {
		return r101_crm_sub_col_elig_equi;
	}
	public void setR101_crm_sub_col_elig_equi(BigDecimal r101_crm_sub_col_elig_equi) {
		this.r101_crm_sub_col_elig_equi = r101_crm_sub_col_elig_equi;
	}
	public BigDecimal getR101_crm_sub_col_elig_unit_tru() {
		return r101_crm_sub_col_elig_unit_tru;
	}
	public void setR101_crm_sub_col_elig_unit_tru(BigDecimal r101_crm_sub_col_elig_unit_tru) {
		this.r101_crm_sub_col_elig_unit_tru = r101_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR101_crm_sub_col_exp_cov() {
		return r101_crm_sub_col_exp_cov;
	}
	public void setR101_crm_sub_col_exp_cov(BigDecimal r101_crm_sub_col_exp_cov) {
		this.r101_crm_sub_col_exp_cov = r101_crm_sub_col_exp_cov;
	}
	public BigDecimal getR101_crm_sub_col_elig_exp_not_cov() {
		return r101_crm_sub_col_elig_exp_not_cov;
	}
	public void setR101_crm_sub_col_elig_exp_not_cov(BigDecimal r101_crm_sub_col_elig_exp_not_cov) {
		this.r101_crm_sub_col_elig_exp_not_cov = r101_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR101_crm_sub_rwa_ris_crm() {
		return r101_crm_sub_rwa_ris_crm;
	}
	public void setR101_crm_sub_rwa_ris_crm(BigDecimal r101_crm_sub_rwa_ris_crm) {
		this.r101_crm_sub_rwa_ris_crm = r101_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR101_crm_sub_rwa_cov_crm() {
		return r101_crm_sub_rwa_cov_crm;
	}
	public void setR101_crm_sub_rwa_cov_crm(BigDecimal r101_crm_sub_rwa_cov_crm) {
		this.r101_crm_sub_rwa_cov_crm = r101_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR101_crm_sub_rwa_org_cou() {
		return r101_crm_sub_rwa_org_cou;
	}
	public void setR101_crm_sub_rwa_org_cou(BigDecimal r101_crm_sub_rwa_org_cou) {
		this.r101_crm_sub_rwa_org_cou = r101_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR101_crm_sub_rwa_not_cov_crm() {
		return r101_crm_sub_rwa_not_cov_crm;
	}
	public void setR101_crm_sub_rwa_not_cov_crm(BigDecimal r101_crm_sub_rwa_not_cov_crm) {
		this.r101_crm_sub_rwa_not_cov_crm = r101_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR101_crm_comp_col_expo_elig() {
		return r101_crm_comp_col_expo_elig;
	}
	public void setR101_crm_comp_col_expo_elig(BigDecimal r101_crm_comp_col_expo_elig) {
		this.r101_crm_comp_col_expo_elig = r101_crm_comp_col_expo_elig;
	}
	public BigDecimal getR101_crm_comp_col_elig_expo_vol_adj() {
		return r101_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR101_crm_comp_col_elig_expo_vol_adj(BigDecimal r101_crm_comp_col_elig_expo_vol_adj) {
		this.r101_crm_comp_col_elig_expo_vol_adj = r101_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR101_crm_comp_col_elig_fin_hai() {
		return r101_crm_comp_col_elig_fin_hai;
	}
	public void setR101_crm_comp_col_elig_fin_hai(BigDecimal r101_crm_comp_col_elig_fin_hai) {
		this.r101_crm_comp_col_elig_fin_hai = r101_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR101_crm_comp_col_expo_val() {
		return r101_crm_comp_col_expo_val;
	}
	public void setR101_crm_comp_col_expo_val(BigDecimal r101_crm_comp_col_expo_val) {
		this.r101_crm_comp_col_expo_val = r101_crm_comp_col_expo_val;
	}
	public BigDecimal getR101_rwa_elig_expo_not_cov_crm() {
		return r101_rwa_elig_expo_not_cov_crm;
	}
	public void setR101_rwa_elig_expo_not_cov_crm(BigDecimal r101_rwa_elig_expo_not_cov_crm) {
		this.r101_rwa_elig_expo_not_cov_crm = r101_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR101_rwa_unsec_expo_cre_ris() {
		return r101_rwa_unsec_expo_cre_ris;
	}
	public void setR101_rwa_unsec_expo_cre_ris(BigDecimal r101_rwa_unsec_expo_cre_ris) {
		this.r101_rwa_unsec_expo_cre_ris = r101_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR101_rwa_unsec_expo() {
		return r101_rwa_unsec_expo;
	}
	public void setR101_rwa_unsec_expo(BigDecimal r101_rwa_unsec_expo) {
		this.r101_rwa_unsec_expo = r101_rwa_unsec_expo;
	}
	public BigDecimal getR101_rwa_tot_ris_wei_ass() {
		return r101_rwa_tot_ris_wei_ass;
	}
	public void setR101_rwa_tot_ris_wei_ass(BigDecimal r101_rwa_tot_ris_wei_ass) {
		this.r101_rwa_tot_ris_wei_ass = r101_rwa_tot_ris_wei_ass;
	}
	public String getR102_exposure_class() {
		return r102_exposure_class;
	}
	public void setR102_exposure_class(String r102_exposure_class) {
		this.r102_exposure_class = r102_exposure_class;
	}
	public BigDecimal getR102_expo_crm() {
		return r102_expo_crm;
	}
	public void setR102_expo_crm(BigDecimal r102_expo_crm) {
		this.r102_expo_crm = r102_expo_crm;
	}
	public BigDecimal getR102_spe_pro_expo() {
		return r102_spe_pro_expo;
	}
	public void setR102_spe_pro_expo(BigDecimal r102_spe_pro_expo) {
		this.r102_spe_pro_expo = r102_spe_pro_expo;
	}
	public BigDecimal getR102_amt_elig_sht_net() {
		return r102_amt_elig_sht_net;
	}
	public void setR102_amt_elig_sht_net(BigDecimal r102_amt_elig_sht_net) {
		this.r102_amt_elig_sht_net = r102_amt_elig_sht_net;
	}
	public BigDecimal getR102_tot_expo_net_spe() {
		return r102_tot_expo_net_spe;
	}
	public void setR102_tot_expo_net_spe(BigDecimal r102_tot_expo_net_spe) {
		this.r102_tot_expo_net_spe = r102_tot_expo_net_spe;
	}
	public BigDecimal getR102_crm_sub_elig_sub_app() {
		return r102_crm_sub_elig_sub_app;
	}
	public void setR102_crm_sub_elig_sub_app(BigDecimal r102_crm_sub_elig_sub_app) {
		this.r102_crm_sub_elig_sub_app = r102_crm_sub_elig_sub_app;
	}
	public BigDecimal getR102_crm_sub_non_col_guar() {
		return r102_crm_sub_non_col_guar;
	}
	public void setR102_crm_sub_non_col_guar(BigDecimal r102_crm_sub_non_col_guar) {
		this.r102_crm_sub_non_col_guar = r102_crm_sub_non_col_guar;
	}
	public BigDecimal getR102_crm_sub_non_col_cre_der() {
		return r102_crm_sub_non_col_cre_der;
	}
	public void setR102_crm_sub_non_col_cre_der(BigDecimal r102_crm_sub_non_col_cre_der) {
		this.r102_crm_sub_non_col_cre_der = r102_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR102_crm_sub_col_elig_cash() {
		return r102_crm_sub_col_elig_cash;
	}
	public void setR102_crm_sub_col_elig_cash(BigDecimal r102_crm_sub_col_elig_cash) {
		this.r102_crm_sub_col_elig_cash = r102_crm_sub_col_elig_cash;
	}
	public BigDecimal getR102_crm_sub_col_elig_trea_bills() {
		return r102_crm_sub_col_elig_trea_bills;
	}
	public void setR102_crm_sub_col_elig_trea_bills(BigDecimal r102_crm_sub_col_elig_trea_bills) {
		this.r102_crm_sub_col_elig_trea_bills = r102_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR102_crm_sub_col_elig_deb_sec() {
		return r102_crm_sub_col_elig_deb_sec;
	}
	public void setR102_crm_sub_col_elig_deb_sec(BigDecimal r102_crm_sub_col_elig_deb_sec) {
		this.r102_crm_sub_col_elig_deb_sec = r102_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR102_crm_sub_col_elig_equi() {
		return r102_crm_sub_col_elig_equi;
	}
	public void setR102_crm_sub_col_elig_equi(BigDecimal r102_crm_sub_col_elig_equi) {
		this.r102_crm_sub_col_elig_equi = r102_crm_sub_col_elig_equi;
	}
	public BigDecimal getR102_crm_sub_col_elig_unit_tru() {
		return r102_crm_sub_col_elig_unit_tru;
	}
	public void setR102_crm_sub_col_elig_unit_tru(BigDecimal r102_crm_sub_col_elig_unit_tru) {
		this.r102_crm_sub_col_elig_unit_tru = r102_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR102_crm_sub_col_exp_cov() {
		return r102_crm_sub_col_exp_cov;
	}
	public void setR102_crm_sub_col_exp_cov(BigDecimal r102_crm_sub_col_exp_cov) {
		this.r102_crm_sub_col_exp_cov = r102_crm_sub_col_exp_cov;
	}
	public BigDecimal getR102_crm_sub_col_elig_exp_not_cov() {
		return r102_crm_sub_col_elig_exp_not_cov;
	}
	public void setR102_crm_sub_col_elig_exp_not_cov(BigDecimal r102_crm_sub_col_elig_exp_not_cov) {
		this.r102_crm_sub_col_elig_exp_not_cov = r102_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR102_crm_sub_rwa_ris_crm() {
		return r102_crm_sub_rwa_ris_crm;
	}
	public void setR102_crm_sub_rwa_ris_crm(BigDecimal r102_crm_sub_rwa_ris_crm) {
		this.r102_crm_sub_rwa_ris_crm = r102_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR102_crm_sub_rwa_cov_crm() {
		return r102_crm_sub_rwa_cov_crm;
	}
	public void setR102_crm_sub_rwa_cov_crm(BigDecimal r102_crm_sub_rwa_cov_crm) {
		this.r102_crm_sub_rwa_cov_crm = r102_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR102_crm_sub_rwa_org_cou() {
		return r102_crm_sub_rwa_org_cou;
	}
	public void setR102_crm_sub_rwa_org_cou(BigDecimal r102_crm_sub_rwa_org_cou) {
		this.r102_crm_sub_rwa_org_cou = r102_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR102_crm_sub_rwa_not_cov_crm() {
		return r102_crm_sub_rwa_not_cov_crm;
	}
	public void setR102_crm_sub_rwa_not_cov_crm(BigDecimal r102_crm_sub_rwa_not_cov_crm) {
		this.r102_crm_sub_rwa_not_cov_crm = r102_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR102_crm_comp_col_expo_elig() {
		return r102_crm_comp_col_expo_elig;
	}
	public void setR102_crm_comp_col_expo_elig(BigDecimal r102_crm_comp_col_expo_elig) {
		this.r102_crm_comp_col_expo_elig = r102_crm_comp_col_expo_elig;
	}
	public BigDecimal getR102_crm_comp_col_elig_expo_vol_adj() {
		return r102_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR102_crm_comp_col_elig_expo_vol_adj(BigDecimal r102_crm_comp_col_elig_expo_vol_adj) {
		this.r102_crm_comp_col_elig_expo_vol_adj = r102_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR102_crm_comp_col_elig_fin_hai() {
		return r102_crm_comp_col_elig_fin_hai;
	}
	public void setR102_crm_comp_col_elig_fin_hai(BigDecimal r102_crm_comp_col_elig_fin_hai) {
		this.r102_crm_comp_col_elig_fin_hai = r102_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR102_crm_comp_col_expo_val() {
		return r102_crm_comp_col_expo_val;
	}
	public void setR102_crm_comp_col_expo_val(BigDecimal r102_crm_comp_col_expo_val) {
		this.r102_crm_comp_col_expo_val = r102_crm_comp_col_expo_val;
	}
	public BigDecimal getR102_rwa_elig_expo_not_cov_crm() {
		return r102_rwa_elig_expo_not_cov_crm;
	}
	public void setR102_rwa_elig_expo_not_cov_crm(BigDecimal r102_rwa_elig_expo_not_cov_crm) {
		this.r102_rwa_elig_expo_not_cov_crm = r102_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR102_rwa_unsec_expo_cre_ris() {
		return r102_rwa_unsec_expo_cre_ris;
	}
	public void setR102_rwa_unsec_expo_cre_ris(BigDecimal r102_rwa_unsec_expo_cre_ris) {
		this.r102_rwa_unsec_expo_cre_ris = r102_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR102_rwa_unsec_expo() {
		return r102_rwa_unsec_expo;
	}
	public void setR102_rwa_unsec_expo(BigDecimal r102_rwa_unsec_expo) {
		this.r102_rwa_unsec_expo = r102_rwa_unsec_expo;
	}
	public BigDecimal getR102_rwa_tot_ris_wei_ass() {
		return r102_rwa_tot_ris_wei_ass;
	}
	public void setR102_rwa_tot_ris_wei_ass(BigDecimal r102_rwa_tot_ris_wei_ass) {
		this.r102_rwa_tot_ris_wei_ass = r102_rwa_tot_ris_wei_ass;
	}
	public String getR105_exposure_class() {
		return r105_exposure_class;
	}
	public void setR105_exposure_class(String r105_exposure_class) {
		this.r105_exposure_class = r105_exposure_class;
	}
	public BigDecimal getR105_expo_crm() {
		return r105_expo_crm;
	}
	public void setR105_expo_crm(BigDecimal r105_expo_crm) {
		this.r105_expo_crm = r105_expo_crm;
	}
	public BigDecimal getR105_spe_pro_expo() {
		return r105_spe_pro_expo;
	}
	public void setR105_spe_pro_expo(BigDecimal r105_spe_pro_expo) {
		this.r105_spe_pro_expo = r105_spe_pro_expo;
	}
	public BigDecimal getR105_amt_elig_sht_net() {
		return r105_amt_elig_sht_net;
	}
	public void setR105_amt_elig_sht_net(BigDecimal r105_amt_elig_sht_net) {
		this.r105_amt_elig_sht_net = r105_amt_elig_sht_net;
	}
	public BigDecimal getR105_tot_expo_net_spe() {
		return r105_tot_expo_net_spe;
	}
	public void setR105_tot_expo_net_spe(BigDecimal r105_tot_expo_net_spe) {
		this.r105_tot_expo_net_spe = r105_tot_expo_net_spe;
	}
	public BigDecimal getR105_crm_sub_elig_sub_app() {
		return r105_crm_sub_elig_sub_app;
	}
	public void setR105_crm_sub_elig_sub_app(BigDecimal r105_crm_sub_elig_sub_app) {
		this.r105_crm_sub_elig_sub_app = r105_crm_sub_elig_sub_app;
	}
	public BigDecimal getR105_crm_sub_non_col_guar() {
		return r105_crm_sub_non_col_guar;
	}
	public void setR105_crm_sub_non_col_guar(BigDecimal r105_crm_sub_non_col_guar) {
		this.r105_crm_sub_non_col_guar = r105_crm_sub_non_col_guar;
	}
	public BigDecimal getR105_crm_sub_non_col_cre_der() {
		return r105_crm_sub_non_col_cre_der;
	}
	public void setR105_crm_sub_non_col_cre_der(BigDecimal r105_crm_sub_non_col_cre_der) {
		this.r105_crm_sub_non_col_cre_der = r105_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR105_crm_sub_col_elig_cash() {
		return r105_crm_sub_col_elig_cash;
	}
	public void setR105_crm_sub_col_elig_cash(BigDecimal r105_crm_sub_col_elig_cash) {
		this.r105_crm_sub_col_elig_cash = r105_crm_sub_col_elig_cash;
	}
	public BigDecimal getR105_crm_sub_col_elig_trea_bills() {
		return r105_crm_sub_col_elig_trea_bills;
	}
	public void setR105_crm_sub_col_elig_trea_bills(BigDecimal r105_crm_sub_col_elig_trea_bills) {
		this.r105_crm_sub_col_elig_trea_bills = r105_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR105_crm_sub_col_elig_deb_sec() {
		return r105_crm_sub_col_elig_deb_sec;
	}
	public void setR105_crm_sub_col_elig_deb_sec(BigDecimal r105_crm_sub_col_elig_deb_sec) {
		this.r105_crm_sub_col_elig_deb_sec = r105_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR105_crm_sub_col_elig_equi() {
		return r105_crm_sub_col_elig_equi;
	}
	public void setR105_crm_sub_col_elig_equi(BigDecimal r105_crm_sub_col_elig_equi) {
		this.r105_crm_sub_col_elig_equi = r105_crm_sub_col_elig_equi;
	}
	public BigDecimal getR105_crm_sub_col_elig_unit_tru() {
		return r105_crm_sub_col_elig_unit_tru;
	}
	public void setR105_crm_sub_col_elig_unit_tru(BigDecimal r105_crm_sub_col_elig_unit_tru) {
		this.r105_crm_sub_col_elig_unit_tru = r105_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR105_crm_sub_col_exp_cov() {
		return r105_crm_sub_col_exp_cov;
	}
	public void setR105_crm_sub_col_exp_cov(BigDecimal r105_crm_sub_col_exp_cov) {
		this.r105_crm_sub_col_exp_cov = r105_crm_sub_col_exp_cov;
	}
	public BigDecimal getR105_crm_sub_col_elig_exp_not_cov() {
		return r105_crm_sub_col_elig_exp_not_cov;
	}
	public void setR105_crm_sub_col_elig_exp_not_cov(BigDecimal r105_crm_sub_col_elig_exp_not_cov) {
		this.r105_crm_sub_col_elig_exp_not_cov = r105_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR105_crm_sub_rwa_ris_crm() {
		return r105_crm_sub_rwa_ris_crm;
	}
	public void setR105_crm_sub_rwa_ris_crm(BigDecimal r105_crm_sub_rwa_ris_crm) {
		this.r105_crm_sub_rwa_ris_crm = r105_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR105_crm_sub_rwa_cov_crm() {
		return r105_crm_sub_rwa_cov_crm;
	}
	public void setR105_crm_sub_rwa_cov_crm(BigDecimal r105_crm_sub_rwa_cov_crm) {
		this.r105_crm_sub_rwa_cov_crm = r105_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR105_crm_sub_rwa_org_cou() {
		return r105_crm_sub_rwa_org_cou;
	}
	public void setR105_crm_sub_rwa_org_cou(BigDecimal r105_crm_sub_rwa_org_cou) {
		this.r105_crm_sub_rwa_org_cou = r105_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR105_crm_sub_rwa_not_cov_crm() {
		return r105_crm_sub_rwa_not_cov_crm;
	}
	public void setR105_crm_sub_rwa_not_cov_crm(BigDecimal r105_crm_sub_rwa_not_cov_crm) {
		this.r105_crm_sub_rwa_not_cov_crm = r105_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR105_crm_comp_col_expo_elig() {
		return r105_crm_comp_col_expo_elig;
	}
	public void setR105_crm_comp_col_expo_elig(BigDecimal r105_crm_comp_col_expo_elig) {
		this.r105_crm_comp_col_expo_elig = r105_crm_comp_col_expo_elig;
	}
	public BigDecimal getR105_crm_comp_col_elig_expo_vol_adj() {
		return r105_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR105_crm_comp_col_elig_expo_vol_adj(BigDecimal r105_crm_comp_col_elig_expo_vol_adj) {
		this.r105_crm_comp_col_elig_expo_vol_adj = r105_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR105_crm_comp_col_elig_fin_hai() {
		return r105_crm_comp_col_elig_fin_hai;
	}
	public void setR105_crm_comp_col_elig_fin_hai(BigDecimal r105_crm_comp_col_elig_fin_hai) {
		this.r105_crm_comp_col_elig_fin_hai = r105_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR105_crm_comp_col_expo_val() {
		return r105_crm_comp_col_expo_val;
	}
	public void setR105_crm_comp_col_expo_val(BigDecimal r105_crm_comp_col_expo_val) {
		this.r105_crm_comp_col_expo_val = r105_crm_comp_col_expo_val;
	}
	public BigDecimal getR105_rwa_elig_expo_not_cov_crm() {
		return r105_rwa_elig_expo_not_cov_crm;
	}
	public void setR105_rwa_elig_expo_not_cov_crm(BigDecimal r105_rwa_elig_expo_not_cov_crm) {
		this.r105_rwa_elig_expo_not_cov_crm = r105_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR105_rwa_unsec_expo_cre_ris() {
		return r105_rwa_unsec_expo_cre_ris;
	}
	public void setR105_rwa_unsec_expo_cre_ris(BigDecimal r105_rwa_unsec_expo_cre_ris) {
		this.r105_rwa_unsec_expo_cre_ris = r105_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR105_rwa_unsec_expo() {
		return r105_rwa_unsec_expo;
	}
	public void setR105_rwa_unsec_expo(BigDecimal r105_rwa_unsec_expo) {
		this.r105_rwa_unsec_expo = r105_rwa_unsec_expo;
	}
	public BigDecimal getR105_rwa_tot_ris_wei_ass() {
		return r105_rwa_tot_ris_wei_ass;
	}
	public void setR105_rwa_tot_ris_wei_ass(BigDecimal r105_rwa_tot_ris_wei_ass) {
		this.r105_rwa_tot_ris_wei_ass = r105_rwa_tot_ris_wei_ass;
	}
	public String getR106_exposure_class() {
		return r106_exposure_class;
	}
	public void setR106_exposure_class(String r106_exposure_class) {
		this.r106_exposure_class = r106_exposure_class;
	}
	public BigDecimal getR106_expo_crm() {
		return r106_expo_crm;
	}
	public void setR106_expo_crm(BigDecimal r106_expo_crm) {
		this.r106_expo_crm = r106_expo_crm;
	}
	public BigDecimal getR106_spe_pro_expo() {
		return r106_spe_pro_expo;
	}
	public void setR106_spe_pro_expo(BigDecimal r106_spe_pro_expo) {
		this.r106_spe_pro_expo = r106_spe_pro_expo;
	}
	public BigDecimal getR106_amt_elig_sht_net() {
		return r106_amt_elig_sht_net;
	}
	public void setR106_amt_elig_sht_net(BigDecimal r106_amt_elig_sht_net) {
		this.r106_amt_elig_sht_net = r106_amt_elig_sht_net;
	}
	public BigDecimal getR106_tot_expo_net_spe() {
		return r106_tot_expo_net_spe;
	}
	public void setR106_tot_expo_net_spe(BigDecimal r106_tot_expo_net_spe) {
		this.r106_tot_expo_net_spe = r106_tot_expo_net_spe;
	}
	public BigDecimal getR106_crm_sub_elig_sub_app() {
		return r106_crm_sub_elig_sub_app;
	}
	public void setR106_crm_sub_elig_sub_app(BigDecimal r106_crm_sub_elig_sub_app) {
		this.r106_crm_sub_elig_sub_app = r106_crm_sub_elig_sub_app;
	}
	public BigDecimal getR106_crm_sub_non_col_guar() {
		return r106_crm_sub_non_col_guar;
	}
	public void setR106_crm_sub_non_col_guar(BigDecimal r106_crm_sub_non_col_guar) {
		this.r106_crm_sub_non_col_guar = r106_crm_sub_non_col_guar;
	}
	public BigDecimal getR106_crm_sub_non_col_cre_der() {
		return r106_crm_sub_non_col_cre_der;
	}
	public void setR106_crm_sub_non_col_cre_der(BigDecimal r106_crm_sub_non_col_cre_der) {
		this.r106_crm_sub_non_col_cre_der = r106_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR106_crm_sub_col_elig_cash() {
		return r106_crm_sub_col_elig_cash;
	}
	public void setR106_crm_sub_col_elig_cash(BigDecimal r106_crm_sub_col_elig_cash) {
		this.r106_crm_sub_col_elig_cash = r106_crm_sub_col_elig_cash;
	}
	public BigDecimal getR106_crm_sub_col_elig_trea_bills() {
		return r106_crm_sub_col_elig_trea_bills;
	}
	public void setR106_crm_sub_col_elig_trea_bills(BigDecimal r106_crm_sub_col_elig_trea_bills) {
		this.r106_crm_sub_col_elig_trea_bills = r106_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR106_crm_sub_col_elig_deb_sec() {
		return r106_crm_sub_col_elig_deb_sec;
	}
	public void setR106_crm_sub_col_elig_deb_sec(BigDecimal r106_crm_sub_col_elig_deb_sec) {
		this.r106_crm_sub_col_elig_deb_sec = r106_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR106_crm_sub_col_elig_equi() {
		return r106_crm_sub_col_elig_equi;
	}
	public void setR106_crm_sub_col_elig_equi(BigDecimal r106_crm_sub_col_elig_equi) {
		this.r106_crm_sub_col_elig_equi = r106_crm_sub_col_elig_equi;
	}
	public BigDecimal getR106_crm_sub_col_elig_unit_tru() {
		return r106_crm_sub_col_elig_unit_tru;
	}
	public void setR106_crm_sub_col_elig_unit_tru(BigDecimal r106_crm_sub_col_elig_unit_tru) {
		this.r106_crm_sub_col_elig_unit_tru = r106_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR106_crm_sub_col_exp_cov() {
		return r106_crm_sub_col_exp_cov;
	}
	public void setR106_crm_sub_col_exp_cov(BigDecimal r106_crm_sub_col_exp_cov) {
		this.r106_crm_sub_col_exp_cov = r106_crm_sub_col_exp_cov;
	}
	public BigDecimal getR106_crm_sub_col_elig_exp_not_cov() {
		return r106_crm_sub_col_elig_exp_not_cov;
	}
	public void setR106_crm_sub_col_elig_exp_not_cov(BigDecimal r106_crm_sub_col_elig_exp_not_cov) {
		this.r106_crm_sub_col_elig_exp_not_cov = r106_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR106_crm_sub_rwa_ris_crm() {
		return r106_crm_sub_rwa_ris_crm;
	}
	public void setR106_crm_sub_rwa_ris_crm(BigDecimal r106_crm_sub_rwa_ris_crm) {
		this.r106_crm_sub_rwa_ris_crm = r106_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR106_crm_sub_rwa_cov_crm() {
		return r106_crm_sub_rwa_cov_crm;
	}
	public void setR106_crm_sub_rwa_cov_crm(BigDecimal r106_crm_sub_rwa_cov_crm) {
		this.r106_crm_sub_rwa_cov_crm = r106_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR106_crm_sub_rwa_org_cou() {
		return r106_crm_sub_rwa_org_cou;
	}
	public void setR106_crm_sub_rwa_org_cou(BigDecimal r106_crm_sub_rwa_org_cou) {
		this.r106_crm_sub_rwa_org_cou = r106_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR106_crm_sub_rwa_not_cov_crm() {
		return r106_crm_sub_rwa_not_cov_crm;
	}
	public void setR106_crm_sub_rwa_not_cov_crm(BigDecimal r106_crm_sub_rwa_not_cov_crm) {
		this.r106_crm_sub_rwa_not_cov_crm = r106_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR106_crm_comp_col_expo_elig() {
		return r106_crm_comp_col_expo_elig;
	}
	public void setR106_crm_comp_col_expo_elig(BigDecimal r106_crm_comp_col_expo_elig) {
		this.r106_crm_comp_col_expo_elig = r106_crm_comp_col_expo_elig;
	}
	public BigDecimal getR106_crm_comp_col_elig_expo_vol_adj() {
		return r106_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR106_crm_comp_col_elig_expo_vol_adj(BigDecimal r106_crm_comp_col_elig_expo_vol_adj) {
		this.r106_crm_comp_col_elig_expo_vol_adj = r106_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR106_crm_comp_col_elig_fin_hai() {
		return r106_crm_comp_col_elig_fin_hai;
	}
	public void setR106_crm_comp_col_elig_fin_hai(BigDecimal r106_crm_comp_col_elig_fin_hai) {
		this.r106_crm_comp_col_elig_fin_hai = r106_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR106_crm_comp_col_expo_val() {
		return r106_crm_comp_col_expo_val;
	}
	public void setR106_crm_comp_col_expo_val(BigDecimal r106_crm_comp_col_expo_val) {
		this.r106_crm_comp_col_expo_val = r106_crm_comp_col_expo_val;
	}
	public BigDecimal getR106_rwa_elig_expo_not_cov_crm() {
		return r106_rwa_elig_expo_not_cov_crm;
	}
	public void setR106_rwa_elig_expo_not_cov_crm(BigDecimal r106_rwa_elig_expo_not_cov_crm) {
		this.r106_rwa_elig_expo_not_cov_crm = r106_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR106_rwa_unsec_expo_cre_ris() {
		return r106_rwa_unsec_expo_cre_ris;
	}
	public void setR106_rwa_unsec_expo_cre_ris(BigDecimal r106_rwa_unsec_expo_cre_ris) {
		this.r106_rwa_unsec_expo_cre_ris = r106_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR106_rwa_unsec_expo() {
		return r106_rwa_unsec_expo;
	}
	public void setR106_rwa_unsec_expo(BigDecimal r106_rwa_unsec_expo) {
		this.r106_rwa_unsec_expo = r106_rwa_unsec_expo;
	}
	public BigDecimal getR106_rwa_tot_ris_wei_ass() {
		return r106_rwa_tot_ris_wei_ass;
	}
	public void setR106_rwa_tot_ris_wei_ass(BigDecimal r106_rwa_tot_ris_wei_ass) {
		this.r106_rwa_tot_ris_wei_ass = r106_rwa_tot_ris_wei_ass;
	}
	public String getR107_exposure_class() {
		return r107_exposure_class;
	}
	public void setR107_exposure_class(String r107_exposure_class) {
		this.r107_exposure_class = r107_exposure_class;
	}
	public BigDecimal getR107_expo_crm() {
		return r107_expo_crm;
	}
	public void setR107_expo_crm(BigDecimal r107_expo_crm) {
		this.r107_expo_crm = r107_expo_crm;
	}
	public BigDecimal getR107_spe_pro_expo() {
		return r107_spe_pro_expo;
	}
	public void setR107_spe_pro_expo(BigDecimal r107_spe_pro_expo) {
		this.r107_spe_pro_expo = r107_spe_pro_expo;
	}
	public BigDecimal getR107_amt_elig_sht_net() {
		return r107_amt_elig_sht_net;
	}
	public void setR107_amt_elig_sht_net(BigDecimal r107_amt_elig_sht_net) {
		this.r107_amt_elig_sht_net = r107_amt_elig_sht_net;
	}
	public BigDecimal getR107_tot_expo_net_spe() {
		return r107_tot_expo_net_spe;
	}
	public void setR107_tot_expo_net_spe(BigDecimal r107_tot_expo_net_spe) {
		this.r107_tot_expo_net_spe = r107_tot_expo_net_spe;
	}
	public BigDecimal getR107_crm_sub_elig_sub_app() {
		return r107_crm_sub_elig_sub_app;
	}
	public void setR107_crm_sub_elig_sub_app(BigDecimal r107_crm_sub_elig_sub_app) {
		this.r107_crm_sub_elig_sub_app = r107_crm_sub_elig_sub_app;
	}
	public BigDecimal getR107_crm_sub_non_col_guar() {
		return r107_crm_sub_non_col_guar;
	}
	public void setR107_crm_sub_non_col_guar(BigDecimal r107_crm_sub_non_col_guar) {
		this.r107_crm_sub_non_col_guar = r107_crm_sub_non_col_guar;
	}
	public BigDecimal getR107_crm_sub_non_col_cre_der() {
		return r107_crm_sub_non_col_cre_der;
	}
	public void setR107_crm_sub_non_col_cre_der(BigDecimal r107_crm_sub_non_col_cre_der) {
		this.r107_crm_sub_non_col_cre_der = r107_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR107_crm_sub_col_elig_cash() {
		return r107_crm_sub_col_elig_cash;
	}
	public void setR107_crm_sub_col_elig_cash(BigDecimal r107_crm_sub_col_elig_cash) {
		this.r107_crm_sub_col_elig_cash = r107_crm_sub_col_elig_cash;
	}
	public BigDecimal getR107_crm_sub_col_elig_trea_bills() {
		return r107_crm_sub_col_elig_trea_bills;
	}
	public void setR107_crm_sub_col_elig_trea_bills(BigDecimal r107_crm_sub_col_elig_trea_bills) {
		this.r107_crm_sub_col_elig_trea_bills = r107_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR107_crm_sub_col_elig_deb_sec() {
		return r107_crm_sub_col_elig_deb_sec;
	}
	public void setR107_crm_sub_col_elig_deb_sec(BigDecimal r107_crm_sub_col_elig_deb_sec) {
		this.r107_crm_sub_col_elig_deb_sec = r107_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR107_crm_sub_col_elig_equi() {
		return r107_crm_sub_col_elig_equi;
	}
	public void setR107_crm_sub_col_elig_equi(BigDecimal r107_crm_sub_col_elig_equi) {
		this.r107_crm_sub_col_elig_equi = r107_crm_sub_col_elig_equi;
	}
	public BigDecimal getR107_crm_sub_col_elig_unit_tru() {
		return r107_crm_sub_col_elig_unit_tru;
	}
	public void setR107_crm_sub_col_elig_unit_tru(BigDecimal r107_crm_sub_col_elig_unit_tru) {
		this.r107_crm_sub_col_elig_unit_tru = r107_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR107_crm_sub_col_exp_cov() {
		return r107_crm_sub_col_exp_cov;
	}
	public void setR107_crm_sub_col_exp_cov(BigDecimal r107_crm_sub_col_exp_cov) {
		this.r107_crm_sub_col_exp_cov = r107_crm_sub_col_exp_cov;
	}
	public BigDecimal getR107_crm_sub_col_elig_exp_not_cov() {
		return r107_crm_sub_col_elig_exp_not_cov;
	}
	public void setR107_crm_sub_col_elig_exp_not_cov(BigDecimal r107_crm_sub_col_elig_exp_not_cov) {
		this.r107_crm_sub_col_elig_exp_not_cov = r107_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR107_crm_sub_rwa_ris_crm() {
		return r107_crm_sub_rwa_ris_crm;
	}
	public void setR107_crm_sub_rwa_ris_crm(BigDecimal r107_crm_sub_rwa_ris_crm) {
		this.r107_crm_sub_rwa_ris_crm = r107_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR107_crm_sub_rwa_cov_crm() {
		return r107_crm_sub_rwa_cov_crm;
	}
	public void setR107_crm_sub_rwa_cov_crm(BigDecimal r107_crm_sub_rwa_cov_crm) {
		this.r107_crm_sub_rwa_cov_crm = r107_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR107_crm_sub_rwa_org_cou() {
		return r107_crm_sub_rwa_org_cou;
	}
	public void setR107_crm_sub_rwa_org_cou(BigDecimal r107_crm_sub_rwa_org_cou) {
		this.r107_crm_sub_rwa_org_cou = r107_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR107_crm_sub_rwa_not_cov_crm() {
		return r107_crm_sub_rwa_not_cov_crm;
	}
	public void setR107_crm_sub_rwa_not_cov_crm(BigDecimal r107_crm_sub_rwa_not_cov_crm) {
		this.r107_crm_sub_rwa_not_cov_crm = r107_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR107_crm_comp_col_expo_elig() {
		return r107_crm_comp_col_expo_elig;
	}
	public void setR107_crm_comp_col_expo_elig(BigDecimal r107_crm_comp_col_expo_elig) {
		this.r107_crm_comp_col_expo_elig = r107_crm_comp_col_expo_elig;
	}
	public BigDecimal getR107_crm_comp_col_elig_expo_vol_adj() {
		return r107_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR107_crm_comp_col_elig_expo_vol_adj(BigDecimal r107_crm_comp_col_elig_expo_vol_adj) {
		this.r107_crm_comp_col_elig_expo_vol_adj = r107_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR107_crm_comp_col_elig_fin_hai() {
		return r107_crm_comp_col_elig_fin_hai;
	}
	public void setR107_crm_comp_col_elig_fin_hai(BigDecimal r107_crm_comp_col_elig_fin_hai) {
		this.r107_crm_comp_col_elig_fin_hai = r107_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR107_crm_comp_col_expo_val() {
		return r107_crm_comp_col_expo_val;
	}
	public void setR107_crm_comp_col_expo_val(BigDecimal r107_crm_comp_col_expo_val) {
		this.r107_crm_comp_col_expo_val = r107_crm_comp_col_expo_val;
	}
	public BigDecimal getR107_rwa_elig_expo_not_cov_crm() {
		return r107_rwa_elig_expo_not_cov_crm;
	}
	public void setR107_rwa_elig_expo_not_cov_crm(BigDecimal r107_rwa_elig_expo_not_cov_crm) {
		this.r107_rwa_elig_expo_not_cov_crm = r107_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR107_rwa_unsec_expo_cre_ris() {
		return r107_rwa_unsec_expo_cre_ris;
	}
	public void setR107_rwa_unsec_expo_cre_ris(BigDecimal r107_rwa_unsec_expo_cre_ris) {
		this.r107_rwa_unsec_expo_cre_ris = r107_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR107_rwa_unsec_expo() {
		return r107_rwa_unsec_expo;
	}
	public void setR107_rwa_unsec_expo(BigDecimal r107_rwa_unsec_expo) {
		this.r107_rwa_unsec_expo = r107_rwa_unsec_expo;
	}
	public BigDecimal getR107_rwa_tot_ris_wei_ass() {
		return r107_rwa_tot_ris_wei_ass;
	}
	public void setR107_rwa_tot_ris_wei_ass(BigDecimal r107_rwa_tot_ris_wei_ass) {
		this.r107_rwa_tot_ris_wei_ass = r107_rwa_tot_ris_wei_ass;
	}
	public String getR108_exposure_class() {
		return r108_exposure_class;
	}
	public void setR108_exposure_class(String r108_exposure_class) {
		this.r108_exposure_class = r108_exposure_class;
	}
	public BigDecimal getR108_expo_crm() {
		return r108_expo_crm;
	}
	public void setR108_expo_crm(BigDecimal r108_expo_crm) {
		this.r108_expo_crm = r108_expo_crm;
	}
	public BigDecimal getR108_spe_pro_expo() {
		return r108_spe_pro_expo;
	}
	public void setR108_spe_pro_expo(BigDecimal r108_spe_pro_expo) {
		this.r108_spe_pro_expo = r108_spe_pro_expo;
	}
	public BigDecimal getR108_amt_elig_sht_net() {
		return r108_amt_elig_sht_net;
	}
	public void setR108_amt_elig_sht_net(BigDecimal r108_amt_elig_sht_net) {
		this.r108_amt_elig_sht_net = r108_amt_elig_sht_net;
	}
	public BigDecimal getR108_tot_expo_net_spe() {
		return r108_tot_expo_net_spe;
	}
	public void setR108_tot_expo_net_spe(BigDecimal r108_tot_expo_net_spe) {
		this.r108_tot_expo_net_spe = r108_tot_expo_net_spe;
	}
	public BigDecimal getR108_crm_sub_elig_sub_app() {
		return r108_crm_sub_elig_sub_app;
	}
	public void setR108_crm_sub_elig_sub_app(BigDecimal r108_crm_sub_elig_sub_app) {
		this.r108_crm_sub_elig_sub_app = r108_crm_sub_elig_sub_app;
	}
	public BigDecimal getR108_crm_sub_non_col_guar() {
		return r108_crm_sub_non_col_guar;
	}
	public void setR108_crm_sub_non_col_guar(BigDecimal r108_crm_sub_non_col_guar) {
		this.r108_crm_sub_non_col_guar = r108_crm_sub_non_col_guar;
	}
	public BigDecimal getR108_crm_sub_non_col_cre_der() {
		return r108_crm_sub_non_col_cre_der;
	}
	public void setR108_crm_sub_non_col_cre_der(BigDecimal r108_crm_sub_non_col_cre_der) {
		this.r108_crm_sub_non_col_cre_der = r108_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR108_crm_sub_col_elig_cash() {
		return r108_crm_sub_col_elig_cash;
	}
	public void setR108_crm_sub_col_elig_cash(BigDecimal r108_crm_sub_col_elig_cash) {
		this.r108_crm_sub_col_elig_cash = r108_crm_sub_col_elig_cash;
	}
	public BigDecimal getR108_crm_sub_col_elig_trea_bills() {
		return r108_crm_sub_col_elig_trea_bills;
	}
	public void setR108_crm_sub_col_elig_trea_bills(BigDecimal r108_crm_sub_col_elig_trea_bills) {
		this.r108_crm_sub_col_elig_trea_bills = r108_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR108_crm_sub_col_elig_deb_sec() {
		return r108_crm_sub_col_elig_deb_sec;
	}
	public void setR108_crm_sub_col_elig_deb_sec(BigDecimal r108_crm_sub_col_elig_deb_sec) {
		this.r108_crm_sub_col_elig_deb_sec = r108_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR108_crm_sub_col_elig_equi() {
		return r108_crm_sub_col_elig_equi;
	}
	public void setR108_crm_sub_col_elig_equi(BigDecimal r108_crm_sub_col_elig_equi) {
		this.r108_crm_sub_col_elig_equi = r108_crm_sub_col_elig_equi;
	}
	public BigDecimal getR108_crm_sub_col_elig_unit_tru() {
		return r108_crm_sub_col_elig_unit_tru;
	}
	public void setR108_crm_sub_col_elig_unit_tru(BigDecimal r108_crm_sub_col_elig_unit_tru) {
		this.r108_crm_sub_col_elig_unit_tru = r108_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR108_crm_sub_col_exp_cov() {
		return r108_crm_sub_col_exp_cov;
	}
	public void setR108_crm_sub_col_exp_cov(BigDecimal r108_crm_sub_col_exp_cov) {
		this.r108_crm_sub_col_exp_cov = r108_crm_sub_col_exp_cov;
	}
	public BigDecimal getR108_crm_sub_col_elig_exp_not_cov() {
		return r108_crm_sub_col_elig_exp_not_cov;
	}
	public void setR108_crm_sub_col_elig_exp_not_cov(BigDecimal r108_crm_sub_col_elig_exp_not_cov) {
		this.r108_crm_sub_col_elig_exp_not_cov = r108_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR108_crm_sub_rwa_ris_crm() {
		return r108_crm_sub_rwa_ris_crm;
	}
	public void setR108_crm_sub_rwa_ris_crm(BigDecimal r108_crm_sub_rwa_ris_crm) {
		this.r108_crm_sub_rwa_ris_crm = r108_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR108_crm_sub_rwa_cov_crm() {
		return r108_crm_sub_rwa_cov_crm;
	}
	public void setR108_crm_sub_rwa_cov_crm(BigDecimal r108_crm_sub_rwa_cov_crm) {
		this.r108_crm_sub_rwa_cov_crm = r108_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR108_crm_sub_rwa_org_cou() {
		return r108_crm_sub_rwa_org_cou;
	}
	public void setR108_crm_sub_rwa_org_cou(BigDecimal r108_crm_sub_rwa_org_cou) {
		this.r108_crm_sub_rwa_org_cou = r108_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR108_crm_sub_rwa_not_cov_crm() {
		return r108_crm_sub_rwa_not_cov_crm;
	}
	public void setR108_crm_sub_rwa_not_cov_crm(BigDecimal r108_crm_sub_rwa_not_cov_crm) {
		this.r108_crm_sub_rwa_not_cov_crm = r108_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR108_crm_comp_col_expo_elig() {
		return r108_crm_comp_col_expo_elig;
	}
	public void setR108_crm_comp_col_expo_elig(BigDecimal r108_crm_comp_col_expo_elig) {
		this.r108_crm_comp_col_expo_elig = r108_crm_comp_col_expo_elig;
	}
	public BigDecimal getR108_crm_comp_col_elig_expo_vol_adj() {
		return r108_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR108_crm_comp_col_elig_expo_vol_adj(BigDecimal r108_crm_comp_col_elig_expo_vol_adj) {
		this.r108_crm_comp_col_elig_expo_vol_adj = r108_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR108_crm_comp_col_elig_fin_hai() {
		return r108_crm_comp_col_elig_fin_hai;
	}
	public void setR108_crm_comp_col_elig_fin_hai(BigDecimal r108_crm_comp_col_elig_fin_hai) {
		this.r108_crm_comp_col_elig_fin_hai = r108_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR108_crm_comp_col_expo_val() {
		return r108_crm_comp_col_expo_val;
	}
	public void setR108_crm_comp_col_expo_val(BigDecimal r108_crm_comp_col_expo_val) {
		this.r108_crm_comp_col_expo_val = r108_crm_comp_col_expo_val;
	}
	public BigDecimal getR108_rwa_elig_expo_not_cov_crm() {
		return r108_rwa_elig_expo_not_cov_crm;
	}
	public void setR108_rwa_elig_expo_not_cov_crm(BigDecimal r108_rwa_elig_expo_not_cov_crm) {
		this.r108_rwa_elig_expo_not_cov_crm = r108_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR108_rwa_unsec_expo_cre_ris() {
		return r108_rwa_unsec_expo_cre_ris;
	}
	public void setR108_rwa_unsec_expo_cre_ris(BigDecimal r108_rwa_unsec_expo_cre_ris) {
		this.r108_rwa_unsec_expo_cre_ris = r108_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR108_rwa_unsec_expo() {
		return r108_rwa_unsec_expo;
	}
	public void setR108_rwa_unsec_expo(BigDecimal r108_rwa_unsec_expo) {
		this.r108_rwa_unsec_expo = r108_rwa_unsec_expo;
	}
	public BigDecimal getR108_rwa_tot_ris_wei_ass() {
		return r108_rwa_tot_ris_wei_ass;
	}
	public void setR108_rwa_tot_ris_wei_ass(BigDecimal r108_rwa_tot_ris_wei_ass) {
		this.r108_rwa_tot_ris_wei_ass = r108_rwa_tot_ris_wei_ass;
	}
	public String getR109_exposure_class() {
		return r109_exposure_class;
	}
	public void setR109_exposure_class(String r109_exposure_class) {
		this.r109_exposure_class = r109_exposure_class;
	}
	public BigDecimal getR109_expo_crm() {
		return r109_expo_crm;
	}
	public void setR109_expo_crm(BigDecimal r109_expo_crm) {
		this.r109_expo_crm = r109_expo_crm;
	}
	public BigDecimal getR109_spe_pro_expo() {
		return r109_spe_pro_expo;
	}
	public void setR109_spe_pro_expo(BigDecimal r109_spe_pro_expo) {
		this.r109_spe_pro_expo = r109_spe_pro_expo;
	}
	public BigDecimal getR109_amt_elig_sht_net() {
		return r109_amt_elig_sht_net;
	}
	public void setR109_amt_elig_sht_net(BigDecimal r109_amt_elig_sht_net) {
		this.r109_amt_elig_sht_net = r109_amt_elig_sht_net;
	}
	public BigDecimal getR109_tot_expo_net_spe() {
		return r109_tot_expo_net_spe;
	}
	public void setR109_tot_expo_net_spe(BigDecimal r109_tot_expo_net_spe) {
		this.r109_tot_expo_net_spe = r109_tot_expo_net_spe;
	}
	public BigDecimal getR109_crm_sub_elig_sub_app() {
		return r109_crm_sub_elig_sub_app;
	}
	public void setR109_crm_sub_elig_sub_app(BigDecimal r109_crm_sub_elig_sub_app) {
		this.r109_crm_sub_elig_sub_app = r109_crm_sub_elig_sub_app;
	}
	public BigDecimal getR109_crm_sub_non_col_guar() {
		return r109_crm_sub_non_col_guar;
	}
	public void setR109_crm_sub_non_col_guar(BigDecimal r109_crm_sub_non_col_guar) {
		this.r109_crm_sub_non_col_guar = r109_crm_sub_non_col_guar;
	}
	public BigDecimal getR109_crm_sub_non_col_cre_der() {
		return r109_crm_sub_non_col_cre_der;
	}
	public void setR109_crm_sub_non_col_cre_der(BigDecimal r109_crm_sub_non_col_cre_der) {
		this.r109_crm_sub_non_col_cre_der = r109_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR109_crm_sub_col_elig_cash() {
		return r109_crm_sub_col_elig_cash;
	}
	public void setR109_crm_sub_col_elig_cash(BigDecimal r109_crm_sub_col_elig_cash) {
		this.r109_crm_sub_col_elig_cash = r109_crm_sub_col_elig_cash;
	}
	public BigDecimal getR109_crm_sub_col_elig_trea_bills() {
		return r109_crm_sub_col_elig_trea_bills;
	}
	public void setR109_crm_sub_col_elig_trea_bills(BigDecimal r109_crm_sub_col_elig_trea_bills) {
		this.r109_crm_sub_col_elig_trea_bills = r109_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR109_crm_sub_col_elig_deb_sec() {
		return r109_crm_sub_col_elig_deb_sec;
	}
	public void setR109_crm_sub_col_elig_deb_sec(BigDecimal r109_crm_sub_col_elig_deb_sec) {
		this.r109_crm_sub_col_elig_deb_sec = r109_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR109_crm_sub_col_elig_equi() {
		return r109_crm_sub_col_elig_equi;
	}
	public void setR109_crm_sub_col_elig_equi(BigDecimal r109_crm_sub_col_elig_equi) {
		this.r109_crm_sub_col_elig_equi = r109_crm_sub_col_elig_equi;
	}
	public BigDecimal getR109_crm_sub_col_elig_unit_tru() {
		return r109_crm_sub_col_elig_unit_tru;
	}
	public void setR109_crm_sub_col_elig_unit_tru(BigDecimal r109_crm_sub_col_elig_unit_tru) {
		this.r109_crm_sub_col_elig_unit_tru = r109_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR109_crm_sub_col_exp_cov() {
		return r109_crm_sub_col_exp_cov;
	}
	public void setR109_crm_sub_col_exp_cov(BigDecimal r109_crm_sub_col_exp_cov) {
		this.r109_crm_sub_col_exp_cov = r109_crm_sub_col_exp_cov;
	}
	public BigDecimal getR109_crm_sub_col_elig_exp_not_cov() {
		return r109_crm_sub_col_elig_exp_not_cov;
	}
	public void setR109_crm_sub_col_elig_exp_not_cov(BigDecimal r109_crm_sub_col_elig_exp_not_cov) {
		this.r109_crm_sub_col_elig_exp_not_cov = r109_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR109_crm_sub_rwa_ris_crm() {
		return r109_crm_sub_rwa_ris_crm;
	}
	public void setR109_crm_sub_rwa_ris_crm(BigDecimal r109_crm_sub_rwa_ris_crm) {
		this.r109_crm_sub_rwa_ris_crm = r109_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR109_crm_sub_rwa_cov_crm() {
		return r109_crm_sub_rwa_cov_crm;
	}
	public void setR109_crm_sub_rwa_cov_crm(BigDecimal r109_crm_sub_rwa_cov_crm) {
		this.r109_crm_sub_rwa_cov_crm = r109_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR109_crm_sub_rwa_org_cou() {
		return r109_crm_sub_rwa_org_cou;
	}
	public void setR109_crm_sub_rwa_org_cou(BigDecimal r109_crm_sub_rwa_org_cou) {
		this.r109_crm_sub_rwa_org_cou = r109_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR109_crm_sub_rwa_not_cov_crm() {
		return r109_crm_sub_rwa_not_cov_crm;
	}
	public void setR109_crm_sub_rwa_not_cov_crm(BigDecimal r109_crm_sub_rwa_not_cov_crm) {
		this.r109_crm_sub_rwa_not_cov_crm = r109_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR109_crm_comp_col_expo_elig() {
		return r109_crm_comp_col_expo_elig;
	}
	public void setR109_crm_comp_col_expo_elig(BigDecimal r109_crm_comp_col_expo_elig) {
		this.r109_crm_comp_col_expo_elig = r109_crm_comp_col_expo_elig;
	}
	public BigDecimal getR109_crm_comp_col_elig_expo_vol_adj() {
		return r109_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR109_crm_comp_col_elig_expo_vol_adj(BigDecimal r109_crm_comp_col_elig_expo_vol_adj) {
		this.r109_crm_comp_col_elig_expo_vol_adj = r109_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR109_crm_comp_col_elig_fin_hai() {
		return r109_crm_comp_col_elig_fin_hai;
	}
	public void setR109_crm_comp_col_elig_fin_hai(BigDecimal r109_crm_comp_col_elig_fin_hai) {
		this.r109_crm_comp_col_elig_fin_hai = r109_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR109_crm_comp_col_expo_val() {
		return r109_crm_comp_col_expo_val;
	}
	public void setR109_crm_comp_col_expo_val(BigDecimal r109_crm_comp_col_expo_val) {
		this.r109_crm_comp_col_expo_val = r109_crm_comp_col_expo_val;
	}
	public BigDecimal getR109_rwa_elig_expo_not_cov_crm() {
		return r109_rwa_elig_expo_not_cov_crm;
	}
	public void setR109_rwa_elig_expo_not_cov_crm(BigDecimal r109_rwa_elig_expo_not_cov_crm) {
		this.r109_rwa_elig_expo_not_cov_crm = r109_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR109_rwa_unsec_expo_cre_ris() {
		return r109_rwa_unsec_expo_cre_ris;
	}
	public void setR109_rwa_unsec_expo_cre_ris(BigDecimal r109_rwa_unsec_expo_cre_ris) {
		this.r109_rwa_unsec_expo_cre_ris = r109_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR109_rwa_unsec_expo() {
		return r109_rwa_unsec_expo;
	}
	public void setR109_rwa_unsec_expo(BigDecimal r109_rwa_unsec_expo) {
		this.r109_rwa_unsec_expo = r109_rwa_unsec_expo;
	}
	public BigDecimal getR109_rwa_tot_ris_wei_ass() {
		return r109_rwa_tot_ris_wei_ass;
	}
	public void setR109_rwa_tot_ris_wei_ass(BigDecimal r109_rwa_tot_ris_wei_ass) {
		this.r109_rwa_tot_ris_wei_ass = r109_rwa_tot_ris_wei_ass;
	}
	public String getR110_exposure_class() {
		return r110_exposure_class;
	}
	public void setR110_exposure_class(String r110_exposure_class) {
		this.r110_exposure_class = r110_exposure_class;
	}
	public BigDecimal getR110_expo_crm() {
		return r110_expo_crm;
	}
	public void setR110_expo_crm(BigDecimal r110_expo_crm) {
		this.r110_expo_crm = r110_expo_crm;
	}
	public BigDecimal getR110_spe_pro_expo() {
		return r110_spe_pro_expo;
	}
	public void setR110_spe_pro_expo(BigDecimal r110_spe_pro_expo) {
		this.r110_spe_pro_expo = r110_spe_pro_expo;
	}
	public BigDecimal getR110_amt_elig_sht_net() {
		return r110_amt_elig_sht_net;
	}
	public void setR110_amt_elig_sht_net(BigDecimal r110_amt_elig_sht_net) {
		this.r110_amt_elig_sht_net = r110_amt_elig_sht_net;
	}
	public BigDecimal getR110_tot_expo_net_spe() {
		return r110_tot_expo_net_spe;
	}
	public void setR110_tot_expo_net_spe(BigDecimal r110_tot_expo_net_spe) {
		this.r110_tot_expo_net_spe = r110_tot_expo_net_spe;
	}
	public BigDecimal getR110_crm_sub_elig_sub_app() {
		return r110_crm_sub_elig_sub_app;
	}
	public void setR110_crm_sub_elig_sub_app(BigDecimal r110_crm_sub_elig_sub_app) {
		this.r110_crm_sub_elig_sub_app = r110_crm_sub_elig_sub_app;
	}
	public BigDecimal getR110_crm_sub_non_col_guar() {
		return r110_crm_sub_non_col_guar;
	}
	public void setR110_crm_sub_non_col_guar(BigDecimal r110_crm_sub_non_col_guar) {
		this.r110_crm_sub_non_col_guar = r110_crm_sub_non_col_guar;
	}
	public BigDecimal getR110_crm_sub_non_col_cre_der() {
		return r110_crm_sub_non_col_cre_der;
	}
	public void setR110_crm_sub_non_col_cre_der(BigDecimal r110_crm_sub_non_col_cre_der) {
		this.r110_crm_sub_non_col_cre_der = r110_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR110_crm_sub_col_elig_cash() {
		return r110_crm_sub_col_elig_cash;
	}
	public void setR110_crm_sub_col_elig_cash(BigDecimal r110_crm_sub_col_elig_cash) {
		this.r110_crm_sub_col_elig_cash = r110_crm_sub_col_elig_cash;
	}
	public BigDecimal getR110_crm_sub_col_elig_trea_bills() {
		return r110_crm_sub_col_elig_trea_bills;
	}
	public void setR110_crm_sub_col_elig_trea_bills(BigDecimal r110_crm_sub_col_elig_trea_bills) {
		this.r110_crm_sub_col_elig_trea_bills = r110_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR110_crm_sub_col_elig_deb_sec() {
		return r110_crm_sub_col_elig_deb_sec;
	}
	public void setR110_crm_sub_col_elig_deb_sec(BigDecimal r110_crm_sub_col_elig_deb_sec) {
		this.r110_crm_sub_col_elig_deb_sec = r110_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR110_crm_sub_col_elig_equi() {
		return r110_crm_sub_col_elig_equi;
	}
	public void setR110_crm_sub_col_elig_equi(BigDecimal r110_crm_sub_col_elig_equi) {
		this.r110_crm_sub_col_elig_equi = r110_crm_sub_col_elig_equi;
	}
	public BigDecimal getR110_crm_sub_col_elig_unit_tru() {
		return r110_crm_sub_col_elig_unit_tru;
	}
	public void setR110_crm_sub_col_elig_unit_tru(BigDecimal r110_crm_sub_col_elig_unit_tru) {
		this.r110_crm_sub_col_elig_unit_tru = r110_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR110_crm_sub_col_exp_cov() {
		return r110_crm_sub_col_exp_cov;
	}
	public void setR110_crm_sub_col_exp_cov(BigDecimal r110_crm_sub_col_exp_cov) {
		this.r110_crm_sub_col_exp_cov = r110_crm_sub_col_exp_cov;
	}
	public BigDecimal getR110_crm_sub_col_elig_exp_not_cov() {
		return r110_crm_sub_col_elig_exp_not_cov;
	}
	public void setR110_crm_sub_col_elig_exp_not_cov(BigDecimal r110_crm_sub_col_elig_exp_not_cov) {
		this.r110_crm_sub_col_elig_exp_not_cov = r110_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR110_crm_sub_rwa_ris_crm() {
		return r110_crm_sub_rwa_ris_crm;
	}
	public void setR110_crm_sub_rwa_ris_crm(BigDecimal r110_crm_sub_rwa_ris_crm) {
		this.r110_crm_sub_rwa_ris_crm = r110_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR110_crm_sub_rwa_cov_crm() {
		return r110_crm_sub_rwa_cov_crm;
	}
	public void setR110_crm_sub_rwa_cov_crm(BigDecimal r110_crm_sub_rwa_cov_crm) {
		this.r110_crm_sub_rwa_cov_crm = r110_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR110_crm_sub_rwa_org_cou() {
		return r110_crm_sub_rwa_org_cou;
	}
	public void setR110_crm_sub_rwa_org_cou(BigDecimal r110_crm_sub_rwa_org_cou) {
		this.r110_crm_sub_rwa_org_cou = r110_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR110_crm_sub_rwa_not_cov_crm() {
		return r110_crm_sub_rwa_not_cov_crm;
	}
	public void setR110_crm_sub_rwa_not_cov_crm(BigDecimal r110_crm_sub_rwa_not_cov_crm) {
		this.r110_crm_sub_rwa_not_cov_crm = r110_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR110_crm_comp_col_expo_elig() {
		return r110_crm_comp_col_expo_elig;
	}
	public void setR110_crm_comp_col_expo_elig(BigDecimal r110_crm_comp_col_expo_elig) {
		this.r110_crm_comp_col_expo_elig = r110_crm_comp_col_expo_elig;
	}
	public BigDecimal getR110_crm_comp_col_elig_expo_vol_adj() {
		return r110_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR110_crm_comp_col_elig_expo_vol_adj(BigDecimal r110_crm_comp_col_elig_expo_vol_adj) {
		this.r110_crm_comp_col_elig_expo_vol_adj = r110_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR110_crm_comp_col_elig_fin_hai() {
		return r110_crm_comp_col_elig_fin_hai;
	}
	public void setR110_crm_comp_col_elig_fin_hai(BigDecimal r110_crm_comp_col_elig_fin_hai) {
		this.r110_crm_comp_col_elig_fin_hai = r110_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR110_crm_comp_col_expo_val() {
		return r110_crm_comp_col_expo_val;
	}
	public void setR110_crm_comp_col_expo_val(BigDecimal r110_crm_comp_col_expo_val) {
		this.r110_crm_comp_col_expo_val = r110_crm_comp_col_expo_val;
	}
	public BigDecimal getR110_rwa_elig_expo_not_cov_crm() {
		return r110_rwa_elig_expo_not_cov_crm;
	}
	public void setR110_rwa_elig_expo_not_cov_crm(BigDecimal r110_rwa_elig_expo_not_cov_crm) {
		this.r110_rwa_elig_expo_not_cov_crm = r110_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR110_rwa_unsec_expo_cre_ris() {
		return r110_rwa_unsec_expo_cre_ris;
	}
	public void setR110_rwa_unsec_expo_cre_ris(BigDecimal r110_rwa_unsec_expo_cre_ris) {
		this.r110_rwa_unsec_expo_cre_ris = r110_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR110_rwa_unsec_expo() {
		return r110_rwa_unsec_expo;
	}
	public void setR110_rwa_unsec_expo(BigDecimal r110_rwa_unsec_expo) {
		this.r110_rwa_unsec_expo = r110_rwa_unsec_expo;
	}
	public BigDecimal getR110_rwa_tot_ris_wei_ass() {
		return r110_rwa_tot_ris_wei_ass;
	}
	public void setR110_rwa_tot_ris_wei_ass(BigDecimal r110_rwa_tot_ris_wei_ass) {
		this.r110_rwa_tot_ris_wei_ass = r110_rwa_tot_ris_wei_ass;
	}
	public String getR111_exposure_class() {
		return r111_exposure_class;
	}
	public void setR111_exposure_class(String r111_exposure_class) {
		this.r111_exposure_class = r111_exposure_class;
	}
	public BigDecimal getR111_expo_crm() {
		return r111_expo_crm;
	}
	public void setR111_expo_crm(BigDecimal r111_expo_crm) {
		this.r111_expo_crm = r111_expo_crm;
	}
	public BigDecimal getR111_spe_pro_expo() {
		return r111_spe_pro_expo;
	}
	public void setR111_spe_pro_expo(BigDecimal r111_spe_pro_expo) {
		this.r111_spe_pro_expo = r111_spe_pro_expo;
	}
	public BigDecimal getR111_amt_elig_sht_net() {
		return r111_amt_elig_sht_net;
	}
	public void setR111_amt_elig_sht_net(BigDecimal r111_amt_elig_sht_net) {
		this.r111_amt_elig_sht_net = r111_amt_elig_sht_net;
	}
	public BigDecimal getR111_tot_expo_net_spe() {
		return r111_tot_expo_net_spe;
	}
	public void setR111_tot_expo_net_spe(BigDecimal r111_tot_expo_net_spe) {
		this.r111_tot_expo_net_spe = r111_tot_expo_net_spe;
	}
	public BigDecimal getR111_crm_sub_elig_sub_app() {
		return r111_crm_sub_elig_sub_app;
	}
	public void setR111_crm_sub_elig_sub_app(BigDecimal r111_crm_sub_elig_sub_app) {
		this.r111_crm_sub_elig_sub_app = r111_crm_sub_elig_sub_app;
	}
	public BigDecimal getR111_crm_sub_non_col_guar() {
		return r111_crm_sub_non_col_guar;
	}
	public void setR111_crm_sub_non_col_guar(BigDecimal r111_crm_sub_non_col_guar) {
		this.r111_crm_sub_non_col_guar = r111_crm_sub_non_col_guar;
	}
	public BigDecimal getR111_crm_sub_non_col_cre_der() {
		return r111_crm_sub_non_col_cre_der;
	}
	public void setR111_crm_sub_non_col_cre_der(BigDecimal r111_crm_sub_non_col_cre_der) {
		this.r111_crm_sub_non_col_cre_der = r111_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR111_crm_sub_col_elig_cash() {
		return r111_crm_sub_col_elig_cash;
	}
	public void setR111_crm_sub_col_elig_cash(BigDecimal r111_crm_sub_col_elig_cash) {
		this.r111_crm_sub_col_elig_cash = r111_crm_sub_col_elig_cash;
	}
	public BigDecimal getR111_crm_sub_col_elig_trea_bills() {
		return r111_crm_sub_col_elig_trea_bills;
	}
	public void setR111_crm_sub_col_elig_trea_bills(BigDecimal r111_crm_sub_col_elig_trea_bills) {
		this.r111_crm_sub_col_elig_trea_bills = r111_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR111_crm_sub_col_elig_deb_sec() {
		return r111_crm_sub_col_elig_deb_sec;
	}
	public void setR111_crm_sub_col_elig_deb_sec(BigDecimal r111_crm_sub_col_elig_deb_sec) {
		this.r111_crm_sub_col_elig_deb_sec = r111_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR111_crm_sub_col_elig_equi() {
		return r111_crm_sub_col_elig_equi;
	}
	public void setR111_crm_sub_col_elig_equi(BigDecimal r111_crm_sub_col_elig_equi) {
		this.r111_crm_sub_col_elig_equi = r111_crm_sub_col_elig_equi;
	}
	public BigDecimal getR111_crm_sub_col_elig_unit_tru() {
		return r111_crm_sub_col_elig_unit_tru;
	}
	public void setR111_crm_sub_col_elig_unit_tru(BigDecimal r111_crm_sub_col_elig_unit_tru) {
		this.r111_crm_sub_col_elig_unit_tru = r111_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR111_crm_sub_col_exp_cov() {
		return r111_crm_sub_col_exp_cov;
	}
	public void setR111_crm_sub_col_exp_cov(BigDecimal r111_crm_sub_col_exp_cov) {
		this.r111_crm_sub_col_exp_cov = r111_crm_sub_col_exp_cov;
	}
	public BigDecimal getR111_crm_sub_col_elig_exp_not_cov() {
		return r111_crm_sub_col_elig_exp_not_cov;
	}
	public void setR111_crm_sub_col_elig_exp_not_cov(BigDecimal r111_crm_sub_col_elig_exp_not_cov) {
		this.r111_crm_sub_col_elig_exp_not_cov = r111_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR111_crm_sub_rwa_ris_crm() {
		return r111_crm_sub_rwa_ris_crm;
	}
	public void setR111_crm_sub_rwa_ris_crm(BigDecimal r111_crm_sub_rwa_ris_crm) {
		this.r111_crm_sub_rwa_ris_crm = r111_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR111_crm_sub_rwa_cov_crm() {
		return r111_crm_sub_rwa_cov_crm;
	}
	public void setR111_crm_sub_rwa_cov_crm(BigDecimal r111_crm_sub_rwa_cov_crm) {
		this.r111_crm_sub_rwa_cov_crm = r111_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR111_crm_sub_rwa_org_cou() {
		return r111_crm_sub_rwa_org_cou;
	}
	public void setR111_crm_sub_rwa_org_cou(BigDecimal r111_crm_sub_rwa_org_cou) {
		this.r111_crm_sub_rwa_org_cou = r111_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR111_crm_sub_rwa_not_cov_crm() {
		return r111_crm_sub_rwa_not_cov_crm;
	}
	public void setR111_crm_sub_rwa_not_cov_crm(BigDecimal r111_crm_sub_rwa_not_cov_crm) {
		this.r111_crm_sub_rwa_not_cov_crm = r111_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR111_crm_comp_col_expo_elig() {
		return r111_crm_comp_col_expo_elig;
	}
	public void setR111_crm_comp_col_expo_elig(BigDecimal r111_crm_comp_col_expo_elig) {
		this.r111_crm_comp_col_expo_elig = r111_crm_comp_col_expo_elig;
	}
	public BigDecimal getR111_crm_comp_col_elig_expo_vol_adj() {
		return r111_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR111_crm_comp_col_elig_expo_vol_adj(BigDecimal r111_crm_comp_col_elig_expo_vol_adj) {
		this.r111_crm_comp_col_elig_expo_vol_adj = r111_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR111_crm_comp_col_elig_fin_hai() {
		return r111_crm_comp_col_elig_fin_hai;
	}
	public void setR111_crm_comp_col_elig_fin_hai(BigDecimal r111_crm_comp_col_elig_fin_hai) {
		this.r111_crm_comp_col_elig_fin_hai = r111_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR111_crm_comp_col_expo_val() {
		return r111_crm_comp_col_expo_val;
	}
	public void setR111_crm_comp_col_expo_val(BigDecimal r111_crm_comp_col_expo_val) {
		this.r111_crm_comp_col_expo_val = r111_crm_comp_col_expo_val;
	}
	public BigDecimal getR111_rwa_elig_expo_not_cov_crm() {
		return r111_rwa_elig_expo_not_cov_crm;
	}
	public void setR111_rwa_elig_expo_not_cov_crm(BigDecimal r111_rwa_elig_expo_not_cov_crm) {
		this.r111_rwa_elig_expo_not_cov_crm = r111_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR111_rwa_unsec_expo_cre_ris() {
		return r111_rwa_unsec_expo_cre_ris;
	}
	public void setR111_rwa_unsec_expo_cre_ris(BigDecimal r111_rwa_unsec_expo_cre_ris) {
		this.r111_rwa_unsec_expo_cre_ris = r111_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR111_rwa_unsec_expo() {
		return r111_rwa_unsec_expo;
	}
	public void setR111_rwa_unsec_expo(BigDecimal r111_rwa_unsec_expo) {
		this.r111_rwa_unsec_expo = r111_rwa_unsec_expo;
	}
	public BigDecimal getR111_rwa_tot_ris_wei_ass() {
		return r111_rwa_tot_ris_wei_ass;
	}
	public void setR111_rwa_tot_ris_wei_ass(BigDecimal r111_rwa_tot_ris_wei_ass) {
		this.r111_rwa_tot_ris_wei_ass = r111_rwa_tot_ris_wei_ass;
	}
	public String getR112_exposure_class() {
		return r112_exposure_class;
	}
	public void setR112_exposure_class(String r112_exposure_class) {
		this.r112_exposure_class = r112_exposure_class;
	}
	public BigDecimal getR112_expo_crm() {
		return r112_expo_crm;
	}
	public void setR112_expo_crm(BigDecimal r112_expo_crm) {
		this.r112_expo_crm = r112_expo_crm;
	}
	public BigDecimal getR112_spe_pro_expo() {
		return r112_spe_pro_expo;
	}
	public void setR112_spe_pro_expo(BigDecimal r112_spe_pro_expo) {
		this.r112_spe_pro_expo = r112_spe_pro_expo;
	}
	public BigDecimal getR112_amt_elig_sht_net() {
		return r112_amt_elig_sht_net;
	}
	public void setR112_amt_elig_sht_net(BigDecimal r112_amt_elig_sht_net) {
		this.r112_amt_elig_sht_net = r112_amt_elig_sht_net;
	}
	public BigDecimal getR112_tot_expo_net_spe() {
		return r112_tot_expo_net_spe;
	}
	public void setR112_tot_expo_net_spe(BigDecimal r112_tot_expo_net_spe) {
		this.r112_tot_expo_net_spe = r112_tot_expo_net_spe;
	}
	public BigDecimal getR112_crm_sub_elig_sub_app() {
		return r112_crm_sub_elig_sub_app;
	}
	public void setR112_crm_sub_elig_sub_app(BigDecimal r112_crm_sub_elig_sub_app) {
		this.r112_crm_sub_elig_sub_app = r112_crm_sub_elig_sub_app;
	}
	public BigDecimal getR112_crm_sub_non_col_guar() {
		return r112_crm_sub_non_col_guar;
	}
	public void setR112_crm_sub_non_col_guar(BigDecimal r112_crm_sub_non_col_guar) {
		this.r112_crm_sub_non_col_guar = r112_crm_sub_non_col_guar;
	}
	public BigDecimal getR112_crm_sub_non_col_cre_der() {
		return r112_crm_sub_non_col_cre_der;
	}
	public void setR112_crm_sub_non_col_cre_der(BigDecimal r112_crm_sub_non_col_cre_der) {
		this.r112_crm_sub_non_col_cre_der = r112_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR112_crm_sub_col_elig_cash() {
		return r112_crm_sub_col_elig_cash;
	}
	public void setR112_crm_sub_col_elig_cash(BigDecimal r112_crm_sub_col_elig_cash) {
		this.r112_crm_sub_col_elig_cash = r112_crm_sub_col_elig_cash;
	}
	public BigDecimal getR112_crm_sub_col_elig_trea_bills() {
		return r112_crm_sub_col_elig_trea_bills;
	}
	public void setR112_crm_sub_col_elig_trea_bills(BigDecimal r112_crm_sub_col_elig_trea_bills) {
		this.r112_crm_sub_col_elig_trea_bills = r112_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR112_crm_sub_col_elig_deb_sec() {
		return r112_crm_sub_col_elig_deb_sec;
	}
	public void setR112_crm_sub_col_elig_deb_sec(BigDecimal r112_crm_sub_col_elig_deb_sec) {
		this.r112_crm_sub_col_elig_deb_sec = r112_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR112_crm_sub_col_elig_equi() {
		return r112_crm_sub_col_elig_equi;
	}
	public void setR112_crm_sub_col_elig_equi(BigDecimal r112_crm_sub_col_elig_equi) {
		this.r112_crm_sub_col_elig_equi = r112_crm_sub_col_elig_equi;
	}
	public BigDecimal getR112_crm_sub_col_elig_unit_tru() {
		return r112_crm_sub_col_elig_unit_tru;
	}
	public void setR112_crm_sub_col_elig_unit_tru(BigDecimal r112_crm_sub_col_elig_unit_tru) {
		this.r112_crm_sub_col_elig_unit_tru = r112_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR112_crm_sub_col_exp_cov() {
		return r112_crm_sub_col_exp_cov;
	}
	public void setR112_crm_sub_col_exp_cov(BigDecimal r112_crm_sub_col_exp_cov) {
		this.r112_crm_sub_col_exp_cov = r112_crm_sub_col_exp_cov;
	}
	public BigDecimal getR112_crm_sub_col_elig_exp_not_cov() {
		return r112_crm_sub_col_elig_exp_not_cov;
	}
	public void setR112_crm_sub_col_elig_exp_not_cov(BigDecimal r112_crm_sub_col_elig_exp_not_cov) {
		this.r112_crm_sub_col_elig_exp_not_cov = r112_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR112_crm_sub_rwa_ris_crm() {
		return r112_crm_sub_rwa_ris_crm;
	}
	public void setR112_crm_sub_rwa_ris_crm(BigDecimal r112_crm_sub_rwa_ris_crm) {
		this.r112_crm_sub_rwa_ris_crm = r112_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR112_crm_sub_rwa_cov_crm() {
		return r112_crm_sub_rwa_cov_crm;
	}
	public void setR112_crm_sub_rwa_cov_crm(BigDecimal r112_crm_sub_rwa_cov_crm) {
		this.r112_crm_sub_rwa_cov_crm = r112_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR112_crm_sub_rwa_org_cou() {
		return r112_crm_sub_rwa_org_cou;
	}
	public void setR112_crm_sub_rwa_org_cou(BigDecimal r112_crm_sub_rwa_org_cou) {
		this.r112_crm_sub_rwa_org_cou = r112_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR112_crm_sub_rwa_not_cov_crm() {
		return r112_crm_sub_rwa_not_cov_crm;
	}
	public void setR112_crm_sub_rwa_not_cov_crm(BigDecimal r112_crm_sub_rwa_not_cov_crm) {
		this.r112_crm_sub_rwa_not_cov_crm = r112_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR112_crm_comp_col_expo_elig() {
		return r112_crm_comp_col_expo_elig;
	}
	public void setR112_crm_comp_col_expo_elig(BigDecimal r112_crm_comp_col_expo_elig) {
		this.r112_crm_comp_col_expo_elig = r112_crm_comp_col_expo_elig;
	}
	public BigDecimal getR112_crm_comp_col_elig_expo_vol_adj() {
		return r112_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR112_crm_comp_col_elig_expo_vol_adj(BigDecimal r112_crm_comp_col_elig_expo_vol_adj) {
		this.r112_crm_comp_col_elig_expo_vol_adj = r112_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR112_crm_comp_col_elig_fin_hai() {
		return r112_crm_comp_col_elig_fin_hai;
	}
	public void setR112_crm_comp_col_elig_fin_hai(BigDecimal r112_crm_comp_col_elig_fin_hai) {
		this.r112_crm_comp_col_elig_fin_hai = r112_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR112_crm_comp_col_expo_val() {
		return r112_crm_comp_col_expo_val;
	}
	public void setR112_crm_comp_col_expo_val(BigDecimal r112_crm_comp_col_expo_val) {
		this.r112_crm_comp_col_expo_val = r112_crm_comp_col_expo_val;
	}
	public BigDecimal getR112_rwa_elig_expo_not_cov_crm() {
		return r112_rwa_elig_expo_not_cov_crm;
	}
	public void setR112_rwa_elig_expo_not_cov_crm(BigDecimal r112_rwa_elig_expo_not_cov_crm) {
		this.r112_rwa_elig_expo_not_cov_crm = r112_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR112_rwa_unsec_expo_cre_ris() {
		return r112_rwa_unsec_expo_cre_ris;
	}
	public void setR112_rwa_unsec_expo_cre_ris(BigDecimal r112_rwa_unsec_expo_cre_ris) {
		this.r112_rwa_unsec_expo_cre_ris = r112_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR112_rwa_unsec_expo() {
		return r112_rwa_unsec_expo;
	}
	public void setR112_rwa_unsec_expo(BigDecimal r112_rwa_unsec_expo) {
		this.r112_rwa_unsec_expo = r112_rwa_unsec_expo;
	}
	public BigDecimal getR112_rwa_tot_ris_wei_ass() {
		return r112_rwa_tot_ris_wei_ass;
	}
	public void setR112_rwa_tot_ris_wei_ass(BigDecimal r112_rwa_tot_ris_wei_ass) {
		this.r112_rwa_tot_ris_wei_ass = r112_rwa_tot_ris_wei_ass;
	}
	public String getR113_exposure_class() {
		return r113_exposure_class;
	}
	public void setR113_exposure_class(String r113_exposure_class) {
		this.r113_exposure_class = r113_exposure_class;
	}
	public BigDecimal getR113_expo_crm() {
		return r113_expo_crm;
	}
	public void setR113_expo_crm(BigDecimal r113_expo_crm) {
		this.r113_expo_crm = r113_expo_crm;
	}
	public BigDecimal getR113_spe_pro_expo() {
		return r113_spe_pro_expo;
	}
	public void setR113_spe_pro_expo(BigDecimal r113_spe_pro_expo) {
		this.r113_spe_pro_expo = r113_spe_pro_expo;
	}
	public BigDecimal getR113_amt_elig_sht_net() {
		return r113_amt_elig_sht_net;
	}
	public void setR113_amt_elig_sht_net(BigDecimal r113_amt_elig_sht_net) {
		this.r113_amt_elig_sht_net = r113_amt_elig_sht_net;
	}
	public BigDecimal getR113_tot_expo_net_spe() {
		return r113_tot_expo_net_spe;
	}
	public void setR113_tot_expo_net_spe(BigDecimal r113_tot_expo_net_spe) {
		this.r113_tot_expo_net_spe = r113_tot_expo_net_spe;
	}
	public BigDecimal getR113_crm_sub_elig_sub_app() {
		return r113_crm_sub_elig_sub_app;
	}
	public void setR113_crm_sub_elig_sub_app(BigDecimal r113_crm_sub_elig_sub_app) {
		this.r113_crm_sub_elig_sub_app = r113_crm_sub_elig_sub_app;
	}
	public BigDecimal getR113_crm_sub_non_col_guar() {
		return r113_crm_sub_non_col_guar;
	}
	public void setR113_crm_sub_non_col_guar(BigDecimal r113_crm_sub_non_col_guar) {
		this.r113_crm_sub_non_col_guar = r113_crm_sub_non_col_guar;
	}
	public BigDecimal getR113_crm_sub_non_col_cre_der() {
		return r113_crm_sub_non_col_cre_der;
	}
	public void setR113_crm_sub_non_col_cre_der(BigDecimal r113_crm_sub_non_col_cre_der) {
		this.r113_crm_sub_non_col_cre_der = r113_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR113_crm_sub_col_elig_cash() {
		return r113_crm_sub_col_elig_cash;
	}
	public void setR113_crm_sub_col_elig_cash(BigDecimal r113_crm_sub_col_elig_cash) {
		this.r113_crm_sub_col_elig_cash = r113_crm_sub_col_elig_cash;
	}
	public BigDecimal getR113_crm_sub_col_elig_trea_bills() {
		return r113_crm_sub_col_elig_trea_bills;
	}
	public void setR113_crm_sub_col_elig_trea_bills(BigDecimal r113_crm_sub_col_elig_trea_bills) {
		this.r113_crm_sub_col_elig_trea_bills = r113_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR113_crm_sub_col_elig_deb_sec() {
		return r113_crm_sub_col_elig_deb_sec;
	}
	public void setR113_crm_sub_col_elig_deb_sec(BigDecimal r113_crm_sub_col_elig_deb_sec) {
		this.r113_crm_sub_col_elig_deb_sec = r113_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR113_crm_sub_col_elig_equi() {
		return r113_crm_sub_col_elig_equi;
	}
	public void setR113_crm_sub_col_elig_equi(BigDecimal r113_crm_sub_col_elig_equi) {
		this.r113_crm_sub_col_elig_equi = r113_crm_sub_col_elig_equi;
	}
	public BigDecimal getR113_crm_sub_col_elig_unit_tru() {
		return r113_crm_sub_col_elig_unit_tru;
	}
	public void setR113_crm_sub_col_elig_unit_tru(BigDecimal r113_crm_sub_col_elig_unit_tru) {
		this.r113_crm_sub_col_elig_unit_tru = r113_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR113_crm_sub_col_exp_cov() {
		return r113_crm_sub_col_exp_cov;
	}
	public void setR113_crm_sub_col_exp_cov(BigDecimal r113_crm_sub_col_exp_cov) {
		this.r113_crm_sub_col_exp_cov = r113_crm_sub_col_exp_cov;
	}
	public BigDecimal getR113_crm_sub_col_elig_exp_not_cov() {
		return r113_crm_sub_col_elig_exp_not_cov;
	}
	public void setR113_crm_sub_col_elig_exp_not_cov(BigDecimal r113_crm_sub_col_elig_exp_not_cov) {
		this.r113_crm_sub_col_elig_exp_not_cov = r113_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR113_crm_sub_rwa_ris_crm() {
		return r113_crm_sub_rwa_ris_crm;
	}
	public void setR113_crm_sub_rwa_ris_crm(BigDecimal r113_crm_sub_rwa_ris_crm) {
		this.r113_crm_sub_rwa_ris_crm = r113_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR113_crm_sub_rwa_cov_crm() {
		return r113_crm_sub_rwa_cov_crm;
	}
	public void setR113_crm_sub_rwa_cov_crm(BigDecimal r113_crm_sub_rwa_cov_crm) {
		this.r113_crm_sub_rwa_cov_crm = r113_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR113_crm_sub_rwa_org_cou() {
		return r113_crm_sub_rwa_org_cou;
	}
	public void setR113_crm_sub_rwa_org_cou(BigDecimal r113_crm_sub_rwa_org_cou) {
		this.r113_crm_sub_rwa_org_cou = r113_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR113_crm_sub_rwa_not_cov_crm() {
		return r113_crm_sub_rwa_not_cov_crm;
	}
	public void setR113_crm_sub_rwa_not_cov_crm(BigDecimal r113_crm_sub_rwa_not_cov_crm) {
		this.r113_crm_sub_rwa_not_cov_crm = r113_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR113_crm_comp_col_expo_elig() {
		return r113_crm_comp_col_expo_elig;
	}
	public void setR113_crm_comp_col_expo_elig(BigDecimal r113_crm_comp_col_expo_elig) {
		this.r113_crm_comp_col_expo_elig = r113_crm_comp_col_expo_elig;
	}
	public BigDecimal getR113_crm_comp_col_elig_expo_vol_adj() {
		return r113_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR113_crm_comp_col_elig_expo_vol_adj(BigDecimal r113_crm_comp_col_elig_expo_vol_adj) {
		this.r113_crm_comp_col_elig_expo_vol_adj = r113_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR113_crm_comp_col_elig_fin_hai() {
		return r113_crm_comp_col_elig_fin_hai;
	}
	public void setR113_crm_comp_col_elig_fin_hai(BigDecimal r113_crm_comp_col_elig_fin_hai) {
		this.r113_crm_comp_col_elig_fin_hai = r113_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR113_crm_comp_col_expo_val() {
		return r113_crm_comp_col_expo_val;
	}
	public void setR113_crm_comp_col_expo_val(BigDecimal r113_crm_comp_col_expo_val) {
		this.r113_crm_comp_col_expo_val = r113_crm_comp_col_expo_val;
	}
	public BigDecimal getR113_rwa_elig_expo_not_cov_crm() {
		return r113_rwa_elig_expo_not_cov_crm;
	}
	public void setR113_rwa_elig_expo_not_cov_crm(BigDecimal r113_rwa_elig_expo_not_cov_crm) {
		this.r113_rwa_elig_expo_not_cov_crm = r113_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR113_rwa_unsec_expo_cre_ris() {
		return r113_rwa_unsec_expo_cre_ris;
	}
	public void setR113_rwa_unsec_expo_cre_ris(BigDecimal r113_rwa_unsec_expo_cre_ris) {
		this.r113_rwa_unsec_expo_cre_ris = r113_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR113_rwa_unsec_expo() {
		return r113_rwa_unsec_expo;
	}
	public void setR113_rwa_unsec_expo(BigDecimal r113_rwa_unsec_expo) {
		this.r113_rwa_unsec_expo = r113_rwa_unsec_expo;
	}
	public BigDecimal getR113_rwa_tot_ris_wei_ass() {
		return r113_rwa_tot_ris_wei_ass;
	}
	public void setR113_rwa_tot_ris_wei_ass(BigDecimal r113_rwa_tot_ris_wei_ass) {
		this.r113_rwa_tot_ris_wei_ass = r113_rwa_tot_ris_wei_ass;
	}
	public String getR114_exposure_class() {
		return r114_exposure_class;
	}
	public void setR114_exposure_class(String r114_exposure_class) {
		this.r114_exposure_class = r114_exposure_class;
	}
	public BigDecimal getR114_expo_crm() {
		return r114_expo_crm;
	}
	public void setR114_expo_crm(BigDecimal r114_expo_crm) {
		this.r114_expo_crm = r114_expo_crm;
	}
	public BigDecimal getR114_spe_pro_expo() {
		return r114_spe_pro_expo;
	}
	public void setR114_spe_pro_expo(BigDecimal r114_spe_pro_expo) {
		this.r114_spe_pro_expo = r114_spe_pro_expo;
	}
	public BigDecimal getR114_amt_elig_sht_net() {
		return r114_amt_elig_sht_net;
	}
	public void setR114_amt_elig_sht_net(BigDecimal r114_amt_elig_sht_net) {
		this.r114_amt_elig_sht_net = r114_amt_elig_sht_net;
	}
	public BigDecimal getR114_tot_expo_net_spe() {
		return r114_tot_expo_net_spe;
	}
	public void setR114_tot_expo_net_spe(BigDecimal r114_tot_expo_net_spe) {
		this.r114_tot_expo_net_spe = r114_tot_expo_net_spe;
	}
	public BigDecimal getR114_crm_sub_elig_sub_app() {
		return r114_crm_sub_elig_sub_app;
	}
	public void setR114_crm_sub_elig_sub_app(BigDecimal r114_crm_sub_elig_sub_app) {
		this.r114_crm_sub_elig_sub_app = r114_crm_sub_elig_sub_app;
	}
	public BigDecimal getR114_crm_sub_non_col_guar() {
		return r114_crm_sub_non_col_guar;
	}
	public void setR114_crm_sub_non_col_guar(BigDecimal r114_crm_sub_non_col_guar) {
		this.r114_crm_sub_non_col_guar = r114_crm_sub_non_col_guar;
	}
	public BigDecimal getR114_crm_sub_non_col_cre_der() {
		return r114_crm_sub_non_col_cre_der;
	}
	public void setR114_crm_sub_non_col_cre_der(BigDecimal r114_crm_sub_non_col_cre_der) {
		this.r114_crm_sub_non_col_cre_der = r114_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR114_crm_sub_col_elig_cash() {
		return r114_crm_sub_col_elig_cash;
	}
	public void setR114_crm_sub_col_elig_cash(BigDecimal r114_crm_sub_col_elig_cash) {
		this.r114_crm_sub_col_elig_cash = r114_crm_sub_col_elig_cash;
	}
	public BigDecimal getR114_crm_sub_col_elig_trea_bills() {
		return r114_crm_sub_col_elig_trea_bills;
	}
	public void setR114_crm_sub_col_elig_trea_bills(BigDecimal r114_crm_sub_col_elig_trea_bills) {
		this.r114_crm_sub_col_elig_trea_bills = r114_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR114_crm_sub_col_elig_deb_sec() {
		return r114_crm_sub_col_elig_deb_sec;
	}
	public void setR114_crm_sub_col_elig_deb_sec(BigDecimal r114_crm_sub_col_elig_deb_sec) {
		this.r114_crm_sub_col_elig_deb_sec = r114_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR114_crm_sub_col_elig_equi() {
		return r114_crm_sub_col_elig_equi;
	}
	public void setR114_crm_sub_col_elig_equi(BigDecimal r114_crm_sub_col_elig_equi) {
		this.r114_crm_sub_col_elig_equi = r114_crm_sub_col_elig_equi;
	}
	public BigDecimal getR114_crm_sub_col_elig_unit_tru() {
		return r114_crm_sub_col_elig_unit_tru;
	}
	public void setR114_crm_sub_col_elig_unit_tru(BigDecimal r114_crm_sub_col_elig_unit_tru) {
		this.r114_crm_sub_col_elig_unit_tru = r114_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR114_crm_sub_col_exp_cov() {
		return r114_crm_sub_col_exp_cov;
	}
	public void setR114_crm_sub_col_exp_cov(BigDecimal r114_crm_sub_col_exp_cov) {
		this.r114_crm_sub_col_exp_cov = r114_crm_sub_col_exp_cov;
	}
	public BigDecimal getR114_crm_sub_col_elig_exp_not_cov() {
		return r114_crm_sub_col_elig_exp_not_cov;
	}
	public void setR114_crm_sub_col_elig_exp_not_cov(BigDecimal r114_crm_sub_col_elig_exp_not_cov) {
		this.r114_crm_sub_col_elig_exp_not_cov = r114_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR114_crm_sub_rwa_ris_crm() {
		return r114_crm_sub_rwa_ris_crm;
	}
	public void setR114_crm_sub_rwa_ris_crm(BigDecimal r114_crm_sub_rwa_ris_crm) {
		this.r114_crm_sub_rwa_ris_crm = r114_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR114_crm_sub_rwa_cov_crm() {
		return r114_crm_sub_rwa_cov_crm;
	}
	public void setR114_crm_sub_rwa_cov_crm(BigDecimal r114_crm_sub_rwa_cov_crm) {
		this.r114_crm_sub_rwa_cov_crm = r114_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR114_crm_sub_rwa_org_cou() {
		return r114_crm_sub_rwa_org_cou;
	}
	public void setR114_crm_sub_rwa_org_cou(BigDecimal r114_crm_sub_rwa_org_cou) {
		this.r114_crm_sub_rwa_org_cou = r114_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR114_crm_sub_rwa_not_cov_crm() {
		return r114_crm_sub_rwa_not_cov_crm;
	}
	public void setR114_crm_sub_rwa_not_cov_crm(BigDecimal r114_crm_sub_rwa_not_cov_crm) {
		this.r114_crm_sub_rwa_not_cov_crm = r114_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR114_crm_comp_col_expo_elig() {
		return r114_crm_comp_col_expo_elig;
	}
	public void setR114_crm_comp_col_expo_elig(BigDecimal r114_crm_comp_col_expo_elig) {
		this.r114_crm_comp_col_expo_elig = r114_crm_comp_col_expo_elig;
	}
	public BigDecimal getR114_crm_comp_col_elig_expo_vol_adj() {
		return r114_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR114_crm_comp_col_elig_expo_vol_adj(BigDecimal r114_crm_comp_col_elig_expo_vol_adj) {
		this.r114_crm_comp_col_elig_expo_vol_adj = r114_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR114_crm_comp_col_elig_fin_hai() {
		return r114_crm_comp_col_elig_fin_hai;
	}
	public void setR114_crm_comp_col_elig_fin_hai(BigDecimal r114_crm_comp_col_elig_fin_hai) {
		this.r114_crm_comp_col_elig_fin_hai = r114_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR114_crm_comp_col_expo_val() {
		return r114_crm_comp_col_expo_val;
	}
	public void setR114_crm_comp_col_expo_val(BigDecimal r114_crm_comp_col_expo_val) {
		this.r114_crm_comp_col_expo_val = r114_crm_comp_col_expo_val;
	}
	public BigDecimal getR114_rwa_elig_expo_not_cov_crm() {
		return r114_rwa_elig_expo_not_cov_crm;
	}
	public void setR114_rwa_elig_expo_not_cov_crm(BigDecimal r114_rwa_elig_expo_not_cov_crm) {
		this.r114_rwa_elig_expo_not_cov_crm = r114_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR114_rwa_unsec_expo_cre_ris() {
		return r114_rwa_unsec_expo_cre_ris;
	}
	public void setR114_rwa_unsec_expo_cre_ris(BigDecimal r114_rwa_unsec_expo_cre_ris) {
		this.r114_rwa_unsec_expo_cre_ris = r114_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR114_rwa_unsec_expo() {
		return r114_rwa_unsec_expo;
	}
	public void setR114_rwa_unsec_expo(BigDecimal r114_rwa_unsec_expo) {
		this.r114_rwa_unsec_expo = r114_rwa_unsec_expo;
	}
	public BigDecimal getR114_rwa_tot_ris_wei_ass() {
		return r114_rwa_tot_ris_wei_ass;
	}
	public void setR114_rwa_tot_ris_wei_ass(BigDecimal r114_rwa_tot_ris_wei_ass) {
		this.r114_rwa_tot_ris_wei_ass = r114_rwa_tot_ris_wei_ass;
	}
	public String getR117_exposure_class() {
		return r117_exposure_class;
	}
	public void setR117_exposure_class(String r117_exposure_class) {
		this.r117_exposure_class = r117_exposure_class;
	}
	public BigDecimal getR117_spe_pro_expo() {
		return r117_spe_pro_expo;
	}
	public void setR117_spe_pro_expo(BigDecimal r117_spe_pro_expo) {
		this.r117_spe_pro_expo = r117_spe_pro_expo;
	}
	public BigDecimal getR117_amt_elig_sht_net() {
		return r117_amt_elig_sht_net;
	}
	public void setR117_amt_elig_sht_net(BigDecimal r117_amt_elig_sht_net) {
		this.r117_amt_elig_sht_net = r117_amt_elig_sht_net;
	}
	public BigDecimal getR117_tot_expo_net_spe() {
		return r117_tot_expo_net_spe;
	}
	public void setR117_tot_expo_net_spe(BigDecimal r117_tot_expo_net_spe) {
		this.r117_tot_expo_net_spe = r117_tot_expo_net_spe;
	}
	public BigDecimal getR117_crm_sub_elig_sub_app() {
		return r117_crm_sub_elig_sub_app;
	}
	public void setR117_crm_sub_elig_sub_app(BigDecimal r117_crm_sub_elig_sub_app) {
		this.r117_crm_sub_elig_sub_app = r117_crm_sub_elig_sub_app;
	}
	public BigDecimal getR117_crm_sub_non_col_guar() {
		return r117_crm_sub_non_col_guar;
	}
	public void setR117_crm_sub_non_col_guar(BigDecimal r117_crm_sub_non_col_guar) {
		this.r117_crm_sub_non_col_guar = r117_crm_sub_non_col_guar;
	}
	public BigDecimal getR117_crm_sub_non_col_cre_der() {
		return r117_crm_sub_non_col_cre_der;
	}
	public void setR117_crm_sub_non_col_cre_der(BigDecimal r117_crm_sub_non_col_cre_der) {
		this.r117_crm_sub_non_col_cre_der = r117_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR117_crm_sub_col_elig_cash() {
		return r117_crm_sub_col_elig_cash;
	}
	public void setR117_crm_sub_col_elig_cash(BigDecimal r117_crm_sub_col_elig_cash) {
		this.r117_crm_sub_col_elig_cash = r117_crm_sub_col_elig_cash;
	}
	public BigDecimal getR117_crm_sub_col_elig_trea_bills() {
		return r117_crm_sub_col_elig_trea_bills;
	}
	public void setR117_crm_sub_col_elig_trea_bills(BigDecimal r117_crm_sub_col_elig_trea_bills) {
		this.r117_crm_sub_col_elig_trea_bills = r117_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR117_crm_sub_col_elig_deb_sec() {
		return r117_crm_sub_col_elig_deb_sec;
	}
	public void setR117_crm_sub_col_elig_deb_sec(BigDecimal r117_crm_sub_col_elig_deb_sec) {
		this.r117_crm_sub_col_elig_deb_sec = r117_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR117_crm_sub_col_elig_equi() {
		return r117_crm_sub_col_elig_equi;
	}
	public void setR117_crm_sub_col_elig_equi(BigDecimal r117_crm_sub_col_elig_equi) {
		this.r117_crm_sub_col_elig_equi = r117_crm_sub_col_elig_equi;
	}
	public BigDecimal getR117_crm_sub_col_elig_unit_tru() {
		return r117_crm_sub_col_elig_unit_tru;
	}
	public void setR117_crm_sub_col_elig_unit_tru(BigDecimal r117_crm_sub_col_elig_unit_tru) {
		this.r117_crm_sub_col_elig_unit_tru = r117_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR117_crm_sub_col_exp_cov() {
		return r117_crm_sub_col_exp_cov;
	}
	public void setR117_crm_sub_col_exp_cov(BigDecimal r117_crm_sub_col_exp_cov) {
		this.r117_crm_sub_col_exp_cov = r117_crm_sub_col_exp_cov;
	}
	public BigDecimal getR117_crm_sub_col_elig_exp_not_cov() {
		return r117_crm_sub_col_elig_exp_not_cov;
	}
	public void setR117_crm_sub_col_elig_exp_not_cov(BigDecimal r117_crm_sub_col_elig_exp_not_cov) {
		this.r117_crm_sub_col_elig_exp_not_cov = r117_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR117_crm_sub_rwa_ris_crm() {
		return r117_crm_sub_rwa_ris_crm;
	}
	public void setR117_crm_sub_rwa_ris_crm(BigDecimal r117_crm_sub_rwa_ris_crm) {
		this.r117_crm_sub_rwa_ris_crm = r117_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR117_crm_sub_rwa_cov_crm() {
		return r117_crm_sub_rwa_cov_crm;
	}
	public void setR117_crm_sub_rwa_cov_crm(BigDecimal r117_crm_sub_rwa_cov_crm) {
		this.r117_crm_sub_rwa_cov_crm = r117_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR117_crm_sub_rwa_org_cou() {
		return r117_crm_sub_rwa_org_cou;
	}
	public void setR117_crm_sub_rwa_org_cou(BigDecimal r117_crm_sub_rwa_org_cou) {
		this.r117_crm_sub_rwa_org_cou = r117_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR117_crm_sub_rwa_not_cov_crm() {
		return r117_crm_sub_rwa_not_cov_crm;
	}
	public void setR117_crm_sub_rwa_not_cov_crm(BigDecimal r117_crm_sub_rwa_not_cov_crm) {
		this.r117_crm_sub_rwa_not_cov_crm = r117_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR117_crm_comp_col_expo_elig() {
		return r117_crm_comp_col_expo_elig;
	}
	public void setR117_crm_comp_col_expo_elig(BigDecimal r117_crm_comp_col_expo_elig) {
		this.r117_crm_comp_col_expo_elig = r117_crm_comp_col_expo_elig;
	}
	public BigDecimal getR117_crm_comp_col_elig_expo_vol_adj() {
		return r117_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR117_crm_comp_col_elig_expo_vol_adj(BigDecimal r117_crm_comp_col_elig_expo_vol_adj) {
		this.r117_crm_comp_col_elig_expo_vol_adj = r117_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR117_crm_comp_col_elig_fin_hai() {
		return r117_crm_comp_col_elig_fin_hai;
	}
	public void setR117_crm_comp_col_elig_fin_hai(BigDecimal r117_crm_comp_col_elig_fin_hai) {
		this.r117_crm_comp_col_elig_fin_hai = r117_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR117_crm_comp_col_expo_val() {
		return r117_crm_comp_col_expo_val;
	}
	public void setR117_crm_comp_col_expo_val(BigDecimal r117_crm_comp_col_expo_val) {
		this.r117_crm_comp_col_expo_val = r117_crm_comp_col_expo_val;
	}
	public BigDecimal getR117_rwa_elig_expo_not_cov_crm() {
		return r117_rwa_elig_expo_not_cov_crm;
	}
	public void setR117_rwa_elig_expo_not_cov_crm(BigDecimal r117_rwa_elig_expo_not_cov_crm) {
		this.r117_rwa_elig_expo_not_cov_crm = r117_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR117_rwa_unsec_expo_cre_ris() {
		return r117_rwa_unsec_expo_cre_ris;
	}
	public void setR117_rwa_unsec_expo_cre_ris(BigDecimal r117_rwa_unsec_expo_cre_ris) {
		this.r117_rwa_unsec_expo_cre_ris = r117_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR117_rwa_unsec_expo() {
		return r117_rwa_unsec_expo;
	}
	public void setR117_rwa_unsec_expo(BigDecimal r117_rwa_unsec_expo) {
		this.r117_rwa_unsec_expo = r117_rwa_unsec_expo;
	}
	public BigDecimal getR117_rwa_tot_ris_wei_ass() {
		return r117_rwa_tot_ris_wei_ass;
	}
	public void setR117_rwa_tot_ris_wei_ass(BigDecimal r117_rwa_tot_ris_wei_ass) {
		this.r117_rwa_tot_ris_wei_ass = r117_rwa_tot_ris_wei_ass;
	}
	public String getR118_exposure_class() {
		return r118_exposure_class;
	}
	public void setR118_exposure_class(String r118_exposure_class) {
		this.r118_exposure_class = r118_exposure_class;
	}
	public BigDecimal getR118_expo_crm() {
		return r118_expo_crm;
	}
	public void setR118_expo_crm(BigDecimal r118_expo_crm) {
		this.r118_expo_crm = r118_expo_crm;
	}
	public BigDecimal getR118_spe_pro_expo() {
		return r118_spe_pro_expo;
	}
	public void setR118_spe_pro_expo(BigDecimal r118_spe_pro_expo) {
		this.r118_spe_pro_expo = r118_spe_pro_expo;
	}
	public BigDecimal getR118_amt_elig_sht_net() {
		return r118_amt_elig_sht_net;
	}
	public void setR118_amt_elig_sht_net(BigDecimal r118_amt_elig_sht_net) {
		this.r118_amt_elig_sht_net = r118_amt_elig_sht_net;
	}
	public BigDecimal getR118_tot_expo_net_spe() {
		return r118_tot_expo_net_spe;
	}
	public void setR118_tot_expo_net_spe(BigDecimal r118_tot_expo_net_spe) {
		this.r118_tot_expo_net_spe = r118_tot_expo_net_spe;
	}
	public BigDecimal getR118_crm_sub_elig_sub_app() {
		return r118_crm_sub_elig_sub_app;
	}
	public void setR118_crm_sub_elig_sub_app(BigDecimal r118_crm_sub_elig_sub_app) {
		this.r118_crm_sub_elig_sub_app = r118_crm_sub_elig_sub_app;
	}
	public BigDecimal getR118_crm_sub_non_col_guar() {
		return r118_crm_sub_non_col_guar;
	}
	public void setR118_crm_sub_non_col_guar(BigDecimal r118_crm_sub_non_col_guar) {
		this.r118_crm_sub_non_col_guar = r118_crm_sub_non_col_guar;
	}
	public BigDecimal getR118_crm_sub_non_col_cre_der() {
		return r118_crm_sub_non_col_cre_der;
	}
	public void setR118_crm_sub_non_col_cre_der(BigDecimal r118_crm_sub_non_col_cre_der) {
		this.r118_crm_sub_non_col_cre_der = r118_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR118_crm_sub_col_elig_cash() {
		return r118_crm_sub_col_elig_cash;
	}
	public void setR118_crm_sub_col_elig_cash(BigDecimal r118_crm_sub_col_elig_cash) {
		this.r118_crm_sub_col_elig_cash = r118_crm_sub_col_elig_cash;
	}
	public BigDecimal getR118_crm_sub_col_elig_trea_bills() {
		return r118_crm_sub_col_elig_trea_bills;
	}
	public void setR118_crm_sub_col_elig_trea_bills(BigDecimal r118_crm_sub_col_elig_trea_bills) {
		this.r118_crm_sub_col_elig_trea_bills = r118_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR118_crm_sub_col_elig_deb_sec() {
		return r118_crm_sub_col_elig_deb_sec;
	}
	public void setR118_crm_sub_col_elig_deb_sec(BigDecimal r118_crm_sub_col_elig_deb_sec) {
		this.r118_crm_sub_col_elig_deb_sec = r118_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR118_crm_sub_col_elig_equi() {
		return r118_crm_sub_col_elig_equi;
	}
	public void setR118_crm_sub_col_elig_equi(BigDecimal r118_crm_sub_col_elig_equi) {
		this.r118_crm_sub_col_elig_equi = r118_crm_sub_col_elig_equi;
	}
	public BigDecimal getR118_crm_sub_col_elig_unit_tru() {
		return r118_crm_sub_col_elig_unit_tru;
	}
	public void setR118_crm_sub_col_elig_unit_tru(BigDecimal r118_crm_sub_col_elig_unit_tru) {
		this.r118_crm_sub_col_elig_unit_tru = r118_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR118_crm_sub_col_exp_cov() {
		return r118_crm_sub_col_exp_cov;
	}
	public void setR118_crm_sub_col_exp_cov(BigDecimal r118_crm_sub_col_exp_cov) {
		this.r118_crm_sub_col_exp_cov = r118_crm_sub_col_exp_cov;
	}
	public BigDecimal getR118_crm_sub_col_elig_exp_not_cov() {
		return r118_crm_sub_col_elig_exp_not_cov;
	}
	public void setR118_crm_sub_col_elig_exp_not_cov(BigDecimal r118_crm_sub_col_elig_exp_not_cov) {
		this.r118_crm_sub_col_elig_exp_not_cov = r118_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR118_crm_sub_rwa_ris_crm() {
		return r118_crm_sub_rwa_ris_crm;
	}
	public void setR118_crm_sub_rwa_ris_crm(BigDecimal r118_crm_sub_rwa_ris_crm) {
		this.r118_crm_sub_rwa_ris_crm = r118_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR118_crm_sub_rwa_cov_crm() {
		return r118_crm_sub_rwa_cov_crm;
	}
	public void setR118_crm_sub_rwa_cov_crm(BigDecimal r118_crm_sub_rwa_cov_crm) {
		this.r118_crm_sub_rwa_cov_crm = r118_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR118_crm_sub_rwa_org_cou() {
		return r118_crm_sub_rwa_org_cou;
	}
	public void setR118_crm_sub_rwa_org_cou(BigDecimal r118_crm_sub_rwa_org_cou) {
		this.r118_crm_sub_rwa_org_cou = r118_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR118_crm_sub_rwa_not_cov_crm() {
		return r118_crm_sub_rwa_not_cov_crm;
	}
	public void setR118_crm_sub_rwa_not_cov_crm(BigDecimal r118_crm_sub_rwa_not_cov_crm) {
		this.r118_crm_sub_rwa_not_cov_crm = r118_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR118_crm_comp_col_expo_elig() {
		return r118_crm_comp_col_expo_elig;
	}
	public void setR118_crm_comp_col_expo_elig(BigDecimal r118_crm_comp_col_expo_elig) {
		this.r118_crm_comp_col_expo_elig = r118_crm_comp_col_expo_elig;
	}
	public BigDecimal getR118_crm_comp_col_elig_expo_vol_adj() {
		return r118_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR118_crm_comp_col_elig_expo_vol_adj(BigDecimal r118_crm_comp_col_elig_expo_vol_adj) {
		this.r118_crm_comp_col_elig_expo_vol_adj = r118_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR118_crm_comp_col_elig_fin_hai() {
		return r118_crm_comp_col_elig_fin_hai;
	}
	public void setR118_crm_comp_col_elig_fin_hai(BigDecimal r118_crm_comp_col_elig_fin_hai) {
		this.r118_crm_comp_col_elig_fin_hai = r118_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR118_crm_comp_col_expo_val() {
		return r118_crm_comp_col_expo_val;
	}
	public void setR118_crm_comp_col_expo_val(BigDecimal r118_crm_comp_col_expo_val) {
		this.r118_crm_comp_col_expo_val = r118_crm_comp_col_expo_val;
	}
	public BigDecimal getR118_rwa_elig_expo_not_cov_crm() {
		return r118_rwa_elig_expo_not_cov_crm;
	}
	public void setR118_rwa_elig_expo_not_cov_crm(BigDecimal r118_rwa_elig_expo_not_cov_crm) {
		this.r118_rwa_elig_expo_not_cov_crm = r118_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR118_rwa_unsec_expo_cre_ris() {
		return r118_rwa_unsec_expo_cre_ris;
	}
	public void setR118_rwa_unsec_expo_cre_ris(BigDecimal r118_rwa_unsec_expo_cre_ris) {
		this.r118_rwa_unsec_expo_cre_ris = r118_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR118_rwa_unsec_expo() {
		return r118_rwa_unsec_expo;
	}
	public void setR118_rwa_unsec_expo(BigDecimal r118_rwa_unsec_expo) {
		this.r118_rwa_unsec_expo = r118_rwa_unsec_expo;
	}
	public BigDecimal getR118_rwa_tot_ris_wei_ass() {
		return r118_rwa_tot_ris_wei_ass;
	}
	public void setR118_rwa_tot_ris_wei_ass(BigDecimal r118_rwa_tot_ris_wei_ass) {
		this.r118_rwa_tot_ris_wei_ass = r118_rwa_tot_ris_wei_ass;
	}
	public String getR119_exposure_class() {
		return r119_exposure_class;
	}
	public void setR119_exposure_class(String r119_exposure_class) {
		this.r119_exposure_class = r119_exposure_class;
	}
	public BigDecimal getR119_expo_crm() {
		return r119_expo_crm;
	}
	public void setR119_expo_crm(BigDecimal r119_expo_crm) {
		this.r119_expo_crm = r119_expo_crm;
	}
	public BigDecimal getR119_spe_pro_expo() {
		return r119_spe_pro_expo;
	}
	public void setR119_spe_pro_expo(BigDecimal r119_spe_pro_expo) {
		this.r119_spe_pro_expo = r119_spe_pro_expo;
	}
	public BigDecimal getR119_amt_elig_sht_net() {
		return r119_amt_elig_sht_net;
	}
	public void setR119_amt_elig_sht_net(BigDecimal r119_amt_elig_sht_net) {
		this.r119_amt_elig_sht_net = r119_amt_elig_sht_net;
	}
	public BigDecimal getR119_tot_expo_net_spe() {
		return r119_tot_expo_net_spe;
	}
	public void setR119_tot_expo_net_spe(BigDecimal r119_tot_expo_net_spe) {
		this.r119_tot_expo_net_spe = r119_tot_expo_net_spe;
	}
	public BigDecimal getR119_crm_sub_elig_sub_app() {
		return r119_crm_sub_elig_sub_app;
	}
	public void setR119_crm_sub_elig_sub_app(BigDecimal r119_crm_sub_elig_sub_app) {
		this.r119_crm_sub_elig_sub_app = r119_crm_sub_elig_sub_app;
	}
	public BigDecimal getR119_crm_sub_non_col_guar() {
		return r119_crm_sub_non_col_guar;
	}
	public void setR119_crm_sub_non_col_guar(BigDecimal r119_crm_sub_non_col_guar) {
		this.r119_crm_sub_non_col_guar = r119_crm_sub_non_col_guar;
	}
	public BigDecimal getR119_crm_sub_non_col_cre_der() {
		return r119_crm_sub_non_col_cre_der;
	}
	public void setR119_crm_sub_non_col_cre_der(BigDecimal r119_crm_sub_non_col_cre_der) {
		this.r119_crm_sub_non_col_cre_der = r119_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR119_crm_sub_col_elig_cash() {
		return r119_crm_sub_col_elig_cash;
	}
	public void setR119_crm_sub_col_elig_cash(BigDecimal r119_crm_sub_col_elig_cash) {
		this.r119_crm_sub_col_elig_cash = r119_crm_sub_col_elig_cash;
	}
	public BigDecimal getR119_crm_sub_col_elig_trea_bills() {
		return r119_crm_sub_col_elig_trea_bills;
	}
	public void setR119_crm_sub_col_elig_trea_bills(BigDecimal r119_crm_sub_col_elig_trea_bills) {
		this.r119_crm_sub_col_elig_trea_bills = r119_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR119_crm_sub_col_elig_deb_sec() {
		return r119_crm_sub_col_elig_deb_sec;
	}
	public void setR119_crm_sub_col_elig_deb_sec(BigDecimal r119_crm_sub_col_elig_deb_sec) {
		this.r119_crm_sub_col_elig_deb_sec = r119_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR119_crm_sub_col_elig_equi() {
		return r119_crm_sub_col_elig_equi;
	}
	public void setR119_crm_sub_col_elig_equi(BigDecimal r119_crm_sub_col_elig_equi) {
		this.r119_crm_sub_col_elig_equi = r119_crm_sub_col_elig_equi;
	}
	public BigDecimal getR119_crm_sub_col_elig_unit_tru() {
		return r119_crm_sub_col_elig_unit_tru;
	}
	public void setR119_crm_sub_col_elig_unit_tru(BigDecimal r119_crm_sub_col_elig_unit_tru) {
		this.r119_crm_sub_col_elig_unit_tru = r119_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR119_crm_sub_col_exp_cov() {
		return r119_crm_sub_col_exp_cov;
	}
	public void setR119_crm_sub_col_exp_cov(BigDecimal r119_crm_sub_col_exp_cov) {
		this.r119_crm_sub_col_exp_cov = r119_crm_sub_col_exp_cov;
	}
	public BigDecimal getR119_crm_sub_col_elig_exp_not_cov() {
		return r119_crm_sub_col_elig_exp_not_cov;
	}
	public void setR119_crm_sub_col_elig_exp_not_cov(BigDecimal r119_crm_sub_col_elig_exp_not_cov) {
		this.r119_crm_sub_col_elig_exp_not_cov = r119_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR119_crm_sub_rwa_ris_crm() {
		return r119_crm_sub_rwa_ris_crm;
	}
	public void setR119_crm_sub_rwa_ris_crm(BigDecimal r119_crm_sub_rwa_ris_crm) {
		this.r119_crm_sub_rwa_ris_crm = r119_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR119_crm_sub_rwa_cov_crm() {
		return r119_crm_sub_rwa_cov_crm;
	}
	public void setR119_crm_sub_rwa_cov_crm(BigDecimal r119_crm_sub_rwa_cov_crm) {
		this.r119_crm_sub_rwa_cov_crm = r119_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR119_crm_sub_rwa_org_cou() {
		return r119_crm_sub_rwa_org_cou;
	}
	public void setR119_crm_sub_rwa_org_cou(BigDecimal r119_crm_sub_rwa_org_cou) {
		this.r119_crm_sub_rwa_org_cou = r119_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR119_crm_sub_rwa_not_cov_crm() {
		return r119_crm_sub_rwa_not_cov_crm;
	}
	public void setR119_crm_sub_rwa_not_cov_crm(BigDecimal r119_crm_sub_rwa_not_cov_crm) {
		this.r119_crm_sub_rwa_not_cov_crm = r119_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR119_crm_comp_col_expo_elig() {
		return r119_crm_comp_col_expo_elig;
	}
	public void setR119_crm_comp_col_expo_elig(BigDecimal r119_crm_comp_col_expo_elig) {
		this.r119_crm_comp_col_expo_elig = r119_crm_comp_col_expo_elig;
	}
	public BigDecimal getR119_crm_comp_col_elig_expo_vol_adj() {
		return r119_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR119_crm_comp_col_elig_expo_vol_adj(BigDecimal r119_crm_comp_col_elig_expo_vol_adj) {
		this.r119_crm_comp_col_elig_expo_vol_adj = r119_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR119_crm_comp_col_elig_fin_hai() {
		return r119_crm_comp_col_elig_fin_hai;
	}
	public void setR119_crm_comp_col_elig_fin_hai(BigDecimal r119_crm_comp_col_elig_fin_hai) {
		this.r119_crm_comp_col_elig_fin_hai = r119_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR119_crm_comp_col_expo_val() {
		return r119_crm_comp_col_expo_val;
	}
	public void setR119_crm_comp_col_expo_val(BigDecimal r119_crm_comp_col_expo_val) {
		this.r119_crm_comp_col_expo_val = r119_crm_comp_col_expo_val;
	}
	public BigDecimal getR119_rwa_elig_expo_not_cov_crm() {
		return r119_rwa_elig_expo_not_cov_crm;
	}
	public void setR119_rwa_elig_expo_not_cov_crm(BigDecimal r119_rwa_elig_expo_not_cov_crm) {
		this.r119_rwa_elig_expo_not_cov_crm = r119_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR119_rwa_unsec_expo_cre_ris() {
		return r119_rwa_unsec_expo_cre_ris;
	}
	public void setR119_rwa_unsec_expo_cre_ris(BigDecimal r119_rwa_unsec_expo_cre_ris) {
		this.r119_rwa_unsec_expo_cre_ris = r119_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR119_rwa_unsec_expo() {
		return r119_rwa_unsec_expo;
	}
	public void setR119_rwa_unsec_expo(BigDecimal r119_rwa_unsec_expo) {
		this.r119_rwa_unsec_expo = r119_rwa_unsec_expo;
	}
	public BigDecimal getR119_rwa_tot_ris_wei_ass() {
		return r119_rwa_tot_ris_wei_ass;
	}
	public void setR119_rwa_tot_ris_wei_ass(BigDecimal r119_rwa_tot_ris_wei_ass) {
		this.r119_rwa_tot_ris_wei_ass = r119_rwa_tot_ris_wei_ass;
	}
	public String getR120_exposure_class() {
		return r120_exposure_class;
	}
	public void setR120_exposure_class(String r120_exposure_class) {
		this.r120_exposure_class = r120_exposure_class;
	}
	public BigDecimal getR120_expo_crm() {
		return r120_expo_crm;
	}
	public void setR120_expo_crm(BigDecimal r120_expo_crm) {
		this.r120_expo_crm = r120_expo_crm;
	}
	public BigDecimal getR120_spe_pro_expo() {
		return r120_spe_pro_expo;
	}
	public void setR120_spe_pro_expo(BigDecimal r120_spe_pro_expo) {
		this.r120_spe_pro_expo = r120_spe_pro_expo;
	}
	public BigDecimal getR120_amt_elig_sht_net() {
		return r120_amt_elig_sht_net;
	}
	public void setR120_amt_elig_sht_net(BigDecimal r120_amt_elig_sht_net) {
		this.r120_amt_elig_sht_net = r120_amt_elig_sht_net;
	}
	public BigDecimal getR120_tot_expo_net_spe() {
		return r120_tot_expo_net_spe;
	}
	public void setR120_tot_expo_net_spe(BigDecimal r120_tot_expo_net_spe) {
		this.r120_tot_expo_net_spe = r120_tot_expo_net_spe;
	}
	public BigDecimal getR120_crm_sub_elig_sub_app() {
		return r120_crm_sub_elig_sub_app;
	}
	public void setR120_crm_sub_elig_sub_app(BigDecimal r120_crm_sub_elig_sub_app) {
		this.r120_crm_sub_elig_sub_app = r120_crm_sub_elig_sub_app;
	}
	public BigDecimal getR120_crm_sub_non_col_guar() {
		return r120_crm_sub_non_col_guar;
	}
	public void setR120_crm_sub_non_col_guar(BigDecimal r120_crm_sub_non_col_guar) {
		this.r120_crm_sub_non_col_guar = r120_crm_sub_non_col_guar;
	}
	public BigDecimal getR120_crm_sub_non_col_cre_der() {
		return r120_crm_sub_non_col_cre_der;
	}
	public void setR120_crm_sub_non_col_cre_der(BigDecimal r120_crm_sub_non_col_cre_der) {
		this.r120_crm_sub_non_col_cre_der = r120_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR120_crm_sub_col_elig_cash() {
		return r120_crm_sub_col_elig_cash;
	}
	public void setR120_crm_sub_col_elig_cash(BigDecimal r120_crm_sub_col_elig_cash) {
		this.r120_crm_sub_col_elig_cash = r120_crm_sub_col_elig_cash;
	}
	public BigDecimal getR120_crm_sub_col_elig_trea_bills() {
		return r120_crm_sub_col_elig_trea_bills;
	}
	public void setR120_crm_sub_col_elig_trea_bills(BigDecimal r120_crm_sub_col_elig_trea_bills) {
		this.r120_crm_sub_col_elig_trea_bills = r120_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR120_crm_sub_col_elig_deb_sec() {
		return r120_crm_sub_col_elig_deb_sec;
	}
	public void setR120_crm_sub_col_elig_deb_sec(BigDecimal r120_crm_sub_col_elig_deb_sec) {
		this.r120_crm_sub_col_elig_deb_sec = r120_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR120_crm_sub_col_elig_equi() {
		return r120_crm_sub_col_elig_equi;
	}
	public void setR120_crm_sub_col_elig_equi(BigDecimal r120_crm_sub_col_elig_equi) {
		this.r120_crm_sub_col_elig_equi = r120_crm_sub_col_elig_equi;
	}
	public BigDecimal getR120_crm_sub_col_elig_unit_tru() {
		return r120_crm_sub_col_elig_unit_tru;
	}
	public void setR120_crm_sub_col_elig_unit_tru(BigDecimal r120_crm_sub_col_elig_unit_tru) {
		this.r120_crm_sub_col_elig_unit_tru = r120_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR120_crm_sub_col_exp_cov() {
		return r120_crm_sub_col_exp_cov;
	}
	public void setR120_crm_sub_col_exp_cov(BigDecimal r120_crm_sub_col_exp_cov) {
		this.r120_crm_sub_col_exp_cov = r120_crm_sub_col_exp_cov;
	}
	public BigDecimal getR120_crm_sub_col_elig_exp_not_cov() {
		return r120_crm_sub_col_elig_exp_not_cov;
	}
	public void setR120_crm_sub_col_elig_exp_not_cov(BigDecimal r120_crm_sub_col_elig_exp_not_cov) {
		this.r120_crm_sub_col_elig_exp_not_cov = r120_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR120_crm_sub_rwa_ris_crm() {
		return r120_crm_sub_rwa_ris_crm;
	}
	public void setR120_crm_sub_rwa_ris_crm(BigDecimal r120_crm_sub_rwa_ris_crm) {
		this.r120_crm_sub_rwa_ris_crm = r120_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR120_crm_sub_rwa_cov_crm() {
		return r120_crm_sub_rwa_cov_crm;
	}
	public void setR120_crm_sub_rwa_cov_crm(BigDecimal r120_crm_sub_rwa_cov_crm) {
		this.r120_crm_sub_rwa_cov_crm = r120_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR120_crm_sub_rwa_org_cou() {
		return r120_crm_sub_rwa_org_cou;
	}
	public void setR120_crm_sub_rwa_org_cou(BigDecimal r120_crm_sub_rwa_org_cou) {
		this.r120_crm_sub_rwa_org_cou = r120_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR120_crm_sub_rwa_not_cov_crm() {
		return r120_crm_sub_rwa_not_cov_crm;
	}
	public void setR120_crm_sub_rwa_not_cov_crm(BigDecimal r120_crm_sub_rwa_not_cov_crm) {
		this.r120_crm_sub_rwa_not_cov_crm = r120_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR120_crm_comp_col_expo_elig() {
		return r120_crm_comp_col_expo_elig;
	}
	public void setR120_crm_comp_col_expo_elig(BigDecimal r120_crm_comp_col_expo_elig) {
		this.r120_crm_comp_col_expo_elig = r120_crm_comp_col_expo_elig;
	}
	public BigDecimal getR120_crm_comp_col_elig_expo_vol_adj() {
		return r120_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR120_crm_comp_col_elig_expo_vol_adj(BigDecimal r120_crm_comp_col_elig_expo_vol_adj) {
		this.r120_crm_comp_col_elig_expo_vol_adj = r120_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR120_crm_comp_col_elig_fin_hai() {
		return r120_crm_comp_col_elig_fin_hai;
	}
	public void setR120_crm_comp_col_elig_fin_hai(BigDecimal r120_crm_comp_col_elig_fin_hai) {
		this.r120_crm_comp_col_elig_fin_hai = r120_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR120_crm_comp_col_expo_val() {
		return r120_crm_comp_col_expo_val;
	}
	public void setR120_crm_comp_col_expo_val(BigDecimal r120_crm_comp_col_expo_val) {
		this.r120_crm_comp_col_expo_val = r120_crm_comp_col_expo_val;
	}
	public BigDecimal getR120_rwa_elig_expo_not_cov_crm() {
		return r120_rwa_elig_expo_not_cov_crm;
	}
	public void setR120_rwa_elig_expo_not_cov_crm(BigDecimal r120_rwa_elig_expo_not_cov_crm) {
		this.r120_rwa_elig_expo_not_cov_crm = r120_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR120_rwa_unsec_expo_cre_ris() {
		return r120_rwa_unsec_expo_cre_ris;
	}
	public void setR120_rwa_unsec_expo_cre_ris(BigDecimal r120_rwa_unsec_expo_cre_ris) {
		this.r120_rwa_unsec_expo_cre_ris = r120_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR120_rwa_unsec_expo() {
		return r120_rwa_unsec_expo;
	}
	public void setR120_rwa_unsec_expo(BigDecimal r120_rwa_unsec_expo) {
		this.r120_rwa_unsec_expo = r120_rwa_unsec_expo;
	}
	public BigDecimal getR120_rwa_tot_ris_wei_ass() {
		return r120_rwa_tot_ris_wei_ass;
	}
	public void setR120_rwa_tot_ris_wei_ass(BigDecimal r120_rwa_tot_ris_wei_ass) {
		this.r120_rwa_tot_ris_wei_ass = r120_rwa_tot_ris_wei_ass;
	}
	public String getR121_exposure_class() {
		return r121_exposure_class;
	}
	public void setR121_exposure_class(String r121_exposure_class) {
		this.r121_exposure_class = r121_exposure_class;
	}
	public BigDecimal getR121_expo_crm() {
		return r121_expo_crm;
	}
	public void setR121_expo_crm(BigDecimal r121_expo_crm) {
		this.r121_expo_crm = r121_expo_crm;
	}
	public BigDecimal getR121_spe_pro_expo() {
		return r121_spe_pro_expo;
	}
	public void setR121_spe_pro_expo(BigDecimal r121_spe_pro_expo) {
		this.r121_spe_pro_expo = r121_spe_pro_expo;
	}
	public BigDecimal getR121_amt_elig_sht_net() {
		return r121_amt_elig_sht_net;
	}
	public void setR121_amt_elig_sht_net(BigDecimal r121_amt_elig_sht_net) {
		this.r121_amt_elig_sht_net = r121_amt_elig_sht_net;
	}
	public BigDecimal getR121_tot_expo_net_spe() {
		return r121_tot_expo_net_spe;
	}
	public void setR121_tot_expo_net_spe(BigDecimal r121_tot_expo_net_spe) {
		this.r121_tot_expo_net_spe = r121_tot_expo_net_spe;
	}
	public BigDecimal getR121_crm_sub_elig_sub_app() {
		return r121_crm_sub_elig_sub_app;
	}
	public void setR121_crm_sub_elig_sub_app(BigDecimal r121_crm_sub_elig_sub_app) {
		this.r121_crm_sub_elig_sub_app = r121_crm_sub_elig_sub_app;
	}
	public BigDecimal getR121_crm_sub_non_col_guar() {
		return r121_crm_sub_non_col_guar;
	}
	public void setR121_crm_sub_non_col_guar(BigDecimal r121_crm_sub_non_col_guar) {
		this.r121_crm_sub_non_col_guar = r121_crm_sub_non_col_guar;
	}
	public BigDecimal getR121_crm_sub_non_col_cre_der() {
		return r121_crm_sub_non_col_cre_der;
	}
	public void setR121_crm_sub_non_col_cre_der(BigDecimal r121_crm_sub_non_col_cre_der) {
		this.r121_crm_sub_non_col_cre_der = r121_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR121_crm_sub_col_elig_cash() {
		return r121_crm_sub_col_elig_cash;
	}
	public void setR121_crm_sub_col_elig_cash(BigDecimal r121_crm_sub_col_elig_cash) {
		this.r121_crm_sub_col_elig_cash = r121_crm_sub_col_elig_cash;
	}
	public BigDecimal getR121_crm_sub_col_elig_trea_bills() {
		return r121_crm_sub_col_elig_trea_bills;
	}
	public void setR121_crm_sub_col_elig_trea_bills(BigDecimal r121_crm_sub_col_elig_trea_bills) {
		this.r121_crm_sub_col_elig_trea_bills = r121_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR121_crm_sub_col_elig_deb_sec() {
		return r121_crm_sub_col_elig_deb_sec;
	}
	public void setR121_crm_sub_col_elig_deb_sec(BigDecimal r121_crm_sub_col_elig_deb_sec) {
		this.r121_crm_sub_col_elig_deb_sec = r121_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR121_crm_sub_col_elig_equi() {
		return r121_crm_sub_col_elig_equi;
	}
	public void setR121_crm_sub_col_elig_equi(BigDecimal r121_crm_sub_col_elig_equi) {
		this.r121_crm_sub_col_elig_equi = r121_crm_sub_col_elig_equi;
	}
	public BigDecimal getR121_crm_sub_col_elig_unit_tru() {
		return r121_crm_sub_col_elig_unit_tru;
	}
	public void setR121_crm_sub_col_elig_unit_tru(BigDecimal r121_crm_sub_col_elig_unit_tru) {
		this.r121_crm_sub_col_elig_unit_tru = r121_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR121_crm_sub_col_exp_cov() {
		return r121_crm_sub_col_exp_cov;
	}
	public void setR121_crm_sub_col_exp_cov(BigDecimal r121_crm_sub_col_exp_cov) {
		this.r121_crm_sub_col_exp_cov = r121_crm_sub_col_exp_cov;
	}
	public BigDecimal getR121_crm_sub_col_elig_exp_not_cov() {
		return r121_crm_sub_col_elig_exp_not_cov;
	}
	public void setR121_crm_sub_col_elig_exp_not_cov(BigDecimal r121_crm_sub_col_elig_exp_not_cov) {
		this.r121_crm_sub_col_elig_exp_not_cov = r121_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR121_crm_sub_rwa_ris_crm() {
		return r121_crm_sub_rwa_ris_crm;
	}
	public void setR121_crm_sub_rwa_ris_crm(BigDecimal r121_crm_sub_rwa_ris_crm) {
		this.r121_crm_sub_rwa_ris_crm = r121_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR121_crm_sub_rwa_cov_crm() {
		return r121_crm_sub_rwa_cov_crm;
	}
	public void setR121_crm_sub_rwa_cov_crm(BigDecimal r121_crm_sub_rwa_cov_crm) {
		this.r121_crm_sub_rwa_cov_crm = r121_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR121_crm_sub_rwa_org_cou() {
		return r121_crm_sub_rwa_org_cou;
	}
	public void setR121_crm_sub_rwa_org_cou(BigDecimal r121_crm_sub_rwa_org_cou) {
		this.r121_crm_sub_rwa_org_cou = r121_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR121_crm_sub_rwa_not_cov_crm() {
		return r121_crm_sub_rwa_not_cov_crm;
	}
	public void setR121_crm_sub_rwa_not_cov_crm(BigDecimal r121_crm_sub_rwa_not_cov_crm) {
		this.r121_crm_sub_rwa_not_cov_crm = r121_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR121_crm_comp_col_expo_elig() {
		return r121_crm_comp_col_expo_elig;
	}
	public void setR121_crm_comp_col_expo_elig(BigDecimal r121_crm_comp_col_expo_elig) {
		this.r121_crm_comp_col_expo_elig = r121_crm_comp_col_expo_elig;
	}
	public BigDecimal getR121_crm_comp_col_elig_expo_vol_adj() {
		return r121_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR121_crm_comp_col_elig_expo_vol_adj(BigDecimal r121_crm_comp_col_elig_expo_vol_adj) {
		this.r121_crm_comp_col_elig_expo_vol_adj = r121_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR121_crm_comp_col_elig_fin_hai() {
		return r121_crm_comp_col_elig_fin_hai;
	}
	public void setR121_crm_comp_col_elig_fin_hai(BigDecimal r121_crm_comp_col_elig_fin_hai) {
		this.r121_crm_comp_col_elig_fin_hai = r121_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR121_crm_comp_col_expo_val() {
		return r121_crm_comp_col_expo_val;
	}
	public void setR121_crm_comp_col_expo_val(BigDecimal r121_crm_comp_col_expo_val) {
		this.r121_crm_comp_col_expo_val = r121_crm_comp_col_expo_val;
	}
	public BigDecimal getR121_rwa_elig_expo_not_cov_crm() {
		return r121_rwa_elig_expo_not_cov_crm;
	}
	public void setR121_rwa_elig_expo_not_cov_crm(BigDecimal r121_rwa_elig_expo_not_cov_crm) {
		this.r121_rwa_elig_expo_not_cov_crm = r121_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR121_rwa_unsec_expo_cre_ris() {
		return r121_rwa_unsec_expo_cre_ris;
	}
	public void setR121_rwa_unsec_expo_cre_ris(BigDecimal r121_rwa_unsec_expo_cre_ris) {
		this.r121_rwa_unsec_expo_cre_ris = r121_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR121_rwa_unsec_expo() {
		return r121_rwa_unsec_expo;
	}
	public void setR121_rwa_unsec_expo(BigDecimal r121_rwa_unsec_expo) {
		this.r121_rwa_unsec_expo = r121_rwa_unsec_expo;
	}
	public BigDecimal getR121_rwa_tot_ris_wei_ass() {
		return r121_rwa_tot_ris_wei_ass;
	}
	public void setR121_rwa_tot_ris_wei_ass(BigDecimal r121_rwa_tot_ris_wei_ass) {
		this.r121_rwa_tot_ris_wei_ass = r121_rwa_tot_ris_wei_ass;
	}
	public String getR122_exposure_class() {
		return r122_exposure_class;
	}
	public void setR122_exposure_class(String r122_exposure_class) {
		this.r122_exposure_class = r122_exposure_class;
	}
	public BigDecimal getR122_expo_crm() {
		return r122_expo_crm;
	}
	public void setR122_expo_crm(BigDecimal r122_expo_crm) {
		this.r122_expo_crm = r122_expo_crm;
	}
	public BigDecimal getR122_spe_pro_expo() {
		return r122_spe_pro_expo;
	}
	public void setR122_spe_pro_expo(BigDecimal r122_spe_pro_expo) {
		this.r122_spe_pro_expo = r122_spe_pro_expo;
	}
	public BigDecimal getR122_amt_elig_sht_net() {
		return r122_amt_elig_sht_net;
	}
	public void setR122_amt_elig_sht_net(BigDecimal r122_amt_elig_sht_net) {
		this.r122_amt_elig_sht_net = r122_amt_elig_sht_net;
	}
	public BigDecimal getR122_tot_expo_net_spe() {
		return r122_tot_expo_net_spe;
	}
	public void setR122_tot_expo_net_spe(BigDecimal r122_tot_expo_net_spe) {
		this.r122_tot_expo_net_spe = r122_tot_expo_net_spe;
	}
	public BigDecimal getR122_crm_sub_elig_sub_app() {
		return r122_crm_sub_elig_sub_app;
	}
	public void setR122_crm_sub_elig_sub_app(BigDecimal r122_crm_sub_elig_sub_app) {
		this.r122_crm_sub_elig_sub_app = r122_crm_sub_elig_sub_app;
	}
	public BigDecimal getR122_crm_sub_non_col_guar() {
		return r122_crm_sub_non_col_guar;
	}
	public void setR122_crm_sub_non_col_guar(BigDecimal r122_crm_sub_non_col_guar) {
		this.r122_crm_sub_non_col_guar = r122_crm_sub_non_col_guar;
	}
	public BigDecimal getR122_crm_sub_non_col_cre_der() {
		return r122_crm_sub_non_col_cre_der;
	}
	public void setR122_crm_sub_non_col_cre_der(BigDecimal r122_crm_sub_non_col_cre_der) {
		this.r122_crm_sub_non_col_cre_der = r122_crm_sub_non_col_cre_der;
	}
	public BigDecimal getR122_crm_sub_col_elig_cash() {
		return r122_crm_sub_col_elig_cash;
	}
	public void setR122_crm_sub_col_elig_cash(BigDecimal r122_crm_sub_col_elig_cash) {
		this.r122_crm_sub_col_elig_cash = r122_crm_sub_col_elig_cash;
	}
	public BigDecimal getR122_crm_sub_col_elig_trea_bills() {
		return r122_crm_sub_col_elig_trea_bills;
	}
	public void setR122_crm_sub_col_elig_trea_bills(BigDecimal r122_crm_sub_col_elig_trea_bills) {
		this.r122_crm_sub_col_elig_trea_bills = r122_crm_sub_col_elig_trea_bills;
	}
	public BigDecimal getR122_crm_sub_col_elig_deb_sec() {
		return r122_crm_sub_col_elig_deb_sec;
	}
	public void setR122_crm_sub_col_elig_deb_sec(BigDecimal r122_crm_sub_col_elig_deb_sec) {
		this.r122_crm_sub_col_elig_deb_sec = r122_crm_sub_col_elig_deb_sec;
	}
	public BigDecimal getR122_crm_sub_col_elig_equi() {
		return r122_crm_sub_col_elig_equi;
	}
	public void setR122_crm_sub_col_elig_equi(BigDecimal r122_crm_sub_col_elig_equi) {
		this.r122_crm_sub_col_elig_equi = r122_crm_sub_col_elig_equi;
	}
	public BigDecimal getR122_crm_sub_col_elig_unit_tru() {
		return r122_crm_sub_col_elig_unit_tru;
	}
	public void setR122_crm_sub_col_elig_unit_tru(BigDecimal r122_crm_sub_col_elig_unit_tru) {
		this.r122_crm_sub_col_elig_unit_tru = r122_crm_sub_col_elig_unit_tru;
	}
	public BigDecimal getR122_crm_sub_col_exp_cov() {
		return r122_crm_sub_col_exp_cov;
	}
	public void setR122_crm_sub_col_exp_cov(BigDecimal r122_crm_sub_col_exp_cov) {
		this.r122_crm_sub_col_exp_cov = r122_crm_sub_col_exp_cov;
	}
	public BigDecimal getR122_crm_sub_col_elig_exp_not_cov() {
		return r122_crm_sub_col_elig_exp_not_cov;
	}
	public void setR122_crm_sub_col_elig_exp_not_cov(BigDecimal r122_crm_sub_col_elig_exp_not_cov) {
		this.r122_crm_sub_col_elig_exp_not_cov = r122_crm_sub_col_elig_exp_not_cov;
	}
	public BigDecimal getR122_crm_sub_rwa_ris_crm() {
		return r122_crm_sub_rwa_ris_crm;
	}
	public void setR122_crm_sub_rwa_ris_crm(BigDecimal r122_crm_sub_rwa_ris_crm) {
		this.r122_crm_sub_rwa_ris_crm = r122_crm_sub_rwa_ris_crm;
	}
	public BigDecimal getR122_crm_sub_rwa_cov_crm() {
		return r122_crm_sub_rwa_cov_crm;
	}
	public void setR122_crm_sub_rwa_cov_crm(BigDecimal r122_crm_sub_rwa_cov_crm) {
		this.r122_crm_sub_rwa_cov_crm = r122_crm_sub_rwa_cov_crm;
	}
	public BigDecimal getR122_crm_sub_rwa_org_cou() {
		return r122_crm_sub_rwa_org_cou;
	}
	public void setR122_crm_sub_rwa_org_cou(BigDecimal r122_crm_sub_rwa_org_cou) {
		this.r122_crm_sub_rwa_org_cou = r122_crm_sub_rwa_org_cou;
	}
	public BigDecimal getR122_crm_sub_rwa_not_cov_crm() {
		return r122_crm_sub_rwa_not_cov_crm;
	}
	public void setR122_crm_sub_rwa_not_cov_crm(BigDecimal r122_crm_sub_rwa_not_cov_crm) {
		this.r122_crm_sub_rwa_not_cov_crm = r122_crm_sub_rwa_not_cov_crm;
	}
	public BigDecimal getR122_crm_comp_col_expo_elig() {
		return r122_crm_comp_col_expo_elig;
	}
	public void setR122_crm_comp_col_expo_elig(BigDecimal r122_crm_comp_col_expo_elig) {
		this.r122_crm_comp_col_expo_elig = r122_crm_comp_col_expo_elig;
	}
	public BigDecimal getR122_crm_comp_col_elig_expo_vol_adj() {
		return r122_crm_comp_col_elig_expo_vol_adj;
	}
	public void setR122_crm_comp_col_elig_expo_vol_adj(BigDecimal r122_crm_comp_col_elig_expo_vol_adj) {
		this.r122_crm_comp_col_elig_expo_vol_adj = r122_crm_comp_col_elig_expo_vol_adj;
	}
	public BigDecimal getR122_crm_comp_col_elig_fin_hai() {
		return r122_crm_comp_col_elig_fin_hai;
	}
	public void setR122_crm_comp_col_elig_fin_hai(BigDecimal r122_crm_comp_col_elig_fin_hai) {
		this.r122_crm_comp_col_elig_fin_hai = r122_crm_comp_col_elig_fin_hai;
	}
	public BigDecimal getR122_crm_comp_col_expo_val() {
		return r122_crm_comp_col_expo_val;
	}
	public void setR122_crm_comp_col_expo_val(BigDecimal r122_crm_comp_col_expo_val) {
		this.r122_crm_comp_col_expo_val = r122_crm_comp_col_expo_val;
	}
	public BigDecimal getR122_rwa_elig_expo_not_cov_crm() {
		return r122_rwa_elig_expo_not_cov_crm;
	}
	public void setR122_rwa_elig_expo_not_cov_crm(BigDecimal r122_rwa_elig_expo_not_cov_crm) {
		this.r122_rwa_elig_expo_not_cov_crm = r122_rwa_elig_expo_not_cov_crm;
	}
	public BigDecimal getR122_rwa_unsec_expo_cre_ris() {
		return r122_rwa_unsec_expo_cre_ris;
	}
	public void setR122_rwa_unsec_expo_cre_ris(BigDecimal r122_rwa_unsec_expo_cre_ris) {
		this.r122_rwa_unsec_expo_cre_ris = r122_rwa_unsec_expo_cre_ris;
	}
	public BigDecimal getR122_rwa_unsec_expo() {
		return r122_rwa_unsec_expo;
	}
	public void setR122_rwa_unsec_expo(BigDecimal r122_rwa_unsec_expo) {
		this.r122_rwa_unsec_expo = r122_rwa_unsec_expo;
	}
	public BigDecimal getR122_rwa_tot_ris_wei_ass() {
		return r122_rwa_tot_ris_wei_ass;
	}
	public void setR122_rwa_tot_ris_wei_ass(BigDecimal r122_rwa_tot_ris_wei_ass) {
		this.r122_rwa_tot_ris_wei_ass = r122_rwa_tot_ris_wei_ass;
	}
	public String getR124_exposure_class() {
		return r124_exposure_class;
	}
	public void setR124_exposure_class(String r124_exposure_class) {
		this.r124_exposure_class = r124_exposure_class;
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
	public M_SRWA_12A_Archival_Summary_Entity3() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	

	

}
