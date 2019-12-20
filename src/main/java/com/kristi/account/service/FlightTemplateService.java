package com.kristi.account.service;

import java.time.LocalDate;
import java.time.ZoneId;
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
import com.kristi.account.validators.AccessValidators;
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
	
	@Autowired
	private UserService userService;
	
	public FlightTemplate save(FlightTemplate flightTemplate) {
		return flightTemplateRepository.save(flightTemplate);
	}
	
	public ModelAndView saveFlightTemplateRegistration(
			@Valid FlightTemplate flightTemplate,
			BindingResult result,
			ModelMap model
			) {
		if(result.hasErrors()) {
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		
		if(!dateValidator.validDepartureDate(flightTemplate.getDepartingDate())) {
			result.rejectValue("departingDate", null, "Departure date must not be before"
					+ " today's date ");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		if(dateValidator.matchingDates(flightTemplate.getDepartingDate(), 
				flightTemplate.getArrivingDate())) {
			result.rejectValue("arrivingDate", null, "Dates must not match");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		
		if(!dateValidator.validArrivalDate(flightTemplate.getArrivingDate())) {
			result.rejectValue("arrivingDate", null, "Arrival date must not be before"
					+ " today's date ");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		
		if(locationValidator.matchingLocations(flightTemplate.getFirstLocation(), flightTemplate.getLastLocation())) {
			result.rejectValue("firstLocation", null, "Locations must not match");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		
		if(dateValidator.inverseValues(flightTemplate.getDepartingDate(), flightTemplate.getArrivingDate())) {
			result.rejectValue("departingDate", null, "Arrival date must be after departure date");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		if(capacityValidator.invalidCapacityInput(flightTemplate.getMaximumCapacity())) {
			result.rejectValue("maximumCapacity", null, "Number of passengers can't be equal/less than 0 or more than 32");
			model.addAttribute("destinations", destinationService.getAllDestinations());
			return new ModelAndView("/admin/createFlightTemplate");
		}
		//LocalDate currentStartingDate = flightTemplate.getDepartingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		//LocalDate currentEndingDate = flightTemplate.getDepartingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.save(flightTemplate);
		model.addAttribute("flightTemplate", flightTemplate);
		return new ModelAndView("/admin/flightTemplateSuccess");
	}
	
	public ModelAndView getAllFlights() {
		List<FlightTemplate> flights = flightTemplateRepository.getAllFlights();
		return new ModelAndView("/admin/viewFlights", "flights", flights);
	}
	
	/*public List<FlightTemplate> getAvailableFlights(String departure_location ,
			String arrival_location) {
		return flightTemplateRepository.getAvaliableFlights(departure_location, arrival_location);
	}*/
	
	
	
	
	
	
	public List<FlightTemplate> getAvailableFlights(long tripId, String departure_location,
			String arrival_location, Date trip_departure_date, String username) {
		Trip trip = tripRepository.findById(tripId).orElse(null);
		List<FlightTemplate> available = flightTemplateRepository
				.getAvaliableFlights(departure_location, arrival_location, trip_departure_date).stream()
				.filter(flight -> dateValidator.validDates(flight, trip)).collect(Collectors.toList());
		List<Flight> flights = flightRepository.getAllSpecifiedFlights(username, departure_location, 
				arrival_location);
		List<FlightTemplate> finalFlights = new ArrayList<>();
		boolean booked = false;
		for(FlightTemplate flight : available) {
			booked = false;
			for(Flight userFlight : flights) {
				if(flight.getDepartingDate().equals(userFlight.getStartingDate()) /*
						|| flight.getDepartingDate().before(tripRepository.findById(tripId).orElse(null).getDepartureDate())
						|| flight.getArrivingDate().after(tripRepository.findById(tripId).orElse(null).getArrivalDate())*/) {
					booked = true;
				}
			}
			if(!booked) {
				finalFlights.add(flight);
			}
		}
		for(FlightTemplate flight : finalFlights) {
			System.out.println(flight.getDepartingDate() + " " + flight.getArrivingDate());
		}
		return finalFlights;
	} 
	
	/*public List<FlightTemplate> getAllAvailableFlights(long tripId, String username) {
		Trip trip = tripRepository.findById(tripId).orElse(null);
		List<FlightTemplate> flights = flightTemplateRepository.getAllAvailableFlights(trip.getFromLocation(), 
				trip.getToLocation(), trip.getDepartureDate(), trip.getArrivalDate());
		/*List<FlightTemplate> flights = flightTemplateRepository.getAllAvailableFlights(trip.getFromLocation(), 
				trip.getToLocation()).stream()
				.filter(flight -> dateValidator.validDates(flight, trip)).collect(Collectors.toList());
		/*for(FlightTemplate flight : flights) {
			System.out.println(flight.getFirstLocation() + flight.getLastLocation() + flight.getDepartingDate());
		return flights;
		}
		/*System.out.println("Finished\n\n\n");
		List<Flight> userFlights = flightRepository.getAllFlightsOfUser(username);
		return userFlights;*/
	

	public ModelAndView deleteFlightTemplate(@PathVariable long flightTemplateId) {
		FlightTemplate currentFlightTemplate = flightTemplateRepository.findById(flightTemplateId).orElse(null);
		currentFlightTemplate.setToDate(new Date());
		this.save(currentFlightTemplate);
		flightService.deleteAllFlightsOfTemplate(new Date(), 
				currentFlightTemplate.getFirstLocation(), 
				currentFlightTemplate.getLastLocation(), 
				currentFlightTemplate.getDepartingDate(), 
				currentFlightTemplate.getArrivingDate());
		return new ModelAndView("redirect:/viewFlights");
	}
	
	public void updateCapacity(int new_capacity, long flight_template_id) {
		flightTemplateRepository.updateCapacity(new_capacity, flight_template_id);
	}
	
	public void removePassenger(int new_capacity, long flight_template_id) {
		flightTemplateRepository.removePassenger(new_capacity, flight_template_id);
	}
}
