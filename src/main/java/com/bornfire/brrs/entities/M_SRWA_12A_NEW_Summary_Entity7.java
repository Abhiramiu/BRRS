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
@Table(name = "BRRS_M_SRWA_12A_NEW_SUMMARYTABLE7")


public class M_SRWA_12A_NEW_Summary_Entity7 {
	
	

		
	
	private String	r213_exposure_class_off_bal;
	private BigDecimal	r213_nom_pri_amt;
	private BigDecimal	r213_ccf;
	private BigDecimal	r213_cea;
	private BigDecimal	r213_cea_elig_coun_bilt_net;
	private BigDecimal	r213_cea_aft_net;
	private BigDecimal	r213_crm_sub_app_cea_elig;
	private BigDecimal	r213_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r213_crm_sub_app_non_col_cre_der;
	private BigDecimal	r213_crm_sub_app_col_elig_cash;
	private BigDecimal	r213_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r213_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r213_crm_sub_app_col_elig_euiq;
	private BigDecimal	r213_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r213_crm_sub_app_col_cea_cov;
	private BigDecimal	r213_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r213_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r213_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r213_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r213_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r213_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r213_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r213_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r213_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r213_rwa_elig_cea_not_cov;
	private BigDecimal	r213_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r213_rwa_unsec_cea;
	private BigDecimal	r213_rwa_tot_ris_wei_ass;
	private String	r214_exposure_class_off_bal;
	private BigDecimal	r214_nom_pri_amt;
	private BigDecimal	r214_ccf;
	private BigDecimal	r214_cea;
	private BigDecimal	r214_cea_elig_coun_bilt_net;
	private BigDecimal	r214_cea_aft_net;
	private BigDecimal	r214_crm_sub_app_cea_elig;
	private BigDecimal	r214_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r214_crm_sub_app_non_col_cre_der;
	private BigDecimal	r214_crm_sub_app_col_elig_cash;
	private BigDecimal	r214_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r214_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r214_crm_sub_app_col_elig_euiq;
	private BigDecimal	r214_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r214_crm_sub_app_col_cea_cov;
	private BigDecimal	r214_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r214_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r214_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r214_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r214_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r214_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r214_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r214_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r214_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r214_rwa_elig_cea_not_cov;
	private BigDecimal	r214_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r214_rwa_unsec_cea;
	private BigDecimal	r214_rwa_tot_ris_wei_ass;
	private String	r215_exposure_class_off_bal;
	private BigDecimal	r215_nom_pri_amt;
	private BigDecimal	r215_ccf;
	private BigDecimal	r215_cea;
	private BigDecimal	r215_cea_elig_coun_bilt_net;
	private BigDecimal	r215_cea_aft_net;
	private BigDecimal	r215_crm_sub_app_cea_elig;
	private BigDecimal	r215_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r215_crm_sub_app_non_col_cre_der;
	private BigDecimal	r215_crm_sub_app_col_elig_cash;
	private BigDecimal	r215_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r215_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r215_crm_sub_app_col_elig_euiq;
	private BigDecimal	r215_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r215_crm_sub_app_col_cea_cov;
	private BigDecimal	r215_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r215_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r215_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r215_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r215_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r215_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r215_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r215_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r215_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r215_rwa_elig_cea_not_cov;
	private BigDecimal	r215_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r215_rwa_unsec_cea;
	private BigDecimal	r215_rwa_tot_ris_wei_ass;
	private String	r216_exposure_class_off_bal;
	private BigDecimal	r216_nom_pri_amt;
	private BigDecimal	r216_ccf;
	private BigDecimal	r216_cea;
	private BigDecimal	r216_cea_elig_coun_bilt_net;
	private BigDecimal	r216_cea_aft_net;
	private BigDecimal	r216_crm_sub_app_cea_elig;
	private BigDecimal	r216_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r216_crm_sub_app_non_col_cre_der;
	private BigDecimal	r216_crm_sub_app_col_elig_cash;
	private BigDecimal	r216_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r216_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r216_crm_sub_app_col_elig_euiq;
	private BigDecimal	r216_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r216_crm_sub_app_col_cea_cov;
	private BigDecimal	r216_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r216_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r216_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r216_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r216_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r216_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r216_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r216_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r216_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r216_rwa_elig_cea_not_cov;
	private BigDecimal	r216_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r216_rwa_unsec_cea;
	private BigDecimal	r216_rwa_tot_ris_wei_ass;
	private String	r217_exposure_class_off_bal;
	private BigDecimal	r217_nom_pri_amt;
	private BigDecimal	r217_ccf;
	private BigDecimal	r217_cea;
	private BigDecimal	r217_cea_elig_coun_bilt_net;
	private BigDecimal	r217_cea_aft_net;
	private BigDecimal	r217_crm_sub_app_cea_elig;
	private BigDecimal	r217_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r217_crm_sub_app_non_col_cre_der;
	private BigDecimal	r217_crm_sub_app_col_elig_cash;
	private BigDecimal	r217_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r217_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r217_crm_sub_app_col_elig_euiq;
	private BigDecimal	r217_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r217_crm_sub_app_col_cea_cov;
	private BigDecimal	r217_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r217_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r217_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r217_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r217_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r217_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r217_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r217_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r217_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r217_rwa_elig_cea_not_cov;
	private BigDecimal	r217_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r217_rwa_unsec_cea;
	private BigDecimal	r217_rwa_tot_ris_wei_ass;
	private String	r218_exposure_class_off_bal;
	private BigDecimal	r218_nom_pri_amt;
	private BigDecimal	r218_ccf;
	private BigDecimal	r218_cea;
	private BigDecimal	r218_cea_elig_coun_bilt_net;
	private BigDecimal	r218_cea_aft_net;
	private BigDecimal	r218_crm_sub_app_cea_elig;
	private BigDecimal	r218_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r218_crm_sub_app_non_col_cre_der;
	private BigDecimal	r218_crm_sub_app_col_elig_cash;
	private BigDecimal	r218_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r218_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r218_crm_sub_app_col_elig_euiq;
	private BigDecimal	r218_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r218_crm_sub_app_col_cea_cov;
	private BigDecimal	r218_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r218_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r218_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r218_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r218_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r218_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r218_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r218_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r218_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r218_rwa_elig_cea_not_cov;
	private BigDecimal	r218_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r218_rwa_unsec_cea;
	private BigDecimal	r218_rwa_tot_ris_wei_ass;
	private String	r219_exposure_class_off_bal;
	private BigDecimal	r219_nom_pri_amt;
	private BigDecimal	r219_ccf;
	private BigDecimal	r219_cea;
	private BigDecimal	r219_cea_elig_coun_bilt_net;
	private BigDecimal	r219_cea_aft_net;
	private BigDecimal	r219_crm_sub_app_cea_elig;
	private BigDecimal	r219_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r219_crm_sub_app_non_col_cre_der;
	private BigDecimal	r219_crm_sub_app_col_elig_cash;
	private BigDecimal	r219_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r219_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r219_crm_sub_app_col_elig_euiq;
	private BigDecimal	r219_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r219_crm_sub_app_col_cea_cov;
	private BigDecimal	r219_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r219_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r219_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r219_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r219_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r219_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r219_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r219_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r219_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r219_rwa_elig_cea_not_cov;
	private BigDecimal	r219_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r219_rwa_unsec_cea;
	private BigDecimal	r219_rwa_tot_ris_wei_ass;
	private String	r220_exposure_class_off_bal;
	private BigDecimal	r220_nom_pri_amt;
	private BigDecimal	r220_ccf;
	private BigDecimal	r220_cea;
	private BigDecimal	r220_cea_elig_coun_bilt_net;
	private BigDecimal	r220_cea_aft_net;
	private BigDecimal	r220_crm_sub_app_cea_elig;
	private BigDecimal	r220_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r220_crm_sub_app_non_col_cre_der;
	private BigDecimal	r220_crm_sub_app_col_elig_cash;
	private BigDecimal	r220_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r220_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r220_crm_sub_app_col_elig_euiq;
	private BigDecimal	r220_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r220_crm_sub_app_col_cea_cov;
	private BigDecimal	r220_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r220_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r220_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r220_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r220_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r220_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r220_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r220_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r220_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r220_rwa_elig_cea_not_cov;
	private BigDecimal	r220_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r220_rwa_unsec_cea;
	private BigDecimal	r220_rwa_tot_ris_wei_ass;
	private String	r221_exposure_class_off_bal;
	private BigDecimal	r221_nom_pri_amt;
	private BigDecimal	r221_ccf;
	private BigDecimal	r221_cea;
	private BigDecimal	r221_cea_elig_coun_bilt_net;
	private BigDecimal	r221_cea_aft_net;
	private BigDecimal	r221_crm_sub_app_cea_elig;
	private BigDecimal	r221_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r221_crm_sub_app_non_col_cre_der;
	private BigDecimal	r221_crm_sub_app_col_elig_cash;
	private BigDecimal	r221_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r221_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r221_crm_sub_app_col_elig_euiq;
	private BigDecimal	r221_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r221_crm_sub_app_col_cea_cov;
	private BigDecimal	r221_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r221_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r221_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r221_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r221_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r221_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r221_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r221_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r221_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r221_rwa_elig_cea_not_cov;
	private BigDecimal	r221_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r221_rwa_unsec_cea;
	private BigDecimal	r221_rwa_tot_ris_wei_ass;
	private String	r222_exposure_class_off_bal;
	private BigDecimal	r222_nom_pri_amt;
	private BigDecimal	r222_ccf;
	private BigDecimal	r222_cea;
	private BigDecimal	r222_cea_elig_coun_bilt_net;
	private BigDecimal	r222_cea_aft_net;
	private BigDecimal	r222_crm_sub_app_cea_elig;
	private BigDecimal	r222_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r222_crm_sub_app_non_col_cre_der;
	private BigDecimal	r222_crm_sub_app_col_elig_cash;
	private BigDecimal	r222_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r222_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r222_crm_sub_app_col_elig_euiq;
	private BigDecimal	r222_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r222_crm_sub_app_col_cea_cov;
	private BigDecimal	r222_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r222_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r222_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r222_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r222_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r222_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r222_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r222_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r222_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r222_rwa_elig_cea_not_cov;
	private BigDecimal	r222_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r222_rwa_unsec_cea;
	private BigDecimal	r222_rwa_tot_ris_wei_ass;
	private String	r223_exposure_class_off_bal;
	private BigDecimal	r223_nom_pri_amt;
	private BigDecimal	r223_ccf;
	private BigDecimal	r223_cea;
	private BigDecimal	r223_cea_elig_coun_bilt_net;
	private BigDecimal	r223_cea_aft_net;
	private BigDecimal	r223_crm_sub_app_cea_elig;
	private BigDecimal	r223_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r223_crm_sub_app_non_col_cre_der;
	private BigDecimal	r223_crm_sub_app_col_elig_cash;
	private BigDecimal	r223_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r223_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r223_crm_sub_app_col_elig_euiq;
	private BigDecimal	r223_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r223_crm_sub_app_col_cea_cov;
	private BigDecimal	r223_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r223_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r223_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r223_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r223_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r223_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r223_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r223_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r223_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r223_rwa_elig_cea_not_cov;
	private BigDecimal	r223_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r223_rwa_unsec_cea;
	private BigDecimal	r223_rwa_tot_ris_wei_ass;
	private String	r224_exposure_class_off_bal;
	private BigDecimal	r224_nom_pri_amt;
	private BigDecimal	r224_ccf;
	private BigDecimal	r224_cea;
	private BigDecimal	r224_cea_elig_coun_bilt_net;
	private BigDecimal	r224_cea_aft_net;
	private BigDecimal	r224_crm_sub_app_cea_elig;
	private BigDecimal	r224_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r224_crm_sub_app_non_col_cre_der;
	private BigDecimal	r224_crm_sub_app_col_elig_cash;
	private BigDecimal	r224_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r224_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r224_crm_sub_app_col_elig_euiq;
	private BigDecimal	r224_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r224_crm_sub_app_col_cea_cov;
	private BigDecimal	r224_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r224_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r224_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r224_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r224_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r224_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r224_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r224_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r224_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r224_rwa_elig_cea_not_cov;
	private BigDecimal	r224_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r224_rwa_unsec_cea;
	private BigDecimal	r224_rwa_tot_ris_wei_ass;
	private String	r225_exposure_class_off_bal;
	private BigDecimal	r225_nom_pri_amt;
	private BigDecimal	r225_ccf;
	private BigDecimal	r225_cea;
	private BigDecimal	r225_cea_elig_coun_bilt_net;
	private BigDecimal	r225_cea_aft_net;
	private BigDecimal	r225_crm_sub_app_cea_elig;
	private BigDecimal	r225_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r225_crm_sub_app_non_col_cre_der;
	private BigDecimal	r225_crm_sub_app_col_elig_cash;
	private BigDecimal	r225_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r225_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r225_crm_sub_app_col_elig_euiq;
	private BigDecimal	r225_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r225_crm_sub_app_col_cea_cov;
	private BigDecimal	r225_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r225_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r225_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r225_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r225_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r225_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r225_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r225_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r225_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r225_rwa_elig_cea_not_cov;
	private BigDecimal	r225_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r225_rwa_unsec_cea;
	private BigDecimal	r225_rwa_tot_ris_wei_ass;
	private String	r226_exposure_class_off_bal;
	private BigDecimal	r226_nom_pri_amt;
	private BigDecimal	r226_ccf;
	private BigDecimal	r226_cea;
	private BigDecimal	r226_cea_elig_coun_bilt_net;
	private BigDecimal	r226_cea_aft_net;
	private BigDecimal	r226_crm_sub_app_cea_elig;
	private BigDecimal	r226_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r226_crm_sub_app_non_col_cre_der;
	private BigDecimal	r226_crm_sub_app_col_elig_cash;
	private BigDecimal	r226_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r226_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r226_crm_sub_app_col_elig_euiq;
	private BigDecimal	r226_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r226_crm_sub_app_col_cea_cov;
	private BigDecimal	r226_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r226_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r226_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r226_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r226_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r226_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r226_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r226_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r226_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r226_rwa_elig_cea_not_cov;
	private BigDecimal	r226_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r226_rwa_unsec_cea;
	private BigDecimal	r226_rwa_tot_ris_wei_ass;
	private String	r227_exposure_class_off_bal;
	private BigDecimal	r227_nom_pri_amt;
	private BigDecimal	r227_ccf;
	private BigDecimal	r227_cea;
	private BigDecimal	r227_cea_elig_coun_bilt_net;
	private BigDecimal	r227_cea_aft_net;
	private BigDecimal	r227_crm_sub_app_cea_elig;
	private BigDecimal	r227_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r227_crm_sub_app_non_col_cre_der;
	private BigDecimal	r227_crm_sub_app_col_elig_cash;
	private BigDecimal	r227_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r227_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r227_crm_sub_app_col_elig_euiq;
	private BigDecimal	r227_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r227_crm_sub_app_col_cea_cov;
	private BigDecimal	r227_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r227_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r227_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r227_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r227_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r227_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r227_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r227_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r227_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r227_rwa_elig_cea_not_cov;
	private BigDecimal	r227_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r227_rwa_unsec_cea;
	private BigDecimal	r227_rwa_tot_ris_wei_ass;
	private String	r228_exposure_class_off_bal;
	private BigDecimal	r228_nom_pri_amt;
	private BigDecimal	r228_ccf;
	private BigDecimal	r228_cea;
	private BigDecimal	r228_cea_elig_coun_bilt_net;
	private BigDecimal	r228_cea_aft_net;
	private BigDecimal	r228_crm_sub_app_cea_elig;
	private BigDecimal	r228_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r228_crm_sub_app_non_col_cre_der;
	private BigDecimal	r228_crm_sub_app_col_elig_cash;
	private BigDecimal	r228_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r228_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r228_crm_sub_app_col_elig_euiq;
	private BigDecimal	r228_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r228_crm_sub_app_col_cea_cov;
	private BigDecimal	r228_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r228_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r228_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r228_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r228_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r228_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r228_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r228_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r228_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r228_rwa_elig_cea_not_cov;
	private BigDecimal	r228_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r228_rwa_unsec_cea;
	private BigDecimal	r228_rwa_tot_ris_wei_ass;
	private String	r229_exposure_class_off_bal;
	private BigDecimal	r229_nom_pri_amt;
	private BigDecimal	r229_ccf;
	private BigDecimal	r229_cea;
	private BigDecimal	r229_cea_elig_coun_bilt_net;
	private BigDecimal	r229_cea_aft_net;
	private BigDecimal	r229_crm_sub_app_cea_elig;
	private BigDecimal	r229_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r229_crm_sub_app_non_col_cre_der;
	private BigDecimal	r229_crm_sub_app_col_elig_cash;
	private BigDecimal	r229_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r229_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r229_crm_sub_app_col_elig_euiq;
	private BigDecimal	r229_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r229_crm_sub_app_col_cea_cov;
	private BigDecimal	r229_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r229_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r229_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r229_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r229_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r229_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r229_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r229_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r229_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r229_rwa_elig_cea_not_cov;
	private BigDecimal	r229_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r229_rwa_unsec_cea;
	private BigDecimal	r229_rwa_tot_ris_wei_ass;
	private String	r230_exposure_class_off_bal;
	private BigDecimal	r230_nom_pri_amt;
	private BigDecimal	r230_ccf;
	private BigDecimal	r230_cea;
	private BigDecimal	r230_cea_elig_coun_bilt_net;
	private BigDecimal	r230_cea_aft_net;
	private BigDecimal	r230_crm_sub_app_cea_elig;
	private BigDecimal	r230_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r230_crm_sub_app_non_col_cre_der;
	private BigDecimal	r230_crm_sub_app_col_elig_cash;
	private BigDecimal	r230_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r230_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r230_crm_sub_app_col_elig_euiq;
	private BigDecimal	r230_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r230_crm_sub_app_col_cea_cov;
	private BigDecimal	r230_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r230_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r230_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r230_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r230_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r230_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r230_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r230_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r230_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r230_rwa_elig_cea_not_cov;
	private BigDecimal	r230_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r230_rwa_unsec_cea;
	private BigDecimal	r230_rwa_tot_ris_wei_ass;
	private String	r231_exposure_class_off_bal;
	private BigDecimal	r231_nom_pri_amt;
	private BigDecimal	r231_ccf;
	private BigDecimal	r231_cea;
	private BigDecimal	r231_cea_elig_coun_bilt_net;
	private BigDecimal	r231_cea_aft_net;
	private BigDecimal	r231_crm_sub_app_cea_elig;
	private BigDecimal	r231_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r231_crm_sub_app_non_col_cre_der;
	private BigDecimal	r231_crm_sub_app_col_elig_cash;
	private BigDecimal	r231_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r231_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r231_crm_sub_app_col_elig_euiq;
	private BigDecimal	r231_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r231_crm_sub_app_col_cea_cov;
	private BigDecimal	r231_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r231_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r231_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r231_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r231_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r231_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r231_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r231_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r231_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r231_rwa_elig_cea_not_cov;
	private BigDecimal	r231_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r231_rwa_unsec_cea;
	private BigDecimal	r231_rwa_tot_ris_wei_ass;
	private String	r232_exposure_class_off_bal;
	private BigDecimal	r232_nom_pri_amt;
	private BigDecimal	r232_ccf;
	private BigDecimal	r232_cea;
	private BigDecimal	r232_cea_elig_coun_bilt_net;
	private BigDecimal	r232_cea_aft_net;
	private BigDecimal	r232_crm_sub_app_cea_elig;
	private BigDecimal	r232_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r232_crm_sub_app_non_col_cre_der;
	private BigDecimal	r232_crm_sub_app_col_elig_cash;
	private BigDecimal	r232_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r232_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r232_crm_sub_app_col_elig_euiq;
	private BigDecimal	r232_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r232_crm_sub_app_col_cea_cov;
	private BigDecimal	r232_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r232_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r232_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r232_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r232_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r232_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r232_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r232_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r232_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r232_rwa_elig_cea_not_cov;
	private BigDecimal	r232_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r232_rwa_unsec_cea;
	private BigDecimal	r232_rwa_tot_ris_wei_ass;
	private String	r233_exposure_class_off_bal;
	private BigDecimal	r233_nom_pri_amt;
	private BigDecimal	r233_ccf;
	private BigDecimal	r233_cea;
	private BigDecimal	r233_cea_elig_coun_bilt_net;
	private BigDecimal	r233_cea_aft_net;
	private BigDecimal	r233_crm_sub_app_cea_elig;
	private BigDecimal	r233_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r233_crm_sub_app_non_col_cre_der;
	private BigDecimal	r233_crm_sub_app_col_elig_cash;
	private BigDecimal	r233_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r233_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r233_crm_sub_app_col_elig_euiq;
	private BigDecimal	r233_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r233_crm_sub_app_col_cea_cov;
	private BigDecimal	r233_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r233_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r233_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r233_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r233_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r233_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r233_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r233_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r233_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r233_rwa_elig_cea_not_cov;
	private BigDecimal	r233_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r233_rwa_unsec_cea;
	private BigDecimal	r233_rwa_tot_ris_wei_ass;
	private String	r234_exposure_class_off_bal;
	private BigDecimal	r234_nom_pri_amt;
	private BigDecimal	r234_ccf;
	private BigDecimal	r234_cea;
	private BigDecimal	r234_cea_elig_coun_bilt_net;
	private BigDecimal	r234_cea_aft_net;
	private BigDecimal	r234_crm_sub_app_cea_elig;
	private BigDecimal	r234_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r234_crm_sub_app_non_col_cre_der;
	private BigDecimal	r234_crm_sub_app_col_elig_cash;
	private BigDecimal	r234_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r234_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r234_crm_sub_app_col_elig_euiq;
	private BigDecimal	r234_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r234_crm_sub_app_col_cea_cov;
	private BigDecimal	r234_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r234_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r234_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r234_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r234_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r234_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r234_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r234_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r234_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r234_rwa_elig_cea_not_cov;
	private BigDecimal	r234_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r234_rwa_unsec_cea;
	private BigDecimal	r234_rwa_tot_ris_wei_ass;
	private String	r235_exposure_class_off_bal;
	private BigDecimal	r235_nom_pri_amt;
	private BigDecimal	r235_ccf;
	private BigDecimal	r235_cea;
	private BigDecimal	r235_cea_elig_coun_bilt_net;
	private BigDecimal	r235_cea_aft_net;
	private BigDecimal	r235_crm_sub_app_cea_elig;
	private BigDecimal	r235_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r235_crm_sub_app_non_col_cre_der;
	private BigDecimal	r235_crm_sub_app_col_elig_cash;
	private BigDecimal	r235_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r235_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r235_crm_sub_app_col_elig_euiq;
	private BigDecimal	r235_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r235_crm_sub_app_col_cea_cov;
	private BigDecimal	r235_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r235_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r235_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r235_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r235_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r235_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r235_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r235_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r235_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r235_rwa_elig_cea_not_cov;
	private BigDecimal	r235_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r235_rwa_unsec_cea;
	private BigDecimal	r235_rwa_tot_ris_wei_ass;
	private String	r236_exposure_class_off_bal;
	private BigDecimal	r236_nom_pri_amt;
	private BigDecimal	r236_ccf;
	private BigDecimal	r236_cea;
	private BigDecimal	r236_cea_elig_coun_bilt_net;
	private BigDecimal	r236_cea_aft_net;
	private BigDecimal	r236_crm_sub_app_cea_elig;
	private BigDecimal	r236_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r236_crm_sub_app_non_col_cre_der;
	private BigDecimal	r236_crm_sub_app_col_elig_cash;
	private BigDecimal	r236_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r236_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r236_crm_sub_app_col_elig_euiq;
	private BigDecimal	r236_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r236_crm_sub_app_col_cea_cov;
	private BigDecimal	r236_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r236_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r236_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r236_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r236_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r236_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r236_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r236_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r236_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r236_rwa_elig_cea_not_cov;
	private BigDecimal	r236_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r236_rwa_unsec_cea;
	private BigDecimal	r236_rwa_tot_ris_wei_ass;
	private String	r237_exposure_class_off_bal;
	private BigDecimal	r237_nom_pri_amt;
	private BigDecimal	r237_ccf;
	private BigDecimal	r237_cea;
	private BigDecimal	r237_cea_elig_coun_bilt_net;
	private BigDecimal	r237_cea_aft_net;
	private BigDecimal	r237_crm_sub_app_cea_elig;
	private BigDecimal	r237_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r237_crm_sub_app_non_col_cre_der;
	private BigDecimal	r237_crm_sub_app_col_elig_cash;
	private BigDecimal	r237_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r237_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r237_crm_sub_app_col_elig_euiq;
	private BigDecimal	r237_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r237_crm_sub_app_col_cea_cov;
	private BigDecimal	r237_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r237_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r237_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r237_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r237_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r237_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r237_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r237_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r237_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r237_rwa_elig_cea_not_cov;
	private BigDecimal	r237_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r237_rwa_unsec_cea;
	private BigDecimal	r237_rwa_tot_ris_wei_ass;
	private String	r238_exposure_class_off_bal;
	private BigDecimal	r238_nom_pri_amt;
	private BigDecimal	r238_ccf;
	private BigDecimal	r238_cea;
	private BigDecimal	r238_cea_elig_coun_bilt_net;
	private BigDecimal	r238_cea_aft_net;
	private BigDecimal	r238_crm_sub_app_cea_elig;
	private BigDecimal	r238_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r238_crm_sub_app_non_col_cre_der;
	private BigDecimal	r238_crm_sub_app_col_elig_cash;
	private BigDecimal	r238_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r238_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r238_crm_sub_app_col_elig_euiq;
	private BigDecimal	r238_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r238_crm_sub_app_col_cea_cov;
	private BigDecimal	r238_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r238_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r238_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r238_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r238_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r238_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r238_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r238_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r238_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r238_rwa_elig_cea_not_cov;
	private BigDecimal	r238_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r238_rwa_unsec_cea;
	private BigDecimal	r238_rwa_tot_ris_wei_ass;
	private String	r239_exposure_class_off_bal;
	private BigDecimal	r239_nom_pri_amt;
	private BigDecimal	r239_ccf;
	private BigDecimal	r239_cea;
	private BigDecimal	r239_cea_elig_coun_bilt_net;
	private BigDecimal	r239_cea_aft_net;
	private BigDecimal	r239_crm_sub_app_cea_elig;
	private BigDecimal	r239_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r239_crm_sub_app_non_col_cre_der;
	private BigDecimal	r239_crm_sub_app_col_elig_cash;
	private BigDecimal	r239_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r239_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r239_crm_sub_app_col_elig_euiq;
	private BigDecimal	r239_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r239_crm_sub_app_col_cea_cov;
	private BigDecimal	r239_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r239_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r239_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r239_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r239_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r239_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r239_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r239_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r239_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r239_rwa_elig_cea_not_cov;
	private BigDecimal	r239_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r239_rwa_unsec_cea;
	private BigDecimal	r239_rwa_tot_ris_wei_ass;
	private String	r240_exposure_class_off_bal;
	private BigDecimal	r240_nom_pri_amt;
	private BigDecimal	r240_ccf;
	private BigDecimal	r240_cea;
	private BigDecimal	r240_cea_elig_coun_bilt_net;
	private BigDecimal	r240_cea_aft_net;
	private BigDecimal	r240_crm_sub_app_cea_elig;
	private BigDecimal	r240_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r240_crm_sub_app_non_col_cre_der;
	private BigDecimal	r240_crm_sub_app_col_elig_cash;
	private BigDecimal	r240_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r240_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r240_crm_sub_app_col_elig_euiq;
	private BigDecimal	r240_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r240_crm_sub_app_col_cea_cov;
	private BigDecimal	r240_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r240_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r240_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r240_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r240_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r240_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r240_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r240_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r240_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r240_rwa_elig_cea_not_cov;
	private BigDecimal	r240_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r240_rwa_unsec_cea;
	private BigDecimal	r240_rwa_tot_ris_wei_ass;
	private String	r241_exposure_class_off_bal;
	private BigDecimal	r241_nom_pri_amt;
	private BigDecimal	r241_ccf;
	private BigDecimal	r241_cea;
	private BigDecimal	r241_cea_elig_coun_bilt_net;
	private BigDecimal	r241_cea_aft_net;
	private BigDecimal	r241_crm_sub_app_cea_elig;
	private BigDecimal	r241_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r241_crm_sub_app_non_col_cre_der;
	private BigDecimal	r241_crm_sub_app_col_elig_cash;
	private BigDecimal	r241_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r241_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r241_crm_sub_app_col_elig_euiq;
	private BigDecimal	r241_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r241_crm_sub_app_col_cea_cov;
	private BigDecimal	r241_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r241_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r241_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r241_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r241_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r241_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r241_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r241_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r241_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r241_rwa_elig_cea_not_cov;
	private BigDecimal	r241_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r241_rwa_unsec_cea;
	private BigDecimal	r241_rwa_tot_ris_wei_ass;
	private String	r242_exposure_class_off_bal;
	private BigDecimal	r242_nom_pri_amt;
	private BigDecimal	r242_ccf;
	private BigDecimal	r242_cea;
	private BigDecimal	r242_cea_elig_coun_bilt_net;
	private BigDecimal	r242_cea_aft_net;
	private BigDecimal	r242_crm_sub_app_cea_elig;
	private BigDecimal	r242_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r242_crm_sub_app_non_col_cre_der;
	private BigDecimal	r242_crm_sub_app_col_elig_cash;
	private BigDecimal	r242_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r242_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r242_crm_sub_app_col_elig_euiq;
	private BigDecimal	r242_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r242_crm_sub_app_col_cea_cov;
	private BigDecimal	r242_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r242_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r242_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r242_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r242_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r242_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r242_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r242_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r242_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r242_rwa_elig_cea_not_cov;
	private BigDecimal	r242_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r242_rwa_unsec_cea;
	private BigDecimal	r242_rwa_tot_ris_wei_ass;
	private String	r243_exposure_class_off_bal;
	private BigDecimal	r243_nom_pri_amt;
	private BigDecimal	r243_ccf;
	private BigDecimal	r243_cea;
	private BigDecimal	r243_cea_elig_coun_bilt_net;
	private BigDecimal	r243_cea_aft_net;
	private BigDecimal	r243_crm_sub_app_cea_elig;
	private BigDecimal	r243_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r243_crm_sub_app_non_col_cre_der;
	private BigDecimal	r243_crm_sub_app_col_elig_cash;
	private BigDecimal	r243_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r243_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r243_crm_sub_app_col_elig_euiq;
	private BigDecimal	r243_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r243_crm_sub_app_col_cea_cov;
	private BigDecimal	r243_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r243_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r243_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r243_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r243_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r243_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r243_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r243_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r243_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r243_rwa_elig_cea_not_cov;
	private BigDecimal	r243_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r243_rwa_unsec_cea;
	private BigDecimal	r243_rwa_tot_ris_wei_ass;
	private String	r244_exposure_class_off_bal;
	private BigDecimal	r244_nom_pri_amt;
	private BigDecimal	r244_ccf;
	private BigDecimal	r244_cea;
	private BigDecimal	r244_cea_elig_coun_bilt_net;
	private BigDecimal	r244_cea_aft_net;
	private BigDecimal	r244_crm_sub_app_cea_elig;
	private BigDecimal	r244_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r244_crm_sub_app_non_col_cre_der;
	private BigDecimal	r244_crm_sub_app_col_elig_cash;
	private BigDecimal	r244_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r244_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r244_crm_sub_app_col_elig_euiq;
	private BigDecimal	r244_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r244_crm_sub_app_col_cea_cov;
	private BigDecimal	r244_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r244_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r244_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r244_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r244_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r244_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r244_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r244_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r244_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r244_rwa_elig_cea_not_cov;
	private BigDecimal	r244_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r244_rwa_unsec_cea;
	private BigDecimal	r244_rwa_tot_ris_wei_ass;
	private String	r245_exposure_class_off_bal;
	private BigDecimal	r245_nom_pri_amt;
	private BigDecimal	r245_ccf;
	private BigDecimal	r245_cea;
	private BigDecimal	r245_cea_elig_coun_bilt_net;
	private BigDecimal	r245_cea_aft_net;
	private BigDecimal	r245_crm_sub_app_cea_elig;
	private BigDecimal	r245_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r245_crm_sub_app_non_col_cre_der;
	private BigDecimal	r245_crm_sub_app_col_elig_cash;
	private BigDecimal	r245_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r245_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r245_crm_sub_app_col_elig_euiq;
	private BigDecimal	r245_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r245_crm_sub_app_col_cea_cov;
	private BigDecimal	r245_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r245_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r245_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r245_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r245_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r245_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r245_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r245_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r245_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r245_rwa_elig_cea_not_cov;
	private BigDecimal	r245_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r245_rwa_unsec_cea;
	private BigDecimal	r245_rwa_tot_ris_wei_ass;
	private String	r246_exposure_class_off_bal;
	private BigDecimal	r246_nom_pri_amt;
	private BigDecimal	r246_ccf;
	private BigDecimal	r246_cea;
	private BigDecimal	r246_cea_elig_coun_bilt_net;
	private BigDecimal	r246_cea_aft_net;
	private BigDecimal	r246_crm_sub_app_cea_elig;
	private BigDecimal	r246_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r246_crm_sub_app_non_col_cre_der;
	private BigDecimal	r246_crm_sub_app_col_elig_cash;
	private BigDecimal	r246_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r246_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r246_crm_sub_app_col_elig_euiq;
	private BigDecimal	r246_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r246_crm_sub_app_col_cea_cov;
	private BigDecimal	r246_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r246_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r246_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r246_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r246_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r246_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r246_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r246_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r246_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r246_rwa_elig_cea_not_cov;
	private BigDecimal	r246_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r246_rwa_unsec_cea;
	private BigDecimal	r246_rwa_tot_ris_wei_ass;
	               
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
	public String getR213_exposure_class_off_bal() {
		return r213_exposure_class_off_bal;
	}
	public void setR213_exposure_class_off_bal(String r213_exposure_class_off_bal) {
		this.r213_exposure_class_off_bal = r213_exposure_class_off_bal;
	}
	public BigDecimal getR213_nom_pri_amt() {
		return r213_nom_pri_amt;
	}
	public void setR213_nom_pri_amt(BigDecimal r213_nom_pri_amt) {
		this.r213_nom_pri_amt = r213_nom_pri_amt;
	}
	public BigDecimal getR213_ccf() {
		return r213_ccf;
	}
	public void setR213_ccf(BigDecimal r213_ccf) {
		this.r213_ccf = r213_ccf;
	}
	public BigDecimal getR213_cea() {
		return r213_cea;
	}
	public void setR213_cea(BigDecimal r213_cea) {
		this.r213_cea = r213_cea;
	}
	public BigDecimal getR213_cea_elig_coun_bilt_net() {
		return r213_cea_elig_coun_bilt_net;
	}
	public void setR213_cea_elig_coun_bilt_net(BigDecimal r213_cea_elig_coun_bilt_net) {
		this.r213_cea_elig_coun_bilt_net = r213_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR213_cea_aft_net() {
		return r213_cea_aft_net;
	}
	public void setR213_cea_aft_net(BigDecimal r213_cea_aft_net) {
		this.r213_cea_aft_net = r213_cea_aft_net;
	}
	public BigDecimal getR213_crm_sub_app_cea_elig() {
		return r213_crm_sub_app_cea_elig;
	}
	public void setR213_crm_sub_app_cea_elig(BigDecimal r213_crm_sub_app_cea_elig) {
		this.r213_crm_sub_app_cea_elig = r213_crm_sub_app_cea_elig;
	}
	public BigDecimal getR213_crm_sub_app_non_col_guar_elig() {
		return r213_crm_sub_app_non_col_guar_elig;
	}
	public void setR213_crm_sub_app_non_col_guar_elig(BigDecimal r213_crm_sub_app_non_col_guar_elig) {
		this.r213_crm_sub_app_non_col_guar_elig = r213_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR213_crm_sub_app_non_col_cre_der() {
		return r213_crm_sub_app_non_col_cre_der;
	}
	public void setR213_crm_sub_app_non_col_cre_der(BigDecimal r213_crm_sub_app_non_col_cre_der) {
		this.r213_crm_sub_app_non_col_cre_der = r213_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR213_crm_sub_app_col_elig_cash() {
		return r213_crm_sub_app_col_elig_cash;
	}
	public void setR213_crm_sub_app_col_elig_cash(BigDecimal r213_crm_sub_app_col_elig_cash) {
		this.r213_crm_sub_app_col_elig_cash = r213_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR213_crm_sub_app_col_elig_tre_bills() {
		return r213_crm_sub_app_col_elig_tre_bills;
	}
	public void setR213_crm_sub_app_col_elig_tre_bills(BigDecimal r213_crm_sub_app_col_elig_tre_bills) {
		this.r213_crm_sub_app_col_elig_tre_bills = r213_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR213_crm_sub_app_col_elig_deb_sec() {
		return r213_crm_sub_app_col_elig_deb_sec;
	}
	public void setR213_crm_sub_app_col_elig_deb_sec(BigDecimal r213_crm_sub_app_col_elig_deb_sec) {
		this.r213_crm_sub_app_col_elig_deb_sec = r213_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR213_crm_sub_app_col_elig_euiq() {
		return r213_crm_sub_app_col_elig_euiq;
	}
	public void setR213_crm_sub_app_col_elig_euiq(BigDecimal r213_crm_sub_app_col_elig_euiq) {
		this.r213_crm_sub_app_col_elig_euiq = r213_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR213_crm_sub_app_col_elig_uni_tru() {
		return r213_crm_sub_app_col_elig_uni_tru;
	}
	public void setR213_crm_sub_app_col_elig_uni_tru(BigDecimal r213_crm_sub_app_col_elig_uni_tru) {
		this.r213_crm_sub_app_col_elig_uni_tru = r213_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR213_crm_sub_app_col_cea_cov() {
		return r213_crm_sub_app_col_cea_cov;
	}
	public void setR213_crm_sub_app_col_cea_cov(BigDecimal r213_crm_sub_app_col_cea_cov) {
		this.r213_crm_sub_app_col_cea_cov = r213_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR213_crm_sub_app_col_cea_not_cov() {
		return r213_crm_sub_app_col_cea_not_cov;
	}
	public void setR213_crm_sub_app_col_cea_not_cov(BigDecimal r213_crm_sub_app_col_cea_not_cov) {
		this.r213_crm_sub_app_col_cea_not_cov = r213_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR213_crm_sub_app_rwa_ris_wei_crm() {
		return r213_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR213_crm_sub_app_rwa_ris_wei_crm(BigDecimal r213_crm_sub_app_rwa_ris_wei_crm) {
		this.r213_crm_sub_app_rwa_ris_wei_crm = r213_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR213_crm_sub_app_rwa_ris_cea_cov() {
		return r213_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR213_crm_sub_app_rwa_ris_cea_cov(BigDecimal r213_crm_sub_app_rwa_ris_cea_cov) {
		this.r213_crm_sub_app_rwa_ris_cea_cov = r213_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR213_crm_sub_app_rwa_appl_org_coun() {
		return r213_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR213_crm_sub_app_rwa_appl_org_coun(BigDecimal r213_crm_sub_app_rwa_appl_org_coun) {
		this.r213_crm_sub_app_rwa_appl_org_coun = r213_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR213_crm_sub_app_rwa_ris_cea_not_cov() {
		return r213_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR213_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r213_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r213_crm_sub_app_rwa_ris_cea_not_cov = r213_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR213_crm_com_app_col_cea_elig_crm() {
		return r213_crm_com_app_col_cea_elig_crm;
	}
	public void setR213_crm_com_app_col_cea_elig_crm(BigDecimal r213_crm_com_app_col_cea_elig_crm) {
		this.r213_crm_com_app_col_cea_elig_crm = r213_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR213_crm_com_app_col_elig_cea_vol_adj() {
		return r213_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR213_crm_com_app_col_elig_cea_vol_adj(BigDecimal r213_crm_com_app_col_elig_cea_vol_adj) {
		this.r213_crm_com_app_col_elig_cea_vol_adj = r213_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR213_crm_com_app_col_elig_fin_hai() {
		return r213_crm_com_app_col_elig_fin_hai;
	}
	public void setR213_crm_com_app_col_elig_fin_hai(BigDecimal r213_crm_com_app_col_elig_fin_hai) {
		this.r213_crm_com_app_col_elig_fin_hai = r213_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR213_crm_com_app_col_cea_val_aft_crm() {
		return r213_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR213_crm_com_app_col_cea_val_aft_crm(BigDecimal r213_crm_com_app_col_cea_val_aft_crm) {
		this.r213_crm_com_app_col_cea_val_aft_crm = r213_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR213_rwa_elig_cea_not_cov() {
		return r213_rwa_elig_cea_not_cov;
	}
	public void setR213_rwa_elig_cea_not_cov(BigDecimal r213_rwa_elig_cea_not_cov) {
		this.r213_rwa_elig_cea_not_cov = r213_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR213_rwa_unsec_cea_sub_cre_ris() {
		return r213_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR213_rwa_unsec_cea_sub_cre_ris(BigDecimal r213_rwa_unsec_cea_sub_cre_ris) {
		this.r213_rwa_unsec_cea_sub_cre_ris = r213_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR213_rwa_unsec_cea() {
		return r213_rwa_unsec_cea;
	}
	public void setR213_rwa_unsec_cea(BigDecimal r213_rwa_unsec_cea) {
		this.r213_rwa_unsec_cea = r213_rwa_unsec_cea;
	}
	public BigDecimal getR213_rwa_tot_ris_wei_ass() {
		return r213_rwa_tot_ris_wei_ass;
	}
	public void setR213_rwa_tot_ris_wei_ass(BigDecimal r213_rwa_tot_ris_wei_ass) {
		this.r213_rwa_tot_ris_wei_ass = r213_rwa_tot_ris_wei_ass;
	}
	public String getR214_exposure_class_off_bal() {
		return r214_exposure_class_off_bal;
	}
	public void setR214_exposure_class_off_bal(String r214_exposure_class_off_bal) {
		this.r214_exposure_class_off_bal = r214_exposure_class_off_bal;
	}
	public BigDecimal getR214_nom_pri_amt() {
		return r214_nom_pri_amt;
	}
	public void setR214_nom_pri_amt(BigDecimal r214_nom_pri_amt) {
		this.r214_nom_pri_amt = r214_nom_pri_amt;
	}
	public BigDecimal getR214_ccf() {
		return r214_ccf;
	}
	public void setR214_ccf(BigDecimal r214_ccf) {
		this.r214_ccf = r214_ccf;
	}
	public BigDecimal getR214_cea() {
		return r214_cea;
	}
	public void setR214_cea(BigDecimal r214_cea) {
		this.r214_cea = r214_cea;
	}
	public BigDecimal getR214_cea_elig_coun_bilt_net() {
		return r214_cea_elig_coun_bilt_net;
	}
	public void setR214_cea_elig_coun_bilt_net(BigDecimal r214_cea_elig_coun_bilt_net) {
		this.r214_cea_elig_coun_bilt_net = r214_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR214_cea_aft_net() {
		return r214_cea_aft_net;
	}
	public void setR214_cea_aft_net(BigDecimal r214_cea_aft_net) {
		this.r214_cea_aft_net = r214_cea_aft_net;
	}
	public BigDecimal getR214_crm_sub_app_cea_elig() {
		return r214_crm_sub_app_cea_elig;
	}
	public void setR214_crm_sub_app_cea_elig(BigDecimal r214_crm_sub_app_cea_elig) {
		this.r214_crm_sub_app_cea_elig = r214_crm_sub_app_cea_elig;
	}
	public BigDecimal getR214_crm_sub_app_non_col_guar_elig() {
		return r214_crm_sub_app_non_col_guar_elig;
	}
	public void setR214_crm_sub_app_non_col_guar_elig(BigDecimal r214_crm_sub_app_non_col_guar_elig) {
		this.r214_crm_sub_app_non_col_guar_elig = r214_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR214_crm_sub_app_non_col_cre_der() {
		return r214_crm_sub_app_non_col_cre_der;
	}
	public void setR214_crm_sub_app_non_col_cre_der(BigDecimal r214_crm_sub_app_non_col_cre_der) {
		this.r214_crm_sub_app_non_col_cre_der = r214_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR214_crm_sub_app_col_elig_cash() {
		return r214_crm_sub_app_col_elig_cash;
	}
	public void setR214_crm_sub_app_col_elig_cash(BigDecimal r214_crm_sub_app_col_elig_cash) {
		this.r214_crm_sub_app_col_elig_cash = r214_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR214_crm_sub_app_col_elig_tre_bills() {
		return r214_crm_sub_app_col_elig_tre_bills;
	}
	public void setR214_crm_sub_app_col_elig_tre_bills(BigDecimal r214_crm_sub_app_col_elig_tre_bills) {
		this.r214_crm_sub_app_col_elig_tre_bills = r214_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR214_crm_sub_app_col_elig_deb_sec() {
		return r214_crm_sub_app_col_elig_deb_sec;
	}
	public void setR214_crm_sub_app_col_elig_deb_sec(BigDecimal r214_crm_sub_app_col_elig_deb_sec) {
		this.r214_crm_sub_app_col_elig_deb_sec = r214_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR214_crm_sub_app_col_elig_euiq() {
		return r214_crm_sub_app_col_elig_euiq;
	}
	public void setR214_crm_sub_app_col_elig_euiq(BigDecimal r214_crm_sub_app_col_elig_euiq) {
		this.r214_crm_sub_app_col_elig_euiq = r214_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR214_crm_sub_app_col_elig_uni_tru() {
		return r214_crm_sub_app_col_elig_uni_tru;
	}
	public void setR214_crm_sub_app_col_elig_uni_tru(BigDecimal r214_crm_sub_app_col_elig_uni_tru) {
		this.r214_crm_sub_app_col_elig_uni_tru = r214_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR214_crm_sub_app_col_cea_cov() {
		return r214_crm_sub_app_col_cea_cov;
	}
	public void setR214_crm_sub_app_col_cea_cov(BigDecimal r214_crm_sub_app_col_cea_cov) {
		this.r214_crm_sub_app_col_cea_cov = r214_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR214_crm_sub_app_col_cea_not_cov() {
		return r214_crm_sub_app_col_cea_not_cov;
	}
	public void setR214_crm_sub_app_col_cea_not_cov(BigDecimal r214_crm_sub_app_col_cea_not_cov) {
		this.r214_crm_sub_app_col_cea_not_cov = r214_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR214_crm_sub_app_rwa_ris_wei_crm() {
		return r214_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR214_crm_sub_app_rwa_ris_wei_crm(BigDecimal r214_crm_sub_app_rwa_ris_wei_crm) {
		this.r214_crm_sub_app_rwa_ris_wei_crm = r214_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR214_crm_sub_app_rwa_ris_cea_cov() {
		return r214_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR214_crm_sub_app_rwa_ris_cea_cov(BigDecimal r214_crm_sub_app_rwa_ris_cea_cov) {
		this.r214_crm_sub_app_rwa_ris_cea_cov = r214_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR214_crm_sub_app_rwa_appl_org_coun() {
		return r214_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR214_crm_sub_app_rwa_appl_org_coun(BigDecimal r214_crm_sub_app_rwa_appl_org_coun) {
		this.r214_crm_sub_app_rwa_appl_org_coun = r214_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR214_crm_sub_app_rwa_ris_cea_not_cov() {
		return r214_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR214_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r214_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r214_crm_sub_app_rwa_ris_cea_not_cov = r214_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR214_crm_com_app_col_cea_elig_crm() {
		return r214_crm_com_app_col_cea_elig_crm;
	}
	public void setR214_crm_com_app_col_cea_elig_crm(BigDecimal r214_crm_com_app_col_cea_elig_crm) {
		this.r214_crm_com_app_col_cea_elig_crm = r214_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR214_crm_com_app_col_elig_cea_vol_adj() {
		return r214_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR214_crm_com_app_col_elig_cea_vol_adj(BigDecimal r214_crm_com_app_col_elig_cea_vol_adj) {
		this.r214_crm_com_app_col_elig_cea_vol_adj = r214_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR214_crm_com_app_col_elig_fin_hai() {
		return r214_crm_com_app_col_elig_fin_hai;
	}
	public void setR214_crm_com_app_col_elig_fin_hai(BigDecimal r214_crm_com_app_col_elig_fin_hai) {
		this.r214_crm_com_app_col_elig_fin_hai = r214_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR214_crm_com_app_col_cea_val_aft_crm() {
		return r214_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR214_crm_com_app_col_cea_val_aft_crm(BigDecimal r214_crm_com_app_col_cea_val_aft_crm) {
		this.r214_crm_com_app_col_cea_val_aft_crm = r214_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR214_rwa_elig_cea_not_cov() {
		return r214_rwa_elig_cea_not_cov;
	}
	public void setR214_rwa_elig_cea_not_cov(BigDecimal r214_rwa_elig_cea_not_cov) {
		this.r214_rwa_elig_cea_not_cov = r214_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR214_rwa_unsec_cea_sub_cre_ris() {
		return r214_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR214_rwa_unsec_cea_sub_cre_ris(BigDecimal r214_rwa_unsec_cea_sub_cre_ris) {
		this.r214_rwa_unsec_cea_sub_cre_ris = r214_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR214_rwa_unsec_cea() {
		return r214_rwa_unsec_cea;
	}
	public void setR214_rwa_unsec_cea(BigDecimal r214_rwa_unsec_cea) {
		this.r214_rwa_unsec_cea = r214_rwa_unsec_cea;
	}
	public BigDecimal getR214_rwa_tot_ris_wei_ass() {
		return r214_rwa_tot_ris_wei_ass;
	}
	public void setR214_rwa_tot_ris_wei_ass(BigDecimal r214_rwa_tot_ris_wei_ass) {
		this.r214_rwa_tot_ris_wei_ass = r214_rwa_tot_ris_wei_ass;
	}
	public String getR215_exposure_class_off_bal() {
		return r215_exposure_class_off_bal;
	}
	public void setR215_exposure_class_off_bal(String r215_exposure_class_off_bal) {
		this.r215_exposure_class_off_bal = r215_exposure_class_off_bal;
	}
	public BigDecimal getR215_nom_pri_amt() {
		return r215_nom_pri_amt;
	}
	public void setR215_nom_pri_amt(BigDecimal r215_nom_pri_amt) {
		this.r215_nom_pri_amt = r215_nom_pri_amt;
	}
	public BigDecimal getR215_ccf() {
		return r215_ccf;
	}
	public void setR215_ccf(BigDecimal r215_ccf) {
		this.r215_ccf = r215_ccf;
	}
	public BigDecimal getR215_cea() {
		return r215_cea;
	}
	public void setR215_cea(BigDecimal r215_cea) {
		this.r215_cea = r215_cea;
	}
	public BigDecimal getR215_cea_elig_coun_bilt_net() {
		return r215_cea_elig_coun_bilt_net;
	}
	public void setR215_cea_elig_coun_bilt_net(BigDecimal r215_cea_elig_coun_bilt_net) {
		this.r215_cea_elig_coun_bilt_net = r215_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR215_cea_aft_net() {
		return r215_cea_aft_net;
	}
	public void setR215_cea_aft_net(BigDecimal r215_cea_aft_net) {
		this.r215_cea_aft_net = r215_cea_aft_net;
	}
	public BigDecimal getR215_crm_sub_app_cea_elig() {
		return r215_crm_sub_app_cea_elig;
	}
	public void setR215_crm_sub_app_cea_elig(BigDecimal r215_crm_sub_app_cea_elig) {
		this.r215_crm_sub_app_cea_elig = r215_crm_sub_app_cea_elig;
	}
	public BigDecimal getR215_crm_sub_app_non_col_guar_elig() {
		return r215_crm_sub_app_non_col_guar_elig;
	}
	public void setR215_crm_sub_app_non_col_guar_elig(BigDecimal r215_crm_sub_app_non_col_guar_elig) {
		this.r215_crm_sub_app_non_col_guar_elig = r215_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR215_crm_sub_app_non_col_cre_der() {
		return r215_crm_sub_app_non_col_cre_der;
	}
	public void setR215_crm_sub_app_non_col_cre_der(BigDecimal r215_crm_sub_app_non_col_cre_der) {
		this.r215_crm_sub_app_non_col_cre_der = r215_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR215_crm_sub_app_col_elig_cash() {
		return r215_crm_sub_app_col_elig_cash;
	}
	public void setR215_crm_sub_app_col_elig_cash(BigDecimal r215_crm_sub_app_col_elig_cash) {
		this.r215_crm_sub_app_col_elig_cash = r215_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR215_crm_sub_app_col_elig_tre_bills() {
		return r215_crm_sub_app_col_elig_tre_bills;
	}
	public void setR215_crm_sub_app_col_elig_tre_bills(BigDecimal r215_crm_sub_app_col_elig_tre_bills) {
		this.r215_crm_sub_app_col_elig_tre_bills = r215_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR215_crm_sub_app_col_elig_deb_sec() {
		return r215_crm_sub_app_col_elig_deb_sec;
	}
	public void setR215_crm_sub_app_col_elig_deb_sec(BigDecimal r215_crm_sub_app_col_elig_deb_sec) {
		this.r215_crm_sub_app_col_elig_deb_sec = r215_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR215_crm_sub_app_col_elig_euiq() {
		return r215_crm_sub_app_col_elig_euiq;
	}
	public void setR215_crm_sub_app_col_elig_euiq(BigDecimal r215_crm_sub_app_col_elig_euiq) {
		this.r215_crm_sub_app_col_elig_euiq = r215_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR215_crm_sub_app_col_elig_uni_tru() {
		return r215_crm_sub_app_col_elig_uni_tru;
	}
	public void setR215_crm_sub_app_col_elig_uni_tru(BigDecimal r215_crm_sub_app_col_elig_uni_tru) {
		this.r215_crm_sub_app_col_elig_uni_tru = r215_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR215_crm_sub_app_col_cea_cov() {
		return r215_crm_sub_app_col_cea_cov;
	}
	public void setR215_crm_sub_app_col_cea_cov(BigDecimal r215_crm_sub_app_col_cea_cov) {
		this.r215_crm_sub_app_col_cea_cov = r215_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR215_crm_sub_app_col_cea_not_cov() {
		return r215_crm_sub_app_col_cea_not_cov;
	}
	public void setR215_crm_sub_app_col_cea_not_cov(BigDecimal r215_crm_sub_app_col_cea_not_cov) {
		this.r215_crm_sub_app_col_cea_not_cov = r215_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR215_crm_sub_app_rwa_ris_wei_crm() {
		return r215_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR215_crm_sub_app_rwa_ris_wei_crm(BigDecimal r215_crm_sub_app_rwa_ris_wei_crm) {
		this.r215_crm_sub_app_rwa_ris_wei_crm = r215_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR215_crm_sub_app_rwa_ris_cea_cov() {
		return r215_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR215_crm_sub_app_rwa_ris_cea_cov(BigDecimal r215_crm_sub_app_rwa_ris_cea_cov) {
		this.r215_crm_sub_app_rwa_ris_cea_cov = r215_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR215_crm_sub_app_rwa_appl_org_coun() {
		return r215_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR215_crm_sub_app_rwa_appl_org_coun(BigDecimal r215_crm_sub_app_rwa_appl_org_coun) {
		this.r215_crm_sub_app_rwa_appl_org_coun = r215_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR215_crm_sub_app_rwa_ris_cea_not_cov() {
		return r215_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR215_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r215_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r215_crm_sub_app_rwa_ris_cea_not_cov = r215_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR215_crm_com_app_col_cea_elig_crm() {
		return r215_crm_com_app_col_cea_elig_crm;
	}
	public void setR215_crm_com_app_col_cea_elig_crm(BigDecimal r215_crm_com_app_col_cea_elig_crm) {
		this.r215_crm_com_app_col_cea_elig_crm = r215_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR215_crm_com_app_col_elig_cea_vol_adj() {
		return r215_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR215_crm_com_app_col_elig_cea_vol_adj(BigDecimal r215_crm_com_app_col_elig_cea_vol_adj) {
		this.r215_crm_com_app_col_elig_cea_vol_adj = r215_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR215_crm_com_app_col_elig_fin_hai() {
		return r215_crm_com_app_col_elig_fin_hai;
	}
	public void setR215_crm_com_app_col_elig_fin_hai(BigDecimal r215_crm_com_app_col_elig_fin_hai) {
		this.r215_crm_com_app_col_elig_fin_hai = r215_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR215_crm_com_app_col_cea_val_aft_crm() {
		return r215_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR215_crm_com_app_col_cea_val_aft_crm(BigDecimal r215_crm_com_app_col_cea_val_aft_crm) {
		this.r215_crm_com_app_col_cea_val_aft_crm = r215_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR215_rwa_elig_cea_not_cov() {
		return r215_rwa_elig_cea_not_cov;
	}
	public void setR215_rwa_elig_cea_not_cov(BigDecimal r215_rwa_elig_cea_not_cov) {
		this.r215_rwa_elig_cea_not_cov = r215_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR215_rwa_unsec_cea_sub_cre_ris() {
		return r215_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR215_rwa_unsec_cea_sub_cre_ris(BigDecimal r215_rwa_unsec_cea_sub_cre_ris) {
		this.r215_rwa_unsec_cea_sub_cre_ris = r215_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR215_rwa_unsec_cea() {
		return r215_rwa_unsec_cea;
	}
	public void setR215_rwa_unsec_cea(BigDecimal r215_rwa_unsec_cea) {
		this.r215_rwa_unsec_cea = r215_rwa_unsec_cea;
	}
	public BigDecimal getR215_rwa_tot_ris_wei_ass() {
		return r215_rwa_tot_ris_wei_ass;
	}
	public void setR215_rwa_tot_ris_wei_ass(BigDecimal r215_rwa_tot_ris_wei_ass) {
		this.r215_rwa_tot_ris_wei_ass = r215_rwa_tot_ris_wei_ass;
	}
	public String getR216_exposure_class_off_bal() {
		return r216_exposure_class_off_bal;
	}
	public void setR216_exposure_class_off_bal(String r216_exposure_class_off_bal) {
		this.r216_exposure_class_off_bal = r216_exposure_class_off_bal;
	}
	public BigDecimal getR216_nom_pri_amt() {
		return r216_nom_pri_amt;
	}
	public void setR216_nom_pri_amt(BigDecimal r216_nom_pri_amt) {
		this.r216_nom_pri_amt = r216_nom_pri_amt;
	}
	public BigDecimal getR216_ccf() {
		return r216_ccf;
	}
	public void setR216_ccf(BigDecimal r216_ccf) {
		this.r216_ccf = r216_ccf;
	}
	public BigDecimal getR216_cea() {
		return r216_cea;
	}
	public void setR216_cea(BigDecimal r216_cea) {
		this.r216_cea = r216_cea;
	}
	public BigDecimal getR216_cea_elig_coun_bilt_net() {
		return r216_cea_elig_coun_bilt_net;
	}
	public void setR216_cea_elig_coun_bilt_net(BigDecimal r216_cea_elig_coun_bilt_net) {
		this.r216_cea_elig_coun_bilt_net = r216_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR216_cea_aft_net() {
		return r216_cea_aft_net;
	}
	public void setR216_cea_aft_net(BigDecimal r216_cea_aft_net) {
		this.r216_cea_aft_net = r216_cea_aft_net;
	}
	public BigDecimal getR216_crm_sub_app_cea_elig() {
		return r216_crm_sub_app_cea_elig;
	}
	public void setR216_crm_sub_app_cea_elig(BigDecimal r216_crm_sub_app_cea_elig) {
		this.r216_crm_sub_app_cea_elig = r216_crm_sub_app_cea_elig;
	}
	public BigDecimal getR216_crm_sub_app_non_col_guar_elig() {
		return r216_crm_sub_app_non_col_guar_elig;
	}
	public void setR216_crm_sub_app_non_col_guar_elig(BigDecimal r216_crm_sub_app_non_col_guar_elig) {
		this.r216_crm_sub_app_non_col_guar_elig = r216_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR216_crm_sub_app_non_col_cre_der() {
		return r216_crm_sub_app_non_col_cre_der;
	}
	public void setR216_crm_sub_app_non_col_cre_der(BigDecimal r216_crm_sub_app_non_col_cre_der) {
		this.r216_crm_sub_app_non_col_cre_der = r216_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR216_crm_sub_app_col_elig_cash() {
		return r216_crm_sub_app_col_elig_cash;
	}
	public void setR216_crm_sub_app_col_elig_cash(BigDecimal r216_crm_sub_app_col_elig_cash) {
		this.r216_crm_sub_app_col_elig_cash = r216_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR216_crm_sub_app_col_elig_tre_bills() {
		return r216_crm_sub_app_col_elig_tre_bills;
	}
	public void setR216_crm_sub_app_col_elig_tre_bills(BigDecimal r216_crm_sub_app_col_elig_tre_bills) {
		this.r216_crm_sub_app_col_elig_tre_bills = r216_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR216_crm_sub_app_col_elig_deb_sec() {
		return r216_crm_sub_app_col_elig_deb_sec;
	}
	public void setR216_crm_sub_app_col_elig_deb_sec(BigDecimal r216_crm_sub_app_col_elig_deb_sec) {
		this.r216_crm_sub_app_col_elig_deb_sec = r216_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR216_crm_sub_app_col_elig_euiq() {
		return r216_crm_sub_app_col_elig_euiq;
	}
	public void setR216_crm_sub_app_col_elig_euiq(BigDecimal r216_crm_sub_app_col_elig_euiq) {
		this.r216_crm_sub_app_col_elig_euiq = r216_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR216_crm_sub_app_col_elig_uni_tru() {
		return r216_crm_sub_app_col_elig_uni_tru;
	}
	public void setR216_crm_sub_app_col_elig_uni_tru(BigDecimal r216_crm_sub_app_col_elig_uni_tru) {
		this.r216_crm_sub_app_col_elig_uni_tru = r216_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR216_crm_sub_app_col_cea_cov() {
		return r216_crm_sub_app_col_cea_cov;
	}
	public void setR216_crm_sub_app_col_cea_cov(BigDecimal r216_crm_sub_app_col_cea_cov) {
		this.r216_crm_sub_app_col_cea_cov = r216_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR216_crm_sub_app_col_cea_not_cov() {
		return r216_crm_sub_app_col_cea_not_cov;
	}
	public void setR216_crm_sub_app_col_cea_not_cov(BigDecimal r216_crm_sub_app_col_cea_not_cov) {
		this.r216_crm_sub_app_col_cea_not_cov = r216_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR216_crm_sub_app_rwa_ris_wei_crm() {
		return r216_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR216_crm_sub_app_rwa_ris_wei_crm(BigDecimal r216_crm_sub_app_rwa_ris_wei_crm) {
		this.r216_crm_sub_app_rwa_ris_wei_crm = r216_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR216_crm_sub_app_rwa_ris_cea_cov() {
		return r216_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR216_crm_sub_app_rwa_ris_cea_cov(BigDecimal r216_crm_sub_app_rwa_ris_cea_cov) {
		this.r216_crm_sub_app_rwa_ris_cea_cov = r216_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR216_crm_sub_app_rwa_appl_org_coun() {
		return r216_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR216_crm_sub_app_rwa_appl_org_coun(BigDecimal r216_crm_sub_app_rwa_appl_org_coun) {
		this.r216_crm_sub_app_rwa_appl_org_coun = r216_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR216_crm_sub_app_rwa_ris_cea_not_cov() {
		return r216_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR216_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r216_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r216_crm_sub_app_rwa_ris_cea_not_cov = r216_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR216_crm_com_app_col_cea_elig_crm() {
		return r216_crm_com_app_col_cea_elig_crm;
	}
	public void setR216_crm_com_app_col_cea_elig_crm(BigDecimal r216_crm_com_app_col_cea_elig_crm) {
		this.r216_crm_com_app_col_cea_elig_crm = r216_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR216_crm_com_app_col_elig_cea_vol_adj() {
		return r216_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR216_crm_com_app_col_elig_cea_vol_adj(BigDecimal r216_crm_com_app_col_elig_cea_vol_adj) {
		this.r216_crm_com_app_col_elig_cea_vol_adj = r216_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR216_crm_com_app_col_elig_fin_hai() {
		return r216_crm_com_app_col_elig_fin_hai;
	}
	public void setR216_crm_com_app_col_elig_fin_hai(BigDecimal r216_crm_com_app_col_elig_fin_hai) {
		this.r216_crm_com_app_col_elig_fin_hai = r216_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR216_crm_com_app_col_cea_val_aft_crm() {
		return r216_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR216_crm_com_app_col_cea_val_aft_crm(BigDecimal r216_crm_com_app_col_cea_val_aft_crm) {
		this.r216_crm_com_app_col_cea_val_aft_crm = r216_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR216_rwa_elig_cea_not_cov() {
		return r216_rwa_elig_cea_not_cov;
	}
	public void setR216_rwa_elig_cea_not_cov(BigDecimal r216_rwa_elig_cea_not_cov) {
		this.r216_rwa_elig_cea_not_cov = r216_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR216_rwa_unsec_cea_sub_cre_ris() {
		return r216_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR216_rwa_unsec_cea_sub_cre_ris(BigDecimal r216_rwa_unsec_cea_sub_cre_ris) {
		this.r216_rwa_unsec_cea_sub_cre_ris = r216_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR216_rwa_unsec_cea() {
		return r216_rwa_unsec_cea;
	}
	public void setR216_rwa_unsec_cea(BigDecimal r216_rwa_unsec_cea) {
		this.r216_rwa_unsec_cea = r216_rwa_unsec_cea;
	}
	public BigDecimal getR216_rwa_tot_ris_wei_ass() {
		return r216_rwa_tot_ris_wei_ass;
	}
	public void setR216_rwa_tot_ris_wei_ass(BigDecimal r216_rwa_tot_ris_wei_ass) {
		this.r216_rwa_tot_ris_wei_ass = r216_rwa_tot_ris_wei_ass;
	}
	public String getR217_exposure_class_off_bal() {
		return r217_exposure_class_off_bal;
	}
	public void setR217_exposure_class_off_bal(String r217_exposure_class_off_bal) {
		this.r217_exposure_class_off_bal = r217_exposure_class_off_bal;
	}
	public BigDecimal getR217_nom_pri_amt() {
		return r217_nom_pri_amt;
	}
	public void setR217_nom_pri_amt(BigDecimal r217_nom_pri_amt) {
		this.r217_nom_pri_amt = r217_nom_pri_amt;
	}
	public BigDecimal getR217_ccf() {
		return r217_ccf;
	}
	public void setR217_ccf(BigDecimal r217_ccf) {
		this.r217_ccf = r217_ccf;
	}
	public BigDecimal getR217_cea() {
		return r217_cea;
	}
	public void setR217_cea(BigDecimal r217_cea) {
		this.r217_cea = r217_cea;
	}
	public BigDecimal getR217_cea_elig_coun_bilt_net() {
		return r217_cea_elig_coun_bilt_net;
	}
	public void setR217_cea_elig_coun_bilt_net(BigDecimal r217_cea_elig_coun_bilt_net) {
		this.r217_cea_elig_coun_bilt_net = r217_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR217_cea_aft_net() {
		return r217_cea_aft_net;
	}
	public void setR217_cea_aft_net(BigDecimal r217_cea_aft_net) {
		this.r217_cea_aft_net = r217_cea_aft_net;
	}
	public BigDecimal getR217_crm_sub_app_cea_elig() {
		return r217_crm_sub_app_cea_elig;
	}
	public void setR217_crm_sub_app_cea_elig(BigDecimal r217_crm_sub_app_cea_elig) {
		this.r217_crm_sub_app_cea_elig = r217_crm_sub_app_cea_elig;
	}
	public BigDecimal getR217_crm_sub_app_non_col_guar_elig() {
		return r217_crm_sub_app_non_col_guar_elig;
	}
	public void setR217_crm_sub_app_non_col_guar_elig(BigDecimal r217_crm_sub_app_non_col_guar_elig) {
		this.r217_crm_sub_app_non_col_guar_elig = r217_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR217_crm_sub_app_non_col_cre_der() {
		return r217_crm_sub_app_non_col_cre_der;
	}
	public void setR217_crm_sub_app_non_col_cre_der(BigDecimal r217_crm_sub_app_non_col_cre_der) {
		this.r217_crm_sub_app_non_col_cre_der = r217_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR217_crm_sub_app_col_elig_cash() {
		return r217_crm_sub_app_col_elig_cash;
	}
	public void setR217_crm_sub_app_col_elig_cash(BigDecimal r217_crm_sub_app_col_elig_cash) {
		this.r217_crm_sub_app_col_elig_cash = r217_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR217_crm_sub_app_col_elig_tre_bills() {
		return r217_crm_sub_app_col_elig_tre_bills;
	}
	public void setR217_crm_sub_app_col_elig_tre_bills(BigDecimal r217_crm_sub_app_col_elig_tre_bills) {
		this.r217_crm_sub_app_col_elig_tre_bills = r217_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR217_crm_sub_app_col_elig_deb_sec() {
		return r217_crm_sub_app_col_elig_deb_sec;
	}
	public void setR217_crm_sub_app_col_elig_deb_sec(BigDecimal r217_crm_sub_app_col_elig_deb_sec) {
		this.r217_crm_sub_app_col_elig_deb_sec = r217_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR217_crm_sub_app_col_elig_euiq() {
		return r217_crm_sub_app_col_elig_euiq;
	}
	public void setR217_crm_sub_app_col_elig_euiq(BigDecimal r217_crm_sub_app_col_elig_euiq) {
		this.r217_crm_sub_app_col_elig_euiq = r217_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR217_crm_sub_app_col_elig_uni_tru() {
		return r217_crm_sub_app_col_elig_uni_tru;
	}
	public void setR217_crm_sub_app_col_elig_uni_tru(BigDecimal r217_crm_sub_app_col_elig_uni_tru) {
		this.r217_crm_sub_app_col_elig_uni_tru = r217_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR217_crm_sub_app_col_cea_cov() {
		return r217_crm_sub_app_col_cea_cov;
	}
	public void setR217_crm_sub_app_col_cea_cov(BigDecimal r217_crm_sub_app_col_cea_cov) {
		this.r217_crm_sub_app_col_cea_cov = r217_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR217_crm_sub_app_col_cea_not_cov() {
		return r217_crm_sub_app_col_cea_not_cov;
	}
	public void setR217_crm_sub_app_col_cea_not_cov(BigDecimal r217_crm_sub_app_col_cea_not_cov) {
		this.r217_crm_sub_app_col_cea_not_cov = r217_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR217_crm_sub_app_rwa_ris_wei_crm() {
		return r217_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR217_crm_sub_app_rwa_ris_wei_crm(BigDecimal r217_crm_sub_app_rwa_ris_wei_crm) {
		this.r217_crm_sub_app_rwa_ris_wei_crm = r217_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR217_crm_sub_app_rwa_ris_cea_cov() {
		return r217_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR217_crm_sub_app_rwa_ris_cea_cov(BigDecimal r217_crm_sub_app_rwa_ris_cea_cov) {
		this.r217_crm_sub_app_rwa_ris_cea_cov = r217_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR217_crm_sub_app_rwa_appl_org_coun() {
		return r217_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR217_crm_sub_app_rwa_appl_org_coun(BigDecimal r217_crm_sub_app_rwa_appl_org_coun) {
		this.r217_crm_sub_app_rwa_appl_org_coun = r217_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR217_crm_sub_app_rwa_ris_cea_not_cov() {
		return r217_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR217_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r217_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r217_crm_sub_app_rwa_ris_cea_not_cov = r217_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR217_crm_com_app_col_cea_elig_crm() {
		return r217_crm_com_app_col_cea_elig_crm;
	}
	public void setR217_crm_com_app_col_cea_elig_crm(BigDecimal r217_crm_com_app_col_cea_elig_crm) {
		this.r217_crm_com_app_col_cea_elig_crm = r217_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR217_crm_com_app_col_elig_cea_vol_adj() {
		return r217_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR217_crm_com_app_col_elig_cea_vol_adj(BigDecimal r217_crm_com_app_col_elig_cea_vol_adj) {
		this.r217_crm_com_app_col_elig_cea_vol_adj = r217_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR217_crm_com_app_col_elig_fin_hai() {
		return r217_crm_com_app_col_elig_fin_hai;
	}
	public void setR217_crm_com_app_col_elig_fin_hai(BigDecimal r217_crm_com_app_col_elig_fin_hai) {
		this.r217_crm_com_app_col_elig_fin_hai = r217_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR217_crm_com_app_col_cea_val_aft_crm() {
		return r217_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR217_crm_com_app_col_cea_val_aft_crm(BigDecimal r217_crm_com_app_col_cea_val_aft_crm) {
		this.r217_crm_com_app_col_cea_val_aft_crm = r217_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR217_rwa_elig_cea_not_cov() {
		return r217_rwa_elig_cea_not_cov;
	}
	public void setR217_rwa_elig_cea_not_cov(BigDecimal r217_rwa_elig_cea_not_cov) {
		this.r217_rwa_elig_cea_not_cov = r217_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR217_rwa_unsec_cea_sub_cre_ris() {
		return r217_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR217_rwa_unsec_cea_sub_cre_ris(BigDecimal r217_rwa_unsec_cea_sub_cre_ris) {
		this.r217_rwa_unsec_cea_sub_cre_ris = r217_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR217_rwa_unsec_cea() {
		return r217_rwa_unsec_cea;
	}
	public void setR217_rwa_unsec_cea(BigDecimal r217_rwa_unsec_cea) {
		this.r217_rwa_unsec_cea = r217_rwa_unsec_cea;
	}
	public BigDecimal getR217_rwa_tot_ris_wei_ass() {
		return r217_rwa_tot_ris_wei_ass;
	}
	public void setR217_rwa_tot_ris_wei_ass(BigDecimal r217_rwa_tot_ris_wei_ass) {
		this.r217_rwa_tot_ris_wei_ass = r217_rwa_tot_ris_wei_ass;
	}
	public String getR218_exposure_class_off_bal() {
		return r218_exposure_class_off_bal;
	}
	public void setR218_exposure_class_off_bal(String r218_exposure_class_off_bal) {
		this.r218_exposure_class_off_bal = r218_exposure_class_off_bal;
	}
	public BigDecimal getR218_nom_pri_amt() {
		return r218_nom_pri_amt;
	}
	public void setR218_nom_pri_amt(BigDecimal r218_nom_pri_amt) {
		this.r218_nom_pri_amt = r218_nom_pri_amt;
	}
	public BigDecimal getR218_ccf() {
		return r218_ccf;
	}
	public void setR218_ccf(BigDecimal r218_ccf) {
		this.r218_ccf = r218_ccf;
	}
	public BigDecimal getR218_cea() {
		return r218_cea;
	}
	public void setR218_cea(BigDecimal r218_cea) {
		this.r218_cea = r218_cea;
	}
	public BigDecimal getR218_cea_elig_coun_bilt_net() {
		return r218_cea_elig_coun_bilt_net;
	}
	public void setR218_cea_elig_coun_bilt_net(BigDecimal r218_cea_elig_coun_bilt_net) {
		this.r218_cea_elig_coun_bilt_net = r218_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR218_cea_aft_net() {
		return r218_cea_aft_net;
	}
	public void setR218_cea_aft_net(BigDecimal r218_cea_aft_net) {
		this.r218_cea_aft_net = r218_cea_aft_net;
	}
	public BigDecimal getR218_crm_sub_app_cea_elig() {
		return r218_crm_sub_app_cea_elig;
	}
	public void setR218_crm_sub_app_cea_elig(BigDecimal r218_crm_sub_app_cea_elig) {
		this.r218_crm_sub_app_cea_elig = r218_crm_sub_app_cea_elig;
	}
	public BigDecimal getR218_crm_sub_app_non_col_guar_elig() {
		return r218_crm_sub_app_non_col_guar_elig;
	}
	public void setR218_crm_sub_app_non_col_guar_elig(BigDecimal r218_crm_sub_app_non_col_guar_elig) {
		this.r218_crm_sub_app_non_col_guar_elig = r218_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR218_crm_sub_app_non_col_cre_der() {
		return r218_crm_sub_app_non_col_cre_der;
	}
	public void setR218_crm_sub_app_non_col_cre_der(BigDecimal r218_crm_sub_app_non_col_cre_der) {
		this.r218_crm_sub_app_non_col_cre_der = r218_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR218_crm_sub_app_col_elig_cash() {
		return r218_crm_sub_app_col_elig_cash;
	}
	public void setR218_crm_sub_app_col_elig_cash(BigDecimal r218_crm_sub_app_col_elig_cash) {
		this.r218_crm_sub_app_col_elig_cash = r218_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR218_crm_sub_app_col_elig_tre_bills() {
		return r218_crm_sub_app_col_elig_tre_bills;
	}
	public void setR218_crm_sub_app_col_elig_tre_bills(BigDecimal r218_crm_sub_app_col_elig_tre_bills) {
		this.r218_crm_sub_app_col_elig_tre_bills = r218_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR218_crm_sub_app_col_elig_deb_sec() {
		return r218_crm_sub_app_col_elig_deb_sec;
	}
	public void setR218_crm_sub_app_col_elig_deb_sec(BigDecimal r218_crm_sub_app_col_elig_deb_sec) {
		this.r218_crm_sub_app_col_elig_deb_sec = r218_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR218_crm_sub_app_col_elig_euiq() {
		return r218_crm_sub_app_col_elig_euiq;
	}
	public void setR218_crm_sub_app_col_elig_euiq(BigDecimal r218_crm_sub_app_col_elig_euiq) {
		this.r218_crm_sub_app_col_elig_euiq = r218_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR218_crm_sub_app_col_elig_uni_tru() {
		return r218_crm_sub_app_col_elig_uni_tru;
	}
	public void setR218_crm_sub_app_col_elig_uni_tru(BigDecimal r218_crm_sub_app_col_elig_uni_tru) {
		this.r218_crm_sub_app_col_elig_uni_tru = r218_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR218_crm_sub_app_col_cea_cov() {
		return r218_crm_sub_app_col_cea_cov;
	}
	public void setR218_crm_sub_app_col_cea_cov(BigDecimal r218_crm_sub_app_col_cea_cov) {
		this.r218_crm_sub_app_col_cea_cov = r218_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR218_crm_sub_app_col_cea_not_cov() {
		return r218_crm_sub_app_col_cea_not_cov;
	}
	public void setR218_crm_sub_app_col_cea_not_cov(BigDecimal r218_crm_sub_app_col_cea_not_cov) {
		this.r218_crm_sub_app_col_cea_not_cov = r218_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR218_crm_sub_app_rwa_ris_wei_crm() {
		return r218_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR218_crm_sub_app_rwa_ris_wei_crm(BigDecimal r218_crm_sub_app_rwa_ris_wei_crm) {
		this.r218_crm_sub_app_rwa_ris_wei_crm = r218_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR218_crm_sub_app_rwa_ris_cea_cov() {
		return r218_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR218_crm_sub_app_rwa_ris_cea_cov(BigDecimal r218_crm_sub_app_rwa_ris_cea_cov) {
		this.r218_crm_sub_app_rwa_ris_cea_cov = r218_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR218_crm_sub_app_rwa_appl_org_coun() {
		return r218_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR218_crm_sub_app_rwa_appl_org_coun(BigDecimal r218_crm_sub_app_rwa_appl_org_coun) {
		this.r218_crm_sub_app_rwa_appl_org_coun = r218_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR218_crm_sub_app_rwa_ris_cea_not_cov() {
		return r218_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR218_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r218_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r218_crm_sub_app_rwa_ris_cea_not_cov = r218_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR218_crm_com_app_col_cea_elig_crm() {
		return r218_crm_com_app_col_cea_elig_crm;
	}
	public void setR218_crm_com_app_col_cea_elig_crm(BigDecimal r218_crm_com_app_col_cea_elig_crm) {
		this.r218_crm_com_app_col_cea_elig_crm = r218_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR218_crm_com_app_col_elig_cea_vol_adj() {
		return r218_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR218_crm_com_app_col_elig_cea_vol_adj(BigDecimal r218_crm_com_app_col_elig_cea_vol_adj) {
		this.r218_crm_com_app_col_elig_cea_vol_adj = r218_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR218_crm_com_app_col_elig_fin_hai() {
		return r218_crm_com_app_col_elig_fin_hai;
	}
	public void setR218_crm_com_app_col_elig_fin_hai(BigDecimal r218_crm_com_app_col_elig_fin_hai) {
		this.r218_crm_com_app_col_elig_fin_hai = r218_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR218_crm_com_app_col_cea_val_aft_crm() {
		return r218_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR218_crm_com_app_col_cea_val_aft_crm(BigDecimal r218_crm_com_app_col_cea_val_aft_crm) {
		this.r218_crm_com_app_col_cea_val_aft_crm = r218_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR218_rwa_elig_cea_not_cov() {
		return r218_rwa_elig_cea_not_cov;
	}
	public void setR218_rwa_elig_cea_not_cov(BigDecimal r218_rwa_elig_cea_not_cov) {
		this.r218_rwa_elig_cea_not_cov = r218_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR218_rwa_unsec_cea_sub_cre_ris() {
		return r218_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR218_rwa_unsec_cea_sub_cre_ris(BigDecimal r218_rwa_unsec_cea_sub_cre_ris) {
		this.r218_rwa_unsec_cea_sub_cre_ris = r218_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR218_rwa_unsec_cea() {
		return r218_rwa_unsec_cea;
	}
	public void setR218_rwa_unsec_cea(BigDecimal r218_rwa_unsec_cea) {
		this.r218_rwa_unsec_cea = r218_rwa_unsec_cea;
	}
	public BigDecimal getR218_rwa_tot_ris_wei_ass() {
		return r218_rwa_tot_ris_wei_ass;
	}
	public void setR218_rwa_tot_ris_wei_ass(BigDecimal r218_rwa_tot_ris_wei_ass) {
		this.r218_rwa_tot_ris_wei_ass = r218_rwa_tot_ris_wei_ass;
	}
	public String getR219_exposure_class_off_bal() {
		return r219_exposure_class_off_bal;
	}
	public void setR219_exposure_class_off_bal(String r219_exposure_class_off_bal) {
		this.r219_exposure_class_off_bal = r219_exposure_class_off_bal;
	}
	public BigDecimal getR219_nom_pri_amt() {
		return r219_nom_pri_amt;
	}
	public void setR219_nom_pri_amt(BigDecimal r219_nom_pri_amt) {
		this.r219_nom_pri_amt = r219_nom_pri_amt;
	}
	public BigDecimal getR219_ccf() {
		return r219_ccf;
	}
	public void setR219_ccf(BigDecimal r219_ccf) {
		this.r219_ccf = r219_ccf;
	}
	public BigDecimal getR219_cea() {
		return r219_cea;
	}
	public void setR219_cea(BigDecimal r219_cea) {
		this.r219_cea = r219_cea;
	}
	public BigDecimal getR219_cea_elig_coun_bilt_net() {
		return r219_cea_elig_coun_bilt_net;
	}
	public void setR219_cea_elig_coun_bilt_net(BigDecimal r219_cea_elig_coun_bilt_net) {
		this.r219_cea_elig_coun_bilt_net = r219_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR219_cea_aft_net() {
		return r219_cea_aft_net;
	}
	public void setR219_cea_aft_net(BigDecimal r219_cea_aft_net) {
		this.r219_cea_aft_net = r219_cea_aft_net;
	}
	public BigDecimal getR219_crm_sub_app_cea_elig() {
		return r219_crm_sub_app_cea_elig;
	}
	public void setR219_crm_sub_app_cea_elig(BigDecimal r219_crm_sub_app_cea_elig) {
		this.r219_crm_sub_app_cea_elig = r219_crm_sub_app_cea_elig;
	}
	public BigDecimal getR219_crm_sub_app_non_col_guar_elig() {
		return r219_crm_sub_app_non_col_guar_elig;
	}
	public void setR219_crm_sub_app_non_col_guar_elig(BigDecimal r219_crm_sub_app_non_col_guar_elig) {
		this.r219_crm_sub_app_non_col_guar_elig = r219_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR219_crm_sub_app_non_col_cre_der() {
		return r219_crm_sub_app_non_col_cre_der;
	}
	public void setR219_crm_sub_app_non_col_cre_der(BigDecimal r219_crm_sub_app_non_col_cre_der) {
		this.r219_crm_sub_app_non_col_cre_der = r219_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR219_crm_sub_app_col_elig_cash() {
		return r219_crm_sub_app_col_elig_cash;
	}
	public void setR219_crm_sub_app_col_elig_cash(BigDecimal r219_crm_sub_app_col_elig_cash) {
		this.r219_crm_sub_app_col_elig_cash = r219_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR219_crm_sub_app_col_elig_tre_bills() {
		return r219_crm_sub_app_col_elig_tre_bills;
	}
	public void setR219_crm_sub_app_col_elig_tre_bills(BigDecimal r219_crm_sub_app_col_elig_tre_bills) {
		this.r219_crm_sub_app_col_elig_tre_bills = r219_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR219_crm_sub_app_col_elig_deb_sec() {
		return r219_crm_sub_app_col_elig_deb_sec;
	}
	public void setR219_crm_sub_app_col_elig_deb_sec(BigDecimal r219_crm_sub_app_col_elig_deb_sec) {
		this.r219_crm_sub_app_col_elig_deb_sec = r219_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR219_crm_sub_app_col_elig_euiq() {
		return r219_crm_sub_app_col_elig_euiq;
	}
	public void setR219_crm_sub_app_col_elig_euiq(BigDecimal r219_crm_sub_app_col_elig_euiq) {
		this.r219_crm_sub_app_col_elig_euiq = r219_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR219_crm_sub_app_col_elig_uni_tru() {
		return r219_crm_sub_app_col_elig_uni_tru;
	}
	public void setR219_crm_sub_app_col_elig_uni_tru(BigDecimal r219_crm_sub_app_col_elig_uni_tru) {
		this.r219_crm_sub_app_col_elig_uni_tru = r219_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR219_crm_sub_app_col_cea_cov() {
		return r219_crm_sub_app_col_cea_cov;
	}
	public void setR219_crm_sub_app_col_cea_cov(BigDecimal r219_crm_sub_app_col_cea_cov) {
		this.r219_crm_sub_app_col_cea_cov = r219_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR219_crm_sub_app_col_cea_not_cov() {
		return r219_crm_sub_app_col_cea_not_cov;
	}
	public void setR219_crm_sub_app_col_cea_not_cov(BigDecimal r219_crm_sub_app_col_cea_not_cov) {
		this.r219_crm_sub_app_col_cea_not_cov = r219_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR219_crm_sub_app_rwa_ris_wei_crm() {
		return r219_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR219_crm_sub_app_rwa_ris_wei_crm(BigDecimal r219_crm_sub_app_rwa_ris_wei_crm) {
		this.r219_crm_sub_app_rwa_ris_wei_crm = r219_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR219_crm_sub_app_rwa_ris_cea_cov() {
		return r219_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR219_crm_sub_app_rwa_ris_cea_cov(BigDecimal r219_crm_sub_app_rwa_ris_cea_cov) {
		this.r219_crm_sub_app_rwa_ris_cea_cov = r219_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR219_crm_sub_app_rwa_appl_org_coun() {
		return r219_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR219_crm_sub_app_rwa_appl_org_coun(BigDecimal r219_crm_sub_app_rwa_appl_org_coun) {
		this.r219_crm_sub_app_rwa_appl_org_coun = r219_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR219_crm_sub_app_rwa_ris_cea_not_cov() {
		return r219_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR219_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r219_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r219_crm_sub_app_rwa_ris_cea_not_cov = r219_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR219_crm_com_app_col_cea_elig_crm() {
		return r219_crm_com_app_col_cea_elig_crm;
	}
	public void setR219_crm_com_app_col_cea_elig_crm(BigDecimal r219_crm_com_app_col_cea_elig_crm) {
		this.r219_crm_com_app_col_cea_elig_crm = r219_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR219_crm_com_app_col_elig_cea_vol_adj() {
		return r219_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR219_crm_com_app_col_elig_cea_vol_adj(BigDecimal r219_crm_com_app_col_elig_cea_vol_adj) {
		this.r219_crm_com_app_col_elig_cea_vol_adj = r219_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR219_crm_com_app_col_elig_fin_hai() {
		return r219_crm_com_app_col_elig_fin_hai;
	}
	public void setR219_crm_com_app_col_elig_fin_hai(BigDecimal r219_crm_com_app_col_elig_fin_hai) {
		this.r219_crm_com_app_col_elig_fin_hai = r219_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR219_crm_com_app_col_cea_val_aft_crm() {
		return r219_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR219_crm_com_app_col_cea_val_aft_crm(BigDecimal r219_crm_com_app_col_cea_val_aft_crm) {
		this.r219_crm_com_app_col_cea_val_aft_crm = r219_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR219_rwa_elig_cea_not_cov() {
		return r219_rwa_elig_cea_not_cov;
	}
	public void setR219_rwa_elig_cea_not_cov(BigDecimal r219_rwa_elig_cea_not_cov) {
		this.r219_rwa_elig_cea_not_cov = r219_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR219_rwa_unsec_cea_sub_cre_ris() {
		return r219_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR219_rwa_unsec_cea_sub_cre_ris(BigDecimal r219_rwa_unsec_cea_sub_cre_ris) {
		this.r219_rwa_unsec_cea_sub_cre_ris = r219_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR219_rwa_unsec_cea() {
		return r219_rwa_unsec_cea;
	}
	public void setR219_rwa_unsec_cea(BigDecimal r219_rwa_unsec_cea) {
		this.r219_rwa_unsec_cea = r219_rwa_unsec_cea;
	}
	public BigDecimal getR219_rwa_tot_ris_wei_ass() {
		return r219_rwa_tot_ris_wei_ass;
	}
	public void setR219_rwa_tot_ris_wei_ass(BigDecimal r219_rwa_tot_ris_wei_ass) {
		this.r219_rwa_tot_ris_wei_ass = r219_rwa_tot_ris_wei_ass;
	}
	public String getR220_exposure_class_off_bal() {
		return r220_exposure_class_off_bal;
	}
	public void setR220_exposure_class_off_bal(String r220_exposure_class_off_bal) {
		this.r220_exposure_class_off_bal = r220_exposure_class_off_bal;
	}
	public BigDecimal getR220_nom_pri_amt() {
		return r220_nom_pri_amt;
	}
	public void setR220_nom_pri_amt(BigDecimal r220_nom_pri_amt) {
		this.r220_nom_pri_amt = r220_nom_pri_amt;
	}
	public BigDecimal getR220_ccf() {
		return r220_ccf;
	}
	public void setR220_ccf(BigDecimal r220_ccf) {
		this.r220_ccf = r220_ccf;
	}
	public BigDecimal getR220_cea() {
		return r220_cea;
	}
	public void setR220_cea(BigDecimal r220_cea) {
		this.r220_cea = r220_cea;
	}
	public BigDecimal getR220_cea_elig_coun_bilt_net() {
		return r220_cea_elig_coun_bilt_net;
	}
	public void setR220_cea_elig_coun_bilt_net(BigDecimal r220_cea_elig_coun_bilt_net) {
		this.r220_cea_elig_coun_bilt_net = r220_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR220_cea_aft_net() {
		return r220_cea_aft_net;
	}
	public void setR220_cea_aft_net(BigDecimal r220_cea_aft_net) {
		this.r220_cea_aft_net = r220_cea_aft_net;
	}
	public BigDecimal getR220_crm_sub_app_cea_elig() {
		return r220_crm_sub_app_cea_elig;
	}
	public void setR220_crm_sub_app_cea_elig(BigDecimal r220_crm_sub_app_cea_elig) {
		this.r220_crm_sub_app_cea_elig = r220_crm_sub_app_cea_elig;
	}
	public BigDecimal getR220_crm_sub_app_non_col_guar_elig() {
		return r220_crm_sub_app_non_col_guar_elig;
	}
	public void setR220_crm_sub_app_non_col_guar_elig(BigDecimal r220_crm_sub_app_non_col_guar_elig) {
		this.r220_crm_sub_app_non_col_guar_elig = r220_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR220_crm_sub_app_non_col_cre_der() {
		return r220_crm_sub_app_non_col_cre_der;
	}
	public void setR220_crm_sub_app_non_col_cre_der(BigDecimal r220_crm_sub_app_non_col_cre_der) {
		this.r220_crm_sub_app_non_col_cre_der = r220_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR220_crm_sub_app_col_elig_cash() {
		return r220_crm_sub_app_col_elig_cash;
	}
	public void setR220_crm_sub_app_col_elig_cash(BigDecimal r220_crm_sub_app_col_elig_cash) {
		this.r220_crm_sub_app_col_elig_cash = r220_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR220_crm_sub_app_col_elig_tre_bills() {
		return r220_crm_sub_app_col_elig_tre_bills;
	}
	public void setR220_crm_sub_app_col_elig_tre_bills(BigDecimal r220_crm_sub_app_col_elig_tre_bills) {
		this.r220_crm_sub_app_col_elig_tre_bills = r220_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR220_crm_sub_app_col_elig_deb_sec() {
		return r220_crm_sub_app_col_elig_deb_sec;
	}
	public void setR220_crm_sub_app_col_elig_deb_sec(BigDecimal r220_crm_sub_app_col_elig_deb_sec) {
		this.r220_crm_sub_app_col_elig_deb_sec = r220_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR220_crm_sub_app_col_elig_euiq() {
		return r220_crm_sub_app_col_elig_euiq;
	}
	public void setR220_crm_sub_app_col_elig_euiq(BigDecimal r220_crm_sub_app_col_elig_euiq) {
		this.r220_crm_sub_app_col_elig_euiq = r220_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR220_crm_sub_app_col_elig_uni_tru() {
		return r220_crm_sub_app_col_elig_uni_tru;
	}
	public void setR220_crm_sub_app_col_elig_uni_tru(BigDecimal r220_crm_sub_app_col_elig_uni_tru) {
		this.r220_crm_sub_app_col_elig_uni_tru = r220_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR220_crm_sub_app_col_cea_cov() {
		return r220_crm_sub_app_col_cea_cov;
	}
	public void setR220_crm_sub_app_col_cea_cov(BigDecimal r220_crm_sub_app_col_cea_cov) {
		this.r220_crm_sub_app_col_cea_cov = r220_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR220_crm_sub_app_col_cea_not_cov() {
		return r220_crm_sub_app_col_cea_not_cov;
	}
	public void setR220_crm_sub_app_col_cea_not_cov(BigDecimal r220_crm_sub_app_col_cea_not_cov) {
		this.r220_crm_sub_app_col_cea_not_cov = r220_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR220_crm_sub_app_rwa_ris_wei_crm() {
		return r220_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR220_crm_sub_app_rwa_ris_wei_crm(BigDecimal r220_crm_sub_app_rwa_ris_wei_crm) {
		this.r220_crm_sub_app_rwa_ris_wei_crm = r220_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR220_crm_sub_app_rwa_ris_cea_cov() {
		return r220_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR220_crm_sub_app_rwa_ris_cea_cov(BigDecimal r220_crm_sub_app_rwa_ris_cea_cov) {
		this.r220_crm_sub_app_rwa_ris_cea_cov = r220_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR220_crm_sub_app_rwa_appl_org_coun() {
		return r220_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR220_crm_sub_app_rwa_appl_org_coun(BigDecimal r220_crm_sub_app_rwa_appl_org_coun) {
		this.r220_crm_sub_app_rwa_appl_org_coun = r220_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR220_crm_sub_app_rwa_ris_cea_not_cov() {
		return r220_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR220_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r220_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r220_crm_sub_app_rwa_ris_cea_not_cov = r220_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR220_crm_com_app_col_cea_elig_crm() {
		return r220_crm_com_app_col_cea_elig_crm;
	}
	public void setR220_crm_com_app_col_cea_elig_crm(BigDecimal r220_crm_com_app_col_cea_elig_crm) {
		this.r220_crm_com_app_col_cea_elig_crm = r220_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR220_crm_com_app_col_elig_cea_vol_adj() {
		return r220_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR220_crm_com_app_col_elig_cea_vol_adj(BigDecimal r220_crm_com_app_col_elig_cea_vol_adj) {
		this.r220_crm_com_app_col_elig_cea_vol_adj = r220_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR220_crm_com_app_col_elig_fin_hai() {
		return r220_crm_com_app_col_elig_fin_hai;
	}
	public void setR220_crm_com_app_col_elig_fin_hai(BigDecimal r220_crm_com_app_col_elig_fin_hai) {
		this.r220_crm_com_app_col_elig_fin_hai = r220_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR220_crm_com_app_col_cea_val_aft_crm() {
		return r220_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR220_crm_com_app_col_cea_val_aft_crm(BigDecimal r220_crm_com_app_col_cea_val_aft_crm) {
		this.r220_crm_com_app_col_cea_val_aft_crm = r220_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR220_rwa_elig_cea_not_cov() {
		return r220_rwa_elig_cea_not_cov;
	}
	public void setR220_rwa_elig_cea_not_cov(BigDecimal r220_rwa_elig_cea_not_cov) {
		this.r220_rwa_elig_cea_not_cov = r220_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR220_rwa_unsec_cea_sub_cre_ris() {
		return r220_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR220_rwa_unsec_cea_sub_cre_ris(BigDecimal r220_rwa_unsec_cea_sub_cre_ris) {
		this.r220_rwa_unsec_cea_sub_cre_ris = r220_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR220_rwa_unsec_cea() {
		return r220_rwa_unsec_cea;
	}
	public void setR220_rwa_unsec_cea(BigDecimal r220_rwa_unsec_cea) {
		this.r220_rwa_unsec_cea = r220_rwa_unsec_cea;
	}
	public BigDecimal getR220_rwa_tot_ris_wei_ass() {
		return r220_rwa_tot_ris_wei_ass;
	}
	public void setR220_rwa_tot_ris_wei_ass(BigDecimal r220_rwa_tot_ris_wei_ass) {
		this.r220_rwa_tot_ris_wei_ass = r220_rwa_tot_ris_wei_ass;
	}
	public String getR221_exposure_class_off_bal() {
		return r221_exposure_class_off_bal;
	}
	public void setR221_exposure_class_off_bal(String r221_exposure_class_off_bal) {
		this.r221_exposure_class_off_bal = r221_exposure_class_off_bal;
	}
	public BigDecimal getR221_nom_pri_amt() {
		return r221_nom_pri_amt;
	}
	public void setR221_nom_pri_amt(BigDecimal r221_nom_pri_amt) {
		this.r221_nom_pri_amt = r221_nom_pri_amt;
	}
	public BigDecimal getR221_ccf() {
		return r221_ccf;
	}
	public void setR221_ccf(BigDecimal r221_ccf) {
		this.r221_ccf = r221_ccf;
	}
	public BigDecimal getR221_cea() {
		return r221_cea;
	}
	public void setR221_cea(BigDecimal r221_cea) {
		this.r221_cea = r221_cea;
	}
	public BigDecimal getR221_cea_elig_coun_bilt_net() {
		return r221_cea_elig_coun_bilt_net;
	}
	public void setR221_cea_elig_coun_bilt_net(BigDecimal r221_cea_elig_coun_bilt_net) {
		this.r221_cea_elig_coun_bilt_net = r221_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR221_cea_aft_net() {
		return r221_cea_aft_net;
	}
	public void setR221_cea_aft_net(BigDecimal r221_cea_aft_net) {
		this.r221_cea_aft_net = r221_cea_aft_net;
	}
	public BigDecimal getR221_crm_sub_app_cea_elig() {
		return r221_crm_sub_app_cea_elig;
	}
	public void setR221_crm_sub_app_cea_elig(BigDecimal r221_crm_sub_app_cea_elig) {
		this.r221_crm_sub_app_cea_elig = r221_crm_sub_app_cea_elig;
	}
	public BigDecimal getR221_crm_sub_app_non_col_guar_elig() {
		return r221_crm_sub_app_non_col_guar_elig;
	}
	public void setR221_crm_sub_app_non_col_guar_elig(BigDecimal r221_crm_sub_app_non_col_guar_elig) {
		this.r221_crm_sub_app_non_col_guar_elig = r221_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR221_crm_sub_app_non_col_cre_der() {
		return r221_crm_sub_app_non_col_cre_der;
	}
	public void setR221_crm_sub_app_non_col_cre_der(BigDecimal r221_crm_sub_app_non_col_cre_der) {
		this.r221_crm_sub_app_non_col_cre_der = r221_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR221_crm_sub_app_col_elig_cash() {
		return r221_crm_sub_app_col_elig_cash;
	}
	public void setR221_crm_sub_app_col_elig_cash(BigDecimal r221_crm_sub_app_col_elig_cash) {
		this.r221_crm_sub_app_col_elig_cash = r221_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR221_crm_sub_app_col_elig_tre_bills() {
		return r221_crm_sub_app_col_elig_tre_bills;
	}
	public void setR221_crm_sub_app_col_elig_tre_bills(BigDecimal r221_crm_sub_app_col_elig_tre_bills) {
		this.r221_crm_sub_app_col_elig_tre_bills = r221_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR221_crm_sub_app_col_elig_deb_sec() {
		return r221_crm_sub_app_col_elig_deb_sec;
	}
	public void setR221_crm_sub_app_col_elig_deb_sec(BigDecimal r221_crm_sub_app_col_elig_deb_sec) {
		this.r221_crm_sub_app_col_elig_deb_sec = r221_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR221_crm_sub_app_col_elig_euiq() {
		return r221_crm_sub_app_col_elig_euiq;
	}
	public void setR221_crm_sub_app_col_elig_euiq(BigDecimal r221_crm_sub_app_col_elig_euiq) {
		this.r221_crm_sub_app_col_elig_euiq = r221_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR221_crm_sub_app_col_elig_uni_tru() {
		return r221_crm_sub_app_col_elig_uni_tru;
	}
	public void setR221_crm_sub_app_col_elig_uni_tru(BigDecimal r221_crm_sub_app_col_elig_uni_tru) {
		this.r221_crm_sub_app_col_elig_uni_tru = r221_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR221_crm_sub_app_col_cea_cov() {
		return r221_crm_sub_app_col_cea_cov;
	}
	public void setR221_crm_sub_app_col_cea_cov(BigDecimal r221_crm_sub_app_col_cea_cov) {
		this.r221_crm_sub_app_col_cea_cov = r221_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR221_crm_sub_app_col_cea_not_cov() {
		return r221_crm_sub_app_col_cea_not_cov;
	}
	public void setR221_crm_sub_app_col_cea_not_cov(BigDecimal r221_crm_sub_app_col_cea_not_cov) {
		this.r221_crm_sub_app_col_cea_not_cov = r221_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR221_crm_sub_app_rwa_ris_wei_crm() {
		return r221_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR221_crm_sub_app_rwa_ris_wei_crm(BigDecimal r221_crm_sub_app_rwa_ris_wei_crm) {
		this.r221_crm_sub_app_rwa_ris_wei_crm = r221_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR221_crm_sub_app_rwa_ris_cea_cov() {
		return r221_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR221_crm_sub_app_rwa_ris_cea_cov(BigDecimal r221_crm_sub_app_rwa_ris_cea_cov) {
		this.r221_crm_sub_app_rwa_ris_cea_cov = r221_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR221_crm_sub_app_rwa_appl_org_coun() {
		return r221_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR221_crm_sub_app_rwa_appl_org_coun(BigDecimal r221_crm_sub_app_rwa_appl_org_coun) {
		this.r221_crm_sub_app_rwa_appl_org_coun = r221_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR221_crm_sub_app_rwa_ris_cea_not_cov() {
		return r221_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR221_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r221_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r221_crm_sub_app_rwa_ris_cea_not_cov = r221_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR221_crm_com_app_col_cea_elig_crm() {
		return r221_crm_com_app_col_cea_elig_crm;
	}
	public void setR221_crm_com_app_col_cea_elig_crm(BigDecimal r221_crm_com_app_col_cea_elig_crm) {
		this.r221_crm_com_app_col_cea_elig_crm = r221_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR221_crm_com_app_col_elig_cea_vol_adj() {
		return r221_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR221_crm_com_app_col_elig_cea_vol_adj(BigDecimal r221_crm_com_app_col_elig_cea_vol_adj) {
		this.r221_crm_com_app_col_elig_cea_vol_adj = r221_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR221_crm_com_app_col_elig_fin_hai() {
		return r221_crm_com_app_col_elig_fin_hai;
	}
	public void setR221_crm_com_app_col_elig_fin_hai(BigDecimal r221_crm_com_app_col_elig_fin_hai) {
		this.r221_crm_com_app_col_elig_fin_hai = r221_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR221_crm_com_app_col_cea_val_aft_crm() {
		return r221_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR221_crm_com_app_col_cea_val_aft_crm(BigDecimal r221_crm_com_app_col_cea_val_aft_crm) {
		this.r221_crm_com_app_col_cea_val_aft_crm = r221_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR221_rwa_elig_cea_not_cov() {
		return r221_rwa_elig_cea_not_cov;
	}
	public void setR221_rwa_elig_cea_not_cov(BigDecimal r221_rwa_elig_cea_not_cov) {
		this.r221_rwa_elig_cea_not_cov = r221_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR221_rwa_unsec_cea_sub_cre_ris() {
		return r221_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR221_rwa_unsec_cea_sub_cre_ris(BigDecimal r221_rwa_unsec_cea_sub_cre_ris) {
		this.r221_rwa_unsec_cea_sub_cre_ris = r221_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR221_rwa_unsec_cea() {
		return r221_rwa_unsec_cea;
	}
	public void setR221_rwa_unsec_cea(BigDecimal r221_rwa_unsec_cea) {
		this.r221_rwa_unsec_cea = r221_rwa_unsec_cea;
	}
	public BigDecimal getR221_rwa_tot_ris_wei_ass() {
		return r221_rwa_tot_ris_wei_ass;
	}
	public void setR221_rwa_tot_ris_wei_ass(BigDecimal r221_rwa_tot_ris_wei_ass) {
		this.r221_rwa_tot_ris_wei_ass = r221_rwa_tot_ris_wei_ass;
	}
	public String getR222_exposure_class_off_bal() {
		return r222_exposure_class_off_bal;
	}
	public void setR222_exposure_class_off_bal(String r222_exposure_class_off_bal) {
		this.r222_exposure_class_off_bal = r222_exposure_class_off_bal;
	}
	public BigDecimal getR222_nom_pri_amt() {
		return r222_nom_pri_amt;
	}
	public void setR222_nom_pri_amt(BigDecimal r222_nom_pri_amt) {
		this.r222_nom_pri_amt = r222_nom_pri_amt;
	}
	public BigDecimal getR222_ccf() {
		return r222_ccf;
	}
	public void setR222_ccf(BigDecimal r222_ccf) {
		this.r222_ccf = r222_ccf;
	}
	public BigDecimal getR222_cea() {
		return r222_cea;
	}
	public void setR222_cea(BigDecimal r222_cea) {
		this.r222_cea = r222_cea;
	}
	public BigDecimal getR222_cea_elig_coun_bilt_net() {
		return r222_cea_elig_coun_bilt_net;
	}
	public void setR222_cea_elig_coun_bilt_net(BigDecimal r222_cea_elig_coun_bilt_net) {
		this.r222_cea_elig_coun_bilt_net = r222_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR222_cea_aft_net() {
		return r222_cea_aft_net;
	}
	public void setR222_cea_aft_net(BigDecimal r222_cea_aft_net) {
		this.r222_cea_aft_net = r222_cea_aft_net;
	}
	public BigDecimal getR222_crm_sub_app_cea_elig() {
		return r222_crm_sub_app_cea_elig;
	}
	public void setR222_crm_sub_app_cea_elig(BigDecimal r222_crm_sub_app_cea_elig) {
		this.r222_crm_sub_app_cea_elig = r222_crm_sub_app_cea_elig;
	}
	public BigDecimal getR222_crm_sub_app_non_col_guar_elig() {
		return r222_crm_sub_app_non_col_guar_elig;
	}
	public void setR222_crm_sub_app_non_col_guar_elig(BigDecimal r222_crm_sub_app_non_col_guar_elig) {
		this.r222_crm_sub_app_non_col_guar_elig = r222_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR222_crm_sub_app_non_col_cre_der() {
		return r222_crm_sub_app_non_col_cre_der;
	}
	public void setR222_crm_sub_app_non_col_cre_der(BigDecimal r222_crm_sub_app_non_col_cre_der) {
		this.r222_crm_sub_app_non_col_cre_der = r222_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR222_crm_sub_app_col_elig_cash() {
		return r222_crm_sub_app_col_elig_cash;
	}
	public void setR222_crm_sub_app_col_elig_cash(BigDecimal r222_crm_sub_app_col_elig_cash) {
		this.r222_crm_sub_app_col_elig_cash = r222_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR222_crm_sub_app_col_elig_tre_bills() {
		return r222_crm_sub_app_col_elig_tre_bills;
	}
	public void setR222_crm_sub_app_col_elig_tre_bills(BigDecimal r222_crm_sub_app_col_elig_tre_bills) {
		this.r222_crm_sub_app_col_elig_tre_bills = r222_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR222_crm_sub_app_col_elig_deb_sec() {
		return r222_crm_sub_app_col_elig_deb_sec;
	}
	public void setR222_crm_sub_app_col_elig_deb_sec(BigDecimal r222_crm_sub_app_col_elig_deb_sec) {
		this.r222_crm_sub_app_col_elig_deb_sec = r222_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR222_crm_sub_app_col_elig_euiq() {
		return r222_crm_sub_app_col_elig_euiq;
	}
	public void setR222_crm_sub_app_col_elig_euiq(BigDecimal r222_crm_sub_app_col_elig_euiq) {
		this.r222_crm_sub_app_col_elig_euiq = r222_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR222_crm_sub_app_col_elig_uni_tru() {
		return r222_crm_sub_app_col_elig_uni_tru;
	}
	public void setR222_crm_sub_app_col_elig_uni_tru(BigDecimal r222_crm_sub_app_col_elig_uni_tru) {
		this.r222_crm_sub_app_col_elig_uni_tru = r222_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR222_crm_sub_app_col_cea_cov() {
		return r222_crm_sub_app_col_cea_cov;
	}
	public void setR222_crm_sub_app_col_cea_cov(BigDecimal r222_crm_sub_app_col_cea_cov) {
		this.r222_crm_sub_app_col_cea_cov = r222_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR222_crm_sub_app_col_cea_not_cov() {
		return r222_crm_sub_app_col_cea_not_cov;
	}
	public void setR222_crm_sub_app_col_cea_not_cov(BigDecimal r222_crm_sub_app_col_cea_not_cov) {
		this.r222_crm_sub_app_col_cea_not_cov = r222_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR222_crm_sub_app_rwa_ris_wei_crm() {
		return r222_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR222_crm_sub_app_rwa_ris_wei_crm(BigDecimal r222_crm_sub_app_rwa_ris_wei_crm) {
		this.r222_crm_sub_app_rwa_ris_wei_crm = r222_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR222_crm_sub_app_rwa_ris_cea_cov() {
		return r222_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR222_crm_sub_app_rwa_ris_cea_cov(BigDecimal r222_crm_sub_app_rwa_ris_cea_cov) {
		this.r222_crm_sub_app_rwa_ris_cea_cov = r222_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR222_crm_sub_app_rwa_appl_org_coun() {
		return r222_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR222_crm_sub_app_rwa_appl_org_coun(BigDecimal r222_crm_sub_app_rwa_appl_org_coun) {
		this.r222_crm_sub_app_rwa_appl_org_coun = r222_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR222_crm_sub_app_rwa_ris_cea_not_cov() {
		return r222_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR222_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r222_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r222_crm_sub_app_rwa_ris_cea_not_cov = r222_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR222_crm_com_app_col_cea_elig_crm() {
		return r222_crm_com_app_col_cea_elig_crm;
	}
	public void setR222_crm_com_app_col_cea_elig_crm(BigDecimal r222_crm_com_app_col_cea_elig_crm) {
		this.r222_crm_com_app_col_cea_elig_crm = r222_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR222_crm_com_app_col_elig_cea_vol_adj() {
		return r222_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR222_crm_com_app_col_elig_cea_vol_adj(BigDecimal r222_crm_com_app_col_elig_cea_vol_adj) {
		this.r222_crm_com_app_col_elig_cea_vol_adj = r222_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR222_crm_com_app_col_elig_fin_hai() {
		return r222_crm_com_app_col_elig_fin_hai;
	}
	public void setR222_crm_com_app_col_elig_fin_hai(BigDecimal r222_crm_com_app_col_elig_fin_hai) {
		this.r222_crm_com_app_col_elig_fin_hai = r222_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR222_crm_com_app_col_cea_val_aft_crm() {
		return r222_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR222_crm_com_app_col_cea_val_aft_crm(BigDecimal r222_crm_com_app_col_cea_val_aft_crm) {
		this.r222_crm_com_app_col_cea_val_aft_crm = r222_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR222_rwa_elig_cea_not_cov() {
		return r222_rwa_elig_cea_not_cov;
	}
	public void setR222_rwa_elig_cea_not_cov(BigDecimal r222_rwa_elig_cea_not_cov) {
		this.r222_rwa_elig_cea_not_cov = r222_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR222_rwa_unsec_cea_sub_cre_ris() {
		return r222_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR222_rwa_unsec_cea_sub_cre_ris(BigDecimal r222_rwa_unsec_cea_sub_cre_ris) {
		this.r222_rwa_unsec_cea_sub_cre_ris = r222_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR222_rwa_unsec_cea() {
		return r222_rwa_unsec_cea;
	}
	public void setR222_rwa_unsec_cea(BigDecimal r222_rwa_unsec_cea) {
		this.r222_rwa_unsec_cea = r222_rwa_unsec_cea;
	}
	public BigDecimal getR222_rwa_tot_ris_wei_ass() {
		return r222_rwa_tot_ris_wei_ass;
	}
	public void setR222_rwa_tot_ris_wei_ass(BigDecimal r222_rwa_tot_ris_wei_ass) {
		this.r222_rwa_tot_ris_wei_ass = r222_rwa_tot_ris_wei_ass;
	}
	public String getR223_exposure_class_off_bal() {
		return r223_exposure_class_off_bal;
	}
	public void setR223_exposure_class_off_bal(String r223_exposure_class_off_bal) {
		this.r223_exposure_class_off_bal = r223_exposure_class_off_bal;
	}
	public BigDecimal getR223_nom_pri_amt() {
		return r223_nom_pri_amt;
	}
	public void setR223_nom_pri_amt(BigDecimal r223_nom_pri_amt) {
		this.r223_nom_pri_amt = r223_nom_pri_amt;
	}
	public BigDecimal getR223_ccf() {
		return r223_ccf;
	}
	public void setR223_ccf(BigDecimal r223_ccf) {
		this.r223_ccf = r223_ccf;
	}
	public BigDecimal getR223_cea() {
		return r223_cea;
	}
	public void setR223_cea(BigDecimal r223_cea) {
		this.r223_cea = r223_cea;
	}
	public BigDecimal getR223_cea_elig_coun_bilt_net() {
		return r223_cea_elig_coun_bilt_net;
	}
	public void setR223_cea_elig_coun_bilt_net(BigDecimal r223_cea_elig_coun_bilt_net) {
		this.r223_cea_elig_coun_bilt_net = r223_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR223_cea_aft_net() {
		return r223_cea_aft_net;
	}
	public void setR223_cea_aft_net(BigDecimal r223_cea_aft_net) {
		this.r223_cea_aft_net = r223_cea_aft_net;
	}
	public BigDecimal getR223_crm_sub_app_cea_elig() {
		return r223_crm_sub_app_cea_elig;
	}
	public void setR223_crm_sub_app_cea_elig(BigDecimal r223_crm_sub_app_cea_elig) {
		this.r223_crm_sub_app_cea_elig = r223_crm_sub_app_cea_elig;
	}
	public BigDecimal getR223_crm_sub_app_non_col_guar_elig() {
		return r223_crm_sub_app_non_col_guar_elig;
	}
	public void setR223_crm_sub_app_non_col_guar_elig(BigDecimal r223_crm_sub_app_non_col_guar_elig) {
		this.r223_crm_sub_app_non_col_guar_elig = r223_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR223_crm_sub_app_non_col_cre_der() {
		return r223_crm_sub_app_non_col_cre_der;
	}
	public void setR223_crm_sub_app_non_col_cre_der(BigDecimal r223_crm_sub_app_non_col_cre_der) {
		this.r223_crm_sub_app_non_col_cre_der = r223_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR223_crm_sub_app_col_elig_cash() {
		return r223_crm_sub_app_col_elig_cash;
	}
	public void setR223_crm_sub_app_col_elig_cash(BigDecimal r223_crm_sub_app_col_elig_cash) {
		this.r223_crm_sub_app_col_elig_cash = r223_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR223_crm_sub_app_col_elig_tre_bills() {
		return r223_crm_sub_app_col_elig_tre_bills;
	}
	public void setR223_crm_sub_app_col_elig_tre_bills(BigDecimal r223_crm_sub_app_col_elig_tre_bills) {
		this.r223_crm_sub_app_col_elig_tre_bills = r223_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR223_crm_sub_app_col_elig_deb_sec() {
		return r223_crm_sub_app_col_elig_deb_sec;
	}
	public void setR223_crm_sub_app_col_elig_deb_sec(BigDecimal r223_crm_sub_app_col_elig_deb_sec) {
		this.r223_crm_sub_app_col_elig_deb_sec = r223_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR223_crm_sub_app_col_elig_euiq() {
		return r223_crm_sub_app_col_elig_euiq;
	}
	public void setR223_crm_sub_app_col_elig_euiq(BigDecimal r223_crm_sub_app_col_elig_euiq) {
		this.r223_crm_sub_app_col_elig_euiq = r223_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR223_crm_sub_app_col_elig_uni_tru() {
		return r223_crm_sub_app_col_elig_uni_tru;
	}
	public void setR223_crm_sub_app_col_elig_uni_tru(BigDecimal r223_crm_sub_app_col_elig_uni_tru) {
		this.r223_crm_sub_app_col_elig_uni_tru = r223_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR223_crm_sub_app_col_cea_cov() {
		return r223_crm_sub_app_col_cea_cov;
	}
	public void setR223_crm_sub_app_col_cea_cov(BigDecimal r223_crm_sub_app_col_cea_cov) {
		this.r223_crm_sub_app_col_cea_cov = r223_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR223_crm_sub_app_col_cea_not_cov() {
		return r223_crm_sub_app_col_cea_not_cov;
	}
	public void setR223_crm_sub_app_col_cea_not_cov(BigDecimal r223_crm_sub_app_col_cea_not_cov) {
		this.r223_crm_sub_app_col_cea_not_cov = r223_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR223_crm_sub_app_rwa_ris_wei_crm() {
		return r223_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR223_crm_sub_app_rwa_ris_wei_crm(BigDecimal r223_crm_sub_app_rwa_ris_wei_crm) {
		this.r223_crm_sub_app_rwa_ris_wei_crm = r223_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR223_crm_sub_app_rwa_ris_cea_cov() {
		return r223_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR223_crm_sub_app_rwa_ris_cea_cov(BigDecimal r223_crm_sub_app_rwa_ris_cea_cov) {
		this.r223_crm_sub_app_rwa_ris_cea_cov = r223_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR223_crm_sub_app_rwa_appl_org_coun() {
		return r223_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR223_crm_sub_app_rwa_appl_org_coun(BigDecimal r223_crm_sub_app_rwa_appl_org_coun) {
		this.r223_crm_sub_app_rwa_appl_org_coun = r223_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR223_crm_sub_app_rwa_ris_cea_not_cov() {
		return r223_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR223_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r223_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r223_crm_sub_app_rwa_ris_cea_not_cov = r223_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR223_crm_com_app_col_cea_elig_crm() {
		return r223_crm_com_app_col_cea_elig_crm;
	}
	public void setR223_crm_com_app_col_cea_elig_crm(BigDecimal r223_crm_com_app_col_cea_elig_crm) {
		this.r223_crm_com_app_col_cea_elig_crm = r223_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR223_crm_com_app_col_elig_cea_vol_adj() {
		return r223_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR223_crm_com_app_col_elig_cea_vol_adj(BigDecimal r223_crm_com_app_col_elig_cea_vol_adj) {
		this.r223_crm_com_app_col_elig_cea_vol_adj = r223_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR223_crm_com_app_col_elig_fin_hai() {
		return r223_crm_com_app_col_elig_fin_hai;
	}
	public void setR223_crm_com_app_col_elig_fin_hai(BigDecimal r223_crm_com_app_col_elig_fin_hai) {
		this.r223_crm_com_app_col_elig_fin_hai = r223_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR223_crm_com_app_col_cea_val_aft_crm() {
		return r223_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR223_crm_com_app_col_cea_val_aft_crm(BigDecimal r223_crm_com_app_col_cea_val_aft_crm) {
		this.r223_crm_com_app_col_cea_val_aft_crm = r223_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR223_rwa_elig_cea_not_cov() {
		return r223_rwa_elig_cea_not_cov;
	}
	public void setR223_rwa_elig_cea_not_cov(BigDecimal r223_rwa_elig_cea_not_cov) {
		this.r223_rwa_elig_cea_not_cov = r223_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR223_rwa_unsec_cea_sub_cre_ris() {
		return r223_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR223_rwa_unsec_cea_sub_cre_ris(BigDecimal r223_rwa_unsec_cea_sub_cre_ris) {
		this.r223_rwa_unsec_cea_sub_cre_ris = r223_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR223_rwa_unsec_cea() {
		return r223_rwa_unsec_cea;
	}
	public void setR223_rwa_unsec_cea(BigDecimal r223_rwa_unsec_cea) {
		this.r223_rwa_unsec_cea = r223_rwa_unsec_cea;
	}
	public BigDecimal getR223_rwa_tot_ris_wei_ass() {
		return r223_rwa_tot_ris_wei_ass;
	}
	public void setR223_rwa_tot_ris_wei_ass(BigDecimal r223_rwa_tot_ris_wei_ass) {
		this.r223_rwa_tot_ris_wei_ass = r223_rwa_tot_ris_wei_ass;
	}
	public String getR224_exposure_class_off_bal() {
		return r224_exposure_class_off_bal;
	}
	public void setR224_exposure_class_off_bal(String r224_exposure_class_off_bal) {
		this.r224_exposure_class_off_bal = r224_exposure_class_off_bal;
	}
	public BigDecimal getR224_nom_pri_amt() {
		return r224_nom_pri_amt;
	}
	public void setR224_nom_pri_amt(BigDecimal r224_nom_pri_amt) {
		this.r224_nom_pri_amt = r224_nom_pri_amt;
	}
	public BigDecimal getR224_ccf() {
		return r224_ccf;
	}
	public void setR224_ccf(BigDecimal r224_ccf) {
		this.r224_ccf = r224_ccf;
	}
	public BigDecimal getR224_cea() {
		return r224_cea;
	}
	public void setR224_cea(BigDecimal r224_cea) {
		this.r224_cea = r224_cea;
	}
	public BigDecimal getR224_cea_elig_coun_bilt_net() {
		return r224_cea_elig_coun_bilt_net;
	}
	public void setR224_cea_elig_coun_bilt_net(BigDecimal r224_cea_elig_coun_bilt_net) {
		this.r224_cea_elig_coun_bilt_net = r224_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR224_cea_aft_net() {
		return r224_cea_aft_net;
	}
	public void setR224_cea_aft_net(BigDecimal r224_cea_aft_net) {
		this.r224_cea_aft_net = r224_cea_aft_net;
	}
	public BigDecimal getR224_crm_sub_app_cea_elig() {
		return r224_crm_sub_app_cea_elig;
	}
	public void setR224_crm_sub_app_cea_elig(BigDecimal r224_crm_sub_app_cea_elig) {
		this.r224_crm_sub_app_cea_elig = r224_crm_sub_app_cea_elig;
	}
	public BigDecimal getR224_crm_sub_app_non_col_guar_elig() {
		return r224_crm_sub_app_non_col_guar_elig;
	}
	public void setR224_crm_sub_app_non_col_guar_elig(BigDecimal r224_crm_sub_app_non_col_guar_elig) {
		this.r224_crm_sub_app_non_col_guar_elig = r224_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR224_crm_sub_app_non_col_cre_der() {
		return r224_crm_sub_app_non_col_cre_der;
	}
	public void setR224_crm_sub_app_non_col_cre_der(BigDecimal r224_crm_sub_app_non_col_cre_der) {
		this.r224_crm_sub_app_non_col_cre_der = r224_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR224_crm_sub_app_col_elig_cash() {
		return r224_crm_sub_app_col_elig_cash;
	}
	public void setR224_crm_sub_app_col_elig_cash(BigDecimal r224_crm_sub_app_col_elig_cash) {
		this.r224_crm_sub_app_col_elig_cash = r224_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR224_crm_sub_app_col_elig_tre_bills() {
		return r224_crm_sub_app_col_elig_tre_bills;
	}
	public void setR224_crm_sub_app_col_elig_tre_bills(BigDecimal r224_crm_sub_app_col_elig_tre_bills) {
		this.r224_crm_sub_app_col_elig_tre_bills = r224_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR224_crm_sub_app_col_elig_deb_sec() {
		return r224_crm_sub_app_col_elig_deb_sec;
	}
	public void setR224_crm_sub_app_col_elig_deb_sec(BigDecimal r224_crm_sub_app_col_elig_deb_sec) {
		this.r224_crm_sub_app_col_elig_deb_sec = r224_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR224_crm_sub_app_col_elig_euiq() {
		return r224_crm_sub_app_col_elig_euiq;
	}
	public void setR224_crm_sub_app_col_elig_euiq(BigDecimal r224_crm_sub_app_col_elig_euiq) {
		this.r224_crm_sub_app_col_elig_euiq = r224_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR224_crm_sub_app_col_elig_uni_tru() {
		return r224_crm_sub_app_col_elig_uni_tru;
	}
	public void setR224_crm_sub_app_col_elig_uni_tru(BigDecimal r224_crm_sub_app_col_elig_uni_tru) {
		this.r224_crm_sub_app_col_elig_uni_tru = r224_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR224_crm_sub_app_col_cea_cov() {
		return r224_crm_sub_app_col_cea_cov;
	}
	public void setR224_crm_sub_app_col_cea_cov(BigDecimal r224_crm_sub_app_col_cea_cov) {
		this.r224_crm_sub_app_col_cea_cov = r224_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR224_crm_sub_app_col_cea_not_cov() {
		return r224_crm_sub_app_col_cea_not_cov;
	}
	public void setR224_crm_sub_app_col_cea_not_cov(BigDecimal r224_crm_sub_app_col_cea_not_cov) {
		this.r224_crm_sub_app_col_cea_not_cov = r224_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR224_crm_sub_app_rwa_ris_wei_crm() {
		return r224_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR224_crm_sub_app_rwa_ris_wei_crm(BigDecimal r224_crm_sub_app_rwa_ris_wei_crm) {
		this.r224_crm_sub_app_rwa_ris_wei_crm = r224_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR224_crm_sub_app_rwa_ris_cea_cov() {
		return r224_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR224_crm_sub_app_rwa_ris_cea_cov(BigDecimal r224_crm_sub_app_rwa_ris_cea_cov) {
		this.r224_crm_sub_app_rwa_ris_cea_cov = r224_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR224_crm_sub_app_rwa_appl_org_coun() {
		return r224_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR224_crm_sub_app_rwa_appl_org_coun(BigDecimal r224_crm_sub_app_rwa_appl_org_coun) {
		this.r224_crm_sub_app_rwa_appl_org_coun = r224_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR224_crm_sub_app_rwa_ris_cea_not_cov() {
		return r224_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR224_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r224_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r224_crm_sub_app_rwa_ris_cea_not_cov = r224_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR224_crm_com_app_col_cea_elig_crm() {
		return r224_crm_com_app_col_cea_elig_crm;
	}
	public void setR224_crm_com_app_col_cea_elig_crm(BigDecimal r224_crm_com_app_col_cea_elig_crm) {
		this.r224_crm_com_app_col_cea_elig_crm = r224_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR224_crm_com_app_col_elig_cea_vol_adj() {
		return r224_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR224_crm_com_app_col_elig_cea_vol_adj(BigDecimal r224_crm_com_app_col_elig_cea_vol_adj) {
		this.r224_crm_com_app_col_elig_cea_vol_adj = r224_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR224_crm_com_app_col_elig_fin_hai() {
		return r224_crm_com_app_col_elig_fin_hai;
	}
	public void setR224_crm_com_app_col_elig_fin_hai(BigDecimal r224_crm_com_app_col_elig_fin_hai) {
		this.r224_crm_com_app_col_elig_fin_hai = r224_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR224_crm_com_app_col_cea_val_aft_crm() {
		return r224_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR224_crm_com_app_col_cea_val_aft_crm(BigDecimal r224_crm_com_app_col_cea_val_aft_crm) {
		this.r224_crm_com_app_col_cea_val_aft_crm = r224_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR224_rwa_elig_cea_not_cov() {
		return r224_rwa_elig_cea_not_cov;
	}
	public void setR224_rwa_elig_cea_not_cov(BigDecimal r224_rwa_elig_cea_not_cov) {
		this.r224_rwa_elig_cea_not_cov = r224_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR224_rwa_unsec_cea_sub_cre_ris() {
		return r224_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR224_rwa_unsec_cea_sub_cre_ris(BigDecimal r224_rwa_unsec_cea_sub_cre_ris) {
		this.r224_rwa_unsec_cea_sub_cre_ris = r224_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR224_rwa_unsec_cea() {
		return r224_rwa_unsec_cea;
	}
	public void setR224_rwa_unsec_cea(BigDecimal r224_rwa_unsec_cea) {
		this.r224_rwa_unsec_cea = r224_rwa_unsec_cea;
	}
	public BigDecimal getR224_rwa_tot_ris_wei_ass() {
		return r224_rwa_tot_ris_wei_ass;
	}
	public void setR224_rwa_tot_ris_wei_ass(BigDecimal r224_rwa_tot_ris_wei_ass) {
		this.r224_rwa_tot_ris_wei_ass = r224_rwa_tot_ris_wei_ass;
	}
	public String getR225_exposure_class_off_bal() {
		return r225_exposure_class_off_bal;
	}
	public void setR225_exposure_class_off_bal(String r225_exposure_class_off_bal) {
		this.r225_exposure_class_off_bal = r225_exposure_class_off_bal;
	}
	public BigDecimal getR225_nom_pri_amt() {
		return r225_nom_pri_amt;
	}
	public void setR225_nom_pri_amt(BigDecimal r225_nom_pri_amt) {
		this.r225_nom_pri_amt = r225_nom_pri_amt;
	}
	public BigDecimal getR225_ccf() {
		return r225_ccf;
	}
	public void setR225_ccf(BigDecimal r225_ccf) {
		this.r225_ccf = r225_ccf;
	}
	public BigDecimal getR225_cea() {
		return r225_cea;
	}
	public void setR225_cea(BigDecimal r225_cea) {
		this.r225_cea = r225_cea;
	}
	public BigDecimal getR225_cea_elig_coun_bilt_net() {
		return r225_cea_elig_coun_bilt_net;
	}
	public void setR225_cea_elig_coun_bilt_net(BigDecimal r225_cea_elig_coun_bilt_net) {
		this.r225_cea_elig_coun_bilt_net = r225_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR225_cea_aft_net() {
		return r225_cea_aft_net;
	}
	public void setR225_cea_aft_net(BigDecimal r225_cea_aft_net) {
		this.r225_cea_aft_net = r225_cea_aft_net;
	}
	public BigDecimal getR225_crm_sub_app_cea_elig() {
		return r225_crm_sub_app_cea_elig;
	}
	public void setR225_crm_sub_app_cea_elig(BigDecimal r225_crm_sub_app_cea_elig) {
		this.r225_crm_sub_app_cea_elig = r225_crm_sub_app_cea_elig;
	}
	public BigDecimal getR225_crm_sub_app_non_col_guar_elig() {
		return r225_crm_sub_app_non_col_guar_elig;
	}
	public void setR225_crm_sub_app_non_col_guar_elig(BigDecimal r225_crm_sub_app_non_col_guar_elig) {
		this.r225_crm_sub_app_non_col_guar_elig = r225_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR225_crm_sub_app_non_col_cre_der() {
		return r225_crm_sub_app_non_col_cre_der;
	}
	public void setR225_crm_sub_app_non_col_cre_der(BigDecimal r225_crm_sub_app_non_col_cre_der) {
		this.r225_crm_sub_app_non_col_cre_der = r225_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR225_crm_sub_app_col_elig_cash() {
		return r225_crm_sub_app_col_elig_cash;
	}
	public void setR225_crm_sub_app_col_elig_cash(BigDecimal r225_crm_sub_app_col_elig_cash) {
		this.r225_crm_sub_app_col_elig_cash = r225_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR225_crm_sub_app_col_elig_tre_bills() {
		return r225_crm_sub_app_col_elig_tre_bills;
	}
	public void setR225_crm_sub_app_col_elig_tre_bills(BigDecimal r225_crm_sub_app_col_elig_tre_bills) {
		this.r225_crm_sub_app_col_elig_tre_bills = r225_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR225_crm_sub_app_col_elig_deb_sec() {
		return r225_crm_sub_app_col_elig_deb_sec;
	}
	public void setR225_crm_sub_app_col_elig_deb_sec(BigDecimal r225_crm_sub_app_col_elig_deb_sec) {
		this.r225_crm_sub_app_col_elig_deb_sec = r225_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR225_crm_sub_app_col_elig_euiq() {
		return r225_crm_sub_app_col_elig_euiq;
	}
	public void setR225_crm_sub_app_col_elig_euiq(BigDecimal r225_crm_sub_app_col_elig_euiq) {
		this.r225_crm_sub_app_col_elig_euiq = r225_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR225_crm_sub_app_col_elig_uni_tru() {
		return r225_crm_sub_app_col_elig_uni_tru;
	}
	public void setR225_crm_sub_app_col_elig_uni_tru(BigDecimal r225_crm_sub_app_col_elig_uni_tru) {
		this.r225_crm_sub_app_col_elig_uni_tru = r225_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR225_crm_sub_app_col_cea_cov() {
		return r225_crm_sub_app_col_cea_cov;
	}
	public void setR225_crm_sub_app_col_cea_cov(BigDecimal r225_crm_sub_app_col_cea_cov) {
		this.r225_crm_sub_app_col_cea_cov = r225_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR225_crm_sub_app_col_cea_not_cov() {
		return r225_crm_sub_app_col_cea_not_cov;
	}
	public void setR225_crm_sub_app_col_cea_not_cov(BigDecimal r225_crm_sub_app_col_cea_not_cov) {
		this.r225_crm_sub_app_col_cea_not_cov = r225_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR225_crm_sub_app_rwa_ris_wei_crm() {
		return r225_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR225_crm_sub_app_rwa_ris_wei_crm(BigDecimal r225_crm_sub_app_rwa_ris_wei_crm) {
		this.r225_crm_sub_app_rwa_ris_wei_crm = r225_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR225_crm_sub_app_rwa_ris_cea_cov() {
		return r225_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR225_crm_sub_app_rwa_ris_cea_cov(BigDecimal r225_crm_sub_app_rwa_ris_cea_cov) {
		this.r225_crm_sub_app_rwa_ris_cea_cov = r225_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR225_crm_sub_app_rwa_appl_org_coun() {
		return r225_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR225_crm_sub_app_rwa_appl_org_coun(BigDecimal r225_crm_sub_app_rwa_appl_org_coun) {
		this.r225_crm_sub_app_rwa_appl_org_coun = r225_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR225_crm_sub_app_rwa_ris_cea_not_cov() {
		return r225_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR225_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r225_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r225_crm_sub_app_rwa_ris_cea_not_cov = r225_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR225_crm_com_app_col_cea_elig_crm() {
		return r225_crm_com_app_col_cea_elig_crm;
	}
	public void setR225_crm_com_app_col_cea_elig_crm(BigDecimal r225_crm_com_app_col_cea_elig_crm) {
		this.r225_crm_com_app_col_cea_elig_crm = r225_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR225_crm_com_app_col_elig_cea_vol_adj() {
		return r225_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR225_crm_com_app_col_elig_cea_vol_adj(BigDecimal r225_crm_com_app_col_elig_cea_vol_adj) {
		this.r225_crm_com_app_col_elig_cea_vol_adj = r225_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR225_crm_com_app_col_elig_fin_hai() {
		return r225_crm_com_app_col_elig_fin_hai;
	}
	public void setR225_crm_com_app_col_elig_fin_hai(BigDecimal r225_crm_com_app_col_elig_fin_hai) {
		this.r225_crm_com_app_col_elig_fin_hai = r225_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR225_crm_com_app_col_cea_val_aft_crm() {
		return r225_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR225_crm_com_app_col_cea_val_aft_crm(BigDecimal r225_crm_com_app_col_cea_val_aft_crm) {
		this.r225_crm_com_app_col_cea_val_aft_crm = r225_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR225_rwa_elig_cea_not_cov() {
		return r225_rwa_elig_cea_not_cov;
	}
	public void setR225_rwa_elig_cea_not_cov(BigDecimal r225_rwa_elig_cea_not_cov) {
		this.r225_rwa_elig_cea_not_cov = r225_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR225_rwa_unsec_cea_sub_cre_ris() {
		return r225_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR225_rwa_unsec_cea_sub_cre_ris(BigDecimal r225_rwa_unsec_cea_sub_cre_ris) {
		this.r225_rwa_unsec_cea_sub_cre_ris = r225_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR225_rwa_unsec_cea() {
		return r225_rwa_unsec_cea;
	}
	public void setR225_rwa_unsec_cea(BigDecimal r225_rwa_unsec_cea) {
		this.r225_rwa_unsec_cea = r225_rwa_unsec_cea;
	}
	public BigDecimal getR225_rwa_tot_ris_wei_ass() {
		return r225_rwa_tot_ris_wei_ass;
	}
	public void setR225_rwa_tot_ris_wei_ass(BigDecimal r225_rwa_tot_ris_wei_ass) {
		this.r225_rwa_tot_ris_wei_ass = r225_rwa_tot_ris_wei_ass;
	}
	public String getR226_exposure_class_off_bal() {
		return r226_exposure_class_off_bal;
	}
	public void setR226_exposure_class_off_bal(String r226_exposure_class_off_bal) {
		this.r226_exposure_class_off_bal = r226_exposure_class_off_bal;
	}
	public BigDecimal getR226_nom_pri_amt() {
		return r226_nom_pri_amt;
	}
	public void setR226_nom_pri_amt(BigDecimal r226_nom_pri_amt) {
		this.r226_nom_pri_amt = r226_nom_pri_amt;
	}
	public BigDecimal getR226_ccf() {
		return r226_ccf;
	}
	public void setR226_ccf(BigDecimal r226_ccf) {
		this.r226_ccf = r226_ccf;
	}
	public BigDecimal getR226_cea() {
		return r226_cea;
	}
	public void setR226_cea(BigDecimal r226_cea) {
		this.r226_cea = r226_cea;
	}
	public BigDecimal getR226_cea_elig_coun_bilt_net() {
		return r226_cea_elig_coun_bilt_net;
	}
	public void setR226_cea_elig_coun_bilt_net(BigDecimal r226_cea_elig_coun_bilt_net) {
		this.r226_cea_elig_coun_bilt_net = r226_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR226_cea_aft_net() {
		return r226_cea_aft_net;
	}
	public void setR226_cea_aft_net(BigDecimal r226_cea_aft_net) {
		this.r226_cea_aft_net = r226_cea_aft_net;
	}
	public BigDecimal getR226_crm_sub_app_cea_elig() {
		return r226_crm_sub_app_cea_elig;
	}
	public void setR226_crm_sub_app_cea_elig(BigDecimal r226_crm_sub_app_cea_elig) {
		this.r226_crm_sub_app_cea_elig = r226_crm_sub_app_cea_elig;
	}
	public BigDecimal getR226_crm_sub_app_non_col_guar_elig() {
		return r226_crm_sub_app_non_col_guar_elig;
	}
	public void setR226_crm_sub_app_non_col_guar_elig(BigDecimal r226_crm_sub_app_non_col_guar_elig) {
		this.r226_crm_sub_app_non_col_guar_elig = r226_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR226_crm_sub_app_non_col_cre_der() {
		return r226_crm_sub_app_non_col_cre_der;
	}
	public void setR226_crm_sub_app_non_col_cre_der(BigDecimal r226_crm_sub_app_non_col_cre_der) {
		this.r226_crm_sub_app_non_col_cre_der = r226_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR226_crm_sub_app_col_elig_cash() {
		return r226_crm_sub_app_col_elig_cash;
	}
	public void setR226_crm_sub_app_col_elig_cash(BigDecimal r226_crm_sub_app_col_elig_cash) {
		this.r226_crm_sub_app_col_elig_cash = r226_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR226_crm_sub_app_col_elig_tre_bills() {
		return r226_crm_sub_app_col_elig_tre_bills;
	}
	public void setR226_crm_sub_app_col_elig_tre_bills(BigDecimal r226_crm_sub_app_col_elig_tre_bills) {
		this.r226_crm_sub_app_col_elig_tre_bills = r226_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR226_crm_sub_app_col_elig_deb_sec() {
		return r226_crm_sub_app_col_elig_deb_sec;
	}
	public void setR226_crm_sub_app_col_elig_deb_sec(BigDecimal r226_crm_sub_app_col_elig_deb_sec) {
		this.r226_crm_sub_app_col_elig_deb_sec = r226_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR226_crm_sub_app_col_elig_euiq() {
		return r226_crm_sub_app_col_elig_euiq;
	}
	public void setR226_crm_sub_app_col_elig_euiq(BigDecimal r226_crm_sub_app_col_elig_euiq) {
		this.r226_crm_sub_app_col_elig_euiq = r226_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR226_crm_sub_app_col_elig_uni_tru() {
		return r226_crm_sub_app_col_elig_uni_tru;
	}
	public void setR226_crm_sub_app_col_elig_uni_tru(BigDecimal r226_crm_sub_app_col_elig_uni_tru) {
		this.r226_crm_sub_app_col_elig_uni_tru = r226_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR226_crm_sub_app_col_cea_cov() {
		return r226_crm_sub_app_col_cea_cov;
	}
	public void setR226_crm_sub_app_col_cea_cov(BigDecimal r226_crm_sub_app_col_cea_cov) {
		this.r226_crm_sub_app_col_cea_cov = r226_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR226_crm_sub_app_col_cea_not_cov() {
		return r226_crm_sub_app_col_cea_not_cov;
	}
	public void setR226_crm_sub_app_col_cea_not_cov(BigDecimal r226_crm_sub_app_col_cea_not_cov) {
		this.r226_crm_sub_app_col_cea_not_cov = r226_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR226_crm_sub_app_rwa_ris_wei_crm() {
		return r226_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR226_crm_sub_app_rwa_ris_wei_crm(BigDecimal r226_crm_sub_app_rwa_ris_wei_crm) {
		this.r226_crm_sub_app_rwa_ris_wei_crm = r226_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR226_crm_sub_app_rwa_ris_cea_cov() {
		return r226_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR226_crm_sub_app_rwa_ris_cea_cov(BigDecimal r226_crm_sub_app_rwa_ris_cea_cov) {
		this.r226_crm_sub_app_rwa_ris_cea_cov = r226_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR226_crm_sub_app_rwa_appl_org_coun() {
		return r226_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR226_crm_sub_app_rwa_appl_org_coun(BigDecimal r226_crm_sub_app_rwa_appl_org_coun) {
		this.r226_crm_sub_app_rwa_appl_org_coun = r226_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR226_crm_sub_app_rwa_ris_cea_not_cov() {
		return r226_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR226_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r226_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r226_crm_sub_app_rwa_ris_cea_not_cov = r226_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR226_crm_com_app_col_cea_elig_crm() {
		return r226_crm_com_app_col_cea_elig_crm;
	}
	public void setR226_crm_com_app_col_cea_elig_crm(BigDecimal r226_crm_com_app_col_cea_elig_crm) {
		this.r226_crm_com_app_col_cea_elig_crm = r226_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR226_crm_com_app_col_elig_cea_vol_adj() {
		return r226_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR226_crm_com_app_col_elig_cea_vol_adj(BigDecimal r226_crm_com_app_col_elig_cea_vol_adj) {
		this.r226_crm_com_app_col_elig_cea_vol_adj = r226_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR226_crm_com_app_col_elig_fin_hai() {
		return r226_crm_com_app_col_elig_fin_hai;
	}
	public void setR226_crm_com_app_col_elig_fin_hai(BigDecimal r226_crm_com_app_col_elig_fin_hai) {
		this.r226_crm_com_app_col_elig_fin_hai = r226_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR226_crm_com_app_col_cea_val_aft_crm() {
		return r226_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR226_crm_com_app_col_cea_val_aft_crm(BigDecimal r226_crm_com_app_col_cea_val_aft_crm) {
		this.r226_crm_com_app_col_cea_val_aft_crm = r226_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR226_rwa_elig_cea_not_cov() {
		return r226_rwa_elig_cea_not_cov;
	}
	public void setR226_rwa_elig_cea_not_cov(BigDecimal r226_rwa_elig_cea_not_cov) {
		this.r226_rwa_elig_cea_not_cov = r226_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR226_rwa_unsec_cea_sub_cre_ris() {
		return r226_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR226_rwa_unsec_cea_sub_cre_ris(BigDecimal r226_rwa_unsec_cea_sub_cre_ris) {
		this.r226_rwa_unsec_cea_sub_cre_ris = r226_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR226_rwa_unsec_cea() {
		return r226_rwa_unsec_cea;
	}
	public void setR226_rwa_unsec_cea(BigDecimal r226_rwa_unsec_cea) {
		this.r226_rwa_unsec_cea = r226_rwa_unsec_cea;
	}
	public BigDecimal getR226_rwa_tot_ris_wei_ass() {
		return r226_rwa_tot_ris_wei_ass;
	}
	public void setR226_rwa_tot_ris_wei_ass(BigDecimal r226_rwa_tot_ris_wei_ass) {
		this.r226_rwa_tot_ris_wei_ass = r226_rwa_tot_ris_wei_ass;
	}
	public String getR227_exposure_class_off_bal() {
		return r227_exposure_class_off_bal;
	}
	public void setR227_exposure_class_off_bal(String r227_exposure_class_off_bal) {
		this.r227_exposure_class_off_bal = r227_exposure_class_off_bal;
	}
	public BigDecimal getR227_nom_pri_amt() {
		return r227_nom_pri_amt;
	}
	public void setR227_nom_pri_amt(BigDecimal r227_nom_pri_amt) {
		this.r227_nom_pri_amt = r227_nom_pri_amt;
	}
	public BigDecimal getR227_ccf() {
		return r227_ccf;
	}
	public void setR227_ccf(BigDecimal r227_ccf) {
		this.r227_ccf = r227_ccf;
	}
	public BigDecimal getR227_cea() {
		return r227_cea;
	}
	public void setR227_cea(BigDecimal r227_cea) {
		this.r227_cea = r227_cea;
	}
	public BigDecimal getR227_cea_elig_coun_bilt_net() {
		return r227_cea_elig_coun_bilt_net;
	}
	public void setR227_cea_elig_coun_bilt_net(BigDecimal r227_cea_elig_coun_bilt_net) {
		this.r227_cea_elig_coun_bilt_net = r227_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR227_cea_aft_net() {
		return r227_cea_aft_net;
	}
	public void setR227_cea_aft_net(BigDecimal r227_cea_aft_net) {
		this.r227_cea_aft_net = r227_cea_aft_net;
	}
	public BigDecimal getR227_crm_sub_app_cea_elig() {
		return r227_crm_sub_app_cea_elig;
	}
	public void setR227_crm_sub_app_cea_elig(BigDecimal r227_crm_sub_app_cea_elig) {
		this.r227_crm_sub_app_cea_elig = r227_crm_sub_app_cea_elig;
	}
	public BigDecimal getR227_crm_sub_app_non_col_guar_elig() {
		return r227_crm_sub_app_non_col_guar_elig;
	}
	public void setR227_crm_sub_app_non_col_guar_elig(BigDecimal r227_crm_sub_app_non_col_guar_elig) {
		this.r227_crm_sub_app_non_col_guar_elig = r227_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR227_crm_sub_app_non_col_cre_der() {
		return r227_crm_sub_app_non_col_cre_der;
	}
	public void setR227_crm_sub_app_non_col_cre_der(BigDecimal r227_crm_sub_app_non_col_cre_der) {
		this.r227_crm_sub_app_non_col_cre_der = r227_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR227_crm_sub_app_col_elig_cash() {
		return r227_crm_sub_app_col_elig_cash;
	}
	public void setR227_crm_sub_app_col_elig_cash(BigDecimal r227_crm_sub_app_col_elig_cash) {
		this.r227_crm_sub_app_col_elig_cash = r227_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR227_crm_sub_app_col_elig_tre_bills() {
		return r227_crm_sub_app_col_elig_tre_bills;
	}
	public void setR227_crm_sub_app_col_elig_tre_bills(BigDecimal r227_crm_sub_app_col_elig_tre_bills) {
		this.r227_crm_sub_app_col_elig_tre_bills = r227_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR227_crm_sub_app_col_elig_deb_sec() {
		return r227_crm_sub_app_col_elig_deb_sec;
	}
	public void setR227_crm_sub_app_col_elig_deb_sec(BigDecimal r227_crm_sub_app_col_elig_deb_sec) {
		this.r227_crm_sub_app_col_elig_deb_sec = r227_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR227_crm_sub_app_col_elig_euiq() {
		return r227_crm_sub_app_col_elig_euiq;
	}
	public void setR227_crm_sub_app_col_elig_euiq(BigDecimal r227_crm_sub_app_col_elig_euiq) {
		this.r227_crm_sub_app_col_elig_euiq = r227_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR227_crm_sub_app_col_elig_uni_tru() {
		return r227_crm_sub_app_col_elig_uni_tru;
	}
	public void setR227_crm_sub_app_col_elig_uni_tru(BigDecimal r227_crm_sub_app_col_elig_uni_tru) {
		this.r227_crm_sub_app_col_elig_uni_tru = r227_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR227_crm_sub_app_col_cea_cov() {
		return r227_crm_sub_app_col_cea_cov;
	}
	public void setR227_crm_sub_app_col_cea_cov(BigDecimal r227_crm_sub_app_col_cea_cov) {
		this.r227_crm_sub_app_col_cea_cov = r227_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR227_crm_sub_app_col_cea_not_cov() {
		return r227_crm_sub_app_col_cea_not_cov;
	}
	public void setR227_crm_sub_app_col_cea_not_cov(BigDecimal r227_crm_sub_app_col_cea_not_cov) {
		this.r227_crm_sub_app_col_cea_not_cov = r227_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR227_crm_sub_app_rwa_ris_wei_crm() {
		return r227_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR227_crm_sub_app_rwa_ris_wei_crm(BigDecimal r227_crm_sub_app_rwa_ris_wei_crm) {
		this.r227_crm_sub_app_rwa_ris_wei_crm = r227_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR227_crm_sub_app_rwa_ris_cea_cov() {
		return r227_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR227_crm_sub_app_rwa_ris_cea_cov(BigDecimal r227_crm_sub_app_rwa_ris_cea_cov) {
		this.r227_crm_sub_app_rwa_ris_cea_cov = r227_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR227_crm_sub_app_rwa_appl_org_coun() {
		return r227_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR227_crm_sub_app_rwa_appl_org_coun(BigDecimal r227_crm_sub_app_rwa_appl_org_coun) {
		this.r227_crm_sub_app_rwa_appl_org_coun = r227_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR227_crm_sub_app_rwa_ris_cea_not_cov() {
		return r227_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR227_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r227_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r227_crm_sub_app_rwa_ris_cea_not_cov = r227_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR227_crm_com_app_col_cea_elig_crm() {
		return r227_crm_com_app_col_cea_elig_crm;
	}
	public void setR227_crm_com_app_col_cea_elig_crm(BigDecimal r227_crm_com_app_col_cea_elig_crm) {
		this.r227_crm_com_app_col_cea_elig_crm = r227_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR227_crm_com_app_col_elig_cea_vol_adj() {
		return r227_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR227_crm_com_app_col_elig_cea_vol_adj(BigDecimal r227_crm_com_app_col_elig_cea_vol_adj) {
		this.r227_crm_com_app_col_elig_cea_vol_adj = r227_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR227_crm_com_app_col_elig_fin_hai() {
		return r227_crm_com_app_col_elig_fin_hai;
	}
	public void setR227_crm_com_app_col_elig_fin_hai(BigDecimal r227_crm_com_app_col_elig_fin_hai) {
		this.r227_crm_com_app_col_elig_fin_hai = r227_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR227_crm_com_app_col_cea_val_aft_crm() {
		return r227_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR227_crm_com_app_col_cea_val_aft_crm(BigDecimal r227_crm_com_app_col_cea_val_aft_crm) {
		this.r227_crm_com_app_col_cea_val_aft_crm = r227_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR227_rwa_elig_cea_not_cov() {
		return r227_rwa_elig_cea_not_cov;
	}
	public void setR227_rwa_elig_cea_not_cov(BigDecimal r227_rwa_elig_cea_not_cov) {
		this.r227_rwa_elig_cea_not_cov = r227_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR227_rwa_unsec_cea_sub_cre_ris() {
		return r227_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR227_rwa_unsec_cea_sub_cre_ris(BigDecimal r227_rwa_unsec_cea_sub_cre_ris) {
		this.r227_rwa_unsec_cea_sub_cre_ris = r227_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR227_rwa_unsec_cea() {
		return r227_rwa_unsec_cea;
	}
	public void setR227_rwa_unsec_cea(BigDecimal r227_rwa_unsec_cea) {
		this.r227_rwa_unsec_cea = r227_rwa_unsec_cea;
	}
	public BigDecimal getR227_rwa_tot_ris_wei_ass() {
		return r227_rwa_tot_ris_wei_ass;
	}
	public void setR227_rwa_tot_ris_wei_ass(BigDecimal r227_rwa_tot_ris_wei_ass) {
		this.r227_rwa_tot_ris_wei_ass = r227_rwa_tot_ris_wei_ass;
	}
	public String getR228_exposure_class_off_bal() {
		return r228_exposure_class_off_bal;
	}
	public void setR228_exposure_class_off_bal(String r228_exposure_class_off_bal) {
		this.r228_exposure_class_off_bal = r228_exposure_class_off_bal;
	}
	public BigDecimal getR228_nom_pri_amt() {
		return r228_nom_pri_amt;
	}
	public void setR228_nom_pri_amt(BigDecimal r228_nom_pri_amt) {
		this.r228_nom_pri_amt = r228_nom_pri_amt;
	}
	public BigDecimal getR228_ccf() {
		return r228_ccf;
	}
	public void setR228_ccf(BigDecimal r228_ccf) {
		this.r228_ccf = r228_ccf;
	}
	public BigDecimal getR228_cea() {
		return r228_cea;
	}
	public void setR228_cea(BigDecimal r228_cea) {
		this.r228_cea = r228_cea;
	}
	public BigDecimal getR228_cea_elig_coun_bilt_net() {
		return r228_cea_elig_coun_bilt_net;
	}
	public void setR228_cea_elig_coun_bilt_net(BigDecimal r228_cea_elig_coun_bilt_net) {
		this.r228_cea_elig_coun_bilt_net = r228_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR228_cea_aft_net() {
		return r228_cea_aft_net;
	}
	public void setR228_cea_aft_net(BigDecimal r228_cea_aft_net) {
		this.r228_cea_aft_net = r228_cea_aft_net;
	}
	public BigDecimal getR228_crm_sub_app_cea_elig() {
		return r228_crm_sub_app_cea_elig;
	}
	public void setR228_crm_sub_app_cea_elig(BigDecimal r228_crm_sub_app_cea_elig) {
		this.r228_crm_sub_app_cea_elig = r228_crm_sub_app_cea_elig;
	}
	public BigDecimal getR228_crm_sub_app_non_col_guar_elig() {
		return r228_crm_sub_app_non_col_guar_elig;
	}
	public void setR228_crm_sub_app_non_col_guar_elig(BigDecimal r228_crm_sub_app_non_col_guar_elig) {
		this.r228_crm_sub_app_non_col_guar_elig = r228_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR228_crm_sub_app_non_col_cre_der() {
		return r228_crm_sub_app_non_col_cre_der;
	}
	public void setR228_crm_sub_app_non_col_cre_der(BigDecimal r228_crm_sub_app_non_col_cre_der) {
		this.r228_crm_sub_app_non_col_cre_der = r228_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR228_crm_sub_app_col_elig_cash() {
		return r228_crm_sub_app_col_elig_cash;
	}
	public void setR228_crm_sub_app_col_elig_cash(BigDecimal r228_crm_sub_app_col_elig_cash) {
		this.r228_crm_sub_app_col_elig_cash = r228_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR228_crm_sub_app_col_elig_tre_bills() {
		return r228_crm_sub_app_col_elig_tre_bills;
	}
	public void setR228_crm_sub_app_col_elig_tre_bills(BigDecimal r228_crm_sub_app_col_elig_tre_bills) {
		this.r228_crm_sub_app_col_elig_tre_bills = r228_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR228_crm_sub_app_col_elig_deb_sec() {
		return r228_crm_sub_app_col_elig_deb_sec;
	}
	public void setR228_crm_sub_app_col_elig_deb_sec(BigDecimal r228_crm_sub_app_col_elig_deb_sec) {
		this.r228_crm_sub_app_col_elig_deb_sec = r228_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR228_crm_sub_app_col_elig_euiq() {
		return r228_crm_sub_app_col_elig_euiq;
	}
	public void setR228_crm_sub_app_col_elig_euiq(BigDecimal r228_crm_sub_app_col_elig_euiq) {
		this.r228_crm_sub_app_col_elig_euiq = r228_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR228_crm_sub_app_col_elig_uni_tru() {
		return r228_crm_sub_app_col_elig_uni_tru;
	}
	public void setR228_crm_sub_app_col_elig_uni_tru(BigDecimal r228_crm_sub_app_col_elig_uni_tru) {
		this.r228_crm_sub_app_col_elig_uni_tru = r228_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR228_crm_sub_app_col_cea_cov() {
		return r228_crm_sub_app_col_cea_cov;
	}
	public void setR228_crm_sub_app_col_cea_cov(BigDecimal r228_crm_sub_app_col_cea_cov) {
		this.r228_crm_sub_app_col_cea_cov = r228_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR228_crm_sub_app_col_cea_not_cov() {
		return r228_crm_sub_app_col_cea_not_cov;
	}
	public void setR228_crm_sub_app_col_cea_not_cov(BigDecimal r228_crm_sub_app_col_cea_not_cov) {
		this.r228_crm_sub_app_col_cea_not_cov = r228_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR228_crm_sub_app_rwa_ris_wei_crm() {
		return r228_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR228_crm_sub_app_rwa_ris_wei_crm(BigDecimal r228_crm_sub_app_rwa_ris_wei_crm) {
		this.r228_crm_sub_app_rwa_ris_wei_crm = r228_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR228_crm_sub_app_rwa_ris_cea_cov() {
		return r228_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR228_crm_sub_app_rwa_ris_cea_cov(BigDecimal r228_crm_sub_app_rwa_ris_cea_cov) {
		this.r228_crm_sub_app_rwa_ris_cea_cov = r228_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR228_crm_sub_app_rwa_appl_org_coun() {
		return r228_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR228_crm_sub_app_rwa_appl_org_coun(BigDecimal r228_crm_sub_app_rwa_appl_org_coun) {
		this.r228_crm_sub_app_rwa_appl_org_coun = r228_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR228_crm_sub_app_rwa_ris_cea_not_cov() {
		return r228_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR228_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r228_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r228_crm_sub_app_rwa_ris_cea_not_cov = r228_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR228_crm_com_app_col_cea_elig_crm() {
		return r228_crm_com_app_col_cea_elig_crm;
	}
	public void setR228_crm_com_app_col_cea_elig_crm(BigDecimal r228_crm_com_app_col_cea_elig_crm) {
		this.r228_crm_com_app_col_cea_elig_crm = r228_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR228_crm_com_app_col_elig_cea_vol_adj() {
		return r228_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR228_crm_com_app_col_elig_cea_vol_adj(BigDecimal r228_crm_com_app_col_elig_cea_vol_adj) {
		this.r228_crm_com_app_col_elig_cea_vol_adj = r228_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR228_crm_com_app_col_elig_fin_hai() {
		return r228_crm_com_app_col_elig_fin_hai;
	}
	public void setR228_crm_com_app_col_elig_fin_hai(BigDecimal r228_crm_com_app_col_elig_fin_hai) {
		this.r228_crm_com_app_col_elig_fin_hai = r228_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR228_crm_com_app_col_cea_val_aft_crm() {
		return r228_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR228_crm_com_app_col_cea_val_aft_crm(BigDecimal r228_crm_com_app_col_cea_val_aft_crm) {
		this.r228_crm_com_app_col_cea_val_aft_crm = r228_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR228_rwa_elig_cea_not_cov() {
		return r228_rwa_elig_cea_not_cov;
	}
	public void setR228_rwa_elig_cea_not_cov(BigDecimal r228_rwa_elig_cea_not_cov) {
		this.r228_rwa_elig_cea_not_cov = r228_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR228_rwa_unsec_cea_sub_cre_ris() {
		return r228_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR228_rwa_unsec_cea_sub_cre_ris(BigDecimal r228_rwa_unsec_cea_sub_cre_ris) {
		this.r228_rwa_unsec_cea_sub_cre_ris = r228_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR228_rwa_unsec_cea() {
		return r228_rwa_unsec_cea;
	}
	public void setR228_rwa_unsec_cea(BigDecimal r228_rwa_unsec_cea) {
		this.r228_rwa_unsec_cea = r228_rwa_unsec_cea;
	}
	public BigDecimal getR228_rwa_tot_ris_wei_ass() {
		return r228_rwa_tot_ris_wei_ass;
	}
	public void setR228_rwa_tot_ris_wei_ass(BigDecimal r228_rwa_tot_ris_wei_ass) {
		this.r228_rwa_tot_ris_wei_ass = r228_rwa_tot_ris_wei_ass;
	}
	public String getR229_exposure_class_off_bal() {
		return r229_exposure_class_off_bal;
	}
	public void setR229_exposure_class_off_bal(String r229_exposure_class_off_bal) {
		this.r229_exposure_class_off_bal = r229_exposure_class_off_bal;
	}
	public BigDecimal getR229_nom_pri_amt() {
		return r229_nom_pri_amt;
	}
	public void setR229_nom_pri_amt(BigDecimal r229_nom_pri_amt) {
		this.r229_nom_pri_amt = r229_nom_pri_amt;
	}
	public BigDecimal getR229_ccf() {
		return r229_ccf;
	}
	public void setR229_ccf(BigDecimal r229_ccf) {
		this.r229_ccf = r229_ccf;
	}
	public BigDecimal getR229_cea() {
		return r229_cea;
	}
	public void setR229_cea(BigDecimal r229_cea) {
		this.r229_cea = r229_cea;
	}
	public BigDecimal getR229_cea_elig_coun_bilt_net() {
		return r229_cea_elig_coun_bilt_net;
	}
	public void setR229_cea_elig_coun_bilt_net(BigDecimal r229_cea_elig_coun_bilt_net) {
		this.r229_cea_elig_coun_bilt_net = r229_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR229_cea_aft_net() {
		return r229_cea_aft_net;
	}
	public void setR229_cea_aft_net(BigDecimal r229_cea_aft_net) {
		this.r229_cea_aft_net = r229_cea_aft_net;
	}
	public BigDecimal getR229_crm_sub_app_cea_elig() {
		return r229_crm_sub_app_cea_elig;
	}
	public void setR229_crm_sub_app_cea_elig(BigDecimal r229_crm_sub_app_cea_elig) {
		this.r229_crm_sub_app_cea_elig = r229_crm_sub_app_cea_elig;
	}
	public BigDecimal getR229_crm_sub_app_non_col_guar_elig() {
		return r229_crm_sub_app_non_col_guar_elig;
	}
	public void setR229_crm_sub_app_non_col_guar_elig(BigDecimal r229_crm_sub_app_non_col_guar_elig) {
		this.r229_crm_sub_app_non_col_guar_elig = r229_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR229_crm_sub_app_non_col_cre_der() {
		return r229_crm_sub_app_non_col_cre_der;
	}
	public void setR229_crm_sub_app_non_col_cre_der(BigDecimal r229_crm_sub_app_non_col_cre_der) {
		this.r229_crm_sub_app_non_col_cre_der = r229_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR229_crm_sub_app_col_elig_cash() {
		return r229_crm_sub_app_col_elig_cash;
	}
	public void setR229_crm_sub_app_col_elig_cash(BigDecimal r229_crm_sub_app_col_elig_cash) {
		this.r229_crm_sub_app_col_elig_cash = r229_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR229_crm_sub_app_col_elig_tre_bills() {
		return r229_crm_sub_app_col_elig_tre_bills;
	}
	public void setR229_crm_sub_app_col_elig_tre_bills(BigDecimal r229_crm_sub_app_col_elig_tre_bills) {
		this.r229_crm_sub_app_col_elig_tre_bills = r229_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR229_crm_sub_app_col_elig_deb_sec() {
		return r229_crm_sub_app_col_elig_deb_sec;
	}
	public void setR229_crm_sub_app_col_elig_deb_sec(BigDecimal r229_crm_sub_app_col_elig_deb_sec) {
		this.r229_crm_sub_app_col_elig_deb_sec = r229_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR229_crm_sub_app_col_elig_euiq() {
		return r229_crm_sub_app_col_elig_euiq;
	}
	public void setR229_crm_sub_app_col_elig_euiq(BigDecimal r229_crm_sub_app_col_elig_euiq) {
		this.r229_crm_sub_app_col_elig_euiq = r229_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR229_crm_sub_app_col_elig_uni_tru() {
		return r229_crm_sub_app_col_elig_uni_tru;
	}
	public void setR229_crm_sub_app_col_elig_uni_tru(BigDecimal r229_crm_sub_app_col_elig_uni_tru) {
		this.r229_crm_sub_app_col_elig_uni_tru = r229_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR229_crm_sub_app_col_cea_cov() {
		return r229_crm_sub_app_col_cea_cov;
	}
	public void setR229_crm_sub_app_col_cea_cov(BigDecimal r229_crm_sub_app_col_cea_cov) {
		this.r229_crm_sub_app_col_cea_cov = r229_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR229_crm_sub_app_col_cea_not_cov() {
		return r229_crm_sub_app_col_cea_not_cov;
	}
	public void setR229_crm_sub_app_col_cea_not_cov(BigDecimal r229_crm_sub_app_col_cea_not_cov) {
		this.r229_crm_sub_app_col_cea_not_cov = r229_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR229_crm_sub_app_rwa_ris_wei_crm() {
		return r229_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR229_crm_sub_app_rwa_ris_wei_crm(BigDecimal r229_crm_sub_app_rwa_ris_wei_crm) {
		this.r229_crm_sub_app_rwa_ris_wei_crm = r229_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR229_crm_sub_app_rwa_ris_cea_cov() {
		return r229_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR229_crm_sub_app_rwa_ris_cea_cov(BigDecimal r229_crm_sub_app_rwa_ris_cea_cov) {
		this.r229_crm_sub_app_rwa_ris_cea_cov = r229_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR229_crm_sub_app_rwa_appl_org_coun() {
		return r229_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR229_crm_sub_app_rwa_appl_org_coun(BigDecimal r229_crm_sub_app_rwa_appl_org_coun) {
		this.r229_crm_sub_app_rwa_appl_org_coun = r229_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR229_crm_sub_app_rwa_ris_cea_not_cov() {
		return r229_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR229_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r229_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r229_crm_sub_app_rwa_ris_cea_not_cov = r229_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR229_crm_com_app_col_cea_elig_crm() {
		return r229_crm_com_app_col_cea_elig_crm;
	}
	public void setR229_crm_com_app_col_cea_elig_crm(BigDecimal r229_crm_com_app_col_cea_elig_crm) {
		this.r229_crm_com_app_col_cea_elig_crm = r229_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR229_crm_com_app_col_elig_cea_vol_adj() {
		return r229_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR229_crm_com_app_col_elig_cea_vol_adj(BigDecimal r229_crm_com_app_col_elig_cea_vol_adj) {
		this.r229_crm_com_app_col_elig_cea_vol_adj = r229_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR229_crm_com_app_col_elig_fin_hai() {
		return r229_crm_com_app_col_elig_fin_hai;
	}
	public void setR229_crm_com_app_col_elig_fin_hai(BigDecimal r229_crm_com_app_col_elig_fin_hai) {
		this.r229_crm_com_app_col_elig_fin_hai = r229_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR229_crm_com_app_col_cea_val_aft_crm() {
		return r229_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR229_crm_com_app_col_cea_val_aft_crm(BigDecimal r229_crm_com_app_col_cea_val_aft_crm) {
		this.r229_crm_com_app_col_cea_val_aft_crm = r229_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR229_rwa_elig_cea_not_cov() {
		return r229_rwa_elig_cea_not_cov;
	}
	public void setR229_rwa_elig_cea_not_cov(BigDecimal r229_rwa_elig_cea_not_cov) {
		this.r229_rwa_elig_cea_not_cov = r229_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR229_rwa_unsec_cea_sub_cre_ris() {
		return r229_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR229_rwa_unsec_cea_sub_cre_ris(BigDecimal r229_rwa_unsec_cea_sub_cre_ris) {
		this.r229_rwa_unsec_cea_sub_cre_ris = r229_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR229_rwa_unsec_cea() {
		return r229_rwa_unsec_cea;
	}
	public void setR229_rwa_unsec_cea(BigDecimal r229_rwa_unsec_cea) {
		this.r229_rwa_unsec_cea = r229_rwa_unsec_cea;
	}
	public BigDecimal getR229_rwa_tot_ris_wei_ass() {
		return r229_rwa_tot_ris_wei_ass;
	}
	public void setR229_rwa_tot_ris_wei_ass(BigDecimal r229_rwa_tot_ris_wei_ass) {
		this.r229_rwa_tot_ris_wei_ass = r229_rwa_tot_ris_wei_ass;
	}
	public String getR230_exposure_class_off_bal() {
		return r230_exposure_class_off_bal;
	}
	public void setR230_exposure_class_off_bal(String r230_exposure_class_off_bal) {
		this.r230_exposure_class_off_bal = r230_exposure_class_off_bal;
	}
	public BigDecimal getR230_nom_pri_amt() {
		return r230_nom_pri_amt;
	}
	public void setR230_nom_pri_amt(BigDecimal r230_nom_pri_amt) {
		this.r230_nom_pri_amt = r230_nom_pri_amt;
	}
	public BigDecimal getR230_ccf() {
		return r230_ccf;
	}
	public void setR230_ccf(BigDecimal r230_ccf) {
		this.r230_ccf = r230_ccf;
	}
	public BigDecimal getR230_cea() {
		return r230_cea;
	}
	public void setR230_cea(BigDecimal r230_cea) {
		this.r230_cea = r230_cea;
	}
	public BigDecimal getR230_cea_elig_coun_bilt_net() {
		return r230_cea_elig_coun_bilt_net;
	}
	public void setR230_cea_elig_coun_bilt_net(BigDecimal r230_cea_elig_coun_bilt_net) {
		this.r230_cea_elig_coun_bilt_net = r230_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR230_cea_aft_net() {
		return r230_cea_aft_net;
	}
	public void setR230_cea_aft_net(BigDecimal r230_cea_aft_net) {
		this.r230_cea_aft_net = r230_cea_aft_net;
	}
	public BigDecimal getR230_crm_sub_app_cea_elig() {
		return r230_crm_sub_app_cea_elig;
	}
	public void setR230_crm_sub_app_cea_elig(BigDecimal r230_crm_sub_app_cea_elig) {
		this.r230_crm_sub_app_cea_elig = r230_crm_sub_app_cea_elig;
	}
	public BigDecimal getR230_crm_sub_app_non_col_guar_elig() {
		return r230_crm_sub_app_non_col_guar_elig;
	}
	public void setR230_crm_sub_app_non_col_guar_elig(BigDecimal r230_crm_sub_app_non_col_guar_elig) {
		this.r230_crm_sub_app_non_col_guar_elig = r230_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR230_crm_sub_app_non_col_cre_der() {
		return r230_crm_sub_app_non_col_cre_der;
	}
	public void setR230_crm_sub_app_non_col_cre_der(BigDecimal r230_crm_sub_app_non_col_cre_der) {
		this.r230_crm_sub_app_non_col_cre_der = r230_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR230_crm_sub_app_col_elig_cash() {
		return r230_crm_sub_app_col_elig_cash;
	}
	public void setR230_crm_sub_app_col_elig_cash(BigDecimal r230_crm_sub_app_col_elig_cash) {
		this.r230_crm_sub_app_col_elig_cash = r230_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR230_crm_sub_app_col_elig_tre_bills() {
		return r230_crm_sub_app_col_elig_tre_bills;
	}
	public void setR230_crm_sub_app_col_elig_tre_bills(BigDecimal r230_crm_sub_app_col_elig_tre_bills) {
		this.r230_crm_sub_app_col_elig_tre_bills = r230_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR230_crm_sub_app_col_elig_deb_sec() {
		return r230_crm_sub_app_col_elig_deb_sec;
	}
	public void setR230_crm_sub_app_col_elig_deb_sec(BigDecimal r230_crm_sub_app_col_elig_deb_sec) {
		this.r230_crm_sub_app_col_elig_deb_sec = r230_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR230_crm_sub_app_col_elig_euiq() {
		return r230_crm_sub_app_col_elig_euiq;
	}
	public void setR230_crm_sub_app_col_elig_euiq(BigDecimal r230_crm_sub_app_col_elig_euiq) {
		this.r230_crm_sub_app_col_elig_euiq = r230_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR230_crm_sub_app_col_elig_uni_tru() {
		return r230_crm_sub_app_col_elig_uni_tru;
	}
	public void setR230_crm_sub_app_col_elig_uni_tru(BigDecimal r230_crm_sub_app_col_elig_uni_tru) {
		this.r230_crm_sub_app_col_elig_uni_tru = r230_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR230_crm_sub_app_col_cea_cov() {
		return r230_crm_sub_app_col_cea_cov;
	}
	public void setR230_crm_sub_app_col_cea_cov(BigDecimal r230_crm_sub_app_col_cea_cov) {
		this.r230_crm_sub_app_col_cea_cov = r230_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR230_crm_sub_app_col_cea_not_cov() {
		return r230_crm_sub_app_col_cea_not_cov;
	}
	public void setR230_crm_sub_app_col_cea_not_cov(BigDecimal r230_crm_sub_app_col_cea_not_cov) {
		this.r230_crm_sub_app_col_cea_not_cov = r230_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR230_crm_sub_app_rwa_ris_wei_crm() {
		return r230_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR230_crm_sub_app_rwa_ris_wei_crm(BigDecimal r230_crm_sub_app_rwa_ris_wei_crm) {
		this.r230_crm_sub_app_rwa_ris_wei_crm = r230_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR230_crm_sub_app_rwa_ris_cea_cov() {
		return r230_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR230_crm_sub_app_rwa_ris_cea_cov(BigDecimal r230_crm_sub_app_rwa_ris_cea_cov) {
		this.r230_crm_sub_app_rwa_ris_cea_cov = r230_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR230_crm_sub_app_rwa_appl_org_coun() {
		return r230_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR230_crm_sub_app_rwa_appl_org_coun(BigDecimal r230_crm_sub_app_rwa_appl_org_coun) {
		this.r230_crm_sub_app_rwa_appl_org_coun = r230_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR230_crm_sub_app_rwa_ris_cea_not_cov() {
		return r230_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR230_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r230_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r230_crm_sub_app_rwa_ris_cea_not_cov = r230_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR230_crm_com_app_col_cea_elig_crm() {
		return r230_crm_com_app_col_cea_elig_crm;
	}
	public void setR230_crm_com_app_col_cea_elig_crm(BigDecimal r230_crm_com_app_col_cea_elig_crm) {
		this.r230_crm_com_app_col_cea_elig_crm = r230_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR230_crm_com_app_col_elig_cea_vol_adj() {
		return r230_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR230_crm_com_app_col_elig_cea_vol_adj(BigDecimal r230_crm_com_app_col_elig_cea_vol_adj) {
		this.r230_crm_com_app_col_elig_cea_vol_adj = r230_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR230_crm_com_app_col_elig_fin_hai() {
		return r230_crm_com_app_col_elig_fin_hai;
	}
	public void setR230_crm_com_app_col_elig_fin_hai(BigDecimal r230_crm_com_app_col_elig_fin_hai) {
		this.r230_crm_com_app_col_elig_fin_hai = r230_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR230_crm_com_app_col_cea_val_aft_crm() {
		return r230_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR230_crm_com_app_col_cea_val_aft_crm(BigDecimal r230_crm_com_app_col_cea_val_aft_crm) {
		this.r230_crm_com_app_col_cea_val_aft_crm = r230_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR230_rwa_elig_cea_not_cov() {
		return r230_rwa_elig_cea_not_cov;
	}
	public void setR230_rwa_elig_cea_not_cov(BigDecimal r230_rwa_elig_cea_not_cov) {
		this.r230_rwa_elig_cea_not_cov = r230_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR230_rwa_unsec_cea_sub_cre_ris() {
		return r230_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR230_rwa_unsec_cea_sub_cre_ris(BigDecimal r230_rwa_unsec_cea_sub_cre_ris) {
		this.r230_rwa_unsec_cea_sub_cre_ris = r230_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR230_rwa_unsec_cea() {
		return r230_rwa_unsec_cea;
	}
	public void setR230_rwa_unsec_cea(BigDecimal r230_rwa_unsec_cea) {
		this.r230_rwa_unsec_cea = r230_rwa_unsec_cea;
	}
	public BigDecimal getR230_rwa_tot_ris_wei_ass() {
		return r230_rwa_tot_ris_wei_ass;
	}
	public void setR230_rwa_tot_ris_wei_ass(BigDecimal r230_rwa_tot_ris_wei_ass) {
		this.r230_rwa_tot_ris_wei_ass = r230_rwa_tot_ris_wei_ass;
	}
	public String getR231_exposure_class_off_bal() {
		return r231_exposure_class_off_bal;
	}
	public void setR231_exposure_class_off_bal(String r231_exposure_class_off_bal) {
		this.r231_exposure_class_off_bal = r231_exposure_class_off_bal;
	}
	public BigDecimal getR231_nom_pri_amt() {
		return r231_nom_pri_amt;
	}
	public void setR231_nom_pri_amt(BigDecimal r231_nom_pri_amt) {
		this.r231_nom_pri_amt = r231_nom_pri_amt;
	}
	public BigDecimal getR231_ccf() {
		return r231_ccf;
	}
	public void setR231_ccf(BigDecimal r231_ccf) {
		this.r231_ccf = r231_ccf;
	}
	public BigDecimal getR231_cea() {
		return r231_cea;
	}
	public void setR231_cea(BigDecimal r231_cea) {
		this.r231_cea = r231_cea;
	}
	public BigDecimal getR231_cea_elig_coun_bilt_net() {
		return r231_cea_elig_coun_bilt_net;
	}
	public void setR231_cea_elig_coun_bilt_net(BigDecimal r231_cea_elig_coun_bilt_net) {
		this.r231_cea_elig_coun_bilt_net = r231_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR231_cea_aft_net() {
		return r231_cea_aft_net;
	}
	public void setR231_cea_aft_net(BigDecimal r231_cea_aft_net) {
		this.r231_cea_aft_net = r231_cea_aft_net;
	}
	public BigDecimal getR231_crm_sub_app_cea_elig() {
		return r231_crm_sub_app_cea_elig;
	}
	public void setR231_crm_sub_app_cea_elig(BigDecimal r231_crm_sub_app_cea_elig) {
		this.r231_crm_sub_app_cea_elig = r231_crm_sub_app_cea_elig;
	}
	public BigDecimal getR231_crm_sub_app_non_col_guar_elig() {
		return r231_crm_sub_app_non_col_guar_elig;
	}
	public void setR231_crm_sub_app_non_col_guar_elig(BigDecimal r231_crm_sub_app_non_col_guar_elig) {
		this.r231_crm_sub_app_non_col_guar_elig = r231_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR231_crm_sub_app_non_col_cre_der() {
		return r231_crm_sub_app_non_col_cre_der;
	}
	public void setR231_crm_sub_app_non_col_cre_der(BigDecimal r231_crm_sub_app_non_col_cre_der) {
		this.r231_crm_sub_app_non_col_cre_der = r231_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR231_crm_sub_app_col_elig_cash() {
		return r231_crm_sub_app_col_elig_cash;
	}
	public void setR231_crm_sub_app_col_elig_cash(BigDecimal r231_crm_sub_app_col_elig_cash) {
		this.r231_crm_sub_app_col_elig_cash = r231_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR231_crm_sub_app_col_elig_tre_bills() {
		return r231_crm_sub_app_col_elig_tre_bills;
	}
	public void setR231_crm_sub_app_col_elig_tre_bills(BigDecimal r231_crm_sub_app_col_elig_tre_bills) {
		this.r231_crm_sub_app_col_elig_tre_bills = r231_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR231_crm_sub_app_col_elig_deb_sec() {
		return r231_crm_sub_app_col_elig_deb_sec;
	}
	public void setR231_crm_sub_app_col_elig_deb_sec(BigDecimal r231_crm_sub_app_col_elig_deb_sec) {
		this.r231_crm_sub_app_col_elig_deb_sec = r231_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR231_crm_sub_app_col_elig_euiq() {
		return r231_crm_sub_app_col_elig_euiq;
	}
	public void setR231_crm_sub_app_col_elig_euiq(BigDecimal r231_crm_sub_app_col_elig_euiq) {
		this.r231_crm_sub_app_col_elig_euiq = r231_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR231_crm_sub_app_col_elig_uni_tru() {
		return r231_crm_sub_app_col_elig_uni_tru;
	}
	public void setR231_crm_sub_app_col_elig_uni_tru(BigDecimal r231_crm_sub_app_col_elig_uni_tru) {
		this.r231_crm_sub_app_col_elig_uni_tru = r231_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR231_crm_sub_app_col_cea_cov() {
		return r231_crm_sub_app_col_cea_cov;
	}
	public void setR231_crm_sub_app_col_cea_cov(BigDecimal r231_crm_sub_app_col_cea_cov) {
		this.r231_crm_sub_app_col_cea_cov = r231_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR231_crm_sub_app_col_cea_not_cov() {
		return r231_crm_sub_app_col_cea_not_cov;
	}
	public void setR231_crm_sub_app_col_cea_not_cov(BigDecimal r231_crm_sub_app_col_cea_not_cov) {
		this.r231_crm_sub_app_col_cea_not_cov = r231_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR231_crm_sub_app_rwa_ris_wei_crm() {
		return r231_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR231_crm_sub_app_rwa_ris_wei_crm(BigDecimal r231_crm_sub_app_rwa_ris_wei_crm) {
		this.r231_crm_sub_app_rwa_ris_wei_crm = r231_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR231_crm_sub_app_rwa_ris_cea_cov() {
		return r231_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR231_crm_sub_app_rwa_ris_cea_cov(BigDecimal r231_crm_sub_app_rwa_ris_cea_cov) {
		this.r231_crm_sub_app_rwa_ris_cea_cov = r231_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR231_crm_sub_app_rwa_appl_org_coun() {
		return r231_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR231_crm_sub_app_rwa_appl_org_coun(BigDecimal r231_crm_sub_app_rwa_appl_org_coun) {
		this.r231_crm_sub_app_rwa_appl_org_coun = r231_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR231_crm_sub_app_rwa_ris_cea_not_cov() {
		return r231_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR231_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r231_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r231_crm_sub_app_rwa_ris_cea_not_cov = r231_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR231_crm_com_app_col_cea_elig_crm() {
		return r231_crm_com_app_col_cea_elig_crm;
	}
	public void setR231_crm_com_app_col_cea_elig_crm(BigDecimal r231_crm_com_app_col_cea_elig_crm) {
		this.r231_crm_com_app_col_cea_elig_crm = r231_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR231_crm_com_app_col_elig_cea_vol_adj() {
		return r231_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR231_crm_com_app_col_elig_cea_vol_adj(BigDecimal r231_crm_com_app_col_elig_cea_vol_adj) {
		this.r231_crm_com_app_col_elig_cea_vol_adj = r231_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR231_crm_com_app_col_elig_fin_hai() {
		return r231_crm_com_app_col_elig_fin_hai;
	}
	public void setR231_crm_com_app_col_elig_fin_hai(BigDecimal r231_crm_com_app_col_elig_fin_hai) {
		this.r231_crm_com_app_col_elig_fin_hai = r231_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR231_crm_com_app_col_cea_val_aft_crm() {
		return r231_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR231_crm_com_app_col_cea_val_aft_crm(BigDecimal r231_crm_com_app_col_cea_val_aft_crm) {
		this.r231_crm_com_app_col_cea_val_aft_crm = r231_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR231_rwa_elig_cea_not_cov() {
		return r231_rwa_elig_cea_not_cov;
	}
	public void setR231_rwa_elig_cea_not_cov(BigDecimal r231_rwa_elig_cea_not_cov) {
		this.r231_rwa_elig_cea_not_cov = r231_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR231_rwa_unsec_cea_sub_cre_ris() {
		return r231_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR231_rwa_unsec_cea_sub_cre_ris(BigDecimal r231_rwa_unsec_cea_sub_cre_ris) {
		this.r231_rwa_unsec_cea_sub_cre_ris = r231_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR231_rwa_unsec_cea() {
		return r231_rwa_unsec_cea;
	}
	public void setR231_rwa_unsec_cea(BigDecimal r231_rwa_unsec_cea) {
		this.r231_rwa_unsec_cea = r231_rwa_unsec_cea;
	}
	public BigDecimal getR231_rwa_tot_ris_wei_ass() {
		return r231_rwa_tot_ris_wei_ass;
	}
	public void setR231_rwa_tot_ris_wei_ass(BigDecimal r231_rwa_tot_ris_wei_ass) {
		this.r231_rwa_tot_ris_wei_ass = r231_rwa_tot_ris_wei_ass;
	}
	public String getR232_exposure_class_off_bal() {
		return r232_exposure_class_off_bal;
	}
	public void setR232_exposure_class_off_bal(String r232_exposure_class_off_bal) {
		this.r232_exposure_class_off_bal = r232_exposure_class_off_bal;
	}
	public BigDecimal getR232_nom_pri_amt() {
		return r232_nom_pri_amt;
	}
	public void setR232_nom_pri_amt(BigDecimal r232_nom_pri_amt) {
		this.r232_nom_pri_amt = r232_nom_pri_amt;
	}
	public BigDecimal getR232_ccf() {
		return r232_ccf;
	}
	public void setR232_ccf(BigDecimal r232_ccf) {
		this.r232_ccf = r232_ccf;
	}
	public BigDecimal getR232_cea() {
		return r232_cea;
	}
	public void setR232_cea(BigDecimal r232_cea) {
		this.r232_cea = r232_cea;
	}
	public BigDecimal getR232_cea_elig_coun_bilt_net() {
		return r232_cea_elig_coun_bilt_net;
	}
	public void setR232_cea_elig_coun_bilt_net(BigDecimal r232_cea_elig_coun_bilt_net) {
		this.r232_cea_elig_coun_bilt_net = r232_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR232_cea_aft_net() {
		return r232_cea_aft_net;
	}
	public void setR232_cea_aft_net(BigDecimal r232_cea_aft_net) {
		this.r232_cea_aft_net = r232_cea_aft_net;
	}
	public BigDecimal getR232_crm_sub_app_cea_elig() {
		return r232_crm_sub_app_cea_elig;
	}
	public void setR232_crm_sub_app_cea_elig(BigDecimal r232_crm_sub_app_cea_elig) {
		this.r232_crm_sub_app_cea_elig = r232_crm_sub_app_cea_elig;
	}
	public BigDecimal getR232_crm_sub_app_non_col_guar_elig() {
		return r232_crm_sub_app_non_col_guar_elig;
	}
	public void setR232_crm_sub_app_non_col_guar_elig(BigDecimal r232_crm_sub_app_non_col_guar_elig) {
		this.r232_crm_sub_app_non_col_guar_elig = r232_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR232_crm_sub_app_non_col_cre_der() {
		return r232_crm_sub_app_non_col_cre_der;
	}
	public void setR232_crm_sub_app_non_col_cre_der(BigDecimal r232_crm_sub_app_non_col_cre_der) {
		this.r232_crm_sub_app_non_col_cre_der = r232_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR232_crm_sub_app_col_elig_cash() {
		return r232_crm_sub_app_col_elig_cash;
	}
	public void setR232_crm_sub_app_col_elig_cash(BigDecimal r232_crm_sub_app_col_elig_cash) {
		this.r232_crm_sub_app_col_elig_cash = r232_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR232_crm_sub_app_col_elig_tre_bills() {
		return r232_crm_sub_app_col_elig_tre_bills;
	}
	public void setR232_crm_sub_app_col_elig_tre_bills(BigDecimal r232_crm_sub_app_col_elig_tre_bills) {
		this.r232_crm_sub_app_col_elig_tre_bills = r232_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR232_crm_sub_app_col_elig_deb_sec() {
		return r232_crm_sub_app_col_elig_deb_sec;
	}
	public void setR232_crm_sub_app_col_elig_deb_sec(BigDecimal r232_crm_sub_app_col_elig_deb_sec) {
		this.r232_crm_sub_app_col_elig_deb_sec = r232_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR232_crm_sub_app_col_elig_euiq() {
		return r232_crm_sub_app_col_elig_euiq;
	}
	public void setR232_crm_sub_app_col_elig_euiq(BigDecimal r232_crm_sub_app_col_elig_euiq) {
		this.r232_crm_sub_app_col_elig_euiq = r232_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR232_crm_sub_app_col_elig_uni_tru() {
		return r232_crm_sub_app_col_elig_uni_tru;
	}
	public void setR232_crm_sub_app_col_elig_uni_tru(BigDecimal r232_crm_sub_app_col_elig_uni_tru) {
		this.r232_crm_sub_app_col_elig_uni_tru = r232_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR232_crm_sub_app_col_cea_cov() {
		return r232_crm_sub_app_col_cea_cov;
	}
	public void setR232_crm_sub_app_col_cea_cov(BigDecimal r232_crm_sub_app_col_cea_cov) {
		this.r232_crm_sub_app_col_cea_cov = r232_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR232_crm_sub_app_col_cea_not_cov() {
		return r232_crm_sub_app_col_cea_not_cov;
	}
	public void setR232_crm_sub_app_col_cea_not_cov(BigDecimal r232_crm_sub_app_col_cea_not_cov) {
		this.r232_crm_sub_app_col_cea_not_cov = r232_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR232_crm_sub_app_rwa_ris_wei_crm() {
		return r232_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR232_crm_sub_app_rwa_ris_wei_crm(BigDecimal r232_crm_sub_app_rwa_ris_wei_crm) {
		this.r232_crm_sub_app_rwa_ris_wei_crm = r232_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR232_crm_sub_app_rwa_ris_cea_cov() {
		return r232_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR232_crm_sub_app_rwa_ris_cea_cov(BigDecimal r232_crm_sub_app_rwa_ris_cea_cov) {
		this.r232_crm_sub_app_rwa_ris_cea_cov = r232_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR232_crm_sub_app_rwa_appl_org_coun() {
		return r232_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR232_crm_sub_app_rwa_appl_org_coun(BigDecimal r232_crm_sub_app_rwa_appl_org_coun) {
		this.r232_crm_sub_app_rwa_appl_org_coun = r232_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR232_crm_sub_app_rwa_ris_cea_not_cov() {
		return r232_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR232_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r232_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r232_crm_sub_app_rwa_ris_cea_not_cov = r232_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR232_crm_com_app_col_cea_elig_crm() {
		return r232_crm_com_app_col_cea_elig_crm;
	}
	public void setR232_crm_com_app_col_cea_elig_crm(BigDecimal r232_crm_com_app_col_cea_elig_crm) {
		this.r232_crm_com_app_col_cea_elig_crm = r232_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR232_crm_com_app_col_elig_cea_vol_adj() {
		return r232_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR232_crm_com_app_col_elig_cea_vol_adj(BigDecimal r232_crm_com_app_col_elig_cea_vol_adj) {
		this.r232_crm_com_app_col_elig_cea_vol_adj = r232_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR232_crm_com_app_col_elig_fin_hai() {
		return r232_crm_com_app_col_elig_fin_hai;
	}
	public void setR232_crm_com_app_col_elig_fin_hai(BigDecimal r232_crm_com_app_col_elig_fin_hai) {
		this.r232_crm_com_app_col_elig_fin_hai = r232_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR232_crm_com_app_col_cea_val_aft_crm() {
		return r232_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR232_crm_com_app_col_cea_val_aft_crm(BigDecimal r232_crm_com_app_col_cea_val_aft_crm) {
		this.r232_crm_com_app_col_cea_val_aft_crm = r232_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR232_rwa_elig_cea_not_cov() {
		return r232_rwa_elig_cea_not_cov;
	}
	public void setR232_rwa_elig_cea_not_cov(BigDecimal r232_rwa_elig_cea_not_cov) {
		this.r232_rwa_elig_cea_not_cov = r232_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR232_rwa_unsec_cea_sub_cre_ris() {
		return r232_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR232_rwa_unsec_cea_sub_cre_ris(BigDecimal r232_rwa_unsec_cea_sub_cre_ris) {
		this.r232_rwa_unsec_cea_sub_cre_ris = r232_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR232_rwa_unsec_cea() {
		return r232_rwa_unsec_cea;
	}
	public void setR232_rwa_unsec_cea(BigDecimal r232_rwa_unsec_cea) {
		this.r232_rwa_unsec_cea = r232_rwa_unsec_cea;
	}
	public BigDecimal getR232_rwa_tot_ris_wei_ass() {
		return r232_rwa_tot_ris_wei_ass;
	}
	public void setR232_rwa_tot_ris_wei_ass(BigDecimal r232_rwa_tot_ris_wei_ass) {
		this.r232_rwa_tot_ris_wei_ass = r232_rwa_tot_ris_wei_ass;
	}
	public String getR233_exposure_class_off_bal() {
		return r233_exposure_class_off_bal;
	}
	public void setR233_exposure_class_off_bal(String r233_exposure_class_off_bal) {
		this.r233_exposure_class_off_bal = r233_exposure_class_off_bal;
	}
	public BigDecimal getR233_nom_pri_amt() {
		return r233_nom_pri_amt;
	}
	public void setR233_nom_pri_amt(BigDecimal r233_nom_pri_amt) {
		this.r233_nom_pri_amt = r233_nom_pri_amt;
	}
	public BigDecimal getR233_ccf() {
		return r233_ccf;
	}
	public void setR233_ccf(BigDecimal r233_ccf) {
		this.r233_ccf = r233_ccf;
	}
	public BigDecimal getR233_cea() {
		return r233_cea;
	}
	public void setR233_cea(BigDecimal r233_cea) {
		this.r233_cea = r233_cea;
	}
	public BigDecimal getR233_cea_elig_coun_bilt_net() {
		return r233_cea_elig_coun_bilt_net;
	}
	public void setR233_cea_elig_coun_bilt_net(BigDecimal r233_cea_elig_coun_bilt_net) {
		this.r233_cea_elig_coun_bilt_net = r233_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR233_cea_aft_net() {
		return r233_cea_aft_net;
	}
	public void setR233_cea_aft_net(BigDecimal r233_cea_aft_net) {
		this.r233_cea_aft_net = r233_cea_aft_net;
	}
	public BigDecimal getR233_crm_sub_app_cea_elig() {
		return r233_crm_sub_app_cea_elig;
	}
	public void setR233_crm_sub_app_cea_elig(BigDecimal r233_crm_sub_app_cea_elig) {
		this.r233_crm_sub_app_cea_elig = r233_crm_sub_app_cea_elig;
	}
	public BigDecimal getR233_crm_sub_app_non_col_guar_elig() {
		return r233_crm_sub_app_non_col_guar_elig;
	}
	public void setR233_crm_sub_app_non_col_guar_elig(BigDecimal r233_crm_sub_app_non_col_guar_elig) {
		this.r233_crm_sub_app_non_col_guar_elig = r233_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR233_crm_sub_app_non_col_cre_der() {
		return r233_crm_sub_app_non_col_cre_der;
	}
	public void setR233_crm_sub_app_non_col_cre_der(BigDecimal r233_crm_sub_app_non_col_cre_der) {
		this.r233_crm_sub_app_non_col_cre_der = r233_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR233_crm_sub_app_col_elig_cash() {
		return r233_crm_sub_app_col_elig_cash;
	}
	public void setR233_crm_sub_app_col_elig_cash(BigDecimal r233_crm_sub_app_col_elig_cash) {
		this.r233_crm_sub_app_col_elig_cash = r233_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR233_crm_sub_app_col_elig_tre_bills() {
		return r233_crm_sub_app_col_elig_tre_bills;
	}
	public void setR233_crm_sub_app_col_elig_tre_bills(BigDecimal r233_crm_sub_app_col_elig_tre_bills) {
		this.r233_crm_sub_app_col_elig_tre_bills = r233_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR233_crm_sub_app_col_elig_deb_sec() {
		return r233_crm_sub_app_col_elig_deb_sec;
	}
	public void setR233_crm_sub_app_col_elig_deb_sec(BigDecimal r233_crm_sub_app_col_elig_deb_sec) {
		this.r233_crm_sub_app_col_elig_deb_sec = r233_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR233_crm_sub_app_col_elig_euiq() {
		return r233_crm_sub_app_col_elig_euiq;
	}
	public void setR233_crm_sub_app_col_elig_euiq(BigDecimal r233_crm_sub_app_col_elig_euiq) {
		this.r233_crm_sub_app_col_elig_euiq = r233_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR233_crm_sub_app_col_elig_uni_tru() {
		return r233_crm_sub_app_col_elig_uni_tru;
	}
	public void setR233_crm_sub_app_col_elig_uni_tru(BigDecimal r233_crm_sub_app_col_elig_uni_tru) {
		this.r233_crm_sub_app_col_elig_uni_tru = r233_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR233_crm_sub_app_col_cea_cov() {
		return r233_crm_sub_app_col_cea_cov;
	}
	public void setR233_crm_sub_app_col_cea_cov(BigDecimal r233_crm_sub_app_col_cea_cov) {
		this.r233_crm_sub_app_col_cea_cov = r233_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR233_crm_sub_app_col_cea_not_cov() {
		return r233_crm_sub_app_col_cea_not_cov;
	}
	public void setR233_crm_sub_app_col_cea_not_cov(BigDecimal r233_crm_sub_app_col_cea_not_cov) {
		this.r233_crm_sub_app_col_cea_not_cov = r233_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR233_crm_sub_app_rwa_ris_wei_crm() {
		return r233_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR233_crm_sub_app_rwa_ris_wei_crm(BigDecimal r233_crm_sub_app_rwa_ris_wei_crm) {
		this.r233_crm_sub_app_rwa_ris_wei_crm = r233_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR233_crm_sub_app_rwa_ris_cea_cov() {
		return r233_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR233_crm_sub_app_rwa_ris_cea_cov(BigDecimal r233_crm_sub_app_rwa_ris_cea_cov) {
		this.r233_crm_sub_app_rwa_ris_cea_cov = r233_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR233_crm_sub_app_rwa_appl_org_coun() {
		return r233_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR233_crm_sub_app_rwa_appl_org_coun(BigDecimal r233_crm_sub_app_rwa_appl_org_coun) {
		this.r233_crm_sub_app_rwa_appl_org_coun = r233_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR233_crm_sub_app_rwa_ris_cea_not_cov() {
		return r233_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR233_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r233_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r233_crm_sub_app_rwa_ris_cea_not_cov = r233_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR233_crm_com_app_col_cea_elig_crm() {
		return r233_crm_com_app_col_cea_elig_crm;
	}
	public void setR233_crm_com_app_col_cea_elig_crm(BigDecimal r233_crm_com_app_col_cea_elig_crm) {
		this.r233_crm_com_app_col_cea_elig_crm = r233_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR233_crm_com_app_col_elig_cea_vol_adj() {
		return r233_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR233_crm_com_app_col_elig_cea_vol_adj(BigDecimal r233_crm_com_app_col_elig_cea_vol_adj) {
		this.r233_crm_com_app_col_elig_cea_vol_adj = r233_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR233_crm_com_app_col_elig_fin_hai() {
		return r233_crm_com_app_col_elig_fin_hai;
	}
	public void setR233_crm_com_app_col_elig_fin_hai(BigDecimal r233_crm_com_app_col_elig_fin_hai) {
		this.r233_crm_com_app_col_elig_fin_hai = r233_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR233_crm_com_app_col_cea_val_aft_crm() {
		return r233_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR233_crm_com_app_col_cea_val_aft_crm(BigDecimal r233_crm_com_app_col_cea_val_aft_crm) {
		this.r233_crm_com_app_col_cea_val_aft_crm = r233_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR233_rwa_elig_cea_not_cov() {
		return r233_rwa_elig_cea_not_cov;
	}
	public void setR233_rwa_elig_cea_not_cov(BigDecimal r233_rwa_elig_cea_not_cov) {
		this.r233_rwa_elig_cea_not_cov = r233_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR233_rwa_unsec_cea_sub_cre_ris() {
		return r233_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR233_rwa_unsec_cea_sub_cre_ris(BigDecimal r233_rwa_unsec_cea_sub_cre_ris) {
		this.r233_rwa_unsec_cea_sub_cre_ris = r233_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR233_rwa_unsec_cea() {
		return r233_rwa_unsec_cea;
	}
	public void setR233_rwa_unsec_cea(BigDecimal r233_rwa_unsec_cea) {
		this.r233_rwa_unsec_cea = r233_rwa_unsec_cea;
	}
	public BigDecimal getR233_rwa_tot_ris_wei_ass() {
		return r233_rwa_tot_ris_wei_ass;
	}
	public void setR233_rwa_tot_ris_wei_ass(BigDecimal r233_rwa_tot_ris_wei_ass) {
		this.r233_rwa_tot_ris_wei_ass = r233_rwa_tot_ris_wei_ass;
	}
	public String getR234_exposure_class_off_bal() {
		return r234_exposure_class_off_bal;
	}
	public void setR234_exposure_class_off_bal(String r234_exposure_class_off_bal) {
		this.r234_exposure_class_off_bal = r234_exposure_class_off_bal;
	}
	public BigDecimal getR234_nom_pri_amt() {
		return r234_nom_pri_amt;
	}
	public void setR234_nom_pri_amt(BigDecimal r234_nom_pri_amt) {
		this.r234_nom_pri_amt = r234_nom_pri_amt;
	}
	public BigDecimal getR234_ccf() {
		return r234_ccf;
	}
	public void setR234_ccf(BigDecimal r234_ccf) {
		this.r234_ccf = r234_ccf;
	}
	public BigDecimal getR234_cea() {
		return r234_cea;
	}
	public void setR234_cea(BigDecimal r234_cea) {
		this.r234_cea = r234_cea;
	}
	public BigDecimal getR234_cea_elig_coun_bilt_net() {
		return r234_cea_elig_coun_bilt_net;
	}
	public void setR234_cea_elig_coun_bilt_net(BigDecimal r234_cea_elig_coun_bilt_net) {
		this.r234_cea_elig_coun_bilt_net = r234_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR234_cea_aft_net() {
		return r234_cea_aft_net;
	}
	public void setR234_cea_aft_net(BigDecimal r234_cea_aft_net) {
		this.r234_cea_aft_net = r234_cea_aft_net;
	}
	public BigDecimal getR234_crm_sub_app_cea_elig() {
		return r234_crm_sub_app_cea_elig;
	}
	public void setR234_crm_sub_app_cea_elig(BigDecimal r234_crm_sub_app_cea_elig) {
		this.r234_crm_sub_app_cea_elig = r234_crm_sub_app_cea_elig;
	}
	public BigDecimal getR234_crm_sub_app_non_col_guar_elig() {
		return r234_crm_sub_app_non_col_guar_elig;
	}
	public void setR234_crm_sub_app_non_col_guar_elig(BigDecimal r234_crm_sub_app_non_col_guar_elig) {
		this.r234_crm_sub_app_non_col_guar_elig = r234_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR234_crm_sub_app_non_col_cre_der() {
		return r234_crm_sub_app_non_col_cre_der;
	}
	public void setR234_crm_sub_app_non_col_cre_der(BigDecimal r234_crm_sub_app_non_col_cre_der) {
		this.r234_crm_sub_app_non_col_cre_der = r234_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR234_crm_sub_app_col_elig_cash() {
		return r234_crm_sub_app_col_elig_cash;
	}
	public void setR234_crm_sub_app_col_elig_cash(BigDecimal r234_crm_sub_app_col_elig_cash) {
		this.r234_crm_sub_app_col_elig_cash = r234_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR234_crm_sub_app_col_elig_tre_bills() {
		return r234_crm_sub_app_col_elig_tre_bills;
	}
	public void setR234_crm_sub_app_col_elig_tre_bills(BigDecimal r234_crm_sub_app_col_elig_tre_bills) {
		this.r234_crm_sub_app_col_elig_tre_bills = r234_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR234_crm_sub_app_col_elig_deb_sec() {
		return r234_crm_sub_app_col_elig_deb_sec;
	}
	public void setR234_crm_sub_app_col_elig_deb_sec(BigDecimal r234_crm_sub_app_col_elig_deb_sec) {
		this.r234_crm_sub_app_col_elig_deb_sec = r234_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR234_crm_sub_app_col_elig_euiq() {
		return r234_crm_sub_app_col_elig_euiq;
	}
	public void setR234_crm_sub_app_col_elig_euiq(BigDecimal r234_crm_sub_app_col_elig_euiq) {
		this.r234_crm_sub_app_col_elig_euiq = r234_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR234_crm_sub_app_col_elig_uni_tru() {
		return r234_crm_sub_app_col_elig_uni_tru;
	}
	public void setR234_crm_sub_app_col_elig_uni_tru(BigDecimal r234_crm_sub_app_col_elig_uni_tru) {
		this.r234_crm_sub_app_col_elig_uni_tru = r234_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR234_crm_sub_app_col_cea_cov() {
		return r234_crm_sub_app_col_cea_cov;
	}
	public void setR234_crm_sub_app_col_cea_cov(BigDecimal r234_crm_sub_app_col_cea_cov) {
		this.r234_crm_sub_app_col_cea_cov = r234_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR234_crm_sub_app_col_cea_not_cov() {
		return r234_crm_sub_app_col_cea_not_cov;
	}
	public void setR234_crm_sub_app_col_cea_not_cov(BigDecimal r234_crm_sub_app_col_cea_not_cov) {
		this.r234_crm_sub_app_col_cea_not_cov = r234_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR234_crm_sub_app_rwa_ris_wei_crm() {
		return r234_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR234_crm_sub_app_rwa_ris_wei_crm(BigDecimal r234_crm_sub_app_rwa_ris_wei_crm) {
		this.r234_crm_sub_app_rwa_ris_wei_crm = r234_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR234_crm_sub_app_rwa_ris_cea_cov() {
		return r234_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR234_crm_sub_app_rwa_ris_cea_cov(BigDecimal r234_crm_sub_app_rwa_ris_cea_cov) {
		this.r234_crm_sub_app_rwa_ris_cea_cov = r234_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR234_crm_sub_app_rwa_appl_org_coun() {
		return r234_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR234_crm_sub_app_rwa_appl_org_coun(BigDecimal r234_crm_sub_app_rwa_appl_org_coun) {
		this.r234_crm_sub_app_rwa_appl_org_coun = r234_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR234_crm_sub_app_rwa_ris_cea_not_cov() {
		return r234_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR234_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r234_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r234_crm_sub_app_rwa_ris_cea_not_cov = r234_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR234_crm_com_app_col_cea_elig_crm() {
		return r234_crm_com_app_col_cea_elig_crm;
	}
	public void setR234_crm_com_app_col_cea_elig_crm(BigDecimal r234_crm_com_app_col_cea_elig_crm) {
		this.r234_crm_com_app_col_cea_elig_crm = r234_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR234_crm_com_app_col_elig_cea_vol_adj() {
		return r234_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR234_crm_com_app_col_elig_cea_vol_adj(BigDecimal r234_crm_com_app_col_elig_cea_vol_adj) {
		this.r234_crm_com_app_col_elig_cea_vol_adj = r234_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR234_crm_com_app_col_elig_fin_hai() {
		return r234_crm_com_app_col_elig_fin_hai;
	}
	public void setR234_crm_com_app_col_elig_fin_hai(BigDecimal r234_crm_com_app_col_elig_fin_hai) {
		this.r234_crm_com_app_col_elig_fin_hai = r234_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR234_crm_com_app_col_cea_val_aft_crm() {
		return r234_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR234_crm_com_app_col_cea_val_aft_crm(BigDecimal r234_crm_com_app_col_cea_val_aft_crm) {
		this.r234_crm_com_app_col_cea_val_aft_crm = r234_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR234_rwa_elig_cea_not_cov() {
		return r234_rwa_elig_cea_not_cov;
	}
	public void setR234_rwa_elig_cea_not_cov(BigDecimal r234_rwa_elig_cea_not_cov) {
		this.r234_rwa_elig_cea_not_cov = r234_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR234_rwa_unsec_cea_sub_cre_ris() {
		return r234_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR234_rwa_unsec_cea_sub_cre_ris(BigDecimal r234_rwa_unsec_cea_sub_cre_ris) {
		this.r234_rwa_unsec_cea_sub_cre_ris = r234_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR234_rwa_unsec_cea() {
		return r234_rwa_unsec_cea;
	}
	public void setR234_rwa_unsec_cea(BigDecimal r234_rwa_unsec_cea) {
		this.r234_rwa_unsec_cea = r234_rwa_unsec_cea;
	}
	public BigDecimal getR234_rwa_tot_ris_wei_ass() {
		return r234_rwa_tot_ris_wei_ass;
	}
	public void setR234_rwa_tot_ris_wei_ass(BigDecimal r234_rwa_tot_ris_wei_ass) {
		this.r234_rwa_tot_ris_wei_ass = r234_rwa_tot_ris_wei_ass;
	}
	public String getR235_exposure_class_off_bal() {
		return r235_exposure_class_off_bal;
	}
	public void setR235_exposure_class_off_bal(String r235_exposure_class_off_bal) {
		this.r235_exposure_class_off_bal = r235_exposure_class_off_bal;
	}
	public BigDecimal getR235_nom_pri_amt() {
		return r235_nom_pri_amt;
	}
	public void setR235_nom_pri_amt(BigDecimal r235_nom_pri_amt) {
		this.r235_nom_pri_amt = r235_nom_pri_amt;
	}
	public BigDecimal getR235_ccf() {
		return r235_ccf;
	}
	public void setR235_ccf(BigDecimal r235_ccf) {
		this.r235_ccf = r235_ccf;
	}
	public BigDecimal getR235_cea() {
		return r235_cea;
	}
	public void setR235_cea(BigDecimal r235_cea) {
		this.r235_cea = r235_cea;
	}
	public BigDecimal getR235_cea_elig_coun_bilt_net() {
		return r235_cea_elig_coun_bilt_net;
	}
	public void setR235_cea_elig_coun_bilt_net(BigDecimal r235_cea_elig_coun_bilt_net) {
		this.r235_cea_elig_coun_bilt_net = r235_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR235_cea_aft_net() {
		return r235_cea_aft_net;
	}
	public void setR235_cea_aft_net(BigDecimal r235_cea_aft_net) {
		this.r235_cea_aft_net = r235_cea_aft_net;
	}
	public BigDecimal getR235_crm_sub_app_cea_elig() {
		return r235_crm_sub_app_cea_elig;
	}
	public void setR235_crm_sub_app_cea_elig(BigDecimal r235_crm_sub_app_cea_elig) {
		this.r235_crm_sub_app_cea_elig = r235_crm_sub_app_cea_elig;
	}
	public BigDecimal getR235_crm_sub_app_non_col_guar_elig() {
		return r235_crm_sub_app_non_col_guar_elig;
	}
	public void setR235_crm_sub_app_non_col_guar_elig(BigDecimal r235_crm_sub_app_non_col_guar_elig) {
		this.r235_crm_sub_app_non_col_guar_elig = r235_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR235_crm_sub_app_non_col_cre_der() {
		return r235_crm_sub_app_non_col_cre_der;
	}
	public void setR235_crm_sub_app_non_col_cre_der(BigDecimal r235_crm_sub_app_non_col_cre_der) {
		this.r235_crm_sub_app_non_col_cre_der = r235_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR235_crm_sub_app_col_elig_cash() {
		return r235_crm_sub_app_col_elig_cash;
	}
	public void setR235_crm_sub_app_col_elig_cash(BigDecimal r235_crm_sub_app_col_elig_cash) {
		this.r235_crm_sub_app_col_elig_cash = r235_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR235_crm_sub_app_col_elig_tre_bills() {
		return r235_crm_sub_app_col_elig_tre_bills;
	}
	public void setR235_crm_sub_app_col_elig_tre_bills(BigDecimal r235_crm_sub_app_col_elig_tre_bills) {
		this.r235_crm_sub_app_col_elig_tre_bills = r235_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR235_crm_sub_app_col_elig_deb_sec() {
		return r235_crm_sub_app_col_elig_deb_sec;
	}
	public void setR235_crm_sub_app_col_elig_deb_sec(BigDecimal r235_crm_sub_app_col_elig_deb_sec) {
		this.r235_crm_sub_app_col_elig_deb_sec = r235_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR235_crm_sub_app_col_elig_euiq() {
		return r235_crm_sub_app_col_elig_euiq;
	}
	public void setR235_crm_sub_app_col_elig_euiq(BigDecimal r235_crm_sub_app_col_elig_euiq) {
		this.r235_crm_sub_app_col_elig_euiq = r235_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR235_crm_sub_app_col_elig_uni_tru() {
		return r235_crm_sub_app_col_elig_uni_tru;
	}
	public void setR235_crm_sub_app_col_elig_uni_tru(BigDecimal r235_crm_sub_app_col_elig_uni_tru) {
		this.r235_crm_sub_app_col_elig_uni_tru = r235_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR235_crm_sub_app_col_cea_cov() {
		return r235_crm_sub_app_col_cea_cov;
	}
	public void setR235_crm_sub_app_col_cea_cov(BigDecimal r235_crm_sub_app_col_cea_cov) {
		this.r235_crm_sub_app_col_cea_cov = r235_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR235_crm_sub_app_col_cea_not_cov() {
		return r235_crm_sub_app_col_cea_not_cov;
	}
	public void setR235_crm_sub_app_col_cea_not_cov(BigDecimal r235_crm_sub_app_col_cea_not_cov) {
		this.r235_crm_sub_app_col_cea_not_cov = r235_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR235_crm_sub_app_rwa_ris_wei_crm() {
		return r235_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR235_crm_sub_app_rwa_ris_wei_crm(BigDecimal r235_crm_sub_app_rwa_ris_wei_crm) {
		this.r235_crm_sub_app_rwa_ris_wei_crm = r235_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR235_crm_sub_app_rwa_ris_cea_cov() {
		return r235_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR235_crm_sub_app_rwa_ris_cea_cov(BigDecimal r235_crm_sub_app_rwa_ris_cea_cov) {
		this.r235_crm_sub_app_rwa_ris_cea_cov = r235_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR235_crm_sub_app_rwa_appl_org_coun() {
		return r235_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR235_crm_sub_app_rwa_appl_org_coun(BigDecimal r235_crm_sub_app_rwa_appl_org_coun) {
		this.r235_crm_sub_app_rwa_appl_org_coun = r235_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR235_crm_sub_app_rwa_ris_cea_not_cov() {
		return r235_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR235_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r235_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r235_crm_sub_app_rwa_ris_cea_not_cov = r235_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR235_crm_com_app_col_cea_elig_crm() {
		return r235_crm_com_app_col_cea_elig_crm;
	}
	public void setR235_crm_com_app_col_cea_elig_crm(BigDecimal r235_crm_com_app_col_cea_elig_crm) {
		this.r235_crm_com_app_col_cea_elig_crm = r235_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR235_crm_com_app_col_elig_cea_vol_adj() {
		return r235_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR235_crm_com_app_col_elig_cea_vol_adj(BigDecimal r235_crm_com_app_col_elig_cea_vol_adj) {
		this.r235_crm_com_app_col_elig_cea_vol_adj = r235_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR235_crm_com_app_col_elig_fin_hai() {
		return r235_crm_com_app_col_elig_fin_hai;
	}
	public void setR235_crm_com_app_col_elig_fin_hai(BigDecimal r235_crm_com_app_col_elig_fin_hai) {
		this.r235_crm_com_app_col_elig_fin_hai = r235_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR235_crm_com_app_col_cea_val_aft_crm() {
		return r235_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR235_crm_com_app_col_cea_val_aft_crm(BigDecimal r235_crm_com_app_col_cea_val_aft_crm) {
		this.r235_crm_com_app_col_cea_val_aft_crm = r235_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR235_rwa_elig_cea_not_cov() {
		return r235_rwa_elig_cea_not_cov;
	}
	public void setR235_rwa_elig_cea_not_cov(BigDecimal r235_rwa_elig_cea_not_cov) {
		this.r235_rwa_elig_cea_not_cov = r235_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR235_rwa_unsec_cea_sub_cre_ris() {
		return r235_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR235_rwa_unsec_cea_sub_cre_ris(BigDecimal r235_rwa_unsec_cea_sub_cre_ris) {
		this.r235_rwa_unsec_cea_sub_cre_ris = r235_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR235_rwa_unsec_cea() {
		return r235_rwa_unsec_cea;
	}
	public void setR235_rwa_unsec_cea(BigDecimal r235_rwa_unsec_cea) {
		this.r235_rwa_unsec_cea = r235_rwa_unsec_cea;
	}
	public BigDecimal getR235_rwa_tot_ris_wei_ass() {
		return r235_rwa_tot_ris_wei_ass;
	}
	public void setR235_rwa_tot_ris_wei_ass(BigDecimal r235_rwa_tot_ris_wei_ass) {
		this.r235_rwa_tot_ris_wei_ass = r235_rwa_tot_ris_wei_ass;
	}
	public String getR236_exposure_class_off_bal() {
		return r236_exposure_class_off_bal;
	}
	public void setR236_exposure_class_off_bal(String r236_exposure_class_off_bal) {
		this.r236_exposure_class_off_bal = r236_exposure_class_off_bal;
	}
	public BigDecimal getR236_nom_pri_amt() {
		return r236_nom_pri_amt;
	}
	public void setR236_nom_pri_amt(BigDecimal r236_nom_pri_amt) {
		this.r236_nom_pri_amt = r236_nom_pri_amt;
	}
	public BigDecimal getR236_ccf() {
		return r236_ccf;
	}
	public void setR236_ccf(BigDecimal r236_ccf) {
		this.r236_ccf = r236_ccf;
	}
	public BigDecimal getR236_cea() {
		return r236_cea;
	}
	public void setR236_cea(BigDecimal r236_cea) {
		this.r236_cea = r236_cea;
	}
	public BigDecimal getR236_cea_elig_coun_bilt_net() {
		return r236_cea_elig_coun_bilt_net;
	}
	public void setR236_cea_elig_coun_bilt_net(BigDecimal r236_cea_elig_coun_bilt_net) {
		this.r236_cea_elig_coun_bilt_net = r236_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR236_cea_aft_net() {
		return r236_cea_aft_net;
	}
	public void setR236_cea_aft_net(BigDecimal r236_cea_aft_net) {
		this.r236_cea_aft_net = r236_cea_aft_net;
	}
	public BigDecimal getR236_crm_sub_app_cea_elig() {
		return r236_crm_sub_app_cea_elig;
	}
	public void setR236_crm_sub_app_cea_elig(BigDecimal r236_crm_sub_app_cea_elig) {
		this.r236_crm_sub_app_cea_elig = r236_crm_sub_app_cea_elig;
	}
	public BigDecimal getR236_crm_sub_app_non_col_guar_elig() {
		return r236_crm_sub_app_non_col_guar_elig;
	}
	public void setR236_crm_sub_app_non_col_guar_elig(BigDecimal r236_crm_sub_app_non_col_guar_elig) {
		this.r236_crm_sub_app_non_col_guar_elig = r236_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR236_crm_sub_app_non_col_cre_der() {
		return r236_crm_sub_app_non_col_cre_der;
	}
	public void setR236_crm_sub_app_non_col_cre_der(BigDecimal r236_crm_sub_app_non_col_cre_der) {
		this.r236_crm_sub_app_non_col_cre_der = r236_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR236_crm_sub_app_col_elig_cash() {
		return r236_crm_sub_app_col_elig_cash;
	}
	public void setR236_crm_sub_app_col_elig_cash(BigDecimal r236_crm_sub_app_col_elig_cash) {
		this.r236_crm_sub_app_col_elig_cash = r236_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR236_crm_sub_app_col_elig_tre_bills() {
		return r236_crm_sub_app_col_elig_tre_bills;
	}
	public void setR236_crm_sub_app_col_elig_tre_bills(BigDecimal r236_crm_sub_app_col_elig_tre_bills) {
		this.r236_crm_sub_app_col_elig_tre_bills = r236_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR236_crm_sub_app_col_elig_deb_sec() {
		return r236_crm_sub_app_col_elig_deb_sec;
	}
	public void setR236_crm_sub_app_col_elig_deb_sec(BigDecimal r236_crm_sub_app_col_elig_deb_sec) {
		this.r236_crm_sub_app_col_elig_deb_sec = r236_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR236_crm_sub_app_col_elig_euiq() {
		return r236_crm_sub_app_col_elig_euiq;
	}
	public void setR236_crm_sub_app_col_elig_euiq(BigDecimal r236_crm_sub_app_col_elig_euiq) {
		this.r236_crm_sub_app_col_elig_euiq = r236_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR236_crm_sub_app_col_elig_uni_tru() {
		return r236_crm_sub_app_col_elig_uni_tru;
	}
	public void setR236_crm_sub_app_col_elig_uni_tru(BigDecimal r236_crm_sub_app_col_elig_uni_tru) {
		this.r236_crm_sub_app_col_elig_uni_tru = r236_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR236_crm_sub_app_col_cea_cov() {
		return r236_crm_sub_app_col_cea_cov;
	}
	public void setR236_crm_sub_app_col_cea_cov(BigDecimal r236_crm_sub_app_col_cea_cov) {
		this.r236_crm_sub_app_col_cea_cov = r236_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR236_crm_sub_app_col_cea_not_cov() {
		return r236_crm_sub_app_col_cea_not_cov;
	}
	public void setR236_crm_sub_app_col_cea_not_cov(BigDecimal r236_crm_sub_app_col_cea_not_cov) {
		this.r236_crm_sub_app_col_cea_not_cov = r236_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR236_crm_sub_app_rwa_ris_wei_crm() {
		return r236_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR236_crm_sub_app_rwa_ris_wei_crm(BigDecimal r236_crm_sub_app_rwa_ris_wei_crm) {
		this.r236_crm_sub_app_rwa_ris_wei_crm = r236_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR236_crm_sub_app_rwa_ris_cea_cov() {
		return r236_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR236_crm_sub_app_rwa_ris_cea_cov(BigDecimal r236_crm_sub_app_rwa_ris_cea_cov) {
		this.r236_crm_sub_app_rwa_ris_cea_cov = r236_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR236_crm_sub_app_rwa_appl_org_coun() {
		return r236_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR236_crm_sub_app_rwa_appl_org_coun(BigDecimal r236_crm_sub_app_rwa_appl_org_coun) {
		this.r236_crm_sub_app_rwa_appl_org_coun = r236_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR236_crm_sub_app_rwa_ris_cea_not_cov() {
		return r236_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR236_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r236_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r236_crm_sub_app_rwa_ris_cea_not_cov = r236_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR236_crm_com_app_col_cea_elig_crm() {
		return r236_crm_com_app_col_cea_elig_crm;
	}
	public void setR236_crm_com_app_col_cea_elig_crm(BigDecimal r236_crm_com_app_col_cea_elig_crm) {
		this.r236_crm_com_app_col_cea_elig_crm = r236_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR236_crm_com_app_col_elig_cea_vol_adj() {
		return r236_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR236_crm_com_app_col_elig_cea_vol_adj(BigDecimal r236_crm_com_app_col_elig_cea_vol_adj) {
		this.r236_crm_com_app_col_elig_cea_vol_adj = r236_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR236_crm_com_app_col_elig_fin_hai() {
		return r236_crm_com_app_col_elig_fin_hai;
	}
	public void setR236_crm_com_app_col_elig_fin_hai(BigDecimal r236_crm_com_app_col_elig_fin_hai) {
		this.r236_crm_com_app_col_elig_fin_hai = r236_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR236_crm_com_app_col_cea_val_aft_crm() {
		return r236_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR236_crm_com_app_col_cea_val_aft_crm(BigDecimal r236_crm_com_app_col_cea_val_aft_crm) {
		this.r236_crm_com_app_col_cea_val_aft_crm = r236_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR236_rwa_elig_cea_not_cov() {
		return r236_rwa_elig_cea_not_cov;
	}
	public void setR236_rwa_elig_cea_not_cov(BigDecimal r236_rwa_elig_cea_not_cov) {
		this.r236_rwa_elig_cea_not_cov = r236_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR236_rwa_unsec_cea_sub_cre_ris() {
		return r236_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR236_rwa_unsec_cea_sub_cre_ris(BigDecimal r236_rwa_unsec_cea_sub_cre_ris) {
		this.r236_rwa_unsec_cea_sub_cre_ris = r236_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR236_rwa_unsec_cea() {
		return r236_rwa_unsec_cea;
	}
	public void setR236_rwa_unsec_cea(BigDecimal r236_rwa_unsec_cea) {
		this.r236_rwa_unsec_cea = r236_rwa_unsec_cea;
	}
	public BigDecimal getR236_rwa_tot_ris_wei_ass() {
		return r236_rwa_tot_ris_wei_ass;
	}
	public void setR236_rwa_tot_ris_wei_ass(BigDecimal r236_rwa_tot_ris_wei_ass) {
		this.r236_rwa_tot_ris_wei_ass = r236_rwa_tot_ris_wei_ass;
	}
	public String getR237_exposure_class_off_bal() {
		return r237_exposure_class_off_bal;
	}
	public void setR237_exposure_class_off_bal(String r237_exposure_class_off_bal) {
		this.r237_exposure_class_off_bal = r237_exposure_class_off_bal;
	}
	public BigDecimal getR237_nom_pri_amt() {
		return r237_nom_pri_amt;
	}
	public void setR237_nom_pri_amt(BigDecimal r237_nom_pri_amt) {
		this.r237_nom_pri_amt = r237_nom_pri_amt;
	}
	public BigDecimal getR237_ccf() {
		return r237_ccf;
	}
	public void setR237_ccf(BigDecimal r237_ccf) {
		this.r237_ccf = r237_ccf;
	}
	public BigDecimal getR237_cea() {
		return r237_cea;
	}
	public void setR237_cea(BigDecimal r237_cea) {
		this.r237_cea = r237_cea;
	}
	public BigDecimal getR237_cea_elig_coun_bilt_net() {
		return r237_cea_elig_coun_bilt_net;
	}
	public void setR237_cea_elig_coun_bilt_net(BigDecimal r237_cea_elig_coun_bilt_net) {
		this.r237_cea_elig_coun_bilt_net = r237_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR237_cea_aft_net() {
		return r237_cea_aft_net;
	}
	public void setR237_cea_aft_net(BigDecimal r237_cea_aft_net) {
		this.r237_cea_aft_net = r237_cea_aft_net;
	}
	public BigDecimal getR237_crm_sub_app_cea_elig() {
		return r237_crm_sub_app_cea_elig;
	}
	public void setR237_crm_sub_app_cea_elig(BigDecimal r237_crm_sub_app_cea_elig) {
		this.r237_crm_sub_app_cea_elig = r237_crm_sub_app_cea_elig;
	}
	public BigDecimal getR237_crm_sub_app_non_col_guar_elig() {
		return r237_crm_sub_app_non_col_guar_elig;
	}
	public void setR237_crm_sub_app_non_col_guar_elig(BigDecimal r237_crm_sub_app_non_col_guar_elig) {
		this.r237_crm_sub_app_non_col_guar_elig = r237_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR237_crm_sub_app_non_col_cre_der() {
		return r237_crm_sub_app_non_col_cre_der;
	}
	public void setR237_crm_sub_app_non_col_cre_der(BigDecimal r237_crm_sub_app_non_col_cre_der) {
		this.r237_crm_sub_app_non_col_cre_der = r237_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR237_crm_sub_app_col_elig_cash() {
		return r237_crm_sub_app_col_elig_cash;
	}
	public void setR237_crm_sub_app_col_elig_cash(BigDecimal r237_crm_sub_app_col_elig_cash) {
		this.r237_crm_sub_app_col_elig_cash = r237_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR237_crm_sub_app_col_elig_tre_bills() {
		return r237_crm_sub_app_col_elig_tre_bills;
	}
	public void setR237_crm_sub_app_col_elig_tre_bills(BigDecimal r237_crm_sub_app_col_elig_tre_bills) {
		this.r237_crm_sub_app_col_elig_tre_bills = r237_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR237_crm_sub_app_col_elig_deb_sec() {
		return r237_crm_sub_app_col_elig_deb_sec;
	}
	public void setR237_crm_sub_app_col_elig_deb_sec(BigDecimal r237_crm_sub_app_col_elig_deb_sec) {
		this.r237_crm_sub_app_col_elig_deb_sec = r237_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR237_crm_sub_app_col_elig_euiq() {
		return r237_crm_sub_app_col_elig_euiq;
	}
	public void setR237_crm_sub_app_col_elig_euiq(BigDecimal r237_crm_sub_app_col_elig_euiq) {
		this.r237_crm_sub_app_col_elig_euiq = r237_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR237_crm_sub_app_col_elig_uni_tru() {
		return r237_crm_sub_app_col_elig_uni_tru;
	}
	public void setR237_crm_sub_app_col_elig_uni_tru(BigDecimal r237_crm_sub_app_col_elig_uni_tru) {
		this.r237_crm_sub_app_col_elig_uni_tru = r237_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR237_crm_sub_app_col_cea_cov() {
		return r237_crm_sub_app_col_cea_cov;
	}
	public void setR237_crm_sub_app_col_cea_cov(BigDecimal r237_crm_sub_app_col_cea_cov) {
		this.r237_crm_sub_app_col_cea_cov = r237_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR237_crm_sub_app_col_cea_not_cov() {
		return r237_crm_sub_app_col_cea_not_cov;
	}
	public void setR237_crm_sub_app_col_cea_not_cov(BigDecimal r237_crm_sub_app_col_cea_not_cov) {
		this.r237_crm_sub_app_col_cea_not_cov = r237_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR237_crm_sub_app_rwa_ris_wei_crm() {
		return r237_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR237_crm_sub_app_rwa_ris_wei_crm(BigDecimal r237_crm_sub_app_rwa_ris_wei_crm) {
		this.r237_crm_sub_app_rwa_ris_wei_crm = r237_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR237_crm_sub_app_rwa_ris_cea_cov() {
		return r237_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR237_crm_sub_app_rwa_ris_cea_cov(BigDecimal r237_crm_sub_app_rwa_ris_cea_cov) {
		this.r237_crm_sub_app_rwa_ris_cea_cov = r237_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR237_crm_sub_app_rwa_appl_org_coun() {
		return r237_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR237_crm_sub_app_rwa_appl_org_coun(BigDecimal r237_crm_sub_app_rwa_appl_org_coun) {
		this.r237_crm_sub_app_rwa_appl_org_coun = r237_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR237_crm_sub_app_rwa_ris_cea_not_cov() {
		return r237_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR237_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r237_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r237_crm_sub_app_rwa_ris_cea_not_cov = r237_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR237_crm_com_app_col_cea_elig_crm() {
		return r237_crm_com_app_col_cea_elig_crm;
	}
	public void setR237_crm_com_app_col_cea_elig_crm(BigDecimal r237_crm_com_app_col_cea_elig_crm) {
		this.r237_crm_com_app_col_cea_elig_crm = r237_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR237_crm_com_app_col_elig_cea_vol_adj() {
		return r237_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR237_crm_com_app_col_elig_cea_vol_adj(BigDecimal r237_crm_com_app_col_elig_cea_vol_adj) {
		this.r237_crm_com_app_col_elig_cea_vol_adj = r237_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR237_crm_com_app_col_elig_fin_hai() {
		return r237_crm_com_app_col_elig_fin_hai;
	}
	public void setR237_crm_com_app_col_elig_fin_hai(BigDecimal r237_crm_com_app_col_elig_fin_hai) {
		this.r237_crm_com_app_col_elig_fin_hai = r237_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR237_crm_com_app_col_cea_val_aft_crm() {
		return r237_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR237_crm_com_app_col_cea_val_aft_crm(BigDecimal r237_crm_com_app_col_cea_val_aft_crm) {
		this.r237_crm_com_app_col_cea_val_aft_crm = r237_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR237_rwa_elig_cea_not_cov() {
		return r237_rwa_elig_cea_not_cov;
	}
	public void setR237_rwa_elig_cea_not_cov(BigDecimal r237_rwa_elig_cea_not_cov) {
		this.r237_rwa_elig_cea_not_cov = r237_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR237_rwa_unsec_cea_sub_cre_ris() {
		return r237_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR237_rwa_unsec_cea_sub_cre_ris(BigDecimal r237_rwa_unsec_cea_sub_cre_ris) {
		this.r237_rwa_unsec_cea_sub_cre_ris = r237_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR237_rwa_unsec_cea() {
		return r237_rwa_unsec_cea;
	}
	public void setR237_rwa_unsec_cea(BigDecimal r237_rwa_unsec_cea) {
		this.r237_rwa_unsec_cea = r237_rwa_unsec_cea;
	}
	public BigDecimal getR237_rwa_tot_ris_wei_ass() {
		return r237_rwa_tot_ris_wei_ass;
	}
	public void setR237_rwa_tot_ris_wei_ass(BigDecimal r237_rwa_tot_ris_wei_ass) {
		this.r237_rwa_tot_ris_wei_ass = r237_rwa_tot_ris_wei_ass;
	}
	public String getR238_exposure_class_off_bal() {
		return r238_exposure_class_off_bal;
	}
	public void setR238_exposure_class_off_bal(String r238_exposure_class_off_bal) {
		this.r238_exposure_class_off_bal = r238_exposure_class_off_bal;
	}
	public BigDecimal getR238_nom_pri_amt() {
		return r238_nom_pri_amt;
	}
	public void setR238_nom_pri_amt(BigDecimal r238_nom_pri_amt) {
		this.r238_nom_pri_amt = r238_nom_pri_amt;
	}
	public BigDecimal getR238_ccf() {
		return r238_ccf;
	}
	public void setR238_ccf(BigDecimal r238_ccf) {
		this.r238_ccf = r238_ccf;
	}
	public BigDecimal getR238_cea() {
		return r238_cea;
	}
	public void setR238_cea(BigDecimal r238_cea) {
		this.r238_cea = r238_cea;
	}
	public BigDecimal getR238_cea_elig_coun_bilt_net() {
		return r238_cea_elig_coun_bilt_net;
	}
	public void setR238_cea_elig_coun_bilt_net(BigDecimal r238_cea_elig_coun_bilt_net) {
		this.r238_cea_elig_coun_bilt_net = r238_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR238_cea_aft_net() {
		return r238_cea_aft_net;
	}
	public void setR238_cea_aft_net(BigDecimal r238_cea_aft_net) {
		this.r238_cea_aft_net = r238_cea_aft_net;
	}
	public BigDecimal getR238_crm_sub_app_cea_elig() {
		return r238_crm_sub_app_cea_elig;
	}
	public void setR238_crm_sub_app_cea_elig(BigDecimal r238_crm_sub_app_cea_elig) {
		this.r238_crm_sub_app_cea_elig = r238_crm_sub_app_cea_elig;
	}
	public BigDecimal getR238_crm_sub_app_non_col_guar_elig() {
		return r238_crm_sub_app_non_col_guar_elig;
	}
	public void setR238_crm_sub_app_non_col_guar_elig(BigDecimal r238_crm_sub_app_non_col_guar_elig) {
		this.r238_crm_sub_app_non_col_guar_elig = r238_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR238_crm_sub_app_non_col_cre_der() {
		return r238_crm_sub_app_non_col_cre_der;
	}
	public void setR238_crm_sub_app_non_col_cre_der(BigDecimal r238_crm_sub_app_non_col_cre_der) {
		this.r238_crm_sub_app_non_col_cre_der = r238_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR238_crm_sub_app_col_elig_cash() {
		return r238_crm_sub_app_col_elig_cash;
	}
	public void setR238_crm_sub_app_col_elig_cash(BigDecimal r238_crm_sub_app_col_elig_cash) {
		this.r238_crm_sub_app_col_elig_cash = r238_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR238_crm_sub_app_col_elig_tre_bills() {
		return r238_crm_sub_app_col_elig_tre_bills;
	}
	public void setR238_crm_sub_app_col_elig_tre_bills(BigDecimal r238_crm_sub_app_col_elig_tre_bills) {
		this.r238_crm_sub_app_col_elig_tre_bills = r238_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR238_crm_sub_app_col_elig_deb_sec() {
		return r238_crm_sub_app_col_elig_deb_sec;
	}
	public void setR238_crm_sub_app_col_elig_deb_sec(BigDecimal r238_crm_sub_app_col_elig_deb_sec) {
		this.r238_crm_sub_app_col_elig_deb_sec = r238_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR238_crm_sub_app_col_elig_euiq() {
		return r238_crm_sub_app_col_elig_euiq;
	}
	public void setR238_crm_sub_app_col_elig_euiq(BigDecimal r238_crm_sub_app_col_elig_euiq) {
		this.r238_crm_sub_app_col_elig_euiq = r238_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR238_crm_sub_app_col_elig_uni_tru() {
		return r238_crm_sub_app_col_elig_uni_tru;
	}
	public void setR238_crm_sub_app_col_elig_uni_tru(BigDecimal r238_crm_sub_app_col_elig_uni_tru) {
		this.r238_crm_sub_app_col_elig_uni_tru = r238_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR238_crm_sub_app_col_cea_cov() {
		return r238_crm_sub_app_col_cea_cov;
	}
	public void setR238_crm_sub_app_col_cea_cov(BigDecimal r238_crm_sub_app_col_cea_cov) {
		this.r238_crm_sub_app_col_cea_cov = r238_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR238_crm_sub_app_col_cea_not_cov() {
		return r238_crm_sub_app_col_cea_not_cov;
	}
	public void setR238_crm_sub_app_col_cea_not_cov(BigDecimal r238_crm_sub_app_col_cea_not_cov) {
		this.r238_crm_sub_app_col_cea_not_cov = r238_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR238_crm_sub_app_rwa_ris_wei_crm() {
		return r238_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR238_crm_sub_app_rwa_ris_wei_crm(BigDecimal r238_crm_sub_app_rwa_ris_wei_crm) {
		this.r238_crm_sub_app_rwa_ris_wei_crm = r238_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR238_crm_sub_app_rwa_ris_cea_cov() {
		return r238_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR238_crm_sub_app_rwa_ris_cea_cov(BigDecimal r238_crm_sub_app_rwa_ris_cea_cov) {
		this.r238_crm_sub_app_rwa_ris_cea_cov = r238_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR238_crm_sub_app_rwa_appl_org_coun() {
		return r238_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR238_crm_sub_app_rwa_appl_org_coun(BigDecimal r238_crm_sub_app_rwa_appl_org_coun) {
		this.r238_crm_sub_app_rwa_appl_org_coun = r238_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR238_crm_sub_app_rwa_ris_cea_not_cov() {
		return r238_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR238_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r238_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r238_crm_sub_app_rwa_ris_cea_not_cov = r238_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR238_crm_com_app_col_cea_elig_crm() {
		return r238_crm_com_app_col_cea_elig_crm;
	}
	public void setR238_crm_com_app_col_cea_elig_crm(BigDecimal r238_crm_com_app_col_cea_elig_crm) {
		this.r238_crm_com_app_col_cea_elig_crm = r238_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR238_crm_com_app_col_elig_cea_vol_adj() {
		return r238_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR238_crm_com_app_col_elig_cea_vol_adj(BigDecimal r238_crm_com_app_col_elig_cea_vol_adj) {
		this.r238_crm_com_app_col_elig_cea_vol_adj = r238_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR238_crm_com_app_col_elig_fin_hai() {
		return r238_crm_com_app_col_elig_fin_hai;
	}
	public void setR238_crm_com_app_col_elig_fin_hai(BigDecimal r238_crm_com_app_col_elig_fin_hai) {
		this.r238_crm_com_app_col_elig_fin_hai = r238_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR238_crm_com_app_col_cea_val_aft_crm() {
		return r238_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR238_crm_com_app_col_cea_val_aft_crm(BigDecimal r238_crm_com_app_col_cea_val_aft_crm) {
		this.r238_crm_com_app_col_cea_val_aft_crm = r238_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR238_rwa_elig_cea_not_cov() {
		return r238_rwa_elig_cea_not_cov;
	}
	public void setR238_rwa_elig_cea_not_cov(BigDecimal r238_rwa_elig_cea_not_cov) {
		this.r238_rwa_elig_cea_not_cov = r238_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR238_rwa_unsec_cea_sub_cre_ris() {
		return r238_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR238_rwa_unsec_cea_sub_cre_ris(BigDecimal r238_rwa_unsec_cea_sub_cre_ris) {
		this.r238_rwa_unsec_cea_sub_cre_ris = r238_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR238_rwa_unsec_cea() {
		return r238_rwa_unsec_cea;
	}
	public void setR238_rwa_unsec_cea(BigDecimal r238_rwa_unsec_cea) {
		this.r238_rwa_unsec_cea = r238_rwa_unsec_cea;
	}
	public BigDecimal getR238_rwa_tot_ris_wei_ass() {
		return r238_rwa_tot_ris_wei_ass;
	}
	public void setR238_rwa_tot_ris_wei_ass(BigDecimal r238_rwa_tot_ris_wei_ass) {
		this.r238_rwa_tot_ris_wei_ass = r238_rwa_tot_ris_wei_ass;
	}
	public String getR239_exposure_class_off_bal() {
		return r239_exposure_class_off_bal;
	}
	public void setR239_exposure_class_off_bal(String r239_exposure_class_off_bal) {
		this.r239_exposure_class_off_bal = r239_exposure_class_off_bal;
	}
	public BigDecimal getR239_nom_pri_amt() {
		return r239_nom_pri_amt;
	}
	public void setR239_nom_pri_amt(BigDecimal r239_nom_pri_amt) {
		this.r239_nom_pri_amt = r239_nom_pri_amt;
	}
	public BigDecimal getR239_ccf() {
		return r239_ccf;
	}
	public void setR239_ccf(BigDecimal r239_ccf) {
		this.r239_ccf = r239_ccf;
	}
	public BigDecimal getR239_cea() {
		return r239_cea;
	}
	public void setR239_cea(BigDecimal r239_cea) {
		this.r239_cea = r239_cea;
	}
	public BigDecimal getR239_cea_elig_coun_bilt_net() {
		return r239_cea_elig_coun_bilt_net;
	}
	public void setR239_cea_elig_coun_bilt_net(BigDecimal r239_cea_elig_coun_bilt_net) {
		this.r239_cea_elig_coun_bilt_net = r239_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR239_cea_aft_net() {
		return r239_cea_aft_net;
	}
	public void setR239_cea_aft_net(BigDecimal r239_cea_aft_net) {
		this.r239_cea_aft_net = r239_cea_aft_net;
	}
	public BigDecimal getR239_crm_sub_app_cea_elig() {
		return r239_crm_sub_app_cea_elig;
	}
	public void setR239_crm_sub_app_cea_elig(BigDecimal r239_crm_sub_app_cea_elig) {
		this.r239_crm_sub_app_cea_elig = r239_crm_sub_app_cea_elig;
	}
	public BigDecimal getR239_crm_sub_app_non_col_guar_elig() {
		return r239_crm_sub_app_non_col_guar_elig;
	}
	public void setR239_crm_sub_app_non_col_guar_elig(BigDecimal r239_crm_sub_app_non_col_guar_elig) {
		this.r239_crm_sub_app_non_col_guar_elig = r239_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR239_crm_sub_app_non_col_cre_der() {
		return r239_crm_sub_app_non_col_cre_der;
	}
	public void setR239_crm_sub_app_non_col_cre_der(BigDecimal r239_crm_sub_app_non_col_cre_der) {
		this.r239_crm_sub_app_non_col_cre_der = r239_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR239_crm_sub_app_col_elig_cash() {
		return r239_crm_sub_app_col_elig_cash;
	}
	public void setR239_crm_sub_app_col_elig_cash(BigDecimal r239_crm_sub_app_col_elig_cash) {
		this.r239_crm_sub_app_col_elig_cash = r239_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR239_crm_sub_app_col_elig_tre_bills() {
		return r239_crm_sub_app_col_elig_tre_bills;
	}
	public void setR239_crm_sub_app_col_elig_tre_bills(BigDecimal r239_crm_sub_app_col_elig_tre_bills) {
		this.r239_crm_sub_app_col_elig_tre_bills = r239_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR239_crm_sub_app_col_elig_deb_sec() {
		return r239_crm_sub_app_col_elig_deb_sec;
	}
	public void setR239_crm_sub_app_col_elig_deb_sec(BigDecimal r239_crm_sub_app_col_elig_deb_sec) {
		this.r239_crm_sub_app_col_elig_deb_sec = r239_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR239_crm_sub_app_col_elig_euiq() {
		return r239_crm_sub_app_col_elig_euiq;
	}
	public void setR239_crm_sub_app_col_elig_euiq(BigDecimal r239_crm_sub_app_col_elig_euiq) {
		this.r239_crm_sub_app_col_elig_euiq = r239_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR239_crm_sub_app_col_elig_uni_tru() {
		return r239_crm_sub_app_col_elig_uni_tru;
	}
	public void setR239_crm_sub_app_col_elig_uni_tru(BigDecimal r239_crm_sub_app_col_elig_uni_tru) {
		this.r239_crm_sub_app_col_elig_uni_tru = r239_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR239_crm_sub_app_col_cea_cov() {
		return r239_crm_sub_app_col_cea_cov;
	}
	public void setR239_crm_sub_app_col_cea_cov(BigDecimal r239_crm_sub_app_col_cea_cov) {
		this.r239_crm_sub_app_col_cea_cov = r239_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR239_crm_sub_app_col_cea_not_cov() {
		return r239_crm_sub_app_col_cea_not_cov;
	}
	public void setR239_crm_sub_app_col_cea_not_cov(BigDecimal r239_crm_sub_app_col_cea_not_cov) {
		this.r239_crm_sub_app_col_cea_not_cov = r239_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR239_crm_sub_app_rwa_ris_wei_crm() {
		return r239_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR239_crm_sub_app_rwa_ris_wei_crm(BigDecimal r239_crm_sub_app_rwa_ris_wei_crm) {
		this.r239_crm_sub_app_rwa_ris_wei_crm = r239_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR239_crm_sub_app_rwa_ris_cea_cov() {
		return r239_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR239_crm_sub_app_rwa_ris_cea_cov(BigDecimal r239_crm_sub_app_rwa_ris_cea_cov) {
		this.r239_crm_sub_app_rwa_ris_cea_cov = r239_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR239_crm_sub_app_rwa_appl_org_coun() {
		return r239_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR239_crm_sub_app_rwa_appl_org_coun(BigDecimal r239_crm_sub_app_rwa_appl_org_coun) {
		this.r239_crm_sub_app_rwa_appl_org_coun = r239_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR239_crm_sub_app_rwa_ris_cea_not_cov() {
		return r239_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR239_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r239_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r239_crm_sub_app_rwa_ris_cea_not_cov = r239_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR239_crm_com_app_col_cea_elig_crm() {
		return r239_crm_com_app_col_cea_elig_crm;
	}
	public void setR239_crm_com_app_col_cea_elig_crm(BigDecimal r239_crm_com_app_col_cea_elig_crm) {
		this.r239_crm_com_app_col_cea_elig_crm = r239_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR239_crm_com_app_col_elig_cea_vol_adj() {
		return r239_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR239_crm_com_app_col_elig_cea_vol_adj(BigDecimal r239_crm_com_app_col_elig_cea_vol_adj) {
		this.r239_crm_com_app_col_elig_cea_vol_adj = r239_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR239_crm_com_app_col_elig_fin_hai() {
		return r239_crm_com_app_col_elig_fin_hai;
	}
	public void setR239_crm_com_app_col_elig_fin_hai(BigDecimal r239_crm_com_app_col_elig_fin_hai) {
		this.r239_crm_com_app_col_elig_fin_hai = r239_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR239_crm_com_app_col_cea_val_aft_crm() {
		return r239_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR239_crm_com_app_col_cea_val_aft_crm(BigDecimal r239_crm_com_app_col_cea_val_aft_crm) {
		this.r239_crm_com_app_col_cea_val_aft_crm = r239_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR239_rwa_elig_cea_not_cov() {
		return r239_rwa_elig_cea_not_cov;
	}
	public void setR239_rwa_elig_cea_not_cov(BigDecimal r239_rwa_elig_cea_not_cov) {
		this.r239_rwa_elig_cea_not_cov = r239_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR239_rwa_unsec_cea_sub_cre_ris() {
		return r239_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR239_rwa_unsec_cea_sub_cre_ris(BigDecimal r239_rwa_unsec_cea_sub_cre_ris) {
		this.r239_rwa_unsec_cea_sub_cre_ris = r239_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR239_rwa_unsec_cea() {
		return r239_rwa_unsec_cea;
	}
	public void setR239_rwa_unsec_cea(BigDecimal r239_rwa_unsec_cea) {
		this.r239_rwa_unsec_cea = r239_rwa_unsec_cea;
	}
	public BigDecimal getR239_rwa_tot_ris_wei_ass() {
		return r239_rwa_tot_ris_wei_ass;
	}
	public void setR239_rwa_tot_ris_wei_ass(BigDecimal r239_rwa_tot_ris_wei_ass) {
		this.r239_rwa_tot_ris_wei_ass = r239_rwa_tot_ris_wei_ass;
	}
	public String getR240_exposure_class_off_bal() {
		return r240_exposure_class_off_bal;
	}
	public void setR240_exposure_class_off_bal(String r240_exposure_class_off_bal) {
		this.r240_exposure_class_off_bal = r240_exposure_class_off_bal;
	}
	public BigDecimal getR240_nom_pri_amt() {
		return r240_nom_pri_amt;
	}
	public void setR240_nom_pri_amt(BigDecimal r240_nom_pri_amt) {
		this.r240_nom_pri_amt = r240_nom_pri_amt;
	}
	public BigDecimal getR240_ccf() {
		return r240_ccf;
	}
	public void setR240_ccf(BigDecimal r240_ccf) {
		this.r240_ccf = r240_ccf;
	}
	public BigDecimal getR240_cea() {
		return r240_cea;
	}
	public void setR240_cea(BigDecimal r240_cea) {
		this.r240_cea = r240_cea;
	}
	public BigDecimal getR240_cea_elig_coun_bilt_net() {
		return r240_cea_elig_coun_bilt_net;
	}
	public void setR240_cea_elig_coun_bilt_net(BigDecimal r240_cea_elig_coun_bilt_net) {
		this.r240_cea_elig_coun_bilt_net = r240_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR240_cea_aft_net() {
		return r240_cea_aft_net;
	}
	public void setR240_cea_aft_net(BigDecimal r240_cea_aft_net) {
		this.r240_cea_aft_net = r240_cea_aft_net;
	}
	public BigDecimal getR240_crm_sub_app_cea_elig() {
		return r240_crm_sub_app_cea_elig;
	}
	public void setR240_crm_sub_app_cea_elig(BigDecimal r240_crm_sub_app_cea_elig) {
		this.r240_crm_sub_app_cea_elig = r240_crm_sub_app_cea_elig;
	}
	public BigDecimal getR240_crm_sub_app_non_col_guar_elig() {
		return r240_crm_sub_app_non_col_guar_elig;
	}
	public void setR240_crm_sub_app_non_col_guar_elig(BigDecimal r240_crm_sub_app_non_col_guar_elig) {
		this.r240_crm_sub_app_non_col_guar_elig = r240_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR240_crm_sub_app_non_col_cre_der() {
		return r240_crm_sub_app_non_col_cre_der;
	}
	public void setR240_crm_sub_app_non_col_cre_der(BigDecimal r240_crm_sub_app_non_col_cre_der) {
		this.r240_crm_sub_app_non_col_cre_der = r240_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR240_crm_sub_app_col_elig_cash() {
		return r240_crm_sub_app_col_elig_cash;
	}
	public void setR240_crm_sub_app_col_elig_cash(BigDecimal r240_crm_sub_app_col_elig_cash) {
		this.r240_crm_sub_app_col_elig_cash = r240_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR240_crm_sub_app_col_elig_tre_bills() {
		return r240_crm_sub_app_col_elig_tre_bills;
	}
	public void setR240_crm_sub_app_col_elig_tre_bills(BigDecimal r240_crm_sub_app_col_elig_tre_bills) {
		this.r240_crm_sub_app_col_elig_tre_bills = r240_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR240_crm_sub_app_col_elig_deb_sec() {
		return r240_crm_sub_app_col_elig_deb_sec;
	}
	public void setR240_crm_sub_app_col_elig_deb_sec(BigDecimal r240_crm_sub_app_col_elig_deb_sec) {
		this.r240_crm_sub_app_col_elig_deb_sec = r240_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR240_crm_sub_app_col_elig_euiq() {
		return r240_crm_sub_app_col_elig_euiq;
	}
	public void setR240_crm_sub_app_col_elig_euiq(BigDecimal r240_crm_sub_app_col_elig_euiq) {
		this.r240_crm_sub_app_col_elig_euiq = r240_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR240_crm_sub_app_col_elig_uni_tru() {
		return r240_crm_sub_app_col_elig_uni_tru;
	}
	public void setR240_crm_sub_app_col_elig_uni_tru(BigDecimal r240_crm_sub_app_col_elig_uni_tru) {
		this.r240_crm_sub_app_col_elig_uni_tru = r240_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR240_crm_sub_app_col_cea_cov() {
		return r240_crm_sub_app_col_cea_cov;
	}
	public void setR240_crm_sub_app_col_cea_cov(BigDecimal r240_crm_sub_app_col_cea_cov) {
		this.r240_crm_sub_app_col_cea_cov = r240_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR240_crm_sub_app_col_cea_not_cov() {
		return r240_crm_sub_app_col_cea_not_cov;
	}
	public void setR240_crm_sub_app_col_cea_not_cov(BigDecimal r240_crm_sub_app_col_cea_not_cov) {
		this.r240_crm_sub_app_col_cea_not_cov = r240_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR240_crm_sub_app_rwa_ris_wei_crm() {
		return r240_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR240_crm_sub_app_rwa_ris_wei_crm(BigDecimal r240_crm_sub_app_rwa_ris_wei_crm) {
		this.r240_crm_sub_app_rwa_ris_wei_crm = r240_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR240_crm_sub_app_rwa_ris_cea_cov() {
		return r240_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR240_crm_sub_app_rwa_ris_cea_cov(BigDecimal r240_crm_sub_app_rwa_ris_cea_cov) {
		this.r240_crm_sub_app_rwa_ris_cea_cov = r240_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR240_crm_sub_app_rwa_appl_org_coun() {
		return r240_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR240_crm_sub_app_rwa_appl_org_coun(BigDecimal r240_crm_sub_app_rwa_appl_org_coun) {
		this.r240_crm_sub_app_rwa_appl_org_coun = r240_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR240_crm_sub_app_rwa_ris_cea_not_cov() {
		return r240_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR240_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r240_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r240_crm_sub_app_rwa_ris_cea_not_cov = r240_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR240_crm_com_app_col_cea_elig_crm() {
		return r240_crm_com_app_col_cea_elig_crm;
	}
	public void setR240_crm_com_app_col_cea_elig_crm(BigDecimal r240_crm_com_app_col_cea_elig_crm) {
		this.r240_crm_com_app_col_cea_elig_crm = r240_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR240_crm_com_app_col_elig_cea_vol_adj() {
		return r240_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR240_crm_com_app_col_elig_cea_vol_adj(BigDecimal r240_crm_com_app_col_elig_cea_vol_adj) {
		this.r240_crm_com_app_col_elig_cea_vol_adj = r240_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR240_crm_com_app_col_elig_fin_hai() {
		return r240_crm_com_app_col_elig_fin_hai;
	}
	public void setR240_crm_com_app_col_elig_fin_hai(BigDecimal r240_crm_com_app_col_elig_fin_hai) {
		this.r240_crm_com_app_col_elig_fin_hai = r240_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR240_crm_com_app_col_cea_val_aft_crm() {
		return r240_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR240_crm_com_app_col_cea_val_aft_crm(BigDecimal r240_crm_com_app_col_cea_val_aft_crm) {
		this.r240_crm_com_app_col_cea_val_aft_crm = r240_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR240_rwa_elig_cea_not_cov() {
		return r240_rwa_elig_cea_not_cov;
	}
	public void setR240_rwa_elig_cea_not_cov(BigDecimal r240_rwa_elig_cea_not_cov) {
		this.r240_rwa_elig_cea_not_cov = r240_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR240_rwa_unsec_cea_sub_cre_ris() {
		return r240_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR240_rwa_unsec_cea_sub_cre_ris(BigDecimal r240_rwa_unsec_cea_sub_cre_ris) {
		this.r240_rwa_unsec_cea_sub_cre_ris = r240_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR240_rwa_unsec_cea() {
		return r240_rwa_unsec_cea;
	}
	public void setR240_rwa_unsec_cea(BigDecimal r240_rwa_unsec_cea) {
		this.r240_rwa_unsec_cea = r240_rwa_unsec_cea;
	}
	public BigDecimal getR240_rwa_tot_ris_wei_ass() {
		return r240_rwa_tot_ris_wei_ass;
	}
	public void setR240_rwa_tot_ris_wei_ass(BigDecimal r240_rwa_tot_ris_wei_ass) {
		this.r240_rwa_tot_ris_wei_ass = r240_rwa_tot_ris_wei_ass;
	}
	public String getR241_exposure_class_off_bal() {
		return r241_exposure_class_off_bal;
	}
	public void setR241_exposure_class_off_bal(String r241_exposure_class_off_bal) {
		this.r241_exposure_class_off_bal = r241_exposure_class_off_bal;
	}
	public BigDecimal getR241_nom_pri_amt() {
		return r241_nom_pri_amt;
	}
	public void setR241_nom_pri_amt(BigDecimal r241_nom_pri_amt) {
		this.r241_nom_pri_amt = r241_nom_pri_amt;
	}
	public BigDecimal getR241_ccf() {
		return r241_ccf;
	}
	public void setR241_ccf(BigDecimal r241_ccf) {
		this.r241_ccf = r241_ccf;
	}
	public BigDecimal getR241_cea() {
		return r241_cea;
	}
	public void setR241_cea(BigDecimal r241_cea) {
		this.r241_cea = r241_cea;
	}
	public BigDecimal getR241_cea_elig_coun_bilt_net() {
		return r241_cea_elig_coun_bilt_net;
	}
	public void setR241_cea_elig_coun_bilt_net(BigDecimal r241_cea_elig_coun_bilt_net) {
		this.r241_cea_elig_coun_bilt_net = r241_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR241_cea_aft_net() {
		return r241_cea_aft_net;
	}
	public void setR241_cea_aft_net(BigDecimal r241_cea_aft_net) {
		this.r241_cea_aft_net = r241_cea_aft_net;
	}
	public BigDecimal getR241_crm_sub_app_cea_elig() {
		return r241_crm_sub_app_cea_elig;
	}
	public void setR241_crm_sub_app_cea_elig(BigDecimal r241_crm_sub_app_cea_elig) {
		this.r241_crm_sub_app_cea_elig = r241_crm_sub_app_cea_elig;
	}
	public BigDecimal getR241_crm_sub_app_non_col_guar_elig() {
		return r241_crm_sub_app_non_col_guar_elig;
	}
	public void setR241_crm_sub_app_non_col_guar_elig(BigDecimal r241_crm_sub_app_non_col_guar_elig) {
		this.r241_crm_sub_app_non_col_guar_elig = r241_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR241_crm_sub_app_non_col_cre_der() {
		return r241_crm_sub_app_non_col_cre_der;
	}
	public void setR241_crm_sub_app_non_col_cre_der(BigDecimal r241_crm_sub_app_non_col_cre_der) {
		this.r241_crm_sub_app_non_col_cre_der = r241_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR241_crm_sub_app_col_elig_cash() {
		return r241_crm_sub_app_col_elig_cash;
	}
	public void setR241_crm_sub_app_col_elig_cash(BigDecimal r241_crm_sub_app_col_elig_cash) {
		this.r241_crm_sub_app_col_elig_cash = r241_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR241_crm_sub_app_col_elig_tre_bills() {
		return r241_crm_sub_app_col_elig_tre_bills;
	}
	public void setR241_crm_sub_app_col_elig_tre_bills(BigDecimal r241_crm_sub_app_col_elig_tre_bills) {
		this.r241_crm_sub_app_col_elig_tre_bills = r241_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR241_crm_sub_app_col_elig_deb_sec() {
		return r241_crm_sub_app_col_elig_deb_sec;
	}
	public void setR241_crm_sub_app_col_elig_deb_sec(BigDecimal r241_crm_sub_app_col_elig_deb_sec) {
		this.r241_crm_sub_app_col_elig_deb_sec = r241_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR241_crm_sub_app_col_elig_euiq() {
		return r241_crm_sub_app_col_elig_euiq;
	}
	public void setR241_crm_sub_app_col_elig_euiq(BigDecimal r241_crm_sub_app_col_elig_euiq) {
		this.r241_crm_sub_app_col_elig_euiq = r241_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR241_crm_sub_app_col_elig_uni_tru() {
		return r241_crm_sub_app_col_elig_uni_tru;
	}
	public void setR241_crm_sub_app_col_elig_uni_tru(BigDecimal r241_crm_sub_app_col_elig_uni_tru) {
		this.r241_crm_sub_app_col_elig_uni_tru = r241_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR241_crm_sub_app_col_cea_cov() {
		return r241_crm_sub_app_col_cea_cov;
	}
	public void setR241_crm_sub_app_col_cea_cov(BigDecimal r241_crm_sub_app_col_cea_cov) {
		this.r241_crm_sub_app_col_cea_cov = r241_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR241_crm_sub_app_col_cea_not_cov() {
		return r241_crm_sub_app_col_cea_not_cov;
	}
	public void setR241_crm_sub_app_col_cea_not_cov(BigDecimal r241_crm_sub_app_col_cea_not_cov) {
		this.r241_crm_sub_app_col_cea_not_cov = r241_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR241_crm_sub_app_rwa_ris_wei_crm() {
		return r241_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR241_crm_sub_app_rwa_ris_wei_crm(BigDecimal r241_crm_sub_app_rwa_ris_wei_crm) {
		this.r241_crm_sub_app_rwa_ris_wei_crm = r241_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR241_crm_sub_app_rwa_ris_cea_cov() {
		return r241_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR241_crm_sub_app_rwa_ris_cea_cov(BigDecimal r241_crm_sub_app_rwa_ris_cea_cov) {
		this.r241_crm_sub_app_rwa_ris_cea_cov = r241_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR241_crm_sub_app_rwa_appl_org_coun() {
		return r241_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR241_crm_sub_app_rwa_appl_org_coun(BigDecimal r241_crm_sub_app_rwa_appl_org_coun) {
		this.r241_crm_sub_app_rwa_appl_org_coun = r241_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR241_crm_sub_app_rwa_ris_cea_not_cov() {
		return r241_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR241_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r241_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r241_crm_sub_app_rwa_ris_cea_not_cov = r241_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR241_crm_com_app_col_cea_elig_crm() {
		return r241_crm_com_app_col_cea_elig_crm;
	}
	public void setR241_crm_com_app_col_cea_elig_crm(BigDecimal r241_crm_com_app_col_cea_elig_crm) {
		this.r241_crm_com_app_col_cea_elig_crm = r241_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR241_crm_com_app_col_elig_cea_vol_adj() {
		return r241_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR241_crm_com_app_col_elig_cea_vol_adj(BigDecimal r241_crm_com_app_col_elig_cea_vol_adj) {
		this.r241_crm_com_app_col_elig_cea_vol_adj = r241_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR241_crm_com_app_col_elig_fin_hai() {
		return r241_crm_com_app_col_elig_fin_hai;
	}
	public void setR241_crm_com_app_col_elig_fin_hai(BigDecimal r241_crm_com_app_col_elig_fin_hai) {
		this.r241_crm_com_app_col_elig_fin_hai = r241_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR241_crm_com_app_col_cea_val_aft_crm() {
		return r241_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR241_crm_com_app_col_cea_val_aft_crm(BigDecimal r241_crm_com_app_col_cea_val_aft_crm) {
		this.r241_crm_com_app_col_cea_val_aft_crm = r241_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR241_rwa_elig_cea_not_cov() {
		return r241_rwa_elig_cea_not_cov;
	}
	public void setR241_rwa_elig_cea_not_cov(BigDecimal r241_rwa_elig_cea_not_cov) {
		this.r241_rwa_elig_cea_not_cov = r241_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR241_rwa_unsec_cea_sub_cre_ris() {
		return r241_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR241_rwa_unsec_cea_sub_cre_ris(BigDecimal r241_rwa_unsec_cea_sub_cre_ris) {
		this.r241_rwa_unsec_cea_sub_cre_ris = r241_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR241_rwa_unsec_cea() {
		return r241_rwa_unsec_cea;
	}
	public void setR241_rwa_unsec_cea(BigDecimal r241_rwa_unsec_cea) {
		this.r241_rwa_unsec_cea = r241_rwa_unsec_cea;
	}
	public BigDecimal getR241_rwa_tot_ris_wei_ass() {
		return r241_rwa_tot_ris_wei_ass;
	}
	public void setR241_rwa_tot_ris_wei_ass(BigDecimal r241_rwa_tot_ris_wei_ass) {
		this.r241_rwa_tot_ris_wei_ass = r241_rwa_tot_ris_wei_ass;
	}
	public String getR242_exposure_class_off_bal() {
		return r242_exposure_class_off_bal;
	}
	public void setR242_exposure_class_off_bal(String r242_exposure_class_off_bal) {
		this.r242_exposure_class_off_bal = r242_exposure_class_off_bal;
	}
	public BigDecimal getR242_nom_pri_amt() {
		return r242_nom_pri_amt;
	}
	public void setR242_nom_pri_amt(BigDecimal r242_nom_pri_amt) {
		this.r242_nom_pri_amt = r242_nom_pri_amt;
	}
	public BigDecimal getR242_ccf() {
		return r242_ccf;
	}
	public void setR242_ccf(BigDecimal r242_ccf) {
		this.r242_ccf = r242_ccf;
	}
	public BigDecimal getR242_cea() {
		return r242_cea;
	}
	public void setR242_cea(BigDecimal r242_cea) {
		this.r242_cea = r242_cea;
	}
	public BigDecimal getR242_cea_elig_coun_bilt_net() {
		return r242_cea_elig_coun_bilt_net;
	}
	public void setR242_cea_elig_coun_bilt_net(BigDecimal r242_cea_elig_coun_bilt_net) {
		this.r242_cea_elig_coun_bilt_net = r242_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR242_cea_aft_net() {
		return r242_cea_aft_net;
	}
	public void setR242_cea_aft_net(BigDecimal r242_cea_aft_net) {
		this.r242_cea_aft_net = r242_cea_aft_net;
	}
	public BigDecimal getR242_crm_sub_app_cea_elig() {
		return r242_crm_sub_app_cea_elig;
	}
	public void setR242_crm_sub_app_cea_elig(BigDecimal r242_crm_sub_app_cea_elig) {
		this.r242_crm_sub_app_cea_elig = r242_crm_sub_app_cea_elig;
	}
	public BigDecimal getR242_crm_sub_app_non_col_guar_elig() {
		return r242_crm_sub_app_non_col_guar_elig;
	}
	public void setR242_crm_sub_app_non_col_guar_elig(BigDecimal r242_crm_sub_app_non_col_guar_elig) {
		this.r242_crm_sub_app_non_col_guar_elig = r242_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR242_crm_sub_app_non_col_cre_der() {
		return r242_crm_sub_app_non_col_cre_der;
	}
	public void setR242_crm_sub_app_non_col_cre_der(BigDecimal r242_crm_sub_app_non_col_cre_der) {
		this.r242_crm_sub_app_non_col_cre_der = r242_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR242_crm_sub_app_col_elig_cash() {
		return r242_crm_sub_app_col_elig_cash;
	}
	public void setR242_crm_sub_app_col_elig_cash(BigDecimal r242_crm_sub_app_col_elig_cash) {
		this.r242_crm_sub_app_col_elig_cash = r242_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR242_crm_sub_app_col_elig_tre_bills() {
		return r242_crm_sub_app_col_elig_tre_bills;
	}
	public void setR242_crm_sub_app_col_elig_tre_bills(BigDecimal r242_crm_sub_app_col_elig_tre_bills) {
		this.r242_crm_sub_app_col_elig_tre_bills = r242_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR242_crm_sub_app_col_elig_deb_sec() {
		return r242_crm_sub_app_col_elig_deb_sec;
	}
	public void setR242_crm_sub_app_col_elig_deb_sec(BigDecimal r242_crm_sub_app_col_elig_deb_sec) {
		this.r242_crm_sub_app_col_elig_deb_sec = r242_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR242_crm_sub_app_col_elig_euiq() {
		return r242_crm_sub_app_col_elig_euiq;
	}
	public void setR242_crm_sub_app_col_elig_euiq(BigDecimal r242_crm_sub_app_col_elig_euiq) {
		this.r242_crm_sub_app_col_elig_euiq = r242_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR242_crm_sub_app_col_elig_uni_tru() {
		return r242_crm_sub_app_col_elig_uni_tru;
	}
	public void setR242_crm_sub_app_col_elig_uni_tru(BigDecimal r242_crm_sub_app_col_elig_uni_tru) {
		this.r242_crm_sub_app_col_elig_uni_tru = r242_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR242_crm_sub_app_col_cea_cov() {
		return r242_crm_sub_app_col_cea_cov;
	}
	public void setR242_crm_sub_app_col_cea_cov(BigDecimal r242_crm_sub_app_col_cea_cov) {
		this.r242_crm_sub_app_col_cea_cov = r242_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR242_crm_sub_app_col_cea_not_cov() {
		return r242_crm_sub_app_col_cea_not_cov;
	}
	public void setR242_crm_sub_app_col_cea_not_cov(BigDecimal r242_crm_sub_app_col_cea_not_cov) {
		this.r242_crm_sub_app_col_cea_not_cov = r242_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR242_crm_sub_app_rwa_ris_wei_crm() {
		return r242_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR242_crm_sub_app_rwa_ris_wei_crm(BigDecimal r242_crm_sub_app_rwa_ris_wei_crm) {
		this.r242_crm_sub_app_rwa_ris_wei_crm = r242_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR242_crm_sub_app_rwa_ris_cea_cov() {
		return r242_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR242_crm_sub_app_rwa_ris_cea_cov(BigDecimal r242_crm_sub_app_rwa_ris_cea_cov) {
		this.r242_crm_sub_app_rwa_ris_cea_cov = r242_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR242_crm_sub_app_rwa_appl_org_coun() {
		return r242_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR242_crm_sub_app_rwa_appl_org_coun(BigDecimal r242_crm_sub_app_rwa_appl_org_coun) {
		this.r242_crm_sub_app_rwa_appl_org_coun = r242_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR242_crm_sub_app_rwa_ris_cea_not_cov() {
		return r242_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR242_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r242_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r242_crm_sub_app_rwa_ris_cea_not_cov = r242_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR242_crm_com_app_col_cea_elig_crm() {
		return r242_crm_com_app_col_cea_elig_crm;
	}
	public void setR242_crm_com_app_col_cea_elig_crm(BigDecimal r242_crm_com_app_col_cea_elig_crm) {
		this.r242_crm_com_app_col_cea_elig_crm = r242_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR242_crm_com_app_col_elig_cea_vol_adj() {
		return r242_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR242_crm_com_app_col_elig_cea_vol_adj(BigDecimal r242_crm_com_app_col_elig_cea_vol_adj) {
		this.r242_crm_com_app_col_elig_cea_vol_adj = r242_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR242_crm_com_app_col_elig_fin_hai() {
		return r242_crm_com_app_col_elig_fin_hai;
	}
	public void setR242_crm_com_app_col_elig_fin_hai(BigDecimal r242_crm_com_app_col_elig_fin_hai) {
		this.r242_crm_com_app_col_elig_fin_hai = r242_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR242_crm_com_app_col_cea_val_aft_crm() {
		return r242_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR242_crm_com_app_col_cea_val_aft_crm(BigDecimal r242_crm_com_app_col_cea_val_aft_crm) {
		this.r242_crm_com_app_col_cea_val_aft_crm = r242_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR242_rwa_elig_cea_not_cov() {
		return r242_rwa_elig_cea_not_cov;
	}
	public void setR242_rwa_elig_cea_not_cov(BigDecimal r242_rwa_elig_cea_not_cov) {
		this.r242_rwa_elig_cea_not_cov = r242_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR242_rwa_unsec_cea_sub_cre_ris() {
		return r242_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR242_rwa_unsec_cea_sub_cre_ris(BigDecimal r242_rwa_unsec_cea_sub_cre_ris) {
		this.r242_rwa_unsec_cea_sub_cre_ris = r242_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR242_rwa_unsec_cea() {
		return r242_rwa_unsec_cea;
	}
	public void setR242_rwa_unsec_cea(BigDecimal r242_rwa_unsec_cea) {
		this.r242_rwa_unsec_cea = r242_rwa_unsec_cea;
	}
	public BigDecimal getR242_rwa_tot_ris_wei_ass() {
		return r242_rwa_tot_ris_wei_ass;
	}
	public void setR242_rwa_tot_ris_wei_ass(BigDecimal r242_rwa_tot_ris_wei_ass) {
		this.r242_rwa_tot_ris_wei_ass = r242_rwa_tot_ris_wei_ass;
	}
	public String getR243_exposure_class_off_bal() {
		return r243_exposure_class_off_bal;
	}
	public void setR243_exposure_class_off_bal(String r243_exposure_class_off_bal) {
		this.r243_exposure_class_off_bal = r243_exposure_class_off_bal;
	}
	public BigDecimal getR243_nom_pri_amt() {
		return r243_nom_pri_amt;
	}
	public void setR243_nom_pri_amt(BigDecimal r243_nom_pri_amt) {
		this.r243_nom_pri_amt = r243_nom_pri_amt;
	}
	public BigDecimal getR243_ccf() {
		return r243_ccf;
	}
	public void setR243_ccf(BigDecimal r243_ccf) {
		this.r243_ccf = r243_ccf;
	}
	public BigDecimal getR243_cea() {
		return r243_cea;
	}
	public void setR243_cea(BigDecimal r243_cea) {
		this.r243_cea = r243_cea;
	}
	public BigDecimal getR243_cea_elig_coun_bilt_net() {
		return r243_cea_elig_coun_bilt_net;
	}
	public void setR243_cea_elig_coun_bilt_net(BigDecimal r243_cea_elig_coun_bilt_net) {
		this.r243_cea_elig_coun_bilt_net = r243_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR243_cea_aft_net() {
		return r243_cea_aft_net;
	}
	public void setR243_cea_aft_net(BigDecimal r243_cea_aft_net) {
		this.r243_cea_aft_net = r243_cea_aft_net;
	}
	public BigDecimal getR243_crm_sub_app_cea_elig() {
		return r243_crm_sub_app_cea_elig;
	}
	public void setR243_crm_sub_app_cea_elig(BigDecimal r243_crm_sub_app_cea_elig) {
		this.r243_crm_sub_app_cea_elig = r243_crm_sub_app_cea_elig;
	}
	public BigDecimal getR243_crm_sub_app_non_col_guar_elig() {
		return r243_crm_sub_app_non_col_guar_elig;
	}
	public void setR243_crm_sub_app_non_col_guar_elig(BigDecimal r243_crm_sub_app_non_col_guar_elig) {
		this.r243_crm_sub_app_non_col_guar_elig = r243_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR243_crm_sub_app_non_col_cre_der() {
		return r243_crm_sub_app_non_col_cre_der;
	}
	public void setR243_crm_sub_app_non_col_cre_der(BigDecimal r243_crm_sub_app_non_col_cre_der) {
		this.r243_crm_sub_app_non_col_cre_der = r243_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR243_crm_sub_app_col_elig_cash() {
		return r243_crm_sub_app_col_elig_cash;
	}
	public void setR243_crm_sub_app_col_elig_cash(BigDecimal r243_crm_sub_app_col_elig_cash) {
		this.r243_crm_sub_app_col_elig_cash = r243_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR243_crm_sub_app_col_elig_tre_bills() {
		return r243_crm_sub_app_col_elig_tre_bills;
	}
	public void setR243_crm_sub_app_col_elig_tre_bills(BigDecimal r243_crm_sub_app_col_elig_tre_bills) {
		this.r243_crm_sub_app_col_elig_tre_bills = r243_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR243_crm_sub_app_col_elig_deb_sec() {
		return r243_crm_sub_app_col_elig_deb_sec;
	}
	public void setR243_crm_sub_app_col_elig_deb_sec(BigDecimal r243_crm_sub_app_col_elig_deb_sec) {
		this.r243_crm_sub_app_col_elig_deb_sec = r243_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR243_crm_sub_app_col_elig_euiq() {
		return r243_crm_sub_app_col_elig_euiq;
	}
	public void setR243_crm_sub_app_col_elig_euiq(BigDecimal r243_crm_sub_app_col_elig_euiq) {
		this.r243_crm_sub_app_col_elig_euiq = r243_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR243_crm_sub_app_col_elig_uni_tru() {
		return r243_crm_sub_app_col_elig_uni_tru;
	}
	public void setR243_crm_sub_app_col_elig_uni_tru(BigDecimal r243_crm_sub_app_col_elig_uni_tru) {
		this.r243_crm_sub_app_col_elig_uni_tru = r243_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR243_crm_sub_app_col_cea_cov() {
		return r243_crm_sub_app_col_cea_cov;
	}
	public void setR243_crm_sub_app_col_cea_cov(BigDecimal r243_crm_sub_app_col_cea_cov) {
		this.r243_crm_sub_app_col_cea_cov = r243_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR243_crm_sub_app_col_cea_not_cov() {
		return r243_crm_sub_app_col_cea_not_cov;
	}
	public void setR243_crm_sub_app_col_cea_not_cov(BigDecimal r243_crm_sub_app_col_cea_not_cov) {
		this.r243_crm_sub_app_col_cea_not_cov = r243_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR243_crm_sub_app_rwa_ris_wei_crm() {
		return r243_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR243_crm_sub_app_rwa_ris_wei_crm(BigDecimal r243_crm_sub_app_rwa_ris_wei_crm) {
		this.r243_crm_sub_app_rwa_ris_wei_crm = r243_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR243_crm_sub_app_rwa_ris_cea_cov() {
		return r243_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR243_crm_sub_app_rwa_ris_cea_cov(BigDecimal r243_crm_sub_app_rwa_ris_cea_cov) {
		this.r243_crm_sub_app_rwa_ris_cea_cov = r243_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR243_crm_sub_app_rwa_appl_org_coun() {
		return r243_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR243_crm_sub_app_rwa_appl_org_coun(BigDecimal r243_crm_sub_app_rwa_appl_org_coun) {
		this.r243_crm_sub_app_rwa_appl_org_coun = r243_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR243_crm_sub_app_rwa_ris_cea_not_cov() {
		return r243_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR243_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r243_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r243_crm_sub_app_rwa_ris_cea_not_cov = r243_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR243_crm_com_app_col_cea_elig_crm() {
		return r243_crm_com_app_col_cea_elig_crm;
	}
	public void setR243_crm_com_app_col_cea_elig_crm(BigDecimal r243_crm_com_app_col_cea_elig_crm) {
		this.r243_crm_com_app_col_cea_elig_crm = r243_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR243_crm_com_app_col_elig_cea_vol_adj() {
		return r243_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR243_crm_com_app_col_elig_cea_vol_adj(BigDecimal r243_crm_com_app_col_elig_cea_vol_adj) {
		this.r243_crm_com_app_col_elig_cea_vol_adj = r243_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR243_crm_com_app_col_elig_fin_hai() {
		return r243_crm_com_app_col_elig_fin_hai;
	}
	public void setR243_crm_com_app_col_elig_fin_hai(BigDecimal r243_crm_com_app_col_elig_fin_hai) {
		this.r243_crm_com_app_col_elig_fin_hai = r243_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR243_crm_com_app_col_cea_val_aft_crm() {
		return r243_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR243_crm_com_app_col_cea_val_aft_crm(BigDecimal r243_crm_com_app_col_cea_val_aft_crm) {
		this.r243_crm_com_app_col_cea_val_aft_crm = r243_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR243_rwa_elig_cea_not_cov() {
		return r243_rwa_elig_cea_not_cov;
	}
	public void setR243_rwa_elig_cea_not_cov(BigDecimal r243_rwa_elig_cea_not_cov) {
		this.r243_rwa_elig_cea_not_cov = r243_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR243_rwa_unsec_cea_sub_cre_ris() {
		return r243_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR243_rwa_unsec_cea_sub_cre_ris(BigDecimal r243_rwa_unsec_cea_sub_cre_ris) {
		this.r243_rwa_unsec_cea_sub_cre_ris = r243_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR243_rwa_unsec_cea() {
		return r243_rwa_unsec_cea;
	}
	public void setR243_rwa_unsec_cea(BigDecimal r243_rwa_unsec_cea) {
		this.r243_rwa_unsec_cea = r243_rwa_unsec_cea;
	}
	public BigDecimal getR243_rwa_tot_ris_wei_ass() {
		return r243_rwa_tot_ris_wei_ass;
	}
	public void setR243_rwa_tot_ris_wei_ass(BigDecimal r243_rwa_tot_ris_wei_ass) {
		this.r243_rwa_tot_ris_wei_ass = r243_rwa_tot_ris_wei_ass;
	}
	public String getR244_exposure_class_off_bal() {
		return r244_exposure_class_off_bal;
	}
	public void setR244_exposure_class_off_bal(String r244_exposure_class_off_bal) {
		this.r244_exposure_class_off_bal = r244_exposure_class_off_bal;
	}
	public BigDecimal getR244_nom_pri_amt() {
		return r244_nom_pri_amt;
	}
	public void setR244_nom_pri_amt(BigDecimal r244_nom_pri_amt) {
		this.r244_nom_pri_amt = r244_nom_pri_amt;
	}
	public BigDecimal getR244_ccf() {
		return r244_ccf;
	}
	public void setR244_ccf(BigDecimal r244_ccf) {
		this.r244_ccf = r244_ccf;
	}
	public BigDecimal getR244_cea() {
		return r244_cea;
	}
	public void setR244_cea(BigDecimal r244_cea) {
		this.r244_cea = r244_cea;
	}
	public BigDecimal getR244_cea_elig_coun_bilt_net() {
		return r244_cea_elig_coun_bilt_net;
	}
	public void setR244_cea_elig_coun_bilt_net(BigDecimal r244_cea_elig_coun_bilt_net) {
		this.r244_cea_elig_coun_bilt_net = r244_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR244_cea_aft_net() {
		return r244_cea_aft_net;
	}
	public void setR244_cea_aft_net(BigDecimal r244_cea_aft_net) {
		this.r244_cea_aft_net = r244_cea_aft_net;
	}
	public BigDecimal getR244_crm_sub_app_cea_elig() {
		return r244_crm_sub_app_cea_elig;
	}
	public void setR244_crm_sub_app_cea_elig(BigDecimal r244_crm_sub_app_cea_elig) {
		this.r244_crm_sub_app_cea_elig = r244_crm_sub_app_cea_elig;
	}
	public BigDecimal getR244_crm_sub_app_non_col_guar_elig() {
		return r244_crm_sub_app_non_col_guar_elig;
	}
	public void setR244_crm_sub_app_non_col_guar_elig(BigDecimal r244_crm_sub_app_non_col_guar_elig) {
		this.r244_crm_sub_app_non_col_guar_elig = r244_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR244_crm_sub_app_non_col_cre_der() {
		return r244_crm_sub_app_non_col_cre_der;
	}
	public void setR244_crm_sub_app_non_col_cre_der(BigDecimal r244_crm_sub_app_non_col_cre_der) {
		this.r244_crm_sub_app_non_col_cre_der = r244_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR244_crm_sub_app_col_elig_cash() {
		return r244_crm_sub_app_col_elig_cash;
	}
	public void setR244_crm_sub_app_col_elig_cash(BigDecimal r244_crm_sub_app_col_elig_cash) {
		this.r244_crm_sub_app_col_elig_cash = r244_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR244_crm_sub_app_col_elig_tre_bills() {
		return r244_crm_sub_app_col_elig_tre_bills;
	}
	public void setR244_crm_sub_app_col_elig_tre_bills(BigDecimal r244_crm_sub_app_col_elig_tre_bills) {
		this.r244_crm_sub_app_col_elig_tre_bills = r244_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR244_crm_sub_app_col_elig_deb_sec() {
		return r244_crm_sub_app_col_elig_deb_sec;
	}
	public void setR244_crm_sub_app_col_elig_deb_sec(BigDecimal r244_crm_sub_app_col_elig_deb_sec) {
		this.r244_crm_sub_app_col_elig_deb_sec = r244_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR244_crm_sub_app_col_elig_euiq() {
		return r244_crm_sub_app_col_elig_euiq;
	}
	public void setR244_crm_sub_app_col_elig_euiq(BigDecimal r244_crm_sub_app_col_elig_euiq) {
		this.r244_crm_sub_app_col_elig_euiq = r244_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR244_crm_sub_app_col_elig_uni_tru() {
		return r244_crm_sub_app_col_elig_uni_tru;
	}
	public void setR244_crm_sub_app_col_elig_uni_tru(BigDecimal r244_crm_sub_app_col_elig_uni_tru) {
		this.r244_crm_sub_app_col_elig_uni_tru = r244_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR244_crm_sub_app_col_cea_cov() {
		return r244_crm_sub_app_col_cea_cov;
	}
	public void setR244_crm_sub_app_col_cea_cov(BigDecimal r244_crm_sub_app_col_cea_cov) {
		this.r244_crm_sub_app_col_cea_cov = r244_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR244_crm_sub_app_col_cea_not_cov() {
		return r244_crm_sub_app_col_cea_not_cov;
	}
	public void setR244_crm_sub_app_col_cea_not_cov(BigDecimal r244_crm_sub_app_col_cea_not_cov) {
		this.r244_crm_sub_app_col_cea_not_cov = r244_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR244_crm_sub_app_rwa_ris_wei_crm() {
		return r244_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR244_crm_sub_app_rwa_ris_wei_crm(BigDecimal r244_crm_sub_app_rwa_ris_wei_crm) {
		this.r244_crm_sub_app_rwa_ris_wei_crm = r244_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR244_crm_sub_app_rwa_ris_cea_cov() {
		return r244_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR244_crm_sub_app_rwa_ris_cea_cov(BigDecimal r244_crm_sub_app_rwa_ris_cea_cov) {
		this.r244_crm_sub_app_rwa_ris_cea_cov = r244_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR244_crm_sub_app_rwa_appl_org_coun() {
		return r244_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR244_crm_sub_app_rwa_appl_org_coun(BigDecimal r244_crm_sub_app_rwa_appl_org_coun) {
		this.r244_crm_sub_app_rwa_appl_org_coun = r244_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR244_crm_sub_app_rwa_ris_cea_not_cov() {
		return r244_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR244_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r244_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r244_crm_sub_app_rwa_ris_cea_not_cov = r244_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR244_crm_com_app_col_cea_elig_crm() {
		return r244_crm_com_app_col_cea_elig_crm;
	}
	public void setR244_crm_com_app_col_cea_elig_crm(BigDecimal r244_crm_com_app_col_cea_elig_crm) {
		this.r244_crm_com_app_col_cea_elig_crm = r244_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR244_crm_com_app_col_elig_cea_vol_adj() {
		return r244_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR244_crm_com_app_col_elig_cea_vol_adj(BigDecimal r244_crm_com_app_col_elig_cea_vol_adj) {
		this.r244_crm_com_app_col_elig_cea_vol_adj = r244_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR244_crm_com_app_col_elig_fin_hai() {
		return r244_crm_com_app_col_elig_fin_hai;
	}
	public void setR244_crm_com_app_col_elig_fin_hai(BigDecimal r244_crm_com_app_col_elig_fin_hai) {
		this.r244_crm_com_app_col_elig_fin_hai = r244_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR244_crm_com_app_col_cea_val_aft_crm() {
		return r244_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR244_crm_com_app_col_cea_val_aft_crm(BigDecimal r244_crm_com_app_col_cea_val_aft_crm) {
		this.r244_crm_com_app_col_cea_val_aft_crm = r244_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR244_rwa_elig_cea_not_cov() {
		return r244_rwa_elig_cea_not_cov;
	}
	public void setR244_rwa_elig_cea_not_cov(BigDecimal r244_rwa_elig_cea_not_cov) {
		this.r244_rwa_elig_cea_not_cov = r244_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR244_rwa_unsec_cea_sub_cre_ris() {
		return r244_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR244_rwa_unsec_cea_sub_cre_ris(BigDecimal r244_rwa_unsec_cea_sub_cre_ris) {
		this.r244_rwa_unsec_cea_sub_cre_ris = r244_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR244_rwa_unsec_cea() {
		return r244_rwa_unsec_cea;
	}
	public void setR244_rwa_unsec_cea(BigDecimal r244_rwa_unsec_cea) {
		this.r244_rwa_unsec_cea = r244_rwa_unsec_cea;
	}
	public BigDecimal getR244_rwa_tot_ris_wei_ass() {
		return r244_rwa_tot_ris_wei_ass;
	}
	public void setR244_rwa_tot_ris_wei_ass(BigDecimal r244_rwa_tot_ris_wei_ass) {
		this.r244_rwa_tot_ris_wei_ass = r244_rwa_tot_ris_wei_ass;
	}
	public String getR245_exposure_class_off_bal() {
		return r245_exposure_class_off_bal;
	}
	public void setR245_exposure_class_off_bal(String r245_exposure_class_off_bal) {
		this.r245_exposure_class_off_bal = r245_exposure_class_off_bal;
	}
	public BigDecimal getR245_nom_pri_amt() {
		return r245_nom_pri_amt;
	}
	public void setR245_nom_pri_amt(BigDecimal r245_nom_pri_amt) {
		this.r245_nom_pri_amt = r245_nom_pri_amt;
	}
	public BigDecimal getR245_ccf() {
		return r245_ccf;
	}
	public void setR245_ccf(BigDecimal r245_ccf) {
		this.r245_ccf = r245_ccf;
	}
	public BigDecimal getR245_cea() {
		return r245_cea;
	}
	public void setR245_cea(BigDecimal r245_cea) {
		this.r245_cea = r245_cea;
	}
	public BigDecimal getR245_cea_elig_coun_bilt_net() {
		return r245_cea_elig_coun_bilt_net;
	}
	public void setR245_cea_elig_coun_bilt_net(BigDecimal r245_cea_elig_coun_bilt_net) {
		this.r245_cea_elig_coun_bilt_net = r245_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR245_cea_aft_net() {
		return r245_cea_aft_net;
	}
	public void setR245_cea_aft_net(BigDecimal r245_cea_aft_net) {
		this.r245_cea_aft_net = r245_cea_aft_net;
	}
	public BigDecimal getR245_crm_sub_app_cea_elig() {
		return r245_crm_sub_app_cea_elig;
	}
	public void setR245_crm_sub_app_cea_elig(BigDecimal r245_crm_sub_app_cea_elig) {
		this.r245_crm_sub_app_cea_elig = r245_crm_sub_app_cea_elig;
	}
	public BigDecimal getR245_crm_sub_app_non_col_guar_elig() {
		return r245_crm_sub_app_non_col_guar_elig;
	}
	public void setR245_crm_sub_app_non_col_guar_elig(BigDecimal r245_crm_sub_app_non_col_guar_elig) {
		this.r245_crm_sub_app_non_col_guar_elig = r245_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR245_crm_sub_app_non_col_cre_der() {
		return r245_crm_sub_app_non_col_cre_der;
	}
	public void setR245_crm_sub_app_non_col_cre_der(BigDecimal r245_crm_sub_app_non_col_cre_der) {
		this.r245_crm_sub_app_non_col_cre_der = r245_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR245_crm_sub_app_col_elig_cash() {
		return r245_crm_sub_app_col_elig_cash;
	}
	public void setR245_crm_sub_app_col_elig_cash(BigDecimal r245_crm_sub_app_col_elig_cash) {
		this.r245_crm_sub_app_col_elig_cash = r245_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR245_crm_sub_app_col_elig_tre_bills() {
		return r245_crm_sub_app_col_elig_tre_bills;
	}
	public void setR245_crm_sub_app_col_elig_tre_bills(BigDecimal r245_crm_sub_app_col_elig_tre_bills) {
		this.r245_crm_sub_app_col_elig_tre_bills = r245_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR245_crm_sub_app_col_elig_deb_sec() {
		return r245_crm_sub_app_col_elig_deb_sec;
	}
	public void setR245_crm_sub_app_col_elig_deb_sec(BigDecimal r245_crm_sub_app_col_elig_deb_sec) {
		this.r245_crm_sub_app_col_elig_deb_sec = r245_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR245_crm_sub_app_col_elig_euiq() {
		return r245_crm_sub_app_col_elig_euiq;
	}
	public void setR245_crm_sub_app_col_elig_euiq(BigDecimal r245_crm_sub_app_col_elig_euiq) {
		this.r245_crm_sub_app_col_elig_euiq = r245_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR245_crm_sub_app_col_elig_uni_tru() {
		return r245_crm_sub_app_col_elig_uni_tru;
	}
	public void setR245_crm_sub_app_col_elig_uni_tru(BigDecimal r245_crm_sub_app_col_elig_uni_tru) {
		this.r245_crm_sub_app_col_elig_uni_tru = r245_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR245_crm_sub_app_col_cea_cov() {
		return r245_crm_sub_app_col_cea_cov;
	}
	public void setR245_crm_sub_app_col_cea_cov(BigDecimal r245_crm_sub_app_col_cea_cov) {
		this.r245_crm_sub_app_col_cea_cov = r245_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR245_crm_sub_app_col_cea_not_cov() {
		return r245_crm_sub_app_col_cea_not_cov;
	}
	public void setR245_crm_sub_app_col_cea_not_cov(BigDecimal r245_crm_sub_app_col_cea_not_cov) {
		this.r245_crm_sub_app_col_cea_not_cov = r245_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR245_crm_sub_app_rwa_ris_wei_crm() {
		return r245_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR245_crm_sub_app_rwa_ris_wei_crm(BigDecimal r245_crm_sub_app_rwa_ris_wei_crm) {
		this.r245_crm_sub_app_rwa_ris_wei_crm = r245_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR245_crm_sub_app_rwa_ris_cea_cov() {
		return r245_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR245_crm_sub_app_rwa_ris_cea_cov(BigDecimal r245_crm_sub_app_rwa_ris_cea_cov) {
		this.r245_crm_sub_app_rwa_ris_cea_cov = r245_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR245_crm_sub_app_rwa_appl_org_coun() {
		return r245_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR245_crm_sub_app_rwa_appl_org_coun(BigDecimal r245_crm_sub_app_rwa_appl_org_coun) {
		this.r245_crm_sub_app_rwa_appl_org_coun = r245_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR245_crm_sub_app_rwa_ris_cea_not_cov() {
		return r245_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR245_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r245_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r245_crm_sub_app_rwa_ris_cea_not_cov = r245_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR245_crm_com_app_col_cea_elig_crm() {
		return r245_crm_com_app_col_cea_elig_crm;
	}
	public void setR245_crm_com_app_col_cea_elig_crm(BigDecimal r245_crm_com_app_col_cea_elig_crm) {
		this.r245_crm_com_app_col_cea_elig_crm = r245_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR245_crm_com_app_col_elig_cea_vol_adj() {
		return r245_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR245_crm_com_app_col_elig_cea_vol_adj(BigDecimal r245_crm_com_app_col_elig_cea_vol_adj) {
		this.r245_crm_com_app_col_elig_cea_vol_adj = r245_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR245_crm_com_app_col_elig_fin_hai() {
		return r245_crm_com_app_col_elig_fin_hai;
	}
	public void setR245_crm_com_app_col_elig_fin_hai(BigDecimal r245_crm_com_app_col_elig_fin_hai) {
		this.r245_crm_com_app_col_elig_fin_hai = r245_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR245_crm_com_app_col_cea_val_aft_crm() {
		return r245_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR245_crm_com_app_col_cea_val_aft_crm(BigDecimal r245_crm_com_app_col_cea_val_aft_crm) {
		this.r245_crm_com_app_col_cea_val_aft_crm = r245_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR245_rwa_elig_cea_not_cov() {
		return r245_rwa_elig_cea_not_cov;
	}
	public void setR245_rwa_elig_cea_not_cov(BigDecimal r245_rwa_elig_cea_not_cov) {
		this.r245_rwa_elig_cea_not_cov = r245_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR245_rwa_unsec_cea_sub_cre_ris() {
		return r245_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR245_rwa_unsec_cea_sub_cre_ris(BigDecimal r245_rwa_unsec_cea_sub_cre_ris) {
		this.r245_rwa_unsec_cea_sub_cre_ris = r245_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR245_rwa_unsec_cea() {
		return r245_rwa_unsec_cea;
	}
	public void setR245_rwa_unsec_cea(BigDecimal r245_rwa_unsec_cea) {
		this.r245_rwa_unsec_cea = r245_rwa_unsec_cea;
	}
	public BigDecimal getR245_rwa_tot_ris_wei_ass() {
		return r245_rwa_tot_ris_wei_ass;
	}
	public void setR245_rwa_tot_ris_wei_ass(BigDecimal r245_rwa_tot_ris_wei_ass) {
		this.r245_rwa_tot_ris_wei_ass = r245_rwa_tot_ris_wei_ass;
	}
	public String getR246_exposure_class_off_bal() {
		return r246_exposure_class_off_bal;
	}
	public void setR246_exposure_class_off_bal(String r246_exposure_class_off_bal) {
		this.r246_exposure_class_off_bal = r246_exposure_class_off_bal;
	}
	public BigDecimal getR246_nom_pri_amt() {
		return r246_nom_pri_amt;
	}
	public void setR246_nom_pri_amt(BigDecimal r246_nom_pri_amt) {
		this.r246_nom_pri_amt = r246_nom_pri_amt;
	}
	public BigDecimal getR246_ccf() {
		return r246_ccf;
	}
	public void setR246_ccf(BigDecimal r246_ccf) {
		this.r246_ccf = r246_ccf;
	}
	public BigDecimal getR246_cea() {
		return r246_cea;
	}
	public void setR246_cea(BigDecimal r246_cea) {
		this.r246_cea = r246_cea;
	}
	public BigDecimal getR246_cea_elig_coun_bilt_net() {
		return r246_cea_elig_coun_bilt_net;
	}
	public void setR246_cea_elig_coun_bilt_net(BigDecimal r246_cea_elig_coun_bilt_net) {
		this.r246_cea_elig_coun_bilt_net = r246_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR246_cea_aft_net() {
		return r246_cea_aft_net;
	}
	public void setR246_cea_aft_net(BigDecimal r246_cea_aft_net) {
		this.r246_cea_aft_net = r246_cea_aft_net;
	}
	public BigDecimal getR246_crm_sub_app_cea_elig() {
		return r246_crm_sub_app_cea_elig;
	}
	public void setR246_crm_sub_app_cea_elig(BigDecimal r246_crm_sub_app_cea_elig) {
		this.r246_crm_sub_app_cea_elig = r246_crm_sub_app_cea_elig;
	}
	public BigDecimal getR246_crm_sub_app_non_col_guar_elig() {
		return r246_crm_sub_app_non_col_guar_elig;
	}
	public void setR246_crm_sub_app_non_col_guar_elig(BigDecimal r246_crm_sub_app_non_col_guar_elig) {
		this.r246_crm_sub_app_non_col_guar_elig = r246_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR246_crm_sub_app_non_col_cre_der() {
		return r246_crm_sub_app_non_col_cre_der;
	}
	public void setR246_crm_sub_app_non_col_cre_der(BigDecimal r246_crm_sub_app_non_col_cre_der) {
		this.r246_crm_sub_app_non_col_cre_der = r246_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR246_crm_sub_app_col_elig_cash() {
		return r246_crm_sub_app_col_elig_cash;
	}
	public void setR246_crm_sub_app_col_elig_cash(BigDecimal r246_crm_sub_app_col_elig_cash) {
		this.r246_crm_sub_app_col_elig_cash = r246_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR246_crm_sub_app_col_elig_tre_bills() {
		return r246_crm_sub_app_col_elig_tre_bills;
	}
	public void setR246_crm_sub_app_col_elig_tre_bills(BigDecimal r246_crm_sub_app_col_elig_tre_bills) {
		this.r246_crm_sub_app_col_elig_tre_bills = r246_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR246_crm_sub_app_col_elig_deb_sec() {
		return r246_crm_sub_app_col_elig_deb_sec;
	}
	public void setR246_crm_sub_app_col_elig_deb_sec(BigDecimal r246_crm_sub_app_col_elig_deb_sec) {
		this.r246_crm_sub_app_col_elig_deb_sec = r246_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR246_crm_sub_app_col_elig_euiq() {
		return r246_crm_sub_app_col_elig_euiq;
	}
	public void setR246_crm_sub_app_col_elig_euiq(BigDecimal r246_crm_sub_app_col_elig_euiq) {
		this.r246_crm_sub_app_col_elig_euiq = r246_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR246_crm_sub_app_col_elig_uni_tru() {
		return r246_crm_sub_app_col_elig_uni_tru;
	}
	public void setR246_crm_sub_app_col_elig_uni_tru(BigDecimal r246_crm_sub_app_col_elig_uni_tru) {
		this.r246_crm_sub_app_col_elig_uni_tru = r246_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR246_crm_sub_app_col_cea_cov() {
		return r246_crm_sub_app_col_cea_cov;
	}
	public void setR246_crm_sub_app_col_cea_cov(BigDecimal r246_crm_sub_app_col_cea_cov) {
		this.r246_crm_sub_app_col_cea_cov = r246_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR246_crm_sub_app_col_cea_not_cov() {
		return r246_crm_sub_app_col_cea_not_cov;
	}
	public void setR246_crm_sub_app_col_cea_not_cov(BigDecimal r246_crm_sub_app_col_cea_not_cov) {
		this.r246_crm_sub_app_col_cea_not_cov = r246_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR246_crm_sub_app_rwa_ris_wei_crm() {
		return r246_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR246_crm_sub_app_rwa_ris_wei_crm(BigDecimal r246_crm_sub_app_rwa_ris_wei_crm) {
		this.r246_crm_sub_app_rwa_ris_wei_crm = r246_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR246_crm_sub_app_rwa_ris_cea_cov() {
		return r246_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR246_crm_sub_app_rwa_ris_cea_cov(BigDecimal r246_crm_sub_app_rwa_ris_cea_cov) {
		this.r246_crm_sub_app_rwa_ris_cea_cov = r246_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR246_crm_sub_app_rwa_appl_org_coun() {
		return r246_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR246_crm_sub_app_rwa_appl_org_coun(BigDecimal r246_crm_sub_app_rwa_appl_org_coun) {
		this.r246_crm_sub_app_rwa_appl_org_coun = r246_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR246_crm_sub_app_rwa_ris_cea_not_cov() {
		return r246_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR246_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r246_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r246_crm_sub_app_rwa_ris_cea_not_cov = r246_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR246_crm_com_app_col_cea_elig_crm() {
		return r246_crm_com_app_col_cea_elig_crm;
	}
	public void setR246_crm_com_app_col_cea_elig_crm(BigDecimal r246_crm_com_app_col_cea_elig_crm) {
		this.r246_crm_com_app_col_cea_elig_crm = r246_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR246_crm_com_app_col_elig_cea_vol_adj() {
		return r246_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR246_crm_com_app_col_elig_cea_vol_adj(BigDecimal r246_crm_com_app_col_elig_cea_vol_adj) {
		this.r246_crm_com_app_col_elig_cea_vol_adj = r246_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR246_crm_com_app_col_elig_fin_hai() {
		return r246_crm_com_app_col_elig_fin_hai;
	}
	public void setR246_crm_com_app_col_elig_fin_hai(BigDecimal r246_crm_com_app_col_elig_fin_hai) {
		this.r246_crm_com_app_col_elig_fin_hai = r246_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR246_crm_com_app_col_cea_val_aft_crm() {
		return r246_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR246_crm_com_app_col_cea_val_aft_crm(BigDecimal r246_crm_com_app_col_cea_val_aft_crm) {
		this.r246_crm_com_app_col_cea_val_aft_crm = r246_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR246_rwa_elig_cea_not_cov() {
		return r246_rwa_elig_cea_not_cov;
	}
	public void setR246_rwa_elig_cea_not_cov(BigDecimal r246_rwa_elig_cea_not_cov) {
		this.r246_rwa_elig_cea_not_cov = r246_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR246_rwa_unsec_cea_sub_cre_ris() {
		return r246_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR246_rwa_unsec_cea_sub_cre_ris(BigDecimal r246_rwa_unsec_cea_sub_cre_ris) {
		this.r246_rwa_unsec_cea_sub_cre_ris = r246_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR246_rwa_unsec_cea() {
		return r246_rwa_unsec_cea;
	}
	public void setR246_rwa_unsec_cea(BigDecimal r246_rwa_unsec_cea) {
		this.r246_rwa_unsec_cea = r246_rwa_unsec_cea;
	}
	public BigDecimal getR246_rwa_tot_ris_wei_ass() {
		return r246_rwa_tot_ris_wei_ass;
	}
	public void setR246_rwa_tot_ris_wei_ass(BigDecimal r246_rwa_tot_ris_wei_ass) {
		this.r246_rwa_tot_ris_wei_ass = r246_rwa_tot_ris_wei_ass;
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
	public M_SRWA_12A_NEW_Summary_Entity7() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
