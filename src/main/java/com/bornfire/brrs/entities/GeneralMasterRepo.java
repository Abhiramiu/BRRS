package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GeneralMasterRepo extends JpaRepository<GeneralMasterEntity, String> {

    // Get all data (without pagination)
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE", nativeQuery = true)
    List<GeneralMasterEntity> getAllData();
    
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE Where ID=?1", nativeQuery = true)
    GeneralMasterEntity getById(String ID);
    
    
    //Getting dates of Reports from General master table
   
    //MCBL
    @Query(value ="SELECT DISTINCT m.report_date FROM GENERAL_MASTER_TABLE m WHERE REPORT_CODE='MCBL' AND DEL_FLG ='N' ORDER BY m.report_date DESC ",nativeQuery = true)
    List<Date> findDistinctReportDatesMCBL();
    
    //LOAN BOOK
    @Query(value = "SELECT DISTINCT m.REPORT_DATE FROM GENERAL_MASTER_TABLE m WHERE REPORT_CODE='LOANB' AND DEL_FLG ='N' ORDER BY m.REPORT_DATE DESC ", nativeQuery = true)
    List<Date> findDistinctReportDatesLoan();

    //DEPOSIT BOOK
    @Query(value = "SELECT DISTINCT m.REPORT_DATE FROM GENERAL_MASTER_TABLE m WHERE REPORT_CODE='DEPB' AND DEL_FLG ='N' ORDER BY m.REPORT_DATE DESC ", nativeQuery = true)
    List<Date> findDistinctReportDatesDepB();

   //DEPOSIT GENERAL
    @Query(value = "SELECT DISTINCT m.REPORT_DATE FROM GENERAL_MASTER_TABLE m WHERE REPORT_CODE='DEPG' AND DEL_FLG ='N' ORDER BY m.REPORT_DATE DESC ", nativeQuery = true)
    List<Date> findDistinctReportDatesDepG();

    
    //fetching Records by Report date
   
    //MCBL
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = ?1 AND REPORT_CODE='MCBL' AND DEL_FLG='N'", nativeQuery = true)
    List<GeneralMasterEntity> findMCBLRecordsByReportDate(String reportDate);

	//DEPB
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = ?1 AND REPORT_CODE='DEPB' AND DEL_FLG='N'", nativeQuery = true)
    List<GeneralMasterEntity> findDepBRecordsByReportDate(String reportDate);

	//DEPG
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = ?1 AND REPORT_CODE='DEPG' AND DEL_FLG='N'", nativeQuery = true)
    List<GeneralMasterEntity> findDepGRecordsByReportDate(String reportDate);

    //LOAN
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = ?1 AND REPORT_CODE='LOANB' AND DEL_FLG='N'", nativeQuery = true)
    List<GeneralMasterEntity> findLoanBRecordsByReportDate(String reportDate);

    
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
    
   

    
    @Query("SELECT g FROM GeneralMasterEntity g WHERE g.accountNo = :accNo AND g.reportDate = :repDate")
    GeneralMasterEntity getdataBybdgf(@Param("accNo") String accountNo,
                                      @Param("repDate") Date reportDate);


    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE "
            + "WHERE account_no=?1 "
            + "AND TRUNC(REPORT_DATE)=TRUNC(?2)", nativeQuery = true)
GeneralMasterEntity getdataBybfbl(String account_no, Date reportDate);
    
    

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM GENERAL_MASTER_TABLE WHERE account_no = ?1 AND TRUNC(REPORT_DATE) = TRUNC(?2)",
           nativeQuery = true)
    int deletedata(String account_no, Date reportDate);
    
    // ---------------- MCBL ----------------
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE " +
                   "WHERE mcbl_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')) " +
                   "ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> findByMcblFlag(@Param("reportDate") String reportDate,
                                             @Param("offset") int offset,
                                             @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE " +
                   "WHERE mcbl_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD'))", nativeQuery = true)
    int countByMcblFlag(@Param("reportDate") String reportDate);

    // ---------------- BLBF ----------------
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE " +
                   "WHERE blbf_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')) " +
                   "ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> findByBlbfFlag(@Param("reportDate") String reportDate,
                                             @Param("offset") int offset,
                                             @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE " +
                   "WHERE blbf_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD'))", nativeQuery = true)
    int countByBlbfFlag(@Param("reportDate") String reportDate);

    // ---------------- BDGF ----------------
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE " +
                   "WHERE bdgf_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')) " +
                   "ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> findByBdgfFlag(@Param("reportDate") String reportDate,
                                             @Param("offset") int offset,
                                             @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE " +
                   "WHERE bdgf_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD'))", nativeQuery = true)
    int countByBdgfFlag(@Param("reportDate") String reportDate);

    // ---------------- BFDB ----------------
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE " +
                   "WHERE bfdb_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')) " +
                   "ORDER BY ID OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY", nativeQuery = true)
    List<GeneralMasterEntity> findByBfdbFlag(@Param("reportDate") String reportDate,
                                             @Param("offset") int offset,
                                             @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM GENERAL_MASTER_TABLE " +
                   "WHERE bfdb_flg = 'Y' " +
                   "AND (:reportDate IS NULL OR REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD'))", nativeQuery = true)
    int countByBfdbFlag(@Param("reportDate") String reportDate);

    // ---------------- Edit/View/Delete by ID ----------------
    @Query(value = "SELECT * FROM GENERAL_MASTER_TABLE WHERE id = :id", nativeQuery = true)
    GeneralMasterEntity findByIdNative(@Param("id") String id);
}