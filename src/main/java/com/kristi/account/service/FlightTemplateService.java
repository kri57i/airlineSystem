package com.kristi.account.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Flight;
import com.kristi.account.model.FlightTemplate;
import com.kristi.account.model.Trip;
import com.kristi.account.repository.FlightRepository;
import com.kristi.account.repository.FlightTemplateRepository;
import com.kristi.account.repository.TripRepository;
import com.kristi.account.validators.CapacityValidator;
import com.kristi.account.validators.DateValidator;
import com.kristi.account.validators.LocationValidator;

@Service
public class FlightTemplateService {

	@Autowired
	private FlightTemplateRepository flightTemplateRepository;
	
	@Autowired
	private DestinationService destinationService;
	
	@Autowired
	private DateValidator dateValidator;
	
	@Autowired
	private LocationValidator locationValidator;
	
	@Autowired
	private CapacityValidator capacityValidator;
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private FlightService flightService;

	//The following method saves a flight template created by the administrator
	public FlightTemplate save(FlightTemplate flightTemplate) {
		return flightTemplateRepository.save(flightTemplate);
	}
	
	/*
	 * The following method saves a flight template when all the validation requirements
	 * are passed successfully
	 */
	public ModelAndView saveFlightTemplateRegistration(
			@Valid FlightTemplate flightTemplate,
			BindingResult result,
			ModelMap model
			) {
		//checking if any of the fields has any errors
		if(result.hasErrors()) {
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		//checking whether the departure date is set before or after today's date
		if(!dateValidator.validDepartureDate(flightTemplate.getDepartingDate())) {
			result.rejectValue("departingDate", null, "Departure date must not be before"
					+ " today's date ");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		//checking whether the arrival date is set before or after today's date
		if(!dateValidator.validArrivalDate(flightTemplate.getArrivingDate())) {
			result.rejectValue("arrivingDate", null, "Arrival date must not be before"
					+ " today's date ");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		//checking if the locations match
		if(locationValidator.matchingLocations(flightTemplate.getFirstLocation(), flightTemplate.getLastLocation())) {
			result.rejectValue("firstLocation", null, "Locations must not match");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		//checking if the arrival date is set after the departure date
		if(dateValidator.inverseValues(flightTemplate.getDepartingDate(), flightTemplate.getArrivingDate())) {
			result.rejectValue("departingDate", null, "Arrival date must be after departure date");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		//checking if the number of passengers is set less/equal than 0 or greater than 32
		if(capacityValidator.invalidCapacityInput(flightTemplate.getMaximumCapacity())) {
			result.rejectValue("maximumCapacity", null, "Number of passengers can't be equal/less than 0 or more than 32");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		//saving the flight template
		this.save(flightTemplate);
		model.addAttribute("flightTemplate", flightTemplate);
		return new ModelAndView("/admin/flightTemplateSuccess");
	}
	
	/*
	 * The following method returns a list of all the flight templates
	 */
	public ModelAndView getAllFlights() {
		List<FlightTemplate> flights = flightTemplateRepository.getAllFlights();
		return new ModelAndView("/admin/viewFlights", "flights", flights);
	}	
	
	//The folowing method returns a list of available flight templates for the given trip
	public List<FlightTemplate> getAvailableFlights(long tripId, String departure_location,
			String arrival_location, Date trip_departure_date, String username) {
		Trip trip = tripRepository.findById(tripId).orElse(null);
		/*
		 * The following stream() filters only those flight templates whose departure date is not 
		 * before trip's departure date and arrival date is not after trip's arrival date
		 */
		List<FlightTemplate> available = flightTemplateRepository
				.getAvaliableFlights(departure_location, arrival_location).stream()
				.filter(flight -> dateValidator.validDates(flight, trip)).collect(Collectors.toList());
		/*
		 * Getting a list of all flights of the given user that match the departure and arrival locations
		 */
		List<Flight> flights = flightRepository.getAllSpecifiedFlights(username, departure_location, 
				arrival_location);
		List<FlightTemplate> finalFlights = new ArrayList<>();
		boolean booked = false;
		/*
		 * Filtering all the flights that hasn't been booked before
		 */
		for(FlightTemplate flight : available) {
			booked = false;
			for(Flight userFlight : flights) {
				if(flight.getDepartingDate().equals(userFlight.getStartingDate())) {
					booked = true;
				}
			}
			/*
			 * If the variable has remained false, it means that the flight 
			 * hasn't been booked before
			 */
			if(!booked) {
				finalFlights.add(flight);
			}
		}
		return finalFlights;
	} 
	
	/*
	 * The following method deletes a flight template alongside with the flights
	 * that has been booked from this template
	 */
	public ModelAndView deleteFlightTemplate(@PathVariable long flightTemplateId) {
		FlightTemplate currentFlightTemplate = flightTemplateRepository
				.findById(flightTemplateId)
				.orElse(null);
		currentFlightTemplate.setToDate(new Date());
		this.save(currentFlightTemplate);
		/*
		 * After deleting the flight template, all the flights that has been booked from this
		 * flight template, have to be deleted
		 */
		flightService.deleteAllFlightsOfTemplate(new Date(), 
				currentFlightTemplate.getFirstLocation(), 
				currentFlightTemplate.getLastLocation(), 
				currentFlightTemplate.getDepartingDate(), 
				currentFlightTemplate.getArrivingDate());
		return new ModelAndView("redirect:/viewFlights");
	}
	
	/*
	 * The following method updates the user capacity of a flight template
	 */
	public void updateCapacity(int new_capacity, long flight_template_id) {
		flightTemplateRepository.updateCapacity(new_capacity, flight_template_id);
	}
	
	/*
	 * The following method removes a passanger from the flight template with the given id
	 */
	public void removePassenger(int new_capacity, long flight_template_id) {
		flightTemplateRepository.removePassenger(new_capacity, flight_template_id);
	}
}
