package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_BRANCHNET_Archival_Summary_Repo extends JpaRepository<Q_BRANCHNET_Archival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getQ_BRANCHNETarchival();

    @Query(value = "select * from BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<Q_BRANCHNET_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

    @Query(value = "SELECT * FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<Q_BRANCHNET_Archival_Summary_Entity> getdatabydateListWithVersion();

}