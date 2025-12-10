package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Recon_Of_FS_Archival_Detail_Repo
        extends JpaRepository<Recon_Of_FS_Archival_Detail_Entity, String> {

    @Query(value = "select * from BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
    List<Recon_Of_FS_Archival_Detail_Entity> getdatabydateList(String todate, String DATA_ENTRY_VERSION);

    @Query(value = "select * from BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3 AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
    List<Recon_Of_FS_Archival_Detail_Entity> GetDataByRowIdAndColumnId(String reportLable, String reportAddlCriteria_1,
            Date reportdate, String DATA_ENTRY_VERSION);

    @Query(value = "select * from BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<Recon_Of_FS_Archival_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);
}