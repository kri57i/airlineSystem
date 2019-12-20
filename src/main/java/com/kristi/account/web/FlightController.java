package com.kristi.account.web;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Flight;
import com.kristi.account.model.FlightTemplate;
import com.kristi.account.model.Trip;
import com.kristi.account.repository.FlightTemplateRepository;
import com.kristi.account.repository.TripRepository;
import com.kristi.account.service.DestinationService;
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
	private DestinationService destinationService;
	
	@Autowired
	private FlightTemplateService flightTemplateService;
	
	@Autowired
	private FlightTemplateRepository flightTemplateRepository;
	
	@Autowired
	private AccessValidators accessValidator;
	
	Logger logger = LoggerFactory.getLogger(FlightController.class);
	
	@ModelAttribute("currentEmail")
	public String getCurrentUsername() {
		return userService.getCurrentUserName();
	}
	
	
	/*@GetMapping("/createFlight/{tripId}")
	public String createFlight(Model model, @PathVariable long tripId) {
		model.addAttribute("flight", new Flight());
		model.addAttribute("destinations", destinationService.getAllAvailableDestinations(
				tripRepository.findById(tripId).orElse(null).getFromLocation(), 
				tripRepository.findById(tripId).orElse(null).getToLocation()));
		model.addAttribute("trip", tripRepository.findById(tripId).orElse(null));
		model.addAttribute("tripId", tripId);
		return "/home/createFlight";
	}*/
	
	@GetMapping("/attachFlight/{tripId}") 
		public String attachFlight(ModelMap model, @PathVariable long tripId, String currentEmail) {
			//model.addAttribute("trip", tripRepository.findById(tripId).orElse(null));
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				tripRepository.findById(tripId).orElse(null).getUsername())) {
			return "/error";
		}
			model.addAttribute("tripId", tripId);
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
			//model.addAttribute("flights", flightTemplateService.getAllAvailableFlights(tripId, currentEmail));
			//model.addAttribute("numberOfPossibilities", flightTemplateService.getAllAvailableFlights(tripId, currentEmail).size());
			logger.info("Running getAvailableFlights() method to attach flights to the view");
			return "/home/attachFlight";
	}
	
	
	@RequestMapping(value = "/bookFlight", 
			method = RequestMethod.GET) 
	public ModelAndView bookFlight(@RequestParam(value = "flightTemplateId", required = true)
	long flightTemplateId, 
			ModelMap model, 
			String currentUser,
			@RequestParam(value = "tripId", required = true) long tripId) {
		logger.info("Booking the selected flight via bookFlight() method");
		return flightService.bookFlight(flightTemplateId, model, userService.getCurrentUserName(), tripId);
	}
	
	@GetMapping("/viewTripFlights/{tripId}")
	public ModelAndView viewFlights(
			@PathVariable long tripId,
			Model model) {
		logger.info("Attempting to get a list of flights beloning to the selected trip");
		return flightService.getAllFlightsOfTrip(tripId, model);
	}
	
	@ModelAttribute("reasons")
	public List<String> initializeReasons() {
		return userService.reasons();
	}
	
	/*@ModelAttribute("destinations")
	public List<String> initializeDestinations() {
		return destinationService.getAllDestinations();
	}*/
	
	/*@RequestMapping(value = "/saveFlight/{tripId}", 
			method = RequestMethod.POST)
	public ModelAndView saveFlightRegistration(@Valid Flight flight, 
			BindingResult result, 
			ModelMap model,
			String currentUser,
			@PathVariable long tripId) {
		return flightService.saveFlightRegistration(flight, result, model, 
				userService.getCurrentUserName(), tripId);
	}*/
	
	/*@RequestMapping(value = "/editFlight/{flightId}")
	public String editFlight(@PathVariable long flightId, ModelMap model, @ModelAttribute("currentEmail")
	String currentEmail) {
	}*/
	
	/*@RequestMapping(value = "/editAndSaveFlight", 
			method = RequestMethod.POST)
	public ModelAndView editAndSaveFlight(@Valid @ModelAttribute("flight") Flight flight, 
			BindingResult result, ModelMap model) {
		return flightService.editAndSaveFlight(flight, result, model);
	}*/
	
	@RequestMapping(value = "/deleteFlight/{flightId}", 
			method = RequestMethod.GET) 
		public ModelAndView deleteFlight(@PathVariable long flightId) {
		logger.info("Attempting to delete the selected flight");
			return flightService.deleteFlight(flightId);
	}
	
	@RequestMapping(value = "/viewFlightDetails/{flightId}", 
			method = RequestMethod.GET)
	public ModelAndView viewFlightDetails(@PathVariable long flightId) {
		logger.info("Attempting to view details of selected flight via viewFlightDetails() method");
		return flightService.viewFlightDetails(flightId);
	}
}
