package com.hourfun.cashexchange.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.TradingMenu;
import com.hourfun.cashexchange.repository.TradingMenuRepository;

@Service
public class TradingMenuService {

	@Autowired
	private TradingMenuRepository tradingMenuRepository;
	
	public TradingMenu save(TradingMenu tradingMenu) {
		if(tradingMenu.isUsed()) {
			tradingMenu.setStopDate(new Date());
		}
		return tradingMenuRepository.save(tradingMenu);
	}
	
	public List<TradingMenu> findAll(){
		return tradingMenuRepository.findAll();
	}
	
	public TradingMenu findByMenuName(String menuName){
		return tradingMenuRepository.findByMenuName(menuName);
	}
}
