package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_Q_SMME_loans_Advances_Detail_Repo
    extends JpaRepository<Q_SMME_loans_Advances_Detail_Entity, String> {
  // Fetch all records for a given date
  @Query(value = "select * from BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
  List<Q_SMME_loans_Advances_Detail_Entity> getdatabydateList(Date parsedDate);

  // ✅ Pagination fixed → use OFFSET and LIMIT correctly
  @Query(value = "select * from BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
  List<Q_SMME_loans_Advances_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);

  // Count rows by date
  @Query(value = "select count(*) from BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
  int getdatacount(Date reportdate);

  @Query(value = "select * from BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE where REPORT_LABEL =?1 and REPORT_ADDL_CRITERIA_1=?2 AND REPORT_DATE=?3", nativeQuery = true)
  List<Q_SMME_loans_Advances_Detail_Entity> GetDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
      Date reportdate);

  @Query(value = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE WHERE ACCT_NUMBER = :acctNumber", nativeQuery = true)
  Q_SMME_loans_Advances_Detail_Entity findByAcctnumber(@Param("acctNumber") String acctNumber);

}