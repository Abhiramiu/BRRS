package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_LARADV_Detail_Repo1 extends JpaRepository<M_LARADV_Detail_Entity1, Date>{
	@Query(value = "select * from BRRS_M_LARADV_DETAILTABLE1 ", nativeQuery = true)
	List<M_LARADV_Detail_Entity1> getdatabydateList(Date rpt_code);
}
