package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/")
	public String main(HttpSession session, Authentication auth, HttpServletRequest request) {
		return "main";
	}
	
	@GetMapping("/users/page")
	public String user(HttpSession session) {
		
		return "user";
	}
	
	@GetMapping("/admin/page")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager/page")
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/page")
	public String subpath() {
		return "subpath";
	}
}
