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
         .antMatchers("/admin/**", 
        		 "/registration",
        		 "/userRegistrationSuccess/**",
        		 "/createFlightTemplate/**",
        		 "/flightTemplateSuccess/**",
        		 "/tripRequests/**",
        		 "/tripDetails/**",
        		 "/tripRequests/**",
        		 "/viewFlights/**",
        		 "/viewUsersOfFlight/**",
        		 "/viewActivity",
        		 "/activateUser/**"
        		 ).hasRole("ADMIN")
         .antMatchers("/approvalSent/**", 
        		 "/attachFlight/**", 
        		 "/flightBookingSuccess/**",
        		 "/viewDetails/**",
        		 "/viewFlightDetails/**",
        		 "/viewTripFlights/**",
        		 "/createTrip/**",
        		 "/tripSuccess/**",
        		 "/editTrip/**",
        		 "/viewTrips/**"
        		 ).hasRole("USER")
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
	
	/*
	 * Password encrypting class
	 */
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