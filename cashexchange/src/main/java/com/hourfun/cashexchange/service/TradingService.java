package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.TradingRepository;
import com.hourfun.cashexchange.repository.UsersRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class TradingService {

	@Autowired
	private TradingRepository tradingRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PinService pinService;

	public Trading save(String userId, String company, List<String> pinCodes) {

		try {
			Users user = usersRepository.findByUserId(userId);
			
			Trading trading = new Trading();
			trading.setUserId(user.getUserId());
			trading.setCompany(company);
			trading.setUserName(user.getName());
			trading.setStatus(TradingStatusEnum.PROGRESS.getValue());
			trading.setWithdrawStatus(TradingStatusEnum.PROGRESS.getValue());
			trading.setRequestPrice(0);
			trading.setComepletePrice(0);
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
			return tradingRepository.findByIdx(idx);
		} else {
			throw new IllegalArgumentException("권한이 없습니다.");
		}
	}

	public Page<Trading> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId,
			Pageable pageable) {
		return tradingRepository.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, pageable);
	}

	public Page<Trading> findByCreateDateBetween(String fromDate, String toDate, Pageable pageable) {
		return tradingRepository.findByCreateDateBetween(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
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
        cal.add(Calendar.MONTH, -3);
        
		
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
			trading.setUserName(replaceString);
		}

	}

}
