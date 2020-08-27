package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.Member;
import com.hourfun.cashexchange.vo.LoginRequest;

@RestController
public class MemberController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public Authentication login(@RequestBody LoginRequest loginRequest, HttpSession session) {
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());
		
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                  SecurityContextHolder.getContext());
		
		return authentication;
	}
	
	@PostMapping("/signin")
	public String signin(@RequestBody Member member) {
		
		return null;
	}
}

