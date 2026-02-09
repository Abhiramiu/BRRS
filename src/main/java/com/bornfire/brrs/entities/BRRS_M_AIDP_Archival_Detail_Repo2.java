package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_AIDP_Archival_Detail_Repo2 extends JpaRepository<M_AIDP_Archival_Detail_Entity2, Date> {
	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_AIDP_ARCHIVALTABLE_DETAIL2 order by REPORT_VERSION", nativeQuery = true)
	List<Object> getM_AIDParchival();

	@Query(value = "select * from BRRS_M_AIDP_ARCHIVALTABLE_DETAIL2 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	List<M_AIDP_Archival_Detail_Entity2> getdatabydateListarchival(Date report_date, BigDecimal report_version);
}
