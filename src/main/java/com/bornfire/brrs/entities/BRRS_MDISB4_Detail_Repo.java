package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_MDISB4_Detail_Repo extends JpaRepository<MDISB4_Detail_Entity, String> {

	@Query(value = "select * from BRRS_MDISB4_DETAILTABLE  ", nativeQuery = true)
	List<MDISB4_Detail_Entity> getdatabydateList(Date reportdate);
	
	
	@Query(value = "select * from BRRS_MDISB4_DETAILTABLE where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3", nativeQuery = true)
	List<MDISB4_Detail_Entity> GetDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria_1, Date reportdate);

	
	@Query(value = "select * from BRRS_MDISB4_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	List<MDISB4_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
	
	@Query(value = "select count(*) from BRRS_MDISB4_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	int getdatacount(Date reportdate);
	
	@Query("SELECT m FROM MDISB4_Detail_Entity m WHERE m.acctNumber = :acctNumber")
	MDISB4_Detail_Entity findByAcctNumber(@Param("acctNumber") String acctNumber);
	
}

