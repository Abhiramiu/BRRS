package com.bornfire.brrs.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BrrsGeneralMasterRepo extends JpaRepository<BrrsGeneralMasterEntity, String> {

    // Get all data (without pagination)
    @Query(value = "SELECT * FROM BRRS_GENERAL_MASTER_TABLE", nativeQuery = true)
    List<BrrsGeneralMasterEntity> getAllData();

    // Count all records
    @Query(value = "SELECT COUNT(*) FROM BRRS_GENERAL_MASTER_TABLE", nativeQuery = true)
    int countAll();

    // Pagination without filter
    @Query(value = "SELECT * FROM BRRS_GENERAL_MASTER_TABLE ORDER BY ID OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY", nativeQuery = true)
    List<BrrsGeneralMasterEntity> getdatabydateList(@Param("offset") int offset, @Param("limit") int limit);

    // Pagination with fileType filter
    @Query(value = "SELECT * FROM BRRS_GENERAL_MASTER_TABLE WHERE FILE_TYPE = :fileType ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<BrrsGeneralMasterEntity> findByFileType(@Param("fileType") String fileType,
                                                 @Param("offset") int offset,
                                                 @Param("size") int size);

    // Pagination with reportDate filter
    @Query(value = "SELECT * FROM BRRS_GENERAL_MASTER_TABLE WHERE REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD') ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<BrrsGeneralMasterEntity> findByReportDate(@Param("reportDate") String reportDate,
                                                   @Param("offset") int offset,
                                                   @Param("size") int size);

    // Pagination with both fileType and reportDate filter
    @Query(value = "SELECT * FROM BRRS_GENERAL_MASTER_TABLE WHERE FILE_TYPE = :fileType AND REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD') ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<BrrsGeneralMasterEntity> findByFileTypeAndReportDate(@Param("fileType") String fileType,
                                                              @Param("reportDate") String reportDate,
                                                              @Param("offset") int offset,
                                                              @Param("size") int size);

    // Count with fileType filter
    @Query(value = "SELECT COUNT(*) FROM BRRS_GENERAL_MASTER_TABLE WHERE FILE_TYPE = :fileType", nativeQuery = true)
    int countByFileType(@Param("fileType") String fileType);

    // Count with reportDate filter
    @Query(value = "SELECT COUNT(*) FROM BRRS_GENERAL_MASTER_TABLE WHERE REPORT_DATE = TO_DATE(:reportDate,'YYYY-MM-DD')", nativeQuery = true)
    int countByReportDate(@Param("reportDate") String reportDate);

    // Count with fileType and reportDate filter
    @Query(value = "SELECT COUNT(*) FROM BRRS_GENERAL_MASTER_TABLE WHERE FILE_TYPE = :fileType AND REPORT_DATE = TO_DATE(:reportDate,'YYYY-MM-DD')", nativeQuery = true)
    int countByFileTypeAndReportDate(@Param("fileType") String fileType,
                                     @Param("reportDate") String reportDate);
}
