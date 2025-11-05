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
@Table(name = "BRRS_M_SRWA_12A_ARCHIVALTABLE_SUMMARY4")


public class M_SRWA_12A_Archival_Summary_Entity4 {
	
	
	private String	r145_exposure_class_off_bal;
	private BigDecimal	r145_nom_pri_amt;
	private BigDecimal	r145_ccf;
	private BigDecimal	r145_cea;
	private BigDecimal	r145_cea_elig_coun_bilt_net;
	private BigDecimal	r145_cea_aft_net;
	private BigDecimal	r145_crm_sub_app_cea_elig;
	private BigDecimal	r145_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r145_crm_sub_app_non_col_cre_der;
	private BigDecimal	r145_crm_sub_app_col_elig_cash;
	private BigDecimal	r145_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r145_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r145_crm_sub_app_col_elig_euiq;
	private BigDecimal	r145_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r145_crm_sub_app_col_cea_cov;
	private BigDecimal	r145_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r145_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r145_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r145_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r145_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r145_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r145_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r145_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r145_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r145_rwa_elig_cea_not_cov;
	private BigDecimal	r145_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r145_rwa_unsec_cea;
	private BigDecimal	r145_rwa_tot_ris_wei_ass;
	
	private String	r146_exposure_class_off_bal;
	private BigDecimal	r146_nom_pri_amt;
	private BigDecimal	r146_ccf;
	private BigDecimal	r146_cea;
	private BigDecimal	r146_cea_elig_coun_bilt_net;
	private BigDecimal	r146_cea_aft_net;
	private BigDecimal	r146_crm_sub_app_cea_elig;
	private BigDecimal	r146_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r146_crm_sub_app_non_col_cre_der;
	private BigDecimal	r146_crm_sub_app_col_elig_cash;
	private BigDecimal	r146_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r146_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r146_crm_sub_app_col_elig_euiq;
	private BigDecimal	r146_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r146_crm_sub_app_col_cea_cov;
	private BigDecimal	r146_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r146_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r146_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r146_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r146_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r146_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r146_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r146_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r146_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r146_rwa_elig_cea_not_cov;
	private BigDecimal	r146_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r146_rwa_unsec_cea;
	private BigDecimal	r146_rwa_tot_ris_wei_ass;
	
	private String	r147_exposure_class_off_bal;
	private BigDecimal	r147_nom_pri_amt;
	private BigDecimal	r147_ccf;
	private BigDecimal	r147_cea;
	private BigDecimal	r147_cea_elig_coun_bilt_net;
	private BigDecimal	r147_cea_aft_net;
	private BigDecimal	r147_crm_sub_app_cea_elig;
	private BigDecimal	r147_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r147_crm_sub_app_non_col_cre_der;
	private BigDecimal	r147_crm_sub_app_col_elig_cash;
	private BigDecimal	r147_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r147_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r147_crm_sub_app_col_elig_euiq;
	private BigDecimal	r147_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r147_crm_sub_app_col_cea_cov;
	private BigDecimal	r147_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r147_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r147_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r147_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r147_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r147_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r147_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r147_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r147_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r147_rwa_elig_cea_not_cov;
	private BigDecimal	r147_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r147_rwa_unsec_cea;
	private BigDecimal	r147_rwa_tot_ris_wei_ass;
	
	private String	r148_exposure_class_off_bal;
	private BigDecimal	r148_nom_pri_amt;
	private BigDecimal	r148_ccf;
	private BigDecimal	r148_cea;
	private BigDecimal	r148_cea_elig_coun_bilt_net;
	private BigDecimal	r148_cea_aft_net;
	private BigDecimal	r148_crm_sub_app_cea_elig;
	private BigDecimal	r148_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r148_crm_sub_app_non_col_cre_der;
	private BigDecimal	r148_crm_sub_app_col_elig_cash;
	private BigDecimal	r148_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r148_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r148_crm_sub_app_col_elig_euiq;
	private BigDecimal	r148_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r148_crm_sub_app_col_cea_cov;
	private BigDecimal	r148_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r148_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r148_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r148_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r148_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r148_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r148_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r148_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r148_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r148_rwa_elig_cea_not_cov;
	private BigDecimal	r148_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r148_rwa_unsec_cea;
	private BigDecimal	r148_rwa_tot_ris_wei_ass;
	
	private String	r149_exposure_class_off_bal;
	private BigDecimal	r149_nom_pri_amt;
	private BigDecimal	r149_ccf;
	private BigDecimal	r149_cea;
	private BigDecimal	r149_cea_elig_coun_bilt_net;
	private BigDecimal	r149_cea_aft_net;
	private BigDecimal	r149_crm_sub_app_cea_elig;
	private BigDecimal	r149_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r149_crm_sub_app_non_col_cre_der;
	private BigDecimal	r149_crm_sub_app_col_elig_cash;
	private BigDecimal	r149_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r149_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r149_crm_sub_app_col_elig_euiq;
	private BigDecimal	r149_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r149_crm_sub_app_col_cea_cov;
	private BigDecimal	r149_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r149_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r149_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r149_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r149_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r149_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r149_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r149_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r149_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r149_rwa_elig_cea_not_cov;
	private BigDecimal	r149_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r149_rwa_unsec_cea;
	private BigDecimal	r149_rwa_tot_ris_wei_ass;
	
	private String	r150_exposure_class_off_bal;
	private BigDecimal	r150_nom_pri_amt;
	private BigDecimal	r150_ccf;
	private BigDecimal	r150_cea;
	private BigDecimal	r150_cea_elig_coun_bilt_net;
	private BigDecimal	r150_cea_aft_net;
	private BigDecimal	r150_crm_sub_app_cea_elig;
	private BigDecimal	r150_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r150_crm_sub_app_non_col_cre_der;
	private BigDecimal	r150_crm_sub_app_col_elig_cash;
	private BigDecimal	r150_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r150_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r150_crm_sub_app_col_elig_euiq;
	private BigDecimal	r150_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r150_crm_sub_app_col_cea_cov;
	private BigDecimal	r150_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r150_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r150_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r150_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r150_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r150_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r150_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r150_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r150_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r150_rwa_elig_cea_not_cov;
	private BigDecimal	r150_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r150_rwa_unsec_cea;
	private BigDecimal	r150_rwa_tot_ris_wei_ass;
	
	private String	r151_exposure_class_off_bal;
	private BigDecimal	r151_nom_pri_amt;
	private BigDecimal	r151_ccf;
	private BigDecimal	r151_cea;
	private BigDecimal	r151_cea_elig_coun_bilt_net;
	private BigDecimal	r151_cea_aft_net;
	private BigDecimal	r151_crm_sub_app_cea_elig;
	private BigDecimal	r151_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r151_crm_sub_app_non_col_cre_der;
	private BigDecimal	r151_crm_sub_app_col_elig_cash;
	private BigDecimal	r151_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r151_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r151_crm_sub_app_col_elig_euiq;
	private BigDecimal	r151_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r151_crm_sub_app_col_cea_cov;
	private BigDecimal	r151_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r151_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r151_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r151_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r151_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r151_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r151_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r151_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r151_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r151_rwa_elig_cea_not_cov;
	private BigDecimal	r151_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r151_rwa_unsec_cea;
	private BigDecimal	r151_rwa_tot_ris_wei_ass;

	private String	r152_exposure_class_off_bal;
	private BigDecimal	r152_nom_pri_amt;
	private BigDecimal	r152_ccf;
	private BigDecimal	r152_cea;
	private BigDecimal	r152_cea_elig_coun_bilt_net;
	private BigDecimal	r152_cea_aft_net;
	private BigDecimal	r152_crm_sub_app_cea_elig;
	private BigDecimal	r152_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r152_crm_sub_app_non_col_cre_der;
	private BigDecimal	r152_crm_sub_app_col_elig_cash;
	private BigDecimal	r152_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r152_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r152_crm_sub_app_col_elig_euiq;
	private BigDecimal	r152_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r152_crm_sub_app_col_cea_cov;
	private BigDecimal	r152_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r152_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r152_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r152_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r152_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r152_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r152_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r152_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r152_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r152_rwa_elig_cea_not_cov;
	private BigDecimal	r152_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r152_rwa_unsec_cea;
	private BigDecimal	r152_rwa_tot_ris_wei_ass;
	
	private String	r153_exposure_class_off_bal;
	private BigDecimal	r153_nom_pri_amt;
	private BigDecimal	r153_ccf;
	private BigDecimal	r153_cea;
	private BigDecimal	r153_cea_elig_coun_bilt_net;
	private BigDecimal	r153_cea_aft_net;
	private BigDecimal	r153_crm_sub_app_cea_elig;
	private BigDecimal	r153_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r153_crm_sub_app_non_col_cre_der;
	private BigDecimal	r153_crm_sub_app_col_elig_cash;
	private BigDecimal	r153_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r153_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r153_crm_sub_app_col_elig_euiq;
	private BigDecimal	r153_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r153_crm_sub_app_col_cea_cov;
	private BigDecimal	r153_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r153_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r153_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r153_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r153_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r153_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r153_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r153_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r153_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r153_rwa_elig_cea_not_cov;
	private BigDecimal	r153_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r153_rwa_unsec_cea;
	private BigDecimal	r153_rwa_tot_ris_wei_ass;
	
	private String	r154_exposure_class_off_bal;
	private BigDecimal	r154_nom_pri_amt;
	private BigDecimal	r154_ccf;
	private BigDecimal	r154_cea;
	private BigDecimal	r154_cea_elig_coun_bilt_net;
	private BigDecimal	r154_cea_aft_net;
	private BigDecimal	r154_crm_sub_app_cea_elig;
	private BigDecimal	r154_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r154_crm_sub_app_non_col_cre_der;
	private BigDecimal	r154_crm_sub_app_col_elig_cash;
	private BigDecimal	r154_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r154_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r154_crm_sub_app_col_elig_euiq;
	private BigDecimal	r154_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r154_crm_sub_app_col_cea_cov;
	private BigDecimal	r154_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r154_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r154_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r154_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r154_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r154_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r154_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r154_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r154_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r154_rwa_elig_cea_not_cov;
	private BigDecimal	r154_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r154_rwa_unsec_cea;
	private BigDecimal	r154_rwa_tot_ris_wei_ass;
	
	private String	r155_exposure_class_off_bal;
	private BigDecimal	r155_nom_pri_amt;
	private BigDecimal	r155_ccf;
	private BigDecimal	r155_cea;
	private BigDecimal	r155_cea_elig_coun_bilt_net;
	private BigDecimal	r155_cea_aft_net;
	private BigDecimal	r155_crm_sub_app_cea_elig;
	private BigDecimal	r155_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r155_crm_sub_app_non_col_cre_der;
	private BigDecimal	r155_crm_sub_app_col_elig_cash;
	private BigDecimal	r155_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r155_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r155_crm_sub_app_col_elig_euiq;
	private BigDecimal	r155_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r155_crm_sub_app_col_cea_cov;
	private BigDecimal	r155_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r155_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r155_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r155_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r155_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r155_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r155_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r155_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r155_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r155_rwa_elig_cea_not_cov;
	private BigDecimal	r155_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r155_rwa_unsec_cea;
	private BigDecimal	r155_rwa_tot_ris_wei_ass;

	private String	r156_exposure_class_off_bal;
	private BigDecimal	r156_nom_pri_amt;
	private BigDecimal	r156_ccf;
	private BigDecimal	r156_cea;
	private BigDecimal	r156_cea_elig_coun_bilt_net;
	private BigDecimal	r156_cea_aft_net;
	private BigDecimal	r156_crm_sub_app_cea_elig;
	private BigDecimal	r156_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r156_crm_sub_app_non_col_cre_der;
	private BigDecimal	r156_crm_sub_app_col_elig_cash;
	private BigDecimal	r156_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r156_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r156_crm_sub_app_col_elig_euiq;
	private BigDecimal	r156_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r156_crm_sub_app_col_cea_cov;
	private BigDecimal	r156_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r156_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r156_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r156_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r156_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r156_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r156_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r156_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r156_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r156_rwa_elig_cea_not_cov;
	private BigDecimal	r156_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r156_rwa_unsec_cea;
	private BigDecimal	r156_rwa_tot_ris_wei_ass;
	
	private String	r157_exposure_class_off_bal;
	private BigDecimal	r157_nom_pri_amt;
	private BigDecimal	r157_ccf;
	private BigDecimal	r157_cea;
	private BigDecimal	r157_cea_elig_coun_bilt_net;
	private BigDecimal	r157_cea_aft_net;
	private BigDecimal	r157_crm_sub_app_cea_elig;
	private BigDecimal	r157_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r157_crm_sub_app_non_col_cre_der;
	private BigDecimal	r157_crm_sub_app_col_elig_cash;
	private BigDecimal	r157_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r157_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r157_crm_sub_app_col_elig_euiq;
	private BigDecimal	r157_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r157_crm_sub_app_col_cea_cov;
	private BigDecimal	r157_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r157_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r157_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r157_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r157_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r157_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r157_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r157_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r157_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r157_rwa_elig_cea_not_cov;
	private BigDecimal	r157_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r157_rwa_unsec_cea;
	private BigDecimal	r157_rwa_tot_ris_wei_ass;
	
	private String	r158_exposure_class_off_bal;
//	private BigDecimal	r158_nom_pri_amt;
	private BigDecimal	r158_ccf;
	private BigDecimal	r158_cea;
	private BigDecimal	r158_cea_elig_coun_bilt_net;
	private BigDecimal	r158_cea_aft_net;
	private BigDecimal	r158_crm_sub_app_cea_elig;
	private BigDecimal	r158_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r158_crm_sub_app_non_col_cre_der;
	private BigDecimal	r158_crm_sub_app_col_elig_cash;
	private BigDecimal	r158_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r158_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r158_crm_sub_app_col_elig_euiq;
	private BigDecimal	r158_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r158_crm_sub_app_col_cea_cov;
	private BigDecimal	r158_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r158_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r158_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r158_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r158_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r158_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r158_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r158_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r158_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r158_rwa_elig_cea_not_cov;
	private BigDecimal	r158_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r158_rwa_unsec_cea;
	private BigDecimal	r158_rwa_tot_ris_wei_ass;
	
	private String	r159_exposure_class_off_bal;
	private BigDecimal	r159_nom_pri_amt;
	private BigDecimal	r159_ccf;
	private BigDecimal	r159_cea;
	private BigDecimal	r159_cea_elig_coun_bilt_net;
	private BigDecimal	r159_cea_aft_net;
	private BigDecimal	r159_crm_sub_app_cea_elig;
	private BigDecimal	r159_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r159_crm_sub_app_non_col_cre_der;
	private BigDecimal	r159_crm_sub_app_col_elig_cash;
	private BigDecimal	r159_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r159_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r159_crm_sub_app_col_elig_euiq;
	private BigDecimal	r159_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r159_crm_sub_app_col_cea_cov;
	private BigDecimal	r159_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r159_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r159_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r159_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r159_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r159_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r159_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r159_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r159_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r159_rwa_elig_cea_not_cov;
	private BigDecimal	r159_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r159_rwa_unsec_cea;
	private BigDecimal	r159_rwa_tot_ris_wei_ass;

	private String	r160_exposure_class_off_bal;
	private BigDecimal	r160_nom_pri_amt;
	private BigDecimal	r160_ccf;
	private BigDecimal	r160_cea;
	private BigDecimal	r160_cea_elig_coun_bilt_net;
	private BigDecimal	r160_cea_aft_net;
	private BigDecimal	r160_crm_sub_app_cea_elig;
	private BigDecimal	r160_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r160_crm_sub_app_non_col_cre_der;
	private BigDecimal	r160_crm_sub_app_col_elig_cash;
	private BigDecimal	r160_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r160_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r160_crm_sub_app_col_elig_euiq;
	private BigDecimal	r160_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r160_crm_sub_app_col_cea_cov;
	private BigDecimal	r160_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r160_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r160_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r160_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r160_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r160_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r160_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r160_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r160_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r160_rwa_elig_cea_not_cov;
	private BigDecimal	r160_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r160_rwa_unsec_cea;
	private BigDecimal	r160_rwa_tot_ris_wei_ass;
	
	private String	r161_exposure_class_off_bal;
	private BigDecimal	r161_nom_pri_amt;
	private BigDecimal	r161_ccf;
	private BigDecimal	r161_cea;
	private BigDecimal	r161_cea_elig_coun_bilt_net;
	private BigDecimal	r161_cea_aft_net;
	private BigDecimal	r161_crm_sub_app_cea_elig;
	private BigDecimal	r161_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r161_crm_sub_app_non_col_cre_der;
	private BigDecimal	r161_crm_sub_app_col_elig_cash;
	private BigDecimal	r161_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r161_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r161_crm_sub_app_col_elig_euiq;
	private BigDecimal	r161_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r161_crm_sub_app_col_cea_cov;
	private BigDecimal	r161_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r161_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r161_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r161_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r161_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r161_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r161_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r161_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r161_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r161_rwa_elig_cea_not_cov;
	private BigDecimal	r161_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r161_rwa_unsec_cea;
	private BigDecimal	r161_rwa_tot_ris_wei_ass;
	
	private String	r162_exposure_class_off_bal;
	private BigDecimal	r162_nom_pri_amt;
	private BigDecimal	r162_ccf;
	private BigDecimal	r162_cea;
	private BigDecimal	r162_cea_elig_coun_bilt_net;
	private BigDecimal	r162_cea_aft_net;
	private BigDecimal	r162_crm_sub_app_cea_elig;
	private BigDecimal	r162_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r162_crm_sub_app_non_col_cre_der;
	private BigDecimal	r162_crm_sub_app_col_elig_cash;
	private BigDecimal	r162_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r162_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r162_crm_sub_app_col_elig_euiq;
	private BigDecimal	r162_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r162_crm_sub_app_col_cea_cov;
	private BigDecimal	r162_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r162_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r162_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r162_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r162_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r162_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r162_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r162_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r162_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r162_rwa_elig_cea_not_cov;
	private BigDecimal	r162_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r162_rwa_unsec_cea;
	private BigDecimal	r162_rwa_tot_ris_wei_ass;
	
	private String	r163_exposure_class_off_bal;
	private BigDecimal	r163_nom_pri_amt;
	private BigDecimal	r163_ccf;
	private BigDecimal	r163_cea;
	private BigDecimal	r163_cea_elig_coun_bilt_net;
	private BigDecimal	r163_cea_aft_net;
	private BigDecimal	r163_crm_sub_app_cea_elig;
	private BigDecimal	r163_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r163_crm_sub_app_non_col_cre_der;
	private BigDecimal	r163_crm_sub_app_col_elig_cash;
	private BigDecimal	r163_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r163_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r163_crm_sub_app_col_elig_euiq;
	private BigDecimal	r163_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r163_crm_sub_app_col_cea_cov;
	private BigDecimal	r163_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r163_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r163_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r163_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r163_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r163_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r163_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r163_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r163_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r163_rwa_elig_cea_not_cov;
	private BigDecimal	r163_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r163_rwa_unsec_cea;
	private BigDecimal	r163_rwa_tot_ris_wei_ass;
	
	private String	r164_exposure_class_off_bal;
	private BigDecimal	r164_nom_pri_amt;
	private BigDecimal	r164_ccf;
	private BigDecimal	r164_cea;
	private BigDecimal	r164_cea_elig_coun_bilt_net;
	private BigDecimal	r164_cea_aft_net;
	private BigDecimal	r164_crm_sub_app_cea_elig;
	private BigDecimal	r164_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r164_crm_sub_app_non_col_cre_der;
	private BigDecimal	r164_crm_sub_app_col_elig_cash;
	private BigDecimal	r164_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r164_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r164_crm_sub_app_col_elig_euiq;
	private BigDecimal	r164_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r164_crm_sub_app_col_cea_cov;
	private BigDecimal	r164_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r164_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r164_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r164_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r164_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r164_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r164_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r164_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r164_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r164_rwa_elig_cea_not_cov;
	private BigDecimal	r164_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r164_rwa_unsec_cea;
	private BigDecimal	r164_rwa_tot_ris_wei_ass;
	
	private String	r165_exposure_class_off_bal;
	private BigDecimal	r165_nom_pri_amt;
	private BigDecimal	r165_ccf;
	private BigDecimal	r165_cea;
	private BigDecimal	r165_cea_elig_coun_bilt_net;
	private BigDecimal	r165_cea_aft_net;
	private BigDecimal	r165_crm_sub_app_cea_elig;
	private BigDecimal	r165_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r165_crm_sub_app_non_col_cre_der;
	private BigDecimal	r165_crm_sub_app_col_elig_cash;
	private BigDecimal	r165_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r165_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r165_crm_sub_app_col_elig_euiq;
	private BigDecimal	r165_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r165_crm_sub_app_col_cea_cov;
	private BigDecimal	r165_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r165_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r165_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r165_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r165_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r165_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r165_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r165_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r165_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r165_rwa_elig_cea_not_cov;
	private BigDecimal	r165_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r165_rwa_unsec_cea;
	private BigDecimal	r165_rwa_tot_ris_wei_ass;
	
	private String	r166_exposure_class_off_bal;
	private BigDecimal	r166_nom_pri_amt;
	private BigDecimal	r166_ccf;
	private BigDecimal	r166_cea;
	private BigDecimal	r166_cea_elig_coun_bilt_net;
	private BigDecimal	r166_cea_aft_net;
	private BigDecimal	r166_crm_sub_app_cea_elig;
	private BigDecimal	r166_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r166_crm_sub_app_non_col_cre_der;
	private BigDecimal	r166_crm_sub_app_col_elig_cash;
	private BigDecimal	r166_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r166_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r166_crm_sub_app_col_elig_euiq;
	private BigDecimal	r166_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r166_crm_sub_app_col_cea_cov;
	private BigDecimal	r166_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r166_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r166_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r166_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r166_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r166_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r166_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r166_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r166_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r166_rwa_elig_cea_not_cov;
	private BigDecimal	r166_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r166_rwa_unsec_cea;
	private BigDecimal	r166_rwa_tot_ris_wei_ass;
	
	private String	r167_exposure_class_off_bal;
	private BigDecimal	r167_nom_pri_amt;
	private BigDecimal	r167_ccf;
	private BigDecimal	r167_cea;
	private BigDecimal	r167_cea_elig_coun_bilt_net;
	private BigDecimal	r167_cea_aft_net;
	private BigDecimal	r167_crm_sub_app_cea_elig;
	private BigDecimal	r167_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r167_crm_sub_app_non_col_cre_der;
	private BigDecimal	r167_crm_sub_app_col_elig_cash;
	private BigDecimal	r167_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r167_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r167_crm_sub_app_col_elig_euiq;
	private BigDecimal	r167_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r167_crm_sub_app_col_cea_cov;
	private BigDecimal	r167_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r167_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r167_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r167_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r167_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r167_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r167_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r167_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r167_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r167_rwa_elig_cea_not_cov;
	private BigDecimal	r167_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r167_rwa_unsec_cea;
	private BigDecimal	r167_rwa_tot_ris_wei_ass;
	
	private String	r168_exposure_class_off_bal;
	private BigDecimal	r168_nom_pri_amt;
	private BigDecimal	r168_ccf;
	private BigDecimal	r168_cea;
	private BigDecimal	r168_cea_elig_coun_bilt_net;
	private BigDecimal	r168_cea_aft_net;
	private BigDecimal	r168_crm_sub_app_cea_elig;
	private BigDecimal	r168_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r168_crm_sub_app_non_col_cre_der;
	private BigDecimal	r168_crm_sub_app_col_elig_cash;
	private BigDecimal	r168_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r168_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r168_crm_sub_app_col_elig_euiq;
	private BigDecimal	r168_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r168_crm_sub_app_col_cea_cov;
	private BigDecimal	r168_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r168_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r168_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r168_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r168_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r168_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r168_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r168_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r168_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r168_rwa_elig_cea_not_cov;
	private BigDecimal	r168_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r168_rwa_unsec_cea;
	private BigDecimal	r168_rwa_tot_ris_wei_ass;
	
	private String	r169_exposure_class_off_bal;
	private BigDecimal	r169_nom_pri_amt;
	private BigDecimal	r169_ccf;
	private BigDecimal	r169_cea;
	private BigDecimal	r169_cea_elig_coun_bilt_net;
	private BigDecimal	r169_cea_aft_net;
	private BigDecimal	r169_crm_sub_app_cea_elig;
	private BigDecimal	r169_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r169_crm_sub_app_non_col_cre_der;
	private BigDecimal	r169_crm_sub_app_col_elig_cash;
	private BigDecimal	r169_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r169_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r169_crm_sub_app_col_elig_euiq;
	private BigDecimal	r169_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r169_crm_sub_app_col_cea_cov;
	private BigDecimal	r169_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r169_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r169_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r169_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r169_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r169_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r169_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r169_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r169_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r169_rwa_elig_cea_not_cov;
	private BigDecimal	r169_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r169_rwa_unsec_cea;
	private BigDecimal	r169_rwa_tot_ris_wei_ass;
	
	private String	r170_exposure_class_off_bal;
	private BigDecimal	r170_nom_pri_amt;
	private BigDecimal	r170_ccf;
	private BigDecimal	r170_cea;
	private BigDecimal	r170_cea_elig_coun_bilt_net;
	private BigDecimal	r170_cea_aft_net;
	private BigDecimal	r170_crm_sub_app_cea_elig;
	private BigDecimal	r170_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r170_crm_sub_app_non_col_cre_der;
	private BigDecimal	r170_crm_sub_app_col_elig_cash;
	private BigDecimal	r170_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r170_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r170_crm_sub_app_col_elig_euiq;
	private BigDecimal	r170_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r170_crm_sub_app_col_cea_cov;
	private BigDecimal	r170_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r170_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r170_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r170_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r170_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r170_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r170_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r170_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r170_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r170_rwa_elig_cea_not_cov;
	private BigDecimal	r170_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r170_rwa_unsec_cea;
	private BigDecimal	r170_rwa_tot_ris_wei_ass;
	
	private String	r171_exposure_class_off_bal;
	private BigDecimal	r171_nom_pri_amt;
	private BigDecimal	r171_ccf;
	private BigDecimal	r171_cea;
	private BigDecimal	r171_cea_elig_coun_bilt_net;
	private BigDecimal	r171_cea_aft_net;
	private BigDecimal	r171_crm_sub_app_cea_elig;
	private BigDecimal	r171_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r171_crm_sub_app_non_col_cre_der;
	private BigDecimal	r171_crm_sub_app_col_elig_cash;
	private BigDecimal	r171_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r171_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r171_crm_sub_app_col_elig_euiq;
	private BigDecimal	r171_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r171_crm_sub_app_col_cea_cov;
	private BigDecimal	r171_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r171_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r171_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r171_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r171_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r171_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r171_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r171_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r171_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r171_rwa_elig_cea_not_cov;
	private BigDecimal	r171_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r171_rwa_unsec_cea;
	private BigDecimal	r171_rwa_tot_ris_wei_ass;
	
	private String	r172_exposure_class_off_bal;
	private BigDecimal	r172_nom_pri_amt;
	private BigDecimal	r172_ccf;
	private BigDecimal	r172_cea;
	private BigDecimal	r172_cea_elig_coun_bilt_net;
	private BigDecimal	r172_cea_aft_net;
	private BigDecimal	r172_crm_sub_app_cea_elig;
	private BigDecimal	r172_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r172_crm_sub_app_non_col_cre_der;
	private BigDecimal	r172_crm_sub_app_col_elig_cash;
	private BigDecimal	r172_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r172_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r172_crm_sub_app_col_elig_euiq;
	private BigDecimal	r172_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r172_crm_sub_app_col_cea_cov;
	private BigDecimal	r172_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r172_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r172_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r172_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r172_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r172_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r172_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r172_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r172_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r172_rwa_elig_cea_not_cov;
	private BigDecimal	r172_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r172_rwa_unsec_cea;
	private BigDecimal	r172_rwa_tot_ris_wei_ass;
	
	private String	r173_exposure_class_off_bal;
	private BigDecimal	r173_nom_pri_amt;
	private BigDecimal	r173_ccf;
	private BigDecimal	r173_cea;
	private BigDecimal	r173_cea_elig_coun_bilt_net;
	private BigDecimal	r173_cea_aft_net;
	private BigDecimal	r173_crm_sub_app_cea_elig;
	private BigDecimal	r173_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r173_crm_sub_app_non_col_cre_der;
	private BigDecimal	r173_crm_sub_app_col_elig_cash;
	private BigDecimal	r173_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r173_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r173_crm_sub_app_col_elig_euiq;
	private BigDecimal	r173_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r173_crm_sub_app_col_cea_cov;
	private BigDecimal	r173_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r173_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r173_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r173_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r173_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r173_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r173_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r173_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r173_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r173_rwa_elig_cea_not_cov;
	private BigDecimal	r173_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r173_rwa_unsec_cea;
	private BigDecimal	r173_rwa_tot_ris_wei_ass;
	
	private String	r174_exposure_class_off_bal;
	private BigDecimal	r174_nom_pri_amt;
	private BigDecimal	r174_ccf;
	private BigDecimal	r174_cea;
	private BigDecimal	r174_cea_elig_coun_bilt_net;
	private BigDecimal	r174_cea_aft_net;
	private BigDecimal	r174_crm_sub_app_cea_elig;
	private BigDecimal	r174_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r174_crm_sub_app_non_col_cre_der;
	private BigDecimal	r174_crm_sub_app_col_elig_cash;
	private BigDecimal	r174_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r174_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r174_crm_sub_app_col_elig_euiq;
	private BigDecimal	r174_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r174_crm_sub_app_col_cea_cov;
	private BigDecimal	r174_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r174_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r174_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r174_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r174_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r174_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r174_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r174_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r174_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r174_rwa_elig_cea_not_cov;
	private BigDecimal	r174_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r174_rwa_unsec_cea;
	private BigDecimal	r174_rwa_tot_ris_wei_ass;
	
	private String	r175_exposure_class_off_bal;
	private BigDecimal	r175_nom_pri_amt;
	private BigDecimal	r175_ccf;
	private BigDecimal	r175_cea;
	private BigDecimal	r175_cea_elig_coun_bilt_net;
	private BigDecimal	r175_cea_aft_net;
	private BigDecimal	r175_crm_sub_app_cea_elig;
	private BigDecimal	r175_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r175_crm_sub_app_non_col_cre_der;
	private BigDecimal	r175_crm_sub_app_col_elig_cash;
	private BigDecimal	r175_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r175_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r175_crm_sub_app_col_elig_euiq;
	private BigDecimal	r175_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r175_crm_sub_app_col_cea_cov;
	private BigDecimal	r175_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r175_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r175_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r175_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r175_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r175_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r175_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r175_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r175_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r175_rwa_elig_cea_not_cov;
	private BigDecimal	r175_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r175_rwa_unsec_cea;
	private BigDecimal	r175_rwa_tot_ris_wei_ass;
	
	private String	r176_exposure_class_off_bal;
	private BigDecimal	r176_nom_pri_amt;
	private BigDecimal	r176_ccf;
	private BigDecimal	r176_cea;
	private BigDecimal	r176_cea_elig_coun_bilt_net;
	private BigDecimal	r176_cea_aft_net;
	private BigDecimal	r176_crm_sub_app_cea_elig;
	private BigDecimal	r176_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r176_crm_sub_app_non_col_cre_der;
	private BigDecimal	r176_crm_sub_app_col_elig_cash;
	private BigDecimal	r176_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r176_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r176_crm_sub_app_col_elig_euiq;
	private BigDecimal	r176_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r176_crm_sub_app_col_cea_cov;
	private BigDecimal	r176_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r176_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r176_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r176_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r176_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r176_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r176_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r176_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r176_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r176_rwa_elig_cea_not_cov;
	private BigDecimal	r176_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r176_rwa_unsec_cea;
	private BigDecimal	r176_rwa_tot_ris_wei_ass;
	
	private String	r177_exposure_class_off_bal;
	private BigDecimal	r177_nom_pri_amt;
	private BigDecimal	r177_ccf;
	private BigDecimal	r177_cea;
	private BigDecimal	r177_cea_elig_coun_bilt_net;
	private BigDecimal	r177_cea_aft_net;
	private BigDecimal	r177_crm_sub_app_cea_elig;
	private BigDecimal	r177_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r177_crm_sub_app_non_col_cre_der;
	private BigDecimal	r177_crm_sub_app_col_elig_cash;
	private BigDecimal	r177_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r177_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r177_crm_sub_app_col_elig_euiq;
	private BigDecimal	r177_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r177_crm_sub_app_col_cea_cov;
	private BigDecimal	r177_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r177_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r177_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r177_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r177_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r177_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r177_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r177_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r177_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r177_rwa_elig_cea_not_cov;
	private BigDecimal	r177_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r177_rwa_unsec_cea;
	private BigDecimal	r177_rwa_tot_ris_wei_ass;
	
	private String	r178_exposure_class_off_bal;
	private BigDecimal	r178_nom_pri_amt;
	private BigDecimal	r178_ccf;
	private BigDecimal	r178_cea;
	private BigDecimal	r178_cea_elig_coun_bilt_net;
	private BigDecimal	r178_cea_aft_net;
	private BigDecimal	r178_crm_sub_app_cea_elig;
	private BigDecimal	r178_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r178_crm_sub_app_non_col_cre_der;
	private BigDecimal	r178_crm_sub_app_col_elig_cash;
	private BigDecimal	r178_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r178_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r178_crm_sub_app_col_elig_euiq;
	private BigDecimal	r178_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r178_crm_sub_app_col_cea_cov;
	private BigDecimal	r178_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r178_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r178_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r178_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r178_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r178_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r178_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r178_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r178_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r178_rwa_elig_cea_not_cov;
	private BigDecimal	r178_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r178_rwa_unsec_cea;
	private BigDecimal	r178_rwa_tot_ris_wei_ass;

	
	
	
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
	public String getR145_exposure_class_off_bal() {
		return r145_exposure_class_off_bal;
	}
	public void setR145_exposure_class_off_bal(String r145_exposure_class_off_bal) {
		this.r145_exposure_class_off_bal = r145_exposure_class_off_bal;
	}
	public BigDecimal getR145_nom_pri_amt() {
		return r145_nom_pri_amt;
	}
	public void setR145_nom_pri_amt(BigDecimal r145_nom_pri_amt) {
		this.r145_nom_pri_amt = r145_nom_pri_amt;
	}
	public BigDecimal getR145_ccf() {
		return r145_ccf;
	}
	public void setR145_ccf(BigDecimal r145_ccf) {
		this.r145_ccf = r145_ccf;
	}
	public BigDecimal getR145_cea() {
		return r145_cea;
	}
	public void setR145_cea(BigDecimal r145_cea) {
		this.r145_cea = r145_cea;
	}
	public BigDecimal getR145_cea_elig_coun_bilt_net() {
		return r145_cea_elig_coun_bilt_net;
	}
	public void setR145_cea_elig_coun_bilt_net(BigDecimal r145_cea_elig_coun_bilt_net) {
		this.r145_cea_elig_coun_bilt_net = r145_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR145_cea_aft_net() {
		return r145_cea_aft_net;
	}
	public void setR145_cea_aft_net(BigDecimal r145_cea_aft_net) {
		this.r145_cea_aft_net = r145_cea_aft_net;
	}
	public BigDecimal getR145_crm_sub_app_cea_elig() {
		return r145_crm_sub_app_cea_elig;
	}
	public void setR145_crm_sub_app_cea_elig(BigDecimal r145_crm_sub_app_cea_elig) {
		this.r145_crm_sub_app_cea_elig = r145_crm_sub_app_cea_elig;
	}
	public BigDecimal getR145_crm_sub_app_non_col_guar_elig() {
		return r145_crm_sub_app_non_col_guar_elig;
	}
	public void setR145_crm_sub_app_non_col_guar_elig(BigDecimal r145_crm_sub_app_non_col_guar_elig) {
		this.r145_crm_sub_app_non_col_guar_elig = r145_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR145_crm_sub_app_non_col_cre_der() {
		return r145_crm_sub_app_non_col_cre_der;
	}
	public void setR145_crm_sub_app_non_col_cre_der(BigDecimal r145_crm_sub_app_non_col_cre_der) {
		this.r145_crm_sub_app_non_col_cre_der = r145_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR145_crm_sub_app_col_elig_cash() {
		return r145_crm_sub_app_col_elig_cash;
	}
	public void setR145_crm_sub_app_col_elig_cash(BigDecimal r145_crm_sub_app_col_elig_cash) {
		this.r145_crm_sub_app_col_elig_cash = r145_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR145_crm_sub_app_col_elig_tre_bills() {
		return r145_crm_sub_app_col_elig_tre_bills;
	}
	public void setR145_crm_sub_app_col_elig_tre_bills(BigDecimal r145_crm_sub_app_col_elig_tre_bills) {
		this.r145_crm_sub_app_col_elig_tre_bills = r145_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR145_crm_sub_app_col_elig_deb_sec() {
		return r145_crm_sub_app_col_elig_deb_sec;
	}
	public void setR145_crm_sub_app_col_elig_deb_sec(BigDecimal r145_crm_sub_app_col_elig_deb_sec) {
		this.r145_crm_sub_app_col_elig_deb_sec = r145_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR145_crm_sub_app_col_elig_euiq() {
		return r145_crm_sub_app_col_elig_euiq;
	}
	public void setR145_crm_sub_app_col_elig_euiq(BigDecimal r145_crm_sub_app_col_elig_euiq) {
		this.r145_crm_sub_app_col_elig_euiq = r145_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR145_crm_sub_app_col_elig_uni_tru() {
		return r145_crm_sub_app_col_elig_uni_tru;
	}
	public void setR145_crm_sub_app_col_elig_uni_tru(BigDecimal r145_crm_sub_app_col_elig_uni_tru) {
		this.r145_crm_sub_app_col_elig_uni_tru = r145_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR145_crm_sub_app_col_cea_cov() {
		return r145_crm_sub_app_col_cea_cov;
	}
	public void setR145_crm_sub_app_col_cea_cov(BigDecimal r145_crm_sub_app_col_cea_cov) {
		this.r145_crm_sub_app_col_cea_cov = r145_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR145_crm_sub_app_col_cea_not_cov() {
		return r145_crm_sub_app_col_cea_not_cov;
	}
	public void setR145_crm_sub_app_col_cea_not_cov(BigDecimal r145_crm_sub_app_col_cea_not_cov) {
		this.r145_crm_sub_app_col_cea_not_cov = r145_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR145_crm_sub_app_rwa_ris_wei_crm() {
		return r145_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR145_crm_sub_app_rwa_ris_wei_crm(BigDecimal r145_crm_sub_app_rwa_ris_wei_crm) {
		this.r145_crm_sub_app_rwa_ris_wei_crm = r145_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR145_crm_sub_app_rwa_ris_cea_cov() {
		return r145_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR145_crm_sub_app_rwa_ris_cea_cov(BigDecimal r145_crm_sub_app_rwa_ris_cea_cov) {
		this.r145_crm_sub_app_rwa_ris_cea_cov = r145_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR145_crm_sub_app_rwa_appl_org_coun() {
		return r145_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR145_crm_sub_app_rwa_appl_org_coun(BigDecimal r145_crm_sub_app_rwa_appl_org_coun) {
		this.r145_crm_sub_app_rwa_appl_org_coun = r145_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR145_crm_sub_app_rwa_ris_cea_not_cov() {
		return r145_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR145_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r145_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r145_crm_sub_app_rwa_ris_cea_not_cov = r145_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR145_crm_com_app_col_cea_elig_crm() {
		return r145_crm_com_app_col_cea_elig_crm;
	}
	public void setR145_crm_com_app_col_cea_elig_crm(BigDecimal r145_crm_com_app_col_cea_elig_crm) {
		this.r145_crm_com_app_col_cea_elig_crm = r145_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR145_crm_com_app_col_elig_cea_vol_adj() {
		return r145_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR145_crm_com_app_col_elig_cea_vol_adj(BigDecimal r145_crm_com_app_col_elig_cea_vol_adj) {
		this.r145_crm_com_app_col_elig_cea_vol_adj = r145_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR145_crm_com_app_col_elig_fin_hai() {
		return r145_crm_com_app_col_elig_fin_hai;
	}
	public void setR145_crm_com_app_col_elig_fin_hai(BigDecimal r145_crm_com_app_col_elig_fin_hai) {
		this.r145_crm_com_app_col_elig_fin_hai = r145_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR145_crm_com_app_col_cea_val_aft_crm() {
		return r145_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR145_crm_com_app_col_cea_val_aft_crm(BigDecimal r145_crm_com_app_col_cea_val_aft_crm) {
		this.r145_crm_com_app_col_cea_val_aft_crm = r145_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR145_rwa_elig_cea_not_cov() {
		return r145_rwa_elig_cea_not_cov;
	}
	public void setR145_rwa_elig_cea_not_cov(BigDecimal r145_rwa_elig_cea_not_cov) {
		this.r145_rwa_elig_cea_not_cov = r145_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR145_rwa_unsec_cea_sub_cre_ris() {
		return r145_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR145_rwa_unsec_cea_sub_cre_ris(BigDecimal r145_rwa_unsec_cea_sub_cre_ris) {
		this.r145_rwa_unsec_cea_sub_cre_ris = r145_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR145_rwa_unsec_cea() {
		return r145_rwa_unsec_cea;
	}
	public void setR145_rwa_unsec_cea(BigDecimal r145_rwa_unsec_cea) {
		this.r145_rwa_unsec_cea = r145_rwa_unsec_cea;
	}
	public BigDecimal getR145_rwa_tot_ris_wei_ass() {
		return r145_rwa_tot_ris_wei_ass;
	}
	public void setR145_rwa_tot_ris_wei_ass(BigDecimal r145_rwa_tot_ris_wei_ass) {
		this.r145_rwa_tot_ris_wei_ass = r145_rwa_tot_ris_wei_ass;
	}
	public String getR146_exposure_class_off_bal() {
		return r146_exposure_class_off_bal;
	}
	public void setR146_exposure_class_off_bal(String r146_exposure_class_off_bal) {
		this.r146_exposure_class_off_bal = r146_exposure_class_off_bal;
	}
	public BigDecimal getR146_nom_pri_amt() {
		return r146_nom_pri_amt;
	}
	public void setR146_nom_pri_amt(BigDecimal r146_nom_pri_amt) {
		this.r146_nom_pri_amt = r146_nom_pri_amt;
	}
	public BigDecimal getR146_ccf() {
		return r146_ccf;
	}
	public void setR146_ccf(BigDecimal r146_ccf) {
		this.r146_ccf = r146_ccf;
	}
	public BigDecimal getR146_cea() {
		return r146_cea;
	}
	public void setR146_cea(BigDecimal r146_cea) {
		this.r146_cea = r146_cea;
	}
	public BigDecimal getR146_cea_elig_coun_bilt_net() {
		return r146_cea_elig_coun_bilt_net;
	}
	public void setR146_cea_elig_coun_bilt_net(BigDecimal r146_cea_elig_coun_bilt_net) {
		this.r146_cea_elig_coun_bilt_net = r146_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR146_cea_aft_net() {
		return r146_cea_aft_net;
	}
	public void setR146_cea_aft_net(BigDecimal r146_cea_aft_net) {
		this.r146_cea_aft_net = r146_cea_aft_net;
	}
	public BigDecimal getR146_crm_sub_app_cea_elig() {
		return r146_crm_sub_app_cea_elig;
	}
	public void setR146_crm_sub_app_cea_elig(BigDecimal r146_crm_sub_app_cea_elig) {
		this.r146_crm_sub_app_cea_elig = r146_crm_sub_app_cea_elig;
	}
	public BigDecimal getR146_crm_sub_app_non_col_guar_elig() {
		return r146_crm_sub_app_non_col_guar_elig;
	}
	public void setR146_crm_sub_app_non_col_guar_elig(BigDecimal r146_crm_sub_app_non_col_guar_elig) {
		this.r146_crm_sub_app_non_col_guar_elig = r146_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR146_crm_sub_app_non_col_cre_der() {
		return r146_crm_sub_app_non_col_cre_der;
	}
	public void setR146_crm_sub_app_non_col_cre_der(BigDecimal r146_crm_sub_app_non_col_cre_der) {
		this.r146_crm_sub_app_non_col_cre_der = r146_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR146_crm_sub_app_col_elig_cash() {
		return r146_crm_sub_app_col_elig_cash;
	}
	public void setR146_crm_sub_app_col_elig_cash(BigDecimal r146_crm_sub_app_col_elig_cash) {
		this.r146_crm_sub_app_col_elig_cash = r146_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR146_crm_sub_app_col_elig_tre_bills() {
		return r146_crm_sub_app_col_elig_tre_bills;
	}
	public void setR146_crm_sub_app_col_elig_tre_bills(BigDecimal r146_crm_sub_app_col_elig_tre_bills) {
		this.r146_crm_sub_app_col_elig_tre_bills = r146_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR146_crm_sub_app_col_elig_deb_sec() {
		return r146_crm_sub_app_col_elig_deb_sec;
	}
	public void setR146_crm_sub_app_col_elig_deb_sec(BigDecimal r146_crm_sub_app_col_elig_deb_sec) {
		this.r146_crm_sub_app_col_elig_deb_sec = r146_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR146_crm_sub_app_col_elig_euiq() {
		return r146_crm_sub_app_col_elig_euiq;
	}
	public void setR146_crm_sub_app_col_elig_euiq(BigDecimal r146_crm_sub_app_col_elig_euiq) {
		this.r146_crm_sub_app_col_elig_euiq = r146_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR146_crm_sub_app_col_elig_uni_tru() {
		return r146_crm_sub_app_col_elig_uni_tru;
	}
	public void setR146_crm_sub_app_col_elig_uni_tru(BigDecimal r146_crm_sub_app_col_elig_uni_tru) {
		this.r146_crm_sub_app_col_elig_uni_tru = r146_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR146_crm_sub_app_col_cea_cov() {
		return r146_crm_sub_app_col_cea_cov;
	}
	public void setR146_crm_sub_app_col_cea_cov(BigDecimal r146_crm_sub_app_col_cea_cov) {
		this.r146_crm_sub_app_col_cea_cov = r146_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR146_crm_sub_app_col_cea_not_cov() {
		return r146_crm_sub_app_col_cea_not_cov;
	}
	public void setR146_crm_sub_app_col_cea_not_cov(BigDecimal r146_crm_sub_app_col_cea_not_cov) {
		this.r146_crm_sub_app_col_cea_not_cov = r146_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR146_crm_sub_app_rwa_ris_wei_crm() {
		return r146_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR146_crm_sub_app_rwa_ris_wei_crm(BigDecimal r146_crm_sub_app_rwa_ris_wei_crm) {
		this.r146_crm_sub_app_rwa_ris_wei_crm = r146_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR146_crm_sub_app_rwa_ris_cea_cov() {
		return r146_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR146_crm_sub_app_rwa_ris_cea_cov(BigDecimal r146_crm_sub_app_rwa_ris_cea_cov) {
		this.r146_crm_sub_app_rwa_ris_cea_cov = r146_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR146_crm_sub_app_rwa_appl_org_coun() {
		return r146_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR146_crm_sub_app_rwa_appl_org_coun(BigDecimal r146_crm_sub_app_rwa_appl_org_coun) {
		this.r146_crm_sub_app_rwa_appl_org_coun = r146_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR146_crm_sub_app_rwa_ris_cea_not_cov() {
		return r146_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR146_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r146_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r146_crm_sub_app_rwa_ris_cea_not_cov = r146_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR146_crm_com_app_col_cea_elig_crm() {
		return r146_crm_com_app_col_cea_elig_crm;
	}
	public void setR146_crm_com_app_col_cea_elig_crm(BigDecimal r146_crm_com_app_col_cea_elig_crm) {
		this.r146_crm_com_app_col_cea_elig_crm = r146_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR146_crm_com_app_col_elig_cea_vol_adj() {
		return r146_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR146_crm_com_app_col_elig_cea_vol_adj(BigDecimal r146_crm_com_app_col_elig_cea_vol_adj) {
		this.r146_crm_com_app_col_elig_cea_vol_adj = r146_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR146_crm_com_app_col_elig_fin_hai() {
		return r146_crm_com_app_col_elig_fin_hai;
	}
	public void setR146_crm_com_app_col_elig_fin_hai(BigDecimal r146_crm_com_app_col_elig_fin_hai) {
		this.r146_crm_com_app_col_elig_fin_hai = r146_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR146_crm_com_app_col_cea_val_aft_crm() {
		return r146_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR146_crm_com_app_col_cea_val_aft_crm(BigDecimal r146_crm_com_app_col_cea_val_aft_crm) {
		this.r146_crm_com_app_col_cea_val_aft_crm = r146_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR146_rwa_elig_cea_not_cov() {
		return r146_rwa_elig_cea_not_cov;
	}
	public void setR146_rwa_elig_cea_not_cov(BigDecimal r146_rwa_elig_cea_not_cov) {
		this.r146_rwa_elig_cea_not_cov = r146_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR146_rwa_unsec_cea_sub_cre_ris() {
		return r146_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR146_rwa_unsec_cea_sub_cre_ris(BigDecimal r146_rwa_unsec_cea_sub_cre_ris) {
		this.r146_rwa_unsec_cea_sub_cre_ris = r146_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR146_rwa_unsec_cea() {
		return r146_rwa_unsec_cea;
	}
	public void setR146_rwa_unsec_cea(BigDecimal r146_rwa_unsec_cea) {
		this.r146_rwa_unsec_cea = r146_rwa_unsec_cea;
	}
	public BigDecimal getR146_rwa_tot_ris_wei_ass() {
		return r146_rwa_tot_ris_wei_ass;
	}
	public void setR146_rwa_tot_ris_wei_ass(BigDecimal r146_rwa_tot_ris_wei_ass) {
		this.r146_rwa_tot_ris_wei_ass = r146_rwa_tot_ris_wei_ass;
	}
	public String getR147_exposure_class_off_bal() {
		return r147_exposure_class_off_bal;
	}
	public void setR147_exposure_class_off_bal(String r147_exposure_class_off_bal) {
		this.r147_exposure_class_off_bal = r147_exposure_class_off_bal;
	}
	public BigDecimal getR147_nom_pri_amt() {
		return r147_nom_pri_amt;
	}
	public void setR147_nom_pri_amt(BigDecimal r147_nom_pri_amt) {
		this.r147_nom_pri_amt = r147_nom_pri_amt;
	}
	public BigDecimal getR147_ccf() {
		return r147_ccf;
	}
	public void setR147_ccf(BigDecimal r147_ccf) {
		this.r147_ccf = r147_ccf;
	}
	public BigDecimal getR147_cea() {
		return r147_cea;
	}
	public void setR147_cea(BigDecimal r147_cea) {
		this.r147_cea = r147_cea;
	}
	public BigDecimal getR147_cea_elig_coun_bilt_net() {
		return r147_cea_elig_coun_bilt_net;
	}
	public void setR147_cea_elig_coun_bilt_net(BigDecimal r147_cea_elig_coun_bilt_net) {
		this.r147_cea_elig_coun_bilt_net = r147_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR147_cea_aft_net() {
		return r147_cea_aft_net;
	}
	public void setR147_cea_aft_net(BigDecimal r147_cea_aft_net) {
		this.r147_cea_aft_net = r147_cea_aft_net;
	}
	public BigDecimal getR147_crm_sub_app_cea_elig() {
		return r147_crm_sub_app_cea_elig;
	}
	public void setR147_crm_sub_app_cea_elig(BigDecimal r147_crm_sub_app_cea_elig) {
		this.r147_crm_sub_app_cea_elig = r147_crm_sub_app_cea_elig;
	}
	public BigDecimal getR147_crm_sub_app_non_col_guar_elig() {
		return r147_crm_sub_app_non_col_guar_elig;
	}
	public void setR147_crm_sub_app_non_col_guar_elig(BigDecimal r147_crm_sub_app_non_col_guar_elig) {
		this.r147_crm_sub_app_non_col_guar_elig = r147_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR147_crm_sub_app_non_col_cre_der() {
		return r147_crm_sub_app_non_col_cre_der;
	}
	public void setR147_crm_sub_app_non_col_cre_der(BigDecimal r147_crm_sub_app_non_col_cre_der) {
		this.r147_crm_sub_app_non_col_cre_der = r147_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR147_crm_sub_app_col_elig_cash() {
		return r147_crm_sub_app_col_elig_cash;
	}
	public void setR147_crm_sub_app_col_elig_cash(BigDecimal r147_crm_sub_app_col_elig_cash) {
		this.r147_crm_sub_app_col_elig_cash = r147_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR147_crm_sub_app_col_elig_tre_bills() {
		return r147_crm_sub_app_col_elig_tre_bills;
	}
	public void setR147_crm_sub_app_col_elig_tre_bills(BigDecimal r147_crm_sub_app_col_elig_tre_bills) {
		this.r147_crm_sub_app_col_elig_tre_bills = r147_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR147_crm_sub_app_col_elig_deb_sec() {
		return r147_crm_sub_app_col_elig_deb_sec;
	}
	public void setR147_crm_sub_app_col_elig_deb_sec(BigDecimal r147_crm_sub_app_col_elig_deb_sec) {
		this.r147_crm_sub_app_col_elig_deb_sec = r147_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR147_crm_sub_app_col_elig_euiq() {
		return r147_crm_sub_app_col_elig_euiq;
	}
	public void setR147_crm_sub_app_col_elig_euiq(BigDecimal r147_crm_sub_app_col_elig_euiq) {
		this.r147_crm_sub_app_col_elig_euiq = r147_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR147_crm_sub_app_col_elig_uni_tru() {
		return r147_crm_sub_app_col_elig_uni_tru;
	}
	public void setR147_crm_sub_app_col_elig_uni_tru(BigDecimal r147_crm_sub_app_col_elig_uni_tru) {
		this.r147_crm_sub_app_col_elig_uni_tru = r147_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR147_crm_sub_app_col_cea_cov() {
		return r147_crm_sub_app_col_cea_cov;
	}
	public void setR147_crm_sub_app_col_cea_cov(BigDecimal r147_crm_sub_app_col_cea_cov) {
		this.r147_crm_sub_app_col_cea_cov = r147_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR147_crm_sub_app_col_cea_not_cov() {
		return r147_crm_sub_app_col_cea_not_cov;
	}
	public void setR147_crm_sub_app_col_cea_not_cov(BigDecimal r147_crm_sub_app_col_cea_not_cov) {
		this.r147_crm_sub_app_col_cea_not_cov = r147_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR147_crm_sub_app_rwa_ris_wei_crm() {
		return r147_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR147_crm_sub_app_rwa_ris_wei_crm(BigDecimal r147_crm_sub_app_rwa_ris_wei_crm) {
		this.r147_crm_sub_app_rwa_ris_wei_crm = r147_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR147_crm_sub_app_rwa_ris_cea_cov() {
		return r147_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR147_crm_sub_app_rwa_ris_cea_cov(BigDecimal r147_crm_sub_app_rwa_ris_cea_cov) {
		this.r147_crm_sub_app_rwa_ris_cea_cov = r147_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR147_crm_sub_app_rwa_appl_org_coun() {
		return r147_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR147_crm_sub_app_rwa_appl_org_coun(BigDecimal r147_crm_sub_app_rwa_appl_org_coun) {
		this.r147_crm_sub_app_rwa_appl_org_coun = r147_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR147_crm_sub_app_rwa_ris_cea_not_cov() {
		return r147_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR147_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r147_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r147_crm_sub_app_rwa_ris_cea_not_cov = r147_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR147_crm_com_app_col_cea_elig_crm() {
		return r147_crm_com_app_col_cea_elig_crm;
	}
	public void setR147_crm_com_app_col_cea_elig_crm(BigDecimal r147_crm_com_app_col_cea_elig_crm) {
		this.r147_crm_com_app_col_cea_elig_crm = r147_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR147_crm_com_app_col_elig_cea_vol_adj() {
		return r147_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR147_crm_com_app_col_elig_cea_vol_adj(BigDecimal r147_crm_com_app_col_elig_cea_vol_adj) {
		this.r147_crm_com_app_col_elig_cea_vol_adj = r147_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR147_crm_com_app_col_elig_fin_hai() {
		return r147_crm_com_app_col_elig_fin_hai;
	}
	public void setR147_crm_com_app_col_elig_fin_hai(BigDecimal r147_crm_com_app_col_elig_fin_hai) {
		this.r147_crm_com_app_col_elig_fin_hai = r147_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR147_crm_com_app_col_cea_val_aft_crm() {
		return r147_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR147_crm_com_app_col_cea_val_aft_crm(BigDecimal r147_crm_com_app_col_cea_val_aft_crm) {
		this.r147_crm_com_app_col_cea_val_aft_crm = r147_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR147_rwa_elig_cea_not_cov() {
		return r147_rwa_elig_cea_not_cov;
	}
	public void setR147_rwa_elig_cea_not_cov(BigDecimal r147_rwa_elig_cea_not_cov) {
		this.r147_rwa_elig_cea_not_cov = r147_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR147_rwa_unsec_cea_sub_cre_ris() {
		return r147_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR147_rwa_unsec_cea_sub_cre_ris(BigDecimal r147_rwa_unsec_cea_sub_cre_ris) {
		this.r147_rwa_unsec_cea_sub_cre_ris = r147_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR147_rwa_unsec_cea() {
		return r147_rwa_unsec_cea;
	}
	public void setR147_rwa_unsec_cea(BigDecimal r147_rwa_unsec_cea) {
		this.r147_rwa_unsec_cea = r147_rwa_unsec_cea;
	}
	public BigDecimal getR147_rwa_tot_ris_wei_ass() {
		return r147_rwa_tot_ris_wei_ass;
	}
	public void setR147_rwa_tot_ris_wei_ass(BigDecimal r147_rwa_tot_ris_wei_ass) {
		this.r147_rwa_tot_ris_wei_ass = r147_rwa_tot_ris_wei_ass;
	}
	public String getR148_exposure_class_off_bal() {
		return r148_exposure_class_off_bal;
	}
	public void setR148_exposure_class_off_bal(String r148_exposure_class_off_bal) {
		this.r148_exposure_class_off_bal = r148_exposure_class_off_bal;
	}
	public BigDecimal getR148_nom_pri_amt() {
		return r148_nom_pri_amt;
	}
	public void setR148_nom_pri_amt(BigDecimal r148_nom_pri_amt) {
		this.r148_nom_pri_amt = r148_nom_pri_amt;
	}
	public BigDecimal getR148_ccf() {
		return r148_ccf;
	}
	public void setR148_ccf(BigDecimal r148_ccf) {
		this.r148_ccf = r148_ccf;
	}
	public BigDecimal getR148_cea() {
		return r148_cea;
	}
	public void setR148_cea(BigDecimal r148_cea) {
		this.r148_cea = r148_cea;
	}
	public BigDecimal getR148_cea_elig_coun_bilt_net() {
		return r148_cea_elig_coun_bilt_net;
	}
	public void setR148_cea_elig_coun_bilt_net(BigDecimal r148_cea_elig_coun_bilt_net) {
		this.r148_cea_elig_coun_bilt_net = r148_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR148_cea_aft_net() {
		return r148_cea_aft_net;
	}
	public void setR148_cea_aft_net(BigDecimal r148_cea_aft_net) {
		this.r148_cea_aft_net = r148_cea_aft_net;
	}
	public BigDecimal getR148_crm_sub_app_cea_elig() {
		return r148_crm_sub_app_cea_elig;
	}
	public void setR148_crm_sub_app_cea_elig(BigDecimal r148_crm_sub_app_cea_elig) {
		this.r148_crm_sub_app_cea_elig = r148_crm_sub_app_cea_elig;
	}
	public BigDecimal getR148_crm_sub_app_non_col_guar_elig() {
		return r148_crm_sub_app_non_col_guar_elig;
	}
	public void setR148_crm_sub_app_non_col_guar_elig(BigDecimal r148_crm_sub_app_non_col_guar_elig) {
		this.r148_crm_sub_app_non_col_guar_elig = r148_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR148_crm_sub_app_non_col_cre_der() {
		return r148_crm_sub_app_non_col_cre_der;
	}
	public void setR148_crm_sub_app_non_col_cre_der(BigDecimal r148_crm_sub_app_non_col_cre_der) {
		this.r148_crm_sub_app_non_col_cre_der = r148_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR148_crm_sub_app_col_elig_cash() {
		return r148_crm_sub_app_col_elig_cash;
	}
	public void setR148_crm_sub_app_col_elig_cash(BigDecimal r148_crm_sub_app_col_elig_cash) {
		this.r148_crm_sub_app_col_elig_cash = r148_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR148_crm_sub_app_col_elig_tre_bills() {
		return r148_crm_sub_app_col_elig_tre_bills;
	}
	public void setR148_crm_sub_app_col_elig_tre_bills(BigDecimal r148_crm_sub_app_col_elig_tre_bills) {
		this.r148_crm_sub_app_col_elig_tre_bills = r148_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR148_crm_sub_app_col_elig_deb_sec() {
		return r148_crm_sub_app_col_elig_deb_sec;
	}
	public void setR148_crm_sub_app_col_elig_deb_sec(BigDecimal r148_crm_sub_app_col_elig_deb_sec) {
		this.r148_crm_sub_app_col_elig_deb_sec = r148_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR148_crm_sub_app_col_elig_euiq() {
		return r148_crm_sub_app_col_elig_euiq;
	}
	public void setR148_crm_sub_app_col_elig_euiq(BigDecimal r148_crm_sub_app_col_elig_euiq) {
		this.r148_crm_sub_app_col_elig_euiq = r148_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR148_crm_sub_app_col_elig_uni_tru() {
		return r148_crm_sub_app_col_elig_uni_tru;
	}
	public void setR148_crm_sub_app_col_elig_uni_tru(BigDecimal r148_crm_sub_app_col_elig_uni_tru) {
		this.r148_crm_sub_app_col_elig_uni_tru = r148_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR148_crm_sub_app_col_cea_cov() {
		return r148_crm_sub_app_col_cea_cov;
	}
	public void setR148_crm_sub_app_col_cea_cov(BigDecimal r148_crm_sub_app_col_cea_cov) {
		this.r148_crm_sub_app_col_cea_cov = r148_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR148_crm_sub_app_col_cea_not_cov() {
		return r148_crm_sub_app_col_cea_not_cov;
	}
	public void setR148_crm_sub_app_col_cea_not_cov(BigDecimal r148_crm_sub_app_col_cea_not_cov) {
		this.r148_crm_sub_app_col_cea_not_cov = r148_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR148_crm_sub_app_rwa_ris_wei_crm() {
		return r148_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR148_crm_sub_app_rwa_ris_wei_crm(BigDecimal r148_crm_sub_app_rwa_ris_wei_crm) {
		this.r148_crm_sub_app_rwa_ris_wei_crm = r148_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR148_crm_sub_app_rwa_ris_cea_cov() {
		return r148_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR148_crm_sub_app_rwa_ris_cea_cov(BigDecimal r148_crm_sub_app_rwa_ris_cea_cov) {
		this.r148_crm_sub_app_rwa_ris_cea_cov = r148_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR148_crm_sub_app_rwa_appl_org_coun() {
		return r148_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR148_crm_sub_app_rwa_appl_org_coun(BigDecimal r148_crm_sub_app_rwa_appl_org_coun) {
		this.r148_crm_sub_app_rwa_appl_org_coun = r148_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR148_crm_sub_app_rwa_ris_cea_not_cov() {
		return r148_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR148_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r148_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r148_crm_sub_app_rwa_ris_cea_not_cov = r148_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR148_crm_com_app_col_cea_elig_crm() {
		return r148_crm_com_app_col_cea_elig_crm;
	}
	public void setR148_crm_com_app_col_cea_elig_crm(BigDecimal r148_crm_com_app_col_cea_elig_crm) {
		this.r148_crm_com_app_col_cea_elig_crm = r148_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR148_crm_com_app_col_elig_cea_vol_adj() {
		return r148_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR148_crm_com_app_col_elig_cea_vol_adj(BigDecimal r148_crm_com_app_col_elig_cea_vol_adj) {
		this.r148_crm_com_app_col_elig_cea_vol_adj = r148_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR148_crm_com_app_col_elig_fin_hai() {
		return r148_crm_com_app_col_elig_fin_hai;
	}
	public void setR148_crm_com_app_col_elig_fin_hai(BigDecimal r148_crm_com_app_col_elig_fin_hai) {
		this.r148_crm_com_app_col_elig_fin_hai = r148_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR148_crm_com_app_col_cea_val_aft_crm() {
		return r148_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR148_crm_com_app_col_cea_val_aft_crm(BigDecimal r148_crm_com_app_col_cea_val_aft_crm) {
		this.r148_crm_com_app_col_cea_val_aft_crm = r148_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR148_rwa_elig_cea_not_cov() {
		return r148_rwa_elig_cea_not_cov;
	}
	public void setR148_rwa_elig_cea_not_cov(BigDecimal r148_rwa_elig_cea_not_cov) {
		this.r148_rwa_elig_cea_not_cov = r148_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR148_rwa_unsec_cea_sub_cre_ris() {
		return r148_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR148_rwa_unsec_cea_sub_cre_ris(BigDecimal r148_rwa_unsec_cea_sub_cre_ris) {
		this.r148_rwa_unsec_cea_sub_cre_ris = r148_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR148_rwa_unsec_cea() {
		return r148_rwa_unsec_cea;
	}
	public void setR148_rwa_unsec_cea(BigDecimal r148_rwa_unsec_cea) {
		this.r148_rwa_unsec_cea = r148_rwa_unsec_cea;
	}
	public BigDecimal getR148_rwa_tot_ris_wei_ass() {
		return r148_rwa_tot_ris_wei_ass;
	}
	public void setR148_rwa_tot_ris_wei_ass(BigDecimal r148_rwa_tot_ris_wei_ass) {
		this.r148_rwa_tot_ris_wei_ass = r148_rwa_tot_ris_wei_ass;
	}
	public String getR149_exposure_class_off_bal() {
		return r149_exposure_class_off_bal;
	}
	public void setR149_exposure_class_off_bal(String r149_exposure_class_off_bal) {
		this.r149_exposure_class_off_bal = r149_exposure_class_off_bal;
	}
	public BigDecimal getR149_nom_pri_amt() {
		return r149_nom_pri_amt;
	}
	public void setR149_nom_pri_amt(BigDecimal r149_nom_pri_amt) {
		this.r149_nom_pri_amt = r149_nom_pri_amt;
	}
	public BigDecimal getR149_ccf() {
		return r149_ccf;
	}
	public void setR149_ccf(BigDecimal r149_ccf) {
		this.r149_ccf = r149_ccf;
	}
	public BigDecimal getR149_cea() {
		return r149_cea;
	}
	public void setR149_cea(BigDecimal r149_cea) {
		this.r149_cea = r149_cea;
	}
	public BigDecimal getR149_cea_elig_coun_bilt_net() {
		return r149_cea_elig_coun_bilt_net;
	}
	public void setR149_cea_elig_coun_bilt_net(BigDecimal r149_cea_elig_coun_bilt_net) {
		this.r149_cea_elig_coun_bilt_net = r149_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR149_cea_aft_net() {
		return r149_cea_aft_net;
	}
	public void setR149_cea_aft_net(BigDecimal r149_cea_aft_net) {
		this.r149_cea_aft_net = r149_cea_aft_net;
	}
	public BigDecimal getR149_crm_sub_app_cea_elig() {
		return r149_crm_sub_app_cea_elig;
	}
	public void setR149_crm_sub_app_cea_elig(BigDecimal r149_crm_sub_app_cea_elig) {
		this.r149_crm_sub_app_cea_elig = r149_crm_sub_app_cea_elig;
	}
	public BigDecimal getR149_crm_sub_app_non_col_guar_elig() {
		return r149_crm_sub_app_non_col_guar_elig;
	}
	public void setR149_crm_sub_app_non_col_guar_elig(BigDecimal r149_crm_sub_app_non_col_guar_elig) {
		this.r149_crm_sub_app_non_col_guar_elig = r149_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR149_crm_sub_app_non_col_cre_der() {
		return r149_crm_sub_app_non_col_cre_der;
	}
	public void setR149_crm_sub_app_non_col_cre_der(BigDecimal r149_crm_sub_app_non_col_cre_der) {
		this.r149_crm_sub_app_non_col_cre_der = r149_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR149_crm_sub_app_col_elig_cash() {
		return r149_crm_sub_app_col_elig_cash;
	}
	public void setR149_crm_sub_app_col_elig_cash(BigDecimal r149_crm_sub_app_col_elig_cash) {
		this.r149_crm_sub_app_col_elig_cash = r149_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR149_crm_sub_app_col_elig_tre_bills() {
		return r149_crm_sub_app_col_elig_tre_bills;
	}
	public void setR149_crm_sub_app_col_elig_tre_bills(BigDecimal r149_crm_sub_app_col_elig_tre_bills) {
		this.r149_crm_sub_app_col_elig_tre_bills = r149_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR149_crm_sub_app_col_elig_deb_sec() {
		return r149_crm_sub_app_col_elig_deb_sec;
	}
	public void setR149_crm_sub_app_col_elig_deb_sec(BigDecimal r149_crm_sub_app_col_elig_deb_sec) {
		this.r149_crm_sub_app_col_elig_deb_sec = r149_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR149_crm_sub_app_col_elig_euiq() {
		return r149_crm_sub_app_col_elig_euiq;
	}
	public void setR149_crm_sub_app_col_elig_euiq(BigDecimal r149_crm_sub_app_col_elig_euiq) {
		this.r149_crm_sub_app_col_elig_euiq = r149_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR149_crm_sub_app_col_elig_uni_tru() {
		return r149_crm_sub_app_col_elig_uni_tru;
	}
	public void setR149_crm_sub_app_col_elig_uni_tru(BigDecimal r149_crm_sub_app_col_elig_uni_tru) {
		this.r149_crm_sub_app_col_elig_uni_tru = r149_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR149_crm_sub_app_col_cea_cov() {
		return r149_crm_sub_app_col_cea_cov;
	}
	public void setR149_crm_sub_app_col_cea_cov(BigDecimal r149_crm_sub_app_col_cea_cov) {
		this.r149_crm_sub_app_col_cea_cov = r149_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR149_crm_sub_app_col_cea_not_cov() {
		return r149_crm_sub_app_col_cea_not_cov;
	}
	public void setR149_crm_sub_app_col_cea_not_cov(BigDecimal r149_crm_sub_app_col_cea_not_cov) {
		this.r149_crm_sub_app_col_cea_not_cov = r149_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR149_crm_sub_app_rwa_ris_wei_crm() {
		return r149_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR149_crm_sub_app_rwa_ris_wei_crm(BigDecimal r149_crm_sub_app_rwa_ris_wei_crm) {
		this.r149_crm_sub_app_rwa_ris_wei_crm = r149_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR149_crm_sub_app_rwa_ris_cea_cov() {
		return r149_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR149_crm_sub_app_rwa_ris_cea_cov(BigDecimal r149_crm_sub_app_rwa_ris_cea_cov) {
		this.r149_crm_sub_app_rwa_ris_cea_cov = r149_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR149_crm_sub_app_rwa_appl_org_coun() {
		return r149_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR149_crm_sub_app_rwa_appl_org_coun(BigDecimal r149_crm_sub_app_rwa_appl_org_coun) {
		this.r149_crm_sub_app_rwa_appl_org_coun = r149_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR149_crm_sub_app_rwa_ris_cea_not_cov() {
		return r149_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR149_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r149_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r149_crm_sub_app_rwa_ris_cea_not_cov = r149_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR149_crm_com_app_col_cea_elig_crm() {
		return r149_crm_com_app_col_cea_elig_crm;
	}
	public void setR149_crm_com_app_col_cea_elig_crm(BigDecimal r149_crm_com_app_col_cea_elig_crm) {
		this.r149_crm_com_app_col_cea_elig_crm = r149_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR149_crm_com_app_col_elig_cea_vol_adj() {
		return r149_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR149_crm_com_app_col_elig_cea_vol_adj(BigDecimal r149_crm_com_app_col_elig_cea_vol_adj) {
		this.r149_crm_com_app_col_elig_cea_vol_adj = r149_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR149_crm_com_app_col_elig_fin_hai() {
		return r149_crm_com_app_col_elig_fin_hai;
	}
	public void setR149_crm_com_app_col_elig_fin_hai(BigDecimal r149_crm_com_app_col_elig_fin_hai) {
		this.r149_crm_com_app_col_elig_fin_hai = r149_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR149_crm_com_app_col_cea_val_aft_crm() {
		return r149_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR149_crm_com_app_col_cea_val_aft_crm(BigDecimal r149_crm_com_app_col_cea_val_aft_crm) {
		this.r149_crm_com_app_col_cea_val_aft_crm = r149_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR149_rwa_elig_cea_not_cov() {
		return r149_rwa_elig_cea_not_cov;
	}
	public void setR149_rwa_elig_cea_not_cov(BigDecimal r149_rwa_elig_cea_not_cov) {
		this.r149_rwa_elig_cea_not_cov = r149_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR149_rwa_unsec_cea_sub_cre_ris() {
		return r149_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR149_rwa_unsec_cea_sub_cre_ris(BigDecimal r149_rwa_unsec_cea_sub_cre_ris) {
		this.r149_rwa_unsec_cea_sub_cre_ris = r149_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR149_rwa_unsec_cea() {
		return r149_rwa_unsec_cea;
	}
	public void setR149_rwa_unsec_cea(BigDecimal r149_rwa_unsec_cea) {
		this.r149_rwa_unsec_cea = r149_rwa_unsec_cea;
	}
	public BigDecimal getR149_rwa_tot_ris_wei_ass() {
		return r149_rwa_tot_ris_wei_ass;
	}
	public void setR149_rwa_tot_ris_wei_ass(BigDecimal r149_rwa_tot_ris_wei_ass) {
		this.r149_rwa_tot_ris_wei_ass = r149_rwa_tot_ris_wei_ass;
	}
	public String getR150_exposure_class_off_bal() {
		return r150_exposure_class_off_bal;
	}
	public void setR150_exposure_class_off_bal(String r150_exposure_class_off_bal) {
		this.r150_exposure_class_off_bal = r150_exposure_class_off_bal;
	}
	public BigDecimal getR150_nom_pri_amt() {
		return r150_nom_pri_amt;
	}
	public void setR150_nom_pri_amt(BigDecimal r150_nom_pri_amt) {
		this.r150_nom_pri_amt = r150_nom_pri_amt;
	}
	public BigDecimal getR150_ccf() {
		return r150_ccf;
	}
	public void setR150_ccf(BigDecimal r150_ccf) {
		this.r150_ccf = r150_ccf;
	}
	public BigDecimal getR150_cea() {
		return r150_cea;
	}
	public void setR150_cea(BigDecimal r150_cea) {
		this.r150_cea = r150_cea;
	}
	public BigDecimal getR150_cea_elig_coun_bilt_net() {
		return r150_cea_elig_coun_bilt_net;
	}
	public void setR150_cea_elig_coun_bilt_net(BigDecimal r150_cea_elig_coun_bilt_net) {
		this.r150_cea_elig_coun_bilt_net = r150_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR150_cea_aft_net() {
		return r150_cea_aft_net;
	}
	public void setR150_cea_aft_net(BigDecimal r150_cea_aft_net) {
		this.r150_cea_aft_net = r150_cea_aft_net;
	}
	public BigDecimal getR150_crm_sub_app_cea_elig() {
		return r150_crm_sub_app_cea_elig;
	}
	public void setR150_crm_sub_app_cea_elig(BigDecimal r150_crm_sub_app_cea_elig) {
		this.r150_crm_sub_app_cea_elig = r150_crm_sub_app_cea_elig;
	}
	public BigDecimal getR150_crm_sub_app_non_col_guar_elig() {
		return r150_crm_sub_app_non_col_guar_elig;
	}
	public void setR150_crm_sub_app_non_col_guar_elig(BigDecimal r150_crm_sub_app_non_col_guar_elig) {
		this.r150_crm_sub_app_non_col_guar_elig = r150_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR150_crm_sub_app_non_col_cre_der() {
		return r150_crm_sub_app_non_col_cre_der;
	}
	public void setR150_crm_sub_app_non_col_cre_der(BigDecimal r150_crm_sub_app_non_col_cre_der) {
		this.r150_crm_sub_app_non_col_cre_der = r150_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR150_crm_sub_app_col_elig_cash() {
		return r150_crm_sub_app_col_elig_cash;
	}
	public void setR150_crm_sub_app_col_elig_cash(BigDecimal r150_crm_sub_app_col_elig_cash) {
		this.r150_crm_sub_app_col_elig_cash = r150_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR150_crm_sub_app_col_elig_tre_bills() {
		return r150_crm_sub_app_col_elig_tre_bills;
	}
	public void setR150_crm_sub_app_col_elig_tre_bills(BigDecimal r150_crm_sub_app_col_elig_tre_bills) {
		this.r150_crm_sub_app_col_elig_tre_bills = r150_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR150_crm_sub_app_col_elig_deb_sec() {
		return r150_crm_sub_app_col_elig_deb_sec;
	}
	public void setR150_crm_sub_app_col_elig_deb_sec(BigDecimal r150_crm_sub_app_col_elig_deb_sec) {
		this.r150_crm_sub_app_col_elig_deb_sec = r150_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR150_crm_sub_app_col_elig_euiq() {
		return r150_crm_sub_app_col_elig_euiq;
	}
	public void setR150_crm_sub_app_col_elig_euiq(BigDecimal r150_crm_sub_app_col_elig_euiq) {
		this.r150_crm_sub_app_col_elig_euiq = r150_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR150_crm_sub_app_col_elig_uni_tru() {
		return r150_crm_sub_app_col_elig_uni_tru;
	}
	public void setR150_crm_sub_app_col_elig_uni_tru(BigDecimal r150_crm_sub_app_col_elig_uni_tru) {
		this.r150_crm_sub_app_col_elig_uni_tru = r150_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR150_crm_sub_app_col_cea_cov() {
		return r150_crm_sub_app_col_cea_cov;
	}
	public void setR150_crm_sub_app_col_cea_cov(BigDecimal r150_crm_sub_app_col_cea_cov) {
		this.r150_crm_sub_app_col_cea_cov = r150_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR150_crm_sub_app_col_cea_not_cov() {
		return r150_crm_sub_app_col_cea_not_cov;
	}
	public void setR150_crm_sub_app_col_cea_not_cov(BigDecimal r150_crm_sub_app_col_cea_not_cov) {
		this.r150_crm_sub_app_col_cea_not_cov = r150_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR150_crm_sub_app_rwa_ris_wei_crm() {
		return r150_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR150_crm_sub_app_rwa_ris_wei_crm(BigDecimal r150_crm_sub_app_rwa_ris_wei_crm) {
		this.r150_crm_sub_app_rwa_ris_wei_crm = r150_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR150_crm_sub_app_rwa_ris_cea_cov() {
		return r150_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR150_crm_sub_app_rwa_ris_cea_cov(BigDecimal r150_crm_sub_app_rwa_ris_cea_cov) {
		this.r150_crm_sub_app_rwa_ris_cea_cov = r150_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR150_crm_sub_app_rwa_appl_org_coun() {
		return r150_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR150_crm_sub_app_rwa_appl_org_coun(BigDecimal r150_crm_sub_app_rwa_appl_org_coun) {
		this.r150_crm_sub_app_rwa_appl_org_coun = r150_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR150_crm_sub_app_rwa_ris_cea_not_cov() {
		return r150_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR150_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r150_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r150_crm_sub_app_rwa_ris_cea_not_cov = r150_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR150_crm_com_app_col_cea_elig_crm() {
		return r150_crm_com_app_col_cea_elig_crm;
	}
	public void setR150_crm_com_app_col_cea_elig_crm(BigDecimal r150_crm_com_app_col_cea_elig_crm) {
		this.r150_crm_com_app_col_cea_elig_crm = r150_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR150_crm_com_app_col_elig_cea_vol_adj() {
		return r150_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR150_crm_com_app_col_elig_cea_vol_adj(BigDecimal r150_crm_com_app_col_elig_cea_vol_adj) {
		this.r150_crm_com_app_col_elig_cea_vol_adj = r150_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR150_crm_com_app_col_elig_fin_hai() {
		return r150_crm_com_app_col_elig_fin_hai;
	}
	public void setR150_crm_com_app_col_elig_fin_hai(BigDecimal r150_crm_com_app_col_elig_fin_hai) {
		this.r150_crm_com_app_col_elig_fin_hai = r150_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR150_crm_com_app_col_cea_val_aft_crm() {
		return r150_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR150_crm_com_app_col_cea_val_aft_crm(BigDecimal r150_crm_com_app_col_cea_val_aft_crm) {
		this.r150_crm_com_app_col_cea_val_aft_crm = r150_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR150_rwa_elig_cea_not_cov() {
		return r150_rwa_elig_cea_not_cov;
	}
	public void setR150_rwa_elig_cea_not_cov(BigDecimal r150_rwa_elig_cea_not_cov) {
		this.r150_rwa_elig_cea_not_cov = r150_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR150_rwa_unsec_cea_sub_cre_ris() {
		return r150_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR150_rwa_unsec_cea_sub_cre_ris(BigDecimal r150_rwa_unsec_cea_sub_cre_ris) {
		this.r150_rwa_unsec_cea_sub_cre_ris = r150_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR150_rwa_unsec_cea() {
		return r150_rwa_unsec_cea;
	}
	public void setR150_rwa_unsec_cea(BigDecimal r150_rwa_unsec_cea) {
		this.r150_rwa_unsec_cea = r150_rwa_unsec_cea;
	}
	public BigDecimal getR150_rwa_tot_ris_wei_ass() {
		return r150_rwa_tot_ris_wei_ass;
	}
	public void setR150_rwa_tot_ris_wei_ass(BigDecimal r150_rwa_tot_ris_wei_ass) {
		this.r150_rwa_tot_ris_wei_ass = r150_rwa_tot_ris_wei_ass;
	}
	public String getR151_exposure_class_off_bal() {
		return r151_exposure_class_off_bal;
	}
	public void setR151_exposure_class_off_bal(String r151_exposure_class_off_bal) {
		this.r151_exposure_class_off_bal = r151_exposure_class_off_bal;
	}
	public BigDecimal getR151_nom_pri_amt() {
		return r151_nom_pri_amt;
	}
	public void setR151_nom_pri_amt(BigDecimal r151_nom_pri_amt) {
		this.r151_nom_pri_amt = r151_nom_pri_amt;
	}
	public BigDecimal getR151_ccf() {
		return r151_ccf;
	}
	public void setR151_ccf(BigDecimal r151_ccf) {
		this.r151_ccf = r151_ccf;
	}
	public BigDecimal getR151_cea() {
		return r151_cea;
	}
	public void setR151_cea(BigDecimal r151_cea) {
		this.r151_cea = r151_cea;
	}
	public BigDecimal getR151_cea_elig_coun_bilt_net() {
		return r151_cea_elig_coun_bilt_net;
	}
	public void setR151_cea_elig_coun_bilt_net(BigDecimal r151_cea_elig_coun_bilt_net) {
		this.r151_cea_elig_coun_bilt_net = r151_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR151_cea_aft_net() {
		return r151_cea_aft_net;
	}
	public void setR151_cea_aft_net(BigDecimal r151_cea_aft_net) {
		this.r151_cea_aft_net = r151_cea_aft_net;
	}
	public BigDecimal getR151_crm_sub_app_cea_elig() {
		return r151_crm_sub_app_cea_elig;
	}
	public void setR151_crm_sub_app_cea_elig(BigDecimal r151_crm_sub_app_cea_elig) {
		this.r151_crm_sub_app_cea_elig = r151_crm_sub_app_cea_elig;
	}
	public BigDecimal getR151_crm_sub_app_non_col_guar_elig() {
		return r151_crm_sub_app_non_col_guar_elig;
	}
	public void setR151_crm_sub_app_non_col_guar_elig(BigDecimal r151_crm_sub_app_non_col_guar_elig) {
		this.r151_crm_sub_app_non_col_guar_elig = r151_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR151_crm_sub_app_non_col_cre_der() {
		return r151_crm_sub_app_non_col_cre_der;
	}
	public void setR151_crm_sub_app_non_col_cre_der(BigDecimal r151_crm_sub_app_non_col_cre_der) {
		this.r151_crm_sub_app_non_col_cre_der = r151_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR151_crm_sub_app_col_elig_cash() {
		return r151_crm_sub_app_col_elig_cash;
	}
	public void setR151_crm_sub_app_col_elig_cash(BigDecimal r151_crm_sub_app_col_elig_cash) {
		this.r151_crm_sub_app_col_elig_cash = r151_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR151_crm_sub_app_col_elig_tre_bills() {
		return r151_crm_sub_app_col_elig_tre_bills;
	}
	public void setR151_crm_sub_app_col_elig_tre_bills(BigDecimal r151_crm_sub_app_col_elig_tre_bills) {
		this.r151_crm_sub_app_col_elig_tre_bills = r151_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR151_crm_sub_app_col_elig_deb_sec() {
		return r151_crm_sub_app_col_elig_deb_sec;
	}
	public void setR151_crm_sub_app_col_elig_deb_sec(BigDecimal r151_crm_sub_app_col_elig_deb_sec) {
		this.r151_crm_sub_app_col_elig_deb_sec = r151_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR151_crm_sub_app_col_elig_euiq() {
		return r151_crm_sub_app_col_elig_euiq;
	}
	public void setR151_crm_sub_app_col_elig_euiq(BigDecimal r151_crm_sub_app_col_elig_euiq) {
		this.r151_crm_sub_app_col_elig_euiq = r151_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR151_crm_sub_app_col_elig_uni_tru() {
		return r151_crm_sub_app_col_elig_uni_tru;
	}
	public void setR151_crm_sub_app_col_elig_uni_tru(BigDecimal r151_crm_sub_app_col_elig_uni_tru) {
		this.r151_crm_sub_app_col_elig_uni_tru = r151_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR151_crm_sub_app_col_cea_cov() {
		return r151_crm_sub_app_col_cea_cov;
	}
	public void setR151_crm_sub_app_col_cea_cov(BigDecimal r151_crm_sub_app_col_cea_cov) {
		this.r151_crm_sub_app_col_cea_cov = r151_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR151_crm_sub_app_col_cea_not_cov() {
		return r151_crm_sub_app_col_cea_not_cov;
	}
	public void setR151_crm_sub_app_col_cea_not_cov(BigDecimal r151_crm_sub_app_col_cea_not_cov) {
		this.r151_crm_sub_app_col_cea_not_cov = r151_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR151_crm_sub_app_rwa_ris_wei_crm() {
		return r151_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR151_crm_sub_app_rwa_ris_wei_crm(BigDecimal r151_crm_sub_app_rwa_ris_wei_crm) {
		this.r151_crm_sub_app_rwa_ris_wei_crm = r151_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR151_crm_sub_app_rwa_ris_cea_cov() {
		return r151_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR151_crm_sub_app_rwa_ris_cea_cov(BigDecimal r151_crm_sub_app_rwa_ris_cea_cov) {
		this.r151_crm_sub_app_rwa_ris_cea_cov = r151_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR151_crm_sub_app_rwa_appl_org_coun() {
		return r151_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR151_crm_sub_app_rwa_appl_org_coun(BigDecimal r151_crm_sub_app_rwa_appl_org_coun) {
		this.r151_crm_sub_app_rwa_appl_org_coun = r151_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR151_crm_sub_app_rwa_ris_cea_not_cov() {
		return r151_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR151_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r151_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r151_crm_sub_app_rwa_ris_cea_not_cov = r151_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR151_crm_com_app_col_cea_elig_crm() {
		return r151_crm_com_app_col_cea_elig_crm;
	}
	public void setR151_crm_com_app_col_cea_elig_crm(BigDecimal r151_crm_com_app_col_cea_elig_crm) {
		this.r151_crm_com_app_col_cea_elig_crm = r151_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR151_crm_com_app_col_elig_cea_vol_adj() {
		return r151_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR151_crm_com_app_col_elig_cea_vol_adj(BigDecimal r151_crm_com_app_col_elig_cea_vol_adj) {
		this.r151_crm_com_app_col_elig_cea_vol_adj = r151_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR151_crm_com_app_col_elig_fin_hai() {
		return r151_crm_com_app_col_elig_fin_hai;
	}
	public void setR151_crm_com_app_col_elig_fin_hai(BigDecimal r151_crm_com_app_col_elig_fin_hai) {
		this.r151_crm_com_app_col_elig_fin_hai = r151_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR151_crm_com_app_col_cea_val_aft_crm() {
		return r151_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR151_crm_com_app_col_cea_val_aft_crm(BigDecimal r151_crm_com_app_col_cea_val_aft_crm) {
		this.r151_crm_com_app_col_cea_val_aft_crm = r151_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR151_rwa_elig_cea_not_cov() {
		return r151_rwa_elig_cea_not_cov;
	}
	public void setR151_rwa_elig_cea_not_cov(BigDecimal r151_rwa_elig_cea_not_cov) {
		this.r151_rwa_elig_cea_not_cov = r151_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR151_rwa_unsec_cea_sub_cre_ris() {
		return r151_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR151_rwa_unsec_cea_sub_cre_ris(BigDecimal r151_rwa_unsec_cea_sub_cre_ris) {
		this.r151_rwa_unsec_cea_sub_cre_ris = r151_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR151_rwa_unsec_cea() {
		return r151_rwa_unsec_cea;
	}
	public void setR151_rwa_unsec_cea(BigDecimal r151_rwa_unsec_cea) {
		this.r151_rwa_unsec_cea = r151_rwa_unsec_cea;
	}
	public BigDecimal getR151_rwa_tot_ris_wei_ass() {
		return r151_rwa_tot_ris_wei_ass;
	}
	public void setR151_rwa_tot_ris_wei_ass(BigDecimal r151_rwa_tot_ris_wei_ass) {
		this.r151_rwa_tot_ris_wei_ass = r151_rwa_tot_ris_wei_ass;
	}
	public String getR152_exposure_class_off_bal() {
		return r152_exposure_class_off_bal;
	}
	public void setR152_exposure_class_off_bal(String r152_exposure_class_off_bal) {
		this.r152_exposure_class_off_bal = r152_exposure_class_off_bal;
	}
	public BigDecimal getR152_nom_pri_amt() {
		return r152_nom_pri_amt;
	}
	public void setR152_nom_pri_amt(BigDecimal r152_nom_pri_amt) {
		this.r152_nom_pri_amt = r152_nom_pri_amt;
	}
	public BigDecimal getR152_ccf() {
		return r152_ccf;
	}
	public void setR152_ccf(BigDecimal r152_ccf) {
		this.r152_ccf = r152_ccf;
	}
	public BigDecimal getR152_cea() {
		return r152_cea;
	}
	public void setR152_cea(BigDecimal r152_cea) {
		this.r152_cea = r152_cea;
	}
	public BigDecimal getR152_cea_elig_coun_bilt_net() {
		return r152_cea_elig_coun_bilt_net;
	}
	public void setR152_cea_elig_coun_bilt_net(BigDecimal r152_cea_elig_coun_bilt_net) {
		this.r152_cea_elig_coun_bilt_net = r152_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR152_cea_aft_net() {
		return r152_cea_aft_net;
	}
	public void setR152_cea_aft_net(BigDecimal r152_cea_aft_net) {
		this.r152_cea_aft_net = r152_cea_aft_net;
	}
	public BigDecimal getR152_crm_sub_app_cea_elig() {
		return r152_crm_sub_app_cea_elig;
	}
	public void setR152_crm_sub_app_cea_elig(BigDecimal r152_crm_sub_app_cea_elig) {
		this.r152_crm_sub_app_cea_elig = r152_crm_sub_app_cea_elig;
	}
	public BigDecimal getR152_crm_sub_app_non_col_guar_elig() {
		return r152_crm_sub_app_non_col_guar_elig;
	}
	public void setR152_crm_sub_app_non_col_guar_elig(BigDecimal r152_crm_sub_app_non_col_guar_elig) {
		this.r152_crm_sub_app_non_col_guar_elig = r152_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR152_crm_sub_app_non_col_cre_der() {
		return r152_crm_sub_app_non_col_cre_der;
	}
	public void setR152_crm_sub_app_non_col_cre_der(BigDecimal r152_crm_sub_app_non_col_cre_der) {
		this.r152_crm_sub_app_non_col_cre_der = r152_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR152_crm_sub_app_col_elig_cash() {
		return r152_crm_sub_app_col_elig_cash;
	}
	public void setR152_crm_sub_app_col_elig_cash(BigDecimal r152_crm_sub_app_col_elig_cash) {
		this.r152_crm_sub_app_col_elig_cash = r152_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR152_crm_sub_app_col_elig_tre_bills() {
		return r152_crm_sub_app_col_elig_tre_bills;
	}
	public void setR152_crm_sub_app_col_elig_tre_bills(BigDecimal r152_crm_sub_app_col_elig_tre_bills) {
		this.r152_crm_sub_app_col_elig_tre_bills = r152_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR152_crm_sub_app_col_elig_deb_sec() {
		return r152_crm_sub_app_col_elig_deb_sec;
	}
	public void setR152_crm_sub_app_col_elig_deb_sec(BigDecimal r152_crm_sub_app_col_elig_deb_sec) {
		this.r152_crm_sub_app_col_elig_deb_sec = r152_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR152_crm_sub_app_col_elig_euiq() {
		return r152_crm_sub_app_col_elig_euiq;
	}
	public void setR152_crm_sub_app_col_elig_euiq(BigDecimal r152_crm_sub_app_col_elig_euiq) {
		this.r152_crm_sub_app_col_elig_euiq = r152_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR152_crm_sub_app_col_elig_uni_tru() {
		return r152_crm_sub_app_col_elig_uni_tru;
	}
	public void setR152_crm_sub_app_col_elig_uni_tru(BigDecimal r152_crm_sub_app_col_elig_uni_tru) {
		this.r152_crm_sub_app_col_elig_uni_tru = r152_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR152_crm_sub_app_col_cea_cov() {
		return r152_crm_sub_app_col_cea_cov;
	}
	public void setR152_crm_sub_app_col_cea_cov(BigDecimal r152_crm_sub_app_col_cea_cov) {
		this.r152_crm_sub_app_col_cea_cov = r152_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR152_crm_sub_app_col_cea_not_cov() {
		return r152_crm_sub_app_col_cea_not_cov;
	}
	public void setR152_crm_sub_app_col_cea_not_cov(BigDecimal r152_crm_sub_app_col_cea_not_cov) {
		this.r152_crm_sub_app_col_cea_not_cov = r152_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR152_crm_sub_app_rwa_ris_wei_crm() {
		return r152_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR152_crm_sub_app_rwa_ris_wei_crm(BigDecimal r152_crm_sub_app_rwa_ris_wei_crm) {
		this.r152_crm_sub_app_rwa_ris_wei_crm = r152_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR152_crm_sub_app_rwa_ris_cea_cov() {
		return r152_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR152_crm_sub_app_rwa_ris_cea_cov(BigDecimal r152_crm_sub_app_rwa_ris_cea_cov) {
		this.r152_crm_sub_app_rwa_ris_cea_cov = r152_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR152_crm_sub_app_rwa_appl_org_coun() {
		return r152_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR152_crm_sub_app_rwa_appl_org_coun(BigDecimal r152_crm_sub_app_rwa_appl_org_coun) {
		this.r152_crm_sub_app_rwa_appl_org_coun = r152_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR152_crm_sub_app_rwa_ris_cea_not_cov() {
		return r152_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR152_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r152_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r152_crm_sub_app_rwa_ris_cea_not_cov = r152_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR152_crm_com_app_col_cea_elig_crm() {
		return r152_crm_com_app_col_cea_elig_crm;
	}
	public void setR152_crm_com_app_col_cea_elig_crm(BigDecimal r152_crm_com_app_col_cea_elig_crm) {
		this.r152_crm_com_app_col_cea_elig_crm = r152_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR152_crm_com_app_col_elig_cea_vol_adj() {
		return r152_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR152_crm_com_app_col_elig_cea_vol_adj(BigDecimal r152_crm_com_app_col_elig_cea_vol_adj) {
		this.r152_crm_com_app_col_elig_cea_vol_adj = r152_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR152_crm_com_app_col_elig_fin_hai() {
		return r152_crm_com_app_col_elig_fin_hai;
	}
	public void setR152_crm_com_app_col_elig_fin_hai(BigDecimal r152_crm_com_app_col_elig_fin_hai) {
		this.r152_crm_com_app_col_elig_fin_hai = r152_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR152_crm_com_app_col_cea_val_aft_crm() {
		return r152_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR152_crm_com_app_col_cea_val_aft_crm(BigDecimal r152_crm_com_app_col_cea_val_aft_crm) {
		this.r152_crm_com_app_col_cea_val_aft_crm = r152_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR152_rwa_elig_cea_not_cov() {
		return r152_rwa_elig_cea_not_cov;
	}
	public void setR152_rwa_elig_cea_not_cov(BigDecimal r152_rwa_elig_cea_not_cov) {
		this.r152_rwa_elig_cea_not_cov = r152_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR152_rwa_unsec_cea_sub_cre_ris() {
		return r152_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR152_rwa_unsec_cea_sub_cre_ris(BigDecimal r152_rwa_unsec_cea_sub_cre_ris) {
		this.r152_rwa_unsec_cea_sub_cre_ris = r152_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR152_rwa_unsec_cea() {
		return r152_rwa_unsec_cea;
	}
	public void setR152_rwa_unsec_cea(BigDecimal r152_rwa_unsec_cea) {
		this.r152_rwa_unsec_cea = r152_rwa_unsec_cea;
	}
	public BigDecimal getR152_rwa_tot_ris_wei_ass() {
		return r152_rwa_tot_ris_wei_ass;
	}
	public void setR152_rwa_tot_ris_wei_ass(BigDecimal r152_rwa_tot_ris_wei_ass) {
		this.r152_rwa_tot_ris_wei_ass = r152_rwa_tot_ris_wei_ass;
	}
	public String getR153_exposure_class_off_bal() {
		return r153_exposure_class_off_bal;
	}
	public void setR153_exposure_class_off_bal(String r153_exposure_class_off_bal) {
		this.r153_exposure_class_off_bal = r153_exposure_class_off_bal;
	}
	public BigDecimal getR153_nom_pri_amt() {
		return r153_nom_pri_amt;
	}
	public void setR153_nom_pri_amt(BigDecimal r153_nom_pri_amt) {
		this.r153_nom_pri_amt = r153_nom_pri_amt;
	}
	public BigDecimal getR153_ccf() {
		return r153_ccf;
	}
	public void setR153_ccf(BigDecimal r153_ccf) {
		this.r153_ccf = r153_ccf;
	}
	public BigDecimal getR153_cea() {
		return r153_cea;
	}
	public void setR153_cea(BigDecimal r153_cea) {
		this.r153_cea = r153_cea;
	}
	public BigDecimal getR153_cea_elig_coun_bilt_net() {
		return r153_cea_elig_coun_bilt_net;
	}
	public void setR153_cea_elig_coun_bilt_net(BigDecimal r153_cea_elig_coun_bilt_net) {
		this.r153_cea_elig_coun_bilt_net = r153_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR153_cea_aft_net() {
		return r153_cea_aft_net;
	}
	public void setR153_cea_aft_net(BigDecimal r153_cea_aft_net) {
		this.r153_cea_aft_net = r153_cea_aft_net;
	}
	public BigDecimal getR153_crm_sub_app_cea_elig() {
		return r153_crm_sub_app_cea_elig;
	}
	public void setR153_crm_sub_app_cea_elig(BigDecimal r153_crm_sub_app_cea_elig) {
		this.r153_crm_sub_app_cea_elig = r153_crm_sub_app_cea_elig;
	}
	public BigDecimal getR153_crm_sub_app_non_col_guar_elig() {
		return r153_crm_sub_app_non_col_guar_elig;
	}
	public void setR153_crm_sub_app_non_col_guar_elig(BigDecimal r153_crm_sub_app_non_col_guar_elig) {
		this.r153_crm_sub_app_non_col_guar_elig = r153_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR153_crm_sub_app_non_col_cre_der() {
		return r153_crm_sub_app_non_col_cre_der;
	}
	public void setR153_crm_sub_app_non_col_cre_der(BigDecimal r153_crm_sub_app_non_col_cre_der) {
		this.r153_crm_sub_app_non_col_cre_der = r153_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR153_crm_sub_app_col_elig_cash() {
		return r153_crm_sub_app_col_elig_cash;
	}
	public void setR153_crm_sub_app_col_elig_cash(BigDecimal r153_crm_sub_app_col_elig_cash) {
		this.r153_crm_sub_app_col_elig_cash = r153_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR153_crm_sub_app_col_elig_tre_bills() {
		return r153_crm_sub_app_col_elig_tre_bills;
	}
	public void setR153_crm_sub_app_col_elig_tre_bills(BigDecimal r153_crm_sub_app_col_elig_tre_bills) {
		this.r153_crm_sub_app_col_elig_tre_bills = r153_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR153_crm_sub_app_col_elig_deb_sec() {
		return r153_crm_sub_app_col_elig_deb_sec;
	}
	public void setR153_crm_sub_app_col_elig_deb_sec(BigDecimal r153_crm_sub_app_col_elig_deb_sec) {
		this.r153_crm_sub_app_col_elig_deb_sec = r153_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR153_crm_sub_app_col_elig_euiq() {
		return r153_crm_sub_app_col_elig_euiq;
	}
	public void setR153_crm_sub_app_col_elig_euiq(BigDecimal r153_crm_sub_app_col_elig_euiq) {
		this.r153_crm_sub_app_col_elig_euiq = r153_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR153_crm_sub_app_col_elig_uni_tru() {
		return r153_crm_sub_app_col_elig_uni_tru;
	}
	public void setR153_crm_sub_app_col_elig_uni_tru(BigDecimal r153_crm_sub_app_col_elig_uni_tru) {
		this.r153_crm_sub_app_col_elig_uni_tru = r153_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR153_crm_sub_app_col_cea_cov() {
		return r153_crm_sub_app_col_cea_cov;
	}
	public void setR153_crm_sub_app_col_cea_cov(BigDecimal r153_crm_sub_app_col_cea_cov) {
		this.r153_crm_sub_app_col_cea_cov = r153_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR153_crm_sub_app_col_cea_not_cov() {
		return r153_crm_sub_app_col_cea_not_cov;
	}
	public void setR153_crm_sub_app_col_cea_not_cov(BigDecimal r153_crm_sub_app_col_cea_not_cov) {
		this.r153_crm_sub_app_col_cea_not_cov = r153_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR153_crm_sub_app_rwa_ris_wei_crm() {
		return r153_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR153_crm_sub_app_rwa_ris_wei_crm(BigDecimal r153_crm_sub_app_rwa_ris_wei_crm) {
		this.r153_crm_sub_app_rwa_ris_wei_crm = r153_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR153_crm_sub_app_rwa_ris_cea_cov() {
		return r153_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR153_crm_sub_app_rwa_ris_cea_cov(BigDecimal r153_crm_sub_app_rwa_ris_cea_cov) {
		this.r153_crm_sub_app_rwa_ris_cea_cov = r153_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR153_crm_sub_app_rwa_appl_org_coun() {
		return r153_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR153_crm_sub_app_rwa_appl_org_coun(BigDecimal r153_crm_sub_app_rwa_appl_org_coun) {
		this.r153_crm_sub_app_rwa_appl_org_coun = r153_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR153_crm_sub_app_rwa_ris_cea_not_cov() {
		return r153_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR153_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r153_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r153_crm_sub_app_rwa_ris_cea_not_cov = r153_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR153_crm_com_app_col_cea_elig_crm() {
		return r153_crm_com_app_col_cea_elig_crm;
	}
	public void setR153_crm_com_app_col_cea_elig_crm(BigDecimal r153_crm_com_app_col_cea_elig_crm) {
		this.r153_crm_com_app_col_cea_elig_crm = r153_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR153_crm_com_app_col_elig_cea_vol_adj() {
		return r153_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR153_crm_com_app_col_elig_cea_vol_adj(BigDecimal r153_crm_com_app_col_elig_cea_vol_adj) {
		this.r153_crm_com_app_col_elig_cea_vol_adj = r153_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR153_crm_com_app_col_elig_fin_hai() {
		return r153_crm_com_app_col_elig_fin_hai;
	}
	public void setR153_crm_com_app_col_elig_fin_hai(BigDecimal r153_crm_com_app_col_elig_fin_hai) {
		this.r153_crm_com_app_col_elig_fin_hai = r153_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR153_crm_com_app_col_cea_val_aft_crm() {
		return r153_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR153_crm_com_app_col_cea_val_aft_crm(BigDecimal r153_crm_com_app_col_cea_val_aft_crm) {
		this.r153_crm_com_app_col_cea_val_aft_crm = r153_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR153_rwa_elig_cea_not_cov() {
		return r153_rwa_elig_cea_not_cov;
	}
	public void setR153_rwa_elig_cea_not_cov(BigDecimal r153_rwa_elig_cea_not_cov) {
		this.r153_rwa_elig_cea_not_cov = r153_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR153_rwa_unsec_cea_sub_cre_ris() {
		return r153_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR153_rwa_unsec_cea_sub_cre_ris(BigDecimal r153_rwa_unsec_cea_sub_cre_ris) {
		this.r153_rwa_unsec_cea_sub_cre_ris = r153_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR153_rwa_unsec_cea() {
		return r153_rwa_unsec_cea;
	}
	public void setR153_rwa_unsec_cea(BigDecimal r153_rwa_unsec_cea) {
		this.r153_rwa_unsec_cea = r153_rwa_unsec_cea;
	}
	public BigDecimal getR153_rwa_tot_ris_wei_ass() {
		return r153_rwa_tot_ris_wei_ass;
	}
	public void setR153_rwa_tot_ris_wei_ass(BigDecimal r153_rwa_tot_ris_wei_ass) {
		this.r153_rwa_tot_ris_wei_ass = r153_rwa_tot_ris_wei_ass;
	}
	public String getR154_exposure_class_off_bal() {
		return r154_exposure_class_off_bal;
	}
	public void setR154_exposure_class_off_bal(String r154_exposure_class_off_bal) {
		this.r154_exposure_class_off_bal = r154_exposure_class_off_bal;
	}
	public BigDecimal getR154_nom_pri_amt() {
		return r154_nom_pri_amt;
	}
	public void setR154_nom_pri_amt(BigDecimal r154_nom_pri_amt) {
		this.r154_nom_pri_amt = r154_nom_pri_amt;
	}
	public BigDecimal getR154_ccf() {
		return r154_ccf;
	}
	public void setR154_ccf(BigDecimal r154_ccf) {
		this.r154_ccf = r154_ccf;
	}
	public BigDecimal getR154_cea() {
		return r154_cea;
	}
	public void setR154_cea(BigDecimal r154_cea) {
		this.r154_cea = r154_cea;
	}
	public BigDecimal getR154_cea_elig_coun_bilt_net() {
		return r154_cea_elig_coun_bilt_net;
	}
	public void setR154_cea_elig_coun_bilt_net(BigDecimal r154_cea_elig_coun_bilt_net) {
		this.r154_cea_elig_coun_bilt_net = r154_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR154_cea_aft_net() {
		return r154_cea_aft_net;
	}
	public void setR154_cea_aft_net(BigDecimal r154_cea_aft_net) {
		this.r154_cea_aft_net = r154_cea_aft_net;
	}
	public BigDecimal getR154_crm_sub_app_cea_elig() {
		return r154_crm_sub_app_cea_elig;
	}
	public void setR154_crm_sub_app_cea_elig(BigDecimal r154_crm_sub_app_cea_elig) {
		this.r154_crm_sub_app_cea_elig = r154_crm_sub_app_cea_elig;
	}
	public BigDecimal getR154_crm_sub_app_non_col_guar_elig() {
		return r154_crm_sub_app_non_col_guar_elig;
	}
	public void setR154_crm_sub_app_non_col_guar_elig(BigDecimal r154_crm_sub_app_non_col_guar_elig) {
		this.r154_crm_sub_app_non_col_guar_elig = r154_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR154_crm_sub_app_non_col_cre_der() {
		return r154_crm_sub_app_non_col_cre_der;
	}
	public void setR154_crm_sub_app_non_col_cre_der(BigDecimal r154_crm_sub_app_non_col_cre_der) {
		this.r154_crm_sub_app_non_col_cre_der = r154_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR154_crm_sub_app_col_elig_cash() {
		return r154_crm_sub_app_col_elig_cash;
	}
	public void setR154_crm_sub_app_col_elig_cash(BigDecimal r154_crm_sub_app_col_elig_cash) {
		this.r154_crm_sub_app_col_elig_cash = r154_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR154_crm_sub_app_col_elig_tre_bills() {
		return r154_crm_sub_app_col_elig_tre_bills;
	}
	public void setR154_crm_sub_app_col_elig_tre_bills(BigDecimal r154_crm_sub_app_col_elig_tre_bills) {
		this.r154_crm_sub_app_col_elig_tre_bills = r154_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR154_crm_sub_app_col_elig_deb_sec() {
		return r154_crm_sub_app_col_elig_deb_sec;
	}
	public void setR154_crm_sub_app_col_elig_deb_sec(BigDecimal r154_crm_sub_app_col_elig_deb_sec) {
		this.r154_crm_sub_app_col_elig_deb_sec = r154_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR154_crm_sub_app_col_elig_euiq() {
		return r154_crm_sub_app_col_elig_euiq;
	}
	public void setR154_crm_sub_app_col_elig_euiq(BigDecimal r154_crm_sub_app_col_elig_euiq) {
		this.r154_crm_sub_app_col_elig_euiq = r154_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR154_crm_sub_app_col_elig_uni_tru() {
		return r154_crm_sub_app_col_elig_uni_tru;
	}
	public void setR154_crm_sub_app_col_elig_uni_tru(BigDecimal r154_crm_sub_app_col_elig_uni_tru) {
		this.r154_crm_sub_app_col_elig_uni_tru = r154_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR154_crm_sub_app_col_cea_cov() {
		return r154_crm_sub_app_col_cea_cov;
	}
	public void setR154_crm_sub_app_col_cea_cov(BigDecimal r154_crm_sub_app_col_cea_cov) {
		this.r154_crm_sub_app_col_cea_cov = r154_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR154_crm_sub_app_col_cea_not_cov() {
		return r154_crm_sub_app_col_cea_not_cov;
	}
	public void setR154_crm_sub_app_col_cea_not_cov(BigDecimal r154_crm_sub_app_col_cea_not_cov) {
		this.r154_crm_sub_app_col_cea_not_cov = r154_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR154_crm_sub_app_rwa_ris_wei_crm() {
		return r154_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR154_crm_sub_app_rwa_ris_wei_crm(BigDecimal r154_crm_sub_app_rwa_ris_wei_crm) {
		this.r154_crm_sub_app_rwa_ris_wei_crm = r154_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR154_crm_sub_app_rwa_ris_cea_cov() {
		return r154_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR154_crm_sub_app_rwa_ris_cea_cov(BigDecimal r154_crm_sub_app_rwa_ris_cea_cov) {
		this.r154_crm_sub_app_rwa_ris_cea_cov = r154_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR154_crm_sub_app_rwa_appl_org_coun() {
		return r154_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR154_crm_sub_app_rwa_appl_org_coun(BigDecimal r154_crm_sub_app_rwa_appl_org_coun) {
		this.r154_crm_sub_app_rwa_appl_org_coun = r154_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR154_crm_sub_app_rwa_ris_cea_not_cov() {
		return r154_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR154_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r154_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r154_crm_sub_app_rwa_ris_cea_not_cov = r154_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR154_crm_com_app_col_cea_elig_crm() {
		return r154_crm_com_app_col_cea_elig_crm;
	}
	public void setR154_crm_com_app_col_cea_elig_crm(BigDecimal r154_crm_com_app_col_cea_elig_crm) {
		this.r154_crm_com_app_col_cea_elig_crm = r154_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR154_crm_com_app_col_elig_cea_vol_adj() {
		return r154_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR154_crm_com_app_col_elig_cea_vol_adj(BigDecimal r154_crm_com_app_col_elig_cea_vol_adj) {
		this.r154_crm_com_app_col_elig_cea_vol_adj = r154_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR154_crm_com_app_col_elig_fin_hai() {
		return r154_crm_com_app_col_elig_fin_hai;
	}
	public void setR154_crm_com_app_col_elig_fin_hai(BigDecimal r154_crm_com_app_col_elig_fin_hai) {
		this.r154_crm_com_app_col_elig_fin_hai = r154_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR154_crm_com_app_col_cea_val_aft_crm() {
		return r154_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR154_crm_com_app_col_cea_val_aft_crm(BigDecimal r154_crm_com_app_col_cea_val_aft_crm) {
		this.r154_crm_com_app_col_cea_val_aft_crm = r154_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR154_rwa_elig_cea_not_cov() {
		return r154_rwa_elig_cea_not_cov;
	}
	public void setR154_rwa_elig_cea_not_cov(BigDecimal r154_rwa_elig_cea_not_cov) {
		this.r154_rwa_elig_cea_not_cov = r154_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR154_rwa_unsec_cea_sub_cre_ris() {
		return r154_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR154_rwa_unsec_cea_sub_cre_ris(BigDecimal r154_rwa_unsec_cea_sub_cre_ris) {
		this.r154_rwa_unsec_cea_sub_cre_ris = r154_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR154_rwa_unsec_cea() {
		return r154_rwa_unsec_cea;
	}
	public void setR154_rwa_unsec_cea(BigDecimal r154_rwa_unsec_cea) {
		this.r154_rwa_unsec_cea = r154_rwa_unsec_cea;
	}
	public BigDecimal getR154_rwa_tot_ris_wei_ass() {
		return r154_rwa_tot_ris_wei_ass;
	}
	public void setR154_rwa_tot_ris_wei_ass(BigDecimal r154_rwa_tot_ris_wei_ass) {
		this.r154_rwa_tot_ris_wei_ass = r154_rwa_tot_ris_wei_ass;
	}
	public String getR155_exposure_class_off_bal() {
		return r155_exposure_class_off_bal;
	}
	public void setR155_exposure_class_off_bal(String r155_exposure_class_off_bal) {
		this.r155_exposure_class_off_bal = r155_exposure_class_off_bal;
	}
	public BigDecimal getR155_nom_pri_amt() {
		return r155_nom_pri_amt;
	}
	public void setR155_nom_pri_amt(BigDecimal r155_nom_pri_amt) {
		this.r155_nom_pri_amt = r155_nom_pri_amt;
	}
	public BigDecimal getR155_ccf() {
		return r155_ccf;
	}
	public void setR155_ccf(BigDecimal r155_ccf) {
		this.r155_ccf = r155_ccf;
	}
	public BigDecimal getR155_cea() {
		return r155_cea;
	}
	public void setR155_cea(BigDecimal r155_cea) {
		this.r155_cea = r155_cea;
	}
	public BigDecimal getR155_cea_elig_coun_bilt_net() {
		return r155_cea_elig_coun_bilt_net;
	}
	public void setR155_cea_elig_coun_bilt_net(BigDecimal r155_cea_elig_coun_bilt_net) {
		this.r155_cea_elig_coun_bilt_net = r155_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR155_cea_aft_net() {
		return r155_cea_aft_net;
	}
	public void setR155_cea_aft_net(BigDecimal r155_cea_aft_net) {
		this.r155_cea_aft_net = r155_cea_aft_net;
	}
	public BigDecimal getR155_crm_sub_app_cea_elig() {
		return r155_crm_sub_app_cea_elig;
	}
	public void setR155_crm_sub_app_cea_elig(BigDecimal r155_crm_sub_app_cea_elig) {
		this.r155_crm_sub_app_cea_elig = r155_crm_sub_app_cea_elig;
	}
	public BigDecimal getR155_crm_sub_app_non_col_guar_elig() {
		return r155_crm_sub_app_non_col_guar_elig;
	}
	public void setR155_crm_sub_app_non_col_guar_elig(BigDecimal r155_crm_sub_app_non_col_guar_elig) {
		this.r155_crm_sub_app_non_col_guar_elig = r155_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR155_crm_sub_app_non_col_cre_der() {
		return r155_crm_sub_app_non_col_cre_der;
	}
	public void setR155_crm_sub_app_non_col_cre_der(BigDecimal r155_crm_sub_app_non_col_cre_der) {
		this.r155_crm_sub_app_non_col_cre_der = r155_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR155_crm_sub_app_col_elig_cash() {
		return r155_crm_sub_app_col_elig_cash;
	}
	public void setR155_crm_sub_app_col_elig_cash(BigDecimal r155_crm_sub_app_col_elig_cash) {
		this.r155_crm_sub_app_col_elig_cash = r155_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR155_crm_sub_app_col_elig_tre_bills() {
		return r155_crm_sub_app_col_elig_tre_bills;
	}
	public void setR155_crm_sub_app_col_elig_tre_bills(BigDecimal r155_crm_sub_app_col_elig_tre_bills) {
		this.r155_crm_sub_app_col_elig_tre_bills = r155_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR155_crm_sub_app_col_elig_deb_sec() {
		return r155_crm_sub_app_col_elig_deb_sec;
	}
	public void setR155_crm_sub_app_col_elig_deb_sec(BigDecimal r155_crm_sub_app_col_elig_deb_sec) {
		this.r155_crm_sub_app_col_elig_deb_sec = r155_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR155_crm_sub_app_col_elig_euiq() {
		return r155_crm_sub_app_col_elig_euiq;
	}
	public void setR155_crm_sub_app_col_elig_euiq(BigDecimal r155_crm_sub_app_col_elig_euiq) {
		this.r155_crm_sub_app_col_elig_euiq = r155_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR155_crm_sub_app_col_elig_uni_tru() {
		return r155_crm_sub_app_col_elig_uni_tru;
	}
	public void setR155_crm_sub_app_col_elig_uni_tru(BigDecimal r155_crm_sub_app_col_elig_uni_tru) {
		this.r155_crm_sub_app_col_elig_uni_tru = r155_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR155_crm_sub_app_col_cea_cov() {
		return r155_crm_sub_app_col_cea_cov;
	}
	public void setR155_crm_sub_app_col_cea_cov(BigDecimal r155_crm_sub_app_col_cea_cov) {
		this.r155_crm_sub_app_col_cea_cov = r155_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR155_crm_sub_app_col_cea_not_cov() {
		return r155_crm_sub_app_col_cea_not_cov;
	}
	public void setR155_crm_sub_app_col_cea_not_cov(BigDecimal r155_crm_sub_app_col_cea_not_cov) {
		this.r155_crm_sub_app_col_cea_not_cov = r155_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR155_crm_sub_app_rwa_ris_wei_crm() {
		return r155_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR155_crm_sub_app_rwa_ris_wei_crm(BigDecimal r155_crm_sub_app_rwa_ris_wei_crm) {
		this.r155_crm_sub_app_rwa_ris_wei_crm = r155_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR155_crm_sub_app_rwa_ris_cea_cov() {
		return r155_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR155_crm_sub_app_rwa_ris_cea_cov(BigDecimal r155_crm_sub_app_rwa_ris_cea_cov) {
		this.r155_crm_sub_app_rwa_ris_cea_cov = r155_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR155_crm_sub_app_rwa_appl_org_coun() {
		return r155_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR155_crm_sub_app_rwa_appl_org_coun(BigDecimal r155_crm_sub_app_rwa_appl_org_coun) {
		this.r155_crm_sub_app_rwa_appl_org_coun = r155_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR155_crm_sub_app_rwa_ris_cea_not_cov() {
		return r155_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR155_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r155_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r155_crm_sub_app_rwa_ris_cea_not_cov = r155_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR155_crm_com_app_col_cea_elig_crm() {
		return r155_crm_com_app_col_cea_elig_crm;
	}
	public void setR155_crm_com_app_col_cea_elig_crm(BigDecimal r155_crm_com_app_col_cea_elig_crm) {
		this.r155_crm_com_app_col_cea_elig_crm = r155_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR155_crm_com_app_col_elig_cea_vol_adj() {
		return r155_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR155_crm_com_app_col_elig_cea_vol_adj(BigDecimal r155_crm_com_app_col_elig_cea_vol_adj) {
		this.r155_crm_com_app_col_elig_cea_vol_adj = r155_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR155_crm_com_app_col_elig_fin_hai() {
		return r155_crm_com_app_col_elig_fin_hai;
	}
	public void setR155_crm_com_app_col_elig_fin_hai(BigDecimal r155_crm_com_app_col_elig_fin_hai) {
		this.r155_crm_com_app_col_elig_fin_hai = r155_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR155_crm_com_app_col_cea_val_aft_crm() {
		return r155_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR155_crm_com_app_col_cea_val_aft_crm(BigDecimal r155_crm_com_app_col_cea_val_aft_crm) {
		this.r155_crm_com_app_col_cea_val_aft_crm = r155_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR155_rwa_elig_cea_not_cov() {
		return r155_rwa_elig_cea_not_cov;
	}
	public void setR155_rwa_elig_cea_not_cov(BigDecimal r155_rwa_elig_cea_not_cov) {
		this.r155_rwa_elig_cea_not_cov = r155_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR155_rwa_unsec_cea_sub_cre_ris() {
		return r155_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR155_rwa_unsec_cea_sub_cre_ris(BigDecimal r155_rwa_unsec_cea_sub_cre_ris) {
		this.r155_rwa_unsec_cea_sub_cre_ris = r155_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR155_rwa_unsec_cea() {
		return r155_rwa_unsec_cea;
	}
	public void setR155_rwa_unsec_cea(BigDecimal r155_rwa_unsec_cea) {
		this.r155_rwa_unsec_cea = r155_rwa_unsec_cea;
	}
	public BigDecimal getR155_rwa_tot_ris_wei_ass() {
		return r155_rwa_tot_ris_wei_ass;
	}
	public void setR155_rwa_tot_ris_wei_ass(BigDecimal r155_rwa_tot_ris_wei_ass) {
		this.r155_rwa_tot_ris_wei_ass = r155_rwa_tot_ris_wei_ass;
	}
	public String getR156_exposure_class_off_bal() {
		return r156_exposure_class_off_bal;
	}
	public void setR156_exposure_class_off_bal(String r156_exposure_class_off_bal) {
		this.r156_exposure_class_off_bal = r156_exposure_class_off_bal;
	}
	public BigDecimal getR156_nom_pri_amt() {
		return r156_nom_pri_amt;
	}
	public void setR156_nom_pri_amt(BigDecimal r156_nom_pri_amt) {
		this.r156_nom_pri_amt = r156_nom_pri_amt;
	}
	public BigDecimal getR156_ccf() {
		return r156_ccf;
	}
	public void setR156_ccf(BigDecimal r156_ccf) {
		this.r156_ccf = r156_ccf;
	}
	public BigDecimal getR156_cea() {
		return r156_cea;
	}
	public void setR156_cea(BigDecimal r156_cea) {
		this.r156_cea = r156_cea;
	}
	public BigDecimal getR156_cea_elig_coun_bilt_net() {
		return r156_cea_elig_coun_bilt_net;
	}
	public void setR156_cea_elig_coun_bilt_net(BigDecimal r156_cea_elig_coun_bilt_net) {
		this.r156_cea_elig_coun_bilt_net = r156_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR156_cea_aft_net() {
		return r156_cea_aft_net;
	}
	public void setR156_cea_aft_net(BigDecimal r156_cea_aft_net) {
		this.r156_cea_aft_net = r156_cea_aft_net;
	}
	public BigDecimal getR156_crm_sub_app_cea_elig() {
		return r156_crm_sub_app_cea_elig;
	}
	public void setR156_crm_sub_app_cea_elig(BigDecimal r156_crm_sub_app_cea_elig) {
		this.r156_crm_sub_app_cea_elig = r156_crm_sub_app_cea_elig;
	}
	public BigDecimal getR156_crm_sub_app_non_col_guar_elig() {
		return r156_crm_sub_app_non_col_guar_elig;
	}
	public void setR156_crm_sub_app_non_col_guar_elig(BigDecimal r156_crm_sub_app_non_col_guar_elig) {
		this.r156_crm_sub_app_non_col_guar_elig = r156_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR156_crm_sub_app_non_col_cre_der() {
		return r156_crm_sub_app_non_col_cre_der;
	}
	public void setR156_crm_sub_app_non_col_cre_der(BigDecimal r156_crm_sub_app_non_col_cre_der) {
		this.r156_crm_sub_app_non_col_cre_der = r156_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR156_crm_sub_app_col_elig_cash() {
		return r156_crm_sub_app_col_elig_cash;
	}
	public void setR156_crm_sub_app_col_elig_cash(BigDecimal r156_crm_sub_app_col_elig_cash) {
		this.r156_crm_sub_app_col_elig_cash = r156_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR156_crm_sub_app_col_elig_tre_bills() {
		return r156_crm_sub_app_col_elig_tre_bills;
	}
	public void setR156_crm_sub_app_col_elig_tre_bills(BigDecimal r156_crm_sub_app_col_elig_tre_bills) {
		this.r156_crm_sub_app_col_elig_tre_bills = r156_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR156_crm_sub_app_col_elig_deb_sec() {
		return r156_crm_sub_app_col_elig_deb_sec;
	}
	public void setR156_crm_sub_app_col_elig_deb_sec(BigDecimal r156_crm_sub_app_col_elig_deb_sec) {
		this.r156_crm_sub_app_col_elig_deb_sec = r156_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR156_crm_sub_app_col_elig_euiq() {
		return r156_crm_sub_app_col_elig_euiq;
	}
	public void setR156_crm_sub_app_col_elig_euiq(BigDecimal r156_crm_sub_app_col_elig_euiq) {
		this.r156_crm_sub_app_col_elig_euiq = r156_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR156_crm_sub_app_col_elig_uni_tru() {
		return r156_crm_sub_app_col_elig_uni_tru;
	}
	public void setR156_crm_sub_app_col_elig_uni_tru(BigDecimal r156_crm_sub_app_col_elig_uni_tru) {
		this.r156_crm_sub_app_col_elig_uni_tru = r156_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR156_crm_sub_app_col_cea_cov() {
		return r156_crm_sub_app_col_cea_cov;
	}
	public void setR156_crm_sub_app_col_cea_cov(BigDecimal r156_crm_sub_app_col_cea_cov) {
		this.r156_crm_sub_app_col_cea_cov = r156_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR156_crm_sub_app_col_cea_not_cov() {
		return r156_crm_sub_app_col_cea_not_cov;
	}
	public void setR156_crm_sub_app_col_cea_not_cov(BigDecimal r156_crm_sub_app_col_cea_not_cov) {
		this.r156_crm_sub_app_col_cea_not_cov = r156_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR156_crm_sub_app_rwa_ris_wei_crm() {
		return r156_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR156_crm_sub_app_rwa_ris_wei_crm(BigDecimal r156_crm_sub_app_rwa_ris_wei_crm) {
		this.r156_crm_sub_app_rwa_ris_wei_crm = r156_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR156_crm_sub_app_rwa_ris_cea_cov() {
		return r156_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR156_crm_sub_app_rwa_ris_cea_cov(BigDecimal r156_crm_sub_app_rwa_ris_cea_cov) {
		this.r156_crm_sub_app_rwa_ris_cea_cov = r156_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR156_crm_sub_app_rwa_appl_org_coun() {
		return r156_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR156_crm_sub_app_rwa_appl_org_coun(BigDecimal r156_crm_sub_app_rwa_appl_org_coun) {
		this.r156_crm_sub_app_rwa_appl_org_coun = r156_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR156_crm_sub_app_rwa_ris_cea_not_cov() {
		return r156_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR156_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r156_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r156_crm_sub_app_rwa_ris_cea_not_cov = r156_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR156_crm_com_app_col_cea_elig_crm() {
		return r156_crm_com_app_col_cea_elig_crm;
	}
	public void setR156_crm_com_app_col_cea_elig_crm(BigDecimal r156_crm_com_app_col_cea_elig_crm) {
		this.r156_crm_com_app_col_cea_elig_crm = r156_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR156_crm_com_app_col_elig_cea_vol_adj() {
		return r156_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR156_crm_com_app_col_elig_cea_vol_adj(BigDecimal r156_crm_com_app_col_elig_cea_vol_adj) {
		this.r156_crm_com_app_col_elig_cea_vol_adj = r156_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR156_crm_com_app_col_elig_fin_hai() {
		return r156_crm_com_app_col_elig_fin_hai;
	}
	public void setR156_crm_com_app_col_elig_fin_hai(BigDecimal r156_crm_com_app_col_elig_fin_hai) {
		this.r156_crm_com_app_col_elig_fin_hai = r156_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR156_crm_com_app_col_cea_val_aft_crm() {
		return r156_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR156_crm_com_app_col_cea_val_aft_crm(BigDecimal r156_crm_com_app_col_cea_val_aft_crm) {
		this.r156_crm_com_app_col_cea_val_aft_crm = r156_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR156_rwa_elig_cea_not_cov() {
		return r156_rwa_elig_cea_not_cov;
	}
	public void setR156_rwa_elig_cea_not_cov(BigDecimal r156_rwa_elig_cea_not_cov) {
		this.r156_rwa_elig_cea_not_cov = r156_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR156_rwa_unsec_cea_sub_cre_ris() {
		return r156_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR156_rwa_unsec_cea_sub_cre_ris(BigDecimal r156_rwa_unsec_cea_sub_cre_ris) {
		this.r156_rwa_unsec_cea_sub_cre_ris = r156_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR156_rwa_unsec_cea() {
		return r156_rwa_unsec_cea;
	}
	public void setR156_rwa_unsec_cea(BigDecimal r156_rwa_unsec_cea) {
		this.r156_rwa_unsec_cea = r156_rwa_unsec_cea;
	}
	public BigDecimal getR156_rwa_tot_ris_wei_ass() {
		return r156_rwa_tot_ris_wei_ass;
	}
	public void setR156_rwa_tot_ris_wei_ass(BigDecimal r156_rwa_tot_ris_wei_ass) {
		this.r156_rwa_tot_ris_wei_ass = r156_rwa_tot_ris_wei_ass;
	}
	public String getR157_exposure_class_off_bal() {
		return r157_exposure_class_off_bal;
	}
	public void setR157_exposure_class_off_bal(String r157_exposure_class_off_bal) {
		this.r157_exposure_class_off_bal = r157_exposure_class_off_bal;
	}
	public BigDecimal getR157_nom_pri_amt() {
		return r157_nom_pri_amt;
	}
	public void setR157_nom_pri_amt(BigDecimal r157_nom_pri_amt) {
		this.r157_nom_pri_amt = r157_nom_pri_amt;
	}
	public BigDecimal getR157_ccf() {
		return r157_ccf;
	}
	public void setR157_ccf(BigDecimal r157_ccf) {
		this.r157_ccf = r157_ccf;
	}
	public BigDecimal getR157_cea() {
		return r157_cea;
	}
	public void setR157_cea(BigDecimal r157_cea) {
		this.r157_cea = r157_cea;
	}
	public BigDecimal getR157_cea_elig_coun_bilt_net() {
		return r157_cea_elig_coun_bilt_net;
	}
	public void setR157_cea_elig_coun_bilt_net(BigDecimal r157_cea_elig_coun_bilt_net) {
		this.r157_cea_elig_coun_bilt_net = r157_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR157_cea_aft_net() {
		return r157_cea_aft_net;
	}
	public void setR157_cea_aft_net(BigDecimal r157_cea_aft_net) {
		this.r157_cea_aft_net = r157_cea_aft_net;
	}
	public BigDecimal getR157_crm_sub_app_cea_elig() {
		return r157_crm_sub_app_cea_elig;
	}
	public void setR157_crm_sub_app_cea_elig(BigDecimal r157_crm_sub_app_cea_elig) {
		this.r157_crm_sub_app_cea_elig = r157_crm_sub_app_cea_elig;
	}
	public BigDecimal getR157_crm_sub_app_non_col_guar_elig() {
		return r157_crm_sub_app_non_col_guar_elig;
	}
	public void setR157_crm_sub_app_non_col_guar_elig(BigDecimal r157_crm_sub_app_non_col_guar_elig) {
		this.r157_crm_sub_app_non_col_guar_elig = r157_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR157_crm_sub_app_non_col_cre_der() {
		return r157_crm_sub_app_non_col_cre_der;
	}
	public void setR157_crm_sub_app_non_col_cre_der(BigDecimal r157_crm_sub_app_non_col_cre_der) {
		this.r157_crm_sub_app_non_col_cre_der = r157_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR157_crm_sub_app_col_elig_cash() {
		return r157_crm_sub_app_col_elig_cash;
	}
	public void setR157_crm_sub_app_col_elig_cash(BigDecimal r157_crm_sub_app_col_elig_cash) {
		this.r157_crm_sub_app_col_elig_cash = r157_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR157_crm_sub_app_col_elig_tre_bills() {
		return r157_crm_sub_app_col_elig_tre_bills;
	}
	public void setR157_crm_sub_app_col_elig_tre_bills(BigDecimal r157_crm_sub_app_col_elig_tre_bills) {
		this.r157_crm_sub_app_col_elig_tre_bills = r157_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR157_crm_sub_app_col_elig_deb_sec() {
		return r157_crm_sub_app_col_elig_deb_sec;
	}
	public void setR157_crm_sub_app_col_elig_deb_sec(BigDecimal r157_crm_sub_app_col_elig_deb_sec) {
		this.r157_crm_sub_app_col_elig_deb_sec = r157_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR157_crm_sub_app_col_elig_euiq() {
		return r157_crm_sub_app_col_elig_euiq;
	}
	public void setR157_crm_sub_app_col_elig_euiq(BigDecimal r157_crm_sub_app_col_elig_euiq) {
		this.r157_crm_sub_app_col_elig_euiq = r157_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR157_crm_sub_app_col_elig_uni_tru() {
		return r157_crm_sub_app_col_elig_uni_tru;
	}
	public void setR157_crm_sub_app_col_elig_uni_tru(BigDecimal r157_crm_sub_app_col_elig_uni_tru) {
		this.r157_crm_sub_app_col_elig_uni_tru = r157_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR157_crm_sub_app_col_cea_cov() {
		return r157_crm_sub_app_col_cea_cov;
	}
	public void setR157_crm_sub_app_col_cea_cov(BigDecimal r157_crm_sub_app_col_cea_cov) {
		this.r157_crm_sub_app_col_cea_cov = r157_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR157_crm_sub_app_col_cea_not_cov() {
		return r157_crm_sub_app_col_cea_not_cov;
	}
	public void setR157_crm_sub_app_col_cea_not_cov(BigDecimal r157_crm_sub_app_col_cea_not_cov) {
		this.r157_crm_sub_app_col_cea_not_cov = r157_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR157_crm_sub_app_rwa_ris_wei_crm() {
		return r157_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR157_crm_sub_app_rwa_ris_wei_crm(BigDecimal r157_crm_sub_app_rwa_ris_wei_crm) {
		this.r157_crm_sub_app_rwa_ris_wei_crm = r157_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR157_crm_sub_app_rwa_ris_cea_cov() {
		return r157_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR157_crm_sub_app_rwa_ris_cea_cov(BigDecimal r157_crm_sub_app_rwa_ris_cea_cov) {
		this.r157_crm_sub_app_rwa_ris_cea_cov = r157_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR157_crm_sub_app_rwa_appl_org_coun() {
		return r157_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR157_crm_sub_app_rwa_appl_org_coun(BigDecimal r157_crm_sub_app_rwa_appl_org_coun) {
		this.r157_crm_sub_app_rwa_appl_org_coun = r157_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR157_crm_sub_app_rwa_ris_cea_not_cov() {
		return r157_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR157_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r157_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r157_crm_sub_app_rwa_ris_cea_not_cov = r157_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR157_crm_com_app_col_cea_elig_crm() {
		return r157_crm_com_app_col_cea_elig_crm;
	}
	public void setR157_crm_com_app_col_cea_elig_crm(BigDecimal r157_crm_com_app_col_cea_elig_crm) {
		this.r157_crm_com_app_col_cea_elig_crm = r157_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR157_crm_com_app_col_elig_cea_vol_adj() {
		return r157_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR157_crm_com_app_col_elig_cea_vol_adj(BigDecimal r157_crm_com_app_col_elig_cea_vol_adj) {
		this.r157_crm_com_app_col_elig_cea_vol_adj = r157_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR157_crm_com_app_col_elig_fin_hai() {
		return r157_crm_com_app_col_elig_fin_hai;
	}
	public void setR157_crm_com_app_col_elig_fin_hai(BigDecimal r157_crm_com_app_col_elig_fin_hai) {
		this.r157_crm_com_app_col_elig_fin_hai = r157_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR157_crm_com_app_col_cea_val_aft_crm() {
		return r157_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR157_crm_com_app_col_cea_val_aft_crm(BigDecimal r157_crm_com_app_col_cea_val_aft_crm) {
		this.r157_crm_com_app_col_cea_val_aft_crm = r157_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR157_rwa_elig_cea_not_cov() {
		return r157_rwa_elig_cea_not_cov;
	}
	public void setR157_rwa_elig_cea_not_cov(BigDecimal r157_rwa_elig_cea_not_cov) {
		this.r157_rwa_elig_cea_not_cov = r157_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR157_rwa_unsec_cea_sub_cre_ris() {
		return r157_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR157_rwa_unsec_cea_sub_cre_ris(BigDecimal r157_rwa_unsec_cea_sub_cre_ris) {
		this.r157_rwa_unsec_cea_sub_cre_ris = r157_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR157_rwa_unsec_cea() {
		return r157_rwa_unsec_cea;
	}
	public void setR157_rwa_unsec_cea(BigDecimal r157_rwa_unsec_cea) {
		this.r157_rwa_unsec_cea = r157_rwa_unsec_cea;
	}
	public BigDecimal getR157_rwa_tot_ris_wei_ass() {
		return r157_rwa_tot_ris_wei_ass;
	}
	public void setR157_rwa_tot_ris_wei_ass(BigDecimal r157_rwa_tot_ris_wei_ass) {
		this.r157_rwa_tot_ris_wei_ass = r157_rwa_tot_ris_wei_ass;
	}
	public String getR158_exposure_class_off_bal() {
		return r158_exposure_class_off_bal;
	}
	public void setR158_exposure_class_off_bal(String r158_exposure_class_off_bal) {
		this.r158_exposure_class_off_bal = r158_exposure_class_off_bal;
	}
	public BigDecimal getR158_ccf() {
		return r158_ccf;
	}
	public void setR158_ccf(BigDecimal r158_ccf) {
		this.r158_ccf = r158_ccf;
	}
	public BigDecimal getR158_cea() {
		return r158_cea;
	}
	public void setR158_cea(BigDecimal r158_cea) {
		this.r158_cea = r158_cea;
	}
	public BigDecimal getR158_cea_elig_coun_bilt_net() {
		return r158_cea_elig_coun_bilt_net;
	}
	public void setR158_cea_elig_coun_bilt_net(BigDecimal r158_cea_elig_coun_bilt_net) {
		this.r158_cea_elig_coun_bilt_net = r158_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR158_cea_aft_net() {
		return r158_cea_aft_net;
	}
	public void setR158_cea_aft_net(BigDecimal r158_cea_aft_net) {
		this.r158_cea_aft_net = r158_cea_aft_net;
	}
	public BigDecimal getR158_crm_sub_app_cea_elig() {
		return r158_crm_sub_app_cea_elig;
	}
	public void setR158_crm_sub_app_cea_elig(BigDecimal r158_crm_sub_app_cea_elig) {
		this.r158_crm_sub_app_cea_elig = r158_crm_sub_app_cea_elig;
	}
	public BigDecimal getR158_crm_sub_app_non_col_guar_elig() {
		return r158_crm_sub_app_non_col_guar_elig;
	}
	public void setR158_crm_sub_app_non_col_guar_elig(BigDecimal r158_crm_sub_app_non_col_guar_elig) {
		this.r158_crm_sub_app_non_col_guar_elig = r158_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR158_crm_sub_app_non_col_cre_der() {
		return r158_crm_sub_app_non_col_cre_der;
	}
	public void setR158_crm_sub_app_non_col_cre_der(BigDecimal r158_crm_sub_app_non_col_cre_der) {
		this.r158_crm_sub_app_non_col_cre_der = r158_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR158_crm_sub_app_col_elig_cash() {
		return r158_crm_sub_app_col_elig_cash;
	}
	public void setR158_crm_sub_app_col_elig_cash(BigDecimal r158_crm_sub_app_col_elig_cash) {
		this.r158_crm_sub_app_col_elig_cash = r158_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR158_crm_sub_app_col_elig_tre_bills() {
		return r158_crm_sub_app_col_elig_tre_bills;
	}
	public void setR158_crm_sub_app_col_elig_tre_bills(BigDecimal r158_crm_sub_app_col_elig_tre_bills) {
		this.r158_crm_sub_app_col_elig_tre_bills = r158_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR158_crm_sub_app_col_elig_deb_sec() {
		return r158_crm_sub_app_col_elig_deb_sec;
	}
	public void setR158_crm_sub_app_col_elig_deb_sec(BigDecimal r158_crm_sub_app_col_elig_deb_sec) {
		this.r158_crm_sub_app_col_elig_deb_sec = r158_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR158_crm_sub_app_col_elig_euiq() {
		return r158_crm_sub_app_col_elig_euiq;
	}
	public void setR158_crm_sub_app_col_elig_euiq(BigDecimal r158_crm_sub_app_col_elig_euiq) {
		this.r158_crm_sub_app_col_elig_euiq = r158_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR158_crm_sub_app_col_elig_uni_tru() {
		return r158_crm_sub_app_col_elig_uni_tru;
	}
	public void setR158_crm_sub_app_col_elig_uni_tru(BigDecimal r158_crm_sub_app_col_elig_uni_tru) {
		this.r158_crm_sub_app_col_elig_uni_tru = r158_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR158_crm_sub_app_col_cea_cov() {
		return r158_crm_sub_app_col_cea_cov;
	}
	public void setR158_crm_sub_app_col_cea_cov(BigDecimal r158_crm_sub_app_col_cea_cov) {
		this.r158_crm_sub_app_col_cea_cov = r158_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR158_crm_sub_app_col_cea_not_cov() {
		return r158_crm_sub_app_col_cea_not_cov;
	}
	public void setR158_crm_sub_app_col_cea_not_cov(BigDecimal r158_crm_sub_app_col_cea_not_cov) {
		this.r158_crm_sub_app_col_cea_not_cov = r158_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR158_crm_sub_app_rwa_ris_wei_crm() {
		return r158_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR158_crm_sub_app_rwa_ris_wei_crm(BigDecimal r158_crm_sub_app_rwa_ris_wei_crm) {
		this.r158_crm_sub_app_rwa_ris_wei_crm = r158_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR158_crm_sub_app_rwa_ris_cea_cov() {
		return r158_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR158_crm_sub_app_rwa_ris_cea_cov(BigDecimal r158_crm_sub_app_rwa_ris_cea_cov) {
		this.r158_crm_sub_app_rwa_ris_cea_cov = r158_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR158_crm_sub_app_rwa_appl_org_coun() {
		return r158_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR158_crm_sub_app_rwa_appl_org_coun(BigDecimal r158_crm_sub_app_rwa_appl_org_coun) {
		this.r158_crm_sub_app_rwa_appl_org_coun = r158_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR158_crm_sub_app_rwa_ris_cea_not_cov() {
		return r158_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR158_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r158_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r158_crm_sub_app_rwa_ris_cea_not_cov = r158_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR158_crm_com_app_col_cea_elig_crm() {
		return r158_crm_com_app_col_cea_elig_crm;
	}
	public void setR158_crm_com_app_col_cea_elig_crm(BigDecimal r158_crm_com_app_col_cea_elig_crm) {
		this.r158_crm_com_app_col_cea_elig_crm = r158_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR158_crm_com_app_col_elig_cea_vol_adj() {
		return r158_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR158_crm_com_app_col_elig_cea_vol_adj(BigDecimal r158_crm_com_app_col_elig_cea_vol_adj) {
		this.r158_crm_com_app_col_elig_cea_vol_adj = r158_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR158_crm_com_app_col_elig_fin_hai() {
		return r158_crm_com_app_col_elig_fin_hai;
	}
	public void setR158_crm_com_app_col_elig_fin_hai(BigDecimal r158_crm_com_app_col_elig_fin_hai) {
		this.r158_crm_com_app_col_elig_fin_hai = r158_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR158_crm_com_app_col_cea_val_aft_crm() {
		return r158_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR158_crm_com_app_col_cea_val_aft_crm(BigDecimal r158_crm_com_app_col_cea_val_aft_crm) {
		this.r158_crm_com_app_col_cea_val_aft_crm = r158_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR158_rwa_elig_cea_not_cov() {
		return r158_rwa_elig_cea_not_cov;
	}
	public void setR158_rwa_elig_cea_not_cov(BigDecimal r158_rwa_elig_cea_not_cov) {
		this.r158_rwa_elig_cea_not_cov = r158_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR158_rwa_unsec_cea_sub_cre_ris() {
		return r158_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR158_rwa_unsec_cea_sub_cre_ris(BigDecimal r158_rwa_unsec_cea_sub_cre_ris) {
		this.r158_rwa_unsec_cea_sub_cre_ris = r158_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR158_rwa_unsec_cea() {
		return r158_rwa_unsec_cea;
	}
	public void setR158_rwa_unsec_cea(BigDecimal r158_rwa_unsec_cea) {
		this.r158_rwa_unsec_cea = r158_rwa_unsec_cea;
	}
	public BigDecimal getR158_rwa_tot_ris_wei_ass() {
		return r158_rwa_tot_ris_wei_ass;
	}
	public void setR158_rwa_tot_ris_wei_ass(BigDecimal r158_rwa_tot_ris_wei_ass) {
		this.r158_rwa_tot_ris_wei_ass = r158_rwa_tot_ris_wei_ass;
	}
	public String getR159_exposure_class_off_bal() {
		return r159_exposure_class_off_bal;
	}
	public void setR159_exposure_class_off_bal(String r159_exposure_class_off_bal) {
		this.r159_exposure_class_off_bal = r159_exposure_class_off_bal;
	}
	public BigDecimal getR159_nom_pri_amt() {
		return r159_nom_pri_amt;
	}
	public void setR159_nom_pri_amt(BigDecimal r159_nom_pri_amt) {
		this.r159_nom_pri_amt = r159_nom_pri_amt;
	}
	public BigDecimal getR159_ccf() {
		return r159_ccf;
	}
	public void setR159_ccf(BigDecimal r159_ccf) {
		this.r159_ccf = r159_ccf;
	}
	public BigDecimal getR159_cea() {
		return r159_cea;
	}
	public void setR159_cea(BigDecimal r159_cea) {
		this.r159_cea = r159_cea;
	}
	public BigDecimal getR159_cea_elig_coun_bilt_net() {
		return r159_cea_elig_coun_bilt_net;
	}
	public void setR159_cea_elig_coun_bilt_net(BigDecimal r159_cea_elig_coun_bilt_net) {
		this.r159_cea_elig_coun_bilt_net = r159_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR159_cea_aft_net() {
		return r159_cea_aft_net;
	}
	public void setR159_cea_aft_net(BigDecimal r159_cea_aft_net) {
		this.r159_cea_aft_net = r159_cea_aft_net;
	}
	public BigDecimal getR159_crm_sub_app_cea_elig() {
		return r159_crm_sub_app_cea_elig;
	}
	public void setR159_crm_sub_app_cea_elig(BigDecimal r159_crm_sub_app_cea_elig) {
		this.r159_crm_sub_app_cea_elig = r159_crm_sub_app_cea_elig;
	}
	public BigDecimal getR159_crm_sub_app_non_col_guar_elig() {
		return r159_crm_sub_app_non_col_guar_elig;
	}
	public void setR159_crm_sub_app_non_col_guar_elig(BigDecimal r159_crm_sub_app_non_col_guar_elig) {
		this.r159_crm_sub_app_non_col_guar_elig = r159_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR159_crm_sub_app_non_col_cre_der() {
		return r159_crm_sub_app_non_col_cre_der;
	}
	public void setR159_crm_sub_app_non_col_cre_der(BigDecimal r159_crm_sub_app_non_col_cre_der) {
		this.r159_crm_sub_app_non_col_cre_der = r159_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR159_crm_sub_app_col_elig_cash() {
		return r159_crm_sub_app_col_elig_cash;
	}
	public void setR159_crm_sub_app_col_elig_cash(BigDecimal r159_crm_sub_app_col_elig_cash) {
		this.r159_crm_sub_app_col_elig_cash = r159_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR159_crm_sub_app_col_elig_tre_bills() {
		return r159_crm_sub_app_col_elig_tre_bills;
	}
	public void setR159_crm_sub_app_col_elig_tre_bills(BigDecimal r159_crm_sub_app_col_elig_tre_bills) {
		this.r159_crm_sub_app_col_elig_tre_bills = r159_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR159_crm_sub_app_col_elig_deb_sec() {
		return r159_crm_sub_app_col_elig_deb_sec;
	}
	public void setR159_crm_sub_app_col_elig_deb_sec(BigDecimal r159_crm_sub_app_col_elig_deb_sec) {
		this.r159_crm_sub_app_col_elig_deb_sec = r159_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR159_crm_sub_app_col_elig_euiq() {
		return r159_crm_sub_app_col_elig_euiq;
	}
	public void setR159_crm_sub_app_col_elig_euiq(BigDecimal r159_crm_sub_app_col_elig_euiq) {
		this.r159_crm_sub_app_col_elig_euiq = r159_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR159_crm_sub_app_col_elig_uni_tru() {
		return r159_crm_sub_app_col_elig_uni_tru;
	}
	public void setR159_crm_sub_app_col_elig_uni_tru(BigDecimal r159_crm_sub_app_col_elig_uni_tru) {
		this.r159_crm_sub_app_col_elig_uni_tru = r159_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR159_crm_sub_app_col_cea_cov() {
		return r159_crm_sub_app_col_cea_cov;
	}
	public void setR159_crm_sub_app_col_cea_cov(BigDecimal r159_crm_sub_app_col_cea_cov) {
		this.r159_crm_sub_app_col_cea_cov = r159_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR159_crm_sub_app_col_cea_not_cov() {
		return r159_crm_sub_app_col_cea_not_cov;
	}
	public void setR159_crm_sub_app_col_cea_not_cov(BigDecimal r159_crm_sub_app_col_cea_not_cov) {
		this.r159_crm_sub_app_col_cea_not_cov = r159_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR159_crm_sub_app_rwa_ris_wei_crm() {
		return r159_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR159_crm_sub_app_rwa_ris_wei_crm(BigDecimal r159_crm_sub_app_rwa_ris_wei_crm) {
		this.r159_crm_sub_app_rwa_ris_wei_crm = r159_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR159_crm_sub_app_rwa_ris_cea_cov() {
		return r159_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR159_crm_sub_app_rwa_ris_cea_cov(BigDecimal r159_crm_sub_app_rwa_ris_cea_cov) {
		this.r159_crm_sub_app_rwa_ris_cea_cov = r159_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR159_crm_sub_app_rwa_appl_org_coun() {
		return r159_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR159_crm_sub_app_rwa_appl_org_coun(BigDecimal r159_crm_sub_app_rwa_appl_org_coun) {
		this.r159_crm_sub_app_rwa_appl_org_coun = r159_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR159_crm_sub_app_rwa_ris_cea_not_cov() {
		return r159_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR159_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r159_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r159_crm_sub_app_rwa_ris_cea_not_cov = r159_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR159_crm_com_app_col_cea_elig_crm() {
		return r159_crm_com_app_col_cea_elig_crm;
	}
	public void setR159_crm_com_app_col_cea_elig_crm(BigDecimal r159_crm_com_app_col_cea_elig_crm) {
		this.r159_crm_com_app_col_cea_elig_crm = r159_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR159_crm_com_app_col_elig_cea_vol_adj() {
		return r159_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR159_crm_com_app_col_elig_cea_vol_adj(BigDecimal r159_crm_com_app_col_elig_cea_vol_adj) {
		this.r159_crm_com_app_col_elig_cea_vol_adj = r159_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR159_crm_com_app_col_elig_fin_hai() {
		return r159_crm_com_app_col_elig_fin_hai;
	}
	public void setR159_crm_com_app_col_elig_fin_hai(BigDecimal r159_crm_com_app_col_elig_fin_hai) {
		this.r159_crm_com_app_col_elig_fin_hai = r159_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR159_crm_com_app_col_cea_val_aft_crm() {
		return r159_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR159_crm_com_app_col_cea_val_aft_crm(BigDecimal r159_crm_com_app_col_cea_val_aft_crm) {
		this.r159_crm_com_app_col_cea_val_aft_crm = r159_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR159_rwa_elig_cea_not_cov() {
		return r159_rwa_elig_cea_not_cov;
	}
	public void setR159_rwa_elig_cea_not_cov(BigDecimal r159_rwa_elig_cea_not_cov) {
		this.r159_rwa_elig_cea_not_cov = r159_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR159_rwa_unsec_cea_sub_cre_ris() {
		return r159_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR159_rwa_unsec_cea_sub_cre_ris(BigDecimal r159_rwa_unsec_cea_sub_cre_ris) {
		this.r159_rwa_unsec_cea_sub_cre_ris = r159_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR159_rwa_unsec_cea() {
		return r159_rwa_unsec_cea;
	}
	public void setR159_rwa_unsec_cea(BigDecimal r159_rwa_unsec_cea) {
		this.r159_rwa_unsec_cea = r159_rwa_unsec_cea;
	}
	public BigDecimal getR159_rwa_tot_ris_wei_ass() {
		return r159_rwa_tot_ris_wei_ass;
	}
	public void setR159_rwa_tot_ris_wei_ass(BigDecimal r159_rwa_tot_ris_wei_ass) {
		this.r159_rwa_tot_ris_wei_ass = r159_rwa_tot_ris_wei_ass;
	}
	public String getR160_exposure_class_off_bal() {
		return r160_exposure_class_off_bal;
	}
	public void setR160_exposure_class_off_bal(String r160_exposure_class_off_bal) {
		this.r160_exposure_class_off_bal = r160_exposure_class_off_bal;
	}
	public BigDecimal getR160_nom_pri_amt() {
		return r160_nom_pri_amt;
	}
	public void setR160_nom_pri_amt(BigDecimal r160_nom_pri_amt) {
		this.r160_nom_pri_amt = r160_nom_pri_amt;
	}
	public BigDecimal getR160_ccf() {
		return r160_ccf;
	}
	public void setR160_ccf(BigDecimal r160_ccf) {
		this.r160_ccf = r160_ccf;
	}
	public BigDecimal getR160_cea() {
		return r160_cea;
	}
	public void setR160_cea(BigDecimal r160_cea) {
		this.r160_cea = r160_cea;
	}
	public BigDecimal getR160_cea_elig_coun_bilt_net() {
		return r160_cea_elig_coun_bilt_net;
	}
	public void setR160_cea_elig_coun_bilt_net(BigDecimal r160_cea_elig_coun_bilt_net) {
		this.r160_cea_elig_coun_bilt_net = r160_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR160_cea_aft_net() {
		return r160_cea_aft_net;
	}
	public void setR160_cea_aft_net(BigDecimal r160_cea_aft_net) {
		this.r160_cea_aft_net = r160_cea_aft_net;
	}
	public BigDecimal getR160_crm_sub_app_cea_elig() {
		return r160_crm_sub_app_cea_elig;
	}
	public void setR160_crm_sub_app_cea_elig(BigDecimal r160_crm_sub_app_cea_elig) {
		this.r160_crm_sub_app_cea_elig = r160_crm_sub_app_cea_elig;
	}
	public BigDecimal getR160_crm_sub_app_non_col_guar_elig() {
		return r160_crm_sub_app_non_col_guar_elig;
	}
	public void setR160_crm_sub_app_non_col_guar_elig(BigDecimal r160_crm_sub_app_non_col_guar_elig) {
		this.r160_crm_sub_app_non_col_guar_elig = r160_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR160_crm_sub_app_non_col_cre_der() {
		return r160_crm_sub_app_non_col_cre_der;
	}
	public void setR160_crm_sub_app_non_col_cre_der(BigDecimal r160_crm_sub_app_non_col_cre_der) {
		this.r160_crm_sub_app_non_col_cre_der = r160_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR160_crm_sub_app_col_elig_cash() {
		return r160_crm_sub_app_col_elig_cash;
	}
	public void setR160_crm_sub_app_col_elig_cash(BigDecimal r160_crm_sub_app_col_elig_cash) {
		this.r160_crm_sub_app_col_elig_cash = r160_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR160_crm_sub_app_col_elig_tre_bills() {
		return r160_crm_sub_app_col_elig_tre_bills;
	}
	public void setR160_crm_sub_app_col_elig_tre_bills(BigDecimal r160_crm_sub_app_col_elig_tre_bills) {
		this.r160_crm_sub_app_col_elig_tre_bills = r160_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR160_crm_sub_app_col_elig_deb_sec() {
		return r160_crm_sub_app_col_elig_deb_sec;
	}
	public void setR160_crm_sub_app_col_elig_deb_sec(BigDecimal r160_crm_sub_app_col_elig_deb_sec) {
		this.r160_crm_sub_app_col_elig_deb_sec = r160_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR160_crm_sub_app_col_elig_euiq() {
		return r160_crm_sub_app_col_elig_euiq;
	}
	public void setR160_crm_sub_app_col_elig_euiq(BigDecimal r160_crm_sub_app_col_elig_euiq) {
		this.r160_crm_sub_app_col_elig_euiq = r160_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR160_crm_sub_app_col_elig_uni_tru() {
		return r160_crm_sub_app_col_elig_uni_tru;
	}
	public void setR160_crm_sub_app_col_elig_uni_tru(BigDecimal r160_crm_sub_app_col_elig_uni_tru) {
		this.r160_crm_sub_app_col_elig_uni_tru = r160_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR160_crm_sub_app_col_cea_cov() {
		return r160_crm_sub_app_col_cea_cov;
	}
	public void setR160_crm_sub_app_col_cea_cov(BigDecimal r160_crm_sub_app_col_cea_cov) {
		this.r160_crm_sub_app_col_cea_cov = r160_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR160_crm_sub_app_col_cea_not_cov() {
		return r160_crm_sub_app_col_cea_not_cov;
	}
	public void setR160_crm_sub_app_col_cea_not_cov(BigDecimal r160_crm_sub_app_col_cea_not_cov) {
		this.r160_crm_sub_app_col_cea_not_cov = r160_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR160_crm_sub_app_rwa_ris_wei_crm() {
		return r160_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR160_crm_sub_app_rwa_ris_wei_crm(BigDecimal r160_crm_sub_app_rwa_ris_wei_crm) {
		this.r160_crm_sub_app_rwa_ris_wei_crm = r160_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR160_crm_sub_app_rwa_ris_cea_cov() {
		return r160_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR160_crm_sub_app_rwa_ris_cea_cov(BigDecimal r160_crm_sub_app_rwa_ris_cea_cov) {
		this.r160_crm_sub_app_rwa_ris_cea_cov = r160_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR160_crm_sub_app_rwa_appl_org_coun() {
		return r160_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR160_crm_sub_app_rwa_appl_org_coun(BigDecimal r160_crm_sub_app_rwa_appl_org_coun) {
		this.r160_crm_sub_app_rwa_appl_org_coun = r160_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR160_crm_sub_app_rwa_ris_cea_not_cov() {
		return r160_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR160_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r160_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r160_crm_sub_app_rwa_ris_cea_not_cov = r160_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR160_crm_com_app_col_cea_elig_crm() {
		return r160_crm_com_app_col_cea_elig_crm;
	}
	public void setR160_crm_com_app_col_cea_elig_crm(BigDecimal r160_crm_com_app_col_cea_elig_crm) {
		this.r160_crm_com_app_col_cea_elig_crm = r160_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR160_crm_com_app_col_elig_cea_vol_adj() {
		return r160_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR160_crm_com_app_col_elig_cea_vol_adj(BigDecimal r160_crm_com_app_col_elig_cea_vol_adj) {
		this.r160_crm_com_app_col_elig_cea_vol_adj = r160_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR160_crm_com_app_col_elig_fin_hai() {
		return r160_crm_com_app_col_elig_fin_hai;
	}
	public void setR160_crm_com_app_col_elig_fin_hai(BigDecimal r160_crm_com_app_col_elig_fin_hai) {
		this.r160_crm_com_app_col_elig_fin_hai = r160_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR160_crm_com_app_col_cea_val_aft_crm() {
		return r160_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR160_crm_com_app_col_cea_val_aft_crm(BigDecimal r160_crm_com_app_col_cea_val_aft_crm) {
		this.r160_crm_com_app_col_cea_val_aft_crm = r160_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR160_rwa_elig_cea_not_cov() {
		return r160_rwa_elig_cea_not_cov;
	}
	public void setR160_rwa_elig_cea_not_cov(BigDecimal r160_rwa_elig_cea_not_cov) {
		this.r160_rwa_elig_cea_not_cov = r160_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR160_rwa_unsec_cea_sub_cre_ris() {
		return r160_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR160_rwa_unsec_cea_sub_cre_ris(BigDecimal r160_rwa_unsec_cea_sub_cre_ris) {
		this.r160_rwa_unsec_cea_sub_cre_ris = r160_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR160_rwa_unsec_cea() {
		return r160_rwa_unsec_cea;
	}
	public void setR160_rwa_unsec_cea(BigDecimal r160_rwa_unsec_cea) {
		this.r160_rwa_unsec_cea = r160_rwa_unsec_cea;
	}
	public BigDecimal getR160_rwa_tot_ris_wei_ass() {
		return r160_rwa_tot_ris_wei_ass;
	}
	public void setR160_rwa_tot_ris_wei_ass(BigDecimal r160_rwa_tot_ris_wei_ass) {
		this.r160_rwa_tot_ris_wei_ass = r160_rwa_tot_ris_wei_ass;
	}
	public String getR161_exposure_class_off_bal() {
		return r161_exposure_class_off_bal;
	}
	public void setR161_exposure_class_off_bal(String r161_exposure_class_off_bal) {
		this.r161_exposure_class_off_bal = r161_exposure_class_off_bal;
	}
	public BigDecimal getR161_nom_pri_amt() {
		return r161_nom_pri_amt;
	}
	public void setR161_nom_pri_amt(BigDecimal r161_nom_pri_amt) {
		this.r161_nom_pri_amt = r161_nom_pri_amt;
	}
	public BigDecimal getR161_ccf() {
		return r161_ccf;
	}
	public void setR161_ccf(BigDecimal r161_ccf) {
		this.r161_ccf = r161_ccf;
	}
	public BigDecimal getR161_cea() {
		return r161_cea;
	}
	public void setR161_cea(BigDecimal r161_cea) {
		this.r161_cea = r161_cea;
	}
	public BigDecimal getR161_cea_elig_coun_bilt_net() {
		return r161_cea_elig_coun_bilt_net;
	}
	public void setR161_cea_elig_coun_bilt_net(BigDecimal r161_cea_elig_coun_bilt_net) {
		this.r161_cea_elig_coun_bilt_net = r161_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR161_cea_aft_net() {
		return r161_cea_aft_net;
	}
	public void setR161_cea_aft_net(BigDecimal r161_cea_aft_net) {
		this.r161_cea_aft_net = r161_cea_aft_net;
	}
	public BigDecimal getR161_crm_sub_app_cea_elig() {
		return r161_crm_sub_app_cea_elig;
	}
	public void setR161_crm_sub_app_cea_elig(BigDecimal r161_crm_sub_app_cea_elig) {
		this.r161_crm_sub_app_cea_elig = r161_crm_sub_app_cea_elig;
	}
	public BigDecimal getR161_crm_sub_app_non_col_guar_elig() {
		return r161_crm_sub_app_non_col_guar_elig;
	}
	public void setR161_crm_sub_app_non_col_guar_elig(BigDecimal r161_crm_sub_app_non_col_guar_elig) {
		this.r161_crm_sub_app_non_col_guar_elig = r161_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR161_crm_sub_app_non_col_cre_der() {
		return r161_crm_sub_app_non_col_cre_der;
	}
	public void setR161_crm_sub_app_non_col_cre_der(BigDecimal r161_crm_sub_app_non_col_cre_der) {
		this.r161_crm_sub_app_non_col_cre_der = r161_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR161_crm_sub_app_col_elig_cash() {
		return r161_crm_sub_app_col_elig_cash;
	}
	public void setR161_crm_sub_app_col_elig_cash(BigDecimal r161_crm_sub_app_col_elig_cash) {
		this.r161_crm_sub_app_col_elig_cash = r161_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR161_crm_sub_app_col_elig_tre_bills() {
		return r161_crm_sub_app_col_elig_tre_bills;
	}
	public void setR161_crm_sub_app_col_elig_tre_bills(BigDecimal r161_crm_sub_app_col_elig_tre_bills) {
		this.r161_crm_sub_app_col_elig_tre_bills = r161_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR161_crm_sub_app_col_elig_deb_sec() {
		return r161_crm_sub_app_col_elig_deb_sec;
	}
	public void setR161_crm_sub_app_col_elig_deb_sec(BigDecimal r161_crm_sub_app_col_elig_deb_sec) {
		this.r161_crm_sub_app_col_elig_deb_sec = r161_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR161_crm_sub_app_col_elig_euiq() {
		return r161_crm_sub_app_col_elig_euiq;
	}
	public void setR161_crm_sub_app_col_elig_euiq(BigDecimal r161_crm_sub_app_col_elig_euiq) {
		this.r161_crm_sub_app_col_elig_euiq = r161_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR161_crm_sub_app_col_elig_uni_tru() {
		return r161_crm_sub_app_col_elig_uni_tru;
	}
	public void setR161_crm_sub_app_col_elig_uni_tru(BigDecimal r161_crm_sub_app_col_elig_uni_tru) {
		this.r161_crm_sub_app_col_elig_uni_tru = r161_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR161_crm_sub_app_col_cea_cov() {
		return r161_crm_sub_app_col_cea_cov;
	}
	public void setR161_crm_sub_app_col_cea_cov(BigDecimal r161_crm_sub_app_col_cea_cov) {
		this.r161_crm_sub_app_col_cea_cov = r161_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR161_crm_sub_app_col_cea_not_cov() {
		return r161_crm_sub_app_col_cea_not_cov;
	}
	public void setR161_crm_sub_app_col_cea_not_cov(BigDecimal r161_crm_sub_app_col_cea_not_cov) {
		this.r161_crm_sub_app_col_cea_not_cov = r161_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR161_crm_sub_app_rwa_ris_wei_crm() {
		return r161_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR161_crm_sub_app_rwa_ris_wei_crm(BigDecimal r161_crm_sub_app_rwa_ris_wei_crm) {
		this.r161_crm_sub_app_rwa_ris_wei_crm = r161_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR161_crm_sub_app_rwa_ris_cea_cov() {
		return r161_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR161_crm_sub_app_rwa_ris_cea_cov(BigDecimal r161_crm_sub_app_rwa_ris_cea_cov) {
		this.r161_crm_sub_app_rwa_ris_cea_cov = r161_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR161_crm_sub_app_rwa_appl_org_coun() {
		return r161_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR161_crm_sub_app_rwa_appl_org_coun(BigDecimal r161_crm_sub_app_rwa_appl_org_coun) {
		this.r161_crm_sub_app_rwa_appl_org_coun = r161_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR161_crm_sub_app_rwa_ris_cea_not_cov() {
		return r161_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR161_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r161_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r161_crm_sub_app_rwa_ris_cea_not_cov = r161_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR161_crm_com_app_col_cea_elig_crm() {
		return r161_crm_com_app_col_cea_elig_crm;
	}
	public void setR161_crm_com_app_col_cea_elig_crm(BigDecimal r161_crm_com_app_col_cea_elig_crm) {
		this.r161_crm_com_app_col_cea_elig_crm = r161_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR161_crm_com_app_col_elig_cea_vol_adj() {
		return r161_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR161_crm_com_app_col_elig_cea_vol_adj(BigDecimal r161_crm_com_app_col_elig_cea_vol_adj) {
		this.r161_crm_com_app_col_elig_cea_vol_adj = r161_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR161_crm_com_app_col_elig_fin_hai() {
		return r161_crm_com_app_col_elig_fin_hai;
	}
	public void setR161_crm_com_app_col_elig_fin_hai(BigDecimal r161_crm_com_app_col_elig_fin_hai) {
		this.r161_crm_com_app_col_elig_fin_hai = r161_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR161_crm_com_app_col_cea_val_aft_crm() {
		return r161_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR161_crm_com_app_col_cea_val_aft_crm(BigDecimal r161_crm_com_app_col_cea_val_aft_crm) {
		this.r161_crm_com_app_col_cea_val_aft_crm = r161_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR161_rwa_elig_cea_not_cov() {
		return r161_rwa_elig_cea_not_cov;
	}
	public void setR161_rwa_elig_cea_not_cov(BigDecimal r161_rwa_elig_cea_not_cov) {
		this.r161_rwa_elig_cea_not_cov = r161_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR161_rwa_unsec_cea_sub_cre_ris() {
		return r161_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR161_rwa_unsec_cea_sub_cre_ris(BigDecimal r161_rwa_unsec_cea_sub_cre_ris) {
		this.r161_rwa_unsec_cea_sub_cre_ris = r161_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR161_rwa_unsec_cea() {
		return r161_rwa_unsec_cea;
	}
	public void setR161_rwa_unsec_cea(BigDecimal r161_rwa_unsec_cea) {
		this.r161_rwa_unsec_cea = r161_rwa_unsec_cea;
	}
	public BigDecimal getR161_rwa_tot_ris_wei_ass() {
		return r161_rwa_tot_ris_wei_ass;
	}
	public void setR161_rwa_tot_ris_wei_ass(BigDecimal r161_rwa_tot_ris_wei_ass) {
		this.r161_rwa_tot_ris_wei_ass = r161_rwa_tot_ris_wei_ass;
	}
	public String getR162_exposure_class_off_bal() {
		return r162_exposure_class_off_bal;
	}
	public void setR162_exposure_class_off_bal(String r162_exposure_class_off_bal) {
		this.r162_exposure_class_off_bal = r162_exposure_class_off_bal;
	}
	public BigDecimal getR162_nom_pri_amt() {
		return r162_nom_pri_amt;
	}
	public void setR162_nom_pri_amt(BigDecimal r162_nom_pri_amt) {
		this.r162_nom_pri_amt = r162_nom_pri_amt;
	}
	public BigDecimal getR162_ccf() {
		return r162_ccf;
	}
	public void setR162_ccf(BigDecimal r162_ccf) {
		this.r162_ccf = r162_ccf;
	}
	public BigDecimal getR162_cea() {
		return r162_cea;
	}
	public void setR162_cea(BigDecimal r162_cea) {
		this.r162_cea = r162_cea;
	}
	public BigDecimal getR162_cea_elig_coun_bilt_net() {
		return r162_cea_elig_coun_bilt_net;
	}
	public void setR162_cea_elig_coun_bilt_net(BigDecimal r162_cea_elig_coun_bilt_net) {
		this.r162_cea_elig_coun_bilt_net = r162_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR162_cea_aft_net() {
		return r162_cea_aft_net;
	}
	public void setR162_cea_aft_net(BigDecimal r162_cea_aft_net) {
		this.r162_cea_aft_net = r162_cea_aft_net;
	}
	public BigDecimal getR162_crm_sub_app_cea_elig() {
		return r162_crm_sub_app_cea_elig;
	}
	public void setR162_crm_sub_app_cea_elig(BigDecimal r162_crm_sub_app_cea_elig) {
		this.r162_crm_sub_app_cea_elig = r162_crm_sub_app_cea_elig;
	}
	public BigDecimal getR162_crm_sub_app_non_col_guar_elig() {
		return r162_crm_sub_app_non_col_guar_elig;
	}
	public void setR162_crm_sub_app_non_col_guar_elig(BigDecimal r162_crm_sub_app_non_col_guar_elig) {
		this.r162_crm_sub_app_non_col_guar_elig = r162_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR162_crm_sub_app_non_col_cre_der() {
		return r162_crm_sub_app_non_col_cre_der;
	}
	public void setR162_crm_sub_app_non_col_cre_der(BigDecimal r162_crm_sub_app_non_col_cre_der) {
		this.r162_crm_sub_app_non_col_cre_der = r162_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR162_crm_sub_app_col_elig_cash() {
		return r162_crm_sub_app_col_elig_cash;
	}
	public void setR162_crm_sub_app_col_elig_cash(BigDecimal r162_crm_sub_app_col_elig_cash) {
		this.r162_crm_sub_app_col_elig_cash = r162_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR162_crm_sub_app_col_elig_tre_bills() {
		return r162_crm_sub_app_col_elig_tre_bills;
	}
	public void setR162_crm_sub_app_col_elig_tre_bills(BigDecimal r162_crm_sub_app_col_elig_tre_bills) {
		this.r162_crm_sub_app_col_elig_tre_bills = r162_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR162_crm_sub_app_col_elig_deb_sec() {
		return r162_crm_sub_app_col_elig_deb_sec;
	}
	public void setR162_crm_sub_app_col_elig_deb_sec(BigDecimal r162_crm_sub_app_col_elig_deb_sec) {
		this.r162_crm_sub_app_col_elig_deb_sec = r162_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR162_crm_sub_app_col_elig_euiq() {
		return r162_crm_sub_app_col_elig_euiq;
	}
	public void setR162_crm_sub_app_col_elig_euiq(BigDecimal r162_crm_sub_app_col_elig_euiq) {
		this.r162_crm_sub_app_col_elig_euiq = r162_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR162_crm_sub_app_col_elig_uni_tru() {
		return r162_crm_sub_app_col_elig_uni_tru;
	}
	public void setR162_crm_sub_app_col_elig_uni_tru(BigDecimal r162_crm_sub_app_col_elig_uni_tru) {
		this.r162_crm_sub_app_col_elig_uni_tru = r162_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR162_crm_sub_app_col_cea_cov() {
		return r162_crm_sub_app_col_cea_cov;
	}
	public void setR162_crm_sub_app_col_cea_cov(BigDecimal r162_crm_sub_app_col_cea_cov) {
		this.r162_crm_sub_app_col_cea_cov = r162_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR162_crm_sub_app_col_cea_not_cov() {
		return r162_crm_sub_app_col_cea_not_cov;
	}
	public void setR162_crm_sub_app_col_cea_not_cov(BigDecimal r162_crm_sub_app_col_cea_not_cov) {
		this.r162_crm_sub_app_col_cea_not_cov = r162_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR162_crm_sub_app_rwa_ris_wei_crm() {
		return r162_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR162_crm_sub_app_rwa_ris_wei_crm(BigDecimal r162_crm_sub_app_rwa_ris_wei_crm) {
		this.r162_crm_sub_app_rwa_ris_wei_crm = r162_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR162_crm_sub_app_rwa_ris_cea_cov() {
		return r162_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR162_crm_sub_app_rwa_ris_cea_cov(BigDecimal r162_crm_sub_app_rwa_ris_cea_cov) {
		this.r162_crm_sub_app_rwa_ris_cea_cov = r162_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR162_crm_sub_app_rwa_appl_org_coun() {
		return r162_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR162_crm_sub_app_rwa_appl_org_coun(BigDecimal r162_crm_sub_app_rwa_appl_org_coun) {
		this.r162_crm_sub_app_rwa_appl_org_coun = r162_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR162_crm_sub_app_rwa_ris_cea_not_cov() {
		return r162_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR162_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r162_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r162_crm_sub_app_rwa_ris_cea_not_cov = r162_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR162_crm_com_app_col_cea_elig_crm() {
		return r162_crm_com_app_col_cea_elig_crm;
	}
	public void setR162_crm_com_app_col_cea_elig_crm(BigDecimal r162_crm_com_app_col_cea_elig_crm) {
		this.r162_crm_com_app_col_cea_elig_crm = r162_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR162_crm_com_app_col_elig_cea_vol_adj() {
		return r162_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR162_crm_com_app_col_elig_cea_vol_adj(BigDecimal r162_crm_com_app_col_elig_cea_vol_adj) {
		this.r162_crm_com_app_col_elig_cea_vol_adj = r162_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR162_crm_com_app_col_elig_fin_hai() {
		return r162_crm_com_app_col_elig_fin_hai;
	}
	public void setR162_crm_com_app_col_elig_fin_hai(BigDecimal r162_crm_com_app_col_elig_fin_hai) {
		this.r162_crm_com_app_col_elig_fin_hai = r162_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR162_crm_com_app_col_cea_val_aft_crm() {
		return r162_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR162_crm_com_app_col_cea_val_aft_crm(BigDecimal r162_crm_com_app_col_cea_val_aft_crm) {
		this.r162_crm_com_app_col_cea_val_aft_crm = r162_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR162_rwa_elig_cea_not_cov() {
		return r162_rwa_elig_cea_not_cov;
	}
	public void setR162_rwa_elig_cea_not_cov(BigDecimal r162_rwa_elig_cea_not_cov) {
		this.r162_rwa_elig_cea_not_cov = r162_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR162_rwa_unsec_cea_sub_cre_ris() {
		return r162_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR162_rwa_unsec_cea_sub_cre_ris(BigDecimal r162_rwa_unsec_cea_sub_cre_ris) {
		this.r162_rwa_unsec_cea_sub_cre_ris = r162_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR162_rwa_unsec_cea() {
		return r162_rwa_unsec_cea;
	}
	public void setR162_rwa_unsec_cea(BigDecimal r162_rwa_unsec_cea) {
		this.r162_rwa_unsec_cea = r162_rwa_unsec_cea;
	}
	public BigDecimal getR162_rwa_tot_ris_wei_ass() {
		return r162_rwa_tot_ris_wei_ass;
	}
	public void setR162_rwa_tot_ris_wei_ass(BigDecimal r162_rwa_tot_ris_wei_ass) {
		this.r162_rwa_tot_ris_wei_ass = r162_rwa_tot_ris_wei_ass;
	}
	public String getR163_exposure_class_off_bal() {
		return r163_exposure_class_off_bal;
	}
	public void setR163_exposure_class_off_bal(String r163_exposure_class_off_bal) {
		this.r163_exposure_class_off_bal = r163_exposure_class_off_bal;
	}
	public BigDecimal getR163_nom_pri_amt() {
		return r163_nom_pri_amt;
	}
	public void setR163_nom_pri_amt(BigDecimal r163_nom_pri_amt) {
		this.r163_nom_pri_amt = r163_nom_pri_amt;
	}
	public BigDecimal getR163_ccf() {
		return r163_ccf;
	}
	public void setR163_ccf(BigDecimal r163_ccf) {
		this.r163_ccf = r163_ccf;
	}
	public BigDecimal getR163_cea() {
		return r163_cea;
	}
	public void setR163_cea(BigDecimal r163_cea) {
		this.r163_cea = r163_cea;
	}
	public BigDecimal getR163_cea_elig_coun_bilt_net() {
		return r163_cea_elig_coun_bilt_net;
	}
	public void setR163_cea_elig_coun_bilt_net(BigDecimal r163_cea_elig_coun_bilt_net) {
		this.r163_cea_elig_coun_bilt_net = r163_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR163_cea_aft_net() {
		return r163_cea_aft_net;
	}
	public void setR163_cea_aft_net(BigDecimal r163_cea_aft_net) {
		this.r163_cea_aft_net = r163_cea_aft_net;
	}
	public BigDecimal getR163_crm_sub_app_cea_elig() {
		return r163_crm_sub_app_cea_elig;
	}
	public void setR163_crm_sub_app_cea_elig(BigDecimal r163_crm_sub_app_cea_elig) {
		this.r163_crm_sub_app_cea_elig = r163_crm_sub_app_cea_elig;
	}
	public BigDecimal getR163_crm_sub_app_non_col_guar_elig() {
		return r163_crm_sub_app_non_col_guar_elig;
	}
	public void setR163_crm_sub_app_non_col_guar_elig(BigDecimal r163_crm_sub_app_non_col_guar_elig) {
		this.r163_crm_sub_app_non_col_guar_elig = r163_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR163_crm_sub_app_non_col_cre_der() {
		return r163_crm_sub_app_non_col_cre_der;
	}
	public void setR163_crm_sub_app_non_col_cre_der(BigDecimal r163_crm_sub_app_non_col_cre_der) {
		this.r163_crm_sub_app_non_col_cre_der = r163_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR163_crm_sub_app_col_elig_cash() {
		return r163_crm_sub_app_col_elig_cash;
	}
	public void setR163_crm_sub_app_col_elig_cash(BigDecimal r163_crm_sub_app_col_elig_cash) {
		this.r163_crm_sub_app_col_elig_cash = r163_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR163_crm_sub_app_col_elig_tre_bills() {
		return r163_crm_sub_app_col_elig_tre_bills;
	}
	public void setR163_crm_sub_app_col_elig_tre_bills(BigDecimal r163_crm_sub_app_col_elig_tre_bills) {
		this.r163_crm_sub_app_col_elig_tre_bills = r163_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR163_crm_sub_app_col_elig_deb_sec() {
		return r163_crm_sub_app_col_elig_deb_sec;
	}
	public void setR163_crm_sub_app_col_elig_deb_sec(BigDecimal r163_crm_sub_app_col_elig_deb_sec) {
		this.r163_crm_sub_app_col_elig_deb_sec = r163_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR163_crm_sub_app_col_elig_euiq() {
		return r163_crm_sub_app_col_elig_euiq;
	}
	public void setR163_crm_sub_app_col_elig_euiq(BigDecimal r163_crm_sub_app_col_elig_euiq) {
		this.r163_crm_sub_app_col_elig_euiq = r163_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR163_crm_sub_app_col_elig_uni_tru() {
		return r163_crm_sub_app_col_elig_uni_tru;
	}
	public void setR163_crm_sub_app_col_elig_uni_tru(BigDecimal r163_crm_sub_app_col_elig_uni_tru) {
		this.r163_crm_sub_app_col_elig_uni_tru = r163_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR163_crm_sub_app_col_cea_cov() {
		return r163_crm_sub_app_col_cea_cov;
	}
	public void setR163_crm_sub_app_col_cea_cov(BigDecimal r163_crm_sub_app_col_cea_cov) {
		this.r163_crm_sub_app_col_cea_cov = r163_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR163_crm_sub_app_col_cea_not_cov() {
		return r163_crm_sub_app_col_cea_not_cov;
	}
	public void setR163_crm_sub_app_col_cea_not_cov(BigDecimal r163_crm_sub_app_col_cea_not_cov) {
		this.r163_crm_sub_app_col_cea_not_cov = r163_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR163_crm_sub_app_rwa_ris_wei_crm() {
		return r163_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR163_crm_sub_app_rwa_ris_wei_crm(BigDecimal r163_crm_sub_app_rwa_ris_wei_crm) {
		this.r163_crm_sub_app_rwa_ris_wei_crm = r163_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR163_crm_sub_app_rwa_ris_cea_cov() {
		return r163_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR163_crm_sub_app_rwa_ris_cea_cov(BigDecimal r163_crm_sub_app_rwa_ris_cea_cov) {
		this.r163_crm_sub_app_rwa_ris_cea_cov = r163_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR163_crm_sub_app_rwa_appl_org_coun() {
		return r163_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR163_crm_sub_app_rwa_appl_org_coun(BigDecimal r163_crm_sub_app_rwa_appl_org_coun) {
		this.r163_crm_sub_app_rwa_appl_org_coun = r163_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR163_crm_sub_app_rwa_ris_cea_not_cov() {
		return r163_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR163_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r163_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r163_crm_sub_app_rwa_ris_cea_not_cov = r163_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR163_crm_com_app_col_cea_elig_crm() {
		return r163_crm_com_app_col_cea_elig_crm;
	}
	public void setR163_crm_com_app_col_cea_elig_crm(BigDecimal r163_crm_com_app_col_cea_elig_crm) {
		this.r163_crm_com_app_col_cea_elig_crm = r163_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR163_crm_com_app_col_elig_cea_vol_adj() {
		return r163_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR163_crm_com_app_col_elig_cea_vol_adj(BigDecimal r163_crm_com_app_col_elig_cea_vol_adj) {
		this.r163_crm_com_app_col_elig_cea_vol_adj = r163_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR163_crm_com_app_col_elig_fin_hai() {
		return r163_crm_com_app_col_elig_fin_hai;
	}
	public void setR163_crm_com_app_col_elig_fin_hai(BigDecimal r163_crm_com_app_col_elig_fin_hai) {
		this.r163_crm_com_app_col_elig_fin_hai = r163_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR163_crm_com_app_col_cea_val_aft_crm() {
		return r163_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR163_crm_com_app_col_cea_val_aft_crm(BigDecimal r163_crm_com_app_col_cea_val_aft_crm) {
		this.r163_crm_com_app_col_cea_val_aft_crm = r163_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR163_rwa_elig_cea_not_cov() {
		return r163_rwa_elig_cea_not_cov;
	}
	public void setR163_rwa_elig_cea_not_cov(BigDecimal r163_rwa_elig_cea_not_cov) {
		this.r163_rwa_elig_cea_not_cov = r163_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR163_rwa_unsec_cea_sub_cre_ris() {
		return r163_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR163_rwa_unsec_cea_sub_cre_ris(BigDecimal r163_rwa_unsec_cea_sub_cre_ris) {
		this.r163_rwa_unsec_cea_sub_cre_ris = r163_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR163_rwa_unsec_cea() {
		return r163_rwa_unsec_cea;
	}
	public void setR163_rwa_unsec_cea(BigDecimal r163_rwa_unsec_cea) {
		this.r163_rwa_unsec_cea = r163_rwa_unsec_cea;
	}
	public BigDecimal getR163_rwa_tot_ris_wei_ass() {
		return r163_rwa_tot_ris_wei_ass;
	}
	public void setR163_rwa_tot_ris_wei_ass(BigDecimal r163_rwa_tot_ris_wei_ass) {
		this.r163_rwa_tot_ris_wei_ass = r163_rwa_tot_ris_wei_ass;
	}
	public String getR164_exposure_class_off_bal() {
		return r164_exposure_class_off_bal;
	}
	public void setR164_exposure_class_off_bal(String r164_exposure_class_off_bal) {
		this.r164_exposure_class_off_bal = r164_exposure_class_off_bal;
	}
	public BigDecimal getR164_nom_pri_amt() {
		return r164_nom_pri_amt;
	}
	public void setR164_nom_pri_amt(BigDecimal r164_nom_pri_amt) {
		this.r164_nom_pri_amt = r164_nom_pri_amt;
	}
	public BigDecimal getR164_ccf() {
		return r164_ccf;
	}
	public void setR164_ccf(BigDecimal r164_ccf) {
		this.r164_ccf = r164_ccf;
	}
	public BigDecimal getR164_cea() {
		return r164_cea;
	}
	public void setR164_cea(BigDecimal r164_cea) {
		this.r164_cea = r164_cea;
	}
	public BigDecimal getR164_cea_elig_coun_bilt_net() {
		return r164_cea_elig_coun_bilt_net;
	}
	public void setR164_cea_elig_coun_bilt_net(BigDecimal r164_cea_elig_coun_bilt_net) {
		this.r164_cea_elig_coun_bilt_net = r164_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR164_cea_aft_net() {
		return r164_cea_aft_net;
	}
	public void setR164_cea_aft_net(BigDecimal r164_cea_aft_net) {
		this.r164_cea_aft_net = r164_cea_aft_net;
	}
	public BigDecimal getR164_crm_sub_app_cea_elig() {
		return r164_crm_sub_app_cea_elig;
	}
	public void setR164_crm_sub_app_cea_elig(BigDecimal r164_crm_sub_app_cea_elig) {
		this.r164_crm_sub_app_cea_elig = r164_crm_sub_app_cea_elig;
	}
	public BigDecimal getR164_crm_sub_app_non_col_guar_elig() {
		return r164_crm_sub_app_non_col_guar_elig;
	}
	public void setR164_crm_sub_app_non_col_guar_elig(BigDecimal r164_crm_sub_app_non_col_guar_elig) {
		this.r164_crm_sub_app_non_col_guar_elig = r164_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR164_crm_sub_app_non_col_cre_der() {
		return r164_crm_sub_app_non_col_cre_der;
	}
	public void setR164_crm_sub_app_non_col_cre_der(BigDecimal r164_crm_sub_app_non_col_cre_der) {
		this.r164_crm_sub_app_non_col_cre_der = r164_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR164_crm_sub_app_col_elig_cash() {
		return r164_crm_sub_app_col_elig_cash;
	}
	public void setR164_crm_sub_app_col_elig_cash(BigDecimal r164_crm_sub_app_col_elig_cash) {
		this.r164_crm_sub_app_col_elig_cash = r164_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR164_crm_sub_app_col_elig_tre_bills() {
		return r164_crm_sub_app_col_elig_tre_bills;
	}
	public void setR164_crm_sub_app_col_elig_tre_bills(BigDecimal r164_crm_sub_app_col_elig_tre_bills) {
		this.r164_crm_sub_app_col_elig_tre_bills = r164_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR164_crm_sub_app_col_elig_deb_sec() {
		return r164_crm_sub_app_col_elig_deb_sec;
	}
	public void setR164_crm_sub_app_col_elig_deb_sec(BigDecimal r164_crm_sub_app_col_elig_deb_sec) {
		this.r164_crm_sub_app_col_elig_deb_sec = r164_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR164_crm_sub_app_col_elig_euiq() {
		return r164_crm_sub_app_col_elig_euiq;
	}
	public void setR164_crm_sub_app_col_elig_euiq(BigDecimal r164_crm_sub_app_col_elig_euiq) {
		this.r164_crm_sub_app_col_elig_euiq = r164_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR164_crm_sub_app_col_elig_uni_tru() {
		return r164_crm_sub_app_col_elig_uni_tru;
	}
	public void setR164_crm_sub_app_col_elig_uni_tru(BigDecimal r164_crm_sub_app_col_elig_uni_tru) {
		this.r164_crm_sub_app_col_elig_uni_tru = r164_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR164_crm_sub_app_col_cea_cov() {
		return r164_crm_sub_app_col_cea_cov;
	}
	public void setR164_crm_sub_app_col_cea_cov(BigDecimal r164_crm_sub_app_col_cea_cov) {
		this.r164_crm_sub_app_col_cea_cov = r164_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR164_crm_sub_app_col_cea_not_cov() {
		return r164_crm_sub_app_col_cea_not_cov;
	}
	public void setR164_crm_sub_app_col_cea_not_cov(BigDecimal r164_crm_sub_app_col_cea_not_cov) {
		this.r164_crm_sub_app_col_cea_not_cov = r164_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR164_crm_sub_app_rwa_ris_wei_crm() {
		return r164_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR164_crm_sub_app_rwa_ris_wei_crm(BigDecimal r164_crm_sub_app_rwa_ris_wei_crm) {
		this.r164_crm_sub_app_rwa_ris_wei_crm = r164_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR164_crm_sub_app_rwa_ris_cea_cov() {
		return r164_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR164_crm_sub_app_rwa_ris_cea_cov(BigDecimal r164_crm_sub_app_rwa_ris_cea_cov) {
		this.r164_crm_sub_app_rwa_ris_cea_cov = r164_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR164_crm_sub_app_rwa_appl_org_coun() {
		return r164_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR164_crm_sub_app_rwa_appl_org_coun(BigDecimal r164_crm_sub_app_rwa_appl_org_coun) {
		this.r164_crm_sub_app_rwa_appl_org_coun = r164_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR164_crm_sub_app_rwa_ris_cea_not_cov() {
		return r164_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR164_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r164_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r164_crm_sub_app_rwa_ris_cea_not_cov = r164_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR164_crm_com_app_col_cea_elig_crm() {
		return r164_crm_com_app_col_cea_elig_crm;
	}
	public void setR164_crm_com_app_col_cea_elig_crm(BigDecimal r164_crm_com_app_col_cea_elig_crm) {
		this.r164_crm_com_app_col_cea_elig_crm = r164_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR164_crm_com_app_col_elig_cea_vol_adj() {
		return r164_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR164_crm_com_app_col_elig_cea_vol_adj(BigDecimal r164_crm_com_app_col_elig_cea_vol_adj) {
		this.r164_crm_com_app_col_elig_cea_vol_adj = r164_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR164_crm_com_app_col_elig_fin_hai() {
		return r164_crm_com_app_col_elig_fin_hai;
	}
	public void setR164_crm_com_app_col_elig_fin_hai(BigDecimal r164_crm_com_app_col_elig_fin_hai) {
		this.r164_crm_com_app_col_elig_fin_hai = r164_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR164_crm_com_app_col_cea_val_aft_crm() {
		return r164_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR164_crm_com_app_col_cea_val_aft_crm(BigDecimal r164_crm_com_app_col_cea_val_aft_crm) {
		this.r164_crm_com_app_col_cea_val_aft_crm = r164_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR164_rwa_elig_cea_not_cov() {
		return r164_rwa_elig_cea_not_cov;
	}
	public void setR164_rwa_elig_cea_not_cov(BigDecimal r164_rwa_elig_cea_not_cov) {
		this.r164_rwa_elig_cea_not_cov = r164_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR164_rwa_unsec_cea_sub_cre_ris() {
		return r164_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR164_rwa_unsec_cea_sub_cre_ris(BigDecimal r164_rwa_unsec_cea_sub_cre_ris) {
		this.r164_rwa_unsec_cea_sub_cre_ris = r164_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR164_rwa_unsec_cea() {
		return r164_rwa_unsec_cea;
	}
	public void setR164_rwa_unsec_cea(BigDecimal r164_rwa_unsec_cea) {
		this.r164_rwa_unsec_cea = r164_rwa_unsec_cea;
	}
	public BigDecimal getR164_rwa_tot_ris_wei_ass() {
		return r164_rwa_tot_ris_wei_ass;
	}
	public void setR164_rwa_tot_ris_wei_ass(BigDecimal r164_rwa_tot_ris_wei_ass) {
		this.r164_rwa_tot_ris_wei_ass = r164_rwa_tot_ris_wei_ass;
	}
	public String getR165_exposure_class_off_bal() {
		return r165_exposure_class_off_bal;
	}
	public void setR165_exposure_class_off_bal(String r165_exposure_class_off_bal) {
		this.r165_exposure_class_off_bal = r165_exposure_class_off_bal;
	}
	public BigDecimal getR165_nom_pri_amt() {
		return r165_nom_pri_amt;
	}
	public void setR165_nom_pri_amt(BigDecimal r165_nom_pri_amt) {
		this.r165_nom_pri_amt = r165_nom_pri_amt;
	}
	public BigDecimal getR165_ccf() {
		return r165_ccf;
	}
	public void setR165_ccf(BigDecimal r165_ccf) {
		this.r165_ccf = r165_ccf;
	}
	public BigDecimal getR165_cea() {
		return r165_cea;
	}
	public void setR165_cea(BigDecimal r165_cea) {
		this.r165_cea = r165_cea;
	}
	public BigDecimal getR165_cea_elig_coun_bilt_net() {
		return r165_cea_elig_coun_bilt_net;
	}
	public void setR165_cea_elig_coun_bilt_net(BigDecimal r165_cea_elig_coun_bilt_net) {
		this.r165_cea_elig_coun_bilt_net = r165_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR165_cea_aft_net() {
		return r165_cea_aft_net;
	}
	public void setR165_cea_aft_net(BigDecimal r165_cea_aft_net) {
		this.r165_cea_aft_net = r165_cea_aft_net;
	}
	public BigDecimal getR165_crm_sub_app_cea_elig() {
		return r165_crm_sub_app_cea_elig;
	}
	public void setR165_crm_sub_app_cea_elig(BigDecimal r165_crm_sub_app_cea_elig) {
		this.r165_crm_sub_app_cea_elig = r165_crm_sub_app_cea_elig;
	}
	public BigDecimal getR165_crm_sub_app_non_col_guar_elig() {
		return r165_crm_sub_app_non_col_guar_elig;
	}
	public void setR165_crm_sub_app_non_col_guar_elig(BigDecimal r165_crm_sub_app_non_col_guar_elig) {
		this.r165_crm_sub_app_non_col_guar_elig = r165_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR165_crm_sub_app_non_col_cre_der() {
		return r165_crm_sub_app_non_col_cre_der;
	}
	public void setR165_crm_sub_app_non_col_cre_der(BigDecimal r165_crm_sub_app_non_col_cre_der) {
		this.r165_crm_sub_app_non_col_cre_der = r165_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR165_crm_sub_app_col_elig_cash() {
		return r165_crm_sub_app_col_elig_cash;
	}
	public void setR165_crm_sub_app_col_elig_cash(BigDecimal r165_crm_sub_app_col_elig_cash) {
		this.r165_crm_sub_app_col_elig_cash = r165_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR165_crm_sub_app_col_elig_tre_bills() {
		return r165_crm_sub_app_col_elig_tre_bills;
	}
	public void setR165_crm_sub_app_col_elig_tre_bills(BigDecimal r165_crm_sub_app_col_elig_tre_bills) {
		this.r165_crm_sub_app_col_elig_tre_bills = r165_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR165_crm_sub_app_col_elig_deb_sec() {
		return r165_crm_sub_app_col_elig_deb_sec;
	}
	public void setR165_crm_sub_app_col_elig_deb_sec(BigDecimal r165_crm_sub_app_col_elig_deb_sec) {
		this.r165_crm_sub_app_col_elig_deb_sec = r165_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR165_crm_sub_app_col_elig_euiq() {
		return r165_crm_sub_app_col_elig_euiq;
	}
	public void setR165_crm_sub_app_col_elig_euiq(BigDecimal r165_crm_sub_app_col_elig_euiq) {
		this.r165_crm_sub_app_col_elig_euiq = r165_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR165_crm_sub_app_col_elig_uni_tru() {
		return r165_crm_sub_app_col_elig_uni_tru;
	}
	public void setR165_crm_sub_app_col_elig_uni_tru(BigDecimal r165_crm_sub_app_col_elig_uni_tru) {
		this.r165_crm_sub_app_col_elig_uni_tru = r165_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR165_crm_sub_app_col_cea_cov() {
		return r165_crm_sub_app_col_cea_cov;
	}
	public void setR165_crm_sub_app_col_cea_cov(BigDecimal r165_crm_sub_app_col_cea_cov) {
		this.r165_crm_sub_app_col_cea_cov = r165_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR165_crm_sub_app_col_cea_not_cov() {
		return r165_crm_sub_app_col_cea_not_cov;
	}
	public void setR165_crm_sub_app_col_cea_not_cov(BigDecimal r165_crm_sub_app_col_cea_not_cov) {
		this.r165_crm_sub_app_col_cea_not_cov = r165_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR165_crm_sub_app_rwa_ris_wei_crm() {
		return r165_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR165_crm_sub_app_rwa_ris_wei_crm(BigDecimal r165_crm_sub_app_rwa_ris_wei_crm) {
		this.r165_crm_sub_app_rwa_ris_wei_crm = r165_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR165_crm_sub_app_rwa_ris_cea_cov() {
		return r165_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR165_crm_sub_app_rwa_ris_cea_cov(BigDecimal r165_crm_sub_app_rwa_ris_cea_cov) {
		this.r165_crm_sub_app_rwa_ris_cea_cov = r165_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR165_crm_sub_app_rwa_appl_org_coun() {
		return r165_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR165_crm_sub_app_rwa_appl_org_coun(BigDecimal r165_crm_sub_app_rwa_appl_org_coun) {
		this.r165_crm_sub_app_rwa_appl_org_coun = r165_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR165_crm_sub_app_rwa_ris_cea_not_cov() {
		return r165_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR165_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r165_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r165_crm_sub_app_rwa_ris_cea_not_cov = r165_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR165_crm_com_app_col_cea_elig_crm() {
		return r165_crm_com_app_col_cea_elig_crm;
	}
	public void setR165_crm_com_app_col_cea_elig_crm(BigDecimal r165_crm_com_app_col_cea_elig_crm) {
		this.r165_crm_com_app_col_cea_elig_crm = r165_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR165_crm_com_app_col_elig_cea_vol_adj() {
		return r165_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR165_crm_com_app_col_elig_cea_vol_adj(BigDecimal r165_crm_com_app_col_elig_cea_vol_adj) {
		this.r165_crm_com_app_col_elig_cea_vol_adj = r165_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR165_crm_com_app_col_elig_fin_hai() {
		return r165_crm_com_app_col_elig_fin_hai;
	}
	public void setR165_crm_com_app_col_elig_fin_hai(BigDecimal r165_crm_com_app_col_elig_fin_hai) {
		this.r165_crm_com_app_col_elig_fin_hai = r165_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR165_crm_com_app_col_cea_val_aft_crm() {
		return r165_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR165_crm_com_app_col_cea_val_aft_crm(BigDecimal r165_crm_com_app_col_cea_val_aft_crm) {
		this.r165_crm_com_app_col_cea_val_aft_crm = r165_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR165_rwa_elig_cea_not_cov() {
		return r165_rwa_elig_cea_not_cov;
	}
	public void setR165_rwa_elig_cea_not_cov(BigDecimal r165_rwa_elig_cea_not_cov) {
		this.r165_rwa_elig_cea_not_cov = r165_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR165_rwa_unsec_cea_sub_cre_ris() {
		return r165_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR165_rwa_unsec_cea_sub_cre_ris(BigDecimal r165_rwa_unsec_cea_sub_cre_ris) {
		this.r165_rwa_unsec_cea_sub_cre_ris = r165_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR165_rwa_unsec_cea() {
		return r165_rwa_unsec_cea;
	}
	public void setR165_rwa_unsec_cea(BigDecimal r165_rwa_unsec_cea) {
		this.r165_rwa_unsec_cea = r165_rwa_unsec_cea;
	}
	public BigDecimal getR165_rwa_tot_ris_wei_ass() {
		return r165_rwa_tot_ris_wei_ass;
	}
	public void setR165_rwa_tot_ris_wei_ass(BigDecimal r165_rwa_tot_ris_wei_ass) {
		this.r165_rwa_tot_ris_wei_ass = r165_rwa_tot_ris_wei_ass;
	}
	public String getR166_exposure_class_off_bal() {
		return r166_exposure_class_off_bal;
	}
	public void setR166_exposure_class_off_bal(String r166_exposure_class_off_bal) {
		this.r166_exposure_class_off_bal = r166_exposure_class_off_bal;
	}
	public BigDecimal getR166_nom_pri_amt() {
		return r166_nom_pri_amt;
	}
	public void setR166_nom_pri_amt(BigDecimal r166_nom_pri_amt) {
		this.r166_nom_pri_amt = r166_nom_pri_amt;
	}
	public BigDecimal getR166_ccf() {
		return r166_ccf;
	}
	public void setR166_ccf(BigDecimal r166_ccf) {
		this.r166_ccf = r166_ccf;
	}
	public BigDecimal getR166_cea() {
		return r166_cea;
	}
	public void setR166_cea(BigDecimal r166_cea) {
		this.r166_cea = r166_cea;
	}
	public BigDecimal getR166_cea_elig_coun_bilt_net() {
		return r166_cea_elig_coun_bilt_net;
	}
	public void setR166_cea_elig_coun_bilt_net(BigDecimal r166_cea_elig_coun_bilt_net) {
		this.r166_cea_elig_coun_bilt_net = r166_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR166_cea_aft_net() {
		return r166_cea_aft_net;
	}
	public void setR166_cea_aft_net(BigDecimal r166_cea_aft_net) {
		this.r166_cea_aft_net = r166_cea_aft_net;
	}
	public BigDecimal getR166_crm_sub_app_cea_elig() {
		return r166_crm_sub_app_cea_elig;
	}
	public void setR166_crm_sub_app_cea_elig(BigDecimal r166_crm_sub_app_cea_elig) {
		this.r166_crm_sub_app_cea_elig = r166_crm_sub_app_cea_elig;
	}
	public BigDecimal getR166_crm_sub_app_non_col_guar_elig() {
		return r166_crm_sub_app_non_col_guar_elig;
	}
	public void setR166_crm_sub_app_non_col_guar_elig(BigDecimal r166_crm_sub_app_non_col_guar_elig) {
		this.r166_crm_sub_app_non_col_guar_elig = r166_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR166_crm_sub_app_non_col_cre_der() {
		return r166_crm_sub_app_non_col_cre_der;
	}
	public void setR166_crm_sub_app_non_col_cre_der(BigDecimal r166_crm_sub_app_non_col_cre_der) {
		this.r166_crm_sub_app_non_col_cre_der = r166_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR166_crm_sub_app_col_elig_cash() {
		return r166_crm_sub_app_col_elig_cash;
	}
	public void setR166_crm_sub_app_col_elig_cash(BigDecimal r166_crm_sub_app_col_elig_cash) {
		this.r166_crm_sub_app_col_elig_cash = r166_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR166_crm_sub_app_col_elig_tre_bills() {
		return r166_crm_sub_app_col_elig_tre_bills;
	}
	public void setR166_crm_sub_app_col_elig_tre_bills(BigDecimal r166_crm_sub_app_col_elig_tre_bills) {
		this.r166_crm_sub_app_col_elig_tre_bills = r166_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR166_crm_sub_app_col_elig_deb_sec() {
		return r166_crm_sub_app_col_elig_deb_sec;
	}
	public void setR166_crm_sub_app_col_elig_deb_sec(BigDecimal r166_crm_sub_app_col_elig_deb_sec) {
		this.r166_crm_sub_app_col_elig_deb_sec = r166_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR166_crm_sub_app_col_elig_euiq() {
		return r166_crm_sub_app_col_elig_euiq;
	}
	public void setR166_crm_sub_app_col_elig_euiq(BigDecimal r166_crm_sub_app_col_elig_euiq) {
		this.r166_crm_sub_app_col_elig_euiq = r166_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR166_crm_sub_app_col_elig_uni_tru() {
		return r166_crm_sub_app_col_elig_uni_tru;
	}
	public void setR166_crm_sub_app_col_elig_uni_tru(BigDecimal r166_crm_sub_app_col_elig_uni_tru) {
		this.r166_crm_sub_app_col_elig_uni_tru = r166_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR166_crm_sub_app_col_cea_cov() {
		return r166_crm_sub_app_col_cea_cov;
	}
	public void setR166_crm_sub_app_col_cea_cov(BigDecimal r166_crm_sub_app_col_cea_cov) {
		this.r166_crm_sub_app_col_cea_cov = r166_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR166_crm_sub_app_col_cea_not_cov() {
		return r166_crm_sub_app_col_cea_not_cov;
	}
	public void setR166_crm_sub_app_col_cea_not_cov(BigDecimal r166_crm_sub_app_col_cea_not_cov) {
		this.r166_crm_sub_app_col_cea_not_cov = r166_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR166_crm_sub_app_rwa_ris_wei_crm() {
		return r166_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR166_crm_sub_app_rwa_ris_wei_crm(BigDecimal r166_crm_sub_app_rwa_ris_wei_crm) {
		this.r166_crm_sub_app_rwa_ris_wei_crm = r166_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR166_crm_sub_app_rwa_ris_cea_cov() {
		return r166_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR166_crm_sub_app_rwa_ris_cea_cov(BigDecimal r166_crm_sub_app_rwa_ris_cea_cov) {
		this.r166_crm_sub_app_rwa_ris_cea_cov = r166_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR166_crm_sub_app_rwa_appl_org_coun() {
		return r166_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR166_crm_sub_app_rwa_appl_org_coun(BigDecimal r166_crm_sub_app_rwa_appl_org_coun) {
		this.r166_crm_sub_app_rwa_appl_org_coun = r166_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR166_crm_sub_app_rwa_ris_cea_not_cov() {
		return r166_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR166_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r166_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r166_crm_sub_app_rwa_ris_cea_not_cov = r166_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR166_crm_com_app_col_cea_elig_crm() {
		return r166_crm_com_app_col_cea_elig_crm;
	}
	public void setR166_crm_com_app_col_cea_elig_crm(BigDecimal r166_crm_com_app_col_cea_elig_crm) {
		this.r166_crm_com_app_col_cea_elig_crm = r166_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR166_crm_com_app_col_elig_cea_vol_adj() {
		return r166_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR166_crm_com_app_col_elig_cea_vol_adj(BigDecimal r166_crm_com_app_col_elig_cea_vol_adj) {
		this.r166_crm_com_app_col_elig_cea_vol_adj = r166_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR166_crm_com_app_col_elig_fin_hai() {
		return r166_crm_com_app_col_elig_fin_hai;
	}
	public void setR166_crm_com_app_col_elig_fin_hai(BigDecimal r166_crm_com_app_col_elig_fin_hai) {
		this.r166_crm_com_app_col_elig_fin_hai = r166_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR166_crm_com_app_col_cea_val_aft_crm() {
		return r166_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR166_crm_com_app_col_cea_val_aft_crm(BigDecimal r166_crm_com_app_col_cea_val_aft_crm) {
		this.r166_crm_com_app_col_cea_val_aft_crm = r166_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR166_rwa_elig_cea_not_cov() {
		return r166_rwa_elig_cea_not_cov;
	}
	public void setR166_rwa_elig_cea_not_cov(BigDecimal r166_rwa_elig_cea_not_cov) {
		this.r166_rwa_elig_cea_not_cov = r166_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR166_rwa_unsec_cea_sub_cre_ris() {
		return r166_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR166_rwa_unsec_cea_sub_cre_ris(BigDecimal r166_rwa_unsec_cea_sub_cre_ris) {
		this.r166_rwa_unsec_cea_sub_cre_ris = r166_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR166_rwa_unsec_cea() {
		return r166_rwa_unsec_cea;
	}
	public void setR166_rwa_unsec_cea(BigDecimal r166_rwa_unsec_cea) {
		this.r166_rwa_unsec_cea = r166_rwa_unsec_cea;
	}
	public BigDecimal getR166_rwa_tot_ris_wei_ass() {
		return r166_rwa_tot_ris_wei_ass;
	}
	public void setR166_rwa_tot_ris_wei_ass(BigDecimal r166_rwa_tot_ris_wei_ass) {
		this.r166_rwa_tot_ris_wei_ass = r166_rwa_tot_ris_wei_ass;
	}
	public String getR167_exposure_class_off_bal() {
		return r167_exposure_class_off_bal;
	}
	public void setR167_exposure_class_off_bal(String r167_exposure_class_off_bal) {
		this.r167_exposure_class_off_bal = r167_exposure_class_off_bal;
	}
	public BigDecimal getR167_nom_pri_amt() {
		return r167_nom_pri_amt;
	}
	public void setR167_nom_pri_amt(BigDecimal r167_nom_pri_amt) {
		this.r167_nom_pri_amt = r167_nom_pri_amt;
	}
	public BigDecimal getR167_ccf() {
		return r167_ccf;
	}
	public void setR167_ccf(BigDecimal r167_ccf) {
		this.r167_ccf = r167_ccf;
	}
	public BigDecimal getR167_cea() {
		return r167_cea;
	}
	public void setR167_cea(BigDecimal r167_cea) {
		this.r167_cea = r167_cea;
	}
	public BigDecimal getR167_cea_elig_coun_bilt_net() {
		return r167_cea_elig_coun_bilt_net;
	}
	public void setR167_cea_elig_coun_bilt_net(BigDecimal r167_cea_elig_coun_bilt_net) {
		this.r167_cea_elig_coun_bilt_net = r167_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR167_cea_aft_net() {
		return r167_cea_aft_net;
	}
	public void setR167_cea_aft_net(BigDecimal r167_cea_aft_net) {
		this.r167_cea_aft_net = r167_cea_aft_net;
	}
	public BigDecimal getR167_crm_sub_app_cea_elig() {
		return r167_crm_sub_app_cea_elig;
	}
	public void setR167_crm_sub_app_cea_elig(BigDecimal r167_crm_sub_app_cea_elig) {
		this.r167_crm_sub_app_cea_elig = r167_crm_sub_app_cea_elig;
	}
	public BigDecimal getR167_crm_sub_app_non_col_guar_elig() {
		return r167_crm_sub_app_non_col_guar_elig;
	}
	public void setR167_crm_sub_app_non_col_guar_elig(BigDecimal r167_crm_sub_app_non_col_guar_elig) {
		this.r167_crm_sub_app_non_col_guar_elig = r167_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR167_crm_sub_app_non_col_cre_der() {
		return r167_crm_sub_app_non_col_cre_der;
	}
	public void setR167_crm_sub_app_non_col_cre_der(BigDecimal r167_crm_sub_app_non_col_cre_der) {
		this.r167_crm_sub_app_non_col_cre_der = r167_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR167_crm_sub_app_col_elig_cash() {
		return r167_crm_sub_app_col_elig_cash;
	}
	public void setR167_crm_sub_app_col_elig_cash(BigDecimal r167_crm_sub_app_col_elig_cash) {
		this.r167_crm_sub_app_col_elig_cash = r167_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR167_crm_sub_app_col_elig_tre_bills() {
		return r167_crm_sub_app_col_elig_tre_bills;
	}
	public void setR167_crm_sub_app_col_elig_tre_bills(BigDecimal r167_crm_sub_app_col_elig_tre_bills) {
		this.r167_crm_sub_app_col_elig_tre_bills = r167_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR167_crm_sub_app_col_elig_deb_sec() {
		return r167_crm_sub_app_col_elig_deb_sec;
	}
	public void setR167_crm_sub_app_col_elig_deb_sec(BigDecimal r167_crm_sub_app_col_elig_deb_sec) {
		this.r167_crm_sub_app_col_elig_deb_sec = r167_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR167_crm_sub_app_col_elig_euiq() {
		return r167_crm_sub_app_col_elig_euiq;
	}
	public void setR167_crm_sub_app_col_elig_euiq(BigDecimal r167_crm_sub_app_col_elig_euiq) {
		this.r167_crm_sub_app_col_elig_euiq = r167_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR167_crm_sub_app_col_elig_uni_tru() {
		return r167_crm_sub_app_col_elig_uni_tru;
	}
	public void setR167_crm_sub_app_col_elig_uni_tru(BigDecimal r167_crm_sub_app_col_elig_uni_tru) {
		this.r167_crm_sub_app_col_elig_uni_tru = r167_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR167_crm_sub_app_col_cea_cov() {
		return r167_crm_sub_app_col_cea_cov;
	}
	public void setR167_crm_sub_app_col_cea_cov(BigDecimal r167_crm_sub_app_col_cea_cov) {
		this.r167_crm_sub_app_col_cea_cov = r167_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR167_crm_sub_app_col_cea_not_cov() {
		return r167_crm_sub_app_col_cea_not_cov;
	}
	public void setR167_crm_sub_app_col_cea_not_cov(BigDecimal r167_crm_sub_app_col_cea_not_cov) {
		this.r167_crm_sub_app_col_cea_not_cov = r167_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR167_crm_sub_app_rwa_ris_wei_crm() {
		return r167_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR167_crm_sub_app_rwa_ris_wei_crm(BigDecimal r167_crm_sub_app_rwa_ris_wei_crm) {
		this.r167_crm_sub_app_rwa_ris_wei_crm = r167_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR167_crm_sub_app_rwa_ris_cea_cov() {
		return r167_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR167_crm_sub_app_rwa_ris_cea_cov(BigDecimal r167_crm_sub_app_rwa_ris_cea_cov) {
		this.r167_crm_sub_app_rwa_ris_cea_cov = r167_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR167_crm_sub_app_rwa_appl_org_coun() {
		return r167_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR167_crm_sub_app_rwa_appl_org_coun(BigDecimal r167_crm_sub_app_rwa_appl_org_coun) {
		this.r167_crm_sub_app_rwa_appl_org_coun = r167_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR167_crm_sub_app_rwa_ris_cea_not_cov() {
		return r167_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR167_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r167_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r167_crm_sub_app_rwa_ris_cea_not_cov = r167_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR167_crm_com_app_col_cea_elig_crm() {
		return r167_crm_com_app_col_cea_elig_crm;
	}
	public void setR167_crm_com_app_col_cea_elig_crm(BigDecimal r167_crm_com_app_col_cea_elig_crm) {
		this.r167_crm_com_app_col_cea_elig_crm = r167_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR167_crm_com_app_col_elig_cea_vol_adj() {
		return r167_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR167_crm_com_app_col_elig_cea_vol_adj(BigDecimal r167_crm_com_app_col_elig_cea_vol_adj) {
		this.r167_crm_com_app_col_elig_cea_vol_adj = r167_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR167_crm_com_app_col_elig_fin_hai() {
		return r167_crm_com_app_col_elig_fin_hai;
	}
	public void setR167_crm_com_app_col_elig_fin_hai(BigDecimal r167_crm_com_app_col_elig_fin_hai) {
		this.r167_crm_com_app_col_elig_fin_hai = r167_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR167_crm_com_app_col_cea_val_aft_crm() {
		return r167_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR167_crm_com_app_col_cea_val_aft_crm(BigDecimal r167_crm_com_app_col_cea_val_aft_crm) {
		this.r167_crm_com_app_col_cea_val_aft_crm = r167_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR167_rwa_elig_cea_not_cov() {
		return r167_rwa_elig_cea_not_cov;
	}
	public void setR167_rwa_elig_cea_not_cov(BigDecimal r167_rwa_elig_cea_not_cov) {
		this.r167_rwa_elig_cea_not_cov = r167_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR167_rwa_unsec_cea_sub_cre_ris() {
		return r167_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR167_rwa_unsec_cea_sub_cre_ris(BigDecimal r167_rwa_unsec_cea_sub_cre_ris) {
		this.r167_rwa_unsec_cea_sub_cre_ris = r167_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR167_rwa_unsec_cea() {
		return r167_rwa_unsec_cea;
	}
	public void setR167_rwa_unsec_cea(BigDecimal r167_rwa_unsec_cea) {
		this.r167_rwa_unsec_cea = r167_rwa_unsec_cea;
	}
	public BigDecimal getR167_rwa_tot_ris_wei_ass() {
		return r167_rwa_tot_ris_wei_ass;
	}
	public void setR167_rwa_tot_ris_wei_ass(BigDecimal r167_rwa_tot_ris_wei_ass) {
		this.r167_rwa_tot_ris_wei_ass = r167_rwa_tot_ris_wei_ass;
	}
	public String getR168_exposure_class_off_bal() {
		return r168_exposure_class_off_bal;
	}
	public void setR168_exposure_class_off_bal(String r168_exposure_class_off_bal) {
		this.r168_exposure_class_off_bal = r168_exposure_class_off_bal;
	}
	public BigDecimal getR168_nom_pri_amt() {
		return r168_nom_pri_amt;
	}
	public void setR168_nom_pri_amt(BigDecimal r168_nom_pri_amt) {
		this.r168_nom_pri_amt = r168_nom_pri_amt;
	}
	public BigDecimal getR168_ccf() {
		return r168_ccf;
	}
	public void setR168_ccf(BigDecimal r168_ccf) {
		this.r168_ccf = r168_ccf;
	}
	public BigDecimal getR168_cea() {
		return r168_cea;
	}
	public void setR168_cea(BigDecimal r168_cea) {
		this.r168_cea = r168_cea;
	}
	public BigDecimal getR168_cea_elig_coun_bilt_net() {
		return r168_cea_elig_coun_bilt_net;
	}
	public void setR168_cea_elig_coun_bilt_net(BigDecimal r168_cea_elig_coun_bilt_net) {
		this.r168_cea_elig_coun_bilt_net = r168_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR168_cea_aft_net() {
		return r168_cea_aft_net;
	}
	public void setR168_cea_aft_net(BigDecimal r168_cea_aft_net) {
		this.r168_cea_aft_net = r168_cea_aft_net;
	}
	public BigDecimal getR168_crm_sub_app_cea_elig() {
		return r168_crm_sub_app_cea_elig;
	}
	public void setR168_crm_sub_app_cea_elig(BigDecimal r168_crm_sub_app_cea_elig) {
		this.r168_crm_sub_app_cea_elig = r168_crm_sub_app_cea_elig;
	}
	public BigDecimal getR168_crm_sub_app_non_col_guar_elig() {
		return r168_crm_sub_app_non_col_guar_elig;
	}
	public void setR168_crm_sub_app_non_col_guar_elig(BigDecimal r168_crm_sub_app_non_col_guar_elig) {
		this.r168_crm_sub_app_non_col_guar_elig = r168_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR168_crm_sub_app_non_col_cre_der() {
		return r168_crm_sub_app_non_col_cre_der;
	}
	public void setR168_crm_sub_app_non_col_cre_der(BigDecimal r168_crm_sub_app_non_col_cre_der) {
		this.r168_crm_sub_app_non_col_cre_der = r168_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR168_crm_sub_app_col_elig_cash() {
		return r168_crm_sub_app_col_elig_cash;
	}
	public void setR168_crm_sub_app_col_elig_cash(BigDecimal r168_crm_sub_app_col_elig_cash) {
		this.r168_crm_sub_app_col_elig_cash = r168_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR168_crm_sub_app_col_elig_tre_bills() {
		return r168_crm_sub_app_col_elig_tre_bills;
	}
	public void setR168_crm_sub_app_col_elig_tre_bills(BigDecimal r168_crm_sub_app_col_elig_tre_bills) {
		this.r168_crm_sub_app_col_elig_tre_bills = r168_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR168_crm_sub_app_col_elig_deb_sec() {
		return r168_crm_sub_app_col_elig_deb_sec;
	}
	public void setR168_crm_sub_app_col_elig_deb_sec(BigDecimal r168_crm_sub_app_col_elig_deb_sec) {
		this.r168_crm_sub_app_col_elig_deb_sec = r168_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR168_crm_sub_app_col_elig_euiq() {
		return r168_crm_sub_app_col_elig_euiq;
	}
	public void setR168_crm_sub_app_col_elig_euiq(BigDecimal r168_crm_sub_app_col_elig_euiq) {
		this.r168_crm_sub_app_col_elig_euiq = r168_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR168_crm_sub_app_col_elig_uni_tru() {
		return r168_crm_sub_app_col_elig_uni_tru;
	}
	public void setR168_crm_sub_app_col_elig_uni_tru(BigDecimal r168_crm_sub_app_col_elig_uni_tru) {
		this.r168_crm_sub_app_col_elig_uni_tru = r168_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR168_crm_sub_app_col_cea_cov() {
		return r168_crm_sub_app_col_cea_cov;
	}
	public void setR168_crm_sub_app_col_cea_cov(BigDecimal r168_crm_sub_app_col_cea_cov) {
		this.r168_crm_sub_app_col_cea_cov = r168_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR168_crm_sub_app_col_cea_not_cov() {
		return r168_crm_sub_app_col_cea_not_cov;
	}
	public void setR168_crm_sub_app_col_cea_not_cov(BigDecimal r168_crm_sub_app_col_cea_not_cov) {
		this.r168_crm_sub_app_col_cea_not_cov = r168_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR168_crm_sub_app_rwa_ris_wei_crm() {
		return r168_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR168_crm_sub_app_rwa_ris_wei_crm(BigDecimal r168_crm_sub_app_rwa_ris_wei_crm) {
		this.r168_crm_sub_app_rwa_ris_wei_crm = r168_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR168_crm_sub_app_rwa_ris_cea_cov() {
		return r168_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR168_crm_sub_app_rwa_ris_cea_cov(BigDecimal r168_crm_sub_app_rwa_ris_cea_cov) {
		this.r168_crm_sub_app_rwa_ris_cea_cov = r168_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR168_crm_sub_app_rwa_appl_org_coun() {
		return r168_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR168_crm_sub_app_rwa_appl_org_coun(BigDecimal r168_crm_sub_app_rwa_appl_org_coun) {
		this.r168_crm_sub_app_rwa_appl_org_coun = r168_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR168_crm_sub_app_rwa_ris_cea_not_cov() {
		return r168_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR168_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r168_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r168_crm_sub_app_rwa_ris_cea_not_cov = r168_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR168_crm_com_app_col_cea_elig_crm() {
		return r168_crm_com_app_col_cea_elig_crm;
	}
	public void setR168_crm_com_app_col_cea_elig_crm(BigDecimal r168_crm_com_app_col_cea_elig_crm) {
		this.r168_crm_com_app_col_cea_elig_crm = r168_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR168_crm_com_app_col_elig_cea_vol_adj() {
		return r168_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR168_crm_com_app_col_elig_cea_vol_adj(BigDecimal r168_crm_com_app_col_elig_cea_vol_adj) {
		this.r168_crm_com_app_col_elig_cea_vol_adj = r168_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR168_crm_com_app_col_elig_fin_hai() {
		return r168_crm_com_app_col_elig_fin_hai;
	}
	public void setR168_crm_com_app_col_elig_fin_hai(BigDecimal r168_crm_com_app_col_elig_fin_hai) {
		this.r168_crm_com_app_col_elig_fin_hai = r168_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR168_crm_com_app_col_cea_val_aft_crm() {
		return r168_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR168_crm_com_app_col_cea_val_aft_crm(BigDecimal r168_crm_com_app_col_cea_val_aft_crm) {
		this.r168_crm_com_app_col_cea_val_aft_crm = r168_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR168_rwa_elig_cea_not_cov() {
		return r168_rwa_elig_cea_not_cov;
	}
	public void setR168_rwa_elig_cea_not_cov(BigDecimal r168_rwa_elig_cea_not_cov) {
		this.r168_rwa_elig_cea_not_cov = r168_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR168_rwa_unsec_cea_sub_cre_ris() {
		return r168_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR168_rwa_unsec_cea_sub_cre_ris(BigDecimal r168_rwa_unsec_cea_sub_cre_ris) {
		this.r168_rwa_unsec_cea_sub_cre_ris = r168_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR168_rwa_unsec_cea() {
		return r168_rwa_unsec_cea;
	}
	public void setR168_rwa_unsec_cea(BigDecimal r168_rwa_unsec_cea) {
		this.r168_rwa_unsec_cea = r168_rwa_unsec_cea;
	}
	public BigDecimal getR168_rwa_tot_ris_wei_ass() {
		return r168_rwa_tot_ris_wei_ass;
	}
	public void setR168_rwa_tot_ris_wei_ass(BigDecimal r168_rwa_tot_ris_wei_ass) {
		this.r168_rwa_tot_ris_wei_ass = r168_rwa_tot_ris_wei_ass;
	}
	public String getR169_exposure_class_off_bal() {
		return r169_exposure_class_off_bal;
	}
	public void setR169_exposure_class_off_bal(String r169_exposure_class_off_bal) {
		this.r169_exposure_class_off_bal = r169_exposure_class_off_bal;
	}
	public BigDecimal getR169_nom_pri_amt() {
		return r169_nom_pri_amt;
	}
	public void setR169_nom_pri_amt(BigDecimal r169_nom_pri_amt) {
		this.r169_nom_pri_amt = r169_nom_pri_amt;
	}
	public BigDecimal getR169_ccf() {
		return r169_ccf;
	}
	public void setR169_ccf(BigDecimal r169_ccf) {
		this.r169_ccf = r169_ccf;
	}
	public BigDecimal getR169_cea() {
		return r169_cea;
	}
	public void setR169_cea(BigDecimal r169_cea) {
		this.r169_cea = r169_cea;
	}
	public BigDecimal getR169_cea_elig_coun_bilt_net() {
		return r169_cea_elig_coun_bilt_net;
	}
	public void setR169_cea_elig_coun_bilt_net(BigDecimal r169_cea_elig_coun_bilt_net) {
		this.r169_cea_elig_coun_bilt_net = r169_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR169_cea_aft_net() {
		return r169_cea_aft_net;
	}
	public void setR169_cea_aft_net(BigDecimal r169_cea_aft_net) {
		this.r169_cea_aft_net = r169_cea_aft_net;
	}
	public BigDecimal getR169_crm_sub_app_cea_elig() {
		return r169_crm_sub_app_cea_elig;
	}
	public void setR169_crm_sub_app_cea_elig(BigDecimal r169_crm_sub_app_cea_elig) {
		this.r169_crm_sub_app_cea_elig = r169_crm_sub_app_cea_elig;
	}
	public BigDecimal getR169_crm_sub_app_non_col_guar_elig() {
		return r169_crm_sub_app_non_col_guar_elig;
	}
	public void setR169_crm_sub_app_non_col_guar_elig(BigDecimal r169_crm_sub_app_non_col_guar_elig) {
		this.r169_crm_sub_app_non_col_guar_elig = r169_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR169_crm_sub_app_non_col_cre_der() {
		return r169_crm_sub_app_non_col_cre_der;
	}
	public void setR169_crm_sub_app_non_col_cre_der(BigDecimal r169_crm_sub_app_non_col_cre_der) {
		this.r169_crm_sub_app_non_col_cre_der = r169_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR169_crm_sub_app_col_elig_cash() {
		return r169_crm_sub_app_col_elig_cash;
	}
	public void setR169_crm_sub_app_col_elig_cash(BigDecimal r169_crm_sub_app_col_elig_cash) {
		this.r169_crm_sub_app_col_elig_cash = r169_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR169_crm_sub_app_col_elig_tre_bills() {
		return r169_crm_sub_app_col_elig_tre_bills;
	}
	public void setR169_crm_sub_app_col_elig_tre_bills(BigDecimal r169_crm_sub_app_col_elig_tre_bills) {
		this.r169_crm_sub_app_col_elig_tre_bills = r169_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR169_crm_sub_app_col_elig_deb_sec() {
		return r169_crm_sub_app_col_elig_deb_sec;
	}
	public void setR169_crm_sub_app_col_elig_deb_sec(BigDecimal r169_crm_sub_app_col_elig_deb_sec) {
		this.r169_crm_sub_app_col_elig_deb_sec = r169_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR169_crm_sub_app_col_elig_euiq() {
		return r169_crm_sub_app_col_elig_euiq;
	}
	public void setR169_crm_sub_app_col_elig_euiq(BigDecimal r169_crm_sub_app_col_elig_euiq) {
		this.r169_crm_sub_app_col_elig_euiq = r169_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR169_crm_sub_app_col_elig_uni_tru() {
		return r169_crm_sub_app_col_elig_uni_tru;
	}
	public void setR169_crm_sub_app_col_elig_uni_tru(BigDecimal r169_crm_sub_app_col_elig_uni_tru) {
		this.r169_crm_sub_app_col_elig_uni_tru = r169_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR169_crm_sub_app_col_cea_cov() {
		return r169_crm_sub_app_col_cea_cov;
	}
	public void setR169_crm_sub_app_col_cea_cov(BigDecimal r169_crm_sub_app_col_cea_cov) {
		this.r169_crm_sub_app_col_cea_cov = r169_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR169_crm_sub_app_col_cea_not_cov() {
		return r169_crm_sub_app_col_cea_not_cov;
	}
	public void setR169_crm_sub_app_col_cea_not_cov(BigDecimal r169_crm_sub_app_col_cea_not_cov) {
		this.r169_crm_sub_app_col_cea_not_cov = r169_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR169_crm_sub_app_rwa_ris_wei_crm() {
		return r169_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR169_crm_sub_app_rwa_ris_wei_crm(BigDecimal r169_crm_sub_app_rwa_ris_wei_crm) {
		this.r169_crm_sub_app_rwa_ris_wei_crm = r169_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR169_crm_sub_app_rwa_ris_cea_cov() {
		return r169_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR169_crm_sub_app_rwa_ris_cea_cov(BigDecimal r169_crm_sub_app_rwa_ris_cea_cov) {
		this.r169_crm_sub_app_rwa_ris_cea_cov = r169_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR169_crm_sub_app_rwa_appl_org_coun() {
		return r169_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR169_crm_sub_app_rwa_appl_org_coun(BigDecimal r169_crm_sub_app_rwa_appl_org_coun) {
		this.r169_crm_sub_app_rwa_appl_org_coun = r169_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR169_crm_sub_app_rwa_ris_cea_not_cov() {
		return r169_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR169_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r169_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r169_crm_sub_app_rwa_ris_cea_not_cov = r169_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR169_crm_com_app_col_cea_elig_crm() {
		return r169_crm_com_app_col_cea_elig_crm;
	}
	public void setR169_crm_com_app_col_cea_elig_crm(BigDecimal r169_crm_com_app_col_cea_elig_crm) {
		this.r169_crm_com_app_col_cea_elig_crm = r169_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR169_crm_com_app_col_elig_cea_vol_adj() {
		return r169_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR169_crm_com_app_col_elig_cea_vol_adj(BigDecimal r169_crm_com_app_col_elig_cea_vol_adj) {
		this.r169_crm_com_app_col_elig_cea_vol_adj = r169_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR169_crm_com_app_col_elig_fin_hai() {
		return r169_crm_com_app_col_elig_fin_hai;
	}
	public void setR169_crm_com_app_col_elig_fin_hai(BigDecimal r169_crm_com_app_col_elig_fin_hai) {
		this.r169_crm_com_app_col_elig_fin_hai = r169_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR169_crm_com_app_col_cea_val_aft_crm() {
		return r169_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR169_crm_com_app_col_cea_val_aft_crm(BigDecimal r169_crm_com_app_col_cea_val_aft_crm) {
		this.r169_crm_com_app_col_cea_val_aft_crm = r169_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR169_rwa_elig_cea_not_cov() {
		return r169_rwa_elig_cea_not_cov;
	}
	public void setR169_rwa_elig_cea_not_cov(BigDecimal r169_rwa_elig_cea_not_cov) {
		this.r169_rwa_elig_cea_not_cov = r169_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR169_rwa_unsec_cea_sub_cre_ris() {
		return r169_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR169_rwa_unsec_cea_sub_cre_ris(BigDecimal r169_rwa_unsec_cea_sub_cre_ris) {
		this.r169_rwa_unsec_cea_sub_cre_ris = r169_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR169_rwa_unsec_cea() {
		return r169_rwa_unsec_cea;
	}
	public void setR169_rwa_unsec_cea(BigDecimal r169_rwa_unsec_cea) {
		this.r169_rwa_unsec_cea = r169_rwa_unsec_cea;
	}
	public BigDecimal getR169_rwa_tot_ris_wei_ass() {
		return r169_rwa_tot_ris_wei_ass;
	}
	public void setR169_rwa_tot_ris_wei_ass(BigDecimal r169_rwa_tot_ris_wei_ass) {
		this.r169_rwa_tot_ris_wei_ass = r169_rwa_tot_ris_wei_ass;
	}
	public String getR170_exposure_class_off_bal() {
		return r170_exposure_class_off_bal;
	}
	public void setR170_exposure_class_off_bal(String r170_exposure_class_off_bal) {
		this.r170_exposure_class_off_bal = r170_exposure_class_off_bal;
	}
	public BigDecimal getR170_nom_pri_amt() {
		return r170_nom_pri_amt;
	}
	public void setR170_nom_pri_amt(BigDecimal r170_nom_pri_amt) {
		this.r170_nom_pri_amt = r170_nom_pri_amt;
	}
	public BigDecimal getR170_ccf() {
		return r170_ccf;
	}
	public void setR170_ccf(BigDecimal r170_ccf) {
		this.r170_ccf = r170_ccf;
	}
	public BigDecimal getR170_cea() {
		return r170_cea;
	}
	public void setR170_cea(BigDecimal r170_cea) {
		this.r170_cea = r170_cea;
	}
	public BigDecimal getR170_cea_elig_coun_bilt_net() {
		return r170_cea_elig_coun_bilt_net;
	}
	public void setR170_cea_elig_coun_bilt_net(BigDecimal r170_cea_elig_coun_bilt_net) {
		this.r170_cea_elig_coun_bilt_net = r170_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR170_cea_aft_net() {
		return r170_cea_aft_net;
	}
	public void setR170_cea_aft_net(BigDecimal r170_cea_aft_net) {
		this.r170_cea_aft_net = r170_cea_aft_net;
	}
	public BigDecimal getR170_crm_sub_app_cea_elig() {
		return r170_crm_sub_app_cea_elig;
	}
	public void setR170_crm_sub_app_cea_elig(BigDecimal r170_crm_sub_app_cea_elig) {
		this.r170_crm_sub_app_cea_elig = r170_crm_sub_app_cea_elig;
	}
	public BigDecimal getR170_crm_sub_app_non_col_guar_elig() {
		return r170_crm_sub_app_non_col_guar_elig;
	}
	public void setR170_crm_sub_app_non_col_guar_elig(BigDecimal r170_crm_sub_app_non_col_guar_elig) {
		this.r170_crm_sub_app_non_col_guar_elig = r170_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR170_crm_sub_app_non_col_cre_der() {
		return r170_crm_sub_app_non_col_cre_der;
	}
	public void setR170_crm_sub_app_non_col_cre_der(BigDecimal r170_crm_sub_app_non_col_cre_der) {
		this.r170_crm_sub_app_non_col_cre_der = r170_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR170_crm_sub_app_col_elig_cash() {
		return r170_crm_sub_app_col_elig_cash;
	}
	public void setR170_crm_sub_app_col_elig_cash(BigDecimal r170_crm_sub_app_col_elig_cash) {
		this.r170_crm_sub_app_col_elig_cash = r170_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR170_crm_sub_app_col_elig_tre_bills() {
		return r170_crm_sub_app_col_elig_tre_bills;
	}
	public void setR170_crm_sub_app_col_elig_tre_bills(BigDecimal r170_crm_sub_app_col_elig_tre_bills) {
		this.r170_crm_sub_app_col_elig_tre_bills = r170_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR170_crm_sub_app_col_elig_deb_sec() {
		return r170_crm_sub_app_col_elig_deb_sec;
	}
	public void setR170_crm_sub_app_col_elig_deb_sec(BigDecimal r170_crm_sub_app_col_elig_deb_sec) {
		this.r170_crm_sub_app_col_elig_deb_sec = r170_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR170_crm_sub_app_col_elig_euiq() {
		return r170_crm_sub_app_col_elig_euiq;
	}
	public void setR170_crm_sub_app_col_elig_euiq(BigDecimal r170_crm_sub_app_col_elig_euiq) {
		this.r170_crm_sub_app_col_elig_euiq = r170_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR170_crm_sub_app_col_elig_uni_tru() {
		return r170_crm_sub_app_col_elig_uni_tru;
	}
	public void setR170_crm_sub_app_col_elig_uni_tru(BigDecimal r170_crm_sub_app_col_elig_uni_tru) {
		this.r170_crm_sub_app_col_elig_uni_tru = r170_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR170_crm_sub_app_col_cea_cov() {
		return r170_crm_sub_app_col_cea_cov;
	}
	public void setR170_crm_sub_app_col_cea_cov(BigDecimal r170_crm_sub_app_col_cea_cov) {
		this.r170_crm_sub_app_col_cea_cov = r170_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR170_crm_sub_app_col_cea_not_cov() {
		return r170_crm_sub_app_col_cea_not_cov;
	}
	public void setR170_crm_sub_app_col_cea_not_cov(BigDecimal r170_crm_sub_app_col_cea_not_cov) {
		this.r170_crm_sub_app_col_cea_not_cov = r170_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR170_crm_sub_app_rwa_ris_wei_crm() {
		return r170_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR170_crm_sub_app_rwa_ris_wei_crm(BigDecimal r170_crm_sub_app_rwa_ris_wei_crm) {
		this.r170_crm_sub_app_rwa_ris_wei_crm = r170_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR170_crm_sub_app_rwa_ris_cea_cov() {
		return r170_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR170_crm_sub_app_rwa_ris_cea_cov(BigDecimal r170_crm_sub_app_rwa_ris_cea_cov) {
		this.r170_crm_sub_app_rwa_ris_cea_cov = r170_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR170_crm_sub_app_rwa_appl_org_coun() {
		return r170_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR170_crm_sub_app_rwa_appl_org_coun(BigDecimal r170_crm_sub_app_rwa_appl_org_coun) {
		this.r170_crm_sub_app_rwa_appl_org_coun = r170_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR170_crm_sub_app_rwa_ris_cea_not_cov() {
		return r170_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR170_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r170_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r170_crm_sub_app_rwa_ris_cea_not_cov = r170_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR170_crm_com_app_col_cea_elig_crm() {
		return r170_crm_com_app_col_cea_elig_crm;
	}
	public void setR170_crm_com_app_col_cea_elig_crm(BigDecimal r170_crm_com_app_col_cea_elig_crm) {
		this.r170_crm_com_app_col_cea_elig_crm = r170_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR170_crm_com_app_col_elig_cea_vol_adj() {
		return r170_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR170_crm_com_app_col_elig_cea_vol_adj(BigDecimal r170_crm_com_app_col_elig_cea_vol_adj) {
		this.r170_crm_com_app_col_elig_cea_vol_adj = r170_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR170_crm_com_app_col_elig_fin_hai() {
		return r170_crm_com_app_col_elig_fin_hai;
	}
	public void setR170_crm_com_app_col_elig_fin_hai(BigDecimal r170_crm_com_app_col_elig_fin_hai) {
		this.r170_crm_com_app_col_elig_fin_hai = r170_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR170_crm_com_app_col_cea_val_aft_crm() {
		return r170_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR170_crm_com_app_col_cea_val_aft_crm(BigDecimal r170_crm_com_app_col_cea_val_aft_crm) {
		this.r170_crm_com_app_col_cea_val_aft_crm = r170_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR170_rwa_elig_cea_not_cov() {
		return r170_rwa_elig_cea_not_cov;
	}
	public void setR170_rwa_elig_cea_not_cov(BigDecimal r170_rwa_elig_cea_not_cov) {
		this.r170_rwa_elig_cea_not_cov = r170_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR170_rwa_unsec_cea_sub_cre_ris() {
		return r170_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR170_rwa_unsec_cea_sub_cre_ris(BigDecimal r170_rwa_unsec_cea_sub_cre_ris) {
		this.r170_rwa_unsec_cea_sub_cre_ris = r170_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR170_rwa_unsec_cea() {
		return r170_rwa_unsec_cea;
	}
	public void setR170_rwa_unsec_cea(BigDecimal r170_rwa_unsec_cea) {
		this.r170_rwa_unsec_cea = r170_rwa_unsec_cea;
	}
	public BigDecimal getR170_rwa_tot_ris_wei_ass() {
		return r170_rwa_tot_ris_wei_ass;
	}
	public void setR170_rwa_tot_ris_wei_ass(BigDecimal r170_rwa_tot_ris_wei_ass) {
		this.r170_rwa_tot_ris_wei_ass = r170_rwa_tot_ris_wei_ass;
	}
	public String getR171_exposure_class_off_bal() {
		return r171_exposure_class_off_bal;
	}
	public void setR171_exposure_class_off_bal(String r171_exposure_class_off_bal) {
		this.r171_exposure_class_off_bal = r171_exposure_class_off_bal;
	}
	public BigDecimal getR171_nom_pri_amt() {
		return r171_nom_pri_amt;
	}
	public void setR171_nom_pri_amt(BigDecimal r171_nom_pri_amt) {
		this.r171_nom_pri_amt = r171_nom_pri_amt;
	}
	public BigDecimal getR171_ccf() {
		return r171_ccf;
	}
	public void setR171_ccf(BigDecimal r171_ccf) {
		this.r171_ccf = r171_ccf;
	}
	public BigDecimal getR171_cea() {
		return r171_cea;
	}
	public void setR171_cea(BigDecimal r171_cea) {
		this.r171_cea = r171_cea;
	}
	public BigDecimal getR171_cea_elig_coun_bilt_net() {
		return r171_cea_elig_coun_bilt_net;
	}
	public void setR171_cea_elig_coun_bilt_net(BigDecimal r171_cea_elig_coun_bilt_net) {
		this.r171_cea_elig_coun_bilt_net = r171_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR171_cea_aft_net() {
		return r171_cea_aft_net;
	}
	public void setR171_cea_aft_net(BigDecimal r171_cea_aft_net) {
		this.r171_cea_aft_net = r171_cea_aft_net;
	}
	public BigDecimal getR171_crm_sub_app_cea_elig() {
		return r171_crm_sub_app_cea_elig;
	}
	public void setR171_crm_sub_app_cea_elig(BigDecimal r171_crm_sub_app_cea_elig) {
		this.r171_crm_sub_app_cea_elig = r171_crm_sub_app_cea_elig;
	}
	public BigDecimal getR171_crm_sub_app_non_col_guar_elig() {
		return r171_crm_sub_app_non_col_guar_elig;
	}
	public void setR171_crm_sub_app_non_col_guar_elig(BigDecimal r171_crm_sub_app_non_col_guar_elig) {
		this.r171_crm_sub_app_non_col_guar_elig = r171_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR171_crm_sub_app_non_col_cre_der() {
		return r171_crm_sub_app_non_col_cre_der;
	}
	public void setR171_crm_sub_app_non_col_cre_der(BigDecimal r171_crm_sub_app_non_col_cre_der) {
		this.r171_crm_sub_app_non_col_cre_der = r171_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR171_crm_sub_app_col_elig_cash() {
		return r171_crm_sub_app_col_elig_cash;
	}
	public void setR171_crm_sub_app_col_elig_cash(BigDecimal r171_crm_sub_app_col_elig_cash) {
		this.r171_crm_sub_app_col_elig_cash = r171_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR171_crm_sub_app_col_elig_tre_bills() {
		return r171_crm_sub_app_col_elig_tre_bills;
	}
	public void setR171_crm_sub_app_col_elig_tre_bills(BigDecimal r171_crm_sub_app_col_elig_tre_bills) {
		this.r171_crm_sub_app_col_elig_tre_bills = r171_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR171_crm_sub_app_col_elig_deb_sec() {
		return r171_crm_sub_app_col_elig_deb_sec;
	}
	public void setR171_crm_sub_app_col_elig_deb_sec(BigDecimal r171_crm_sub_app_col_elig_deb_sec) {
		this.r171_crm_sub_app_col_elig_deb_sec = r171_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR171_crm_sub_app_col_elig_euiq() {
		return r171_crm_sub_app_col_elig_euiq;
	}
	public void setR171_crm_sub_app_col_elig_euiq(BigDecimal r171_crm_sub_app_col_elig_euiq) {
		this.r171_crm_sub_app_col_elig_euiq = r171_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR171_crm_sub_app_col_elig_uni_tru() {
		return r171_crm_sub_app_col_elig_uni_tru;
	}
	public void setR171_crm_sub_app_col_elig_uni_tru(BigDecimal r171_crm_sub_app_col_elig_uni_tru) {
		this.r171_crm_sub_app_col_elig_uni_tru = r171_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR171_crm_sub_app_col_cea_cov() {
		return r171_crm_sub_app_col_cea_cov;
	}
	public void setR171_crm_sub_app_col_cea_cov(BigDecimal r171_crm_sub_app_col_cea_cov) {
		this.r171_crm_sub_app_col_cea_cov = r171_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR171_crm_sub_app_col_cea_not_cov() {
		return r171_crm_sub_app_col_cea_not_cov;
	}
	public void setR171_crm_sub_app_col_cea_not_cov(BigDecimal r171_crm_sub_app_col_cea_not_cov) {
		this.r171_crm_sub_app_col_cea_not_cov = r171_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR171_crm_sub_app_rwa_ris_wei_crm() {
		return r171_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR171_crm_sub_app_rwa_ris_wei_crm(BigDecimal r171_crm_sub_app_rwa_ris_wei_crm) {
		this.r171_crm_sub_app_rwa_ris_wei_crm = r171_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR171_crm_sub_app_rwa_ris_cea_cov() {
		return r171_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR171_crm_sub_app_rwa_ris_cea_cov(BigDecimal r171_crm_sub_app_rwa_ris_cea_cov) {
		this.r171_crm_sub_app_rwa_ris_cea_cov = r171_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR171_crm_sub_app_rwa_appl_org_coun() {
		return r171_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR171_crm_sub_app_rwa_appl_org_coun(BigDecimal r171_crm_sub_app_rwa_appl_org_coun) {
		this.r171_crm_sub_app_rwa_appl_org_coun = r171_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR171_crm_sub_app_rwa_ris_cea_not_cov() {
		return r171_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR171_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r171_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r171_crm_sub_app_rwa_ris_cea_not_cov = r171_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR171_crm_com_app_col_cea_elig_crm() {
		return r171_crm_com_app_col_cea_elig_crm;
	}
	public void setR171_crm_com_app_col_cea_elig_crm(BigDecimal r171_crm_com_app_col_cea_elig_crm) {
		this.r171_crm_com_app_col_cea_elig_crm = r171_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR171_crm_com_app_col_elig_cea_vol_adj() {
		return r171_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR171_crm_com_app_col_elig_cea_vol_adj(BigDecimal r171_crm_com_app_col_elig_cea_vol_adj) {
		this.r171_crm_com_app_col_elig_cea_vol_adj = r171_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR171_crm_com_app_col_elig_fin_hai() {
		return r171_crm_com_app_col_elig_fin_hai;
	}
	public void setR171_crm_com_app_col_elig_fin_hai(BigDecimal r171_crm_com_app_col_elig_fin_hai) {
		this.r171_crm_com_app_col_elig_fin_hai = r171_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR171_crm_com_app_col_cea_val_aft_crm() {
		return r171_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR171_crm_com_app_col_cea_val_aft_crm(BigDecimal r171_crm_com_app_col_cea_val_aft_crm) {
		this.r171_crm_com_app_col_cea_val_aft_crm = r171_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR171_rwa_elig_cea_not_cov() {
		return r171_rwa_elig_cea_not_cov;
	}
	public void setR171_rwa_elig_cea_not_cov(BigDecimal r171_rwa_elig_cea_not_cov) {
		this.r171_rwa_elig_cea_not_cov = r171_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR171_rwa_unsec_cea_sub_cre_ris() {
		return r171_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR171_rwa_unsec_cea_sub_cre_ris(BigDecimal r171_rwa_unsec_cea_sub_cre_ris) {
		this.r171_rwa_unsec_cea_sub_cre_ris = r171_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR171_rwa_unsec_cea() {
		return r171_rwa_unsec_cea;
	}
	public void setR171_rwa_unsec_cea(BigDecimal r171_rwa_unsec_cea) {
		this.r171_rwa_unsec_cea = r171_rwa_unsec_cea;
	}
	public BigDecimal getR171_rwa_tot_ris_wei_ass() {
		return r171_rwa_tot_ris_wei_ass;
	}
	public void setR171_rwa_tot_ris_wei_ass(BigDecimal r171_rwa_tot_ris_wei_ass) {
		this.r171_rwa_tot_ris_wei_ass = r171_rwa_tot_ris_wei_ass;
	}
	public String getR172_exposure_class_off_bal() {
		return r172_exposure_class_off_bal;
	}
	public void setR172_exposure_class_off_bal(String r172_exposure_class_off_bal) {
		this.r172_exposure_class_off_bal = r172_exposure_class_off_bal;
	}
	public BigDecimal getR172_nom_pri_amt() {
		return r172_nom_pri_amt;
	}
	public void setR172_nom_pri_amt(BigDecimal r172_nom_pri_amt) {
		this.r172_nom_pri_amt = r172_nom_pri_amt;
	}
	public BigDecimal getR172_ccf() {
		return r172_ccf;
	}
	public void setR172_ccf(BigDecimal r172_ccf) {
		this.r172_ccf = r172_ccf;
	}
	public BigDecimal getR172_cea() {
		return r172_cea;
	}
	public void setR172_cea(BigDecimal r172_cea) {
		this.r172_cea = r172_cea;
	}
	public BigDecimal getR172_cea_elig_coun_bilt_net() {
		return r172_cea_elig_coun_bilt_net;
	}
	public void setR172_cea_elig_coun_bilt_net(BigDecimal r172_cea_elig_coun_bilt_net) {
		this.r172_cea_elig_coun_bilt_net = r172_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR172_cea_aft_net() {
		return r172_cea_aft_net;
	}
	public void setR172_cea_aft_net(BigDecimal r172_cea_aft_net) {
		this.r172_cea_aft_net = r172_cea_aft_net;
	}
	public BigDecimal getR172_crm_sub_app_cea_elig() {
		return r172_crm_sub_app_cea_elig;
	}
	public void setR172_crm_sub_app_cea_elig(BigDecimal r172_crm_sub_app_cea_elig) {
		this.r172_crm_sub_app_cea_elig = r172_crm_sub_app_cea_elig;
	}
	public BigDecimal getR172_crm_sub_app_non_col_guar_elig() {
		return r172_crm_sub_app_non_col_guar_elig;
	}
	public void setR172_crm_sub_app_non_col_guar_elig(BigDecimal r172_crm_sub_app_non_col_guar_elig) {
		this.r172_crm_sub_app_non_col_guar_elig = r172_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR172_crm_sub_app_non_col_cre_der() {
		return r172_crm_sub_app_non_col_cre_der;
	}
	public void setR172_crm_sub_app_non_col_cre_der(BigDecimal r172_crm_sub_app_non_col_cre_der) {
		this.r172_crm_sub_app_non_col_cre_der = r172_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR172_crm_sub_app_col_elig_cash() {
		return r172_crm_sub_app_col_elig_cash;
	}
	public void setR172_crm_sub_app_col_elig_cash(BigDecimal r172_crm_sub_app_col_elig_cash) {
		this.r172_crm_sub_app_col_elig_cash = r172_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR172_crm_sub_app_col_elig_tre_bills() {
		return r172_crm_sub_app_col_elig_tre_bills;
	}
	public void setR172_crm_sub_app_col_elig_tre_bills(BigDecimal r172_crm_sub_app_col_elig_tre_bills) {
		this.r172_crm_sub_app_col_elig_tre_bills = r172_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR172_crm_sub_app_col_elig_deb_sec() {
		return r172_crm_sub_app_col_elig_deb_sec;
	}
	public void setR172_crm_sub_app_col_elig_deb_sec(BigDecimal r172_crm_sub_app_col_elig_deb_sec) {
		this.r172_crm_sub_app_col_elig_deb_sec = r172_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR172_crm_sub_app_col_elig_euiq() {
		return r172_crm_sub_app_col_elig_euiq;
	}
	public void setR172_crm_sub_app_col_elig_euiq(BigDecimal r172_crm_sub_app_col_elig_euiq) {
		this.r172_crm_sub_app_col_elig_euiq = r172_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR172_crm_sub_app_col_elig_uni_tru() {
		return r172_crm_sub_app_col_elig_uni_tru;
	}
	public void setR172_crm_sub_app_col_elig_uni_tru(BigDecimal r172_crm_sub_app_col_elig_uni_tru) {
		this.r172_crm_sub_app_col_elig_uni_tru = r172_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR172_crm_sub_app_col_cea_cov() {
		return r172_crm_sub_app_col_cea_cov;
	}
	public void setR172_crm_sub_app_col_cea_cov(BigDecimal r172_crm_sub_app_col_cea_cov) {
		this.r172_crm_sub_app_col_cea_cov = r172_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR172_crm_sub_app_col_cea_not_cov() {
		return r172_crm_sub_app_col_cea_not_cov;
	}
	public void setR172_crm_sub_app_col_cea_not_cov(BigDecimal r172_crm_sub_app_col_cea_not_cov) {
		this.r172_crm_sub_app_col_cea_not_cov = r172_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR172_crm_sub_app_rwa_ris_wei_crm() {
		return r172_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR172_crm_sub_app_rwa_ris_wei_crm(BigDecimal r172_crm_sub_app_rwa_ris_wei_crm) {
		this.r172_crm_sub_app_rwa_ris_wei_crm = r172_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR172_crm_sub_app_rwa_ris_cea_cov() {
		return r172_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR172_crm_sub_app_rwa_ris_cea_cov(BigDecimal r172_crm_sub_app_rwa_ris_cea_cov) {
		this.r172_crm_sub_app_rwa_ris_cea_cov = r172_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR172_crm_sub_app_rwa_appl_org_coun() {
		return r172_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR172_crm_sub_app_rwa_appl_org_coun(BigDecimal r172_crm_sub_app_rwa_appl_org_coun) {
		this.r172_crm_sub_app_rwa_appl_org_coun = r172_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR172_crm_sub_app_rwa_ris_cea_not_cov() {
		return r172_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR172_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r172_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r172_crm_sub_app_rwa_ris_cea_not_cov = r172_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR172_crm_com_app_col_cea_elig_crm() {
		return r172_crm_com_app_col_cea_elig_crm;
	}
	public void setR172_crm_com_app_col_cea_elig_crm(BigDecimal r172_crm_com_app_col_cea_elig_crm) {
		this.r172_crm_com_app_col_cea_elig_crm = r172_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR172_crm_com_app_col_elig_cea_vol_adj() {
		return r172_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR172_crm_com_app_col_elig_cea_vol_adj(BigDecimal r172_crm_com_app_col_elig_cea_vol_adj) {
		this.r172_crm_com_app_col_elig_cea_vol_adj = r172_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR172_crm_com_app_col_elig_fin_hai() {
		return r172_crm_com_app_col_elig_fin_hai;
	}
	public void setR172_crm_com_app_col_elig_fin_hai(BigDecimal r172_crm_com_app_col_elig_fin_hai) {
		this.r172_crm_com_app_col_elig_fin_hai = r172_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR172_crm_com_app_col_cea_val_aft_crm() {
		return r172_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR172_crm_com_app_col_cea_val_aft_crm(BigDecimal r172_crm_com_app_col_cea_val_aft_crm) {
		this.r172_crm_com_app_col_cea_val_aft_crm = r172_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR172_rwa_elig_cea_not_cov() {
		return r172_rwa_elig_cea_not_cov;
	}
	public void setR172_rwa_elig_cea_not_cov(BigDecimal r172_rwa_elig_cea_not_cov) {
		this.r172_rwa_elig_cea_not_cov = r172_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR172_rwa_unsec_cea_sub_cre_ris() {
		return r172_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR172_rwa_unsec_cea_sub_cre_ris(BigDecimal r172_rwa_unsec_cea_sub_cre_ris) {
		this.r172_rwa_unsec_cea_sub_cre_ris = r172_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR172_rwa_unsec_cea() {
		return r172_rwa_unsec_cea;
	}
	public void setR172_rwa_unsec_cea(BigDecimal r172_rwa_unsec_cea) {
		this.r172_rwa_unsec_cea = r172_rwa_unsec_cea;
	}
	public BigDecimal getR172_rwa_tot_ris_wei_ass() {
		return r172_rwa_tot_ris_wei_ass;
	}
	public void setR172_rwa_tot_ris_wei_ass(BigDecimal r172_rwa_tot_ris_wei_ass) {
		this.r172_rwa_tot_ris_wei_ass = r172_rwa_tot_ris_wei_ass;
	}
	public String getR173_exposure_class_off_bal() {
		return r173_exposure_class_off_bal;
	}
	public void setR173_exposure_class_off_bal(String r173_exposure_class_off_bal) {
		this.r173_exposure_class_off_bal = r173_exposure_class_off_bal;
	}
	public BigDecimal getR173_nom_pri_amt() {
		return r173_nom_pri_amt;
	}
	public void setR173_nom_pri_amt(BigDecimal r173_nom_pri_amt) {
		this.r173_nom_pri_amt = r173_nom_pri_amt;
	}
	public BigDecimal getR173_ccf() {
		return r173_ccf;
	}
	public void setR173_ccf(BigDecimal r173_ccf) {
		this.r173_ccf = r173_ccf;
	}
	public BigDecimal getR173_cea() {
		return r173_cea;
	}
	public void setR173_cea(BigDecimal r173_cea) {
		this.r173_cea = r173_cea;
	}
	public BigDecimal getR173_cea_elig_coun_bilt_net() {
		return r173_cea_elig_coun_bilt_net;
	}
	public void setR173_cea_elig_coun_bilt_net(BigDecimal r173_cea_elig_coun_bilt_net) {
		this.r173_cea_elig_coun_bilt_net = r173_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR173_cea_aft_net() {
		return r173_cea_aft_net;
	}
	public void setR173_cea_aft_net(BigDecimal r173_cea_aft_net) {
		this.r173_cea_aft_net = r173_cea_aft_net;
	}
	public BigDecimal getR173_crm_sub_app_cea_elig() {
		return r173_crm_sub_app_cea_elig;
	}
	public void setR173_crm_sub_app_cea_elig(BigDecimal r173_crm_sub_app_cea_elig) {
		this.r173_crm_sub_app_cea_elig = r173_crm_sub_app_cea_elig;
	}
	public BigDecimal getR173_crm_sub_app_non_col_guar_elig() {
		return r173_crm_sub_app_non_col_guar_elig;
	}
	public void setR173_crm_sub_app_non_col_guar_elig(BigDecimal r173_crm_sub_app_non_col_guar_elig) {
		this.r173_crm_sub_app_non_col_guar_elig = r173_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR173_crm_sub_app_non_col_cre_der() {
		return r173_crm_sub_app_non_col_cre_der;
	}
	public void setR173_crm_sub_app_non_col_cre_der(BigDecimal r173_crm_sub_app_non_col_cre_der) {
		this.r173_crm_sub_app_non_col_cre_der = r173_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR173_crm_sub_app_col_elig_cash() {
		return r173_crm_sub_app_col_elig_cash;
	}
	public void setR173_crm_sub_app_col_elig_cash(BigDecimal r173_crm_sub_app_col_elig_cash) {
		this.r173_crm_sub_app_col_elig_cash = r173_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR173_crm_sub_app_col_elig_tre_bills() {
		return r173_crm_sub_app_col_elig_tre_bills;
	}
	public void setR173_crm_sub_app_col_elig_tre_bills(BigDecimal r173_crm_sub_app_col_elig_tre_bills) {
		this.r173_crm_sub_app_col_elig_tre_bills = r173_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR173_crm_sub_app_col_elig_deb_sec() {
		return r173_crm_sub_app_col_elig_deb_sec;
	}
	public void setR173_crm_sub_app_col_elig_deb_sec(BigDecimal r173_crm_sub_app_col_elig_deb_sec) {
		this.r173_crm_sub_app_col_elig_deb_sec = r173_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR173_crm_sub_app_col_elig_euiq() {
		return r173_crm_sub_app_col_elig_euiq;
	}
	public void setR173_crm_sub_app_col_elig_euiq(BigDecimal r173_crm_sub_app_col_elig_euiq) {
		this.r173_crm_sub_app_col_elig_euiq = r173_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR173_crm_sub_app_col_elig_uni_tru() {
		return r173_crm_sub_app_col_elig_uni_tru;
	}
	public void setR173_crm_sub_app_col_elig_uni_tru(BigDecimal r173_crm_sub_app_col_elig_uni_tru) {
		this.r173_crm_sub_app_col_elig_uni_tru = r173_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR173_crm_sub_app_col_cea_cov() {
		return r173_crm_sub_app_col_cea_cov;
	}
	public void setR173_crm_sub_app_col_cea_cov(BigDecimal r173_crm_sub_app_col_cea_cov) {
		this.r173_crm_sub_app_col_cea_cov = r173_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR173_crm_sub_app_col_cea_not_cov() {
		return r173_crm_sub_app_col_cea_not_cov;
	}
	public void setR173_crm_sub_app_col_cea_not_cov(BigDecimal r173_crm_sub_app_col_cea_not_cov) {
		this.r173_crm_sub_app_col_cea_not_cov = r173_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR173_crm_sub_app_rwa_ris_wei_crm() {
		return r173_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR173_crm_sub_app_rwa_ris_wei_crm(BigDecimal r173_crm_sub_app_rwa_ris_wei_crm) {
		this.r173_crm_sub_app_rwa_ris_wei_crm = r173_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR173_crm_sub_app_rwa_ris_cea_cov() {
		return r173_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR173_crm_sub_app_rwa_ris_cea_cov(BigDecimal r173_crm_sub_app_rwa_ris_cea_cov) {
		this.r173_crm_sub_app_rwa_ris_cea_cov = r173_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR173_crm_sub_app_rwa_appl_org_coun() {
		return r173_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR173_crm_sub_app_rwa_appl_org_coun(BigDecimal r173_crm_sub_app_rwa_appl_org_coun) {
		this.r173_crm_sub_app_rwa_appl_org_coun = r173_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR173_crm_sub_app_rwa_ris_cea_not_cov() {
		return r173_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR173_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r173_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r173_crm_sub_app_rwa_ris_cea_not_cov = r173_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR173_crm_com_app_col_cea_elig_crm() {
		return r173_crm_com_app_col_cea_elig_crm;
	}
	public void setR173_crm_com_app_col_cea_elig_crm(BigDecimal r173_crm_com_app_col_cea_elig_crm) {
		this.r173_crm_com_app_col_cea_elig_crm = r173_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR173_crm_com_app_col_elig_cea_vol_adj() {
		return r173_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR173_crm_com_app_col_elig_cea_vol_adj(BigDecimal r173_crm_com_app_col_elig_cea_vol_adj) {
		this.r173_crm_com_app_col_elig_cea_vol_adj = r173_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR173_crm_com_app_col_elig_fin_hai() {
		return r173_crm_com_app_col_elig_fin_hai;
	}
	public void setR173_crm_com_app_col_elig_fin_hai(BigDecimal r173_crm_com_app_col_elig_fin_hai) {
		this.r173_crm_com_app_col_elig_fin_hai = r173_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR173_crm_com_app_col_cea_val_aft_crm() {
		return r173_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR173_crm_com_app_col_cea_val_aft_crm(BigDecimal r173_crm_com_app_col_cea_val_aft_crm) {
		this.r173_crm_com_app_col_cea_val_aft_crm = r173_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR173_rwa_elig_cea_not_cov() {
		return r173_rwa_elig_cea_not_cov;
	}
	public void setR173_rwa_elig_cea_not_cov(BigDecimal r173_rwa_elig_cea_not_cov) {
		this.r173_rwa_elig_cea_not_cov = r173_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR173_rwa_unsec_cea_sub_cre_ris() {
		return r173_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR173_rwa_unsec_cea_sub_cre_ris(BigDecimal r173_rwa_unsec_cea_sub_cre_ris) {
		this.r173_rwa_unsec_cea_sub_cre_ris = r173_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR173_rwa_unsec_cea() {
		return r173_rwa_unsec_cea;
	}
	public void setR173_rwa_unsec_cea(BigDecimal r173_rwa_unsec_cea) {
		this.r173_rwa_unsec_cea = r173_rwa_unsec_cea;
	}
	public BigDecimal getR173_rwa_tot_ris_wei_ass() {
		return r173_rwa_tot_ris_wei_ass;
	}
	public void setR173_rwa_tot_ris_wei_ass(BigDecimal r173_rwa_tot_ris_wei_ass) {
		this.r173_rwa_tot_ris_wei_ass = r173_rwa_tot_ris_wei_ass;
	}
	public String getR174_exposure_class_off_bal() {
		return r174_exposure_class_off_bal;
	}
	public void setR174_exposure_class_off_bal(String r174_exposure_class_off_bal) {
		this.r174_exposure_class_off_bal = r174_exposure_class_off_bal;
	}
	public BigDecimal getR174_nom_pri_amt() {
		return r174_nom_pri_amt;
	}
	public void setR174_nom_pri_amt(BigDecimal r174_nom_pri_amt) {
		this.r174_nom_pri_amt = r174_nom_pri_amt;
	}
	public BigDecimal getR174_ccf() {
		return r174_ccf;
	}
	public void setR174_ccf(BigDecimal r174_ccf) {
		this.r174_ccf = r174_ccf;
	}
	public BigDecimal getR174_cea() {
		return r174_cea;
	}
	public void setR174_cea(BigDecimal r174_cea) {
		this.r174_cea = r174_cea;
	}
	public BigDecimal getR174_cea_elig_coun_bilt_net() {
		return r174_cea_elig_coun_bilt_net;
	}
	public void setR174_cea_elig_coun_bilt_net(BigDecimal r174_cea_elig_coun_bilt_net) {
		this.r174_cea_elig_coun_bilt_net = r174_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR174_cea_aft_net() {
		return r174_cea_aft_net;
	}
	public void setR174_cea_aft_net(BigDecimal r174_cea_aft_net) {
		this.r174_cea_aft_net = r174_cea_aft_net;
	}
	public BigDecimal getR174_crm_sub_app_cea_elig() {
		return r174_crm_sub_app_cea_elig;
	}
	public void setR174_crm_sub_app_cea_elig(BigDecimal r174_crm_sub_app_cea_elig) {
		this.r174_crm_sub_app_cea_elig = r174_crm_sub_app_cea_elig;
	}
	public BigDecimal getR174_crm_sub_app_non_col_guar_elig() {
		return r174_crm_sub_app_non_col_guar_elig;
	}
	public void setR174_crm_sub_app_non_col_guar_elig(BigDecimal r174_crm_sub_app_non_col_guar_elig) {
		this.r174_crm_sub_app_non_col_guar_elig = r174_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR174_crm_sub_app_non_col_cre_der() {
		return r174_crm_sub_app_non_col_cre_der;
	}
	public void setR174_crm_sub_app_non_col_cre_der(BigDecimal r174_crm_sub_app_non_col_cre_der) {
		this.r174_crm_sub_app_non_col_cre_der = r174_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR174_crm_sub_app_col_elig_cash() {
		return r174_crm_sub_app_col_elig_cash;
	}
	public void setR174_crm_sub_app_col_elig_cash(BigDecimal r174_crm_sub_app_col_elig_cash) {
		this.r174_crm_sub_app_col_elig_cash = r174_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR174_crm_sub_app_col_elig_tre_bills() {
		return r174_crm_sub_app_col_elig_tre_bills;
	}
	public void setR174_crm_sub_app_col_elig_tre_bills(BigDecimal r174_crm_sub_app_col_elig_tre_bills) {
		this.r174_crm_sub_app_col_elig_tre_bills = r174_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR174_crm_sub_app_col_elig_deb_sec() {
		return r174_crm_sub_app_col_elig_deb_sec;
	}
	public void setR174_crm_sub_app_col_elig_deb_sec(BigDecimal r174_crm_sub_app_col_elig_deb_sec) {
		this.r174_crm_sub_app_col_elig_deb_sec = r174_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR174_crm_sub_app_col_elig_euiq() {
		return r174_crm_sub_app_col_elig_euiq;
	}
	public void setR174_crm_sub_app_col_elig_euiq(BigDecimal r174_crm_sub_app_col_elig_euiq) {
		this.r174_crm_sub_app_col_elig_euiq = r174_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR174_crm_sub_app_col_elig_uni_tru() {
		return r174_crm_sub_app_col_elig_uni_tru;
	}
	public void setR174_crm_sub_app_col_elig_uni_tru(BigDecimal r174_crm_sub_app_col_elig_uni_tru) {
		this.r174_crm_sub_app_col_elig_uni_tru = r174_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR174_crm_sub_app_col_cea_cov() {
		return r174_crm_sub_app_col_cea_cov;
	}
	public void setR174_crm_sub_app_col_cea_cov(BigDecimal r174_crm_sub_app_col_cea_cov) {
		this.r174_crm_sub_app_col_cea_cov = r174_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR174_crm_sub_app_col_cea_not_cov() {
		return r174_crm_sub_app_col_cea_not_cov;
	}
	public void setR174_crm_sub_app_col_cea_not_cov(BigDecimal r174_crm_sub_app_col_cea_not_cov) {
		this.r174_crm_sub_app_col_cea_not_cov = r174_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR174_crm_sub_app_rwa_ris_wei_crm() {
		return r174_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR174_crm_sub_app_rwa_ris_wei_crm(BigDecimal r174_crm_sub_app_rwa_ris_wei_crm) {
		this.r174_crm_sub_app_rwa_ris_wei_crm = r174_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR174_crm_sub_app_rwa_ris_cea_cov() {
		return r174_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR174_crm_sub_app_rwa_ris_cea_cov(BigDecimal r174_crm_sub_app_rwa_ris_cea_cov) {
		this.r174_crm_sub_app_rwa_ris_cea_cov = r174_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR174_crm_sub_app_rwa_appl_org_coun() {
		return r174_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR174_crm_sub_app_rwa_appl_org_coun(BigDecimal r174_crm_sub_app_rwa_appl_org_coun) {
		this.r174_crm_sub_app_rwa_appl_org_coun = r174_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR174_crm_sub_app_rwa_ris_cea_not_cov() {
		return r174_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR174_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r174_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r174_crm_sub_app_rwa_ris_cea_not_cov = r174_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR174_crm_com_app_col_cea_elig_crm() {
		return r174_crm_com_app_col_cea_elig_crm;
	}
	public void setR174_crm_com_app_col_cea_elig_crm(BigDecimal r174_crm_com_app_col_cea_elig_crm) {
		this.r174_crm_com_app_col_cea_elig_crm = r174_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR174_crm_com_app_col_elig_cea_vol_adj() {
		return r174_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR174_crm_com_app_col_elig_cea_vol_adj(BigDecimal r174_crm_com_app_col_elig_cea_vol_adj) {
		this.r174_crm_com_app_col_elig_cea_vol_adj = r174_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR174_crm_com_app_col_elig_fin_hai() {
		return r174_crm_com_app_col_elig_fin_hai;
	}
	public void setR174_crm_com_app_col_elig_fin_hai(BigDecimal r174_crm_com_app_col_elig_fin_hai) {
		this.r174_crm_com_app_col_elig_fin_hai = r174_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR174_crm_com_app_col_cea_val_aft_crm() {
		return r174_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR174_crm_com_app_col_cea_val_aft_crm(BigDecimal r174_crm_com_app_col_cea_val_aft_crm) {
		this.r174_crm_com_app_col_cea_val_aft_crm = r174_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR174_rwa_elig_cea_not_cov() {
		return r174_rwa_elig_cea_not_cov;
	}
	public void setR174_rwa_elig_cea_not_cov(BigDecimal r174_rwa_elig_cea_not_cov) {
		this.r174_rwa_elig_cea_not_cov = r174_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR174_rwa_unsec_cea_sub_cre_ris() {
		return r174_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR174_rwa_unsec_cea_sub_cre_ris(BigDecimal r174_rwa_unsec_cea_sub_cre_ris) {
		this.r174_rwa_unsec_cea_sub_cre_ris = r174_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR174_rwa_unsec_cea() {
		return r174_rwa_unsec_cea;
	}
	public void setR174_rwa_unsec_cea(BigDecimal r174_rwa_unsec_cea) {
		this.r174_rwa_unsec_cea = r174_rwa_unsec_cea;
	}
	public BigDecimal getR174_rwa_tot_ris_wei_ass() {
		return r174_rwa_tot_ris_wei_ass;
	}
	public void setR174_rwa_tot_ris_wei_ass(BigDecimal r174_rwa_tot_ris_wei_ass) {
		this.r174_rwa_tot_ris_wei_ass = r174_rwa_tot_ris_wei_ass;
	}
	public String getR175_exposure_class_off_bal() {
		return r175_exposure_class_off_bal;
	}
	public void setR175_exposure_class_off_bal(String r175_exposure_class_off_bal) {
		this.r175_exposure_class_off_bal = r175_exposure_class_off_bal;
	}
	public BigDecimal getR175_nom_pri_amt() {
		return r175_nom_pri_amt;
	}
	public void setR175_nom_pri_amt(BigDecimal r175_nom_pri_amt) {
		this.r175_nom_pri_amt = r175_nom_pri_amt;
	}
	public BigDecimal getR175_ccf() {
		return r175_ccf;
	}
	public void setR175_ccf(BigDecimal r175_ccf) {
		this.r175_ccf = r175_ccf;
	}
	public BigDecimal getR175_cea() {
		return r175_cea;
	}
	public void setR175_cea(BigDecimal r175_cea) {
		this.r175_cea = r175_cea;
	}
	public BigDecimal getR175_cea_elig_coun_bilt_net() {
		return r175_cea_elig_coun_bilt_net;
	}
	public void setR175_cea_elig_coun_bilt_net(BigDecimal r175_cea_elig_coun_bilt_net) {
		this.r175_cea_elig_coun_bilt_net = r175_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR175_cea_aft_net() {
		return r175_cea_aft_net;
	}
	public void setR175_cea_aft_net(BigDecimal r175_cea_aft_net) {
		this.r175_cea_aft_net = r175_cea_aft_net;
	}
	public BigDecimal getR175_crm_sub_app_cea_elig() {
		return r175_crm_sub_app_cea_elig;
	}
	public void setR175_crm_sub_app_cea_elig(BigDecimal r175_crm_sub_app_cea_elig) {
		this.r175_crm_sub_app_cea_elig = r175_crm_sub_app_cea_elig;
	}
	public BigDecimal getR175_crm_sub_app_non_col_guar_elig() {
		return r175_crm_sub_app_non_col_guar_elig;
	}
	public void setR175_crm_sub_app_non_col_guar_elig(BigDecimal r175_crm_sub_app_non_col_guar_elig) {
		this.r175_crm_sub_app_non_col_guar_elig = r175_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR175_crm_sub_app_non_col_cre_der() {
		return r175_crm_sub_app_non_col_cre_der;
	}
	public void setR175_crm_sub_app_non_col_cre_der(BigDecimal r175_crm_sub_app_non_col_cre_der) {
		this.r175_crm_sub_app_non_col_cre_der = r175_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR175_crm_sub_app_col_elig_cash() {
		return r175_crm_sub_app_col_elig_cash;
	}
	public void setR175_crm_sub_app_col_elig_cash(BigDecimal r175_crm_sub_app_col_elig_cash) {
		this.r175_crm_sub_app_col_elig_cash = r175_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR175_crm_sub_app_col_elig_tre_bills() {
		return r175_crm_sub_app_col_elig_tre_bills;
	}
	public void setR175_crm_sub_app_col_elig_tre_bills(BigDecimal r175_crm_sub_app_col_elig_tre_bills) {
		this.r175_crm_sub_app_col_elig_tre_bills = r175_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR175_crm_sub_app_col_elig_deb_sec() {
		return r175_crm_sub_app_col_elig_deb_sec;
	}
	public void setR175_crm_sub_app_col_elig_deb_sec(BigDecimal r175_crm_sub_app_col_elig_deb_sec) {
		this.r175_crm_sub_app_col_elig_deb_sec = r175_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR175_crm_sub_app_col_elig_euiq() {
		return r175_crm_sub_app_col_elig_euiq;
	}
	public void setR175_crm_sub_app_col_elig_euiq(BigDecimal r175_crm_sub_app_col_elig_euiq) {
		this.r175_crm_sub_app_col_elig_euiq = r175_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR175_crm_sub_app_col_elig_uni_tru() {
		return r175_crm_sub_app_col_elig_uni_tru;
	}
	public void setR175_crm_sub_app_col_elig_uni_tru(BigDecimal r175_crm_sub_app_col_elig_uni_tru) {
		this.r175_crm_sub_app_col_elig_uni_tru = r175_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR175_crm_sub_app_col_cea_cov() {
		return r175_crm_sub_app_col_cea_cov;
	}
	public void setR175_crm_sub_app_col_cea_cov(BigDecimal r175_crm_sub_app_col_cea_cov) {
		this.r175_crm_sub_app_col_cea_cov = r175_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR175_crm_sub_app_col_cea_not_cov() {
		return r175_crm_sub_app_col_cea_not_cov;
	}
	public void setR175_crm_sub_app_col_cea_not_cov(BigDecimal r175_crm_sub_app_col_cea_not_cov) {
		this.r175_crm_sub_app_col_cea_not_cov = r175_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR175_crm_sub_app_rwa_ris_wei_crm() {
		return r175_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR175_crm_sub_app_rwa_ris_wei_crm(BigDecimal r175_crm_sub_app_rwa_ris_wei_crm) {
		this.r175_crm_sub_app_rwa_ris_wei_crm = r175_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR175_crm_sub_app_rwa_ris_cea_cov() {
		return r175_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR175_crm_sub_app_rwa_ris_cea_cov(BigDecimal r175_crm_sub_app_rwa_ris_cea_cov) {
		this.r175_crm_sub_app_rwa_ris_cea_cov = r175_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR175_crm_sub_app_rwa_appl_org_coun() {
		return r175_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR175_crm_sub_app_rwa_appl_org_coun(BigDecimal r175_crm_sub_app_rwa_appl_org_coun) {
		this.r175_crm_sub_app_rwa_appl_org_coun = r175_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR175_crm_sub_app_rwa_ris_cea_not_cov() {
		return r175_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR175_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r175_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r175_crm_sub_app_rwa_ris_cea_not_cov = r175_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR175_crm_com_app_col_cea_elig_crm() {
		return r175_crm_com_app_col_cea_elig_crm;
	}
	public void setR175_crm_com_app_col_cea_elig_crm(BigDecimal r175_crm_com_app_col_cea_elig_crm) {
		this.r175_crm_com_app_col_cea_elig_crm = r175_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR175_crm_com_app_col_elig_cea_vol_adj() {
		return r175_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR175_crm_com_app_col_elig_cea_vol_adj(BigDecimal r175_crm_com_app_col_elig_cea_vol_adj) {
		this.r175_crm_com_app_col_elig_cea_vol_adj = r175_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR175_crm_com_app_col_elig_fin_hai() {
		return r175_crm_com_app_col_elig_fin_hai;
	}
	public void setR175_crm_com_app_col_elig_fin_hai(BigDecimal r175_crm_com_app_col_elig_fin_hai) {
		this.r175_crm_com_app_col_elig_fin_hai = r175_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR175_crm_com_app_col_cea_val_aft_crm() {
		return r175_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR175_crm_com_app_col_cea_val_aft_crm(BigDecimal r175_crm_com_app_col_cea_val_aft_crm) {
		this.r175_crm_com_app_col_cea_val_aft_crm = r175_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR175_rwa_elig_cea_not_cov() {
		return r175_rwa_elig_cea_not_cov;
	}
	public void setR175_rwa_elig_cea_not_cov(BigDecimal r175_rwa_elig_cea_not_cov) {
		this.r175_rwa_elig_cea_not_cov = r175_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR175_rwa_unsec_cea_sub_cre_ris() {
		return r175_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR175_rwa_unsec_cea_sub_cre_ris(BigDecimal r175_rwa_unsec_cea_sub_cre_ris) {
		this.r175_rwa_unsec_cea_sub_cre_ris = r175_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR175_rwa_unsec_cea() {
		return r175_rwa_unsec_cea;
	}
	public void setR175_rwa_unsec_cea(BigDecimal r175_rwa_unsec_cea) {
		this.r175_rwa_unsec_cea = r175_rwa_unsec_cea;
	}
	public BigDecimal getR175_rwa_tot_ris_wei_ass() {
		return r175_rwa_tot_ris_wei_ass;
	}
	public void setR175_rwa_tot_ris_wei_ass(BigDecimal r175_rwa_tot_ris_wei_ass) {
		this.r175_rwa_tot_ris_wei_ass = r175_rwa_tot_ris_wei_ass;
	}
	public String getR176_exposure_class_off_bal() {
		return r176_exposure_class_off_bal;
	}
	public void setR176_exposure_class_off_bal(String r176_exposure_class_off_bal) {
		this.r176_exposure_class_off_bal = r176_exposure_class_off_bal;
	}
	public BigDecimal getR176_nom_pri_amt() {
		return r176_nom_pri_amt;
	}
	public void setR176_nom_pri_amt(BigDecimal r176_nom_pri_amt) {
		this.r176_nom_pri_amt = r176_nom_pri_amt;
	}
	public BigDecimal getR176_ccf() {
		return r176_ccf;
	}
	public void setR176_ccf(BigDecimal r176_ccf) {
		this.r176_ccf = r176_ccf;
	}
	public BigDecimal getR176_cea() {
		return r176_cea;
	}
	public void setR176_cea(BigDecimal r176_cea) {
		this.r176_cea = r176_cea;
	}
	public BigDecimal getR176_cea_elig_coun_bilt_net() {
		return r176_cea_elig_coun_bilt_net;
	}
	public void setR176_cea_elig_coun_bilt_net(BigDecimal r176_cea_elig_coun_bilt_net) {
		this.r176_cea_elig_coun_bilt_net = r176_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR176_cea_aft_net() {
		return r176_cea_aft_net;
	}
	public void setR176_cea_aft_net(BigDecimal r176_cea_aft_net) {
		this.r176_cea_aft_net = r176_cea_aft_net;
	}
	public BigDecimal getR176_crm_sub_app_cea_elig() {
		return r176_crm_sub_app_cea_elig;
	}
	public void setR176_crm_sub_app_cea_elig(BigDecimal r176_crm_sub_app_cea_elig) {
		this.r176_crm_sub_app_cea_elig = r176_crm_sub_app_cea_elig;
	}
	public BigDecimal getR176_crm_sub_app_non_col_guar_elig() {
		return r176_crm_sub_app_non_col_guar_elig;
	}
	public void setR176_crm_sub_app_non_col_guar_elig(BigDecimal r176_crm_sub_app_non_col_guar_elig) {
		this.r176_crm_sub_app_non_col_guar_elig = r176_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR176_crm_sub_app_non_col_cre_der() {
		return r176_crm_sub_app_non_col_cre_der;
	}
	public void setR176_crm_sub_app_non_col_cre_der(BigDecimal r176_crm_sub_app_non_col_cre_der) {
		this.r176_crm_sub_app_non_col_cre_der = r176_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR176_crm_sub_app_col_elig_cash() {
		return r176_crm_sub_app_col_elig_cash;
	}
	public void setR176_crm_sub_app_col_elig_cash(BigDecimal r176_crm_sub_app_col_elig_cash) {
		this.r176_crm_sub_app_col_elig_cash = r176_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR176_crm_sub_app_col_elig_tre_bills() {
		return r176_crm_sub_app_col_elig_tre_bills;
	}
	public void setR176_crm_sub_app_col_elig_tre_bills(BigDecimal r176_crm_sub_app_col_elig_tre_bills) {
		this.r176_crm_sub_app_col_elig_tre_bills = r176_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR176_crm_sub_app_col_elig_deb_sec() {
		return r176_crm_sub_app_col_elig_deb_sec;
	}
	public void setR176_crm_sub_app_col_elig_deb_sec(BigDecimal r176_crm_sub_app_col_elig_deb_sec) {
		this.r176_crm_sub_app_col_elig_deb_sec = r176_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR176_crm_sub_app_col_elig_euiq() {
		return r176_crm_sub_app_col_elig_euiq;
	}
	public void setR176_crm_sub_app_col_elig_euiq(BigDecimal r176_crm_sub_app_col_elig_euiq) {
		this.r176_crm_sub_app_col_elig_euiq = r176_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR176_crm_sub_app_col_elig_uni_tru() {
		return r176_crm_sub_app_col_elig_uni_tru;
	}
	public void setR176_crm_sub_app_col_elig_uni_tru(BigDecimal r176_crm_sub_app_col_elig_uni_tru) {
		this.r176_crm_sub_app_col_elig_uni_tru = r176_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR176_crm_sub_app_col_cea_cov() {
		return r176_crm_sub_app_col_cea_cov;
	}
	public void setR176_crm_sub_app_col_cea_cov(BigDecimal r176_crm_sub_app_col_cea_cov) {
		this.r176_crm_sub_app_col_cea_cov = r176_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR176_crm_sub_app_col_cea_not_cov() {
		return r176_crm_sub_app_col_cea_not_cov;
	}
	public void setR176_crm_sub_app_col_cea_not_cov(BigDecimal r176_crm_sub_app_col_cea_not_cov) {
		this.r176_crm_sub_app_col_cea_not_cov = r176_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR176_crm_sub_app_rwa_ris_wei_crm() {
		return r176_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR176_crm_sub_app_rwa_ris_wei_crm(BigDecimal r176_crm_sub_app_rwa_ris_wei_crm) {
		this.r176_crm_sub_app_rwa_ris_wei_crm = r176_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR176_crm_sub_app_rwa_ris_cea_cov() {
		return r176_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR176_crm_sub_app_rwa_ris_cea_cov(BigDecimal r176_crm_sub_app_rwa_ris_cea_cov) {
		this.r176_crm_sub_app_rwa_ris_cea_cov = r176_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR176_crm_sub_app_rwa_appl_org_coun() {
		return r176_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR176_crm_sub_app_rwa_appl_org_coun(BigDecimal r176_crm_sub_app_rwa_appl_org_coun) {
		this.r176_crm_sub_app_rwa_appl_org_coun = r176_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR176_crm_sub_app_rwa_ris_cea_not_cov() {
		return r176_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR176_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r176_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r176_crm_sub_app_rwa_ris_cea_not_cov = r176_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR176_crm_com_app_col_cea_elig_crm() {
		return r176_crm_com_app_col_cea_elig_crm;
	}
	public void setR176_crm_com_app_col_cea_elig_crm(BigDecimal r176_crm_com_app_col_cea_elig_crm) {
		this.r176_crm_com_app_col_cea_elig_crm = r176_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR176_crm_com_app_col_elig_cea_vol_adj() {
		return r176_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR176_crm_com_app_col_elig_cea_vol_adj(BigDecimal r176_crm_com_app_col_elig_cea_vol_adj) {
		this.r176_crm_com_app_col_elig_cea_vol_adj = r176_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR176_crm_com_app_col_elig_fin_hai() {
		return r176_crm_com_app_col_elig_fin_hai;
	}
	public void setR176_crm_com_app_col_elig_fin_hai(BigDecimal r176_crm_com_app_col_elig_fin_hai) {
		this.r176_crm_com_app_col_elig_fin_hai = r176_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR176_crm_com_app_col_cea_val_aft_crm() {
		return r176_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR176_crm_com_app_col_cea_val_aft_crm(BigDecimal r176_crm_com_app_col_cea_val_aft_crm) {
		this.r176_crm_com_app_col_cea_val_aft_crm = r176_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR176_rwa_elig_cea_not_cov() {
		return r176_rwa_elig_cea_not_cov;
	}
	public void setR176_rwa_elig_cea_not_cov(BigDecimal r176_rwa_elig_cea_not_cov) {
		this.r176_rwa_elig_cea_not_cov = r176_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR176_rwa_unsec_cea_sub_cre_ris() {
		return r176_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR176_rwa_unsec_cea_sub_cre_ris(BigDecimal r176_rwa_unsec_cea_sub_cre_ris) {
		this.r176_rwa_unsec_cea_sub_cre_ris = r176_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR176_rwa_unsec_cea() {
		return r176_rwa_unsec_cea;
	}
	public void setR176_rwa_unsec_cea(BigDecimal r176_rwa_unsec_cea) {
		this.r176_rwa_unsec_cea = r176_rwa_unsec_cea;
	}
	public BigDecimal getR176_rwa_tot_ris_wei_ass() {
		return r176_rwa_tot_ris_wei_ass;
	}
	public void setR176_rwa_tot_ris_wei_ass(BigDecimal r176_rwa_tot_ris_wei_ass) {
		this.r176_rwa_tot_ris_wei_ass = r176_rwa_tot_ris_wei_ass;
	}
	public String getR177_exposure_class_off_bal() {
		return r177_exposure_class_off_bal;
	}
	public void setR177_exposure_class_off_bal(String r177_exposure_class_off_bal) {
		this.r177_exposure_class_off_bal = r177_exposure_class_off_bal;
	}
	public BigDecimal getR177_nom_pri_amt() {
		return r177_nom_pri_amt;
	}
	public void setR177_nom_pri_amt(BigDecimal r177_nom_pri_amt) {
		this.r177_nom_pri_amt = r177_nom_pri_amt;
	}
	public BigDecimal getR177_ccf() {
		return r177_ccf;
	}
	public void setR177_ccf(BigDecimal r177_ccf) {
		this.r177_ccf = r177_ccf;
	}
	public BigDecimal getR177_cea() {
		return r177_cea;
	}
	public void setR177_cea(BigDecimal r177_cea) {
		this.r177_cea = r177_cea;
	}
	public BigDecimal getR177_cea_elig_coun_bilt_net() {
		return r177_cea_elig_coun_bilt_net;
	}
	public void setR177_cea_elig_coun_bilt_net(BigDecimal r177_cea_elig_coun_bilt_net) {
		this.r177_cea_elig_coun_bilt_net = r177_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR177_cea_aft_net() {
		return r177_cea_aft_net;
	}
	public void setR177_cea_aft_net(BigDecimal r177_cea_aft_net) {
		this.r177_cea_aft_net = r177_cea_aft_net;
	}
	public BigDecimal getR177_crm_sub_app_cea_elig() {
		return r177_crm_sub_app_cea_elig;
	}
	public void setR177_crm_sub_app_cea_elig(BigDecimal r177_crm_sub_app_cea_elig) {
		this.r177_crm_sub_app_cea_elig = r177_crm_sub_app_cea_elig;
	}
	public BigDecimal getR177_crm_sub_app_non_col_guar_elig() {
		return r177_crm_sub_app_non_col_guar_elig;
	}
	public void setR177_crm_sub_app_non_col_guar_elig(BigDecimal r177_crm_sub_app_non_col_guar_elig) {
		this.r177_crm_sub_app_non_col_guar_elig = r177_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR177_crm_sub_app_non_col_cre_der() {
		return r177_crm_sub_app_non_col_cre_der;
	}
	public void setR177_crm_sub_app_non_col_cre_der(BigDecimal r177_crm_sub_app_non_col_cre_der) {
		this.r177_crm_sub_app_non_col_cre_der = r177_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR177_crm_sub_app_col_elig_cash() {
		return r177_crm_sub_app_col_elig_cash;
	}
	public void setR177_crm_sub_app_col_elig_cash(BigDecimal r177_crm_sub_app_col_elig_cash) {
		this.r177_crm_sub_app_col_elig_cash = r177_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR177_crm_sub_app_col_elig_tre_bills() {
		return r177_crm_sub_app_col_elig_tre_bills;
	}
	public void setR177_crm_sub_app_col_elig_tre_bills(BigDecimal r177_crm_sub_app_col_elig_tre_bills) {
		this.r177_crm_sub_app_col_elig_tre_bills = r177_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR177_crm_sub_app_col_elig_deb_sec() {
		return r177_crm_sub_app_col_elig_deb_sec;
	}
	public void setR177_crm_sub_app_col_elig_deb_sec(BigDecimal r177_crm_sub_app_col_elig_deb_sec) {
		this.r177_crm_sub_app_col_elig_deb_sec = r177_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR177_crm_sub_app_col_elig_euiq() {
		return r177_crm_sub_app_col_elig_euiq;
	}
	public void setR177_crm_sub_app_col_elig_euiq(BigDecimal r177_crm_sub_app_col_elig_euiq) {
		this.r177_crm_sub_app_col_elig_euiq = r177_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR177_crm_sub_app_col_elig_uni_tru() {
		return r177_crm_sub_app_col_elig_uni_tru;
	}
	public void setR177_crm_sub_app_col_elig_uni_tru(BigDecimal r177_crm_sub_app_col_elig_uni_tru) {
		this.r177_crm_sub_app_col_elig_uni_tru = r177_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR177_crm_sub_app_col_cea_cov() {
		return r177_crm_sub_app_col_cea_cov;
	}
	public void setR177_crm_sub_app_col_cea_cov(BigDecimal r177_crm_sub_app_col_cea_cov) {
		this.r177_crm_sub_app_col_cea_cov = r177_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR177_crm_sub_app_col_cea_not_cov() {
		return r177_crm_sub_app_col_cea_not_cov;
	}
	public void setR177_crm_sub_app_col_cea_not_cov(BigDecimal r177_crm_sub_app_col_cea_not_cov) {
		this.r177_crm_sub_app_col_cea_not_cov = r177_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR177_crm_sub_app_rwa_ris_wei_crm() {
		return r177_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR177_crm_sub_app_rwa_ris_wei_crm(BigDecimal r177_crm_sub_app_rwa_ris_wei_crm) {
		this.r177_crm_sub_app_rwa_ris_wei_crm = r177_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR177_crm_sub_app_rwa_ris_cea_cov() {
		return r177_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR177_crm_sub_app_rwa_ris_cea_cov(BigDecimal r177_crm_sub_app_rwa_ris_cea_cov) {
		this.r177_crm_sub_app_rwa_ris_cea_cov = r177_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR177_crm_sub_app_rwa_appl_org_coun() {
		return r177_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR177_crm_sub_app_rwa_appl_org_coun(BigDecimal r177_crm_sub_app_rwa_appl_org_coun) {
		this.r177_crm_sub_app_rwa_appl_org_coun = r177_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR177_crm_sub_app_rwa_ris_cea_not_cov() {
		return r177_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR177_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r177_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r177_crm_sub_app_rwa_ris_cea_not_cov = r177_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR177_crm_com_app_col_cea_elig_crm() {
		return r177_crm_com_app_col_cea_elig_crm;
	}
	public void setR177_crm_com_app_col_cea_elig_crm(BigDecimal r177_crm_com_app_col_cea_elig_crm) {
		this.r177_crm_com_app_col_cea_elig_crm = r177_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR177_crm_com_app_col_elig_cea_vol_adj() {
		return r177_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR177_crm_com_app_col_elig_cea_vol_adj(BigDecimal r177_crm_com_app_col_elig_cea_vol_adj) {
		this.r177_crm_com_app_col_elig_cea_vol_adj = r177_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR177_crm_com_app_col_elig_fin_hai() {
		return r177_crm_com_app_col_elig_fin_hai;
	}
	public void setR177_crm_com_app_col_elig_fin_hai(BigDecimal r177_crm_com_app_col_elig_fin_hai) {
		this.r177_crm_com_app_col_elig_fin_hai = r177_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR177_crm_com_app_col_cea_val_aft_crm() {
		return r177_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR177_crm_com_app_col_cea_val_aft_crm(BigDecimal r177_crm_com_app_col_cea_val_aft_crm) {
		this.r177_crm_com_app_col_cea_val_aft_crm = r177_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR177_rwa_elig_cea_not_cov() {
		return r177_rwa_elig_cea_not_cov;
	}
	public void setR177_rwa_elig_cea_not_cov(BigDecimal r177_rwa_elig_cea_not_cov) {
		this.r177_rwa_elig_cea_not_cov = r177_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR177_rwa_unsec_cea_sub_cre_ris() {
		return r177_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR177_rwa_unsec_cea_sub_cre_ris(BigDecimal r177_rwa_unsec_cea_sub_cre_ris) {
		this.r177_rwa_unsec_cea_sub_cre_ris = r177_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR177_rwa_unsec_cea() {
		return r177_rwa_unsec_cea;
	}
	public void setR177_rwa_unsec_cea(BigDecimal r177_rwa_unsec_cea) {
		this.r177_rwa_unsec_cea = r177_rwa_unsec_cea;
	}
	public BigDecimal getR177_rwa_tot_ris_wei_ass() {
		return r177_rwa_tot_ris_wei_ass;
	}
	public void setR177_rwa_tot_ris_wei_ass(BigDecimal r177_rwa_tot_ris_wei_ass) {
		this.r177_rwa_tot_ris_wei_ass = r177_rwa_tot_ris_wei_ass;
	}
	public String getR178_exposure_class_off_bal() {
		return r178_exposure_class_off_bal;
	}
	public void setR178_exposure_class_off_bal(String r178_exposure_class_off_bal) {
		this.r178_exposure_class_off_bal = r178_exposure_class_off_bal;
	}
	public BigDecimal getR178_nom_pri_amt() {
		return r178_nom_pri_amt;
	}
	public void setR178_nom_pri_amt(BigDecimal r178_nom_pri_amt) {
		this.r178_nom_pri_amt = r178_nom_pri_amt;
	}
	public BigDecimal getR178_ccf() {
		return r178_ccf;
	}
	public void setR178_ccf(BigDecimal r178_ccf) {
		this.r178_ccf = r178_ccf;
	}
	public BigDecimal getR178_cea() {
		return r178_cea;
	}
	public void setR178_cea(BigDecimal r178_cea) {
		this.r178_cea = r178_cea;
	}
	public BigDecimal getR178_cea_elig_coun_bilt_net() {
		return r178_cea_elig_coun_bilt_net;
	}
	public void setR178_cea_elig_coun_bilt_net(BigDecimal r178_cea_elig_coun_bilt_net) {
		this.r178_cea_elig_coun_bilt_net = r178_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR178_cea_aft_net() {
		return r178_cea_aft_net;
	}
	public void setR178_cea_aft_net(BigDecimal r178_cea_aft_net) {
		this.r178_cea_aft_net = r178_cea_aft_net;
	}
	public BigDecimal getR178_crm_sub_app_cea_elig() {
		return r178_crm_sub_app_cea_elig;
	}
	public void setR178_crm_sub_app_cea_elig(BigDecimal r178_crm_sub_app_cea_elig) {
		this.r178_crm_sub_app_cea_elig = r178_crm_sub_app_cea_elig;
	}
	public BigDecimal getR178_crm_sub_app_non_col_guar_elig() {
		return r178_crm_sub_app_non_col_guar_elig;
	}
	public void setR178_crm_sub_app_non_col_guar_elig(BigDecimal r178_crm_sub_app_non_col_guar_elig) {
		this.r178_crm_sub_app_non_col_guar_elig = r178_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR178_crm_sub_app_non_col_cre_der() {
		return r178_crm_sub_app_non_col_cre_der;
	}
	public void setR178_crm_sub_app_non_col_cre_der(BigDecimal r178_crm_sub_app_non_col_cre_der) {
		this.r178_crm_sub_app_non_col_cre_der = r178_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR178_crm_sub_app_col_elig_cash() {
		return r178_crm_sub_app_col_elig_cash;
	}
	public void setR178_crm_sub_app_col_elig_cash(BigDecimal r178_crm_sub_app_col_elig_cash) {
		this.r178_crm_sub_app_col_elig_cash = r178_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR178_crm_sub_app_col_elig_tre_bills() {
		return r178_crm_sub_app_col_elig_tre_bills;
	}
	public void setR178_crm_sub_app_col_elig_tre_bills(BigDecimal r178_crm_sub_app_col_elig_tre_bills) {
		this.r178_crm_sub_app_col_elig_tre_bills = r178_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR178_crm_sub_app_col_elig_deb_sec() {
		return r178_crm_sub_app_col_elig_deb_sec;
	}
	public void setR178_crm_sub_app_col_elig_deb_sec(BigDecimal r178_crm_sub_app_col_elig_deb_sec) {
		this.r178_crm_sub_app_col_elig_deb_sec = r178_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR178_crm_sub_app_col_elig_euiq() {
		return r178_crm_sub_app_col_elig_euiq;
	}
	public void setR178_crm_sub_app_col_elig_euiq(BigDecimal r178_crm_sub_app_col_elig_euiq) {
		this.r178_crm_sub_app_col_elig_euiq = r178_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR178_crm_sub_app_col_elig_uni_tru() {
		return r178_crm_sub_app_col_elig_uni_tru;
	}
	public void setR178_crm_sub_app_col_elig_uni_tru(BigDecimal r178_crm_sub_app_col_elig_uni_tru) {
		this.r178_crm_sub_app_col_elig_uni_tru = r178_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR178_crm_sub_app_col_cea_cov() {
		return r178_crm_sub_app_col_cea_cov;
	}
	public void setR178_crm_sub_app_col_cea_cov(BigDecimal r178_crm_sub_app_col_cea_cov) {
		this.r178_crm_sub_app_col_cea_cov = r178_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR178_crm_sub_app_col_cea_not_cov() {
		return r178_crm_sub_app_col_cea_not_cov;
	}
	public void setR178_crm_sub_app_col_cea_not_cov(BigDecimal r178_crm_sub_app_col_cea_not_cov) {
		this.r178_crm_sub_app_col_cea_not_cov = r178_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR178_crm_sub_app_rwa_ris_wei_crm() {
		return r178_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR178_crm_sub_app_rwa_ris_wei_crm(BigDecimal r178_crm_sub_app_rwa_ris_wei_crm) {
		this.r178_crm_sub_app_rwa_ris_wei_crm = r178_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR178_crm_sub_app_rwa_ris_cea_cov() {
		return r178_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR178_crm_sub_app_rwa_ris_cea_cov(BigDecimal r178_crm_sub_app_rwa_ris_cea_cov) {
		this.r178_crm_sub_app_rwa_ris_cea_cov = r178_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR178_crm_sub_app_rwa_appl_org_coun() {
		return r178_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR178_crm_sub_app_rwa_appl_org_coun(BigDecimal r178_crm_sub_app_rwa_appl_org_coun) {
		this.r178_crm_sub_app_rwa_appl_org_coun = r178_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR178_crm_sub_app_rwa_ris_cea_not_cov() {
		return r178_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR178_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r178_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r178_crm_sub_app_rwa_ris_cea_not_cov = r178_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR178_crm_com_app_col_cea_elig_crm() {
		return r178_crm_com_app_col_cea_elig_crm;
	}
	public void setR178_crm_com_app_col_cea_elig_crm(BigDecimal r178_crm_com_app_col_cea_elig_crm) {
		this.r178_crm_com_app_col_cea_elig_crm = r178_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR178_crm_com_app_col_elig_cea_vol_adj() {
		return r178_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR178_crm_com_app_col_elig_cea_vol_adj(BigDecimal r178_crm_com_app_col_elig_cea_vol_adj) {
		this.r178_crm_com_app_col_elig_cea_vol_adj = r178_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR178_crm_com_app_col_elig_fin_hai() {
		return r178_crm_com_app_col_elig_fin_hai;
	}
	public void setR178_crm_com_app_col_elig_fin_hai(BigDecimal r178_crm_com_app_col_elig_fin_hai) {
		this.r178_crm_com_app_col_elig_fin_hai = r178_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR178_crm_com_app_col_cea_val_aft_crm() {
		return r178_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR178_crm_com_app_col_cea_val_aft_crm(BigDecimal r178_crm_com_app_col_cea_val_aft_crm) {
		this.r178_crm_com_app_col_cea_val_aft_crm = r178_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR178_rwa_elig_cea_not_cov() {
		return r178_rwa_elig_cea_not_cov;
	}
	public void setR178_rwa_elig_cea_not_cov(BigDecimal r178_rwa_elig_cea_not_cov) {
		this.r178_rwa_elig_cea_not_cov = r178_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR178_rwa_unsec_cea_sub_cre_ris() {
		return r178_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR178_rwa_unsec_cea_sub_cre_ris(BigDecimal r178_rwa_unsec_cea_sub_cre_ris) {
		this.r178_rwa_unsec_cea_sub_cre_ris = r178_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR178_rwa_unsec_cea() {
		return r178_rwa_unsec_cea;
	}
	public void setR178_rwa_unsec_cea(BigDecimal r178_rwa_unsec_cea) {
		this.r178_rwa_unsec_cea = r178_rwa_unsec_cea;
	}
	public BigDecimal getR178_rwa_tot_ris_wei_ass() {
		return r178_rwa_tot_ris_wei_ass;
	}
	public void setR178_rwa_tot_ris_wei_ass(BigDecimal r178_rwa_tot_ris_wei_ass) {
		this.r178_rwa_tot_ris_wei_ass = r178_rwa_tot_ris_wei_ass;
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
	public M_SRWA_12A_Archival_Summary_Entity4() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	

}
