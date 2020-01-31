package com.kristi.account.web;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.kristi.account.repository.TripRepository;
import com.kristi.account.service.FlightService;
import com.kristi.account.service.FlightTemplateService;
import com.kristi.account.service.UserService;
import com.kristi.account.validators.AccessValidators;

@Controller
public class FlightController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private FlightTemplateService flightTemplateService;
	
	@Autowired
	private AccessValidators accessValidator;
	
	//logging possibility using the logger object
	Logger logger = LoggerFactory.getLogger(FlightController.class);
	
	//adds the currently logged in user's email as a model attribute
	@ModelAttribute("currentEmail")
	public String getCurrentUsername() {
		return userService.getCurrentUserName();
	}
	
	//mapping for /attachFlight/tripId url
	@GetMapping("/attachFlight/{tripId}") 
		public String attachFlight(ModelMap model, 
				@PathVariable long tripId, 
				String currentEmail) {
		/*
		 * The following condition checks whether the user who has created the trip is the same as 
		 * the user who is currently logged in
		 */
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return "/error";
		}
		//adding the trip's id as an attribute
			model.addAttribute("tripId", tripId);
			//getting all the available flights for the given trip
			model.addAttribute("flights", 
					flightTemplateService.getAvailableFlights(tripId,
							tripRepository.findById(tripId).orElse(null).getFromLocation(), 
							tripRepository.findById(tripId).orElse(null).getToLocation(), 
							tripRepository.findById(tripId).orElse(null).getDepartureDate(), userService.getCurrentUserName())
					);
			
			model.addAttribute("numberOfPossibilities", flightTemplateService.getAvailableFlights(tripId,
							tripRepository.findById(tripId).orElse(null).getFromLocation(), 
							tripRepository.findById(tripId).orElse(null).getToLocation(),
							tripRepository.findById(tripId).orElse(null).getDepartureDate(), userService.getCurrentUserName())
					.size()); 
			logger.info("Running getAvailableFlights() method to attach flights to the view");
			return "/home/attachFlight";
	}
	
	//mapping to bookFlight url when clicking book flight button
	@RequestMapping(value = "/bookFlight", 
			method = RequestMethod.GET) 
	public ModelAndView bookFlight(@RequestParam(value = "flightTemplateId", required = true)
	long flightTemplateId, 
			ModelMap model, 
			String currentUser,
			@RequestParam(value = "tripId", required = true) long tripId) {
		logger.info("Booking the selected flight via bookFlight() method");
		//calling the bookFlight method from the flight service
		return flightService.bookFlight(flightTemplateId, 
				model, 
				userService.getCurrentUserName(), 
				tripId);
	}
	
	//mapping to this url, will return a list of flights that belong to the given trip
	@GetMapping("/viewTripFlights/{tripId}")
	public ModelAndView viewFlights(
			@PathVariable long tripId,
			Model model) {
		logger.info("Attempting to get a list of flights beloning to the selected trip");
		return flightService.getAllFlightsOfTrip(tripId, model);
	}

	//mapping to this url, will return a list of flights for the given user
	@GetMapping("/viewMyFlights")
	public ModelAndView viewUserFlights() {
		return new ModelAndView("/home/viewMyFlights",
				"myFlights", flightService.getAllFlightsOfGivenUser(userService.getCurrentUserName()));
	}
	
	//adding the reasons list as an attribute
	@ModelAttribute("reasons")
	public List<String> initializeReasons() {
		return userService.reasons();
	}
	
	/*
	 * When the following url is passed, the deleteFlight method of the 
	 * flight service will be invoked
	 */
	@RequestMapping(value = "/deleteFlight/{flightId}", 
			method = RequestMethod.GET) 
		public ModelAndView deleteFlight(@PathVariable long flightId) {
		logger.info("Attempting to delete the selected flight");
			return flightService.deleteFlight(flightId);
	}
	
	/*
	 * When the following url is passed, the viewFlightDetails method of the 
	 * flight service will be invoked
	 */
	@RequestMapping(value = "/viewFlightDetails/{flightId}", 
			method = RequestMethod.GET)
	public ModelAndView viewFlightDetails(@PathVariable long flightId) {
		logger.info("Attempting to view details of selected flight via viewFlightDetails() method");
		return flightService.viewFlightDetails(flightId);
	}
}
