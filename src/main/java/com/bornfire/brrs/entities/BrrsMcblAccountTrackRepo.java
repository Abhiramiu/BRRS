package com.bornfire.brrs.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrrsMcblAccountTrackRepo extends JpaRepository<BrrsMcblAccountTrack, String> {

    // Custom finder methods
    List<BrrsMcblAccountTrack> findByReportDate(LocalDate reportDate);

    List<BrrsMcblAccountTrack> findByChangeType(String changeType);

    List<BrrsMcblAccountTrack> findByEntryUser(String entryUser);
}