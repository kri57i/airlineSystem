package com.kristi.account.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


/*
 * Flight template entity, that will create instances of flight templates, that 
 * can be booked by users
 */
@Entity
@Table(name = "flight_template")
@EntityListeners(AuditingEntityListener.class)
public class FlightTemplate {

	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 
	 @Column(nullable = false, name = "first_location")
	 @NotEmpty
	 private String firstLocation;
	 
	 @Column(nullable = false, name = "last_location")
	 @NotEmpty
	 private String lastLocation;
	 
	 @Column(nullable = false, name = "departing_date")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 @Temporal(TemporalType.DATE)
	 @NotNull
	 private Date departingDate;
	 
	 @Column(nullable = false, name = "arriving_date")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 @Temporal(TemporalType.DATE)
	 @NotNull
	 private Date arrivingDate;
	 
	 /*
	  * Maximum number of passengers that can book the current flight template
	  */
	 @Column(nullable = false, name = "maximum_capacity")
	 private int maximumCapacity;
	 
	 @Column(nullable = false, name = "current_capacity")
	 private int currentCapacity;
	 
	 @Column(name = "to_date")
	 @Temporal(TemporalType.DATE)
     private Date toDate;
	 
	//Standard getters and setters
	public int getMaximumCapacity() {
		return maximumCapacity;
	}

	public void setMaximumCapacity(int maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
	}

	public int getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(int currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstLocation() {
		return firstLocation;
	}

	public void setFirstLocation(String firstLocation) {
		this.firstLocation = firstLocation;
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Date getDepartingDate() {
		return departingDate;
	}

	public void setDepartingDate(Date departingDate) {
		this.departingDate = departingDate;
	}

	public Date getArrivingDate() {
		return arrivingDate;
	}

	public void setArrivingDate(Date arrivingDate) {
		this.arrivingDate = arrivingDate;
	}
	 
}
