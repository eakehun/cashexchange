package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.service.UsersService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UsersService service;
	
	private String auth = "ROLE_ADNIN";
		
	@RequestMapping(value ="/login/id/{id}/pwd/{pwd}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> login(@PathVariable String id, @PathVariable String pwd, HttpSession session) {
		return new ResponseEntity<Users>(service.customLogin(id, pwd, session, auth), HttpStatus.OK);
	}
	
	@RequestMapping(value ="/findId/{tel}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> findId(@PathVariable String tel) {
		return new ResponseEntity<Users>(service.findId(tel), HttpStatus.OK);
	}
	
	@RequestMapping(value ="/findPassword/", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Users> findPassword(@RequestBody Users users) {
		return new ResponseEntity<Users>(service.findPassword(users), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/signin/", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Users> signin(@RequestBody Users users) {
		return new ResponseEntity<Users>(service.signIn(users), HttpStatus.OK);
	}

}
