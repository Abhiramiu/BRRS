package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface BRRS_M_LA1_Detail_Repo extends JpaRepository<M_LA1_Detail_Entity, String> {

    // Fetch all records for a given date
    @Query(value = "select * from BRRS_M_LA1_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    List<M_LA1_Detail_Entity> getdatabydateList(Date reportdate);

    // ✅ Pagination fixed → use OFFSET and LIMIT correctly
    @Query(value = "select * from BRRS_M_LA1_DETAILTABLE where REPORT_DATE = ?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
    List<M_LA1_Detail_Entity> getdatabydateList(Date reportdate, int offset, int limit);

    // Count rows by date
    @Query(value = "select count(*) from BRRS_M_LA1_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    int getdatacount(Date reportdate);

    @Query(value = "SELECT * FROM BRRS_M_LA1_DETAILTABLE " +
            "WHERE report_label = :rowId " +
            "AND report_addl_criteria_1 = :columnId " +
            "AND ( :columnId1 IS NULL OR :columnId1 = '' OR report_addl_criteria_2 = :columnId1 ) " +
            "AND ( :columnId2 IS NULL OR :columnId2 = '' OR report_addl_criteria_3 = :columnId2 ) " +
            "AND REPORT_DATE = :reportDate",
        nativeQuery = true)
    List<M_LA1_Detail_Entity> GetDataByRowIdAndColumnId(
        @Param("rowId") String rowId,
        @Param("columnId") String columnId,
        @Param("columnId1") String columnId1,
        @Param("columnId2") String columnId2,
        @Param("reportDate") Date reportDate
    );



}
