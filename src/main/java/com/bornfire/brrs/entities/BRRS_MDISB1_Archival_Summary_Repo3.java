package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_MDISB1_Archival_Summary_Repo3 extends JpaRepository<MDISB1_Archival_Summary_Manual, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_MDISB1_ARCHIVALTABLE_MANUAL order by REPORT_VERSION", nativeQuery = true)
    List<Object> getMDISB1archival();

    @Query(value = "select * from BRRS_MDISB1_ARCHIVALTABLE_MANUAL where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<MDISB1_Archival_Summary_Manual> getdatabydateListarchival(Date report_date, String report_version);
}
