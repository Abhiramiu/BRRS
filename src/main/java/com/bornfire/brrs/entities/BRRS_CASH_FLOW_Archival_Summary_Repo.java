package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_CASH_FLOW_Archival_Summary_Repo extends JpaRepository<CASH_FLOW_Archival_Summary_Entity, CASH_FLOW_PK> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getCASH_FLOWarchival();

    @Query(value = "select * from BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<CASH_FLOW_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);
    
    @Query(value = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<CASH_FLOW_Archival_Summary_Entity> getdatabydateListWithVersion();
}
