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
@Table(name = "BRRS_M_CALOC_SUMMARYTABLE1")
public class M_CALOC_Summary_Entity1 { 
	
	private String	r11_product;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_othercurrencies_inr;
	private BigDecimal	r11_othercurrencies_aud;
	private BigDecimal	r11_othercurrencies_i;
	private BigDecimal	r11_othercurrencies_j;
	private BigDecimal	r11_othercurrencies_k;
	private BigDecimal	r11_othercurrencies_l;
	private BigDecimal	r11_othercurrencies_m;
	private BigDecimal	r11_othercurrencies_n;
	private BigDecimal	r11_othercurrencies_0;
	private BigDecimal	r11_othercurrencies_p;
	private BigDecimal	r11_othercurrencies_q;
	private BigDecimal	r11_othercurrencies_r;
	private BigDecimal	r11_othercurrencies_s;
	private BigDecimal	r11_othercurrencies_t;
	private BigDecimal	r11_othercurrencies_u;
	private BigDecimal	r11_othercurrencies_v;
	private BigDecimal	r11_othercurrencies_w;
	private String	r12_product;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_othercurrencies_inr;
	private BigDecimal	r12_othercurrencies_aud;
	private BigDecimal	r12_othercurrencies_i;
	private BigDecimal	r12_othercurrencies_j;
	private BigDecimal	r12_othercurrencies_k;
	private BigDecimal	r12_othercurrencies_l;
	private BigDecimal	r12_othercurrencies_m;
	private BigDecimal	r12_othercurrencies_n;
	private BigDecimal	r12_othercurrencies_0;
	private BigDecimal	r12_othercurrencies_p;
	private BigDecimal	r12_othercurrencies_q;
	private BigDecimal	r12_othercurrencies_r;
	private BigDecimal	r12_othercurrencies_s;
	private BigDecimal	r12_othercurrencies_t;
	private BigDecimal	r12_othercurrencies_u;
	private BigDecimal	r12_othercurrencies_v;
	private BigDecimal	r12_othercurrencies_w;
	private String	r13_product;
	private BigDecimal	r13_pula;
	private BigDecimal	r13_usd;
	private BigDecimal	r13_zar;
	private BigDecimal	r13_gbp;
	private BigDecimal	r13_euro;
	private BigDecimal	r13_othercurrencies_inr;
	private BigDecimal	r13_othercurrencies_aud;
	private BigDecimal	r13_othercurrencies_i;
	private BigDecimal	r13_othercurrencies_j;
	private BigDecimal	r13_othercurrencies_k;
	private BigDecimal	r13_othercurrencies_l;
	private BigDecimal	r13_othercurrencies_m;
	private BigDecimal	r13_othercurrencies_n;
	private BigDecimal	r13_othercurrencies_0;
	private BigDecimal	r13_othercurrencies_p;
	private BigDecimal	r13_othercurrencies_q;
	private BigDecimal	r13_othercurrencies_r;
	private BigDecimal	r13_othercurrencies_s;
	private BigDecimal	r13_othercurrencies_t;
	private BigDecimal	r13_othercurrencies_u;
	private BigDecimal	r13_othercurrencies_v;
	private BigDecimal	r13_othercurrencies_w;
	private String	r14_product;
	private BigDecimal	r14_pula;
	private BigDecimal	r14_usd;
	private BigDecimal	r14_zar;
	private BigDecimal	r14_gbp;
	private BigDecimal	r14_euro;
	private BigDecimal	r14_othercurrencies_inr;
	private BigDecimal	r14_othercurrencies_aud;
	private BigDecimal	r14_othercurrencies_i;
	private BigDecimal	r14_othercurrencies_j;
	private BigDecimal	r14_othercurrencies_k;
	private BigDecimal	r14_othercurrencies_l;
	private BigDecimal	r14_othercurrencies_m;
	private BigDecimal	r14_othercurrencies_n;
	private BigDecimal	r14_othercurrencies_0;
	private BigDecimal	r14_othercurrencies_p;
	private BigDecimal	r14_othercurrencies_q;
	private BigDecimal	r14_othercurrencies_r;
	private BigDecimal	r14_othercurrencies_s;
	private BigDecimal	r14_othercurrencies_t;
	private BigDecimal	r14_othercurrencies_u;
	private BigDecimal	r14_othercurrencies_v;
	private BigDecimal	r14_othercurrencies_w;
	private String	r15_product;
	private BigDecimal	r15_pula;
	private BigDecimal	r15_usd;
	private BigDecimal	r15_zar;
	private BigDecimal	r15_gbp;
	private BigDecimal	r15_euro;
	private BigDecimal	r15_othercurrencies_inr;
	private BigDecimal	r15_othercurrencies_aud;
	private BigDecimal	r15_othercurrencies_i;
	private BigDecimal	r15_othercurrencies_j;
	private BigDecimal	r15_othercurrencies_k;
	private BigDecimal	r15_othercurrencies_l;
	private BigDecimal	r15_othercurrencies_m;
	private BigDecimal	r15_othercurrencies_n;
	private BigDecimal	r15_othercurrencies_0;
	private BigDecimal	r15_othercurrencies_p;
	private BigDecimal	r15_othercurrencies_q;
	private BigDecimal	r15_othercurrencies_r;
	private BigDecimal	r15_othercurrencies_s;
	private BigDecimal	r15_othercurrencies_t;
	private BigDecimal	r15_othercurrencies_u;
	private BigDecimal	r15_othercurrencies_v;
	private BigDecimal	r15_othercurrencies_w;
	private String	r16_product;
	private BigDecimal	r16_pula;
	private BigDecimal	r16_usd;
	private BigDecimal	r16_zar;
	private BigDecimal	r16_gbp;
	private BigDecimal	r16_euro;
	private BigDecimal	r16_othercurrencies_inr;
	private BigDecimal	r16_othercurrencies_aud;
	private BigDecimal	r16_othercurrencies_i;
	private BigDecimal	r16_othercurrencies_j;
	private BigDecimal	r16_othercurrencies_k;
	private BigDecimal	r16_othercurrencies_l;
	private BigDecimal	r16_othercurrencies_m;
	private BigDecimal	r16_othercurrencies_n;
	private BigDecimal	r16_othercurrencies_0;
	private BigDecimal	r16_othercurrencies_p;
	private BigDecimal	r16_othercurrencies_q;
	private BigDecimal	r16_othercurrencies_r;
	private BigDecimal	r16_othercurrencies_s;
	private BigDecimal	r16_othercurrencies_t;
	private BigDecimal	r16_othercurrencies_u;
	private BigDecimal	r16_othercurrencies_v;
	private BigDecimal	r16_othercurrencies_w;
	private String	r17_product;
	private BigDecimal	r17_pula;
	private BigDecimal	r17_usd;
	private BigDecimal	r17_zar;
	private BigDecimal	r17_gbp;
	private BigDecimal	r17_euro;
	private BigDecimal	r17_othercurrencies_inr;
	private BigDecimal	r17_othercurrencies_aud;
	private BigDecimal	r17_othercurrencies_i;
	private BigDecimal	r17_othercurrencies_j;
	private BigDecimal	r17_othercurrencies_k;
	private BigDecimal	r17_othercurrencies_l;
	private BigDecimal	r17_othercurrencies_m;
	private BigDecimal	r17_othercurrencies_n;
	private BigDecimal	r17_othercurrencies_0;
	private BigDecimal	r17_othercurrencies_p;
	private BigDecimal	r17_othercurrencies_q;
	private BigDecimal	r17_othercurrencies_r;
	private BigDecimal	r17_othercurrencies_s;
	private BigDecimal	r17_othercurrencies_t;
	private BigDecimal	r17_othercurrencies_u;
	private BigDecimal	r17_othercurrencies_v;
	private BigDecimal	r17_othercurrencies_w;
	private String	r18_product;
	private BigDecimal	r18_pula;
	private BigDecimal	r18_usd;
	private BigDecimal	r18_zar;
	private BigDecimal	r18_gbp;
	private BigDecimal	r18_euro;
	private BigDecimal	r18_othercurrencies_inr;
	private BigDecimal	r18_othercurrencies_aud;
	private BigDecimal	r18_othercurrencies_i;
	private BigDecimal	r18_othercurrencies_j;
	private BigDecimal	r18_othercurrencies_k;
	private BigDecimal	r18_othercurrencies_l;
	private BigDecimal	r18_othercurrencies_m;
	private BigDecimal	r18_othercurrencies_n;
	private BigDecimal	r18_othercurrencies_0;
	private BigDecimal	r18_othercurrencies_p;
	private BigDecimal	r18_othercurrencies_q;
	private BigDecimal	r18_othercurrencies_r;
	private BigDecimal	r18_othercurrencies_s;
	private BigDecimal	r18_othercurrencies_t;
	private BigDecimal	r18_othercurrencies_u;
	private BigDecimal	r18_othercurrencies_v;
	private BigDecimal	r18_othercurrencies_w;
	private String	r19_product;
	private BigDecimal	r19_pula;
	private BigDecimal	r19_usd;
	private BigDecimal	r19_zar;
	private BigDecimal	r19_gbp;
	private BigDecimal	r19_euro;
	private BigDecimal	r19_othercurrencies_inr;
	private BigDecimal	r19_othercurrencies_aud;
	private BigDecimal	r19_othercurrencies_i;
	private BigDecimal	r19_othercurrencies_j;
	private BigDecimal	r19_othercurrencies_k;
	private BigDecimal	r19_othercurrencies_l;
	private BigDecimal	r19_othercurrencies_m;
	private BigDecimal	r19_othercurrencies_n;
	private BigDecimal	r19_othercurrencies_0;
	private BigDecimal	r19_othercurrencies_p;
	private BigDecimal	r19_othercurrencies_q;
	private BigDecimal	r19_othercurrencies_r;
	private BigDecimal	r19_othercurrencies_s;
	private BigDecimal	r19_othercurrencies_t;
	private BigDecimal	r19_othercurrencies_u;
	private BigDecimal	r19_othercurrencies_v;
	private BigDecimal	r19_othercurrencies_w;
	private String	r20_product;
	private BigDecimal	r20_pula;
	private BigDecimal	r20_usd;
	private BigDecimal	r20_zar;
	private BigDecimal	r20_gbp;
	private BigDecimal	r20_euro;
	private BigDecimal	r20_othercurrencies_inr;
	private BigDecimal	r20_othercurrencies_aud;
	private BigDecimal	r20_othercurrencies_i;
	private BigDecimal	r20_othercurrencies_j;
	private BigDecimal	r20_othercurrencies_k;
	private BigDecimal	r20_othercurrencies_l;
	private BigDecimal	r20_othercurrencies_m;
	private BigDecimal	r20_othercurrencies_n;
	private BigDecimal	r20_othercurrencies_0;
	private BigDecimal	r20_othercurrencies_p;
	private BigDecimal	r20_othercurrencies_q;
	private BigDecimal	r20_othercurrencies_r;
	private BigDecimal	r20_othercurrencies_s;
	private BigDecimal	r20_othercurrencies_t;
	private BigDecimal	r20_othercurrencies_u;
	private BigDecimal	r20_othercurrencies_v;
	private BigDecimal	r20_othercurrencies_w;
	private String	r21_product;
	private BigDecimal	r21_pula;
	private BigDecimal	r21_usd;
	private BigDecimal	r21_zar;
	private BigDecimal	r21_gbp;
	private BigDecimal	r21_euro;
	private BigDecimal	r21_othercurrencies_inr;
	private BigDecimal	r21_othercurrencies_aud;
	private BigDecimal	r21_othercurrencies_i;
	private BigDecimal	r21_othercurrencies_j;
	private BigDecimal	r21_othercurrencies_k;
	private BigDecimal	r21_othercurrencies_l;
	private BigDecimal	r21_othercurrencies_m;
	private BigDecimal	r21_othercurrencies_n;
	private BigDecimal	r21_othercurrencies_0;
	private BigDecimal	r21_othercurrencies_p;
	private BigDecimal	r21_othercurrencies_q;
	private BigDecimal	r21_othercurrencies_r;
	private BigDecimal	r21_othercurrencies_s;
	private BigDecimal	r21_othercurrencies_t;
	private BigDecimal	r21_othercurrencies_u;
	private BigDecimal	r21_othercurrencies_v;
	private BigDecimal	r21_othercurrencies_w;
	private String	r22_product;
	private BigDecimal	r22_pula;
	private BigDecimal	r22_usd;
	private BigDecimal	r22_zar;
	private BigDecimal	r22_gbp;
	private BigDecimal	r22_euro;
	private BigDecimal	r22_othercurrencies_inr;
	private BigDecimal	r22_othercurrencies_aud;
	private BigDecimal	r22_othercurrencies_i;
	private BigDecimal	r22_othercurrencies_j;
	private BigDecimal	r22_othercurrencies_k;
	private BigDecimal	r22_othercurrencies_l;
	private BigDecimal	r22_othercurrencies_m;
	private BigDecimal	r22_othercurrencies_n;
	private BigDecimal	r22_othercurrencies_0;
	private BigDecimal	r22_othercurrencies_p;
	private BigDecimal	r22_othercurrencies_q;
	private BigDecimal	r22_othercurrencies_r;
	private BigDecimal	r22_othercurrencies_s;
	private BigDecimal	r22_othercurrencies_t;
	private BigDecimal	r22_othercurrencies_u;
	private BigDecimal	r22_othercurrencies_v;
	private BigDecimal	r22_othercurrencies_w;
	private String	r23_product;
	private BigDecimal	r23_pula;
	private BigDecimal	r23_usd;
	private BigDecimal	r23_zar;
	private BigDecimal	r23_gbp;
	private BigDecimal	r23_euro;
	private BigDecimal	r23_othercurrencies_inr;
	private BigDecimal	r23_othercurrencies_aud;
	private BigDecimal	r23_othercurrencies_i;
	private BigDecimal	r23_othercurrencies_j;
	private BigDecimal	r23_othercurrencies_k;
	private BigDecimal	r23_othercurrencies_l;
	private BigDecimal	r23_othercurrencies_m;
	private BigDecimal	r23_othercurrencies_n;
	private BigDecimal	r23_othercurrencies_0;
	private BigDecimal	r23_othercurrencies_p;
	private BigDecimal	r23_othercurrencies_q;
	private BigDecimal	r23_othercurrencies_r;
	private BigDecimal	r23_othercurrencies_s;
	private BigDecimal	r23_othercurrencies_t;
	private BigDecimal	r23_othercurrencies_u;
	private BigDecimal	r23_othercurrencies_v;
	private BigDecimal	r23_othercurrencies_w;
	private String	r24_product;
	private BigDecimal	r24_pula;
	private BigDecimal	r24_usd;
	private BigDecimal	r24_zar;
	private BigDecimal	r24_gbp;
	private BigDecimal	r24_euro;
	private BigDecimal	r24_othercurrencies_inr;
	private BigDecimal	r24_othercurrencies_aud;
	private BigDecimal	r24_othercurrencies_i;
	private BigDecimal	r24_othercurrencies_j;
	private BigDecimal	r24_othercurrencies_k;
	private BigDecimal	r24_othercurrencies_l;
	private BigDecimal	r24_othercurrencies_m;
	private BigDecimal	r24_othercurrencies_n;
	private BigDecimal	r24_othercurrencies_0;
	private BigDecimal	r24_othercurrencies_p;
	private BigDecimal	r24_othercurrencies_q;
	private BigDecimal	r24_othercurrencies_r;
	private BigDecimal	r24_othercurrencies_s;
	private BigDecimal	r24_othercurrencies_t;
	private BigDecimal	r24_othercurrencies_u;
	private BigDecimal	r24_othercurrencies_v;
	private BigDecimal	r24_othercurrencies_w;
	private String	r25_product;
	private BigDecimal	r25_pula;
	private BigDecimal	r25_usd;
	private BigDecimal	r25_zar;
	private BigDecimal	r25_gbp;
	private BigDecimal	r25_euro;
	private BigDecimal	r25_othercurrencies_inr;
	private BigDecimal	r25_othercurrencies_aud;
	private BigDecimal	r25_othercurrencies_i;
	private BigDecimal	r25_othercurrencies_j;
	private BigDecimal	r25_othercurrencies_k;
	private BigDecimal	r25_othercurrencies_l;
	private BigDecimal	r25_othercurrencies_m;
	private BigDecimal	r25_othercurrencies_n;
	private BigDecimal	r25_othercurrencies_0;
	private BigDecimal	r25_othercurrencies_p;
	private BigDecimal	r25_othercurrencies_q;
	private BigDecimal	r25_othercurrencies_r;
	private BigDecimal	r25_othercurrencies_s;
	private BigDecimal	r25_othercurrencies_t;
	private BigDecimal	r25_othercurrencies_u;
	private BigDecimal	r25_othercurrencies_v;
	private BigDecimal	r25_othercurrencies_w;
	private String	r26_product;
	private BigDecimal	r26_pula;
	private BigDecimal	r26_usd;
	private BigDecimal	r26_zar;
	private BigDecimal	r26_gbp;
	private BigDecimal	r26_euro;
	private BigDecimal	r26_othercurrencies_inr;
	private BigDecimal	r26_othercurrencies_aud;
	private BigDecimal	r26_othercurrencies_i;
	private BigDecimal	r26_othercurrencies_j;
	private BigDecimal	r26_othercurrencies_k;
	private BigDecimal	r26_othercurrencies_l;
	private BigDecimal	r26_othercurrencies_m;
	private BigDecimal	r26_othercurrencies_n;
	private BigDecimal	r26_othercurrencies_0;
	private BigDecimal	r26_othercurrencies_p;
	private BigDecimal	r26_othercurrencies_q;
	private BigDecimal	r26_othercurrencies_r;
	private BigDecimal	r26_othercurrencies_s;
	private BigDecimal	r26_othercurrencies_t;
	private BigDecimal	r26_othercurrencies_u;
	private BigDecimal	r26_othercurrencies_v;
	private BigDecimal	r26_othercurrencies_w;
	private String	r27_product;
	private BigDecimal	r27_pula;
	private BigDecimal	r27_usd;
	private BigDecimal	r27_zar;
	private BigDecimal	r27_gbp;
	private BigDecimal	r27_euro;
	private BigDecimal	r27_othercurrencies_inr;
	private BigDecimal	r27_othercurrencies_aud;
	private BigDecimal	r27_othercurrencies_i;
	private BigDecimal	r27_othercurrencies_j;
	private BigDecimal	r27_othercurrencies_k;
	private BigDecimal	r27_othercurrencies_l;
	private BigDecimal	r27_othercurrencies_m;
	private BigDecimal	r27_othercurrencies_n;
	private BigDecimal	r27_othercurrencies_0;
	private BigDecimal	r27_othercurrencies_p;
	private BigDecimal	r27_othercurrencies_q;
	private BigDecimal	r27_othercurrencies_r;
	private BigDecimal	r27_othercurrencies_s;
	private BigDecimal	r27_othercurrencies_t;
	private BigDecimal	r27_othercurrencies_u;
	private BigDecimal	r27_othercurrencies_v;
	private BigDecimal	r27_othercurrencies_w;
	private String	r28_product;
	private BigDecimal	r28_pula;
	private BigDecimal	r28_usd;
	private BigDecimal	r28_zar;
	private BigDecimal	r28_gbp;
	private BigDecimal	r28_euro;
	private BigDecimal	r28_othercurrencies_inr;
	private BigDecimal	r28_othercurrencies_aud;
	private BigDecimal	r28_othercurrencies_i;
	private BigDecimal	r28_othercurrencies_j;
	private BigDecimal	r28_othercurrencies_k;
	private BigDecimal	r28_othercurrencies_l;
	private BigDecimal	r28_othercurrencies_m;
	private BigDecimal	r28_othercurrencies_n;
	private BigDecimal	r28_othercurrencies_0;
	private BigDecimal	r28_othercurrencies_p;
	private BigDecimal	r28_othercurrencies_q;
	private BigDecimal	r28_othercurrencies_r;
	private BigDecimal	r28_othercurrencies_s;
	private BigDecimal	r28_othercurrencies_t;
	private BigDecimal	r28_othercurrencies_u;
	private BigDecimal	r28_othercurrencies_v;
	private BigDecimal	r28_othercurrencies_w;
	private String	r29_product;
	private BigDecimal	r29_pula;
	private BigDecimal	r29_usd;
	private BigDecimal	r29_zar;
	private BigDecimal	r29_gbp;
	private BigDecimal	r29_euro;
	private BigDecimal	r29_othercurrencies_inr;
	private BigDecimal	r29_othercurrencies_aud;
	private BigDecimal	r29_othercurrencies_i;
	private BigDecimal	r29_othercurrencies_j;
	private BigDecimal	r29_othercurrencies_k;
	private BigDecimal	r29_othercurrencies_l;
	private BigDecimal	r29_othercurrencies_m;
	private BigDecimal	r29_othercurrencies_n;
	private BigDecimal	r29_othercurrencies_0;
	private BigDecimal	r29_othercurrencies_p;
	private BigDecimal	r29_othercurrencies_q;
	private BigDecimal	r29_othercurrencies_r;
	private BigDecimal	r29_othercurrencies_s;
	private BigDecimal	r29_othercurrencies_t;
	private BigDecimal	r29_othercurrencies_u;
	private BigDecimal	r29_othercurrencies_v;
	private BigDecimal	r29_othercurrencies_w;
	private String	r30_product;
	private BigDecimal	r30_pula;
	private BigDecimal	r30_usd;
	private BigDecimal	r30_zar;
	private BigDecimal	r30_gbp;
	private BigDecimal	r30_euro;
	private BigDecimal	r30_othercurrencies_inr;
	private BigDecimal	r30_othercurrencies_aud;
	private BigDecimal	r30_othercurrencies_i;
	private BigDecimal	r30_othercurrencies_j;
	private BigDecimal	r30_othercurrencies_k;
	private BigDecimal	r30_othercurrencies_l;
	private BigDecimal	r30_othercurrencies_m;
	private BigDecimal	r30_othercurrencies_n;
	private BigDecimal	r30_othercurrencies_0;
	private BigDecimal	r30_othercurrencies_p;
	private BigDecimal	r30_othercurrencies_q;
	private BigDecimal	r30_othercurrencies_r;
	private BigDecimal	r30_othercurrencies_s;
	private BigDecimal	r30_othercurrencies_t;
	private BigDecimal	r30_othercurrencies_u;
	private BigDecimal	r30_othercurrencies_v;
	private BigDecimal	r30_othercurrencies_w;
	private String	r31_product;
	private BigDecimal	r31_pula;
	private BigDecimal	r31_usd;
	private BigDecimal	r31_zar;
	private BigDecimal	r31_gbp;
	private BigDecimal	r31_euro;
	private BigDecimal	r31_othercurrencies_inr;
	private BigDecimal	r31_othercurrencies_aud;
	private BigDecimal	r31_othercurrencies_i;
	private BigDecimal	r31_othercurrencies_j;
	private BigDecimal	r31_othercurrencies_k;
	private BigDecimal	r31_othercurrencies_l;
	private BigDecimal	r31_othercurrencies_m;
	private BigDecimal	r31_othercurrencies_n;
	private BigDecimal	r31_othercurrencies_0;
	private BigDecimal	r31_othercurrencies_p;
	private BigDecimal	r31_othercurrencies_q;
	private BigDecimal	r31_othercurrencies_r;
	private BigDecimal	r31_othercurrencies_s;
	private BigDecimal	r31_othercurrencies_t;
	private BigDecimal	r31_othercurrencies_u;
	private BigDecimal	r31_othercurrencies_v;
	private BigDecimal	r31_othercurrencies_w;
	private String	r32_product;
	private BigDecimal	r32_pula;
	private BigDecimal	r32_usd;
	private BigDecimal	r32_zar;
	private BigDecimal	r32_gbp;
	private BigDecimal	r32_euro;
	private BigDecimal	r32_othercurrencies_inr;
	private BigDecimal	r32_othercurrencies_aud;
	private BigDecimal	r32_othercurrencies_i;
	private BigDecimal	r32_othercurrencies_j;
	private BigDecimal	r32_othercurrencies_k;
	private BigDecimal	r32_othercurrencies_l;
	private BigDecimal	r32_othercurrencies_m;
	private BigDecimal	r32_othercurrencies_n;
	private BigDecimal	r32_othercurrencies_0;
	private BigDecimal	r32_othercurrencies_p;
	private BigDecimal	r32_othercurrencies_q;
	private BigDecimal	r32_othercurrencies_r;
	private BigDecimal	r32_othercurrencies_s;
	private BigDecimal	r32_othercurrencies_t;
	private BigDecimal	r32_othercurrencies_u;
	private BigDecimal	r32_othercurrencies_v;
	private BigDecimal	r32_othercurrencies_w;
	private String	r33_product;
	private BigDecimal	r33_pula;
	private BigDecimal	r33_usd;
	private BigDecimal	r33_zar;
	private BigDecimal	r33_gbp;
	private BigDecimal	r33_euro;
	private BigDecimal	r33_othercurrencies_inr;
	private BigDecimal	r33_othercurrencies_aud;
	private BigDecimal	r33_othercurrencies_i;
	private BigDecimal	r33_othercurrencies_j;
	private BigDecimal	r33_othercurrencies_k;
	private BigDecimal	r33_othercurrencies_l;
	private BigDecimal	r33_othercurrencies_m;
	private BigDecimal	r33_othercurrencies_n;
	private BigDecimal	r33_othercurrencies_0;
	private BigDecimal	r33_othercurrencies_p;
	private BigDecimal	r33_othercurrencies_q;
	private BigDecimal	r33_othercurrencies_r;
	private BigDecimal	r33_othercurrencies_s;
	private BigDecimal	r33_othercurrencies_t;
	private BigDecimal	r33_othercurrencies_u;
	private BigDecimal	r33_othercurrencies_v;
	private BigDecimal	r33_othercurrencies_w;
	private String	r34_product;
	private BigDecimal	r34_pula;
	private BigDecimal	r34_usd;
	private BigDecimal	r34_zar;
	private BigDecimal	r34_gbp;
	private BigDecimal	r34_euro;
	private BigDecimal	r34_othercurrencies_inr;
	private BigDecimal	r34_othercurrencies_aud;
	private BigDecimal	r34_othercurrencies_i;
	private BigDecimal	r34_othercurrencies_j;
	private BigDecimal	r34_othercurrencies_k;
	private BigDecimal	r34_othercurrencies_l;
	private BigDecimal	r34_othercurrencies_m;
	private BigDecimal	r34_othercurrencies_n;
	private BigDecimal	r34_othercurrencies_0;
	private BigDecimal	r34_othercurrencies_p;
	private BigDecimal	r34_othercurrencies_q;
	private BigDecimal	r34_othercurrencies_r;
	private BigDecimal	r34_othercurrencies_s;
	private BigDecimal	r34_othercurrencies_t;
	private BigDecimal	r34_othercurrencies_u;
	private BigDecimal	r34_othercurrencies_v;
	private BigDecimal	r34_othercurrencies_w;
	private String	r35_product;
	private BigDecimal	r35_pula;
	private BigDecimal	r35_usd;
	private BigDecimal	r35_zar;
	private BigDecimal	r35_gbp;
	private BigDecimal	r35_euro;
	private BigDecimal	r35_othercurrencies_inr;
	private BigDecimal	r35_othercurrencies_aud;
	private BigDecimal	r35_othercurrencies_i;
	private BigDecimal	r35_othercurrencies_j;
	private BigDecimal	r35_othercurrencies_k;
	private BigDecimal	r35_othercurrencies_l;
	private BigDecimal	r35_othercurrencies_m;
	private BigDecimal	r35_othercurrencies_n;
	private BigDecimal	r35_othercurrencies_0;
	private BigDecimal	r35_othercurrencies_p;
	private BigDecimal	r35_othercurrencies_q;
	private BigDecimal	r35_othercurrencies_r;
	private BigDecimal	r35_othercurrencies_s;
	private BigDecimal	r35_othercurrencies_t;
	private BigDecimal	r35_othercurrencies_u;
	private BigDecimal	r35_othercurrencies_v;
	private BigDecimal	r35_othercurrencies_w;
	private String	r36_product;
	private BigDecimal	r36_pula;
	private BigDecimal	r36_usd;
	private BigDecimal	r36_zar;
	private BigDecimal	r36_gbp;
	private BigDecimal	r36_euro;
	private BigDecimal	r36_othercurrencies_inr;
	private BigDecimal	r36_othercurrencies_aud;
	private BigDecimal	r36_othercurrencies_i;
	private BigDecimal	r36_othercurrencies_j;
	private BigDecimal	r36_othercurrencies_k;
	private BigDecimal	r36_othercurrencies_l;
	private BigDecimal	r36_othercurrencies_m;
	private BigDecimal	r36_othercurrencies_n;
	private BigDecimal	r36_othercurrencies_0;
	private BigDecimal	r36_othercurrencies_p;
	private BigDecimal	r36_othercurrencies_q;
	private BigDecimal	r36_othercurrencies_r;
	private BigDecimal	r36_othercurrencies_s;
	private BigDecimal	r36_othercurrencies_t;
	private BigDecimal	r36_othercurrencies_u;
	private BigDecimal	r36_othercurrencies_v;
	private BigDecimal	r36_othercurrencies_w;
	private String	r37_product;
	private BigDecimal	r37_pula;
	private BigDecimal	r37_usd;
	private BigDecimal	r37_zar;
	private BigDecimal	r37_gbp;
	private BigDecimal	r37_euro;
	private BigDecimal	r37_othercurrencies_inr;
	private BigDecimal	r37_othercurrencies_aud;
	private BigDecimal	r37_othercurrencies_i;
	private BigDecimal	r37_othercurrencies_j;
	private BigDecimal	r37_othercurrencies_k;
	private BigDecimal	r37_othercurrencies_l;
	private BigDecimal	r37_othercurrencies_m;
	private BigDecimal	r37_othercurrencies_n;
	private BigDecimal	r37_othercurrencies_0;
	private BigDecimal	r37_othercurrencies_p;
	private BigDecimal	r37_othercurrencies_q;
	private BigDecimal	r37_othercurrencies_r;
	private BigDecimal	r37_othercurrencies_s;
	private BigDecimal	r37_othercurrencies_t;
	private BigDecimal	r37_othercurrencies_u;
	private BigDecimal	r37_othercurrencies_v;
	private BigDecimal	r37_othercurrencies_w;
	private String	r38_product;
	private BigDecimal	r38_pula;
	private BigDecimal	r38_usd;
	private BigDecimal	r38_zar;
	private BigDecimal	r38_gbp;
	private BigDecimal	r38_euro;
	private BigDecimal	r38_othercurrencies_inr;
	private BigDecimal	r38_othercurrencies_aud;
	private BigDecimal	r38_othercurrencies_i;
	private BigDecimal	r38_othercurrencies_j;
	private BigDecimal	r38_othercurrencies_k;
	private BigDecimal	r38_othercurrencies_l;
	private BigDecimal	r38_othercurrencies_m;
	private BigDecimal	r38_othercurrencies_n;
	private BigDecimal	r38_othercurrencies_0;
	private BigDecimal	r38_othercurrencies_p;
	private BigDecimal	r38_othercurrencies_q;
	private BigDecimal	r38_othercurrencies_r;
	private BigDecimal	r38_othercurrencies_s;
	private BigDecimal	r38_othercurrencies_t;
	private BigDecimal	r38_othercurrencies_u;
	private BigDecimal	r38_othercurrencies_v;
	private BigDecimal	r38_othercurrencies_w;
	private String	r39_product;
	private BigDecimal	r39_pula;
	private BigDecimal	r39_usd;
	private BigDecimal	r39_zar;
	private BigDecimal	r39_gbp;
	private BigDecimal	r39_euro;
	private BigDecimal	r39_othercurrencies_inr;
	private BigDecimal	r39_othercurrencies_aud;
	private BigDecimal	r39_othercurrencies_i;
	private BigDecimal	r39_othercurrencies_j;
	private BigDecimal	r39_othercurrencies_k;
	private BigDecimal	r39_othercurrencies_l;
	private BigDecimal	r39_othercurrencies_m;
	private BigDecimal	r39_othercurrencies_n;
	private BigDecimal	r39_othercurrencies_0;
	private BigDecimal	r39_othercurrencies_p;
	private BigDecimal	r39_othercurrencies_q;
	private BigDecimal	r39_othercurrencies_r;
	private BigDecimal	r39_othercurrencies_s;
	private BigDecimal	r39_othercurrencies_t;
	private BigDecimal	r39_othercurrencies_u;
	private BigDecimal	r39_othercurrencies_v;
	private BigDecimal	r39_othercurrencies_w;
	private String	r40_product;
	private BigDecimal	r40_pula;
	private BigDecimal	r40_usd;
	private BigDecimal	r40_zar;
	private BigDecimal	r40_gbp;
	private BigDecimal	r40_euro;
	private BigDecimal	r40_othercurrencies_inr;
	private BigDecimal	r40_othercurrencies_aud;
	private BigDecimal	r40_othercurrencies_i;
	private BigDecimal	r40_othercurrencies_j;
	private BigDecimal	r40_othercurrencies_k;
	private BigDecimal	r40_othercurrencies_l;
	private BigDecimal	r40_othercurrencies_m;
	private BigDecimal	r40_othercurrencies_n;
	private BigDecimal	r40_othercurrencies_0;
	private BigDecimal	r40_othercurrencies_p;
	private BigDecimal	r40_othercurrencies_q;
	private BigDecimal	r40_othercurrencies_r;
	private BigDecimal	r40_othercurrencies_s;
	private BigDecimal	r40_othercurrencies_t;
	private BigDecimal	r40_othercurrencies_u;
	private BigDecimal	r40_othercurrencies_v;
	private BigDecimal	r40_othercurrencies_w;
	private String	r41_product;
	private BigDecimal	r41_pula;
	private BigDecimal	r41_usd;
	private BigDecimal	r41_zar;
	private BigDecimal	r41_gbp;
	private BigDecimal	r41_euro;
	private BigDecimal	r41_othercurrencies_inr;
	private BigDecimal	r41_othercurrencies_aud;
	private BigDecimal	r41_othercurrencies_i;
	private BigDecimal	r41_othercurrencies_j;
	private BigDecimal	r41_othercurrencies_k;
	private BigDecimal	r41_othercurrencies_l;
	private BigDecimal	r41_othercurrencies_m;
	private BigDecimal	r41_othercurrencies_n;
	private BigDecimal	r41_othercurrencies_0;
	private BigDecimal	r41_othercurrencies_p;
	private BigDecimal	r41_othercurrencies_q;
	private BigDecimal	r41_othercurrencies_r;
	private BigDecimal	r41_othercurrencies_s;
	private BigDecimal	r41_othercurrencies_t;
	private BigDecimal	r41_othercurrencies_u;
	private BigDecimal	r41_othercurrencies_v;
	private BigDecimal	r41_othercurrencies_w;
	private String	r42_product;
	private BigDecimal	r42_pula;
	private BigDecimal	r42_usd;
	private BigDecimal	r42_zar;
	private BigDecimal	r42_gbp;
	private BigDecimal	r42_euro;
	private BigDecimal	r42_othercurrencies_inr;
	private BigDecimal	r42_othercurrencies_aud;
	private BigDecimal	r42_othercurrencies_i;
	private BigDecimal	r42_othercurrencies_j;
	private BigDecimal	r42_othercurrencies_k;
	private BigDecimal	r42_othercurrencies_l;
	private BigDecimal	r42_othercurrencies_m;
	private BigDecimal	r42_othercurrencies_n;
	private BigDecimal	r42_othercurrencies_0;
	private BigDecimal	r42_othercurrencies_p;
	private BigDecimal	r42_othercurrencies_q;
	private BigDecimal	r42_othercurrencies_r;
	private BigDecimal	r42_othercurrencies_s;
	private BigDecimal	r42_othercurrencies_t;
	private BigDecimal	r42_othercurrencies_u;
	private BigDecimal	r42_othercurrencies_v;
	private BigDecimal	r42_othercurrencies_w;
	private String	r43_product;
	private BigDecimal	r43_pula;
	private BigDecimal	r43_usd;
	private BigDecimal	r43_zar;
	private BigDecimal	r43_gbp;
	private BigDecimal	r43_euro;
	private BigDecimal	r43_othercurrencies_inr;
	private BigDecimal	r43_othercurrencies_aud;
	private BigDecimal	r43_othercurrencies_i;
	private BigDecimal	r43_othercurrencies_j;
	private BigDecimal	r43_othercurrencies_k;
	private BigDecimal	r43_othercurrencies_l;
	private BigDecimal	r43_othercurrencies_m;
	private BigDecimal	r43_othercurrencies_n;
	private BigDecimal	r43_othercurrencies_0;
	private BigDecimal	r43_othercurrencies_p;
	private BigDecimal	r43_othercurrencies_q;
	private BigDecimal	r43_othercurrencies_r;
	private BigDecimal	r43_othercurrencies_s;
	private BigDecimal	r43_othercurrencies_t;
	private BigDecimal	r43_othercurrencies_u;
	private BigDecimal	r43_othercurrencies_v;
	private BigDecimal	r43_othercurrencies_w;
	private String	r44_product;
	private BigDecimal	r44_pula;
	private BigDecimal	r44_usd;
	private BigDecimal	r44_zar;
	private BigDecimal	r44_gbp;
	private BigDecimal	r44_euro;
	private BigDecimal	r44_othercurrencies_inr;
	private BigDecimal	r44_othercurrencies_aud;
	private BigDecimal	r44_othercurrencies_i;
	private BigDecimal	r44_othercurrencies_j;
	private BigDecimal	r44_othercurrencies_k;
	private BigDecimal	r44_othercurrencies_l;
	private BigDecimal	r44_othercurrencies_m;
	private BigDecimal	r44_othercurrencies_n;
	private BigDecimal	r44_othercurrencies_0;
	private BigDecimal	r44_othercurrencies_p;
	private BigDecimal	r44_othercurrencies_q;
	private BigDecimal	r44_othercurrencies_r;
	private BigDecimal	r44_othercurrencies_s;
	private BigDecimal	r44_othercurrencies_t;
	private BigDecimal	r44_othercurrencies_u;
	private BigDecimal	r44_othercurrencies_v;
	private BigDecimal	r44_othercurrencies_w;
	private String	r45_product;
	private BigDecimal	r45_pula;
	private BigDecimal	r45_usd;
	private BigDecimal	r45_zar;
	private BigDecimal	r45_gbp;
	private BigDecimal	r45_euro;
	private BigDecimal	r45_othercurrencies_inr;
	private BigDecimal	r45_othercurrencies_aud;
	private BigDecimal	r45_othercurrencies_i;
	private BigDecimal	r45_othercurrencies_j;
	private BigDecimal	r45_othercurrencies_k;
	private BigDecimal	r45_othercurrencies_l;
	private BigDecimal	r45_othercurrencies_m;
	private BigDecimal	r45_othercurrencies_n;
	private BigDecimal	r45_othercurrencies_0;
	private BigDecimal	r45_othercurrencies_p;
	private BigDecimal	r45_othercurrencies_q;
	private BigDecimal	r45_othercurrencies_r;
	private BigDecimal	r45_othercurrencies_s;
	private BigDecimal	r45_othercurrencies_t;
	private BigDecimal	r45_othercurrencies_u;
	private BigDecimal	r45_othercurrencies_v;
	private BigDecimal	r45_othercurrencies_w;
	private String	r46_product;
	private BigDecimal	r46_pula;
	private BigDecimal	r46_usd;
	private BigDecimal	r46_zar;
	private BigDecimal	r46_gbp;
	private BigDecimal	r46_euro;
	private BigDecimal	r46_othercurrencies_inr;
	private BigDecimal	r46_othercurrencies_aud;
	private BigDecimal	r46_othercurrencies_i;
	private BigDecimal	r46_othercurrencies_j;
	private BigDecimal	r46_othercurrencies_k;
	private BigDecimal	r46_othercurrencies_l;
	private BigDecimal	r46_othercurrencies_m;
	private BigDecimal	r46_othercurrencies_n;
	private BigDecimal	r46_othercurrencies_0;
	private BigDecimal	r46_othercurrencies_p;
	private BigDecimal	r46_othercurrencies_q;
	private BigDecimal	r46_othercurrencies_r;
	private BigDecimal	r46_othercurrencies_s;
	private BigDecimal	r46_othercurrencies_t;
	private BigDecimal	r46_othercurrencies_u;
	private BigDecimal	r46_othercurrencies_v;
	private BigDecimal	r46_othercurrencies_w;
	private String	r47_product;
	private BigDecimal	r47_pula;
	private BigDecimal	r47_usd;
	private BigDecimal	r47_zar;
	private BigDecimal	r47_gbp;
	private BigDecimal	r47_euro;
	private BigDecimal	r47_othercurrencies_inr;
	private BigDecimal	r47_othercurrencies_aud;
	private BigDecimal	r47_othercurrencies_i;
	private BigDecimal	r47_othercurrencies_j;
	private BigDecimal	r47_othercurrencies_k;
	private BigDecimal	r47_othercurrencies_l;
	private BigDecimal	r47_othercurrencies_m;
	private BigDecimal	r47_othercurrencies_n;
	private BigDecimal	r47_othercurrencies_0;
	private BigDecimal	r47_othercurrencies_p;
	private BigDecimal	r47_othercurrencies_q;
	private BigDecimal	r47_othercurrencies_r;
	private BigDecimal	r47_othercurrencies_s;
	private BigDecimal	r47_othercurrencies_t;
	private BigDecimal	r47_othercurrencies_u;
	private BigDecimal	r47_othercurrencies_v;
	private BigDecimal	r47_othercurrencies_w;
	private String	r48_product;
	private BigDecimal	r48_pula;
	private BigDecimal	r48_usd;
	private BigDecimal	r48_zar;
	private BigDecimal	r48_gbp;
	private BigDecimal	r48_euro;
	private BigDecimal	r48_othercurrencies_inr;
	private BigDecimal	r48_othercurrencies_aud;
	private BigDecimal	r48_othercurrencies_i;
	private BigDecimal	r48_othercurrencies_j;
	private BigDecimal	r48_othercurrencies_k;
	private BigDecimal	r48_othercurrencies_l;
	private BigDecimal	r48_othercurrencies_m;
	private BigDecimal	r48_othercurrencies_n;
	private BigDecimal	r48_othercurrencies_0;
	private BigDecimal	r48_othercurrencies_p;
	private BigDecimal	r48_othercurrencies_q;
	private BigDecimal	r48_othercurrencies_r;
	private BigDecimal	r48_othercurrencies_s;
	private BigDecimal	r48_othercurrencies_t;
	private BigDecimal	r48_othercurrencies_u;
	private BigDecimal	r48_othercurrencies_v;
	private BigDecimal	r48_othercurrencies_w;
	private String	r49_product;
	private BigDecimal	r49_pula;
	private BigDecimal	r49_usd;
	private BigDecimal	r49_zar;
	private BigDecimal	r49_gbp;
	private BigDecimal	r49_euro;
	private BigDecimal	r49_othercurrencies_inr;
	private BigDecimal	r49_othercurrencies_aud;
	private BigDecimal	r49_othercurrencies_i;
	private BigDecimal	r49_othercurrencies_j;
	private BigDecimal	r49_othercurrencies_k;
	private BigDecimal	r49_othercurrencies_l;
	private BigDecimal	r49_othercurrencies_m;
	private BigDecimal	r49_othercurrencies_n;
	private BigDecimal	r49_othercurrencies_0;
	private BigDecimal	r49_othercurrencies_p;
	private BigDecimal	r49_othercurrencies_q;
	private BigDecimal	r49_othercurrencies_r;
	private BigDecimal	r49_othercurrencies_s;
	private BigDecimal	r49_othercurrencies_t;
	private BigDecimal	r49_othercurrencies_u;
	private BigDecimal	r49_othercurrencies_v;
	private BigDecimal	r49_othercurrencies_w;
	private String	r50_product;
	private BigDecimal	r50_pula;
	private BigDecimal	r50_usd;
	private BigDecimal	r50_zar;
	private BigDecimal	r50_gbp;
	private BigDecimal	r50_euro;
	private BigDecimal	r50_othercurrencies_inr;
	private BigDecimal	r50_othercurrencies_aud;
	private BigDecimal	r50_othercurrencies_i;
	private BigDecimal	r50_othercurrencies_j;
	private BigDecimal	r50_othercurrencies_k;
	private BigDecimal	r50_othercurrencies_l;
	private BigDecimal	r50_othercurrencies_m;
	private BigDecimal	r50_othercurrencies_n;
	private BigDecimal	r50_othercurrencies_0;
	private BigDecimal	r50_othercurrencies_p;
	private BigDecimal	r50_othercurrencies_q;
	private BigDecimal	r50_othercurrencies_r;
	private BigDecimal	r50_othercurrencies_s;
	private BigDecimal	r50_othercurrencies_t;
	private BigDecimal	r50_othercurrencies_u;
	private BigDecimal	r50_othercurrencies_v;
	private BigDecimal	r50_othercurrencies_w;
	private String	r51_product;
	private BigDecimal	r51_pula;
	private BigDecimal	r51_usd;
	private BigDecimal	r51_zar;
	private BigDecimal	r51_gbp;
	private BigDecimal	r51_euro;
	private BigDecimal	r51_othercurrencies_inr;
	private BigDecimal	r51_othercurrencies_aud;
	private BigDecimal	r51_othercurrencies_i;
	private BigDecimal	r51_othercurrencies_j;
	private BigDecimal	r51_othercurrencies_k;
	private BigDecimal	r51_othercurrencies_l;
	private BigDecimal	r51_othercurrencies_m;
	private BigDecimal	r51_othercurrencies_n;
	private BigDecimal	r51_othercurrencies_0;
	private BigDecimal	r51_othercurrencies_p;
	private BigDecimal	r51_othercurrencies_q;
	private BigDecimal	r51_othercurrencies_r;
	private BigDecimal	r51_othercurrencies_s;
	private BigDecimal	r51_othercurrencies_t;
	private BigDecimal	r51_othercurrencies_u;
	private BigDecimal	r51_othercurrencies_v;
	private BigDecimal	r51_othercurrencies_w;
	
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
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_pula() {
		return r11_pula;
	}
	public void setR11_pula(BigDecimal r11_pula) {
		this.r11_pula = r11_pula;
	}
	public BigDecimal getR11_usd() {
		return r11_usd;
	}
	public void setR11_usd(BigDecimal r11_usd) {
		this.r11_usd = r11_usd;
	}
	public BigDecimal getR11_zar() {
		return r11_zar;
	}
	public void setR11_zar(BigDecimal r11_zar) {
		this.r11_zar = r11_zar;
	}
	public BigDecimal getR11_gbp() {
		return r11_gbp;
	}
	public void setR11_gbp(BigDecimal r11_gbp) {
		this.r11_gbp = r11_gbp;
	}
	public BigDecimal getR11_euro() {
		return r11_euro;
	}
	public void setR11_euro(BigDecimal r11_euro) {
		this.r11_euro = r11_euro;
	}
	public BigDecimal getR11_othercurrencies_inr() {
		return r11_othercurrencies_inr;
	}
	public void setR11_othercurrencies_inr(BigDecimal r11_othercurrencies_inr) {
		this.r11_othercurrencies_inr = r11_othercurrencies_inr;
	}
	public BigDecimal getR11_othercurrencies_aud() {
		return r11_othercurrencies_aud;
	}
	public void setR11_othercurrencies_aud(BigDecimal r11_othercurrencies_aud) {
		this.r11_othercurrencies_aud = r11_othercurrencies_aud;
	}
	public BigDecimal getR11_othercurrencies_i() {
		return r11_othercurrencies_i;
	}
	public void setR11_othercurrencies_i(BigDecimal r11_othercurrencies_i) {
		this.r11_othercurrencies_i = r11_othercurrencies_i;
	}
	public BigDecimal getR11_othercurrencies_j() {
		return r11_othercurrencies_j;
	}
	public void setR11_othercurrencies_j(BigDecimal r11_othercurrencies_j) {
		this.r11_othercurrencies_j = r11_othercurrencies_j;
	}
	public BigDecimal getR11_othercurrencies_k() {
		return r11_othercurrencies_k;
	}
	public void setR11_othercurrencies_k(BigDecimal r11_othercurrencies_k) {
		this.r11_othercurrencies_k = r11_othercurrencies_k;
	}
	public BigDecimal getR11_othercurrencies_l() {
		return r11_othercurrencies_l;
	}
	public void setR11_othercurrencies_l(BigDecimal r11_othercurrencies_l) {
		this.r11_othercurrencies_l = r11_othercurrencies_l;
	}
	public BigDecimal getR11_othercurrencies_m() {
		return r11_othercurrencies_m;
	}
	public void setR11_othercurrencies_m(BigDecimal r11_othercurrencies_m) {
		this.r11_othercurrencies_m = r11_othercurrencies_m;
	}
	public BigDecimal getR11_othercurrencies_n() {
		return r11_othercurrencies_n;
	}
	public void setR11_othercurrencies_n(BigDecimal r11_othercurrencies_n) {
		this.r11_othercurrencies_n = r11_othercurrencies_n;
	}
	public BigDecimal getR11_othercurrencies_0() {
		return r11_othercurrencies_0;
	}
	public void setR11_othercurrencies_0(BigDecimal r11_othercurrencies_0) {
		this.r11_othercurrencies_0 = r11_othercurrencies_0;
	}
	public BigDecimal getR11_othercurrencies_p() {
		return r11_othercurrencies_p;
	}
	public void setR11_othercurrencies_p(BigDecimal r11_othercurrencies_p) {
		this.r11_othercurrencies_p = r11_othercurrencies_p;
	}
	public BigDecimal getR11_othercurrencies_q() {
		return r11_othercurrencies_q;
	}
	public void setR11_othercurrencies_q(BigDecimal r11_othercurrencies_q) {
		this.r11_othercurrencies_q = r11_othercurrencies_q;
	}
	public BigDecimal getR11_othercurrencies_r() {
		return r11_othercurrencies_r;
	}
	public void setR11_othercurrencies_r(BigDecimal r11_othercurrencies_r) {
		this.r11_othercurrencies_r = r11_othercurrencies_r;
	}
	public BigDecimal getR11_othercurrencies_s() {
		return r11_othercurrencies_s;
	}
	public void setR11_othercurrencies_s(BigDecimal r11_othercurrencies_s) {
		this.r11_othercurrencies_s = r11_othercurrencies_s;
	}
	public BigDecimal getR11_othercurrencies_t() {
		return r11_othercurrencies_t;
	}
	public void setR11_othercurrencies_t(BigDecimal r11_othercurrencies_t) {
		this.r11_othercurrencies_t = r11_othercurrencies_t;
	}
	public BigDecimal getR11_othercurrencies_u() {
		return r11_othercurrencies_u;
	}
	public void setR11_othercurrencies_u(BigDecimal r11_othercurrencies_u) {
		this.r11_othercurrencies_u = r11_othercurrencies_u;
	}
	public BigDecimal getR11_othercurrencies_v() {
		return r11_othercurrencies_v;
	}
	public void setR11_othercurrencies_v(BigDecimal r11_othercurrencies_v) {
		this.r11_othercurrencies_v = r11_othercurrencies_v;
	}
	public BigDecimal getR11_othercurrencies_w() {
		return r11_othercurrencies_w;
	}
	public void setR11_othercurrencies_w(BigDecimal r11_othercurrencies_w) {
		this.r11_othercurrencies_w = r11_othercurrencies_w;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_pula() {
		return r12_pula;
	}
	public void setR12_pula(BigDecimal r12_pula) {
		this.r12_pula = r12_pula;
	}
	public BigDecimal getR12_usd() {
		return r12_usd;
	}
	public void setR12_usd(BigDecimal r12_usd) {
		this.r12_usd = r12_usd;
	}
	public BigDecimal getR12_zar() {
		return r12_zar;
	}
	public void setR12_zar(BigDecimal r12_zar) {
		this.r12_zar = r12_zar;
	}
	public BigDecimal getR12_gbp() {
		return r12_gbp;
	}
	public void setR12_gbp(BigDecimal r12_gbp) {
		this.r12_gbp = r12_gbp;
	}
	public BigDecimal getR12_euro() {
		return r12_euro;
	}
	public void setR12_euro(BigDecimal r12_euro) {
		this.r12_euro = r12_euro;
	}
	public BigDecimal getR12_othercurrencies_inr() {
		return r12_othercurrencies_inr;
	}
	public void setR12_othercurrencies_inr(BigDecimal r12_othercurrencies_inr) {
		this.r12_othercurrencies_inr = r12_othercurrencies_inr;
	}
	public BigDecimal getR12_othercurrencies_aud() {
		return r12_othercurrencies_aud;
	}
	public void setR12_othercurrencies_aud(BigDecimal r12_othercurrencies_aud) {
		this.r12_othercurrencies_aud = r12_othercurrencies_aud;
	}
	public BigDecimal getR12_othercurrencies_i() {
		return r12_othercurrencies_i;
	}
	public void setR12_othercurrencies_i(BigDecimal r12_othercurrencies_i) {
		this.r12_othercurrencies_i = r12_othercurrencies_i;
	}
	public BigDecimal getR12_othercurrencies_j() {
		return r12_othercurrencies_j;
	}
	public void setR12_othercurrencies_j(BigDecimal r12_othercurrencies_j) {
		this.r12_othercurrencies_j = r12_othercurrencies_j;
	}
	public BigDecimal getR12_othercurrencies_k() {
		return r12_othercurrencies_k;
	}
	public void setR12_othercurrencies_k(BigDecimal r12_othercurrencies_k) {
		this.r12_othercurrencies_k = r12_othercurrencies_k;
	}
	public BigDecimal getR12_othercurrencies_l() {
		return r12_othercurrencies_l;
	}
	public void setR12_othercurrencies_l(BigDecimal r12_othercurrencies_l) {
		this.r12_othercurrencies_l = r12_othercurrencies_l;
	}
	public BigDecimal getR12_othercurrencies_m() {
		return r12_othercurrencies_m;
	}
	public void setR12_othercurrencies_m(BigDecimal r12_othercurrencies_m) {
		this.r12_othercurrencies_m = r12_othercurrencies_m;
	}
	public BigDecimal getR12_othercurrencies_n() {
		return r12_othercurrencies_n;
	}
	public void setR12_othercurrencies_n(BigDecimal r12_othercurrencies_n) {
		this.r12_othercurrencies_n = r12_othercurrencies_n;
	}
	public BigDecimal getR12_othercurrencies_0() {
		return r12_othercurrencies_0;
	}
	public void setR12_othercurrencies_0(BigDecimal r12_othercurrencies_0) {
		this.r12_othercurrencies_0 = r12_othercurrencies_0;
	}
	public BigDecimal getR12_othercurrencies_p() {
		return r12_othercurrencies_p;
	}
	public void setR12_othercurrencies_p(BigDecimal r12_othercurrencies_p) {
		this.r12_othercurrencies_p = r12_othercurrencies_p;
	}
	public BigDecimal getR12_othercurrencies_q() {
		return r12_othercurrencies_q;
	}
	public void setR12_othercurrencies_q(BigDecimal r12_othercurrencies_q) {
		this.r12_othercurrencies_q = r12_othercurrencies_q;
	}
	public BigDecimal getR12_othercurrencies_r() {
		return r12_othercurrencies_r;
	}
	public void setR12_othercurrencies_r(BigDecimal r12_othercurrencies_r) {
		this.r12_othercurrencies_r = r12_othercurrencies_r;
	}
	public BigDecimal getR12_othercurrencies_s() {
		return r12_othercurrencies_s;
	}
	public void setR12_othercurrencies_s(BigDecimal r12_othercurrencies_s) {
		this.r12_othercurrencies_s = r12_othercurrencies_s;
	}
	public BigDecimal getR12_othercurrencies_t() {
		return r12_othercurrencies_t;
	}
	public void setR12_othercurrencies_t(BigDecimal r12_othercurrencies_t) {
		this.r12_othercurrencies_t = r12_othercurrencies_t;
	}
	public BigDecimal getR12_othercurrencies_u() {
		return r12_othercurrencies_u;
	}
	public void setR12_othercurrencies_u(BigDecimal r12_othercurrencies_u) {
		this.r12_othercurrencies_u = r12_othercurrencies_u;
	}
	public BigDecimal getR12_othercurrencies_v() {
		return r12_othercurrencies_v;
	}
	public void setR12_othercurrencies_v(BigDecimal r12_othercurrencies_v) {
		this.r12_othercurrencies_v = r12_othercurrencies_v;
	}
	public BigDecimal getR12_othercurrencies_w() {
		return r12_othercurrencies_w;
	}
	public void setR12_othercurrencies_w(BigDecimal r12_othercurrencies_w) {
		this.r12_othercurrencies_w = r12_othercurrencies_w;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_pula() {
		return r13_pula;
	}
	public void setR13_pula(BigDecimal r13_pula) {
		this.r13_pula = r13_pula;
	}
	public BigDecimal getR13_usd() {
		return r13_usd;
	}
	public void setR13_usd(BigDecimal r13_usd) {
		this.r13_usd = r13_usd;
	}
	public BigDecimal getR13_zar() {
		return r13_zar;
	}
	public void setR13_zar(BigDecimal r13_zar) {
		this.r13_zar = r13_zar;
	}
	public BigDecimal getR13_gbp() {
		return r13_gbp;
	}
	public void setR13_gbp(BigDecimal r13_gbp) {
		this.r13_gbp = r13_gbp;
	}
	public BigDecimal getR13_euro() {
		return r13_euro;
	}
	public void setR13_euro(BigDecimal r13_euro) {
		this.r13_euro = r13_euro;
	}
	public BigDecimal getR13_othercurrencies_inr() {
		return r13_othercurrencies_inr;
	}
	public void setR13_othercurrencies_inr(BigDecimal r13_othercurrencies_inr) {
		this.r13_othercurrencies_inr = r13_othercurrencies_inr;
	}
	public BigDecimal getR13_othercurrencies_aud() {
		return r13_othercurrencies_aud;
	}
	public void setR13_othercurrencies_aud(BigDecimal r13_othercurrencies_aud) {
		this.r13_othercurrencies_aud = r13_othercurrencies_aud;
	}
	public BigDecimal getR13_othercurrencies_i() {
		return r13_othercurrencies_i;
	}
	public void setR13_othercurrencies_i(BigDecimal r13_othercurrencies_i) {
		this.r13_othercurrencies_i = r13_othercurrencies_i;
	}
	public BigDecimal getR13_othercurrencies_j() {
		return r13_othercurrencies_j;
	}
	public void setR13_othercurrencies_j(BigDecimal r13_othercurrencies_j) {
		this.r13_othercurrencies_j = r13_othercurrencies_j;
	}
	public BigDecimal getR13_othercurrencies_k() {
		return r13_othercurrencies_k;
	}
	public void setR13_othercurrencies_k(BigDecimal r13_othercurrencies_k) {
		this.r13_othercurrencies_k = r13_othercurrencies_k;
	}
	public BigDecimal getR13_othercurrencies_l() {
		return r13_othercurrencies_l;
	}
	public void setR13_othercurrencies_l(BigDecimal r13_othercurrencies_l) {
		this.r13_othercurrencies_l = r13_othercurrencies_l;
	}
	public BigDecimal getR13_othercurrencies_m() {
		return r13_othercurrencies_m;
	}
	public void setR13_othercurrencies_m(BigDecimal r13_othercurrencies_m) {
		this.r13_othercurrencies_m = r13_othercurrencies_m;
	}
	public BigDecimal getR13_othercurrencies_n() {
		return r13_othercurrencies_n;
	}
	public void setR13_othercurrencies_n(BigDecimal r13_othercurrencies_n) {
		this.r13_othercurrencies_n = r13_othercurrencies_n;
	}
	public BigDecimal getR13_othercurrencies_0() {
		return r13_othercurrencies_0;
	}
	public void setR13_othercurrencies_0(BigDecimal r13_othercurrencies_0) {
		this.r13_othercurrencies_0 = r13_othercurrencies_0;
	}
	public BigDecimal getR13_othercurrencies_p() {
		return r13_othercurrencies_p;
	}
	public void setR13_othercurrencies_p(BigDecimal r13_othercurrencies_p) {
		this.r13_othercurrencies_p = r13_othercurrencies_p;
	}
	public BigDecimal getR13_othercurrencies_q() {
		return r13_othercurrencies_q;
	}
	public void setR13_othercurrencies_q(BigDecimal r13_othercurrencies_q) {
		this.r13_othercurrencies_q = r13_othercurrencies_q;
	}
	public BigDecimal getR13_othercurrencies_r() {
		return r13_othercurrencies_r;
	}
	public void setR13_othercurrencies_r(BigDecimal r13_othercurrencies_r) {
		this.r13_othercurrencies_r = r13_othercurrencies_r;
	}
	public BigDecimal getR13_othercurrencies_s() {
		return r13_othercurrencies_s;
	}
	public void setR13_othercurrencies_s(BigDecimal r13_othercurrencies_s) {
		this.r13_othercurrencies_s = r13_othercurrencies_s;
	}
	public BigDecimal getR13_othercurrencies_t() {
		return r13_othercurrencies_t;
	}
	public void setR13_othercurrencies_t(BigDecimal r13_othercurrencies_t) {
		this.r13_othercurrencies_t = r13_othercurrencies_t;
	}
	public BigDecimal getR13_othercurrencies_u() {
		return r13_othercurrencies_u;
	}
	public void setR13_othercurrencies_u(BigDecimal r13_othercurrencies_u) {
		this.r13_othercurrencies_u = r13_othercurrencies_u;
	}
	public BigDecimal getR13_othercurrencies_v() {
		return r13_othercurrencies_v;
	}
	public void setR13_othercurrencies_v(BigDecimal r13_othercurrencies_v) {
		this.r13_othercurrencies_v = r13_othercurrencies_v;
	}
	public BigDecimal getR13_othercurrencies_w() {
		return r13_othercurrencies_w;
	}
	public void setR13_othercurrencies_w(BigDecimal r13_othercurrencies_w) {
		this.r13_othercurrencies_w = r13_othercurrencies_w;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_pula() {
		return r14_pula;
	}
	public void setR14_pula(BigDecimal r14_pula) {
		this.r14_pula = r14_pula;
	}
	public BigDecimal getR14_usd() {
		return r14_usd;
	}
	public void setR14_usd(BigDecimal r14_usd) {
		this.r14_usd = r14_usd;
	}
	public BigDecimal getR14_zar() {
		return r14_zar;
	}
	public void setR14_zar(BigDecimal r14_zar) {
		this.r14_zar = r14_zar;
	}
	public BigDecimal getR14_gbp() {
		return r14_gbp;
	}
	public void setR14_gbp(BigDecimal r14_gbp) {
		this.r14_gbp = r14_gbp;
	}
	public BigDecimal getR14_euro() {
		return r14_euro;
	}
	public void setR14_euro(BigDecimal r14_euro) {
		this.r14_euro = r14_euro;
	}
	public BigDecimal getR14_othercurrencies_inr() {
		return r14_othercurrencies_inr;
	}
	public void setR14_othercurrencies_inr(BigDecimal r14_othercurrencies_inr) {
		this.r14_othercurrencies_inr = r14_othercurrencies_inr;
	}
	public BigDecimal getR14_othercurrencies_aud() {
		return r14_othercurrencies_aud;
	}
	public void setR14_othercurrencies_aud(BigDecimal r14_othercurrencies_aud) {
		this.r14_othercurrencies_aud = r14_othercurrencies_aud;
	}
	public BigDecimal getR14_othercurrencies_i() {
		return r14_othercurrencies_i;
	}
	public void setR14_othercurrencies_i(BigDecimal r14_othercurrencies_i) {
		this.r14_othercurrencies_i = r14_othercurrencies_i;
	}
	public BigDecimal getR14_othercurrencies_j() {
		return r14_othercurrencies_j;
	}
	public void setR14_othercurrencies_j(BigDecimal r14_othercurrencies_j) {
		this.r14_othercurrencies_j = r14_othercurrencies_j;
	}
	public BigDecimal getR14_othercurrencies_k() {
		return r14_othercurrencies_k;
	}
	public void setR14_othercurrencies_k(BigDecimal r14_othercurrencies_k) {
		this.r14_othercurrencies_k = r14_othercurrencies_k;
	}
	public BigDecimal getR14_othercurrencies_l() {
		return r14_othercurrencies_l;
	}
	public void setR14_othercurrencies_l(BigDecimal r14_othercurrencies_l) {
		this.r14_othercurrencies_l = r14_othercurrencies_l;
	}
	public BigDecimal getR14_othercurrencies_m() {
		return r14_othercurrencies_m;
	}
	public void setR14_othercurrencies_m(BigDecimal r14_othercurrencies_m) {
		this.r14_othercurrencies_m = r14_othercurrencies_m;
	}
	public BigDecimal getR14_othercurrencies_n() {
		return r14_othercurrencies_n;
	}
	public void setR14_othercurrencies_n(BigDecimal r14_othercurrencies_n) {
		this.r14_othercurrencies_n = r14_othercurrencies_n;
	}
	public BigDecimal getR14_othercurrencies_0() {
		return r14_othercurrencies_0;
	}
	public void setR14_othercurrencies_0(BigDecimal r14_othercurrencies_0) {
		this.r14_othercurrencies_0 = r14_othercurrencies_0;
	}
	public BigDecimal getR14_othercurrencies_p() {
		return r14_othercurrencies_p;
	}
	public void setR14_othercurrencies_p(BigDecimal r14_othercurrencies_p) {
		this.r14_othercurrencies_p = r14_othercurrencies_p;
	}
	public BigDecimal getR14_othercurrencies_q() {
		return r14_othercurrencies_q;
	}
	public void setR14_othercurrencies_q(BigDecimal r14_othercurrencies_q) {
		this.r14_othercurrencies_q = r14_othercurrencies_q;
	}
	public BigDecimal getR14_othercurrencies_r() {
		return r14_othercurrencies_r;
	}
	public void setR14_othercurrencies_r(BigDecimal r14_othercurrencies_r) {
		this.r14_othercurrencies_r = r14_othercurrencies_r;
	}
	public BigDecimal getR14_othercurrencies_s() {
		return r14_othercurrencies_s;
	}
	public void setR14_othercurrencies_s(BigDecimal r14_othercurrencies_s) {
		this.r14_othercurrencies_s = r14_othercurrencies_s;
	}
	public BigDecimal getR14_othercurrencies_t() {
		return r14_othercurrencies_t;
	}
	public void setR14_othercurrencies_t(BigDecimal r14_othercurrencies_t) {
		this.r14_othercurrencies_t = r14_othercurrencies_t;
	}
	public BigDecimal getR14_othercurrencies_u() {
		return r14_othercurrencies_u;
	}
	public void setR14_othercurrencies_u(BigDecimal r14_othercurrencies_u) {
		this.r14_othercurrencies_u = r14_othercurrencies_u;
	}
	public BigDecimal getR14_othercurrencies_v() {
		return r14_othercurrencies_v;
	}
	public void setR14_othercurrencies_v(BigDecimal r14_othercurrencies_v) {
		this.r14_othercurrencies_v = r14_othercurrencies_v;
	}
	public BigDecimal getR14_othercurrencies_w() {
		return r14_othercurrencies_w;
	}
	public void setR14_othercurrencies_w(BigDecimal r14_othercurrencies_w) {
		this.r14_othercurrencies_w = r14_othercurrencies_w;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_pula() {
		return r15_pula;
	}
	public void setR15_pula(BigDecimal r15_pula) {
		this.r15_pula = r15_pula;
	}
	public BigDecimal getR15_usd() {
		return r15_usd;
	}
	public void setR15_usd(BigDecimal r15_usd) {
		this.r15_usd = r15_usd;
	}
	public BigDecimal getR15_zar() {
		return r15_zar;
	}
	public void setR15_zar(BigDecimal r15_zar) {
		this.r15_zar = r15_zar;
	}
	public BigDecimal getR15_gbp() {
		return r15_gbp;
	}
	public void setR15_gbp(BigDecimal r15_gbp) {
		this.r15_gbp = r15_gbp;
	}
	public BigDecimal getR15_euro() {
		return r15_euro;
	}
	public void setR15_euro(BigDecimal r15_euro) {
		this.r15_euro = r15_euro;
	}
	public BigDecimal getR15_othercurrencies_inr() {
		return r15_othercurrencies_inr;
	}
	public void setR15_othercurrencies_inr(BigDecimal r15_othercurrencies_inr) {
		this.r15_othercurrencies_inr = r15_othercurrencies_inr;
	}
	public BigDecimal getR15_othercurrencies_aud() {
		return r15_othercurrencies_aud;
	}
	public void setR15_othercurrencies_aud(BigDecimal r15_othercurrencies_aud) {
		this.r15_othercurrencies_aud = r15_othercurrencies_aud;
	}
	public BigDecimal getR15_othercurrencies_i() {
		return r15_othercurrencies_i;
	}
	public void setR15_othercurrencies_i(BigDecimal r15_othercurrencies_i) {
		this.r15_othercurrencies_i = r15_othercurrencies_i;
	}
	public BigDecimal getR15_othercurrencies_j() {
		return r15_othercurrencies_j;
	}
	public void setR15_othercurrencies_j(BigDecimal r15_othercurrencies_j) {
		this.r15_othercurrencies_j = r15_othercurrencies_j;
	}
	public BigDecimal getR15_othercurrencies_k() {
		return r15_othercurrencies_k;
	}
	public void setR15_othercurrencies_k(BigDecimal r15_othercurrencies_k) {
		this.r15_othercurrencies_k = r15_othercurrencies_k;
	}
	public BigDecimal getR15_othercurrencies_l() {
		return r15_othercurrencies_l;
	}
	public void setR15_othercurrencies_l(BigDecimal r15_othercurrencies_l) {
		this.r15_othercurrencies_l = r15_othercurrencies_l;
	}
	public BigDecimal getR15_othercurrencies_m() {
		return r15_othercurrencies_m;
	}
	public void setR15_othercurrencies_m(BigDecimal r15_othercurrencies_m) {
		this.r15_othercurrencies_m = r15_othercurrencies_m;
	}
	public BigDecimal getR15_othercurrencies_n() {
		return r15_othercurrencies_n;
	}
	public void setR15_othercurrencies_n(BigDecimal r15_othercurrencies_n) {
		this.r15_othercurrencies_n = r15_othercurrencies_n;
	}
	public BigDecimal getR15_othercurrencies_0() {
		return r15_othercurrencies_0;
	}
	public void setR15_othercurrencies_0(BigDecimal r15_othercurrencies_0) {
		this.r15_othercurrencies_0 = r15_othercurrencies_0;
	}
	public BigDecimal getR15_othercurrencies_p() {
		return r15_othercurrencies_p;
	}
	public void setR15_othercurrencies_p(BigDecimal r15_othercurrencies_p) {
		this.r15_othercurrencies_p = r15_othercurrencies_p;
	}
	public BigDecimal getR15_othercurrencies_q() {
		return r15_othercurrencies_q;
	}
	public void setR15_othercurrencies_q(BigDecimal r15_othercurrencies_q) {
		this.r15_othercurrencies_q = r15_othercurrencies_q;
	}
	public BigDecimal getR15_othercurrencies_r() {
		return r15_othercurrencies_r;
	}
	public void setR15_othercurrencies_r(BigDecimal r15_othercurrencies_r) {
		this.r15_othercurrencies_r = r15_othercurrencies_r;
	}
	public BigDecimal getR15_othercurrencies_s() {
		return r15_othercurrencies_s;
	}
	public void setR15_othercurrencies_s(BigDecimal r15_othercurrencies_s) {
		this.r15_othercurrencies_s = r15_othercurrencies_s;
	}
	public BigDecimal getR15_othercurrencies_t() {
		return r15_othercurrencies_t;
	}
	public void setR15_othercurrencies_t(BigDecimal r15_othercurrencies_t) {
		this.r15_othercurrencies_t = r15_othercurrencies_t;
	}
	public BigDecimal getR15_othercurrencies_u() {
		return r15_othercurrencies_u;
	}
	public void setR15_othercurrencies_u(BigDecimal r15_othercurrencies_u) {
		this.r15_othercurrencies_u = r15_othercurrencies_u;
	}
	public BigDecimal getR15_othercurrencies_v() {
		return r15_othercurrencies_v;
	}
	public void setR15_othercurrencies_v(BigDecimal r15_othercurrencies_v) {
		this.r15_othercurrencies_v = r15_othercurrencies_v;
	}
	public BigDecimal getR15_othercurrencies_w() {
		return r15_othercurrencies_w;
	}
	public void setR15_othercurrencies_w(BigDecimal r15_othercurrencies_w) {
		this.r15_othercurrencies_w = r15_othercurrencies_w;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_pula() {
		return r16_pula;
	}
	public void setR16_pula(BigDecimal r16_pula) {
		this.r16_pula = r16_pula;
	}
	public BigDecimal getR16_usd() {
		return r16_usd;
	}
	public void setR16_usd(BigDecimal r16_usd) {
		this.r16_usd = r16_usd;
	}
	public BigDecimal getR16_zar() {
		return r16_zar;
	}
	public void setR16_zar(BigDecimal r16_zar) {
		this.r16_zar = r16_zar;
	}
	public BigDecimal getR16_gbp() {
		return r16_gbp;
	}
	public void setR16_gbp(BigDecimal r16_gbp) {
		this.r16_gbp = r16_gbp;
	}
	public BigDecimal getR16_euro() {
		return r16_euro;
	}
	public void setR16_euro(BigDecimal r16_euro) {
		this.r16_euro = r16_euro;
	}
	public BigDecimal getR16_othercurrencies_inr() {
		return r16_othercurrencies_inr;
	}
	public void setR16_othercurrencies_inr(BigDecimal r16_othercurrencies_inr) {
		this.r16_othercurrencies_inr = r16_othercurrencies_inr;
	}
	public BigDecimal getR16_othercurrencies_aud() {
		return r16_othercurrencies_aud;
	}
	public void setR16_othercurrencies_aud(BigDecimal r16_othercurrencies_aud) {
		this.r16_othercurrencies_aud = r16_othercurrencies_aud;
	}
	public BigDecimal getR16_othercurrencies_i() {
		return r16_othercurrencies_i;
	}
	public void setR16_othercurrencies_i(BigDecimal r16_othercurrencies_i) {
		this.r16_othercurrencies_i = r16_othercurrencies_i;
	}
	public BigDecimal getR16_othercurrencies_j() {
		return r16_othercurrencies_j;
	}
	public void setR16_othercurrencies_j(BigDecimal r16_othercurrencies_j) {
		this.r16_othercurrencies_j = r16_othercurrencies_j;
	}
	public BigDecimal getR16_othercurrencies_k() {
		return r16_othercurrencies_k;
	}
	public void setR16_othercurrencies_k(BigDecimal r16_othercurrencies_k) {
		this.r16_othercurrencies_k = r16_othercurrencies_k;
	}
	public BigDecimal getR16_othercurrencies_l() {
		return r16_othercurrencies_l;
	}
	public void setR16_othercurrencies_l(BigDecimal r16_othercurrencies_l) {
		this.r16_othercurrencies_l = r16_othercurrencies_l;
	}
	public BigDecimal getR16_othercurrencies_m() {
		return r16_othercurrencies_m;
	}
	public void setR16_othercurrencies_m(BigDecimal r16_othercurrencies_m) {
		this.r16_othercurrencies_m = r16_othercurrencies_m;
	}
	public BigDecimal getR16_othercurrencies_n() {
		return r16_othercurrencies_n;
	}
	public void setR16_othercurrencies_n(BigDecimal r16_othercurrencies_n) {
		this.r16_othercurrencies_n = r16_othercurrencies_n;
	}
	public BigDecimal getR16_othercurrencies_0() {
		return r16_othercurrencies_0;
	}
	public void setR16_othercurrencies_0(BigDecimal r16_othercurrencies_0) {
		this.r16_othercurrencies_0 = r16_othercurrencies_0;
	}
	public BigDecimal getR16_othercurrencies_p() {
		return r16_othercurrencies_p;
	}
	public void setR16_othercurrencies_p(BigDecimal r16_othercurrencies_p) {
		this.r16_othercurrencies_p = r16_othercurrencies_p;
	}
	public BigDecimal getR16_othercurrencies_q() {
		return r16_othercurrencies_q;
	}
	public void setR16_othercurrencies_q(BigDecimal r16_othercurrencies_q) {
		this.r16_othercurrencies_q = r16_othercurrencies_q;
	}
	public BigDecimal getR16_othercurrencies_r() {
		return r16_othercurrencies_r;
	}
	public void setR16_othercurrencies_r(BigDecimal r16_othercurrencies_r) {
		this.r16_othercurrencies_r = r16_othercurrencies_r;
	}
	public BigDecimal getR16_othercurrencies_s() {
		return r16_othercurrencies_s;
	}
	public void setR16_othercurrencies_s(BigDecimal r16_othercurrencies_s) {
		this.r16_othercurrencies_s = r16_othercurrencies_s;
	}
	public BigDecimal getR16_othercurrencies_t() {
		return r16_othercurrencies_t;
	}
	public void setR16_othercurrencies_t(BigDecimal r16_othercurrencies_t) {
		this.r16_othercurrencies_t = r16_othercurrencies_t;
	}
	public BigDecimal getR16_othercurrencies_u() {
		return r16_othercurrencies_u;
	}
	public void setR16_othercurrencies_u(BigDecimal r16_othercurrencies_u) {
		this.r16_othercurrencies_u = r16_othercurrencies_u;
	}
	public BigDecimal getR16_othercurrencies_v() {
		return r16_othercurrencies_v;
	}
	public void setR16_othercurrencies_v(BigDecimal r16_othercurrencies_v) {
		this.r16_othercurrencies_v = r16_othercurrencies_v;
	}
	public BigDecimal getR16_othercurrencies_w() {
		return r16_othercurrencies_w;
	}
	public void setR16_othercurrencies_w(BigDecimal r16_othercurrencies_w) {
		this.r16_othercurrencies_w = r16_othercurrencies_w;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_pula() {
		return r17_pula;
	}
	public void setR17_pula(BigDecimal r17_pula) {
		this.r17_pula = r17_pula;
	}
	public BigDecimal getR17_usd() {
		return r17_usd;
	}
	public void setR17_usd(BigDecimal r17_usd) {
		this.r17_usd = r17_usd;
	}
	public BigDecimal getR17_zar() {
		return r17_zar;
	}
	public void setR17_zar(BigDecimal r17_zar) {
		this.r17_zar = r17_zar;
	}
	public BigDecimal getR17_gbp() {
		return r17_gbp;
	}
	public void setR17_gbp(BigDecimal r17_gbp) {
		this.r17_gbp = r17_gbp;
	}
	public BigDecimal getR17_euro() {
		return r17_euro;
	}
	public void setR17_euro(BigDecimal r17_euro) {
		this.r17_euro = r17_euro;
	}
	public BigDecimal getR17_othercurrencies_inr() {
		return r17_othercurrencies_inr;
	}
	public void setR17_othercurrencies_inr(BigDecimal r17_othercurrencies_inr) {
		this.r17_othercurrencies_inr = r17_othercurrencies_inr;
	}
	public BigDecimal getR17_othercurrencies_aud() {
		return r17_othercurrencies_aud;
	}
	public void setR17_othercurrencies_aud(BigDecimal r17_othercurrencies_aud) {
		this.r17_othercurrencies_aud = r17_othercurrencies_aud;
	}
	public BigDecimal getR17_othercurrencies_i() {
		return r17_othercurrencies_i;
	}
	public void setR17_othercurrencies_i(BigDecimal r17_othercurrencies_i) {
		this.r17_othercurrencies_i = r17_othercurrencies_i;
	}
	public BigDecimal getR17_othercurrencies_j() {
		return r17_othercurrencies_j;
	}
	public void setR17_othercurrencies_j(BigDecimal r17_othercurrencies_j) {
		this.r17_othercurrencies_j = r17_othercurrencies_j;
	}
	public BigDecimal getR17_othercurrencies_k() {
		return r17_othercurrencies_k;
	}
	public void setR17_othercurrencies_k(BigDecimal r17_othercurrencies_k) {
		this.r17_othercurrencies_k = r17_othercurrencies_k;
	}
	public BigDecimal getR17_othercurrencies_l() {
		return r17_othercurrencies_l;
	}
	public void setR17_othercurrencies_l(BigDecimal r17_othercurrencies_l) {
		this.r17_othercurrencies_l = r17_othercurrencies_l;
	}
	public BigDecimal getR17_othercurrencies_m() {
		return r17_othercurrencies_m;
	}
	public void setR17_othercurrencies_m(BigDecimal r17_othercurrencies_m) {
		this.r17_othercurrencies_m = r17_othercurrencies_m;
	}
	public BigDecimal getR17_othercurrencies_n() {
		return r17_othercurrencies_n;
	}
	public void setR17_othercurrencies_n(BigDecimal r17_othercurrencies_n) {
		this.r17_othercurrencies_n = r17_othercurrencies_n;
	}
	public BigDecimal getR17_othercurrencies_0() {
		return r17_othercurrencies_0;
	}
	public void setR17_othercurrencies_0(BigDecimal r17_othercurrencies_0) {
		this.r17_othercurrencies_0 = r17_othercurrencies_0;
	}
	public BigDecimal getR17_othercurrencies_p() {
		return r17_othercurrencies_p;
	}
	public void setR17_othercurrencies_p(BigDecimal r17_othercurrencies_p) {
		this.r17_othercurrencies_p = r17_othercurrencies_p;
	}
	public BigDecimal getR17_othercurrencies_q() {
		return r17_othercurrencies_q;
	}
	public void setR17_othercurrencies_q(BigDecimal r17_othercurrencies_q) {
		this.r17_othercurrencies_q = r17_othercurrencies_q;
	}
	public BigDecimal getR17_othercurrencies_r() {
		return r17_othercurrencies_r;
	}
	public void setR17_othercurrencies_r(BigDecimal r17_othercurrencies_r) {
		this.r17_othercurrencies_r = r17_othercurrencies_r;
	}
	public BigDecimal getR17_othercurrencies_s() {
		return r17_othercurrencies_s;
	}
	public void setR17_othercurrencies_s(BigDecimal r17_othercurrencies_s) {
		this.r17_othercurrencies_s = r17_othercurrencies_s;
	}
	public BigDecimal getR17_othercurrencies_t() {
		return r17_othercurrencies_t;
	}
	public void setR17_othercurrencies_t(BigDecimal r17_othercurrencies_t) {
		this.r17_othercurrencies_t = r17_othercurrencies_t;
	}
	public BigDecimal getR17_othercurrencies_u() {
		return r17_othercurrencies_u;
	}
	public void setR17_othercurrencies_u(BigDecimal r17_othercurrencies_u) {
		this.r17_othercurrencies_u = r17_othercurrencies_u;
	}
	public BigDecimal getR17_othercurrencies_v() {
		return r17_othercurrencies_v;
	}
	public void setR17_othercurrencies_v(BigDecimal r17_othercurrencies_v) {
		this.r17_othercurrencies_v = r17_othercurrencies_v;
	}
	public BigDecimal getR17_othercurrencies_w() {
		return r17_othercurrencies_w;
	}
	public void setR17_othercurrencies_w(BigDecimal r17_othercurrencies_w) {
		this.r17_othercurrencies_w = r17_othercurrencies_w;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_pula() {
		return r18_pula;
	}
	public void setR18_pula(BigDecimal r18_pula) {
		this.r18_pula = r18_pula;
	}
	public BigDecimal getR18_usd() {
		return r18_usd;
	}
	public void setR18_usd(BigDecimal r18_usd) {
		this.r18_usd = r18_usd;
	}
	public BigDecimal getR18_zar() {
		return r18_zar;
	}
	public void setR18_zar(BigDecimal r18_zar) {
		this.r18_zar = r18_zar;
	}
	public BigDecimal getR18_gbp() {
		return r18_gbp;
	}
	public void setR18_gbp(BigDecimal r18_gbp) {
		this.r18_gbp = r18_gbp;
	}
	public BigDecimal getR18_euro() {
		return r18_euro;
	}
	public void setR18_euro(BigDecimal r18_euro) {
		this.r18_euro = r18_euro;
	}
	public BigDecimal getR18_othercurrencies_inr() {
		return r18_othercurrencies_inr;
	}
	public void setR18_othercurrencies_inr(BigDecimal r18_othercurrencies_inr) {
		this.r18_othercurrencies_inr = r18_othercurrencies_inr;
	}
	public BigDecimal getR18_othercurrencies_aud() {
		return r18_othercurrencies_aud;
	}
	public void setR18_othercurrencies_aud(BigDecimal r18_othercurrencies_aud) {
		this.r18_othercurrencies_aud = r18_othercurrencies_aud;
	}
	public BigDecimal getR18_othercurrencies_i() {
		return r18_othercurrencies_i;
	}
	public void setR18_othercurrencies_i(BigDecimal r18_othercurrencies_i) {
		this.r18_othercurrencies_i = r18_othercurrencies_i;
	}
	public BigDecimal getR18_othercurrencies_j() {
		return r18_othercurrencies_j;
	}
	public void setR18_othercurrencies_j(BigDecimal r18_othercurrencies_j) {
		this.r18_othercurrencies_j = r18_othercurrencies_j;
	}
	public BigDecimal getR18_othercurrencies_k() {
		return r18_othercurrencies_k;
	}
	public void setR18_othercurrencies_k(BigDecimal r18_othercurrencies_k) {
		this.r18_othercurrencies_k = r18_othercurrencies_k;
	}
	public BigDecimal getR18_othercurrencies_l() {
		return r18_othercurrencies_l;
	}
	public void setR18_othercurrencies_l(BigDecimal r18_othercurrencies_l) {
		this.r18_othercurrencies_l = r18_othercurrencies_l;
	}
	public BigDecimal getR18_othercurrencies_m() {
		return r18_othercurrencies_m;
	}
	public void setR18_othercurrencies_m(BigDecimal r18_othercurrencies_m) {
		this.r18_othercurrencies_m = r18_othercurrencies_m;
	}
	public BigDecimal getR18_othercurrencies_n() {
		return r18_othercurrencies_n;
	}
	public void setR18_othercurrencies_n(BigDecimal r18_othercurrencies_n) {
		this.r18_othercurrencies_n = r18_othercurrencies_n;
	}
	public BigDecimal getR18_othercurrencies_0() {
		return r18_othercurrencies_0;
	}
	public void setR18_othercurrencies_0(BigDecimal r18_othercurrencies_0) {
		this.r18_othercurrencies_0 = r18_othercurrencies_0;
	}
	public BigDecimal getR18_othercurrencies_p() {
		return r18_othercurrencies_p;
	}
	public void setR18_othercurrencies_p(BigDecimal r18_othercurrencies_p) {
		this.r18_othercurrencies_p = r18_othercurrencies_p;
	}
	public BigDecimal getR18_othercurrencies_q() {
		return r18_othercurrencies_q;
	}
	public void setR18_othercurrencies_q(BigDecimal r18_othercurrencies_q) {
		this.r18_othercurrencies_q = r18_othercurrencies_q;
	}
	public BigDecimal getR18_othercurrencies_r() {
		return r18_othercurrencies_r;
	}
	public void setR18_othercurrencies_r(BigDecimal r18_othercurrencies_r) {
		this.r18_othercurrencies_r = r18_othercurrencies_r;
	}
	public BigDecimal getR18_othercurrencies_s() {
		return r18_othercurrencies_s;
	}
	public void setR18_othercurrencies_s(BigDecimal r18_othercurrencies_s) {
		this.r18_othercurrencies_s = r18_othercurrencies_s;
	}
	public BigDecimal getR18_othercurrencies_t() {
		return r18_othercurrencies_t;
	}
	public void setR18_othercurrencies_t(BigDecimal r18_othercurrencies_t) {
		this.r18_othercurrencies_t = r18_othercurrencies_t;
	}
	public BigDecimal getR18_othercurrencies_u() {
		return r18_othercurrencies_u;
	}
	public void setR18_othercurrencies_u(BigDecimal r18_othercurrencies_u) {
		this.r18_othercurrencies_u = r18_othercurrencies_u;
	}
	public BigDecimal getR18_othercurrencies_v() {
		return r18_othercurrencies_v;
	}
	public void setR18_othercurrencies_v(BigDecimal r18_othercurrencies_v) {
		this.r18_othercurrencies_v = r18_othercurrencies_v;
	}
	public BigDecimal getR18_othercurrencies_w() {
		return r18_othercurrencies_w;
	}
	public void setR18_othercurrencies_w(BigDecimal r18_othercurrencies_w) {
		this.r18_othercurrencies_w = r18_othercurrencies_w;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_pula() {
		return r19_pula;
	}
	public void setR19_pula(BigDecimal r19_pula) {
		this.r19_pula = r19_pula;
	}
	public BigDecimal getR19_usd() {
		return r19_usd;
	}
	public void setR19_usd(BigDecimal r19_usd) {
		this.r19_usd = r19_usd;
	}
	public BigDecimal getR19_zar() {
		return r19_zar;
	}
	public void setR19_zar(BigDecimal r19_zar) {
		this.r19_zar = r19_zar;
	}
	public BigDecimal getR19_gbp() {
		return r19_gbp;
	}
	public void setR19_gbp(BigDecimal r19_gbp) {
		this.r19_gbp = r19_gbp;
	}
	public BigDecimal getR19_euro() {
		return r19_euro;
	}
	public void setR19_euro(BigDecimal r19_euro) {
		this.r19_euro = r19_euro;
	}
	public BigDecimal getR19_othercurrencies_inr() {
		return r19_othercurrencies_inr;
	}
	public void setR19_othercurrencies_inr(BigDecimal r19_othercurrencies_inr) {
		this.r19_othercurrencies_inr = r19_othercurrencies_inr;
	}
	public BigDecimal getR19_othercurrencies_aud() {
		return r19_othercurrencies_aud;
	}
	public void setR19_othercurrencies_aud(BigDecimal r19_othercurrencies_aud) {
		this.r19_othercurrencies_aud = r19_othercurrencies_aud;
	}
	public BigDecimal getR19_othercurrencies_i() {
		return r19_othercurrencies_i;
	}
	public void setR19_othercurrencies_i(BigDecimal r19_othercurrencies_i) {
		this.r19_othercurrencies_i = r19_othercurrencies_i;
	}
	public BigDecimal getR19_othercurrencies_j() {
		return r19_othercurrencies_j;
	}
	public void setR19_othercurrencies_j(BigDecimal r19_othercurrencies_j) {
		this.r19_othercurrencies_j = r19_othercurrencies_j;
	}
	public BigDecimal getR19_othercurrencies_k() {
		return r19_othercurrencies_k;
	}
	public void setR19_othercurrencies_k(BigDecimal r19_othercurrencies_k) {
		this.r19_othercurrencies_k = r19_othercurrencies_k;
	}
	public BigDecimal getR19_othercurrencies_l() {
		return r19_othercurrencies_l;
	}
	public void setR19_othercurrencies_l(BigDecimal r19_othercurrencies_l) {
		this.r19_othercurrencies_l = r19_othercurrencies_l;
	}
	public BigDecimal getR19_othercurrencies_m() {
		return r19_othercurrencies_m;
	}
	public void setR19_othercurrencies_m(BigDecimal r19_othercurrencies_m) {
		this.r19_othercurrencies_m = r19_othercurrencies_m;
	}
	public BigDecimal getR19_othercurrencies_n() {
		return r19_othercurrencies_n;
	}
	public void setR19_othercurrencies_n(BigDecimal r19_othercurrencies_n) {
		this.r19_othercurrencies_n = r19_othercurrencies_n;
	}
	public BigDecimal getR19_othercurrencies_0() {
		return r19_othercurrencies_0;
	}
	public void setR19_othercurrencies_0(BigDecimal r19_othercurrencies_0) {
		this.r19_othercurrencies_0 = r19_othercurrencies_0;
	}
	public BigDecimal getR19_othercurrencies_p() {
		return r19_othercurrencies_p;
	}
	public void setR19_othercurrencies_p(BigDecimal r19_othercurrencies_p) {
		this.r19_othercurrencies_p = r19_othercurrencies_p;
	}
	public BigDecimal getR19_othercurrencies_q() {
		return r19_othercurrencies_q;
	}
	public void setR19_othercurrencies_q(BigDecimal r19_othercurrencies_q) {
		this.r19_othercurrencies_q = r19_othercurrencies_q;
	}
	public BigDecimal getR19_othercurrencies_r() {
		return r19_othercurrencies_r;
	}
	public void setR19_othercurrencies_r(BigDecimal r19_othercurrencies_r) {
		this.r19_othercurrencies_r = r19_othercurrencies_r;
	}
	public BigDecimal getR19_othercurrencies_s() {
		return r19_othercurrencies_s;
	}
	public void setR19_othercurrencies_s(BigDecimal r19_othercurrencies_s) {
		this.r19_othercurrencies_s = r19_othercurrencies_s;
	}
	public BigDecimal getR19_othercurrencies_t() {
		return r19_othercurrencies_t;
	}
	public void setR19_othercurrencies_t(BigDecimal r19_othercurrencies_t) {
		this.r19_othercurrencies_t = r19_othercurrencies_t;
	}
	public BigDecimal getR19_othercurrencies_u() {
		return r19_othercurrencies_u;
	}
	public void setR19_othercurrencies_u(BigDecimal r19_othercurrencies_u) {
		this.r19_othercurrencies_u = r19_othercurrencies_u;
	}
	public BigDecimal getR19_othercurrencies_v() {
		return r19_othercurrencies_v;
	}
	public void setR19_othercurrencies_v(BigDecimal r19_othercurrencies_v) {
		this.r19_othercurrencies_v = r19_othercurrencies_v;
	}
	public BigDecimal getR19_othercurrencies_w() {
		return r19_othercurrencies_w;
	}
	public void setR19_othercurrencies_w(BigDecimal r19_othercurrencies_w) {
		this.r19_othercurrencies_w = r19_othercurrencies_w;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_pula() {
		return r20_pula;
	}
	public void setR20_pula(BigDecimal r20_pula) {
		this.r20_pula = r20_pula;
	}
	public BigDecimal getR20_usd() {
		return r20_usd;
	}
	public void setR20_usd(BigDecimal r20_usd) {
		this.r20_usd = r20_usd;
	}
	public BigDecimal getR20_zar() {
		return r20_zar;
	}
	public void setR20_zar(BigDecimal r20_zar) {
		this.r20_zar = r20_zar;
	}
	public BigDecimal getR20_gbp() {
		return r20_gbp;
	}
	public void setR20_gbp(BigDecimal r20_gbp) {
		this.r20_gbp = r20_gbp;
	}
	public BigDecimal getR20_euro() {
		return r20_euro;
	}
	public void setR20_euro(BigDecimal r20_euro) {
		this.r20_euro = r20_euro;
	}
	public BigDecimal getR20_othercurrencies_inr() {
		return r20_othercurrencies_inr;
	}
	public void setR20_othercurrencies_inr(BigDecimal r20_othercurrencies_inr) {
		this.r20_othercurrencies_inr = r20_othercurrencies_inr;
	}
	public BigDecimal getR20_othercurrencies_aud() {
		return r20_othercurrencies_aud;
	}
	public void setR20_othercurrencies_aud(BigDecimal r20_othercurrencies_aud) {
		this.r20_othercurrencies_aud = r20_othercurrencies_aud;
	}
	public BigDecimal getR20_othercurrencies_i() {
		return r20_othercurrencies_i;
	}
	public void setR20_othercurrencies_i(BigDecimal r20_othercurrencies_i) {
		this.r20_othercurrencies_i = r20_othercurrencies_i;
	}
	public BigDecimal getR20_othercurrencies_j() {
		return r20_othercurrencies_j;
	}
	public void setR20_othercurrencies_j(BigDecimal r20_othercurrencies_j) {
		this.r20_othercurrencies_j = r20_othercurrencies_j;
	}
	public BigDecimal getR20_othercurrencies_k() {
		return r20_othercurrencies_k;
	}
	public void setR20_othercurrencies_k(BigDecimal r20_othercurrencies_k) {
		this.r20_othercurrencies_k = r20_othercurrencies_k;
	}
	public BigDecimal getR20_othercurrencies_l() {
		return r20_othercurrencies_l;
	}
	public void setR20_othercurrencies_l(BigDecimal r20_othercurrencies_l) {
		this.r20_othercurrencies_l = r20_othercurrencies_l;
	}
	public BigDecimal getR20_othercurrencies_m() {
		return r20_othercurrencies_m;
	}
	public void setR20_othercurrencies_m(BigDecimal r20_othercurrencies_m) {
		this.r20_othercurrencies_m = r20_othercurrencies_m;
	}
	public BigDecimal getR20_othercurrencies_n() {
		return r20_othercurrencies_n;
	}
	public void setR20_othercurrencies_n(BigDecimal r20_othercurrencies_n) {
		this.r20_othercurrencies_n = r20_othercurrencies_n;
	}
	public BigDecimal getR20_othercurrencies_0() {
		return r20_othercurrencies_0;
	}
	public void setR20_othercurrencies_0(BigDecimal r20_othercurrencies_0) {
		this.r20_othercurrencies_0 = r20_othercurrencies_0;
	}
	public BigDecimal getR20_othercurrencies_p() {
		return r20_othercurrencies_p;
	}
	public void setR20_othercurrencies_p(BigDecimal r20_othercurrencies_p) {
		this.r20_othercurrencies_p = r20_othercurrencies_p;
	}
	public BigDecimal getR20_othercurrencies_q() {
		return r20_othercurrencies_q;
	}
	public void setR20_othercurrencies_q(BigDecimal r20_othercurrencies_q) {
		this.r20_othercurrencies_q = r20_othercurrencies_q;
	}
	public BigDecimal getR20_othercurrencies_r() {
		return r20_othercurrencies_r;
	}
	public void setR20_othercurrencies_r(BigDecimal r20_othercurrencies_r) {
		this.r20_othercurrencies_r = r20_othercurrencies_r;
	}
	public BigDecimal getR20_othercurrencies_s() {
		return r20_othercurrencies_s;
	}
	public void setR20_othercurrencies_s(BigDecimal r20_othercurrencies_s) {
		this.r20_othercurrencies_s = r20_othercurrencies_s;
	}
	public BigDecimal getR20_othercurrencies_t() {
		return r20_othercurrencies_t;
	}
	public void setR20_othercurrencies_t(BigDecimal r20_othercurrencies_t) {
		this.r20_othercurrencies_t = r20_othercurrencies_t;
	}
	public BigDecimal getR20_othercurrencies_u() {
		return r20_othercurrencies_u;
	}
	public void setR20_othercurrencies_u(BigDecimal r20_othercurrencies_u) {
		this.r20_othercurrencies_u = r20_othercurrencies_u;
	}
	public BigDecimal getR20_othercurrencies_v() {
		return r20_othercurrencies_v;
	}
	public void setR20_othercurrencies_v(BigDecimal r20_othercurrencies_v) {
		this.r20_othercurrencies_v = r20_othercurrencies_v;
	}
	public BigDecimal getR20_othercurrencies_w() {
		return r20_othercurrencies_w;
	}
	public void setR20_othercurrencies_w(BigDecimal r20_othercurrencies_w) {
		this.r20_othercurrencies_w = r20_othercurrencies_w;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_pula() {
		return r21_pula;
	}
	public void setR21_pula(BigDecimal r21_pula) {
		this.r21_pula = r21_pula;
	}
	public BigDecimal getR21_usd() {
		return r21_usd;
	}
	public void setR21_usd(BigDecimal r21_usd) {
		this.r21_usd = r21_usd;
	}
	public BigDecimal getR21_zar() {
		return r21_zar;
	}
	public void setR21_zar(BigDecimal r21_zar) {
		this.r21_zar = r21_zar;
	}
	public BigDecimal getR21_gbp() {
		return r21_gbp;
	}
	public void setR21_gbp(BigDecimal r21_gbp) {
		this.r21_gbp = r21_gbp;
	}
	public BigDecimal getR21_euro() {
		return r21_euro;
	}
	public void setR21_euro(BigDecimal r21_euro) {
		this.r21_euro = r21_euro;
	}
	public BigDecimal getR21_othercurrencies_inr() {
		return r21_othercurrencies_inr;
	}
	public void setR21_othercurrencies_inr(BigDecimal r21_othercurrencies_inr) {
		this.r21_othercurrencies_inr = r21_othercurrencies_inr;
	}
	public BigDecimal getR21_othercurrencies_aud() {
		return r21_othercurrencies_aud;
	}
	public void setR21_othercurrencies_aud(BigDecimal r21_othercurrencies_aud) {
		this.r21_othercurrencies_aud = r21_othercurrencies_aud;
	}
	public BigDecimal getR21_othercurrencies_i() {
		return r21_othercurrencies_i;
	}
	public void setR21_othercurrencies_i(BigDecimal r21_othercurrencies_i) {
		this.r21_othercurrencies_i = r21_othercurrencies_i;
	}
	public BigDecimal getR21_othercurrencies_j() {
		return r21_othercurrencies_j;
	}
	public void setR21_othercurrencies_j(BigDecimal r21_othercurrencies_j) {
		this.r21_othercurrencies_j = r21_othercurrencies_j;
	}
	public BigDecimal getR21_othercurrencies_k() {
		return r21_othercurrencies_k;
	}
	public void setR21_othercurrencies_k(BigDecimal r21_othercurrencies_k) {
		this.r21_othercurrencies_k = r21_othercurrencies_k;
	}
	public BigDecimal getR21_othercurrencies_l() {
		return r21_othercurrencies_l;
	}
	public void setR21_othercurrencies_l(BigDecimal r21_othercurrencies_l) {
		this.r21_othercurrencies_l = r21_othercurrencies_l;
	}
	public BigDecimal getR21_othercurrencies_m() {
		return r21_othercurrencies_m;
	}
	public void setR21_othercurrencies_m(BigDecimal r21_othercurrencies_m) {
		this.r21_othercurrencies_m = r21_othercurrencies_m;
	}
	public BigDecimal getR21_othercurrencies_n() {
		return r21_othercurrencies_n;
	}
	public void setR21_othercurrencies_n(BigDecimal r21_othercurrencies_n) {
		this.r21_othercurrencies_n = r21_othercurrencies_n;
	}
	public BigDecimal getR21_othercurrencies_0() {
		return r21_othercurrencies_0;
	}
	public void setR21_othercurrencies_0(BigDecimal r21_othercurrencies_0) {
		this.r21_othercurrencies_0 = r21_othercurrencies_0;
	}
	public BigDecimal getR21_othercurrencies_p() {
		return r21_othercurrencies_p;
	}
	public void setR21_othercurrencies_p(BigDecimal r21_othercurrencies_p) {
		this.r21_othercurrencies_p = r21_othercurrencies_p;
	}
	public BigDecimal getR21_othercurrencies_q() {
		return r21_othercurrencies_q;
	}
	public void setR21_othercurrencies_q(BigDecimal r21_othercurrencies_q) {
		this.r21_othercurrencies_q = r21_othercurrencies_q;
	}
	public BigDecimal getR21_othercurrencies_r() {
		return r21_othercurrencies_r;
	}
	public void setR21_othercurrencies_r(BigDecimal r21_othercurrencies_r) {
		this.r21_othercurrencies_r = r21_othercurrencies_r;
	}
	public BigDecimal getR21_othercurrencies_s() {
		return r21_othercurrencies_s;
	}
	public void setR21_othercurrencies_s(BigDecimal r21_othercurrencies_s) {
		this.r21_othercurrencies_s = r21_othercurrencies_s;
	}
	public BigDecimal getR21_othercurrencies_t() {
		return r21_othercurrencies_t;
	}
	public void setR21_othercurrencies_t(BigDecimal r21_othercurrencies_t) {
		this.r21_othercurrencies_t = r21_othercurrencies_t;
	}
	public BigDecimal getR21_othercurrencies_u() {
		return r21_othercurrencies_u;
	}
	public void setR21_othercurrencies_u(BigDecimal r21_othercurrencies_u) {
		this.r21_othercurrencies_u = r21_othercurrencies_u;
	}
	public BigDecimal getR21_othercurrencies_v() {
		return r21_othercurrencies_v;
	}
	public void setR21_othercurrencies_v(BigDecimal r21_othercurrencies_v) {
		this.r21_othercurrencies_v = r21_othercurrencies_v;
	}
	public BigDecimal getR21_othercurrencies_w() {
		return r21_othercurrencies_w;
	}
	public void setR21_othercurrencies_w(BigDecimal r21_othercurrencies_w) {
		this.r21_othercurrencies_w = r21_othercurrencies_w;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_pula() {
		return r22_pula;
	}
	public void setR22_pula(BigDecimal r22_pula) {
		this.r22_pula = r22_pula;
	}
	public BigDecimal getR22_usd() {
		return r22_usd;
	}
	public void setR22_usd(BigDecimal r22_usd) {
		this.r22_usd = r22_usd;
	}
	public BigDecimal getR22_zar() {
		return r22_zar;
	}
	public void setR22_zar(BigDecimal r22_zar) {
		this.r22_zar = r22_zar;
	}
	public BigDecimal getR22_gbp() {
		return r22_gbp;
	}
	public void setR22_gbp(BigDecimal r22_gbp) {
		this.r22_gbp = r22_gbp;
	}
	public BigDecimal getR22_euro() {
		return r22_euro;
	}
	public void setR22_euro(BigDecimal r22_euro) {
		this.r22_euro = r22_euro;
	}
	public BigDecimal getR22_othercurrencies_inr() {
		return r22_othercurrencies_inr;
	}
	public void setR22_othercurrencies_inr(BigDecimal r22_othercurrencies_inr) {
		this.r22_othercurrencies_inr = r22_othercurrencies_inr;
	}
	public BigDecimal getR22_othercurrencies_aud() {
		return r22_othercurrencies_aud;
	}
	public void setR22_othercurrencies_aud(BigDecimal r22_othercurrencies_aud) {
		this.r22_othercurrencies_aud = r22_othercurrencies_aud;
	}
	public BigDecimal getR22_othercurrencies_i() {
		return r22_othercurrencies_i;
	}
	public void setR22_othercurrencies_i(BigDecimal r22_othercurrencies_i) {
		this.r22_othercurrencies_i = r22_othercurrencies_i;
	}
	public BigDecimal getR22_othercurrencies_j() {
		return r22_othercurrencies_j;
	}
	public void setR22_othercurrencies_j(BigDecimal r22_othercurrencies_j) {
		this.r22_othercurrencies_j = r22_othercurrencies_j;
	}
	public BigDecimal getR22_othercurrencies_k() {
		return r22_othercurrencies_k;
	}
	public void setR22_othercurrencies_k(BigDecimal r22_othercurrencies_k) {
		this.r22_othercurrencies_k = r22_othercurrencies_k;
	}
	public BigDecimal getR22_othercurrencies_l() {
		return r22_othercurrencies_l;
	}
	public void setR22_othercurrencies_l(BigDecimal r22_othercurrencies_l) {
		this.r22_othercurrencies_l = r22_othercurrencies_l;
	}
	public BigDecimal getR22_othercurrencies_m() {
		return r22_othercurrencies_m;
	}
	public void setR22_othercurrencies_m(BigDecimal r22_othercurrencies_m) {
		this.r22_othercurrencies_m = r22_othercurrencies_m;
	}
	public BigDecimal getR22_othercurrencies_n() {
		return r22_othercurrencies_n;
	}
	public void setR22_othercurrencies_n(BigDecimal r22_othercurrencies_n) {
		this.r22_othercurrencies_n = r22_othercurrencies_n;
	}
	public BigDecimal getR22_othercurrencies_0() {
		return r22_othercurrencies_0;
	}
	public void setR22_othercurrencies_0(BigDecimal r22_othercurrencies_0) {
		this.r22_othercurrencies_0 = r22_othercurrencies_0;
	}
	public BigDecimal getR22_othercurrencies_p() {
		return r22_othercurrencies_p;
	}
	public void setR22_othercurrencies_p(BigDecimal r22_othercurrencies_p) {
		this.r22_othercurrencies_p = r22_othercurrencies_p;
	}
	public BigDecimal getR22_othercurrencies_q() {
		return r22_othercurrencies_q;
	}
	public void setR22_othercurrencies_q(BigDecimal r22_othercurrencies_q) {
		this.r22_othercurrencies_q = r22_othercurrencies_q;
	}
	public BigDecimal getR22_othercurrencies_r() {
		return r22_othercurrencies_r;
	}
	public void setR22_othercurrencies_r(BigDecimal r22_othercurrencies_r) {
		this.r22_othercurrencies_r = r22_othercurrencies_r;
	}
	public BigDecimal getR22_othercurrencies_s() {
		return r22_othercurrencies_s;
	}
	public void setR22_othercurrencies_s(BigDecimal r22_othercurrencies_s) {
		this.r22_othercurrencies_s = r22_othercurrencies_s;
	}
	public BigDecimal getR22_othercurrencies_t() {
		return r22_othercurrencies_t;
	}
	public void setR22_othercurrencies_t(BigDecimal r22_othercurrencies_t) {
		this.r22_othercurrencies_t = r22_othercurrencies_t;
	}
	public BigDecimal getR22_othercurrencies_u() {
		return r22_othercurrencies_u;
	}
	public void setR22_othercurrencies_u(BigDecimal r22_othercurrencies_u) {
		this.r22_othercurrencies_u = r22_othercurrencies_u;
	}
	public BigDecimal getR22_othercurrencies_v() {
		return r22_othercurrencies_v;
	}
	public void setR22_othercurrencies_v(BigDecimal r22_othercurrencies_v) {
		this.r22_othercurrencies_v = r22_othercurrencies_v;
	}
	public BigDecimal getR22_othercurrencies_w() {
		return r22_othercurrencies_w;
	}
	public void setR22_othercurrencies_w(BigDecimal r22_othercurrencies_w) {
		this.r22_othercurrencies_w = r22_othercurrencies_w;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_pula() {
		return r23_pula;
	}
	public void setR23_pula(BigDecimal r23_pula) {
		this.r23_pula = r23_pula;
	}
	public BigDecimal getR23_usd() {
		return r23_usd;
	}
	public void setR23_usd(BigDecimal r23_usd) {
		this.r23_usd = r23_usd;
	}
	public BigDecimal getR23_zar() {
		return r23_zar;
	}
	public void setR23_zar(BigDecimal r23_zar) {
		this.r23_zar = r23_zar;
	}
	public BigDecimal getR23_gbp() {
		return r23_gbp;
	}
	public void setR23_gbp(BigDecimal r23_gbp) {
		this.r23_gbp = r23_gbp;
	}
	public BigDecimal getR23_euro() {
		return r23_euro;
	}
	public void setR23_euro(BigDecimal r23_euro) {
		this.r23_euro = r23_euro;
	}
	public BigDecimal getR23_othercurrencies_inr() {
		return r23_othercurrencies_inr;
	}
	public void setR23_othercurrencies_inr(BigDecimal r23_othercurrencies_inr) {
		this.r23_othercurrencies_inr = r23_othercurrencies_inr;
	}
	public BigDecimal getR23_othercurrencies_aud() {
		return r23_othercurrencies_aud;
	}
	public void setR23_othercurrencies_aud(BigDecimal r23_othercurrencies_aud) {
		this.r23_othercurrencies_aud = r23_othercurrencies_aud;
	}
	public BigDecimal getR23_othercurrencies_i() {
		return r23_othercurrencies_i;
	}
	public void setR23_othercurrencies_i(BigDecimal r23_othercurrencies_i) {
		this.r23_othercurrencies_i = r23_othercurrencies_i;
	}
	public BigDecimal getR23_othercurrencies_j() {
		return r23_othercurrencies_j;
	}
	public void setR23_othercurrencies_j(BigDecimal r23_othercurrencies_j) {
		this.r23_othercurrencies_j = r23_othercurrencies_j;
	}
	public BigDecimal getR23_othercurrencies_k() {
		return r23_othercurrencies_k;
	}
	public void setR23_othercurrencies_k(BigDecimal r23_othercurrencies_k) {
		this.r23_othercurrencies_k = r23_othercurrencies_k;
	}
	public BigDecimal getR23_othercurrencies_l() {
		return r23_othercurrencies_l;
	}
	public void setR23_othercurrencies_l(BigDecimal r23_othercurrencies_l) {
		this.r23_othercurrencies_l = r23_othercurrencies_l;
	}
	public BigDecimal getR23_othercurrencies_m() {
		return r23_othercurrencies_m;
	}
	public void setR23_othercurrencies_m(BigDecimal r23_othercurrencies_m) {
		this.r23_othercurrencies_m = r23_othercurrencies_m;
	}
	public BigDecimal getR23_othercurrencies_n() {
		return r23_othercurrencies_n;
	}
	public void setR23_othercurrencies_n(BigDecimal r23_othercurrencies_n) {
		this.r23_othercurrencies_n = r23_othercurrencies_n;
	}
	public BigDecimal getR23_othercurrencies_0() {
		return r23_othercurrencies_0;
	}
	public void setR23_othercurrencies_0(BigDecimal r23_othercurrencies_0) {
		this.r23_othercurrencies_0 = r23_othercurrencies_0;
	}
	public BigDecimal getR23_othercurrencies_p() {
		return r23_othercurrencies_p;
	}
	public void setR23_othercurrencies_p(BigDecimal r23_othercurrencies_p) {
		this.r23_othercurrencies_p = r23_othercurrencies_p;
	}
	public BigDecimal getR23_othercurrencies_q() {
		return r23_othercurrencies_q;
	}
	public void setR23_othercurrencies_q(BigDecimal r23_othercurrencies_q) {
		this.r23_othercurrencies_q = r23_othercurrencies_q;
	}
	public BigDecimal getR23_othercurrencies_r() {
		return r23_othercurrencies_r;
	}
	public void setR23_othercurrencies_r(BigDecimal r23_othercurrencies_r) {
		this.r23_othercurrencies_r = r23_othercurrencies_r;
	}
	public BigDecimal getR23_othercurrencies_s() {
		return r23_othercurrencies_s;
	}
	public void setR23_othercurrencies_s(BigDecimal r23_othercurrencies_s) {
		this.r23_othercurrencies_s = r23_othercurrencies_s;
	}
	public BigDecimal getR23_othercurrencies_t() {
		return r23_othercurrencies_t;
	}
	public void setR23_othercurrencies_t(BigDecimal r23_othercurrencies_t) {
		this.r23_othercurrencies_t = r23_othercurrencies_t;
	}
	public BigDecimal getR23_othercurrencies_u() {
		return r23_othercurrencies_u;
	}
	public void setR23_othercurrencies_u(BigDecimal r23_othercurrencies_u) {
		this.r23_othercurrencies_u = r23_othercurrencies_u;
	}
	public BigDecimal getR23_othercurrencies_v() {
		return r23_othercurrencies_v;
	}
	public void setR23_othercurrencies_v(BigDecimal r23_othercurrencies_v) {
		this.r23_othercurrencies_v = r23_othercurrencies_v;
	}
	public BigDecimal getR23_othercurrencies_w() {
		return r23_othercurrencies_w;
	}
	public void setR23_othercurrencies_w(BigDecimal r23_othercurrencies_w) {
		this.r23_othercurrencies_w = r23_othercurrencies_w;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_pula() {
		return r24_pula;
	}
	public void setR24_pula(BigDecimal r24_pula) {
		this.r24_pula = r24_pula;
	}
	public BigDecimal getR24_usd() {
		return r24_usd;
	}
	public void setR24_usd(BigDecimal r24_usd) {
		this.r24_usd = r24_usd;
	}
	public BigDecimal getR24_zar() {
		return r24_zar;
	}
	public void setR24_zar(BigDecimal r24_zar) {
		this.r24_zar = r24_zar;
	}
	public BigDecimal getR24_gbp() {
		return r24_gbp;
	}
	public void setR24_gbp(BigDecimal r24_gbp) {
		this.r24_gbp = r24_gbp;
	}
	public BigDecimal getR24_euro() {
		return r24_euro;
	}
	public void setR24_euro(BigDecimal r24_euro) {
		this.r24_euro = r24_euro;
	}
	public BigDecimal getR24_othercurrencies_inr() {
		return r24_othercurrencies_inr;
	}
	public void setR24_othercurrencies_inr(BigDecimal r24_othercurrencies_inr) {
		this.r24_othercurrencies_inr = r24_othercurrencies_inr;
	}
	public BigDecimal getR24_othercurrencies_aud() {
		return r24_othercurrencies_aud;
	}
	public void setR24_othercurrencies_aud(BigDecimal r24_othercurrencies_aud) {
		this.r24_othercurrencies_aud = r24_othercurrencies_aud;
	}
	public BigDecimal getR24_othercurrencies_i() {
		return r24_othercurrencies_i;
	}
	public void setR24_othercurrencies_i(BigDecimal r24_othercurrencies_i) {
		this.r24_othercurrencies_i = r24_othercurrencies_i;
	}
	public BigDecimal getR24_othercurrencies_j() {
		return r24_othercurrencies_j;
	}
	public void setR24_othercurrencies_j(BigDecimal r24_othercurrencies_j) {
		this.r24_othercurrencies_j = r24_othercurrencies_j;
	}
	public BigDecimal getR24_othercurrencies_k() {
		return r24_othercurrencies_k;
	}
	public void setR24_othercurrencies_k(BigDecimal r24_othercurrencies_k) {
		this.r24_othercurrencies_k = r24_othercurrencies_k;
	}
	public BigDecimal getR24_othercurrencies_l() {
		return r24_othercurrencies_l;
	}
	public void setR24_othercurrencies_l(BigDecimal r24_othercurrencies_l) {
		this.r24_othercurrencies_l = r24_othercurrencies_l;
	}
	public BigDecimal getR24_othercurrencies_m() {
		return r24_othercurrencies_m;
	}
	public void setR24_othercurrencies_m(BigDecimal r24_othercurrencies_m) {
		this.r24_othercurrencies_m = r24_othercurrencies_m;
	}
	public BigDecimal getR24_othercurrencies_n() {
		return r24_othercurrencies_n;
	}
	public void setR24_othercurrencies_n(BigDecimal r24_othercurrencies_n) {
		this.r24_othercurrencies_n = r24_othercurrencies_n;
	}
	public BigDecimal getR24_othercurrencies_0() {
		return r24_othercurrencies_0;
	}
	public void setR24_othercurrencies_0(BigDecimal r24_othercurrencies_0) {
		this.r24_othercurrencies_0 = r24_othercurrencies_0;
	}
	public BigDecimal getR24_othercurrencies_p() {
		return r24_othercurrencies_p;
	}
	public void setR24_othercurrencies_p(BigDecimal r24_othercurrencies_p) {
		this.r24_othercurrencies_p = r24_othercurrencies_p;
	}
	public BigDecimal getR24_othercurrencies_q() {
		return r24_othercurrencies_q;
	}
	public void setR24_othercurrencies_q(BigDecimal r24_othercurrencies_q) {
		this.r24_othercurrencies_q = r24_othercurrencies_q;
	}
	public BigDecimal getR24_othercurrencies_r() {
		return r24_othercurrencies_r;
	}
	public void setR24_othercurrencies_r(BigDecimal r24_othercurrencies_r) {
		this.r24_othercurrencies_r = r24_othercurrencies_r;
	}
	public BigDecimal getR24_othercurrencies_s() {
		return r24_othercurrencies_s;
	}
	public void setR24_othercurrencies_s(BigDecimal r24_othercurrencies_s) {
		this.r24_othercurrencies_s = r24_othercurrencies_s;
	}
	public BigDecimal getR24_othercurrencies_t() {
		return r24_othercurrencies_t;
	}
	public void setR24_othercurrencies_t(BigDecimal r24_othercurrencies_t) {
		this.r24_othercurrencies_t = r24_othercurrencies_t;
	}
	public BigDecimal getR24_othercurrencies_u() {
		return r24_othercurrencies_u;
	}
	public void setR24_othercurrencies_u(BigDecimal r24_othercurrencies_u) {
		this.r24_othercurrencies_u = r24_othercurrencies_u;
	}
	public BigDecimal getR24_othercurrencies_v() {
		return r24_othercurrencies_v;
	}
	public void setR24_othercurrencies_v(BigDecimal r24_othercurrencies_v) {
		this.r24_othercurrencies_v = r24_othercurrencies_v;
	}
	public BigDecimal getR24_othercurrencies_w() {
		return r24_othercurrencies_w;
	}
	public void setR24_othercurrencies_w(BigDecimal r24_othercurrencies_w) {
		this.r24_othercurrencies_w = r24_othercurrencies_w;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_pula() {
		return r25_pula;
	}
	public void setR25_pula(BigDecimal r25_pula) {
		this.r25_pula = r25_pula;
	}
	public BigDecimal getR25_usd() {
		return r25_usd;
	}
	public void setR25_usd(BigDecimal r25_usd) {
		this.r25_usd = r25_usd;
	}
	public BigDecimal getR25_zar() {
		return r25_zar;
	}
	public void setR25_zar(BigDecimal r25_zar) {
		this.r25_zar = r25_zar;
	}
	public BigDecimal getR25_gbp() {
		return r25_gbp;
	}
	public void setR25_gbp(BigDecimal r25_gbp) {
		this.r25_gbp = r25_gbp;
	}
	public BigDecimal getR25_euro() {
		return r25_euro;
	}
	public void setR25_euro(BigDecimal r25_euro) {
		this.r25_euro = r25_euro;
	}
	public BigDecimal getR25_othercurrencies_inr() {
		return r25_othercurrencies_inr;
	}
	public void setR25_othercurrencies_inr(BigDecimal r25_othercurrencies_inr) {
		this.r25_othercurrencies_inr = r25_othercurrencies_inr;
	}
	public BigDecimal getR25_othercurrencies_aud() {
		return r25_othercurrencies_aud;
	}
	public void setR25_othercurrencies_aud(BigDecimal r25_othercurrencies_aud) {
		this.r25_othercurrencies_aud = r25_othercurrencies_aud;
	}
	public BigDecimal getR25_othercurrencies_i() {
		return r25_othercurrencies_i;
	}
	public void setR25_othercurrencies_i(BigDecimal r25_othercurrencies_i) {
		this.r25_othercurrencies_i = r25_othercurrencies_i;
	}
	public BigDecimal getR25_othercurrencies_j() {
		return r25_othercurrencies_j;
	}
	public void setR25_othercurrencies_j(BigDecimal r25_othercurrencies_j) {
		this.r25_othercurrencies_j = r25_othercurrencies_j;
	}
	public BigDecimal getR25_othercurrencies_k() {
		return r25_othercurrencies_k;
	}
	public void setR25_othercurrencies_k(BigDecimal r25_othercurrencies_k) {
		this.r25_othercurrencies_k = r25_othercurrencies_k;
	}
	public BigDecimal getR25_othercurrencies_l() {
		return r25_othercurrencies_l;
	}
	public void setR25_othercurrencies_l(BigDecimal r25_othercurrencies_l) {
		this.r25_othercurrencies_l = r25_othercurrencies_l;
	}
	public BigDecimal getR25_othercurrencies_m() {
		return r25_othercurrencies_m;
	}
	public void setR25_othercurrencies_m(BigDecimal r25_othercurrencies_m) {
		this.r25_othercurrencies_m = r25_othercurrencies_m;
	}
	public BigDecimal getR25_othercurrencies_n() {
		return r25_othercurrencies_n;
	}
	public void setR25_othercurrencies_n(BigDecimal r25_othercurrencies_n) {
		this.r25_othercurrencies_n = r25_othercurrencies_n;
	}
	public BigDecimal getR25_othercurrencies_0() {
		return r25_othercurrencies_0;
	}
	public void setR25_othercurrencies_0(BigDecimal r25_othercurrencies_0) {
		this.r25_othercurrencies_0 = r25_othercurrencies_0;
	}
	public BigDecimal getR25_othercurrencies_p() {
		return r25_othercurrencies_p;
	}
	public void setR25_othercurrencies_p(BigDecimal r25_othercurrencies_p) {
		this.r25_othercurrencies_p = r25_othercurrencies_p;
	}
	public BigDecimal getR25_othercurrencies_q() {
		return r25_othercurrencies_q;
	}
	public void setR25_othercurrencies_q(BigDecimal r25_othercurrencies_q) {
		this.r25_othercurrencies_q = r25_othercurrencies_q;
	}
	public BigDecimal getR25_othercurrencies_r() {
		return r25_othercurrencies_r;
	}
	public void setR25_othercurrencies_r(BigDecimal r25_othercurrencies_r) {
		this.r25_othercurrencies_r = r25_othercurrencies_r;
	}
	public BigDecimal getR25_othercurrencies_s() {
		return r25_othercurrencies_s;
	}
	public void setR25_othercurrencies_s(BigDecimal r25_othercurrencies_s) {
		this.r25_othercurrencies_s = r25_othercurrencies_s;
	}
	public BigDecimal getR25_othercurrencies_t() {
		return r25_othercurrencies_t;
	}
	public void setR25_othercurrencies_t(BigDecimal r25_othercurrencies_t) {
		this.r25_othercurrencies_t = r25_othercurrencies_t;
	}
	public BigDecimal getR25_othercurrencies_u() {
		return r25_othercurrencies_u;
	}
	public void setR25_othercurrencies_u(BigDecimal r25_othercurrencies_u) {
		this.r25_othercurrencies_u = r25_othercurrencies_u;
	}
	public BigDecimal getR25_othercurrencies_v() {
		return r25_othercurrencies_v;
	}
	public void setR25_othercurrencies_v(BigDecimal r25_othercurrencies_v) {
		this.r25_othercurrencies_v = r25_othercurrencies_v;
	}
	public BigDecimal getR25_othercurrencies_w() {
		return r25_othercurrencies_w;
	}
	public void setR25_othercurrencies_w(BigDecimal r25_othercurrencies_w) {
		this.r25_othercurrencies_w = r25_othercurrencies_w;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_pula() {
		return r26_pula;
	}
	public void setR26_pula(BigDecimal r26_pula) {
		this.r26_pula = r26_pula;
	}
	public BigDecimal getR26_usd() {
		return r26_usd;
	}
	public void setR26_usd(BigDecimal r26_usd) {
		this.r26_usd = r26_usd;
	}
	public BigDecimal getR26_zar() {
		return r26_zar;
	}
	public void setR26_zar(BigDecimal r26_zar) {
		this.r26_zar = r26_zar;
	}
	public BigDecimal getR26_gbp() {
		return r26_gbp;
	}
	public void setR26_gbp(BigDecimal r26_gbp) {
		this.r26_gbp = r26_gbp;
	}
	public BigDecimal getR26_euro() {
		return r26_euro;
	}
	public void setR26_euro(BigDecimal r26_euro) {
		this.r26_euro = r26_euro;
	}
	public BigDecimal getR26_othercurrencies_inr() {
		return r26_othercurrencies_inr;
	}
	public void setR26_othercurrencies_inr(BigDecimal r26_othercurrencies_inr) {
		this.r26_othercurrencies_inr = r26_othercurrencies_inr;
	}
	public BigDecimal getR26_othercurrencies_aud() {
		return r26_othercurrencies_aud;
	}
	public void setR26_othercurrencies_aud(BigDecimal r26_othercurrencies_aud) {
		this.r26_othercurrencies_aud = r26_othercurrencies_aud;
	}
	public BigDecimal getR26_othercurrencies_i() {
		return r26_othercurrencies_i;
	}
	public void setR26_othercurrencies_i(BigDecimal r26_othercurrencies_i) {
		this.r26_othercurrencies_i = r26_othercurrencies_i;
	}
	public BigDecimal getR26_othercurrencies_j() {
		return r26_othercurrencies_j;
	}
	public void setR26_othercurrencies_j(BigDecimal r26_othercurrencies_j) {
		this.r26_othercurrencies_j = r26_othercurrencies_j;
	}
	public BigDecimal getR26_othercurrencies_k() {
		return r26_othercurrencies_k;
	}
	public void setR26_othercurrencies_k(BigDecimal r26_othercurrencies_k) {
		this.r26_othercurrencies_k = r26_othercurrencies_k;
	}
	public BigDecimal getR26_othercurrencies_l() {
		return r26_othercurrencies_l;
	}
	public void setR26_othercurrencies_l(BigDecimal r26_othercurrencies_l) {
		this.r26_othercurrencies_l = r26_othercurrencies_l;
	}
	public BigDecimal getR26_othercurrencies_m() {
		return r26_othercurrencies_m;
	}
	public void setR26_othercurrencies_m(BigDecimal r26_othercurrencies_m) {
		this.r26_othercurrencies_m = r26_othercurrencies_m;
	}
	public BigDecimal getR26_othercurrencies_n() {
		return r26_othercurrencies_n;
	}
	public void setR26_othercurrencies_n(BigDecimal r26_othercurrencies_n) {
		this.r26_othercurrencies_n = r26_othercurrencies_n;
	}
	public BigDecimal getR26_othercurrencies_0() {
		return r26_othercurrencies_0;
	}
	public void setR26_othercurrencies_0(BigDecimal r26_othercurrencies_0) {
		this.r26_othercurrencies_0 = r26_othercurrencies_0;
	}
	public BigDecimal getR26_othercurrencies_p() {
		return r26_othercurrencies_p;
	}
	public void setR26_othercurrencies_p(BigDecimal r26_othercurrencies_p) {
		this.r26_othercurrencies_p = r26_othercurrencies_p;
	}
	public BigDecimal getR26_othercurrencies_q() {
		return r26_othercurrencies_q;
	}
	public void setR26_othercurrencies_q(BigDecimal r26_othercurrencies_q) {
		this.r26_othercurrencies_q = r26_othercurrencies_q;
	}
	public BigDecimal getR26_othercurrencies_r() {
		return r26_othercurrencies_r;
	}
	public void setR26_othercurrencies_r(BigDecimal r26_othercurrencies_r) {
		this.r26_othercurrencies_r = r26_othercurrencies_r;
	}
	public BigDecimal getR26_othercurrencies_s() {
		return r26_othercurrencies_s;
	}
	public void setR26_othercurrencies_s(BigDecimal r26_othercurrencies_s) {
		this.r26_othercurrencies_s = r26_othercurrencies_s;
	}
	public BigDecimal getR26_othercurrencies_t() {
		return r26_othercurrencies_t;
	}
	public void setR26_othercurrencies_t(BigDecimal r26_othercurrencies_t) {
		this.r26_othercurrencies_t = r26_othercurrencies_t;
	}
	public BigDecimal getR26_othercurrencies_u() {
		return r26_othercurrencies_u;
	}
	public void setR26_othercurrencies_u(BigDecimal r26_othercurrencies_u) {
		this.r26_othercurrencies_u = r26_othercurrencies_u;
	}
	public BigDecimal getR26_othercurrencies_v() {
		return r26_othercurrencies_v;
	}
	public void setR26_othercurrencies_v(BigDecimal r26_othercurrencies_v) {
		this.r26_othercurrencies_v = r26_othercurrencies_v;
	}
	public BigDecimal getR26_othercurrencies_w() {
		return r26_othercurrencies_w;
	}
	public void setR26_othercurrencies_w(BigDecimal r26_othercurrencies_w) {
		this.r26_othercurrencies_w = r26_othercurrencies_w;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_pula() {
		return r27_pula;
	}
	public void setR27_pula(BigDecimal r27_pula) {
		this.r27_pula = r27_pula;
	}
	public BigDecimal getR27_usd() {
		return r27_usd;
	}
	public void setR27_usd(BigDecimal r27_usd) {
		this.r27_usd = r27_usd;
	}
	public BigDecimal getR27_zar() {
		return r27_zar;
	}
	public void setR27_zar(BigDecimal r27_zar) {
		this.r27_zar = r27_zar;
	}
	public BigDecimal getR27_gbp() {
		return r27_gbp;
	}
	public void setR27_gbp(BigDecimal r27_gbp) {
		this.r27_gbp = r27_gbp;
	}
	public BigDecimal getR27_euro() {
		return r27_euro;
	}
	public void setR27_euro(BigDecimal r27_euro) {
		this.r27_euro = r27_euro;
	}
	public BigDecimal getR27_othercurrencies_inr() {
		return r27_othercurrencies_inr;
	}
	public void setR27_othercurrencies_inr(BigDecimal r27_othercurrencies_inr) {
		this.r27_othercurrencies_inr = r27_othercurrencies_inr;
	}
	public BigDecimal getR27_othercurrencies_aud() {
		return r27_othercurrencies_aud;
	}
	public void setR27_othercurrencies_aud(BigDecimal r27_othercurrencies_aud) {
		this.r27_othercurrencies_aud = r27_othercurrencies_aud;
	}
	public BigDecimal getR27_othercurrencies_i() {
		return r27_othercurrencies_i;
	}
	public void setR27_othercurrencies_i(BigDecimal r27_othercurrencies_i) {
		this.r27_othercurrencies_i = r27_othercurrencies_i;
	}
	public BigDecimal getR27_othercurrencies_j() {
		return r27_othercurrencies_j;
	}
	public void setR27_othercurrencies_j(BigDecimal r27_othercurrencies_j) {
		this.r27_othercurrencies_j = r27_othercurrencies_j;
	}
	public BigDecimal getR27_othercurrencies_k() {
		return r27_othercurrencies_k;
	}
	public void setR27_othercurrencies_k(BigDecimal r27_othercurrencies_k) {
		this.r27_othercurrencies_k = r27_othercurrencies_k;
	}
	public BigDecimal getR27_othercurrencies_l() {
		return r27_othercurrencies_l;
	}
	public void setR27_othercurrencies_l(BigDecimal r27_othercurrencies_l) {
		this.r27_othercurrencies_l = r27_othercurrencies_l;
	}
	public BigDecimal getR27_othercurrencies_m() {
		return r27_othercurrencies_m;
	}
	public void setR27_othercurrencies_m(BigDecimal r27_othercurrencies_m) {
		this.r27_othercurrencies_m = r27_othercurrencies_m;
	}
	public BigDecimal getR27_othercurrencies_n() {
		return r27_othercurrencies_n;
	}
	public void setR27_othercurrencies_n(BigDecimal r27_othercurrencies_n) {
		this.r27_othercurrencies_n = r27_othercurrencies_n;
	}
	public BigDecimal getR27_othercurrencies_0() {
		return r27_othercurrencies_0;
	}
	public void setR27_othercurrencies_0(BigDecimal r27_othercurrencies_0) {
		this.r27_othercurrencies_0 = r27_othercurrencies_0;
	}
	public BigDecimal getR27_othercurrencies_p() {
		return r27_othercurrencies_p;
	}
	public void setR27_othercurrencies_p(BigDecimal r27_othercurrencies_p) {
		this.r27_othercurrencies_p = r27_othercurrencies_p;
	}
	public BigDecimal getR27_othercurrencies_q() {
		return r27_othercurrencies_q;
	}
	public void setR27_othercurrencies_q(BigDecimal r27_othercurrencies_q) {
		this.r27_othercurrencies_q = r27_othercurrencies_q;
	}
	public BigDecimal getR27_othercurrencies_r() {
		return r27_othercurrencies_r;
	}
	public void setR27_othercurrencies_r(BigDecimal r27_othercurrencies_r) {
		this.r27_othercurrencies_r = r27_othercurrencies_r;
	}
	public BigDecimal getR27_othercurrencies_s() {
		return r27_othercurrencies_s;
	}
	public void setR27_othercurrencies_s(BigDecimal r27_othercurrencies_s) {
		this.r27_othercurrencies_s = r27_othercurrencies_s;
	}
	public BigDecimal getR27_othercurrencies_t() {
		return r27_othercurrencies_t;
	}
	public void setR27_othercurrencies_t(BigDecimal r27_othercurrencies_t) {
		this.r27_othercurrencies_t = r27_othercurrencies_t;
	}
	public BigDecimal getR27_othercurrencies_u() {
		return r27_othercurrencies_u;
	}
	public void setR27_othercurrencies_u(BigDecimal r27_othercurrencies_u) {
		this.r27_othercurrencies_u = r27_othercurrencies_u;
	}
	public BigDecimal getR27_othercurrencies_v() {
		return r27_othercurrencies_v;
	}
	public void setR27_othercurrencies_v(BigDecimal r27_othercurrencies_v) {
		this.r27_othercurrencies_v = r27_othercurrencies_v;
	}
	public BigDecimal getR27_othercurrencies_w() {
		return r27_othercurrencies_w;
	}
	public void setR27_othercurrencies_w(BigDecimal r27_othercurrencies_w) {
		this.r27_othercurrencies_w = r27_othercurrencies_w;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_pula() {
		return r28_pula;
	}
	public void setR28_pula(BigDecimal r28_pula) {
		this.r28_pula = r28_pula;
	}
	public BigDecimal getR28_usd() {
		return r28_usd;
	}
	public void setR28_usd(BigDecimal r28_usd) {
		this.r28_usd = r28_usd;
	}
	public BigDecimal getR28_zar() {
		return r28_zar;
	}
	public void setR28_zar(BigDecimal r28_zar) {
		this.r28_zar = r28_zar;
	}
	public BigDecimal getR28_gbp() {
		return r28_gbp;
	}
	public void setR28_gbp(BigDecimal r28_gbp) {
		this.r28_gbp = r28_gbp;
	}
	public BigDecimal getR28_euro() {
		return r28_euro;
	}
	public void setR28_euro(BigDecimal r28_euro) {
		this.r28_euro = r28_euro;
	}
	public BigDecimal getR28_othercurrencies_inr() {
		return r28_othercurrencies_inr;
	}
	public void setR28_othercurrencies_inr(BigDecimal r28_othercurrencies_inr) {
		this.r28_othercurrencies_inr = r28_othercurrencies_inr;
	}
	public BigDecimal getR28_othercurrencies_aud() {
		return r28_othercurrencies_aud;
	}
	public void setR28_othercurrencies_aud(BigDecimal r28_othercurrencies_aud) {
		this.r28_othercurrencies_aud = r28_othercurrencies_aud;
	}
	public BigDecimal getR28_othercurrencies_i() {
		return r28_othercurrencies_i;
	}
	public void setR28_othercurrencies_i(BigDecimal r28_othercurrencies_i) {
		this.r28_othercurrencies_i = r28_othercurrencies_i;
	}
	public BigDecimal getR28_othercurrencies_j() {
		return r28_othercurrencies_j;
	}
	public void setR28_othercurrencies_j(BigDecimal r28_othercurrencies_j) {
		this.r28_othercurrencies_j = r28_othercurrencies_j;
	}
	public BigDecimal getR28_othercurrencies_k() {
		return r28_othercurrencies_k;
	}
	public void setR28_othercurrencies_k(BigDecimal r28_othercurrencies_k) {
		this.r28_othercurrencies_k = r28_othercurrencies_k;
	}
	public BigDecimal getR28_othercurrencies_l() {
		return r28_othercurrencies_l;
	}
	public void setR28_othercurrencies_l(BigDecimal r28_othercurrencies_l) {
		this.r28_othercurrencies_l = r28_othercurrencies_l;
	}
	public BigDecimal getR28_othercurrencies_m() {
		return r28_othercurrencies_m;
	}
	public void setR28_othercurrencies_m(BigDecimal r28_othercurrencies_m) {
		this.r28_othercurrencies_m = r28_othercurrencies_m;
	}
	public BigDecimal getR28_othercurrencies_n() {
		return r28_othercurrencies_n;
	}
	public void setR28_othercurrencies_n(BigDecimal r28_othercurrencies_n) {
		this.r28_othercurrencies_n = r28_othercurrencies_n;
	}
	public BigDecimal getR28_othercurrencies_0() {
		return r28_othercurrencies_0;
	}
	public void setR28_othercurrencies_0(BigDecimal r28_othercurrencies_0) {
		this.r28_othercurrencies_0 = r28_othercurrencies_0;
	}
	public BigDecimal getR28_othercurrencies_p() {
		return r28_othercurrencies_p;
	}
	public void setR28_othercurrencies_p(BigDecimal r28_othercurrencies_p) {
		this.r28_othercurrencies_p = r28_othercurrencies_p;
	}
	public BigDecimal getR28_othercurrencies_q() {
		return r28_othercurrencies_q;
	}
	public void setR28_othercurrencies_q(BigDecimal r28_othercurrencies_q) {
		this.r28_othercurrencies_q = r28_othercurrencies_q;
	}
	public BigDecimal getR28_othercurrencies_r() {
		return r28_othercurrencies_r;
	}
	public void setR28_othercurrencies_r(BigDecimal r28_othercurrencies_r) {
		this.r28_othercurrencies_r = r28_othercurrencies_r;
	}
	public BigDecimal getR28_othercurrencies_s() {
		return r28_othercurrencies_s;
	}
	public void setR28_othercurrencies_s(BigDecimal r28_othercurrencies_s) {
		this.r28_othercurrencies_s = r28_othercurrencies_s;
	}
	public BigDecimal getR28_othercurrencies_t() {
		return r28_othercurrencies_t;
	}
	public void setR28_othercurrencies_t(BigDecimal r28_othercurrencies_t) {
		this.r28_othercurrencies_t = r28_othercurrencies_t;
	}
	public BigDecimal getR28_othercurrencies_u() {
		return r28_othercurrencies_u;
	}
	public void setR28_othercurrencies_u(BigDecimal r28_othercurrencies_u) {
		this.r28_othercurrencies_u = r28_othercurrencies_u;
	}
	public BigDecimal getR28_othercurrencies_v() {
		return r28_othercurrencies_v;
	}
	public void setR28_othercurrencies_v(BigDecimal r28_othercurrencies_v) {
		this.r28_othercurrencies_v = r28_othercurrencies_v;
	}
	public BigDecimal getR28_othercurrencies_w() {
		return r28_othercurrencies_w;
	}
	public void setR28_othercurrencies_w(BigDecimal r28_othercurrencies_w) {
		this.r28_othercurrencies_w = r28_othercurrencies_w;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_pula() {
		return r29_pula;
	}
	public void setR29_pula(BigDecimal r29_pula) {
		this.r29_pula = r29_pula;
	}
	public BigDecimal getR29_usd() {
		return r29_usd;
	}
	public void setR29_usd(BigDecimal r29_usd) {
		this.r29_usd = r29_usd;
	}
	public BigDecimal getR29_zar() {
		return r29_zar;
	}
	public void setR29_zar(BigDecimal r29_zar) {
		this.r29_zar = r29_zar;
	}
	public BigDecimal getR29_gbp() {
		return r29_gbp;
	}
	public void setR29_gbp(BigDecimal r29_gbp) {
		this.r29_gbp = r29_gbp;
	}
	public BigDecimal getR29_euro() {
		return r29_euro;
	}
	public void setR29_euro(BigDecimal r29_euro) {
		this.r29_euro = r29_euro;
	}
	public BigDecimal getR29_othercurrencies_inr() {
		return r29_othercurrencies_inr;
	}
	public void setR29_othercurrencies_inr(BigDecimal r29_othercurrencies_inr) {
		this.r29_othercurrencies_inr = r29_othercurrencies_inr;
	}
	public BigDecimal getR29_othercurrencies_aud() {
		return r29_othercurrencies_aud;
	}
	public void setR29_othercurrencies_aud(BigDecimal r29_othercurrencies_aud) {
		this.r29_othercurrencies_aud = r29_othercurrencies_aud;
	}
	public BigDecimal getR29_othercurrencies_i() {
		return r29_othercurrencies_i;
	}
	public void setR29_othercurrencies_i(BigDecimal r29_othercurrencies_i) {
		this.r29_othercurrencies_i = r29_othercurrencies_i;
	}
	public BigDecimal getR29_othercurrencies_j() {
		return r29_othercurrencies_j;
	}
	public void setR29_othercurrencies_j(BigDecimal r29_othercurrencies_j) {
		this.r29_othercurrencies_j = r29_othercurrencies_j;
	}
	public BigDecimal getR29_othercurrencies_k() {
		return r29_othercurrencies_k;
	}
	public void setR29_othercurrencies_k(BigDecimal r29_othercurrencies_k) {
		this.r29_othercurrencies_k = r29_othercurrencies_k;
	}
	public BigDecimal getR29_othercurrencies_l() {
		return r29_othercurrencies_l;
	}
	public void setR29_othercurrencies_l(BigDecimal r29_othercurrencies_l) {
		this.r29_othercurrencies_l = r29_othercurrencies_l;
	}
	public BigDecimal getR29_othercurrencies_m() {
		return r29_othercurrencies_m;
	}
	public void setR29_othercurrencies_m(BigDecimal r29_othercurrencies_m) {
		this.r29_othercurrencies_m = r29_othercurrencies_m;
	}
	public BigDecimal getR29_othercurrencies_n() {
		return r29_othercurrencies_n;
	}
	public void setR29_othercurrencies_n(BigDecimal r29_othercurrencies_n) {
		this.r29_othercurrencies_n = r29_othercurrencies_n;
	}
	public BigDecimal getR29_othercurrencies_0() {
		return r29_othercurrencies_0;
	}
	public void setR29_othercurrencies_0(BigDecimal r29_othercurrencies_0) {
		this.r29_othercurrencies_0 = r29_othercurrencies_0;
	}
	public BigDecimal getR29_othercurrencies_p() {
		return r29_othercurrencies_p;
	}
	public void setR29_othercurrencies_p(BigDecimal r29_othercurrencies_p) {
		this.r29_othercurrencies_p = r29_othercurrencies_p;
	}
	public BigDecimal getR29_othercurrencies_q() {
		return r29_othercurrencies_q;
	}
	public void setR29_othercurrencies_q(BigDecimal r29_othercurrencies_q) {
		this.r29_othercurrencies_q = r29_othercurrencies_q;
	}
	public BigDecimal getR29_othercurrencies_r() {
		return r29_othercurrencies_r;
	}
	public void setR29_othercurrencies_r(BigDecimal r29_othercurrencies_r) {
		this.r29_othercurrencies_r = r29_othercurrencies_r;
	}
	public BigDecimal getR29_othercurrencies_s() {
		return r29_othercurrencies_s;
	}
	public void setR29_othercurrencies_s(BigDecimal r29_othercurrencies_s) {
		this.r29_othercurrencies_s = r29_othercurrencies_s;
	}
	public BigDecimal getR29_othercurrencies_t() {
		return r29_othercurrencies_t;
	}
	public void setR29_othercurrencies_t(BigDecimal r29_othercurrencies_t) {
		this.r29_othercurrencies_t = r29_othercurrencies_t;
	}
	public BigDecimal getR29_othercurrencies_u() {
		return r29_othercurrencies_u;
	}
	public void setR29_othercurrencies_u(BigDecimal r29_othercurrencies_u) {
		this.r29_othercurrencies_u = r29_othercurrencies_u;
	}
	public BigDecimal getR29_othercurrencies_v() {
		return r29_othercurrencies_v;
	}
	public void setR29_othercurrencies_v(BigDecimal r29_othercurrencies_v) {
		this.r29_othercurrencies_v = r29_othercurrencies_v;
	}
	public BigDecimal getR29_othercurrencies_w() {
		return r29_othercurrencies_w;
	}
	public void setR29_othercurrencies_w(BigDecimal r29_othercurrencies_w) {
		this.r29_othercurrencies_w = r29_othercurrencies_w;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_pula() {
		return r30_pula;
	}
	public void setR30_pula(BigDecimal r30_pula) {
		this.r30_pula = r30_pula;
	}
	public BigDecimal getR30_usd() {
		return r30_usd;
	}
	public void setR30_usd(BigDecimal r30_usd) {
		this.r30_usd = r30_usd;
	}
	public BigDecimal getR30_zar() {
		return r30_zar;
	}
	public void setR30_zar(BigDecimal r30_zar) {
		this.r30_zar = r30_zar;
	}
	public BigDecimal getR30_gbp() {
		return r30_gbp;
	}
	public void setR30_gbp(BigDecimal r30_gbp) {
		this.r30_gbp = r30_gbp;
	}
	public BigDecimal getR30_euro() {
		return r30_euro;
	}
	public void setR30_euro(BigDecimal r30_euro) {
		this.r30_euro = r30_euro;
	}
	public BigDecimal getR30_othercurrencies_inr() {
		return r30_othercurrencies_inr;
	}
	public void setR30_othercurrencies_inr(BigDecimal r30_othercurrencies_inr) {
		this.r30_othercurrencies_inr = r30_othercurrencies_inr;
	}
	public BigDecimal getR30_othercurrencies_aud() {
		return r30_othercurrencies_aud;
	}
	public void setR30_othercurrencies_aud(BigDecimal r30_othercurrencies_aud) {
		this.r30_othercurrencies_aud = r30_othercurrencies_aud;
	}
	public BigDecimal getR30_othercurrencies_i() {
		return r30_othercurrencies_i;
	}
	public void setR30_othercurrencies_i(BigDecimal r30_othercurrencies_i) {
		this.r30_othercurrencies_i = r30_othercurrencies_i;
	}
	public BigDecimal getR30_othercurrencies_j() {
		return r30_othercurrencies_j;
	}
	public void setR30_othercurrencies_j(BigDecimal r30_othercurrencies_j) {
		this.r30_othercurrencies_j = r30_othercurrencies_j;
	}
	public BigDecimal getR30_othercurrencies_k() {
		return r30_othercurrencies_k;
	}
	public void setR30_othercurrencies_k(BigDecimal r30_othercurrencies_k) {
		this.r30_othercurrencies_k = r30_othercurrencies_k;
	}
	public BigDecimal getR30_othercurrencies_l() {
		return r30_othercurrencies_l;
	}
	public void setR30_othercurrencies_l(BigDecimal r30_othercurrencies_l) {
		this.r30_othercurrencies_l = r30_othercurrencies_l;
	}
	public BigDecimal getR30_othercurrencies_m() {
		return r30_othercurrencies_m;
	}
	public void setR30_othercurrencies_m(BigDecimal r30_othercurrencies_m) {
		this.r30_othercurrencies_m = r30_othercurrencies_m;
	}
	public BigDecimal getR30_othercurrencies_n() {
		return r30_othercurrencies_n;
	}
	public void setR30_othercurrencies_n(BigDecimal r30_othercurrencies_n) {
		this.r30_othercurrencies_n = r30_othercurrencies_n;
	}
	public BigDecimal getR30_othercurrencies_0() {
		return r30_othercurrencies_0;
	}
	public void setR30_othercurrencies_0(BigDecimal r30_othercurrencies_0) {
		this.r30_othercurrencies_0 = r30_othercurrencies_0;
	}
	public BigDecimal getR30_othercurrencies_p() {
		return r30_othercurrencies_p;
	}
	public void setR30_othercurrencies_p(BigDecimal r30_othercurrencies_p) {
		this.r30_othercurrencies_p = r30_othercurrencies_p;
	}
	public BigDecimal getR30_othercurrencies_q() {
		return r30_othercurrencies_q;
	}
	public void setR30_othercurrencies_q(BigDecimal r30_othercurrencies_q) {
		this.r30_othercurrencies_q = r30_othercurrencies_q;
	}
	public BigDecimal getR30_othercurrencies_r() {
		return r30_othercurrencies_r;
	}
	public void setR30_othercurrencies_r(BigDecimal r30_othercurrencies_r) {
		this.r30_othercurrencies_r = r30_othercurrencies_r;
	}
	public BigDecimal getR30_othercurrencies_s() {
		return r30_othercurrencies_s;
	}
	public void setR30_othercurrencies_s(BigDecimal r30_othercurrencies_s) {
		this.r30_othercurrencies_s = r30_othercurrencies_s;
	}
	public BigDecimal getR30_othercurrencies_t() {
		return r30_othercurrencies_t;
	}
	public void setR30_othercurrencies_t(BigDecimal r30_othercurrencies_t) {
		this.r30_othercurrencies_t = r30_othercurrencies_t;
	}
	public BigDecimal getR30_othercurrencies_u() {
		return r30_othercurrencies_u;
	}
	public void setR30_othercurrencies_u(BigDecimal r30_othercurrencies_u) {
		this.r30_othercurrencies_u = r30_othercurrencies_u;
	}
	public BigDecimal getR30_othercurrencies_v() {
		return r30_othercurrencies_v;
	}
	public void setR30_othercurrencies_v(BigDecimal r30_othercurrencies_v) {
		this.r30_othercurrencies_v = r30_othercurrencies_v;
	}
	public BigDecimal getR30_othercurrencies_w() {
		return r30_othercurrencies_w;
	}
	public void setR30_othercurrencies_w(BigDecimal r30_othercurrencies_w) {
		this.r30_othercurrencies_w = r30_othercurrencies_w;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_pula() {
		return r31_pula;
	}
	public void setR31_pula(BigDecimal r31_pula) {
		this.r31_pula = r31_pula;
	}
	public BigDecimal getR31_usd() {
		return r31_usd;
	}
	public void setR31_usd(BigDecimal r31_usd) {
		this.r31_usd = r31_usd;
	}
	public BigDecimal getR31_zar() {
		return r31_zar;
	}
	public void setR31_zar(BigDecimal r31_zar) {
		this.r31_zar = r31_zar;
	}
	public BigDecimal getR31_gbp() {
		return r31_gbp;
	}
	public void setR31_gbp(BigDecimal r31_gbp) {
		this.r31_gbp = r31_gbp;
	}
	public BigDecimal getR31_euro() {
		return r31_euro;
	}
	public void setR31_euro(BigDecimal r31_euro) {
		this.r31_euro = r31_euro;
	}
	public BigDecimal getR31_othercurrencies_inr() {
		return r31_othercurrencies_inr;
	}
	public void setR31_othercurrencies_inr(BigDecimal r31_othercurrencies_inr) {
		this.r31_othercurrencies_inr = r31_othercurrencies_inr;
	}
	public BigDecimal getR31_othercurrencies_aud() {
		return r31_othercurrencies_aud;
	}
	public void setR31_othercurrencies_aud(BigDecimal r31_othercurrencies_aud) {
		this.r31_othercurrencies_aud = r31_othercurrencies_aud;
	}
	public BigDecimal getR31_othercurrencies_i() {
		return r31_othercurrencies_i;
	}
	public void setR31_othercurrencies_i(BigDecimal r31_othercurrencies_i) {
		this.r31_othercurrencies_i = r31_othercurrencies_i;
	}
	public BigDecimal getR31_othercurrencies_j() {
		return r31_othercurrencies_j;
	}
	public void setR31_othercurrencies_j(BigDecimal r31_othercurrencies_j) {
		this.r31_othercurrencies_j = r31_othercurrencies_j;
	}
	public BigDecimal getR31_othercurrencies_k() {
		return r31_othercurrencies_k;
	}
	public void setR31_othercurrencies_k(BigDecimal r31_othercurrencies_k) {
		this.r31_othercurrencies_k = r31_othercurrencies_k;
	}
	public BigDecimal getR31_othercurrencies_l() {
		return r31_othercurrencies_l;
	}
	public void setR31_othercurrencies_l(BigDecimal r31_othercurrencies_l) {
		this.r31_othercurrencies_l = r31_othercurrencies_l;
	}
	public BigDecimal getR31_othercurrencies_m() {
		return r31_othercurrencies_m;
	}
	public void setR31_othercurrencies_m(BigDecimal r31_othercurrencies_m) {
		this.r31_othercurrencies_m = r31_othercurrencies_m;
	}
	public BigDecimal getR31_othercurrencies_n() {
		return r31_othercurrencies_n;
	}
	public void setR31_othercurrencies_n(BigDecimal r31_othercurrencies_n) {
		this.r31_othercurrencies_n = r31_othercurrencies_n;
	}
	public BigDecimal getR31_othercurrencies_0() {
		return r31_othercurrencies_0;
	}
	public void setR31_othercurrencies_0(BigDecimal r31_othercurrencies_0) {
		this.r31_othercurrencies_0 = r31_othercurrencies_0;
	}
	public BigDecimal getR31_othercurrencies_p() {
		return r31_othercurrencies_p;
	}
	public void setR31_othercurrencies_p(BigDecimal r31_othercurrencies_p) {
		this.r31_othercurrencies_p = r31_othercurrencies_p;
	}
	public BigDecimal getR31_othercurrencies_q() {
		return r31_othercurrencies_q;
	}
	public void setR31_othercurrencies_q(BigDecimal r31_othercurrencies_q) {
		this.r31_othercurrencies_q = r31_othercurrencies_q;
	}
	public BigDecimal getR31_othercurrencies_r() {
		return r31_othercurrencies_r;
	}
	public void setR31_othercurrencies_r(BigDecimal r31_othercurrencies_r) {
		this.r31_othercurrencies_r = r31_othercurrencies_r;
	}
	public BigDecimal getR31_othercurrencies_s() {
		return r31_othercurrencies_s;
	}
	public void setR31_othercurrencies_s(BigDecimal r31_othercurrencies_s) {
		this.r31_othercurrencies_s = r31_othercurrencies_s;
	}
	public BigDecimal getR31_othercurrencies_t() {
		return r31_othercurrencies_t;
	}
	public void setR31_othercurrencies_t(BigDecimal r31_othercurrencies_t) {
		this.r31_othercurrencies_t = r31_othercurrencies_t;
	}
	public BigDecimal getR31_othercurrencies_u() {
		return r31_othercurrencies_u;
	}
	public void setR31_othercurrencies_u(BigDecimal r31_othercurrencies_u) {
		this.r31_othercurrencies_u = r31_othercurrencies_u;
	}
	public BigDecimal getR31_othercurrencies_v() {
		return r31_othercurrencies_v;
	}
	public void setR31_othercurrencies_v(BigDecimal r31_othercurrencies_v) {
		this.r31_othercurrencies_v = r31_othercurrencies_v;
	}
	public BigDecimal getR31_othercurrencies_w() {
		return r31_othercurrencies_w;
	}
	public void setR31_othercurrencies_w(BigDecimal r31_othercurrencies_w) {
		this.r31_othercurrencies_w = r31_othercurrencies_w;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_pula() {
		return r32_pula;
	}
	public void setR32_pula(BigDecimal r32_pula) {
		this.r32_pula = r32_pula;
	}
	public BigDecimal getR32_usd() {
		return r32_usd;
	}
	public void setR32_usd(BigDecimal r32_usd) {
		this.r32_usd = r32_usd;
	}
	public BigDecimal getR32_zar() {
		return r32_zar;
	}
	public void setR32_zar(BigDecimal r32_zar) {
		this.r32_zar = r32_zar;
	}
	public BigDecimal getR32_gbp() {
		return r32_gbp;
	}
	public void setR32_gbp(BigDecimal r32_gbp) {
		this.r32_gbp = r32_gbp;
	}
	public BigDecimal getR32_euro() {
		return r32_euro;
	}
	public void setR32_euro(BigDecimal r32_euro) {
		this.r32_euro = r32_euro;
	}
	public BigDecimal getR32_othercurrencies_inr() {
		return r32_othercurrencies_inr;
	}
	public void setR32_othercurrencies_inr(BigDecimal r32_othercurrencies_inr) {
		this.r32_othercurrencies_inr = r32_othercurrencies_inr;
	}
	public BigDecimal getR32_othercurrencies_aud() {
		return r32_othercurrencies_aud;
	}
	public void setR32_othercurrencies_aud(BigDecimal r32_othercurrencies_aud) {
		this.r32_othercurrencies_aud = r32_othercurrencies_aud;
	}
	public BigDecimal getR32_othercurrencies_i() {
		return r32_othercurrencies_i;
	}
	public void setR32_othercurrencies_i(BigDecimal r32_othercurrencies_i) {
		this.r32_othercurrencies_i = r32_othercurrencies_i;
	}
	public BigDecimal getR32_othercurrencies_j() {
		return r32_othercurrencies_j;
	}
	public void setR32_othercurrencies_j(BigDecimal r32_othercurrencies_j) {
		this.r32_othercurrencies_j = r32_othercurrencies_j;
	}
	public BigDecimal getR32_othercurrencies_k() {
		return r32_othercurrencies_k;
	}
	public void setR32_othercurrencies_k(BigDecimal r32_othercurrencies_k) {
		this.r32_othercurrencies_k = r32_othercurrencies_k;
	}
	public BigDecimal getR32_othercurrencies_l() {
		return r32_othercurrencies_l;
	}
	public void setR32_othercurrencies_l(BigDecimal r32_othercurrencies_l) {
		this.r32_othercurrencies_l = r32_othercurrencies_l;
	}
	public BigDecimal getR32_othercurrencies_m() {
		return r32_othercurrencies_m;
	}
	public void setR32_othercurrencies_m(BigDecimal r32_othercurrencies_m) {
		this.r32_othercurrencies_m = r32_othercurrencies_m;
	}
	public BigDecimal getR32_othercurrencies_n() {
		return r32_othercurrencies_n;
	}
	public void setR32_othercurrencies_n(BigDecimal r32_othercurrencies_n) {
		this.r32_othercurrencies_n = r32_othercurrencies_n;
	}
	public BigDecimal getR32_othercurrencies_0() {
		return r32_othercurrencies_0;
	}
	public void setR32_othercurrencies_0(BigDecimal r32_othercurrencies_0) {
		this.r32_othercurrencies_0 = r32_othercurrencies_0;
	}
	public BigDecimal getR32_othercurrencies_p() {
		return r32_othercurrencies_p;
	}
	public void setR32_othercurrencies_p(BigDecimal r32_othercurrencies_p) {
		this.r32_othercurrencies_p = r32_othercurrencies_p;
	}
	public BigDecimal getR32_othercurrencies_q() {
		return r32_othercurrencies_q;
	}
	public void setR32_othercurrencies_q(BigDecimal r32_othercurrencies_q) {
		this.r32_othercurrencies_q = r32_othercurrencies_q;
	}
	public BigDecimal getR32_othercurrencies_r() {
		return r32_othercurrencies_r;
	}
	public void setR32_othercurrencies_r(BigDecimal r32_othercurrencies_r) {
		this.r32_othercurrencies_r = r32_othercurrencies_r;
	}
	public BigDecimal getR32_othercurrencies_s() {
		return r32_othercurrencies_s;
	}
	public void setR32_othercurrencies_s(BigDecimal r32_othercurrencies_s) {
		this.r32_othercurrencies_s = r32_othercurrencies_s;
	}
	public BigDecimal getR32_othercurrencies_t() {
		return r32_othercurrencies_t;
	}
	public void setR32_othercurrencies_t(BigDecimal r32_othercurrencies_t) {
		this.r32_othercurrencies_t = r32_othercurrencies_t;
	}
	public BigDecimal getR32_othercurrencies_u() {
		return r32_othercurrencies_u;
	}
	public void setR32_othercurrencies_u(BigDecimal r32_othercurrencies_u) {
		this.r32_othercurrencies_u = r32_othercurrencies_u;
	}
	public BigDecimal getR32_othercurrencies_v() {
		return r32_othercurrencies_v;
	}
	public void setR32_othercurrencies_v(BigDecimal r32_othercurrencies_v) {
		this.r32_othercurrencies_v = r32_othercurrencies_v;
	}
	public BigDecimal getR32_othercurrencies_w() {
		return r32_othercurrencies_w;
	}
	public void setR32_othercurrencies_w(BigDecimal r32_othercurrencies_w) {
		this.r32_othercurrencies_w = r32_othercurrencies_w;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_pula() {
		return r33_pula;
	}
	public void setR33_pula(BigDecimal r33_pula) {
		this.r33_pula = r33_pula;
	}
	public BigDecimal getR33_usd() {
		return r33_usd;
	}
	public void setR33_usd(BigDecimal r33_usd) {
		this.r33_usd = r33_usd;
	}
	public BigDecimal getR33_zar() {
		return r33_zar;
	}
	public void setR33_zar(BigDecimal r33_zar) {
		this.r33_zar = r33_zar;
	}
	public BigDecimal getR33_gbp() {
		return r33_gbp;
	}
	public void setR33_gbp(BigDecimal r33_gbp) {
		this.r33_gbp = r33_gbp;
	}
	public BigDecimal getR33_euro() {
		return r33_euro;
	}
	public void setR33_euro(BigDecimal r33_euro) {
		this.r33_euro = r33_euro;
	}
	public BigDecimal getR33_othercurrencies_inr() {
		return r33_othercurrencies_inr;
	}
	public void setR33_othercurrencies_inr(BigDecimal r33_othercurrencies_inr) {
		this.r33_othercurrencies_inr = r33_othercurrencies_inr;
	}
	public BigDecimal getR33_othercurrencies_aud() {
		return r33_othercurrencies_aud;
	}
	public void setR33_othercurrencies_aud(BigDecimal r33_othercurrencies_aud) {
		this.r33_othercurrencies_aud = r33_othercurrencies_aud;
	}
	public BigDecimal getR33_othercurrencies_i() {
		return r33_othercurrencies_i;
	}
	public void setR33_othercurrencies_i(BigDecimal r33_othercurrencies_i) {
		this.r33_othercurrencies_i = r33_othercurrencies_i;
	}
	public BigDecimal getR33_othercurrencies_j() {
		return r33_othercurrencies_j;
	}
	public void setR33_othercurrencies_j(BigDecimal r33_othercurrencies_j) {
		this.r33_othercurrencies_j = r33_othercurrencies_j;
	}
	public BigDecimal getR33_othercurrencies_k() {
		return r33_othercurrencies_k;
	}
	public void setR33_othercurrencies_k(BigDecimal r33_othercurrencies_k) {
		this.r33_othercurrencies_k = r33_othercurrencies_k;
	}
	public BigDecimal getR33_othercurrencies_l() {
		return r33_othercurrencies_l;
	}
	public void setR33_othercurrencies_l(BigDecimal r33_othercurrencies_l) {
		this.r33_othercurrencies_l = r33_othercurrencies_l;
	}
	public BigDecimal getR33_othercurrencies_m() {
		return r33_othercurrencies_m;
	}
	public void setR33_othercurrencies_m(BigDecimal r33_othercurrencies_m) {
		this.r33_othercurrencies_m = r33_othercurrencies_m;
	}
	public BigDecimal getR33_othercurrencies_n() {
		return r33_othercurrencies_n;
	}
	public void setR33_othercurrencies_n(BigDecimal r33_othercurrencies_n) {
		this.r33_othercurrencies_n = r33_othercurrencies_n;
	}
	public BigDecimal getR33_othercurrencies_0() {
		return r33_othercurrencies_0;
	}
	public void setR33_othercurrencies_0(BigDecimal r33_othercurrencies_0) {
		this.r33_othercurrencies_0 = r33_othercurrencies_0;
	}
	public BigDecimal getR33_othercurrencies_p() {
		return r33_othercurrencies_p;
	}
	public void setR33_othercurrencies_p(BigDecimal r33_othercurrencies_p) {
		this.r33_othercurrencies_p = r33_othercurrencies_p;
	}
	public BigDecimal getR33_othercurrencies_q() {
		return r33_othercurrencies_q;
	}
	public void setR33_othercurrencies_q(BigDecimal r33_othercurrencies_q) {
		this.r33_othercurrencies_q = r33_othercurrencies_q;
	}
	public BigDecimal getR33_othercurrencies_r() {
		return r33_othercurrencies_r;
	}
	public void setR33_othercurrencies_r(BigDecimal r33_othercurrencies_r) {
		this.r33_othercurrencies_r = r33_othercurrencies_r;
	}
	public BigDecimal getR33_othercurrencies_s() {
		return r33_othercurrencies_s;
	}
	public void setR33_othercurrencies_s(BigDecimal r33_othercurrencies_s) {
		this.r33_othercurrencies_s = r33_othercurrencies_s;
	}
	public BigDecimal getR33_othercurrencies_t() {
		return r33_othercurrencies_t;
	}
	public void setR33_othercurrencies_t(BigDecimal r33_othercurrencies_t) {
		this.r33_othercurrencies_t = r33_othercurrencies_t;
	}
	public BigDecimal getR33_othercurrencies_u() {
		return r33_othercurrencies_u;
	}
	public void setR33_othercurrencies_u(BigDecimal r33_othercurrencies_u) {
		this.r33_othercurrencies_u = r33_othercurrencies_u;
	}
	public BigDecimal getR33_othercurrencies_v() {
		return r33_othercurrencies_v;
	}
	public void setR33_othercurrencies_v(BigDecimal r33_othercurrencies_v) {
		this.r33_othercurrencies_v = r33_othercurrencies_v;
	}
	public BigDecimal getR33_othercurrencies_w() {
		return r33_othercurrencies_w;
	}
	public void setR33_othercurrencies_w(BigDecimal r33_othercurrencies_w) {
		this.r33_othercurrencies_w = r33_othercurrencies_w;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_pula() {
		return r34_pula;
	}
	public void setR34_pula(BigDecimal r34_pula) {
		this.r34_pula = r34_pula;
	}
	public BigDecimal getR34_usd() {
		return r34_usd;
	}
	public void setR34_usd(BigDecimal r34_usd) {
		this.r34_usd = r34_usd;
	}
	public BigDecimal getR34_zar() {
		return r34_zar;
	}
	public void setR34_zar(BigDecimal r34_zar) {
		this.r34_zar = r34_zar;
	}
	public BigDecimal getR34_gbp() {
		return r34_gbp;
	}
	public void setR34_gbp(BigDecimal r34_gbp) {
		this.r34_gbp = r34_gbp;
	}
	public BigDecimal getR34_euro() {
		return r34_euro;
	}
	public void setR34_euro(BigDecimal r34_euro) {
		this.r34_euro = r34_euro;
	}
	public BigDecimal getR34_othercurrencies_inr() {
		return r34_othercurrencies_inr;
	}
	public void setR34_othercurrencies_inr(BigDecimal r34_othercurrencies_inr) {
		this.r34_othercurrencies_inr = r34_othercurrencies_inr;
	}
	public BigDecimal getR34_othercurrencies_aud() {
		return r34_othercurrencies_aud;
	}
	public void setR34_othercurrencies_aud(BigDecimal r34_othercurrencies_aud) {
		this.r34_othercurrencies_aud = r34_othercurrencies_aud;
	}
	public BigDecimal getR34_othercurrencies_i() {
		return r34_othercurrencies_i;
	}
	public void setR34_othercurrencies_i(BigDecimal r34_othercurrencies_i) {
		this.r34_othercurrencies_i = r34_othercurrencies_i;
	}
	public BigDecimal getR34_othercurrencies_j() {
		return r34_othercurrencies_j;
	}
	public void setR34_othercurrencies_j(BigDecimal r34_othercurrencies_j) {
		this.r34_othercurrencies_j = r34_othercurrencies_j;
	}
	public BigDecimal getR34_othercurrencies_k() {
		return r34_othercurrencies_k;
	}
	public void setR34_othercurrencies_k(BigDecimal r34_othercurrencies_k) {
		this.r34_othercurrencies_k = r34_othercurrencies_k;
	}
	public BigDecimal getR34_othercurrencies_l() {
		return r34_othercurrencies_l;
	}
	public void setR34_othercurrencies_l(BigDecimal r34_othercurrencies_l) {
		this.r34_othercurrencies_l = r34_othercurrencies_l;
	}
	public BigDecimal getR34_othercurrencies_m() {
		return r34_othercurrencies_m;
	}
	public void setR34_othercurrencies_m(BigDecimal r34_othercurrencies_m) {
		this.r34_othercurrencies_m = r34_othercurrencies_m;
	}
	public BigDecimal getR34_othercurrencies_n() {
		return r34_othercurrencies_n;
	}
	public void setR34_othercurrencies_n(BigDecimal r34_othercurrencies_n) {
		this.r34_othercurrencies_n = r34_othercurrencies_n;
	}
	public BigDecimal getR34_othercurrencies_0() {
		return r34_othercurrencies_0;
	}
	public void setR34_othercurrencies_0(BigDecimal r34_othercurrencies_0) {
		this.r34_othercurrencies_0 = r34_othercurrencies_0;
	}
	public BigDecimal getR34_othercurrencies_p() {
		return r34_othercurrencies_p;
	}
	public void setR34_othercurrencies_p(BigDecimal r34_othercurrencies_p) {
		this.r34_othercurrencies_p = r34_othercurrencies_p;
	}
	public BigDecimal getR34_othercurrencies_q() {
		return r34_othercurrencies_q;
	}
	public void setR34_othercurrencies_q(BigDecimal r34_othercurrencies_q) {
		this.r34_othercurrencies_q = r34_othercurrencies_q;
	}
	public BigDecimal getR34_othercurrencies_r() {
		return r34_othercurrencies_r;
	}
	public void setR34_othercurrencies_r(BigDecimal r34_othercurrencies_r) {
		this.r34_othercurrencies_r = r34_othercurrencies_r;
	}
	public BigDecimal getR34_othercurrencies_s() {
		return r34_othercurrencies_s;
	}
	public void setR34_othercurrencies_s(BigDecimal r34_othercurrencies_s) {
		this.r34_othercurrencies_s = r34_othercurrencies_s;
	}
	public BigDecimal getR34_othercurrencies_t() {
		return r34_othercurrencies_t;
	}
	public void setR34_othercurrencies_t(BigDecimal r34_othercurrencies_t) {
		this.r34_othercurrencies_t = r34_othercurrencies_t;
	}
	public BigDecimal getR34_othercurrencies_u() {
		return r34_othercurrencies_u;
	}
	public void setR34_othercurrencies_u(BigDecimal r34_othercurrencies_u) {
		this.r34_othercurrencies_u = r34_othercurrencies_u;
	}
	public BigDecimal getR34_othercurrencies_v() {
		return r34_othercurrencies_v;
	}
	public void setR34_othercurrencies_v(BigDecimal r34_othercurrencies_v) {
		this.r34_othercurrencies_v = r34_othercurrencies_v;
	}
	public BigDecimal getR34_othercurrencies_w() {
		return r34_othercurrencies_w;
	}
	public void setR34_othercurrencies_w(BigDecimal r34_othercurrencies_w) {
		this.r34_othercurrencies_w = r34_othercurrencies_w;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_pula() {
		return r35_pula;
	}
	public void setR35_pula(BigDecimal r35_pula) {
		this.r35_pula = r35_pula;
	}
	public BigDecimal getR35_usd() {
		return r35_usd;
	}
	public void setR35_usd(BigDecimal r35_usd) {
		this.r35_usd = r35_usd;
	}
	public BigDecimal getR35_zar() {
		return r35_zar;
	}
	public void setR35_zar(BigDecimal r35_zar) {
		this.r35_zar = r35_zar;
	}
	public BigDecimal getR35_gbp() {
		return r35_gbp;
	}
	public void setR35_gbp(BigDecimal r35_gbp) {
		this.r35_gbp = r35_gbp;
	}
	public BigDecimal getR35_euro() {
		return r35_euro;
	}
	public void setR35_euro(BigDecimal r35_euro) {
		this.r35_euro = r35_euro;
	}
	public BigDecimal getR35_othercurrencies_inr() {
		return r35_othercurrencies_inr;
	}
	public void setR35_othercurrencies_inr(BigDecimal r35_othercurrencies_inr) {
		this.r35_othercurrencies_inr = r35_othercurrencies_inr;
	}
	public BigDecimal getR35_othercurrencies_aud() {
		return r35_othercurrencies_aud;
	}
	public void setR35_othercurrencies_aud(BigDecimal r35_othercurrencies_aud) {
		this.r35_othercurrencies_aud = r35_othercurrencies_aud;
	}
	public BigDecimal getR35_othercurrencies_i() {
		return r35_othercurrencies_i;
	}
	public void setR35_othercurrencies_i(BigDecimal r35_othercurrencies_i) {
		this.r35_othercurrencies_i = r35_othercurrencies_i;
	}
	public BigDecimal getR35_othercurrencies_j() {
		return r35_othercurrencies_j;
	}
	public void setR35_othercurrencies_j(BigDecimal r35_othercurrencies_j) {
		this.r35_othercurrencies_j = r35_othercurrencies_j;
	}
	public BigDecimal getR35_othercurrencies_k() {
		return r35_othercurrencies_k;
	}
	public void setR35_othercurrencies_k(BigDecimal r35_othercurrencies_k) {
		this.r35_othercurrencies_k = r35_othercurrencies_k;
	}
	public BigDecimal getR35_othercurrencies_l() {
		return r35_othercurrencies_l;
	}
	public void setR35_othercurrencies_l(BigDecimal r35_othercurrencies_l) {
		this.r35_othercurrencies_l = r35_othercurrencies_l;
	}
	public BigDecimal getR35_othercurrencies_m() {
		return r35_othercurrencies_m;
	}
	public void setR35_othercurrencies_m(BigDecimal r35_othercurrencies_m) {
		this.r35_othercurrencies_m = r35_othercurrencies_m;
	}
	public BigDecimal getR35_othercurrencies_n() {
		return r35_othercurrencies_n;
	}
	public void setR35_othercurrencies_n(BigDecimal r35_othercurrencies_n) {
		this.r35_othercurrencies_n = r35_othercurrencies_n;
	}
	public BigDecimal getR35_othercurrencies_0() {
		return r35_othercurrencies_0;
	}
	public void setR35_othercurrencies_0(BigDecimal r35_othercurrencies_0) {
		this.r35_othercurrencies_0 = r35_othercurrencies_0;
	}
	public BigDecimal getR35_othercurrencies_p() {
		return r35_othercurrencies_p;
	}
	public void setR35_othercurrencies_p(BigDecimal r35_othercurrencies_p) {
		this.r35_othercurrencies_p = r35_othercurrencies_p;
	}
	public BigDecimal getR35_othercurrencies_q() {
		return r35_othercurrencies_q;
	}
	public void setR35_othercurrencies_q(BigDecimal r35_othercurrencies_q) {
		this.r35_othercurrencies_q = r35_othercurrencies_q;
	}
	public BigDecimal getR35_othercurrencies_r() {
		return r35_othercurrencies_r;
	}
	public void setR35_othercurrencies_r(BigDecimal r35_othercurrencies_r) {
		this.r35_othercurrencies_r = r35_othercurrencies_r;
	}
	public BigDecimal getR35_othercurrencies_s() {
		return r35_othercurrencies_s;
	}
	public void setR35_othercurrencies_s(BigDecimal r35_othercurrencies_s) {
		this.r35_othercurrencies_s = r35_othercurrencies_s;
	}
	public BigDecimal getR35_othercurrencies_t() {
		return r35_othercurrencies_t;
	}
	public void setR35_othercurrencies_t(BigDecimal r35_othercurrencies_t) {
		this.r35_othercurrencies_t = r35_othercurrencies_t;
	}
	public BigDecimal getR35_othercurrencies_u() {
		return r35_othercurrencies_u;
	}
	public void setR35_othercurrencies_u(BigDecimal r35_othercurrencies_u) {
		this.r35_othercurrencies_u = r35_othercurrencies_u;
	}
	public BigDecimal getR35_othercurrencies_v() {
		return r35_othercurrencies_v;
	}
	public void setR35_othercurrencies_v(BigDecimal r35_othercurrencies_v) {
		this.r35_othercurrencies_v = r35_othercurrencies_v;
	}
	public BigDecimal getR35_othercurrencies_w() {
		return r35_othercurrencies_w;
	}
	public void setR35_othercurrencies_w(BigDecimal r35_othercurrencies_w) {
		this.r35_othercurrencies_w = r35_othercurrencies_w;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_pula() {
		return r36_pula;
	}
	public void setR36_pula(BigDecimal r36_pula) {
		this.r36_pula = r36_pula;
	}
	public BigDecimal getR36_usd() {
		return r36_usd;
	}
	public void setR36_usd(BigDecimal r36_usd) {
		this.r36_usd = r36_usd;
	}
	public BigDecimal getR36_zar() {
		return r36_zar;
	}
	public void setR36_zar(BigDecimal r36_zar) {
		this.r36_zar = r36_zar;
	}
	public BigDecimal getR36_gbp() {
		return r36_gbp;
	}
	public void setR36_gbp(BigDecimal r36_gbp) {
		this.r36_gbp = r36_gbp;
	}
	public BigDecimal getR36_euro() {
		return r36_euro;
	}
	public void setR36_euro(BigDecimal r36_euro) {
		this.r36_euro = r36_euro;
	}
	public BigDecimal getR36_othercurrencies_inr() {
		return r36_othercurrencies_inr;
	}
	public void setR36_othercurrencies_inr(BigDecimal r36_othercurrencies_inr) {
		this.r36_othercurrencies_inr = r36_othercurrencies_inr;
	}
	public BigDecimal getR36_othercurrencies_aud() {
		return r36_othercurrencies_aud;
	}
	public void setR36_othercurrencies_aud(BigDecimal r36_othercurrencies_aud) {
		this.r36_othercurrencies_aud = r36_othercurrencies_aud;
	}
	public BigDecimal getR36_othercurrencies_i() {
		return r36_othercurrencies_i;
	}
	public void setR36_othercurrencies_i(BigDecimal r36_othercurrencies_i) {
		this.r36_othercurrencies_i = r36_othercurrencies_i;
	}
	public BigDecimal getR36_othercurrencies_j() {
		return r36_othercurrencies_j;
	}
	public void setR36_othercurrencies_j(BigDecimal r36_othercurrencies_j) {
		this.r36_othercurrencies_j = r36_othercurrencies_j;
	}
	public BigDecimal getR36_othercurrencies_k() {
		return r36_othercurrencies_k;
	}
	public void setR36_othercurrencies_k(BigDecimal r36_othercurrencies_k) {
		this.r36_othercurrencies_k = r36_othercurrencies_k;
	}
	public BigDecimal getR36_othercurrencies_l() {
		return r36_othercurrencies_l;
	}
	public void setR36_othercurrencies_l(BigDecimal r36_othercurrencies_l) {
		this.r36_othercurrencies_l = r36_othercurrencies_l;
	}
	public BigDecimal getR36_othercurrencies_m() {
		return r36_othercurrencies_m;
	}
	public void setR36_othercurrencies_m(BigDecimal r36_othercurrencies_m) {
		this.r36_othercurrencies_m = r36_othercurrencies_m;
	}
	public BigDecimal getR36_othercurrencies_n() {
		return r36_othercurrencies_n;
	}
	public void setR36_othercurrencies_n(BigDecimal r36_othercurrencies_n) {
		this.r36_othercurrencies_n = r36_othercurrencies_n;
	}
	public BigDecimal getR36_othercurrencies_0() {
		return r36_othercurrencies_0;
	}
	public void setR36_othercurrencies_0(BigDecimal r36_othercurrencies_0) {
		this.r36_othercurrencies_0 = r36_othercurrencies_0;
	}
	public BigDecimal getR36_othercurrencies_p() {
		return r36_othercurrencies_p;
	}
	public void setR36_othercurrencies_p(BigDecimal r36_othercurrencies_p) {
		this.r36_othercurrencies_p = r36_othercurrencies_p;
	}
	public BigDecimal getR36_othercurrencies_q() {
		return r36_othercurrencies_q;
	}
	public void setR36_othercurrencies_q(BigDecimal r36_othercurrencies_q) {
		this.r36_othercurrencies_q = r36_othercurrencies_q;
	}
	public BigDecimal getR36_othercurrencies_r() {
		return r36_othercurrencies_r;
	}
	public void setR36_othercurrencies_r(BigDecimal r36_othercurrencies_r) {
		this.r36_othercurrencies_r = r36_othercurrencies_r;
	}
	public BigDecimal getR36_othercurrencies_s() {
		return r36_othercurrencies_s;
	}
	public void setR36_othercurrencies_s(BigDecimal r36_othercurrencies_s) {
		this.r36_othercurrencies_s = r36_othercurrencies_s;
	}
	public BigDecimal getR36_othercurrencies_t() {
		return r36_othercurrencies_t;
	}
	public void setR36_othercurrencies_t(BigDecimal r36_othercurrencies_t) {
		this.r36_othercurrencies_t = r36_othercurrencies_t;
	}
	public BigDecimal getR36_othercurrencies_u() {
		return r36_othercurrencies_u;
	}
	public void setR36_othercurrencies_u(BigDecimal r36_othercurrencies_u) {
		this.r36_othercurrencies_u = r36_othercurrencies_u;
	}
	public BigDecimal getR36_othercurrencies_v() {
		return r36_othercurrencies_v;
	}
	public void setR36_othercurrencies_v(BigDecimal r36_othercurrencies_v) {
		this.r36_othercurrencies_v = r36_othercurrencies_v;
	}
	public BigDecimal getR36_othercurrencies_w() {
		return r36_othercurrencies_w;
	}
	public void setR36_othercurrencies_w(BigDecimal r36_othercurrencies_w) {
		this.r36_othercurrencies_w = r36_othercurrencies_w;
	}
	public String getR37_product() {
		return r37_product;
	}
	public void setR37_product(String r37_product) {
		this.r37_product = r37_product;
	}
	public BigDecimal getR37_pula() {
		return r37_pula;
	}
	public void setR37_pula(BigDecimal r37_pula) {
		this.r37_pula = r37_pula;
	}
	public BigDecimal getR37_usd() {
		return r37_usd;
	}
	public void setR37_usd(BigDecimal r37_usd) {
		this.r37_usd = r37_usd;
	}
	public BigDecimal getR37_zar() {
		return r37_zar;
	}
	public void setR37_zar(BigDecimal r37_zar) {
		this.r37_zar = r37_zar;
	}
	public BigDecimal getR37_gbp() {
		return r37_gbp;
	}
	public void setR37_gbp(BigDecimal r37_gbp) {
		this.r37_gbp = r37_gbp;
	}
	public BigDecimal getR37_euro() {
		return r37_euro;
	}
	public void setR37_euro(BigDecimal r37_euro) {
		this.r37_euro = r37_euro;
	}
	public BigDecimal getR37_othercurrencies_inr() {
		return r37_othercurrencies_inr;
	}
	public void setR37_othercurrencies_inr(BigDecimal r37_othercurrencies_inr) {
		this.r37_othercurrencies_inr = r37_othercurrencies_inr;
	}
	public BigDecimal getR37_othercurrencies_aud() {
		return r37_othercurrencies_aud;
	}
	public void setR37_othercurrencies_aud(BigDecimal r37_othercurrencies_aud) {
		this.r37_othercurrencies_aud = r37_othercurrencies_aud;
	}
	public BigDecimal getR37_othercurrencies_i() {
		return r37_othercurrencies_i;
	}
	public void setR37_othercurrencies_i(BigDecimal r37_othercurrencies_i) {
		this.r37_othercurrencies_i = r37_othercurrencies_i;
	}
	public BigDecimal getR37_othercurrencies_j() {
		return r37_othercurrencies_j;
	}
	public void setR37_othercurrencies_j(BigDecimal r37_othercurrencies_j) {
		this.r37_othercurrencies_j = r37_othercurrencies_j;
	}
	public BigDecimal getR37_othercurrencies_k() {
		return r37_othercurrencies_k;
	}
	public void setR37_othercurrencies_k(BigDecimal r37_othercurrencies_k) {
		this.r37_othercurrencies_k = r37_othercurrencies_k;
	}
	public BigDecimal getR37_othercurrencies_l() {
		return r37_othercurrencies_l;
	}
	public void setR37_othercurrencies_l(BigDecimal r37_othercurrencies_l) {
		this.r37_othercurrencies_l = r37_othercurrencies_l;
	}
	public BigDecimal getR37_othercurrencies_m() {
		return r37_othercurrencies_m;
	}
	public void setR37_othercurrencies_m(BigDecimal r37_othercurrencies_m) {
		this.r37_othercurrencies_m = r37_othercurrencies_m;
	}
	public BigDecimal getR37_othercurrencies_n() {
		return r37_othercurrencies_n;
	}
	public void setR37_othercurrencies_n(BigDecimal r37_othercurrencies_n) {
		this.r37_othercurrencies_n = r37_othercurrencies_n;
	}
	public BigDecimal getR37_othercurrencies_0() {
		return r37_othercurrencies_0;
	}
	public void setR37_othercurrencies_0(BigDecimal r37_othercurrencies_0) {
		this.r37_othercurrencies_0 = r37_othercurrencies_0;
	}
	public BigDecimal getR37_othercurrencies_p() {
		return r37_othercurrencies_p;
	}
	public void setR37_othercurrencies_p(BigDecimal r37_othercurrencies_p) {
		this.r37_othercurrencies_p = r37_othercurrencies_p;
	}
	public BigDecimal getR37_othercurrencies_q() {
		return r37_othercurrencies_q;
	}
	public void setR37_othercurrencies_q(BigDecimal r37_othercurrencies_q) {
		this.r37_othercurrencies_q = r37_othercurrencies_q;
	}
	public BigDecimal getR37_othercurrencies_r() {
		return r37_othercurrencies_r;
	}
	public void setR37_othercurrencies_r(BigDecimal r37_othercurrencies_r) {
		this.r37_othercurrencies_r = r37_othercurrencies_r;
	}
	public BigDecimal getR37_othercurrencies_s() {
		return r37_othercurrencies_s;
	}
	public void setR37_othercurrencies_s(BigDecimal r37_othercurrencies_s) {
		this.r37_othercurrencies_s = r37_othercurrencies_s;
	}
	public BigDecimal getR37_othercurrencies_t() {
		return r37_othercurrencies_t;
	}
	public void setR37_othercurrencies_t(BigDecimal r37_othercurrencies_t) {
		this.r37_othercurrencies_t = r37_othercurrencies_t;
	}
	public BigDecimal getR37_othercurrencies_u() {
		return r37_othercurrencies_u;
	}
	public void setR37_othercurrencies_u(BigDecimal r37_othercurrencies_u) {
		this.r37_othercurrencies_u = r37_othercurrencies_u;
	}
	public BigDecimal getR37_othercurrencies_v() {
		return r37_othercurrencies_v;
	}
	public void setR37_othercurrencies_v(BigDecimal r37_othercurrencies_v) {
		this.r37_othercurrencies_v = r37_othercurrencies_v;
	}
	public BigDecimal getR37_othercurrencies_w() {
		return r37_othercurrencies_w;
	}
	public void setR37_othercurrencies_w(BigDecimal r37_othercurrencies_w) {
		this.r37_othercurrencies_w = r37_othercurrencies_w;
	}
	public String getR38_product() {
		return r38_product;
	}
	public void setR38_product(String r38_product) {
		this.r38_product = r38_product;
	}
	public BigDecimal getR38_pula() {
		return r38_pula;
	}
	public void setR38_pula(BigDecimal r38_pula) {
		this.r38_pula = r38_pula;
	}
	public BigDecimal getR38_usd() {
		return r38_usd;
	}
	public void setR38_usd(BigDecimal r38_usd) {
		this.r38_usd = r38_usd;
	}
	public BigDecimal getR38_zar() {
		return r38_zar;
	}
	public void setR38_zar(BigDecimal r38_zar) {
		this.r38_zar = r38_zar;
	}
	public BigDecimal getR38_gbp() {
		return r38_gbp;
	}
	public void setR38_gbp(BigDecimal r38_gbp) {
		this.r38_gbp = r38_gbp;
	}
	public BigDecimal getR38_euro() {
		return r38_euro;
	}
	public void setR38_euro(BigDecimal r38_euro) {
		this.r38_euro = r38_euro;
	}
	public BigDecimal getR38_othercurrencies_inr() {
		return r38_othercurrencies_inr;
	}
	public void setR38_othercurrencies_inr(BigDecimal r38_othercurrencies_inr) {
		this.r38_othercurrencies_inr = r38_othercurrencies_inr;
	}
	public BigDecimal getR38_othercurrencies_aud() {
		return r38_othercurrencies_aud;
	}
	public void setR38_othercurrencies_aud(BigDecimal r38_othercurrencies_aud) {
		this.r38_othercurrencies_aud = r38_othercurrencies_aud;
	}
	public BigDecimal getR38_othercurrencies_i() {
		return r38_othercurrencies_i;
	}
	public void setR38_othercurrencies_i(BigDecimal r38_othercurrencies_i) {
		this.r38_othercurrencies_i = r38_othercurrencies_i;
	}
	public BigDecimal getR38_othercurrencies_j() {
		return r38_othercurrencies_j;
	}
	public void setR38_othercurrencies_j(BigDecimal r38_othercurrencies_j) {
		this.r38_othercurrencies_j = r38_othercurrencies_j;
	}
	public BigDecimal getR38_othercurrencies_k() {
		return r38_othercurrencies_k;
	}
	public void setR38_othercurrencies_k(BigDecimal r38_othercurrencies_k) {
		this.r38_othercurrencies_k = r38_othercurrencies_k;
	}
	public BigDecimal getR38_othercurrencies_l() {
		return r38_othercurrencies_l;
	}
	public void setR38_othercurrencies_l(BigDecimal r38_othercurrencies_l) {
		this.r38_othercurrencies_l = r38_othercurrencies_l;
	}
	public BigDecimal getR38_othercurrencies_m() {
		return r38_othercurrencies_m;
	}
	public void setR38_othercurrencies_m(BigDecimal r38_othercurrencies_m) {
		this.r38_othercurrencies_m = r38_othercurrencies_m;
	}
	public BigDecimal getR38_othercurrencies_n() {
		return r38_othercurrencies_n;
	}
	public void setR38_othercurrencies_n(BigDecimal r38_othercurrencies_n) {
		this.r38_othercurrencies_n = r38_othercurrencies_n;
	}
	public BigDecimal getR38_othercurrencies_0() {
		return r38_othercurrencies_0;
	}
	public void setR38_othercurrencies_0(BigDecimal r38_othercurrencies_0) {
		this.r38_othercurrencies_0 = r38_othercurrencies_0;
	}
	public BigDecimal getR38_othercurrencies_p() {
		return r38_othercurrencies_p;
	}
	public void setR38_othercurrencies_p(BigDecimal r38_othercurrencies_p) {
		this.r38_othercurrencies_p = r38_othercurrencies_p;
	}
	public BigDecimal getR38_othercurrencies_q() {
		return r38_othercurrencies_q;
	}
	public void setR38_othercurrencies_q(BigDecimal r38_othercurrencies_q) {
		this.r38_othercurrencies_q = r38_othercurrencies_q;
	}
	public BigDecimal getR38_othercurrencies_r() {
		return r38_othercurrencies_r;
	}
	public void setR38_othercurrencies_r(BigDecimal r38_othercurrencies_r) {
		this.r38_othercurrencies_r = r38_othercurrencies_r;
	}
	public BigDecimal getR38_othercurrencies_s() {
		return r38_othercurrencies_s;
	}
	public void setR38_othercurrencies_s(BigDecimal r38_othercurrencies_s) {
		this.r38_othercurrencies_s = r38_othercurrencies_s;
	}
	public BigDecimal getR38_othercurrencies_t() {
		return r38_othercurrencies_t;
	}
	public void setR38_othercurrencies_t(BigDecimal r38_othercurrencies_t) {
		this.r38_othercurrencies_t = r38_othercurrencies_t;
	}
	public BigDecimal getR38_othercurrencies_u() {
		return r38_othercurrencies_u;
	}
	public void setR38_othercurrencies_u(BigDecimal r38_othercurrencies_u) {
		this.r38_othercurrencies_u = r38_othercurrencies_u;
	}
	public BigDecimal getR38_othercurrencies_v() {
		return r38_othercurrencies_v;
	}
	public void setR38_othercurrencies_v(BigDecimal r38_othercurrencies_v) {
		this.r38_othercurrencies_v = r38_othercurrencies_v;
	}
	public BigDecimal getR38_othercurrencies_w() {
		return r38_othercurrencies_w;
	}
	public void setR38_othercurrencies_w(BigDecimal r38_othercurrencies_w) {
		this.r38_othercurrencies_w = r38_othercurrencies_w;
	}
	public String getR39_product() {
		return r39_product;
	}
	public void setR39_product(String r39_product) {
		this.r39_product = r39_product;
	}
	public BigDecimal getR39_pula() {
		return r39_pula;
	}
	public void setR39_pula(BigDecimal r39_pula) {
		this.r39_pula = r39_pula;
	}
	public BigDecimal getR39_usd() {
		return r39_usd;
	}
	public void setR39_usd(BigDecimal r39_usd) {
		this.r39_usd = r39_usd;
	}
	public BigDecimal getR39_zar() {
		return r39_zar;
	}
	public void setR39_zar(BigDecimal r39_zar) {
		this.r39_zar = r39_zar;
	}
	public BigDecimal getR39_gbp() {
		return r39_gbp;
	}
	public void setR39_gbp(BigDecimal r39_gbp) {
		this.r39_gbp = r39_gbp;
	}
	public BigDecimal getR39_euro() {
		return r39_euro;
	}
	public void setR39_euro(BigDecimal r39_euro) {
		this.r39_euro = r39_euro;
	}
	public BigDecimal getR39_othercurrencies_inr() {
		return r39_othercurrencies_inr;
	}
	public void setR39_othercurrencies_inr(BigDecimal r39_othercurrencies_inr) {
		this.r39_othercurrencies_inr = r39_othercurrencies_inr;
	}
	public BigDecimal getR39_othercurrencies_aud() {
		return r39_othercurrencies_aud;
	}
	public void setR39_othercurrencies_aud(BigDecimal r39_othercurrencies_aud) {
		this.r39_othercurrencies_aud = r39_othercurrencies_aud;
	}
	public BigDecimal getR39_othercurrencies_i() {
		return r39_othercurrencies_i;
	}
	public void setR39_othercurrencies_i(BigDecimal r39_othercurrencies_i) {
		this.r39_othercurrencies_i = r39_othercurrencies_i;
	}
	public BigDecimal getR39_othercurrencies_j() {
		return r39_othercurrencies_j;
	}
	public void setR39_othercurrencies_j(BigDecimal r39_othercurrencies_j) {
		this.r39_othercurrencies_j = r39_othercurrencies_j;
	}
	public BigDecimal getR39_othercurrencies_k() {
		return r39_othercurrencies_k;
	}
	public void setR39_othercurrencies_k(BigDecimal r39_othercurrencies_k) {
		this.r39_othercurrencies_k = r39_othercurrencies_k;
	}
	public BigDecimal getR39_othercurrencies_l() {
		return r39_othercurrencies_l;
	}
	public void setR39_othercurrencies_l(BigDecimal r39_othercurrencies_l) {
		this.r39_othercurrencies_l = r39_othercurrencies_l;
	}
	public BigDecimal getR39_othercurrencies_m() {
		return r39_othercurrencies_m;
	}
	public void setR39_othercurrencies_m(BigDecimal r39_othercurrencies_m) {
		this.r39_othercurrencies_m = r39_othercurrencies_m;
	}
	public BigDecimal getR39_othercurrencies_n() {
		return r39_othercurrencies_n;
	}
	public void setR39_othercurrencies_n(BigDecimal r39_othercurrencies_n) {
		this.r39_othercurrencies_n = r39_othercurrencies_n;
	}
	public BigDecimal getR39_othercurrencies_0() {
		return r39_othercurrencies_0;
	}
	public void setR39_othercurrencies_0(BigDecimal r39_othercurrencies_0) {
		this.r39_othercurrencies_0 = r39_othercurrencies_0;
	}
	public BigDecimal getR39_othercurrencies_p() {
		return r39_othercurrencies_p;
	}
	public void setR39_othercurrencies_p(BigDecimal r39_othercurrencies_p) {
		this.r39_othercurrencies_p = r39_othercurrencies_p;
	}
	public BigDecimal getR39_othercurrencies_q() {
		return r39_othercurrencies_q;
	}
	public void setR39_othercurrencies_q(BigDecimal r39_othercurrencies_q) {
		this.r39_othercurrencies_q = r39_othercurrencies_q;
	}
	public BigDecimal getR39_othercurrencies_r() {
		return r39_othercurrencies_r;
	}
	public void setR39_othercurrencies_r(BigDecimal r39_othercurrencies_r) {
		this.r39_othercurrencies_r = r39_othercurrencies_r;
	}
	public BigDecimal getR39_othercurrencies_s() {
		return r39_othercurrencies_s;
	}
	public void setR39_othercurrencies_s(BigDecimal r39_othercurrencies_s) {
		this.r39_othercurrencies_s = r39_othercurrencies_s;
	}
	public BigDecimal getR39_othercurrencies_t() {
		return r39_othercurrencies_t;
	}
	public void setR39_othercurrencies_t(BigDecimal r39_othercurrencies_t) {
		this.r39_othercurrencies_t = r39_othercurrencies_t;
	}
	public BigDecimal getR39_othercurrencies_u() {
		return r39_othercurrencies_u;
	}
	public void setR39_othercurrencies_u(BigDecimal r39_othercurrencies_u) {
		this.r39_othercurrencies_u = r39_othercurrencies_u;
	}
	public BigDecimal getR39_othercurrencies_v() {
		return r39_othercurrencies_v;
	}
	public void setR39_othercurrencies_v(BigDecimal r39_othercurrencies_v) {
		this.r39_othercurrencies_v = r39_othercurrencies_v;
	}
	public BigDecimal getR39_othercurrencies_w() {
		return r39_othercurrencies_w;
	}
	public void setR39_othercurrencies_w(BigDecimal r39_othercurrencies_w) {
		this.r39_othercurrencies_w = r39_othercurrencies_w;
	}
	public String getR40_product() {
		return r40_product;
	}
	public void setR40_product(String r40_product) {
		this.r40_product = r40_product;
	}
	public BigDecimal getR40_pula() {
		return r40_pula;
	}
	public void setR40_pula(BigDecimal r40_pula) {
		this.r40_pula = r40_pula;
	}
	public BigDecimal getR40_usd() {
		return r40_usd;
	}
	public void setR40_usd(BigDecimal r40_usd) {
		this.r40_usd = r40_usd;
	}
	public BigDecimal getR40_zar() {
		return r40_zar;
	}
	public void setR40_zar(BigDecimal r40_zar) {
		this.r40_zar = r40_zar;
	}
	public BigDecimal getR40_gbp() {
		return r40_gbp;
	}
	public void setR40_gbp(BigDecimal r40_gbp) {
		this.r40_gbp = r40_gbp;
	}
	public BigDecimal getR40_euro() {
		return r40_euro;
	}
	public void setR40_euro(BigDecimal r40_euro) {
		this.r40_euro = r40_euro;
	}
	public BigDecimal getR40_othercurrencies_inr() {
		return r40_othercurrencies_inr;
	}
	public void setR40_othercurrencies_inr(BigDecimal r40_othercurrencies_inr) {
		this.r40_othercurrencies_inr = r40_othercurrencies_inr;
	}
	public BigDecimal getR40_othercurrencies_aud() {
		return r40_othercurrencies_aud;
	}
	public void setR40_othercurrencies_aud(BigDecimal r40_othercurrencies_aud) {
		this.r40_othercurrencies_aud = r40_othercurrencies_aud;
	}
	public BigDecimal getR40_othercurrencies_i() {
		return r40_othercurrencies_i;
	}
	public void setR40_othercurrencies_i(BigDecimal r40_othercurrencies_i) {
		this.r40_othercurrencies_i = r40_othercurrencies_i;
	}
	public BigDecimal getR40_othercurrencies_j() {
		return r40_othercurrencies_j;
	}
	public void setR40_othercurrencies_j(BigDecimal r40_othercurrencies_j) {
		this.r40_othercurrencies_j = r40_othercurrencies_j;
	}
	public BigDecimal getR40_othercurrencies_k() {
		return r40_othercurrencies_k;
	}
	public void setR40_othercurrencies_k(BigDecimal r40_othercurrencies_k) {
		this.r40_othercurrencies_k = r40_othercurrencies_k;
	}
	public BigDecimal getR40_othercurrencies_l() {
		return r40_othercurrencies_l;
	}
	public void setR40_othercurrencies_l(BigDecimal r40_othercurrencies_l) {
		this.r40_othercurrencies_l = r40_othercurrencies_l;
	}
	public BigDecimal getR40_othercurrencies_m() {
		return r40_othercurrencies_m;
	}
	public void setR40_othercurrencies_m(BigDecimal r40_othercurrencies_m) {
		this.r40_othercurrencies_m = r40_othercurrencies_m;
	}
	public BigDecimal getR40_othercurrencies_n() {
		return r40_othercurrencies_n;
	}
	public void setR40_othercurrencies_n(BigDecimal r40_othercurrencies_n) {
		this.r40_othercurrencies_n = r40_othercurrencies_n;
	}
	public BigDecimal getR40_othercurrencies_0() {
		return r40_othercurrencies_0;
	}
	public void setR40_othercurrencies_0(BigDecimal r40_othercurrencies_0) {
		this.r40_othercurrencies_0 = r40_othercurrencies_0;
	}
	public BigDecimal getR40_othercurrencies_p() {
		return r40_othercurrencies_p;
	}
	public void setR40_othercurrencies_p(BigDecimal r40_othercurrencies_p) {
		this.r40_othercurrencies_p = r40_othercurrencies_p;
	}
	public BigDecimal getR40_othercurrencies_q() {
		return r40_othercurrencies_q;
	}
	public void setR40_othercurrencies_q(BigDecimal r40_othercurrencies_q) {
		this.r40_othercurrencies_q = r40_othercurrencies_q;
	}
	public BigDecimal getR40_othercurrencies_r() {
		return r40_othercurrencies_r;
	}
	public void setR40_othercurrencies_r(BigDecimal r40_othercurrencies_r) {
		this.r40_othercurrencies_r = r40_othercurrencies_r;
	}
	public BigDecimal getR40_othercurrencies_s() {
		return r40_othercurrencies_s;
	}
	public void setR40_othercurrencies_s(BigDecimal r40_othercurrencies_s) {
		this.r40_othercurrencies_s = r40_othercurrencies_s;
	}
	public BigDecimal getR40_othercurrencies_t() {
		return r40_othercurrencies_t;
	}
	public void setR40_othercurrencies_t(BigDecimal r40_othercurrencies_t) {
		this.r40_othercurrencies_t = r40_othercurrencies_t;
	}
	public BigDecimal getR40_othercurrencies_u() {
		return r40_othercurrencies_u;
	}
	public void setR40_othercurrencies_u(BigDecimal r40_othercurrencies_u) {
		this.r40_othercurrencies_u = r40_othercurrencies_u;
	}
	public BigDecimal getR40_othercurrencies_v() {
		return r40_othercurrencies_v;
	}
	public void setR40_othercurrencies_v(BigDecimal r40_othercurrencies_v) {
		this.r40_othercurrencies_v = r40_othercurrencies_v;
	}
	public BigDecimal getR40_othercurrencies_w() {
		return r40_othercurrencies_w;
	}
	public void setR40_othercurrencies_w(BigDecimal r40_othercurrencies_w) {
		this.r40_othercurrencies_w = r40_othercurrencies_w;
	}
	public String getR41_product() {
		return r41_product;
	}
	public void setR41_product(String r41_product) {
		this.r41_product = r41_product;
	}
	public BigDecimal getR41_pula() {
		return r41_pula;
	}
	public void setR41_pula(BigDecimal r41_pula) {
		this.r41_pula = r41_pula;
	}
	public BigDecimal getR41_usd() {
		return r41_usd;
	}
	public void setR41_usd(BigDecimal r41_usd) {
		this.r41_usd = r41_usd;
	}
	public BigDecimal getR41_zar() {
		return r41_zar;
	}
	public void setR41_zar(BigDecimal r41_zar) {
		this.r41_zar = r41_zar;
	}
	public BigDecimal getR41_gbp() {
		return r41_gbp;
	}
	public void setR41_gbp(BigDecimal r41_gbp) {
		this.r41_gbp = r41_gbp;
	}
	public BigDecimal getR41_euro() {
		return r41_euro;
	}
	public void setR41_euro(BigDecimal r41_euro) {
		this.r41_euro = r41_euro;
	}
	public BigDecimal getR41_othercurrencies_inr() {
		return r41_othercurrencies_inr;
	}
	public void setR41_othercurrencies_inr(BigDecimal r41_othercurrencies_inr) {
		this.r41_othercurrencies_inr = r41_othercurrencies_inr;
	}
	public BigDecimal getR41_othercurrencies_aud() {
		return r41_othercurrencies_aud;
	}
	public void setR41_othercurrencies_aud(BigDecimal r41_othercurrencies_aud) {
		this.r41_othercurrencies_aud = r41_othercurrencies_aud;
	}
	public BigDecimal getR41_othercurrencies_i() {
		return r41_othercurrencies_i;
	}
	public void setR41_othercurrencies_i(BigDecimal r41_othercurrencies_i) {
		this.r41_othercurrencies_i = r41_othercurrencies_i;
	}
	public BigDecimal getR41_othercurrencies_j() {
		return r41_othercurrencies_j;
	}
	public void setR41_othercurrencies_j(BigDecimal r41_othercurrencies_j) {
		this.r41_othercurrencies_j = r41_othercurrencies_j;
	}
	public BigDecimal getR41_othercurrencies_k() {
		return r41_othercurrencies_k;
	}
	public void setR41_othercurrencies_k(BigDecimal r41_othercurrencies_k) {
		this.r41_othercurrencies_k = r41_othercurrencies_k;
	}
	public BigDecimal getR41_othercurrencies_l() {
		return r41_othercurrencies_l;
	}
	public void setR41_othercurrencies_l(BigDecimal r41_othercurrencies_l) {
		this.r41_othercurrencies_l = r41_othercurrencies_l;
	}
	public BigDecimal getR41_othercurrencies_m() {
		return r41_othercurrencies_m;
	}
	public void setR41_othercurrencies_m(BigDecimal r41_othercurrencies_m) {
		this.r41_othercurrencies_m = r41_othercurrencies_m;
	}
	public BigDecimal getR41_othercurrencies_n() {
		return r41_othercurrencies_n;
	}
	public void setR41_othercurrencies_n(BigDecimal r41_othercurrencies_n) {
		this.r41_othercurrencies_n = r41_othercurrencies_n;
	}
	public BigDecimal getR41_othercurrencies_0() {
		return r41_othercurrencies_0;
	}
	public void setR41_othercurrencies_0(BigDecimal r41_othercurrencies_0) {
		this.r41_othercurrencies_0 = r41_othercurrencies_0;
	}
	public BigDecimal getR41_othercurrencies_p() {
		return r41_othercurrencies_p;
	}
	public void setR41_othercurrencies_p(BigDecimal r41_othercurrencies_p) {
		this.r41_othercurrencies_p = r41_othercurrencies_p;
	}
	public BigDecimal getR41_othercurrencies_q() {
		return r41_othercurrencies_q;
	}
	public void setR41_othercurrencies_q(BigDecimal r41_othercurrencies_q) {
		this.r41_othercurrencies_q = r41_othercurrencies_q;
	}
	public BigDecimal getR41_othercurrencies_r() {
		return r41_othercurrencies_r;
	}
	public void setR41_othercurrencies_r(BigDecimal r41_othercurrencies_r) {
		this.r41_othercurrencies_r = r41_othercurrencies_r;
	}
	public BigDecimal getR41_othercurrencies_s() {
		return r41_othercurrencies_s;
	}
	public void setR41_othercurrencies_s(BigDecimal r41_othercurrencies_s) {
		this.r41_othercurrencies_s = r41_othercurrencies_s;
	}
	public BigDecimal getR41_othercurrencies_t() {
		return r41_othercurrencies_t;
	}
	public void setR41_othercurrencies_t(BigDecimal r41_othercurrencies_t) {
		this.r41_othercurrencies_t = r41_othercurrencies_t;
	}
	public BigDecimal getR41_othercurrencies_u() {
		return r41_othercurrencies_u;
	}
	public void setR41_othercurrencies_u(BigDecimal r41_othercurrencies_u) {
		this.r41_othercurrencies_u = r41_othercurrencies_u;
	}
	public BigDecimal getR41_othercurrencies_v() {
		return r41_othercurrencies_v;
	}
	public void setR41_othercurrencies_v(BigDecimal r41_othercurrencies_v) {
		this.r41_othercurrencies_v = r41_othercurrencies_v;
	}
	public BigDecimal getR41_othercurrencies_w() {
		return r41_othercurrencies_w;
	}
	public void setR41_othercurrencies_w(BigDecimal r41_othercurrencies_w) {
		this.r41_othercurrencies_w = r41_othercurrencies_w;
	}
	public String getR42_product() {
		return r42_product;
	}
	public void setR42_product(String r42_product) {
		this.r42_product = r42_product;
	}
	public BigDecimal getR42_pula() {
		return r42_pula;
	}
	public void setR42_pula(BigDecimal r42_pula) {
		this.r42_pula = r42_pula;
	}
	public BigDecimal getR42_usd() {
		return r42_usd;
	}
	public void setR42_usd(BigDecimal r42_usd) {
		this.r42_usd = r42_usd;
	}
	public BigDecimal getR42_zar() {
		return r42_zar;
	}
	public void setR42_zar(BigDecimal r42_zar) {
		this.r42_zar = r42_zar;
	}
	public BigDecimal getR42_gbp() {
		return r42_gbp;
	}
	public void setR42_gbp(BigDecimal r42_gbp) {
		this.r42_gbp = r42_gbp;
	}
	public BigDecimal getR42_euro() {
		return r42_euro;
	}
	public void setR42_euro(BigDecimal r42_euro) {
		this.r42_euro = r42_euro;
	}
	public BigDecimal getR42_othercurrencies_inr() {
		return r42_othercurrencies_inr;
	}
	public void setR42_othercurrencies_inr(BigDecimal r42_othercurrencies_inr) {
		this.r42_othercurrencies_inr = r42_othercurrencies_inr;
	}
	public BigDecimal getR42_othercurrencies_aud() {
		return r42_othercurrencies_aud;
	}
	public void setR42_othercurrencies_aud(BigDecimal r42_othercurrencies_aud) {
		this.r42_othercurrencies_aud = r42_othercurrencies_aud;
	}
	public BigDecimal getR42_othercurrencies_i() {
		return r42_othercurrencies_i;
	}
	public void setR42_othercurrencies_i(BigDecimal r42_othercurrencies_i) {
		this.r42_othercurrencies_i = r42_othercurrencies_i;
	}
	public BigDecimal getR42_othercurrencies_j() {
		return r42_othercurrencies_j;
	}
	public void setR42_othercurrencies_j(BigDecimal r42_othercurrencies_j) {
		this.r42_othercurrencies_j = r42_othercurrencies_j;
	}
	public BigDecimal getR42_othercurrencies_k() {
		return r42_othercurrencies_k;
	}
	public void setR42_othercurrencies_k(BigDecimal r42_othercurrencies_k) {
		this.r42_othercurrencies_k = r42_othercurrencies_k;
	}
	public BigDecimal getR42_othercurrencies_l() {
		return r42_othercurrencies_l;
	}
	public void setR42_othercurrencies_l(BigDecimal r42_othercurrencies_l) {
		this.r42_othercurrencies_l = r42_othercurrencies_l;
	}
	public BigDecimal getR42_othercurrencies_m() {
		return r42_othercurrencies_m;
	}
	public void setR42_othercurrencies_m(BigDecimal r42_othercurrencies_m) {
		this.r42_othercurrencies_m = r42_othercurrencies_m;
	}
	public BigDecimal getR42_othercurrencies_n() {
		return r42_othercurrencies_n;
	}
	public void setR42_othercurrencies_n(BigDecimal r42_othercurrencies_n) {
		this.r42_othercurrencies_n = r42_othercurrencies_n;
	}
	public BigDecimal getR42_othercurrencies_0() {
		return r42_othercurrencies_0;
	}
	public void setR42_othercurrencies_0(BigDecimal r42_othercurrencies_0) {
		this.r42_othercurrencies_0 = r42_othercurrencies_0;
	}
	public BigDecimal getR42_othercurrencies_p() {
		return r42_othercurrencies_p;
	}
	public void setR42_othercurrencies_p(BigDecimal r42_othercurrencies_p) {
		this.r42_othercurrencies_p = r42_othercurrencies_p;
	}
	public BigDecimal getR42_othercurrencies_q() {
		return r42_othercurrencies_q;
	}
	public void setR42_othercurrencies_q(BigDecimal r42_othercurrencies_q) {
		this.r42_othercurrencies_q = r42_othercurrencies_q;
	}
	public BigDecimal getR42_othercurrencies_r() {
		return r42_othercurrencies_r;
	}
	public void setR42_othercurrencies_r(BigDecimal r42_othercurrencies_r) {
		this.r42_othercurrencies_r = r42_othercurrencies_r;
	}
	public BigDecimal getR42_othercurrencies_s() {
		return r42_othercurrencies_s;
	}
	public void setR42_othercurrencies_s(BigDecimal r42_othercurrencies_s) {
		this.r42_othercurrencies_s = r42_othercurrencies_s;
	}
	public BigDecimal getR42_othercurrencies_t() {
		return r42_othercurrencies_t;
	}
	public void setR42_othercurrencies_t(BigDecimal r42_othercurrencies_t) {
		this.r42_othercurrencies_t = r42_othercurrencies_t;
	}
	public BigDecimal getR42_othercurrencies_u() {
		return r42_othercurrencies_u;
	}
	public void setR42_othercurrencies_u(BigDecimal r42_othercurrencies_u) {
		this.r42_othercurrencies_u = r42_othercurrencies_u;
	}
	public BigDecimal getR42_othercurrencies_v() {
		return r42_othercurrencies_v;
	}
	public void setR42_othercurrencies_v(BigDecimal r42_othercurrencies_v) {
		this.r42_othercurrencies_v = r42_othercurrencies_v;
	}
	public BigDecimal getR42_othercurrencies_w() {
		return r42_othercurrencies_w;
	}
	public void setR42_othercurrencies_w(BigDecimal r42_othercurrencies_w) {
		this.r42_othercurrencies_w = r42_othercurrencies_w;
	}
	public String getR43_product() {
		return r43_product;
	}
	public void setR43_product(String r43_product) {
		this.r43_product = r43_product;
	}
	public BigDecimal getR43_pula() {
		return r43_pula;
	}
	public void setR43_pula(BigDecimal r43_pula) {
		this.r43_pula = r43_pula;
	}
	public BigDecimal getR43_usd() {
		return r43_usd;
	}
	public void setR43_usd(BigDecimal r43_usd) {
		this.r43_usd = r43_usd;
	}
	public BigDecimal getR43_zar() {
		return r43_zar;
	}
	public void setR43_zar(BigDecimal r43_zar) {
		this.r43_zar = r43_zar;
	}
	public BigDecimal getR43_gbp() {
		return r43_gbp;
	}
	public void setR43_gbp(BigDecimal r43_gbp) {
		this.r43_gbp = r43_gbp;
	}
	public BigDecimal getR43_euro() {
		return r43_euro;
	}
	public void setR43_euro(BigDecimal r43_euro) {
		this.r43_euro = r43_euro;
	}
	public BigDecimal getR43_othercurrencies_inr() {
		return r43_othercurrencies_inr;
	}
	public void setR43_othercurrencies_inr(BigDecimal r43_othercurrencies_inr) {
		this.r43_othercurrencies_inr = r43_othercurrencies_inr;
	}
	public BigDecimal getR43_othercurrencies_aud() {
		return r43_othercurrencies_aud;
	}
	public void setR43_othercurrencies_aud(BigDecimal r43_othercurrencies_aud) {
		this.r43_othercurrencies_aud = r43_othercurrencies_aud;
	}
	public BigDecimal getR43_othercurrencies_i() {
		return r43_othercurrencies_i;
	}
	public void setR43_othercurrencies_i(BigDecimal r43_othercurrencies_i) {
		this.r43_othercurrencies_i = r43_othercurrencies_i;
	}
	public BigDecimal getR43_othercurrencies_j() {
		return r43_othercurrencies_j;
	}
	public void setR43_othercurrencies_j(BigDecimal r43_othercurrencies_j) {
		this.r43_othercurrencies_j = r43_othercurrencies_j;
	}
	public BigDecimal getR43_othercurrencies_k() {
		return r43_othercurrencies_k;
	}
	public void setR43_othercurrencies_k(BigDecimal r43_othercurrencies_k) {
		this.r43_othercurrencies_k = r43_othercurrencies_k;
	}
	public BigDecimal getR43_othercurrencies_l() {
		return r43_othercurrencies_l;
	}
	public void setR43_othercurrencies_l(BigDecimal r43_othercurrencies_l) {
		this.r43_othercurrencies_l = r43_othercurrencies_l;
	}
	public BigDecimal getR43_othercurrencies_m() {
		return r43_othercurrencies_m;
	}
	public void setR43_othercurrencies_m(BigDecimal r43_othercurrencies_m) {
		this.r43_othercurrencies_m = r43_othercurrencies_m;
	}
	public BigDecimal getR43_othercurrencies_n() {
		return r43_othercurrencies_n;
	}
	public void setR43_othercurrencies_n(BigDecimal r43_othercurrencies_n) {
		this.r43_othercurrencies_n = r43_othercurrencies_n;
	}
	public BigDecimal getR43_othercurrencies_0() {
		return r43_othercurrencies_0;
	}
	public void setR43_othercurrencies_0(BigDecimal r43_othercurrencies_0) {
		this.r43_othercurrencies_0 = r43_othercurrencies_0;
	}
	public BigDecimal getR43_othercurrencies_p() {
		return r43_othercurrencies_p;
	}
	public void setR43_othercurrencies_p(BigDecimal r43_othercurrencies_p) {
		this.r43_othercurrencies_p = r43_othercurrencies_p;
	}
	public BigDecimal getR43_othercurrencies_q() {
		return r43_othercurrencies_q;
	}
	public void setR43_othercurrencies_q(BigDecimal r43_othercurrencies_q) {
		this.r43_othercurrencies_q = r43_othercurrencies_q;
	}
	public BigDecimal getR43_othercurrencies_r() {
		return r43_othercurrencies_r;
	}
	public void setR43_othercurrencies_r(BigDecimal r43_othercurrencies_r) {
		this.r43_othercurrencies_r = r43_othercurrencies_r;
	}
	public BigDecimal getR43_othercurrencies_s() {
		return r43_othercurrencies_s;
	}
	public void setR43_othercurrencies_s(BigDecimal r43_othercurrencies_s) {
		this.r43_othercurrencies_s = r43_othercurrencies_s;
	}
	public BigDecimal getR43_othercurrencies_t() {
		return r43_othercurrencies_t;
	}
	public void setR43_othercurrencies_t(BigDecimal r43_othercurrencies_t) {
		this.r43_othercurrencies_t = r43_othercurrencies_t;
	}
	public BigDecimal getR43_othercurrencies_u() {
		return r43_othercurrencies_u;
	}
	public void setR43_othercurrencies_u(BigDecimal r43_othercurrencies_u) {
		this.r43_othercurrencies_u = r43_othercurrencies_u;
	}
	public BigDecimal getR43_othercurrencies_v() {
		return r43_othercurrencies_v;
	}
	public void setR43_othercurrencies_v(BigDecimal r43_othercurrencies_v) {
		this.r43_othercurrencies_v = r43_othercurrencies_v;
	}
	public BigDecimal getR43_othercurrencies_w() {
		return r43_othercurrencies_w;
	}
	public void setR43_othercurrencies_w(BigDecimal r43_othercurrencies_w) {
		this.r43_othercurrencies_w = r43_othercurrencies_w;
	}
	public String getR44_product() {
		return r44_product;
	}
	public void setR44_product(String r44_product) {
		this.r44_product = r44_product;
	}
	public BigDecimal getR44_pula() {
		return r44_pula;
	}
	public void setR44_pula(BigDecimal r44_pula) {
		this.r44_pula = r44_pula;
	}
	public BigDecimal getR44_usd() {
		return r44_usd;
	}
	public void setR44_usd(BigDecimal r44_usd) {
		this.r44_usd = r44_usd;
	}
	public BigDecimal getR44_zar() {
		return r44_zar;
	}
	public void setR44_zar(BigDecimal r44_zar) {
		this.r44_zar = r44_zar;
	}
	public BigDecimal getR44_gbp() {
		return r44_gbp;
	}
	public void setR44_gbp(BigDecimal r44_gbp) {
		this.r44_gbp = r44_gbp;
	}
	public BigDecimal getR44_euro() {
		return r44_euro;
	}
	public void setR44_euro(BigDecimal r44_euro) {
		this.r44_euro = r44_euro;
	}
	public BigDecimal getR44_othercurrencies_inr() {
		return r44_othercurrencies_inr;
	}
	public void setR44_othercurrencies_inr(BigDecimal r44_othercurrencies_inr) {
		this.r44_othercurrencies_inr = r44_othercurrencies_inr;
	}
	public BigDecimal getR44_othercurrencies_aud() {
		return r44_othercurrencies_aud;
	}
	public void setR44_othercurrencies_aud(BigDecimal r44_othercurrencies_aud) {
		this.r44_othercurrencies_aud = r44_othercurrencies_aud;
	}
	public BigDecimal getR44_othercurrencies_i() {
		return r44_othercurrencies_i;
	}
	public void setR44_othercurrencies_i(BigDecimal r44_othercurrencies_i) {
		this.r44_othercurrencies_i = r44_othercurrencies_i;
	}
	public BigDecimal getR44_othercurrencies_j() {
		return r44_othercurrencies_j;
	}
	public void setR44_othercurrencies_j(BigDecimal r44_othercurrencies_j) {
		this.r44_othercurrencies_j = r44_othercurrencies_j;
	}
	public BigDecimal getR44_othercurrencies_k() {
		return r44_othercurrencies_k;
	}
	public void setR44_othercurrencies_k(BigDecimal r44_othercurrencies_k) {
		this.r44_othercurrencies_k = r44_othercurrencies_k;
	}
	public BigDecimal getR44_othercurrencies_l() {
		return r44_othercurrencies_l;
	}
	public void setR44_othercurrencies_l(BigDecimal r44_othercurrencies_l) {
		this.r44_othercurrencies_l = r44_othercurrencies_l;
	}
	public BigDecimal getR44_othercurrencies_m() {
		return r44_othercurrencies_m;
	}
	public void setR44_othercurrencies_m(BigDecimal r44_othercurrencies_m) {
		this.r44_othercurrencies_m = r44_othercurrencies_m;
	}
	public BigDecimal getR44_othercurrencies_n() {
		return r44_othercurrencies_n;
	}
	public void setR44_othercurrencies_n(BigDecimal r44_othercurrencies_n) {
		this.r44_othercurrencies_n = r44_othercurrencies_n;
	}
	public BigDecimal getR44_othercurrencies_0() {
		return r44_othercurrencies_0;
	}
	public void setR44_othercurrencies_0(BigDecimal r44_othercurrencies_0) {
		this.r44_othercurrencies_0 = r44_othercurrencies_0;
	}
	public BigDecimal getR44_othercurrencies_p() {
		return r44_othercurrencies_p;
	}
	public void setR44_othercurrencies_p(BigDecimal r44_othercurrencies_p) {
		this.r44_othercurrencies_p = r44_othercurrencies_p;
	}
	public BigDecimal getR44_othercurrencies_q() {
		return r44_othercurrencies_q;
	}
	public void setR44_othercurrencies_q(BigDecimal r44_othercurrencies_q) {
		this.r44_othercurrencies_q = r44_othercurrencies_q;
	}
	public BigDecimal getR44_othercurrencies_r() {
		return r44_othercurrencies_r;
	}
	public void setR44_othercurrencies_r(BigDecimal r44_othercurrencies_r) {
		this.r44_othercurrencies_r = r44_othercurrencies_r;
	}
	public BigDecimal getR44_othercurrencies_s() {
		return r44_othercurrencies_s;
	}
	public void setR44_othercurrencies_s(BigDecimal r44_othercurrencies_s) {
		this.r44_othercurrencies_s = r44_othercurrencies_s;
	}
	public BigDecimal getR44_othercurrencies_t() {
		return r44_othercurrencies_t;
	}
	public void setR44_othercurrencies_t(BigDecimal r44_othercurrencies_t) {
		this.r44_othercurrencies_t = r44_othercurrencies_t;
	}
	public BigDecimal getR44_othercurrencies_u() {
		return r44_othercurrencies_u;
	}
	public void setR44_othercurrencies_u(BigDecimal r44_othercurrencies_u) {
		this.r44_othercurrencies_u = r44_othercurrencies_u;
	}
	public BigDecimal getR44_othercurrencies_v() {
		return r44_othercurrencies_v;
	}
	public void setR44_othercurrencies_v(BigDecimal r44_othercurrencies_v) {
		this.r44_othercurrencies_v = r44_othercurrencies_v;
	}
	public BigDecimal getR44_othercurrencies_w() {
		return r44_othercurrencies_w;
	}
	public void setR44_othercurrencies_w(BigDecimal r44_othercurrencies_w) {
		this.r44_othercurrencies_w = r44_othercurrencies_w;
	}
	public String getR45_product() {
		return r45_product;
	}
	public void setR45_product(String r45_product) {
		this.r45_product = r45_product;
	}
	public BigDecimal getR45_pula() {
		return r45_pula;
	}
	public void setR45_pula(BigDecimal r45_pula) {
		this.r45_pula = r45_pula;
	}
	public BigDecimal getR45_usd() {
		return r45_usd;
	}
	public void setR45_usd(BigDecimal r45_usd) {
		this.r45_usd = r45_usd;
	}
	public BigDecimal getR45_zar() {
		return r45_zar;
	}
	public void setR45_zar(BigDecimal r45_zar) {
		this.r45_zar = r45_zar;
	}
	public BigDecimal getR45_gbp() {
		return r45_gbp;
	}
	public void setR45_gbp(BigDecimal r45_gbp) {
		this.r45_gbp = r45_gbp;
	}
	public BigDecimal getR45_euro() {
		return r45_euro;
	}
	public void setR45_euro(BigDecimal r45_euro) {
		this.r45_euro = r45_euro;
	}
	public BigDecimal getR45_othercurrencies_inr() {
		return r45_othercurrencies_inr;
	}
	public void setR45_othercurrencies_inr(BigDecimal r45_othercurrencies_inr) {
		this.r45_othercurrencies_inr = r45_othercurrencies_inr;
	}
	public BigDecimal getR45_othercurrencies_aud() {
		return r45_othercurrencies_aud;
	}
	public void setR45_othercurrencies_aud(BigDecimal r45_othercurrencies_aud) {
		this.r45_othercurrencies_aud = r45_othercurrencies_aud;
	}
	public BigDecimal getR45_othercurrencies_i() {
		return r45_othercurrencies_i;
	}
	public void setR45_othercurrencies_i(BigDecimal r45_othercurrencies_i) {
		this.r45_othercurrencies_i = r45_othercurrencies_i;
	}
	public BigDecimal getR45_othercurrencies_j() {
		return r45_othercurrencies_j;
	}
	public void setR45_othercurrencies_j(BigDecimal r45_othercurrencies_j) {
		this.r45_othercurrencies_j = r45_othercurrencies_j;
	}
	public BigDecimal getR45_othercurrencies_k() {
		return r45_othercurrencies_k;
	}
	public void setR45_othercurrencies_k(BigDecimal r45_othercurrencies_k) {
		this.r45_othercurrencies_k = r45_othercurrencies_k;
	}
	public BigDecimal getR45_othercurrencies_l() {
		return r45_othercurrencies_l;
	}
	public void setR45_othercurrencies_l(BigDecimal r45_othercurrencies_l) {
		this.r45_othercurrencies_l = r45_othercurrencies_l;
	}
	public BigDecimal getR45_othercurrencies_m() {
		return r45_othercurrencies_m;
	}
	public void setR45_othercurrencies_m(BigDecimal r45_othercurrencies_m) {
		this.r45_othercurrencies_m = r45_othercurrencies_m;
	}
	public BigDecimal getR45_othercurrencies_n() {
		return r45_othercurrencies_n;
	}
	public void setR45_othercurrencies_n(BigDecimal r45_othercurrencies_n) {
		this.r45_othercurrencies_n = r45_othercurrencies_n;
	}
	public BigDecimal getR45_othercurrencies_0() {
		return r45_othercurrencies_0;
	}
	public void setR45_othercurrencies_0(BigDecimal r45_othercurrencies_0) {
		this.r45_othercurrencies_0 = r45_othercurrencies_0;
	}
	public BigDecimal getR45_othercurrencies_p() {
		return r45_othercurrencies_p;
	}
	public void setR45_othercurrencies_p(BigDecimal r45_othercurrencies_p) {
		this.r45_othercurrencies_p = r45_othercurrencies_p;
	}
	public BigDecimal getR45_othercurrencies_q() {
		return r45_othercurrencies_q;
	}
	public void setR45_othercurrencies_q(BigDecimal r45_othercurrencies_q) {
		this.r45_othercurrencies_q = r45_othercurrencies_q;
	}
	public BigDecimal getR45_othercurrencies_r() {
		return r45_othercurrencies_r;
	}
	public void setR45_othercurrencies_r(BigDecimal r45_othercurrencies_r) {
		this.r45_othercurrencies_r = r45_othercurrencies_r;
	}
	public BigDecimal getR45_othercurrencies_s() {
		return r45_othercurrencies_s;
	}
	public void setR45_othercurrencies_s(BigDecimal r45_othercurrencies_s) {
		this.r45_othercurrencies_s = r45_othercurrencies_s;
	}
	public BigDecimal getR45_othercurrencies_t() {
		return r45_othercurrencies_t;
	}
	public void setR45_othercurrencies_t(BigDecimal r45_othercurrencies_t) {
		this.r45_othercurrencies_t = r45_othercurrencies_t;
	}
	public BigDecimal getR45_othercurrencies_u() {
		return r45_othercurrencies_u;
	}
	public void setR45_othercurrencies_u(BigDecimal r45_othercurrencies_u) {
		this.r45_othercurrencies_u = r45_othercurrencies_u;
	}
	public BigDecimal getR45_othercurrencies_v() {
		return r45_othercurrencies_v;
	}
	public void setR45_othercurrencies_v(BigDecimal r45_othercurrencies_v) {
		this.r45_othercurrencies_v = r45_othercurrencies_v;
	}
	public BigDecimal getR45_othercurrencies_w() {
		return r45_othercurrencies_w;
	}
	public void setR45_othercurrencies_w(BigDecimal r45_othercurrencies_w) {
		this.r45_othercurrencies_w = r45_othercurrencies_w;
	}
	public String getR46_product() {
		return r46_product;
	}
	public void setR46_product(String r46_product) {
		this.r46_product = r46_product;
	}
	public BigDecimal getR46_pula() {
		return r46_pula;
	}
	public void setR46_pula(BigDecimal r46_pula) {
		this.r46_pula = r46_pula;
	}
	public BigDecimal getR46_usd() {
		return r46_usd;
	}
	public void setR46_usd(BigDecimal r46_usd) {
		this.r46_usd = r46_usd;
	}
	public BigDecimal getR46_zar() {
		return r46_zar;
	}
	public void setR46_zar(BigDecimal r46_zar) {
		this.r46_zar = r46_zar;
	}
	public BigDecimal getR46_gbp() {
		return r46_gbp;
	}
	public void setR46_gbp(BigDecimal r46_gbp) {
		this.r46_gbp = r46_gbp;
	}
	public BigDecimal getR46_euro() {
		return r46_euro;
	}
	public void setR46_euro(BigDecimal r46_euro) {
		this.r46_euro = r46_euro;
	}
	public BigDecimal getR46_othercurrencies_inr() {
		return r46_othercurrencies_inr;
	}
	public void setR46_othercurrencies_inr(BigDecimal r46_othercurrencies_inr) {
		this.r46_othercurrencies_inr = r46_othercurrencies_inr;
	}
	public BigDecimal getR46_othercurrencies_aud() {
		return r46_othercurrencies_aud;
	}
	public void setR46_othercurrencies_aud(BigDecimal r46_othercurrencies_aud) {
		this.r46_othercurrencies_aud = r46_othercurrencies_aud;
	}
	public BigDecimal getR46_othercurrencies_i() {
		return r46_othercurrencies_i;
	}
	public void setR46_othercurrencies_i(BigDecimal r46_othercurrencies_i) {
		this.r46_othercurrencies_i = r46_othercurrencies_i;
	}
	public BigDecimal getR46_othercurrencies_j() {
		return r46_othercurrencies_j;
	}
	public void setR46_othercurrencies_j(BigDecimal r46_othercurrencies_j) {
		this.r46_othercurrencies_j = r46_othercurrencies_j;
	}
	public BigDecimal getR46_othercurrencies_k() {
		return r46_othercurrencies_k;
	}
	public void setR46_othercurrencies_k(BigDecimal r46_othercurrencies_k) {
		this.r46_othercurrencies_k = r46_othercurrencies_k;
	}
	public BigDecimal getR46_othercurrencies_l() {
		return r46_othercurrencies_l;
	}
	public void setR46_othercurrencies_l(BigDecimal r46_othercurrencies_l) {
		this.r46_othercurrencies_l = r46_othercurrencies_l;
	}
	public BigDecimal getR46_othercurrencies_m() {
		return r46_othercurrencies_m;
	}
	public void setR46_othercurrencies_m(BigDecimal r46_othercurrencies_m) {
		this.r46_othercurrencies_m = r46_othercurrencies_m;
	}
	public BigDecimal getR46_othercurrencies_n() {
		return r46_othercurrencies_n;
	}
	public void setR46_othercurrencies_n(BigDecimal r46_othercurrencies_n) {
		this.r46_othercurrencies_n = r46_othercurrencies_n;
	}
	public BigDecimal getR46_othercurrencies_0() {
		return r46_othercurrencies_0;
	}
	public void setR46_othercurrencies_0(BigDecimal r46_othercurrencies_0) {
		this.r46_othercurrencies_0 = r46_othercurrencies_0;
	}
	public BigDecimal getR46_othercurrencies_p() {
		return r46_othercurrencies_p;
	}
	public void setR46_othercurrencies_p(BigDecimal r46_othercurrencies_p) {
		this.r46_othercurrencies_p = r46_othercurrencies_p;
	}
	public BigDecimal getR46_othercurrencies_q() {
		return r46_othercurrencies_q;
	}
	public void setR46_othercurrencies_q(BigDecimal r46_othercurrencies_q) {
		this.r46_othercurrencies_q = r46_othercurrencies_q;
	}
	public BigDecimal getR46_othercurrencies_r() {
		return r46_othercurrencies_r;
	}
	public void setR46_othercurrencies_r(BigDecimal r46_othercurrencies_r) {
		this.r46_othercurrencies_r = r46_othercurrencies_r;
	}
	public BigDecimal getR46_othercurrencies_s() {
		return r46_othercurrencies_s;
	}
	public void setR46_othercurrencies_s(BigDecimal r46_othercurrencies_s) {
		this.r46_othercurrencies_s = r46_othercurrencies_s;
	}
	public BigDecimal getR46_othercurrencies_t() {
		return r46_othercurrencies_t;
	}
	public void setR46_othercurrencies_t(BigDecimal r46_othercurrencies_t) {
		this.r46_othercurrencies_t = r46_othercurrencies_t;
	}
	public BigDecimal getR46_othercurrencies_u() {
		return r46_othercurrencies_u;
	}
	public void setR46_othercurrencies_u(BigDecimal r46_othercurrencies_u) {
		this.r46_othercurrencies_u = r46_othercurrencies_u;
	}
	public BigDecimal getR46_othercurrencies_v() {
		return r46_othercurrencies_v;
	}
	public void setR46_othercurrencies_v(BigDecimal r46_othercurrencies_v) {
		this.r46_othercurrencies_v = r46_othercurrencies_v;
	}
	public BigDecimal getR46_othercurrencies_w() {
		return r46_othercurrencies_w;
	}
	public void setR46_othercurrencies_w(BigDecimal r46_othercurrencies_w) {
		this.r46_othercurrencies_w = r46_othercurrencies_w;
	}
	public String getR47_product() {
		return r47_product;
	}
	public void setR47_product(String r47_product) {
		this.r47_product = r47_product;
	}
	public BigDecimal getR47_pula() {
		return r47_pula;
	}
	public void setR47_pula(BigDecimal r47_pula) {
		this.r47_pula = r47_pula;
	}
	public BigDecimal getR47_usd() {
		return r47_usd;
	}
	public void setR47_usd(BigDecimal r47_usd) {
		this.r47_usd = r47_usd;
	}
	public BigDecimal getR47_zar() {
		return r47_zar;
	}
	public void setR47_zar(BigDecimal r47_zar) {
		this.r47_zar = r47_zar;
	}
	public BigDecimal getR47_gbp() {
		return r47_gbp;
	}
	public void setR47_gbp(BigDecimal r47_gbp) {
		this.r47_gbp = r47_gbp;
	}
	public BigDecimal getR47_euro() {
		return r47_euro;
	}
	public void setR47_euro(BigDecimal r47_euro) {
		this.r47_euro = r47_euro;
	}
	public BigDecimal getR47_othercurrencies_inr() {
		return r47_othercurrencies_inr;
	}
	public void setR47_othercurrencies_inr(BigDecimal r47_othercurrencies_inr) {
		this.r47_othercurrencies_inr = r47_othercurrencies_inr;
	}
	public BigDecimal getR47_othercurrencies_aud() {
		return r47_othercurrencies_aud;
	}
	public void setR47_othercurrencies_aud(BigDecimal r47_othercurrencies_aud) {
		this.r47_othercurrencies_aud = r47_othercurrencies_aud;
	}
	public BigDecimal getR47_othercurrencies_i() {
		return r47_othercurrencies_i;
	}
	public void setR47_othercurrencies_i(BigDecimal r47_othercurrencies_i) {
		this.r47_othercurrencies_i = r47_othercurrencies_i;
	}
	public BigDecimal getR47_othercurrencies_j() {
		return r47_othercurrencies_j;
	}
	public void setR47_othercurrencies_j(BigDecimal r47_othercurrencies_j) {
		this.r47_othercurrencies_j = r47_othercurrencies_j;
	}
	public BigDecimal getR47_othercurrencies_k() {
		return r47_othercurrencies_k;
	}
	public void setR47_othercurrencies_k(BigDecimal r47_othercurrencies_k) {
		this.r47_othercurrencies_k = r47_othercurrencies_k;
	}
	public BigDecimal getR47_othercurrencies_l() {
		return r47_othercurrencies_l;
	}
	public void setR47_othercurrencies_l(BigDecimal r47_othercurrencies_l) {
		this.r47_othercurrencies_l = r47_othercurrencies_l;
	}
	public BigDecimal getR47_othercurrencies_m() {
		return r47_othercurrencies_m;
	}
	public void setR47_othercurrencies_m(BigDecimal r47_othercurrencies_m) {
		this.r47_othercurrencies_m = r47_othercurrencies_m;
	}
	public BigDecimal getR47_othercurrencies_n() {
		return r47_othercurrencies_n;
	}
	public void setR47_othercurrencies_n(BigDecimal r47_othercurrencies_n) {
		this.r47_othercurrencies_n = r47_othercurrencies_n;
	}
	public BigDecimal getR47_othercurrencies_0() {
		return r47_othercurrencies_0;
	}
	public void setR47_othercurrencies_0(BigDecimal r47_othercurrencies_0) {
		this.r47_othercurrencies_0 = r47_othercurrencies_0;
	}
	public BigDecimal getR47_othercurrencies_p() {
		return r47_othercurrencies_p;
	}
	public void setR47_othercurrencies_p(BigDecimal r47_othercurrencies_p) {
		this.r47_othercurrencies_p = r47_othercurrencies_p;
	}
	public BigDecimal getR47_othercurrencies_q() {
		return r47_othercurrencies_q;
	}
	public void setR47_othercurrencies_q(BigDecimal r47_othercurrencies_q) {
		this.r47_othercurrencies_q = r47_othercurrencies_q;
	}
	public BigDecimal getR47_othercurrencies_r() {
		return r47_othercurrencies_r;
	}
	public void setR47_othercurrencies_r(BigDecimal r47_othercurrencies_r) {
		this.r47_othercurrencies_r = r47_othercurrencies_r;
	}
	public BigDecimal getR47_othercurrencies_s() {
		return r47_othercurrencies_s;
	}
	public void setR47_othercurrencies_s(BigDecimal r47_othercurrencies_s) {
		this.r47_othercurrencies_s = r47_othercurrencies_s;
	}
	public BigDecimal getR47_othercurrencies_t() {
		return r47_othercurrencies_t;
	}
	public void setR47_othercurrencies_t(BigDecimal r47_othercurrencies_t) {
		this.r47_othercurrencies_t = r47_othercurrencies_t;
	}
	public BigDecimal getR47_othercurrencies_u() {
		return r47_othercurrencies_u;
	}
	public void setR47_othercurrencies_u(BigDecimal r47_othercurrencies_u) {
		this.r47_othercurrencies_u = r47_othercurrencies_u;
	}
	public BigDecimal getR47_othercurrencies_v() {
		return r47_othercurrencies_v;
	}
	public void setR47_othercurrencies_v(BigDecimal r47_othercurrencies_v) {
		this.r47_othercurrencies_v = r47_othercurrencies_v;
	}
	public BigDecimal getR47_othercurrencies_w() {
		return r47_othercurrencies_w;
	}
	public void setR47_othercurrencies_w(BigDecimal r47_othercurrencies_w) {
		this.r47_othercurrencies_w = r47_othercurrencies_w;
	}
	public String getR48_product() {
		return r48_product;
	}
	public void setR48_product(String r48_product) {
		this.r48_product = r48_product;
	}
	public BigDecimal getR48_pula() {
		return r48_pula;
	}
	public void setR48_pula(BigDecimal r48_pula) {
		this.r48_pula = r48_pula;
	}
	public BigDecimal getR48_usd() {
		return r48_usd;
	}
	public void setR48_usd(BigDecimal r48_usd) {
		this.r48_usd = r48_usd;
	}
	public BigDecimal getR48_zar() {
		return r48_zar;
	}
	public void setR48_zar(BigDecimal r48_zar) {
		this.r48_zar = r48_zar;
	}
	public BigDecimal getR48_gbp() {
		return r48_gbp;
	}
	public void setR48_gbp(BigDecimal r48_gbp) {
		this.r48_gbp = r48_gbp;
	}
	public BigDecimal getR48_euro() {
		return r48_euro;
	}
	public void setR48_euro(BigDecimal r48_euro) {
		this.r48_euro = r48_euro;
	}
	public BigDecimal getR48_othercurrencies_inr() {
		return r48_othercurrencies_inr;
	}
	public void setR48_othercurrencies_inr(BigDecimal r48_othercurrencies_inr) {
		this.r48_othercurrencies_inr = r48_othercurrencies_inr;
	}
	public BigDecimal getR48_othercurrencies_aud() {
		return r48_othercurrencies_aud;
	}
	public void setR48_othercurrencies_aud(BigDecimal r48_othercurrencies_aud) {
		this.r48_othercurrencies_aud = r48_othercurrencies_aud;
	}
	public BigDecimal getR48_othercurrencies_i() {
		return r48_othercurrencies_i;
	}
	public void setR48_othercurrencies_i(BigDecimal r48_othercurrencies_i) {
		this.r48_othercurrencies_i = r48_othercurrencies_i;
	}
	public BigDecimal getR48_othercurrencies_j() {
		return r48_othercurrencies_j;
	}
	public void setR48_othercurrencies_j(BigDecimal r48_othercurrencies_j) {
		this.r48_othercurrencies_j = r48_othercurrencies_j;
	}
	public BigDecimal getR48_othercurrencies_k() {
		return r48_othercurrencies_k;
	}
	public void setR48_othercurrencies_k(BigDecimal r48_othercurrencies_k) {
		this.r48_othercurrencies_k = r48_othercurrencies_k;
	}
	public BigDecimal getR48_othercurrencies_l() {
		return r48_othercurrencies_l;
	}
	public void setR48_othercurrencies_l(BigDecimal r48_othercurrencies_l) {
		this.r48_othercurrencies_l = r48_othercurrencies_l;
	}
	public BigDecimal getR48_othercurrencies_m() {
		return r48_othercurrencies_m;
	}
	public void setR48_othercurrencies_m(BigDecimal r48_othercurrencies_m) {
		this.r48_othercurrencies_m = r48_othercurrencies_m;
	}
	public BigDecimal getR48_othercurrencies_n() {
		return r48_othercurrencies_n;
	}
	public void setR48_othercurrencies_n(BigDecimal r48_othercurrencies_n) {
		this.r48_othercurrencies_n = r48_othercurrencies_n;
	}
	public BigDecimal getR48_othercurrencies_0() {
		return r48_othercurrencies_0;
	}
	public void setR48_othercurrencies_0(BigDecimal r48_othercurrencies_0) {
		this.r48_othercurrencies_0 = r48_othercurrencies_0;
	}
	public BigDecimal getR48_othercurrencies_p() {
		return r48_othercurrencies_p;
	}
	public void setR48_othercurrencies_p(BigDecimal r48_othercurrencies_p) {
		this.r48_othercurrencies_p = r48_othercurrencies_p;
	}
	public BigDecimal getR48_othercurrencies_q() {
		return r48_othercurrencies_q;
	}
	public void setR48_othercurrencies_q(BigDecimal r48_othercurrencies_q) {
		this.r48_othercurrencies_q = r48_othercurrencies_q;
	}
	public BigDecimal getR48_othercurrencies_r() {
		return r48_othercurrencies_r;
	}
	public void setR48_othercurrencies_r(BigDecimal r48_othercurrencies_r) {
		this.r48_othercurrencies_r = r48_othercurrencies_r;
	}
	public BigDecimal getR48_othercurrencies_s() {
		return r48_othercurrencies_s;
	}
	public void setR48_othercurrencies_s(BigDecimal r48_othercurrencies_s) {
		this.r48_othercurrencies_s = r48_othercurrencies_s;
	}
	public BigDecimal getR48_othercurrencies_t() {
		return r48_othercurrencies_t;
	}
	public void setR48_othercurrencies_t(BigDecimal r48_othercurrencies_t) {
		this.r48_othercurrencies_t = r48_othercurrencies_t;
	}
	public BigDecimal getR48_othercurrencies_u() {
		return r48_othercurrencies_u;
	}
	public void setR48_othercurrencies_u(BigDecimal r48_othercurrencies_u) {
		this.r48_othercurrencies_u = r48_othercurrencies_u;
	}
	public BigDecimal getR48_othercurrencies_v() {
		return r48_othercurrencies_v;
	}
	public void setR48_othercurrencies_v(BigDecimal r48_othercurrencies_v) {
		this.r48_othercurrencies_v = r48_othercurrencies_v;
	}
	public BigDecimal getR48_othercurrencies_w() {
		return r48_othercurrencies_w;
	}
	public void setR48_othercurrencies_w(BigDecimal r48_othercurrencies_w) {
		this.r48_othercurrencies_w = r48_othercurrencies_w;
	}
	public String getR49_product() {
		return r49_product;
	}
	public void setR49_product(String r49_product) {
		this.r49_product = r49_product;
	}
	public BigDecimal getR49_pula() {
		return r49_pula;
	}
	public void setR49_pula(BigDecimal r49_pula) {
		this.r49_pula = r49_pula;
	}
	public BigDecimal getR49_usd() {
		return r49_usd;
	}
	public void setR49_usd(BigDecimal r49_usd) {
		this.r49_usd = r49_usd;
	}
	public BigDecimal getR49_zar() {
		return r49_zar;
	}
	public void setR49_zar(BigDecimal r49_zar) {
		this.r49_zar = r49_zar;
	}
	public BigDecimal getR49_gbp() {
		return r49_gbp;
	}
	public void setR49_gbp(BigDecimal r49_gbp) {
		this.r49_gbp = r49_gbp;
	}
	public BigDecimal getR49_euro() {
		return r49_euro;
	}
	public void setR49_euro(BigDecimal r49_euro) {
		this.r49_euro = r49_euro;
	}
	public BigDecimal getR49_othercurrencies_inr() {
		return r49_othercurrencies_inr;
	}
	public void setR49_othercurrencies_inr(BigDecimal r49_othercurrencies_inr) {
		this.r49_othercurrencies_inr = r49_othercurrencies_inr;
	}
	public BigDecimal getR49_othercurrencies_aud() {
		return r49_othercurrencies_aud;
	}
	public void setR49_othercurrencies_aud(BigDecimal r49_othercurrencies_aud) {
		this.r49_othercurrencies_aud = r49_othercurrencies_aud;
	}
	public BigDecimal getR49_othercurrencies_i() {
		return r49_othercurrencies_i;
	}
	public void setR49_othercurrencies_i(BigDecimal r49_othercurrencies_i) {
		this.r49_othercurrencies_i = r49_othercurrencies_i;
	}
	public BigDecimal getR49_othercurrencies_j() {
		return r49_othercurrencies_j;
	}
	public void setR49_othercurrencies_j(BigDecimal r49_othercurrencies_j) {
		this.r49_othercurrencies_j = r49_othercurrencies_j;
	}
	public BigDecimal getR49_othercurrencies_k() {
		return r49_othercurrencies_k;
	}
	public void setR49_othercurrencies_k(BigDecimal r49_othercurrencies_k) {
		this.r49_othercurrencies_k = r49_othercurrencies_k;
	}
	public BigDecimal getR49_othercurrencies_l() {
		return r49_othercurrencies_l;
	}
	public void setR49_othercurrencies_l(BigDecimal r49_othercurrencies_l) {
		this.r49_othercurrencies_l = r49_othercurrencies_l;
	}
	public BigDecimal getR49_othercurrencies_m() {
		return r49_othercurrencies_m;
	}
	public void setR49_othercurrencies_m(BigDecimal r49_othercurrencies_m) {
		this.r49_othercurrencies_m = r49_othercurrencies_m;
	}
	public BigDecimal getR49_othercurrencies_n() {
		return r49_othercurrencies_n;
	}
	public void setR49_othercurrencies_n(BigDecimal r49_othercurrencies_n) {
		this.r49_othercurrencies_n = r49_othercurrencies_n;
	}
	public BigDecimal getR49_othercurrencies_0() {
		return r49_othercurrencies_0;
	}
	public void setR49_othercurrencies_0(BigDecimal r49_othercurrencies_0) {
		this.r49_othercurrencies_0 = r49_othercurrencies_0;
	}
	public BigDecimal getR49_othercurrencies_p() {
		return r49_othercurrencies_p;
	}
	public void setR49_othercurrencies_p(BigDecimal r49_othercurrencies_p) {
		this.r49_othercurrencies_p = r49_othercurrencies_p;
	}
	public BigDecimal getR49_othercurrencies_q() {
		return r49_othercurrencies_q;
	}
	public void setR49_othercurrencies_q(BigDecimal r49_othercurrencies_q) {
		this.r49_othercurrencies_q = r49_othercurrencies_q;
	}
	public BigDecimal getR49_othercurrencies_r() {
		return r49_othercurrencies_r;
	}
	public void setR49_othercurrencies_r(BigDecimal r49_othercurrencies_r) {
		this.r49_othercurrencies_r = r49_othercurrencies_r;
	}
	public BigDecimal getR49_othercurrencies_s() {
		return r49_othercurrencies_s;
	}
	public void setR49_othercurrencies_s(BigDecimal r49_othercurrencies_s) {
		this.r49_othercurrencies_s = r49_othercurrencies_s;
	}
	public BigDecimal getR49_othercurrencies_t() {
		return r49_othercurrencies_t;
	}
	public void setR49_othercurrencies_t(BigDecimal r49_othercurrencies_t) {
		this.r49_othercurrencies_t = r49_othercurrencies_t;
	}
	public BigDecimal getR49_othercurrencies_u() {
		return r49_othercurrencies_u;
	}
	public void setR49_othercurrencies_u(BigDecimal r49_othercurrencies_u) {
		this.r49_othercurrencies_u = r49_othercurrencies_u;
	}
	public BigDecimal getR49_othercurrencies_v() {
		return r49_othercurrencies_v;
	}
	public void setR49_othercurrencies_v(BigDecimal r49_othercurrencies_v) {
		this.r49_othercurrencies_v = r49_othercurrencies_v;
	}
	public BigDecimal getR49_othercurrencies_w() {
		return r49_othercurrencies_w;
	}
	public void setR49_othercurrencies_w(BigDecimal r49_othercurrencies_w) {
		this.r49_othercurrencies_w = r49_othercurrencies_w;
	}
	public String getR50_product() {
		return r50_product;
	}
	public void setR50_product(String r50_product) {
		this.r50_product = r50_product;
	}
	public BigDecimal getR50_pula() {
		return r50_pula;
	}
	public void setR50_pula(BigDecimal r50_pula) {
		this.r50_pula = r50_pula;
	}
	public BigDecimal getR50_usd() {
		return r50_usd;
	}
	public void setR50_usd(BigDecimal r50_usd) {
		this.r50_usd = r50_usd;
	}
	public BigDecimal getR50_zar() {
		return r50_zar;
	}
	public void setR50_zar(BigDecimal r50_zar) {
		this.r50_zar = r50_zar;
	}
	public BigDecimal getR50_gbp() {
		return r50_gbp;
	}
	public void setR50_gbp(BigDecimal r50_gbp) {
		this.r50_gbp = r50_gbp;
	}
	public BigDecimal getR50_euro() {
		return r50_euro;
	}
	public void setR50_euro(BigDecimal r50_euro) {
		this.r50_euro = r50_euro;
	}
	public BigDecimal getR50_othercurrencies_inr() {
		return r50_othercurrencies_inr;
	}
	public void setR50_othercurrencies_inr(BigDecimal r50_othercurrencies_inr) {
		this.r50_othercurrencies_inr = r50_othercurrencies_inr;
	}
	public BigDecimal getR50_othercurrencies_aud() {
		return r50_othercurrencies_aud;
	}
	public void setR50_othercurrencies_aud(BigDecimal r50_othercurrencies_aud) {
		this.r50_othercurrencies_aud = r50_othercurrencies_aud;
	}
	public BigDecimal getR50_othercurrencies_i() {
		return r50_othercurrencies_i;
	}
	public void setR50_othercurrencies_i(BigDecimal r50_othercurrencies_i) {
		this.r50_othercurrencies_i = r50_othercurrencies_i;
	}
	public BigDecimal getR50_othercurrencies_j() {
		return r50_othercurrencies_j;
	}
	public void setR50_othercurrencies_j(BigDecimal r50_othercurrencies_j) {
		this.r50_othercurrencies_j = r50_othercurrencies_j;
	}
	public BigDecimal getR50_othercurrencies_k() {
		return r50_othercurrencies_k;
	}
	public void setR50_othercurrencies_k(BigDecimal r50_othercurrencies_k) {
		this.r50_othercurrencies_k = r50_othercurrencies_k;
	}
	public BigDecimal getR50_othercurrencies_l() {
		return r50_othercurrencies_l;
	}
	public void setR50_othercurrencies_l(BigDecimal r50_othercurrencies_l) {
		this.r50_othercurrencies_l = r50_othercurrencies_l;
	}
	public BigDecimal getR50_othercurrencies_m() {
		return r50_othercurrencies_m;
	}
	public void setR50_othercurrencies_m(BigDecimal r50_othercurrencies_m) {
		this.r50_othercurrencies_m = r50_othercurrencies_m;
	}
	public BigDecimal getR50_othercurrencies_n() {
		return r50_othercurrencies_n;
	}
	public void setR50_othercurrencies_n(BigDecimal r50_othercurrencies_n) {
		this.r50_othercurrencies_n = r50_othercurrencies_n;
	}
	public BigDecimal getR50_othercurrencies_0() {
		return r50_othercurrencies_0;
	}
	public void setR50_othercurrencies_0(BigDecimal r50_othercurrencies_0) {
		this.r50_othercurrencies_0 = r50_othercurrencies_0;
	}
	public BigDecimal getR50_othercurrencies_p() {
		return r50_othercurrencies_p;
	}
	public void setR50_othercurrencies_p(BigDecimal r50_othercurrencies_p) {
		this.r50_othercurrencies_p = r50_othercurrencies_p;
	}
	public BigDecimal getR50_othercurrencies_q() {
		return r50_othercurrencies_q;
	}
	public void setR50_othercurrencies_q(BigDecimal r50_othercurrencies_q) {
		this.r50_othercurrencies_q = r50_othercurrencies_q;
	}
	public BigDecimal getR50_othercurrencies_r() {
		return r50_othercurrencies_r;
	}
	public void setR50_othercurrencies_r(BigDecimal r50_othercurrencies_r) {
		this.r50_othercurrencies_r = r50_othercurrencies_r;
	}
	public BigDecimal getR50_othercurrencies_s() {
		return r50_othercurrencies_s;
	}
	public void setR50_othercurrencies_s(BigDecimal r50_othercurrencies_s) {
		this.r50_othercurrencies_s = r50_othercurrencies_s;
	}
	public BigDecimal getR50_othercurrencies_t() {
		return r50_othercurrencies_t;
	}
	public void setR50_othercurrencies_t(BigDecimal r50_othercurrencies_t) {
		this.r50_othercurrencies_t = r50_othercurrencies_t;
	}
	public BigDecimal getR50_othercurrencies_u() {
		return r50_othercurrencies_u;
	}
	public void setR50_othercurrencies_u(BigDecimal r50_othercurrencies_u) {
		this.r50_othercurrencies_u = r50_othercurrencies_u;
	}
	public BigDecimal getR50_othercurrencies_v() {
		return r50_othercurrencies_v;
	}
	public void setR50_othercurrencies_v(BigDecimal r50_othercurrencies_v) {
		this.r50_othercurrencies_v = r50_othercurrencies_v;
	}
	public BigDecimal getR50_othercurrencies_w() {
		return r50_othercurrencies_w;
	}
	public void setR50_othercurrencies_w(BigDecimal r50_othercurrencies_w) {
		this.r50_othercurrencies_w = r50_othercurrencies_w;
	}
	public String getR51_product() {
		return r51_product;
	}
	public void setR51_product(String r51_product) {
		this.r51_product = r51_product;
	}
	public BigDecimal getR51_pula() {
		return r51_pula;
	}
	public void setR51_pula(BigDecimal r51_pula) {
		this.r51_pula = r51_pula;
	}
	public BigDecimal getR51_usd() {
		return r51_usd;
	}
	public void setR51_usd(BigDecimal r51_usd) {
		this.r51_usd = r51_usd;
	}
	public BigDecimal getR51_zar() {
		return r51_zar;
	}
	public void setR51_zar(BigDecimal r51_zar) {
		this.r51_zar = r51_zar;
	}
	public BigDecimal getR51_gbp() {
		return r51_gbp;
	}
	public void setR51_gbp(BigDecimal r51_gbp) {
		this.r51_gbp = r51_gbp;
	}
	public BigDecimal getR51_euro() {
		return r51_euro;
	}
	public void setR51_euro(BigDecimal r51_euro) {
		this.r51_euro = r51_euro;
	}
	public BigDecimal getR51_othercurrencies_inr() {
		return r51_othercurrencies_inr;
	}
	public void setR51_othercurrencies_inr(BigDecimal r51_othercurrencies_inr) {
		this.r51_othercurrencies_inr = r51_othercurrencies_inr;
	}
	public BigDecimal getR51_othercurrencies_aud() {
		return r51_othercurrencies_aud;
	}
	public void setR51_othercurrencies_aud(BigDecimal r51_othercurrencies_aud) {
		this.r51_othercurrencies_aud = r51_othercurrencies_aud;
	}
	public BigDecimal getR51_othercurrencies_i() {
		return r51_othercurrencies_i;
	}
	public void setR51_othercurrencies_i(BigDecimal r51_othercurrencies_i) {
		this.r51_othercurrencies_i = r51_othercurrencies_i;
	}
	public BigDecimal getR51_othercurrencies_j() {
		return r51_othercurrencies_j;
	}
	public void setR51_othercurrencies_j(BigDecimal r51_othercurrencies_j) {
		this.r51_othercurrencies_j = r51_othercurrencies_j;
	}
	public BigDecimal getR51_othercurrencies_k() {
		return r51_othercurrencies_k;
	}
	public void setR51_othercurrencies_k(BigDecimal r51_othercurrencies_k) {
		this.r51_othercurrencies_k = r51_othercurrencies_k;
	}
	public BigDecimal getR51_othercurrencies_l() {
		return r51_othercurrencies_l;
	}
	public void setR51_othercurrencies_l(BigDecimal r51_othercurrencies_l) {
		this.r51_othercurrencies_l = r51_othercurrencies_l;
	}
	public BigDecimal getR51_othercurrencies_m() {
		return r51_othercurrencies_m;
	}
	public void setR51_othercurrencies_m(BigDecimal r51_othercurrencies_m) {
		this.r51_othercurrencies_m = r51_othercurrencies_m;
	}
	public BigDecimal getR51_othercurrencies_n() {
		return r51_othercurrencies_n;
	}
	public void setR51_othercurrencies_n(BigDecimal r51_othercurrencies_n) {
		this.r51_othercurrencies_n = r51_othercurrencies_n;
	}
	public BigDecimal getR51_othercurrencies_0() {
		return r51_othercurrencies_0;
	}
	public void setR51_othercurrencies_0(BigDecimal r51_othercurrencies_0) {
		this.r51_othercurrencies_0 = r51_othercurrencies_0;
	}
	public BigDecimal getR51_othercurrencies_p() {
		return r51_othercurrencies_p;
	}
	public void setR51_othercurrencies_p(BigDecimal r51_othercurrencies_p) {
		this.r51_othercurrencies_p = r51_othercurrencies_p;
	}
	public BigDecimal getR51_othercurrencies_q() {
		return r51_othercurrencies_q;
	}
	public void setR51_othercurrencies_q(BigDecimal r51_othercurrencies_q) {
		this.r51_othercurrencies_q = r51_othercurrencies_q;
	}
	public BigDecimal getR51_othercurrencies_r() {
		return r51_othercurrencies_r;
	}
	public void setR51_othercurrencies_r(BigDecimal r51_othercurrencies_r) {
		this.r51_othercurrencies_r = r51_othercurrencies_r;
	}
	public BigDecimal getR51_othercurrencies_s() {
		return r51_othercurrencies_s;
	}
	public void setR51_othercurrencies_s(BigDecimal r51_othercurrencies_s) {
		this.r51_othercurrencies_s = r51_othercurrencies_s;
	}
	public BigDecimal getR51_othercurrencies_t() {
		return r51_othercurrencies_t;
	}
	public void setR51_othercurrencies_t(BigDecimal r51_othercurrencies_t) {
		this.r51_othercurrencies_t = r51_othercurrencies_t;
	}
	public BigDecimal getR51_othercurrencies_u() {
		return r51_othercurrencies_u;
	}
	public void setR51_othercurrencies_u(BigDecimal r51_othercurrencies_u) {
		this.r51_othercurrencies_u = r51_othercurrencies_u;
	}
	public BigDecimal getR51_othercurrencies_v() {
		return r51_othercurrencies_v;
	}
	public void setR51_othercurrencies_v(BigDecimal r51_othercurrencies_v) {
		this.r51_othercurrencies_v = r51_othercurrencies_v;
	}
	public BigDecimal getR51_othercurrencies_w() {
		return r51_othercurrencies_w;
	}
	public void setR51_othercurrencies_w(BigDecimal r51_othercurrencies_w) {
		this.r51_othercurrencies_w = r51_othercurrencies_w;
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
	public M_CALOC_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


}
