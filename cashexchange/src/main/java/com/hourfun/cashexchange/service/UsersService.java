package com.hourfun.cashexchange.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.Users;
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

	@SuppressWarnings("unchecked")
	public Users customLogin(String id, String pwd, AuthEnum common, HttpServletRequest request,
			HttpServletResponse response) {
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
			}else {
				throw new IllegalArgumentException("login authority not correct. Please check ..");
			}

		}else {
			throw new IllegalArgumentException("account doesn't exists. Please check ..");
		}		

	}

	public Users findByTel(String tel) {

		Users selectUser = repository.findByTel(tel);

		if (selectUser != null) {
			String maskedEmail = StringUtil.getMaskedEmail(selectUser.getUserId());

			selectUser.setUserId(maskedEmail);
			selectUser.setEmail(maskedEmail);
			
			return selectUser;
		}else {
			throw new IllegalArgumentException("account doesn't exists. Please check ..");
		}

		
	}

	public Users findByUserIdAndTel(String id, String tel) {
		
		Users selectUser = repository.findByUserIdAndTel(id, tel);
		
		if (selectUser != null) {
			return selectUser;
		}else {
			throw new IllegalArgumentException("account doesn't exists. Please check ..");
		}
		
	}

	public Users signIn(Users users, AuthEnum common) {

		String auth = common.name().split("_")[1];

		users.setAuth(auth);
		users.setTelChkValue("T");
		users.setPwd(customPasswordEncoder.encode(users.getPwd()));

		return repository.save(users);
	}

//	public Page<Users> findAllUsers(Pageable pageable){
//		return repository.findAll(pageable);
//	}
	
	public Page<Users> findByCreateDateBetween(String fromDate, String toDate, Pageable pageable) {
		return repository.findByCreateDateBetween(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
	}

	public Page<Users> findByCreateDateBetweenAndAccountStatus(String fromDate, String toDate, String accountStatus, Pageable pageable) {
		return repository.findByCreateDateBetweenAndAccountStatus(
				DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), accountStatus, pageable);
	}

}
