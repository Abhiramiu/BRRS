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
@Table(name = "BRRS_AML_ARCHIVALTABLE_SUMMARY")
public class AML_Archival_Summary_Entity {
     private String	r11_cust_base_deposit;
private BigDecimal	r11_cust_base_no_of_acct;
private BigDecimal	r11_cust_base_tot_dep;
private String	r12_cust_base_deposit;
private BigDecimal	r12_cust_base_no_of_acct;
private BigDecimal	r12_cust_base_tot_dep;
private String	r13_cust_base_deposit;
private BigDecimal	r13_cust_base_no_of_acct;
private BigDecimal	r13_cust_base_tot_dep;
private String	r14_cust_base_deposit;
private BigDecimal	r14_cust_base_no_of_acct;
private BigDecimal	r14_cust_base_tot_dep;
private String	r15_cust_base_deposit;
private BigDecimal	r15_cust_base_no_of_acct;
private BigDecimal	r15_cust_base_tot_dep;
private String	r21_cust_risk_pro_deposit;
private BigDecimal	r21_cust_risk_pro_num_of_cust;
private BigDecimal	r21_cust_risk_pro_value;
private String	r22_cust_risk_pro_deposit;
private BigDecimal	r22_cust_risk_pro_num_of_cust;
private BigDecimal	r22_cust_risk_pro_value;
private String	r23_cust_risk_pro_deposit;
private BigDecimal	r23_cust_risk_pro_num_of_cust;
private BigDecimal	r23_cust_risk_pro_value;
private String	r24_cust_risk_pro_deposit;
private BigDecimal	r24_cust_risk_pro_num_of_cust;
private BigDecimal	r24_cust_risk_pro_value;
private String	r30_b2_cust_deposit;
private BigDecimal	r30_b2_low_risk_no_cust;
private BigDecimal	r30_b2_low_risk_deposit;
private BigDecimal	r30_b2_medi_risk_no_cust;
private BigDecimal	r30_b2_medi_risk_deposit;
private BigDecimal	r30_b2_high_risk_no_cust;
private BigDecimal	r30_b2_high_risk_deposit;
private BigDecimal	r30_b2_tot_no_cust;
private BigDecimal	r30_b2_tot_deposit;
private String	r31_b2_cust_deposit;
private BigDecimal	r31_b2_low_risk_no_cust;
private BigDecimal	r31_b2_low_risk_deposit;
private BigDecimal	r31_b2_medi_risk_no_cust;
private BigDecimal	r31_b2_medi_risk_deposit;
private BigDecimal	r31_b2_high_risk_no_cust;
private BigDecimal	r31_b2_high_risk_deposit;
private BigDecimal	r31_b2_tot_no_cust;
private BigDecimal	r31_b2_tot_deposit;
private String	r32_b2_cust_deposit;
private BigDecimal	r32_b2_low_risk_no_cust;
private BigDecimal	r32_b2_low_risk_deposit;
private BigDecimal	r32_b2_medi_risk_no_cust;
private BigDecimal	r32_b2_medi_risk_deposit;
private BigDecimal	r32_b2_high_risk_no_cust;
private BigDecimal	r32_b2_high_risk_deposit;
private BigDecimal	r32_b2_tot_no_cust;
private BigDecimal	r32_b2_tot_deposit;
private String	r33_b2_cust_deposit;
private BigDecimal	r33_b2_low_risk_no_cust;
private BigDecimal	r33_b2_low_risk_deposit;
private BigDecimal	r33_b2_medi_risk_no_cust;
private BigDecimal	r33_b2_medi_risk_deposit;
private BigDecimal	r33_b2_high_risk_no_cust;
private BigDecimal	r33_b2_high_risk_deposit;
private BigDecimal	r33_b2_tot_no_cust;
private BigDecimal	r33_b2_tot_deposit;
private String	r39_cust_base_cust_deposit;
private BigDecimal	r39_cust_base_no_cust;
private BigDecimal	r39_cust_base_deposits;
private String	r40_cust_base_cust_deposit;
private BigDecimal	r40_cust_base_no_cust;
private BigDecimal	r40_cust_base_deposits;
private String	r41_cust_base_cust_deposit;
private BigDecimal	r41_cust_base_no_cust;
private BigDecimal	r41_cust_base_deposits;
private String	r50_brkdown_typ_of_cust;
private BigDecimal	r50_brkdown_num_of_cust;
private BigDecimal	r50_brkdown_tot_depo;
private String	r51_brkdown_typ_of_cust;
private BigDecimal	r51_brkdown_num_of_cust;
private BigDecimal	r51_brkdown_tot_depo;
private String	r52_brkdown_typ_of_cust;
private BigDecimal	r52_brkdown_num_of_cust;
private BigDecimal	r52_brkdown_tot_depo;
private String	r53_brkdown_typ_of_cust;
private BigDecimal	r53_brkdown_num_of_cust;
private BigDecimal	r53_brkdown_tot_depo;
private String	r54_brkdown_typ_of_cust;
private BigDecimal	r54_brkdown_num_of_cust;
private BigDecimal	r54_brkdown_tot_depo;
private String	r55_brkdown_typ_of_cust;
private BigDecimal	r55_brkdown_num_of_cust;
private BigDecimal	r55_brkdown_tot_depo;
private String	r56_brkdown_typ_of_cust;
private BigDecimal	r56_brkdown_num_of_cust;
private BigDecimal	r56_brkdown_tot_depo;
private String	r57_brkdown_typ_of_cust;
private BigDecimal	r57_brkdown_num_of_cust;
private BigDecimal	r57_brkdown_tot_depo;
private String	r58_brkdown_typ_of_cust;
private BigDecimal	r58_brkdown_num_of_cust;
private BigDecimal	r58_brkdown_tot_depo;
private String	r59_brkdown_typ_of_cust;
private BigDecimal	r59_brkdown_num_of_cust;
private BigDecimal	r59_brkdown_tot_depo;
private String	r60_brkdown_typ_of_cust;
private BigDecimal	r60_brkdown_num_of_cust;
private BigDecimal	r60_brkdown_tot_depo;
private String	r61_brkdown_typ_of_cust;
private BigDecimal	r61_brkdown_num_of_cust;
private BigDecimal	r61_brkdown_tot_depo;
private String	r62_brkdown_typ_of_cust;
private BigDecimal	r62_brkdown_num_of_cust;
private BigDecimal	r62_brkdown_tot_depo;
private String	r63_brkdown_typ_of_cust;
private BigDecimal	r63_brkdown_num_of_cust;
private BigDecimal	r63_brkdown_tot_depo;
private String	r64_brkdown_typ_of_cust;
private BigDecimal	r64_brkdown_num_of_cust;
private BigDecimal	r64_brkdown_tot_depo;
private String	r65_brkdown_typ_of_cust;
private BigDecimal	r65_brkdown_num_of_cust;
private BigDecimal	r65_brkdown_tot_depo;
private String	r66_brkdown_typ_of_cust;
private BigDecimal	r66_brkdown_num_of_cust;
private BigDecimal	r66_brkdown_tot_depo;
private String	r67_brkdown_typ_of_cust;
private BigDecimal	r67_brkdown_num_of_cust;
private BigDecimal	r67_brkdown_tot_depo;
private String	r68_brkdown_typ_of_cust;
private BigDecimal	r68_brkdown_num_of_cust;
private BigDecimal	r68_brkdown_tot_depo;
private String	r69_brkdown_typ_of_cust;
private BigDecimal	r69_brkdown_num_of_cust;
private BigDecimal	r69_brkdown_tot_depo;
private String	r70_brkdown_typ_of_cust;
private BigDecimal	r70_brkdown_num_of_cust;
private BigDecimal	r70_brkdown_tot_depo;
private String	r71_brkdown_typ_of_cust;
private BigDecimal	r71_brkdown_num_of_cust;
private BigDecimal	r71_brkdown_tot_depo;
private String	r72_brkdown_typ_of_cust;
private BigDecimal	r72_brkdown_num_of_cust;
private BigDecimal	r72_brkdown_tot_depo;
private String	r73_brkdown_typ_of_cust;
private BigDecimal	r73_brkdown_num_of_cust;
private BigDecimal	r73_brkdown_tot_depo;
private String	r74_brkdown_typ_of_cust;
private BigDecimal	r74_brkdown_num_of_cust;
private BigDecimal	r74_brkdown_tot_depo;
private String	r75_brkdown_typ_of_cust;
private BigDecimal	r75_brkdown_num_of_cust;
private BigDecimal	r75_brkdown_tot_depo;
private BigDecimal	r82_e1_tot_no_cust;
private BigDecimal	r82_e1_loan_on_bal_expo;
private BigDecimal	r82_e1_deposit;
private BigDecimal	r82_e1_funds_behalf_cust;
private BigDecimal	r82_e1_turnover;
private BigDecimal	r83_e1_tot_no_cust;
private BigDecimal	r83_e1_loan_on_bal_expo;
private BigDecimal	r83_e1_deposit;
private BigDecimal	r83_e1_funds_behalf_cust;
private BigDecimal	r83_e1_turnover;
private BigDecimal	r89_e2_tot_no_cust;
private BigDecimal	r89_e2_loans_bal_expo;
private BigDecimal	r89_e2_deposit;
private BigDecimal	r89_e2_funds_behalf_cust;
private BigDecimal	r89_e2_turnover;
private BigDecimal	r90_e2_tot_no_cust;
private BigDecimal	r90_e2_loans_bal_expo;
private BigDecimal	r90_e2_deposit;
private BigDecimal	r90_e2_funds_behalf_cust;
private BigDecimal	r90_e2_turnover;
private BigDecimal	r96_e3_tot_no_cust;
private BigDecimal	r96_e3_loans_bal_expo;
private BigDecimal	r96_e3_deposit;
private BigDecimal	r96_e3_funds_behalf_cust;
private BigDecimal	r96_e3_turnover;
private BigDecimal	r97_e3_tot_no_cust;
private BigDecimal	r97_e3_loans_bal_expo;
private BigDecimal	r97_e3_deposit;
private BigDecimal	r97_e3_funds_behalf_cust;
private BigDecimal	r97_e3_turnover;
private BigDecimal	r104_f_num_of_cust;
private BigDecimal	r104_f_loans_bal_expo;
private BigDecimal	r104_f_deposit;
private BigDecimal	r104_f_funds_behalf_cust;
private BigDecimal	r104_f_turnover;
private BigDecimal	r105_f_num_of_cust;
private BigDecimal	r105_f_loans_bal_expo;
private BigDecimal	r105_f_deposit;
private BigDecimal	r105_f_funds_behalf_cust;
private BigDecimal	r105_f_turnover;
private String	r111_g1_pay_mech;
private String	r111_g1_pay_mechanisum;
private BigDecimal	r111_g1_num_trans;
private BigDecimal	r111_g1_val_trans;
private String	r112_g1_pay_mech;
private String	r112_g1_pay_mechanisum;
private BigDecimal	r112_g1_num_trans;
private BigDecimal	r112_g1_val_trans;
private String	r113_g1_pay_mech;
private String	r113_g1_pay_mechanisum;
private BigDecimal	r113_g1_num_trans;
private BigDecimal	r113_g1_val_trans;
private String	r114_g1_pay_mech;
private String	r114_g1_pay_mechanisum;
private BigDecimal	r114_g1_num_trans;
private BigDecimal	r114_g1_val_trans;
private String	r115_g1_pay_mech;
private String	r115_g1_pay_mechanisum;
private BigDecimal	r115_g1_num_trans;
private BigDecimal	r115_g1_val_trans;
private String	r116_g1_pay_mech;
private String	r116_g1_pay_mechanisum;
private BigDecimal	r116_g1_num_trans;
private BigDecimal	r116_g1_val_trans;
private String	r117_g1_pay_mech;
private String	r117_g1_pay_mechanisum;
private BigDecimal	r117_g1_num_trans;
private BigDecimal	r117_g1_val_trans;
private String	r118_g1_pay_mech;
private String	r118_g1_pay_mechanisum;
private BigDecimal	r118_g1_num_trans;
private BigDecimal	r118_g1_val_trans;
private String	r119_g1_pay_mech;
private String	r119_g1_pay_mechanisum;
private BigDecimal	r119_g1_num_trans;
private BigDecimal	r119_g1_val_trans;
private String	r120_g1_pay_mech;
private String	r120_g1_pay_mechanisum;
private BigDecimal	r120_g1_num_trans;
private BigDecimal	r120_g1_val_trans;
private String	r121_g1_pay_mech;
private String	r121_g1_pay_mechanisum;
private BigDecimal	r121_g1_num_trans;
private BigDecimal	r121_g1_val_trans;
private String	r122_g1_pay_mech;
private String	r122_g1_pay_mechanisum;
private BigDecimal	r122_g1_num_trans;
private BigDecimal	r122_g1_val_trans;
private String	r123_g1_pay_mech;
private String	r123_g1_pay_mechanisum;
private BigDecimal	r123_g1_num_trans;
private BigDecimal	r123_g1_val_trans;
private String	r124_g1_pay_mech;
private String	r124_g1_pay_mechanisum;
private BigDecimal	r124_g1_num_trans;
private BigDecimal	r124_g1_val_trans;
private String	r125_g1_pay_mech;
private String	r125_g1_pay_mechanisum;
private BigDecimal	r125_g1_num_trans;
private BigDecimal	r125_g1_val_trans;
private String	r126_g1_pay_mech;
private String	r126_g1_pay_mechanisum;
private BigDecimal	r126_g1_num_trans;
private BigDecimal	r126_g1_val_trans;
private String	r127_g1_pay_mech;
private String	r127_g1_pay_mechanisum;
private BigDecimal	r127_g1_num_trans;
private BigDecimal	r127_g1_val_trans;
private String	r128_g1_pay_mech;
private String	r128_g1_pay_mechanisum;
private BigDecimal	r128_g1_num_trans;
private BigDecimal	r128_g1_val_trans;
private String	r135_g2_foreign_exchange;
private String	r135_g2_fore_exchange;
private BigDecimal	r135_g2_val_transac;
private String	r136_g2_fore_exchange;
private BigDecimal	r136_g2_val_transac;
private String	r138_g2_foreign_exchange;
private String	r138_g2_fore_exchange;
private BigDecimal	r138_g2_val_transac;
private String	r139_g2_fore_exchange;
private BigDecimal	r139_g2_val_transac;
private String	r144_h_types;
private BigDecimal	r144_h_amount;
private String	r145_h_types;
private BigDecimal	r145_h_amount;
private String	r146_h_types;
private BigDecimal	r146_h_amount;
private String	r147_h_types;
private BigDecimal	r147_h_amount;
private String	r148_h_types;
private BigDecimal	r148_h_amount;
private String	r153_i_product_serv;
private BigDecimal	r153_i_no_cust;
private BigDecimal	r153_i_outs_bal;
private BigDecimal	r153_i_turnover;
private String	r154_i_product_serv;
private BigDecimal	r154_i_no_cust;
private BigDecimal	r154_i_outs_bal;
private BigDecimal	r154_i_turnover;
private String	r155_i_product_serv;
private BigDecimal	r155_i_no_cust;
private BigDecimal	r155_i_outs_bal;
private BigDecimal	r155_i_turnover;
private String	r161_j_trade_finc_prod;
private BigDecimal	r161_j_num_of_cust;
private BigDecimal	r161_j_commitment_at_jun;
private String	r162_j_trade_finc_prod;
private BigDecimal	r162_j_num_of_cust;
private BigDecimal	r162_j_commitment_at_jun;
private String	r163_j_trade_finc_prod;
private BigDecimal	r163_j_num_of_cust;
private BigDecimal	r163_j_commitment_at_jun;
private String	r164_j_trade_finc_prod;
private BigDecimal	r164_j_num_of_cust;
private BigDecimal	r164_j_commitment_at_jun;
private String	r170_k_pay_mechanism;
private String	r170_k_pay_mech;
private BigDecimal	r170_k_num_of_trans;
private BigDecimal	r170_k_value_of_trans;
private String	r171_k_pay_mech;
private BigDecimal	r171_k_num_of_trans;
private BigDecimal	r171_k_value_of_trans;
private String	r172_k_pay_mechanism;
private String	r172_k_pay_mech;
private BigDecimal	r172_k_num_of_trans;
private BigDecimal	r172_k_value_of_trans;
private String	r179_l_transac_report;
private BigDecimal	r179_l_num_of_transac;
private String	r180_l_transac_report;
private BigDecimal	r180_l_num_of_transac;
private String	r181_l_transac_report;
private BigDecimal	r181_l_num_of_transac;
private String	r187_m_transac_life;
private BigDecimal	r187_m_num_of_transac;
private BigDecimal	r187_m_val_of_transac;
private String	r192_n_transac_life;
private BigDecimal	r192_n_num_of_transac;
private BigDecimal	r192_n_val_of_transac;
private String	r196_o_transac_life;
private BigDecimal	r196_o_num_of_transac;
private BigDecimal	r196_o_val_of_transac;
private String	r201_p_transac_life;
private BigDecimal	r201_p_num_of_transac;
private BigDecimal	r201_p_val_of_transac;
@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id

private Date report_date;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;
public String getR11_cust_base_deposit() {
    return r11_cust_base_deposit;
}
public void setR11_cust_base_deposit(String r11_cust_base_deposit) {
    this.r11_cust_base_deposit = r11_cust_base_deposit;
}
public BigDecimal getR11_cust_base_no_of_acct() {
    return r11_cust_base_no_of_acct;
}
public void setR11_cust_base_no_of_acct(BigDecimal r11_cust_base_no_of_acct) {
    this.r11_cust_base_no_of_acct = r11_cust_base_no_of_acct;
}
public BigDecimal getR11_cust_base_tot_dep() {
    return r11_cust_base_tot_dep;
}
public void setR11_cust_base_tot_dep(BigDecimal r11_cust_base_tot_dep) {
    this.r11_cust_base_tot_dep = r11_cust_base_tot_dep;
}
public String getR12_cust_base_deposit() {
    return r12_cust_base_deposit;
}
public void setR12_cust_base_deposit(String r12_cust_base_deposit) {
    this.r12_cust_base_deposit = r12_cust_base_deposit;
}
public BigDecimal getR12_cust_base_no_of_acct() {
    return r12_cust_base_no_of_acct;
}
public void setR12_cust_base_no_of_acct(BigDecimal r12_cust_base_no_of_acct) {
    this.r12_cust_base_no_of_acct = r12_cust_base_no_of_acct;
}
public BigDecimal getR12_cust_base_tot_dep() {
    return r12_cust_base_tot_dep;
}
public void setR12_cust_base_tot_dep(BigDecimal r12_cust_base_tot_dep) {
    this.r12_cust_base_tot_dep = r12_cust_base_tot_dep;
}
public String getR13_cust_base_deposit() {
    return r13_cust_base_deposit;
}
public void setR13_cust_base_deposit(String r13_cust_base_deposit) {
    this.r13_cust_base_deposit = r13_cust_base_deposit;
}
public BigDecimal getR13_cust_base_no_of_acct() {
    return r13_cust_base_no_of_acct;
}
public void setR13_cust_base_no_of_acct(BigDecimal r13_cust_base_no_of_acct) {
    this.r13_cust_base_no_of_acct = r13_cust_base_no_of_acct;
}
public BigDecimal getR13_cust_base_tot_dep() {
    return r13_cust_base_tot_dep;
}
public void setR13_cust_base_tot_dep(BigDecimal r13_cust_base_tot_dep) {
    this.r13_cust_base_tot_dep = r13_cust_base_tot_dep;
}
public String getR14_cust_base_deposit() {
    return r14_cust_base_deposit;
}
public void setR14_cust_base_deposit(String r14_cust_base_deposit) {
    this.r14_cust_base_deposit = r14_cust_base_deposit;
}
public BigDecimal getR14_cust_base_no_of_acct() {
    return r14_cust_base_no_of_acct;
}
public void setR14_cust_base_no_of_acct(BigDecimal r14_cust_base_no_of_acct) {
    this.r14_cust_base_no_of_acct = r14_cust_base_no_of_acct;
}
public BigDecimal getR14_cust_base_tot_dep() {
    return r14_cust_base_tot_dep;
}
public void setR14_cust_base_tot_dep(BigDecimal r14_cust_base_tot_dep) {
    this.r14_cust_base_tot_dep = r14_cust_base_tot_dep;
}
public String getR15_cust_base_deposit() {
    return r15_cust_base_deposit;
}
public void setR15_cust_base_deposit(String r15_cust_base_deposit) {
    this.r15_cust_base_deposit = r15_cust_base_deposit;
}
public BigDecimal getR15_cust_base_no_of_acct() {
    return r15_cust_base_no_of_acct;
}
public void setR15_cust_base_no_of_acct(BigDecimal r15_cust_base_no_of_acct) {
    this.r15_cust_base_no_of_acct = r15_cust_base_no_of_acct;
}
public BigDecimal getR15_cust_base_tot_dep() {
    return r15_cust_base_tot_dep;
}
public void setR15_cust_base_tot_dep(BigDecimal r15_cust_base_tot_dep) {
    this.r15_cust_base_tot_dep = r15_cust_base_tot_dep;
}
public String getR21_cust_risk_pro_deposit() {
    return r21_cust_risk_pro_deposit;
}
public void setR21_cust_risk_pro_deposit(String r21_cust_risk_pro_deposit) {
    this.r21_cust_risk_pro_deposit = r21_cust_risk_pro_deposit;
}
public BigDecimal getR21_cust_risk_pro_num_of_cust() {
    return r21_cust_risk_pro_num_of_cust;
}
public void setR21_cust_risk_pro_num_of_cust(BigDecimal r21_cust_risk_pro_num_of_cust) {
    this.r21_cust_risk_pro_num_of_cust = r21_cust_risk_pro_num_of_cust;
}
public BigDecimal getR21_cust_risk_pro_value() {
    return r21_cust_risk_pro_value;
}
public void setR21_cust_risk_pro_value(BigDecimal r21_cust_risk_pro_value) {
    this.r21_cust_risk_pro_value = r21_cust_risk_pro_value;
}
public String getR22_cust_risk_pro_deposit() {
    return r22_cust_risk_pro_deposit;
}
public void setR22_cust_risk_pro_deposit(String r22_cust_risk_pro_deposit) {
    this.r22_cust_risk_pro_deposit = r22_cust_risk_pro_deposit;
}
public BigDecimal getR22_cust_risk_pro_num_of_cust() {
    return r22_cust_risk_pro_num_of_cust;
}
public void setR22_cust_risk_pro_num_of_cust(BigDecimal r22_cust_risk_pro_num_of_cust) {
    this.r22_cust_risk_pro_num_of_cust = r22_cust_risk_pro_num_of_cust;
}
public BigDecimal getR22_cust_risk_pro_value() {
    return r22_cust_risk_pro_value;
}
public void setR22_cust_risk_pro_value(BigDecimal r22_cust_risk_pro_value) {
    this.r22_cust_risk_pro_value = r22_cust_risk_pro_value;
}
public String getR23_cust_risk_pro_deposit() {
    return r23_cust_risk_pro_deposit;
}
public void setR23_cust_risk_pro_deposit(String r23_cust_risk_pro_deposit) {
    this.r23_cust_risk_pro_deposit = r23_cust_risk_pro_deposit;
}
public BigDecimal getR23_cust_risk_pro_num_of_cust() {
    return r23_cust_risk_pro_num_of_cust;
}
public void setR23_cust_risk_pro_num_of_cust(BigDecimal r23_cust_risk_pro_num_of_cust) {
    this.r23_cust_risk_pro_num_of_cust = r23_cust_risk_pro_num_of_cust;
}
public BigDecimal getR23_cust_risk_pro_value() {
    return r23_cust_risk_pro_value;
}
public void setR23_cust_risk_pro_value(BigDecimal r23_cust_risk_pro_value) {
    this.r23_cust_risk_pro_value = r23_cust_risk_pro_value;
}
public String getR24_cust_risk_pro_deposit() {
    return r24_cust_risk_pro_deposit;
}
public void setR24_cust_risk_pro_deposit(String r24_cust_risk_pro_deposit) {
    this.r24_cust_risk_pro_deposit = r24_cust_risk_pro_deposit;
}
public BigDecimal getR24_cust_risk_pro_num_of_cust() {
    return r24_cust_risk_pro_num_of_cust;
}
public void setR24_cust_risk_pro_num_of_cust(BigDecimal r24_cust_risk_pro_num_of_cust) {
    this.r24_cust_risk_pro_num_of_cust = r24_cust_risk_pro_num_of_cust;
}
public BigDecimal getR24_cust_risk_pro_value() {
    return r24_cust_risk_pro_value;
}
public void setR24_cust_risk_pro_value(BigDecimal r24_cust_risk_pro_value) {
    this.r24_cust_risk_pro_value = r24_cust_risk_pro_value;
}
public String getR30_b2_cust_deposit() {
    return r30_b2_cust_deposit;
}
public void setR30_b2_cust_deposit(String r30_b2_cust_deposit) {
    this.r30_b2_cust_deposit = r30_b2_cust_deposit;
}
public BigDecimal getR30_b2_low_risk_no_cust() {
    return r30_b2_low_risk_no_cust;
}
public void setR30_b2_low_risk_no_cust(BigDecimal r30_b2_low_risk_no_cust) {
    this.r30_b2_low_risk_no_cust = r30_b2_low_risk_no_cust;
}
public BigDecimal getR30_b2_low_risk_deposit() {
    return r30_b2_low_risk_deposit;
}
public void setR30_b2_low_risk_deposit(BigDecimal r30_b2_low_risk_deposit) {
    this.r30_b2_low_risk_deposit = r30_b2_low_risk_deposit;
}
public BigDecimal getR30_b2_medi_risk_no_cust() {
    return r30_b2_medi_risk_no_cust;
}
public void setR30_b2_medi_risk_no_cust(BigDecimal r30_b2_medi_risk_no_cust) {
    this.r30_b2_medi_risk_no_cust = r30_b2_medi_risk_no_cust;
}
public BigDecimal getR30_b2_medi_risk_deposit() {
    return r30_b2_medi_risk_deposit;
}
public void setR30_b2_medi_risk_deposit(BigDecimal r30_b2_medi_risk_deposit) {
    this.r30_b2_medi_risk_deposit = r30_b2_medi_risk_deposit;
}
public BigDecimal getR30_b2_high_risk_no_cust() {
    return r30_b2_high_risk_no_cust;
}
public void setR30_b2_high_risk_no_cust(BigDecimal r30_b2_high_risk_no_cust) {
    this.r30_b2_high_risk_no_cust = r30_b2_high_risk_no_cust;
}
public BigDecimal getR30_b2_high_risk_deposit() {
    return r30_b2_high_risk_deposit;
}
public void setR30_b2_high_risk_deposit(BigDecimal r30_b2_high_risk_deposit) {
    this.r30_b2_high_risk_deposit = r30_b2_high_risk_deposit;
}
public BigDecimal getR30_b2_tot_no_cust() {
    return r30_b2_tot_no_cust;
}
public void setR30_b2_tot_no_cust(BigDecimal r30_b2_tot_no_cust) {
    this.r30_b2_tot_no_cust = r30_b2_tot_no_cust;
}
public BigDecimal getR30_b2_tot_deposit() {
    return r30_b2_tot_deposit;
}
public void setR30_b2_tot_deposit(BigDecimal r30_b2_tot_deposit) {
    this.r30_b2_tot_deposit = r30_b2_tot_deposit;
}
public String getR31_b2_cust_deposit() {
    return r31_b2_cust_deposit;
}
public void setR31_b2_cust_deposit(String r31_b2_cust_deposit) {
    this.r31_b2_cust_deposit = r31_b2_cust_deposit;
}
public BigDecimal getR31_b2_low_risk_no_cust() {
    return r31_b2_low_risk_no_cust;
}
public void setR31_b2_low_risk_no_cust(BigDecimal r31_b2_low_risk_no_cust) {
    this.r31_b2_low_risk_no_cust = r31_b2_low_risk_no_cust;
}
public BigDecimal getR31_b2_low_risk_deposit() {
    return r31_b2_low_risk_deposit;
}
public void setR31_b2_low_risk_deposit(BigDecimal r31_b2_low_risk_deposit) {
    this.r31_b2_low_risk_deposit = r31_b2_low_risk_deposit;
}
public BigDecimal getR31_b2_medi_risk_no_cust() {
    return r31_b2_medi_risk_no_cust;
}
public void setR31_b2_medi_risk_no_cust(BigDecimal r31_b2_medi_risk_no_cust) {
    this.r31_b2_medi_risk_no_cust = r31_b2_medi_risk_no_cust;
}
public BigDecimal getR31_b2_medi_risk_deposit() {
    return r31_b2_medi_risk_deposit;
}
public void setR31_b2_medi_risk_deposit(BigDecimal r31_b2_medi_risk_deposit) {
    this.r31_b2_medi_risk_deposit = r31_b2_medi_risk_deposit;
}
public BigDecimal getR31_b2_high_risk_no_cust() {
    return r31_b2_high_risk_no_cust;
}
public void setR31_b2_high_risk_no_cust(BigDecimal r31_b2_high_risk_no_cust) {
    this.r31_b2_high_risk_no_cust = r31_b2_high_risk_no_cust;
}
public BigDecimal getR31_b2_high_risk_deposit() {
    return r31_b2_high_risk_deposit;
}
public void setR31_b2_high_risk_deposit(BigDecimal r31_b2_high_risk_deposit) {
    this.r31_b2_high_risk_deposit = r31_b2_high_risk_deposit;
}
public BigDecimal getR31_b2_tot_no_cust() {
    return r31_b2_tot_no_cust;
}
public void setR31_b2_tot_no_cust(BigDecimal r31_b2_tot_no_cust) {
    this.r31_b2_tot_no_cust = r31_b2_tot_no_cust;
}
public BigDecimal getR31_b2_tot_deposit() {
    return r31_b2_tot_deposit;
}
public void setR31_b2_tot_deposit(BigDecimal r31_b2_tot_deposit) {
    this.r31_b2_tot_deposit = r31_b2_tot_deposit;
}
public String getR32_b2_cust_deposit() {
    return r32_b2_cust_deposit;
}
public void setR32_b2_cust_deposit(String r32_b2_cust_deposit) {
    this.r32_b2_cust_deposit = r32_b2_cust_deposit;
}
public BigDecimal getR32_b2_low_risk_no_cust() {
    return r32_b2_low_risk_no_cust;
}
public void setR32_b2_low_risk_no_cust(BigDecimal r32_b2_low_risk_no_cust) {
    this.r32_b2_low_risk_no_cust = r32_b2_low_risk_no_cust;
}
public BigDecimal getR32_b2_low_risk_deposit() {
    return r32_b2_low_risk_deposit;
}
public void setR32_b2_low_risk_deposit(BigDecimal r32_b2_low_risk_deposit) {
    this.r32_b2_low_risk_deposit = r32_b2_low_risk_deposit;
}
public BigDecimal getR32_b2_medi_risk_no_cust() {
    return r32_b2_medi_risk_no_cust;
}
public void setR32_b2_medi_risk_no_cust(BigDecimal r32_b2_medi_risk_no_cust) {
    this.r32_b2_medi_risk_no_cust = r32_b2_medi_risk_no_cust;
}
public BigDecimal getR32_b2_medi_risk_deposit() {
    return r32_b2_medi_risk_deposit;
}
public void setR32_b2_medi_risk_deposit(BigDecimal r32_b2_medi_risk_deposit) {
    this.r32_b2_medi_risk_deposit = r32_b2_medi_risk_deposit;
}
public BigDecimal getR32_b2_high_risk_no_cust() {
    return r32_b2_high_risk_no_cust;
}
public void setR32_b2_high_risk_no_cust(BigDecimal r32_b2_high_risk_no_cust) {
    this.r32_b2_high_risk_no_cust = r32_b2_high_risk_no_cust;
}
public BigDecimal getR32_b2_high_risk_deposit() {
    return r32_b2_high_risk_deposit;
}
public void setR32_b2_high_risk_deposit(BigDecimal r32_b2_high_risk_deposit) {
    this.r32_b2_high_risk_deposit = r32_b2_high_risk_deposit;
}
public BigDecimal getR32_b2_tot_no_cust() {
    return r32_b2_tot_no_cust;
}
public void setR32_b2_tot_no_cust(BigDecimal r32_b2_tot_no_cust) {
    this.r32_b2_tot_no_cust = r32_b2_tot_no_cust;
}
public BigDecimal getR32_b2_tot_deposit() {
    return r32_b2_tot_deposit;
}
public void setR32_b2_tot_deposit(BigDecimal r32_b2_tot_deposit) {
    this.r32_b2_tot_deposit = r32_b2_tot_deposit;
}
public String getR33_b2_cust_deposit() {
    return r33_b2_cust_deposit;
}
public void setR33_b2_cust_deposit(String r33_b2_cust_deposit) {
    this.r33_b2_cust_deposit = r33_b2_cust_deposit;
}
public BigDecimal getR33_b2_low_risk_no_cust() {
    return r33_b2_low_risk_no_cust;
}
public void setR33_b2_low_risk_no_cust(BigDecimal r33_b2_low_risk_no_cust) {
    this.r33_b2_low_risk_no_cust = r33_b2_low_risk_no_cust;
}
public BigDecimal getR33_b2_low_risk_deposit() {
    return r33_b2_low_risk_deposit;
}
public void setR33_b2_low_risk_deposit(BigDecimal r33_b2_low_risk_deposit) {
    this.r33_b2_low_risk_deposit = r33_b2_low_risk_deposit;
}
public BigDecimal getR33_b2_medi_risk_no_cust() {
    return r33_b2_medi_risk_no_cust;
}
public void setR33_b2_medi_risk_no_cust(BigDecimal r33_b2_medi_risk_no_cust) {
    this.r33_b2_medi_risk_no_cust = r33_b2_medi_risk_no_cust;
}
public BigDecimal getR33_b2_medi_risk_deposit() {
    return r33_b2_medi_risk_deposit;
}
public void setR33_b2_medi_risk_deposit(BigDecimal r33_b2_medi_risk_deposit) {
    this.r33_b2_medi_risk_deposit = r33_b2_medi_risk_deposit;
}
public BigDecimal getR33_b2_high_risk_no_cust() {
    return r33_b2_high_risk_no_cust;
}
public void setR33_b2_high_risk_no_cust(BigDecimal r33_b2_high_risk_no_cust) {
    this.r33_b2_high_risk_no_cust = r33_b2_high_risk_no_cust;
}
public BigDecimal getR33_b2_high_risk_deposit() {
    return r33_b2_high_risk_deposit;
}
public void setR33_b2_high_risk_deposit(BigDecimal r33_b2_high_risk_deposit) {
    this.r33_b2_high_risk_deposit = r33_b2_high_risk_deposit;
}
public BigDecimal getR33_b2_tot_no_cust() {
    return r33_b2_tot_no_cust;
}
public void setR33_b2_tot_no_cust(BigDecimal r33_b2_tot_no_cust) {
    this.r33_b2_tot_no_cust = r33_b2_tot_no_cust;
}
public BigDecimal getR33_b2_tot_deposit() {
    return r33_b2_tot_deposit;
}
public void setR33_b2_tot_deposit(BigDecimal r33_b2_tot_deposit) {
    this.r33_b2_tot_deposit = r33_b2_tot_deposit;
}
public String getR39_cust_base_cust_deposit() {
    return r39_cust_base_cust_deposit;
}
public void setR39_cust_base_cust_deposit(String r39_cust_base_cust_deposit) {
    this.r39_cust_base_cust_deposit = r39_cust_base_cust_deposit;
}
public BigDecimal getR39_cust_base_no_cust() {
    return r39_cust_base_no_cust;
}
public void setR39_cust_base_no_cust(BigDecimal r39_cust_base_no_cust) {
    this.r39_cust_base_no_cust = r39_cust_base_no_cust;
}
public BigDecimal getR39_cust_base_deposits() {
    return r39_cust_base_deposits;
}
public void setR39_cust_base_deposits(BigDecimal r39_cust_base_deposits) {
    this.r39_cust_base_deposits = r39_cust_base_deposits;
}
public String getR40_cust_base_cust_deposit() {
    return r40_cust_base_cust_deposit;
}
public void setR40_cust_base_cust_deposit(String r40_cust_base_cust_deposit) {
    this.r40_cust_base_cust_deposit = r40_cust_base_cust_deposit;
}
public BigDecimal getR40_cust_base_no_cust() {
    return r40_cust_base_no_cust;
}
public void setR40_cust_base_no_cust(BigDecimal r40_cust_base_no_cust) {
    this.r40_cust_base_no_cust = r40_cust_base_no_cust;
}
public BigDecimal getR40_cust_base_deposits() {
    return r40_cust_base_deposits;
}
public void setR40_cust_base_deposits(BigDecimal r40_cust_base_deposits) {
    this.r40_cust_base_deposits = r40_cust_base_deposits;
}
public String getR41_cust_base_cust_deposit() {
    return r41_cust_base_cust_deposit;
}
public void setR41_cust_base_cust_deposit(String r41_cust_base_cust_deposit) {
    this.r41_cust_base_cust_deposit = r41_cust_base_cust_deposit;
}
public BigDecimal getR41_cust_base_no_cust() {
    return r41_cust_base_no_cust;
}
public void setR41_cust_base_no_cust(BigDecimal r41_cust_base_no_cust) {
    this.r41_cust_base_no_cust = r41_cust_base_no_cust;
}
public BigDecimal getR41_cust_base_deposits() {
    return r41_cust_base_deposits;
}
public void setR41_cust_base_deposits(BigDecimal r41_cust_base_deposits) {
    this.r41_cust_base_deposits = r41_cust_base_deposits;
}
public String getR50_brkdown_typ_of_cust() {
    return r50_brkdown_typ_of_cust;
}
public void setR50_brkdown_typ_of_cust(String r50_brkdown_typ_of_cust) {
    this.r50_brkdown_typ_of_cust = r50_brkdown_typ_of_cust;
}
public BigDecimal getR50_brkdown_num_of_cust() {
    return r50_brkdown_num_of_cust;
}
public void setR50_brkdown_num_of_cust(BigDecimal r50_brkdown_num_of_cust) {
    this.r50_brkdown_num_of_cust = r50_brkdown_num_of_cust;
}
public BigDecimal getR50_brkdown_tot_depo() {
    return r50_brkdown_tot_depo;
}
public void setR50_brkdown_tot_depo(BigDecimal r50_brkdown_tot_depo) {
    this.r50_brkdown_tot_depo = r50_brkdown_tot_depo;
}
public String getR51_brkdown_typ_of_cust() {
    return r51_brkdown_typ_of_cust;
}
public void setR51_brkdown_typ_of_cust(String r51_brkdown_typ_of_cust) {
    this.r51_brkdown_typ_of_cust = r51_brkdown_typ_of_cust;
}
public BigDecimal getR51_brkdown_num_of_cust() {
    return r51_brkdown_num_of_cust;
}
public void setR51_brkdown_num_of_cust(BigDecimal r51_brkdown_num_of_cust) {
    this.r51_brkdown_num_of_cust = r51_brkdown_num_of_cust;
}
public BigDecimal getR51_brkdown_tot_depo() {
    return r51_brkdown_tot_depo;
}
public void setR51_brkdown_tot_depo(BigDecimal r51_brkdown_tot_depo) {
    this.r51_brkdown_tot_depo = r51_brkdown_tot_depo;
}
public String getR52_brkdown_typ_of_cust() {
    return r52_brkdown_typ_of_cust;
}
public void setR52_brkdown_typ_of_cust(String r52_brkdown_typ_of_cust) {
    this.r52_brkdown_typ_of_cust = r52_brkdown_typ_of_cust;
}
public BigDecimal getR52_brkdown_num_of_cust() {
    return r52_brkdown_num_of_cust;
}
public void setR52_brkdown_num_of_cust(BigDecimal r52_brkdown_num_of_cust) {
    this.r52_brkdown_num_of_cust = r52_brkdown_num_of_cust;
}
public BigDecimal getR52_brkdown_tot_depo() {
    return r52_brkdown_tot_depo;
}
public void setR52_brkdown_tot_depo(BigDecimal r52_brkdown_tot_depo) {
    this.r52_brkdown_tot_depo = r52_brkdown_tot_depo;
}
public String getR53_brkdown_typ_of_cust() {
    return r53_brkdown_typ_of_cust;
}
public void setR53_brkdown_typ_of_cust(String r53_brkdown_typ_of_cust) {
    this.r53_brkdown_typ_of_cust = r53_brkdown_typ_of_cust;
}
public BigDecimal getR53_brkdown_num_of_cust() {
    return r53_brkdown_num_of_cust;
}
public void setR53_brkdown_num_of_cust(BigDecimal r53_brkdown_num_of_cust) {
    this.r53_brkdown_num_of_cust = r53_brkdown_num_of_cust;
}
public BigDecimal getR53_brkdown_tot_depo() {
    return r53_brkdown_tot_depo;
}
public void setR53_brkdown_tot_depo(BigDecimal r53_brkdown_tot_depo) {
    this.r53_brkdown_tot_depo = r53_brkdown_tot_depo;
}
public String getR54_brkdown_typ_of_cust() {
    return r54_brkdown_typ_of_cust;
}
public void setR54_brkdown_typ_of_cust(String r54_brkdown_typ_of_cust) {
    this.r54_brkdown_typ_of_cust = r54_brkdown_typ_of_cust;
}
public BigDecimal getR54_brkdown_num_of_cust() {
    return r54_brkdown_num_of_cust;
}
public void setR54_brkdown_num_of_cust(BigDecimal r54_brkdown_num_of_cust) {
    this.r54_brkdown_num_of_cust = r54_brkdown_num_of_cust;
}
public BigDecimal getR54_brkdown_tot_depo() {
    return r54_brkdown_tot_depo;
}
public void setR54_brkdown_tot_depo(BigDecimal r54_brkdown_tot_depo) {
    this.r54_brkdown_tot_depo = r54_brkdown_tot_depo;
}
public String getR55_brkdown_typ_of_cust() {
    return r55_brkdown_typ_of_cust;
}
public void setR55_brkdown_typ_of_cust(String r55_brkdown_typ_of_cust) {
    this.r55_brkdown_typ_of_cust = r55_brkdown_typ_of_cust;
}
public BigDecimal getR55_brkdown_num_of_cust() {
    return r55_brkdown_num_of_cust;
}
public void setR55_brkdown_num_of_cust(BigDecimal r55_brkdown_num_of_cust) {
    this.r55_brkdown_num_of_cust = r55_brkdown_num_of_cust;
}
public BigDecimal getR55_brkdown_tot_depo() {
    return r55_brkdown_tot_depo;
}
public void setR55_brkdown_tot_depo(BigDecimal r55_brkdown_tot_depo) {
    this.r55_brkdown_tot_depo = r55_brkdown_tot_depo;
}
public String getR56_brkdown_typ_of_cust() {
    return r56_brkdown_typ_of_cust;
}
public void setR56_brkdown_typ_of_cust(String r56_brkdown_typ_of_cust) {
    this.r56_brkdown_typ_of_cust = r56_brkdown_typ_of_cust;
}
public BigDecimal getR56_brkdown_num_of_cust() {
    return r56_brkdown_num_of_cust;
}
public void setR56_brkdown_num_of_cust(BigDecimal r56_brkdown_num_of_cust) {
    this.r56_brkdown_num_of_cust = r56_brkdown_num_of_cust;
}
public BigDecimal getR56_brkdown_tot_depo() {
    return r56_brkdown_tot_depo;
}
public void setR56_brkdown_tot_depo(BigDecimal r56_brkdown_tot_depo) {
    this.r56_brkdown_tot_depo = r56_brkdown_tot_depo;
}
public String getR57_brkdown_typ_of_cust() {
    return r57_brkdown_typ_of_cust;
}
public void setR57_brkdown_typ_of_cust(String r57_brkdown_typ_of_cust) {
    this.r57_brkdown_typ_of_cust = r57_brkdown_typ_of_cust;
}
public BigDecimal getR57_brkdown_num_of_cust() {
    return r57_brkdown_num_of_cust;
}
public void setR57_brkdown_num_of_cust(BigDecimal r57_brkdown_num_of_cust) {
    this.r57_brkdown_num_of_cust = r57_brkdown_num_of_cust;
}
public BigDecimal getR57_brkdown_tot_depo() {
    return r57_brkdown_tot_depo;
}
public void setR57_brkdown_tot_depo(BigDecimal r57_brkdown_tot_depo) {
    this.r57_brkdown_tot_depo = r57_brkdown_tot_depo;
}
public String getR58_brkdown_typ_of_cust() {
    return r58_brkdown_typ_of_cust;
}
public void setR58_brkdown_typ_of_cust(String r58_brkdown_typ_of_cust) {
    this.r58_brkdown_typ_of_cust = r58_brkdown_typ_of_cust;
}
public BigDecimal getR58_brkdown_num_of_cust() {
    return r58_brkdown_num_of_cust;
}
public void setR58_brkdown_num_of_cust(BigDecimal r58_brkdown_num_of_cust) {
    this.r58_brkdown_num_of_cust = r58_brkdown_num_of_cust;
}
public BigDecimal getR58_brkdown_tot_depo() {
    return r58_brkdown_tot_depo;
}
public void setR58_brkdown_tot_depo(BigDecimal r58_brkdown_tot_depo) {
    this.r58_brkdown_tot_depo = r58_brkdown_tot_depo;
}
public String getR59_brkdown_typ_of_cust() {
    return r59_brkdown_typ_of_cust;
}
public void setR59_brkdown_typ_of_cust(String r59_brkdown_typ_of_cust) {
    this.r59_brkdown_typ_of_cust = r59_brkdown_typ_of_cust;
}
public BigDecimal getR59_brkdown_num_of_cust() {
    return r59_brkdown_num_of_cust;
}
public void setR59_brkdown_num_of_cust(BigDecimal r59_brkdown_num_of_cust) {
    this.r59_brkdown_num_of_cust = r59_brkdown_num_of_cust;
}
public BigDecimal getR59_brkdown_tot_depo() {
    return r59_brkdown_tot_depo;
}
public void setR59_brkdown_tot_depo(BigDecimal r59_brkdown_tot_depo) {
    this.r59_brkdown_tot_depo = r59_brkdown_tot_depo;
}
public String getR60_brkdown_typ_of_cust() {
    return r60_brkdown_typ_of_cust;
}
public void setR60_brkdown_typ_of_cust(String r60_brkdown_typ_of_cust) {
    this.r60_brkdown_typ_of_cust = r60_brkdown_typ_of_cust;
}
public BigDecimal getR60_brkdown_num_of_cust() {
    return r60_brkdown_num_of_cust;
}
public void setR60_brkdown_num_of_cust(BigDecimal r60_brkdown_num_of_cust) {
    this.r60_brkdown_num_of_cust = r60_brkdown_num_of_cust;
}
public BigDecimal getR60_brkdown_tot_depo() {
    return r60_brkdown_tot_depo;
}
public void setR60_brkdown_tot_depo(BigDecimal r60_brkdown_tot_depo) {
    this.r60_brkdown_tot_depo = r60_brkdown_tot_depo;
}
public String getR61_brkdown_typ_of_cust() {
    return r61_brkdown_typ_of_cust;
}
public void setR61_brkdown_typ_of_cust(String r61_brkdown_typ_of_cust) {
    this.r61_brkdown_typ_of_cust = r61_brkdown_typ_of_cust;
}
public BigDecimal getR61_brkdown_num_of_cust() {
    return r61_brkdown_num_of_cust;
}
public void setR61_brkdown_num_of_cust(BigDecimal r61_brkdown_num_of_cust) {
    this.r61_brkdown_num_of_cust = r61_brkdown_num_of_cust;
}
public BigDecimal getR61_brkdown_tot_depo() {
    return r61_brkdown_tot_depo;
}
public void setR61_brkdown_tot_depo(BigDecimal r61_brkdown_tot_depo) {
    this.r61_brkdown_tot_depo = r61_brkdown_tot_depo;
}
public String getR62_brkdown_typ_of_cust() {
    return r62_brkdown_typ_of_cust;
}
public void setR62_brkdown_typ_of_cust(String r62_brkdown_typ_of_cust) {
    this.r62_brkdown_typ_of_cust = r62_brkdown_typ_of_cust;
}
public BigDecimal getR62_brkdown_num_of_cust() {
    return r62_brkdown_num_of_cust;
}
public void setR62_brkdown_num_of_cust(BigDecimal r62_brkdown_num_of_cust) {
    this.r62_brkdown_num_of_cust = r62_brkdown_num_of_cust;
}
public BigDecimal getR62_brkdown_tot_depo() {
    return r62_brkdown_tot_depo;
}
public void setR62_brkdown_tot_depo(BigDecimal r62_brkdown_tot_depo) {
    this.r62_brkdown_tot_depo = r62_brkdown_tot_depo;
}
public String getR63_brkdown_typ_of_cust() {
    return r63_brkdown_typ_of_cust;
}
public void setR63_brkdown_typ_of_cust(String r63_brkdown_typ_of_cust) {
    this.r63_brkdown_typ_of_cust = r63_brkdown_typ_of_cust;
}
public BigDecimal getR63_brkdown_num_of_cust() {
    return r63_brkdown_num_of_cust;
}
public void setR63_brkdown_num_of_cust(BigDecimal r63_brkdown_num_of_cust) {
    this.r63_brkdown_num_of_cust = r63_brkdown_num_of_cust;
}
public BigDecimal getR63_brkdown_tot_depo() {
    return r63_brkdown_tot_depo;
}
public void setR63_brkdown_tot_depo(BigDecimal r63_brkdown_tot_depo) {
    this.r63_brkdown_tot_depo = r63_brkdown_tot_depo;
}
public String getR64_brkdown_typ_of_cust() {
    return r64_brkdown_typ_of_cust;
}
public void setR64_brkdown_typ_of_cust(String r64_brkdown_typ_of_cust) {
    this.r64_brkdown_typ_of_cust = r64_brkdown_typ_of_cust;
}
public BigDecimal getR64_brkdown_num_of_cust() {
    return r64_brkdown_num_of_cust;
}
public void setR64_brkdown_num_of_cust(BigDecimal r64_brkdown_num_of_cust) {
    this.r64_brkdown_num_of_cust = r64_brkdown_num_of_cust;
}
public BigDecimal getR64_brkdown_tot_depo() {
    return r64_brkdown_tot_depo;
}
public void setR64_brkdown_tot_depo(BigDecimal r64_brkdown_tot_depo) {
    this.r64_brkdown_tot_depo = r64_brkdown_tot_depo;
}
public String getR65_brkdown_typ_of_cust() {
    return r65_brkdown_typ_of_cust;
}
public void setR65_brkdown_typ_of_cust(String r65_brkdown_typ_of_cust) {
    this.r65_brkdown_typ_of_cust = r65_brkdown_typ_of_cust;
}
public BigDecimal getR65_brkdown_num_of_cust() {
    return r65_brkdown_num_of_cust;
}
public void setR65_brkdown_num_of_cust(BigDecimal r65_brkdown_num_of_cust) {
    this.r65_brkdown_num_of_cust = r65_brkdown_num_of_cust;
}
public BigDecimal getR65_brkdown_tot_depo() {
    return r65_brkdown_tot_depo;
}
public void setR65_brkdown_tot_depo(BigDecimal r65_brkdown_tot_depo) {
    this.r65_brkdown_tot_depo = r65_brkdown_tot_depo;
}
public String getR66_brkdown_typ_of_cust() {
    return r66_brkdown_typ_of_cust;
}
public void setR66_brkdown_typ_of_cust(String r66_brkdown_typ_of_cust) {
    this.r66_brkdown_typ_of_cust = r66_brkdown_typ_of_cust;
}
public BigDecimal getR66_brkdown_num_of_cust() {
    return r66_brkdown_num_of_cust;
}
public void setR66_brkdown_num_of_cust(BigDecimal r66_brkdown_num_of_cust) {
    this.r66_brkdown_num_of_cust = r66_brkdown_num_of_cust;
}
public BigDecimal getR66_brkdown_tot_depo() {
    return r66_brkdown_tot_depo;
}
public void setR66_brkdown_tot_depo(BigDecimal r66_brkdown_tot_depo) {
    this.r66_brkdown_tot_depo = r66_brkdown_tot_depo;
}
public String getR67_brkdown_typ_of_cust() {
    return r67_brkdown_typ_of_cust;
}
public void setR67_brkdown_typ_of_cust(String r67_brkdown_typ_of_cust) {
    this.r67_brkdown_typ_of_cust = r67_brkdown_typ_of_cust;
}
public BigDecimal getR67_brkdown_num_of_cust() {
    return r67_brkdown_num_of_cust;
}
public void setR67_brkdown_num_of_cust(BigDecimal r67_brkdown_num_of_cust) {
    this.r67_brkdown_num_of_cust = r67_brkdown_num_of_cust;
}
public BigDecimal getR67_brkdown_tot_depo() {
    return r67_brkdown_tot_depo;
}
public void setR67_brkdown_tot_depo(BigDecimal r67_brkdown_tot_depo) {
    this.r67_brkdown_tot_depo = r67_brkdown_tot_depo;
}
public String getR68_brkdown_typ_of_cust() {
    return r68_brkdown_typ_of_cust;
}
public void setR68_brkdown_typ_of_cust(String r68_brkdown_typ_of_cust) {
    this.r68_brkdown_typ_of_cust = r68_brkdown_typ_of_cust;
}
public BigDecimal getR68_brkdown_num_of_cust() {
    return r68_brkdown_num_of_cust;
}
public void setR68_brkdown_num_of_cust(BigDecimal r68_brkdown_num_of_cust) {
    this.r68_brkdown_num_of_cust = r68_brkdown_num_of_cust;
}
public BigDecimal getR68_brkdown_tot_depo() {
    return r68_brkdown_tot_depo;
}
public void setR68_brkdown_tot_depo(BigDecimal r68_brkdown_tot_depo) {
    this.r68_brkdown_tot_depo = r68_brkdown_tot_depo;
}
public String getR69_brkdown_typ_of_cust() {
    return r69_brkdown_typ_of_cust;
}
public void setR69_brkdown_typ_of_cust(String r69_brkdown_typ_of_cust) {
    this.r69_brkdown_typ_of_cust = r69_brkdown_typ_of_cust;
}
public BigDecimal getR69_brkdown_num_of_cust() {
    return r69_brkdown_num_of_cust;
}
public void setR69_brkdown_num_of_cust(BigDecimal r69_brkdown_num_of_cust) {
    this.r69_brkdown_num_of_cust = r69_brkdown_num_of_cust;
}
public BigDecimal getR69_brkdown_tot_depo() {
    return r69_brkdown_tot_depo;
}
public void setR69_brkdown_tot_depo(BigDecimal r69_brkdown_tot_depo) {
    this.r69_brkdown_tot_depo = r69_brkdown_tot_depo;
}
public String getR70_brkdown_typ_of_cust() {
    return r70_brkdown_typ_of_cust;
}
public void setR70_brkdown_typ_of_cust(String r70_brkdown_typ_of_cust) {
    this.r70_brkdown_typ_of_cust = r70_brkdown_typ_of_cust;
}
public BigDecimal getR70_brkdown_num_of_cust() {
    return r70_brkdown_num_of_cust;
}
public void setR70_brkdown_num_of_cust(BigDecimal r70_brkdown_num_of_cust) {
    this.r70_brkdown_num_of_cust = r70_brkdown_num_of_cust;
}
public BigDecimal getR70_brkdown_tot_depo() {
    return r70_brkdown_tot_depo;
}
public void setR70_brkdown_tot_depo(BigDecimal r70_brkdown_tot_depo) {
    this.r70_brkdown_tot_depo = r70_brkdown_tot_depo;
}
public String getR71_brkdown_typ_of_cust() {
    return r71_brkdown_typ_of_cust;
}
public void setR71_brkdown_typ_of_cust(String r71_brkdown_typ_of_cust) {
    this.r71_brkdown_typ_of_cust = r71_brkdown_typ_of_cust;
}
public BigDecimal getR71_brkdown_num_of_cust() {
    return r71_brkdown_num_of_cust;
}
public void setR71_brkdown_num_of_cust(BigDecimal r71_brkdown_num_of_cust) {
    this.r71_brkdown_num_of_cust = r71_brkdown_num_of_cust;
}
public BigDecimal getR71_brkdown_tot_depo() {
    return r71_brkdown_tot_depo;
}
public void setR71_brkdown_tot_depo(BigDecimal r71_brkdown_tot_depo) {
    this.r71_brkdown_tot_depo = r71_brkdown_tot_depo;
}
public String getR72_brkdown_typ_of_cust() {
    return r72_brkdown_typ_of_cust;
}
public void setR72_brkdown_typ_of_cust(String r72_brkdown_typ_of_cust) {
    this.r72_brkdown_typ_of_cust = r72_brkdown_typ_of_cust;
}
public BigDecimal getR72_brkdown_num_of_cust() {
    return r72_brkdown_num_of_cust;
}
public void setR72_brkdown_num_of_cust(BigDecimal r72_brkdown_num_of_cust) {
    this.r72_brkdown_num_of_cust = r72_brkdown_num_of_cust;
}
public BigDecimal getR72_brkdown_tot_depo() {
    return r72_brkdown_tot_depo;
}
public void setR72_brkdown_tot_depo(BigDecimal r72_brkdown_tot_depo) {
    this.r72_brkdown_tot_depo = r72_brkdown_tot_depo;
}
public String getR73_brkdown_typ_of_cust() {
    return r73_brkdown_typ_of_cust;
}
public void setR73_brkdown_typ_of_cust(String r73_brkdown_typ_of_cust) {
    this.r73_brkdown_typ_of_cust = r73_brkdown_typ_of_cust;
}
public BigDecimal getR73_brkdown_num_of_cust() {
    return r73_brkdown_num_of_cust;
}
public void setR73_brkdown_num_of_cust(BigDecimal r73_brkdown_num_of_cust) {
    this.r73_brkdown_num_of_cust = r73_brkdown_num_of_cust;
}
public BigDecimal getR73_brkdown_tot_depo() {
    return r73_brkdown_tot_depo;
}
public void setR73_brkdown_tot_depo(BigDecimal r73_brkdown_tot_depo) {
    this.r73_brkdown_tot_depo = r73_brkdown_tot_depo;
}
public String getR74_brkdown_typ_of_cust() {
    return r74_brkdown_typ_of_cust;
}
public void setR74_brkdown_typ_of_cust(String r74_brkdown_typ_of_cust) {
    this.r74_brkdown_typ_of_cust = r74_brkdown_typ_of_cust;
}
public BigDecimal getR74_brkdown_num_of_cust() {
    return r74_brkdown_num_of_cust;
}
public void setR74_brkdown_num_of_cust(BigDecimal r74_brkdown_num_of_cust) {
    this.r74_brkdown_num_of_cust = r74_brkdown_num_of_cust;
}
public BigDecimal getR74_brkdown_tot_depo() {
    return r74_brkdown_tot_depo;
}
public void setR74_brkdown_tot_depo(BigDecimal r74_brkdown_tot_depo) {
    this.r74_brkdown_tot_depo = r74_brkdown_tot_depo;
}
public String getR75_brkdown_typ_of_cust() {
    return r75_brkdown_typ_of_cust;
}
public void setR75_brkdown_typ_of_cust(String r75_brkdown_typ_of_cust) {
    this.r75_brkdown_typ_of_cust = r75_brkdown_typ_of_cust;
}
public BigDecimal getR75_brkdown_num_of_cust() {
    return r75_brkdown_num_of_cust;
}
public void setR75_brkdown_num_of_cust(BigDecimal r75_brkdown_num_of_cust) {
    this.r75_brkdown_num_of_cust = r75_brkdown_num_of_cust;
}
public BigDecimal getR75_brkdown_tot_depo() {
    return r75_brkdown_tot_depo;
}
public void setR75_brkdown_tot_depo(BigDecimal r75_brkdown_tot_depo) {
    this.r75_brkdown_tot_depo = r75_brkdown_tot_depo;
}
public BigDecimal getR82_e1_tot_no_cust() {
    return r82_e1_tot_no_cust;
}
public void setR82_e1_tot_no_cust(BigDecimal r82_e1_tot_no_cust) {
    this.r82_e1_tot_no_cust = r82_e1_tot_no_cust;
}
public BigDecimal getR82_e1_loan_on_bal_expo() {
    return r82_e1_loan_on_bal_expo;
}
public void setR82_e1_loan_on_bal_expo(BigDecimal r82_e1_loan_on_bal_expo) {
    this.r82_e1_loan_on_bal_expo = r82_e1_loan_on_bal_expo;
}
public BigDecimal getR82_e1_deposit() {
    return r82_e1_deposit;
}
public void setR82_e1_deposit(BigDecimal r82_e1_deposit) {
    this.r82_e1_deposit = r82_e1_deposit;
}
public BigDecimal getR82_e1_funds_behalf_cust() {
    return r82_e1_funds_behalf_cust;
}
public void setR82_e1_funds_behalf_cust(BigDecimal r82_e1_funds_behalf_cust) {
    this.r82_e1_funds_behalf_cust = r82_e1_funds_behalf_cust;
}
public BigDecimal getR82_e1_turnover() {
    return r82_e1_turnover;
}
public void setR82_e1_turnover(BigDecimal r82_e1_turnover) {
    this.r82_e1_turnover = r82_e1_turnover;
}
public BigDecimal getR83_e1_tot_no_cust() {
    return r83_e1_tot_no_cust;
}
public void setR83_e1_tot_no_cust(BigDecimal r83_e1_tot_no_cust) {
    this.r83_e1_tot_no_cust = r83_e1_tot_no_cust;
}
public BigDecimal getR83_e1_loan_on_bal_expo() {
    return r83_e1_loan_on_bal_expo;
}
public void setR83_e1_loan_on_bal_expo(BigDecimal r83_e1_loan_on_bal_expo) {
    this.r83_e1_loan_on_bal_expo = r83_e1_loan_on_bal_expo;
}
public BigDecimal getR83_e1_deposit() {
    return r83_e1_deposit;
}
public void setR83_e1_deposit(BigDecimal r83_e1_deposit) {
    this.r83_e1_deposit = r83_e1_deposit;
}
public BigDecimal getR83_e1_funds_behalf_cust() {
    return r83_e1_funds_behalf_cust;
}
public void setR83_e1_funds_behalf_cust(BigDecimal r83_e1_funds_behalf_cust) {
    this.r83_e1_funds_behalf_cust = r83_e1_funds_behalf_cust;
}
public BigDecimal getR83_e1_turnover() {
    return r83_e1_turnover;
}
public void setR83_e1_turnover(BigDecimal r83_e1_turnover) {
    this.r83_e1_turnover = r83_e1_turnover;
}
public BigDecimal getR89_e2_tot_no_cust() {
    return r89_e2_tot_no_cust;
}
public void setR89_e2_tot_no_cust(BigDecimal r89_e2_tot_no_cust) {
    this.r89_e2_tot_no_cust = r89_e2_tot_no_cust;
}
public BigDecimal getR89_e2_loans_bal_expo() {
    return r89_e2_loans_bal_expo;
}
public void setR89_e2_loans_bal_expo(BigDecimal r89_e2_loans_bal_expo) {
    this.r89_e2_loans_bal_expo = r89_e2_loans_bal_expo;
}
public BigDecimal getR89_e2_deposit() {
    return r89_e2_deposit;
}
public void setR89_e2_deposit(BigDecimal r89_e2_deposit) {
    this.r89_e2_deposit = r89_e2_deposit;
}
public BigDecimal getR89_e2_funds_behalf_cust() {
    return r89_e2_funds_behalf_cust;
}
public void setR89_e2_funds_behalf_cust(BigDecimal r89_e2_funds_behalf_cust) {
    this.r89_e2_funds_behalf_cust = r89_e2_funds_behalf_cust;
}
public BigDecimal getR89_e2_turnover() {
    return r89_e2_turnover;
}
public void setR89_e2_turnover(BigDecimal r89_e2_turnover) {
    this.r89_e2_turnover = r89_e2_turnover;
}
public BigDecimal getR90_e2_tot_no_cust() {
    return r90_e2_tot_no_cust;
}
public void setR90_e2_tot_no_cust(BigDecimal r90_e2_tot_no_cust) {
    this.r90_e2_tot_no_cust = r90_e2_tot_no_cust;
}
public BigDecimal getR90_e2_loans_bal_expo() {
    return r90_e2_loans_bal_expo;
}
public void setR90_e2_loans_bal_expo(BigDecimal r90_e2_loans_bal_expo) {
    this.r90_e2_loans_bal_expo = r90_e2_loans_bal_expo;
}
public BigDecimal getR90_e2_deposit() {
    return r90_e2_deposit;
}
public void setR90_e2_deposit(BigDecimal r90_e2_deposit) {
    this.r90_e2_deposit = r90_e2_deposit;
}
public BigDecimal getR90_e2_funds_behalf_cust() {
    return r90_e2_funds_behalf_cust;
}
public void setR90_e2_funds_behalf_cust(BigDecimal r90_e2_funds_behalf_cust) {
    this.r90_e2_funds_behalf_cust = r90_e2_funds_behalf_cust;
}
public BigDecimal getR90_e2_turnover() {
    return r90_e2_turnover;
}
public void setR90_e2_turnover(BigDecimal r90_e2_turnover) {
    this.r90_e2_turnover = r90_e2_turnover;
}
public BigDecimal getR96_e3_tot_no_cust() {
    return r96_e3_tot_no_cust;
}
public void setR96_e3_tot_no_cust(BigDecimal r96_e3_tot_no_cust) {
    this.r96_e3_tot_no_cust = r96_e3_tot_no_cust;
}
public BigDecimal getR96_e3_loans_bal_expo() {
    return r96_e3_loans_bal_expo;
}
public void setR96_e3_loans_bal_expo(BigDecimal r96_e3_loans_bal_expo) {
    this.r96_e3_loans_bal_expo = r96_e3_loans_bal_expo;
}
public BigDecimal getR96_e3_deposit() {
    return r96_e3_deposit;
}
public void setR96_e3_deposit(BigDecimal r96_e3_deposit) {
    this.r96_e3_deposit = r96_e3_deposit;
}
public BigDecimal getR96_e3_funds_behalf_cust() {
    return r96_e3_funds_behalf_cust;
}
public void setR96_e3_funds_behalf_cust(BigDecimal r96_e3_funds_behalf_cust) {
    this.r96_e3_funds_behalf_cust = r96_e3_funds_behalf_cust;
}
public BigDecimal getR96_e3_turnover() {
    return r96_e3_turnover;
}
public void setR96_e3_turnover(BigDecimal r96_e3_turnover) {
    this.r96_e3_turnover = r96_e3_turnover;
}
public BigDecimal getR97_e3_tot_no_cust() {
    return r97_e3_tot_no_cust;
}
public void setR97_e3_tot_no_cust(BigDecimal r97_e3_tot_no_cust) {
    this.r97_e3_tot_no_cust = r97_e3_tot_no_cust;
}
public BigDecimal getR97_e3_loans_bal_expo() {
    return r97_e3_loans_bal_expo;
}
public void setR97_e3_loans_bal_expo(BigDecimal r97_e3_loans_bal_expo) {
    this.r97_e3_loans_bal_expo = r97_e3_loans_bal_expo;
}
public BigDecimal getR97_e3_deposit() {
    return r97_e3_deposit;
}
public void setR97_e3_deposit(BigDecimal r97_e3_deposit) {
    this.r97_e3_deposit = r97_e3_deposit;
}
public BigDecimal getR97_e3_funds_behalf_cust() {
    return r97_e3_funds_behalf_cust;
}
public void setR97_e3_funds_behalf_cust(BigDecimal r97_e3_funds_behalf_cust) {
    this.r97_e3_funds_behalf_cust = r97_e3_funds_behalf_cust;
}
public BigDecimal getR97_e3_turnover() {
    return r97_e3_turnover;
}
public void setR97_e3_turnover(BigDecimal r97_e3_turnover) {
    this.r97_e3_turnover = r97_e3_turnover;
}
public BigDecimal getR104_f_num_of_cust() {
    return r104_f_num_of_cust;
}
public void setR104_f_num_of_cust(BigDecimal r104_f_num_of_cust) {
    this.r104_f_num_of_cust = r104_f_num_of_cust;
}
public BigDecimal getR104_f_loans_bal_expo() {
    return r104_f_loans_bal_expo;
}
public void setR104_f_loans_bal_expo(BigDecimal r104_f_loans_bal_expo) {
    this.r104_f_loans_bal_expo = r104_f_loans_bal_expo;
}
public BigDecimal getR104_f_deposit() {
    return r104_f_deposit;
}
public void setR104_f_deposit(BigDecimal r104_f_deposit) {
    this.r104_f_deposit = r104_f_deposit;
}
public BigDecimal getR104_f_funds_behalf_cust() {
    return r104_f_funds_behalf_cust;
}
public void setR104_f_funds_behalf_cust(BigDecimal r104_f_funds_behalf_cust) {
    this.r104_f_funds_behalf_cust = r104_f_funds_behalf_cust;
}
public BigDecimal getR104_f_turnover() {
    return r104_f_turnover;
}
public void setR104_f_turnover(BigDecimal r104_f_turnover) {
    this.r104_f_turnover = r104_f_turnover;
}
public BigDecimal getR105_f_num_of_cust() {
    return r105_f_num_of_cust;
}
public void setR105_f_num_of_cust(BigDecimal r105_f_num_of_cust) {
    this.r105_f_num_of_cust = r105_f_num_of_cust;
}
public BigDecimal getR105_f_loans_bal_expo() {
    return r105_f_loans_bal_expo;
}
public void setR105_f_loans_bal_expo(BigDecimal r105_f_loans_bal_expo) {
    this.r105_f_loans_bal_expo = r105_f_loans_bal_expo;
}
public BigDecimal getR105_f_deposit() {
    return r105_f_deposit;
}
public void setR105_f_deposit(BigDecimal r105_f_deposit) {
    this.r105_f_deposit = r105_f_deposit;
}
public BigDecimal getR105_f_funds_behalf_cust() {
    return r105_f_funds_behalf_cust;
}
public void setR105_f_funds_behalf_cust(BigDecimal r105_f_funds_behalf_cust) {
    this.r105_f_funds_behalf_cust = r105_f_funds_behalf_cust;
}
public BigDecimal getR105_f_turnover() {
    return r105_f_turnover;
}
public void setR105_f_turnover(BigDecimal r105_f_turnover) {
    this.r105_f_turnover = r105_f_turnover;
}
public String getR111_g1_pay_mech() {
    return r111_g1_pay_mech;
}
public void setR111_g1_pay_mech(String r111_g1_pay_mech) {
    this.r111_g1_pay_mech = r111_g1_pay_mech;
}
public String getR111_g1_pay_mechanisum() {
    return r111_g1_pay_mechanisum;
}
public void setR111_g1_pay_mechanisum(String r111_g1_pay_mechanisum) {
    this.r111_g1_pay_mechanisum = r111_g1_pay_mechanisum;
}
public BigDecimal getR111_g1_num_trans() {
    return r111_g1_num_trans;
}
public void setR111_g1_num_trans(BigDecimal r111_g1_num_trans) {
    this.r111_g1_num_trans = r111_g1_num_trans;
}
public BigDecimal getR111_g1_val_trans() {
    return r111_g1_val_trans;
}
public void setR111_g1_val_trans(BigDecimal r111_g1_val_trans) {
    this.r111_g1_val_trans = r111_g1_val_trans;
}
public String getR112_g1_pay_mech() {
    return r112_g1_pay_mech;
}
public void setR112_g1_pay_mech(String r112_g1_pay_mech) {
    this.r112_g1_pay_mech = r112_g1_pay_mech;
}
public String getR112_g1_pay_mechanisum() {
    return r112_g1_pay_mechanisum;
}
public void setR112_g1_pay_mechanisum(String r112_g1_pay_mechanisum) {
    this.r112_g1_pay_mechanisum = r112_g1_pay_mechanisum;
}
public BigDecimal getR112_g1_num_trans() {
    return r112_g1_num_trans;
}
public void setR112_g1_num_trans(BigDecimal r112_g1_num_trans) {
    this.r112_g1_num_trans = r112_g1_num_trans;
}
public BigDecimal getR112_g1_val_trans() {
    return r112_g1_val_trans;
}
public void setR112_g1_val_trans(BigDecimal r112_g1_val_trans) {
    this.r112_g1_val_trans = r112_g1_val_trans;
}
public String getR113_g1_pay_mech() {
    return r113_g1_pay_mech;
}
public void setR113_g1_pay_mech(String r113_g1_pay_mech) {
    this.r113_g1_pay_mech = r113_g1_pay_mech;
}
public String getR113_g1_pay_mechanisum() {
    return r113_g1_pay_mechanisum;
}
public void setR113_g1_pay_mechanisum(String r113_g1_pay_mechanisum) {
    this.r113_g1_pay_mechanisum = r113_g1_pay_mechanisum;
}
public BigDecimal getR113_g1_num_trans() {
    return r113_g1_num_trans;
}
public void setR113_g1_num_trans(BigDecimal r113_g1_num_trans) {
    this.r113_g1_num_trans = r113_g1_num_trans;
}
public BigDecimal getR113_g1_val_trans() {
    return r113_g1_val_trans;
}
public void setR113_g1_val_trans(BigDecimal r113_g1_val_trans) {
    this.r113_g1_val_trans = r113_g1_val_trans;
}
public String getR114_g1_pay_mech() {
    return r114_g1_pay_mech;
}
public void setR114_g1_pay_mech(String r114_g1_pay_mech) {
    this.r114_g1_pay_mech = r114_g1_pay_mech;
}
public String getR114_g1_pay_mechanisum() {
    return r114_g1_pay_mechanisum;
}
public void setR114_g1_pay_mechanisum(String r114_g1_pay_mechanisum) {
    this.r114_g1_pay_mechanisum = r114_g1_pay_mechanisum;
}
public BigDecimal getR114_g1_num_trans() {
    return r114_g1_num_trans;
}
public void setR114_g1_num_trans(BigDecimal r114_g1_num_trans) {
    this.r114_g1_num_trans = r114_g1_num_trans;
}
public BigDecimal getR114_g1_val_trans() {
    return r114_g1_val_trans;
}
public void setR114_g1_val_trans(BigDecimal r114_g1_val_trans) {
    this.r114_g1_val_trans = r114_g1_val_trans;
}
public String getR115_g1_pay_mech() {
    return r115_g1_pay_mech;
}
public void setR115_g1_pay_mech(String r115_g1_pay_mech) {
    this.r115_g1_pay_mech = r115_g1_pay_mech;
}
public String getR115_g1_pay_mechanisum() {
    return r115_g1_pay_mechanisum;
}
public void setR115_g1_pay_mechanisum(String r115_g1_pay_mechanisum) {
    this.r115_g1_pay_mechanisum = r115_g1_pay_mechanisum;
}
public BigDecimal getR115_g1_num_trans() {
    return r115_g1_num_trans;
}
public void setR115_g1_num_trans(BigDecimal r115_g1_num_trans) {
    this.r115_g1_num_trans = r115_g1_num_trans;
}
public BigDecimal getR115_g1_val_trans() {
    return r115_g1_val_trans;
}
public void setR115_g1_val_trans(BigDecimal r115_g1_val_trans) {
    this.r115_g1_val_trans = r115_g1_val_trans;
}
public String getR116_g1_pay_mech() {
    return r116_g1_pay_mech;
}
public void setR116_g1_pay_mech(String r116_g1_pay_mech) {
    this.r116_g1_pay_mech = r116_g1_pay_mech;
}
public String getR116_g1_pay_mechanisum() {
    return r116_g1_pay_mechanisum;
}
public void setR116_g1_pay_mechanisum(String r116_g1_pay_mechanisum) {
    this.r116_g1_pay_mechanisum = r116_g1_pay_mechanisum;
}
public BigDecimal getR116_g1_num_trans() {
    return r116_g1_num_trans;
}
public void setR116_g1_num_trans(BigDecimal r116_g1_num_trans) {
    this.r116_g1_num_trans = r116_g1_num_trans;
}
public BigDecimal getR116_g1_val_trans() {
    return r116_g1_val_trans;
}
public void setR116_g1_val_trans(BigDecimal r116_g1_val_trans) {
    this.r116_g1_val_trans = r116_g1_val_trans;
}
public String getR117_g1_pay_mech() {
    return r117_g1_pay_mech;
}
public void setR117_g1_pay_mech(String r117_g1_pay_mech) {
    this.r117_g1_pay_mech = r117_g1_pay_mech;
}
public String getR117_g1_pay_mechanisum() {
    return r117_g1_pay_mechanisum;
}
public void setR117_g1_pay_mechanisum(String r117_g1_pay_mechanisum) {
    this.r117_g1_pay_mechanisum = r117_g1_pay_mechanisum;
}
public BigDecimal getR117_g1_num_trans() {
    return r117_g1_num_trans;
}
public void setR117_g1_num_trans(BigDecimal r117_g1_num_trans) {
    this.r117_g1_num_trans = r117_g1_num_trans;
}
public BigDecimal getR117_g1_val_trans() {
    return r117_g1_val_trans;
}
public void setR117_g1_val_trans(BigDecimal r117_g1_val_trans) {
    this.r117_g1_val_trans = r117_g1_val_trans;
}
public String getR118_g1_pay_mech() {
    return r118_g1_pay_mech;
}
public void setR118_g1_pay_mech(String r118_g1_pay_mech) {
    this.r118_g1_pay_mech = r118_g1_pay_mech;
}
public String getR118_g1_pay_mechanisum() {
    return r118_g1_pay_mechanisum;
}
public void setR118_g1_pay_mechanisum(String r118_g1_pay_mechanisum) {
    this.r118_g1_pay_mechanisum = r118_g1_pay_mechanisum;
}
public BigDecimal getR118_g1_num_trans() {
    return r118_g1_num_trans;
}
public void setR118_g1_num_trans(BigDecimal r118_g1_num_trans) {
    this.r118_g1_num_trans = r118_g1_num_trans;
}
public BigDecimal getR118_g1_val_trans() {
    return r118_g1_val_trans;
}
public void setR118_g1_val_trans(BigDecimal r118_g1_val_trans) {
    this.r118_g1_val_trans = r118_g1_val_trans;
}
public String getR119_g1_pay_mech() {
    return r119_g1_pay_mech;
}
public void setR119_g1_pay_mech(String r119_g1_pay_mech) {
    this.r119_g1_pay_mech = r119_g1_pay_mech;
}
public String getR119_g1_pay_mechanisum() {
    return r119_g1_pay_mechanisum;
}
public void setR119_g1_pay_mechanisum(String r119_g1_pay_mechanisum) {
    this.r119_g1_pay_mechanisum = r119_g1_pay_mechanisum;
}
public BigDecimal getR119_g1_num_trans() {
    return r119_g1_num_trans;
}
public void setR119_g1_num_trans(BigDecimal r119_g1_num_trans) {
    this.r119_g1_num_trans = r119_g1_num_trans;
}
public BigDecimal getR119_g1_val_trans() {
    return r119_g1_val_trans;
}
public void setR119_g1_val_trans(BigDecimal r119_g1_val_trans) {
    this.r119_g1_val_trans = r119_g1_val_trans;
}
public String getR120_g1_pay_mech() {
    return r120_g1_pay_mech;
}
public void setR120_g1_pay_mech(String r120_g1_pay_mech) {
    this.r120_g1_pay_mech = r120_g1_pay_mech;
}
public String getR120_g1_pay_mechanisum() {
    return r120_g1_pay_mechanisum;
}
public void setR120_g1_pay_mechanisum(String r120_g1_pay_mechanisum) {
    this.r120_g1_pay_mechanisum = r120_g1_pay_mechanisum;
}
public BigDecimal getR120_g1_num_trans() {
    return r120_g1_num_trans;
}
public void setR120_g1_num_trans(BigDecimal r120_g1_num_trans) {
    this.r120_g1_num_trans = r120_g1_num_trans;
}
public BigDecimal getR120_g1_val_trans() {
    return r120_g1_val_trans;
}
public void setR120_g1_val_trans(BigDecimal r120_g1_val_trans) {
    this.r120_g1_val_trans = r120_g1_val_trans;
}
public String getR121_g1_pay_mech() {
    return r121_g1_pay_mech;
}
public void setR121_g1_pay_mech(String r121_g1_pay_mech) {
    this.r121_g1_pay_mech = r121_g1_pay_mech;
}
public String getR121_g1_pay_mechanisum() {
    return r121_g1_pay_mechanisum;
}
public void setR121_g1_pay_mechanisum(String r121_g1_pay_mechanisum) {
    this.r121_g1_pay_mechanisum = r121_g1_pay_mechanisum;
}
public BigDecimal getR121_g1_num_trans() {
    return r121_g1_num_trans;
}
public void setR121_g1_num_trans(BigDecimal r121_g1_num_trans) {
    this.r121_g1_num_trans = r121_g1_num_trans;
}
public BigDecimal getR121_g1_val_trans() {
    return r121_g1_val_trans;
}
public void setR121_g1_val_trans(BigDecimal r121_g1_val_trans) {
    this.r121_g1_val_trans = r121_g1_val_trans;
}
public String getR122_g1_pay_mech() {
    return r122_g1_pay_mech;
}
public void setR122_g1_pay_mech(String r122_g1_pay_mech) {
    this.r122_g1_pay_mech = r122_g1_pay_mech;
}
public String getR122_g1_pay_mechanisum() {
    return r122_g1_pay_mechanisum;
}
public void setR122_g1_pay_mechanisum(String r122_g1_pay_mechanisum) {
    this.r122_g1_pay_mechanisum = r122_g1_pay_mechanisum;
}
public BigDecimal getR122_g1_num_trans() {
    return r122_g1_num_trans;
}
public void setR122_g1_num_trans(BigDecimal r122_g1_num_trans) {
    this.r122_g1_num_trans = r122_g1_num_trans;
}
public BigDecimal getR122_g1_val_trans() {
    return r122_g1_val_trans;
}
public void setR122_g1_val_trans(BigDecimal r122_g1_val_trans) {
    this.r122_g1_val_trans = r122_g1_val_trans;
}
public String getR123_g1_pay_mech() {
    return r123_g1_pay_mech;
}
public void setR123_g1_pay_mech(String r123_g1_pay_mech) {
    this.r123_g1_pay_mech = r123_g1_pay_mech;
}
public String getR123_g1_pay_mechanisum() {
    return r123_g1_pay_mechanisum;
}
public void setR123_g1_pay_mechanisum(String r123_g1_pay_mechanisum) {
    this.r123_g1_pay_mechanisum = r123_g1_pay_mechanisum;
}
public BigDecimal getR123_g1_num_trans() {
    return r123_g1_num_trans;
}
public void setR123_g1_num_trans(BigDecimal r123_g1_num_trans) {
    this.r123_g1_num_trans = r123_g1_num_trans;
}
public BigDecimal getR123_g1_val_trans() {
    return r123_g1_val_trans;
}
public void setR123_g1_val_trans(BigDecimal r123_g1_val_trans) {
    this.r123_g1_val_trans = r123_g1_val_trans;
}
public String getR124_g1_pay_mech() {
    return r124_g1_pay_mech;
}
public void setR124_g1_pay_mech(String r124_g1_pay_mech) {
    this.r124_g1_pay_mech = r124_g1_pay_mech;
}
public String getR124_g1_pay_mechanisum() {
    return r124_g1_pay_mechanisum;
}
public void setR124_g1_pay_mechanisum(String r124_g1_pay_mechanisum) {
    this.r124_g1_pay_mechanisum = r124_g1_pay_mechanisum;
}
public BigDecimal getR124_g1_num_trans() {
    return r124_g1_num_trans;
}
public void setR124_g1_num_trans(BigDecimal r124_g1_num_trans) {
    this.r124_g1_num_trans = r124_g1_num_trans;
}
public BigDecimal getR124_g1_val_trans() {
    return r124_g1_val_trans;
}
public void setR124_g1_val_trans(BigDecimal r124_g1_val_trans) {
    this.r124_g1_val_trans = r124_g1_val_trans;
}
public String getR125_g1_pay_mech() {
    return r125_g1_pay_mech;
}
public void setR125_g1_pay_mech(String r125_g1_pay_mech) {
    this.r125_g1_pay_mech = r125_g1_pay_mech;
}
public String getR125_g1_pay_mechanisum() {
    return r125_g1_pay_mechanisum;
}
public void setR125_g1_pay_mechanisum(String r125_g1_pay_mechanisum) {
    this.r125_g1_pay_mechanisum = r125_g1_pay_mechanisum;
}
public BigDecimal getR125_g1_num_trans() {
    return r125_g1_num_trans;
}
public void setR125_g1_num_trans(BigDecimal r125_g1_num_trans) {
    this.r125_g1_num_trans = r125_g1_num_trans;
}
public BigDecimal getR125_g1_val_trans() {
    return r125_g1_val_trans;
}
public void setR125_g1_val_trans(BigDecimal r125_g1_val_trans) {
    this.r125_g1_val_trans = r125_g1_val_trans;
}
public String getR126_g1_pay_mech() {
    return r126_g1_pay_mech;
}
public void setR126_g1_pay_mech(String r126_g1_pay_mech) {
    this.r126_g1_pay_mech = r126_g1_pay_mech;
}
public String getR126_g1_pay_mechanisum() {
    return r126_g1_pay_mechanisum;
}
public void setR126_g1_pay_mechanisum(String r126_g1_pay_mechanisum) {
    this.r126_g1_pay_mechanisum = r126_g1_pay_mechanisum;
}
public BigDecimal getR126_g1_num_trans() {
    return r126_g1_num_trans;
}
public void setR126_g1_num_trans(BigDecimal r126_g1_num_trans) {
    this.r126_g1_num_trans = r126_g1_num_trans;
}
public BigDecimal getR126_g1_val_trans() {
    return r126_g1_val_trans;
}
public void setR126_g1_val_trans(BigDecimal r126_g1_val_trans) {
    this.r126_g1_val_trans = r126_g1_val_trans;
}
public String getR127_g1_pay_mech() {
    return r127_g1_pay_mech;
}
public void setR127_g1_pay_mech(String r127_g1_pay_mech) {
    this.r127_g1_pay_mech = r127_g1_pay_mech;
}
public String getR127_g1_pay_mechanisum() {
    return r127_g1_pay_mechanisum;
}
public void setR127_g1_pay_mechanisum(String r127_g1_pay_mechanisum) {
    this.r127_g1_pay_mechanisum = r127_g1_pay_mechanisum;
}
public BigDecimal getR127_g1_num_trans() {
    return r127_g1_num_trans;
}
public void setR127_g1_num_trans(BigDecimal r127_g1_num_trans) {
    this.r127_g1_num_trans = r127_g1_num_trans;
}
public BigDecimal getR127_g1_val_trans() {
    return r127_g1_val_trans;
}
public void setR127_g1_val_trans(BigDecimal r127_g1_val_trans) {
    this.r127_g1_val_trans = r127_g1_val_trans;
}
public String getR128_g1_pay_mech() {
    return r128_g1_pay_mech;
}
public void setR128_g1_pay_mech(String r128_g1_pay_mech) {
    this.r128_g1_pay_mech = r128_g1_pay_mech;
}
public String getR128_g1_pay_mechanisum() {
    return r128_g1_pay_mechanisum;
}
public void setR128_g1_pay_mechanisum(String r128_g1_pay_mechanisum) {
    this.r128_g1_pay_mechanisum = r128_g1_pay_mechanisum;
}
public BigDecimal getR128_g1_num_trans() {
    return r128_g1_num_trans;
}
public void setR128_g1_num_trans(BigDecimal r128_g1_num_trans) {
    this.r128_g1_num_trans = r128_g1_num_trans;
}
public BigDecimal getR128_g1_val_trans() {
    return r128_g1_val_trans;
}
public void setR128_g1_val_trans(BigDecimal r128_g1_val_trans) {
    this.r128_g1_val_trans = r128_g1_val_trans;
}
public String getR135_g2_foreign_exchange() {
    return r135_g2_foreign_exchange;
}
public void setR135_g2_foreign_exchange(String r135_g2_foreign_exchange) {
    this.r135_g2_foreign_exchange = r135_g2_foreign_exchange;
}
public String getR135_g2_fore_exchange() {
    return r135_g2_fore_exchange;
}
public void setR135_g2_fore_exchange(String r135_g2_fore_exchange) {
    this.r135_g2_fore_exchange = r135_g2_fore_exchange;
}
public BigDecimal getR135_g2_val_transac() {
    return r135_g2_val_transac;
}
public void setR135_g2_val_transac(BigDecimal r135_g2_val_transac) {
    this.r135_g2_val_transac = r135_g2_val_transac;
}
public String getR136_g2_fore_exchange() {
    return r136_g2_fore_exchange;
}
public void setR136_g2_fore_exchange(String r136_g2_fore_exchange) {
    this.r136_g2_fore_exchange = r136_g2_fore_exchange;
}
public BigDecimal getR136_g2_val_transac() {
    return r136_g2_val_transac;
}
public void setR136_g2_val_transac(BigDecimal r136_g2_val_transac) {
    this.r136_g2_val_transac = r136_g2_val_transac;
}
public String getR138_g2_foreign_exchange() {
    return r138_g2_foreign_exchange;
}
public void setR138_g2_foreign_exchange(String r138_g2_foreign_exchange) {
    this.r138_g2_foreign_exchange = r138_g2_foreign_exchange;
}
public String getR138_g2_fore_exchange() {
    return r138_g2_fore_exchange;
}
public void setR138_g2_fore_exchange(String r138_g2_fore_exchange) {
    this.r138_g2_fore_exchange = r138_g2_fore_exchange;
}
public BigDecimal getR138_g2_val_transac() {
    return r138_g2_val_transac;
}
public void setR138_g2_val_transac(BigDecimal r138_g2_val_transac) {
    this.r138_g2_val_transac = r138_g2_val_transac;
}
public String getR139_g2_fore_exchange() {
    return r139_g2_fore_exchange;
}
public void setR139_g2_fore_exchange(String r139_g2_fore_exchange) {
    this.r139_g2_fore_exchange = r139_g2_fore_exchange;
}
public BigDecimal getR139_g2_val_transac() {
    return r139_g2_val_transac;
}
public void setR139_g2_val_transac(BigDecimal r139_g2_val_transac) {
    this.r139_g2_val_transac = r139_g2_val_transac;
}
public String getR144_h_types() {
    return r144_h_types;
}
public void setR144_h_types(String r144_h_types) {
    this.r144_h_types = r144_h_types;
}
public BigDecimal getR144_h_amount() {
    return r144_h_amount;
}
public void setR144_h_amount(BigDecimal r144_h_amount) {
    this.r144_h_amount = r144_h_amount;
}
public String getR145_h_types() {
    return r145_h_types;
}
public void setR145_h_types(String r145_h_types) {
    this.r145_h_types = r145_h_types;
}
public BigDecimal getR145_h_amount() {
    return r145_h_amount;
}
public void setR145_h_amount(BigDecimal r145_h_amount) {
    this.r145_h_amount = r145_h_amount;
}
public String getR146_h_types() {
    return r146_h_types;
}
public void setR146_h_types(String r146_h_types) {
    this.r146_h_types = r146_h_types;
}
public BigDecimal getR146_h_amount() {
    return r146_h_amount;
}
public void setR146_h_amount(BigDecimal r146_h_amount) {
    this.r146_h_amount = r146_h_amount;
}
public String getR147_h_types() {
    return r147_h_types;
}
public void setR147_h_types(String r147_h_types) {
    this.r147_h_types = r147_h_types;
}
public BigDecimal getR147_h_amount() {
    return r147_h_amount;
}
public void setR147_h_amount(BigDecimal r147_h_amount) {
    this.r147_h_amount = r147_h_amount;
}
public String getR148_h_types() {
    return r148_h_types;
}
public void setR148_h_types(String r148_h_types) {
    this.r148_h_types = r148_h_types;
}
public BigDecimal getR148_h_amount() {
    return r148_h_amount;
}
public void setR148_h_amount(BigDecimal r148_h_amount) {
    this.r148_h_amount = r148_h_amount;
}
public String getR153_i_product_serv() {
    return r153_i_product_serv;
}
public void setR153_i_product_serv(String r153_i_product_serv) {
    this.r153_i_product_serv = r153_i_product_serv;
}
public BigDecimal getR153_i_no_cust() {
    return r153_i_no_cust;
}
public void setR153_i_no_cust(BigDecimal r153_i_no_cust) {
    this.r153_i_no_cust = r153_i_no_cust;
}
public BigDecimal getR153_i_outs_bal() {
    return r153_i_outs_bal;
}
public void setR153_i_outs_bal(BigDecimal r153_i_outs_bal) {
    this.r153_i_outs_bal = r153_i_outs_bal;
}
public BigDecimal getR153_i_turnover() {
    return r153_i_turnover;
}
public void setR153_i_turnover(BigDecimal r153_i_turnover) {
    this.r153_i_turnover = r153_i_turnover;
}
public String getR154_i_product_serv() {
    return r154_i_product_serv;
}
public void setR154_i_product_serv(String r154_i_product_serv) {
    this.r154_i_product_serv = r154_i_product_serv;
}
public BigDecimal getR154_i_no_cust() {
    return r154_i_no_cust;
}
public void setR154_i_no_cust(BigDecimal r154_i_no_cust) {
    this.r154_i_no_cust = r154_i_no_cust;
}
public BigDecimal getR154_i_outs_bal() {
    return r154_i_outs_bal;
}
public void setR154_i_outs_bal(BigDecimal r154_i_outs_bal) {
    this.r154_i_outs_bal = r154_i_outs_bal;
}
public BigDecimal getR154_i_turnover() {
    return r154_i_turnover;
}
public void setR154_i_turnover(BigDecimal r154_i_turnover) {
    this.r154_i_turnover = r154_i_turnover;
}
public String getR155_i_product_serv() {
    return r155_i_product_serv;
}
public void setR155_i_product_serv(String r155_i_product_serv) {
    this.r155_i_product_serv = r155_i_product_serv;
}
public BigDecimal getR155_i_no_cust() {
    return r155_i_no_cust;
}
public void setR155_i_no_cust(BigDecimal r155_i_no_cust) {
    this.r155_i_no_cust = r155_i_no_cust;
}
public BigDecimal getR155_i_outs_bal() {
    return r155_i_outs_bal;
}
public void setR155_i_outs_bal(BigDecimal r155_i_outs_bal) {
    this.r155_i_outs_bal = r155_i_outs_bal;
}
public BigDecimal getR155_i_turnover() {
    return r155_i_turnover;
}
public void setR155_i_turnover(BigDecimal r155_i_turnover) {
    this.r155_i_turnover = r155_i_turnover;
}
public String getR161_j_trade_finc_prod() {
    return r161_j_trade_finc_prod;
}
public void setR161_j_trade_finc_prod(String r161_j_trade_finc_prod) {
    this.r161_j_trade_finc_prod = r161_j_trade_finc_prod;
}
public BigDecimal getR161_j_num_of_cust() {
    return r161_j_num_of_cust;
}
public void setR161_j_num_of_cust(BigDecimal r161_j_num_of_cust) {
    this.r161_j_num_of_cust = r161_j_num_of_cust;
}
public BigDecimal getR161_j_commitment_at_jun() {
    return r161_j_commitment_at_jun;
}
public void setR161_j_commitment_at_jun(BigDecimal r161_j_commitment_at_jun) {
    this.r161_j_commitment_at_jun = r161_j_commitment_at_jun;
}
public String getR162_j_trade_finc_prod() {
    return r162_j_trade_finc_prod;
}
public void setR162_j_trade_finc_prod(String r162_j_trade_finc_prod) {
    this.r162_j_trade_finc_prod = r162_j_trade_finc_prod;
}
public BigDecimal getR162_j_num_of_cust() {
    return r162_j_num_of_cust;
}
public void setR162_j_num_of_cust(BigDecimal r162_j_num_of_cust) {
    this.r162_j_num_of_cust = r162_j_num_of_cust;
}
public BigDecimal getR162_j_commitment_at_jun() {
    return r162_j_commitment_at_jun;
}
public void setR162_j_commitment_at_jun(BigDecimal r162_j_commitment_at_jun) {
    this.r162_j_commitment_at_jun = r162_j_commitment_at_jun;
}
public String getR163_j_trade_finc_prod() {
    return r163_j_trade_finc_prod;
}
public void setR163_j_trade_finc_prod(String r163_j_trade_finc_prod) {
    this.r163_j_trade_finc_prod = r163_j_trade_finc_prod;
}
public BigDecimal getR163_j_num_of_cust() {
    return r163_j_num_of_cust;
}
public void setR163_j_num_of_cust(BigDecimal r163_j_num_of_cust) {
    this.r163_j_num_of_cust = r163_j_num_of_cust;
}
public BigDecimal getR163_j_commitment_at_jun() {
    return r163_j_commitment_at_jun;
}
public void setR163_j_commitment_at_jun(BigDecimal r163_j_commitment_at_jun) {
    this.r163_j_commitment_at_jun = r163_j_commitment_at_jun;
}
public String getR164_j_trade_finc_prod() {
    return r164_j_trade_finc_prod;
}
public void setR164_j_trade_finc_prod(String r164_j_trade_finc_prod) {
    this.r164_j_trade_finc_prod = r164_j_trade_finc_prod;
}
public BigDecimal getR164_j_num_of_cust() {
    return r164_j_num_of_cust;
}
public void setR164_j_num_of_cust(BigDecimal r164_j_num_of_cust) {
    this.r164_j_num_of_cust = r164_j_num_of_cust;
}
public BigDecimal getR164_j_commitment_at_jun() {
    return r164_j_commitment_at_jun;
}
public void setR164_j_commitment_at_jun(BigDecimal r164_j_commitment_at_jun) {
    this.r164_j_commitment_at_jun = r164_j_commitment_at_jun;
}
public String getR170_k_pay_mechanism() {
    return r170_k_pay_mechanism;
}
public void setR170_k_pay_mechanism(String r170_k_pay_mechanism) {
    this.r170_k_pay_mechanism = r170_k_pay_mechanism;
}
public String getR170_k_pay_mech() {
    return r170_k_pay_mech;
}
public void setR170_k_pay_mech(String r170_k_pay_mech) {
    this.r170_k_pay_mech = r170_k_pay_mech;
}
public BigDecimal getR170_k_num_of_trans() {
    return r170_k_num_of_trans;
}
public void setR170_k_num_of_trans(BigDecimal r170_k_num_of_trans) {
    this.r170_k_num_of_trans = r170_k_num_of_trans;
}
public BigDecimal getR170_k_value_of_trans() {
    return r170_k_value_of_trans;
}
public void setR170_k_value_of_trans(BigDecimal r170_k_value_of_trans) {
    this.r170_k_value_of_trans = r170_k_value_of_trans;
}
public String getR171_k_pay_mech() {
    return r171_k_pay_mech;
}
public void setR171_k_pay_mech(String r171_k_pay_mech) {
    this.r171_k_pay_mech = r171_k_pay_mech;
}
public BigDecimal getR171_k_num_of_trans() {
    return r171_k_num_of_trans;
}
public void setR171_k_num_of_trans(BigDecimal r171_k_num_of_trans) {
    this.r171_k_num_of_trans = r171_k_num_of_trans;
}
public BigDecimal getR171_k_value_of_trans() {
    return r171_k_value_of_trans;
}
public void setR171_k_value_of_trans(BigDecimal r171_k_value_of_trans) {
    this.r171_k_value_of_trans = r171_k_value_of_trans;
}
public String getR172_k_pay_mechanism() {
    return r172_k_pay_mechanism;
}
public void setR172_k_pay_mechanism(String r172_k_pay_mechanism) {
    this.r172_k_pay_mechanism = r172_k_pay_mechanism;
}
public String getR172_k_pay_mech() {
    return r172_k_pay_mech;
}
public void setR172_k_pay_mech(String r172_k_pay_mech) {
    this.r172_k_pay_mech = r172_k_pay_mech;
}
public BigDecimal getR172_k_num_of_trans() {
    return r172_k_num_of_trans;
}
public void setR172_k_num_of_trans(BigDecimal r172_k_num_of_trans) {
    this.r172_k_num_of_trans = r172_k_num_of_trans;
}
public BigDecimal getR172_k_value_of_trans() {
    return r172_k_value_of_trans;
}
public void setR172_k_value_of_trans(BigDecimal r172_k_value_of_trans) {
    this.r172_k_value_of_trans = r172_k_value_of_trans;
}
public String getR179_l_transac_report() {
    return r179_l_transac_report;
}
public void setR179_l_transac_report(String r179_l_transac_report) {
    this.r179_l_transac_report = r179_l_transac_report;
}
public BigDecimal getR179_l_num_of_transac() {
    return r179_l_num_of_transac;
}
public void setR179_l_num_of_transac(BigDecimal r179_l_num_of_transac) {
    this.r179_l_num_of_transac = r179_l_num_of_transac;
}
public String getR180_l_transac_report() {
    return r180_l_transac_report;
}
public void setR180_l_transac_report(String r180_l_transac_report) {
    this.r180_l_transac_report = r180_l_transac_report;
}
public BigDecimal getR180_l_num_of_transac() {
    return r180_l_num_of_transac;
}
public void setR180_l_num_of_transac(BigDecimal r180_l_num_of_transac) {
    this.r180_l_num_of_transac = r180_l_num_of_transac;
}
public String getR181_l_transac_report() {
    return r181_l_transac_report;
}
public void setR181_l_transac_report(String r181_l_transac_report) {
    this.r181_l_transac_report = r181_l_transac_report;
}
public BigDecimal getR181_l_num_of_transac() {
    return r181_l_num_of_transac;
}
public void setR181_l_num_of_transac(BigDecimal r181_l_num_of_transac) {
    this.r181_l_num_of_transac = r181_l_num_of_transac;
}
public String getR187_m_transac_life() {
    return r187_m_transac_life;
}
public void setR187_m_transac_life(String r187_m_transac_life) {
    this.r187_m_transac_life = r187_m_transac_life;
}
public BigDecimal getR187_m_num_of_transac() {
    return r187_m_num_of_transac;
}
public void setR187_m_num_of_transac(BigDecimal r187_m_num_of_transac) {
    this.r187_m_num_of_transac = r187_m_num_of_transac;
}
public BigDecimal getR187_m_val_of_transac() {
    return r187_m_val_of_transac;
}
public void setR187_m_val_of_transac(BigDecimal r187_m_val_of_transac) {
    this.r187_m_val_of_transac = r187_m_val_of_transac;
}
public String getR192_n_transac_life() {
    return r192_n_transac_life;
}
public void setR192_n_transac_life(String r192_n_transac_life) {
    this.r192_n_transac_life = r192_n_transac_life;
}
public BigDecimal getR192_n_num_of_transac() {
    return r192_n_num_of_transac;
}
public void setR192_n_num_of_transac(BigDecimal r192_n_num_of_transac) {
    this.r192_n_num_of_transac = r192_n_num_of_transac;
}
public BigDecimal getR192_n_val_of_transac() {
    return r192_n_val_of_transac;
}
public void setR192_n_val_of_transac(BigDecimal r192_n_val_of_transac) {
    this.r192_n_val_of_transac = r192_n_val_of_transac;
}
public String getR196_o_transac_life() {
    return r196_o_transac_life;
}
public void setR196_o_transac_life(String r196_o_transac_life) {
    this.r196_o_transac_life = r196_o_transac_life;
}
public BigDecimal getR196_o_num_of_transac() {
    return r196_o_num_of_transac;
}
public void setR196_o_num_of_transac(BigDecimal r196_o_num_of_transac) {
    this.r196_o_num_of_transac = r196_o_num_of_transac;
}
public BigDecimal getR196_o_val_of_transac() {
    return r196_o_val_of_transac;
}
public void setR196_o_val_of_transac(BigDecimal r196_o_val_of_transac) {
    this.r196_o_val_of_transac = r196_o_val_of_transac;
}
public String getR201_p_transac_life() {
    return r201_p_transac_life;
}
public void setR201_p_transac_life(String r201_p_transac_life) {
    this.r201_p_transac_life = r201_p_transac_life;
}
public BigDecimal getR201_p_num_of_transac() {
    return r201_p_num_of_transac;
}
public void setR201_p_num_of_transac(BigDecimal r201_p_num_of_transac) {
    this.r201_p_num_of_transac = r201_p_num_of_transac;
}
public BigDecimal getR201_p_val_of_transac() {
    return r201_p_val_of_transac;
}
public void setR201_p_val_of_transac(BigDecimal r201_p_val_of_transac) {
    this.r201_p_val_of_transac = r201_p_val_of_transac;
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
public AML_Archival_Summary_Entity() {
       super();
}


}
