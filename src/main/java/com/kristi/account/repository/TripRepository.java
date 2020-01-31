package com.kristi.account.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristi.account.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long>{

	/*
	 * The following query returns a list of trips that belong to the 
	 * user with the given email
	 */
	@Query(value = "SELECT * FROM trip WHERE trip_user = :trip_user "
			+ "AND to_date IS NULL", 
			nativeQuery = true)
	List<Trip> getAllTripsOfUser(@Param("trip_user") String trip_user);

	@Query(value = "SELECT * FROM trip WHERE trip_user = :trip_user "
			+ "AND to_date IS NULL AND trip_status = :trip_status", nativeQuery = true)
	List<Trip> getTripsAccordingToStatus(@Param("trip_user") String trip_user,
										 @Param("trip_status") String trip_status);
	/*
	 * The following query returns a list of trips with the required status
	 */
	@Query(value = "SELECT * FROM trip WHERE trip_status = :trip_status "
			+ "AND to_date IS NULL", 
			nativeQuery = true)
	List<Trip> getAllTrips(@Param("trip_status") String trip_status);
	
	/*
	 * The following query deletes the trip with the given id
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE trip t SET t.to_date = :new_date "
			+ "WHERE t.id = :trip_id", 
	nativeQuery = true)
	void deleteTrip(@Param("trip_id") long trip_id, 
			@Param("new_date") Date new_date);
	
	/*
	 * The following query deletes all the trips that belong
	 * to the given user email
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE trip t SET t.to_date = :new_date "
			+ "WHERE t.trip_user = :trip_user", 
	nativeQuery = true)
	void deleteTripsOfUser(@Param("trip_user") String trip_user, 
			@Param("new_date") Date new_date);
}
