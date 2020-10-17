package com.hourfun.cashexchange.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.TradingMini;


@Repository
public interface TradingRepository extends JpaRepository<Trading, Long> {
	Trading findByIdx(Long idx);
	
	Page<Trading> findbyUserId(String userId, Pageable pageable);
	
	Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);

	Page<TradingMini> findByCreateDate(Date fromDate, Pageable pageable);
}
