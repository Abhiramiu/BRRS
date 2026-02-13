package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_SMME_DEP_Resub_Detail_Repo extends JpaRepository<Q_SMME_DEP_Resub_Detail_Entity, Q_SMME_DEP_PK> {

    
    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_SMME_DEP_RESUB_DETAILTABLE order by REPORT_VERSION", nativeQuery = true)
    List<Object> getQ_SMME_DEParchival();

    @Query(value = "select * from BRRS_Q_SMME_DEP_RESUB_DETAILTABLE where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<Q_SMME_DEP_Resub_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

    @Query(value = "SELECT * FROM BRRS_Q_SMME_DEP_RESUB_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<Q_SMME_DEP_Resub_Detail_Entity> getdatabydateListWithVersion();
    
    //Resub
    @Query("SELECT MAX(e.reportVersion) FROM Q_SMME_DEP_Resub_Detail_Entity e WHERE e.reportDate = :date")
    BigDecimal findMaxVersion(@Param("date") Date date);


}
