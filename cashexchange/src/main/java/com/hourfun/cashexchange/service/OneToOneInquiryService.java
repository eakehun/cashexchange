package com.hourfun.cashexchange.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryType;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.OneToOneInquiryRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class OneToOneInquiryService {

	@Autowired
	private OneToOneInquiryRepository oneToOneInquiryRepository;
	
	@Autowired
	private UsersService usersService;
	
	public OneToOneInquiry save(OneToOneInquiry oneInquiry, Authentication auth) {
		if(oneInquiry.getIdx() < 1l) {
			oneInquiry.setStatus(OneToOneInquiryType.Ready);
		}
		Users users = usersService.findByUserId(auth.getName());
		return oneToOneInquiryRepository.save(oneInquiry);
	}
	
	public Optional<OneToOneInquiry> findById(long idx){
		return oneToOneInquiryRepository.findById(idx);
	}
	
	public Page<OneToOneInquiry> findByCreateDateBetweenAndUserId(String fromDateStr,String toDateStr, 
			String userId, Pageable pageable){
		
		return oneToOneInquiryRepository.findByCreateDateBetweenAndUserId(DateUtils.changeStringToDate(fromDateStr, "yyyy-MM-dd HH:mm:ss")
				,DateUtils.changeStringToDate(toDateStr, "yyyy-MM-dd HH:mm:ss"),userId,pageable);
	}
	
	public Page<OneToOneInquiry> findByCreateDateBetweenAndUserIdAndStatus(String fromDate, String toDate, 
			String userId,OneToOneInquiryType status, Pageable pageable){
		return oneToOneInquiryRepository.findByCreateDateBetweenAndUserIdAndStatus(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),userId,status,pageable);
	}
	
	public Page<OneToOneInquiry> findByCreateDateBetweenAndTitleLikeAndStatus(String fromDate, 
			String toDate, String title,OneToOneInquiryType status, Pageable pageable){
		return oneToOneInquiryRepository.findByCreateDateBetweenAndTitleLikeAndStatus(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),title,status,pageable);
	}
	
	public Page<OneToOneInquiry> findByCreateDateBetweenAndTitleLike(String fromDate, 
			String toDate, String title, Pageable pageable){
		return oneToOneInquiryRepository.findByCreateDateBetweenAndTitleLike(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),title,pageable);
	}
}
