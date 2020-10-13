package com.hourfun.cashexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hourfun.cashexchange.model.Trading;

public interface TradingRepository extends JpaRepository<Trading, Long> {

}
