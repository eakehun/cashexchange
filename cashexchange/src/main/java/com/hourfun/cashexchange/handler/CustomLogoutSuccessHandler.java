package com.hourfun.cashexchange.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.hourfun.cashexchange.common.AuthEnum;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Value("${session.cookie.key}")
	private String cookieKey;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		Cookie cookie = new Cookie(cookieKey, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		if (authentication != null) {

			List<GrantedAuthority> currentAuth = (List<GrantedAuthority>) authentication.getAuthorities();

			String currentAuthString = currentAuth.get(0).getAuthority();

			String cookieName = "RMB_RU";

			if (currentAuthString.equals(AuthEnum.ROLE_ADMIN.name())) {
				cookieName = "RMB_RA";
			} else if (currentAuthString.equals(AuthEnum.ROLE_MANAGER.name())) {
				cookieName = "RMB_RM";
			}

			Cookie rememberMecookie = new Cookie(cookieName, null);
			rememberMecookie.setMaxAge(0);
			response.addCookie(rememberMecookie);
		} else {
			String cookieName = "RMB_RU";
			Cookie rememberMeUserCookie = new Cookie(cookieName, null);
			rememberMeUserCookie.setMaxAge(0);
			response.addCookie(rememberMeUserCookie);
			
			cookieName = "RMB_RA";
			Cookie rememberMeAdminCookie = new Cookie(cookieName, null);
			rememberMeAdminCookie.setMaxAge(0);
			response.addCookie(rememberMeAdminCookie);
			
			cookieName = "RMB_RM";
			Cookie rememberMeManagerCookie = new Cookie(cookieName, null);
			rememberMeManagerCookie.setMaxAge(0);
			response.addCookie(rememberMeManagerCookie);
		}

	}
}
