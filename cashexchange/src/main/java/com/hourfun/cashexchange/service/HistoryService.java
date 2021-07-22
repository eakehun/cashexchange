package com.hourfun.cashexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.repository.HistoryRepository;
import com.hourfun.cashexchange.util.DateUtils;


@Service
public class HistoryService {

	@Autowired
	HistoryRepository historyRepository;

	public History insertLoginHistory(History history) {
		history.setType("LOGIN");
		return historyRepository.save(history);
	}

	public History insertConfigHistory(History history) {
		return historyRepository.save(history);
	}

	public Page<History> selectLoginHistory(Pageable pageRequest) {
		return historyRepository.findByType(pageRequest, "LOGIN");
	}

	public History upsertHistory(History history, String type) {
		history.setType(type);
		return historyRepository.save(history);
	}

	public Page<History> selectHistory(Pageable pageRequest, String type) {
		type = type.toUpperCase();
		return historyRepository.findByType(pageRequest, type);
	}

	public History selectLastLoginHistory(String userId) {
		return historyRepository.findTopByUserAndTypeOrderByCreateDateDesc(userId, "login");
	}

	public Page<History> selectTradingMenuUpdateHistoryByCreateDate(String fromDate, String toDate,
			Pageable pageRequest) {
		Page<History> selectHistory = historyRepository.findByServiceAndCreateDateBetween(pageRequest, "tradingMenu",
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		return selectHistory;
	}

	public Page<History> selectTradingMenuUpdateHistoryByCreateDateAndCompany(String fromDate, String toDate,
			Pageable pageRequest, String company) {
		String likeString = company + "%success";
		Page<History> selectHistory = historyRepository.findByServiceAndCreateDateBetweenAndKeywordLike(pageRequest,
				"tradingMenu", DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), likeString);
		return selectHistory;
	}

	public Page<History> selectTradingMenuUpdateHistoryByCreateDateAndUsed(String fromDate, String toDate,
			Pageable pageRequest, String used) {
		String likeString = "%" + used + " success";
		Page<History> selectHistory = historyRepository.findByServiceAndCreateDateBetweenAndKeywordLike(pageRequest,
				"tradingMenu", DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), likeString);
		return selectHistory;
	}

	public Page<History> selectTradingMenuUpdateHistoryByCreateDateAndCompanyAndUsed(String fromDate, String toDate,
			Pageable pageRequest, String company, String used) {
		String keywordString = company + " " + used + " success";
		Page<History> selectHistory = historyRepository.findByServiceAndKeywordAndCreateDateBetween(pageRequest,
				"tradingMenu", keywordString, DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		return selectHistory;
	}
}
