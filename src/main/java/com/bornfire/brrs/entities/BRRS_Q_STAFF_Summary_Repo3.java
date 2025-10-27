
package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_STAFF_Summary_Repo3 extends JpaRepository<Q_STAFF_Summary_Entity3, Date> {
	@Query(value = "select * from BRRS_Q_STAFF_SUMMARYTABLE3  ", nativeQuery = true)
	List<Q_STAFF_Summary_Entity3> getdatabydateList(Date rpt_code);
	
}