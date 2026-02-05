package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_M_FXR_SUMMARYTABLE")

public class M_FXR_Summary_Entity {

   // ===== PRIMARY KEY =====

    @Id
    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.DATE)
    private Date reportDate;

    // ===== R11 =====

    @Column(name = "R11_NET_SPOT_POSITION")
    private BigDecimal r11_net_spot_position;

    @Column(name = "R11_NET_FORWARD_POSITION")
    private BigDecimal r11_net_forward_position;

    @Column(name = "R11_GUARANTEES")
    private BigDecimal r11_guarantees;

    @Column(name = "R11_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r11_net_future_inc_or_exp;

    @Column(name = "R11_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r11_net_delta_wei_fx_opt_posi;

    @Column(name = "R11_OTHER_ITEMS")
    private BigDecimal r11_other_items;

    @Column(name = "R11_NET_LONG_POSITION")
    private BigDecimal r11_net_long_position;

    @Column(name = "R11_OR")
    private BigDecimal r11_or;

    @Column(name = "R11_NET_SHORT_POSITION")
    private BigDecimal r11_net_short_position;

    // ===== R12 =====

    @Column(name = "R12_NET_SPOT_POSITION")
    private BigDecimal r12_net_spot_position;

    @Column(name = "R12_NET_FORWARD_POSITION")
    private BigDecimal r12_net_forward_position;

    @Column(name = "R12_GUARANTEES")
    private BigDecimal r12_guarantees;

    @Column(name = "R12_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r12_net_future_inc_or_exp;

    @Column(name = "R12_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r12_net_delta_wei_fx_opt_posi;

    @Column(name = "R12_OTHER_ITEMS")
    private BigDecimal r12_other_items;

    @Column(name = "R12_NET_LONG_POSITION")
    private BigDecimal r12_net_long_position;

    @Column(name = "R12_OR")
    private BigDecimal r12_or;

    @Column(name = "R12_NET_SHORT_POSITION")
    private BigDecimal r12_net_short_position;

    // ===== R13 =====

    @Column(name = "R13_NET_SPOT_POSITION")
    private BigDecimal r13_net_spot_position;

    @Column(name = "R13_NET_FORWARD_POSITION")
    private BigDecimal r13_net_forward_position;

    @Column(name = "R13_GUARANTEES")
    private BigDecimal r13_guarantees;

    @Column(name = "R13_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r13_net_future_inc_or_exp;

    @Column(name = "R13_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r13_net_delta_wei_fx_opt_posi;

    @Column(name = "R13_OTHER_ITEMS")
    private BigDecimal r13_other_items;

    @Column(name = "R13_NET_LONG_POSITION")
    private BigDecimal r13_net_long_position;

    @Column(name = "R13_OR")
    private BigDecimal r13_or;

    @Column(name = "R13_NET_SHORT_POSITION")
    private BigDecimal r13_net_short_position;

    // ===== R14 =====

    @Column(name = "R14_NET_SPOT_POSITION")
    private BigDecimal r14_net_spot_position;

    @Column(name = "R14_NET_FORWARD_POSITION")
    private BigDecimal r14_net_forward_position;

    @Column(name = "R14_GUARANTEES")
    private BigDecimal r14_guarantees;

    @Column(name = "R14_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r14_net_future_inc_or_exp;

    @Column(name = "R14_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r14_net_delta_wei_fx_opt_posi;

    @Column(name = "R14_OTHER_ITEMS")
    private BigDecimal r14_other_items;

    @Column(name = "R14_NET_LONG_POSITION")
    private BigDecimal r14_net_long_position;

    @Column(name = "R14_OR")
    private BigDecimal r14_or;

    @Column(name = "R14_NET_SHORT_POSITION")
    private BigDecimal r14_net_short_position;

    // ===== R15 =====

    @Column(name = "R15_NET_SPOT_POSITION")
    private BigDecimal r15_net_spot_position;

    @Column(name = "R15_NET_FORWARD_POSITION")
    private BigDecimal r15_net_forward_position;

    @Column(name = "R15_GUARANTEES")
    private BigDecimal r15_guarantees;

    @Column(name = "R15_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r15_net_future_inc_or_exp;

    @Column(name = "R15_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r15_net_delta_wei_fx_opt_posi;

    @Column(name = "R15_OTHER_ITEMS")
    private BigDecimal r15_other_items;

    @Column(name = "R15_NET_LONG_POSITION")
    private BigDecimal r15_net_long_position;

    @Column(name = "R15_OR")
    private BigDecimal r15_or;

    @Column(name = "R15_NET_SHORT_POSITION")
    private BigDecimal r15_net_short_position;

    // ===== R16 =====

    @Column(name = "R16_NET_SPOT_POSITION")
    private BigDecimal r16_net_spot_position;

    @Column(name = "R16_NET_FORWARD_POSITION")
    private BigDecimal r16_net_forward_position;

    @Column(name = "R16_GUARANTEES")
    private BigDecimal r16_guarantees;

    @Column(name = "R16_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r16_net_future_inc_or_exp;

    @Column(name = "R16_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r16_net_delta_wei_fx_opt_posi;

    @Column(name = "R16_OTHER_ITEMS")
    private BigDecimal r16_other_items;

    @Column(name = "R16_NET_LONG_POSITION")
    private BigDecimal r16_net_long_position;

    @Column(name = "R16_OR")
    private BigDecimal r16_or;

    @Column(name = "R16_NET_SHORT_POSITION")
    private BigDecimal r16_net_short_position;

    // ===== R17 =====

    @Column(name = "R17_NET_LONG_POSITION")
    private BigDecimal r17_net_long_position;

    @Column(name = "R17_OR")
    private BigDecimal r17_or;

    @Column(name = "R17_NET_SHORT_POSITION")
    private BigDecimal r17_net_short_position;

    // ===== R21 =====

    @Column(name = "R21_LONG")
    private BigDecimal r21_long;

    @Column(name = "R21_SHORT")
    private BigDecimal r21_short;

    @Column(name = "R21_TOTAL_GROSS_LONG_SHORT")
    private BigDecimal r21_total_gross_long_short;

    @Column(name = "R21_NET_POSITION")
    private BigDecimal r21_net_position;

    // ===== R22 =====

    @Column(name = "R22_LONG")
    private BigDecimal r22_long;

    @Column(name = "R22_SHORT")
    private BigDecimal r22_short;

    @Column(name = "R22_TOTAL_GROSS_LONG_SHORT")
    private BigDecimal r22_total_gross_long_short;

    @Column(name = "R22_NET_POSITION")
    private BigDecimal r22_net_position;

    // ===== R23 =====

    @Column(name = "R23_NET_POSITION")
    private BigDecimal r23_net_position;

    // ===== R29 & R30 =====

    @Column(name = "R29_GREATER_NET_LONG_OR_SHORT")
    private BigDecimal r29_greater_net_long_or_short;

    @Column(name = "R29_ABS_VALUE_NET_GOLD_POSI")
    private BigDecimal r29_abs_value_net_gold_posi;

    @Column(name = "R29_CAPITAL_CHARGE")
    private BigDecimal r29_capital_charge;

    @Column(name = "R30_CAPITAL_REQUIRE")
    private BigDecimal r30_capital_require;

    // ===== COMMON FIELDS =====

    @Column(name = "REPORT_VERSION")
    private BigDecimal report_version;

    @Column(name = "REPORT_FREQUENCY")
    private String report_frequency;

    @Column(name = "REPORT_CODE")
    private String report_code;

    @Column(name = "REPORT_DESC")
    private String report_desc;

    @Column(name = "ENTITY_FLG")
    private String entity_flg;

    @Column(name = "MODIFY_FLG")
    private String modify_flg;

    @Column(name = "DEL_FLG")
    private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
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

	public M_FXR_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}