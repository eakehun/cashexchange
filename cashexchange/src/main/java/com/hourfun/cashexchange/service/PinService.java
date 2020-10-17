package com.hourfun.cashexchange.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PinService {

	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@SuppressWarnings("unchecked")
	public void setPinCode(String pinCode) {
		String key = "culture_pin";
		
		List<String> list = new ArrayList<String>();
		
		if (redisTemplate.hasKey(key)) {
			list = (List<String>) redisTemplate.opsForValue().get(key);			
        }
		
		list.add(pinCode);
		
		redisTemplate.opsForValue().set(key, list);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPinCode() throws Exception{
		String key = "culture_pin";
		
		if (redisTemplate.hasKey(key)) {
			List<String> list = (List<String>) redisTemplate.opsForValue().get(key);
			
			return list;
        }else {
        	throw new Exception("redis key 없음");
        }
		
	}
	
}
