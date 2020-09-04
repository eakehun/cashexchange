package com.hourfun.cashexchange.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class CustomSecurityRememberMeService extends TokenBasedRememberMeServices {

	public CustomSecurityRememberMeService(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
	}
	
}
