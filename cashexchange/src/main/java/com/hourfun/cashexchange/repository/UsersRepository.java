package com.hourfun.cashexchange.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.model.Users;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users, Long>{

	
	Users findById(String id);
	
	Users findByTel(String tel);
	
	Users findByIdAndTel(String id, String tel);
	
	
//	@Query(value="select email, create_date from users"
//			+ "where tel = :tel",
//			nativeQuery = true)
//	Users findByTel(@Param("tel")String tel);
	
}
