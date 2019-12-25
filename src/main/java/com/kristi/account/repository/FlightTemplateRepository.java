package com.kristi.account.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.FlightTemplate;

public interface FlightTemplateRepository extends JpaRepository<FlightTemplate, Long>{

	/*
	 * The following query returns all the active flight templates
	 */
	@Query(value = "SELECT * FROM flight_template WHERE to_date IS NULL", 
			nativeQuery = true)
	List<FlightTemplate> getAllFlights();
	
	
	/*
	 * The following query returns a list of flight templates whose locations
	 * match the given locations
	 */
	@Query(value = "SELECT * FROM flight_template WHERE "
			+ "first_location = :trip_departure_location AND "
			+ "last_location = :trip_arrival_location AND "
			+ "to_date IS NULL AND NOT current_capacity = maximum_capacity", 
			nativeQuery = true)
	List<FlightTemplate> getAvaliableFlights(@Param("trip_departure_location")
	 String trip_departure_location, @Param("trip_arrival_location") 
	String trip_arrival_location);
	
	/*
	 * The following query is used to increment the capacity of the flight template, the moment 
	 * a new user has booked the flight
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight_template SET "
			+ "current_capacity = :new_capacity WHERE "
			+ "id = :flight_template_id", 
	nativeQuery = true)
	void updateCapacity(@Param("new_capacity") int new_capacity, 
			@Param("flight_template_id") long flight_template_id);
	
	/*
	 * The following query is used to remove a passanger from the flight template
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight_template SET "
			+ "current_capacity = :new_capacity WHERE "
			+ "id = :flight_template_id",
	nativeQuery = true)
	void removePassenger(@Param("new_capacity") int new_capacity, 
			@Param("flight_template_id") long flight_template_id);
}
