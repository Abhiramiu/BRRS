
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SEC_Archival_Summary_Repo3
        extends JpaRepository<BRRS_M_SEC_Archival_Summary_Entity3,M_SEC_PK> {

        	 @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 order by REPORT_VERSION", nativeQuery = true)
        	    List<Object> getM_SECarchival();

        	    @Query(value = "select * from BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
        	    List<BRRS_M_SEC_Archival_Summary_Entity3> getdatabydateListarchival(Date report_date, BigDecimal report_version);

        	    @Query(value = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
        	    List<BRRS_M_SEC_Archival_Summary_Entity3> getdatabydateListWithVersion();
        	    
        	    @Query("SELECT MAX(e.report_version) FROM BRRS_M_SEC_Archival_Summary_Entity3 e WHERE e.report_date = :date")
        	    BigDecimal findMaxVersion(@Param("date") Date date);


}
