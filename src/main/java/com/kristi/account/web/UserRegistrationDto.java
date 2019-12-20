package com.kristi.account.web;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedDate;

import com.kristi.account.constraint.FieldMatch;

@FieldMatch.List({
	@FieldMatch(first = "password", second = "confirmPassowrd", message = "The password fields must match"),
	@FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})

public class UserRegistrationDto {

	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String confirmPassword;
	
	@Email
	@NotEmpty
	private String email;
	
	@Email
	@NotEmpty
	private String confirmEmail;
	
	 @Column(name = "created_at")
	 @Temporal(TemporalType.TIMESTAMP)
     @CreatedDate
	 private Date createdAt;
	
	@AssertTrue
	private Boolean terms;

	public String getFirstName() {
		return firstName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public Boolean getTerms() {
		return terms;
	}

	public void setTerms(Boolean terms) {
		this.terms = terms;
	}
}
