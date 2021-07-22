package com.hourfun.cashexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.Fee;
import com.hourfun.cashexchange.repository.FeeRepository;


@Service
public class FeeService {

	@Autowired
	private FeeRepository feeRepository;
	
	
	public Page<Fee> findAll(Pageable pageable){
		return feeRepository.findAll(pageable);
	}
	
	public Fee save(Fee fee) {
		return feeRepository.save(fee);
	}
	
	public void deleteById(long idx) {
		feeRepository.deleteById(idx);
	}
	
	public Fee findByCompany(String company) {
		return feeRepository.findByCompany(company);
	}
}
