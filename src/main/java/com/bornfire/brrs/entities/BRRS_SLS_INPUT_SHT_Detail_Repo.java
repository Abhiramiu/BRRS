package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_SLS_INPUT_SHT_Detail_Repo extends JpaRepository<SLS_INPUT_SHT_Detail_Entity,String>  {

	@Query(value = "select * from BRRS_SLS_INPUT_SHT_DETAILTABLE where REPORT_DATE=?1 AND Cust_id is NOT null  offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	 List<SLS_INPUT_SHT_Detail_Entity> slsdetaillist(Date REPORT_DATE,int startpage,int endpage);
	 
	 @Query(value = "select * from BRRS_SLS_INPUT_SHT_DETAILTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	 List<SLS_INPUT_SHT_Detail_Entity> slsdetaillistdate(Date REPORT_DATE);
	 
	 @Query(value = "select * from BRRS_SLS_INPUT_SHT_DETAILTABLE where REPORT_DATE=?1 AND REPORT_LABEL=?2", nativeQuery = true)
	 List<SLS_INPUT_SHT_Detail_Entity> slsdetaillistrowid(Date REPORT_DATE,String REPORT_LABEL);
	 
	@Query(value = "select count(*) from BRRS_SLS_INPUT_SHT_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	 int  slsdetaillistcount(Date REPORT_DATE);
	
	@Query(value = "select count(*) from BRRS_SLS_INPUT_SHT_DETAILTABLE where REPORT_DATE=?1 AND REPORT_LABEL=?2", nativeQuery = true)
	 int  slsdetaillistcountROWID(Date REPORT_DATE,String REPORT_LABEL);
}
