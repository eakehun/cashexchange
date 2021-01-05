package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.user_id = :userId \r\n group by td.idx ", countQuery = "select count(*) from trading where \r\n"
					+ "user_id = :userId \r\n", nativeQuery = true)
	Page<Trading> findByUserId(String userId, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate \r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetween(Date fromDate, Date toDate, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_id = :userId \r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate and idx = :idx \r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndIdx(Date fromDate, Date toDate, Long idx, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_name = :userNname \r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndUserName(Date fromDate, Date toDate, String userNname, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.create_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where status = :status and create_date between :fromDate and :toDate", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndStatus(Date fromDate, Date toDate, String status, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate and idx = :idx and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status,
			Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_name = :userName and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_id = :userId and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByCreateDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId, String status,
			Pageable pageable);

	List<Trading> findByCreateDateBetween(Date fromDate, Date toDate);

	List<Trading> findByUserIdAndWithdrawStatusNot(String userId, String withdrawStatus);

	Page<TradingMini> findByCreateDate(Date fromDate, Pageable pageable);

	@Query(value = "SELECT sum(comeplete_price) as complete_price\r\n" + "FROM trading \r\n"
			+ "where create_date between :fromDate and :toDate\r\n" + "and user_id = :userId", nativeQuery = true)
	Integer findCompletePriceByCreateDateBetween(String userId, Date fromDate, Date toDate);

	long countByCreateDateBetween(Date fromDate, Date toDate);

	@Query(value = "SELECT * FROM cashexchange.trading where create_date between :fromDate and :toDate group by user_id",
			nativeQuery = true)
	List<Trading> findByCreateDateBetweenAndGroupByUserId(Date fromDate, Date toDate);
	
	
	@Query(value = "SELECT sum(request_price) as request_price, sum(comeplete_price) as complete_price FROM cashexchange.trading where create_date between :fromDate and :toDate",
			nativeQuery = true)
	Map<String, Object> findByCreateDateBetweenAndSumPrice(Date fromDate, Date toDate);
	
	
	
	
	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate \r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetween(Date fromDate, Date toDate, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_id = :userId \r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and idx = :idx \r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndIdx(Date fromDate, Date toDate, Long idx, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_name = :userNname \r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndUserName(Date fromDate, Date toDate, String userNname, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.pin_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where status = :status and pin_complete_date between :fromDate and :toDate", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndStatus(Date fromDate, Date toDate, String status, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and idx = :idx and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status,
			Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_name = :userName and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_id = :userId and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByPinCompleteDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId, String status,
			Pageable pageable);
	
	
	
	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate \r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetween(Date fromDate, Date toDate, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_id = :userId \r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and idx = :idx \r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndIdx(Date fromDate, Date toDate, Long idx, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_name = :userNname \r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndUserName(Date fromDate, Date toDate, String userNname, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.withdraw_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where status = :status and withdraw_complete_date between :fromDate and :toDate", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndStatus(Date fromDate, Date toDate, String status, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and idx = :idx and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status,
			Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_name = :userName and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status, Pageable pageable);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",(select sum(price) from pin_code where trading_idx = td.idx)as request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, td.withdraw_complete_date \r\n"
			+ "FROM trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_id = :userId and status = :status\r\n", nativeQuery = true)
	Page<Trading> findByWithdrawCompleteDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId, String status,
			Pageable pageable);


}
