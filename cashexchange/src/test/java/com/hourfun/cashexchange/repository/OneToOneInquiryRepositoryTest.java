package com.hourfun.cashexchange.repository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryType;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class OneToOneInquiryRepositoryTest {

	@Autowired
	private OneToOneInquiryRepository repository;
	
	@Test
	void insert() {
		OneToOneInquiry oneToOneInquiry = new OneToOneInquiry();
		oneToOneInquiry.setContent("test content");
		oneToOneInquiry.setStatus(OneToOneInquiryType.Ready);
		oneToOneInquiry.setTitle("test title");
		oneToOneInquiry.setUserId("123");
		oneToOneInquiry.setUserName("abc");
		repository.save(oneToOneInquiry);
	}
	
//	@Test
	void findByCreateDateBetweenAndUserId() throws ParseException {
		String fromDateStr="2020-08-30 16:18:00";
		String toDateStr = "2020-08-30 16:18:02";
		String status = "ready";
		String userId="123";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date fromDate = transFormat.parse(fromDateStr);
		Date toDate = transFormat.parse(toDateStr);



		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "createDate"));
		Page<OneToOneInquiry> onePage =  repository.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, page);
		System.out.println(onePage);
	}
	
//	@Test
	void findByCreateDateBetweenAndUserIdAndStatus() throws ParseException {
		String fromDateStr="2020-08-30 16:18:00";
		String toDateStr = "2020-08-30 16:18:02";
		String status = "ready";
		OneToOneInquiryType oneToOneInquiryType= OneToOneInquiryType.Response_Complate;
		String userId="123";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date fromDate = transFormat.parse(fromDateStr);
		Date toDate = transFormat.parse(toDateStr);



		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "createDate"));
		
		Page<OneToOneInquiry> onePage =  repository.findByCreateDateBetweenAndUserIdAndStatus(fromDate, toDate, userId, oneToOneInquiryType, page);
		System.out.println(onePage);
	}
	
//	@Test
	void findByCreateDateBetweenAndTitleLikeAndStatus() throws ParseException {
		String fromDateStr="2020-08-30 16:18:00";
		String toDateStr = "2020-08-30 16:18:02";
		String status = "ready";
		OneToOneInquiryType oneToOneInquiryType= OneToOneInquiryType.Response_Complate;
		String userId="123";
		String title = "1";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date fromDate = transFormat.parse(fromDateStr);
		Date toDate = transFormat.parse(toDateStr);



		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "create_date"));
		
		Page<OneToOneInquiry> onePage =  repository.findByCreateDateBetweenAndTitleLikeAndStatus(fromDate, toDate, title,oneToOneInquiryType, page);
		System.out.println(onePage);
	}
	
//	@Test
	void findByCreateDateBetweenAndTitleLike() throws ParseException {
		String fromDateStr="2020-08-30 16:18:00";
		String toDateStr = "2020-08-30 16:18:02";
		String status = "ready";
		OneToOneInquiryType oneToOneInquiryType= OneToOneInquiryType.Ready;
		String userId="123";
		String title = "1";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date fromDate = transFormat.parse(fromDateStr);
		Date toDate = transFormat.parse(toDateStr);



		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "create_date"));
		
		Page<OneToOneInquiry> onePage =  repository.findByCreateDateBetweenAndTitleLike(fromDate, toDate, title, page);
		System.out.println(onePage);
	}
	
//	@Test
	void findByCreateDateBetweenAndUserIdAndStatusAndLikeTitleAndLikeContent() {
		String fromDate="2020-08-30 16:18:00";
		String toDate = "2020-08-30 16:18:02";
		String status = "ready";
		String userId="123";
		String title="test";
		String content="1";
		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "createDate"));
		Page<OneToOneInquiry> onePage =  
				repository.findByCreateDateBetweenAndUserIdAndStatusAndLikeTitleAndLikeContent(fromDate, toDate, status, userId, title, content, page);
		System.out.println(onePage);
	}

}
