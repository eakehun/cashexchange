package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.LoginRequest;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.model.UsersRequest;
import com.hourfun.cashexchange.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UsersService service;

	@RequestMapping(value ="/login/id/{id}/pwd/{pwd}/", method = RequestMethod.GET)
	public String login(@PathVariable String id, @PathVariable String pwd, HttpSession session) {
        
//		return service.customLogin(loginRequest, session);
		
		return "";
	}
	
	@RequestMapping(value ="/findId/{email}/", method = RequestMethod.GET)
	public @ResponseBody Users findId(@PathVariable String email) {
		
//		return service.findId(request);
		
		return null;
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

