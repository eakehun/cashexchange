package com.hourfun.cashexchange.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryResponse;
import com.hourfun.cashexchange.model.OneToOneInquiryType;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.OneToOneInquiryResponseRepository;

@Service
public class OneToOneInquiryReponseService {

	@Autowired
	private OneToOneInquiryResponseRepository oneToOneInquiryResponseRepository;
	
	@Autowired
	private OneToOneInquiryService oneToOneInquiryService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private MailService mailService;
	
	@Transactional
	public OneToOneInquiryResponse save(OneToOneInquiryResponse oneToOneInquiryResponse, long parentIdx, Authentication auth) throws MessagingException {
		Users users = usersService.findByUserId(auth.getName());
		oneToOneInquiryResponse.setUserId(users.getUserId());
		Optional<OneToOneInquiry> oneToOneInquiryOptional =  oneToOneInquiryService.findById(parentIdx);
		if(oneToOneInquiryOptional.isPresent()) {
			OneToOneInquiry oneToOneInquiry = oneToOneInquiryOptional.get();
			
			oneToOneInquiryResponse.setOneToOneInquiry(oneToOneInquiry);
			if(oneToOneInquiryResponse.getIdx() < 1l) {
				Date date = new Date();
				//mail 보내기 기능 추가
				if(users.getEmail() != null && !users.getEmail().equals("")) {
					Users clinetUser = usersService.findByUserId(oneToOneInquiry.getUserId());
					
					mailService.ontToOneInquirySend(clinetUser, date);
				}
				
				
				oneToOneInquiry.setStatus(OneToOneInquiryType.Response_Complate);
				oneToOneInquiry.setResponseDate(date);
				oneToOneInquiryService.saveComplete(oneToOneInquiry);
			}
			return oneToOneInquiryResponseRepository.save(oneToOneInquiryResponse);
		}else {
			throw new IllegalArgumentException("There are no registered comments...");
		}
	}
	public Map<String,Object> oneToOneInquiryDetail(long idx){
		Optional<OneToOneInquiry> oneInquiry = oneToOneInquiryService.findById(idx);
		Map<String,Object> returnVal = null;
		if(oneInquiry.isPresent()) {
			returnVal = new HashMap<>();
			List<OneToOneInquiryResponse> oneToOneInquiryResponses = oneToOneInquiryResponseRepository.findByParentIdx(idx);
			returnVal.put("OneToOne", oneInquiry.get());
			returnVal.put("oneToOneResponses", oneToOneInquiryResponses);
		}else {
			throw new IllegalArgumentException("Idx value doesn't exists. Please check ..");
		}
		return returnVal;
	}
	
	public List<OneToOneInquiryResponse> findByParentIdx(long idx){
		return oneToOneInquiryResponseRepository.findByParentIdx(idx);
	}
}
