package com.hourfun.cashexchange.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.Fee;
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

	@Autowired
	private FeeService feeService;

	@Autowired
	private BankService bankService;

	@SuppressWarnings("unchecked")
	public void setPinCode(String company, String pinCode) {

		String key = getRedisKey(company);

		
		List<String> list = new ArrayList<String>();

		if (redisTemplate.hasKey(key)) {
			list = (List<String>) redisTemplate.opsForValue().get(key);
		}

		list.add(pinCode);

		redisTemplate.opsForValue().set(key, list);
	}

	@SuppressWarnings("unchecked")
	public List<String> getPinCode(String company) throws Exception {

		String key = getRedisKey(company);
		String backupKey = getRedisUsedKey(company);

		List<String> returnList = new ArrayList<String>();

		if (redisTemplate.hasKey(key)) {
			List<String> list = (List<String>) redisTemplate.opsForValue().get(key);
			List<String> backList = (List<String>) redisTemplate.opsForValue().get(backupKey);
			if (backList == null) {
				backList = new ArrayList<>();
			}

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

				backList.addAll(returnList);

				redisTemplate.opsForValue().set(key, list);
				redisTemplate.opsForValue().set(backupKey, backList);
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
			String strPin = pinCode.getPinCode();
			PinCode selectPin = null;

			if (strPin.indexOf("-") > -1) {
				String firstPin = strPin.split("-")[0];
				String middlePin = strPin.split("-")[2];

				String likeString = firstPin + "____" + middlePin + "%";
				selectPin = repository.findByPincodeLike(likeString);
			} else {
				selectPin = repository.findByPinCode(strPin);
			}

			selectPin.setPrice(pinCode.getPrice());

			if (pinCode.getStatus().equals("X")) {
				selectPin.setStatus(TradingStatusEnum.FAIL.getValue());
			} else if (pinCode.getStatus().equals("D")) {
				selectPin.setStatus(TradingStatusEnum.ALREADY_ADDED.getValue());
			} else {
				selectPin.setStatus(TradingStatusEnum.COMPLETE.getValue());
			}

			selectPin.setMessage(pinCode.getStatus() + " - " + pinCode.getMessage());

			returnList.add(repository.save(selectPin));

			String company = selectPin.getCompany();
			String backupKey = getRedisUsedKey(company);

			if (redisTemplate.hasKey(backupKey)) {
				List<String> list = (List<String>) redisTemplate.opsForValue().get(backupKey);
				list.remove(selectPin.getPinCode());
				redisTemplate.opsForValue().set(backupKey, list);
			}

			checkAndUpdate(selectPin);
		}

		return returnList;
	}

	public void checkAndUpdate(PinCode pinCode) {
		long tradingIdx = pinCode.getTradingIdx();
		List<PinCode> selectPinCodes = repository.findByTradingIdx(tradingIdx);

		int complete = 0;
		int fail = 0;
		int progress = 0;
		int totalPrice = 0;
		int completePrice = 0;

		String status = TradingStatusEnum.PROGRESS.getValue();
		Trading trading = tradingService.findByIdx(tradingIdx);

		for (PinCode selectPinCode : selectPinCodes) {
			if (selectPinCode.getStatus().equals(TradingStatusEnum.COMPLETE.getValue())) {
				completePrice += selectPinCode.getPrice();
				complete++;
			} else if (selectPinCode.getStatus().equals(TradingStatusEnum.FAIL.getValue())) {
				fail++;
			} else if (selectPinCode.getStatus().equals(TradingStatusEnum.PROGRESS.getValue())) {
				progress++;
			}
			totalPrice += selectPinCode.getPrice();
		}

		trading.setRequestPrice(totalPrice);
		trading.setComepletePrice(completePrice);

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

			trading = tradingService.calcFee(trading);

			trading.setPinCompleteDate(new Date());
			try {
				bankService.pay(trading);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		trading.setStatus(status);

		tradingService.update(trading);

	}

	public List<PinCode> findByPinCodeIn(List<String> pinCodes) {
		return repository.findByPinCodeIn(pinCodes);
	}

	public Page<PinCode> findByPinCodeIn(List<String> pinCodes, Pageable pageable) {
		return repository.findByPinCodeIn(pinCodes, pageable);
	}

	public long countByCreateDateBetween() {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();

		return repository.countByCreateDateBetween(monthStart, now);
	}

	public List<String> recyclePin(String company) throws Exception {
		String key = getRedisKey(company);
		String backupKey = getRedisUsedKey(company);

		List<String> returnList = new ArrayList<String>();

		if (redisTemplate.hasKey(key)) {
			List<String> list = (List<String>) redisTemplate.opsForValue().get(key);
			List<String> backList = (List<String>) redisTemplate.opsForValue().get(backupKey);
			

			if (list == null) {
				list = new ArrayList<>();
			}
			
			if (backList == null) {
				backList = new ArrayList<>();
			}else {
				backList = recyclePinCodeStatusCheck(backList);
			}
			
			if (backList.size() > 0) {
				Iterator<String> iter = backList.iterator();
				while (iter.hasNext()) {
					String pincode = iter.next();
					iter.remove();
					returnList.add(pincode);
				}
				list.addAll(returnList);
				redisTemplate.opsForValue().set(key, list);
				redisTemplate.opsForValue().set(backupKey, backList);
			}
			return returnList;
		} else {
			throw new Exception("redis key 없음");
		}
	}

	public List<String> recyclePinCodeStatusCheck(List<String> backList) {
		List<PinCode> pinList = repository.findByPinCodeIn(backList);
		List<String> returnList = new ArrayList<String>();

		for (PinCode pinCode : pinList) {
			String status = pinCode.getStatus();

			if (status.equals(TradingStatusEnum.PROGRESS.getValue())) {
				returnList.add(pinCode.getPinCode());
			}

		}

		return returnList;
	}
	
	public String getRedisKey(String company) {
		if (company.equals("culture")) {
			return "culture_pin";
		} else {
			return "happy_pin";
		}
	}
	
	public String getRedisUsedKey(String company) {
		if (company.equals("culture")) {
			return "culture_pin_used";
		} else {
			return "happy_pin_used";
		}
	}
	
}