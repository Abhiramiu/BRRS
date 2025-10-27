package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SFINP1_Archival_Summary_Manual_Repo extends JpaRepository<M_SFINP1_Archival_Summary_Manual_Entity, Date> {

    @Query(value = "SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = :reportDate", nativeQuery = true)
    List<M_SFINP1_Archival_Summary_Manual_Entity> getdatabydateListarchival(@Param("reportDate") Date reportDate);

    }
