package com.hourfun.cashexchange.repository;


import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryResponse;


@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class OneToOneInquiryResponseRepositoryTest {

	@Autowired
	private OneToOneInquiryRepository oneRepository;
	@Autowired
	private OneToOneInquiryResponseRepository oneResponseRepository;
	
	@Test
	void insert() {
		OneToOneInquiry oneInquiry = oneRepository.findById(1l).get();
		
		OneToOneInquiryResponse oneToOneInquiryResponse = new OneToOneInquiryResponse();
		oneToOneInquiryResponse.setContent("response");
		oneToOneInquiryResponse.setOneToOneInquiry(oneInquiry);
		oneToOneInquiryResponse.setUserId("admin");
		
		oneToOneInquiryResponse = oneResponseRepository.save(oneToOneInquiryResponse);
		//문제가 없다면 1:1 문의에 답변 시간을 update해준다. 
		if(oneToOneInquiryResponse != null) {
			oneInquiry.setResponseDate(new Date());
			oneRepository.save(oneInquiry);
		}
		
		
	}

}
