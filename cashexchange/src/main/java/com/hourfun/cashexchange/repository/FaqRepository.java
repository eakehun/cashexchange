package com.hourfun.cashexchange.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hourfun.cashexchange.model.Faq;
import com.hourfun.cashexchange.model.FaqMini;

public interface FaqRepository extends JpaRepository<Faq, Long>{

	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate " ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetween(Date fromDate,Date toDate, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenLikeTitle(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate, 
			@Param("title") String title, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenLikeContent(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate, 
			@Param("content") String content, Pageable pageable);
	
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n"+ 
			"and display = :display \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					" and display=:display ",
					nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndDisplay(Date fromDate,Date toDate,Boolean display, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') and display = :display \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and display=:display ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndTitleLikeAndDisplay(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("display")Boolean display, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') and display = :display \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') and display=:display ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndContentLikeAndDisplay(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("display")Boolean display, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and fixed = :fixed \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and fixed=:fixed ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndFixed(Date fromDate,Date toDate,Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') and fixed = :fixed \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and fixed=:fixed ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndTitleLikeAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("fixed")Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') and fixed = :fixed \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') and fixed=:fixed ",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndContentLikeAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("fixed")Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and display = :display and  fixed = :fixed \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and display= :display and  fixed = :fixed",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndDisplayAndFixed(Date fromDate,Date toDate,Boolean display,Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') and display = :display and  fixed = :fixed \r\n" ,
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and display= :display and  fixed = :fixed",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("display")Boolean display,
			@Param("fixed")Boolean fixed , Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') and display = :display and  fixed = :fixed \r\n", 
			countQuery = "select count(*) from faq where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') and display=:display and  fixed = :fixed",
			nativeQuery = true)
	public Page<FaqMini> findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("display")Boolean display,
			@Param("fixed")Boolean fixed , Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,create_date,update_date from faq " ,
			countQuery = "select count(*) from faq ",
			nativeQuery = true)
	public Page<FaqMini> findSpecificAll( Pageable pageable);

	
	
	public Page<Faq> findByDisplay(Boolean display, Pageable pageable);
	
	@Query(value="select * from faq where \r\n" + 
			"(content like CONCAT('%',:param,'%') or title like CONCAT('%',:param,'%') ) "
			+ "and display = true \r\n", 
			countQuery = "select count(*) from faq where \r\n" + 
					"(content like CONCAT('%',:param,'%') or title like CONCAT('%',:param,'%') ) "
					+ "and display = true",
			nativeQuery = true)
	public Page<Faq> findByContentLikeAndTitleLike(@Param("param")String param , Pageable pageable);

	
	
}
