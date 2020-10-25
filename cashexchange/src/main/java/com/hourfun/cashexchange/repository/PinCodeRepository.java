package com.hourfun.cashexchange.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.PinCode;

@Repository
@Transactional
public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
	public Page<PinCode> findByTradingIdx(Long tradingIdx, Pageable pageable);
	
	PinCode findByPinCode(String pinCode);
}

