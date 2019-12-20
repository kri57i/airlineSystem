package com.kristi.account.web;



import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kristi.account.model.User;
import com.kristi.account.service.UserService;

@Controller

public class UserRegistrationController {

	@Autowired
	private UserService userService;
	
	Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);
	
	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String showRegistrationForm(Model model) {
		logger.info("Accessing the user registration view");
		return "/admin/registration";
	}
	
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, 
			BindingResult result, 
			Model model) {
		User existingUser = userService.findByEmail(userDto.getEmail());
		if(existingUser != null) {
			result.rejectValue("email", null, "There is an existing account with that email!");
		}
		if(result.hasErrors()) {
			return new ModelAndView("/admin/registration");
		}
		User currentUser = userService.save(userDto);
		logger.info("Saving the created user");
		return new ModelAndView("/admin/userRegistrationSuccess", 
				"currentUser", currentUser);
	}
}
