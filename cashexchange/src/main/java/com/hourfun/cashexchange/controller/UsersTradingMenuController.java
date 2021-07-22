package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hourfun.cashexchange.model.TradingMenu;
import com.hourfun.cashexchange.service.TradingMenuService;

@Controller
@RequestMapping("/users/tradingMenu")
public class UsersTradingMenuController {

	@Autowired
	private TradingMenuService tradingMenuService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<TradingMenu>> findAll(){
		return new ResponseEntity<List<TradingMenu>>(tradingMenuService.findAll(),
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/menuName/{menuName}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<TradingMenu> findByMenuName(@PathVariable String menuName){
		return new ResponseEntity<TradingMenu>(tradingMenuService.findByMenuName(menuName),
				HttpStatus.OK);
	}
}
