package com.hourfun.cashexchange.service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.LoginRequest;
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

	public Users customLogin(String id, String pwd, HttpSession session) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id,
				pwd);

		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		
		if(authentication != null) {
			return repository.findById(id);
		}
		
		return null;

	}

	public Users findId(String tel) {

		Users selectUser = repository.findByTel(tel);
		
		if(selectUser != null) {
			String maskedEmail = getMaskedEmail(selectUser.getId());
			
			selectUser.setId(maskedEmail);
			selectUser.setEmail(maskedEmail);
		}
		
		return selectUser;
	}

	public Users findPassword(Users users) {
		return repository.findByIdAndTel(users.getId(), users.getTel());
	}

	public Users signIn(Users users) {

		users.setAuth("USER");
		users.setTelChkValue("T");
		users.setPwd(customPasswordEncoder.encode(users.getPwd()));

		
		return repository.save(users);
	}

	/*
	 * 그냥 검색해서 찾은  마스킹 코드
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
