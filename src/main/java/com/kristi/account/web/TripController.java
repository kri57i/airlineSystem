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
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Trip;
import com.kristi.account.service.DestinationService;
import com.kristi.account.service.TripService;
import com.kristi.account.service.UserService;

@Controller
public class TripController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private DestinationService destinationService;
	
	Logger logger = LoggerFactory.getLogger(TripController.class);
	
	//loading the trip creating view 
	@GetMapping("/createTrip")
	public String createTrip(Model model) {
		model.addAttribute("trip", new Trip());
		logger.info("Loading the trip creation view via the createTrip() method");
		return "/home/createTrip";
	}
	
	//adding the current user as an attribute
	@ModelAttribute("currentEmail")
	public String getCurrentUsername() {
		return userService.getCurrentUserName();
	}
	
	//loading the trip editing view
	@GetMapping("/editTrip")
	public String editTrip(Model model) {
		logger.info("Loading the trip editing view via the editTrip() method");
		return "/home/editTrip";
	}
	
	//loading the view that shows all the trips of the current user
	@GetMapping("/viewTrips")
	public ModelAndView viewTrips(ModelMap model) {
		logger.info("Attempting to view all the trips of the current user");
		return tripService.getAllTripsOfUser(userService.getCurrentUserName(), model);
	}
	
	//loading the view that shows all the trips with the "WAITING FOR APPROVAL" status
	@GetMapping("/tripRequests")
	public ModelAndView tripRequests(Model model) {
		logger.info("Fetching all the trip requests from all the users");
		return tripService.getAllTrips();
	}
	
	//loading the view alongside with the trip details of the given trip
	@GetMapping("/tripDetails/{tripId}") 
	public ModelAndView tripDetails(@PathVariable long tripId) {
		logger.info("Redirecting to trip details of the selected trip request");
		return tripService.tripDetails(tripId);
	}
	
	//loading the trip creating success view 
	@GetMapping("/tripSuccess")
	public String tripSuccess(Model model) {
		logger.info("Success creating trip");
		return "/home/tripSuccess";
	}
	
	@ModelAttribute("reasons")
	public List<String> initializeReasons() {
		return userService.reasons();
	}
	
	@ModelAttribute("destinations")
	public List<String> initializeDestinations() {
		return destinationService.getAllDestinations();
	}
	
	//the following url is the action url of the trip creating form
	@RequestMapping(value = "/saveTrip", 
			method = RequestMethod.POST)
	public String saveTripRegistration(@Valid Trip trip, 
			BindingResult result,
			ModelMap model,
			String currentUser) {
		logger.info("Success registering trip");
		return tripService.saveTripRegistration(trip, 
				result, 
				model,  
				userService.getCurrentUserName());
	}
	
	
	@RequestMapping(value = "/editTrip/{tripId}")
	public String editTrip(@PathVariable long tripId, ModelMap model) {
		logger.info("Loading the trip editing view");
		return tripService.editTrip(tripId, model);
	}
	
	//the following url is the form action of the edit trip form
	@RequestMapping(value = "/editAndSaveTrip", 
			method = RequestMethod.POST)
	public ModelAndView editAndSave(@Valid @ModelAttribute("trip") Trip trip, 
			BindingResult result) {
		logger.info("Success updating the edited trip");
		return tripService.editAndSaveTrip(trip, result);
	}
	
	//the following url mapping invokes the delete trip method of the trip service 
	@RequestMapping(value = "/deleteTrip/{tripId}", 
			method = RequestMethod.GET)
	public ModelAndView deleteTrip(@PathVariable long tripId) {
		logger.info("Attempting to delete trip");
		return tripService.deleteTrip(tripId);
	}
	
	//loading the details view for the given trip
	@RequestMapping(value = "/viewDetails/{tripId}", 
			method = RequestMethod.GET)
	public ModelAndView viewDetails(@PathVariable long tripId) {
		logger.info("Loading the view details template of the selected trip");
		return tripService.viewDetails(tripId);
	}
	
	//the following url mapping sends an approval for the given trip
	@RequestMapping(value = "/sendApproval/{tripId}", 
			method = RequestMethod.GET)
	public ModelAndView sendApproval(@PathVariable long tripId) {
		logger.info("Attempting to send approval for the created trip");
		return tripService.sendApproval(tripId);
	}
	
	//the following url approves the given trip(only from administrator side)
	@RequestMapping(value = "/approveTrip/{tripId}", 
			method = RequestMethod.GET)
	public ModelAndView approveTrip(@PathVariable long tripId) {
		logger.info("Approving the current trip");
		return tripService.approveTrip(tripId);
	}
	
	//the following url rejects the given trip(only from administrator side)
	@RequestMapping(value = "/rejectTrip/{tripId}", 
			method = RequestMethod.GET)
	public ModelAndView rejectTrip(@PathVariable long tripId) {
		logger.info("Rejecting the current trip");
		return tripService.rejectTrip(tripId);
	}
	
}
