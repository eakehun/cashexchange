package com.hourfun.cashexchange.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface TradingMini {

	
	long getIdx();
	@JsonProperty("createDate")
	Date getCreate_date();
	@JsonProperty("userId")
	String getUser_id();
	@JsonProperty("userName")
	String getUser_name();
	String getCompany();
	String getStatus();
	
	@JsonProperty("requestPrice")
	String getRequest_price();
	@JsonProperty("comepletePrice")
	String getComplate_price();
	
	String getFees();
	
//private String company;
//	
//	private String status;
//	
//	private String requestPrice;
//	
//	private String fees;
//	
//	private String comepletePrice;
}
