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
@Table(name = "BRRS_COMMON_DISCLOSURE_SUMMARYTABLE", schema = "BRRS")
public class Common_Disclosure_Summary_Entity {

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "REPORT_DATE")
    private Date REPORT_DATE;

    @Column(name = "R7_PRODUCT", length = 100)
    private String R7_PRODUCT;

    @Column(name = "R7_COMPONENT_OF_REGU")
    private BigDecimal R7_COMPONENT_OF_REGU;

    @Column(name = "R7_SOURCE_REF", length = 200)
    private String R7_SOURCE_REF;

    @Column(name = "R8_PRODUCT", length = 100)
    private String R8_PRODUCT;

    @Column(name = "R8_COMPONENT_OF_REGU")
    private BigDecimal R8_COMPONENT_OF_REGU;

    @Column(name = "R8_SOURCE_REF", length = 100)
    private String R8_SOURCE_REF;

    @Column(name = "R9_PRODUCT", length = 100)
    private String R9_PRODUCT;

    @Column(name = "R9_COMPONENT_OF_REGU")
    private BigDecimal R9_COMPONENT_OF_REGU;

    @Column(name = "R9_SOURCE_REF", length = 100)
    private String R9_SOURCE_REF;

    @Column(name = "R10_PRODUCT", length = 200)
    private String R10_PRODUCT;

    @Column(name = "R10_COMPONENT_OF_REGU")
    private BigDecimal R10_COMPONENT_OF_REGU;

    @Column(name = "R10_SOURCE_REF", length = 100)
    private String R10_SOURCE_REF;

    @Column(name = "R11_PRODUCT", length = 200)
    private String R11_PRODUCT;

    @Column(name = "R11_COMPONENT_OF_REGU")
    private BigDecimal R11_COMPONENT_OF_REGU;

    @Column(name = "R11_SOURCE_REF", length = 100)
    private String R11_SOURCE_REF;

    @Column(name = "R12_PRODUCT", length = 100)
    private String R12_PRODUCT;

    @Column(name = "R12_COMPONENT_OF_REGU")
    private BigDecimal R12_COMPONENT_OF_REGU;

    @Column(name = "R12_SOURCE_REF", length = 100)
    private String R12_SOURCE_REF;

    @Column(name = "R13_PRODUCT", length = 100)
    private String R13_PRODUCT;

    @Column(name = "R13_COMPONENT_OF_REGU")
    private BigDecimal R13_COMPONENT_OF_REGU;

    @Column(name = "R13_SOURCE_REF", length = 100)
    private String R13_SOURCE_REF;

    @Column(name = "R14_PRODUCT", length = 100)
    private String R14_PRODUCT;

    @Column(name = "R14_COMPONENT_OF_REGU")
    private BigDecimal R14_COMPONENT_OF_REGU;

    @Column(name = "R14_SOURCE_REF", length = 100)
    private String R14_SOURCE_REF;

    @Column(name = "REPORT_VERSION", length = 100)
    private BigDecimal REPORT_VERSION;

    @Column(name = "REPORT_FREQUENCY", length = 100)
    private String REPORT_FREQUENCY;

    @Column(name = "REPORT_CODE", length = 100)
    private String REPORT_CODE;

    @Column(name = "REPORT_DESC", length = 100)
    private String REPORT_DESC;

    @Column(name = "ENTITY_FLG", length = 1)
    private String ENTITY_FLG;

    @Column(name = "MODIFY_FLG", length = 1)
    private String MODIFY_FLG;

    @Column(name = "DEL_FLG", length = 1)
    private String DEL_FLG;

    public Date getREPORT_DATE() {
        return REPORT_DATE;
    }

    public void setREPORT_DATE(Date rEPORT_DATE) {
        REPORT_DATE = rEPORT_DATE;
    }

    public String getR7_PRODUCT() {
        return R7_PRODUCT;
    }

    public void setR7_PRODUCT(String r7_PRODUCT) {
        R7_PRODUCT = r7_PRODUCT;
    }

    public BigDecimal getR7_COMPONENT_OF_REGU() {
        return R7_COMPONENT_OF_REGU;
    }

    public void setR7_COMPONENT_OF_REGU(BigDecimal r7_COMPONENT_OF_REGU) {
        R7_COMPONENT_OF_REGU = r7_COMPONENT_OF_REGU;
    }

    public String getR7_SOURCE_REF() {
        return R7_SOURCE_REF;
    }

    public void setR7_SOURCE_REF(String r7_SOURCE_REF) {
        R7_SOURCE_REF = r7_SOURCE_REF;
    }

    public String getR8_PRODUCT() {
        return R8_PRODUCT;
    }

    public void setR8_PRODUCT(String r8_PRODUCT) {
        R8_PRODUCT = r8_PRODUCT;
    }

    public BigDecimal getR8_COMPONENT_OF_REGU() {
        return R8_COMPONENT_OF_REGU;
    }

    public void setR8_COMPONENT_OF_REGU(BigDecimal r8_COMPONENT_OF_REGU) {
        R8_COMPONENT_OF_REGU = r8_COMPONENT_OF_REGU;
    }

    public String getR8_SOURCE_REF() {
        return R8_SOURCE_REF;
    }

    public void setR8_SOURCE_REF(String r8_SOURCE_REF) {
        R8_SOURCE_REF = r8_SOURCE_REF;
    }

    public String getR9_PRODUCT() {
        return R9_PRODUCT;
    }

    public void setR9_PRODUCT(String r9_PRODUCT) {
        R9_PRODUCT = r9_PRODUCT;
    }

    public BigDecimal getR9_COMPONENT_OF_REGU() {
        return R9_COMPONENT_OF_REGU;
    }

    public void setR9_COMPONENT_OF_REGU(BigDecimal r9_COMPONENT_OF_REGU) {
        R9_COMPONENT_OF_REGU = r9_COMPONENT_OF_REGU;
    }

    public String getR9_SOURCE_REF() {
        return R9_SOURCE_REF;
    }

    public void setR9_SOURCE_REF(String r9_SOURCE_REF) {
        R9_SOURCE_REF = r9_SOURCE_REF;
    }

    public String getR10_PRODUCT() {
        return R10_PRODUCT;
    }

    public void setR10_PRODUCT(String r10_PRODUCT) {
        R10_PRODUCT = r10_PRODUCT;
    }

    public BigDecimal getR10_COMPONENT_OF_REGU() {
        return R10_COMPONENT_OF_REGU;
    }

    public void setR10_COMPONENT_OF_REGU(BigDecimal r10_COMPONENT_OF_REGU) {
        R10_COMPONENT_OF_REGU = r10_COMPONENT_OF_REGU;
    }

    public String getR10_SOURCE_REF() {
        return R10_SOURCE_REF;
    }

    public void setR10_SOURCE_REF(String r10_SOURCE_REF) {
        R10_SOURCE_REF = r10_SOURCE_REF;
    }

    public String getR11_PRODUCT() {
        return R11_PRODUCT;
    }

    public void setR11_PRODUCT(String r11_PRODUCT) {
        R11_PRODUCT = r11_PRODUCT;
    }

    public BigDecimal getR11_COMPONENT_OF_REGU() {
        return R11_COMPONENT_OF_REGU;
    }

    public void setR11_COMPONENT_OF_REGU(BigDecimal r11_COMPONENT_OF_REGU) {
        R11_COMPONENT_OF_REGU = r11_COMPONENT_OF_REGU;
    }

    public String getR11_SOURCE_REF() {
        return R11_SOURCE_REF;
    }

    public void setR11_SOURCE_REF(String r11_SOURCE_REF) {
        R11_SOURCE_REF = r11_SOURCE_REF;
    }

    public String getR12_PRODUCT() {
        return R12_PRODUCT;
    }

    public void setR12_PRODUCT(String r12_PRODUCT) {
        R12_PRODUCT = r12_PRODUCT;
    }

    public BigDecimal getR12_COMPONENT_OF_REGU() {
        return R12_COMPONENT_OF_REGU;
    }

    public void setR12_COMPONENT_OF_REGU(BigDecimal r12_COMPONENT_OF_REGU) {
        R12_COMPONENT_OF_REGU = r12_COMPONENT_OF_REGU;
    }

    public String getR12_SOURCE_REF() {
        return R12_SOURCE_REF;
    }

    public void setR12_SOURCE_REF(String r12_SOURCE_REF) {
        R12_SOURCE_REF = r12_SOURCE_REF;
    }

    public String getR13_PRODUCT() {
        return R13_PRODUCT;
    }

    public void setR13_PRODUCT(String r13_PRODUCT) {
        R13_PRODUCT = r13_PRODUCT;
    }

    public BigDecimal getR13_COMPONENT_OF_REGU() {
        return R13_COMPONENT_OF_REGU;
    }

    public void setR13_COMPONENT_OF_REGU(BigDecimal r13_COMPONENT_OF_REGU) {
        R13_COMPONENT_OF_REGU = r13_COMPONENT_OF_REGU;
    }

    public String getR13_SOURCE_REF() {
        return R13_SOURCE_REF;
    }

    public void setR13_SOURCE_REF(String r13_SOURCE_REF) {
        R13_SOURCE_REF = r13_SOURCE_REF;
    }

    public String getR14_PRODUCT() {
        return R14_PRODUCT;
    }

    public void setR14_PRODUCT(String r14_PRODUCT) {
        R14_PRODUCT = r14_PRODUCT;
    }

    public BigDecimal getR14_COMPONENT_OF_REGU() {
        return R14_COMPONENT_OF_REGU;
    }

    public void setR14_COMPONENT_OF_REGU(BigDecimal r14_COMPONENT_OF_REGU) {
        R14_COMPONENT_OF_REGU = r14_COMPONENT_OF_REGU;
    }

    public String getR14_SOURCE_REF() {
        return R14_SOURCE_REF;
    }

    public void setR14_SOURCE_REF(String r14_SOURCE_REF) {
        R14_SOURCE_REF = r14_SOURCE_REF;
    }

    public BigDecimal getREPORT_VERSION() {
        return REPORT_VERSION;
    }

    public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
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

    public Common_Disclosure_Summary_Entity() {
        super();
    }

    
}
