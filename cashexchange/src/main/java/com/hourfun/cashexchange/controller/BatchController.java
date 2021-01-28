package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.service.CaptchaService;
import com.hourfun.cashexchange.service.ObserverService;
import com.hourfun.cashexchange.service.PinService;
import com.hourfun.cashexchange.service.TradingService;

@RestController
@RequestMapping("/batch")
public class BatchController {
	
	@Autowired
	private TradingService tradingService;
	
	@RequestMapping(value = "/trading/retransfer/", method = RequestMethod.GET)
	public ResponseEntity<List<Trading>> transferAllWithdrawFailTrading(){
		try {
			return new ResponseEntity<List<Trading>>(tradingService.transferAllWithdrawFailTrading(), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	
	
}
