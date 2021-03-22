package com.hourfun.cashexchange.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TradingStatusEnum {

	PROGRESS("처리중"), WITHDRAWFAIL("입금오류"), PARTIAL_COMPLETE("판매완료(일부)"), FAIL("판매불가"), COMPLETE("판매완료"),
	ALREADY_ADDED("이미_등록된_상품권"), NO_BALANCE("잔액없음");

	private String value;

	public String getValue() {
		return value;
	}
}
