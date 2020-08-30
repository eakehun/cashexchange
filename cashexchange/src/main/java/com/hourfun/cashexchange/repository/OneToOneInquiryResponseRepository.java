package com.hourfun.cashexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.OneToOneInquiryResponse;


@Repository
public interface OneToOneInquiryResponseRepository extends JpaRepository<OneToOneInquiryResponse, Long>{

}
