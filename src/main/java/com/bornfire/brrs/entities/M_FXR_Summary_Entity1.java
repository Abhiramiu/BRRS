
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
@Table(name = "BRRS_M_FXR_SUMMARYTABLE1")

public class M_FXR_Summary_Entity1{	
	
	
//	private String r11_currency;
	private BigDecimal r11_net_spot_position;
	private BigDecimal r11_net_forward_position;
	private BigDecimal r11_guarantees;
	private BigDecimal r11_net_future_inc_or_exp;
	private BigDecimal r11_net_delta_wei_fx_opt_posi;
	private BigDecimal r11_other_items;
	private BigDecimal r11_net_long_position;
	private BigDecimal r11_or;
	private BigDecimal r11_net_short_position;
//	private String r12_currency;
	private BigDecimal r12_net_spot_position;
	private BigDecimal r12_net_forward_position;
	private BigDecimal r12_guarantees;
	private BigDecimal r12_net_future_inc_or_exp;
	private BigDecimal r12_net_delta_wei_fx_opt_posi;
	private BigDecimal r12_other_items;
	private BigDecimal r12_net_long_position;
	private BigDecimal r12_or;
	private BigDecimal r12_net_short_position;
//	private String r13_currency;
	private BigDecimal r13_net_spot_position;
	private BigDecimal r13_net_forward_position;
	private BigDecimal r13_guarantees;
	private BigDecimal r13_net_future_inc_or_exp;
	private BigDecimal r13_net_delta_wei_fx_opt_posi;
	private BigDecimal r13_other_items;
	private BigDecimal r13_net_long_position;
	private BigDecimal r13_or;
	private BigDecimal r13_net_short_position;
//	private String r14_currency;
	private BigDecimal r14_net_spot_position;
	private BigDecimal r14_net_forward_position;
	private BigDecimal r14_guarantees;
	private BigDecimal r14_net_future_inc_or_exp;
	private BigDecimal r14_net_delta_wei_fx_opt_posi;
	private BigDecimal r14_other_items;
	private BigDecimal r14_net_long_position;
	private BigDecimal r14_or;
	private BigDecimal r14_net_short_position;
//	private String r15_currency;
	private BigDecimal r15_net_spot_position;
	private BigDecimal r15_net_forward_position;
	private BigDecimal r15_guarantees;
	private BigDecimal r15_net_future_inc_or_exp;
	private BigDecimal r15_net_delta_wei_fx_opt_posi;
	private BigDecimal r15_other_items;
	private BigDecimal r15_net_long_position;
	private BigDecimal r15_or;
	private BigDecimal r15_net_short_position;
//	private String r16_currency;
	private BigDecimal r16_net_spot_position;
	private BigDecimal r16_net_forward_position;
	private BigDecimal r16_guarantees;
	private BigDecimal r16_net_future_inc_or_exp;
	private BigDecimal r16_net_delta_wei_fx_opt_posi;
	private BigDecimal r16_other_items;
	private BigDecimal r16_net_long_position;
	private BigDecimal r16_or;
	private BigDecimal r16_net_short_position;

	private BigDecimal r17_net_long_position;
	private BigDecimal r17_or;
	private BigDecimal r17_net_short_position;


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

	
//	
//	public String getR11_currency() {
//		return r11_currency;
//	}
//
//
//
//	public void setR11_currency(String r11_currency) {
//		this.r11_currency = r11_currency;
//	}
//


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



//	public String getR12_currency() {
//		return r12_currency;
//	}
//
//
//
//	public void setR12_currency(String r12_currency) {
//		this.r12_currency = r12_currency;
//	}



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



//	public String getR13_currency() {
//		return r13_currency;
//	}
//
//
//
//	public void setR13_currency(String r13_currency) {
//		this.r13_currency = r13_currency;
//	}
//


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


//
//	public String getR14_currency() {
//		return r14_currency;
//	}
//
//
//
//	public void setR14_currency(String r14_currency) {
//		this.r14_currency = r14_currency;
//	}
//


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


//
//	public String getR15_currency() {
//		return r15_currency;
//	}
//
//
//
//	public void setR15_currency(String r15_currency) {
//		this.r15_currency = r15_currency;
//	}
//


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

//
//
//	public String getR16_currency() {
//		return r16_currency;
//	}
//
//
//
//	public void setR16_currency(String r16_currency) {
//		this.r16_currency = r16_currency;
//	}



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



	public M_FXR_Summary_Entity1() {
	super();
	// TODO Auto-generated constructor stub
}

	
}