package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_BOP_Archival_Detail_Repo extends JpaRepository<M_BOP_Archival_Detail_Entity, Date>{
	

	 @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_BOP_ARCHIVALTABLE_DETAIL order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getM_BOP_archival();

	    @Query(value = "select * from BRRS_M_BOP_ARCHIVALTABLE_DETAIL where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	    List<M_BOP_Archival_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);	


}


