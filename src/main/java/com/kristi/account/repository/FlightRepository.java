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
	
	/*
	 * The following query returns a list of active flights beloning to the specified
	 * user
	 */
	@Query(value = "SELECT * FROM flight WHERE user = :flight_user AND to_date IS NULL", 
			nativeQuery = true)
	List<Flight> getAllFlightsOfUser(@Param("flight_user") String flight_user);
	
	/*
	 * The following query returns a list of active flights belonging to the specified user 
	 * where the destinations match the destinations specified as parameters
	 */
	@Query(value = "SELECT * FROM flight WHERE user = :flight_user AND to_date IS NULL "
			+ "AND departing_location = :dep_location AND final_location = :fin_location", 
			nativeQuery = true)
	List<Flight> getAllSpecifiedFlights(@Param("flight_user") String flight_user, 
			@Param("dep_location") String dep_location, 
			@Param("fin_location") String fin_location);

	/*
	 * The following query returns a list of flights belonging to the trip with the 
	 * given id
	 */
	@Query(value = "SELECT * FROM flight WHERE "
			+ "trip_id = :trip_id AND to_date IS NULL", 
			nativeQuery = true)
	List<Flight> getAllFlightsOfTrip(@Param("trip_id")
	long trip_id);
	
	/*
	 * The following query returns a list of flights that are booked from the given template with the
	 * given id and belonging to the given user
	 */
	@Query(value = "SELECT * FROM flight WHERE flight_template_id = :ft_id AND "
			+ "user = :username", nativeQuery = true)
	List<Flight> getFlightsBelongingToTemplate(@Param("ft_id") long ft_id, 
			@Param("user") String username);
	
	/*
	 * The following query returns a list of flights for the given user 
	 * with the given flight template(model) id
	 */
	@Query(value = "SELECT * FROM flight WHERE "
			+ "flight_template_id = :f_id AND user = :username", nativeQuery = true)
	List<Flight> getUserFlightsOfTemplate(@Param("f_id") long flightTemplateId, 
			@Param("username") String username);
	
	/*
	 * The following query returns a list of flights that belong to a given flight template
	 */
	@Query(value = "SELECT * FROM flight WHERE "
			+ "flight_template_id = :flight_template_id AND to_date IS NULL", 
			nativeQuery = true)
	List<Flight> getAllFlightsOfTemplate(@Param("flight_template_id") long flight_template_id);
	
	/*
	 * The following query deletes(updates the to_date field) all the flights that
	 * belong to the trip with the given id
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight SET to_date = :new_date WHERE "
			+ "trip_id = :trip_id AND to_date IS NULL", 
	nativeQuery = true)
	void deleteFlightsOfTrip(@Param("new_date") Date new_date, 
			@Param("trip_id") long trip_id);
	
	/*
	 * The following query deletes(updates the to_date field) all the flights that
	 * belong to the user with the given name
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE flight f SET f.to_date = :new_date "
			+ "WHERE f.user = :flight_user AND f.to_date IS NULL", 
	nativeQuery = true)
	void deleteFlightsOfUser(@Param("flight_user") String flight_user, 
			@Param("new_date") Date new_date);
	
	/*
	 * The following query deletes all the flights belonging to a flight template, 
	 * when the given template is deleted by the administrator
	 */
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
