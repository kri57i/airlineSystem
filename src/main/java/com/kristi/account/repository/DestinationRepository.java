package com.kristi.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.Destination;


public interface DestinationRepository extends JpaRepository<Destination, Long>{

	/*
	 * The following query returns a list of strings, containing all the destinations
	 */
	@Query(value = "SELECT destination_name FROM destination", 
			nativeQuery = true)
	List<String> getAllDestinations();
	
	
	@Query(value = "SELECT destination_name FROM destination "
			+ "WHERE NOT destination_name = :from_location AND NOT destination_name = :to_location", 
			nativeQuery = true)
	List<String> getAllAvailableDestinations(@Param("from_location") String from_location, 
			@Param("to_location") String to_location);
}
