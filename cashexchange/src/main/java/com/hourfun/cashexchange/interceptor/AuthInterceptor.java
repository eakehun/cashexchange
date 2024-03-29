package com.hourfun.cashexchange.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hourfun.cashexchange.common.AccountStatusEnum;
import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.model.TradingMenu;
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
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

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
			History history, String type) throws JsonMappingException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();

		if (history == null) {
			return;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return;
		}
		StringBuilder sbuf = new StringBuilder();
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		history.setIp(ip);
		Users users = usersService.findByUserId(authentication.getName());
		history.setUser(users.getUserId());

		history.setType(type);

		String userAgent = request.getHeader("User-Agent").toUpperCase();

		if (userAgent.indexOf("MOBILE") > -1) {
			if (userAgent.indexOf("PHONE") == -1) {
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
			
			TradingMenu tradingMenu = mapper.readValue(body.toString(), TradingMenu.class);

			history.setContents(tradingMenu.getContent());
			if(tradingMenu.isUsed()) {
				sbuf.append(tradingMenu.getMenuName() + " on");
			}else {
				sbuf.append(tradingMenu.getMenuName() + " off");
			}
		} else if (url.contains("/admin/users/")) {
			history.setService("users");
			if (type.equals("update")) {
				sbuf.append("update user status");
			}
			history.setContents(body.toString());
		} else {
			sbuf.append(url + " " + type);
			history.setContents(body.toString());
		}

		if (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value()) {
			sbuf.append(" success");
		} else {
			sbuf.append(" fail");
		}

		history.setKeyword(sbuf.toString());

	}

}
