package com.hourfun.cashexchange.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.repository.PinCodeRepository;

@Service
public class PinService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private PinCodeRepository repository;

	@Autowired
	private TradingService tradingService;

	@SuppressWarnings("unchecked")
	public void setPinCode(String company, String pinCode) {

		String key = "";

		if (company.equals("culture")) {
			key = "culture_pin";
		} else if (company.equals("happy")) {
			key = "happy_pin";
		}

		List<String> list = new ArrayList<String>();

		if (redisTemplate.hasKey(key)) {
			list = (List<String>) redisTemplate.opsForValue().get(key);
		}

		list.add(pinCode);

		redisTemplate.opsForValue().set(key, list);
	}

	@SuppressWarnings("unchecked")
	public List<String> getPinCode(String company) throws Exception {

		String key = "";
		List<String> returnList = new ArrayList<String>();

		if (company.equals("culture")) {
			key = "culture_pin";
		} else {
			key = "happy_pin";
		}

		if (redisTemplate.hasKey(key)) {
			List<String> list = (List<String>) redisTemplate.opsForValue().get(key);

			if (list.size() != 0) {

				int maxSize = 9;
				if (list.size() < 10) {
					maxSize = list.size();
				}
				int index = 0;

				Iterator<String> iter = list.iterator();

				while (iter.hasNext()) {
					String pincode = iter.next();
					iter.remove();
					returnList.add(pincode);
					index++;
					if (index == maxSize) {
						break;
					}
				}

				redisTemplate.opsForValue().set(key, list);
			}

			return returnList;
		} else {
			throw new Exception("redis key 없음");
		}

	}

	public List<PinCode> save(Trading trading, List<String> pinCodes) throws Exception {
		List<PinCode> saveList = new ArrayList<PinCode>();

		for (String pinCode : pinCodes) {
			PinCode code = new PinCode();
			code.setCompany(trading.getCompany());
			code.setPinCode(pinCode);
			code.setPrice(0);
			code.setStatus(TradingStatusEnum.PROGRESS.getValue());
			code.setTradingIdx(trading.getIdx());

			saveList.add(repository.save(code));
		}

		return saveList;
	}

	public List<PinCode> update(List<PinCode> pinCodes) {

		List<PinCode> returnList = new ArrayList<PinCode>();

		for (PinCode pinCode : pinCodes) {
			PinCode code = repository.findByPinCode(pinCode.getPinCode());
			code.setPrice(pinCode.getPrice());

			if (pinCode.getPrice() == 0) {
				code.setStatus(TradingStatusEnum.FAIL.getValue());
			} else {
				code.setStatus(TradingStatusEnum.COMPLETE.getValue());
			}

			code.setMessage(pinCode.getStatus() + " - " + pinCode.getMessage());

			checkAndUpdate(code);

			returnList.add(repository.save(code));
		}

		return returnList;
	}

	public void checkAndUpdate(PinCode pinCode) {
		long tradingIdx = pinCode.getTradingIdx();
		List<PinCode> selectPinCodes = repository.findByTradingIdx(tradingIdx);

		int complete = 0;
		int fail = 0;
		int progress = 0;

		String status = TradingStatusEnum.PROGRESS.getValue();

		for (PinCode selectPinCode : selectPinCodes) {
			if (selectPinCode.getStatus().equals(TradingStatusEnum.COMPLETE.getValue())) {
				complete++;
			} else if (selectPinCode.getStatus().equals(TradingStatusEnum.FAIL.getValue())) {
				fail++;
			} else if (selectPinCode.getStatus().equals(TradingStatusEnum.PROGRESS.getValue())) {
				progress++;
			}
		}

		if (progress > 0) {
			if (complete > 0) {
				status = TradingStatusEnum.PARTIAL_COMPLETE.getValue();
			} else if (fail > 0) {
				status = TradingStatusEnum.PROGRESS.getValue();
			}
		} else {
			if (complete > 0 && fail == 0) {
				status = TradingStatusEnum.COMPLETE.getValue();
			} else if (complete == 0 && fail > 0) {
				status = TradingStatusEnum.FAIL.getValue();
			} else {
				status = TradingStatusEnum.PARTIAL_COMPLETE.getValue();
			}

		}

		Trading trading = tradingService.findByIdx(tradingIdx);
		trading.setStatus(status);
		
		tradingService.update(trading);

	}

}
