package com.bornfire.brrs.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GeneralMasterRepo extends JpaRepository<GeneralMasterEntity, String> {

    // Get all data (without pagination)
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE", nativeQuery = true)
    List<GeneralMasterEntity> getAllData();

    // Count all records
    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE", nativeQuery = true)
    int countAll();

    // Pagination without filter
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE ORDER BY ID OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> getdatabydateList(@Param("offset") int offset, @Param("limit") int limit);

    // Pagination with reportDate filter
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD') ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> findByReportDate(@Param("reportDate") String reportDate,
                                                   @Param("offset") int offset,
                                                   @Param("size") int size);

    // Pagination with both fileType and reportDate filter
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD') ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> findByFileTypeAndReportDate(
                                                              @Param("reportDate") String reportDate,
                                                              @Param("offset") int offset,
                                                              @Param("size") int size);


    // Count with reportDate filter
    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = TO_DATE(:reportDate,'YYYY-MM-DD')", nativeQuery = true)
    int countByReportDate(@Param("reportDate") String reportDate);

    // Count with fileType and reportDate filter
    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = TO_DATE(:reportDate,'YYYY-MM-DD')", nativeQuery = true)
    int countByFileTypeAndReportDate(@Param("reportDate") String reportDate);
}