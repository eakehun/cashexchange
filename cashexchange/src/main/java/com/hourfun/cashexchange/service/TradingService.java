package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.Fee;
import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.PinCodeRepository;
import com.hourfun.cashexchange.repository.TradingRepository;
import com.hourfun.cashexchange.repository.UsersRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class TradingService {

	@Autowired
	private TradingRepository tradingRepository;

	@Autowired
	private PinCodeRepository pinCodeRepository;

	@Autowired
	private UsersService UsersService;

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

	public Trading save(String userId, String company, List<String> pinCodes) {

		try {
			if (pinCodes == null || pinCodes.size() == 0) {
				throw new IllegalArgumentException("pinCodes null");
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

			Users user = UsersService.findByUserId(userId);

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

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetween(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndUserId(String dateCategory, String fromDate, String toDate, String userId,
			Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetweenAndUserId(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndStatus(String dateCategory, String fromDate, String toDate, String status,
			Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetweenAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), status, pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndUserName(String dateCategory, String fromDate, String toDate,
			String userName, Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetweenAndUserName(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndIdx(String dateCategory, String fromDate, String toDate, long idx,
			Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetweenAndIdx(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndIdxAndStatus(String dateCategory, String fromDate, String toDate, Long idx,
			String status, Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetweenAndIdxAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), idx, status, pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndUserIdAndStatus(String dateCategory, String fromDate, String toDate,
			String userId, String status, Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByPinCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		} else {
			return tradingRepository.findByWithdrawCompleteDateBetweenAndUserIdAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, status, pageable);
		}

	}

	public Page<Trading> findByDateBetweenAndUserNameAndStatus(String dateCategory, String fromDate, String toDate,
			String userName, String status, Pageable pageable) {

		if (dateCategory.equals("createDate")) {
			return tradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		} else if (dateCategory.equals("pinCompleteDate")) {
			return tradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		} else {
			return tradingRepository.findByCreateDateBetweenAndUserNameAndStatus(
					DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userName, status, pageable);
		}

	}

	public File excelDownload(String fromDate, String toDate) {

		List<Trading> selectList = tradingRepository.findByCreateDateBetween(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));

		StringBuilder content = new StringBuilder();

		content.append("idx").append(",").append("userId").append(",").append("userName").append(",").append("company")
				.append(",").append("status").append(",").append("withdrawStatus").append(",").append("requestPrice")
				.append(",").append("fees").append(",").append("comepletePrice").append(",").append("createDate")
				.append(",").append("pinCompleteDate").append(",").append("withdrawCompleteDate").append("\n");

		for (Trading trading : selectList) {
			content.append(trading.getIdx()).append(",").append(trading.getUserId()).append(",")
					.append(trading.getUserName()).append(",").append(trading.getCompany()).append(",")
					.append(trading.getStatus()).append(",").append(trading.getWithdrawStatus()).append(",")
					.append(trading.getRequestPrice()).append(",").append(trading.getFees()).append(",")
					.append(trading.getComepletePrice()).append(",").append(trading.getCreateDate()).append(",")
					.append(trading.getPinCompleteDate()).append(",").append(trading.getWithdrawCompleteDate())
					.append("\n");
		}

		File file = null;
		PrintWriter pw = null;

		try {
			file = new File("trading_" + fromDate + "_" + toDate + ".csv");
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

	public Trading update(Trading trading) {
		return tradingRepository.save(trading);
	}

	public Page<Trading> findByCreateDateBetweenMasking(Pageable pageable) {

		Date now = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, -7);

		Date tomorrow = cal.getTime();

		Page<Trading> tradings = tradingRepository.findByCreateDateBetween(tomorrow, now, pageable);

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

		if (trading.getComepletePrice() > 0) {
			BigDecimal decimalPrice = new BigDecimal(trading.getComepletePrice());
			BigDecimal decimalCalcFee = decimalPrice.multiply(decimalFee).setScale(0, RoundingMode.HALF_EVEN);
			decimalCalcFee = decimalCalcFee.add(new BigDecimal(fee.getTransperFees()));

			int intCalPrice = decimalPrice.subtract(decimalCalcFee).intValue();
			trading.setFees(String.valueOf(decimalCalcFee));
			trading.setComepletePrice(intCalPrice);
		} else {
			trading.setFees("0");
			trading.setComepletePrice(0);
		}

		return trading;
	}

}
