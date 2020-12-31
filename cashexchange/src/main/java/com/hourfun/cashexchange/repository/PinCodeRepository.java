package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.PinCode;

@Repository
@Transactional
public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
	public List<PinCode> findByTradingIdx(Long tradingIdx);
	
	PinCode findByPinCode(String pinCode);
	
	
	List<PinCode> findByPinCodeIn(List<String> pinStringList);
	
	
	@Query(value="SELECT * FROM cashexchange.pin_code where pin_code like :firstPin% and pin_code like %:middlePin%",
			nativeQuery = true)
	PinCode findByPincodeLike(String firstPin, String middlePin);
	
	long countByCreateDateBetween(Date fromDate, Date toDate);
}

