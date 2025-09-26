package com.bornfire.brrs.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_MCBL_MAIN")
public class MCBL_Main_Entity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "gl_code")
    private String gl_code;

    @Column(name = "gl_sub_code")
    private String gl_sub_code;

    @Column(name = "head_acc_no")
    private String head_acc_no;

    @Column(name = "currency")
    private String currency;

    private String create_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date create_date;
    private String modify_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date modify_date;
    private String modify_flg;
    private String del_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date del_date;
    private String del_flg;
    
    
    public String getDel_user() {
		return del_user;
	}
	public void setDel_user(String del_user) {
		this.del_user = del_user;
	}
	public Date getDel_date() {
		return del_date;
	}
	public void setDel_date(Date del_date) {
		this.del_date = del_date;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	
    
    public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	

    // 🔹 Getters & Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getGl_code() {
        return gl_code;
    }
    public void setGl_code(String gl_code) {
        this.gl_code = gl_code;
    }

    public String getGl_sub_code() {
        return gl_sub_code;
    }
    public void setGl_sub_code(String gl_sub_code) {
        this.gl_sub_code = gl_sub_code;
    }

    public String getHead_acc_no() {
        return head_acc_no;
    }
    public void setHead_acc_no(String head_acc_no) {
        this.head_acc_no = head_acc_no;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
