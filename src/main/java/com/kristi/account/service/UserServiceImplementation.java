package com.kristi.account.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.Role;
import com.kristi.account.model.User;
import com.kristi.account.model.UserLogins;
import com.kristi.account.repository.UserRepository;
import com.kristi.account.web.UserRegistrationDto;

@Service
public class UserServiceImplementation implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private UserLoginsService userLoginsService;
	
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/*
	 * The following method saves a user with the properties that are passed from the 
	 * registration dto
	 */
	public User save(UserRegistrationDto registration) {
		User user = new User();
		//creating a record for the user login times statistics
		UserLogins currentUser = new UserLogins();
		currentUser.setUsername(registration.getEmail());
		userLoginsService.save(currentUser);
		user.setFirstName(registration.getFirstName());
		user.setLastName(registration.getLastName());
		user.setEmail(registration.getEmail());
		user.setCreatedAt(new Date());
		user.setPassword(passwordEncoder.encode(registration.getPassword()));
		//setting the user role
		user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		return userRepository.save(user);
	}
	
	//the following method deletes a user and also all his/her trips, flights
	public ModelAndView deleteUser(@PathVariable String email) {
		User user = this.findByEmail(email);
		user.setToDate(new Date());
		//user.setEmail(null);
		flightService.removeUserNumberFromTemplate(email);
		tripService.deleteTripsOfUser(email);
		flightService.deleteFlightsOfUser(email);
		userRepository.save(user);
		return new ModelAndView("redirect:/admin");
	}
	
	//the following method activates a deactivated user account 
	public ModelAndView activateAccount(@PathVariable String email) {
		User user = userRepository.findDeactivatedUser(email);
		user.setToDate(null);
		userRepository.save(user);
		return new ModelAndView("redirect:/admin");
	}
	
	public ModelAndView deleteMyAccount(@PathVariable String email) {
		User user = this.findByEmail(email);
		user.setToDate(new Date());
		//user.setEmail(null);
		flightService.removeUserNumberFromTemplate(email);
		tripService.deleteTripsOfUser(email);
		flightService.deleteFlightsOfUser(email);
		userRepository.save(user);
		return new ModelAndView("redirect:/login?logout");
	}	
	
	
	public List<String> reasons() {
		List<String> reasons = new ArrayList<String>();
		reasons.add("Meeting");
		reasons.add("Training");
		reasons.add("Project");
		reasons.add("Workshop");
		reasons.add("Event");
		reasons.add("Other");
		return reasons;
	}
	
	//overriding the loadUserByUsername method
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		//if the user is not found
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password!");
		}
		//incrementing login times for users
		userLoginsService.incrementUserLogin(userLoginsService.getRecordFromUser(email).getLoginTimes() + 1, email);
		return new org.springframework.security.core.userdetails.User(user.getEmail(), 
				user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	//returning the email of the currently logged in user
	public String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
	
	//getting a list of all users
	public List<User> getAll() {
		return userRepository.getAllUsers();
	}
	
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
}
