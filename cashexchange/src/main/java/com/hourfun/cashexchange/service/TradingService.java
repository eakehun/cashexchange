package com.hourfun.cashexchange.service;

import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.TradingRepository;
import com.hourfun.cashexchange.repository.UsersRepository;

@Service
public class TradingService {

	private TradingRepository tradingRepository;
	
	private UsersRepository usersRepository;
	
	public Trading registTrading(String userId, Trading trading) {
		Users user = usersRepository.findByUserId(userId);		
		
		trading.setUserId(user.getUserId());
		trading.setUserName(user.getName());
		trading.setStatus(TradingStatusEnum.PROGRESS.getValue());
		
		
		return tradingRepository.save(trading);
	}
}
