package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_GL_SCH_MANUAL_ARCHIVALTABLE_SUMMARY", schema = "BRRS")

public class GL_SCH_Manual_Archival_Summary_Entity {

	/* ================= R61 ================= */
    @Column(name = "R61_PRODUCT")
    private String R61_PRODUCT;

    @Column(name = "R61_FIG_BAL_BWP1")
    private BigDecimal R61_FIG_BAL_BWP1;

    @Column(name = "R61_FIG_BAL_BWP2")
    private BigDecimal R61_FIG_BAL_BWP2;

    @Column(name = "R61_AMT_ADJ_BWP1")
    private BigDecimal R61_AMT_ADJ_BWP1;

    @Column(name = "R61_AMT_ADJ_BWP2")
    private BigDecimal R61_AMT_ADJ_BWP2;

    @Column(name = "R61_NET_AMT_BWP1")
    private BigDecimal R61_NET_AMT_BWP1;

    @Column(name = "R61_NET_AMT_BWP2")
    private BigDecimal R61_NET_AMT_BWP2;

    @Column(name = "R61_BAL_SUB_BWP1")
    private BigDecimal R61_BAL_SUB_BWP1;

    @Column(name = "R61_BAL_SUB_BWP2")
    private BigDecimal R61_BAL_SUB_BWP2;

    @Column(name = "R61_BAL_ACT_SUB_BWP1")
    private BigDecimal R61_BAL_ACT_SUB_BWP1;

    @Column(name = "R61_BAL_ACT_SUB_BWP2")
    private BigDecimal R61_BAL_ACT_SUB_BWP2;
	
	
	
    /* ================= R103 ================= */
    @Column(name = "R103_PRODUCT")
    private String R103_PRODUCT;

    @Column(name = "R103_FIG_BAL_BWP1")
    private BigDecimal R103_FIG_BAL_BWP1;

    @Column(name = "R103_FIG_BAL_BWP2")
    private BigDecimal R103_FIG_BAL_BWP2;

    @Column(name = "R103_AMT_ADJ_BWP1")
    private BigDecimal R103_AMT_ADJ_BWP1;

    @Column(name = "R103_AMT_ADJ_BWP2")
    private BigDecimal R103_AMT_ADJ_BWP2;

    @Column(name = "R103_NET_AMT_BWP1")
    private BigDecimal R103_NET_AMT_BWP1;

    @Column(name = "R103_NET_AMT_BWP2")
    private BigDecimal R103_NET_AMT_BWP2;

    @Column(name = "R103_BAL_SUB_BWP1")
    private BigDecimal R103_BAL_SUB_BWP1;

    @Column(name = "R103_BAL_SUB_BWP2")
    private BigDecimal R103_BAL_SUB_BWP2;

    @Column(name = "R103_BAL_ACT_SUB_BWP1")
    private BigDecimal R103_BAL_ACT_SUB_BWP1;

    @Column(name = "R103_BAL_ACT_SUB_BWP2")
    private BigDecimal R103_BAL_ACT_SUB_BWP2;
	
	 /* ================= R139 ================= */
    @Column(name = "R139_PRODUCT")
    private String R139_PRODUCT;

    @Column(name = "R139_FIG_BAL_BWP1")
    private BigDecimal R139_FIG_BAL_BWP1;

    @Column(name = "R139_FIG_BAL_BWP2")
    private BigDecimal R139_FIG_BAL_BWP2;

    @Column(name = "R139_AMT_ADJ_BWP1")
    private BigDecimal R139_AMT_ADJ_BWP1;

    @Column(name = "R139_AMT_ADJ_BWP2")
    private BigDecimal R139_AMT_ADJ_BWP2;

    @Column(name = "R139_NET_AMT_BWP1")
    private BigDecimal R139_NET_AMT_BWP1;

    @Column(name = "R139_NET_AMT_BWP2")
    private BigDecimal R139_NET_AMT_BWP2;

    @Column(name = "R139_BAL_SUB_BWP1")
    private BigDecimal R139_BAL_SUB_BWP1;

    @Column(name = "R139_BAL_SUB_BWP2")
    private BigDecimal R139_BAL_SUB_BWP2;

    @Column(name = "R139_BAL_ACT_SUB_BWP1")
    private BigDecimal R139_BAL_ACT_SUB_BWP1;

    @Column(name = "R139_BAL_ACT_SUB_BWP2")
    private BigDecimal R139_BAL_ACT_SUB_BWP2;
	
	
	    /* ================= R130 ================= */
    @Column(name = "R130_PRODUCT")
    private String R130_PRODUCT;

    @Column(name = "R130_FIG_BAL_BWP1")
    private BigDecimal R130_FIG_BAL_BWP1;

    @Column(name = "R130_FIG_BAL_BWP2")
    private BigDecimal R130_FIG_BAL_BWP2;

    @Column(name = "R130_AMT_ADJ_BWP1")
    private BigDecimal R130_AMT_ADJ_BWP1;

    @Column(name = "R130_AMT_ADJ_BWP2")
    private BigDecimal R130_AMT_ADJ_BWP2;

    @Column(name = "R130_NET_AMT_BWP1")
    private BigDecimal R130_NET_AMT_BWP1;

    @Column(name = "R130_NET_AMT_BWP2")
    private BigDecimal R130_NET_AMT_BWP2;

    @Column(name = "R130_BAL_SUB_BWP1")
    private BigDecimal R130_BAL_SUB_BWP1;

    @Column(name = "R130_BAL_SUB_BWP2")
    private BigDecimal R130_BAL_SUB_BWP2;

    @Column(name = "R130_BAL_ACT_SUB_BWP1")
    private BigDecimal R130_BAL_ACT_SUB_BWP1;

    @Column(name = "R130_BAL_ACT_SUB_BWP2")
    private BigDecimal R130_BAL_ACT_SUB_BWP2;
	
	
	    /* ================= R241 ================= */
    @Column(name = "R241_PRODUCT")
    private String R241_PRODUCT;

    @Column(name = "R241_FIG_BAL_BWP1")
    private BigDecimal R241_FIG_BAL_BWP1;

    @Column(name = "R241_FIG_BAL_BWP2")
    private BigDecimal R241_FIG_BAL_BWP2;

    @Column(name = "R241_AMT_ADJ_BWP1")
    private BigDecimal R241_AMT_ADJ_BWP1;

    @Column(name = "R241_AMT_ADJ_BWP2")
    private BigDecimal R241_AMT_ADJ_BWP2;

    @Column(name = "R241_NET_AMT_BWP1")
    private BigDecimal R241_NET_AMT_BWP1;

    @Column(name = "R241_NET_AMT_BWP2")
    private BigDecimal R241_NET_AMT_BWP2;

    @Column(name = "R241_BAL_SUB_BWP1")
    private BigDecimal R241_BAL_SUB_BWP1;

    @Column(name = "R241_BAL_SUB_BWP2")
    private BigDecimal R241_BAL_SUB_BWP2;

    @Column(name = "R241_BAL_ACT_SUB_BWP1")
    private BigDecimal R241_BAL_ACT_SUB_BWP1;

    @Column(name = "R241_BAL_ACT_SUB_BWP2")
    private BigDecimal R241_BAL_ACT_SUB_BWP2;


    /* ================= R243 ================= */
    @Column(name = "R243_PRODUCT")
    private String R243_PRODUCT;

    @Column(name = "R243_FIG_BAL_BWP1")
    private BigDecimal R243_FIG_BAL_BWP1;

    @Column(name = "R243_FIG_BAL_BWP2")
    private BigDecimal R243_FIG_BAL_BWP2;

    @Column(name = "R243_AMT_ADJ_BWP1")
    private BigDecimal R243_AMT_ADJ_BWP1;

    @Column(name = "R243_AMT_ADJ_BWP2")
    private BigDecimal R243_AMT_ADJ_BWP2;

    @Column(name = "R243_NET_AMT_BWP1")
    private BigDecimal R243_NET_AMT_BWP1;

    @Column(name = "R243_NET_AMT_BWP2")
    private BigDecimal R243_NET_AMT_BWP2;

    @Column(name = "R243_BAL_SUB_BWP1")
    private BigDecimal R243_BAL_SUB_BWP1;

    @Column(name = "R243_BAL_SUB_BWP2")
    private BigDecimal R243_BAL_SUB_BWP2;

    @Column(name = "R243_BAL_ACT_SUB_BWP1")
    private BigDecimal R243_BAL_ACT_SUB_BWP1;

    @Column(name = "R243_BAL_ACT_SUB_BWP2")
    private BigDecimal R243_BAL_ACT_SUB_BWP2;
	
	 /* ================= R245 ================= */
    @Column(name = "R245_PRODUCT")
    private String R245_PRODUCT;

    @Column(name = "R245_FIG_BAL_BWP1")
    private BigDecimal R245_FIG_BAL_BWP1;

    @Column(name = "R245_FIG_BAL_BWP2")
    private BigDecimal R245_FIG_BAL_BWP2;

    @Column(name = "R245_AMT_ADJ_BWP1")
    private BigDecimal R245_AMT_ADJ_BWP1;

    @Column(name = "R245_AMT_ADJ_BWP2")
    private BigDecimal R245_AMT_ADJ_BWP2;

    @Column(name = "R245_NET_AMT_BWP1")
    private BigDecimal R245_NET_AMT_BWP1;

    @Column(name = "R245_NET_AMT_BWP2")
    private BigDecimal R245_NET_AMT_BWP2;

    @Column(name = "R245_BAL_SUB_BWP1")
    private BigDecimal R245_BAL_SUB_BWP1;

    @Column(name = "R245_BAL_SUB_BWP2")
    private BigDecimal R245_BAL_SUB_BWP2;

    @Column(name = "R245_BAL_ACT_SUB_BWP1")
    private BigDecimal R245_BAL_ACT_SUB_BWP1;

    @Column(name = "R245_BAL_ACT_SUB_BWP2")
    private BigDecimal R245_BAL_ACT_SUB_BWP2;
    /* ================= COMMON ================= */

    @Id
    @Column(name = "REPORT_DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date REPORT_DATE;

    @Column(name = "REPORT_VERSION")
    private String REPORT_VERSION;

    @Column(name = "REPORT_FREQUENCY")
    private String REPORT_FREQUENCY;

    @Column(name = "REPORT_CODE")
    private String REPORT_CODE;

    @Column(name = "REPORT_DESC")
    private String REPORT_DESC;

    @Column(name = "ENTITY_FLG")
    private String ENTITY_FLG;

    @Column(name = "MODIFY_FLG")
    private String MODIFY_FLG;

    @Column(name = "DEL_FLG")
    private String DEL_FLG;

	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}

	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}

	public BigDecimal getR61_FIG_BAL_BWP1() {
		return R61_FIG_BAL_BWP1;
	}

	public void setR61_FIG_BAL_BWP1(BigDecimal r61_FIG_BAL_BWP1) {
		R61_FIG_BAL_BWP1 = r61_FIG_BAL_BWP1;
	}

	public BigDecimal getR61_FIG_BAL_BWP2() {
		return R61_FIG_BAL_BWP2;
	}

	public void setR61_FIG_BAL_BWP2(BigDecimal r61_FIG_BAL_BWP2) {
		R61_FIG_BAL_BWP2 = r61_FIG_BAL_BWP2;
	}

	public BigDecimal getR61_AMT_ADJ_BWP1() {
		return R61_AMT_ADJ_BWP1;
	}

	public void setR61_AMT_ADJ_BWP1(BigDecimal r61_AMT_ADJ_BWP1) {
		R61_AMT_ADJ_BWP1 = r61_AMT_ADJ_BWP1;
	}

	public BigDecimal getR61_AMT_ADJ_BWP2() {
		return R61_AMT_ADJ_BWP2;
	}

	public void setR61_AMT_ADJ_BWP2(BigDecimal r61_AMT_ADJ_BWP2) {
		R61_AMT_ADJ_BWP2 = r61_AMT_ADJ_BWP2;
	}

	public BigDecimal getR61_NET_AMT_BWP1() {
		return R61_NET_AMT_BWP1;
	}

	public void setR61_NET_AMT_BWP1(BigDecimal r61_NET_AMT_BWP1) {
		R61_NET_AMT_BWP1 = r61_NET_AMT_BWP1;
	}

	public BigDecimal getR61_NET_AMT_BWP2() {
		return R61_NET_AMT_BWP2;
	}

	public void setR61_NET_AMT_BWP2(BigDecimal r61_NET_AMT_BWP2) {
		R61_NET_AMT_BWP2 = r61_NET_AMT_BWP2;
	}

	public BigDecimal getR61_BAL_SUB_BWP1() {
		return R61_BAL_SUB_BWP1;
	}

	public void setR61_BAL_SUB_BWP1(BigDecimal r61_BAL_SUB_BWP1) {
		R61_BAL_SUB_BWP1 = r61_BAL_SUB_BWP1;
	}

	public BigDecimal getR61_BAL_SUB_BWP2() {
		return R61_BAL_SUB_BWP2;
	}

	public void setR61_BAL_SUB_BWP2(BigDecimal r61_BAL_SUB_BWP2) {
		R61_BAL_SUB_BWP2 = r61_BAL_SUB_BWP2;
	}

	public BigDecimal getR61_BAL_ACT_SUB_BWP1() {
		return R61_BAL_ACT_SUB_BWP1;
	}

	public void setR61_BAL_ACT_SUB_BWP1(BigDecimal r61_BAL_ACT_SUB_BWP1) {
		R61_BAL_ACT_SUB_BWP1 = r61_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR61_BAL_ACT_SUB_BWP2() {
		return R61_BAL_ACT_SUB_BWP2;
	}

	public void setR61_BAL_ACT_SUB_BWP2(BigDecimal r61_BAL_ACT_SUB_BWP2) {
		R61_BAL_ACT_SUB_BWP2 = r61_BAL_ACT_SUB_BWP2;
	}

	public String getR103_PRODUCT() {
		return R103_PRODUCT;
	}

	public void setR103_PRODUCT(String r103_PRODUCT) {
		R103_PRODUCT = r103_PRODUCT;
	}

	public BigDecimal getR103_FIG_BAL_BWP1() {
		return R103_FIG_BAL_BWP1;
	}

	public void setR103_FIG_BAL_BWP1(BigDecimal r103_FIG_BAL_BWP1) {
		R103_FIG_BAL_BWP1 = r103_FIG_BAL_BWP1;
	}

	public BigDecimal getR103_FIG_BAL_BWP2() {
		return R103_FIG_BAL_BWP2;
	}

	public void setR103_FIG_BAL_BWP2(BigDecimal r103_FIG_BAL_BWP2) {
		R103_FIG_BAL_BWP2 = r103_FIG_BAL_BWP2;
	}

	public BigDecimal getR103_AMT_ADJ_BWP1() {
		return R103_AMT_ADJ_BWP1;
	}

	public void setR103_AMT_ADJ_BWP1(BigDecimal r103_AMT_ADJ_BWP1) {
		R103_AMT_ADJ_BWP1 = r103_AMT_ADJ_BWP1;
	}

	public BigDecimal getR103_AMT_ADJ_BWP2() {
		return R103_AMT_ADJ_BWP2;
	}

	public void setR103_AMT_ADJ_BWP2(BigDecimal r103_AMT_ADJ_BWP2) {
		R103_AMT_ADJ_BWP2 = r103_AMT_ADJ_BWP2;
	}

	public BigDecimal getR103_NET_AMT_BWP1() {
		return R103_NET_AMT_BWP1;
	}

	public void setR103_NET_AMT_BWP1(BigDecimal r103_NET_AMT_BWP1) {
		R103_NET_AMT_BWP1 = r103_NET_AMT_BWP1;
	}

	public BigDecimal getR103_NET_AMT_BWP2() {
		return R103_NET_AMT_BWP2;
	}

	public void setR103_NET_AMT_BWP2(BigDecimal r103_NET_AMT_BWP2) {
		R103_NET_AMT_BWP2 = r103_NET_AMT_BWP2;
	}

	public BigDecimal getR103_BAL_SUB_BWP1() {
		return R103_BAL_SUB_BWP1;
	}

	public void setR103_BAL_SUB_BWP1(BigDecimal r103_BAL_SUB_BWP1) {
		R103_BAL_SUB_BWP1 = r103_BAL_SUB_BWP1;
	}

	public BigDecimal getR103_BAL_SUB_BWP2() {
		return R103_BAL_SUB_BWP2;
	}

	public void setR103_BAL_SUB_BWP2(BigDecimal r103_BAL_SUB_BWP2) {
		R103_BAL_SUB_BWP2 = r103_BAL_SUB_BWP2;
	}

	public BigDecimal getR103_BAL_ACT_SUB_BWP1() {
		return R103_BAL_ACT_SUB_BWP1;
	}

	public void setR103_BAL_ACT_SUB_BWP1(BigDecimal r103_BAL_ACT_SUB_BWP1) {
		R103_BAL_ACT_SUB_BWP1 = r103_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR103_BAL_ACT_SUB_BWP2() {
		return R103_BAL_ACT_SUB_BWP2;
	}

	public void setR103_BAL_ACT_SUB_BWP2(BigDecimal r103_BAL_ACT_SUB_BWP2) {
		R103_BAL_ACT_SUB_BWP2 = r103_BAL_ACT_SUB_BWP2;
	}

	public String getR139_PRODUCT() {
		return R139_PRODUCT;
	}

	public void setR139_PRODUCT(String r139_PRODUCT) {
		R139_PRODUCT = r139_PRODUCT;
	}

	public BigDecimal getR139_FIG_BAL_BWP1() {
		return R139_FIG_BAL_BWP1;
	}

	public void setR139_FIG_BAL_BWP1(BigDecimal r139_FIG_BAL_BWP1) {
		R139_FIG_BAL_BWP1 = r139_FIG_BAL_BWP1;
	}

	public BigDecimal getR139_FIG_BAL_BWP2() {
		return R139_FIG_BAL_BWP2;
	}

	public void setR139_FIG_BAL_BWP2(BigDecimal r139_FIG_BAL_BWP2) {
		R139_FIG_BAL_BWP2 = r139_FIG_BAL_BWP2;
	}

	public BigDecimal getR139_AMT_ADJ_BWP1() {
		return R139_AMT_ADJ_BWP1;
	}

	public void setR139_AMT_ADJ_BWP1(BigDecimal r139_AMT_ADJ_BWP1) {
		R139_AMT_ADJ_BWP1 = r139_AMT_ADJ_BWP1;
	}

	public BigDecimal getR139_AMT_ADJ_BWP2() {
		return R139_AMT_ADJ_BWP2;
	}

	public void setR139_AMT_ADJ_BWP2(BigDecimal r139_AMT_ADJ_BWP2) {
		R139_AMT_ADJ_BWP2 = r139_AMT_ADJ_BWP2;
	}

	public BigDecimal getR139_NET_AMT_BWP1() {
		return R139_NET_AMT_BWP1;
	}

	public void setR139_NET_AMT_BWP1(BigDecimal r139_NET_AMT_BWP1) {
		R139_NET_AMT_BWP1 = r139_NET_AMT_BWP1;
	}

	public BigDecimal getR139_NET_AMT_BWP2() {
		return R139_NET_AMT_BWP2;
	}

	public void setR139_NET_AMT_BWP2(BigDecimal r139_NET_AMT_BWP2) {
		R139_NET_AMT_BWP2 = r139_NET_AMT_BWP2;
	}

	public BigDecimal getR139_BAL_SUB_BWP1() {
		return R139_BAL_SUB_BWP1;
	}

	public void setR139_BAL_SUB_BWP1(BigDecimal r139_BAL_SUB_BWP1) {
		R139_BAL_SUB_BWP1 = r139_BAL_SUB_BWP1;
	}

	public BigDecimal getR139_BAL_SUB_BWP2() {
		return R139_BAL_SUB_BWP2;
	}

	public void setR139_BAL_SUB_BWP2(BigDecimal r139_BAL_SUB_BWP2) {
		R139_BAL_SUB_BWP2 = r139_BAL_SUB_BWP2;
	}

	public BigDecimal getR139_BAL_ACT_SUB_BWP1() {
		return R139_BAL_ACT_SUB_BWP1;
	}

	public void setR139_BAL_ACT_SUB_BWP1(BigDecimal r139_BAL_ACT_SUB_BWP1) {
		R139_BAL_ACT_SUB_BWP1 = r139_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR139_BAL_ACT_SUB_BWP2() {
		return R139_BAL_ACT_SUB_BWP2;
	}

	public void setR139_BAL_ACT_SUB_BWP2(BigDecimal r139_BAL_ACT_SUB_BWP2) {
		R139_BAL_ACT_SUB_BWP2 = r139_BAL_ACT_SUB_BWP2;
	}

	public String getR130_PRODUCT() {
		return R130_PRODUCT;
	}

	public void setR130_PRODUCT(String r130_PRODUCT) {
		R130_PRODUCT = r130_PRODUCT;
	}

	public BigDecimal getR130_FIG_BAL_BWP1() {
		return R130_FIG_BAL_BWP1;
	}

	public void setR130_FIG_BAL_BWP1(BigDecimal r130_FIG_BAL_BWP1) {
		R130_FIG_BAL_BWP1 = r130_FIG_BAL_BWP1;
	}

	public BigDecimal getR130_FIG_BAL_BWP2() {
		return R130_FIG_BAL_BWP2;
	}

	public void setR130_FIG_BAL_BWP2(BigDecimal r130_FIG_BAL_BWP2) {
		R130_FIG_BAL_BWP2 = r130_FIG_BAL_BWP2;
	}

	public BigDecimal getR130_AMT_ADJ_BWP1() {
		return R130_AMT_ADJ_BWP1;
	}

	public void setR130_AMT_ADJ_BWP1(BigDecimal r130_AMT_ADJ_BWP1) {
		R130_AMT_ADJ_BWP1 = r130_AMT_ADJ_BWP1;
	}

	public BigDecimal getR130_AMT_ADJ_BWP2() {
		return R130_AMT_ADJ_BWP2;
	}

	public void setR130_AMT_ADJ_BWP2(BigDecimal r130_AMT_ADJ_BWP2) {
		R130_AMT_ADJ_BWP2 = r130_AMT_ADJ_BWP2;
	}

	public BigDecimal getR130_NET_AMT_BWP1() {
		return R130_NET_AMT_BWP1;
	}

	public void setR130_NET_AMT_BWP1(BigDecimal r130_NET_AMT_BWP1) {
		R130_NET_AMT_BWP1 = r130_NET_AMT_BWP1;
	}

	public BigDecimal getR130_NET_AMT_BWP2() {
		return R130_NET_AMT_BWP2;
	}

	public void setR130_NET_AMT_BWP2(BigDecimal r130_NET_AMT_BWP2) {
		R130_NET_AMT_BWP2 = r130_NET_AMT_BWP2;
	}

	public BigDecimal getR130_BAL_SUB_BWP1() {
		return R130_BAL_SUB_BWP1;
	}

	public void setR130_BAL_SUB_BWP1(BigDecimal r130_BAL_SUB_BWP1) {
		R130_BAL_SUB_BWP1 = r130_BAL_SUB_BWP1;
	}

	public BigDecimal getR130_BAL_SUB_BWP2() {
		return R130_BAL_SUB_BWP2;
	}

	public void setR130_BAL_SUB_BWP2(BigDecimal r130_BAL_SUB_BWP2) {
		R130_BAL_SUB_BWP2 = r130_BAL_SUB_BWP2;
	}

	public BigDecimal getR130_BAL_ACT_SUB_BWP1() {
		return R130_BAL_ACT_SUB_BWP1;
	}

	public void setR130_BAL_ACT_SUB_BWP1(BigDecimal r130_BAL_ACT_SUB_BWP1) {
		R130_BAL_ACT_SUB_BWP1 = r130_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR130_BAL_ACT_SUB_BWP2() {
		return R130_BAL_ACT_SUB_BWP2;
	}

	public void setR130_BAL_ACT_SUB_BWP2(BigDecimal r130_BAL_ACT_SUB_BWP2) {
		R130_BAL_ACT_SUB_BWP2 = r130_BAL_ACT_SUB_BWP2;
	}

	public String getR241_PRODUCT() {
		return R241_PRODUCT;
	}

	public void setR241_PRODUCT(String r241_PRODUCT) {
		R241_PRODUCT = r241_PRODUCT;
	}

	public BigDecimal getR241_FIG_BAL_BWP1() {
		return R241_FIG_BAL_BWP1;
	}

	public void setR241_FIG_BAL_BWP1(BigDecimal r241_FIG_BAL_BWP1) {
		R241_FIG_BAL_BWP1 = r241_FIG_BAL_BWP1;
	}

	public BigDecimal getR241_FIG_BAL_BWP2() {
		return R241_FIG_BAL_BWP2;
	}

	public void setR241_FIG_BAL_BWP2(BigDecimal r241_FIG_BAL_BWP2) {
		R241_FIG_BAL_BWP2 = r241_FIG_BAL_BWP2;
	}

	public BigDecimal getR241_AMT_ADJ_BWP1() {
		return R241_AMT_ADJ_BWP1;
	}

	public void setR241_AMT_ADJ_BWP1(BigDecimal r241_AMT_ADJ_BWP1) {
		R241_AMT_ADJ_BWP1 = r241_AMT_ADJ_BWP1;
	}

	public BigDecimal getR241_AMT_ADJ_BWP2() {
		return R241_AMT_ADJ_BWP2;
	}

	public void setR241_AMT_ADJ_BWP2(BigDecimal r241_AMT_ADJ_BWP2) {
		R241_AMT_ADJ_BWP2 = r241_AMT_ADJ_BWP2;
	}

	public BigDecimal getR241_NET_AMT_BWP1() {
		return R241_NET_AMT_BWP1;
	}

	public void setR241_NET_AMT_BWP1(BigDecimal r241_NET_AMT_BWP1) {
		R241_NET_AMT_BWP1 = r241_NET_AMT_BWP1;
	}

	public BigDecimal getR241_NET_AMT_BWP2() {
		return R241_NET_AMT_BWP2;
	}

	public void setR241_NET_AMT_BWP2(BigDecimal r241_NET_AMT_BWP2) {
		R241_NET_AMT_BWP2 = r241_NET_AMT_BWP2;
	}

	public BigDecimal getR241_BAL_SUB_BWP1() {
		return R241_BAL_SUB_BWP1;
	}

	public void setR241_BAL_SUB_BWP1(BigDecimal r241_BAL_SUB_BWP1) {
		R241_BAL_SUB_BWP1 = r241_BAL_SUB_BWP1;
	}

	public BigDecimal getR241_BAL_SUB_BWP2() {
		return R241_BAL_SUB_BWP2;
	}

	public void setR241_BAL_SUB_BWP2(BigDecimal r241_BAL_SUB_BWP2) {
		R241_BAL_SUB_BWP2 = r241_BAL_SUB_BWP2;
	}

	public BigDecimal getR241_BAL_ACT_SUB_BWP1() {
		return R241_BAL_ACT_SUB_BWP1;
	}

	public void setR241_BAL_ACT_SUB_BWP1(BigDecimal r241_BAL_ACT_SUB_BWP1) {
		R241_BAL_ACT_SUB_BWP1 = r241_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR241_BAL_ACT_SUB_BWP2() {
		return R241_BAL_ACT_SUB_BWP2;
	}

	public void setR241_BAL_ACT_SUB_BWP2(BigDecimal r241_BAL_ACT_SUB_BWP2) {
		R241_BAL_ACT_SUB_BWP2 = r241_BAL_ACT_SUB_BWP2;
	}

	public String getR243_PRODUCT() {
		return R243_PRODUCT;
	}

	public void setR243_PRODUCT(String r243_PRODUCT) {
		R243_PRODUCT = r243_PRODUCT;
	}

	public BigDecimal getR243_FIG_BAL_BWP1() {
		return R243_FIG_BAL_BWP1;
	}

	public void setR243_FIG_BAL_BWP1(BigDecimal r243_FIG_BAL_BWP1) {
		R243_FIG_BAL_BWP1 = r243_FIG_BAL_BWP1;
	}

	public BigDecimal getR243_FIG_BAL_BWP2() {
		return R243_FIG_BAL_BWP2;
	}

	public void setR243_FIG_BAL_BWP2(BigDecimal r243_FIG_BAL_BWP2) {
		R243_FIG_BAL_BWP2 = r243_FIG_BAL_BWP2;
	}

	public BigDecimal getR243_AMT_ADJ_BWP1() {
		return R243_AMT_ADJ_BWP1;
	}

	public void setR243_AMT_ADJ_BWP1(BigDecimal r243_AMT_ADJ_BWP1) {
		R243_AMT_ADJ_BWP1 = r243_AMT_ADJ_BWP1;
	}

	public BigDecimal getR243_AMT_ADJ_BWP2() {
		return R243_AMT_ADJ_BWP2;
	}

	public void setR243_AMT_ADJ_BWP2(BigDecimal r243_AMT_ADJ_BWP2) {
		R243_AMT_ADJ_BWP2 = r243_AMT_ADJ_BWP2;
	}

	public BigDecimal getR243_NET_AMT_BWP1() {
		return R243_NET_AMT_BWP1;
	}

	public void setR243_NET_AMT_BWP1(BigDecimal r243_NET_AMT_BWP1) {
		R243_NET_AMT_BWP1 = r243_NET_AMT_BWP1;
	}

	public BigDecimal getR243_NET_AMT_BWP2() {
		return R243_NET_AMT_BWP2;
	}

	public void setR243_NET_AMT_BWP2(BigDecimal r243_NET_AMT_BWP2) {
		R243_NET_AMT_BWP2 = r243_NET_AMT_BWP2;
	}

	public BigDecimal getR243_BAL_SUB_BWP1() {
		return R243_BAL_SUB_BWP1;
	}

	public void setR243_BAL_SUB_BWP1(BigDecimal r243_BAL_SUB_BWP1) {
		R243_BAL_SUB_BWP1 = r243_BAL_SUB_BWP1;
	}

	public BigDecimal getR243_BAL_SUB_BWP2() {
		return R243_BAL_SUB_BWP2;
	}

	public void setR243_BAL_SUB_BWP2(BigDecimal r243_BAL_SUB_BWP2) {
		R243_BAL_SUB_BWP2 = r243_BAL_SUB_BWP2;
	}

	public BigDecimal getR243_BAL_ACT_SUB_BWP1() {
		return R243_BAL_ACT_SUB_BWP1;
	}

	public void setR243_BAL_ACT_SUB_BWP1(BigDecimal r243_BAL_ACT_SUB_BWP1) {
		R243_BAL_ACT_SUB_BWP1 = r243_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR243_BAL_ACT_SUB_BWP2() {
		return R243_BAL_ACT_SUB_BWP2;
	}

	public void setR243_BAL_ACT_SUB_BWP2(BigDecimal r243_BAL_ACT_SUB_BWP2) {
		R243_BAL_ACT_SUB_BWP2 = r243_BAL_ACT_SUB_BWP2;
	}

	public String getR245_PRODUCT() {
		return R245_PRODUCT;
	}

	public void setR245_PRODUCT(String r245_PRODUCT) {
		R245_PRODUCT = r245_PRODUCT;
	}

	public BigDecimal getR245_FIG_BAL_BWP1() {
		return R245_FIG_BAL_BWP1;
	}

	public void setR245_FIG_BAL_BWP1(BigDecimal r245_FIG_BAL_BWP1) {
		R245_FIG_BAL_BWP1 = r245_FIG_BAL_BWP1;
	}

	public BigDecimal getR245_FIG_BAL_BWP2() {
		return R245_FIG_BAL_BWP2;
	}

	public void setR245_FIG_BAL_BWP2(BigDecimal r245_FIG_BAL_BWP2) {
		R245_FIG_BAL_BWP2 = r245_FIG_BAL_BWP2;
	}

	public BigDecimal getR245_AMT_ADJ_BWP1() {
		return R245_AMT_ADJ_BWP1;
	}

	public void setR245_AMT_ADJ_BWP1(BigDecimal r245_AMT_ADJ_BWP1) {
		R245_AMT_ADJ_BWP1 = r245_AMT_ADJ_BWP1;
	}

	public BigDecimal getR245_AMT_ADJ_BWP2() {
		return R245_AMT_ADJ_BWP2;
	}

	public void setR245_AMT_ADJ_BWP2(BigDecimal r245_AMT_ADJ_BWP2) {
		R245_AMT_ADJ_BWP2 = r245_AMT_ADJ_BWP2;
	}

	public BigDecimal getR245_NET_AMT_BWP1() {
		return R245_NET_AMT_BWP1;
	}

	public void setR245_NET_AMT_BWP1(BigDecimal r245_NET_AMT_BWP1) {
		R245_NET_AMT_BWP1 = r245_NET_AMT_BWP1;
	}

	public BigDecimal getR245_NET_AMT_BWP2() {
		return R245_NET_AMT_BWP2;
	}

	public void setR245_NET_AMT_BWP2(BigDecimal r245_NET_AMT_BWP2) {
		R245_NET_AMT_BWP2 = r245_NET_AMT_BWP2;
	}

	public BigDecimal getR245_BAL_SUB_BWP1() {
		return R245_BAL_SUB_BWP1;
	}

	public void setR245_BAL_SUB_BWP1(BigDecimal r245_BAL_SUB_BWP1) {
		R245_BAL_SUB_BWP1 = r245_BAL_SUB_BWP1;
	}

	public BigDecimal getR245_BAL_SUB_BWP2() {
		return R245_BAL_SUB_BWP2;
	}

	public void setR245_BAL_SUB_BWP2(BigDecimal r245_BAL_SUB_BWP2) {
		R245_BAL_SUB_BWP2 = r245_BAL_SUB_BWP2;
	}

	public BigDecimal getR245_BAL_ACT_SUB_BWP1() {
		return R245_BAL_ACT_SUB_BWP1;
	}

	public void setR245_BAL_ACT_SUB_BWP1(BigDecimal r245_BAL_ACT_SUB_BWP1) {
		R245_BAL_ACT_SUB_BWP1 = r245_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR245_BAL_ACT_SUB_BWP2() {
		return R245_BAL_ACT_SUB_BWP2;
	}

	public void setR245_BAL_ACT_SUB_BWP2(BigDecimal r245_BAL_ACT_SUB_BWP2) {
		R245_BAL_ACT_SUB_BWP2 = r245_BAL_ACT_SUB_BWP2;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}

	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}

	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}

	public GL_SCH_Manual_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

  

    
    
    
    
    
    
}
