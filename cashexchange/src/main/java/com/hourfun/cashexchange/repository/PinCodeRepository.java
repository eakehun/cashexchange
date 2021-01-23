package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;

@Repository
@Transactional
public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
	public List<PinCode> findByTradingIdx(Long tradingIdx);
	
	PinCode findByPinCode(String pinCode);
	
	
	List<PinCode> findByPinCodeIn(List<String> pinStringList);
	
	
	Page<PinCode> findByPinCodeIn(List<String> pinStringList, Pageable pageable);
	
	
//	@Query(value="SELECT * FROM cashexchange.pin_code where pin_code like :firstPin%:middlePin%",
//			nativeQuery = true)
//	PinCode findByPincodeLike(String firstPin, String middlePin);
	
	@Query(value="SELECT * FROM cashexchange.pin_code where pin_code like :likeString",
			nativeQuery = true)
	PinCode findByPincodeLike(String likeString);
	
	long countByCreateDateBetween(Date fromDate, Date toDate);
}

