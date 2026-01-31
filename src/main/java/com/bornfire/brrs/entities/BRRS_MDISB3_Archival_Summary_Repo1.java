package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface BRRS_MDISB3_Archival_Summary_Repo1 extends JpaRepository<MDISB3_Archival_Summary_Entity1, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_MDISB3_ARCHIVALTABLE_SUMMARY1 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getMDISB3archival();

    @Query(value = "select * from BRRS_MDISB3_ARCHIVALTABLE_SUMMARY1 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<MDISB3_Archival_Summary_Entity1> getdatabydateListarchival(Date report_date, BigDecimal report_version);
}
