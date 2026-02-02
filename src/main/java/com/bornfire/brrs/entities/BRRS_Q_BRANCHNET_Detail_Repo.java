package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_BRANCHNET_Detail_Repo extends JpaRepository<Q_BRANCHNET_Detail_Entity, Date> {

    // Fetch all records for a given date
    @Query(value = "select * from BRRS_Q_BRANCHNET_DETAILTABLE where REPORT_DATE = ?1", nativeQuery = true)
    List<Q_BRANCHNET_Detail_Entity> getdatabydateList(Date reportdate);

}
