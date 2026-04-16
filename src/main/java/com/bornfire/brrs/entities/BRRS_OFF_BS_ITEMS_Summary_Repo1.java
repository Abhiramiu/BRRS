package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_OFF_BS_ITEMS_Summary_Repo1 extends JpaRepository<OFF_BS_ITEMS_Summary_Entity1, Date> {

	@Query(value = "select * from BRRS_OFF_BS_ITEMS_SUMMARYTABLE1 ", nativeQuery = true)
	List<OFF_BS_ITEMS_Summary_Entity1> getdatabydateList(Date reportdate);

}
