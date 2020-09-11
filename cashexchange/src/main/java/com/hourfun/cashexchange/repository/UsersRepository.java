package com.hourfun.cashexchange.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.OneToOneInquiry;
import com.hourfun.cashexchange.model.Users;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users, Long> {

	Users findByUserId(String userId);

	Users findByTel(String tel);

	Users findByUserIdAndTel(String userId, String tel);

//	Page<Users> findAll(Pageable pageable); 

	Page<Users> findByCreateDateBetween(Date fromDate, Date toDate, Pageable pageable);

	Page<Users> findByAccountStatus(String accountStatus, Pageable pageable);

	Page<Users> findByCreateDateBetweenAndAccountStatus(Date fromDate, Date toDate, String accountStatus,
			Pageable pageable);


}
