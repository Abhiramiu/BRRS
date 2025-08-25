package com.bornfire.brrs.entities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RRReportRepo extends JpaRepository<RRReport, Integer> {
	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'M' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportListbrrs();
	
	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'M2' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportListmonthly2();

	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'M3' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportListmonthly3();
	

	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'HY1' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportHalfYearly1();
	
	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'Y1' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportYearly1();
	
	
	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'FN' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportListFORTNIGHTLY();
	
	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE RPT_CODE =?1", nativeQuery = true)
	Optional<RRReport> getParticularReport3(String rptcode);
	
	@Query(value = "select * from BRRS_RR_RPT_MAST where rpt_code=?1", nativeQuery = true)
	RRReport getReportbyrptcode(String rpt_code);
	
	@Query(value = "select * from BRRS_RR_RPT_MAST WHERE REMARKS_5 = 'Q2' ORDER BY rpt_code", nativeQuery = true)
	List<RRReport> getReportListQuarterly2();
}
