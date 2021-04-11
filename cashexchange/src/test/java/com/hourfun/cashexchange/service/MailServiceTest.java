package com.hourfun.cashexchange.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import javax.mail.MessagingException;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.model.Users;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MailServiceTest {

	@Autowired
	MailService mailService;
	
	@Autowired
	UsersService usersService; 
	
	@Test
	void test() {
		
		String email = "gnogun@naver.com";
		
		Users users = usersService.findByUserId(email);
		
		System.out.println("start");
		
		
		try {
			mailService.ontToOneInquirySend(users, new Date());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("end");
		
//		try {
//			mailService.mailSend();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
