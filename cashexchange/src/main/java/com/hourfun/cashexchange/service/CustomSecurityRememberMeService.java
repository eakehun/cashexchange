package com.hourfun.cashexchange.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import com.hourfun.cashexchange.common.AuthEnum;

public class CustomSecurityRememberMeService extends TokenBasedRememberMeServices {
	
	@Value("${session.cookie.domain}")
	private String cookieDomain;

	public CustomSecurityRememberMeService(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
		setAlwaysRemember(true);
//		setCookieName("qwer");
		setTokenValiditySeconds(60 * 60 * 24 * 7);
//		setTokenValiditySeconds(60);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		String username = retrieveUserName(successfulAuthentication);
		String password = retrievePassword(successfulAuthentication);

		if (!StringUtils.hasLength(username)) {
			logger.debug("Unable to retrieve username");
			return;
		}

		if (!StringUtils.hasLength(password)) {
			UserDetails user = getUserDetailsService().loadUserByUsername(username);
			password = user.getPassword();

			if (!StringUtils.hasLength(password)) {
				logger.debug("Unable to obtain password for user: " + username);
				return;
			}
		}
		
		int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
		long expiryTime = System.currentTimeMillis();
		// SEC-949
		expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

		String signatureValue = makeTokenSignature(expiryTime, username, password);

		List<GrantedAuthority> currentAuth = (List<GrantedAuthority>) successfulAuthentication.getAuthorities();

		String currentAuthString = currentAuth.get(0).getAuthority();

		String cookieName = "";

		if (currentAuthString.equals(AuthEnum.ROLE_ADMIN.name())) {
			cookieName = "RMB_RA";
		} else if (currentAuthString.equals(AuthEnum.ROLE_MANAGER.name())) {
			cookieName = "RMB_RM";
		} else {
			cookieName = "RMB_RU";
		}

		setCookieName(cookieName);

		setCookie(new String[] { username, Long.toString(expiryTime), signatureValue }, tokenLifetime, request,
				response);

		if (logger.isDebugEnabled()) {
			logger.debug(
					"Added remember-me cookie for user '" + username + "', expiry: '" + new Date(expiryTime) + "'");
		}
	}

	@Override
	protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		String cookieName = getCookieName();
//		String cookieDomain = request.getServerName();

		String cookieValue = encodeCookie(tokens);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath(getCookiePath(request));
		if (cookieDomain != null) {
			cookie.setDomain(cookieDomain);
		}
		if (maxAge < 1) {
			cookie.setVersion(1);
		}

		cookie.setSecure(false);

		cookie.setHttpOnly(true);

		response.addCookie(cookie);
	}

	private String getCookiePath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return contextPath.length() > 0 ? contextPath : "/";
	}

}
