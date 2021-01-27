package com.hourfun.cashexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.HistoryPinCode;

@Repository
public interface HistoryPinCodeRepository extends JpaRepository<HistoryPinCode, Long> {

}
