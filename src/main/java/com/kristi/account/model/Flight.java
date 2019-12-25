package com.kristi.account.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


/*
 * Flight entity, that will keep instances of flights booked by users
 */
@Entity
@Table(name = "flight")
@EntityListeners(AuditingEntityListener.class)
public class Flight {

	
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 
	 @Column(nullable = false, name = "departing_location")
	 @NotEmpty
	 private String departingLocation;
	 
	 @Column(nullable = false, name = "final_location")
	 @NotEmpty
	 private String finalLocation;
	 
	 @Column(nullable = false, name = "starting_date")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 @Temporal(TemporalType.DATE)
	 @NotNull
	 private Date startingDate;
	 
	 @Column(nullable = false, name = "ending_date")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 @Temporal(TemporalType.DATE)
	 @NotNull
	 private Date endingDate;
	 
	 @Column(name = "created_at")
     @Temporal(TemporalType.TIMESTAMP)
	 @CreatedDate
	 private Date createdAt;
	 
	 @Column(name = "to_date")
	 @Temporal(TemporalType.DATE)
     private Date toDate;
	 
	 /*
	  * A flight has a many to one relationship to trip entity,
	  * which means that multiple flights can belong to a single trip
	  */
	 @ManyToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "trip_id")
	 private Trip trip;
	 
	 /*
	  * A flight has a many to one relationship to FlightTemplate entity,
	  * which means that multiple flights can belong to a single flight template
	  */
	 @ManyToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "flight_template_id")
	 private FlightTemplate flightTemplate;
	 
	 // Flight user
	 @Column(name = "user")
	 private String username;
	 

	 //Standard getters and setters
	public FlightTemplate getFlightTemplate() {
		return flightTemplate;
	}

	public void setFlightTemplate(FlightTemplate flightTemplate) {
		this.flightTemplate = flightTemplate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartingLocation() {
		return departingLocation;
	}

	public void setDepartingLocation(String departingLocation) {
		this.departingLocation = departingLocation;
	}

	public String getFinalLocation() {
		return finalLocation;
	}

	public void setFinalLocation(String finalLocation) {
		this.finalLocation = finalLocation;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	
}
