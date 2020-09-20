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

import com.hourfun.cashexchange.model.Notice;
import com.hourfun.cashexchange.model.NoticeMini;
import com.hourfun.cashexchange.service.NoticeService;

@RestController
@RequestMapping("/admin/board/notice")
public class AdminNoticeController {
	@Autowired
	private NoticeService noticeService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetween( Pageable pageable){
		return new ResponseEntity<>(noticeService.findSpecificAll(pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/idx/{idx}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Notice> findByIdx(@PathVariable long idx){
		return new ResponseEntity<>(noticeService.findByIdx(idx), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetween(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetween(fromDate, toDate, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenLikeTitle(@PathVariable String fromDate,
			@PathVariable String toDate, 
			@PathVariable String title, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenLikeTitle(fromDate, toDate,title,pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenLikeContent(@PathVariable String fromDate,
			@PathVariable String toDate, 
			@PathVariable String content, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenLikeContent(fromDate, toDate ,content,pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndDisplay(fromDate, toDate ,display,pageable), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndTitleLikeAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable String title, 
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndTitleLikeAndDisplay(fromDate, toDate, title, display, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndContentLikeAndDisplay(@PathVariable String fromDate,
			@PathVariable String toDate,		@PathVariable String content, 
			@PathVariable Boolean display, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndContentLikeAndDisplay(fromDate, toDate, content, display, pageable), HttpStatus.OK);
	}
	
///////////////////
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndFixed(fromDate, toDate,  fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/fixed/{fixed}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndTitleLikeAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable String title, 
			@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndTitleLikeAndFixed(fromDate, toDate, title, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/fixed/{fixed}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndContentLikeAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable String content, 
			@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndContentLikeAndFixed(fromDate, toDate, content, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/fixed/{fixed}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndDisplayAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean display, 
			@PathVariable Boolean fixed, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndDisplayAndFixed(fromDate, toDate, display, fixed, pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/fixed/{fixed}/title/{title}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean display, 
			@PathVariable Boolean fixed, 
			@PathVariable String title, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(fromDate, toDate, title, display, fixed,  pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/display/{display}/fixed/{fixed}/content/{content}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<NoticeMini>> findByCreateDateBetweenAndContentLikeAndFixed(@PathVariable String fromDate,
			@PathVariable String toDate,
			@PathVariable Boolean display, 
			@PathVariable Boolean fixed, 
			@PathVariable String content, Pageable pageable){
		return new ResponseEntity<>(noticeService.findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(fromDate, toDate, content, display, fixed,  pageable), HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
	public ResponseEntity<Notice> insertFaq(@RequestBody Notice notice, Authentication auth) {
		if(notice.getIdx() >0l) {
			throw new IllegalArgumentException("Idx 값이 존재합니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(noticeService.save(notice,auth), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
	public ResponseEntity<Notice> updateFaq(@RequestBody Notice notice, Authentication auth) {
		if(notice.getIdx() < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		return new ResponseEntity<>(noticeService.save(notice,auth), HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteById(long idx) {
		if(idx < 1l) {
			throw new IllegalArgumentException("Idx 값이 없습니다. 확인해 주세요. ");
		}
		noticeService.deleteById(idx);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
