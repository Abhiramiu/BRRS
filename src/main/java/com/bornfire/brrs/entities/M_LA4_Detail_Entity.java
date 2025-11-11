package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "BRRS_M_LA4_DETAILTABLE")
public class M_LA4_Detail_Entity {

   

    private String	cust_id;
    @Id
    private String	acct_number;
    private String	acct_name;
    private String	data_type;
    private String	report_label;
    private String	report_addl_criteria1;
    private String	report_addl_criteria2;
    private String	report_addl_criteria3;
    private String	segment;
    private String	facility;
    private BigDecimal	acct_balance_in_pula;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date	report_date;
    private String	report_name;
    private String	create_user;
    private Date	create_time;
    private String	modify_user;
    private Date	modify_time;
    private String	verify_user;
    private Date	verify_time;
    private String	entity_flg;
    private String	modify_flg;
    private String	del_flg;
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getAcct_number() {
		return acct_number;
	}
	public void setAcct_number(String acct_number) {
		this.acct_number = acct_number;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getReport_label() {
		return report_label;
	}
	public void setReport_label(String report_label) {
		this.report_label = report_label;
	}
	public String getReport_addl_criteria1() {
		return report_addl_criteria1;
	}
	public void setReport_addl_criteria1(String report_addl_criteria1) {
		this.report_addl_criteria1 = report_addl_criteria1;
	}
	public String getReport_addl_criteria2() {
		return report_addl_criteria2;
	}
	public void setReport_addl_criteria2(String report_addl_criteria2) {
		this.report_addl_criteria2 = report_addl_criteria2;
	}
	public String getReport_addl_criteria3() {
		return report_addl_criteria3;
	}
	public void setReport_addl_criteria3(String report_addl_criteria3) {
		this.report_addl_criteria3 = report_addl_criteria3;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public BigDecimal getAcct_balance_in_pula() {
		return acct_balance_in_pula;
	}
	public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
		this.acct_balance_in_pula = acct_balance_in_pula;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
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
	public M_LA4_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
    

}