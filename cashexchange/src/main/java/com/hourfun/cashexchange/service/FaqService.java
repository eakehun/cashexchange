package com.hourfun.cashexchange.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.Faq;
import com.hourfun.cashexchange.model.FaqMiniFindAll;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.repository.FaqRepository;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class FaqService {

	@Autowired
	private FaqRepository faqRepository;
	
	@Autowired
	private UsersService usersService;
	
	public Page<FaqMiniFindAll> findByCreateDateBetween(String fromDate,String toDate, Pageable pageable){
		return faqRepository.findByCreateDateBetween(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenLikeTitle(String fromDate,String toDate, 
			 String title, Pageable pageable){
		return faqRepository.findByCreateDateBetweenLikeTitle(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),title,pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenLikeContent(String fromDate,String toDate, 
			 String content, Pageable pageable){
		return faqRepository.findByCreateDateBetweenLikeContent(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),content,pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndDisplay(String fromDate,String toDate,Boolean display, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndDisplay(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),display,pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndTitleLikeAndDisplay(String fromDate,String toDate
			, String title, Boolean display, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndTitleLikeAndDisplay(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), title, display, pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndContentLikeAndDisplay(String fromDate,String toDate
			, String content,Boolean display, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndContentLikeAndDisplay(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), content, display, pageable);
	}
	
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndFixed(String fromDate,String toDate,Boolean fixed, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), fixed, pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndTitleLikeAndFixed(String fromDate,String toDate
			,String title,Boolean fixed, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndTitleLikeAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), title, fixed, pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndContentLikeAndFixed(String fromDate,String toDate
			, String content,Boolean fixed, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndContentLikeAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), content, fixed, pageable);
	}
	
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndDisplayAndFixed(String fromDate,String toDate,
			Boolean display,Boolean fixed, Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndDisplayAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"),display, fixed, pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(String fromDate,String toDate
			, String title, Boolean display,
			Boolean fixed , Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), title, display, fixed, pageable);
	}
	
	public Page<FaqMiniFindAll> findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(String fromDate,String toDate
			, String content,Boolean display,
			Boolean fixed , Pageable pageable){
		return faqRepository.findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(DateUtils.changeStringToDate(fromDate, "yyyy-MM-dd HH:mm:ss"), 
				DateUtils.changeStringToDate(toDate, "yyyy-MM-dd HH:mm:ss"), content, display, fixed, pageable);
	}
	
	public Page<FaqMiniFindAll> findSpecificAll( Pageable pageable){
		return faqRepository.findSpecificAll(pageable);
	}
	
	public Faq findByIdx(long idx) {
		return faqRepository.findById(idx).get();
	}
	
	public Faq save(Faq faq,Authentication auth) {
		Users users = usersService.findByUserId(auth.getName());
		faq.setUserId(users.getUserId());
		return faqRepository.save(faq);
	}
	
	public void deleteById(long idx) {
		faqRepository.deleteById(idx);
	}
}
