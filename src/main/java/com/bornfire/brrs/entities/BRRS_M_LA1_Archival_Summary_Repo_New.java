package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_LA1_Archival_Summary_Repo_New extends JpaRepository<M_LA1_Archival_Summary_Entity_New, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_LA1_ARCHIVALTABLE_SUMMARY_NEW order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_LA1archival();

    @Query(value = "select * from BRRS_M_LA1_ARCHIVALTABLE_SUMMARY_NEW where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_LA1_Archival_Summary_Entity_New> getdatabydateListarchival(Date report_date, BigDecimal report_version);
}
