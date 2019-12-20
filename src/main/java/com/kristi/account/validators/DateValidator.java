package com.kristi.account.validators;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristi.account.model.FlightTemplate;
import com.kristi.account.model.Trip;
import com.kristi.account.repository.FlightRepository;


@Service
public class DateValidator {
	
	@Autowired
	private FlightRepository flightRepository;
	
	
	public DateValidator() {}
	
	public boolean validDepartureDate(Date departureDate) {
		if(departureDate.before(new Date())) {
			return false;
		}
		return true;
	}
	
	public boolean matchingDates(Date departureDate, Date arrivalDate) {
		if(departureDate.equals(arrivalDate)) {
			return true;
		}
		return false;
	}
	
	public boolean validArrivalDate(Date arrivalDate) {
		if(arrivalDate.before(new Date())) {
			return false;
		}
		return true;
	}
	
	public boolean inverseValues(Date departureDate, Date arrivalDate) {
		if(arrivalDate.before(departureDate)) {
			return true;
		}
		return false;
	}
	
	public boolean validFlightDepartureDate(Date flightDepartureDate, 
			Date tripDepartureDate) {
		if(flightDepartureDate.equals(tripDepartureDate) 
				|| flightDepartureDate.before(tripDepartureDate)) {
			return false;
		}
		return true;
	}
	
	public boolean validFlightArrivalDate(Date flightArrivalDate, 
			Date tripArrivalDate) {
		if(flightArrivalDate.equals(tripArrivalDate) 
				|| flightArrivalDate.after(tripArrivalDate)) {
			return false;
		}
		return true;
	}
	
	public boolean validDates(FlightTemplate flight, Trip trip) {
		if(flight.getDepartingDate().before(trip.getDepartureDate()) || 
				flight.getArrivingDate().after(trip.getArrivalDate())) {
			return false;
		}
		return true;
	}
	
	public boolean previouslyBooked(long flightTemplateId, String username) {
		if(flightRepository.getUserFlightsOfTemplate(flightTemplateId, username).isEmpty()) {
			return true;
		}
		return false;
	}
}
