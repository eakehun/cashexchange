package com.hourfun.cashexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.TradingMenu;

@Repository
public interface TradingMenuRepository extends JpaRepository<TradingMenu, Long> {

	TradingMenu findByMenuName(String menuName);
}
