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
@Table(name = "BRRS_M_BOP_ARCHIVALTABLE_SUMMARY")  
public class M_BOP_Archival_Summary_Entity {
	private String	r13_product;
	private BigDecimal	r13_open_position;
	private BigDecimal	r13_cpdm_dt_inc;
	private BigDecimal	r13_cpdm_dt_dec;
	private BigDecimal	r13_net;
	private BigDecimal	r13_cpdm_dt_der;
	private BigDecimal	r13_cpdm_dt_dto;
	private BigDecimal	r13_cp;
	private String	r14_product;
	private BigDecimal	r14_open_position;
	private BigDecimal	r14_cpdm_dt_inc;
	private BigDecimal	r14_cpdm_dt_dec;
	private BigDecimal	r14_net;
	private BigDecimal	r14_cpdm_dt_der;
	private BigDecimal	r14_cpdm_dt_dto;
	private BigDecimal	r14_cp;
	private String	r15_product;
	private BigDecimal	r15_open_position;
	private BigDecimal	r15_cpdm_dt_inc;
	private BigDecimal	r15_cpdm_dt_dec;
	private BigDecimal	r15_net;
	private BigDecimal	r15_cpdm_dt_der;
	private BigDecimal	r15_cpdm_dt_dto;
	private BigDecimal	r15_cp;
	private String	r16_product;
	private BigDecimal	r16_open_position;
	private BigDecimal	r16_cpdm_dt_inc;
	private BigDecimal	r16_cpdm_dt_dec;
	private BigDecimal	r16_net;
	private BigDecimal	r16_cpdm_dt_der;
	private BigDecimal	r16_cpdm_dt_dto;
	private BigDecimal	r16_cp;
	private String	r17_product;
	private BigDecimal	r17_open_position;
	private BigDecimal	r17_cpdm_dt_inc;
	private BigDecimal	r17_cpdm_dt_dec;
	private BigDecimal	r17_net;
	private BigDecimal	r17_cpdm_dt_der;
	private BigDecimal	r17_cpdm_dt_dto;
	private BigDecimal	r17_cp;
	private String	r18_product;
	private BigDecimal	r18_open_position;
	private BigDecimal	r18_cpdm_dt_inc;
	private BigDecimal	r18_cpdm_dt_dec;
	private BigDecimal	r18_net;
	private BigDecimal	r18_cpdm_dt_der;
	private BigDecimal	r18_cpdm_dt_dto;
	private BigDecimal	r18_cp;
	private String	r19_product;
	private BigDecimal	r19_open_position;
	private BigDecimal	r19_cpdm_dt_inc;
	private BigDecimal	r19_cpdm_dt_dec;
	private BigDecimal	r19_net;
	private BigDecimal	r19_cpdm_dt_der;
	private BigDecimal	r19_cpdm_dt_dto;
	private BigDecimal	r19_cp;
	private String	r20_product;
	private BigDecimal	r20_open_position;
	private BigDecimal	r20_cpdm_dt_inc;
	private BigDecimal	r20_cpdm_dt_dec;
	private BigDecimal	r20_net;
	private BigDecimal	r20_cpdm_dt_der;
	private BigDecimal	r20_cpdm_dt_dto;
	private BigDecimal	r20_cp;
	private String	r21_product;
	private BigDecimal	r21_open_position;
	private BigDecimal	r21_cpdm_dt_inc;
	private BigDecimal	r21_cpdm_dt_dec;
	private BigDecimal	r21_net;
	private BigDecimal	r21_cpdm_dt_der;
	private BigDecimal	r21_cpdm_dt_dto;
	private BigDecimal	r21_cp;
	private String	r22_product;
	private BigDecimal	r22_open_position;
	private BigDecimal	r22_cpdm_dt_inc;
	private BigDecimal	r22_cpdm_dt_dec;
	private BigDecimal	r22_net;
	private BigDecimal	r22_cpdm_dt_der;
	private BigDecimal	r22_cpdm_dt_dto;
	private BigDecimal	r22_cp;
	private String	r23_product;
	private BigDecimal	r23_open_position;
	private BigDecimal	r23_cpdm_dt_inc;
	private BigDecimal	r23_cpdm_dt_dec;
	private BigDecimal	r23_net;
	private BigDecimal	r23_cpdm_dt_der;
	private BigDecimal	r23_cpdm_dt_dto;
	private BigDecimal	r23_cp;
	private String	r24_product;
	private BigDecimal	r24_open_position;
	private BigDecimal	r24_cpdm_dt_inc;
	private BigDecimal	r24_cpdm_dt_dec;
	private BigDecimal	r24_net;
	private BigDecimal	r24_cpdm_dt_der;
	private BigDecimal	r24_cpdm_dt_dto;
	private BigDecimal	r24_cp;
	private String	r25_product;
	private BigDecimal	r25_open_position;
	private BigDecimal	r25_cpdm_dt_inc;
	private BigDecimal	r25_cpdm_dt_dec;
	private BigDecimal	r25_net;
	private BigDecimal	r25_cpdm_dt_der;
	private BigDecimal	r25_cpdm_dt_dto;
	private BigDecimal	r25_cp;
	private String	r26_product;
	private BigDecimal	r26_open_position;
	private BigDecimal	r26_cpdm_dt_inc;
	private BigDecimal	r26_cpdm_dt_dec;
	private BigDecimal	r26_net;
	private BigDecimal	r26_cpdm_dt_der;
	private BigDecimal	r26_cpdm_dt_dto;
	private BigDecimal	r26_cp;
	private String	r27_product;
	private BigDecimal	r27_open_position;
	private BigDecimal	r27_cpdm_dt_inc;
	private BigDecimal	r27_cpdm_dt_dec;
	private BigDecimal	r27_net;
	private BigDecimal	r27_cpdm_dt_der;
	private BigDecimal	r27_cpdm_dt_dto;
	private BigDecimal	r27_cp;
	private String	r28_product;
	private BigDecimal	r28_open_position;
	private BigDecimal	r28_cpdm_dt_inc;
	private BigDecimal	r28_cpdm_dt_dec;
	private BigDecimal	r28_net;
	private BigDecimal	r28_cpdm_dt_der;
	private BigDecimal	r28_cpdm_dt_dto;
	private BigDecimal	r28_cp;
	private String	r29_product;
	private BigDecimal	r29_open_position;
	private BigDecimal	r29_cpdm_dt_inc;
	private BigDecimal	r29_cpdm_dt_dec;
	private BigDecimal	r29_net;
	private BigDecimal	r29_cpdm_dt_der;
	private BigDecimal	r29_cpdm_dt_dto;
	private BigDecimal	r29_cp;
	private String	r30_product;
	private BigDecimal	r30_open_position;
	private BigDecimal	r30_cpdm_dt_inc;
	private BigDecimal	r30_cpdm_dt_dec;
	private BigDecimal	r30_net;
	private BigDecimal	r30_cpdm_dt_der;
	private BigDecimal	r30_cpdm_dt_dto;
	private BigDecimal	r30_cp;
	private String	r31_product;
	private BigDecimal	r31_open_position;
	private BigDecimal	r31_cpdm_dt_inc;
	private BigDecimal	r31_cpdm_dt_dec;
	private BigDecimal	r31_net;
	private BigDecimal	r31_cpdm_dt_der;
	private BigDecimal	r31_cpdm_dt_dto;
	private BigDecimal	r31_cp;
	private String	r32_product;
	private BigDecimal	r32_open_position;
	private BigDecimal	r32_cpdm_dt_inc;
	private BigDecimal	r32_cpdm_dt_dec;
	private BigDecimal	r32_net;
	private BigDecimal	r32_cpdm_dt_der;
	private BigDecimal	r32_cpdm_dt_dto;
	private BigDecimal	r32_cp;
	private String	r33_product;
	private BigDecimal	r33_open_position;
	private BigDecimal	r33_cpdm_dt_inc;
	private BigDecimal	r33_cpdm_dt_dec;
	private BigDecimal	r33_net;
	private BigDecimal	r33_cpdm_dt_der;
	private BigDecimal	r33_cpdm_dt_dto;
	private BigDecimal	r33_cp;
	private String	r34_product;
	private BigDecimal	r34_open_position;
	private BigDecimal	r34_cpdm_dt_inc;
	private BigDecimal	r34_cpdm_dt_dec;
	private BigDecimal	r34_net;
	private BigDecimal	r34_cpdm_dt_der;
	private BigDecimal	r34_cpdm_dt_dto;
	private BigDecimal	r34_cp;
	private String	r35_product;
	private BigDecimal	r35_open_position;
	private BigDecimal	r35_cpdm_dt_inc;
	private BigDecimal	r35_cpdm_dt_dec;
	private BigDecimal	r35_net;
	private BigDecimal	r35_cpdm_dt_der;
	private BigDecimal	r35_cpdm_dt_dto;
	private BigDecimal	r35_cp;
	private String	r36_product;
	private BigDecimal	r36_open_position;
	private BigDecimal	r36_cpdm_dt_inc;
	private BigDecimal	r36_cpdm_dt_dec;
	private BigDecimal	r36_net;
	private BigDecimal	r36_cpdm_dt_der;
	private BigDecimal	r36_cpdm_dt_dto;
	private BigDecimal	r36_cp;
	
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
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_open_position() {
		return r13_open_position;
	}
	public void setR13_open_position(BigDecimal r13_open_position) {
		this.r13_open_position = r13_open_position;
	}
	public BigDecimal getR13_cpdm_dt_inc() {
		return r13_cpdm_dt_inc;
	}
	public void setR13_cpdm_dt_inc(BigDecimal r13_cpdm_dt_inc) {
		this.r13_cpdm_dt_inc = r13_cpdm_dt_inc;
	}
	public BigDecimal getR13_cpdm_dt_dec() {
		return r13_cpdm_dt_dec;
	}
	public void setR13_cpdm_dt_dec(BigDecimal r13_cpdm_dt_dec) {
		this.r13_cpdm_dt_dec = r13_cpdm_dt_dec;
	}
	public BigDecimal getR13_net() {
		return r13_net;
	}
	public void setR13_net(BigDecimal r13_net) {
		this.r13_net = r13_net;
	}
	public BigDecimal getR13_cpdm_dt_der() {
		return r13_cpdm_dt_der;
	}
	public void setR13_cpdm_dt_der(BigDecimal r13_cpdm_dt_der) {
		this.r13_cpdm_dt_der = r13_cpdm_dt_der;
	}
	public BigDecimal getR13_cpdm_dt_dto() {
		return r13_cpdm_dt_dto;
	}
	public void setR13_cpdm_dt_dto(BigDecimal r13_cpdm_dt_dto) {
		this.r13_cpdm_dt_dto = r13_cpdm_dt_dto;
	}
	public BigDecimal getR13_cp() {
		return r13_cp;
	}
	public void setR13_cp(BigDecimal r13_cp) {
		this.r13_cp = r13_cp;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_open_position() {
		return r14_open_position;
	}
	public void setR14_open_position(BigDecimal r14_open_position) {
		this.r14_open_position = r14_open_position;
	}
	public BigDecimal getR14_cpdm_dt_inc() {
		return r14_cpdm_dt_inc;
	}
	public void setR14_cpdm_dt_inc(BigDecimal r14_cpdm_dt_inc) {
		this.r14_cpdm_dt_inc = r14_cpdm_dt_inc;
	}
	public BigDecimal getR14_cpdm_dt_dec() {
		return r14_cpdm_dt_dec;
	}
	public void setR14_cpdm_dt_dec(BigDecimal r14_cpdm_dt_dec) {
		this.r14_cpdm_dt_dec = r14_cpdm_dt_dec;
	}
	public BigDecimal getR14_net() {
		return r14_net;
	}
	public void setR14_net(BigDecimal r14_net) {
		this.r14_net = r14_net;
	}
	public BigDecimal getR14_cpdm_dt_der() {
		return r14_cpdm_dt_der;
	}
	public void setR14_cpdm_dt_der(BigDecimal r14_cpdm_dt_der) {
		this.r14_cpdm_dt_der = r14_cpdm_dt_der;
	}
	public BigDecimal getR14_cpdm_dt_dto() {
		return r14_cpdm_dt_dto;
	}
	public void setR14_cpdm_dt_dto(BigDecimal r14_cpdm_dt_dto) {
		this.r14_cpdm_dt_dto = r14_cpdm_dt_dto;
	}
	public BigDecimal getR14_cp() {
		return r14_cp;
	}
	public void setR14_cp(BigDecimal r14_cp) {
		this.r14_cp = r14_cp;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_open_position() {
		return r15_open_position;
	}
	public void setR15_open_position(BigDecimal r15_open_position) {
		this.r15_open_position = r15_open_position;
	}
	public BigDecimal getR15_cpdm_dt_inc() {
		return r15_cpdm_dt_inc;
	}
	public void setR15_cpdm_dt_inc(BigDecimal r15_cpdm_dt_inc) {
		this.r15_cpdm_dt_inc = r15_cpdm_dt_inc;
	}
	public BigDecimal getR15_cpdm_dt_dec() {
		return r15_cpdm_dt_dec;
	}
	public void setR15_cpdm_dt_dec(BigDecimal r15_cpdm_dt_dec) {
		this.r15_cpdm_dt_dec = r15_cpdm_dt_dec;
	}
	public BigDecimal getR15_net() {
		return r15_net;
	}
	public void setR15_net(BigDecimal r15_net) {
		this.r15_net = r15_net;
	}
	public BigDecimal getR15_cpdm_dt_der() {
		return r15_cpdm_dt_der;
	}
	public void setR15_cpdm_dt_der(BigDecimal r15_cpdm_dt_der) {
		this.r15_cpdm_dt_der = r15_cpdm_dt_der;
	}
	public BigDecimal getR15_cpdm_dt_dto() {
		return r15_cpdm_dt_dto;
	}
	public void setR15_cpdm_dt_dto(BigDecimal r15_cpdm_dt_dto) {
		this.r15_cpdm_dt_dto = r15_cpdm_dt_dto;
	}
	public BigDecimal getR15_cp() {
		return r15_cp;
	}
	public void setR15_cp(BigDecimal r15_cp) {
		this.r15_cp = r15_cp;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_open_position() {
		return r16_open_position;
	}
	public void setR16_open_position(BigDecimal r16_open_position) {
		this.r16_open_position = r16_open_position;
	}
	public BigDecimal getR16_cpdm_dt_inc() {
		return r16_cpdm_dt_inc;
	}
	public void setR16_cpdm_dt_inc(BigDecimal r16_cpdm_dt_inc) {
		this.r16_cpdm_dt_inc = r16_cpdm_dt_inc;
	}
	public BigDecimal getR16_cpdm_dt_dec() {
		return r16_cpdm_dt_dec;
	}
	public void setR16_cpdm_dt_dec(BigDecimal r16_cpdm_dt_dec) {
		this.r16_cpdm_dt_dec = r16_cpdm_dt_dec;
	}
	public BigDecimal getR16_net() {
		return r16_net;
	}
	public void setR16_net(BigDecimal r16_net) {
		this.r16_net = r16_net;
	}
	public BigDecimal getR16_cpdm_dt_der() {
		return r16_cpdm_dt_der;
	}
	public void setR16_cpdm_dt_der(BigDecimal r16_cpdm_dt_der) {
		this.r16_cpdm_dt_der = r16_cpdm_dt_der;
	}
	public BigDecimal getR16_cpdm_dt_dto() {
		return r16_cpdm_dt_dto;
	}
	public void setR16_cpdm_dt_dto(BigDecimal r16_cpdm_dt_dto) {
		this.r16_cpdm_dt_dto = r16_cpdm_dt_dto;
	}
	public BigDecimal getR16_cp() {
		return r16_cp;
	}
	public void setR16_cp(BigDecimal r16_cp) {
		this.r16_cp = r16_cp;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_open_position() {
		return r17_open_position;
	}
	public void setR17_open_position(BigDecimal r17_open_position) {
		this.r17_open_position = r17_open_position;
	}
	public BigDecimal getR17_cpdm_dt_inc() {
		return r17_cpdm_dt_inc;
	}
	public void setR17_cpdm_dt_inc(BigDecimal r17_cpdm_dt_inc) {
		this.r17_cpdm_dt_inc = r17_cpdm_dt_inc;
	}
	public BigDecimal getR17_cpdm_dt_dec() {
		return r17_cpdm_dt_dec;
	}
	public void setR17_cpdm_dt_dec(BigDecimal r17_cpdm_dt_dec) {
		this.r17_cpdm_dt_dec = r17_cpdm_dt_dec;
	}
	public BigDecimal getR17_net() {
		return r17_net;
	}
	public void setR17_net(BigDecimal r17_net) {
		this.r17_net = r17_net;
	}
	public BigDecimal getR17_cpdm_dt_der() {
		return r17_cpdm_dt_der;
	}
	public void setR17_cpdm_dt_der(BigDecimal r17_cpdm_dt_der) {
		this.r17_cpdm_dt_der = r17_cpdm_dt_der;
	}
	public BigDecimal getR17_cpdm_dt_dto() {
		return r17_cpdm_dt_dto;
	}
	public void setR17_cpdm_dt_dto(BigDecimal r17_cpdm_dt_dto) {
		this.r17_cpdm_dt_dto = r17_cpdm_dt_dto;
	}
	public BigDecimal getR17_cp() {
		return r17_cp;
	}
	public void setR17_cp(BigDecimal r17_cp) {
		this.r17_cp = r17_cp;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_open_position() {
		return r18_open_position;
	}
	public void setR18_open_position(BigDecimal r18_open_position) {
		this.r18_open_position = r18_open_position;
	}
	public BigDecimal getR18_cpdm_dt_inc() {
		return r18_cpdm_dt_inc;
	}
	public void setR18_cpdm_dt_inc(BigDecimal r18_cpdm_dt_inc) {
		this.r18_cpdm_dt_inc = r18_cpdm_dt_inc;
	}
	public BigDecimal getR18_cpdm_dt_dec() {
		return r18_cpdm_dt_dec;
	}
	public void setR18_cpdm_dt_dec(BigDecimal r18_cpdm_dt_dec) {
		this.r18_cpdm_dt_dec = r18_cpdm_dt_dec;
	}
	public BigDecimal getR18_net() {
		return r18_net;
	}
	public void setR18_net(BigDecimal r18_net) {
		this.r18_net = r18_net;
	}
	public BigDecimal getR18_cpdm_dt_der() {
		return r18_cpdm_dt_der;
	}
	public void setR18_cpdm_dt_der(BigDecimal r18_cpdm_dt_der) {
		this.r18_cpdm_dt_der = r18_cpdm_dt_der;
	}
	public BigDecimal getR18_cpdm_dt_dto() {
		return r18_cpdm_dt_dto;
	}
	public void setR18_cpdm_dt_dto(BigDecimal r18_cpdm_dt_dto) {
		this.r18_cpdm_dt_dto = r18_cpdm_dt_dto;
	}
	public BigDecimal getR18_cp() {
		return r18_cp;
	}
	public void setR18_cp(BigDecimal r18_cp) {
		this.r18_cp = r18_cp;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_open_position() {
		return r19_open_position;
	}
	public void setR19_open_position(BigDecimal r19_open_position) {
		this.r19_open_position = r19_open_position;
	}
	public BigDecimal getR19_cpdm_dt_inc() {
		return r19_cpdm_dt_inc;
	}
	public void setR19_cpdm_dt_inc(BigDecimal r19_cpdm_dt_inc) {
		this.r19_cpdm_dt_inc = r19_cpdm_dt_inc;
	}
	public BigDecimal getR19_cpdm_dt_dec() {
		return r19_cpdm_dt_dec;
	}
	public void setR19_cpdm_dt_dec(BigDecimal r19_cpdm_dt_dec) {
		this.r19_cpdm_dt_dec = r19_cpdm_dt_dec;
	}
	public BigDecimal getR19_net() {
		return r19_net;
	}
	public void setR19_net(BigDecimal r19_net) {
		this.r19_net = r19_net;
	}
	public BigDecimal getR19_cpdm_dt_der() {
		return r19_cpdm_dt_der;
	}
	public void setR19_cpdm_dt_der(BigDecimal r19_cpdm_dt_der) {
		this.r19_cpdm_dt_der = r19_cpdm_dt_der;
	}
	public BigDecimal getR19_cpdm_dt_dto() {
		return r19_cpdm_dt_dto;
	}
	public void setR19_cpdm_dt_dto(BigDecimal r19_cpdm_dt_dto) {
		this.r19_cpdm_dt_dto = r19_cpdm_dt_dto;
	}
	public BigDecimal getR19_cp() {
		return r19_cp;
	}
	public void setR19_cp(BigDecimal r19_cp) {
		this.r19_cp = r19_cp;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_open_position() {
		return r20_open_position;
	}
	public void setR20_open_position(BigDecimal r20_open_position) {
		this.r20_open_position = r20_open_position;
	}
	public BigDecimal getR20_cpdm_dt_inc() {
		return r20_cpdm_dt_inc;
	}
	public void setR20_cpdm_dt_inc(BigDecimal r20_cpdm_dt_inc) {
		this.r20_cpdm_dt_inc = r20_cpdm_dt_inc;
	}
	public BigDecimal getR20_cpdm_dt_dec() {
		return r20_cpdm_dt_dec;
	}
	public void setR20_cpdm_dt_dec(BigDecimal r20_cpdm_dt_dec) {
		this.r20_cpdm_dt_dec = r20_cpdm_dt_dec;
	}
	public BigDecimal getR20_net() {
		return r20_net;
	}
	public void setR20_net(BigDecimal r20_net) {
		this.r20_net = r20_net;
	}
	public BigDecimal getR20_cpdm_dt_der() {
		return r20_cpdm_dt_der;
	}
	public void setR20_cpdm_dt_der(BigDecimal r20_cpdm_dt_der) {
		this.r20_cpdm_dt_der = r20_cpdm_dt_der;
	}
	public BigDecimal getR20_cpdm_dt_dto() {
		return r20_cpdm_dt_dto;
	}
	public void setR20_cpdm_dt_dto(BigDecimal r20_cpdm_dt_dto) {
		this.r20_cpdm_dt_dto = r20_cpdm_dt_dto;
	}
	public BigDecimal getR20_cp() {
		return r20_cp;
	}
	public void setR20_cp(BigDecimal r20_cp) {
		this.r20_cp = r20_cp;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_open_position() {
		return r21_open_position;
	}
	public void setR21_open_position(BigDecimal r21_open_position) {
		this.r21_open_position = r21_open_position;
	}
	public BigDecimal getR21_cpdm_dt_inc() {
		return r21_cpdm_dt_inc;
	}
	public void setR21_cpdm_dt_inc(BigDecimal r21_cpdm_dt_inc) {
		this.r21_cpdm_dt_inc = r21_cpdm_dt_inc;
	}
	public BigDecimal getR21_cpdm_dt_dec() {
		return r21_cpdm_dt_dec;
	}
	public void setR21_cpdm_dt_dec(BigDecimal r21_cpdm_dt_dec) {
		this.r21_cpdm_dt_dec = r21_cpdm_dt_dec;
	}
	public BigDecimal getR21_net() {
		return r21_net;
	}
	public void setR21_net(BigDecimal r21_net) {
		this.r21_net = r21_net;
	}
	public BigDecimal getR21_cpdm_dt_der() {
		return r21_cpdm_dt_der;
	}
	public void setR21_cpdm_dt_der(BigDecimal r21_cpdm_dt_der) {
		this.r21_cpdm_dt_der = r21_cpdm_dt_der;
	}
	public BigDecimal getR21_cpdm_dt_dto() {
		return r21_cpdm_dt_dto;
	}
	public void setR21_cpdm_dt_dto(BigDecimal r21_cpdm_dt_dto) {
		this.r21_cpdm_dt_dto = r21_cpdm_dt_dto;
	}
	public BigDecimal getR21_cp() {
		return r21_cp;
	}
	public void setR21_cp(BigDecimal r21_cp) {
		this.r21_cp = r21_cp;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_open_position() {
		return r22_open_position;
	}
	public void setR22_open_position(BigDecimal r22_open_position) {
		this.r22_open_position = r22_open_position;
	}
	public BigDecimal getR22_cpdm_dt_inc() {
		return r22_cpdm_dt_inc;
	}
	public void setR22_cpdm_dt_inc(BigDecimal r22_cpdm_dt_inc) {
		this.r22_cpdm_dt_inc = r22_cpdm_dt_inc;
	}
	public BigDecimal getR22_cpdm_dt_dec() {
		return r22_cpdm_dt_dec;
	}
	public void setR22_cpdm_dt_dec(BigDecimal r22_cpdm_dt_dec) {
		this.r22_cpdm_dt_dec = r22_cpdm_dt_dec;
	}
	public BigDecimal getR22_net() {
		return r22_net;
	}
	public void setR22_net(BigDecimal r22_net) {
		this.r22_net = r22_net;
	}
	public BigDecimal getR22_cpdm_dt_der() {
		return r22_cpdm_dt_der;
	}
	public void setR22_cpdm_dt_der(BigDecimal r22_cpdm_dt_der) {
		this.r22_cpdm_dt_der = r22_cpdm_dt_der;
	}
	public BigDecimal getR22_cpdm_dt_dto() {
		return r22_cpdm_dt_dto;
	}
	public void setR22_cpdm_dt_dto(BigDecimal r22_cpdm_dt_dto) {
		this.r22_cpdm_dt_dto = r22_cpdm_dt_dto;
	}
	public BigDecimal getR22_cp() {
		return r22_cp;
	}
	public void setR22_cp(BigDecimal r22_cp) {
		this.r22_cp = r22_cp;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_open_position() {
		return r23_open_position;
	}
	public void setR23_open_position(BigDecimal r23_open_position) {
		this.r23_open_position = r23_open_position;
	}
	public BigDecimal getR23_cpdm_dt_inc() {
		return r23_cpdm_dt_inc;
	}
	public void setR23_cpdm_dt_inc(BigDecimal r23_cpdm_dt_inc) {
		this.r23_cpdm_dt_inc = r23_cpdm_dt_inc;
	}
	public BigDecimal getR23_cpdm_dt_dec() {
		return r23_cpdm_dt_dec;
	}
	public void setR23_cpdm_dt_dec(BigDecimal r23_cpdm_dt_dec) {
		this.r23_cpdm_dt_dec = r23_cpdm_dt_dec;
	}
	public BigDecimal getR23_net() {
		return r23_net;
	}
	public void setR23_net(BigDecimal r23_net) {
		this.r23_net = r23_net;
	}
	public BigDecimal getR23_cpdm_dt_der() {
		return r23_cpdm_dt_der;
	}
	public void setR23_cpdm_dt_der(BigDecimal r23_cpdm_dt_der) {
		this.r23_cpdm_dt_der = r23_cpdm_dt_der;
	}
	public BigDecimal getR23_cpdm_dt_dto() {
		return r23_cpdm_dt_dto;
	}
	public void setR23_cpdm_dt_dto(BigDecimal r23_cpdm_dt_dto) {
		this.r23_cpdm_dt_dto = r23_cpdm_dt_dto;
	}
	public BigDecimal getR23_cp() {
		return r23_cp;
	}
	public void setR23_cp(BigDecimal r23_cp) {
		this.r23_cp = r23_cp;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_open_position() {
		return r24_open_position;
	}
	public void setR24_open_position(BigDecimal r24_open_position) {
		this.r24_open_position = r24_open_position;
	}
	public BigDecimal getR24_cpdm_dt_inc() {
		return r24_cpdm_dt_inc;
	}
	public void setR24_cpdm_dt_inc(BigDecimal r24_cpdm_dt_inc) {
		this.r24_cpdm_dt_inc = r24_cpdm_dt_inc;
	}
	public BigDecimal getR24_cpdm_dt_dec() {
		return r24_cpdm_dt_dec;
	}
	public void setR24_cpdm_dt_dec(BigDecimal r24_cpdm_dt_dec) {
		this.r24_cpdm_dt_dec = r24_cpdm_dt_dec;
	}
	public BigDecimal getR24_net() {
		return r24_net;
	}
	public void setR24_net(BigDecimal r24_net) {
		this.r24_net = r24_net;
	}
	public BigDecimal getR24_cpdm_dt_der() {
		return r24_cpdm_dt_der;
	}
	public void setR24_cpdm_dt_der(BigDecimal r24_cpdm_dt_der) {
		this.r24_cpdm_dt_der = r24_cpdm_dt_der;
	}
	public BigDecimal getR24_cpdm_dt_dto() {
		return r24_cpdm_dt_dto;
	}
	public void setR24_cpdm_dt_dto(BigDecimal r24_cpdm_dt_dto) {
		this.r24_cpdm_dt_dto = r24_cpdm_dt_dto;
	}
	public BigDecimal getR24_cp() {
		return r24_cp;
	}
	public void setR24_cp(BigDecimal r24_cp) {
		this.r24_cp = r24_cp;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_open_position() {
		return r25_open_position;
	}
	public void setR25_open_position(BigDecimal r25_open_position) {
		this.r25_open_position = r25_open_position;
	}
	public BigDecimal getR25_cpdm_dt_inc() {
		return r25_cpdm_dt_inc;
	}
	public void setR25_cpdm_dt_inc(BigDecimal r25_cpdm_dt_inc) {
		this.r25_cpdm_dt_inc = r25_cpdm_dt_inc;
	}
	public BigDecimal getR25_cpdm_dt_dec() {
		return r25_cpdm_dt_dec;
	}
	public void setR25_cpdm_dt_dec(BigDecimal r25_cpdm_dt_dec) {
		this.r25_cpdm_dt_dec = r25_cpdm_dt_dec;
	}
	public BigDecimal getR25_net() {
		return r25_net;
	}
	public void setR25_net(BigDecimal r25_net) {
		this.r25_net = r25_net;
	}
	public BigDecimal getR25_cpdm_dt_der() {
		return r25_cpdm_dt_der;
	}
	public void setR25_cpdm_dt_der(BigDecimal r25_cpdm_dt_der) {
		this.r25_cpdm_dt_der = r25_cpdm_dt_der;
	}
	public BigDecimal getR25_cpdm_dt_dto() {
		return r25_cpdm_dt_dto;
	}
	public void setR25_cpdm_dt_dto(BigDecimal r25_cpdm_dt_dto) {
		this.r25_cpdm_dt_dto = r25_cpdm_dt_dto;
	}
	public BigDecimal getR25_cp() {
		return r25_cp;
	}
	public void setR25_cp(BigDecimal r25_cp) {
		this.r25_cp = r25_cp;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_open_position() {
		return r26_open_position;
	}
	public void setR26_open_position(BigDecimal r26_open_position) {
		this.r26_open_position = r26_open_position;
	}
	public BigDecimal getR26_cpdm_dt_inc() {
		return r26_cpdm_dt_inc;
	}
	public void setR26_cpdm_dt_inc(BigDecimal r26_cpdm_dt_inc) {
		this.r26_cpdm_dt_inc = r26_cpdm_dt_inc;
	}
	public BigDecimal getR26_cpdm_dt_dec() {
		return r26_cpdm_dt_dec;
	}
	public void setR26_cpdm_dt_dec(BigDecimal r26_cpdm_dt_dec) {
		this.r26_cpdm_dt_dec = r26_cpdm_dt_dec;
	}
	public BigDecimal getR26_net() {
		return r26_net;
	}
	public void setR26_net(BigDecimal r26_net) {
		this.r26_net = r26_net;
	}
	public BigDecimal getR26_cpdm_dt_der() {
		return r26_cpdm_dt_der;
	}
	public void setR26_cpdm_dt_der(BigDecimal r26_cpdm_dt_der) {
		this.r26_cpdm_dt_der = r26_cpdm_dt_der;
	}
	public BigDecimal getR26_cpdm_dt_dto() {
		return r26_cpdm_dt_dto;
	}
	public void setR26_cpdm_dt_dto(BigDecimal r26_cpdm_dt_dto) {
		this.r26_cpdm_dt_dto = r26_cpdm_dt_dto;
	}
	public BigDecimal getR26_cp() {
		return r26_cp;
	}
	public void setR26_cp(BigDecimal r26_cp) {
		this.r26_cp = r26_cp;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_open_position() {
		return r27_open_position;
	}
	public void setR27_open_position(BigDecimal r27_open_position) {
		this.r27_open_position = r27_open_position;
	}
	public BigDecimal getR27_cpdm_dt_inc() {
		return r27_cpdm_dt_inc;
	}
	public void setR27_cpdm_dt_inc(BigDecimal r27_cpdm_dt_inc) {
		this.r27_cpdm_dt_inc = r27_cpdm_dt_inc;
	}
	public BigDecimal getR27_cpdm_dt_dec() {
		return r27_cpdm_dt_dec;
	}
	public void setR27_cpdm_dt_dec(BigDecimal r27_cpdm_dt_dec) {
		this.r27_cpdm_dt_dec = r27_cpdm_dt_dec;
	}
	public BigDecimal getR27_net() {
		return r27_net;
	}
	public void setR27_net(BigDecimal r27_net) {
		this.r27_net = r27_net;
	}
	public BigDecimal getR27_cpdm_dt_der() {
		return r27_cpdm_dt_der;
	}
	public void setR27_cpdm_dt_der(BigDecimal r27_cpdm_dt_der) {
		this.r27_cpdm_dt_der = r27_cpdm_dt_der;
	}
	public BigDecimal getR27_cpdm_dt_dto() {
		return r27_cpdm_dt_dto;
	}
	public void setR27_cpdm_dt_dto(BigDecimal r27_cpdm_dt_dto) {
		this.r27_cpdm_dt_dto = r27_cpdm_dt_dto;
	}
	public BigDecimal getR27_cp() {
		return r27_cp;
	}
	public void setR27_cp(BigDecimal r27_cp) {
		this.r27_cp = r27_cp;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_open_position() {
		return r28_open_position;
	}
	public void setR28_open_position(BigDecimal r28_open_position) {
		this.r28_open_position = r28_open_position;
	}
	public BigDecimal getR28_cpdm_dt_inc() {
		return r28_cpdm_dt_inc;
	}
	public void setR28_cpdm_dt_inc(BigDecimal r28_cpdm_dt_inc) {
		this.r28_cpdm_dt_inc = r28_cpdm_dt_inc;
	}
	public BigDecimal getR28_cpdm_dt_dec() {
		return r28_cpdm_dt_dec;
	}
	public void setR28_cpdm_dt_dec(BigDecimal r28_cpdm_dt_dec) {
		this.r28_cpdm_dt_dec = r28_cpdm_dt_dec;
	}
	public BigDecimal getR28_net() {
		return r28_net;
	}
	public void setR28_net(BigDecimal r28_net) {
		this.r28_net = r28_net;
	}
	public BigDecimal getR28_cpdm_dt_der() {
		return r28_cpdm_dt_der;
	}
	public void setR28_cpdm_dt_der(BigDecimal r28_cpdm_dt_der) {
		this.r28_cpdm_dt_der = r28_cpdm_dt_der;
	}
	public BigDecimal getR28_cpdm_dt_dto() {
		return r28_cpdm_dt_dto;
	}
	public void setR28_cpdm_dt_dto(BigDecimal r28_cpdm_dt_dto) {
		this.r28_cpdm_dt_dto = r28_cpdm_dt_dto;
	}
	public BigDecimal getR28_cp() {
		return r28_cp;
	}
	public void setR28_cp(BigDecimal r28_cp) {
		this.r28_cp = r28_cp;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_open_position() {
		return r29_open_position;
	}
	public void setR29_open_position(BigDecimal r29_open_position) {
		this.r29_open_position = r29_open_position;
	}
	public BigDecimal getR29_cpdm_dt_inc() {
		return r29_cpdm_dt_inc;
	}
	public void setR29_cpdm_dt_inc(BigDecimal r29_cpdm_dt_inc) {
		this.r29_cpdm_dt_inc = r29_cpdm_dt_inc;
	}
	public BigDecimal getR29_cpdm_dt_dec() {
		return r29_cpdm_dt_dec;
	}
	public void setR29_cpdm_dt_dec(BigDecimal r29_cpdm_dt_dec) {
		this.r29_cpdm_dt_dec = r29_cpdm_dt_dec;
	}
	public BigDecimal getR29_net() {
		return r29_net;
	}
	public void setR29_net(BigDecimal r29_net) {
		this.r29_net = r29_net;
	}
	public BigDecimal getR29_cpdm_dt_der() {
		return r29_cpdm_dt_der;
	}
	public void setR29_cpdm_dt_der(BigDecimal r29_cpdm_dt_der) {
		this.r29_cpdm_dt_der = r29_cpdm_dt_der;
	}
	public BigDecimal getR29_cpdm_dt_dto() {
		return r29_cpdm_dt_dto;
	}
	public void setR29_cpdm_dt_dto(BigDecimal r29_cpdm_dt_dto) {
		this.r29_cpdm_dt_dto = r29_cpdm_dt_dto;
	}
	public BigDecimal getR29_cp() {
		return r29_cp;
	}
	public void setR29_cp(BigDecimal r29_cp) {
		this.r29_cp = r29_cp;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_open_position() {
		return r30_open_position;
	}
	public void setR30_open_position(BigDecimal r30_open_position) {
		this.r30_open_position = r30_open_position;
	}
	public BigDecimal getR30_cpdm_dt_inc() {
		return r30_cpdm_dt_inc;
	}
	public void setR30_cpdm_dt_inc(BigDecimal r30_cpdm_dt_inc) {
		this.r30_cpdm_dt_inc = r30_cpdm_dt_inc;
	}
	public BigDecimal getR30_cpdm_dt_dec() {
		return r30_cpdm_dt_dec;
	}
	public void setR30_cpdm_dt_dec(BigDecimal r30_cpdm_dt_dec) {
		this.r30_cpdm_dt_dec = r30_cpdm_dt_dec;
	}
	public BigDecimal getR30_net() {
		return r30_net;
	}
	public void setR30_net(BigDecimal r30_net) {
		this.r30_net = r30_net;
	}
	public BigDecimal getR30_cpdm_dt_der() {
		return r30_cpdm_dt_der;
	}
	public void setR30_cpdm_dt_der(BigDecimal r30_cpdm_dt_der) {
		this.r30_cpdm_dt_der = r30_cpdm_dt_der;
	}
	public BigDecimal getR30_cpdm_dt_dto() {
		return r30_cpdm_dt_dto;
	}
	public void setR30_cpdm_dt_dto(BigDecimal r30_cpdm_dt_dto) {
		this.r30_cpdm_dt_dto = r30_cpdm_dt_dto;
	}
	public BigDecimal getR30_cp() {
		return r30_cp;
	}
	public void setR30_cp(BigDecimal r30_cp) {
		this.r30_cp = r30_cp;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_open_position() {
		return r31_open_position;
	}
	public void setR31_open_position(BigDecimal r31_open_position) {
		this.r31_open_position = r31_open_position;
	}
	public BigDecimal getR31_cpdm_dt_inc() {
		return r31_cpdm_dt_inc;
	}
	public void setR31_cpdm_dt_inc(BigDecimal r31_cpdm_dt_inc) {
		this.r31_cpdm_dt_inc = r31_cpdm_dt_inc;
	}
	public BigDecimal getR31_cpdm_dt_dec() {
		return r31_cpdm_dt_dec;
	}
	public void setR31_cpdm_dt_dec(BigDecimal r31_cpdm_dt_dec) {
		this.r31_cpdm_dt_dec = r31_cpdm_dt_dec;
	}
	public BigDecimal getR31_net() {
		return r31_net;
	}
	public void setR31_net(BigDecimal r31_net) {
		this.r31_net = r31_net;
	}
	public BigDecimal getR31_cpdm_dt_der() {
		return r31_cpdm_dt_der;
	}
	public void setR31_cpdm_dt_der(BigDecimal r31_cpdm_dt_der) {
		this.r31_cpdm_dt_der = r31_cpdm_dt_der;
	}
	public BigDecimal getR31_cpdm_dt_dto() {
		return r31_cpdm_dt_dto;
	}
	public void setR31_cpdm_dt_dto(BigDecimal r31_cpdm_dt_dto) {
		this.r31_cpdm_dt_dto = r31_cpdm_dt_dto;
	}
	public BigDecimal getR31_cp() {
		return r31_cp;
	}
	public void setR31_cp(BigDecimal r31_cp) {
		this.r31_cp = r31_cp;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_open_position() {
		return r32_open_position;
	}
	public void setR32_open_position(BigDecimal r32_open_position) {
		this.r32_open_position = r32_open_position;
	}
	public BigDecimal getR32_cpdm_dt_inc() {
		return r32_cpdm_dt_inc;
	}
	public void setR32_cpdm_dt_inc(BigDecimal r32_cpdm_dt_inc) {
		this.r32_cpdm_dt_inc = r32_cpdm_dt_inc;
	}
	public BigDecimal getR32_cpdm_dt_dec() {
		return r32_cpdm_dt_dec;
	}
	public void setR32_cpdm_dt_dec(BigDecimal r32_cpdm_dt_dec) {
		this.r32_cpdm_dt_dec = r32_cpdm_dt_dec;
	}
	public BigDecimal getR32_net() {
		return r32_net;
	}
	public void setR32_net(BigDecimal r32_net) {
		this.r32_net = r32_net;
	}
	public BigDecimal getR32_cpdm_dt_der() {
		return r32_cpdm_dt_der;
	}
	public void setR32_cpdm_dt_der(BigDecimal r32_cpdm_dt_der) {
		this.r32_cpdm_dt_der = r32_cpdm_dt_der;
	}
	public BigDecimal getR32_cpdm_dt_dto() {
		return r32_cpdm_dt_dto;
	}
	public void setR32_cpdm_dt_dto(BigDecimal r32_cpdm_dt_dto) {
		this.r32_cpdm_dt_dto = r32_cpdm_dt_dto;
	}
	public BigDecimal getR32_cp() {
		return r32_cp;
	}
	public void setR32_cp(BigDecimal r32_cp) {
		this.r32_cp = r32_cp;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_open_position() {
		return r33_open_position;
	}
	public void setR33_open_position(BigDecimal r33_open_position) {
		this.r33_open_position = r33_open_position;
	}
	public BigDecimal getR33_cpdm_dt_inc() {
		return r33_cpdm_dt_inc;
	}
	public void setR33_cpdm_dt_inc(BigDecimal r33_cpdm_dt_inc) {
		this.r33_cpdm_dt_inc = r33_cpdm_dt_inc;
	}
	public BigDecimal getR33_cpdm_dt_dec() {
		return r33_cpdm_dt_dec;
	}
	public void setR33_cpdm_dt_dec(BigDecimal r33_cpdm_dt_dec) {
		this.r33_cpdm_dt_dec = r33_cpdm_dt_dec;
	}
	public BigDecimal getR33_net() {
		return r33_net;
	}
	public void setR33_net(BigDecimal r33_net) {
		this.r33_net = r33_net;
	}
	public BigDecimal getR33_cpdm_dt_der() {
		return r33_cpdm_dt_der;
	}
	public void setR33_cpdm_dt_der(BigDecimal r33_cpdm_dt_der) {
		this.r33_cpdm_dt_der = r33_cpdm_dt_der;
	}
	public BigDecimal getR33_cpdm_dt_dto() {
		return r33_cpdm_dt_dto;
	}
	public void setR33_cpdm_dt_dto(BigDecimal r33_cpdm_dt_dto) {
		this.r33_cpdm_dt_dto = r33_cpdm_dt_dto;
	}
	public BigDecimal getR33_cp() {
		return r33_cp;
	}
	public void setR33_cp(BigDecimal r33_cp) {
		this.r33_cp = r33_cp;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_open_position() {
		return r34_open_position;
	}
	public void setR34_open_position(BigDecimal r34_open_position) {
		this.r34_open_position = r34_open_position;
	}
	public BigDecimal getR34_cpdm_dt_inc() {
		return r34_cpdm_dt_inc;
	}
	public void setR34_cpdm_dt_inc(BigDecimal r34_cpdm_dt_inc) {
		this.r34_cpdm_dt_inc = r34_cpdm_dt_inc;
	}
	public BigDecimal getR34_cpdm_dt_dec() {
		return r34_cpdm_dt_dec;
	}
	public void setR34_cpdm_dt_dec(BigDecimal r34_cpdm_dt_dec) {
		this.r34_cpdm_dt_dec = r34_cpdm_dt_dec;
	}
	public BigDecimal getR34_net() {
		return r34_net;
	}
	public void setR34_net(BigDecimal r34_net) {
		this.r34_net = r34_net;
	}
	public BigDecimal getR34_cpdm_dt_der() {
		return r34_cpdm_dt_der;
	}
	public void setR34_cpdm_dt_der(BigDecimal r34_cpdm_dt_der) {
		this.r34_cpdm_dt_der = r34_cpdm_dt_der;
	}
	public BigDecimal getR34_cpdm_dt_dto() {
		return r34_cpdm_dt_dto;
	}
	public void setR34_cpdm_dt_dto(BigDecimal r34_cpdm_dt_dto) {
		this.r34_cpdm_dt_dto = r34_cpdm_dt_dto;
	}
	public BigDecimal getR34_cp() {
		return r34_cp;
	}
	public void setR34_cp(BigDecimal r34_cp) {
		this.r34_cp = r34_cp;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_open_position() {
		return r35_open_position;
	}
	public void setR35_open_position(BigDecimal r35_open_position) {
		this.r35_open_position = r35_open_position;
	}
	public BigDecimal getR35_cpdm_dt_inc() {
		return r35_cpdm_dt_inc;
	}
	public void setR35_cpdm_dt_inc(BigDecimal r35_cpdm_dt_inc) {
		this.r35_cpdm_dt_inc = r35_cpdm_dt_inc;
	}
	public BigDecimal getR35_cpdm_dt_dec() {
		return r35_cpdm_dt_dec;
	}
	public void setR35_cpdm_dt_dec(BigDecimal r35_cpdm_dt_dec) {
		this.r35_cpdm_dt_dec = r35_cpdm_dt_dec;
	}
	public BigDecimal getR35_net() {
		return r35_net;
	}
	public void setR35_net(BigDecimal r35_net) {
		this.r35_net = r35_net;
	}
	public BigDecimal getR35_cpdm_dt_der() {
		return r35_cpdm_dt_der;
	}
	public void setR35_cpdm_dt_der(BigDecimal r35_cpdm_dt_der) {
		this.r35_cpdm_dt_der = r35_cpdm_dt_der;
	}
	public BigDecimal getR35_cpdm_dt_dto() {
		return r35_cpdm_dt_dto;
	}
	public void setR35_cpdm_dt_dto(BigDecimal r35_cpdm_dt_dto) {
		this.r35_cpdm_dt_dto = r35_cpdm_dt_dto;
	}
	public BigDecimal getR35_cp() {
		return r35_cp;
	}
	public void setR35_cp(BigDecimal r35_cp) {
		this.r35_cp = r35_cp;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_open_position() {
		return r36_open_position;
	}
	public void setR36_open_position(BigDecimal r36_open_position) {
		this.r36_open_position = r36_open_position;
	}
	public BigDecimal getR36_cpdm_dt_inc() {
		return r36_cpdm_dt_inc;
	}
	public void setR36_cpdm_dt_inc(BigDecimal r36_cpdm_dt_inc) {
		this.r36_cpdm_dt_inc = r36_cpdm_dt_inc;
	}
	public BigDecimal getR36_cpdm_dt_dec() {
		return r36_cpdm_dt_dec;
	}
	public void setR36_cpdm_dt_dec(BigDecimal r36_cpdm_dt_dec) {
		this.r36_cpdm_dt_dec = r36_cpdm_dt_dec;
	}
	public BigDecimal getR36_net() {
		return r36_net;
	}
	public void setR36_net(BigDecimal r36_net) {
		this.r36_net = r36_net;
	}
	public BigDecimal getR36_cpdm_dt_der() {
		return r36_cpdm_dt_der;
	}
	public void setR36_cpdm_dt_der(BigDecimal r36_cpdm_dt_der) {
		this.r36_cpdm_dt_der = r36_cpdm_dt_der;
	}
	public BigDecimal getR36_cpdm_dt_dto() {
		return r36_cpdm_dt_dto;
	}
	public void setR36_cpdm_dt_dto(BigDecimal r36_cpdm_dt_dto) {
		this.r36_cpdm_dt_dto = r36_cpdm_dt_dto;
	}
	public BigDecimal getR36_cp() {
		return r36_cp;
	}
	public void setR36_cp(BigDecimal r36_cp) {
		this.r36_cp = r36_cp;
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
	public M_BOP_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


}
