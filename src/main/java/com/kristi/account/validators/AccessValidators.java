package com.kristi.account.validators;

import org.springframework.stereotype.Service;

@Service
public class AccessValidators {

	
	public AccessValidators() {}
	
	//this method will be used to check if the requested user is the same as the logged in user
	public boolean matchingUsers(String firstUser, String secondUser) {
		if(firstUser.equals(secondUser)) {
			return true;
		}
		return false;
	}
}
