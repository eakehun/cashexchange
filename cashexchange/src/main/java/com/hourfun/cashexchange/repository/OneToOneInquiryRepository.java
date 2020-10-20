package com.hourfun.cashexchange.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryMini;

@Repository
@Transactional
public interface OneToOneInquiryRepository extends JpaRepository<OneToOneInquiry, Long>{

	public Page<OneToOneInquiry> findByCreateDateBetween(Date fromDate,Date toDate, 
			 Pageable pageable);
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date  from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" ,
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" ,
			nativeQuery = true)
	public Page<OneToOneInquiryMini> adminFindByCreateDateBetween(Date fromDate,Date toDate, 
			 Pageable pageable);
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date  from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and user_id = :userId \r\n" ,
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"and user_id = :userId \r\n" +
					"create_date between :fromDate and :toDate \r\n" ,
			nativeQuery = true)
	public Page<OneToOneInquiryMini> findByCreateDateBetweenAndUserId(Date fromDate,Date toDate, 
			String userId, Pageable pageable);
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date  from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and user_id = :userId and status=:status \r\n" ,
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"and user_id = :userId and status=:status \r\n" +
					"create_date between :fromDate and :toDate \r\n" ,
			nativeQuery = true)
	public Page<OneToOneInquiryMini> findByCreateDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, 
			String userId,String status, Pageable pageable);
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like CONCAT('%',:title,'%') and status = :status \r\n" ,
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like CONCAT('%',:title,'%') and status=:status ",
			nativeQuery = true)
	public Page<OneToOneInquiryMini> findByCreateDateBetweenAndTitleLikeAndStatus(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("status")String status, Pageable pageable);
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like  CONCAT('%',:title,'%')  \r\n" ,
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like  CONCAT('%',:title,'%')",
			nativeQuery = true)
	public Page<OneToOneInquiryMini> findByCreateDateBetweenAndTitleLike(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, Pageable pageable);
	
	
	
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date from cashExchange.one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and status = :status and user_id=:userId \r\n" + 
			"and title like %:title% or content like %:content% ",
			countQuery = "select count(*) from cashExchange.one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and status = :status and user_id=:userId \r\n" + 
					"and title like %:title% or content like %:content%  ",
			nativeQuery = true)
	Page<OneToOneInquiry> findByCreateDateBetweenAndUserIdAndStatusAndLikeTitleAndLikeContent
	(@Param("fromDate")String fromDate, @Param("toDate")String toDate,@Param("status") String status, 
			@Param("userId")String userId,@Param("title") String title, @Param("content")String content, Pageable pageable);
	
	@Query(value="select idx,user_id, user_name, title,status,create_date,response_date from cashExchange.one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and user_id=:userId \r\n" + 
			"and title like %:title% or content like %:content% ",
			countQuery = "select count(*) from cashExchange.one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and user_id=:userId \r\n" + 
					"and title like %:title% or content like %:content%  ",
			nativeQuery = true)
	Page<OneToOneInquiry> findByCreateDateBetweenAndUserIdAndLikeTitleAndLikeContent
	(@Param("fromDate")String fromDate, @Param("toDate")String toDate, 
			@Param("userId")String userId,@Param("title") String title, @Param("content")String content, Pageable pageable);
	
	
	
}
