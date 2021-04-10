package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.service.PinService;

@RestController
@RequestMapping("/admin/pinCode")
public class AdminPinController {
	
	@Autowired
	private PinService service;

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public ResponseEntity<Page<PinCode>> selectPin(@RequestBody List<String> pinCodes, Pageable pageable) {
//		return new ResponseEntity<Page<PinCode>>(service.findByPinCodeIn(pinCodes, pageable),
//				HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Page<PinCode>> selectPin(@RequestBody List<String> pinCodes, Pageable pageable) {
		return new ResponseEntity<Page<PinCode>>(service.findByPinCodeIn(pinCodes, pageable),
				HttpStatus.OK);
	}
	
	
}
