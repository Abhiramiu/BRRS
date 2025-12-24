package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_MDISB2_Detail_Repo extends JpaRepository<MDISB2_Detail_Entity, String> {

	@Query(value = "select * from BRRS_MDISB2_DETAILTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	List<MDISB2_Detail_Entity> getdatabydateList(Date reportdate);

	@Query(value = "select * from BRRS_MDISB2_DETAILTABLE  where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 and REPORT_DATE=?3", nativeQuery = true)
	List<MDISB2_Detail_Entity> GetDataByRowIdAndColumnId(String report_label,String report_addl_criteria_1,Date reportdate);
	
	@Query(value = "select * from BRRS_MDISB2_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	List<MDISB2_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
	
	@Query(value = "select count(*) from BRRS_MDISB2_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	int getdatacount(Date reportdate);
	
	@Query(value = "SELECT * FROM BRRS_MDISB2_DETAILTABLE WHERE ACCT_NUMBER = :acct_number", nativeQuery = true)
	 MDISB2_Detail_Entity findByAcctnumber(@Param("acct_number") String acct_number);
	
		 
}

