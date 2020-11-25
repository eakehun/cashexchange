package com.hourfun.cashexchange.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BankCode {
	
	KDB("산업은행", "002"),
	KB("기업은행", "003");

	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
}
