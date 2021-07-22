package com.hourfun.cashexchange.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Agreement;
@Repository
@Transactional
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

	@Query(value="select a.* from agreement a " + 
			"left join user_agreements ua "
			+ "on a.idx=ua.agreements_idx where ua.users_idx=:member_idx",			
			nativeQuery = true)
	List<Agreement> findAllByMemberIdx(@Param("member_idx")long memberIdx);
	
	
	List<Agreement> findByUsed(boolean used);
	
	
}
