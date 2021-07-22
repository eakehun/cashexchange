package com.hourfun.cashexchange.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface OneToOneInquiryMini {

	long getIdx();
	@JsonProperty("createDate")
	Date getCreate_date();
	@JsonProperty("responseDate")
	Date getResponse_date();
	@JsonProperty("userId")
	String getUser_id();
	@JsonProperty("userName")
	String getUser_name();
	String getTitle();
	String getStatus();
	String getTel();
	
}
