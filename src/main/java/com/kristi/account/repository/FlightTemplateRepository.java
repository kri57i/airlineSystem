package com.kristi.account.repository;

import java.util.List;

import javax.transaction.Transactional;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.FlightTemplate;

public interface FlightTemplateRepository extends JpaRepository<FlightTemplate, Long>{

	@Query(value = "SELECT * FROM flight_template WHERE to_date IS NULL", 
			nativeQuery = true)
	List<FlightTemplate> getAllFlights();
	
	/*@Query(value = "SELECT * FROM flight_template WHERE "
			+ "first_location = :f_l AND last_location = :l_l AND "
			+ "to_date IS NULL", nativeQuery = true)
	List<FlightTemplate> getAllAvailableFlights(@Param("f_l") String f_l, 
			@Param("l_l") String l_l);*/
	
	
	
	@Query(value = "SELECT * FROM flight_template WHERE "
			+ "first_location = :trip_departure_location AND "
			+ "last_location = :trip_arrival_location AND "
			+ "to_date IS NULL AND "
			+ "NOT departing_date = :trip_departure_date", 
			nativeQuery = true)
	List<FlightTemplate> getAvaliableFlights(@Param("trip_departure_location")
	 String trip_departure_location, @Param("trip_arrival_location") 
	String trip_arrival_location, @Param("trip_departure_date") Date trip_departure_date);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight_template SET "
			+ "current_capacity = :new_capacity WHERE "
			+ "id = :flight_template_id", 
	nativeQuery = true)
	void updateCapacity(@Param("new_capacity") int new_capacity, 
			@Param("flight_template_id") long flight_template_id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight_template SET "
			+ "current_capacity = :new_capacity WHERE "
			+ "id = :flight_template_id",
	nativeQuery = true)
	void removePassenger(@Param("new_capacity") int new_capacity, 
			@Param("flight_template_id") long flight_template_id);
}
