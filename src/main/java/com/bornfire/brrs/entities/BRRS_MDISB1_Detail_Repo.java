package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_MDISB1_Detail_Repo extends JpaRepository<MDISB1_Detail_Entity, String> {

	@Query(value = "select * from BRRS_MDISB1_DETAILTABLE  ", nativeQuery = true)
	List<MDISB1_Detail_Entity> getdatabydateList(Date reportdate);

	@Query(value = "select * from BRRS_MDISB1_DETAILTABLE  where ROW_ID =?1 and COLUMN_ID=?2 and REPORT_DATE=?3", nativeQuery = true)
	List<MDISB1_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);
	
	@Query(value = "select * from BRRS_MDISB1_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	List<MDISB1_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
	
	@Query(value = "select count(*) from BRRS_MDISB1_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	int getdatacount(Date reportdate);
	
	@Query("SELECT m FROM MDISB1_Detail_Entity m WHERE m.acct_number = :acctNumber")
	MDISB1_Detail_Entity findByAcctNumber(@Param("acctNumber") String acctNumber);
	
}

