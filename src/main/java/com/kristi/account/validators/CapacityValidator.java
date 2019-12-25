package com.kristi.account.validators;

import org.springframework.stereotype.Service;

@Service
public class CapacityValidator {

	public CapacityValidator() {}
	
	//This method will be used to make sure that a valid flight capacity has been set
	public boolean invalidCapacityInput(int capacity) {
		if(capacity <= 0 || capacity > 32) {
			return true;
		}
		return false;
	}
}
