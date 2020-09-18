package com.hourfun.cashexchange.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface FaqMini {
	long getIdx();
	@JsonProperty("createDate")
	Date getCreate_date();
	@JsonProperty("updateDate")
	Date getUpdate_date();
	@JsonProperty("userId")
	String getUser_id();
	String getTitle();
    Boolean getDisplay();
}
