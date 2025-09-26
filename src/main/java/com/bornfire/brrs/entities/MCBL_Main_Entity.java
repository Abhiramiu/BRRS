package com.bornfire.brrs.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
