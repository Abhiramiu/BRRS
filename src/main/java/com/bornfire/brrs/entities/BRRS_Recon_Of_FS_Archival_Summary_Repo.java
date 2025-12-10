

package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Recon_Of_FS_Archival_Summary_Repo extends JpaRepository<Recon_Of_FS_Achival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getRecon_Of_FSarchival();
@Query(value = "select * from BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY where REPORT_DATE = to_date(?1,'dd-MM-yyyy') and REPORT_VERSION = ?2", nativeQuery = true)
    List<Recon_Of_FS_Achival_Summary_Entity> getdatabydateListarchival(String report_date, Object version);

}
