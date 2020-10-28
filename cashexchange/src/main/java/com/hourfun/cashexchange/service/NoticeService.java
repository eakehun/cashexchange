package com.hourfun.cashexchange.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.Notice;
import com.hourfun.cashexchange.model.NoticeMini;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.NoticeRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private UsersService usersService;
	
	//client 리스트만 보이는 서비스 
	public List<Page<Notice>> findByAllList(Pageable pageable){
		PageRequest page = PageRequest.of(0,5,Sort.by(Sort.Direction.DESC, "idx"));
		Page<Notice> fixedPage = noticeRepository.findByFixedAndDisplay(true,true, page);
		Page<Notice> notFixedPage = noticeRepository.findByFixedAndDisplay(false,true, pageable);
		List<Page<Notice>> list = new ArrayList<>();
		list.add(fixedPage);
		list.add(notFixedPage);
		return list;
	}
	
	public Page<Notice> findByFixed(Pageable pageable){
		Page<Notice> fixedPage = noticeRepository.findByFixedAndDisplay(true,true, pageable);
		return fixedPage;
	}
	
	public Page<Notice> findByNotFixed(Pageable pageable){
		Page<Notice> notFixedPage = noticeRepository.findByFixedAndDisplay(false,true, pageable);
		return notFixedPage;
	}
	
	public Page<Notice> findByContentLikeAndTitleLike(String param, Pageable pageable){
		Page<Notice> noticePage = noticeRepository.findByContentLikeAndTitleLike(param, pageable);
		if(noticePage.getSize() == 0 ) {
			PageRequest page = PageRequest.of(0,5,Sort.by(Sort.Direction.DESC, "idx"));
			return noticeRepository.findByFixedAndDisplay(true,true, page);
		}
		return noticePage;
	}
	
	public Page<NoticeMini> findByCreateDateBetween(String fromDate,String toDate, Pageable pageable){
		return noticeRepository.findByCreateDateBetween(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenLikeTitle(String fromDate,String toDate, 
			 String title, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenLikeTitle(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),title,pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenLikeContent(String fromDate,String toDate, 
			 String content, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenLikeContent(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),content,pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndDisplay(String fromDate,String toDate,Boolean display, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndDisplay(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),display,pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndTitleLikeAndDisplay(String fromDate,String toDate
			, String title, Boolean display, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndTitleLikeAndDisplay(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), title, display, pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndContentLikeAndDisplay(String fromDate,String toDate
			, String content,Boolean display, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndContentLikeAndDisplay(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), content, display, pageable);
	}
	
	
	public Page<NoticeMini> findByCreateDateBetweenAndFixed(String fromDate,String toDate,Boolean fixed, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), fixed, pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndTitleLikeAndFixed(String fromDate,String toDate
			,String title,Boolean fixed, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndTitleLikeAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), title, fixed, pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndContentLikeAndFixed(String fromDate,String toDate
			, String content,Boolean fixed, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndContentLikeAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), content, fixed, pageable);
	}
	
	
	public Page<NoticeMini> findByCreateDateBetweenAndDisplayAndFixed(String fromDate,String toDate,
			Boolean display,Boolean fixed, Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndDisplayAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),display, fixed, pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(String fromDate,String toDate
			, String title, Boolean display,
			Boolean fixed , Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), title, display, fixed, pageable);
	}
	
	public Page<NoticeMini> findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(String fromDate,String toDate
			, String content,Boolean display,
			Boolean fixed , Pageable pageable){
		return noticeRepository.findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), content, display, fixed, pageable);
	}
	
	public Page<NoticeMini> findSpecificAll( Pageable pageable){
		return noticeRepository.findSpecificAll(pageable);
	}
	
	public Notice findByIdx(long idx) {
		return noticeRepository.findById(idx).get();
	}
	
	public Notice save(Notice notice,Authentication auth) {
		Users users = usersService.findByUserId(auth.getName());
		notice.setUserId(users.getUserId());
		return noticeRepository.save(notice);
	}
	
	public void deleteById(long idx) {
		noticeRepository.deleteById(idx);
	}
}
