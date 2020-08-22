package com.hourfun.cashexchange.repository;


import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.model.Member;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MemberRepositoryTest {

	@Autowired
	private MemberRepository repository;
	@Autowired
	private AgreementRepository agreementRepository;
	
	@Autowired
	private PasswordEncoder customPasswordEncoder;
	
	@Test
	void save() {
		Member member = new Member();
		member.setAccountName("신한");
		member.setAccountNum("123456789");
		member.setAccountStatus("가능");
		member.setAuth("USER");
		member.setBirth("781211");
		member.setEmail("zest111@gmail.com");
		member.setGender("F");
		member.setId("gnogun");
		member.setMobileOperator("sk");
		member.setTel("123456789");
		member.setTelChkValue("T");
		
		
		
		member.setPwd(customPasswordEncoder.encode("qwer"));
		
		repository.save(member);
		
	}

//	@Test
	void agreementTest() {
		Member member =repository.findById(1l).get();
		Agreement agreement = agreementRepository.getOne(2l);
		Agreement agreement2 = agreementRepository.getOne(3l);
		
		List<Agreement> agreementList = new ArrayList<>();
		agreementList.add(agreement);
		agreementList.add(agreement2);
		
		member.setAgreements(agreementList);
		repository.save(member);
		
	}
}