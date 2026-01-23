package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SRWA_12A_New_Archival_Summary3_Repo extends JpaRepository<M_SRWA_12A_New_Archival_Summary_Entity3, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_SRWA_12A_NEW_ARCHIVALTABLE_SUMMARY3 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_SRWA_12NewAarchival();

    @Query(value = "select * from BRRS_M_SRWA_12A_NEW_ARCHIVALTABLE_SUMMARY3 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_SRWA_12A_New_Archival_Summary_Entity3> getdatabydateListarchival(Date report_date, BigDecimal report_version);
}