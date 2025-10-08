package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_DEP3_Archival_Summary_Repo2 extends JpaRepository<M_DEP3_Archival_Summary_Entity2, Date> {

  @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY2 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_DEP3archival();

    @Query(value = "select * from BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY2 where REPORT_DATE = to_date(?1,'dd-MM-yyyy') and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_DEP3_Archival_Summary_Entity2> getdatabydateListarchival(String todate, Object version);
}


