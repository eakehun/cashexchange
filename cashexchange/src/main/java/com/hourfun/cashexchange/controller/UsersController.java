package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.LoginRequest;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.model.UsersRequest;
import com.hourfun.cashexchange.service.UsersService;

@RestController
public class UsersController {
	
	@Autowired
	private UsersService service;

	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        
		return service.customLogin(loginRequest, session);
	}
	
	@PostMapping("/findid")
	public @ResponseBody UsersRequest findId(@RequestBody UsersRequest request) {
		
		return service.findId(request);
	}
	
	@PostMapping("/findPassword")
	public @ResponseBody UsersRequest findPassword(@RequestBody UsersRequest request) {
		return null;
	}
	
	@PostMapping("/signin")
	public String signin(@RequestBody UsersRequest request) {
		
		return service.signIn(request);
	}
}

