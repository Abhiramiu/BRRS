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
@Table(name = "BRRS_M_SRWA_12A_NEW_SUMMARYTABLE6")


public class M_SRWA_12A_NEW_Summary_Entity6 {
	
	

		
	
	private String	r179_exposure_class_off_bal;
	private BigDecimal	r179_nom_pri_amt;
	private BigDecimal	r179_ccf;
	private BigDecimal	r179_cea;
	private BigDecimal	r179_cea_elig_coun_bilt_net;
	private BigDecimal	r179_cea_aft_net;
	private BigDecimal	r179_crm_sub_app_cea_elig;
	private BigDecimal	r179_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r179_crm_sub_app_non_col_cre_der;
	private BigDecimal	r179_crm_sub_app_col_elig_cash;
	private BigDecimal	r179_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r179_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r179_crm_sub_app_col_elig_euiq;
	private BigDecimal	r179_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r179_crm_sub_app_col_cea_cov;
	private BigDecimal	r179_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r179_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r179_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r179_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r179_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r179_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r179_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r179_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r179_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r179_rwa_elig_cea_not_cov;
	private BigDecimal	r179_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r179_rwa_unsec_cea;
	private BigDecimal	r179_rwa_tot_ris_wei_ass;
	private String	r180_exposure_class_off_bal;
	private BigDecimal	r180_nom_pri_amt;
	private BigDecimal	r180_ccf;
	private BigDecimal	r180_cea;
	private BigDecimal	r180_cea_elig_coun_bilt_net;
	private BigDecimal	r180_cea_aft_net;
	private BigDecimal	r180_crm_sub_app_cea_elig;
	private BigDecimal	r180_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r180_crm_sub_app_non_col_cre_der;
	private BigDecimal	r180_crm_sub_app_col_elig_cash;
	private BigDecimal	r180_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r180_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r180_crm_sub_app_col_elig_euiq;
	private BigDecimal	r180_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r180_crm_sub_app_col_cea_cov;
	private BigDecimal	r180_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r180_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r180_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r180_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r180_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r180_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r180_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r180_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r180_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r180_rwa_elig_cea_not_cov;
	private BigDecimal	r180_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r180_rwa_unsec_cea;
	private BigDecimal	r180_rwa_tot_ris_wei_ass;
	private String	r181_exposure_class_off_bal;
	private BigDecimal	r181_nom_pri_amt;
	private BigDecimal	r181_ccf;
	private BigDecimal	r181_cea;
	private BigDecimal	r181_cea_elig_coun_bilt_net;
	private BigDecimal	r181_cea_aft_net;
	private BigDecimal	r181_crm_sub_app_cea_elig;
	private BigDecimal	r181_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r181_crm_sub_app_non_col_cre_der;
	private BigDecimal	r181_crm_sub_app_col_elig_cash;
	private BigDecimal	r181_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r181_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r181_crm_sub_app_col_elig_euiq;
	private BigDecimal	r181_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r181_crm_sub_app_col_cea_cov;
	private BigDecimal	r181_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r181_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r181_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r181_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r181_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r181_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r181_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r181_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r181_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r181_rwa_elig_cea_not_cov;
	private BigDecimal	r181_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r181_rwa_unsec_cea;
	private BigDecimal	r181_rwa_tot_ris_wei_ass;
	private String	r182_exposure_class_off_bal;
	private BigDecimal	r182_nom_pri_amt;
	private BigDecimal	r182_ccf;
	private BigDecimal	r182_cea;
	private BigDecimal	r182_cea_elig_coun_bilt_net;
	private BigDecimal	r182_cea_aft_net;
	private BigDecimal	r182_crm_sub_app_cea_elig;
	private BigDecimal	r182_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r182_crm_sub_app_non_col_cre_der;
	private BigDecimal	r182_crm_sub_app_col_elig_cash;
	private BigDecimal	r182_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r182_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r182_crm_sub_app_col_elig_euiq;
	private BigDecimal	r182_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r182_crm_sub_app_col_cea_cov;
	private BigDecimal	r182_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r182_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r182_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r182_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r182_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r182_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r182_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r182_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r182_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r182_rwa_elig_cea_not_cov;
	private BigDecimal	r182_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r182_rwa_unsec_cea;
	private BigDecimal	r182_rwa_tot_ris_wei_ass;
	private String	r183_exposure_class_off_bal;
	private BigDecimal	r183_nom_pri_amt;
	private BigDecimal	r183_ccf;
	private BigDecimal	r183_cea;
	private BigDecimal	r183_cea_elig_coun_bilt_net;
	private BigDecimal	r183_cea_aft_net;
	private BigDecimal	r183_crm_sub_app_cea_elig;
	private BigDecimal	r183_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r183_crm_sub_app_non_col_cre_der;
	private BigDecimal	r183_crm_sub_app_col_elig_cash;
	private BigDecimal	r183_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r183_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r183_crm_sub_app_col_elig_euiq;
	private BigDecimal	r183_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r183_crm_sub_app_col_cea_cov;
	private BigDecimal	r183_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r183_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r183_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r183_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r183_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r183_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r183_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r183_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r183_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r183_rwa_elig_cea_not_cov;
	private BigDecimal	r183_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r183_rwa_unsec_cea;
	private BigDecimal	r183_rwa_tot_ris_wei_ass;
	private String	r184_exposure_class_off_bal;
	private BigDecimal	r184_nom_pri_amt;
	private BigDecimal	r184_ccf;
	private BigDecimal	r184_cea;
	private BigDecimal	r184_cea_elig_coun_bilt_net;
	private BigDecimal	r184_cea_aft_net;
	private BigDecimal	r184_crm_sub_app_cea_elig;
	private BigDecimal	r184_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r184_crm_sub_app_non_col_cre_der;
	private BigDecimal	r184_crm_sub_app_col_elig_cash;
	private BigDecimal	r184_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r184_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r184_crm_sub_app_col_elig_euiq;
	private BigDecimal	r184_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r184_crm_sub_app_col_cea_cov;
	private BigDecimal	r184_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r184_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r184_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r184_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r184_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r184_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r184_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r184_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r184_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r184_rwa_elig_cea_not_cov;
	private BigDecimal	r184_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r184_rwa_unsec_cea;
	private BigDecimal	r184_rwa_tot_ris_wei_ass;
	private String	r185_exposure_class_off_bal;
	private BigDecimal	r185_nom_pri_amt;
	private BigDecimal	r185_ccf;
	private BigDecimal	r185_cea;
	private BigDecimal	r185_cea_elig_coun_bilt_net;
	private BigDecimal	r185_cea_aft_net;
	private BigDecimal	r185_crm_sub_app_cea_elig;
	private BigDecimal	r185_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r185_crm_sub_app_non_col_cre_der;
	private BigDecimal	r185_crm_sub_app_col_elig_cash;
	private BigDecimal	r185_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r185_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r185_crm_sub_app_col_elig_euiq;
	private BigDecimal	r185_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r185_crm_sub_app_col_cea_cov;
	private BigDecimal	r185_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r185_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r185_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r185_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r185_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r185_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r185_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r185_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r185_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r185_rwa_elig_cea_not_cov;
	private BigDecimal	r185_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r185_rwa_unsec_cea;
	private BigDecimal	r185_rwa_tot_ris_wei_ass;
	private String	r186_exposure_class_off_bal;
	private BigDecimal	r186_nom_pri_amt;
	private BigDecimal	r186_ccf;
	private BigDecimal	r186_cea;
	private BigDecimal	r186_cea_elig_coun_bilt_net;
	private BigDecimal	r186_cea_aft_net;
	private BigDecimal	r186_crm_sub_app_cea_elig;
	private BigDecimal	r186_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r186_crm_sub_app_non_col_cre_der;
	private BigDecimal	r186_crm_sub_app_col_elig_cash;
	private BigDecimal	r186_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r186_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r186_crm_sub_app_col_elig_euiq;
	private BigDecimal	r186_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r186_crm_sub_app_col_cea_cov;
	private BigDecimal	r186_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r186_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r186_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r186_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r186_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r186_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r186_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r186_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r186_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r186_rwa_elig_cea_not_cov;
	private BigDecimal	r186_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r186_rwa_unsec_cea;
	private BigDecimal	r186_rwa_tot_ris_wei_ass;
	private String	r187_exposure_class_off_bal;
	private BigDecimal	r187_nom_pri_amt;
	private BigDecimal	r187_ccf;
	private BigDecimal	r187_cea;
	private BigDecimal	r187_cea_elig_coun_bilt_net;
	private BigDecimal	r187_cea_aft_net;
	private BigDecimal	r187_crm_sub_app_cea_elig;
	private BigDecimal	r187_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r187_crm_sub_app_non_col_cre_der;
	private BigDecimal	r187_crm_sub_app_col_elig_cash;
	private BigDecimal	r187_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r187_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r187_crm_sub_app_col_elig_euiq;
	private BigDecimal	r187_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r187_crm_sub_app_col_cea_cov;
	private BigDecimal	r187_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r187_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r187_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r187_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r187_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r187_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r187_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r187_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r187_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r187_rwa_elig_cea_not_cov;
	private BigDecimal	r187_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r187_rwa_unsec_cea;
	private BigDecimal	r187_rwa_tot_ris_wei_ass;
	private String	r188_exposure_class_off_bal;
	private BigDecimal	r188_nom_pri_amt;
	private BigDecimal	r188_ccf;
	private BigDecimal	r188_cea;
	private BigDecimal	r188_cea_elig_coun_bilt_net;
	private BigDecimal	r188_cea_aft_net;
	private BigDecimal	r188_crm_sub_app_cea_elig;
	private BigDecimal	r188_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r188_crm_sub_app_non_col_cre_der;
	private BigDecimal	r188_crm_sub_app_col_elig_cash;
	private BigDecimal	r188_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r188_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r188_crm_sub_app_col_elig_euiq;
	private BigDecimal	r188_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r188_crm_sub_app_col_cea_cov;
	private BigDecimal	r188_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r188_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r188_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r188_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r188_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r188_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r188_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r188_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r188_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r188_rwa_elig_cea_not_cov;
	private BigDecimal	r188_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r188_rwa_unsec_cea;
	private BigDecimal	r188_rwa_tot_ris_wei_ass;
	private String	r189_exposure_class_off_bal;
	private BigDecimal	r189_nom_pri_amt;
	private BigDecimal	r189_ccf;
	private BigDecimal	r189_cea;
	private BigDecimal	r189_cea_elig_coun_bilt_net;
	private BigDecimal	r189_cea_aft_net;
	private BigDecimal	r189_crm_sub_app_cea_elig;
	private BigDecimal	r189_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r189_crm_sub_app_non_col_cre_der;
	private BigDecimal	r189_crm_sub_app_col_elig_cash;
	private BigDecimal	r189_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r189_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r189_crm_sub_app_col_elig_euiq;
	private BigDecimal	r189_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r189_crm_sub_app_col_cea_cov;
	private BigDecimal	r189_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r189_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r189_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r189_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r189_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r189_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r189_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r189_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r189_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r189_rwa_elig_cea_not_cov;
	private BigDecimal	r189_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r189_rwa_unsec_cea;
	private BigDecimal	r189_rwa_tot_ris_wei_ass;
	private String	r190_exposure_class_off_bal;
	private BigDecimal	r190_nom_pri_amt;
	private BigDecimal	r190_ccf;
	private BigDecimal	r190_cea;
	private BigDecimal	r190_cea_elig_coun_bilt_net;
	private BigDecimal	r190_cea_aft_net;
	private BigDecimal	r190_crm_sub_app_cea_elig;
	private BigDecimal	r190_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r190_crm_sub_app_non_col_cre_der;
	private BigDecimal	r190_crm_sub_app_col_elig_cash;
	private BigDecimal	r190_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r190_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r190_crm_sub_app_col_elig_euiq;
	private BigDecimal	r190_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r190_crm_sub_app_col_cea_cov;
	private BigDecimal	r190_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r190_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r190_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r190_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r190_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r190_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r190_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r190_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r190_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r190_rwa_elig_cea_not_cov;
	private BigDecimal	r190_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r190_rwa_unsec_cea;
	private BigDecimal	r190_rwa_tot_ris_wei_ass;
	private String	r191_exposure_class_off_bal;
	private BigDecimal	r191_nom_pri_amt;
	private BigDecimal	r191_ccf;
	private BigDecimal	r191_cea;
	private BigDecimal	r191_cea_elig_coun_bilt_net;
	private BigDecimal	r191_cea_aft_net;
	private BigDecimal	r191_crm_sub_app_cea_elig;
	private BigDecimal	r191_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r191_crm_sub_app_non_col_cre_der;
	private BigDecimal	r191_crm_sub_app_col_elig_cash;
	private BigDecimal	r191_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r191_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r191_crm_sub_app_col_elig_euiq;
	private BigDecimal	r191_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r191_crm_sub_app_col_cea_cov;
	private BigDecimal	r191_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r191_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r191_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r191_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r191_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r191_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r191_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r191_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r191_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r191_rwa_elig_cea_not_cov;
	private BigDecimal	r191_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r191_rwa_unsec_cea;
	private BigDecimal	r191_rwa_tot_ris_wei_ass;
	private String	r192_exposure_class_off_bal;
	private BigDecimal	r192_nom_pri_amt;
	private BigDecimal	r192_ccf;
	private BigDecimal	r192_cea;
	private BigDecimal	r192_cea_elig_coun_bilt_net;
	private BigDecimal	r192_cea_aft_net;
	private BigDecimal	r192_crm_sub_app_cea_elig;
	private BigDecimal	r192_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r192_crm_sub_app_non_col_cre_der;
	private BigDecimal	r192_crm_sub_app_col_elig_cash;
	private BigDecimal	r192_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r192_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r192_crm_sub_app_col_elig_euiq;
	private BigDecimal	r192_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r192_crm_sub_app_col_cea_cov;
	private BigDecimal	r192_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r192_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r192_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r192_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r192_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r192_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r192_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r192_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r192_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r192_rwa_elig_cea_not_cov;
	private BigDecimal	r192_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r192_rwa_unsec_cea;
	private BigDecimal	r192_rwa_tot_ris_wei_ass;
	private String	r193_exposure_class_off_bal;
	private BigDecimal	r193_nom_pri_amt;
	private BigDecimal	r193_ccf;
	private BigDecimal	r193_cea;
	private BigDecimal	r193_cea_elig_coun_bilt_net;
	private BigDecimal	r193_cea_aft_net;
	private BigDecimal	r193_crm_sub_app_cea_elig;
	private BigDecimal	r193_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r193_crm_sub_app_non_col_cre_der;
	private BigDecimal	r193_crm_sub_app_col_elig_cash;
	private BigDecimal	r193_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r193_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r193_crm_sub_app_col_elig_euiq;
	private BigDecimal	r193_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r193_crm_sub_app_col_cea_cov;
	private BigDecimal	r193_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r193_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r193_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r193_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r193_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r193_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r193_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r193_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r193_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r193_rwa_elig_cea_not_cov;
	private BigDecimal	r193_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r193_rwa_unsec_cea;
	private BigDecimal	r193_rwa_tot_ris_wei_ass;
	private String	r194_exposure_class_off_bal;
	private BigDecimal	r194_nom_pri_amt;
	private BigDecimal	r194_ccf;
	private BigDecimal	r194_cea;
	private BigDecimal	r194_cea_elig_coun_bilt_net;
	private BigDecimal	r194_cea_aft_net;
	private BigDecimal	r194_crm_sub_app_cea_elig;
	private BigDecimal	r194_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r194_crm_sub_app_non_col_cre_der;
	private BigDecimal	r194_crm_sub_app_col_elig_cash;
	private BigDecimal	r194_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r194_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r194_crm_sub_app_col_elig_euiq;
	private BigDecimal	r194_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r194_crm_sub_app_col_cea_cov;
	private BigDecimal	r194_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r194_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r194_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r194_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r194_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r194_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r194_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r194_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r194_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r194_rwa_elig_cea_not_cov;
	private BigDecimal	r194_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r194_rwa_unsec_cea;
	private BigDecimal	r194_rwa_tot_ris_wei_ass;
	private String	r195_exposure_class_off_bal;
	private BigDecimal	r195_nom_pri_amt;
	private BigDecimal	r195_ccf;
	private BigDecimal	r195_cea;
	private BigDecimal	r195_cea_elig_coun_bilt_net;
	private BigDecimal	r195_cea_aft_net;
	private BigDecimal	r195_crm_sub_app_cea_elig;
	private BigDecimal	r195_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r195_crm_sub_app_non_col_cre_der;
	private BigDecimal	r195_crm_sub_app_col_elig_cash;
	private BigDecimal	r195_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r195_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r195_crm_sub_app_col_elig_euiq;
	private BigDecimal	r195_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r195_crm_sub_app_col_cea_cov;
	private BigDecimal	r195_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r195_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r195_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r195_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r195_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r195_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r195_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r195_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r195_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r195_rwa_elig_cea_not_cov;
	private BigDecimal	r195_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r195_rwa_unsec_cea;
	private BigDecimal	r195_rwa_tot_ris_wei_ass;
	private String	r196_exposure_class_off_bal;
	private BigDecimal	r196_nom_pri_amt;
	private BigDecimal	r196_ccf;
	private BigDecimal	r196_cea;
	private BigDecimal	r196_cea_elig_coun_bilt_net;
	private BigDecimal	r196_cea_aft_net;
	private BigDecimal	r196_crm_sub_app_cea_elig;
	private BigDecimal	r196_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r196_crm_sub_app_non_col_cre_der;
	private BigDecimal	r196_crm_sub_app_col_elig_cash;
	private BigDecimal	r196_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r196_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r196_crm_sub_app_col_elig_euiq;
	private BigDecimal	r196_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r196_crm_sub_app_col_cea_cov;
	private BigDecimal	r196_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r196_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r196_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r196_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r196_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r196_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r196_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r196_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r196_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r196_rwa_elig_cea_not_cov;
	private BigDecimal	r196_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r196_rwa_unsec_cea;
	private BigDecimal	r196_rwa_tot_ris_wei_ass;
	private String	r197_exposure_class_off_bal;
	private BigDecimal	r197_nom_pri_amt;
	private BigDecimal	r197_ccf;
	private BigDecimal	r197_cea;
	private BigDecimal	r197_cea_elig_coun_bilt_net;
	private BigDecimal	r197_cea_aft_net;
	private BigDecimal	r197_crm_sub_app_cea_elig;
	private BigDecimal	r197_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r197_crm_sub_app_non_col_cre_der;
	private BigDecimal	r197_crm_sub_app_col_elig_cash;
	private BigDecimal	r197_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r197_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r197_crm_sub_app_col_elig_euiq;
	private BigDecimal	r197_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r197_crm_sub_app_col_cea_cov;
	private BigDecimal	r197_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r197_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r197_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r197_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r197_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r197_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r197_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r197_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r197_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r197_rwa_elig_cea_not_cov;
	private BigDecimal	r197_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r197_rwa_unsec_cea;
	private BigDecimal	r197_rwa_tot_ris_wei_ass;
	private String	r198_exposure_class_off_bal;
	private BigDecimal	r198_nom_pri_amt;
	private BigDecimal	r198_ccf;
	private BigDecimal	r198_cea;
	private BigDecimal	r198_cea_elig_coun_bilt_net;
	private BigDecimal	r198_cea_aft_net;
	private BigDecimal	r198_crm_sub_app_cea_elig;
	private BigDecimal	r198_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r198_crm_sub_app_non_col_cre_der;
	private BigDecimal	r198_crm_sub_app_col_elig_cash;
	private BigDecimal	r198_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r198_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r198_crm_sub_app_col_elig_euiq;
	private BigDecimal	r198_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r198_crm_sub_app_col_cea_cov;
	private BigDecimal	r198_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r198_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r198_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r198_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r198_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r198_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r198_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r198_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r198_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r198_rwa_elig_cea_not_cov;
	private BigDecimal	r198_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r198_rwa_unsec_cea;
	private BigDecimal	r198_rwa_tot_ris_wei_ass;
	private String	r199_exposure_class_off_bal;
	private BigDecimal	r199_nom_pri_amt;
	private BigDecimal	r199_ccf;
	private BigDecimal	r199_cea;
	private BigDecimal	r199_cea_elig_coun_bilt_net;
	private BigDecimal	r199_cea_aft_net;
	private BigDecimal	r199_crm_sub_app_cea_elig;
	private BigDecimal	r199_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r199_crm_sub_app_non_col_cre_der;
	private BigDecimal	r199_crm_sub_app_col_elig_cash;
	private BigDecimal	r199_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r199_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r199_crm_sub_app_col_elig_euiq;
	private BigDecimal	r199_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r199_crm_sub_app_col_cea_cov;
	private BigDecimal	r199_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r199_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r199_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r199_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r199_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r199_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r199_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r199_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r199_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r199_rwa_elig_cea_not_cov;
	private BigDecimal	r199_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r199_rwa_unsec_cea;
	private BigDecimal	r199_rwa_tot_ris_wei_ass;
	private String	r200_exposure_class_off_bal;
	private BigDecimal	r200_nom_pri_amt;
	private BigDecimal	r200_ccf;
	private BigDecimal	r200_cea;
	private BigDecimal	r200_cea_elig_coun_bilt_net;
	private BigDecimal	r200_cea_aft_net;
	private BigDecimal	r200_crm_sub_app_cea_elig;
	private BigDecimal	r200_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r200_crm_sub_app_non_col_cre_der;
	private BigDecimal	r200_crm_sub_app_col_elig_cash;
	private BigDecimal	r200_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r200_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r200_crm_sub_app_col_elig_euiq;
	private BigDecimal	r200_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r200_crm_sub_app_col_cea_cov;
	private BigDecimal	r200_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r200_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r200_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r200_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r200_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r200_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r200_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r200_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r200_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r200_rwa_elig_cea_not_cov;
	private BigDecimal	r200_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r200_rwa_unsec_cea;
	private BigDecimal	r200_rwa_tot_ris_wei_ass;
	private String	r201_exposure_class_off_bal;
	private BigDecimal	r201_nom_pri_amt;
	private BigDecimal	r201_ccf;
	private BigDecimal	r201_cea;
	private BigDecimal	r201_cea_elig_coun_bilt_net;
	private BigDecimal	r201_cea_aft_net;
	private BigDecimal	r201_crm_sub_app_cea_elig;
	private BigDecimal	r201_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r201_crm_sub_app_non_col_cre_der;
	private BigDecimal	r201_crm_sub_app_col_elig_cash;
	private BigDecimal	r201_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r201_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r201_crm_sub_app_col_elig_euiq;
	private BigDecimal	r201_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r201_crm_sub_app_col_cea_cov;
	private BigDecimal	r201_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r201_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r201_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r201_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r201_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r201_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r201_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r201_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r201_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r201_rwa_elig_cea_not_cov;
	private BigDecimal	r201_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r201_rwa_unsec_cea;
	private BigDecimal	r201_rwa_tot_ris_wei_ass;
	private String	r202_exposure_class_off_bal;
	private BigDecimal	r202_nom_pri_amt;
	private BigDecimal	r202_ccf;
	private BigDecimal	r202_cea;
	private BigDecimal	r202_cea_elig_coun_bilt_net;
	private BigDecimal	r202_cea_aft_net;
	private BigDecimal	r202_crm_sub_app_cea_elig;
	private BigDecimal	r202_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r202_crm_sub_app_non_col_cre_der;
	private BigDecimal	r202_crm_sub_app_col_elig_cash;
	private BigDecimal	r202_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r202_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r202_crm_sub_app_col_elig_euiq;
	private BigDecimal	r202_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r202_crm_sub_app_col_cea_cov;
	private BigDecimal	r202_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r202_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r202_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r202_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r202_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r202_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r202_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r202_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r202_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r202_rwa_elig_cea_not_cov;
	private BigDecimal	r202_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r202_rwa_unsec_cea;
	private BigDecimal	r202_rwa_tot_ris_wei_ass;
	private String	r203_exposure_class_off_bal;
	private BigDecimal	r203_nom_pri_amt;
	private BigDecimal	r203_ccf;
	private BigDecimal	r203_cea;
	private BigDecimal	r203_cea_elig_coun_bilt_net;
	private BigDecimal	r203_cea_aft_net;
	private BigDecimal	r203_crm_sub_app_cea_elig;
	private BigDecimal	r203_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r203_crm_sub_app_non_col_cre_der;
	private BigDecimal	r203_crm_sub_app_col_elig_cash;
	private BigDecimal	r203_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r203_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r203_crm_sub_app_col_elig_euiq;
	private BigDecimal	r203_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r203_crm_sub_app_col_cea_cov;
	private BigDecimal	r203_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r203_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r203_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r203_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r203_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r203_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r203_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r203_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r203_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r203_rwa_elig_cea_not_cov;
	private BigDecimal	r203_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r203_rwa_unsec_cea;
	private BigDecimal	r203_rwa_tot_ris_wei_ass;
	private String	r204_exposure_class_off_bal;
	private BigDecimal	r204_nom_pri_amt;
	private BigDecimal	r204_ccf;
	private BigDecimal	r204_cea;
	private BigDecimal	r204_cea_elig_coun_bilt_net;
	private BigDecimal	r204_cea_aft_net;
	private BigDecimal	r204_crm_sub_app_cea_elig;
	private BigDecimal	r204_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r204_crm_sub_app_non_col_cre_der;
	private BigDecimal	r204_crm_sub_app_col_elig_cash;
	private BigDecimal	r204_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r204_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r204_crm_sub_app_col_elig_euiq;
	private BigDecimal	r204_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r204_crm_sub_app_col_cea_cov;
	private BigDecimal	r204_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r204_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r204_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r204_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r204_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r204_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r204_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r204_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r204_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r204_rwa_elig_cea_not_cov;
	private BigDecimal	r204_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r204_rwa_unsec_cea;
	private BigDecimal	r204_rwa_tot_ris_wei_ass;
	private String	r205_exposure_class_off_bal;
	private BigDecimal	r205_nom_pri_amt;
	private BigDecimal	r205_ccf;
	private BigDecimal	r205_cea;
	private BigDecimal	r205_cea_elig_coun_bilt_net;
	private BigDecimal	r205_cea_aft_net;
	private BigDecimal	r205_crm_sub_app_cea_elig;
	private BigDecimal	r205_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r205_crm_sub_app_non_col_cre_der;
	private BigDecimal	r205_crm_sub_app_col_elig_cash;
	private BigDecimal	r205_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r205_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r205_crm_sub_app_col_elig_euiq;
	private BigDecimal	r205_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r205_crm_sub_app_col_cea_cov;
	private BigDecimal	r205_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r205_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r205_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r205_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r205_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r205_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r205_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r205_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r205_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r205_rwa_elig_cea_not_cov;
	private BigDecimal	r205_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r205_rwa_unsec_cea;
	private BigDecimal	r205_rwa_tot_ris_wei_ass;
	private String	r206_exposure_class_off_bal;
	private BigDecimal	r206_nom_pri_amt;
	private BigDecimal	r206_ccf;
	private BigDecimal	r206_cea;
	private BigDecimal	r206_cea_elig_coun_bilt_net;
	private BigDecimal	r206_cea_aft_net;
	private BigDecimal	r206_crm_sub_app_cea_elig;
	private BigDecimal	r206_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r206_crm_sub_app_non_col_cre_der;
	private BigDecimal	r206_crm_sub_app_col_elig_cash;
	private BigDecimal	r206_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r206_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r206_crm_sub_app_col_elig_euiq;
	private BigDecimal	r206_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r206_crm_sub_app_col_cea_cov;
	private BigDecimal	r206_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r206_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r206_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r206_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r206_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r206_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r206_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r206_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r206_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r206_rwa_elig_cea_not_cov;
	private BigDecimal	r206_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r206_rwa_unsec_cea;
	private BigDecimal	r206_rwa_tot_ris_wei_ass;
	private String	r207_exposure_class_off_bal;
	private BigDecimal	r207_nom_pri_amt;
	private BigDecimal	r207_ccf;
	private BigDecimal	r207_cea;
	private BigDecimal	r207_cea_elig_coun_bilt_net;
	private BigDecimal	r207_cea_aft_net;
	private BigDecimal	r207_crm_sub_app_cea_elig;
	private BigDecimal	r207_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r207_crm_sub_app_non_col_cre_der;
	private BigDecimal	r207_crm_sub_app_col_elig_cash;
	private BigDecimal	r207_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r207_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r207_crm_sub_app_col_elig_euiq;
	private BigDecimal	r207_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r207_crm_sub_app_col_cea_cov;
	private BigDecimal	r207_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r207_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r207_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r207_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r207_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r207_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r207_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r207_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r207_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r207_rwa_elig_cea_not_cov;
	private BigDecimal	r207_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r207_rwa_unsec_cea;
	private BigDecimal	r207_rwa_tot_ris_wei_ass;
	private String	r208_exposure_class_off_bal;
	private BigDecimal	r208_nom_pri_amt;
	private BigDecimal	r208_ccf;
	private BigDecimal	r208_cea;
	private BigDecimal	r208_cea_elig_coun_bilt_net;
	private BigDecimal	r208_cea_aft_net;
	private BigDecimal	r208_crm_sub_app_cea_elig;
	private BigDecimal	r208_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r208_crm_sub_app_non_col_cre_der;
	private BigDecimal	r208_crm_sub_app_col_elig_cash;
	private BigDecimal	r208_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r208_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r208_crm_sub_app_col_elig_euiq;
	private BigDecimal	r208_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r208_crm_sub_app_col_cea_cov;
	private BigDecimal	r208_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r208_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r208_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r208_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r208_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r208_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r208_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r208_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r208_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r208_rwa_elig_cea_not_cov;
	private BigDecimal	r208_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r208_rwa_unsec_cea;
	private BigDecimal	r208_rwa_tot_ris_wei_ass;
	private String	r209_exposure_class_off_bal;
	private BigDecimal	r209_nom_pri_amt;
	private BigDecimal	r209_ccf;
	private BigDecimal	r209_cea;
	private BigDecimal	r209_cea_elig_coun_bilt_net;
	private BigDecimal	r209_cea_aft_net;
	private BigDecimal	r209_crm_sub_app_cea_elig;
	private BigDecimal	r209_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r209_crm_sub_app_non_col_cre_der;
	private BigDecimal	r209_crm_sub_app_col_elig_cash;
	private BigDecimal	r209_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r209_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r209_crm_sub_app_col_elig_euiq;
	private BigDecimal	r209_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r209_crm_sub_app_col_cea_cov;
	private BigDecimal	r209_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r209_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r209_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r209_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r209_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r209_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r209_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r209_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r209_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r209_rwa_elig_cea_not_cov;
	private BigDecimal	r209_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r209_rwa_unsec_cea;
	private BigDecimal	r209_rwa_tot_ris_wei_ass;
	private String	r210_exposure_class_off_bal;
	private BigDecimal	r210_nom_pri_amt;
	private BigDecimal	r210_ccf;
	private BigDecimal	r210_cea;
	private BigDecimal	r210_cea_elig_coun_bilt_net;
	private BigDecimal	r210_cea_aft_net;
	private BigDecimal	r210_crm_sub_app_cea_elig;
	private BigDecimal	r210_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r210_crm_sub_app_non_col_cre_der;
	private BigDecimal	r210_crm_sub_app_col_elig_cash;
	private BigDecimal	r210_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r210_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r210_crm_sub_app_col_elig_euiq;
	private BigDecimal	r210_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r210_crm_sub_app_col_cea_cov;
	private BigDecimal	r210_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r210_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r210_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r210_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r210_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r210_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r210_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r210_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r210_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r210_rwa_elig_cea_not_cov;
	private BigDecimal	r210_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r210_rwa_unsec_cea;
	private BigDecimal	r210_rwa_tot_ris_wei_ass;
	private String	r211_exposure_class_off_bal;
	private BigDecimal	r211_nom_pri_amt;
	private BigDecimal	r211_ccf;
	private BigDecimal	r211_cea;
	private BigDecimal	r211_cea_elig_coun_bilt_net;
	private BigDecimal	r211_cea_aft_net;
	private BigDecimal	r211_crm_sub_app_cea_elig;
	private BigDecimal	r211_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r211_crm_sub_app_non_col_cre_der;
	private BigDecimal	r211_crm_sub_app_col_elig_cash;
	private BigDecimal	r211_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r211_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r211_crm_sub_app_col_elig_euiq;
	private BigDecimal	r211_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r211_crm_sub_app_col_cea_cov;
	private BigDecimal	r211_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r211_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r211_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r211_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r211_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r211_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r211_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r211_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r211_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r211_rwa_elig_cea_not_cov;
	private BigDecimal	r211_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r211_rwa_unsec_cea;
	private BigDecimal	r211_rwa_tot_ris_wei_ass;
	private String	r212_exposure_class_off_bal;
	private BigDecimal	r212_nom_pri_amt;
	private BigDecimal	r212_ccf;
	private BigDecimal	r212_cea;
	private BigDecimal	r212_cea_elig_coun_bilt_net;
	private BigDecimal	r212_cea_aft_net;
	private BigDecimal	r212_crm_sub_app_cea_elig;
	private BigDecimal	r212_crm_sub_app_non_col_guar_elig;
	private BigDecimal	r212_crm_sub_app_non_col_cre_der;
	private BigDecimal	r212_crm_sub_app_col_elig_cash;
	private BigDecimal	r212_crm_sub_app_col_elig_tre_bills;
	private BigDecimal	r212_crm_sub_app_col_elig_deb_sec;
	private BigDecimal	r212_crm_sub_app_col_elig_euiq;
	private BigDecimal	r212_crm_sub_app_col_elig_uni_tru;
	private BigDecimal	r212_crm_sub_app_col_cea_cov;
	private BigDecimal	r212_crm_sub_app_col_cea_not_cov;
	private BigDecimal	r212_crm_sub_app_rwa_ris_wei_crm;
	private BigDecimal	r212_crm_sub_app_rwa_ris_cea_cov;
	private BigDecimal	r212_crm_sub_app_rwa_appl_org_coun;
	private BigDecimal	r212_crm_sub_app_rwa_ris_cea_not_cov;
	private BigDecimal	r212_crm_com_app_col_cea_elig_crm;
	private BigDecimal	r212_crm_com_app_col_elig_cea_vol_adj;
	private BigDecimal	r212_crm_com_app_col_elig_fin_hai;
	private BigDecimal	r212_crm_com_app_col_cea_val_aft_crm;
	private BigDecimal	r212_rwa_elig_cea_not_cov;
	private BigDecimal	r212_rwa_unsec_cea_sub_cre_ris;
	private BigDecimal	r212_rwa_unsec_cea;
	private BigDecimal	r212_rwa_tot_ris_wei_ass;
	               
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
	public String getR179_exposure_class_off_bal() {
		return r179_exposure_class_off_bal;
	}
	public void setR179_exposure_class_off_bal(String r179_exposure_class_off_bal) {
		this.r179_exposure_class_off_bal = r179_exposure_class_off_bal;
	}
	public BigDecimal getR179_nom_pri_amt() {
		return r179_nom_pri_amt;
	}
	public void setR179_nom_pri_amt(BigDecimal r179_nom_pri_amt) {
		this.r179_nom_pri_amt = r179_nom_pri_amt;
	}
	public BigDecimal getR179_ccf() {
		return r179_ccf;
	}
	public void setR179_ccf(BigDecimal r179_ccf) {
		this.r179_ccf = r179_ccf;
	}
	public BigDecimal getR179_cea() {
		return r179_cea;
	}
	public void setR179_cea(BigDecimal r179_cea) {
		this.r179_cea = r179_cea;
	}
	public BigDecimal getR179_cea_elig_coun_bilt_net() {
		return r179_cea_elig_coun_bilt_net;
	}
	public void setR179_cea_elig_coun_bilt_net(BigDecimal r179_cea_elig_coun_bilt_net) {
		this.r179_cea_elig_coun_bilt_net = r179_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR179_cea_aft_net() {
		return r179_cea_aft_net;
	}
	public void setR179_cea_aft_net(BigDecimal r179_cea_aft_net) {
		this.r179_cea_aft_net = r179_cea_aft_net;
	}
	public BigDecimal getR179_crm_sub_app_cea_elig() {
		return r179_crm_sub_app_cea_elig;
	}
	public void setR179_crm_sub_app_cea_elig(BigDecimal r179_crm_sub_app_cea_elig) {
		this.r179_crm_sub_app_cea_elig = r179_crm_sub_app_cea_elig;
	}
	public BigDecimal getR179_crm_sub_app_non_col_guar_elig() {
		return r179_crm_sub_app_non_col_guar_elig;
	}
	public void setR179_crm_sub_app_non_col_guar_elig(BigDecimal r179_crm_sub_app_non_col_guar_elig) {
		this.r179_crm_sub_app_non_col_guar_elig = r179_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR179_crm_sub_app_non_col_cre_der() {
		return r179_crm_sub_app_non_col_cre_der;
	}
	public void setR179_crm_sub_app_non_col_cre_der(BigDecimal r179_crm_sub_app_non_col_cre_der) {
		this.r179_crm_sub_app_non_col_cre_der = r179_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR179_crm_sub_app_col_elig_cash() {
		return r179_crm_sub_app_col_elig_cash;
	}
	public void setR179_crm_sub_app_col_elig_cash(BigDecimal r179_crm_sub_app_col_elig_cash) {
		this.r179_crm_sub_app_col_elig_cash = r179_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR179_crm_sub_app_col_elig_tre_bills() {
		return r179_crm_sub_app_col_elig_tre_bills;
	}
	public void setR179_crm_sub_app_col_elig_tre_bills(BigDecimal r179_crm_sub_app_col_elig_tre_bills) {
		this.r179_crm_sub_app_col_elig_tre_bills = r179_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR179_crm_sub_app_col_elig_deb_sec() {
		return r179_crm_sub_app_col_elig_deb_sec;
	}
	public void setR179_crm_sub_app_col_elig_deb_sec(BigDecimal r179_crm_sub_app_col_elig_deb_sec) {
		this.r179_crm_sub_app_col_elig_deb_sec = r179_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR179_crm_sub_app_col_elig_euiq() {
		return r179_crm_sub_app_col_elig_euiq;
	}
	public void setR179_crm_sub_app_col_elig_euiq(BigDecimal r179_crm_sub_app_col_elig_euiq) {
		this.r179_crm_sub_app_col_elig_euiq = r179_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR179_crm_sub_app_col_elig_uni_tru() {
		return r179_crm_sub_app_col_elig_uni_tru;
	}
	public void setR179_crm_sub_app_col_elig_uni_tru(BigDecimal r179_crm_sub_app_col_elig_uni_tru) {
		this.r179_crm_sub_app_col_elig_uni_tru = r179_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR179_crm_sub_app_col_cea_cov() {
		return r179_crm_sub_app_col_cea_cov;
	}
	public void setR179_crm_sub_app_col_cea_cov(BigDecimal r179_crm_sub_app_col_cea_cov) {
		this.r179_crm_sub_app_col_cea_cov = r179_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR179_crm_sub_app_col_cea_not_cov() {
		return r179_crm_sub_app_col_cea_not_cov;
	}
	public void setR179_crm_sub_app_col_cea_not_cov(BigDecimal r179_crm_sub_app_col_cea_not_cov) {
		this.r179_crm_sub_app_col_cea_not_cov = r179_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR179_crm_sub_app_rwa_ris_wei_crm() {
		return r179_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR179_crm_sub_app_rwa_ris_wei_crm(BigDecimal r179_crm_sub_app_rwa_ris_wei_crm) {
		this.r179_crm_sub_app_rwa_ris_wei_crm = r179_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR179_crm_sub_app_rwa_ris_cea_cov() {
		return r179_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR179_crm_sub_app_rwa_ris_cea_cov(BigDecimal r179_crm_sub_app_rwa_ris_cea_cov) {
		this.r179_crm_sub_app_rwa_ris_cea_cov = r179_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR179_crm_sub_app_rwa_appl_org_coun() {
		return r179_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR179_crm_sub_app_rwa_appl_org_coun(BigDecimal r179_crm_sub_app_rwa_appl_org_coun) {
		this.r179_crm_sub_app_rwa_appl_org_coun = r179_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR179_crm_sub_app_rwa_ris_cea_not_cov() {
		return r179_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR179_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r179_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r179_crm_sub_app_rwa_ris_cea_not_cov = r179_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR179_crm_com_app_col_cea_elig_crm() {
		return r179_crm_com_app_col_cea_elig_crm;
	}
	public void setR179_crm_com_app_col_cea_elig_crm(BigDecimal r179_crm_com_app_col_cea_elig_crm) {
		this.r179_crm_com_app_col_cea_elig_crm = r179_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR179_crm_com_app_col_elig_cea_vol_adj() {
		return r179_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR179_crm_com_app_col_elig_cea_vol_adj(BigDecimal r179_crm_com_app_col_elig_cea_vol_adj) {
		this.r179_crm_com_app_col_elig_cea_vol_adj = r179_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR179_crm_com_app_col_elig_fin_hai() {
		return r179_crm_com_app_col_elig_fin_hai;
	}
	public void setR179_crm_com_app_col_elig_fin_hai(BigDecimal r179_crm_com_app_col_elig_fin_hai) {
		this.r179_crm_com_app_col_elig_fin_hai = r179_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR179_crm_com_app_col_cea_val_aft_crm() {
		return r179_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR179_crm_com_app_col_cea_val_aft_crm(BigDecimal r179_crm_com_app_col_cea_val_aft_crm) {
		this.r179_crm_com_app_col_cea_val_aft_crm = r179_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR179_rwa_elig_cea_not_cov() {
		return r179_rwa_elig_cea_not_cov;
	}
	public void setR179_rwa_elig_cea_not_cov(BigDecimal r179_rwa_elig_cea_not_cov) {
		this.r179_rwa_elig_cea_not_cov = r179_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR179_rwa_unsec_cea_sub_cre_ris() {
		return r179_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR179_rwa_unsec_cea_sub_cre_ris(BigDecimal r179_rwa_unsec_cea_sub_cre_ris) {
		this.r179_rwa_unsec_cea_sub_cre_ris = r179_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR179_rwa_unsec_cea() {
		return r179_rwa_unsec_cea;
	}
	public void setR179_rwa_unsec_cea(BigDecimal r179_rwa_unsec_cea) {
		this.r179_rwa_unsec_cea = r179_rwa_unsec_cea;
	}
	public BigDecimal getR179_rwa_tot_ris_wei_ass() {
		return r179_rwa_tot_ris_wei_ass;
	}
	public void setR179_rwa_tot_ris_wei_ass(BigDecimal r179_rwa_tot_ris_wei_ass) {
		this.r179_rwa_tot_ris_wei_ass = r179_rwa_tot_ris_wei_ass;
	}
	public String getR180_exposure_class_off_bal() {
		return r180_exposure_class_off_bal;
	}
	public void setR180_exposure_class_off_bal(String r180_exposure_class_off_bal) {
		this.r180_exposure_class_off_bal = r180_exposure_class_off_bal;
	}
	public BigDecimal getR180_nom_pri_amt() {
		return r180_nom_pri_amt;
	}
	public void setR180_nom_pri_amt(BigDecimal r180_nom_pri_amt) {
		this.r180_nom_pri_amt = r180_nom_pri_amt;
	}
	public BigDecimal getR180_ccf() {
		return r180_ccf;
	}
	public void setR180_ccf(BigDecimal r180_ccf) {
		this.r180_ccf = r180_ccf;
	}
	public BigDecimal getR180_cea() {
		return r180_cea;
	}
	public void setR180_cea(BigDecimal r180_cea) {
		this.r180_cea = r180_cea;
	}
	public BigDecimal getR180_cea_elig_coun_bilt_net() {
		return r180_cea_elig_coun_bilt_net;
	}
	public void setR180_cea_elig_coun_bilt_net(BigDecimal r180_cea_elig_coun_bilt_net) {
		this.r180_cea_elig_coun_bilt_net = r180_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR180_cea_aft_net() {
		return r180_cea_aft_net;
	}
	public void setR180_cea_aft_net(BigDecimal r180_cea_aft_net) {
		this.r180_cea_aft_net = r180_cea_aft_net;
	}
	public BigDecimal getR180_crm_sub_app_cea_elig() {
		return r180_crm_sub_app_cea_elig;
	}
	public void setR180_crm_sub_app_cea_elig(BigDecimal r180_crm_sub_app_cea_elig) {
		this.r180_crm_sub_app_cea_elig = r180_crm_sub_app_cea_elig;
	}
	public BigDecimal getR180_crm_sub_app_non_col_guar_elig() {
		return r180_crm_sub_app_non_col_guar_elig;
	}
	public void setR180_crm_sub_app_non_col_guar_elig(BigDecimal r180_crm_sub_app_non_col_guar_elig) {
		this.r180_crm_sub_app_non_col_guar_elig = r180_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR180_crm_sub_app_non_col_cre_der() {
		return r180_crm_sub_app_non_col_cre_der;
	}
	public void setR180_crm_sub_app_non_col_cre_der(BigDecimal r180_crm_sub_app_non_col_cre_der) {
		this.r180_crm_sub_app_non_col_cre_der = r180_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR180_crm_sub_app_col_elig_cash() {
		return r180_crm_sub_app_col_elig_cash;
	}
	public void setR180_crm_sub_app_col_elig_cash(BigDecimal r180_crm_sub_app_col_elig_cash) {
		this.r180_crm_sub_app_col_elig_cash = r180_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR180_crm_sub_app_col_elig_tre_bills() {
		return r180_crm_sub_app_col_elig_tre_bills;
	}
	public void setR180_crm_sub_app_col_elig_tre_bills(BigDecimal r180_crm_sub_app_col_elig_tre_bills) {
		this.r180_crm_sub_app_col_elig_tre_bills = r180_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR180_crm_sub_app_col_elig_deb_sec() {
		return r180_crm_sub_app_col_elig_deb_sec;
	}
	public void setR180_crm_sub_app_col_elig_deb_sec(BigDecimal r180_crm_sub_app_col_elig_deb_sec) {
		this.r180_crm_sub_app_col_elig_deb_sec = r180_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR180_crm_sub_app_col_elig_euiq() {
		return r180_crm_sub_app_col_elig_euiq;
	}
	public void setR180_crm_sub_app_col_elig_euiq(BigDecimal r180_crm_sub_app_col_elig_euiq) {
		this.r180_crm_sub_app_col_elig_euiq = r180_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR180_crm_sub_app_col_elig_uni_tru() {
		return r180_crm_sub_app_col_elig_uni_tru;
	}
	public void setR180_crm_sub_app_col_elig_uni_tru(BigDecimal r180_crm_sub_app_col_elig_uni_tru) {
		this.r180_crm_sub_app_col_elig_uni_tru = r180_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR180_crm_sub_app_col_cea_cov() {
		return r180_crm_sub_app_col_cea_cov;
	}
	public void setR180_crm_sub_app_col_cea_cov(BigDecimal r180_crm_sub_app_col_cea_cov) {
		this.r180_crm_sub_app_col_cea_cov = r180_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR180_crm_sub_app_col_cea_not_cov() {
		return r180_crm_sub_app_col_cea_not_cov;
	}
	public void setR180_crm_sub_app_col_cea_not_cov(BigDecimal r180_crm_sub_app_col_cea_not_cov) {
		this.r180_crm_sub_app_col_cea_not_cov = r180_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR180_crm_sub_app_rwa_ris_wei_crm() {
		return r180_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR180_crm_sub_app_rwa_ris_wei_crm(BigDecimal r180_crm_sub_app_rwa_ris_wei_crm) {
		this.r180_crm_sub_app_rwa_ris_wei_crm = r180_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR180_crm_sub_app_rwa_ris_cea_cov() {
		return r180_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR180_crm_sub_app_rwa_ris_cea_cov(BigDecimal r180_crm_sub_app_rwa_ris_cea_cov) {
		this.r180_crm_sub_app_rwa_ris_cea_cov = r180_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR180_crm_sub_app_rwa_appl_org_coun() {
		return r180_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR180_crm_sub_app_rwa_appl_org_coun(BigDecimal r180_crm_sub_app_rwa_appl_org_coun) {
		this.r180_crm_sub_app_rwa_appl_org_coun = r180_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR180_crm_sub_app_rwa_ris_cea_not_cov() {
		return r180_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR180_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r180_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r180_crm_sub_app_rwa_ris_cea_not_cov = r180_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR180_crm_com_app_col_cea_elig_crm() {
		return r180_crm_com_app_col_cea_elig_crm;
	}
	public void setR180_crm_com_app_col_cea_elig_crm(BigDecimal r180_crm_com_app_col_cea_elig_crm) {
		this.r180_crm_com_app_col_cea_elig_crm = r180_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR180_crm_com_app_col_elig_cea_vol_adj() {
		return r180_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR180_crm_com_app_col_elig_cea_vol_adj(BigDecimal r180_crm_com_app_col_elig_cea_vol_adj) {
		this.r180_crm_com_app_col_elig_cea_vol_adj = r180_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR180_crm_com_app_col_elig_fin_hai() {
		return r180_crm_com_app_col_elig_fin_hai;
	}
	public void setR180_crm_com_app_col_elig_fin_hai(BigDecimal r180_crm_com_app_col_elig_fin_hai) {
		this.r180_crm_com_app_col_elig_fin_hai = r180_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR180_crm_com_app_col_cea_val_aft_crm() {
		return r180_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR180_crm_com_app_col_cea_val_aft_crm(BigDecimal r180_crm_com_app_col_cea_val_aft_crm) {
		this.r180_crm_com_app_col_cea_val_aft_crm = r180_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR180_rwa_elig_cea_not_cov() {
		return r180_rwa_elig_cea_not_cov;
	}
	public void setR180_rwa_elig_cea_not_cov(BigDecimal r180_rwa_elig_cea_not_cov) {
		this.r180_rwa_elig_cea_not_cov = r180_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR180_rwa_unsec_cea_sub_cre_ris() {
		return r180_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR180_rwa_unsec_cea_sub_cre_ris(BigDecimal r180_rwa_unsec_cea_sub_cre_ris) {
		this.r180_rwa_unsec_cea_sub_cre_ris = r180_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR180_rwa_unsec_cea() {
		return r180_rwa_unsec_cea;
	}
	public void setR180_rwa_unsec_cea(BigDecimal r180_rwa_unsec_cea) {
		this.r180_rwa_unsec_cea = r180_rwa_unsec_cea;
	}
	public BigDecimal getR180_rwa_tot_ris_wei_ass() {
		return r180_rwa_tot_ris_wei_ass;
	}
	public void setR180_rwa_tot_ris_wei_ass(BigDecimal r180_rwa_tot_ris_wei_ass) {
		this.r180_rwa_tot_ris_wei_ass = r180_rwa_tot_ris_wei_ass;
	}
	public String getR181_exposure_class_off_bal() {
		return r181_exposure_class_off_bal;
	}
	public void setR181_exposure_class_off_bal(String r181_exposure_class_off_bal) {
		this.r181_exposure_class_off_bal = r181_exposure_class_off_bal;
	}
	public BigDecimal getR181_nom_pri_amt() {
		return r181_nom_pri_amt;
	}
	public void setR181_nom_pri_amt(BigDecimal r181_nom_pri_amt) {
		this.r181_nom_pri_amt = r181_nom_pri_amt;
	}
	public BigDecimal getR181_ccf() {
		return r181_ccf;
	}
	public void setR181_ccf(BigDecimal r181_ccf) {
		this.r181_ccf = r181_ccf;
	}
	public BigDecimal getR181_cea() {
		return r181_cea;
	}
	public void setR181_cea(BigDecimal r181_cea) {
		this.r181_cea = r181_cea;
	}
	public BigDecimal getR181_cea_elig_coun_bilt_net() {
		return r181_cea_elig_coun_bilt_net;
	}
	public void setR181_cea_elig_coun_bilt_net(BigDecimal r181_cea_elig_coun_bilt_net) {
		this.r181_cea_elig_coun_bilt_net = r181_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR181_cea_aft_net() {
		return r181_cea_aft_net;
	}
	public void setR181_cea_aft_net(BigDecimal r181_cea_aft_net) {
		this.r181_cea_aft_net = r181_cea_aft_net;
	}
	public BigDecimal getR181_crm_sub_app_cea_elig() {
		return r181_crm_sub_app_cea_elig;
	}
	public void setR181_crm_sub_app_cea_elig(BigDecimal r181_crm_sub_app_cea_elig) {
		this.r181_crm_sub_app_cea_elig = r181_crm_sub_app_cea_elig;
	}
	public BigDecimal getR181_crm_sub_app_non_col_guar_elig() {
		return r181_crm_sub_app_non_col_guar_elig;
	}
	public void setR181_crm_sub_app_non_col_guar_elig(BigDecimal r181_crm_sub_app_non_col_guar_elig) {
		this.r181_crm_sub_app_non_col_guar_elig = r181_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR181_crm_sub_app_non_col_cre_der() {
		return r181_crm_sub_app_non_col_cre_der;
	}
	public void setR181_crm_sub_app_non_col_cre_der(BigDecimal r181_crm_sub_app_non_col_cre_der) {
		this.r181_crm_sub_app_non_col_cre_der = r181_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR181_crm_sub_app_col_elig_cash() {
		return r181_crm_sub_app_col_elig_cash;
	}
	public void setR181_crm_sub_app_col_elig_cash(BigDecimal r181_crm_sub_app_col_elig_cash) {
		this.r181_crm_sub_app_col_elig_cash = r181_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR181_crm_sub_app_col_elig_tre_bills() {
		return r181_crm_sub_app_col_elig_tre_bills;
	}
	public void setR181_crm_sub_app_col_elig_tre_bills(BigDecimal r181_crm_sub_app_col_elig_tre_bills) {
		this.r181_crm_sub_app_col_elig_tre_bills = r181_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR181_crm_sub_app_col_elig_deb_sec() {
		return r181_crm_sub_app_col_elig_deb_sec;
	}
	public void setR181_crm_sub_app_col_elig_deb_sec(BigDecimal r181_crm_sub_app_col_elig_deb_sec) {
		this.r181_crm_sub_app_col_elig_deb_sec = r181_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR181_crm_sub_app_col_elig_euiq() {
		return r181_crm_sub_app_col_elig_euiq;
	}
	public void setR181_crm_sub_app_col_elig_euiq(BigDecimal r181_crm_sub_app_col_elig_euiq) {
		this.r181_crm_sub_app_col_elig_euiq = r181_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR181_crm_sub_app_col_elig_uni_tru() {
		return r181_crm_sub_app_col_elig_uni_tru;
	}
	public void setR181_crm_sub_app_col_elig_uni_tru(BigDecimal r181_crm_sub_app_col_elig_uni_tru) {
		this.r181_crm_sub_app_col_elig_uni_tru = r181_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR181_crm_sub_app_col_cea_cov() {
		return r181_crm_sub_app_col_cea_cov;
	}
	public void setR181_crm_sub_app_col_cea_cov(BigDecimal r181_crm_sub_app_col_cea_cov) {
		this.r181_crm_sub_app_col_cea_cov = r181_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR181_crm_sub_app_col_cea_not_cov() {
		return r181_crm_sub_app_col_cea_not_cov;
	}
	public void setR181_crm_sub_app_col_cea_not_cov(BigDecimal r181_crm_sub_app_col_cea_not_cov) {
		this.r181_crm_sub_app_col_cea_not_cov = r181_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR181_crm_sub_app_rwa_ris_wei_crm() {
		return r181_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR181_crm_sub_app_rwa_ris_wei_crm(BigDecimal r181_crm_sub_app_rwa_ris_wei_crm) {
		this.r181_crm_sub_app_rwa_ris_wei_crm = r181_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR181_crm_sub_app_rwa_ris_cea_cov() {
		return r181_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR181_crm_sub_app_rwa_ris_cea_cov(BigDecimal r181_crm_sub_app_rwa_ris_cea_cov) {
		this.r181_crm_sub_app_rwa_ris_cea_cov = r181_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR181_crm_sub_app_rwa_appl_org_coun() {
		return r181_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR181_crm_sub_app_rwa_appl_org_coun(BigDecimal r181_crm_sub_app_rwa_appl_org_coun) {
		this.r181_crm_sub_app_rwa_appl_org_coun = r181_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR181_crm_sub_app_rwa_ris_cea_not_cov() {
		return r181_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR181_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r181_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r181_crm_sub_app_rwa_ris_cea_not_cov = r181_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR181_crm_com_app_col_cea_elig_crm() {
		return r181_crm_com_app_col_cea_elig_crm;
	}
	public void setR181_crm_com_app_col_cea_elig_crm(BigDecimal r181_crm_com_app_col_cea_elig_crm) {
		this.r181_crm_com_app_col_cea_elig_crm = r181_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR181_crm_com_app_col_elig_cea_vol_adj() {
		return r181_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR181_crm_com_app_col_elig_cea_vol_adj(BigDecimal r181_crm_com_app_col_elig_cea_vol_adj) {
		this.r181_crm_com_app_col_elig_cea_vol_adj = r181_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR181_crm_com_app_col_elig_fin_hai() {
		return r181_crm_com_app_col_elig_fin_hai;
	}
	public void setR181_crm_com_app_col_elig_fin_hai(BigDecimal r181_crm_com_app_col_elig_fin_hai) {
		this.r181_crm_com_app_col_elig_fin_hai = r181_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR181_crm_com_app_col_cea_val_aft_crm() {
		return r181_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR181_crm_com_app_col_cea_val_aft_crm(BigDecimal r181_crm_com_app_col_cea_val_aft_crm) {
		this.r181_crm_com_app_col_cea_val_aft_crm = r181_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR181_rwa_elig_cea_not_cov() {
		return r181_rwa_elig_cea_not_cov;
	}
	public void setR181_rwa_elig_cea_not_cov(BigDecimal r181_rwa_elig_cea_not_cov) {
		this.r181_rwa_elig_cea_not_cov = r181_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR181_rwa_unsec_cea_sub_cre_ris() {
		return r181_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR181_rwa_unsec_cea_sub_cre_ris(BigDecimal r181_rwa_unsec_cea_sub_cre_ris) {
		this.r181_rwa_unsec_cea_sub_cre_ris = r181_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR181_rwa_unsec_cea() {
		return r181_rwa_unsec_cea;
	}
	public void setR181_rwa_unsec_cea(BigDecimal r181_rwa_unsec_cea) {
		this.r181_rwa_unsec_cea = r181_rwa_unsec_cea;
	}
	public BigDecimal getR181_rwa_tot_ris_wei_ass() {
		return r181_rwa_tot_ris_wei_ass;
	}
	public void setR181_rwa_tot_ris_wei_ass(BigDecimal r181_rwa_tot_ris_wei_ass) {
		this.r181_rwa_tot_ris_wei_ass = r181_rwa_tot_ris_wei_ass;
	}
	public String getR182_exposure_class_off_bal() {
		return r182_exposure_class_off_bal;
	}
	public void setR182_exposure_class_off_bal(String r182_exposure_class_off_bal) {
		this.r182_exposure_class_off_bal = r182_exposure_class_off_bal;
	}
	public BigDecimal getR182_nom_pri_amt() {
		return r182_nom_pri_amt;
	}
	public void setR182_nom_pri_amt(BigDecimal r182_nom_pri_amt) {
		this.r182_nom_pri_amt = r182_nom_pri_amt;
	}
	public BigDecimal getR182_ccf() {
		return r182_ccf;
	}
	public void setR182_ccf(BigDecimal r182_ccf) {
		this.r182_ccf = r182_ccf;
	}
	public BigDecimal getR182_cea() {
		return r182_cea;
	}
	public void setR182_cea(BigDecimal r182_cea) {
		this.r182_cea = r182_cea;
	}
	public BigDecimal getR182_cea_elig_coun_bilt_net() {
		return r182_cea_elig_coun_bilt_net;
	}
	public void setR182_cea_elig_coun_bilt_net(BigDecimal r182_cea_elig_coun_bilt_net) {
		this.r182_cea_elig_coun_bilt_net = r182_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR182_cea_aft_net() {
		return r182_cea_aft_net;
	}
	public void setR182_cea_aft_net(BigDecimal r182_cea_aft_net) {
		this.r182_cea_aft_net = r182_cea_aft_net;
	}
	public BigDecimal getR182_crm_sub_app_cea_elig() {
		return r182_crm_sub_app_cea_elig;
	}
	public void setR182_crm_sub_app_cea_elig(BigDecimal r182_crm_sub_app_cea_elig) {
		this.r182_crm_sub_app_cea_elig = r182_crm_sub_app_cea_elig;
	}
	public BigDecimal getR182_crm_sub_app_non_col_guar_elig() {
		return r182_crm_sub_app_non_col_guar_elig;
	}
	public void setR182_crm_sub_app_non_col_guar_elig(BigDecimal r182_crm_sub_app_non_col_guar_elig) {
		this.r182_crm_sub_app_non_col_guar_elig = r182_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR182_crm_sub_app_non_col_cre_der() {
		return r182_crm_sub_app_non_col_cre_der;
	}
	public void setR182_crm_sub_app_non_col_cre_der(BigDecimal r182_crm_sub_app_non_col_cre_der) {
		this.r182_crm_sub_app_non_col_cre_der = r182_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR182_crm_sub_app_col_elig_cash() {
		return r182_crm_sub_app_col_elig_cash;
	}
	public void setR182_crm_sub_app_col_elig_cash(BigDecimal r182_crm_sub_app_col_elig_cash) {
		this.r182_crm_sub_app_col_elig_cash = r182_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR182_crm_sub_app_col_elig_tre_bills() {
		return r182_crm_sub_app_col_elig_tre_bills;
	}
	public void setR182_crm_sub_app_col_elig_tre_bills(BigDecimal r182_crm_sub_app_col_elig_tre_bills) {
		this.r182_crm_sub_app_col_elig_tre_bills = r182_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR182_crm_sub_app_col_elig_deb_sec() {
		return r182_crm_sub_app_col_elig_deb_sec;
	}
	public void setR182_crm_sub_app_col_elig_deb_sec(BigDecimal r182_crm_sub_app_col_elig_deb_sec) {
		this.r182_crm_sub_app_col_elig_deb_sec = r182_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR182_crm_sub_app_col_elig_euiq() {
		return r182_crm_sub_app_col_elig_euiq;
	}
	public void setR182_crm_sub_app_col_elig_euiq(BigDecimal r182_crm_sub_app_col_elig_euiq) {
		this.r182_crm_sub_app_col_elig_euiq = r182_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR182_crm_sub_app_col_elig_uni_tru() {
		return r182_crm_sub_app_col_elig_uni_tru;
	}
	public void setR182_crm_sub_app_col_elig_uni_tru(BigDecimal r182_crm_sub_app_col_elig_uni_tru) {
		this.r182_crm_sub_app_col_elig_uni_tru = r182_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR182_crm_sub_app_col_cea_cov() {
		return r182_crm_sub_app_col_cea_cov;
	}
	public void setR182_crm_sub_app_col_cea_cov(BigDecimal r182_crm_sub_app_col_cea_cov) {
		this.r182_crm_sub_app_col_cea_cov = r182_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR182_crm_sub_app_col_cea_not_cov() {
		return r182_crm_sub_app_col_cea_not_cov;
	}
	public void setR182_crm_sub_app_col_cea_not_cov(BigDecimal r182_crm_sub_app_col_cea_not_cov) {
		this.r182_crm_sub_app_col_cea_not_cov = r182_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR182_crm_sub_app_rwa_ris_wei_crm() {
		return r182_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR182_crm_sub_app_rwa_ris_wei_crm(BigDecimal r182_crm_sub_app_rwa_ris_wei_crm) {
		this.r182_crm_sub_app_rwa_ris_wei_crm = r182_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR182_crm_sub_app_rwa_ris_cea_cov() {
		return r182_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR182_crm_sub_app_rwa_ris_cea_cov(BigDecimal r182_crm_sub_app_rwa_ris_cea_cov) {
		this.r182_crm_sub_app_rwa_ris_cea_cov = r182_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR182_crm_sub_app_rwa_appl_org_coun() {
		return r182_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR182_crm_sub_app_rwa_appl_org_coun(BigDecimal r182_crm_sub_app_rwa_appl_org_coun) {
		this.r182_crm_sub_app_rwa_appl_org_coun = r182_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR182_crm_sub_app_rwa_ris_cea_not_cov() {
		return r182_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR182_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r182_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r182_crm_sub_app_rwa_ris_cea_not_cov = r182_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR182_crm_com_app_col_cea_elig_crm() {
		return r182_crm_com_app_col_cea_elig_crm;
	}
	public void setR182_crm_com_app_col_cea_elig_crm(BigDecimal r182_crm_com_app_col_cea_elig_crm) {
		this.r182_crm_com_app_col_cea_elig_crm = r182_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR182_crm_com_app_col_elig_cea_vol_adj() {
		return r182_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR182_crm_com_app_col_elig_cea_vol_adj(BigDecimal r182_crm_com_app_col_elig_cea_vol_adj) {
		this.r182_crm_com_app_col_elig_cea_vol_adj = r182_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR182_crm_com_app_col_elig_fin_hai() {
		return r182_crm_com_app_col_elig_fin_hai;
	}
	public void setR182_crm_com_app_col_elig_fin_hai(BigDecimal r182_crm_com_app_col_elig_fin_hai) {
		this.r182_crm_com_app_col_elig_fin_hai = r182_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR182_crm_com_app_col_cea_val_aft_crm() {
		return r182_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR182_crm_com_app_col_cea_val_aft_crm(BigDecimal r182_crm_com_app_col_cea_val_aft_crm) {
		this.r182_crm_com_app_col_cea_val_aft_crm = r182_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR182_rwa_elig_cea_not_cov() {
		return r182_rwa_elig_cea_not_cov;
	}
	public void setR182_rwa_elig_cea_not_cov(BigDecimal r182_rwa_elig_cea_not_cov) {
		this.r182_rwa_elig_cea_not_cov = r182_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR182_rwa_unsec_cea_sub_cre_ris() {
		return r182_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR182_rwa_unsec_cea_sub_cre_ris(BigDecimal r182_rwa_unsec_cea_sub_cre_ris) {
		this.r182_rwa_unsec_cea_sub_cre_ris = r182_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR182_rwa_unsec_cea() {
		return r182_rwa_unsec_cea;
	}
	public void setR182_rwa_unsec_cea(BigDecimal r182_rwa_unsec_cea) {
		this.r182_rwa_unsec_cea = r182_rwa_unsec_cea;
	}
	public BigDecimal getR182_rwa_tot_ris_wei_ass() {
		return r182_rwa_tot_ris_wei_ass;
	}
	public void setR182_rwa_tot_ris_wei_ass(BigDecimal r182_rwa_tot_ris_wei_ass) {
		this.r182_rwa_tot_ris_wei_ass = r182_rwa_tot_ris_wei_ass;
	}
	public String getR183_exposure_class_off_bal() {
		return r183_exposure_class_off_bal;
	}
	public void setR183_exposure_class_off_bal(String r183_exposure_class_off_bal) {
		this.r183_exposure_class_off_bal = r183_exposure_class_off_bal;
	}
	public BigDecimal getR183_nom_pri_amt() {
		return r183_nom_pri_amt;
	}
	public void setR183_nom_pri_amt(BigDecimal r183_nom_pri_amt) {
		this.r183_nom_pri_amt = r183_nom_pri_amt;
	}
	public BigDecimal getR183_ccf() {
		return r183_ccf;
	}
	public void setR183_ccf(BigDecimal r183_ccf) {
		this.r183_ccf = r183_ccf;
	}
	public BigDecimal getR183_cea() {
		return r183_cea;
	}
	public void setR183_cea(BigDecimal r183_cea) {
		this.r183_cea = r183_cea;
	}
	public BigDecimal getR183_cea_elig_coun_bilt_net() {
		return r183_cea_elig_coun_bilt_net;
	}
	public void setR183_cea_elig_coun_bilt_net(BigDecimal r183_cea_elig_coun_bilt_net) {
		this.r183_cea_elig_coun_bilt_net = r183_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR183_cea_aft_net() {
		return r183_cea_aft_net;
	}
	public void setR183_cea_aft_net(BigDecimal r183_cea_aft_net) {
		this.r183_cea_aft_net = r183_cea_aft_net;
	}
	public BigDecimal getR183_crm_sub_app_cea_elig() {
		return r183_crm_sub_app_cea_elig;
	}
	public void setR183_crm_sub_app_cea_elig(BigDecimal r183_crm_sub_app_cea_elig) {
		this.r183_crm_sub_app_cea_elig = r183_crm_sub_app_cea_elig;
	}
	public BigDecimal getR183_crm_sub_app_non_col_guar_elig() {
		return r183_crm_sub_app_non_col_guar_elig;
	}
	public void setR183_crm_sub_app_non_col_guar_elig(BigDecimal r183_crm_sub_app_non_col_guar_elig) {
		this.r183_crm_sub_app_non_col_guar_elig = r183_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR183_crm_sub_app_non_col_cre_der() {
		return r183_crm_sub_app_non_col_cre_der;
	}
	public void setR183_crm_sub_app_non_col_cre_der(BigDecimal r183_crm_sub_app_non_col_cre_der) {
		this.r183_crm_sub_app_non_col_cre_der = r183_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR183_crm_sub_app_col_elig_cash() {
		return r183_crm_sub_app_col_elig_cash;
	}
	public void setR183_crm_sub_app_col_elig_cash(BigDecimal r183_crm_sub_app_col_elig_cash) {
		this.r183_crm_sub_app_col_elig_cash = r183_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR183_crm_sub_app_col_elig_tre_bills() {
		return r183_crm_sub_app_col_elig_tre_bills;
	}
	public void setR183_crm_sub_app_col_elig_tre_bills(BigDecimal r183_crm_sub_app_col_elig_tre_bills) {
		this.r183_crm_sub_app_col_elig_tre_bills = r183_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR183_crm_sub_app_col_elig_deb_sec() {
		return r183_crm_sub_app_col_elig_deb_sec;
	}
	public void setR183_crm_sub_app_col_elig_deb_sec(BigDecimal r183_crm_sub_app_col_elig_deb_sec) {
		this.r183_crm_sub_app_col_elig_deb_sec = r183_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR183_crm_sub_app_col_elig_euiq() {
		return r183_crm_sub_app_col_elig_euiq;
	}
	public void setR183_crm_sub_app_col_elig_euiq(BigDecimal r183_crm_sub_app_col_elig_euiq) {
		this.r183_crm_sub_app_col_elig_euiq = r183_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR183_crm_sub_app_col_elig_uni_tru() {
		return r183_crm_sub_app_col_elig_uni_tru;
	}
	public void setR183_crm_sub_app_col_elig_uni_tru(BigDecimal r183_crm_sub_app_col_elig_uni_tru) {
		this.r183_crm_sub_app_col_elig_uni_tru = r183_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR183_crm_sub_app_col_cea_cov() {
		return r183_crm_sub_app_col_cea_cov;
	}
	public void setR183_crm_sub_app_col_cea_cov(BigDecimal r183_crm_sub_app_col_cea_cov) {
		this.r183_crm_sub_app_col_cea_cov = r183_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR183_crm_sub_app_col_cea_not_cov() {
		return r183_crm_sub_app_col_cea_not_cov;
	}
	public void setR183_crm_sub_app_col_cea_not_cov(BigDecimal r183_crm_sub_app_col_cea_not_cov) {
		this.r183_crm_sub_app_col_cea_not_cov = r183_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR183_crm_sub_app_rwa_ris_wei_crm() {
		return r183_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR183_crm_sub_app_rwa_ris_wei_crm(BigDecimal r183_crm_sub_app_rwa_ris_wei_crm) {
		this.r183_crm_sub_app_rwa_ris_wei_crm = r183_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR183_crm_sub_app_rwa_ris_cea_cov() {
		return r183_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR183_crm_sub_app_rwa_ris_cea_cov(BigDecimal r183_crm_sub_app_rwa_ris_cea_cov) {
		this.r183_crm_sub_app_rwa_ris_cea_cov = r183_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR183_crm_sub_app_rwa_appl_org_coun() {
		return r183_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR183_crm_sub_app_rwa_appl_org_coun(BigDecimal r183_crm_sub_app_rwa_appl_org_coun) {
		this.r183_crm_sub_app_rwa_appl_org_coun = r183_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR183_crm_sub_app_rwa_ris_cea_not_cov() {
		return r183_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR183_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r183_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r183_crm_sub_app_rwa_ris_cea_not_cov = r183_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR183_crm_com_app_col_cea_elig_crm() {
		return r183_crm_com_app_col_cea_elig_crm;
	}
	public void setR183_crm_com_app_col_cea_elig_crm(BigDecimal r183_crm_com_app_col_cea_elig_crm) {
		this.r183_crm_com_app_col_cea_elig_crm = r183_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR183_crm_com_app_col_elig_cea_vol_adj() {
		return r183_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR183_crm_com_app_col_elig_cea_vol_adj(BigDecimal r183_crm_com_app_col_elig_cea_vol_adj) {
		this.r183_crm_com_app_col_elig_cea_vol_adj = r183_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR183_crm_com_app_col_elig_fin_hai() {
		return r183_crm_com_app_col_elig_fin_hai;
	}
	public void setR183_crm_com_app_col_elig_fin_hai(BigDecimal r183_crm_com_app_col_elig_fin_hai) {
		this.r183_crm_com_app_col_elig_fin_hai = r183_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR183_crm_com_app_col_cea_val_aft_crm() {
		return r183_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR183_crm_com_app_col_cea_val_aft_crm(BigDecimal r183_crm_com_app_col_cea_val_aft_crm) {
		this.r183_crm_com_app_col_cea_val_aft_crm = r183_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR183_rwa_elig_cea_not_cov() {
		return r183_rwa_elig_cea_not_cov;
	}
	public void setR183_rwa_elig_cea_not_cov(BigDecimal r183_rwa_elig_cea_not_cov) {
		this.r183_rwa_elig_cea_not_cov = r183_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR183_rwa_unsec_cea_sub_cre_ris() {
		return r183_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR183_rwa_unsec_cea_sub_cre_ris(BigDecimal r183_rwa_unsec_cea_sub_cre_ris) {
		this.r183_rwa_unsec_cea_sub_cre_ris = r183_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR183_rwa_unsec_cea() {
		return r183_rwa_unsec_cea;
	}
	public void setR183_rwa_unsec_cea(BigDecimal r183_rwa_unsec_cea) {
		this.r183_rwa_unsec_cea = r183_rwa_unsec_cea;
	}
	public BigDecimal getR183_rwa_tot_ris_wei_ass() {
		return r183_rwa_tot_ris_wei_ass;
	}
	public void setR183_rwa_tot_ris_wei_ass(BigDecimal r183_rwa_tot_ris_wei_ass) {
		this.r183_rwa_tot_ris_wei_ass = r183_rwa_tot_ris_wei_ass;
	}
	public String getR184_exposure_class_off_bal() {
		return r184_exposure_class_off_bal;
	}
	public void setR184_exposure_class_off_bal(String r184_exposure_class_off_bal) {
		this.r184_exposure_class_off_bal = r184_exposure_class_off_bal;
	}
	public BigDecimal getR184_nom_pri_amt() {
		return r184_nom_pri_amt;
	}
	public void setR184_nom_pri_amt(BigDecimal r184_nom_pri_amt) {
		this.r184_nom_pri_amt = r184_nom_pri_amt;
	}
	public BigDecimal getR184_ccf() {
		return r184_ccf;
	}
	public void setR184_ccf(BigDecimal r184_ccf) {
		this.r184_ccf = r184_ccf;
	}
	public BigDecimal getR184_cea() {
		return r184_cea;
	}
	public void setR184_cea(BigDecimal r184_cea) {
		this.r184_cea = r184_cea;
	}
	public BigDecimal getR184_cea_elig_coun_bilt_net() {
		return r184_cea_elig_coun_bilt_net;
	}
	public void setR184_cea_elig_coun_bilt_net(BigDecimal r184_cea_elig_coun_bilt_net) {
		this.r184_cea_elig_coun_bilt_net = r184_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR184_cea_aft_net() {
		return r184_cea_aft_net;
	}
	public void setR184_cea_aft_net(BigDecimal r184_cea_aft_net) {
		this.r184_cea_aft_net = r184_cea_aft_net;
	}
	public BigDecimal getR184_crm_sub_app_cea_elig() {
		return r184_crm_sub_app_cea_elig;
	}
	public void setR184_crm_sub_app_cea_elig(BigDecimal r184_crm_sub_app_cea_elig) {
		this.r184_crm_sub_app_cea_elig = r184_crm_sub_app_cea_elig;
	}
	public BigDecimal getR184_crm_sub_app_non_col_guar_elig() {
		return r184_crm_sub_app_non_col_guar_elig;
	}
	public void setR184_crm_sub_app_non_col_guar_elig(BigDecimal r184_crm_sub_app_non_col_guar_elig) {
		this.r184_crm_sub_app_non_col_guar_elig = r184_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR184_crm_sub_app_non_col_cre_der() {
		return r184_crm_sub_app_non_col_cre_der;
	}
	public void setR184_crm_sub_app_non_col_cre_der(BigDecimal r184_crm_sub_app_non_col_cre_der) {
		this.r184_crm_sub_app_non_col_cre_der = r184_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR184_crm_sub_app_col_elig_cash() {
		return r184_crm_sub_app_col_elig_cash;
	}
	public void setR184_crm_sub_app_col_elig_cash(BigDecimal r184_crm_sub_app_col_elig_cash) {
		this.r184_crm_sub_app_col_elig_cash = r184_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR184_crm_sub_app_col_elig_tre_bills() {
		return r184_crm_sub_app_col_elig_tre_bills;
	}
	public void setR184_crm_sub_app_col_elig_tre_bills(BigDecimal r184_crm_sub_app_col_elig_tre_bills) {
		this.r184_crm_sub_app_col_elig_tre_bills = r184_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR184_crm_sub_app_col_elig_deb_sec() {
		return r184_crm_sub_app_col_elig_deb_sec;
	}
	public void setR184_crm_sub_app_col_elig_deb_sec(BigDecimal r184_crm_sub_app_col_elig_deb_sec) {
		this.r184_crm_sub_app_col_elig_deb_sec = r184_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR184_crm_sub_app_col_elig_euiq() {
		return r184_crm_sub_app_col_elig_euiq;
	}
	public void setR184_crm_sub_app_col_elig_euiq(BigDecimal r184_crm_sub_app_col_elig_euiq) {
		this.r184_crm_sub_app_col_elig_euiq = r184_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR184_crm_sub_app_col_elig_uni_tru() {
		return r184_crm_sub_app_col_elig_uni_tru;
	}
	public void setR184_crm_sub_app_col_elig_uni_tru(BigDecimal r184_crm_sub_app_col_elig_uni_tru) {
		this.r184_crm_sub_app_col_elig_uni_tru = r184_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR184_crm_sub_app_col_cea_cov() {
		return r184_crm_sub_app_col_cea_cov;
	}
	public void setR184_crm_sub_app_col_cea_cov(BigDecimal r184_crm_sub_app_col_cea_cov) {
		this.r184_crm_sub_app_col_cea_cov = r184_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR184_crm_sub_app_col_cea_not_cov() {
		return r184_crm_sub_app_col_cea_not_cov;
	}
	public void setR184_crm_sub_app_col_cea_not_cov(BigDecimal r184_crm_sub_app_col_cea_not_cov) {
		this.r184_crm_sub_app_col_cea_not_cov = r184_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR184_crm_sub_app_rwa_ris_wei_crm() {
		return r184_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR184_crm_sub_app_rwa_ris_wei_crm(BigDecimal r184_crm_sub_app_rwa_ris_wei_crm) {
		this.r184_crm_sub_app_rwa_ris_wei_crm = r184_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR184_crm_sub_app_rwa_ris_cea_cov() {
		return r184_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR184_crm_sub_app_rwa_ris_cea_cov(BigDecimal r184_crm_sub_app_rwa_ris_cea_cov) {
		this.r184_crm_sub_app_rwa_ris_cea_cov = r184_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR184_crm_sub_app_rwa_appl_org_coun() {
		return r184_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR184_crm_sub_app_rwa_appl_org_coun(BigDecimal r184_crm_sub_app_rwa_appl_org_coun) {
		this.r184_crm_sub_app_rwa_appl_org_coun = r184_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR184_crm_sub_app_rwa_ris_cea_not_cov() {
		return r184_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR184_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r184_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r184_crm_sub_app_rwa_ris_cea_not_cov = r184_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR184_crm_com_app_col_cea_elig_crm() {
		return r184_crm_com_app_col_cea_elig_crm;
	}
	public void setR184_crm_com_app_col_cea_elig_crm(BigDecimal r184_crm_com_app_col_cea_elig_crm) {
		this.r184_crm_com_app_col_cea_elig_crm = r184_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR184_crm_com_app_col_elig_cea_vol_adj() {
		return r184_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR184_crm_com_app_col_elig_cea_vol_adj(BigDecimal r184_crm_com_app_col_elig_cea_vol_adj) {
		this.r184_crm_com_app_col_elig_cea_vol_adj = r184_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR184_crm_com_app_col_elig_fin_hai() {
		return r184_crm_com_app_col_elig_fin_hai;
	}
	public void setR184_crm_com_app_col_elig_fin_hai(BigDecimal r184_crm_com_app_col_elig_fin_hai) {
		this.r184_crm_com_app_col_elig_fin_hai = r184_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR184_crm_com_app_col_cea_val_aft_crm() {
		return r184_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR184_crm_com_app_col_cea_val_aft_crm(BigDecimal r184_crm_com_app_col_cea_val_aft_crm) {
		this.r184_crm_com_app_col_cea_val_aft_crm = r184_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR184_rwa_elig_cea_not_cov() {
		return r184_rwa_elig_cea_not_cov;
	}
	public void setR184_rwa_elig_cea_not_cov(BigDecimal r184_rwa_elig_cea_not_cov) {
		this.r184_rwa_elig_cea_not_cov = r184_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR184_rwa_unsec_cea_sub_cre_ris() {
		return r184_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR184_rwa_unsec_cea_sub_cre_ris(BigDecimal r184_rwa_unsec_cea_sub_cre_ris) {
		this.r184_rwa_unsec_cea_sub_cre_ris = r184_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR184_rwa_unsec_cea() {
		return r184_rwa_unsec_cea;
	}
	public void setR184_rwa_unsec_cea(BigDecimal r184_rwa_unsec_cea) {
		this.r184_rwa_unsec_cea = r184_rwa_unsec_cea;
	}
	public BigDecimal getR184_rwa_tot_ris_wei_ass() {
		return r184_rwa_tot_ris_wei_ass;
	}
	public void setR184_rwa_tot_ris_wei_ass(BigDecimal r184_rwa_tot_ris_wei_ass) {
		this.r184_rwa_tot_ris_wei_ass = r184_rwa_tot_ris_wei_ass;
	}
	public String getR185_exposure_class_off_bal() {
		return r185_exposure_class_off_bal;
	}
	public void setR185_exposure_class_off_bal(String r185_exposure_class_off_bal) {
		this.r185_exposure_class_off_bal = r185_exposure_class_off_bal;
	}
	public BigDecimal getR185_nom_pri_amt() {
		return r185_nom_pri_amt;
	}
	public void setR185_nom_pri_amt(BigDecimal r185_nom_pri_amt) {
		this.r185_nom_pri_amt = r185_nom_pri_amt;
	}
	public BigDecimal getR185_ccf() {
		return r185_ccf;
	}
	public void setR185_ccf(BigDecimal r185_ccf) {
		this.r185_ccf = r185_ccf;
	}
	public BigDecimal getR185_cea() {
		return r185_cea;
	}
	public void setR185_cea(BigDecimal r185_cea) {
		this.r185_cea = r185_cea;
	}
	public BigDecimal getR185_cea_elig_coun_bilt_net() {
		return r185_cea_elig_coun_bilt_net;
	}
	public void setR185_cea_elig_coun_bilt_net(BigDecimal r185_cea_elig_coun_bilt_net) {
		this.r185_cea_elig_coun_bilt_net = r185_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR185_cea_aft_net() {
		return r185_cea_aft_net;
	}
	public void setR185_cea_aft_net(BigDecimal r185_cea_aft_net) {
		this.r185_cea_aft_net = r185_cea_aft_net;
	}
	public BigDecimal getR185_crm_sub_app_cea_elig() {
		return r185_crm_sub_app_cea_elig;
	}
	public void setR185_crm_sub_app_cea_elig(BigDecimal r185_crm_sub_app_cea_elig) {
		this.r185_crm_sub_app_cea_elig = r185_crm_sub_app_cea_elig;
	}
	public BigDecimal getR185_crm_sub_app_non_col_guar_elig() {
		return r185_crm_sub_app_non_col_guar_elig;
	}
	public void setR185_crm_sub_app_non_col_guar_elig(BigDecimal r185_crm_sub_app_non_col_guar_elig) {
		this.r185_crm_sub_app_non_col_guar_elig = r185_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR185_crm_sub_app_non_col_cre_der() {
		return r185_crm_sub_app_non_col_cre_der;
	}
	public void setR185_crm_sub_app_non_col_cre_der(BigDecimal r185_crm_sub_app_non_col_cre_der) {
		this.r185_crm_sub_app_non_col_cre_der = r185_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR185_crm_sub_app_col_elig_cash() {
		return r185_crm_sub_app_col_elig_cash;
	}
	public void setR185_crm_sub_app_col_elig_cash(BigDecimal r185_crm_sub_app_col_elig_cash) {
		this.r185_crm_sub_app_col_elig_cash = r185_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR185_crm_sub_app_col_elig_tre_bills() {
		return r185_crm_sub_app_col_elig_tre_bills;
	}
	public void setR185_crm_sub_app_col_elig_tre_bills(BigDecimal r185_crm_sub_app_col_elig_tre_bills) {
		this.r185_crm_sub_app_col_elig_tre_bills = r185_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR185_crm_sub_app_col_elig_deb_sec() {
		return r185_crm_sub_app_col_elig_deb_sec;
	}
	public void setR185_crm_sub_app_col_elig_deb_sec(BigDecimal r185_crm_sub_app_col_elig_deb_sec) {
		this.r185_crm_sub_app_col_elig_deb_sec = r185_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR185_crm_sub_app_col_elig_euiq() {
		return r185_crm_sub_app_col_elig_euiq;
	}
	public void setR185_crm_sub_app_col_elig_euiq(BigDecimal r185_crm_sub_app_col_elig_euiq) {
		this.r185_crm_sub_app_col_elig_euiq = r185_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR185_crm_sub_app_col_elig_uni_tru() {
		return r185_crm_sub_app_col_elig_uni_tru;
	}
	public void setR185_crm_sub_app_col_elig_uni_tru(BigDecimal r185_crm_sub_app_col_elig_uni_tru) {
		this.r185_crm_sub_app_col_elig_uni_tru = r185_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR185_crm_sub_app_col_cea_cov() {
		return r185_crm_sub_app_col_cea_cov;
	}
	public void setR185_crm_sub_app_col_cea_cov(BigDecimal r185_crm_sub_app_col_cea_cov) {
		this.r185_crm_sub_app_col_cea_cov = r185_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR185_crm_sub_app_col_cea_not_cov() {
		return r185_crm_sub_app_col_cea_not_cov;
	}
	public void setR185_crm_sub_app_col_cea_not_cov(BigDecimal r185_crm_sub_app_col_cea_not_cov) {
		this.r185_crm_sub_app_col_cea_not_cov = r185_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR185_crm_sub_app_rwa_ris_wei_crm() {
		return r185_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR185_crm_sub_app_rwa_ris_wei_crm(BigDecimal r185_crm_sub_app_rwa_ris_wei_crm) {
		this.r185_crm_sub_app_rwa_ris_wei_crm = r185_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR185_crm_sub_app_rwa_ris_cea_cov() {
		return r185_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR185_crm_sub_app_rwa_ris_cea_cov(BigDecimal r185_crm_sub_app_rwa_ris_cea_cov) {
		this.r185_crm_sub_app_rwa_ris_cea_cov = r185_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR185_crm_sub_app_rwa_appl_org_coun() {
		return r185_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR185_crm_sub_app_rwa_appl_org_coun(BigDecimal r185_crm_sub_app_rwa_appl_org_coun) {
		this.r185_crm_sub_app_rwa_appl_org_coun = r185_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR185_crm_sub_app_rwa_ris_cea_not_cov() {
		return r185_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR185_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r185_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r185_crm_sub_app_rwa_ris_cea_not_cov = r185_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR185_crm_com_app_col_cea_elig_crm() {
		return r185_crm_com_app_col_cea_elig_crm;
	}
	public void setR185_crm_com_app_col_cea_elig_crm(BigDecimal r185_crm_com_app_col_cea_elig_crm) {
		this.r185_crm_com_app_col_cea_elig_crm = r185_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR185_crm_com_app_col_elig_cea_vol_adj() {
		return r185_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR185_crm_com_app_col_elig_cea_vol_adj(BigDecimal r185_crm_com_app_col_elig_cea_vol_adj) {
		this.r185_crm_com_app_col_elig_cea_vol_adj = r185_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR185_crm_com_app_col_elig_fin_hai() {
		return r185_crm_com_app_col_elig_fin_hai;
	}
	public void setR185_crm_com_app_col_elig_fin_hai(BigDecimal r185_crm_com_app_col_elig_fin_hai) {
		this.r185_crm_com_app_col_elig_fin_hai = r185_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR185_crm_com_app_col_cea_val_aft_crm() {
		return r185_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR185_crm_com_app_col_cea_val_aft_crm(BigDecimal r185_crm_com_app_col_cea_val_aft_crm) {
		this.r185_crm_com_app_col_cea_val_aft_crm = r185_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR185_rwa_elig_cea_not_cov() {
		return r185_rwa_elig_cea_not_cov;
	}
	public void setR185_rwa_elig_cea_not_cov(BigDecimal r185_rwa_elig_cea_not_cov) {
		this.r185_rwa_elig_cea_not_cov = r185_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR185_rwa_unsec_cea_sub_cre_ris() {
		return r185_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR185_rwa_unsec_cea_sub_cre_ris(BigDecimal r185_rwa_unsec_cea_sub_cre_ris) {
		this.r185_rwa_unsec_cea_sub_cre_ris = r185_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR185_rwa_unsec_cea() {
		return r185_rwa_unsec_cea;
	}
	public void setR185_rwa_unsec_cea(BigDecimal r185_rwa_unsec_cea) {
		this.r185_rwa_unsec_cea = r185_rwa_unsec_cea;
	}
	public BigDecimal getR185_rwa_tot_ris_wei_ass() {
		return r185_rwa_tot_ris_wei_ass;
	}
	public void setR185_rwa_tot_ris_wei_ass(BigDecimal r185_rwa_tot_ris_wei_ass) {
		this.r185_rwa_tot_ris_wei_ass = r185_rwa_tot_ris_wei_ass;
	}
	public String getR186_exposure_class_off_bal() {
		return r186_exposure_class_off_bal;
	}
	public void setR186_exposure_class_off_bal(String r186_exposure_class_off_bal) {
		this.r186_exposure_class_off_bal = r186_exposure_class_off_bal;
	}
	public BigDecimal getR186_nom_pri_amt() {
		return r186_nom_pri_amt;
	}
	public void setR186_nom_pri_amt(BigDecimal r186_nom_pri_amt) {
		this.r186_nom_pri_amt = r186_nom_pri_amt;
	}
	public BigDecimal getR186_ccf() {
		return r186_ccf;
	}
	public void setR186_ccf(BigDecimal r186_ccf) {
		this.r186_ccf = r186_ccf;
	}
	public BigDecimal getR186_cea() {
		return r186_cea;
	}
	public void setR186_cea(BigDecimal r186_cea) {
		this.r186_cea = r186_cea;
	}
	public BigDecimal getR186_cea_elig_coun_bilt_net() {
		return r186_cea_elig_coun_bilt_net;
	}
	public void setR186_cea_elig_coun_bilt_net(BigDecimal r186_cea_elig_coun_bilt_net) {
		this.r186_cea_elig_coun_bilt_net = r186_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR186_cea_aft_net() {
		return r186_cea_aft_net;
	}
	public void setR186_cea_aft_net(BigDecimal r186_cea_aft_net) {
		this.r186_cea_aft_net = r186_cea_aft_net;
	}
	public BigDecimal getR186_crm_sub_app_cea_elig() {
		return r186_crm_sub_app_cea_elig;
	}
	public void setR186_crm_sub_app_cea_elig(BigDecimal r186_crm_sub_app_cea_elig) {
		this.r186_crm_sub_app_cea_elig = r186_crm_sub_app_cea_elig;
	}
	public BigDecimal getR186_crm_sub_app_non_col_guar_elig() {
		return r186_crm_sub_app_non_col_guar_elig;
	}
	public void setR186_crm_sub_app_non_col_guar_elig(BigDecimal r186_crm_sub_app_non_col_guar_elig) {
		this.r186_crm_sub_app_non_col_guar_elig = r186_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR186_crm_sub_app_non_col_cre_der() {
		return r186_crm_sub_app_non_col_cre_der;
	}
	public void setR186_crm_sub_app_non_col_cre_der(BigDecimal r186_crm_sub_app_non_col_cre_der) {
		this.r186_crm_sub_app_non_col_cre_der = r186_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR186_crm_sub_app_col_elig_cash() {
		return r186_crm_sub_app_col_elig_cash;
	}
	public void setR186_crm_sub_app_col_elig_cash(BigDecimal r186_crm_sub_app_col_elig_cash) {
		this.r186_crm_sub_app_col_elig_cash = r186_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR186_crm_sub_app_col_elig_tre_bills() {
		return r186_crm_sub_app_col_elig_tre_bills;
	}
	public void setR186_crm_sub_app_col_elig_tre_bills(BigDecimal r186_crm_sub_app_col_elig_tre_bills) {
		this.r186_crm_sub_app_col_elig_tre_bills = r186_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR186_crm_sub_app_col_elig_deb_sec() {
		return r186_crm_sub_app_col_elig_deb_sec;
	}
	public void setR186_crm_sub_app_col_elig_deb_sec(BigDecimal r186_crm_sub_app_col_elig_deb_sec) {
		this.r186_crm_sub_app_col_elig_deb_sec = r186_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR186_crm_sub_app_col_elig_euiq() {
		return r186_crm_sub_app_col_elig_euiq;
	}
	public void setR186_crm_sub_app_col_elig_euiq(BigDecimal r186_crm_sub_app_col_elig_euiq) {
		this.r186_crm_sub_app_col_elig_euiq = r186_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR186_crm_sub_app_col_elig_uni_tru() {
		return r186_crm_sub_app_col_elig_uni_tru;
	}
	public void setR186_crm_sub_app_col_elig_uni_tru(BigDecimal r186_crm_sub_app_col_elig_uni_tru) {
		this.r186_crm_sub_app_col_elig_uni_tru = r186_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR186_crm_sub_app_col_cea_cov() {
		return r186_crm_sub_app_col_cea_cov;
	}
	public void setR186_crm_sub_app_col_cea_cov(BigDecimal r186_crm_sub_app_col_cea_cov) {
		this.r186_crm_sub_app_col_cea_cov = r186_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR186_crm_sub_app_col_cea_not_cov() {
		return r186_crm_sub_app_col_cea_not_cov;
	}
	public void setR186_crm_sub_app_col_cea_not_cov(BigDecimal r186_crm_sub_app_col_cea_not_cov) {
		this.r186_crm_sub_app_col_cea_not_cov = r186_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR186_crm_sub_app_rwa_ris_wei_crm() {
		return r186_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR186_crm_sub_app_rwa_ris_wei_crm(BigDecimal r186_crm_sub_app_rwa_ris_wei_crm) {
		this.r186_crm_sub_app_rwa_ris_wei_crm = r186_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR186_crm_sub_app_rwa_ris_cea_cov() {
		return r186_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR186_crm_sub_app_rwa_ris_cea_cov(BigDecimal r186_crm_sub_app_rwa_ris_cea_cov) {
		this.r186_crm_sub_app_rwa_ris_cea_cov = r186_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR186_crm_sub_app_rwa_appl_org_coun() {
		return r186_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR186_crm_sub_app_rwa_appl_org_coun(BigDecimal r186_crm_sub_app_rwa_appl_org_coun) {
		this.r186_crm_sub_app_rwa_appl_org_coun = r186_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR186_crm_sub_app_rwa_ris_cea_not_cov() {
		return r186_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR186_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r186_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r186_crm_sub_app_rwa_ris_cea_not_cov = r186_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR186_crm_com_app_col_cea_elig_crm() {
		return r186_crm_com_app_col_cea_elig_crm;
	}
	public void setR186_crm_com_app_col_cea_elig_crm(BigDecimal r186_crm_com_app_col_cea_elig_crm) {
		this.r186_crm_com_app_col_cea_elig_crm = r186_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR186_crm_com_app_col_elig_cea_vol_adj() {
		return r186_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR186_crm_com_app_col_elig_cea_vol_adj(BigDecimal r186_crm_com_app_col_elig_cea_vol_adj) {
		this.r186_crm_com_app_col_elig_cea_vol_adj = r186_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR186_crm_com_app_col_elig_fin_hai() {
		return r186_crm_com_app_col_elig_fin_hai;
	}
	public void setR186_crm_com_app_col_elig_fin_hai(BigDecimal r186_crm_com_app_col_elig_fin_hai) {
		this.r186_crm_com_app_col_elig_fin_hai = r186_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR186_crm_com_app_col_cea_val_aft_crm() {
		return r186_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR186_crm_com_app_col_cea_val_aft_crm(BigDecimal r186_crm_com_app_col_cea_val_aft_crm) {
		this.r186_crm_com_app_col_cea_val_aft_crm = r186_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR186_rwa_elig_cea_not_cov() {
		return r186_rwa_elig_cea_not_cov;
	}
	public void setR186_rwa_elig_cea_not_cov(BigDecimal r186_rwa_elig_cea_not_cov) {
		this.r186_rwa_elig_cea_not_cov = r186_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR186_rwa_unsec_cea_sub_cre_ris() {
		return r186_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR186_rwa_unsec_cea_sub_cre_ris(BigDecimal r186_rwa_unsec_cea_sub_cre_ris) {
		this.r186_rwa_unsec_cea_sub_cre_ris = r186_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR186_rwa_unsec_cea() {
		return r186_rwa_unsec_cea;
	}
	public void setR186_rwa_unsec_cea(BigDecimal r186_rwa_unsec_cea) {
		this.r186_rwa_unsec_cea = r186_rwa_unsec_cea;
	}
	public BigDecimal getR186_rwa_tot_ris_wei_ass() {
		return r186_rwa_tot_ris_wei_ass;
	}
	public void setR186_rwa_tot_ris_wei_ass(BigDecimal r186_rwa_tot_ris_wei_ass) {
		this.r186_rwa_tot_ris_wei_ass = r186_rwa_tot_ris_wei_ass;
	}
	public String getR187_exposure_class_off_bal() {
		return r187_exposure_class_off_bal;
	}
	public void setR187_exposure_class_off_bal(String r187_exposure_class_off_bal) {
		this.r187_exposure_class_off_bal = r187_exposure_class_off_bal;
	}
	public BigDecimal getR187_nom_pri_amt() {
		return r187_nom_pri_amt;
	}
	public void setR187_nom_pri_amt(BigDecimal r187_nom_pri_amt) {
		this.r187_nom_pri_amt = r187_nom_pri_amt;
	}
	public BigDecimal getR187_ccf() {
		return r187_ccf;
	}
	public void setR187_ccf(BigDecimal r187_ccf) {
		this.r187_ccf = r187_ccf;
	}
	public BigDecimal getR187_cea() {
		return r187_cea;
	}
	public void setR187_cea(BigDecimal r187_cea) {
		this.r187_cea = r187_cea;
	}
	public BigDecimal getR187_cea_elig_coun_bilt_net() {
		return r187_cea_elig_coun_bilt_net;
	}
	public void setR187_cea_elig_coun_bilt_net(BigDecimal r187_cea_elig_coun_bilt_net) {
		this.r187_cea_elig_coun_bilt_net = r187_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR187_cea_aft_net() {
		return r187_cea_aft_net;
	}
	public void setR187_cea_aft_net(BigDecimal r187_cea_aft_net) {
		this.r187_cea_aft_net = r187_cea_aft_net;
	}
	public BigDecimal getR187_crm_sub_app_cea_elig() {
		return r187_crm_sub_app_cea_elig;
	}
	public void setR187_crm_sub_app_cea_elig(BigDecimal r187_crm_sub_app_cea_elig) {
		this.r187_crm_sub_app_cea_elig = r187_crm_sub_app_cea_elig;
	}
	public BigDecimal getR187_crm_sub_app_non_col_guar_elig() {
		return r187_crm_sub_app_non_col_guar_elig;
	}
	public void setR187_crm_sub_app_non_col_guar_elig(BigDecimal r187_crm_sub_app_non_col_guar_elig) {
		this.r187_crm_sub_app_non_col_guar_elig = r187_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR187_crm_sub_app_non_col_cre_der() {
		return r187_crm_sub_app_non_col_cre_der;
	}
	public void setR187_crm_sub_app_non_col_cre_der(BigDecimal r187_crm_sub_app_non_col_cre_der) {
		this.r187_crm_sub_app_non_col_cre_der = r187_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR187_crm_sub_app_col_elig_cash() {
		return r187_crm_sub_app_col_elig_cash;
	}
	public void setR187_crm_sub_app_col_elig_cash(BigDecimal r187_crm_sub_app_col_elig_cash) {
		this.r187_crm_sub_app_col_elig_cash = r187_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR187_crm_sub_app_col_elig_tre_bills() {
		return r187_crm_sub_app_col_elig_tre_bills;
	}
	public void setR187_crm_sub_app_col_elig_tre_bills(BigDecimal r187_crm_sub_app_col_elig_tre_bills) {
		this.r187_crm_sub_app_col_elig_tre_bills = r187_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR187_crm_sub_app_col_elig_deb_sec() {
		return r187_crm_sub_app_col_elig_deb_sec;
	}
	public void setR187_crm_sub_app_col_elig_deb_sec(BigDecimal r187_crm_sub_app_col_elig_deb_sec) {
		this.r187_crm_sub_app_col_elig_deb_sec = r187_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR187_crm_sub_app_col_elig_euiq() {
		return r187_crm_sub_app_col_elig_euiq;
	}
	public void setR187_crm_sub_app_col_elig_euiq(BigDecimal r187_crm_sub_app_col_elig_euiq) {
		this.r187_crm_sub_app_col_elig_euiq = r187_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR187_crm_sub_app_col_elig_uni_tru() {
		return r187_crm_sub_app_col_elig_uni_tru;
	}
	public void setR187_crm_sub_app_col_elig_uni_tru(BigDecimal r187_crm_sub_app_col_elig_uni_tru) {
		this.r187_crm_sub_app_col_elig_uni_tru = r187_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR187_crm_sub_app_col_cea_cov() {
		return r187_crm_sub_app_col_cea_cov;
	}
	public void setR187_crm_sub_app_col_cea_cov(BigDecimal r187_crm_sub_app_col_cea_cov) {
		this.r187_crm_sub_app_col_cea_cov = r187_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR187_crm_sub_app_col_cea_not_cov() {
		return r187_crm_sub_app_col_cea_not_cov;
	}
	public void setR187_crm_sub_app_col_cea_not_cov(BigDecimal r187_crm_sub_app_col_cea_not_cov) {
		this.r187_crm_sub_app_col_cea_not_cov = r187_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR187_crm_sub_app_rwa_ris_wei_crm() {
		return r187_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR187_crm_sub_app_rwa_ris_wei_crm(BigDecimal r187_crm_sub_app_rwa_ris_wei_crm) {
		this.r187_crm_sub_app_rwa_ris_wei_crm = r187_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR187_crm_sub_app_rwa_ris_cea_cov() {
		return r187_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR187_crm_sub_app_rwa_ris_cea_cov(BigDecimal r187_crm_sub_app_rwa_ris_cea_cov) {
		this.r187_crm_sub_app_rwa_ris_cea_cov = r187_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR187_crm_sub_app_rwa_appl_org_coun() {
		return r187_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR187_crm_sub_app_rwa_appl_org_coun(BigDecimal r187_crm_sub_app_rwa_appl_org_coun) {
		this.r187_crm_sub_app_rwa_appl_org_coun = r187_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR187_crm_sub_app_rwa_ris_cea_not_cov() {
		return r187_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR187_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r187_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r187_crm_sub_app_rwa_ris_cea_not_cov = r187_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR187_crm_com_app_col_cea_elig_crm() {
		return r187_crm_com_app_col_cea_elig_crm;
	}
	public void setR187_crm_com_app_col_cea_elig_crm(BigDecimal r187_crm_com_app_col_cea_elig_crm) {
		this.r187_crm_com_app_col_cea_elig_crm = r187_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR187_crm_com_app_col_elig_cea_vol_adj() {
		return r187_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR187_crm_com_app_col_elig_cea_vol_adj(BigDecimal r187_crm_com_app_col_elig_cea_vol_adj) {
		this.r187_crm_com_app_col_elig_cea_vol_adj = r187_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR187_crm_com_app_col_elig_fin_hai() {
		return r187_crm_com_app_col_elig_fin_hai;
	}
	public void setR187_crm_com_app_col_elig_fin_hai(BigDecimal r187_crm_com_app_col_elig_fin_hai) {
		this.r187_crm_com_app_col_elig_fin_hai = r187_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR187_crm_com_app_col_cea_val_aft_crm() {
		return r187_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR187_crm_com_app_col_cea_val_aft_crm(BigDecimal r187_crm_com_app_col_cea_val_aft_crm) {
		this.r187_crm_com_app_col_cea_val_aft_crm = r187_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR187_rwa_elig_cea_not_cov() {
		return r187_rwa_elig_cea_not_cov;
	}
	public void setR187_rwa_elig_cea_not_cov(BigDecimal r187_rwa_elig_cea_not_cov) {
		this.r187_rwa_elig_cea_not_cov = r187_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR187_rwa_unsec_cea_sub_cre_ris() {
		return r187_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR187_rwa_unsec_cea_sub_cre_ris(BigDecimal r187_rwa_unsec_cea_sub_cre_ris) {
		this.r187_rwa_unsec_cea_sub_cre_ris = r187_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR187_rwa_unsec_cea() {
		return r187_rwa_unsec_cea;
	}
	public void setR187_rwa_unsec_cea(BigDecimal r187_rwa_unsec_cea) {
		this.r187_rwa_unsec_cea = r187_rwa_unsec_cea;
	}
	public BigDecimal getR187_rwa_tot_ris_wei_ass() {
		return r187_rwa_tot_ris_wei_ass;
	}
	public void setR187_rwa_tot_ris_wei_ass(BigDecimal r187_rwa_tot_ris_wei_ass) {
		this.r187_rwa_tot_ris_wei_ass = r187_rwa_tot_ris_wei_ass;
	}
	public String getR188_exposure_class_off_bal() {
		return r188_exposure_class_off_bal;
	}
	public void setR188_exposure_class_off_bal(String r188_exposure_class_off_bal) {
		this.r188_exposure_class_off_bal = r188_exposure_class_off_bal;
	}
	public BigDecimal getR188_nom_pri_amt() {
		return r188_nom_pri_amt;
	}
	public void setR188_nom_pri_amt(BigDecimal r188_nom_pri_amt) {
		this.r188_nom_pri_amt = r188_nom_pri_amt;
	}
	public BigDecimal getR188_ccf() {
		return r188_ccf;
	}
	public void setR188_ccf(BigDecimal r188_ccf) {
		this.r188_ccf = r188_ccf;
	}
	public BigDecimal getR188_cea() {
		return r188_cea;
	}
	public void setR188_cea(BigDecimal r188_cea) {
		this.r188_cea = r188_cea;
	}
	public BigDecimal getR188_cea_elig_coun_bilt_net() {
		return r188_cea_elig_coun_bilt_net;
	}
	public void setR188_cea_elig_coun_bilt_net(BigDecimal r188_cea_elig_coun_bilt_net) {
		this.r188_cea_elig_coun_bilt_net = r188_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR188_cea_aft_net() {
		return r188_cea_aft_net;
	}
	public void setR188_cea_aft_net(BigDecimal r188_cea_aft_net) {
		this.r188_cea_aft_net = r188_cea_aft_net;
	}
	public BigDecimal getR188_crm_sub_app_cea_elig() {
		return r188_crm_sub_app_cea_elig;
	}
	public void setR188_crm_sub_app_cea_elig(BigDecimal r188_crm_sub_app_cea_elig) {
		this.r188_crm_sub_app_cea_elig = r188_crm_sub_app_cea_elig;
	}
	public BigDecimal getR188_crm_sub_app_non_col_guar_elig() {
		return r188_crm_sub_app_non_col_guar_elig;
	}
	public void setR188_crm_sub_app_non_col_guar_elig(BigDecimal r188_crm_sub_app_non_col_guar_elig) {
		this.r188_crm_sub_app_non_col_guar_elig = r188_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR188_crm_sub_app_non_col_cre_der() {
		return r188_crm_sub_app_non_col_cre_der;
	}
	public void setR188_crm_sub_app_non_col_cre_der(BigDecimal r188_crm_sub_app_non_col_cre_der) {
		this.r188_crm_sub_app_non_col_cre_der = r188_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR188_crm_sub_app_col_elig_cash() {
		return r188_crm_sub_app_col_elig_cash;
	}
	public void setR188_crm_sub_app_col_elig_cash(BigDecimal r188_crm_sub_app_col_elig_cash) {
		this.r188_crm_sub_app_col_elig_cash = r188_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR188_crm_sub_app_col_elig_tre_bills() {
		return r188_crm_sub_app_col_elig_tre_bills;
	}
	public void setR188_crm_sub_app_col_elig_tre_bills(BigDecimal r188_crm_sub_app_col_elig_tre_bills) {
		this.r188_crm_sub_app_col_elig_tre_bills = r188_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR188_crm_sub_app_col_elig_deb_sec() {
		return r188_crm_sub_app_col_elig_deb_sec;
	}
	public void setR188_crm_sub_app_col_elig_deb_sec(BigDecimal r188_crm_sub_app_col_elig_deb_sec) {
		this.r188_crm_sub_app_col_elig_deb_sec = r188_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR188_crm_sub_app_col_elig_euiq() {
		return r188_crm_sub_app_col_elig_euiq;
	}
	public void setR188_crm_sub_app_col_elig_euiq(BigDecimal r188_crm_sub_app_col_elig_euiq) {
		this.r188_crm_sub_app_col_elig_euiq = r188_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR188_crm_sub_app_col_elig_uni_tru() {
		return r188_crm_sub_app_col_elig_uni_tru;
	}
	public void setR188_crm_sub_app_col_elig_uni_tru(BigDecimal r188_crm_sub_app_col_elig_uni_tru) {
		this.r188_crm_sub_app_col_elig_uni_tru = r188_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR188_crm_sub_app_col_cea_cov() {
		return r188_crm_sub_app_col_cea_cov;
	}
	public void setR188_crm_sub_app_col_cea_cov(BigDecimal r188_crm_sub_app_col_cea_cov) {
		this.r188_crm_sub_app_col_cea_cov = r188_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR188_crm_sub_app_col_cea_not_cov() {
		return r188_crm_sub_app_col_cea_not_cov;
	}
	public void setR188_crm_sub_app_col_cea_not_cov(BigDecimal r188_crm_sub_app_col_cea_not_cov) {
		this.r188_crm_sub_app_col_cea_not_cov = r188_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR188_crm_sub_app_rwa_ris_wei_crm() {
		return r188_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR188_crm_sub_app_rwa_ris_wei_crm(BigDecimal r188_crm_sub_app_rwa_ris_wei_crm) {
		this.r188_crm_sub_app_rwa_ris_wei_crm = r188_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR188_crm_sub_app_rwa_ris_cea_cov() {
		return r188_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR188_crm_sub_app_rwa_ris_cea_cov(BigDecimal r188_crm_sub_app_rwa_ris_cea_cov) {
		this.r188_crm_sub_app_rwa_ris_cea_cov = r188_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR188_crm_sub_app_rwa_appl_org_coun() {
		return r188_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR188_crm_sub_app_rwa_appl_org_coun(BigDecimal r188_crm_sub_app_rwa_appl_org_coun) {
		this.r188_crm_sub_app_rwa_appl_org_coun = r188_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR188_crm_sub_app_rwa_ris_cea_not_cov() {
		return r188_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR188_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r188_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r188_crm_sub_app_rwa_ris_cea_not_cov = r188_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR188_crm_com_app_col_cea_elig_crm() {
		return r188_crm_com_app_col_cea_elig_crm;
	}
	public void setR188_crm_com_app_col_cea_elig_crm(BigDecimal r188_crm_com_app_col_cea_elig_crm) {
		this.r188_crm_com_app_col_cea_elig_crm = r188_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR188_crm_com_app_col_elig_cea_vol_adj() {
		return r188_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR188_crm_com_app_col_elig_cea_vol_adj(BigDecimal r188_crm_com_app_col_elig_cea_vol_adj) {
		this.r188_crm_com_app_col_elig_cea_vol_adj = r188_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR188_crm_com_app_col_elig_fin_hai() {
		return r188_crm_com_app_col_elig_fin_hai;
	}
	public void setR188_crm_com_app_col_elig_fin_hai(BigDecimal r188_crm_com_app_col_elig_fin_hai) {
		this.r188_crm_com_app_col_elig_fin_hai = r188_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR188_crm_com_app_col_cea_val_aft_crm() {
		return r188_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR188_crm_com_app_col_cea_val_aft_crm(BigDecimal r188_crm_com_app_col_cea_val_aft_crm) {
		this.r188_crm_com_app_col_cea_val_aft_crm = r188_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR188_rwa_elig_cea_not_cov() {
		return r188_rwa_elig_cea_not_cov;
	}
	public void setR188_rwa_elig_cea_not_cov(BigDecimal r188_rwa_elig_cea_not_cov) {
		this.r188_rwa_elig_cea_not_cov = r188_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR188_rwa_unsec_cea_sub_cre_ris() {
		return r188_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR188_rwa_unsec_cea_sub_cre_ris(BigDecimal r188_rwa_unsec_cea_sub_cre_ris) {
		this.r188_rwa_unsec_cea_sub_cre_ris = r188_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR188_rwa_unsec_cea() {
		return r188_rwa_unsec_cea;
	}
	public void setR188_rwa_unsec_cea(BigDecimal r188_rwa_unsec_cea) {
		this.r188_rwa_unsec_cea = r188_rwa_unsec_cea;
	}
	public BigDecimal getR188_rwa_tot_ris_wei_ass() {
		return r188_rwa_tot_ris_wei_ass;
	}
	public void setR188_rwa_tot_ris_wei_ass(BigDecimal r188_rwa_tot_ris_wei_ass) {
		this.r188_rwa_tot_ris_wei_ass = r188_rwa_tot_ris_wei_ass;
	}
	public String getR189_exposure_class_off_bal() {
		return r189_exposure_class_off_bal;
	}
	public void setR189_exposure_class_off_bal(String r189_exposure_class_off_bal) {
		this.r189_exposure_class_off_bal = r189_exposure_class_off_bal;
	}
	public BigDecimal getR189_nom_pri_amt() {
		return r189_nom_pri_amt;
	}
	public void setR189_nom_pri_amt(BigDecimal r189_nom_pri_amt) {
		this.r189_nom_pri_amt = r189_nom_pri_amt;
	}
	public BigDecimal getR189_ccf() {
		return r189_ccf;
	}
	public void setR189_ccf(BigDecimal r189_ccf) {
		this.r189_ccf = r189_ccf;
	}
	public BigDecimal getR189_cea() {
		return r189_cea;
	}
	public void setR189_cea(BigDecimal r189_cea) {
		this.r189_cea = r189_cea;
	}
	public BigDecimal getR189_cea_elig_coun_bilt_net() {
		return r189_cea_elig_coun_bilt_net;
	}
	public void setR189_cea_elig_coun_bilt_net(BigDecimal r189_cea_elig_coun_bilt_net) {
		this.r189_cea_elig_coun_bilt_net = r189_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR189_cea_aft_net() {
		return r189_cea_aft_net;
	}
	public void setR189_cea_aft_net(BigDecimal r189_cea_aft_net) {
		this.r189_cea_aft_net = r189_cea_aft_net;
	}
	public BigDecimal getR189_crm_sub_app_cea_elig() {
		return r189_crm_sub_app_cea_elig;
	}
	public void setR189_crm_sub_app_cea_elig(BigDecimal r189_crm_sub_app_cea_elig) {
		this.r189_crm_sub_app_cea_elig = r189_crm_sub_app_cea_elig;
	}
	public BigDecimal getR189_crm_sub_app_non_col_guar_elig() {
		return r189_crm_sub_app_non_col_guar_elig;
	}
	public void setR189_crm_sub_app_non_col_guar_elig(BigDecimal r189_crm_sub_app_non_col_guar_elig) {
		this.r189_crm_sub_app_non_col_guar_elig = r189_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR189_crm_sub_app_non_col_cre_der() {
		return r189_crm_sub_app_non_col_cre_der;
	}
	public void setR189_crm_sub_app_non_col_cre_der(BigDecimal r189_crm_sub_app_non_col_cre_der) {
		this.r189_crm_sub_app_non_col_cre_der = r189_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR189_crm_sub_app_col_elig_cash() {
		return r189_crm_sub_app_col_elig_cash;
	}
	public void setR189_crm_sub_app_col_elig_cash(BigDecimal r189_crm_sub_app_col_elig_cash) {
		this.r189_crm_sub_app_col_elig_cash = r189_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR189_crm_sub_app_col_elig_tre_bills() {
		return r189_crm_sub_app_col_elig_tre_bills;
	}
	public void setR189_crm_sub_app_col_elig_tre_bills(BigDecimal r189_crm_sub_app_col_elig_tre_bills) {
		this.r189_crm_sub_app_col_elig_tre_bills = r189_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR189_crm_sub_app_col_elig_deb_sec() {
		return r189_crm_sub_app_col_elig_deb_sec;
	}
	public void setR189_crm_sub_app_col_elig_deb_sec(BigDecimal r189_crm_sub_app_col_elig_deb_sec) {
		this.r189_crm_sub_app_col_elig_deb_sec = r189_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR189_crm_sub_app_col_elig_euiq() {
		return r189_crm_sub_app_col_elig_euiq;
	}
	public void setR189_crm_sub_app_col_elig_euiq(BigDecimal r189_crm_sub_app_col_elig_euiq) {
		this.r189_crm_sub_app_col_elig_euiq = r189_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR189_crm_sub_app_col_elig_uni_tru() {
		return r189_crm_sub_app_col_elig_uni_tru;
	}
	public void setR189_crm_sub_app_col_elig_uni_tru(BigDecimal r189_crm_sub_app_col_elig_uni_tru) {
		this.r189_crm_sub_app_col_elig_uni_tru = r189_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR189_crm_sub_app_col_cea_cov() {
		return r189_crm_sub_app_col_cea_cov;
	}
	public void setR189_crm_sub_app_col_cea_cov(BigDecimal r189_crm_sub_app_col_cea_cov) {
		this.r189_crm_sub_app_col_cea_cov = r189_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR189_crm_sub_app_col_cea_not_cov() {
		return r189_crm_sub_app_col_cea_not_cov;
	}
	public void setR189_crm_sub_app_col_cea_not_cov(BigDecimal r189_crm_sub_app_col_cea_not_cov) {
		this.r189_crm_sub_app_col_cea_not_cov = r189_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR189_crm_sub_app_rwa_ris_wei_crm() {
		return r189_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR189_crm_sub_app_rwa_ris_wei_crm(BigDecimal r189_crm_sub_app_rwa_ris_wei_crm) {
		this.r189_crm_sub_app_rwa_ris_wei_crm = r189_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR189_crm_sub_app_rwa_ris_cea_cov() {
		return r189_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR189_crm_sub_app_rwa_ris_cea_cov(BigDecimal r189_crm_sub_app_rwa_ris_cea_cov) {
		this.r189_crm_sub_app_rwa_ris_cea_cov = r189_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR189_crm_sub_app_rwa_appl_org_coun() {
		return r189_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR189_crm_sub_app_rwa_appl_org_coun(BigDecimal r189_crm_sub_app_rwa_appl_org_coun) {
		this.r189_crm_sub_app_rwa_appl_org_coun = r189_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR189_crm_sub_app_rwa_ris_cea_not_cov() {
		return r189_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR189_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r189_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r189_crm_sub_app_rwa_ris_cea_not_cov = r189_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR189_crm_com_app_col_cea_elig_crm() {
		return r189_crm_com_app_col_cea_elig_crm;
	}
	public void setR189_crm_com_app_col_cea_elig_crm(BigDecimal r189_crm_com_app_col_cea_elig_crm) {
		this.r189_crm_com_app_col_cea_elig_crm = r189_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR189_crm_com_app_col_elig_cea_vol_adj() {
		return r189_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR189_crm_com_app_col_elig_cea_vol_adj(BigDecimal r189_crm_com_app_col_elig_cea_vol_adj) {
		this.r189_crm_com_app_col_elig_cea_vol_adj = r189_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR189_crm_com_app_col_elig_fin_hai() {
		return r189_crm_com_app_col_elig_fin_hai;
	}
	public void setR189_crm_com_app_col_elig_fin_hai(BigDecimal r189_crm_com_app_col_elig_fin_hai) {
		this.r189_crm_com_app_col_elig_fin_hai = r189_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR189_crm_com_app_col_cea_val_aft_crm() {
		return r189_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR189_crm_com_app_col_cea_val_aft_crm(BigDecimal r189_crm_com_app_col_cea_val_aft_crm) {
		this.r189_crm_com_app_col_cea_val_aft_crm = r189_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR189_rwa_elig_cea_not_cov() {
		return r189_rwa_elig_cea_not_cov;
	}
	public void setR189_rwa_elig_cea_not_cov(BigDecimal r189_rwa_elig_cea_not_cov) {
		this.r189_rwa_elig_cea_not_cov = r189_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR189_rwa_unsec_cea_sub_cre_ris() {
		return r189_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR189_rwa_unsec_cea_sub_cre_ris(BigDecimal r189_rwa_unsec_cea_sub_cre_ris) {
		this.r189_rwa_unsec_cea_sub_cre_ris = r189_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR189_rwa_unsec_cea() {
		return r189_rwa_unsec_cea;
	}
	public void setR189_rwa_unsec_cea(BigDecimal r189_rwa_unsec_cea) {
		this.r189_rwa_unsec_cea = r189_rwa_unsec_cea;
	}
	public BigDecimal getR189_rwa_tot_ris_wei_ass() {
		return r189_rwa_tot_ris_wei_ass;
	}
	public void setR189_rwa_tot_ris_wei_ass(BigDecimal r189_rwa_tot_ris_wei_ass) {
		this.r189_rwa_tot_ris_wei_ass = r189_rwa_tot_ris_wei_ass;
	}
	public String getR190_exposure_class_off_bal() {
		return r190_exposure_class_off_bal;
	}
	public void setR190_exposure_class_off_bal(String r190_exposure_class_off_bal) {
		this.r190_exposure_class_off_bal = r190_exposure_class_off_bal;
	}
	public BigDecimal getR190_nom_pri_amt() {
		return r190_nom_pri_amt;
	}
	public void setR190_nom_pri_amt(BigDecimal r190_nom_pri_amt) {
		this.r190_nom_pri_amt = r190_nom_pri_amt;
	}
	public BigDecimal getR190_ccf() {
		return r190_ccf;
	}
	public void setR190_ccf(BigDecimal r190_ccf) {
		this.r190_ccf = r190_ccf;
	}
	public BigDecimal getR190_cea() {
		return r190_cea;
	}
	public void setR190_cea(BigDecimal r190_cea) {
		this.r190_cea = r190_cea;
	}
	public BigDecimal getR190_cea_elig_coun_bilt_net() {
		return r190_cea_elig_coun_bilt_net;
	}
	public void setR190_cea_elig_coun_bilt_net(BigDecimal r190_cea_elig_coun_bilt_net) {
		this.r190_cea_elig_coun_bilt_net = r190_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR190_cea_aft_net() {
		return r190_cea_aft_net;
	}
	public void setR190_cea_aft_net(BigDecimal r190_cea_aft_net) {
		this.r190_cea_aft_net = r190_cea_aft_net;
	}
	public BigDecimal getR190_crm_sub_app_cea_elig() {
		return r190_crm_sub_app_cea_elig;
	}
	public void setR190_crm_sub_app_cea_elig(BigDecimal r190_crm_sub_app_cea_elig) {
		this.r190_crm_sub_app_cea_elig = r190_crm_sub_app_cea_elig;
	}
	public BigDecimal getR190_crm_sub_app_non_col_guar_elig() {
		return r190_crm_sub_app_non_col_guar_elig;
	}
	public void setR190_crm_sub_app_non_col_guar_elig(BigDecimal r190_crm_sub_app_non_col_guar_elig) {
		this.r190_crm_sub_app_non_col_guar_elig = r190_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR190_crm_sub_app_non_col_cre_der() {
		return r190_crm_sub_app_non_col_cre_der;
	}
	public void setR190_crm_sub_app_non_col_cre_der(BigDecimal r190_crm_sub_app_non_col_cre_der) {
		this.r190_crm_sub_app_non_col_cre_der = r190_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR190_crm_sub_app_col_elig_cash() {
		return r190_crm_sub_app_col_elig_cash;
	}
	public void setR190_crm_sub_app_col_elig_cash(BigDecimal r190_crm_sub_app_col_elig_cash) {
		this.r190_crm_sub_app_col_elig_cash = r190_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR190_crm_sub_app_col_elig_tre_bills() {
		return r190_crm_sub_app_col_elig_tre_bills;
	}
	public void setR190_crm_sub_app_col_elig_tre_bills(BigDecimal r190_crm_sub_app_col_elig_tre_bills) {
		this.r190_crm_sub_app_col_elig_tre_bills = r190_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR190_crm_sub_app_col_elig_deb_sec() {
		return r190_crm_sub_app_col_elig_deb_sec;
	}
	public void setR190_crm_sub_app_col_elig_deb_sec(BigDecimal r190_crm_sub_app_col_elig_deb_sec) {
		this.r190_crm_sub_app_col_elig_deb_sec = r190_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR190_crm_sub_app_col_elig_euiq() {
		return r190_crm_sub_app_col_elig_euiq;
	}
	public void setR190_crm_sub_app_col_elig_euiq(BigDecimal r190_crm_sub_app_col_elig_euiq) {
		this.r190_crm_sub_app_col_elig_euiq = r190_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR190_crm_sub_app_col_elig_uni_tru() {
		return r190_crm_sub_app_col_elig_uni_tru;
	}
	public void setR190_crm_sub_app_col_elig_uni_tru(BigDecimal r190_crm_sub_app_col_elig_uni_tru) {
		this.r190_crm_sub_app_col_elig_uni_tru = r190_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR190_crm_sub_app_col_cea_cov() {
		return r190_crm_sub_app_col_cea_cov;
	}
	public void setR190_crm_sub_app_col_cea_cov(BigDecimal r190_crm_sub_app_col_cea_cov) {
		this.r190_crm_sub_app_col_cea_cov = r190_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR190_crm_sub_app_col_cea_not_cov() {
		return r190_crm_sub_app_col_cea_not_cov;
	}
	public void setR190_crm_sub_app_col_cea_not_cov(BigDecimal r190_crm_sub_app_col_cea_not_cov) {
		this.r190_crm_sub_app_col_cea_not_cov = r190_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR190_crm_sub_app_rwa_ris_wei_crm() {
		return r190_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR190_crm_sub_app_rwa_ris_wei_crm(BigDecimal r190_crm_sub_app_rwa_ris_wei_crm) {
		this.r190_crm_sub_app_rwa_ris_wei_crm = r190_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR190_crm_sub_app_rwa_ris_cea_cov() {
		return r190_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR190_crm_sub_app_rwa_ris_cea_cov(BigDecimal r190_crm_sub_app_rwa_ris_cea_cov) {
		this.r190_crm_sub_app_rwa_ris_cea_cov = r190_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR190_crm_sub_app_rwa_appl_org_coun() {
		return r190_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR190_crm_sub_app_rwa_appl_org_coun(BigDecimal r190_crm_sub_app_rwa_appl_org_coun) {
		this.r190_crm_sub_app_rwa_appl_org_coun = r190_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR190_crm_sub_app_rwa_ris_cea_not_cov() {
		return r190_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR190_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r190_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r190_crm_sub_app_rwa_ris_cea_not_cov = r190_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR190_crm_com_app_col_cea_elig_crm() {
		return r190_crm_com_app_col_cea_elig_crm;
	}
	public void setR190_crm_com_app_col_cea_elig_crm(BigDecimal r190_crm_com_app_col_cea_elig_crm) {
		this.r190_crm_com_app_col_cea_elig_crm = r190_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR190_crm_com_app_col_elig_cea_vol_adj() {
		return r190_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR190_crm_com_app_col_elig_cea_vol_adj(BigDecimal r190_crm_com_app_col_elig_cea_vol_adj) {
		this.r190_crm_com_app_col_elig_cea_vol_adj = r190_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR190_crm_com_app_col_elig_fin_hai() {
		return r190_crm_com_app_col_elig_fin_hai;
	}
	public void setR190_crm_com_app_col_elig_fin_hai(BigDecimal r190_crm_com_app_col_elig_fin_hai) {
		this.r190_crm_com_app_col_elig_fin_hai = r190_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR190_crm_com_app_col_cea_val_aft_crm() {
		return r190_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR190_crm_com_app_col_cea_val_aft_crm(BigDecimal r190_crm_com_app_col_cea_val_aft_crm) {
		this.r190_crm_com_app_col_cea_val_aft_crm = r190_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR190_rwa_elig_cea_not_cov() {
		return r190_rwa_elig_cea_not_cov;
	}
	public void setR190_rwa_elig_cea_not_cov(BigDecimal r190_rwa_elig_cea_not_cov) {
		this.r190_rwa_elig_cea_not_cov = r190_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR190_rwa_unsec_cea_sub_cre_ris() {
		return r190_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR190_rwa_unsec_cea_sub_cre_ris(BigDecimal r190_rwa_unsec_cea_sub_cre_ris) {
		this.r190_rwa_unsec_cea_sub_cre_ris = r190_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR190_rwa_unsec_cea() {
		return r190_rwa_unsec_cea;
	}
	public void setR190_rwa_unsec_cea(BigDecimal r190_rwa_unsec_cea) {
		this.r190_rwa_unsec_cea = r190_rwa_unsec_cea;
	}
	public BigDecimal getR190_rwa_tot_ris_wei_ass() {
		return r190_rwa_tot_ris_wei_ass;
	}
	public void setR190_rwa_tot_ris_wei_ass(BigDecimal r190_rwa_tot_ris_wei_ass) {
		this.r190_rwa_tot_ris_wei_ass = r190_rwa_tot_ris_wei_ass;
	}
	public String getR191_exposure_class_off_bal() {
		return r191_exposure_class_off_bal;
	}
	public void setR191_exposure_class_off_bal(String r191_exposure_class_off_bal) {
		this.r191_exposure_class_off_bal = r191_exposure_class_off_bal;
	}
	public BigDecimal getR191_nom_pri_amt() {
		return r191_nom_pri_amt;
	}
	public void setR191_nom_pri_amt(BigDecimal r191_nom_pri_amt) {
		this.r191_nom_pri_amt = r191_nom_pri_amt;
	}
	public BigDecimal getR191_ccf() {
		return r191_ccf;
	}
	public void setR191_ccf(BigDecimal r191_ccf) {
		this.r191_ccf = r191_ccf;
	}
	public BigDecimal getR191_cea() {
		return r191_cea;
	}
	public void setR191_cea(BigDecimal r191_cea) {
		this.r191_cea = r191_cea;
	}
	public BigDecimal getR191_cea_elig_coun_bilt_net() {
		return r191_cea_elig_coun_bilt_net;
	}
	public void setR191_cea_elig_coun_bilt_net(BigDecimal r191_cea_elig_coun_bilt_net) {
		this.r191_cea_elig_coun_bilt_net = r191_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR191_cea_aft_net() {
		return r191_cea_aft_net;
	}
	public void setR191_cea_aft_net(BigDecimal r191_cea_aft_net) {
		this.r191_cea_aft_net = r191_cea_aft_net;
	}
	public BigDecimal getR191_crm_sub_app_cea_elig() {
		return r191_crm_sub_app_cea_elig;
	}
	public void setR191_crm_sub_app_cea_elig(BigDecimal r191_crm_sub_app_cea_elig) {
		this.r191_crm_sub_app_cea_elig = r191_crm_sub_app_cea_elig;
	}
	public BigDecimal getR191_crm_sub_app_non_col_guar_elig() {
		return r191_crm_sub_app_non_col_guar_elig;
	}
	public void setR191_crm_sub_app_non_col_guar_elig(BigDecimal r191_crm_sub_app_non_col_guar_elig) {
		this.r191_crm_sub_app_non_col_guar_elig = r191_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR191_crm_sub_app_non_col_cre_der() {
		return r191_crm_sub_app_non_col_cre_der;
	}
	public void setR191_crm_sub_app_non_col_cre_der(BigDecimal r191_crm_sub_app_non_col_cre_der) {
		this.r191_crm_sub_app_non_col_cre_der = r191_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR191_crm_sub_app_col_elig_cash() {
		return r191_crm_sub_app_col_elig_cash;
	}
	public void setR191_crm_sub_app_col_elig_cash(BigDecimal r191_crm_sub_app_col_elig_cash) {
		this.r191_crm_sub_app_col_elig_cash = r191_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR191_crm_sub_app_col_elig_tre_bills() {
		return r191_crm_sub_app_col_elig_tre_bills;
	}
	public void setR191_crm_sub_app_col_elig_tre_bills(BigDecimal r191_crm_sub_app_col_elig_tre_bills) {
		this.r191_crm_sub_app_col_elig_tre_bills = r191_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR191_crm_sub_app_col_elig_deb_sec() {
		return r191_crm_sub_app_col_elig_deb_sec;
	}
	public void setR191_crm_sub_app_col_elig_deb_sec(BigDecimal r191_crm_sub_app_col_elig_deb_sec) {
		this.r191_crm_sub_app_col_elig_deb_sec = r191_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR191_crm_sub_app_col_elig_euiq() {
		return r191_crm_sub_app_col_elig_euiq;
	}
	public void setR191_crm_sub_app_col_elig_euiq(BigDecimal r191_crm_sub_app_col_elig_euiq) {
		this.r191_crm_sub_app_col_elig_euiq = r191_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR191_crm_sub_app_col_elig_uni_tru() {
		return r191_crm_sub_app_col_elig_uni_tru;
	}
	public void setR191_crm_sub_app_col_elig_uni_tru(BigDecimal r191_crm_sub_app_col_elig_uni_tru) {
		this.r191_crm_sub_app_col_elig_uni_tru = r191_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR191_crm_sub_app_col_cea_cov() {
		return r191_crm_sub_app_col_cea_cov;
	}
	public void setR191_crm_sub_app_col_cea_cov(BigDecimal r191_crm_sub_app_col_cea_cov) {
		this.r191_crm_sub_app_col_cea_cov = r191_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR191_crm_sub_app_col_cea_not_cov() {
		return r191_crm_sub_app_col_cea_not_cov;
	}
	public void setR191_crm_sub_app_col_cea_not_cov(BigDecimal r191_crm_sub_app_col_cea_not_cov) {
		this.r191_crm_sub_app_col_cea_not_cov = r191_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR191_crm_sub_app_rwa_ris_wei_crm() {
		return r191_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR191_crm_sub_app_rwa_ris_wei_crm(BigDecimal r191_crm_sub_app_rwa_ris_wei_crm) {
		this.r191_crm_sub_app_rwa_ris_wei_crm = r191_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR191_crm_sub_app_rwa_ris_cea_cov() {
		return r191_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR191_crm_sub_app_rwa_ris_cea_cov(BigDecimal r191_crm_sub_app_rwa_ris_cea_cov) {
		this.r191_crm_sub_app_rwa_ris_cea_cov = r191_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR191_crm_sub_app_rwa_appl_org_coun() {
		return r191_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR191_crm_sub_app_rwa_appl_org_coun(BigDecimal r191_crm_sub_app_rwa_appl_org_coun) {
		this.r191_crm_sub_app_rwa_appl_org_coun = r191_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR191_crm_sub_app_rwa_ris_cea_not_cov() {
		return r191_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR191_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r191_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r191_crm_sub_app_rwa_ris_cea_not_cov = r191_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR191_crm_com_app_col_cea_elig_crm() {
		return r191_crm_com_app_col_cea_elig_crm;
	}
	public void setR191_crm_com_app_col_cea_elig_crm(BigDecimal r191_crm_com_app_col_cea_elig_crm) {
		this.r191_crm_com_app_col_cea_elig_crm = r191_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR191_crm_com_app_col_elig_cea_vol_adj() {
		return r191_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR191_crm_com_app_col_elig_cea_vol_adj(BigDecimal r191_crm_com_app_col_elig_cea_vol_adj) {
		this.r191_crm_com_app_col_elig_cea_vol_adj = r191_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR191_crm_com_app_col_elig_fin_hai() {
		return r191_crm_com_app_col_elig_fin_hai;
	}
	public void setR191_crm_com_app_col_elig_fin_hai(BigDecimal r191_crm_com_app_col_elig_fin_hai) {
		this.r191_crm_com_app_col_elig_fin_hai = r191_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR191_crm_com_app_col_cea_val_aft_crm() {
		return r191_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR191_crm_com_app_col_cea_val_aft_crm(BigDecimal r191_crm_com_app_col_cea_val_aft_crm) {
		this.r191_crm_com_app_col_cea_val_aft_crm = r191_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR191_rwa_elig_cea_not_cov() {
		return r191_rwa_elig_cea_not_cov;
	}
	public void setR191_rwa_elig_cea_not_cov(BigDecimal r191_rwa_elig_cea_not_cov) {
		this.r191_rwa_elig_cea_not_cov = r191_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR191_rwa_unsec_cea_sub_cre_ris() {
		return r191_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR191_rwa_unsec_cea_sub_cre_ris(BigDecimal r191_rwa_unsec_cea_sub_cre_ris) {
		this.r191_rwa_unsec_cea_sub_cre_ris = r191_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR191_rwa_unsec_cea() {
		return r191_rwa_unsec_cea;
	}
	public void setR191_rwa_unsec_cea(BigDecimal r191_rwa_unsec_cea) {
		this.r191_rwa_unsec_cea = r191_rwa_unsec_cea;
	}
	public BigDecimal getR191_rwa_tot_ris_wei_ass() {
		return r191_rwa_tot_ris_wei_ass;
	}
	public void setR191_rwa_tot_ris_wei_ass(BigDecimal r191_rwa_tot_ris_wei_ass) {
		this.r191_rwa_tot_ris_wei_ass = r191_rwa_tot_ris_wei_ass;
	}
	public String getR192_exposure_class_off_bal() {
		return r192_exposure_class_off_bal;
	}
	public void setR192_exposure_class_off_bal(String r192_exposure_class_off_bal) {
		this.r192_exposure_class_off_bal = r192_exposure_class_off_bal;
	}
	public BigDecimal getR192_nom_pri_amt() {
		return r192_nom_pri_amt;
	}
	public void setR192_nom_pri_amt(BigDecimal r192_nom_pri_amt) {
		this.r192_nom_pri_amt = r192_nom_pri_amt;
	}
	public BigDecimal getR192_ccf() {
		return r192_ccf;
	}
	public void setR192_ccf(BigDecimal r192_ccf) {
		this.r192_ccf = r192_ccf;
	}
	public BigDecimal getR192_cea() {
		return r192_cea;
	}
	public void setR192_cea(BigDecimal r192_cea) {
		this.r192_cea = r192_cea;
	}
	public BigDecimal getR192_cea_elig_coun_bilt_net() {
		return r192_cea_elig_coun_bilt_net;
	}
	public void setR192_cea_elig_coun_bilt_net(BigDecimal r192_cea_elig_coun_bilt_net) {
		this.r192_cea_elig_coun_bilt_net = r192_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR192_cea_aft_net() {
		return r192_cea_aft_net;
	}
	public void setR192_cea_aft_net(BigDecimal r192_cea_aft_net) {
		this.r192_cea_aft_net = r192_cea_aft_net;
	}
	public BigDecimal getR192_crm_sub_app_cea_elig() {
		return r192_crm_sub_app_cea_elig;
	}
	public void setR192_crm_sub_app_cea_elig(BigDecimal r192_crm_sub_app_cea_elig) {
		this.r192_crm_sub_app_cea_elig = r192_crm_sub_app_cea_elig;
	}
	public BigDecimal getR192_crm_sub_app_non_col_guar_elig() {
		return r192_crm_sub_app_non_col_guar_elig;
	}
	public void setR192_crm_sub_app_non_col_guar_elig(BigDecimal r192_crm_sub_app_non_col_guar_elig) {
		this.r192_crm_sub_app_non_col_guar_elig = r192_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR192_crm_sub_app_non_col_cre_der() {
		return r192_crm_sub_app_non_col_cre_der;
	}
	public void setR192_crm_sub_app_non_col_cre_der(BigDecimal r192_crm_sub_app_non_col_cre_der) {
		this.r192_crm_sub_app_non_col_cre_der = r192_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR192_crm_sub_app_col_elig_cash() {
		return r192_crm_sub_app_col_elig_cash;
	}
	public void setR192_crm_sub_app_col_elig_cash(BigDecimal r192_crm_sub_app_col_elig_cash) {
		this.r192_crm_sub_app_col_elig_cash = r192_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR192_crm_sub_app_col_elig_tre_bills() {
		return r192_crm_sub_app_col_elig_tre_bills;
	}
	public void setR192_crm_sub_app_col_elig_tre_bills(BigDecimal r192_crm_sub_app_col_elig_tre_bills) {
		this.r192_crm_sub_app_col_elig_tre_bills = r192_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR192_crm_sub_app_col_elig_deb_sec() {
		return r192_crm_sub_app_col_elig_deb_sec;
	}
	public void setR192_crm_sub_app_col_elig_deb_sec(BigDecimal r192_crm_sub_app_col_elig_deb_sec) {
		this.r192_crm_sub_app_col_elig_deb_sec = r192_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR192_crm_sub_app_col_elig_euiq() {
		return r192_crm_sub_app_col_elig_euiq;
	}
	public void setR192_crm_sub_app_col_elig_euiq(BigDecimal r192_crm_sub_app_col_elig_euiq) {
		this.r192_crm_sub_app_col_elig_euiq = r192_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR192_crm_sub_app_col_elig_uni_tru() {
		return r192_crm_sub_app_col_elig_uni_tru;
	}
	public void setR192_crm_sub_app_col_elig_uni_tru(BigDecimal r192_crm_sub_app_col_elig_uni_tru) {
		this.r192_crm_sub_app_col_elig_uni_tru = r192_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR192_crm_sub_app_col_cea_cov() {
		return r192_crm_sub_app_col_cea_cov;
	}
	public void setR192_crm_sub_app_col_cea_cov(BigDecimal r192_crm_sub_app_col_cea_cov) {
		this.r192_crm_sub_app_col_cea_cov = r192_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR192_crm_sub_app_col_cea_not_cov() {
		return r192_crm_sub_app_col_cea_not_cov;
	}
	public void setR192_crm_sub_app_col_cea_not_cov(BigDecimal r192_crm_sub_app_col_cea_not_cov) {
		this.r192_crm_sub_app_col_cea_not_cov = r192_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR192_crm_sub_app_rwa_ris_wei_crm() {
		return r192_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR192_crm_sub_app_rwa_ris_wei_crm(BigDecimal r192_crm_sub_app_rwa_ris_wei_crm) {
		this.r192_crm_sub_app_rwa_ris_wei_crm = r192_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR192_crm_sub_app_rwa_ris_cea_cov() {
		return r192_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR192_crm_sub_app_rwa_ris_cea_cov(BigDecimal r192_crm_sub_app_rwa_ris_cea_cov) {
		this.r192_crm_sub_app_rwa_ris_cea_cov = r192_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR192_crm_sub_app_rwa_appl_org_coun() {
		return r192_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR192_crm_sub_app_rwa_appl_org_coun(BigDecimal r192_crm_sub_app_rwa_appl_org_coun) {
		this.r192_crm_sub_app_rwa_appl_org_coun = r192_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR192_crm_sub_app_rwa_ris_cea_not_cov() {
		return r192_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR192_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r192_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r192_crm_sub_app_rwa_ris_cea_not_cov = r192_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR192_crm_com_app_col_cea_elig_crm() {
		return r192_crm_com_app_col_cea_elig_crm;
	}
	public void setR192_crm_com_app_col_cea_elig_crm(BigDecimal r192_crm_com_app_col_cea_elig_crm) {
		this.r192_crm_com_app_col_cea_elig_crm = r192_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR192_crm_com_app_col_elig_cea_vol_adj() {
		return r192_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR192_crm_com_app_col_elig_cea_vol_adj(BigDecimal r192_crm_com_app_col_elig_cea_vol_adj) {
		this.r192_crm_com_app_col_elig_cea_vol_adj = r192_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR192_crm_com_app_col_elig_fin_hai() {
		return r192_crm_com_app_col_elig_fin_hai;
	}
	public void setR192_crm_com_app_col_elig_fin_hai(BigDecimal r192_crm_com_app_col_elig_fin_hai) {
		this.r192_crm_com_app_col_elig_fin_hai = r192_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR192_crm_com_app_col_cea_val_aft_crm() {
		return r192_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR192_crm_com_app_col_cea_val_aft_crm(BigDecimal r192_crm_com_app_col_cea_val_aft_crm) {
		this.r192_crm_com_app_col_cea_val_aft_crm = r192_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR192_rwa_elig_cea_not_cov() {
		return r192_rwa_elig_cea_not_cov;
	}
	public void setR192_rwa_elig_cea_not_cov(BigDecimal r192_rwa_elig_cea_not_cov) {
		this.r192_rwa_elig_cea_not_cov = r192_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR192_rwa_unsec_cea_sub_cre_ris() {
		return r192_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR192_rwa_unsec_cea_sub_cre_ris(BigDecimal r192_rwa_unsec_cea_sub_cre_ris) {
		this.r192_rwa_unsec_cea_sub_cre_ris = r192_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR192_rwa_unsec_cea() {
		return r192_rwa_unsec_cea;
	}
	public void setR192_rwa_unsec_cea(BigDecimal r192_rwa_unsec_cea) {
		this.r192_rwa_unsec_cea = r192_rwa_unsec_cea;
	}
	public BigDecimal getR192_rwa_tot_ris_wei_ass() {
		return r192_rwa_tot_ris_wei_ass;
	}
	public void setR192_rwa_tot_ris_wei_ass(BigDecimal r192_rwa_tot_ris_wei_ass) {
		this.r192_rwa_tot_ris_wei_ass = r192_rwa_tot_ris_wei_ass;
	}
	public String getR193_exposure_class_off_bal() {
		return r193_exposure_class_off_bal;
	}
	public void setR193_exposure_class_off_bal(String r193_exposure_class_off_bal) {
		this.r193_exposure_class_off_bal = r193_exposure_class_off_bal;
	}
	public BigDecimal getR193_nom_pri_amt() {
		return r193_nom_pri_amt;
	}
	public void setR193_nom_pri_amt(BigDecimal r193_nom_pri_amt) {
		this.r193_nom_pri_amt = r193_nom_pri_amt;
	}
	public BigDecimal getR193_ccf() {
		return r193_ccf;
	}
	public void setR193_ccf(BigDecimal r193_ccf) {
		this.r193_ccf = r193_ccf;
	}
	public BigDecimal getR193_cea() {
		return r193_cea;
	}
	public void setR193_cea(BigDecimal r193_cea) {
		this.r193_cea = r193_cea;
	}
	public BigDecimal getR193_cea_elig_coun_bilt_net() {
		return r193_cea_elig_coun_bilt_net;
	}
	public void setR193_cea_elig_coun_bilt_net(BigDecimal r193_cea_elig_coun_bilt_net) {
		this.r193_cea_elig_coun_bilt_net = r193_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR193_cea_aft_net() {
		return r193_cea_aft_net;
	}
	public void setR193_cea_aft_net(BigDecimal r193_cea_aft_net) {
		this.r193_cea_aft_net = r193_cea_aft_net;
	}
	public BigDecimal getR193_crm_sub_app_cea_elig() {
		return r193_crm_sub_app_cea_elig;
	}
	public void setR193_crm_sub_app_cea_elig(BigDecimal r193_crm_sub_app_cea_elig) {
		this.r193_crm_sub_app_cea_elig = r193_crm_sub_app_cea_elig;
	}
	public BigDecimal getR193_crm_sub_app_non_col_guar_elig() {
		return r193_crm_sub_app_non_col_guar_elig;
	}
	public void setR193_crm_sub_app_non_col_guar_elig(BigDecimal r193_crm_sub_app_non_col_guar_elig) {
		this.r193_crm_sub_app_non_col_guar_elig = r193_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR193_crm_sub_app_non_col_cre_der() {
		return r193_crm_sub_app_non_col_cre_der;
	}
	public void setR193_crm_sub_app_non_col_cre_der(BigDecimal r193_crm_sub_app_non_col_cre_der) {
		this.r193_crm_sub_app_non_col_cre_der = r193_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR193_crm_sub_app_col_elig_cash() {
		return r193_crm_sub_app_col_elig_cash;
	}
	public void setR193_crm_sub_app_col_elig_cash(BigDecimal r193_crm_sub_app_col_elig_cash) {
		this.r193_crm_sub_app_col_elig_cash = r193_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR193_crm_sub_app_col_elig_tre_bills() {
		return r193_crm_sub_app_col_elig_tre_bills;
	}
	public void setR193_crm_sub_app_col_elig_tre_bills(BigDecimal r193_crm_sub_app_col_elig_tre_bills) {
		this.r193_crm_sub_app_col_elig_tre_bills = r193_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR193_crm_sub_app_col_elig_deb_sec() {
		return r193_crm_sub_app_col_elig_deb_sec;
	}
	public void setR193_crm_sub_app_col_elig_deb_sec(BigDecimal r193_crm_sub_app_col_elig_deb_sec) {
		this.r193_crm_sub_app_col_elig_deb_sec = r193_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR193_crm_sub_app_col_elig_euiq() {
		return r193_crm_sub_app_col_elig_euiq;
	}
	public void setR193_crm_sub_app_col_elig_euiq(BigDecimal r193_crm_sub_app_col_elig_euiq) {
		this.r193_crm_sub_app_col_elig_euiq = r193_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR193_crm_sub_app_col_elig_uni_tru() {
		return r193_crm_sub_app_col_elig_uni_tru;
	}
	public void setR193_crm_sub_app_col_elig_uni_tru(BigDecimal r193_crm_sub_app_col_elig_uni_tru) {
		this.r193_crm_sub_app_col_elig_uni_tru = r193_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR193_crm_sub_app_col_cea_cov() {
		return r193_crm_sub_app_col_cea_cov;
	}
	public void setR193_crm_sub_app_col_cea_cov(BigDecimal r193_crm_sub_app_col_cea_cov) {
		this.r193_crm_sub_app_col_cea_cov = r193_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR193_crm_sub_app_col_cea_not_cov() {
		return r193_crm_sub_app_col_cea_not_cov;
	}
	public void setR193_crm_sub_app_col_cea_not_cov(BigDecimal r193_crm_sub_app_col_cea_not_cov) {
		this.r193_crm_sub_app_col_cea_not_cov = r193_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR193_crm_sub_app_rwa_ris_wei_crm() {
		return r193_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR193_crm_sub_app_rwa_ris_wei_crm(BigDecimal r193_crm_sub_app_rwa_ris_wei_crm) {
		this.r193_crm_sub_app_rwa_ris_wei_crm = r193_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR193_crm_sub_app_rwa_ris_cea_cov() {
		return r193_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR193_crm_sub_app_rwa_ris_cea_cov(BigDecimal r193_crm_sub_app_rwa_ris_cea_cov) {
		this.r193_crm_sub_app_rwa_ris_cea_cov = r193_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR193_crm_sub_app_rwa_appl_org_coun() {
		return r193_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR193_crm_sub_app_rwa_appl_org_coun(BigDecimal r193_crm_sub_app_rwa_appl_org_coun) {
		this.r193_crm_sub_app_rwa_appl_org_coun = r193_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR193_crm_sub_app_rwa_ris_cea_not_cov() {
		return r193_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR193_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r193_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r193_crm_sub_app_rwa_ris_cea_not_cov = r193_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR193_crm_com_app_col_cea_elig_crm() {
		return r193_crm_com_app_col_cea_elig_crm;
	}
	public void setR193_crm_com_app_col_cea_elig_crm(BigDecimal r193_crm_com_app_col_cea_elig_crm) {
		this.r193_crm_com_app_col_cea_elig_crm = r193_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR193_crm_com_app_col_elig_cea_vol_adj() {
		return r193_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR193_crm_com_app_col_elig_cea_vol_adj(BigDecimal r193_crm_com_app_col_elig_cea_vol_adj) {
		this.r193_crm_com_app_col_elig_cea_vol_adj = r193_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR193_crm_com_app_col_elig_fin_hai() {
		return r193_crm_com_app_col_elig_fin_hai;
	}
	public void setR193_crm_com_app_col_elig_fin_hai(BigDecimal r193_crm_com_app_col_elig_fin_hai) {
		this.r193_crm_com_app_col_elig_fin_hai = r193_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR193_crm_com_app_col_cea_val_aft_crm() {
		return r193_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR193_crm_com_app_col_cea_val_aft_crm(BigDecimal r193_crm_com_app_col_cea_val_aft_crm) {
		this.r193_crm_com_app_col_cea_val_aft_crm = r193_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR193_rwa_elig_cea_not_cov() {
		return r193_rwa_elig_cea_not_cov;
	}
	public void setR193_rwa_elig_cea_not_cov(BigDecimal r193_rwa_elig_cea_not_cov) {
		this.r193_rwa_elig_cea_not_cov = r193_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR193_rwa_unsec_cea_sub_cre_ris() {
		return r193_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR193_rwa_unsec_cea_sub_cre_ris(BigDecimal r193_rwa_unsec_cea_sub_cre_ris) {
		this.r193_rwa_unsec_cea_sub_cre_ris = r193_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR193_rwa_unsec_cea() {
		return r193_rwa_unsec_cea;
	}
	public void setR193_rwa_unsec_cea(BigDecimal r193_rwa_unsec_cea) {
		this.r193_rwa_unsec_cea = r193_rwa_unsec_cea;
	}
	public BigDecimal getR193_rwa_tot_ris_wei_ass() {
		return r193_rwa_tot_ris_wei_ass;
	}
	public void setR193_rwa_tot_ris_wei_ass(BigDecimal r193_rwa_tot_ris_wei_ass) {
		this.r193_rwa_tot_ris_wei_ass = r193_rwa_tot_ris_wei_ass;
	}
	public String getR194_exposure_class_off_bal() {
		return r194_exposure_class_off_bal;
	}
	public void setR194_exposure_class_off_bal(String r194_exposure_class_off_bal) {
		this.r194_exposure_class_off_bal = r194_exposure_class_off_bal;
	}
	public BigDecimal getR194_nom_pri_amt() {
		return r194_nom_pri_amt;
	}
	public void setR194_nom_pri_amt(BigDecimal r194_nom_pri_amt) {
		this.r194_nom_pri_amt = r194_nom_pri_amt;
	}
	public BigDecimal getR194_ccf() {
		return r194_ccf;
	}
	public void setR194_ccf(BigDecimal r194_ccf) {
		this.r194_ccf = r194_ccf;
	}
	public BigDecimal getR194_cea() {
		return r194_cea;
	}
	public void setR194_cea(BigDecimal r194_cea) {
		this.r194_cea = r194_cea;
	}
	public BigDecimal getR194_cea_elig_coun_bilt_net() {
		return r194_cea_elig_coun_bilt_net;
	}
	public void setR194_cea_elig_coun_bilt_net(BigDecimal r194_cea_elig_coun_bilt_net) {
		this.r194_cea_elig_coun_bilt_net = r194_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR194_cea_aft_net() {
		return r194_cea_aft_net;
	}
	public void setR194_cea_aft_net(BigDecimal r194_cea_aft_net) {
		this.r194_cea_aft_net = r194_cea_aft_net;
	}
	public BigDecimal getR194_crm_sub_app_cea_elig() {
		return r194_crm_sub_app_cea_elig;
	}
	public void setR194_crm_sub_app_cea_elig(BigDecimal r194_crm_sub_app_cea_elig) {
		this.r194_crm_sub_app_cea_elig = r194_crm_sub_app_cea_elig;
	}
	public BigDecimal getR194_crm_sub_app_non_col_guar_elig() {
		return r194_crm_sub_app_non_col_guar_elig;
	}
	public void setR194_crm_sub_app_non_col_guar_elig(BigDecimal r194_crm_sub_app_non_col_guar_elig) {
		this.r194_crm_sub_app_non_col_guar_elig = r194_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR194_crm_sub_app_non_col_cre_der() {
		return r194_crm_sub_app_non_col_cre_der;
	}
	public void setR194_crm_sub_app_non_col_cre_der(BigDecimal r194_crm_sub_app_non_col_cre_der) {
		this.r194_crm_sub_app_non_col_cre_der = r194_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR194_crm_sub_app_col_elig_cash() {
		return r194_crm_sub_app_col_elig_cash;
	}
	public void setR194_crm_sub_app_col_elig_cash(BigDecimal r194_crm_sub_app_col_elig_cash) {
		this.r194_crm_sub_app_col_elig_cash = r194_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR194_crm_sub_app_col_elig_tre_bills() {
		return r194_crm_sub_app_col_elig_tre_bills;
	}
	public void setR194_crm_sub_app_col_elig_tre_bills(BigDecimal r194_crm_sub_app_col_elig_tre_bills) {
		this.r194_crm_sub_app_col_elig_tre_bills = r194_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR194_crm_sub_app_col_elig_deb_sec() {
		return r194_crm_sub_app_col_elig_deb_sec;
	}
	public void setR194_crm_sub_app_col_elig_deb_sec(BigDecimal r194_crm_sub_app_col_elig_deb_sec) {
		this.r194_crm_sub_app_col_elig_deb_sec = r194_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR194_crm_sub_app_col_elig_euiq() {
		return r194_crm_sub_app_col_elig_euiq;
	}
	public void setR194_crm_sub_app_col_elig_euiq(BigDecimal r194_crm_sub_app_col_elig_euiq) {
		this.r194_crm_sub_app_col_elig_euiq = r194_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR194_crm_sub_app_col_elig_uni_tru() {
		return r194_crm_sub_app_col_elig_uni_tru;
	}
	public void setR194_crm_sub_app_col_elig_uni_tru(BigDecimal r194_crm_sub_app_col_elig_uni_tru) {
		this.r194_crm_sub_app_col_elig_uni_tru = r194_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR194_crm_sub_app_col_cea_cov() {
		return r194_crm_sub_app_col_cea_cov;
	}
	public void setR194_crm_sub_app_col_cea_cov(BigDecimal r194_crm_sub_app_col_cea_cov) {
		this.r194_crm_sub_app_col_cea_cov = r194_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR194_crm_sub_app_col_cea_not_cov() {
		return r194_crm_sub_app_col_cea_not_cov;
	}
	public void setR194_crm_sub_app_col_cea_not_cov(BigDecimal r194_crm_sub_app_col_cea_not_cov) {
		this.r194_crm_sub_app_col_cea_not_cov = r194_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR194_crm_sub_app_rwa_ris_wei_crm() {
		return r194_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR194_crm_sub_app_rwa_ris_wei_crm(BigDecimal r194_crm_sub_app_rwa_ris_wei_crm) {
		this.r194_crm_sub_app_rwa_ris_wei_crm = r194_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR194_crm_sub_app_rwa_ris_cea_cov() {
		return r194_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR194_crm_sub_app_rwa_ris_cea_cov(BigDecimal r194_crm_sub_app_rwa_ris_cea_cov) {
		this.r194_crm_sub_app_rwa_ris_cea_cov = r194_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR194_crm_sub_app_rwa_appl_org_coun() {
		return r194_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR194_crm_sub_app_rwa_appl_org_coun(BigDecimal r194_crm_sub_app_rwa_appl_org_coun) {
		this.r194_crm_sub_app_rwa_appl_org_coun = r194_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR194_crm_sub_app_rwa_ris_cea_not_cov() {
		return r194_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR194_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r194_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r194_crm_sub_app_rwa_ris_cea_not_cov = r194_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR194_crm_com_app_col_cea_elig_crm() {
		return r194_crm_com_app_col_cea_elig_crm;
	}
	public void setR194_crm_com_app_col_cea_elig_crm(BigDecimal r194_crm_com_app_col_cea_elig_crm) {
		this.r194_crm_com_app_col_cea_elig_crm = r194_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR194_crm_com_app_col_elig_cea_vol_adj() {
		return r194_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR194_crm_com_app_col_elig_cea_vol_adj(BigDecimal r194_crm_com_app_col_elig_cea_vol_adj) {
		this.r194_crm_com_app_col_elig_cea_vol_adj = r194_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR194_crm_com_app_col_elig_fin_hai() {
		return r194_crm_com_app_col_elig_fin_hai;
	}
	public void setR194_crm_com_app_col_elig_fin_hai(BigDecimal r194_crm_com_app_col_elig_fin_hai) {
		this.r194_crm_com_app_col_elig_fin_hai = r194_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR194_crm_com_app_col_cea_val_aft_crm() {
		return r194_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR194_crm_com_app_col_cea_val_aft_crm(BigDecimal r194_crm_com_app_col_cea_val_aft_crm) {
		this.r194_crm_com_app_col_cea_val_aft_crm = r194_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR194_rwa_elig_cea_not_cov() {
		return r194_rwa_elig_cea_not_cov;
	}
	public void setR194_rwa_elig_cea_not_cov(BigDecimal r194_rwa_elig_cea_not_cov) {
		this.r194_rwa_elig_cea_not_cov = r194_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR194_rwa_unsec_cea_sub_cre_ris() {
		return r194_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR194_rwa_unsec_cea_sub_cre_ris(BigDecimal r194_rwa_unsec_cea_sub_cre_ris) {
		this.r194_rwa_unsec_cea_sub_cre_ris = r194_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR194_rwa_unsec_cea() {
		return r194_rwa_unsec_cea;
	}
	public void setR194_rwa_unsec_cea(BigDecimal r194_rwa_unsec_cea) {
		this.r194_rwa_unsec_cea = r194_rwa_unsec_cea;
	}
	public BigDecimal getR194_rwa_tot_ris_wei_ass() {
		return r194_rwa_tot_ris_wei_ass;
	}
	public void setR194_rwa_tot_ris_wei_ass(BigDecimal r194_rwa_tot_ris_wei_ass) {
		this.r194_rwa_tot_ris_wei_ass = r194_rwa_tot_ris_wei_ass;
	}
	public String getR195_exposure_class_off_bal() {
		return r195_exposure_class_off_bal;
	}
	public void setR195_exposure_class_off_bal(String r195_exposure_class_off_bal) {
		this.r195_exposure_class_off_bal = r195_exposure_class_off_bal;
	}
	public BigDecimal getR195_nom_pri_amt() {
		return r195_nom_pri_amt;
	}
	public void setR195_nom_pri_amt(BigDecimal r195_nom_pri_amt) {
		this.r195_nom_pri_amt = r195_nom_pri_amt;
	}
	public BigDecimal getR195_ccf() {
		return r195_ccf;
	}
	public void setR195_ccf(BigDecimal r195_ccf) {
		this.r195_ccf = r195_ccf;
	}
	public BigDecimal getR195_cea() {
		return r195_cea;
	}
	public void setR195_cea(BigDecimal r195_cea) {
		this.r195_cea = r195_cea;
	}
	public BigDecimal getR195_cea_elig_coun_bilt_net() {
		return r195_cea_elig_coun_bilt_net;
	}
	public void setR195_cea_elig_coun_bilt_net(BigDecimal r195_cea_elig_coun_bilt_net) {
		this.r195_cea_elig_coun_bilt_net = r195_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR195_cea_aft_net() {
		return r195_cea_aft_net;
	}
	public void setR195_cea_aft_net(BigDecimal r195_cea_aft_net) {
		this.r195_cea_aft_net = r195_cea_aft_net;
	}
	public BigDecimal getR195_crm_sub_app_cea_elig() {
		return r195_crm_sub_app_cea_elig;
	}
	public void setR195_crm_sub_app_cea_elig(BigDecimal r195_crm_sub_app_cea_elig) {
		this.r195_crm_sub_app_cea_elig = r195_crm_sub_app_cea_elig;
	}
	public BigDecimal getR195_crm_sub_app_non_col_guar_elig() {
		return r195_crm_sub_app_non_col_guar_elig;
	}
	public void setR195_crm_sub_app_non_col_guar_elig(BigDecimal r195_crm_sub_app_non_col_guar_elig) {
		this.r195_crm_sub_app_non_col_guar_elig = r195_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR195_crm_sub_app_non_col_cre_der() {
		return r195_crm_sub_app_non_col_cre_der;
	}
	public void setR195_crm_sub_app_non_col_cre_der(BigDecimal r195_crm_sub_app_non_col_cre_der) {
		this.r195_crm_sub_app_non_col_cre_der = r195_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR195_crm_sub_app_col_elig_cash() {
		return r195_crm_sub_app_col_elig_cash;
	}
	public void setR195_crm_sub_app_col_elig_cash(BigDecimal r195_crm_sub_app_col_elig_cash) {
		this.r195_crm_sub_app_col_elig_cash = r195_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR195_crm_sub_app_col_elig_tre_bills() {
		return r195_crm_sub_app_col_elig_tre_bills;
	}
	public void setR195_crm_sub_app_col_elig_tre_bills(BigDecimal r195_crm_sub_app_col_elig_tre_bills) {
		this.r195_crm_sub_app_col_elig_tre_bills = r195_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR195_crm_sub_app_col_elig_deb_sec() {
		return r195_crm_sub_app_col_elig_deb_sec;
	}
	public void setR195_crm_sub_app_col_elig_deb_sec(BigDecimal r195_crm_sub_app_col_elig_deb_sec) {
		this.r195_crm_sub_app_col_elig_deb_sec = r195_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR195_crm_sub_app_col_elig_euiq() {
		return r195_crm_sub_app_col_elig_euiq;
	}
	public void setR195_crm_sub_app_col_elig_euiq(BigDecimal r195_crm_sub_app_col_elig_euiq) {
		this.r195_crm_sub_app_col_elig_euiq = r195_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR195_crm_sub_app_col_elig_uni_tru() {
		return r195_crm_sub_app_col_elig_uni_tru;
	}
	public void setR195_crm_sub_app_col_elig_uni_tru(BigDecimal r195_crm_sub_app_col_elig_uni_tru) {
		this.r195_crm_sub_app_col_elig_uni_tru = r195_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR195_crm_sub_app_col_cea_cov() {
		return r195_crm_sub_app_col_cea_cov;
	}
	public void setR195_crm_sub_app_col_cea_cov(BigDecimal r195_crm_sub_app_col_cea_cov) {
		this.r195_crm_sub_app_col_cea_cov = r195_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR195_crm_sub_app_col_cea_not_cov() {
		return r195_crm_sub_app_col_cea_not_cov;
	}
	public void setR195_crm_sub_app_col_cea_not_cov(BigDecimal r195_crm_sub_app_col_cea_not_cov) {
		this.r195_crm_sub_app_col_cea_not_cov = r195_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR195_crm_sub_app_rwa_ris_wei_crm() {
		return r195_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR195_crm_sub_app_rwa_ris_wei_crm(BigDecimal r195_crm_sub_app_rwa_ris_wei_crm) {
		this.r195_crm_sub_app_rwa_ris_wei_crm = r195_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR195_crm_sub_app_rwa_ris_cea_cov() {
		return r195_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR195_crm_sub_app_rwa_ris_cea_cov(BigDecimal r195_crm_sub_app_rwa_ris_cea_cov) {
		this.r195_crm_sub_app_rwa_ris_cea_cov = r195_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR195_crm_sub_app_rwa_appl_org_coun() {
		return r195_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR195_crm_sub_app_rwa_appl_org_coun(BigDecimal r195_crm_sub_app_rwa_appl_org_coun) {
		this.r195_crm_sub_app_rwa_appl_org_coun = r195_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR195_crm_sub_app_rwa_ris_cea_not_cov() {
		return r195_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR195_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r195_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r195_crm_sub_app_rwa_ris_cea_not_cov = r195_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR195_crm_com_app_col_cea_elig_crm() {
		return r195_crm_com_app_col_cea_elig_crm;
	}
	public void setR195_crm_com_app_col_cea_elig_crm(BigDecimal r195_crm_com_app_col_cea_elig_crm) {
		this.r195_crm_com_app_col_cea_elig_crm = r195_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR195_crm_com_app_col_elig_cea_vol_adj() {
		return r195_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR195_crm_com_app_col_elig_cea_vol_adj(BigDecimal r195_crm_com_app_col_elig_cea_vol_adj) {
		this.r195_crm_com_app_col_elig_cea_vol_adj = r195_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR195_crm_com_app_col_elig_fin_hai() {
		return r195_crm_com_app_col_elig_fin_hai;
	}
	public void setR195_crm_com_app_col_elig_fin_hai(BigDecimal r195_crm_com_app_col_elig_fin_hai) {
		this.r195_crm_com_app_col_elig_fin_hai = r195_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR195_crm_com_app_col_cea_val_aft_crm() {
		return r195_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR195_crm_com_app_col_cea_val_aft_crm(BigDecimal r195_crm_com_app_col_cea_val_aft_crm) {
		this.r195_crm_com_app_col_cea_val_aft_crm = r195_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR195_rwa_elig_cea_not_cov() {
		return r195_rwa_elig_cea_not_cov;
	}
	public void setR195_rwa_elig_cea_not_cov(BigDecimal r195_rwa_elig_cea_not_cov) {
		this.r195_rwa_elig_cea_not_cov = r195_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR195_rwa_unsec_cea_sub_cre_ris() {
		return r195_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR195_rwa_unsec_cea_sub_cre_ris(BigDecimal r195_rwa_unsec_cea_sub_cre_ris) {
		this.r195_rwa_unsec_cea_sub_cre_ris = r195_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR195_rwa_unsec_cea() {
		return r195_rwa_unsec_cea;
	}
	public void setR195_rwa_unsec_cea(BigDecimal r195_rwa_unsec_cea) {
		this.r195_rwa_unsec_cea = r195_rwa_unsec_cea;
	}
	public BigDecimal getR195_rwa_tot_ris_wei_ass() {
		return r195_rwa_tot_ris_wei_ass;
	}
	public void setR195_rwa_tot_ris_wei_ass(BigDecimal r195_rwa_tot_ris_wei_ass) {
		this.r195_rwa_tot_ris_wei_ass = r195_rwa_tot_ris_wei_ass;
	}
	public String getR196_exposure_class_off_bal() {
		return r196_exposure_class_off_bal;
	}
	public void setR196_exposure_class_off_bal(String r196_exposure_class_off_bal) {
		this.r196_exposure_class_off_bal = r196_exposure_class_off_bal;
	}
	public BigDecimal getR196_nom_pri_amt() {
		return r196_nom_pri_amt;
	}
	public void setR196_nom_pri_amt(BigDecimal r196_nom_pri_amt) {
		this.r196_nom_pri_amt = r196_nom_pri_amt;
	}
	public BigDecimal getR196_ccf() {
		return r196_ccf;
	}
	public void setR196_ccf(BigDecimal r196_ccf) {
		this.r196_ccf = r196_ccf;
	}
	public BigDecimal getR196_cea() {
		return r196_cea;
	}
	public void setR196_cea(BigDecimal r196_cea) {
		this.r196_cea = r196_cea;
	}
	public BigDecimal getR196_cea_elig_coun_bilt_net() {
		return r196_cea_elig_coun_bilt_net;
	}
	public void setR196_cea_elig_coun_bilt_net(BigDecimal r196_cea_elig_coun_bilt_net) {
		this.r196_cea_elig_coun_bilt_net = r196_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR196_cea_aft_net() {
		return r196_cea_aft_net;
	}
	public void setR196_cea_aft_net(BigDecimal r196_cea_aft_net) {
		this.r196_cea_aft_net = r196_cea_aft_net;
	}
	public BigDecimal getR196_crm_sub_app_cea_elig() {
		return r196_crm_sub_app_cea_elig;
	}
	public void setR196_crm_sub_app_cea_elig(BigDecimal r196_crm_sub_app_cea_elig) {
		this.r196_crm_sub_app_cea_elig = r196_crm_sub_app_cea_elig;
	}
	public BigDecimal getR196_crm_sub_app_non_col_guar_elig() {
		return r196_crm_sub_app_non_col_guar_elig;
	}
	public void setR196_crm_sub_app_non_col_guar_elig(BigDecimal r196_crm_sub_app_non_col_guar_elig) {
		this.r196_crm_sub_app_non_col_guar_elig = r196_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR196_crm_sub_app_non_col_cre_der() {
		return r196_crm_sub_app_non_col_cre_der;
	}
	public void setR196_crm_sub_app_non_col_cre_der(BigDecimal r196_crm_sub_app_non_col_cre_der) {
		this.r196_crm_sub_app_non_col_cre_der = r196_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR196_crm_sub_app_col_elig_cash() {
		return r196_crm_sub_app_col_elig_cash;
	}
	public void setR196_crm_sub_app_col_elig_cash(BigDecimal r196_crm_sub_app_col_elig_cash) {
		this.r196_crm_sub_app_col_elig_cash = r196_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR196_crm_sub_app_col_elig_tre_bills() {
		return r196_crm_sub_app_col_elig_tre_bills;
	}
	public void setR196_crm_sub_app_col_elig_tre_bills(BigDecimal r196_crm_sub_app_col_elig_tre_bills) {
		this.r196_crm_sub_app_col_elig_tre_bills = r196_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR196_crm_sub_app_col_elig_deb_sec() {
		return r196_crm_sub_app_col_elig_deb_sec;
	}
	public void setR196_crm_sub_app_col_elig_deb_sec(BigDecimal r196_crm_sub_app_col_elig_deb_sec) {
		this.r196_crm_sub_app_col_elig_deb_sec = r196_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR196_crm_sub_app_col_elig_euiq() {
		return r196_crm_sub_app_col_elig_euiq;
	}
	public void setR196_crm_sub_app_col_elig_euiq(BigDecimal r196_crm_sub_app_col_elig_euiq) {
		this.r196_crm_sub_app_col_elig_euiq = r196_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR196_crm_sub_app_col_elig_uni_tru() {
		return r196_crm_sub_app_col_elig_uni_tru;
	}
	public void setR196_crm_sub_app_col_elig_uni_tru(BigDecimal r196_crm_sub_app_col_elig_uni_tru) {
		this.r196_crm_sub_app_col_elig_uni_tru = r196_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR196_crm_sub_app_col_cea_cov() {
		return r196_crm_sub_app_col_cea_cov;
	}
	public void setR196_crm_sub_app_col_cea_cov(BigDecimal r196_crm_sub_app_col_cea_cov) {
		this.r196_crm_sub_app_col_cea_cov = r196_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR196_crm_sub_app_col_cea_not_cov() {
		return r196_crm_sub_app_col_cea_not_cov;
	}
	public void setR196_crm_sub_app_col_cea_not_cov(BigDecimal r196_crm_sub_app_col_cea_not_cov) {
		this.r196_crm_sub_app_col_cea_not_cov = r196_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR196_crm_sub_app_rwa_ris_wei_crm() {
		return r196_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR196_crm_sub_app_rwa_ris_wei_crm(BigDecimal r196_crm_sub_app_rwa_ris_wei_crm) {
		this.r196_crm_sub_app_rwa_ris_wei_crm = r196_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR196_crm_sub_app_rwa_ris_cea_cov() {
		return r196_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR196_crm_sub_app_rwa_ris_cea_cov(BigDecimal r196_crm_sub_app_rwa_ris_cea_cov) {
		this.r196_crm_sub_app_rwa_ris_cea_cov = r196_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR196_crm_sub_app_rwa_appl_org_coun() {
		return r196_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR196_crm_sub_app_rwa_appl_org_coun(BigDecimal r196_crm_sub_app_rwa_appl_org_coun) {
		this.r196_crm_sub_app_rwa_appl_org_coun = r196_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR196_crm_sub_app_rwa_ris_cea_not_cov() {
		return r196_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR196_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r196_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r196_crm_sub_app_rwa_ris_cea_not_cov = r196_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR196_crm_com_app_col_cea_elig_crm() {
		return r196_crm_com_app_col_cea_elig_crm;
	}
	public void setR196_crm_com_app_col_cea_elig_crm(BigDecimal r196_crm_com_app_col_cea_elig_crm) {
		this.r196_crm_com_app_col_cea_elig_crm = r196_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR196_crm_com_app_col_elig_cea_vol_adj() {
		return r196_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR196_crm_com_app_col_elig_cea_vol_adj(BigDecimal r196_crm_com_app_col_elig_cea_vol_adj) {
		this.r196_crm_com_app_col_elig_cea_vol_adj = r196_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR196_crm_com_app_col_elig_fin_hai() {
		return r196_crm_com_app_col_elig_fin_hai;
	}
	public void setR196_crm_com_app_col_elig_fin_hai(BigDecimal r196_crm_com_app_col_elig_fin_hai) {
		this.r196_crm_com_app_col_elig_fin_hai = r196_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR196_crm_com_app_col_cea_val_aft_crm() {
		return r196_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR196_crm_com_app_col_cea_val_aft_crm(BigDecimal r196_crm_com_app_col_cea_val_aft_crm) {
		this.r196_crm_com_app_col_cea_val_aft_crm = r196_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR196_rwa_elig_cea_not_cov() {
		return r196_rwa_elig_cea_not_cov;
	}
	public void setR196_rwa_elig_cea_not_cov(BigDecimal r196_rwa_elig_cea_not_cov) {
		this.r196_rwa_elig_cea_not_cov = r196_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR196_rwa_unsec_cea_sub_cre_ris() {
		return r196_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR196_rwa_unsec_cea_sub_cre_ris(BigDecimal r196_rwa_unsec_cea_sub_cre_ris) {
		this.r196_rwa_unsec_cea_sub_cre_ris = r196_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR196_rwa_unsec_cea() {
		return r196_rwa_unsec_cea;
	}
	public void setR196_rwa_unsec_cea(BigDecimal r196_rwa_unsec_cea) {
		this.r196_rwa_unsec_cea = r196_rwa_unsec_cea;
	}
	public BigDecimal getR196_rwa_tot_ris_wei_ass() {
		return r196_rwa_tot_ris_wei_ass;
	}
	public void setR196_rwa_tot_ris_wei_ass(BigDecimal r196_rwa_tot_ris_wei_ass) {
		this.r196_rwa_tot_ris_wei_ass = r196_rwa_tot_ris_wei_ass;
	}
	public String getR197_exposure_class_off_bal() {
		return r197_exposure_class_off_bal;
	}
	public void setR197_exposure_class_off_bal(String r197_exposure_class_off_bal) {
		this.r197_exposure_class_off_bal = r197_exposure_class_off_bal;
	}
	public BigDecimal getR197_nom_pri_amt() {
		return r197_nom_pri_amt;
	}
	public void setR197_nom_pri_amt(BigDecimal r197_nom_pri_amt) {
		this.r197_nom_pri_amt = r197_nom_pri_amt;
	}
	public BigDecimal getR197_ccf() {
		return r197_ccf;
	}
	public void setR197_ccf(BigDecimal r197_ccf) {
		this.r197_ccf = r197_ccf;
	}
	public BigDecimal getR197_cea() {
		return r197_cea;
	}
	public void setR197_cea(BigDecimal r197_cea) {
		this.r197_cea = r197_cea;
	}
	public BigDecimal getR197_cea_elig_coun_bilt_net() {
		return r197_cea_elig_coun_bilt_net;
	}
	public void setR197_cea_elig_coun_bilt_net(BigDecimal r197_cea_elig_coun_bilt_net) {
		this.r197_cea_elig_coun_bilt_net = r197_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR197_cea_aft_net() {
		return r197_cea_aft_net;
	}
	public void setR197_cea_aft_net(BigDecimal r197_cea_aft_net) {
		this.r197_cea_aft_net = r197_cea_aft_net;
	}
	public BigDecimal getR197_crm_sub_app_cea_elig() {
		return r197_crm_sub_app_cea_elig;
	}
	public void setR197_crm_sub_app_cea_elig(BigDecimal r197_crm_sub_app_cea_elig) {
		this.r197_crm_sub_app_cea_elig = r197_crm_sub_app_cea_elig;
	}
	public BigDecimal getR197_crm_sub_app_non_col_guar_elig() {
		return r197_crm_sub_app_non_col_guar_elig;
	}
	public void setR197_crm_sub_app_non_col_guar_elig(BigDecimal r197_crm_sub_app_non_col_guar_elig) {
		this.r197_crm_sub_app_non_col_guar_elig = r197_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR197_crm_sub_app_non_col_cre_der() {
		return r197_crm_sub_app_non_col_cre_der;
	}
	public void setR197_crm_sub_app_non_col_cre_der(BigDecimal r197_crm_sub_app_non_col_cre_der) {
		this.r197_crm_sub_app_non_col_cre_der = r197_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR197_crm_sub_app_col_elig_cash() {
		return r197_crm_sub_app_col_elig_cash;
	}
	public void setR197_crm_sub_app_col_elig_cash(BigDecimal r197_crm_sub_app_col_elig_cash) {
		this.r197_crm_sub_app_col_elig_cash = r197_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR197_crm_sub_app_col_elig_tre_bills() {
		return r197_crm_sub_app_col_elig_tre_bills;
	}
	public void setR197_crm_sub_app_col_elig_tre_bills(BigDecimal r197_crm_sub_app_col_elig_tre_bills) {
		this.r197_crm_sub_app_col_elig_tre_bills = r197_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR197_crm_sub_app_col_elig_deb_sec() {
		return r197_crm_sub_app_col_elig_deb_sec;
	}
	public void setR197_crm_sub_app_col_elig_deb_sec(BigDecimal r197_crm_sub_app_col_elig_deb_sec) {
		this.r197_crm_sub_app_col_elig_deb_sec = r197_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR197_crm_sub_app_col_elig_euiq() {
		return r197_crm_sub_app_col_elig_euiq;
	}
	public void setR197_crm_sub_app_col_elig_euiq(BigDecimal r197_crm_sub_app_col_elig_euiq) {
		this.r197_crm_sub_app_col_elig_euiq = r197_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR197_crm_sub_app_col_elig_uni_tru() {
		return r197_crm_sub_app_col_elig_uni_tru;
	}
	public void setR197_crm_sub_app_col_elig_uni_tru(BigDecimal r197_crm_sub_app_col_elig_uni_tru) {
		this.r197_crm_sub_app_col_elig_uni_tru = r197_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR197_crm_sub_app_col_cea_cov() {
		return r197_crm_sub_app_col_cea_cov;
	}
	public void setR197_crm_sub_app_col_cea_cov(BigDecimal r197_crm_sub_app_col_cea_cov) {
		this.r197_crm_sub_app_col_cea_cov = r197_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR197_crm_sub_app_col_cea_not_cov() {
		return r197_crm_sub_app_col_cea_not_cov;
	}
	public void setR197_crm_sub_app_col_cea_not_cov(BigDecimal r197_crm_sub_app_col_cea_not_cov) {
		this.r197_crm_sub_app_col_cea_not_cov = r197_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR197_crm_sub_app_rwa_ris_wei_crm() {
		return r197_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR197_crm_sub_app_rwa_ris_wei_crm(BigDecimal r197_crm_sub_app_rwa_ris_wei_crm) {
		this.r197_crm_sub_app_rwa_ris_wei_crm = r197_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR197_crm_sub_app_rwa_ris_cea_cov() {
		return r197_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR197_crm_sub_app_rwa_ris_cea_cov(BigDecimal r197_crm_sub_app_rwa_ris_cea_cov) {
		this.r197_crm_sub_app_rwa_ris_cea_cov = r197_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR197_crm_sub_app_rwa_appl_org_coun() {
		return r197_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR197_crm_sub_app_rwa_appl_org_coun(BigDecimal r197_crm_sub_app_rwa_appl_org_coun) {
		this.r197_crm_sub_app_rwa_appl_org_coun = r197_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR197_crm_sub_app_rwa_ris_cea_not_cov() {
		return r197_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR197_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r197_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r197_crm_sub_app_rwa_ris_cea_not_cov = r197_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR197_crm_com_app_col_cea_elig_crm() {
		return r197_crm_com_app_col_cea_elig_crm;
	}
	public void setR197_crm_com_app_col_cea_elig_crm(BigDecimal r197_crm_com_app_col_cea_elig_crm) {
		this.r197_crm_com_app_col_cea_elig_crm = r197_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR197_crm_com_app_col_elig_cea_vol_adj() {
		return r197_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR197_crm_com_app_col_elig_cea_vol_adj(BigDecimal r197_crm_com_app_col_elig_cea_vol_adj) {
		this.r197_crm_com_app_col_elig_cea_vol_adj = r197_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR197_crm_com_app_col_elig_fin_hai() {
		return r197_crm_com_app_col_elig_fin_hai;
	}
	public void setR197_crm_com_app_col_elig_fin_hai(BigDecimal r197_crm_com_app_col_elig_fin_hai) {
		this.r197_crm_com_app_col_elig_fin_hai = r197_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR197_crm_com_app_col_cea_val_aft_crm() {
		return r197_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR197_crm_com_app_col_cea_val_aft_crm(BigDecimal r197_crm_com_app_col_cea_val_aft_crm) {
		this.r197_crm_com_app_col_cea_val_aft_crm = r197_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR197_rwa_elig_cea_not_cov() {
		return r197_rwa_elig_cea_not_cov;
	}
	public void setR197_rwa_elig_cea_not_cov(BigDecimal r197_rwa_elig_cea_not_cov) {
		this.r197_rwa_elig_cea_not_cov = r197_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR197_rwa_unsec_cea_sub_cre_ris() {
		return r197_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR197_rwa_unsec_cea_sub_cre_ris(BigDecimal r197_rwa_unsec_cea_sub_cre_ris) {
		this.r197_rwa_unsec_cea_sub_cre_ris = r197_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR197_rwa_unsec_cea() {
		return r197_rwa_unsec_cea;
	}
	public void setR197_rwa_unsec_cea(BigDecimal r197_rwa_unsec_cea) {
		this.r197_rwa_unsec_cea = r197_rwa_unsec_cea;
	}
	public BigDecimal getR197_rwa_tot_ris_wei_ass() {
		return r197_rwa_tot_ris_wei_ass;
	}
	public void setR197_rwa_tot_ris_wei_ass(BigDecimal r197_rwa_tot_ris_wei_ass) {
		this.r197_rwa_tot_ris_wei_ass = r197_rwa_tot_ris_wei_ass;
	}
	public String getR198_exposure_class_off_bal() {
		return r198_exposure_class_off_bal;
	}
	public void setR198_exposure_class_off_bal(String r198_exposure_class_off_bal) {
		this.r198_exposure_class_off_bal = r198_exposure_class_off_bal;
	}
	public BigDecimal getR198_nom_pri_amt() {
		return r198_nom_pri_amt;
	}
	public void setR198_nom_pri_amt(BigDecimal r198_nom_pri_amt) {
		this.r198_nom_pri_amt = r198_nom_pri_amt;
	}
	public BigDecimal getR198_ccf() {
		return r198_ccf;
	}
	public void setR198_ccf(BigDecimal r198_ccf) {
		this.r198_ccf = r198_ccf;
	}
	public BigDecimal getR198_cea() {
		return r198_cea;
	}
	public void setR198_cea(BigDecimal r198_cea) {
		this.r198_cea = r198_cea;
	}
	public BigDecimal getR198_cea_elig_coun_bilt_net() {
		return r198_cea_elig_coun_bilt_net;
	}
	public void setR198_cea_elig_coun_bilt_net(BigDecimal r198_cea_elig_coun_bilt_net) {
		this.r198_cea_elig_coun_bilt_net = r198_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR198_cea_aft_net() {
		return r198_cea_aft_net;
	}
	public void setR198_cea_aft_net(BigDecimal r198_cea_aft_net) {
		this.r198_cea_aft_net = r198_cea_aft_net;
	}
	public BigDecimal getR198_crm_sub_app_cea_elig() {
		return r198_crm_sub_app_cea_elig;
	}
	public void setR198_crm_sub_app_cea_elig(BigDecimal r198_crm_sub_app_cea_elig) {
		this.r198_crm_sub_app_cea_elig = r198_crm_sub_app_cea_elig;
	}
	public BigDecimal getR198_crm_sub_app_non_col_guar_elig() {
		return r198_crm_sub_app_non_col_guar_elig;
	}
	public void setR198_crm_sub_app_non_col_guar_elig(BigDecimal r198_crm_sub_app_non_col_guar_elig) {
		this.r198_crm_sub_app_non_col_guar_elig = r198_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR198_crm_sub_app_non_col_cre_der() {
		return r198_crm_sub_app_non_col_cre_der;
	}
	public void setR198_crm_sub_app_non_col_cre_der(BigDecimal r198_crm_sub_app_non_col_cre_der) {
		this.r198_crm_sub_app_non_col_cre_der = r198_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR198_crm_sub_app_col_elig_cash() {
		return r198_crm_sub_app_col_elig_cash;
	}
	public void setR198_crm_sub_app_col_elig_cash(BigDecimal r198_crm_sub_app_col_elig_cash) {
		this.r198_crm_sub_app_col_elig_cash = r198_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR198_crm_sub_app_col_elig_tre_bills() {
		return r198_crm_sub_app_col_elig_tre_bills;
	}
	public void setR198_crm_sub_app_col_elig_tre_bills(BigDecimal r198_crm_sub_app_col_elig_tre_bills) {
		this.r198_crm_sub_app_col_elig_tre_bills = r198_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR198_crm_sub_app_col_elig_deb_sec() {
		return r198_crm_sub_app_col_elig_deb_sec;
	}
	public void setR198_crm_sub_app_col_elig_deb_sec(BigDecimal r198_crm_sub_app_col_elig_deb_sec) {
		this.r198_crm_sub_app_col_elig_deb_sec = r198_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR198_crm_sub_app_col_elig_euiq() {
		return r198_crm_sub_app_col_elig_euiq;
	}
	public void setR198_crm_sub_app_col_elig_euiq(BigDecimal r198_crm_sub_app_col_elig_euiq) {
		this.r198_crm_sub_app_col_elig_euiq = r198_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR198_crm_sub_app_col_elig_uni_tru() {
		return r198_crm_sub_app_col_elig_uni_tru;
	}
	public void setR198_crm_sub_app_col_elig_uni_tru(BigDecimal r198_crm_sub_app_col_elig_uni_tru) {
		this.r198_crm_sub_app_col_elig_uni_tru = r198_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR198_crm_sub_app_col_cea_cov() {
		return r198_crm_sub_app_col_cea_cov;
	}
	public void setR198_crm_sub_app_col_cea_cov(BigDecimal r198_crm_sub_app_col_cea_cov) {
		this.r198_crm_sub_app_col_cea_cov = r198_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR198_crm_sub_app_col_cea_not_cov() {
		return r198_crm_sub_app_col_cea_not_cov;
	}
	public void setR198_crm_sub_app_col_cea_not_cov(BigDecimal r198_crm_sub_app_col_cea_not_cov) {
		this.r198_crm_sub_app_col_cea_not_cov = r198_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR198_crm_sub_app_rwa_ris_wei_crm() {
		return r198_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR198_crm_sub_app_rwa_ris_wei_crm(BigDecimal r198_crm_sub_app_rwa_ris_wei_crm) {
		this.r198_crm_sub_app_rwa_ris_wei_crm = r198_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR198_crm_sub_app_rwa_ris_cea_cov() {
		return r198_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR198_crm_sub_app_rwa_ris_cea_cov(BigDecimal r198_crm_sub_app_rwa_ris_cea_cov) {
		this.r198_crm_sub_app_rwa_ris_cea_cov = r198_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR198_crm_sub_app_rwa_appl_org_coun() {
		return r198_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR198_crm_sub_app_rwa_appl_org_coun(BigDecimal r198_crm_sub_app_rwa_appl_org_coun) {
		this.r198_crm_sub_app_rwa_appl_org_coun = r198_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR198_crm_sub_app_rwa_ris_cea_not_cov() {
		return r198_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR198_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r198_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r198_crm_sub_app_rwa_ris_cea_not_cov = r198_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR198_crm_com_app_col_cea_elig_crm() {
		return r198_crm_com_app_col_cea_elig_crm;
	}
	public void setR198_crm_com_app_col_cea_elig_crm(BigDecimal r198_crm_com_app_col_cea_elig_crm) {
		this.r198_crm_com_app_col_cea_elig_crm = r198_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR198_crm_com_app_col_elig_cea_vol_adj() {
		return r198_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR198_crm_com_app_col_elig_cea_vol_adj(BigDecimal r198_crm_com_app_col_elig_cea_vol_adj) {
		this.r198_crm_com_app_col_elig_cea_vol_adj = r198_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR198_crm_com_app_col_elig_fin_hai() {
		return r198_crm_com_app_col_elig_fin_hai;
	}
	public void setR198_crm_com_app_col_elig_fin_hai(BigDecimal r198_crm_com_app_col_elig_fin_hai) {
		this.r198_crm_com_app_col_elig_fin_hai = r198_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR198_crm_com_app_col_cea_val_aft_crm() {
		return r198_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR198_crm_com_app_col_cea_val_aft_crm(BigDecimal r198_crm_com_app_col_cea_val_aft_crm) {
		this.r198_crm_com_app_col_cea_val_aft_crm = r198_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR198_rwa_elig_cea_not_cov() {
		return r198_rwa_elig_cea_not_cov;
	}
	public void setR198_rwa_elig_cea_not_cov(BigDecimal r198_rwa_elig_cea_not_cov) {
		this.r198_rwa_elig_cea_not_cov = r198_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR198_rwa_unsec_cea_sub_cre_ris() {
		return r198_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR198_rwa_unsec_cea_sub_cre_ris(BigDecimal r198_rwa_unsec_cea_sub_cre_ris) {
		this.r198_rwa_unsec_cea_sub_cre_ris = r198_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR198_rwa_unsec_cea() {
		return r198_rwa_unsec_cea;
	}
	public void setR198_rwa_unsec_cea(BigDecimal r198_rwa_unsec_cea) {
		this.r198_rwa_unsec_cea = r198_rwa_unsec_cea;
	}
	public BigDecimal getR198_rwa_tot_ris_wei_ass() {
		return r198_rwa_tot_ris_wei_ass;
	}
	public void setR198_rwa_tot_ris_wei_ass(BigDecimal r198_rwa_tot_ris_wei_ass) {
		this.r198_rwa_tot_ris_wei_ass = r198_rwa_tot_ris_wei_ass;
	}
	public String getR199_exposure_class_off_bal() {
		return r199_exposure_class_off_bal;
	}
	public void setR199_exposure_class_off_bal(String r199_exposure_class_off_bal) {
		this.r199_exposure_class_off_bal = r199_exposure_class_off_bal;
	}
	public BigDecimal getR199_nom_pri_amt() {
		return r199_nom_pri_amt;
	}
	public void setR199_nom_pri_amt(BigDecimal r199_nom_pri_amt) {
		this.r199_nom_pri_amt = r199_nom_pri_amt;
	}
	public BigDecimal getR199_ccf() {
		return r199_ccf;
	}
	public void setR199_ccf(BigDecimal r199_ccf) {
		this.r199_ccf = r199_ccf;
	}
	public BigDecimal getR199_cea() {
		return r199_cea;
	}
	public void setR199_cea(BigDecimal r199_cea) {
		this.r199_cea = r199_cea;
	}
	public BigDecimal getR199_cea_elig_coun_bilt_net() {
		return r199_cea_elig_coun_bilt_net;
	}
	public void setR199_cea_elig_coun_bilt_net(BigDecimal r199_cea_elig_coun_bilt_net) {
		this.r199_cea_elig_coun_bilt_net = r199_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR199_cea_aft_net() {
		return r199_cea_aft_net;
	}
	public void setR199_cea_aft_net(BigDecimal r199_cea_aft_net) {
		this.r199_cea_aft_net = r199_cea_aft_net;
	}
	public BigDecimal getR199_crm_sub_app_cea_elig() {
		return r199_crm_sub_app_cea_elig;
	}
	public void setR199_crm_sub_app_cea_elig(BigDecimal r199_crm_sub_app_cea_elig) {
		this.r199_crm_sub_app_cea_elig = r199_crm_sub_app_cea_elig;
	}
	public BigDecimal getR199_crm_sub_app_non_col_guar_elig() {
		return r199_crm_sub_app_non_col_guar_elig;
	}
	public void setR199_crm_sub_app_non_col_guar_elig(BigDecimal r199_crm_sub_app_non_col_guar_elig) {
		this.r199_crm_sub_app_non_col_guar_elig = r199_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR199_crm_sub_app_non_col_cre_der() {
		return r199_crm_sub_app_non_col_cre_der;
	}
	public void setR199_crm_sub_app_non_col_cre_der(BigDecimal r199_crm_sub_app_non_col_cre_der) {
		this.r199_crm_sub_app_non_col_cre_der = r199_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR199_crm_sub_app_col_elig_cash() {
		return r199_crm_sub_app_col_elig_cash;
	}
	public void setR199_crm_sub_app_col_elig_cash(BigDecimal r199_crm_sub_app_col_elig_cash) {
		this.r199_crm_sub_app_col_elig_cash = r199_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR199_crm_sub_app_col_elig_tre_bills() {
		return r199_crm_sub_app_col_elig_tre_bills;
	}
	public void setR199_crm_sub_app_col_elig_tre_bills(BigDecimal r199_crm_sub_app_col_elig_tre_bills) {
		this.r199_crm_sub_app_col_elig_tre_bills = r199_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR199_crm_sub_app_col_elig_deb_sec() {
		return r199_crm_sub_app_col_elig_deb_sec;
	}
	public void setR199_crm_sub_app_col_elig_deb_sec(BigDecimal r199_crm_sub_app_col_elig_deb_sec) {
		this.r199_crm_sub_app_col_elig_deb_sec = r199_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR199_crm_sub_app_col_elig_euiq() {
		return r199_crm_sub_app_col_elig_euiq;
	}
	public void setR199_crm_sub_app_col_elig_euiq(BigDecimal r199_crm_sub_app_col_elig_euiq) {
		this.r199_crm_sub_app_col_elig_euiq = r199_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR199_crm_sub_app_col_elig_uni_tru() {
		return r199_crm_sub_app_col_elig_uni_tru;
	}
	public void setR199_crm_sub_app_col_elig_uni_tru(BigDecimal r199_crm_sub_app_col_elig_uni_tru) {
		this.r199_crm_sub_app_col_elig_uni_tru = r199_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR199_crm_sub_app_col_cea_cov() {
		return r199_crm_sub_app_col_cea_cov;
	}
	public void setR199_crm_sub_app_col_cea_cov(BigDecimal r199_crm_sub_app_col_cea_cov) {
		this.r199_crm_sub_app_col_cea_cov = r199_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR199_crm_sub_app_col_cea_not_cov() {
		return r199_crm_sub_app_col_cea_not_cov;
	}
	public void setR199_crm_sub_app_col_cea_not_cov(BigDecimal r199_crm_sub_app_col_cea_not_cov) {
		this.r199_crm_sub_app_col_cea_not_cov = r199_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR199_crm_sub_app_rwa_ris_wei_crm() {
		return r199_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR199_crm_sub_app_rwa_ris_wei_crm(BigDecimal r199_crm_sub_app_rwa_ris_wei_crm) {
		this.r199_crm_sub_app_rwa_ris_wei_crm = r199_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR199_crm_sub_app_rwa_ris_cea_cov() {
		return r199_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR199_crm_sub_app_rwa_ris_cea_cov(BigDecimal r199_crm_sub_app_rwa_ris_cea_cov) {
		this.r199_crm_sub_app_rwa_ris_cea_cov = r199_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR199_crm_sub_app_rwa_appl_org_coun() {
		return r199_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR199_crm_sub_app_rwa_appl_org_coun(BigDecimal r199_crm_sub_app_rwa_appl_org_coun) {
		this.r199_crm_sub_app_rwa_appl_org_coun = r199_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR199_crm_sub_app_rwa_ris_cea_not_cov() {
		return r199_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR199_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r199_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r199_crm_sub_app_rwa_ris_cea_not_cov = r199_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR199_crm_com_app_col_cea_elig_crm() {
		return r199_crm_com_app_col_cea_elig_crm;
	}
	public void setR199_crm_com_app_col_cea_elig_crm(BigDecimal r199_crm_com_app_col_cea_elig_crm) {
		this.r199_crm_com_app_col_cea_elig_crm = r199_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR199_crm_com_app_col_elig_cea_vol_adj() {
		return r199_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR199_crm_com_app_col_elig_cea_vol_adj(BigDecimal r199_crm_com_app_col_elig_cea_vol_adj) {
		this.r199_crm_com_app_col_elig_cea_vol_adj = r199_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR199_crm_com_app_col_elig_fin_hai() {
		return r199_crm_com_app_col_elig_fin_hai;
	}
	public void setR199_crm_com_app_col_elig_fin_hai(BigDecimal r199_crm_com_app_col_elig_fin_hai) {
		this.r199_crm_com_app_col_elig_fin_hai = r199_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR199_crm_com_app_col_cea_val_aft_crm() {
		return r199_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR199_crm_com_app_col_cea_val_aft_crm(BigDecimal r199_crm_com_app_col_cea_val_aft_crm) {
		this.r199_crm_com_app_col_cea_val_aft_crm = r199_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR199_rwa_elig_cea_not_cov() {
		return r199_rwa_elig_cea_not_cov;
	}
	public void setR199_rwa_elig_cea_not_cov(BigDecimal r199_rwa_elig_cea_not_cov) {
		this.r199_rwa_elig_cea_not_cov = r199_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR199_rwa_unsec_cea_sub_cre_ris() {
		return r199_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR199_rwa_unsec_cea_sub_cre_ris(BigDecimal r199_rwa_unsec_cea_sub_cre_ris) {
		this.r199_rwa_unsec_cea_sub_cre_ris = r199_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR199_rwa_unsec_cea() {
		return r199_rwa_unsec_cea;
	}
	public void setR199_rwa_unsec_cea(BigDecimal r199_rwa_unsec_cea) {
		this.r199_rwa_unsec_cea = r199_rwa_unsec_cea;
	}
	public BigDecimal getR199_rwa_tot_ris_wei_ass() {
		return r199_rwa_tot_ris_wei_ass;
	}
	public void setR199_rwa_tot_ris_wei_ass(BigDecimal r199_rwa_tot_ris_wei_ass) {
		this.r199_rwa_tot_ris_wei_ass = r199_rwa_tot_ris_wei_ass;
	}
	public String getR200_exposure_class_off_bal() {
		return r200_exposure_class_off_bal;
	}
	public void setR200_exposure_class_off_bal(String r200_exposure_class_off_bal) {
		this.r200_exposure_class_off_bal = r200_exposure_class_off_bal;
	}
	public BigDecimal getR200_nom_pri_amt() {
		return r200_nom_pri_amt;
	}
	public void setR200_nom_pri_amt(BigDecimal r200_nom_pri_amt) {
		this.r200_nom_pri_amt = r200_nom_pri_amt;
	}
	public BigDecimal getR200_ccf() {
		return r200_ccf;
	}
	public void setR200_ccf(BigDecimal r200_ccf) {
		this.r200_ccf = r200_ccf;
	}
	public BigDecimal getR200_cea() {
		return r200_cea;
	}
	public void setR200_cea(BigDecimal r200_cea) {
		this.r200_cea = r200_cea;
	}
	public BigDecimal getR200_cea_elig_coun_bilt_net() {
		return r200_cea_elig_coun_bilt_net;
	}
	public void setR200_cea_elig_coun_bilt_net(BigDecimal r200_cea_elig_coun_bilt_net) {
		this.r200_cea_elig_coun_bilt_net = r200_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR200_cea_aft_net() {
		return r200_cea_aft_net;
	}
	public void setR200_cea_aft_net(BigDecimal r200_cea_aft_net) {
		this.r200_cea_aft_net = r200_cea_aft_net;
	}
	public BigDecimal getR200_crm_sub_app_cea_elig() {
		return r200_crm_sub_app_cea_elig;
	}
	public void setR200_crm_sub_app_cea_elig(BigDecimal r200_crm_sub_app_cea_elig) {
		this.r200_crm_sub_app_cea_elig = r200_crm_sub_app_cea_elig;
	}
	public BigDecimal getR200_crm_sub_app_non_col_guar_elig() {
		return r200_crm_sub_app_non_col_guar_elig;
	}
	public void setR200_crm_sub_app_non_col_guar_elig(BigDecimal r200_crm_sub_app_non_col_guar_elig) {
		this.r200_crm_sub_app_non_col_guar_elig = r200_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR200_crm_sub_app_non_col_cre_der() {
		return r200_crm_sub_app_non_col_cre_der;
	}
	public void setR200_crm_sub_app_non_col_cre_der(BigDecimal r200_crm_sub_app_non_col_cre_der) {
		this.r200_crm_sub_app_non_col_cre_der = r200_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR200_crm_sub_app_col_elig_cash() {
		return r200_crm_sub_app_col_elig_cash;
	}
	public void setR200_crm_sub_app_col_elig_cash(BigDecimal r200_crm_sub_app_col_elig_cash) {
		this.r200_crm_sub_app_col_elig_cash = r200_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR200_crm_sub_app_col_elig_tre_bills() {
		return r200_crm_sub_app_col_elig_tre_bills;
	}
	public void setR200_crm_sub_app_col_elig_tre_bills(BigDecimal r200_crm_sub_app_col_elig_tre_bills) {
		this.r200_crm_sub_app_col_elig_tre_bills = r200_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR200_crm_sub_app_col_elig_deb_sec() {
		return r200_crm_sub_app_col_elig_deb_sec;
	}
	public void setR200_crm_sub_app_col_elig_deb_sec(BigDecimal r200_crm_sub_app_col_elig_deb_sec) {
		this.r200_crm_sub_app_col_elig_deb_sec = r200_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR200_crm_sub_app_col_elig_euiq() {
		return r200_crm_sub_app_col_elig_euiq;
	}
	public void setR200_crm_sub_app_col_elig_euiq(BigDecimal r200_crm_sub_app_col_elig_euiq) {
		this.r200_crm_sub_app_col_elig_euiq = r200_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR200_crm_sub_app_col_elig_uni_tru() {
		return r200_crm_sub_app_col_elig_uni_tru;
	}
	public void setR200_crm_sub_app_col_elig_uni_tru(BigDecimal r200_crm_sub_app_col_elig_uni_tru) {
		this.r200_crm_sub_app_col_elig_uni_tru = r200_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR200_crm_sub_app_col_cea_cov() {
		return r200_crm_sub_app_col_cea_cov;
	}
	public void setR200_crm_sub_app_col_cea_cov(BigDecimal r200_crm_sub_app_col_cea_cov) {
		this.r200_crm_sub_app_col_cea_cov = r200_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR200_crm_sub_app_col_cea_not_cov() {
		return r200_crm_sub_app_col_cea_not_cov;
	}
	public void setR200_crm_sub_app_col_cea_not_cov(BigDecimal r200_crm_sub_app_col_cea_not_cov) {
		this.r200_crm_sub_app_col_cea_not_cov = r200_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR200_crm_sub_app_rwa_ris_wei_crm() {
		return r200_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR200_crm_sub_app_rwa_ris_wei_crm(BigDecimal r200_crm_sub_app_rwa_ris_wei_crm) {
		this.r200_crm_sub_app_rwa_ris_wei_crm = r200_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR200_crm_sub_app_rwa_ris_cea_cov() {
		return r200_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR200_crm_sub_app_rwa_ris_cea_cov(BigDecimal r200_crm_sub_app_rwa_ris_cea_cov) {
		this.r200_crm_sub_app_rwa_ris_cea_cov = r200_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR200_crm_sub_app_rwa_appl_org_coun() {
		return r200_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR200_crm_sub_app_rwa_appl_org_coun(BigDecimal r200_crm_sub_app_rwa_appl_org_coun) {
		this.r200_crm_sub_app_rwa_appl_org_coun = r200_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR200_crm_sub_app_rwa_ris_cea_not_cov() {
		return r200_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR200_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r200_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r200_crm_sub_app_rwa_ris_cea_not_cov = r200_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR200_crm_com_app_col_cea_elig_crm() {
		return r200_crm_com_app_col_cea_elig_crm;
	}
	public void setR200_crm_com_app_col_cea_elig_crm(BigDecimal r200_crm_com_app_col_cea_elig_crm) {
		this.r200_crm_com_app_col_cea_elig_crm = r200_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR200_crm_com_app_col_elig_cea_vol_adj() {
		return r200_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR200_crm_com_app_col_elig_cea_vol_adj(BigDecimal r200_crm_com_app_col_elig_cea_vol_adj) {
		this.r200_crm_com_app_col_elig_cea_vol_adj = r200_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR200_crm_com_app_col_elig_fin_hai() {
		return r200_crm_com_app_col_elig_fin_hai;
	}
	public void setR200_crm_com_app_col_elig_fin_hai(BigDecimal r200_crm_com_app_col_elig_fin_hai) {
		this.r200_crm_com_app_col_elig_fin_hai = r200_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR200_crm_com_app_col_cea_val_aft_crm() {
		return r200_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR200_crm_com_app_col_cea_val_aft_crm(BigDecimal r200_crm_com_app_col_cea_val_aft_crm) {
		this.r200_crm_com_app_col_cea_val_aft_crm = r200_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR200_rwa_elig_cea_not_cov() {
		return r200_rwa_elig_cea_not_cov;
	}
	public void setR200_rwa_elig_cea_not_cov(BigDecimal r200_rwa_elig_cea_not_cov) {
		this.r200_rwa_elig_cea_not_cov = r200_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR200_rwa_unsec_cea_sub_cre_ris() {
		return r200_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR200_rwa_unsec_cea_sub_cre_ris(BigDecimal r200_rwa_unsec_cea_sub_cre_ris) {
		this.r200_rwa_unsec_cea_sub_cre_ris = r200_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR200_rwa_unsec_cea() {
		return r200_rwa_unsec_cea;
	}
	public void setR200_rwa_unsec_cea(BigDecimal r200_rwa_unsec_cea) {
		this.r200_rwa_unsec_cea = r200_rwa_unsec_cea;
	}
	public BigDecimal getR200_rwa_tot_ris_wei_ass() {
		return r200_rwa_tot_ris_wei_ass;
	}
	public void setR200_rwa_tot_ris_wei_ass(BigDecimal r200_rwa_tot_ris_wei_ass) {
		this.r200_rwa_tot_ris_wei_ass = r200_rwa_tot_ris_wei_ass;
	}
	public String getR201_exposure_class_off_bal() {
		return r201_exposure_class_off_bal;
	}
	public void setR201_exposure_class_off_bal(String r201_exposure_class_off_bal) {
		this.r201_exposure_class_off_bal = r201_exposure_class_off_bal;
	}
	public BigDecimal getR201_nom_pri_amt() {
		return r201_nom_pri_amt;
	}
	public void setR201_nom_pri_amt(BigDecimal r201_nom_pri_amt) {
		this.r201_nom_pri_amt = r201_nom_pri_amt;
	}
	public BigDecimal getR201_ccf() {
		return r201_ccf;
	}
	public void setR201_ccf(BigDecimal r201_ccf) {
		this.r201_ccf = r201_ccf;
	}
	public BigDecimal getR201_cea() {
		return r201_cea;
	}
	public void setR201_cea(BigDecimal r201_cea) {
		this.r201_cea = r201_cea;
	}
	public BigDecimal getR201_cea_elig_coun_bilt_net() {
		return r201_cea_elig_coun_bilt_net;
	}
	public void setR201_cea_elig_coun_bilt_net(BigDecimal r201_cea_elig_coun_bilt_net) {
		this.r201_cea_elig_coun_bilt_net = r201_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR201_cea_aft_net() {
		return r201_cea_aft_net;
	}
	public void setR201_cea_aft_net(BigDecimal r201_cea_aft_net) {
		this.r201_cea_aft_net = r201_cea_aft_net;
	}
	public BigDecimal getR201_crm_sub_app_cea_elig() {
		return r201_crm_sub_app_cea_elig;
	}
	public void setR201_crm_sub_app_cea_elig(BigDecimal r201_crm_sub_app_cea_elig) {
		this.r201_crm_sub_app_cea_elig = r201_crm_sub_app_cea_elig;
	}
	public BigDecimal getR201_crm_sub_app_non_col_guar_elig() {
		return r201_crm_sub_app_non_col_guar_elig;
	}
	public void setR201_crm_sub_app_non_col_guar_elig(BigDecimal r201_crm_sub_app_non_col_guar_elig) {
		this.r201_crm_sub_app_non_col_guar_elig = r201_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR201_crm_sub_app_non_col_cre_der() {
		return r201_crm_sub_app_non_col_cre_der;
	}
	public void setR201_crm_sub_app_non_col_cre_der(BigDecimal r201_crm_sub_app_non_col_cre_der) {
		this.r201_crm_sub_app_non_col_cre_der = r201_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR201_crm_sub_app_col_elig_cash() {
		return r201_crm_sub_app_col_elig_cash;
	}
	public void setR201_crm_sub_app_col_elig_cash(BigDecimal r201_crm_sub_app_col_elig_cash) {
		this.r201_crm_sub_app_col_elig_cash = r201_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR201_crm_sub_app_col_elig_tre_bills() {
		return r201_crm_sub_app_col_elig_tre_bills;
	}
	public void setR201_crm_sub_app_col_elig_tre_bills(BigDecimal r201_crm_sub_app_col_elig_tre_bills) {
		this.r201_crm_sub_app_col_elig_tre_bills = r201_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR201_crm_sub_app_col_elig_deb_sec() {
		return r201_crm_sub_app_col_elig_deb_sec;
	}
	public void setR201_crm_sub_app_col_elig_deb_sec(BigDecimal r201_crm_sub_app_col_elig_deb_sec) {
		this.r201_crm_sub_app_col_elig_deb_sec = r201_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR201_crm_sub_app_col_elig_euiq() {
		return r201_crm_sub_app_col_elig_euiq;
	}
	public void setR201_crm_sub_app_col_elig_euiq(BigDecimal r201_crm_sub_app_col_elig_euiq) {
		this.r201_crm_sub_app_col_elig_euiq = r201_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR201_crm_sub_app_col_elig_uni_tru() {
		return r201_crm_sub_app_col_elig_uni_tru;
	}
	public void setR201_crm_sub_app_col_elig_uni_tru(BigDecimal r201_crm_sub_app_col_elig_uni_tru) {
		this.r201_crm_sub_app_col_elig_uni_tru = r201_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR201_crm_sub_app_col_cea_cov() {
		return r201_crm_sub_app_col_cea_cov;
	}
	public void setR201_crm_sub_app_col_cea_cov(BigDecimal r201_crm_sub_app_col_cea_cov) {
		this.r201_crm_sub_app_col_cea_cov = r201_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR201_crm_sub_app_col_cea_not_cov() {
		return r201_crm_sub_app_col_cea_not_cov;
	}
	public void setR201_crm_sub_app_col_cea_not_cov(BigDecimal r201_crm_sub_app_col_cea_not_cov) {
		this.r201_crm_sub_app_col_cea_not_cov = r201_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR201_crm_sub_app_rwa_ris_wei_crm() {
		return r201_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR201_crm_sub_app_rwa_ris_wei_crm(BigDecimal r201_crm_sub_app_rwa_ris_wei_crm) {
		this.r201_crm_sub_app_rwa_ris_wei_crm = r201_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR201_crm_sub_app_rwa_ris_cea_cov() {
		return r201_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR201_crm_sub_app_rwa_ris_cea_cov(BigDecimal r201_crm_sub_app_rwa_ris_cea_cov) {
		this.r201_crm_sub_app_rwa_ris_cea_cov = r201_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR201_crm_sub_app_rwa_appl_org_coun() {
		return r201_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR201_crm_sub_app_rwa_appl_org_coun(BigDecimal r201_crm_sub_app_rwa_appl_org_coun) {
		this.r201_crm_sub_app_rwa_appl_org_coun = r201_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR201_crm_sub_app_rwa_ris_cea_not_cov() {
		return r201_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR201_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r201_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r201_crm_sub_app_rwa_ris_cea_not_cov = r201_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR201_crm_com_app_col_cea_elig_crm() {
		return r201_crm_com_app_col_cea_elig_crm;
	}
	public void setR201_crm_com_app_col_cea_elig_crm(BigDecimal r201_crm_com_app_col_cea_elig_crm) {
		this.r201_crm_com_app_col_cea_elig_crm = r201_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR201_crm_com_app_col_elig_cea_vol_adj() {
		return r201_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR201_crm_com_app_col_elig_cea_vol_adj(BigDecimal r201_crm_com_app_col_elig_cea_vol_adj) {
		this.r201_crm_com_app_col_elig_cea_vol_adj = r201_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR201_crm_com_app_col_elig_fin_hai() {
		return r201_crm_com_app_col_elig_fin_hai;
	}
	public void setR201_crm_com_app_col_elig_fin_hai(BigDecimal r201_crm_com_app_col_elig_fin_hai) {
		this.r201_crm_com_app_col_elig_fin_hai = r201_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR201_crm_com_app_col_cea_val_aft_crm() {
		return r201_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR201_crm_com_app_col_cea_val_aft_crm(BigDecimal r201_crm_com_app_col_cea_val_aft_crm) {
		this.r201_crm_com_app_col_cea_val_aft_crm = r201_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR201_rwa_elig_cea_not_cov() {
		return r201_rwa_elig_cea_not_cov;
	}
	public void setR201_rwa_elig_cea_not_cov(BigDecimal r201_rwa_elig_cea_not_cov) {
		this.r201_rwa_elig_cea_not_cov = r201_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR201_rwa_unsec_cea_sub_cre_ris() {
		return r201_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR201_rwa_unsec_cea_sub_cre_ris(BigDecimal r201_rwa_unsec_cea_sub_cre_ris) {
		this.r201_rwa_unsec_cea_sub_cre_ris = r201_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR201_rwa_unsec_cea() {
		return r201_rwa_unsec_cea;
	}
	public void setR201_rwa_unsec_cea(BigDecimal r201_rwa_unsec_cea) {
		this.r201_rwa_unsec_cea = r201_rwa_unsec_cea;
	}
	public BigDecimal getR201_rwa_tot_ris_wei_ass() {
		return r201_rwa_tot_ris_wei_ass;
	}
	public void setR201_rwa_tot_ris_wei_ass(BigDecimal r201_rwa_tot_ris_wei_ass) {
		this.r201_rwa_tot_ris_wei_ass = r201_rwa_tot_ris_wei_ass;
	}
	public String getR202_exposure_class_off_bal() {
		return r202_exposure_class_off_bal;
	}
	public void setR202_exposure_class_off_bal(String r202_exposure_class_off_bal) {
		this.r202_exposure_class_off_bal = r202_exposure_class_off_bal;
	}
	public BigDecimal getR202_nom_pri_amt() {
		return r202_nom_pri_amt;
	}
	public void setR202_nom_pri_amt(BigDecimal r202_nom_pri_amt) {
		this.r202_nom_pri_amt = r202_nom_pri_amt;
	}
	public BigDecimal getR202_ccf() {
		return r202_ccf;
	}
	public void setR202_ccf(BigDecimal r202_ccf) {
		this.r202_ccf = r202_ccf;
	}
	public BigDecimal getR202_cea() {
		return r202_cea;
	}
	public void setR202_cea(BigDecimal r202_cea) {
		this.r202_cea = r202_cea;
	}
	public BigDecimal getR202_cea_elig_coun_bilt_net() {
		return r202_cea_elig_coun_bilt_net;
	}
	public void setR202_cea_elig_coun_bilt_net(BigDecimal r202_cea_elig_coun_bilt_net) {
		this.r202_cea_elig_coun_bilt_net = r202_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR202_cea_aft_net() {
		return r202_cea_aft_net;
	}
	public void setR202_cea_aft_net(BigDecimal r202_cea_aft_net) {
		this.r202_cea_aft_net = r202_cea_aft_net;
	}
	public BigDecimal getR202_crm_sub_app_cea_elig() {
		return r202_crm_sub_app_cea_elig;
	}
	public void setR202_crm_sub_app_cea_elig(BigDecimal r202_crm_sub_app_cea_elig) {
		this.r202_crm_sub_app_cea_elig = r202_crm_sub_app_cea_elig;
	}
	public BigDecimal getR202_crm_sub_app_non_col_guar_elig() {
		return r202_crm_sub_app_non_col_guar_elig;
	}
	public void setR202_crm_sub_app_non_col_guar_elig(BigDecimal r202_crm_sub_app_non_col_guar_elig) {
		this.r202_crm_sub_app_non_col_guar_elig = r202_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR202_crm_sub_app_non_col_cre_der() {
		return r202_crm_sub_app_non_col_cre_der;
	}
	public void setR202_crm_sub_app_non_col_cre_der(BigDecimal r202_crm_sub_app_non_col_cre_der) {
		this.r202_crm_sub_app_non_col_cre_der = r202_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR202_crm_sub_app_col_elig_cash() {
		return r202_crm_sub_app_col_elig_cash;
	}
	public void setR202_crm_sub_app_col_elig_cash(BigDecimal r202_crm_sub_app_col_elig_cash) {
		this.r202_crm_sub_app_col_elig_cash = r202_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR202_crm_sub_app_col_elig_tre_bills() {
		return r202_crm_sub_app_col_elig_tre_bills;
	}
	public void setR202_crm_sub_app_col_elig_tre_bills(BigDecimal r202_crm_sub_app_col_elig_tre_bills) {
		this.r202_crm_sub_app_col_elig_tre_bills = r202_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR202_crm_sub_app_col_elig_deb_sec() {
		return r202_crm_sub_app_col_elig_deb_sec;
	}
	public void setR202_crm_sub_app_col_elig_deb_sec(BigDecimal r202_crm_sub_app_col_elig_deb_sec) {
		this.r202_crm_sub_app_col_elig_deb_sec = r202_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR202_crm_sub_app_col_elig_euiq() {
		return r202_crm_sub_app_col_elig_euiq;
	}
	public void setR202_crm_sub_app_col_elig_euiq(BigDecimal r202_crm_sub_app_col_elig_euiq) {
		this.r202_crm_sub_app_col_elig_euiq = r202_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR202_crm_sub_app_col_elig_uni_tru() {
		return r202_crm_sub_app_col_elig_uni_tru;
	}
	public void setR202_crm_sub_app_col_elig_uni_tru(BigDecimal r202_crm_sub_app_col_elig_uni_tru) {
		this.r202_crm_sub_app_col_elig_uni_tru = r202_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR202_crm_sub_app_col_cea_cov() {
		return r202_crm_sub_app_col_cea_cov;
	}
	public void setR202_crm_sub_app_col_cea_cov(BigDecimal r202_crm_sub_app_col_cea_cov) {
		this.r202_crm_sub_app_col_cea_cov = r202_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR202_crm_sub_app_col_cea_not_cov() {
		return r202_crm_sub_app_col_cea_not_cov;
	}
	public void setR202_crm_sub_app_col_cea_not_cov(BigDecimal r202_crm_sub_app_col_cea_not_cov) {
		this.r202_crm_sub_app_col_cea_not_cov = r202_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR202_crm_sub_app_rwa_ris_wei_crm() {
		return r202_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR202_crm_sub_app_rwa_ris_wei_crm(BigDecimal r202_crm_sub_app_rwa_ris_wei_crm) {
		this.r202_crm_sub_app_rwa_ris_wei_crm = r202_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR202_crm_sub_app_rwa_ris_cea_cov() {
		return r202_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR202_crm_sub_app_rwa_ris_cea_cov(BigDecimal r202_crm_sub_app_rwa_ris_cea_cov) {
		this.r202_crm_sub_app_rwa_ris_cea_cov = r202_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR202_crm_sub_app_rwa_appl_org_coun() {
		return r202_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR202_crm_sub_app_rwa_appl_org_coun(BigDecimal r202_crm_sub_app_rwa_appl_org_coun) {
		this.r202_crm_sub_app_rwa_appl_org_coun = r202_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR202_crm_sub_app_rwa_ris_cea_not_cov() {
		return r202_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR202_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r202_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r202_crm_sub_app_rwa_ris_cea_not_cov = r202_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR202_crm_com_app_col_cea_elig_crm() {
		return r202_crm_com_app_col_cea_elig_crm;
	}
	public void setR202_crm_com_app_col_cea_elig_crm(BigDecimal r202_crm_com_app_col_cea_elig_crm) {
		this.r202_crm_com_app_col_cea_elig_crm = r202_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR202_crm_com_app_col_elig_cea_vol_adj() {
		return r202_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR202_crm_com_app_col_elig_cea_vol_adj(BigDecimal r202_crm_com_app_col_elig_cea_vol_adj) {
		this.r202_crm_com_app_col_elig_cea_vol_adj = r202_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR202_crm_com_app_col_elig_fin_hai() {
		return r202_crm_com_app_col_elig_fin_hai;
	}
	public void setR202_crm_com_app_col_elig_fin_hai(BigDecimal r202_crm_com_app_col_elig_fin_hai) {
		this.r202_crm_com_app_col_elig_fin_hai = r202_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR202_crm_com_app_col_cea_val_aft_crm() {
		return r202_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR202_crm_com_app_col_cea_val_aft_crm(BigDecimal r202_crm_com_app_col_cea_val_aft_crm) {
		this.r202_crm_com_app_col_cea_val_aft_crm = r202_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR202_rwa_elig_cea_not_cov() {
		return r202_rwa_elig_cea_not_cov;
	}
	public void setR202_rwa_elig_cea_not_cov(BigDecimal r202_rwa_elig_cea_not_cov) {
		this.r202_rwa_elig_cea_not_cov = r202_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR202_rwa_unsec_cea_sub_cre_ris() {
		return r202_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR202_rwa_unsec_cea_sub_cre_ris(BigDecimal r202_rwa_unsec_cea_sub_cre_ris) {
		this.r202_rwa_unsec_cea_sub_cre_ris = r202_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR202_rwa_unsec_cea() {
		return r202_rwa_unsec_cea;
	}
	public void setR202_rwa_unsec_cea(BigDecimal r202_rwa_unsec_cea) {
		this.r202_rwa_unsec_cea = r202_rwa_unsec_cea;
	}
	public BigDecimal getR202_rwa_tot_ris_wei_ass() {
		return r202_rwa_tot_ris_wei_ass;
	}
	public void setR202_rwa_tot_ris_wei_ass(BigDecimal r202_rwa_tot_ris_wei_ass) {
		this.r202_rwa_tot_ris_wei_ass = r202_rwa_tot_ris_wei_ass;
	}
	public String getR203_exposure_class_off_bal() {
		return r203_exposure_class_off_bal;
	}
	public void setR203_exposure_class_off_bal(String r203_exposure_class_off_bal) {
		this.r203_exposure_class_off_bal = r203_exposure_class_off_bal;
	}
	public BigDecimal getR203_nom_pri_amt() {
		return r203_nom_pri_amt;
	}
	public void setR203_nom_pri_amt(BigDecimal r203_nom_pri_amt) {
		this.r203_nom_pri_amt = r203_nom_pri_amt;
	}
	public BigDecimal getR203_ccf() {
		return r203_ccf;
	}
	public void setR203_ccf(BigDecimal r203_ccf) {
		this.r203_ccf = r203_ccf;
	}
	public BigDecimal getR203_cea() {
		return r203_cea;
	}
	public void setR203_cea(BigDecimal r203_cea) {
		this.r203_cea = r203_cea;
	}
	public BigDecimal getR203_cea_elig_coun_bilt_net() {
		return r203_cea_elig_coun_bilt_net;
	}
	public void setR203_cea_elig_coun_bilt_net(BigDecimal r203_cea_elig_coun_bilt_net) {
		this.r203_cea_elig_coun_bilt_net = r203_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR203_cea_aft_net() {
		return r203_cea_aft_net;
	}
	public void setR203_cea_aft_net(BigDecimal r203_cea_aft_net) {
		this.r203_cea_aft_net = r203_cea_aft_net;
	}
	public BigDecimal getR203_crm_sub_app_cea_elig() {
		return r203_crm_sub_app_cea_elig;
	}
	public void setR203_crm_sub_app_cea_elig(BigDecimal r203_crm_sub_app_cea_elig) {
		this.r203_crm_sub_app_cea_elig = r203_crm_sub_app_cea_elig;
	}
	public BigDecimal getR203_crm_sub_app_non_col_guar_elig() {
		return r203_crm_sub_app_non_col_guar_elig;
	}
	public void setR203_crm_sub_app_non_col_guar_elig(BigDecimal r203_crm_sub_app_non_col_guar_elig) {
		this.r203_crm_sub_app_non_col_guar_elig = r203_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR203_crm_sub_app_non_col_cre_der() {
		return r203_crm_sub_app_non_col_cre_der;
	}
	public void setR203_crm_sub_app_non_col_cre_der(BigDecimal r203_crm_sub_app_non_col_cre_der) {
		this.r203_crm_sub_app_non_col_cre_der = r203_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR203_crm_sub_app_col_elig_cash() {
		return r203_crm_sub_app_col_elig_cash;
	}
	public void setR203_crm_sub_app_col_elig_cash(BigDecimal r203_crm_sub_app_col_elig_cash) {
		this.r203_crm_sub_app_col_elig_cash = r203_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR203_crm_sub_app_col_elig_tre_bills() {
		return r203_crm_sub_app_col_elig_tre_bills;
	}
	public void setR203_crm_sub_app_col_elig_tre_bills(BigDecimal r203_crm_sub_app_col_elig_tre_bills) {
		this.r203_crm_sub_app_col_elig_tre_bills = r203_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR203_crm_sub_app_col_elig_deb_sec() {
		return r203_crm_sub_app_col_elig_deb_sec;
	}
	public void setR203_crm_sub_app_col_elig_deb_sec(BigDecimal r203_crm_sub_app_col_elig_deb_sec) {
		this.r203_crm_sub_app_col_elig_deb_sec = r203_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR203_crm_sub_app_col_elig_euiq() {
		return r203_crm_sub_app_col_elig_euiq;
	}
	public void setR203_crm_sub_app_col_elig_euiq(BigDecimal r203_crm_sub_app_col_elig_euiq) {
		this.r203_crm_sub_app_col_elig_euiq = r203_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR203_crm_sub_app_col_elig_uni_tru() {
		return r203_crm_sub_app_col_elig_uni_tru;
	}
	public void setR203_crm_sub_app_col_elig_uni_tru(BigDecimal r203_crm_sub_app_col_elig_uni_tru) {
		this.r203_crm_sub_app_col_elig_uni_tru = r203_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR203_crm_sub_app_col_cea_cov() {
		return r203_crm_sub_app_col_cea_cov;
	}
	public void setR203_crm_sub_app_col_cea_cov(BigDecimal r203_crm_sub_app_col_cea_cov) {
		this.r203_crm_sub_app_col_cea_cov = r203_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR203_crm_sub_app_col_cea_not_cov() {
		return r203_crm_sub_app_col_cea_not_cov;
	}
	public void setR203_crm_sub_app_col_cea_not_cov(BigDecimal r203_crm_sub_app_col_cea_not_cov) {
		this.r203_crm_sub_app_col_cea_not_cov = r203_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR203_crm_sub_app_rwa_ris_wei_crm() {
		return r203_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR203_crm_sub_app_rwa_ris_wei_crm(BigDecimal r203_crm_sub_app_rwa_ris_wei_crm) {
		this.r203_crm_sub_app_rwa_ris_wei_crm = r203_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR203_crm_sub_app_rwa_ris_cea_cov() {
		return r203_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR203_crm_sub_app_rwa_ris_cea_cov(BigDecimal r203_crm_sub_app_rwa_ris_cea_cov) {
		this.r203_crm_sub_app_rwa_ris_cea_cov = r203_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR203_crm_sub_app_rwa_appl_org_coun() {
		return r203_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR203_crm_sub_app_rwa_appl_org_coun(BigDecimal r203_crm_sub_app_rwa_appl_org_coun) {
		this.r203_crm_sub_app_rwa_appl_org_coun = r203_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR203_crm_sub_app_rwa_ris_cea_not_cov() {
		return r203_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR203_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r203_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r203_crm_sub_app_rwa_ris_cea_not_cov = r203_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR203_crm_com_app_col_cea_elig_crm() {
		return r203_crm_com_app_col_cea_elig_crm;
	}
	public void setR203_crm_com_app_col_cea_elig_crm(BigDecimal r203_crm_com_app_col_cea_elig_crm) {
		this.r203_crm_com_app_col_cea_elig_crm = r203_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR203_crm_com_app_col_elig_cea_vol_adj() {
		return r203_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR203_crm_com_app_col_elig_cea_vol_adj(BigDecimal r203_crm_com_app_col_elig_cea_vol_adj) {
		this.r203_crm_com_app_col_elig_cea_vol_adj = r203_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR203_crm_com_app_col_elig_fin_hai() {
		return r203_crm_com_app_col_elig_fin_hai;
	}
	public void setR203_crm_com_app_col_elig_fin_hai(BigDecimal r203_crm_com_app_col_elig_fin_hai) {
		this.r203_crm_com_app_col_elig_fin_hai = r203_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR203_crm_com_app_col_cea_val_aft_crm() {
		return r203_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR203_crm_com_app_col_cea_val_aft_crm(BigDecimal r203_crm_com_app_col_cea_val_aft_crm) {
		this.r203_crm_com_app_col_cea_val_aft_crm = r203_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR203_rwa_elig_cea_not_cov() {
		return r203_rwa_elig_cea_not_cov;
	}
	public void setR203_rwa_elig_cea_not_cov(BigDecimal r203_rwa_elig_cea_not_cov) {
		this.r203_rwa_elig_cea_not_cov = r203_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR203_rwa_unsec_cea_sub_cre_ris() {
		return r203_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR203_rwa_unsec_cea_sub_cre_ris(BigDecimal r203_rwa_unsec_cea_sub_cre_ris) {
		this.r203_rwa_unsec_cea_sub_cre_ris = r203_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR203_rwa_unsec_cea() {
		return r203_rwa_unsec_cea;
	}
	public void setR203_rwa_unsec_cea(BigDecimal r203_rwa_unsec_cea) {
		this.r203_rwa_unsec_cea = r203_rwa_unsec_cea;
	}
	public BigDecimal getR203_rwa_tot_ris_wei_ass() {
		return r203_rwa_tot_ris_wei_ass;
	}
	public void setR203_rwa_tot_ris_wei_ass(BigDecimal r203_rwa_tot_ris_wei_ass) {
		this.r203_rwa_tot_ris_wei_ass = r203_rwa_tot_ris_wei_ass;
	}
	public String getR204_exposure_class_off_bal() {
		return r204_exposure_class_off_bal;
	}
	public void setR204_exposure_class_off_bal(String r204_exposure_class_off_bal) {
		this.r204_exposure_class_off_bal = r204_exposure_class_off_bal;
	}
	public BigDecimal getR204_nom_pri_amt() {
		return r204_nom_pri_amt;
	}
	public void setR204_nom_pri_amt(BigDecimal r204_nom_pri_amt) {
		this.r204_nom_pri_amt = r204_nom_pri_amt;
	}
	public BigDecimal getR204_ccf() {
		return r204_ccf;
	}
	public void setR204_ccf(BigDecimal r204_ccf) {
		this.r204_ccf = r204_ccf;
	}
	public BigDecimal getR204_cea() {
		return r204_cea;
	}
	public void setR204_cea(BigDecimal r204_cea) {
		this.r204_cea = r204_cea;
	}
	public BigDecimal getR204_cea_elig_coun_bilt_net() {
		return r204_cea_elig_coun_bilt_net;
	}
	public void setR204_cea_elig_coun_bilt_net(BigDecimal r204_cea_elig_coun_bilt_net) {
		this.r204_cea_elig_coun_bilt_net = r204_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR204_cea_aft_net() {
		return r204_cea_aft_net;
	}
	public void setR204_cea_aft_net(BigDecimal r204_cea_aft_net) {
		this.r204_cea_aft_net = r204_cea_aft_net;
	}
	public BigDecimal getR204_crm_sub_app_cea_elig() {
		return r204_crm_sub_app_cea_elig;
	}
	public void setR204_crm_sub_app_cea_elig(BigDecimal r204_crm_sub_app_cea_elig) {
		this.r204_crm_sub_app_cea_elig = r204_crm_sub_app_cea_elig;
	}
	public BigDecimal getR204_crm_sub_app_non_col_guar_elig() {
		return r204_crm_sub_app_non_col_guar_elig;
	}
	public void setR204_crm_sub_app_non_col_guar_elig(BigDecimal r204_crm_sub_app_non_col_guar_elig) {
		this.r204_crm_sub_app_non_col_guar_elig = r204_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR204_crm_sub_app_non_col_cre_der() {
		return r204_crm_sub_app_non_col_cre_der;
	}
	public void setR204_crm_sub_app_non_col_cre_der(BigDecimal r204_crm_sub_app_non_col_cre_der) {
		this.r204_crm_sub_app_non_col_cre_der = r204_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR204_crm_sub_app_col_elig_cash() {
		return r204_crm_sub_app_col_elig_cash;
	}
	public void setR204_crm_sub_app_col_elig_cash(BigDecimal r204_crm_sub_app_col_elig_cash) {
		this.r204_crm_sub_app_col_elig_cash = r204_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR204_crm_sub_app_col_elig_tre_bills() {
		return r204_crm_sub_app_col_elig_tre_bills;
	}
	public void setR204_crm_sub_app_col_elig_tre_bills(BigDecimal r204_crm_sub_app_col_elig_tre_bills) {
		this.r204_crm_sub_app_col_elig_tre_bills = r204_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR204_crm_sub_app_col_elig_deb_sec() {
		return r204_crm_sub_app_col_elig_deb_sec;
	}
	public void setR204_crm_sub_app_col_elig_deb_sec(BigDecimal r204_crm_sub_app_col_elig_deb_sec) {
		this.r204_crm_sub_app_col_elig_deb_sec = r204_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR204_crm_sub_app_col_elig_euiq() {
		return r204_crm_sub_app_col_elig_euiq;
	}
	public void setR204_crm_sub_app_col_elig_euiq(BigDecimal r204_crm_sub_app_col_elig_euiq) {
		this.r204_crm_sub_app_col_elig_euiq = r204_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR204_crm_sub_app_col_elig_uni_tru() {
		return r204_crm_sub_app_col_elig_uni_tru;
	}
	public void setR204_crm_sub_app_col_elig_uni_tru(BigDecimal r204_crm_sub_app_col_elig_uni_tru) {
		this.r204_crm_sub_app_col_elig_uni_tru = r204_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR204_crm_sub_app_col_cea_cov() {
		return r204_crm_sub_app_col_cea_cov;
	}
	public void setR204_crm_sub_app_col_cea_cov(BigDecimal r204_crm_sub_app_col_cea_cov) {
		this.r204_crm_sub_app_col_cea_cov = r204_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR204_crm_sub_app_col_cea_not_cov() {
		return r204_crm_sub_app_col_cea_not_cov;
	}
	public void setR204_crm_sub_app_col_cea_not_cov(BigDecimal r204_crm_sub_app_col_cea_not_cov) {
		this.r204_crm_sub_app_col_cea_not_cov = r204_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR204_crm_sub_app_rwa_ris_wei_crm() {
		return r204_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR204_crm_sub_app_rwa_ris_wei_crm(BigDecimal r204_crm_sub_app_rwa_ris_wei_crm) {
		this.r204_crm_sub_app_rwa_ris_wei_crm = r204_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR204_crm_sub_app_rwa_ris_cea_cov() {
		return r204_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR204_crm_sub_app_rwa_ris_cea_cov(BigDecimal r204_crm_sub_app_rwa_ris_cea_cov) {
		this.r204_crm_sub_app_rwa_ris_cea_cov = r204_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR204_crm_sub_app_rwa_appl_org_coun() {
		return r204_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR204_crm_sub_app_rwa_appl_org_coun(BigDecimal r204_crm_sub_app_rwa_appl_org_coun) {
		this.r204_crm_sub_app_rwa_appl_org_coun = r204_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR204_crm_sub_app_rwa_ris_cea_not_cov() {
		return r204_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR204_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r204_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r204_crm_sub_app_rwa_ris_cea_not_cov = r204_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR204_crm_com_app_col_cea_elig_crm() {
		return r204_crm_com_app_col_cea_elig_crm;
	}
	public void setR204_crm_com_app_col_cea_elig_crm(BigDecimal r204_crm_com_app_col_cea_elig_crm) {
		this.r204_crm_com_app_col_cea_elig_crm = r204_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR204_crm_com_app_col_elig_cea_vol_adj() {
		return r204_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR204_crm_com_app_col_elig_cea_vol_adj(BigDecimal r204_crm_com_app_col_elig_cea_vol_adj) {
		this.r204_crm_com_app_col_elig_cea_vol_adj = r204_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR204_crm_com_app_col_elig_fin_hai() {
		return r204_crm_com_app_col_elig_fin_hai;
	}
	public void setR204_crm_com_app_col_elig_fin_hai(BigDecimal r204_crm_com_app_col_elig_fin_hai) {
		this.r204_crm_com_app_col_elig_fin_hai = r204_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR204_crm_com_app_col_cea_val_aft_crm() {
		return r204_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR204_crm_com_app_col_cea_val_aft_crm(BigDecimal r204_crm_com_app_col_cea_val_aft_crm) {
		this.r204_crm_com_app_col_cea_val_aft_crm = r204_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR204_rwa_elig_cea_not_cov() {
		return r204_rwa_elig_cea_not_cov;
	}
	public void setR204_rwa_elig_cea_not_cov(BigDecimal r204_rwa_elig_cea_not_cov) {
		this.r204_rwa_elig_cea_not_cov = r204_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR204_rwa_unsec_cea_sub_cre_ris() {
		return r204_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR204_rwa_unsec_cea_sub_cre_ris(BigDecimal r204_rwa_unsec_cea_sub_cre_ris) {
		this.r204_rwa_unsec_cea_sub_cre_ris = r204_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR204_rwa_unsec_cea() {
		return r204_rwa_unsec_cea;
	}
	public void setR204_rwa_unsec_cea(BigDecimal r204_rwa_unsec_cea) {
		this.r204_rwa_unsec_cea = r204_rwa_unsec_cea;
	}
	public BigDecimal getR204_rwa_tot_ris_wei_ass() {
		return r204_rwa_tot_ris_wei_ass;
	}
	public void setR204_rwa_tot_ris_wei_ass(BigDecimal r204_rwa_tot_ris_wei_ass) {
		this.r204_rwa_tot_ris_wei_ass = r204_rwa_tot_ris_wei_ass;
	}
	public String getR205_exposure_class_off_bal() {
		return r205_exposure_class_off_bal;
	}
	public void setR205_exposure_class_off_bal(String r205_exposure_class_off_bal) {
		this.r205_exposure_class_off_bal = r205_exposure_class_off_bal;
	}
	public BigDecimal getR205_nom_pri_amt() {
		return r205_nom_pri_amt;
	}
	public void setR205_nom_pri_amt(BigDecimal r205_nom_pri_amt) {
		this.r205_nom_pri_amt = r205_nom_pri_amt;
	}
	public BigDecimal getR205_ccf() {
		return r205_ccf;
	}
	public void setR205_ccf(BigDecimal r205_ccf) {
		this.r205_ccf = r205_ccf;
	}
	public BigDecimal getR205_cea() {
		return r205_cea;
	}
	public void setR205_cea(BigDecimal r205_cea) {
		this.r205_cea = r205_cea;
	}
	public BigDecimal getR205_cea_elig_coun_bilt_net() {
		return r205_cea_elig_coun_bilt_net;
	}
	public void setR205_cea_elig_coun_bilt_net(BigDecimal r205_cea_elig_coun_bilt_net) {
		this.r205_cea_elig_coun_bilt_net = r205_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR205_cea_aft_net() {
		return r205_cea_aft_net;
	}
	public void setR205_cea_aft_net(BigDecimal r205_cea_aft_net) {
		this.r205_cea_aft_net = r205_cea_aft_net;
	}
	public BigDecimal getR205_crm_sub_app_cea_elig() {
		return r205_crm_sub_app_cea_elig;
	}
	public void setR205_crm_sub_app_cea_elig(BigDecimal r205_crm_sub_app_cea_elig) {
		this.r205_crm_sub_app_cea_elig = r205_crm_sub_app_cea_elig;
	}
	public BigDecimal getR205_crm_sub_app_non_col_guar_elig() {
		return r205_crm_sub_app_non_col_guar_elig;
	}
	public void setR205_crm_sub_app_non_col_guar_elig(BigDecimal r205_crm_sub_app_non_col_guar_elig) {
		this.r205_crm_sub_app_non_col_guar_elig = r205_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR205_crm_sub_app_non_col_cre_der() {
		return r205_crm_sub_app_non_col_cre_der;
	}
	public void setR205_crm_sub_app_non_col_cre_der(BigDecimal r205_crm_sub_app_non_col_cre_der) {
		this.r205_crm_sub_app_non_col_cre_der = r205_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR205_crm_sub_app_col_elig_cash() {
		return r205_crm_sub_app_col_elig_cash;
	}
	public void setR205_crm_sub_app_col_elig_cash(BigDecimal r205_crm_sub_app_col_elig_cash) {
		this.r205_crm_sub_app_col_elig_cash = r205_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR205_crm_sub_app_col_elig_tre_bills() {
		return r205_crm_sub_app_col_elig_tre_bills;
	}
	public void setR205_crm_sub_app_col_elig_tre_bills(BigDecimal r205_crm_sub_app_col_elig_tre_bills) {
		this.r205_crm_sub_app_col_elig_tre_bills = r205_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR205_crm_sub_app_col_elig_deb_sec() {
		return r205_crm_sub_app_col_elig_deb_sec;
	}
	public void setR205_crm_sub_app_col_elig_deb_sec(BigDecimal r205_crm_sub_app_col_elig_deb_sec) {
		this.r205_crm_sub_app_col_elig_deb_sec = r205_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR205_crm_sub_app_col_elig_euiq() {
		return r205_crm_sub_app_col_elig_euiq;
	}
	public void setR205_crm_sub_app_col_elig_euiq(BigDecimal r205_crm_sub_app_col_elig_euiq) {
		this.r205_crm_sub_app_col_elig_euiq = r205_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR205_crm_sub_app_col_elig_uni_tru() {
		return r205_crm_sub_app_col_elig_uni_tru;
	}
	public void setR205_crm_sub_app_col_elig_uni_tru(BigDecimal r205_crm_sub_app_col_elig_uni_tru) {
		this.r205_crm_sub_app_col_elig_uni_tru = r205_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR205_crm_sub_app_col_cea_cov() {
		return r205_crm_sub_app_col_cea_cov;
	}
	public void setR205_crm_sub_app_col_cea_cov(BigDecimal r205_crm_sub_app_col_cea_cov) {
		this.r205_crm_sub_app_col_cea_cov = r205_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR205_crm_sub_app_col_cea_not_cov() {
		return r205_crm_sub_app_col_cea_not_cov;
	}
	public void setR205_crm_sub_app_col_cea_not_cov(BigDecimal r205_crm_sub_app_col_cea_not_cov) {
		this.r205_crm_sub_app_col_cea_not_cov = r205_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR205_crm_sub_app_rwa_ris_wei_crm() {
		return r205_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR205_crm_sub_app_rwa_ris_wei_crm(BigDecimal r205_crm_sub_app_rwa_ris_wei_crm) {
		this.r205_crm_sub_app_rwa_ris_wei_crm = r205_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR205_crm_sub_app_rwa_ris_cea_cov() {
		return r205_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR205_crm_sub_app_rwa_ris_cea_cov(BigDecimal r205_crm_sub_app_rwa_ris_cea_cov) {
		this.r205_crm_sub_app_rwa_ris_cea_cov = r205_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR205_crm_sub_app_rwa_appl_org_coun() {
		return r205_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR205_crm_sub_app_rwa_appl_org_coun(BigDecimal r205_crm_sub_app_rwa_appl_org_coun) {
		this.r205_crm_sub_app_rwa_appl_org_coun = r205_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR205_crm_sub_app_rwa_ris_cea_not_cov() {
		return r205_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR205_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r205_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r205_crm_sub_app_rwa_ris_cea_not_cov = r205_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR205_crm_com_app_col_cea_elig_crm() {
		return r205_crm_com_app_col_cea_elig_crm;
	}
	public void setR205_crm_com_app_col_cea_elig_crm(BigDecimal r205_crm_com_app_col_cea_elig_crm) {
		this.r205_crm_com_app_col_cea_elig_crm = r205_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR205_crm_com_app_col_elig_cea_vol_adj() {
		return r205_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR205_crm_com_app_col_elig_cea_vol_adj(BigDecimal r205_crm_com_app_col_elig_cea_vol_adj) {
		this.r205_crm_com_app_col_elig_cea_vol_adj = r205_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR205_crm_com_app_col_elig_fin_hai() {
		return r205_crm_com_app_col_elig_fin_hai;
	}
	public void setR205_crm_com_app_col_elig_fin_hai(BigDecimal r205_crm_com_app_col_elig_fin_hai) {
		this.r205_crm_com_app_col_elig_fin_hai = r205_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR205_crm_com_app_col_cea_val_aft_crm() {
		return r205_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR205_crm_com_app_col_cea_val_aft_crm(BigDecimal r205_crm_com_app_col_cea_val_aft_crm) {
		this.r205_crm_com_app_col_cea_val_aft_crm = r205_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR205_rwa_elig_cea_not_cov() {
		return r205_rwa_elig_cea_not_cov;
	}
	public void setR205_rwa_elig_cea_not_cov(BigDecimal r205_rwa_elig_cea_not_cov) {
		this.r205_rwa_elig_cea_not_cov = r205_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR205_rwa_unsec_cea_sub_cre_ris() {
		return r205_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR205_rwa_unsec_cea_sub_cre_ris(BigDecimal r205_rwa_unsec_cea_sub_cre_ris) {
		this.r205_rwa_unsec_cea_sub_cre_ris = r205_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR205_rwa_unsec_cea() {
		return r205_rwa_unsec_cea;
	}
	public void setR205_rwa_unsec_cea(BigDecimal r205_rwa_unsec_cea) {
		this.r205_rwa_unsec_cea = r205_rwa_unsec_cea;
	}
	public BigDecimal getR205_rwa_tot_ris_wei_ass() {
		return r205_rwa_tot_ris_wei_ass;
	}
	public void setR205_rwa_tot_ris_wei_ass(BigDecimal r205_rwa_tot_ris_wei_ass) {
		this.r205_rwa_tot_ris_wei_ass = r205_rwa_tot_ris_wei_ass;
	}
	public String getR206_exposure_class_off_bal() {
		return r206_exposure_class_off_bal;
	}
	public void setR206_exposure_class_off_bal(String r206_exposure_class_off_bal) {
		this.r206_exposure_class_off_bal = r206_exposure_class_off_bal;
	}
	public BigDecimal getR206_nom_pri_amt() {
		return r206_nom_pri_amt;
	}
	public void setR206_nom_pri_amt(BigDecimal r206_nom_pri_amt) {
		this.r206_nom_pri_amt = r206_nom_pri_amt;
	}
	public BigDecimal getR206_ccf() {
		return r206_ccf;
	}
	public void setR206_ccf(BigDecimal r206_ccf) {
		this.r206_ccf = r206_ccf;
	}
	public BigDecimal getR206_cea() {
		return r206_cea;
	}
	public void setR206_cea(BigDecimal r206_cea) {
		this.r206_cea = r206_cea;
	}
	public BigDecimal getR206_cea_elig_coun_bilt_net() {
		return r206_cea_elig_coun_bilt_net;
	}
	public void setR206_cea_elig_coun_bilt_net(BigDecimal r206_cea_elig_coun_bilt_net) {
		this.r206_cea_elig_coun_bilt_net = r206_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR206_cea_aft_net() {
		return r206_cea_aft_net;
	}
	public void setR206_cea_aft_net(BigDecimal r206_cea_aft_net) {
		this.r206_cea_aft_net = r206_cea_aft_net;
	}
	public BigDecimal getR206_crm_sub_app_cea_elig() {
		return r206_crm_sub_app_cea_elig;
	}
	public void setR206_crm_sub_app_cea_elig(BigDecimal r206_crm_sub_app_cea_elig) {
		this.r206_crm_sub_app_cea_elig = r206_crm_sub_app_cea_elig;
	}
	public BigDecimal getR206_crm_sub_app_non_col_guar_elig() {
		return r206_crm_sub_app_non_col_guar_elig;
	}
	public void setR206_crm_sub_app_non_col_guar_elig(BigDecimal r206_crm_sub_app_non_col_guar_elig) {
		this.r206_crm_sub_app_non_col_guar_elig = r206_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR206_crm_sub_app_non_col_cre_der() {
		return r206_crm_sub_app_non_col_cre_der;
	}
	public void setR206_crm_sub_app_non_col_cre_der(BigDecimal r206_crm_sub_app_non_col_cre_der) {
		this.r206_crm_sub_app_non_col_cre_der = r206_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR206_crm_sub_app_col_elig_cash() {
		return r206_crm_sub_app_col_elig_cash;
	}
	public void setR206_crm_sub_app_col_elig_cash(BigDecimal r206_crm_sub_app_col_elig_cash) {
		this.r206_crm_sub_app_col_elig_cash = r206_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR206_crm_sub_app_col_elig_tre_bills() {
		return r206_crm_sub_app_col_elig_tre_bills;
	}
	public void setR206_crm_sub_app_col_elig_tre_bills(BigDecimal r206_crm_sub_app_col_elig_tre_bills) {
		this.r206_crm_sub_app_col_elig_tre_bills = r206_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR206_crm_sub_app_col_elig_deb_sec() {
		return r206_crm_sub_app_col_elig_deb_sec;
	}
	public void setR206_crm_sub_app_col_elig_deb_sec(BigDecimal r206_crm_sub_app_col_elig_deb_sec) {
		this.r206_crm_sub_app_col_elig_deb_sec = r206_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR206_crm_sub_app_col_elig_euiq() {
		return r206_crm_sub_app_col_elig_euiq;
	}
	public void setR206_crm_sub_app_col_elig_euiq(BigDecimal r206_crm_sub_app_col_elig_euiq) {
		this.r206_crm_sub_app_col_elig_euiq = r206_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR206_crm_sub_app_col_elig_uni_tru() {
		return r206_crm_sub_app_col_elig_uni_tru;
	}
	public void setR206_crm_sub_app_col_elig_uni_tru(BigDecimal r206_crm_sub_app_col_elig_uni_tru) {
		this.r206_crm_sub_app_col_elig_uni_tru = r206_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR206_crm_sub_app_col_cea_cov() {
		return r206_crm_sub_app_col_cea_cov;
	}
	public void setR206_crm_sub_app_col_cea_cov(BigDecimal r206_crm_sub_app_col_cea_cov) {
		this.r206_crm_sub_app_col_cea_cov = r206_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR206_crm_sub_app_col_cea_not_cov() {
		return r206_crm_sub_app_col_cea_not_cov;
	}
	public void setR206_crm_sub_app_col_cea_not_cov(BigDecimal r206_crm_sub_app_col_cea_not_cov) {
		this.r206_crm_sub_app_col_cea_not_cov = r206_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR206_crm_sub_app_rwa_ris_wei_crm() {
		return r206_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR206_crm_sub_app_rwa_ris_wei_crm(BigDecimal r206_crm_sub_app_rwa_ris_wei_crm) {
		this.r206_crm_sub_app_rwa_ris_wei_crm = r206_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR206_crm_sub_app_rwa_ris_cea_cov() {
		return r206_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR206_crm_sub_app_rwa_ris_cea_cov(BigDecimal r206_crm_sub_app_rwa_ris_cea_cov) {
		this.r206_crm_sub_app_rwa_ris_cea_cov = r206_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR206_crm_sub_app_rwa_appl_org_coun() {
		return r206_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR206_crm_sub_app_rwa_appl_org_coun(BigDecimal r206_crm_sub_app_rwa_appl_org_coun) {
		this.r206_crm_sub_app_rwa_appl_org_coun = r206_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR206_crm_sub_app_rwa_ris_cea_not_cov() {
		return r206_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR206_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r206_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r206_crm_sub_app_rwa_ris_cea_not_cov = r206_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR206_crm_com_app_col_cea_elig_crm() {
		return r206_crm_com_app_col_cea_elig_crm;
	}
	public void setR206_crm_com_app_col_cea_elig_crm(BigDecimal r206_crm_com_app_col_cea_elig_crm) {
		this.r206_crm_com_app_col_cea_elig_crm = r206_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR206_crm_com_app_col_elig_cea_vol_adj() {
		return r206_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR206_crm_com_app_col_elig_cea_vol_adj(BigDecimal r206_crm_com_app_col_elig_cea_vol_adj) {
		this.r206_crm_com_app_col_elig_cea_vol_adj = r206_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR206_crm_com_app_col_elig_fin_hai() {
		return r206_crm_com_app_col_elig_fin_hai;
	}
	public void setR206_crm_com_app_col_elig_fin_hai(BigDecimal r206_crm_com_app_col_elig_fin_hai) {
		this.r206_crm_com_app_col_elig_fin_hai = r206_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR206_crm_com_app_col_cea_val_aft_crm() {
		return r206_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR206_crm_com_app_col_cea_val_aft_crm(BigDecimal r206_crm_com_app_col_cea_val_aft_crm) {
		this.r206_crm_com_app_col_cea_val_aft_crm = r206_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR206_rwa_elig_cea_not_cov() {
		return r206_rwa_elig_cea_not_cov;
	}
	public void setR206_rwa_elig_cea_not_cov(BigDecimal r206_rwa_elig_cea_not_cov) {
		this.r206_rwa_elig_cea_not_cov = r206_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR206_rwa_unsec_cea_sub_cre_ris() {
		return r206_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR206_rwa_unsec_cea_sub_cre_ris(BigDecimal r206_rwa_unsec_cea_sub_cre_ris) {
		this.r206_rwa_unsec_cea_sub_cre_ris = r206_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR206_rwa_unsec_cea() {
		return r206_rwa_unsec_cea;
	}
	public void setR206_rwa_unsec_cea(BigDecimal r206_rwa_unsec_cea) {
		this.r206_rwa_unsec_cea = r206_rwa_unsec_cea;
	}
	public BigDecimal getR206_rwa_tot_ris_wei_ass() {
		return r206_rwa_tot_ris_wei_ass;
	}
	public void setR206_rwa_tot_ris_wei_ass(BigDecimal r206_rwa_tot_ris_wei_ass) {
		this.r206_rwa_tot_ris_wei_ass = r206_rwa_tot_ris_wei_ass;
	}
	public String getR207_exposure_class_off_bal() {
		return r207_exposure_class_off_bal;
	}
	public void setR207_exposure_class_off_bal(String r207_exposure_class_off_bal) {
		this.r207_exposure_class_off_bal = r207_exposure_class_off_bal;
	}
	public BigDecimal getR207_nom_pri_amt() {
		return r207_nom_pri_amt;
	}
	public void setR207_nom_pri_amt(BigDecimal r207_nom_pri_amt) {
		this.r207_nom_pri_amt = r207_nom_pri_amt;
	}
	public BigDecimal getR207_ccf() {
		return r207_ccf;
	}
	public void setR207_ccf(BigDecimal r207_ccf) {
		this.r207_ccf = r207_ccf;
	}
	public BigDecimal getR207_cea() {
		return r207_cea;
	}
	public void setR207_cea(BigDecimal r207_cea) {
		this.r207_cea = r207_cea;
	}
	public BigDecimal getR207_cea_elig_coun_bilt_net() {
		return r207_cea_elig_coun_bilt_net;
	}
	public void setR207_cea_elig_coun_bilt_net(BigDecimal r207_cea_elig_coun_bilt_net) {
		this.r207_cea_elig_coun_bilt_net = r207_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR207_cea_aft_net() {
		return r207_cea_aft_net;
	}
	public void setR207_cea_aft_net(BigDecimal r207_cea_aft_net) {
		this.r207_cea_aft_net = r207_cea_aft_net;
	}
	public BigDecimal getR207_crm_sub_app_cea_elig() {
		return r207_crm_sub_app_cea_elig;
	}
	public void setR207_crm_sub_app_cea_elig(BigDecimal r207_crm_sub_app_cea_elig) {
		this.r207_crm_sub_app_cea_elig = r207_crm_sub_app_cea_elig;
	}
	public BigDecimal getR207_crm_sub_app_non_col_guar_elig() {
		return r207_crm_sub_app_non_col_guar_elig;
	}
	public void setR207_crm_sub_app_non_col_guar_elig(BigDecimal r207_crm_sub_app_non_col_guar_elig) {
		this.r207_crm_sub_app_non_col_guar_elig = r207_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR207_crm_sub_app_non_col_cre_der() {
		return r207_crm_sub_app_non_col_cre_der;
	}
	public void setR207_crm_sub_app_non_col_cre_der(BigDecimal r207_crm_sub_app_non_col_cre_der) {
		this.r207_crm_sub_app_non_col_cre_der = r207_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR207_crm_sub_app_col_elig_cash() {
		return r207_crm_sub_app_col_elig_cash;
	}
	public void setR207_crm_sub_app_col_elig_cash(BigDecimal r207_crm_sub_app_col_elig_cash) {
		this.r207_crm_sub_app_col_elig_cash = r207_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR207_crm_sub_app_col_elig_tre_bills() {
		return r207_crm_sub_app_col_elig_tre_bills;
	}
	public void setR207_crm_sub_app_col_elig_tre_bills(BigDecimal r207_crm_sub_app_col_elig_tre_bills) {
		this.r207_crm_sub_app_col_elig_tre_bills = r207_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR207_crm_sub_app_col_elig_deb_sec() {
		return r207_crm_sub_app_col_elig_deb_sec;
	}
	public void setR207_crm_sub_app_col_elig_deb_sec(BigDecimal r207_crm_sub_app_col_elig_deb_sec) {
		this.r207_crm_sub_app_col_elig_deb_sec = r207_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR207_crm_sub_app_col_elig_euiq() {
		return r207_crm_sub_app_col_elig_euiq;
	}
	public void setR207_crm_sub_app_col_elig_euiq(BigDecimal r207_crm_sub_app_col_elig_euiq) {
		this.r207_crm_sub_app_col_elig_euiq = r207_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR207_crm_sub_app_col_elig_uni_tru() {
		return r207_crm_sub_app_col_elig_uni_tru;
	}
	public void setR207_crm_sub_app_col_elig_uni_tru(BigDecimal r207_crm_sub_app_col_elig_uni_tru) {
		this.r207_crm_sub_app_col_elig_uni_tru = r207_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR207_crm_sub_app_col_cea_cov() {
		return r207_crm_sub_app_col_cea_cov;
	}
	public void setR207_crm_sub_app_col_cea_cov(BigDecimal r207_crm_sub_app_col_cea_cov) {
		this.r207_crm_sub_app_col_cea_cov = r207_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR207_crm_sub_app_col_cea_not_cov() {
		return r207_crm_sub_app_col_cea_not_cov;
	}
	public void setR207_crm_sub_app_col_cea_not_cov(BigDecimal r207_crm_sub_app_col_cea_not_cov) {
		this.r207_crm_sub_app_col_cea_not_cov = r207_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR207_crm_sub_app_rwa_ris_wei_crm() {
		return r207_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR207_crm_sub_app_rwa_ris_wei_crm(BigDecimal r207_crm_sub_app_rwa_ris_wei_crm) {
		this.r207_crm_sub_app_rwa_ris_wei_crm = r207_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR207_crm_sub_app_rwa_ris_cea_cov() {
		return r207_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR207_crm_sub_app_rwa_ris_cea_cov(BigDecimal r207_crm_sub_app_rwa_ris_cea_cov) {
		this.r207_crm_sub_app_rwa_ris_cea_cov = r207_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR207_crm_sub_app_rwa_appl_org_coun() {
		return r207_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR207_crm_sub_app_rwa_appl_org_coun(BigDecimal r207_crm_sub_app_rwa_appl_org_coun) {
		this.r207_crm_sub_app_rwa_appl_org_coun = r207_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR207_crm_sub_app_rwa_ris_cea_not_cov() {
		return r207_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR207_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r207_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r207_crm_sub_app_rwa_ris_cea_not_cov = r207_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR207_crm_com_app_col_cea_elig_crm() {
		return r207_crm_com_app_col_cea_elig_crm;
	}
	public void setR207_crm_com_app_col_cea_elig_crm(BigDecimal r207_crm_com_app_col_cea_elig_crm) {
		this.r207_crm_com_app_col_cea_elig_crm = r207_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR207_crm_com_app_col_elig_cea_vol_adj() {
		return r207_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR207_crm_com_app_col_elig_cea_vol_adj(BigDecimal r207_crm_com_app_col_elig_cea_vol_adj) {
		this.r207_crm_com_app_col_elig_cea_vol_adj = r207_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR207_crm_com_app_col_elig_fin_hai() {
		return r207_crm_com_app_col_elig_fin_hai;
	}
	public void setR207_crm_com_app_col_elig_fin_hai(BigDecimal r207_crm_com_app_col_elig_fin_hai) {
		this.r207_crm_com_app_col_elig_fin_hai = r207_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR207_crm_com_app_col_cea_val_aft_crm() {
		return r207_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR207_crm_com_app_col_cea_val_aft_crm(BigDecimal r207_crm_com_app_col_cea_val_aft_crm) {
		this.r207_crm_com_app_col_cea_val_aft_crm = r207_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR207_rwa_elig_cea_not_cov() {
		return r207_rwa_elig_cea_not_cov;
	}
	public void setR207_rwa_elig_cea_not_cov(BigDecimal r207_rwa_elig_cea_not_cov) {
		this.r207_rwa_elig_cea_not_cov = r207_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR207_rwa_unsec_cea_sub_cre_ris() {
		return r207_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR207_rwa_unsec_cea_sub_cre_ris(BigDecimal r207_rwa_unsec_cea_sub_cre_ris) {
		this.r207_rwa_unsec_cea_sub_cre_ris = r207_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR207_rwa_unsec_cea() {
		return r207_rwa_unsec_cea;
	}
	public void setR207_rwa_unsec_cea(BigDecimal r207_rwa_unsec_cea) {
		this.r207_rwa_unsec_cea = r207_rwa_unsec_cea;
	}
	public BigDecimal getR207_rwa_tot_ris_wei_ass() {
		return r207_rwa_tot_ris_wei_ass;
	}
	public void setR207_rwa_tot_ris_wei_ass(BigDecimal r207_rwa_tot_ris_wei_ass) {
		this.r207_rwa_tot_ris_wei_ass = r207_rwa_tot_ris_wei_ass;
	}
	public String getR208_exposure_class_off_bal() {
		return r208_exposure_class_off_bal;
	}
	public void setR208_exposure_class_off_bal(String r208_exposure_class_off_bal) {
		this.r208_exposure_class_off_bal = r208_exposure_class_off_bal;
	}
	public BigDecimal getR208_nom_pri_amt() {
		return r208_nom_pri_amt;
	}
	public void setR208_nom_pri_amt(BigDecimal r208_nom_pri_amt) {
		this.r208_nom_pri_amt = r208_nom_pri_amt;
	}
	public BigDecimal getR208_ccf() {
		return r208_ccf;
	}
	public void setR208_ccf(BigDecimal r208_ccf) {
		this.r208_ccf = r208_ccf;
	}
	public BigDecimal getR208_cea() {
		return r208_cea;
	}
	public void setR208_cea(BigDecimal r208_cea) {
		this.r208_cea = r208_cea;
	}
	public BigDecimal getR208_cea_elig_coun_bilt_net() {
		return r208_cea_elig_coun_bilt_net;
	}
	public void setR208_cea_elig_coun_bilt_net(BigDecimal r208_cea_elig_coun_bilt_net) {
		this.r208_cea_elig_coun_bilt_net = r208_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR208_cea_aft_net() {
		return r208_cea_aft_net;
	}
	public void setR208_cea_aft_net(BigDecimal r208_cea_aft_net) {
		this.r208_cea_aft_net = r208_cea_aft_net;
	}
	public BigDecimal getR208_crm_sub_app_cea_elig() {
		return r208_crm_sub_app_cea_elig;
	}
	public void setR208_crm_sub_app_cea_elig(BigDecimal r208_crm_sub_app_cea_elig) {
		this.r208_crm_sub_app_cea_elig = r208_crm_sub_app_cea_elig;
	}
	public BigDecimal getR208_crm_sub_app_non_col_guar_elig() {
		return r208_crm_sub_app_non_col_guar_elig;
	}
	public void setR208_crm_sub_app_non_col_guar_elig(BigDecimal r208_crm_sub_app_non_col_guar_elig) {
		this.r208_crm_sub_app_non_col_guar_elig = r208_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR208_crm_sub_app_non_col_cre_der() {
		return r208_crm_sub_app_non_col_cre_der;
	}
	public void setR208_crm_sub_app_non_col_cre_der(BigDecimal r208_crm_sub_app_non_col_cre_der) {
		this.r208_crm_sub_app_non_col_cre_der = r208_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR208_crm_sub_app_col_elig_cash() {
		return r208_crm_sub_app_col_elig_cash;
	}
	public void setR208_crm_sub_app_col_elig_cash(BigDecimal r208_crm_sub_app_col_elig_cash) {
		this.r208_crm_sub_app_col_elig_cash = r208_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR208_crm_sub_app_col_elig_tre_bills() {
		return r208_crm_sub_app_col_elig_tre_bills;
	}
	public void setR208_crm_sub_app_col_elig_tre_bills(BigDecimal r208_crm_sub_app_col_elig_tre_bills) {
		this.r208_crm_sub_app_col_elig_tre_bills = r208_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR208_crm_sub_app_col_elig_deb_sec() {
		return r208_crm_sub_app_col_elig_deb_sec;
	}
	public void setR208_crm_sub_app_col_elig_deb_sec(BigDecimal r208_crm_sub_app_col_elig_deb_sec) {
		this.r208_crm_sub_app_col_elig_deb_sec = r208_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR208_crm_sub_app_col_elig_euiq() {
		return r208_crm_sub_app_col_elig_euiq;
	}
	public void setR208_crm_sub_app_col_elig_euiq(BigDecimal r208_crm_sub_app_col_elig_euiq) {
		this.r208_crm_sub_app_col_elig_euiq = r208_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR208_crm_sub_app_col_elig_uni_tru() {
		return r208_crm_sub_app_col_elig_uni_tru;
	}
	public void setR208_crm_sub_app_col_elig_uni_tru(BigDecimal r208_crm_sub_app_col_elig_uni_tru) {
		this.r208_crm_sub_app_col_elig_uni_tru = r208_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR208_crm_sub_app_col_cea_cov() {
		return r208_crm_sub_app_col_cea_cov;
	}
	public void setR208_crm_sub_app_col_cea_cov(BigDecimal r208_crm_sub_app_col_cea_cov) {
		this.r208_crm_sub_app_col_cea_cov = r208_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR208_crm_sub_app_col_cea_not_cov() {
		return r208_crm_sub_app_col_cea_not_cov;
	}
	public void setR208_crm_sub_app_col_cea_not_cov(BigDecimal r208_crm_sub_app_col_cea_not_cov) {
		this.r208_crm_sub_app_col_cea_not_cov = r208_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR208_crm_sub_app_rwa_ris_wei_crm() {
		return r208_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR208_crm_sub_app_rwa_ris_wei_crm(BigDecimal r208_crm_sub_app_rwa_ris_wei_crm) {
		this.r208_crm_sub_app_rwa_ris_wei_crm = r208_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR208_crm_sub_app_rwa_ris_cea_cov() {
		return r208_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR208_crm_sub_app_rwa_ris_cea_cov(BigDecimal r208_crm_sub_app_rwa_ris_cea_cov) {
		this.r208_crm_sub_app_rwa_ris_cea_cov = r208_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR208_crm_sub_app_rwa_appl_org_coun() {
		return r208_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR208_crm_sub_app_rwa_appl_org_coun(BigDecimal r208_crm_sub_app_rwa_appl_org_coun) {
		this.r208_crm_sub_app_rwa_appl_org_coun = r208_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR208_crm_sub_app_rwa_ris_cea_not_cov() {
		return r208_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR208_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r208_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r208_crm_sub_app_rwa_ris_cea_not_cov = r208_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR208_crm_com_app_col_cea_elig_crm() {
		return r208_crm_com_app_col_cea_elig_crm;
	}
	public void setR208_crm_com_app_col_cea_elig_crm(BigDecimal r208_crm_com_app_col_cea_elig_crm) {
		this.r208_crm_com_app_col_cea_elig_crm = r208_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR208_crm_com_app_col_elig_cea_vol_adj() {
		return r208_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR208_crm_com_app_col_elig_cea_vol_adj(BigDecimal r208_crm_com_app_col_elig_cea_vol_adj) {
		this.r208_crm_com_app_col_elig_cea_vol_adj = r208_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR208_crm_com_app_col_elig_fin_hai() {
		return r208_crm_com_app_col_elig_fin_hai;
	}
	public void setR208_crm_com_app_col_elig_fin_hai(BigDecimal r208_crm_com_app_col_elig_fin_hai) {
		this.r208_crm_com_app_col_elig_fin_hai = r208_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR208_crm_com_app_col_cea_val_aft_crm() {
		return r208_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR208_crm_com_app_col_cea_val_aft_crm(BigDecimal r208_crm_com_app_col_cea_val_aft_crm) {
		this.r208_crm_com_app_col_cea_val_aft_crm = r208_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR208_rwa_elig_cea_not_cov() {
		return r208_rwa_elig_cea_not_cov;
	}
	public void setR208_rwa_elig_cea_not_cov(BigDecimal r208_rwa_elig_cea_not_cov) {
		this.r208_rwa_elig_cea_not_cov = r208_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR208_rwa_unsec_cea_sub_cre_ris() {
		return r208_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR208_rwa_unsec_cea_sub_cre_ris(BigDecimal r208_rwa_unsec_cea_sub_cre_ris) {
		this.r208_rwa_unsec_cea_sub_cre_ris = r208_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR208_rwa_unsec_cea() {
		return r208_rwa_unsec_cea;
	}
	public void setR208_rwa_unsec_cea(BigDecimal r208_rwa_unsec_cea) {
		this.r208_rwa_unsec_cea = r208_rwa_unsec_cea;
	}
	public BigDecimal getR208_rwa_tot_ris_wei_ass() {
		return r208_rwa_tot_ris_wei_ass;
	}
	public void setR208_rwa_tot_ris_wei_ass(BigDecimal r208_rwa_tot_ris_wei_ass) {
		this.r208_rwa_tot_ris_wei_ass = r208_rwa_tot_ris_wei_ass;
	}
	public String getR209_exposure_class_off_bal() {
		return r209_exposure_class_off_bal;
	}
	public void setR209_exposure_class_off_bal(String r209_exposure_class_off_bal) {
		this.r209_exposure_class_off_bal = r209_exposure_class_off_bal;
	}
	public BigDecimal getR209_nom_pri_amt() {
		return r209_nom_pri_amt;
	}
	public void setR209_nom_pri_amt(BigDecimal r209_nom_pri_amt) {
		this.r209_nom_pri_amt = r209_nom_pri_amt;
	}
	public BigDecimal getR209_ccf() {
		return r209_ccf;
	}
	public void setR209_ccf(BigDecimal r209_ccf) {
		this.r209_ccf = r209_ccf;
	}
	public BigDecimal getR209_cea() {
		return r209_cea;
	}
	public void setR209_cea(BigDecimal r209_cea) {
		this.r209_cea = r209_cea;
	}
	public BigDecimal getR209_cea_elig_coun_bilt_net() {
		return r209_cea_elig_coun_bilt_net;
	}
	public void setR209_cea_elig_coun_bilt_net(BigDecimal r209_cea_elig_coun_bilt_net) {
		this.r209_cea_elig_coun_bilt_net = r209_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR209_cea_aft_net() {
		return r209_cea_aft_net;
	}
	public void setR209_cea_aft_net(BigDecimal r209_cea_aft_net) {
		this.r209_cea_aft_net = r209_cea_aft_net;
	}
	public BigDecimal getR209_crm_sub_app_cea_elig() {
		return r209_crm_sub_app_cea_elig;
	}
	public void setR209_crm_sub_app_cea_elig(BigDecimal r209_crm_sub_app_cea_elig) {
		this.r209_crm_sub_app_cea_elig = r209_crm_sub_app_cea_elig;
	}
	public BigDecimal getR209_crm_sub_app_non_col_guar_elig() {
		return r209_crm_sub_app_non_col_guar_elig;
	}
	public void setR209_crm_sub_app_non_col_guar_elig(BigDecimal r209_crm_sub_app_non_col_guar_elig) {
		this.r209_crm_sub_app_non_col_guar_elig = r209_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR209_crm_sub_app_non_col_cre_der() {
		return r209_crm_sub_app_non_col_cre_der;
	}
	public void setR209_crm_sub_app_non_col_cre_der(BigDecimal r209_crm_sub_app_non_col_cre_der) {
		this.r209_crm_sub_app_non_col_cre_der = r209_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR209_crm_sub_app_col_elig_cash() {
		return r209_crm_sub_app_col_elig_cash;
	}
	public void setR209_crm_sub_app_col_elig_cash(BigDecimal r209_crm_sub_app_col_elig_cash) {
		this.r209_crm_sub_app_col_elig_cash = r209_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR209_crm_sub_app_col_elig_tre_bills() {
		return r209_crm_sub_app_col_elig_tre_bills;
	}
	public void setR209_crm_sub_app_col_elig_tre_bills(BigDecimal r209_crm_sub_app_col_elig_tre_bills) {
		this.r209_crm_sub_app_col_elig_tre_bills = r209_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR209_crm_sub_app_col_elig_deb_sec() {
		return r209_crm_sub_app_col_elig_deb_sec;
	}
	public void setR209_crm_sub_app_col_elig_deb_sec(BigDecimal r209_crm_sub_app_col_elig_deb_sec) {
		this.r209_crm_sub_app_col_elig_deb_sec = r209_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR209_crm_sub_app_col_elig_euiq() {
		return r209_crm_sub_app_col_elig_euiq;
	}
	public void setR209_crm_sub_app_col_elig_euiq(BigDecimal r209_crm_sub_app_col_elig_euiq) {
		this.r209_crm_sub_app_col_elig_euiq = r209_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR209_crm_sub_app_col_elig_uni_tru() {
		return r209_crm_sub_app_col_elig_uni_tru;
	}
	public void setR209_crm_sub_app_col_elig_uni_tru(BigDecimal r209_crm_sub_app_col_elig_uni_tru) {
		this.r209_crm_sub_app_col_elig_uni_tru = r209_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR209_crm_sub_app_col_cea_cov() {
		return r209_crm_sub_app_col_cea_cov;
	}
	public void setR209_crm_sub_app_col_cea_cov(BigDecimal r209_crm_sub_app_col_cea_cov) {
		this.r209_crm_sub_app_col_cea_cov = r209_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR209_crm_sub_app_col_cea_not_cov() {
		return r209_crm_sub_app_col_cea_not_cov;
	}
	public void setR209_crm_sub_app_col_cea_not_cov(BigDecimal r209_crm_sub_app_col_cea_not_cov) {
		this.r209_crm_sub_app_col_cea_not_cov = r209_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR209_crm_sub_app_rwa_ris_wei_crm() {
		return r209_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR209_crm_sub_app_rwa_ris_wei_crm(BigDecimal r209_crm_sub_app_rwa_ris_wei_crm) {
		this.r209_crm_sub_app_rwa_ris_wei_crm = r209_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR209_crm_sub_app_rwa_ris_cea_cov() {
		return r209_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR209_crm_sub_app_rwa_ris_cea_cov(BigDecimal r209_crm_sub_app_rwa_ris_cea_cov) {
		this.r209_crm_sub_app_rwa_ris_cea_cov = r209_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR209_crm_sub_app_rwa_appl_org_coun() {
		return r209_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR209_crm_sub_app_rwa_appl_org_coun(BigDecimal r209_crm_sub_app_rwa_appl_org_coun) {
		this.r209_crm_sub_app_rwa_appl_org_coun = r209_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR209_crm_sub_app_rwa_ris_cea_not_cov() {
		return r209_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR209_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r209_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r209_crm_sub_app_rwa_ris_cea_not_cov = r209_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR209_crm_com_app_col_cea_elig_crm() {
		return r209_crm_com_app_col_cea_elig_crm;
	}
	public void setR209_crm_com_app_col_cea_elig_crm(BigDecimal r209_crm_com_app_col_cea_elig_crm) {
		this.r209_crm_com_app_col_cea_elig_crm = r209_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR209_crm_com_app_col_elig_cea_vol_adj() {
		return r209_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR209_crm_com_app_col_elig_cea_vol_adj(BigDecimal r209_crm_com_app_col_elig_cea_vol_adj) {
		this.r209_crm_com_app_col_elig_cea_vol_adj = r209_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR209_crm_com_app_col_elig_fin_hai() {
		return r209_crm_com_app_col_elig_fin_hai;
	}
	public void setR209_crm_com_app_col_elig_fin_hai(BigDecimal r209_crm_com_app_col_elig_fin_hai) {
		this.r209_crm_com_app_col_elig_fin_hai = r209_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR209_crm_com_app_col_cea_val_aft_crm() {
		return r209_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR209_crm_com_app_col_cea_val_aft_crm(BigDecimal r209_crm_com_app_col_cea_val_aft_crm) {
		this.r209_crm_com_app_col_cea_val_aft_crm = r209_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR209_rwa_elig_cea_not_cov() {
		return r209_rwa_elig_cea_not_cov;
	}
	public void setR209_rwa_elig_cea_not_cov(BigDecimal r209_rwa_elig_cea_not_cov) {
		this.r209_rwa_elig_cea_not_cov = r209_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR209_rwa_unsec_cea_sub_cre_ris() {
		return r209_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR209_rwa_unsec_cea_sub_cre_ris(BigDecimal r209_rwa_unsec_cea_sub_cre_ris) {
		this.r209_rwa_unsec_cea_sub_cre_ris = r209_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR209_rwa_unsec_cea() {
		return r209_rwa_unsec_cea;
	}
	public void setR209_rwa_unsec_cea(BigDecimal r209_rwa_unsec_cea) {
		this.r209_rwa_unsec_cea = r209_rwa_unsec_cea;
	}
	public BigDecimal getR209_rwa_tot_ris_wei_ass() {
		return r209_rwa_tot_ris_wei_ass;
	}
	public void setR209_rwa_tot_ris_wei_ass(BigDecimal r209_rwa_tot_ris_wei_ass) {
		this.r209_rwa_tot_ris_wei_ass = r209_rwa_tot_ris_wei_ass;
	}
	public String getR210_exposure_class_off_bal() {
		return r210_exposure_class_off_bal;
	}
	public void setR210_exposure_class_off_bal(String r210_exposure_class_off_bal) {
		this.r210_exposure_class_off_bal = r210_exposure_class_off_bal;
	}
	public BigDecimal getR210_nom_pri_amt() {
		return r210_nom_pri_amt;
	}
	public void setR210_nom_pri_amt(BigDecimal r210_nom_pri_amt) {
		this.r210_nom_pri_amt = r210_nom_pri_amt;
	}
	public BigDecimal getR210_ccf() {
		return r210_ccf;
	}
	public void setR210_ccf(BigDecimal r210_ccf) {
		this.r210_ccf = r210_ccf;
	}
	public BigDecimal getR210_cea() {
		return r210_cea;
	}
	public void setR210_cea(BigDecimal r210_cea) {
		this.r210_cea = r210_cea;
	}
	public BigDecimal getR210_cea_elig_coun_bilt_net() {
		return r210_cea_elig_coun_bilt_net;
	}
	public void setR210_cea_elig_coun_bilt_net(BigDecimal r210_cea_elig_coun_bilt_net) {
		this.r210_cea_elig_coun_bilt_net = r210_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR210_cea_aft_net() {
		return r210_cea_aft_net;
	}
	public void setR210_cea_aft_net(BigDecimal r210_cea_aft_net) {
		this.r210_cea_aft_net = r210_cea_aft_net;
	}
	public BigDecimal getR210_crm_sub_app_cea_elig() {
		return r210_crm_sub_app_cea_elig;
	}
	public void setR210_crm_sub_app_cea_elig(BigDecimal r210_crm_sub_app_cea_elig) {
		this.r210_crm_sub_app_cea_elig = r210_crm_sub_app_cea_elig;
	}
	public BigDecimal getR210_crm_sub_app_non_col_guar_elig() {
		return r210_crm_sub_app_non_col_guar_elig;
	}
	public void setR210_crm_sub_app_non_col_guar_elig(BigDecimal r210_crm_sub_app_non_col_guar_elig) {
		this.r210_crm_sub_app_non_col_guar_elig = r210_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR210_crm_sub_app_non_col_cre_der() {
		return r210_crm_sub_app_non_col_cre_der;
	}
	public void setR210_crm_sub_app_non_col_cre_der(BigDecimal r210_crm_sub_app_non_col_cre_der) {
		this.r210_crm_sub_app_non_col_cre_der = r210_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR210_crm_sub_app_col_elig_cash() {
		return r210_crm_sub_app_col_elig_cash;
	}
	public void setR210_crm_sub_app_col_elig_cash(BigDecimal r210_crm_sub_app_col_elig_cash) {
		this.r210_crm_sub_app_col_elig_cash = r210_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR210_crm_sub_app_col_elig_tre_bills() {
		return r210_crm_sub_app_col_elig_tre_bills;
	}
	public void setR210_crm_sub_app_col_elig_tre_bills(BigDecimal r210_crm_sub_app_col_elig_tre_bills) {
		this.r210_crm_sub_app_col_elig_tre_bills = r210_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR210_crm_sub_app_col_elig_deb_sec() {
		return r210_crm_sub_app_col_elig_deb_sec;
	}
	public void setR210_crm_sub_app_col_elig_deb_sec(BigDecimal r210_crm_sub_app_col_elig_deb_sec) {
		this.r210_crm_sub_app_col_elig_deb_sec = r210_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR210_crm_sub_app_col_elig_euiq() {
		return r210_crm_sub_app_col_elig_euiq;
	}
	public void setR210_crm_sub_app_col_elig_euiq(BigDecimal r210_crm_sub_app_col_elig_euiq) {
		this.r210_crm_sub_app_col_elig_euiq = r210_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR210_crm_sub_app_col_elig_uni_tru() {
		return r210_crm_sub_app_col_elig_uni_tru;
	}
	public void setR210_crm_sub_app_col_elig_uni_tru(BigDecimal r210_crm_sub_app_col_elig_uni_tru) {
		this.r210_crm_sub_app_col_elig_uni_tru = r210_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR210_crm_sub_app_col_cea_cov() {
		return r210_crm_sub_app_col_cea_cov;
	}
	public void setR210_crm_sub_app_col_cea_cov(BigDecimal r210_crm_sub_app_col_cea_cov) {
		this.r210_crm_sub_app_col_cea_cov = r210_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR210_crm_sub_app_col_cea_not_cov() {
		return r210_crm_sub_app_col_cea_not_cov;
	}
	public void setR210_crm_sub_app_col_cea_not_cov(BigDecimal r210_crm_sub_app_col_cea_not_cov) {
		this.r210_crm_sub_app_col_cea_not_cov = r210_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR210_crm_sub_app_rwa_ris_wei_crm() {
		return r210_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR210_crm_sub_app_rwa_ris_wei_crm(BigDecimal r210_crm_sub_app_rwa_ris_wei_crm) {
		this.r210_crm_sub_app_rwa_ris_wei_crm = r210_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR210_crm_sub_app_rwa_ris_cea_cov() {
		return r210_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR210_crm_sub_app_rwa_ris_cea_cov(BigDecimal r210_crm_sub_app_rwa_ris_cea_cov) {
		this.r210_crm_sub_app_rwa_ris_cea_cov = r210_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR210_crm_sub_app_rwa_appl_org_coun() {
		return r210_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR210_crm_sub_app_rwa_appl_org_coun(BigDecimal r210_crm_sub_app_rwa_appl_org_coun) {
		this.r210_crm_sub_app_rwa_appl_org_coun = r210_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR210_crm_sub_app_rwa_ris_cea_not_cov() {
		return r210_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR210_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r210_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r210_crm_sub_app_rwa_ris_cea_not_cov = r210_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR210_crm_com_app_col_cea_elig_crm() {
		return r210_crm_com_app_col_cea_elig_crm;
	}
	public void setR210_crm_com_app_col_cea_elig_crm(BigDecimal r210_crm_com_app_col_cea_elig_crm) {
		this.r210_crm_com_app_col_cea_elig_crm = r210_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR210_crm_com_app_col_elig_cea_vol_adj() {
		return r210_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR210_crm_com_app_col_elig_cea_vol_adj(BigDecimal r210_crm_com_app_col_elig_cea_vol_adj) {
		this.r210_crm_com_app_col_elig_cea_vol_adj = r210_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR210_crm_com_app_col_elig_fin_hai() {
		return r210_crm_com_app_col_elig_fin_hai;
	}
	public void setR210_crm_com_app_col_elig_fin_hai(BigDecimal r210_crm_com_app_col_elig_fin_hai) {
		this.r210_crm_com_app_col_elig_fin_hai = r210_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR210_crm_com_app_col_cea_val_aft_crm() {
		return r210_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR210_crm_com_app_col_cea_val_aft_crm(BigDecimal r210_crm_com_app_col_cea_val_aft_crm) {
		this.r210_crm_com_app_col_cea_val_aft_crm = r210_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR210_rwa_elig_cea_not_cov() {
		return r210_rwa_elig_cea_not_cov;
	}
	public void setR210_rwa_elig_cea_not_cov(BigDecimal r210_rwa_elig_cea_not_cov) {
		this.r210_rwa_elig_cea_not_cov = r210_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR210_rwa_unsec_cea_sub_cre_ris() {
		return r210_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR210_rwa_unsec_cea_sub_cre_ris(BigDecimal r210_rwa_unsec_cea_sub_cre_ris) {
		this.r210_rwa_unsec_cea_sub_cre_ris = r210_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR210_rwa_unsec_cea() {
		return r210_rwa_unsec_cea;
	}
	public void setR210_rwa_unsec_cea(BigDecimal r210_rwa_unsec_cea) {
		this.r210_rwa_unsec_cea = r210_rwa_unsec_cea;
	}
	public BigDecimal getR210_rwa_tot_ris_wei_ass() {
		return r210_rwa_tot_ris_wei_ass;
	}
	public void setR210_rwa_tot_ris_wei_ass(BigDecimal r210_rwa_tot_ris_wei_ass) {
		this.r210_rwa_tot_ris_wei_ass = r210_rwa_tot_ris_wei_ass;
	}
	public String getR211_exposure_class_off_bal() {
		return r211_exposure_class_off_bal;
	}
	public void setR211_exposure_class_off_bal(String r211_exposure_class_off_bal) {
		this.r211_exposure_class_off_bal = r211_exposure_class_off_bal;
	}
	public BigDecimal getR211_nom_pri_amt() {
		return r211_nom_pri_amt;
	}
	public void setR211_nom_pri_amt(BigDecimal r211_nom_pri_amt) {
		this.r211_nom_pri_amt = r211_nom_pri_amt;
	}
	public BigDecimal getR211_ccf() {
		return r211_ccf;
	}
	public void setR211_ccf(BigDecimal r211_ccf) {
		this.r211_ccf = r211_ccf;
	}
	public BigDecimal getR211_cea() {
		return r211_cea;
	}
	public void setR211_cea(BigDecimal r211_cea) {
		this.r211_cea = r211_cea;
	}
	public BigDecimal getR211_cea_elig_coun_bilt_net() {
		return r211_cea_elig_coun_bilt_net;
	}
	public void setR211_cea_elig_coun_bilt_net(BigDecimal r211_cea_elig_coun_bilt_net) {
		this.r211_cea_elig_coun_bilt_net = r211_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR211_cea_aft_net() {
		return r211_cea_aft_net;
	}
	public void setR211_cea_aft_net(BigDecimal r211_cea_aft_net) {
		this.r211_cea_aft_net = r211_cea_aft_net;
	}
	public BigDecimal getR211_crm_sub_app_cea_elig() {
		return r211_crm_sub_app_cea_elig;
	}
	public void setR211_crm_sub_app_cea_elig(BigDecimal r211_crm_sub_app_cea_elig) {
		this.r211_crm_sub_app_cea_elig = r211_crm_sub_app_cea_elig;
	}
	public BigDecimal getR211_crm_sub_app_non_col_guar_elig() {
		return r211_crm_sub_app_non_col_guar_elig;
	}
	public void setR211_crm_sub_app_non_col_guar_elig(BigDecimal r211_crm_sub_app_non_col_guar_elig) {
		this.r211_crm_sub_app_non_col_guar_elig = r211_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR211_crm_sub_app_non_col_cre_der() {
		return r211_crm_sub_app_non_col_cre_der;
	}
	public void setR211_crm_sub_app_non_col_cre_der(BigDecimal r211_crm_sub_app_non_col_cre_der) {
		this.r211_crm_sub_app_non_col_cre_der = r211_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR211_crm_sub_app_col_elig_cash() {
		return r211_crm_sub_app_col_elig_cash;
	}
	public void setR211_crm_sub_app_col_elig_cash(BigDecimal r211_crm_sub_app_col_elig_cash) {
		this.r211_crm_sub_app_col_elig_cash = r211_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR211_crm_sub_app_col_elig_tre_bills() {
		return r211_crm_sub_app_col_elig_tre_bills;
	}
	public void setR211_crm_sub_app_col_elig_tre_bills(BigDecimal r211_crm_sub_app_col_elig_tre_bills) {
		this.r211_crm_sub_app_col_elig_tre_bills = r211_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR211_crm_sub_app_col_elig_deb_sec() {
		return r211_crm_sub_app_col_elig_deb_sec;
	}
	public void setR211_crm_sub_app_col_elig_deb_sec(BigDecimal r211_crm_sub_app_col_elig_deb_sec) {
		this.r211_crm_sub_app_col_elig_deb_sec = r211_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR211_crm_sub_app_col_elig_euiq() {
		return r211_crm_sub_app_col_elig_euiq;
	}
	public void setR211_crm_sub_app_col_elig_euiq(BigDecimal r211_crm_sub_app_col_elig_euiq) {
		this.r211_crm_sub_app_col_elig_euiq = r211_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR211_crm_sub_app_col_elig_uni_tru() {
		return r211_crm_sub_app_col_elig_uni_tru;
	}
	public void setR211_crm_sub_app_col_elig_uni_tru(BigDecimal r211_crm_sub_app_col_elig_uni_tru) {
		this.r211_crm_sub_app_col_elig_uni_tru = r211_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR211_crm_sub_app_col_cea_cov() {
		return r211_crm_sub_app_col_cea_cov;
	}
	public void setR211_crm_sub_app_col_cea_cov(BigDecimal r211_crm_sub_app_col_cea_cov) {
		this.r211_crm_sub_app_col_cea_cov = r211_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR211_crm_sub_app_col_cea_not_cov() {
		return r211_crm_sub_app_col_cea_not_cov;
	}
	public void setR211_crm_sub_app_col_cea_not_cov(BigDecimal r211_crm_sub_app_col_cea_not_cov) {
		this.r211_crm_sub_app_col_cea_not_cov = r211_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR211_crm_sub_app_rwa_ris_wei_crm() {
		return r211_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR211_crm_sub_app_rwa_ris_wei_crm(BigDecimal r211_crm_sub_app_rwa_ris_wei_crm) {
		this.r211_crm_sub_app_rwa_ris_wei_crm = r211_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR211_crm_sub_app_rwa_ris_cea_cov() {
		return r211_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR211_crm_sub_app_rwa_ris_cea_cov(BigDecimal r211_crm_sub_app_rwa_ris_cea_cov) {
		this.r211_crm_sub_app_rwa_ris_cea_cov = r211_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR211_crm_sub_app_rwa_appl_org_coun() {
		return r211_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR211_crm_sub_app_rwa_appl_org_coun(BigDecimal r211_crm_sub_app_rwa_appl_org_coun) {
		this.r211_crm_sub_app_rwa_appl_org_coun = r211_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR211_crm_sub_app_rwa_ris_cea_not_cov() {
		return r211_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR211_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r211_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r211_crm_sub_app_rwa_ris_cea_not_cov = r211_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR211_crm_com_app_col_cea_elig_crm() {
		return r211_crm_com_app_col_cea_elig_crm;
	}
	public void setR211_crm_com_app_col_cea_elig_crm(BigDecimal r211_crm_com_app_col_cea_elig_crm) {
		this.r211_crm_com_app_col_cea_elig_crm = r211_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR211_crm_com_app_col_elig_cea_vol_adj() {
		return r211_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR211_crm_com_app_col_elig_cea_vol_adj(BigDecimal r211_crm_com_app_col_elig_cea_vol_adj) {
		this.r211_crm_com_app_col_elig_cea_vol_adj = r211_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR211_crm_com_app_col_elig_fin_hai() {
		return r211_crm_com_app_col_elig_fin_hai;
	}
	public void setR211_crm_com_app_col_elig_fin_hai(BigDecimal r211_crm_com_app_col_elig_fin_hai) {
		this.r211_crm_com_app_col_elig_fin_hai = r211_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR211_crm_com_app_col_cea_val_aft_crm() {
		return r211_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR211_crm_com_app_col_cea_val_aft_crm(BigDecimal r211_crm_com_app_col_cea_val_aft_crm) {
		this.r211_crm_com_app_col_cea_val_aft_crm = r211_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR211_rwa_elig_cea_not_cov() {
		return r211_rwa_elig_cea_not_cov;
	}
	public void setR211_rwa_elig_cea_not_cov(BigDecimal r211_rwa_elig_cea_not_cov) {
		this.r211_rwa_elig_cea_not_cov = r211_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR211_rwa_unsec_cea_sub_cre_ris() {
		return r211_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR211_rwa_unsec_cea_sub_cre_ris(BigDecimal r211_rwa_unsec_cea_sub_cre_ris) {
		this.r211_rwa_unsec_cea_sub_cre_ris = r211_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR211_rwa_unsec_cea() {
		return r211_rwa_unsec_cea;
	}
	public void setR211_rwa_unsec_cea(BigDecimal r211_rwa_unsec_cea) {
		this.r211_rwa_unsec_cea = r211_rwa_unsec_cea;
	}
	public BigDecimal getR211_rwa_tot_ris_wei_ass() {
		return r211_rwa_tot_ris_wei_ass;
	}
	public void setR211_rwa_tot_ris_wei_ass(BigDecimal r211_rwa_tot_ris_wei_ass) {
		this.r211_rwa_tot_ris_wei_ass = r211_rwa_tot_ris_wei_ass;
	}
	public String getR212_exposure_class_off_bal() {
		return r212_exposure_class_off_bal;
	}
	public void setR212_exposure_class_off_bal(String r212_exposure_class_off_bal) {
		this.r212_exposure_class_off_bal = r212_exposure_class_off_bal;
	}
	public BigDecimal getR212_nom_pri_amt() {
		return r212_nom_pri_amt;
	}
	public void setR212_nom_pri_amt(BigDecimal r212_nom_pri_amt) {
		this.r212_nom_pri_amt = r212_nom_pri_amt;
	}
	public BigDecimal getR212_ccf() {
		return r212_ccf;
	}
	public void setR212_ccf(BigDecimal r212_ccf) {
		this.r212_ccf = r212_ccf;
	}
	public BigDecimal getR212_cea() {
		return r212_cea;
	}
	public void setR212_cea(BigDecimal r212_cea) {
		this.r212_cea = r212_cea;
	}
	public BigDecimal getR212_cea_elig_coun_bilt_net() {
		return r212_cea_elig_coun_bilt_net;
	}
	public void setR212_cea_elig_coun_bilt_net(BigDecimal r212_cea_elig_coun_bilt_net) {
		this.r212_cea_elig_coun_bilt_net = r212_cea_elig_coun_bilt_net;
	}
	public BigDecimal getR212_cea_aft_net() {
		return r212_cea_aft_net;
	}
	public void setR212_cea_aft_net(BigDecimal r212_cea_aft_net) {
		this.r212_cea_aft_net = r212_cea_aft_net;
	}
	public BigDecimal getR212_crm_sub_app_cea_elig() {
		return r212_crm_sub_app_cea_elig;
	}
	public void setR212_crm_sub_app_cea_elig(BigDecimal r212_crm_sub_app_cea_elig) {
		this.r212_crm_sub_app_cea_elig = r212_crm_sub_app_cea_elig;
	}
	public BigDecimal getR212_crm_sub_app_non_col_guar_elig() {
		return r212_crm_sub_app_non_col_guar_elig;
	}
	public void setR212_crm_sub_app_non_col_guar_elig(BigDecimal r212_crm_sub_app_non_col_guar_elig) {
		this.r212_crm_sub_app_non_col_guar_elig = r212_crm_sub_app_non_col_guar_elig;
	}
	public BigDecimal getR212_crm_sub_app_non_col_cre_der() {
		return r212_crm_sub_app_non_col_cre_der;
	}
	public void setR212_crm_sub_app_non_col_cre_der(BigDecimal r212_crm_sub_app_non_col_cre_der) {
		this.r212_crm_sub_app_non_col_cre_der = r212_crm_sub_app_non_col_cre_der;
	}
	public BigDecimal getR212_crm_sub_app_col_elig_cash() {
		return r212_crm_sub_app_col_elig_cash;
	}
	public void setR212_crm_sub_app_col_elig_cash(BigDecimal r212_crm_sub_app_col_elig_cash) {
		this.r212_crm_sub_app_col_elig_cash = r212_crm_sub_app_col_elig_cash;
	}
	public BigDecimal getR212_crm_sub_app_col_elig_tre_bills() {
		return r212_crm_sub_app_col_elig_tre_bills;
	}
	public void setR212_crm_sub_app_col_elig_tre_bills(BigDecimal r212_crm_sub_app_col_elig_tre_bills) {
		this.r212_crm_sub_app_col_elig_tre_bills = r212_crm_sub_app_col_elig_tre_bills;
	}
	public BigDecimal getR212_crm_sub_app_col_elig_deb_sec() {
		return r212_crm_sub_app_col_elig_deb_sec;
	}
	public void setR212_crm_sub_app_col_elig_deb_sec(BigDecimal r212_crm_sub_app_col_elig_deb_sec) {
		this.r212_crm_sub_app_col_elig_deb_sec = r212_crm_sub_app_col_elig_deb_sec;
	}
	public BigDecimal getR212_crm_sub_app_col_elig_euiq() {
		return r212_crm_sub_app_col_elig_euiq;
	}
	public void setR212_crm_sub_app_col_elig_euiq(BigDecimal r212_crm_sub_app_col_elig_euiq) {
		this.r212_crm_sub_app_col_elig_euiq = r212_crm_sub_app_col_elig_euiq;
	}
	public BigDecimal getR212_crm_sub_app_col_elig_uni_tru() {
		return r212_crm_sub_app_col_elig_uni_tru;
	}
	public void setR212_crm_sub_app_col_elig_uni_tru(BigDecimal r212_crm_sub_app_col_elig_uni_tru) {
		this.r212_crm_sub_app_col_elig_uni_tru = r212_crm_sub_app_col_elig_uni_tru;
	}
	public BigDecimal getR212_crm_sub_app_col_cea_cov() {
		return r212_crm_sub_app_col_cea_cov;
	}
	public void setR212_crm_sub_app_col_cea_cov(BigDecimal r212_crm_sub_app_col_cea_cov) {
		this.r212_crm_sub_app_col_cea_cov = r212_crm_sub_app_col_cea_cov;
	}
	public BigDecimal getR212_crm_sub_app_col_cea_not_cov() {
		return r212_crm_sub_app_col_cea_not_cov;
	}
	public void setR212_crm_sub_app_col_cea_not_cov(BigDecimal r212_crm_sub_app_col_cea_not_cov) {
		this.r212_crm_sub_app_col_cea_not_cov = r212_crm_sub_app_col_cea_not_cov;
	}
	public BigDecimal getR212_crm_sub_app_rwa_ris_wei_crm() {
		return r212_crm_sub_app_rwa_ris_wei_crm;
	}
	public void setR212_crm_sub_app_rwa_ris_wei_crm(BigDecimal r212_crm_sub_app_rwa_ris_wei_crm) {
		this.r212_crm_sub_app_rwa_ris_wei_crm = r212_crm_sub_app_rwa_ris_wei_crm;
	}
	public BigDecimal getR212_crm_sub_app_rwa_ris_cea_cov() {
		return r212_crm_sub_app_rwa_ris_cea_cov;
	}
	public void setR212_crm_sub_app_rwa_ris_cea_cov(BigDecimal r212_crm_sub_app_rwa_ris_cea_cov) {
		this.r212_crm_sub_app_rwa_ris_cea_cov = r212_crm_sub_app_rwa_ris_cea_cov;
	}
	public BigDecimal getR212_crm_sub_app_rwa_appl_org_coun() {
		return r212_crm_sub_app_rwa_appl_org_coun;
	}
	public void setR212_crm_sub_app_rwa_appl_org_coun(BigDecimal r212_crm_sub_app_rwa_appl_org_coun) {
		this.r212_crm_sub_app_rwa_appl_org_coun = r212_crm_sub_app_rwa_appl_org_coun;
	}
	public BigDecimal getR212_crm_sub_app_rwa_ris_cea_not_cov() {
		return r212_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public void setR212_crm_sub_app_rwa_ris_cea_not_cov(BigDecimal r212_crm_sub_app_rwa_ris_cea_not_cov) {
		this.r212_crm_sub_app_rwa_ris_cea_not_cov = r212_crm_sub_app_rwa_ris_cea_not_cov;
	}
	public BigDecimal getR212_crm_com_app_col_cea_elig_crm() {
		return r212_crm_com_app_col_cea_elig_crm;
	}
	public void setR212_crm_com_app_col_cea_elig_crm(BigDecimal r212_crm_com_app_col_cea_elig_crm) {
		this.r212_crm_com_app_col_cea_elig_crm = r212_crm_com_app_col_cea_elig_crm;
	}
	public BigDecimal getR212_crm_com_app_col_elig_cea_vol_adj() {
		return r212_crm_com_app_col_elig_cea_vol_adj;
	}
	public void setR212_crm_com_app_col_elig_cea_vol_adj(BigDecimal r212_crm_com_app_col_elig_cea_vol_adj) {
		this.r212_crm_com_app_col_elig_cea_vol_adj = r212_crm_com_app_col_elig_cea_vol_adj;
	}
	public BigDecimal getR212_crm_com_app_col_elig_fin_hai() {
		return r212_crm_com_app_col_elig_fin_hai;
	}
	public void setR212_crm_com_app_col_elig_fin_hai(BigDecimal r212_crm_com_app_col_elig_fin_hai) {
		this.r212_crm_com_app_col_elig_fin_hai = r212_crm_com_app_col_elig_fin_hai;
	}
	public BigDecimal getR212_crm_com_app_col_cea_val_aft_crm() {
		return r212_crm_com_app_col_cea_val_aft_crm;
	}
	public void setR212_crm_com_app_col_cea_val_aft_crm(BigDecimal r212_crm_com_app_col_cea_val_aft_crm) {
		this.r212_crm_com_app_col_cea_val_aft_crm = r212_crm_com_app_col_cea_val_aft_crm;
	}
	public BigDecimal getR212_rwa_elig_cea_not_cov() {
		return r212_rwa_elig_cea_not_cov;
	}
	public void setR212_rwa_elig_cea_not_cov(BigDecimal r212_rwa_elig_cea_not_cov) {
		this.r212_rwa_elig_cea_not_cov = r212_rwa_elig_cea_not_cov;
	}
	public BigDecimal getR212_rwa_unsec_cea_sub_cre_ris() {
		return r212_rwa_unsec_cea_sub_cre_ris;
	}
	public void setR212_rwa_unsec_cea_sub_cre_ris(BigDecimal r212_rwa_unsec_cea_sub_cre_ris) {
		this.r212_rwa_unsec_cea_sub_cre_ris = r212_rwa_unsec_cea_sub_cre_ris;
	}
	public BigDecimal getR212_rwa_unsec_cea() {
		return r212_rwa_unsec_cea;
	}
	public void setR212_rwa_unsec_cea(BigDecimal r212_rwa_unsec_cea) {
		this.r212_rwa_unsec_cea = r212_rwa_unsec_cea;
	}
	public BigDecimal getR212_rwa_tot_ris_wei_ass() {
		return r212_rwa_tot_ris_wei_ass;
	}
	public void setR212_rwa_tot_ris_wei_ass(BigDecimal r212_rwa_tot_ris_wei_ass) {
		this.r212_rwa_tot_ris_wei_ass = r212_rwa_tot_ris_wei_ass;
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
	public M_SRWA_12A_NEW_Summary_Entity6() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
