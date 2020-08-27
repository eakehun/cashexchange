package com.hourfun.cashexchange.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
	
	private String id;
	private String pwd;
	
	private String name;
	
	private String tel;
	private String mobileOperator;
	
	private String accountNum;
	private String accountName;
	
	private String ci;
	
	private Date createDate;
	
}
