package com.hourfun.cashexchange.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.service.BankService;
import com.hourfun.cashexchange.service.TradingService;

@RestController
@RequestMapping("/admin")
public class AdminBankController {

	@Autowired
	private BankService service;
	
	@Autowired
	private TradingService tradingService;

	@RequestMapping(value = "/bank/pay/{tradingIdx}/", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<Trading> bankList(@PathVariable String tradingIdx) {
		try {
			return new ResponseEntity<Trading>(service.pay(tradingService.findByIdx(Long.valueOf(tradingIdx))), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
