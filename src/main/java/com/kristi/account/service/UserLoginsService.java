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
	
	public void incrementUserLogin(int new_login, String username) {
		userLoginsRepository.incrementUserLogin(new_login, username);
	}
	
	public UserLogins getRecordFromUser(String username) {
		return userLoginsRepository.getRecordFromUser(username);
	}
	
	public List<UserLogins> getAllLogins() {
		return userLoginsRepository.getAllLogins();
	}
	
}
