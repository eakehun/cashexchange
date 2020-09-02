package com.hourfun.cashexchange.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryResponse;
import com.hourfun.cashexchange.model.OneToOneInquiryType;
import com.hourfun.cashexchange.repository.OneToOneInquiryResponseRepository;

@Service
public class OneToOneInquiryReponseService {

	@Autowired
	private OneToOneInquiryResponseRepository oneToOneInquiryResponseRepository;
	
	@Autowired
	private OneToOneInquiryService oneToOneInquiryService;
	
	@Transactional
	public OneToOneInquiryResponse save(OneToOneInquiryResponse oneToOneInquiryResponse, long parentIdx) {
		if(oneToOneInquiryResponse.getIdx() > 0) {
			return oneToOneInquiryResponseRepository.save(oneToOneInquiryResponse);
		}else {
			Optional<OneToOneInquiry> oneToOneInquiryOptional =  oneToOneInquiryService.findById(parentIdx);
			oneToOneInquiryResponse =  oneToOneInquiryResponseRepository.save(oneToOneInquiryResponse);
			if(oneToOneInquiryOptional.isPresent()) {
				OneToOneInquiry oneToOneInquiry = oneToOneInquiryOptional.get();
				oneToOneInquiry.setStatus(OneToOneInquiryType.Response_Complate);
				oneToOneInquiry.setResponseDate(new Date());
				oneToOneInquiryService.save(oneToOneInquiry);
			}
		}
		return oneToOneInquiryResponse;
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
}
