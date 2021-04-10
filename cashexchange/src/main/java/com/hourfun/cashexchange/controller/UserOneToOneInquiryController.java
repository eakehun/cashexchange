package com.hourfun.cashexchange.controller;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryResponse;
import com.hourfun.cashexchange.service.OneToOneInquiryReponseService;
import com.hourfun.cashexchange.service.OneToOneInquiryService;

@RestController
@RequestMapping("/users/board/OneToOne")
public class UserOneToOneInquiryController {

	
	@Autowired
	private OneToOneInquiryService oneToOneInquiryService;
	
	@Autowired
	private OneToOneInquiryReponseService oneToOneInquiryResponseService;
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<OneToOneInquiry>> findByCreateDateBetween(Authentication auth, @PathVariable String fromDate, @PathVariable String toDate, Pageable pageable){
		
		return new ResponseEntity<>(oneToOneInquiryService.findByCreateDateBetweenAndUserId(fromDate, toDate, auth.getName(), pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/response/parentIdx/{parentIdx}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<OneToOneInquiryResponse>> findByParentIdx(@PathVariable long parentIdx) {
		if(parentIdx < 1l) {
			throw new IllegalArgumentException("Idx value doesn't exists. Please check ..");
		}
		
        return new ResponseEntity<>(oneToOneInquiryResponseService.findByParentIdx(parentIdx), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OneToOneInquiry> insertOneToOne(@RequestBody OneToOneInquiry oneInquiry,   Authentication auth) {
		if(oneInquiry.getIdx() >0l) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Idx value exists. Please check ..");
		}
		OneToOneInquiry result = oneToOneInquiryService.save(oneInquiry,auth);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<OneToOneInquiry> updateOneToOne(@RequestBody OneToOneInquiry oneInquiry,   Authentication auth) {
		if(oneInquiry.getIdx() <1l) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Idx value doesn't exists. Please check ..", new IllegalArgumentException());
		}
		OneToOneInquiry result = oneToOneInquiryService.save(oneInquiry, auth);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
