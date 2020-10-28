package com.hourfun.cashexchange.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.service.TradingService;

@RestController
@RequestMapping("/users/trading")
public class UserTradingController {
	
	@Autowired
	private TradingService service;
	
	@RequestMapping(value = "/regist/{company}/", method = RequestMethod.POST)
	public ResponseEntity<Trading> registTrading(Authentication auth, @PathVariable String company, @RequestBody List<String> pinCodes) {
		try {
			return new ResponseEntity<Trading>(service.save(auth.getName(), company, pinCodes),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByUserId(Authentication auth, Pageable pageable){
		return new ResponseEntity<Page<Trading>>(service.findByUserId((String) auth.getPrincipal(), pageable),
				HttpStatus.OK);
	}

}
