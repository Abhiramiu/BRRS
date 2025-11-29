
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface BRRS_M_FAS_Archival_Detail_Repo extends JpaRepository<M_FAS_Archival_Detail_Entity, String> {
	
	@Query(value = "select * from BRRS_M_FAS_ARCHIVALTABLE_DETAIL where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
	List<M_FAS_Archival_Detail_Entity> getdatabydateList(String todate,String DATA_ENTRY_VERSION);
	
	@Query(value = "select * from BRRS_M_FAS_ARCHIVALTABLE_DETAIL where ROW_ID =?1 and COLUMN_ID=?2 AND to_date(?3,'dd-MM-yyyy') AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
	List<M_FAS_Archival_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate,String DATA_ENTRY_VERSION);

	    //Current Report Version Only Shown 
    @Query(value = "SELECT *  FROM BRRS_M_FAS_ARCHIVALTABLE_DETAIL WHERE DATA_ENTRY_VERSION IS NOT NULL ORDER BY DATA_ENTRY_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_FAS_Archival_Detail_Entity> getdatabydateListWithVersion();

    Optional<M_FAS_Archival_Detail_Entity> findByCustIdAndAcctNumber(String custId, String acctNumber);
}
