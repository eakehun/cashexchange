package com.hourfun.cashexchange.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Agreement;
@Repository
@Transactional
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

	@Query(value="select a.* from agreement a " + 
			"left join member_agreements ma "
			+ "on a.idx=ma.agreements_idx where ma.member_idx=:member_idx",
			countQuery = "select count(a.*) from agreement a " + 
					"left join member_agreements ma "
					+ "on a.idx=ma.agreements_idx where ma.member_idx=:member_idx",
			nativeQuery = true)
	Page<Agreement> findAllByMemberIdx(@Param("member_idx")long memberIdx,Pageable pageable);
	
	
	List<Agreement> findByUsed(boolean used);
	
	
}
