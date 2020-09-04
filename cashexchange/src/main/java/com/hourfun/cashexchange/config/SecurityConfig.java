package com.hourfun.cashexchange.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.hourfun.cashexchange.handler.CustomLogoutSuccessHandler;
import com.hourfun.cashexchange.service.CustomSecurityRememberMeService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService customSecurityService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.cors()
			.and()
			.csrf()
				.disable() // csrf 값 안 쓰게 테스트용
			.httpBasic()
			.and()
			.rememberMe()				
				.alwaysRemember(true)
				.key("qwer")
				.rememberMeCookieName("remember-me")
			.and()			
            .logout()
            	.logoutUrl("/logout")
            	.logoutSuccessHandler(logoutSuccessHandler())
            .and()
			.authorizeRequests()
				.antMatchers("/board/**").hasAnyRole("USER", "ADMIN", "MANAGER")
	        	.antMatchers("/user/**").hasAnyRole("USER")
		        .antMatchers("/admin/**").hasAnyRole("ADMIN")
		        .antMatchers("/manager/**").hasAnyRole("MANAGER")
		        .antMatchers("/**").permitAll();
		    
	}
	
	@Bean
	public PasswordEncoder customPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler(); 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("qwer").password("{noop}qwer").roles("USER");
		
		auth
			.userDetailsService(customSecurityService)			
			.passwordEncoder(customPasswordEncoder());
	}
	

	@Bean
	public TokenBasedRememberMeServices customSecurityRememberMeService() {
		
		
		return new CustomSecurityRememberMeService("qwer", customSecurityService);
	}
	
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
}
