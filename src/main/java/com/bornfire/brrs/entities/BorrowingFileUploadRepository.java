package com.bornfire.brrs.entities;

import com.bornfire.brrs.entities.BorrowingFileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowingFileUploadRepository extends JpaRepository<BorrowingFileUploadEntity, String> {

    List<BorrowingFileUploadEntity> findByAsOnDateAndCategory(Date asOnDate, String category);

    @Transactional
    @Modifying
    @Query("DELETE FROM BorrowingFileUploadEntity b WHERE b.asOnDate = :asOnDate AND b.category = :category")
    void deleteByAsOnDateAndCategory(Date asOnDate, String category);
}