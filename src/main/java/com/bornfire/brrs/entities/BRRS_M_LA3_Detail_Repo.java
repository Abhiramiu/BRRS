package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

	public interface BRRS_M_LA3_Detail_Repo extends JpaRepository<M_LA3_Detail_Entity, Date> {

		@Query(value = "select * from BRRS_M_LA3_DETAILTABLE", nativeQuery = true)
		List<M_LA3_Detail_Entity> getdatabydateList(Date reportdate);
		
		/*
		 * @Query(value =
		 * "select * from BRRS_M_LA3_DETAILTABLE WHERE REPORT_DATE =?1 and REPORT_ADDL_CRITERIA_1 =?2 and REPORT_ADDL_CRITERIA_2 =?3 and REPORT_ADDL_CRITERIA_3 =?4 and REPORT_LABEL=?4 "
		 * , nativeQuery = true) List<M_LA3_Detail_Entity> getdatabydateListrow(Date
		 * reportdate,String REPORT_ADDL_CRITERIA_1,String REPORT_LABEL);
		 */
		
		@Query(value = "select * from BRRS_M_LA3_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
		List<M_LA3_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);
		
		@Query(value = "select count(*) from BRRS_M_LA3_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
		int getdatacount(Date reportdate);
		
		@Query(value = "SELECT * FROM BRRS_M_LA3_DETAILTABLE " +
	            "WHERE report_label = :rowId " +
	            "AND ( :columnId IS NULL OR :columnId = '' OR report_addl_criteria_1 = :columnId ) " + // Corrected Line
	            "AND ( :columnId1 IS NULL OR :columnId1 = '' OR report_addl_criteria_2 = :columnId1 ) " +
	            "AND ( :columnId2 IS NULL OR :columnId2 = '' OR report_addl_criteria_3 = :columnId2 ) " +
	            "AND REPORT_DATE = :reportDate",
	        nativeQuery = true)
	    List<M_LA3_Detail_Entity> GetDataByRowIdAndColumnId(
	        @Param("rowId") String rowId,
	        @Param("columnId") String columnId,
	        @Param("columnId1") String columnId1,
	        @Param("columnId2") String columnId2,
	        @Param("reportDate") Date reportDate
	    );

		    @Query(value = "SELECT * FROM BRRS_M_LA3_DETAILTABLE WHERE ACCT_NUMBER = :acct_number", nativeQuery = true)
		    M_LA3_Detail_Entity findByAcctnumber(@Param("acct_number") String acct_number);
			
			
			
	}

	
	
