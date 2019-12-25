package com.kristi.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristi.account.model.UserLogins;
import com.kristi.account.repository.UserLoginsRepository;

@Service
public class UserLoginsService {

	@Autowired
	private UserLoginsRepository userLoginsRepository;
	
	public UserLogins save(UserLogins userLogins) {
		return userLoginsRepository.save(userLogins);
	}
	
	//the following method increments the login times number of the given user
	public void incrementUserLogin(int new_login, String username) {
		userLoginsRepository.incrementUserLogin(new_login, username);
	}
	
	//returning the user logins record for the given user
	public UserLogins getRecordFromUser(String username) {
		return userLoginsRepository.getRecordFromUser(username);
	}
	
	//getting a list of all users and their respective login times
	public List<UserLogins> getAllLogins() {
		return userLoginsRepository.getAllLogins();
	}
	
}
