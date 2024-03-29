package com.hourfun.cashexchange.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.service.BankService;

@RestController
@RequestMapping("/users")
public class UserBankController {

	@Autowired
	private BankService service;

	@RequestMapping(value = "/bank/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String, String>> bankList() {
		try {
			return new ResponseEntity<Map<String, String>>(service.bankList(), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/bank/{code}/{account}/{name}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> bankVerify(@PathVariable String code,
			@PathVariable String account, @PathVariable String name) {
		try {
			return new ResponseEntity<String>(service.ownerCheck(code, account, name), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	

	@RequestMapping(value = "/bank/{bankCode}/{divCode}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> bankList(@PathVariable String bankCode, @PathVariable String divCode) {
		try {
			return new ResponseEntity<String>(service.maintenanceCheck(bankCode, divCode), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
