package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.service.PinService;

@RestController
@RequestMapping("/macro")
public class MacroController {
	
	@Autowired
	private PinService service;
	
	
	@RequestMapping(value = "/{company}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> selectPin(Authentication auth, @PathVariable String company) {
		try {
			return new ResponseEntity<List<String>>(service.getPinCode(company), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	
}
