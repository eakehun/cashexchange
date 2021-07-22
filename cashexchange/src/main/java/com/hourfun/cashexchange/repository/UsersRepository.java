package com.hourfun.cashexchange.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Users;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users, Long> {

	Users findByIdx(Long idx);
	
	
	@Query(value = "SELECT \r\n" + 
			"idx,\r\n" + 
			"account_name,\r\n" + 
			"account_num,\r\n" + 
			"account_status,\r\n" + 
			"auth,\r\n" + 
			"back_code,\r\n" + 
			"birth,\r\n" + 
			"ci,\r\n" + 
			"create_date,\r\n" + 
			"email,\r\n" + 
			"gender,\r\n" + 
			"mobile_operator,\r\n" + 
			"name,\r\n" + 
			"pwd,\r\n" + 
			"tel,\r\n" + 
			"tel_chk_value,\r\n" + 
			"update_date,\r\n" + 
			"user_id,\r\n" + 
			"foreigner,\r\n" + 
			"account_code\r\n" + 
			"FROM users where user_id = :userId",
			nativeQuery = true)
	Users findByUserIdWithoutAgreement(String userId);
	
	Users findByUserId(String userId);
	
	/*
	 * SELECT * FROM users where user_id = ?;
	 */
	
	Users findByCi(String ci);

	Users findByTel(String tel);

	Users findByUserIdAndTel(String userId, String tel);

//	Page<Users> findAll(Pageable pageable); 

	Page<Users> findByCreateDateBetween(Date fromDate, Date toDate, Pageable pageable);
	List<Users> findByCreateDateBetween(Date fromDate, Date toDate);

	Page<Users> findByAccountStatus(String accountStatus, Pageable pageable);
	List<Users> findByAccountStatus(String accountStatus);

	Page<Users> findByCreateDateBetweenAndAccountStatus(Date fromDate, Date toDate, String accountStatus,
			Pageable pageable);
	
	List<Users> findByCreateDateBetweenAndAccountStatus(Date fromDate, Date toDate, String accountStatus);

	Page<Users> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId, Pageable pageable);
	List<Users> findByCreateDateBetweenAndUserId(Date fromDate, Date toDate, String userId);

	Page<Users> findByCreateDateBetweenAndName(Date fromDate, Date toDate, String name, Pageable pageable);
	List<Users> findByCreateDateBetweenAndName(Date fromDate, Date toDate, String name);

	Page<Users> findByCreateDateBetweenAndTel(Date fromDate, Date toDate, String tel, Pageable pageable);
	List<Users> findByCreateDateBetweenAndTel(Date fromDate, Date toDate, String tel);

	Page<Users> findByCreateDateBetweenAndIdx(Date fromDate, Date toDate, long idx, Pageable pageable);
	List<Users> findByCreateDateBetweenAndIdx(Date fromDate, Date toDate, long idx);

	Page<Users> findByCreateDateBetweenAndUserIdAndAccountStatus(Date fromDate, Date toDate, String userId,
			String accountStatus, Pageable pageable);
	List<Users> findByCreateDateBetweenAndUserIdAndAccountStatus(Date fromDate, Date toDate, String userId,
			String accountStatus);

	Page<Users> findByCreateDateBetweenAndNameAndAccountStatus(Date fromDate, Date toDate, String name,
			String accountStatus, Pageable pageable);
	List<Users> findByCreateDateBetweenAndNameAndAccountStatus(Date fromDate, Date toDate, String name,
			String accountStatus);

	Page<Users> findByCreateDateBetweenAndTelAndAccountStatus(Date fromDate, Date toDate, String tel,
			String accountStatus, Pageable pageable);
	List<Users> findByCreateDateBetweenAndTelAndAccountStatus(Date fromDate, Date toDate, String tel,
			String accountStatus);

	Page<Users> findByCreateDateBetweenAndIdxAndAccountStatus(Date fromDate, Date toDate, long idx,
			String accountStatus, Pageable pageable);

	List<Users> findByCreateDateBetweenAndIdxAndAccountStatus(Date fromDate, Date toDate, long idx,
			String accountStatus);

}
