package com.hourfun.cashexchange.repository;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.common.AccountStatusEnum;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.service.UsersService;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UsersService service;
	
//	@Test
	public void userList() {
		
		String fromDate="2020-08-20 00:00:00";
		String toDate = "2020-09-15 23:59:59";
		String accountStatus = AccountStatusEnum.NORMAL.getValue();
		String userId="gnogun@naver.com";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "createDate"));
		
		Page<Users> list = service.findByCreateDateBetweenAndUserIdAndAccountStatus(fromDate, toDate, userId, accountStatus, page);
		
		System.out.println("!");
		
	}
	
	@Test
	public void updateUserStatus() {
		long idx = 1;
		String status = AccountStatusEnum.NORMAL.getValue();
		
		Users users = new Users();
		users.setIdx(idx);
		users.setAccountStatus(status);
		
		Users returnUsers = service.updateAccountStatus(users);
		System.out.println(returnUsers.getAccountStatus());
	}
}
