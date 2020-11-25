package com.hourfun.cashexchange.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.repository.AgreementRepository;

@Service
public class AgreementService {
	
	@Autowired
	private AgreementRepository repository;

	public List<Agreement> findAll(){		
		return repository.findAll();
	}
	
	public List<Agreement> findByUsedTrue(){
		return repository.findByUsed(true);
	}
	
	
}
