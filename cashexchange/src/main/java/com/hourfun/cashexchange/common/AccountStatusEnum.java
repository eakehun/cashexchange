package com.hourfun.cashexchange.common;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum AccountStatusEnum {
	SUSPENDED("정지"), WITHDRAW("탈퇴");

	private String value;
	
	public String getValue() {
		return value;
	}

}
