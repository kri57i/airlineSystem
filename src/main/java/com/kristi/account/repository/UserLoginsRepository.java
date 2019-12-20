package com.kristi.account.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.FlightTemplate;
import com.kristi.account.model.UserLogins;

public interface UserLoginsRepository extends JpaRepository<UserLogins, Long>{

	@Transactional
	@Modifying
	@Query(value = "UPDATE user_logins SET login_times = :new_login "
			+ "WHERE username = :username", 
	nativeQuery = true)
	void incrementUserLogin(@Param("new_login") int new_login, 
			@Param("username") String username);
	
	@Query(value = "SELECT * FROM user_logins WHERE username = :username", 
			nativeQuery = true)
	UserLogins getRecordFromUser(@Param("username") String username);
	
	@Query(value = "SELECT * FROM user_logins ORDER BY login_times DESC", 
			nativeQuery = true)
	List<UserLogins> getAllLogins();
}
