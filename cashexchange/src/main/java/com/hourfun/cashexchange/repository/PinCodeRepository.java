package com.hourfun.cashexchange.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.PinCode;

@Repository
@Transactional
public interface PinCodeRepository extends JpaRepository<PinCode, Long>{

}
