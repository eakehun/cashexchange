package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;

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
			",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n" +
			"FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n" +
			"where td.user_id = :userId \r\n group by td.idx " ,			
			countQuery = "select count(*) from trading where \r\n" + 
					"user_id = :userId \r\n",
			nativeQuery = true)	
	Page<Trading> findByUserId(String userId, Pageable pageable);
	
	@Query(value="SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date" +
			",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n" +
			"FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n" +
			"where td.create_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx" ,			
			countQuery = "select count(*) from trading \r\n" + 
					"where create_date between :fromDate and :toDate and user_id = :userId \r\n" ,
			nativeQuery = true)	
	Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);
	
	@Query(value="SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date" +
			",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n" +
			"FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n" +
			"where td.create_date between :fromDate and :toDate \r\n group by td.idx" ,			
			countQuery = "select count(*) from trading \r\n" + 
					"where create_date between :fromDate and :toDate \r\n" ,
			nativeQuery = true)	
	Page<Trading> findByCreateDateBetween(Date fromDate, Date toDate, Pageable pageable);
	
	List<Trading> findByCreateDateBetween(Date fromDate, Date toDate);
	
	List<Trading> findByUserIdAndWithdrawStatusNot(String userId, String withdrawStatus);

	
	Page<TradingMini> findByCreateDate(Date fromDate, Pageable pageable);
	
	@Query(value="SELECT sum(comeplete_price) as complete_price\r\n" + 
			"FROM cashexchange.trading \r\n" + 
			"where create_date between :fromDate and :toDate\r\n" + 
			"and user_id = :userId")
	Long findCompletePriceByCreateDateBetween(String userId, Date fromDate, Date toDate);
}
