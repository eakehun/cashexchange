package com.hourfun.cashexchange.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{

	@Value("${session.cookie.key}")
	private String cookieKey;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		Cookie cookie = new Cookie(cookieKey, null);
		cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        Cookie rememberMeUsercookie = new Cookie("RMB_RU", null);
        rememberMeUsercookie.setMaxAge(0);
        response.addCookie(rememberMeUsercookie);
        
        Cookie rememberMeAdmincookie = new Cookie("ROLE_ADMIN", null);
        rememberMeAdmincookie.setMaxAge(0);
        response.addCookie(rememberMeAdmincookie);
        
        Cookie rememberMeManagercookie = new Cookie("RMB_RM", null);
        rememberMeManagercookie.setMaxAge(0);
        response.addCookie(rememberMeManagercookie);
        
//        if(currentAuthString.equals("ROLE_ADMIN")) {
//			cookieName = "RMB_RA";
//		}else if(currentAuthString.equals("ROLE_MANAGER")) {
//			cookieName = "RMB_RM";
//		}else {
//			cookieName = "RMB_RU";
//		}
        
		
	}
}
