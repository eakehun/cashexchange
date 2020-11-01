package com.hourfun.cashexchange.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.PinCode;

@Repository
@Transactional
public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
	public List<PinCode> findByTradingIdx(Long tradingIdx);
	
	PinCode findByPinCode(String pinCode);
}

