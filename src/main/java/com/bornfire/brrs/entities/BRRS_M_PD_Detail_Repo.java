////package com.bornfire.brrs.entities;
////
////import java.util.Date;
////import java.util.List;
////
////import org.springframework.data.jpa.repository.JpaRepository;
////import org.springframework.data.jpa.repository.Query;
////
////public interface BRRS_M_PD_Detail_Repo extends JpaRepository<M_PD_Detail_Entity, String> {
////    
////	
////	   @Query(value = "select * from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
////	   List<M_PD_Detail_Entity> getdatabydateList(Date reportdate);
////
////	   @Query(value = "select * from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
////	   List<M_PD_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);
////
////	   @Query(value = "select count(*) from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
////	   int getdatacount(Date reportdate);
////	 
////		@Query(value = "select * from BRRS_M_PD_DETAILTABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3", nativeQuery = true)
////		List<M_PD_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);
////}
//
//
//
//package com.bornfire.brrs.entities;
//import java.util.Date;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface BRRS_M_PD_Detail_Repo extends JpaRepository<M_PD_Detail_Entity, String> {
//
//	// Fetch all records for a given date
// @Query(value = "select * from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
// List<M_PD_Detail_Entity> getdatabydateList(Date reportdate);
// 
// // ✅ Pagination fixed → use OFFSET and LIMIT correctly
// @Query(value = "select * from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
// List<M_PD_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);
// 
// // Count rows by date
// @Query(value = "select count(*) from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
// int getdatacount(Date reportdate);
// 
//	  @Query(value =
//	  "select * from BRRS_M_PD_DETAILTABLE where REPORT_LABLE =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3"
//	  , nativeQuery = true) List<M_PD_Detail_Entity>
//	  GetDataByRowIdAndColumnId(String reportLable,String reportAddlCriteria_1,Date reportdate);
//	  
//	  @Query(value = "SELECT * FROM BRRS_M_PD_DETAILTABLE WHERE ACCT_NUMBER = :acctNumber", nativeQuery = true)
//	    M_PD_Detail_Entity findByAcctnumber(@Param("acctNumber") String acctNumber);
//		
//
//	
//}
//
//
//
//
//

package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface BRRS_M_PD_Detail_Repo extends JpaRepository<M_PD_Detail_Entity, String> {

    // Fetch all records for a given date
    @Query(value = "select * from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    List<M_PD_Detail_Entity> getdatabydateList(Date reportdate);

    // ✅ Pagination fixed → use OFFSET and LIMIT correctly
    @Query(value = "select * from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<M_PD_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);

    // Count rows by date
    @Query(value = "select count(*) from BRRS_M_PD_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    int getdatacount(Date reportdate);

 // In your BRRS_M_PD_Detail_Repo.java file

    @Query(value = "SELECT * FROM BRRS_M_PD_DETAILTABLE " +
            "WHERE REPORT_LABLE = :reportLable " +
            "AND ( :reportAddlCriteria1 IS NULL OR :reportAddlCriteria1 = '' OR REPORT_ADDL_CRITERIA_1 = :reportAddlCriteria1 ) " +
            "AND ( :reportAddlCriteria2 IS NULL OR :reportAddlCriteria2 = '' OR REPORT_ADDL_CRITERIA_2 = :reportAddlCriteria2 ) " +
            "AND ( :reportAddlCriteria3 IS NULL OR :reportAddlCriteria3 = '' OR REPORT_ADDL_CRITERIA_3 = :reportAddlCriteria3 ) " +
            "AND ( :reportAddlCriteria4 IS NULL OR :reportAddlCriteria4 = '' OR REPORT_ADDL_CRITERIA_4 = :reportAddlCriteria4 ) " +
            "AND REPORT_DATE = :reportDate",
            nativeQuery = true)
    List<M_PD_Detail_Entity> GetDataByRowIdAndColumnId(
            @Param("reportLable") String reportLable,
            @Param("reportAddlCriteria1") String reportAddlCriteria1,
            @Param("reportAddlCriteria2") String reportAddlCriteria2,
            @Param("reportAddlCriteria3") String reportAddlCriteria3,
            @Param("reportAddlCriteria4") String reportAddlCriteria4,
            @Param("reportDate") Date reportDate
    );



    @Query(value = "SELECT * FROM BRRS_M_PD_DETAILTABLE WHERE ACCT_NUMBER = :acctNumber", nativeQuery = true)
    M_PD_Detail_Entity findByAcctnumber(@Param("acctNumber") String acctNumber);
	
	
    
}
