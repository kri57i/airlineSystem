package com.kristi.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.kristi.account.repository.DestinationRepository;
import com.kristi.account.model.Destination;
import java.util.List;

@Service
public class DestinationService {

	@Autowired
	private DestinationRepository destinationRepository;
	
	public List<String> getAllDestinations() {
		return destinationRepository.getAllDestinations();
	}
	
	public List<String> getAllAvailableDestinations(String from_location, String to_location) {
		return destinationRepository.getAllAvailableDestinations(from_location, to_location);
	}
}
