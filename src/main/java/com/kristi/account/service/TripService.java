package com.kristi.account.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Flight;
import com.kristi.account.model.Trip;
import com.kristi.account.repository.TripRepository;
import com.kristi.account.validators.AccessValidators;
import com.kristi.account.validators.DateValidator;
import com.kristi.account.validators.LocationValidator;

@Service
public class TripService {

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccessValidators accessValidator;
	
	@Autowired
	private DateValidator dateValidator;
	
	@Autowired
	private LocationValidator locationValidator;
	
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private FlightTemplateService flightTemplateService;
	
	
	public Trip save(Trip trip) {
		return tripRepository.save(trip);
	}
	
	/*
	 * Saving the trip after no errors has been thrown from the validations
	 */
	public String saveTripRegistration(@Valid Trip trip,
			BindingResult result,
			ModelMap model,
			String currentUser) {
		if(result.hasErrors()) {
			//if there is any error during the validation
			return "/home/createTrip";
		}
		
		//if the arrival date is not valid
		if(!dateValidator.validArrivalDate(trip.getArrivalDate())) {
			result.rejectValue("arrivalDate", null, "Arrival date must not be before today's date");
			model.addAttribute("trip", trip);
			return "/home/createTrip";
		}
		
		//if the departure date is not valid
		if(!dateValidator.validDepartureDate(trip.getDepartureDate())) {
			result.rejectValue("departureDate", null, "Departure date must not be before today's date");
			model.addAttribute("trip", trip);
			return "/home/createTrip";
		}
		
		//if the arrival date is before departure date
		if(dateValidator.inverseValues(trip.getDepartureDate(), trip.getArrivalDate())) {
			result.rejectValue("departureDate", null, "Arrival date must not be before departure date");
			model.addAttribute("trip", trip);
			return "/home/createTrip";
		}
		
		//if the locations match
		if(locationValidator.matchingLocations(trip.getFromLocation(), trip.getToLocation())) {
			result.rejectValue("toLocation", null, "Locations must not match!");
			model.addAttribute("trip", trip);
			return "/home/createTrip";
		}
		//setting user fields
		trip.setUser(userService.findByEmail(currentUser));
		trip.setUsername(currentUser);
		trip.setCreatedAt(new Date());
		trip.setStatus("CREATED");
		this.save(trip);
		/*
		 * Passing the trip object as an attribute to the thymeleaf view
		 */
		model.addAttribute("trip", trip);
		return "/home/tripSuccess";
	}
	
	public String editTrip(@PathVariable long tripId, ModelMap model) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return "/error";
		}
		Trip trip = tripRepository.findById(tripId).orElse(null);
		model.addAttribute("trip", trip);
		return "/home/editTrip";
	}
	
	//the following method performs the required validations on the trip editing form
	public ModelAndView editAndSaveTrip(@Valid @ModelAttribute("trip") Trip trip,
			BindingResult result) {
		//checking if there are any errors while saving the updated trip
		if(result.hasErrors()) {
			return new ModelAndView("/home/editTrip");
		}
		//making sure that all the values are correct
		if(!dateValidator.validArrivalDate(trip.getArrivalDate())) {
			result.rejectValue("arrivalDate", null, "Arrival date must not be before today's date");
			return new ModelAndView("/home/editTrip", "trip", trip);
		}
		if(!dateValidator.validDepartureDate(trip.getDepartureDate())) {
			result.rejectValue("departureDate", null, "Departure date must not be before today's date");
			return new ModelAndView("/home/editTrip", "trip", trip);
		}
		if(dateValidator.inverseValues(trip.getDepartureDate(), trip.getArrivalDate())) {
			result.rejectValue("departureDate", null, "Departure date must not be before arrival date");
			return new ModelAndView("/home/editTrip", "trip", trip);
		}
		if(locationValidator.matchingLocations(trip.getFromLocation(), trip.getToLocation())) {
			result.rejectValue("toLocation", null, "Locations must not match!");
			return new ModelAndView("/home/editTrip", "trip", trip);
		}
		Trip currentTrip = tripRepository.findById(trip.getId()).orElse(null);
		currentTrip.setDescription(trip.getDescription());
		currentTrip.setReason(trip.getReason());
		currentTrip.setDepartureDate(trip.getDepartureDate());
		currentTrip.setArrivalDate(trip.getArrivalDate());
		currentTrip.setFromLocation(trip.getFromLocation());
		currentTrip.setToLocation(trip.getToLocation());
		this.save(currentTrip);
		ModelAndView mdv = new ModelAndView("redirect:/viewTrips");
		//returning the ModelAndView object
		return mdv;
	}

	public int getTripsNumberAccordingToStatus(String trip_user,
											   String trip_status) {
		return tripRepository.getTripsAccordingToStatus(trip_user, trip_status).size();
	}
	
	/*
	 * The following method returns all the trips of the given user
	 */
	public ModelAndView getAllTripsOfUser(@PathVariable String trip_user, 
			ModelMap model) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), trip_user)) {
			return new ModelAndView("/home/error");
		}
		List<Trip> tripsOfUser = tripRepository.getAllTripsOfUser(trip_user);
		model.addAttribute("totalNumberOfTrips", tripsOfUser.size());
		return new ModelAndView("/home/viewTrips", "tripsOfUser", tripsOfUser);
	}
	
	//getting all the trips with the 'WAITING FOR APPROVAL' status
	public ModelAndView getAllTrips() {
		List<Trip> trips = tripRepository.getAllTrips("WAITING FOR APPROVAL");
		return new ModelAndView("/admin/tripRequests", "trips", trips);
	}

	public ModelAndView getStatistics(String currentUser) {
		int numberOfTrips = tripRepository.getAllTripsOfUser(currentUser).size();
		return new ModelAndView("/home/viewStatistics", "numberOfTrips", numberOfTrips);
	}
	
	//deleting the given trip
	public ModelAndView deleteTrip(@PathVariable long tripId) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Trip currentTrip = tripRepository.findById(tripId).orElse(null);
		/*
		 * Removing the passanger from each flight of the trip
		 */
		List<Flight> flightsOfTrip = flightService.getAllFlightsOfTrip(tripId);
		for(Flight flight : flightsOfTrip) {
				flightTemplateService.removePassenger(flight.getFlightTemplate().getCurrentCapacity() - 1, 
						flight.getFlightTemplate().getId());
		}
		currentTrip.setToDate(new Date());
		this.save(currentTrip);
		flightService.deleteFlightsOfTrip(new Date(), tripId);
		return new ModelAndView("redirect:/viewTrips");
	}
	
	//deletes all the trips of the given user
	public void deleteTripsOfUser(String trip_user) {
		tripRepository.deleteTripsOfUser(trip_user, new Date());
	}
	
	//returns the viewDetails view and the trip object
	public ModelAndView viewDetails(@PathVariable Long tripId) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Trip trip = tripRepository.findById(tripId).orElse(null);
		return new ModelAndView("/home/viewDetails", "trip", trip);
	}
	
	public ModelAndView tripDetails(@PathVariable long tripId) {
		Trip trip = tripRepository.findById(tripId).orElse(null);
		return new ModelAndView("/admin/tripDetails", "trip", trip);
	}
	
	public ModelAndView sendApproval(@PathVariable long tripId) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return new ModelAndView("/error");
		}
		Trip trip = tripRepository.findById(tripId).orElse(null);
		trip.setStatus("WAITING FOR APPROVAL");
		this.save(trip);
		return new ModelAndView("/home/approvalSent", "trip", trip);
	}
	
	//The following method approves a trip
	public ModelAndView approveTrip(@PathVariable long tripId) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				"admin@gmail.com")) {
			return new ModelAndView("/error");
		}
		Trip trip = tripRepository.findById(tripId).orElse(null);
		//changing the status to 'APPROVED'
		trip.setStatus("APPROVED");
		this.save(trip);
		return new ModelAndView("redirect:/tripRequests");
	}
	
	//The following method rejects a trip
	public ModelAndView rejectTrip(@PathVariable long tripId) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				"admin@gmail.com")) {
			return new ModelAndView("/error");
		}
		Trip trip = tripRepository.findById(tripId).orElse(null);
		//changing the status to 'REJECTED'
		trip.setStatus("REJECTED");
		this.save(trip);
		return new ModelAndView("redirect:/tripRequests");
	}
}
