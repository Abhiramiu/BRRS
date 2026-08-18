package com.bornfire.brrs.entities;

import com.bornfire.brrs.entities.PlacementFileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface PlacementFileUploadRepository extends JpaRepository<PlacementFileUploadEntity, String> {

	@Transactional
	@Modifying
	@Query("DELETE FROM PlacementFileUploadEntity p WHERE p.asOnDate = :asOnDate AND p.category = :category")
	void deleteByAsOnDateAndCategory(Date asOnDate, String category);
}