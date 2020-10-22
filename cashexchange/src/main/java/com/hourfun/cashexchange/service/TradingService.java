package com.hourfun.cashexchange.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Trading save(String userId, Trading trading) {
		Users user = usersRepository.findByUserId(userId);		
		
		trading.setUserId(user.getUserId());
		trading.setUserName(user.getName());
		trading.setStatus(TradingStatusEnum.PROGRESS.getValue());
		
		
		return tradingRepository.save(trading);
	}
	
	public Page<Trading> findByUserId(String userId, Pageable pageable){
		return tradingRepository.findByUserId(userId, pageable);
	}
	
	
	public Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable){
		return tradingRepository.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, pageable);
	}
}
