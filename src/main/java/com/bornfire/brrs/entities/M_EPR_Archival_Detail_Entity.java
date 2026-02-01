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
@Table(name = "BRRS_M_EPR_ARCHIVALTABLE_DETAIL")
public class M_EPR_Archival_Detail_Entity {

	private BigDecimal	r11_market;
	private BigDecimal	r11_gpfsr_nom_amt;
	private BigDecimal	r11_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg;
	private BigDecimal	r11_gpfsr_nom_amt1;
	private BigDecimal	r11_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg1;
	private BigDecimal	r11_gpfsr_nom_amt2;
	private BigDecimal	r11_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r11_gpfsr_chrg2;
	private BigDecimal	r11_tot_spe_ris_chrg;
	private BigDecimal	r11_net_pos_gen_mar_ris;
	private BigDecimal	r11_gen_mar_ris_chrg_8per;
	private BigDecimal	r11_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r11_tot_gen_mar_risk_chrg;
	private BigDecimal	r11_tot_mar_ris_chrg;
	
	private BigDecimal	r12_market;
	private BigDecimal	r12_gpfsr_nom_amt;
	private BigDecimal	r12_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg;
	private BigDecimal	r12_gpfsr_nom_amt1;
	private BigDecimal	r12_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg1;
	private BigDecimal	r12_gpfsr_nom_amt2;
	private BigDecimal	r12_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r12_gpfsr_chrg2;
	private BigDecimal	r12_tot_spe_ris_chrg;
	private BigDecimal	r12_net_pos_gen_mar_ris;
	private BigDecimal	r12_gen_mar_ris_chrg_8per;
	private BigDecimal	r12_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r12_tot_gen_mar_risk_chrg;
	private BigDecimal	r12_tot_mar_ris_chrg;
	
	private BigDecimal	r13_market;
	private BigDecimal	r13_gpfsr_nom_amt;
	private BigDecimal	r13_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg;
	private BigDecimal	r13_gpfsr_nom_amt1;
	private BigDecimal	r13_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg1;
	private BigDecimal	r13_gpfsr_nom_amt2;
	private BigDecimal	r13_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r13_gpfsr_chrg2;
	private BigDecimal	r13_tot_spe_ris_chrg;
	private BigDecimal	r13_net_pos_gen_mar_ris;
	private BigDecimal	r13_gen_mar_ris_chrg_8per;
	private BigDecimal	r13_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r13_tot_gen_mar_risk_chrg;
	private BigDecimal	r13_tot_mar_ris_chrg;
	
	private BigDecimal	r14_market;
	private BigDecimal	r14_gpfsr_nom_amt;
	private BigDecimal	r14_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg;
	private BigDecimal	r14_gpfsr_nom_amt1;
	private BigDecimal	r14_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg1;
	private BigDecimal	r14_gpfsr_nom_amt2;
	private BigDecimal	r14_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r14_gpfsr_chrg2;
	private BigDecimal	r14_tot_spe_ris_chrg;
	private BigDecimal	r14_net_pos_gen_mar_ris;
	private BigDecimal	r14_gen_mar_ris_chrg_8per;
	private BigDecimal	r14_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r14_tot_gen_mar_risk_chrg;
	private BigDecimal	r14_tot_mar_ris_chrg;
	
	private BigDecimal	r15_market;
	private BigDecimal	r15_gpfsr_nom_amt;
	private BigDecimal	r15_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg;
	private BigDecimal	r15_gpfsr_nom_amt1;
	private BigDecimal	r15_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg1;
	private BigDecimal	r15_gpfsr_nom_amt2;
	private BigDecimal	r15_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r15_gpfsr_chrg2;
	private BigDecimal	r15_tot_spe_ris_chrg;
	private BigDecimal	r15_net_pos_gen_mar_ris;
	private BigDecimal	r15_gen_mar_ris_chrg_8per;
	private BigDecimal	r15_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r15_tot_gen_mar_risk_chrg;
	private BigDecimal	r15_tot_mar_ris_chrg;
	
	private BigDecimal	r16_market;
	private BigDecimal	r16_gpfsr_nom_amt;
	private BigDecimal	r16_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg;
	private BigDecimal	r16_gpfsr_nom_amt1;
	private BigDecimal	r16_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg1;
	private BigDecimal	r16_gpfsr_nom_amt2;
	private BigDecimal	r16_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r16_gpfsr_chrg2;
	private BigDecimal	r16_tot_spe_ris_chrg;
	private BigDecimal	r16_net_pos_gen_mar_ris;
	private BigDecimal	r16_gen_mar_ris_chrg_8per;
	private BigDecimal	r16_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r16_tot_gen_mar_risk_chrg;
	private BigDecimal	r16_tot_mar_ris_chrg;
	
	private BigDecimal	r17_market;
	private BigDecimal	r17_gpfsr_nom_amt;
	private BigDecimal	r17_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg;
	private BigDecimal	r17_gpfsr_nom_amt1;
	private BigDecimal	r17_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg1;
	private BigDecimal	r17_gpfsr_nom_amt2;
	private BigDecimal	r17_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r17_gpfsr_chrg2;
	private BigDecimal	r17_tot_spe_ris_chrg;
	private BigDecimal	r17_net_pos_gen_mar_ris;
	private BigDecimal	r17_gen_mar_ris_chrg_8per;
	private BigDecimal	r17_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r17_tot_gen_mar_risk_chrg;
	private BigDecimal	r17_tot_mar_ris_chrg;
	
	private BigDecimal	r18_market;
	private BigDecimal	r18_gpfsr_nom_amt;
	private BigDecimal	r18_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg;
	private BigDecimal	r18_gpfsr_nom_amt1;
	private BigDecimal	r18_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg1;
	private BigDecimal	r18_gpfsr_nom_amt2;
	private BigDecimal	r18_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r18_gpfsr_chrg2;
	private BigDecimal	r18_tot_spe_ris_chrg;
	private BigDecimal	r18_net_pos_gen_mar_ris;
	private BigDecimal	r18_gen_mar_ris_chrg_8per;
	private BigDecimal	r18_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r18_tot_gen_mar_risk_chrg;
	private BigDecimal	r18_tot_mar_ris_chrg;
	
	private BigDecimal	r19_market;
	private BigDecimal	r19_gpfsr_nom_amt;
	private BigDecimal	r19_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg;
	private BigDecimal	r19_gpfsr_nom_amt1;
	private BigDecimal	r19_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg1;
	private BigDecimal	r19_gpfsr_nom_amt2;
	private BigDecimal	r19_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r19_gpfsr_chrg2;
	private BigDecimal	r19_tot_spe_ris_chrg;
	private BigDecimal	r19_net_pos_gen_mar_ris;
	private BigDecimal	r19_gen_mar_ris_chrg_8per;
	private BigDecimal	r19_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r19_tot_gen_mar_risk_chrg;
	private BigDecimal	r19_tot_mar_ris_chrg;
	
	private BigDecimal	r20_market;
	private BigDecimal	r20_gpfsr_nom_amt;
	private BigDecimal	r20_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg;
	private BigDecimal	r20_gpfsr_nom_amt1;
	private BigDecimal	r20_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg1;
	private BigDecimal	r20_gpfsr_nom_amt2;
	private BigDecimal	r20_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r20_gpfsr_chrg2;
	private BigDecimal	r20_tot_spe_ris_chrg;
	private BigDecimal	r20_net_pos_gen_mar_ris;
	private BigDecimal	r20_gen_mar_ris_chrg_8per;
	private BigDecimal	r20_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r20_tot_gen_mar_risk_chrg;
	private BigDecimal	r20_tot_mar_ris_chrg;
	
	private BigDecimal	r21_market;
	private BigDecimal	r21_gpfsr_nom_amt;
	private BigDecimal	r21_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg;
	private BigDecimal	r21_gpfsr_nom_amt1;
	private BigDecimal	r21_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg1;
	private BigDecimal	r21_gpfsr_nom_amt2;
	private BigDecimal	r21_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r21_gpfsr_chrg2;
	private BigDecimal	r21_tot_spe_ris_chrg;
	private BigDecimal	r21_net_pos_gen_mar_ris;
	private BigDecimal	r21_gen_mar_ris_chrg_8per;
	private BigDecimal	r21_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r21_tot_gen_mar_risk_chrg;
	private BigDecimal	r21_tot_mar_ris_chrg;
	
	private BigDecimal	r22_market;
	private BigDecimal	r22_gpfsr_nom_amt;
	private BigDecimal	r22_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg;
	private BigDecimal	r22_gpfsr_nom_amt1;
	private BigDecimal	r22_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg1;
	private BigDecimal	r22_gpfsr_nom_amt2;
	private BigDecimal	r22_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r22_gpfsr_chrg2;
	private BigDecimal	r22_tot_spe_ris_chrg;
	private BigDecimal	r22_net_pos_gen_mar_ris;
	private BigDecimal	r22_gen_mar_ris_chrg_8per;
	private BigDecimal	r22_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r22_tot_gen_mar_risk_chrg;
	private BigDecimal	r22_tot_mar_ris_chrg;
	
	private String	r23_market;
	private BigDecimal	r23_gpfsr_nom_amt;
	private BigDecimal	r23_gpfsr_pos_att8_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg;
	private BigDecimal	r23_gpfsr_nom_amt1;
	private BigDecimal	r23_gpfsr_pos_att4_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg1;
	private BigDecimal	r23_gpfsr_nom_amt2;
	private BigDecimal	r23_gpfsr_pos_att2_per_spe_ris;
	private BigDecimal	r23_gpfsr_chrg2;
	private BigDecimal	r23_tot_spe_ris_chrg;
	private BigDecimal	r23_net_pos_gen_mar_ris;
	private BigDecimal	r23_gen_mar_ris_chrg_8per;
	private BigDecimal	r23_2per_gen_mar_ris_chrg_div_port;
	private BigDecimal	r23_tot_gen_mar_risk_chrg;
	private BigDecimal	r23_tot_mar_ris_chrg;
	
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
	private String	 del_flg;
	public BigDecimal getR11_market() {
		return r11_market;
	}
	public void setR11_market(BigDecimal r11_market) {
		this.r11_market = r11_market;
	}
	public BigDecimal getR11_gpfsr_nom_amt() {
		return r11_gpfsr_nom_amt;
	}
	public void setR11_gpfsr_nom_amt(BigDecimal r11_gpfsr_nom_amt) {
		this.r11_gpfsr_nom_amt = r11_gpfsr_nom_amt;
	}
	public BigDecimal getR11_gpfsr_pos_att8_per_spe_ris() {
		return r11_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att8_per_spe_ris(BigDecimal r11_gpfsr_pos_att8_per_spe_ris) {
		this.r11_gpfsr_pos_att8_per_spe_ris = r11_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg() {
		return r11_gpfsr_chrg;
	}
	public void setR11_gpfsr_chrg(BigDecimal r11_gpfsr_chrg) {
		this.r11_gpfsr_chrg = r11_gpfsr_chrg;
	}
	public BigDecimal getR11_gpfsr_nom_amt1() {
		return r11_gpfsr_nom_amt1;
	}
	public void setR11_gpfsr_nom_amt1(BigDecimal r11_gpfsr_nom_amt1) {
		this.r11_gpfsr_nom_amt1 = r11_gpfsr_nom_amt1;
	}
	public BigDecimal getR11_gpfsr_pos_att4_per_spe_ris() {
		return r11_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att4_per_spe_ris(BigDecimal r11_gpfsr_pos_att4_per_spe_ris) {
		this.r11_gpfsr_pos_att4_per_spe_ris = r11_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg1() {
		return r11_gpfsr_chrg1;
	}
	public void setR11_gpfsr_chrg1(BigDecimal r11_gpfsr_chrg1) {
		this.r11_gpfsr_chrg1 = r11_gpfsr_chrg1;
	}
	public BigDecimal getR11_gpfsr_nom_amt2() {
		return r11_gpfsr_nom_amt2;
	}
	public void setR11_gpfsr_nom_amt2(BigDecimal r11_gpfsr_nom_amt2) {
		this.r11_gpfsr_nom_amt2 = r11_gpfsr_nom_amt2;
	}
	public BigDecimal getR11_gpfsr_pos_att2_per_spe_ris() {
		return r11_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR11_gpfsr_pos_att2_per_spe_ris(BigDecimal r11_gpfsr_pos_att2_per_spe_ris) {
		this.r11_gpfsr_pos_att2_per_spe_ris = r11_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR11_gpfsr_chrg2() {
		return r11_gpfsr_chrg2;
	}
	public void setR11_gpfsr_chrg2(BigDecimal r11_gpfsr_chrg2) {
		this.r11_gpfsr_chrg2 = r11_gpfsr_chrg2;
	}
	public BigDecimal getR11_tot_spe_ris_chrg() {
		return r11_tot_spe_ris_chrg;
	}
	public void setR11_tot_spe_ris_chrg(BigDecimal r11_tot_spe_ris_chrg) {
		this.r11_tot_spe_ris_chrg = r11_tot_spe_ris_chrg;
	}
	public BigDecimal getR11_net_pos_gen_mar_ris() {
		return r11_net_pos_gen_mar_ris;
	}
	public void setR11_net_pos_gen_mar_ris(BigDecimal r11_net_pos_gen_mar_ris) {
		this.r11_net_pos_gen_mar_ris = r11_net_pos_gen_mar_ris;
	}
	public BigDecimal getR11_gen_mar_ris_chrg_8per() {
		return r11_gen_mar_ris_chrg_8per;
	}
	public void setR11_gen_mar_ris_chrg_8per(BigDecimal r11_gen_mar_ris_chrg_8per) {
		this.r11_gen_mar_ris_chrg_8per = r11_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR11_2per_gen_mar_ris_chrg_div_port() {
		return r11_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR11_2per_gen_mar_ris_chrg_div_port(BigDecimal r11_2per_gen_mar_ris_chrg_div_port) {
		this.r11_2per_gen_mar_ris_chrg_div_port = r11_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR11_tot_gen_mar_risk_chrg() {
		return r11_tot_gen_mar_risk_chrg;
	}
	public void setR11_tot_gen_mar_risk_chrg(BigDecimal r11_tot_gen_mar_risk_chrg) {
		this.r11_tot_gen_mar_risk_chrg = r11_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR11_tot_mar_ris_chrg() {
		return r11_tot_mar_ris_chrg;
	}
	public void setR11_tot_mar_ris_chrg(BigDecimal r11_tot_mar_ris_chrg) {
		this.r11_tot_mar_ris_chrg = r11_tot_mar_ris_chrg;
	}
	public BigDecimal getR12_market() {
		return r12_market;
	}
	public void setR12_market(BigDecimal r12_market) {
		this.r12_market = r12_market;
	}
	public BigDecimal getR12_gpfsr_nom_amt() {
		return r12_gpfsr_nom_amt;
	}
	public void setR12_gpfsr_nom_amt(BigDecimal r12_gpfsr_nom_amt) {
		this.r12_gpfsr_nom_amt = r12_gpfsr_nom_amt;
	}
	public BigDecimal getR12_gpfsr_pos_att8_per_spe_ris() {
		return r12_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att8_per_spe_ris(BigDecimal r12_gpfsr_pos_att8_per_spe_ris) {
		this.r12_gpfsr_pos_att8_per_spe_ris = r12_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg() {
		return r12_gpfsr_chrg;
	}
	public void setR12_gpfsr_chrg(BigDecimal r12_gpfsr_chrg) {
		this.r12_gpfsr_chrg = r12_gpfsr_chrg;
	}
	public BigDecimal getR12_gpfsr_nom_amt1() {
		return r12_gpfsr_nom_amt1;
	}
	public void setR12_gpfsr_nom_amt1(BigDecimal r12_gpfsr_nom_amt1) {
		this.r12_gpfsr_nom_amt1 = r12_gpfsr_nom_amt1;
	}
	public BigDecimal getR12_gpfsr_pos_att4_per_spe_ris() {
		return r12_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att4_per_spe_ris(BigDecimal r12_gpfsr_pos_att4_per_spe_ris) {
		this.r12_gpfsr_pos_att4_per_spe_ris = r12_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg1() {
		return r12_gpfsr_chrg1;
	}
	public void setR12_gpfsr_chrg1(BigDecimal r12_gpfsr_chrg1) {
		this.r12_gpfsr_chrg1 = r12_gpfsr_chrg1;
	}
	public BigDecimal getR12_gpfsr_nom_amt2() {
		return r12_gpfsr_nom_amt2;
	}
	public void setR12_gpfsr_nom_amt2(BigDecimal r12_gpfsr_nom_amt2) {
		this.r12_gpfsr_nom_amt2 = r12_gpfsr_nom_amt2;
	}
	public BigDecimal getR12_gpfsr_pos_att2_per_spe_ris() {
		return r12_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR12_gpfsr_pos_att2_per_spe_ris(BigDecimal r12_gpfsr_pos_att2_per_spe_ris) {
		this.r12_gpfsr_pos_att2_per_spe_ris = r12_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR12_gpfsr_chrg2() {
		return r12_gpfsr_chrg2;
	}
	public void setR12_gpfsr_chrg2(BigDecimal r12_gpfsr_chrg2) {
		this.r12_gpfsr_chrg2 = r12_gpfsr_chrg2;
	}
	public BigDecimal getR12_tot_spe_ris_chrg() {
		return r12_tot_spe_ris_chrg;
	}
	public void setR12_tot_spe_ris_chrg(BigDecimal r12_tot_spe_ris_chrg) {
		this.r12_tot_spe_ris_chrg = r12_tot_spe_ris_chrg;
	}
	public BigDecimal getR12_net_pos_gen_mar_ris() {
		return r12_net_pos_gen_mar_ris;
	}
	public void setR12_net_pos_gen_mar_ris(BigDecimal r12_net_pos_gen_mar_ris) {
		this.r12_net_pos_gen_mar_ris = r12_net_pos_gen_mar_ris;
	}
	public BigDecimal getR12_gen_mar_ris_chrg_8per() {
		return r12_gen_mar_ris_chrg_8per;
	}
	public void setR12_gen_mar_ris_chrg_8per(BigDecimal r12_gen_mar_ris_chrg_8per) {
		this.r12_gen_mar_ris_chrg_8per = r12_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR12_2per_gen_mar_ris_chrg_div_port() {
		return r12_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR12_2per_gen_mar_ris_chrg_div_port(BigDecimal r12_2per_gen_mar_ris_chrg_div_port) {
		this.r12_2per_gen_mar_ris_chrg_div_port = r12_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR12_tot_gen_mar_risk_chrg() {
		return r12_tot_gen_mar_risk_chrg;
	}
	public void setR12_tot_gen_mar_risk_chrg(BigDecimal r12_tot_gen_mar_risk_chrg) {
		this.r12_tot_gen_mar_risk_chrg = r12_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR12_tot_mar_ris_chrg() {
		return r12_tot_mar_ris_chrg;
	}
	public void setR12_tot_mar_ris_chrg(BigDecimal r12_tot_mar_ris_chrg) {
		this.r12_tot_mar_ris_chrg = r12_tot_mar_ris_chrg;
	}
	public BigDecimal getR13_market() {
		return r13_market;
	}
	public void setR13_market(BigDecimal r13_market) {
		this.r13_market = r13_market;
	}
	public BigDecimal getR13_gpfsr_nom_amt() {
		return r13_gpfsr_nom_amt;
	}
	public void setR13_gpfsr_nom_amt(BigDecimal r13_gpfsr_nom_amt) {
		this.r13_gpfsr_nom_amt = r13_gpfsr_nom_amt;
	}
	public BigDecimal getR13_gpfsr_pos_att8_per_spe_ris() {
		return r13_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att8_per_spe_ris(BigDecimal r13_gpfsr_pos_att8_per_spe_ris) {
		this.r13_gpfsr_pos_att8_per_spe_ris = r13_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg() {
		return r13_gpfsr_chrg;
	}
	public void setR13_gpfsr_chrg(BigDecimal r13_gpfsr_chrg) {
		this.r13_gpfsr_chrg = r13_gpfsr_chrg;
	}
	public BigDecimal getR13_gpfsr_nom_amt1() {
		return r13_gpfsr_nom_amt1;
	}
	public void setR13_gpfsr_nom_amt1(BigDecimal r13_gpfsr_nom_amt1) {
		this.r13_gpfsr_nom_amt1 = r13_gpfsr_nom_amt1;
	}
	public BigDecimal getR13_gpfsr_pos_att4_per_spe_ris() {
		return r13_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att4_per_spe_ris(BigDecimal r13_gpfsr_pos_att4_per_spe_ris) {
		this.r13_gpfsr_pos_att4_per_spe_ris = r13_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg1() {
		return r13_gpfsr_chrg1;
	}
	public void setR13_gpfsr_chrg1(BigDecimal r13_gpfsr_chrg1) {
		this.r13_gpfsr_chrg1 = r13_gpfsr_chrg1;
	}
	public BigDecimal getR13_gpfsr_nom_amt2() {
		return r13_gpfsr_nom_amt2;
	}
	public void setR13_gpfsr_nom_amt2(BigDecimal r13_gpfsr_nom_amt2) {
		this.r13_gpfsr_nom_amt2 = r13_gpfsr_nom_amt2;
	}
	public BigDecimal getR13_gpfsr_pos_att2_per_spe_ris() {
		return r13_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR13_gpfsr_pos_att2_per_spe_ris(BigDecimal r13_gpfsr_pos_att2_per_spe_ris) {
		this.r13_gpfsr_pos_att2_per_spe_ris = r13_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR13_gpfsr_chrg2() {
		return r13_gpfsr_chrg2;
	}
	public void setR13_gpfsr_chrg2(BigDecimal r13_gpfsr_chrg2) {
		this.r13_gpfsr_chrg2 = r13_gpfsr_chrg2;
	}
	public BigDecimal getR13_tot_spe_ris_chrg() {
		return r13_tot_spe_ris_chrg;
	}
	public void setR13_tot_spe_ris_chrg(BigDecimal r13_tot_spe_ris_chrg) {
		this.r13_tot_spe_ris_chrg = r13_tot_spe_ris_chrg;
	}
	public BigDecimal getR13_net_pos_gen_mar_ris() {
		return r13_net_pos_gen_mar_ris;
	}
	public void setR13_net_pos_gen_mar_ris(BigDecimal r13_net_pos_gen_mar_ris) {
		this.r13_net_pos_gen_mar_ris = r13_net_pos_gen_mar_ris;
	}
	public BigDecimal getR13_gen_mar_ris_chrg_8per() {
		return r13_gen_mar_ris_chrg_8per;
	}
	public void setR13_gen_mar_ris_chrg_8per(BigDecimal r13_gen_mar_ris_chrg_8per) {
		this.r13_gen_mar_ris_chrg_8per = r13_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR13_2per_gen_mar_ris_chrg_div_port() {
		return r13_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR13_2per_gen_mar_ris_chrg_div_port(BigDecimal r13_2per_gen_mar_ris_chrg_div_port) {
		this.r13_2per_gen_mar_ris_chrg_div_port = r13_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR13_tot_gen_mar_risk_chrg() {
		return r13_tot_gen_mar_risk_chrg;
	}
	public void setR13_tot_gen_mar_risk_chrg(BigDecimal r13_tot_gen_mar_risk_chrg) {
		this.r13_tot_gen_mar_risk_chrg = r13_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR13_tot_mar_ris_chrg() {
		return r13_tot_mar_ris_chrg;
	}
	public void setR13_tot_mar_ris_chrg(BigDecimal r13_tot_mar_ris_chrg) {
		this.r13_tot_mar_ris_chrg = r13_tot_mar_ris_chrg;
	}
	public BigDecimal getR14_market() {
		return r14_market;
	}
	public void setR14_market(BigDecimal r14_market) {
		this.r14_market = r14_market;
	}
	public BigDecimal getR14_gpfsr_nom_amt() {
		return r14_gpfsr_nom_amt;
	}
	public void setR14_gpfsr_nom_amt(BigDecimal r14_gpfsr_nom_amt) {
		this.r14_gpfsr_nom_amt = r14_gpfsr_nom_amt;
	}
	public BigDecimal getR14_gpfsr_pos_att8_per_spe_ris() {
		return r14_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att8_per_spe_ris(BigDecimal r14_gpfsr_pos_att8_per_spe_ris) {
		this.r14_gpfsr_pos_att8_per_spe_ris = r14_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg() {
		return r14_gpfsr_chrg;
	}
	public void setR14_gpfsr_chrg(BigDecimal r14_gpfsr_chrg) {
		this.r14_gpfsr_chrg = r14_gpfsr_chrg;
	}
	public BigDecimal getR14_gpfsr_nom_amt1() {
		return r14_gpfsr_nom_amt1;
	}
	public void setR14_gpfsr_nom_amt1(BigDecimal r14_gpfsr_nom_amt1) {
		this.r14_gpfsr_nom_amt1 = r14_gpfsr_nom_amt1;
	}
	public BigDecimal getR14_gpfsr_pos_att4_per_spe_ris() {
		return r14_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att4_per_spe_ris(BigDecimal r14_gpfsr_pos_att4_per_spe_ris) {
		this.r14_gpfsr_pos_att4_per_spe_ris = r14_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg1() {
		return r14_gpfsr_chrg1;
	}
	public void setR14_gpfsr_chrg1(BigDecimal r14_gpfsr_chrg1) {
		this.r14_gpfsr_chrg1 = r14_gpfsr_chrg1;
	}
	public BigDecimal getR14_gpfsr_nom_amt2() {
		return r14_gpfsr_nom_amt2;
	}
	public void setR14_gpfsr_nom_amt2(BigDecimal r14_gpfsr_nom_amt2) {
		this.r14_gpfsr_nom_amt2 = r14_gpfsr_nom_amt2;
	}
	public BigDecimal getR14_gpfsr_pos_att2_per_spe_ris() {
		return r14_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR14_gpfsr_pos_att2_per_spe_ris(BigDecimal r14_gpfsr_pos_att2_per_spe_ris) {
		this.r14_gpfsr_pos_att2_per_spe_ris = r14_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR14_gpfsr_chrg2() {
		return r14_gpfsr_chrg2;
	}
	public void setR14_gpfsr_chrg2(BigDecimal r14_gpfsr_chrg2) {
		this.r14_gpfsr_chrg2 = r14_gpfsr_chrg2;
	}
	public BigDecimal getR14_tot_spe_ris_chrg() {
		return r14_tot_spe_ris_chrg;
	}
	public void setR14_tot_spe_ris_chrg(BigDecimal r14_tot_spe_ris_chrg) {
		this.r14_tot_spe_ris_chrg = r14_tot_spe_ris_chrg;
	}
	public BigDecimal getR14_net_pos_gen_mar_ris() {
		return r14_net_pos_gen_mar_ris;
	}
	public void setR14_net_pos_gen_mar_ris(BigDecimal r14_net_pos_gen_mar_ris) {
		this.r14_net_pos_gen_mar_ris = r14_net_pos_gen_mar_ris;
	}
	public BigDecimal getR14_gen_mar_ris_chrg_8per() {
		return r14_gen_mar_ris_chrg_8per;
	}
	public void setR14_gen_mar_ris_chrg_8per(BigDecimal r14_gen_mar_ris_chrg_8per) {
		this.r14_gen_mar_ris_chrg_8per = r14_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR14_2per_gen_mar_ris_chrg_div_port() {
		return r14_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR14_2per_gen_mar_ris_chrg_div_port(BigDecimal r14_2per_gen_mar_ris_chrg_div_port) {
		this.r14_2per_gen_mar_ris_chrg_div_port = r14_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR14_tot_gen_mar_risk_chrg() {
		return r14_tot_gen_mar_risk_chrg;
	}
	public void setR14_tot_gen_mar_risk_chrg(BigDecimal r14_tot_gen_mar_risk_chrg) {
		this.r14_tot_gen_mar_risk_chrg = r14_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR14_tot_mar_ris_chrg() {
		return r14_tot_mar_ris_chrg;
	}
	public void setR14_tot_mar_ris_chrg(BigDecimal r14_tot_mar_ris_chrg) {
		this.r14_tot_mar_ris_chrg = r14_tot_mar_ris_chrg;
	}
	public BigDecimal getR15_market() {
		return r15_market;
	}
	public void setR15_market(BigDecimal r15_market) {
		this.r15_market = r15_market;
	}
	public BigDecimal getR15_gpfsr_nom_amt() {
		return r15_gpfsr_nom_amt;
	}
	public void setR15_gpfsr_nom_amt(BigDecimal r15_gpfsr_nom_amt) {
		this.r15_gpfsr_nom_amt = r15_gpfsr_nom_amt;
	}
	public BigDecimal getR15_gpfsr_pos_att8_per_spe_ris() {
		return r15_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att8_per_spe_ris(BigDecimal r15_gpfsr_pos_att8_per_spe_ris) {
		this.r15_gpfsr_pos_att8_per_spe_ris = r15_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg() {
		return r15_gpfsr_chrg;
	}
	public void setR15_gpfsr_chrg(BigDecimal r15_gpfsr_chrg) {
		this.r15_gpfsr_chrg = r15_gpfsr_chrg;
	}
	public BigDecimal getR15_gpfsr_nom_amt1() {
		return r15_gpfsr_nom_amt1;
	}
	public void setR15_gpfsr_nom_amt1(BigDecimal r15_gpfsr_nom_amt1) {
		this.r15_gpfsr_nom_amt1 = r15_gpfsr_nom_amt1;
	}
	public BigDecimal getR15_gpfsr_pos_att4_per_spe_ris() {
		return r15_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att4_per_spe_ris(BigDecimal r15_gpfsr_pos_att4_per_spe_ris) {
		this.r15_gpfsr_pos_att4_per_spe_ris = r15_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg1() {
		return r15_gpfsr_chrg1;
	}
	public void setR15_gpfsr_chrg1(BigDecimal r15_gpfsr_chrg1) {
		this.r15_gpfsr_chrg1 = r15_gpfsr_chrg1;
	}
	public BigDecimal getR15_gpfsr_nom_amt2() {
		return r15_gpfsr_nom_amt2;
	}
	public void setR15_gpfsr_nom_amt2(BigDecimal r15_gpfsr_nom_amt2) {
		this.r15_gpfsr_nom_amt2 = r15_gpfsr_nom_amt2;
	}
	public BigDecimal getR15_gpfsr_pos_att2_per_spe_ris() {
		return r15_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR15_gpfsr_pos_att2_per_spe_ris(BigDecimal r15_gpfsr_pos_att2_per_spe_ris) {
		this.r15_gpfsr_pos_att2_per_spe_ris = r15_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR15_gpfsr_chrg2() {
		return r15_gpfsr_chrg2;
	}
	public void setR15_gpfsr_chrg2(BigDecimal r15_gpfsr_chrg2) {
		this.r15_gpfsr_chrg2 = r15_gpfsr_chrg2;
	}
	public BigDecimal getR15_tot_spe_ris_chrg() {
		return r15_tot_spe_ris_chrg;
	}
	public void setR15_tot_spe_ris_chrg(BigDecimal r15_tot_spe_ris_chrg) {
		this.r15_tot_spe_ris_chrg = r15_tot_spe_ris_chrg;
	}
	public BigDecimal getR15_net_pos_gen_mar_ris() {
		return r15_net_pos_gen_mar_ris;
	}
	public void setR15_net_pos_gen_mar_ris(BigDecimal r15_net_pos_gen_mar_ris) {
		this.r15_net_pos_gen_mar_ris = r15_net_pos_gen_mar_ris;
	}
	public BigDecimal getR15_gen_mar_ris_chrg_8per() {
		return r15_gen_mar_ris_chrg_8per;
	}
	public void setR15_gen_mar_ris_chrg_8per(BigDecimal r15_gen_mar_ris_chrg_8per) {
		this.r15_gen_mar_ris_chrg_8per = r15_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR15_2per_gen_mar_ris_chrg_div_port() {
		return r15_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR15_2per_gen_mar_ris_chrg_div_port(BigDecimal r15_2per_gen_mar_ris_chrg_div_port) {
		this.r15_2per_gen_mar_ris_chrg_div_port = r15_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR15_tot_gen_mar_risk_chrg() {
		return r15_tot_gen_mar_risk_chrg;
	}
	public void setR15_tot_gen_mar_risk_chrg(BigDecimal r15_tot_gen_mar_risk_chrg) {
		this.r15_tot_gen_mar_risk_chrg = r15_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR15_tot_mar_ris_chrg() {
		return r15_tot_mar_ris_chrg;
	}
	public void setR15_tot_mar_ris_chrg(BigDecimal r15_tot_mar_ris_chrg) {
		this.r15_tot_mar_ris_chrg = r15_tot_mar_ris_chrg;
	}
	public BigDecimal getR16_market() {
		return r16_market;
	}
	public void setR16_market(BigDecimal r16_market) {
		this.r16_market = r16_market;
	}
	public BigDecimal getR16_gpfsr_nom_amt() {
		return r16_gpfsr_nom_amt;
	}
	public void setR16_gpfsr_nom_amt(BigDecimal r16_gpfsr_nom_amt) {
		this.r16_gpfsr_nom_amt = r16_gpfsr_nom_amt;
	}
	public BigDecimal getR16_gpfsr_pos_att8_per_spe_ris() {
		return r16_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att8_per_spe_ris(BigDecimal r16_gpfsr_pos_att8_per_spe_ris) {
		this.r16_gpfsr_pos_att8_per_spe_ris = r16_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg() {
		return r16_gpfsr_chrg;
	}
	public void setR16_gpfsr_chrg(BigDecimal r16_gpfsr_chrg) {
		this.r16_gpfsr_chrg = r16_gpfsr_chrg;
	}
	public BigDecimal getR16_gpfsr_nom_amt1() {
		return r16_gpfsr_nom_amt1;
	}
	public void setR16_gpfsr_nom_amt1(BigDecimal r16_gpfsr_nom_amt1) {
		this.r16_gpfsr_nom_amt1 = r16_gpfsr_nom_amt1;
	}
	public BigDecimal getR16_gpfsr_pos_att4_per_spe_ris() {
		return r16_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att4_per_spe_ris(BigDecimal r16_gpfsr_pos_att4_per_spe_ris) {
		this.r16_gpfsr_pos_att4_per_spe_ris = r16_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg1() {
		return r16_gpfsr_chrg1;
	}
	public void setR16_gpfsr_chrg1(BigDecimal r16_gpfsr_chrg1) {
		this.r16_gpfsr_chrg1 = r16_gpfsr_chrg1;
	}
	public BigDecimal getR16_gpfsr_nom_amt2() {
		return r16_gpfsr_nom_amt2;
	}
	public void setR16_gpfsr_nom_amt2(BigDecimal r16_gpfsr_nom_amt2) {
		this.r16_gpfsr_nom_amt2 = r16_gpfsr_nom_amt2;
	}
	public BigDecimal getR16_gpfsr_pos_att2_per_spe_ris() {
		return r16_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR16_gpfsr_pos_att2_per_spe_ris(BigDecimal r16_gpfsr_pos_att2_per_spe_ris) {
		this.r16_gpfsr_pos_att2_per_spe_ris = r16_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR16_gpfsr_chrg2() {
		return r16_gpfsr_chrg2;
	}
	public void setR16_gpfsr_chrg2(BigDecimal r16_gpfsr_chrg2) {
		this.r16_gpfsr_chrg2 = r16_gpfsr_chrg2;
	}
	public BigDecimal getR16_tot_spe_ris_chrg() {
		return r16_tot_spe_ris_chrg;
	}
	public void setR16_tot_spe_ris_chrg(BigDecimal r16_tot_spe_ris_chrg) {
		this.r16_tot_spe_ris_chrg = r16_tot_spe_ris_chrg;
	}
	public BigDecimal getR16_net_pos_gen_mar_ris() {
		return r16_net_pos_gen_mar_ris;
	}
	public void setR16_net_pos_gen_mar_ris(BigDecimal r16_net_pos_gen_mar_ris) {
		this.r16_net_pos_gen_mar_ris = r16_net_pos_gen_mar_ris;
	}
	public BigDecimal getR16_gen_mar_ris_chrg_8per() {
		return r16_gen_mar_ris_chrg_8per;
	}
	public void setR16_gen_mar_ris_chrg_8per(BigDecimal r16_gen_mar_ris_chrg_8per) {
		this.r16_gen_mar_ris_chrg_8per = r16_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR16_2per_gen_mar_ris_chrg_div_port() {
		return r16_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR16_2per_gen_mar_ris_chrg_div_port(BigDecimal r16_2per_gen_mar_ris_chrg_div_port) {
		this.r16_2per_gen_mar_ris_chrg_div_port = r16_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR16_tot_gen_mar_risk_chrg() {
		return r16_tot_gen_mar_risk_chrg;
	}
	public void setR16_tot_gen_mar_risk_chrg(BigDecimal r16_tot_gen_mar_risk_chrg) {
		this.r16_tot_gen_mar_risk_chrg = r16_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR16_tot_mar_ris_chrg() {
		return r16_tot_mar_ris_chrg;
	}
	public void setR16_tot_mar_ris_chrg(BigDecimal r16_tot_mar_ris_chrg) {
		this.r16_tot_mar_ris_chrg = r16_tot_mar_ris_chrg;
	}
	public BigDecimal getR17_market() {
		return r17_market;
	}
	public void setR17_market(BigDecimal r17_market) {
		this.r17_market = r17_market;
	}
	public BigDecimal getR17_gpfsr_nom_amt() {
		return r17_gpfsr_nom_amt;
	}
	public void setR17_gpfsr_nom_amt(BigDecimal r17_gpfsr_nom_amt) {
		this.r17_gpfsr_nom_amt = r17_gpfsr_nom_amt;
	}
	public BigDecimal getR17_gpfsr_pos_att8_per_spe_ris() {
		return r17_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att8_per_spe_ris(BigDecimal r17_gpfsr_pos_att8_per_spe_ris) {
		this.r17_gpfsr_pos_att8_per_spe_ris = r17_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg() {
		return r17_gpfsr_chrg;
	}
	public void setR17_gpfsr_chrg(BigDecimal r17_gpfsr_chrg) {
		this.r17_gpfsr_chrg = r17_gpfsr_chrg;
	}
	public BigDecimal getR17_gpfsr_nom_amt1() {
		return r17_gpfsr_nom_amt1;
	}
	public void setR17_gpfsr_nom_amt1(BigDecimal r17_gpfsr_nom_amt1) {
		this.r17_gpfsr_nom_amt1 = r17_gpfsr_nom_amt1;
	}
	public BigDecimal getR17_gpfsr_pos_att4_per_spe_ris() {
		return r17_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att4_per_spe_ris(BigDecimal r17_gpfsr_pos_att4_per_spe_ris) {
		this.r17_gpfsr_pos_att4_per_spe_ris = r17_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg1() {
		return r17_gpfsr_chrg1;
	}
	public void setR17_gpfsr_chrg1(BigDecimal r17_gpfsr_chrg1) {
		this.r17_gpfsr_chrg1 = r17_gpfsr_chrg1;
	}
	public BigDecimal getR17_gpfsr_nom_amt2() {
		return r17_gpfsr_nom_amt2;
	}
	public void setR17_gpfsr_nom_amt2(BigDecimal r17_gpfsr_nom_amt2) {
		this.r17_gpfsr_nom_amt2 = r17_gpfsr_nom_amt2;
	}
	public BigDecimal getR17_gpfsr_pos_att2_per_spe_ris() {
		return r17_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR17_gpfsr_pos_att2_per_spe_ris(BigDecimal r17_gpfsr_pos_att2_per_spe_ris) {
		this.r17_gpfsr_pos_att2_per_spe_ris = r17_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR17_gpfsr_chrg2() {
		return r17_gpfsr_chrg2;
	}
	public void setR17_gpfsr_chrg2(BigDecimal r17_gpfsr_chrg2) {
		this.r17_gpfsr_chrg2 = r17_gpfsr_chrg2;
	}
	public BigDecimal getR17_tot_spe_ris_chrg() {
		return r17_tot_spe_ris_chrg;
	}
	public void setR17_tot_spe_ris_chrg(BigDecimal r17_tot_spe_ris_chrg) {
		this.r17_tot_spe_ris_chrg = r17_tot_spe_ris_chrg;
	}
	public BigDecimal getR17_net_pos_gen_mar_ris() {
		return r17_net_pos_gen_mar_ris;
	}
	public void setR17_net_pos_gen_mar_ris(BigDecimal r17_net_pos_gen_mar_ris) {
		this.r17_net_pos_gen_mar_ris = r17_net_pos_gen_mar_ris;
	}
	public BigDecimal getR17_gen_mar_ris_chrg_8per() {
		return r17_gen_mar_ris_chrg_8per;
	}
	public void setR17_gen_mar_ris_chrg_8per(BigDecimal r17_gen_mar_ris_chrg_8per) {
		this.r17_gen_mar_ris_chrg_8per = r17_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR17_2per_gen_mar_ris_chrg_div_port() {
		return r17_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR17_2per_gen_mar_ris_chrg_div_port(BigDecimal r17_2per_gen_mar_ris_chrg_div_port) {
		this.r17_2per_gen_mar_ris_chrg_div_port = r17_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR17_tot_gen_mar_risk_chrg() {
		return r17_tot_gen_mar_risk_chrg;
	}
	public void setR17_tot_gen_mar_risk_chrg(BigDecimal r17_tot_gen_mar_risk_chrg) {
		this.r17_tot_gen_mar_risk_chrg = r17_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR17_tot_mar_ris_chrg() {
		return r17_tot_mar_ris_chrg;
	}
	public void setR17_tot_mar_ris_chrg(BigDecimal r17_tot_mar_ris_chrg) {
		this.r17_tot_mar_ris_chrg = r17_tot_mar_ris_chrg;
	}
	public BigDecimal getR18_market() {
		return r18_market;
	}
	public void setR18_market(BigDecimal r18_market) {
		this.r18_market = r18_market;
	}
	public BigDecimal getR18_gpfsr_nom_amt() {
		return r18_gpfsr_nom_amt;
	}
	public void setR18_gpfsr_nom_amt(BigDecimal r18_gpfsr_nom_amt) {
		this.r18_gpfsr_nom_amt = r18_gpfsr_nom_amt;
	}
	public BigDecimal getR18_gpfsr_pos_att8_per_spe_ris() {
		return r18_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att8_per_spe_ris(BigDecimal r18_gpfsr_pos_att8_per_spe_ris) {
		this.r18_gpfsr_pos_att8_per_spe_ris = r18_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg() {
		return r18_gpfsr_chrg;
	}
	public void setR18_gpfsr_chrg(BigDecimal r18_gpfsr_chrg) {
		this.r18_gpfsr_chrg = r18_gpfsr_chrg;
	}
	public BigDecimal getR18_gpfsr_nom_amt1() {
		return r18_gpfsr_nom_amt1;
	}
	public void setR18_gpfsr_nom_amt1(BigDecimal r18_gpfsr_nom_amt1) {
		this.r18_gpfsr_nom_amt1 = r18_gpfsr_nom_amt1;
	}
	public BigDecimal getR18_gpfsr_pos_att4_per_spe_ris() {
		return r18_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att4_per_spe_ris(BigDecimal r18_gpfsr_pos_att4_per_spe_ris) {
		this.r18_gpfsr_pos_att4_per_spe_ris = r18_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg1() {
		return r18_gpfsr_chrg1;
	}
	public void setR18_gpfsr_chrg1(BigDecimal r18_gpfsr_chrg1) {
		this.r18_gpfsr_chrg1 = r18_gpfsr_chrg1;
	}
	public BigDecimal getR18_gpfsr_nom_amt2() {
		return r18_gpfsr_nom_amt2;
	}
	public void setR18_gpfsr_nom_amt2(BigDecimal r18_gpfsr_nom_amt2) {
		this.r18_gpfsr_nom_amt2 = r18_gpfsr_nom_amt2;
	}
	public BigDecimal getR18_gpfsr_pos_att2_per_spe_ris() {
		return r18_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR18_gpfsr_pos_att2_per_spe_ris(BigDecimal r18_gpfsr_pos_att2_per_spe_ris) {
		this.r18_gpfsr_pos_att2_per_spe_ris = r18_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR18_gpfsr_chrg2() {
		return r18_gpfsr_chrg2;
	}
	public void setR18_gpfsr_chrg2(BigDecimal r18_gpfsr_chrg2) {
		this.r18_gpfsr_chrg2 = r18_gpfsr_chrg2;
	}
	public BigDecimal getR18_tot_spe_ris_chrg() {
		return r18_tot_spe_ris_chrg;
	}
	public void setR18_tot_spe_ris_chrg(BigDecimal r18_tot_spe_ris_chrg) {
		this.r18_tot_spe_ris_chrg = r18_tot_spe_ris_chrg;
	}
	public BigDecimal getR18_net_pos_gen_mar_ris() {
		return r18_net_pos_gen_mar_ris;
	}
	public void setR18_net_pos_gen_mar_ris(BigDecimal r18_net_pos_gen_mar_ris) {
		this.r18_net_pos_gen_mar_ris = r18_net_pos_gen_mar_ris;
	}
	public BigDecimal getR18_gen_mar_ris_chrg_8per() {
		return r18_gen_mar_ris_chrg_8per;
	}
	public void setR18_gen_mar_ris_chrg_8per(BigDecimal r18_gen_mar_ris_chrg_8per) {
		this.r18_gen_mar_ris_chrg_8per = r18_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR18_2per_gen_mar_ris_chrg_div_port() {
		return r18_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR18_2per_gen_mar_ris_chrg_div_port(BigDecimal r18_2per_gen_mar_ris_chrg_div_port) {
		this.r18_2per_gen_mar_ris_chrg_div_port = r18_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR18_tot_gen_mar_risk_chrg() {
		return r18_tot_gen_mar_risk_chrg;
	}
	public void setR18_tot_gen_mar_risk_chrg(BigDecimal r18_tot_gen_mar_risk_chrg) {
		this.r18_tot_gen_mar_risk_chrg = r18_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR18_tot_mar_ris_chrg() {
		return r18_tot_mar_ris_chrg;
	}
	public void setR18_tot_mar_ris_chrg(BigDecimal r18_tot_mar_ris_chrg) {
		this.r18_tot_mar_ris_chrg = r18_tot_mar_ris_chrg;
	}
	public BigDecimal getR19_market() {
		return r19_market;
	}
	public void setR19_market(BigDecimal r19_market) {
		this.r19_market = r19_market;
	}
	public BigDecimal getR19_gpfsr_nom_amt() {
		return r19_gpfsr_nom_amt;
	}
	public void setR19_gpfsr_nom_amt(BigDecimal r19_gpfsr_nom_amt) {
		this.r19_gpfsr_nom_amt = r19_gpfsr_nom_amt;
	}
	public BigDecimal getR19_gpfsr_pos_att8_per_spe_ris() {
		return r19_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att8_per_spe_ris(BigDecimal r19_gpfsr_pos_att8_per_spe_ris) {
		this.r19_gpfsr_pos_att8_per_spe_ris = r19_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg() {
		return r19_gpfsr_chrg;
	}
	public void setR19_gpfsr_chrg(BigDecimal r19_gpfsr_chrg) {
		this.r19_gpfsr_chrg = r19_gpfsr_chrg;
	}
	public BigDecimal getR19_gpfsr_nom_amt1() {
		return r19_gpfsr_nom_amt1;
	}
	public void setR19_gpfsr_nom_amt1(BigDecimal r19_gpfsr_nom_amt1) {
		this.r19_gpfsr_nom_amt1 = r19_gpfsr_nom_amt1;
	}
	public BigDecimal getR19_gpfsr_pos_att4_per_spe_ris() {
		return r19_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att4_per_spe_ris(BigDecimal r19_gpfsr_pos_att4_per_spe_ris) {
		this.r19_gpfsr_pos_att4_per_spe_ris = r19_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg1() {
		return r19_gpfsr_chrg1;
	}
	public void setR19_gpfsr_chrg1(BigDecimal r19_gpfsr_chrg1) {
		this.r19_gpfsr_chrg1 = r19_gpfsr_chrg1;
	}
	public BigDecimal getR19_gpfsr_nom_amt2() {
		return r19_gpfsr_nom_amt2;
	}
	public void setR19_gpfsr_nom_amt2(BigDecimal r19_gpfsr_nom_amt2) {
		this.r19_gpfsr_nom_amt2 = r19_gpfsr_nom_amt2;
	}
	public BigDecimal getR19_gpfsr_pos_att2_per_spe_ris() {
		return r19_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR19_gpfsr_pos_att2_per_spe_ris(BigDecimal r19_gpfsr_pos_att2_per_spe_ris) {
		this.r19_gpfsr_pos_att2_per_spe_ris = r19_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR19_gpfsr_chrg2() {
		return r19_gpfsr_chrg2;
	}
	public void setR19_gpfsr_chrg2(BigDecimal r19_gpfsr_chrg2) {
		this.r19_gpfsr_chrg2 = r19_gpfsr_chrg2;
	}
	public BigDecimal getR19_tot_spe_ris_chrg() {
		return r19_tot_spe_ris_chrg;
	}
	public void setR19_tot_spe_ris_chrg(BigDecimal r19_tot_spe_ris_chrg) {
		this.r19_tot_spe_ris_chrg = r19_tot_spe_ris_chrg;
	}
	public BigDecimal getR19_net_pos_gen_mar_ris() {
		return r19_net_pos_gen_mar_ris;
	}
	public void setR19_net_pos_gen_mar_ris(BigDecimal r19_net_pos_gen_mar_ris) {
		this.r19_net_pos_gen_mar_ris = r19_net_pos_gen_mar_ris;
	}
	public BigDecimal getR19_gen_mar_ris_chrg_8per() {
		return r19_gen_mar_ris_chrg_8per;
	}
	public void setR19_gen_mar_ris_chrg_8per(BigDecimal r19_gen_mar_ris_chrg_8per) {
		this.r19_gen_mar_ris_chrg_8per = r19_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR19_2per_gen_mar_ris_chrg_div_port() {
		return r19_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR19_2per_gen_mar_ris_chrg_div_port(BigDecimal r19_2per_gen_mar_ris_chrg_div_port) {
		this.r19_2per_gen_mar_ris_chrg_div_port = r19_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR19_tot_gen_mar_risk_chrg() {
		return r19_tot_gen_mar_risk_chrg;
	}
	public void setR19_tot_gen_mar_risk_chrg(BigDecimal r19_tot_gen_mar_risk_chrg) {
		this.r19_tot_gen_mar_risk_chrg = r19_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR19_tot_mar_ris_chrg() {
		return r19_tot_mar_ris_chrg;
	}
	public void setR19_tot_mar_ris_chrg(BigDecimal r19_tot_mar_ris_chrg) {
		this.r19_tot_mar_ris_chrg = r19_tot_mar_ris_chrg;
	}
	public BigDecimal getR20_market() {
		return r20_market;
	}
	public void setR20_market(BigDecimal r20_market) {
		this.r20_market = r20_market;
	}
	public BigDecimal getR20_gpfsr_nom_amt() {
		return r20_gpfsr_nom_amt;
	}
	public void setR20_gpfsr_nom_amt(BigDecimal r20_gpfsr_nom_amt) {
		this.r20_gpfsr_nom_amt = r20_gpfsr_nom_amt;
	}
	public BigDecimal getR20_gpfsr_pos_att8_per_spe_ris() {
		return r20_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att8_per_spe_ris(BigDecimal r20_gpfsr_pos_att8_per_spe_ris) {
		this.r20_gpfsr_pos_att8_per_spe_ris = r20_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg() {
		return r20_gpfsr_chrg;
	}
	public void setR20_gpfsr_chrg(BigDecimal r20_gpfsr_chrg) {
		this.r20_gpfsr_chrg = r20_gpfsr_chrg;
	}
	public BigDecimal getR20_gpfsr_nom_amt1() {
		return r20_gpfsr_nom_amt1;
	}
	public void setR20_gpfsr_nom_amt1(BigDecimal r20_gpfsr_nom_amt1) {
		this.r20_gpfsr_nom_amt1 = r20_gpfsr_nom_amt1;
	}
	public BigDecimal getR20_gpfsr_pos_att4_per_spe_ris() {
		return r20_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att4_per_spe_ris(BigDecimal r20_gpfsr_pos_att4_per_spe_ris) {
		this.r20_gpfsr_pos_att4_per_spe_ris = r20_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg1() {
		return r20_gpfsr_chrg1;
	}
	public void setR20_gpfsr_chrg1(BigDecimal r20_gpfsr_chrg1) {
		this.r20_gpfsr_chrg1 = r20_gpfsr_chrg1;
	}
	public BigDecimal getR20_gpfsr_nom_amt2() {
		return r20_gpfsr_nom_amt2;
	}
	public void setR20_gpfsr_nom_amt2(BigDecimal r20_gpfsr_nom_amt2) {
		this.r20_gpfsr_nom_amt2 = r20_gpfsr_nom_amt2;
	}
	public BigDecimal getR20_gpfsr_pos_att2_per_spe_ris() {
		return r20_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR20_gpfsr_pos_att2_per_spe_ris(BigDecimal r20_gpfsr_pos_att2_per_spe_ris) {
		this.r20_gpfsr_pos_att2_per_spe_ris = r20_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR20_gpfsr_chrg2() {
		return r20_gpfsr_chrg2;
	}
	public void setR20_gpfsr_chrg2(BigDecimal r20_gpfsr_chrg2) {
		this.r20_gpfsr_chrg2 = r20_gpfsr_chrg2;
	}
	public BigDecimal getR20_tot_spe_ris_chrg() {
		return r20_tot_spe_ris_chrg;
	}
	public void setR20_tot_spe_ris_chrg(BigDecimal r20_tot_spe_ris_chrg) {
		this.r20_tot_spe_ris_chrg = r20_tot_spe_ris_chrg;
	}
	public BigDecimal getR20_net_pos_gen_mar_ris() {
		return r20_net_pos_gen_mar_ris;
	}
	public void setR20_net_pos_gen_mar_ris(BigDecimal r20_net_pos_gen_mar_ris) {
		this.r20_net_pos_gen_mar_ris = r20_net_pos_gen_mar_ris;
	}
	public BigDecimal getR20_gen_mar_ris_chrg_8per() {
		return r20_gen_mar_ris_chrg_8per;
	}
	public void setR20_gen_mar_ris_chrg_8per(BigDecimal r20_gen_mar_ris_chrg_8per) {
		this.r20_gen_mar_ris_chrg_8per = r20_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR20_2per_gen_mar_ris_chrg_div_port() {
		return r20_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR20_2per_gen_mar_ris_chrg_div_port(BigDecimal r20_2per_gen_mar_ris_chrg_div_port) {
		this.r20_2per_gen_mar_ris_chrg_div_port = r20_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR20_tot_gen_mar_risk_chrg() {
		return r20_tot_gen_mar_risk_chrg;
	}
	public void setR20_tot_gen_mar_risk_chrg(BigDecimal r20_tot_gen_mar_risk_chrg) {
		this.r20_tot_gen_mar_risk_chrg = r20_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR20_tot_mar_ris_chrg() {
		return r20_tot_mar_ris_chrg;
	}
	public void setR20_tot_mar_ris_chrg(BigDecimal r20_tot_mar_ris_chrg) {
		this.r20_tot_mar_ris_chrg = r20_tot_mar_ris_chrg;
	}
	public BigDecimal getR21_market() {
		return r21_market;
	}
	public void setR21_market(BigDecimal r21_market) {
		this.r21_market = r21_market;
	}
	public BigDecimal getR21_gpfsr_nom_amt() {
		return r21_gpfsr_nom_amt;
	}
	public void setR21_gpfsr_nom_amt(BigDecimal r21_gpfsr_nom_amt) {
		this.r21_gpfsr_nom_amt = r21_gpfsr_nom_amt;
	}
	public BigDecimal getR21_gpfsr_pos_att8_per_spe_ris() {
		return r21_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att8_per_spe_ris(BigDecimal r21_gpfsr_pos_att8_per_spe_ris) {
		this.r21_gpfsr_pos_att8_per_spe_ris = r21_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg() {
		return r21_gpfsr_chrg;
	}
	public void setR21_gpfsr_chrg(BigDecimal r21_gpfsr_chrg) {
		this.r21_gpfsr_chrg = r21_gpfsr_chrg;
	}
	public BigDecimal getR21_gpfsr_nom_amt1() {
		return r21_gpfsr_nom_amt1;
	}
	public void setR21_gpfsr_nom_amt1(BigDecimal r21_gpfsr_nom_amt1) {
		this.r21_gpfsr_nom_amt1 = r21_gpfsr_nom_amt1;
	}
	public BigDecimal getR21_gpfsr_pos_att4_per_spe_ris() {
		return r21_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att4_per_spe_ris(BigDecimal r21_gpfsr_pos_att4_per_spe_ris) {
		this.r21_gpfsr_pos_att4_per_spe_ris = r21_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg1() {
		return r21_gpfsr_chrg1;
	}
	public void setR21_gpfsr_chrg1(BigDecimal r21_gpfsr_chrg1) {
		this.r21_gpfsr_chrg1 = r21_gpfsr_chrg1;
	}
	public BigDecimal getR21_gpfsr_nom_amt2() {
		return r21_gpfsr_nom_amt2;
	}
	public void setR21_gpfsr_nom_amt2(BigDecimal r21_gpfsr_nom_amt2) {
		this.r21_gpfsr_nom_amt2 = r21_gpfsr_nom_amt2;
	}
	public BigDecimal getR21_gpfsr_pos_att2_per_spe_ris() {
		return r21_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR21_gpfsr_pos_att2_per_spe_ris(BigDecimal r21_gpfsr_pos_att2_per_spe_ris) {
		this.r21_gpfsr_pos_att2_per_spe_ris = r21_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR21_gpfsr_chrg2() {
		return r21_gpfsr_chrg2;
	}
	public void setR21_gpfsr_chrg2(BigDecimal r21_gpfsr_chrg2) {
		this.r21_gpfsr_chrg2 = r21_gpfsr_chrg2;
	}
	public BigDecimal getR21_tot_spe_ris_chrg() {
		return r21_tot_spe_ris_chrg;
	}
	public void setR21_tot_spe_ris_chrg(BigDecimal r21_tot_spe_ris_chrg) {
		this.r21_tot_spe_ris_chrg = r21_tot_spe_ris_chrg;
	}
	public BigDecimal getR21_net_pos_gen_mar_ris() {
		return r21_net_pos_gen_mar_ris;
	}
	public void setR21_net_pos_gen_mar_ris(BigDecimal r21_net_pos_gen_mar_ris) {
		this.r21_net_pos_gen_mar_ris = r21_net_pos_gen_mar_ris;
	}
	public BigDecimal getR21_gen_mar_ris_chrg_8per() {
		return r21_gen_mar_ris_chrg_8per;
	}
	public void setR21_gen_mar_ris_chrg_8per(BigDecimal r21_gen_mar_ris_chrg_8per) {
		this.r21_gen_mar_ris_chrg_8per = r21_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR21_2per_gen_mar_ris_chrg_div_port() {
		return r21_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR21_2per_gen_mar_ris_chrg_div_port(BigDecimal r21_2per_gen_mar_ris_chrg_div_port) {
		this.r21_2per_gen_mar_ris_chrg_div_port = r21_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR21_tot_gen_mar_risk_chrg() {
		return r21_tot_gen_mar_risk_chrg;
	}
	public void setR21_tot_gen_mar_risk_chrg(BigDecimal r21_tot_gen_mar_risk_chrg) {
		this.r21_tot_gen_mar_risk_chrg = r21_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR21_tot_mar_ris_chrg() {
		return r21_tot_mar_ris_chrg;
	}
	public void setR21_tot_mar_ris_chrg(BigDecimal r21_tot_mar_ris_chrg) {
		this.r21_tot_mar_ris_chrg = r21_tot_mar_ris_chrg;
	}
	public BigDecimal getR22_market() {
		return r22_market;
	}
	public void setR22_market(BigDecimal r22_market) {
		this.r22_market = r22_market;
	}
	public BigDecimal getR22_gpfsr_nom_amt() {
		return r22_gpfsr_nom_amt;
	}
	public void setR22_gpfsr_nom_amt(BigDecimal r22_gpfsr_nom_amt) {
		this.r22_gpfsr_nom_amt = r22_gpfsr_nom_amt;
	}
	public BigDecimal getR22_gpfsr_pos_att8_per_spe_ris() {
		return r22_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att8_per_spe_ris(BigDecimal r22_gpfsr_pos_att8_per_spe_ris) {
		this.r22_gpfsr_pos_att8_per_spe_ris = r22_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg() {
		return r22_gpfsr_chrg;
	}
	public void setR22_gpfsr_chrg(BigDecimal r22_gpfsr_chrg) {
		this.r22_gpfsr_chrg = r22_gpfsr_chrg;
	}
	public BigDecimal getR22_gpfsr_nom_amt1() {
		return r22_gpfsr_nom_amt1;
	}
	public void setR22_gpfsr_nom_amt1(BigDecimal r22_gpfsr_nom_amt1) {
		this.r22_gpfsr_nom_amt1 = r22_gpfsr_nom_amt1;
	}
	public BigDecimal getR22_gpfsr_pos_att4_per_spe_ris() {
		return r22_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att4_per_spe_ris(BigDecimal r22_gpfsr_pos_att4_per_spe_ris) {
		this.r22_gpfsr_pos_att4_per_spe_ris = r22_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg1() {
		return r22_gpfsr_chrg1;
	}
	public void setR22_gpfsr_chrg1(BigDecimal r22_gpfsr_chrg1) {
		this.r22_gpfsr_chrg1 = r22_gpfsr_chrg1;
	}
	public BigDecimal getR22_gpfsr_nom_amt2() {
		return r22_gpfsr_nom_amt2;
	}
	public void setR22_gpfsr_nom_amt2(BigDecimal r22_gpfsr_nom_amt2) {
		this.r22_gpfsr_nom_amt2 = r22_gpfsr_nom_amt2;
	}
	public BigDecimal getR22_gpfsr_pos_att2_per_spe_ris() {
		return r22_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR22_gpfsr_pos_att2_per_spe_ris(BigDecimal r22_gpfsr_pos_att2_per_spe_ris) {
		this.r22_gpfsr_pos_att2_per_spe_ris = r22_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR22_gpfsr_chrg2() {
		return r22_gpfsr_chrg2;
	}
	public void setR22_gpfsr_chrg2(BigDecimal r22_gpfsr_chrg2) {
		this.r22_gpfsr_chrg2 = r22_gpfsr_chrg2;
	}
	public BigDecimal getR22_tot_spe_ris_chrg() {
		return r22_tot_spe_ris_chrg;
	}
	public void setR22_tot_spe_ris_chrg(BigDecimal r22_tot_spe_ris_chrg) {
		this.r22_tot_spe_ris_chrg = r22_tot_spe_ris_chrg;
	}
	public BigDecimal getR22_net_pos_gen_mar_ris() {
		return r22_net_pos_gen_mar_ris;
	}
	public void setR22_net_pos_gen_mar_ris(BigDecimal r22_net_pos_gen_mar_ris) {
		this.r22_net_pos_gen_mar_ris = r22_net_pos_gen_mar_ris;
	}
	public BigDecimal getR22_gen_mar_ris_chrg_8per() {
		return r22_gen_mar_ris_chrg_8per;
	}
	public void setR22_gen_mar_ris_chrg_8per(BigDecimal r22_gen_mar_ris_chrg_8per) {
		this.r22_gen_mar_ris_chrg_8per = r22_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR22_2per_gen_mar_ris_chrg_div_port() {
		return r22_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR22_2per_gen_mar_ris_chrg_div_port(BigDecimal r22_2per_gen_mar_ris_chrg_div_port) {
		this.r22_2per_gen_mar_ris_chrg_div_port = r22_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR22_tot_gen_mar_risk_chrg() {
		return r22_tot_gen_mar_risk_chrg;
	}
	public void setR22_tot_gen_mar_risk_chrg(BigDecimal r22_tot_gen_mar_risk_chrg) {
		this.r22_tot_gen_mar_risk_chrg = r22_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR22_tot_mar_ris_chrg() {
		return r22_tot_mar_ris_chrg;
	}
	public void setR22_tot_mar_ris_chrg(BigDecimal r22_tot_mar_ris_chrg) {
		this.r22_tot_mar_ris_chrg = r22_tot_mar_ris_chrg;
	}
	public String getR23_market() {
		return r23_market;
	}
	public void setR23_market(String r23_market) {
		this.r23_market = r23_market;
	}
	public BigDecimal getR23_gpfsr_nom_amt() {
		return r23_gpfsr_nom_amt;
	}
	public void setR23_gpfsr_nom_amt(BigDecimal r23_gpfsr_nom_amt) {
		this.r23_gpfsr_nom_amt = r23_gpfsr_nom_amt;
	}
	public BigDecimal getR23_gpfsr_pos_att8_per_spe_ris() {
		return r23_gpfsr_pos_att8_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att8_per_spe_ris(BigDecimal r23_gpfsr_pos_att8_per_spe_ris) {
		this.r23_gpfsr_pos_att8_per_spe_ris = r23_gpfsr_pos_att8_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg() {
		return r23_gpfsr_chrg;
	}
	public void setR23_gpfsr_chrg(BigDecimal r23_gpfsr_chrg) {
		this.r23_gpfsr_chrg = r23_gpfsr_chrg;
	}
	public BigDecimal getR23_gpfsr_nom_amt1() {
		return r23_gpfsr_nom_amt1;
	}
	public void setR23_gpfsr_nom_amt1(BigDecimal r23_gpfsr_nom_amt1) {
		this.r23_gpfsr_nom_amt1 = r23_gpfsr_nom_amt1;
	}
	public BigDecimal getR23_gpfsr_pos_att4_per_spe_ris() {
		return r23_gpfsr_pos_att4_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att4_per_spe_ris(BigDecimal r23_gpfsr_pos_att4_per_spe_ris) {
		this.r23_gpfsr_pos_att4_per_spe_ris = r23_gpfsr_pos_att4_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg1() {
		return r23_gpfsr_chrg1;
	}
	public void setR23_gpfsr_chrg1(BigDecimal r23_gpfsr_chrg1) {
		this.r23_gpfsr_chrg1 = r23_gpfsr_chrg1;
	}
	public BigDecimal getR23_gpfsr_nom_amt2() {
		return r23_gpfsr_nom_amt2;
	}
	public void setR23_gpfsr_nom_amt2(BigDecimal r23_gpfsr_nom_amt2) {
		this.r23_gpfsr_nom_amt2 = r23_gpfsr_nom_amt2;
	}
	public BigDecimal getR23_gpfsr_pos_att2_per_spe_ris() {
		return r23_gpfsr_pos_att2_per_spe_ris;
	}
	public void setR23_gpfsr_pos_att2_per_spe_ris(BigDecimal r23_gpfsr_pos_att2_per_spe_ris) {
		this.r23_gpfsr_pos_att2_per_spe_ris = r23_gpfsr_pos_att2_per_spe_ris;
	}
	public BigDecimal getR23_gpfsr_chrg2() {
		return r23_gpfsr_chrg2;
	}
	public void setR23_gpfsr_chrg2(BigDecimal r23_gpfsr_chrg2) {
		this.r23_gpfsr_chrg2 = r23_gpfsr_chrg2;
	}
	public BigDecimal getR23_tot_spe_ris_chrg() {
		return r23_tot_spe_ris_chrg;
	}
	public void setR23_tot_spe_ris_chrg(BigDecimal r23_tot_spe_ris_chrg) {
		this.r23_tot_spe_ris_chrg = r23_tot_spe_ris_chrg;
	}
	public BigDecimal getR23_net_pos_gen_mar_ris() {
		return r23_net_pos_gen_mar_ris;
	}
	public void setR23_net_pos_gen_mar_ris(BigDecimal r23_net_pos_gen_mar_ris) {
		this.r23_net_pos_gen_mar_ris = r23_net_pos_gen_mar_ris;
	}
	public BigDecimal getR23_gen_mar_ris_chrg_8per() {
		return r23_gen_mar_ris_chrg_8per;
	}
	public void setR23_gen_mar_ris_chrg_8per(BigDecimal r23_gen_mar_ris_chrg_8per) {
		this.r23_gen_mar_ris_chrg_8per = r23_gen_mar_ris_chrg_8per;
	}
	public BigDecimal getR23_2per_gen_mar_ris_chrg_div_port() {
		return r23_2per_gen_mar_ris_chrg_div_port;
	}
	public void setR23_2per_gen_mar_ris_chrg_div_port(BigDecimal r23_2per_gen_mar_ris_chrg_div_port) {
		this.r23_2per_gen_mar_ris_chrg_div_port = r23_2per_gen_mar_ris_chrg_div_port;
	}
	public BigDecimal getR23_tot_gen_mar_risk_chrg() {
		return r23_tot_gen_mar_risk_chrg;
	}
	public void setR23_tot_gen_mar_risk_chrg(BigDecimal r23_tot_gen_mar_risk_chrg) {
		this.r23_tot_gen_mar_risk_chrg = r23_tot_gen_mar_risk_chrg;
	}
	public BigDecimal getR23_tot_mar_ris_chrg() {
		return r23_tot_mar_ris_chrg;
	}
	public void setR23_tot_mar_ris_chrg(BigDecimal r23_tot_mar_ris_chrg) {
		this.r23_tot_mar_ris_chrg = r23_tot_mar_ris_chrg;
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
	public M_EPR_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
		

		
	    
	    
	    

}
