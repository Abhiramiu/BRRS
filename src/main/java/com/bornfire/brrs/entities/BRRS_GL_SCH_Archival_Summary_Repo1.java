package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_GL_SCH_Archival_Summary_Repo1 extends JpaRepository<GL_SCH_Archival_Summary_Entity1, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_GL_SCH_ARCHIVALTABLE_SUMMARY1 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getGL_SCHarchival();

    @Query(value = "select * from BRRS_GL_SCH_ARCHIVALTABLE_SUMMARY1 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<GL_SCH_Archival_Summary_Entity1> getdatabydateListarchival(Date report_date, String report_version);
}
