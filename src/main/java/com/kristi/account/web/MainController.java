package com.kristi.account.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.service.DestinationService;
import com.kristi.account.service.UserLoginsService;
import com.kristi.account.service.UserService;
import com.kristi.account.validators.AccessValidators;

@Controller
public class MainController {

	@Autowired
	private UserService userService;
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private DestinationService destinationService;
	
	@Autowired
	private AccessValidators accessValidator;
	
	@Autowired
	private UserLoginsService userLoginsService;
	
	@GetMapping("/")
	public String root(Model model) {
		logger.info("Loading the main view");
		model.addAttribute("currentUser", userService.findByEmail(
				userService.getCurrentUserName()));
		return "home/index";
	}
	
	//mapping to error template
	@GetMapping("/error") 
		public String error() {
			return "/error";
	}
	
	@GetMapping("/viewActivity")
	public ModelAndView returnActivity() {
		return new ModelAndView("/admin/viewActivity", "logins", 
				userLoginsService.getAllLogins());
	}
	
	@ModelAttribute("currentEmail")
	public String getCurrentUsername() {
		return userService.getCurrentUserName();
	}
	
	
	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("users", userService.getAll());
		logger.info("Loading the administrator view");
		return "/admin/admin";
	}
	
	//loading the login view
	@GetMapping("/login")
	public String login(Model model) {
		logger.info("Loading the login view");
		return "/login";
	}
	
	@GetMapping("/userRegistrationSuccess")
	public String userSuccess(Model model) {
		logger.info("Success registering user");
		return "/admin/userRegistrationSuccess";
	}
	
	//initializing trip reasons
	@ModelAttribute("reasons")
	public List<String> initializeReasons() {
		return userService.reasons();
	}
	
	//initializing the destinations
	@ModelAttribute("destinations")
	public List<String> initializeDestinations() {
		return destinationService.getAllDestinations();
	}
	
	
	//delete account from administrator dashboard
	@RequestMapping(value = "/delete/{email}", 
			method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String email) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				"admin@gmail.com")) {
			return new ModelAndView("/error");
		}
		logger.info("Attempting to delete the selected user account from administrator site");
		return userService.deleteUser(email);
	}
	
	//delete account from user view
	@RequestMapping(value = "/deleteMyAccount/{email}", 
			method = RequestMethod.GET)
	public ModelAndView deleteMyAccount(@PathVariable String email) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				email)) {
			return new ModelAndView("/error");
		}
		logger.info("Attempting to delete the selected user account from user site");
		return userService.deleteMyAccount(email);
	}
	
	//reactivate account
	@RequestMapping(value = "/activateUser/{email}", 
			method = RequestMethod.GET)
	public ModelAndView activateAccount(@PathVariable String email) {
		if(!accessValidator.matchingUsers(userService.getCurrentUserName(), 
				"admin@gmail.com")) {
			return new ModelAndView("/error");
		}
		return userService.activateAccount(email);
	}
	
}
