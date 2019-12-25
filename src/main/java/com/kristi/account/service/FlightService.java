package com.kristi.account.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Flight;
import com.kristi.account.model.FlightTemplate;
import com.kristi.account.repository.FlightRepository;
import com.kristi.account.repository.FlightTemplateRepository;
import com.kristi.account.repository.TripRepository;
import com.kristi.account.validators.AccessValidators;

@Service
public class FlightService {
	
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private AccessValidators accessValidator;
	
	@Autowired
	private FlightTemplateRepository flightTemplateRepository;
	
	@Autowired
	private FlightTemplateService flightTemplateService;
	
	@Autowired
	private UserService userService;
	
	/*
	 * The following method saves a flight
	 */
	public Flight save(Flight flight) {
		return flightRepository.save(flight);
	}
	
	/*
	 * The following function saves a flight and returns the flight booking success
	 * template
	 */
	public ModelAndView bookFlight(long flightTemplateId, 
			ModelMap model, 
			String currentUser, 
			long tripId) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		//finding the flight template that will be attached to the flight
		FlightTemplate currentFlightTemplate = flightTemplateRepository.findById(flightTemplateId).orElse(null);
		//setting the properties of the flight
		Flight flight = new Flight();
		flight.setFlightTemplate(currentFlightTemplate);
		flight.setCreatedAt(new Date());
		flight.setDepartingLocation(currentFlightTemplate.getFirstLocation());
		flight.setEndingDate(currentFlightTemplate.getArrivingDate());
		flight.setFinalLocation(currentFlightTemplate.getLastLocation());
		flight.setStartingDate(currentFlightTemplate.getDepartingDate());
		flight.setTrip(tripRepository.findById(tripId).orElse(null));
		flight.setUsername(currentUser);
		//incrementing the capacity in the flight template
		flightTemplateService.updateCapacity(currentFlightTemplate.getCurrentCapacity() + 1, 
				currentFlightTemplate.getId());
		this.save(flight);
		//passing the flight as an attribute to the thymeleaf view
		model.addAttribute("flight", flight);
		//passing the trip id as an attribute to the thymeleaf view
		model.addAttribute("tripId", flight.getTrip().getId());
		//heading to flight booking success template
		return new ModelAndView("/home/flightBookingSuccess");
	}
	
	/*
	 * The following function gets a trip id and returns a list of flights 
	 * belonging to that trip
	 */
	public ModelAndView getAllFlightsOfTrip(@PathVariable 
			long tripId, 
			Model model) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		List<Flight> flights = flightRepository.getAllFlightsOfTrip(tripId);
		model.addAttribute("numberOfFlights", flights.size());
		//adding the list as an object at the thymeleaf view
		return new ModelAndView("/home/viewTripFlights", "flights", flights);
	}
	
	//calling the getAllFlightsOfTrip method of the flight repository
	public List<Flight> getAllFlightsOfTrip(long tripId) {
		return flightRepository.getAllFlightsOfTrip(tripId);
	}
	
	/*
	 * The following method returns a flight details view alongside with the flight object
	 */
	public ModelAndView viewFlightDetails(@PathVariable long flightId) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				flightRepository.findById(flightId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Flight flight = flightRepository.findById(flightId).orElse(null);
		return new ModelAndView("/home/viewFlightDetails", "flight", flight);
	}
	
	/*
	 * The following method decrements the passenger number with 1 
	 * on each flight of the user(passenger)
	 */
	public void removeUserNumberFromTemplate(String current_user) {
		List<Flight> flightsOfUser = flightRepository.getAllFlightsOfUser(current_user);
		for(Flight flight : flightsOfUser) {
			flightTemplateService.removePassenger(flight.getFlightTemplate().getCurrentCapacity() - 1, 
					flight.getFlightTemplate().getId());
		}
	}
	
	/*
	 * The following method deletes a flight
	 */
	public ModelAndView deleteFlight(@PathVariable long flightId) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				flightRepository.findById(flightId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Flight currentFlight = flightRepository.findById(flightId).orElse(null);
		currentFlight.setToDate(new Date());
		this.save(currentFlight);
		flightTemplateService.removePassenger(currentFlight
				.getFlightTemplate()
				.getCurrentCapacity() - 1, 
				currentFlight.getFlightTemplate().getId());
		return new ModelAndView("redirect:/viewTripFlights/" + currentFlight.getTrip().getId());
	}
	
	//The following method deletes all the flights of the trip with the given id
	public void deleteFlightsOfTrip(Date new_date, long tripId) {
		flightRepository.deleteFlightsOfTrip(new_date, tripId);
	}
	
	//The following method deletes all the flights of the user with the given email
	public void deleteFlightsOfUser(String flight_user) {
		flightRepository.deleteFlightsOfUser(flight_user, new Date());
	}
	
	/*
	 * The following method deletes all the flights that belong to the flight template that 
	 * is being deleted by the administrator
	 */
	public void deleteAllFlightsOfTemplate(Date new_date, 
			String template_departing_location, 
			String template_arriving_location, 
			Date template_starting_date, 
			Date template_ending_date) {
		flightRepository.deleteAllFlightsOfTemplate(new_date, 
				template_departing_location, 
				template_arriving_location, 
				template_starting_date, 
				template_ending_date);
	}
	
	/*
	 * The following method returns a list of flights that belong to the flight template
	 * with the given id
	 */
	public List<Flight> getAllFlightsOfTemplate(long flightTemplateId) {
		return flightRepository.getAllFlightsOfTemplate(flightTemplateId);
	}
}
