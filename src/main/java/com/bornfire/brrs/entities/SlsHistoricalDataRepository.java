package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlsHistoricalDataRepository extends JpaRepository<SlsHistoricalDataEntity, Long> {
	// The table holds one row per month (~a few dozen rows), so the upload service
	// simply loads findAll() once and matches rows by year-month in memory.
}
