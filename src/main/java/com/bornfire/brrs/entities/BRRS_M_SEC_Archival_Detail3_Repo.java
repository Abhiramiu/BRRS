package com.bornfire.brrs.entities;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SEC_Archival_Detail3_Repo extends JpaRepository<M_SEC_Archival_Detail3_Entity,Date> {

	 @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getM_SEC_archival();

	    @Query(value = "select * from BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	    List<M_SEC_Archival_Detail3_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);	    
}