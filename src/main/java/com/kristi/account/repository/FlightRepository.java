package com.kristi.account.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.Flight;



public interface FlightRepository extends JpaRepository<Flight, Long>{
	
	@Query(value = "SELECT * FROM flight WHERE user = :flight_user AND to_date IS NULL", 
			nativeQuery = true)
	List<Flight> getAllFlightsOfUser(@Param("flight_user") String flight_user);
	
	@Query(value = "SELECT * FROM flight WHERE user = :flight_user AND to_date IS NULL "
			+ "AND departing_location = :dep_location AND final_location = :fin_location", 
			nativeQuery = true)
	List<Flight> getAllSpecifiedFlights(@Param("flight_user") String flight_user, 
			@Param("dep_location") String dep_location, 
			@Param("fin_location") String fin_location);

	@Query(value = "SELECT * FROM flight WHERE "
			+ "trip_id = :trip_id AND to_date IS NULL", 
			nativeQuery = true)
	List<Flight> getAllFlightsOfTrip(@Param("trip_id")
	long trip_id);
	
	@Query(value = "SELECT * FROM flight WHERE flight_template_id = :ft_id AND "
			+ "user = :username", nativeQuery = true)
	List<Flight> getFlightsBelongingToTemplate(@Param("ft_id") long ft_id, 
			@Param("user") String username);
	
	@Query(value = "SELECT * FROM flight WHERE "
			+ "flight_template_id = :f_id AND user = :username", nativeQuery = true)
	List<Flight> getUserFlightsOfTemplate(@Param("f_id") long flightTemplateId, 
			@Param("username") String username);
	
	@Query(value = "SELECT * FROM flight WHERE "
			+ "flight_template_id = :flight_template_id AND to_date IS NULL", 
			nativeQuery = true)
	List<Flight> getAllFlightsOfTemplate(@Param("flight_template_id") long flight_template_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight SET to_date = :new_date WHERE "
			+ "trip_id = :trip_id AND to_date IS NULL", 
	nativeQuery = true)
	void deleteFlightsOfTrip(@Param("new_date") Date new_date, 
			@Param("trip_id") long trip_id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight f SET f.to_date = :new_date "
			+ "WHERE f.user = :flight_user AND f.to_date IS NULL", 
	nativeQuery = true)
	void deleteFlightsOfUser(@Param("flight_user") String flight_user, 
			@Param("new_date") Date new_date);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight SET to_date = :new_date WHERE "
			+ "departing_location = :template_departing_location AND "
			+ "final_location = :template_arriving_location AND "
			+ "starting_date = :template_starting_date AND "
			+ "ending_date = :template_ending_date", 
	nativeQuery = true)
	void deleteAllFlightsOfTemplate(@Param("new_date") Date new_date, 
			@Param("template_departing_location") String template_departing_location, 
			@Param("template_arriving_location") String template_arriving_location, 
			@Param("template_starting_date") Date template_starting_date, 
			@Param("template_ending_date") Date template_ending_date);
	
}
