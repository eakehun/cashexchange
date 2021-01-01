package com.hourfun.cashexchange.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.AgreementRepository;

@Service
public class AgreementService {

	@Autowired
	private AgreementRepository repository;

	@Autowired
	private UsersService usersService;

	public List<Agreement> findAll() {
		return repository.findAll();
	}

	public List<Agreement> findByUsedTrue() {
		return repository.findByUsed(true);
	}

	public List<Agreement> findAllByUserId(String userId) {

		Users selectUser = usersService.findByUserId(userId);

		return repository.findAllByMemberIdx(selectUser.getIdx());
	}

	public List<Agreement> findAllByMemberIdx(long idx) {

		return repository.findAllByMemberIdx(idx);
	}

}
