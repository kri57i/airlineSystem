package com.kristi.account.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


import com.kristi.account.model.User;
import com.kristi.account.web.UserRegistrationDto;



public interface UserService extends UserDetailsService{

	User findByEmail(String email);
	
	User save(UserRegistrationDto registration);
	
	List<String> reasons();
	
	ModelAndView activateAccount(@PathVariable String email);
	
	String getCurrentUserName();
	
    ModelAndView deleteUser(@PathVariable String email);
    ModelAndView deleteMyAccount(@PathVariable String email);
	
	List<User> getAll();

}
