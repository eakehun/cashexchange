package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.LoginRequest;
import com.hourfun.cashexchange.model.Member;
import com.hourfun.cashexchange.model.MemberRequest;
import com.hourfun.cashexchange.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService service;

	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        
		return service.customLogin(loginRequest, session);
	}
	
	@PostMapping("/findid")
	public @ResponseBody MemberRequest findId(@RequestBody MemberRequest request) {
		
		return service.findId(request);
	}
	
	@PostMapping("/findPassword")
	public @ResponseBody MemberRequest findPassword(@RequestBody MemberRequest request) {
		return null;
	}
	
	@PostMapping("/signin")
	public String signin(@RequestBody MemberRequest request) {
		
		return service.signIn(request);
	}
}

