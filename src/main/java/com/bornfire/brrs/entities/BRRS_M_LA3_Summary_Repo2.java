package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_LA3_Summary_Repo2 extends JpaRepository<M_LA3_Summary_Entity2, Date> {
    
    // Use the actual column name from Oracle, usually uppercase
	@Query(value = "select * from BRRS_M_LA3_SUMMARYTABLE2 ", nativeQuery = true)
	List<M_LA3_Summary_Entity2> getdatabydateList(Date rpt_code);

}
