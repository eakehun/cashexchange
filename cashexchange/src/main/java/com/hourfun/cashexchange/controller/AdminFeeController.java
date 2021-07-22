package com.hourfun.cashexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.Fee;
import com.hourfun.cashexchange.service.FeeService;

@RestController
@RequestMapping("/admin/fee")
public class AdminFeeController {

	@Autowired
	private FeeService feeService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Fee>> findAll(Pageable pageable) {
		return new ResponseEntity<>(feeService.findAll(pageable), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
	public ResponseEntity<Fee> insertFaq(@RequestBody Fee fee) {
		if(fee.getIdx() >0l) {
			throw new IllegalArgumentException("Idx 값이 존재합니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(feeService.save(fee), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
	public ResponseEntity<Fee> updateFaq(@RequestBody Fee fee) {
		if(fee.getIdx() < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(feeService.save(fee), HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteById(long idx) {
		if(idx < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		feeService.deleteById(idx);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
