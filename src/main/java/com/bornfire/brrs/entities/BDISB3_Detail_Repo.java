package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BDISB3_Detail_Repo extends JpaRepository<BDISB3_Detail_Entity,String> {

	List<BDISB3_Detail_Entity>
    findByReportDateAndReportLableAndReportAddlCriteria1(
            Date reportDate,
            String reportLable,
            String reportAddlCriteria1
    );
    
    // ✅ Pagination fixed → use OFFSET and LIMIT correctly
    @Query(value = "select * from BRRS_BDISB3_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<BDISB3_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);
    
 // Count rows by date
    @Query(value = "select count(*) from BRRS_BDISB3_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    int getdatacount(Date reportdate);
    
    @Query(value ="select * from BRRS_BDISB3_DETAILTABLE where REPORT_LABLE =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3"
    		  , nativeQuery = true) 
    List<BDISB3_Detail_Entity> GetDataByRowIdAndColumnId(String reportLable,String reportAddlCriteria_1,Date reportdate);
    		  
}
