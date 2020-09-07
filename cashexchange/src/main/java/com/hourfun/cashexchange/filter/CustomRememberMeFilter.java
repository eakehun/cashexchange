package com.hourfun.cashexchange.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

public class CustomRememberMeFilter extends RememberMeAuthenticationFilter{

	public CustomRememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
		super(authenticationManager, rememberMeServices);
		// TODO Auto-generated constructor stub
		
		
		
	}

}
