package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "BRRS_OFF_BS_ITEMS_ARCHIVALTABLE_SUMMARY2")
@IdClass(OFF_BS_ITEMS_PK.class)

public class OFF_BS_ITEMS_Archival_Summary_Entity2 {
	
	
	private String	r151_product;
	private String	r151_client_grp;
	private BigDecimal	r151_total_book_expo;
	private BigDecimal	r151_margin_pro;
	private BigDecimal	r151_book_expo;
	private BigDecimal	r151_ccf_cont;
	private BigDecimal	r151_equiv_value;
	private BigDecimal	r151_rw_obligant;
	private BigDecimal	r151_rav;
	private String	r152_product;
	private String	r152_client_grp;
	private BigDecimal	r152_total_book_expo;
	private BigDecimal	r152_margin_pro;
	private BigDecimal	r152_book_expo;
	private BigDecimal	r152_ccf_cont;
	private BigDecimal	r152_equiv_value;
	private BigDecimal	r152_rw_obligant;
	private BigDecimal	r152_rav;
	private String	r153_product;
	private String	r153_client_grp;
	private BigDecimal	r153_total_book_expo;
	private BigDecimal	r153_margin_pro;
	private BigDecimal	r153_book_expo;
	private BigDecimal	r153_ccf_cont;
	private BigDecimal	r153_equiv_value;
	private BigDecimal	r153_rw_obligant;
	private BigDecimal	r153_rav;
	private String	r154_product;
	private String	r154_client_grp;
	private BigDecimal	r154_total_book_expo;
	private BigDecimal	r154_margin_pro;
	private BigDecimal	r154_book_expo;
	private BigDecimal	r154_ccf_cont;
	private BigDecimal	r154_equiv_value;
	private BigDecimal	r154_rw_obligant;
	private BigDecimal	r154_rav;
	private String	r155_product;
	private String	r155_client_grp;
	private BigDecimal	r155_total_book_expo;
	private BigDecimal	r155_margin_pro;
	private BigDecimal	r155_book_expo;
	private BigDecimal	r155_ccf_cont;
	private BigDecimal	r155_equiv_value;
	private BigDecimal	r155_rw_obligant;
	private BigDecimal	r155_rav;
	private String	r156_product;
	private String	r156_client_grp;
	private BigDecimal	r156_total_book_expo;
	private BigDecimal	r156_margin_pro;
	private BigDecimal	r156_book_expo;
	private BigDecimal	r156_ccf_cont;
	private BigDecimal	r156_equiv_value;
	private BigDecimal	r156_rw_obligant;
	private BigDecimal	r156_rav;
	private String	r157_product;
	private String	r157_client_grp;
	private BigDecimal	r157_total_book_expo;
	private BigDecimal	r157_margin_pro;
	private BigDecimal	r157_book_expo;
	private BigDecimal	r157_ccf_cont;
	private BigDecimal	r157_equiv_value;
	private BigDecimal	r157_rw_obligant;
	private BigDecimal	r157_rav;
	private String	r158_product;
	private String	r158_client_grp;
	private BigDecimal	r158_total_book_expo;
	private BigDecimal	r158_margin_pro;
	private BigDecimal	r158_book_expo;
	private BigDecimal	r158_ccf_cont;
	private BigDecimal	r158_equiv_value;
	private BigDecimal	r158_rw_obligant;
	private BigDecimal	r158_rav;
	private String	r159_product;
	private String	r159_client_grp;
	private BigDecimal	r159_total_book_expo;
	private BigDecimal	r159_margin_pro;
	private BigDecimal	r159_book_expo;
	private BigDecimal	r159_ccf_cont;
	private BigDecimal	r159_equiv_value;
	private BigDecimal	r159_rw_obligant;
	private BigDecimal	r159_rav;
	private String	r160_product;
	private String	r160_client_grp;
	private BigDecimal	r160_total_book_expo;
	private BigDecimal	r160_margin_pro;
	private BigDecimal	r160_book_expo;
	private BigDecimal	r160_ccf_cont;
	private BigDecimal	r160_equiv_value;
	private BigDecimal	r160_rw_obligant;
	private BigDecimal	r160_rav;
	private String	r161_product;
	private String	r161_client_grp;
	private BigDecimal	r161_total_book_expo;
	private BigDecimal	r161_margin_pro;
	private BigDecimal	r161_book_expo;
	private BigDecimal	r161_ccf_cont;
	private BigDecimal	r161_equiv_value;
	private BigDecimal	r161_rw_obligant;
	private BigDecimal	r161_rav;
	private String	r162_product;
	private String	r162_client_grp;
	private BigDecimal	r162_total_book_expo;
	private BigDecimal	r162_margin_pro;
	private BigDecimal	r162_book_expo;
	private BigDecimal	r162_ccf_cont;
	private BigDecimal	r162_equiv_value;
	private BigDecimal	r162_rw_obligant;
	private BigDecimal	r162_rav;
	private String	r163_product;
	private String	r163_client_grp;
	private BigDecimal	r163_total_book_expo;
	private BigDecimal	r163_margin_pro;
	private BigDecimal	r163_book_expo;
	private BigDecimal	r163_ccf_cont;
	private BigDecimal	r163_equiv_value;
	private BigDecimal	r163_rw_obligant;
	private BigDecimal	r163_rav;
	private String	r164_product;
	private String	r164_client_grp;
	private BigDecimal	r164_total_book_expo;
	private BigDecimal	r164_margin_pro;
	private BigDecimal	r164_book_expo;
	private BigDecimal	r164_ccf_cont;
	private BigDecimal	r164_equiv_value;
	private BigDecimal	r164_rw_obligant;
	private BigDecimal	r164_rav;
	private String	r165_product;
	private String	r165_client_grp;
	private BigDecimal	r165_total_book_expo;
	private BigDecimal	r165_margin_pro;
	private BigDecimal	r165_book_expo;
	private BigDecimal	r165_ccf_cont;
	private BigDecimal	r165_equiv_value;
	private BigDecimal	r165_rw_obligant;
	private BigDecimal	r165_rav;
	private String	r166_product;
	private String	r166_client_grp;
	private BigDecimal	r166_total_book_expo;
	private BigDecimal	r166_margin_pro;
	private BigDecimal	r166_book_expo;
	private BigDecimal	r166_ccf_cont;
	private BigDecimal	r166_equiv_value;
	private BigDecimal	r166_rw_obligant;
	private BigDecimal	r166_rav;
	private String	r167_product;
	private String	r167_client_grp;
	private BigDecimal	r167_total_book_expo;
	private BigDecimal	r167_margin_pro;
	private BigDecimal	r167_book_expo;
	private BigDecimal	r167_ccf_cont;
	private BigDecimal	r167_equiv_value;
	private BigDecimal	r167_rw_obligant;
	private BigDecimal	r167_rav;
	private String	r168_product;
	private String	r168_client_grp;
	private BigDecimal	r168_total_book_expo;
	private BigDecimal	r168_margin_pro;
	private BigDecimal	r168_book_expo;
	private BigDecimal	r168_ccf_cont;
	private BigDecimal	r168_equiv_value;
	private BigDecimal	r168_rw_obligant;
	private BigDecimal	r168_rav;
	private String	r169_product;
	private String	r169_client_grp;
	private BigDecimal	r169_total_book_expo;
	private BigDecimal	r169_margin_pro;
	private BigDecimal	r169_book_expo;
	private BigDecimal	r169_ccf_cont;
	private BigDecimal	r169_equiv_value;
	private BigDecimal	r169_rw_obligant;
	private BigDecimal	r169_rav;
	private String	r170_product;
	private String	r170_client_grp;
	private BigDecimal	r170_total_book_expo;
	private BigDecimal	r170_margin_pro;
	private BigDecimal	r170_book_expo;
	private BigDecimal	r170_ccf_cont;
	private BigDecimal	r170_equiv_value;
	private BigDecimal	r170_rw_obligant;
	private BigDecimal	r170_rav;
	private String	r171_product;
	private String	r171_client_grp;
	private BigDecimal	r171_total_book_expo;
	private BigDecimal	r171_margin_pro;
	private BigDecimal	r171_book_expo;
	private BigDecimal	r171_ccf_cont;
	private BigDecimal	r171_equiv_value;
	private BigDecimal	r171_rw_obligant;
	private BigDecimal	r171_rav;
	private String	r172_product;
	private String	r172_client_grp;
	private BigDecimal	r172_total_book_expo;
	private BigDecimal	r172_margin_pro;
	private BigDecimal	r172_book_expo;
	private BigDecimal	r172_ccf_cont;
	private BigDecimal	r172_equiv_value;
	private BigDecimal	r172_rw_obligant;
	private BigDecimal	r172_rav;
	private String	r173_product;
	private String	r173_client_grp;
	private BigDecimal	r173_total_book_expo;
	private BigDecimal	r173_margin_pro;
	private BigDecimal	r173_book_expo;
	private BigDecimal	r173_ccf_cont;
	private BigDecimal	r173_equiv_value;
	private BigDecimal	r173_rw_obligant;
	private BigDecimal	r173_rav;
	private String	r174_product;
	private String	r174_client_grp;
	private BigDecimal	r174_total_book_expo;
	private BigDecimal	r174_margin_pro;
	private BigDecimal	r174_book_expo;
	private BigDecimal	r174_ccf_cont;
	private BigDecimal	r174_equiv_value;
	private BigDecimal	r174_rw_obligant;
	private BigDecimal	r174_rav;
	private String	r175_product;
	private String	r175_client_grp;
	private BigDecimal	r175_total_book_expo;
	private BigDecimal	r175_margin_pro;
	private BigDecimal	r175_book_expo;
	private BigDecimal	r175_ccf_cont;
	private BigDecimal	r175_equiv_value;
	private BigDecimal	r175_rw_obligant;
	private BigDecimal	r175_rav;
	private String	r176_product;
	private String	r176_client_grp;
	private BigDecimal	r176_total_book_expo;
	private BigDecimal	r176_margin_pro;
	private BigDecimal	r176_book_expo;
	private BigDecimal	r176_ccf_cont;
	private BigDecimal	r176_equiv_value;
	private BigDecimal	r176_rw_obligant;
	private BigDecimal	r176_rav;
	private String	r177_product;
	private String	r177_client_grp;
	private BigDecimal	r177_total_book_expo;
	private BigDecimal	r177_margin_pro;
	private BigDecimal	r177_book_expo;
	private BigDecimal	r177_ccf_cont;
	private BigDecimal	r177_equiv_value;
	private BigDecimal	r177_rw_obligant;
	private BigDecimal	r177_rav;
	private String	r178_product;
	private String	r178_client_grp;
	private BigDecimal	r178_total_book_expo;
	private BigDecimal	r178_margin_pro;
	private BigDecimal	r178_book_expo;
	private BigDecimal	r178_ccf_cont;
	private BigDecimal	r178_equiv_value;
	private BigDecimal	r178_rw_obligant;
	private BigDecimal	r178_rav;
	private String	r179_product;
	private String	r179_client_grp;
	private BigDecimal	r179_total_book_expo;
	private BigDecimal	r179_margin_pro;
	private BigDecimal	r179_book_expo;
	private BigDecimal	r179_ccf_cont;
	private BigDecimal	r179_equiv_value;
	private BigDecimal	r179_rw_obligant;
	private BigDecimal	r179_rav;
	private String	r180_product;
	private String	r180_client_grp;
	private BigDecimal	r180_total_book_expo;
	private BigDecimal	r180_margin_pro;
	private BigDecimal	r180_book_expo;
	private BigDecimal	r180_ccf_cont;
	private BigDecimal	r180_equiv_value;
	private BigDecimal	r180_rw_obligant;
	private BigDecimal	r180_rav;
	private String	r181_product;
	private String	r181_client_grp;
	private BigDecimal	r181_total_book_expo;
	private BigDecimal	r181_margin_pro;
	private BigDecimal	r181_book_expo;
	private BigDecimal	r181_ccf_cont;
	private BigDecimal	r181_equiv_value;
	private BigDecimal	r181_rw_obligant;
	private BigDecimal	r181_rav;
	private String	r182_product;
	private String	r182_client_grp;
	private BigDecimal	r182_total_book_expo;
	private BigDecimal	r182_margin_pro;
	private BigDecimal	r182_book_expo;
	private BigDecimal	r182_ccf_cont;
	private BigDecimal	r182_equiv_value;
	private BigDecimal	r182_rw_obligant;
	private BigDecimal	r182_rav;
	private String	r183_product;
	private String	r183_client_grp;
	private BigDecimal	r183_total_book_expo;
	private BigDecimal	r183_margin_pro;
	private BigDecimal	r183_book_expo;
	private BigDecimal	r183_ccf_cont;
	private BigDecimal	r183_equiv_value;
	private BigDecimal	r183_rw_obligant;
	private BigDecimal	r183_rav;
	private String	r184_product;
	private String	r184_client_grp;
	private BigDecimal	r184_total_book_expo;
	private BigDecimal	r184_margin_pro;
	private BigDecimal	r184_book_expo;
	private BigDecimal	r184_ccf_cont;
	private BigDecimal	r184_equiv_value;
	private BigDecimal	r184_rw_obligant;
	private BigDecimal	r184_rav;
	private String	r185_product;
	private String	r185_client_grp;
	private BigDecimal	r185_total_book_expo;
	private BigDecimal	r185_margin_pro;
	private BigDecimal	r185_book_expo;
	private BigDecimal	r185_ccf_cont;
	private BigDecimal	r185_equiv_value;
	private BigDecimal	r185_rw_obligant;
	private BigDecimal	r185_rav;
	private String	r186_product;
	private String	r186_client_grp;
	private BigDecimal	r186_total_book_expo;
	private BigDecimal	r186_margin_pro;
	private BigDecimal	r186_book_expo;
	private BigDecimal	r186_ccf_cont;
	private BigDecimal	r186_equiv_value;
	private BigDecimal	r186_rw_obligant;
	private BigDecimal	r186_rav;
	private String	r187_product;
	private String	r187_client_grp;
	private BigDecimal	r187_total_book_expo;
	private BigDecimal	r187_margin_pro;
	private BigDecimal	r187_book_expo;
	private BigDecimal	r187_ccf_cont;
	private BigDecimal	r187_equiv_value;
	private BigDecimal	r187_rw_obligant;
	private BigDecimal	r187_rav;
	private String	r188_product;
	private String	r188_client_grp;
	private BigDecimal	r188_total_book_expo;
	private BigDecimal	r188_margin_pro;
	private BigDecimal	r188_book_expo;
	private BigDecimal	r188_ccf_cont;
	private BigDecimal	r188_equiv_value;
	private BigDecimal	r188_rw_obligant;
	private BigDecimal	r188_rav;
	private String	r189_product;
	private String	r189_client_grp;
	private BigDecimal	r189_total_book_expo;
	private BigDecimal	r189_margin_pro;
	private BigDecimal	r189_book_expo;
	private BigDecimal	r189_ccf_cont;
	private BigDecimal	r189_equiv_value;
	private BigDecimal	r189_rw_obligant;
	private BigDecimal	r189_rav;


	

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
		
	private Date	report_date;
	 @Column(name = "REPORT_VERSION")
	 @Id
	private BigDecimal	report_version;
	@Column(name = "REPORT_RESUBDATE")

    private Date reportResubDate;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR151_product() {
		return r151_product;
	}
	public void setR151_product(String r151_product) {
		this.r151_product = r151_product;
	}
	public String getR151_client_grp() {
		return r151_client_grp;
	}
	public void setR151_client_grp(String r151_client_grp) {
		this.r151_client_grp = r151_client_grp;
	}
	public BigDecimal getR151_total_book_expo() {
		return r151_total_book_expo;
	}
	public void setR151_total_book_expo(BigDecimal r151_total_book_expo) {
		this.r151_total_book_expo = r151_total_book_expo;
	}
	public BigDecimal getR151_margin_pro() {
		return r151_margin_pro;
	}
	public void setR151_margin_pro(BigDecimal r151_margin_pro) {
		this.r151_margin_pro = r151_margin_pro;
	}
	public BigDecimal getR151_book_expo() {
		return r151_book_expo;
	}
	public void setR151_book_expo(BigDecimal r151_book_expo) {
		this.r151_book_expo = r151_book_expo;
	}
	public BigDecimal getR151_ccf_cont() {
		return r151_ccf_cont;
	}
	public void setR151_ccf_cont(BigDecimal r151_ccf_cont) {
		this.r151_ccf_cont = r151_ccf_cont;
	}
	public BigDecimal getR151_equiv_value() {
		return r151_equiv_value;
	}
	public void setR151_equiv_value(BigDecimal r151_equiv_value) {
		this.r151_equiv_value = r151_equiv_value;
	}
	public BigDecimal getR151_rw_obligant() {
		return r151_rw_obligant;
	}
	public void setR151_rw_obligant(BigDecimal r151_rw_obligant) {
		this.r151_rw_obligant = r151_rw_obligant;
	}
	public BigDecimal getR151_rav() {
		return r151_rav;
	}
	public void setR151_rav(BigDecimal r151_rav) {
		this.r151_rav = r151_rav;
	}
	public String getR152_product() {
		return r152_product;
	}
	public void setR152_product(String r152_product) {
		this.r152_product = r152_product;
	}
	public String getR152_client_grp() {
		return r152_client_grp;
	}
	public void setR152_client_grp(String r152_client_grp) {
		this.r152_client_grp = r152_client_grp;
	}
	public BigDecimal getR152_total_book_expo() {
		return r152_total_book_expo;
	}
	public void setR152_total_book_expo(BigDecimal r152_total_book_expo) {
		this.r152_total_book_expo = r152_total_book_expo;
	}
	public BigDecimal getR152_margin_pro() {
		return r152_margin_pro;
	}
	public void setR152_margin_pro(BigDecimal r152_margin_pro) {
		this.r152_margin_pro = r152_margin_pro;
	}
	public BigDecimal getR152_book_expo() {
		return r152_book_expo;
	}
	public void setR152_book_expo(BigDecimal r152_book_expo) {
		this.r152_book_expo = r152_book_expo;
	}
	public BigDecimal getR152_ccf_cont() {
		return r152_ccf_cont;
	}
	public void setR152_ccf_cont(BigDecimal r152_ccf_cont) {
		this.r152_ccf_cont = r152_ccf_cont;
	}
	public BigDecimal getR152_equiv_value() {
		return r152_equiv_value;
	}
	public void setR152_equiv_value(BigDecimal r152_equiv_value) {
		this.r152_equiv_value = r152_equiv_value;
	}
	public BigDecimal getR152_rw_obligant() {
		return r152_rw_obligant;
	}
	public void setR152_rw_obligant(BigDecimal r152_rw_obligant) {
		this.r152_rw_obligant = r152_rw_obligant;
	}
	public BigDecimal getR152_rav() {
		return r152_rav;
	}
	public void setR152_rav(BigDecimal r152_rav) {
		this.r152_rav = r152_rav;
	}
	public String getR153_product() {
		return r153_product;
	}
	public void setR153_product(String r153_product) {
		this.r153_product = r153_product;
	}
	public String getR153_client_grp() {
		return r153_client_grp;
	}
	public void setR153_client_grp(String r153_client_grp) {
		this.r153_client_grp = r153_client_grp;
	}
	public BigDecimal getR153_total_book_expo() {
		return r153_total_book_expo;
	}
	public void setR153_total_book_expo(BigDecimal r153_total_book_expo) {
		this.r153_total_book_expo = r153_total_book_expo;
	}
	public BigDecimal getR153_margin_pro() {
		return r153_margin_pro;
	}
	public void setR153_margin_pro(BigDecimal r153_margin_pro) {
		this.r153_margin_pro = r153_margin_pro;
	}
	public BigDecimal getR153_book_expo() {
		return r153_book_expo;
	}
	public void setR153_book_expo(BigDecimal r153_book_expo) {
		this.r153_book_expo = r153_book_expo;
	}
	public BigDecimal getR153_ccf_cont() {
		return r153_ccf_cont;
	}
	public void setR153_ccf_cont(BigDecimal r153_ccf_cont) {
		this.r153_ccf_cont = r153_ccf_cont;
	}
	public BigDecimal getR153_equiv_value() {
		return r153_equiv_value;
	}
	public void setR153_equiv_value(BigDecimal r153_equiv_value) {
		this.r153_equiv_value = r153_equiv_value;
	}
	public BigDecimal getR153_rw_obligant() {
		return r153_rw_obligant;
	}
	public void setR153_rw_obligant(BigDecimal r153_rw_obligant) {
		this.r153_rw_obligant = r153_rw_obligant;
	}
	public BigDecimal getR153_rav() {
		return r153_rav;
	}
	public void setR153_rav(BigDecimal r153_rav) {
		this.r153_rav = r153_rav;
	}
	public String getR154_product() {
		return r154_product;
	}
	public void setR154_product(String r154_product) {
		this.r154_product = r154_product;
	}
	public String getR154_client_grp() {
		return r154_client_grp;
	}
	public void setR154_client_grp(String r154_client_grp) {
		this.r154_client_grp = r154_client_grp;
	}
	public BigDecimal getR154_total_book_expo() {
		return r154_total_book_expo;
	}
	public void setR154_total_book_expo(BigDecimal r154_total_book_expo) {
		this.r154_total_book_expo = r154_total_book_expo;
	}
	public BigDecimal getR154_margin_pro() {
		return r154_margin_pro;
	}
	public void setR154_margin_pro(BigDecimal r154_margin_pro) {
		this.r154_margin_pro = r154_margin_pro;
	}
	public BigDecimal getR154_book_expo() {
		return r154_book_expo;
	}
	public void setR154_book_expo(BigDecimal r154_book_expo) {
		this.r154_book_expo = r154_book_expo;
	}
	public BigDecimal getR154_ccf_cont() {
		return r154_ccf_cont;
	}
	public void setR154_ccf_cont(BigDecimal r154_ccf_cont) {
		this.r154_ccf_cont = r154_ccf_cont;
	}
	public BigDecimal getR154_equiv_value() {
		return r154_equiv_value;
	}
	public void setR154_equiv_value(BigDecimal r154_equiv_value) {
		this.r154_equiv_value = r154_equiv_value;
	}
	public BigDecimal getR154_rw_obligant() {
		return r154_rw_obligant;
	}
	public void setR154_rw_obligant(BigDecimal r154_rw_obligant) {
		this.r154_rw_obligant = r154_rw_obligant;
	}
	public BigDecimal getR154_rav() {
		return r154_rav;
	}
	public void setR154_rav(BigDecimal r154_rav) {
		this.r154_rav = r154_rav;
	}
	public String getR155_product() {
		return r155_product;
	}
	public void setR155_product(String r155_product) {
		this.r155_product = r155_product;
	}
	public String getR155_client_grp() {
		return r155_client_grp;
	}
	public void setR155_client_grp(String r155_client_grp) {
		this.r155_client_grp = r155_client_grp;
	}
	public BigDecimal getR155_total_book_expo() {
		return r155_total_book_expo;
	}
	public void setR155_total_book_expo(BigDecimal r155_total_book_expo) {
		this.r155_total_book_expo = r155_total_book_expo;
	}
	public BigDecimal getR155_margin_pro() {
		return r155_margin_pro;
	}
	public void setR155_margin_pro(BigDecimal r155_margin_pro) {
		this.r155_margin_pro = r155_margin_pro;
	}
	public BigDecimal getR155_book_expo() {
		return r155_book_expo;
	}
	public void setR155_book_expo(BigDecimal r155_book_expo) {
		this.r155_book_expo = r155_book_expo;
	}
	public BigDecimal getR155_ccf_cont() {
		return r155_ccf_cont;
	}
	public void setR155_ccf_cont(BigDecimal r155_ccf_cont) {
		this.r155_ccf_cont = r155_ccf_cont;
	}
	public BigDecimal getR155_equiv_value() {
		return r155_equiv_value;
	}
	public void setR155_equiv_value(BigDecimal r155_equiv_value) {
		this.r155_equiv_value = r155_equiv_value;
	}
	public BigDecimal getR155_rw_obligant() {
		return r155_rw_obligant;
	}
	public void setR155_rw_obligant(BigDecimal r155_rw_obligant) {
		this.r155_rw_obligant = r155_rw_obligant;
	}
	public BigDecimal getR155_rav() {
		return r155_rav;
	}
	public void setR155_rav(BigDecimal r155_rav) {
		this.r155_rav = r155_rav;
	}
	public String getR156_product() {
		return r156_product;
	}
	public void setR156_product(String r156_product) {
		this.r156_product = r156_product;
	}
	public String getR156_client_grp() {
		return r156_client_grp;
	}
	public void setR156_client_grp(String r156_client_grp) {
		this.r156_client_grp = r156_client_grp;
	}
	public BigDecimal getR156_total_book_expo() {
		return r156_total_book_expo;
	}
	public void setR156_total_book_expo(BigDecimal r156_total_book_expo) {
		this.r156_total_book_expo = r156_total_book_expo;
	}
	public BigDecimal getR156_margin_pro() {
		return r156_margin_pro;
	}
	public void setR156_margin_pro(BigDecimal r156_margin_pro) {
		this.r156_margin_pro = r156_margin_pro;
	}
	public BigDecimal getR156_book_expo() {
		return r156_book_expo;
	}
	public void setR156_book_expo(BigDecimal r156_book_expo) {
		this.r156_book_expo = r156_book_expo;
	}
	public BigDecimal getR156_ccf_cont() {
		return r156_ccf_cont;
	}
	public void setR156_ccf_cont(BigDecimal r156_ccf_cont) {
		this.r156_ccf_cont = r156_ccf_cont;
	}
	public BigDecimal getR156_equiv_value() {
		return r156_equiv_value;
	}
	public void setR156_equiv_value(BigDecimal r156_equiv_value) {
		this.r156_equiv_value = r156_equiv_value;
	}
	public BigDecimal getR156_rw_obligant() {
		return r156_rw_obligant;
	}
	public void setR156_rw_obligant(BigDecimal r156_rw_obligant) {
		this.r156_rw_obligant = r156_rw_obligant;
	}
	public BigDecimal getR156_rav() {
		return r156_rav;
	}
	public void setR156_rav(BigDecimal r156_rav) {
		this.r156_rav = r156_rav;
	}
	public String getR157_product() {
		return r157_product;
	}
	public void setR157_product(String r157_product) {
		this.r157_product = r157_product;
	}
	public String getR157_client_grp() {
		return r157_client_grp;
	}
	public void setR157_client_grp(String r157_client_grp) {
		this.r157_client_grp = r157_client_grp;
	}
	public BigDecimal getR157_total_book_expo() {
		return r157_total_book_expo;
	}
	public void setR157_total_book_expo(BigDecimal r157_total_book_expo) {
		this.r157_total_book_expo = r157_total_book_expo;
	}
	public BigDecimal getR157_margin_pro() {
		return r157_margin_pro;
	}
	public void setR157_margin_pro(BigDecimal r157_margin_pro) {
		this.r157_margin_pro = r157_margin_pro;
	}
	public BigDecimal getR157_book_expo() {
		return r157_book_expo;
	}
	public void setR157_book_expo(BigDecimal r157_book_expo) {
		this.r157_book_expo = r157_book_expo;
	}
	public BigDecimal getR157_ccf_cont() {
		return r157_ccf_cont;
	}
	public void setR157_ccf_cont(BigDecimal r157_ccf_cont) {
		this.r157_ccf_cont = r157_ccf_cont;
	}
	public BigDecimal getR157_equiv_value() {
		return r157_equiv_value;
	}
	public void setR157_equiv_value(BigDecimal r157_equiv_value) {
		this.r157_equiv_value = r157_equiv_value;
	}
	public BigDecimal getR157_rw_obligant() {
		return r157_rw_obligant;
	}
	public void setR157_rw_obligant(BigDecimal r157_rw_obligant) {
		this.r157_rw_obligant = r157_rw_obligant;
	}
	public BigDecimal getR157_rav() {
		return r157_rav;
	}
	public void setR157_rav(BigDecimal r157_rav) {
		this.r157_rav = r157_rav;
	}
	public String getR158_product() {
		return r158_product;
	}
	public void setR158_product(String r158_product) {
		this.r158_product = r158_product;
	}
	public String getR158_client_grp() {
		return r158_client_grp;
	}
	public void setR158_client_grp(String r158_client_grp) {
		this.r158_client_grp = r158_client_grp;
	}
	public BigDecimal getR158_total_book_expo() {
		return r158_total_book_expo;
	}
	public void setR158_total_book_expo(BigDecimal r158_total_book_expo) {
		this.r158_total_book_expo = r158_total_book_expo;
	}
	public BigDecimal getR158_margin_pro() {
		return r158_margin_pro;
	}
	public void setR158_margin_pro(BigDecimal r158_margin_pro) {
		this.r158_margin_pro = r158_margin_pro;
	}
	public BigDecimal getR158_book_expo() {
		return r158_book_expo;
	}
	public void setR158_book_expo(BigDecimal r158_book_expo) {
		this.r158_book_expo = r158_book_expo;
	}
	public BigDecimal getR158_ccf_cont() {
		return r158_ccf_cont;
	}
	public void setR158_ccf_cont(BigDecimal r158_ccf_cont) {
		this.r158_ccf_cont = r158_ccf_cont;
	}
	public BigDecimal getR158_equiv_value() {
		return r158_equiv_value;
	}
	public void setR158_equiv_value(BigDecimal r158_equiv_value) {
		this.r158_equiv_value = r158_equiv_value;
	}
	public BigDecimal getR158_rw_obligant() {
		return r158_rw_obligant;
	}
	public void setR158_rw_obligant(BigDecimal r158_rw_obligant) {
		this.r158_rw_obligant = r158_rw_obligant;
	}
	public BigDecimal getR158_rav() {
		return r158_rav;
	}
	public void setR158_rav(BigDecimal r158_rav) {
		this.r158_rav = r158_rav;
	}
	public String getR159_product() {
		return r159_product;
	}
	public void setR159_product(String r159_product) {
		this.r159_product = r159_product;
	}
	public String getR159_client_grp() {
		return r159_client_grp;
	}
	public void setR159_client_grp(String r159_client_grp) {
		this.r159_client_grp = r159_client_grp;
	}
	public BigDecimal getR159_total_book_expo() {
		return r159_total_book_expo;
	}
	public void setR159_total_book_expo(BigDecimal r159_total_book_expo) {
		this.r159_total_book_expo = r159_total_book_expo;
	}
	public BigDecimal getR159_margin_pro() {
		return r159_margin_pro;
	}
	public void setR159_margin_pro(BigDecimal r159_margin_pro) {
		this.r159_margin_pro = r159_margin_pro;
	}
	public BigDecimal getR159_book_expo() {
		return r159_book_expo;
	}
	public void setR159_book_expo(BigDecimal r159_book_expo) {
		this.r159_book_expo = r159_book_expo;
	}
	public BigDecimal getR159_ccf_cont() {
		return r159_ccf_cont;
	}
	public void setR159_ccf_cont(BigDecimal r159_ccf_cont) {
		this.r159_ccf_cont = r159_ccf_cont;
	}
	public BigDecimal getR159_equiv_value() {
		return r159_equiv_value;
	}
	public void setR159_equiv_value(BigDecimal r159_equiv_value) {
		this.r159_equiv_value = r159_equiv_value;
	}
	public BigDecimal getR159_rw_obligant() {
		return r159_rw_obligant;
	}
	public void setR159_rw_obligant(BigDecimal r159_rw_obligant) {
		this.r159_rw_obligant = r159_rw_obligant;
	}
	public BigDecimal getR159_rav() {
		return r159_rav;
	}
	public void setR159_rav(BigDecimal r159_rav) {
		this.r159_rav = r159_rav;
	}
	public String getR160_product() {
		return r160_product;
	}
	public void setR160_product(String r160_product) {
		this.r160_product = r160_product;
	}
	public String getR160_client_grp() {
		return r160_client_grp;
	}
	public void setR160_client_grp(String r160_client_grp) {
		this.r160_client_grp = r160_client_grp;
	}
	public BigDecimal getR160_total_book_expo() {
		return r160_total_book_expo;
	}
	public void setR160_total_book_expo(BigDecimal r160_total_book_expo) {
		this.r160_total_book_expo = r160_total_book_expo;
	}
	public BigDecimal getR160_margin_pro() {
		return r160_margin_pro;
	}
	public void setR160_margin_pro(BigDecimal r160_margin_pro) {
		this.r160_margin_pro = r160_margin_pro;
	}
	public BigDecimal getR160_book_expo() {
		return r160_book_expo;
	}
	public void setR160_book_expo(BigDecimal r160_book_expo) {
		this.r160_book_expo = r160_book_expo;
	}
	public BigDecimal getR160_ccf_cont() {
		return r160_ccf_cont;
	}
	public void setR160_ccf_cont(BigDecimal r160_ccf_cont) {
		this.r160_ccf_cont = r160_ccf_cont;
	}
	public BigDecimal getR160_equiv_value() {
		return r160_equiv_value;
	}
	public void setR160_equiv_value(BigDecimal r160_equiv_value) {
		this.r160_equiv_value = r160_equiv_value;
	}
	public BigDecimal getR160_rw_obligant() {
		return r160_rw_obligant;
	}
	public void setR160_rw_obligant(BigDecimal r160_rw_obligant) {
		this.r160_rw_obligant = r160_rw_obligant;
	}
	public BigDecimal getR160_rav() {
		return r160_rav;
	}
	public void setR160_rav(BigDecimal r160_rav) {
		this.r160_rav = r160_rav;
	}
	public String getR161_product() {
		return r161_product;
	}
	public void setR161_product(String r161_product) {
		this.r161_product = r161_product;
	}
	public String getR161_client_grp() {
		return r161_client_grp;
	}
	public void setR161_client_grp(String r161_client_grp) {
		this.r161_client_grp = r161_client_grp;
	}
	public BigDecimal getR161_total_book_expo() {
		return r161_total_book_expo;
	}
	public void setR161_total_book_expo(BigDecimal r161_total_book_expo) {
		this.r161_total_book_expo = r161_total_book_expo;
	}
	public BigDecimal getR161_margin_pro() {
		return r161_margin_pro;
	}
	public void setR161_margin_pro(BigDecimal r161_margin_pro) {
		this.r161_margin_pro = r161_margin_pro;
	}
	public BigDecimal getR161_book_expo() {
		return r161_book_expo;
	}
	public void setR161_book_expo(BigDecimal r161_book_expo) {
		this.r161_book_expo = r161_book_expo;
	}
	public BigDecimal getR161_ccf_cont() {
		return r161_ccf_cont;
	}
	public void setR161_ccf_cont(BigDecimal r161_ccf_cont) {
		this.r161_ccf_cont = r161_ccf_cont;
	}
	public BigDecimal getR161_equiv_value() {
		return r161_equiv_value;
	}
	public void setR161_equiv_value(BigDecimal r161_equiv_value) {
		this.r161_equiv_value = r161_equiv_value;
	}
	public BigDecimal getR161_rw_obligant() {
		return r161_rw_obligant;
	}
	public void setR161_rw_obligant(BigDecimal r161_rw_obligant) {
		this.r161_rw_obligant = r161_rw_obligant;
	}
	public BigDecimal getR161_rav() {
		return r161_rav;
	}
	public void setR161_rav(BigDecimal r161_rav) {
		this.r161_rav = r161_rav;
	}
	public String getR162_product() {
		return r162_product;
	}
	public void setR162_product(String r162_product) {
		this.r162_product = r162_product;
	}
	public String getR162_client_grp() {
		return r162_client_grp;
	}
	public void setR162_client_grp(String r162_client_grp) {
		this.r162_client_grp = r162_client_grp;
	}
	public BigDecimal getR162_total_book_expo() {
		return r162_total_book_expo;
	}
	public void setR162_total_book_expo(BigDecimal r162_total_book_expo) {
		this.r162_total_book_expo = r162_total_book_expo;
	}
	public BigDecimal getR162_margin_pro() {
		return r162_margin_pro;
	}
	public void setR162_margin_pro(BigDecimal r162_margin_pro) {
		this.r162_margin_pro = r162_margin_pro;
	}
	public BigDecimal getR162_book_expo() {
		return r162_book_expo;
	}
	public void setR162_book_expo(BigDecimal r162_book_expo) {
		this.r162_book_expo = r162_book_expo;
	}
	public BigDecimal getR162_ccf_cont() {
		return r162_ccf_cont;
	}
	public void setR162_ccf_cont(BigDecimal r162_ccf_cont) {
		this.r162_ccf_cont = r162_ccf_cont;
	}
	public BigDecimal getR162_equiv_value() {
		return r162_equiv_value;
	}
	public void setR162_equiv_value(BigDecimal r162_equiv_value) {
		this.r162_equiv_value = r162_equiv_value;
	}
	public BigDecimal getR162_rw_obligant() {
		return r162_rw_obligant;
	}
	public void setR162_rw_obligant(BigDecimal r162_rw_obligant) {
		this.r162_rw_obligant = r162_rw_obligant;
	}
	public BigDecimal getR162_rav() {
		return r162_rav;
	}
	public void setR162_rav(BigDecimal r162_rav) {
		this.r162_rav = r162_rav;
	}
	public String getR163_product() {
		return r163_product;
	}
	public void setR163_product(String r163_product) {
		this.r163_product = r163_product;
	}
	public String getR163_client_grp() {
		return r163_client_grp;
	}
	public void setR163_client_grp(String r163_client_grp) {
		this.r163_client_grp = r163_client_grp;
	}
	public BigDecimal getR163_total_book_expo() {
		return r163_total_book_expo;
	}
	public void setR163_total_book_expo(BigDecimal r163_total_book_expo) {
		this.r163_total_book_expo = r163_total_book_expo;
	}
	public BigDecimal getR163_margin_pro() {
		return r163_margin_pro;
	}
	public void setR163_margin_pro(BigDecimal r163_margin_pro) {
		this.r163_margin_pro = r163_margin_pro;
	}
	public BigDecimal getR163_book_expo() {
		return r163_book_expo;
	}
	public void setR163_book_expo(BigDecimal r163_book_expo) {
		this.r163_book_expo = r163_book_expo;
	}
	public BigDecimal getR163_ccf_cont() {
		return r163_ccf_cont;
	}
	public void setR163_ccf_cont(BigDecimal r163_ccf_cont) {
		this.r163_ccf_cont = r163_ccf_cont;
	}
	public BigDecimal getR163_equiv_value() {
		return r163_equiv_value;
	}
	public void setR163_equiv_value(BigDecimal r163_equiv_value) {
		this.r163_equiv_value = r163_equiv_value;
	}
	public BigDecimal getR163_rw_obligant() {
		return r163_rw_obligant;
	}
	public void setR163_rw_obligant(BigDecimal r163_rw_obligant) {
		this.r163_rw_obligant = r163_rw_obligant;
	}
	public BigDecimal getR163_rav() {
		return r163_rav;
	}
	public void setR163_rav(BigDecimal r163_rav) {
		this.r163_rav = r163_rav;
	}
	public String getR164_product() {
		return r164_product;
	}
	public void setR164_product(String r164_product) {
		this.r164_product = r164_product;
	}
	public String getR164_client_grp() {
		return r164_client_grp;
	}
	public void setR164_client_grp(String r164_client_grp) {
		this.r164_client_grp = r164_client_grp;
	}
	public BigDecimal getR164_total_book_expo() {
		return r164_total_book_expo;
	}
	public void setR164_total_book_expo(BigDecimal r164_total_book_expo) {
		this.r164_total_book_expo = r164_total_book_expo;
	}
	public BigDecimal getR164_margin_pro() {
		return r164_margin_pro;
	}
	public void setR164_margin_pro(BigDecimal r164_margin_pro) {
		this.r164_margin_pro = r164_margin_pro;
	}
	public BigDecimal getR164_book_expo() {
		return r164_book_expo;
	}
	public void setR164_book_expo(BigDecimal r164_book_expo) {
		this.r164_book_expo = r164_book_expo;
	}
	public BigDecimal getR164_ccf_cont() {
		return r164_ccf_cont;
	}
	public void setR164_ccf_cont(BigDecimal r164_ccf_cont) {
		this.r164_ccf_cont = r164_ccf_cont;
	}
	public BigDecimal getR164_equiv_value() {
		return r164_equiv_value;
	}
	public void setR164_equiv_value(BigDecimal r164_equiv_value) {
		this.r164_equiv_value = r164_equiv_value;
	}
	public BigDecimal getR164_rw_obligant() {
		return r164_rw_obligant;
	}
	public void setR164_rw_obligant(BigDecimal r164_rw_obligant) {
		this.r164_rw_obligant = r164_rw_obligant;
	}
	public BigDecimal getR164_rav() {
		return r164_rav;
	}
	public void setR164_rav(BigDecimal r164_rav) {
		this.r164_rav = r164_rav;
	}
	public String getR165_product() {
		return r165_product;
	}
	public void setR165_product(String r165_product) {
		this.r165_product = r165_product;
	}
	public String getR165_client_grp() {
		return r165_client_grp;
	}
	public void setR165_client_grp(String r165_client_grp) {
		this.r165_client_grp = r165_client_grp;
	}
	public BigDecimal getR165_total_book_expo() {
		return r165_total_book_expo;
	}
	public void setR165_total_book_expo(BigDecimal r165_total_book_expo) {
		this.r165_total_book_expo = r165_total_book_expo;
	}
	public BigDecimal getR165_margin_pro() {
		return r165_margin_pro;
	}
	public void setR165_margin_pro(BigDecimal r165_margin_pro) {
		this.r165_margin_pro = r165_margin_pro;
	}
	public BigDecimal getR165_book_expo() {
		return r165_book_expo;
	}
	public void setR165_book_expo(BigDecimal r165_book_expo) {
		this.r165_book_expo = r165_book_expo;
	}
	public BigDecimal getR165_ccf_cont() {
		return r165_ccf_cont;
	}
	public void setR165_ccf_cont(BigDecimal r165_ccf_cont) {
		this.r165_ccf_cont = r165_ccf_cont;
	}
	public BigDecimal getR165_equiv_value() {
		return r165_equiv_value;
	}
	public void setR165_equiv_value(BigDecimal r165_equiv_value) {
		this.r165_equiv_value = r165_equiv_value;
	}
	public BigDecimal getR165_rw_obligant() {
		return r165_rw_obligant;
	}
	public void setR165_rw_obligant(BigDecimal r165_rw_obligant) {
		this.r165_rw_obligant = r165_rw_obligant;
	}
	public BigDecimal getR165_rav() {
		return r165_rav;
	}
	public void setR165_rav(BigDecimal r165_rav) {
		this.r165_rav = r165_rav;
	}
	public String getR166_product() {
		return r166_product;
	}
	public void setR166_product(String r166_product) {
		this.r166_product = r166_product;
	}
	public String getR166_client_grp() {
		return r166_client_grp;
	}
	public void setR166_client_grp(String r166_client_grp) {
		this.r166_client_grp = r166_client_grp;
	}
	public BigDecimal getR166_total_book_expo() {
		return r166_total_book_expo;
	}
	public void setR166_total_book_expo(BigDecimal r166_total_book_expo) {
		this.r166_total_book_expo = r166_total_book_expo;
	}
	public BigDecimal getR166_margin_pro() {
		return r166_margin_pro;
	}
	public void setR166_margin_pro(BigDecimal r166_margin_pro) {
		this.r166_margin_pro = r166_margin_pro;
	}
	public BigDecimal getR166_book_expo() {
		return r166_book_expo;
	}
	public void setR166_book_expo(BigDecimal r166_book_expo) {
		this.r166_book_expo = r166_book_expo;
	}
	public BigDecimal getR166_ccf_cont() {
		return r166_ccf_cont;
	}
	public void setR166_ccf_cont(BigDecimal r166_ccf_cont) {
		this.r166_ccf_cont = r166_ccf_cont;
	}
	public BigDecimal getR166_equiv_value() {
		return r166_equiv_value;
	}
	public void setR166_equiv_value(BigDecimal r166_equiv_value) {
		this.r166_equiv_value = r166_equiv_value;
	}
	public BigDecimal getR166_rw_obligant() {
		return r166_rw_obligant;
	}
	public void setR166_rw_obligant(BigDecimal r166_rw_obligant) {
		this.r166_rw_obligant = r166_rw_obligant;
	}
	public BigDecimal getR166_rav() {
		return r166_rav;
	}
	public void setR166_rav(BigDecimal r166_rav) {
		this.r166_rav = r166_rav;
	}
	public String getR167_product() {
		return r167_product;
	}
	public void setR167_product(String r167_product) {
		this.r167_product = r167_product;
	}
	public String getR167_client_grp() {
		return r167_client_grp;
	}
	public void setR167_client_grp(String r167_client_grp) {
		this.r167_client_grp = r167_client_grp;
	}
	public BigDecimal getR167_total_book_expo() {
		return r167_total_book_expo;
	}
	public void setR167_total_book_expo(BigDecimal r167_total_book_expo) {
		this.r167_total_book_expo = r167_total_book_expo;
	}
	public BigDecimal getR167_margin_pro() {
		return r167_margin_pro;
	}
	public void setR167_margin_pro(BigDecimal r167_margin_pro) {
		this.r167_margin_pro = r167_margin_pro;
	}
	public BigDecimal getR167_book_expo() {
		return r167_book_expo;
	}
	public void setR167_book_expo(BigDecimal r167_book_expo) {
		this.r167_book_expo = r167_book_expo;
	}
	public BigDecimal getR167_ccf_cont() {
		return r167_ccf_cont;
	}
	public void setR167_ccf_cont(BigDecimal r167_ccf_cont) {
		this.r167_ccf_cont = r167_ccf_cont;
	}
	public BigDecimal getR167_equiv_value() {
		return r167_equiv_value;
	}
	public void setR167_equiv_value(BigDecimal r167_equiv_value) {
		this.r167_equiv_value = r167_equiv_value;
	}
	public BigDecimal getR167_rw_obligant() {
		return r167_rw_obligant;
	}
	public void setR167_rw_obligant(BigDecimal r167_rw_obligant) {
		this.r167_rw_obligant = r167_rw_obligant;
	}
	public BigDecimal getR167_rav() {
		return r167_rav;
	}
	public void setR167_rav(BigDecimal r167_rav) {
		this.r167_rav = r167_rav;
	}
	public String getR168_product() {
		return r168_product;
	}
	public void setR168_product(String r168_product) {
		this.r168_product = r168_product;
	}
	public String getR168_client_grp() {
		return r168_client_grp;
	}
	public void setR168_client_grp(String r168_client_grp) {
		this.r168_client_grp = r168_client_grp;
	}
	public BigDecimal getR168_total_book_expo() {
		return r168_total_book_expo;
	}
	public void setR168_total_book_expo(BigDecimal r168_total_book_expo) {
		this.r168_total_book_expo = r168_total_book_expo;
	}
	public BigDecimal getR168_margin_pro() {
		return r168_margin_pro;
	}
	public void setR168_margin_pro(BigDecimal r168_margin_pro) {
		this.r168_margin_pro = r168_margin_pro;
	}
	public BigDecimal getR168_book_expo() {
		return r168_book_expo;
	}
	public void setR168_book_expo(BigDecimal r168_book_expo) {
		this.r168_book_expo = r168_book_expo;
	}
	public BigDecimal getR168_ccf_cont() {
		return r168_ccf_cont;
	}
	public void setR168_ccf_cont(BigDecimal r168_ccf_cont) {
		this.r168_ccf_cont = r168_ccf_cont;
	}
	public BigDecimal getR168_equiv_value() {
		return r168_equiv_value;
	}
	public void setR168_equiv_value(BigDecimal r168_equiv_value) {
		this.r168_equiv_value = r168_equiv_value;
	}
	public BigDecimal getR168_rw_obligant() {
		return r168_rw_obligant;
	}
	public void setR168_rw_obligant(BigDecimal r168_rw_obligant) {
		this.r168_rw_obligant = r168_rw_obligant;
	}
	public BigDecimal getR168_rav() {
		return r168_rav;
	}
	public void setR168_rav(BigDecimal r168_rav) {
		this.r168_rav = r168_rav;
	}
	public String getR169_product() {
		return r169_product;
	}
	public void setR169_product(String r169_product) {
		this.r169_product = r169_product;
	}
	public String getR169_client_grp() {
		return r169_client_grp;
	}
	public void setR169_client_grp(String r169_client_grp) {
		this.r169_client_grp = r169_client_grp;
	}
	public BigDecimal getR169_total_book_expo() {
		return r169_total_book_expo;
	}
	public void setR169_total_book_expo(BigDecimal r169_total_book_expo) {
		this.r169_total_book_expo = r169_total_book_expo;
	}
	public BigDecimal getR169_margin_pro() {
		return r169_margin_pro;
	}
	public void setR169_margin_pro(BigDecimal r169_margin_pro) {
		this.r169_margin_pro = r169_margin_pro;
	}
	public BigDecimal getR169_book_expo() {
		return r169_book_expo;
	}
	public void setR169_book_expo(BigDecimal r169_book_expo) {
		this.r169_book_expo = r169_book_expo;
	}
	public BigDecimal getR169_ccf_cont() {
		return r169_ccf_cont;
	}
	public void setR169_ccf_cont(BigDecimal r169_ccf_cont) {
		this.r169_ccf_cont = r169_ccf_cont;
	}
	public BigDecimal getR169_equiv_value() {
		return r169_equiv_value;
	}
	public void setR169_equiv_value(BigDecimal r169_equiv_value) {
		this.r169_equiv_value = r169_equiv_value;
	}
	public BigDecimal getR169_rw_obligant() {
		return r169_rw_obligant;
	}
	public void setR169_rw_obligant(BigDecimal r169_rw_obligant) {
		this.r169_rw_obligant = r169_rw_obligant;
	}
	public BigDecimal getR169_rav() {
		return r169_rav;
	}
	public void setR169_rav(BigDecimal r169_rav) {
		this.r169_rav = r169_rav;
	}
	public String getR170_product() {
		return r170_product;
	}
	public void setR170_product(String r170_product) {
		this.r170_product = r170_product;
	}
	public String getR170_client_grp() {
		return r170_client_grp;
	}
	public void setR170_client_grp(String r170_client_grp) {
		this.r170_client_grp = r170_client_grp;
	}
	public BigDecimal getR170_total_book_expo() {
		return r170_total_book_expo;
	}
	public void setR170_total_book_expo(BigDecimal r170_total_book_expo) {
		this.r170_total_book_expo = r170_total_book_expo;
	}
	public BigDecimal getR170_margin_pro() {
		return r170_margin_pro;
	}
	public void setR170_margin_pro(BigDecimal r170_margin_pro) {
		this.r170_margin_pro = r170_margin_pro;
	}
	public BigDecimal getR170_book_expo() {
		return r170_book_expo;
	}
	public void setR170_book_expo(BigDecimal r170_book_expo) {
		this.r170_book_expo = r170_book_expo;
	}
	public BigDecimal getR170_ccf_cont() {
		return r170_ccf_cont;
	}
	public void setR170_ccf_cont(BigDecimal r170_ccf_cont) {
		this.r170_ccf_cont = r170_ccf_cont;
	}
	public BigDecimal getR170_equiv_value() {
		return r170_equiv_value;
	}
	public void setR170_equiv_value(BigDecimal r170_equiv_value) {
		this.r170_equiv_value = r170_equiv_value;
	}
	public BigDecimal getR170_rw_obligant() {
		return r170_rw_obligant;
	}
	public void setR170_rw_obligant(BigDecimal r170_rw_obligant) {
		this.r170_rw_obligant = r170_rw_obligant;
	}
	public BigDecimal getR170_rav() {
		return r170_rav;
	}
	public void setR170_rav(BigDecimal r170_rav) {
		this.r170_rav = r170_rav;
	}
	public String getR171_product() {
		return r171_product;
	}
	public void setR171_product(String r171_product) {
		this.r171_product = r171_product;
	}
	public String getR171_client_grp() {
		return r171_client_grp;
	}
	public void setR171_client_grp(String r171_client_grp) {
		this.r171_client_grp = r171_client_grp;
	}
	public BigDecimal getR171_total_book_expo() {
		return r171_total_book_expo;
	}
	public void setR171_total_book_expo(BigDecimal r171_total_book_expo) {
		this.r171_total_book_expo = r171_total_book_expo;
	}
	public BigDecimal getR171_margin_pro() {
		return r171_margin_pro;
	}
	public void setR171_margin_pro(BigDecimal r171_margin_pro) {
		this.r171_margin_pro = r171_margin_pro;
	}
	public BigDecimal getR171_book_expo() {
		return r171_book_expo;
	}
	public void setR171_book_expo(BigDecimal r171_book_expo) {
		this.r171_book_expo = r171_book_expo;
	}
	public BigDecimal getR171_ccf_cont() {
		return r171_ccf_cont;
	}
	public void setR171_ccf_cont(BigDecimal r171_ccf_cont) {
		this.r171_ccf_cont = r171_ccf_cont;
	}
	public BigDecimal getR171_equiv_value() {
		return r171_equiv_value;
	}
	public void setR171_equiv_value(BigDecimal r171_equiv_value) {
		this.r171_equiv_value = r171_equiv_value;
	}
	public BigDecimal getR171_rw_obligant() {
		return r171_rw_obligant;
	}
	public void setR171_rw_obligant(BigDecimal r171_rw_obligant) {
		this.r171_rw_obligant = r171_rw_obligant;
	}
	public BigDecimal getR171_rav() {
		return r171_rav;
	}
	public void setR171_rav(BigDecimal r171_rav) {
		this.r171_rav = r171_rav;
	}
	public String getR172_product() {
		return r172_product;
	}
	public void setR172_product(String r172_product) {
		this.r172_product = r172_product;
	}
	public String getR172_client_grp() {
		return r172_client_grp;
	}
	public void setR172_client_grp(String r172_client_grp) {
		this.r172_client_grp = r172_client_grp;
	}
	public BigDecimal getR172_total_book_expo() {
		return r172_total_book_expo;
	}
	public void setR172_total_book_expo(BigDecimal r172_total_book_expo) {
		this.r172_total_book_expo = r172_total_book_expo;
	}
	public BigDecimal getR172_margin_pro() {
		return r172_margin_pro;
	}
	public void setR172_margin_pro(BigDecimal r172_margin_pro) {
		this.r172_margin_pro = r172_margin_pro;
	}
	public BigDecimal getR172_book_expo() {
		return r172_book_expo;
	}
	public void setR172_book_expo(BigDecimal r172_book_expo) {
		this.r172_book_expo = r172_book_expo;
	}
	public BigDecimal getR172_ccf_cont() {
		return r172_ccf_cont;
	}
	public void setR172_ccf_cont(BigDecimal r172_ccf_cont) {
		this.r172_ccf_cont = r172_ccf_cont;
	}
	public BigDecimal getR172_equiv_value() {
		return r172_equiv_value;
	}
	public void setR172_equiv_value(BigDecimal r172_equiv_value) {
		this.r172_equiv_value = r172_equiv_value;
	}
	public BigDecimal getR172_rw_obligant() {
		return r172_rw_obligant;
	}
	public void setR172_rw_obligant(BigDecimal r172_rw_obligant) {
		this.r172_rw_obligant = r172_rw_obligant;
	}
	public BigDecimal getR172_rav() {
		return r172_rav;
	}
	public void setR172_rav(BigDecimal r172_rav) {
		this.r172_rav = r172_rav;
	}
	public String getR173_product() {
		return r173_product;
	}
	public void setR173_product(String r173_product) {
		this.r173_product = r173_product;
	}
	public String getR173_client_grp() {
		return r173_client_grp;
	}
	public void setR173_client_grp(String r173_client_grp) {
		this.r173_client_grp = r173_client_grp;
	}
	public BigDecimal getR173_total_book_expo() {
		return r173_total_book_expo;
	}
	public void setR173_total_book_expo(BigDecimal r173_total_book_expo) {
		this.r173_total_book_expo = r173_total_book_expo;
	}
	public BigDecimal getR173_margin_pro() {
		return r173_margin_pro;
	}
	public void setR173_margin_pro(BigDecimal r173_margin_pro) {
		this.r173_margin_pro = r173_margin_pro;
	}
	public BigDecimal getR173_book_expo() {
		return r173_book_expo;
	}
	public void setR173_book_expo(BigDecimal r173_book_expo) {
		this.r173_book_expo = r173_book_expo;
	}
	public BigDecimal getR173_ccf_cont() {
		return r173_ccf_cont;
	}
	public void setR173_ccf_cont(BigDecimal r173_ccf_cont) {
		this.r173_ccf_cont = r173_ccf_cont;
	}
	public BigDecimal getR173_equiv_value() {
		return r173_equiv_value;
	}
	public void setR173_equiv_value(BigDecimal r173_equiv_value) {
		this.r173_equiv_value = r173_equiv_value;
	}
	public BigDecimal getR173_rw_obligant() {
		return r173_rw_obligant;
	}
	public void setR173_rw_obligant(BigDecimal r173_rw_obligant) {
		this.r173_rw_obligant = r173_rw_obligant;
	}
	public BigDecimal getR173_rav() {
		return r173_rav;
	}
	public void setR173_rav(BigDecimal r173_rav) {
		this.r173_rav = r173_rav;
	}
	public String getR174_product() {
		return r174_product;
	}
	public void setR174_product(String r174_product) {
		this.r174_product = r174_product;
	}
	public String getR174_client_grp() {
		return r174_client_grp;
	}
	public void setR174_client_grp(String r174_client_grp) {
		this.r174_client_grp = r174_client_grp;
	}
	public BigDecimal getR174_total_book_expo() {
		return r174_total_book_expo;
	}
	public void setR174_total_book_expo(BigDecimal r174_total_book_expo) {
		this.r174_total_book_expo = r174_total_book_expo;
	}
	public BigDecimal getR174_margin_pro() {
		return r174_margin_pro;
	}
	public void setR174_margin_pro(BigDecimal r174_margin_pro) {
		this.r174_margin_pro = r174_margin_pro;
	}
	public BigDecimal getR174_book_expo() {
		return r174_book_expo;
	}
	public void setR174_book_expo(BigDecimal r174_book_expo) {
		this.r174_book_expo = r174_book_expo;
	}
	public BigDecimal getR174_ccf_cont() {
		return r174_ccf_cont;
	}
	public void setR174_ccf_cont(BigDecimal r174_ccf_cont) {
		this.r174_ccf_cont = r174_ccf_cont;
	}
	public BigDecimal getR174_equiv_value() {
		return r174_equiv_value;
	}
	public void setR174_equiv_value(BigDecimal r174_equiv_value) {
		this.r174_equiv_value = r174_equiv_value;
	}
	public BigDecimal getR174_rw_obligant() {
		return r174_rw_obligant;
	}
	public void setR174_rw_obligant(BigDecimal r174_rw_obligant) {
		this.r174_rw_obligant = r174_rw_obligant;
	}
	public BigDecimal getR174_rav() {
		return r174_rav;
	}
	public void setR174_rav(BigDecimal r174_rav) {
		this.r174_rav = r174_rav;
	}
	public String getR175_product() {
		return r175_product;
	}
	public void setR175_product(String r175_product) {
		this.r175_product = r175_product;
	}
	public String getR175_client_grp() {
		return r175_client_grp;
	}
	public void setR175_client_grp(String r175_client_grp) {
		this.r175_client_grp = r175_client_grp;
	}
	public BigDecimal getR175_total_book_expo() {
		return r175_total_book_expo;
	}
	public void setR175_total_book_expo(BigDecimal r175_total_book_expo) {
		this.r175_total_book_expo = r175_total_book_expo;
	}
	public BigDecimal getR175_margin_pro() {
		return r175_margin_pro;
	}
	public void setR175_margin_pro(BigDecimal r175_margin_pro) {
		this.r175_margin_pro = r175_margin_pro;
	}
	public BigDecimal getR175_book_expo() {
		return r175_book_expo;
	}
	public void setR175_book_expo(BigDecimal r175_book_expo) {
		this.r175_book_expo = r175_book_expo;
	}
	public BigDecimal getR175_ccf_cont() {
		return r175_ccf_cont;
	}
	public void setR175_ccf_cont(BigDecimal r175_ccf_cont) {
		this.r175_ccf_cont = r175_ccf_cont;
	}
	public BigDecimal getR175_equiv_value() {
		return r175_equiv_value;
	}
	public void setR175_equiv_value(BigDecimal r175_equiv_value) {
		this.r175_equiv_value = r175_equiv_value;
	}
	public BigDecimal getR175_rw_obligant() {
		return r175_rw_obligant;
	}
	public void setR175_rw_obligant(BigDecimal r175_rw_obligant) {
		this.r175_rw_obligant = r175_rw_obligant;
	}
	public BigDecimal getR175_rav() {
		return r175_rav;
	}
	public void setR175_rav(BigDecimal r175_rav) {
		this.r175_rav = r175_rav;
	}
	public String getR176_product() {
		return r176_product;
	}
	public void setR176_product(String r176_product) {
		this.r176_product = r176_product;
	}
	public String getR176_client_grp() {
		return r176_client_grp;
	}
	public void setR176_client_grp(String r176_client_grp) {
		this.r176_client_grp = r176_client_grp;
	}
	public BigDecimal getR176_total_book_expo() {
		return r176_total_book_expo;
	}
	public void setR176_total_book_expo(BigDecimal r176_total_book_expo) {
		this.r176_total_book_expo = r176_total_book_expo;
	}
	public BigDecimal getR176_margin_pro() {
		return r176_margin_pro;
	}
	public void setR176_margin_pro(BigDecimal r176_margin_pro) {
		this.r176_margin_pro = r176_margin_pro;
	}
	public BigDecimal getR176_book_expo() {
		return r176_book_expo;
	}
	public void setR176_book_expo(BigDecimal r176_book_expo) {
		this.r176_book_expo = r176_book_expo;
	}
	public BigDecimal getR176_ccf_cont() {
		return r176_ccf_cont;
	}
	public void setR176_ccf_cont(BigDecimal r176_ccf_cont) {
		this.r176_ccf_cont = r176_ccf_cont;
	}
	public BigDecimal getR176_equiv_value() {
		return r176_equiv_value;
	}
	public void setR176_equiv_value(BigDecimal r176_equiv_value) {
		this.r176_equiv_value = r176_equiv_value;
	}
	public BigDecimal getR176_rw_obligant() {
		return r176_rw_obligant;
	}
	public void setR176_rw_obligant(BigDecimal r176_rw_obligant) {
		this.r176_rw_obligant = r176_rw_obligant;
	}
	public BigDecimal getR176_rav() {
		return r176_rav;
	}
	public void setR176_rav(BigDecimal r176_rav) {
		this.r176_rav = r176_rav;
	}
	public String getR177_product() {
		return r177_product;
	}
	public void setR177_product(String r177_product) {
		this.r177_product = r177_product;
	}
	public String getR177_client_grp() {
		return r177_client_grp;
	}
	public void setR177_client_grp(String r177_client_grp) {
		this.r177_client_grp = r177_client_grp;
	}
	public BigDecimal getR177_total_book_expo() {
		return r177_total_book_expo;
	}
	public void setR177_total_book_expo(BigDecimal r177_total_book_expo) {
		this.r177_total_book_expo = r177_total_book_expo;
	}
	public BigDecimal getR177_margin_pro() {
		return r177_margin_pro;
	}
	public void setR177_margin_pro(BigDecimal r177_margin_pro) {
		this.r177_margin_pro = r177_margin_pro;
	}
	public BigDecimal getR177_book_expo() {
		return r177_book_expo;
	}
	public void setR177_book_expo(BigDecimal r177_book_expo) {
		this.r177_book_expo = r177_book_expo;
	}
	public BigDecimal getR177_ccf_cont() {
		return r177_ccf_cont;
	}
	public void setR177_ccf_cont(BigDecimal r177_ccf_cont) {
		this.r177_ccf_cont = r177_ccf_cont;
	}
	public BigDecimal getR177_equiv_value() {
		return r177_equiv_value;
	}
	public void setR177_equiv_value(BigDecimal r177_equiv_value) {
		this.r177_equiv_value = r177_equiv_value;
	}
	public BigDecimal getR177_rw_obligant() {
		return r177_rw_obligant;
	}
	public void setR177_rw_obligant(BigDecimal r177_rw_obligant) {
		this.r177_rw_obligant = r177_rw_obligant;
	}
	public BigDecimal getR177_rav() {
		return r177_rav;
	}
	public void setR177_rav(BigDecimal r177_rav) {
		this.r177_rav = r177_rav;
	}
	public String getR178_product() {
		return r178_product;
	}
	public void setR178_product(String r178_product) {
		this.r178_product = r178_product;
	}
	public String getR178_client_grp() {
		return r178_client_grp;
	}
	public void setR178_client_grp(String r178_client_grp) {
		this.r178_client_grp = r178_client_grp;
	}
	public BigDecimal getR178_total_book_expo() {
		return r178_total_book_expo;
	}
	public void setR178_total_book_expo(BigDecimal r178_total_book_expo) {
		this.r178_total_book_expo = r178_total_book_expo;
	}
	public BigDecimal getR178_margin_pro() {
		return r178_margin_pro;
	}
	public void setR178_margin_pro(BigDecimal r178_margin_pro) {
		this.r178_margin_pro = r178_margin_pro;
	}
	public BigDecimal getR178_book_expo() {
		return r178_book_expo;
	}
	public void setR178_book_expo(BigDecimal r178_book_expo) {
		this.r178_book_expo = r178_book_expo;
	}
	public BigDecimal getR178_ccf_cont() {
		return r178_ccf_cont;
	}
	public void setR178_ccf_cont(BigDecimal r178_ccf_cont) {
		this.r178_ccf_cont = r178_ccf_cont;
	}
	public BigDecimal getR178_equiv_value() {
		return r178_equiv_value;
	}
	public void setR178_equiv_value(BigDecimal r178_equiv_value) {
		this.r178_equiv_value = r178_equiv_value;
	}
	public BigDecimal getR178_rw_obligant() {
		return r178_rw_obligant;
	}
	public void setR178_rw_obligant(BigDecimal r178_rw_obligant) {
		this.r178_rw_obligant = r178_rw_obligant;
	}
	public BigDecimal getR178_rav() {
		return r178_rav;
	}
	public void setR178_rav(BigDecimal r178_rav) {
		this.r178_rav = r178_rav;
	}
	public String getR179_product() {
		return r179_product;
	}
	public void setR179_product(String r179_product) {
		this.r179_product = r179_product;
	}
	public String getR179_client_grp() {
		return r179_client_grp;
	}
	public void setR179_client_grp(String r179_client_grp) {
		this.r179_client_grp = r179_client_grp;
	}
	public BigDecimal getR179_total_book_expo() {
		return r179_total_book_expo;
	}
	public void setR179_total_book_expo(BigDecimal r179_total_book_expo) {
		this.r179_total_book_expo = r179_total_book_expo;
	}
	public BigDecimal getR179_margin_pro() {
		return r179_margin_pro;
	}
	public void setR179_margin_pro(BigDecimal r179_margin_pro) {
		this.r179_margin_pro = r179_margin_pro;
	}
	public BigDecimal getR179_book_expo() {
		return r179_book_expo;
	}
	public void setR179_book_expo(BigDecimal r179_book_expo) {
		this.r179_book_expo = r179_book_expo;
	}
	public BigDecimal getR179_ccf_cont() {
		return r179_ccf_cont;
	}
	public void setR179_ccf_cont(BigDecimal r179_ccf_cont) {
		this.r179_ccf_cont = r179_ccf_cont;
	}
	public BigDecimal getR179_equiv_value() {
		return r179_equiv_value;
	}
	public void setR179_equiv_value(BigDecimal r179_equiv_value) {
		this.r179_equiv_value = r179_equiv_value;
	}
	public BigDecimal getR179_rw_obligant() {
		return r179_rw_obligant;
	}
	public void setR179_rw_obligant(BigDecimal r179_rw_obligant) {
		this.r179_rw_obligant = r179_rw_obligant;
	}
	public BigDecimal getR179_rav() {
		return r179_rav;
	}
	public void setR179_rav(BigDecimal r179_rav) {
		this.r179_rav = r179_rav;
	}
	public String getR180_product() {
		return r180_product;
	}
	public void setR180_product(String r180_product) {
		this.r180_product = r180_product;
	}
	public String getR180_client_grp() {
		return r180_client_grp;
	}
	public void setR180_client_grp(String r180_client_grp) {
		this.r180_client_grp = r180_client_grp;
	}
	public BigDecimal getR180_total_book_expo() {
		return r180_total_book_expo;
	}
	public void setR180_total_book_expo(BigDecimal r180_total_book_expo) {
		this.r180_total_book_expo = r180_total_book_expo;
	}
	public BigDecimal getR180_margin_pro() {
		return r180_margin_pro;
	}
	public void setR180_margin_pro(BigDecimal r180_margin_pro) {
		this.r180_margin_pro = r180_margin_pro;
	}
	public BigDecimal getR180_book_expo() {
		return r180_book_expo;
	}
	public void setR180_book_expo(BigDecimal r180_book_expo) {
		this.r180_book_expo = r180_book_expo;
	}
	public BigDecimal getR180_ccf_cont() {
		return r180_ccf_cont;
	}
	public void setR180_ccf_cont(BigDecimal r180_ccf_cont) {
		this.r180_ccf_cont = r180_ccf_cont;
	}
	public BigDecimal getR180_equiv_value() {
		return r180_equiv_value;
	}
	public void setR180_equiv_value(BigDecimal r180_equiv_value) {
		this.r180_equiv_value = r180_equiv_value;
	}
	public BigDecimal getR180_rw_obligant() {
		return r180_rw_obligant;
	}
	public void setR180_rw_obligant(BigDecimal r180_rw_obligant) {
		this.r180_rw_obligant = r180_rw_obligant;
	}
	public BigDecimal getR180_rav() {
		return r180_rav;
	}
	public void setR180_rav(BigDecimal r180_rav) {
		this.r180_rav = r180_rav;
	}
	public String getR181_product() {
		return r181_product;
	}
	public void setR181_product(String r181_product) {
		this.r181_product = r181_product;
	}
	public String getR181_client_grp() {
		return r181_client_grp;
	}
	public void setR181_client_grp(String r181_client_grp) {
		this.r181_client_grp = r181_client_grp;
	}
	public BigDecimal getR181_total_book_expo() {
		return r181_total_book_expo;
	}
	public void setR181_total_book_expo(BigDecimal r181_total_book_expo) {
		this.r181_total_book_expo = r181_total_book_expo;
	}
	public BigDecimal getR181_margin_pro() {
		return r181_margin_pro;
	}
	public void setR181_margin_pro(BigDecimal r181_margin_pro) {
		this.r181_margin_pro = r181_margin_pro;
	}
	public BigDecimal getR181_book_expo() {
		return r181_book_expo;
	}
	public void setR181_book_expo(BigDecimal r181_book_expo) {
		this.r181_book_expo = r181_book_expo;
	}
	public BigDecimal getR181_ccf_cont() {
		return r181_ccf_cont;
	}
	public void setR181_ccf_cont(BigDecimal r181_ccf_cont) {
		this.r181_ccf_cont = r181_ccf_cont;
	}
	public BigDecimal getR181_equiv_value() {
		return r181_equiv_value;
	}
	public void setR181_equiv_value(BigDecimal r181_equiv_value) {
		this.r181_equiv_value = r181_equiv_value;
	}
	public BigDecimal getR181_rw_obligant() {
		return r181_rw_obligant;
	}
	public void setR181_rw_obligant(BigDecimal r181_rw_obligant) {
		this.r181_rw_obligant = r181_rw_obligant;
	}
	public BigDecimal getR181_rav() {
		return r181_rav;
	}
	public void setR181_rav(BigDecimal r181_rav) {
		this.r181_rav = r181_rav;
	}
	public String getR182_product() {
		return r182_product;
	}
	public void setR182_product(String r182_product) {
		this.r182_product = r182_product;
	}
	public String getR182_client_grp() {
		return r182_client_grp;
	}
	public void setR182_client_grp(String r182_client_grp) {
		this.r182_client_grp = r182_client_grp;
	}
	public BigDecimal getR182_total_book_expo() {
		return r182_total_book_expo;
	}
	public void setR182_total_book_expo(BigDecimal r182_total_book_expo) {
		this.r182_total_book_expo = r182_total_book_expo;
	}
	public BigDecimal getR182_margin_pro() {
		return r182_margin_pro;
	}
	public void setR182_margin_pro(BigDecimal r182_margin_pro) {
		this.r182_margin_pro = r182_margin_pro;
	}
	public BigDecimal getR182_book_expo() {
		return r182_book_expo;
	}
	public void setR182_book_expo(BigDecimal r182_book_expo) {
		this.r182_book_expo = r182_book_expo;
	}
	public BigDecimal getR182_ccf_cont() {
		return r182_ccf_cont;
	}
	public void setR182_ccf_cont(BigDecimal r182_ccf_cont) {
		this.r182_ccf_cont = r182_ccf_cont;
	}
	public BigDecimal getR182_equiv_value() {
		return r182_equiv_value;
	}
	public void setR182_equiv_value(BigDecimal r182_equiv_value) {
		this.r182_equiv_value = r182_equiv_value;
	}
	public BigDecimal getR182_rw_obligant() {
		return r182_rw_obligant;
	}
	public void setR182_rw_obligant(BigDecimal r182_rw_obligant) {
		this.r182_rw_obligant = r182_rw_obligant;
	}
	public BigDecimal getR182_rav() {
		return r182_rav;
	}
	public void setR182_rav(BigDecimal r182_rav) {
		this.r182_rav = r182_rav;
	}
	public String getR183_product() {
		return r183_product;
	}
	public void setR183_product(String r183_product) {
		this.r183_product = r183_product;
	}
	public String getR183_client_grp() {
		return r183_client_grp;
	}
	public void setR183_client_grp(String r183_client_grp) {
		this.r183_client_grp = r183_client_grp;
	}
	public BigDecimal getR183_total_book_expo() {
		return r183_total_book_expo;
	}
	public void setR183_total_book_expo(BigDecimal r183_total_book_expo) {
		this.r183_total_book_expo = r183_total_book_expo;
	}
	public BigDecimal getR183_margin_pro() {
		return r183_margin_pro;
	}
	public void setR183_margin_pro(BigDecimal r183_margin_pro) {
		this.r183_margin_pro = r183_margin_pro;
	}
	public BigDecimal getR183_book_expo() {
		return r183_book_expo;
	}
	public void setR183_book_expo(BigDecimal r183_book_expo) {
		this.r183_book_expo = r183_book_expo;
	}
	public BigDecimal getR183_ccf_cont() {
		return r183_ccf_cont;
	}
	public void setR183_ccf_cont(BigDecimal r183_ccf_cont) {
		this.r183_ccf_cont = r183_ccf_cont;
	}
	public BigDecimal getR183_equiv_value() {
		return r183_equiv_value;
	}
	public void setR183_equiv_value(BigDecimal r183_equiv_value) {
		this.r183_equiv_value = r183_equiv_value;
	}
	public BigDecimal getR183_rw_obligant() {
		return r183_rw_obligant;
	}
	public void setR183_rw_obligant(BigDecimal r183_rw_obligant) {
		this.r183_rw_obligant = r183_rw_obligant;
	}
	public BigDecimal getR183_rav() {
		return r183_rav;
	}
	public void setR183_rav(BigDecimal r183_rav) {
		this.r183_rav = r183_rav;
	}
	public String getR184_product() {
		return r184_product;
	}
	public void setR184_product(String r184_product) {
		this.r184_product = r184_product;
	}
	public String getR184_client_grp() {
		return r184_client_grp;
	}
	public void setR184_client_grp(String r184_client_grp) {
		this.r184_client_grp = r184_client_grp;
	}
	public BigDecimal getR184_total_book_expo() {
		return r184_total_book_expo;
	}
	public void setR184_total_book_expo(BigDecimal r184_total_book_expo) {
		this.r184_total_book_expo = r184_total_book_expo;
	}
	public BigDecimal getR184_margin_pro() {
		return r184_margin_pro;
	}
	public void setR184_margin_pro(BigDecimal r184_margin_pro) {
		this.r184_margin_pro = r184_margin_pro;
	}
	public BigDecimal getR184_book_expo() {
		return r184_book_expo;
	}
	public void setR184_book_expo(BigDecimal r184_book_expo) {
		this.r184_book_expo = r184_book_expo;
	}
	public BigDecimal getR184_ccf_cont() {
		return r184_ccf_cont;
	}
	public void setR184_ccf_cont(BigDecimal r184_ccf_cont) {
		this.r184_ccf_cont = r184_ccf_cont;
	}
	public BigDecimal getR184_equiv_value() {
		return r184_equiv_value;
	}
	public void setR184_equiv_value(BigDecimal r184_equiv_value) {
		this.r184_equiv_value = r184_equiv_value;
	}
	public BigDecimal getR184_rw_obligant() {
		return r184_rw_obligant;
	}
	public void setR184_rw_obligant(BigDecimal r184_rw_obligant) {
		this.r184_rw_obligant = r184_rw_obligant;
	}
	public BigDecimal getR184_rav() {
		return r184_rav;
	}
	public void setR184_rav(BigDecimal r184_rav) {
		this.r184_rav = r184_rav;
	}
	public String getR185_product() {
		return r185_product;
	}
	public void setR185_product(String r185_product) {
		this.r185_product = r185_product;
	}
	public String getR185_client_grp() {
		return r185_client_grp;
	}
	public void setR185_client_grp(String r185_client_grp) {
		this.r185_client_grp = r185_client_grp;
	}
	public BigDecimal getR185_total_book_expo() {
		return r185_total_book_expo;
	}
	public void setR185_total_book_expo(BigDecimal r185_total_book_expo) {
		this.r185_total_book_expo = r185_total_book_expo;
	}
	public BigDecimal getR185_margin_pro() {
		return r185_margin_pro;
	}
	public void setR185_margin_pro(BigDecimal r185_margin_pro) {
		this.r185_margin_pro = r185_margin_pro;
	}
	public BigDecimal getR185_book_expo() {
		return r185_book_expo;
	}
	public void setR185_book_expo(BigDecimal r185_book_expo) {
		this.r185_book_expo = r185_book_expo;
	}
	public BigDecimal getR185_ccf_cont() {
		return r185_ccf_cont;
	}
	public void setR185_ccf_cont(BigDecimal r185_ccf_cont) {
		this.r185_ccf_cont = r185_ccf_cont;
	}
	public BigDecimal getR185_equiv_value() {
		return r185_equiv_value;
	}
	public void setR185_equiv_value(BigDecimal r185_equiv_value) {
		this.r185_equiv_value = r185_equiv_value;
	}
	public BigDecimal getR185_rw_obligant() {
		return r185_rw_obligant;
	}
	public void setR185_rw_obligant(BigDecimal r185_rw_obligant) {
		this.r185_rw_obligant = r185_rw_obligant;
	}
	public BigDecimal getR185_rav() {
		return r185_rav;
	}
	public void setR185_rav(BigDecimal r185_rav) {
		this.r185_rav = r185_rav;
	}
	public String getR186_product() {
		return r186_product;
	}
	public void setR186_product(String r186_product) {
		this.r186_product = r186_product;
	}
	public String getR186_client_grp() {
		return r186_client_grp;
	}
	public void setR186_client_grp(String r186_client_grp) {
		this.r186_client_grp = r186_client_grp;
	}
	public BigDecimal getR186_total_book_expo() {
		return r186_total_book_expo;
	}
	public void setR186_total_book_expo(BigDecimal r186_total_book_expo) {
		this.r186_total_book_expo = r186_total_book_expo;
	}
	public BigDecimal getR186_margin_pro() {
		return r186_margin_pro;
	}
	public void setR186_margin_pro(BigDecimal r186_margin_pro) {
		this.r186_margin_pro = r186_margin_pro;
	}
	public BigDecimal getR186_book_expo() {
		return r186_book_expo;
	}
	public void setR186_book_expo(BigDecimal r186_book_expo) {
		this.r186_book_expo = r186_book_expo;
	}
	public BigDecimal getR186_ccf_cont() {
		return r186_ccf_cont;
	}
	public void setR186_ccf_cont(BigDecimal r186_ccf_cont) {
		this.r186_ccf_cont = r186_ccf_cont;
	}
	public BigDecimal getR186_equiv_value() {
		return r186_equiv_value;
	}
	public void setR186_equiv_value(BigDecimal r186_equiv_value) {
		this.r186_equiv_value = r186_equiv_value;
	}
	public BigDecimal getR186_rw_obligant() {
		return r186_rw_obligant;
	}
	public void setR186_rw_obligant(BigDecimal r186_rw_obligant) {
		this.r186_rw_obligant = r186_rw_obligant;
	}
	public BigDecimal getR186_rav() {
		return r186_rav;
	}
	public void setR186_rav(BigDecimal r186_rav) {
		this.r186_rav = r186_rav;
	}
	public String getR187_product() {
		return r187_product;
	}
	public void setR187_product(String r187_product) {
		this.r187_product = r187_product;
	}
	public String getR187_client_grp() {
		return r187_client_grp;
	}
	public void setR187_client_grp(String r187_client_grp) {
		this.r187_client_grp = r187_client_grp;
	}
	public BigDecimal getR187_total_book_expo() {
		return r187_total_book_expo;
	}
	public void setR187_total_book_expo(BigDecimal r187_total_book_expo) {
		this.r187_total_book_expo = r187_total_book_expo;
	}
	public BigDecimal getR187_margin_pro() {
		return r187_margin_pro;
	}
	public void setR187_margin_pro(BigDecimal r187_margin_pro) {
		this.r187_margin_pro = r187_margin_pro;
	}
	public BigDecimal getR187_book_expo() {
		return r187_book_expo;
	}
	public void setR187_book_expo(BigDecimal r187_book_expo) {
		this.r187_book_expo = r187_book_expo;
	}
	public BigDecimal getR187_ccf_cont() {
		return r187_ccf_cont;
	}
	public void setR187_ccf_cont(BigDecimal r187_ccf_cont) {
		this.r187_ccf_cont = r187_ccf_cont;
	}
	public BigDecimal getR187_equiv_value() {
		return r187_equiv_value;
	}
	public void setR187_equiv_value(BigDecimal r187_equiv_value) {
		this.r187_equiv_value = r187_equiv_value;
	}
	public BigDecimal getR187_rw_obligant() {
		return r187_rw_obligant;
	}
	public void setR187_rw_obligant(BigDecimal r187_rw_obligant) {
		this.r187_rw_obligant = r187_rw_obligant;
	}
	public BigDecimal getR187_rav() {
		return r187_rav;
	}
	public void setR187_rav(BigDecimal r187_rav) {
		this.r187_rav = r187_rav;
	}
	public String getR188_product() {
		return r188_product;
	}
	public void setR188_product(String r188_product) {
		this.r188_product = r188_product;
	}
	public String getR188_client_grp() {
		return r188_client_grp;
	}
	public void setR188_client_grp(String r188_client_grp) {
		this.r188_client_grp = r188_client_grp;
	}
	public BigDecimal getR188_total_book_expo() {
		return r188_total_book_expo;
	}
	public void setR188_total_book_expo(BigDecimal r188_total_book_expo) {
		this.r188_total_book_expo = r188_total_book_expo;
	}
	public BigDecimal getR188_margin_pro() {
		return r188_margin_pro;
	}
	public void setR188_margin_pro(BigDecimal r188_margin_pro) {
		this.r188_margin_pro = r188_margin_pro;
	}
	public BigDecimal getR188_book_expo() {
		return r188_book_expo;
	}
	public void setR188_book_expo(BigDecimal r188_book_expo) {
		this.r188_book_expo = r188_book_expo;
	}
	public BigDecimal getR188_ccf_cont() {
		return r188_ccf_cont;
	}
	public void setR188_ccf_cont(BigDecimal r188_ccf_cont) {
		this.r188_ccf_cont = r188_ccf_cont;
	}
	public BigDecimal getR188_equiv_value() {
		return r188_equiv_value;
	}
	public void setR188_equiv_value(BigDecimal r188_equiv_value) {
		this.r188_equiv_value = r188_equiv_value;
	}
	public BigDecimal getR188_rw_obligant() {
		return r188_rw_obligant;
	}
	public void setR188_rw_obligant(BigDecimal r188_rw_obligant) {
		this.r188_rw_obligant = r188_rw_obligant;
	}
	public BigDecimal getR188_rav() {
		return r188_rav;
	}
	public void setR188_rav(BigDecimal r188_rav) {
		this.r188_rav = r188_rav;
	}
	public String getR189_product() {
		return r189_product;
	}
	public void setR189_product(String r189_product) {
		this.r189_product = r189_product;
	}
	public String getR189_client_grp() {
		return r189_client_grp;
	}
	public void setR189_client_grp(String r189_client_grp) {
		this.r189_client_grp = r189_client_grp;
	}
	public BigDecimal getR189_total_book_expo() {
		return r189_total_book_expo;
	}
	public void setR189_total_book_expo(BigDecimal r189_total_book_expo) {
		this.r189_total_book_expo = r189_total_book_expo;
	}
	public BigDecimal getR189_margin_pro() {
		return r189_margin_pro;
	}
	public void setR189_margin_pro(BigDecimal r189_margin_pro) {
		this.r189_margin_pro = r189_margin_pro;
	}
	public BigDecimal getR189_book_expo() {
		return r189_book_expo;
	}
	public void setR189_book_expo(BigDecimal r189_book_expo) {
		this.r189_book_expo = r189_book_expo;
	}
	public BigDecimal getR189_ccf_cont() {
		return r189_ccf_cont;
	}
	public void setR189_ccf_cont(BigDecimal r189_ccf_cont) {
		this.r189_ccf_cont = r189_ccf_cont;
	}
	public BigDecimal getR189_equiv_value() {
		return r189_equiv_value;
	}
	public void setR189_equiv_value(BigDecimal r189_equiv_value) {
		this.r189_equiv_value = r189_equiv_value;
	}
	public BigDecimal getR189_rw_obligant() {
		return r189_rw_obligant;
	}
	public void setR189_rw_obligant(BigDecimal r189_rw_obligant) {
		this.r189_rw_obligant = r189_rw_obligant;
	}
	public BigDecimal getR189_rav() {
		return r189_rav;
	}
	public void setR189_rav(BigDecimal r189_rav) {
		this.r189_rav = r189_rav;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public OFF_BS_ITEMS_Archival_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
		
		
		
		
		
}
