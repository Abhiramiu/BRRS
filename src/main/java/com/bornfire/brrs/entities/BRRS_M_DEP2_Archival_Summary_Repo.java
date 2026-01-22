package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_DEP2_Archival_Summary_Repo extends JpaRepository<M_DEP2_Archival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_DEP2_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_DEP2archival();


            @Query(value = "SELECT * FROM BRRS_M_DEP2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2 ",nativeQuery = true)
    List<M_DEP2_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);
}

