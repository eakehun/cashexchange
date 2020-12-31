package com.hourfun.cashexchange.interceptor;

import java.util.Formatter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hourfun.cashexchange.common.AccountStatusEnum;
import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.service.HistoryService;
import com.hourfun.cashexchange.service.UsersService;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Value("${session.cookie.key}")
	private String cookieKey;

	@Autowired
	private UsersService usersService;

	@Autowired
	private HistoryService historyService;

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			List<GrantedAuthority> currentAuth = (List<GrantedAuthority>) authentication.getAuthorities();

			String currentAuthString = currentAuth.get(0).getAuthority();

			if (currentAuthString.equals(AuthEnum.ROLE_USER.name())
					|| currentAuthString.equals(AuthEnum.ROLE_MANAGER.name())
					|| currentAuthString.equals(AuthEnum.ROLE_ADMIN.name())) {

				Users users = usersService.findByUserId(authentication.getName());

				String currentAccountStatus = users.getAccountStatus();

				if (currentAccountStatus != null) {
					if (currentAccountStatus.equals(AccountStatusEnum.SUSPENDED.getValue())) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "suspended user");
					} else if (currentAccountStatus.equals(AccountStatusEnum.WITHDRAW.getValue())) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "withdraw user");
					}
				}

				HttpSession session = request.getSession();
				if (session != null) {
					Cookie cookie = new Cookie(cookieKey, session.getId());
					cookie.setPath("/");
//					cookie.setMaxAge(60 * 60 * 24 * 7);
					cookie.setMaxAge(60 * 30);
					response.addCookie(cookie);
				}
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		super.postHandle(request, response, handler, modelAndView);

		String url = request.getRequestURL().toString();
		String body = null;
		Object requestBody = request.getAttribute("requestBody");
		if (requestBody != null) {
			body = requestBody.toString();
		}
		History history = new History();

		if (url.contains("/login/")) {
			crudHistory(request, response, url, body, history, "login");
		}

		switch (request.getMethod()) {
		case "POST":
			crudHistory(request, response, url, body, history, "insert");
			break;
		case "PUT":
			crudHistory(request, response, url, body, history, "update");
			break;
		case "DELETE":
			crudHistory(request, response, url, body, history, "delete");
			break;
		}
		if (history.getType() != null) {
			historyService.upsertHistory(history, history.getType());
		}
	}

	private void crudHistory(HttpServletRequest request, HttpServletResponse response, String url, String body,
			History history, String type) {
		if (history == null) {
			return;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return;
		}
		StringBuilder sbuf = new StringBuilder();
		Formatter fmt = new Formatter(sbuf);
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		history.setIp(ip);
		Users users = usersService.findByUserId(authentication.getName());
		history.setUser(users.getUserId());

		history.setType(type);
		
		String userAgent = request.getHeader("User-Agent").toUpperCase();
		
	    if(userAgent.indexOf("MOBILE") > -1) {
	        if(userAgent.indexOf("PHONE") == -1) {
	        	history.setDevice("PHONE");
	        } else {
	        	history.setDevice("TABLET");
	        }
	    } else {
	    	history.setDevice("PC");
	    }

		if (url.contains("/login/")) {
			history.setService("user");
			sbuf.append("login");
			history.setContents(users.getUserId() + " login");
		} else if (url.contains("/password/")) {
			history.setService("user");
			history.setKeyword("password");
			history.setContents(users.getUserId() + " password change");
		} else if (url.contains("/trading/")) {
			history.setService("trading");
			if (type.equals("insert")) {
				sbuf.append("insert trading");
			} else if (type.equals("update")) {
				sbuf.append("update trading");
			}
			
			history.setContents(body.toString());
		} else if (url.contains("/OneToOne/")) {
			history.setService("OneToOne");
			
			if (type.equals("insert")) {
				sbuf.append("insert OneToOne");
			} else if (type.equals("update")) {
				sbuf.append("update OneToOne");
			}
		} else if (url.contains("/tradingMenu/")) {
			history.setService("tradingMenu");
			
			if (type.equals("insert")) {
				sbuf.append("insert tradingMenu");
			} else if (type.equals("update")) {
				sbuf.append("update tradingMenu");
			}
			
			history.setContents(body.toString());
		}
		
		if (response.getStatus() == HttpStatus.OK.value()
				|| response.getStatus() == HttpStatus.CREATED.value()) {
			sbuf.append(" success");
		}else {
			sbuf.append(" fail");
		}
		
		history.setKeyword(sbuf.toString());

//		if (url.contains("/user/login/")) {
//			// 로그인 기록 저장
//
//		} else if (url.contains("/user_group/")) {
//			if (type.equals("insert")) {
//				if (response.getStatus() == HttpStatus.OK.value()
//						|| response.getStatus() == HttpStatus.CREATED.value()) {
//					fmt.format("<%s> " + type + " this user create data :<%s>.", users.getName(),
//							users.getCreateDate());
//				} else {
//					fmt.format("<%s> failed to " + type + "  .", users.getName());
//				}
//				history.setType("INSERT USER");
//				history.setKeyword(sbuf.toString());
//			} else if (type.equals("delete")) {
//				int index = url.indexOf("user_group");
//				String temp = url.substring(index);
//				String[] strArr = temp.split("[/]");
//				if (strArr.length == 2) {
//					String groupId = strArr[1];
//					if (response.getStatus() == HttpStatus.OK.value()) {
//						fmt.format("<%s> " + type + " this group Id :<%s>.", users.getName(), groupId);
//					} else {
//						fmt.format("<%s> failed to " + type + " group Id :<%s>.", users.getName(), groupId);
//					}
//				}
//				history.setType("INSERT USER");
//				if (sbuf.toString().length() > 200) {
//					String tempData = sbuf.toString().substring(0, 200);
//					history.setKeyword(tempData);
//				} else {
//					history.setKeyword(sbuf.toString());
//				}
//			}
//		} else if (url.contains("/user")
//				&& (!url.contains("login") || !url.contains("check") || !url.contains("logout"))
//				&& !url.contains("role")) {
//			String userId;
//
//		}
	}

//	private void crudHistory(HttpServletRequest request, HttpServletResponse response, String url, String body,
//			History history, String type) {
//		
//		if (history == null) {
//			return;
//		}
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication == null) {
//			return;
//		}
//		
//		String ip = request.getHeader("X-FORWARDED-FOR");
//		if (ip == null) {
//			ip = request.getRemoteAddr();
//		}
//		history.setIp(ip);
//		
//		Users users = usersService.findByUserId(authentication.getName());
//		history.setUser(users.getUserId());
//		
//		if (url.contains("/user/login/")) {
//			
//			history.setType("LOGIN");
//			history.setContents("");
//		} else if () {
//			
//		}
//
//	}

}
