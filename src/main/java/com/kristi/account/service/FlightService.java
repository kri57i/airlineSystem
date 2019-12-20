package com.kristi.account.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Flight;
import com.kristi.account.model.FlightTemplate;
import com.kristi.account.model.Trip;
import com.kristi.account.repository.FlightRepository;
import com.kristi.account.repository.FlightTemplateRepository;
import com.kristi.account.repository.TripRepository;
import com.kristi.account.validators.AccessValidators;
import com.kristi.account.validators.DateValidator;
import com.kristi.account.validators.LocationValidator;

@Service
public class FlightService {
	
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private AccessValidators accessValidator;
	
	@Autowired
	private DestinationService destinationService;
	
	@Autowired
	private DateValidator dateValidator;
	
	@Autowired
	private LocationValidator locationValidator;
	
	@Autowired
	private FlightTemplateRepository flightTemplateRepository;
	
	@Autowired
	private FlightTemplateService flightTemplateService;
	
	@Autowired
	private UserService userService;
	
	public Flight save(Flight flight) {
		return flightRepository.save(flight);
	}
	
	/*public ModelAndView saveFlightRegistration(@Valid Flight flight, 
			BindingResult result, 
			ModelMap model,
			String currentUser, 
			long tripId) {
		if(result.hasErrors()) {
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		
		if(!dateValidator.validFlightDepartureDate(flight.getStartingDate(), 
				tripRepository.findById(tripId).orElse(null).getDepartureDate())) {
			result.rejectValue("startingDate", null, "Flight departure date must be after trip's "
					+ "departure date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		if(!dateValidator.validFlightArrivalDate(flight.getEndingDate(), 
				tripRepository.findById(tripId).orElse(null).getArrivalDate())) {
			result.rejectValue("endingDate", null, "Flight arrival date must be before trip's "
					+ "arrival date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		if(dateValidator.matchingDates(flight.getStartingDate(), flight.getEndingDate())) {
			result.rejectValue("endingDate", null, "Dates must not match");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		
		if(dateValidator.inverseValues(flight.getStartingDate(), flight.getEndingDate())) {
			result.rejectValue("startingDate", null, "Arrival date must be after departure date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		
		if(!dateValidator.validDepartureDate(flight.getStartingDate())) {
			result.rejectValue("startingDate", null, "Departure date must not be "
					+ "before today's date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		
		if(!dateValidator.validArrivalDate(flight.getEndingDate())) {
			result.rejectValue("endingDate", null, "Arrival date must not be "
					+ "before today's date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(), 
					tripRepository.findById(tripId).orElse(null).getToLocation()));
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		if(locationValidator.matchingLocations(flight.getDepartingLocation(), 
				flight.getFinalLocation())) {
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(tripId).orElse(null).getFromLocation(),
					tripRepository.findById(tripId).orElse(null).getToLocation()
					));
			result.rejectValue("finalLocation", null, "Locations must not match!");
			return new ModelAndView("/home/createFlight", "tripId", tripId);
		}
		flight.setUsername(currentUser);
		flight.setCreatedAt(new Date());
		flight.setTrip(tripRepository.findById(tripId).orElse(null));
		this.save(flight);
		model.addAttribute("flight", flight);
		model.addAttribute("tripId", flight.getTrip().getId());
		return new ModelAndView("/home/flightSuccess");
	}*/
	
	public ModelAndView bookFlight(long flightTemplateId, 
			ModelMap model, 
			String currentUser, 
			long tripId) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		FlightTemplate currentFlightTemplate = flightTemplateRepository.findById(flightTemplateId).orElse(null);
		Flight flight = new Flight();
		flight.setFlightTemplate(currentFlightTemplate);
		flight.setCreatedAt(new Date());
		flight.setDepartingLocation(currentFlightTemplate.getFirstLocation());
		flight.setEndingDate(currentFlightTemplate.getArrivingDate());
		flight.setFinalLocation(currentFlightTemplate.getLastLocation());
		flight.setStartingDate(currentFlightTemplate.getDepartingDate());
		flight.setTrip(tripRepository.findById(tripId).orElse(null));
		flight.setUsername(currentUser);
		flightTemplateService.updateCapacity(currentFlightTemplate.getCurrentCapacity() + 1, 
				currentFlightTemplate.getId());
		this.save(flight);
		model.addAttribute("flight", flight);
		model.addAttribute("tripId", flight.getTrip().getId());
		return new ModelAndView("/home/flightBookingSuccess");
	}
	
	public ModelAndView getAllFlightsOfTrip(@PathVariable 
			long tripId, 
			Model model) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		List<Flight> flights = flightRepository.getAllFlightsOfTrip(tripId);
		model.addAttribute("numberOfFlights", flights.size());
		return new ModelAndView("/home/viewTripFlights", "flights", flights);
	}
	
	public List<Flight> getAllFlightsOfTrip(long tripId) {
		return flightRepository.getAllFlightsOfTrip(tripId);
	}
	
	public ModelAndView viewFlightDetails(@PathVariable long flightId) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				flightRepository.findById(flightId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Flight flight = flightRepository.findById(flightId).orElse(null);
		return new ModelAndView("/home/viewFlightDetails", "flight", flight);
	}
	
	/*public String editFlight(@PathVariable long flightId, ModelMap model, 
			@ModelAttribute("currentEmail") String currentEmail) {
		if(!accessValidator.matchingUsers(currentEmail, flightRepository.findById(flightId)
				.orElse(null).getUsername())) {
			return "/home/error";
		}
		Flight flight = flightRepository.findById(flightId).orElse(null);
		model.addAttribute("flight", flight);
		model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
				tripRepository.findById(flight.getTrip().getId()).orElse(null).getFromLocation(), 
				tripRepository.findById(flight.getTrip().getId()).orElse(null).getToLocation()));
		return "/home/editFlight";
	} */
	
	/*public ModelAndView editAndSaveFlight(@Valid @ModelAttribute("flight") Flight flight, 
			BindingResult result, ModelMap model) {
		Flight currentFlight = flightRepository.findById(flight.getId()).orElse(null);
		if(result.hasErrors()) {
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		if(dateValidator.matchingDates(flight.getStartingDate(), flight.getEndingDate())) {
			result.rejectValue("endingDate", null, "Dates must not match");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		if(!dateValidator.validArrivalDate(flight.getEndingDate())) {
			result.rejectValue("endingDate", null, "Arrival date must not be before today's date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		if(!dateValidator.validDepartureDate(flight.getStartingDate())) {
			result.rejectValue("startingDate", null, "Departure date must not be before today's date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}

		if(dateValidator.inverseValues(flight.getStartingDate(), flight.getEndingDate())) {
			result.rejectValue("endingDate", null, "Arrival date must not be before departure date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		if(!dateValidator.validFlightDepartureDate(flight.getStartingDate(), 
				currentFlight.getTrip().getDepartureDate())) {
			result.rejectValue("startingDate", null, "Flight departure date must be after trip's "
					+ "departure date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		if(!dateValidator.validFlightArrivalDate(flight.getEndingDate(), 
				currentFlight.getTrip().getArrivalDate())) {
			result.rejectValue("endingDate", null, "Flight arrival date must be before trip's "
					+ "arrival date");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		if(locationValidator.matchingLocations(flight.getDepartingLocation(), 
				flight.getFinalLocation())) {
			result.rejectValue("finalLocation", null, "Locations must not match!");
			model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getFromLocation(), 
					tripRepository.findById(currentFlight.getTrip().getId()).orElse(null).getToLocation()));
			return new ModelAndView("/home/editFlight", "flight", flight);
		}
		currentFlight.setDepartingLocation(flight.getDepartingLocation());
		currentFlight.setFinalLocation(flight.getFinalLocation());
		currentFlight.setStartingDate(flight.getStartingDate());
		currentFlight.setEndingDate(flight.getEndingDate());
		this.save(currentFlight);
		return new ModelAndView("redirect:/viewFlights/" + currentFlight.getTrip().getId());
	}*/
	
	public void removeUserNumberFromTemplate(String current_user) {
		List<Flight> flightsOfUser = flightRepository.getAllFlightsOfUser(current_user);
		for(Flight flight : flightsOfUser) {
			flightTemplateService.removePassenger(flight.getFlightTemplate().getCurrentCapacity() - 1, 
					flight.getFlightTemplate().getId());
		}
	}
	
	public ModelAndView deleteFlight(@PathVariable long flightId) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				flightRepository.findById(flightId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Flight currentFlight = flightRepository.findById(flightId).orElse(null);
		currentFlight.setToDate(new Date());
		this.save(currentFlight);
		flightTemplateService.removePassenger(currentFlight.getFlightTemplate().getCurrentCapacity() - 1, 
				currentFlight.getFlightTemplate().getId());
		return new ModelAndView("redirect:/viewTripFlights/" + currentFlight.getTrip().getId());
	}
	
	public void deleteFlightsOfTrip(Date new_date, long tripId) {
		flightRepository.deleteFlightsOfTrip(new_date, tripId);
	}
	
	public void deleteFlightsOfUser(String flight_user) {
		flightRepository.deleteFlightsOfUser(flight_user, new Date());
	}
	
	public void deleteAllFlightsOfTemplate(Date new_date, 
			String template_departing_location, 
			String template_arriving_location, 
			Date template_starting_date, 
			Date template_ending_date) {
		flightRepository.deleteAllFlightsOfTemplate(new_date, template_departing_location, template_arriving_location, template_starting_date, template_ending_date);
	}
	
	public List<Flight> getAllFlightsOfTemplate(long flightTemplateId) {
		return flightRepository.getAllFlightsOfTemplate(flightTemplateId);
	}
}
