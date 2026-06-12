package com.bornfire.brrs.entities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserProfileRep extends CrudRepository<UserProfile,String>{
	

	public Optional<UserProfile> findByusername(String userName);
	
	@Query(value = "select * from BOTS_USER_PROFILE_TABLE where user_id=?1 ", nativeQuery = true)
	List<UserProfile> delete(String userid);
	
	@Query(value = "select * from BOTS_USER_PROFILE_TABLE", nativeQuery = true)
	List<UserProfile> getalluser();
	
	@Query(value = "select * from BOTS_USER_PROFILE_TABLE where USER_ID=?1", nativeQuery = true)
	UserProfile getRole(String userId);

	@Query(value = "select DISTINCT BRANCH_CODE from BOTS_USER_PROFILE_TABLE", nativeQuery = true)
	List<String> getallcodes();
	
	@Query(value = "select * from BOTS_USER_PROFILE_TABLE", nativeQuery = true)
	List<UserProfile> getallbranches();
	
	@Query(value = "SELECT work_class FROM BOTS_USER_PROFILE_TABLE WHERE USER_ID = ?1", nativeQuery = true)
	String getUserRole(String userid);
}
