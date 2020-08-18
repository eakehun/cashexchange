package com.hourfun.cashexchange.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Member;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Long>{

	
	
}
