package com.hourfun.cashexchange.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

	Page<History> findByType(Pageable pageRequest, String type);
	
	Page<History> findByService(Pageable pageRequest, String service);
	Page<History> findByServiceAndKeyword(Pageable pageRequest, String service, String keyword);
	
	@Query(value="SELECT * FROM history where service = :service and keyword like :keyword",
			nativeQuery = true)
	Page<History> findByServiceAndKeywordLike(Pageable pageRequest, String service, String keyword);
	
	
	Page<History> findByServiceAndCreateDateBetween(Pageable pageRequest, String service, Date fromDate, Date toDate);
	Page<History> findByServiceAndKeywordAndCreateDateBetween(Pageable pageRequest, String service, String keyword, Date fromDate, Date toDate);
	
	@Query(value="SELECT * FROM history where service = :service and create_date between :fromDate and :toDate and keyword like :keyword",
			nativeQuery = true)
	Page<History> findByServiceAndCreateDateBetweenAndKeywordLike(Pageable pageRequest, String service, Date fromDate, Date toDate, String keyword);
	

	History findTopByUserAndTypeOrderByCreateDateDesc(String user, String type);
	
	
}
