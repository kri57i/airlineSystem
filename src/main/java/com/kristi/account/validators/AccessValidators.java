package com.kristi.account.validators;

import org.springframework.stereotype.Service;

@Service
public class AccessValidators {

	
	public AccessValidators() {}
	
	public boolean matchingUsers(String firstUser, String secondUser) {
		if(firstUser.equals(secondUser)) {
			return true;
		}
		return false;
	}
}
