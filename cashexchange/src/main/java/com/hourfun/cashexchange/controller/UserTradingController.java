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

	@RequestMapping(value = "/{company}/", method = RequestMethod.POST)
	public ResponseEntity<Trading> registTrading(Authentication auth, @PathVariable String company,
			@RequestBody List<String> pinCodes) {
		try {
			return new ResponseEntity<Trading>(service.save(auth.getName(), company, pinCodes), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByUserId(Authentication auth, @PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByCreateDateBetweenAndUserId(fromDate, toDate, auth.getName(), pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{idx}", method = RequestMethod.GET)
	public ResponseEntity<Trading> findByIdx(Authentication auth, @PathVariable String idx, Pageable pageable) {
		try {
			return new ResponseEntity<Trading>(service.findByIdx(auth.getName(), Long.parseLong(idx)), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/recent/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetween(Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByCreateDateBetweenMasking(pageable), HttpStatus.OK);
	}
}
