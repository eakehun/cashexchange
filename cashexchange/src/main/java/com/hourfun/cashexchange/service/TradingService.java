package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.Fee;
import com.hourfun.cashexchange.model.HistoryPinCode;
import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.HistoryTrading;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.PinCodeRepository;
import com.hourfun.cashexchange.repository.HistoryTradingRepository;
import com.hourfun.cashexchange.repository.TradingRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class TradingService {

	@Autowired
	private TradingRepository tradingRepository;

	@Autowired
	private PinCodeRepository pinCodeRepository;

	@Autowired
	private UsersService usersService;

	@Autowired
	private FeeService feeService;

	@Autowired
	private PinService pinService;

	@Value("${trading.limit.day}")
	private String dayLimit;

	@Value("${trading.limit.month}")
	private String monthLimit;

	@Value("${trading.limit.night}")
	private String nightLimit;

	@Autowired
	private HistoryTradingRepository historyTradingRepository;

	@Autowired
	private BankService bankService;

	public Trading save(String userId, String company, List<String> pinCodes) {

		try {
			if (pinCodes == null || pinCodes.size() == 0) {
				throw new IllegalArgumentException("pinCodes null");
			}

			if (company.equals("culture")) {
				for (String pinCode : pinCodes) {
					if (pinCode.length() == 16 || pinCode.length() == 18) {
						continue;
					} else {
						throw new IllegalArgumentException("pinCodes size not available (16 or 18)");
					}
				}
			} else {
				for (String pinCode : pinCodes) {
					if (pinCode.length() != 24) {
						throw new IllegalArgumentException("pinCodes size not available (16 + 8)");
					} else {
						continue;
					}
				}
			}

			List<PinCode> selectPinCodes = pinService.findByPinCodeIn(pinCodes);

			if (selectPinCodes.size() > 0) {
				String duplicatePin = "";
				for (String pinCode : pinCodes) {

					for (PinCode selectPin : selectPinCodes) {
						if (pinCode.equals(selectPin.getPinCode())) {
							duplicatePin += pinCode + " ";
						}
					}
				}

				throw new IllegalArgumentException(duplicatePin + "pinCodes duplicate");
			}

			Users user = usersService.findByUserId(userId);

			Trading trading = new Trading();
			trading.setUserId(user.getUserId());
			trading.setCompany(company);
			trading.setUserName(user.getName());
			trading.setStatus(TradingStatusEnum.PROGRESS.getValue());
			trading.setWithdrawStatus(TradingStatusEnum.PROGRESS.getValue());
			trading.setRequestPrice(0);
			trading.setComepletePrice(0);
			trading.setTel(user.getTel());

			String companyRealName = "";

			if (trading.getCompany().equals("culture")) {
				companyRealName = "컬처랜드";
			} else {
				companyRealName = "해피머니";
			}

			Fee fee = feeService.findByCompany(companyRealName);
			trading.setPurchaseFeePercents(fee.getPurchaseFeePercents());

			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();

			String userAgent = request.getHeader("User-Agent").toUpperCase();

			if (userAgent.indexOf("MOBILE") > -1) {
				if (userAgent.indexOf("PHONE") == -1) {
					trading.setDevice("PHONE");
				} else {
					trading.setDevice("TABLET");
				}
			} else {
				trading.setDevice("PC");
			}

			Trading savedTrading = tradingRepository.save(trading);
			pinService.save(savedTrading, pinCodes);
			for (String pinCode : pinCodes) {
				pinService.setPinCode(company, pinCode);
			}
			return savedTrading;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException(e.getMessage());
		}

	}

	public Page<Trading> findByUserId(String userId, Pageable pageable) {
		return tradingRepository.findByUserId(userId, pageable);
	}

	public Trading findByIdx(long idx) {

		return tradingRepository.findByIdx(idx);
	}

	public Trading findByIdx(String userId, long idx) {

		Trading trading = tradingRepository.findByIdx(idx);

		if (userId.equals(trading.getUserId())) {
			return trading;
		} else {
			throw new IllegalArgumentException("권한이 없습니다.");
		}
	}

	public Page<Trading> findByDateBetween(String dateCategory, String fromDate, String toDate, Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;
	}

	public Page<Trading> findByDateBetweenAndUserId(String dateCategory, String fromDate, String toDate, String userId,
			Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<Trading> findByDateBetweenAndStatus(String dateCategory, String fromDate, String toDate, String status,
			Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		}
		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<Trading> findByDateBetweenAndUserName(String dateCategory, String fromDate, String toDate,
			String userName, Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<Trading> findByDateBetweenAndIdx(String dateCategory, String fromDate, String toDate, long idx,
			Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<Trading> findByDateBetweenAndIdxAndStatus(String dateCategory, String fromDate, String toDate, Long idx,
			String status, Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<Trading> findByDateBetweenAndUserIdAndStatus(String dateCategory, String fromDate, String toDate,
			String userId, String status, Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public Page<Trading> findByDateBetweenAndUserNameAndStatus(String dateCategory, String fromDate, String toDate,
			String userName, String status, Pageable pageable) {

		Page<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		}

		selectTradingAccountInfo(returnVal.getContent());

		return returnVal;

	}

	public List<Trading> findByDateBetween(String dateCategory, String fromDate, String toDate) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));
		}

		selectTradingAccountInfo(returnVal);
		return returnVal;
	}

	public List<Trading> findByDateBetweenAndIdx(String dateCategory, String fromDate, String toDate, long idx) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<Trading> findByDateBetweenAndUserId(String dateCategory, String fromDate, String toDate,
			String userId) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<Trading> findByDateBetweenAndUserName(String dateCategory, String fromDate, String toDate,
			String userName) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<Trading> findByDateBetweenAndStatus(String dateCategory, String fromDate, String toDate,
			String status) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status);
		}
		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<Trading> findByDateBetweenAndIdxAndStatus(String dateCategory, String fromDate, String toDate, Long idx,
			String status) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<Trading> findByDateBetweenAndUserIdAndStatus(String dateCategory, String fromDate, String toDate,
			String userId, String status) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public List<Trading> findByDateBetweenAndUserNameAndStatus(String dateCategory, String fromDate, String toDate,
			String userName, String status) {

		List<Trading> returnVal = null;

		if (dateCategory.equals("createDate")) {
			returnVal = tradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status);
		} else if (dateCategory.equals("pinCompleteDate")) {
			returnVal = tradingRepository.findByPinCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status);
		} else {
			returnVal = tradingRepository.findByWithdrawCompleteDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status);
		}

		selectTradingAccountInfo(returnVal);

		return returnVal;

	}

	public File excelDownload(List<Trading> selectList, String fromDate, String toDate) {

		StringBuilder content = new StringBuilder();

		content.append("번호").append(",").append("사용자 아이디").append(",").append("사용자 이름").append(",").append("상품권 구분")
				.append(",").append("거래 상태").append(",").append("송금 상태").append(",").append("요청 금액")
				.append(",").append("수수료").append(",").append("송금 금액").append(",").append("송금 상태 메시지")
				.append(",").append("전화번호").append(",").append("거래 수수료 비율").append(",").append("장치")
				.append(",").append("계좌번호").append(",").append("은행명").append(",").append("핀코드")
				.append(",").append("신청일").append(",").append("처리완료일").append(",")
				.append("송금완료일").append("\n");

		for (Trading trading : selectList) {
			List<PinCode> pinCodes = (List<PinCode>) trading.getPincode();
			String pinCodeString = "";

			for (PinCode pinCode : pinCodes) {
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
			OutputStream os = new FileOutputStream(file);
			pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
			pw.write(content.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}

		return file;
	}

	public Trading update(Trading trading) {
		return tradingRepository.save(trading);
	}

	public Page<Trading> findByCreateDateBetweenMasking(Pageable pageable) {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -7);

		Date week = cal.getTime();

		Page<Trading> tradings = tradingRepository.findByCreateDateBetween(week, now, pageable);

		tradingMasking(tradings.getContent());

		return tradings;
	}

	public void tradingMasking(List<Trading> list) {

		for (Trading trading : list) {
			String replaceString = trading.getUserName();

			String pattern = "";
			if (replaceString.length() <= 2) {
				pattern = "^(.)(.+)$";
			} else {
				pattern = "^(.)(.+)(.)$";
			}

			Matcher matcher = Pattern.compile(pattern).matcher(replaceString);

			if (matcher.matches()) {
				replaceString = "";

				for (int i = 1; i <= matcher.groupCount(); i++) {
					String replaceTarget = matcher.group(i);
					if (i == 2) {
						char[] c = new char[replaceTarget.length()];
						Arrays.fill(c, '*');

						replaceString = replaceString + String.valueOf(c);
					} else {
						replaceString = replaceString + replaceTarget;
					}

				}
			}

			SimpleDateFormat creteDateFormat = new SimpleDateFormat("MM-dd");
			SimpleDateFormat withdrawCompleteDateFormat = new SimpleDateFormat("HH:mm");

			trading.setCreateDateString(creteDateFormat.format(trading.getCreateDate()));

			if (trading.getWithdrawCompleteDate() != null) {
				trading.setWithdrawCompleteDateString(
						withdrawCompleteDateFormat.format(trading.getWithdrawCompleteDate()));
			} else {
				trading.setWithdrawCompleteDateString("");
			}

			trading.setUserName(replaceString);
		}

	}

	public List<Trading> findByUserIdAndWithdrawStatusNot(String userId, String withdrawStatus) {
		return tradingRepository.findByUserIdAndWithdrawStatusNot(userId, withdrawStatus);
	}

	public List<Trading> findByUserIdAndWithdrawStatus(String userId, String withdrawStatus) {
		return tradingRepository.findByUserIdAndWithdrawStatus(userId, withdrawStatus);
	}

	@SuppressWarnings("unused")
	public Map<String, Integer> getTradingLimit(String userId) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();

		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date dayStart = calendar.getTime();

		Date nightStart = null;

		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();

		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		calendar.setTime(now);
		if (calendar.get(Calendar.HOUR_OF_DAY) > 22) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			nightStart = calendar.getTime();
		}

		if (calendar.get(Calendar.HOUR_OF_DAY) < 9) {
			calendar.add(Calendar.DATE, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			nightStart = calendar.getTime();
		}

		Integer dayCompletePrice = tradingRepository.findCompletePriceByCreateDateBetween(userId, dayStart, now);
		Integer monthCompletePrice = tradingRepository.findCompletePriceByCreateDateBetween(userId, monthStart, now);

		returnMap.put("dayLimit", Integer.valueOf(dayLimit));
		returnMap.put("monthLimit", Integer.valueOf(monthLimit));
		returnMap.put("dayCompletePrice", dayCompletePrice);
		returnMap.put("monthCompletePrice", monthCompletePrice);
		returnMap.put("nightLimit", Integer.valueOf(nightLimit));

		if (nightStart != null) {
			Integer nightCompletePrice = tradingRepository.findCompletePriceByCreateDateBetween(userId, nightStart,
					now);

			returnMap.put("nightCompletePrice", monthCompletePrice);

		}

		return returnMap;

	}

	public List<Trading> findByCreateDateBetweenAndGroupByUserId() {

		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();

		return tradingRepository.findByCreateDateBetweenAndGroupByUserId(monthStart, now);
	}

	public long countByCreateDateBetween() {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();

		return tradingRepository.countByCreateDateBetween(monthStart, now);
	}

	public Map<String, Object> findByCreateDateBetweenAndSumPrice() {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();

		return tradingRepository.findByCreateDateBetweenAndSumPrice(monthStart, now);
	}

	public List<Object> recentSummary() {

		List<Object> returnList = new ArrayList<Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();

		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DATE, -7);
		Date beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		Date nextDay = calendar.getTime();

		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		dataMap = new HashMap<String, Object>();

		beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		nextDay = calendar.getTime();
		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		dataMap = new HashMap<String, Object>();

		beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		nextDay = calendar.getTime();
		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		dataMap = new HashMap<String, Object>();

		beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		nextDay = calendar.getTime();
		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		dataMap = new HashMap<String, Object>();

		beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		nextDay = calendar.getTime();
		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		dataMap = new HashMap<String, Object>();

		beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		nextDay = calendar.getTime();
		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		dataMap = new HashMap<String, Object>();

		beforeDay = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		nextDay = calendar.getTime();
		dataMap.put("date", DateUtils.changeDateToString(beforeDay, "yyyy-MM-dd"));
		dataMap.put("tradingCount",
				tradingRepository.findByCreateDateBetweenAndGroupByUserId(beforeDay, nextDay).size());
		dataMap.put("pinCodeCount", pinCodeRepository.countByCreateDateBetween(beforeDay, nextDay));
		dataMap.putAll(tradingRepository.findByCreateDateBetweenAndSumPrice(beforeDay, nextDay));
		returnList.add(dataMap);

		return returnList;
	}

	public Trading calcFee(Trading trading) {
		String company = "";
		if (trading.getCompany().equals("culture")) {
			company = "컬처랜드";
		} else {
			company = "해피머니";
		}
		Fee fee = feeService.findByCompany(company);

		BigDecimal decimalFee = new BigDecimal(fee.getPurchaseFeePercents() * 0.01).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal decimalWaiverAmount = new BigDecimal(fee.getWaiverAmount());

		if (trading.getComepletePrice() > 0) {
			BigDecimal decimalPrice = new BigDecimal(trading.getComepletePrice());
			BigDecimal decimalCalcFee = decimalPrice.multiply(decimalFee).setScale(0, RoundingMode.HALF_EVEN);

			trading.setPurchaseFee(String.valueOf(decimalCalcFee));
			
			if (decimalPrice.compareTo(decimalWaiverAmount) < 0) {
				decimalCalcFee = decimalCalcFee.add(new BigDecimal(fee.getTransperFees()));
				trading.setTransferFee(String.valueOf(fee.getTransperFees()));
			}

			int intCalPrice = decimalPrice.subtract(decimalCalcFee).intValue();

			trading.setFees(String.valueOf(decimalCalcFee));
			trading.setComepletePrice(intCalPrice);
		} else {
			trading.setFees("0");
			trading.setComepletePrice(0);
		}

		return trading;
	}

	public List<Trading> transferAllWithdrawFailTrading() throws Exception {


		List<Trading> tradingList = tradingRepository.findByStatus(TradingStatusEnum.WITHDRAWFAIL.getValue());


		for (Trading trading : tradingList) {
			trading = bankService.pay(trading);
		}

		return tradingList;
	}

	public void insertIntoHistoryTradingCreateDateBetween() {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -3);
		Date threeDayAgo = cal.getTime();
		cal.add(Calendar.DATE, -1);
		Date fourDayAgo = cal.getTime();

		List<Trading> tradingList = tradingRepository.findByCreateDateBetween(fourDayAgo, threeDayAgo);
		List<HistoryTrading> historyTradingList = new ArrayList<HistoryTrading>();
		List<HistoryPinCode> historyPinCodeList = new ArrayList<HistoryPinCode>();

		for (Trading trading : tradingList) {
			HistoryTrading historyTrading = new HistoryTrading();

			historyTrading.setIdx(trading.getIdx());
			historyTrading.setComepletePrice(trading.getComepletePrice());
			historyTrading.setCompany(trading.getCompany());
			historyTrading.setCreateDate(trading.getCreateDate());
			historyTrading.setFees(trading.getFees());
			historyTrading.setMessage(trading.getMessage());
			historyTrading.setPinCompleteDate(trading.getPinCompleteDate());
			historyTrading.setPurchaseFeePercents(trading.getPurchaseFeePercents());
			historyTrading.setRequestPrice(trading.getRequestPrice());
			historyTrading.setStatus(trading.getStatus());
			historyTrading.setTel(trading.getTel());
			historyTrading.setUpdateDate(trading.getUpdateDate());
			historyTrading.setUserId(trading.getUserId());
			historyTrading.setUserName(trading.getUserName());
			historyTrading.setWithdrawCompleteDate(trading.getWithdrawCompleteDate());
			historyTrading.setWithdrawStatus(trading.getWithdrawStatus());

			historyTradingList.add(historyTrading);

			List<PinCode> pinCodeList = pinService.findByTradingIdx(trading.getIdx());

			for (PinCode pinCode : pinCodeList) {
				HistoryPinCode historyPinCode = new HistoryPinCode();
				historyPinCode.setCompany(pinCode.getCompany());
				historyPinCode.setCreateDate(pinCode.getCreateDate());
				historyPinCode.setIdx(pinCode.getIdx());
				historyPinCode.setMessage(pinCode.getMessage());
				historyPinCode.setPinCode(pinCode.getPinCode());
				historyPinCode.setPrice(pinCode.getPrice());
				historyPinCode.setStatus(pinCode.getStatus());
				historyPinCode.setTradingIdx(pinCode.getTradingIdx());
				historyPinCode.setUpdateDate(pinCode.getUpdateDate());

				historyPinCodeList.add(historyPinCode);
			}
		}

		pinService.saveHistoryList(historyPinCodeList);
		historyTradingRepository.saveAll(historyTradingList);
	}

	public void deleteTradingCreateDateBefore() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -30);
		Date thrtyDaysAgo = cal.getTime();

		List<Trading> tradingList = tradingRepository.findByCreateDateBefore(thrtyDaysAgo);

		for (Trading trading : tradingList) {

			List<PinCode> pinCodeList = pinService.findByTradingIdx(trading.getIdx());

			pinCodeRepository.deleteInBatch(pinCodeList);

		}

		tradingRepository.deleteInBatch(tradingList);
	}

	public void selectTradingAccountInfo(List<Trading> tradingList) {

		for (Trading trading : tradingList) {
			Users user = usersService.findByUserId(trading.getUserId());

			trading.setAccountName(user.getAccountName());
			trading.setAccountNum(user.getAccountNum());

		}
	}

	public List<Trading> findByCreateDateBefore(Date date) {
		return tradingRepository.findByCreateDateBefore(date);
	}
	
	
	public long countByUserId(String userId) {
		return tradingRepository.countByUserId(userId);
	}
	public long countByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId) {
		return tradingRepository.countByCreateDateBetweenAndUserId(fromDate, toDate, userId);
	}
	
	public Map<String, Object> findByUserIdAndSumPrice(String userId){
		return tradingRepository.findByUserIdAndSumPrice(userId);
	}
	
	public Map<String, Object> findByCreateDateBetweenAndUserIdAndSumPrice(Date fromDate, Date toDate, String userId){
		return tradingRepository.findByCreateDateBetweenAndUserIdAndSumPrice(fromDate, toDate, userId);
	}

}
