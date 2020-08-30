package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.OneToOneInquiryType;

@Repository
@Transactional
public interface OneToOneInquiryRepository extends JpaRepository<OneToOneInquiry, Long>{

	public Page<OneToOneInquiry> findByCreateDateBetweenAndUserId(Date fromDate,Date toDate, 
			String userId, Pageable pageable);
	
	public Page<OneToOneInquiry> findByCreateDateBetweenAndUserIdAndStatus(Date fromDate, Date toDate, 
			String userId,OneToOneInquiryType status, Pageable pageable);
	
	@Query(value="select * from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like '%'||:title||'%' and status = :status \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like '%'||:title||'%' and status=:status ",
			nativeQuery = true)
	public Page<OneToOneInquiry> findByCreateDateBetweenAndTitleLikeAndStatus(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, @Param("status")OneToOneInquiryType status, Pageable pageable);
	
	@Query(value="select * from one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and title like  '%'||:title||'%'  \r\n" + 
			"\n#pageable\n",
			countQuery = "select count(*) from one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and title like  '%'||:title||'%'",
			nativeQuery = true)
	public Page<OneToOneInquiry> findByCreateDateBetweenAndTitleLike(@Param("fromDate")Date fromDate, 
			@Param("toDate")Date toDate,@Param("title") String title, Pageable pageable);
	
	
	
	
	@Query(value="select * from cashExchange.one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and status = :status and user_id=:userId \r\n" + 
			"and title like %:title% or content like %:content%  \n#pageable\n",
			countQuery = "select count(*) from cashExchange.one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and status = :status and user_id=:userId \r\n" + 
					"and title like %:title% or content like %:content%  ",
			nativeQuery = true)
	Page<OneToOneInquiry> findByCreateDateBetweenAndUserIdAndStatusAndLikeTitleAndLikeContent
	(@Param("fromDate")String fromDate, @Param("toDate")String toDate,@Param("status") String status, 
			@Param("userId")String userId,@Param("title") String title, @Param("content")String content, Pageable pageable);
	
	@Query(value="select * from cashExchange.one_to_one_inquiry where \r\n" + 
			"create_date between :fromDate and :toDate \r\n" + 
			"and user_id=:userId \r\n" + 
			"and title like %:title% or content like %:content%  \n#pageable\n",
			countQuery = "select count(*) from cashExchange.one_to_one_inquiry where \r\n" + 
					"create_date between :fromDate and :toDate \r\n" + 
					"and user_id=:userId \r\n" + 
					"and title like %:title% or content like %:content%  ",
			nativeQuery = true)
	Page<OneToOneInquiry> findByCreateDateBetweenAndUserIdAndLikeTitleAndLikeContent
	(@Param("fromDate")String fromDate, @Param("toDate")String toDate, 
			@Param("userId")String userId,@Param("title") String title, @Param("content")String content, Pageable pageable);
	
	
	
}
