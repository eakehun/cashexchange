package com.hourfun.cashexchange.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryResponse;
import com.hourfun.cashexchange.model.OneToOneInquiryType;
import com.hourfun.cashexchange.service.OneToOneInquiryReponseService;
import com.hourfun.cashexchange.service.OneToOneInquiryService;

@RestController
@RequestMapping("/admin/board/OneToOne")
public class AdminOneToOneInquiryController {
	
	@Autowired
	private OneToOneInquiryService oneToOneInquiryService;
	
	@Autowired
	private OneToOneInquiryReponseService oneToOneInquiryResponseService;
	
	@RequestMapping(value = "/response/parentIdx/{parentIdx}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> oneToOneInquiryDetail(@PathVariable long parentIdx) {
		if(parentIdx < 1l) {
			throw new IllegalArgumentException("Idx value doesn't exists. Please check ..");
		}
		
        return new ResponseEntity<>(oneToOneInquiryResponseService.oneToOneInquiryDetail(parentIdx), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/response/parentIdx/{parentIdx}/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OneToOneInquiryResponse> insertOneToOneResponse(@PathVariable long parentIdx, 
    		@RequestBody OneToOneInquiryResponse oneInquiryResponse,  Authentication auth) {
		if(oneInquiryResponse.getIdx() >0l) {
			throw new IllegalArgumentException("Idx value exists. Please check ..");
		}
		OneToOneInquiryResponse result = oneToOneInquiryResponseService.save(oneInquiryResponse,parentIdx,auth);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/response/", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<OneToOneInquiryResponse> updateOneToOneResponse(@RequestBody OneToOneInquiryResponse oneInquiryResponse,  Authentication auth) {
		if(oneInquiryResponse.getIdx() < 1l) {
			throw new IllegalArgumentException("Idx value doesn't exists. Please check ..");
		}
		OneToOneInquiryResponse result = oneToOneInquiryResponseService.save(oneInquiryResponse,0, auth);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<OneToOneInquiry>> findByCreateDateBetweenAndUserId(@PathVariable String fromDate, @PathVariable String toDate, 
    		@PathVariable String userId, Pageable pageable) {
        return new ResponseEntity<>(oneToOneInquiryService.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, pageable), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<OneToOneInquiry>> findByCreateDateBetweenAndUserIdAndStatus(@PathVariable String fromDate, @PathVariable String toDate, 
    		@PathVariable String userId,@PathVariable OneToOneInquiryType status, Pageable pageable) {
        return new ResponseEntity<>(oneToOneInquiryService.findByCreateDateBetweenAndUserIdAndStatus(fromDate, toDate, userId,status, pageable), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/status/{status}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<OneToOneInquiry>> findByCreateDateBetweenAndTitleLikeAndStatus(@PathVariable String fromDate, 
    		@PathVariable String toDate, @PathVariable String title,@PathVariable OneToOneInquiryType status, Pageable pageable) {
        return new ResponseEntity<>(oneToOneInquiryService.findByCreateDateBetweenAndTitleLikeAndStatus(fromDate, toDate, title,status, pageable), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<OneToOneInquiry>> findByCreateDateBetweenAndTitleLikeAndStatus(@PathVariable String fromDate, 
    		@PathVariable String toDate, @PathVariable String title, Pageable pageable) {
        return new ResponseEntity<>(oneToOneInquiryService.findByCreateDateBetweenAndTitleLike(fromDate, toDate, title, pageable), HttpStatus.OK);
    }
	
	
	
	@RequestMapping(value = "/id/{id}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity< Optional<OneToOneInquiry>> findById(@PathVariable long id, Pageable pageable) {
        return new ResponseEntity<>(oneToOneInquiryService.findById(id), HttpStatus.OK);
    }

}
