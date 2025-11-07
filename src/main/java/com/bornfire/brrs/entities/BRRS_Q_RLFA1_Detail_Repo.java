

package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_RLFA1_Detail_Repo extends JpaRepository<Q_RLFA1_Detail_Entity, String> {

    @Query(value = "select * from BRRS_Q_RLFA1_DETAIL_TABLE where REPORT_DATE =?1  ", nativeQuery = true)
    List<Q_RLFA1_Detail_Entity> getdatabydateList(Date reportdate);

    @Query(value = "select * from BRRS_Q_RLFA1_DETAIL_TABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3", nativeQuery = true)
    List<Q_RLFA1_Detail_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);

    @Query(value = "select * from BRRS_Q_RLFA1_DETAIL_TABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<Q_RLFA1_Detail_Entity> getdatabydateList(Date reportdate,int startpage,int endpage);

    @Query(value = "select count(*) from BRRS_Q_RLFA1_DETAIL_TABLE where REPORT_DATE=?1", nativeQuery = true)
    int getdatacount(Date reportdate);
	
}
