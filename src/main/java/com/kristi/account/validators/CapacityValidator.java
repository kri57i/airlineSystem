package com.kristi.account.validators;

import org.springframework.stereotype.Service;

@Service
public class CapacityValidator {

	public CapacityValidator() {}
	
	public boolean invalidCapacityInput(int capacity) {
		if(capacity <= 0 || capacity > 32) {
			return true;
		}
		return false;
	}
}
