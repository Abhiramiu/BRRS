package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BRRS_M_IRB_Detail_Archival_Repo extends JpaRepository<BRRS_M_IRB_Detail_Archival_Entity, String> {
    @Query(value = "select * from M_IRB_DETAIL_ARCHIVAL_TABLE where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
    List<BRRS_M_IRB_Detail_Archival_Entity> getdatabydateList(Date reportdate,String DATA_ENTRY_VERSION);

    @Query(value = "select * from M_IRB_DETAIL_ARCHIVAL_TABLE where ROW_ID =?1 and COLUMN_ID=?2 AND REPORT_DATE=?3 AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
    List<BRRS_M_IRB_Detail_Archival_Entity> GetDataByRowIdAndColumnId(String rowId,String ColumnId,Date reportdate,String DATA_ENTRY_VERSION);
}