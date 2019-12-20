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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;



@Entity
@Table(name = "trip")
@EntityListeners(AuditingEntityListener.class)
public class Trip {
	
	public Trip () {}
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 
	 @Column(nullable = false, name = "trip_reason")
	 @NotEmpty
	 private String reason;
	 
	 @Column(nullable = false, name = "trip_description")
	 @NotEmpty
	 private String description;
	 
	 @Column(nullable = false, name = "from_location")
	 @NotEmpty
	 private String fromLocation;
	 
	 @Column(nullable = false, name = "to_location")
	 @NotEmpty
	 private String toLocation;
	 
	 @Column(nullable = false, name = "departure_date")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 @NotNull(message = "Departure date must not be empty")
	 private Date departureDate;
	 
	 @Column(nullable = false, name = "arrival_date")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 @NotNull(message = "Arrival date must not be empty")
	 private Date arrivalDate;
	 
     @Column(name = "created_at")
     @Temporal(TemporalType.TIMESTAMP)
	 @CreatedDate
	 private Date createdAt;
     
     @Column(name = "to_date")
     private Date toDate;
     
     @ManyToOne(cascade = CascadeType.ALL)
 	 @JoinColumn(name = "user_id")
 	 private User user;
     

     @Column(nullable = false, name = "trip_user")
     private String username;
     
     @Column(nullable = false, name = "trip_status")
     private String status;

     
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
     
     
}
