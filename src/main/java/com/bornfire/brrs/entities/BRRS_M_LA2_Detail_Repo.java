package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface BRRS_M_LA2_Detail_Repo extends JpaRepository<M_LA2_Detail_Entity, String> {

	@Query(value = "select * from BRRS_M_LA2_DETAILTABLE where REPORT_DATE = ?1 AND REPORT_LABEL= ?2 AND REPORT_ADDL_CRITERIA_1= ?3", nativeQuery = true)
    List<M_LA2_Detail_Entity> findByReportDateAndReportLableAndReportAddlCriteria1(
            Date reportDate,
            String reportLabel,
            String reportAddlCriteria1
    );
	
	@Query(value = "select * from BRRS_M_LA2_DETAILTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	List<M_LA2_Detail_Entity> getdatabydateList(Date reportdate);

	@Query(value = "select * from BRRS_M_LA2_DETAILTABLE  where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 and REPORT_DATE=?3", nativeQuery = true)
	List<M_LA2_Detail_Entity> GetDataByRowIdAndColumnId(String report_label,String report_addl_criteria_1,Date reportdate);
	
	@Query(value = "select * from BRRS_M_LA2_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	List<M_LA2_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
	
	@Query(value = "select count(*) from BRRS_M_LA2_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	int getdatacount(Date reportdate);
		 
	@Query(value = "SELECT * FROM BRRS_M_LA2_DETAILTABLE WHERE ACCT_NUMBER = :acct_number", nativeQuery = true)
	 M_LA2_Detail_Entity findByAcctnumber(@Param("acct_number") String acct_number);
	
	@Query(value = "SELECT * FROM BRRS_M_LA2_DETAILTABLE WHERE SNO = :Sno", nativeQuery = true)
	M_LA2_Detail_Entity findBySno(@Param("Sno") String Sno);

}
    
