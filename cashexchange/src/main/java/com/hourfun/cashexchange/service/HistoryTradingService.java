package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.HistoryPinCode;
import com.hourfun.cashexchange.model.HistoryTrading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.HistoryTradingRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class HistoryTradingService {

	@Autowired
	private UsersService usersService;

	@Autowired
	private HistoryTradingRepository historyTradingRepository;

	public Page<HistoryTrading> findByUserId(String userId, Pageable pageable) {
		return historyTradingRepository.findByUserId(userId, pageable);
	}

	public HistoryTrading findByIdx(long idx) {

		return historyTradingRepository.findByIdx(idx);
	}

	public HistoryTrading findByIdx(String userId, long idx) {

		HistoryTrading trading = historyTradingRepository.findByIdx(idx);

		if (userId.equals(trading.getUserId())) {
			return trading;
		} else {
			throw new IllegalArgumentException("권한이 없습니다.");
		}
	}

	public Page<HistoryTrading> findByDateBetween(String dateCategory, String fromDate, String toDate, Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;
	}

	public Page<HistoryTrading> findByDateBetweenAndUserId(String dateCategory, String fromDate, String toDate, String userId,
			Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<HistoryTrading> findByDateBetweenAndStatus(String dateCategory, String fromDate, String toDate, String status,
			Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		}
		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<HistoryTrading> findByDateBetweenAndUserName(String dateCategory, String fromDate, String toDate,
			String userName, Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<HistoryTrading> findByDateBetweenAndIdx(String dateCategory, String fromDate, String toDate, long idx,
			Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<HistoryTrading> findByDateBetweenAndIdxAndStatus(String dateCategory, String fromDate, String toDate, Long idx,
			String status, Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<HistoryTrading> findByDateBetweenAndUserIdAndStatus(String dateCategory, String fromDate, String toDate,
			String userId, String status, Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<HistoryTrading> findByDateBetweenAndUserNameAndStatus(String dateCategory, String fromDate, String toDate,
			String userName, String status, Pageable pageable) {

		Page<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetween(String dateCategory, String fromDate, String toDate) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		}

		selectTradingAccountInfo(returnVal);
		return returnVal;
	}

	public List<HistoryTrading> findByDateBetweenAndIdx(String dateCategory, String fromDate, String toDate, long idx) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetweenAndUserId(String dateCategory, String fromDate, String toDate,
			String userId) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetweenAndUserName(String dateCategory, String fromDate, String toDate,
			String userName) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetweenAndStatus(String dateCategory, String fromDate, String toDate,
			String status) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status);
		}
		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetweenAndIdxAndStatus(String dateCategory, String fromDate, String toDate, Long idx,
			String status) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetweenAndUserIdAndStatus(String dateCategory, String fromDate, String toDate,
			String userId, String status) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<HistoryTrading> findByDateBetweenAndUserNameAndStatus(String dateCategory, String fromDate, String toDate,
			String userName, String status) {

		List<HistoryTrading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = historyTradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = historyTradingRepository.findByPinCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status);
		} else {
			returnVal = historyTradingRepository.findByWithdrawCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public File excelDownload(List<HistoryTrading> selectList, String fromDate, String toDate) {

		StringBuilder content = new StringBuilder();

		content.append("idx").append(",").append("userId").append(",").append("userName").append(",").append("company")
				.append(",").append("status").append(",").append("withdrawStatus").append(",").append("requestPrice")
				.append(",").append("fees").append(",").append("comepletePrice").append(",").append("message")
				.append(",").append("tel").append(",").append("purchaseFeePercents").append(",").append("device")
				.append(",").append("accountNum").append(",").append("accountName").append(",").append("pincode")
				.append(",").append("createDate").append(",").append("pinCompleteDate").append(",")
				.append("withdrawCompleteDate").append("\n");

		for (HistoryTrading trading : selectList) {
			List<HistoryPinCode> pinCodes = (List<HistoryPinCode>) trading.getPincode();
			String pinCodeString = "";

			for (HistoryPinCode pinCode : pinCodes) {
				pinCodeString = pinCodeString + pinCode.getPinCode() + "/";
			}
			pinCodeString = pinCodeString.substring(0, pinCodeString.length() - 1);

			content.append(trading.getIdx()).append(",").append(trading.getUserId()).append(",")
					.append(trading.getUserName()).append(",").append(trading.getCompany()).append(",")
					.append(trading.getStatus()).append(",").append(trading.getWithdrawStatus()).append(",")
					.append(trading.getRequestPrice()).append(",").append(trading.getFees()).append(",")
					.append(trading.getTel()).append(",").append(trading.getPurchaseFeePercents()).append(",")
					.append(trading.getDevice()).append(",").append(trading.getAccountNum()).append(",")
					.append(trading.getAccountName()).append(",").append(pinCodeString).append(",")
					.append(trading.getCreateDate()).append(",").append(trading.getPinCompleteDate()).append(",")
					.append(trading.getWithdrawCompleteDate()).append("\n");
		}

		File file = null;
		PrintWriter pw = null;

		fromDate = fromDate.substring(0, 10);
		toDate = toDate.substring(0, 10);

		try {
			file = new File("trading " + fromDate + " " + toDate + ".csv");
			pw = new PrintWriter(file);
			pw.write(content.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}

		return file;
	}



	public void selectTradingAccountInfo(List<HistoryTrading> tradingList) {

		for (HistoryTrading trading : tradingList) {
			Users user = usersService.findByUserId(trading.getUserId());

			trading.setAccountName(user.getAccountName());
			trading.setAccountNum(user.getAccountNum());

		}
	}

}
