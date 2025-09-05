package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_SRWA_12H_Detail_Repo extends JpaRepository<M_SRWA_12H_Detail_Entity, String> {

	@Query(value = "select * from BRRS_M_SRWA_12H_DETAILTABLE where REPORT_DATE =?1  ", nativeQuery = true)
	List<M_SRWA_12H_Detail_Entity> getdatabydateList(Date reportdate);
	
	@Query(value = "select * from BRRS_M_SRWA_12H_DETAILTABLE WHERE REPORT_DATE =?1 and COLUMN_ID =?2 and ROW_ID=?3 ", nativeQuery = true)
	List<M_SRWA_12H_Detail_Entity> GetDataByRowIdAndColumnId( String ROW_ID,String COLUMN_ID,Date reportdate);
	
	@Query(value = "select * from BRRS_M_SRWA_12H_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	List<M_SRWA_12H_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
	
	@Query(value = "select count(*) from BRRS_M_SRWA_12H_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	int getdatacount(Date reportdate);
}
