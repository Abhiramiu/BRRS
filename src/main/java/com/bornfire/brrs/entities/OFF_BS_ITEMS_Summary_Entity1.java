package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "BRRS_OFF_BS_ITEMS_SUMMARYTABLE1")


public class OFF_BS_ITEMS_Summary_Entity1 {
	
	
	private String	r12_product;
	private String	r12_client_grp;
	private BigDecimal	r12_total_book_expo;
	private BigDecimal	r12_margin_pro;
	private BigDecimal	r12_book_expo;
	private BigDecimal	r12_ccf_cont;
	private BigDecimal	r12_equiv_value;
	private BigDecimal	r12_rw_obligant;
	private BigDecimal	r12_rav;
	private String	r13_product;
	private String	r13_client_grp;
	private BigDecimal	r13_total_book_expo;
	private BigDecimal	r13_margin_pro;
	private BigDecimal	r13_book_expo;
	private BigDecimal	r13_ccf_cont;
	private BigDecimal	r13_equiv_value;
	private BigDecimal	r13_rw_obligant;
	private BigDecimal	r13_rav;
	private String	r14_product;
	private String	r14_client_grp;
	private BigDecimal	r14_total_book_expo;
	private BigDecimal	r14_margin_pro;
	private BigDecimal	r14_book_expo;
	private BigDecimal	r14_ccf_cont;
	private BigDecimal	r14_equiv_value;
	private BigDecimal	r14_rw_obligant;
	private BigDecimal	r14_rav;
	private String	r15_product;
	private String	r15_client_grp;
	private BigDecimal	r15_total_book_expo;
	private BigDecimal	r15_margin_pro;
	private BigDecimal	r15_book_expo;
	private BigDecimal	r15_ccf_cont;
	private BigDecimal	r15_equiv_value;
	private BigDecimal	r15_rw_obligant;
	private BigDecimal	r15_rav;
	private String	r16_product;
	private String	r16_client_grp;
	private BigDecimal	r16_total_book_expo;
	private BigDecimal	r16_margin_pro;
	private BigDecimal	r16_book_expo;
	private BigDecimal	r16_ccf_cont;
	private BigDecimal	r16_equiv_value;
	private BigDecimal	r16_rw_obligant;
	private BigDecimal	r16_rav;
	private String	r17_product;
	private String	r17_client_grp;
	private BigDecimal	r17_total_book_expo;
	private BigDecimal	r17_margin_pro;
	private BigDecimal	r17_book_expo;
	private BigDecimal	r17_ccf_cont;
	private BigDecimal	r17_equiv_value;
	private BigDecimal	r17_rw_obligant;
	private BigDecimal	r17_rav;
	private String	r18_product;
	private String	r18_client_grp;
	private BigDecimal	r18_total_book_expo;
	private BigDecimal	r18_margin_pro;
	private BigDecimal	r18_book_expo;
	private BigDecimal	r18_ccf_cont;
	private BigDecimal	r18_equiv_value;
	private BigDecimal	r18_rw_obligant;
	private BigDecimal	r18_rav;
	private String	r19_product;
	private String	r19_client_grp;
	private BigDecimal	r19_total_book_expo;
	private BigDecimal	r19_margin_pro;
	private BigDecimal	r19_book_expo;
	private BigDecimal	r19_ccf_cont;
	private BigDecimal	r19_equiv_value;
	private BigDecimal	r19_rw_obligant;
	private BigDecimal	r19_rav;
	private String	r20_product;
	private String	r20_client_grp;
	private BigDecimal	r20_total_book_expo;
	private BigDecimal	r20_margin_pro;
	private BigDecimal	r20_book_expo;
	private BigDecimal	r20_ccf_cont;
	private BigDecimal	r20_equiv_value;
	private BigDecimal	r20_rw_obligant;
	private BigDecimal	r20_rav;
	private String	r21_product;
	private String	r21_client_grp;
	private BigDecimal	r21_total_book_expo;
	private BigDecimal	r21_margin_pro;
	private BigDecimal	r21_book_expo;
	private BigDecimal	r21_ccf_cont;
	private BigDecimal	r21_equiv_value;
	private BigDecimal	r21_rw_obligant;
	private BigDecimal	r21_rav;
	private String	r22_product;
	private String	r22_client_grp;
	private BigDecimal	r22_total_book_expo;
	private BigDecimal	r22_margin_pro;
	private BigDecimal	r22_book_expo;
	private BigDecimal	r22_ccf_cont;
	private BigDecimal	r22_equiv_value;
	private BigDecimal	r22_rw_obligant;
	private BigDecimal	r22_rav;
	private String	r23_product;
	private String	r23_client_grp;
	private BigDecimal	r23_total_book_expo;
	private BigDecimal	r23_margin_pro;
	private BigDecimal	r23_book_expo;
	private BigDecimal	r23_ccf_cont;
	private BigDecimal	r23_equiv_value;
	private BigDecimal	r23_rw_obligant;
	private BigDecimal	r23_rav;
	private String	r24_product;
	private String	r24_client_grp;
	private BigDecimal	r24_total_book_expo;
	private BigDecimal	r24_margin_pro;
	private BigDecimal	r24_book_expo;
	private BigDecimal	r24_ccf_cont;
	private BigDecimal	r24_equiv_value;
	private BigDecimal	r24_rw_obligant;
	private BigDecimal	r24_rav;
	private String	r25_product;
	private String	r25_client_grp;
	private BigDecimal	r25_total_book_expo;
	private BigDecimal	r25_margin_pro;
	private BigDecimal	r25_book_expo;
	private BigDecimal	r25_ccf_cont;
	private BigDecimal	r25_equiv_value;
	private BigDecimal	r25_rw_obligant;
	private BigDecimal	r25_rav;
	private String	r26_product;
	private String	r26_client_grp;
	private BigDecimal	r26_total_book_expo;
	private BigDecimal	r26_margin_pro;
	private BigDecimal	r26_book_expo;
	private BigDecimal	r26_ccf_cont;
	private BigDecimal	r26_equiv_value;
	private BigDecimal	r26_rw_obligant;
	private BigDecimal	r26_rav;
	private String	r27_product;
	private String	r27_client_grp;
	private BigDecimal	r27_total_book_expo;
	private BigDecimal	r27_margin_pro;
	private BigDecimal	r27_book_expo;
	private BigDecimal	r27_ccf_cont;
	private BigDecimal	r27_equiv_value;
	private BigDecimal	r27_rw_obligant;
	private BigDecimal	r27_rav;
	private String	r28_product;
	private String	r28_client_grp;
	private BigDecimal	r28_total_book_expo;
	private BigDecimal	r28_margin_pro;
	private BigDecimal	r28_book_expo;
	private BigDecimal	r28_ccf_cont;
	private BigDecimal	r28_equiv_value;
	private BigDecimal	r28_rw_obligant;
	private BigDecimal	r28_rav;
	private String	r29_product;
	private String	r29_client_grp;
	private BigDecimal	r29_total_book_expo;
	private BigDecimal	r29_margin_pro;
	private BigDecimal	r29_book_expo;
	private BigDecimal	r29_ccf_cont;
	private BigDecimal	r29_equiv_value;
	private BigDecimal	r29_rw_obligant;
	private BigDecimal	r29_rav;
	private String	r30_product;
	private String	r30_client_grp;
	private BigDecimal	r30_total_book_expo;
	private BigDecimal	r30_margin_pro;
	private BigDecimal	r30_book_expo;
	private BigDecimal	r30_ccf_cont;
	private BigDecimal	r30_equiv_value;
	private BigDecimal	r30_rw_obligant;
	private BigDecimal	r30_rav;
	private String	r31_product;
	private String	r31_client_grp;
	private BigDecimal	r31_total_book_expo;
	private BigDecimal	r31_margin_pro;
	private BigDecimal	r31_book_expo;
	private BigDecimal	r31_ccf_cont;
	private BigDecimal	r31_equiv_value;
	private BigDecimal	r31_rw_obligant;
	private BigDecimal	r31_rav;
	private String	r32_product;
	private String	r32_client_grp;
	private BigDecimal	r32_total_book_expo;
	private BigDecimal	r32_margin_pro;
	private BigDecimal	r32_book_expo;
	private BigDecimal	r32_ccf_cont;
	private BigDecimal	r32_equiv_value;
	private BigDecimal	r32_rw_obligant;
	private BigDecimal	r32_rav;
	private String	r33_product;
	private String	r33_client_grp;
	private BigDecimal	r33_total_book_expo;
	private BigDecimal	r33_margin_pro;
	private BigDecimal	r33_book_expo;
	private BigDecimal	r33_ccf_cont;
	private BigDecimal	r33_equiv_value;
	private BigDecimal	r33_rw_obligant;
	private BigDecimal	r33_rav;
	private String	r34_product;
	private String	r34_client_grp;
	private BigDecimal	r34_total_book_expo;
	private BigDecimal	r34_margin_pro;
	private BigDecimal	r34_book_expo;
	private BigDecimal	r34_ccf_cont;
	private BigDecimal	r34_equiv_value;
	private BigDecimal	r34_rw_obligant;
	private BigDecimal	r34_rav;
	private String	r35_product;
	private String	r35_client_grp;
	private BigDecimal	r35_total_book_expo;
	private BigDecimal	r35_margin_pro;
	private BigDecimal	r35_book_expo;
	private BigDecimal	r35_ccf_cont;
	private BigDecimal	r35_equiv_value;
	private BigDecimal	r35_rw_obligant;
	private BigDecimal	r35_rav;
	private String	r36_product;
	private String	r36_client_grp;
	private BigDecimal	r36_total_book_expo;
	private BigDecimal	r36_margin_pro;
	private BigDecimal	r36_book_expo;
	private BigDecimal	r36_ccf_cont;
	private BigDecimal	r36_equiv_value;
	private BigDecimal	r36_rw_obligant;
	private BigDecimal	r36_rav;
	private String	r37_product;
	private String	r37_client_grp;
	private BigDecimal	r37_total_book_expo;
	private BigDecimal	r37_margin_pro;
	private BigDecimal	r37_book_expo;
	private BigDecimal	r37_ccf_cont;
	private BigDecimal	r37_equiv_value;
	private BigDecimal	r37_rw_obligant;
	private BigDecimal	r37_rav;
	private String	r38_product;
	private String	r38_client_grp;
	private BigDecimal	r38_total_book_expo;
	private BigDecimal	r38_margin_pro;
	private BigDecimal	r38_book_expo;
	private BigDecimal	r38_ccf_cont;
	private BigDecimal	r38_equiv_value;
	private BigDecimal	r38_rw_obligant;
	private BigDecimal	r38_rav;
	private String	r39_product;
	private String	r39_client_grp;
	private BigDecimal	r39_total_book_expo;
	private BigDecimal	r39_margin_pro;
	private BigDecimal	r39_book_expo;
	private BigDecimal	r39_ccf_cont;
	private BigDecimal	r39_equiv_value;
	private BigDecimal	r39_rw_obligant;
	private BigDecimal	r39_rav;
	private String	r40_product;
	private String	r40_client_grp;
	private BigDecimal	r40_total_book_expo;
	private BigDecimal	r40_margin_pro;
	private BigDecimal	r40_book_expo;
	private BigDecimal	r40_ccf_cont;
	private BigDecimal	r40_equiv_value;
	private BigDecimal	r40_rw_obligant;
	private BigDecimal	r40_rav;
	private String	r41_product;
	private String	r41_client_grp;
	private BigDecimal	r41_total_book_expo;
	private BigDecimal	r41_margin_pro;
	private BigDecimal	r41_book_expo;
	private BigDecimal	r41_ccf_cont;
	private BigDecimal	r41_equiv_value;
	private BigDecimal	r41_rw_obligant;
	private BigDecimal	r41_rav;
	private String	r42_product;
	private String	r42_client_grp;
	private BigDecimal	r42_total_book_expo;
	private BigDecimal	r42_margin_pro;
	private BigDecimal	r42_book_expo;
	private BigDecimal	r42_ccf_cont;
	private BigDecimal	r42_equiv_value;
	private BigDecimal	r42_rw_obligant;
	private BigDecimal	r42_rav;
	private String	r43_product;
	private String	r43_client_grp;
	private BigDecimal	r43_total_book_expo;
	private BigDecimal	r43_margin_pro;
	private BigDecimal	r43_book_expo;
	private BigDecimal	r43_ccf_cont;
	private BigDecimal	r43_equiv_value;
	private BigDecimal	r43_rw_obligant;
	private BigDecimal	r43_rav;
	private String	r44_product;
	private String	r44_client_grp;
	private BigDecimal	r44_total_book_expo;
	private BigDecimal	r44_margin_pro;
	private BigDecimal	r44_book_expo;
	private BigDecimal	r44_ccf_cont;
	private BigDecimal	r44_equiv_value;
	private BigDecimal	r44_rw_obligant;
	private BigDecimal	r44_rav;
	private String	r45_product;
	private String	r45_client_grp;
	private BigDecimal	r45_total_book_expo;
	private BigDecimal	r45_margin_pro;
	private BigDecimal	r45_book_expo;
	private BigDecimal	r45_ccf_cont;
	private BigDecimal	r45_equiv_value;
	private BigDecimal	r45_rw_obligant;
	private BigDecimal	r45_rav;
	private String	r46_product;
	private String	r46_client_grp;
	private BigDecimal	r46_total_book_expo;
	private BigDecimal	r46_margin_pro;
	private BigDecimal	r46_book_expo;
	private BigDecimal	r46_ccf_cont;
	private BigDecimal	r46_equiv_value;
	private BigDecimal	r46_rw_obligant;
	private BigDecimal	r46_rav;
	private String	r61_product;
	private String	r61_client_grp;
	private BigDecimal	r61_total_book_expo;
	private BigDecimal	r61_margin_pro;
	private BigDecimal	r61_book_expo;
	private BigDecimal	r61_ccf_cont;
	private BigDecimal	r61_equiv_value;
	private BigDecimal	r61_rw_obligant;
	private BigDecimal	r61_rav;
	private String	r62_product;
	private String	r62_client_grp;
	private BigDecimal	r62_total_book_expo;
	private BigDecimal	r62_margin_pro;
	private BigDecimal	r62_book_expo;
	private BigDecimal	r62_ccf_cont;
	private BigDecimal	r62_equiv_value;
	private BigDecimal	r62_rw_obligant;
	private BigDecimal	r62_rav;
	private String	r63_product;
	private String	r63_client_grp;
	private BigDecimal	r63_total_book_expo;
	private BigDecimal	r63_margin_pro;
	private BigDecimal	r63_book_expo;
	private BigDecimal	r63_ccf_cont;
	private BigDecimal	r63_equiv_value;
	private BigDecimal	r63_rw_obligant;
	private BigDecimal	r63_rav;
	private String	r64_product;
	private String	r64_client_grp;
	private BigDecimal	r64_total_book_expo;
	private BigDecimal	r64_margin_pro;
	private BigDecimal	r64_book_expo;
	private BigDecimal	r64_ccf_cont;
	private BigDecimal	r64_equiv_value;
	private BigDecimal	r64_rw_obligant;
	private BigDecimal	r64_rav;
	private String	r65_product;
	private String	r65_client_grp;
	private BigDecimal	r65_total_book_expo;
	private BigDecimal	r65_margin_pro;
	private BigDecimal	r65_book_expo;
	private BigDecimal	r65_ccf_cont;
	private BigDecimal	r65_equiv_value;
	private BigDecimal	r65_rw_obligant;
	private BigDecimal	r65_rav;
	private String	r66_product;
	private String	r66_client_grp;
	private BigDecimal	r66_total_book_expo;
	private BigDecimal	r66_margin_pro;
	private BigDecimal	r66_book_expo;
	private BigDecimal	r66_ccf_cont;
	private BigDecimal	r66_equiv_value;
	private BigDecimal	r66_rw_obligant;
	private BigDecimal	r66_rav;
	private String	r67_product;
	private String	r67_client_grp;
	private BigDecimal	r67_total_book_expo;
	private BigDecimal	r67_margin_pro;
	private BigDecimal	r67_book_expo;
	private BigDecimal	r67_ccf_cont;
	private BigDecimal	r67_equiv_value;
	private BigDecimal	r67_rw_obligant;
	private BigDecimal	r67_rav;
	private String	r68_product;
	private String	r68_client_grp;
	private BigDecimal	r68_total_book_expo;
	private BigDecimal	r68_margin_pro;
	private BigDecimal	r68_book_expo;
	private BigDecimal	r68_ccf_cont;
	private BigDecimal	r68_equiv_value;
	private BigDecimal	r68_rw_obligant;
	private BigDecimal	r68_rav;
	private String	r69_product;
	private String	r69_client_grp;
	private BigDecimal	r69_total_book_expo;
	private BigDecimal	r69_margin_pro;
	private BigDecimal	r69_book_expo;
	private BigDecimal	r69_ccf_cont;
	private BigDecimal	r69_equiv_value;
	private BigDecimal	r69_rw_obligant;
	private BigDecimal	r69_rav;
	private String	r70_product;
	private String	r70_client_grp;
	private BigDecimal	r70_total_book_expo;
	private BigDecimal	r70_margin_pro;
	private BigDecimal	r70_book_expo;
	private BigDecimal	r70_ccf_cont;
	private BigDecimal	r70_equiv_value;
	private BigDecimal	r70_rw_obligant;
	private BigDecimal	r70_rav;
	private String	r71_product;
	private String	r71_client_grp;
	private BigDecimal	r71_total_book_expo;
	private BigDecimal	r71_margin_pro;
	private BigDecimal	r71_book_expo;
	private BigDecimal	r71_ccf_cont;
	private BigDecimal	r71_equiv_value;
	private BigDecimal	r71_rw_obligant;
	private BigDecimal	r71_rav;
	private String	r72_product;
	private String	r72_client_grp;
	private BigDecimal	r72_total_book_expo;
	private BigDecimal	r72_margin_pro;
	private BigDecimal	r72_book_expo;
	private BigDecimal	r72_ccf_cont;
	private BigDecimal	r72_equiv_value;
	private BigDecimal	r72_rw_obligant;
	private BigDecimal	r72_rav;
	private String	r73_product;
	private String	r73_client_grp;
	private BigDecimal	r73_total_book_expo;
	private BigDecimal	r73_margin_pro;
	private BigDecimal	r73_book_expo;
	private BigDecimal	r73_ccf_cont;
	private BigDecimal	r73_equiv_value;
	private BigDecimal	r73_rw_obligant;
	private BigDecimal	r73_rav;
	private String	r74_product;
	private String	r74_client_grp;
	private BigDecimal	r74_total_book_expo;
	private BigDecimal	r74_margin_pro;
	private BigDecimal	r74_book_expo;
	private BigDecimal	r74_ccf_cont;
	private BigDecimal	r74_equiv_value;
	private BigDecimal	r74_rw_obligant;
	private BigDecimal	r74_rav;
	private String	r75_product;
	private String	r75_client_grp;
	private BigDecimal	r75_total_book_expo;
	private BigDecimal	r75_margin_pro;
	private BigDecimal	r75_book_expo;
	private BigDecimal	r75_ccf_cont;
	private BigDecimal	r75_equiv_value;
	private BigDecimal	r75_rw_obligant;
	private BigDecimal	r75_rav;
	private String	r76_product;
	private String	r76_client_grp;
	private BigDecimal	r76_total_book_expo;
	private BigDecimal	r76_margin_pro;
	private BigDecimal	r76_book_expo;
	private BigDecimal	r76_ccf_cont;
	private BigDecimal	r76_equiv_value;
	private BigDecimal	r76_rw_obligant;
	private BigDecimal	r76_rav;
	private String	r77_product;
	private String	r77_client_grp;
	private BigDecimal	r77_total_book_expo;
	private BigDecimal	r77_margin_pro;
	private BigDecimal	r77_book_expo;
	private BigDecimal	r77_ccf_cont;
	private BigDecimal	r77_equiv_value;
	private BigDecimal	r77_rw_obligant;
	private BigDecimal	r77_rav;
	private String	r78_product;
	private String	r78_client_grp;
	private BigDecimal	r78_total_book_expo;
	private BigDecimal	r78_margin_pro;
	private BigDecimal	r78_book_expo;
	private BigDecimal	r78_ccf_cont;
	private BigDecimal	r78_equiv_value;
	private BigDecimal	r78_rw_obligant;
	private BigDecimal	r78_rav;
	private String	r79_product;
	private String	r79_client_grp;
	private BigDecimal	r79_total_book_expo;
	private BigDecimal	r79_margin_pro;
	private BigDecimal	r79_book_expo;
	private BigDecimal	r79_ccf_cont;
	private BigDecimal	r79_equiv_value;
	private BigDecimal	r79_rw_obligant;
	private BigDecimal	r79_rav;
	private String	r80_product;
	private String	r80_client_grp;
	private BigDecimal	r80_total_book_expo;
	private BigDecimal	r80_margin_pro;
	private BigDecimal	r80_book_expo;
	private BigDecimal	r80_ccf_cont;
	private BigDecimal	r80_equiv_value;
	private BigDecimal	r80_rw_obligant;
	private BigDecimal	r80_rav;
	private String	r81_product;
	private String	r81_client_grp;
	private BigDecimal	r81_total_book_expo;
	private BigDecimal	r81_margin_pro;
	private BigDecimal	r81_book_expo;
	private BigDecimal	r81_ccf_cont;
	private BigDecimal	r81_equiv_value;
	private BigDecimal	r81_rw_obligant;
	private BigDecimal	r81_rav;
	private String	r82_product;
	private String	r82_client_grp;
	private BigDecimal	r82_total_book_expo;
	private BigDecimal	r82_margin_pro;
	private BigDecimal	r82_book_expo;
	private BigDecimal	r82_ccf_cont;
	private BigDecimal	r82_equiv_value;
	private BigDecimal	r82_rw_obligant;
	private BigDecimal	r82_rav;
	private String	r83_product;
	private String	r83_client_grp;
	private BigDecimal	r83_total_book_expo;
	private BigDecimal	r83_margin_pro;
	private BigDecimal	r83_book_expo;
	private BigDecimal	r83_ccf_cont;
	private BigDecimal	r83_equiv_value;
	private BigDecimal	r83_rw_obligant;
	private BigDecimal	r83_rav;
	private String	r84_product;
	private String	r84_client_grp;
	private BigDecimal	r84_total_book_expo;
	private BigDecimal	r84_margin_pro;
	private BigDecimal	r84_book_expo;
	private BigDecimal	r84_ccf_cont;
	private BigDecimal	r84_equiv_value;
	private BigDecimal	r84_rw_obligant;
	private BigDecimal	r84_rav;
	private String	r100_product;
	private String	r100_client_grp;
	private BigDecimal	r100_total_book_expo;
	private BigDecimal	r100_margin_pro;
	private BigDecimal	r100_book_expo;
	private BigDecimal	r100_ccf_cont;
	private BigDecimal	r100_equiv_value;
	private BigDecimal	r100_rw_obligant;
	private BigDecimal	r100_rav;
	private String	r101_product;
	private String	r101_client_grp;
	private BigDecimal	r101_total_book_expo;
	private BigDecimal	r101_margin_pro;
	private BigDecimal	r101_book_expo;
	private BigDecimal	r101_ccf_cont;
	private BigDecimal	r101_equiv_value;
	private BigDecimal	r101_rw_obligant;
	private BigDecimal	r101_rav;
	private String	r102_product;
	private String	r102_client_grp;
	private BigDecimal	r102_total_book_expo;
	private BigDecimal	r102_margin_pro;
	private BigDecimal	r102_book_expo;
	private BigDecimal	r102_ccf_cont;
	private BigDecimal	r102_equiv_value;
	private BigDecimal	r102_rw_obligant;
	private BigDecimal	r102_rav;
	private String	r103_product;
	private String	r103_client_grp;
	private BigDecimal	r103_total_book_expo;
	private BigDecimal	r103_margin_pro;
	private BigDecimal	r103_book_expo;
	private BigDecimal	r103_ccf_cont;
	private BigDecimal	r103_equiv_value;
	private BigDecimal	r103_rw_obligant;
	private BigDecimal	r103_rav;
	private String	r104_product;
	private String	r104_client_grp;
	private BigDecimal	r104_total_book_expo;
	private BigDecimal	r104_margin_pro;
	private BigDecimal	r104_book_expo;
	private BigDecimal	r104_ccf_cont;
	private BigDecimal	r104_equiv_value;
	private BigDecimal	r104_rw_obligant;
	private BigDecimal	r104_rav;
	private String	r105_product;
	private String	r105_client_grp;
	private BigDecimal	r105_total_book_expo;
	private BigDecimal	r105_margin_pro;
	private BigDecimal	r105_book_expo;
	private BigDecimal	r105_ccf_cont;
	private BigDecimal	r105_equiv_value;
	private BigDecimal	r105_rw_obligant;
	private BigDecimal	r105_rav;
	private String	r106_product;
	private String	r106_client_grp;
	private BigDecimal	r106_total_book_expo;
	private BigDecimal	r106_margin_pro;
	private BigDecimal	r106_book_expo;
	private BigDecimal	r106_ccf_cont;
	private BigDecimal	r106_equiv_value;
	private BigDecimal	r106_rw_obligant;
	private BigDecimal	r106_rav;
	private String	r107_product;
	private String	r107_client_grp;
	private BigDecimal	r107_total_book_expo;
	private BigDecimal	r107_margin_pro;
	private BigDecimal	r107_book_expo;
	private BigDecimal	r107_ccf_cont;
	private BigDecimal	r107_equiv_value;
	private BigDecimal	r107_rw_obligant;
	private BigDecimal	r107_rav;
	private String	r108_product;
	private String	r108_client_grp;
	private BigDecimal	r108_total_book_expo;
	private BigDecimal	r108_margin_pro;
	private BigDecimal	r108_book_expo;
	private BigDecimal	r108_ccf_cont;
	private BigDecimal	r108_equiv_value;
	private BigDecimal	r108_rw_obligant;
	private BigDecimal	r108_rav;
	private String	r109_product;
	private String	r109_client_grp;
	private BigDecimal	r109_total_book_expo;
	private BigDecimal	r109_margin_pro;
	private BigDecimal	r109_book_expo;
	private BigDecimal	r109_ccf_cont;
	private BigDecimal	r109_equiv_value;
	private BigDecimal	r109_rw_obligant;
	private BigDecimal	r109_rav;
	private String	r110_product;
	private String	r110_client_grp;
	private BigDecimal	r110_total_book_expo;
	private BigDecimal	r110_margin_pro;
	private BigDecimal	r110_book_expo;
	private BigDecimal	r110_ccf_cont;
	private BigDecimal	r110_equiv_value;
	private BigDecimal	r110_rw_obligant;
	private BigDecimal	r110_rav;
	private String	r111_product;
	private String	r111_client_grp;
	private BigDecimal	r111_total_book_expo;
	private BigDecimal	r111_margin_pro;
	private BigDecimal	r111_book_expo;
	private BigDecimal	r111_ccf_cont;
	private BigDecimal	r111_equiv_value;
	private BigDecimal	r111_rw_obligant;
	private BigDecimal	r111_rav;
	private String	r112_product;
	private String	r112_client_grp;
	private BigDecimal	r112_total_book_expo;
	private BigDecimal	r112_margin_pro;
	private BigDecimal	r112_book_expo;
	private BigDecimal	r112_ccf_cont;
	private BigDecimal	r112_equiv_value;
	private BigDecimal	r112_rw_obligant;
	private BigDecimal	r112_rav;
	private String	r113_product;
	private String	r113_client_grp;
	private BigDecimal	r113_total_book_expo;
	private BigDecimal	r113_margin_pro;
	private BigDecimal	r113_book_expo;
	private BigDecimal	r113_ccf_cont;
	private BigDecimal	r113_equiv_value;
	private BigDecimal	r113_rw_obligant;
	private BigDecimal	r113_rav;
	private String	r114_product;
	private String	r114_client_grp;
	private BigDecimal	r114_total_book_expo;
	private BigDecimal	r114_margin_pro;
	private BigDecimal	r114_book_expo;
	private BigDecimal	r114_ccf_cont;
	private BigDecimal	r114_equiv_value;
	private BigDecimal	r114_rw_obligant;
	private BigDecimal	r114_rav;
	private String	r115_product;
	private String	r115_client_grp;
	private BigDecimal	r115_total_book_expo;
	private BigDecimal	r115_margin_pro;
	private BigDecimal	r115_book_expo;
	private BigDecimal	r115_ccf_cont;
	private BigDecimal	r115_equiv_value;
	private BigDecimal	r115_rw_obligant;
	private BigDecimal	r115_rav;
	private String	r116_product;
	private String	r116_client_grp;
	private BigDecimal	r116_total_book_expo;
	private BigDecimal	r116_margin_pro;
	private BigDecimal	r116_book_expo;
	private BigDecimal	r116_ccf_cont;
	private BigDecimal	r116_equiv_value;
	private BigDecimal	r116_rw_obligant;
	private BigDecimal	r116_rav;
	private String	r117_product;
	private String	r117_client_grp;
	private BigDecimal	r117_total_book_expo;
	private BigDecimal	r117_margin_pro;
	private BigDecimal	r117_book_expo;
	private BigDecimal	r117_ccf_cont;
	private BigDecimal	r117_equiv_value;
	private BigDecimal	r117_rw_obligant;
	private BigDecimal	r117_rav;
	private String	r118_product;
	private String	r118_client_grp;
	private BigDecimal	r118_total_book_expo;
	private BigDecimal	r118_margin_pro;
	private BigDecimal	r118_book_expo;
	private BigDecimal	r118_ccf_cont;
	private BigDecimal	r118_equiv_value;
	private BigDecimal	r118_rw_obligant;
	private BigDecimal	r118_rav;
	private String	r119_product;
	private String	r119_client_grp;
	private BigDecimal	r119_total_book_expo;
	private BigDecimal	r119_margin_pro;
	private BigDecimal	r119_book_expo;
	private BigDecimal	r119_ccf_cont;
	private BigDecimal	r119_equiv_value;
	private BigDecimal	r119_rw_obligant;
	private BigDecimal	r119_rav;
	private String	r120_product;
	private String	r120_client_grp;
	private BigDecimal	r120_total_book_expo;
	private BigDecimal	r120_margin_pro;
	private BigDecimal	r120_book_expo;
	private BigDecimal	r120_ccf_cont;
	private BigDecimal	r120_equiv_value;
	private BigDecimal	r120_rw_obligant;
	private BigDecimal	r120_rav;
	private String	r121_product;
	private String	r121_client_grp;
	private BigDecimal	r121_total_book_expo;
	private BigDecimal	r121_margin_pro;
	private BigDecimal	r121_book_expo;
	private BigDecimal	r121_ccf_cont;
	private BigDecimal	r121_equiv_value;
	private BigDecimal	r121_rw_obligant;
	private BigDecimal	r121_rav;
	private String	r122_product;
	private String	r122_client_grp;
	private BigDecimal	r122_total_book_expo;
	private BigDecimal	r122_margin_pro;
	private BigDecimal	r122_book_expo;
	private BigDecimal	r122_ccf_cont;
	private BigDecimal	r122_equiv_value;
	private BigDecimal	r122_rw_obligant;
	private BigDecimal	r122_rav;
	private String	r123_product;
	private String	r123_client_grp;
	private BigDecimal	r123_total_book_expo;
	private BigDecimal	r123_margin_pro;
	private BigDecimal	r123_book_expo;
	private BigDecimal	r123_ccf_cont;
	private BigDecimal	r123_equiv_value;
	private BigDecimal	r123_rw_obligant;
	private BigDecimal	r123_rav;
	private String	r124_product;
	private String	r124_client_grp;
	private BigDecimal	r124_total_book_expo;
	private BigDecimal	r124_margin_pro;
	private BigDecimal	r124_book_expo;
	private BigDecimal	r124_ccf_cont;
	private BigDecimal	r124_equiv_value;
	private BigDecimal	r124_rw_obligant;
	private BigDecimal	r124_rav;
	private String	r125_product;
	private String	r125_client_grp;
	private BigDecimal	r125_total_book_expo;
	private BigDecimal	r125_margin_pro;
	private BigDecimal	r125_book_expo;
	private BigDecimal	r125_ccf_cont;
	private BigDecimal	r125_equiv_value;
	private BigDecimal	r125_rw_obligant;
	private BigDecimal	r125_rav;
	private String	r126_product;
	private String	r126_client_grp;
	private BigDecimal	r126_total_book_expo;
	private BigDecimal	r126_margin_pro;
	private BigDecimal	r126_book_expo;
	private BigDecimal	r126_ccf_cont;
	private BigDecimal	r126_equiv_value;
	private BigDecimal	r126_rw_obligant;
	private BigDecimal	r126_rav;
	private String	r127_product;
	private String	r127_client_grp;
	private BigDecimal	r127_total_book_expo;
	private BigDecimal	r127_margin_pro;
	private BigDecimal	r127_book_expo;
	private BigDecimal	r127_ccf_cont;
	private BigDecimal	r127_equiv_value;
	private BigDecimal	r127_rw_obligant;
	private BigDecimal	r127_rav;
	private String	r128_product;
	private String	r128_client_grp;
	private BigDecimal	r128_total_book_expo;
	private BigDecimal	r128_margin_pro;
	private BigDecimal	r128_book_expo;
	private BigDecimal	r128_ccf_cont;
	private BigDecimal	r128_equiv_value;
	private BigDecimal	r128_rw_obligant;
	private BigDecimal	r128_rav;
	private String	r129_product;
	private String	r129_client_grp;
	private BigDecimal	r129_total_book_expo;
	private BigDecimal	r129_margin_pro;
	private BigDecimal	r129_book_expo;
	private BigDecimal	r129_ccf_cont;
	private BigDecimal	r129_equiv_value;
	private BigDecimal	r129_rw_obligant;
	private BigDecimal	r129_rav;
	private String	r130_product;
	private String	r130_client_grp;
	private BigDecimal	r130_total_book_expo;
	private BigDecimal	r130_margin_pro;
	private BigDecimal	r130_book_expo;
	private BigDecimal	r130_ccf_cont;
	private BigDecimal	r130_equiv_value;
	private BigDecimal	r130_rw_obligant;
	private BigDecimal	r130_rav;
	private String	r131_product;
	private String	r131_client_grp;
	private BigDecimal	r131_total_book_expo;
	private BigDecimal	r131_margin_pro;
	private BigDecimal	r131_book_expo;
	private BigDecimal	r131_ccf_cont;
	private BigDecimal	r131_equiv_value;
	private BigDecimal	r131_rw_obligant;
	private BigDecimal	r131_rav;
	private String	r132_product;
	private String	r132_client_grp;
	private BigDecimal	r132_total_book_expo;
	private BigDecimal	r132_margin_pro;
	private BigDecimal	r132_book_expo;
	private BigDecimal	r132_ccf_cont;
	private BigDecimal	r132_equiv_value;
	private BigDecimal	r132_rw_obligant;
	private BigDecimal	r132_rav;
	private String	r133_product;
	private String	r133_client_grp;
	private BigDecimal	r133_total_book_expo;
	private BigDecimal	r133_margin_pro;
	private BigDecimal	r133_book_expo;
	private BigDecimal	r133_ccf_cont;
	private BigDecimal	r133_equiv_value;
	private BigDecimal	r133_rw_obligant;
	private BigDecimal	r133_rav;
	private String	r134_product;
	private String	r134_client_grp;
	private BigDecimal	r134_total_book_expo;
	private BigDecimal	r134_margin_pro;
	private BigDecimal	r134_book_expo;
	private BigDecimal	r134_ccf_cont;
	private BigDecimal	r134_equiv_value;
	private BigDecimal	r134_rw_obligant;
	private BigDecimal	r134_rav;
	private String	r148_product;
	private String	r148_client_grp;
	private BigDecimal	r148_total_book_expo;
	private BigDecimal	r148_margin_pro;
	private BigDecimal	r148_book_expo;
	private BigDecimal	r148_ccf_cont;
	private BigDecimal	r148_equiv_value;
	private BigDecimal	r148_rw_obligant;
	private BigDecimal	r148_rav;
	private String	r149_product;
	private String	r149_client_grp;
	private BigDecimal	r149_total_book_expo;
	private BigDecimal	r149_margin_pro;
	private BigDecimal	r149_book_expo;
	private BigDecimal	r149_ccf_cont;
	private BigDecimal	r149_equiv_value;
	private BigDecimal	r149_rw_obligant;
	private BigDecimal	r149_rav;
	private String	r150_product;
	private String	r150_client_grp;
	private BigDecimal	r150_total_book_expo;
	private BigDecimal	r150_margin_pro;
	private BigDecimal	r150_book_expo;
	private BigDecimal	r150_ccf_cont;
	private BigDecimal	r150_equiv_value;
	private BigDecimal	r150_rw_obligant;
	private BigDecimal	r150_rav;

	

		
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
		public String getR12_product() {
			return r12_product;
		}
		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}
		public String getR12_client_grp() {
			return r12_client_grp;
		}
		public void setR12_client_grp(String r12_client_grp) {
			this.r12_client_grp = r12_client_grp;
		}
		public BigDecimal getR12_total_book_expo() {
			return r12_total_book_expo;
		}
		public void setR12_total_book_expo(BigDecimal r12_total_book_expo) {
			this.r12_total_book_expo = r12_total_book_expo;
		}
		public BigDecimal getR12_margin_pro() {
			return r12_margin_pro;
		}
		public void setR12_margin_pro(BigDecimal r12_margin_pro) {
			this.r12_margin_pro = r12_margin_pro;
		}
		public BigDecimal getR12_book_expo() {
			return r12_book_expo;
		}
		public void setR12_book_expo(BigDecimal r12_book_expo) {
			this.r12_book_expo = r12_book_expo;
		}
		public BigDecimal getR12_ccf_cont() {
			return r12_ccf_cont;
		}
		public void setR12_ccf_cont(BigDecimal r12_ccf_cont) {
			this.r12_ccf_cont = r12_ccf_cont;
		}
		public BigDecimal getR12_equiv_value() {
			return r12_equiv_value;
		}
		public void setR12_equiv_value(BigDecimal r12_equiv_value) {
			this.r12_equiv_value = r12_equiv_value;
		}
		public BigDecimal getR12_rw_obligant() {
			return r12_rw_obligant;
		}
		public void setR12_rw_obligant(BigDecimal r12_rw_obligant) {
			this.r12_rw_obligant = r12_rw_obligant;
		}
		public BigDecimal getR12_rav() {
			return r12_rav;
		}
		public void setR12_rav(BigDecimal r12_rav) {
			this.r12_rav = r12_rav;
		}
		public String getR13_product() {
			return r13_product;
		}
		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}
		public String getR13_client_grp() {
			return r13_client_grp;
		}
		public void setR13_client_grp(String r13_client_grp) {
			this.r13_client_grp = r13_client_grp;
		}
		public BigDecimal getR13_total_book_expo() {
			return r13_total_book_expo;
		}
		public void setR13_total_book_expo(BigDecimal r13_total_book_expo) {
			this.r13_total_book_expo = r13_total_book_expo;
		}
		public BigDecimal getR13_margin_pro() {
			return r13_margin_pro;
		}
		public void setR13_margin_pro(BigDecimal r13_margin_pro) {
			this.r13_margin_pro = r13_margin_pro;
		}
		public BigDecimal getR13_book_expo() {
			return r13_book_expo;
		}
		public void setR13_book_expo(BigDecimal r13_book_expo) {
			this.r13_book_expo = r13_book_expo;
		}
		public BigDecimal getR13_ccf_cont() {
			return r13_ccf_cont;
		}
		public void setR13_ccf_cont(BigDecimal r13_ccf_cont) {
			this.r13_ccf_cont = r13_ccf_cont;
		}
		public BigDecimal getR13_equiv_value() {
			return r13_equiv_value;
		}
		public void setR13_equiv_value(BigDecimal r13_equiv_value) {
			this.r13_equiv_value = r13_equiv_value;
		}
		public BigDecimal getR13_rw_obligant() {
			return r13_rw_obligant;
		}
		public void setR13_rw_obligant(BigDecimal r13_rw_obligant) {
			this.r13_rw_obligant = r13_rw_obligant;
		}
		public BigDecimal getR13_rav() {
			return r13_rav;
		}
		public void setR13_rav(BigDecimal r13_rav) {
			this.r13_rav = r13_rav;
		}
		public String getR14_product() {
			return r14_product;
		}
		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}
		public String getR14_client_grp() {
			return r14_client_grp;
		}
		public void setR14_client_grp(String r14_client_grp) {
			this.r14_client_grp = r14_client_grp;
		}
		public BigDecimal getR14_total_book_expo() {
			return r14_total_book_expo;
		}
		public void setR14_total_book_expo(BigDecimal r14_total_book_expo) {
			this.r14_total_book_expo = r14_total_book_expo;
		}
		public BigDecimal getR14_margin_pro() {
			return r14_margin_pro;
		}
		public void setR14_margin_pro(BigDecimal r14_margin_pro) {
			this.r14_margin_pro = r14_margin_pro;
		}
		public BigDecimal getR14_book_expo() {
			return r14_book_expo;
		}
		public void setR14_book_expo(BigDecimal r14_book_expo) {
			this.r14_book_expo = r14_book_expo;
		}
		public BigDecimal getR14_ccf_cont() {
			return r14_ccf_cont;
		}
		public void setR14_ccf_cont(BigDecimal r14_ccf_cont) {
			this.r14_ccf_cont = r14_ccf_cont;
		}
		public BigDecimal getR14_equiv_value() {
			return r14_equiv_value;
		}
		public void setR14_equiv_value(BigDecimal r14_equiv_value) {
			this.r14_equiv_value = r14_equiv_value;
		}
		public BigDecimal getR14_rw_obligant() {
			return r14_rw_obligant;
		}
		public void setR14_rw_obligant(BigDecimal r14_rw_obligant) {
			this.r14_rw_obligant = r14_rw_obligant;
		}
		public BigDecimal getR14_rav() {
			return r14_rav;
		}
		public void setR14_rav(BigDecimal r14_rav) {
			this.r14_rav = r14_rav;
		}
		public String getR15_product() {
			return r15_product;
		}
		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}
		public String getR15_client_grp() {
			return r15_client_grp;
		}
		public void setR15_client_grp(String r15_client_grp) {
			this.r15_client_grp = r15_client_grp;
		}
		public BigDecimal getR15_total_book_expo() {
			return r15_total_book_expo;
		}
		public void setR15_total_book_expo(BigDecimal r15_total_book_expo) {
			this.r15_total_book_expo = r15_total_book_expo;
		}
		public BigDecimal getR15_margin_pro() {
			return r15_margin_pro;
		}
		public void setR15_margin_pro(BigDecimal r15_margin_pro) {
			this.r15_margin_pro = r15_margin_pro;
		}
		public BigDecimal getR15_book_expo() {
			return r15_book_expo;
		}
		public void setR15_book_expo(BigDecimal r15_book_expo) {
			this.r15_book_expo = r15_book_expo;
		}
		public BigDecimal getR15_ccf_cont() {
			return r15_ccf_cont;
		}
		public void setR15_ccf_cont(BigDecimal r15_ccf_cont) {
			this.r15_ccf_cont = r15_ccf_cont;
		}
		public BigDecimal getR15_equiv_value() {
			return r15_equiv_value;
		}
		public void setR15_equiv_value(BigDecimal r15_equiv_value) {
			this.r15_equiv_value = r15_equiv_value;
		}
		public BigDecimal getR15_rw_obligant() {
			return r15_rw_obligant;
		}
		public void setR15_rw_obligant(BigDecimal r15_rw_obligant) {
			this.r15_rw_obligant = r15_rw_obligant;
		}
		public BigDecimal getR15_rav() {
			return r15_rav;
		}
		public void setR15_rav(BigDecimal r15_rav) {
			this.r15_rav = r15_rav;
		}
		public String getR16_product() {
			return r16_product;
		}
		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}
		public String getR16_client_grp() {
			return r16_client_grp;
		}
		public void setR16_client_grp(String r16_client_grp) {
			this.r16_client_grp = r16_client_grp;
		}
		public BigDecimal getR16_total_book_expo() {
			return r16_total_book_expo;
		}
		public void setR16_total_book_expo(BigDecimal r16_total_book_expo) {
			this.r16_total_book_expo = r16_total_book_expo;
		}
		public BigDecimal getR16_margin_pro() {
			return r16_margin_pro;
		}
		public void setR16_margin_pro(BigDecimal r16_margin_pro) {
			this.r16_margin_pro = r16_margin_pro;
		}
		public BigDecimal getR16_book_expo() {
			return r16_book_expo;
		}
		public void setR16_book_expo(BigDecimal r16_book_expo) {
			this.r16_book_expo = r16_book_expo;
		}
		public BigDecimal getR16_ccf_cont() {
			return r16_ccf_cont;
		}
		public void setR16_ccf_cont(BigDecimal r16_ccf_cont) {
			this.r16_ccf_cont = r16_ccf_cont;
		}
		public BigDecimal getR16_equiv_value() {
			return r16_equiv_value;
		}
		public void setR16_equiv_value(BigDecimal r16_equiv_value) {
			this.r16_equiv_value = r16_equiv_value;
		}
		public BigDecimal getR16_rw_obligant() {
			return r16_rw_obligant;
		}
		public void setR16_rw_obligant(BigDecimal r16_rw_obligant) {
			this.r16_rw_obligant = r16_rw_obligant;
		}
		public BigDecimal getR16_rav() {
			return r16_rav;
		}
		public void setR16_rav(BigDecimal r16_rav) {
			this.r16_rav = r16_rav;
		}
		public String getR17_product() {
			return r17_product;
		}
		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}
		public String getR17_client_grp() {
			return r17_client_grp;
		}
		public void setR17_client_grp(String r17_client_grp) {
			this.r17_client_grp = r17_client_grp;
		}
		public BigDecimal getR17_total_book_expo() {
			return r17_total_book_expo;
		}
		public void setR17_total_book_expo(BigDecimal r17_total_book_expo) {
			this.r17_total_book_expo = r17_total_book_expo;
		}
		public BigDecimal getR17_margin_pro() {
			return r17_margin_pro;
		}
		public void setR17_margin_pro(BigDecimal r17_margin_pro) {
			this.r17_margin_pro = r17_margin_pro;
		}
		public BigDecimal getR17_book_expo() {
			return r17_book_expo;
		}
		public void setR17_book_expo(BigDecimal r17_book_expo) {
			this.r17_book_expo = r17_book_expo;
		}
		public BigDecimal getR17_ccf_cont() {
			return r17_ccf_cont;
		}
		public void setR17_ccf_cont(BigDecimal r17_ccf_cont) {
			this.r17_ccf_cont = r17_ccf_cont;
		}
		public BigDecimal getR17_equiv_value() {
			return r17_equiv_value;
		}
		public void setR17_equiv_value(BigDecimal r17_equiv_value) {
			this.r17_equiv_value = r17_equiv_value;
		}
		public BigDecimal getR17_rw_obligant() {
			return r17_rw_obligant;
		}
		public void setR17_rw_obligant(BigDecimal r17_rw_obligant) {
			this.r17_rw_obligant = r17_rw_obligant;
		}
		public BigDecimal getR17_rav() {
			return r17_rav;
		}
		public void setR17_rav(BigDecimal r17_rav) {
			this.r17_rav = r17_rav;
		}
		public String getR18_product() {
			return r18_product;
		}
		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}
		public String getR18_client_grp() {
			return r18_client_grp;
		}
		public void setR18_client_grp(String r18_client_grp) {
			this.r18_client_grp = r18_client_grp;
		}
		public BigDecimal getR18_total_book_expo() {
			return r18_total_book_expo;
		}
		public void setR18_total_book_expo(BigDecimal r18_total_book_expo) {
			this.r18_total_book_expo = r18_total_book_expo;
		}
		public BigDecimal getR18_margin_pro() {
			return r18_margin_pro;
		}
		public void setR18_margin_pro(BigDecimal r18_margin_pro) {
			this.r18_margin_pro = r18_margin_pro;
		}
		public BigDecimal getR18_book_expo() {
			return r18_book_expo;
		}
		public void setR18_book_expo(BigDecimal r18_book_expo) {
			this.r18_book_expo = r18_book_expo;
		}
		public BigDecimal getR18_ccf_cont() {
			return r18_ccf_cont;
		}
		public void setR18_ccf_cont(BigDecimal r18_ccf_cont) {
			this.r18_ccf_cont = r18_ccf_cont;
		}
		public BigDecimal getR18_equiv_value() {
			return r18_equiv_value;
		}
		public void setR18_equiv_value(BigDecimal r18_equiv_value) {
			this.r18_equiv_value = r18_equiv_value;
		}
		public BigDecimal getR18_rw_obligant() {
			return r18_rw_obligant;
		}
		public void setR18_rw_obligant(BigDecimal r18_rw_obligant) {
			this.r18_rw_obligant = r18_rw_obligant;
		}
		public BigDecimal getR18_rav() {
			return r18_rav;
		}
		public void setR18_rav(BigDecimal r18_rav) {
			this.r18_rav = r18_rav;
		}
		public String getR19_product() {
			return r19_product;
		}
		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}
		public String getR19_client_grp() {
			return r19_client_grp;
		}
		public void setR19_client_grp(String r19_client_grp) {
			this.r19_client_grp = r19_client_grp;
		}
		public BigDecimal getR19_total_book_expo() {
			return r19_total_book_expo;
		}
		public void setR19_total_book_expo(BigDecimal r19_total_book_expo) {
			this.r19_total_book_expo = r19_total_book_expo;
		}
		public BigDecimal getR19_margin_pro() {
			return r19_margin_pro;
		}
		public void setR19_margin_pro(BigDecimal r19_margin_pro) {
			this.r19_margin_pro = r19_margin_pro;
		}
		public BigDecimal getR19_book_expo() {
			return r19_book_expo;
		}
		public void setR19_book_expo(BigDecimal r19_book_expo) {
			this.r19_book_expo = r19_book_expo;
		}
		public BigDecimal getR19_ccf_cont() {
			return r19_ccf_cont;
		}
		public void setR19_ccf_cont(BigDecimal r19_ccf_cont) {
			this.r19_ccf_cont = r19_ccf_cont;
		}
		public BigDecimal getR19_equiv_value() {
			return r19_equiv_value;
		}
		public void setR19_equiv_value(BigDecimal r19_equiv_value) {
			this.r19_equiv_value = r19_equiv_value;
		}
		public BigDecimal getR19_rw_obligant() {
			return r19_rw_obligant;
		}
		public void setR19_rw_obligant(BigDecimal r19_rw_obligant) {
			this.r19_rw_obligant = r19_rw_obligant;
		}
		public BigDecimal getR19_rav() {
			return r19_rav;
		}
		public void setR19_rav(BigDecimal r19_rav) {
			this.r19_rav = r19_rav;
		}
		public String getR20_product() {
			return r20_product;
		}
		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}
		public String getR20_client_grp() {
			return r20_client_grp;
		}
		public void setR20_client_grp(String r20_client_grp) {
			this.r20_client_grp = r20_client_grp;
		}
		public BigDecimal getR20_total_book_expo() {
			return r20_total_book_expo;
		}
		public void setR20_total_book_expo(BigDecimal r20_total_book_expo) {
			this.r20_total_book_expo = r20_total_book_expo;
		}
		public BigDecimal getR20_margin_pro() {
			return r20_margin_pro;
		}
		public void setR20_margin_pro(BigDecimal r20_margin_pro) {
			this.r20_margin_pro = r20_margin_pro;
		}
		public BigDecimal getR20_book_expo() {
			return r20_book_expo;
		}
		public void setR20_book_expo(BigDecimal r20_book_expo) {
			this.r20_book_expo = r20_book_expo;
		}
		public BigDecimal getR20_ccf_cont() {
			return r20_ccf_cont;
		}
		public void setR20_ccf_cont(BigDecimal r20_ccf_cont) {
			this.r20_ccf_cont = r20_ccf_cont;
		}
		public BigDecimal getR20_equiv_value() {
			return r20_equiv_value;
		}
		public void setR20_equiv_value(BigDecimal r20_equiv_value) {
			this.r20_equiv_value = r20_equiv_value;
		}
		public BigDecimal getR20_rw_obligant() {
			return r20_rw_obligant;
		}
		public void setR20_rw_obligant(BigDecimal r20_rw_obligant) {
			this.r20_rw_obligant = r20_rw_obligant;
		}
		public BigDecimal getR20_rav() {
			return r20_rav;
		}
		public void setR20_rav(BigDecimal r20_rav) {
			this.r20_rav = r20_rav;
		}
		public String getR21_product() {
			return r21_product;
		}
		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}
		public String getR21_client_grp() {
			return r21_client_grp;
		}
		public void setR21_client_grp(String r21_client_grp) {
			this.r21_client_grp = r21_client_grp;
		}
		public BigDecimal getR21_total_book_expo() {
			return r21_total_book_expo;
		}
		public void setR21_total_book_expo(BigDecimal r21_total_book_expo) {
			this.r21_total_book_expo = r21_total_book_expo;
		}
		public BigDecimal getR21_margin_pro() {
			return r21_margin_pro;
		}
		public void setR21_margin_pro(BigDecimal r21_margin_pro) {
			this.r21_margin_pro = r21_margin_pro;
		}
		public BigDecimal getR21_book_expo() {
			return r21_book_expo;
		}
		public void setR21_book_expo(BigDecimal r21_book_expo) {
			this.r21_book_expo = r21_book_expo;
		}
		public BigDecimal getR21_ccf_cont() {
			return r21_ccf_cont;
		}
		public void setR21_ccf_cont(BigDecimal r21_ccf_cont) {
			this.r21_ccf_cont = r21_ccf_cont;
		}
		public BigDecimal getR21_equiv_value() {
			return r21_equiv_value;
		}
		public void setR21_equiv_value(BigDecimal r21_equiv_value) {
			this.r21_equiv_value = r21_equiv_value;
		}
		public BigDecimal getR21_rw_obligant() {
			return r21_rw_obligant;
		}
		public void setR21_rw_obligant(BigDecimal r21_rw_obligant) {
			this.r21_rw_obligant = r21_rw_obligant;
		}
		public BigDecimal getR21_rav() {
			return r21_rav;
		}
		public void setR21_rav(BigDecimal r21_rav) {
			this.r21_rav = r21_rav;
		}
		public String getR22_product() {
			return r22_product;
		}
		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}
		public String getR22_client_grp() {
			return r22_client_grp;
		}
		public void setR22_client_grp(String r22_client_grp) {
			this.r22_client_grp = r22_client_grp;
		}
		public BigDecimal getR22_total_book_expo() {
			return r22_total_book_expo;
		}
		public void setR22_total_book_expo(BigDecimal r22_total_book_expo) {
			this.r22_total_book_expo = r22_total_book_expo;
		}
		public BigDecimal getR22_margin_pro() {
			return r22_margin_pro;
		}
		public void setR22_margin_pro(BigDecimal r22_margin_pro) {
			this.r22_margin_pro = r22_margin_pro;
		}
		public BigDecimal getR22_book_expo() {
			return r22_book_expo;
		}
		public void setR22_book_expo(BigDecimal r22_book_expo) {
			this.r22_book_expo = r22_book_expo;
		}
		public BigDecimal getR22_ccf_cont() {
			return r22_ccf_cont;
		}
		public void setR22_ccf_cont(BigDecimal r22_ccf_cont) {
			this.r22_ccf_cont = r22_ccf_cont;
		}
		public BigDecimal getR22_equiv_value() {
			return r22_equiv_value;
		}
		public void setR22_equiv_value(BigDecimal r22_equiv_value) {
			this.r22_equiv_value = r22_equiv_value;
		}
		public BigDecimal getR22_rw_obligant() {
			return r22_rw_obligant;
		}
		public void setR22_rw_obligant(BigDecimal r22_rw_obligant) {
			this.r22_rw_obligant = r22_rw_obligant;
		}
		public BigDecimal getR22_rav() {
			return r22_rav;
		}
		public void setR22_rav(BigDecimal r22_rav) {
			this.r22_rav = r22_rav;
		}
		public String getR23_product() {
			return r23_product;
		}
		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}
		public String getR23_client_grp() {
			return r23_client_grp;
		}
		public void setR23_client_grp(String r23_client_grp) {
			this.r23_client_grp = r23_client_grp;
		}
		public BigDecimal getR23_total_book_expo() {
			return r23_total_book_expo;
		}
		public void setR23_total_book_expo(BigDecimal r23_total_book_expo) {
			this.r23_total_book_expo = r23_total_book_expo;
		}
		public BigDecimal getR23_margin_pro() {
			return r23_margin_pro;
		}
		public void setR23_margin_pro(BigDecimal r23_margin_pro) {
			this.r23_margin_pro = r23_margin_pro;
		}
		public BigDecimal getR23_book_expo() {
			return r23_book_expo;
		}
		public void setR23_book_expo(BigDecimal r23_book_expo) {
			this.r23_book_expo = r23_book_expo;
		}
		public BigDecimal getR23_ccf_cont() {
			return r23_ccf_cont;
		}
		public void setR23_ccf_cont(BigDecimal r23_ccf_cont) {
			this.r23_ccf_cont = r23_ccf_cont;
		}
		public BigDecimal getR23_equiv_value() {
			return r23_equiv_value;
		}
		public void setR23_equiv_value(BigDecimal r23_equiv_value) {
			this.r23_equiv_value = r23_equiv_value;
		}
		public BigDecimal getR23_rw_obligant() {
			return r23_rw_obligant;
		}
		public void setR23_rw_obligant(BigDecimal r23_rw_obligant) {
			this.r23_rw_obligant = r23_rw_obligant;
		}
		public BigDecimal getR23_rav() {
			return r23_rav;
		}
		public void setR23_rav(BigDecimal r23_rav) {
			this.r23_rav = r23_rav;
		}
		public String getR24_product() {
			return r24_product;
		}
		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}
		public String getR24_client_grp() {
			return r24_client_grp;
		}
		public void setR24_client_grp(String r24_client_grp) {
			this.r24_client_grp = r24_client_grp;
		}
		public BigDecimal getR24_total_book_expo() {
			return r24_total_book_expo;
		}
		public void setR24_total_book_expo(BigDecimal r24_total_book_expo) {
			this.r24_total_book_expo = r24_total_book_expo;
		}
		public BigDecimal getR24_margin_pro() {
			return r24_margin_pro;
		}
		public void setR24_margin_pro(BigDecimal r24_margin_pro) {
			this.r24_margin_pro = r24_margin_pro;
		}
		public BigDecimal getR24_book_expo() {
			return r24_book_expo;
		}
		public void setR24_book_expo(BigDecimal r24_book_expo) {
			this.r24_book_expo = r24_book_expo;
		}
		public BigDecimal getR24_ccf_cont() {
			return r24_ccf_cont;
		}
		public void setR24_ccf_cont(BigDecimal r24_ccf_cont) {
			this.r24_ccf_cont = r24_ccf_cont;
		}
		public BigDecimal getR24_equiv_value() {
			return r24_equiv_value;
		}
		public void setR24_equiv_value(BigDecimal r24_equiv_value) {
			this.r24_equiv_value = r24_equiv_value;
		}
		public BigDecimal getR24_rw_obligant() {
			return r24_rw_obligant;
		}
		public void setR24_rw_obligant(BigDecimal r24_rw_obligant) {
			this.r24_rw_obligant = r24_rw_obligant;
		}
		public BigDecimal getR24_rav() {
			return r24_rav;
		}
		public void setR24_rav(BigDecimal r24_rav) {
			this.r24_rav = r24_rav;
		}
		public String getR25_product() {
			return r25_product;
		}
		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}
		public String getR25_client_grp() {
			return r25_client_grp;
		}
		public void setR25_client_grp(String r25_client_grp) {
			this.r25_client_grp = r25_client_grp;
		}
		public BigDecimal getR25_total_book_expo() {
			return r25_total_book_expo;
		}
		public void setR25_total_book_expo(BigDecimal r25_total_book_expo) {
			this.r25_total_book_expo = r25_total_book_expo;
		}
		public BigDecimal getR25_margin_pro() {
			return r25_margin_pro;
		}
		public void setR25_margin_pro(BigDecimal r25_margin_pro) {
			this.r25_margin_pro = r25_margin_pro;
		}
		public BigDecimal getR25_book_expo() {
			return r25_book_expo;
		}
		public void setR25_book_expo(BigDecimal r25_book_expo) {
			this.r25_book_expo = r25_book_expo;
		}
		public BigDecimal getR25_ccf_cont() {
			return r25_ccf_cont;
		}
		public void setR25_ccf_cont(BigDecimal r25_ccf_cont) {
			this.r25_ccf_cont = r25_ccf_cont;
		}
		public BigDecimal getR25_equiv_value() {
			return r25_equiv_value;
		}
		public void setR25_equiv_value(BigDecimal r25_equiv_value) {
			this.r25_equiv_value = r25_equiv_value;
		}
		public BigDecimal getR25_rw_obligant() {
			return r25_rw_obligant;
		}
		public void setR25_rw_obligant(BigDecimal r25_rw_obligant) {
			this.r25_rw_obligant = r25_rw_obligant;
		}
		public BigDecimal getR25_rav() {
			return r25_rav;
		}
		public void setR25_rav(BigDecimal r25_rav) {
			this.r25_rav = r25_rav;
		}
		public String getR26_product() {
			return r26_product;
		}
		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}
		public String getR26_client_grp() {
			return r26_client_grp;
		}
		public void setR26_client_grp(String r26_client_grp) {
			this.r26_client_grp = r26_client_grp;
		}
		public BigDecimal getR26_total_book_expo() {
			return r26_total_book_expo;
		}
		public void setR26_total_book_expo(BigDecimal r26_total_book_expo) {
			this.r26_total_book_expo = r26_total_book_expo;
		}
		public BigDecimal getR26_margin_pro() {
			return r26_margin_pro;
		}
		public void setR26_margin_pro(BigDecimal r26_margin_pro) {
			this.r26_margin_pro = r26_margin_pro;
		}
		public BigDecimal getR26_book_expo() {
			return r26_book_expo;
		}
		public void setR26_book_expo(BigDecimal r26_book_expo) {
			this.r26_book_expo = r26_book_expo;
		}
		public BigDecimal getR26_ccf_cont() {
			return r26_ccf_cont;
		}
		public void setR26_ccf_cont(BigDecimal r26_ccf_cont) {
			this.r26_ccf_cont = r26_ccf_cont;
		}
		public BigDecimal getR26_equiv_value() {
			return r26_equiv_value;
		}
		public void setR26_equiv_value(BigDecimal r26_equiv_value) {
			this.r26_equiv_value = r26_equiv_value;
		}
		public BigDecimal getR26_rw_obligant() {
			return r26_rw_obligant;
		}
		public void setR26_rw_obligant(BigDecimal r26_rw_obligant) {
			this.r26_rw_obligant = r26_rw_obligant;
		}
		public BigDecimal getR26_rav() {
			return r26_rav;
		}
		public void setR26_rav(BigDecimal r26_rav) {
			this.r26_rav = r26_rav;
		}
		public String getR27_product() {
			return r27_product;
		}
		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}
		public String getR27_client_grp() {
			return r27_client_grp;
		}
		public void setR27_client_grp(String r27_client_grp) {
			this.r27_client_grp = r27_client_grp;
		}
		public BigDecimal getR27_total_book_expo() {
			return r27_total_book_expo;
		}
		public void setR27_total_book_expo(BigDecimal r27_total_book_expo) {
			this.r27_total_book_expo = r27_total_book_expo;
		}
		public BigDecimal getR27_margin_pro() {
			return r27_margin_pro;
		}
		public void setR27_margin_pro(BigDecimal r27_margin_pro) {
			this.r27_margin_pro = r27_margin_pro;
		}
		public BigDecimal getR27_book_expo() {
			return r27_book_expo;
		}
		public void setR27_book_expo(BigDecimal r27_book_expo) {
			this.r27_book_expo = r27_book_expo;
		}
		public BigDecimal getR27_ccf_cont() {
			return r27_ccf_cont;
		}
		public void setR27_ccf_cont(BigDecimal r27_ccf_cont) {
			this.r27_ccf_cont = r27_ccf_cont;
		}
		public BigDecimal getR27_equiv_value() {
			return r27_equiv_value;
		}
		public void setR27_equiv_value(BigDecimal r27_equiv_value) {
			this.r27_equiv_value = r27_equiv_value;
		}
		public BigDecimal getR27_rw_obligant() {
			return r27_rw_obligant;
		}
		public void setR27_rw_obligant(BigDecimal r27_rw_obligant) {
			this.r27_rw_obligant = r27_rw_obligant;
		}
		public BigDecimal getR27_rav() {
			return r27_rav;
		}
		public void setR27_rav(BigDecimal r27_rav) {
			this.r27_rav = r27_rav;
		}
		public String getR28_product() {
			return r28_product;
		}
		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}
		public String getR28_client_grp() {
			return r28_client_grp;
		}
		public void setR28_client_grp(String r28_client_grp) {
			this.r28_client_grp = r28_client_grp;
		}
		public BigDecimal getR28_total_book_expo() {
			return r28_total_book_expo;
		}
		public void setR28_total_book_expo(BigDecimal r28_total_book_expo) {
			this.r28_total_book_expo = r28_total_book_expo;
		}
		public BigDecimal getR28_margin_pro() {
			return r28_margin_pro;
		}
		public void setR28_margin_pro(BigDecimal r28_margin_pro) {
			this.r28_margin_pro = r28_margin_pro;
		}
		public BigDecimal getR28_book_expo() {
			return r28_book_expo;
		}
		public void setR28_book_expo(BigDecimal r28_book_expo) {
			this.r28_book_expo = r28_book_expo;
		}
		public BigDecimal getR28_ccf_cont() {
			return r28_ccf_cont;
		}
		public void setR28_ccf_cont(BigDecimal r28_ccf_cont) {
			this.r28_ccf_cont = r28_ccf_cont;
		}
		public BigDecimal getR28_equiv_value() {
			return r28_equiv_value;
		}
		public void setR28_equiv_value(BigDecimal r28_equiv_value) {
			this.r28_equiv_value = r28_equiv_value;
		}
		public BigDecimal getR28_rw_obligant() {
			return r28_rw_obligant;
		}
		public void setR28_rw_obligant(BigDecimal r28_rw_obligant) {
			this.r28_rw_obligant = r28_rw_obligant;
		}
		public BigDecimal getR28_rav() {
			return r28_rav;
		}
		public void setR28_rav(BigDecimal r28_rav) {
			this.r28_rav = r28_rav;
		}
		public String getR29_product() {
			return r29_product;
		}
		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}
		public String getR29_client_grp() {
			return r29_client_grp;
		}
		public void setR29_client_grp(String r29_client_grp) {
			this.r29_client_grp = r29_client_grp;
		}
		public BigDecimal getR29_total_book_expo() {
			return r29_total_book_expo;
		}
		public void setR29_total_book_expo(BigDecimal r29_total_book_expo) {
			this.r29_total_book_expo = r29_total_book_expo;
		}
		public BigDecimal getR29_margin_pro() {
			return r29_margin_pro;
		}
		public void setR29_margin_pro(BigDecimal r29_margin_pro) {
			this.r29_margin_pro = r29_margin_pro;
		}
		public BigDecimal getR29_book_expo() {
			return r29_book_expo;
		}
		public void setR29_book_expo(BigDecimal r29_book_expo) {
			this.r29_book_expo = r29_book_expo;
		}
		public BigDecimal getR29_ccf_cont() {
			return r29_ccf_cont;
		}
		public void setR29_ccf_cont(BigDecimal r29_ccf_cont) {
			this.r29_ccf_cont = r29_ccf_cont;
		}
		public BigDecimal getR29_equiv_value() {
			return r29_equiv_value;
		}
		public void setR29_equiv_value(BigDecimal r29_equiv_value) {
			this.r29_equiv_value = r29_equiv_value;
		}
		public BigDecimal getR29_rw_obligant() {
			return r29_rw_obligant;
		}
		public void setR29_rw_obligant(BigDecimal r29_rw_obligant) {
			this.r29_rw_obligant = r29_rw_obligant;
		}
		public BigDecimal getR29_rav() {
			return r29_rav;
		}
		public void setR29_rav(BigDecimal r29_rav) {
			this.r29_rav = r29_rav;
		}
		public String getR30_product() {
			return r30_product;
		}
		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}
		public String getR30_client_grp() {
			return r30_client_grp;
		}
		public void setR30_client_grp(String r30_client_grp) {
			this.r30_client_grp = r30_client_grp;
		}
		public BigDecimal getR30_total_book_expo() {
			return r30_total_book_expo;
		}
		public void setR30_total_book_expo(BigDecimal r30_total_book_expo) {
			this.r30_total_book_expo = r30_total_book_expo;
		}
		public BigDecimal getR30_margin_pro() {
			return r30_margin_pro;
		}
		public void setR30_margin_pro(BigDecimal r30_margin_pro) {
			this.r30_margin_pro = r30_margin_pro;
		}
		public BigDecimal getR30_book_expo() {
			return r30_book_expo;
		}
		public void setR30_book_expo(BigDecimal r30_book_expo) {
			this.r30_book_expo = r30_book_expo;
		}
		public BigDecimal getR30_ccf_cont() {
			return r30_ccf_cont;
		}
		public void setR30_ccf_cont(BigDecimal r30_ccf_cont) {
			this.r30_ccf_cont = r30_ccf_cont;
		}
		public BigDecimal getR30_equiv_value() {
			return r30_equiv_value;
		}
		public void setR30_equiv_value(BigDecimal r30_equiv_value) {
			this.r30_equiv_value = r30_equiv_value;
		}
		public BigDecimal getR30_rw_obligant() {
			return r30_rw_obligant;
		}
		public void setR30_rw_obligant(BigDecimal r30_rw_obligant) {
			this.r30_rw_obligant = r30_rw_obligant;
		}
		public BigDecimal getR30_rav() {
			return r30_rav;
		}
		public void setR30_rav(BigDecimal r30_rav) {
			this.r30_rav = r30_rav;
		}
		public String getR31_product() {
			return r31_product;
		}
		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}
		public String getR31_client_grp() {
			return r31_client_grp;
		}
		public void setR31_client_grp(String r31_client_grp) {
			this.r31_client_grp = r31_client_grp;
		}
		public BigDecimal getR31_total_book_expo() {
			return r31_total_book_expo;
		}
		public void setR31_total_book_expo(BigDecimal r31_total_book_expo) {
			this.r31_total_book_expo = r31_total_book_expo;
		}
		public BigDecimal getR31_margin_pro() {
			return r31_margin_pro;
		}
		public void setR31_margin_pro(BigDecimal r31_margin_pro) {
			this.r31_margin_pro = r31_margin_pro;
		}
		public BigDecimal getR31_book_expo() {
			return r31_book_expo;
		}
		public void setR31_book_expo(BigDecimal r31_book_expo) {
			this.r31_book_expo = r31_book_expo;
		}
		public BigDecimal getR31_ccf_cont() {
			return r31_ccf_cont;
		}
		public void setR31_ccf_cont(BigDecimal r31_ccf_cont) {
			this.r31_ccf_cont = r31_ccf_cont;
		}
		public BigDecimal getR31_equiv_value() {
			return r31_equiv_value;
		}
		public void setR31_equiv_value(BigDecimal r31_equiv_value) {
			this.r31_equiv_value = r31_equiv_value;
		}
		public BigDecimal getR31_rw_obligant() {
			return r31_rw_obligant;
		}
		public void setR31_rw_obligant(BigDecimal r31_rw_obligant) {
			this.r31_rw_obligant = r31_rw_obligant;
		}
		public BigDecimal getR31_rav() {
			return r31_rav;
		}
		public void setR31_rav(BigDecimal r31_rav) {
			this.r31_rav = r31_rav;
		}
		public String getR32_product() {
			return r32_product;
		}
		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}
		public String getR32_client_grp() {
			return r32_client_grp;
		}
		public void setR32_client_grp(String r32_client_grp) {
			this.r32_client_grp = r32_client_grp;
		}
		public BigDecimal getR32_total_book_expo() {
			return r32_total_book_expo;
		}
		public void setR32_total_book_expo(BigDecimal r32_total_book_expo) {
			this.r32_total_book_expo = r32_total_book_expo;
		}
		public BigDecimal getR32_margin_pro() {
			return r32_margin_pro;
		}
		public void setR32_margin_pro(BigDecimal r32_margin_pro) {
			this.r32_margin_pro = r32_margin_pro;
		}
		public BigDecimal getR32_book_expo() {
			return r32_book_expo;
		}
		public void setR32_book_expo(BigDecimal r32_book_expo) {
			this.r32_book_expo = r32_book_expo;
		}
		public BigDecimal getR32_ccf_cont() {
			return r32_ccf_cont;
		}
		public void setR32_ccf_cont(BigDecimal r32_ccf_cont) {
			this.r32_ccf_cont = r32_ccf_cont;
		}
		public BigDecimal getR32_equiv_value() {
			return r32_equiv_value;
		}
		public void setR32_equiv_value(BigDecimal r32_equiv_value) {
			this.r32_equiv_value = r32_equiv_value;
		}
		public BigDecimal getR32_rw_obligant() {
			return r32_rw_obligant;
		}
		public void setR32_rw_obligant(BigDecimal r32_rw_obligant) {
			this.r32_rw_obligant = r32_rw_obligant;
		}
		public BigDecimal getR32_rav() {
			return r32_rav;
		}
		public void setR32_rav(BigDecimal r32_rav) {
			this.r32_rav = r32_rav;
		}
		public String getR33_product() {
			return r33_product;
		}
		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}
		public String getR33_client_grp() {
			return r33_client_grp;
		}
		public void setR33_client_grp(String r33_client_grp) {
			this.r33_client_grp = r33_client_grp;
		}
		public BigDecimal getR33_total_book_expo() {
			return r33_total_book_expo;
		}
		public void setR33_total_book_expo(BigDecimal r33_total_book_expo) {
			this.r33_total_book_expo = r33_total_book_expo;
		}
		public BigDecimal getR33_margin_pro() {
			return r33_margin_pro;
		}
		public void setR33_margin_pro(BigDecimal r33_margin_pro) {
			this.r33_margin_pro = r33_margin_pro;
		}
		public BigDecimal getR33_book_expo() {
			return r33_book_expo;
		}
		public void setR33_book_expo(BigDecimal r33_book_expo) {
			this.r33_book_expo = r33_book_expo;
		}
		public BigDecimal getR33_ccf_cont() {
			return r33_ccf_cont;
		}
		public void setR33_ccf_cont(BigDecimal r33_ccf_cont) {
			this.r33_ccf_cont = r33_ccf_cont;
		}
		public BigDecimal getR33_equiv_value() {
			return r33_equiv_value;
		}
		public void setR33_equiv_value(BigDecimal r33_equiv_value) {
			this.r33_equiv_value = r33_equiv_value;
		}
		public BigDecimal getR33_rw_obligant() {
			return r33_rw_obligant;
		}
		public void setR33_rw_obligant(BigDecimal r33_rw_obligant) {
			this.r33_rw_obligant = r33_rw_obligant;
		}
		public BigDecimal getR33_rav() {
			return r33_rav;
		}
		public void setR33_rav(BigDecimal r33_rav) {
			this.r33_rav = r33_rav;
		}
		public String getR34_product() {
			return r34_product;
		}
		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}
		public String getR34_client_grp() {
			return r34_client_grp;
		}
		public void setR34_client_grp(String r34_client_grp) {
			this.r34_client_grp = r34_client_grp;
		}
		public BigDecimal getR34_total_book_expo() {
			return r34_total_book_expo;
		}
		public void setR34_total_book_expo(BigDecimal r34_total_book_expo) {
			this.r34_total_book_expo = r34_total_book_expo;
		}
		public BigDecimal getR34_margin_pro() {
			return r34_margin_pro;
		}
		public void setR34_margin_pro(BigDecimal r34_margin_pro) {
			this.r34_margin_pro = r34_margin_pro;
		}
		public BigDecimal getR34_book_expo() {
			return r34_book_expo;
		}
		public void setR34_book_expo(BigDecimal r34_book_expo) {
			this.r34_book_expo = r34_book_expo;
		}
		public BigDecimal getR34_ccf_cont() {
			return r34_ccf_cont;
		}
		public void setR34_ccf_cont(BigDecimal r34_ccf_cont) {
			this.r34_ccf_cont = r34_ccf_cont;
		}
		public BigDecimal getR34_equiv_value() {
			return r34_equiv_value;
		}
		public void setR34_equiv_value(BigDecimal r34_equiv_value) {
			this.r34_equiv_value = r34_equiv_value;
		}
		public BigDecimal getR34_rw_obligant() {
			return r34_rw_obligant;
		}
		public void setR34_rw_obligant(BigDecimal r34_rw_obligant) {
			this.r34_rw_obligant = r34_rw_obligant;
		}
		public BigDecimal getR34_rav() {
			return r34_rav;
		}
		public void setR34_rav(BigDecimal r34_rav) {
			this.r34_rav = r34_rav;
		}
		public String getR35_product() {
			return r35_product;
		}
		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}
		public String getR35_client_grp() {
			return r35_client_grp;
		}
		public void setR35_client_grp(String r35_client_grp) {
			this.r35_client_grp = r35_client_grp;
		}
		public BigDecimal getR35_total_book_expo() {
			return r35_total_book_expo;
		}
		public void setR35_total_book_expo(BigDecimal r35_total_book_expo) {
			this.r35_total_book_expo = r35_total_book_expo;
		}
		public BigDecimal getR35_margin_pro() {
			return r35_margin_pro;
		}
		public void setR35_margin_pro(BigDecimal r35_margin_pro) {
			this.r35_margin_pro = r35_margin_pro;
		}
		public BigDecimal getR35_book_expo() {
			return r35_book_expo;
		}
		public void setR35_book_expo(BigDecimal r35_book_expo) {
			this.r35_book_expo = r35_book_expo;
		}
		public BigDecimal getR35_ccf_cont() {
			return r35_ccf_cont;
		}
		public void setR35_ccf_cont(BigDecimal r35_ccf_cont) {
			this.r35_ccf_cont = r35_ccf_cont;
		}
		public BigDecimal getR35_equiv_value() {
			return r35_equiv_value;
		}
		public void setR35_equiv_value(BigDecimal r35_equiv_value) {
			this.r35_equiv_value = r35_equiv_value;
		}
		public BigDecimal getR35_rw_obligant() {
			return r35_rw_obligant;
		}
		public void setR35_rw_obligant(BigDecimal r35_rw_obligant) {
			this.r35_rw_obligant = r35_rw_obligant;
		}
		public BigDecimal getR35_rav() {
			return r35_rav;
		}
		public void setR35_rav(BigDecimal r35_rav) {
			this.r35_rav = r35_rav;
		}
		public String getR36_product() {
			return r36_product;
		}
		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}
		public String getR36_client_grp() {
			return r36_client_grp;
		}
		public void setR36_client_grp(String r36_client_grp) {
			this.r36_client_grp = r36_client_grp;
		}
		public BigDecimal getR36_total_book_expo() {
			return r36_total_book_expo;
		}
		public void setR36_total_book_expo(BigDecimal r36_total_book_expo) {
			this.r36_total_book_expo = r36_total_book_expo;
		}
		public BigDecimal getR36_margin_pro() {
			return r36_margin_pro;
		}
		public void setR36_margin_pro(BigDecimal r36_margin_pro) {
			this.r36_margin_pro = r36_margin_pro;
		}
		public BigDecimal getR36_book_expo() {
			return r36_book_expo;
		}
		public void setR36_book_expo(BigDecimal r36_book_expo) {
			this.r36_book_expo = r36_book_expo;
		}
		public BigDecimal getR36_ccf_cont() {
			return r36_ccf_cont;
		}
		public void setR36_ccf_cont(BigDecimal r36_ccf_cont) {
			this.r36_ccf_cont = r36_ccf_cont;
		}
		public BigDecimal getR36_equiv_value() {
			return r36_equiv_value;
		}
		public void setR36_equiv_value(BigDecimal r36_equiv_value) {
			this.r36_equiv_value = r36_equiv_value;
		}
		public BigDecimal getR36_rw_obligant() {
			return r36_rw_obligant;
		}
		public void setR36_rw_obligant(BigDecimal r36_rw_obligant) {
			this.r36_rw_obligant = r36_rw_obligant;
		}
		public BigDecimal getR36_rav() {
			return r36_rav;
		}
		public void setR36_rav(BigDecimal r36_rav) {
			this.r36_rav = r36_rav;
		}
		public String getR37_product() {
			return r37_product;
		}
		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}
		public String getR37_client_grp() {
			return r37_client_grp;
		}
		public void setR37_client_grp(String r37_client_grp) {
			this.r37_client_grp = r37_client_grp;
		}
		public BigDecimal getR37_total_book_expo() {
			return r37_total_book_expo;
		}
		public void setR37_total_book_expo(BigDecimal r37_total_book_expo) {
			this.r37_total_book_expo = r37_total_book_expo;
		}
		public BigDecimal getR37_margin_pro() {
			return r37_margin_pro;
		}
		public void setR37_margin_pro(BigDecimal r37_margin_pro) {
			this.r37_margin_pro = r37_margin_pro;
		}
		public BigDecimal getR37_book_expo() {
			return r37_book_expo;
		}
		public void setR37_book_expo(BigDecimal r37_book_expo) {
			this.r37_book_expo = r37_book_expo;
		}
		public BigDecimal getR37_ccf_cont() {
			return r37_ccf_cont;
		}
		public void setR37_ccf_cont(BigDecimal r37_ccf_cont) {
			this.r37_ccf_cont = r37_ccf_cont;
		}
		public BigDecimal getR37_equiv_value() {
			return r37_equiv_value;
		}
		public void setR37_equiv_value(BigDecimal r37_equiv_value) {
			this.r37_equiv_value = r37_equiv_value;
		}
		public BigDecimal getR37_rw_obligant() {
			return r37_rw_obligant;
		}
		public void setR37_rw_obligant(BigDecimal r37_rw_obligant) {
			this.r37_rw_obligant = r37_rw_obligant;
		}
		public BigDecimal getR37_rav() {
			return r37_rav;
		}
		public void setR37_rav(BigDecimal r37_rav) {
			this.r37_rav = r37_rav;
		}
		public String getR38_product() {
			return r38_product;
		}
		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}
		public String getR38_client_grp() {
			return r38_client_grp;
		}
		public void setR38_client_grp(String r38_client_grp) {
			this.r38_client_grp = r38_client_grp;
		}
		public BigDecimal getR38_total_book_expo() {
			return r38_total_book_expo;
		}
		public void setR38_total_book_expo(BigDecimal r38_total_book_expo) {
			this.r38_total_book_expo = r38_total_book_expo;
		}
		public BigDecimal getR38_margin_pro() {
			return r38_margin_pro;
		}
		public void setR38_margin_pro(BigDecimal r38_margin_pro) {
			this.r38_margin_pro = r38_margin_pro;
		}
		public BigDecimal getR38_book_expo() {
			return r38_book_expo;
		}
		public void setR38_book_expo(BigDecimal r38_book_expo) {
			this.r38_book_expo = r38_book_expo;
		}
		public BigDecimal getR38_ccf_cont() {
			return r38_ccf_cont;
		}
		public void setR38_ccf_cont(BigDecimal r38_ccf_cont) {
			this.r38_ccf_cont = r38_ccf_cont;
		}
		public BigDecimal getR38_equiv_value() {
			return r38_equiv_value;
		}
		public void setR38_equiv_value(BigDecimal r38_equiv_value) {
			this.r38_equiv_value = r38_equiv_value;
		}
		public BigDecimal getR38_rw_obligant() {
			return r38_rw_obligant;
		}
		public void setR38_rw_obligant(BigDecimal r38_rw_obligant) {
			this.r38_rw_obligant = r38_rw_obligant;
		}
		public BigDecimal getR38_rav() {
			return r38_rav;
		}
		public void setR38_rav(BigDecimal r38_rav) {
			this.r38_rav = r38_rav;
		}
		public String getR39_product() {
			return r39_product;
		}
		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}
		public String getR39_client_grp() {
			return r39_client_grp;
		}
		public void setR39_client_grp(String r39_client_grp) {
			this.r39_client_grp = r39_client_grp;
		}
		public BigDecimal getR39_total_book_expo() {
			return r39_total_book_expo;
		}
		public void setR39_total_book_expo(BigDecimal r39_total_book_expo) {
			this.r39_total_book_expo = r39_total_book_expo;
		}
		public BigDecimal getR39_margin_pro() {
			return r39_margin_pro;
		}
		public void setR39_margin_pro(BigDecimal r39_margin_pro) {
			this.r39_margin_pro = r39_margin_pro;
		}
		public BigDecimal getR39_book_expo() {
			return r39_book_expo;
		}
		public void setR39_book_expo(BigDecimal r39_book_expo) {
			this.r39_book_expo = r39_book_expo;
		}
		public BigDecimal getR39_ccf_cont() {
			return r39_ccf_cont;
		}
		public void setR39_ccf_cont(BigDecimal r39_ccf_cont) {
			this.r39_ccf_cont = r39_ccf_cont;
		}
		public BigDecimal getR39_equiv_value() {
			return r39_equiv_value;
		}
		public void setR39_equiv_value(BigDecimal r39_equiv_value) {
			this.r39_equiv_value = r39_equiv_value;
		}
		public BigDecimal getR39_rw_obligant() {
			return r39_rw_obligant;
		}
		public void setR39_rw_obligant(BigDecimal r39_rw_obligant) {
			this.r39_rw_obligant = r39_rw_obligant;
		}
		public BigDecimal getR39_rav() {
			return r39_rav;
		}
		public void setR39_rav(BigDecimal r39_rav) {
			this.r39_rav = r39_rav;
		}
		public String getR40_product() {
			return r40_product;
		}
		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}
		public String getR40_client_grp() {
			return r40_client_grp;
		}
		public void setR40_client_grp(String r40_client_grp) {
			this.r40_client_grp = r40_client_grp;
		}
		public BigDecimal getR40_total_book_expo() {
			return r40_total_book_expo;
		}
		public void setR40_total_book_expo(BigDecimal r40_total_book_expo) {
			this.r40_total_book_expo = r40_total_book_expo;
		}
		public BigDecimal getR40_margin_pro() {
			return r40_margin_pro;
		}
		public void setR40_margin_pro(BigDecimal r40_margin_pro) {
			this.r40_margin_pro = r40_margin_pro;
		}
		public BigDecimal getR40_book_expo() {
			return r40_book_expo;
		}
		public void setR40_book_expo(BigDecimal r40_book_expo) {
			this.r40_book_expo = r40_book_expo;
		}
		public BigDecimal getR40_ccf_cont() {
			return r40_ccf_cont;
		}
		public void setR40_ccf_cont(BigDecimal r40_ccf_cont) {
			this.r40_ccf_cont = r40_ccf_cont;
		}
		public BigDecimal getR40_equiv_value() {
			return r40_equiv_value;
		}
		public void setR40_equiv_value(BigDecimal r40_equiv_value) {
			this.r40_equiv_value = r40_equiv_value;
		}
		public BigDecimal getR40_rw_obligant() {
			return r40_rw_obligant;
		}
		public void setR40_rw_obligant(BigDecimal r40_rw_obligant) {
			this.r40_rw_obligant = r40_rw_obligant;
		}
		public BigDecimal getR40_rav() {
			return r40_rav;
		}
		public void setR40_rav(BigDecimal r40_rav) {
			this.r40_rav = r40_rav;
		}
		public String getR41_product() {
			return r41_product;
		}
		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}
		public String getR41_client_grp() {
			return r41_client_grp;
		}
		public void setR41_client_grp(String r41_client_grp) {
			this.r41_client_grp = r41_client_grp;
		}
		public BigDecimal getR41_total_book_expo() {
			return r41_total_book_expo;
		}
		public void setR41_total_book_expo(BigDecimal r41_total_book_expo) {
			this.r41_total_book_expo = r41_total_book_expo;
		}
		public BigDecimal getR41_margin_pro() {
			return r41_margin_pro;
		}
		public void setR41_margin_pro(BigDecimal r41_margin_pro) {
			this.r41_margin_pro = r41_margin_pro;
		}
		public BigDecimal getR41_book_expo() {
			return r41_book_expo;
		}
		public void setR41_book_expo(BigDecimal r41_book_expo) {
			this.r41_book_expo = r41_book_expo;
		}
		public BigDecimal getR41_ccf_cont() {
			return r41_ccf_cont;
		}
		public void setR41_ccf_cont(BigDecimal r41_ccf_cont) {
			this.r41_ccf_cont = r41_ccf_cont;
		}
		public BigDecimal getR41_equiv_value() {
			return r41_equiv_value;
		}
		public void setR41_equiv_value(BigDecimal r41_equiv_value) {
			this.r41_equiv_value = r41_equiv_value;
		}
		public BigDecimal getR41_rw_obligant() {
			return r41_rw_obligant;
		}
		public void setR41_rw_obligant(BigDecimal r41_rw_obligant) {
			this.r41_rw_obligant = r41_rw_obligant;
		}
		public BigDecimal getR41_rav() {
			return r41_rav;
		}
		public void setR41_rav(BigDecimal r41_rav) {
			this.r41_rav = r41_rav;
		}
		public String getR42_product() {
			return r42_product;
		}
		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}
		public String getR42_client_grp() {
			return r42_client_grp;
		}
		public void setR42_client_grp(String r42_client_grp) {
			this.r42_client_grp = r42_client_grp;
		}
		public BigDecimal getR42_total_book_expo() {
			return r42_total_book_expo;
		}
		public void setR42_total_book_expo(BigDecimal r42_total_book_expo) {
			this.r42_total_book_expo = r42_total_book_expo;
		}
		public BigDecimal getR42_margin_pro() {
			return r42_margin_pro;
		}
		public void setR42_margin_pro(BigDecimal r42_margin_pro) {
			this.r42_margin_pro = r42_margin_pro;
		}
		public BigDecimal getR42_book_expo() {
			return r42_book_expo;
		}
		public void setR42_book_expo(BigDecimal r42_book_expo) {
			this.r42_book_expo = r42_book_expo;
		}
		public BigDecimal getR42_ccf_cont() {
			return r42_ccf_cont;
		}
		public void setR42_ccf_cont(BigDecimal r42_ccf_cont) {
			this.r42_ccf_cont = r42_ccf_cont;
		}
		public BigDecimal getR42_equiv_value() {
			return r42_equiv_value;
		}
		public void setR42_equiv_value(BigDecimal r42_equiv_value) {
			this.r42_equiv_value = r42_equiv_value;
		}
		public BigDecimal getR42_rw_obligant() {
			return r42_rw_obligant;
		}
		public void setR42_rw_obligant(BigDecimal r42_rw_obligant) {
			this.r42_rw_obligant = r42_rw_obligant;
		}
		public BigDecimal getR42_rav() {
			return r42_rav;
		}
		public void setR42_rav(BigDecimal r42_rav) {
			this.r42_rav = r42_rav;
		}
		public String getR43_product() {
			return r43_product;
		}
		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}
		public String getR43_client_grp() {
			return r43_client_grp;
		}
		public void setR43_client_grp(String r43_client_grp) {
			this.r43_client_grp = r43_client_grp;
		}
		public BigDecimal getR43_total_book_expo() {
			return r43_total_book_expo;
		}
		public void setR43_total_book_expo(BigDecimal r43_total_book_expo) {
			this.r43_total_book_expo = r43_total_book_expo;
		}
		public BigDecimal getR43_margin_pro() {
			return r43_margin_pro;
		}
		public void setR43_margin_pro(BigDecimal r43_margin_pro) {
			this.r43_margin_pro = r43_margin_pro;
		}
		public BigDecimal getR43_book_expo() {
			return r43_book_expo;
		}
		public void setR43_book_expo(BigDecimal r43_book_expo) {
			this.r43_book_expo = r43_book_expo;
		}
		public BigDecimal getR43_ccf_cont() {
			return r43_ccf_cont;
		}
		public void setR43_ccf_cont(BigDecimal r43_ccf_cont) {
			this.r43_ccf_cont = r43_ccf_cont;
		}
		public BigDecimal getR43_equiv_value() {
			return r43_equiv_value;
		}
		public void setR43_equiv_value(BigDecimal r43_equiv_value) {
			this.r43_equiv_value = r43_equiv_value;
		}
		public BigDecimal getR43_rw_obligant() {
			return r43_rw_obligant;
		}
		public void setR43_rw_obligant(BigDecimal r43_rw_obligant) {
			this.r43_rw_obligant = r43_rw_obligant;
		}
		public BigDecimal getR43_rav() {
			return r43_rav;
		}
		public void setR43_rav(BigDecimal r43_rav) {
			this.r43_rav = r43_rav;
		}
		public String getR44_product() {
			return r44_product;
		}
		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}
		public String getR44_client_grp() {
			return r44_client_grp;
		}
		public void setR44_client_grp(String r44_client_grp) {
			this.r44_client_grp = r44_client_grp;
		}
		public BigDecimal getR44_total_book_expo() {
			return r44_total_book_expo;
		}
		public void setR44_total_book_expo(BigDecimal r44_total_book_expo) {
			this.r44_total_book_expo = r44_total_book_expo;
		}
		public BigDecimal getR44_margin_pro() {
			return r44_margin_pro;
		}
		public void setR44_margin_pro(BigDecimal r44_margin_pro) {
			this.r44_margin_pro = r44_margin_pro;
		}
		public BigDecimal getR44_book_expo() {
			return r44_book_expo;
		}
		public void setR44_book_expo(BigDecimal r44_book_expo) {
			this.r44_book_expo = r44_book_expo;
		}
		public BigDecimal getR44_ccf_cont() {
			return r44_ccf_cont;
		}
		public void setR44_ccf_cont(BigDecimal r44_ccf_cont) {
			this.r44_ccf_cont = r44_ccf_cont;
		}
		public BigDecimal getR44_equiv_value() {
			return r44_equiv_value;
		}
		public void setR44_equiv_value(BigDecimal r44_equiv_value) {
			this.r44_equiv_value = r44_equiv_value;
		}
		public BigDecimal getR44_rw_obligant() {
			return r44_rw_obligant;
		}
		public void setR44_rw_obligant(BigDecimal r44_rw_obligant) {
			this.r44_rw_obligant = r44_rw_obligant;
		}
		public BigDecimal getR44_rav() {
			return r44_rav;
		}
		public void setR44_rav(BigDecimal r44_rav) {
			this.r44_rav = r44_rav;
		}
		public String getR45_product() {
			return r45_product;
		}
		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}
		public String getR45_client_grp() {
			return r45_client_grp;
		}
		public void setR45_client_grp(String r45_client_grp) {
			this.r45_client_grp = r45_client_grp;
		}
		public BigDecimal getR45_total_book_expo() {
			return r45_total_book_expo;
		}
		public void setR45_total_book_expo(BigDecimal r45_total_book_expo) {
			this.r45_total_book_expo = r45_total_book_expo;
		}
		public BigDecimal getR45_margin_pro() {
			return r45_margin_pro;
		}
		public void setR45_margin_pro(BigDecimal r45_margin_pro) {
			this.r45_margin_pro = r45_margin_pro;
		}
		public BigDecimal getR45_book_expo() {
			return r45_book_expo;
		}
		public void setR45_book_expo(BigDecimal r45_book_expo) {
			this.r45_book_expo = r45_book_expo;
		}
		public BigDecimal getR45_ccf_cont() {
			return r45_ccf_cont;
		}
		public void setR45_ccf_cont(BigDecimal r45_ccf_cont) {
			this.r45_ccf_cont = r45_ccf_cont;
		}
		public BigDecimal getR45_equiv_value() {
			return r45_equiv_value;
		}
		public void setR45_equiv_value(BigDecimal r45_equiv_value) {
			this.r45_equiv_value = r45_equiv_value;
		}
		public BigDecimal getR45_rw_obligant() {
			return r45_rw_obligant;
		}
		public void setR45_rw_obligant(BigDecimal r45_rw_obligant) {
			this.r45_rw_obligant = r45_rw_obligant;
		}
		public BigDecimal getR45_rav() {
			return r45_rav;
		}
		public void setR45_rav(BigDecimal r45_rav) {
			this.r45_rav = r45_rav;
		}
		public String getR46_product() {
			return r46_product;
		}
		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}
		public String getR46_client_grp() {
			return r46_client_grp;
		}
		public void setR46_client_grp(String r46_client_grp) {
			this.r46_client_grp = r46_client_grp;
		}
		public BigDecimal getR46_total_book_expo() {
			return r46_total_book_expo;
		}
		public void setR46_total_book_expo(BigDecimal r46_total_book_expo) {
			this.r46_total_book_expo = r46_total_book_expo;
		}
		public BigDecimal getR46_margin_pro() {
			return r46_margin_pro;
		}
		public void setR46_margin_pro(BigDecimal r46_margin_pro) {
			this.r46_margin_pro = r46_margin_pro;
		}
		public BigDecimal getR46_book_expo() {
			return r46_book_expo;
		}
		public void setR46_book_expo(BigDecimal r46_book_expo) {
			this.r46_book_expo = r46_book_expo;
		}
		public BigDecimal getR46_ccf_cont() {
			return r46_ccf_cont;
		}
		public void setR46_ccf_cont(BigDecimal r46_ccf_cont) {
			this.r46_ccf_cont = r46_ccf_cont;
		}
		public BigDecimal getR46_equiv_value() {
			return r46_equiv_value;
		}
		public void setR46_equiv_value(BigDecimal r46_equiv_value) {
			this.r46_equiv_value = r46_equiv_value;
		}
		public BigDecimal getR46_rw_obligant() {
			return r46_rw_obligant;
		}
		public void setR46_rw_obligant(BigDecimal r46_rw_obligant) {
			this.r46_rw_obligant = r46_rw_obligant;
		}
		public BigDecimal getR46_rav() {
			return r46_rav;
		}
		public void setR46_rav(BigDecimal r46_rav) {
			this.r46_rav = r46_rav;
		}
		public String getR61_product() {
			return r61_product;
		}
		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}
		public String getR61_client_grp() {
			return r61_client_grp;
		}
		public void setR61_client_grp(String r61_client_grp) {
			this.r61_client_grp = r61_client_grp;
		}
		public BigDecimal getR61_total_book_expo() {
			return r61_total_book_expo;
		}
		public void setR61_total_book_expo(BigDecimal r61_total_book_expo) {
			this.r61_total_book_expo = r61_total_book_expo;
		}
		public BigDecimal getR61_margin_pro() {
			return r61_margin_pro;
		}
		public void setR61_margin_pro(BigDecimal r61_margin_pro) {
			this.r61_margin_pro = r61_margin_pro;
		}
		public BigDecimal getR61_book_expo() {
			return r61_book_expo;
		}
		public void setR61_book_expo(BigDecimal r61_book_expo) {
			this.r61_book_expo = r61_book_expo;
		}
		public BigDecimal getR61_ccf_cont() {
			return r61_ccf_cont;
		}
		public void setR61_ccf_cont(BigDecimal r61_ccf_cont) {
			this.r61_ccf_cont = r61_ccf_cont;
		}
		public BigDecimal getR61_equiv_value() {
			return r61_equiv_value;
		}
		public void setR61_equiv_value(BigDecimal r61_equiv_value) {
			this.r61_equiv_value = r61_equiv_value;
		}
		public BigDecimal getR61_rw_obligant() {
			return r61_rw_obligant;
		}
		public void setR61_rw_obligant(BigDecimal r61_rw_obligant) {
			this.r61_rw_obligant = r61_rw_obligant;
		}
		public BigDecimal getR61_rav() {
			return r61_rav;
		}
		public void setR61_rav(BigDecimal r61_rav) {
			this.r61_rav = r61_rav;
		}
		public String getR62_product() {
			return r62_product;
		}
		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}
		public String getR62_client_grp() {
			return r62_client_grp;
		}
		public void setR62_client_grp(String r62_client_grp) {
			this.r62_client_grp = r62_client_grp;
		}
		public BigDecimal getR62_total_book_expo() {
			return r62_total_book_expo;
		}
		public void setR62_total_book_expo(BigDecimal r62_total_book_expo) {
			this.r62_total_book_expo = r62_total_book_expo;
		}
		public BigDecimal getR62_margin_pro() {
			return r62_margin_pro;
		}
		public void setR62_margin_pro(BigDecimal r62_margin_pro) {
			this.r62_margin_pro = r62_margin_pro;
		}
		public BigDecimal getR62_book_expo() {
			return r62_book_expo;
		}
		public void setR62_book_expo(BigDecimal r62_book_expo) {
			this.r62_book_expo = r62_book_expo;
		}
		public BigDecimal getR62_ccf_cont() {
			return r62_ccf_cont;
		}
		public void setR62_ccf_cont(BigDecimal r62_ccf_cont) {
			this.r62_ccf_cont = r62_ccf_cont;
		}
		public BigDecimal getR62_equiv_value() {
			return r62_equiv_value;
		}
		public void setR62_equiv_value(BigDecimal r62_equiv_value) {
			this.r62_equiv_value = r62_equiv_value;
		}
		public BigDecimal getR62_rw_obligant() {
			return r62_rw_obligant;
		}
		public void setR62_rw_obligant(BigDecimal r62_rw_obligant) {
			this.r62_rw_obligant = r62_rw_obligant;
		}
		public BigDecimal getR62_rav() {
			return r62_rav;
		}
		public void setR62_rav(BigDecimal r62_rav) {
			this.r62_rav = r62_rav;
		}
		public String getR63_product() {
			return r63_product;
		}
		public void setR63_product(String r63_product) {
			this.r63_product = r63_product;
		}
		public String getR63_client_grp() {
			return r63_client_grp;
		}
		public void setR63_client_grp(String r63_client_grp) {
			this.r63_client_grp = r63_client_grp;
		}
		public BigDecimal getR63_total_book_expo() {
			return r63_total_book_expo;
		}
		public void setR63_total_book_expo(BigDecimal r63_total_book_expo) {
			this.r63_total_book_expo = r63_total_book_expo;
		}
		public BigDecimal getR63_margin_pro() {
			return r63_margin_pro;
		}
		public void setR63_margin_pro(BigDecimal r63_margin_pro) {
			this.r63_margin_pro = r63_margin_pro;
		}
		public BigDecimal getR63_book_expo() {
			return r63_book_expo;
		}
		public void setR63_book_expo(BigDecimal r63_book_expo) {
			this.r63_book_expo = r63_book_expo;
		}
		public BigDecimal getR63_ccf_cont() {
			return r63_ccf_cont;
		}
		public void setR63_ccf_cont(BigDecimal r63_ccf_cont) {
			this.r63_ccf_cont = r63_ccf_cont;
		}
		public BigDecimal getR63_equiv_value() {
			return r63_equiv_value;
		}
		public void setR63_equiv_value(BigDecimal r63_equiv_value) {
			this.r63_equiv_value = r63_equiv_value;
		}
		public BigDecimal getR63_rw_obligant() {
			return r63_rw_obligant;
		}
		public void setR63_rw_obligant(BigDecimal r63_rw_obligant) {
			this.r63_rw_obligant = r63_rw_obligant;
		}
		public BigDecimal getR63_rav() {
			return r63_rav;
		}
		public void setR63_rav(BigDecimal r63_rav) {
			this.r63_rav = r63_rav;
		}
		public String getR64_product() {
			return r64_product;
		}
		public void setR64_product(String r64_product) {
			this.r64_product = r64_product;
		}
		public String getR64_client_grp() {
			return r64_client_grp;
		}
		public void setR64_client_grp(String r64_client_grp) {
			this.r64_client_grp = r64_client_grp;
		}
		public BigDecimal getR64_total_book_expo() {
			return r64_total_book_expo;
		}
		public void setR64_total_book_expo(BigDecimal r64_total_book_expo) {
			this.r64_total_book_expo = r64_total_book_expo;
		}
		public BigDecimal getR64_margin_pro() {
			return r64_margin_pro;
		}
		public void setR64_margin_pro(BigDecimal r64_margin_pro) {
			this.r64_margin_pro = r64_margin_pro;
		}
		public BigDecimal getR64_book_expo() {
			return r64_book_expo;
		}
		public void setR64_book_expo(BigDecimal r64_book_expo) {
			this.r64_book_expo = r64_book_expo;
		}
		public BigDecimal getR64_ccf_cont() {
			return r64_ccf_cont;
		}
		public void setR64_ccf_cont(BigDecimal r64_ccf_cont) {
			this.r64_ccf_cont = r64_ccf_cont;
		}
		public BigDecimal getR64_equiv_value() {
			return r64_equiv_value;
		}
		public void setR64_equiv_value(BigDecimal r64_equiv_value) {
			this.r64_equiv_value = r64_equiv_value;
		}
		public BigDecimal getR64_rw_obligant() {
			return r64_rw_obligant;
		}
		public void setR64_rw_obligant(BigDecimal r64_rw_obligant) {
			this.r64_rw_obligant = r64_rw_obligant;
		}
		public BigDecimal getR64_rav() {
			return r64_rav;
		}
		public void setR64_rav(BigDecimal r64_rav) {
			this.r64_rav = r64_rav;
		}
		public String getR65_product() {
			return r65_product;
		}
		public void setR65_product(String r65_product) {
			this.r65_product = r65_product;
		}
		public String getR65_client_grp() {
			return r65_client_grp;
		}
		public void setR65_client_grp(String r65_client_grp) {
			this.r65_client_grp = r65_client_grp;
		}
		public BigDecimal getR65_total_book_expo() {
			return r65_total_book_expo;
		}
		public void setR65_total_book_expo(BigDecimal r65_total_book_expo) {
			this.r65_total_book_expo = r65_total_book_expo;
		}
		public BigDecimal getR65_margin_pro() {
			return r65_margin_pro;
		}
		public void setR65_margin_pro(BigDecimal r65_margin_pro) {
			this.r65_margin_pro = r65_margin_pro;
		}
		public BigDecimal getR65_book_expo() {
			return r65_book_expo;
		}
		public void setR65_book_expo(BigDecimal r65_book_expo) {
			this.r65_book_expo = r65_book_expo;
		}
		public BigDecimal getR65_ccf_cont() {
			return r65_ccf_cont;
		}
		public void setR65_ccf_cont(BigDecimal r65_ccf_cont) {
			this.r65_ccf_cont = r65_ccf_cont;
		}
		public BigDecimal getR65_equiv_value() {
			return r65_equiv_value;
		}
		public void setR65_equiv_value(BigDecimal r65_equiv_value) {
			this.r65_equiv_value = r65_equiv_value;
		}
		public BigDecimal getR65_rw_obligant() {
			return r65_rw_obligant;
		}
		public void setR65_rw_obligant(BigDecimal r65_rw_obligant) {
			this.r65_rw_obligant = r65_rw_obligant;
		}
		public BigDecimal getR65_rav() {
			return r65_rav;
		}
		public void setR65_rav(BigDecimal r65_rav) {
			this.r65_rav = r65_rav;
		}
		public String getR66_product() {
			return r66_product;
		}
		public void setR66_product(String r66_product) {
			this.r66_product = r66_product;
		}
		public String getR66_client_grp() {
			return r66_client_grp;
		}
		public void setR66_client_grp(String r66_client_grp) {
			this.r66_client_grp = r66_client_grp;
		}
		public BigDecimal getR66_total_book_expo() {
			return r66_total_book_expo;
		}
		public void setR66_total_book_expo(BigDecimal r66_total_book_expo) {
			this.r66_total_book_expo = r66_total_book_expo;
		}
		public BigDecimal getR66_margin_pro() {
			return r66_margin_pro;
		}
		public void setR66_margin_pro(BigDecimal r66_margin_pro) {
			this.r66_margin_pro = r66_margin_pro;
		}
		public BigDecimal getR66_book_expo() {
			return r66_book_expo;
		}
		public void setR66_book_expo(BigDecimal r66_book_expo) {
			this.r66_book_expo = r66_book_expo;
		}
		public BigDecimal getR66_ccf_cont() {
			return r66_ccf_cont;
		}
		public void setR66_ccf_cont(BigDecimal r66_ccf_cont) {
			this.r66_ccf_cont = r66_ccf_cont;
		}
		public BigDecimal getR66_equiv_value() {
			return r66_equiv_value;
		}
		public void setR66_equiv_value(BigDecimal r66_equiv_value) {
			this.r66_equiv_value = r66_equiv_value;
		}
		public BigDecimal getR66_rw_obligant() {
			return r66_rw_obligant;
		}
		public void setR66_rw_obligant(BigDecimal r66_rw_obligant) {
			this.r66_rw_obligant = r66_rw_obligant;
		}
		public BigDecimal getR66_rav() {
			return r66_rav;
		}
		public void setR66_rav(BigDecimal r66_rav) {
			this.r66_rav = r66_rav;
		}
		public String getR67_product() {
			return r67_product;
		}
		public void setR67_product(String r67_product) {
			this.r67_product = r67_product;
		}
		public String getR67_client_grp() {
			return r67_client_grp;
		}
		public void setR67_client_grp(String r67_client_grp) {
			this.r67_client_grp = r67_client_grp;
		}
		public BigDecimal getR67_total_book_expo() {
			return r67_total_book_expo;
		}
		public void setR67_total_book_expo(BigDecimal r67_total_book_expo) {
			this.r67_total_book_expo = r67_total_book_expo;
		}
		public BigDecimal getR67_margin_pro() {
			return r67_margin_pro;
		}
		public void setR67_margin_pro(BigDecimal r67_margin_pro) {
			this.r67_margin_pro = r67_margin_pro;
		}
		public BigDecimal getR67_book_expo() {
			return r67_book_expo;
		}
		public void setR67_book_expo(BigDecimal r67_book_expo) {
			this.r67_book_expo = r67_book_expo;
		}
		public BigDecimal getR67_ccf_cont() {
			return r67_ccf_cont;
		}
		public void setR67_ccf_cont(BigDecimal r67_ccf_cont) {
			this.r67_ccf_cont = r67_ccf_cont;
		}
		public BigDecimal getR67_equiv_value() {
			return r67_equiv_value;
		}
		public void setR67_equiv_value(BigDecimal r67_equiv_value) {
			this.r67_equiv_value = r67_equiv_value;
		}
		public BigDecimal getR67_rw_obligant() {
			return r67_rw_obligant;
		}
		public void setR67_rw_obligant(BigDecimal r67_rw_obligant) {
			this.r67_rw_obligant = r67_rw_obligant;
		}
		public BigDecimal getR67_rav() {
			return r67_rav;
		}
		public void setR67_rav(BigDecimal r67_rav) {
			this.r67_rav = r67_rav;
		}
		public String getR68_product() {
			return r68_product;
		}
		public void setR68_product(String r68_product) {
			this.r68_product = r68_product;
		}
		public String getR68_client_grp() {
			return r68_client_grp;
		}
		public void setR68_client_grp(String r68_client_grp) {
			this.r68_client_grp = r68_client_grp;
		}
		public BigDecimal getR68_total_book_expo() {
			return r68_total_book_expo;
		}
		public void setR68_total_book_expo(BigDecimal r68_total_book_expo) {
			this.r68_total_book_expo = r68_total_book_expo;
		}
		public BigDecimal getR68_margin_pro() {
			return r68_margin_pro;
		}
		public void setR68_margin_pro(BigDecimal r68_margin_pro) {
			this.r68_margin_pro = r68_margin_pro;
		}
		public BigDecimal getR68_book_expo() {
			return r68_book_expo;
		}
		public void setR68_book_expo(BigDecimal r68_book_expo) {
			this.r68_book_expo = r68_book_expo;
		}
		public BigDecimal getR68_ccf_cont() {
			return r68_ccf_cont;
		}
		public void setR68_ccf_cont(BigDecimal r68_ccf_cont) {
			this.r68_ccf_cont = r68_ccf_cont;
		}
		public BigDecimal getR68_equiv_value() {
			return r68_equiv_value;
		}
		public void setR68_equiv_value(BigDecimal r68_equiv_value) {
			this.r68_equiv_value = r68_equiv_value;
		}
		public BigDecimal getR68_rw_obligant() {
			return r68_rw_obligant;
		}
		public void setR68_rw_obligant(BigDecimal r68_rw_obligant) {
			this.r68_rw_obligant = r68_rw_obligant;
		}
		public BigDecimal getR68_rav() {
			return r68_rav;
		}
		public void setR68_rav(BigDecimal r68_rav) {
			this.r68_rav = r68_rav;
		}
		public String getR69_product() {
			return r69_product;
		}
		public void setR69_product(String r69_product) {
			this.r69_product = r69_product;
		}
		public String getR69_client_grp() {
			return r69_client_grp;
		}
		public void setR69_client_grp(String r69_client_grp) {
			this.r69_client_grp = r69_client_grp;
		}
		public BigDecimal getR69_total_book_expo() {
			return r69_total_book_expo;
		}
		public void setR69_total_book_expo(BigDecimal r69_total_book_expo) {
			this.r69_total_book_expo = r69_total_book_expo;
		}
		public BigDecimal getR69_margin_pro() {
			return r69_margin_pro;
		}
		public void setR69_margin_pro(BigDecimal r69_margin_pro) {
			this.r69_margin_pro = r69_margin_pro;
		}
		public BigDecimal getR69_book_expo() {
			return r69_book_expo;
		}
		public void setR69_book_expo(BigDecimal r69_book_expo) {
			this.r69_book_expo = r69_book_expo;
		}
		public BigDecimal getR69_ccf_cont() {
			return r69_ccf_cont;
		}
		public void setR69_ccf_cont(BigDecimal r69_ccf_cont) {
			this.r69_ccf_cont = r69_ccf_cont;
		}
		public BigDecimal getR69_equiv_value() {
			return r69_equiv_value;
		}
		public void setR69_equiv_value(BigDecimal r69_equiv_value) {
			this.r69_equiv_value = r69_equiv_value;
		}
		public BigDecimal getR69_rw_obligant() {
			return r69_rw_obligant;
		}
		public void setR69_rw_obligant(BigDecimal r69_rw_obligant) {
			this.r69_rw_obligant = r69_rw_obligant;
		}
		public BigDecimal getR69_rav() {
			return r69_rav;
		}
		public void setR69_rav(BigDecimal r69_rav) {
			this.r69_rav = r69_rav;
		}
		public String getR70_product() {
			return r70_product;
		}
		public void setR70_product(String r70_product) {
			this.r70_product = r70_product;
		}
		public String getR70_client_grp() {
			return r70_client_grp;
		}
		public void setR70_client_grp(String r70_client_grp) {
			this.r70_client_grp = r70_client_grp;
		}
		public BigDecimal getR70_total_book_expo() {
			return r70_total_book_expo;
		}
		public void setR70_total_book_expo(BigDecimal r70_total_book_expo) {
			this.r70_total_book_expo = r70_total_book_expo;
		}
		public BigDecimal getR70_margin_pro() {
			return r70_margin_pro;
		}
		public void setR70_margin_pro(BigDecimal r70_margin_pro) {
			this.r70_margin_pro = r70_margin_pro;
		}
		public BigDecimal getR70_book_expo() {
			return r70_book_expo;
		}
		public void setR70_book_expo(BigDecimal r70_book_expo) {
			this.r70_book_expo = r70_book_expo;
		}
		public BigDecimal getR70_ccf_cont() {
			return r70_ccf_cont;
		}
		public void setR70_ccf_cont(BigDecimal r70_ccf_cont) {
			this.r70_ccf_cont = r70_ccf_cont;
		}
		public BigDecimal getR70_equiv_value() {
			return r70_equiv_value;
		}
		public void setR70_equiv_value(BigDecimal r70_equiv_value) {
			this.r70_equiv_value = r70_equiv_value;
		}
		public BigDecimal getR70_rw_obligant() {
			return r70_rw_obligant;
		}
		public void setR70_rw_obligant(BigDecimal r70_rw_obligant) {
			this.r70_rw_obligant = r70_rw_obligant;
		}
		public BigDecimal getR70_rav() {
			return r70_rav;
		}
		public void setR70_rav(BigDecimal r70_rav) {
			this.r70_rav = r70_rav;
		}
		public String getR71_product() {
			return r71_product;
		}
		public void setR71_product(String r71_product) {
			this.r71_product = r71_product;
		}
		public String getR71_client_grp() {
			return r71_client_grp;
		}
		public void setR71_client_grp(String r71_client_grp) {
			this.r71_client_grp = r71_client_grp;
		}
		public BigDecimal getR71_total_book_expo() {
			return r71_total_book_expo;
		}
		public void setR71_total_book_expo(BigDecimal r71_total_book_expo) {
			this.r71_total_book_expo = r71_total_book_expo;
		}
		public BigDecimal getR71_margin_pro() {
			return r71_margin_pro;
		}
		public void setR71_margin_pro(BigDecimal r71_margin_pro) {
			this.r71_margin_pro = r71_margin_pro;
		}
		public BigDecimal getR71_book_expo() {
			return r71_book_expo;
		}
		public void setR71_book_expo(BigDecimal r71_book_expo) {
			this.r71_book_expo = r71_book_expo;
		}
		public BigDecimal getR71_ccf_cont() {
			return r71_ccf_cont;
		}
		public void setR71_ccf_cont(BigDecimal r71_ccf_cont) {
			this.r71_ccf_cont = r71_ccf_cont;
		}
		public BigDecimal getR71_equiv_value() {
			return r71_equiv_value;
		}
		public void setR71_equiv_value(BigDecimal r71_equiv_value) {
			this.r71_equiv_value = r71_equiv_value;
		}
		public BigDecimal getR71_rw_obligant() {
			return r71_rw_obligant;
		}
		public void setR71_rw_obligant(BigDecimal r71_rw_obligant) {
			this.r71_rw_obligant = r71_rw_obligant;
		}
		public BigDecimal getR71_rav() {
			return r71_rav;
		}
		public void setR71_rav(BigDecimal r71_rav) {
			this.r71_rav = r71_rav;
		}
		public String getR72_product() {
			return r72_product;
		}
		public void setR72_product(String r72_product) {
			this.r72_product = r72_product;
		}
		public String getR72_client_grp() {
			return r72_client_grp;
		}
		public void setR72_client_grp(String r72_client_grp) {
			this.r72_client_grp = r72_client_grp;
		}
		public BigDecimal getR72_total_book_expo() {
			return r72_total_book_expo;
		}
		public void setR72_total_book_expo(BigDecimal r72_total_book_expo) {
			this.r72_total_book_expo = r72_total_book_expo;
		}
		public BigDecimal getR72_margin_pro() {
			return r72_margin_pro;
		}
		public void setR72_margin_pro(BigDecimal r72_margin_pro) {
			this.r72_margin_pro = r72_margin_pro;
		}
		public BigDecimal getR72_book_expo() {
			return r72_book_expo;
		}
		public void setR72_book_expo(BigDecimal r72_book_expo) {
			this.r72_book_expo = r72_book_expo;
		}
		public BigDecimal getR72_ccf_cont() {
			return r72_ccf_cont;
		}
		public void setR72_ccf_cont(BigDecimal r72_ccf_cont) {
			this.r72_ccf_cont = r72_ccf_cont;
		}
		public BigDecimal getR72_equiv_value() {
			return r72_equiv_value;
		}
		public void setR72_equiv_value(BigDecimal r72_equiv_value) {
			this.r72_equiv_value = r72_equiv_value;
		}
		public BigDecimal getR72_rw_obligant() {
			return r72_rw_obligant;
		}
		public void setR72_rw_obligant(BigDecimal r72_rw_obligant) {
			this.r72_rw_obligant = r72_rw_obligant;
		}
		public BigDecimal getR72_rav() {
			return r72_rav;
		}
		public void setR72_rav(BigDecimal r72_rav) {
			this.r72_rav = r72_rav;
		}
		public String getR73_product() {
			return r73_product;
		}
		public void setR73_product(String r73_product) {
			this.r73_product = r73_product;
		}
		public String getR73_client_grp() {
			return r73_client_grp;
		}
		public void setR73_client_grp(String r73_client_grp) {
			this.r73_client_grp = r73_client_grp;
		}
		public BigDecimal getR73_total_book_expo() {
			return r73_total_book_expo;
		}
		public void setR73_total_book_expo(BigDecimal r73_total_book_expo) {
			this.r73_total_book_expo = r73_total_book_expo;
		}
		public BigDecimal getR73_margin_pro() {
			return r73_margin_pro;
		}
		public void setR73_margin_pro(BigDecimal r73_margin_pro) {
			this.r73_margin_pro = r73_margin_pro;
		}
		public BigDecimal getR73_book_expo() {
			return r73_book_expo;
		}
		public void setR73_book_expo(BigDecimal r73_book_expo) {
			this.r73_book_expo = r73_book_expo;
		}
		public BigDecimal getR73_ccf_cont() {
			return r73_ccf_cont;
		}
		public void setR73_ccf_cont(BigDecimal r73_ccf_cont) {
			this.r73_ccf_cont = r73_ccf_cont;
		}
		public BigDecimal getR73_equiv_value() {
			return r73_equiv_value;
		}
		public void setR73_equiv_value(BigDecimal r73_equiv_value) {
			this.r73_equiv_value = r73_equiv_value;
		}
		public BigDecimal getR73_rw_obligant() {
			return r73_rw_obligant;
		}
		public void setR73_rw_obligant(BigDecimal r73_rw_obligant) {
			this.r73_rw_obligant = r73_rw_obligant;
		}
		public BigDecimal getR73_rav() {
			return r73_rav;
		}
		public void setR73_rav(BigDecimal r73_rav) {
			this.r73_rav = r73_rav;
		}
		public String getR74_product() {
			return r74_product;
		}
		public void setR74_product(String r74_product) {
			this.r74_product = r74_product;
		}
		public String getR74_client_grp() {
			return r74_client_grp;
		}
		public void setR74_client_grp(String r74_client_grp) {
			this.r74_client_grp = r74_client_grp;
		}
		public BigDecimal getR74_total_book_expo() {
			return r74_total_book_expo;
		}
		public void setR74_total_book_expo(BigDecimal r74_total_book_expo) {
			this.r74_total_book_expo = r74_total_book_expo;
		}
		public BigDecimal getR74_margin_pro() {
			return r74_margin_pro;
		}
		public void setR74_margin_pro(BigDecimal r74_margin_pro) {
			this.r74_margin_pro = r74_margin_pro;
		}
		public BigDecimal getR74_book_expo() {
			return r74_book_expo;
		}
		public void setR74_book_expo(BigDecimal r74_book_expo) {
			this.r74_book_expo = r74_book_expo;
		}
		public BigDecimal getR74_ccf_cont() {
			return r74_ccf_cont;
		}
		public void setR74_ccf_cont(BigDecimal r74_ccf_cont) {
			this.r74_ccf_cont = r74_ccf_cont;
		}
		public BigDecimal getR74_equiv_value() {
			return r74_equiv_value;
		}
		public void setR74_equiv_value(BigDecimal r74_equiv_value) {
			this.r74_equiv_value = r74_equiv_value;
		}
		public BigDecimal getR74_rw_obligant() {
			return r74_rw_obligant;
		}
		public void setR74_rw_obligant(BigDecimal r74_rw_obligant) {
			this.r74_rw_obligant = r74_rw_obligant;
		}
		public BigDecimal getR74_rav() {
			return r74_rav;
		}
		public void setR74_rav(BigDecimal r74_rav) {
			this.r74_rav = r74_rav;
		}
		public String getR75_product() {
			return r75_product;
		}
		public void setR75_product(String r75_product) {
			this.r75_product = r75_product;
		}
		public String getR75_client_grp() {
			return r75_client_grp;
		}
		public void setR75_client_grp(String r75_client_grp) {
			this.r75_client_grp = r75_client_grp;
		}
		public BigDecimal getR75_total_book_expo() {
			return r75_total_book_expo;
		}
		public void setR75_total_book_expo(BigDecimal r75_total_book_expo) {
			this.r75_total_book_expo = r75_total_book_expo;
		}
		public BigDecimal getR75_margin_pro() {
			return r75_margin_pro;
		}
		public void setR75_margin_pro(BigDecimal r75_margin_pro) {
			this.r75_margin_pro = r75_margin_pro;
		}
		public BigDecimal getR75_book_expo() {
			return r75_book_expo;
		}
		public void setR75_book_expo(BigDecimal r75_book_expo) {
			this.r75_book_expo = r75_book_expo;
		}
		public BigDecimal getR75_ccf_cont() {
			return r75_ccf_cont;
		}
		public void setR75_ccf_cont(BigDecimal r75_ccf_cont) {
			this.r75_ccf_cont = r75_ccf_cont;
		}
		public BigDecimal getR75_equiv_value() {
			return r75_equiv_value;
		}
		public void setR75_equiv_value(BigDecimal r75_equiv_value) {
			this.r75_equiv_value = r75_equiv_value;
		}
		public BigDecimal getR75_rw_obligant() {
			return r75_rw_obligant;
		}
		public void setR75_rw_obligant(BigDecimal r75_rw_obligant) {
			this.r75_rw_obligant = r75_rw_obligant;
		}
		public BigDecimal getR75_rav() {
			return r75_rav;
		}
		public void setR75_rav(BigDecimal r75_rav) {
			this.r75_rav = r75_rav;
		}
		public String getR76_product() {
			return r76_product;
		}
		public void setR76_product(String r76_product) {
			this.r76_product = r76_product;
		}
		public String getR76_client_grp() {
			return r76_client_grp;
		}
		public void setR76_client_grp(String r76_client_grp) {
			this.r76_client_grp = r76_client_grp;
		}
		public BigDecimal getR76_total_book_expo() {
			return r76_total_book_expo;
		}
		public void setR76_total_book_expo(BigDecimal r76_total_book_expo) {
			this.r76_total_book_expo = r76_total_book_expo;
		}
		public BigDecimal getR76_margin_pro() {
			return r76_margin_pro;
		}
		public void setR76_margin_pro(BigDecimal r76_margin_pro) {
			this.r76_margin_pro = r76_margin_pro;
		}
		public BigDecimal getR76_book_expo() {
			return r76_book_expo;
		}
		public void setR76_book_expo(BigDecimal r76_book_expo) {
			this.r76_book_expo = r76_book_expo;
		}
		public BigDecimal getR76_ccf_cont() {
			return r76_ccf_cont;
		}
		public void setR76_ccf_cont(BigDecimal r76_ccf_cont) {
			this.r76_ccf_cont = r76_ccf_cont;
		}
		public BigDecimal getR76_equiv_value() {
			return r76_equiv_value;
		}
		public void setR76_equiv_value(BigDecimal r76_equiv_value) {
			this.r76_equiv_value = r76_equiv_value;
		}
		public BigDecimal getR76_rw_obligant() {
			return r76_rw_obligant;
		}
		public void setR76_rw_obligant(BigDecimal r76_rw_obligant) {
			this.r76_rw_obligant = r76_rw_obligant;
		}
		public BigDecimal getR76_rav() {
			return r76_rav;
		}
		public void setR76_rav(BigDecimal r76_rav) {
			this.r76_rav = r76_rav;
		}
		public String getR77_product() {
			return r77_product;
		}
		public void setR77_product(String r77_product) {
			this.r77_product = r77_product;
		}
		public String getR77_client_grp() {
			return r77_client_grp;
		}
		public void setR77_client_grp(String r77_client_grp) {
			this.r77_client_grp = r77_client_grp;
		}
		public BigDecimal getR77_total_book_expo() {
			return r77_total_book_expo;
		}
		public void setR77_total_book_expo(BigDecimal r77_total_book_expo) {
			this.r77_total_book_expo = r77_total_book_expo;
		}
		public BigDecimal getR77_margin_pro() {
			return r77_margin_pro;
		}
		public void setR77_margin_pro(BigDecimal r77_margin_pro) {
			this.r77_margin_pro = r77_margin_pro;
		}
		public BigDecimal getR77_book_expo() {
			return r77_book_expo;
		}
		public void setR77_book_expo(BigDecimal r77_book_expo) {
			this.r77_book_expo = r77_book_expo;
		}
		public BigDecimal getR77_ccf_cont() {
			return r77_ccf_cont;
		}
		public void setR77_ccf_cont(BigDecimal r77_ccf_cont) {
			this.r77_ccf_cont = r77_ccf_cont;
		}
		public BigDecimal getR77_equiv_value() {
			return r77_equiv_value;
		}
		public void setR77_equiv_value(BigDecimal r77_equiv_value) {
			this.r77_equiv_value = r77_equiv_value;
		}
		public BigDecimal getR77_rw_obligant() {
			return r77_rw_obligant;
		}
		public void setR77_rw_obligant(BigDecimal r77_rw_obligant) {
			this.r77_rw_obligant = r77_rw_obligant;
		}
		public BigDecimal getR77_rav() {
			return r77_rav;
		}
		public void setR77_rav(BigDecimal r77_rav) {
			this.r77_rav = r77_rav;
		}
		public String getR78_product() {
			return r78_product;
		}
		public void setR78_product(String r78_product) {
			this.r78_product = r78_product;
		}
		public String getR78_client_grp() {
			return r78_client_grp;
		}
		public void setR78_client_grp(String r78_client_grp) {
			this.r78_client_grp = r78_client_grp;
		}
		public BigDecimal getR78_total_book_expo() {
			return r78_total_book_expo;
		}
		public void setR78_total_book_expo(BigDecimal r78_total_book_expo) {
			this.r78_total_book_expo = r78_total_book_expo;
		}
		public BigDecimal getR78_margin_pro() {
			return r78_margin_pro;
		}
		public void setR78_margin_pro(BigDecimal r78_margin_pro) {
			this.r78_margin_pro = r78_margin_pro;
		}
		public BigDecimal getR78_book_expo() {
			return r78_book_expo;
		}
		public void setR78_book_expo(BigDecimal r78_book_expo) {
			this.r78_book_expo = r78_book_expo;
		}
		public BigDecimal getR78_ccf_cont() {
			return r78_ccf_cont;
		}
		public void setR78_ccf_cont(BigDecimal r78_ccf_cont) {
			this.r78_ccf_cont = r78_ccf_cont;
		}
		public BigDecimal getR78_equiv_value() {
			return r78_equiv_value;
		}
		public void setR78_equiv_value(BigDecimal r78_equiv_value) {
			this.r78_equiv_value = r78_equiv_value;
		}
		public BigDecimal getR78_rw_obligant() {
			return r78_rw_obligant;
		}
		public void setR78_rw_obligant(BigDecimal r78_rw_obligant) {
			this.r78_rw_obligant = r78_rw_obligant;
		}
		public BigDecimal getR78_rav() {
			return r78_rav;
		}
		public void setR78_rav(BigDecimal r78_rav) {
			this.r78_rav = r78_rav;
		}
		public String getR79_product() {
			return r79_product;
		}
		public void setR79_product(String r79_product) {
			this.r79_product = r79_product;
		}
		public String getR79_client_grp() {
			return r79_client_grp;
		}
		public void setR79_client_grp(String r79_client_grp) {
			this.r79_client_grp = r79_client_grp;
		}
		public BigDecimal getR79_total_book_expo() {
			return r79_total_book_expo;
		}
		public void setR79_total_book_expo(BigDecimal r79_total_book_expo) {
			this.r79_total_book_expo = r79_total_book_expo;
		}
		public BigDecimal getR79_margin_pro() {
			return r79_margin_pro;
		}
		public void setR79_margin_pro(BigDecimal r79_margin_pro) {
			this.r79_margin_pro = r79_margin_pro;
		}
		public BigDecimal getR79_book_expo() {
			return r79_book_expo;
		}
		public void setR79_book_expo(BigDecimal r79_book_expo) {
			this.r79_book_expo = r79_book_expo;
		}
		public BigDecimal getR79_ccf_cont() {
			return r79_ccf_cont;
		}
		public void setR79_ccf_cont(BigDecimal r79_ccf_cont) {
			this.r79_ccf_cont = r79_ccf_cont;
		}
		public BigDecimal getR79_equiv_value() {
			return r79_equiv_value;
		}
		public void setR79_equiv_value(BigDecimal r79_equiv_value) {
			this.r79_equiv_value = r79_equiv_value;
		}
		public BigDecimal getR79_rw_obligant() {
			return r79_rw_obligant;
		}
		public void setR79_rw_obligant(BigDecimal r79_rw_obligant) {
			this.r79_rw_obligant = r79_rw_obligant;
		}
		public BigDecimal getR79_rav() {
			return r79_rav;
		}
		public void setR79_rav(BigDecimal r79_rav) {
			this.r79_rav = r79_rav;
		}
		public String getR80_product() {
			return r80_product;
		}
		public void setR80_product(String r80_product) {
			this.r80_product = r80_product;
		}
		public String getR80_client_grp() {
			return r80_client_grp;
		}
		public void setR80_client_grp(String r80_client_grp) {
			this.r80_client_grp = r80_client_grp;
		}
		public BigDecimal getR80_total_book_expo() {
			return r80_total_book_expo;
		}
		public void setR80_total_book_expo(BigDecimal r80_total_book_expo) {
			this.r80_total_book_expo = r80_total_book_expo;
		}
		public BigDecimal getR80_margin_pro() {
			return r80_margin_pro;
		}
		public void setR80_margin_pro(BigDecimal r80_margin_pro) {
			this.r80_margin_pro = r80_margin_pro;
		}
		public BigDecimal getR80_book_expo() {
			return r80_book_expo;
		}
		public void setR80_book_expo(BigDecimal r80_book_expo) {
			this.r80_book_expo = r80_book_expo;
		}
		public BigDecimal getR80_ccf_cont() {
			return r80_ccf_cont;
		}
		public void setR80_ccf_cont(BigDecimal r80_ccf_cont) {
			this.r80_ccf_cont = r80_ccf_cont;
		}
		public BigDecimal getR80_equiv_value() {
			return r80_equiv_value;
		}
		public void setR80_equiv_value(BigDecimal r80_equiv_value) {
			this.r80_equiv_value = r80_equiv_value;
		}
		public BigDecimal getR80_rw_obligant() {
			return r80_rw_obligant;
		}
		public void setR80_rw_obligant(BigDecimal r80_rw_obligant) {
			this.r80_rw_obligant = r80_rw_obligant;
		}
		public BigDecimal getR80_rav() {
			return r80_rav;
		}
		public void setR80_rav(BigDecimal r80_rav) {
			this.r80_rav = r80_rav;
		}
		public String getR81_product() {
			return r81_product;
		}
		public void setR81_product(String r81_product) {
			this.r81_product = r81_product;
		}
		public String getR81_client_grp() {
			return r81_client_grp;
		}
		public void setR81_client_grp(String r81_client_grp) {
			this.r81_client_grp = r81_client_grp;
		}
		public BigDecimal getR81_total_book_expo() {
			return r81_total_book_expo;
		}
		public void setR81_total_book_expo(BigDecimal r81_total_book_expo) {
			this.r81_total_book_expo = r81_total_book_expo;
		}
		public BigDecimal getR81_margin_pro() {
			return r81_margin_pro;
		}
		public void setR81_margin_pro(BigDecimal r81_margin_pro) {
			this.r81_margin_pro = r81_margin_pro;
		}
		public BigDecimal getR81_book_expo() {
			return r81_book_expo;
		}
		public void setR81_book_expo(BigDecimal r81_book_expo) {
			this.r81_book_expo = r81_book_expo;
		}
		public BigDecimal getR81_ccf_cont() {
			return r81_ccf_cont;
		}
		public void setR81_ccf_cont(BigDecimal r81_ccf_cont) {
			this.r81_ccf_cont = r81_ccf_cont;
		}
		public BigDecimal getR81_equiv_value() {
			return r81_equiv_value;
		}
		public void setR81_equiv_value(BigDecimal r81_equiv_value) {
			this.r81_equiv_value = r81_equiv_value;
		}
		public BigDecimal getR81_rw_obligant() {
			return r81_rw_obligant;
		}
		public void setR81_rw_obligant(BigDecimal r81_rw_obligant) {
			this.r81_rw_obligant = r81_rw_obligant;
		}
		public BigDecimal getR81_rav() {
			return r81_rav;
		}
		public void setR81_rav(BigDecimal r81_rav) {
			this.r81_rav = r81_rav;
		}
		public String getR82_product() {
			return r82_product;
		}
		public void setR82_product(String r82_product) {
			this.r82_product = r82_product;
		}
		public String getR82_client_grp() {
			return r82_client_grp;
		}
		public void setR82_client_grp(String r82_client_grp) {
			this.r82_client_grp = r82_client_grp;
		}
		public BigDecimal getR82_total_book_expo() {
			return r82_total_book_expo;
		}
		public void setR82_total_book_expo(BigDecimal r82_total_book_expo) {
			this.r82_total_book_expo = r82_total_book_expo;
		}
		public BigDecimal getR82_margin_pro() {
			return r82_margin_pro;
		}
		public void setR82_margin_pro(BigDecimal r82_margin_pro) {
			this.r82_margin_pro = r82_margin_pro;
		}
		public BigDecimal getR82_book_expo() {
			return r82_book_expo;
		}
		public void setR82_book_expo(BigDecimal r82_book_expo) {
			this.r82_book_expo = r82_book_expo;
		}
		public BigDecimal getR82_ccf_cont() {
			return r82_ccf_cont;
		}
		public void setR82_ccf_cont(BigDecimal r82_ccf_cont) {
			this.r82_ccf_cont = r82_ccf_cont;
		}
		public BigDecimal getR82_equiv_value() {
			return r82_equiv_value;
		}
		public void setR82_equiv_value(BigDecimal r82_equiv_value) {
			this.r82_equiv_value = r82_equiv_value;
		}
		public BigDecimal getR82_rw_obligant() {
			return r82_rw_obligant;
		}
		public void setR82_rw_obligant(BigDecimal r82_rw_obligant) {
			this.r82_rw_obligant = r82_rw_obligant;
		}
		public BigDecimal getR82_rav() {
			return r82_rav;
		}
		public void setR82_rav(BigDecimal r82_rav) {
			this.r82_rav = r82_rav;
		}
		public String getR83_product() {
			return r83_product;
		}
		public void setR83_product(String r83_product) {
			this.r83_product = r83_product;
		}
		public String getR83_client_grp() {
			return r83_client_grp;
		}
		public void setR83_client_grp(String r83_client_grp) {
			this.r83_client_grp = r83_client_grp;
		}
		public BigDecimal getR83_total_book_expo() {
			return r83_total_book_expo;
		}
		public void setR83_total_book_expo(BigDecimal r83_total_book_expo) {
			this.r83_total_book_expo = r83_total_book_expo;
		}
		public BigDecimal getR83_margin_pro() {
			return r83_margin_pro;
		}
		public void setR83_margin_pro(BigDecimal r83_margin_pro) {
			this.r83_margin_pro = r83_margin_pro;
		}
		public BigDecimal getR83_book_expo() {
			return r83_book_expo;
		}
		public void setR83_book_expo(BigDecimal r83_book_expo) {
			this.r83_book_expo = r83_book_expo;
		}
		public BigDecimal getR83_ccf_cont() {
			return r83_ccf_cont;
		}
		public void setR83_ccf_cont(BigDecimal r83_ccf_cont) {
			this.r83_ccf_cont = r83_ccf_cont;
		}
		public BigDecimal getR83_equiv_value() {
			return r83_equiv_value;
		}
		public void setR83_equiv_value(BigDecimal r83_equiv_value) {
			this.r83_equiv_value = r83_equiv_value;
		}
		public BigDecimal getR83_rw_obligant() {
			return r83_rw_obligant;
		}
		public void setR83_rw_obligant(BigDecimal r83_rw_obligant) {
			this.r83_rw_obligant = r83_rw_obligant;
		}
		public BigDecimal getR83_rav() {
			return r83_rav;
		}
		public void setR83_rav(BigDecimal r83_rav) {
			this.r83_rav = r83_rav;
		}
		public String getR84_product() {
			return r84_product;
		}
		public void setR84_product(String r84_product) {
			this.r84_product = r84_product;
		}
		public String getR84_client_grp() {
			return r84_client_grp;
		}
		public void setR84_client_grp(String r84_client_grp) {
			this.r84_client_grp = r84_client_grp;
		}
		public BigDecimal getR84_total_book_expo() {
			return r84_total_book_expo;
		}
		public void setR84_total_book_expo(BigDecimal r84_total_book_expo) {
			this.r84_total_book_expo = r84_total_book_expo;
		}
		public BigDecimal getR84_margin_pro() {
			return r84_margin_pro;
		}
		public void setR84_margin_pro(BigDecimal r84_margin_pro) {
			this.r84_margin_pro = r84_margin_pro;
		}
		public BigDecimal getR84_book_expo() {
			return r84_book_expo;
		}
		public void setR84_book_expo(BigDecimal r84_book_expo) {
			this.r84_book_expo = r84_book_expo;
		}
		public BigDecimal getR84_ccf_cont() {
			return r84_ccf_cont;
		}
		public void setR84_ccf_cont(BigDecimal r84_ccf_cont) {
			this.r84_ccf_cont = r84_ccf_cont;
		}
		public BigDecimal getR84_equiv_value() {
			return r84_equiv_value;
		}
		public void setR84_equiv_value(BigDecimal r84_equiv_value) {
			this.r84_equiv_value = r84_equiv_value;
		}
		public BigDecimal getR84_rw_obligant() {
			return r84_rw_obligant;
		}
		public void setR84_rw_obligant(BigDecimal r84_rw_obligant) {
			this.r84_rw_obligant = r84_rw_obligant;
		}
		public BigDecimal getR84_rav() {
			return r84_rav;
		}
		public void setR84_rav(BigDecimal r84_rav) {
			this.r84_rav = r84_rav;
		}
		public String getR100_product() {
			return r100_product;
		}
		public void setR100_product(String r100_product) {
			this.r100_product = r100_product;
		}
		public String getR100_client_grp() {
			return r100_client_grp;
		}
		public void setR100_client_grp(String r100_client_grp) {
			this.r100_client_grp = r100_client_grp;
		}
		public BigDecimal getR100_total_book_expo() {
			return r100_total_book_expo;
		}
		public void setR100_total_book_expo(BigDecimal r100_total_book_expo) {
			this.r100_total_book_expo = r100_total_book_expo;
		}
		public BigDecimal getR100_margin_pro() {
			return r100_margin_pro;
		}
		public void setR100_margin_pro(BigDecimal r100_margin_pro) {
			this.r100_margin_pro = r100_margin_pro;
		}
		public BigDecimal getR100_book_expo() {
			return r100_book_expo;
		}
		public void setR100_book_expo(BigDecimal r100_book_expo) {
			this.r100_book_expo = r100_book_expo;
		}
		public BigDecimal getR100_ccf_cont() {
			return r100_ccf_cont;
		}
		public void setR100_ccf_cont(BigDecimal r100_ccf_cont) {
			this.r100_ccf_cont = r100_ccf_cont;
		}
		public BigDecimal getR100_equiv_value() {
			return r100_equiv_value;
		}
		public void setR100_equiv_value(BigDecimal r100_equiv_value) {
			this.r100_equiv_value = r100_equiv_value;
		}
		public BigDecimal getR100_rw_obligant() {
			return r100_rw_obligant;
		}
		public void setR100_rw_obligant(BigDecimal r100_rw_obligant) {
			this.r100_rw_obligant = r100_rw_obligant;
		}
		public BigDecimal getR100_rav() {
			return r100_rav;
		}
		public void setR100_rav(BigDecimal r100_rav) {
			this.r100_rav = r100_rav;
		}
		public String getR101_product() {
			return r101_product;
		}
		public void setR101_product(String r101_product) {
			this.r101_product = r101_product;
		}
		public String getR101_client_grp() {
			return r101_client_grp;
		}
		public void setR101_client_grp(String r101_client_grp) {
			this.r101_client_grp = r101_client_grp;
		}
		public BigDecimal getR101_total_book_expo() {
			return r101_total_book_expo;
		}
		public void setR101_total_book_expo(BigDecimal r101_total_book_expo) {
			this.r101_total_book_expo = r101_total_book_expo;
		}
		public BigDecimal getR101_margin_pro() {
			return r101_margin_pro;
		}
		public void setR101_margin_pro(BigDecimal r101_margin_pro) {
			this.r101_margin_pro = r101_margin_pro;
		}
		public BigDecimal getR101_book_expo() {
			return r101_book_expo;
		}
		public void setR101_book_expo(BigDecimal r101_book_expo) {
			this.r101_book_expo = r101_book_expo;
		}
		public BigDecimal getR101_ccf_cont() {
			return r101_ccf_cont;
		}
		public void setR101_ccf_cont(BigDecimal r101_ccf_cont) {
			this.r101_ccf_cont = r101_ccf_cont;
		}
		public BigDecimal getR101_equiv_value() {
			return r101_equiv_value;
		}
		public void setR101_equiv_value(BigDecimal r101_equiv_value) {
			this.r101_equiv_value = r101_equiv_value;
		}
		public BigDecimal getR101_rw_obligant() {
			return r101_rw_obligant;
		}
		public void setR101_rw_obligant(BigDecimal r101_rw_obligant) {
			this.r101_rw_obligant = r101_rw_obligant;
		}
		public BigDecimal getR101_rav() {
			return r101_rav;
		}
		public void setR101_rav(BigDecimal r101_rav) {
			this.r101_rav = r101_rav;
		}
		public String getR102_product() {
			return r102_product;
		}
		public void setR102_product(String r102_product) {
			this.r102_product = r102_product;
		}
		public String getR102_client_grp() {
			return r102_client_grp;
		}
		public void setR102_client_grp(String r102_client_grp) {
			this.r102_client_grp = r102_client_grp;
		}
		public BigDecimal getR102_total_book_expo() {
			return r102_total_book_expo;
		}
		public void setR102_total_book_expo(BigDecimal r102_total_book_expo) {
			this.r102_total_book_expo = r102_total_book_expo;
		}
		public BigDecimal getR102_margin_pro() {
			return r102_margin_pro;
		}
		public void setR102_margin_pro(BigDecimal r102_margin_pro) {
			this.r102_margin_pro = r102_margin_pro;
		}
		public BigDecimal getR102_book_expo() {
			return r102_book_expo;
		}
		public void setR102_book_expo(BigDecimal r102_book_expo) {
			this.r102_book_expo = r102_book_expo;
		}
		public BigDecimal getR102_ccf_cont() {
			return r102_ccf_cont;
		}
		public void setR102_ccf_cont(BigDecimal r102_ccf_cont) {
			this.r102_ccf_cont = r102_ccf_cont;
		}
		public BigDecimal getR102_equiv_value() {
			return r102_equiv_value;
		}
		public void setR102_equiv_value(BigDecimal r102_equiv_value) {
			this.r102_equiv_value = r102_equiv_value;
		}
		public BigDecimal getR102_rw_obligant() {
			return r102_rw_obligant;
		}
		public void setR102_rw_obligant(BigDecimal r102_rw_obligant) {
			this.r102_rw_obligant = r102_rw_obligant;
		}
		public BigDecimal getR102_rav() {
			return r102_rav;
		}
		public void setR102_rav(BigDecimal r102_rav) {
			this.r102_rav = r102_rav;
		}
		public String getR103_product() {
			return r103_product;
		}
		public void setR103_product(String r103_product) {
			this.r103_product = r103_product;
		}
		public String getR103_client_grp() {
			return r103_client_grp;
		}
		public void setR103_client_grp(String r103_client_grp) {
			this.r103_client_grp = r103_client_grp;
		}
		public BigDecimal getR103_total_book_expo() {
			return r103_total_book_expo;
		}
		public void setR103_total_book_expo(BigDecimal r103_total_book_expo) {
			this.r103_total_book_expo = r103_total_book_expo;
		}
		public BigDecimal getR103_margin_pro() {
			return r103_margin_pro;
		}
		public void setR103_margin_pro(BigDecimal r103_margin_pro) {
			this.r103_margin_pro = r103_margin_pro;
		}
		public BigDecimal getR103_book_expo() {
			return r103_book_expo;
		}
		public void setR103_book_expo(BigDecimal r103_book_expo) {
			this.r103_book_expo = r103_book_expo;
		}
		public BigDecimal getR103_ccf_cont() {
			return r103_ccf_cont;
		}
		public void setR103_ccf_cont(BigDecimal r103_ccf_cont) {
			this.r103_ccf_cont = r103_ccf_cont;
		}
		public BigDecimal getR103_equiv_value() {
			return r103_equiv_value;
		}
		public void setR103_equiv_value(BigDecimal r103_equiv_value) {
			this.r103_equiv_value = r103_equiv_value;
		}
		public BigDecimal getR103_rw_obligant() {
			return r103_rw_obligant;
		}
		public void setR103_rw_obligant(BigDecimal r103_rw_obligant) {
			this.r103_rw_obligant = r103_rw_obligant;
		}
		public BigDecimal getR103_rav() {
			return r103_rav;
		}
		public void setR103_rav(BigDecimal r103_rav) {
			this.r103_rav = r103_rav;
		}
		public String getR104_product() {
			return r104_product;
		}
		public void setR104_product(String r104_product) {
			this.r104_product = r104_product;
		}
		public String getR104_client_grp() {
			return r104_client_grp;
		}
		public void setR104_client_grp(String r104_client_grp) {
			this.r104_client_grp = r104_client_grp;
		}
		public BigDecimal getR104_total_book_expo() {
			return r104_total_book_expo;
		}
		public void setR104_total_book_expo(BigDecimal r104_total_book_expo) {
			this.r104_total_book_expo = r104_total_book_expo;
		}
		public BigDecimal getR104_margin_pro() {
			return r104_margin_pro;
		}
		public void setR104_margin_pro(BigDecimal r104_margin_pro) {
			this.r104_margin_pro = r104_margin_pro;
		}
		public BigDecimal getR104_book_expo() {
			return r104_book_expo;
		}
		public void setR104_book_expo(BigDecimal r104_book_expo) {
			this.r104_book_expo = r104_book_expo;
		}
		public BigDecimal getR104_ccf_cont() {
			return r104_ccf_cont;
		}
		public void setR104_ccf_cont(BigDecimal r104_ccf_cont) {
			this.r104_ccf_cont = r104_ccf_cont;
		}
		public BigDecimal getR104_equiv_value() {
			return r104_equiv_value;
		}
		public void setR104_equiv_value(BigDecimal r104_equiv_value) {
			this.r104_equiv_value = r104_equiv_value;
		}
		public BigDecimal getR104_rw_obligant() {
			return r104_rw_obligant;
		}
		public void setR104_rw_obligant(BigDecimal r104_rw_obligant) {
			this.r104_rw_obligant = r104_rw_obligant;
		}
		public BigDecimal getR104_rav() {
			return r104_rav;
		}
		public void setR104_rav(BigDecimal r104_rav) {
			this.r104_rav = r104_rav;
		}
		public String getR105_product() {
			return r105_product;
		}
		public void setR105_product(String r105_product) {
			this.r105_product = r105_product;
		}
		public String getR105_client_grp() {
			return r105_client_grp;
		}
		public void setR105_client_grp(String r105_client_grp) {
			this.r105_client_grp = r105_client_grp;
		}
		public BigDecimal getR105_total_book_expo() {
			return r105_total_book_expo;
		}
		public void setR105_total_book_expo(BigDecimal r105_total_book_expo) {
			this.r105_total_book_expo = r105_total_book_expo;
		}
		public BigDecimal getR105_margin_pro() {
			return r105_margin_pro;
		}
		public void setR105_margin_pro(BigDecimal r105_margin_pro) {
			this.r105_margin_pro = r105_margin_pro;
		}
		public BigDecimal getR105_book_expo() {
			return r105_book_expo;
		}
		public void setR105_book_expo(BigDecimal r105_book_expo) {
			this.r105_book_expo = r105_book_expo;
		}
		public BigDecimal getR105_ccf_cont() {
			return r105_ccf_cont;
		}
		public void setR105_ccf_cont(BigDecimal r105_ccf_cont) {
			this.r105_ccf_cont = r105_ccf_cont;
		}
		public BigDecimal getR105_equiv_value() {
			return r105_equiv_value;
		}
		public void setR105_equiv_value(BigDecimal r105_equiv_value) {
			this.r105_equiv_value = r105_equiv_value;
		}
		public BigDecimal getR105_rw_obligant() {
			return r105_rw_obligant;
		}
		public void setR105_rw_obligant(BigDecimal r105_rw_obligant) {
			this.r105_rw_obligant = r105_rw_obligant;
		}
		public BigDecimal getR105_rav() {
			return r105_rav;
		}
		public void setR105_rav(BigDecimal r105_rav) {
			this.r105_rav = r105_rav;
		}
		public String getR106_product() {
			return r106_product;
		}
		public void setR106_product(String r106_product) {
			this.r106_product = r106_product;
		}
		public String getR106_client_grp() {
			return r106_client_grp;
		}
		public void setR106_client_grp(String r106_client_grp) {
			this.r106_client_grp = r106_client_grp;
		}
		public BigDecimal getR106_total_book_expo() {
			return r106_total_book_expo;
		}
		public void setR106_total_book_expo(BigDecimal r106_total_book_expo) {
			this.r106_total_book_expo = r106_total_book_expo;
		}
		public BigDecimal getR106_margin_pro() {
			return r106_margin_pro;
		}
		public void setR106_margin_pro(BigDecimal r106_margin_pro) {
			this.r106_margin_pro = r106_margin_pro;
		}
		public BigDecimal getR106_book_expo() {
			return r106_book_expo;
		}
		public void setR106_book_expo(BigDecimal r106_book_expo) {
			this.r106_book_expo = r106_book_expo;
		}
		public BigDecimal getR106_ccf_cont() {
			return r106_ccf_cont;
		}
		public void setR106_ccf_cont(BigDecimal r106_ccf_cont) {
			this.r106_ccf_cont = r106_ccf_cont;
		}
		public BigDecimal getR106_equiv_value() {
			return r106_equiv_value;
		}
		public void setR106_equiv_value(BigDecimal r106_equiv_value) {
			this.r106_equiv_value = r106_equiv_value;
		}
		public BigDecimal getR106_rw_obligant() {
			return r106_rw_obligant;
		}
		public void setR106_rw_obligant(BigDecimal r106_rw_obligant) {
			this.r106_rw_obligant = r106_rw_obligant;
		}
		public BigDecimal getR106_rav() {
			return r106_rav;
		}
		public void setR106_rav(BigDecimal r106_rav) {
			this.r106_rav = r106_rav;
		}
		public String getR107_product() {
			return r107_product;
		}
		public void setR107_product(String r107_product) {
			this.r107_product = r107_product;
		}
		public String getR107_client_grp() {
			return r107_client_grp;
		}
		public void setR107_client_grp(String r107_client_grp) {
			this.r107_client_grp = r107_client_grp;
		}
		public BigDecimal getR107_total_book_expo() {
			return r107_total_book_expo;
		}
		public void setR107_total_book_expo(BigDecimal r107_total_book_expo) {
			this.r107_total_book_expo = r107_total_book_expo;
		}
		public BigDecimal getR107_margin_pro() {
			return r107_margin_pro;
		}
		public void setR107_margin_pro(BigDecimal r107_margin_pro) {
			this.r107_margin_pro = r107_margin_pro;
		}
		public BigDecimal getR107_book_expo() {
			return r107_book_expo;
		}
		public void setR107_book_expo(BigDecimal r107_book_expo) {
			this.r107_book_expo = r107_book_expo;
		}
		public BigDecimal getR107_ccf_cont() {
			return r107_ccf_cont;
		}
		public void setR107_ccf_cont(BigDecimal r107_ccf_cont) {
			this.r107_ccf_cont = r107_ccf_cont;
		}
		public BigDecimal getR107_equiv_value() {
			return r107_equiv_value;
		}
		public void setR107_equiv_value(BigDecimal r107_equiv_value) {
			this.r107_equiv_value = r107_equiv_value;
		}
		public BigDecimal getR107_rw_obligant() {
			return r107_rw_obligant;
		}
		public void setR107_rw_obligant(BigDecimal r107_rw_obligant) {
			this.r107_rw_obligant = r107_rw_obligant;
		}
		public BigDecimal getR107_rav() {
			return r107_rav;
		}
		public void setR107_rav(BigDecimal r107_rav) {
			this.r107_rav = r107_rav;
		}
		public String getR108_product() {
			return r108_product;
		}
		public void setR108_product(String r108_product) {
			this.r108_product = r108_product;
		}
		public String getR108_client_grp() {
			return r108_client_grp;
		}
		public void setR108_client_grp(String r108_client_grp) {
			this.r108_client_grp = r108_client_grp;
		}
		public BigDecimal getR108_total_book_expo() {
			return r108_total_book_expo;
		}
		public void setR108_total_book_expo(BigDecimal r108_total_book_expo) {
			this.r108_total_book_expo = r108_total_book_expo;
		}
		public BigDecimal getR108_margin_pro() {
			return r108_margin_pro;
		}
		public void setR108_margin_pro(BigDecimal r108_margin_pro) {
			this.r108_margin_pro = r108_margin_pro;
		}
		public BigDecimal getR108_book_expo() {
			return r108_book_expo;
		}
		public void setR108_book_expo(BigDecimal r108_book_expo) {
			this.r108_book_expo = r108_book_expo;
		}
		public BigDecimal getR108_ccf_cont() {
			return r108_ccf_cont;
		}
		public void setR108_ccf_cont(BigDecimal r108_ccf_cont) {
			this.r108_ccf_cont = r108_ccf_cont;
		}
		public BigDecimal getR108_equiv_value() {
			return r108_equiv_value;
		}
		public void setR108_equiv_value(BigDecimal r108_equiv_value) {
			this.r108_equiv_value = r108_equiv_value;
		}
		public BigDecimal getR108_rw_obligant() {
			return r108_rw_obligant;
		}
		public void setR108_rw_obligant(BigDecimal r108_rw_obligant) {
			this.r108_rw_obligant = r108_rw_obligant;
		}
		public BigDecimal getR108_rav() {
			return r108_rav;
		}
		public void setR108_rav(BigDecimal r108_rav) {
			this.r108_rav = r108_rav;
		}
		public String getR109_product() {
			return r109_product;
		}
		public void setR109_product(String r109_product) {
			this.r109_product = r109_product;
		}
		public String getR109_client_grp() {
			return r109_client_grp;
		}
		public void setR109_client_grp(String r109_client_grp) {
			this.r109_client_grp = r109_client_grp;
		}
		public BigDecimal getR109_total_book_expo() {
			return r109_total_book_expo;
		}
		public void setR109_total_book_expo(BigDecimal r109_total_book_expo) {
			this.r109_total_book_expo = r109_total_book_expo;
		}
		public BigDecimal getR109_margin_pro() {
			return r109_margin_pro;
		}
		public void setR109_margin_pro(BigDecimal r109_margin_pro) {
			this.r109_margin_pro = r109_margin_pro;
		}
		public BigDecimal getR109_book_expo() {
			return r109_book_expo;
		}
		public void setR109_book_expo(BigDecimal r109_book_expo) {
			this.r109_book_expo = r109_book_expo;
		}
		public BigDecimal getR109_ccf_cont() {
			return r109_ccf_cont;
		}
		public void setR109_ccf_cont(BigDecimal r109_ccf_cont) {
			this.r109_ccf_cont = r109_ccf_cont;
		}
		public BigDecimal getR109_equiv_value() {
			return r109_equiv_value;
		}
		public void setR109_equiv_value(BigDecimal r109_equiv_value) {
			this.r109_equiv_value = r109_equiv_value;
		}
		public BigDecimal getR109_rw_obligant() {
			return r109_rw_obligant;
		}
		public void setR109_rw_obligant(BigDecimal r109_rw_obligant) {
			this.r109_rw_obligant = r109_rw_obligant;
		}
		public BigDecimal getR109_rav() {
			return r109_rav;
		}
		public void setR109_rav(BigDecimal r109_rav) {
			this.r109_rav = r109_rav;
		}
		public String getR110_product() {
			return r110_product;
		}
		public void setR110_product(String r110_product) {
			this.r110_product = r110_product;
		}
		public String getR110_client_grp() {
			return r110_client_grp;
		}
		public void setR110_client_grp(String r110_client_grp) {
			this.r110_client_grp = r110_client_grp;
		}
		public BigDecimal getR110_total_book_expo() {
			return r110_total_book_expo;
		}
		public void setR110_total_book_expo(BigDecimal r110_total_book_expo) {
			this.r110_total_book_expo = r110_total_book_expo;
		}
		public BigDecimal getR110_margin_pro() {
			return r110_margin_pro;
		}
		public void setR110_margin_pro(BigDecimal r110_margin_pro) {
			this.r110_margin_pro = r110_margin_pro;
		}
		public BigDecimal getR110_book_expo() {
			return r110_book_expo;
		}
		public void setR110_book_expo(BigDecimal r110_book_expo) {
			this.r110_book_expo = r110_book_expo;
		}
		public BigDecimal getR110_ccf_cont() {
			return r110_ccf_cont;
		}
		public void setR110_ccf_cont(BigDecimal r110_ccf_cont) {
			this.r110_ccf_cont = r110_ccf_cont;
		}
		public BigDecimal getR110_equiv_value() {
			return r110_equiv_value;
		}
		public void setR110_equiv_value(BigDecimal r110_equiv_value) {
			this.r110_equiv_value = r110_equiv_value;
		}
		public BigDecimal getR110_rw_obligant() {
			return r110_rw_obligant;
		}
		public void setR110_rw_obligant(BigDecimal r110_rw_obligant) {
			this.r110_rw_obligant = r110_rw_obligant;
		}
		public BigDecimal getR110_rav() {
			return r110_rav;
		}
		public void setR110_rav(BigDecimal r110_rav) {
			this.r110_rav = r110_rav;
		}
		public String getR111_product() {
			return r111_product;
		}
		public void setR111_product(String r111_product) {
			this.r111_product = r111_product;
		}
		public String getR111_client_grp() {
			return r111_client_grp;
		}
		public void setR111_client_grp(String r111_client_grp) {
			this.r111_client_grp = r111_client_grp;
		}
		public BigDecimal getR111_total_book_expo() {
			return r111_total_book_expo;
		}
		public void setR111_total_book_expo(BigDecimal r111_total_book_expo) {
			this.r111_total_book_expo = r111_total_book_expo;
		}
		public BigDecimal getR111_margin_pro() {
			return r111_margin_pro;
		}
		public void setR111_margin_pro(BigDecimal r111_margin_pro) {
			this.r111_margin_pro = r111_margin_pro;
		}
		public BigDecimal getR111_book_expo() {
			return r111_book_expo;
		}
		public void setR111_book_expo(BigDecimal r111_book_expo) {
			this.r111_book_expo = r111_book_expo;
		}
		public BigDecimal getR111_ccf_cont() {
			return r111_ccf_cont;
		}
		public void setR111_ccf_cont(BigDecimal r111_ccf_cont) {
			this.r111_ccf_cont = r111_ccf_cont;
		}
		public BigDecimal getR111_equiv_value() {
			return r111_equiv_value;
		}
		public void setR111_equiv_value(BigDecimal r111_equiv_value) {
			this.r111_equiv_value = r111_equiv_value;
		}
		public BigDecimal getR111_rw_obligant() {
			return r111_rw_obligant;
		}
		public void setR111_rw_obligant(BigDecimal r111_rw_obligant) {
			this.r111_rw_obligant = r111_rw_obligant;
		}
		public BigDecimal getR111_rav() {
			return r111_rav;
		}
		public void setR111_rav(BigDecimal r111_rav) {
			this.r111_rav = r111_rav;
		}
		public String getR112_product() {
			return r112_product;
		}
		public void setR112_product(String r112_product) {
			this.r112_product = r112_product;
		}
		public String getR112_client_grp() {
			return r112_client_grp;
		}
		public void setR112_client_grp(String r112_client_grp) {
			this.r112_client_grp = r112_client_grp;
		}
		public BigDecimal getR112_total_book_expo() {
			return r112_total_book_expo;
		}
		public void setR112_total_book_expo(BigDecimal r112_total_book_expo) {
			this.r112_total_book_expo = r112_total_book_expo;
		}
		public BigDecimal getR112_margin_pro() {
			return r112_margin_pro;
		}
		public void setR112_margin_pro(BigDecimal r112_margin_pro) {
			this.r112_margin_pro = r112_margin_pro;
		}
		public BigDecimal getR112_book_expo() {
			return r112_book_expo;
		}
		public void setR112_book_expo(BigDecimal r112_book_expo) {
			this.r112_book_expo = r112_book_expo;
		}
		public BigDecimal getR112_ccf_cont() {
			return r112_ccf_cont;
		}
		public void setR112_ccf_cont(BigDecimal r112_ccf_cont) {
			this.r112_ccf_cont = r112_ccf_cont;
		}
		public BigDecimal getR112_equiv_value() {
			return r112_equiv_value;
		}
		public void setR112_equiv_value(BigDecimal r112_equiv_value) {
			this.r112_equiv_value = r112_equiv_value;
		}
		public BigDecimal getR112_rw_obligant() {
			return r112_rw_obligant;
		}
		public void setR112_rw_obligant(BigDecimal r112_rw_obligant) {
			this.r112_rw_obligant = r112_rw_obligant;
		}
		public BigDecimal getR112_rav() {
			return r112_rav;
		}
		public void setR112_rav(BigDecimal r112_rav) {
			this.r112_rav = r112_rav;
		}
		public String getR113_product() {
			return r113_product;
		}
		public void setR113_product(String r113_product) {
			this.r113_product = r113_product;
		}
		public String getR113_client_grp() {
			return r113_client_grp;
		}
		public void setR113_client_grp(String r113_client_grp) {
			this.r113_client_grp = r113_client_grp;
		}
		public BigDecimal getR113_total_book_expo() {
			return r113_total_book_expo;
		}
		public void setR113_total_book_expo(BigDecimal r113_total_book_expo) {
			this.r113_total_book_expo = r113_total_book_expo;
		}
		public BigDecimal getR113_margin_pro() {
			return r113_margin_pro;
		}
		public void setR113_margin_pro(BigDecimal r113_margin_pro) {
			this.r113_margin_pro = r113_margin_pro;
		}
		public BigDecimal getR113_book_expo() {
			return r113_book_expo;
		}
		public void setR113_book_expo(BigDecimal r113_book_expo) {
			this.r113_book_expo = r113_book_expo;
		}
		public BigDecimal getR113_ccf_cont() {
			return r113_ccf_cont;
		}
		public void setR113_ccf_cont(BigDecimal r113_ccf_cont) {
			this.r113_ccf_cont = r113_ccf_cont;
		}
		public BigDecimal getR113_equiv_value() {
			return r113_equiv_value;
		}
		public void setR113_equiv_value(BigDecimal r113_equiv_value) {
			this.r113_equiv_value = r113_equiv_value;
		}
		public BigDecimal getR113_rw_obligant() {
			return r113_rw_obligant;
		}
		public void setR113_rw_obligant(BigDecimal r113_rw_obligant) {
			this.r113_rw_obligant = r113_rw_obligant;
		}
		public BigDecimal getR113_rav() {
			return r113_rav;
		}
		public void setR113_rav(BigDecimal r113_rav) {
			this.r113_rav = r113_rav;
		}
		public String getR114_product() {
			return r114_product;
		}
		public void setR114_product(String r114_product) {
			this.r114_product = r114_product;
		}
		public String getR114_client_grp() {
			return r114_client_grp;
		}
		public void setR114_client_grp(String r114_client_grp) {
			this.r114_client_grp = r114_client_grp;
		}
		public BigDecimal getR114_total_book_expo() {
			return r114_total_book_expo;
		}
		public void setR114_total_book_expo(BigDecimal r114_total_book_expo) {
			this.r114_total_book_expo = r114_total_book_expo;
		}
		public BigDecimal getR114_margin_pro() {
			return r114_margin_pro;
		}
		public void setR114_margin_pro(BigDecimal r114_margin_pro) {
			this.r114_margin_pro = r114_margin_pro;
		}
		public BigDecimal getR114_book_expo() {
			return r114_book_expo;
		}
		public void setR114_book_expo(BigDecimal r114_book_expo) {
			this.r114_book_expo = r114_book_expo;
		}
		public BigDecimal getR114_ccf_cont() {
			return r114_ccf_cont;
		}
		public void setR114_ccf_cont(BigDecimal r114_ccf_cont) {
			this.r114_ccf_cont = r114_ccf_cont;
		}
		public BigDecimal getR114_equiv_value() {
			return r114_equiv_value;
		}
		public void setR114_equiv_value(BigDecimal r114_equiv_value) {
			this.r114_equiv_value = r114_equiv_value;
		}
		public BigDecimal getR114_rw_obligant() {
			return r114_rw_obligant;
		}
		public void setR114_rw_obligant(BigDecimal r114_rw_obligant) {
			this.r114_rw_obligant = r114_rw_obligant;
		}
		public BigDecimal getR114_rav() {
			return r114_rav;
		}
		public void setR114_rav(BigDecimal r114_rav) {
			this.r114_rav = r114_rav;
		}
		public String getR115_product() {
			return r115_product;
		}
		public void setR115_product(String r115_product) {
			this.r115_product = r115_product;
		}
		public String getR115_client_grp() {
			return r115_client_grp;
		}
		public void setR115_client_grp(String r115_client_grp) {
			this.r115_client_grp = r115_client_grp;
		}
		public BigDecimal getR115_total_book_expo() {
			return r115_total_book_expo;
		}
		public void setR115_total_book_expo(BigDecimal r115_total_book_expo) {
			this.r115_total_book_expo = r115_total_book_expo;
		}
		public BigDecimal getR115_margin_pro() {
			return r115_margin_pro;
		}
		public void setR115_margin_pro(BigDecimal r115_margin_pro) {
			this.r115_margin_pro = r115_margin_pro;
		}
		public BigDecimal getR115_book_expo() {
			return r115_book_expo;
		}
		public void setR115_book_expo(BigDecimal r115_book_expo) {
			this.r115_book_expo = r115_book_expo;
		}
		public BigDecimal getR115_ccf_cont() {
			return r115_ccf_cont;
		}
		public void setR115_ccf_cont(BigDecimal r115_ccf_cont) {
			this.r115_ccf_cont = r115_ccf_cont;
		}
		public BigDecimal getR115_equiv_value() {
			return r115_equiv_value;
		}
		public void setR115_equiv_value(BigDecimal r115_equiv_value) {
			this.r115_equiv_value = r115_equiv_value;
		}
		public BigDecimal getR115_rw_obligant() {
			return r115_rw_obligant;
		}
		public void setR115_rw_obligant(BigDecimal r115_rw_obligant) {
			this.r115_rw_obligant = r115_rw_obligant;
		}
		public BigDecimal getR115_rav() {
			return r115_rav;
		}
		public void setR115_rav(BigDecimal r115_rav) {
			this.r115_rav = r115_rav;
		}
		public String getR116_product() {
			return r116_product;
		}
		public void setR116_product(String r116_product) {
			this.r116_product = r116_product;
		}
		public String getR116_client_grp() {
			return r116_client_grp;
		}
		public void setR116_client_grp(String r116_client_grp) {
			this.r116_client_grp = r116_client_grp;
		}
		public BigDecimal getR116_total_book_expo() {
			return r116_total_book_expo;
		}
		public void setR116_total_book_expo(BigDecimal r116_total_book_expo) {
			this.r116_total_book_expo = r116_total_book_expo;
		}
		public BigDecimal getR116_margin_pro() {
			return r116_margin_pro;
		}
		public void setR116_margin_pro(BigDecimal r116_margin_pro) {
			this.r116_margin_pro = r116_margin_pro;
		}
		public BigDecimal getR116_book_expo() {
			return r116_book_expo;
		}
		public void setR116_book_expo(BigDecimal r116_book_expo) {
			this.r116_book_expo = r116_book_expo;
		}
		public BigDecimal getR116_ccf_cont() {
			return r116_ccf_cont;
		}
		public void setR116_ccf_cont(BigDecimal r116_ccf_cont) {
			this.r116_ccf_cont = r116_ccf_cont;
		}
		public BigDecimal getR116_equiv_value() {
			return r116_equiv_value;
		}
		public void setR116_equiv_value(BigDecimal r116_equiv_value) {
			this.r116_equiv_value = r116_equiv_value;
		}
		public BigDecimal getR116_rw_obligant() {
			return r116_rw_obligant;
		}
		public void setR116_rw_obligant(BigDecimal r116_rw_obligant) {
			this.r116_rw_obligant = r116_rw_obligant;
		}
		public BigDecimal getR116_rav() {
			return r116_rav;
		}
		public void setR116_rav(BigDecimal r116_rav) {
			this.r116_rav = r116_rav;
		}
		public String getR117_product() {
			return r117_product;
		}
		public void setR117_product(String r117_product) {
			this.r117_product = r117_product;
		}
		public String getR117_client_grp() {
			return r117_client_grp;
		}
		public void setR117_client_grp(String r117_client_grp) {
			this.r117_client_grp = r117_client_grp;
		}
		public BigDecimal getR117_total_book_expo() {
			return r117_total_book_expo;
		}
		public void setR117_total_book_expo(BigDecimal r117_total_book_expo) {
			this.r117_total_book_expo = r117_total_book_expo;
		}
		public BigDecimal getR117_margin_pro() {
			return r117_margin_pro;
		}
		public void setR117_margin_pro(BigDecimal r117_margin_pro) {
			this.r117_margin_pro = r117_margin_pro;
		}
		public BigDecimal getR117_book_expo() {
			return r117_book_expo;
		}
		public void setR117_book_expo(BigDecimal r117_book_expo) {
			this.r117_book_expo = r117_book_expo;
		}
		public BigDecimal getR117_ccf_cont() {
			return r117_ccf_cont;
		}
		public void setR117_ccf_cont(BigDecimal r117_ccf_cont) {
			this.r117_ccf_cont = r117_ccf_cont;
		}
		public BigDecimal getR117_equiv_value() {
			return r117_equiv_value;
		}
		public void setR117_equiv_value(BigDecimal r117_equiv_value) {
			this.r117_equiv_value = r117_equiv_value;
		}
		public BigDecimal getR117_rw_obligant() {
			return r117_rw_obligant;
		}
		public void setR117_rw_obligant(BigDecimal r117_rw_obligant) {
			this.r117_rw_obligant = r117_rw_obligant;
		}
		public BigDecimal getR117_rav() {
			return r117_rav;
		}
		public void setR117_rav(BigDecimal r117_rav) {
			this.r117_rav = r117_rav;
		}
		public String getR118_product() {
			return r118_product;
		}
		public void setR118_product(String r118_product) {
			this.r118_product = r118_product;
		}
		public String getR118_client_grp() {
			return r118_client_grp;
		}
		public void setR118_client_grp(String r118_client_grp) {
			this.r118_client_grp = r118_client_grp;
		}
		public BigDecimal getR118_total_book_expo() {
			return r118_total_book_expo;
		}
		public void setR118_total_book_expo(BigDecimal r118_total_book_expo) {
			this.r118_total_book_expo = r118_total_book_expo;
		}
		public BigDecimal getR118_margin_pro() {
			return r118_margin_pro;
		}
		public void setR118_margin_pro(BigDecimal r118_margin_pro) {
			this.r118_margin_pro = r118_margin_pro;
		}
		public BigDecimal getR118_book_expo() {
			return r118_book_expo;
		}
		public void setR118_book_expo(BigDecimal r118_book_expo) {
			this.r118_book_expo = r118_book_expo;
		}
		public BigDecimal getR118_ccf_cont() {
			return r118_ccf_cont;
		}
		public void setR118_ccf_cont(BigDecimal r118_ccf_cont) {
			this.r118_ccf_cont = r118_ccf_cont;
		}
		public BigDecimal getR118_equiv_value() {
			return r118_equiv_value;
		}
		public void setR118_equiv_value(BigDecimal r118_equiv_value) {
			this.r118_equiv_value = r118_equiv_value;
		}
		public BigDecimal getR118_rw_obligant() {
			return r118_rw_obligant;
		}
		public void setR118_rw_obligant(BigDecimal r118_rw_obligant) {
			this.r118_rw_obligant = r118_rw_obligant;
		}
		public BigDecimal getR118_rav() {
			return r118_rav;
		}
		public void setR118_rav(BigDecimal r118_rav) {
			this.r118_rav = r118_rav;
		}
		public String getR119_product() {
			return r119_product;
		}
		public void setR119_product(String r119_product) {
			this.r119_product = r119_product;
		}
		public String getR119_client_grp() {
			return r119_client_grp;
		}
		public void setR119_client_grp(String r119_client_grp) {
			this.r119_client_grp = r119_client_grp;
		}
		public BigDecimal getR119_total_book_expo() {
			return r119_total_book_expo;
		}
		public void setR119_total_book_expo(BigDecimal r119_total_book_expo) {
			this.r119_total_book_expo = r119_total_book_expo;
		}
		public BigDecimal getR119_margin_pro() {
			return r119_margin_pro;
		}
		public void setR119_margin_pro(BigDecimal r119_margin_pro) {
			this.r119_margin_pro = r119_margin_pro;
		}
		public BigDecimal getR119_book_expo() {
			return r119_book_expo;
		}
		public void setR119_book_expo(BigDecimal r119_book_expo) {
			this.r119_book_expo = r119_book_expo;
		}
		public BigDecimal getR119_ccf_cont() {
			return r119_ccf_cont;
		}
		public void setR119_ccf_cont(BigDecimal r119_ccf_cont) {
			this.r119_ccf_cont = r119_ccf_cont;
		}
		public BigDecimal getR119_equiv_value() {
			return r119_equiv_value;
		}
		public void setR119_equiv_value(BigDecimal r119_equiv_value) {
			this.r119_equiv_value = r119_equiv_value;
		}
		public BigDecimal getR119_rw_obligant() {
			return r119_rw_obligant;
		}
		public void setR119_rw_obligant(BigDecimal r119_rw_obligant) {
			this.r119_rw_obligant = r119_rw_obligant;
		}
		public BigDecimal getR119_rav() {
			return r119_rav;
		}
		public void setR119_rav(BigDecimal r119_rav) {
			this.r119_rav = r119_rav;
		}
		public String getR120_product() {
			return r120_product;
		}
		public void setR120_product(String r120_product) {
			this.r120_product = r120_product;
		}
		public String getR120_client_grp() {
			return r120_client_grp;
		}
		public void setR120_client_grp(String r120_client_grp) {
			this.r120_client_grp = r120_client_grp;
		}
		public BigDecimal getR120_total_book_expo() {
			return r120_total_book_expo;
		}
		public void setR120_total_book_expo(BigDecimal r120_total_book_expo) {
			this.r120_total_book_expo = r120_total_book_expo;
		}
		public BigDecimal getR120_margin_pro() {
			return r120_margin_pro;
		}
		public void setR120_margin_pro(BigDecimal r120_margin_pro) {
			this.r120_margin_pro = r120_margin_pro;
		}
		public BigDecimal getR120_book_expo() {
			return r120_book_expo;
		}
		public void setR120_book_expo(BigDecimal r120_book_expo) {
			this.r120_book_expo = r120_book_expo;
		}
		public BigDecimal getR120_ccf_cont() {
			return r120_ccf_cont;
		}
		public void setR120_ccf_cont(BigDecimal r120_ccf_cont) {
			this.r120_ccf_cont = r120_ccf_cont;
		}
		public BigDecimal getR120_equiv_value() {
			return r120_equiv_value;
		}
		public void setR120_equiv_value(BigDecimal r120_equiv_value) {
			this.r120_equiv_value = r120_equiv_value;
		}
		public BigDecimal getR120_rw_obligant() {
			return r120_rw_obligant;
		}
		public void setR120_rw_obligant(BigDecimal r120_rw_obligant) {
			this.r120_rw_obligant = r120_rw_obligant;
		}
		public BigDecimal getR120_rav() {
			return r120_rav;
		}
		public void setR120_rav(BigDecimal r120_rav) {
			this.r120_rav = r120_rav;
		}
		public String getR121_product() {
			return r121_product;
		}
		public void setR121_product(String r121_product) {
			this.r121_product = r121_product;
		}
		public String getR121_client_grp() {
			return r121_client_grp;
		}
		public void setR121_client_grp(String r121_client_grp) {
			this.r121_client_grp = r121_client_grp;
		}
		public BigDecimal getR121_total_book_expo() {
			return r121_total_book_expo;
		}
		public void setR121_total_book_expo(BigDecimal r121_total_book_expo) {
			this.r121_total_book_expo = r121_total_book_expo;
		}
		public BigDecimal getR121_margin_pro() {
			return r121_margin_pro;
		}
		public void setR121_margin_pro(BigDecimal r121_margin_pro) {
			this.r121_margin_pro = r121_margin_pro;
		}
		public BigDecimal getR121_book_expo() {
			return r121_book_expo;
		}
		public void setR121_book_expo(BigDecimal r121_book_expo) {
			this.r121_book_expo = r121_book_expo;
		}
		public BigDecimal getR121_ccf_cont() {
			return r121_ccf_cont;
		}
		public void setR121_ccf_cont(BigDecimal r121_ccf_cont) {
			this.r121_ccf_cont = r121_ccf_cont;
		}
		public BigDecimal getR121_equiv_value() {
			return r121_equiv_value;
		}
		public void setR121_equiv_value(BigDecimal r121_equiv_value) {
			this.r121_equiv_value = r121_equiv_value;
		}
		public BigDecimal getR121_rw_obligant() {
			return r121_rw_obligant;
		}
		public void setR121_rw_obligant(BigDecimal r121_rw_obligant) {
			this.r121_rw_obligant = r121_rw_obligant;
		}
		public BigDecimal getR121_rav() {
			return r121_rav;
		}
		public void setR121_rav(BigDecimal r121_rav) {
			this.r121_rav = r121_rav;
		}
		public String getR122_product() {
			return r122_product;
		}
		public void setR122_product(String r122_product) {
			this.r122_product = r122_product;
		}
		public String getR122_client_grp() {
			return r122_client_grp;
		}
		public void setR122_client_grp(String r122_client_grp) {
			this.r122_client_grp = r122_client_grp;
		}
		public BigDecimal getR122_total_book_expo() {
			return r122_total_book_expo;
		}
		public void setR122_total_book_expo(BigDecimal r122_total_book_expo) {
			this.r122_total_book_expo = r122_total_book_expo;
		}
		public BigDecimal getR122_margin_pro() {
			return r122_margin_pro;
		}
		public void setR122_margin_pro(BigDecimal r122_margin_pro) {
			this.r122_margin_pro = r122_margin_pro;
		}
		public BigDecimal getR122_book_expo() {
			return r122_book_expo;
		}
		public void setR122_book_expo(BigDecimal r122_book_expo) {
			this.r122_book_expo = r122_book_expo;
		}
		public BigDecimal getR122_ccf_cont() {
			return r122_ccf_cont;
		}
		public void setR122_ccf_cont(BigDecimal r122_ccf_cont) {
			this.r122_ccf_cont = r122_ccf_cont;
		}
		public BigDecimal getR122_equiv_value() {
			return r122_equiv_value;
		}
		public void setR122_equiv_value(BigDecimal r122_equiv_value) {
			this.r122_equiv_value = r122_equiv_value;
		}
		public BigDecimal getR122_rw_obligant() {
			return r122_rw_obligant;
		}
		public void setR122_rw_obligant(BigDecimal r122_rw_obligant) {
			this.r122_rw_obligant = r122_rw_obligant;
		}
		public BigDecimal getR122_rav() {
			return r122_rav;
		}
		public void setR122_rav(BigDecimal r122_rav) {
			this.r122_rav = r122_rav;
		}
		public String getR123_product() {
			return r123_product;
		}
		public void setR123_product(String r123_product) {
			this.r123_product = r123_product;
		}
		public String getR123_client_grp() {
			return r123_client_grp;
		}
		public void setR123_client_grp(String r123_client_grp) {
			this.r123_client_grp = r123_client_grp;
		}
		public BigDecimal getR123_total_book_expo() {
			return r123_total_book_expo;
		}
		public void setR123_total_book_expo(BigDecimal r123_total_book_expo) {
			this.r123_total_book_expo = r123_total_book_expo;
		}
		public BigDecimal getR123_margin_pro() {
			return r123_margin_pro;
		}
		public void setR123_margin_pro(BigDecimal r123_margin_pro) {
			this.r123_margin_pro = r123_margin_pro;
		}
		public BigDecimal getR123_book_expo() {
			return r123_book_expo;
		}
		public void setR123_book_expo(BigDecimal r123_book_expo) {
			this.r123_book_expo = r123_book_expo;
		}
		public BigDecimal getR123_ccf_cont() {
			return r123_ccf_cont;
		}
		public void setR123_ccf_cont(BigDecimal r123_ccf_cont) {
			this.r123_ccf_cont = r123_ccf_cont;
		}
		public BigDecimal getR123_equiv_value() {
			return r123_equiv_value;
		}
		public void setR123_equiv_value(BigDecimal r123_equiv_value) {
			this.r123_equiv_value = r123_equiv_value;
		}
		public BigDecimal getR123_rw_obligant() {
			return r123_rw_obligant;
		}
		public void setR123_rw_obligant(BigDecimal r123_rw_obligant) {
			this.r123_rw_obligant = r123_rw_obligant;
		}
		public BigDecimal getR123_rav() {
			return r123_rav;
		}
		public void setR123_rav(BigDecimal r123_rav) {
			this.r123_rav = r123_rav;
		}
		public String getR124_product() {
			return r124_product;
		}
		public void setR124_product(String r124_product) {
			this.r124_product = r124_product;
		}
		public String getR124_client_grp() {
			return r124_client_grp;
		}
		public void setR124_client_grp(String r124_client_grp) {
			this.r124_client_grp = r124_client_grp;
		}
		public BigDecimal getR124_total_book_expo() {
			return r124_total_book_expo;
		}
		public void setR124_total_book_expo(BigDecimal r124_total_book_expo) {
			this.r124_total_book_expo = r124_total_book_expo;
		}
		public BigDecimal getR124_margin_pro() {
			return r124_margin_pro;
		}
		public void setR124_margin_pro(BigDecimal r124_margin_pro) {
			this.r124_margin_pro = r124_margin_pro;
		}
		public BigDecimal getR124_book_expo() {
			return r124_book_expo;
		}
		public void setR124_book_expo(BigDecimal r124_book_expo) {
			this.r124_book_expo = r124_book_expo;
		}
		public BigDecimal getR124_ccf_cont() {
			return r124_ccf_cont;
		}
		public void setR124_ccf_cont(BigDecimal r124_ccf_cont) {
			this.r124_ccf_cont = r124_ccf_cont;
		}
		public BigDecimal getR124_equiv_value() {
			return r124_equiv_value;
		}
		public void setR124_equiv_value(BigDecimal r124_equiv_value) {
			this.r124_equiv_value = r124_equiv_value;
		}
		public BigDecimal getR124_rw_obligant() {
			return r124_rw_obligant;
		}
		public void setR124_rw_obligant(BigDecimal r124_rw_obligant) {
			this.r124_rw_obligant = r124_rw_obligant;
		}
		public BigDecimal getR124_rav() {
			return r124_rav;
		}
		public void setR124_rav(BigDecimal r124_rav) {
			this.r124_rav = r124_rav;
		}
		public String getR125_product() {
			return r125_product;
		}
		public void setR125_product(String r125_product) {
			this.r125_product = r125_product;
		}
		public String getR125_client_grp() {
			return r125_client_grp;
		}
		public void setR125_client_grp(String r125_client_grp) {
			this.r125_client_grp = r125_client_grp;
		}
		public BigDecimal getR125_total_book_expo() {
			return r125_total_book_expo;
		}
		public void setR125_total_book_expo(BigDecimal r125_total_book_expo) {
			this.r125_total_book_expo = r125_total_book_expo;
		}
		public BigDecimal getR125_margin_pro() {
			return r125_margin_pro;
		}
		public void setR125_margin_pro(BigDecimal r125_margin_pro) {
			this.r125_margin_pro = r125_margin_pro;
		}
		public BigDecimal getR125_book_expo() {
			return r125_book_expo;
		}
		public void setR125_book_expo(BigDecimal r125_book_expo) {
			this.r125_book_expo = r125_book_expo;
		}
		public BigDecimal getR125_ccf_cont() {
			return r125_ccf_cont;
		}
		public void setR125_ccf_cont(BigDecimal r125_ccf_cont) {
			this.r125_ccf_cont = r125_ccf_cont;
		}
		public BigDecimal getR125_equiv_value() {
			return r125_equiv_value;
		}
		public void setR125_equiv_value(BigDecimal r125_equiv_value) {
			this.r125_equiv_value = r125_equiv_value;
		}
		public BigDecimal getR125_rw_obligant() {
			return r125_rw_obligant;
		}
		public void setR125_rw_obligant(BigDecimal r125_rw_obligant) {
			this.r125_rw_obligant = r125_rw_obligant;
		}
		public BigDecimal getR125_rav() {
			return r125_rav;
		}
		public void setR125_rav(BigDecimal r125_rav) {
			this.r125_rav = r125_rav;
		}
		public String getR126_product() {
			return r126_product;
		}
		public void setR126_product(String r126_product) {
			this.r126_product = r126_product;
		}
		public String getR126_client_grp() {
			return r126_client_grp;
		}
		public void setR126_client_grp(String r126_client_grp) {
			this.r126_client_grp = r126_client_grp;
		}
		public BigDecimal getR126_total_book_expo() {
			return r126_total_book_expo;
		}
		public void setR126_total_book_expo(BigDecimal r126_total_book_expo) {
			this.r126_total_book_expo = r126_total_book_expo;
		}
		public BigDecimal getR126_margin_pro() {
			return r126_margin_pro;
		}
		public void setR126_margin_pro(BigDecimal r126_margin_pro) {
			this.r126_margin_pro = r126_margin_pro;
		}
		public BigDecimal getR126_book_expo() {
			return r126_book_expo;
		}
		public void setR126_book_expo(BigDecimal r126_book_expo) {
			this.r126_book_expo = r126_book_expo;
		}
		public BigDecimal getR126_ccf_cont() {
			return r126_ccf_cont;
		}
		public void setR126_ccf_cont(BigDecimal r126_ccf_cont) {
			this.r126_ccf_cont = r126_ccf_cont;
		}
		public BigDecimal getR126_equiv_value() {
			return r126_equiv_value;
		}
		public void setR126_equiv_value(BigDecimal r126_equiv_value) {
			this.r126_equiv_value = r126_equiv_value;
		}
		public BigDecimal getR126_rw_obligant() {
			return r126_rw_obligant;
		}
		public void setR126_rw_obligant(BigDecimal r126_rw_obligant) {
			this.r126_rw_obligant = r126_rw_obligant;
		}
		public BigDecimal getR126_rav() {
			return r126_rav;
		}
		public void setR126_rav(BigDecimal r126_rav) {
			this.r126_rav = r126_rav;
		}
		public String getR127_product() {
			return r127_product;
		}
		public void setR127_product(String r127_product) {
			this.r127_product = r127_product;
		}
		public String getR127_client_grp() {
			return r127_client_grp;
		}
		public void setR127_client_grp(String r127_client_grp) {
			this.r127_client_grp = r127_client_grp;
		}
		public BigDecimal getR127_total_book_expo() {
			return r127_total_book_expo;
		}
		public void setR127_total_book_expo(BigDecimal r127_total_book_expo) {
			this.r127_total_book_expo = r127_total_book_expo;
		}
		public BigDecimal getR127_margin_pro() {
			return r127_margin_pro;
		}
		public void setR127_margin_pro(BigDecimal r127_margin_pro) {
			this.r127_margin_pro = r127_margin_pro;
		}
		public BigDecimal getR127_book_expo() {
			return r127_book_expo;
		}
		public void setR127_book_expo(BigDecimal r127_book_expo) {
			this.r127_book_expo = r127_book_expo;
		}
		public BigDecimal getR127_ccf_cont() {
			return r127_ccf_cont;
		}
		public void setR127_ccf_cont(BigDecimal r127_ccf_cont) {
			this.r127_ccf_cont = r127_ccf_cont;
		}
		public BigDecimal getR127_equiv_value() {
			return r127_equiv_value;
		}
		public void setR127_equiv_value(BigDecimal r127_equiv_value) {
			this.r127_equiv_value = r127_equiv_value;
		}
		public BigDecimal getR127_rw_obligant() {
			return r127_rw_obligant;
		}
		public void setR127_rw_obligant(BigDecimal r127_rw_obligant) {
			this.r127_rw_obligant = r127_rw_obligant;
		}
		public BigDecimal getR127_rav() {
			return r127_rav;
		}
		public void setR127_rav(BigDecimal r127_rav) {
			this.r127_rav = r127_rav;
		}
		public String getR128_product() {
			return r128_product;
		}
		public void setR128_product(String r128_product) {
			this.r128_product = r128_product;
		}
		public String getR128_client_grp() {
			return r128_client_grp;
		}
		public void setR128_client_grp(String r128_client_grp) {
			this.r128_client_grp = r128_client_grp;
		}
		public BigDecimal getR128_total_book_expo() {
			return r128_total_book_expo;
		}
		public void setR128_total_book_expo(BigDecimal r128_total_book_expo) {
			this.r128_total_book_expo = r128_total_book_expo;
		}
		public BigDecimal getR128_margin_pro() {
			return r128_margin_pro;
		}
		public void setR128_margin_pro(BigDecimal r128_margin_pro) {
			this.r128_margin_pro = r128_margin_pro;
		}
		public BigDecimal getR128_book_expo() {
			return r128_book_expo;
		}
		public void setR128_book_expo(BigDecimal r128_book_expo) {
			this.r128_book_expo = r128_book_expo;
		}
		public BigDecimal getR128_ccf_cont() {
			return r128_ccf_cont;
		}
		public void setR128_ccf_cont(BigDecimal r128_ccf_cont) {
			this.r128_ccf_cont = r128_ccf_cont;
		}
		public BigDecimal getR128_equiv_value() {
			return r128_equiv_value;
		}
		public void setR128_equiv_value(BigDecimal r128_equiv_value) {
			this.r128_equiv_value = r128_equiv_value;
		}
		public BigDecimal getR128_rw_obligant() {
			return r128_rw_obligant;
		}
		public void setR128_rw_obligant(BigDecimal r128_rw_obligant) {
			this.r128_rw_obligant = r128_rw_obligant;
		}
		public BigDecimal getR128_rav() {
			return r128_rav;
		}
		public void setR128_rav(BigDecimal r128_rav) {
			this.r128_rav = r128_rav;
		}
		public String getR129_product() {
			return r129_product;
		}
		public void setR129_product(String r129_product) {
			this.r129_product = r129_product;
		}
		public String getR129_client_grp() {
			return r129_client_grp;
		}
		public void setR129_client_grp(String r129_client_grp) {
			this.r129_client_grp = r129_client_grp;
		}
		public BigDecimal getR129_total_book_expo() {
			return r129_total_book_expo;
		}
		public void setR129_total_book_expo(BigDecimal r129_total_book_expo) {
			this.r129_total_book_expo = r129_total_book_expo;
		}
		public BigDecimal getR129_margin_pro() {
			return r129_margin_pro;
		}
		public void setR129_margin_pro(BigDecimal r129_margin_pro) {
			this.r129_margin_pro = r129_margin_pro;
		}
		public BigDecimal getR129_book_expo() {
			return r129_book_expo;
		}
		public void setR129_book_expo(BigDecimal r129_book_expo) {
			this.r129_book_expo = r129_book_expo;
		}
		public BigDecimal getR129_ccf_cont() {
			return r129_ccf_cont;
		}
		public void setR129_ccf_cont(BigDecimal r129_ccf_cont) {
			this.r129_ccf_cont = r129_ccf_cont;
		}
		public BigDecimal getR129_equiv_value() {
			return r129_equiv_value;
		}
		public void setR129_equiv_value(BigDecimal r129_equiv_value) {
			this.r129_equiv_value = r129_equiv_value;
		}
		public BigDecimal getR129_rw_obligant() {
			return r129_rw_obligant;
		}
		public void setR129_rw_obligant(BigDecimal r129_rw_obligant) {
			this.r129_rw_obligant = r129_rw_obligant;
		}
		public BigDecimal getR129_rav() {
			return r129_rav;
		}
		public void setR129_rav(BigDecimal r129_rav) {
			this.r129_rav = r129_rav;
		}
		public String getR130_product() {
			return r130_product;
		}
		public void setR130_product(String r130_product) {
			this.r130_product = r130_product;
		}
		public String getR130_client_grp() {
			return r130_client_grp;
		}
		public void setR130_client_grp(String r130_client_grp) {
			this.r130_client_grp = r130_client_grp;
		}
		public BigDecimal getR130_total_book_expo() {
			return r130_total_book_expo;
		}
		public void setR130_total_book_expo(BigDecimal r130_total_book_expo) {
			this.r130_total_book_expo = r130_total_book_expo;
		}
		public BigDecimal getR130_margin_pro() {
			return r130_margin_pro;
		}
		public void setR130_margin_pro(BigDecimal r130_margin_pro) {
			this.r130_margin_pro = r130_margin_pro;
		}
		public BigDecimal getR130_book_expo() {
			return r130_book_expo;
		}
		public void setR130_book_expo(BigDecimal r130_book_expo) {
			this.r130_book_expo = r130_book_expo;
		}
		public BigDecimal getR130_ccf_cont() {
			return r130_ccf_cont;
		}
		public void setR130_ccf_cont(BigDecimal r130_ccf_cont) {
			this.r130_ccf_cont = r130_ccf_cont;
		}
		public BigDecimal getR130_equiv_value() {
			return r130_equiv_value;
		}
		public void setR130_equiv_value(BigDecimal r130_equiv_value) {
			this.r130_equiv_value = r130_equiv_value;
		}
		public BigDecimal getR130_rw_obligant() {
			return r130_rw_obligant;
		}
		public void setR130_rw_obligant(BigDecimal r130_rw_obligant) {
			this.r130_rw_obligant = r130_rw_obligant;
		}
		public BigDecimal getR130_rav() {
			return r130_rav;
		}
		public void setR130_rav(BigDecimal r130_rav) {
			this.r130_rav = r130_rav;
		}
		public String getR131_product() {
			return r131_product;
		}
		public void setR131_product(String r131_product) {
			this.r131_product = r131_product;
		}
		public String getR131_client_grp() {
			return r131_client_grp;
		}
		public void setR131_client_grp(String r131_client_grp) {
			this.r131_client_grp = r131_client_grp;
		}
		public BigDecimal getR131_total_book_expo() {
			return r131_total_book_expo;
		}
		public void setR131_total_book_expo(BigDecimal r131_total_book_expo) {
			this.r131_total_book_expo = r131_total_book_expo;
		}
		public BigDecimal getR131_margin_pro() {
			return r131_margin_pro;
		}
		public void setR131_margin_pro(BigDecimal r131_margin_pro) {
			this.r131_margin_pro = r131_margin_pro;
		}
		public BigDecimal getR131_book_expo() {
			return r131_book_expo;
		}
		public void setR131_book_expo(BigDecimal r131_book_expo) {
			this.r131_book_expo = r131_book_expo;
		}
		public BigDecimal getR131_ccf_cont() {
			return r131_ccf_cont;
		}
		public void setR131_ccf_cont(BigDecimal r131_ccf_cont) {
			this.r131_ccf_cont = r131_ccf_cont;
		}
		public BigDecimal getR131_equiv_value() {
			return r131_equiv_value;
		}
		public void setR131_equiv_value(BigDecimal r131_equiv_value) {
			this.r131_equiv_value = r131_equiv_value;
		}
		public BigDecimal getR131_rw_obligant() {
			return r131_rw_obligant;
		}
		public void setR131_rw_obligant(BigDecimal r131_rw_obligant) {
			this.r131_rw_obligant = r131_rw_obligant;
		}
		public BigDecimal getR131_rav() {
			return r131_rav;
		}
		public void setR131_rav(BigDecimal r131_rav) {
			this.r131_rav = r131_rav;
		}
		public String getR132_product() {
			return r132_product;
		}
		public void setR132_product(String r132_product) {
			this.r132_product = r132_product;
		}
		public String getR132_client_grp() {
			return r132_client_grp;
		}
		public void setR132_client_grp(String r132_client_grp) {
			this.r132_client_grp = r132_client_grp;
		}
		public BigDecimal getR132_total_book_expo() {
			return r132_total_book_expo;
		}
		public void setR132_total_book_expo(BigDecimal r132_total_book_expo) {
			this.r132_total_book_expo = r132_total_book_expo;
		}
		public BigDecimal getR132_margin_pro() {
			return r132_margin_pro;
		}
		public void setR132_margin_pro(BigDecimal r132_margin_pro) {
			this.r132_margin_pro = r132_margin_pro;
		}
		public BigDecimal getR132_book_expo() {
			return r132_book_expo;
		}
		public void setR132_book_expo(BigDecimal r132_book_expo) {
			this.r132_book_expo = r132_book_expo;
		}
		public BigDecimal getR132_ccf_cont() {
			return r132_ccf_cont;
		}
		public void setR132_ccf_cont(BigDecimal r132_ccf_cont) {
			this.r132_ccf_cont = r132_ccf_cont;
		}
		public BigDecimal getR132_equiv_value() {
			return r132_equiv_value;
		}
		public void setR132_equiv_value(BigDecimal r132_equiv_value) {
			this.r132_equiv_value = r132_equiv_value;
		}
		public BigDecimal getR132_rw_obligant() {
			return r132_rw_obligant;
		}
		public void setR132_rw_obligant(BigDecimal r132_rw_obligant) {
			this.r132_rw_obligant = r132_rw_obligant;
		}
		public BigDecimal getR132_rav() {
			return r132_rav;
		}
		public void setR132_rav(BigDecimal r132_rav) {
			this.r132_rav = r132_rav;
		}
		public String getR133_product() {
			return r133_product;
		}
		public void setR133_product(String r133_product) {
			this.r133_product = r133_product;
		}
		public String getR133_client_grp() {
			return r133_client_grp;
		}
		public void setR133_client_grp(String r133_client_grp) {
			this.r133_client_grp = r133_client_grp;
		}
		public BigDecimal getR133_total_book_expo() {
			return r133_total_book_expo;
		}
		public void setR133_total_book_expo(BigDecimal r133_total_book_expo) {
			this.r133_total_book_expo = r133_total_book_expo;
		}
		public BigDecimal getR133_margin_pro() {
			return r133_margin_pro;
		}
		public void setR133_margin_pro(BigDecimal r133_margin_pro) {
			this.r133_margin_pro = r133_margin_pro;
		}
		public BigDecimal getR133_book_expo() {
			return r133_book_expo;
		}
		public void setR133_book_expo(BigDecimal r133_book_expo) {
			this.r133_book_expo = r133_book_expo;
		}
		public BigDecimal getR133_ccf_cont() {
			return r133_ccf_cont;
		}
		public void setR133_ccf_cont(BigDecimal r133_ccf_cont) {
			this.r133_ccf_cont = r133_ccf_cont;
		}
		public BigDecimal getR133_equiv_value() {
			return r133_equiv_value;
		}
		public void setR133_equiv_value(BigDecimal r133_equiv_value) {
			this.r133_equiv_value = r133_equiv_value;
		}
		public BigDecimal getR133_rw_obligant() {
			return r133_rw_obligant;
		}
		public void setR133_rw_obligant(BigDecimal r133_rw_obligant) {
			this.r133_rw_obligant = r133_rw_obligant;
		}
		public BigDecimal getR133_rav() {
			return r133_rav;
		}
		public void setR133_rav(BigDecimal r133_rav) {
			this.r133_rav = r133_rav;
		}
		public String getR134_product() {
			return r134_product;
		}
		public void setR134_product(String r134_product) {
			this.r134_product = r134_product;
		}
		public String getR134_client_grp() {
			return r134_client_grp;
		}
		public void setR134_client_grp(String r134_client_grp) {
			this.r134_client_grp = r134_client_grp;
		}
		public BigDecimal getR134_total_book_expo() {
			return r134_total_book_expo;
		}
		public void setR134_total_book_expo(BigDecimal r134_total_book_expo) {
			this.r134_total_book_expo = r134_total_book_expo;
		}
		public BigDecimal getR134_margin_pro() {
			return r134_margin_pro;
		}
		public void setR134_margin_pro(BigDecimal r134_margin_pro) {
			this.r134_margin_pro = r134_margin_pro;
		}
		public BigDecimal getR134_book_expo() {
			return r134_book_expo;
		}
		public void setR134_book_expo(BigDecimal r134_book_expo) {
			this.r134_book_expo = r134_book_expo;
		}
		public BigDecimal getR134_ccf_cont() {
			return r134_ccf_cont;
		}
		public void setR134_ccf_cont(BigDecimal r134_ccf_cont) {
			this.r134_ccf_cont = r134_ccf_cont;
		}
		public BigDecimal getR134_equiv_value() {
			return r134_equiv_value;
		}
		public void setR134_equiv_value(BigDecimal r134_equiv_value) {
			this.r134_equiv_value = r134_equiv_value;
		}
		public BigDecimal getR134_rw_obligant() {
			return r134_rw_obligant;
		}
		public void setR134_rw_obligant(BigDecimal r134_rw_obligant) {
			this.r134_rw_obligant = r134_rw_obligant;
		}
		public BigDecimal getR134_rav() {
			return r134_rav;
		}
		public void setR134_rav(BigDecimal r134_rav) {
			this.r134_rav = r134_rav;
		}
		public String getR148_product() {
			return r148_product;
		}
		public void setR148_product(String r148_product) {
			this.r148_product = r148_product;
		}
		public String getR148_client_grp() {
			return r148_client_grp;
		}
		public void setR148_client_grp(String r148_client_grp) {
			this.r148_client_grp = r148_client_grp;
		}
		public BigDecimal getR148_total_book_expo() {
			return r148_total_book_expo;
		}
		public void setR148_total_book_expo(BigDecimal r148_total_book_expo) {
			this.r148_total_book_expo = r148_total_book_expo;
		}
		public BigDecimal getR148_margin_pro() {
			return r148_margin_pro;
		}
		public void setR148_margin_pro(BigDecimal r148_margin_pro) {
			this.r148_margin_pro = r148_margin_pro;
		}
		public BigDecimal getR148_book_expo() {
			return r148_book_expo;
		}
		public void setR148_book_expo(BigDecimal r148_book_expo) {
			this.r148_book_expo = r148_book_expo;
		}
		public BigDecimal getR148_ccf_cont() {
			return r148_ccf_cont;
		}
		public void setR148_ccf_cont(BigDecimal r148_ccf_cont) {
			this.r148_ccf_cont = r148_ccf_cont;
		}
		public BigDecimal getR148_equiv_value() {
			return r148_equiv_value;
		}
		public void setR148_equiv_value(BigDecimal r148_equiv_value) {
			this.r148_equiv_value = r148_equiv_value;
		}
		public BigDecimal getR148_rw_obligant() {
			return r148_rw_obligant;
		}
		public void setR148_rw_obligant(BigDecimal r148_rw_obligant) {
			this.r148_rw_obligant = r148_rw_obligant;
		}
		public BigDecimal getR148_rav() {
			return r148_rav;
		}
		public void setR148_rav(BigDecimal r148_rav) {
			this.r148_rav = r148_rav;
		}
		public String getR149_product() {
			return r149_product;
		}
		public void setR149_product(String r149_product) {
			this.r149_product = r149_product;
		}
		public String getR149_client_grp() {
			return r149_client_grp;
		}
		public void setR149_client_grp(String r149_client_grp) {
			this.r149_client_grp = r149_client_grp;
		}
		public BigDecimal getR149_total_book_expo() {
			return r149_total_book_expo;
		}
		public void setR149_total_book_expo(BigDecimal r149_total_book_expo) {
			this.r149_total_book_expo = r149_total_book_expo;
		}
		public BigDecimal getR149_margin_pro() {
			return r149_margin_pro;
		}
		public void setR149_margin_pro(BigDecimal r149_margin_pro) {
			this.r149_margin_pro = r149_margin_pro;
		}
		public BigDecimal getR149_book_expo() {
			return r149_book_expo;
		}
		public void setR149_book_expo(BigDecimal r149_book_expo) {
			this.r149_book_expo = r149_book_expo;
		}
		public BigDecimal getR149_ccf_cont() {
			return r149_ccf_cont;
		}
		public void setR149_ccf_cont(BigDecimal r149_ccf_cont) {
			this.r149_ccf_cont = r149_ccf_cont;
		}
		public BigDecimal getR149_equiv_value() {
			return r149_equiv_value;
		}
		public void setR149_equiv_value(BigDecimal r149_equiv_value) {
			this.r149_equiv_value = r149_equiv_value;
		}
		public BigDecimal getR149_rw_obligant() {
			return r149_rw_obligant;
		}
		public void setR149_rw_obligant(BigDecimal r149_rw_obligant) {
			this.r149_rw_obligant = r149_rw_obligant;
		}
		public BigDecimal getR149_rav() {
			return r149_rav;
		}
		public void setR149_rav(BigDecimal r149_rav) {
			this.r149_rav = r149_rav;
		}
		public String getR150_product() {
			return r150_product;
		}
		public void setR150_product(String r150_product) {
			this.r150_product = r150_product;
		}
		public String getR150_client_grp() {
			return r150_client_grp;
		}
		public void setR150_client_grp(String r150_client_grp) {
			this.r150_client_grp = r150_client_grp;
		}
		public BigDecimal getR150_total_book_expo() {
			return r150_total_book_expo;
		}
		public void setR150_total_book_expo(BigDecimal r150_total_book_expo) {
			this.r150_total_book_expo = r150_total_book_expo;
		}
		public BigDecimal getR150_margin_pro() {
			return r150_margin_pro;
		}
		public void setR150_margin_pro(BigDecimal r150_margin_pro) {
			this.r150_margin_pro = r150_margin_pro;
		}
		public BigDecimal getR150_book_expo() {
			return r150_book_expo;
		}
		public void setR150_book_expo(BigDecimal r150_book_expo) {
			this.r150_book_expo = r150_book_expo;
		}
		public BigDecimal getR150_ccf_cont() {
			return r150_ccf_cont;
		}
		public void setR150_ccf_cont(BigDecimal r150_ccf_cont) {
			this.r150_ccf_cont = r150_ccf_cont;
		}
		public BigDecimal getR150_equiv_value() {
			return r150_equiv_value;
		}
		public void setR150_equiv_value(BigDecimal r150_equiv_value) {
			this.r150_equiv_value = r150_equiv_value;
		}
		public BigDecimal getR150_rw_obligant() {
			return r150_rw_obligant;
		}
		public void setR150_rw_obligant(BigDecimal r150_rw_obligant) {
			this.r150_rw_obligant = r150_rw_obligant;
		}
		public BigDecimal getR150_rav() {
			return r150_rav;
		}
		public void setR150_rav(BigDecimal r150_rav) {
			this.r150_rav = r150_rav;
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
		public OFF_BS_ITEMS_Summary_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		
}
