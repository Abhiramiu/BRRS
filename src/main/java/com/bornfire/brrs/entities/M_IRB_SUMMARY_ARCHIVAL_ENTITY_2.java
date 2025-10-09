package com.bornfire.brrs.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BRRS_M_IRB_TABLE_ARCHVAL_SUMMARY_2")
public class M_IRB_SUMMARY_ARCHIVAL_ENTITY_2 {
	
	@Id
	private Date	report_date;
	
	private String	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;

	private String	r10_total_assets_txt;
	private BigDecimal	r10_total_assets_up_to_1_mnt;
	private BigDecimal	r10_total_assets_mor_then_1_to_3_mon;
	private BigDecimal	r10_total_assets_mor_then_3_to_6_mon;
	private BigDecimal	r10_total_assets_mor_then_6_to_12_mon;
	private BigDecimal	r10_total_assets_mor_then_12_mon_to_3_year;
	private BigDecimal	r10_total_assets_mor_then_3_to_5_year;
	private BigDecimal	r10_total_assets_mor_then_5_to_10_year;
	private BigDecimal	r10_total_assets_mor_then_10_year;
	private BigDecimal	r10_total_assets_non_rat_sens_itm;
	private BigDecimal	r10_total_assets_total;
	private String	r11_var_rate_items_txt;
	private BigDecimal	r11_var_rate_items_up_to_1_mnt;
	private BigDecimal	r11_var_rate_items_mor_then_1_to_3_mon;
	private BigDecimal	r11_var_rate_items_mor_then_3_to_6_mon;
	private BigDecimal	r11_var_rate_items_mor_then_6_to_12_mon;
	private BigDecimal	r11_var_rate_items_mor_then_12_mon_to_3_year;
	private BigDecimal	r11_var_rate_items_mor_then_3_to_5_year;
	private BigDecimal	r11_var_rate_items_mor_then_5_to_10_year;
	private BigDecimal	r11_var_rate_items_mor_then_10_year;
	private BigDecimal	r11_var_rate_items_non_rat_sens_itm;
	private BigDecimal	r11_var_rate_items_total;
	private String	r12_assets_cash_txt;
	private BigDecimal	r12_assets_cash_up_to_1_mnt;
	private BigDecimal	r12_assets_cash_mor_then_1_to_3_mon;
	private BigDecimal	r12_assets_cash_mor_then_3_to_6_mon;
	private BigDecimal	r12_assets_cash_mor_then_6_to_12_mon;
	private BigDecimal	r12_assets_cash_mor_then_12_mon_to_3_year;
	private BigDecimal	r12_assets_cash_mor_then_3_to_5_year;
	private BigDecimal	r12_assets_cash_mor_then_5_to_10_year;
	private BigDecimal	r12_assets_cash_mor_then_10_year;
	private BigDecimal	r12_assets_cash_non_rat_sens_itm;
	private BigDecimal	r12_assets_cash_total;
	private String	r13_bal_bnk_of_botswana_txt;
	private BigDecimal	r13_bal_bnk_of_botswana_up_to_1_mnt;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_1_to_3_mon;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_3_to_6_mon;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_6_to_12_mon;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_3_to_5_year;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_5_to_10_year;
	private BigDecimal	r13_bal_bnk_of_botswana_mor_then_10_year;
	private BigDecimal	r13_bal_bnk_of_botswana_non_rat_sens_itm;
	private BigDecimal	r13_bal_bnk_of_botswana_total;
	private String	r14_bal_doms_bnks_txt;
	private BigDecimal	r14_bal_doms_bnks_up_to_1_mnt;
	private BigDecimal	r14_bal_doms_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r14_bal_doms_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r14_bal_doms_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r14_bal_doms_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r14_bal_doms_bnks_mor_then_3_to_5_year;
	private BigDecimal	r14_bal_doms_bnks_mor_then_5_to_10_year;
	private BigDecimal	r14_bal_doms_bnks_mor_then_10_year;
	private BigDecimal	r14_bal_doms_bnks_non_rat_sens_itm;
	private BigDecimal	r14_bal_doms_bnks_total;
	private String	r15_bal_foreign_bnks_txt;
	private BigDecimal	r15_bal_foreign_bnks_up_to_1_mnt;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_3_to_5_year;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_5_to_10_year;
	private BigDecimal	r15_bal_foreign_bnks_mor_then_10_year;
	private BigDecimal	r15_bal_foreign_bnks_non_rat_sens_itm;
	private BigDecimal	r15_bal_foreign_bnks_total;
	private String	r16_bal_related_comp_txt;
	private BigDecimal	r16_bal_related_comp_up_to_1_mnt;
	private BigDecimal	r16_bal_related_comp_mor_then_1_to_3_mon;
	private BigDecimal	r16_bal_related_comp_mor_then_3_to_6_mon;
	private BigDecimal	r16_bal_related_comp_mor_then_6_to_12_mon;
	private BigDecimal	r16_bal_related_comp_mor_then_12_mon_to_3_year;
	private BigDecimal	r16_bal_related_comp_mor_then_3_to_5_year;
	private BigDecimal	r16_bal_related_comp_mor_then_5_to_10_year;
	private BigDecimal	r16_bal_related_comp_mor_then_10_year;
	private BigDecimal	r16_bal_related_comp_non_rat_sens_itm;
	private BigDecimal	r16_bal_related_comp_total;
	private String	r17_bnk_of_botswana_cert_txt;
	private BigDecimal	r17_bnk_of_botswana_cert_up_to_1_mnt;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_1_to_3_mon;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_3_to_6_mon;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_6_to_12_mon;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_3_to_5_year;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_5_to_10_year;
	private BigDecimal	r17_bnk_of_botswana_cert_mor_then_10_year;
	private BigDecimal	r17_bnk_of_botswana_cert_non_rat_sens_itm;
	private BigDecimal	r17_bnk_of_botswana_cert_total;
	private String	r18_gov_bonds_txt;
	private BigDecimal	r18_gov_bonds_up_to_1_mnt;
	private BigDecimal	r18_gov_bonds_mor_then_1_to_3_mon;
	private BigDecimal	r18_gov_bonds_mor_then_3_to_6_mon;
	private BigDecimal	r18_gov_bonds_mor_then_6_to_12_mon;
	private BigDecimal	r18_gov_bonds_mor_then_12_mon_to_3_year;
	private BigDecimal	r18_gov_bonds_mor_then_3_to_5_year;
	private BigDecimal	r18_gov_bonds_mor_then_5_to_10_year;
	private BigDecimal	r18_gov_bonds_mor_then_10_year;
	private BigDecimal	r18_gov_bonds_non_rat_sens_itm;
	private BigDecimal	r18_gov_bonds_total;
	private String	r19_other_invt_specify_txt;
	private BigDecimal	r19_other_invt_specify_up_to_1_mnt;
	private BigDecimal	r19_other_invt_specify_mor_then_1_to_3_mon;
	private BigDecimal	r19_other_invt_specify_mor_then_3_to_6_mon;
	private BigDecimal	r19_other_invt_specify_mor_then_6_to_12_mon;
	private BigDecimal	r19_other_invt_specify_mor_then_12_mon_to_3_year;
	private BigDecimal	r19_other_invt_specify_mor_then_3_to_5_year;
	private BigDecimal	r19_other_invt_specify_mor_then_5_to_10_year;
	private BigDecimal	r19_other_invt_specify_mor_then_10_year;
	private BigDecimal	r19_other_invt_specify_non_rat_sens_itm;
	private BigDecimal	r19_other_invt_specify_total;
	private String	r20_loans_and_adv_to_cust_txt;
	private BigDecimal	r20_loans_and_adv_to_cust_up_to_1_mnt;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_1_to_3_mon;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_3_to_6_mon;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_6_to_12_mon;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_3_to_5_year;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_5_to_10_year;
	private BigDecimal	r20_loans_and_adv_to_cust_mor_then_10_year;
	private BigDecimal	r20_loans_and_adv_to_cust_non_rat_sens_itm;
	private BigDecimal	r20_loans_and_adv_to_cust_total;
	private String	r21_prop_and_eqp_txt;
	private BigDecimal	r21_prop_and_eqp_up_to_1_mnt;
	private BigDecimal	r21_prop_and_eqp_mor_then_1_to_3_mon;
	private BigDecimal	r21_prop_and_eqp_mor_then_3_to_6_mon;
	private BigDecimal	r21_prop_and_eqp_mor_then_6_to_12_mon;
	private BigDecimal	r21_prop_and_eqp_mor_then_12_mon_to_3_year;
	private BigDecimal	r21_prop_and_eqp_mor_then_3_to_5_year;
	private BigDecimal	r21_prop_and_eqp_mor_then_5_to_10_year;
	private BigDecimal	r21_prop_and_eqp_mor_then_10_year;
	private BigDecimal	r21_prop_and_eqp_non_rat_sens_itm;
	private BigDecimal	r21_prop_and_eqp_total;
	private String	r22_other_assets_specify_txt;
	private BigDecimal	r22_other_assets_specify_up_to_1_mnt;
	private BigDecimal	r22_other_assets_specify_mor_then_1_to_3_mon;
	private BigDecimal	r22_other_assets_specify_mor_then_3_to_6_mon;
	private BigDecimal	r22_other_assets_specify_mor_then_6_to_12_mon;
	private BigDecimal	r22_other_assets_specify_mor_then_12_mon_to_3_year;
	private BigDecimal	r22_other_assets_specify_mor_then_3_to_5_year;
	private BigDecimal	r22_other_assets_specify_mor_then_5_to_10_year;
	private BigDecimal	r22_other_assets_specify_mor_then_10_year;
	private BigDecimal	r22_other_assets_specify_non_rat_sens_itm;
	private BigDecimal	r22_other_assets_specify_total;
	private String	r23_dis_admt_discrt_admt_rate_items_txt;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_up_to_1_mnt;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_mor_then_10_year;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm;
	private BigDecimal	r23_dis_admt_discrt_admt_rate_items_total;
	private String	r24_dis_admt_cash_txt;
	private BigDecimal	r24_dis_admt_cash_up_to_1_mnt;
	private BigDecimal	r24_dis_admt_cash_mor_then_1_to_3_mon;
	private BigDecimal	r24_dis_admt_cash_mor_then_3_to_6_mon;
	private BigDecimal	r24_dis_admt_cash_mor_then_6_to_12_mon;
	private BigDecimal	r24_dis_admt_cash_mor_then_12_mon_to_3_year;
	private BigDecimal	r24_dis_admt_cash_mor_then_3_to_5_year;
	private BigDecimal	r24_dis_admt_cash_mor_then_5_to_10_year;
	private BigDecimal	r24_dis_admt_cash_mor_then_10_year;
	private BigDecimal	r24_dis_admt_cash_non_rat_sens_itm;
	private BigDecimal	r24_dis_admt_cash_total;
	private String	r25_dis_admt_bal_bnk_of_botswana_txt;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_mor_then_10_year;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm;
	private BigDecimal	r25_dis_admt_bal_bnk_of_botswana_total;
	private String	r26_dis_admt_bal_doms_bnks_txt;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_up_to_1_mnt;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_mor_then_10_year;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_non_rat_sens_itm;
	private BigDecimal	r26_dis_admt_bal_doms_bnks_total;
	private String	r27_dis_admt_bal_foreign_bnks_txt;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_up_to_1_mnt;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_mor_then_10_year;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_non_rat_sens_itm;
	private BigDecimal	r27_dis_admt_bal_foreign_bnks_total;
	private String	r28_dis_admt_bal_related_comp_txt;
	private BigDecimal	r28_dis_admt_bal_related_comp_up_to_1_mnt;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_1_to_3_mon;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_3_to_6_mon;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_6_to_12_mon;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_3_to_5_year;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_5_to_10_year;
	private BigDecimal	r28_dis_admt_bal_related_comp_mor_then_10_year;
	private BigDecimal	r28_dis_admt_bal_related_comp_non_rat_sens_itm;
	private BigDecimal	r28_dis_admt_bal_related_comp_total;
	private String	r29_dis_admt_bnk_of_botswana_cert_txt;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_mor_then_10_year;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm;
	private BigDecimal	r29_dis_admt_bnk_of_botswana_cert_total;
	private String	r30_dis_admt_gov_bonds_txt;
	private BigDecimal	r30_dis_admt_gov_bonds_up_to_1_mnt;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_1_to_3_mon;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_3_to_6_mon;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_6_to_12_mon;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_3_to_5_year;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_5_to_10_year;
	private BigDecimal	r30_dis_admt_gov_bonds_mor_then_10_year;
	private BigDecimal	r30_dis_admt_gov_bonds_non_rat_sens_itm;
	private BigDecimal	r30_dis_admt_gov_bonds_total;
	private String	r31_dis_admt_other_invt_specify_txt;
	private BigDecimal	r31_dis_admt_other_invt_specify_up_to_1_mnt;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_1_to_3_mon;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_3_to_6_mon;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_6_to_12_mon;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_3_to_5_year;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_5_to_10_year;
	private BigDecimal	r31_dis_admt_other_invt_specify_mor_then_10_year;
	private BigDecimal	r31_dis_admt_other_invt_specify_non_rat_sens_itm;
	private BigDecimal	r31_dis_admt_other_invt_specify_total;
	private String	r32_dis_admt_loans_and_adv_to_cust_txt;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_mor_then_10_year;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm;
	private BigDecimal	r32_dis_admt_loans_and_adv_to_cust_total;
	private String	r33_dis_admt_prop_and_eqp_txt;
	private BigDecimal	r33_dis_admt_prop_and_eqp_up_to_1_mnt;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_3_to_5_year;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_5_to_10_year;
	private BigDecimal	r33_dis_admt_prop_and_eqp_mor_then_10_year;
	private BigDecimal	r33_dis_admt_prop_and_eqp_non_rat_sens_itm;
	private BigDecimal	r33_dis_admt_prop_and_eqp_total;
	private String	r34_dis_admt_other_assets_specify_txt;
	private BigDecimal	r34_dis_admt_other_assets_specify_up_to_1_mnt;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_1_to_3_mon;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_3_to_6_mon;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_6_to_12_mon;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_3_to_5_year;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_5_to_10_year;
	private BigDecimal	r34_dis_admt_other_assets_specify_mor_then_10_year;
	private BigDecimal	r34_dis_admt_other_assets_specify_non_rat_sens_itm;
	private BigDecimal	r34_dis_admt_other_assets_specify_total;
	private String	r35_dis_admt_fix_rate_items_txt;
	private BigDecimal	r35_dis_admt_fix_rate_items_up_to_1_mnt;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_1_to_3_mon;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_3_to_6_mon;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_6_to_12_mon;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_3_to_5_year;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_5_to_10_year;
	private BigDecimal	r35_dis_admt_fix_rate_items_mor_then_10_year;
	private BigDecimal	r35_dis_admt_fix_rate_items_non_rat_sens_itm;
	private BigDecimal	r35_dis_admt_fix_rate_items_total;
	private String	r36_fix_rate_cash_txt;
	private BigDecimal	r36_fix_rate_cash_up_to_1_mnt;
	private BigDecimal	r36_fix_rate_cash_mor_then_1_to_3_mon;
	private BigDecimal	r36_fix_rate_cash_mor_then_3_to_6_mon;
	private BigDecimal	r36_fix_rate_cash_mor_then_6_to_12_mon;
	private BigDecimal	r36_fix_rate_cash_mor_then_12_mon_to_3_year;
	private BigDecimal	r36_fix_rate_cash_mor_then_3_to_5_year;
	private BigDecimal	r36_fix_rate_cash_mor_then_5_to_10_year;
	private BigDecimal	r36_fix_rate_cash_mor_then_10_year;
	private BigDecimal	r36_fix_rate_cash_non_rat_sens_itm;
	private BigDecimal	r36_fix_rate_cash_total;
	private String	r37_fix_rate_bal_bnk_of_botswana_txt;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_mor_then_10_year;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm;
	private BigDecimal	r37_fix_rate_bal_bnk_of_botswana_total;
	private String	r38_fix_rate_bal_doms_bnks_txt;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_up_to_1_mnt;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_mor_then_10_year;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_non_rat_sens_itm;
	private BigDecimal	r38_fix_rate_bal_doms_bnks_total;
	private String	r39_fix_rate_bal_foreign_bnks_txt;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_up_to_1_mnt;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_mor_then_10_year;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_non_rat_sens_itm;
	private BigDecimal	r39_fix_rate_bal_foreign_bnks_total;
	private String	r40_fix_rate_bal_related_comp_txt;
	private BigDecimal	r40_fix_rate_bal_related_comp_up_to_1_mnt;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_1_to_3_mon;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_3_to_6_mon;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_6_to_12_mon;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_3_to_5_year;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_5_to_10_year;
	private BigDecimal	r40_fix_rate_bal_related_comp_mor_then_10_year;
	private BigDecimal	r40_fix_rate_bal_related_comp_non_rat_sens_itm;
	private BigDecimal	r40_fix_rate_bal_related_comp_total;
	private String	r41_fix_rate_bnk_of_botswana_cert_txt;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_mor_then_10_year;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm;
	private BigDecimal	r41_fix_rate_bnk_of_botswana_cert_total;
	private String	r42_fix_rate_gov_bonds_txt;
	private BigDecimal	r42_fix_rate_gov_bonds_up_to_1_mnt;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_1_to_3_mon;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_3_to_6_mon;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_6_to_12_mon;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_3_to_5_year;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_5_to_10_year;
	private BigDecimal	r42_fix_rate_gov_bonds_mor_then_10_year;
	private BigDecimal	r42_fix_rate_gov_bonds_non_rat_sens_itm;
	private BigDecimal	r42_fix_rate_gov_bonds_total;
	private String	r43_fix_rate_other_invt_specify_txt;
	private BigDecimal	r43_fix_rate_other_invt_specify_up_to_1_mnt;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_1_to_3_mon;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_3_to_6_mon;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_6_to_12_mon;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_3_to_5_year;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_5_to_10_year;
	private BigDecimal	r43_fix_rate_other_invt_specify_mor_then_10_year;
	private BigDecimal	r43_fix_rate_other_invt_specify_non_rat_sens_itm;
	private BigDecimal	r43_fix_rate_other_invt_specify_total;
	private String	r44_fix_rate_loans_and_adv_to_cust_txt;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_mor_then_10_year;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm;
	private BigDecimal	r44_fix_rate_loans_and_adv_to_cust_total;
	private String	r45_fix_rate_prop_and_eqp_txt;
	private BigDecimal	r45_fix_rate_prop_and_eqp_up_to_1_mnt;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_3_to_5_year;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_5_to_10_year;
	private BigDecimal	r45_fix_rate_prop_and_eqp_mor_then_10_year;
	private BigDecimal	r45_fix_rate_prop_and_eqp_non_rat_sens_itm;
	private BigDecimal	r45_fix_rate_prop_and_eqp_total;
	private String	r46_fix_rate_other_assets_specify_txt;
	private BigDecimal	r46_fix_rate_other_assets_specify_up_to_1_mnt;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_1_to_3_mon;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_3_to_6_mon;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_6_to_12_mon;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_3_to_5_year;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_5_to_10_year;
	private BigDecimal	r46_fix_rate_other_assets_specify_mor_then_10_year;
	private BigDecimal	r46_fix_rate_other_assets_specify_non_rat_sens_itm;
	private BigDecimal	r46_fix_rate_other_assets_specify_total;
	private String	r47_non_rate_sensitive_items_txt;
	private BigDecimal	r47_non_rate_sensitive_items_up_to_1_mnt;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_1_to_3_mon;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_3_to_6_mon;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_6_to_12_mon;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_12_mon_to_3_year;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_3_to_5_year;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_5_to_10_year;
	private BigDecimal	r47_non_rate_sensitive_items_mor_then_10_year;
	private BigDecimal	r47_non_rate_sensitive_items_non_rat_sens_itm;
	private BigDecimal	r47_non_rate_sensitive_items_total;
	private String	r48_non_rate_sens_cash_txt;
	private BigDecimal	r48_non_rate_sens_cash_up_to_1_mnt;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_1_to_3_mon;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_3_to_6_mon;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_6_to_12_mon;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_12_mon_to_3_year;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_3_to_5_year;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_5_to_10_year;
	private BigDecimal	r48_non_rate_sens_cash_mor_then_10_year;
	private BigDecimal	r48_non_rate_sens_cash_non_rat_sens_itm;
	private BigDecimal	r48_non_rate_sens_cash_total;
	private String	r49_non_rate_sens_bal_bnk_of_botswana_txt;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm;
	private BigDecimal	r49_non_rate_sens_bal_bnk_of_botswana_total;
	private String	r50_non_rate_sens_bal_doms_bnks_txt;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_up_to_1_mnt;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_mor_then_10_year;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm;
	private BigDecimal	r50_non_rate_sens_bal_doms_bnks_total;
	private String	r51_non_rate_sens_bal_foreign_bnks_txt;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_mor_then_10_year;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm;
	private BigDecimal	r51_non_rate_sens_bal_foreign_bnks_total;
	private String	r52_non_rate_sens_bal_related_comp_txt;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_up_to_1_mnt;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_mor_then_10_year;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_non_rat_sens_itm;
	private BigDecimal	r52_non_rate_sens_bal_related_comp_total;
	private String	r53_non_rate_sens_bnk_of_botswana_cert_txt;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm;
	private BigDecimal	r53_non_rate_sens_bnk_of_botswana_cert_total;

    public String getReport_desc() {
        return report_desc;
    }

    public void setReport_desc(String report_desc) {
        this.report_desc = report_desc;
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

    public String getR10_total_assets_txt() {
        return r10_total_assets_txt;
    }

    public void setR10_total_assets_txt(String r10_total_assets_txt) {
        this.r10_total_assets_txt = r10_total_assets_txt;
    }

    public BigDecimal getR10_total_assets_up_to_1_mnt() {
        return r10_total_assets_up_to_1_mnt;
    }

    public void setR10_total_assets_up_to_1_mnt(BigDecimal r10_total_assets_up_to_1_mnt) {
        this.r10_total_assets_up_to_1_mnt = r10_total_assets_up_to_1_mnt;
    }

    public BigDecimal getR10_total_assets_mor_then_1_to_3_mon() {
        return r10_total_assets_mor_then_1_to_3_mon;
    }

    public void setR10_total_assets_mor_then_1_to_3_mon(BigDecimal r10_total_assets_mor_then_1_to_3_mon) {
        this.r10_total_assets_mor_then_1_to_3_mon = r10_total_assets_mor_then_1_to_3_mon;
    }

    public BigDecimal getR10_total_assets_mor_then_3_to_6_mon() {
        return r10_total_assets_mor_then_3_to_6_mon;
    }

    public void setR10_total_assets_mor_then_3_to_6_mon(BigDecimal r10_total_assets_mor_then_3_to_6_mon) {
        this.r10_total_assets_mor_then_3_to_6_mon = r10_total_assets_mor_then_3_to_6_mon;
    }

    public BigDecimal getR10_total_assets_mor_then_6_to_12_mon() {
        return r10_total_assets_mor_then_6_to_12_mon;
    }

    public void setR10_total_assets_mor_then_6_to_12_mon(BigDecimal r10_total_assets_mor_then_6_to_12_mon) {
        this.r10_total_assets_mor_then_6_to_12_mon = r10_total_assets_mor_then_6_to_12_mon;
    }

    public BigDecimal getR10_total_assets_mor_then_12_mon_to_3_year() {
        return r10_total_assets_mor_then_12_mon_to_3_year;
    }

    public void setR10_total_assets_mor_then_12_mon_to_3_year(BigDecimal r10_total_assets_mor_then_12_mon_to_3_year) {
        this.r10_total_assets_mor_then_12_mon_to_3_year = r10_total_assets_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR10_total_assets_mor_then_3_to_5_year() {
        return r10_total_assets_mor_then_3_to_5_year;
    }

    public void setR10_total_assets_mor_then_3_to_5_year(BigDecimal r10_total_assets_mor_then_3_to_5_year) {
        this.r10_total_assets_mor_then_3_to_5_year = r10_total_assets_mor_then_3_to_5_year;
    }

    public BigDecimal getR10_total_assets_mor_then_5_to_10_year() {
        return r10_total_assets_mor_then_5_to_10_year;
    }

    public void setR10_total_assets_mor_then_5_to_10_year(BigDecimal r10_total_assets_mor_then_5_to_10_year) {
        this.r10_total_assets_mor_then_5_to_10_year = r10_total_assets_mor_then_5_to_10_year;
    }

    public BigDecimal getR10_total_assets_mor_then_10_year() {
        return r10_total_assets_mor_then_10_year;
    }

    public void setR10_total_assets_mor_then_10_year(BigDecimal r10_total_assets_mor_then_10_year) {
        this.r10_total_assets_mor_then_10_year = r10_total_assets_mor_then_10_year;
    }

    public BigDecimal getR10_total_assets_non_rat_sens_itm() {
        return r10_total_assets_non_rat_sens_itm;
    }

    public void setR10_total_assets_non_rat_sens_itm(BigDecimal r10_total_assets_non_rat_sens_itm) {
        this.r10_total_assets_non_rat_sens_itm = r10_total_assets_non_rat_sens_itm;
    }

    public BigDecimal getR10_total_assets_total() {
        return r10_total_assets_total;
    }

    public void setR10_total_assets_total(BigDecimal r10_total_assets_total) {
        this.r10_total_assets_total = r10_total_assets_total;
    }

    public String getR11_var_rate_items_txt() {
        return r11_var_rate_items_txt;
    }

    public void setR11_var_rate_items_txt(String r11_var_rate_items_txt) {
        this.r11_var_rate_items_txt = r11_var_rate_items_txt;
    }

    public BigDecimal getR11_var_rate_items_up_to_1_mnt() {
        return r11_var_rate_items_up_to_1_mnt;
    }

    public void setR11_var_rate_items_up_to_1_mnt(BigDecimal r11_var_rate_items_up_to_1_mnt) {
        this.r11_var_rate_items_up_to_1_mnt = r11_var_rate_items_up_to_1_mnt;
    }

    public BigDecimal getR11_var_rate_items_mor_then_1_to_3_mon() {
        return r11_var_rate_items_mor_then_1_to_3_mon;
    }

    public void setR11_var_rate_items_mor_then_1_to_3_mon(BigDecimal r11_var_rate_items_mor_then_1_to_3_mon) {
        this.r11_var_rate_items_mor_then_1_to_3_mon = r11_var_rate_items_mor_then_1_to_3_mon;
    }

    public BigDecimal getR11_var_rate_items_mor_then_3_to_6_mon() {
        return r11_var_rate_items_mor_then_3_to_6_mon;
    }

    public void setR11_var_rate_items_mor_then_3_to_6_mon(BigDecimal r11_var_rate_items_mor_then_3_to_6_mon) {
        this.r11_var_rate_items_mor_then_3_to_6_mon = r11_var_rate_items_mor_then_3_to_6_mon;
    }

    public BigDecimal getR11_var_rate_items_mor_then_6_to_12_mon() {
        return r11_var_rate_items_mor_then_6_to_12_mon;
    }

    public void setR11_var_rate_items_mor_then_6_to_12_mon(BigDecimal r11_var_rate_items_mor_then_6_to_12_mon) {
        this.r11_var_rate_items_mor_then_6_to_12_mon = r11_var_rate_items_mor_then_6_to_12_mon;
    }

    public BigDecimal getR11_var_rate_items_mor_then_12_mon_to_3_year() {
        return r11_var_rate_items_mor_then_12_mon_to_3_year;
    }

    public void setR11_var_rate_items_mor_then_12_mon_to_3_year(BigDecimal r11_var_rate_items_mor_then_12_mon_to_3_year) {
        this.r11_var_rate_items_mor_then_12_mon_to_3_year = r11_var_rate_items_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR11_var_rate_items_mor_then_3_to_5_year() {
        return r11_var_rate_items_mor_then_3_to_5_year;
    }

    public void setR11_var_rate_items_mor_then_3_to_5_year(BigDecimal r11_var_rate_items_mor_then_3_to_5_year) {
        this.r11_var_rate_items_mor_then_3_to_5_year = r11_var_rate_items_mor_then_3_to_5_year;
    }

    public BigDecimal getR11_var_rate_items_mor_then_5_to_10_year() {
        return r11_var_rate_items_mor_then_5_to_10_year;
    }

    public void setR11_var_rate_items_mor_then_5_to_10_year(BigDecimal r11_var_rate_items_mor_then_5_to_10_year) {
        this.r11_var_rate_items_mor_then_5_to_10_year = r11_var_rate_items_mor_then_5_to_10_year;
    }

    public BigDecimal getR11_var_rate_items_mor_then_10_year() {
        return r11_var_rate_items_mor_then_10_year;
    }

    public void setR11_var_rate_items_mor_then_10_year(BigDecimal r11_var_rate_items_mor_then_10_year) {
        this.r11_var_rate_items_mor_then_10_year = r11_var_rate_items_mor_then_10_year;
    }

    public BigDecimal getR11_var_rate_items_non_rat_sens_itm() {
        return r11_var_rate_items_non_rat_sens_itm;
    }

    public void setR11_var_rate_items_non_rat_sens_itm(BigDecimal r11_var_rate_items_non_rat_sens_itm) {
        this.r11_var_rate_items_non_rat_sens_itm = r11_var_rate_items_non_rat_sens_itm;
    }

    public BigDecimal getR11_var_rate_items_total() {
        return r11_var_rate_items_total;
    }

    public void setR11_var_rate_items_total(BigDecimal r11_var_rate_items_total) {
        this.r11_var_rate_items_total = r11_var_rate_items_total;
    }

    public String getR12_assets_cash_txt() {
        return r12_assets_cash_txt;
    }

    public void setR12_assets_cash_txt(String r12_assets_cash_txt) {
        this.r12_assets_cash_txt = r12_assets_cash_txt;
    }

    public BigDecimal getR12_assets_cash_up_to_1_mnt() {
        return r12_assets_cash_up_to_1_mnt;
    }

    public void setR12_assets_cash_up_to_1_mnt(BigDecimal r12_assets_cash_up_to_1_mnt) {
        this.r12_assets_cash_up_to_1_mnt = r12_assets_cash_up_to_1_mnt;
    }

    public BigDecimal getR12_assets_cash_mor_then_1_to_3_mon() {
        return r12_assets_cash_mor_then_1_to_3_mon;
    }

    public void setR12_assets_cash_mor_then_1_to_3_mon(BigDecimal r12_assets_cash_mor_then_1_to_3_mon) {
        this.r12_assets_cash_mor_then_1_to_3_mon = r12_assets_cash_mor_then_1_to_3_mon;
    }

    public BigDecimal getR12_assets_cash_mor_then_3_to_6_mon() {
        return r12_assets_cash_mor_then_3_to_6_mon;
    }

    public void setR12_assets_cash_mor_then_3_to_6_mon(BigDecimal r12_assets_cash_mor_then_3_to_6_mon) {
        this.r12_assets_cash_mor_then_3_to_6_mon = r12_assets_cash_mor_then_3_to_6_mon;
    }

    public BigDecimal getR12_assets_cash_mor_then_6_to_12_mon() {
        return r12_assets_cash_mor_then_6_to_12_mon;
    }

    public void setR12_assets_cash_mor_then_6_to_12_mon(BigDecimal r12_assets_cash_mor_then_6_to_12_mon) {
        this.r12_assets_cash_mor_then_6_to_12_mon = r12_assets_cash_mor_then_6_to_12_mon;
    }

    public BigDecimal getR12_assets_cash_mor_then_12_mon_to_3_year() {
        return r12_assets_cash_mor_then_12_mon_to_3_year;
    }

    public void setR12_assets_cash_mor_then_12_mon_to_3_year(BigDecimal r12_assets_cash_mor_then_12_mon_to_3_year) {
        this.r12_assets_cash_mor_then_12_mon_to_3_year = r12_assets_cash_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR12_assets_cash_mor_then_3_to_5_year() {
        return r12_assets_cash_mor_then_3_to_5_year;
    }

    public void setR12_assets_cash_mor_then_3_to_5_year(BigDecimal r12_assets_cash_mor_then_3_to_5_year) {
        this.r12_assets_cash_mor_then_3_to_5_year = r12_assets_cash_mor_then_3_to_5_year;
    }

    public BigDecimal getR12_assets_cash_mor_then_5_to_10_year() {
        return r12_assets_cash_mor_then_5_to_10_year;
    }

    public void setR12_assets_cash_mor_then_5_to_10_year(BigDecimal r12_assets_cash_mor_then_5_to_10_year) {
        this.r12_assets_cash_mor_then_5_to_10_year = r12_assets_cash_mor_then_5_to_10_year;
    }

    public BigDecimal getR12_assets_cash_mor_then_10_year() {
        return r12_assets_cash_mor_then_10_year;
    }

    public void setR12_assets_cash_mor_then_10_year(BigDecimal r12_assets_cash_mor_then_10_year) {
        this.r12_assets_cash_mor_then_10_year = r12_assets_cash_mor_then_10_year;
    }

    public BigDecimal getR12_assets_cash_non_rat_sens_itm() {
        return r12_assets_cash_non_rat_sens_itm;
    }

    public void setR12_assets_cash_non_rat_sens_itm(BigDecimal r12_assets_cash_non_rat_sens_itm) {
        this.r12_assets_cash_non_rat_sens_itm = r12_assets_cash_non_rat_sens_itm;
    }

    public BigDecimal getR12_assets_cash_total() {
        return r12_assets_cash_total;
    }

    public void setR12_assets_cash_total(BigDecimal r12_assets_cash_total) {
        this.r12_assets_cash_total = r12_assets_cash_total;
    }

    public String getR13_bal_bnk_of_botswana_txt() {
        return r13_bal_bnk_of_botswana_txt;
    }

    public void setR13_bal_bnk_of_botswana_txt(String r13_bal_bnk_of_botswana_txt) {
        this.r13_bal_bnk_of_botswana_txt = r13_bal_bnk_of_botswana_txt;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_up_to_1_mnt() {
        return r13_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public void setR13_bal_bnk_of_botswana_up_to_1_mnt(BigDecimal r13_bal_bnk_of_botswana_up_to_1_mnt) {
        this.r13_bal_bnk_of_botswana_up_to_1_mnt = r13_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_1_to_3_mon() {
        return r13_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_1_to_3_mon(BigDecimal r13_bal_bnk_of_botswana_mor_then_1_to_3_mon) {
        this.r13_bal_bnk_of_botswana_mor_then_1_to_3_mon = r13_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_3_to_6_mon() {
        return r13_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_3_to_6_mon(BigDecimal r13_bal_bnk_of_botswana_mor_then_3_to_6_mon) {
        this.r13_bal_bnk_of_botswana_mor_then_3_to_6_mon = r13_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_6_to_12_mon() {
        return r13_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_6_to_12_mon(BigDecimal r13_bal_bnk_of_botswana_mor_then_6_to_12_mon) {
        this.r13_bal_bnk_of_botswana_mor_then_6_to_12_mon = r13_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() {
        return r13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year(BigDecimal r13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year) {
        this.r13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year = r13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_3_to_5_year() {
        return r13_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_3_to_5_year(BigDecimal r13_bal_bnk_of_botswana_mor_then_3_to_5_year) {
        this.r13_bal_bnk_of_botswana_mor_then_3_to_5_year = r13_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_5_to_10_year() {
        return r13_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_5_to_10_year(BigDecimal r13_bal_bnk_of_botswana_mor_then_5_to_10_year) {
        this.r13_bal_bnk_of_botswana_mor_then_5_to_10_year = r13_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_mor_then_10_year() {
        return r13_bal_bnk_of_botswana_mor_then_10_year;
    }

    public void setR13_bal_bnk_of_botswana_mor_then_10_year(BigDecimal r13_bal_bnk_of_botswana_mor_then_10_year) {
        this.r13_bal_bnk_of_botswana_mor_then_10_year = r13_bal_bnk_of_botswana_mor_then_10_year;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_non_rat_sens_itm() {
        return r13_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public void setR13_bal_bnk_of_botswana_non_rat_sens_itm(BigDecimal r13_bal_bnk_of_botswana_non_rat_sens_itm) {
        this.r13_bal_bnk_of_botswana_non_rat_sens_itm = r13_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public BigDecimal getR13_bal_bnk_of_botswana_total() {
        return r13_bal_bnk_of_botswana_total;
    }

    public void setR13_bal_bnk_of_botswana_total(BigDecimal r13_bal_bnk_of_botswana_total) {
        this.r13_bal_bnk_of_botswana_total = r13_bal_bnk_of_botswana_total;
    }

    public String getR14_bal_doms_bnks_txt() {
        return r14_bal_doms_bnks_txt;
    }

    public void setR14_bal_doms_bnks_txt(String r14_bal_doms_bnks_txt) {
        this.r14_bal_doms_bnks_txt = r14_bal_doms_bnks_txt;
    }

    public BigDecimal getR14_bal_doms_bnks_up_to_1_mnt() {
        return r14_bal_doms_bnks_up_to_1_mnt;
    }

    public void setR14_bal_doms_bnks_up_to_1_mnt(BigDecimal r14_bal_doms_bnks_up_to_1_mnt) {
        this.r14_bal_doms_bnks_up_to_1_mnt = r14_bal_doms_bnks_up_to_1_mnt;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_1_to_3_mon() {
        return r14_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public void setR14_bal_doms_bnks_mor_then_1_to_3_mon(BigDecimal r14_bal_doms_bnks_mor_then_1_to_3_mon) {
        this.r14_bal_doms_bnks_mor_then_1_to_3_mon = r14_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_3_to_6_mon() {
        return r14_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public void setR14_bal_doms_bnks_mor_then_3_to_6_mon(BigDecimal r14_bal_doms_bnks_mor_then_3_to_6_mon) {
        this.r14_bal_doms_bnks_mor_then_3_to_6_mon = r14_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_6_to_12_mon() {
        return r14_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public void setR14_bal_doms_bnks_mor_then_6_to_12_mon(BigDecimal r14_bal_doms_bnks_mor_then_6_to_12_mon) {
        this.r14_bal_doms_bnks_mor_then_6_to_12_mon = r14_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_12_mon_to_3_year() {
        return r14_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR14_bal_doms_bnks_mor_then_12_mon_to_3_year(BigDecimal r14_bal_doms_bnks_mor_then_12_mon_to_3_year) {
        this.r14_bal_doms_bnks_mor_then_12_mon_to_3_year = r14_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_3_to_5_year() {
        return r14_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public void setR14_bal_doms_bnks_mor_then_3_to_5_year(BigDecimal r14_bal_doms_bnks_mor_then_3_to_5_year) {
        this.r14_bal_doms_bnks_mor_then_3_to_5_year = r14_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_5_to_10_year() {
        return r14_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public void setR14_bal_doms_bnks_mor_then_5_to_10_year(BigDecimal r14_bal_doms_bnks_mor_then_5_to_10_year) {
        this.r14_bal_doms_bnks_mor_then_5_to_10_year = r14_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR14_bal_doms_bnks_mor_then_10_year() {
        return r14_bal_doms_bnks_mor_then_10_year;
    }

    public void setR14_bal_doms_bnks_mor_then_10_year(BigDecimal r14_bal_doms_bnks_mor_then_10_year) {
        this.r14_bal_doms_bnks_mor_then_10_year = r14_bal_doms_bnks_mor_then_10_year;
    }

    public BigDecimal getR14_bal_doms_bnks_non_rat_sens_itm() {
        return r14_bal_doms_bnks_non_rat_sens_itm;
    }

    public void setR14_bal_doms_bnks_non_rat_sens_itm(BigDecimal r14_bal_doms_bnks_non_rat_sens_itm) {
        this.r14_bal_doms_bnks_non_rat_sens_itm = r14_bal_doms_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR14_bal_doms_bnks_total() {
        return r14_bal_doms_bnks_total;
    }

    public void setR14_bal_doms_bnks_total(BigDecimal r14_bal_doms_bnks_total) {
        this.r14_bal_doms_bnks_total = r14_bal_doms_bnks_total;
    }

    public String getR15_bal_foreign_bnks_txt() {
        return r15_bal_foreign_bnks_txt;
    }

    public void setR15_bal_foreign_bnks_txt(String r15_bal_foreign_bnks_txt) {
        this.r15_bal_foreign_bnks_txt = r15_bal_foreign_bnks_txt;
    }

    public BigDecimal getR15_bal_foreign_bnks_up_to_1_mnt() {
        return r15_bal_foreign_bnks_up_to_1_mnt;
    }

    public void setR15_bal_foreign_bnks_up_to_1_mnt(BigDecimal r15_bal_foreign_bnks_up_to_1_mnt) {
        this.r15_bal_foreign_bnks_up_to_1_mnt = r15_bal_foreign_bnks_up_to_1_mnt;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_1_to_3_mon() {
        return r15_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public void setR15_bal_foreign_bnks_mor_then_1_to_3_mon(BigDecimal r15_bal_foreign_bnks_mor_then_1_to_3_mon) {
        this.r15_bal_foreign_bnks_mor_then_1_to_3_mon = r15_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_3_to_6_mon() {
        return r15_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public void setR15_bal_foreign_bnks_mor_then_3_to_6_mon(BigDecimal r15_bal_foreign_bnks_mor_then_3_to_6_mon) {
        this.r15_bal_foreign_bnks_mor_then_3_to_6_mon = r15_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_6_to_12_mon() {
        return r15_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public void setR15_bal_foreign_bnks_mor_then_6_to_12_mon(BigDecimal r15_bal_foreign_bnks_mor_then_6_to_12_mon) {
        this.r15_bal_foreign_bnks_mor_then_6_to_12_mon = r15_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_12_mon_to_3_year() {
        return r15_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR15_bal_foreign_bnks_mor_then_12_mon_to_3_year(BigDecimal r15_bal_foreign_bnks_mor_then_12_mon_to_3_year) {
        this.r15_bal_foreign_bnks_mor_then_12_mon_to_3_year = r15_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_3_to_5_year() {
        return r15_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public void setR15_bal_foreign_bnks_mor_then_3_to_5_year(BigDecimal r15_bal_foreign_bnks_mor_then_3_to_5_year) {
        this.r15_bal_foreign_bnks_mor_then_3_to_5_year = r15_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_5_to_10_year() {
        return r15_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public void setR15_bal_foreign_bnks_mor_then_5_to_10_year(BigDecimal r15_bal_foreign_bnks_mor_then_5_to_10_year) {
        this.r15_bal_foreign_bnks_mor_then_5_to_10_year = r15_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR15_bal_foreign_bnks_mor_then_10_year() {
        return r15_bal_foreign_bnks_mor_then_10_year;
    }

    public void setR15_bal_foreign_bnks_mor_then_10_year(BigDecimal r15_bal_foreign_bnks_mor_then_10_year) {
        this.r15_bal_foreign_bnks_mor_then_10_year = r15_bal_foreign_bnks_mor_then_10_year;
    }

    public BigDecimal getR15_bal_foreign_bnks_non_rat_sens_itm() {
        return r15_bal_foreign_bnks_non_rat_sens_itm;
    }

    public void setR15_bal_foreign_bnks_non_rat_sens_itm(BigDecimal r15_bal_foreign_bnks_non_rat_sens_itm) {
        this.r15_bal_foreign_bnks_non_rat_sens_itm = r15_bal_foreign_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR15_bal_foreign_bnks_total() {
        return r15_bal_foreign_bnks_total;
    }

    public void setR15_bal_foreign_bnks_total(BigDecimal r15_bal_foreign_bnks_total) {
        this.r15_bal_foreign_bnks_total = r15_bal_foreign_bnks_total;
    }

    public String getR16_bal_related_comp_txt() {
        return r16_bal_related_comp_txt;
    }

    public void setR16_bal_related_comp_txt(String r16_bal_related_comp_txt) {
        this.r16_bal_related_comp_txt = r16_bal_related_comp_txt;
    }

    public BigDecimal getR16_bal_related_comp_up_to_1_mnt() {
        return r16_bal_related_comp_up_to_1_mnt;
    }

    public void setR16_bal_related_comp_up_to_1_mnt(BigDecimal r16_bal_related_comp_up_to_1_mnt) {
        this.r16_bal_related_comp_up_to_1_mnt = r16_bal_related_comp_up_to_1_mnt;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_1_to_3_mon() {
        return r16_bal_related_comp_mor_then_1_to_3_mon;
    }

    public void setR16_bal_related_comp_mor_then_1_to_3_mon(BigDecimal r16_bal_related_comp_mor_then_1_to_3_mon) {
        this.r16_bal_related_comp_mor_then_1_to_3_mon = r16_bal_related_comp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_3_to_6_mon() {
        return r16_bal_related_comp_mor_then_3_to_6_mon;
    }

    public void setR16_bal_related_comp_mor_then_3_to_6_mon(BigDecimal r16_bal_related_comp_mor_then_3_to_6_mon) {
        this.r16_bal_related_comp_mor_then_3_to_6_mon = r16_bal_related_comp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_6_to_12_mon() {
        return r16_bal_related_comp_mor_then_6_to_12_mon;
    }

    public void setR16_bal_related_comp_mor_then_6_to_12_mon(BigDecimal r16_bal_related_comp_mor_then_6_to_12_mon) {
        this.r16_bal_related_comp_mor_then_6_to_12_mon = r16_bal_related_comp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_12_mon_to_3_year() {
        return r16_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public void setR16_bal_related_comp_mor_then_12_mon_to_3_year(BigDecimal r16_bal_related_comp_mor_then_12_mon_to_3_year) {
        this.r16_bal_related_comp_mor_then_12_mon_to_3_year = r16_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_3_to_5_year() {
        return r16_bal_related_comp_mor_then_3_to_5_year;
    }

    public void setR16_bal_related_comp_mor_then_3_to_5_year(BigDecimal r16_bal_related_comp_mor_then_3_to_5_year) {
        this.r16_bal_related_comp_mor_then_3_to_5_year = r16_bal_related_comp_mor_then_3_to_5_year;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_5_to_10_year() {
        return r16_bal_related_comp_mor_then_5_to_10_year;
    }

    public void setR16_bal_related_comp_mor_then_5_to_10_year(BigDecimal r16_bal_related_comp_mor_then_5_to_10_year) {
        this.r16_bal_related_comp_mor_then_5_to_10_year = r16_bal_related_comp_mor_then_5_to_10_year;
    }

    public BigDecimal getR16_bal_related_comp_mor_then_10_year() {
        return r16_bal_related_comp_mor_then_10_year;
    }

    public void setR16_bal_related_comp_mor_then_10_year(BigDecimal r16_bal_related_comp_mor_then_10_year) {
        this.r16_bal_related_comp_mor_then_10_year = r16_bal_related_comp_mor_then_10_year;
    }

    public BigDecimal getR16_bal_related_comp_non_rat_sens_itm() {
        return r16_bal_related_comp_non_rat_sens_itm;
    }

    public void setR16_bal_related_comp_non_rat_sens_itm(BigDecimal r16_bal_related_comp_non_rat_sens_itm) {
        this.r16_bal_related_comp_non_rat_sens_itm = r16_bal_related_comp_non_rat_sens_itm;
    }

    public BigDecimal getR16_bal_related_comp_total() {
        return r16_bal_related_comp_total;
    }

    public void setR16_bal_related_comp_total(BigDecimal r16_bal_related_comp_total) {
        this.r16_bal_related_comp_total = r16_bal_related_comp_total;
    }

    public String getR17_bnk_of_botswana_cert_txt() {
        return r17_bnk_of_botswana_cert_txt;
    }

    public void setR17_bnk_of_botswana_cert_txt(String r17_bnk_of_botswana_cert_txt) {
        this.r17_bnk_of_botswana_cert_txt = r17_bnk_of_botswana_cert_txt;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_up_to_1_mnt() {
        return r17_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public void setR17_bnk_of_botswana_cert_up_to_1_mnt(BigDecimal r17_bnk_of_botswana_cert_up_to_1_mnt) {
        this.r17_bnk_of_botswana_cert_up_to_1_mnt = r17_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_1_to_3_mon() {
        return r17_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_1_to_3_mon(BigDecimal r17_bnk_of_botswana_cert_mor_then_1_to_3_mon) {
        this.r17_bnk_of_botswana_cert_mor_then_1_to_3_mon = r17_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_3_to_6_mon() {
        return r17_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_3_to_6_mon(BigDecimal r17_bnk_of_botswana_cert_mor_then_3_to_6_mon) {
        this.r17_bnk_of_botswana_cert_mor_then_3_to_6_mon = r17_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_6_to_12_mon() {
        return r17_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_6_to_12_mon(BigDecimal r17_bnk_of_botswana_cert_mor_then_6_to_12_mon) {
        this.r17_bnk_of_botswana_cert_mor_then_6_to_12_mon = r17_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() {
        return r17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year(BigDecimal r17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year) {
        this.r17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year = r17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_3_to_5_year() {
        return r17_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_3_to_5_year(BigDecimal r17_bnk_of_botswana_cert_mor_then_3_to_5_year) {
        this.r17_bnk_of_botswana_cert_mor_then_3_to_5_year = r17_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_5_to_10_year() {
        return r17_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_5_to_10_year(BigDecimal r17_bnk_of_botswana_cert_mor_then_5_to_10_year) {
        this.r17_bnk_of_botswana_cert_mor_then_5_to_10_year = r17_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_mor_then_10_year() {
        return r17_bnk_of_botswana_cert_mor_then_10_year;
    }

    public void setR17_bnk_of_botswana_cert_mor_then_10_year(BigDecimal r17_bnk_of_botswana_cert_mor_then_10_year) {
        this.r17_bnk_of_botswana_cert_mor_then_10_year = r17_bnk_of_botswana_cert_mor_then_10_year;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_non_rat_sens_itm() {
        return r17_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public void setR17_bnk_of_botswana_cert_non_rat_sens_itm(BigDecimal r17_bnk_of_botswana_cert_non_rat_sens_itm) {
        this.r17_bnk_of_botswana_cert_non_rat_sens_itm = r17_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public BigDecimal getR17_bnk_of_botswana_cert_total() {
        return r17_bnk_of_botswana_cert_total;
    }

    public void setR17_bnk_of_botswana_cert_total(BigDecimal r17_bnk_of_botswana_cert_total) {
        this.r17_bnk_of_botswana_cert_total = r17_bnk_of_botswana_cert_total;
    }

    public String getR18_gov_bonds_txt() {
        return r18_gov_bonds_txt;
    }

    public void setR18_gov_bonds_txt(String r18_gov_bonds_txt) {
        this.r18_gov_bonds_txt = r18_gov_bonds_txt;
    }

    public BigDecimal getR18_gov_bonds_up_to_1_mnt() {
        return r18_gov_bonds_up_to_1_mnt;
    }

    public void setR18_gov_bonds_up_to_1_mnt(BigDecimal r18_gov_bonds_up_to_1_mnt) {
        this.r18_gov_bonds_up_to_1_mnt = r18_gov_bonds_up_to_1_mnt;
    }

    public BigDecimal getR18_gov_bonds_mor_then_1_to_3_mon() {
        return r18_gov_bonds_mor_then_1_to_3_mon;
    }

    public void setR18_gov_bonds_mor_then_1_to_3_mon(BigDecimal r18_gov_bonds_mor_then_1_to_3_mon) {
        this.r18_gov_bonds_mor_then_1_to_3_mon = r18_gov_bonds_mor_then_1_to_3_mon;
    }

    public BigDecimal getR18_gov_bonds_mor_then_3_to_6_mon() {
        return r18_gov_bonds_mor_then_3_to_6_mon;
    }

    public void setR18_gov_bonds_mor_then_3_to_6_mon(BigDecimal r18_gov_bonds_mor_then_3_to_6_mon) {
        this.r18_gov_bonds_mor_then_3_to_6_mon = r18_gov_bonds_mor_then_3_to_6_mon;
    }

    public BigDecimal getR18_gov_bonds_mor_then_6_to_12_mon() {
        return r18_gov_bonds_mor_then_6_to_12_mon;
    }

    public void setR18_gov_bonds_mor_then_6_to_12_mon(BigDecimal r18_gov_bonds_mor_then_6_to_12_mon) {
        this.r18_gov_bonds_mor_then_6_to_12_mon = r18_gov_bonds_mor_then_6_to_12_mon;
    }

    public BigDecimal getR18_gov_bonds_mor_then_12_mon_to_3_year() {
        return r18_gov_bonds_mor_then_12_mon_to_3_year;
    }

    public void setR18_gov_bonds_mor_then_12_mon_to_3_year(BigDecimal r18_gov_bonds_mor_then_12_mon_to_3_year) {
        this.r18_gov_bonds_mor_then_12_mon_to_3_year = r18_gov_bonds_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR18_gov_bonds_mor_then_3_to_5_year() {
        return r18_gov_bonds_mor_then_3_to_5_year;
    }

    public void setR18_gov_bonds_mor_then_3_to_5_year(BigDecimal r18_gov_bonds_mor_then_3_to_5_year) {
        this.r18_gov_bonds_mor_then_3_to_5_year = r18_gov_bonds_mor_then_3_to_5_year;
    }

    public BigDecimal getR18_gov_bonds_mor_then_5_to_10_year() {
        return r18_gov_bonds_mor_then_5_to_10_year;
    }

    public void setR18_gov_bonds_mor_then_5_to_10_year(BigDecimal r18_gov_bonds_mor_then_5_to_10_year) {
        this.r18_gov_bonds_mor_then_5_to_10_year = r18_gov_bonds_mor_then_5_to_10_year;
    }

    public BigDecimal getR18_gov_bonds_mor_then_10_year() {
        return r18_gov_bonds_mor_then_10_year;
    }

    public void setR18_gov_bonds_mor_then_10_year(BigDecimal r18_gov_bonds_mor_then_10_year) {
        this.r18_gov_bonds_mor_then_10_year = r18_gov_bonds_mor_then_10_year;
    }

    public BigDecimal getR18_gov_bonds_non_rat_sens_itm() {
        return r18_gov_bonds_non_rat_sens_itm;
    }

    public void setR18_gov_bonds_non_rat_sens_itm(BigDecimal r18_gov_bonds_non_rat_sens_itm) {
        this.r18_gov_bonds_non_rat_sens_itm = r18_gov_bonds_non_rat_sens_itm;
    }

    public BigDecimal getR18_gov_bonds_total() {
        return r18_gov_bonds_total;
    }

    public void setR18_gov_bonds_total(BigDecimal r18_gov_bonds_total) {
        this.r18_gov_bonds_total = r18_gov_bonds_total;
    }

    public String getR19_other_invt_specify_txt() {
        return r19_other_invt_specify_txt;
    }

    public void setR19_other_invt_specify_txt(String r19_other_invt_specify_txt) {
        this.r19_other_invt_specify_txt = r19_other_invt_specify_txt;
    }

    public BigDecimal getR19_other_invt_specify_up_to_1_mnt() {
        return r19_other_invt_specify_up_to_1_mnt;
    }

    public void setR19_other_invt_specify_up_to_1_mnt(BigDecimal r19_other_invt_specify_up_to_1_mnt) {
        this.r19_other_invt_specify_up_to_1_mnt = r19_other_invt_specify_up_to_1_mnt;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_1_to_3_mon() {
        return r19_other_invt_specify_mor_then_1_to_3_mon;
    }

    public void setR19_other_invt_specify_mor_then_1_to_3_mon(BigDecimal r19_other_invt_specify_mor_then_1_to_3_mon) {
        this.r19_other_invt_specify_mor_then_1_to_3_mon = r19_other_invt_specify_mor_then_1_to_3_mon;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_3_to_6_mon() {
        return r19_other_invt_specify_mor_then_3_to_6_mon;
    }

    public void setR19_other_invt_specify_mor_then_3_to_6_mon(BigDecimal r19_other_invt_specify_mor_then_3_to_6_mon) {
        this.r19_other_invt_specify_mor_then_3_to_6_mon = r19_other_invt_specify_mor_then_3_to_6_mon;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_6_to_12_mon() {
        return r19_other_invt_specify_mor_then_6_to_12_mon;
    }

    public void setR19_other_invt_specify_mor_then_6_to_12_mon(BigDecimal r19_other_invt_specify_mor_then_6_to_12_mon) {
        this.r19_other_invt_specify_mor_then_6_to_12_mon = r19_other_invt_specify_mor_then_6_to_12_mon;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_12_mon_to_3_year() {
        return r19_other_invt_specify_mor_then_12_mon_to_3_year;
    }

    public void setR19_other_invt_specify_mor_then_12_mon_to_3_year(BigDecimal r19_other_invt_specify_mor_then_12_mon_to_3_year) {
        this.r19_other_invt_specify_mor_then_12_mon_to_3_year = r19_other_invt_specify_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_3_to_5_year() {
        return r19_other_invt_specify_mor_then_3_to_5_year;
    }

    public void setR19_other_invt_specify_mor_then_3_to_5_year(BigDecimal r19_other_invt_specify_mor_then_3_to_5_year) {
        this.r19_other_invt_specify_mor_then_3_to_5_year = r19_other_invt_specify_mor_then_3_to_5_year;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_5_to_10_year() {
        return r19_other_invt_specify_mor_then_5_to_10_year;
    }

    public void setR19_other_invt_specify_mor_then_5_to_10_year(BigDecimal r19_other_invt_specify_mor_then_5_to_10_year) {
        this.r19_other_invt_specify_mor_then_5_to_10_year = r19_other_invt_specify_mor_then_5_to_10_year;
    }

    public BigDecimal getR19_other_invt_specify_mor_then_10_year() {
        return r19_other_invt_specify_mor_then_10_year;
    }

    public void setR19_other_invt_specify_mor_then_10_year(BigDecimal r19_other_invt_specify_mor_then_10_year) {
        this.r19_other_invt_specify_mor_then_10_year = r19_other_invt_specify_mor_then_10_year;
    }

    public BigDecimal getR19_other_invt_specify_non_rat_sens_itm() {
        return r19_other_invt_specify_non_rat_sens_itm;
    }

    public void setR19_other_invt_specify_non_rat_sens_itm(BigDecimal r19_other_invt_specify_non_rat_sens_itm) {
        this.r19_other_invt_specify_non_rat_sens_itm = r19_other_invt_specify_non_rat_sens_itm;
    }

    public BigDecimal getR19_other_invt_specify_total() {
        return r19_other_invt_specify_total;
    }

    public void setR19_other_invt_specify_total(BigDecimal r19_other_invt_specify_total) {
        this.r19_other_invt_specify_total = r19_other_invt_specify_total;
    }

    public String getR20_loans_and_adv_to_cust_txt() {
        return r20_loans_and_adv_to_cust_txt;
    }

    public void setR20_loans_and_adv_to_cust_txt(String r20_loans_and_adv_to_cust_txt) {
        this.r20_loans_and_adv_to_cust_txt = r20_loans_and_adv_to_cust_txt;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_up_to_1_mnt() {
        return r20_loans_and_adv_to_cust_up_to_1_mnt;
    }

    public void setR20_loans_and_adv_to_cust_up_to_1_mnt(BigDecimal r20_loans_and_adv_to_cust_up_to_1_mnt) {
        this.r20_loans_and_adv_to_cust_up_to_1_mnt = r20_loans_and_adv_to_cust_up_to_1_mnt;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_1_to_3_mon() {
        return r20_loans_and_adv_to_cust_mor_then_1_to_3_mon;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_1_to_3_mon(BigDecimal r20_loans_and_adv_to_cust_mor_then_1_to_3_mon) {
        this.r20_loans_and_adv_to_cust_mor_then_1_to_3_mon = r20_loans_and_adv_to_cust_mor_then_1_to_3_mon;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_3_to_6_mon() {
        return r20_loans_and_adv_to_cust_mor_then_3_to_6_mon;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_3_to_6_mon(BigDecimal r20_loans_and_adv_to_cust_mor_then_3_to_6_mon) {
        this.r20_loans_and_adv_to_cust_mor_then_3_to_6_mon = r20_loans_and_adv_to_cust_mor_then_3_to_6_mon;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_6_to_12_mon() {
        return r20_loans_and_adv_to_cust_mor_then_6_to_12_mon;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_6_to_12_mon(BigDecimal r20_loans_and_adv_to_cust_mor_then_6_to_12_mon) {
        this.r20_loans_and_adv_to_cust_mor_then_6_to_12_mon = r20_loans_and_adv_to_cust_mor_then_6_to_12_mon;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year() {
        return r20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year(BigDecimal r20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year) {
        this.r20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year = r20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_3_to_5_year() {
        return r20_loans_and_adv_to_cust_mor_then_3_to_5_year;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_3_to_5_year(BigDecimal r20_loans_and_adv_to_cust_mor_then_3_to_5_year) {
        this.r20_loans_and_adv_to_cust_mor_then_3_to_5_year = r20_loans_and_adv_to_cust_mor_then_3_to_5_year;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_5_to_10_year() {
        return r20_loans_and_adv_to_cust_mor_then_5_to_10_year;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_5_to_10_year(BigDecimal r20_loans_and_adv_to_cust_mor_then_5_to_10_year) {
        this.r20_loans_and_adv_to_cust_mor_then_5_to_10_year = r20_loans_and_adv_to_cust_mor_then_5_to_10_year;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_mor_then_10_year() {
        return r20_loans_and_adv_to_cust_mor_then_10_year;
    }

    public void setR20_loans_and_adv_to_cust_mor_then_10_year(BigDecimal r20_loans_and_adv_to_cust_mor_then_10_year) {
        this.r20_loans_and_adv_to_cust_mor_then_10_year = r20_loans_and_adv_to_cust_mor_then_10_year;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_non_rat_sens_itm() {
        return r20_loans_and_adv_to_cust_non_rat_sens_itm;
    }

    public void setR20_loans_and_adv_to_cust_non_rat_sens_itm(BigDecimal r20_loans_and_adv_to_cust_non_rat_sens_itm) {
        this.r20_loans_and_adv_to_cust_non_rat_sens_itm = r20_loans_and_adv_to_cust_non_rat_sens_itm;
    }

    public BigDecimal getR20_loans_and_adv_to_cust_total() {
        return r20_loans_and_adv_to_cust_total;
    }

    public void setR20_loans_and_adv_to_cust_total(BigDecimal r20_loans_and_adv_to_cust_total) {
        this.r20_loans_and_adv_to_cust_total = r20_loans_and_adv_to_cust_total;
    }

    public String getR21_prop_and_eqp_txt() {
        return r21_prop_and_eqp_txt;
    }

    public void setR21_prop_and_eqp_txt(String r21_prop_and_eqp_txt) {
        this.r21_prop_and_eqp_txt = r21_prop_and_eqp_txt;
    }

    public BigDecimal getR21_prop_and_eqp_up_to_1_mnt() {
        return r21_prop_and_eqp_up_to_1_mnt;
    }

    public void setR21_prop_and_eqp_up_to_1_mnt(BigDecimal r21_prop_and_eqp_up_to_1_mnt) {
        this.r21_prop_and_eqp_up_to_1_mnt = r21_prop_and_eqp_up_to_1_mnt;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_1_to_3_mon() {
        return r21_prop_and_eqp_mor_then_1_to_3_mon;
    }

    public void setR21_prop_and_eqp_mor_then_1_to_3_mon(BigDecimal r21_prop_and_eqp_mor_then_1_to_3_mon) {
        this.r21_prop_and_eqp_mor_then_1_to_3_mon = r21_prop_and_eqp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_3_to_6_mon() {
        return r21_prop_and_eqp_mor_then_3_to_6_mon;
    }

    public void setR21_prop_and_eqp_mor_then_3_to_6_mon(BigDecimal r21_prop_and_eqp_mor_then_3_to_6_mon) {
        this.r21_prop_and_eqp_mor_then_3_to_6_mon = r21_prop_and_eqp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_6_to_12_mon() {
        return r21_prop_and_eqp_mor_then_6_to_12_mon;
    }

    public void setR21_prop_and_eqp_mor_then_6_to_12_mon(BigDecimal r21_prop_and_eqp_mor_then_6_to_12_mon) {
        this.r21_prop_and_eqp_mor_then_6_to_12_mon = r21_prop_and_eqp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_12_mon_to_3_year() {
        return r21_prop_and_eqp_mor_then_12_mon_to_3_year;
    }

    public void setR21_prop_and_eqp_mor_then_12_mon_to_3_year(BigDecimal r21_prop_and_eqp_mor_then_12_mon_to_3_year) {
        this.r21_prop_and_eqp_mor_then_12_mon_to_3_year = r21_prop_and_eqp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_3_to_5_year() {
        return r21_prop_and_eqp_mor_then_3_to_5_year;
    }

    public void setR21_prop_and_eqp_mor_then_3_to_5_year(BigDecimal r21_prop_and_eqp_mor_then_3_to_5_year) {
        this.r21_prop_and_eqp_mor_then_3_to_5_year = r21_prop_and_eqp_mor_then_3_to_5_year;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_5_to_10_year() {
        return r21_prop_and_eqp_mor_then_5_to_10_year;
    }

    public void setR21_prop_and_eqp_mor_then_5_to_10_year(BigDecimal r21_prop_and_eqp_mor_then_5_to_10_year) {
        this.r21_prop_and_eqp_mor_then_5_to_10_year = r21_prop_and_eqp_mor_then_5_to_10_year;
    }

    public BigDecimal getR21_prop_and_eqp_mor_then_10_year() {
        return r21_prop_and_eqp_mor_then_10_year;
    }

    public void setR21_prop_and_eqp_mor_then_10_year(BigDecimal r21_prop_and_eqp_mor_then_10_year) {
        this.r21_prop_and_eqp_mor_then_10_year = r21_prop_and_eqp_mor_then_10_year;
    }

    public BigDecimal getR21_prop_and_eqp_non_rat_sens_itm() {
        return r21_prop_and_eqp_non_rat_sens_itm;
    }

    public void setR21_prop_and_eqp_non_rat_sens_itm(BigDecimal r21_prop_and_eqp_non_rat_sens_itm) {
        this.r21_prop_and_eqp_non_rat_sens_itm = r21_prop_and_eqp_non_rat_sens_itm;
    }

    public BigDecimal getR21_prop_and_eqp_total() {
        return r21_prop_and_eqp_total;
    }

    public void setR21_prop_and_eqp_total(BigDecimal r21_prop_and_eqp_total) {
        this.r21_prop_and_eqp_total = r21_prop_and_eqp_total;
    }

    public String getR22_other_assets_specify_txt() {
        return r22_other_assets_specify_txt;
    }

    public void setR22_other_assets_specify_txt(String r22_other_assets_specify_txt) {
        this.r22_other_assets_specify_txt = r22_other_assets_specify_txt;
    }

    public BigDecimal getR22_other_assets_specify_up_to_1_mnt() {
        return r22_other_assets_specify_up_to_1_mnt;
    }

    public void setR22_other_assets_specify_up_to_1_mnt(BigDecimal r22_other_assets_specify_up_to_1_mnt) {
        this.r22_other_assets_specify_up_to_1_mnt = r22_other_assets_specify_up_to_1_mnt;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_1_to_3_mon() {
        return r22_other_assets_specify_mor_then_1_to_3_mon;
    }

    public void setR22_other_assets_specify_mor_then_1_to_3_mon(BigDecimal r22_other_assets_specify_mor_then_1_to_3_mon) {
        this.r22_other_assets_specify_mor_then_1_to_3_mon = r22_other_assets_specify_mor_then_1_to_3_mon;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_3_to_6_mon() {
        return r22_other_assets_specify_mor_then_3_to_6_mon;
    }

    public void setR22_other_assets_specify_mor_then_3_to_6_mon(BigDecimal r22_other_assets_specify_mor_then_3_to_6_mon) {
        this.r22_other_assets_specify_mor_then_3_to_6_mon = r22_other_assets_specify_mor_then_3_to_6_mon;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_6_to_12_mon() {
        return r22_other_assets_specify_mor_then_6_to_12_mon;
    }

    public void setR22_other_assets_specify_mor_then_6_to_12_mon(BigDecimal r22_other_assets_specify_mor_then_6_to_12_mon) {
        this.r22_other_assets_specify_mor_then_6_to_12_mon = r22_other_assets_specify_mor_then_6_to_12_mon;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_12_mon_to_3_year() {
        return r22_other_assets_specify_mor_then_12_mon_to_3_year;
    }

    public void setR22_other_assets_specify_mor_then_12_mon_to_3_year(BigDecimal r22_other_assets_specify_mor_then_12_mon_to_3_year) {
        this.r22_other_assets_specify_mor_then_12_mon_to_3_year = r22_other_assets_specify_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_3_to_5_year() {
        return r22_other_assets_specify_mor_then_3_to_5_year;
    }

    public void setR22_other_assets_specify_mor_then_3_to_5_year(BigDecimal r22_other_assets_specify_mor_then_3_to_5_year) {
        this.r22_other_assets_specify_mor_then_3_to_5_year = r22_other_assets_specify_mor_then_3_to_5_year;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_5_to_10_year() {
        return r22_other_assets_specify_mor_then_5_to_10_year;
    }

    public void setR22_other_assets_specify_mor_then_5_to_10_year(BigDecimal r22_other_assets_specify_mor_then_5_to_10_year) {
        this.r22_other_assets_specify_mor_then_5_to_10_year = r22_other_assets_specify_mor_then_5_to_10_year;
    }

    public BigDecimal getR22_other_assets_specify_mor_then_10_year() {
        return r22_other_assets_specify_mor_then_10_year;
    }

    public void setR22_other_assets_specify_mor_then_10_year(BigDecimal r22_other_assets_specify_mor_then_10_year) {
        this.r22_other_assets_specify_mor_then_10_year = r22_other_assets_specify_mor_then_10_year;
    }

    public BigDecimal getR22_other_assets_specify_non_rat_sens_itm() {
        return r22_other_assets_specify_non_rat_sens_itm;
    }

    public void setR22_other_assets_specify_non_rat_sens_itm(BigDecimal r22_other_assets_specify_non_rat_sens_itm) {
        this.r22_other_assets_specify_non_rat_sens_itm = r22_other_assets_specify_non_rat_sens_itm;
    }

    public BigDecimal getR22_other_assets_specify_total() {
        return r22_other_assets_specify_total;
    }

    public void setR22_other_assets_specify_total(BigDecimal r22_other_assets_specify_total) {
        this.r22_other_assets_specify_total = r22_other_assets_specify_total;
    }

    public String getR23_dis_admt_discrt_admt_rate_items_txt() {
        return r23_dis_admt_discrt_admt_rate_items_txt;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_txt(String r23_dis_admt_discrt_admt_rate_items_txt) {
        this.r23_dis_admt_discrt_admt_rate_items_txt = r23_dis_admt_discrt_admt_rate_items_txt;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_up_to_1_mnt() {
        return r23_dis_admt_discrt_admt_rate_items_up_to_1_mnt;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_up_to_1_mnt(BigDecimal r23_dis_admt_discrt_admt_rate_items_up_to_1_mnt) {
        this.r23_dis_admt_discrt_admt_rate_items_up_to_1_mnt = r23_dis_admt_discrt_admt_rate_items_up_to_1_mnt;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon = r23_dis_admt_discrt_admt_rate_items_mor_then_1_to_3_mon;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon = r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_6_mon;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon = r23_dis_admt_discrt_admt_rate_items_mor_then_6_to_12_mon;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year = r23_dis_admt_discrt_admt_rate_items_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year = r23_dis_admt_discrt_admt_rate_items_mor_then_3_to_5_year;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year = r23_dis_admt_discrt_admt_rate_items_mor_then_5_to_10_year;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_mor_then_10_year() {
        return r23_dis_admt_discrt_admt_rate_items_mor_then_10_year;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_mor_then_10_year(BigDecimal r23_dis_admt_discrt_admt_rate_items_mor_then_10_year) {
        this.r23_dis_admt_discrt_admt_rate_items_mor_then_10_year = r23_dis_admt_discrt_admt_rate_items_mor_then_10_year;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm() {
        return r23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm(BigDecimal r23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm) {
        this.r23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm = r23_dis_admt_discrt_admt_rate_items_non_rat_sens_itm;
    }

    public BigDecimal getR23_dis_admt_discrt_admt_rate_items_total() {
        return r23_dis_admt_discrt_admt_rate_items_total;
    }

    public void setR23_dis_admt_discrt_admt_rate_items_total(BigDecimal r23_dis_admt_discrt_admt_rate_items_total) {
        this.r23_dis_admt_discrt_admt_rate_items_total = r23_dis_admt_discrt_admt_rate_items_total;
    }

    public String getR24_dis_admt_cash_txt() {
        return r24_dis_admt_cash_txt;
    }

    public void setR24_dis_admt_cash_txt(String r24_dis_admt_cash_txt) {
        this.r24_dis_admt_cash_txt = r24_dis_admt_cash_txt;
    }

    public BigDecimal getR24_dis_admt_cash_up_to_1_mnt() {
        return r24_dis_admt_cash_up_to_1_mnt;
    }

    public void setR24_dis_admt_cash_up_to_1_mnt(BigDecimal r24_dis_admt_cash_up_to_1_mnt) {
        this.r24_dis_admt_cash_up_to_1_mnt = r24_dis_admt_cash_up_to_1_mnt;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_1_to_3_mon() {
        return r24_dis_admt_cash_mor_then_1_to_3_mon;
    }

    public void setR24_dis_admt_cash_mor_then_1_to_3_mon(BigDecimal r24_dis_admt_cash_mor_then_1_to_3_mon) {
        this.r24_dis_admt_cash_mor_then_1_to_3_mon = r24_dis_admt_cash_mor_then_1_to_3_mon;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_3_to_6_mon() {
        return r24_dis_admt_cash_mor_then_3_to_6_mon;
    }

    public void setR24_dis_admt_cash_mor_then_3_to_6_mon(BigDecimal r24_dis_admt_cash_mor_then_3_to_6_mon) {
        this.r24_dis_admt_cash_mor_then_3_to_6_mon = r24_dis_admt_cash_mor_then_3_to_6_mon;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_6_to_12_mon() {
        return r24_dis_admt_cash_mor_then_6_to_12_mon;
    }

    public void setR24_dis_admt_cash_mor_then_6_to_12_mon(BigDecimal r24_dis_admt_cash_mor_then_6_to_12_mon) {
        this.r24_dis_admt_cash_mor_then_6_to_12_mon = r24_dis_admt_cash_mor_then_6_to_12_mon;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_12_mon_to_3_year() {
        return r24_dis_admt_cash_mor_then_12_mon_to_3_year;
    }

    public void setR24_dis_admt_cash_mor_then_12_mon_to_3_year(BigDecimal r24_dis_admt_cash_mor_then_12_mon_to_3_year) {
        this.r24_dis_admt_cash_mor_then_12_mon_to_3_year = r24_dis_admt_cash_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_3_to_5_year() {
        return r24_dis_admt_cash_mor_then_3_to_5_year;
    }

    public void setR24_dis_admt_cash_mor_then_3_to_5_year(BigDecimal r24_dis_admt_cash_mor_then_3_to_5_year) {
        this.r24_dis_admt_cash_mor_then_3_to_5_year = r24_dis_admt_cash_mor_then_3_to_5_year;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_5_to_10_year() {
        return r24_dis_admt_cash_mor_then_5_to_10_year;
    }

    public void setR24_dis_admt_cash_mor_then_5_to_10_year(BigDecimal r24_dis_admt_cash_mor_then_5_to_10_year) {
        this.r24_dis_admt_cash_mor_then_5_to_10_year = r24_dis_admt_cash_mor_then_5_to_10_year;
    }

    public BigDecimal getR24_dis_admt_cash_mor_then_10_year() {
        return r24_dis_admt_cash_mor_then_10_year;
    }

    public void setR24_dis_admt_cash_mor_then_10_year(BigDecimal r24_dis_admt_cash_mor_then_10_year) {
        this.r24_dis_admt_cash_mor_then_10_year = r24_dis_admt_cash_mor_then_10_year;
    }

    public BigDecimal getR24_dis_admt_cash_non_rat_sens_itm() {
        return r24_dis_admt_cash_non_rat_sens_itm;
    }

    public void setR24_dis_admt_cash_non_rat_sens_itm(BigDecimal r24_dis_admt_cash_non_rat_sens_itm) {
        this.r24_dis_admt_cash_non_rat_sens_itm = r24_dis_admt_cash_non_rat_sens_itm;
    }

    public BigDecimal getR24_dis_admt_cash_total() {
        return r24_dis_admt_cash_total;
    }

    public void setR24_dis_admt_cash_total(BigDecimal r24_dis_admt_cash_total) {
        this.r24_dis_admt_cash_total = r24_dis_admt_cash_total;
    }

    public String getR25_dis_admt_bal_bnk_of_botswana_txt() {
        return r25_dis_admt_bal_bnk_of_botswana_txt;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_txt(String r25_dis_admt_bal_bnk_of_botswana_txt) {
        this.r25_dis_admt_bal_bnk_of_botswana_txt = r25_dis_admt_bal_bnk_of_botswana_txt;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt() {
        return r25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt(BigDecimal r25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt) {
        this.r25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt = r25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon = r25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon = r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon = r25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year = r25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year = r25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year = r25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_mor_then_10_year() {
        return r25_dis_admt_bal_bnk_of_botswana_mor_then_10_year;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_mor_then_10_year(BigDecimal r25_dis_admt_bal_bnk_of_botswana_mor_then_10_year) {
        this.r25_dis_admt_bal_bnk_of_botswana_mor_then_10_year = r25_dis_admt_bal_bnk_of_botswana_mor_then_10_year;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm() {
        return r25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm(BigDecimal r25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm) {
        this.r25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm = r25_dis_admt_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public BigDecimal getR25_dis_admt_bal_bnk_of_botswana_total() {
        return r25_dis_admt_bal_bnk_of_botswana_total;
    }

    public void setR25_dis_admt_bal_bnk_of_botswana_total(BigDecimal r25_dis_admt_bal_bnk_of_botswana_total) {
        this.r25_dis_admt_bal_bnk_of_botswana_total = r25_dis_admt_bal_bnk_of_botswana_total;
    }

    public String getR26_dis_admt_bal_doms_bnks_txt() {
        return r26_dis_admt_bal_doms_bnks_txt;
    }

    public void setR26_dis_admt_bal_doms_bnks_txt(String r26_dis_admt_bal_doms_bnks_txt) {
        this.r26_dis_admt_bal_doms_bnks_txt = r26_dis_admt_bal_doms_bnks_txt;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_up_to_1_mnt() {
        return r26_dis_admt_bal_doms_bnks_up_to_1_mnt;
    }

    public void setR26_dis_admt_bal_doms_bnks_up_to_1_mnt(BigDecimal r26_dis_admt_bal_doms_bnks_up_to_1_mnt) {
        this.r26_dis_admt_bal_doms_bnks_up_to_1_mnt = r26_dis_admt_bal_doms_bnks_up_to_1_mnt;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon() {
        return r26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon = r26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon() {
        return r26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon = r26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon() {
        return r26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon = r26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year() {
        return r26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year = r26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year() {
        return r26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year = r26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year() {
        return r26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year = r26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_mor_then_10_year() {
        return r26_dis_admt_bal_doms_bnks_mor_then_10_year;
    }

    public void setR26_dis_admt_bal_doms_bnks_mor_then_10_year(BigDecimal r26_dis_admt_bal_doms_bnks_mor_then_10_year) {
        this.r26_dis_admt_bal_doms_bnks_mor_then_10_year = r26_dis_admt_bal_doms_bnks_mor_then_10_year;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_non_rat_sens_itm() {
        return r26_dis_admt_bal_doms_bnks_non_rat_sens_itm;
    }

    public void setR26_dis_admt_bal_doms_bnks_non_rat_sens_itm(BigDecimal r26_dis_admt_bal_doms_bnks_non_rat_sens_itm) {
        this.r26_dis_admt_bal_doms_bnks_non_rat_sens_itm = r26_dis_admt_bal_doms_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR26_dis_admt_bal_doms_bnks_total() {
        return r26_dis_admt_bal_doms_bnks_total;
    }

    public void setR26_dis_admt_bal_doms_bnks_total(BigDecimal r26_dis_admt_bal_doms_bnks_total) {
        this.r26_dis_admt_bal_doms_bnks_total = r26_dis_admt_bal_doms_bnks_total;
    }

    public String getR27_dis_admt_bal_foreign_bnks_txt() {
        return r27_dis_admt_bal_foreign_bnks_txt;
    }

    public void setR27_dis_admt_bal_foreign_bnks_txt(String r27_dis_admt_bal_foreign_bnks_txt) {
        this.r27_dis_admt_bal_foreign_bnks_txt = r27_dis_admt_bal_foreign_bnks_txt;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_up_to_1_mnt() {
        return r27_dis_admt_bal_foreign_bnks_up_to_1_mnt;
    }

    public void setR27_dis_admt_bal_foreign_bnks_up_to_1_mnt(BigDecimal r27_dis_admt_bal_foreign_bnks_up_to_1_mnt) {
        this.r27_dis_admt_bal_foreign_bnks_up_to_1_mnt = r27_dis_admt_bal_foreign_bnks_up_to_1_mnt;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon = r27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon = r27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon = r27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year = r27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year = r27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year = r27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_mor_then_10_year() {
        return r27_dis_admt_bal_foreign_bnks_mor_then_10_year;
    }

    public void setR27_dis_admt_bal_foreign_bnks_mor_then_10_year(BigDecimal r27_dis_admt_bal_foreign_bnks_mor_then_10_year) {
        this.r27_dis_admt_bal_foreign_bnks_mor_then_10_year = r27_dis_admt_bal_foreign_bnks_mor_then_10_year;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_non_rat_sens_itm() {
        return r27_dis_admt_bal_foreign_bnks_non_rat_sens_itm;
    }

    public void setR27_dis_admt_bal_foreign_bnks_non_rat_sens_itm(BigDecimal r27_dis_admt_bal_foreign_bnks_non_rat_sens_itm) {
        this.r27_dis_admt_bal_foreign_bnks_non_rat_sens_itm = r27_dis_admt_bal_foreign_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR27_dis_admt_bal_foreign_bnks_total() {
        return r27_dis_admt_bal_foreign_bnks_total;
    }

    public void setR27_dis_admt_bal_foreign_bnks_total(BigDecimal r27_dis_admt_bal_foreign_bnks_total) {
        this.r27_dis_admt_bal_foreign_bnks_total = r27_dis_admt_bal_foreign_bnks_total;
    }

    public String getR28_dis_admt_bal_related_comp_txt() {
        return r28_dis_admt_bal_related_comp_txt;
    }

    public void setR28_dis_admt_bal_related_comp_txt(String r28_dis_admt_bal_related_comp_txt) {
        this.r28_dis_admt_bal_related_comp_txt = r28_dis_admt_bal_related_comp_txt;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_up_to_1_mnt() {
        return r28_dis_admt_bal_related_comp_up_to_1_mnt;
    }

    public void setR28_dis_admt_bal_related_comp_up_to_1_mnt(BigDecimal r28_dis_admt_bal_related_comp_up_to_1_mnt) {
        this.r28_dis_admt_bal_related_comp_up_to_1_mnt = r28_dis_admt_bal_related_comp_up_to_1_mnt;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_1_to_3_mon() {
        return r28_dis_admt_bal_related_comp_mor_then_1_to_3_mon;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_1_to_3_mon(BigDecimal r28_dis_admt_bal_related_comp_mor_then_1_to_3_mon) {
        this.r28_dis_admt_bal_related_comp_mor_then_1_to_3_mon = r28_dis_admt_bal_related_comp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_3_to_6_mon() {
        return r28_dis_admt_bal_related_comp_mor_then_3_to_6_mon;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_3_to_6_mon(BigDecimal r28_dis_admt_bal_related_comp_mor_then_3_to_6_mon) {
        this.r28_dis_admt_bal_related_comp_mor_then_3_to_6_mon = r28_dis_admt_bal_related_comp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_6_to_12_mon() {
        return r28_dis_admt_bal_related_comp_mor_then_6_to_12_mon;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_6_to_12_mon(BigDecimal r28_dis_admt_bal_related_comp_mor_then_6_to_12_mon) {
        this.r28_dis_admt_bal_related_comp_mor_then_6_to_12_mon = r28_dis_admt_bal_related_comp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year() {
        return r28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year(BigDecimal r28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year) {
        this.r28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year = r28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_3_to_5_year() {
        return r28_dis_admt_bal_related_comp_mor_then_3_to_5_year;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_3_to_5_year(BigDecimal r28_dis_admt_bal_related_comp_mor_then_3_to_5_year) {
        this.r28_dis_admt_bal_related_comp_mor_then_3_to_5_year = r28_dis_admt_bal_related_comp_mor_then_3_to_5_year;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_5_to_10_year() {
        return r28_dis_admt_bal_related_comp_mor_then_5_to_10_year;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_5_to_10_year(BigDecimal r28_dis_admt_bal_related_comp_mor_then_5_to_10_year) {
        this.r28_dis_admt_bal_related_comp_mor_then_5_to_10_year = r28_dis_admt_bal_related_comp_mor_then_5_to_10_year;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_mor_then_10_year() {
        return r28_dis_admt_bal_related_comp_mor_then_10_year;
    }

    public void setR28_dis_admt_bal_related_comp_mor_then_10_year(BigDecimal r28_dis_admt_bal_related_comp_mor_then_10_year) {
        this.r28_dis_admt_bal_related_comp_mor_then_10_year = r28_dis_admt_bal_related_comp_mor_then_10_year;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_non_rat_sens_itm() {
        return r28_dis_admt_bal_related_comp_non_rat_sens_itm;
    }

    public void setR28_dis_admt_bal_related_comp_non_rat_sens_itm(BigDecimal r28_dis_admt_bal_related_comp_non_rat_sens_itm) {
        this.r28_dis_admt_bal_related_comp_non_rat_sens_itm = r28_dis_admt_bal_related_comp_non_rat_sens_itm;
    }

    public BigDecimal getR28_dis_admt_bal_related_comp_total() {
        return r28_dis_admt_bal_related_comp_total;
    }

    public void setR28_dis_admt_bal_related_comp_total(BigDecimal r28_dis_admt_bal_related_comp_total) {
        this.r28_dis_admt_bal_related_comp_total = r28_dis_admt_bal_related_comp_total;
    }

    public String getR29_dis_admt_bnk_of_botswana_cert_txt() {
        return r29_dis_admt_bnk_of_botswana_cert_txt;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_txt(String r29_dis_admt_bnk_of_botswana_cert_txt) {
        this.r29_dis_admt_bnk_of_botswana_cert_txt = r29_dis_admt_bnk_of_botswana_cert_txt;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt() {
        return r29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt(BigDecimal r29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt) {
        this.r29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt = r29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon = r29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon = r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon = r29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year = r29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year = r29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year = r29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_mor_then_10_year() {
        return r29_dis_admt_bnk_of_botswana_cert_mor_then_10_year;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_mor_then_10_year(BigDecimal r29_dis_admt_bnk_of_botswana_cert_mor_then_10_year) {
        this.r29_dis_admt_bnk_of_botswana_cert_mor_then_10_year = r29_dis_admt_bnk_of_botswana_cert_mor_then_10_year;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm() {
        return r29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm(BigDecimal r29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm) {
        this.r29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm = r29_dis_admt_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public BigDecimal getR29_dis_admt_bnk_of_botswana_cert_total() {
        return r29_dis_admt_bnk_of_botswana_cert_total;
    }

    public void setR29_dis_admt_bnk_of_botswana_cert_total(BigDecimal r29_dis_admt_bnk_of_botswana_cert_total) {
        this.r29_dis_admt_bnk_of_botswana_cert_total = r29_dis_admt_bnk_of_botswana_cert_total;
    }

    public String getR30_dis_admt_gov_bonds_txt() {
        return r30_dis_admt_gov_bonds_txt;
    }

    public void setR30_dis_admt_gov_bonds_txt(String r30_dis_admt_gov_bonds_txt) {
        this.r30_dis_admt_gov_bonds_txt = r30_dis_admt_gov_bonds_txt;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_up_to_1_mnt() {
        return r30_dis_admt_gov_bonds_up_to_1_mnt;
    }

    public void setR30_dis_admt_gov_bonds_up_to_1_mnt(BigDecimal r30_dis_admt_gov_bonds_up_to_1_mnt) {
        this.r30_dis_admt_gov_bonds_up_to_1_mnt = r30_dis_admt_gov_bonds_up_to_1_mnt;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_1_to_3_mon() {
        return r30_dis_admt_gov_bonds_mor_then_1_to_3_mon;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_1_to_3_mon(BigDecimal r30_dis_admt_gov_bonds_mor_then_1_to_3_mon) {
        this.r30_dis_admt_gov_bonds_mor_then_1_to_3_mon = r30_dis_admt_gov_bonds_mor_then_1_to_3_mon;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_3_to_6_mon() {
        return r30_dis_admt_gov_bonds_mor_then_3_to_6_mon;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_3_to_6_mon(BigDecimal r30_dis_admt_gov_bonds_mor_then_3_to_6_mon) {
        this.r30_dis_admt_gov_bonds_mor_then_3_to_6_mon = r30_dis_admt_gov_bonds_mor_then_3_to_6_mon;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_6_to_12_mon() {
        return r30_dis_admt_gov_bonds_mor_then_6_to_12_mon;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_6_to_12_mon(BigDecimal r30_dis_admt_gov_bonds_mor_then_6_to_12_mon) {
        this.r30_dis_admt_gov_bonds_mor_then_6_to_12_mon = r30_dis_admt_gov_bonds_mor_then_6_to_12_mon;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year() {
        return r30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year(BigDecimal r30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year) {
        this.r30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year = r30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_3_to_5_year() {
        return r30_dis_admt_gov_bonds_mor_then_3_to_5_year;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_3_to_5_year(BigDecimal r30_dis_admt_gov_bonds_mor_then_3_to_5_year) {
        this.r30_dis_admt_gov_bonds_mor_then_3_to_5_year = r30_dis_admt_gov_bonds_mor_then_3_to_5_year;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_5_to_10_year() {
        return r30_dis_admt_gov_bonds_mor_then_5_to_10_year;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_5_to_10_year(BigDecimal r30_dis_admt_gov_bonds_mor_then_5_to_10_year) {
        this.r30_dis_admt_gov_bonds_mor_then_5_to_10_year = r30_dis_admt_gov_bonds_mor_then_5_to_10_year;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_mor_then_10_year() {
        return r30_dis_admt_gov_bonds_mor_then_10_year;
    }

    public void setR30_dis_admt_gov_bonds_mor_then_10_year(BigDecimal r30_dis_admt_gov_bonds_mor_then_10_year) {
        this.r30_dis_admt_gov_bonds_mor_then_10_year = r30_dis_admt_gov_bonds_mor_then_10_year;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_non_rat_sens_itm() {
        return r30_dis_admt_gov_bonds_non_rat_sens_itm;
    }

    public void setR30_dis_admt_gov_bonds_non_rat_sens_itm(BigDecimal r30_dis_admt_gov_bonds_non_rat_sens_itm) {
        this.r30_dis_admt_gov_bonds_non_rat_sens_itm = r30_dis_admt_gov_bonds_non_rat_sens_itm;
    }

    public BigDecimal getR30_dis_admt_gov_bonds_total() {
        return r30_dis_admt_gov_bonds_total;
    }

    public void setR30_dis_admt_gov_bonds_total(BigDecimal r30_dis_admt_gov_bonds_total) {
        this.r30_dis_admt_gov_bonds_total = r30_dis_admt_gov_bonds_total;
    }

    public String getR31_dis_admt_other_invt_specify_txt() {
        return r31_dis_admt_other_invt_specify_txt;
    }

    public void setR31_dis_admt_other_invt_specify_txt(String r31_dis_admt_other_invt_specify_txt) {
        this.r31_dis_admt_other_invt_specify_txt = r31_dis_admt_other_invt_specify_txt;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_up_to_1_mnt() {
        return r31_dis_admt_other_invt_specify_up_to_1_mnt;
    }

    public void setR31_dis_admt_other_invt_specify_up_to_1_mnt(BigDecimal r31_dis_admt_other_invt_specify_up_to_1_mnt) {
        this.r31_dis_admt_other_invt_specify_up_to_1_mnt = r31_dis_admt_other_invt_specify_up_to_1_mnt;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_1_to_3_mon() {
        return r31_dis_admt_other_invt_specify_mor_then_1_to_3_mon;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_1_to_3_mon(BigDecimal r31_dis_admt_other_invt_specify_mor_then_1_to_3_mon) {
        this.r31_dis_admt_other_invt_specify_mor_then_1_to_3_mon = r31_dis_admt_other_invt_specify_mor_then_1_to_3_mon;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_3_to_6_mon() {
        return r31_dis_admt_other_invt_specify_mor_then_3_to_6_mon;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_3_to_6_mon(BigDecimal r31_dis_admt_other_invt_specify_mor_then_3_to_6_mon) {
        this.r31_dis_admt_other_invt_specify_mor_then_3_to_6_mon = r31_dis_admt_other_invt_specify_mor_then_3_to_6_mon;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_6_to_12_mon() {
        return r31_dis_admt_other_invt_specify_mor_then_6_to_12_mon;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_6_to_12_mon(BigDecimal r31_dis_admt_other_invt_specify_mor_then_6_to_12_mon) {
        this.r31_dis_admt_other_invt_specify_mor_then_6_to_12_mon = r31_dis_admt_other_invt_specify_mor_then_6_to_12_mon;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year() {
        return r31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year(BigDecimal r31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year) {
        this.r31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year = r31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_3_to_5_year() {
        return r31_dis_admt_other_invt_specify_mor_then_3_to_5_year;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_3_to_5_year(BigDecimal r31_dis_admt_other_invt_specify_mor_then_3_to_5_year) {
        this.r31_dis_admt_other_invt_specify_mor_then_3_to_5_year = r31_dis_admt_other_invt_specify_mor_then_3_to_5_year;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_5_to_10_year() {
        return r31_dis_admt_other_invt_specify_mor_then_5_to_10_year;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_5_to_10_year(BigDecimal r31_dis_admt_other_invt_specify_mor_then_5_to_10_year) {
        this.r31_dis_admt_other_invt_specify_mor_then_5_to_10_year = r31_dis_admt_other_invt_specify_mor_then_5_to_10_year;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_mor_then_10_year() {
        return r31_dis_admt_other_invt_specify_mor_then_10_year;
    }

    public void setR31_dis_admt_other_invt_specify_mor_then_10_year(BigDecimal r31_dis_admt_other_invt_specify_mor_then_10_year) {
        this.r31_dis_admt_other_invt_specify_mor_then_10_year = r31_dis_admt_other_invt_specify_mor_then_10_year;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_non_rat_sens_itm() {
        return r31_dis_admt_other_invt_specify_non_rat_sens_itm;
    }

    public void setR31_dis_admt_other_invt_specify_non_rat_sens_itm(BigDecimal r31_dis_admt_other_invt_specify_non_rat_sens_itm) {
        this.r31_dis_admt_other_invt_specify_non_rat_sens_itm = r31_dis_admt_other_invt_specify_non_rat_sens_itm;
    }

    public BigDecimal getR31_dis_admt_other_invt_specify_total() {
        return r31_dis_admt_other_invt_specify_total;
    }

    public void setR31_dis_admt_other_invt_specify_total(BigDecimal r31_dis_admt_other_invt_specify_total) {
        this.r31_dis_admt_other_invt_specify_total = r31_dis_admt_other_invt_specify_total;
    }

    public String getR32_dis_admt_loans_and_adv_to_cust_txt() {
        return r32_dis_admt_loans_and_adv_to_cust_txt;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_txt(String r32_dis_admt_loans_and_adv_to_cust_txt) {
        this.r32_dis_admt_loans_and_adv_to_cust_txt = r32_dis_admt_loans_and_adv_to_cust_txt;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt() {
        return r32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt(BigDecimal r32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt) {
        this.r32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt = r32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon = r32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon = r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon = r32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year = r32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year = r32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year = r32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_mor_then_10_year() {
        return r32_dis_admt_loans_and_adv_to_cust_mor_then_10_year;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_mor_then_10_year(BigDecimal r32_dis_admt_loans_and_adv_to_cust_mor_then_10_year) {
        this.r32_dis_admt_loans_and_adv_to_cust_mor_then_10_year = r32_dis_admt_loans_and_adv_to_cust_mor_then_10_year;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm() {
        return r32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm(BigDecimal r32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm) {
        this.r32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm = r32_dis_admt_loans_and_adv_to_cust_non_rat_sens_itm;
    }

    public BigDecimal getR32_dis_admt_loans_and_adv_to_cust_total() {
        return r32_dis_admt_loans_and_adv_to_cust_total;
    }

    public void setR32_dis_admt_loans_and_adv_to_cust_total(BigDecimal r32_dis_admt_loans_and_adv_to_cust_total) {
        this.r32_dis_admt_loans_and_adv_to_cust_total = r32_dis_admt_loans_and_adv_to_cust_total;
    }

    public String getR33_dis_admt_prop_and_eqp_txt() {
        return r33_dis_admt_prop_and_eqp_txt;
    }

    public void setR33_dis_admt_prop_and_eqp_txt(String r33_dis_admt_prop_and_eqp_txt) {
        this.r33_dis_admt_prop_and_eqp_txt = r33_dis_admt_prop_and_eqp_txt;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_up_to_1_mnt() {
        return r33_dis_admt_prop_and_eqp_up_to_1_mnt;
    }

    public void setR33_dis_admt_prop_and_eqp_up_to_1_mnt(BigDecimal r33_dis_admt_prop_and_eqp_up_to_1_mnt) {
        this.r33_dis_admt_prop_and_eqp_up_to_1_mnt = r33_dis_admt_prop_and_eqp_up_to_1_mnt;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon() {
        return r33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon) {
        this.r33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon = r33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon() {
        return r33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon) {
        this.r33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon = r33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon() {
        return r33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon) {
        this.r33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon = r33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year() {
        return r33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year) {
        this.r33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year = r33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_3_to_5_year() {
        return r33_dis_admt_prop_and_eqp_mor_then_3_to_5_year;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_3_to_5_year(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_3_to_5_year) {
        this.r33_dis_admt_prop_and_eqp_mor_then_3_to_5_year = r33_dis_admt_prop_and_eqp_mor_then_3_to_5_year;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_5_to_10_year() {
        return r33_dis_admt_prop_and_eqp_mor_then_5_to_10_year;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_5_to_10_year(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_5_to_10_year) {
        this.r33_dis_admt_prop_and_eqp_mor_then_5_to_10_year = r33_dis_admt_prop_and_eqp_mor_then_5_to_10_year;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_mor_then_10_year() {
        return r33_dis_admt_prop_and_eqp_mor_then_10_year;
    }

    public void setR33_dis_admt_prop_and_eqp_mor_then_10_year(BigDecimal r33_dis_admt_prop_and_eqp_mor_then_10_year) {
        this.r33_dis_admt_prop_and_eqp_mor_then_10_year = r33_dis_admt_prop_and_eqp_mor_then_10_year;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_non_rat_sens_itm() {
        return r33_dis_admt_prop_and_eqp_non_rat_sens_itm;
    }

    public void setR33_dis_admt_prop_and_eqp_non_rat_sens_itm(BigDecimal r33_dis_admt_prop_and_eqp_non_rat_sens_itm) {
        this.r33_dis_admt_prop_and_eqp_non_rat_sens_itm = r33_dis_admt_prop_and_eqp_non_rat_sens_itm;
    }

    public BigDecimal getR33_dis_admt_prop_and_eqp_total() {
        return r33_dis_admt_prop_and_eqp_total;
    }

    public void setR33_dis_admt_prop_and_eqp_total(BigDecimal r33_dis_admt_prop_and_eqp_total) {
        this.r33_dis_admt_prop_and_eqp_total = r33_dis_admt_prop_and_eqp_total;
    }

    public String getR34_dis_admt_other_assets_specify_txt() {
        return r34_dis_admt_other_assets_specify_txt;
    }

    public void setR34_dis_admt_other_assets_specify_txt(String r34_dis_admt_other_assets_specify_txt) {
        this.r34_dis_admt_other_assets_specify_txt = r34_dis_admt_other_assets_specify_txt;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_up_to_1_mnt() {
        return r34_dis_admt_other_assets_specify_up_to_1_mnt;
    }

    public void setR34_dis_admt_other_assets_specify_up_to_1_mnt(BigDecimal r34_dis_admt_other_assets_specify_up_to_1_mnt) {
        this.r34_dis_admt_other_assets_specify_up_to_1_mnt = r34_dis_admt_other_assets_specify_up_to_1_mnt;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_1_to_3_mon() {
        return r34_dis_admt_other_assets_specify_mor_then_1_to_3_mon;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_1_to_3_mon(BigDecimal r34_dis_admt_other_assets_specify_mor_then_1_to_3_mon) {
        this.r34_dis_admt_other_assets_specify_mor_then_1_to_3_mon = r34_dis_admt_other_assets_specify_mor_then_1_to_3_mon;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_3_to_6_mon() {
        return r34_dis_admt_other_assets_specify_mor_then_3_to_6_mon;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_3_to_6_mon(BigDecimal r34_dis_admt_other_assets_specify_mor_then_3_to_6_mon) {
        this.r34_dis_admt_other_assets_specify_mor_then_3_to_6_mon = r34_dis_admt_other_assets_specify_mor_then_3_to_6_mon;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_6_to_12_mon() {
        return r34_dis_admt_other_assets_specify_mor_then_6_to_12_mon;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_6_to_12_mon(BigDecimal r34_dis_admt_other_assets_specify_mor_then_6_to_12_mon) {
        this.r34_dis_admt_other_assets_specify_mor_then_6_to_12_mon = r34_dis_admt_other_assets_specify_mor_then_6_to_12_mon;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year() {
        return r34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year(BigDecimal r34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year) {
        this.r34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year = r34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_3_to_5_year() {
        return r34_dis_admt_other_assets_specify_mor_then_3_to_5_year;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_3_to_5_year(BigDecimal r34_dis_admt_other_assets_specify_mor_then_3_to_5_year) {
        this.r34_dis_admt_other_assets_specify_mor_then_3_to_5_year = r34_dis_admt_other_assets_specify_mor_then_3_to_5_year;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_5_to_10_year() {
        return r34_dis_admt_other_assets_specify_mor_then_5_to_10_year;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_5_to_10_year(BigDecimal r34_dis_admt_other_assets_specify_mor_then_5_to_10_year) {
        this.r34_dis_admt_other_assets_specify_mor_then_5_to_10_year = r34_dis_admt_other_assets_specify_mor_then_5_to_10_year;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_mor_then_10_year() {
        return r34_dis_admt_other_assets_specify_mor_then_10_year;
    }

    public void setR34_dis_admt_other_assets_specify_mor_then_10_year(BigDecimal r34_dis_admt_other_assets_specify_mor_then_10_year) {
        this.r34_dis_admt_other_assets_specify_mor_then_10_year = r34_dis_admt_other_assets_specify_mor_then_10_year;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_non_rat_sens_itm() {
        return r34_dis_admt_other_assets_specify_non_rat_sens_itm;
    }

    public void setR34_dis_admt_other_assets_specify_non_rat_sens_itm(BigDecimal r34_dis_admt_other_assets_specify_non_rat_sens_itm) {
        this.r34_dis_admt_other_assets_specify_non_rat_sens_itm = r34_dis_admt_other_assets_specify_non_rat_sens_itm;
    }

    public BigDecimal getR34_dis_admt_other_assets_specify_total() {
        return r34_dis_admt_other_assets_specify_total;
    }

    public void setR34_dis_admt_other_assets_specify_total(BigDecimal r34_dis_admt_other_assets_specify_total) {
        this.r34_dis_admt_other_assets_specify_total = r34_dis_admt_other_assets_specify_total;
    }

    public String getR35_dis_admt_fix_rate_items_txt() {
        return r35_dis_admt_fix_rate_items_txt;
    }

    public void setR35_dis_admt_fix_rate_items_txt(String r35_dis_admt_fix_rate_items_txt) {
        this.r35_dis_admt_fix_rate_items_txt = r35_dis_admt_fix_rate_items_txt;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_up_to_1_mnt() {
        return r35_dis_admt_fix_rate_items_up_to_1_mnt;
    }

    public void setR35_dis_admt_fix_rate_items_up_to_1_mnt(BigDecimal r35_dis_admt_fix_rate_items_up_to_1_mnt) {
        this.r35_dis_admt_fix_rate_items_up_to_1_mnt = r35_dis_admt_fix_rate_items_up_to_1_mnt;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_1_to_3_mon() {
        return r35_dis_admt_fix_rate_items_mor_then_1_to_3_mon;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_1_to_3_mon(BigDecimal r35_dis_admt_fix_rate_items_mor_then_1_to_3_mon) {
        this.r35_dis_admt_fix_rate_items_mor_then_1_to_3_mon = r35_dis_admt_fix_rate_items_mor_then_1_to_3_mon;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_3_to_6_mon() {
        return r35_dis_admt_fix_rate_items_mor_then_3_to_6_mon;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_3_to_6_mon(BigDecimal r35_dis_admt_fix_rate_items_mor_then_3_to_6_mon) {
        this.r35_dis_admt_fix_rate_items_mor_then_3_to_6_mon = r35_dis_admt_fix_rate_items_mor_then_3_to_6_mon;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_6_to_12_mon() {
        return r35_dis_admt_fix_rate_items_mor_then_6_to_12_mon;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_6_to_12_mon(BigDecimal r35_dis_admt_fix_rate_items_mor_then_6_to_12_mon) {
        this.r35_dis_admt_fix_rate_items_mor_then_6_to_12_mon = r35_dis_admt_fix_rate_items_mor_then_6_to_12_mon;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year() {
        return r35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year(BigDecimal r35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year) {
        this.r35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year = r35_dis_admt_fix_rate_items_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_3_to_5_year() {
        return r35_dis_admt_fix_rate_items_mor_then_3_to_5_year;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_3_to_5_year(BigDecimal r35_dis_admt_fix_rate_items_mor_then_3_to_5_year) {
        this.r35_dis_admt_fix_rate_items_mor_then_3_to_5_year = r35_dis_admt_fix_rate_items_mor_then_3_to_5_year;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_5_to_10_year() {
        return r35_dis_admt_fix_rate_items_mor_then_5_to_10_year;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_5_to_10_year(BigDecimal r35_dis_admt_fix_rate_items_mor_then_5_to_10_year) {
        this.r35_dis_admt_fix_rate_items_mor_then_5_to_10_year = r35_dis_admt_fix_rate_items_mor_then_5_to_10_year;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_mor_then_10_year() {
        return r35_dis_admt_fix_rate_items_mor_then_10_year;
    }

    public void setR35_dis_admt_fix_rate_items_mor_then_10_year(BigDecimal r35_dis_admt_fix_rate_items_mor_then_10_year) {
        this.r35_dis_admt_fix_rate_items_mor_then_10_year = r35_dis_admt_fix_rate_items_mor_then_10_year;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_non_rat_sens_itm() {
        return r35_dis_admt_fix_rate_items_non_rat_sens_itm;
    }

    public void setR35_dis_admt_fix_rate_items_non_rat_sens_itm(BigDecimal r35_dis_admt_fix_rate_items_non_rat_sens_itm) {
        this.r35_dis_admt_fix_rate_items_non_rat_sens_itm = r35_dis_admt_fix_rate_items_non_rat_sens_itm;
    }

    public BigDecimal getR35_dis_admt_fix_rate_items_total() {
        return r35_dis_admt_fix_rate_items_total;
    }

    public void setR35_dis_admt_fix_rate_items_total(BigDecimal r35_dis_admt_fix_rate_items_total) {
        this.r35_dis_admt_fix_rate_items_total = r35_dis_admt_fix_rate_items_total;
    }

    public String getR36_fix_rate_cash_txt() {
        return r36_fix_rate_cash_txt;
    }

    public void setR36_fix_rate_cash_txt(String r36_fix_rate_cash_txt) {
        this.r36_fix_rate_cash_txt = r36_fix_rate_cash_txt;
    }

    public BigDecimal getR36_fix_rate_cash_up_to_1_mnt() {
        return r36_fix_rate_cash_up_to_1_mnt;
    }

    public void setR36_fix_rate_cash_up_to_1_mnt(BigDecimal r36_fix_rate_cash_up_to_1_mnt) {
        this.r36_fix_rate_cash_up_to_1_mnt = r36_fix_rate_cash_up_to_1_mnt;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_1_to_3_mon() {
        return r36_fix_rate_cash_mor_then_1_to_3_mon;
    }

    public void setR36_fix_rate_cash_mor_then_1_to_3_mon(BigDecimal r36_fix_rate_cash_mor_then_1_to_3_mon) {
        this.r36_fix_rate_cash_mor_then_1_to_3_mon = r36_fix_rate_cash_mor_then_1_to_3_mon;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_3_to_6_mon() {
        return r36_fix_rate_cash_mor_then_3_to_6_mon;
    }

    public void setR36_fix_rate_cash_mor_then_3_to_6_mon(BigDecimal r36_fix_rate_cash_mor_then_3_to_6_mon) {
        this.r36_fix_rate_cash_mor_then_3_to_6_mon = r36_fix_rate_cash_mor_then_3_to_6_mon;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_6_to_12_mon() {
        return r36_fix_rate_cash_mor_then_6_to_12_mon;
    }

    public void setR36_fix_rate_cash_mor_then_6_to_12_mon(BigDecimal r36_fix_rate_cash_mor_then_6_to_12_mon) {
        this.r36_fix_rate_cash_mor_then_6_to_12_mon = r36_fix_rate_cash_mor_then_6_to_12_mon;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_12_mon_to_3_year() {
        return r36_fix_rate_cash_mor_then_12_mon_to_3_year;
    }

    public void setR36_fix_rate_cash_mor_then_12_mon_to_3_year(BigDecimal r36_fix_rate_cash_mor_then_12_mon_to_3_year) {
        this.r36_fix_rate_cash_mor_then_12_mon_to_3_year = r36_fix_rate_cash_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_3_to_5_year() {
        return r36_fix_rate_cash_mor_then_3_to_5_year;
    }

    public void setR36_fix_rate_cash_mor_then_3_to_5_year(BigDecimal r36_fix_rate_cash_mor_then_3_to_5_year) {
        this.r36_fix_rate_cash_mor_then_3_to_5_year = r36_fix_rate_cash_mor_then_3_to_5_year;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_5_to_10_year() {
        return r36_fix_rate_cash_mor_then_5_to_10_year;
    }

    public void setR36_fix_rate_cash_mor_then_5_to_10_year(BigDecimal r36_fix_rate_cash_mor_then_5_to_10_year) {
        this.r36_fix_rate_cash_mor_then_5_to_10_year = r36_fix_rate_cash_mor_then_5_to_10_year;
    }

    public BigDecimal getR36_fix_rate_cash_mor_then_10_year() {
        return r36_fix_rate_cash_mor_then_10_year;
    }

    public void setR36_fix_rate_cash_mor_then_10_year(BigDecimal r36_fix_rate_cash_mor_then_10_year) {
        this.r36_fix_rate_cash_mor_then_10_year = r36_fix_rate_cash_mor_then_10_year;
    }

    public BigDecimal getR36_fix_rate_cash_non_rat_sens_itm() {
        return r36_fix_rate_cash_non_rat_sens_itm;
    }

    public void setR36_fix_rate_cash_non_rat_sens_itm(BigDecimal r36_fix_rate_cash_non_rat_sens_itm) {
        this.r36_fix_rate_cash_non_rat_sens_itm = r36_fix_rate_cash_non_rat_sens_itm;
    }

    public BigDecimal getR36_fix_rate_cash_total() {
        return r36_fix_rate_cash_total;
    }

    public void setR36_fix_rate_cash_total(BigDecimal r36_fix_rate_cash_total) {
        this.r36_fix_rate_cash_total = r36_fix_rate_cash_total;
    }

    public String getR37_fix_rate_bal_bnk_of_botswana_txt() {
        return r37_fix_rate_bal_bnk_of_botswana_txt;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_txt(String r37_fix_rate_bal_bnk_of_botswana_txt) {
        this.r37_fix_rate_bal_bnk_of_botswana_txt = r37_fix_rate_bal_bnk_of_botswana_txt;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt() {
        return r37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt(BigDecimal r37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt) {
        this.r37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt = r37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon = r37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon = r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon = r37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year = r37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year = r37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year = r37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_mor_then_10_year() {
        return r37_fix_rate_bal_bnk_of_botswana_mor_then_10_year;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_mor_then_10_year(BigDecimal r37_fix_rate_bal_bnk_of_botswana_mor_then_10_year) {
        this.r37_fix_rate_bal_bnk_of_botswana_mor_then_10_year = r37_fix_rate_bal_bnk_of_botswana_mor_then_10_year;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm() {
        return r37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm(BigDecimal r37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm) {
        this.r37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm = r37_fix_rate_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public BigDecimal getR37_fix_rate_bal_bnk_of_botswana_total() {
        return r37_fix_rate_bal_bnk_of_botswana_total;
    }

    public void setR37_fix_rate_bal_bnk_of_botswana_total(BigDecimal r37_fix_rate_bal_bnk_of_botswana_total) {
        this.r37_fix_rate_bal_bnk_of_botswana_total = r37_fix_rate_bal_bnk_of_botswana_total;
    }

    public String getR38_fix_rate_bal_doms_bnks_txt() {
        return r38_fix_rate_bal_doms_bnks_txt;
    }

    public void setR38_fix_rate_bal_doms_bnks_txt(String r38_fix_rate_bal_doms_bnks_txt) {
        this.r38_fix_rate_bal_doms_bnks_txt = r38_fix_rate_bal_doms_bnks_txt;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_up_to_1_mnt() {
        return r38_fix_rate_bal_doms_bnks_up_to_1_mnt;
    }

    public void setR38_fix_rate_bal_doms_bnks_up_to_1_mnt(BigDecimal r38_fix_rate_bal_doms_bnks_up_to_1_mnt) {
        this.r38_fix_rate_bal_doms_bnks_up_to_1_mnt = r38_fix_rate_bal_doms_bnks_up_to_1_mnt;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon() {
        return r38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon = r38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon() {
        return r38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon = r38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon() {
        return r38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon = r38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year() {
        return r38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year = r38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year() {
        return r38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year = r38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year() {
        return r38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year = r38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_mor_then_10_year() {
        return r38_fix_rate_bal_doms_bnks_mor_then_10_year;
    }

    public void setR38_fix_rate_bal_doms_bnks_mor_then_10_year(BigDecimal r38_fix_rate_bal_doms_bnks_mor_then_10_year) {
        this.r38_fix_rate_bal_doms_bnks_mor_then_10_year = r38_fix_rate_bal_doms_bnks_mor_then_10_year;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_non_rat_sens_itm() {
        return r38_fix_rate_bal_doms_bnks_non_rat_sens_itm;
    }

    public void setR38_fix_rate_bal_doms_bnks_non_rat_sens_itm(BigDecimal r38_fix_rate_bal_doms_bnks_non_rat_sens_itm) {
        this.r38_fix_rate_bal_doms_bnks_non_rat_sens_itm = r38_fix_rate_bal_doms_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR38_fix_rate_bal_doms_bnks_total() {
        return r38_fix_rate_bal_doms_bnks_total;
    }

    public void setR38_fix_rate_bal_doms_bnks_total(BigDecimal r38_fix_rate_bal_doms_bnks_total) {
        this.r38_fix_rate_bal_doms_bnks_total = r38_fix_rate_bal_doms_bnks_total;
    }

    public String getR39_fix_rate_bal_foreign_bnks_txt() {
        return r39_fix_rate_bal_foreign_bnks_txt;
    }

    public void setR39_fix_rate_bal_foreign_bnks_txt(String r39_fix_rate_bal_foreign_bnks_txt) {
        this.r39_fix_rate_bal_foreign_bnks_txt = r39_fix_rate_bal_foreign_bnks_txt;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_up_to_1_mnt() {
        return r39_fix_rate_bal_foreign_bnks_up_to_1_mnt;
    }

    public void setR39_fix_rate_bal_foreign_bnks_up_to_1_mnt(BigDecimal r39_fix_rate_bal_foreign_bnks_up_to_1_mnt) {
        this.r39_fix_rate_bal_foreign_bnks_up_to_1_mnt = r39_fix_rate_bal_foreign_bnks_up_to_1_mnt;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon = r39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon = r39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon = r39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year = r39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year = r39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year = r39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_mor_then_10_year() {
        return r39_fix_rate_bal_foreign_bnks_mor_then_10_year;
    }

    public void setR39_fix_rate_bal_foreign_bnks_mor_then_10_year(BigDecimal r39_fix_rate_bal_foreign_bnks_mor_then_10_year) {
        this.r39_fix_rate_bal_foreign_bnks_mor_then_10_year = r39_fix_rate_bal_foreign_bnks_mor_then_10_year;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_non_rat_sens_itm() {
        return r39_fix_rate_bal_foreign_bnks_non_rat_sens_itm;
    }

    public void setR39_fix_rate_bal_foreign_bnks_non_rat_sens_itm(BigDecimal r39_fix_rate_bal_foreign_bnks_non_rat_sens_itm) {
        this.r39_fix_rate_bal_foreign_bnks_non_rat_sens_itm = r39_fix_rate_bal_foreign_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR39_fix_rate_bal_foreign_bnks_total() {
        return r39_fix_rate_bal_foreign_bnks_total;
    }

    public void setR39_fix_rate_bal_foreign_bnks_total(BigDecimal r39_fix_rate_bal_foreign_bnks_total) {
        this.r39_fix_rate_bal_foreign_bnks_total = r39_fix_rate_bal_foreign_bnks_total;
    }

    public String getR40_fix_rate_bal_related_comp_txt() {
        return r40_fix_rate_bal_related_comp_txt;
    }

    public void setR40_fix_rate_bal_related_comp_txt(String r40_fix_rate_bal_related_comp_txt) {
        this.r40_fix_rate_bal_related_comp_txt = r40_fix_rate_bal_related_comp_txt;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_up_to_1_mnt() {
        return r40_fix_rate_bal_related_comp_up_to_1_mnt;
    }

    public void setR40_fix_rate_bal_related_comp_up_to_1_mnt(BigDecimal r40_fix_rate_bal_related_comp_up_to_1_mnt) {
        this.r40_fix_rate_bal_related_comp_up_to_1_mnt = r40_fix_rate_bal_related_comp_up_to_1_mnt;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_1_to_3_mon() {
        return r40_fix_rate_bal_related_comp_mor_then_1_to_3_mon;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_1_to_3_mon(BigDecimal r40_fix_rate_bal_related_comp_mor_then_1_to_3_mon) {
        this.r40_fix_rate_bal_related_comp_mor_then_1_to_3_mon = r40_fix_rate_bal_related_comp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_3_to_6_mon() {
        return r40_fix_rate_bal_related_comp_mor_then_3_to_6_mon;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_3_to_6_mon(BigDecimal r40_fix_rate_bal_related_comp_mor_then_3_to_6_mon) {
        this.r40_fix_rate_bal_related_comp_mor_then_3_to_6_mon = r40_fix_rate_bal_related_comp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_6_to_12_mon() {
        return r40_fix_rate_bal_related_comp_mor_then_6_to_12_mon;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_6_to_12_mon(BigDecimal r40_fix_rate_bal_related_comp_mor_then_6_to_12_mon) {
        this.r40_fix_rate_bal_related_comp_mor_then_6_to_12_mon = r40_fix_rate_bal_related_comp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year() {
        return r40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year(BigDecimal r40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year) {
        this.r40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year = r40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_3_to_5_year() {
        return r40_fix_rate_bal_related_comp_mor_then_3_to_5_year;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_3_to_5_year(BigDecimal r40_fix_rate_bal_related_comp_mor_then_3_to_5_year) {
        this.r40_fix_rate_bal_related_comp_mor_then_3_to_5_year = r40_fix_rate_bal_related_comp_mor_then_3_to_5_year;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_5_to_10_year() {
        return r40_fix_rate_bal_related_comp_mor_then_5_to_10_year;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_5_to_10_year(BigDecimal r40_fix_rate_bal_related_comp_mor_then_5_to_10_year) {
        this.r40_fix_rate_bal_related_comp_mor_then_5_to_10_year = r40_fix_rate_bal_related_comp_mor_then_5_to_10_year;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_mor_then_10_year() {
        return r40_fix_rate_bal_related_comp_mor_then_10_year;
    }

    public void setR40_fix_rate_bal_related_comp_mor_then_10_year(BigDecimal r40_fix_rate_bal_related_comp_mor_then_10_year) {
        this.r40_fix_rate_bal_related_comp_mor_then_10_year = r40_fix_rate_bal_related_comp_mor_then_10_year;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_non_rat_sens_itm() {
        return r40_fix_rate_bal_related_comp_non_rat_sens_itm;
    }

    public void setR40_fix_rate_bal_related_comp_non_rat_sens_itm(BigDecimal r40_fix_rate_bal_related_comp_non_rat_sens_itm) {
        this.r40_fix_rate_bal_related_comp_non_rat_sens_itm = r40_fix_rate_bal_related_comp_non_rat_sens_itm;
    }

    public BigDecimal getR40_fix_rate_bal_related_comp_total() {
        return r40_fix_rate_bal_related_comp_total;
    }

    public void setR40_fix_rate_bal_related_comp_total(BigDecimal r40_fix_rate_bal_related_comp_total) {
        this.r40_fix_rate_bal_related_comp_total = r40_fix_rate_bal_related_comp_total;
    }

    public String getR41_fix_rate_bnk_of_botswana_cert_txt() {
        return r41_fix_rate_bnk_of_botswana_cert_txt;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_txt(String r41_fix_rate_bnk_of_botswana_cert_txt) {
        this.r41_fix_rate_bnk_of_botswana_cert_txt = r41_fix_rate_bnk_of_botswana_cert_txt;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt() {
        return r41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt(BigDecimal r41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt) {
        this.r41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt = r41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon = r41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon = r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon = r41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year = r41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year = r41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year = r41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_mor_then_10_year() {
        return r41_fix_rate_bnk_of_botswana_cert_mor_then_10_year;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_mor_then_10_year(BigDecimal r41_fix_rate_bnk_of_botswana_cert_mor_then_10_year) {
        this.r41_fix_rate_bnk_of_botswana_cert_mor_then_10_year = r41_fix_rate_bnk_of_botswana_cert_mor_then_10_year;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm() {
        return r41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm(BigDecimal r41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm) {
        this.r41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm = r41_fix_rate_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public BigDecimal getR41_fix_rate_bnk_of_botswana_cert_total() {
        return r41_fix_rate_bnk_of_botswana_cert_total;
    }

    public void setR41_fix_rate_bnk_of_botswana_cert_total(BigDecimal r41_fix_rate_bnk_of_botswana_cert_total) {
        this.r41_fix_rate_bnk_of_botswana_cert_total = r41_fix_rate_bnk_of_botswana_cert_total;
    }

    public String getR42_fix_rate_gov_bonds_txt() {
        return r42_fix_rate_gov_bonds_txt;
    }

    public void setR42_fix_rate_gov_bonds_txt(String r42_fix_rate_gov_bonds_txt) {
        this.r42_fix_rate_gov_bonds_txt = r42_fix_rate_gov_bonds_txt;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_up_to_1_mnt() {
        return r42_fix_rate_gov_bonds_up_to_1_mnt;
    }

    public void setR42_fix_rate_gov_bonds_up_to_1_mnt(BigDecimal r42_fix_rate_gov_bonds_up_to_1_mnt) {
        this.r42_fix_rate_gov_bonds_up_to_1_mnt = r42_fix_rate_gov_bonds_up_to_1_mnt;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_1_to_3_mon() {
        return r42_fix_rate_gov_bonds_mor_then_1_to_3_mon;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_1_to_3_mon(BigDecimal r42_fix_rate_gov_bonds_mor_then_1_to_3_mon) {
        this.r42_fix_rate_gov_bonds_mor_then_1_to_3_mon = r42_fix_rate_gov_bonds_mor_then_1_to_3_mon;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_3_to_6_mon() {
        return r42_fix_rate_gov_bonds_mor_then_3_to_6_mon;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_3_to_6_mon(BigDecimal r42_fix_rate_gov_bonds_mor_then_3_to_6_mon) {
        this.r42_fix_rate_gov_bonds_mor_then_3_to_6_mon = r42_fix_rate_gov_bonds_mor_then_3_to_6_mon;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_6_to_12_mon() {
        return r42_fix_rate_gov_bonds_mor_then_6_to_12_mon;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_6_to_12_mon(BigDecimal r42_fix_rate_gov_bonds_mor_then_6_to_12_mon) {
        this.r42_fix_rate_gov_bonds_mor_then_6_to_12_mon = r42_fix_rate_gov_bonds_mor_then_6_to_12_mon;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year() {
        return r42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year(BigDecimal r42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year) {
        this.r42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year = r42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_3_to_5_year() {
        return r42_fix_rate_gov_bonds_mor_then_3_to_5_year;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_3_to_5_year(BigDecimal r42_fix_rate_gov_bonds_mor_then_3_to_5_year) {
        this.r42_fix_rate_gov_bonds_mor_then_3_to_5_year = r42_fix_rate_gov_bonds_mor_then_3_to_5_year;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_5_to_10_year() {
        return r42_fix_rate_gov_bonds_mor_then_5_to_10_year;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_5_to_10_year(BigDecimal r42_fix_rate_gov_bonds_mor_then_5_to_10_year) {
        this.r42_fix_rate_gov_bonds_mor_then_5_to_10_year = r42_fix_rate_gov_bonds_mor_then_5_to_10_year;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_mor_then_10_year() {
        return r42_fix_rate_gov_bonds_mor_then_10_year;
    }

    public void setR42_fix_rate_gov_bonds_mor_then_10_year(BigDecimal r42_fix_rate_gov_bonds_mor_then_10_year) {
        this.r42_fix_rate_gov_bonds_mor_then_10_year = r42_fix_rate_gov_bonds_mor_then_10_year;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_non_rat_sens_itm() {
        return r42_fix_rate_gov_bonds_non_rat_sens_itm;
    }

    public void setR42_fix_rate_gov_bonds_non_rat_sens_itm(BigDecimal r42_fix_rate_gov_bonds_non_rat_sens_itm) {
        this.r42_fix_rate_gov_bonds_non_rat_sens_itm = r42_fix_rate_gov_bonds_non_rat_sens_itm;
    }

    public BigDecimal getR42_fix_rate_gov_bonds_total() {
        return r42_fix_rate_gov_bonds_total;
    }

    public void setR42_fix_rate_gov_bonds_total(BigDecimal r42_fix_rate_gov_bonds_total) {
        this.r42_fix_rate_gov_bonds_total = r42_fix_rate_gov_bonds_total;
    }

    public String getR43_fix_rate_other_invt_specify_txt() {
        return r43_fix_rate_other_invt_specify_txt;
    }

    public void setR43_fix_rate_other_invt_specify_txt(String r43_fix_rate_other_invt_specify_txt) {
        this.r43_fix_rate_other_invt_specify_txt = r43_fix_rate_other_invt_specify_txt;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_up_to_1_mnt() {
        return r43_fix_rate_other_invt_specify_up_to_1_mnt;
    }

    public void setR43_fix_rate_other_invt_specify_up_to_1_mnt(BigDecimal r43_fix_rate_other_invt_specify_up_to_1_mnt) {
        this.r43_fix_rate_other_invt_specify_up_to_1_mnt = r43_fix_rate_other_invt_specify_up_to_1_mnt;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_1_to_3_mon() {
        return r43_fix_rate_other_invt_specify_mor_then_1_to_3_mon;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_1_to_3_mon(BigDecimal r43_fix_rate_other_invt_specify_mor_then_1_to_3_mon) {
        this.r43_fix_rate_other_invt_specify_mor_then_1_to_3_mon = r43_fix_rate_other_invt_specify_mor_then_1_to_3_mon;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_3_to_6_mon() {
        return r43_fix_rate_other_invt_specify_mor_then_3_to_6_mon;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_3_to_6_mon(BigDecimal r43_fix_rate_other_invt_specify_mor_then_3_to_6_mon) {
        this.r43_fix_rate_other_invt_specify_mor_then_3_to_6_mon = r43_fix_rate_other_invt_specify_mor_then_3_to_6_mon;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_6_to_12_mon() {
        return r43_fix_rate_other_invt_specify_mor_then_6_to_12_mon;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_6_to_12_mon(BigDecimal r43_fix_rate_other_invt_specify_mor_then_6_to_12_mon) {
        this.r43_fix_rate_other_invt_specify_mor_then_6_to_12_mon = r43_fix_rate_other_invt_specify_mor_then_6_to_12_mon;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year() {
        return r43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year(BigDecimal r43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year) {
        this.r43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year = r43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_3_to_5_year() {
        return r43_fix_rate_other_invt_specify_mor_then_3_to_5_year;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_3_to_5_year(BigDecimal r43_fix_rate_other_invt_specify_mor_then_3_to_5_year) {
        this.r43_fix_rate_other_invt_specify_mor_then_3_to_5_year = r43_fix_rate_other_invt_specify_mor_then_3_to_5_year;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_5_to_10_year() {
        return r43_fix_rate_other_invt_specify_mor_then_5_to_10_year;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_5_to_10_year(BigDecimal r43_fix_rate_other_invt_specify_mor_then_5_to_10_year) {
        this.r43_fix_rate_other_invt_specify_mor_then_5_to_10_year = r43_fix_rate_other_invt_specify_mor_then_5_to_10_year;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_mor_then_10_year() {
        return r43_fix_rate_other_invt_specify_mor_then_10_year;
    }

    public void setR43_fix_rate_other_invt_specify_mor_then_10_year(BigDecimal r43_fix_rate_other_invt_specify_mor_then_10_year) {
        this.r43_fix_rate_other_invt_specify_mor_then_10_year = r43_fix_rate_other_invt_specify_mor_then_10_year;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_non_rat_sens_itm() {
        return r43_fix_rate_other_invt_specify_non_rat_sens_itm;
    }

    public void setR43_fix_rate_other_invt_specify_non_rat_sens_itm(BigDecimal r43_fix_rate_other_invt_specify_non_rat_sens_itm) {
        this.r43_fix_rate_other_invt_specify_non_rat_sens_itm = r43_fix_rate_other_invt_specify_non_rat_sens_itm;
    }

    public BigDecimal getR43_fix_rate_other_invt_specify_total() {
        return r43_fix_rate_other_invt_specify_total;
    }

    public void setR43_fix_rate_other_invt_specify_total(BigDecimal r43_fix_rate_other_invt_specify_total) {
        this.r43_fix_rate_other_invt_specify_total = r43_fix_rate_other_invt_specify_total;
    }

    public String getR44_fix_rate_loans_and_adv_to_cust_txt() {
        return r44_fix_rate_loans_and_adv_to_cust_txt;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_txt(String r44_fix_rate_loans_and_adv_to_cust_txt) {
        this.r44_fix_rate_loans_and_adv_to_cust_txt = r44_fix_rate_loans_and_adv_to_cust_txt;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt() {
        return r44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt(BigDecimal r44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt) {
        this.r44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt = r44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon = r44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon = r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon = r44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year = r44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year = r44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year = r44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_mor_then_10_year() {
        return r44_fix_rate_loans_and_adv_to_cust_mor_then_10_year;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_mor_then_10_year(BigDecimal r44_fix_rate_loans_and_adv_to_cust_mor_then_10_year) {
        this.r44_fix_rate_loans_and_adv_to_cust_mor_then_10_year = r44_fix_rate_loans_and_adv_to_cust_mor_then_10_year;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm() {
        return r44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm(BigDecimal r44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm) {
        this.r44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm = r44_fix_rate_loans_and_adv_to_cust_non_rat_sens_itm;
    }

    public BigDecimal getR44_fix_rate_loans_and_adv_to_cust_total() {
        return r44_fix_rate_loans_and_adv_to_cust_total;
    }

    public void setR44_fix_rate_loans_and_adv_to_cust_total(BigDecimal r44_fix_rate_loans_and_adv_to_cust_total) {
        this.r44_fix_rate_loans_and_adv_to_cust_total = r44_fix_rate_loans_and_adv_to_cust_total;
    }

    public String getR45_fix_rate_prop_and_eqp_txt() {
        return r45_fix_rate_prop_and_eqp_txt;
    }

    public void setR45_fix_rate_prop_and_eqp_txt(String r45_fix_rate_prop_and_eqp_txt) {
        this.r45_fix_rate_prop_and_eqp_txt = r45_fix_rate_prop_and_eqp_txt;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_up_to_1_mnt() {
        return r45_fix_rate_prop_and_eqp_up_to_1_mnt;
    }

    public void setR45_fix_rate_prop_and_eqp_up_to_1_mnt(BigDecimal r45_fix_rate_prop_and_eqp_up_to_1_mnt) {
        this.r45_fix_rate_prop_and_eqp_up_to_1_mnt = r45_fix_rate_prop_and_eqp_up_to_1_mnt;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon() {
        return r45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon) {
        this.r45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon = r45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon() {
        return r45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon) {
        this.r45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon = r45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon() {
        return r45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon) {
        this.r45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon = r45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year() {
        return r45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year) {
        this.r45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year = r45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_3_to_5_year() {
        return r45_fix_rate_prop_and_eqp_mor_then_3_to_5_year;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_3_to_5_year(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_3_to_5_year) {
        this.r45_fix_rate_prop_and_eqp_mor_then_3_to_5_year = r45_fix_rate_prop_and_eqp_mor_then_3_to_5_year;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_5_to_10_year() {
        return r45_fix_rate_prop_and_eqp_mor_then_5_to_10_year;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_5_to_10_year(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_5_to_10_year) {
        this.r45_fix_rate_prop_and_eqp_mor_then_5_to_10_year = r45_fix_rate_prop_and_eqp_mor_then_5_to_10_year;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_mor_then_10_year() {
        return r45_fix_rate_prop_and_eqp_mor_then_10_year;
    }

    public void setR45_fix_rate_prop_and_eqp_mor_then_10_year(BigDecimal r45_fix_rate_prop_and_eqp_mor_then_10_year) {
        this.r45_fix_rate_prop_and_eqp_mor_then_10_year = r45_fix_rate_prop_and_eqp_mor_then_10_year;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_non_rat_sens_itm() {
        return r45_fix_rate_prop_and_eqp_non_rat_sens_itm;
    }

    public void setR45_fix_rate_prop_and_eqp_non_rat_sens_itm(BigDecimal r45_fix_rate_prop_and_eqp_non_rat_sens_itm) {
        this.r45_fix_rate_prop_and_eqp_non_rat_sens_itm = r45_fix_rate_prop_and_eqp_non_rat_sens_itm;
    }

    public BigDecimal getR45_fix_rate_prop_and_eqp_total() {
        return r45_fix_rate_prop_and_eqp_total;
    }

    public void setR45_fix_rate_prop_and_eqp_total(BigDecimal r45_fix_rate_prop_and_eqp_total) {
        this.r45_fix_rate_prop_and_eqp_total = r45_fix_rate_prop_and_eqp_total;
    }

    public String getR46_fix_rate_other_assets_specify_txt() {
        return r46_fix_rate_other_assets_specify_txt;
    }

    public void setR46_fix_rate_other_assets_specify_txt(String r46_fix_rate_other_assets_specify_txt) {
        this.r46_fix_rate_other_assets_specify_txt = r46_fix_rate_other_assets_specify_txt;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_up_to_1_mnt() {
        return r46_fix_rate_other_assets_specify_up_to_1_mnt;
    }

    public void setR46_fix_rate_other_assets_specify_up_to_1_mnt(BigDecimal r46_fix_rate_other_assets_specify_up_to_1_mnt) {
        this.r46_fix_rate_other_assets_specify_up_to_1_mnt = r46_fix_rate_other_assets_specify_up_to_1_mnt;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_1_to_3_mon() {
        return r46_fix_rate_other_assets_specify_mor_then_1_to_3_mon;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_1_to_3_mon(BigDecimal r46_fix_rate_other_assets_specify_mor_then_1_to_3_mon) {
        this.r46_fix_rate_other_assets_specify_mor_then_1_to_3_mon = r46_fix_rate_other_assets_specify_mor_then_1_to_3_mon;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_3_to_6_mon() {
        return r46_fix_rate_other_assets_specify_mor_then_3_to_6_mon;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_3_to_6_mon(BigDecimal r46_fix_rate_other_assets_specify_mor_then_3_to_6_mon) {
        this.r46_fix_rate_other_assets_specify_mor_then_3_to_6_mon = r46_fix_rate_other_assets_specify_mor_then_3_to_6_mon;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_6_to_12_mon() {
        return r46_fix_rate_other_assets_specify_mor_then_6_to_12_mon;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_6_to_12_mon(BigDecimal r46_fix_rate_other_assets_specify_mor_then_6_to_12_mon) {
        this.r46_fix_rate_other_assets_specify_mor_then_6_to_12_mon = r46_fix_rate_other_assets_specify_mor_then_6_to_12_mon;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year() {
        return r46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year(BigDecimal r46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year) {
        this.r46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year = r46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_3_to_5_year() {
        return r46_fix_rate_other_assets_specify_mor_then_3_to_5_year;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_3_to_5_year(BigDecimal r46_fix_rate_other_assets_specify_mor_then_3_to_5_year) {
        this.r46_fix_rate_other_assets_specify_mor_then_3_to_5_year = r46_fix_rate_other_assets_specify_mor_then_3_to_5_year;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_5_to_10_year() {
        return r46_fix_rate_other_assets_specify_mor_then_5_to_10_year;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_5_to_10_year(BigDecimal r46_fix_rate_other_assets_specify_mor_then_5_to_10_year) {
        this.r46_fix_rate_other_assets_specify_mor_then_5_to_10_year = r46_fix_rate_other_assets_specify_mor_then_5_to_10_year;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_mor_then_10_year() {
        return r46_fix_rate_other_assets_specify_mor_then_10_year;
    }

    public void setR46_fix_rate_other_assets_specify_mor_then_10_year(BigDecimal r46_fix_rate_other_assets_specify_mor_then_10_year) {
        this.r46_fix_rate_other_assets_specify_mor_then_10_year = r46_fix_rate_other_assets_specify_mor_then_10_year;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_non_rat_sens_itm() {
        return r46_fix_rate_other_assets_specify_non_rat_sens_itm;
    }

    public void setR46_fix_rate_other_assets_specify_non_rat_sens_itm(BigDecimal r46_fix_rate_other_assets_specify_non_rat_sens_itm) {
        this.r46_fix_rate_other_assets_specify_non_rat_sens_itm = r46_fix_rate_other_assets_specify_non_rat_sens_itm;
    }

    public BigDecimal getR46_fix_rate_other_assets_specify_total() {
        return r46_fix_rate_other_assets_specify_total;
    }

    public void setR46_fix_rate_other_assets_specify_total(BigDecimal r46_fix_rate_other_assets_specify_total) {
        this.r46_fix_rate_other_assets_specify_total = r46_fix_rate_other_assets_specify_total;
    }

    public String getR47_non_rate_sensitive_items_txt() {
        return r47_non_rate_sensitive_items_txt;
    }

    public void setR47_non_rate_sensitive_items_txt(String r47_non_rate_sensitive_items_txt) {
        this.r47_non_rate_sensitive_items_txt = r47_non_rate_sensitive_items_txt;
    }

    public BigDecimal getR47_non_rate_sensitive_items_up_to_1_mnt() {
        return r47_non_rate_sensitive_items_up_to_1_mnt;
    }

    public void setR47_non_rate_sensitive_items_up_to_1_mnt(BigDecimal r47_non_rate_sensitive_items_up_to_1_mnt) {
        this.r47_non_rate_sensitive_items_up_to_1_mnt = r47_non_rate_sensitive_items_up_to_1_mnt;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_1_to_3_mon() {
        return r47_non_rate_sensitive_items_mor_then_1_to_3_mon;
    }

    public void setR47_non_rate_sensitive_items_mor_then_1_to_3_mon(BigDecimal r47_non_rate_sensitive_items_mor_then_1_to_3_mon) {
        this.r47_non_rate_sensitive_items_mor_then_1_to_3_mon = r47_non_rate_sensitive_items_mor_then_1_to_3_mon;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_3_to_6_mon() {
        return r47_non_rate_sensitive_items_mor_then_3_to_6_mon;
    }

    public void setR47_non_rate_sensitive_items_mor_then_3_to_6_mon(BigDecimal r47_non_rate_sensitive_items_mor_then_3_to_6_mon) {
        this.r47_non_rate_sensitive_items_mor_then_3_to_6_mon = r47_non_rate_sensitive_items_mor_then_3_to_6_mon;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_6_to_12_mon() {
        return r47_non_rate_sensitive_items_mor_then_6_to_12_mon;
    }

    public void setR47_non_rate_sensitive_items_mor_then_6_to_12_mon(BigDecimal r47_non_rate_sensitive_items_mor_then_6_to_12_mon) {
        this.r47_non_rate_sensitive_items_mor_then_6_to_12_mon = r47_non_rate_sensitive_items_mor_then_6_to_12_mon;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_12_mon_to_3_year() {
        return r47_non_rate_sensitive_items_mor_then_12_mon_to_3_year;
    }

    public void setR47_non_rate_sensitive_items_mor_then_12_mon_to_3_year(BigDecimal r47_non_rate_sensitive_items_mor_then_12_mon_to_3_year) {
        this.r47_non_rate_sensitive_items_mor_then_12_mon_to_3_year = r47_non_rate_sensitive_items_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_3_to_5_year() {
        return r47_non_rate_sensitive_items_mor_then_3_to_5_year;
    }

    public void setR47_non_rate_sensitive_items_mor_then_3_to_5_year(BigDecimal r47_non_rate_sensitive_items_mor_then_3_to_5_year) {
        this.r47_non_rate_sensitive_items_mor_then_3_to_5_year = r47_non_rate_sensitive_items_mor_then_3_to_5_year;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_5_to_10_year() {
        return r47_non_rate_sensitive_items_mor_then_5_to_10_year;
    }

    public void setR47_non_rate_sensitive_items_mor_then_5_to_10_year(BigDecimal r47_non_rate_sensitive_items_mor_then_5_to_10_year) {
        this.r47_non_rate_sensitive_items_mor_then_5_to_10_year = r47_non_rate_sensitive_items_mor_then_5_to_10_year;
    }

    public BigDecimal getR47_non_rate_sensitive_items_mor_then_10_year() {
        return r47_non_rate_sensitive_items_mor_then_10_year;
    }

    public void setR47_non_rate_sensitive_items_mor_then_10_year(BigDecimal r47_non_rate_sensitive_items_mor_then_10_year) {
        this.r47_non_rate_sensitive_items_mor_then_10_year = r47_non_rate_sensitive_items_mor_then_10_year;
    }

    public BigDecimal getR47_non_rate_sensitive_items_non_rat_sens_itm() {
        return r47_non_rate_sensitive_items_non_rat_sens_itm;
    }

    public void setR47_non_rate_sensitive_items_non_rat_sens_itm(BigDecimal r47_non_rate_sensitive_items_non_rat_sens_itm) {
        this.r47_non_rate_sensitive_items_non_rat_sens_itm = r47_non_rate_sensitive_items_non_rat_sens_itm;
    }

    public BigDecimal getR47_non_rate_sensitive_items_total() {
        return r47_non_rate_sensitive_items_total;
    }

    public void setR47_non_rate_sensitive_items_total(BigDecimal r47_non_rate_sensitive_items_total) {
        this.r47_non_rate_sensitive_items_total = r47_non_rate_sensitive_items_total;
    }

    public String getR48_non_rate_sens_cash_txt() {
        return r48_non_rate_sens_cash_txt;
    }

    public void setR48_non_rate_sens_cash_txt(String r48_non_rate_sens_cash_txt) {
        this.r48_non_rate_sens_cash_txt = r48_non_rate_sens_cash_txt;
    }

    public BigDecimal getR48_non_rate_sens_cash_up_to_1_mnt() {
        return r48_non_rate_sens_cash_up_to_1_mnt;
    }

    public void setR48_non_rate_sens_cash_up_to_1_mnt(BigDecimal r48_non_rate_sens_cash_up_to_1_mnt) {
        this.r48_non_rate_sens_cash_up_to_1_mnt = r48_non_rate_sens_cash_up_to_1_mnt;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_1_to_3_mon() {
        return r48_non_rate_sens_cash_mor_then_1_to_3_mon;
    }

    public void setR48_non_rate_sens_cash_mor_then_1_to_3_mon(BigDecimal r48_non_rate_sens_cash_mor_then_1_to_3_mon) {
        this.r48_non_rate_sens_cash_mor_then_1_to_3_mon = r48_non_rate_sens_cash_mor_then_1_to_3_mon;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_3_to_6_mon() {
        return r48_non_rate_sens_cash_mor_then_3_to_6_mon;
    }

    public void setR48_non_rate_sens_cash_mor_then_3_to_6_mon(BigDecimal r48_non_rate_sens_cash_mor_then_3_to_6_mon) {
        this.r48_non_rate_sens_cash_mor_then_3_to_6_mon = r48_non_rate_sens_cash_mor_then_3_to_6_mon;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_6_to_12_mon() {
        return r48_non_rate_sens_cash_mor_then_6_to_12_mon;
    }

    public void setR48_non_rate_sens_cash_mor_then_6_to_12_mon(BigDecimal r48_non_rate_sens_cash_mor_then_6_to_12_mon) {
        this.r48_non_rate_sens_cash_mor_then_6_to_12_mon = r48_non_rate_sens_cash_mor_then_6_to_12_mon;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_12_mon_to_3_year() {
        return r48_non_rate_sens_cash_mor_then_12_mon_to_3_year;
    }

    public void setR48_non_rate_sens_cash_mor_then_12_mon_to_3_year(BigDecimal r48_non_rate_sens_cash_mor_then_12_mon_to_3_year) {
        this.r48_non_rate_sens_cash_mor_then_12_mon_to_3_year = r48_non_rate_sens_cash_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_3_to_5_year() {
        return r48_non_rate_sens_cash_mor_then_3_to_5_year;
    }

    public void setR48_non_rate_sens_cash_mor_then_3_to_5_year(BigDecimal r48_non_rate_sens_cash_mor_then_3_to_5_year) {
        this.r48_non_rate_sens_cash_mor_then_3_to_5_year = r48_non_rate_sens_cash_mor_then_3_to_5_year;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_5_to_10_year() {
        return r48_non_rate_sens_cash_mor_then_5_to_10_year;
    }

    public void setR48_non_rate_sens_cash_mor_then_5_to_10_year(BigDecimal r48_non_rate_sens_cash_mor_then_5_to_10_year) {
        this.r48_non_rate_sens_cash_mor_then_5_to_10_year = r48_non_rate_sens_cash_mor_then_5_to_10_year;
    }

    public BigDecimal getR48_non_rate_sens_cash_mor_then_10_year() {
        return r48_non_rate_sens_cash_mor_then_10_year;
    }

    public void setR48_non_rate_sens_cash_mor_then_10_year(BigDecimal r48_non_rate_sens_cash_mor_then_10_year) {
        this.r48_non_rate_sens_cash_mor_then_10_year = r48_non_rate_sens_cash_mor_then_10_year;
    }

    public BigDecimal getR48_non_rate_sens_cash_non_rat_sens_itm() {
        return r48_non_rate_sens_cash_non_rat_sens_itm;
    }

    public void setR48_non_rate_sens_cash_non_rat_sens_itm(BigDecimal r48_non_rate_sens_cash_non_rat_sens_itm) {
        this.r48_non_rate_sens_cash_non_rat_sens_itm = r48_non_rate_sens_cash_non_rat_sens_itm;
    }

    public BigDecimal getR48_non_rate_sens_cash_total() {
        return r48_non_rate_sens_cash_total;
    }

    public void setR48_non_rate_sens_cash_total(BigDecimal r48_non_rate_sens_cash_total) {
        this.r48_non_rate_sens_cash_total = r48_non_rate_sens_cash_total;
    }

    public String getR49_non_rate_sens_bal_bnk_of_botswana_txt() {
        return r49_non_rate_sens_bal_bnk_of_botswana_txt;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_txt(String r49_non_rate_sens_bal_bnk_of_botswana_txt) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_txt = r49_non_rate_sens_bal_bnk_of_botswana_txt;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt() {
        return r49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt = r49_non_rate_sens_bal_bnk_of_botswana_up_to_1_mnt;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_1_to_3_mon;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_6_mon;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_6_to_12_mon;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_3_to_5_year;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_5_to_10_year;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year() {
        return r49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year = r49_non_rate_sens_bal_bnk_of_botswana_mor_then_10_year;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm() {
        return r49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm = r49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm;
    }

    public BigDecimal getR49_non_rate_sens_bal_bnk_of_botswana_total() {
        return r49_non_rate_sens_bal_bnk_of_botswana_total;
    }

    public void setR49_non_rate_sens_bal_bnk_of_botswana_total(BigDecimal r49_non_rate_sens_bal_bnk_of_botswana_total) {
        this.r49_non_rate_sens_bal_bnk_of_botswana_total = r49_non_rate_sens_bal_bnk_of_botswana_total;
    }

    public String getR50_non_rate_sens_bal_doms_bnks_txt() {
        return r50_non_rate_sens_bal_doms_bnks_txt;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_txt(String r50_non_rate_sens_bal_doms_bnks_txt) {
        this.r50_non_rate_sens_bal_doms_bnks_txt = r50_non_rate_sens_bal_doms_bnks_txt;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_up_to_1_mnt() {
        return r50_non_rate_sens_bal_doms_bnks_up_to_1_mnt;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_up_to_1_mnt(BigDecimal r50_non_rate_sens_bal_doms_bnks_up_to_1_mnt) {
        this.r50_non_rate_sens_bal_doms_bnks_up_to_1_mnt = r50_non_rate_sens_bal_doms_bnks_up_to_1_mnt;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon = r50_non_rate_sens_bal_doms_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon = r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon = r50_non_rate_sens_bal_doms_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year = r50_non_rate_sens_bal_doms_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year = r50_non_rate_sens_bal_doms_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year = r50_non_rate_sens_bal_doms_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_mor_then_10_year() {
        return r50_non_rate_sens_bal_doms_bnks_mor_then_10_year;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_mor_then_10_year(BigDecimal r50_non_rate_sens_bal_doms_bnks_mor_then_10_year) {
        this.r50_non_rate_sens_bal_doms_bnks_mor_then_10_year = r50_non_rate_sens_bal_doms_bnks_mor_then_10_year;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm() {
        return r50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm(BigDecimal r50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm) {
        this.r50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm = r50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR50_non_rate_sens_bal_doms_bnks_total() {
        return r50_non_rate_sens_bal_doms_bnks_total;
    }

    public void setR50_non_rate_sens_bal_doms_bnks_total(BigDecimal r50_non_rate_sens_bal_doms_bnks_total) {
        this.r50_non_rate_sens_bal_doms_bnks_total = r50_non_rate_sens_bal_doms_bnks_total;
    }

    public String getR51_non_rate_sens_bal_foreign_bnks_txt() {
        return r51_non_rate_sens_bal_foreign_bnks_txt;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_txt(String r51_non_rate_sens_bal_foreign_bnks_txt) {
        this.r51_non_rate_sens_bal_foreign_bnks_txt = r51_non_rate_sens_bal_foreign_bnks_txt;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt() {
        return r51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt(BigDecimal r51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt) {
        this.r51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt = r51_non_rate_sens_bal_foreign_bnks_up_to_1_mnt;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon = r51_non_rate_sens_bal_foreign_bnks_mor_then_1_to_3_mon;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon = r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_6_mon;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon = r51_non_rate_sens_bal_foreign_bnks_mor_then_6_to_12_mon;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year = r51_non_rate_sens_bal_foreign_bnks_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year = r51_non_rate_sens_bal_foreign_bnks_mor_then_3_to_5_year;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year = r51_non_rate_sens_bal_foreign_bnks_mor_then_5_to_10_year;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_mor_then_10_year() {
        return r51_non_rate_sens_bal_foreign_bnks_mor_then_10_year;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_mor_then_10_year(BigDecimal r51_non_rate_sens_bal_foreign_bnks_mor_then_10_year) {
        this.r51_non_rate_sens_bal_foreign_bnks_mor_then_10_year = r51_non_rate_sens_bal_foreign_bnks_mor_then_10_year;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm() {
        return r51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm(BigDecimal r51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm) {
        this.r51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm = r51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm;
    }

    public BigDecimal getR51_non_rate_sens_bal_foreign_bnks_total() {
        return r51_non_rate_sens_bal_foreign_bnks_total;
    }

    public void setR51_non_rate_sens_bal_foreign_bnks_total(BigDecimal r51_non_rate_sens_bal_foreign_bnks_total) {
        this.r51_non_rate_sens_bal_foreign_bnks_total = r51_non_rate_sens_bal_foreign_bnks_total;
    }

    public String getR52_non_rate_sens_bal_related_comp_txt() {
        return r52_non_rate_sens_bal_related_comp_txt;
    }

    public void setR52_non_rate_sens_bal_related_comp_txt(String r52_non_rate_sens_bal_related_comp_txt) {
        this.r52_non_rate_sens_bal_related_comp_txt = r52_non_rate_sens_bal_related_comp_txt;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_up_to_1_mnt() {
        return r52_non_rate_sens_bal_related_comp_up_to_1_mnt;
    }

    public void setR52_non_rate_sens_bal_related_comp_up_to_1_mnt(BigDecimal r52_non_rate_sens_bal_related_comp_up_to_1_mnt) {
        this.r52_non_rate_sens_bal_related_comp_up_to_1_mnt = r52_non_rate_sens_bal_related_comp_up_to_1_mnt;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon() {
        return r52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon = r52_non_rate_sens_bal_related_comp_mor_then_1_to_3_mon;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon() {
        return r52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon = r52_non_rate_sens_bal_related_comp_mor_then_3_to_6_mon;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon() {
        return r52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon = r52_non_rate_sens_bal_related_comp_mor_then_6_to_12_mon;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year() {
        return r52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year = r52_non_rate_sens_bal_related_comp_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year() {
        return r52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year = r52_non_rate_sens_bal_related_comp_mor_then_3_to_5_year;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year() {
        return r52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year = r52_non_rate_sens_bal_related_comp_mor_then_5_to_10_year;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_mor_then_10_year() {
        return r52_non_rate_sens_bal_related_comp_mor_then_10_year;
    }

    public void setR52_non_rate_sens_bal_related_comp_mor_then_10_year(BigDecimal r52_non_rate_sens_bal_related_comp_mor_then_10_year) {
        this.r52_non_rate_sens_bal_related_comp_mor_then_10_year = r52_non_rate_sens_bal_related_comp_mor_then_10_year;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_non_rat_sens_itm() {
        return r52_non_rate_sens_bal_related_comp_non_rat_sens_itm;
    }

    public void setR52_non_rate_sens_bal_related_comp_non_rat_sens_itm(BigDecimal r52_non_rate_sens_bal_related_comp_non_rat_sens_itm) {
        this.r52_non_rate_sens_bal_related_comp_non_rat_sens_itm = r52_non_rate_sens_bal_related_comp_non_rat_sens_itm;
    }

    public BigDecimal getR52_non_rate_sens_bal_related_comp_total() {
        return r52_non_rate_sens_bal_related_comp_total;
    }

    public void setR52_non_rate_sens_bal_related_comp_total(BigDecimal r52_non_rate_sens_bal_related_comp_total) {
        this.r52_non_rate_sens_bal_related_comp_total = r52_non_rate_sens_bal_related_comp_total;
    }

    public String getR53_non_rate_sens_bnk_of_botswana_cert_txt() {
        return r53_non_rate_sens_bnk_of_botswana_cert_txt;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_txt(String r53_non_rate_sens_bnk_of_botswana_cert_txt) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_txt = r53_non_rate_sens_bnk_of_botswana_cert_txt;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt() {
        return r53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt = r53_non_rate_sens_bnk_of_botswana_cert_up_to_1_mnt;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_1_to_3_mon;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_6_mon;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_6_to_12_mon;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_12_mon_to_3_year;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_3_to_5_year;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_5_to_10_year;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year() {
        return r53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year = r53_non_rate_sens_bnk_of_botswana_cert_mor_then_10_year;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm() {
        return r53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm = r53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm;
    }

    public BigDecimal getR53_non_rate_sens_bnk_of_botswana_cert_total() {
        return r53_non_rate_sens_bnk_of_botswana_cert_total;
    }

    public void setR53_non_rate_sens_bnk_of_botswana_cert_total(BigDecimal r53_non_rate_sens_bnk_of_botswana_cert_total) {
        this.r53_non_rate_sens_bnk_of_botswana_cert_total = r53_non_rate_sens_bnk_of_botswana_cert_total;
    }
}
	