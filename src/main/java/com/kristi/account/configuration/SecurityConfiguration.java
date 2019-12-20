package com.kristi.account.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.kristi.account.service.UserService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
         .authorizeRequests()
         .antMatchers("/admin/**").hasRole("ADMIN")
         .antMatchers("/registration").hasRole("ADMIN")
         .antMatchers("/userRegistrationSuccess/**").hasRole("ADMIN")
         .antMatchers("/createFlightTemplate/**").hasRole("ADMIN")
         .antMatchers("/flightTemplateSuccess/**").hasRole("ADMIN")
         .antMatchers("/tripRequests/**").hasRole("ADMIN")
         .antMatchers("/tripDetails/**").hasRole("ADMIN")
         .antMatchers("/tripRequests/**").hasRole("ADMIN")
         .antMatchers("/viewFlights/**").hasRole("ADMIN")
         .antMatchers("/viewUsersOfFlight/**").hasRole("ADMIN")
         .antMatchers("/viewActivity").hasRole("ADMIN")
         .antMatchers("/approvalSent/**").hasRole("USER")
         .antMatchers("/attachFlight/**").hasRole("USER")
         .antMatchers("/flightBookingSuccess/**").hasRole("USER")
         .antMatchers("/viewDetails/**").hasRole("USER")
         .antMatchers("/viewFlightDetails/**").hasRole("USER")
         .antMatchers("/viewTripFlights/**").hasRole("USER")
         .antMatchers("/createTrip/**").hasRole("USER")
         .antMatchers("/tripSuccess/**").hasRole("USER")
         .antMatchers("/editTrip/**").hasRole("USER")
         .antMatchers("/viewTrips/**").hasRole("USER")
         .antMatchers("/createFlight/**").hasRole("USER")
         .antMatchers("/activateUser/**").hasRole("ADMIN")
             .antMatchers(
                     "/js/**",
                     "/css/**",
                     "/login**",
                     "/img/**",
                     "/webjars/**").permitAll()
             .anyRequest().authenticated()
         .and()
             .formLogin()
                 .loginPage("/login")
                     .permitAll()
         .and()
             .logout()
                 .invalidateHttpSession(true)
                 .clearAuthentication(true)
                 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                 .logoutSuccessUrl("/login?logout")
         .permitAll();
		}
	

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}