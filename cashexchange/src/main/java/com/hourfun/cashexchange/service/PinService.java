package com.hourfun.cashexchange.service;

import java.util.ArrayList;
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

	@SuppressWarnings("unchecked")
	public void setPinCode(String company, String pinCode) {		
		
		String key = "";
		
		if(company.equals("컬쳐랜드")) {
			key = "culture_pin";
		}else {
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
		
		if(company.equals("컬쳐랜드")) {
			key = "culture_pin";
		}else {
			key = "happy_pin";
		}
		
		if (redisTemplate.hasKey(key)) {
			List<String> list = (List<String>) redisTemplate.opsForValue().get(key);

			return list;
		} else {
			throw new Exception("redis key 없음");
		}

	}

	public List<PinCode> save(Trading trading, List<String> pinCodes) throws Exception{
		List<PinCode> saveList = new ArrayList<PinCode>();

		for (String pinCode : pinCodes) {
			PinCode code = new PinCode();
			code.setCompany(trading.getCompany());
			code.setPinCode(pinCode);
			code.setPrice(0);
			code.setStatus(TradingStatusEnum.PROGRESS.getValue());
			code.setTradingIdx(trading.getIdx());

			repository.save(code);
		}

		return saveList;
	}

	public PinCode update(PinCode pinCode) {

		PinCode code = repository.findByPinCode(pinCode.getPinCode());
		code.setPrice(pinCode.getPrice());

		if (pinCode.getPrice() == 0) {
			code.setStatus(TradingStatusEnum.FAIL.getValue());
		} else {
			code.setStatus(TradingStatusEnum.COMPLETE.getValue());
		}

		return repository.save(code);
	}

}
