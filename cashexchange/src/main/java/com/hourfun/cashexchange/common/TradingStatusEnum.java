package com.hourfun.cashexchange.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TradingStatusEnum {

	PROGRESS("처리중"), WITHDRAWFAIL("입금오류"), PARTIAL_COMPLETE("판매완료(일부)"), FAIL("판매불가"), COMPLETE("판매완료"),
	ALREADY_ADDED("이미_등록된_문화상품권");

	private String value;

	public String getValue() {
		return value;
	}
}
