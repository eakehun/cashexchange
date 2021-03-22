package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.HistoryTrading;
import com.hourfun.cashexchange.model.TradingMini;

@Repository
public interface HistoryTradingRepository extends JpaRepository<HistoryTrading, Long> {

	@Query(value = "INSERT INTO history_trading \r\n"
			+ "(idx, userId, userName, company, status, withdrawStatus, requestPrice, fees, comepletePrice, message, tel, purchaseFeePercents, createDate, pinCompleteDate, withdrawCompleteDate, updateDate) \r\n"
			+ "SELECT idx, userId, userName, company, status, withdrawStatus, requestPrice, fees, comepletePrice, message, tel, purchaseFeePercents, createDate, pinCompleteDate, withdrawCompleteDate, updateDate FROM history_trading"
			+ " where create_date between :fromDate and :toDate", nativeQuery = true)
	int insertIntoHistoryTradingCreateDateBetween(Date fromDate, Date toDate);

	HistoryTrading findByIdx(Long idx);

	List<HistoryTrading> findByStatus(String status);

	@Query(value = "SELECT td.idx, td.comeplete_price, td.company, td.create_date, td.fees, td.pin_complete_date"
			+ ",request_price, td.status, td.withdraw_status, td.update_date, td.user_id, td.user_name, "
			+ "td.withdraw_complete_date, td.message, td.purchase_fee_percents, td.tel, td.device \r\n"
			+ "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.user_id = :userId \r\n group by td.idx ", countQuery = "select count(*) from history_trading where \r\n"
					+ "user_id = :userId \r\n", nativeQuery = true)
	Page<HistoryTrading> findByUserId(String userId, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate \r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetween(Date fromDate, Date toDate, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetween(Date fromDate, Date toDate);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_id = :userId \r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate and idx = :idx \r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndIdx(Date fromDate, Date toDate, Long idx, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndIdx(Date fromDate, Date toDate, Long idx);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_name = :userNname \r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndUserName(Date fromDate, Date toDate, String userNname, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndUserName(Date fromDate, Date toDate, String userNname);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.create_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where status = :status and create_date between :fromDate and :toDate", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndStatus(Date fromDate, Date toDate, String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.create_date between :fromDate and :toDate \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndStatus(Date fromDate, Date toDate, String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate and idx = :idx and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_name = :userName and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where create_date between :fromDate and :toDate and user_id = :userId and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByCreateDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId, String status,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.create_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId, String status);

	List<HistoryTrading> findByUserIdAndWithdrawStatusNot(String userId, String withdrawStatus);

	List<HistoryTrading> findByUserIdAndWithdrawStatus(String userId, String withdrawStatus);

	Page<TradingMini> findByCreateDate(Date fromDate, Pageable pageable);

	@Query(value = "SELECT sum(comeplete_price) as complete_price\r\n" + "from history_trading \r\n"
			+ "where create_date between :fromDate and :toDate\r\n" + "and user_id = :userId", nativeQuery = true)
	Integer findCompletePriceByCreateDateBetween(String userId, Date fromDate, Date toDate);

	long countByCreateDateBetween(Date fromDate, Date toDate);

	@Query(value = "SELECT * FROM history_trading where create_date between :fromDate and :toDate group by user_id", nativeQuery = true)
	List<HistoryTrading> findByCreateDateBetweenAndGroupByUserId(Date fromDate, Date toDate);

	@Query(value = "SELECT sum(request_price) as request_price, sum(comeplete_price) as complete_price FROM history_trading where create_date between :fromDate and :toDate", nativeQuery = true)
	Map<String, Object> findByCreateDateBetweenAndSumPrice(Date fromDate, Date toDate);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate \r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetween(Date fromDate, Date toDate, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetween(Date fromDate, Date toDate);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_id = :userId \r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndUserId(Date fromDate, Date toDate, String userId);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and idx = :idx \r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndIdx(Date fromDate, Date toDate, Long idx, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndIdx(Date fromDate, Date toDate, Long idx);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_name = :userNname \r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndUserName(Date fromDate, Date toDate, String userNname,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndUserName(Date fromDate, Date toDate, String userNname);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.pin_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where status = :status and pin_complete_date between :fromDate and :toDate", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndStatus(Date fromDate, Date toDate, String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.pin_complete_date between :fromDate and :toDate \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndStatus(Date fromDate, Date toDate, String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and idx = :idx and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_name = :userName and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where pin_complete_date between :fromDate and :toDate and user_id = :userId and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByPinCompleteDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId,
			String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.pin_complete_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByPinCompleteDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId,
			String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate \r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetween(Date fromDate, Date toDate, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetween(Date fromDate, Date toDate);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_id = :userId \r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserId(Date fromDate, Date toDate, String userId,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_id = :userId \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserId(Date fromDate, Date toDate, String userId);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and idx = :idx \r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndIdx(Date fromDate, Date toDate, Long idx, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.idx = :idx \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndIdx(Date fromDate, Date toDate, Long idx);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_name = :userNname \r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserName(Date fromDate, Date toDate, String userNname,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_name = :userNname \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserName(Date fromDate, Date toDate, String userNname);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.withdraw_complete_date between :fromDate and :toDate \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where status = :status and withdraw_complete_date between :fromDate and :toDate", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndStatus(Date fromDate, Date toDate, String status,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.status = :status and td.withdraw_complete_date between :fromDate and :toDate \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndStatus(Date fromDate, Date toDate, String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and idx = :idx and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status,
			Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.idx = :idx and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndIdxAndStatus(Date fromDate, Date toDate, Long idx, String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_name = :userName and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_name = :userName and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserNameAndStatus(Date fromDate, Date toDate, String userName,
			String status);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", countQuery = "select count(*) from history_trading \r\n"
					+ "where withdraw_complete_date between :fromDate and :toDate and user_id = :userId and status = :status\r\n", nativeQuery = true)
	Page<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId,
			String status, Pageable pageable);

	@Query(value = "SELECT * \r\n" + "from history_trading as td left join pin_code as pin on td.idx = pin.trading_idx \r\n"
			+ "where td.withdraw_complete_date between :fromDate and :toDate and td.user_id = :userId and td.status = :status \r\n group by td.idx", nativeQuery = true)
	List<HistoryTrading> findByWithdrawCompleteDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, String userId,
			String status);
	
	
	List<HistoryTrading> findByCreateDateBefore(Date createDate);


}
