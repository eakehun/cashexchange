package com.hourfun.cashexchange.controller;

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

import com.hourfun.cashexchange.model.Faq;
import com.hourfun.cashexchange.model.FaqMini;
import com.hourfun.cashexchange.service.FaqService;

@RestController
@RequestMapping("/admin/board/faq")
public class AdminFaqController {
	@Autowired
	private FaqService faqService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetween( Pageable pageable){
		return new ResponseEntity<>(faqService.findSpecificAll(pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/idx/{idx}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Faq> findByIdx(@PathVariable long idx){
		return new ResponseEntity<>(faqService.findByIdx(idx), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetween(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetween(fromDate, toDate, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetweenLikeTitle(@PathVariable String fromDate,
			@PathVariable String toDate, 
			@PathVariable String title, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenLikeTitle(fromDate, toDate,title,pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetweenLikeContent(@PathVariable String fromDate,
			@PathVariable String toDate, 
			@PathVariable String content, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenLikeContent(fromDate, toDate ,content,pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetweenAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndDisplay(fromDate, toDate ,display,pageable), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetweenAndTitleLikeAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable String title, 
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndTitleLikeAndDisplay(fromDate, toDate, title, display, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<FaqMini>> findByCreateDateBetweenAndContentLikeAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,		@PathVariable String content, 
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndContentLikeAndDisplay(fromDate, toDate, content, display, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
	public ResponseEntity<Faq> insertFaq(@RequestBody Faq faq, Authentication auth) {
		if(faq.getIdx() >0l) {
			throw new IllegalArgumentException("Idx 값이 존재합니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(faqService.save(faq,auth), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
	public ResponseEntity<Faq> updateFaq(@RequestBody Faq faq, Authentication auth) {
		if(faq.getIdx() < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(faqService.save(faq,auth), HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteById(long idx) {
		if(idx < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		faqService.deleteById(idx);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
