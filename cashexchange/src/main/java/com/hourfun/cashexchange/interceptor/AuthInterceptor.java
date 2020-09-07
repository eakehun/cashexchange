package com.hourfun.cashexchange.interceptor;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hourfun.cashexchange.common.AuthEnum;


public class AuthInterceptor extends HandlerInterceptorAdapter{
	
	@Value("${session,cookie.key}")
	private String cookieKey;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			List<GrantedAuthority> currentAuth = (List<GrantedAuthority>) authentication.getAuthorities();
			
			String currentAuthString = currentAuth.get(0).getAuthority();
			
			if(currentAuthString.equals(AuthEnum.ROLE_USER.name()) ||
					currentAuthString.equals(AuthEnum.ROLE_MANAGER.name()) ||
					currentAuthString.equals(AuthEnum.ROLE_ADMIN.name())) {
				HttpSession session = request.getSession();
				if(session != null) {
					Cookie cookie = new Cookie(cookieKey, session.getId());
					cookie.setPath("/");
					cookie.setMaxAge(60 * 60 * 24 * 7);
					response.addCookie(cookie);
				}
			}
		}
		
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}
}
