package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsMasterRep extends JpaRepository<ReportsMaster, String>{

	

	@Modifying
	@Query("update ReportsMaster a set report_validity=?2, lchg_user_id=?3, lchg_time=sysdate where reportid=?1 ")
	public int updateValidity(String reportId, String valid, String userid);
	

}
