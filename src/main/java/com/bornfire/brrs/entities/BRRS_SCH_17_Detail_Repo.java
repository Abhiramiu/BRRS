package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BRRS_SCH_17_Detail_Repo extends JpaRepository<SCH_17_Detail_Entity, String> {

	
	@Query(value = "select * from BRRS_SCH_17_DETAILTABLE where REPORT_DATE = ?1 AND REPORT_LABEL= ?2 AND REPORT_ADDL_CRITERIA_1= ?3", nativeQuery = true)
    List<SCH_17_Detail_Entity> findByReportDateAndReportLableAndReportAddlCriteria1(
            Date reportDate,
            String reportLabel,
            String reportAddlCriteria1
    );
	
	
	// Fetch all records for a given date

	@Query(value = "select * from BRRS_SCH_17_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
	List<SCH_17_Detail_Entity> getdatabydateList(Date reportdate);

	// ✅ Pagination fixed → use OFFSET and LIMIT correctly

	@Query(value = "select * from BRRS_SCH_17_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	List<SCH_17_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);

	// Count rows by date

	@Query(value = "select count(*) from BRRS_SCH_17_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
	int getdatacount(Date reportdate);

	@Query(value = "select * from BRRS_SCH_17_DETAILTABLE where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3", nativeQuery = true)
	List<SCH_17_Detail_Entity> GetDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria_1, Date reportdate);

	@Query(value = "SELECT * FROM BRRS_SCH_17_DETAILTABLE WHERE ACCT_NUMBER = :acct_number", nativeQuery = true)
	SCH_17_Detail_Entity findByAcctnumber(@Param("acct_number") String acct_number);

}
