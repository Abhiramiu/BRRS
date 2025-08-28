package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface BRRS_M_SFINP2_Detail_Repo extends JpaRepository<M_SFINP2_Detail_Entity, String> {

    // Fetch all records for a given date
    @Query(value = "select * from BRRS_M_SFINP2_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    List<M_SFINP2_Detail_Entity> getdatabydateList(Date reportdate);

    // ✅ Pagination fixed → use OFFSET and LIMIT correctly
    @Query(value = "select * from BRRS_M_SFINP2_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<M_SFINP2_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);

    // Count rows by date
    @Query(value = "select count(*) from BRRS_M_SFINP2_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    int getdatacount(Date reportdate);

   
	@Query(value = "select * from BRRS_M_SFINP2_DETAILTABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3", nativeQuery = true)
	List<M_SFINP2_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);
}
