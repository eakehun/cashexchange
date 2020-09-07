package com.hourfun.cashexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.service.OneToOneInquiryService;

@RestController
@RequestMapping("/users/board/OneToOne")
public class UserOneToOneInquiryController {

	
	@Autowired
	private OneToOneInquiryService oneToOneInquiryService;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OneToOneInquiry> insertOneToOne(@RequestBody OneToOneInquiry oneInquiry) {
		if(oneInquiry.getIdx() >0l) {
			throw new IllegalArgumentException("Idx value exists. Please check ..");
		}
		OneToOneInquiry result = oneToOneInquiryService.save(oneInquiry);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<OneToOneInquiry> updateOneToOne(@RequestBody OneToOneInquiry oneInquiry) {
		if(oneInquiry.getIdx() <1l) {
			throw new IllegalArgumentException("Idx value doesn't exists. Please check ..");
		}
		OneToOneInquiry result = oneToOneInquiryService.save(oneInquiry);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
