
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_SMME_loans_Advances_Archival_Detail_Repo extends JpaRepository<Q_SMME_loans_Advances_Archival_Detail_Entity, String> {
	
	@Query(value = "select * from BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL where REPORT_DATE=?1 AND DATA_ENTRY_VERSION=?2", nativeQuery = true)
	List<Q_SMME_loans_Advances_Archival_Detail_Entity> getdatabydateList(String todate,String DATA_ENTRY_VERSION);
	
	@Query(value = "select * from BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND to_date(?3,'dd-MM-yyyy') AND DATA_ENTRY_VERSION=?4", nativeQuery = true)
	List<Q_SMME_loans_Advances_Archival_Detail_Entity> GetDataByRowIdAndColumnId(String reportLabel,String reportAddlCriteria1,Date reportdate,String DATA_ENTRY_VERSION);

}
