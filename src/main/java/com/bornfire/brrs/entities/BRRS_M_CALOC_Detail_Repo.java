package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_CALOC_Detail_Repo extends JpaRepository<M_CALOC_Detail_Entity,String>{
	
	 @Query(value = "SELECT * FROM BRRS_M_CALOC_DETAILTABLE WHERE REPORT_DATE = :reportdate", nativeQuery = true)
	 List<M_CALOC_Detail_Entity> getdatabydateList(@Param("reportdate") Date reportdate); 
	 
	 @Query(value = "select * from BRRS_M_CALOC_DETAILTABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3", nativeQuery = true)
	List<M_CALOC_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);
	
	@Query(value = "select * from BRRS_M_CALOC_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	 List<M_CALOC_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
	 
	 @Query(value = "select count(*) from BRRS_M_CALOC_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
		int getdatacount(Date reportdate);
	 
	 
}


