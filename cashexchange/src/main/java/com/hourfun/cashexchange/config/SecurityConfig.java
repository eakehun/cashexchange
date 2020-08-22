package com.hourfun.cashexchange.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService customSecurityService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.httpBasic()
			.and()
			.csrf()
				.disable() // csrf 값 안 쓰게 테스트용 
			.authorizeRequests()
	        	.antMatchers("/user/**").hasRole("USER")
		        .antMatchers("/admin/**").hasRole("ADMIN")
		        .antMatchers("/manager/**").hasRole("MANAGER")
		        .antMatchers("/**").permitAll();
		    
	}
	
	@Bean
	public PasswordEncoder customPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("qwer").password("{noop}qwer").roles("USER");
		
		auth
			.userDetailsService(customSecurityService);
//			.passwordEncoder(customPasswordEncoder());
	}

}
