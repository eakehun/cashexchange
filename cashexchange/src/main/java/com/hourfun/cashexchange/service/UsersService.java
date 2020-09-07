package com.hourfun.cashexchange.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder customPasswordEncoder;

	@Autowired
	private RememberMeServices customSecurityRememberMeService;

	@SuppressWarnings("unchecked")
	public Users customLogin(String id, String pwd, AuthEnum common, HttpServletRequest request,
			HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, pwd);

		Authentication authentication = authenticationManager.authenticate(token);

		HttpSession session = request.getSession();

		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());

		if (authentication != null) {
			List<GrantedAuthority> currentAuth = (List<GrantedAuthority>) authentication.getAuthorities();

			if (currentAuth.get(0).getAuthority().equals(common.name())) {

				customSecurityRememberMeService.loginSuccess(request, response, authentication);

				return repository.findById(id);
			}

		}

		return null;

	}

	public Users findId(String tel) {

		Users selectUser = repository.findByTel(tel);

		if (selectUser != null) {
			String maskedEmail = getMaskedEmail(selectUser.getId());

			selectUser.setId(maskedEmail);
			selectUser.setEmail(maskedEmail);
		}

		return selectUser;
	}

	public Users findPassword(String id, String tel) {
		return repository.findByIdAndTel(id, tel);
	}

	public Users signIn(Users users, AuthEnum common) {

		String auth = common.name().split("_")[1];

		users.setAuth(auth);
		users.setTelChkValue("T");
		users.setPwd(customPasswordEncoder.encode(users.getPwd()));

		return repository.save(users);
	}

	/*
	 * 그냥 검색해서 찾은 마스킹 코드
	 */
	public String getMaskedEmail(String email) {

		String regex = "\\b(\\S+)+@(\\S+.\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(email);
		if (matcher.find()) {
			String id = matcher.group(1);
			int length = id.length();
			if (length < 3) {
				char[] c = new char[length];
				Arrays.fill(c, '*');
				return email.replace(id, String.valueOf(c));
			} else if (length == 3) {
				return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
			} else {
				return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
			}
		}
		return email;
	}

}
