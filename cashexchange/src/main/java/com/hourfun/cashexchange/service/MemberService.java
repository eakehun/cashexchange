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
import com.hourfun.cashexchange.model.Member;
import com.hourfun.cashexchange.model.MemberRequest;
import com.hourfun.cashexchange.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder customPasswordEncoder;

	public String customLogin(LoginRequest loginRequest, HttpSession session) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getId(),
				loginRequest.getPassword());

		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());

		if (authentication != null) {
			return "success";
		}

		return "fail";
	}

	public MemberRequest findId(MemberRequest request) {

		Member member = repository.findByTel(request.getTel());

		String email = member.getId();

		request.setId(getMaskedEmail(email));
		request.setCreateDate(member.getCreateDate());

		return request;
	}

	public MemberRequest findPassword(MemberRequest request) {
		return null;
	}

	public String signIn(MemberRequest request) {

		Member member = new Member();
		member.setAccountName(request.getAccountName());
		member.setAccountNum(request.getAccountNum());
//		member.setAccountStatus("가능");
		member.setAuth("USER");
//		member.setBirth("781211");
//		member.setEmail("zest111@gmail.com");
//		member.setGender("F");

		member.setName(request.getName());
		member.setCi(request.getCi());

		member.setId(request.getId());
		member.setMobileOperator(request.getMobileOperator());
		member.setTel(request.getTel());
		member.setTelChkValue("T");

		member.setPwd(customPasswordEncoder.encode(request.getPwd()));

		if (repository.save(member) != null) {
			return "success";
		}

		return "fail";
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
