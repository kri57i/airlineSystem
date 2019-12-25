package com.kristi.account.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/*
 * User Logins entity will keep record of login times for 
 * each user
 */
@Entity
@Table(name = "user_logins")
@EntityListeners(AuditingEntityListener.class)
public class UserLogins {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//This field will be incremented by 1 each time user logins
	@Column(nullable = false, name = "login_times")
	private int loginTimes;
	
	@Column(nullable = false, name = "username")
	private String username;
	
	//Standard getters and setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public int getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}

}
