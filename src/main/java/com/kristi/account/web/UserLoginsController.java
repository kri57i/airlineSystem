package com.kristi.account.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kristi.account.repository.UserLoginsRepository;
import com.kristi.account.service.UserLoginsService;

@Controller
public class UserLoginsController {

	
	@Autowired
	private UserLoginsService userLoginsService;
	
	@Autowired
	private UserLoginsRepository userLoginsRepository;
}
