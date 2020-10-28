package com.hourfun.cashexchange.controller;

import java.util.List;

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

import com.hourfun.cashexchange.model.Notice;
import com.hourfun.cashexchange.service.NoticeService;

@RestController
@RequestMapping("/users/board/notice")
public class UsersNoticeController {

	@Autowired
	private NoticeService noticeService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<List<Page<Notice>>> findByAllList( Pageable pageable){
		
		return new ResponseEntity<>(noticeService.findByAllList(pageable), HttpStatus.OK);
	}
	@RequestMapping(value = "/fixed/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Notice>> findByFixed(Pageable pageable){
		return new ResponseEntity<>(noticeService.findByFixed(pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/notFixed/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Notice>> findByNotFixed(Pageable pageable){
		return new ResponseEntity<>(noticeService.findByNotFixed(pageable), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search/{search}/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<Page<Notice>> findByContentLikeAndTitleLike(@PathVariable String search,  Pageable pageable){
		
		return new ResponseEntity<>(noticeService.findByContentLikeAndTitleLike(search,pageable), HttpStatus.OK);
	}
}
