package com.hourfun.cashexchange.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

}
