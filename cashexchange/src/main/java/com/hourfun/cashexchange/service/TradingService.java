package com.hourfun.cashexchange.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.TradingRepository;
import com.hourfun.cashexchange.repository.UsersRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class TradingService {

	@Autowired
	private TradingRepository tradingRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PinService pinService;

	public Trading save(String userId, String company, List<String> pinCodes) {
		Users user = usersRepository.findByUserId(userId);

		Trading trading = new Trading();
		trading.setUserId(user.getUserId());
		trading.setCompany(company);
		trading.setUserName(user.getName());
		trading.setStatus(TradingStatusEnum.PROGRESS.getValue());
		trading.setWithdrawStatus(TradingStatusEnum.PROGRESS.getValue());


		try {
			Trading savedTrading = tradingRepository.save(trading);
			List<PinCode> savedPincodes = pinService.save(savedTrading, pinCodes);
			for (String pinCode : pinCodes) {
				pinService.setPinCode(company, pinCode);
			}
			return savedTrading;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException(e.getMessage());
		}


	}

	public Page<Trading> findByUserId(String userId, Pageable pageable) {
		return tradingRepository.findByUserId(userId, pageable);
	}

	public Trading findByIdx(long idx) {
		return tradingRepository.findByIdx(idx);
	}

	public Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId,
			Pageable pageable) {
		return tradingRepository.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, pageable);
	}

	public Page<Trading> findByCreateDateBetween(String fromDate, String toDate, Pageable pageable) {
		return tradingRepository.findByCreateDateBetween(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
	}

	public Trading update(Trading trading) {
		return tradingRepository.save(trading);
	}
}
