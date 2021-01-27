package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.HistoryTrading;

@Repository
public interface HistoryTradingRepository extends JpaRepository<HistoryTrading, Long> {

	@Query(value = "INSERT INTO cashexchange.history_trading \r\n"
			+ "(idx, userId, userName, company, status, withdrawStatus, requestPrice, fees, comepletePrice, message, tel, purchaseFeePercents, createDate, pinCompleteDate, withdrawCompleteDate, updateDate) \r\n"
			+ "SELECT idx, userId, userName, company, status, withdrawStatus, requestPrice, fees, comepletePrice, message, tel, purchaseFeePercents, createDate, pinCompleteDate, withdrawCompleteDate, updateDate FROM cashexchange.trading"
			+ " where create_date between :fromDate and :toDate", nativeQuery = true)
	int insertIntoHistoryTradingCreateDateBetween(Date fromDate, Date toDate);

}
