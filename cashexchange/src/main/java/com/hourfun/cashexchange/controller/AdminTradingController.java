package com.hourfun.cashexchange.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.service.TradingService;

@RestController
@RequestMapping("/users/trading")
public class AdminTradingController {

	@Autowired
	private TradingService service;
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetween(@PathVariable String fromDate, @PathVariable String toDate, Pageable pageable){
		return new ResponseEntity<Page<Trading>>(service.findByCreateDateBetween(fromDate, toDate, pageable),
				HttpStatus.OK);
	}
}
