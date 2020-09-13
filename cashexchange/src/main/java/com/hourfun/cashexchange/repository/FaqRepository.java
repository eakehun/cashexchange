package com.hourfun.cashexchange.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hourfun.cashexchange.model.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>{

	public Page<Faq> findByCreateDateBetween(Date fromDate,Date toDate, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like '%'||:title||'%' \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like '%'||:title||'%' ",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenLikeTitle(@Param("fromDate") Date fromDate,@Param("fromDate") Date toDate, 
			@Param("title") String title, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like '%'||:content||'%' \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like '%'||:content||'%' ",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenLikeContent(@Param("fromDate") Date fromDate,@Param("fromDate") Date toDate, 
			@Param("content") String content, Pageable pageable);
	
	public Page<Faq> findByCreateDateBetweenAndDisplay(Date fromDate,Date toDate,Boolean display, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like '%'||:title||'%' and display = :display \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like '%'||:title||'%' and display=:display ",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenAndTitleLikeAndDisplay(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("display")Boolean display, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like '%'||:content||'%' and display = :display \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like '%'||:content||'%' and display=:display ",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenAndContentLikeAndDisplay(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("display")Boolean display, Pageable pageable);
	
	
	public Page<Faq> findByCreateDateBetweenAndFixed(Date fromDate,Date toDate,Boolean fixed, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like '%'||:title||'%' and fixed = :fixed \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like '%'||:title||'%' and fixed=:fixed ",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenAndTitleLikeAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("fixed")Boolean fixed, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like '%'||:content||'%' and fixed = :fixed \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like '%'||:content||'%' and fixed=:fixed ",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenAndContentLikeAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("fixed")Boolean fixed, Pageable pageable);
	
	
	public Page<Faq> findByCreateDateBetweenAndDisplayAndFixed(Date fromDate,Date toDate,Boolean display,Boolean fixed, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like '%'||:title||'%' and display = :display and  fixed = :fixed \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like '%'||:title||'%' and display= :display and  fixed = :fixed",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("display")Boolean display,
			@Param("fixed")Boolean fixed , Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like '%'||:content||'%' and display = :display and  fixed = :fixed \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like '%'||:content||'%' and display=:display and  fixed = :fixed",
			nativeQuery = true)
	public Page<Faq> findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("display")Boolean display,
			@Param("fixed")Boolean fixed , Pageable pageable);
	
	
	
}
