package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.common.AccountStatusEnum;
import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.common.CacheKey;
import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.AgreementRepository;
import com.hourfun.cashexchange.repository.TradingRepository;
import com.hourfun.cashexchange.repository.UsersRepository;
import com.hourfun.cashexchange.util.DateUtils;
import com.hourfun.cashexchange.util.StringUtil;

@Service
public class UsersService {

	@Autowired
	private UsersRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder customPasswordEncoder;

	@Autowired
	private RememberMeServices customSecurityRememberMeService;

	@Autowired
	private MailService mailService;

	@Autowired
	private TradingService tradingService;

	@Autowired
	private AgreementRepository agreementRepository;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private TradingRepository tradingRepository;

	@Value("${trading.limit.month}")
	private String monthLimit;

	@SuppressWarnings("unchecked")
	public Users customLogin(String id, String pwd, AuthEnum common, HttpServletRequest request,
			HttpServletResponse response) throws BadCredentialsException, UsernameNotFoundException {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, pwd);

		Authentication authentication = authenticationManager.authenticate(token);

		if (authentication != null) {
			HttpSession session = request.getSession();

			SecurityContextHolder.getContext().setAuthentication(authentication);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
			List<GrantedAuthority> currentAuth = (List<GrantedAuthority>) authentication.getAuthorities();

			if (currentAuth.get(0).getAuthority().equals(common.name())) {

				customSecurityRememberMeService.loginSuccess(request, response, authentication);

				Users selectUser = findByUserId(id);
				
				String currentAccountStatus = selectUser.getAccountStatus();

				if (currentAccountStatus != null) {
					if (currentAccountStatus.equals(AccountStatusEnum.SUSPENDED.getValue())) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "suspended user");
					} else if (currentAccountStatus.equals(AccountStatusEnum.WITHDRAW.getValue())) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "withdraw user");
					}
				}
				
				return findByUserId(id);
				
			} else {
				throw new IllegalArgumentException("login authority not correct. Please check authority");
			}

		} else {
			throw new IllegalArgumentException("account doesn't exists. Please check userid, password");
		}

	}

	public Users findByTel(String tel) {

		Users selectUser = repository.findByTel(tel);

		if (selectUser != null) {
			String maskedEmail = StringUtil.getMaskedEmail(selectUser.getUserId());

			selectUser.setUserId(maskedEmail);
			selectUser.setEmail(maskedEmail);

			return selectUser;
		} else {
			throw new IllegalArgumentException("account doesn't exists. Please check tel");
		}

	}

	public String findByCi(String ci) {

		Users selectUser = repository.findByCi(ci);

		if (selectUser != null) {
			throw new IllegalArgumentException("Users signIn error = ci duplicate");
		} else {
			return "ok";
		}

	}

	public String checkEmailDuplicate(String id) {
		Users user = findByUserId(id);

		if (user != null) {
			throw new IllegalArgumentException("email duplicate");
		} else {
			return id;
		}
	}

	public Users findByUserIdAndTel(String id, String tel) {

		Users selectUser = repository.findByUserIdAndTel(id, tel);

		if (selectUser != null) {
			return selectUser;
		} else {
			throw new IllegalArgumentException("account doesn't exists. Please check userid");
		}

	}

	public Users signIn(Users users, AuthEnum common) throws Exception {

		String auth = common.name().split("_")[1];

		if (repository.findByCi(users.getCi()) != null) {
			throw new Exception("Users signIn error = ci duplicate");
		}

		users.setAuth(auth);
		users.setTelChkValue("T");
		users.setPwd(customPasswordEncoder.encode(users.getPwd()));
		users.setAccountStatus(AccountStatusEnum.NORMAL.getValue());

		try {
			List<Agreement> agreementList = new ArrayList<Agreement>();

			for (Agreement agreement : users.getAgreements()) {
				agreementList.add(agreementRepository.getOne(agreement.getIdx()));
			}

			users.setAgreements(null);

			Users savedUser = repository.save(users);

			mailService.welcomeMailSend(savedUser);

			savedUser.setAgreements(agreementList);
			savedUser = repository.save(savedUser);

			return savedUser;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Users signIn error = " + e.getMessage());
		}

	}

	public Page<Users> findByCreateDateBetween(String fromDate, String toDate, Pageable pageable) {

		Page<Users> returnVal = repository.findByCreateDateBetween(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetween(String fromDate, String toDate) {

		List<Users> returnVal = repository.findByCreateDateBetween(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"));

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndAccountStatus(String fromDate, String toDate, String accountStatus,
			Pageable pageable) {

		Page<Users> returnVal = repository.findByCreateDateBetweenAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), accountStatus, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndAccountStatus(String fromDate, String toDate, String accountStatus) {

		List<Users> returnVal = repository.findByCreateDateBetweenAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), accountStatus);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndUserId(String fromDate, String toDate, String userId,
			Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndUserId(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndUserId(String fromDate, String toDate, String userId) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndUserId(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndName(String fromDate, String toDate, String name, Pageable pageable) {

		Page<Users> returnVal = repository.findByCreateDateBetweenAndName(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), name, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndName(String fromDate, String toDate, String name) {

		List<Users> returnVal = repository.findByCreateDateBetweenAndName(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), name);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndTel(String fromDate, String toDate, String tel, Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndTel(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), tel, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndTel(String fromDate, String toDate, String tel) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndTel(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), tel);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndIdx(String fromDate, String toDate, String idx, Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndIdx(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), Long.valueOf(idx), pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndIdx(String fromDate, String toDate, String idx) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndIdx(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), Long.valueOf(idx));

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndUserIdAndAccountStatus(String fromDate, String toDate, String userId,
			String accountStatus, Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndUserIdAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, accountStatus, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndUserIdAndAccountStatus(String fromDate, String toDate, String userId,
			String accountStatus) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndUserIdAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), userId, accountStatus);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndNameAndAccountStatus(String fromDate, String toDate, String name,
			String accountStatus, Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndNameAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), name, accountStatus, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndNameAndAccountStatus(String fromDate, String toDate, String name,
			String accountStatus) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndNameAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), name, accountStatus);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndTelAndAccountStatus(String fromDate, String toDate, String tel,
			String accountStatus, Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndTelAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), tel, accountStatus, pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndTelAndAccountStatus(String fromDate, String toDate, String tel,
			String accountStatus) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndTelAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), tel, accountStatus);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Page<Users> findByCreateDateBetweenAndIdxAndAccountStatus(String fromDate, String toDate, String idx,
			String accountStatus, Pageable pageable) {
		Page<Users> returnVal = repository.findByCreateDateBetweenAndIdxAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), Long.valueOf(idx), accountStatus,
				pageable);

		List<Users> list = returnVal.getContent();

		for (Users users : list) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public List<Users> findByCreateDateBetweenAndIdxAndAccountStatus(String fromDate, String toDate, String idx,
			String accountStatus) {
		List<Users> returnVal = repository.findByCreateDateBetweenAndIdxAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), Long.valueOf(idx), accountStatus);

		for (Users users : returnVal) {
			selectLoginHistory(users);
		}

		return returnVal;
	}

	public Users findByIdx(long idx) {
		Users selectUser = repository.findByIdx(idx);

		selectLoginHistory(selectUser);
		selectUserTradingCompleteInfo(selectUser);

		return selectUser;
	}

	@CachePut(value = CacheKey.USER, cacheManager = "cacheManager")
	public Users updateAccountStatus(Users users) {
		Users selectUser = repository.findByIdx(users.getIdx());

		String accountStatus = users.getAccountStatus();

		Date now = new Date();

		if (accountStatus.equals(AccountStatusEnum.NORMAL)) {
			selectUser.setWithdrawDate(null);
			selectUser.setSuspendedDate(null);
		} else if (accountStatus.equals(AccountStatusEnum.SUSPENDED.getValue())) {
			selectUser.setWithdrawDate(null);
			selectUser.setSuspendedDate(now);
		} else if (accountStatus.equals(AccountStatusEnum.WITHDRAW.getValue())) {
			selectUser.setWithdrawDate(now);
			selectUser.setSuspendedDate(null);
		}

		selectUser.setAccountStatus(accountStatus);

		return repository.save(selectUser);
	}

	@CachePut(value = CacheKey.USER, cacheManager = "cacheManager")
	public Users updateAccountPassword(Authentication auth, Users users) {
		Users selectUser = null;
		if (auth != null) {
			selectUser = findByUserId(auth.getName());
		} else {
			selectUser = findByUserId(users.getUserId());
			if (!selectUser.getCi().equals(users.getCi())) {
				throw new IllegalArgumentException("CI not match");
			}
		}

		selectUser.setPwd(customPasswordEncoder.encode(users.getPwd()));

		return repository.save(selectUser);
	}

	@CacheEvict(value = CacheKey.USER, cacheManager = "cacheManager")
	public Users secede(String userId) {

		Users selectUser = findByUserId(userId);

		List<Trading> progressList = tradingService.findByUserIdAndWithdrawStatus(userId,
				TradingStatusEnum.PROGRESS.getValue());
		List<Trading> withdrawFailList = tradingService.findByUserIdAndWithdrawStatus(userId,
				TradingStatusEnum.WITHDRAWFAIL.getValue());
		List<Trading> noBalanceList = tradingService.findByUserIdAndWithdrawStatus(userId,
				TradingStatusEnum.NO_BALANCE.getValue());

		if (progressList.size() > 0 || withdrawFailList.size() > 0 || noBalanceList.size() > 0) {
			throw new IllegalArgumentException("not complete trading remain");
		}

		selectUser.setAccountStatus(AccountStatusEnum.WITHDRAW.getValue());
		selectUser.setWithdrawDate(new Date());

		return repository.save(selectUser);

	}

	@CachePut(value = CacheKey.USER, cacheManager = "cacheManager")
	public Users updatePhone(String userId, Users users) {
		Users selectUser = findByUserId(userId);

		if (selectUser.getCi().equals(users.getCi())) {
			selectUser.setMobileOperator(users.getMobileOperator());
			selectUser.setTel(users.getTel());

			return repository.save(selectUser);
		} else {
			throw new IllegalArgumentException("CI not match");
		}

	}

	@CachePut(value = CacheKey.USER, cacheManager = "cacheManager")
	public Users updateAccount(String userId, Users users) {
		Users selectUser = findByUserId(userId);

		selectUser.setAccountCode(users.getAccountCode());
		selectUser.setAccountName(users.getAccountName());
		selectUser.setAccountNum(users.getAccountNum());

		return repository.save(selectUser);

	}

	@Cacheable(value = CacheKey.USER, cacheManager = "cacheManager")
	public Users findByUserId(String userId) {
		return repository.findByUserId(userId);
	}

	public List<Agreement> findAgreementByUserId(String userId) {
		Users selectUser = findByUserId(userId);
		return agreementRepository.findAllByMemberIdx(selectUser.getIdx());
	}

	public void selectLoginHistory(Users users) {
		users.setAgreementList(findAgreementByUserId(users.getUserId()));
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();
		Integer monthCompletePrice = tradingRepository.findCompletePriceByCreateDateBetween(users.getUserId(),
				monthStart, now);
		if (monthCompletePrice == null) {
			monthCompletePrice = 0;
		}
		users.setLimitOver(Integer.valueOf(monthLimit) < monthCompletePrice);

		History history = historyService.selectLastLoginHistory(users.getUserId());
		if (history != null) {
			users.setLastLogin(history.getCreateDate());
			users.setLastDevice(history.getDevice());
		}
	}

	public File excelDownload(List<Users> selectList, String fromDate, String toDate) {

		StringBuilder content = new StringBuilder();

		content.append("번호").append(",").append("사용자 아이디").append(",").append("사용자 이름").append(",").append("전화번호")
				.append(",").append("가입일").append(",").append("계정 상태").append(",").append("한도초과")
				.append(",").append("탈퇴일").append(",").append("정지일").append(",").append("약관 동의 목록")
				.append(",").append("은행명").append(",").append("계좌번호").append(",").append("마지막 접속 장치")
				.append(",").append("마지막 접속일").append("\n");

		for (Users users : selectList) {
			List<Agreement> agreements = (List<Agreement>) users.getAgreementList();
			String agreementString = "";

			for (Agreement agreement : agreements) {
				agreementString = agreementString + agreement.getTitle() + "/";
			}
			if (agreementString != "") {
				agreementString = agreementString.substring(0, agreementString.length() - 1);
			}

			content.append(users.getIdx()).append(",").append(users.getUserId()).append(",").append(users.getName())
					.append(",").append(users.getTel()).append(",").append(users.getCreateDate()).append(",")
					.append(users.getAccountStatus()).append(",").append(users.getLimitOver()).append(",")
					.append(users.getWithdrawDate()).append(",").append(users.getSuspendedDate()).append(",")
					.append(agreementString).append(",").append(users.getAccountName()).append(",")
					.append(users.getLastDevice()).append(",").append(users.getLastLogin()).append("\n");
		}

		File file = null;
		PrintWriter pw = null;

		fromDate = fromDate.substring(0, 10);
		toDate = toDate.substring(0, 10);

		try {
			file = new File("users " + fromDate + " " + toDate + ".csv");
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

	public void selectUserTradingCompleteInfo(Users users) {

		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date monthStart = calendar.getTime();

		long totalTradingCount = tradingService.countByUserId(users.getUserId());
		Map<String, Object> totalCompletePrice = tradingService.findByUserIdAndSumPrice(users.getUserId());

		long monthTradingCount = tradingService.countByCreateDateBetweenAndUserId(monthStart, now, users.getUserId());
		Map<String, Object> monthCompletePrice =  tradingService.findByCreateDateBetweenAndUserIdAndSumPrice(monthStart, now, users.getUserId());

		users.setTotalTradingCount(totalTradingCount);
		if(totalCompletePrice.get("complete_price") != null) {
			users.setTotalCompletePrice(String.valueOf(totalCompletePrice.get("complete_price")));
		}else {
			users.setTotalCompletePrice("0");
		}
		
		users.setMonthTradingCount(monthTradingCount);
		if(monthCompletePrice.get("complete_price") != null) {
			users.setMonthCompletePrice(String.valueOf(monthCompletePrice.get("complete_price")));
		}else {
			users.setMonthCompletePrice("0");
		}
		
	}

}
