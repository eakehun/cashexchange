package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.service.AgreementService;

@RestController
@RequestMapping("/users/agreement")
public class UsersAgreementController {

	@Autowired
	private AgreementService service;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Agreement>> findByUsedTrue() {
		return new ResponseEntity<List<Agreement>>(service.findByUsedTrue(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/info/", method = RequestMethod.GET)
	public ResponseEntity<List<Agreement>> findAllByMemberIdx(Authentication auth) {
		try {
			return new ResponseEntity<List<Agreement>>(service.findAllByUserId(auth.getName()), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());			
		}
	}
}
