package com.hourfun.cashexchange.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hourfun.cashexchange.model.Notice;
import com.hourfun.cashexchange.model.NoticeMini;

public interface NoticeRepository extends JpaRepository<Notice, Long>{

	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate " ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetween(Date fromDate,Date toDate, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenLikeTitle(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate, 
			@Param("title") String title, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content CONCAT('%',:content,'%') ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenLikeContent(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate, 
			@Param("content") String content, Pageable pageable);
	
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n"+ 
			"and display = :display \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					" and display=:display ",
					nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndDisplay(Date fromDate,Date toDate,Boolean display, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like  CONCAT('%',:title,'%') and display = :display \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and display=:display ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndTitleLikeAndDisplay(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("display")Boolean display, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') and display = :display \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') and display=:display ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndContentLikeAndDisplay(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("display")Boolean display, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and fixed = :fixed \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and fixed=:fixed ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndFixed(Date fromDate,Date toDate,Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed, create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') and fixed = :fixed \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and fixed=:fixed ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndTitleLikeAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("fixed")Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed,create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') and fixed = :fixed \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') and fixed=:fixed ",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndContentLikeAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("fixed")Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed,create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and display = :display and  fixed = :fixed \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and display= :display and  fixed = :fixed",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndDisplayAndFixed(Date fromDate,Date toDate,Boolean display,Boolean fixed, Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed,create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') and display = :display and  fixed = :fixed \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and display= :display and  fixed = :fixed",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndTitleLikeAndDisplayAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("display")Boolean display,
			@Param("fixed")Boolean fixed , Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed,create_date,update_date from notice where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and content like CONCAT('%',:content,'%') and display = :display and  fixed = :fixed \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and content like CONCAT('%',:content,'%') and display=:display and  fixed = :fixed",
			nativeQuery = true)
	public Page<NoticeMini> findByCreateDateBetweenAndContentLikeAndDisplayAndFixed(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("content") String content, @Param("display")Boolean display,
			@Param("fixed")Boolean fixed , Pageable pageable);
	
	@Query(value="select idx, user_id, title, display,fixed,create_date,update_date from notice  " ,
			countQuery = "select count(*) from notice ",
			nativeQuery = true)
	public Page<NoticeMini> findSpecificAll( Pageable pageable);
	
	
	
	public Page<Notice> findByFixedAndDisplay(Boolean fixed,Boolean display, Pageable pageable);
	
	@Query(value="select * from ( select * from notice where \r\n" + 
			"(content like CONCAT('%',:param,'%') or title like CONCAT('%',:param,'%') ) "
			+ "and display = true order by idx desc) as temp \r\n" ,
			countQuery = "select count(*) from notice where \r\n" + 
					"(content like CONCAT('%',:param,'%') or title like CONCAT('%',:param,'%') ) "
					+ "and display = true",
			nativeQuery = true)
	public Page<Notice> findByContentLikeAndTitleLike(@Param("param")String param , Pageable pageable);

	
	
	
}
