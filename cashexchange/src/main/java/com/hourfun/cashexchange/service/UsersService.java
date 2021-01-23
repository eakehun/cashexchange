package com.hourfun.cashexchange.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.hourfun.cashexchange.common.AccountStatusEnum;
import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.common.CacheKey;
import com.hourfun.cashexchange.common.TradingStatusEnum;
import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.AgreementRepository;
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

				return repository.findByUserId(id);
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

//	@Cacheable(value = "users", key="#p0")
//	public Users findByUserIdWithoutAgreement(String id) {
//		return repository.findByUserIdWithoutAgreement(id);
//	}

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

		users.setAuth(auth);
		users.setTelChkValue("T");
		users.setPwd(customPasswordEncoder.encode(users.getPwd()));
		users.setAccountStatus(AccountStatusEnum.NORMAL.getValue());

		try {

			List<Agreement> agreementList = new ArrayList<Agreement>();
			;

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
			// TODO: handle exception
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

	public Page<Users> findByCreateDateBetweenAndName(String fromDate, String toDate, String name, Pageable pageable) {
		return repository.findByCreateDateBetweenAndName(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), name, pageable);
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

	public Users findByIdx(long idx) {
		Users selectUser = repository.findByIdx(idx);

//		History history = historyService.selectLastLoginHistory(selectUser.getUserId());
//		if (history != null) {
//			selectUser.setLastLogin(history.getCreateDate());
//			selectUser.setLastDevice(history.getDevice());
//		}

		selectLoginHistory(selectUser);

		return selectUser;
	}

	@CachePut(value = "users", key = "#p0")
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

	@CachePut(value = "users", key = "#p0")
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

	@CachePut(value = "users", key = "#p0")
	public Users secede(String userId) {

		Users selectUser = findByUserId(userId);

		List<Trading> tradingList = tradingService.findByUserIdAndWithdrawStatusNot(userId,
				TradingStatusEnum.COMPLETE.getValue());

		if (tradingList.size() > 0) {
			throw new IllegalArgumentException("not complete trading remain");
		}

		selectUser.setAccountStatus(AccountStatusEnum.WITHDRAW.getValue());

		return repository.save(selectUser);

	}

	@CachePut(value = "users", key = "#p0")
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

	@CachePut(value = "users", key = "#p0")
	public Users updateAccount(String userId, Users users) {
		Users selectUser = findByUserId(userId);

		selectUser.setAccountCode(users.getAccountCode());
		selectUser.setAccountName(users.getAccountName());
		selectUser.setAccountNum(users.getAccountNum());

		return repository.save(selectUser);

//		if(selectUser.getCi().equals(users.getCi())) {
//			selectUser.setMobileOperator(users.getMobileOperator());
//			selectUser.setTel(users.getTel());
//			
//			return repository.save(selectUser);
//		}else {
//			throw new IllegalArgumentException("CI not match");
//		}

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
		History history = historyService.selectLastLoginHistory(users.getUserId());
		if (history != null) {
			users.setLastLogin(history.getCreateDate());
			users.setLastDevice(history.getDevice());
		}
	}

}
