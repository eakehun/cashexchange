package com.hourfun.cashexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.Fee;
import com.hourfun.cashexchange.service.FeeService;

@RestController
@RequestMapping("/users/fee")
public class UsersFeeController {
	@Autowired
	private FeeService feeService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Fee>> findAll(Pageable pageable) {
		return new ResponseEntity<>(feeService.findAll(pageable), HttpStatus.CREATED);
	}
}
