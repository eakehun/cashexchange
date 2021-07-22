package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.service.AgreementService;

@RestController
@RequestMapping("/admin/agreement")
public class AdminAgreementController {

	@Autowired
	private AgreementService service;
	
	@RequestMapping(value = "/info/{idx}", method = RequestMethod.GET)
	public ResponseEntity<List<Agreement>> findAllByMemberIdx(@PathVariable String idx) {
		try {
			return new ResponseEntity<List<Agreement>>(service.findAllByMemberIdx(Long.valueOf(idx)), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());			
		}
	}
}
