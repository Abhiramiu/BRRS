package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_AIDP_Archival_Summary_Repo1 extends JpaRepository<M_AIDP_Archival_Summary_Entity1, M_AIDP_PK> {

	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_AIDP_ARCHIVALTABLE_SUMMARY1 order by REPORT_VERSION", nativeQuery = true)
	List<Object> getM_AIDParchival();

	@Query(value = "select * from BRRS_M_AIDP_ARCHIVALTABLE_SUMMARY1 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	List<M_AIDP_Archival_Summary_Entity1> getdatabydateListarchival(Date report_date, BigDecimal report_version);

	@Query(value = "select * from BRRS_M_AIDP_ARCHIVALTABLE_SUMMARY1 ", nativeQuery = true)
	static List<M_AIDP_Archival_Summary_Entity1> getdatabydateList(Date rpt_code) {
		// TODO Auto-generated method stub
		return null;
	}
}
