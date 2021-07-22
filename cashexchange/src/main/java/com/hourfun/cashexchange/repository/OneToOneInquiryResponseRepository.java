package com.hourfun.cashexchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.OneToOneInquiryResponse;


@Repository
public interface OneToOneInquiryResponseRepository extends JpaRepository<OneToOneInquiryResponse, Long>{
	@Query(value="select * from one_to_one_inquiry_response where parent_idx=:parentIdx",
			nativeQuery = true)
	public List<OneToOneInquiryResponse> findByParentIdx(@Param("parentIdx") long parentIdx);
}
