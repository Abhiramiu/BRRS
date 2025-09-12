package com.bornfire.brrs.entities;

import java.util.Date;

public class AIDPUpdateDto {

	
	private Date REPORT_DATE;

    private BRRS_M_AIDP_Summary_Entity1 entity1;
    private BRRS_M_AIDP_Summary_Entity2 entity2;
    private BRRS_M_AIDP_Summary_Entity3 entity3;
    private BRRS_M_AIDP_Summary_Entity4 entity4;
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public BRRS_M_AIDP_Summary_Entity1 getEntity1() {
		return entity1;
	}
	public void setEntity1(BRRS_M_AIDP_Summary_Entity1 entity1) {
		this.entity1 = entity1;
	}
	public BRRS_M_AIDP_Summary_Entity2 getEntity2() {
		return entity2;
	}
	public void setEntity2(BRRS_M_AIDP_Summary_Entity2 entity2) {
		this.entity2 = entity2;
	}
	public BRRS_M_AIDP_Summary_Entity3 getEntity3() {
		return entity3;
	}
	public void setEntity3(BRRS_M_AIDP_Summary_Entity3 entity3) {
		this.entity3 = entity3;
	}
	public BRRS_M_AIDP_Summary_Entity4 getEntity4() {

		return entity4;
	}
	public void setEntity4(BRRS_M_AIDP_Summary_Entity4 entity4) {
		this.entity4 = entity4;
	}
	public AIDPUpdateDto(Date rEPORT_DATE, BRRS_M_AIDP_Summary_Entity1 entity1, BRRS_M_AIDP_Summary_Entity2 entity2,
			BRRS_M_AIDP_Summary_Entity3 entity3, BRRS_M_AIDP_Summary_Entity4 entity4) {
		super();
		REPORT_DATE = rEPORT_DATE;
		this.entity1 = entity1;
		this.entity2 = entity2;
		this.entity3 = entity3;
		this.entity4 = entity4;
	}
	public AIDPUpdateDto() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
	
}

