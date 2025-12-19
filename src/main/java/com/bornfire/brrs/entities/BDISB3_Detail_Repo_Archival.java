package com.bornfire.brrs.entities;



import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BDISB3_Detail_Repo_Archival extends JpaRepository<BDISB3_Detail_Entity_Archival,String> {

	@Query(value = "select * from BRRS_BDISB3_ARCHIVALTABLE_DETAIL where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
	List<BDISB3_Detail_Entity_Archival> getdatabydateList(Date reportdate,String DATA_ENTRY_VERSION);
	
	@Query(value = "select * from BRRS_BDISB3_ARCHIVALTABLE_DETAIL where REPORT_LABLE =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3 AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
	List<BDISB3_Detail_Entity_Archival> GetDataByRowIdAndColumnId(String reportLable,String reportAddlCriteria_1,Date reportdate,String DATA_ENTRY_VERSION);

}
