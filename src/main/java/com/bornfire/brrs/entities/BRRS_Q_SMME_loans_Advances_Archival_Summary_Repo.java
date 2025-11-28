package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_SMME_loans_Advances_Archival_Summary_Repo extends JpaRepository<Q_SMME_Intrest_Income_Archival_Summary_Entity, Date> {

  @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVAL_SUMMARYTABLE order by REPORT_VERSION", nativeQuery = true)
    List<Object> getQ_SMMEarchival();

    @Query(value = "select * from BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVAL_SUMMARYTABLE where REPORT_DATE = to_date(?1,'dd-MM-yyyy') and REPORT_VERSION = ?2", nativeQuery = true)
    List<Q_SMME_loans_Advances_Archival_Summary_Entity> getdatabydateListarchival(String report_date, Object version);
}


