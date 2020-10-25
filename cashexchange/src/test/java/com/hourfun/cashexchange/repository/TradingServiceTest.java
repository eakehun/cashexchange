package com.hourfun.cashexchange.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.service.PinService;
import com.hourfun.cashexchange.service.TradingService;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TradingServiceTest {

	@Autowired
	private TradingService service;
	
	@Autowired
	private PinService pinService;
	
	
//	@Test
	public void tradingSave() {
		String userId = "gnogun@naver.com";
		
		Trading trading = new Trading();
		
		trading.setCompany("컬쳐랜드");
		
		Trading saveTrading = service.save(userId, trading);
		
		pinSave(saveTrading);
	}
	
	public void pinSave(Trading trading) {
		List<String> pinList = new ArrayList<String>();
		
		pinList.add("pin123");
		pinList.add("pin456");
		pinList.add("pin789");
		pinList.add("pin101");
		
		pinService.save(trading, pinList);
	}
	
//	@Test
	public void pinUpdate() {
		PinCode code = new PinCode();
		code.setPinCode("pin123");
		code.setPrice(10000);		
		
		pinService.update(code);
		
		code.setPinCode("pin456");
		code.setPrice(10000);		
		
		pinService.update(code);
		
		code.setPinCode("pin789");
		code.setPrice(10000);		
		
		pinService.update(code);
		
		code.setPinCode("pin101");
		code.setPrice(0);		
		
		pinService.update(code);
	}
	
//	@Test
	public void tradingSelect() {
		String userId = "gnogun@naver.com";
		PageRequest pageable = PageRequest.of(0,10,Sort.by(Sort.Direction.DESC, "idx"));
		
		Page<Trading> tradings = service.findByUserId(userId, pageable);
		
		System.out.println("!");
	}
	
	@Test
	public void tradingSelectDate() throws ParseException {
		String fromDateStr="2020-08-30 16:18:00";
		String toDateStr = "2020-10-26 16:18:02";
		String status = "ready";
		String userId="123";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date fromDate = transFormat.parse(fromDateStr);
		Date toDate = transFormat.parse(toDateStr);
		
		PageRequest pageable = PageRequest.of(0,10,Sort.by(Sort.Direction.DESC, "idx"));
		
		Page<Trading> tradings = service.findByCreateDateBetween(fromDate, toDate, pageable);
		
		System.out.println("!");
	}
}
