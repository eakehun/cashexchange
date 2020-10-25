package com.hourfun.cashexchange.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.TradingMini;


@Repository
public interface TradingRepository extends JpaRepository<Trading, Long> {
	Trading findByIdx(Long idx);
	
	@Query(value="SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date" +
			",sum(pin.price)as request_price, td.status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n" +
			"FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n" +
			"where td.user_id = :userId \r\n" ,			
			countQuery = "select count(*) from trading where \r\n" + 
					"td.user_id = :userId \r\n",
			nativeQuery = true)	
	Page<Trading> findByUserId(String userId, Pageable pageable);
	
	@Query(value="SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date" +
			",sum(pin.price)as request_price, td.status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n" +
			"FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n" +
			"where td.create_date between :fromDate and :toDate and td.user_id = :userId \r\n" ,			
			countQuery = "select count(*) from trading \r\n" + 
					"where td.create_date between :fromDate and :toDate and user_id = :userId \r\n" ,
			nativeQuery = true)	
	Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);
	
	@Query(value="SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date" +
			",sum(pin.price)as request_price, td.status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n" +
			"FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n" +
			"where td.create_date between :fromDate and :toDate \r\n" ,			
			countQuery = "select count(*) from trading \r\n" + 
					"where td.create_date between :fromDate and :toDate \r\n" ,
			nativeQuery = true)	
	Page<Trading> findByCreateDateBetween(Date fromDate, Date toDate, Pageable pageable);

	
	Page<TradingMini> findByCreateDate(Date fromDate, Pageable pageable);
}
