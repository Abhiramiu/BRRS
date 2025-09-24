package com.bornfire.brrs.entities;

import java.util.Date;

public class LA2UpdateDto {
	
	private Date REPORT_DATE;

    private M_LA2_Summary_Entity entity1;

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}

	public M_LA2_Summary_Entity getEntity1() {
		return entity1;
	}

	public void setEntity1(M_LA2_Summary_Entity entity1) {
		this.entity1 = entity1;
	}

	public LA2UpdateDto(Date rEPORT_DATE, M_LA2_Summary_Entity entity1) {
		super();
		REPORT_DATE = rEPORT_DATE;
		this.entity1 = entity1;
	}

	public LA2UpdateDto() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

}
