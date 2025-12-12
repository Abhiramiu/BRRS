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
@Table(name = "BRRS_FSI_SUMMARYTABLE")
public class FSI_Summary_Entity {


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



public FSI_Summary_Entity() {
    super();
}


}
