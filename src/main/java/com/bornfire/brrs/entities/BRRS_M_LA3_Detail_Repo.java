package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

	public interface BRRS_M_LA3_Detail_Repo extends JpaRepository<M_LA3_Detail_Entity, Date> {

		@Query(value = "select * from BRRS_M_LA3_DETAILTABLE", nativeQuery = true)
		List<M_LA3_Detail_Entity> getdatabydateList(Date reportdate);
		
		@Query(value = "select * from BRRS_M_LA3_DETAILTABLE WHERE REPORT_DATE =?1 and REPORT_ADDL_CRITERIA_1 =?2 and REPORT_ADDL_CRITERIA_2 =?3 and REPORT_ADDL_CRITERIA_3 =?4 and REPORT_LABEL=?4 ", nativeQuery = true)
		List<M_LA3_Detail_Entity> getdatabydateListrow(Date reportdate,String REPORT_ADDL_CRITERIA_1,String REPORT_LABEL);
		
		@Query(value = "select * from BRRS_M_LA3_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
		List<M_LA3_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
		
		@Query(value = "select count(*) from BRRS_M_LA3_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
		int getdatacount(Date reportdate);
	}

	
	
