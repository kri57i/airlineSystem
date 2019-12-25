package com.kristi.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class AccountSecuritySpringApplication {
	public static void main(String[] args) {
		//running the application
		SpringApplication.run(AccountSecuritySpringApplication.class, args);
	}
}
