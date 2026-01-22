package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_SMME_Intrest_Income_Archival_Summary_Repo extends JpaRepository<Q_SMME_Intrest_Income_Archival_Summary_Entity, Date> {

  @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC ", nativeQuery = true)
    List<Object> getQ_SMMEarchival();

        @Query(value = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2 ",nativeQuery = true)
    List<Q_SMME_Intrest_Income_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);
}


