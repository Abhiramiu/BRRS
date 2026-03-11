package com.bornfire.brrs.entities;
import java.util.Date;





import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;






public interface BRRS_M_I_S_CA_Detail_Repo extends JpaRepository<M_I_S_CA_Detail_Entity, String> {
	
	
	// Fetch all records for a given date
	   @Query(value = "select * from BRRS_M_I_S_CA_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
	   List<M_I_S_CA_Detail_Entity> getdatabydateList(Date reportdate);
	   
	   // ✅ Pagination fixed → use OFFSET and LIMIT correctly
	   @Query(value = "select * from BRRS_M_I_S_CA_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	   List<M_I_S_CA_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);
	   
	   // Count rows by date
	   @Query(value = "select count(*) from BRRS_M_I_S_CA_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
	   int getdatacount(Date reportdate);
	   
	 
		/*
		 * @Query(value =
		 * "select * from BRRS_M_I_S_CA_DETAILTABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3"
		 * , nativeQuery = true) List<M_I_S_CA_Detail_Entity>
		 * GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);
		 */
		
	   @Query(value = "SELECT * FROM BRRS_M_I_S_CA_DETAILTABLE " +
	            "WHERE REPORT_LABEL = :reportLabel " +
	            "AND  REPORT_LABEL_1 = :reportLabel1 " +
	            "AND ( :reportAddlCriteria_1 IS NULL OR :reportAddlCriteria_1 = '' OR REPORT_ADDL_CRITERIA_1 = :reportAddlCriteria_1 ) " +
	            "AND ( :reportAddlCriteria_2 IS NULL OR :reportAddlCriteria_2 = '' OR REPORT_ADDL_CRITERIA_2 = :reportAddlCriteria_2 ) " +
	            "AND ( :reportAddlCriteria_3 IS NULL OR :reportAddlCriteria_3 = '' OR REPORT_ADDL_CRITERIA_3 = :reportAddlCriteria_3 ) " +
	           
	            "AND REPORT_DATE = :reportDate",
	        nativeQuery = true)
	    List<M_I_S_CA_Detail_Entity> GetDataByRowIdAndColumnId(
	        @Param("reportLabel") String reportLabel,
	        @Param("reportLabel1") String reportLabel1,
	        @Param("reportAddlCriteria_1") String reportAddlCriteria_1,
	        @Param("reportAddlCriteria_2") String reportAddlCriteria_2,
	        @Param("reportAddlCriteria_3") String reportAddlCriteria_3,
	       
	        @Param("reportDate") Date reportDate
	    );
		
		/* M_SCI_E_Detail_Entity findByAcctNumber(String acctNumber); */
		
		@Query(value = "SELECT * FROM BRRS_M_I_S_CA_DETAILTABLE WHERE ACCT_NUMBER = :acctNumber", nativeQuery = true)
		M_I_S_CA_Detail_Entity findByAcctnumber(@Param("acctNumber") String acctNumber);
		
}