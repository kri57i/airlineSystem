package com.kristi.account.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.FlightTemplate;
import com.kristi.account.service.DestinationService;
import com.kristi.account.service.FlightService;
import com.kristi.account.service.FlightTemplateService;

@Controller
public class FlightTemplateController {

	@Autowired
	private FlightTemplateService flightTemplateService;
	
	@Autowired
	private DestinationService destinationService;
	
	@Autowired
	private FlightService flightService;
	
	
	Logger logger = LoggerFactory.getLogger(FlightTemplateController.class);
	
	/*
	 * When mapping to this url, the getAllFlights method will be invoked
	 */
	@GetMapping("/viewFlights")
	public ModelAndView viewFlights(ModelMap model) {
		logger.info("Attempting to get a list of all the flight templates the administrator has created");
		return flightTemplateService.getAllFlights();
	}
	
	/*
	 * When mapping to the following url, the view including the flight template registration
	 * form will be shown
	 */
	@GetMapping("/createFlightTemplate")
	public String createFlightTemplate(Model model) {
		model.addAttribute("flightTemplate", new FlightTemplate());
		model.addAttribute("destinations", destinationService.getAllDestinations());
		logger.info("Attempting to create the current flight template");
		return "/admin/createFlightTemplate";
	}
	
	//the following url mapping will invoke the saveFlightTemplateRegistration method
	@RequestMapping(value = "/saveFlightTemplate", 
			method = RequestMethod.POST)
	public ModelAndView saveFlightTemplateRegistration(@Valid FlightTemplate flightTemplate, 
			BindingResult result, 
			ModelMap model) {
		logger.info("Saving the flight template");
		return flightTemplateService.saveFlightTemplateRegistration(flightTemplate, 
				result, 
				model);
	}
	
	
	/*
	 * The following url mapping will invoke the method that deletes a flight template
	 */
	@RequestMapping(value = "/deleteFlightTemplate/{flightTemplateId}", 
			method = RequestMethod.GET)
	public ModelAndView deleteFlightTemplate(@PathVariable long flightTemplateId) {
		logger.info("Attempting to delete the selected flight template");
		return flightTemplateService.deleteFlightTemplate(flightTemplateId);
	}
	
	/*
	 * The following url mapping will invoke the method that returns a view of all the flights 
	 * that belong to the given flight template
	 */
	@RequestMapping(value = "/viewUsersOfFlight/{flightTemplateId}", 
			method = RequestMethod.GET)
	public ModelAndView getAllFlightsOfTemplate(@PathVariable long flightTemplateId) {
		logger.info("Displaying the list of users for the selected flight template");
		return new ModelAndView("/admin/viewUsersOfFlight", "flightsOfTemplate", 
				flightService.getAllFlightsOfTemplate(flightTemplateId));
	}
}
