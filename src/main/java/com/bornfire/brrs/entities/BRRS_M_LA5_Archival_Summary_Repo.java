package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_LA5_Archival_Summary_Repo extends JpaRepository<M_LA5_Archival_Summary_Entity, Date> {

//    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_LA5_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
//    List<Object> getM_LA5archival();

    @Query(value = "select * from BRRS_M_LA5_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_LA5_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);
 
    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_LA5_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC ", nativeQuery = true)
    List<Object> getM_LA5archival();
    
    @Query(value = "SELECT * FROM BRRS_M_LA5_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<M_LA5_Archival_Summary_Entity> getdatabydateListWithVersion();



}
