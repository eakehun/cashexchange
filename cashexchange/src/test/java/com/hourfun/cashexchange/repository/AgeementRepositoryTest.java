package com.hourfun.cashexchange.repository;

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

import com.hourfun.cashexchange.model.Agreement;

@ActiveProfiles(value = "local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AgeementRepositoryTest {

	@Autowired
	private AgreementRepository repository;
	
	@Test
	void save() {
		Agreement agreement = new Agreement();
		agreement.setChkNecessary(true);
		agreement.setUsed(true);
		agreement.setTitle("마케팅 정보 수신");
		agreement.setContents("");
		repository.save(agreement);
	}

	
//	@Test
	void findAllByMemberIdx() {
		PageRequest page = PageRequest.of(0,1000,Sort.by(Sort.Direction.DESC, "idx"));
		Page<Agreement> agreements =repository.findAllByMemberIdx(1l,page);
		System.out.println(agreements);
	}
}
