package com.kristi.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristi.account.repository.DestinationRepository;
import java.util.List;

@Service
public class DestinationService {

	@Autowired
	private DestinationRepository destinationRepository;
	
	/*
	 * The following function calls the getAllDestinations() method from the destination repository
	 * and returns a list of strings of destinations
	 */
	public List<String> getAllDestinations() {
		return destinationRepository.getAllDestinations();
	}
	
	/*
	 * The following function returns a list of strings of destinations that match the given 
	 * locations
	 */
	public List<String> getAllAvailableDestinations(String from_location, String to_location) {
		return destinationRepository.getAllAvailableDestinations(from_location, to_location);
	}
}
