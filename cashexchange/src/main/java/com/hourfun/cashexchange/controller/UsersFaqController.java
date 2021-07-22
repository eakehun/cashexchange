package com.hourfun.cashexchange.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hourfun.cashexchange.model.Faq;
import com.hourfun.cashexchange.service.FaqService;

@RestController
@RequestMapping("/users/board/faq")
public class UsersFaqController {

	@Autowired
	private FaqService faqService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByFixed( Pageable pageable){
		
		return new ResponseEntity<>(faqService.findByDisplay(pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search/{search}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByContentLikeAndTitleLike(@PathVariable String search,  Pageable pageable){
		
		return new ResponseEntity<>(faqService.findByContentLikeAndTitleLike(search,pageable), HttpStatus.OK);
	}
}
