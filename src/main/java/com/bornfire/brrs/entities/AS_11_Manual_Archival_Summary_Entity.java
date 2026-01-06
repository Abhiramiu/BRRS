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
@Table(name = "BRRS_AS_11_MANUAL_ARCHIVALTABLE_SUMMARY")
public class AS_11_Manual_Archival_Summary_Entity {

    // R21
    private String r21_product;
    private BigDecimal r21_qua_i_lc;
    private BigDecimal r21_qua_i_qar;
    private BigDecimal r21_qua_i_inr;
    private BigDecimal r21_qua_ii_lc;
    private BigDecimal r21_qua_ii_qar;
    private BigDecimal r21_qua_ii_inr;
    private BigDecimal r21_qua_iii_lc;
    private BigDecimal r21_qua_iii_qar;
    private BigDecimal r21_qua_iii_inr;
    private BigDecimal r21_qua_iv_lc;
    private BigDecimal r21_qua_iv_qar;
    private BigDecimal r21_qua_iv_inr;
    private BigDecimal r21_cumm_inr;
    private BigDecimal r21_cumm_bwp;

    // R46
    private String r46_product;
    private BigDecimal r46_qua_i_lc;
    private BigDecimal r46_qua_i_qar;
    private BigDecimal r46_qua_i_inr;
    private BigDecimal r46_qua_ii_lc;
    private BigDecimal r46_qua_ii_qar;
    private BigDecimal r46_qua_ii_inr;
    private BigDecimal r46_qua_iii_lc;
    private BigDecimal r46_qua_iii_qar;
    private BigDecimal r46_qua_iii_inr;
    private BigDecimal r46_qua_iv_lc;
    private BigDecimal r46_qua_iv_qar;
    private BigDecimal r46_qua_iv_inr;
    private BigDecimal r46_cumm_inr;
    private BigDecimal r46_cumm_bwp;

    // R57
    private String r57_product;
    private BigDecimal r57_qua_i_lc;
    private BigDecimal r57_qua_i_qar;
    private BigDecimal r57_qua_i_inr;
    private BigDecimal r57_qua_ii_lc;
    private BigDecimal r57_qua_ii_qar;
    private BigDecimal r57_qua_ii_inr;
    private BigDecimal r57_qua_iii_lc;
    private BigDecimal r57_qua_iii_qar;
    private BigDecimal r57_qua_iii_inr;
    private BigDecimal r57_qua_iv_lc;
    private BigDecimal r57_qua_iv_qar;
    private BigDecimal r57_qua_iv_inr;
    private BigDecimal r57_cumm_inr;
    private BigDecimal r57_cumm_bwp;

    // R72
    private String r72_product;
    private BigDecimal r72_qua_i_lc;
    private BigDecimal r72_qua_i_qar;
    private BigDecimal r72_qua_i_inr;
    private BigDecimal r72_qua_ii_lc;
    private BigDecimal r72_qua_ii_qar;
    private BigDecimal r72_qua_ii_inr;
    private BigDecimal r72_qua_iii_lc;
    private BigDecimal r72_qua_iii_qar;
    private BigDecimal r72_qua_iii_inr;
    private BigDecimal r72_qua_iv_lc;
    private BigDecimal r72_qua_iv_qar;
    private BigDecimal r72_qua_iv_inr;
    private BigDecimal r72_cumm_inr;
    private BigDecimal r72_cumm_bwp;

    // R73
    private String r73_product;
    private BigDecimal r73_qua_i_lc;
    private BigDecimal r73_qua_i_qar;
    private BigDecimal r73_qua_i_inr;
    private BigDecimal r73_qua_ii_lc;
    private BigDecimal r73_qua_ii_qar;
    private BigDecimal r73_qua_ii_inr;
    private BigDecimal r73_qua_iii_lc;
    private BigDecimal r73_qua_iii_qar;
    private BigDecimal r73_qua_iii_inr;
    private BigDecimal r73_qua_iv_lc;
    private BigDecimal r73_qua_iv_qar;
    private BigDecimal r73_qua_iv_inr;
    private BigDecimal r73_cumm_inr;
    private BigDecimal r73_cumm_bwp;

    // R74
    private String r74_product;
    private BigDecimal r74_qua_i_lc;
    private BigDecimal r74_qua_i_qar;
    private BigDecimal r74_qua_i_inr;
    private BigDecimal r74_qua_ii_lc;
    private BigDecimal r74_qua_ii_qar;
    private BigDecimal r74_qua_ii_inr;
    private BigDecimal r74_qua_iii_lc;
    private BigDecimal r74_qua_iii_qar;
    private BigDecimal r74_qua_iii_inr;
    private BigDecimal r74_qua_iv_lc;
    private BigDecimal r74_qua_iv_qar;
    private BigDecimal r74_qua_iv_inr;
    private BigDecimal r74_cumm_inr;
    private BigDecimal r74_cumm_bwp;

    // R76
    private String r76_product;
    private BigDecimal r76_qua_i_lc;
    private BigDecimal r76_qua_i_qar;
    private BigDecimal r76_qua_i_inr;
    private BigDecimal r76_qua_ii_lc;
    private BigDecimal r76_qua_ii_qar;
    private BigDecimal r76_qua_ii_inr;
    private BigDecimal r76_qua_iii_lc;
    private BigDecimal r76_qua_iii_qar;
    private BigDecimal r76_qua_iii_inr;
    private BigDecimal r76_qua_iv_lc;
    private BigDecimal r76_qua_iv_qar;
    private BigDecimal r76_qua_iv_inr;
    private BigDecimal r76_cumm_inr;
    private BigDecimal r76_cumm_bwp;

    // R77
    private String r77_product;
    private BigDecimal r77_qua_i_lc;
    private BigDecimal r77_qua_i_qar;
    private BigDecimal r77_qua_i_inr;
    private BigDecimal r77_qua_ii_lc;
    private BigDecimal r77_qua_ii_qar;
    private BigDecimal r77_qua_ii_inr;
    private BigDecimal r77_qua_iii_lc;
    private BigDecimal r77_qua_iii_qar;
    private BigDecimal r77_qua_iii_inr;
    private BigDecimal r77_qua_iv_lc;
    private BigDecimal r77_qua_iv_qar;
    private BigDecimal r77_qua_iv_inr;
    private BigDecimal r77_cumm_inr;
    private BigDecimal r77_cumm_bwp;

    // R78
    private String r78_product;
    private BigDecimal r78_qua_i_lc;
    private BigDecimal r78_qua_i_qar;
    private BigDecimal r78_qua_i_inr;
    private BigDecimal r78_qua_ii_lc;
    private BigDecimal r78_qua_ii_qar;
    private BigDecimal r78_qua_ii_inr;
    private BigDecimal r78_qua_iii_lc;
    private BigDecimal r78_qua_iii_qar;
    private BigDecimal r78_qua_iii_inr;
    private BigDecimal r78_qua_iv_lc;
    private BigDecimal r78_qua_iv_qar;
    private BigDecimal r78_qua_iv_inr;
    private BigDecimal r78_cumm_inr;
    private BigDecimal r78_cumm_bwp;

    // R79
    private String r79_product;
    private BigDecimal r79_qua_i_lc;
    private BigDecimal r79_qua_i_qar;
    private BigDecimal r79_qua_i_inr;
    private BigDecimal r79_qua_ii_lc;
    private BigDecimal r79_qua_ii_qar;
    private BigDecimal r79_qua_ii_inr;
    private BigDecimal r79_qua_iii_lc;
    private BigDecimal r79_qua_iii_qar;
    private BigDecimal r79_qua_iii_inr;
    private BigDecimal r79_qua_iv_lc;
    private BigDecimal r79_qua_iv_qar;
    private BigDecimal r79_qua_iv_inr;
    private BigDecimal r79_cumm_inr;
    private BigDecimal r79_cumm_bwp;

    // R80
    private String r80_product;
    private BigDecimal r80_qua_i_lc;
    private BigDecimal r80_qua_i_qar;
    private BigDecimal r80_qua_i_inr;
    private BigDecimal r80_qua_ii_lc;
    private BigDecimal r80_qua_ii_qar;
    private BigDecimal r80_qua_ii_inr;
    private BigDecimal r80_qua_iii_lc;
    private BigDecimal r80_qua_iii_qar;
    private BigDecimal r80_qua_iii_inr;
    private BigDecimal r80_qua_iv_lc;
    private BigDecimal r80_qua_iv_qar;
    private BigDecimal r80_qua_iv_inr;
    private BigDecimal r80_cumm_inr;
    private BigDecimal r80_cumm_bwp;

    // R81
    private String r81_product;
    private BigDecimal r81_qua_i_lc;
    private BigDecimal r81_qua_i_qar;
    private BigDecimal r81_qua_i_inr;
    private BigDecimal r81_qua_ii_lc;
    private BigDecimal r81_qua_ii_qar;
    private BigDecimal r81_qua_ii_inr;
    private BigDecimal r81_qua_iii_lc;
    private BigDecimal r81_qua_iii_qar;
    private BigDecimal r81_qua_iii_inr;
    private BigDecimal r81_qua_iv_lc;
    private BigDecimal r81_qua_iv_qar;
    private BigDecimal r81_qua_iv_inr;
    private BigDecimal r81_cumm_inr;
    private BigDecimal r81_cumm_bwp;

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

    public String getR21_product() {
        return r21_product;
    }

    public void setR21_product(String r21_product) {
        this.r21_product = r21_product;
    }

    public BigDecimal getR21_qua_i_lc() {
        return r21_qua_i_lc;
    }

    public void setR21_qua_i_lc(BigDecimal r21_qua_i_lc) {
        this.r21_qua_i_lc = r21_qua_i_lc;
    }

    public BigDecimal getR21_qua_i_qar() {
        return r21_qua_i_qar;
    }

    public void setR21_qua_i_qar(BigDecimal r21_qua_i_qar) {
        this.r21_qua_i_qar = r21_qua_i_qar;
    }

    public BigDecimal getR21_qua_i_inr() {
        return r21_qua_i_inr;
    }

    public void setR21_qua_i_inr(BigDecimal r21_qua_i_inr) {
        this.r21_qua_i_inr = r21_qua_i_inr;
    }

    public BigDecimal getR21_qua_ii_lc() {
        return r21_qua_ii_lc;
    }

    public void setR21_qua_ii_lc(BigDecimal r21_qua_ii_lc) {
        this.r21_qua_ii_lc = r21_qua_ii_lc;
    }

    public BigDecimal getR21_qua_ii_qar() {
        return r21_qua_ii_qar;
    }

    public void setR21_qua_ii_qar(BigDecimal r21_qua_ii_qar) {
        this.r21_qua_ii_qar = r21_qua_ii_qar;
    }

    public BigDecimal getR21_qua_ii_inr() {
        return r21_qua_ii_inr;
    }

    public void setR21_qua_ii_inr(BigDecimal r21_qua_ii_inr) {
        this.r21_qua_ii_inr = r21_qua_ii_inr;
    }

    public BigDecimal getR21_qua_iii_lc() {
        return r21_qua_iii_lc;
    }

    public void setR21_qua_iii_lc(BigDecimal r21_qua_iii_lc) {
        this.r21_qua_iii_lc = r21_qua_iii_lc;
    }

    public BigDecimal getR21_qua_iii_qar() {
        return r21_qua_iii_qar;
    }

    public void setR21_qua_iii_qar(BigDecimal r21_qua_iii_qar) {
        this.r21_qua_iii_qar = r21_qua_iii_qar;
    }

    public BigDecimal getR21_qua_iii_inr() {
        return r21_qua_iii_inr;
    }

    public void setR21_qua_iii_inr(BigDecimal r21_qua_iii_inr) {
        this.r21_qua_iii_inr = r21_qua_iii_inr;
    }

    public BigDecimal getR21_qua_iv_lc() {
        return r21_qua_iv_lc;
    }

    public void setR21_qua_iv_lc(BigDecimal r21_qua_iv_lc) {
        this.r21_qua_iv_lc = r21_qua_iv_lc;
    }

    public BigDecimal getR21_qua_iv_qar() {
        return r21_qua_iv_qar;
    }

    public void setR21_qua_iv_qar(BigDecimal r21_qua_iv_qar) {
        this.r21_qua_iv_qar = r21_qua_iv_qar;
    }

    public BigDecimal getR21_qua_iv_inr() {
        return r21_qua_iv_inr;
    }

    public void setR21_qua_iv_inr(BigDecimal r21_qua_iv_inr) {
        this.r21_qua_iv_inr = r21_qua_iv_inr;
    }

    public BigDecimal getR21_cumm_inr() {
        return r21_cumm_inr;
    }

    public void setR21_cumm_inr(BigDecimal r21_cumm_inr) {
        this.r21_cumm_inr = r21_cumm_inr;
    }

    public BigDecimal getR21_cumm_bwp() {
        return r21_cumm_bwp;
    }

    public void setR21_cumm_bwp(BigDecimal r21_cumm_bwp) {
        this.r21_cumm_bwp = r21_cumm_bwp;
    }

    public String getR46_product() {
        return r46_product;
    }

    public void setR46_product(String r46_product) {
        this.r46_product = r46_product;
    }

    public BigDecimal getR46_qua_i_lc() {
        return r46_qua_i_lc;
    }

    public void setR46_qua_i_lc(BigDecimal r46_qua_i_lc) {
        this.r46_qua_i_lc = r46_qua_i_lc;
    }

    public BigDecimal getR46_qua_i_qar() {
        return r46_qua_i_qar;
    }

    public void setR46_qua_i_qar(BigDecimal r46_qua_i_qar) {
        this.r46_qua_i_qar = r46_qua_i_qar;
    }

    public BigDecimal getR46_qua_i_inr() {
        return r46_qua_i_inr;
    }

    public void setR46_qua_i_inr(BigDecimal r46_qua_i_inr) {
        this.r46_qua_i_inr = r46_qua_i_inr;
    }

    public BigDecimal getR46_qua_ii_lc() {
        return r46_qua_ii_lc;
    }

    public void setR46_qua_ii_lc(BigDecimal r46_qua_ii_lc) {
        this.r46_qua_ii_lc = r46_qua_ii_lc;
    }

    public BigDecimal getR46_qua_ii_qar() {
        return r46_qua_ii_qar;
    }

    public void setR46_qua_ii_qar(BigDecimal r46_qua_ii_qar) {
        this.r46_qua_ii_qar = r46_qua_ii_qar;
    }

    public BigDecimal getR46_qua_ii_inr() {
        return r46_qua_ii_inr;
    }

    public void setR46_qua_ii_inr(BigDecimal r46_qua_ii_inr) {
        this.r46_qua_ii_inr = r46_qua_ii_inr;
    }

    public BigDecimal getR46_qua_iii_lc() {
        return r46_qua_iii_lc;
    }

    public void setR46_qua_iii_lc(BigDecimal r46_qua_iii_lc) {
        this.r46_qua_iii_lc = r46_qua_iii_lc;
    }

    public BigDecimal getR46_qua_iii_qar() {
        return r46_qua_iii_qar;
    }

    public void setR46_qua_iii_qar(BigDecimal r46_qua_iii_qar) {
        this.r46_qua_iii_qar = r46_qua_iii_qar;
    }

    public BigDecimal getR46_qua_iii_inr() {
        return r46_qua_iii_inr;
    }

    public void setR46_qua_iii_inr(BigDecimal r46_qua_iii_inr) {
        this.r46_qua_iii_inr = r46_qua_iii_inr;
    }

    public BigDecimal getR46_qua_iv_lc() {
        return r46_qua_iv_lc;
    }

    public void setR46_qua_iv_lc(BigDecimal r46_qua_iv_lc) {
        this.r46_qua_iv_lc = r46_qua_iv_lc;
    }

    public BigDecimal getR46_qua_iv_qar() {
        return r46_qua_iv_qar;
    }

    public void setR46_qua_iv_qar(BigDecimal r46_qua_iv_qar) {
        this.r46_qua_iv_qar = r46_qua_iv_qar;
    }

    public BigDecimal getR46_qua_iv_inr() {
        return r46_qua_iv_inr;
    }

    public void setR46_qua_iv_inr(BigDecimal r46_qua_iv_inr) {
        this.r46_qua_iv_inr = r46_qua_iv_inr;
    }

    public BigDecimal getR46_cumm_inr() {
        return r46_cumm_inr;
    }

    public void setR46_cumm_inr(BigDecimal r46_cumm_inr) {
        this.r46_cumm_inr = r46_cumm_inr;
    }

    public BigDecimal getR46_cumm_bwp() {
        return r46_cumm_bwp;
    }

    public void setR46_cumm_bwp(BigDecimal r46_cumm_bwp) {
        this.r46_cumm_bwp = r46_cumm_bwp;
    }

    public String getR57_product() {
        return r57_product;
    }

    public void setR57_product(String r57_product) {
        this.r57_product = r57_product;
    }

    public BigDecimal getR57_qua_i_lc() {
        return r57_qua_i_lc;
    }

    public void setR57_qua_i_lc(BigDecimal r57_qua_i_lc) {
        this.r57_qua_i_lc = r57_qua_i_lc;
    }

    public BigDecimal getR57_qua_i_qar() {
        return r57_qua_i_qar;
    }

    public void setR57_qua_i_qar(BigDecimal r57_qua_i_qar) {
        this.r57_qua_i_qar = r57_qua_i_qar;
    }

    public BigDecimal getR57_qua_i_inr() {
        return r57_qua_i_inr;
    }

    public void setR57_qua_i_inr(BigDecimal r57_qua_i_inr) {
        this.r57_qua_i_inr = r57_qua_i_inr;
    }

    public BigDecimal getR57_qua_ii_lc() {
        return r57_qua_ii_lc;
    }

    public void setR57_qua_ii_lc(BigDecimal r57_qua_ii_lc) {
        this.r57_qua_ii_lc = r57_qua_ii_lc;
    }

    public BigDecimal getR57_qua_ii_qar() {
        return r57_qua_ii_qar;
    }

    public void setR57_qua_ii_qar(BigDecimal r57_qua_ii_qar) {
        this.r57_qua_ii_qar = r57_qua_ii_qar;
    }

    public BigDecimal getR57_qua_ii_inr() {
        return r57_qua_ii_inr;
    }

    public void setR57_qua_ii_inr(BigDecimal r57_qua_ii_inr) {
        this.r57_qua_ii_inr = r57_qua_ii_inr;
    }

    public BigDecimal getR57_qua_iii_lc() {
        return r57_qua_iii_lc;
    }

    public void setR57_qua_iii_lc(BigDecimal r57_qua_iii_lc) {
        this.r57_qua_iii_lc = r57_qua_iii_lc;
    }

    public BigDecimal getR57_qua_iii_qar() {
        return r57_qua_iii_qar;
    }

    public void setR57_qua_iii_qar(BigDecimal r57_qua_iii_qar) {
        this.r57_qua_iii_qar = r57_qua_iii_qar;
    }

    public BigDecimal getR57_qua_iii_inr() {
        return r57_qua_iii_inr;
    }

    public void setR57_qua_iii_inr(BigDecimal r57_qua_iii_inr) {
        this.r57_qua_iii_inr = r57_qua_iii_inr;
    }

    public BigDecimal getR57_qua_iv_lc() {
        return r57_qua_iv_lc;
    }

    public void setR57_qua_iv_lc(BigDecimal r57_qua_iv_lc) {
        this.r57_qua_iv_lc = r57_qua_iv_lc;
    }

    public BigDecimal getR57_qua_iv_qar() {
        return r57_qua_iv_qar;
    }

    public void setR57_qua_iv_qar(BigDecimal r57_qua_iv_qar) {
        this.r57_qua_iv_qar = r57_qua_iv_qar;
    }

    public BigDecimal getR57_qua_iv_inr() {
        return r57_qua_iv_inr;
    }

    public void setR57_qua_iv_inr(BigDecimal r57_qua_iv_inr) {
        this.r57_qua_iv_inr = r57_qua_iv_inr;
    }

    public BigDecimal getR57_cumm_inr() {
        return r57_cumm_inr;
    }

    public void setR57_cumm_inr(BigDecimal r57_cumm_inr) {
        this.r57_cumm_inr = r57_cumm_inr;
    }

    public BigDecimal getR57_cumm_bwp() {
        return r57_cumm_bwp;
    }

    public void setR57_cumm_bwp(BigDecimal r57_cumm_bwp) {
        this.r57_cumm_bwp = r57_cumm_bwp;
    }

    public String getR72_product() {
        return r72_product;
    }

    public void setR72_product(String r72_product) {
        this.r72_product = r72_product;
    }

    public BigDecimal getR72_qua_i_lc() {
        return r72_qua_i_lc;
    }

    public void setR72_qua_i_lc(BigDecimal r72_qua_i_lc) {
        this.r72_qua_i_lc = r72_qua_i_lc;
    }

    public BigDecimal getR72_qua_i_qar() {
        return r72_qua_i_qar;
    }

    public void setR72_qua_i_qar(BigDecimal r72_qua_i_qar) {
        this.r72_qua_i_qar = r72_qua_i_qar;
    }

    public BigDecimal getR72_qua_i_inr() {
        return r72_qua_i_inr;
    }

    public void setR72_qua_i_inr(BigDecimal r72_qua_i_inr) {
        this.r72_qua_i_inr = r72_qua_i_inr;
    }

    public BigDecimal getR72_qua_ii_lc() {
        return r72_qua_ii_lc;
    }

    public void setR72_qua_ii_lc(BigDecimal r72_qua_ii_lc) {
        this.r72_qua_ii_lc = r72_qua_ii_lc;
    }

    public BigDecimal getR72_qua_ii_qar() {
        return r72_qua_ii_qar;
    }

    public void setR72_qua_ii_qar(BigDecimal r72_qua_ii_qar) {
        this.r72_qua_ii_qar = r72_qua_ii_qar;
    }

    public BigDecimal getR72_qua_ii_inr() {
        return r72_qua_ii_inr;
    }

    public void setR72_qua_ii_inr(BigDecimal r72_qua_ii_inr) {
        this.r72_qua_ii_inr = r72_qua_ii_inr;
    }

    public BigDecimal getR72_qua_iii_lc() {
        return r72_qua_iii_lc;
    }

    public void setR72_qua_iii_lc(BigDecimal r72_qua_iii_lc) {
        this.r72_qua_iii_lc = r72_qua_iii_lc;
    }

    public BigDecimal getR72_qua_iii_qar() {
        return r72_qua_iii_qar;
    }

    public void setR72_qua_iii_qar(BigDecimal r72_qua_iii_qar) {
        this.r72_qua_iii_qar = r72_qua_iii_qar;
    }

    public BigDecimal getR72_qua_iii_inr() {
        return r72_qua_iii_inr;
    }

    public void setR72_qua_iii_inr(BigDecimal r72_qua_iii_inr) {
        this.r72_qua_iii_inr = r72_qua_iii_inr;
    }

    public BigDecimal getR72_qua_iv_lc() {
        return r72_qua_iv_lc;
    }

    public void setR72_qua_iv_lc(BigDecimal r72_qua_iv_lc) {
        this.r72_qua_iv_lc = r72_qua_iv_lc;
    }

    public BigDecimal getR72_qua_iv_qar() {
        return r72_qua_iv_qar;
    }

    public void setR72_qua_iv_qar(BigDecimal r72_qua_iv_qar) {
        this.r72_qua_iv_qar = r72_qua_iv_qar;
    }

    public BigDecimal getR72_qua_iv_inr() {
        return r72_qua_iv_inr;
    }

    public void setR72_qua_iv_inr(BigDecimal r72_qua_iv_inr) {
        this.r72_qua_iv_inr = r72_qua_iv_inr;
    }

    public BigDecimal getR72_cumm_inr() {
        return r72_cumm_inr;
    }

    public void setR72_cumm_inr(BigDecimal r72_cumm_inr) {
        this.r72_cumm_inr = r72_cumm_inr;
    }

    public BigDecimal getR72_cumm_bwp() {
        return r72_cumm_bwp;
    }

    public void setR72_cumm_bwp(BigDecimal r72_cumm_bwp) {
        this.r72_cumm_bwp = r72_cumm_bwp;
    }

    public String getR73_product() {
        return r73_product;
    }

    public void setR73_product(String r73_product) {
        this.r73_product = r73_product;
    }

    public BigDecimal getR73_qua_i_lc() {
        return r73_qua_i_lc;
    }

    public void setR73_qua_i_lc(BigDecimal r73_qua_i_lc) {
        this.r73_qua_i_lc = r73_qua_i_lc;
    }

    public BigDecimal getR73_qua_i_qar() {
        return r73_qua_i_qar;
    }

    public void setR73_qua_i_qar(BigDecimal r73_qua_i_qar) {
        this.r73_qua_i_qar = r73_qua_i_qar;
    }

    public BigDecimal getR73_qua_i_inr() {
        return r73_qua_i_inr;
    }

    public void setR73_qua_i_inr(BigDecimal r73_qua_i_inr) {
        this.r73_qua_i_inr = r73_qua_i_inr;
    }

    public BigDecimal getR73_qua_ii_lc() {
        return r73_qua_ii_lc;
    }

    public void setR73_qua_ii_lc(BigDecimal r73_qua_ii_lc) {
        this.r73_qua_ii_lc = r73_qua_ii_lc;
    }

    public BigDecimal getR73_qua_ii_qar() {
        return r73_qua_ii_qar;
    }

    public void setR73_qua_ii_qar(BigDecimal r73_qua_ii_qar) {
        this.r73_qua_ii_qar = r73_qua_ii_qar;
    }

    public BigDecimal getR73_qua_ii_inr() {
        return r73_qua_ii_inr;
    }

    public void setR73_qua_ii_inr(BigDecimal r73_qua_ii_inr) {
        this.r73_qua_ii_inr = r73_qua_ii_inr;
    }

    public BigDecimal getR73_qua_iii_lc() {
        return r73_qua_iii_lc;
    }

    public void setR73_qua_iii_lc(BigDecimal r73_qua_iii_lc) {
        this.r73_qua_iii_lc = r73_qua_iii_lc;
    }

    public BigDecimal getR73_qua_iii_qar() {
        return r73_qua_iii_qar;
    }

    public void setR73_qua_iii_qar(BigDecimal r73_qua_iii_qar) {
        this.r73_qua_iii_qar = r73_qua_iii_qar;
    }

    public BigDecimal getR73_qua_iii_inr() {
        return r73_qua_iii_inr;
    }

    public void setR73_qua_iii_inr(BigDecimal r73_qua_iii_inr) {
        this.r73_qua_iii_inr = r73_qua_iii_inr;
    }

    public BigDecimal getR73_qua_iv_lc() {
        return r73_qua_iv_lc;
    }

    public void setR73_qua_iv_lc(BigDecimal r73_qua_iv_lc) {
        this.r73_qua_iv_lc = r73_qua_iv_lc;
    }

    public BigDecimal getR73_qua_iv_qar() {
        return r73_qua_iv_qar;
    }

    public void setR73_qua_iv_qar(BigDecimal r73_qua_iv_qar) {
        this.r73_qua_iv_qar = r73_qua_iv_qar;
    }

    public BigDecimal getR73_qua_iv_inr() {
        return r73_qua_iv_inr;
    }

    public void setR73_qua_iv_inr(BigDecimal r73_qua_iv_inr) {
        this.r73_qua_iv_inr = r73_qua_iv_inr;
    }

    public BigDecimal getR73_cumm_inr() {
        return r73_cumm_inr;
    }

    public void setR73_cumm_inr(BigDecimal r73_cumm_inr) {
        this.r73_cumm_inr = r73_cumm_inr;
    }

    public BigDecimal getR73_cumm_bwp() {
        return r73_cumm_bwp;
    }

    public void setR73_cumm_bwp(BigDecimal r73_cumm_bwp) {
        this.r73_cumm_bwp = r73_cumm_bwp;
    }

    public String getR74_product() {
        return r74_product;
    }

    public void setR74_product(String r74_product) {
        this.r74_product = r74_product;
    }

    public BigDecimal getR74_qua_i_lc() {
        return r74_qua_i_lc;
    }

    public void setR74_qua_i_lc(BigDecimal r74_qua_i_lc) {
        this.r74_qua_i_lc = r74_qua_i_lc;
    }

    public BigDecimal getR74_qua_i_qar() {
        return r74_qua_i_qar;
    }

    public void setR74_qua_i_qar(BigDecimal r74_qua_i_qar) {
        this.r74_qua_i_qar = r74_qua_i_qar;
    }

    public BigDecimal getR74_qua_i_inr() {
        return r74_qua_i_inr;
    }

    public void setR74_qua_i_inr(BigDecimal r74_qua_i_inr) {
        this.r74_qua_i_inr = r74_qua_i_inr;
    }

    public BigDecimal getR74_qua_ii_lc() {
        return r74_qua_ii_lc;
    }

    public void setR74_qua_ii_lc(BigDecimal r74_qua_ii_lc) {
        this.r74_qua_ii_lc = r74_qua_ii_lc;
    }

    public BigDecimal getR74_qua_ii_qar() {
        return r74_qua_ii_qar;
    }

    public void setR74_qua_ii_qar(BigDecimal r74_qua_ii_qar) {
        this.r74_qua_ii_qar = r74_qua_ii_qar;
    }

    public BigDecimal getR74_qua_ii_inr() {
        return r74_qua_ii_inr;
    }

    public void setR74_qua_ii_inr(BigDecimal r74_qua_ii_inr) {
        this.r74_qua_ii_inr = r74_qua_ii_inr;
    }

    public BigDecimal getR74_qua_iii_lc() {
        return r74_qua_iii_lc;
    }

    public void setR74_qua_iii_lc(BigDecimal r74_qua_iii_lc) {
        this.r74_qua_iii_lc = r74_qua_iii_lc;
    }

    public BigDecimal getR74_qua_iii_qar() {
        return r74_qua_iii_qar;
    }

    public void setR74_qua_iii_qar(BigDecimal r74_qua_iii_qar) {
        this.r74_qua_iii_qar = r74_qua_iii_qar;
    }

    public BigDecimal getR74_qua_iii_inr() {
        return r74_qua_iii_inr;
    }

    public void setR74_qua_iii_inr(BigDecimal r74_qua_iii_inr) {
        this.r74_qua_iii_inr = r74_qua_iii_inr;
    }

    public BigDecimal getR74_qua_iv_lc() {
        return r74_qua_iv_lc;
    }

    public void setR74_qua_iv_lc(BigDecimal r74_qua_iv_lc) {
        this.r74_qua_iv_lc = r74_qua_iv_lc;
    }

    public BigDecimal getR74_qua_iv_qar() {
        return r74_qua_iv_qar;
    }

    public void setR74_qua_iv_qar(BigDecimal r74_qua_iv_qar) {
        this.r74_qua_iv_qar = r74_qua_iv_qar;
    }

    public BigDecimal getR74_qua_iv_inr() {
        return r74_qua_iv_inr;
    }

    public void setR74_qua_iv_inr(BigDecimal r74_qua_iv_inr) {
        this.r74_qua_iv_inr = r74_qua_iv_inr;
    }

    public BigDecimal getR74_cumm_inr() {
        return r74_cumm_inr;
    }

    public void setR74_cumm_inr(BigDecimal r74_cumm_inr) {
        this.r74_cumm_inr = r74_cumm_inr;
    }

    public BigDecimal getR74_cumm_bwp() {
        return r74_cumm_bwp;
    }

    public void setR74_cumm_bwp(BigDecimal r74_cumm_bwp) {
        this.r74_cumm_bwp = r74_cumm_bwp;
    }

    public String getR76_product() {
        return r76_product;
    }

    public void setR76_product(String r76_product) {
        this.r76_product = r76_product;
    }

    public BigDecimal getR76_qua_i_lc() {
        return r76_qua_i_lc;
    }

    public void setR76_qua_i_lc(BigDecimal r76_qua_i_lc) {
        this.r76_qua_i_lc = r76_qua_i_lc;
    }

    public BigDecimal getR76_qua_i_qar() {
        return r76_qua_i_qar;
    }

    public void setR76_qua_i_qar(BigDecimal r76_qua_i_qar) {
        this.r76_qua_i_qar = r76_qua_i_qar;
    }

    public BigDecimal getR76_qua_i_inr() {
        return r76_qua_i_inr;
    }

    public void setR76_qua_i_inr(BigDecimal r76_qua_i_inr) {
        this.r76_qua_i_inr = r76_qua_i_inr;
    }

    public BigDecimal getR76_qua_ii_lc() {
        return r76_qua_ii_lc;
    }

    public void setR76_qua_ii_lc(BigDecimal r76_qua_ii_lc) {
        this.r76_qua_ii_lc = r76_qua_ii_lc;
    }

    public BigDecimal getR76_qua_ii_qar() {
        return r76_qua_ii_qar;
    }

    public void setR76_qua_ii_qar(BigDecimal r76_qua_ii_qar) {
        this.r76_qua_ii_qar = r76_qua_ii_qar;
    }

    public BigDecimal getR76_qua_ii_inr() {
        return r76_qua_ii_inr;
    }

    public void setR76_qua_ii_inr(BigDecimal r76_qua_ii_inr) {
        this.r76_qua_ii_inr = r76_qua_ii_inr;
    }

    public BigDecimal getR76_qua_iii_lc() {
        return r76_qua_iii_lc;
    }

    public void setR76_qua_iii_lc(BigDecimal r76_qua_iii_lc) {
        this.r76_qua_iii_lc = r76_qua_iii_lc;
    }

    public BigDecimal getR76_qua_iii_qar() {
        return r76_qua_iii_qar;
    }

    public void setR76_qua_iii_qar(BigDecimal r76_qua_iii_qar) {
        this.r76_qua_iii_qar = r76_qua_iii_qar;
    }

    public BigDecimal getR76_qua_iii_inr() {
        return r76_qua_iii_inr;
    }

    public void setR76_qua_iii_inr(BigDecimal r76_qua_iii_inr) {
        this.r76_qua_iii_inr = r76_qua_iii_inr;
    }

    public BigDecimal getR76_qua_iv_lc() {
        return r76_qua_iv_lc;
    }

    public void setR76_qua_iv_lc(BigDecimal r76_qua_iv_lc) {
        this.r76_qua_iv_lc = r76_qua_iv_lc;
    }

    public BigDecimal getR76_qua_iv_qar() {
        return r76_qua_iv_qar;
    }

    public void setR76_qua_iv_qar(BigDecimal r76_qua_iv_qar) {
        this.r76_qua_iv_qar = r76_qua_iv_qar;
    }

    public BigDecimal getR76_qua_iv_inr() {
        return r76_qua_iv_inr;
    }

    public void setR76_qua_iv_inr(BigDecimal r76_qua_iv_inr) {
        this.r76_qua_iv_inr = r76_qua_iv_inr;
    }

    public BigDecimal getR76_cumm_inr() {
        return r76_cumm_inr;
    }

    public void setR76_cumm_inr(BigDecimal r76_cumm_inr) {
        this.r76_cumm_inr = r76_cumm_inr;
    }

    public BigDecimal getR76_cumm_bwp() {
        return r76_cumm_bwp;
    }

    public void setR76_cumm_bwp(BigDecimal r76_cumm_bwp) {
        this.r76_cumm_bwp = r76_cumm_bwp;
    }

    public String getR77_product() {
        return r77_product;
    }

    public void setR77_product(String r77_product) {
        this.r77_product = r77_product;
    }

    public BigDecimal getR77_qua_i_lc() {
        return r77_qua_i_lc;
    }

    public void setR77_qua_i_lc(BigDecimal r77_qua_i_lc) {
        this.r77_qua_i_lc = r77_qua_i_lc;
    }

    public BigDecimal getR77_qua_i_qar() {
        return r77_qua_i_qar;
    }

    public void setR77_qua_i_qar(BigDecimal r77_qua_i_qar) {
        this.r77_qua_i_qar = r77_qua_i_qar;
    }

    public BigDecimal getR77_qua_i_inr() {
        return r77_qua_i_inr;
    }

    public void setR77_qua_i_inr(BigDecimal r77_qua_i_inr) {
        this.r77_qua_i_inr = r77_qua_i_inr;
    }

    public BigDecimal getR77_qua_ii_lc() {
        return r77_qua_ii_lc;
    }

    public void setR77_qua_ii_lc(BigDecimal r77_qua_ii_lc) {
        this.r77_qua_ii_lc = r77_qua_ii_lc;
    }

    public BigDecimal getR77_qua_ii_qar() {
        return r77_qua_ii_qar;
    }

    public void setR77_qua_ii_qar(BigDecimal r77_qua_ii_qar) {
        this.r77_qua_ii_qar = r77_qua_ii_qar;
    }

    public BigDecimal getR77_qua_ii_inr() {
        return r77_qua_ii_inr;
    }

    public void setR77_qua_ii_inr(BigDecimal r77_qua_ii_inr) {
        this.r77_qua_ii_inr = r77_qua_ii_inr;
    }

    public BigDecimal getR77_qua_iii_lc() {
        return r77_qua_iii_lc;
    }

    public void setR77_qua_iii_lc(BigDecimal r77_qua_iii_lc) {
        this.r77_qua_iii_lc = r77_qua_iii_lc;
    }

    public BigDecimal getR77_qua_iii_qar() {
        return r77_qua_iii_qar;
    }

    public void setR77_qua_iii_qar(BigDecimal r77_qua_iii_qar) {
        this.r77_qua_iii_qar = r77_qua_iii_qar;
    }

    public BigDecimal getR77_qua_iii_inr() {
        return r77_qua_iii_inr;
    }

    public void setR77_qua_iii_inr(BigDecimal r77_qua_iii_inr) {
        this.r77_qua_iii_inr = r77_qua_iii_inr;
    }

    public BigDecimal getR77_qua_iv_lc() {
        return r77_qua_iv_lc;
    }

    public void setR77_qua_iv_lc(BigDecimal r77_qua_iv_lc) {
        this.r77_qua_iv_lc = r77_qua_iv_lc;
    }

    public BigDecimal getR77_qua_iv_qar() {
        return r77_qua_iv_qar;
    }

    public void setR77_qua_iv_qar(BigDecimal r77_qua_iv_qar) {
        this.r77_qua_iv_qar = r77_qua_iv_qar;
    }

    public BigDecimal getR77_qua_iv_inr() {
        return r77_qua_iv_inr;
    }

    public void setR77_qua_iv_inr(BigDecimal r77_qua_iv_inr) {
        this.r77_qua_iv_inr = r77_qua_iv_inr;
    }

    public BigDecimal getR77_cumm_inr() {
        return r77_cumm_inr;
    }

    public void setR77_cumm_inr(BigDecimal r77_cumm_inr) {
        this.r77_cumm_inr = r77_cumm_inr;
    }

    public BigDecimal getR77_cumm_bwp() {
        return r77_cumm_bwp;
    }

    public void setR77_cumm_bwp(BigDecimal r77_cumm_bwp) {
        this.r77_cumm_bwp = r77_cumm_bwp;
    }

    public String getR78_product() {
        return r78_product;
    }

    public void setR78_product(String r78_product) {
        this.r78_product = r78_product;
    }

    public BigDecimal getR78_qua_i_lc() {
        return r78_qua_i_lc;
    }

    public void setR78_qua_i_lc(BigDecimal r78_qua_i_lc) {
        this.r78_qua_i_lc = r78_qua_i_lc;
    }

    public BigDecimal getR78_qua_i_qar() {
        return r78_qua_i_qar;
    }

    public void setR78_qua_i_qar(BigDecimal r78_qua_i_qar) {
        this.r78_qua_i_qar = r78_qua_i_qar;
    }

    public BigDecimal getR78_qua_i_inr() {
        return r78_qua_i_inr;
    }

    public void setR78_qua_i_inr(BigDecimal r78_qua_i_inr) {
        this.r78_qua_i_inr = r78_qua_i_inr;
    }

    public BigDecimal getR78_qua_ii_lc() {
        return r78_qua_ii_lc;
    }

    public void setR78_qua_ii_lc(BigDecimal r78_qua_ii_lc) {
        this.r78_qua_ii_lc = r78_qua_ii_lc;
    }

    public BigDecimal getR78_qua_ii_qar() {
        return r78_qua_ii_qar;
    }

    public void setR78_qua_ii_qar(BigDecimal r78_qua_ii_qar) {
        this.r78_qua_ii_qar = r78_qua_ii_qar;
    }

    public BigDecimal getR78_qua_ii_inr() {
        return r78_qua_ii_inr;
    }

    public void setR78_qua_ii_inr(BigDecimal r78_qua_ii_inr) {
        this.r78_qua_ii_inr = r78_qua_ii_inr;
    }

    public BigDecimal getR78_qua_iii_lc() {
        return r78_qua_iii_lc;
    }

    public void setR78_qua_iii_lc(BigDecimal r78_qua_iii_lc) {
        this.r78_qua_iii_lc = r78_qua_iii_lc;
    }

    public BigDecimal getR78_qua_iii_qar() {
        return r78_qua_iii_qar;
    }

    public void setR78_qua_iii_qar(BigDecimal r78_qua_iii_qar) {
        this.r78_qua_iii_qar = r78_qua_iii_qar;
    }

    public BigDecimal getR78_qua_iii_inr() {
        return r78_qua_iii_inr;
    }

    public void setR78_qua_iii_inr(BigDecimal r78_qua_iii_inr) {
        this.r78_qua_iii_inr = r78_qua_iii_inr;
    }

    public BigDecimal getR78_qua_iv_lc() {
        return r78_qua_iv_lc;
    }

    public void setR78_qua_iv_lc(BigDecimal r78_qua_iv_lc) {
        this.r78_qua_iv_lc = r78_qua_iv_lc;
    }

    public BigDecimal getR78_qua_iv_qar() {
        return r78_qua_iv_qar;
    }

    public void setR78_qua_iv_qar(BigDecimal r78_qua_iv_qar) {
        this.r78_qua_iv_qar = r78_qua_iv_qar;
    }

    public BigDecimal getR78_qua_iv_inr() {
        return r78_qua_iv_inr;
    }

    public void setR78_qua_iv_inr(BigDecimal r78_qua_iv_inr) {
        this.r78_qua_iv_inr = r78_qua_iv_inr;
    }

    public BigDecimal getR78_cumm_inr() {
        return r78_cumm_inr;
    }

    public void setR78_cumm_inr(BigDecimal r78_cumm_inr) {
        this.r78_cumm_inr = r78_cumm_inr;
    }

    public BigDecimal getR78_cumm_bwp() {
        return r78_cumm_bwp;
    }

    public void setR78_cumm_bwp(BigDecimal r78_cumm_bwp) {
        this.r78_cumm_bwp = r78_cumm_bwp;
    }

    public String getR79_product() {
        return r79_product;
    }

    public void setR79_product(String r79_product) {
        this.r79_product = r79_product;
    }

    public BigDecimal getR79_qua_i_lc() {
        return r79_qua_i_lc;
    }

    public void setR79_qua_i_lc(BigDecimal r79_qua_i_lc) {
        this.r79_qua_i_lc = r79_qua_i_lc;
    }

    public BigDecimal getR79_qua_i_qar() {
        return r79_qua_i_qar;
    }

    public void setR79_qua_i_qar(BigDecimal r79_qua_i_qar) {
        this.r79_qua_i_qar = r79_qua_i_qar;
    }

    public BigDecimal getR79_qua_i_inr() {
        return r79_qua_i_inr;
    }

    public void setR79_qua_i_inr(BigDecimal r79_qua_i_inr) {
        this.r79_qua_i_inr = r79_qua_i_inr;
    }

    public BigDecimal getR79_qua_ii_lc() {
        return r79_qua_ii_lc;
    }

    public void setR79_qua_ii_lc(BigDecimal r79_qua_ii_lc) {
        this.r79_qua_ii_lc = r79_qua_ii_lc;
    }

    public BigDecimal getR79_qua_ii_qar() {
        return r79_qua_ii_qar;
    }

    public void setR79_qua_ii_qar(BigDecimal r79_qua_ii_qar) {
        this.r79_qua_ii_qar = r79_qua_ii_qar;
    }

    public BigDecimal getR79_qua_ii_inr() {
        return r79_qua_ii_inr;
    }

    public void setR79_qua_ii_inr(BigDecimal r79_qua_ii_inr) {
        this.r79_qua_ii_inr = r79_qua_ii_inr;
    }

    public BigDecimal getR79_qua_iii_lc() {
        return r79_qua_iii_lc;
    }

    public void setR79_qua_iii_lc(BigDecimal r79_qua_iii_lc) {
        this.r79_qua_iii_lc = r79_qua_iii_lc;
    }

    public BigDecimal getR79_qua_iii_qar() {
        return r79_qua_iii_qar;
    }

    public void setR79_qua_iii_qar(BigDecimal r79_qua_iii_qar) {
        this.r79_qua_iii_qar = r79_qua_iii_qar;
    }

    public BigDecimal getR79_qua_iii_inr() {
        return r79_qua_iii_inr;
    }

    public void setR79_qua_iii_inr(BigDecimal r79_qua_iii_inr) {
        this.r79_qua_iii_inr = r79_qua_iii_inr;
    }

    public BigDecimal getR79_qua_iv_lc() {
        return r79_qua_iv_lc;
    }

    public void setR79_qua_iv_lc(BigDecimal r79_qua_iv_lc) {
        this.r79_qua_iv_lc = r79_qua_iv_lc;
    }

    public BigDecimal getR79_qua_iv_qar() {
        return r79_qua_iv_qar;
    }

    public void setR79_qua_iv_qar(BigDecimal r79_qua_iv_qar) {
        this.r79_qua_iv_qar = r79_qua_iv_qar;
    }

    public BigDecimal getR79_qua_iv_inr() {
        return r79_qua_iv_inr;
    }

    public void setR79_qua_iv_inr(BigDecimal r79_qua_iv_inr) {
        this.r79_qua_iv_inr = r79_qua_iv_inr;
    }

    public BigDecimal getR79_cumm_inr() {
        return r79_cumm_inr;
    }

    public void setR79_cumm_inr(BigDecimal r79_cumm_inr) {
        this.r79_cumm_inr = r79_cumm_inr;
    }

    public BigDecimal getR79_cumm_bwp() {
        return r79_cumm_bwp;
    }

    public void setR79_cumm_bwp(BigDecimal r79_cumm_bwp) {
        this.r79_cumm_bwp = r79_cumm_bwp;
    }

    public String getR80_product() {
        return r80_product;
    }

    public void setR80_product(String r80_product) {
        this.r80_product = r80_product;
    }

    public BigDecimal getR80_qua_i_lc() {
        return r80_qua_i_lc;
    }

    public void setR80_qua_i_lc(BigDecimal r80_qua_i_lc) {
        this.r80_qua_i_lc = r80_qua_i_lc;
    }

    public BigDecimal getR80_qua_i_qar() {
        return r80_qua_i_qar;
    }

    public void setR80_qua_i_qar(BigDecimal r80_qua_i_qar) {
        this.r80_qua_i_qar = r80_qua_i_qar;
    }

    public BigDecimal getR80_qua_i_inr() {
        return r80_qua_i_inr;
    }

    public void setR80_qua_i_inr(BigDecimal r80_qua_i_inr) {
        this.r80_qua_i_inr = r80_qua_i_inr;
    }

    public BigDecimal getR80_qua_ii_lc() {
        return r80_qua_ii_lc;
    }

    public void setR80_qua_ii_lc(BigDecimal r80_qua_ii_lc) {
        this.r80_qua_ii_lc = r80_qua_ii_lc;
    }

    public BigDecimal getR80_qua_ii_qar() {
        return r80_qua_ii_qar;
    }

    public void setR80_qua_ii_qar(BigDecimal r80_qua_ii_qar) {
        this.r80_qua_ii_qar = r80_qua_ii_qar;
    }

    public BigDecimal getR80_qua_ii_inr() {
        return r80_qua_ii_inr;
    }

    public void setR80_qua_ii_inr(BigDecimal r80_qua_ii_inr) {
        this.r80_qua_ii_inr = r80_qua_ii_inr;
    }

    public BigDecimal getR80_qua_iii_lc() {
        return r80_qua_iii_lc;
    }

    public void setR80_qua_iii_lc(BigDecimal r80_qua_iii_lc) {
        this.r80_qua_iii_lc = r80_qua_iii_lc;
    }

    public BigDecimal getR80_qua_iii_qar() {
        return r80_qua_iii_qar;
    }

    public void setR80_qua_iii_qar(BigDecimal r80_qua_iii_qar) {
        this.r80_qua_iii_qar = r80_qua_iii_qar;
    }

    public BigDecimal getR80_qua_iii_inr() {
        return r80_qua_iii_inr;
    }

    public void setR80_qua_iii_inr(BigDecimal r80_qua_iii_inr) {
        this.r80_qua_iii_inr = r80_qua_iii_inr;
    }

    public BigDecimal getR80_qua_iv_lc() {
        return r80_qua_iv_lc;
    }

    public void setR80_qua_iv_lc(BigDecimal r80_qua_iv_lc) {
        this.r80_qua_iv_lc = r80_qua_iv_lc;
    }

    public BigDecimal getR80_qua_iv_qar() {
        return r80_qua_iv_qar;
    }

    public void setR80_qua_iv_qar(BigDecimal r80_qua_iv_qar) {
        this.r80_qua_iv_qar = r80_qua_iv_qar;
    }

    public BigDecimal getR80_qua_iv_inr() {
        return r80_qua_iv_inr;
    }

    public void setR80_qua_iv_inr(BigDecimal r80_qua_iv_inr) {
        this.r80_qua_iv_inr = r80_qua_iv_inr;
    }

    public BigDecimal getR80_cumm_inr() {
        return r80_cumm_inr;
    }

    public void setR80_cumm_inr(BigDecimal r80_cumm_inr) {
        this.r80_cumm_inr = r80_cumm_inr;
    }

    public BigDecimal getR80_cumm_bwp() {
        return r80_cumm_bwp;
    }

    public void setR80_cumm_bwp(BigDecimal r80_cumm_bwp) {
        this.r80_cumm_bwp = r80_cumm_bwp;
    }

    public String getR81_product() {
        return r81_product;
    }

    public void setR81_product(String r81_product) {
        this.r81_product = r81_product;
    }

    public BigDecimal getR81_qua_i_lc() {
        return r81_qua_i_lc;
    }

    public void setR81_qua_i_lc(BigDecimal r81_qua_i_lc) {
        this.r81_qua_i_lc = r81_qua_i_lc;
    }

    public BigDecimal getR81_qua_i_qar() {
        return r81_qua_i_qar;
    }

    public void setR81_qua_i_qar(BigDecimal r81_qua_i_qar) {
        this.r81_qua_i_qar = r81_qua_i_qar;
    }

    public BigDecimal getR81_qua_i_inr() {
        return r81_qua_i_inr;
    }

    public void setR81_qua_i_inr(BigDecimal r81_qua_i_inr) {
        this.r81_qua_i_inr = r81_qua_i_inr;
    }

    public BigDecimal getR81_qua_ii_lc() {
        return r81_qua_ii_lc;
    }

    public void setR81_qua_ii_lc(BigDecimal r81_qua_ii_lc) {
        this.r81_qua_ii_lc = r81_qua_ii_lc;
    }

    public BigDecimal getR81_qua_ii_qar() {
        return r81_qua_ii_qar;
    }

    public void setR81_qua_ii_qar(BigDecimal r81_qua_ii_qar) {
        this.r81_qua_ii_qar = r81_qua_ii_qar;
    }

    public BigDecimal getR81_qua_ii_inr() {
        return r81_qua_ii_inr;
    }

    public void setR81_qua_ii_inr(BigDecimal r81_qua_ii_inr) {
        this.r81_qua_ii_inr = r81_qua_ii_inr;
    }

    public BigDecimal getR81_qua_iii_lc() {
        return r81_qua_iii_lc;
    }

    public void setR81_qua_iii_lc(BigDecimal r81_qua_iii_lc) {
        this.r81_qua_iii_lc = r81_qua_iii_lc;
    }

    public BigDecimal getR81_qua_iii_qar() {
        return r81_qua_iii_qar;
    }

    public void setR81_qua_iii_qar(BigDecimal r81_qua_iii_qar) {
        this.r81_qua_iii_qar = r81_qua_iii_qar;
    }

    public BigDecimal getR81_qua_iii_inr() {
        return r81_qua_iii_inr;
    }

    public void setR81_qua_iii_inr(BigDecimal r81_qua_iii_inr) {
        this.r81_qua_iii_inr = r81_qua_iii_inr;
    }

    public BigDecimal getR81_qua_iv_lc() {
        return r81_qua_iv_lc;
    }

    public void setR81_qua_iv_lc(BigDecimal r81_qua_iv_lc) {
        this.r81_qua_iv_lc = r81_qua_iv_lc;
    }

    public BigDecimal getR81_qua_iv_qar() {
        return r81_qua_iv_qar;
    }

    public void setR81_qua_iv_qar(BigDecimal r81_qua_iv_qar) {
        this.r81_qua_iv_qar = r81_qua_iv_qar;
    }

    public BigDecimal getR81_qua_iv_inr() {
        return r81_qua_iv_inr;
    }

    public void setR81_qua_iv_inr(BigDecimal r81_qua_iv_inr) {
        this.r81_qua_iv_inr = r81_qua_iv_inr;
    }

    public BigDecimal getR81_cumm_inr() {
        return r81_cumm_inr;
    }

    public void setR81_cumm_inr(BigDecimal r81_cumm_inr) {
        this.r81_cumm_inr = r81_cumm_inr;
    }

    public BigDecimal getR81_cumm_bwp() {
        return r81_cumm_bwp;
    }

    public void setR81_cumm_bwp(BigDecimal r81_cumm_bwp) {
        this.r81_cumm_bwp = r81_cumm_bwp;
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

    public AS_11_Manual_Archival_Summary_Entity() {
        super();
    }

    

}
