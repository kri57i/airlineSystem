package com.kristi.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	/*
	 * The following query returns a user with the required email
	 */
	@Query(value = "SELECT * FROM user  WHERE email = :email AND to_date IS NULL", 
			nativeQuery = true)
	User findByEmail(@Param("email") String email);
	
	/*
	 * THe following query returns a deactivated user with the required email
	 */
	@Query(value = "SELECT * FROM user WHERE email = :email AND to_date IS NOT NULL", 
			nativeQuery = true)
	User findDeactivatedUser(@Param("email") String email);
	
	/*
	 * The following query returns a list of all users(except the administrator)
	 */
	@Query(value = "SELECT * FROM user WHERE NOT email = 'admin@gmail.com' OR email IS NULL", 
			nativeQuery = true)
	List<User> getAllUsers();
	
}
