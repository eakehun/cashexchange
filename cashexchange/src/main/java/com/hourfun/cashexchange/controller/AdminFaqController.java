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
import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.service.FaqService;

@RestController
@RequestMapping("/admin/board/faq")
public class AdminFaqController {
	@Autowired
	private FaqService faqService;
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetween(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetween(fromDate, toDate, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenLikeTitle(@PathVariable String fromDate,
			@PathVariable String toDate, 
			@PathVariable String title, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenLikeTitle(fromDate, toDate,title,pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenLikeContent(@PathVariable String fromDate,
			@PathVariable String toDate, 
			@PathVariable String content, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenLikeContent(fromDate, toDate ,content,pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndDisplay(fromDate, toDate ,display,pageable), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/display/{display}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndTitleLikeAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable String title, 
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndTitleLikeAndDisplay(fromDate, toDate, title, display, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/content/{content}/display/{display}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndContentLikeAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,		@PathVariable String content, 
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndContentLikeAndDisplay(fromDate, toDate, content, display, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndFixed(fromDate, toDate, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndTitleLikeAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate ,@PathVariable String title,@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndTitleLikeAndFixed(fromDate, toDate, title, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/content/{content}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndContentLikeAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String content,@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndContentLikeAndFixed(fromDate, toDate, content, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndDisplayAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,@PathVariable	Boolean display,
			@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndDisplayAndFixed(fromDate, toDate,display, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/display/{display}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate	,@PathVariable String title,@PathVariable Boolean display,
			@PathVariable Boolean fixed , Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(fromDate, toDate , title, display, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/content/{content}/display/{display}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Faq>> findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,@PathVariable String content, @PathVariable Boolean display,
			@PathVariable Boolean fixed , Pageable pageable){
		return new ResponseEntity<>(faqService.findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(fromDate, toDate, content, display, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
	public ResponseEntity<Faq> insertFaq(Faq faq) {
		if(faq.getIdx() >0l) {
			throw new IllegalArgumentException("Idx 값이 존재합니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(faqService.save(faq), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
	public ResponseEntity<Faq> updateFaq(Faq faq) {
		if(faq.getIdx() < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(faqService.save(faq), HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteById(long idx) {
		if(idx < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		faqService.deleteById(idx);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
