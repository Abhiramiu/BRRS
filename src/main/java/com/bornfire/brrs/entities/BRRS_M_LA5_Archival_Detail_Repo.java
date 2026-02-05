package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_LA5_Archival_Detail_Repo extends JpaRepository<M_LA5_Archival_Detail_Entity, String>{
	

	@Query(value = "select * from BRRS_M_LA5_ARCHIVALTABLE_DETAIL where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
	List<M_LA5_Archival_Detail_Entity> getdatabydateList(Date reportdate,String DATA_ENTRY_VERSION);
	
	@Query(value = "select * from BRRS_M_LA5_ARCHIVALTABLE_DETAIL where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3 AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
	List<M_LA5_Archival_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate,String DATA_ENTRY_VERSION);

	
	
}
