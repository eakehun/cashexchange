package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.Trading;
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
	
	@RequestMapping(value = "/trading/backup/", method = RequestMethod.GET)
	public ResponseEntity<String> backupTradingHistory(){
		try {
			tradingService.insertIntoHistoryTradingCreateDateBetween();
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/trading/delete/", method = RequestMethod.GET)
	public ResponseEntity<String> deleteOldTrading(){
		try {
			tradingService.deleteTradingCreateDateBefore();
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	
	
}
