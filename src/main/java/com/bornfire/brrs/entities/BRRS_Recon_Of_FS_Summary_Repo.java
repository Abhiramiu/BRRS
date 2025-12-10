package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Recon_Of_FS_Summary_Repo
        extends JpaRepository<Recon_Of_FS_Summary_Entity, Date> {

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_RECON_OF_FS_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<Recon_Of_FS_Summary_Entity> getdatabydateList(Date rpt_date);

    @Query(value = "SELECT *  FROM BRRS_RECON_OF_FS_SUMMARYTABLE WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<Recon_Of_FS_Summary_Entity> getdatabydateListWithVersion(String todate);
}
