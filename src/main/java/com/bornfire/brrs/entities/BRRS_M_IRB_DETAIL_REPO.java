package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BRRS_M_IRB_DETAIL_REPO extends JpaRepository<BRRS_M_IRB_DETAILS_ENTITY, String> {
    @Query(value = "select * from M_IRB_DETAIL_TABLE where REPORT_DATE =?1  ", nativeQuery = true)
    List<BRRS_M_IRB_DETAILS_ENTITY> getdatabydateList(Date reportdate);

    @Query(value = "select * from M_IRB_DETAIL_TABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3", nativeQuery = true)
    List<BRRS_M_IRB_DETAILS_ENTITY> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate);

    @Query(value = "select * from M_IRB_DETAIL_TABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<BRRS_M_IRB_DETAILS_ENTITY> getdatabydateList(Date reportdate,int startpage,int endpage);

    @Query(value = "select count(*) from M_IRB_DETAIL_TABLE where REPORT_DATE=?1", nativeQuery = true)
    int getdatacount(Date reportdate);
}
