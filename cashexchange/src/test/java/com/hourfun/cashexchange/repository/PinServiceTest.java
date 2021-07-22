package com.hourfun.cashexchange.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.service.PinService;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PinServiceTest {

	@Autowired
	private PinService service;
	
	@Autowired
	private PinCodeRepository repository;
	
//	@Test
	public void addPin() {
		
		String pinCode = "qwer";
		
//		service.setPinCode(pinCode);
	}
	
	
//	@Test
	public void getPinList() {
		
		try {
			System.out.println(service.getPinCode("culture"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void pinLikeSelect() {
		String likeString = "4180%8001%";
		String firstPin = "4180";
		String middlePin = "8001";
		
		
		System.out.println(repository.findByPincodeLike(likeString));
		
	}
}
