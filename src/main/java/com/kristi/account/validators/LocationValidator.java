package com.kristi.account.validators;

import org.springframework.stereotype.Service;

@Service
public class LocationValidator {

	
	public LocationValidator() {}
	
	public boolean matchingLocations(String startingLocation, String finalLocation) {
		if(startingLocation.equals(finalLocation)) {
			return true;
		}
		return false;
	}
}
