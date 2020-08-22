package com.hourfun.cashexchange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/")
	public String main() {
		return "main";
	}
	
	@GetMapping("/user/page")
	public String user() {
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
