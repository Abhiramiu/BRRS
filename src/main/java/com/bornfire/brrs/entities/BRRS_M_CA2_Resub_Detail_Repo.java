package com.bornfire.brrs.entities;
import java.util.Date;





import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;






public interface BRRS_M_CA2_Resub_Detail_Repo extends JpaRepository<M_CA2_RESUB_Detail_Entity, String> {

	
	@Query(value = "select * from BRRS_M_CA2_RESUB_DETAILTABLE where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
	List<M_CA2_RESUB_Detail_Entity> getdatabydateList(Date reportdate,String DATA_ENTRY_VERSION);
	
	@Query(value = "select * from BRRS_M_CA2_RESUB_DETAILTABLE where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3 AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
	List<M_CA2_RESUB_Detail_Entity> GetDataByRowIdAndColumnId(String reportLable,String reportAddlCriteria_1,Date reportdate,String DATA_ENTRY_VERSION);
}