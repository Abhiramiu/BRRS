package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_BASEL_III_COM_EQUITY_DISC_Archival_Summary_Repo extends JpaRepository<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getB_III_CETDarchival();

    @Query(value = "select * from BRRS_BASEL_III_COM_EQUITY_DISC_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);
}
