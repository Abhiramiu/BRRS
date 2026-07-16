package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_UNCONS_INVEST_Archival_Summary_Repo
    extends JpaRepository<M_UNCONS_INVEST_Archival_Summary_Entity, M_UNCONS_INVEST_PK> {


    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_UNCONS_INVESTarchival();

    @Query(value = "select * from BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_UNCONS_INVEST_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

    @Query(value = "SELECT * FROM BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<M_UNCONS_INVEST_Archival_Summary_Entity> getdatabydateListWithVersion();
    
    @Query("SELECT MAX(e.reportVersion) FROM M_UNCONS_INVEST_Archival_Summary_Entity e WHERE e.reportDate = :date")
    BigDecimal findMaxVersion(@Param("date") Date date);

}
